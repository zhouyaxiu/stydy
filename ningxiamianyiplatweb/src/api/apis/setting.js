import url from "@/api/urls";
import axiosUtil from "@/api/axios";

/** *************************************** 菜单管理 *****************************************/

// 查询所有菜单列表 (路由接口)
export const getAllMenu = (data) => {
  return axiosUtil.axiosPost(`${url.Setting}/menu/getAllMenu`, data);
};
// 树形查询所有的菜单接口
export const getTreeMenu = (data) => {
  return axiosUtil.axiosPost(`${url.Setting}/menu/treeMenu`, data);
};

/** *************************************** 角色管理 *****************************************/

// 获取所有角色
export const getAllRole = (data) => {
  return axiosUtil.axiosPost(`${url.Setting}/role/selectAll`, data);
};
// 新增,修改角色
export const addRole = (data) => {
  return axiosUtil.axiosPost(`${url.Setting}/role/insert`, data);
};
// 修改角色
export const editRole = (data) => {
  return axiosUtil.axiosPost(`${url.Setting}/role/update`, data);
};
// 角色分页查询
export const getRoleList = (data) => {
  return axiosUtil.axiosPost(`${url.Setting}/role/list`, data);
};
// 删除角色
export const deleteRole = (data) => {
  return axiosUtil.axiosPost(`${url.Setting}/role/delete`, data);
};
// 根据id查询角色信息
export const getRoleInfo = (data) => {
  return axiosUtil.axiosPost(`${url.Setting}/role/getById`, data);
};

/** *************************************** 用户登录日志 *****************************************/

// 用户登录日志
export const getUserLoginLog = (data) => {
  return axiosUtil.axiosPost(`${url.Setting}/loginLogList`, data);
};

/** *************************************** 地区管理 *****************************************/

// 地区管理 - 列表
export function queryAreaList(data) {
  return axiosUtil.axiosPost(`${url.AreaManager}/queryArea`, data)
}
// 地区管理 - 查看详情
export function getAreaById(data) {
  return axiosUtil.axiosPost(`${url.AreaManager}/getAreaById`, data)
}
// 地区管理 - 新增
export function insertArea(data) {
  return axiosUtil.axiosPost(`${url.AreaManager}/insertArea`, data)
}
// 地区管理 - 修改
export function updateArea(data) {
  return axiosUtil.axiosPost(`${url.AreaManager}/updateArea`, data)
}
// 地区管理 - 删除
export function deleteArea(data) {
  return axiosUtil.axiosPost(`${url.AreaManager}/deleteArea`, data)
}

/** *************************************** 系统日志 *****************************************/

// 日志查询 - 列表
export function queryLogList(data) {
  return axiosUtil.axiosPost(`${url.Setting}/logList`, data)
}

/** *************************************** 接种单位管理 *****************************************/

// 接种单位 - 列表
export function queryInoculationUnitList(data) {
  return axiosUtil.axiosPost(`${url.StationManager}/queryStations`, data)
}
// 接种单位 - 查看详情
export function getInoculationUnitDetail(data) {
  return axiosUtil.axiosPost(`${url.StationManager}/findSystionByCode`, data)
}
// 接种单位 - 新增
export function addInoculationUnit(data) {
  return axiosUtil.axiosPost(`${url.StationManager}/insertStation`, data)
}
// 接种单位 - 修改
export function editInoculationUnit(data) {
  return axiosUtil.axiosPost(`${url.StationManager}/updateSystion`, data)
}
// 接种单位 - 删除
export function delInoculationUnit(data) {
  return axiosUtil.axiosPost(`${url.StationManager}/deleteStation`, data)
}
// 接种单位 - 导出
export function exportInoculationUnit(data) {
  return axiosUtil.downloadFile(`${url.StationManager}/outputStation`, data)
}
// 接种单位迁移
export function inoculationMove(data) {
  return axiosUtil.axiosPost(url.StationManager + '/stationMove', data)
}
// 接种单位合并
export function inoculationMerge(data) {
  return axiosUtil.axiosPost(url.StationManager + '/stationMerge', data)
}
// 删除某接种单位数据
export function deleteStationAllMess(data) {
  return axiosUtil.axiosPost(url.StationManager + '/deleteStationAllMess', data)
}

/** *************************************** 公告板管理 *****************************************/

// 公告板管理 - 列表
export function getNoticeManageList(data) {
  return axiosUtil.axiosPost(url.Bbs + '/bbsList', data)
}
// 公告板管理 - 详情
export function getNoticeDetial(data) {
  return axiosUtil.axiosPost(url.Bbs + '/bbsDetail', data)
}
// 公告板管理 - 删除
export function delNotice(data) {
  return axiosUtil.axiosPost(url.Bbs + '/delBbs', data)
}
// 公告板管理 - 新增
export function addNotice(data) {
  return axiosUtil.axiosPost(url.Bbs + '/addBBS', data)
}
// 公告板管理 - 编辑
export function updateNotice(data) {
  return axiosUtil.axiosPost(url.Bbs + '/updateBbs', data)
}

/** *************************************** 系统角色管理 *****************************************/

// 系统角色管理 - 角色下拉框
export function getRoleSelect(data) {
  return axiosUtil.axiosPost(url.Role + '/allRoleList ', data)
}
// 系统角色管理 - 所有模块
export function getAllModuleList(data) {
  return axiosUtil.axiosPost(url.Role + '/treeModuleList', data)
}
// 系统角色管理 - 角色详情
export function getRoleDetial(data) {
  return axiosUtil.axiosPost(url.Role + '/roleDetail', data)
}
// 系统角色管理 - 删除角色
export function delRole(data) {
  return axiosUtil.axiosPost(url.Role + '/deleteRole', data)
}
// 系统角色管理 - 新增角色
export function addSystemRole(data) {
  return axiosUtil.axiosPost(url.Role + '/addRole', data)
}
// 系统角色管理 - 更新角色
export function updateRole(data) {
  return axiosUtil.axiosPost(url.Role + '/updateRole', data)
}


/** *************************************** 投诉管理 *****************************************/

// 投诉列表
export function queryComlains(data) {
  return axiosUtil.axiosPost(url.complain + '/queryComlains ', data)
}
// 投诉详情-根据id查询具体的信息
export function queryComlainDetail(data) {
  return axiosUtil.axiosPost(url.complain + '/queryComlainDetail ', data)
}
// 根据id删除投诉信息
export function deleteComplain(data) {
  return axiosUtil.axiosPost(url.complain + '/deleteComplain ', data)
}
// 新增投诉信息
export function saveComplain(data) {
  return axiosUtil.axiosPost(url.complain + '/saveComplain ', data)
}