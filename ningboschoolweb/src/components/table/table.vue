<!--
 * @Description: 标准表格
-->
<template>
  <div>
    <el-table
      ref="itsmDataTable"
      class="hideFixedLine"
      :border="true"
      :data="data"
      :stripe="stripe"
      :style="'width: ' + width"
      :row-class-name="tableRowClassName"
      :max-height="height"
      empty-text="暂无数据"
      :header-cell-style="headerCellStyle"
      :cell-style="cellStyle"
      :span-method="arraySpanMethod"
      :header-cell-class-name="headerCellClassName"
      @selection-change="handleSelectionChange"
      @cell-click="handleCellClick"
      :row-key="getRowKeys"
    >
      <slot></slot>
    </el-table>
    <ss-pagination @goPage="goPage" :pagenum="pagenum" :total="total" />
  </div>

</template>

<script>
export default {
  name: 'tableContainer',
  data () {
    return {
      height: null,
      width: '100%'
    }
  },
  props: {
    data: {
      type: Array,
      required: true
    },
    rowClassName: {
      required: false
    },
    /* stripe为true代表隔行变色 */
    stripe: {
      type: Boolean,
      required: false,
      defalut: false
    },
    changeRowBg: {
      type: Boolean,
      required: false,
      defalut: false
    },
    // 修改行背景颜色标识列属性
    changeRowAttr: {
      type: String,
      required: false,
      defalut: ''
    },
    // 背景颜色列表
    rowBgList: {
      type: Array,
      required: false,
      defalut: () => {
        return [
          {
            text: '校医录入',
            style: { background: '#E8FFE8' },
            stuSourceid: '2',
            className: 'row-white'
          },
          {
            text: 'excel导入',
            style: { background: '#E9FFFF' },
            stuSourceid: '1',
            className: 'row-green'
          },
          {
            text: '信息系统提供',
            style: { background: '#FFFDD9' },
            stuSourceid: '0',
            className: 'row-yellow'
          }
        ]
      }
    },
    transfer: {
      type: Boolean,
      required: false,
      defalut: false
    },
    maxHeight: {
      type: Number,
      required: false
    },
    headerCellStyle: {
      type: Object,
      default: () => {
        return {}
      }
    },
    headerCellClassName: {
      type: Function,
      default: function () {}
    },
    cellStyle: {
      type: Object,
      default: () => {
        return {}
      }
    },
    arraySpanMethod: {
      type: Function,
      default: function () {}
    },
    // page相关
    pagenum: {
      type: Number,
      required: false,
      default: 1
    },
    total: {
      type: Number,
      required: false,
      default: 0
    }
  },
  methods: {
    handleCellClick (row, column, cell, event) {
      this.$emit('handleCellClick', row, column, cell, event)
    },
    handleSelectionChange (val) {
      this.$emit('handleSelectionChange', val)
    },
    setWidth () {
      let that = this
      that.width = '100%'
      if (that.$parent.$refs.conditionPage) {
        // 表格上方查询条件的宽度
        that.width = that.$parent.$refs.conditionPage.$el.clientWidth + 'px'
      }
    },
    setWidth2 () {
      let that = this
      that.width = '100%'
      if (that.$parent.$refs.conditionPage) {
        // 表格上方查询条件的宽度
        that.width =
          that.$parent.$refs.conditionPage.$el.clientWidth - 30 + 'px'
      }
    },
    setHeight () {
      let that = this
      if (that.maxHeight && that.maxHeight > 0) {
        that.height = that.maxHeight
      } else {
        let top = this.$util.getAbsoluteTop(that.$el)
        if (!top) {
          that.height = null
          return
        }
        let remarkHeight = 0
        if (that.$parent.$refs.remarkPage) {
          // 表格下方备注区域的高度
          remarkHeight = that.$parent.$refs.remarkPage.clientHeight
        }
        that.height =
          window.winHeight - top - 15 / window.scale - remarkHeight - 86 // 调整翻页条位置后，需减掉翻页条高度36px
        // console.log(`setHeight---top:${top};height:${that.height}`)
      }
    },
    tableRowClassName ({ row }) {
      let that = this
      // 是否修改背景颜色
      if (that.changeRowBg && that.changeRowAttr) {
        // 根据标识属性值获取颜色
        let rowBg = that.rowBgList.find(
          (i) => i[that.changeRowAttr] == row[that.changeRowAttr]
        )
        if (rowBg) {
          return rowBg.className
        }
      }
      if (that.changeRowBg && row.fState == '9') {
        return 'row-grey'
      }
    },
    goPage (pagenum) {
      this.$emit('goPage', pagenum)
    },
    // 换页时不清空之前页选中项
    getRowKeys (row) {
      return row.id || row.rowkey
    },
  },
  mounted () {
    this.setWidth()
    this.setScrollBar()
  },
  updated () {
    this.setHeight()
    this.setScrollBar()
  }
}
</script>
<style>
.pointer {
  cursor: pointer;
}
/* 解决固定列显示不完整问题 */
.el-table__fixed {
  height: 100% !important;
  /* bottom: 15px !important; */
}
.el-table__fixed-right {
  height: 100% !important;
}
/*在册受种者：颜色标注蓝色/白色；单纯迁出受种者：颜色标注绿色；死亡受种者：颜色标注黄色。*/
.row-white {
  background-color: #E8FFE8 !important;
}
.row-blue {
  background-color: #ccffff !important;
}

.row-green {
  background-color: #e8ffe8 !important;
}
.row-grey{
  color:#ccc;
}
.row-yellow {
  background-color: #f7ffff !important;
}
/*紫色*/
.row-purple {
  background-color: #cfb0dc !important;
}
.row-red {
  background-color: red !important;
}
.hideFixedLine .el-table__fixed-right::before,
.hideFixedLine .el-table__fixed::before {
  opacity: 0;
}
</style>
