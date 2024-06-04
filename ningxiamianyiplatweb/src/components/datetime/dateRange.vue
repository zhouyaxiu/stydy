<template>
  <div>
    <span class="label">{{ title }}：</span>
    <el-date-picker v-model="daterange" type="daterange" range-separator="至" :start-placeholder="startPlaceholder"
      :end-placeholder="endPlaceholder" format="YYYY-MM-DD" unlink-panels :value-format="valueFormat"
      :clearable="clearable" :disabled="disabledFlag" :disabled-date="disabledDate"
      @change="$emit('update:defaultValue', daterange)" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const startDate = globalProxy.$dateUtil.nDaysAgoTime(7);
const endDate = globalProxy.$dateUtil.currentTime();
const daterange = ref([startDate, endDate])
watch(() => props.defaultValue, (val) => {
  if (val) {
    daterange.value = val
  }
})
const emits = defineEmits(['update:defaultValue'])
const props = defineProps({
  defaultValue: {
    required: false,
    default: []
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
    default: 'YYYY-MM-DD'
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
  },
})
onMounted(() => {
  if (props.defaultValue && props.defaultValue.length) {
    daterange.value = props.defaultValue
  }
})

</script>
<style scoped lang='scss'></style>