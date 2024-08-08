export const tableHeader = [
  // {
  //   title: '报告单位',
  //   type: 'index'
  // },
  {
    title: "操作项",
    type: "operate",
    minWidth: "150",
    btns: [
      {
        type: 'delete',
        text: '删除'
      },
      {
        type: 'alter',
        text: '编辑'
      },
    ]
  },
  {
    title: '轮次',
    key: 'roundName',
    minWidth: '120'
  },
  {
    title: '年份',
    key: 'roundYear',
    minWidth: '120'
  },
  {
    title: '疫苗',
    key: 'vaccName',
    minWidth: '120'
  },
  {
    title: '学校名称',
    key: 'schNames',
    minWidth: '120'
  },
  {
    title: '年级',
    key: 'areaName',
    minWidth: '120'
  },
  {
    title: '出生日期范围',
    key: 'startBirth',
    minWidth: '120'
  },
  {
    title: '截止时间',
    key: 'startTime',
    minWidth: '120'
  },
]
