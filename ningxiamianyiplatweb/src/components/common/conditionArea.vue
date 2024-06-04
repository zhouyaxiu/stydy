<template>
  <div class="condition-area">
    <div class="title-area">
      <div class="title-bg">
        <img src='@/assets/img/index/title_area.png'>
      </div>
      <div class="title">
        <!-- <div class="back-btn" v-show="showBackBtn" @click="clickBack"></div> -->
        <div class="title-content">{{ store.currentTitle }}</div>
      </div>
      <div class="fold-btn" @click="clickHandler" v-if="isShowBtn">
        <img class="btn-hide" v-show="!isShow" src='@/assets/img/index/btn_show.png'>
        <img class="btn-hide" v-show="isShow" src='@/assets/img/index/btn_hide.png'>
      </div>
    </div>
    <div class="search-area" v-show="isShow && isShowBtn">
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, toRefs } from 'vue';
import { mainStore } from "@/store";
const store = mainStore()
const props = defineProps({
  isShowBtn: {
    type: Boolean,
    default: true
  }
})
const data = reactive({});

const isShow = ref(true)
const emits = defineEmits(['setConditionFlag'])
const clickHandler = () => {
  isShow.value = !isShow.value;
  emits("setConditionFlag");
}
// 使用toRefs解构
defineExpose({
  ...toRefs(data)
})

</script>

<style scoped lang='scss'>
.condition-area {
  width: 100%;
  margin-bottom: 16px;
  background-color: #fff;

  .btn-hide {
    height: 33px;
    width: 66px;
  }

  .title-area {
    display: flex;
    width: 100%;
    height: 36px;
    align-items: center;
    line-height: 36px;
    background: url(@/assets/img/index/condition_area_bg.png) left top no-repeat;
    background-size: 100% 100%;
    background-position: left top;

    .title {
      display: flex;
      padding-left: 14px;

      .title-content {
        min-width: 300px;
        height: 36px;
        font-size: 15px;
        font-weight: bold;
        line-height: 36px;
        color: #03134E;
      }
    }

    .title-bg {
      display: flex;
      padding-left: 36px;

      img {
        width: 24px;
        height: 24px;
      }
    }

    .fold-btn {
      position: absolute;
      display: flex;
      right: 16px;
      font-size: 14px;
      color: #3385FF;
      cursor: pointer;
    }
  }

  .search-area {
    padding: 10px 16px 16px 16px;
    border-radius: 0px 0px 6px 6px;
  }
}
</style>