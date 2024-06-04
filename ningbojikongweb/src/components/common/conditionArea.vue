<template>
  <!-- 查询条件容器
    * v-showBackBtn {Boolean}  是否显示返回按钮
    * v-definedTitle {Number}  一级页面名称
    * v-subTitle {String}  二级页面名称
    * v-thirdTitle {String}  三级页面名称
    * @setConditionFlag {Function}  子组件回调父组件方法---关闭弹窗
    * @callback {Function}  子组件回调父组件方法---导出
     -->
  <div class="condition-area">
    <div class="title-area">
      <div class="title">
        <div class="back-btn" v-show="showBackBtn" @click="clickBack"></div>
        <div class="homt-btn" @click="clickToHome"></div>
        <div class="titleContent">
          <span>{{ $store.state.title }}</span>
        </div>
      </div>
      <div class="fold-btn" @click="clickHandler">
        <span>{{ showFlag ? "收起" : "展开" }}</span>
        <ss-icon
          v-show="!showFlag"
          iconType="bottom"
          style="width: 14px; height: 14px"
        />
        <ss-icon
          v-show="showFlag"
          iconType="top"
          style="width: 14px; height: 14px"
        />
      </div>
    </div>
    <transition name="el-zoom-in-top">
      <div
        class="select-content"
        :style="'vertical-align: ' + (showFlag ? 'text-top' : 'baseline')"
      >
        <div v-show="showFlag" class="condition">
          <el-form ref="form" size="medium" onsubmit="return false;">
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
    showBackBtn: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  mounted () {
    console.log(1111, this.$store)
  },
  methods: {
    clickHandler () {
      this.showFlag = !this.showFlag
      this.$nextTick(() => {
        this.$emit('setConditionFlag', this.showFlag)
      })
    },
    clickBack () {
      this.$router.go(-1)
    },
    clickToHome () {
      this.$router.push('/index')
    }
  }
}
</script>

<style scoped>
.back-btn {
  background: url(../../assets/img/button/icon-back.png) left top no-repeat;
  background-size: contain;
  background-position: left top;
  width: 65px;
  height: 25px;
  cursor: pointer;
  margin-top: 3px;
  /* margin-left: -15px; */
}

.homt-btn {
  background: url(../../assets/img/button/house-door-fill.png) left top
    no-repeat;
  background-size: contain;
  background-position: left top;
  width: 20px;
  height: 19px;
  cursor: pointer;
  margin-top: 6px;
  margin-right: 12px;
}

.condition-area {
}

.title-area {
  width: 100%;
  display: flex;
  justify-content: space-between;
  background: #eaf2fe;
  height: 32px;
  line-height: 32px;
  border-radius: 4px 4px 0px 0px;
  /* box-shadow: 0 2px 8px 2px rgb(30 111 255 / 16%); */
  align-items: center;
}

.title-area .title {
  font-size: 16px;
  /* font-weight: bolder; */
  color: #7283b3;
  padding-left: 10px;
  background: #eaf2fe;
  background: url(../../assets/img/index/image-title.png) left top no-repeat;
  background-size: contain;
  background-position: left top;
  min-height: 32px;
  width: 100%;
  display: flex;
}

.title-area .title .titleContent {
  min-width: 300px;
  height: 32px;
  line-height: 32px;
}

.title-area .title .titleContent span:last-child {
  font-weight: bold;
}

.title-area .fold-btn {
  cursor: pointer;
  font-size: 12px;
  color: #1e6ce4;
  margin-right: 20px;
  width: 56px;
  height: 20px;
  line-height: 20px;
  background-color: #ffffff;
  border-radius: 6px;
  text-align: center;
}

.condition {
  background: #fff;
  /* border-radius: 0px 0px 4px 4px; */
  /* box-shadow: 0 2px 8px 2px rgb(30 111 255 / 16%); */
  padding: 10px 0px 10px 0px;
}
</style>
