<template>
  <!-- 信息扫码 -->
  <div class="information">
    <ss-condition-area definedTitle="信息扫码" ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <div class="label"><el-radio :label="4" v-model="unitManageForm.radio1"><span
                class="texts">儿童姓名：</span></el-radio></div>
          <el-input v-model="unitManageForm.stuName" placeholder="请输入儿童姓名" clearable
            :disabled="unitManageForm.radio1 != 4" class="w200"></el-input>
        </div>
        <div class="item">
          <div class="label"><span class="texts">性别：</span></div>
          <el-select v-model="unitManageForm.stuSex" placeholder="请选择性别" clearable :disabled="unitManageForm.radio1 != 4"
            @change="$forceUpdate()">
            <el-option label="未知的性别" value="未知的性别" />
            <el-option label="男性" value="男性" />
            <el-option label="女性" value="女性" />
            <el-option label="未说明的性别" value="未说明的性别" />
          </el-select>
        </div>
        <div class="item">
          <div class="label"><span class="texts">出生日期：</span></div>
          <el-date-picker class="w_320" v-model="unitManageForm.stuBirth" :picker-options="startTimeOption"
            value-format="yyyy-MM-dd" format="yyyyMMdd" type="date" placeholder="请输入"
            :disabled="unitManageForm.radio1 != 4" @change="$forceUpdate()">
          </el-date-picker>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <div class="label"><el-radio :label="1" v-model="unitManageForm.radio1"><span
                class="texts">儿童编码：</span></el-radio></div>
          <el-input v-model="unitManageForm.childNo" ref="searchInput" clearable :disabled="unitManageForm.radio1 != 1"
            placeholder="请输入儿童编码"></el-input>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <div class="label"><el-radio :label="2" v-model="unitManageForm.radio1"><span
                class="texts">儿童条码：</span></el-radio></div>
          <el-input v-model="unitManageForm.imonu" ref="searchInput" clearable :disabled="unitManageForm.radio1 != 2"
            placeholder="请输入儿童条码"></el-input>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <div class="label"><el-radio :label="3" v-model="unitManageForm.radio1"><span
                class="texts">身份证件号码：</span></el-radio></div>
          <el-input v-model="unitManageForm.cid" ref="searchInput" clearable :disabled="unitManageForm.radio1 != 3"
            placeholder="请输入身份证件号码"></el-input>
        </div>
        <div class="item" style="align-items: center;">
          <div class="label">&nbsp;</div>
          <div style="display: flex;">
            <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
            <ss-custom-button @click="resetHandler" :type="$consts.CUSTOMBUTTONTYPES.reset" class="ml_10" />
            <ss-custom-button @click="saveHandler" :type="$consts.CUSTOMBUTTONTYPES.save" class="ml_10" />
          </div>
        </div>
      </div>
    </ss-condition-area>
    <div class="main-content" style="position: relative;">
      <ss-custom-title type="info" text="学生信息" slot="title"></ss-custom-title>
      <!-- <div class="mt_10" style="position: absolute;bottom: 30px;left:0;right:0;text-align: center;margin:0 auto;">
        <ss-custom-button @click="saveHandler" :type="$consts.CUSTOMBUTTONTYPES.save" />
      </div> -->
      <el-row :gutter="20">
        <el-col :span="6">
          <el-descriptions :column="1" border labelClassName="labelwidth">
            <el-descriptions-item label="学生姓名">{{ stuInfo.stuName }}</el-descriptions-item>
            <el-descriptions-item label="学生性别">{{ stuInfo.stuSex }}</el-descriptions-item>
            <el-descriptions-item label="出生日期">{{ stuInfo.stuBirth }}</el-descriptions-item>
            <el-descriptions-item label="父亲/母亲/监护人姓名">{{ stuInfo.stuParentname }}</el-descriptions-item>
          </el-descriptions>
        </el-col>
        <el-col :span="6">
          <el-descriptions :column="1" border labelClassName="labelwidth">
            <el-descriptions-item label="家庭住址" contentStyle="height:92px" labelStyle="height:92px">{{ stuInfo.fResideAddr
            }}</el-descriptions-item>
            <el-descriptions-item label="所属预防接种单位" contentStyle="height:92px" labelStyle="height:92px">{{
              stuInfo.stuBelongStationName }}</el-descriptions-item>
          </el-descriptions>
        </el-col>
        <el-col :span="12">
          <el-descriptions :column="2" border labelClassName="labelwidth">
            <!-- <el-descriptions-item label="家庭住址" span="2">{{ stuInfo.fResideAddr }}</el-descriptions-item> -->
            <el-descriptions-item label="选择地区" labelStyle="color:red;" contentClassName="nopad" span="2">
              <ss-area-areaTreeStation class="area" :showSelect="false" ref="areaTreePage"
                :getToLevelNum="5 - currentLevel" @nodeClick="nodeClick" />
            </el-descriptions-item>
            <el-descriptions-item label="学校类型" labelStyle="color:red;" contentClassName="nopad">
              <el-select v-model="unitManageForm.checkType" placeholder="请选择学校类型" clearable class="select" @change="checkTypeChange">
                <el-option v-for="item in selectTypeList2" :key="item.code" :label="item.name" :value="item.code" />
              </el-select>
            </el-descriptions-item>
            <el-descriptions-item label="学校名称" labelStyle="color:red;" contentClassName="nopad">
              <el-select filterable v-model="unitManageForm.schoolId" placeholder="请选择学校" @change="schoolChange" clearable
                class="select">
                <el-option v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
              </el-select>
            </el-descriptions-item>
            <el-descriptions-item label="学年" labelStyle="color:red;" contentClassName="nopad">
              <el-select clearable v-model="unitManageForm.stuclayear" placeholder="请选择学年" class="select">
                <el-option v-for="item in schoolYearList" :key="item.rowkey" :label="item.claNameYear" :value="item.claNameYear" />
              </el-select>
            </el-descriptions-item>
            <el-descriptions-item label="年级" labelStyle="color:red;" contentClassName="nopad">
              <el-select clearable v-model="unitManageForm.stuClalevel" placeholder="请选择年级" class="select" @change="clalevelChange">
                <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code" />
              </el-select>
            </el-descriptions-item>
            <el-descriptions-item label="班级" labelStyle="color:red;" contentClassName="nopad">
              <el-select clearable v-model="unitManageForm.classId" placeholder="请选择班级" style="width: 100%;"  class="select">
                <el-option v-for="item in classesList" :key="item.rowkey" :label="item.claShortName" :value="item.rowkey" />
              </el-select>
              <!-- <el-input placeholder="请输入班级" clearable v-model.trim="unitManageForm.classId" maxlength="10"></el-input> -->
            </el-descriptions-item>
          </el-descriptions>
        </el-col>
      </el-row>
    </div>
    <!-- 详细信息弹框 -->
    <info-scan-dialog :visible="dialogVisible" :dataList="dataList" @saveHandler="saveHandler" @close="close"
      v-if="dialogVisible" />
    <mselectDialog :visible="dialogTableVisible" :gridData="gridData" :searchData="searchData"></mselectDialog>
  </div>
