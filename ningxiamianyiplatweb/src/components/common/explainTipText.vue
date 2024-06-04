<template>
  <div class="explain-tip-text">
    <div v-if="tipText">{{ tipText }}</div>
    <div v-if="secTime > 0">
      <ss-icon iconType="tshi" style="margin-right:4px;" />
      <span>友情提示：本次查询所花费时间大约是 {{ secTime }} 秒。</span>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  showSecTimeFlag: {
    type: Boolean,
    required: false,
    default: false
  },
  secTime: {
    type: Number,
    required: false,
    default: 0
  },
  tipText: {
    type: String,
    required: false,
    default: ''
  }
})
</script>

<style scoped lang="scss">
.explain-tip-text {
  display: inline-block;
  padding-left: 20px;
  font-size: 12px;
  color: #206CDC;

  div {
    display: flex;
    align-items: flex-end;
    height: 26px;
  }
}
</style>
