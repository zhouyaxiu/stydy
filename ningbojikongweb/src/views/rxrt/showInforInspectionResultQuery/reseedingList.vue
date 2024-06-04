<!-- 需补种名单列表 -->
<template>
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <div class="label">
            <span style="color: red; margin-right: 6px;">*</span>选择地区：
          </div>
          <ss-area-areaTreeStation style="width:200px;" :showSelect="false" :disabled="!isJikong" ref="areaTreePage"
            :getToLevelNum="5 - currentLevel" />
        </div>
        <div class="item mLeft">
          <div class="label">学校类型：</div>
          <el-select v-model="schType" @change="changeSchType" clearable>
            <el-option v-for="item in selectTypeList" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">
            学校名称：
          </div>
          <el-select style="width:200px;" v-model="schId" placeholder="请选择学校名称" clearable @change="changeSchool"
            filterable>
            <el-option v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">
            <span style="color: red; margin-right: 6px;">*</span>学年：
          </div>
          <el-select style="width:200px;" v-model="year" placeholder="请选择年份" clearable>
            <el-option style="width:200px;" v-for="item in schoolYearList" :key="item" :label="item" :value="item" />
          </el-select>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <div class="label">
            年级：
          </div>
          <el-select style="width:200px;" clearable v-model="grade" placeholder="请选择年级" @change="changeGrade">
            <el-option v-for="item in gradeList" style="width:200px;" :key="item.code" :label="item.name"
              :value="item.code" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">
            <span style="color: red; margin-right: 6px;">*</span>轮次：
          </div>
          <el-select v-model="rounds" placeholder="请选择轮次" clearable @change="changeGrade">
            <el-option label="春季" value="1" />
            <el-option label="秋季" value="0" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">班级：</div>
          <el-select v-model="className" placeholder="请选择" clearable>
            <el-option v-for="item in classesList" :key="item" :label="item" :value="item"/>
          </el-select>
        </div>
      </div>
      <div class="condition-row mt_10">
        <!-- 查询 -->
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <!-- 导出 -->
        <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
        <!-- 打印 -->
        <!-- <ss-custom-button v-print="printObj" :type="$consts.CUSTOMBUTTONTYPES.print" class="ml_10" /> -->
        <button ref="printBtn" style="display:none" v-print="printObj" class="ml_10">打印</button>
        <ss-custom-button @click="prints" :type="$consts.CUSTOMBUTTONTYPES.print" class="ml_10" />
        <!-- <ss-explain-tip-text :tipText="tipText" :secTime="secTime" /> -->
      </div>
    </ss-condition-area>
    <ss-table :data="dataList" ref="tabPage" class="reseedTable" @goPage="search" :pagenum="pagenum" :total="total">
      <el-table-column type="index" align="center" width="80" label="序号" />
      <el-table-column prop="schName" align="center" min-width="120" label="学校" />
      <el-table-column prop="gradeName" align="center" min-width="120" label="年级" />
      <el-table-column prop="clazzName" align="center" min-width="120" label="班级">
      </el-table-column>
      <el-table-column prop="stuName" align="center" min-width="120" label="儿童姓名" />
      <el-table-column prop="stuBirth" align="center" min-width="120" label="出生日期" />
      <el-table-column prop="needReplantRoster" align="center" min-width="180" label="补种疫苗名称" showOverflowTooltip />
      <el-table-column prop="stuPhone" align="center" min-width="120" label="手机号码" >
        <template slot-scope="{ row }">
          <span>{{ row.stuPhone ? $util.encryptPhone(row.stuPhone,false) : '' }}</span>
        </template>
      </el-table-column>
    </ss-table>
    <div v-show="printShow">
      <!-- 打印表格 -->
      <vaccination-table :dataList="dataList" id="reseed_table">
      </vaccination-table>
    </div>
    <export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'需补种名单列表'"
      :dialogVisible="exportDialogVisible"></export-dialog>
  </div>
