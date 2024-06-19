<template>
    <div :class="['flip-layout', props.isBack && 'active']">
      <div class="flip-panel">
        <!-- 正面 -->
        <div class="flip-container front">
          <slot></slot>
        </div>
        <!-- 背面 -->
        <div class="flip-container back">
          <slot name="back"></slot>
        </div>
      </div>
    </div>
  </template>
  <script setup>
    const props=defineProps({
            isBack:{
            type:Boolean,
            default:false
        },
    })

</script>
  <style lang="scss" scoped>
  .flip-layout{
    width: 100%;
    height: 100px;
    perspective: 3000px;
    overflow: hidden;
    &.active{
      .flip-panel{
        transform: rotateY(180deg); // 点击时翻转正反面
      }
    }
    .flip-panel{
      width: 100%;
      height: 100%;
      transition: all 1s ease; // 添加翻转过渡效果
      transform-style: preserve-3d; // 子元素将保留其 3D 位置
      .flip-container{
        width: 100%;
        height: 100%;
        position: absolute; // 让背面和正面重叠
        left: 0;
        top: 0;
        // 背面
        &.back{
          backface-visibility: hidden; // 背对屏幕时隐藏
          transform: rotateY(180deg); // 子元素内容先旋转至背面
        }
      }
      .front {
        backface-visibility: hidden; // 背对屏幕时隐藏
        z-index: 10;
      }
    }
  }
  
  </style>