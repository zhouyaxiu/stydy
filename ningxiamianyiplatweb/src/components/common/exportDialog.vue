<template>
  <!-- 导出分页
  /** 父组件传参
    * v-dialogVisible {Boolean}  弹窗是否显示
    * v-total {Number}  记录总数
    * v-typeStr {String}  导出类型提示
    * @handleClose {Function}  子组件回调父组件方法---关闭弹窗
    * @callback {Function}  子组件回调父组件方法---导出
 */ -->
  <el-dialog title="导出" v-model="dialogVisible" width="40%" :before-close="handleClose" :close-on-click-modal="false"
    :close-on-press-escape="false" :append-to-body="true">
    <div class="scroll-item">
      <template v-for="item in pageNum" :key="item">
        <el-row class="export-row">
          <span class="pointer" @click="downloadFun(item)">下 载</span>
          <span>
            导出{{ typeStr }}第 {{ (item - 1) * 5000 + 1 }} 到
            {{ item * 5000 > total ? item * 5000 : total }} 条
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

<script setup>
const props = defineProps({
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
})
const pageNum = ref(0)
const endRow = ref(false)
watch(() => props.dialogVisible, (val) => {
  if (val) {
    pageNum.value = parseInt(props.total / 5000)
    endRow.value = pageNum.value * 5000 < props.total
  } else {
    pageNum.value = 0
  }
})
const emits = defineEmits(['handleClose', 'callback'])
const handleClose = () => {
  emits('handleClose')
}
const downloadFun = (pageIndex) => {
  emits('callback', pageIndex)
}
</script>

<style  scoped lang="scss">
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
