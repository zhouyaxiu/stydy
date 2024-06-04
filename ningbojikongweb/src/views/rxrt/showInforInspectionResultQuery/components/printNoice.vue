<template>
  <!-- 模板通知单 -->
  <el-dialog :title="title" :visible.sync="visible" width="800px" :before-close="close" :close-on-click-modal="false"
    class="manualForm" :lock-scroll="true" :append-to-body="true">
    <div style="height:700px" v-for="(item, index) in noiceList" :key="index" id="printAreaManual">
      <filing-notice :title="title" :isAfter="isAfter" :formData="item" style="width:700px"></filing-notice>
    </div>
    <div v-show="printPaper">
      <vue-easy-print tableShow ref="easyPrint">
        <template slot-scope="func">
          <div v-for="(item, idx) in paperList" :key="idx">
            <filing-notice-paper :title="title" :isAfter="isAfter" :formData="item"
              style="width:700px"></filing-notice-paper>
          </div>
        </template>
      </vue-easy-print>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="printPaperData">打 印</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { queryNoticeMess } from '@/utils/axios/apis/notice/index'
import filingNotice from '@/components/common/filingNotice'
import filingNoticePaper from '@/components/common/filingNoticePaper'

export default {
  name: 'printNoice',
  components: { filingNotice, filingNoticePaper },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '打印建档通知单'
    },
    isAfter: {
      type: Boolean,
      default: false
    },
    // 批量打印数组
    noiceList: {
      type: Array,
      default: []
    }
  },
  watch: {
    visible(v) {
      if (v) {
        this.printPaper = false;
        this.paperList = []
        for (let i = 0; i < this.noiceList.length; i++) {
          this.paperList.push(JSON.parse(JSON.stringify(this.noiceList[i])));
        }
        // console.log(this.noiceList)
        // console.log(this.paperList)

      }
    }
  },
  data() {
    return {
      formData: {
        noticeContent: ''
      },
      paperList: [],
      printPaper: false,
      print: {
        id: 'printAreaManual',
        popTitle: '打印', // 打印配置页上方标题
        extraHead: '', //最上方的头部文字，附加在head标签上的额外标签,使用逗号分隔
        preview: '', // 是否启动预览模式，默认是false（开启预览模式，可以先预览后打印）
        previewTitle: '', // 打印预览的标题（开启预览模式后出现）,
        previewPrintBtnLabel: '', // 打印预览的标题的下方按钮文本，点击可进入打印（开启预览模式后出现）
        zIndex: '', // 预览的窗口的z-index，默认是 20002（此值要高一些，这涉及到预览模式是否显示在最上面）   
        previewBeforeOpenCallback() { this.isPrint = false; }, //预览窗口打开之前的callback（开启预览模式调用）
        previewOpenCallback() { this.isPrint = false; }, // 预览窗口打开之后的callback（开启预览模式调用）
        beforeOpenCallback() {
          this.isPrint = false;
        }, // 开启打印前的回调事件
        openCallback() {
          this.isPrint = true;
        }, // 调用打印之后的回调事件
        closeCallback() { }, //关闭打印的回调事件（无法确定点击的是确认还是取消）
        url: '',
        standard: '',
        extraCss: '',
      },
    }
  },
  methods: {
    close() {
      this.$emit('close')
    },
    printPaperData() {
      let that = this;
      that.printPaper = true;
      that.$refs.easyPrint.print();
      that.close();
    },
  }
}
</script>
<style>
.manualForm .el-dialog__body {
  overflow: auto !important;
}
</style>