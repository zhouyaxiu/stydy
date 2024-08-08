<template>
  <!-- 查验接种信息 -->
  <div class="checkVaccination">
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <ss-class-select ref="selectClass" :classMultiple="false" :ischeck="true" :classSelect="true"
        :otherField="{ 'schoolname': true, 'grade': true, 'classname': true, 'gradeRequired': true, 'schoolnameRequired': true }">
        <div class="item">
          <div class="label">是否种全：</div>
          <el-select v-model="unitManageForm.isjz" placeholder="请选择是否种全" clearable
            :class="unitManageForm.isjz==1? 'isjzSelects' : ''">
            <el-option label="全部" value="" />
            <el-option label="是" value="0" />
            <el-option label="否" value="1" />
          </el-select>
          <el-select v-model="unitManageForm.isjzTemp" placeholder="请选择" clearable
            class="isjzSelect ml_10" v-if="unitManageForm.isjz==1">
            <el-option label="需补种" value="1" />
            <el-option label="无需补种" value="-1" />
          </el-select>
        </div>
        <div class="item">
          <div class="label">查验状态：</div>
          <el-select v-model="unitManageForm.status" placeholder="请选择查验状态" clearable>
            <el-option label="全部" value="" />
            <el-option label="已种全" value="0" />
            <el-option label="未种全需补种" value="1" />
            <el-option label="未种全无需补种" value="-1" />
            <el-option label="无接种证" value="2" />
          </el-select>
        </div>
        <div class="item">
          <div class="label">儿童姓名：</div>
          <el-input placeholder="请输入儿童姓名" clearable v-model="unitManageForm.name"></el-input>
        </div>
        <div class="item">
          <div class="label">身份证件号码：</div>
          <el-input placeholder="请输入身份证件号码" clearable v-model="unitManageForm.idcard"></el-input>
        </div>
        <div class="item">
          <div class="label">受种者编码：</div>
          <el-input placeholder="请输入受种者编码" clearable v-model="unitManageForm.code"></el-input>
        </div>
      </ss-class-select>
      <div class="mt_10" style="display: flex;">
        <ss-custom-button @click="search(1,'search')" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <ss-custom-button v-if="$store.state.modelObject != 0" @click="exportExcelFun"
          :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
        <!-- <ss-custom-button v-if="$store.state.modelObject != 0" @click="checkHandler()"
          :type="$consts.CUSTOMBUTTONTYPES.check" class="ml_10" />
        <ss-custom-button v-if="$store.state.modelObject != 0" @click="deleteHandler()"
          :type="$consts.CUSTOMBUTTONTYPES.delete" class="ml_10" />
        <ss-custom-button v-if="$store.state.modelObject != 0" :type="$consts.CUSTOMBUTTONTYPES.alter"
          @click="alterHandler()" class="ml_10" /> -->
        <ss-custom-button v-if="$store.state.modelObject != 0" :type="$consts.CUSTOMBUTTONTYPES.buzhong"
          @click="mobanHandler('预防接种证查验疫苗补种联系', 0)" class="ml_10" />
        <ss-custom-button v-if="$store.state.modelObject != 0" :type="$consts.CUSTOMBUTTONTYPES.buzheng"
          @click="mobanHandler('预防接种证查验疫苗补证联系', 1)" />
        <ss-custom-button v-if="$store.state.modelObject != 0" :type="$consts.CUSTOMBUTTONTYPES.chayan"
          @click="chayanHandler()" />
      </div>
    </ss-condition-area>
    <div style="position:relative;">
      <custom-in-table :changeRowBg="true" :rowBgList="tipList" :changeRowAttr="'stuIsjz'" style="width: 100%"
        @selectionChange="handleSelectionChange" :data="dataList" :columnData="columnData" ref="tabPage" class="vaccination" :pageSize="$store.state.pagesize" :changeNull="true" @goPage="search" @rowOperateFun="rowOperateFun" :pagenum="pagenum" :total="total"></custom-in-table>
      <div class="tags" style="position: absolute;bottom: 10px;" v-if="dataList.length > 0">
        <div><span></span>需补种</div>
        <div><span></span>需补证</div>
      </div>
    </div>
    <export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'查验接种信息'"
      :dialogVisible="exportDialogVisible"></export-dialog>
    <formwork-dialog :visible="dialogVisible" v-if="dialogVisible" @close="close" :formData="formData"
      :stuData="stuData" />
    <check-dialog :visible="checkDialogVisible" v-if="checkDialogVisible" @close="closeCheck" :dataList="checkList" />
    <!-- 详细信息修改弹框 -->
    <alter-dialog :visible="alterdialogVisible" :rowkey="rowkey" @close="closeAlter" />
    <!-- 打印建档通知单 -->
    <print-noice :visible="dialogNoiceVisible" :isAfter="true" @close="closeModal" :noiceList="noiceList"
      title="预防接种证查验疫苗建档联系通知单" />
  </div>
