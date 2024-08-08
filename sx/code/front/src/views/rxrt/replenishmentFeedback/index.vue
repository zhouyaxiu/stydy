<template>
  <!-- 补种情况反馈表 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <ss-class-select ref="selectClass" :ischeck="true" :classSelect="true" :otherField="{'schoolname':true,'grade': true,'classname':true,'class':true,'schoolnameRequired':false}"></ss-class-select>
      <div class="mt_10" style="display: flex;">
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
        <button ref="printBtn" style="display:none" v-print="print" class="ml_10">打印</button>
        <ss-custom-button @click="prints" :type="$consts.CUSTOMBUTTONTYPES.print" class="ml_10" />
      </div>
    </ss-condition-area>
    <ss-table :data="dataList" ref="tabPage" class="vaccination" :pageSize="$store.state.pagesize" :changeNull="true" @goPage="search" :pagenum="pagenum" :total="total">
      <el-table-column type="index" align="center" width="80" label="序号" />
      <el-table-column prop="schName" align="center" label="学校" />
      <el-table-column prop="claLevel" align="center" min-width="120" label="年级" />
      <el-table-column prop="claName" align="center" min-width="80" label="班级" />
      <el-table-column prop="stuName" align="center" min-width="100" label="儿童姓名" />
      <el-table-column prop="stuBirth" align="center" min-width="120" label="出生日期" />
      <el-table-column prop="replantName" align="center" min-width="120" label="补种疫苗名称" />
      <el-table-column prop="replateDate" align="center" min-width="150" label="补种时间" />
      <el-table-column prop="ifFull" align="center" min-width="120" label="是否种全">
        <template slot-scope="{ row }">
          <span>{{ row.ifFull == '0' ? '是' : row.ifFull == '1'?'否-需补种':row.ifFull == '-1'?'否-无需补种':'' }}</span>
        </template>
      </el-table-column>
    </ss-table>
    <export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'补种情况反馈报表'" :dialogVisible="exportDialogVisible"></export-dialog>
    <div v-show="printShow">
      <!-- 打印表格 -->
      <vaccination-table :dataList="dataList" id="reseed_table">
      </vaccination-table>
    </div>
  </div>
</template>
<script>
  import { queryReplantMess,outPutReplantMess } from '@/utils/axios/apis/rxrt/replenishmentFeedback'
  import commonOptions from '@/mixin/commonOptions'
  import exportDialog from '@/components/common/exportDialog'
  import vaccinationTable from './components/vaccinationTable.vue'
  export default {
    name: 'vaccinationInspection',
    components: {
      exportDialog,
      vaccinationTable
    },
    mixins: [commonOptions],
    data () {
      return {
        type: '6',
        dataList: [],
        secTime: 0,
        tipText: '统计截止到：' + this.$util.today() + '。',
        pagesize: 20,
        total: 0,
        pagenum: 1,
        exportDialogVisible: false,
        /* 以下是弹框详细 */
        detailDialog: false,
        detailParams: {},
        exportAreaName: '',
        print: {
          id: 'reseed_table',
          popTitle: '打印', // 打印配置页上方标题
          extraCss: '', //最上方的头部文字，附加在head标签上的额外标签,使用逗号分隔
          extraHead: '<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">', //最上方的头部文字，附加在head标签上的额外标签,使用逗号分隔
          preview: '', // 是否启动预览模式，默认是false（开启预览模式，可以先预览后打印）
          previewTitle: '', // 打印预览的标题（开启预览模式后出现）,
          previewPrintBtnLabel: '', // 打印预览的标题的下方按钮文本，点击可进入打印（开启预览模式后出现）
          zIndex: '', // 预览的窗口的z-index，默认是 20002（此值要高一些，这涉及到预览模式是否显示在最上面）   
          previewBeforeOpenCallback() { }, //预览窗口打开之前的callback（开启预览模式调用）
          previewOpenCallback() { }, // 预览窗口打开之后的callback（开启预览模式调用）
          beforeOpenCallback() {}, // 开启打印前的回调事件
          openCallback() {}, // 调用打印之后的回调事件
          closeCallback() {}, //关闭打印的回调事件（无法确定点击的是确认还是取消）
          url: '',
          standard: '',
        },
        printShow: false
      }
    },
    methods: {
      setConditionFlag (falg) {
        this.$refs.tabPage.setHeight()
      },
      // 查询接口
      async search (pagenum) {
        let unitManageForm = this.$refs.selectClass.unitManageForm
        let flag = this.$refs['selectClass'].validateForm();
        if(!flag){
          return false;
        }
        let that = this
        if (!pagenum) {
          pagenum = 1
        }
        that.pagenum = pagenum
        let params = {
          areaCode: that.$refs.selectClass.$refs.areaTreePage.selectAreaCode,
          checkType:unitManageForm.checkType,//学校类型
          schId:unitManageForm.schoolId,//学校主键
          classYear:unitManageForm.stuclayear,//学年
          lunci:unitManageForm.lunci,//轮次
          pageIndex:pagenum,
          className:unitManageForm.classId,//班级名称
          operateType:this.$consts.OPTYPE_QUERY,
          grade:unitManageForm.stuClalevel
        }
        // that.exportAreaName = that.$refs.areaTreePage.selectAreaName + ' ' + that.$refs.areaTreePage.selectAreaCode
        that.searchData = params
        that.openFullScreen()
        queryReplantMess(that.searchData).then((res) => {
          this.dataList=res.data.list
          that.secTime = res.sec
          that.total = res.data.total
          that.closeFullScreen()
        }).catch(() => {
          that.closeFullScreen()
        })
      },
      handleExportClose () {
        this.exportDialogVisible = false
      },
      exportExcelFun () {
        if (!this.searchData) {
          this.$util.errorMsg('查询后才可以导出！')
          return
        }
        if (this.dataList && this.dataList.length === 0) {
          this.$util.errorMsg('暂无需要导出的数据！')
          return
        }
        this.exportDialogVisible = true
      },
      exportExcel (pageNum) {
        let params = JSON.parse(JSON.stringify(this.searchData))
        params.areaName = this.$refs.selectClass.$refs.areaTreePage.selectAreaName
        params.schName = this.$refs.selectClass.unitManageForm.schoolname
        params.operateType = this.$consts.OPTYPE_OUTPUT
        params.pageIndex = pageNum
        // console.log('导出参数', params)
        outPutReplantMess(params)
      },
      prints(){
        if (!this.searchData || this.searchData.areaCode!=sessionStorage.getItem('areacodeSelect')) {
          this.$util.errorMsg('查询后才可以打印！')
          return
        }
        if(this.dataList.length==0){
          this.$util.errorMsg('暂无需要打印的数据！')
          return
        }
        this.$refs.printBtn.click();
      }
    }
  }
  </script>
  <style media="print">
  @page {
    size: auto;
    margin: 3mm;
  }
  @media print {
    html {
      background-color: #ffffff;
      height: auto;
      margin: 0px;
    }
  
    body {
      margin: 10mm;
      max-width:188mm;
    }
  }
  </style>