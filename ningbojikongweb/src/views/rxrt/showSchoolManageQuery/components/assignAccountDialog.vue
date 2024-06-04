<!-- 账号分配弹框 -->
<template>
  <el-dialog title="账号分配" :visible.sync="visible" width="40%" :before-close="close" :close-on-click-modal="false"
    class="assignAccountDialog" :lock-scroll="true" :append-to-body="true">
    <el-row :gutter="20">
      <el-col :span="12">
        <span class="label-left">学校编码:</span>
        <el-input v-model="schCode" disabled style="width: 250px" maxlength="10"></el-input>
      </el-col>
      <el-col :span="12">
        <span class="label-left">学校名称:</span>
        <el-input v-model="schName" disabled style="width: 250px"></el-input>
      </el-col>
    </el-row>
    <div class="condition-row mt_20">
      <el-button size="small" type="primary" @click="addAccount">新增账号</el-button>
      <div class="ml20">备注：点击【新增账号】 或 【重置密码】，对应账号的密码默认为NBcyxt@123456</div>
    </div>
    <ss-table :data="dataList" ref="tabPage" class="assignTable" :maxHeight="200">
      <el-table-column type="index" label="序号" align="center" width="55">
      </el-table-column>
      <el-table-column prop="loginName" align="center" min-width="150" label="学校端登录账号" />
      <el-table-column align="center" min-width="150" label="操作">
        <template slot-scope="{ row }">
          <el-row>
            <el-button @click.native="resetPwd(row)" type="text" class="table-btn">重置密码
            </el-button>
          </el-row>
        </template>
      </el-table-column>
    </ss-table>

  </el-dialog>
</template>
<script>
import { schUserList, addSchUser, resetSchUser } from '@/utils/axios/apis/informationManage/schoolManage'
export default {
  name: 'assignAccountDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    curId: {
      type: [Number, String],
      default: null
    },
    schCode: {
      type: String,
      default: ''
    },
    schName: {
      type: String,
      default: ''
    },
    fullCode: {
      type: String,
      default: ''
    },
  },
  data() {
    return {
      dataList: [],
      total: 0,
      accountForm: {}
    }
  },
  watch: {
    visible(v) {
      if (v) {
        this.getUserList();
      }
    }
  },
  methods: {
    close() {
      this.$emit('closeDialog')
    },
    getUserList() {
      schUserList({ schId: this.curId }).then((res) => {
        console.log(res)
        if (res.data.length > 0) {
          this.dataList = res.data
        } else {
          this.dataList = []
        }
      })
    },
    // 新增账号
    addAccount() {
      this.$confirm('确定要给该学校新增一个账号？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        addSchUser({
          schId: this.curId,
          fullCode: this.fullCode,
        }).then((res) => {
          this.$message({
            type: 'success',
            message: '新增账号成功!'
          })
          this.getUserList()
        })
      }).catch(() => {
        this.closeFullScreen()
        this.$message({
          type: 'info',
          message: '已取消'
        })
      })
    },
    // 重置密码
    resetPwd(row) {
      if (row.id) {
        resetSchUser({ id: row.id }).then((res) => {
          if (res) {
            this.$message({
              type: 'success',
              message: '重置密码成功!'
            })
            this.getUserList()
          }
        })
      }
    }
  }
}

</script>
<style lang="less">
.assignAccountDialog {
  .el-dialog__body {
    margin: auto 40px;
  }

  .el-dialog :not(.el-transfer) .el-button--primary,
  .el-dialog :not(.el-transfer) .el-button--default {
    margin-left: 0px;
    font-size: 14px !important;
    height: 32px;
  }

  .assignTable .el-table__header-wrapper tr th:nth-last-child(2) {
    border-right: 0 !important;
  }
}
</style>
<style scoped lang="less">
.mt_20 {
  margin-top: 20px;
}

.assignAccountDialog {
  .condition-row {
    display: flex;
    line-height: 32px;
    font-size: 14px;
    color: #4B5C8C;
  }

  .assignTable {
    margin-top: 20px;
  }
}

.ml20 {
  margin-left: 20px;
}

.label-left {
  font-size: 14px;
  float: left;
  line-height: 32px;
  margin-right: 4px;
}
</style>