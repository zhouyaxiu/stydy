/* eslint-disable no-mixed-spaces-and-tabs */
/* eslint-disable no-new */
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import Vuex from 'vuex'
import './assets/css/public.css'
import './assets/css/icons.css'
import './assets/css/table.css'
import './assets/css/condition.css'
import './assets/css/customScrollBar.css'
import './assets/css/print.css'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import util from './utils/common/util'
import dateutil from './utils/common/dateutil'
import directive from './utils/directive'
import store from './store/store'
import * as types from './store/types'
import * as consts from './utils/common/consts'
import PScrollbar from 'perfect-scrollbar'
// 富文本
import VueQuillEditor from 'vue-quill-editor'
// 引入样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

import 'perfect-scrollbar/css/perfect-scrollbar.css'
// import '@wangeditor/editor/dist/css/style.css'

// 组件统一放components.js内
import components from '@/plugins/components.js'
import reloads from '@/plugins/reloads.js'

// 全局安装md5
import md5 from 'js-md5'
// 全局安装lodash
import _ from 'lodash'
import Print from 'vue-print-nb'
Vue.prototype._ = _
Vue.prototype.$md5 = md5
// 打印
Vue.use(Print)
const scrollClassName = [{
  name: 'scroll-item',
  props: {
    suppressScrollX: true,
    wheelPropagation: true
  }
}, {
  name: 'scroll-item-y',
  props: {
    suppressScrollY: true,
    wheelPropagation: true
  }
}, {
  name: 'el-table__body-wrapper',
  props: {}
}, {
  name: 'el-transfer-panel__list',
  props: {
    suppressScrollX: true,
    wheelPropagation: true
  }
}, {
  name: 'transfer-main',
  props: {
    wheelPropagation: true
  }
}, {
  name: 'el-dialog__body',
  props: {
    suppressScrollX: true,
    wheelPropagation: true
  }
}, {
  name: 'el-tab-pane',
  props: {
    suppressScrollX: true,
    wheelPropagation: true
  }
}]
// 用firefox变量表示火狐代理
let firefox = navigator.userAgent.indexOf('Firefox') !== -1

// eslint-disable-next-line no-unused-vars
function MouseWheel(e) { // 阻止事件冒泡和默认行为的完整兼容性代码
  e = e || window.event
  if (e.stopPropagation) { // 这是取消冒泡
    e.stopPropagation()
  } else {
    e.cancelBubble = true
  };
  if (e.preventDefault) { // 这是取消默认行为，要弄清楚取消默认行为和冒泡不是一回事
    e.preventDefault()
  } else {
    e.returnValue = false
  };
}
const update = (el, noTopFlag) => {
  let className = el.className
  if (className && typeof (className) === 'string') {
    let arr = className.split(' ')
    scrollClassName.forEach(item => {
      if (arr.indexOf(item.name) > -1) {
        componentsScroll(el, item.props, noTopFlag)
      }
    })
  }
  if (el.children && el.children.length > 0) {
    for (let i = 0; i < el.children.length; i++) {
      update(el.children[i], noTopFlag)
    }
  }
}
const componentsScroll = (el, props, noTopFlag) => {
  // 在元素上加点私货，名字随便取，确保不会和已有属性重复即可，我取名叫做_ps_
  if (el._ps_ instanceof PScrollbar) {
    el._ps_.update()
    if (!noTopFlag) {
      setTimeout(function () {
        let evt = document.createEvent('MouseEvents')
        evt.initEvent(firefox ? 'DOMMouseScroll' : 'mousewheel', true, true)
        evt.wheelData = 120
        el.dispatchEvent(evt)
        el._ps_.update()
      }, 200)
    }
  } else {
    // el上挂一份属性
    el._ps_ = new PScrollbar(el, props)
    // 如果是ff就绑定DOMMouseScroll事件，其他浏览器就用onmousewheel事件触发
    // firefox ? el.parentElement.addEventListener('DOMMouseScroll',MouseWheel,false) : (el.parentElement.onmousewheel = MouseWheel);
  }
}
// eslint-disable-next-line no-unused-expressions
Vue.prototype.setScrollBar = function (noTopFlag) {
  this.$nextTick(() => {
    update(this.$el, noTopFlag)
  })
}

Vue.use(Vuex)
Vue.use(router)
Vue.use(ElementUI)
Vue.use(components)
Vue.use(reloads)
Vue.use(Print)
Vue.use(directive)
Vue.use(VueQuillEditor)

Vue.config.productionTip = false

Vue.prototype.$util = util
Vue.prototype.$dateutil = dateutil
Vue.prototype.$types = types
Vue.prototype.$consts = consts

// 加载loading页面
let loading
Vue.prototype.openFullScreen = function (msg, background) {
  let content = '正在查询...'
  if (msg && msg !== '') {
    content = msg
  }
  let rgba = 'rgba(0, 0, 0, 0.5)'
  if (background && background !== '') {
    rgba = background
  }
  this.$nextTick(() => {
    loading = this.$loading({
      lock: true,
      text: content,
      spinner: 'my-el-custom-spinner',
      background: rgba
    })
  })
}
// 关闭loading页面
Vue.prototype.closeFullScreen = function () {
  this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
    setTimeout(() => {
      loading.close()
    }, 100)
  })
}

router.beforeEach((to, from, next) => {
  if (from && from.path === to.path && to.path === '/errorPage') {
    if(sessionStorage.getItem('isJC')=='true'){
      this.$router.back();
      window.history.go(-1);
      return
    }else{
      next(false)
      return
    }
  }
  next()
})

window._Vue = new Vue({
  el: '#app',
  router,
  store,
  components: {
    App
  },
  template: '<App/>'
})
