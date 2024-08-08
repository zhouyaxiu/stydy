// import './assets/main.css'

// import { createApp } from 'vue'
// import App from './App.vue'
// import router from './router'

// const app = createApp(App)

// app.use(router)

// app.mount('#app')

import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";
import './assets/css/public.css'
import './assets/css/icons.css'
import './assets/css/common.css'
import './assets/css/customScrollBar.css'
import installElementPlus from "./plugins/element";

import installUtilFun from "./utils/common/index";
import * as echarts from 'echarts';
import divArea from "@/components/common/divArea.vue";

const pinia = createPinia(); //状态管理
const app = createApp(App);

app.config.globalProperties.$echarts = echarts;
app.component("ss-div-area", divArea);

installElementPlus(app);
// installComponent(app); //公共组件加载
installUtilFun(app); //全局挂载公共方法
app.use(router).use(pinia).mount("#app");
