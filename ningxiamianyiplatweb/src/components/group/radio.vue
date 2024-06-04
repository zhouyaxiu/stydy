 <!-- 自定义单选框组
  /** 父组件传参
   * v-dataInfo {Object}  组件信息
    {
        *value {T}  默认值
        *optsValue {String}  绑定值--与opts列表属性对应
        *optsLabel {String}  显示值--与opts列表属性对应
    }
    * v-selectList {Array}  选项列表
    * @radioChange {Function}  子组件回调父组件方法
 */ -->
<template>
    <el-radio-group v-model="radioVal" @change="radioChange">
        <el-radio v-for="(item, idx) in selectList" :label="item[dataInfo.optsValue]">
            {{ item[dataInfo.optsLabel] }}
        </el-radio>
    </el-radio-group>
</template>
<script setup>
const radioVal = ref()
//子组件回调父组件方法
const emits = defineEmits(['select-change'])
const radioChange = (val) => {
    emits('select-change', val, props.dataInfo)
};
//父组件传参--按钮类型
const props = defineProps({
    dataInfo: {
        type: Object,
        required: true
    },
    selectList: {
        type: Array,
        default: () => {
            return []
        },
        required: false
    }
})
onMounted(() => {
    radioVal.value = props.dataInfo.value
})
</script>
<style scoped lang="scss"></style>
