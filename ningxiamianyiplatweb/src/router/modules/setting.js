export default [
  {
    path: "/systemsetting/showAdministrativeQuery",
    name: "areaManage",
    meta: {
      title: "地区管理",
    },
    component: () => import("@/views/setting/areaManage/index.vue"),
  },
  {
    path: "/systemsetting/openBBSQueryPage",
    name: "noticeManage",
    meta: {
      title: "公告板管理",
    },
    component: () => import("@/views/setting/noticeManage/index.vue"),
  },
  {
    path: '/systemsetting/showInoculationUnitDel',
    name: "deleteUnitDate",
    meta: {
      title: "删除某接种单位数据",
    },
    component: () => import("@/views/setting/deleteUnitDate/index.vue"),
  },
  {
    path: "/systemsetting/openSysLogQuery",
    name: "openSysLogQuery",
    meta: {
      title: "系统日志查看",
    },
    component: () => import("@/views/setting/systemLogs/index.vue"),
  },
  {
    path: "/systemsetting/showInoculationUnitQuery",
    name: "showInoculationUnitQuery",
    meta: {
      title: "接种单位管理",
    },
    component: () => import("@/views/setting/inoculationUnit/index.vue"),
  },
  {
    path: "/systemsetting/sysLoginLogAction",
    name: "userLoginLog",
    meta: {
      title: "登录用户查询",
    },
    component: () => import("@/views/setting/userLoginLog/index.vue"),
  },
  {
    path: "/systemsetting/showInoculationUnitMove",
    name: "showInoculationUnitMove",
    meta: {
      title: "接种单位迁移",
    },
    component: () => import("@/views/setting/inoculationMove/index.vue"),
  },
  {
    path: "/systemsetting/showInoculationUnitMerge",
    name: "showInoculationUnitMerge",
    meta: {
      title: "接种单位合并",
    },
    component: () => import("@/views/setting/inoculationMerge/index.vue"),
  },
  {
    path: "/systemsetting/showSysRole",
    name: "roleManage",
    meta: {
      title: "系统角色管理",
    },
    component: () => import("@/views/setting/roleManage/index.vue"),
  },
  {
    path: "/systemsetting/complainManage_query",
    name: "complainManage_query",
    meta: {
      title: "投诉管理",
    },
    component: () => import("@/views/setting/complainManage/index.vue"),
  },
];
