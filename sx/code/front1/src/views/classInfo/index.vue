<template>
  <!-- 批量导入 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row ml_250 mt_-20">
        <div class="item">
          <el-checkbox v-model="isSearch"></el-checkbox>
          <div class="label">班级：</div>
          <el-input clearable maxlength="4" v-model.trim="schoolYear" placeholder="请输入" class="w_240" :disabled="!isSearch"></el-input>
          <div class="label ml_18">年份</div>
          <el-select clearable v-model="grade" placeholder="请选择" class="w_240 "  :disabled="!isSearch">
            <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code"/>
          </el-select>
          <el-input clearable maxlength="4" v-model.trim="classes" placeholder="请输入" class="w_240 ml_18"  :disabled="!isSearch"></el-input>
          <el-checkbox v-model="isLunci" class="ml_18"></el-checkbox>
          <div class="label">轮次：</div>
          <el-select v-model="claSeq" placeholder="请选择" clearable :disabled="!isLunci">
            <el-option label="春季" value="1" />
            <el-option label="秋季" value="0" />
          </el-select>
        </div>
      </div>
      <div class="condition-row mt_10 ml_250">
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query"/>
        <ss-custom-button @click="reset" :type="$consts.CUSTOMBUTTONTYPES.reset" class="ml_20" />
        <ss-custom-button @click="add" :type="$consts.CUSTOMBUTTONTYPES.add" class="ml_20" />
        <ss-custom-button @click="batchDel" :type="$consts.CUSTOMBUTTONTYPES.delete" class="ml_20" />
        <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_20" />
      </div>
    </ss-condition-area>
    <div class="main-content">
      <ss-table :data="dataList" ref="tabPage" class="infoTable" @goPage="find" :pagenum="pagenum" :total="total" @handleSelectionChange="handleSelectionChange">
        <el-table-column type="selection" align="center" width="55"></el-table-column>
        <el-table-column prop="claSeq" align="center" min-width="120" label="轮次">
          <template slot-scope="{ row }">
            <span>{{ row.claSeq === '1' ? '春季' : '秋季' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="claName" align="center" min-width="200" label="班级名称" />
        <el-table-column prop="claTotal" align="center" min-width="100" label="班级总人数" />
        <el-table-column prop="claLinkman" align="center" min-width="120" label="联系人" />
        <el-table-column prop="claPhone" align="center" min-width="120" label="电话" />
        <el-table-column align="center" min-width="80" label="操作">
          <template slot-scope="{ row }">
            <el-row>
              <el-button @click.native="edit(row)" type="text" class="table-btn">编辑</el-button>
              <el-button @click.native="del(row)" type="text" class="table-btn">删除</el-button>
            </el-row>
          </template>
        </el-table-column>
      </ss-table>
      <classesManageDialog :visible="unitManageDialogVisible" @fetchData="search" @closeDialog="close" :isEdit="isEdit" :curId="curId" />
      <export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'班级查询'" :dialogVisible="exportDialogVisible"></export-dialog>
    </div>
  </div>
</template>
<script>
import { queryClassMess, deleteClassMess, outClassMess } from '@/utils/axios/apis/classInfo'
import commonOptions from '@/mixin/commonOptions'
import classesManageDialog from './components/classesManageDialog.vue'
import exportDialog from '@/components/common/exportDialog'
export default {
  name: 'classesManage',
  components: { classesManageDialog, exportDialog },
  mixins: [commonOptions],
  data () {
    return {
      isSearch: false,
      isLunci: false,
      schoolYear: '',
      claSeq: '',
      grade: '',
      classes: '',
      secTime: 0,
      curId: '0',
      cModId: this.$store.state.moduleId,
      isEdit: false,
      unitManageDialogVisible: false,
      pagenum: 1,
      total: 0,
      exportDialogVisible: false,
      dataList: [],
      gradeList: [],
      rowKeys: '',
      tipText: '统计截止到：' + this.$util.dayAdd(this.$util.today(), -1) + '。'
    }
  },
  watch: {
    isSearch (v) {
      if (!v) {
        this.schoolYear = ''
        this.grade = ''
        this.classes = ''
      }
    },
    isLunci (v) {
      if (!v) {
        this.claSeq = ''
      }
    }
  },
  methods: {
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    },
    // 重置
    reset () {
      this.isSearch = false
      this.isLunci = false
      this.schoolYear = ''
      this.grade = ''
      this.classes = ''
      this.claSeq = ''
      this.pagenum = 1
      this.total = 0
      this.secTime = 0
      this.dataList = []
    },
    // 查询判断
    find (pagenum) {
      this.search(pagenum)
    },
    // 查询
    search (pagenum) {
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      if (this.isSearch && this.schoolYear === '' && this.grade === '' && this.classes === '') {
        that.$util.errorMsg('年份和班级名称请至少选择一项!')
        return
      }
      if (that.schoolYear && !/^[0-9]{4}[\d]*$/.test(that.schoolYear)) {
        that.$util.errorMsg('年份请输入四位数字！')
        return
      }
      if (that.schoolYear && (that.schoolYear > new Date().getFullYear() || that.schoolYear < 1900)) {
        that.$util.errorMsg('年份不能小于1900，并且不能大于当前年份！')
        return
      }
      if(that.isLunci && !that.claSeq){
        that.$util.errorMsg('轮次必填')
        return
      }
      let params = {
        claNameYear: that.isSearch ? that.schoolYear : '',
        claShortName: that.isSearch ? that.classes : '',
        claLevel: that.isSearch ? that.grade : '',
        claSeq: that.isLunci?that.claSeq:'',
        cModId: that.cModId,
        operateType: that.$consts.OPTYPE_QUERY,
        pageNum: pagenum,
        pageSize: 20
      }
      that.openFullScreen()
      queryClassMess(params).then((res) => {
        that.dataList = res.data.list
        that.secTime = res.sec
        that.total = res.data.total
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
      that.searchData = JSON.parse(JSON.stringify(params))
    },
    exportExcelFun () {
      if (!this.searchData) {
        this.$util.errorMsg('查询后才可以导出数据！')
        return false
      }
      if (!this.dataList.length) {
        this.$util.errorMsg('暂无可以导出的数据！')
        return false
      }
      this.exportDialogVisible = true
    },
    handleExportClose () {
      this.exportDialogVisible = false
    },
    // 导出
    exportExcel (pagenum) {
      let params = JSON.parse(JSON.stringify(this.searchData))
      params.operateType = this.$consts.OPTYPE_OUTPUT
      params.pageNum = pagenum
      params.pageSize = 5000
      console.log('导出', params)
      outClassMess(params)
    },
    // 添加
    add () {
      this.unitManageDialogVisible = true
    },
    // 编辑
    edit (row) {
      this.unitManageDialogVisible = true
      this.curId = row.rowkey
      this.isEdit = true
    },
    // 删除
    del (row) {
      this.$confirm('是否要确认删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        deleteClassMess({
          classIds: row.rowkey,
          operateType: this.$consts.OPTYPE_DELETE,
          cModId: this.$store.state.moduleId,
          deleteType: row.rowkey.indexOf(',') !== -1 ? 1 : 0
        }).then((res) => {
          if (res.msg && res.msg !== '删除成功!') {
            this.$message({
              type: 'error',
              message: res.msg
            })
            return
          }
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.search()
        })
      }).catch(() => {
        this.closeFullScreen()
        this.$message({
          type: 'info',
          message: '已取消'
        })
      })
    },
    // 批量选择
    handleSelectionChange (val) {
      if (val.length > 0) {
        this.rowKeys = val.map(function (elem) { return elem.rowkey }).join(',')
      } else {
        this.rowKeys = ''
      }
    },
    // 批量删除
    batchDel () {
      if (!this.rowKeys) {
        this.$util.errorMsg('必须选择一条数据！')
      } else {
        this.del({ rowkey: this.rowKeys })
      }
    },
    // 关闭弹窗
    close () {
      this.unitManageDialogVisible = false
      this.isEdit = false
      this.curId = null
    }
  },
  created () {
    if (this.$store.state.schType === '1') {
      this.gradeList = this.primaryGradeList
    } else {
      this.gradeList = this.kindergartenGradeList
    }
  }
}
</script>
<style>
.infoTable .el-table__header-wrapper tr th:nth-last-child(2) {
  border-right: 0 !important;
}

.table-btn {
  display: inline !important;
  padding: 5px !important;
}
.schSelect .el-select__input{
  width: 0.12%;
  margin-left: 5px;
}
</style>
<style scoped lang="less">

.btn-util-cls() {
  background-size: contain;
  background-position: left top;
  min-width: 108px;
  height: 32px;
  cursor: pointer;
}

.deleteBatch-btn {
  background: url(../../assets/img/button/icon-delete-batch.png) left top no-repeat;
  .btn-util-cls
}

.importTemplate-btn {
  .btn-util-cls;
  background: url(../../assets/img/button/icon-import-template.png) left top no-repeat;
  min-width:131px;
  height: 34px;
  a {
    text-decoration: none;
    opacity: 0;
    font-size: 22px;
  }
}
.w_240 {
  width: 240px;
}
.ml_18 {
  margin-left: 18px;
}
.h_32{
  height: 32px !important;
}
</style>
