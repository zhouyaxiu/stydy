import {getCurrentInstance} from "vue"
export default ({roles})=>{
    // 1.获取组件实例
    const instance=getCurrentInstance();
    if(roles.includes("admin")){
        // 2.渲染插槽内容111
        return instance.slots && instance.slots.default()
    }
}