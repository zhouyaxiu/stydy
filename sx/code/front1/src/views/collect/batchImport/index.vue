<template>
  <!-- Excel批量导入 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag" :titleLong="true">
      <ss-class-select ref="selectClass" :classMultiple="false" :defaultValue="true" :defaultLunci="true"
        :disabledYearAndLunci="true" :isRequired="true">
      </ss-class-select>
      <div class="button-set">
        <!-- 导入模板下载 -->
        <ss-custom-button @click="downloadTemplate" class="mr10" :type="$consts.CUSTOMBUTTONTYPES.downloadImport" />
        <!--  Excel批量导入-->
        <batchImportBtn @upload="uploadFile" class="mr10" @progress="progress" @uploadEnd="uploadEnd" ref="uploadPro">
        </batchImportBtn>
        <!-- 导入失败查询 -->
        <ss-custom-button @click="errorSearch" :type="$consts.CUSTOMBUTTONTYPES.failedQuery" />
      </div>
    </ss-condition-area>
    <div class="main-content">
      <ss-table :data="dataList" ref="tabPage" class="infoTable" @goPage="find" :pagenum="pagenum" :total="total">
        <el-table-column type="index" align="center" width="120" label="序号" />
        <el-table-column prop="name" align="center" min-width="150" label="姓名" />
        <el-table-column prop="fchildNo" align="center" min-width="150" label="编码" />
        <el-table-column prop="className" align="center" min-width="100" label="所在班级"></el-table-column>
        <el-table-column prop="msg" align="center" min-width="150" label="导入失败原因" show-overflow-tooltip />
      </ss-table>
    </div>
  </div>
</template>
<script>
import commonOptions from '@/mixin/commonOptions'
import batchImportBtn from './components/batchImport.vue'
import downloadFile from './components/downloadFile.vue'
import { batchExcelImport, downLoadImportMould, errorList } from '@/utils/axios/apis/batchExcel/index'
export default {
  name: 'batchImport',
  components: { batchImportBtn, downloadFile },
  mixins: [commonOptions],
  data() {
    return {
      cModId: this.$store.state.moduleId,
      importVisible: false,
      isDownFileVisible: false,
      pagenum: 1,
      total: 0,
      dataList: [],
      isImportSearch: false,
      importSuccessList: [],
      tipText: '批量导入未成功的受种者信息请使用信息扫码单独处理',
      schStationcode: this.$store.state.schStationcode,
      falg: null
    }
  },
  methods: {
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    // 下载模板弹窗
    downloadFileClose() {
      this.isDownFileVisible = false
    },
    // 下载模板弹窗
    downloadFileOpen() {
      console.log(111, this.$refs.selectClass)
      this.isDownFileVisible = true
    },
    importClose() {
      this.importVisible = false
    },
    find(pagenum) {
      if (this.falg) {
        this.errorSearch(pagenum)
      } else {
        this.importSearch(pagenum)
      }
    },
    importSearch(pagenum) {
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      that.dataList = that.importSuccessList.slice((pagenum - 1) * 20, pagenum * 20)
    },
    // 导入查询
    errorSearch(pagenum) {
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      let unitManageForm = this.$refs.selectClass.unitManageForm
      if (!unitManageForm.stuclayear) {
        that.$util.errorMsg('必须选择学年！')
        return
      }
      if (!unitManageForm.stuClalevel) {
        that.$util.errorMsg('必须选择年级！')
        return
      }
      if (!unitManageForm.lunci) {
        that.$util.errorMsg('必须选择轮次！')
        return
      }
      let params = {
        schId: that.$store.state.schId,
        schStationCode: that.$store.state.schStationcode,
        year: unitManageForm.stuclayear,
        round: unitManageForm.lunci,
        grade: unitManageForm.stuClalevel,
        operateType: that.$consts.OPTYPE_QUERY,
        pageNum: pagenum
      }
      this.openFullScreen()
      // todo
      errorList(params).then((res) => {
        this.falg = true;
        console.log(res)
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
      let unitManageForm = this.$refs.selectClass.unitManageForm
      if (!unitManageForm.stuclayear) {
        that.$util.errorMsg('必须选择学年！')
        return
      }
      if (!unitManageForm.stuClalevel) {
        that.$util.errorMsg('必须选择年级！')
        return
      }
      if (!unitManageForm.lunci) {
        that.$util.errorMsg('必须选择轮次！')
        return
      }
      let params = {
        schId: that.$store.state.schId,
        schStationCode: that.$store.state.schStationcode,
        year: unitManageForm.stuclayear,
        round: unitManageForm.lunci,
        grade: unitManageForm.stuClalevel,
        operateType: that.$consts.OPTYPE_INSERT,
        file: file.file
      }
      batchExcelImport(params).then(res => {
        this.falg = false;
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
  }
}
</script>
<style>
.batch .el-form-item {
  margin-bottom: 10px;
}

.infoTable .el-table__header-wrapper tr th:nth-last-child(2) {
  border-right: 0 !important;
}

.table-btn {
  display: inline !important;
}

.schSelect .el-select__input {
  width: 0.12%;
  margin-left: 5px;
}
</style>
<style scoped lang="less">
.ml_40 {
  margin-left: 40px;
}

.tips {
  color: #FF2121;
}

.mr10 {
  margin-right: 10px;
}
</style>
