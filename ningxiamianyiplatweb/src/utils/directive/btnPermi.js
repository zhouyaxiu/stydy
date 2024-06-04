/**
 * 自定义指令-按钮显示权限
    * 判断规则
    *   1.接口获取所有页面按钮权限
    * √ 2.每个页面获取一次按钮权限 
    * **按钮组件--显示隐藏 
    * **普通按钮类型--禁用启用**
 */
import { mainStore } from '@/store/index'
// import { getPageAuthorizedBtn } from '@/utils/axios/api'

export default {
  mounted(el, binding, vnode) {
    const { value } = binding
    const nodeName = el.nodeName
    const store = mainStore()
    const currentMenu = store.menuModules
    // console.log('pageAuthorizedBtn --> currentMenu: ', currentMenu)
    let moduleName = currentMenu.title
    let params = { userId: currentMenu.id, moduleId: currentMenu.moduleId }
    //接口获取当前页面按钮权限配置，暂无接口，默认显示
    el.style.display = 'inline-block'
    return { output: true, insert: true, update: true, delete: true, query: true }
    //---------
    let fun = function (value, nodeName) {
      if (value && currentMenu.permis[value] && nodeName !== 'BUTTON') {
        el.style.display = 'inline-block'
      } else if (value && !currentMenu.permis[value] && nodeName === 'BUTTON') {
        el.setAttribute('class', el.getAttribute('class') + ' is-disabled')
        el.setAttribute('disabled', 'disabled')
      }
    }
    if (currentMenu.curAuthorizedBtnPageName === moduleName) {
      if (currentMenu.permis) {
        fun(value, nodeName)
      } else {
        let timerStr = value + 'btn_timer'
        if (window[timerStr]) clearInterval(window[timerStr])
        window[timerStr] = setInterval(() => {
          if (currentMenu.permis) {
            clearInterval(window[timerStr])
            fun(value, nodeName)
          }
        }, 300)
      }
    } else {
      if (params.userId && params.moduleId) {
        currentMenu.curAuthorizedBtnPageName = moduleName
        currentMenu.permis = null
        getPageAuthorizedBtn(params).then((res) => {
          // 现有按钮类型：导出--output  新增--insert  修改--update  删除--delete  查询--query
          let permis = res.data ? res.data.selModuleList : [{ output: true, insert: true, update: true, delete: true, query: true }]
          permis = permis.length ? permis[0] : { output: true, insert: true, update: true, delete: true, query: true }
          currentMenu.permis = permis
          fun(value, nodeName)
          console.log('pageAuthorizedBtn --> info: ', permis)
        })
      }
    }
  }
}
