<template>
  <!-- 特定疫苗查验接种信息 -->
  <div class="checkVaccination">
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <select-class ref="selectClass">
        <div class="item">
          <div class="label">儿童姓名：</div>
          <el-input placeholder="请输入儿童姓名" clearable v-model="unitManageForm.stuName" style="width:200px"></el-input>
        </div>
        <div class="item">
          <div class="label">身份证件号码：</div>
          <el-input placeholder="请输入身份证件号码" clearable v-model="unitManageForm.cid" style="width:200px"></el-input>
        </div>
      </select-class>
      <div class="mt_10" style="display: flex;">
        <ss-custom-button @click="search(1,'search')" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
        <ss-custom-button @click="deleteHandler()" v-if="msgPubPermission" :type="$consts.CUSTOMBUTTONTYPES.batchDelete" class="ml_10" />
        <ss-custom-button :type="$consts.CUSTOMBUTTONTYPES.cuizhong" v-if="msgPubPermission" @click="mobanHandler()" class="ml_10" />
      </div>
    </ss-condition-area>
    <ss-table :data="dataList" ref="tabPage" class="infoTable" @goPage="search" :pagenum="pagenum" :total="total" @handleSelectionChange="handleSelectionChange">
      <el-table-column type="selection" align="center" width="55"></el-table-column>
      <el-table-column align="center" min-width="80" label="操作" v-if="msgPubPermission">
        <template slot-scope="{ row }">
          <el-row>
            <el-button @click.native="deleteHandler(row.roundId,row.stuNo)" type="text" class="table-btn">删除</el-button>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column prop="roundName" align="center" min-width="100" label="轮次">
        <template slot-scope="{ row }">
          <span>第{{ row.roundName }}轮</span>
        </template>
      </el-table-column>
      <el-table-column prop="claNameyear" align="center" min-width="80" label="学年" />
      <el-table-column prop="schName" align="center" min-width="100" label="学校名称" />
      <el-table-column prop="claLevel" align="center" min-width="100" label="年级" />
      <el-table-column prop="claName" align="center" min-width="140" label="班级" />
      <el-table-column prop="stuName" align="center" min-width="100" label="姓名" />
      <el-table-column prop="cid" align="center" min-width="170" label="身份证件号码" />
      <el-table-column prop="fresideAddr" align="center" min-width="100" label="现住地址" show-overflow-tooltip />
      <template v-if="vaccinumName=='流感疫苗'">
        <el-table-column prop="firstDose" align="center" min-width="100" label="流感疫苗" />
        <!-- <el-table-column prop="secondDose" align="center" min-width="100" label="流感疫苗2" /> -->
      </template>
      <template v-else-if="vaccinumName=='水痘疫苗'">
        <el-table-column prop="firstDose" align="center" min-width="100" label="水痘疫苗1" />
        <el-table-column prop="secondDose" align="center" min-width="100" label="水痘疫苗2" />
      </template>
      <template v-else>
        <el-table-column prop="stuType" align="center" min-width="120" label="户籍类型" />
        <el-table-column prop="firstDose" align="center" min-width="130" label="本轮强化已接种" />
        <el-table-column prop="secondDose" align="center" min-width="130" label="本轮强化需补种" />
        <el-table-column prop="mark" align="center" min-width="140" label="备注" />
      </template>
    </ss-table>
    <export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'特定疫苗查验接种信息'"
      :dialogVisible="exportDialogVisible"></export-dialog>
    <formwork-dialog :visible="dialogVisible" v-if="dialogVisible" @close="close" :stuData="stuData" :vaccinumName="vaccinumName" />
  </div>
