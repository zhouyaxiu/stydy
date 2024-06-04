<template>
  <!-- 导出分页
  /** 父组件传参
    * v-dialogVisible {Boolean}  弹窗是否显示
    * v-total {Number}  记录总数
    * v-typeStr {String}  导出类型提示
    * @handleClose {Function}  子组件回调父组件方法---关闭弹窗
    * @callback {Function}  子组件回调父组件方法---导出
 */ -->
  <el-dialog
    title="导出"
    :visible.sync="dialogVisible"
    width="40%"
    :before-close="handleClose"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :append-to-body="true"
  >
    <div class="scroll-item">
      <template v-for="item in pageNum">
        <el-row :key="item" class="export-row">
          <span class="pointer" @click="downloadFun(item)">下 载</span>
          <span>
            导出{{ typeStr }}第 {{ (item - 1) * 5000 + 1 }} 到
            {{ item * 5000 &lt; total ? item * 5000 : total }} 条
          </span>
        </el-row>
      </template>
      <el-row v-if="endRow" class="export-row">
        <span class="pointer" @click="downloadFun(pageNum + 1)">下 载</span>
        <span>
          导出{{ typeStr }}第 {{ pageNum * 5000 + 1 }}到 {{ total }} 条
        </span>
      </el-row>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'exportDialog',
  props: {
    total: {
      type: Number,
      default: 0
    },
    dialogVisible: {
      type: Boolean,
      default: false
    },
    typeStr: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
      pageNum: 0,
      endRow: false
    }
  },
  watch: {
    dialogVisible (val) {
      if (val) {
        this.pageNum = parseInt(this.total / 5000)
        this.endRow = this.pageNum * 5000 < this.total
      } else {
        this.pageNum = 0
      }
    }
  },
  methods: {
    handleClose () {
      this.$emit('handleClose')
    },
    downloadFun (pageIndex) {
      this.$emit('callback', pageIndex)
    }
  },
  mounted () {
    this.setScrollBar()
  },
  updated () {
    this.setScrollBar()
  }
}
</script>

<style  scoped lang="less">
.pointer {
  cursor: pointer;
  color: #1e6ce4 !important;
}
.scroll-item {
  height: 200px;
  max-height: 450px;
  overflow-y: auto;
  padding-bottom: 10px;
}
.export-row {
  padding: 10px 20px;
  span {
    padding-left: 20px;
  }
}
</style>
