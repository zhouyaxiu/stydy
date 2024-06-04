<template>
  <!-- 年份、轮次、年级、班级联动选择组件 -->
  <!-- schoolnameRequired学校名称必填   schoolname学校名称显示 -->
  <!-- classRequired班级必填   class班级显示 -->
  <!-- grade班级显示 -->
  <div>
    <div class="condition-row newselect">
      <div class="item">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>选择地区：</div>
        <ss-area-areaTreeStation :disabled="ischeck && $store.state.modelObject==1 ?true:false" class="area" :showSelect="false" ref="areaTreePage" :getToLevelNum="5 - currentLevel" @nodeClick="nodeClick" />
      </div>
      <div class="item">
        <div class="label">学校类型：</div>
        <el-select v-model="unitManageForm.checkType" placeholder="请选择学校类型" clearable>
          <el-option label="幼托机构" value="0" />
          <el-option label="小学" value="1" />
        </el-select>
      </div>
      <div class="item" v-if="otherField.schoolname">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>学校名称：</div>
        <el-select style="width:200px;" filterable v-model="unitManageForm.schoolId" placeholder="请选择学校" clearable @change="schoolChange">
          <el-option style="width:200px;" v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
        </el-select>
      </div>
      <div class="item">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>学年：</div>
        <el-select clearable v-model="unitManageForm.stuclayear" placeholder="请选择">
          <el-option v-for="item in schoolYearList" :key="item" :label="item" :value="item"/>
        </el-select>
      </div>
      <div class="item" v-if="otherField.grade">
        <div class="label">年级：</div>
        <el-select clearable v-model="unitManageForm.stuClalevel" placeholder="请选择" @change="$forceUpdate()">
          <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code"/>
        </el-select>
      </div>
      <div class="item">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>轮次：</div>
        <el-select v-model="unitManageForm.lunci" placeholder="请选择" clearable>
          <el-option label="春季" value="1" />
          <el-option label="秋季" value="0" />
        </el-select>
      </div>
      <div class="item" v-if="otherField.classname">
        <div class="label"><span style="color: red; margin-right: 6px;" v-if="otherField.classRequired">*</span>班级：</div>
        <el-input placeholder="请输入" clearable v-model.trim="unitManageForm.classId" maxlength="10" v-if="!classSelect"></el-input>
        <el-select v-model="unitManageForm.classId" placeholder="请选择" clearable v-else>
          <el-option v-for="item in classesList" :key="item" :label="item" :value="item"/>
        </el-select>
      </div>
      <slot></slot>
    </div>
  </div>
</template>

