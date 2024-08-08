<template>
  <div>
  </div>
</template>

<script setup>
import { ref,onMounted, getCurrentInstance, onActivated } from 'vue';
import { onBeforeRouteEnter } from "vue-router";
  //自定义全局公共方法引用
  const { appContext } = getCurrentInstance();
  const globalProxy = appContext.config.globalProperties;
  import { mainStore } from "@/store/store";
  const store = mainStore();

  import router from '@/router'
  // import store from '@/store/store'
  // import * as types from '@/store/types';

  const setError=()=>{
    globalProxy.$util.errorMsg("您没有权限查看该页面！");
    globalProxy.$router.go(-1);
  }
  onMounted(()=> {
    setError();
  })
  onActivated(()=> {
    setError();
  })
  onBeforeRouteEnter((to, from, next) =>{
    if (!from || !from.name) {
      store.commit(types.SET_ERROR,"您没有权限查看该页面！");
      router.push({name: 'errorPage'});
      return;
    }
    next();
  })
</script>

