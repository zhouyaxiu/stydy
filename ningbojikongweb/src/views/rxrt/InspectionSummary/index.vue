<template>
  <!-- 接种证查验汇总表 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <ss-class-select
        ref="selectClass"
        :classMultiple="false"
        :ischeck="true"
        :otherField="{'school':true,'lunci':true}">
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
  import { queryVacCertCheckStat,outputVacCertCheckStat } from '@/utils/axios/apis/rxrt/InspectionSummary'
  import commonOptions from '@/mixin/commonOptions'
  // import exportDialog from '@/components/common/exportDialog'
  import { tableHeader } from './tableHeader'
  export default {
    name: 'vaccinationInspection',
    // components: {
    //   exportDialog
    // },
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
        exportDialogVisible: false,
        exportAreaName: '',
      }
    },
    methods: {
      rowStyle({row,column,rowIndex,columnIndex}){
        // if ((columnIndex==6 || columnIndex==7) && column.level==1) {
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
          areaCode: that.$refs.selectClass.$refs.areaTreePage.selectAreaCode,
          areaLevel:that.$refs.selectClass.$refs.areaTreePage.selectAreaLevel,
          rounds:unitManageForm.lunci,//轮次
          schType:unitManageForm.checkType,//学校类型
          year:unitManageForm.stuclayear,//学年
          operateType:this.$consts.OPTYPE_QUERY
        }
        that.searchData = params
        that.openFullScreen()
        queryVacCertCheckStat(that.searchData).then((res) => {
          this.dataList = res.data.list
          that.secTime = res.sec
          // that.total = res.data.total-1
          that.closeFullScreen()
        }).catch(() => {
          console.log(res,'10101')
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
        // 分页
        // this.exportDialogVisible = true
        this.exportExcel()
      },
      exportExcel (pageNum) {
        let params = JSON.parse(JSON.stringify(this.searchData))
        params.areaName = this.$refs.selectClass.$refs.areaTreePage.selectAreaName
        params.operateType=this.$consts.OPTYPE_OUTPUT
        console.log('导出参数', params)
        outputVacCertCheckStat(params)
      }
    }
  }
  </script>