</template>
<script>
import { findCheckResult,outPutCheckResult,queryPrintBuZhong,deleteCheckResult } from '@/utils/axios/apis/speccheckVaccination'
import commonOptions from '@/mixin/commonOptions'
import exportDialog from '@/components/common/exportDialog'
import formworkDialog from './components/formworkDialog.vue'
import selectClass from './components/selectClass.vue'
export default {
  name: 'vaccinationInspection',
  components: {
    exportDialog,
    formworkDialog,
    selectClass,
  },
  mixins: [commonOptions],
  data() {
    return {
      dataList: [],
      secTime: 0,
      tipText: '统计截止到：' + this.$util.today() + '。',
      pagesize: 20,
      total: 0,
      pagenum: 1,
      exportDialogVisible: false,
      dialogVisible: false,
      exportAreaName: '',
      cModId: this.$store.state.moduleId,
      selectResiTypeFlag: true,
      // formData: {},
      stuData: [],
      rowkey: '',
      noiceList: [],
      noticeContent: '',
      msgPubPermission:false,
      unitManageForm:{
        stuName:'',
        cid:''
      },
      vaccinumName:''
    }
  },
  methods: {
    // 批量选择
    handleSelectionChange (val) {
      if (val.length > 0) {
        this.rowKeys = val.map(function (elem) { return elem.rowkey }).join(',')
      } else {
        this.rowKeys = ''
      }
    },
    mobanHandler() {
      if (!this.multipleSelection || this.multipleSelection.length === 0) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }

      if(this.Dose && this.Dose.length>0 && this.Dose.includes(false)){
        this.$util.errorMsg('选中的受种者无需催种，请重新选择！')
        return
      }
      let params = {
        roundId: this.roundId,
        stuNos: this.multipleSelection.join(","),
      }
      queryPrintBuZhong(params).then(res => {
        this.stuData=res.data
        this.dialogVisible = true;
      }).catch(() => {
      })
    },
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    // 查询接口
    async search(pagenum,names) {
      let unitManageForm = this.$refs.selectClass.unitManageForm
      if(this.unitManageForm.stuName=='' && this.unitManageForm.cid==''){
        let flag = this.$refs['selectClass'].validateForm();
        if (!flag) {
          return false;
        }
      }
      
      if (!pagenum) {
        pagenum = 1
      }
      this.pagenum = pagenum
      let params = {
        schId: unitManageForm.schId,//学校主键
        nowYear:unitManageForm.nowYear,
        vaccName:unitManageForm.vaccName,
        roundName:unitManageForm.roundName.slice(1,-1),
        claYear:unitManageForm.claYear,
        claLevel:unitManageForm.claLevel,
        claId:unitManageForm.claId,
        cid:this.unitManageForm.cid,
        stuName:this.unitManageForm.stuName,
        pageNum:this.pagenum,
        operateType: this.$consts.OPTYPE_QUERY
      }
      this.vaccinumName=unitManageForm.vaccName
      this.searchData = {
        ...params,
        schType:unitManageForm.schType,
        claName:unitManageForm.claId!=''?this.$refs.selectClass.classesList.filter(item=>item.rowkey==unitManageForm.claId)[0].claShortName:'',
        schName:unitManageForm.schId!=''?this.$refs.selectClass.schoolList.filter(item=>item.schId==unitManageForm.schId)[0].schName:''
      }
      this.openFullScreen()
      findCheckResult(this.searchData).then((res) => {
        this.dataList = res.data.list
        this.secTime = res.sec
        this.total = res.data.total
        if(names=='search'){
          this.$refs.tabPage.$refs.itsmDataTable.clearSelection();
          this.multipleSelection=null;
        }
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    handleExportClose() {
      this.exportDialogVisible = false
    },
    exportExcelFun() {
      if (!this.searchData || this.searchData.areaCode!=sessionStorage.getItem('areacodeSelect')) {
        this.$util.errorMsg('查询后才可以导出！')
        return
      }
      if (this.dataList.length === 0) {
        this.$util.errorMsg('暂无需要导出的数据！')
        return
      }
      this.exportDialogVisible = true
    },
    exportExcel(pageNum) {
      let params = JSON.parse(JSON.stringify(this.searchData))
      params.areaName = this.$refs.selectClass.$refs.areaTreePage.selectAreaName
      params.claLevelName=this.getClaLevelNameNew(params.claLevel)
      params.operateType = this.$consts.OPTYPE_OUTPUT
      params.pageNum=pageNum
      outPutCheckResult(params)
    },
    handleSelectionChange(val) {
      if (val.length > 0) {
        this.multipleSelection = val.map(item => item.stuNo)
        this.roundId = val.map(item => item.roundId)[0]
        if(this.$refs.selectClass.unitManageForm.vaccName=='麻腮风疫苗' || this.$refs.selectClass.unitManageForm.vaccName=='流感疫苗'){
          this.Dose=val.map(item => item.firstDose=='❌')
        }else{
          this.Dose=val.map(item => item.firstDose=='❌' || item.secondDose=='❌')
        }
      } else {
        this.multipleSelection = []
        this.roundId =''
        this.Dose=[]
      }
    },
    // 删除
    deleteHandler(roundId,stuNo) {
      if (stuNo === undefined && (!this.multipleSelection || this.multipleSelection.length === 0)) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      this.$confirm('您确定要删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCheckResult({ roundId: roundId || this.roundId,stuNos:stuNo || this.multipleSelection.join(','),operateType: this.$consts.OPTYPE_DELETE }).then((res) => {
          this.$message({
            type: 'success',
            message: res.popMsg || '删除成功'
          })
          this.search()
          this.closeFullScreen()
        }).catch(() => {
          this.closeFullScreen()
        })
      }).catch(() => {
      })
    },
    close() {
      this.dialogVisible = false;
    },
  },
  async mounted() {
    this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) == 4
  }
}
</script>
<style scoped>
.tags {
  position: absolute;
  bottom: 30px;
  display: flex;
}

.tags div {
  margin-right: 20px;
}

.tags span {
  width: 14px;
  height: 14px;
  background: #e8ffe8;
  display: inline-block;
  margin-right: 4px;
  vertical-align: middle;
}

/* .tags div:nth-child(2) span{background: #e8ffe8;} */
.tags div:last-child span {
  background: #f7ffff;
}</style>
<style>.isjzSelect .el-input {
  width: 134px;
}

.isjzSelect {
  min-width: 134px !important;
  margin-left: 6px;
}

.isjzSelects .el-input {
  width: 60px;
}

.isjzSelects {
  min-width: 60px !important;
  width: 60px;
}</style>