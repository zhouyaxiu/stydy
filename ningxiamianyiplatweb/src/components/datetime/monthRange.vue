<template>
  <div>
    <span class="label">{{ title }}：</span>
    <el-date-picker v-model="monthrange" type="monthrange" range-separator="至" :start-placeholder="startPlaceholder"
      :end-placeholder="endPlaceholder" format="YYYY-MM" unlink-panels :value-format="valueFormat" :clearable="clearable"
      :disabled="disabledFlag" :disabled-date="disabledDate" @change="$emit('update:defaultValue', monthrange)" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
const monthrange = ref(['2022-08', '2022-12'])

watch(() => props.defaultValue, (val) => {
  if (val) {
    monthrange.value = val
  }
})
const emits = defineEmits(['update:defaultValue'])
const props = defineProps({
  defaultValue: {
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
    default: 'YYYY-MM'
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
const monthChange = () => {
  emits('change', monthrange)
}
onMounted(() => {
  if (props.defaultValue && props.defaultValue.length) {
    monthrange.value = props.defaultValue
  }
})

</script>
<style scoped lang='scss'></style>