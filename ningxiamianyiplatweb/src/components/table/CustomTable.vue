<!-- 自定义标准表格
  /** 父组件传参
   
 */ -->
<template>
  <div class="table-container">
    <ss-pagination v-show="total" :total="total" @pageChange="pageChangeFun" :pageIndex="pageNum" />
    <el-table ref="normalTable" border :data="tableData" :maxHeight="height" :style="tableStyle" empty-text="暂无数据"
      highlight-current-row stripe header-cell-class-name="headerClass" :span-method="spanMethod"
      :header-cell-style="{ 'background-color': '#EEF4FF', height: '36px' }" cell-class-name="rowClass"
      :row-class-name="tableRowClassName">
      <template v-for="(item, idx) in columnList">
        <el-table-column type="selection" width="55" v-if="item.type == 'selection'" align="center" />
        <el-table-column :key="idx" type="index" label="序号" v-if="item.type === 'index'" width="80" :fixed="item.fixed"
          align="center">
          <template #default="scope">
            {{ 20 * (pageNum - 1) + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column v-else-if="!item.type && item.hasChildrenTitle" :label="item.label"
          :show-overflow-tooltip="item.overflowTip ? item.overflowTip : false" align="center"
          :fixed="item.fixed ? item.fixed : false">
          <el-table-column v-for="(itm, inx) in item.list" :key="inx" :prop="itm.prop" :label="itm.label"
            :min-width="itm.minWidth || 80" align="center"
            :show-overflow-tooltip="itm.overflowTip ? itm.overflowTip : false">
            <template #default="scope" v-if="itm.type == 'clickable'">
              <span v-if="scope.row[itm.prop] == 0">{{ scope.row[itm.prop] }}</span>
              <span v-else @click="openDetailDialog(scope.row, itm.prop)" class="pointer blue">{{ scope.row[itm.prop]
                }}</span>
            </template>
            <template #default="scope" v-else>
              <span>{{ scope.row[itm.prop] }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column v-else-if="!item.type && !item.hasChildrenTitle" :label="item.label" :prop="item.prop"
          align="center" :show-overflow-tooltip="item.overflowTip ? item.overflowTip : false"
          :fixed="item.fixed ? item.fixed : false" :min-width="item.minWidth || 80"
          :formatter="item.formatter"></el-table-column>
        <el-table-column align="center" v-else-if="item.type == 'operation'" label="操作"
          :min-width="item.minWidth || 120">
          <template #default="scope">
            <el-button v-for="btn in item.btns" :key="btn" @click="btnHandle(scope.row, btn.event)" size="small"
              :type="btn.type ? btn.type : 'primary'" :disabled="getBtnEditable(scope.row, btn)">
              {{ btn.text }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" v-else-if="item.type == 'clickable' && !item.hasChildrenTitle"
          :label="item.label" min-width="120">
          <template #default="scope">
            <span v-if="scope.row[item.prop] == 0">{{ scope.row[item.prop] }}</span>
            <span v-else @click="openDetailDialog(scope.row, item.prop)" class="pointer blue">{{ scope.row[item.prop]
              }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" v-else-if="item.type == 'judge'" :label="item.label" :prop="item.prop"
          min-width="120"></el-table-column>
        <!-- 尾部具名插槽，插入自定义列 -->
        <slot name="slotSufixCols"></slot>
      </template>
    </el-table>
    <ss-remark-text :remarkText="remarkText" v-if="remarkText"></ss-remark-text>
  </div>
</template>
<script setup>
import ssPagination from '@/components/table/Pagination.vue'
import ssRemarkText from '@/components/common/RemarkText.vue'

const normalTable = ref()
const tableStyle = ref({ width: '100%' })
const pageNum = ref(1)

//子组件回调父组件方法
const emits = defineEmits(['tableEvents', 'openDetailDialog', 'rowOperateFun', 'pageChangeFun'])
const callback = (type, data) => {
  let params = {
    eventType: type,
    data: data
  }
  emits('tableEvents', params)
};
//页面数据跳转
const pageChangeFun = (pageIndex) => {
  pageNum.value = pageIndex
  emits('goPage', pageIndex)
  // getData()
}
//行按钮是否启用判断
const getBtnEditable = (row, btn) => {
  return row.unEditable ? row.unEditable : false
}
//行按钮点击事件
const btnHandle = (row, btn) => {
  emits('rowOperateFun', row, btn)
}
//父组件传参--按钮类型
const props = defineProps({
  maxHeight: {
    type: Number,
    default: 150
  },
  columnList: {
    type: Array,
    default: () => {
      return []
    }
  },
  tableData: {
    type: Array,
    default: () => {
      return []
    }
  },
  total: {
    type: Number,
    default: 0
  },
  // 是否有备注
  remarkText: {
    type: String,
    default: ''
  },
  // 是否修改背景颜色
  changeRowBg: {
    type: Boolean,
    default: false
  },
  spanMethod: {
    type: Function,
  }
})
watch(() => props.tableData, (val) => {
  if (val.length) {
    setHeight()
  }
})

const height = ref(150)
const setHeight = () => {
  let documentHeight = document.body.clientHeight;
  let headerHeight = document.getElementsByClassName('header')[0].clientHeight
  let conditionHeight = document.getElementsByClassName('condition-area')[0].clientHeight
  let paginationHeight = document.getElementsByClassName('ss-pagination')[0].clientHeight
  let remarkHeight = document.getElementsByClassName('remark-text')[0]?.clientHeight || 0
  height.value = documentHeight - headerHeight - conditionHeight - paginationHeight - remarkHeight - 120
}
const setWidth = () => {
  tableStyle.value.width = '100%';
}
// 点击查看详情
const openDetailDialog = (row, prop) => {
  emits('openDetailDialog', row, prop);
}
// 背景色
const tableRowClassName = ({ row }) => {
  if (props.changeRowBg) {
    if (row.childStatus === '0' || row.childstatus === '0') {
      return 'row-blue'
    }
    if (row.childStatus === '1' || row.childstatus === '1') {
      return 'row-green'
    }
    // if (row.childStatus === '8' || row.childstatus === '8') {
    //   return 'row-yellow'
    // }
  }
}

defineExpose({ setHeight, setWidth })
</script>
<style scoped lang="scss">
.headerClass .cell {
  text-align: center;
  font-size: 15px;
  color: #223B80;
}

.rowClass {
  .cell {
    text-align: center;
    font-size: 14px;
    color: #333;
  }
}

.blue {
  color: #2c6aff
}
</style>
<style>
.row-blue {
  background: #CCFFFF !important;
}

.row-green {
  background: #E0FFDF !important;
}

.el-table__empty-block {
  margin-bottom: -1px;
}

.table-container .el-table--border::after {
  right: 1px;
}
</style>
