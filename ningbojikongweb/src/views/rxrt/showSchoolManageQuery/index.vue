<template>
  <!-- 学校管理 -->
  <div class="school">
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row ">
        <div class="item">
          <div class="label"> <span style="color: red; margin-right: 6px;">*</span>选择地区：</div>
          <ss-area-areaTreeStation :showSelect="false" ref="areaTreePage" :disabled="msgPubPermission"
            :getToLevelNum="5 - currentLevel" />
        </div>
        <div class="item mLeft">
          <div class="label">学校类型：</div>
          <el-select v-model="schType" placeholder="请选择学校类型" clearable>
            <el-option label="幼托机构" value="0" />
            <el-option label="小学" value="1" />
          </el-select>
        </div>
        <div class="item mLeft">
          <div class="label">学校编码：</div>
          <el-input placeholder="请输入学校编码" maxlength="3" clearable style="width:200px;" v-model="number"></el-input>
        </div>
        <div class="item mLeft">
          <div class="label">学校名称：</div>
          <el-input placeholder="请输入学校名称" maxlength="10" clearable style="width:200px;" v-model="name"></el-input>
        </div>
      </div>
      <div class="condition-row mt_10">
        <!-- 查询 -->
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <!-- 新增 -->
        <ss-custom-button v-if="msgPubPermission" @click="add()" :type="$consts.CUSTOMBUTTONTYPES.add" class="ml_10" />
        <!-- 删除 -->
        <ss-custom-button v-if="msgPubPermission" @click="batchDelete()" :type="$consts.CUSTOMBUTTONTYPES.delete"
          class="ml_10" />
        <!-- Excel导出 -->
        <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export"
          class="ml_10" />
        <!-- <ss-explain-tip-text :tipText="tipText" :secTime="secTime" /> -->
      </div>
    </ss-condition-area>
    <ss-table :data="dataList" ref="tabPage" class="infoTable" @goPage="search" :pagenum="pagenum" :total="total"
      @handleSelectionChange="handleSelectionChange" :key="msgPubPermission">
      <el-table-column type="selection" align="center" width="55" v-if="msgPubPermission">
      </el-table-column>
      <el-table-column prop="cityName" align="center" min-width="150" label="地市名称" />
      <el-table-column prop="countyName" align="center" min-width="150" label="区县名称" />
      <el-table-column prop="schCode" align="center" min-width="100" label="学校编码" />
      <el-table-column prop="schName" align="center" min-width="150" label="学校名称" show-overflow-tooltip />
      <el-table-column prop="schType" align="center" min-width="100" label="学校类型">
        <template slot-scope="{ row }">
          <span>{{ row.schType === '0' ? '幼托机构' : '小学' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="supCode" align="center" min-width="150" label="地区编码" show-overflow-tooltip />
      <el-table-column prop="supName" align="center" min-width="200" label="地区名称" show-overflow-tooltip />
      <el-table-column prop="schStationCode" align="center" min-width="140" label="所属接种单位编码" />
      <el-table-column prop="schStationName" align="center" min-width="220" label="接种单位名称" show-overflow-tooltip />
      <!-- <el-table-column prop="schPhone" align="center" min-width="120" label="电话" />
            <el-table-column prop="schLinkMan" align="center" min-width="120" label="联系人" /> -->
      <el-table-column align="center" min-width="280" label="操作" v-if="msgPubPermission">
        <template slot-scope="{ row }">
          <el-row>
            <el-button @click.native="edit(row)" type="text" class="table-btn">修改
            </el-button>
            <el-button @click.native="del(row)" type="text" class="table-btn">删除
            </el-button>
            <el-button @click.native="assignAccount(row)" type="text" class="table-btn">账号分配</el-button>
          </el-row>
        </template>
      </el-table-column>
    </ss-table>
    <schoolManageDialog :visible="unitManageDialogVisible" @fetchData="search" @closeDialog="close" :isEdit="isEdit"
      :curId="curId" :areaId="areaId" />
    <assignAccountDialog :visible="assignAccountDialogVisible" :curId="assignId" @closeDialog="closeAssignDialog"
      :schCode="schCode" :schName="schName" :fullCode="fullCode">
    </assignAccountDialog>
    <export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'学校管理'"
      :dialogVisible="exportDialogVisible"></export-dialog>

  </div>
</template>
<script>
import { searchSchoolList, deleteSchool, authorizeSchool, exportSchool } from '@/utils/axios/apis/informationManage/schoolManage'
import commonOptions from '@/mixin/commonOptions'
import schoolManageDialog from './components/schoolManageDialog.vue'
import assignAccountDialog from './components/assignAccountDialog.vue'
import exportDialog from '@/components/common/exportDialog'
import { getLevel } from '@/utils/common/area.js'
export default {
  name: 'school',
  components: { schoolManageDialog, assignAccountDialog, exportDialog },
  mixins: [commonOptions],
  data() {
    return {
      number: '',
      name: '',
      schType: '',
      tipText: '统计截止到：' + this.$util.dayAdd(this.$util.today(), 0) + '。',
      secTime: 0,
      curId: '0',
      areaId: '0',
      cModId: this.$store.state.moduleId,
      isEdit: false,
      unitManageDialogVisible: false,
      pagenum: 1,
      total: 0,
      dataList: [],
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      assignAccountDialogVisible: false,
      selectedId: [],//选中的删除id数组
      assignId: null,
      schCode: null,
      schName: null,
      fullCode: null,
      msgPubPermission: false,
      exportDialogVisible: false
    }
  },
  methods: {
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    handleSelectionChange(row) {
      this.selectedId = row.map(item => item.schId)
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
      let params = {
        areaId: that.$refs.areaTreePage.selectAreaId,
        areaCode: that.$refs.areaTreePage.selectAreaCode,
        areaLevel: that.$refs.areaTreePage.selectAreaLevel,
        selectType: that.$refs.areaTreePage.selectType,
        schName: that.name,
        schCode: that.number,
        // cModId: that.cModId,
        schType: that.schType,
        operateType: that.$consts.OPTYPE_QUERY,
        pageNum: pagenum,
        pageSize: 20
      }
      params.areaName = that.$refs.areaTreePage.selectAreaName
      params.areaLevel = that.$refs.areaTreePage.selectAreaLevel
      that.searchData = params
      that.openFullScreen()
      searchSchoolList(params).then((res) => {
        that.dataList = res.data.list
        that.secTime = res.sec
        that.total = res.data.total
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
    handleExportClose() {
      this.exportDialogVisible = false
    },
    exportExcelFun() {
      if (!this.searchData || this.searchData.areaCode!=sessionStorage.getItem('areacodeSelect')) {
        this.$util.errorMsg('查询后才可以导出！')
        return
      }
      if (this.dataList && this.dataList.length === 0) {
        this.$util.errorMsg('暂无需要导出的数据！')
        return
      }
      this.exportDialogVisible = true
    },
    exportExcel(pageNum) {
      let params = JSON.parse(JSON.stringify(this.searchData))
      params.operateType = this.$consts.OPTYPE_OUTPUT
      params.pageNum = pageNum
      console.log('导出参数', params)
      exportSchool(params)
    },
    // 添加
    add() {
      this.unitManageDialogVisible = true
    },
    // 编辑
    edit(row) {
      this.unitManageDialogVisible = true
      this.curId = row.schId
      this.isEdit = true
    },
    // 删除
    del(row) {
      this.$confirm('请确认是否删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        deleteSchool({
          schIds: row.schId,
          operateType: this.$consts.OPTYPE_DELETE,
          cModId: this.$store.state.moduleId
        }).then((res) => {
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
    // 关闭弹窗
    close() {
      this.unitManageDialogVisible = false
      this.isEdit = false
      this.curId = null
    },
    closeAssignDialog() {
      this.assignAccountDialogVisible = false;
      this.assignId = null;
      this.schCode = null;
      this.schName = null;
      this.fullCode = null;
    },
    // 分配账号
    assignAccount(row) {
      this.assignId = row.schId;
      this.schCode = row.schCode;
      this.schName = row.schName;
      this.fullCode = row.fullCode;
      this.assignAccountDialogVisible = true;
    },
    // 批量删除
    batchDelete() {
      if (this.selectedId.length == 0) {
        this.$util.errorMsg('请选择要删除的学校！')
        return
      } else {
        // TODO：删除接口
        this.$confirm('请确认是否删除所选的学校?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          deleteSchool({
            schIds: this.selectedId.join(','),
            operateType: this.$consts.OPTYPE_DELETE,
            cModId: this.$store.state.moduleId
          }).then((res) => {
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
      }
    }
  },
  mounted() {
    this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) == 4
    console.log(this.msgPubPermission)
    // 等于4为门诊：true，其他是疾控：false
  }
}
</script>
<style >
.infoTable .el-table__header-wrapper tr th:nth-last-child(2) {
  border-right: 0 !important;
}

.table-btn {
  display: inline !important;
}
</style>
<style scoped>
.ml_10 {
  margin-left: 10px;
}

.mt_10 {
  margin-top: 10px;
}

.school .condition-row .label {
  width: auto !important;
}
</style>
