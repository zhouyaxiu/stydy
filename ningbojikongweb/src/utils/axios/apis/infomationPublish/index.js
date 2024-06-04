import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 新增通知
export function addNotify (data) {
  return axiosUtil.axiosPost(url.notify + '/addNotify', data)
}
// 修改通知
export function updateNotify (data) {
  return axiosUtil.axiosPost(url.notify + '/updateNotify', data)
}
// 查询通知列表
export function searchNotifyList (data) {
  return axiosUtil.axiosPost(url.notify + '/notifyList', data)
}
// 删除通知
export function delNotify (data) {
  return axiosUtil.axiosPost(url.notify + '/delNotify', data)
}
// 通知详情
export function getNotify (data) {
  return axiosUtil.axiosPost(url.notify + '/detail', data)
}
