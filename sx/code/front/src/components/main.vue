<template>
  <el-container class="main-container">
    <!--:style="'width:100%;height:calc(100% / ' + scale + ')'"-->
    <el-main
      class="scroll-item"
      :style="'width:100%;height:'+heightNum"
    >
      <ss-menu-top v-if="isTopShow" />
      <div style="display:flex;" v-if="isTopShow">
        <div :style="'width:'+leftW">
          <ss-menu-left
            v-if="isShow"
            :foldFlag="foldFlag"
            :leftHeight="leftHeight"
            @foldHandler="foldHandler"
          />
        </div>
        <div :style="'width:'+rightW+';padding:10px 26px 0px 24px;'">
          <transition
            :name="isIE?'':'ss-fade'"
            mode="out-in"
          >
            <!--只缓存以 cache 开头的组件（使用正则表达式，需使用 v-bind）-->
            <keep-alive :include="/^cache.* /">
              <!--key变化可以让直接点击不缓存-->
              <router-view
                :key="$route.name+timestamp"
                ref="page"
              />
            </keep-alive>
          </transition>
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import menuTop from '@/components/menu/menuTop'
import menuLeft from '@/components/menu/menuLeft'
import {} from '@/utils/axios/api'
import { JiChengloginIn } from '../utils/axios/api'
import { modules } from '@/plugins/menu'
export default {
  name: 'index',
  mixins: [],
  components: {
    'ss-menu-top': menuTop,
    'ss-menu-left': menuLeft
  },
  data () {
    return {
      isShow: true,
      heightNum: 'calc(' + 100 / window.scale + 'vh)',
      timestamp: new Date().getTime(),
      scale: window.scale,
      isIE: window.isIE(),
      foldFlag: false,
      leftW: '320px',
      rightW: 'calc(100% - 302px)',
      isTopShow:false
    }
  },
  watch: {
    foldFlag (newVal) {
      this.leftW = '320px'
      this.rightW = 'calc(100% - 302px)'// 402 = 352（左侧宽度）+ 24（左内边距）+ 26（右内边距）
      if (newVal) {
        this.leftW = '42px'
        this.rightW = 'calc(100% - 92px)'// 92 = 42（左侧宽度）+ 24（左内边距）+ 26（右内边距）
      }
    }
  },
  computed: {
    leftHeight () {
      return window.winHeight - 64
    }
  },
  methods: {
    foldHandler (newVal) {
      this.foldFlag = newVal
      this.isShow = false

      // 因左侧菜单显示or隐藏 调整右侧表格的宽度
      this.$nextTick(() => {
        this.isShow = true
        if (!this.$refs.page.$refs.tabPage) return
        if (newVal) {
          this.$refs.page.$refs.tabPage.setWidth()
        } else {
          this.$refs.page.$refs.tabPage.setWidth2()
        }
      })
    },
    loginFun(params, isJC, jsptPassword) {
      let that = this
      console.log('登录信息', params, jsptPassword)
      JiChengloginIn(params)
        .then((response) => {
          console.log('login-res', response)
          sessionStorage.tokenBack=''
          sessionStorage.response = JSON.stringify(response)
          that.$store.commit(that.$types.LOGIN, response)
          that.$store.commit(that.$types.MODELOBJECT, response)
          if (response.errorCode === '0') {
            that.$store.commit(that.$types.ACCOUNT, response.userinfo.loginName)
            sessionStorage.setItem('jsptPassword', jsptPassword)
          } else if (response.errorCode === '1') {
            that.$util.errorMsg('查询参数异常！')
            return
          } else if (response.errorCode === '2') {
            that.$util.errorMsg('Token验证失败')
            return
          } else if (response.errorCode === '3') {
            that.$util.errorMsg('Token超时（Token有效时间一分钟）')
            return
          } else if (response.errorCode === '4') {
            that.$util.errorMsg('当前用户不允许登录第三方查验系统')
            return
          }else{
            that.$util.errorMsg('集成接口返回无数据')
            this.$router.back();
            window.history.go(-1);
			      sessionStorage.setItem('isJC', false)
            return
          }
          that.$store.commit(that.$types.USERNAME, response.userinfo)
          sessionStorage.areaname=response.userinfo.areaName
          sessionStorage.areacode=response.userinfo.areaCode
          sessionStorage.userName=response.userinfo.userName
          let modules2;
          //如果是疾控则去掉信息查验，补种情况反馈表，通知单模板管理
          //如果是疾控则去掉信息扫码
          let modulesNew=JSON.parse(JSON.stringify(modules));
          //如果是疾控则去掉信息扫码 如果是疾控则去掉信息查验，补种情况反馈表，通知单模板管理
          if(response.arealevel !== "4"){
            if(response.userinfo.userType==1){
              modulesNew[1].supModules = modulesNew.filter(item => item.id=='M2')[0].supModules.filter(itm=>itm.id!='M29')
            }
            modulesNew[1].supModules = modulesNew.filter(item => item.id=='M2')[0].supModules.filter(itm=>itm.id!='M23')
            modulesNew[1].supModules = modulesNew.filter(item => item.id=='M2')[0].supModules.filter(itm=>itm.id!='M28')
            modulesNew[2].supModules = modulesNew.filter(item => item.id=='M3')[0].supModules.filter(itm=>itm.id!='M34')
            modules2 = modulesNew
          } else {
            if(response.userinfo.userType==1){
              modulesNew[1].supModules = modulesNew.filter(item=>item.id=='M2')[0].supModules.filter(itm=>itm.id != 'M29')
            }
            modules2 = modulesNew
          }
          that.$store.dispatch(that.$types.SET_USERAREA_ACTION, modules2)
          // that.loading = true
          sessionStorage.setItem('isJC', isJC)
          // 2022-11-2 去除强制修改密码功能
          // if (!that.pwdEdit(response.userinfo)) {
          // 清除权限按钮缓存
          that.$store.commit(that.$types.RESET_PAGEBTN)
          // 清除权限按钮缓存
          document.getElementById('app').scrollTop = 0
          this.isTopShow=true;
          that.closeFullScreen()
        })
        .catch((res) => {
          this.$router.back();
          window.history.go(-1);
          sessionStorage.setItem('isJC', false)
        })
    },
  },
  created () {
    this.setScrollBar()
    this.showDigContent = true
    this.$emit('destoryScrollbar')
    if (
      this.$route.meta.childCheckType &&
      this.$route.meta.childCheckType !== 'b'
    ) {
      this.$nextTick(() => {
        this.$refs.commonInput && this.$refs.commonInput.focus()
      })
    }
    let loginParams = this.$route.query.token
    if (loginParams) {
      this.openFullScreen('加载中...', 'rgba(255, 255, 255, 1)')
      let params = {
        token: loginParams
      }
      sessionStorage.setItem('isJC', true)
      this.loginFun(params, true, '')
    }else{
      this.isTopShow=true;
    }
  },
  beforeRouteEnter(to,from,next){
    next(vm=>{
      if(!from.name && !vm.$route.query.token && to.path=='/index'){
        vm.$router.back();
        window.history.go(-1);
        sessionStorage.setItem('isJC', false)
      }
    })
  },
  updated () {
    this.$root.setScrollBar(true)
  }
}
</script>
<style scoped>
.main-container {
  background: #ffffff;
}

.main-header {
  z-index: 99;
  margin-top: -10px;
}
</style>
<style>
.el-main {
  padding: 0px 0px !important;
}

.app-contanter .el-header {
  padding: 0 !important;
}

.app-contanter .main-header {
  width: 100% !important;
}

.app-contanter .main-header .shadow-card {
  padding: 10px 5px !important;
  width: calc(100% - 12px) !important;
  margin: 0 !important;
}
</style>
