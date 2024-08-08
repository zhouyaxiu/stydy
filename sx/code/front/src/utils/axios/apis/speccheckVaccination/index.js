import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 查验结果查询
export function findCheckResult(data) {
  return axiosUtil.axiosPost(url.highStu + '/findCheckResult', data)
}
// 导出查验结果表
export function outPutCheckResult(data) {
  return axiosUtil.downloadFile(url.highStu + '/outPutCheckResult', data)
}
// 删除查验结果
export function deleteCheckResult(data) {
  return axiosUtil.axiosPost(url.highStu + '/deleteCheckResult', data)
}
// 打印补种通知单（前）
export function queryPrintBuZhong(data) {
  return axiosUtil.axiosPost(url.highStu + '/queryPrintBuZhong', data)
}
