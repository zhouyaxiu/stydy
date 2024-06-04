/**
 * 自定义指令-防抖
    * 判断规则
    *  *value--时间，默认为1000毫秒
 */

export default {
  mounted(el, binding, vnode) {
    const { value } = binding
    el.addEventListener('click', e => {
      let cls = 'no-click'
      if (el.classList.contains('el-button')) {
        cls = 'is-disabled'
      }
      el.classList.add(cls)
      el.disabled = true
      setTimeout(() => {
        el.disabled = false
        el.classList.remove(cls)
      }, value || 1000);
    })
  }
}
