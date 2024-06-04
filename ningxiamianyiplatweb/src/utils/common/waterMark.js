/**
 * 页面水印
 */
import { mainStore } from "@/store/index";
import WaterMark from "@/utils/common/waterMarkmin";
export const setWaterMark = () => {
  const store = mainStore()
  //自定义全局公共方法引用
  const { appContext } = getCurrentInstance();
  const globalProxy = appContext.config.globalProperties;
  const userName = store.userInfo.userName
  const loginTime = globalProxy.$dateUtil.currentDate() + ' ' + globalProxy.$dateUtil.todayMinutes()
  let waterMarkObj = null;
  waterMarkObj = new WaterMark({
    watermark_id: "",
  });
  waterMarkObj.load({
    watermark_txt: `免疫规划信息系统-${userName}-${loginTime}`,
    // watermark_color:'#5579ee',            //水印字体颜色
    // watermark_fontsize:'24px',          //水印字体大小
    watermark_alpha: 0.2, //水印透明度，要求设置在大于等于0.005
    // watermark_angle:135,                 //水印倾斜度数
    watermark_width: 460, //水印宽度
    watermark_height: 180, //水印长度
  });
}
