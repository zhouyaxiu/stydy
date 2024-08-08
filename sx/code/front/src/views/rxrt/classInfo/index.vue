<template>
  <!-- 班级管理 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <div class="label"> <span style="color: red; margin-right: 6px;">*</span>选择地区：</div>
          <ss-area-areaTreeStation :showSelect="false" ref="areaTreePage" :disabled="msgPubPermission"
            :getToLevelNum="5 - currentLevel" @nodeClick="nodeClick" />
        </div>
        <div class="item mLeft">
          <div class="label"><span style="color: red; margin-right: 6px;">*</span>学校类型：</div>
          <el-select v-model="unitManageForm.schType" placeholder="请选择学校类型" clearable @change="schTypeChange">
            <el-option label="幼托机构" value="0" />
            <el-option label="小学" value="1" />
            <el-option label="初中" value="2" />
            <el-option label="高中" value="3" />
            <el-option label="大学" value="4" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label"><span style="color: red; margin-right: 6px;">*</span>学校名称：</div>
          <el-select style="width:200px;" slot="fEnterno" v-model="unitManageForm.schId" placeholder="请选择学校" clearable>
            <el-option style="width:200px;" v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
          </el-select>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <div class="label">班级：</div>
          <el-input clearable maxlength="4" v-model.trim="unitManageForm.claNameYear" placeholder="请输入学年" style="width:200px;"></el-input>
          <el-select clearable v-model="unitManageForm.claLevel" placeholder="请选择年级" style="width:200px;margin-left: 10px;">
            <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code"/>
          </el-select>
          <el-input clearable v-model.trim="unitManageForm.claShortName" placeholder="请输入班级名称" style="width:200px;margin-left: 10px;"></el-input>
        </div>
      </div>
      <div class="condition-row mt_10">
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query"/>
        <!-- <ss-custom-button @click="reset" :type="$consts.CUSTOMBUTTONTYPES.reset" class="ml_10" /> -->
        <ss-custom-button @click="add" :type="$consts.CUSTOMBUTTONTYPES.add" class="ml_10" v-if="msgPubPermission"/>
        <ss-custom-button @click="batchDel" :type="$consts.CUSTOMBUTTONTYPES.batchDelete" class="ml_10" v-if="msgPubPermission"/>
        <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
      </div>
    </ss-condition-area>
    <div class="main-content">
      <ss-table :data="dataList" ref="tabPage" class="infoTable" @goPage="find" :pagenum="pagenum" :total="total" @handleSelectionChange="handleSelectionChange">
        <el-table-column type="selection" align="center" width="55"></el-table-column>
        <el-table-column align="center" min-width="100" label="操作项" v-if="msgPubPermission">
          <template slot-scope="{ row }">
            <el-row>
              <el-button @click.native="edit(row)" type="text" class="table-btn">编辑</el-button>
              <el-button @click.native="del(row)" type="text" class="table-btn">删除</el-button>
            </el-row>
          </template>
        </el-table-column>
        <el-table-column prop="supName" align="center" min-width="100" label="地区名称" />
        <el-table-column prop="stationName" align="center" min-width="200" label="接种单位名称" show-overflow-tooltip />
        <el-table-column prop="schName" align="center" min-width="120" label="学校名称" />
        <el-table-column prop="claName" align="center" min-width="140" label="班级名称" />
        <el-table-column prop="claTotal" align="center" min-width="110" label="班级应查验人数" />
        <el-table-column prop="claLinkman" align="center" min-width="80" label="联系人" />
        <el-table-column prop="claPhone" align="center" min-width="110" label="电话" />
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
import { getLevel } from '@/utils/common/area.js'
export default {
  name: 'classesManage',
  components: { classesManageDialog, exportDialog },
  mixins: [commonOptions],
  data () {
    return {
      curId: '0',
      cModId: this.$store.state.moduleId,
      isEdit: false,
      unitManageDialogVisible: false,
      pagenum: 1,
      total: 0,
      exportDialogVisible: false,
      dataList: [],
      rowKeys: '',
      tipText: '统计截止到：' + this.$util.dayAdd(this.$util.today(), -1) + '。',
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      msgPubPermission: false,
      selectTypeList: [
        { code: '0', name: '幼托机构' },
        { code: '1', name: '小学' },
        { code: '2', name: '初中' },
        { code: '3', name: '高中' },
        { code: '4', name: '大学' }
      ],
      unitManageForm: {
        schType: '',
        schId: '', // 学校
        claNameYear: '', // 学年
        claLevel: '', // 年级
        claShortName: '' // 班级名称
      }
    }
  },
  methods: {
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
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
      if (!that.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        this.$util.errorMsg('必须选择地区！')
        return false
      }
      if (!that.unitManageForm.schType) {
        this.$util.errorMsg('必须选择学校类型！')
        return false
      }
      if (!that.unitManageForm.schId) {
        this.$util.errorMsg('必须选择学校名称！')
        return false
      }
      if (that.unitManageForm.claNameYear && !/^[0-9]{4}[\d]*$/.test(that.unitManageForm.claNameYear)) {
        that.$util.errorMsg('年份请输入四位数字！')
        return
      }
      if (that.unitManageForm.claNameYear && (that.unitManageForm.claNameYear > new Date().getFullYear() || that.unitManageForm.claNameYear < 1900)) {
        that.$util.errorMsg('年份不能小于1900，并且不能大于当前年份！')
        return
      }
      let params = {
        schId: that.unitManageForm.schId,
        claNameYear: that.unitManageForm.claNameYear,
        claLevel: that.unitManageForm.claLevel,
        claShortName: that.unitManageForm.claShortName,
        pageNum: that.pagenum,
        operateType: that.$consts.OPTYPE_QUERY
      }
      that.openFullScreen()
      let schTypeName = this.selectTypeList.filter(item => item.code === this.unitManageForm.schType)[0].name
      let schName = this.schoolList.length !== 0 && this.unitManageForm.schId !== '' && this.schoolList.filter(item => item.schId === this.unitManageForm.schId)[0].schName || ''
      that.searchData = {
        ...params,
        schType: this.unitManageForm.schType,
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaName: this.$refs.areaTreePage.selectAreaName,
        schTypeCn: schTypeName,
        schName: schName
      }
      queryClassMess(params).then((res) => {
        that.dataList = res.data.list
        that.total = res.data.total
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
    exportExcelFun () {
      if (!this.searchData || this.searchData.areaCode !== sessionStorage.getItem('areacodeSelect')) {
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
    },

    nodeClick (data, checked, node) {
      this.unitManageForm.schId = ''
      this.schoolList = []
      this.findSchool()
    },
    async schTypeChange (val) {
      this.unitManageForm.claLevel = ''
      this.unitManageForm.schId = ''
      this.schoolList = []
      await this.findClaLevelNew(val)
      await this.findSchool()
    },
    // 查询学校
    async findSchool () {
      if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        // this.$util.errorMsg('必须选择地区！')
        return false
      }
      if (!this.unitManageForm.schType) {
        // this.$util.errorMsg('必须选择学校类型！')
        return false
      }
      let params = {
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel,
        schType: this.unitManageForm.schType
      }
      await this.findSchoolNew(params)
      // findSchoolByAreaAndSchType({
      //   areaCode: this.$refs.areaTreePage.selectAreaCode,
      //   areaLevel: this.$refs.areaTreePage.selectAreaLevel,
      //   schType: this.schType
      // }).then((res) => {
      //   this.schoolList = res.data
      // })
    }
  },
  mounted () {
    // 等于4为门诊：true，其他是疾控：false
    this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) === 4
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
<!-- <style scoped lang="less">

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
</style> -->
