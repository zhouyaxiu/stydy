<!-- Excel批量导入 -->
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
          <el-select v-model="schType" @change="changeSchType" clearable>
            <el-option v-for="item in selectTypeList" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">
            <span style="color: red; margin-right: 6px;">*</span>学校名称：
          </div>
          <el-select v-model="schId" placeholder="请选择学校名称" clearable @change="changeSchool">
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
        <div class="item ">
          <div class="label">
            <span style="color: red; margin-right: 6px;">*</span>年级：
          </div>
          <el-select clearable v-model="grade" placeholder="请选择年级">
            <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">
            <span style="color: red; margin-right: 6px;">*</span>轮次：
          </div>
          <el-select v-model="lunci" placeholder="请选择轮次" clearable>
            <el-option label="春季" value="1" />
            <el-option label="秋季" value="0" />
          </el-select>
        </div>
      </div>
      <div class="mt_10" style="display:flex;">
        <!-- 导入失败查询 -->
        <ss-custom-button @click="importSearch" :type="$consts.CUSTOMBUTTONTYPES.failedQuery" />
        <!-- 导入模板下载 -->
        <ss-custom-button @click="downloadTemplate" :type="$consts.CUSTOMBUTTONTYPES.download" class="ml_10" />
        <!-- Excel批量导入 -->
        <batchImportBtn @upload="uploadFile" @progress="progress" @uploadEnd="uploadEnd" ref="uploadPro"
          class="import ml_10">
        </batchImportBtn>
        <!-- <ss-explain-tip-text :tipText="tipText" :secTime="secTime" /> -->
      </div>
    </ss-condition-area>
    <ss-table :data="dataList" ref="tabPage" class="infoTable" @goPage="importSearch" :pagenum="pagenum" :total="total">
      <el-table-column type="index" align="center" width="120" label="序号" />
      <el-table-column prop="name" align="center" min-width="150" label="姓名" />
      <el-table-column prop="fchildNo" align="center" min-width="150" label="编码" />
      <el-table-column prop="className" align="center" min-width="100" label="所在班级"></el-table-column>
      <el-table-column prop="msg" align="center" min-width="150" label="导入失败原因" show-overflow-tooltip />
    </ss-table>
  </div>
</template>
<script>
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
import batchImportBtn from './components/batchImport.vue'
import { findSchoolByAreaAndSchType } from '@/utils/axios/api'
import { batchExcelImport, downLoadImportMould, errorList } from '@/utils/axios/apis/batchImport/index'
export default {
  mixins: [commonOptions],
  components: { batchImportBtn },
  data() {
    return {
      number: '',
      name: '',
      schType: '',
      year: this.$dateutil.currentYear(),
      schId: '',
      grade: '',
      lunci: '',
      schStationCode: '',
      tipText: '统计截止到：' + this.$util.dayAdd(this.$util.today(), 0) + '。',
      secTime: 0,
      pagenum: 1,
      total: 0,
      isJikong: false,
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
      currentLevel: Number(getLevel(this.$store.state.userCode))
    }
  },
  methods: {
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    nodeClick(data, checked, node) {
      this.findSchool()
    },
    changeSchool(val) {
      if (val) {
        this.schStationCode = this.schoolList.filter(item => item.schId == val)[0].schStationCode
        let schType = this.schoolList.filter(item => item.schId == val)[0].schType;
        this.gradeList = this.$util.getGrade(schType)
        this.grade = ''
      } else {
        this.gradeList = this.$util.getGrade(2)
        this.grade = ''
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
    // 导入失败查询
    importSearch(pagenum) {
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      if (!that.$refs.areaTreePage.selectAreaCode || that.$refs.areaTreePage.selectAreaCode.length === 0) {
        that.$util.errorMsg('必须选择地区！')
        return
      }
      if (!that.schId) {
        that.$util.errorMsg('必须选择学校名称！')
        return
      }
      if (!that.year) {
        that.$util.errorMsg('必须选择学年！')
        return
      }
      if (!that.grade) {
        that.$util.errorMsg('必须选择年级！')
        return
      }
      if (!that.lunci) {
        that.$util.errorMsg('必须选择轮次！')
        return
      }
      let params = {
        schId: that.schId,
        schStationCode: that.schStationCode,
        year: that.year,
        round: that.lunci,
        grade: that.grade,
        operateType: that.$consts.OPTYPE_QUERY,
        pageNum: pagenum
      }
      this.openFullScreen()
      // todo
      errorList(params).then((res) => {
        this.dataList = res.data.list;
        this.total = res.data.total;
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    // 导入模板下载
    downloadTemplate() {
      downLoadImportMould()
    },
    // 批量导入
    uploadFile(file, type) {
      let that = this
      this.progress()
      if (!that.$refs.areaTreePage.selectAreaCode || that.$refs.areaTreePage.selectAreaCode.length === 0) {
        that.$util.errorMsg('必须选择地区！')
        return
      }
      if (!that.schId) {
        that.$util.errorMsg('必须选择学校名称！')
        return
      }
      if (!that.year) {
        that.$util.errorMsg('必须选择学年！')
        return
      }
      if (!that.grade) {
        that.$util.errorMsg('必须选择年级！')
        return
      }
      if (!that.lunci) {
        that.$util.errorMsg('必须选择轮次！')
        return
      }
      let params = {
        schId: that.schId,
        schStationCode: that.schStationCode,
        year: that.year,
        round: that.lunci,
        grade: that.grade,
        operateType: that.$consts.OPTYPE_INSERT,
        file: file.file
      }
      batchExcelImport(params).then(res => {
        if (res.data && res.data.length > 0) {
          this.$util.errorMsg(res.message)
          this.importSuccessList = res.data
          this.dataList = this.importSuccessList.slice(0, 1 * 20)
          this.total = res.data.length
          this.uploadEnd()
        }
        else {
          if (res.message == '模板文件不正确，请使用正确的导入模板文件！') {
            this.$message({
              message: res.message,
              type: 'error'
            })
          } else {
            this.$message({
              message: res.message,
              type: 'success'
            })
          }
          this.uploadEnd()
        }
      }).catch((e) => {
        this.uploadEnd()
      })
    },
    // 上传文件中
    progress() {
      this.openFullScreen('正在导入，请稍后...')
    },
    // 上传结束
    uploadEnd() {
      this.closeFullScreen()
      this.$refs.uploadPro.$refs.upload_file.clearFiles()
    }
  },
  mounted() {
    this.findClaYear()
    if (this.$refs.areaTreePage.selectAreaCode) {
      this.findSchool();
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
</style>