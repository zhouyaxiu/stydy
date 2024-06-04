<template>
  <!-- 自定义按钮组件
  /** 父组件传参
    * v-type {String}  按钮类型 详情见 CUSTOMBUTTONTYPES  utils/common/consts.js
    * @click {Function}  子组件回调父组件方法
 */ -->
  <div>
    <div :class="type + '-btn'" @click="clickHandle" />
  </div>
</template>

<script>
export default {
  name: 'customButton',
  data() {
    return {}
  },
  props: {
    type: {
      type: String,
      required: true
    }
  },
  methods: {
    clickHandle() {
      this.$emit('click')
    }
  }
}
</script>

<style scoped lang="less">
.btn-util-cls() {
  background-size: contain;
  background-position: left top;
  min-width: 72px;
  min-height: 32px;
  cursor: pointer;
}

.insert-btn {
  background: url(../../assets/img/button/button-add.png) left top no-repeat;
  .btn-util-cls
}

.update-btn {
  background: url(../../assets/img/button/button-save.png) left top no-repeat;
  .btn-util-cls
}

.delete-btn {
  background: url(../../assets/img/button/icon-delete-single.png) left top no-repeat;
  .btn-util-cls
}

.query-btn {
  background: url(../../assets/img/button/button-search.png) left top no-repeat;
  .btn-util-cls
}

.reset-btn {
  background: url(../../assets/img/button/button-reset.png) left top no-repeat;
  .btn-util-cls;
  min-width: 108px;
  min-height: 32px;
}

.output-btn {
  background: url(../../assets/img/button/button-export.png) left top no-repeat;
  .btn-util-cls;
  min-width: 108px;
}

.download-btn {
  background: url(../../assets/img/button/button-download.png) left top no-repeat;
  .btn-util-cls;
  min-width: 141px;
}

.import-btn {
  background: url(../../assets/img/button/button-import.png) left top no-repeat;
  .btn-util-cls;
  min-width: 141px;
  background-size: 100% 100%;
}

.failedQuery-btn {
  background: url(../../assets/img/button/button-failed-query.png) left top no-repeat;
  .btn-util-cls;
  min-width: 141px;
}

.unfiledQuery-btn {
  background: url(../../assets/img/button/button-unfiled-query.png) left top no-repeat;
  .btn-util-cls;
  min-width: 118px;
}

.singleImport-btn {
  background: url(../../assets/img/button/button-single-import.png) left top no-repeat;
  .btn-util-cls;
  min-width: 108px;
}

.printNotice-btn {
  background: url(../../assets/img/button/button-print-notice.png) left top no-repeat;
  .btn-util-cls;
  min-width: 147px;
}

.printNotice40-btn {
  background: url(../../assets/img/button/button-print-notice-40.png) left top no-repeat;
  .btn-util-cls;
  min-width: 147px;
  height: 40px;
}

.check-btn {
  background: url(../../assets/img/button/button-check.png) left top no-repeat;
  .btn-util-cls;
}

.entry-btn {
  background: url(../../assets/img/button/button-entry.png) left top no-repeat;
  .btn-util-cls;
}

.print-btn {
  background: url(../../assets/img/button/button-print.png) left top no-repeat;
  .btn-util-cls;
}

.jiandang-btn {
  background: url(../../assets/img/button/button-jiandang.png) left top no-repeat;
  .btn-util-cls;
  min-width: 157px;
}

.buzhong-btn {
  background: url(../../assets/img/button/button-buzhong.png) left top no-repeat;
  .btn-util-cls;
  min-width: 157px;
}

.buzheng-btn {
  background: url(../../assets/img/button/button-buzheng.png) left top no-repeat;
  .btn-util-cls;
  min-width: 157px;
}

.chayan-btn {
  background: url(../../assets/img/button/button-chayan.png) left top no-repeat;
  .btn-util-cls;
  min-width: 132px;
}

.alter-btn {
  background: url(../../assets/img/button/button-alter.png) left top no-repeat;
  .btn-util-cls;
}
</style>
