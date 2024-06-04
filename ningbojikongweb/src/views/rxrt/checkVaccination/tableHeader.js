
import util from '@/utils/common/util'
export const tableHeader = [
  {
    title: '',
    type: 'selection'
  },
  {
    title: '操作项',
    type: 'operate',
    minWidth: '150',
    btns: [
      {
        type: 'delete',
        text: '删除'
      },
      {
        type: 'alter',
        text: '修改'
      },
      {
        type: 'check',
        text: '查验'
      }
    ]
  },
  {
    title: '查验状态',
    key: 'stuIsjzOrigin',
    minWidth: '130',
    render: (h, params) => { return h('span', params.row.stuIsjzOrigin == '0' ? '已种全' : params.row.stuIsjzOrigin == 1 ? '未种全需补种' : params.row.stuIsjzOrigin == -1 ? '未种全无需补种' : params.row.stuIsjzOrigin == 2 || params.row.stuIsjzOrigin == null ? '无接种证' : '') }
  },
  {
    title: '是否种全',
    key: 'stuIsjz',
    minWidth: '100',
    render: (h, params) => { return h('span', params.row.stuIsjz == '0' ? '是' : params.row.stuIsjz == '1' ? '否-需补种' : params.row.stuIsjz == '-1' ? '否-无需补种' : '') }
  },
  {
    title: '学校名称',
    key: 'schName',
    minWidth: '120',
    showOverflowTooltip: true
  },
  {
    title: '学年',
    key: 'academicYear',
    minWidth: '80',
    showOverflowTooltip: true
  },
  {
    title: '轮次',
    key: 'rounds',
    minWidth: '80',
    showOverflowTooltip: true,
    render: (h, params) => { return h('span', params.row.rounds == '1' ? '春季' : '秋季') }
  },
  {
    title: '所在年级',
    key: 'grade',
    minWidth: '100'
  },
  {
    title: '所在班级',
    key: 'clazzName',
    minWidth: '100'
  },
  {
    title: '受种者编码',
    key: 'stuNo',
    minWidth: '180'
  },
  {
    title: '手机号码',
    key: 'stuPhone',
    minWidth: '150',
    render: (h, params) => { return h('span', util.encryptPhone(params.row.stuPhone)) }
  },
  {
    title: '儿童姓名',
    key: 'stuName',
    minWidth: '120'
  },
  {
    title: '性别',
    key: 'stuSex',
    minWidth: '120',
    showOverflowTooltip: true
  },
  {
    title: '出生日期',
    key: 'stuBirth',
    minWidth: '180',
    showOverflowTooltip: true
  },
  {
    title: '是否有接种证',
    key: 'stuIsjzz',
    minWidth: '120',
    showOverflowTooltip: true,
    render: (h, params) => { return h('span', params.row.stuIsjzz == '0' || params.row.stuIsbjzz == '0' ? '是' : (params.row.stuIsjzz == '1' && params.row.stuIsbjzz == '1') || (params.row.stuIsjzz == '1' && params.row.stuIsbjzz == null) || params.row.stuIsjz == '2' ? '否' : '') }
    // render:(h, params) => {return h('span',params.row.stuIsjzz == null && params.row.stuIsbjzz == null ? '' : params.row.stuIsbjzz == '0' || params.row.stuIsjzz == '0' || params.row.stuIsjz != '2' ? '是' :'否')}
  },
  {
    title: '儿童条码',
    key: 'stuImuno',
    minWidth: '180',
    showOverflowTooltip: true
  },
  {
    title: '户籍类型',
    key: 'stuType',
    minWidth: '120',
    showOverflowTooltip: true,
    render: (h, params) => {
      return h('span', params.row.stuType == '1' ? '常住' : params.row.stuType == '2' ? '县区内流动' : params.row.stuType == '3' ? '地市内流动' : params.row.stuType ==
    '4' ? '省内流动' : params.row.stuType == '5' ? '国内流动' : params.row.stuType == '6' ? '国外流动' : '')
    }
  },
  {
    title: '查验日期',
    key: 'firstStuSmrq',
    minWidth: '180',
    showOverflowTooltip: true
  },
  {
    title: '卡介苗',
    key: 'd001',
    minWidth: '120',
    isVaccine: true
  },
  {
    title: '乙肝疫苗',
    hasChildrenTitle: true,
    isVaccine: true,
    list: [
      {
        title: '1',
        key: 'd063',
        minWidth: '50'
      },
      {
        title: '2',
        key: 'd064',
        minWidth: '50'
      },
      {
        title: '3',
        key: 'd065',
        minWidth: '50'
      }
    ]
  },
  {
    title: '脊灰疫苗',
    isVaccine: true,
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        key: 'd009',
        minWidth: '50'
      },
      {
        title: '2',
        key: 'd010',
        minWidth: '50'
      },
      {
        title: '3',
        key: 'd011',
        minWidth: '50'
      },
      {
        title: '4',
        key: 'd012',
        minWidth: '50'
      }
    ]
  },
  {
    title: '百白破疫苗',
    isVaccine: true,
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        key: 'd015',
        minWidth: '50'
      },
      {
        title: '2',
        key: 'd016',
        minWidth: '50'
      },
      {
        title: '3',
        key: 'd017',
        minWidth: '50'
      },
      {
        title: '4',
        key: 'd018',
        minWidth: '50'
      }
    ]
  },
  {
    title: '白破疫苗',
    key: 'd037',
    minWidth: '50',
    isVaccine: true
  },
  {
    title: '麻腮风疫苗',
    isVaccine: true,
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        key: 'd059',
        minWidth: '50'
      },
      {
        title: '2',
        key: 'd060',
        minWidth: '50'
      }
    ]
  },
  {
    title: '流脑疫苗',
    isVaccine: true,
    hasChildrenTitle: true,
    list: [
      {
        title: 'A群流脑疫苗',
        isVaccine: true,
        hasChildrenTitle: true,
        list: [
          { title: '1', key: 'd040', minWidth: '50' },
          { title: '2', key: 'd041', minWidth: '50' }
        ]
      },
      {
        title: 'A+C群流脑疫苗',
        isVaccine: true,
        hasChildrenTitle: true,
        list: [
          { title: '1', key: 'd045', minWidth: '70' },
          { title: '2', key: 'd046', minWidth: '70' }
        ]
      }
    ]
  },
  {
    title: '乙脑疫苗',
    isVaccine: true,
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        key: 'd032',
        minWidth: '50'
      },
      {
        title: '2',
        key: 'd033',
        minWidth: '50'
      }
    ]
  },
  {
    title: '甲肝灭活疫苗',
    isVaccine: true,
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        key: 'd073',
        minWidth: '60'
      },
      {
        title: '2',
        key: 'd074',
        minWidth: '60'
      }
    ]
  },
  {
    title: '*水痘疫苗',
    isVaccine: true,
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        key: 'd050',
        minWidth: '50'
      },
      {
        title: '2',
        key: 'd132',
        minWidth: '50'
      }
    ]
  }
]
