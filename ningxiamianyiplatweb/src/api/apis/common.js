import url from "@/api/urls";
import axiosUtil from "@/api/axios";

// 通用--查询地区
export const queryArea = (data) => {
  return axiosUtil.axiosPost(`${url.Common}/queryArea`, data);
};
// 通用--查询所有地区
export const queryAllArea = (data) => {
  return axiosUtil.axiosPost(`${url.Common}/queryAllArea`, data);
};
// 通用--所有用户
export const queryAllUser = (data) => {
  return axiosUtil.axiosPost(`${url.Common}/queryAllUser`, data);
};
// 通用--所有模块
export const queryAllModule = (data) => {
  return axiosUtil.axiosPost(`${url.Common}/queryAllModule`, data);
};
/** ***************************************通用--显示设置*****************************************/
// 通用--受种者列表显示设置
export function getShowInfoSetting(data) {
  return axiosUtil.axiosPost(url.Common + '/queryColumns', data)
}
// 通用--保存受种者列表显示设置
export function saveShowInfoSetting(data) {
  return axiosUtil.axiosPost(url.Common + '/saveColumns', data)
}
// 通用--查询页面显示的户籍信息
export function queryShowResiType(data) {
  return axiosUtil.axiosPost(url.Common + '/queryShowResiType', data)
}
// 通用--户籍类型往后台传递时需要转换的户籍类型
export function changeResiType(data) {
  return axiosUtil.axiosPost(url.Common + '/changeResiType', data)
}
// 通用--查询新冠疫苗企业公共接口
export function xgVaccineFactory(data) {
  return axiosUtil.axiosPost(url.Common + '/xgVaccineFactory', data)
}
// 通用--查询新冠疫苗企业公共接口按条件查询
export function xgRptAgeFactory(data) {
  return axiosUtil.axiosPost(url.Common + '/xgRptAgeFactory', data)
}
// 通用--查询职业地区控件
export function queryProfessions(data) {
  return axiosUtil.axiosPost(url.Common + '/queryProfessions', data)
}