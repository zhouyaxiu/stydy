export const tableHeader = [
  {
    title: '轮次',
    key: 'stuSeq',
    minWidth: '120'
  },
  {
    title: '班级',
    key: 'className',
    minWidth: '300'
  },
  {
    title: '入托入学或转入人数',
    key: 'virtualStudentCount',
    minWidth: '150'
  },
  {
    title: '实查验人数',
    key: 'studentCount',
    minWidth: '110'
  },
  {
    title: '无证人数',
    key: 'hasNotCard',
    minWidth: '110'
  },
  {
    title: '已补证人数',
    key: 'hasCard',
    minWidth: '120'
  },
  {
    title: '需补种人数',
    key: 'needReplant',
    minWidth: '130',
    isclick:'true'
  },
  {
    title: '已补种全人数',
    key: 'hasReplant',
    minWidth: '130'
  },
  {
    title: '乙肝疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b063NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b063HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b064NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b064HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '3',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b065NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b065HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '小计',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'yiGanNeedReplantSubtotal',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'yiGanHasReplantSubtotal',
            minWidth: '110'
          }
        ]
      }
    ]
  },
  {
    title: '脊灰疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b009NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b009HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b010NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b010HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '3',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b011NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b011HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '4',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b012NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b012HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '小计',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'jiHuiNeedReplantSubtotal',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'jiHuiHasReplantSubtotal',
            minWidth: '110'
          }
        ]
      }
    ]
  },
  {
    title: '百白破疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b015NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b015HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b016NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b016HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '3',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b017NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b017HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '4',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b018NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b018HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '小计',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'baiBaiPoNeedReplantSubtotal',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'baiBaiPoHasReplantSubtotal',
            minWidth: '110'
          }
        ]
      }
    ]
  },
  {
    title: '白破疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '需补种剂次数',
        key: 'b037NeedReplant',
        minWidth: '110'
      },
      {
        title: '已补种剂次数',
        key: 'b037HasReplant',
        minWidth: '110'
      }
    ]
  },
  {
    title: '麻疹类疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b059NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b059HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b060NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b060HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '小计',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'maFengNeedReplantSubtotal',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'maFengHasReplantSubtotal',
            minWidth: '110'
          }
        ]
      }
    ]
  },
  {
    title: 'A群流脑疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b040NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b040HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b041NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b041HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '小计',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'aqunLiuNaoNeedReplantSubtotal',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'aqunLiuNaoHasReplantSubtotal',
            minWidth: '110'
          }
        ]
      }
    ]
  },
  {
    title: 'A+C群流脑疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b045NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b045HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b046NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b046HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '小计',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'acqunLiuNaoNeedReplantSubtotal',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'acqunLiuNaoHasReplantSubtotal',
            minWidth: '110'
          }
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
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b032NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b032HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'b033NeedReplant',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'b033HasReplant',
            minWidth: '110'
          }
        ]
      },
      {
        title: '小计',
        hasChildrenTitle: true,
        list: [
          {
            title: '需补种剂次数',
            key: 'yiNaoJianDuHuoNeedReplantSubtotal',
            minWidth: '110'
          },
          {
            title: '已补种剂次数',
            key: 'yiNaoJianDuHuoHasReplantSubtotal',
            minWidth: '110'
          }
        ]
      }
    ]
  },
  {
    title: '甲肝疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '需补种剂次数',
        key: 'b069NeedReplant',
        minWidth: '110'
      },
      {
        title: '已补种剂次数',
        key: 'b069HasReplant',
        minWidth: '110'
      }
    ]
  }
]
