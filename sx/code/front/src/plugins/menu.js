export const modules = [
  {
    id: 'M1',
    moduleCode: 'M01',
    moduleName: '学校管理',
    moduleUrl: '/rxrt/showSchoolManageQuery',
    supId: '0',
    supModules: []
  },
  {
    id: 'M2',
    moduleCode: 'M02',
    moduleName: '入托入学查验',
    supId: '0',
    supModules: [
      {
        id: 'M20',
        moduleCode: 'M020',
        moduleLevel: null,
        moduleName: '查验接种信息',
        moduleUrl: '/rxrt/checkVaccination',
        supId: '2',
        supModules: []
      },
      {
        id: 'M21',
        moduleCode: 'M021',
        moduleLevel: null,
        moduleName: '补种汇总表',
        moduleUrl: '/rxrt/showInforInspectionResultQuery/summaryReseeding',
        supId: '2',
        supModules: []
      },
      {
        id: 'M22',
        moduleCode: 'M022',
        moduleLevel: null,
        moduleName: '需补种名单列表',
        moduleUrl: '/rxrt/showInforInspectionResultQuery/reseedingList',
        supId: '2',
        supModules: []
      },
      {
        id: 'M23',
        moduleCode: 'M023',
        moduleLevel: null,
        moduleName: '补种情况反馈表',
        moduleUrl: '/rxrt/replenishmentFeedback',
        supId: '2',
        supModules: []
      },
      {
        id: 'M24',
        moduleCode: 'M024',
        moduleLevel: null,
        moduleName: '接种证查验情况汇总表',
        moduleUrl: '/rxrt/InspectionSummary',
        supId: '2',
        supModules: []
      },
      {
        id: 'M25',
        moduleCode: 'M025',
        moduleLevel: null,
        moduleName: '接种证查验情况汇总表（分剂次）',
        moduleUrl: '/rxrt/summarySituation',
        supId: '2',
        supModules: []
      },
      {
        id: 'M26',
        moduleCode: 'M026',
        moduleLevel: null,
        moduleName: '补种疫苗统计表',
        moduleUrl: '/rxrt/showInforInspectionResultQuery/statisticalVaccines',
        supId: '2',
        supModules: []
      },
      {
        id: 'M27',
        moduleCode: 'M027',
        moduleLevel: null,
        moduleName: '日志查看',
        moduleUrl: '/rxrt/logQuery',
        supId: '2',
        supModules: []
      },
      {
        id: 'M28',
        moduleCode: 'M028',
        moduleLevel: null,
        moduleName: '通知单模板管理',
        moduleUrl: '/rxrt/formwork',
        supId: '2',
        supModules: []
      },
      {
        id: 'M29',
        moduleCode: 'M029',
        moduleLevel: null,
        moduleName: '用户管理模块',
        moduleUrl: '/rxrt/userManage',
        supId: '2',
        supModules: []
      }
    ]
  },
  {
    id: 'M3',
    moduleCode: 'M03',
    moduleName: '特定疫苗查验',
    supId: '0',
    supModules: [
      {
        id: 'M31',
        moduleCode: 'M031',
        moduleName: '班级管理',
        moduleUrl: '/rxrt/classInfo',
        supId: '3',
        supModules: []
      },
      {
        id: 'M32',
        moduleCode: 'M032',
        moduleName: '特定疫苗接种证查验轮次管理',
        moduleUrl: '/rxrt/claSeq',
        supId: '3',
        supModules: []
      },
      {
        id: 'M33',
        moduleCode: 'M033',
        moduleName: 'Excel批量导入',
        moduleUrl: '/rxrt/batchImport',
        supId: '3',
        supModules: []
      },
      {
        id: 'M34',
        moduleCode: 'M034',
        moduleLevel: null,
        moduleName: '信息扫码',
        moduleUrl: '/rxrt/infoScan',
        onOff: '1',
        supId: '3',
        supModules: []
      },
      {
        id: 'M35',
        moduleCode: 'M035',
        moduleLevel: null,
        moduleName: '特定疫苗查验接种信息',
        moduleUrl: '/rxrt/speccheckVaccination',
        supId: '3',
        supModules: []
      },
      {
        id: 'M36',
        moduleCode: 'M036',
        moduleLevel: null,
        moduleName: '特定疫苗接种证查验汇总表',
        moduleUrl: '/rxrt/specInspectionSummary',
        supId: '3',
        supModules: []
      },
    ]
  },
]
