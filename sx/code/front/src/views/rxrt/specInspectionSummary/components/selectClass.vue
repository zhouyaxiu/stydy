<template>
  <!-- 年份、轮次、年级、班级联动选择组件 -->
  <div>
    <div class="condition-row">
      <div class="item">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>选择地区：</div>
        <ss-area-areaTreeStation :showSelect="false" ref="areaTreePage" class="area" :disabled="msgPubPermission"
          :getToLevelNum="5 - currentLevel" @nodeClick="nodeClick" />
      </div>
      <div class="item mLeft">
        <div class="label">学校类型：</div>
        <el-select v-model="unitManageForm.checkType" placeholder="请选择学校类型" clearable @change="schTypeChange">
          <el-option label="幼托机构" value="0" />
          <el-option label="小学" value="1" />
          <el-option label="初中" value="2" />
          <el-option label="高中" value="3" />
          <el-option label="大学" value="4" />
        </el-select>
      </div>
      <div class="item mLeft">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>学校名称：</div>
        <el-select style="width:200px;" v-model="unitManageForm.schId" placeholder="请选择学校名称" clearable @change="schIdChange" @clear="schIdChange">
          <el-option style="width:200px;" v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
        </el-select>
      </div>
      <div class="item mLeft">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>学年：</div>
        <el-select v-model="unitManageForm.claYear" placeholder="请选择学年" clearable @change="vaccinumChange">
          <el-option v-for="item in schoolYearList" :key="item.claNameYear" :label="item.claNameYear" :value="item.claNameYear" />
        </el-select>
      </div>
    </div>
    <div class="condition-row">
      <div class="item">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>年份：</div>
        <el-select v-model="unitManageForm.nowYear" placeholder="请选择年份" clearable @change="vaccinumChange">
          <el-option v-for="item in nowYearList" :key="item" :label="item" :value="item" />
        </el-select>
      </div>
      <div class="item mLeft">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>疫苗：</div>
        <el-select v-model="unitManageForm.vaccName" placeholder="请选择疫苗" clearable @change="vaccinumChange">
          <el-option v-for="item in vaccinumList" :key="item.name" :label="item.name" :value="item.name" />
        </el-select>
      </div>
      <div class="item mLeft">
        <div class="label"><span style="color: red; margin-right: 6px;">*</span>轮次：</div>
        <el-select v-model="unitManageForm.roundId" placeholder="请选择轮次" clearable @change="roundChange">
          <el-option v-for="item in claseqList" :key="item.roundId" :label="item.roundName" :value="item.roundId"></el-option>
        </el-select>
      </div>
      <div class="item mLeft">
        <div class="label">年级：</div>
        <el-select clearable v-model="unitManageForm.claLevel" placeholder="请选择年级" @change="changeGrade">
          <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </div>
    </div>
    <div class="condition-row">
      <div class="item">
        <div class="label">班级：</div>
        <el-select clearable v-model="unitManageForm.claId" placeholder="请选择班级" @change="changeClass">
          <el-option v-for="item in classesList" :key="item.rowkey" :label="item.claShortName" :value="item.rowkey" />
        </el-select>
      </div>
      <slot></slot>
    </div>
  </div>
</template>

<script>
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
import { findRoundYear, findRounds } from '@/utils/axios/api'
export default {
  name: 'selectClass',
  mixins: [commonOptions],
  data () {
    return {
      resetForm: {},
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      selectTypeList: this.$store.state.selectTypeList, // 学校类型列表
      vaccinumList: this.$store.state.vaccinumList, // 疫苗列表
      YearList: [], // 年份列表
      claseqList: [], // 轮次列表
      classesList: [], // 班级列表
      unitManageForm: {
        schType: '', // 学校类型
        schId: '', // 学校名称
        claYear: '', // 学年
        nowYear: '', // 年份
        vaccName: '', // 疫苗
        roundId: '', // 轮次
        claLevel: '', // 年级
        claId: '' // 班级
      },
      msgPubPermission: false,
      schTypeStatus: {
        '0': '幼托机构',
        '1': '小学',
        '2': '初中',
        '3': '高中',
        '4': '大学'
      }
    }
  },
  methods: {
    nodeClick () {
      this.unitManageForm.schId = ''
      this.schoolList = []
      this.findSchoolFun()
    },
    // 查询学校
    async findSchoolFun () {
      let params = {
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel,
        schType: this.unitManageForm.checkType
      }
      await this.findSchoolNew(params)
    },
    // 查询班级
    async changeGrade () {
      this.unitManageForm.claId = ''
      await this.queryHighClassMess(this.unitManageForm.schId, this.unitManageForm.claLevel, this.unitManageForm.claYear.toString())
    },
    schTypeChange (val) {
      this.unitManageForm.schId = ''
      if (val) {
        this.findClaLevelSecond(val)
      }
      this.findSchoolFun()
      this.unitManageForm.schTypeCn = this.schTypeStatus[val]
    },
    validateForm (callback) {
      if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        this.$util.errorMsg('必须选择地区！')
        return false
      }
      if (!this.unitManageForm.schId) {
        this.$util.errorMsg('请选择学校名称!')
        return false
      }
      if (!this.unitManageForm.claYear) {
        this.$util.errorMsg('请选择学年!')
        return false
      }
      if (!this.unitManageForm.nowYear) {
        this.$util.errorMsg('请选择年份!')
        return false
      }
      if (!this.unitManageForm.vaccName) {
        this.$util.errorMsg('请选择疫苗!')
        return false
      }
      if (!this.unitManageForm.roundId) {
        this.$util.errorMsg('请选择轮次!')
        return false
      }
      return true
    },
    vaccinumChange (val) {
      this.unitManageForm.roundId = ''
      if (val) {
        this.getRound()
      }
    },
    getRound () {
      if (this.unitManageForm.nowYear === '' || this.unitManageForm.vaccName === '') return
      let params = {
        nowYear: this.unitManageForm.nowYear,
        vaccName: this.unitManageForm.vaccName,
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel
      }
      findRounds(params).then((res) => {
        this.claseqList = res.data.list
      }).catch(() => {

      })
    },
    getRoundYear () {
      let params = {
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel
      }
      findRoundYear(params).then((res) => {
        this.nowYearList = res.data
      }).catch(() => {
      })
    },
    schIdChange (val) {
      if (val) {
        let params = {
          schId: this.unitManageForm.schId
        }
        this.unitManageForm.schName = this.schoolList.filter(item => item.schId === this.unitManageForm.schId)[0].schName
        this.getClassesYear(params)
      }
      if (!this.unitManageForm.schType || !val) {
        this.unitManageForm.claYear = ''
        this.unitManageForm.claLevel = ''
        this.unitManageForm.claId = ''
        this.findClaLevelSecond(this.schoolList.filter(item => item.schId === this.unitManageForm.schId)[0].schType)
      }
    },
    roundChange () {
      this.unitManageForm.roundName = this.claseqList.filter(item => item.roundId === this.unitManageForm.roundId)[0].roundName
    },
    changeClass () {
      this.unitManageForm.claName = this.classesList.filter(item => item.rowkey === this.unitManageForm.claId)[0].claShortName
    }
  },
  mounted () {
    this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) == 4
    this.getRoundYear()
    this.findSchoolFun()
  }
}
</script>

<style>
.newselect{flex-wrap: wrap;}
.newselect .el-input{width:200px;}
</style>
