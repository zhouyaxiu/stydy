import { createRouter, createWebHashHistory } from "vue-router";
import Home from "../views/login/home.vue";
import setting from "./modules/setting";
import qc from "./modules/qc";
import gjdc from "./modules/gjdc";
import hr from "./modules/hr";
import usersetting from "./modules/usersetting";
import childrendata from "./modules/childrendata";
import inoculation from "./modules/inoculation";
import stat from "./modules/stat";
import { mainStore } from "@/store/index";

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home, //展示的首页
    redirect: "/index",

    children: [
      {
        path: "/index",
        name: "index",
        meta: {
          title: "系统首页",
        },
        component: () =>
          import(/* webpackChunkName: "dashboard" */ "../views/index.vue"),
      },
      ...setting,
      ...qc,
      ...gjdc,
      ...hr,
      ...childrendata,
      ...usersetting,
      ...stat,
      ...inoculation
    ],
  },
  {
    path: "/login",
    name: "Login",
    meta: {
      title: "登录",
    },
    component: () =>
      import(/* webpackChunkName: "login" */ "../views/login/login.vue"),
  },
  {
    path: "/404",
    name: "404",
    meta: {
      title: "找不到页面",
    },
    component: () =>
      import(/* webpackChunkName: "404" */ "../views/error/404.vue"),
  },
  {
    path: "/403",
    name: "403",
    meta: {
      title: "没有权限",
    },
    component: () =>
      import(/* webpackChunkName: "403" */ "../views/error/403.vue"),
  },
];

const router = createRouter({
  // scrollBehavior: () => ({x: 0, y: 0}),
  history: createWebHashHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  document.title = to.meta.title;
  const store = mainStore();

  store.currentPath = to.path;
  store.currentTitle = to.meta.title;
  store.loadingPageFlag = true;
  // if (to.meta.auth) {
  //     if ((to.meta.onlyUser && store.userInfo) || store.hasPermission) {
  //       next()
  //     } else {
  //       console.log('无权限')
  //       next('/403')
  //     }
  //   } else {
  //     next()
  //   }
  const role = sessionStorage.getItem("username");
  if (to.path === "/login") {
    sessionStorage.removeItem("username");
    sessionStorage.removeItem("userInfo");
    sessionStorage.removeItem("title");
    sessionStorage.removeItem("menuModules");
    sessionStorage.removeItem("token");
    const store = mainStore();
    store.clearStore();
    next();
  } else if (!role && to.path !== "/login") {
    next("/login");
  } else if (to.meta.permission) {
    // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
    role === "admin" ? next() : next("/403");
  } else {
    next();
  }
});
router.afterEach(() => {
  const store = mainStore();
  store.loadingPageFlag = false;
});

export default router;
