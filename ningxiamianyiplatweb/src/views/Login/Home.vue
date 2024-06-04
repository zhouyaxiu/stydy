<template>
  <div class="about">
    <v-header />
    <v-sidebar />
    <div class="content-box" :class="{ 'content-collapse': sidebar.collapse }">
      <div class="content">
        <el-scrollbar>
          <!-- <transition name="ss-animate" mode="out-in">
            <keep-alive include="home">
              <router-view v-slot="{ Component, route }">
                <component :is="Component" :key="route.name" />
              </router-view>
            </keep-alive>
          </transition> -->
          <router-view v-slot="{ Component, route }">
            <transition name="ss-animate" mode="out-in">
              <keep-alive include="home">
                <div :key="route.name">
                  <component :is="Component" />
                </div>
              </keep-alive>
            </transition>
          </router-view>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>
<script setup>
import { useSidebarStore } from '../../store/sidebar'
import vHeader from "../../components/common/TheHeader.vue";
import vSidebar from "../../components/common/TheSidebar.vue";
const sidebar = useSidebarStore();


</script>
<style scoped>
.about {
  height: 100%;
}
</style>