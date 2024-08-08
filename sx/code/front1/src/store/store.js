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
    schName: '', // 学校名称
    schCode: '', // 学校编码
    schId: '', // 学校ID
    schType: '', // 学校类型
    account: '', // 用户名
    schStationcode: '',//学校编码
    schStationname: '',//单位名称
    aesKey: 'mGb93nbJif8154dA', // 秘钥
    aesKey2: 'b6vhedgjior655gj', // 数据库秘钥
    errorMsg: '', // 错误提示语
    title: null, // 当前页面标题
    breadcrumb: null, // 当前页面面包屑
    pagesize: 20, // 分页每页数量
    modules: [], // 目录
    routers: [], // 权限
    currentLevel: '',
    // 菜单页面内跳转页面集合,用于跳过路由鉴权---暂定
    redirectPages: [],
    modelObject: 2,
    userName: ''
  },
  mutations: {
    [types.LOGIN]: (state, data) => {
      if (data) {
        sessionStorage.token = data.token
        state.token = data.token
      }
    },
    [types.LOGOUT]: (state) => {
      sessionStorage.removeItem('token')
      sessionStorage.removeItem('schName')
      state.token = null
      state.account = null
    },
    [types.ACCOUNT]: (state, data) => {
      sessionStorage.account = data
      state.account = data
    },
    [types.USERNAME]: (state, data) => {
      sessionStorage.id = data.userinfo.id
      sessionStorage.schId = data.userinfo.schId
      sessionStorage.schName = data.userinfo.schName
      sessionStorage.schStationcode = data.userinfo.schStationCode // 接种点编码
      sessionStorage.schStationname = data.userinfo.schStationName // 接种点名称
      // sessionStorage.schStationid = data.userinfo.schStationId // 接种点ID
      sessionStorage.areaLevel = Number(getLevel(data.userinfo.schStationCode))
      // state.schStationid = data.userinfo.schStationId
      state.schName = data.userinfo.schName
      state.schPwd = data.userinfo.password
      state.userName = data.userinfo.loginName
      state.schStationcode = data.userinfo.schStationCode
      state.currentLevel = Number(getLevel(data.userinfo.schStationCode))
      state.schStationname = data.userinfo.schStationName
      state.schId = data.userinfo.schId
      state.schType = data.userinfo.schType
      sessionStorage.currentUserInfo = JSON.stringify(data)
    },
    [types.SET_ERROR]: (state, data) => {
      state.errorMsg = data
    },
    [types.SET_TITLE]: (state, data) => {
      sessionStorage.moduleId = data.moduleId
      sessionStorage.title = data.title
      state.moduleId = data.moduleId
      state.title = data.title
      state.breadcrumb = data.breadcrumb
      sessionStorage.breadcrumb = data.breadcrumb
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
