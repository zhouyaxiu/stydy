<template>
  <!-- 批量扫码 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <el-form class="mt_-20 batch" label-width="200px" label-position="right" :model="unitManageForm" ref="unitManageForm" :inline="true">
        <ss-class-select ref="selectClass" :classMultiple="false" :defaultValue="true" :defaultLunci="true" :disabledYearAndLunci="true" :disabled="disabled" />
      </el-form>
      <div class="condition-row ml_250">
        <ss-custom-button v-if="!isScan" @click="scan" :type="$consts.CUSTOMBUTTONTYPES.scan" />
        <ss-custom-button v-if="isScan" :type="$consts.CUSTOMBUTTONTYPES.disscan"/>
        <ss-custom-button v-if="!scanFinish" :type="$consts.CUSTOMBUTTONTYPES.dissubmit" class="ml_20" />
        <ss-custom-button v-if="scanFinish" @click="onSubmit" :type="$consts.CUSTOMBUTTONTYPES.submit" class="ml_20" />
        <div class="tips ml_20">
          <ss-icon iconType="redtips"></ss-icon>
          <span>{{tipText}}</span>
        </div>
      </div>
    </ss-condition-area>
    <div class="main-content">
      <el-table border :data="dataList" highlight-current-row @cell-click="cellClick" class="tableDiv">
        <el-table-column
          v-for="(item, index) in tableColumn"
          align="center"
          :key="index"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
        >
          <template slot-scope="scope">
            <el-input
              v-if="item.edit && isScan"
              size="small"
              ref="tableInput"
              :autofocus="true"
              v-model="scope.row[item.prop]"
              @blur="blur"
              @change="handleEdit(item.prop, scope.$index, scope.row)"
              width="240px"
            ></el-input>
            <div>{{ scope.row[item.prop] }}</div>
          </template>
        </el-table-column>
      </el-table>
      <ss-pagination @goPage="goPage" :pagenum="pagenum" :total="total" :pageSize="pagesize"/>
    </div>
  </div>
</template>
<script>
import commonOptions from '@/mixin/commonOptions'
import { batchScan } from '@/utils/axios/apis/collect'
export default {
  mixins: [commonOptions],
  name: 'batchScan',
  data () {
    return {
      isScan: false, // 开始扫码
      scanFinish: false, // 扫码结束，可以提交
      tableColumn: [
        { label: '批量扫入的电子条码', prop: 'imuno', edit: true, width: 400 },
        { label: '是否成功获取受种者信息', prop: 'queryInfo' },
        { label: '受种者预防接种档案编号', prop: 'fchildno' },
        { label: '受种者姓名', prop: 'name' },
        { label: '受种者出生日期', prop: 'birth' }
      ],
      dataList: [],
      tableData: [],
      pagenum: 1,
      total: 0,
      maxLength: 0,
      pagesize: 8,
      data: {},
      unitManageForm: {},
      disabled: false,
      tipText: '批量扫码未获取成功的受种者信息请使用信息扫码单独处理'
    }
  },
  methods: {
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    },
    // 开始扫码
    scan () {
      this.tableData = []
      this.unitManageForm = this.$refs.selectClass.unitManageForm
      if (!this.unitManageForm.stuclayear) {
        this.$util.errorMsg('请选择年份！')
        return
      }
      if (!this.unitManageForm.lunci) {
        this.$util.errorMsg('请选择轮次！')
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
      this.maxLength = Number(this.$refs.selectClass.classesList.filter((i) => {
        if (i.classId === this.unitManageForm.classId) { return i }
      })[0].maxClaTotal) || 0
      this.$confirm('最多可添加人数：' + this.maxLength, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(() => {
        if (this.maxLength <= 0) {
          this.$message({
            type: 'info',
            message: '人数已满'
          })
          return
        }
        this.isScan = true
        this.disabled = true
        this.tableData.push({ key: 1 })
        this.pagenum = 1
        this.total = this.tableData.length
        this.dataList = this.tableData
        this.$nextTick(() => {
          document.getElementsByClassName('el-table__cell')[6].classList.add('current-cell')
          this.$refs['tableInput'][0].$refs.input.focus()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        })
      })
    },
    // 提交
    onSubmit () {
      let data = {
        classId: this.unitManageForm.classId,
        imunos: this.tableData.map((elem) => { if (elem.imuno) { return elem.imuno } }).join(','),
        schStationCode: this.$store.state.schStationcode,
        schName: this.$store.state.schName
      }
      this.openFullScreen('提交中...')
      batchScan(data).then(res => {
        this.tableData = res.data
        this.pagenum = 1
        this.secTime = res.sec
        this.total = res.data.length
        this.goPage(this.pagenum)
        this.isScan = false
        this.scanFinish = false
        this.disabled = false
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    // 编辑行数据
    handleEdit (prop, index, row) {
      this.removeOtherClass()
      let i = index === 7 ? 6 : (index + 2) * 5 + 1
      let j = index === 7 ? 0 : index + 1
      if (this.tableData.length === this.maxLength || this.total === this.maxLength) {
        return
      }
      this.tableData.push({ key: 1 })
      if (this.tableData.length > this.pagesize) {
        this.total = this.tableData.length
        this.pagenum = (this.total % this.pagesize) ? (parseInt(this.total / this.pagesize) + 1) : (this.total / this.pagesize)
      }
      this.goPage(this.pagenum)
      setTimeout(() => {
        document.getElementsByClassName('el-table__cell')[i].classList.add('current-cell')
        this.$refs['tableInput'][j].$refs.input.focus()
      }, 100)
    },
    // 行高亮切换
    cellClick (row, column, cell, event) {
      this.removeOtherClass()
      cell.classList.add('current-cell')
    },
    removeOtherClass () {
      for (let i = 0; i < document.getElementsByClassName('current-cell').length; i++) {
        document.getElementsByClassName('current-cell')[i].classList.remove('current-cell')
      }
    },
    // 行失焦时可以提交
    blur () {
      this.scanFinish = true
    },
    // 分页
    goPage (pagenum) {
      let that = this
      that.dataList = that.tableData.slice((pagenum - 1) * that.pagesize, pagenum * that.pagesize)
    }
  }
}
</script>
<style scoped>
.w_320{
  width: 320px;
}
.tableDiv .el-input {
  display: none;
}
.current-cell .el-input {
  display: block;
}
.current-cell .el-input + div {
  display: none;
}
.tips{
  color: #FF2121;
}
</style>
<style lang="less">
.tableDiv .el-table__cell{
  height: 60px !important;
  .el-input__inner{
    width: 240px;
  }
}
.batch .el-form-item{
  margin-bottom: 10px;
}
</style>
