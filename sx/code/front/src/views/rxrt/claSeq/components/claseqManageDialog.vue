<template>
  <el-dialog title='新增' :visible.sync="visible" width="700px" :before-close="close"
    :close-on-click-modal="false" v-if="visible" class="claseq">
    <div v-if="visible">
      <el-form label-width="150px" :model="unitManageForm" ref="unitManageForm" :rules="rules" inline>
        <ss-common-area-group :columnCount="1" style="width：100%">
          <ss-common-area-item label="年份:"  prop="nowYear">
            <el-input clearable maxlength="4" v-model.trim="unitManageForm.nowYear" style="width:200px;" disabled></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="选择地区:" required>
            <ss-area-areaTreeStation ref="areaTreePage" :getToLevelNum="5 - currentLevel" :disabled="msgPubPermission" @nodeClick="nodeClick" />
          </ss-common-area-item>
          <ss-common-area-item label="出生日期:" prop="stuBirth">
            <ss-date-range v-model="unitManageForm.stuBirth" clearable class="claseqDate" />
          </ss-common-area-item>
          <ss-common-area-item label="截止时间:" prop="fillinDate">
            <el-date-picker
              v-model="unitManageForm.fillinDate"
              type="date"
              placeholder="选择日期"
              :picker-options="pickerOptions"
              value-format="yyyy-MM-dd"
              style="width:200px;">
            </el-date-picker>
            <!-- <ss-date-range v-model="unitManageForm.fillinDate" :startTimeOption="startTimeOption" :endTimeOption="endTimeOption" clearable class="claseqDate" /> -->
          </ss-common-area-item>
          <ss-common-area-item label="疫苗:" prop="vaccName">
            <el-select clearable v-model="unitManageForm.vaccName" @change="vaccNameChange" multiline placeholder="请选择疫苗">
              <el-option v-for="item in vaccinumList" :key="item.code" :label="item.name" :value="item.code" />
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item label="轮次:"  required>
            <el-input clearable maxlength="4" v-model.trim="unitManageForm.roundName" style="width:202px;" disabled placeholder="请先选择疫苗"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="学校类型:" prop="schType">
            <el-select clearable v-model="unitManageForm.schType" @change="schTypeHandler" placeholder="请选择学校类型">
              <el-option v-for="item in selectTypeList2" :key="item.code" :label="item.name" :value="item.code" />
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item label="学校名称:" prop="schIds">
            <el-select clearable v-model="unitManageForm.schIds" multiple collapse-tags placeholder="请选择学校名称" @change="claIdsChange">
              <el-option v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item label="学年:"  prop="stuclayear">
            <el-input clearable maxlength="4" v-model.trim="unitManageForm.stuclayear" style="width:200px;" disabled></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="年级:" prop="claIds">
            <el-select clearable v-model="unitManageForm.claIds" multiple collapse-tags placeholder="请选择年级" @change="claIdsChange">
              <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code" />
            </el-select>
          </ss-common-area-item>
        </ss-common-area-group>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <!-- <el-button type="primary" @click="save">确 定</el-button> -->
      <ss-custom-button @click="save" />
    </span>
  </el-dialog>
