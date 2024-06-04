import { defineStore } from "pinia";

export const mainStore = defineStore('main', {
  state: () => {
    return {
      errorMsg: '啊哦~ 你所访问的页面不存在',
      errorCode: 404,
      aesKey: 'mGb93nbJif8154dA', // 秘钥
      aesKey2: 'b6vhedgjior655gj', // 数据库秘钥
    }
  },
  getters: {
  	
  },
  actions: {
  	
  }
})