 <!-- 自定义头部查询条件组件
  * v-searchNameList {Array}  查询条件列表
        [{
              ------通用属性------
              name {String} lable名称
              type {String}  组件类型  slot/input/checkbox/checkboxgroup/radiogroup/daterange/select/multiselect/checkboxselect/txtUpload
              key {String}  绑定字段
              value {String} 绑定值
              isShow {Boolean}  组件显示/隐藏
              clearable {Boolean}  是否可清除

              ------下拉 && 单选 && 复选------
              optsValue {String}  绑定值--与opts列表属性对应
              optsLabel {String}  显示值--与opts列表属性对应
              opts {Array}  选项列表
                    [{
                      optsValue {String} 绑定值
                      optsLabel {String} 显示值
                    }]
              ------下拉多选------
              multipleLimit {Number}  多选时用户最多可以选择的项目数，为 0 则不限制，默认 0
              collapseTags {Boolean}  多选时是否将选中值按文字的形式展示，默认 false
        }]
  * @changeFun {Function}  子组件回调父组件方法,更新数据
  */ -->
<template>
  <el-space wrap>
    <div v-for="(item, idx) in searchNameList" class="space-item" :key="idx" :style="item.style">
      <!-- label内容 -->
      <span class="lable-cls" v-if="item.labelHidden ? false : true">
        {{ item.name }}：
      </span>
      <!-- 插槽--自定义复杂组件 -->
      <template v-if="item.type === 'slot'">
        <slot :name="item.slotName"></slot>
      </template>
      <!-- 日期时间段 -->
      <ss-date-picker v-if="(item.type === 'datetimerange' ||
        item.type == 'datetime' ||
        item.type == 'date') &&
        item.isShow
        " :style="item.type === 'datetimerange' ? 'top:-4px' : ''" :value="item.value" @input="changeFun($event, item)"
        :dateType="item.type" :clearable="item.clearable" :valueFormat="item.type == 'date' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:mm:ss'
          " />
      <!-- 普通输入框 -->
      <el-input v-if="item.type === 'input' && item.isShow" v-model.trim="item.value"
        :style="item.style ? item.style : 'width: 200px'" @input="changeFun($event, item)"
        :placeholder="'请输入' + item.name" :clearable="item.clearable ? item.clearable : false" />
      <!-- 复选框--单个 -->
      <el-checkbox v-if="item.type === 'checkbox' && item.isShow" @change="changeFun($event, item)">
        {{ item.name }}
      </el-checkbox>
      <!-- 复选框--组 -->
      <ss-checkbox-group :dataInfo="item" v-if="item.type === 'checkboxgroup' && item.isShow" :selectList="item.opts"
        @select-change="changeFun" />
      <!-- 单选--组 -->
      <ss-radio-group :dataInfo="item" v-if="item.type === 'radiogroup' && item.isShow" :selectList="item.opts"
        @select-change="changeFun" />
      <!-- 普通下拉 -->
      <ss-common-select v-if="item.type == 'select' && item.isShow" :selectType="1" :dataInfo="item"
        :selectList="item.opts" @select-change="changeFun" />
      <!-- 多选下拉 -->
      <ss-common-select v-if="item.type == 'multiselect' && item.isShow" :selectType="2" :dataInfo="item"
        :selectList="item.opts" @select-change="changeFun" />
      <!-- 复选框多选下拉 -->
      <ss-common-select v-if="item.type == 'checkboxselect' && item.isShow" :selectType="3" :dataInfo="item"
        :selectList="item.opts" @select-change="changeFun" />
      <!-- upload 上传 -->
      <ss-upload-txt v-if="item.type === 'txtUpload'" @upload-txt-change="changeFun" :dataInfo="item" />
    </div>
  </el-space>
</template>
<script setup>
//子组件回调父组件方法
const emits = defineEmits(['query-change'])
const changeFun = (val, item) => {
  emits('query-change', val, item)
}
//父组件传参--按钮类型
const props = defineProps({
  searchNameList: {
    type: Array,
    default: () => {
      return []
    },
    required: false
  }
})
</script>

<style scoped lang="scss">
.lable-cls {
  display: inline-block;
  color: #4b5c8c;
  line-height: 48px;
}

.space-item {
  position: relative;
}
</style>
