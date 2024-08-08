<template>
  <div>
    <div class="condition-row">
      <div class="item">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>选择地区：</div>
        <ss-area-areaTreeStation :showSelect="false" ref="areaTreePage" class="area" :disabled="msgPubPermission"
          :getToLevelNum="5 - currentLevel" @nodeClick="nodeClick" />
      </div>
      <div class="item">
        <div class="label">学校类型：</div>
        <el-select v-model="unitManageForm.schType" placeholder="请选择学校类型" clearable @change="schTypeChange">
          <el-option v-for="item in selectTypeList2" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </div>
      <div class="item">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>学校名称：</div>
        <el-select style="width:200px;" slot="fEnterno" v-model="unitManageForm.schId" placeholder="请选择学校名称" clearable @change="schIdChange">
          <el-option style="width:200px;" v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
        </el-select>
      </div>
      <div class="item">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>学年：</div>
        <el-select v-model="unitManageForm.claYear" placeholder="请选择学年" clearable @change="claLevelChange">
          <el-option v-for="item in schoolYearList" :key="item.rowkey" :label="item.claNameYear" :value="item.claNameYear" />
        </el-select>
      </div>
    </div>
    <div class="condition-row">
      <div class="item">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>疫苗：</div>
        <el-select v-model="unitManageForm.vaccName" placeholder="请选择疫苗" clearable @change="vaccinumChange($event,'vaccName')">
          <el-option v-for="item in vaccinumList" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </div>
      <div class="item">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>年份：</div>
        <el-select v-model="unitManageForm.nowYear" placeholder="请选择年份" clearable @change="vaccinumChange($event,'nowYear')">
          <el-option v-for="item in nowYearList" :key="item" :label="item" :value="item" />
        </el-select>
      </div>
      <div class="item">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>轮次：</div>
        <el-select v-model="unitManageForm.roundName" placeholder="请选择轮次" clearable>
          <el-option v-for="item in roundNameList" :key="item" :label="item" :value="item"></el-option>
        </el-select>
      </div>
      <div class="item">
        <div class="label">年级：</div>
        <el-select clearable v-model="unitManageForm.claLevel" placeholder="请选择年级" @change="claLevelChange">
          <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </div>
    </div>
    <div class="condition-row">
      <div class="item">
        <div class="label">班级：</div>
        <el-select clearable v-model="unitManageForm.claId" placeholder="请选择班级">
          <el-option v-for="item in classesList" :key="item.rowkey" :label="item.claShortName" :value="item.rowkey" />
        </el-select>
      </div>
      <slot></slot>
    </div>
  </div>
</template>

<script>
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
export default {
  name: 'selectClass',
  mixins: [commonOptions],
  data () {
    return {
      resetForm: {},
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      selectTypeList:this.$store.state.selectTypeList,//学校类型列表
      vaccinumList:this.$store.state.vaccinumList,//疫苗列表
      // schoolYearList:[],//学年列表
      classesList: [],//班级列表
      unitManageForm:{
        schType:'',//学校类型
        schId:'',//学校名称
        claYear:'',//学年
        nowYear:'',//年份
        vaccName:'',//疫苗
        roundName:'',//轮次
        claLevel:'',//年级
        claId:"",//班级
      },
      msgPubPermission:false,
    }
  },
  methods: {
    nodeClick () {
      this.unitManageForm.schId=""
      this.schoolList=[]
      this.unitManageForm.claYear=""
      this.schoolYearList=[]
      this.findSchool()
    },
    // 查询学校
    async findSchool () {
      if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        return false
      }
      // if (!this.unitManageForm.schType) {
      //   return false
      // }
      let params={
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel,
        schType: this.unitManageForm.schType
      }
      await this.findSchoolNew(params)
    },
    async schTypeChange(val){
      this.unitManageForm.claLevel=""
      this.unitManageForm.schId=""
      this.unitManageForm.claId=""
      // this.unitManageForm.claYear=""
      this.schoolList=[]
      // this.schoolYearList=[]
      // this.classesList=[]
      await this.findClaLevelSecond(val)
      await this.findSchool()
      await this.getClass()
    },
    schIdChange (val) {
      this.unitManageForm.claId=''
      this.unitManageForm.claYear=""
      this.schoolYearList=[]
      this.classesList=[]
      this.getClass()
      this.getClassesYears()
      // if(val){
      //   this.getClass()
      //   this.getClassesYears()
      // }
    },
    validateForm(){
      if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        this.$util.errorMsg('必须选择地区！')
        return false
      }
      if(!this.unitManageForm.schId){
        this.$util.errorMsg('请选择学校名称！')
        return false
      }
      if(!this.unitManageForm.claYear){
        this.$util.errorMsg('请选择学年！')
        return false
      }
      if(!this.unitManageForm.nowYear){
        this.$util.errorMsg('请选择年份！')
        return false
      }
      if(!this.unitManageForm.vaccName){
        this.$util.errorMsg('请选择疫苗！')
        return false
      }
      if(!this.unitManageForm.roundName){
        this.$util.errorMsg('请选择轮次！')
        return false
      }
      return true
    },
    vaccinumChange(val,name){
      // if(name=='vaccName') this.unitManageForm.nowYear=''
      this.unitManageForm.roundName=''
      this.getRound()
      // if(this.unitManageForm.nowYear && this.unitManageForm.vaccName){
      //   this.getRound()
      // }else{
      //   // if(name=='vaccName')  this.nowYearList=[]
      //   this.roundNameList=[]
      // }
    },
    async claLevelChange(val){
      this.unitManageForm.claId=''
      this.classesList=[]
      await this.getClass()
      // if(val){
      //   await this.getClass()
      // }else{
      //   this.classesList=[]
      // }
    },
    getRound(){
      let params = {
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel:this.$refs.areaTreePage.selectAreaLevel,
        nowYear:this.unitManageForm.nowYear,//查询年份
        vaccName:this.unitManageForm.vaccName,//疫苗名称
        roundName:this.unitManageForm.roundName,//轮次名称
      }
      this.getRoundName(params)
    },
    getYear(){
      let params = {
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel:this.$refs.areaTreePage.selectAreaLevel,
        nowYear:this.unitManageForm.nowYear,//查询年份
        vaccName:this.unitManageForm.vaccName,//疫苗名称
        roundName:this.unitManageForm.roundName,//轮次名称
      }
      this.getRoundYear(params)
    },
    getClass(){
      // if(this.unitManageForm.schId && this.unitManageForm.claLevel && this.unitManageForm.claYear){
        let params = {
          schId:this.unitManageForm.schId,
          classLevel:this.unitManageForm.claLevel,
          classYear:this.unitManageForm.claYear,
        }
        this.getClasses(params)
      // }
    },
    getClassesYears(){
      let params = {
        schId:this.unitManageForm.schId,
        classLevel:this.unitManageForm.claLevel,
        classYear:this.unitManageForm.claYear,
      }
      this.getClassesYear(params)
    }
  },
  async mounted () {
    this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) == 4
    await this.getClass()
    await this.findSchool()
    await this.getYear()
    await this.getRound()
    await this.findClaLevelSecond()
    await this.getClassesYears()
  }
}
</script>