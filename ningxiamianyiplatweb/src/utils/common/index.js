/**
 * 全局公共方法挂载入口
 */
import dateUtil from "./dateutil";
import util from "./util";
import * as consts from "./consts";
import * as commonOptions from "./commonOptions";

export default (app) => {
  //公共日期处理方法
  app.config.globalProperties.$dateUtil = dateUtil;
  app.config.globalProperties.$util = util;
  app.config.globalProperties.$consts = consts;
  app.config.globalProperties.$commonOptions = commonOptions;
};
