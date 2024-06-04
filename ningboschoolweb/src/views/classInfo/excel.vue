<template>
  <!-- 批量导入 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row ml_250 mt_-20">
        <div class="item">
          <div class="label">空白EXCEL下载：</div>
          <ss-custom-button @click="mubanDown" :type="$consts.CUSTOMBUTTONTYPES.download"/>
          <div class="label ml_40">选择文件导入：</div>
          <batchImportBtn @upload="uploadFile" @progress="progress" @uploadEnd="uploadEnd" ref="uploadPro" class="import"></batchImportBtn>
        </div>
      </div>
    </ss-condition-area>
    <div class="main-content">
      <ss-table :data="dataList" ref="tabPage" class="infoTable" @goPage="find" :pagenum="pagenum" :total="total">
        <el-table-column prop="claSeq" align="center" min-width="120" label="轮次">
          <template slot-scope="{ row }">
            <span>{{ row.claSeq === '1' ? '春季' : '秋季' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="claName" align="center" min-width="200" label="班级名称" />
        <el-table-column prop="claTotal" align="center" min-width="100" label="班级总人数" />
        <el-table-column prop="claLinkman" align="center" min-width="120" label="联系人" />
        <el-table-column prop="claPhone" align="center" min-width="120" label="电话" />
      </ss-table>
    </div>
  </div>
</template>
<script>
import { inClassMesses, dowmLoadClassMould } from '@/utils/axios/apis/classInfo'
import commonOptions from '@/mixin/commonOptions'
import batchImportBtn from './components/batchImport.vue'
export default {
  name: 'Excel',
  components: { batchImportBtn },
  mixins: [commonOptions],
  data () {
    return {
      secTime: 0,
      pagenum: 1,
      total: 0,
      dataList: [],
      importSuccessList: []
    }
  },
  methods: {
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    },
    // 前端分页查询
    find (pagenum) {
      this.importSearch(pagenum)
    },
    // 批量导入
    uploadFile (file) {
      this.progress()
      // 执行上传excel
      inClassMesses({ file: file.file, schType: this.$store.state.schType, schId: this.$store.state.schId }).then(res => {
        if (res.message && res.data.length === 0) {
          this.$message({
            message: res.message,
            type: 'error',
            duration: 6000,
            showClose: true
          })
          this.uploadEnd()
        } else {
          this.$message({
            message: '导入成功',
            type: 'success'
          })
          this.secTime = res.sec
          this.importSuccessList = res.data
          this.dataList = this.importSuccessList.slice(0, 1 * 20)
          this.total = res.data.length
          this.uploadEnd()
        }
      }).catch((e) => {
        this.uploadEnd()
      })
    },
    // 导入查询
    importSearch (pagenum) {
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      that.dataList = that.importSuccessList.slice((pagenum - 1) * 20, pagenum * 20)
    },
    // 上传文件中
    progress () {
      this.openFullScreen('正在导入班级信息，请稍后...')
    },
    // 上传结束
    uploadEnd () {
      this.closeFullScreen()
      this.$refs.uploadPro.$refs.upload_file.clearFiles()
    },
    // 下载模板
    mubanDown () {
      dowmLoadClassMould()
    }
  }
}
</script>
<style scoped lang="less">
.btn-util-cls() {
  background-size: contain;
  background-position: left top;
  min-width: 108px;
  height: 32px;
  cursor: pointer;
}
.ml_40 {
  margin-left: 40px;
}
.import{
  display: flex;
  align-items: center;
}
</style>
