import url from '@/api/urls'
import axiosUtil from '@/api/axios'
import {recordFlow,upperLeft,centre,age,vaccine,timeAxis,areaTrail} from "./indexMock.js"
let mockData=window.isMockData
// 时间轴——受种者轨迹查询接口
export function queryTimeAxis(params) {
  if(mockData) return timeAxis(params)
  return axiosUtil.axiosPost(url.GET_DATA + '/timeAxis/query', params)
}

// 地图——受种者轨迹查询接口
export function queryAreaTrail(params) {
  if(mockData) return areaTrail(params)
  return axiosUtil.axiosPost(url.GET_DATA + '/areaTrail/query', params)
}

// 数据交换——中间（门诊与档案数量查询）
export function centreQuery(params) {
  if(mockData) return centre(params)
  return axiosUtil.axiosPost(url.GET_DATA + '/screenDisplay/centreQuery', params)
}
// 数据交换——左上（总交换量/流入流出）
export function upperLeftQuery(params) {
  if(mockData) return upperLeft()
  return axiosUtil.axiosPost(url.GET_DATA + '/screenDisplay/upperLeftQuery', params)
}
// 数据交换——左下（档案流入流出情况统计）
export function recordFlowQuery(params) {
  if(mockData) return recordFlow(params)
  return axiosUtil.axiosPost(url.GET_DATA + '/screenDisplay/recordFlowQuery', params)
}
// 数据交换——右上（跨域累计接种情况统计（年龄组/人次数））
export function ageQuery(params) {
  if(mockData) return age(params)
  return axiosUtil.axiosPost(url.GET_DATA + '/screenDisplay/crossDomainInoculationAgeQuery', params)
}
// 数据交换——右下（跨域累计接种情况统计（疫苗/人次数））
export function vaccineQuery(params) {
  if(mockData) return vaccine(params)
  return axiosUtil.axiosPost(url.GET_DATA + '/screenDisplay/crossDomainInoculationVaccineQuery', params)
}
