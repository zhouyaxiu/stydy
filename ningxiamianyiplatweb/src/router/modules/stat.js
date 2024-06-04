export default [
  {
    path: "/stat/staticRpt",
    name: "staticRpt",
    meta: {
      title: "postMessage",
    },
    component: () => import("@/views/stat/staticRpt/index.vue"),
  },
  {
    path: "/qc/infofull",
    name: "infofull",
    meta: {
      title: "档案基本信息完整性",
    },
    component: () => import("@/views/qc/infoFull/index.vue"),
  },
  {
    path: "/qc/jzjlfull",
    name: "jzjlfull",
    meta: {
      title: "档案接种信息完整性",
    },
    component: () => import("@/views/qc/jzjlFull/index.vue"),
  },
  {
    path: "/qc/jzjlbl",
    name: "jzjlfull",
    meta: {
      title: "合并单元格",
    },
    component: () => import("@/views/qc/jzjlbl/index.vue"),
  },
  {
    path: "/qc/blockupWarn",
    name: "blockupWarn",
    meta: {
      title: "拖拽表格单元格",
    },
    component: () => import("@/views/qc/blockupWarn/index.vue"),
  },
  {
    path: "/qc/jzjlUploedTimely",
    name: "jzjlUploedTimely",
    meta: {
      title: "合并表格单元格列和行",
    },
    component: () => import("@/views/qc/jzjlUploedTimely/index.vue"),
  },
];
