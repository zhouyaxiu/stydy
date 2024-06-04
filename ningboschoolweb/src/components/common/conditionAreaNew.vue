<template>
  <!-- 查询条件容器
    * v-showBackBtn {Boolean}  是否显示返回按钮
    * v-definedTitle {Number}  一级页面名称
    * v-subTitle {String}  二级页面名称
    * v-thirdTitle {String}  三级页面名称
    * @setConditionFlag {Function}  子组件回调父组件方法---关闭弹窗
    * @callback {Function}  子组件回调父组件方法---导出
     -->
  <div class="condition-area-new condition-block">
    <div class="title-area">
      <div class="title">
        <div class="homt-btn" @click="clickToHome"></div>
        <div class="titleContent">
          <span>{{ definedTitle ? definedTitle : $store.state.title }}</span>
        </div>
      </div>
    </div>
    <transition name="el-zoom-in-top">
      <div class="select-content" style="padding-left: 80px;margin-top: -20px;">
        <div class="condition">
          <slot></slot>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'selectArea',
  data () {
    return {
      showFlag: true,
      results: []
    }
  },
  props: {
    definedTitle: {
      type: String,
      required: false,
      default: ''
    },
    subTitle: {
      type: String,
      required: false,
      default: ''
    }
    // thirdTitle: {
    //   type: String,
    //   required: false,
    //   default: ''
    // },
    // direction: {
    //   type: String,
    //   required: false,
    //   default: 'row'
    // },
    // showBackBtn: {
    //   type: Boolean,
    //   required: false,
    //   default: false
    // }
  },
  methods: {
    clickToHome () {
      this.$router.push('/index')
    }
  }
}
</script>

<style scoped>
@font-face{
  font-family: titleFont;
  src:url('../../assets/fonts/titileFont.woff2')
}

.homt-btn {
  background: url(../../assets/img/button/house-door-fill.png) left top
    no-repeat;
  background-size: contain;
  background-position: left top;
  width: 20px;
  height: 19px;
  cursor: pointer;
  margin: 6px 10px 0 10px;
}
</style>
<style>
.condition-area-new {
  border-radius: 10px;
  background: #ffffff;
  margin-bottom: 20px;
  padding-bottom: 10px;
}

.condition-area-new .title-area {
  width: 100%;
  display: flex;
  justify-content: space-between;
  height: 32px;
  line-height: 32px;
  border-radius: 4px 4px 0px 0px;
  align-items: center;
}

.condition-area-new .title-area .title {
  font-size: 16px;
  color: #2E5BFF;
  padding-left: 10px;
  background: url(../../assets/img/index/image-title.png) left top no-repeat;
  background-size: contain;
  background-position: left top;
  min-height: 32px;
  width: 100%;
  display: flex;
}

.condition-area-new .title-area .title .titleContent {
  min-width: 300px;
  height: 32px;
  line-height: 32px;
  font-family: titleFont;
  font-size: 20px;
  color: #2E5BFF;
}
.condition-area-new .condition-row {
	display: flex;
	line-height: 48px;
	font-size: 14px;
	color: #333333;
  width: 100%;
  display: -webkit-inline-box;
}

.condition-area-new .condition-row .item {
	display: flex;
	align-items: center;
	width: 33.333333%;
  margin-bottom: 10px;
}

.condition-area-new .condition-row .item+.item {
	/* margin-left: 80px; */
}

.condition-area-new .condition-row .mLeft {
	margin-left: 24px;
}
.condition-area-new .condition-row .el-form-item__label{
  line-height: initial;
}
.condition-area-new .condition-row .label {
	margin-right: 0;
	text-align: right;
	min-width:100px;
  display: inline;
}

.condition-area-new .condition-row .el-radio {
	margin-right: 10px !important;
}

.condition-area-new .condition-row .el-checkbox {
	color: #4B5C8C;
	margin-right: 10px !important;
}
.condition-area-new .condition-row .el-input:not(.el-date-editor) input {
	font-size: 14px !important;
	line-height: 38px !important;
	height: 38px !important;
	border-radius: 4px !important;
	background: #fff;
	border-color:#DDE0EB;
	width: 200px;
}
.condition-area-new .condition-row .el-input.bginput input{
  background: #F6F8FC;
}
.condition-area-new .condition-row .el-select .el-input input{
	background: #fff;
}
.condition-area-new .condition-row .el-input.is-disabled .el-input__inner{
  background: #F6F8FC;
  border-color:#DDE0EB;
}
.condition-area-new .condition-row .el-date-editor.el-input, .el-date-editor.el-input__inner{
	width: 200px;
}
.condition-area-new .el-form-item__content{
  display: flex;
  white-space: nowrap;
}
</style>
