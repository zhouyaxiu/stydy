 <!-- 自定义日期时间段组件
 /** 父组件传参
    * v-value {Array}  默认时间段数组
    * v-dateType {String}  date-picker类型 datetimerange/datetime/date
    * v-startPlaceholder {String}  开始时间输入框提示
    * v-endPlaceholder {String}  结束时间输入框提示
    * v-disabledFlag {Boolean}  是否禁用
    * v-clearable {Boolean}  是否可清除
    * v-startTimeOption {Object}  开始时间限制
    * v-endTimeOption {Object}  结束时间限制
 */ -->
<template>
    <el-date-picker v-model="pickerValue" :type="dateType" range-separator="至" :start-placeholder="startPlaceholder"
        :disabled="disabledFlag" :picker-options="pickerOptions" :clearable="clearable" :format="valueFormat"
        :value-format="valueFormat" :end-placeholder="endPlaceholder" @change="emitDate" />
</template>
<script setup>
//单向数据流，组件内声明可修改变量，初始化赋值
const pickerValue = ref('')
//父组件传参--按钮类型
const props = defineProps({
    value: {
        required: true
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
    // 开始时间限制
    startTimeOption: {
        type: String,
        default: ''
    },
    // 结束时间限制
    endTimeOption: {
        type: String,
        default: ''
    },
    dateType: {
        type: String,
        default: 'date'
    },
    valueFormat: {
        type: String,
        default: 'YYYY-MM-DD HH:mm:ss'
    },
    pickerOptions: {
        type: Object,
        default: {}
    }
})
//子组件回调父组件方法
const emits = defineEmits(['input'])
const emitDate = () => {
    emits('input', pickerValue)
}
onMounted(() => {
    if (props.value && props.value.length) {
        pickerValue.value = props.value
    } else {
        pickerValue.value = ''
    }
})
</script>
<style scoped lang="scss"></style>
