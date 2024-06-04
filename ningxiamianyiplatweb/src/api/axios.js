import service from "axios";
import router from "@/router";
import qs from "query-string";
import smCrypto from "sm-crypto";

import { mainStore } from "@/store";
import { useWarningStore } from "@/store/warnMsg";
import paramsUtil from "./params.js";
import { ElNotification } from "element-plus";

const aesKey = "mGb93nbJif8154dA"; // 秘钥
const aesKey2 = "b6vhedgjior655gj"; // 数据库秘钥

// 加密方式
function Encrypt(word) {
  let sm4 = smCrypto.sm4;
  let keyStr = stringToHex(aesKey); //16进制串32位
  let encrypted = sm4.encrypt(word, keyStr);
  return encrypted.toString();
}
//字符转16进制串32位
function stringToHex(str) {
  var val = "";
  for (var i = 0; i < str.length; i++) {
    if (val == "") {
      val = str.charCodeAt(i).toString(16);
    } else {
      val += str.charCodeAt(i).toString(16);
    }
  }
  return val;
}

const axios = service.create({
  timeout: 500000, // 请求超时时间,
});

axios.defaults.withCredentials = false;
// 跳转错误页面
function goErrorPage(msg, code) {
  // store多次创建原因是pina在项目初始化时候还没创建好,报错
  const store = mainStore();
  store.errorCode = code;
  store.errorMsg = msg;
  router.push("/login");
}
// 提示错误信息
function errorMessage(msg) {
  ElNotification.error({ title: "错误", message: msg });
}
let i = 0;
/**
 * axios请求拦截器
 * @param {object} config axios请求配置对象
 * @return {object} 请求成功或失败时返回的配置对象或者promise error对象
 **/
