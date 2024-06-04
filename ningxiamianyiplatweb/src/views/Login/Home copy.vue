<template>
  <div class="about">
    <v-header />
    <v-sidebar />
    <div class="content-box" :class="{ 'content-collapse': sidebar.collapse }">
      <div class="content">
        <el-scrollbar>
          <router-view v-slot="{ Component }" :key="routeKey">
            <transition name="ss-animate" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>
<script setup>
import { useSidebarStore } from '../../store/sidebar'
import { useRoute } from "vue-router";
import vHeader from "../../components/common/TheHeader.vue";
import vSidebar from "../../components/common/TheSidebar.vue";
const router = useRoute();
const sidebar = useSidebarStore();
const routeKey = computed(() => router.path);
</script>
<style scoped>
.about {
  height: 100%;
}
</style>