</template>
<script>
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
import { findSchoolByAreaAndSchType } from '@/utils/axios/api'
import { queryReplantRoster, outputReplantRoster } from '@/utils/axios/apis/rxrt/checkVaccination'
import vaccinationTable from './components/vaccinationTable.vue'
import exportDialog from '@/components/common/exportDialog'
export default {
  mixins: [commonOptions],
  components: { vaccinationTable, exportDialog },
  data () {
    return {
      number: '',
      name: '',
      schType: '',
      year: this.$dateutil.currentYear(),
      schId: '',
      grade: '',
      rounds: '',
      classes: '',
      tipText: '统计截止到：' + this.$util.dayAdd(this.$util.today(), 0) + '。',
      secTime: 0,
      pagenum: 1,
      total: 0,
      dataList: [],
      importSuccessList: [],
      schoolList: [],
      selectTypeList: [
        {
          name: '幼托机构',
          code: '0'
        },
        {
          name: '小学',
          code: '1'
        }
      ],
      gradeList: this.$util.getGrade(2),
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      searchData: {},
      isJikong: false,
      schoolName: '',
      printObj: {
        id: 'reseed_table',
        popTitle: '需补种名单列表',
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>',
        beforeOpenCallback (vue) {
          console.log('打开之前')
        },
        openCallback (vue) {
          console.log('执行了打印')
        },
        closeCallback (vue) {
          console.log('关闭了打印工具')
        }
      },
      printShow: false,
      exportDialogVisible: false,
      className: '' // 班级
    }
  },
  methods: {
    // 打印
    // print() {
    // let that = this
    // if (!that.searchData) {
    //   that.$util.errorMsg('查询后才可以打印！')
    //   return
    // }
    // if (that.dataList.length === 0) {
    //   that.$util.errorMsg('暂无需要打印的数据！')
    //   return
    // }
    // let params = {
    //   stuRowId: this.multipleSelection.join(','),
    //   schName: this.$store.state.schName
    // }
    // printOut(params)
    // },
    changeSchool (val) {
      if (val) {
        console.log(this.schoolList.filter(item => item.schId == val))
        // this.schstationCode = this.schoolList.filter(item => item.schId == val)[0].schStationCode
        this.schoolName = this.schoolList.filter(item => item.schId == val)[0].schName
        // this.schStationName = this.schoolList.filter(item => item.schId == val)[0].schStationName
        let schType = this.schoolList.filter(item => item.schId == val)[0].schType
        this.gradeList = this.$util.getGrade(schType)
        this.className = ''
        this.classesList = []
        this.grade = ''
      } else {
        this.grade = ''
        this.gradeList = this.$util.getGrade(2)
        this.className = ''
        this.classesList = []
      }
    },
    async changeGrade (val) {
      if (val && this.schId != '') {
        let params = {
          schId: this.schId || '-1',
          classLevel: this.grade,
          classYear: this.year,
          lunci: this.rounds
        }
        this.className = ''
        this.classesList = []
        await this.findClassNew(params)
        if (this.classesList && this.classesList.length == 0) {
          this.className = ''
        }
      } else {
        this.className = ''
        this.classesList = []
      }
    },
    prints () {
      if (!this.searchData || this.searchData.areaCode != sessionStorage.getItem('areacodeSelect')) {
        this.$util.errorMsg('查询后才可以打印！')
        return
      }
      if (this.dataList.length == 0) {
        this.$util.errorMsg('暂无需要打印的数据！')
        return
      }
      this.$refs.printBtn.click()
    },
    // 查询
    search (pagenum) {
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      if (!that.$refs.areaTreePage.selectAreaCode || that.$refs.areaTreePage.selectAreaCode.length === 0) {
        that.$util.errorMsg('必须选择地区！')
        return
      }
      if (!that.year) {
        that.$util.errorMsg('必须选择学年！')
        return
      }
      // if (!that.grade) {
      //   that.$util.errorMsg('必须选择年级！')
      //   return
      // }
      if (!that.rounds) {
        that.$util.errorMsg('必须选择轮次！')
        return
      }
      let params = {
        // areaId: that.$refs.areaTreePage.selectAreaId,
        areaCode: that.$refs.areaTreePage.selectAreaCode,
        areaLevel: that.$refs.areaTreePage.selectAreaLevel,
        // selectType: that.$refs.areaTreePage.selectType,
        schId: that.schId,
        rounds: that.rounds,
        year: that.year,
        grade: that.grade,
        schType: that.schType,
        operateType: that.$consts.OPTYPE_QUERY,
        pageNum: pagenum,
        pageSize: 20,
        className: that.className
      }

      that.searchData = JSON.parse(JSON.stringify(params))
      that.openFullScreen()
      queryReplantRoster(params).then((res) => {
        that.dataList = res.data.list
        that.dataList = that.dataList.map(item => {
          return {
            ...item,
            gradeName: this.gradeList.filter(sub => sub.code == item.grade)[0].name
          }
        })
        that.secTime = res.sec
        that.total = res.data.total
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
    nodeClick (data, checked, node) {
      console.log(data)
      console.log(checked)
      console.log(node)
      this.findSchool()
    },
    changeSchType (val) {
      if (val) {
        this.schType = val
        this.gradeList = this.$util.getGrade(val)
        this.schId = ''
        this.grade = ''
        this.findSchool()
      } else {
        this.gradeList = this.$util.getGrade(2)
        this.schId = ''
        this.grade = ''
        this.findSchool()
      }
      this.className = ''
      this.classesList = []
    },
    // 查询学校
    findSchool () {
      findSchoolByAreaAndSchType({
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel,
        schType: this.schType
      }).then((res) => {
        this.schoolList = res.data
      })
    },
    handleExportClose () {
      this.exportDialogVisible = false
    },
    // 导出
    exportExcelFun () {
      let that = this
      if (!that.searchData) {
        that.$util.errorMsg('查询后才可以导出！')
        return
      }
      if (that.dataList.length === 0) {
        that.$util.errorMsg('暂无需要导出的数据！')
        return
      }
      this.exportDialogVisible = true
      // let params = JSON.parse(JSON.stringify(that.searchData))
      // params.areaName = this.$refs.areaTreePage.selectAreaName + params.areaCode
      // params.schName = this.schoolName
      // params.operateType = that.$consts.OPTYPE_OUTPUT
      // outputReplantRoster(params)
    },
    exportExcel (pageNum) {
      let params = JSON.parse(JSON.stringify(this.searchData))
      params.areaName = this.$refs.areaTreePage.selectAreaName + params.areaCode
      params.schName = this.schoolName
      params.pageNum = pageNum
      params.operateType = this.$consts.OPTYPE_OUTPUT
      console.log('导出参数', params)
      outputReplantRoster(params)
    },
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    }
  },
  mounted () {
    this.findClaYear()
    this.isJikong = parseInt(sessionStorage.getItem('areaLevel')) !== 4
    if (this.$refs.areaTreePage.selectAreaCode) {
      this.findSchool()
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
    /* border: solid 1px red; */
    margin: 10mm;
    max-width: 188mm;
  }

  #reseed_table table {
    table-layout: auto !important;
    width: 100% !important;
  }
}
</style>
<style>
.reseedTable .el-table__header-wrapper tr:last-child th:nth-last-child(2),
.reseedTable .el-table__header-wrapper tr:nth-last-child(2) th:nth-last-child(2),
.reseedTable .el-table__header-wrapper tr+tr th:nth-last-child(2),
.reseedTable .el-table__header-wrapper tr th:not(.is-leaf) {
  border-right: none !important;
  text-align: center !important;
}

.reseedTable .el-table__header-wrapper tr th:last-child,
.reseedTable .el-table__header-wrapper tr th:nth-last-child(2) {
  border-right: none !important
}

.reseedTable .el-table__fixed-right {
  right: 0 !important;
}
</style>
