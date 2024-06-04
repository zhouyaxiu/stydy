 <!-- 自定义复选框组
  /** 父组件传参
   * v-dataInfo {Object}  组件信息
    {
        *value {T}  默认值
        *optsValue {String}  绑定值--与opts列表属性对应
        *optsLabel {String}  显示值--与opts列表属性对应
    }
    * v-selectList {Array}  选项列表
    * @checkboxChange {Function}  子组件回调父组件方法
 */ -->
<template>
    <el-checkbox v-if="dataInfo.hasCheckAll" :indeterminate="isIndeterminate" v-model="checkAll" style="padding-top: 20px;"
        @change="handleCheckAllChange">全选</el-checkbox>
    <el-checkbox-group v-model="checkboxValue" style="padding-top: 10px; margin-left: 20px;">
        <el-checkbox v-for="(item, idx) in selectList" :label="item[dataInfo.optsValue]">
            {{ item[dataInfo.optsLabel] }}
        </el-checkbox>
    </el-checkbox-group>
</template>
<script setup>
const checkboxValue = ref([])
const checkAll = ref(false)
const isIndeterminate = ref(true)
//子组件回调父组件方法
const emits = defineEmits(['select-change'])
const checkboxChange = (val) => {
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
watch(checkboxValue, (newVal, oldVal) => {
    if (checkboxValue.value.length == props.selectList.length) {
        isIndeterminate.value = false
        checkAll.value = true
    } else {
        checkAll.value = false
        if (checkboxValue.value.length == 0) {
            isIndeterminate.value = false
        } else {
            isIndeterminate.value = true
        }
    }
    checkboxChange(checkboxValue.value)
})
onMounted(() => {
    checkboxValue.value = props.dataInfo.value
    if (checkboxValue.value.length) {
        isIndeterminate.value = true
    }
})
//全选/全不选事件
const handleCheckAllChange = (val) => {
    let arr = []
    if (val) {
        props.selectList.forEach(i => {
            if (checkboxValue.value.indexOf(i[props.dataInfo.optsValue] < 0)) {
                arr.push(i[props.dataInfo.optsValue])
            }
        })
    }
    checkboxValue.value = arr
}
</script>
<style scoped lang="scss"></style>
