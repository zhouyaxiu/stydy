export const tableHeader = [
  {
    type: 'selection',
  },
  {
    title: '数据来源',
    key: 'stu_classname',
    minWidth: '180',
    showOverflowTooltip: true
  },
  {
    title: '流水号',
    key: 'stu_name',
    minWidth: '150',
    showOverflowTooltip: true
  },
  {
    title: '受种者班级',
    key: 'stu_sex',
    minWidth: '110'
  },
  {
    title: '受种者预防接种档案编号',
    key: 'stu_birth',
    minWidth: '210'
  },
  {
    title: '受种者信息介质识别码',
    key: 'stu_parentname',
    minWidth: '160'
  },
  {
    title: '户籍类型',
    key: 'stu_phone',
    minWidth: '110'
  },
  {
    title: '受种者姓名',
    key: 'ifBuZhong',
    minWidth: '110'
  },
  {
    title: '性别',
    key: 'ifBuZhong',
  },
  {
    title: '出生日期',
    key: 'ifBuZhong',
  },
  {
    title: '接种证',
    key: 'ifBuZhong',
  },
  {
    title: '父亲/母亲/监护人姓名',
    key: 'ifBuZhong',
    minWidth: '150'
  },
  {
    title: '所属预防接种单位',
    key: 'ifBuZhong',
    minWidth: '130'
  },
  {
    title: '乙肝疫苗',
    hasChildrenTitle: true,
    list: [
      { title: '1', key: 'yiGanNeed', minWidth: '110' },
      { title: '2', key: 'yiGanHas', minWidth: '110' },
      { title: '3', key: 'yiGanHas', minWidth: '110' }
    ]
  },
  {
    title: '脊灰疫苗',
    hasChildrenTitle: true,
    list: [
      { title: '1', key: 'jiHuiNeed', minWidth: '110' },
      { title: '2', key: 'jiHuiHas', minWidth: '110' },
      { title: '3', key: 'jiHuiHas', minWidth: '110' },
      { title: '4', key: 'jiHuiHas', minWidth: '110' },
    ]
  },
  
  {
    title: '百白破疫苗',
    hasChildrenTitle: true,
    list: [
      { title: '1', key: 'baiBaiPoNeed', minWidth: '110' },
      { title: '2', key: 'baiBaiPoHas', minWidth: '110' },
      { title: '3', key: 'baiBaiPoHas', minWidth: '110' },
      { title: '4', key: 'baiBaiPoHas', minWidth: '110' },
    ]
  },
  {
    title: '白破疫苗',
    key: 'ifBuZhong',
    minWidth: '110'
  },
  {
    title: '麻疹类疫苗',
    hasChildrenTitle: true,
    list: [
      { title: '1', key: 'baiPoNeed', minWidth: '110' },
      { title: '2', key: 'baiPoHas', minWidth: '110' }
    ]
  },
  {
    title: '流脑疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: 'A群流脑疫苗',
        hasChildrenTitle: true,
        list: [
          { title: '1', key: 'aqunLiuNaoNeed', minWidth: '110' },
          { title: '2', key: 'aqunLiuNaoHas', minWidth: '110' }
        ]
      },
      {
        title: 'A+C群流脑疫苗',
        hasChildrenTitle: true,
        list: [
          { title: '1', key: 'acQunliuNaoNeed', minWidth: '110' },
          { title: '2', key: 'acQunliuNaoHas', minWidth: '110' }
        ]
      }
    ]
  },
  {
    title: '乙脑疫苗',
    hasChildrenTitle: true,
    list: [
      { title: '1', key: 'yiNaoNeed', minWidth: '110' },
      { title: '2', key: 'yiNaoHas', minWidth: '110' }
    ]
  },
  {
    title: '甲肝疫苗',
    key: 'ifBuZhong',
    minWidth: '110'
  },
  {
    title: '是否种全',
    key: 'ifBuZhong',
    minWidth: '110'
  },
  {
    title: '补种是否完成',
    key: 'ifBuZhong',
    minWidth: '110'
  },
]
