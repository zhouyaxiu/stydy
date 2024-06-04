<template>
  <!-- 查询提示信息
    * v-secTime {Number}  查询花费时间
    * v-tipText {String}  首部固定显示的提示
    * statisTics {Boolean} 是否显示统计截至
-->
  <div class="mt10">
    <div style="color: #717e89;font-size:14px;margin-bottom: 10px;" v-if="tipTextData.length">
      <div v-for="item in tipTextData" :key="item">{{ item }}</div>
    </div>
    <div class="flex">
      <slot></slot>
      <span v-if="statisTics" class="ml20" style="color:#627FAB">
        统计截止到：{{ endDate }}
      </span>
      <span style="color: #206CDC;font-size:12px; margin-left: 10px;display: flex;align-items: center;"
        v-if="secTime > 0">
        <ss-icon iconType="tshi" style="color:#206CDC;margin-right:4px;" />
        <span>友情提示：本次查询所花费时间大约是 {{ secTime }} 秒。</span>
      </span>
    </div>
  </div>
</template>

<script setup>
//父组件传参
const props = defineProps({
  secTime: {
    type: Number,
    required: false,
    default: 0
  },
  tipText: {
    type: String,
    required: false,
    default: ''
  },
  statisTics: {
    type: Boolean,
    default: false
  },
})
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const endDate = globalProxy.$dateUtil.dayAddNum(1);
const tipTextData = computed(() => {
  let arr = props.tipText.indexOf("<br>") > -1 ? props.tipText.split("<br>") : [props.tipText];
  return arr;
})
</script>

<style scoped>
.explain-tip-text {
  width: 100%;
  display: flex;
  line-height: 32px;
  padding-left: 20px;
  font-size: 12px;
  background: #fff;
  color: #000;
  user-select: none;
}
</style>
