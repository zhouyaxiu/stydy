<template>
  <div class="sidebar">
    <div class="flex">
      <div class="sider-scrollbar">
        <el-menu class="sidebar-el-menu" v-if="!sidebar.collapse" :default-active="onRoutes" background-color="#fff"
          text-color="#606266" active-text-color="#20a0ff" unique-opened router>
          <!-- <TheSidebarItem v-for="item in menuList" :key="item.index" :level="1" :item="item" /> -->
          <menu-item v-for="item in menuList" :key="item.index" :item="item" :level="1"></menu-item>
        </el-menu>
      </div>
      <!-- 折叠按钮 -->
      <div class="menu-fold-btn" :class="{ 'menu-collapse': sidebar.collapse }" @click="collapseChage">
        <div class="btn-bg">
          <div v-if="!sidebar.collapse" class="btn-zk"></div>
          <div v-else class="btn-zd"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useSidebarStore } from '../../store/sidebar'
import { useRoute } from "vue-router";
import { mainStore } from "@/store/index";
import TheSidebarItem from "./TheSidebarItem.vue"
import menuItem from "./menuItem.vue";
const store = mainStore()
const route = useRoute();
const menuList = store.getMenus
const onRoutes = computed(() => {
  return route.path;
});
const sidebar = useSidebarStore();
// 侧边栏折叠
const collapseChage = () => {
  sidebar.handleCollapse();
};

onMounted(() => {
  if (document.body.clientWidth < 1500) {
    collapseChage();
  }
});

</script>
