export default [
    {
      path: "/inoculation/coverageReport",
      name: "coverageReport",
      meta: {
        title: "接种率调查",
      },
      component: () => import("@/views/inoculation/coverageReport/index.vue"),
    }
  ];
  