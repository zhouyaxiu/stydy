<template>
  <PermComp :roles="roles">
            <button>admin权限按钮</button>
        </PermComp>
  <div id="animation"></div>
  <el-input v-model="password" autocomplete="none" />
    <div class="strength-meter-bar">
      <div class="strength-meter-bar--fill" :data-score="passwordStrength"></div>
    </div>
  <div class="main-content"  @mouseenter="stop()" @mouseleave="start()">
    <Transition v-for="(build, index) in list" :key="selectIndex">
        <div class="banner-scroll-wrap" v-show="index === selectIndex">
            卡片自定义内容{{ index }}
        </div>
    </Transition>
    
</div>
</template>
  
<script setup>
import PermComp from '@/utils/directive/PermComp.js';
// 后台返回的角色权限列表
const roles = ['admin', 'apadmin'];
import { zxcvbn } from '@zxcvbn-ts/core';
import lottie from 'lottie-web';
import autofit from 'autofit.js'
const selectIndex = ref(0)
const list = ref(
  [{ name: "卡片1", id: 1 }, { name: "卡片1", id: 2 }, { name: "卡片1", id: 2 }]
)
const password = ref('');
const passwordStrength = computed(() => {
  return zxcvbn(password.value).score;
});
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

    const dom = document.getElementById("animation");
        lottie.loadAnimation({
            container: document.getElementById('animation'), // 动效图容器的元素
            loop: true, // 是否循环播放
            autoplay: true, // 是否自动播放
            // rendererSettings: {
            //     preserveAspectRatio: 'xMidYMid slice',
            //     quality: 'high', // 提高图片质量
            // },
            // path: 'animation.json' // JSON文件的路径 =》 public文件夹中
        });
        let options = {
            designHeight: 1080, // 设计稿的宽度
            designWidth: 1920, // 设计稿的高度
            renderDom: ".home-container", // 要渲染的容器
        }
        autofit.init(options)
  })
    
  onBeforeUnmount(() => stop())
</script>

<style lang="scss" scoped>
#animatiion{
    width: 55%;
    height:50%;
    /* ... */
}
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

.strength-meter-bar {
  position: relative;
  height: 6px;
  margin: 10px auto 6px;
  border-radius: 6px;
  background-color: rgb(0 0 0 / 25%);

  // 增加的伪元素样式代码
  &::before,
  &::after {
    content: '';
    display: block;
    position: absolute;
    z-index: 10;
    width: 20%;
    height: inherit;
    border-width: 0 5px;
    border-style: solid;
    border-color: #fff;
    background-color: transparent;
  }
  &::before {
    left: 20%;
  }
  &::after {
    right: 20%;
  }
  // 增加的伪元素样式代码

  &--fill {
    position: absolute;
    width: 0;
    height: inherit;
    transition:
      width 0.5s ease-in-out,
      background 0.25s;
    border-radius: inherit;
    background-color: transparent;

    &[data-score='0'] {
      width: 20%;
      background-color: darken(#e74242, 10%);
    }

    &[data-score='1'] {
      width: 40%;
      background-color: #e74242;
    }

    &[data-score='2'] {
      width: 60%;
      background-color: #efbd47;
    }

    &[data-score='3'] {
      width: 80%;
      background-color: fade(#55d187, 50%);
    }

    &[data-score='4'] {
      width: 100%;
      background-color: #55d187;
    }
  }
}
</style>