</template>
<script>
import { queryList, deletes, check, output, queryNoArchive, queryCheckCert, queryReplantMess, queryReplantCard } from '@/utils/axios/apis/rxrt/checkVaccination'
import commonOptions from '@/mixin/commonOptions'
import exportDialog from '@/components/common/exportDialog'
import formworkDialog from './components/formworkDialog.vue'
import customInTable from './components/customInTable.vue'
import { tableHeader } from './tableHeader'
import checkDialog from './components/checkDialog.vue'
import alterDialog from './components/alertDialog.vue'
import printNoice from '../showInforInspectionResultQuery/components/printNoice.vue'
import { queryNoticeMess } from '@/utils/axios/apis/notice/index'

export default {
  name: 'vaccinationInspection',
  components: {
    exportDialog,
    formworkDialog,
    checkDialog,
    alterDialog,
    printNoice,
    customInTable
  },
  mixins: [commonOptions],
  data() {
    return {
      dataList: [],
      columnData: tableHeader,
      secTime: 0,
      tipText: '统计截止到：' + this.$util.today() + '。',
      pagesize: 20,
      total: 0,
      pagenum: 1,
      exportDialogVisible: false,
      alterdialogVisible: false,
      dialogVisible: false,
      /* 以下是弹框详细 */
      detailDialog: false,
      detailParams: {},
      exportAreaName: '',
      cModId: this.$store.state.moduleId,
      selectResiTypeFlag: true,
      unitManageForm: {
        status: '',
        isjz: '',
        isjzTemp:'',
        name:'',
        idcard:'',
        code:''
      },
      formData: {},
      stuData: [],
      checkDialogVisible: false,
      rowkey: '',
      dialogNoiceVisible: false,
      noiceList: [],
      gradeList: this.$util.getGrade(2),
      noticeContent: '',
      checkList: [],
      tipList: [
        {
          text: '需补种',
          style: { background: '#E8FFE8' },
          stuIsjz: '1',
          className: 'row-green'
        },
        {
          text: '无接种证',
          style: { background: '#E9FFFF' },
          stuIsjz: '2',
          className: 'row-yellow'
        },
      ]
    }
  },
  methods: {
    // 打印查验证明
    chayanHandler() {
      if (!this.multipleSelection || this.multipleSelection.length === 0) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      if (this.stuIsjz.includes("2")) {
        this.$util.errorMsg('未匹配上的无接种证的不能打印查验证明！')
        return
      }
      let params = JSON.parse(JSON.stringify(this.searchData))
      params.rowkey = this.multipleSelection.join(',')
      queryCheckCert(params).then(res => {
        this.checkList = res.data.list;
        this.$nextTick(()=>{
          this.checkDialogVisible = true;
        })
      })
    },
    // 打印建档通知单
    noiceHandler() {
      if (!this.multipleSelection || this.multipleSelection.length === 0) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      this.noiceList = []
      let params = JSON.parse(JSON.stringify(this.searchData))
      params.rowkey = this.multipleSelection.join(',')
      let unitManageForm = this.$refs.selectClass.unitManageForm
      queryNoticeMess({
        noticeType: 2,
        schId: unitManageForm.schoolId
      }).then(res => {
        if (this.stuIsjzOrigin.includes("0") || this.stuIsjzOrigin.includes("1") || this.stuIsjzOrigin.includes("-1") || this.stuIsjzOrigin.includes("")) {
          this.$util.errorMsg('存在已建档的数据，无需打印！')
          return
        }
        this.noticeContent = res.data[0].noticeContent
        queryNoArchive(params).then(res => {
          this.noiceList = res.data.list
          this.noiceList = this.noiceList.map(item => {
            return {
              stuName: item.stuName,
              schoolName: item.schName,
              gradeName: this.gradeList.filter(sub => sub.code == item.claLevel)[0].name,
              className: item.claName,
              stuParentname: item.parentName,
              schStationName: item.belongSchoolStation,
              noticeContent: this.noticeContent,
              stuBirth: item.stuBirth
            }
          })
          this.dialogNoiceVisible = true;
        })
      })
    },
    rowOperateFun (row, btnType) {
      if(btnType=='delete'){
        this.deleteHandler(row.rowkey)
      }else if(btnType=='alter'){
        this.alterHandler(row.rowkey)
      }else{
        this.checkHandler(row.rowkey)
      }
    },
    mobanHandler(title, maintype) {
      if (!this.multipleSelection || this.multipleSelection.length === 0) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      this.formData = {
        title: title + '通知单',
        maintype: maintype
      }
      let params = {
        schId: this.searchData.schId,
        noticeType: maintype,
      }
      // if (maintype == 0) {
      //   this.formData.customcontent = true
      //   this.formData.isZiFei = true
      // }
      queryNoticeMess(params).then(res => {
        this.formData.content = res.data[0].noticeContent
        this.formData = Object.assign({}, this.formData, {
          'content': res.data[0].noticeContent,
        })
      }).catch(() => {
      })
      let paramsNew = {
        ...this.searchData,
        rowkey: this.multipleSelection.join(',')
      }
      if (maintype == 0) {
        if (this.stuIsjz.findIndex(item=>item==2)>-1) {
          this.$util.errorMsg('选中的受种者存在无接种证状态，请先去补证！')
          return
        }
        if (this.stuIsjz.findIndex(item=>item==-1)>-1) {
          this.$util.errorMsg('选中的受种者无需补种，请重新选择！')
          return
        }
        if (this.stuIsjz.findIndex(item=>item==0)>-1) {
          this.$util.errorMsg('选中的受种者存在已种全状态，请重新选择！')
          return
        }
        queryReplantMess({ ...paramsNew }).then((res) => {
          for (let i = 0; i < res.data.list.length; i++) {
            let arr = []
            for (let key in res.data.list[i].replantMap) {
              arr.push({ name: key, value: res.data.list[i].replantMap[key] })
            }
            res.data.list[i].tableData = arr
          }
          this.stuData = res.data.list
          this.$nextTick(() => {
            this.dialogVisible = true;
          })
        }).catch(() => {
        })
      }
      if (maintype == 1) {
        if (this.stuIsjzz.includes("0") || this.stuIsbjzz.includes("0")) {
          this.$util.errorMsg('已有接种证，无需打印！')
          return
        }
        queryReplantCard({ ...paramsNew }).then((res) => {
          this.stuData = res.data.list
          this.$nextTick(() => {
            this.dialogVisible = true;
          })
        }).catch(() => {
        })
      }
    },
    checkHandler(rowkey) {
      if (rowkey === undefined && (!this.multipleSelection || this.multipleSelection.length === 0)) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      check({ rowkey: rowkey || this.multipleSelection.join(',') }).then((res) => {
        this.$message({
          type: 'success',
          message: res.popMsg || '查验成功'
        })
        this.search()
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    // 查询接口
    async search(pagenum,names) {
      let unitManageForm = this.$refs.selectClass.unitManageForm
      if(this.unitManageForm.name=='' && this.unitManageForm.idcard=='' && this.unitManageForm.code==''){
        let flag = this.$refs['selectClass'].validateForm();
        if (!flag) {
          return false;
        }
      }
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      let params = {
        areaCode: that.$refs.selectClass.$refs.areaTreePage.selectAreaCode,
        areaLevel: that.$refs.selectClass.$refs.areaTreePage.selectAreaLevel,
        grade: unitManageForm.stuClalevel,//年级
        schId: unitManageForm.schoolId,//学校主键
        year: unitManageForm.stuclayear,//学年
        rounds: unitManageForm.lunci,
        class: unitManageForm.classId,//班级
        code: this.unitManageForm.code,//儿童编码
        idcard: this.unitManageForm.idcard,//身份证件号码
        isjz: this.unitManageForm.isjz==1?this.unitManageForm.isjzTemp==''?'1,-1':this.unitManageForm.isjzTemp:this.unitManageForm.isjz,//是否种全
        name: this.unitManageForm.name,//儿童姓名
        pageNum: pagenum,
        pageSize: this.pagesize,
        status: this.unitManageForm.status,//查验状态
        schType: unitManageForm.checkType,//学校类型
        operateType: this.$consts.OPTYPE_QUERY
      }
      // that.exportAreaName = that.$refs.areaTreePage.selectAreaName + ' ' + that.$refs.areaTreePage.selectAreaCode
      that.searchData = params
      that.openFullScreen()
      queryList(that.searchData).then((res) => {
        this.dataList = res.data.list
        that.secTime = res.sec
        that.total = res.data.total
        if(names=='search'){
          this.$refs.tabPage.$refs.itsmDataTable.clearSelection();
          this.multipleSelection=null;
        }
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
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
      params.schName = this.$refs.selectClass.unitManageForm.schoolname
      params.pageNum=pageNum
      params.operateType = this.$consts.OPTYPE_OUTPUT
      // console.log('导出参数', params)
      output(params)
    },
    handleSelectionChange(val) {
      this.multipleSelection = val.map(item => item.rowkey)
      this.stuIsjz = val.map(item => item.stuIsjz)
      this.stuIsjzz = val.map(item => item.stuIsjzz)
      this.clazzName = new Set(val.map(item => item.clazzName))
      this.grade = new Set(val.map(item => item.grade))
      this.stuIsjzOrigin=val.map(item=>item.stuIsjzOrigin)
      this.stuIsbjzz=val.map(item=>item.stuIsbjzz)
    },
    // 删除
    deleteHandler(rowkey) {
      if (rowkey === undefined && (!this.multipleSelection || this.multipleSelection.length === 0)) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      this.$confirm('您确定要删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletes({ rowkey: rowkey || this.multipleSelection.join(','), operateType: this.$consts.OPTYPE_DELETE }).then((res) => {
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
    // 修改
    alterHandler(rowkey) {
      if (rowkey === undefined && (!this.multipleSelection || this.multipleSelection.length === 0)) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      if (rowkey === undefined && (this.grade == undefined || this.grade.size > 1)) {
        this.$util.errorMsg('请先选中查询页面的年级，再进行勾选批量修改！')
        return false
      }
      if (rowkey === undefined && (this.clazzName == undefined || this.clazzName.size > 1)) {
        this.$util.errorMsg('请先选中查询页面的班级，再进行勾选批量修改！')
        return false
      }
      this.rowkey = rowkey || this.multipleSelection.join(',');
      this.$nextTick(() => {
        this.alterdialogVisible = true
      })
    },
    close() {
      this.dialogVisible = false;
      this.formData = {};
    },
    closeCheck() {
      this.checkDialogVisible = false;
    },
    closeModal() {
      this.dialogNoiceVisible = false;
      this.noiceList = []
    },
    closeAlter() {
      this.alterdialogVisible = false;
      this.rowkey = "";
    }
  },
  mounted() {
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