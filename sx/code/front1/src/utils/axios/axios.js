/**
 * axios封装
 */
import axios from 'axios'
import router from '../../router'
import ElementUI, { Loading } from 'element-ui'
// import Vue from 'vue'
import store from '../../store/store'
import * as types from '../../store/types'
// import CryptoJS from 'crypto-js'
import urls from './url'
import qs from 'qs'
// 加密方法
// function EncryptOld (word) {
//   let srcs = CryptoJS.enc.Utf8.parse(word)
//   let encrypted = CryptoJS.AES.encrypt(srcs, CryptoJS.enc.Utf8.parse(store.state.aesKey), {
//     mode: CryptoJS.mode.ECB,
//     padding: CryptoJS.pad.Pkcs7
//   })
//   return encrypted.ciphertext.toString() // encrypted.toString();
// }
// SM4加密
function Encrypt(word) {
  let sm4 = require('sm-crypto').sm4
  let keyStr = stringToHex(store.state.aesKey)// 16进制串32位
  let encrypted = sm4.encrypt(word, keyStr)
  return encrypted.toString()
}
// 字符转16进制串32位
function stringToHex(str) {
  var val = ''
  for (var i = 0; i < str.length; i++) {
    if (val === '') {
      val = str.charCodeAt(i).toString(16)
    } else {
      val += str.charCodeAt(i).toString(16)
    }
  }
  return val
}
const service = axios.create({
  timeout: 500000 // 请求超时时间
})

/* Vue.use(ElementUI); */
/**
 * axios请求拦截器
 * @param {object} config axios请求配置对象
 * @return {object} 请求成功或失败时返回的配置对象或者promise error对象
 **/
/* let loading;
let i=0; */
service.interceptors.request.use(config => {
  /* i++;
    if(!loading){
        loading = Vue.prototype.$loading({
          lock: true,
          text: '数据加载中...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
    } */
  // let sessionid = sessionStorage.getItem("sessionid");
  // if (sessionid != 'undefined') {
  //   config.headers.common['Authorization'] = 'JSESSIONID=' + sessionid;
  // }
  // if (config.headers["timeout"]) {
  //   config.timeout = config.headers["timeout"];
  //   delete config.headers["timeout"];
  // }
  return config
}, error => {
  return Promise.reject(error)
})

/**
 * axios 响应拦截器
 * @param {object} response 从服务端响应的数据对象或者error对象
 * @return {object} 响应成功或失败时返回的响应对象或者promise error对象
 **/

