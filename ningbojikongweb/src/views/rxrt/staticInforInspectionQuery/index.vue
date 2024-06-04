<template>
  <!-- 查验补种情况汇总表 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <div class="label">地区：</div>
          <ss-area-areaTreeStation :showSelect="false" ref="areaTreePage" :getToLevelNum="5 - currentLevel" />
        </div>
        <div class="item mLeft">
          <div class="label">年份：</div>
          <el-select v-model="claNameYear" placeholder="请选择年份" clearable>
            <el-option v-for="item in schoolYearList" :key="item" :label="item" :value="item" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">轮次：</div>
          <el-select v-model="claSeq" placeholder="请选择轮次" clearable>
            <el-option label="春季" value="1" />
            <el-option label="秋季" value="0" />
          </el-select>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <div class="label">查验日期：</div>
          <ss-date-range clearable v-model="checkDate" :startTimeOption="startTimeOption"
            :endTimeOption="endTimeOption" />
        </div>
        <div class="item mLeft">
          <div class="label">学校名称：</div>
          <el-input clearable style="width: 272px" v-model="schName" placeholder="请输入学校名称"></el-input>
        </div>
        <div class="item mLeft">
          <div class="label">查验类型：</div>
          <el-select v-model="checkType" placeholder="请选择查验类型" clearable>
            <el-option label="入园儿童" value="0"></el-option>
            <el-option label="入学儿童" value="1"></el-option>
          </el-select>
        </div>
      </div>
      <div class="condition-row mt_10" style="margin-top:5px;">
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
        <!-- <ss-explain-tip-text :tipText="tipText" :secTime="secTime" /> -->
      </div>
    </ss-condition-area>
    <!-- <customize-table :data="dataList" :columnData="columnData" ref="tabPage" class="vaccination"/> -->
    <ss-table style="width: 100%" :data="dataList" ref="tabPage" class="vaccination" @goPage="search" :pagenum="pageNum"
      :total="total">
      <el-table-column type="index" align="center" width="120" label="序号" />
      <el-table-column prop="cityName" align="center" min-width="120" label="市" showOverflowTooltip />
      <el-table-column prop="countyName" align="center" min-width="120" label="区" />
      <el-table-column prop="supName" align="center" min-width="150" label="街道" />
      <el-table-column prop="schName" align="center" min-width="150" label="学校名称" />
      <el-table-column prop="claName" align="center" min-width="150" label="班级" />
      <el-table-column prop="claSeq" align="center" min-width="100" label="轮次">
        <template slot-scope="{ row }">
          <span>{{ row.claSeq === '1' ? '春季' : '秋季' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="studentCount" align="center" min-width="130" label="查验人数" />
      <el-table-column prop="hasCard" align="center" min-width="150" label="有预防接种证人数" />
      <el-table-column prop="needCard" align="center" min-width="150" label="补预防接种证人数" />
      <el-table-column prop="hasReplant" align="center" min-width="150" label="完成全程接种人数" />
      <el-table-column prop="needReplant" align="center" min-width="130" label="需补种疫苗人数">
        <template slot-scope="{ row }"><span @click="openDetailDialogInfo(row.rowKey)"
            :style="{ 'cursor': row.needReplant != 0 ? 'pointer' : '', 'font-weight': row.needReplant != 0 ? 'bold' : '', 'color': row.needReplant != 0 ? '#2f6cc9' : '' }">{{
              row.needReplant }}</span></template>
      </el-table-column>
      <el-table-column prop="needReplantFull" align="center" min-width="150" label="完成全程补种人数" />
      <el-table-column prop="vaccineHasReplant" align="center" min-width="130" label="已补种总剂次数" />
      <el-table-column prop="vaccineNeedReplant" align="center" min-width="130" label="需补种总剂次数" />
      <el-table-column prop="replantPercent" align="center" min-width="130" label="补种率" />
      <el-table-column align="center" label="乙肝疫苗">
        <el-table-column align="center" label="需补种剂次数" prop="B063NeedReplant" min-width="110"></el-table-column>
        <el-table-column align="center" label="已补种剂次数" prop="B063HasReplant" min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="脊灰疫苗">
        <el-table-column align="center" label="需补种剂次数" prop="JiHuiNeedReplantSubtotal" min-width="110"></el-table-column>
        <el-table-column align="center" label="已补种剂次数" prop="JiHuiHasReplantSubtotal" min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="百白破疫苗">
        <el-table-column align="center" label="需补种剂次数" prop="BaiBaiPoNeedReplantSubtotal"
          min-width="110"></el-table-column>
        <el-table-column align="center" label="已补种剂次数" prop="BaiBaiPoHasReplantSubtotal"
          min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="白破疫苗">
        <el-table-column align="center" label="需补种剂次数" prop="B037NeedReplant" min-width="140"></el-table-column>
        <el-table-column align="center" label="已补种剂次数" prop="B037HasReplant" min-width="140"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="麻疹类疫苗">
        <el-table-column align="center" label="需补种剂次数" prop="MaFengNeedReplantSubtotal" min-width="110"></el-table-column>
        <el-table-column align="center" label="已补种剂次数" prop="MaFengHasReplantSubtotal" min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="A群流脑疫苗">
        <el-table-column align="center" label="需补种剂次数" prop="AQunLiuNaoNeedReplantSubtotal"
          min-width="110"></el-table-column>
        <el-table-column align="center" label="已补种剂次数" prop="AQunLiuNaoHasReplantSubtotal"
          min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="A+C群流脑疫苗">
        <el-table-column align="center" label="需补种剂次数" prop="ACQunLiuNaoNeedReplantSubtotal"
          min-width="110"></el-table-column>
        <el-table-column align="center" label="已补种剂次数" prop="ACQunLiuNaoHasReplantSubtotal"
          min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="乙脑疫苗">
        <el-table-column align="center" label="需补种剂次数" prop="YiNaoJianDuHuoNeedReplantSubtotal"
          min-width="110"></el-table-column>
        <el-table-column align="center" label="已补种剂次数" prop="YiNaoJianDuHuoHasReplantSubtotal"
          min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="甲肝疫苗">
        <el-table-column align="center" label="需补种剂次数" prop="B069NeedReplant" min-width="140"></el-table-column>
        <el-table-column align="center" label="已补种剂次数" prop="B069HasReplant" min-width="140"></el-table-column>
      </el-table-column>
    </ss-table>
    <!-- 详细信息弹框 -->
    <detail-dialog-info :visible="dialogVisible" :rowkey="rowkey" @close="close" />
  </div>
</template>
<script>
import { findAllClaYears } from '@/utils/axios/api'
import { searchInspectionQuery, exportInspection } from '@/utils/axios/apis/vaccinationInspection'
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
import { tableHeader } from './tableHeader'
import detailDialogInfo from './DialogInfo.vue'
export default {
  name: 'vaccinationInspection',
  mixins: [commonOptions],
  components: { detailDialogInfo },
  data() {
    return {
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      columnData: tableHeader,
      type: '6',
      dataList: [],
      schoolYearList: [],
      claNameYear: '',
      pageNum: 1,
      total: 0,
      secTime: 0,
      tipText: '统计截止到：' + this.$util.today() + '。',
      exportAreaName: '',
      cModId: this.$store.state.moduleId,
      dialogVisible: false,
      rowkey: '',
      claSeq: '',
      checkDate: [], // 查验日期
      schName: '', // 学校名称
      checkType: '0', // 查验类型
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
    }
  },
  methods: {
    openDetailDialogInfo(rowkey) {
      this.$nextTick(() => {
        this.rowkey = rowkey;
        this.dialogVisible = true;
      })
    },
    close() {
      this.dialogVisible = false;
      this.rowkey = '';
    },
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    // 查询接口
    search(pagenum) {
      if (!pagenum) {
        pagenum = 1
      }
      this.pageNum = pagenum
      let that = this
      if (!that.$refs.areaTreePage.selectAreaCode || that.$refs.areaTreePage.selectAreaCode.length === 0) {
        that.$util.errorMsg('必须选择地区！')
        return
      }
      let checkStartTime = this.checkDate[0] || ''
      let checkEndTime = this.checkDate[1] || ''
      if ((checkStartTime === '' && checkEndTime !== '') || (checkStartTime !== '' && checkEndTime === '')) {
        this.$util.errorMsg('查验日期需填写完整')
        return
      }

      let params = {
        // moduleName: '查验补种情况汇总表',
        areaCode: that.$refs.areaTreePage.selectAreaCode,
        areaLevel: that.$refs.areaTreePage.selectAreaLevel,
        claNameYear: that.claNameYear,
        // cModId: that.cModId,
        // operateType: that.$consts.OPTYPE_QUERY,
        // newBaseCode: that.$refs.areaTreePage.newBaseCode,
        startDate: checkStartTime,
        endData: checkEndTime,
        schName: this.schName,
        checkType: this.checkType, // 查验类型
        pageNum: pagenum,
        claSeq: this.claSeq
      }
      that.exportAreaName = that.$refs.areaTreePage.selectAreaName + ' ' + that.$refs.areaTreePage.selectAreaCode
      that.searchData = params
      that.openFullScreen()
      searchInspectionQuery(that.searchData).then((res) => {
        this.dataList = res.data.list
        this.total = res.data.total
        // this.dataList = res.data.map(i => {
        //   return {
        //     areaNameNew: i.areaName === '合计' ? i.areaName : i.areaName + ' ' + i.areaCode,
        //     ...i
        //   }
        // })
        that.secTime = res.sec
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
    // 导出
    exportExcelFun() {
      if (!this.searchData || this.searchData.areaCode!=sessionStorage.getItem('areacodeSelect')) {
        this.$util.errorMsg('查询后才可以导出！')
        return
      }
      if (this.dataList.length === 0) {
        this.$util.errorMsg('暂无需要导出的数据！')
        return
      }
      let params = JSON.parse(JSON.stringify(this.searchData))
      params.areaName = this.exportAreaName
      params.operateType = this.$consts.OPTYPE_OUTPUT
      console.log('导出', params)
      exportInspection(params)
    },
    // 查询学年
    findClaYear() {
      findAllClaYears().then((res) => {
        this.schoolYearList = res
      })
    }
  },
  mounted() {
    this.findClaYear()
  }
}
</script>
<style scoped>
.ml_10 {
  margin-left: 10px;
}

.mt_10 {
  margin-top: 10px;
}
</style>
<style>
.vaccination .el-table__header-wrapper tr:last-child th:nth-last-child(2),
.vaccination .el-table__header-wrapper tr:nth-last-child(2) th:nth-last-child(2),
.vaccination .el-table__header-wrapper tr+tr th:nth-last-child(2),
.vaccination .el-table__header-wrapper tr th:not(.is-leaf) {
  border-right: 1px solid #C0D2EE !important;
}

.vaccination .el-table__header-wrapper tr th:last-child,
.vaccination .el-table__header-wrapper tr th:nth-last-child(2) {
  border-right: none !important
}
</style>
