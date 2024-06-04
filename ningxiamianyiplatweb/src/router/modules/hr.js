export default [
  {
    path: "/hr/hr_init",
    name: "hrInit",
    meta: {
      title: "人力资源管理",
    },
    component: () => import("@/views/hr/hrInit/index.vue"),
  },
  {
    path: "/hr/hrStatistics_init",
    name: "hrStatisticsInit",
    meta: {
      title: "人力资源统计",
    },
    component: () => import("@/views/hr/hrStatisticsInit/index.vue"),
  }
];
