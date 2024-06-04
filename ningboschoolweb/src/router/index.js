import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main'
import store from '@/store/store'
// import * as types from '@/store/types'

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
      name: 'login',
      component: resolve => require(['@/components/Login'], resolve)
    },
    {
      path: '/main',
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
        /** ***************************************班级花名册录入*****************************************/
        {
          path: '/roster/entry',
          name: '信息扫码',
          meta: {
            requireAuth: true
          },
          component: resolve =>
            require([
              '@/views/roster/entry'
            ], resolve)
        },
        {
          path: '/collect/batchImport',
          name: 'Excel批量导入',
          meta: {
            requireAuth: true
          },
          component: resolve =>
            require([
              '@/views/collect/batchImport/index'
            ], resolve)
        },
        {
          path: '/manualAddition/index',
          name: '手动添加',
          meta: {
            requireAuth: true
          },
          component: resolve =>
            require([
              '@/views/manualAddition/index'
            ], resolve)
        },

        /** ***************************************班级花名册查询*****************************************/
        {
          path: '/roster/query',
          name: '班级花名册查询',
          meta: {
            requireAuth: true
          },
          component: resolve =>
            require([
              '@/views/roster/query'
            ], resolve)
        },

        /** ***************************************统计报表*****************************************/
        {
          path: '/vaccinationInspection',
          name: '查验补种情况汇总表',
          meta: {
            requireAuth: true
          },
          component: resolve =>
            require([
              '@/views/vaccinationInspection/index'
            ], resolve)
        },

        /** ***************************************日志查询*****************************************/
        {
          path: '/log/changeQuery',
          name: '变更日志查询',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/log/changeQuery'
            ], resolve)
        },
        {
          path: '/log/exportQuery',
          name: '导出日志查询',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/log/exportQuery'
            ], resolve)
        },
        {
          path: '/log/exportQueryInfo',
          name: '导出日志明细',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/log/exportQueryInfo'
            ], resolve)
        },
        /** ***************************************班级管理*****************************************/
        {
          path: '/classInfo/query',
          name: '班级管理',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/classInfo/index'
            ], resolve)
        },
        {
          path: '/classInfo/excel',
          name: 'Excel导入',
          meta: {
            requireAuth: false
          },
          component: resolve =>
            require([
              '@/views/classInfo/excel'
            ], resolve)
        },


        {
          path: '/replenishmentFeedback',
          name: '补种反馈表',
          meta: {
            requireAuth: true
          },
          component: resolve =>
            require([
              '@/views/replenishmentFeedback/index'
            ], resolve)
        },

        /** ***************************************系统管理*****************************************/
        {
          path: '/systemManage/changePass',
          name: '修改密码',
          meta: {
            requireAuth: true
          },
          component: resolve =>
            require([
              '@/views/systemManage/changePass'
            ], resolve)
        },
        // {
        //   path: '/systemManage/notify',
        //   name: '消息管理',
        //   meta: {
        //     requireAuth: false
        //   },
        //   component: resolve =>
        //     require([
        //       '@/views/systemManage/notify/index'
        //     ], resolve)
        // },
        {
          path: '/systemManage/formwork',
          name: '通知单模板管理',
          meta: {
            requireAuth: true
          },
          component: resolve =>
            require([
              '@/views/systemManage/formwork'
            ], resolve)
        }
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
  let path = to.path
  // next()
  if (path.indexOf('/edit/') > -1) {
    path = path.substring(0, path.lastIndexOf('/'))
  }
  if (to.matched.some(r => r.meta.requireAuth)) {
    if (store.state.token) {
      next()
    } else {
      // next()
      next('/NoPermission')
    }
  } else {
    next()
  }
})

export default router
