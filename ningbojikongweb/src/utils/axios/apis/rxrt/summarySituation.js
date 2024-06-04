import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 浙江省入托、入学预防接种证查验情况汇总表 查询
export function queryInforInspect (data) {
  return axiosUtil.axiosPost(url.inforInspect + '/queryInforInspect', data)
}
//  浙江省入托、入学预防接种证查验情况汇总表 导出
export function outPutInforInspect (data) {
  return axiosUtil.downloadFile(url.inforInspect + '/outPutInforInspect', data)
}
