<template>
  <!-- excel导入 -->
  <div>
    <ss-condition-area definedTitle="Excel导入" ref="conditionPage" @setConditionFlag="setConditionFlag">
      <el-form class="mt_-20 batch" label-width="200px" label-position="right" :model="unitManageForm" ref="unitManageForm" :inline="true">
        <ss-class-select ref="selectClass" :classMultiple="false" :defaultValue="true" :defaultLunci="true"  :disabledYearAndLunci="true"/>
        <div class="condition-row" style="padding-left:230px;">
          <div class="item">
            <div class="label">空白EXCEL下载：</div>
            <ss-custom-button @click="mubanDown" :type="$consts.CUSTOMBUTTONTYPES.download" style="margin-right:30px;"/>
            <div class="label">选择文件导入：</div>
            <excelImportBtn @upload="uploadFile" @progress="progress" @uploadEnd="uploadEnd" ref="uploadPro" style="display: flex;align-items: center;"></excelImportBtn>
          </div>
        </div>
      </el-form>
    </ss-condition-area>
    <div class="main-content">
      <ss-table :data="dataList" ref="tabPage" class="infoTable" :pageSize="20" @goPage="find" :pagenum="pagenum" :total="total">
        <el-table-column type="index" align="center" min-width="200" label="序号" />
        <el-table-column prop="stuSeq" align="center" min-width="120" label="轮次">
          <template slot-scope="{ row }">
            <span>{{ row.stuSeq === '1' ? '春季' : '秋季' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stuName" align="center" min-width="200" label="受种者姓名">
          <template slot-scope="{ row }"><span @click="openDetailDialogInfo(row)" style="cursor:pointer;font-weight: bold;color: #2f6cc9;margin-right:10px;">{{row.stuName}}</span></template>
        </el-table-column>
        <el-table-column prop="stuSex" align="center" min-width="200" label="性别" />
        <el-table-column prop="stuBirth" align="center" min-width="200" label="出生日期" />
        <el-table-column prop="stuIsjzz" align="center" min-width="200" label="是否有接种证">
          <template slot-scope="{ row }">
            <span>{{ row.stuIsjzz === '0' ? '有' : '无' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stuParentname" align="center" min-width="200" label="父亲/母亲/监护人姓名" />
        <el-table-column prop="stuPhone" align="center" min-width="200" label="父亲/母亲/监护人电话" />
      </ss-table>
    </div>
    <!-- 详细信息弹框 -->
    <detail-dialog-info
      :visible="dialogVisible"
      :stuId="rowkey"
      @close="close"
    />
  </div>
</template>
<script>
import { excelImport,dowmLoadInfoCollectMould } from '@/utils/axios/apis/collect'
import excelImportBtn from './components/excelImport.vue'
import detailDialogInfo from './components/DialogInfo.vue'
  export default {
    components: { excelImportBtn,detailDialogInfo },
    data () {
      return {
        pagenum: 1,
        total: 0,
        dataList:[],
        unitManageForm: {},
        pagenum: 1,
        total: 0,
        dataList: [],
        importSuccessList: [],
        rowkey:{},
        dialogVisible:false,
      }
    },
    methods: {
      openDetailDialogInfo(rowkey){
        this.rowkey=rowkey;
        this.dialogVisible=true;
      },
      close(){
        this.dialogVisible=false;
        this.rowkey={};
      },
      setConditionFlag (falg) {
        this.$refs.tabPage.setHeight()
      },
      // 前端分页查询
      find (pagenum) {
        this.importSearch(pagenum)
      },
      // 批量导入
      uploadFile (file) {
        this.unitManageForm = this.$refs.selectClass.unitManageForm
        if (!this.unitManageForm.stuclayear) {
          this.$util.errorMsg('请选择年份！')
          return
        }
        if (!this.unitManageForm.stuClalevel) {
          this.$util.errorMsg('请选择年级！')
          return
        }
        if (!this.unitManageForm.classId) {
          this.$util.errorMsg('请选择班级！')
          return
        }
        if (!this.unitManageForm.lunci) {
          this.$util.errorMsg('请选择轮次！')
          return
        }
        this.progress()
        this.unitManageForm = this.$refs.selectClass.unitManageForm
        // 执行上传excel
        this.file=file.file
        let params={
          schId:this.$store.state.schId,
          stuStationcode:this.$store.state.schStationcode,
          schName:this.$store.state.schName,
          classId:this.unitManageForm.classId,
          file:file.file,
          stuSeq:this.unitManageForm.lunci,
          stuclayear:this.unitManageForm.stuclayear
        }
        excelImport(params).then(res => {
          if (res.errorList && res.errorList.length > 0) {
            this.$message({
              dangerouslyUseHTMLString: true,
              message: res.errorList[0].remark,
              type: 'error',
              duration: 6000,
              showClose: true
            })
            this.uploadEnd()
          } else {
            this.$message({
              message: res.message || '导入成功',
              type: res.data.length>0?'success':'error',
              center: true,
              offset: 100,
              duration: 3000,
              showClose: true
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
        let params={
          collectType:0,
          file:this.file
        }
        dowmLoadInfoCollectMould(params)
      }
    }
  }
  </script>