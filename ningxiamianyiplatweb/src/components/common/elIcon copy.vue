<!-- 自定义图标组件
  /** 父组件传参
    * v-elIcon {String}  来自element plus 图标库
    * @click {Function}  子组件回调父组件方法
 */ -->
<template>
  {{ props.elIcon }}===
  <el-icon v-if="props.elIcon">
    <component :is="props.elIcon" @click="clickHandle" :key="props.elIcon"> </component>
  </el-icon>
</template>
<script setup>
const iconStyle = { width: '1em', height: '1em', marginRight: '8px' }
//子组件回调父组件方法
const emits = defineEmits(['icon-click'])
const clickHandle = (type) => {
  emits('icon-click', type)
};
//父组件传参--按钮类型
const props = defineProps({
  elIcon: {
    type: String,
    default: 'insert',
    required: true
  }
})
</script>

<style scoped lang="scss">
.def-icon-cls {
  width: 1em;
  height: 1em;
  margin-right: 8px;
}
</style>
