export const tableHeader = [
  // {
  //   title: '报告单位',
  //   type: 'index'
  // },
  {
    title: '报告单位',
    key: 'areaName',
    minWidth: '120',
  },
  {
    title: '托幼机构小学',
    hasChildrenTitle: true,
    list: [
      {
        title: '应查验数',
        key: 'shcNum',
        minWidth: '80'
      },
      {
        title: '查验数',
        key: 'checkSchNum',
        minWidth: '60'
      }
    ]
  },
  {
    title: '入托入学人数',
    key: 'allStuNum',
    minWidth: '120'
  },
  {
    title: '查验人数',
    key: 'checkStuNum',
    minWidth: '100'
  },
  {
    title: '持接种证人数',
    key: 'hasCard',
    minWidth: '120'
  },
  {
    title: '补接种证人数',
    key: 'needCard',
    minWidth: '120'
  },
  {
    title: '完成全程接种人数',
    key: 'qcjzCount',
    minWidth: '150'
  },
  {
    title: '需补种疫苗人数',
    key: 'needReplant',
    minWidth: '120'
  },
  {
    title: '完成全程补种人数',
    key: 'hasReplant',
    minWidth: '150'
  },
  {
    title: '卡介苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '应补种剂次',
        key: 'B001NeedReplant',
        minWidth: '100'
      },
      {
        title: '补种剂次',
        key: 'B001HasReplant',
        minWidth: '100'
      }
    ]
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
            title: '应补种剂次',
            key: 'B063NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B063HasReplant',
            minWidth: '100'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B064NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B064HasReplant',
            minWidth: '100'
          }
        ]
      },
      {
        title: '3',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B065NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B065HasReplant',
            minWidth: '100'
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
            title: '应补种剂次',
            key: 'B009NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B009HasReplant',
            minWidth: '100'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B010NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B010HasReplant',
            minWidth: '100'
          }
        ]
      },
      {
        title: '3',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B011NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B011HasReplant',
            minWidth: '100'
          }
        ]
      },
      {
        title: '4',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B012NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B012HasReplant',
            minWidth: '100'
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
            title: '应补种剂次',
            key: 'B015NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B015HasReplant',
            minWidth: '100'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B016NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B016HasReplant',
            minWidth: '100'
          }
        ]
      },
      {
        title: '3',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B017NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B017HasReplant',
            minWidth: '100'
          }
        ]
      },
      {
        title: '4',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B018NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B018HasReplant',
            minWidth: '100'
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
        title: '应补种剂次',
        key: 'B037NeedReplant',
        minWidth: '100'
      },
      {
        title: '补种剂次',
        key: 'B037HasReplant',
        minWidth: '100'
      }
    ]
  },
  {
    title: '麻腮风疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B059NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B059HasReplant',
            minWidth: '100'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B060NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B060HasReplant',
            minWidth: '100'
          }
        ]
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
          {
            title: '1',
            hasChildrenTitle: true,
            list: [
              {
                title: '应补种剂次',
                key: 'B040NeedReplant',
                minWidth: '100'
              },
              {
                title: '补种剂次',
                key: 'B040HasReplant',
                minWidth: '100'
              }
            ]
          },
          {
            title: '2',
            hasChildrenTitle: true,
            list: [
              {
                title: '应补种剂次',
                key: 'B041NeedReplant',
                minWidth: '100'
              },
              {
                title: '补种剂次',
                key: 'B041HasReplant',
                minWidth: '100'
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
                title: '应补种剂次',
                key: 'B045NeedReplant',
                minWidth: '100'
              },
              {
                title: '补种剂次',
                key: 'B045HasReplant',
                minWidth: '100'
              }
            ]
          },
          {
            title: '2',
            hasChildrenTitle: true,
            list: [
              {
                title: '应补种剂次',
                key: 'B046NeedReplant',
                minWidth: '100'
              },
              {
                title: '补种剂次',
                key: 'B046HasReplant',
                minWidth: '100'
              }
            ]
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
            title: '应补种剂次',
            key: 'B032NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B032HasReplant',
            minWidth: '100'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B033NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B033HasReplant',
            minWidth: '100'
          }
        ]
      }
    ]
  },
  // {
  //   title: '甲肝疫苗',
  //   hasChildrenTitle: true,
  //   list: [
  //     {
  //       title: '应补种剂次',
  //       key: 'B069NeedReplant',
  //       minWidth: '100'
  //     },
  //     {
  //       title: '补种剂次数',
  //       key: 'B069HasReplant',
  //       minWidth: '100'
  //     }
  //   ]
  // },
  {
    title: '甲肝灭活疫苗',
    hasChildrenTitle: true,
    list: [
      {
        title: '1',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B073NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B073HasReplant',
            minWidth: '100'
          }
        ]
      },
      {
        title: '2',
        hasChildrenTitle: true,
        list: [
          {
            title: '应补种剂次',
            key: 'B074NeedReplant',
            minWidth: '100'
          },
          {
            title: '补种剂次',
            key: 'B074HasReplant',
            minWidth: '100'
          }
        ]
      }
    ]
  },
  // {
  //   title: '*水痘疫苗',
  //   hasChildrenTitle: true,
  //   list: [
  //     {
  //       title: '1',
  //       hasChildrenTitle: true,
  //       list: [
  //         {
  //           title: '应补种剂次',
  //           key: 'B050NeedReplant',
  //           minWidth: '100'
  //         },
  //         {
  //           title: '补种剂次数',
  //           key: 'B050HasReplant',
  //           minWidth: '100'
  //         }
  //       ]
  //     },
  //     {
  //       title: '2',
  //       hasChildrenTitle: true,
  //       list: [
  //         {
  //           title: '应补种剂次',
  //           key: 'B132NeedReplant',
  //           minWidth: '100'
  //         },
  //         {
  //           title: '补种剂次数',
  //           key: 'B132HasReplant',
  //           minWidth: '100'
  //         }
  //       ]
  //     }
  //   ]
  // },
]
