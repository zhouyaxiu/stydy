<template>
  <!-- 查询条件容器
    * v-showBackBtn {Boolean}  是否显示返回按钮
    * v-definedTitle {Number}  一级页面名称
    * v-subTitle {String}  二级页面名称
    * v-thirdTitle {String}  三级页面名称
    * @setConditionFlag {Function}  子组件回调父组件方法---关闭弹窗
    * @callback {Function}  子组件回调父组件方法---导出
     -->
  <div class="condition-area condition-block">
    <div class="title-area">
      <div :class="[titleLong?'titleLong':'titleSmall','title']">
        <div class="homt-btn" @click="clickToHome"></div>
        <div class="titleContent">
          <span>{{ definedTitle ? definedTitle : $store.state.title }}</span>
        </div>
      </div>
    </div>
    <transition name="el-zoom-in-top">
      <div
        class="select-content"
        :style="'vertical-align: ' + (showFlag ? 'text-top' : 'baseline')"
      >
        <div v-show="showFlag" class="condition">
          <el-form ref="form" size="medium" onsubmit="return false;" label-position="right" label-width="200px" :inline="true">
            <slot></slot>
          </el-form>
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
    },
    thirdTitle: {
      type: String,
      required: false,
      default: ''
    },
    direction: {
      type: String,
      required: false,
      default: 'row'
    },
    titleLong: {
      type: Boolean,
      required: false,
      default: false
    },
    showBackBtn: {
      type: Boolean,
      required: false,
      default: false
    }
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

.condition-area {
  border-radius: 10px;
  background: #ffffff;
  margin-bottom: 20px;
}

.title-area {
  width: 100%;
  display: flex;
  justify-content: space-between;
  height: 32px;
  line-height: 32px;
  border-radius: 4px 4px 0px 0px;
  align-items: center;
}

.title-area .title {
  font-size: 16px;
  color: #2E5BFF;
  padding-left: 10px;
  background-size: contain;
  min-height: 32px;
  width: 100%;
  display: flex;
}
.titleSmall{
  background: url(../../assets/img/index/image-title.png) left top no-repeat;
}
.titleLong{
  background: url(../../assets/img/index/image-longtitle.png) left top no-repeat;
  width: 264px !important;
  background-size: cover !important;
}

.title-area .title .titleContent {
  height: 32px;
  line-height: 32px;
  font-family: titleFont;
  font-size: 20px;
  color: #2E5BFF;
}
</style>
