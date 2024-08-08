<template>
  <!-- 模板通知单 -->
  <el-dialog :title="formData.title" :visible.sync="visible" width="800px" :before-close="close"
    :close-on-click-modal="false" class="userManageFormDialog" :lock-scroll="true" :append-to-body="true">
    <div style="height:700px">
      <formwork id="printArea" :formData="formData" :title="formData.title" :afterInfo="false" style="width:700px"></formwork>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" v-print="print">打 印</el-button>
    </span>
  </el-dialog>
</template>
<script>
import formwork from '@/components/common/formwork'
import { queryNoticeMess } from '@/utils/axios/apis/notice/index'
export default {
  name: 'publishManageDialog',
  components: { formwork },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData:{
      type:Object,
      default:function(){return {}}
    },
  },
  watch: {
    visible(v) {
      if (v) {
        let params = {
          noticeType: this.formData.maintype,
          schId: this.formData.schId
        }
        queryNoticeMess(params).then(res => {
          // this.formData=Object.assign({},this.formData,{
          //   'content':res.data[0].noticeContent,
          // })
          this.$set(this.formData,'content',res.data[0].noticeContent)
        })
      }
    }
  },
  data () {
    return {
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
    close () {
      this.$emit('close')
    }
  }
}
</script>
<style>
.userManageFormDialog .el-dialog__body {
  overflow: auto !important;
}

</style>