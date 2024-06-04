export default [
  {
    path: "/childrendata/ChildrenCount",
    name: "ChildrenCount",
    meta: {
      title: "受种者数查询",
    },
    component: () => import("@/views/childrendata/childrenCount/index.vue"),
  },
  {
    path: "/childrendata/ChildrenCase",
    name: "ChildrenCase",
    meta: {
      title: "档案查询",
    },
    component: () => import("@/views/childrendata/childrenCase/index.vue"),
  },
  {
    path: "/childrendata/Transfer",
    name: "Transfer",
    meta: {
      title: "变迁情况查询",
    },
    component: () => import("@/views/childrendata/transfer/index.vue"),
  },
  {
    path: "/childrendata/SameChildren_init",
    name: "SameChildren_init",
    meta: {
      title: "重复档案受种者查询",
    },
    component: () => import("@/views/childrendata/sameChildren_init/index.vue"),
  }
];
