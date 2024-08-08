// 手动添加
import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

//  添加学生
export function manualEntry(data) {
  return axiosUtil.axiosPost(url.manualEntry + '/pageEntry', data)
}

//  是否可打印补证通知单
export function ifNotHasCard(data) {
  return axiosUtil.axiosPost(url.manualEntry + '/ifNotHasCard', data)
}