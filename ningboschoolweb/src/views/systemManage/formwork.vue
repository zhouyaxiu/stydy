<template>
  <!-- 通知单模板管理 -->
  <div class="formwork">
    <ss-condition-area definedTitle="通知单模板管理" :titleLong="true" ref="conditionPage" @setConditionFlag="setConditionFlag">
      <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
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
import formwork from '@/components/common/formworkNew'
// import filingNotice from '@/components/common/filingNotice'
import { queryNoticeMess, upNoticeMess } from '@/utils/axios/apis/notice/index'
export default {
  name: 'vaccinationInspection',
  components: {
    formwork, 
    // filingNotice
  },
  mixins: [commonOptions],
  data() {
    return {
      type: '6',
      /* 以下是弹框详细 */
      cModId: this.$store.state.moduleId,
      activeName: '0',
      formData: {
        schStationName: this.$store.state.schStationName,
        maintype: '0',
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
    }
  },
  methods: {
    handleClick(tab, event) {
      this.search()
    },
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    search() {
      let params = {
        schId: this.$store.state.schId,
        noticeType: this.activeName,
        operateType: this.$consts.OPTYPE_QUERY,
      }
      queryNoticeMess(params).then(res => {
        this.fillingData.noticeContent = res.data[0].noticeContent
        // this.formData.content=res.data[0].noticeContent;
        // this.$set(formData,'content',res.data[0].noticeContent)
        // this.formData.maintype=this.activeName
        // this.$set(formData,{'content':res.data[0].noticeContent,'maintype':this.activeName})
        this.formData = Object.assign({}, this.formData, {
          'content': res.data[0].noticeContent,
          'maintype': this.activeName
        })
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    editHandler(noticeType) {
      let params = {
        schId: this.$store.state.schId,
        noticeType: noticeType,
        noticeContent: this.fillingData.noticeContent,
        operateType: this.$consts.OPTYPE_UPDATE,
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
  mounted() {
    this.search()
  }
}
</script>
<style >
.pointer {
  cursor: pointer;
  color: #1e6ce4;
}

.ml_10 {
  margin-left: 10px;
}

.mt_10 {
  margin-top: 10px;
}

.mt_20 {
  margin-top: 20px;
}

.formwork .el-tabs {
  margin: 30px 50px;
}

</style>
    