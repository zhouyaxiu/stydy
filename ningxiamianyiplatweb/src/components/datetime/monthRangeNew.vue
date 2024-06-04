<!-- monthrange  当前月格式，如['2011-11','2011-11'] 
    uptoDate  截至开始日期  如 2015-01
-->
<template>
  <div>
    <el-date-picker v-model="monthrange" type="monthrange" range-separator="至" format="YYYY-MM" unlink-panels
      :value-format="valueFormat" :clearable="clearable" :readonly="disabledFlag" :disabled-date="disabledDate"
      @change="$emit('update:defaultValue', monthrange)" :disabled="disabledFlag" />
  </div>
</template>

<script setup>
import moment from "moment";
import { ref, watch, onMounted } from 'vue';
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const currentDate = globalProxy.$dateUtil.currentMonth();
const monthrange = ref([currentDate, currentDate]);

watch(() => props.defaultValue, (val) => {
  if (val) {
    monthrange.value = val
  }
}, { immediate: true, deep: true })
const emits = defineEmits(['update:defaultValue'])
const props = defineProps({
  defaultValue: {
  },
  clearable: {
    type: Boolean,
    required: false,
    default: false
  },
  disabledFlag: {
    type: Boolean,
    required: false,
    default: false
  },
  dateType: {
    type: String,
    default: 'date'
  },
  valueFormat: {
    type: String,
    default: 'YYYY-MM'
  },
  disabledDate: {
    type: Function,
    default: (time) => {
      return time.getTime() > Date.now()
    }
  },
  uptoDate: {
    type: String,
    default: ''
  }
})
const disabledDate = (time) => {
  let temptime = globalProxy.$dateUtil.toDateTimeFormat(time, 'YYYY-MM');
  return time.getTime() > Date.now() || moment(temptime).isBefore(props.uptoDate || globalProxy.$consts.ENDBEGINMONTH);
}
onMounted(() => {
  if (props.disabledFlag) {
    return monthrange.value = [];
  }
  if (props.defaultValue && props.defaultValue.length) {
    monthrange.value = props.defaultValue;
  }
})

</script>
<style scoped lang='scss'></style>