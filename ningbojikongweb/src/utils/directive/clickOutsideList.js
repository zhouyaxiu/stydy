/**
 * 自定义指令-区域树组件点击外区域收缩已展开的下拉树
 */

export default {
  inserted (el, binding, vnode) {
    function documentHandler (e) {
      if (
        e.target.className &&
        e.target.className.indexOf('shot-img-select') > -1
      ) {
        return true
      }
      if (el.contains(e.target)) {
        return false
      }
      if (
        e.target.className &&
        e.target.className.indexOf('el-container') > -1
      ) {
        return true
      }
      if (
        e.target.className &&
        e.target.className.indexOf('el-input__inner') > -1
      ) {
        return false
      }
      if (
        e.target.className &&
        e.target.className.indexOf('batchInfoDiv') > -1
      ) {
        return false
      }
      if (
        e.target.className &&
        e.target.className.indexOf('ps__thumb-y') > -1
      ) {
        return false
      }
      if (binding.expression) {
        binding.value(e)
      }
    }
    el._vueClickOutside_ = documentHandler
    document.addEventListener('click', documentHandler)
  }
}
