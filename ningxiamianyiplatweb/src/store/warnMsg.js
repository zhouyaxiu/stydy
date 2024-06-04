import { defineStore } from 'pinia'

export const useWarningStore = defineStore('warning', {
    state: () => {
        return {
            warnMsgObj: {
                504:'请查看网络是否链接！',
                404: '请查看网络是否链接！',
                500: '服务器开小差啦~',
                521: '服务器开小差啦~',
                524: '您的的登录信息已经过期！'
            },
            ERROR_INVALID_MESSAGE:'信息校验失败！'
            
        }
    },
    getters: {
        
    },
    actions: {
      
    }
})