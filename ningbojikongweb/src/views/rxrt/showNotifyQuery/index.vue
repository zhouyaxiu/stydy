<template>
  <!-- 通知管理 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <div class="label">通知日期：</div>
          <ss-date-range v-model="fillinDate" :startTimeOption="startTimeOption" :endTimeOption="endTimeOption" />
        </div>
        <div class="item mLeft">
          <div class="label">通知标题：</div>
          <el-input placeholder="请输入通知标题" clearable style="width:200px;" v-model="notifyTitle"></el-input>
        </div>
      </div>
      <div class="condition-row mt_10">
        <ss-custom-button @click="add()" :type="$consts.CUSTOMBUTTONTYPES.add" />
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" class="ml_10" />
        <ss-explain-tip-text :secTime="secTime" />
      </div>
    </ss-condition-area>
    <ss-table :data="dataList" ref="tabPage" class="infoTable" :pageSize="20" @goPage="search" :pagenum="pagenum" :total="total">
      <el-table-column prop="notifyTime" align="center" min-width="200" label="通知日期" />
      <el-table-column prop="notifyTitle" align="center" min-width="200" label="通知标题" />
      <el-table-column prop="notifyArea" align="center" min-width="200" label="通知范围" show-overflow-tooltip/>
      <el-table-column prop="updateTime" align="center" min-width="200" label="发布时间" />
      <el-table-column align="center" min-width="200" label="操作项">
        <template slot-scope="{ row }">
          <el-button @click.native="edit(row)" type="text" :disabled="Number(currentLevel)>Number(row.areaLevel)" class="table-btn">编辑</el-button>
          <el-button @click.native="del(row)" type="text" :disabled="Number(currentLevel)>Number(row.areaLevel)" class="table-btn">删除</el-button>
        </template>
      </el-table-column>
    </ss-table>
    <publishDialog :visible="unitManageDialogVisible" @fetchData="search" @close="close" :isEdit="isEdit" :curId="curId" />
  </div>
</template>
<script>
import { searchNotifyList, delNotify } from '@/utils/axios/apis/rxrt/showNotifyQuery'
import commonOptions from '@/mixin/commonOptions'
import publishDialog from './components/publishDialog.vue'
import { getLevel } from '@/utils/common/area.js'
export default {
  name: 'notify',
  components: { publishDialog },
  mixins: [commonOptions],
  data () {
    return {
      notifyTitle: '',
      fillinDate: [this.$util.getBeforeOneMonthsDateStr(), this.$dateutil.dayAddNum()],
      secTime: 0,
      curId: '0',
      pagenum: 1,
      startTimeOption: {
        disabledDate: time => {
          return (
            time.getTime() > new Date(this.$dateutil.dayAddNum()).getTime()
          )
        }
      },
      endTimeOption: {
        disabledDate: time => {
          return (
            time.getTime() > new Date(this.$dateutil.dayAddNum()).getTime()
          )
        }
      },
      cModId: this.$store.state.moduleId,
      isEdit: false,
      unitManageDialogVisible: false,
      total: 0,
      dataList: [],
      currentLevel: Number(getLevel(this.$store.state.userCode))
    }
  },
  methods: {
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    },
    search (pagenum) {
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      let fillinStartDate = that.fillinDate[0] || ''
      let fillinEndDate = that.fillinDate[1] || ''
      if (!fillinStartDate || fillinStartDate === '' || !fillinEndDate || fillinEndDate === '') {
        that.$util.errorMsg('通知日期为必填项，请填写完整再查询！')
        return false
      }
      let params = {
        writeStartDate: fillinStartDate,
        writeEndDate: fillinEndDate,
        notifyTitle: that.notifyTitle,
        pageNum: pagenum,
        userAreaId: that.$store.state.areaId
      }
      that.searchData = JSON.parse(JSON.stringify(params))
      that.openFullScreen()
      searchNotifyList(params).then((res) => {
        that.dataList = res.data.list
        that.secTime = res.sec
        that.total = res.data.total
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
    add () {
      this.unitManageDialogVisible = true
    },
    edit (row) {
      this.unitManageDialogVisible = true
      this.curId = row.id
      this.isEdit = true
    },
    del (row) {
      console.log(row.id, '00')
      this.$confirm('请确认是否删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        delNotify({
          id: row.id
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
    close () {
      this.unitManageDialogVisible = false
      this.isEdit = false
      this.curId = null
    }
  },
  mounted () {
    this.search()
  }
}
</script>
<style>
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
</style>
