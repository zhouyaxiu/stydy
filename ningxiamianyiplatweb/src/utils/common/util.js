import { ElMessage, ElMessageBox, ElLoading } from 'element-plus'
import CryptoJS from 'crypto-js'
import SmCrypto from 'sm-crypto'
import { mainStore } from "@/store/index";
let loadingMask = null
export default {
  // SM4加密
  sm4(word) {
    let sm4 = SmCrypto.sm4
    const store = mainStore()
    let keyStr = this.stringToHex(store.aesKey) // 16进制串32位
    let encrypted = sm4.encrypt(word, keyStr)
    return encrypted.toString()
  },
  // sm4解密
  decodeAes(word) {
    if (!word) {
      return ''
    }
    const store = mainStore()
    let keyStr = this.stringToHex(store.aesKey2) // 16进制串32位
    let sm4 = SmCrypto.sm4
    let decryptedStr = sm4.decrypt(word, keyStr)
    return decryptedStr.toString().replace(/\u0000/g, '')
  },
  // 字符转16进制串32位
  stringToHex(str) {
    var val = ''
    for (var i = 0; i < str.length; i++) {
      if (val == '') {
        val = str.charCodeAt(i).toString(16)
      } else {
        val += str.charCodeAt(i).toString(16)
      }
    }
    return val
  },
  encodeAes(word) {
    if (!word) {
      return ''
    }
    const store = mainStore()
    let srcs = CryptoJS.enc.Utf8.parse(word)
    let encrypted = CryptoJS.AES.encrypt(srcs, CryptoJS.enc.Utf8.parse(store.aesKey2), {
      mode: CryptoJS.mode.ECB,
      padding: CryptoJS.pad.Pkcs7
    })
    return encrypted.ciphertext.toString() // 还原;
  },
  //全局显示提示信息
  showMsg(msg = '', type = 'info', duration = 3000, showClose = false) {
    ElMessage({
      message: msg,
      type: type, //提示类型-->info,success,error,worning
      duration: duration,
      'show-close': showClose,
    })
  },
  //全局显示确认消息弹窗
  showConfirmDialog(info = '', callback, title = '确认',) {
    ElMessageBox.confirm(info, title,
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        // type: 'warning',
        draggable: true,
      }
    )
      .then(() => {
        if (callback) callback('confirm')
      })
      .catch(() => {
        if (callback) callback('cancle')
      })
  },
  /**显示遮罩
   * @function showLoadingMask
   * @param {遮罩层目标元素，dom/'className'} targetEl 
   * @param {提示信息} text 
   */
  showLoadingMask(targetEl = document.body, text = '加载中...') {
    loadingMask = ElLoading.service({
      target: targetEl,
      lock: true,
      text: text,
      background: 'rgba(0, 0, 0, 0.7)',
    })
  },
  //隐藏遮罩
  hideLoadingMask() {
    if (loadingMask) loadingMask.close()
  }
}
