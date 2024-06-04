import service from 'axios'
import router from '@/router'
import qs from 'query-string'
 import { mainStore }  from '@/store'
 import { useWarningStore }  from '@/store/warnMsg'
import paramsUtil from './params.js'
import { ElNotification } from 'element-plus'
import  smCrypto from "sm-crypto";

const store = mainStore()
const warnStore=useWarningStore()

// SM4加密
function Encrypt (word) {
  let sm4 = smCrypto.sm4
  let keyStr = stringToHex(store.aesKey)// 16进制串32位
  let encrypted = sm4.encrypt(word, keyStr)
  return encrypted.toString()
}
// 字符转16进制串32位
function stringToHex (str) {
  var val = ''
  for (var i = 0; i < str.length; i++) {
    if (val == '') {
      val = str.charCodeAt(i).toString(16)
    } else {
      val += str.charCodeAt(i).toString(16)
    }
  }
  return val
}

const axios = service.create({
  timeout: 1500000 // 请求超时时间,
})
axios.defaults.withCredentials = false
// 跳转错误页面
function goErrorPage(msg,code) {
  store.errorCode = code
  store.errorMsg = msg
  router.push('/404')
}
// 提示错误信息
function errorMessage(msg) {
	console.log(msg);
  // ElNotification.error({ title: '错误', message: msg })
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
    store.loadingAxiosFlag= true
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
      store.loadingAxiosFlag= false
    }
    if (response.data) {
      return paramsUtil.decodeParams(response.data)
    }
    return response.data
  },
  error => {
    i--
    if (i < 1) {
      store.loadingAxiosFlag= false
    }
    // 响应错误处理
    console.log('错误信息展示', error);
    const msgObj= warnStore.warnMsgObj;
    if (error.response && error.response.data) {
    	const data = paramsUtil.decodeParams(error.response.data);
    	const code = error.response.status;
      if ([530, 500, 521].indexOf(code) !== -1) {
        errorMessage(msgObj[code]?msgObj[code]:data)
      } else if ([504, 404, 520, 524].indexOf(code) !== -1) {
        goErrorPage(msgObj[code]?msgObj[code]:data, code)
      } else { 
        errorMessage(msgObj[code]?msgObj[code]:data.message)
        return Promise.reject(data)

      }
    }else{
    	errorMessage(msgObj[521])
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
        .then(res => {
          resolve(res)
        })
        .catch(res => {
          reject(res)
        })
    })
  },
  axiosPost (url, params) {
    if (!url) {
      return
    }
    if (!params) {
      params = {}
    }
    params.timestamp = new Date().getTime()
    console.log('params', params)
    return new Promise(function (resolve, reject) {
      service.post(url, qs.stringify({
        params: Encrypt(JSON.stringify(params))
      })).then((response) => {
        resolve(response)
      }).catch(res => {
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
        .then(res => {
          resolve(res)
        })
        .catch(res => {
          reject(res)
        })
    })
  }
}
