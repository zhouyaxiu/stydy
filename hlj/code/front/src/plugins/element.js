import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// import * as ElementPlusIconsVue from '@element-plus/icons-vue'

export default (app) => {
  app.use(ElementPlus)
  //导入所有图标并进行全局注册（不需要可注释此处，在页面按需引入）
  // for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  //   app.component(key, component)
  // }
}