service.interceptors.response.use(response => {
  /* i--
  if(i==0){
    loading.close();
    loading = null;
  } */

  /* if(response.data && typeof response.data == "object"){
      if (response.data.code == 4003) {
          errorMessaage("抱歉，您没有权限查看该页面。");
          return Promise.reject(response);
      }else if(response.data.code == 4002) {
          errorMessaage("TOKEN失效,请重新登录。");
          return Promise.reject(response);
      }else if(response.data.code == 4001) {
          errorMessaage("请求参数不正确。",true);
          return Promise.reject(response);
      }else if(response.data.code === 1) {
          errorMessaage(response.data.msg||response.data.message,true);
          return Promise.reject(response);
      }
  } */
  return response.data
}, error => { // 响应错误处理
  if (error.response) {
    if (error.response.status === 522) {
      return Promise.reject(error.response.data)
    } else if (error.response.status === 520) {
      let loadingInstance = Loading.service({
        fullscreen: true
      })
      loadingInstance.close()
      errorMessaage(error.response.message || '服务器崩溃~')
      router.go(-1)
    } else if (error.response.status === 521) {
      /* if(i==0){ */
      let loadingInstance = Loading.service({
        fullscreen: true
      })
      loadingInstance.close()
      errorMessaage(error.response.data)
      // console.log('error', error.response);
      return Promise.reject(error.response.data)
      /* } */
    } else if (error.response.status === 523) {
      return Promise.reject(error.response.data)
    } else if (error.response.status === 524) {
      goErrorPage('登录失效，请重新登录！')
    } else if (error.response.status === 504 || error.response.status === 404) {
      goErrorPage('请查看网络是否链接！')
    } else if (error.response.status === 403) {
      goErrorPage('登录失效，请重新登录！')
    } else {
      let loadingInstance = Loading.service({
        fullscreen: true
      })
      loadingInstance.close()
      errorMessaage('服务器可能发生意外~')
    }
  }
  return Promise.reject(error)
})
// 弹出框配置
/* const errorOptionsPushPage = {
    type:"error",
    confirmButtonText:"返回登录页面",
    callback:backLogin,
    showClose:false,
    center:true
} */
const errorOptionsNoPushPage = {
  type: 'error',
  title: '提示',
  confirmButtonText: '确定',
  showClose: true,
  center: true
}
// 跳转错误页面
function goErrorPage(msg) {
  let loadingInstance = Loading.service({
    fullscreen: true
  })
  loadingInstance.close()
  store.commit(types.SET_ERROR, msg)
  router.push({
    name: 'errorPage'
  })
}
// 返回首页方法
/* function backLogin(){
    router.replace({name: 'login'});
} */
// 弹出错误信息弹框
function errorMessaage(msg) {
  ElementUI.MessageBox.alert(msg, errorOptionsNoPushPage)
}
/* function errorMessaage(msg,noPushPageFlag){
    if(noPushPageFlag){
        ElementUI.MessageBox.alert(msg, errorOptionsNoPushPage);
    }else{
        ElementUI.MessageBox.alert(msg, errorOptionsPushPage);
    }
} */
export default {
  // 导出
  downloadFile(url, params, fileName) {
    // console.log('开始导出');
    // console.log(url);
    // this.axiosGet(urls.checkLogout); //测试是否长时间未登录
    if (fileName === undefined) { fileName = '' }
    let oldiframe = document.getElementById('downloadTmpIframe')
    if (oldiframe) {
      document.body.removeChild(oldiframe)
    }
    let iframe = document.createElement('iframe')
    iframe.id = 'downloadTmpIframe'
    iframe.name = 'downloadTmpIframe'
    iframe.style.display = 'none'
    document.body.appendChild(iframe)
    let a = document.createElement('a')
    if (!params) {
      params = {}
    }
    let token = sessionStorage.getItem('token')
    if (!token && url.indexOf('login') === -1) {
      return goErrorPage('登录失效，请重新登录！')
    }
    // let getTimestamp = new Date().getTime()
    params.token = token
    params.userName = store.state.userName
    params.userId = sessionStorage.getItem('id')
    params.loginname = sessionStorage.getItem('account')
    params.modelName = sessionStorage.getItem('title')
    params.accountName = store.state.schName
    params.modelObject = store.state.modelObject
    params.areaCode = sessionStorage.getItem('schStationcode')
    params.areaLevel = sessionStorage.getItem('areaLevel')
    if (!fileName) {
      a.setAttribute('href', url + '?params=' + encodeURIComponent(encodeURIComponent(Encrypt(JSON.stringify(params)))))
      a.setAttribute('target', 'downloadTmpIframe')
      if (!!window.ActiveXObject || 'ActiveXObject' in window) {
        document.body.appendChild(a).click()
        document.body.removeChild(a)
        return
      }
      if (document.createEvent) {
        var event = document.createEvent('MouseEvents')
        event.initEvent('click', true, true)
        return a.dispatchEvent(event)
      }
      a.click()
    } else {
      this.download(url + '?params=' + encodeURIComponent(encodeURIComponent(Encrypt(JSON.stringify(params)))), fileName)
    }
  },
  download(URL, name) {
    const x = new window.XMLHttpRequest()
    x.open('GET', URL, true)
    x.responseType = 'blob'
    x.onload = () => {
      const url = window.URL.createObjectURL(x.response)
      const a = document.createElement('a')
      a.href = url
      a.download = name + '.xls'
      a.click()
    }
    x.send()
  },
  axioSessionId(url) {
    return new Promise(function (resolve, reject) {
      axios.get(url)
        .then(function (response) {
          sessionStorage.setItem('sessionid', response.data.data)
          resolve(response.data.data)
        })
    })
  },
  axiosLogin(url, params) {
    if (!url) {
      return
    }
    if (!params) {
      params = {}
    }
    // let that = this
    let sessionid = sessionStorage.getItem('sessionid')
    params.jessionid = sessionid
    params.loginname = params.userName
    params.accountName = params.userName
    params.modelName = '登录'
    params.modelObject = store.state.modelObject
    if (!sessionid) {
      return new Promise(function (resolve, reject) {
        axios.get(urls.getSessionId)
          .then(function (response) {
            sessionStorage.setItem('sessionid', response.data.data)
            sessionid = sessionStorage.getItem('sessionid')
            resolve(new Promise(function (resolve, reject) {
              document.cookie = 'JSESSIONID=' + sessionid
              service.post(url, qs.stringify({
                params: Encrypt(JSON.stringify(params))
              }), {
                withCredentials: true
              }).then((response) => {
                resolve(response)
              }).catch(res => {
                reject(res)
              })
            }))
          })
      })
    }
    document.cookie = 'JSESSIONID=' + sessionid
    return new Promise(function (resolve, reject) {
      service.post(url, qs.stringify({
        params: Encrypt(JSON.stringify(params))
      }), {
        withCredentials: true
      }).then((response) => {
        resolve(response)
      }).catch(res => {
        reject(res)
      })
    })
  },
  axiosPostWithoutLogin(url, params) {
    if (!url) {
      return
    }
    if (!params) {
      params = {}
    }
    // let that = this
    let sessionid = sessionStorage.getItem('sessionid')
    params.jessionid = sessionid
    if (!sessionid) {
      return new Promise(function (resolve, reject) {
        axios.post(urls.getSessionId)
          .then(function (response) {
            sessionStorage.setItem('sessionid', response.data.data)
            sessionid = sessionStorage.getItem('sessionid')
            resolve(new Promise(function (resolve, reject) {
              document.cookie = 'JSESSIONID=' + sessionid
              service.get(url + '?params=' + encodeURIComponent(Encrypt(JSON.stringify(
                params))), {
                withCredentials: true
              }).then((response) => {
                resolve(response)
              }).catch(res => {
                reject(res)
              })
            }))
          })
      })
    }
    document.cookie = 'JSESSIONID=' + sessionid
    return new Promise(function (resolve, reject) {
      service.post(url + '?params=' + encodeURIComponent(Encrypt(JSON.stringify(params))), {
        withCredentials: true
      }).then((response) => {
        resolve(response)
      }).catch(res => {
        reject(res)
      })
    })
  },
  axiosPostFile(url, params) {
    if (!url) {
      return
    }
    if (!params) {
      params = new FormData()
    }
    // let batchId = params.get('batchId')
    let token = sessionStorage.getItem('token')
    if (!token && url.indexOf('login') === -1) {
      return goErrorPage('登录失效，请重新登录！')
    }
    new Date().getTime()
    params.token = token
    params.userName = store.state.userName
    params.loginname = store.state.account
    // params.unitname = store.getters.getUserAreaName
    params.accountName = store.state.schName
    params.modelName = sessionStorage.getItem('title')
    params.modelObject = store.state.modelObject
    params.areaCode = sessionStorage.getItem('schStationcode')
    params.areaLevel = sessionStorage.getItem('areaLevel')
    let fileFormData = new FormData()
    for (const key in params) {
      if (params[key] instanceof File) {
        fileFormData.append(key, params[key])
        delete params[key]
      }
    }
    console.log(params)
    fileFormData.append('params', Encrypt(JSON.stringify(params)))
    console.log('params', fileFormData)
    return new Promise(function (resolve, reject) {
      service({
        method: 'post',
        url: url,
        data: fileFormData,
        dataType: 'json',
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then((res) => {
        resolve(res)
      }).catch(res => {
        reject(res)
      })
    })
  },
  axiosGet(url, params) {
    if (!url) {
      return
    }
    if (!params) {
      params = {}
    }
    let token = sessionStorage.getItem('token')
    if (!token && url.indexOf('login') === -1) {
      return goErrorPage('登录失效，请重新登录！')
    }
    let getTimestamp = new Date().getTime()
    params.token = token
    params.userName = store.state.userName
    params.unitname = store.getters.getUserAreaName
    params.loginname = store.state.account
    params.modelName = sessionStorage.getItem('title')
    params.accountName = store.state.schName
    params.modelObject = store.state.modelObject
    params.areaCode = sessionStorage.getItem('schStationcode')
    params.areaLevel = sessionStorage.getItem('areaLevel')
    return new Promise(function (resolve, reject) {
      service.get(url + '?timestamp=' + getTimestamp + '&params=' + encodeURIComponent(Encrypt(JSON.stringify(
        params)))).then((response) => {
          resolve(response)
        }).catch(res => {
          reject(res)
        })
    })
  },
  axiosDelete(url, params) {
    if (!url) {
      return
    }
    if (!params) {
      params = {}
    }
    let token = sessionStorage.getItem('token')
    if (!token && url.indexOf('login') === -1) {
      return goErrorPage('登录失效，请重新登录！')
    }
    params.token = token
    params.userName = store.state.userName
    params.userCode = store.state.userCode
    params.loginname = store.state.account
    params.modelName = sessionStorage.getItem('title')
    params.accountName = store.state.schName
    params.modelObject = store.state.modelObject
    params.areaCode = sessionStorage.getItem('schStationcode')
    params.areaLevel = sessionStorage.getItem('areaLevel')
    params.timestamp = new Date().getTime()
    return new Promise(function (resolve, reject) {
      service.delete(url + '?params=' + encodeURIComponent(Encrypt(JSON.stringify(params)))).then((
        response) => {
        resolve(response)
      }).catch(res => {
        reject(res)
      })
    })
  },
  axiosPost(url, params) {
    if (!url) {
      return
    }
    if (!params) {
      params = {}
    }
    console.log('params', params)
    let token = sessionStorage.getItem('token')
    if (!token && url.indexOf('login') === -1) {
      return goErrorPage('登录失效，请重新登录！')
    }
    params.token = token
    params.userName = store.state.userName
    params.userId = sessionStorage.getItem('id')
    params.schId = sessionStorage.getItem('schId')
    params.timestamp = new Date().getTime()
    params.unitname = store.getters.getUserAreaName
    params.loginname = store.state.account
    params.modelName = params.modelName ? params.modelName : sessionStorage.getItem('title')
    params.accountName = store.state.schName
    params.modelObject = store.state.modelObject
    params.areaCode = sessionStorage.getItem('schStationcode')
    params.areaLevel = sessionStorage.getItem('areaLevel')
    return new Promise(function (resolve, reject) {
      // let tt = Encrypt(JSON.stringify(params))
      service.post(url, qs.stringify({
        params: Encrypt(JSON.stringify(params))
      })).then((response) => {
        resolve(response)
      }).catch(res => {
        reject(res)
      })
    })
  },
  axiosJCLogout(url, params) {
    if (!url) {
      return
    }
    if (!params) {
      params = {}
    }
    let token = sessionStorage.getItem('token')
    if (!token && url.indexOf('login') === -1) {
      return goErrorPage('登录失效，请重新登录！')
    }
    return new Promise(function (resolve, reject) {
      service.post(url, qs.stringify(params)).then((response) => {
        resolve(response)
      }).catch(res => {
        reject(res)
      })
    })
  },
  axiosPut(url, params) {
    if (!url) {
      return
    }
    if (!params) {
      params = {}
    }
    let token = sessionStorage.getItem('token')
    if (!token && url.indexOf('login') === -1) {
      return goErrorPage('登录失效，请重新登录！')
    }
    params.token = token
    params.userName = store.state.userName
    params.loginname = store.state.account
    params.timestamp = new Date().getTime()
    params.unitname = store.getters.getUserAreaName
    params.accountName = store.state.schName
    return new Promise(function (resolve, reject) {
      service.put(url, qs.stringify({
        params: Encrypt(JSON.stringify(params))
      })).then((response) => {
        resolve(response)
      }).catch(res => {
        reject(res)
      })
    })
  }
}
