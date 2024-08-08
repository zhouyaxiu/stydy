<template>
  <!-- 自定义日期组件
  /** 父组件传参
    * v-value {Array}  默认时间段数组
    * v-dateType {String}  date-picker类型
    * v-valueFormat {Number}  格式化选中日期时间值
    * v-startPlaceholder {String}  开始时间输入框提示
    * v-endPlaceholder {String}  结束时间输入框提示
    * v-disabledFlag {Boolean}  是否禁用
    * v-clearable {Boolean}  是否可清除
    * v-startTimeOption {Object}  开始时间限制
    * v-endTimeOption {Object}  结束时间限制
    * v-isClearOneYear {Boolean}  跨年后结束日期是否更改为同年份
    * v-popperClass {String}  自定义DatePicker下拉框的样式类名
    * v-isObstetrics {Boolean}  产科添加疫苗针次-接种日期特殊限制
  */
   -->
  <div style="display: inline-block" class="datePicker">
    <el-date-picker
      :popper-class="popperClass"
      :clearable="clearable"
      style="width: 150px"
      v-model="startdate"
      :type="dateType"
      :placeholder="startPlaceholder"
      :format="valueFormat"
      :value-format="valueFormat"
      :editable="false"
      :picker-options="
        startTimeOption ? startTimeOption : startDefaultTimeOption
      "
      @change="emitDate"
      :disabled="disabledFlag"
    ></el-date-picker>
    <span> 至 </span>
    <el-date-picker
      :popper-class="popperClass"
      :clearable="clearable"
      style="width: 150px"
      v-model="enddate"
      :type="dateType"
      :placeholder="endPlaceholder"
      :format="valueFormat"
      :value-format="valueFormat"
      :editable="false"
      :picker-options="endTimeOption ? endTimeOption : endDefaultTimeOption"
      @change="emitDate"
      :disabled="disabledFlag"
    ></el-date-picker>
  </div>
</template>

<script>
export default {
  name: 'daterange',
  data () {
    return {
      startdate: '',
      enddate: '',
      startDefaultTimeOption: {
        disabledDate: (time) => {
          return time.getTime() > new Date(this.$util.today()).getTime()
        }
      },
      endDefaultTimeOption: {
        disabledDate: (time) => {
          return time.getTime() > new Date(this.$util.today()).getTime()
        }
      }
    }
  },
  props: {
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
      type: Object,
      default: () => {
        return null
      }
    },
    // 结束时间限制
    endTimeOption: {
      type: Object,
      default: () => {
        return null
      }
    },
    // 跨年后结束日期是否更改为同年份
    isClearOneYear: {
      type: Boolean,
      default: false
    },
    dateType: {
      type: String,
      default: 'date'
    },
    valueFormat: {
      type: String,
      default: 'yyyy-MM-dd'
    },
    popperClass: {
      type: String,
      default: ''
    },
    // 产科添加疫苗针次-接种日期特殊限制
    isObstetrics: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    value (val) {
      if (val) {
        this.startdate = val[0]
        this.enddate = val[1]
      } else {
        this.startdate = ''
        this.enddate = ''
      }
    }
  },
  methods: {
    emitDate () {
      if (
        this.startdate !== '' &&
        this.enddate !== '' &&
        this.startdate > this.enddate
      ) {
        if (!this.isObstetrics) {
          this.enddate = ''
          this.$util.errorMsg(
            `${this.startPlaceholder}不能大于${this.endPlaceholder}，请重新填写！`
          )
          this.$emit('input', [this.startdate, this.enddate])
          return
        } else {
          this.$util.errorMsg(
            `${this.startPlaceholder}不能大于${this.endPlaceholder}！`
          )
          this.enddate = this.startdate
        }
      }
      if (this.isClearOneYear) {
        const startYear = this.startdate.slice(0, 4)
        const endYear = this.enddate.slice(0, 4)
        if (startYear !== endYear) {
          this.enddate = this.startdate
        }
      }
      this.$emit('input', [this.startdate, this.enddate])
    }
  },
  mounted () {
    if (this.value && this.value.length) {
      this.startdate = this.value[0]
      this.enddate = this.value[1]
    } else {
      this.startdate = ''
      this.enddate = ''
    }
  }
}
</script>

<style>
/* .el-scrollbar:nth-of-type(2) {
  display: none;
} */
</style>
<style>
.noMinutes .el-scrollbar:nth-child(2) {
  display: none;
}
</style>
