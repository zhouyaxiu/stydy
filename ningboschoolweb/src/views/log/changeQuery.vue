<template>
  <!-- 变更日志查询 -->
  <div>
    <ss-condition-area-new definedTitle="变更日志查询" ref="conditionPage" @setConditionFlag="setConditionFlag">
      <el-form ref="form" :model="form" size="medium" onsubmit="return false;" label-position="right" label-width="170px" :inline="true">
        <div class="condition-row">
          <el-form-item prop="stuclayear" class="item" style="width:30%">
            <div class="label" slot="label" style="font-size: 14px;color: #4B5C8C;"><span class="red">*</span>年份：</div>
            <el-select clearable v-model="form.stuclayear" placeholder="请选择">
              <el-option v-for="item in schoolYearList" :key="item.claNameYear" :label="item.claNameYear" :value="item.claNameYear"/>
            </el-select>
          </el-form-item>
          <el-form-item prop="stuName" class="item" style="width:30%" label-width="200px">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAll" @change="val => changeLogType(val, '受种者姓名')">受种者姓名：</el-checkbox>
            </div>
            <el-input clearable v-model="form.stuName" :disabled="!exportAll"></el-input>
          </el-form-item>
          <el-form-item prop="changeDate" class="item" style="width:40%">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAllDate" @change="val => changeLogType(val, '变更日期')">变更日期：</el-checkbox>
            </div>
            <ss-date-range :disabledFlag="!exportAllDate" startPlaceholder="开始日期" endPlaceholder="结束日期" v-model="form.changeDate" :startTimeOption="startTimeOption" :endTimeOption="endTimeOption" />
          </el-form-item>
        </div>
        <div class="condition-row">
          <el-form-item class="item" style="width:30%">
            <div class="label" slot="label" style="font-size: 14px;color: #4B5C8C;"><span class="red">*</span>轮次：</div>
            <el-select v-model="form.lunci" placeholder="请选择" clearable>
              <el-option label="春季" value="1" />
              <el-option label="秋季" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item prop="stuImuno" class="item" style="width:30%" label-width="200px">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAllImu" @change="val => changeLogType(val,'受种者信息介质识别码')">受种者信息介质识别码：</el-checkbox>
            </div>
            <el-input clearable v-model="form.stuImuno" :disabled="!exportAllImu"></el-input>
          </el-form-item>
          <el-form-item prop="birthDate" class="item" style="width:40%">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAllBirth" @change="val => changeLogType(val, '出生日期')">出生日期：</el-checkbox>
            </div>
            <ss-date-range :disabledFlag="!exportAllBirth" startPlaceholder="开始日期" endPlaceholder="结束日期" v-model="form.birthDate" :startTimeOption="startTimeOption" :endTimeOption="endTimeOption" />
          </el-form-item>
        </div>
        <div class="condition-row">
          <el-form-item prop="stuClalevel" class="item" style="width:30%">
            <div class="label" slot="label" style="font-size: 14px;color: #4B5C8C;"><span class="red">*</span>年级：</div>
            <el-select clearable v-model="form.stuClalevel" placeholder="请选择">
              <el-option v-for="item in gradeList" :key="item.claLevel" :label="item.claLevelCN" :value="item.claLevel"/>
            </el-select>
          </el-form-item>
          <el-form-item prop="classId" class="item" style="width:30%;" label-width="200px">
            <div class="label" slot="label" style="font-size: 14px;color: #4B5C8C;margin-right:10px;"><span class="red">*</span>班级名称：</div>
            <el-select clearable v-model="form.classId" placeholder="请选择" multiple collapse-tags>
              <el-option v-for="item in classesList" :key="item.classId" :label="item.claName" :value="item.classId"/>
            </el-select>
            <!-- <el-select v-model="form.stuClacode" placeholder="请选择班级名称" clearable>
              <el-option v-for="item in claNameList" :key="item.id" :label="item.id" :value="item.name" />
            </el-select> -->
          </el-form-item>
          <el-form-item prop="logType" class="item" style="width:30%">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAllType" @change="val => changeLogType(val, '变更类型')">变更类型：</el-checkbox>
            </div>
            <el-checkbox v-model="checkAll" :disabled="!exportAllType" @change="val => changeLogType(val, '全部')">全部</el-checkbox>
            <el-checkbox-group v-model="form.logType" :disabled="!exportAllType" class="flex">
              <el-checkbox v-for="item in logTypeList" :label="item.id" :key="item.id" @change="val => changeLogType(val, item.id)">{{item.name}}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <!-- <el-form-item label="轮次：" style="margin-left:-11px;">
            <el-select v-model="form.lunci" placeholder="请选择" clearable>
              <el-option label="春季" value="1" />
              <el-option label="秋季" value="0" />
            </el-select>
          </el-form-item> -->
        </div>
        <div class="condition-row mt_10">
          <el-form-item class="item" style="margin-left: 170px;">
            <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
            <ss-custom-button @click="reset" :type="$consts.CUSTOMBUTTONTYPES.reset" class="ml_10" />
          </el-form-item>
        </div>
      </el-form>
    </ss-condition-area-new>
    <div class="main-content">
      <ss-table :data="dataList" ref="tabPage" class="infoTable" :pageSize="20" @goPage="search" :pagenum="pagenum" :total="total">
        <el-table-column prop="logClassName" align="center" min-width="150" label="受种者班级" />
        <el-table-column prop="logStuimuno" align="center" min-width="200" label="受种者信息介质识别码" />
        <el-table-column prop="logStuname" align="center" min-width="100" label="受种者姓名" />
        <el-table-column prop="logStubirth" align="center" min-width="100" label="出生日期" />
        <el-table-column prop="logUpdatetype" align="center" min-width="100" label="变更类型" />
        <el-table-column prop="logUpdatetime" align="center" min-width="100" label="变更日期" />
        <el-table-column prop="logUpcontent" align="center" min-width="400" label="变更内容" />
      </ss-table>
    </div>
  </div>
