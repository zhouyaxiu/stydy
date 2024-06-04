import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main'
import store from '@/store/store'
const originalPush = Router.prototype.push
Router.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject) {
    return originalPush.call(this, location, onResolve, onReject)
  }
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router)
const router = new Router({
  // mode: 'history',  //去掉url中的#
  scrollBehavior(to, from, savedPosition) {
    return {
      x: 0,
      y: 0
    }
  },
  routes: [
    {
      path: '/',
      name: '欢迎页面',
      redirect: '/index',
      component: resolve => require(['@/views/index'], resolve),
    },
    {
      path: '/login',
      name: 'login',
      component: resolve => require(['@/components/Login'], resolve)
    },
    {
      path: '/jclogin',
      name: 'jclogin',
      component: resolve => require(['@/views/jclogin'], resolve)
    },
    {
      path: '/',
      name: 'Main',
      redirect: '/index',
      component: Main,
      children: [
        {
          path: '/index',
          name: '欢迎页面',
          meta: {
            requireAuth: true,
            bgImg: 'index',
            showMenu: true
          },
          component: resolve => require(['@/views/index'], resolve)
        },
        {
          path: '/rxrt/showSchoolManageQuery/index',
          name: '学校管理',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/rxrt/showSchoolManageQuery/index'
            ], resolve)
        },

        /******************信息查验*****************/
        {
          path: '/rxrt/showInforInspectionResultQuery/infoScan',
          name: '信息扫码',
          meta: {
            requireAuth: false
          },
          component: resolve => require([
            '@/views/rxrt/showInforInspectionResultQuery/infoScan'
          ], resolve)
        },
        {
          path: '/rxrt/showInforInspectionResultQuery/batchImport',
          name: 'Excel批量导入',
          meta: {
            requireAuth: false
          },
          component: resolve => require([
            '@/views/rxrt/showInforInspectionResultQuery/batchImport'
          ], resolve)
        },
        {
          path: '/rxrt/showInforInspectionResultQuery/manualAddition',
          name: '手动添加',
          meta: {
            requireAuth: false
          },
          component: resolve => require([
            '@/views/rxrt/showInforInspectionResultQuery/manualInput'
          ], resolve)
        },
        /*************查验接种信息************/
        {
          path: '/rxrt/checkVaccination',
          name: '查验接种信息',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/rxrt/checkVaccination/index'
            ], resolve)
        },
        /*************补种汇总表************/
        {
          path: '/rxrt/showInforInspectionResultQuery/summaryReseeding',
          name: '补种汇总表',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/rxrt/showInforInspectionResultQuery/summaryReseeding'
            ], resolve)
        },
        /*************需补种名单列表************/
        {
          path: '/rxrt/showInforInspectionResultQuery/reseedingList',
          name: '需补种名单列表',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/rxrt/showInforInspectionResultQuery/reseedingList'
            ], resolve)
        },
        /*************补种情况反馈表************/
        {
          path: '/rxrt/replenishmentFeedback',
          name: '补种情况反馈表',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/rxrt/replenishmentFeedback/index'
            ], resolve)
        },
        /*************接种证查验情况汇总表************/
        {
          path: '/rxrt/InspectionSummary',
          name: '接种证查验情况汇总表',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/rxrt/InspectionSummary/index'
            ], resolve)
        },
        /*************接种证查验情况汇总表_分剂次************/
        {
          path: '/rxrt/summarySituation',
          name: '接种证查验情况汇总表（分剂次）',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/rxrt/summarySituation/index'
            ], resolve)
        },
        /*************补种疫苗统计表************/
        {
          path: '/rxrt/showInforInspectionResultQuery/statisticalVaccines',
          name: '补种疫苗统计表',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/rxrt/showInforInspectionResultQuery/statisticalVaccines'
            ], resolve)
        },
        /*************日志查看************/
        {
          path: '/rxrt/logQuery',
          name: '日志查看',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/rxrt/logQuery/index'
            ], resolve)
        },
        /*************通知单模板管理************/
        {
          path: '/rxrt/formwork',
          name: '通知单模板管理',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/rxrt/formwork/index'
            ], resolve)
        },
      ]
    },
    {
      path: '/errorPage',
      name: 'errorPage',
      component: resolve => require(['@/views/errorPage'], resolve)
    },
    {
      path: '/NoPermission',
      name: 'NoPermission',
      component: resolve => require(['@/views/noPermission'], resolve)
    },
    {
      path: '*',
      name: '404',
      redirect: '/NoPermission'
    },
    {
      path: '/refresh',
      name: 'refresh',
      component: resolve => require(['@/views/refresh'], resolve)
    }
  ]
})
router.beforeEach((to, from, next) => {
  let main = document.querySelector('.el-main')
  if (main) {
    main.scrollTop = 0
  }
  if(window.hostname && !from.name && !to.query.token && to.path=='/index'){
    next("/login")
  }
  next()
})

export default router
