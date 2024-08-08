<template>
  <!-- 头部查询条件集合
/***
  * v-searchNameList {Array}  查询条件列表
        [{
              ------通用属性------
              name {String} lable名称
              type {String}  组件类型  slot/input/checkbox/checkboxgroup/radiogroup/daterange/select/multiselect/checkboxselect
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
  */
-->

  <div>
    <template v-for="(item, index) in searchNameList">
      <el-form-item
        :key="index"
        v-if="item.isShow"
        :label="item.type != 'slot' && item.isShow ? item.name+'：' : '' "
        :required="item.required"
      >
        <!-- 插槽--自定义复杂组件 -->
        <template v-if="item.type === 'slot'">
          <slot :name="item.slotName"></slot>
        </template>
        <!-- 普通输入框 -->
        <el-input
          v-if="item.type === 'input' && item.isShow"
          v-model.trim="item.value"
          :style="item.style ? item.style : 'width: 200px'"
          @input="changeFun($event, item)"
          :placeholder="'请输入' + item.name"
          :clearable="item.clearable ? item.clearable : false"
        />
        <!-- 复选框--单个 -->
        <el-checkbox
          v-if="item.type === 'checkbox' && item.isShow"
          @change="changeFun($event, item)"
        >
          {{ item.name }}
        </el-checkbox>
        <!-- 日期时间段 -->
        <ss-date-range
          v-if="item.type === 'daterange' && item.isShow"
          v-model="item.value"
          :style="item.style ? item.style : 'width: 200px'"
          :startTimeOption="item.startTimeOption"
          :endTimeOption="item.endTimeOption"
          @input="changeFun($event, item)"
        />
        <!-- 单选--组 -->
        <el-radio-group
          v-if="item.type === 'radiogroup' && item.isShow"
          v-model="item.value"
          @change="changeFun($event, item)"
          :style="item.style ? item.style : 'width: 200px'"
        >
          <el-radio
            v-for="citem in item.opts"
            :label="citem[item.optsValue]"
            :key="citem[item.optsValue]"
          >
            {{ citem[item.optsLabel] }}
          </el-radio>
        </el-radio-group>
        <!-- 复选框--组 -->
        <el-checkbox-group
          style="float: right"
          v-if="item.type === 'checkboxgroup' && item.isShow"
          @change="changeFun($event, item)"
          v-model="item.value"
          :style="item.style ? item.style : 'width: 200px'"
        >
          <template v-for="citem in item.opts">
            <el-checkbox
              :label="citem[item.optsValue]"
              :key="citem[item.optsValue]"
            >
              {{ citem[item.optsLabel] }}
            </el-checkbox>
          </template>
        </el-checkbox-group>
        <!-- 普通下拉选择 -->
        <ss-custom-select
          v-if="item.type == 'select' && item.isShow"
          :hasCheckbox="false"
          :selectType="1"
          :dataInfo="item"
          :selectList="item.opts"
          @callback="changeFun"
        ></ss-custom-select>
        <!-- 多选下拉 -->
        <ss-custom-select
          v-if="item.type == 'multiselect' && item.isShow"
          :hasCheckbox="false"
          :selectType="2"
          :dataInfo="item"
          :selectList="item.opts"
          @callback="changeFun"
        ></ss-custom-select>
        <!-- 复选框多选下拉 -->
        <ss-custom-select
          v-if="item.type == 'checkboxselect' && item.isShow"
          :hasCheckbox="true"
          :selectType="3"
          :dataInfo="item"
          :selectList="item.opts"
          @callback="changeFun"
        ></ss-custom-select>
      </el-form-item>
    </template>
  </div>
</template>
<script>
import commonOptions from '@/mixin/commonOptions'
export default {
  name: 'searchHeader',
  props: {
    mLeft: {
      type: Boolean,
      default: true
    },
    searchNameList: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  mixins: [commonOptions],
  data: function () {
    return {}
  },
  methods: {
    changeFun (val, item) {
      this.$emit('changeFun', val, item)
    }
  }
}
</script>

<style>
.search-header .el-checkbox__label,
.el-radio__label {
  color: #4b5c8c !important;
}

.mr20 {
  margin-right: 20px;
}
.search-header .el-input__prefix {
  font-size: 12px !important;
}

.search-header .el-radio,
.search-header .el-checkbox {
  margin-right: 10px;
}
.custom-ckbox:not(:first-child) {
  margin-left: 0;
}
.condition .el-form-item:last-child {
  float: right;
  margin-right: 70px;
}
</style>
