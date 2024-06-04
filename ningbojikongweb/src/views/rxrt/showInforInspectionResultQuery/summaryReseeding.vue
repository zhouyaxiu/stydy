<!-- 补种汇总表 -->
<template>
  <div :class="!isJikong ? 'reseeding2' : 'reseeding'">
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <!-- 门诊端登录 -->
      <div v-if="!isJikong">
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
            <el-select v-model="schType" @change="changeSchType" clearable>
              <el-option v-for="item in selectTypeList" :key="item.code" :label="item.name" :value="item.code" />
            </el-select>
          </div>
          <div class="item mLeft">
            <div class="label">
              学校名称：
            </div>
            <el-select v-model="schId" placeholder="请选择学校名称" clearable @change="changeSchool" filterable>
              <el-option v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
            </el-select>
          </div>
          <div class="item mLeft">
            <div class="label">
              <span style="color: red; margin-right: 6px;">*</span>学年：
            </div>
            <el-select v-model="year" placeholder="请选择年份" clearable>
              <el-option v-for="item in schoolYearList" :key="item" :label="item" :value="item" />
            </el-select>
          </div>
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
            <el-select v-model="round" placeholder="请选择轮次" clearable>
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
      </div>
      <!-- 疾控端登录 -->
      <div v-else>
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
            <el-select v-model="schType" @change="changeSchType" clearable>
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
            <el-select v-model="year" placeholder="请选择年份" clearable>
              <el-option v-for="item in schoolYearList" :key="item" :label="item" :value="item" />
            </el-select>
          </div>
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
            <el-select v-model="round" placeholder="请选择轮次" @change="changeGrade" clearable>
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
      </div>
      <div class="condition-row mt_10">
        <!-- 查询 -->
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <!-- 导出 -->
        <ss-custom-button @click="exportExcel" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
        <!-- 打印 v-print="printObj"-->
        <!-- <ss-custom-button @click="print" :type="$consts.CUSTOMBUTTONTYPES.print" class="ml_10" /> -->
        <!-- <ss-explain-tip-text :tipText="tipText" :secTime="secTime" /> -->
      </div>
    </ss-condition-area>
    <div class="mb_10" v-if="isChayan">应查验幼托（学校）数{{ virtualSchoolCount }}个 实查验幼托（学校）数{{
      schoolCount
    }}个</div>
    <ss-table style="width: 100%;margin-top:10px" id="summary_table" :data="dataList" ref="tabPage" class="summary"
      :changeNull="true" @goPage="search">
      <!-- 疾控端显示单位名称 -->
      <el-table-column align="center" prop="name" min-width="250">
        <template slot="header" slot-scope="scope">
          <span>{{ labelTitle }}</span>
        </template>
        <template slot-scope="{ row }">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="应查验幼托、学校数" v-if="isJikong" prop="virtualSchoolCount"
        min-width="150"></el-table-column>
      <el-table-column align="center" label="实查验幼托、学校数" v-if="isJikong" prop="schoolCount"
        min-width="150"></el-table-column>
      <el-table-column align="center" label="应查验人数" prop="virtualStudentCount" min-width="110"></el-table-column>
      <el-table-column align="center" label="实查验人数" prop="studentCount" min-width="110"></el-table-column>
      <el-table-column prop="hasNotCard" align="center" min-width="120" label="无证人数" />
      <el-table-column prop="needCard" align="center" min-width="120" label="补接种证人数" />
      <el-table-column prop="needReplant" align="center" min-width="120" label="需补种疫苗人数" showOverflowTooltip />
      <el-table-column prop="needReplantFull" align="center" min-width="150" label="完成全程补种人数" />
      <el-table-column prop="stu_stationname" align="center" min-width="100" label="卡介苗">
        <el-table-column align="center" label="应补种剂次" prop="B001NeedReplant" min-width="110"></el-table-column>
        <el-table-column align="center" label="补种剂次" prop="B001HasReplant" min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="乙肝疫苗">
        <el-table-column align="center" label="应补种剂次" prop="YiGanNeedReplantSubtotal" min-width="110"></el-table-column>
        <el-table-column align="center" label="补种剂次" prop="YiGanHasReplantSubtotal" min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="脊灰疫苗">
        <el-table-column align="center" label="应补种剂次" prop="JiHuiNeedReplantSubtotal" min-width="110"></el-table-column>
        <el-table-column align="center" label="补种剂次" prop="JiHuiHasReplantSubtotal" min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="百白破疫苗">
        <el-table-column align="center" label="应补种剂次" prop="BaiBaiPoNeedReplantSubtotal"
          min-width="110"></el-table-column>
        <el-table-column align="center" label="补种剂次" prop="BaiBaiPoHasReplantSubtotal"
          min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="白破疫苗" min-width="100">
        <el-table-column align="center" label="应补种剂次" prop="BaiPoNeedReplantSubtotal" min-width="110"></el-table-column>
        <el-table-column align="center" label="补种剂次" prop="BaiPoHasReplantSubtotal" min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="麻腮风疫苗">
        <el-table-column align="center" label="应补种剂次" prop="MaFengNeedReplantSubtotal" min-width="110"></el-table-column>
        <el-table-column align="center" label="补种剂次" prop="MaFengHasReplantSubtotal" min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="乙脑疫苗">
        <el-table-column align="center" label="应补种剂次" prop="YiNaoJianDuHuoNeedReplantSubtotal"
          min-width="110"></el-table-column>
        <el-table-column align="center" label="补种剂次" prop="YiNaoJianDuHuoHasReplantSubtotal"
          min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="A群流脑疫苗">
        <el-table-column align="center" label="应补种剂次" prop="AQunLiuNaoNeedReplantSubtotal"
          min-width="110"></el-table-column>
        <el-table-column align="center" label="补种剂次" prop="AQunLiuNaoHasReplantSubtotal"
          min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="A＋C群流脑疫苗">
        <el-table-column align="center" label="应补种剂次" prop="ACQunLiuNaoNeedReplantSubtotal"
          min-width="110"></el-table-column>
        <el-table-column align="center" label="补种剂次" prop="ACQunLiuNaoHasReplantSubtotal"
          min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="甲肝灭活疫苗" min-width="100">
        <el-table-column align="center" label="应补种剂次" prop="JiaGanMieNeedReplantSubtotal"
          min-width="110"></el-table-column>
        <el-table-column align="center" label="补种剂次" prop="JiaGanMieHasReplantSubtotal"
          min-width="110"></el-table-column>
      </el-table-column>
      <el-table-column align="center" label="*水痘疫苗">
        <el-table-column align="center" label="应补种剂次" prop="ShuiDouNeedReplantSubtotal"
          min-width="110"></el-table-column>
        <el-table-column align="center" label="补种剂次" prop="ShuiDouHasReplantSubtotal" min-width="110"></el-table-column>
      </el-table-column>
    </ss-table>
  </div>
