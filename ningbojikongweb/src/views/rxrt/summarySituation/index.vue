<template>
  <!-- 浙江省入托、入学预防接种证查验情况汇总表 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <ss-class-select
        ref="selectClass"
        :classMultiple="false"
        :otherField="{}"
        :ischeck="true">
      </ss-class-select>
      <div class="mt_10" style="display: flex;">
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
        <!-- <ss-custom-button v-print="print" :type="$consts.CUSTOMBUTTONTYPES.print" class="ml_10" /> -->
      </div>
    </ss-condition-area>
    <customize-table id="printArea" :data="dataList" :columnData="columnData" ref="tabPage" class="vaccination" :pageSize="20" :changeNull="true" @goPage="search" :pagenum="pagenum" :total="total" :headerCellStyle="rowStyle"></customize-table>
  </div>
</template>
<script>
import { queryInforInspect, outPutInforInspect } from '@/utils/axios/apis/rxrt/summarySituation'
import commonOptions from '@/mixin/commonOptions'
import { tableHeader } from './tableHeader'
export default {
  name: 'vaccinationInspection',
  mixins: [commonOptions],
  data () {
    return {
      columnData: tableHeader,
      dataList: [],
      secTime: 0,
      tipText: '统计截止到：' + this.$util.today() + '。',
      pagesize: 20,
      total: 0,
      pagenum: 1,
      unitManageForm:{
        stuclayear:this.$dateutil.currentYear()
      },
      print: {
        id: 'printArea',
        popTitle: '打印', // 打印配置页上方标题
        extraHead: '', //最上方的头部文字，附加在head标签上的额外标签,使用逗号分隔
        preview: '', // 是否启动预览模式，默认是false（开启预览模式，可以先预览后打印）
        previewTitle: '', // 打印预览的标题（开启预览模式后出现）,
        previewPrintBtnLabel: '', // 打印预览的标题的下方按钮文本，点击可进入打印（开启预览模式后出现）
        zIndex: '', // 预览的窗口的z-index，默认是 20002（此值要高一些，这涉及到预览模式是否显示在最上面）   
        previewBeforeOpenCallback() { }, //预览窗口打开之前的callback（开启预览模式调用）
        previewOpenCallback() { }, // 预览窗口打开之后的callback（开启预览模式调用）
        beforeOpenCallback() {
          
        }, // 开启打印前的回调事件
        openCallback() {
          this.isPrint=true;
        }, // 调用打印之后的回调事件
        closeCallback() {}, //关闭打印的回调事件（无法确定点击的是确认还是取消）
        url: '',
        standard: '',
        extraCss: '',
      },
    }
  },
  methods: {
    rowStyle({row,column,rowIndex,columnIndex}){
      // if ((columnIndex==6 || columnIndex==7) && column.colSpan==1 && !column.label) {
      //   return 'color:red!important;'
      // }
    },
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
        areaLever:that.$refs.selectClass.$refs.areaTreePage.selectAreaLevel,
        areaCode: that.$refs.selectClass.$refs.areaTreePage.selectAreaCode,
        classYear:unitManageForm.stuclayear,
        checkType:unitManageForm.checkType,
        lunci:unitManageForm.lunci,
        operateType:this.$consts.OPTYPE_QUERY
      }
      that.searchData = params
      that.openFullScreen()
      queryInforInspect(that.searchData).then((res) => {
        res.data[res.data.length-1].areaName='合计';
        this.dataList = res.data
        that.secTime = res.sec
        that.total = res.data.total
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
    handleSelectionChange (val) {
      this.multipleSelection = val.map(item => item.rowkey)
    },
    handleExportClose () {
      this.exportDialogVisible = false
    },
    exportExcelFun () {
      if (!this.searchData || this.searchData.areaCode!=sessionStorage.getItem('areacodeSelect')) {
        this.$util.errorMsg('查询后才可以导出！')
        return
      }
      if (this.dataList.length === 0) {
        this.$util.errorMsg('暂无需要导出的数据！')
        return
      }
      // this.exportDialogVisible = true
      let params = JSON.parse(JSON.stringify(this.searchData))
      params.areaName = this.$refs.selectClass.$refs.areaTreePage.selectAreaName
      params.operateType = this.$consts.OPTYPE_OUTPUT
      console.log('导出参数', params)
      outPutInforInspect(params)
    },
    exportExcel (pageNum) {
      let params = JSON.parse(JSON.stringify(this.searchData))
      params.areaName = this.exportAreaName
      let schoolName = this.schoolId ? this.schoolList.filter(item => item.code === this.schoolId)[0].name : ''
      params.schoolName = schoolName
      let regTypes = this.$refs.checkResiTypePage.resiTypeList.filter(item => Object.values(item).some(v => this.$refs.checkResiTypePage.resiType.includes(v))).map(item => item.domicilename)
      if (regTypes.includes('全部')) {
        params.regTypeOriginal = '全部'
      } else if (regTypes.includes('全部流动')) {
        let forDelete = ['本市外县', '本省外市', '外省']
        regTypes = regTypes.filter(item => !forDelete.includes(item))
        params.regTypeOriginal = regTypes.join(',')
      } else {
        params.regTypeOriginal = this.selectResiTypeFlag ? regTypes.join(',') : ''
      }
      params.regTypes = this.selectResiTypeFlag ? this.resiTypeChange.join(',') : ''
      params.operateType = this.$consts.OPTYPE_OUTPUT
      params.pageNum = pageNum
      params.lunci = this.lunci
      console.log('导出参数', params)
      outPutInforInspect(params)
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