<template>
  <div>
    
    <el-table row-key="cid" border :data="tableData" :span-method="mergeCellsMethod" highlight-current-row
      @cell-mouse-enter="cellMouseEnter" @cell-mouse-leave="cellMouseLeave" :row-class-name="tableRowClassName">
      <el-table-column prop="country" label="国家"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="work" label="工作"></el-table-column>
      <el-table-column prop="name" label="姓名"></el-table-column>
      <el-table-column prop="age" label="年龄"></el-table-column>
      <el-table-column prop="" label="">
        <template #default="scope">
          <span @click="showDetail(scope.row)">详情</span>
        </template>
      </el-table-column>
    </el-table>
    ksskk
    <flipPanel :is-back="isBackSide">
      <template #default>
        <div class="front-wrap" @click="isBackSide = !isBackSide">正面内容</div>
      </template>
      <template #back>
        <div class="back-wrap" @click="isBackSide = !isBackSide">反面内容</div>
      </template>
    </flipPanel>

    <detail v-if="editshow" :pwd="edtailencodedCode" :dialogVisible="editshow" @closeEvent="hideEdit"></detail>
  </div>
</template>

<script>
import detail from "./detail.vue"
import flipPanel from "./flipPanel.vue"
export default {
  components:{
    detail,
    flipPanel
  },
  data() {
    return {
      tableData: [
        { id: 1, country: '美国', address: '洛杉矶', work: '销售', name: '李四', age: 18, cid: 1 },
        { id: 1, country: '美国', address: '洛杉矶', work: '销售', name: '王五', age: 18, cid: 2 },
        { id: 2, country: '英国', address: '伦敦', work: '收银', name: '赵六', age: 18, cid: 3 }
      ],
      editshow:false,
      edtailencodedCode:"",
      isBackSide:true
    }
  },
  methods: {
    mergeCellsMethod({ row, column, rowIndex }) {
      // 根据行id进行合并，这样不会导致上下数据相同但id不同时会合并错误的问题
      switch (column.property) {
        case 'country':
          return this.setTableRowCol(row, rowIndex, ['id', 'country'])
        case 'address':
          return this.setTableRowCol(row, rowIndex, ['id', 'address'])
        case 'work':
          return this.setTableRowCol(row, rowIndex, ['id', 'work'])
        default:
          break
      }
    },
    setTableRowCol(row, rowIndex, keys) {
      let firstIndex = 0
      let rowspan = 1
      let colspan = 1
      // 找到需要合并的行的下标
      firstIndex = this.tableData.findIndex((item) => {
        return this.filterSameKeys(item, row, keys)
      })
      if (rowIndex === firstIndex) {
        // 需要合并的行数
        rowspan = this.tableData.filter((item) => {
          return this.filterSameKeys(item, row, keys)
        }).length
        colspan = 1
      } else {
        // 被合并的行需要设置成0
        rowspan = 0
        colspan = 0
      }
      return {
        rowspan,
        colspan
      }
    },
    // 根据keys数组所有字段去做合并
    filterSameKeys(item, row, keys) {
      let flag = true
      keys.forEach((key) => {
        flag = flag && item[key] === row[key]
      })
      return flag
    },
    // 鼠标移入
    cellMouseEnter(row) {
      //currentIndex在data中定义，我这里就不写了
      this.currentIndex = row.id
    },
    // 鼠标移出
    cellMouseLeave() {
      this.currentIndex = ''
    },
    // 设置行class
    tableRowClassName({ row }) {
      let flag = row.id === this.currentIndex
      return flag ? 'table-hover-row' : ''
    },
    showDetail(data){
      this.detailencodedCode=data.encodedCode;
      this.editshow=true;
    },
    // 关闭
    hideEdit(){
      this.editshow=false;
    }
  }
}
</script>
<style>
.el-table .table-hover-row {
  background: #e0f3ff !important;
}

.el-table tbody tr:hover>td {
  background-color: #e0f3ff !important;
}
</style>
