import url from '@/api/urls'
import axiosUtil from '@/api/axios'
// 时间轴——受种者轨迹查询接口



export function timeAxis(params) {
  console.log('params',params);
  
  const error = () => {
    let data = {
      data: {
          "success": false,
          "popMsg": "",
          "data": {
              "sec": 0.007,
              "data": null
          },
          "otherdata": null,
          "otherflag": null,
          "code": 521,
          "errorCode": null
      }
    }
    return new Promise((resolve, reject) => {
      resolve(data)
    })
  }

  if((params.fchildno  && params.cid == ''  && params.imuno == '')) {
    let res = window.trackDataTable.find(item => {
      return item.fchildno == params.fchildno
    })
    if(res != undefined) {
      let data = {
        data: {
            "success": true,
            "popMsg": null,
            "data": {
                "sec": 0.007,
                "data": res.trackData
            },
            "otherdata": null,
            "otherflag": null,
            "code": 200,
            "errorCode": null
        }
      }
      return new Promise((resolve, reject) => {
        resolve(data)
      })
    }else {
      return error()
    }
  }else if((params.fchildno === '' && params.cid && params.imuno == '')) {
    let res = window.trackDataTable.find(item => {
      return item.cid == params.cid
    })
    if(res != undefined) {
      let data = {
        data: {
            "success": true,
            "popMsg": null,
            "data": {
                "sec": 0.007,
                "data": res.trackData
            },
            "otherdata": null,
            "otherflag": null,
            "code": 200,
            "errorCode": null
        }
      }
      return new Promise((resolve, reject) => {
        resolve(data)
      })
    }else {
      return error()
    }
  }else if(params.imuno && params.fchildno === '' && params.cid=='') {
    let res = window.trackDataTable.find(item => {
      return item.imuno == params.imuno
    })
    if(res != undefined) {
      let data = {
        data: {
            "success": true,
            "popMsg": null,
            "data": {
                "sec": 0.007,
                "data": res.trackData
            },
            "otherdata": null,
            "otherflag": null,
            "code": 200,
            "errorCode": null
        }
      }
      return new Promise((resolve, reject) => {
        resolve(data)
      })
    }else {
      return error()
    }
  }else {
    return error()
  }
}

// // 地图——受种者轨迹查询接口
export function areaTrail(params) {
  console.log('params',params);
  const error = () => {
    let data = {
      data: {
          "success": false,
          "popMsg": "",
          "data": {
              "sec": 0.007,
              "data": null
          },
          "otherdata": null,
          "otherflag": null,
          "code": 521,
          "errorCode": null
      }
    }
    return new Promise((resolve, reject) => {
      resolve(data)
    })
  }
  if((params.fchildno  && params.cid == ''  && params.imuno == '')) {
    let res = window.mapDataTable.find(item => {
      return item.fchildno == params.fchildno
    })
    if(res != undefined) {
      let data = {
        data: {
            "success": true,
            "popMsg": null,
            "data": {
                "sec": 0.007,
                "data": res.mapData
            },
            "otherdata": null,
            "otherflag": null,
            "code": 200,
            "errorCode": null
        }
      }
      return new Promise((resolve, reject) => {
        resolve(data)
      })
    }else {
      return error()
    }
  }else if((params.fchildno === '' && params.cid && params.imuno == '')){
    let res = window.mapDataTable.find(item => {
      return item.cid == params.cid
    })
    console.log('mapres',res,window.mapData);
    
    if(res != undefined) {
      let data = {
        data: {
            "success": true,
            "popMsg": null,
            "data": {
                "sec": 0.007,
                "data": res.mapData
            },
            "otherdata": null,
            "otherflag": null,
            "code": 200,
            "errorCode": null
        }
      }
      return new Promise((resolve, reject) => {
        resolve(data)
      })
    }else {
      return error()
    }
  }else if(params.imuno && params.fchildno == '' && params.cid=='') {
    let res = window.mapDataTable.find(item => {
      return item.imuno == params.imuno
    })
    if(res != undefined) {
      let data = {
        data: {
            "success": true,
            "popMsg": null,
            "data": {
                "sec": 0.007,
                "data": res.mapData
            },
            "otherdata": null,
            "otherflag": null,
            "code": 200,
            "errorCode": null
        }
      }
      return new Promise((resolve, reject) => {
        resolve(data)
      })
    }else {
      return error()
    }
  }else {
    return error()
  }
}

// 数据交换——中间（门诊与档案数量查询）
export function centre(params) {
  let data={
    "data":{
        "success": true,
        "popMsg": null,
        "data": {
            "sec": 0.007,
            "data": window['centreData'+Number(params.type)]
        },
        "otherdata": null,
        "otherflag": null,
        "code": 200,
        "errorCode": null
    }
}
// return data
return new Promise(resolve=>{resolve(data)})
}
// 数据交换——左上（总交换量/流入流出）
export function upperLeft() {
  let data={
    "data":{
      "success": true,
      "popMsg": null,
      "data": {
          "sec": 0.503,
          "data": window.upperLeftData
      },
      "otherdata": null,
      "otherflag": null,
      "code": 200,
      "errorCode": null
    }
}
// return data
return new Promise(resolve=>{resolve(data)})
}
// 数据交换——左下（档案流入流出情况统计）
export function recordFlow(params) {
  let data={
    data:{
      "success": true,
      "popMsg": null,
      "data": {
          "sec": 0.124,
          "data":window['recordFlowData'+Number(params.type)]
      },
      "otherdata": null,
      "otherflag": null,
      "code": 200,
      "errorCode": null
    }
  }
  // return data
  return new Promise(resolve=>{resolve(data)})
}
// 数据交换——右上（跨域累计接种情况统计（年龄组/人次数））
export function age(params) {
  console.log(params.type,'000')
  let data={
      data:{
        "success": true,
        "popMsg": null,
        "data": {
            "sec": 0.002,
            "data": window['crossDomainInoculationAgeData'+Number(params.type)]
        },
        "otherdata": null,
        "otherflag": null,
        "code": 200,
        "errorCode": null
      }
  }
  // return data
  return new Promise(resolve=>{resolve(data)})
}
// 数据交换——右下（跨域累计接种情况统计（疫苗/人次数））
export function vaccine(params) {
  let data={
    data:{
        "success": true,
        "popMsg": null,
        "data": {
            "sec": 0.002,
            "data": window['crossDomainInoculationVaccineData'+Number(params.type)]
        },
        "otherdata": null,
        "otherflag": null,
        "code": 200,
        "errorCode": null
    }
  }
  // return data
  return new Promise(resolve=>{resolve(data)})
}
