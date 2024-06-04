// 中英文数字
export const reg_cn_en_num = /^[A-Za-z0-9\u4e00-\u9fa5 ]{0,10}$/;
// 密码
export const reg_password =
  /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&*()])[\da-zA-Z!@#$%^&*()]{8,25}$/;
// 正整数
export const positive_interger = /^[1-9][0-9{}]{0,10}$/;
// 不能包含特殊字符< > & ' "
export const special_character = /[^<>&\’\”\'\"]$/;
