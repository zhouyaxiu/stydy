import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";
import installElementPlus from "./plugins/element";
import initUitlDirectives from "./utils/directive";
import installComponent from "./plugins/components";
import reloads from "./plugins/reloads";
import installUtilFun from "./utils/common/index";

// import * as ElementPlusIconsVue from "@element-plus/icons-vue";

const pinia = createPinia(); //状态管理
const app = createApp(App);

// for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
//   app.component(key, component);
// }

installElementPlus(app);
installComponent(app); //公共组件加载
installUtilFun(app); //全局挂载公共方法
app.use(router).use(pinia).use(reloads).use(initUitlDirectives).mount("#app");
