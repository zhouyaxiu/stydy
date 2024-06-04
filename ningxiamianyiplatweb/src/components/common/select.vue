<!-- 自定义下拉组件
  /** 父组件传参
    * v-selectType {Number}  1：普通单选  2：普通多选  3：复选框多选
    * v-dataInfo {Object}  组件信息
        {
          *value {T}  默认值
          *optsValue {String}  绑定值--与opts列表属性对应
          *optsLabel {String}  显示值--与opts列表属性对应
           name {String}  lable名称 默认""
           style {Object}  自定义样式 默认 {}
           clearable {Boolean}  是否可清除 默认 true
        }
    * v-selectList {Array}  下拉数据
    * @callback {Function}  子组件回调父组件方法
  */  -->
<template>
    <el-select ref="ssSelect" class="selectStyle" :collapse-tags="true" collapse-tags-tooltip
        :multiple="selectType != 1" :clearable="clearable" :style="style ? style : 'width: 200px;'"
        v-model="selectValue" @change="selectChange" placeholder="请选择">
        <div class="select-btn-row" v-if="selectType == 3">
            <div class="select-text" @click="selectAllFun">全选</div>
            <div class="select-text" @click="selectNoData">全不选</div>
        </div>
        <el-option class="optionCss" v-for="(item, index) in selectOpts" :key="index" :label="item[dataInfo.optsLabel]"
            :value="item[dataInfo.optsValue]">
            <el-checkbox style="float: left" v-model="item.checkedState" v-if="selectType == 3"></el-checkbox>
            <span :class="selectType == 3 ? 'select-check-option' : ''">
                {{ item[dataInfo.optsLabel] }}
            </span>
        </el-option>
    </el-select>
</template>
<script setup>
const selectValue = ref()
const selectOpts = ref()
const clearable = ref(false)
const style = ref({})
const ssSelect = ref()
//子组件回调父组件方法
const emits = defineEmits(['select-change'])
const selectChange = () => {
    if (props.selectType !== 1) {
        selectOpts.value.forEach((item) => {
            if (selectValue.value.indexOf(item.value) != -1) {
                item.checkedState = true
            } else {
                item.checkedState = false
            }
        })
    }
    emits('select-change', selectValue.value, props.dataInfo)
};
//父组件传参--按钮类型
const props = defineProps({
    dataInfo: {
        type: Object,
        required: true
    },
    selectType: {
        type: Number,
        required: true
    },
    selectList: {
        type: Array,
        default: () => {
            return []
        }
    }
})
watch(() => props.selectList, (newVal) => {
    let arr = newVal
    selectValue.value = Array.isArray(props.dataInfo.value) ? [] : ''
    arr.forEach((i) => {
        i.checkedState = false
        i.label = i[props.dataInfo.optsLabel]
        i.value = i[props.dataInfo.optsValue]
    })
    selectOpts.value = arr
    selectAllFun()
})
onMounted(() => {
    selectValue.value = props.dataInfo.value
    selectOpts.value = props.selectList.map((i) => {
        i.checkedState = false
        i.label = i[props.dataInfo.optsLabel]
        i.value = i[props.dataInfo.optsValue]
        return i
    })
    clearable.value = props.dataInfo.clearable ? props.dataInfo.clearable : true
    style.value = props.dataInfo.style ? props.dataInfo.style : {}
})
// 全选
const selectAllFun = () => {
    selectNoData()
    selectOpts.value.forEach((item) => {
        item.checkedState = true
        selectValue.value.push(item.value)
    })
    emits('select-change', selectValue.value, props.dataInfo)
    ssSelect.value.blur()
}
// 全不选
const selectNoData = () => {
    selectValue.value = []
    selectOpts.value.forEach((item) => {
        item.checkedState = false
    })
    emits('select-change', selectValue.value, props.dataInfo)
    ssSelect.value.blur()
}
</script>

<style scoped lang="scss">
.select-btn-row {
    display: flex;
    background: #F5F7FA;
    margin-bottom: 5px;
    margin-top: -6px;
}

.select-text {
    font-size: 14px;
    padding: 0 20px;
    overflow: hidden;
    color: #606266;
    height: 34px;
    line-height: 34px;
    font-weight: bold;
    cursor: pointer;
}

.select-check-option {
    float: left;
    margin-left: 4px;
    color: #8492a6;
    font-size: 13px;
    display: inline-block;
    width: 120px;
    position: relative;
    left: -20px;
    z-index: 1000;
    padding-left: 25px;
}
</style>
