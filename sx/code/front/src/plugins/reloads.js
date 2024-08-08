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
        let modules2;
        let modulesNew=JSON.parse(JSON.stringify(modules));
        if(window.sessionStorage.getItem('areaLevel') !== "4"){
          if(window.sessionStorage.getItem('userType')==1){
            modulesNew[1].supModules = modulesNew.filter(item => item.id=='M2')[0].supModules.filter(itm=>itm.id!='M29')
          }
          modulesNew[1].supModules = modulesNew.filter(item => item.id=='M2')[0].supModules.filter(itm=>itm.id!='M23')
          modulesNew[1].supModules = modulesNew.filter(item => item.id=='M2')[0].supModules.filter(itm=>itm.id!='M28')
          modulesNew[2].supModules = modulesNew.filter(item => item.id=='M3')[0].supModules.filter(itm=>itm.id!='M34')
          modules2 = modulesNew
        }else{
          if(window.sessionStorage.getItem('userType')==1){
            modulesNew[1].supModules = modulesNew.filter(item=>item.id=='M2')[0].supModules.filter(itm=>itm.id != 'M29')
          }
          modules2 = modulesNew
        }
        // modules2 = modules
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
          id: window.sessionStorage.getItem('id'),
          userType: window.sessionStorage.getItem('userType'),
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