</template>
<script>
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
import { findSchoolByAreaAndSchType } from '@/utils/axios/api'
import { reseedQueryList, reseedOutput } from '@/utils/axios/apis/reseedSummar/index'

export default {
  mixins: [commonOptions],
  data() {
    return {
      number: '',
      name: '',
      schType: '',
      year: this.$dateutil.currentYear(),
      schId: '',
      grade: '',
      round: '',
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
      isJikong: false,
      isShow: false,
      virtualSchoolCount: 0,
      schoolCount: 0,
      exportAreaName: '',
      printObj: {
        id: "summary_table",
        popTitle: "补种汇总表",
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>',
        beforeOpenCallback(vue) {
          console.log("打开之前");
        },
        openCallback(vue) {
          console.log("执行了打印");
        },
        closeCallback(vue) {
          console.log("关闭了打印工具");
        },
      },
      className:'',//班级
      labelTitle:'单位名称',//是否可修改单位名称
      isChayan:false,//是否点击搜索按钮
    }
  },
  methods: {
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    // 导出
    exportExcel() {
      let that = this
      if (!that.searchData) {
        that.$util.errorMsg('查询后才可以导出！')
        return
      }
      if (that.dataList.length === 0) {
        that.$util.errorMsg('暂无需要导出的数据！')
        return
      }
      let params = JSON.parse(JSON.stringify(that.searchData))
      params.areaName = this.exportAreaName
      if (params.schId) {
        params.schName = that.schoolList.filter(item => item.schId == params.schId)[0].schName
      } else {
        params.schName = ''
      }
      params.operateType = that.$consts.OPTYPE_OUTPUT
      reseedOutput(params)
    },
    // 打印
    print() {
      let that = this
      if (!that.searchData || this.searchData.areaCode!=sessionStorage.getItem('areacodeSelect')) {
        that.$util.errorMsg('查询后才可以打印！')
        return
      }
      if (that.dataList.length === 0) {
        that.$util.errorMsg('暂无需要打印的数据！')
        return
      }
      // let params = {
      //   stuRowId: this.multipleSelection.join(','),
      //   schName: this.$store.state.schName
      // }
      // printOut(params)
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
    nodeClick(data, checked, node) {
      this.schId=''
      if(this.$refs.areaTreePage.selectAreaLevel=='4'){
        this.findSchool()
      }else{
        this.schoolList=[]
      }
      this.className=''
      this.classesList=[]
      this.grade = ''
      if(this.schType==''){
        this.gradeList = this.$util.getGrade(2)
      }else{
        this.gradeList = this.$util.getGrade(this.schType)
      }
    },
    changeSchType(val) {
      if (val && this.$refs.areaTreePage.selectAreaLevel=='4') {
        this.schType = val;
        this.gradeList = this.$util.getGrade(val)
        this.schId = ''
        this.grade = ''
        this.findSchool();
      } else {
        this.gradeList = this.$util.getGrade(2)
        this.schId = ''
        this.grade = ''
        if(this.$refs.areaTreePage.selectAreaLevel=='4'){
          this.findSchool();
        }
        this.schoolList=[]
      }
      if(this.schType==''){
        this.gradeList = this.$util.getGrade(2)
      }else{
        this.gradeList = this.$util.getGrade(this.schType)
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
    // 查询
    search() {
      let that = this
      if (!that.$refs.areaTreePage.selectAreaCode || that.$refs.areaTreePage.selectAreaCode.length === 0) {
        that.$util.errorMsg('必须选择地区！')
        return
      }
      if (!that.year) {
        that.$util.errorMsg('必须选择学年！')
        return
      }
      // if (!that.grade && !this.isJikong) {
      //   that.$util.errorMsg('必须选择年级！')
      //   return
      // }
      if (!that.round) {
        that.$util.errorMsg('必须选择轮次！')
        return
      }
      let params = {
        areaCode: that.$refs.areaTreePage.selectAreaCode,
        areaLevel: that.$refs.areaTreePage.selectAreaLevel,
        // selectType: that.$refs.areaTreePage.selectType,
        operateType: that.$consts.OPTYPE_QUERY,
        schId: that.schId,
        year: that.year,
        round: that.round,
        grade: that.grade,
        schType: that.schType,
        isStation: that.isJikong && this.$refs.areaTreePage.selectAreaLevel!='4' ? 1 : 0,
        className:that.className
      }
      that.exportAreaName = that.$refs.areaTreePage.selectAreaName + ' ' + that.$refs.areaTreePage.selectAreaCode
      that.searchData = params;
      that.isShow = true;
      that.openFullScreen()
      reseedQueryList(params).then(res => {
        if (res.data) {
          that.dataList = res.data;
          this.labelTitle=this.$refs.areaTreePage.selectAreaLevel=='4' && this.schId!=''?'名称':'单位名称'
          that.dataList.forEach(row=>{
            row.name=row.name!='合计' && row.grade!=null && this.schId!=''?row.grade+row.name:row.name
          })
          if (that.dataList.length > 0) {
            that.virtualSchoolCount = that.dataList[that.dataList.length - 1].virtualSchoolCount;
            that.schoolCount = that.dataList[that.dataList.length - 1].schoolCount;
          }
          if(this.schId=='' && !this.isJikong && that.virtualSchoolCount>0){
            this.isChayan=true
          }else{
            this.isChayan=false
          }
        }
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })

    },

  },
  async mounted() {
    this.findClaYear()
    if (this.$refs.areaTreePage.selectAreaCode) {
      // await this.findSchool();
      await this.findClaLevelNew()
    }
    if(this.$refs.areaTreePage.selectAreaLevel=='4'){
      this.findSchool()
    }
    this.isJikong = parseInt(sessionStorage.getItem('areaLevel')) !== 4
  }
}
</script>
<style scoped>
.mt_10 {
  margin-top: 10px;
}

.ml_10 {
  margin-left: 10px;
}

/* .reseeding .condition-row .label {
  width: auto !important;
} */

.reseeding2 .condition-row .label {
  width: 120px !important;
}
</style>
<style>
.summary .el-table__header-wrapper tr:last-child th:nth-last-child(2),
.summary .el-table__header-wrapper tr:nth-last-child(2) th:nth-last-child(2),
.summary .el-table__header-wrapper tr+tr th:nth-last-child(2),
.summary .el-table__header-wrapper tr th:not(.is-leaf) {
  border-right: 1px solid #C0D2EE !important;
  text-align: center !important;
}

.summary .el-table__header-wrapper tr th:last-child,
.summary .el-table__header-wrapper tr th:nth-last-child(2) {
  border-right: none !important
}

.summary .el-table__fixed-right {
  right: 0 !important;
}
</style>