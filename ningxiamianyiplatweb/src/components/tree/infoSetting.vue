 <!-- 信息展示设置--用于页面弹窗
 /** 父组件传参
    * height {String}  弹窗的高度
 */ -->
<template>
  <div class="info-show">
    <el-button round plain type="primary" @click="open">信息显示设置</el-button>
    <el-dialog title="信息显示设置" v-model="dialogVisible" width="65%" :destroy-on-close="true" style="height: 70%;">
      <ss-info-setting-show :showCloseBtn="true" :height="height" />
    </el-dialog>
  </div>
</template>

<script setup>
import { mainStore } from "@/store/index";
const store = mainStore()
const dialogVisible = ref(false)
const props = defineProps({
  height: {
    default: '300px'
  }
})
watchEffect(() => {
  if (!store.infoSettingClose) {
    dialogVisible.value = false
    store.setInfoSettingClose(true)
  }
})
const open = () => {
  dialogVisible.value = true
}
</script>

<style>
.info-show .el-dialog__body {
  height: calc(100% - 54px);
}
</style>
