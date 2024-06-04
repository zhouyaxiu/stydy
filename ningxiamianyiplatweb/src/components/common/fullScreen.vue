<template>
  <el-button v-if="enableFullscreen" @click="changeFullscreen">
    <span v-if="fullscreen">退出全屏</span>
    <span v-else>全屏</span>
  </el-button>
</template>

<script setup>
import { FullscreenFilled, FullscreenExitFilled } from "@vicons/material"
import { onMounted } from "vue";

// 当浏览器不支持全屏模式时，隐藏全屏按钮
const enableFullscreen = document.fullscreenEnabled
const fullscreen = ref(document.fullscreenElement === document.documentElement);
const changeFullscreen = () => {
  if (!fullscreen.value) {
    // 进入全屏
    if (document.fullscreenElement != null) {
      return
    }
    document.documentElement.requestFullscreen()
    return
  }
  // 退出全屏
  if (document.fullscreenElement !== document.documentElement) {
    return
  }
  document.exitFullscreen()
}

// 禁用f11按下后的原生全屏，改由组件控制
const onnativeFullscreenChange = (event) => {
  if (event.key === 'F11') {
    event.preventDefault()
    changeFullscreen()
  }
}

// 通过监听事件来改变组件中的状态，而不是直接再调用API后更改，可避免通过按键退出时导致状态未变化
const updateFullscreenStatus = () => {
  fullscreen.value = document.fullscreenElement != null
}

onMounted(() => {
  document.addEventListener("keydown", onnativeFullscreenChange)
  document.addEventListener("fullscreenchange", updateFullscreenStatus)
})

onUnmounted(() => {
  document.removeEventListener('keydown', onNativeFullscreenChange)
  document.removeEventListener('fullscreenchange', updateFullscreenStatus)
})

</script>


<style scoped></style>
