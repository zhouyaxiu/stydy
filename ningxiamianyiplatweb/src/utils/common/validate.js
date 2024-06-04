export const ptn = {
  // 最多n位小数
  decimal: function (decimallen) {
    return '^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,' + decimallen + '})))$'
  },
  // 纯英文
  checkEnCode: function (min = 0, max = 9999) {
    return '^[A-Za-z\u4e00-\u9fa5 ]{' + min + ',' + max + '}$'
  },
  // 纯下划线
  checkUnderLine: function (min = 0, max = 9999) {
    return '^[_\u4e00-\u9fa5 ]{' + min + ',' + max + '}$'
  },
  // 中英文数字
  cn_en_num: function (min = 0, max = 9999) {
    return '^[A-Za-z0-9\u4e00-\u9fa5 ]{' + min + ',' + max + '}$'
  },
  // 中英文数字点
  cn_en_num_point: function (min = 0, max = 9999) {
    return '^[A-Za-z0-9\u4e00-\u9fa5.{}]{' + min + ',' + max + '}$'
  },
  // 中英文数字冒号
  cn_en_num_colon: function (min = 0, max = 9999) {
    return '^[A-Za-z0-9\u4e00-\u9fa5:{}]{' + min + ',' + max + '}$'
  },
  // 正整数方式二,允许包含0，如10,20等
  positive_interger_two: function () {
    return '^[1-9][0-9{}]{0,10}$'
  },
  request_api: function (min = 0, max = 9999) {
    return '^(((https|http)?:\\/\\/)|(\\/))[^\\s]{' + min + ',' + max + '}$'
  },
  // IP + 端口号
  ip_and_port: function () {
    return '^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\:([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])$'
  },
  // 中文数字
  cn_num: function (min = 0, max = 9999) {
    return '^[0-9\u4e00-\u9fa5]{' + min + ',' + max + '}$'
  },
  // 英文或数字
  en_num: function (min = 0, max = 9999) {
    return '^[A-Za-z0-9 ]{' + min + ',' + max + '}$'
  },
  // 英文和数字组合
  en_and_num: function (min = 0, max = 9999) {
    return '^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{' + min + ',' + max + '}$'
  },
  // 英文数字反斜杠
  en_num_backslash: function (min = 0, max = 9999) {
    return '^[/a-zA-Z0-9{}}]{' + min + ',' + max + '}$'
  },
  // 英文数字下划线
  en_num_line: function (min = 0, max = 9999) {
    return '^[_a-zA-Z0-9 ]{' + min + ',' + max + '}$'
  },
  // 英文下划线
  en_line: function (min = 0, max = 9999) {
    return '^[_a-zA-Z ]{' + min + ',' + max + '}$'
  },
  // 英文数字点
  en_num_point: function (min = 0, max = 9999) {
    return '^[a-zA-Z0-9.' +
      '' +
      ' ]{' + min + ',' + max + '}$'
  },
  // 中文、英文、数字及下划线
  cn_en_num_line: function (min = 0, max = 9999) {
    return '^[\u4e00-\u9fa5_a-zA-Z0-9 ]{' + min + ',' + max + '}$'
  },
  // 数字
  positive_num: function () {
    return '^[0-9]+\\d*$'
  },
  // 正整数
  positive_interger: function () {
    return '^[1-9]+\\d*$'
  },
  // 中文
  cn: function (min = 0, max = 9999) {
    return '^[\u4e00-\u9fa5]{' + min + ',' + max + '}$'
  },
  // 英文
  en: function (min = 0, max = 9999) {
    return '^[A-Za-z ]{' + min + ',' + max + '}$'
  },
  // 中文、英文、数字及中划线
  cn_en_num_cen_line: function (min = 0, max = 9999) {
    return '^[A-Za-z0-9-\u4e00-\u9fa5 ]{' + min + ',' + max + '}$'
  },
  // 英文中文
  en_cn: function (min = 0, max = 9999) {
    return '^[A-Za-z\u4e00-\u9fa5]{' + min + ',' + max + '}$'
  },
  // 英文中文分号
  en_cn_semicolon: function (min = 0, max = 9999) {
    return '^[;A-Za-z\u4e00-\u9fa5]{' + min + ',' + max + '}$'
  },
  // 通配限制长度
  all_match: function (min = 0, max = 9999) {
    return '^[\\s\\S]{' + min + ',' + max + '}$'
  },
  // 英文,点，中划线和下划线
  en_dot_line: function (min = 0, max = 9999) {
    return '^[-A-Za-z\._]{' + min + ',' + max + '}$'
  },
  // 数字
  number: function (min = 0, max = 9999) {
    return '^[0-9]{' + min + ',' + max + '}$'
  },
  // 数字 01<=xx<=98
  number2: function () {
    return '(^[0-9]{1}[1-8]{1}|^[1-8]{1}[0-9]{1}|^[0]{1}[9]{1}|^[9]{1}[0]{1})$'
    // return '^[0-9]{10,60}$'
  },
  number_dot: function () {
    return '/^\\d+(\\.\\d+)?$/'
  },
  // 英文和常用字符
  en_common_character: function (min = 0, max = 9999) {
    return '^[^0-9^\u4e00-\u9fa5^ ]{' + min + ',' + max + '}$'
  },
  // 不带空白字符英文数字
  pure_en_num: function (min = 0, max = 9999) {
    return '^[A-Za-z0-9]{' + min + ',' + max + '}$'
  },
  // 邮箱地址
  email: function (min = 0, max = 9999) {
    return '^[a-zA-Z0-9.-_\u4e00-\u9fa5]+@[a-zA-Z0-9-_]+(\\.[A-Za-z0-9-_]+)+$'
  },
  // 身份证号码
  carid: function () {
    // return '^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$|(^\d{17}(\d|X|x)$)'
    // return '(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)'; //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
    return '^(^\\d{15}$)|\\d{6}(18|19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[X])$'
  },
  // 手机号码
  mobile: function () {
    return '^1[3456789]\\d{9}$'
  },
  // 座机号码
  phone: function () {
    return '^((\\d{3,4})|\\d{3,4}-|\\s)?\\d{7,14}$'
  },
  /// (\d{2,5}-\d{7,8})/
  // 座机号码
  phone2: function () {
    return '^(\\d{3,4}-\\d{7,8})'
  },
  // 电话号码，手机和座机
  phoneall: function () {
    return '(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)'
  },
  // 用户密码
  userpassword: function () {
    // return '([0-9]+[a-zA-Z]+|[a-zA-Z]+[0-9]+)[0-9a-zA-Z]*';
    return '^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$'
  },
  // 最多不超过2位小数的正数
  doseRtn: function () {
    return '(^[1-9]([0-9]+)?([.][0-9]{1,2})?$)|(^[1-9][.][0-9]([0-9])?$)|(^0[.][0-9][0-9]{0,1}$)|(^0$)|(^0[.]0[0-9]$)'
  },
  // 最多不超过2位小数的正数
  doseOneRtn: function () {
    return '(^[1-9]([0-9]+)?([.][0-9]{1})?$)|(^[1-9][.][0-9]([0-9])?$)|(^0[.][1-9][0-9]{0,1}$)|(^0[.]0[1-9]$)'
  },
  // 最多不超过2位小数的正数
  doseRtnRange: function (min = 0, max = 9999) {
    return '^(\\d{' + min + ',' + max + '}|\\d{' + min + ',' + max + '}\\.\\d{1,2})$'
  },
  // 最多不超过2位小数的正数
  doseRtnPrice: function (min = 0, max = 6) {
    return '^(\\d{' + min + ',' + max + '}|\\d{' + min + ',' + max + '}\\.\\d{1,2})$'
  },
  // 校验日期格式是否正确
  isDate: function () {
    return '^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-' +
      '(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})' +
      '(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))$'
  },
  isDateTime: function () {
    return '^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-' +
      '(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})' +
      '(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+' +
      '([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]).([0-9]{3})$'
  },
  //
  spaceRtn: function () {
    return '[^\\s]+'
  },
  // QQ号码 只能为数字且最少5位最长为19位 第一位1-9之间的数字，第二位0-9之间的数字(4, 19)
  checkQQnum: function (min = 0, max = 9999) {
    return '^[1-9][0-9]{' + min + ',' + max + '}$'
  },
  // 微信号 必须以字母开头，可以使用6-20位数字、字母、下划线、减号或它们的组合
  checkWXnum: function () {
    return '^[a-zA-Z_]{1}([-_a-zA-Z0-9]{5,19})$'
  }
}
