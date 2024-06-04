import url from "@/api/urls";
import axiosUtil from "@/api/axios";

// 人力资源管理列表
export const queryEmployeeInfoList = (data) => {
  return axiosUtil.axiosGet(`${url.Hr}/queryEmployeeInfoList`, data);
};
// 人力资源信息删除
export const deleteEmployee = (data) => {
  return axiosUtil.axiosGet(`${url.Hr}/delete`, data);
};
// 人力资源信息
export const detail = (data) => {
  return axiosUtil.axiosGet(`${url.Hr}/detail`, data);
};
// 人力资源字典
export const getDicts = (data) => {
  return axiosUtil.axiosGet(`${url.Hr}/getDicts`, data);
};
// 人力资源详情保存
export const save = (data) => {
  return axiosUtil.axiosPost(`${url.Hr}/save`, data);
};
// 人力资源统计
export const statistics = (data) => {
  return axiosUtil.axiosGet(`${url.Hr}/statistics`, data);
};
// 人力资源统计导出
export const outputStatistics = (data) => {
  return axiosUtil.downloadFile(`${url.Hr}/outputStatistics`, data);
};
