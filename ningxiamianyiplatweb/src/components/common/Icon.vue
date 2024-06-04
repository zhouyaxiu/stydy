<template>
  <el-icon v-if="props.iconType">
    <template #default>
      <div :class="'icon ss-icon-' + props.iconType"></div>
    </template>
  </el-icon>
</template>

<script setup>
//父组件传参--按钮类型
const props = defineProps({
  iconType: {
    type: String,
    required: false
  },
  size: {
    type: String,
    required: false,
    default: "14px",
  },
})
</script>

<style scoped>
.icon {
  position: relative;
  vertical-align: middle;
  display: inline-block;
  background-size: contain;
  outline: none;
  background-repeat: no-repeat;
}
</style>
