// 记得要在main.ts里面进行注册哦
import { createPinia } from 'pinia'
const pinia = createPinia()

//-------accessauthorization需要再次创建文件
import { defineStore } from 'pinia';
export const accessauthorization = defineStore({
  id: 'accessauthorization',
  state: () => ({
    // 
    location: [],
  }),
  actions: {
    locationEmpty(){
      this.location=[]
    },
    LocationChange(num) {
      this.location.push(num)
    }
  }
})

