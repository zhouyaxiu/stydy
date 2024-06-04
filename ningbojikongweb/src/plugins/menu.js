export const modules = [
  {
    id: 'M1',
    moduleCode: 'M01',
    moduleName: '学校管理',
    moduleUrl: '/rxrt/showSchoolManageQuery/index',
    supId: '0',
    supModules: []
  },
  {
    id: 'M2',
    moduleCode: 'M02',
    moduleName: '信息查验',
    supId: '0',
    supModules: [
      {
        id: 'M21',
        moduleCode: 'M021',
        moduleLevel: null,
        moduleName: '信息扫码',
        moduleUrl: '/rxrt/showInforInspectionResultQuery/infoScan',
        onOff: '1',
        supId: '1',
        supModules: []
      },
      {
        id: 'M22',
        moduleCode: 'M022',
        moduleLevel: null,
        moduleName: 'Excel批量导入',
        moduleUrl: '/rxrt/showInforInspectionResultQuery/batchImport',
        onOff: '1',
        supId: '1',
        supModules: []
      },
      {
        id: 'M23',
        moduleCode: 'M023',
        moduleLevel: null,
        moduleName: '手动添加',
        moduleUrl: '/rxrt/showInforInspectionResultQuery/manualAddition',
        onOff: '1',
        supId: '1',
        supModules: []
      },]
  },
  {
    id: 'M3',
    moduleCode: 'M03',
    moduleLevel: null,
    moduleName: '查验接种信息',
    moduleUrl: '/rxrt/checkVaccination',
    supId: '0',
    supModules: []
  },
  {
    id: 'M4',
    moduleCode: 'M04',
    moduleLevel: null,
    moduleName: '补种汇总表',
    moduleUrl: '/rxrt/showInforInspectionResultQuery/summaryReseeding',
    supId: '0',
    supModules: []
  },
  {
    id: 'M5',
    moduleCode: 'M05',
    moduleLevel: null,
    moduleName: '需补种名单列表',
    moduleUrl: '/rxrt/showInforInspectionResultQuery/reseedingList',
    supId: '0',
    supModules: []
  },
  {
    id: 'M6',
    moduleCode: 'M06',
    moduleLevel: null,
    moduleName: '补种情况反馈表',
    moduleUrl: '/rxrt/replenishmentFeedback',
    supId: '0',
    supModules: []
  },
  {
    id: 'M7',
    moduleCode: 'M07',
    moduleLevel: null,
    moduleName: '接种证查验情况汇总表',
    moduleUrl: '/rxrt/InspectionSummary',
    supId: '0',
    supModules: []
  },
  {
    id: 'M8',
    moduleCode: 'M08',
    moduleLevel: null,
    moduleName: '接种证查验情况汇总表（分剂次）',
    moduleUrl: '/rxrt/summarySituation',
    supId: '0',
    supModules: []
  },
  {
    id: 'M9',
    moduleCode: 'M09',
    moduleLevel: null,
    moduleName: '补种疫苗统计表',
    moduleUrl: '/rxrt/showInforInspectionResultQuery/statisticalVaccines',
    supId: '0',
    supModules: []
  },
  {
    id: 'M10',
    moduleCode: 'M10',
    moduleLevel: null,
    moduleName: '日志查看',
    moduleUrl: '/rxrt/logQuery',
    supId: '0',
    supModules: []
  },
  {
    id: 'M11',
    moduleCode: 'M11',
    moduleLevel: null,
    moduleName: '通知单模板管理',
    moduleUrl: '/rxrt/formwork',
    supId: '0',
    supModules: []
  }



  // { id: 4,
  //   moduleCode: 'M04',
  //   moduleName: '日志查询',
  //   supId: '0',
  //   supModules: [
  //     {
  //       id: '41',
  //       moduleCode: 'M041',
  //       moduleLevel: null,
  //       moduleName: '变更日志查询',
  //       moduleUrl: '/rxrt/checkVaccination',
  //       onOff: '1',
  //       supId: '4',
  //       supModules: []
  //     },
  //     {
  //       id: '42',
  //       moduleCode: 'M042',
  //       moduleLevel: null,
  //       moduleName: '导出日志查询',
  //       moduleUrl: '/rxrt/checkVaccination',
  //       onOff: '1',
  //       supId: '4',
  //       supModules: []
  //     }
  //   ]
  // },

]
