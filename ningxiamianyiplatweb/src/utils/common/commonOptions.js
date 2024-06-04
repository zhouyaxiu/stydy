// 受种者标识：全部-1  儿童0  成人 1
export const heraStateList = [
  { code: '', name: '全部' },
  { code: '0', name: '儿童' },
  { code: '1', name: '成人' }
]
// 迁入地：-1全部   0空  1非空
export const qrdList = [
  { code: '-1', name: '全部' },
  { code: '0', name: '空' },
  { code: '1', name: '非空' }
]
// 累计表 - 统计方式： 0截至每月月底数据累计  1截至当前时间前一天数据累计
export const tjTypeList = [
  { code: '0', name: '截至每月月底数据累计' },
  { code: '1', name: '截至当前时间前一天数据累计' }
]
// 统计表 - 统计方式： 0截至每月月底数据  1截至当前时间前一天数据
export const statisticsTypeList = [
  { code: '0', name: '截至每月月底数据' },
  { code: '1', name: '截至当前时间前一天数据' }
]
// 统计方式  1任意时段 2月统计表 3双月统计表  4年统计表
export const staticDateTypeList = [
  { code: '1', name: '任意时段' },
  { code: '2', name: '月统计表' },
  { code: '3', name: '双月统计表' },
  { code: '4', name: '年统计表' }
]
// 数据来源
export const sourceList = [
  { code: '', name: '全部' },
  // { code: '-1', name: '全部' },
  { code: 'A', name: '手机端接种' },
  { code: 'C', name: '客户端接种' }
]
// 疫苗制品
export const vaccNoList = [
  { code: '-1', name: '全部' },
  { code: 'G1', name: 'Vero细胞' },
  { code: 'G2', name: 'CHO细胞' },
  { code: 'G3', name: '腺病毒载体' }
]
// 接种剂次list
export const tjjcList = [
  { code: '-1', name: '全部' },
  { code: 'B655', name: '第1剂次' },
  { code: 'B656', name: '第2剂次' },
  { code: 'B676', name: '第3剂次' },
  { code: 'B677', name: '第4剂次' }
]
export const fbactidList = [
  { code: 'B655', name: '1' },
  { code: 'B656', name: '2' },
  { code: 'B676', name: '3' },
  { code: 'B677', name: '4' }
]
// 门诊类型
export const clinicTypeList = [
  { code: 'DAY', name: '日' },
  { code: 'WEEK', name: '周' },
  { code: 'PERIOD', name: '旬' },
  { code: 'MOMTH', name: '月' }
]
// 服务类型
export const fwTypeList = [
  { code: '01', name: '常规' },
  // { code: '02', name: '产科' },
  { code: '03', name: '成人' },
  { code: '04', name: '犬伤' },
  { code: '05', name: '破伤风接种' },
  { code: '06', name: '新冠接种' }
]
//  工作类型
export const workTypeList = [
  { code: '01', name: '社区卫生服务中心' },
  { code: '02', name: '乡镇卫生院' },
  { code: '03', name: '社区卫生服务站' },
  { code: '04', name: '村卫生室' },
  { code: '05', name: '其他' }
]
// 技术职称
export const technicalTitleSelect = [
  { id: '1', name: '正主任医师' },
  { id: '2', name: '主管医师' },
  { id: '3', name: '医师' },
  { id: '4', name: '医士' },
  { id: '5', name: '主管护师' },
  { id: '6', name: '护师' },
  { id: '7', name: '护士' },
  { id: '8', name: '防疫员' },
  { id: '9', name: '乡村医生' },
  { id: '10', name: '副主任医师' },
  { id: '11', name: '正主任技师' },
  { id: '12', name: '副主任技师' },
  { id: '13', name: '主管技师' },
  { id: '14', name: '技师' },
  { id: '15', name: '技士' },
  { id: '16', name: '正主任护师' },
  { id: '17', name: '副主任护师' },
  { id: '18', name: '其他' }
]
// 性别
export const genderSelect = [
  { id: '1', name: '男' },
  { id: '2', name: '女' }
]
// 专/兼职
export const positionTypeSelect = [
  { id: '1', name: '专职' },
  { id: '2', name: '兼职' }
]
// 培训证有无
export const certiFicate = [
  { id: '1', name: '有' },
  { id: '2', name: '无' }
]
// 学历
export const degreeSelect = [
  { id: '1', name: '博士' },
  { id: '2', name: '硕士' },
  { id: '3', name: '本科' },
  { id: '4', name: '大专' },
  { id: '5', name: '高中' },
  { id: '6', name: '中专' },
  { id: '7', name: '初中' },
  { id: '8', name: '小学' }
]
// 分析儿童疫苗选择
export const ymSelect = { 'B001': '卡介苗1', 'B009': 'OPV1', 'B010': 'OPV2', 'B011': 'OPV3', 'B012': 'OPV4', 'B015': 'DPT1', 'B016': 'DPT2', 'B017': 'DPT3', 'B018': 'DPT4', 'B059': 'MV1', 'B089': 'MMR1', 'B063': 'HBV1', 'B064': 'HBV2', 'B065': 'HBV3', 'B032': '乙脑1', 'B033': '乙脑2', 'B040': '流脑A1', 'B041': '流脑A2', 'B045': '流脑A+C1', 'B046': '流脑A+C2', 'B073': '甲肝灭活1', 'B074': '甲肝灭活2', 'B069': '甲肝减毒1', 'B037': '白破1' }
//  分析儿童-报表类型
export const childReportList = [
  { id: 0, name: '接种率报表' },
  { id: 1, name: '合格率报表' },
  { id: 2, name: '及时率报表' },
  { id: 3, name: '不合格率报表' }
]
// 接种单位管理-管理类型
export const manageTypeList = [{ code: '01', name: '疾病预防控制局' }, { code: '02', name: '医政医管局' }, { code: '03', name: '基层卫生健康司' }, { code: '04', name: '妇幼健康司 ' }, { code: '05', name: '国家中医药局医政司' }, { code: '06', name: '其他' }]
// 接种单位管理-业务类型
export const businessTypeList = [{ code: '01', name: '产科预防接种' }, { code: '02', name: '免疫规划疫苗预防接种' }, { code: '03', name: '青少年和成人预防接种' }, { code: '04', name: '狂犬病疫苗接种 ' }, { code: '05', name: '外伤后破伤风疫苗接种' }]
// 年龄组
export const agesList = [
  { id: '0', name: '小于3岁' },
  { id: '3', name: '3~6岁' },
  { id: '7', name: '7~11岁' },
  { id: '12', name: '12~14岁' },
  { id: '15', name: '15~17岁' },
  { id: '18', name: '18~49岁' },
  { id: '50', name: '50~54岁' },
  { id: '55', name: '55~59岁' },
  { id: '60', name: '60~64岁' },
  { id: '65', name: '65~69岁' },
  { id: '70', name: '70~79岁' },
  { id: '80', name: '≥ 80岁' }
]
// 双月统计 1-6
export const towMonthList = [
  { code: '1', name: '1' },
  { code: '2', name: '2' },
  { code: '3', name: '3' },
  { code: '4', name: '4' },
  { code: '5', name: '5' },
  { code: '6', name: '6' }
]
//是否合格
export const qualifiedList = [
  { code: '1', name: '合格' },
  { code: '0', name: '不合格' }
]
//身份证件类别
export const fidCardTypeList = [
  { code: '01', name: '居民身份证' },
  { code: '02', name: '居民户口簿' },
  { code: '03', name: '护照' },
  { code: '04', name: '军官证' },
  { code: '05', name: '驾驶证' },
  { code: '06', name: '港澳居民来往内地通行证' },
  { code: '07', name: '台湾居民来往内地通行证' },
  { code: '88', name: '暂未获取' },
  { code: '99', name: '其他法定有效证件' },
]
//性别
export const sexList = [
  { code: '未知', name: '未知的性别' },
  { code: '男', name: '男性' },
  { code: '女', name: '女性' },
  { code: '9', name: '未说明的性别' }
]