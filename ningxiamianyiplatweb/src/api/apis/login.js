import url from "@/api/urls";
import axiosUtil from "@/api/axios";

// 登录
export const getSessionid = (data) => {
  return axiosUtil.axioSessionId(`${url.Login}/getSessionId`, data);
};
export const login = (data) => {
  return axiosUtil.axiosLogin(`${url.Login}/login`, data);
};
export const logout = (data) => {
  return axiosUtil.axiosLogout(`${url.Login}/logout`, data);
};
