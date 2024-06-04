import { mainStore } from "@/store/index";
export default {
  install: () => {
    // 页面刷新时，重新赋值token
    if (sessionStorage.getItem('token')) {
      const store = mainStore()
      try {
        store.userInfo = JSON.parse(sessionStorage.getItem('userInfo'))
        store.moduleId = sessionStorage.getItem('moduleId')
        store.currentLevel = sessionStorage.getItem('currentLevel')
        store.titleIndex = sessionStorage.getItem('titleIndex')
        store.title = sessionStorage.getItem('title')
        store.breadcrumb = sessionStorage.getItem('breadcrumb')
        store.sessionid = sessionStorage.getItem('sessionid')
        store.treeArr = JSON.parse(sessionStorage.getItem('treeArr'))
      } catch (e) { }
    }
  }
}
