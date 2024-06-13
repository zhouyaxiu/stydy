<template>
  <div class="main-content"  @mouseenter="stop()" @mouseleave="start()">
    <Transition v-for="(build, index) in list" :key="selectIndex">
        <div class="banner-scroll-wrap" v-show="index === selectIndex">
            卡片自定义内容{{ index }}
        </div>
    </Transition>
</div>
</template>
  
<script setup>
const selectIndex = ref(0)
const list = ref(
  [{ name: "卡片1", id: 1 }, { name: "卡片1", id: 2 }, { name: "卡片1", id: 2 }]
)

// #计时器实例
let timer = null

// >计时器逻辑
const timeFuc = () => {
    timer = setInterval(() => {
      // 更改选中的index
      if (selectIndex.value >= list.value.length - 1) {
        selectIndex.value = 0
      } else {
        selectIndex.value++
      }
    }, 5000)
}
// >开启轮播
const start = () => {
    if (timer) return
    timeFuc()
  }

  // >关闭轮播
  const stop = () => {
    clearInterval(timer)
    timer = null
  }

timeFuc()

onMounted(() => {
    document.addEventListener('visibilitychange', () => {
      // 用户息屏、或者切到后台运行 （离开页面）
      if (document.visibilityState === 'hidden') {
        stop()
      }
      // 用户打开或回到页面
      if (document.visibilityState === 'visible') {
        start()
      }
    })
  })
    
  onBeforeUnmount(() => stop())
</script>

<style lang="scss" scoped>
.main-content {
    position: relative;
    height: 500px;
    width:500px;
    background:#fff;
    .banner-scroll-wrap {
        position: absolute;
        top: 0;
        bottom: 0;
        right: 0;
        left: 0;
    }
}

.v-enter-from {
    transform: translateX(100%);
    opacity: 0;
}

.v-enter-active,
.v-leave-active {
    transition: transform 1600ms ease-in-out, opacity 1600ms ease-in-out;
}

.v-enter-to {
    transform: translateX(0);
    opacity: 1;
}

.v-leave-from {
    transform: translateX(0);
    opacity: 1;
}

.v-leave-to {
    transform: translateX(-100%);
    opacity: 0;
}
</style>