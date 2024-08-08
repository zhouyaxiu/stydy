import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 用户列表
export function uerList (data) {
  return axiosUtil.axiosPost(url.userManage + '/uerList', data)
}
// 用户新增
export function addUser (data) {
  return axiosUtil.axiosPost(url.userManage + '/addUser', data)
}
// 修改用户
export function updateUser (data) {
  return axiosUtil.axiosPost(url.userManage + '/updateUser', data)
}
// 删除用户
export function delUser (data) {
  return axiosUtil.axiosPost(url.userManage + '/delUser', data)
}
