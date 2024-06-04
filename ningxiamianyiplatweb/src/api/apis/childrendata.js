import url from "@/api/urls";
import axiosUtil from "@/api/axios";

// 受种者数查询
export const queryChildCount = (data) => {
  return axiosUtil.axiosPost(`${url.childCount}/queryChildCount`, data);
};
// 受种者数量查询导出
export const outputChildCount = (data) => {
  return axiosUtil.downloadFile(`${url.childCount}/outputChildCount`, data);
};
