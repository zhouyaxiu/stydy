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
          <div class="label"><span style="color: red; margin-right: 6px;">*</span>学校类型：</div>
          <el-select v-model="unitManageForm.schType" placeholder="请选择学校类型" clearable @change="changeSchType">
            <el-option label="幼托机构" value="0" />
            <el-option label="小学" value="1" />
            <el-option label="初中" value="2" />
            <el-option label="高中" value="3" />
            <el-option label="大学" value="4" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">
            <span style="color: red; margin-right: 6px;">*</span>学校名称：
          </div>
          <el-select v-model="unitManageForm.schId" placeholder="请选择学校名称" clearable @change="changeSchool">
            <el-option v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">
            <span style="color: red; margin-right: 6px;">*</span>学年：
          </div>
          <el-select v-model="unitManageForm.claNameYear" placeholder="请选择年份" clearable @change="changeSchool">
            <el-option v-for="item in schoolYearList" :key="item" :label="item" :value="item" />
          </el-select>
        </div>
      </div>
      <div class="condition-row">
        <div class="item ">
          <div class="label">
            <span style="color: red; margin-right: 6px;">*</span>年级：
          </div>
          <el-select clearable v-model="unitManageForm.claLevel" placeholder="请选择年级" @change="changeGrade">
            <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">
            <span style="color: red; margin-right: 6px;">*</span>班级：
          </div>
          <el-select clearable v-model="unitManageForm.claShortName" placeholder="请选择班级">
            <el-option v-for="item in classesList" :key="item.rowkey" :label="item.claShortName" :value="item.rowkey" />
          </el-select>
        </div>
      </div>
      <div class="mt_10" style="display:flex;">
        <!-- 导入模板下载 -->
        <ss-custom-button @click="downloadTemplate" :type="$consts.CUSTOMBUTTONTYPES.download" />
        <!-- Excel批量导入 -->
        <batchImportBtn @upload="uploadFile" @progress="progress" @uploadEnd="uploadEnd" ref="uploadPro" class="import ml_10">
        </batchImportBtn>
        <!-- 导入失败查询 -->
        <ss-custom-button @click="importSearch" :type="$consts.CUSTOMBUTTONTYPES.failedQuery" class="ml_10" />
      </div>
    </ss-condition-area>
    <ss-table :data="dataList" ref="tabPage" class="infoTable" @goPage="importSearch" :pagenum="pagenum" :total="total">
      <el-table-column type="index" align="center" width="120" label="序号" />
      <el-table-column prop="name" align="center" min-width="150" label="姓名" />
      <el-table-column prop="cid" align="center" min-width="150" label="身份证件号码" />
      <el-table-column prop="className" align="center" min-width="100" label="所在班级"></el-table-column>
      <el-table-column prop="msg" align="center" min-width="150" label="导入失败原因" show-overflow-tooltip />
    </ss-table>
  </div>
</template>
<script>
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
import batchImportBtn from './components/batchImport.vue'
import { batchExcelImport, downLoadImportMould, errorList } from '@/utils/axios/apis/batchImport'
export default {
  mixins: [commonOptions],
  components: { batchImportBtn },
  data () {
    return {
      number: '',
      name: '',
      tipText: '统计截止到：' + this.$util.dayAdd(this.$util.today(), 0) + '。',
      secTime: 0,
      pagenum: 1,
      total: 0,
      isJikong: false,
      dataList: [],
      importSuccessList: [],
      selectTypeList: this.$store.state.selectTypeList,
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      unitManageForm: {
        schType: '',
        schId: '',
        claNameYear: this.$dateutil.currentYear(),
        claLevel: '',
        claShortName: ''
      }
    }
  },
  methods: {
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    },
    nodeClick (data, checked, node) {
      this.unitManageForm.schId = ''
      this.schoolList = []
      this.findSchool()
    },
    async changeSchType (val) {
      this.unitManageForm.claLevel = ''
      this.unitManageForm.schId = ''
      this.unitManageForm.claShortName = ''
      this.schoolList = []
      this.classesList = []
      await this.findClaLevelNew(val)
      await this.findSchool()
    },
    changeSchool () {
      this.unitManageForm.claLevel = ''
      this.unitManageForm.claShortName = ''
      this.classesList = []
    },
    async changeGrade () {
      this.unitManageForm.claShortName = ''
      await this.queryHighClassMess(this.unitManageForm.schId, this.unitManageForm.claLevel, this.unitManageForm.claNameYear)
    },
    // 查询学校
    async findSchool () {
      let params = {
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel,
        schType: this.unitManageForm.schType
      }
      await this.findSchoolNew(params)
    },
    // 导入失败查询
    importSearch (pagenum) {
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      if (!that.$refs.areaTreePage.selectAreaCode || that.$refs.areaTreePage.selectAreaCode.length === 0) {
        that.$util.errorMsg('必须选择地区！')
        return
      }
      if (!that.unitManageForm.schType) {
        that.$util.errorMsg('必须选择学校类型！')
        return
      }
      if (!that.unitManageForm.schId) {
        that.$util.errorMsg('必须选择学校名称！')
        return
      }
      if (!that.unitManageForm.claNameYear) {
        that.$util.errorMsg('必须选择学年！')
        return
      }
      if (!that.unitManageForm.claLevel) {
        that.$util.errorMsg('必须选择年级！')
        return
      }
      if (!that.unitManageForm.claShortName) {
        that.$util.errorMsg('必须选择班级！')
        return
      }
      let params = {
        schId: that.unitManageForm.schId,
        claId: that.unitManageForm.claShortName,
        operateType: that.$consts.OPTYPE_QUERY,
        pageNum: that.pagenum
      }
      this.openFullScreen()
      errorList(params).then((res) => {
        this.dataList = res.data.list
        this.total = res.data.total
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    // 导入模板下载
    downloadTemplate () {
      downLoadImportMould()
    },
    // 批量导入
    uploadFile (file, type) {
      let that = this
      this.progress()
      if (!that.$refs.areaTreePage.selectAreaCode || that.$refs.areaTreePage.selectAreaCode.length === 0) {
        that.$util.errorMsg('必须选择地区！')
        return
      }
      if (!that.unitManageForm.schId) {
        that.$util.errorMsg('必须选择学校名称！')
        return
      }
      if (!that.unitManageForm.claNameYear) {
        that.$util.errorMsg('必须选择学年！')
        return
      }
      if (!that.unitManageForm.claLevel) {
        that.$util.errorMsg('必须选择年级！')
        return
      }
      if (!that.unitManageForm.claShortName) {
        that.$util.errorMsg('必须选择班级！')
        return
      }
      let params = {
        file: file.file,
        schId: that.unitManageForm.schId,
        schStationCode: that.$refs.areaTreePage.selectAreaCode,
        classId: that.unitManageForm.claShortName,
        className: this.classesList.filter(item => item.rowkey === this.unitManageForm.claShortName)[0].claShortName,
        operateType: that.$consts.OPTYPE_INSERT
      }
      batchExcelImport(params).then(res => {
        if (res.data && res.data.length > 0) {
          this.$util.errorMsg(res.message)
          this.importSuccessList = res.data
          this.dataList = this.importSuccessList.slice(0, 1 * 20)
          this.total = res.data.length
          this.uploadEnd()
        } else {
          if (res.message === '模板文件不正确，请使用正确的导入模板文件！') {
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
    progress () {
      this.openFullScreen('正在导入，请稍后...')
    },
    // 上传结束
    uploadEnd () {
      this.closeFullScreen()
      this.$refs.uploadPro.$refs.upload_file.clearFiles()
    }
  },
  async mounted () {
    await this.findClaYear()
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
