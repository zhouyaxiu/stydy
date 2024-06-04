<template>
  <!-- 查验信息查询 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <div class="label">地区：</div>
          <ss-area-areaTreeStation :showSelect="false" ref="areaTreePage" :getToLevelNum="5 - currentLevel" @nodeClick="nodeClick"/>
        </div>
        <div class="item mLeft">
          <div class="label">学校：</div>
          <el-select style="width:200px;" slot="fEnterno" v-model="schoolId" placeholder="请选择学校" clearable>
            <el-option style="width:200px;" v-for="item in schoolList" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">年份：</div>
          <el-select style="width:200px;" v-model="classYear" placeholder="请选择年份" clearable>
            <el-option style="width:200px;" v-for="item in classYearList" :key="item.claNameYear" :label="item.claNameYear" :value="item.claNameYear" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">轮次：</div>
          <el-select v-model="lunci" placeholder="请选择轮次" clearable>
            <el-option label="春季" value="1" />
            <el-option label="秋季" value="0" />
          </el-select>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <div class="label">受种者姓名：</div>
          <el-input placeholder="请输入受种者姓名" clearable style="width:214px;" v-model="stuName"></el-input>
        </div>
        <div class="item mLeft">
          <div class="label">户籍类型：</div>
          <ss-check-resiType ref="checkResiTypePage" />
        </div>
      </div>
      <div class="condition-row mt_10" style="margin-top:5px;">
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
        <ss-explain-tip-text :tipText="tipText" :secTime="secTime" />
      </div>
    </ss-condition-area>
    <ss-table style="width: 100%" :data="dataList" :columnData="columnData" ref="tabPage" class="vaccination" :pageSize="20" :changeNull="true" @goPage="search" @rowOperateFun="rowOperateFun" :pagenum="pagenum" :total="total">
      <el-table-column type="index" align="center" width="120" label="序号" />
      <el-table-column prop="self_stu_sourceid" align="center" min-width="140" label="数据来源" />
      <el-table-column prop="stu_classname" align="center" min-width="220" label="受种者班级" />
      <el-table-column prop="stu_no" align="center" min-width="180" label="预防接种档案编号" showOverflowTooltip />
      <el-table-column prop="stu_imuno" align="center" min-width="180" label="受种者信息介质识别码">
        <template slot-scope="{ row }">
          <span>{{ row.stu_imuno == '0000' ? '' : row.stu_imuno }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="stu_type" align="center" min-width="180" label="户籍类型" showOverflowTooltip />
      <el-table-column prop="stu_name" align="center" min-width="150" label="受种者姓名" />
      <el-table-column prop="stu_sex" align="center" min-width="80" label="性别" />
      <el-table-column prop="stu_birth" align="center" min-width="120" label="出生日期" />
      <el-table-column prop="stu_seq" align="center" min-width="120" label="轮次">
        <template slot-scope="{ row }">
          <span>{{ row.stu_seq === '1' ? '春季' : '秋季' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="stu_isjzz" align="center" min-width="100" label="有无接种证">
        <template slot-scope="{ row }">
          <span>{{ row.stu_isjzz === '0' ? '有' : '无' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="stu_parentname" align="center" min-width="180" label="父亲/母亲/监护人姓名" />
      <el-table-column prop="stu_stationname" align="center" min-width="180" label="所属预防接种单位" showOverflowTooltip />
      <el-table-column align="center" label="乙肝疫苗">
        <el-table-column align="center" label="1" prop="b063InspectionShow" min-width="140"></el-table-column>
        <el-table-column align="center" label="2" prop="b064InspectionShow" min-width="140"></el-table-column>
        <el-table-column align="center" label="3" prop="b065InspectionShow" min-width="140"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="脊灰疫苗">
        <el-table-column align="center" label="1" prop="b009InspectionShow" min-width="140"></el-table-column>
        <el-table-column align="center" label="2" prop="b010InspectionShow" min-width="140"></el-table-column>
        <el-table-column align="center" label="3" prop="b011InspectionShow" min-width="140"></el-table-column>
        <el-table-column align="center" label="4" prop="b012InspectionShow" min-width="140"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="百白破疫苗">
        <el-table-column align="center" label="1" prop="b015InspectionShow" min-width="140"></el-table-column>
        <el-table-column align="center" label="2" prop="b016InspectionShow" min-width="140"></el-table-column>
        <el-table-column align="center" label="3" prop="b017InspectionShow" min-width="140"></el-table-column>
        <el-table-column align="center" label="4" prop="b018InspectionShow" min-width="140"></el-table-column>
      </el-table-column>
      <el-table-column prop="b037InspectionShow" align="center" label="白破疫苗" min-width="140" />
      <el-table-column align="center" label="麻疹类疫苗">
        <el-table-column align="center" label="1" prop="b059InspectionShow" min-width="140"></el-table-column>
        <el-table-column align="center" label="2" prop="b060InspectionShow" min-width="140"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="流脑疫苗">
        <el-table-column align="center" label="A群流脑疫苗">
          <el-table-column align="center" label="1" prop="b040InspectionShow" min-width="140"></el-table-column>
          <el-table-column align="center" label="2" prop="b041InspectionShow" min-width="140"></el-table-column>
        </el-table-column>
        <el-table-column align="center" label="A+C群流脑疫苗">
          <el-table-column align="center" label="1" prop="b045InspectionShow" min-width="140"></el-table-column>
          <el-table-column align="center" label="2" prop="b046InspectionShow" min-width="140"></el-table-column>
        </el-table-column>
      </el-table-column>
      <el-table-column align="center" label="乙脑疫苗">
        <el-table-column align="center" label="1" prop="b032InspectionShow" min-width="140"></el-table-column>
        <el-table-column align="center" label="2" prop="b033InspectionShow" min-width="140"></el-table-column>
      </el-table-column>
      <el-table-column prop="b069InspectionShow" align="center" label="甲肝疫苗" min-width="140" />
      <el-table-column prop="stu_isjz" align="center" label="是否种全">
        <template slot-scope="{ row }">
          <span>{{ row.stu_isjz === '0' ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="stuIsjzFirst" align="center" min-width="120" label="补种是否完成">
        <template slot-scope="{ row }">
          <span>{{ row.stuIsjzFirst == '0' && row.stuIsjz === '0' ? '-' : row.stu_isjz==='0'?'是':'否' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" align="center" min-width="180" label="备注" />
      <el-table-column align="center" label="操作" fixed="right" width="120" v-if="dataList.length!=0 && $dateutil.currentYear()==classYear && lunci==defautltlunci">
        <template slot-scope="{ row }">
          <span @click="refind(row)" style="cursor:pointer" class="el-button table-btn el-button--text">重新查验</span>
        </template>
      </el-table-column>
    </ss-table>
    <!-- <customize-table :data="dataList" :columnData="columnData" ref="tabPage" class="vaccination" :pageSize="20" :changeNull="true" @goPage="search" @rowOperateFun="rowOperateFun" :pagenum="pagenum" :total="total"></customize-table> -->
    <export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'查验信息查询报表'" :dialogVisible="exportDialogVisible"></export-dialog>
  </div>
</template>
<script>
import { rxrtQuery, checkStudent, queryAllSchoolByAreaCode, rxrtOutPut } from '@/utils/axios/apis/rxrt/showInforInspectionResultQuery'
import { findClaYearBySchId } from '@/utils/axios/api'
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
import exportDialog from '@/components/common/exportDialog'
import { tableHeader } from './tableHeader'
export default {
  name: 'vaccinationInspection',
  components: {
    exportDialog
  },
  mixins: [commonOptions],
  data () {
    return {
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      columnData: tableHeader,
      type: '6',
      dataList: [],
      claNameYear: '',
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
      cModId: this.$store.state.moduleId,
      selectResiTypeFlag: true,
      classYear: '', // 学年
      schoolId: '',
      schoolList: [],
      classYearList: [],
      stuName: '', // 受种者名称
      lunci:'',
      defautltlunci:sessionStorage.getItem("defaultLunci")
    }
  },
  watch: {
    schoolId (newVal) {
      this.classYear = ''
      this.findClaYear()
    }
  },
  methods: {
    rowOperateFun (row, btnType) {
      this.refind(row)
    },
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    },
    // 重新查验
    async refind (row) {
      let that = this

      if (row.stu_isjz === '0') {
        that.$util.errorMsg('已种全，无须重新查验')
        return false
      }
      let resiType = that.$refs.checkResiTypePage.resiType
      if ((!resiType || resiType.length === 0) && that.selectResiTypeFlag) {
        that.$util.errorMsg('户籍类型至少选中一个！')
        return false
      } else {
        await that.changeRegType(resiType.join(','))
      }
      let data = {
        rowkey: row.rowkey
      }
      let index = this.dataList.findIndex(item => item.rowkey === row.rowkey)
      this.$confirm('确定重新查验吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        that.openFullScreen()
        checkStudent(data).then((res) => {
          this.secTime = res.sec
          this.$message({
            type: res.msg === '查验失败' ? 'error' : 'success',
            message: res.msg
          })
          if (res.msg === '查验成功') {
            if (this.dataList[index].rowkey === res.data.rowkey) {
              res.data.self_stu_sourceid = res.data.stu_sourceid === '0' ? '信息系统提供' : res.data.stu_sourceid === '1' ? 'excel导入' : '校医录入'
              this.$set(this.dataList, index, res.data)
            }
          }
          this.closeFullScreen()
        })
      }).catch(() => {
        this.closeFullScreen()
        this.$message({
          type: 'info',
          message: '已取消'
        })
      })
    },
    // 查询接口
    async search (pagenum) {
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      if (!that.$refs.areaTreePage.selectAreaCode || that.$refs.areaTreePage.selectAreaCode.length === 0) {
        that.$util.errorMsg('必须选择地区！')
        return
      }
      if(!that.schoolId){
        that.$util.errorMsg('必须选择学校！')
        return
      }
      if(!that.classYear){
        that.$util.errorMsg('必须选择年份！')
        return
      }
      if(!that.lunci){
        that.$util.errorMsg('必须选择轮次！')
        return
      }
      let resiType = that.$refs.checkResiTypePage.resiType
      if ((!resiType || resiType.length === 0) && that.selectResiTypeFlag) {
        that.$util.errorMsg('户籍类型至少选中一个！')
        return false
      } else {
        await that.changeRegType(resiType.join(','))
      }
      let params = {
        // moduleName: '查验接种信息',
        areaCode: that.$refs.areaTreePage.selectAreaCode,
        areaLevel: that.$refs.areaTreePage.selectAreaLevel,
        schoolId: that.schoolId,
        claNameYear: that.classYear, // 学年
        stuName: that.stuName, // 受种者名称
        regTypes: that.selectResiTypeFlag ? that.resiTypeChange.join(',') : '',
        pageNum: pagenum,
        lunci:that.lunci
      }
      that.exportAreaName = that.$refs.areaTreePage.selectAreaName + ' ' + that.$refs.areaTreePage.selectAreaCode
      that.searchData = params
      that.openFullScreen()
      rxrtQuery(that.searchData).then((res) => {
        this.dataList = res.data.data.map(i => {
          return {
            self_stu_isjzz: i.stu_isjzz === '0' ? '有' : '无',
            self_stu_isjz: i.stu_isjz === '0' ? '是' : '否',
            self_stu_isfull: i.stu_isfull === '0' ? '完成' : '未完成',
            self_stu_sourceid: i.stu_sourceid === '0' ? '信息系统提供' : i.stu_sourceid === '1' ? 'excel导入' : '校医录入',
            ...i
          }
        })
        that.secTime = res.sec
        that.total = res.data.total
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
    nodeClick () {
      this.findSchool()
    },
    // 查询学校
    findSchool () {
      queryAllSchoolByAreaCode({
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel
      }).then((res) => {
        this.schoolList = res
        this.schoolId = ''
        this.classYearList = []
      })
    },
    // 查询学年
    findClaYear () {
      findClaYearBySchId({ schId: this.schoolId }).then((res) => {
        this.classYearList = res
      })
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
      this.exportDialogVisible = true
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
      rxrtOutPut(params)
    }
  },
  mounted () {
    this.findSchool()
  }
}
</script>
<style scoped>
.pointer {
    cursor: pointer;
    color: #1e6ce4;
}
.ml_10 {
  margin-left: 10px;
}
.mt_10 {
  margin-top: 10px;
}
</style>