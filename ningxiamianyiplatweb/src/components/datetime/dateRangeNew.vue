<!-- daterange  当前月格式，如['2011-11-11','2011-11-11'] 
    rangeName  选择范围内时间  weeks：一周  month:当前月  year:当前年

-->
<template>
  <div>
    <el-date-picker v-model="daterange" type="daterange" range-separator="至" :format="valueFormat"
      :value-format="valueFormat" :clearable="clearable" :disabled="disabledFlag" :disabled-date="disabledDate" clearable
      :unlink-panels="unlinkPanels" @change="$emit('update:defaultValue', daterange)" @calendar-change="calendarChange"
      @visible-change="visibleChange" :start-placeholder="startPlaceholder" :end-placeholder="endPlaceholder" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import moment from "moment";
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const startDate = globalProxy.$dateUtil.nDaysAgoTime(7);
const endDate = globalProxy.$dateUtil.currentTime();
const daterange = ref([startDate, endDate])

const emits = defineEmits(['update:defaultValue'])
const props = defineProps({
  defaultValue: {
    required: false,
    default: []
  },
  startPlaceholder: {
    type: String,
    default: '起始日期'
  },
  endPlaceholder: {
    type: String,
    default: '截止日期'
  },
  disabledFlag: {
    type: Boolean,
    default: false
  },
  clearable: {
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
    default: 'YYYY-MM-DD'
  },
  disabledDate: {
    type: Function,
    default: (time) => {
      return time.getTime() > Date.now()
    }
  },
  unlinkPanels: {
    type: Boolean,
    default: true
  },
  rangeName: {
    type: String,
    default: ''
  }
})
watch(() => props.defaultValue, (val) => {
  if (val) {
    daterange.value = val
  }
})
const startVal = ref(null);
const calendarChange = (e) => {
  if (e[0] && !e[1]) {
    startVal.value = e[0];
  }
};

const visibleChange = (e) => {
  if (e) {
    startVal.value = null;
  }
};

// 只能选择7天内 只能选择当前月  只能选择当前年
const disabledDate = (time) => {
  let temptime = globalProxy.$dateUtil.toDateTimeFormat(time, 'YYYY-MM-DD');
  if (props.rangeName == 'weeks') {
    let startTempVal = globalProxy.$dateUtil.toDateTimeFormat(startVal.value, 'YYYY-MM-DD')
    let beforeVal = moment(startTempVal).subtract(7, "days").format("YYYY-MM-DD");
    let endVal = moment(startTempVal).add(7, "days").format("YYYY-MM-DD");
    if ((moment(temptime).isBefore(beforeVal) || moment(temptime).isAfter(endVal)) || time.getTime() > Date.now()) return true;
    return false;
  }
  if (props.rangeName == 'month') {
    let startTempVal = globalProxy.$dateUtil.toDateTimeFormat(startVal.value, 'YYYY-MM');
    let beforeVal = moment(startTempVal).startOf("month").format("YYYY-MM-DD");
    let endVal = moment(startTempVal).endOf("month").format("YYYY-MM-DD");
    if ((moment(temptime).isBefore(beforeVal) || moment(temptime).isAfter(endVal)) || time.getTime() > Date.now()) return true;
    return false;
  }
  if (props.rangeName == 'year') {
    let startTempVal = globalProxy.$dateUtil.toDateTimeFormat(startVal.value, 'YYYY');
    let beforeVal = moment(startTempVal).startOf("year").format("YYYY-MM-DD");
    let endVal = moment(startTempVal).endOf("year").format("YYYY-MM-DD");
    if ((moment(temptime).isBefore(beforeVal) || moment(temptime).isAfter(endVal)) || time.getTime() > Date.now()) return true;
    return false;
  }
  return time.getTime() > Date.now();
}
onMounted(() => {
  if (props.disabledFlag) {
    daterange.value = [];
  }
  if (props.defaultValue && props.defaultValue.length) {
    daterange.value = props.defaultValue;
  }
})

</script>
<style scoped lang='scss'></style>