</template>
<script>
import { queryByConditions,saveByFchildno,saveAndChange } from '@/utils/axios/apis/infoScan'
import commonOptions from '@/mixin/commonOptions'
import InfoScanDialog from './components/InfoScanDialog.vue'
import mselectDialog from './components/mselectDialog.vue'
import { getLevel } from '@/utils/common/area.js'
export default {
  components: { InfoScanDialog, mselectDialog},
  mixins: [commonOptions],
  data() {
    return {
      data: {},
      unitManageForm: {
        stuclayear: '',
        radio1: '',
        stuName: '',
        stuSex: '',
        stuBirth: '',
        childNo: '',
        imonu: '',
        stuClalevel: '',
        classId: '',
        checkType: '',
        schoolId: '',
        cid:''
      },
      height: window.winHeight - 15 / window.scale - 320,
      dialogVisible: false,
      dataList: [],
      startTimeOption: {
        disabledDate: time => {
          return (
            time.getTime() > new Date(this.$dateutil.dayAddNum()).getTime()
          )
        }
      },
      stuInfo: {},
      maininfo: {},
      dialogTableVisible: false,
      gridData: [],
      searchData: {},
      currentLevel: Number(getLevel(this.$store.state.userCode)),
    }
  },
  watch: {
    'unitManageForm.radio1'(newVal, oldVal) {
      if(oldVal==1) this.unitManageForm.childNo=""
      if(oldVal==2) this.unitManageForm.imonu = ''
      if(oldVal==3) this.unitManageForm.cid = ''
      if(oldVal==4) this.unitManageForm.stuName = ''; this.unitManageForm.stuSex = ''; this.unitManageForm.stuBirth = ''
    },
  },
  methods: {
    // 查询接口
    async search(pagenum) {
      if (!pagenum) {
        pagenum = 1
      }
      this.pagenum = pagenum
      if (!this.unitManageForm.radio1) {
        this.$util.errorMsg('儿童编码或儿童条码或身份证件号码或儿童姓名等至少选中一个！')
        return false
      }
      if (this.unitManageForm.radio1 === 1 && !this.unitManageForm.childNo) {
        this.$util.errorMsg('儿童编码不能为空！')
        return false
      }
      if (this.unitManageForm.radio1 === 2 && !this.unitManageForm.imonu) {
        this.$util.errorMsg('儿童条码不能为空！')
        return false
      }
      if (this.unitManageForm.radio1 === 3 && !this.unitManageForm.cid) {
        this.$util.errorMsg('身份证件号码不能为空！')
        return false
      }
      if (this.unitManageForm.radio1 === 4) {
        if (!this.unitManageForm.stuName) {
          this.$util.errorMsg('儿童姓名不能为空！')
          return false
        }
        if (!this.unitManageForm.stuSex) {
          this.$util.errorMsg('儿童性别不能为空！')
          return false
        }
        if (!this.unitManageForm.stuBirth) {
          this.$util.errorMsg('儿童出生日期不能为空！')
          return false
        }
      }
      let params={
        // schid:'3306210400003306210401004',
        // type:2,//类型
        // birth:'2015-02-12',//出生日期
        // childno:'140525022015007',//编码
        // cid:'330102199812212128',//身份证
        // gender:'男性',//性别
        // imuno:'1245679901478',//条码 1234567890001
        // name:'自动测试_戴林希',//姓名  金秀丽  199589  女
        schid:this.unitManageForm.schoolId,
        type:this.unitManageForm.radio1,//类型
        birth:this.unitManageForm.stuBirth,//出生日期
        childno:this.unitManageForm.childNo,//编码
        cid:this.unitManageForm.cid,//身份证
        gender:this.unitManageForm.stuSex,//性别
        imuno:this.unitManageForm.imonu,//条码
        name:this.unitManageForm.stuName,//姓名
        stationCode:this.$refs.areaTreePage.selectAreaCode,
        stationName:this.$refs.areaTreePage.selectAreaName,
        operateType: this.$consts.OPTYPE_QUERY,
      }
      this.openFullScreen()
      this.searchData = params
      queryByConditions(params).then((res) => {
        if (res.data.code == 200) {
          if(!Array.isArray(res.data.data)){
            this.stuInfo = res.data.data
          }else{
            this.dataList = res.data.data;
            this.dialogVisible = true;
          }
        }
        if (res.data.code == 521) {
          this.$util.errorMsg(res.data.data)
        }
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    async sonSearch(){
      let params = {
        ...this.searchData,
        childno: this.unitManageForm.tchildNo,
        type:1,
        operateType: this.$consts.OPTYPE_QUERY,
      }
      this.openFullScreen()
      queryByConditions(params).then((res) => {
        if (res.data.code == 200) {
          if(!Array.isArray(res.data.data)){
            this.stuInfo = res.data.data
          }else{
            this.dataList = res.data.data;
            this.dialogVisible = true;
          }
        }
        if (res.code == 521) {
          this.$util.errorMsg(res.data)
        }
        this.secTime = res.sec
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    // 保存
    saveHandler(name) {
      let unitManageForm = this.unitManageForm
      if (Object.keys(this.stuInfo).length == 0) {
        this.$util.errorMsg('必须先点击查询！')
        return
      }
      if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        this.$util.errorMsg('必须选择地区！')
        return false
      }
      if (!this.unitManageForm.checkType) {
        this.$util.errorMsg('请选择学校类型!')
        return false
      }
      if (!this.unitManageForm.schoolId) {
        this.$util.errorMsg('请选择学校名称!')
        return false
      }
      if (!this.unitManageForm.stuclayear) {
        this.$util.errorMsg('请选择学年!')
        return false
      }
      if (!this.unitManageForm.stuClalevel) {
        this.$util.errorMsg('请选择年级!')
        return false
      }
      if (!this.unitManageForm.classId) {
        this.$util.errorMsg('请选择班级!')
        return false
      }
      // if (!/^[0-9A-Za-z\u4e00-\u9fa5]+$/.test(this.unitManageForm.classId)) {
      //   this.$util.errorMsg('班级只能是中文，英文，数字!')
      //   return false
      // }
      let params={
        childno:this.stuInfo.stuNo,//儿童编码
        classId:this.unitManageForm.classId,
        className:this.classesList.filter(item => item.rowkey === unitManageForm.classId)[0].claShortName,
        grade:this.unitManageForm.stuClalevel,
        schid:this.unitManageForm.schoolId,
        stationCode:this.$refs.areaTreePage.selectAreaCode,
        stationName:this.$refs.areaTreePage.selectAreaName,
        year:this.unitManageForm.stuclayear,
        operateType: this.$consts.OPTYPE_INSERT
      }
      this.openFullScreen()
      saveByFchildno(params).then((res) => {
        if (res.data.code == 200) {
          this.$message({
            message: res.data.data || '保存成功!',
            type: 'success'
          })
        }
        if (res.data.code == 202) {
          this.$confirm(res.data.data, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let params1={
              childno:params.childno,
              classId:params.classId,
              schid:params.schid,
              stateCode:res.data.code,
              stationCode:this.$refs.areaTreePage.selectAreaCode,
              stationName:this.$refs.areaTreePage.selectAreaName,
              oldSchId:res.data.data[0].schid,
              operateType: this.$consts.OPTYPE_INSERT
            }
            saveAndChange(params1).then(res => {
              this.$message({
                message: res.data.data || '保存成功!',
                type: 'success'
              })
              this.closeFullScreen()
            }).catch(() => {
              this.closeFullScreen()
            })
            this.closeFullScreen()
          }).catch(() => {
            this.closeFullScreen()
          })
        }
        if (res.data.code == 204) {
          this.dialogTableVisible = true;
          this.gridData = res.data.data
          let params1={
            childno:params.childno,
            classId:params.classId,
            schid:params.schid,
            stateCode:res.data.code,
            stationCode:this.$refs.areaTreePage.selectAreaCode,
            stationName:this.$refs.areaTreePage.selectAreaName,
            oldSchId:res.data.data[0].schid
          }
          this.searchData = params1
        }
        if (res.data.code == 521 || res.data.code == 203) {
          this.$util.errorMsg(res.data.data)
        }
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    // 重置
    resetHandler() {
      this.unitManageForm.radio1 = ''
      this.unitManageForm.stuclayear = '';
      this.unitManageForm.checkType = '';
      this.unitManageForm.schoolId = '';
      this.unitManageForm.stuClalevel = '';
      // this.unitManageForm.lunci = '';
      this.unitManageForm.classId = '';
      this.unitManageForm.tchildNo='';
      this.unitManageForm.oldSchId='';
      this.classesList=[]
      this.schoolList=[]
      this.schoolYearList=[]
      this.classesList=[]
      this.stuInfo = {};
      this.findSchool()
    },
    nodeClick() {
      this.unitManageForm.schoolId = ''
      this.unitManageForm.stuclayear=""
      this.schoolList=""
      this.schoolYearList=""
      this.findSchool()
    },
    async schoolChange(val) {
      this.unitManageForm.stuclayear = ""
      this.unitManageForm.classId=''
      this.classesList=[]
      await this.getClassesYears()
      await this.getClass()
      // if(val){
      //   await this.getClassesYears()
      //   this.getClass()
      // }else{
      //   this.schoolYearList=[]
      // }
    },
    // 查询学校
    async findSchool() {
      if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        return false
      }
      // if (!this.unitManageForm.checkType) {
      //   return false
      // }
      let params={
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel,
        schType: this.unitManageForm.checkType
      }
      await this.findSchoolNew(params)
    },
    close() {
      this.dialogVisible = false
      this.dataList = []
      this.dialogTableVisible = false;
      this.gridData = []
      // this.searchData = {}
    },
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    async checkTypeChange(val){
      this.unitManageForm.stuClalevel=""
      this.unitManageForm.schoolId=""
      this.schoolList=[]
      this.unitManageForm.stuclayear=''
      // this.schoolYearList=[]
      this.unitManageForm.classId=''
      this.classesList=[]
      await this.findClaLevelSecond(val)
      await this.findSchool()
      await this.getClassesYears()
      await this.getClass()
    },
    async clalevelChange(){
      // this.unitManageForm.stuclayear=''
      // this.schoolYearList=[]
      // await this.getClassesYears()
      this.unitManageForm.classId=""
      this.classesList=""
      this.getClass()
    },
    async getClassesYears(){
      let params = {
        schId:this.unitManageForm.schoolId,
        classLevel:this.unitManageForm.stuClalevel,
        classYear:this.unitManageForm.stuclayear,
      }
      await this.getClassesYear(params)
    },
    getClass(){
      let params = {
        schId:this.unitManageForm.schoolId,
        classLevel:this.unitManageForm.stuClalevel,
        classYear:this.unitManageForm.stuclayear,
      }
      this.getClasses(params)
    },
  },
  async mounted() {
    await this.getClassesYears()
    await this.findSchool()
    await this.findClaLevelSecond()
    await this.getClass()
  }
}
</script>
<style scoped>
.information .el-input {
  width: 200px
}

.information .condition-row .el-radio {
  margin-right: 0 !important;
}
</style>
<style>
.mr_10 {
  margin-right: 10px;
}

.information .el-input {
  width: 200px
}

.information .el-radio {
  margin-right: 0 !important;
}

.information .texts {
  font-size: 13px;
  color: #213255;
}

.information .labelwidth {
  width: 160px;
}

.information .labelwidth2 {
  width: 60px;
  text-align: center !important;
}

.information .nopad {
  padding: 0 !important;
  border-top: 0;
  border-bottom: 0;
}

.information .nopad .el-descriptions {
  margin: -1px;
}

.information .nopad tbody:last-of-type th,
.information .nopad tbody:last-of-type td {
  border-bottom-color: transparent;
  line-height: initial;
  border-bottom: 0;
}

.information .nopad tbody:first-of-type th,
.information .nopad tbody:first-of-type td {
  border-top-color: transparent;
  line-height: initial;
  border-top: 0;
}

.information .infos {
  list-style-type: decimal;
  margin: 0;
  padding-left: 16px;
  line-height: 26px;
}

.information .select {
  width: 100%;
  outline: none;
  border: 0;
}

.information .select .el-input.is-disabled .el-input__inner {
  background: #fff;
  color: #333;
  border: 0;
  cursor: default;
}

.information .select input {
  height: 45px !important;
  line-height: 45px !important;
  border-color: #fff;
  outline: none;
  width: 100% !important;
  border: 0;
}

.information .select .el-input,
.information .nopad .el-input {
  width: 100%;
  border: 0;
  border-color: #fff;
  outline: none;
}

.information .select .el-select:hover .el-input__inner,
.information .nopad .el-input__inner {
  border-color: #fff;
}
</style>