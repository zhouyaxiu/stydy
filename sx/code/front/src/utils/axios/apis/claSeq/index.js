import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

//  新增轮次信息
export function addRound (data) {
  return axiosUtil.axiosPost(url.round + '/addRound', data)
}
//  根据年份和疫苗名称查询最新轮次名称
export function getRoundName (data) {
  return axiosUtil.axiosPost(url.round + '/getRoundName', data)
}
//  轮次信息查询
export function findRounds (data) {
  return axiosUtil.axiosPost(url.round + '/findRounds', data)
}

//  根据id删除轮次信息
export function deleteRounds (data) {
  return axiosUtil.axiosPost(url.round + '/deleteRounds', data)
}
//  根据班级
export function findHighClassYears (data) {
  return axiosUtil.axiosPost(url.commURI + '/findHighClassYears', data)
}
