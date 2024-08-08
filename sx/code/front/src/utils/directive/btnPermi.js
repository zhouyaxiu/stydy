/**
 * 自定义指令-按钮显示权限
 * added by xing.yy 22.07.12
 */
import store from '../../store/store'
import { getPageAuthorizedBtn } from '@/utils/axios/api'

export default {
  inserted (el, binding, vnode) {
    const { value } = binding
    const nodeName = el.nodeName
    // 判断规则
    //   1.接口获取所有页面按钮权限
    // √ 2.每个页面获取一次按钮权限
    //* **菜单页面内跳转页面暂无判断** */
    //* **按钮组件--显示隐藏  普通按钮类型--禁用启用** */
    let moduleName = store.state.title
    let params = { userId: store.state.id, moduleId: store.state.moduleId }
    let fun = function (value, nodeName) {
      if (value && store.state.permis[value] && nodeName !== 'BUTTON') {
        el.style.display = 'inline-block'
      } else if (value && !store.state.permis[value] && nodeName === 'BUTTON') {
        el.setAttribute('class', el.getAttribute('class') + ' is-disabled')
        el.setAttribute('disabled', 'disabled')
      }
    }
    if (store.state.curAuthorizedBtnPageName === moduleName) {
      if (store.state.permis) {
        fun(value, nodeName)
      } else {
        let timerStr = value + 'btn_timer'
        if (window[timerStr]) clearInterval(window[timerStr])
        window[timerStr] = setInterval(() => {
          if (store.state.permis) {
            clearInterval(window[timerStr])
            fun(value, nodeName)
          }
        }, 300)
      }
    } else {
      if (params.userId && params.moduleId) {
        store.state.curAuthorizedBtnPageName = moduleName
        store.state.permis = null
        getPageAuthorizedBtn(params).then((res) => {
          // 现有按钮类型：导出--output  新增--insert  修改--undate  删除--delete  查询--query
          let permis = res.data ? res.data.selModuleList : [{ output: true, insert: true, update: true, delete: true, query: true }]
          permis = permis.length ? permis[0] : { output: true, insert: true, update: true, delete: true, query: true }
          store.state.permis = permis
          fun(value, nodeName)
          // console.log('pageAuthorizedBtn --> info: ', permis)
        })
      }
    }
  }
}
