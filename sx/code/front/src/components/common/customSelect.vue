<template>
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
  */ -->
  <el-select
    class="selectStyle"
    :collapse-tags="true"
    :multiple="selectType != 1"
    :clearable="clearable"
    :style="style ? style : 'width: 200px;'"
    v-model="selectValue"
    @change="selectChange"
    :placeholder="'请选择' + selectName"
  >
    <div class="select-btn-row" v-if="selectType == 3">
      <div class="select-text" @click="selectAllFun">全选</div>
      <div class="select-text" @click="selectNoData">全不选</div>
    </div>
    <el-option
      class="optionCss"
      v-for="(item, index) in selectOpts"
      :key="index"
      :label="item.label"
      :value="item.value"
    >
      <el-checkbox
        style="float: left"
        v-model="item.checkedState"
        v-if="selectType == 3"
      ></el-checkbox>
      <span :class="selectType == 3 ? 'select-check-option' : ''">
        {{ item.label }}
      </span>
    </el-option>
  </el-select>
</template>
<script>
export default {
  name: 'multiSelect',
  props: {
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
  },
  data () {
    return {
      selectValue: null,
      selectOpts: [],
      selectName: '',
      clearable: true,
      style: {}
    }
  },
  watch: {
    selectList (newVal, oldVal) {
      let arr = newVal
      let _self = this
      this.selectValue = Array.isArray(this.dataInfo.value) ? [] : ''
      arr.forEach((i) => {
        i.checkedState = false
        i.label = i[_self.dataInfo.optsLabel]
        i.value = i[_self.dataInfo.optsValue]
      })
      this.selectOpts = arr
    }
  },
  methods: {
    // 全选
    selectAllFun () {
      this.selectNoData()
      this.selectOpts.forEach((item) => {
        item.checkedState = true
        this.selectValue.push(item.value)
      })
      this.$emit('callback', this.selectValue, this.dataInfo)
    },
    // 全不选
    selectNoData () {
      this.selectValue = []
      this.selectOpts.forEach((item) => {
        item.checkedState = false
      })
      this.$emit('callback', this.selectValue, this.dataInfo)
    },
    selectChange (val) {
      if (this.selectType !== 1) {
        this.selectOpts.forEach((item) => {
          if (this.selectValue.indexOf(item.code) > -1) {
            item.checkedState = true
          } else {
            item.checkedState = false
          }
        })
      }
      this.$emit('callback', this.selectValue, this.dataInfo)
    }
  },
  mounted () {
    let _self = this
    this.selectValue = this.dataInfo.value
    this.selectName = this.dataInfo.name ? this.dataInfo.name : ''
    this.selectOpts = this.selectList.map((i) => {
      i.checkedState = false
      i.label = i[_self.dataInfo.optsLabel]
      i.value = i[_self.dataInfo.optsValue]
      return i
    })
    this.clearable = this.dataInfo.clearable ? this.dataInfo.clearable : true
    this.style = this.dataInfo.style ? this.dataInfo.style : {}
  }
}
</script>

<style scoped>
</style>