</template>
<script>
import { addRound,getRoundName,findHighClassYears } from '@/utils/axios/apis/claSeq'
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
import moment from 'moment'
export default {
  name: 'classesManageDialog',
  mixins: [commonOptions],
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    curId: {
      type: [Number, String],
      default: null
    }
  },
  data () {
    let validateDate = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请选择截至日期'))
      }
      callback()
    }
    return {
      pickerOptions: {
        disabledDate: (time) =>  {
          return time.getTime() < new Date(this.$dateutil.dayAddNum(1)).getTime() || time.getTime() > new Date(moment(this.$dateutil.dayAddNum()).add(1, 'y').format("YYYY-MM-DD")).getTime()
        }
      },
      classInfo: '',
      unitManageForm: {
        nowYear:this.$dateutil.currentYear(),
        stuclayear:this.$dateutil.currentYear(),
        schType:'',
        schIds:[],
        claIds:[],
        stuBirth:[],
        fillinDate: '',
        vaccName:'',
        roundName:''
      },
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      msgPubPermission: false,
      selectTypeList: this.$store.state.selectTypeList,
      rules: {
        fillinDate: [
          { required: true, message: '请选择截止日期', trigger: 'change' },
          { validator: validateDate, trigger: 'blur' }
        ],
        vaccName: [
          { required: true, message: '请选择疫苗', trigger: 'change' },
        ],
        schType: [
          { required: true, message: '请选择学校类型', trigger: 'change' },
        ],
        schIds: [
          { required: true, message: '请选择学校名称', trigger: 'change' },
        ],
        stuclayear: [
          { required: true, message: '请输入学年', trigger: 'blur' },
        ],
        claIds: [
          { required: true, message: '请选择年级', trigger: 'change' },
        ],
      },
      vaccinumList:this.$store.state.vaccinumList
    }
  },
  methods: {
    // 保存或编辑
    save () {
      if (this.$refs.areaTreePage.selectAreaLevel !== '4') {
        return this.$util.errorMsg('请选择到接种单位!')
      }
      let startBirth=this.unitManageForm.stuBirth[0] || ''
      let endBirth=this.unitManageForm.stuBirth[1] || ''
      // let startTime=this.unitManageForm.fillinDate[0]
      // let endTime=this.unitManageForm.fillinDate[1]
      if(!startBirth && endBirth!=''){
        return this.$util.errorMsg('开始日期不能为空')
      }
      if(!endBirth && startBirth!=''){
        return this.$util.errorMsg('截止日期不能为空')
      }
      let data={
        areaCode:this.$refs.areaTreePage.selectAreaCode,
        startBirth:startBirth,
        endBirth:endBirth,
        schIds:this.unitManageForm.schIds.join(","),
        claLevels:this.unitManageForm.claIds.sort((a,b)=>a-b).join(","),
        // claIds:this.unitManageForm.claIds.sort((a,b)=>a-b).join(","),
        claId:this.classesListNew.map(item=>item.rowkey).join(","),
        nowYear:this.unitManageForm.nowYear,
        vaccName:this.unitManageForm.vaccName,
        roundName:this.unitManageForm.roundName.slice(1,-1),
        startTime:this.$dateutil.currentDate(),
        endTime:this.unitManageForm.fillinDate
      }
      this.$refs.unitManageForm.validate().then((flag) => {
        if (flag) {
          this.$confirm('是否确定要添加'+this.unitManageForm.roundName+'次的查验结果？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(() => {
            // 新增
            addRound({
              ...data,
              operateType: this.$consts.OPTYPE_INSERT,
              cModId: this.$store.state.moduleId
            }).then((res) => {
              if (res.msg) {
                if (res.msg === '轮次信息插入成功') {
                  this.$message({
                    message: res.msg,
                    type: 'success'
                  })
                  this.$emit('fetchData')
                  this.close()
                } else {
                  this.$message({
                    message: res.msg,
                    type: 'warning'
                  })
                }
              } else {
                this.$message({
                  message: res.data,
                  type: 'success'
                })
                this.$emit('fetchData')
                this.close()
              }
            }).catch(() => {
            })
          }).catch(() => {
            this.closeFullScreen()
            this.$message({
              type: 'info',
              message: '已取消'
            })
          })
        }
      })
    },
    close () {
      this.unitManageForm.roundName=""
      this.$refs.unitManageForm.resetFields()
      this.$emit('closeDialog')
    },
    async schTypeHandler(val){
      this.unitManageForm.claIds=[]
      this.unitManageForm.schIds=[]
      this.schoolList=[]
      await this.findClaLevelSecond(val)
      await this.findSchool()
    },
    // 查询学校
    async findSchool() {
      if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        return false
      }
      if (!this.unitManageForm.schType) {
        return false
      }
      let params={
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel,
        schType: this.unitManageForm.schType
      }
      await this.findSchoolNew(params)
    },
    async nodeClick(data, checked, node) {
      this.unitManageForm.claIds=[]
      this.unitManageForm.schIds=[]
      if (this.$refs.areaTreePage.selectAreaLevel !== '4') {
        return this.$util.errorMsg('请选择到接种单位!')
      }else{
        await this.findSchool()
      }
    },
    getRound(){
      let params={
        nowYear:this.unitManageForm.nowYear,
        vaccName:this.unitManageForm.vaccName
      }
      getRoundName(params).then((res) => {
        this.unitManageForm.roundName=res.data
      }).catch(() => {
        
      })
    },
    vaccNameChange(val){
      if(val){
        this.getRound()
      }else{
        this.unitManageForm.roundName=""
      }
    },
    async claIdsChange(val){
      await this.getClass()
    },
    getClass(){
      let params = {
        schId:this.unitManageForm.schIds.join(","),
        classLevel:this.unitManageForm.claIds.sort((a,b)=>a-b).join(","),
        classYear:this.unitManageForm.stuclayear,
      }
      findHighClassYears(params).then((res) => {
        this.classesListNew=res.data
      }).catch(() => {
        
      })
    },
  },
  async created () {
    this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) == 4
  }
}
</script>
<style scoped>
  .ml_10 {
    margin-left: 10px;
  }
  .w_240 {
    width: 240px;
  }
 
</style>
<style>
.claseqDate .el-date-editor.el-input{
  width:200px!important;
}
.claseq .el-select{
  width:200px!important;
}
</style>