<template>
  <!-- 通知单模板管理 -->
  <div class="formwork">
    <ss-condition-area definedTitle="通知单模板管理" ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row newselect">
        <div class="item">
          <div class="label"><span style="color: red; margin-right: 6px;">*</span>选择地区：</div>
          <ss-area-areaTreeStation class="area" :disabled="$store.state.modelObject==1 ?true:false" :showSelect="false" ref="areaTreePage" :getToLevelNum="5 - currentLevel"
            @nodeClick="nodeClick" />
        </div>
        <div class="item">
          <div class="label"><span style="color: red; margin-right: 6px;">*</span>学校类型：</div>
          <el-select v-model="unitManageForm.checkType" placeholder="请选择学校类型" clearable>
            <el-option label="幼托机构" value="0" />
            <el-option label="小学" value="1" />
          </el-select>
        </div>
        <div class="item">
          <div class="label"><span style="color: red; margin-right: 6px;">*</span>学校名称：</div>
          <el-select style="width:200px;" @change="changeSchool" filterable v-model="unitManageForm.schoolId"
            placeholder="请选择学校" clearable>
            <el-option style="width:200px;" v-for="item in schoolList" :key="item.schId" :label="item.schName"
              :value="item.schId" />
          </el-select>
        </div>
      </div>
      <div class="mt_10" style="display: flex;">
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
      </div>
      <!-- v-if="formData.content" -->
      <el-tabs v-model="activeName" type="card" @tab-click="handleClick" v-if="unitManageForm.schoolId && isShowContent">
        <el-tab-pane label="补种通知单模板" name="0">
          <el-row :gutter="20" v-if="activeName == 0">
            <el-col :span="12">
              <formwork title="补种通知单模板（信息添加前）" :formData="formData"></formwork>
            </el-col>
            <el-col :span="12">
              <formwork title="补种通知单模板（信息添加后）" :formData="formData" :afterInfo="true"></formwork>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="补证通知单模板" name="1">
          <el-row :gutter="20" v-if="activeName == 1">
            <el-col :span="12">
              <formwork title="补证通知单模板（手动添加模块）" :formData="formData"></formwork>
            </el-col>
            <el-col :span="12">
              <formwork title="补证通知单模板（信息添加后）" :afterInfo="true" :formData="formData"></formwork>
            </el-col>
          </el-row>
        </el-tab-pane>
        <!-- <el-tab-pane label="建档通知单模板" name="2">
          <el-row :gutter="20" v-if="activeName == 2">
            <el-col :span="12">
              <filing-notice title="建档通知单模板（信息添加前）" :isEdit="true" :formData="fillingData">
                <template slot="edit">
                  <div class="btn" @click="editHandler(2)">
                    <el-button>保存编辑内容</el-button>
                  </div>
                </template>
              </filing-notice>
            </el-col>
            <el-col :span="12">
              <filing-notice title="建档通知单模板（信息添加后）" :isAfter="true" :isEdit="true" :formData="fillingData">
                <template slot="edit">
                  <div class="btn" @click="editHandler(2)">
                    <el-button>保存编辑内容</el-button>
                  </div>
                </template>
              </filing-notice>
            </el-col>
          </el-row>
        </el-tab-pane> -->
      </el-tabs>
    </ss-condition-area>
  </div>
