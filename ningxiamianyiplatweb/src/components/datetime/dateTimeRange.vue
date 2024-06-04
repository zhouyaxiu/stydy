<template>
  <div>
    <span class="label">{{ title }}：</span>
    <el-date-picker v-model="dateTimeRange" type="datetimerange" range-separator="至" :start-placeholder="startPlaceholder"
      :end-placeholder="endPlaceholder" :format="format" unlink-panels :value-format="valueFormat" :clearable="clearable"
      :disabled="disabledFlag" :disabled-date="disabledDate" @change="$emit('update:defaultValue', dateTimeRange)" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;


const startDate = globalProxy.$dateUtil.nDaysAgoTime(7);
const endDate = globalProxy.$dateUtil.currentTime();
const dateTimeRange = ref([startDate, endDate])

watch(() => props.defaultValue, (val) => {
  if (val) {
    dateTimeRange.value = val
  }
})
const emits = defineEmits(['update:defaultValue'])
const props = defineProps({
  defaultValue: {
    required: false,
    default: () => {
      return []
    }
  },
  startPlaceholder: {
    type: String,
    default: '开始日期'
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
    default: 'YYYY-MM-DD HH:mm:ss'
  },
  format: {
    type: String,
    default: 'YYYY-MM-DD HH:mm:ss'
  },
  disabledDate: {
    type: Function,
    default: (time) => {
      return time.getTime() > Date.now()
    }
  },
  title: {
    type: String,
    default: "选择日期"
  }
})
onMounted(() => {
  if (props.defaultValue && props.defaultValue.length) {
    dateTimeRange.value = props.defaultValue
  }
})

</script>
<style scoped lang='scss'></style>