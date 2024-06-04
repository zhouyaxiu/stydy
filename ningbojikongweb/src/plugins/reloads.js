import store from '../store/store.js'
import * as types from '../store/types.js'
import { modules } from '@/plugins/menu'
export default {
  install: () => {
    // 页面刷新时，重新赋值token
    if (sessionStorage.getItem('token')) {
      try {
        // store.commit(types.LOGIN, JSON.parse(window.sessionStorage.getItem('currentUserInfo')))
        store.commit(types.LOGIN, JSON.parse(window.sessionStorage.getItem('response')))
        // store.dispatch(types.SET_USERAREA_ACTION, JSON.parse(window.sessionStorage.getItem('currentUserInfo')))
        let modules2;
        if (window.sessionStorage.getItem('areaLevel') !== "4") {
          modules2 = modules.filter(item => item.id != 'M2')
          modules2 = modules2.filter(item => item.id != 'M6')
          modules2 = modules2.filter(item => item.id != 'M11')
        } else {
          modules2 = modules
        }
        store.dispatch(types.SET_USERAREA_ACTION, modules2)
        store.commit(types.USERNAME, {
          userName: window.sessionStorage.getItem('userName'),
          userCode: window.sessionStorage.getItem('userCode'),
          areaname: window.sessionStorage.getItem('areaname'),
          areaCode: window.sessionStorage.getItem('areacode'),
          // areaLevel: window.sessionStorage.getItem('areaLevel'),
          accountName: window.sessionStorage.getItem('account'),
          loginname: window.sessionStorage.getItem('account'),
          baseCode: window.sessionStorage.getItem('baseCode'),
          areaId: window.sessionStorage.getItem('areaId'),
          id: window.sessionStorage.getItem('id')
        })
        store.commit(types.ACCOUNT, window.sessionStorage.getItem('account'))
        // store.commit(types.MODELOBJECT, window.sessionStorage.getItem('response'))
        store.commit(types.SET_TITLE, {
          moduleId: window.sessionStorage.getItem('moduleId'),
          title: window.sessionStorage.getItem('title'),
          breadcrumb: window.sessionStorage.getItem('breadcrumb')
        })
      } catch (e) { }
    }
  }
}