</template>
<script>
import commonOptions from '@/mixin/commonOptions'
import formwork from './formworkNew'
import { getLevel } from '@/utils/common/area.js'
import { findSchoolByAreaAndSchType } from '@/utils/axios/api'
import { upNoticeMess, queryNoticeMess } from '@/utils/axios/apis/notice/index'
import filingNotice from '@/components/common/filingNotice'
export default {
  name: 'vaccinationInspection',
  components: {
    formwork,
    filingNotice
  },
  mixins: [commonOptions],
  data() {
    return {
      type: '6',
      /* 以下是弹框详细 */
      cModId: this.$store.state.moduleId,
      activeName: '0',
      rules: {
        checkType: [
          { required: true, message: '请选择学校类型', trigger: 'blur' },
        ],
        schoolId: [
          { required: true, message: '请选择学校名称', trigger: 'blur' },
        ],
      },
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      unitManageForm: {
        schoolId: ''
      },
      lableWidth: '200px',
      formData: {
        content: ''
      },
      fillingData: {
        stuName: 'XXX',
        schoolName: 'XXX',
        gradeName: 'XXX',
        className: 'XXX',
        stuBirth: 'XXXX年XX月XX日',
        stuParentname: 'XXX',
        noticeContent: '',
        schStationName: 'XXX'
      },
      content: '',
      isShowContent:false,//是否显示内容
    }
  },
  watch: {
    'unitManageForm.checkType': {
      deep: true,
      async handler(newVal, oldVal) {
        // 学校类型发生改变，查找学校
        if (newVal !== oldVal) {
          this.unitManageForm.schoolId = ''
          if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
            this.$util.errorMsg('必须选择地区！')
            return
          }
          this.findSchool()
          this.formData.content=''
        }
      }
    },
    'unitManageForm.schoolId': {
      deep: false,
      async handler(newVal, oldVal) {
        if (newVal!='' && newVal!=oldVal) {
          this.formData.content=''
        }
      }
    },
    activeName: {
      deep: true,
      async handler(newVal, oldVal) {
        if (newVal !== oldVal) {
          this.search(this.oldSchId)
        }
      }
    },
    'unitManageForm.schoolId': {
      deep: true,
      async handler(newVal, oldVal) {
        console.log(newVal, oldVal,'newVal, oldVal')
        this.oldSchId=oldVal
      }
    },
  },
  methods: {
    nodeClick() {
      this.unitManageForm.schoolId=''
      this.findSchool()
    },
    changeSchool(v) {
      if (v) {
        this.fillingData.schoolName = this.schoolList.filter(item => item.schId == v)[0].schName
        this.fillingData.schStationName = this.schoolList.filter(item => item.schId == v)[0].schStationName
        this.unitManageForm.schoolId = v;
        // this.formData.schName = this.schoolList.filter(item => item.schId == v)[0].schName
        // this.formData.schStationName = this.schoolList.filter(item => item.schId == v)[0].schStationName
      }
    },
    // 查询学校
    findSchool() {
      findSchoolByAreaAndSchType({
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel,
        schType: this.unitManageForm.checkType
      }).then((res) => {
        this.schoolList = res.data
      })
    },
    handleClick(tab, event) {
      this.activeName=tab.index
    },
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    search(schoolId) {
      let that = this;
      if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        this.$util.errorMsg('必须选择地区！')
        return
      }
      if (!this.unitManageForm.checkType) {
        this.$util.errorMsg('必须选择学校类型！')
        return
      }
      if (!this.unitManageForm.schoolId) {
        this.$util.errorMsg('必须选择学校名称！')
        return
      }
      let params = {
        schId: schoolId || this.unitManageForm.schoolId,
        noticeType: this.activeName,
        operateType: this.$consts.OPTYPE_QUERY,
      }
      this.openFullScreen()
      queryNoticeMess(params).then(res => {
        that.fillingData.noticeContent = res.data[0].noticeContent
        that.formData={
          ...that.formData,
          content : res.data[0].noticeContent,
          maintype : this.activeName,
          schoolId : schoolId || this.unitManageForm.schoolId
        }
        if(schoolId && isShowContent){
          this.formData.schName = this.schoolList.filter(item => item.schId == schoolId)[0].schName
          this.formData.schStationName = this.schoolList.filter(item => item.schId == schoolId)[0].schStationName
        }else{
          this.formData.schName = this.schoolList.filter(item => item.schId == this.unitManageForm.schoolId)[0].schName
          this.formData.schStationName = this.schoolList.filter(item => item.schId == this.unitManageForm.schoolId)[0].schStationName
        }
        this.isShowContent=true;
        this.oldSchId=this.unitManageForm.schoolId
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    editHandler(noticeType) {
      if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        this.$util.errorMsg('必须选择地区！')
        return
      }
      if (!this.unitManageForm.checkType) {
        this.$util.errorMsg('必须选择学校类型！')
        return
      }
      if (!this.unitManageForm.schoolId) {
        this.$util.errorMsg('必须选择学校名称！')
        return
      }
      let params = {
        schId: this.unitManageForm.schoolId,
        noticeType: noticeType | 0,
        noticeContent: this.formData.content,
        operateType: this.$consts.OPTYPE_UPDATE,
      }
      if (noticeType == 2) {
        params.noticeContent = this.fillingData.noticeContent;
      }
      this.openFullScreen()
      upNoticeMess(params).then(res => {
        this.$message({
          message: res.data || '保存成功!',
          type: 'success'
        })
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    }
  },
  mounted(){
  }
}
</script>
<style scoped>
.formwork .el-tabs {
  margin: 30px 50px;
}
</style>
    