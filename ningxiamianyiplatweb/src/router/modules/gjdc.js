export default [
  {
    path: "/gjdc/basechanged",
    name: "baseChanged",
    meta: {
      title: "档案变化情况统计",
    },
    component: () => import("@/views/gjdc/baseChanged/index.vue"),
  },
  {
    path: "/gjdc/basetimely",
    name: "baseTimely",
    meta: {
      title: "建档及时性统计",
    },
    component: () => import("@/views/gjdc/baseTimely/index.vue"),
  },
  {
    path: "/gjdc/setThreshold",
    name: "setThreshold",
    meta: {
      title: "设置阈值",
    },
    component: () => import("@/views/gjdc/setThreshold/index.vue"),
  },
  {
    path: "/gjdc/xgRptAgeAndFenterno",
    name: "xgRptAgeAndFenterno",
    meta: {
      title: "新冠病毒疫苗分年龄、分企业接种信息汇总表",
    },
    component: () => import("@/views/gjdc/xgRptAgeAndFenterno/index.vue"),
  },
  {
    path: "/gjdc/xgRptCrowdkind",
    name: "xgRptCrowdkind",
    meta: {
      title: "按人群分类的新冠病毒疫苗接种信息汇总表",
    },
    component: () => import("@/views/gjdc/xgRptCrowdkind/index.vue"),
  },
  {
    path: "/gjdc/xgRptProfession",
    name: "xgRptProfession",
    meta: {
      title: "按职业分类的新冠病毒疫苗接种信息汇总表",
    },
    component: () => import("@/views/gjdc/xgRptProfession/index.vue"),
  },
  {
    path: "/gjdc/xgRptArea_query",
    name: "xgRptArea_query",
    meta: {
      title: "新冠病毒疫苗分地区接种信息汇总表",
    },
    component: () => import("@/views/gjdc/xgRptArea/index.vue"),
  },
  {
    path: "/gjdc/xgRptJzjlInfo_query",
    name: "xgRptJzjlInfo_query",
    meta: {
      title: "新型冠状病毒疫苗及接种信息采集表",
    },
    component: () => import("@/views/gjdc/xgRptJzjlInfo/index.vue"),
  },
  {
    path: "/gjdc/xgRptWz_query",
    name: "xgRptWz_query",
    meta: {
      title: "新冠病毒疫苗到期未种统计表",
    },
    component: () => import("@/views/gjdc/xgRptWz/index.vue"),
  },
  {
    path: "/gjdc/xgRptFinished_query",
    name: "xgRptFinished_query",
    meta: {
      title: "新冠病毒疫苗免疫程序完成情况统计表",
    },
    component: () => import("@/views/gjdc/xgRptFinished/index.vue"),
  },
  {
    path: "/gjdc/xgRptGjcd_query",
    name: "xgRptGjcd",
    meta: {
      title: "国家重档数据查询",
    },
    component: () => import("@/views/gjdc/xgRptGjcd/index.vue"),
  },
  {
    path: "/gjdc/gjUploadCheckRpt_query",
    name: "gjUploadCheck",
    meta: {
      title: "国家数据上传监测统计表",
    },
    component: () => import("@/views/gjdc/gjUploadCheck/index.vue"),
  },
  {
    path: "/gjdc/gjUploadWrongRpt_query",
    name: "gjUploadWrong",
    meta: {
      title: "国家数据上传错误统计表",
    },
    component: () => import("@/views/gjdc/gjUploadWrong/index.vue"),
  },
  {
    path: "/gjdc/gjNoPackageRpt_query",
    name: "gjNoPackage",
    meta: {
      title: "国家数据不合格未上传统计表",
    },
    component: () => import("@/views/gjdc/gjNoPackage/index.vue"),
  },
  {
    path: "/gjdc/xgRptZlkz_infofull",
    name: "xgRptZlkzInfo",
    meta: {
      title: "新冠病毒疫苗基本信息质量控制报表",
    },
    component: () => import("@/views/gjdc/xgRptZlkzInfo/index.vue"),
  },
  {
    path: "/gjdc/xgRptZlkz_jzjlfull",
    name: "xgRptZlkzJzjlfull",
    meta: {
      title: "新冠病毒疫苗接种记录质量控制报表",
    },
    component: () => import("@/views/gjdc/xgRptInfoFulJzjlfull/index.vue"),
  },
  {
    path: "/gjdc/xgRptJzjlInfoByMonth_query",
    name: "xgRptJzjlInfoByMonth",
    meta: {
      title: "按月提供新冠疫苗数据",
    },
    component: () => import("@/views/gjdc/xgDataMonth/index.vue"),
  },
  {
    path: "/gjdc/xgRptJzrs_query",
    name: "xgRptJzrs",
    meta: {
      title: "各地不同年龄段新冠病毒疫苗接种人数汇总表",
    },
    component: () => import("@/views/gjdc/NXxgRptAreaQuery/index.vue"),
  },
  {
    path: "/gjdc/xgRptAgeJq_veroQuery",
    name: "xgRptAgeJqVero",
    meta: {
      title: "新冠疫苗加强第2针分年龄汇总表（vero）",
    },
    component: () => import("@/views/gjdc/xgRptAgeJqVero/index.vue"),
  },
  {
    path: "/gjdc/xgRptAgeJq_zfQuery",
    name: "xgRptAgeJqzf",
    meta: {
      title: "新冠疫苗加强第2针分年龄汇总表（智飞）",
    },
    component: () => import("@/views/gjdc/xgRptAgeJqZf/index.vue"),
  },
  {
    path: "/gjdc/xgRptAgeJq_lsQuery",
    name: "xgRptAgeJqls",
    meta: {
      title: "新冠疫苗加强第2针分年龄汇总表（丽珠、三叶）",
    },
    component: () => import("@/views/gjdc/xgRptAgeJqLs/index.vue"),
  },
  {
    path: "/gjdc/xgRptAgeJq_kxnQuery",
    name: "xgRptAgeJqkxn",
    meta: {
      title: "新冠疫苗加强第2针分年龄汇总表（康希诺）",
    },
    component: () => import("@/views/gjdc/xgRptAgeJqKxn/index.vue"),
  },
  {
    path: "/gjdc/xgRptAgeJq_wswQuery",
    name: "xgRptAgeJqwsw",
    meta: {
      title: "新冠疫苗加强第2针分年龄汇总表（威斯克、神州、万泰、石药）",
    },
    component: () => import("@/views/gjdc/xgRptAgeJqWsw/index.vue"),
  },
  {
    path: "/gjdc/xgRptAgeShot_query",
    name: "xgRptAgeShot",
    meta: {
      title: "新冠疫苗分年龄接种情况",
    },
    component: () => import("@/views/gjdc/xgRptAgeShot/index.vue"),
  },
  {
    path: "/gjdc/insurance_query",
    name: "insurance_query",
    meta: {
      title: "新冠病毒疫苗医保正确数据统计表",
    },
    component: () => import("@/views/gjdc/insurance/index.vue"),
  },
  {
    path: "/gjdc/insurance_allErrorQuery",
    name: "insurance_allErrorQuery",
    meta: {
      title: "新冠病毒疫苗医保错误数据统计（总表）",
    },
    component: () => import("@/views/gjdc/insuranceAllError/index.vue"),
  },
  {
    path: "/gjdc/insurance_areaErrorQuery",
    name: "insurance_areaErrorQuery",
    meta: {
      title: "新冠病毒疫苗医保错误数据统计表（分地区）",
    },
    component: () => import("@/views/gjdc/insuranceAreaError/index.vue"),
  },
];
