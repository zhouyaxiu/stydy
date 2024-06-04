import url from "@/api/urls";
import axiosUtil from "@/api/axios";

// 质量控制
// 上传数据质量统计列表查询
export const noUploadList = (data) => {
  return axiosUtil.axiosPost(`${url.noUpload}/noUploadList`, data);
};
// 上传数据质量统计列表导出
export const output = (data) => {
  return axiosUtil.downloadFile(`${url.noUpload}/output`, data);
};
// 上传数据质量统计详情
export const detailList = (data) => {
  return axiosUtil.axiosPost(`${url.noUpload}/detailList`, data);
};
// 上传数据质量统计详情导出
export const detailOutput = (data) => {
  return axiosUtil.downloadFile(`${url.noUpload}/detailOutput`, data);
};
// 档案基本信息列表查询
export const infofullList = (data) => {
  return axiosUtil.axiosPost(`${url.childInfo}/infofullList`, data);
};
// 档案基本信息列表导出
export const infoOutput = (data) => {
  return axiosUtil.downloadFile(`${url.childInfo}/output`, data);
};
// 档案基本信息列表详情获取展示列
export const getColumnList = (data) => {
  return axiosUtil.axiosPost(`${url.childInfo}/getColumnList`, data);
};
// 档案基本信息列表详情详情查询
export const ColumnDetailList = (data) => {
  return axiosUtil.axiosPost(`${url.childInfo}/detailList`, data);
};