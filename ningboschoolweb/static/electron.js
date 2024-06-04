if (window.require) {
  // 通讯变量
  window.ipcRenderer = require('electron').ipcRenderer
  // 是否在桌面应用下
  window.electronFlag = true
}
