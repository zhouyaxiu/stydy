<template>
  <!-- 模板通知单 -->
  <el-dialog :title="title" :visible.sync="visible" width="800px" :before-close="close" :close-on-click-modal="false"
    class="manualForm">
    <div style="height:500px" v-for="(item, index) in noiceList" :key="index" id="printAreaManual">
      <filing-notice :isAfter="isAfter" :formData="item" style="width:700px" :title="title"></filing-notice>
    </div>
    <div v-show="printPaper">
      <vue-easy-print tableShow ref="easyPrint">
        <template slot-scope="func">
          <div v-for="(item, idx) in paperList" :key="idx" style="margin:0"
            :class="paperList.length > 1 ? 'pageAfter' : ''">
            <filing-notice-paper :isAfter="isAfter" :formData="item" style="width:700px"
              :title="title"></filing-notice-paper>
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
      default: '打印补证通知单'
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

.pageAfter {
  page-break-after: always;
}
</style>