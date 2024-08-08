import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.VITE_PUBLIC_PATH),
//   history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
			path: '/',
			name: 'main',
			component: () =>import(/* webpackChunkName: "main" */ '@/views/main.vue')
		},
		{
			path: '/errorPage',
			name: 'errorPage',
			component: () =>import(/* webpackChunkName: "error" */ '@/views/errorPage.vue')
		},
		{
			path: '/NoPermission',
			name: 'NoPermission',
			component: () =>import(/* webpackChunkName: "nopermission" */ '@/views/noPermission.vue')
		},
		// {
		// 	path: '*',
		// 	name: '404',
		// 	redirect: '/NoPermission'
		// },
		/***************集成平台相关跳转的页面**********************/
		{
			path: '/jcpt_login',
			name: '集成平台_登录页面',
			component: () =>import(/* webpackChunkName: "login" */ '@/views/jcpt_login.vue')
		},
		{
			path: '/jcpt_error',
			name: '集成平台_登录错误页面',
			component: () =>import(/* webpackChunkName: "error" */ '@/views/jcpt_error.vue')
		},
  ]
})
router.beforeEach((to, from, next) => {
	let main = document.querySelector('.el-main')
	if(main) {
		main.scrollTop = 0
	}
	let path = to.path;
	/*next();*/
	if(path.indexOf('/edit/') > -1) {
		path = path.substring(0, path.lastIndexOf('/'))
	}
	if(to.matched.some(r => r.meta.requireAuth)) {
		if(store.state.token && store.getters.checkRouteName(path)) {
			next()
		} else {
			next('/NoPermission')
		}
	} else {
		next()
	}
})

export default router
