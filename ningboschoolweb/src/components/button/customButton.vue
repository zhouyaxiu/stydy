<template>
  <!-- 自定义按钮组件
  /** 父组件传参
    * v-type {String}  按钮类型 详情见 CUSTOMBUTTONTYPES  utils/common/consts.js
    * @click {Function}  子组件回调父组件方法
 */ -->
  <div :class="type + '-btn'" @click="clickHandle" />
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
  background-position: left center;
  min-width: 82px;
  min-height: 34px;
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
  .btn-util-cls
}

.import-btn {
  background: url(../../assets/img/button/button-import.png) left top no-repeat;
  .btn-util-cls
}

.batchImport-btn {
  background: url(../../assets/img/button/button-batch-import.png) left top no-repeat;
  .btn-util-cls;
  min-width: 142px;
}

.download-btn {
  background: url(../../assets/img/button/button-download.png) left top no-repeat;
  .btn-util-cls
}

.downloadImport-btn {
  background: url(../../assets/img/button/button-import-download.png) left top no-repeat;
  .btn-util-cls;
  min-width: 144px;
}

.disscan-btn {
  background: url(../../assets/img/button/button-disscan.png) left top no-repeat;
  .btn-util-cls;
  min-width: 110px;
}

.scan-btn {
  background: url(../../assets/img/button/button-scan.png) left top no-repeat;
  .btn-util-cls;
  min-width: 110px;
}

.dissubmit-btn {
  background: url(../../assets/img/button/button-dissubmit.png) left top no-repeat;
  .btn-util-cls;
}

.submit-btn {
  background: url(../../assets/img/button/button-submit.png) left top no-repeat;
  .btn-util-cls
}

.output-btn {
  background: url(../../assets/img/button/button-export.png) left top no-repeat;
  .btn-util-cls;
  // min-width: 108px;
}

.alter-btn {
  background: url(../../assets/img/button/button-alter.png) left top no-repeat;
  .btn-util-cls;
}

.print-btn {
  background: url(../../assets/img/button/button-print.png) left top no-repeat;
  .btn-util-cls;
}

.column-btn {
  background: url(../../assets/img/button/button-column.png) left top no-repeat;
  .btn-util-cls;
  min-width: 101px;
}

.jiandang-btn {
  background: url(../../assets/img/button/button-jiandang.png) left top no-repeat;
  .btn-util-cls;
  min-width: 157px;
}

.check-btn {
  background: url(../../assets/img/button/button-check.png) left top no-repeat;
  .btn-util-cls;
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
  min-width: 157px;
}

.failedQuery-btn {
  background: url(../../assets/img/button/button-failed-query.png) left top no-repeat;
  .btn-util-cls;
  min-width: 141px;
}

.entry-btn {
  background: url(../../assets/img/button/button-entry.png) left top no-repeat;
  .btn-util-cls;
}

.excelImport-btn {
  background: url(../../assets/img/button/button-excel-import.png) left top no-repeat;
  .btn-util-cls;
  min-width: 114px;
  background-size: 100% 100%;
}
</style>
