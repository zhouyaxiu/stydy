<template>
  <!-- 特定疫苗接种证查验汇总表 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <select-class ref="selectClass"></select-class>
      <div class="condition-row mt_10">
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query"/>
        <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
      </div>
    </ss-condition-area>
    <ss-table :data="dataList" ref="tabPage" class="infoTable" @goPage="search" :pagenum="pagenum" :total="total">
      <el-table-column prop="roundName" align="center" min-width="100" label="轮次" />
      <el-table-column prop="year" align="center" min-width="100" label="学年" />
      <el-table-column prop="schName" align="center" min-width="100" label="学校名称" />
      <el-table-column prop="claName" align="center" min-width="150" label="班级" show-overflow-tooltip />
      <template v-if="vaccinumName=='水痘疫苗'">
        <el-table-column prop="stuTotal" align="center" min-width="100" label="学生人数" :key="Math.random()"/>
        <el-table-column align="center" label="已种人数" :key="Math.random()">
          <el-table-column prop="varOneSz" align="center" min-width="100" label="水痘疫苗1" :key="Math.random()"></el-table-column>
          <el-table-column prop="varTwoSz" align="center" min-width="100" label="水痘疫苗2" :key="Math.random()"></el-table-column>
        </el-table-column>
      </template>
      <template v-if="vaccinumName=='流感疫苗'">
        <el-table-column prop="stuTotal" align="center" min-width="100" label="学生人数" :key="Math.random()"/>
        <el-table-column prop="fluSz" align="center" min-width="180" label="当年度接种流感疫苗人数" :key="Math.random()"/>
      </template>
      <template v-if="vaccinumName=='麻腮风疫苗'">
        <el-table-column prop="stuTotal" align="center" min-width="100" label="学生总数" :key="Math.random()"/>
        <el-table-column align="center" label="应种数" :key="Math.random()">
          <el-table-column prop="thisProvinceYz" align="center" min-width="100" label="本省户籍" :key="Math.random()"></el-table-column>
          <el-table-column prop="outProvinceYz" align="center" min-width="100" label="外省户籍" :key="Math.random()"></el-table-column>
        </el-table-column>
        <el-table-column align="center" label="实种数" :key="Math.random()">
          <el-table-column prop="thisProvinceSz" align="center" min-width="100" label="本省户籍" :key="Math.random()"></el-table-column>
          <el-table-column prop="outProvinceSz" align="center" min-width="100" label="外省户籍" :key="Math.random()"></el-table-column>
        </el-table-column>
        <el-table-column align="center" label="未接种原因统计" :key="Math.random()">
          <el-table-column align="center" min-width="80" label="禁忌" :key="Math.random()"></el-table-column>
          <el-table-column align="center" min-width="100" label="暂缓接种" :key="Math.random()"></el-table-column>
          <el-table-column align="center" min-width="120" label="既往免疫/患病" :key="Math.random()"></el-table-column>
          <el-table-column align="center" min-width="80" label="其他" :key="Math.random()"></el-table-column>
        </el-table-column>
      </template>
    </ss-table>
  </div>
</template>
<script>
import { queryList, output } from '@/utils/axios/apis/specInspectionSummary'
import commonOptions from '@/mixin/commonOptions'
import selectClass from './components/selectClass.vue'
import { getLevel } from '@/utils/common/area.js'
export default {
  name: 'vaccinationInspection',
  components: {
    selectClass
  },
  mixins: [commonOptions],
  data () {
    return {
      dataList: [],
      secTime: 0,
      tipText: '统计截止到：' + this.$util.today() + '。',
      pagesize: 20,
      total: 0,
      pagenum: 1,
      exportAreaName: '',
      msgPubPermission: false,
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      vaccinumName: ''
    }
  },
  methods: {
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    },
    // 查询接口
    async search (pagenum) {
      let that = this
      let unitManageForm = this.$refs.selectClass.unitManageForm
      let flag = this.$refs['selectClass'].validateForm()
      if (!flag) {
        return false
      }
      this.vaccinumName = unitManageForm.vaccName
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      let params = {
        areaCode: that.$refs.selectClass.$refs.areaTreePage.selectAreaCode,
        areaLevel: that.$refs.selectClass.$refs.areaTreePage.selectAreaLevel,
        schId: unitManageForm.schId,
        schName: unitManageForm.schName,
        claNameYear: unitManageForm.claYear,
        year: unitManageForm.nowYear,
        roundId: unitManageForm.roundId,
        roundName: unitManageForm.roundName,
        claLevel: unitManageForm.claLevel,
        claId: unitManageForm.claId,
        schTypeCn: unitManageForm.schTypeCn,
        claName: unitManageForm.claName,
        vaccineName: this.vaccinumName,
        pageNum: pagenum,
        operateType: this.$consts.OPTYPE_QUERY
      }
      that.searchData = params
      that.openFullScreen()
      queryList(that.searchData).then((res) => {
        this.dataList = res.data.list
        that.secTime = res.sec
        that.total = res.data.total
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
    exportExcelFun () {
      if (!this.searchData || this.searchData.areaCode !== sessionStorage.getItem('areacodeSelect')) {
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
    exportExcel () {
      let params = JSON.parse(JSON.stringify(this.searchData))
      params.areaName = this.$refs.selectClass.$refs.areaTreePage.selectAreaName
      params.operateType = this.$consts.OPTYPE_OUTPUT
      output(params)
    }
  },
  mounted () {
    this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) === 4
    // 等于4为门诊：true，其他是疾控：false
  }
}
</script>
