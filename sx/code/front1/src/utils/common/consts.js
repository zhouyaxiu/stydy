export const SEX = 'SEX' // 性别
export const FIDCARDTYPE = 'FIDCARDTYPE' // 身份证件类别
export const RESITYPE = 'RESITYPE' // 户籍类型
export const MANUFACTORY = 'MANUFACTORY' // 疫苗生产企业
export const PROFESSION = 'PROFESSION' // 职业(分层获取职业)
export const ALLPROFESSION = 'ALLPROFESSION' // 一次性获取全部的职业
// 统计时间最小日期到2008年
export const MINSELECTTIME = '2008-01-01'
export const STATISTICDATE = '2005-01-01'
export const OTHERDATE = '2015-01-01'
// 操作类型
export const OPTYPE_LOGIN = '0'
export const OPTYPE_INSERT = '1'
export const OPTYPE_UPDATE = '2'
export const OPTYPE_DELETE = '3'
export const OPTYPE_QUERY = '4'
export const OPTYPE_PRINT = '5'
export const OPTYPE_OUTPUT = '6'
// 动态表格-每列宽110px
export const FIXED_CELL_W = 110
// 固定列最少10个
export const FIXED_CELL_NUM = 10
// 活动表格-一共宽800px
export const FLOAT_TABLE_W = 800
/** 自定义按钮类型 */
export const CUSTOMBUTTONTYPES = {
  add: 'insert', // 新增
  delete: 'delete', // 删除
  save: 'update', // 编辑保存
  query: 'query', // 查询
  export: 'output', // 导出
  reset: 'reset', // 重置
  import: 'import', // 导入
  download: 'download', // 下载
  disscan: 'disscan', // 不可扫码
  scan: 'scan', // 扫码
  dissubmit: 'dissubmit', // 不可提交
  submit: 'submit', // 提交
  print: 'print', // 打印
  column: 'column', // 导出列设置
  alter: 'alter', // 修改
  check: 'check',//查验
  buzhong: 'buzhong',//打印补种通知单
  buzheng: 'buzheng',//打印补证通知单
  chayan: 'chayan',//打印查验通知单
  jiandang: 'jiandang',//打印建档通知单
  downloadImport: 'downloadImport',//导入模板下载
  batchImport: 'batchImport',//Excel批量导入
  failedQuery: 'failedQuery',//导入失败查询
  entry: 'entry',//录入
  excelImport: 'excelImport',//Excel导入
}