<script>
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
import { findSchoolByAreaAndSchType } from '@/utils/axios/api'
export default {
  name: 'selectClass',
  mixins: [commonOptions],
  data () {
    return {
      classesList: [],
      resetForm: {},
      rules:{
        checkType: [
          // { required: true, message: '请选择学校类型', trigger: 'blur' },
        ],
        schoolId: [
          { required: this.otherField.schoolnameRequired?true:false, message: '请选择学校名称', trigger: 'change' },
        ],
        stuclayear: [
          { required: true, message: '请选择学年', trigger: 'change' },
        ],
        lunci: [
          { required: true, message: '请选择轮次', trigger: 'change' },
        ],
        stuClalevel: [
          { required: true, message: '请选择年级', trigger: 'change' },
        ],
        classId: [
          { required: this.otherField.classRequired?true:false, message: '请输入班级', trigger: 'blur' },
        ],
      },
      currentLevel: Number(getLevel(this.$store.state.userCode)),
    }
  },
  props: {
    lableWidth: {
      type: String,
      required: false,
      default: '200px'
    },
    classSelect:{
      type:Boolean,
      default:false
    },
    ischeck:{
      type:Boolean,
      default:false
    },
    defaultField:{
      type: Object,
      default:function(){
        return {}
      }
    },
    otherField:{
      type:Object,
      default:{}
    },
    unitManageForm: {
      type: Object,
      default: function () {
        return {
          stuclayear: this.$dateutil.currentYear(),
          stuClalevel: '',
          classId: '',//班级
          lunci: '',
          // classNames:'',
          checkType:'',//学校类型
          schoolname:'',//学校名称
          schoolId:"",//学校id
          areaCode:'',//地区code
          schStationCode:'',//学校所属单位
        }
      }
    }
  },
  computed: {
    unitManageFormCopy () {
      return JSON.parse(JSON.stringify(this.unitManageForm))
    }
  },
  watch: {
    formValue (newVal, oldVal) {
      this.unitManageForm = JSON.parse(JSON.stringify(this.resetForm))
    },

    unitManageFormCopy: {
      deep: true,
      async handler (newVal, oldVal) {
        // 学校类型发生改变，查找学校
        if(this.otherField.schoolname){
          if (newVal.checkType !== oldVal.checkType) {
            this.unitManageForm.schoolId=''
            if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
              this.$util.errorMsg('必须选择地区！')
              return
            }
            this.findSchool()
          }
        }
        if(newVal.checkType !== oldVal.checkType){
          this.unitManageForm.stuClalevel=""
          this.unitManageForm.schoolId=""
          await this.findClaLevelNew(this.unitManageForm.checkType)
        }
        // && (this.otherField.grade && newVal.stuClalevel!='')
        if(newVal.schoolId!='' && newVal.stuclayear!='' && newVal.lunci!='' || (this.otherField.grade && newVal.stuClalevel!='')){
          if(newVal.schoolId!=oldVal.schoolId || newVal.stuclayear!=oldVal.stuclayear || newVal.lunci!=oldVal.lunci || (this.otherField.grade && newVal.stuClalevel!=oldVal.stuClalevel)){
            this.unitManageForm.classId=''
          }
          if(newVal.classId ==''){
            let params={
              schId:newVal.schoolId || '-1',
              classLevel:newVal.stuClalevel,
              classYear:newVal.stuclayear,
              lunci:newVal.lunci
            }
            if(sessionStorage.getItem('title')=='补种情况反馈表' && this.unitManageForm.stuClalevel==''){
              this.unitManageForm.classId=''
              this.classesList=[]
              return
            }
            await this.findClassNew(params)
          }
        }else{
          if(this.classSelect){
            this.unitManageForm.classId=''
            await this.findClassNew()
          }
        }
      }
    }
  },
  methods: {
    nodeClick () {
      if(this.otherField.schoolname){
        // if(!this.unitManageForm.checkType){
        //   // this.$util.errorMsg('必须选择学校类型！')
        //   return
        // }
        this.unitManageForm.schoolId=''
        this.findSchool()
      }
    },
    // 查询学校
    findSchool () {
      findSchoolByAreaAndSchType({
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel,
        schType:this.unitManageForm.checkType,
      }).then((res) => {
        this.schoolList = res.data
      })
    },
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    async schoolChange(val){
      this.unitManageForm.schoolname=this.schoolList.filter(item=>this.unitManageForm.schoolId==item.schId).map(item=>item.schName).join(",")
      this.unitManageForm.schStationCode=this.schoolList.filter(item=>this.unitManageForm.schoolId==item.schId).map(item=>item.schStationCode).join(",")
      if (val) {
        let schType = this.schoolList.filter(item => item.schId == val)[0].schType;
        this.unitManageForm.stuClalevel=''
        this.unitManageForm.classId=''
        this.classesList=[]
        await this.findClaLevelNew(schType)
        sessionStorage.schTypes=schType
      } else {
        this.unitManageForm.stuClalevel=""
        this.unitManageForm.classId=""
        this.classesList=[]
        await this.findClaLevelNew(2)
        sessionStorage.schTypes=2
      }
      this.$forceUpdate()
    },
    classChange () {
      if (this.defaultValue && this.classMultiple) {
        let arr = []
        this.classesList.forEach((item, index) => {
          arr.push(this.classesList[index].classId + '-' + this.classesList[index].claName)
        })
        this.unitManageForm.classId = arr
      }
      if (this.defaultValue && !this.classMultiple && this.classesList.length > 0) {
        this.unitManageForm.classId = this.classesList[0].classId
      }
      if (this.flag) {
        this.resetForm = JSON.parse(JSON.stringify(this.unitManageForm))
        this.flag = false
      }
    },
    validateForm(callback){
      if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        this.$util.errorMsg('必须选择地区！')
        return false
      }
      if(this.otherField.schoolname && !this.unitManageForm.schoolId){
        this.$util.errorMsg('请选择学校名称!')
        return false
      }
      if(!this.unitManageForm.stuclayear){
        this.$util.errorMsg('请选择学年!')
        return false
      }
      // if(this.otherField.grade && !this.unitManageForm.stuClalevel){
      //   this.$util.errorMsg('请选择年级!')
      //   return false
      // }
      if(!this.unitManageForm.lunci){
        this.$util.errorMsg('请选择轮次!')
        return false
      }
      if(this.otherField.classRequired && !this.unitManageForm.classId && !this.classSelect){
        this.$util.errorMsg('请输入班级!')
        return false
      }
      if(this.otherField.classRequired && !this.unitManageForm.classId && this.classSelect){
        this.$util.errorMsg('请选择班级!')
        return false
      }
      return true
    }
  },
  async mounted () {
    await this.findSchool()
    // 查询学年
    await this.findClaYear()
    await this.findClaLevelNew()
  }
}
</script>

<style>
.newselect{flex-wrap: wrap;}
.newselect .el-input{width:200px;}
</style>
