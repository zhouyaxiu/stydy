import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 接种证查验汇总表
export function queryVacCertCheckStat (data) {
    return axiosUtil.axiosGet(url.checkInfo + '/queryVacCertCheckStat', data)
}
// 接种证查验汇总表导出
export function outputVacCertCheckStat (data) {
    return axiosUtil.downloadFile(url.checkInfo + '/outputVacCertCheckStat', data)
}
  