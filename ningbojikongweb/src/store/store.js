/**
 * 模块
 */
import Vue from 'vue'
import Vuex from 'vuex'
import * as types from './types'
import rolemodules from './rolemodules.js'
import { getLevel } from '@/utils/common/area.js'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    rolemodules: rolemodules
  },
  state: {
    baseUrl: window.pathCommon,
    token: null, // 登录token
    userName: '', // 用户昵称
    userCode: '', // 用户编码
    account: '', // 用户名
    aesKey: 'mGb93nbJif8154dA', // 秘钥
    aesKey2: 'b6vhedgjior655gj', // 数据库秘钥
    errorMsg: '', // 错误提示语
    title: null, // 当前页面标题
    breadcrumb: null, // 当前页面面包屑
    pagesize: 20, // 分页每页数量
    modules: [], // 目录
    routers: [], // 权限
    id: '', // userId
    currentLevel: '',
    // 菜单页面内跳转页面集合,用于跳过路由鉴权---暂定
    redirectPages: [],
    moduleId: '',
    modelObject: sessionStorage.getItem('modelObject') || 0,//那个平台登录0：疾控端 1：门诊端 2：学校端，默认疾控端
  },
  mutations: {
    [types.LOGIN]: (state, data) => {
      if (data) {
        sessionStorage.token = data.token
        state.token = data.token
      }
    },
    [types.MODELOBJECT]: (state, data) => {
      if (data) {
        let a = data.arealevel == '4' ? 1 : 0
        state.modelObject = a
        sessionStorage.modelObject=a
      }
    },
    [types.LOGOUT]: (state) => {
      sessionStorage.removeItem('token')
      sessionStorage.removeItem('userName')
      state.token = null
      state.account = null
    },
    [types.ACCOUNT]: (state, data) => {
      sessionStorage.account = data
      state.account = data
    },
    [types.USERNAME]: (state, data) => {
      // console.log('data', data)
      sessionStorage.userName = data.userName
      console.log('?? sessionStorage.userCode = data.areacode ??')
      sessionStorage.userCode = data.areaCode ? data.areaCode : data.areacode
      sessionStorage.areaname = data.areaName ? data.areaName : data.areaname
      sessionStorage.areacode = data.areaCode ? data.areaCode : data.areacode
      sessionStorage.areacodeSelect=data.areaCode ? data.areaCode : data.areacode
      sessionStorage.areaLevelSelect=Number(getLevel(data.areaCode ? data.areaCode : data.areacode))
      sessionStorage.baseCode = data.baseCode
      sessionStorage.areaId = data.areaId
      sessionStorage.id = data.id
      sessionStorage.areaLevel = Number(getLevel(data.areaCode ? data.areaCode : data.areacode))
      state.id = data.id
      state.userName = data.userName
      state.userPassword = data.userPassword
      state.userCode = data.areaCode ? data.areaCode : data.areacode
      state.currentLevel = Number(getLevel(data.areaCode ? data.areaCode : data.areacode))
      state.areaLevel = Number(getLevel(data.areaCode ? data.areaCode : data.areacode))
      state.areaname = data.areaName ? data.areaName : data.areaname
      state.areacode = data.areaCode ? data.areaCode : data.areacode
      state.baseCode = data.baseCode
      state.areaId = data.areaId
    },
    [types.SET_ERROR]: (state, data) => {
      state.errorMsg = data
    },
    [types.SET_TITLE]: (state, data) => {
      sessionStorage.moduleId = data.moduleId
      sessionStorage.title = data.title
      sessionStorage.titleIndex = data.titleIndex
      state.moduleId = data.moduleId
      state.title = data.title
      state.breadcrumb = data.breadcrumb
      sessionStorage.breadcrumb = data.breadcrumb
      state.titleIndex = data.titleIndex
    },
    [types.RESET_PAGEBTN]: (state, data) => {
      state.curAuthorizedBtnPageName = ''
      state.permis = null
    }
  },
  actions: {
    [types.SET_USERAREA_ACTION](context, data) {
      sessionStorage.modules = JSON.stringify(data)
      context.state.modules = data
      let routers = []
      for (let m1 of data) {
        if (m1.supModules && m1.supModules.length > 0) {
          for (let m2 of m1.supModules) {
            if (m2.supModules && m2.supModules.length > 0) {
              for (let m3 of m2.supModules) {
                routers.push(m3.moduleUrl)
              }
            } else if (m2.moduleUrl && m2.moduleUrl !== '') {
              routers.push(m2.moduleUrl)
            }
          }
        }
      }
      context.state.routers = routers.concat(this.state.redirectPages)
      sessionStorage.currentUserInfo = JSON.stringify(data)
      // // data.modules = data.modules.filter(item => { return item.id === '102' })[0].supModules.filter(i => { return i.id === '10100111151' })[0].supModules
      // sessionStorage.modules = JSON.stringify(data.modules)
      // context.state.modules = data.modules
      // let routers = []
      // for (let m1 of data.modules) {
      //   if (m1.supModules && m1.supModules.length > 0) {
      //     for (let m2 of m1.supModules) {
      //       if (m2.supModules && m2.supModules.length > 0) {
      //         for (let m3 of m2.supModules) {
      //           routers.push(m3.moduleUrl)
      //         }
      //       } else if (m2.moduleUrl && m2.moduleUrl !== '') {
      //         routers.push(m2.moduleUrl)
      //       }
      //     }
      //   }
      // }
      // context.state.routers = routers.concat(this.state.redirectPages)
    }
  },
  getters: {
    getUserAreaList: (state, getters) => {
      if (!state.userAreaList) {
        return []
      }
      return state.userAreaList.map(item => {
        return item.userarea
      })
    },
    getUnitIndex: (state, getters) => {
      let unitCode = getters.getUserAreaCode
      for (let i = 0; i < state.userAreaList.length; i++) {
        if (unitCode === state.userAreaList[i].unitCode) {
          return i
        }
      }
      return -1
    },
    getTitlePre: (state, getters) => {
      if (!state.title || state.title.indexOf('/') === -1) {
        return ''
      }
      return state.title.substr(0, state.title.lastIndexOf('/') + 1)
    },
    getTitleUse: (state, getters) => {
      if (!state.title) {
        return ''
      }
      return state.title.substr(state.title.lastIndexOf('/') + 1)
    },
    checkRouteName: (state, getters) => {
      return (routerName) => {
        if (routerName === '/' || routerName === '/index') {
          return true
        }
        if (state.routers.indexOf(routerName) > -1) {
          return true
        }

        while (routerName.lastIndexOf('/') > 0) {
          if (state.routers.indexOf(routerName) > -1) {
            return true
          }
          routerName = routerName.substring(0, routerName.lastIndexOf('/'))
        }
        if (state.routers.indexOf(routerName) > -1) {
          return true
        }
        return false
      }
    },
    checkModules: (state, getters) => {
      return (path) => {
        let idx = state.modules.findIndex(i => i.moduleUrl === path)
        return idx > -1
      }
    }
  }
})
