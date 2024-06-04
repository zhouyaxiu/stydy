<template>
    <el-config-provider :locale="locale">
        <fullScreen>全屏</fullScreen>
        <router-view />
    </el-config-provider>
</template>

<script setup>
import { ElLoading, ElConfigProvider } from 'element-plus'
import fullScreen from '@/components/common/fullScreen.vue'
import { mainStore } from "@/store";
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import { useRoute } from "vue-router";

const router = useRoute();
const routeKey = computed(() => router.path + Math.random());
//Element-plus 汉化
const locale = zhCn
const store = mainStore()
let loading = ref(null)
let options = reactive({
    fullscreen: true,
    lock: true,
    text: '处理中，请稍候….'
})
// watch(
//     () => store.loadingAxiosFlag,
//     (val) => {
//         if (val) {
//             loading = ElLoading.service(options)
//         } else {
//             loading.close()
//         }
//     },
// )
// watch(
//     () => store.loadingPageFlag,
//     (val) => {
//         if (val) {
//             loading = ElLoading.service(options)
//         } else {
//             loading.close()
//         }
//     },

// )
onMounted(() => {
    window.document.documentElement.setAttribute('data-theme', 'default')
    // sessionStorage.setItem("lastWidth", window.outerWidth);
    // window.onresize = () => {
    //     setTimeout(function () {
    //         if (!sessionStorage.lastWidth || sessionStorage.lastWidth != window.outerWidth
    //             || window.screen.availWidth < parseInt(sessionStorage.lastWidth)) {
    //             sessionStorage.setItem("lastWidth", window.outerWidth);
    //             window.location.reload()
    //         }
    //     }, 200)
    // }
})


</script>

<style>
@import "./assets/css/main.css";
@import "./assets/css/table.css";
@import "./assets/css/icons.css";
@import "./assets/css/theme/index.scss"
</style>