axios.interceptors.request.use(
  (config) => {
    const store = mainStore();

    i++;
    store.loadingAxiosFlag = true;
    const token = sessionStorage.getItem("token");
    if (token) {
      config.headers["token"] = token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
/**
 * axios 响应拦截器
 * @param {object} response 从服务端响应的数据对象或者error对象
 * @return {object} 响应成功或失败时返回的响应对象或者promise error对象
 **/
axios.interceptors.response.use(
  (response) => {
    const store = mainStore();

    i--;
    if (i < 1) {
      store.loadingAxiosFlag = false;
    }

    if (response.data.code !== 200) {
      // errorMessage(response.data.msg);
    }
    return response.data;
  },
  (error) => {
    const store = mainStore();
    const warnStore = useWarningStore();

    i--;
    if (i < 1) {
      store.loadingAxiosFlag = false;
    }
    // 响应错误处理

    if (error.response.data) {
      const msgObj = warnStore.warnMsgObj;
      const data = paramsUtil.decodeParams(error.response.data);
      const code = error.response.status;
      if ([530, 521].indexOf(code) !== -1) {
        errorMessage(msgObj[code] ? msgObj[code] : data);
      } else if (code == 500) {
        errorMessage("登录过期,请重新登录~");
        goErrorPage(msgObj[code] ? msgObj[code] : data, code);
      } else if ([504, 404, 520, 524].indexOf(code) !== -1) {
        errorMessage(data.msg);
        goErrorPage(msgObj[code] ? msgObj[code] : data, code);
      } else {
        errorMessage(msgObj[code] ? msgObj[code] : data.msg);
        return Promise.reject(data);
      }
    }

    return Promise.reject(error);
  }
);
export default {
  // 下载文件
  downloadFile(url, params) {
    let oldIframe = document.getElementById("downloadTmpIframe");
    if (oldIframe) {
      document.body.removeChild(oldIframe);
    }
    let iframe = document.createElement("iframe");
    iframe.id = "downloadTmpIframe";
    iframe.name = "downloadTmpIframe";
    iframe.style.display = "none";
    document.body.appendChild(iframe);
    let a = document.createElement("a");
    if (!params) {
      params = {};
    }
    a.setAttribute(
      "href",
      url + "?params=" + encodeURIComponent(Encrypt(JSON.stringify(
        params)))
    );
    a.setAttribute("target", "downloadTmpIframe");
    if (!!window.ActiveXObject || "ActiveXObject" in window) {
      document.body.appendChild(a).click();
      document.body.removeChild(a);
      return;
    }
    if (document.createEvent) {
      var event = document.createEvent("MouseEvents");
      event.initEvent("click", true, true);
      return a.dispatchEvent(event);
    }
    a.click();
  },
  axioSessionId(url) {
    return new Promise(function (resolve, reject) {
      axios.get(url)
        .then(function (response) {
          sessionStorage.setItem("sessionid", response.data.data);
          resolve(response.data.data);
        })
    })
  },
  axiosLogin(url, params) {
    if (!url) {
      return;
    }
    if (!params) {
      params = {};
    }
    let that = this;
    let sessionid = sessionStorage.getItem("sessionid");
    params.jessionid = sessionid;
    if (!sessionid) {
      return new Promise(function (resolve, reject) {
        axios.get(url.getSessionId)
          .then(function (response) {
            sessionStorage.setItem("sessionid", response.data.data);
            sessionid = sessionStorage.getItem("sessionid");
            resolve(new Promise(function (resolve, reject) {
              document.cookie = 'JSESSIONID=' + sessionid;
              service.get(url + "?params=" + encodeURIComponent(Encrypt(JSON.stringify(
                params))), {
                withCredentials: true
              }).then((response) => {
                resolve(response);
              }).catch(res => {
                reject(res);
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
  axiosLogout(url, params) {
    if (!url) {
      return;
    }
    if (!params) {
      params = {};
    }
    const store = mainStore();
    let token = sessionStorage.getItem("token");
    if (!token && url.indexOf("login") == -1) {
      return goErrorPage("登录失效，请重新登录！");
    }
    params.token = token;
    params.loginname = store.userInfo.loginName;
    params.userName = store.userInfo.userName;
    params.timestamp = new Date().getTime();
    return new Promise(function (resolve, reject) {
      service.delete(url + "?params=" + encodeURIComponent(Encrypt(JSON.stringify(params)))).then((
        response) => {
        resolve(response);
      }).catch(res => {
        reject(res);
      })
    })
  },
  axiosGet(url, params) {
    if (!params) {
      params = {};
    }
    const store = mainStore();
    let token = sessionStorage.getItem("token");
    if (!token && url.indexOf("login") == -1) {
      return goErrorPage("登录失效，请重新登录！");
    }
    params.token = token;
    params.userName = store.userInfo.userName;
    params.loginname = store.userInfo.loginName;
    params.timestamp = new Date().getTime();
    params.unitname = store.userInfo.areaName;
    return new Promise(function (resolve, reject) {
      service.get(url + "?params=" + encodeURIComponent(Encrypt(JSON.stringify(
        params))), {
        withCredentials: false
      }).then((response) => {
        resolve(response);
      }).catch(res => {
        reject(res);
      })
    })
  },
  axiosPost(url, params) {
    if (!url) {
      return;
    }
    if (!params) {
      params = {};
    }
    const store = mainStore();
    let token = sessionStorage.getItem("token");
    if (!token && url.indexOf("login") == -1) {
      return goErrorPage("登录失效，请重新登录！");
    }
    params.token = token;
    params.userName = store.userInfo.userName;
    params.loginname = store.userInfo.loginName;
    params.loginName = store.userInfo.loginName;
    params.timestamp = new Date().getTime();
    params.unitname = store.userInfo.areaName;
    console.log('params', params)
    if (url.indexOf('queryArea') == -1) {
      store.loadingAxiosFlag = true;
    }
    return new Promise(function (resolve, reject) {
      service.post(url, qs.stringify({
        params: Encrypt(JSON.stringify(params)),
      })).then((response) => {
        store.loadingAxiosFlag = false;
        resolve(response);
      }).catch(res => {
        store.loadingAxiosFlag = false;
        reject(res);
      })
    })
  },
  // 不加密
  axiosPost1(url, params) {
    if (!url) {
      return;
    }
    if (params === undefined) {
      params = {};
    }
    return new Promise(function (resolve, reject) {
      axios
        .post(url, params, {
          headers: {
            "Content-Type": "application/json",
          },
        })
        .then((res) => {
          resolve(res);
        })
        .catch((res) => {
          reject(res);
        });
    });
  },

  axiosPut(url, params, files) {
    if (params === undefined) {
      params = {};
    }
    return new Promise(function (resolve, reject) {
      axios
        .put(url, paramsUtil.encodeParamsPost(params, files), {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((res) => {
          resolve(res);
        })
        .catch((res) => {
          reject(res);
        });
    });
  },
  axiosDelete(url, params) {
    if (!params) {
      params = {};
    }
    return new Promise(function (resolve, reject) {
      axios
        .delete(url + "?" + qs.stringify(paramsUtil.encodeParams(params)), {
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
        })
        .then((res) => {
          resolve(res);
        })
        .catch((res) => {
          reject(res);
        });
    });
  },
};
