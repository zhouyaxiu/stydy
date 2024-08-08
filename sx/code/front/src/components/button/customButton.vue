<template>
  <!-- 自定义按钮组件
  /** 父组件传参
    * v-type {String}  按钮类型 详情见 CUSTOMBUTTONTYPES  utils/common/consts.js
    * @click {Function}  子组件回调父组件方法
 */ -->
  <div class="btnwrap">
    <el-button v-if="type" :class="type + '-btn'" class="btn" @click="clickHandle" :loading="loading"></el-button>
    <el-button v-else type="primary" @click="clickHandle" :loading="loading">确 定</el-button>
    <!-- <div :class="type + '-btn'" @click="clickHandle" :disabled="loading"></div> -->
  </div>
</template>

<script>
export default {
  name: 'customButton',
  data () {
    return {
      loading: false
    }
  },
  props: {
    type: {
      type: String
      // required: true
    }
  },
  methods: {
    clickHandle () {
      let isClick = true
      let that = this
      if (isClick) {
        isClick = false
        this.loading = true
        this.$emit('click')
        // 定时器
        this.timeout = setTimeout(function () {
          isClick = true
          that.loading = false
        }, 1000)// 一秒内不能重复点击
      } else {
        this.loading = true
      }
    }
  },
  beforeDestroy () {
    clearTimeout(this.timeout)
  }
}
</script>

<style scoped lang="less">
.btnwrap{
  line-height: initial;
  display: inline-block;
}
.btn-util-cls() {
  background-size: contain;
  background-position: left top;
  min-width: 72px;
  min-height: 32px;
  cursor: pointer;
  padding:0;
  border-radius: 0;
  width:initial;
  height: initial;
  border: 0;
}
.el-button--primary:focus, .el-button--primary:hover {
    border: 0 solid transparent!important;
}
.btn:hover{
  padding:0;
  border-radius: 0;
  width:initial;
  height: initial;
  border: 0;
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
.batch-delete-btn {
  background: url(../../assets/img/button/icon-delete-batch.png) left top no-repeat;
  .btn-util-cls;
  min-width: 108px;
  min-height: 32px;
}
.query-btn {
  background: url(../../assets/img/button/button-search.png) left top no-repeat;
  .btn-util-cls;
  min-height: 32px;
}

.reset-btn {
  background: url(../../assets/img/button/button-reset.png) left top no-repeat;
  .btn-util-cls;
  min-width: 77px;
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
.cuizhong-btn {
  background: url(../../assets/img/button/button-cuizhong.png) left top no-repeat;
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
.sync-btn {
  background: url(../../assets/img/button/button-sync.png) left top no-repeat;
  .btn-util-cls;
  min-width: 106px;
}

</style>
