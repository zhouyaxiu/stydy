import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 变更日志查询接口
export function queryChangeLog (data) {
  return axiosUtil.axiosPost(url.log + '/queryChangeLog', data)
}
// 导出日志查询接口
export function queryExportLog (data) {
  return axiosUtil.axiosPost(url.log + '/queryExportLog', data)
}
// 查看受种者接口
export function queryExportDetail (data) {
  return axiosUtil.axiosPost(url.log + '/queryExportDetail', data)
}