import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 海南省儿童入托、入学预防接种查验情况报表查询接口
export function checkStateNumList (data) {
  return axiosUtil.axiosPost(url.rxrt + '/checkStateNumList', data)
}
//  海南省儿童入托、入学预防接种查验情况报表导出
export function checkStateNumOutput (data) {
  return axiosUtil.downloadFile(url.rxrt + '/checkStateNumOutput', data)
}
//  查询所有学年
export function findAllClaYears (data) {
  return axiosUtil.axiosPost(url.commURI + '/findAllClaYears', data)
}
