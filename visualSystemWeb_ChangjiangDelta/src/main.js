import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import installElementPlus from './plugins/element'
import * as echarts from 'echarts';
//import 'echarts-gl';
import util from './utils/util';
import drawMixin from "@/utils/drawMixin";

const pinia = createPinia()  //状态管理
const app = createApp(App)

app.config.globalProperties.$echarts = echarts
app.config.globalProperties.$util = util

installElementPlus(app)
app.use(pinia)
    .use(router)
    .mixin(drawMixin)
    .mount('#app')