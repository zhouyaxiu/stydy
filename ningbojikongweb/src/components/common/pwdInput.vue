<template>
  <!-- 密码输入框
  /** 父组件传参
    * v-value {String}  输入框绑定值
    * v-view {Boolean}  输入框是否显示密码
    * v-placeholder {String}  输入框提示
    * v-vertical {String}  输入框右侧眼睛垂直方向样式 middle/bottom
    * @callback {Function}  子组件回调父组件方法
 */ -->
  <el-input
    :placeholder="placeholder"
    v-model.trim="passValue"
    :type="open ? 'password' : 'text'"
  >
    <i
      slot="suffix"
      :class="'el-input__icon ss-icon-view-' + vertical"
      v-show="open && passValue"
      style="cursor: pointer"
      @click="open = !open"
    ></i>
    <i
      slot="suffix"
      :class="'el-input__icon ss-icon-unview-' + vertical"
      v-show="!open && passValue"
      style="cursor: pointer"
      @click="open = !open"
    ></i>
  </el-input>
</template>

<script>
export default {
  name: 'pwdInput',
  data () {
    return {
      passValue: '',
      open: true
    }
  },
  watch: {
    passValue (v) {
      // 更新父组件
      this.$emit('callback', v)
    }
  },
  props: {
    /** 初始值 */
    value: {
      type: String,
      required: false,
      default: ''
    },
    /** 是否可见 */
    view: {
      type: Boolean,
      required: false,
      default: true
    },
    /** 输入提示 */
    placeholder: {
      type: String,
      default: '请输入密码'
    },
    /** 垂直样式 */
    vertical: {
      type: String,
      default: 'middle'
    }
  },
  mounted () {
    // 单向数据流，避免直接改动
    this.passValue = this.value
    this.open = this.view
  }
}
</script>

<style scoped>
.ss-icon-view-middle::after {
  position: relative;
  width: 26px;
  height: 26px;
  background-image: url(../../assets/img/icon/icon_view.png);
  vertical-align: middle;
}
.ss-icon-unview-middle::after {
  position: relative;
  width: 26px;
  height: 26px;
  background-image: url(../../assets/img/icon/icon_un_view.png);
  vertical-align: middle;
}
.ss-icon-view-bottom::after {
  position: relative;
  width: 26px;
  height: 26px;
  background-image: url(../../assets/img/icon/icon_view.png);
  vertical-align: bottom;
}
.ss-icon-unview-bottom::after {
  position: relative;
  width: 26px;
  height: 26px;
  background-image: url(../../assets/img/icon/icon_un_view.png);
  vertical-align: bottom;
}
</style>