</template>
<script>
  import { queryChangeLog } from '@/utils/axios/apis/log'
  import commonOptions from '@/mixin/commonOptions'
  export default {
    name: 'vaccinationInspection',
    mixins: [commonOptions],
    data () {
      return {
        type: '6',
        dataList: [],
        // schoolYearList:[],
        // gradeList: [],
        // claNameList: [],
        // classesList: [],
        exportAreaName: '',
        cModId: this.$store.state.moduleId,
        // schoolYearList: [],
        logTypeList:[{id:0,name:'修改'},{id:1,name:'删除'}],
        pagenum: 1,
        total: 0,
        checkAll:true,
        exportAll:false,
        exportAllDate:false,
        exportAllImu:false,
        exportAllType:false,
        exportAllBirth:false,
        // disabled:true,
        startTimeOption: {
          disabledDate: time => {
            return (
              time.getTime() > new Date(this.$dateutil.dayAddNum()).getTime()
            )
          }
        },
        endTimeOption: {
          disabledDate: time => {
            return (
              time.getTime() > new Date(this.$dateutil.dayAddNum()).getTime()
            )
          }
        },
        form:{
          stuclayear: '',
          stuClalevel: '',
          classId: [],
          claCode:'',
          stuImuno:'',//受种者介质码
          stuName:'',//受种者姓名
          birthDate: [],//出生日期
          changeDate:[],//变更日期
          logType:[0,1],//变更类型
          lunci:sessionStorage.getItem('defaultLunci')
        }
      }
    },
    computed: {
      unitManageFormCopy () {
        return JSON.parse(JSON.stringify(this.form))
      }
    },
    watch: {
      unitManageFormCopy: {
        deep: true,
        async handler (newVal, oldVal) {
          // 学年变化，查找年级
          if (newVal.stuclayear !== oldVal.stuclayear) {
            this.form.stuClalevel = ''
            this.form.classId = []
            // this.form.claName = ''
            if (newVal.stuclayear && this.form.lunci) {
              await this.findClaLevelNew({ schId: this.$store.state.schId,claNameYear: newVal.stuclayear,lunci:this.form.lunci })
              // if (this.defaultValue) {
                this.form.stuClalevel = this.gradeList.length>0?this.gradeList[0].claLevel:''
              // }
              // 查找班级
              if(this.gradeList.length>0){
                await this.findClassNew({ schId: this.$store.state.schId,claLevel: this.form.stuClalevel, claNameYear: newVal.stuclayear,lunci:this.form.lunci })
                this.form.classId = this.classesList && this.classesList.length > 0 ? [this.classesList[0].classId] : []
              }
            } else {
              this.gradeList = []
              this.classesList = []
            }
          }
          // 年级发生改变
          if (newVal.stuClalevel !== oldVal.stuClalevel) {
            // this.form.stuClalevel = ''
            this.form.classId = []
            // this.form.claName = ''
            if (newVal.stuClalevel && this.form.lunci) {
            // 查找班级
              await this.findClassNew({ schId: this.$store.state.schId,claLevel: newVal.stuClalevel, claNameYear: newVal.stuclayear,lunci:this.form.lunci })
              this.form.classId = this.classesList && this.classesList.length > 0 ? [this.classesList[0].classId] : []
              // this.classChange()
            } else {
              this.classesList = []
            }
          }
        }
      },
      async "form.lunci"(newVal,oldVal){
      if(newVal==''){
        this.form.stuClalevel = ''
        this.form.classId = []
        this.classesList = []
        this.gradeList = []
        return
      }
      if(newVal!=oldVal && newVal!='' && this.form.lunci){
        this.form.stuClalevel = ''
        this.form.classId = []
        // 查找年级
        await this.findClaLevelNew({ schId: this.$store.state.schId,claNameYear: this.form.stuclayear,lunci:newVal })
        // this.form.stuClalevel.push(this.gradeList[0].claLevel)
        this.form.stuClalevel = this.gradeList.length>0?this.gradeList[0].claLevel:''
        // 查找班级
        if(this.gradeList.length>0){
          await this.findClassNew({ schId: this.$store.state.schId,claLevel: this.form.stuClalevel, claNameYear: this.form.stuclayear,lunci:newVal })
          this.form.classId = this.classesList && this.classesList.length > 0 ? [this.classesList[0].classId] : []
        }
        // this.form.classId=this.classesList.length>0?this.classesList.map(item=>item.classId):[]
      }
    }
    },
    methods: {
      classChange () {
        let arr = []
        this.classesList.forEach((item, index) => {
          arr.push(this.classesList[index].classId)
        })
        this.form.classId = arr
      },
      setConditionFlag (falg) {
        this.$refs.tabPage.setHeight()
      },
      changeLogType(val,name){
        if(!val && name=='变更日期'){this.form.changeDate=[];}
        if(!val && name=='出生日期'){this.form.birthDate=[];}
        if(val && name=='变更类型') this.disabled=false;
        if(!val && name=='受种者姓名') {this.form.stuName='';}
        if(!val && name=='受种者信息介质识别码') {this.form.stuImuno='';}
        if(!val && name=='变更类型') {this.disabled=true;this.form.logType=[0,1];this.checkAll=true}
        if(!val && name!='变更类型' && name!='全部' && name!='受种者信息介质识别码' && name!='受种者姓名' && name!='变更日期' && name!='出生日期') this.checkAll=false;
        if(val && name!='变更类型' && name!='全部' && name!='受种者信息介质识别码' && name!='受种者姓名' && name!='变更日期' && name!='出生日期' && this.form.logType.length==2) this.checkAll=true;
        if(!val && name=='全部') this.form.logType=[];
        if(val && name=='全部') this.form.logType=[0,1];
      },
      // 查询接口
      search (pagenum) {
        let that = this
        if (!pagenum) {
          pagenum = 1
        }
        this.pagenum = pagenum
        let updateDate = this.form.changeDate[0] || ''
        let endDate = this.form.changeDate[1] || ''
        let beginBirth = this.form.birthDate[0] || ''
        let endBirth = this.form.birthDate[1] || ''
        if(that.exportAll && !that.form.stuName){
          that.$util.errorMsg('受种者姓名必填')
          return false
        }
        if(that.exportAllDate && !updateDate && !endDate){
          that.$util.errorMsg('变更日期必填')
          return false
        }
        if(that.exportAllImu && !that.form.stuImuno){
          that.$util.errorMsg('受种者信息介质识别码必填')
          return false
        }
        if(that.exportAllBirth && !beginBirth && !endBirth){
          that.$util.errorMsg('出生日期必填')
          return false
        }
        if ((!updateDate && endDate) || (updateDate && !endDate)) {
          this.$util.errorMsg('变更日期请填写完整再查询！')
          return false
        }
        if ((!beginBirth && endBirth) || (beginBirth && !endBirth)) {
          this.$util.errorMsg('出生日期请填写完整再查询！')
          return false
        }
        if(that.form.classId.length==0){
          that.$util.errorMsg('班级名称至少选择一个')
          return false
        }
        if(that.exportAllType && that.form.logType.length==0){
          that.$util.errorMsg('变更类型至少选择一个')
          return false
        }
        let params = {
          beginBirth:beginBirth,
          endBirth:endBirth,
          userId:sessionStorage.id,
          classCode: that.form.classId.join(","),
          stuImuno: that.form.stuImuno,
          stuName: that.form.stuName,
          logType:this.exportAllType?this.form.logType.join(","):'',
          updateDate:updateDate,
          endDate:endDate,
          pageIndex:this.pagenum,
        }
        that.searchData = params
        that.openFullScreen()
        queryChangeLog(that.searchData).then((res) => {
          this.dataList = res.data.list;
          this.total=res.data.total;
          that.closeFullScreen()
        }).catch(() => {
          that.closeFullScreen()
        })
      },
      async reset(){
        this.exportAll=false;
        this.exportAllDate=false;
        this.exportAllImu=false;
        this.exportAllType=false;
        this.checkAll=true;
        this.exportAllBirth=false;
        this.birthDate=[];
        this.logType=[0,1];
        this.$refs.form.resetFields();
        this.dataList=[];
        this.total=0;
        this.pagenum=1;
        // 查询学年
        await this.findClaYear()
        if(this.schoolYearList.length>0){
          this.form.stuclayear = this.schoolYearList[0].claNameYear
        }
      },
    },
    async mounted () {
      // 查询学年
      await this.findClaYear()
      if(this.schoolYearList.length>0){
        this.form.stuclayear = this.schoolYearList[0].claNameYear
      }
    }
  }
</script>