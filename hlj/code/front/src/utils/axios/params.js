import CryptoJS from 'crypto-js'
import md5 from 'md5'
import * as consts from '@/utils/common/consts.js'
import { ElMessage } from 'element-plus'

const MD5_KEY = 'lvifj$%Gsdf2#BS@'
const sKeyApp = 'lWkfe$GD#GS3f9f4'

function encrypt(word) {
  const key = CryptoJS.enc.Utf8.parse(sKeyApp) // 16位
  let encrypted = ''
  if (typeof word === 'object') {
    word = JSON.stringify(word)
  }
  const srcs = CryptoJS.enc.Utf8.parse(word)
  encrypted = CryptoJS.AES.encrypt(srcs, key, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  })
  return encrypted.toString()
}

function decrypt(word) {
  const key = CryptoJS.enc.Utf8.parse(sKeyApp)
  word = CryptoJS.enc.Base64.parse(word).toString()
  const encryptedHexStr = CryptoJS.enc.Hex.parse(word)
  const srcs = CryptoJS.enc.Base64.stringify(encryptedHexStr).toString(
    CryptoJS.enc.Utf8
  )
  const decrypt = CryptoJS.AES.decrypt(srcs, key, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  })
  const decryptedStr = decrypt.toString(CryptoJS.enc.Utf8)
  // eslint-disable-next-line
  const result = decryptedStr.toString().replace(/\u0000/g, '')
  return result
}
export default {
  encodeParams(origin = {}) {
    //console.log('入参：', origin)
    origin.timestamp = new Date().getTime()
    const check = encrypt(origin)
    return {
      parameters: check,
      sign: md5(check + MD5_KEY),
      timestamp: new Date().getTime()
    }
  },
  encodeParamsPost(origin = {}, files) {
    //console.log('入参：', origin)
    origin.timestamp = new Date().getTime()
    const check = encrypt(origin)
    const data = new FormData()
    data.append('parameters', check)
    data.append('sign', md5(check + MD5_KEY))
    data.append('timestamp', new Date().getTime())
    if (files) {
      files.forEach(file => {
        data.append(file.name, file.file)
      })
    }
    return data
  },
  decodeParams(response) {
    if (!response.parameters) {
      return response
    }
    const parameters = decrypt(response.parameters)
    const sign = md5(response.parameters + MD5_KEY)
    if (sign !== response.sign) {
      ElMessage.error({ message: consts.ERROR_INVALID_MESSAGE })
      return
    }
    try {
      //console.log('返参：', JSON.parse(parameters))
      return JSON.parse(parameters)
    } catch (e) {
      //console.log('返参：', parameters)
      return parameters
    }
  }
}
