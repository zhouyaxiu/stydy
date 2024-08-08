import service from 'axios'
import router from '@/router'
import qs from 'qs'
// import store from '../../store/store'
import { mainStore } from '@/store/store'
import * as types from '@/store/types'
import * as consts from '@/utils/common/consts.js'
import paramsUtil from './params.js'
import { ElNotification } from 'element-plus'
const store = mainStore()

const axios = service.create({
  timeout: 1500000 // 请求超时时间,
})
axios.defaults.withCredentials = true
// 跳转错误页面
function goErrorPage(msg) {
  sessionStorage.setItem('error', msg)
  router.push('/errorPage')
}
// 提示错误信息
function errorMessage(msg) {
  console.log("=========errorMessage=============="+msg);
  ElNotification.error({ title: '错误', message: msg })
}


let i = 0
/**
 * axios请求拦截器
 * @param {object} config axios请求配置对象
 * @return {object} 请求成功或失败时返回的配置对象或者promise error对象
 **/
axios.interceptors.request.use(
  config => {
    i++
    // store.SET_LOADING_AXIOS(true)
    return config
  },
  error => {
    return Promise.reject(error)
  }
)
/**
 * axios 响应拦截器
 * @param {object} response 从服务端响应的数据对象或者error对象
 * @return {object} 响应成功或失败时返回的响应对象或者promise error对象
 **/
axios.interceptors.response.use(
  response => {
    i--
    if (i < 1) {
      // store.SET_LOADING_AXIOS(false)
    }
    if (response.data) {
      return paramsUtil.decodeParams(response.data)
    }
    return response.data
  },
  error => {
    i--
    if (i < 1) {
      // store.SET_LOADING_AXIOS(false)
    }
    // 响应错误处理
    if (error.response.data) {
      if (error.response.status === 530) {
        errorMessage(error.response.data)
        return
      }
      const data = paramsUtil.decodeParams(error.response.data)
      /* if (error.response.status === 504 || error.response.status === 404) {
        return goErrorPage(consts.ERROR_NO_NETWORK);
      } else */ if (
        error.response.status === 500
      ) {
        errorMessage(consts.ERROR_COMMON)
      } else if (error.response.status === 520) {
        goErrorPage(data)
      } else if (error.response.status === 521) {
        errorMessage(data)
      } else if (error.response.status === 522) {
        return Promise.reject(data)
      } else if (error.response.status === 523) {
        return Promise.reject(data)
      } else if (error.response.status === 524) {
        goErrorPage(consts.ERROR_NO_LOGIN)
      } /* else {
        errorMessage(consts.ERROR_UNKNOWN);
      } */
    }
    return Promise.reject(error)
  }
)
export default {
  // 下载文件
  downloadFile(url, params) {
    let oldIframe = document.getElementById('downloadTmpIframe')
    if (oldIframe) {
      document.body.removeChild(oldIframe)
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
    a.setAttribute('href', url + '?' + qs.stringify(paramsUtil.encodeParams(params)))
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
  },
  axiosGet(url, params) {
    if (!params) {
      params = {}
    }
    return new Promise(function(resolve, reject) {
      axios
        .get(url + '?' + qs.stringify(paramsUtil.encodeParams(params)), {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        })
        // .then(response => {
        //   return response
        // })
        .then(res => {
          resolve(res)
        })
        .catch(res => {
          reject(res)
        })
    })
  },
  axiosPost(url, params, files) {
    if (params === undefined) {
      params = {}
    }
    return new Promise(function(resolve, reject) {
      axios
        .post(url, paramsUtil.encodeParamsPost(params, files), {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        // .then(response => {
        //   return response
        // })
        .then(res => {
          resolve(res)
        })
        .catch(res => {
          reject(res)
        })
    })
  },
  axiosPut(url, params, files) {
    if (params === undefined) {
      params = {}
    }
    return new Promise(function(resolve, reject) {
      axios
        .put(url, paramsUtil.encodeParamsPost(params, files), {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        // .then(response => {
        //   return response
        // })
        .then(res => {
          resolve(res)
        })
        .catch(res => {
          reject(res)
        })
    })
  },
  axiosDelete(url, params) {
    if (!params) {
      params = {}
    }
    return new Promise(function(resolve, reject) {
      axios
        .delete(url + '?' + qs.stringify(paramsUtil.encodeParams(params)), {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        })
        // .then(response => {
        //   return response
        // })
        .then(res => {
          resolve(res)
        })
        .catch(res => {
          reject(res)
        })
    })
  }
}
