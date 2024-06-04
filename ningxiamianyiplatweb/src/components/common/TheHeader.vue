<template>
  <div class="header" v-watermark>
    <div class="header-left">
      <div class="header-text" v-if="isCKJC === false">免疫规划信息系统</div>
      <div class="header-text" v-else>免疫规划产院接种信息系统</div>
    </div>
    <div class="header-right">
      <div v-if="isCKJC === false" class="logout-area" @click="logoutFun()">
        <ss-icon iconType="logout" class='logout-icon'></ss-icon>
        <span>退出系统</span>
      </div>
      <div v-else class="logout-area" @click="goBack()">
        <ss-icon iconType="back" class='logout-icon'></ss-icon>
        <span>导航中心</span>
      </div>
      <div class="split-line"></div>
      <div class="account-area">
        <div class="account-text" :title="userInfo.userName">{{ userInfo.userName }}</div>
        <div class="account-text">
          <span>{{ userInfo.areaName }}</span>
          <span style="margin-left:10px;">{{ userInfo.areaCode }}</span>
        </div>
      </div>
    </div>
  </div>
  <!-- <Watermark text="前端百事通">
    <div class="content"></div>
  </Watermark> -->
</template>
<script setup>
import { useRouter } from "vue-router";
import { ElMessage } from 'element-plus'
import { logout } from '@/api/apis/login';
import { mainStore } from "@/store/index";
// import Watermark from '@/components/common/waterMark.vue';
// import * as waterMark from '@/utils/common/waterMark'
// waterMark.setWaterMark()
const store = mainStore()
const userInfo = JSON.parse(sessionStorage.getItem('userInfo'))
const isCKJC = ref(window.ISCKJC)
const jcptPath = ref(window.JCPT_URL)

// 用户名下拉菜单选择事件
const router = useRouter();
const logoutFun = () => {
  logout().then(res => {
    if (res.status == 200) {
      store.clearStore()
      router.push("/login");
      ElMessage.success("退出成功")
    }
  }).catch(err => {
    ElMessage.error("退出失败")
  })
}
const goBack = () => {
  store.clearStore()
  window.location.replace(jcptPath.value)
}
</script>
<style scoped lang="scss">
.header {
  height: 64px;
  background: #2964ff;
  color: #fff;

  .header-left {
    background: url(@/assets/img/index/image-bg.png);
    background-size: contain;
    background-position: left top;
    background-repeat: no-repeat;
    min-width: 940px;
    min-height: 64px;

    .header-text {
      padding-left: 66px;
      font-size: 30px;
      font-weight: bold;
      line-height: 64px;
      letter-spacing: 6px;
      text-shadow: 1px 3px 3px #0E50C4;
      color: #FFFFFF;
    }
  }


  .header-right {
    display: flex;
    position: absolute;
    justify-content: flex-end;
    align-items: center;
    right: 30px;
    top: 0;
    font-size: 14px;
    font-weight: 400;

    .logout-area {
      cursor: pointer;
      line-height: 64px;
      display: flex;
      align-items: center;
    }

    .split-line {
      border-left: 1px solid #fff;
      height: 18px;
      margin: 0 16px;
    }

    .account-area {
      line-height: 24px;
    }

    .account-text {
      display: flex;
      justify-content: flex-end;
    }

    .logout-icon {
      margin-right: 8px;
    }
  }
}
</style>
