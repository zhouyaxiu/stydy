/**
 * 自定义指令
 * 1.按钮显示权限---btnPermi
 */
import Vue from 'vue'
import btnPermi from './btnPermi'
import clickOutsideList from './clickOutsideList'

const install = function (Vue) {
  Vue.directive('btnPermi', btnPermi)
  Vue.directive('clickoutsideList', clickOutsideList)
}

if (window.Vue) {
  window['btnPermi'] = btnPermi
  window['clickoutsideList'] = clickOutsideList
  Vue.use(install)
}

export default install