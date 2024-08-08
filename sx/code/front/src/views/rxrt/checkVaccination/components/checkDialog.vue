<template>
  <!-- 打印查验证明 -->
  <el-dialog title="打印查验证明" :visible.sync="visible" width="850px" :before-close="close" :close-on-click-modal="false"
    class="userManageFormDialog" :lock-scroll="true" :append-to-body="true">
    <div class="check-content" v-for="(item, idx) in dataList" :key="idx">
      <check-model :checkData="item"></check-model>
    </div>
    <div v-show="printPaper">
      <vue-easy-print tableShow ref="easyPrint">
        <template slot-scope="func">
          <div class="check-content" style="page-break-after: always" v-for="(item, idx) in dataList" :key="idx">
            <check-model-paper id="printArea" :checkData="item"></check-model-paper>
          </div>
        </template>
      </vue-easy-print>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <!-- <el-button type="primary" v-print="print">打 印</el-button> -->
      <el-button type="primary" @click="printPaperData">打 印</el-button>
    </span>
  </el-dialog>
</template>
<script>
import checkModel from './checkModel.vue'
import checkModelPaper from './checkModelPaper.vue'
export default {
  name: 'checkDialog',
  components: { checkModel, checkModelPaper },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    dataList: {
      type: Array,
      default: []
    }
  },
  watch: {
    visible(v) {
      if (v) {
        this.printPaper = false;
        this.dataList2 = [];
        for (let i = 0; i < this.dataList.length; i++) {
          this.dataList2.push(JSON.parse(JSON.stringify(this.dataList[i])));
        }
        this.printPaper = true;
      }
    }
  },
  data() {
    return {
      dataList2: [],
      printPaper: false,
      print: {
        id: 'printArea',
        popTitle: '打印', // 打印配置页上方标题
        extraHead: '', //最上方的头部文字，附加在head标签上的额外标签,使用逗号分隔
        preview: '', // 是否启动预览模式，默认是false（开启预览模式，可以先预览后打印）
        previewTitle: '', // 打印预览的标题（开启预览模式后出现）,
        previewPrintBtnLabel: '', // 打印预览的标题的下方按钮文本，点击可进入打印（开启预览模式后出现）
        zIndex: '', // 预览的窗口的z-index，默认是 20002（此值要高一些，这涉及到预览模式是否显示在最上面）   
        previewBeforeOpenCallback() { this.isPrint=false;}, //预览窗口打开之前的callback（开启预览模式调用）
        previewOpenCallback() { this.isPrint=false;}, // 预览窗口打开之后的callback（开启预览模式调用）
        beforeOpenCallback() {
          this.isPrint=false;
        }, // 开启打印前的回调事件
        openCallback() {
          this.isPrint=true;
        }, // 调用打印之后的回调事件
        closeCallback() {}, //关闭打印的回调事件（无法确定点击的是确认还是取消）
        url: '',
        standard: '',
        extraCss: '',
      },
    }
  },
  methods: {
    printPaperData() {
      let that = this;
      that.printPaper = true;
      that.$refs.easyPrint.print();
      that.close();
    },
    close() {
      this.$emit('close')
      // this.content = ''
      // this.queryNode = {}
    }
  }
}
</script>
<style>
.userManageFormDialog .el-dialog__body {
  overflow-y: auto !important;
}
</style>