<template>
  <el-dialog
    title="消息明细"
    :visible.sync="visible"
    width="60%"
    :before-close="close"
    :close-on-click-modal="false"
    class="notifydialog"
  >
    <div class="areaInfoDialog">
      <el-form label-width="150px" ref="unitManageForm" inline>
        <ss-common-area-group :columnCount="1" style="width:100%;padding-left: 250px;">
          <ss-common-area-item label="消息日期:" prop="notifyTime">
            <el-input v-model="notifyInfo.notifyTime" disabled style="width: 250px" maxlength="10"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="消息标题:" prop="notifyTitle">
            <el-input v-model="notifyInfo.notifyTitle" disabled style="width: 250px" maxlength="10"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="消息内容:" prop="notifyContext">
            <el-input v-html="notifyInfo.notifyContext" disabled style="width: 550px;max-height: 300px;overflow-y: auto;"></el-input>
          </ss-common-area-item>
          <!-- <ss-common-area-item label="通知状态:" prop="status">
            <el-input v-model="notifyInfo.status" disabled style="width: 250px"></el-input>
          </ss-common-area-item> -->
        </ss-common-area-group>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { getNotifyDetail } from '@/utils/axios/apis/notify/index'
export default {
  name: 'receptionInfoDialog',
  data () {
    return {
      editorVisible: false
      // notifyInfo: {},
    }
  },
  props: {
    notifyInfo: {
      type: Object,
      default: {}
    },
    visible: {
      type: Boolean,
      required: true,
      default: false
    }
  },
  // watch: {
  //   notifyId: {
  //     immediate: true,
  //     handler (val) {
  //       if (val) this.getDetailInfo()
  //     }
  //   }
  // },
  methods: {
    getDetailInfo () {
      if (!this.notifyId || this.notifyId === '') return false
      this.openFullScreen()
      getNotifyDetail({ notifyId: this.notifyId, schId: this.$store.state.schId })
        .then((res) => {
          if (res && res.data) {
            this.notifyInfo = res.data
          } else {
            this.$util.errorMsg('消息被删除或者不存在！')
          }
          this.closeFullScreen()
        })
        .catch(() => {
          this.closeFullScreen()
        })
    },
    close () {
      this.$emit('close', true)
    }
  }
}
</script>
  <style>
    .notifydialog .el-input p{
      padding:0;
      margin:0
    }
  </style>
