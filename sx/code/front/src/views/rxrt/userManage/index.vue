<template>
  <!-- 用户管理 -->
  <div class="school">
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row ">
        <div class="item">
          <div class="label"> <span style="color: red; margin-right: 6px;">*</span>所属地区：</div>
          <ss-area-areaTreeStation :showSelect="false" ref="areaTreePage" :disabled="msgPubPermission"
            :getToLevelNum="5 - currentLevel" />
        </div>
        <div class="item mLeft">
          <div class="label">登录账号：</div>
          <el-input placeholder="请输入登录账号" clearable style="width:200px;" v-model="loginName"></el-input>
        </div>
        <div class="item mLeft">
          <div class="label">用户显示名：</div>
          <el-input placeholder="请输入用户显示名" clearable style="width:200px;" v-model="userName"></el-input>
        </div>
      </div>
      <div class="condition-row mt_10">
        <!-- 查询 -->
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <!-- 新增 -->
        <ss-custom-button @click="add()" :type="$consts.CUSTOMBUTTONTYPES.add" class="ml_10" />
        <!-- 删除 -->
        <!-- <ss-custom-button v-if="msgPubPermission" @click="batchDelete()" :type="$consts.CUSTOMBUTTONTYPES.delete"
          class="ml_10" /> -->
        <!-- <ss-explain-tip-text :tipText="tipText" :secTime="secTime" /> -->
      </div>
    </ss-condition-area>
    <ss-table :data="dataList" ref="tabPage" class="infoTable" @goPage="search" :pagenum="pagenum" :total="total">
      <!-- <el-table-column type="selection" align="center" width="55">
      </el-table-column> -->
      <el-table-column type="index" align="center" label="序号" width="100">
      </el-table-column>
      <el-table-column align="center" min-width="130" label="操作">
        <template slot-scope="{ row }">
          <el-row>
            <el-button @click.native="edit(row)" type="text" class="table-btn">修改
            </el-button>
            <el-button @click.native="del(row)" type="text" class="table-btn">删除
            </el-button>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column prop="areaName" align="center" min-width="250" label="所属地区" />
      <el-table-column prop="loginName" align="center" min-width="150" label="用户登录账号" />
      <el-table-column prop="userName" align="center" min-width="150" label="用户显示名" />
    </ss-table>
    <publishDialog :visible="unitManageDialogVisible" @close="close" @fetchData="search" :isEdit="isEdit" :row="row" />
  </div>
</template>
<script>
import { uerList,delUser } from '@/utils/axios/apis/rxrt/userManage'
import commonOptions from '@/mixin/commonOptions'
import publishDialog from './components/publishDialog.vue'
import { getLevel } from '@/utils/common/area.js'
export default {
  name: 'school',
  components: { publishDialog },
  mixins: [commonOptions],
  data() {
    return {
      row: {},
      isEdit: false,
      unitManageDialogVisible: false,
      pagenum: 1,
      total: 0,
      dataList: [],
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      msgPubPermission: false,
      loginName:'',
      userName:''
    }
  },
  methods: {
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    // handleSelectionChange(row) {
    //   console.log(row)
    //   this.selectedId = row.map(item => item.schId)
    // },
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
        operateType: that.$consts.OPTYPE_QUERY,
        pageNum: that.pagenum,
        loginName:that.loginName,
        username:that.userName
      }
      that.searchData = JSON.parse(JSON.stringify(params))
      that.openFullScreen()
      uerList(params).then((res) => {
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
    // 添加
    add() {
      this.row = null
      this.isEdit = false
      this.unitManageDialogVisible = true
    },
    // 编辑
    edit(row) {
      this.row = row
      this.isEdit = true
      this.unitManageDialogVisible = true
    },
    // 删除
    del(row) {
      this.$confirm('确定删除该账户？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        delUser({
          id: row.id,
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
      this.row=null;
      this.unitManageDialogVisible = false
      // this.isEdit = false
    },
  },
  mounted() {
    this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) == 4
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
