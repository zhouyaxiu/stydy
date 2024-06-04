<template>
    <router-view />
</template>

<script setup>
import { reactive, ref, watch, onMounted } from "vue";
import { ElLoading } from 'element-plus'
import { mainStore } from "@/store";
const store = mainStore()
let loading = ref(null)
let options = reactive({
    fullscreen: true,
    lock: true,
    text: '加载中'
})
watch(
    // ps：去掉loading效果以后需要再加
    () => store.loadingAxiosFlag,
    (val) => {
        if (val) {
            // loading = ElLoading.service(options)
        } else {
            // loading.close()
        }
    },
)
watch(
    () => store.loadingPageFlag,
    (val) => {
        if (val) {
            loading = ElLoading.service(options)
        } else {
            loading.close()
        }
    },

)


</script>

<style>
@import "./assets/css/main.css";
</style>
