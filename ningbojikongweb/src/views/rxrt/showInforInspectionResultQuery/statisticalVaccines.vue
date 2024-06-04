<!-- 补种疫苗统计表 -->
<template>
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <div class="label">
            <span style="color: red; margin-right: 6px;">*</span>选择地区：
          </div>
          <ss-area-areaTreeStation :showSelect="false" ref="areaTreePage" :disabled="!isJikong"
            :getToLevelNum="5 - currentLevel" @nodeClick="nodeClick" />
        </div>
        <div class="item mLeft">
          <div class="label">学校类型：</div>
          <el-select v-model="schType" clearable @change="changeSchType">
            <el-option v-for="item in selectTypeList" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </div>
        <div class="item mLeft">
            <div class="label">学校名称：</div>
            <el-select v-model="schId" placeholder="请选择学校名称" clearable @change="changeSchool" filterable>
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
        <!-- <div class="item mLeft">
          <div class="label">
            是否完成补种：
          </div>
          <el-select v-model="isFull" placeholder="请选择" clearable>
            <el-option label="是" value="1" />
            <el-option label="否" value="0" />
          </el-select>
        </div> -->
      </div>
      <div class="condition-row">
        <div class="item">
            <div class="label">年级：</div>
            <el-select clearable v-model="grade" placeholder="请选择年级" @change="changeGrade">
              <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code" />
            </el-select>
          </div>
        <div class="item mLeft">
          <div class="label">
            <span style="color: red; margin-right: 6px;">*</span>轮次：
          </div>
          <el-select v-model="rounds" placeholder="请选择轮次" clearable>
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
      <div class="condition-row">
        <div class="item mLeft">
          <div class="label">
            出生日期：
          </div>
          <ss-date-range clearable v-model="birthDate" :startTimeOption="startTimeOption"
            :endTimeOption="endTimeOption" />
        </div>
        <div class="item ">
          <div class="label">
            补种日期：
          </div>
          <ss-date-range clearable v-model="buzhongDate" :startTimeOption="startTimeOption"
            :endTimeOption="endTimeOption" />
        </div>
      </div>
      <div class="condition-row mt_10">
        <!-- 查询 -->
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <!-- <ss-explain-tip-text :tipText="tipText" :secTime="secTime" /> -->
      </div>
    </ss-condition-area>
    <ss-table :data="dataList" ref="tabPage" class="infoTable" :total="total" :showPage="false">
      <el-table-column prop="name" align="center" min-width="120" label="疫苗种类" />
      <el-table-column prop="needVacNum" align="center" min-width="150" label="需补种剂次数" />
      <el-table-column prop="vacNum" align="center" min-width="150" label="补种剂次数" />
      <el-table-column prop="vacRate" align="center" min-width="150" label="补种率(%)" />
    </ss-table>
  </div>
</template>
<script>
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
import { queryReplantVacStat } from '@/utils/axios/apis/rxrt/checkVaccination'
import { findSchoolByAreaAndSchType } from '@/utils/axios/api'
export default {
  mixins: [commonOptions],
  data() {
    return {
      number: '',
      name: '',
      schType: '',
      year: this.$dateutil.currentYear(),
      schoolName: '',
      rounds: '',
      birthDate: [],
      buzhongDate: [],
      isJikong: false,
      // isFull: '',
      tipText: '统计截止到：' + this.$util.dayAdd(this.$util.today(), 0) + '。',
      secTime: 0,
      pagenum: 1,
      total: 0,
      dataList: [],
      importSuccessList: [],
      schoolNameList: [],
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
      gradeList: [],
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      // searchData: {},
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
      schId:'',
      grade:'',
      className:''
    }
  },
  methods: {
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    // 查询
    search(pagenum) {
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
      if (!that.rounds) {
        that.$util.errorMsg('必须选择轮次！')
        return
      }
      if (that.birthDate[0] && !that.birthDate[1]) {
        that.$util.errorMsg('请确认出生日期的填写是否完整！')
        return
      }
      if (!that.birthDate[0] && that.birthDate[1]) {
        that.$util.errorMsg('请确认出生日期的填写是否完整！')
        return
      }
      if (that.buzhongDate[0] && !that.buzhongDate[1]) {
        that.$util.errorMsg('请确认补种日期的填写是否完整！')
        return
      }
      if (!that.buzhongDate[0] && that.buzhongDate[1]) {
        that.$util.errorMsg('请确认补种日期的填写是否完整！')
        return
      }
      let params = {
        // areaId: that.$refs.areaTreePage.selectAreaId,
        areaCode: that.$refs.areaTreePage.selectAreaCode,
        areaLevel: that.$refs.areaTreePage.selectAreaLevel,
        // selectType: that.$refs.areaTreePage.selectType,
        rounds: that.rounds,
        year: that.year,
        // isFull: that.isFull,
        schType: that.schType,
        birthStart: that.birthDate[0],
        birthEnd: that.birthDate[1],
        replantStart: that.buzhongDate[0],
        replantEnd: that.buzhongDate[1],
        operateType: that.$consts.OPTYPE_QUERY,
        pageNum: pagenum,
        pageSize: 20,
        schId: that.schId,
        grade: that.grade,
        className:that.className
      }
      params.areaName = that.$refs.areaTreePage.selectAreaName
      // params.areaLevel = that.$refs.areaTreePage.selectAreaLevel
      console.log(params)
      that.searchData = JSON.parse(JSON.stringify(params))
      that.openFullScreen()
      queryReplantVacStat(params).then((res) => {
        that.dataList = res.data.list
        that.secTime = res.sec
        that.total = res.data.total
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
    changeSchool(val) {
      if (val) {
        let schType = this.schoolList.filter(item => item.schId == val)[0].schType;
        this.gradeList = this.$util.getGrade(schType)
      } else {
        this.gradeList = this.$util.getGrade(2)
      }
      this.grade = ''
      this.className=''
      this.classesList=[]
    },
    async changeGrade(val){
      if (val) {
        let params={
          schId:this.schId || '-1',
          classLevel:this.grade,
          classYear:this.year,
          lunci:this.round
        }
        this.className=''
        this.classesList=[]
        await this.findClassNew(params)
        if(this.classesList && this.classesList.length==0){
          this.className=''
        }
      }else{
        this.className=''
        this.classesList=[]
      }
    },
    changeSchType(val) {
      if (val) {
        this.schType = val;
        this.gradeList = this.$util.getGrade(val)
        this.schId = ''
        this.grade = ''
        this.findSchool();
      } else {
        this.gradeList = this.$util.getGrade(2)
        this.schId = ''
        this.grade = ''
        this.findSchool();
      }
      this.className=''
      this.classesList=[]
    },
    // 查询学校
    findSchool() {
      findSchoolByAreaAndSchType({
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel,
        schType: this.schType
      }).then((res) => {
        this.schoolList = res.data
      })
    },
    nodeClick(data, checked, node) {
      this.schId=''
      this.findSchool()
    },
  },
  async mounted() {
    this.findClaYear()
    if (this.$refs.areaTreePage.selectAreaCode) {
      await this.findSchool();
      await this.findClaLevelNew()
    }
    this.isJikong = parseInt(sessionStorage.getItem('areaLevel')) !== 4
  }
}
</script>