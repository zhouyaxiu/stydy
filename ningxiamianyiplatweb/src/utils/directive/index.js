/**
 * 自定义全局指令
 */

import btnPermi from './btnPermi'
import debounce from './debounce'
import dynamic from './dynamic'
import watermark from './waterMark'
const initUitlDirectives = function (app) {
  app.directive('btnPermi', btnPermi)
  app.directive('btnDebounce', debounce)
  app.directive('dynamic', dynamic)
  app.directive(watermark.name, watermark.directives);
  // app.directive(watermark.name, watermark.directives);
  // https://juejin.cn/post/7132620574198595597?searchId=2024051617131752536648826F4E2D8275
}

export default initUitlDirectives
