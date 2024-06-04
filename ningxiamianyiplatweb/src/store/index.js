import { defineStore } from "pinia";
import { useSidebarStore } from "./sidebar";
export const mainStore = defineStore("main", {
  state: () => {
    return {
      phone: "", //测试数据需要删除
      loadingAxiosFlag: false, //异步loading
      loadingPageFlag: false,
      currentPath: "", //当前路由
      currentTitle: "", //当前页面标题
      errorMsg: "啊哦~ 你所访问的页面不存在",
      errorCode: 404,
      userInfo: null, //用户信息
      token: "",// 登录token
      userName: '', // 用户昵称
      userCode: '', // 用户编码
      account: '', // 用户名
      aesKey: 'mGb93nbJif8154dA', // 秘钥
      aesKey2: 'b6vhedgjior655gj', // 数据库秘钥
      errorMsg: '', // 错误提示语
      title: null, // 当前页面标题
      titleIndex: null,
      breadcrumb: null,
      pagesize: 20, // 分页每页数量
      routers: [], // 权限
      id: '', // userId
      currentLevel: '',
      //菜单栏模块
      menuModules: [],
      // infoSetting组件弹窗关闭
      infoSettingClose: true,
      treeArr: []
    };
  },
  getters: {
    // 缓存特性：多次调用会展示出来
    phoneHidden(state) {
      console.log("getter被调用");
      return state.phone
        .toString()
        .replace(/^(\d{3})\d{4}(\d{4})$/, "$1****$2");
    },
    hasPermission(state, getters) {
      if (!state.currentPath || !state.modules) {
        return false;
      }
      for (const top of state.modules) {
        if (top.moduleRoute && state.currentPath.startsWith(top.moduleRoute)) {
          return getters.hasPagePermission;
        }
        if (!top.children) {
          continue;
        }
        for (const bottom of top.children) {
          if (
            bottom.moduleRoute &&
            state.currentPath.startsWith(bottom.moduleRoute)
          ) {
            return getters.hasPagePermission;
          }
        }
      }
      return false;
    },
    getMenus(state) {
      let menus = JSON.parse(sessionStorage.getItem("menuModules"));
      return state.menuModules.length ? state.menuModules : menus;
    },
  },
  actions: {
    //缓存当前选中的导航菜单信息
    setSelectedMenu(data) {
      sessionStorage.moduleId = data.id;
      sessionStorage.title = data.moduleName;
      sessionStorage.titleIndex = data.titleIndex;
      sessionStorage.breadcrumb = data.breadcrumb;
      this.moduleId = data.moduleId;
      this.title = data.title;
      this.breadcrumb = data.breadcrumb;
      this.titleIndex = data.titleIndex;
    },
    setToken(token) {
      this.token = token;
    },
    setMenus(menus) {
      this.menuModules = [...menus];
      let menustr = JSON.stringify(menus);
      sessionStorage.setItem("menuModules", menustr);
    },
    setTreeArr(treeArr) {
      this.treeArr = treeArr;
      sessionStorage.setItem("treeArr", JSON.stringify(treeArr));
    },
    setInfoSettingClose(value) {
      this.infoSettingClose = value;
    },
    clearStore() {
      this.token = "";
      this.userInfo = "";
      this.menuModules = [];
      this.treeArr = [];
      sessionStorage.removeItem('token')
      sessionStorage.removeItem('userInfo')
      sessionStorage.removeItem('menuModules')
      sessionStorage.removeItem('treeArr')
    }
  },
});
