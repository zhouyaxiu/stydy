export const modules = [
  // {
  //   id: 1,
  //   moduleCode: 'M01',
  //   moduleName: '班级花名册录入',
  //   supId: '0',
  //   supModules: [
  //     {
  //       id: '11',
  //       moduleCode: 'M011',
  //       moduleLevel: null,
  //       moduleName: '信息扫码',
  //       moduleUrl: '/roster/entry',
  //       onOff: '1',
  //       supId: '1',
  //       supModules: []
  //     },
  //     {
  //       id: '12',
  //       moduleCode: 'M012',
  //       moduleLevel: null,
  //       moduleName: 'Excel批量导入',
  //       moduleUrl: '/collect/batchImport',
  //       onOff: '1',
  //       supId: '1',
  //       supModules: []
  //     },
  //     {
  //       id: '13',
  //       moduleCode: 'M013',
  //       moduleLevel: null,
  //       moduleName: '手动添加',
  //       moduleUrl: '/manualAddition/index',
  //       onOff: '1',
  //       supId: '1',
  //       supModules: []
  //     },

  //   ]
  // },
  {
    id: 2,
    moduleCode: 'M02',
    moduleName: '班级花名册查询',
    supId: '0',
    supModules: [
      {
        id: '21',
        moduleCode: 'M021',
        moduleLevel: null,
        moduleName: '班级花名册查询',
        moduleUrl: '/roster/query',
        onOff: '1',
        supId: '2',
        supModules: []
      },
    ]
  },
  {
    id: 3,
    moduleCode: 'M03',
    moduleName: '统计报表',
    supId: '0',
    supModules: [
      {
        id: '31',
        moduleCode: 'M031',
        moduleLevel: null,
        moduleName: '需补种名单列表',
        moduleUrl: '/vaccinationInspection',
        onOff: '1',
        supId: '2',
        supModules: []
      },
      {
        id: '32',
        moduleCode: 'M032',
        moduleLevel: null,
        moduleName: '补种反馈表',
        moduleUrl: '/replenishmentFeedback',
        onOff: '1',
        supId: '2',
        supModules: []
      }
    ]
  },
  // {
  //   id: 4,
  //   moduleCode: 'M04',
  //   moduleName: '日志查询',
  //   supId: '0',
  //   supModules: [
  //     {
  //       id: '41',
  //       moduleCode: 'M041',
  //       moduleLevel: null,
  //       moduleName: '变更日志查询',
  //       moduleUrl: '/log/changeQuery',
  //       onOff: '1',
  //       supId: '4',
  //       supModules: []
  //     },
  //     {
  //       id: '42',
  //       moduleCode: 'M042',
  //       moduleLevel: null,
  //       moduleName: '导出日志查询',
  //       moduleUrl: '/log/exportQuery',
  //       onOff: '1',
  //       supId: '4',
  //       supModules: []
  //     }
  //   ]
  // },
  // {
  //   id: 5,
  //   moduleCode: 'M05',
  //   moduleName: '班级管理',
  //   supId: '0',
  //   supModules: [
  //     {
  //       id: '51',
  //       moduleCode: 'M051',
  //       moduleLevel: null,
  //       moduleName: '班级查询',
  //       moduleUrl: '/classInfo/query',
  //       onOff: '1',
  //       supId: '5',
  //       supModules: []
  //     },
  //     {
  //       id: '52',
  //       moduleCode: 'M052',
  //       moduleLevel: null,
  //       moduleName: 'Excel导入',
  //       moduleUrl: '/classInfo/excel',
  //       onOff: '1',
  //       supId: '5',
  //       supModules: []
  //     }
  //   ]
  // },
  {
    id: 6,
    moduleCode: 'M06',
    moduleName: '系统管理',
    supId: '0',
    supModules: [
      // {
      //   id: '61',
      //   moduleCode: 'M061',
      //   moduleLevel: null,
      //   moduleName: '修改密码',
      //   moduleUrl: '/systemManage/changePass',
      //   onOff: '1',
      //   supId: '6',
      //   supModules: []
      // },
      {
        id: '62',
        moduleCode: 'M062',
        moduleLevel: null,
        moduleName: '通知单模板管理',
        moduleUrl: '/systemManage/formwork',
        onOff: '1',
        supId: '6',
        supModules: []
      }
    ]
  }
]
