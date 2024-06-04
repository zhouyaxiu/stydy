export const tableHeader = [
  {
    title: '序号',
    type: 'index'
  },
  {
    title: '数据来源',
    key: 'self_stu_sourceid',
    minWidth: '150'
  },
  {
    title: '受种者班级',
    key: 'stu_classname',
    minWidth: '150'
  },
  {
    title: '预防接种档案编号',
    key: 'stu_no',
    minWidth: '180',
    showOverflowTooltip: true
  },
  {
    title: '受种者信息介质识别码',
    key: 'stu_imuno',
    minWidth: '180',
    showOverflowTooltip: true
  },
  {
    title: '户籍类型',
    key: 'stu_type',
    minWidth: '100',
    showOverflowTooltip: true
  },
  {
    title: '受种者姓名',
    key: 'stu_name',
    minWidth: '120'
  },
  {
    title: '性别',
    key: 'stu_sex',
    minWidth: '60'
  },
  {
    title: '出生日期',
    key: 'stu_birth',
    minWidth: '100'
  },
  {
    title: '有无接种证',
    key: 'stu_isjzz',
    minWidth: '100'
  },
  {
    title: '父亲/母亲/监护人姓名',
    key: 'stu_parentname',
    minWidth: '100'
  },
  {
    title: '所属预防接种单位',
    key: 'stu_stationname',
    minWidth: '280',
    showOverflowTooltip: true
  },
  {
    title: '乙肝疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        key: 'b063InspectionShow',
        minWidth: '100'
      },
      {
        title: '2',
        key: 'b064InspectionShow',
        minWidth: '100'
      },
      {
        title: '3',
        key: 'b065InspectionShow',
        minWidth: '100'
      }
    ]
  },
  {
    title: '脊灰疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        key: 'b009InspectionShow',
        minWidth: '100'
      },
      {
        title: '2',
        key: 'b010InspectionShow',
        minWidth: '100'
      },
      {
        title: '3',
        key: 'b011InspectionShow',
        minWidth: '100'
      },
      {
        title: '4',
        key: 'b012InspectionShow',
        minWidth: '100'
      }
    ]
  },
  {
    title: '百白破疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        key: 'b015InspectionShow',
        minWidth: '100'
      },
      {
        title: '2',
        key: 'b016InspectionShow',
        minWidth: '100'
      },
      {
        title: '3',
        key: 'b017InspectionShow',
        minWidth: '100'
      },
      {
        title: '4',
        key: 'b018InspectionShow',
        minWidth: '100'
      }
    ]
  },
  {
    title: '白破疫苗',
    key: 'b037InspectionShow',
    minWidth: '100'
  },
  {
    title: '麻疹类疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        key: 'b059InspectionShow',
        minWidth: '100'
      },
      {
        title: '2',
        key: 'b060InspectionShow',
        minWidth: '100'
      }
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
          { title: '1', key: 'b040InspectionShow', minWidth: '100' },
          { title: '2', key: 'b041InspectionShow', minWidth: '100' }
        ]
      },
      {
        title: 'A+C群流脑疫苗',
        hasChildrenTitle: true,
        list: [
          { title: '1', key: 'b045InspectionShow', minWidth: '100' },
          { title: '2', key: 'b046InspectionShow', minWidth: '100' }
        ]
      }
    ]
  },
  {
    title: '乙脑疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        key: 'b032InspectionShow',
        minWidth: '100'
      },
      {
        title: '2',
        key: 'b033InspectionShow',
        minWidth: '100'
      }
    ]
  },
  {
    title: '甲肝疫苗',
    key: 'b069InspectionShow',
    minWidth: '100'
  },
  {
    title: '是否种全',
    key: 'self_stu_isjz'
  },
  {
    title: '补种是否完成',
    key: 'self_stu_isfull',
    minWidth: '120'
  },
  {
    title: "备注",
    key: "remark",
    minWidth: "200",
    showOverflowTooltip: true,
  },
  {
    title: "操作项",
    type: "operate",
    minWidth: "100",
    fixed: 'right',
    btns:[
      {
        type:'query',
        text:'重新查验'
      }
    ]
  },
]
