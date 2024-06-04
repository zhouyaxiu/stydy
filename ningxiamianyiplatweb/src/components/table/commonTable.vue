<template>
  <div class="table-container">
    <el-table :data="data" :max-height="height" border header-cell-class-name="headerClass" cell-class-name="rowClass">
      <slot></slot>
    </el-table>
  </div>
</template>

<script setup>
import { ref, watch, } from 'vue';

const props = defineProps({
  data: {
    type: Array,
    default: () => {
      return []
    }
  },
  maxHeight: {
    type: Number,
    required: false
  }
})
watch(() => props.data, (val) => {
  if (val.length) {
    setheight()
  }
})
const height = ref(150)
const setheight = () => {
  if (props.maxHeight) {
    height.value = props.maxHeight
    return
  }
  setTimeout(() => {
    let documentHeight = document.body.clientHeight;
    let headerHeight = document.getElementsByClassName('header')[0].clientHeight
    let conditionHeight = document.getElementsByClassName('condition-area')[0].clientHeight
    let paginationHeight = document.getElementsByClassName('ss-pagination')[0]?.clientHeight || (-18)
    let remarkHeight = document.getElementsByClassName('remark-text')[0]?.clientHeight || 0
    let opAreaHeight = document.getElementsByClassName('op-area')[0]?.clientHeight || 0
    height.value = documentHeight - headerHeight - conditionHeight - paginationHeight - remarkHeight - opAreaHeight - 100
  }, 100);
}
defineExpose({ setheight })
</script>

<style lang='scss'>
.headerClass,
.rowClass {
  .cell {
    text-align: center;
  }
}
</style>