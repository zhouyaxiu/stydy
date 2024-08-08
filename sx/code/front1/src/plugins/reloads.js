import store from '../store/store.js'
import * as types from '../store/types.js'
import { modules } from '@/plugins/menu'

export default {
  install: () => {
    // 页面刷新时，重新赋值token
    if (sessionStorage.getItem('token')) {
      try {
        store.commit(types.LOGIN, JSON.parse(window.sessionStorage.getItem('currentUserInfo')))
        store.dispatch(types.SET_USERAREA_ACTION, modules)
        store.commit(types.USERNAME, JSON.parse(window.sessionStorage.getItem('currentUserInfo')))
        store.commit(types.ACCOUNT, window.sessionStorage.getItem('account'))
        store.commit(types.SET_TITLE, {
          moduleId: window.sessionStorage.getItem('moduleId'),
          title: window.sessionStorage.getItem('title'),
          breadcrumb: window.sessionStorage.getItem('breadcrumb')
        })
      } catch (e) {}
    }
  }
}
