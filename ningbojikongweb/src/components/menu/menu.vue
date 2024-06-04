<template>
    <el-menu :default-active="defaultActive" defaultOpenMenu="[menu0]" class="menu-plan" :style="'overflow:auto;width:350px;max-height:' + leftHeight + 'px;'">
      <template v-for="(itemTop, indexTop) in menus">
        <el-submenu :key="'menu' + indexTop" :index="indexTop + ''" v-if="itemTop.supModules.length" >
          <template slot="title">
            <div @click="itemTop.moduleUrl ? clickHandler(indexTop + '-', itemTop) : '' ">
              <span>{{ itemTop.moduleName }}</span>
              <ss-icon iconType="down" size="10px" />
            </div>
          </template>
          <template v-for="(itemSub, indexSub) in itemTop.supModules">
            <!-- 有三级菜单 -->
            <el-submenu :index="indexTop + '-' + indexSub" :key="'menu' + indexTop + '-' + indexSub"
              v-if="itemSub.supModules && itemSub.supModules.length > 0">
              <template slot="title"><span :style="'color:#717E89'">{{itemSub.moduleName}}</span></template>
              <el-menu-item v-for="(itemSub2, indexSub2) in itemSub.supModules" :index="itemSub2.moduleUrl"
                :key="'menu' + indexTop + '-' + indexSub + '-' + indexSub2" @click="
                  clickHandler(indexTop + '-' + indexSub + '-' + indexSub2,itemSub2)"
                  :class="indexTop + '-' + indexSub + '-' + indexSub2 == defaultActive? 'activeItem' : '' ">
                <span>{{ itemSub2.moduleName }}</span>
              </el-menu-item>
            </el-submenu>
            <!-- 无三级菜单 -->
            <template>
              <el-menu-item :index="itemSub.moduleUrl" :key="'menu' + indexTop + '-' + indexSub"
                v-if="!itemSub.supModules || itemSub.supModules.length == 0"
                @click="clickHandler(indexTop + '-' + indexSub, itemSub)">
                <template slot="title">{{ itemSub.moduleName }}</template>
              </el-menu-item>
            </template>
          </template>
        </el-submenu>
        <template v-else>
          <template>
            <el-menu-item :key="'menu' + indexTop" @click="clickHandler(indexTop, itemTop)"
              :class="indexTop == defaultActive ? 'activeItem' : ''" :index="itemTop.moduleUrl"
              v-if="itemTop.moduleUrl != '/systemManage/infoReception'">
              <template slot="title">
                <div @click="
                  itemTop.moduleUrl ? clickHandler(indexTop, itemTop) : ''
                ">
                  <span :style="'color:#4B5C8C;font-weight:bold'">
                    {{ itemTop.moduleName }}
                  </span>
                </div>
              </template>
            </el-menu-item>
          </template>
        </template>
      </template>
    </el-menu>
  </template>
  <script>
  export default {
    name: 'menuLeft',
    components: {},
    data () {
      return {
        currentUrl: '',
        heightNum: 'calc(' + 100 / window.scale + 'vh)',
        scale: window.scale,
        defaultOpenMenu: ['0'],
        defaultActive: '',
        tabHeight: 0,
        msgPubPermission: false
      }
    },
    props: {
      foldFlag: {
        type: Boolean,
        required: true
      },
      leftHeight: {
        type: Number,
        required: true
      }
    },
    // watch: {
    //   '$route.path' (val) {
    //     this.recursiveRes(this.menus, val) // 递归查找当前的路由得到 this.currentUrl
    //     this.$store.commit(this.$types.SET_TITLE, {
    //       moduleId: this.currentUrl.id,
    //       title: this.currentUrl.moduleName,
    //       breadcrumb: this.currentUrl.breadcrumb
    //     })
    //   }
    // },
    computed: {
      menus () {
        return this.$store.state.modules
      }
    },
    methods: {
      // 递归
      recursiveRes (list, url) {
        for (let i = 0; i < list.length; i++) {
          if (list[i].moduleUrl === url) {
            this.currentUrl = list[i]
            return
          } else {
            if (list[i].supModules) {
              let supList = list[i].supModules
              for (let j = 0; j < supList.length; j++) {
                let item = supList[j]
                if (list[i].breadcrumb) {
                  item.breadcrumb =
                    list[i].breadcrumb +
                    ' / ' +
                    (item.supModules.length ? item.moduleName : '')
                } else {
                  item.breadcrumb =
                    list[i].moduleName +
                    ' / ' +
                    (item.supModules.length ? item.moduleName : '')
                }
              }
              this.recursiveRes(list[i].supModules, url)
            }
          }
        }
      },
      foldHandler () {
        this.$emit('foldHandler', !this.foldFlag)
      },
      clickHandler (titleIndex, item) {
        this.$store.commit(this.$types.SET_TITLE, {
          moduleId: item.id,
          title: item.moduleName,
          breadcrumb: item.breadcrumb
        })
        this.$router.push(item.moduleUrl)
      },
      setHeight () {
        let top = this.$util.getAbsoluteTop(this.$el)
        if (!top) {
          this.tabHeight = null
          return
        }
        this.tabHeight = window.winHeight - top - 14 / window.scale
      }
    },
    mounted () {
      let that = this
      that.setHeight()
      if (
        that.$store.state.title &&
        that.$store.state.titleIndex &&
        that.$store.state.titleIndex !== ''
      ) {
        let arr = that.$store.state.titleIndex.split('-')
        if (arr.length === 2) {
          that.defaultOpenMenu = [arr[0], arr[0] + '-' + arr[1]]
          that.defaultActive = that.defaultOpenMenu[1]
        }
        if (arr.length === 3) {
          that.defaultOpenMenu = [
            arr[0],
            arr[0] + '-' + arr[1],
            arr[0] + '-' + arr[1] + '-' + arr[2]
          ]
          that.defaultActive = that.defaultOpenMenu[2]
        }
      }
      that.setScrollBar()
      this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) !== 4
    },
    updated () {
      this.setHeight()
      this.setScrollBar()
    }
  }
  </script>
  
  <style scoped>
  .el-menu-item {
    color: #717e89;
    /* width: 580px !important; */
  }
  
  .activeItem {
    /*background: #ECF5FF;*/
  }
  
  /*折叠按钮*/
  
  .menu-fold-btn {
    cursor: pointer;
    position: relative;
    height: 62px;
  }
  .el-menu-item span {
    color: #717e89;
  }
  
  .el-menu-item.is-active,
  .el-menu-item.is-active span {
    color: #1e6ce4;
  }
  
  .el-menu-item:focus,
  .el-menu-item:hover,
  .el-menu-item.is-active {
    background-color: #f3f7ff;
  }
  </style>
  <style lang="less">
  .el-menu{
    background-color: rgba(0, 0, 0, 0);
  }
  .el-menu--popup{
    background-color: #EFF2FF;
    margin-top: 0;
    min-width: 136px;
    border-radius: 10px;
    padding: 0;
    text-align: center;
    color: #424655;
    font-weight: bold;
    margin-left: -10px;
  }
  .el-menu--horizontal>.el-submenu .el-submenu__title{
    color: #E9EDFF;
    font-size: 18px;
    border-bottom: 0;
    height: 32px;
    line-height: 32px;
    letter-spacing: 2px;
    border-radius: 18px;
    padding: 0 10px 0 14px;
    display: flex;
    align-items: center;
    span{
      display: inline-block;
      margin-top: -6px;
    }
  }
  .el-menu.el-menu--horizontal{
    border-bottom: 0;
  }
  .el-menu--horizontal>.el-submenu .el-submenu__icon-arrow{
    display: none;
  }
  .el-menu--horizontal>.el-submenu .el-submenu__title:hover, .el-menu--horizontal>.el-submenu:hover{
    background-color: rgba(0, 0, 0, 0);
  }
  .el-menu--horizontal>.el-submenu.is-active .el-submenu__title
  {
    background-color: #2E5BFF;
    color: #fff;
    font-weight: 500;
    border-radius: 18px;
    border-bottom: 0;
    height: 32px;
    line-height: 32px;
    span{
      display: inline-block;
      margin-top: -6px;
    }
  }
  .el-menu--horizontal .el-menu--popup .el-menu-item, .el-menu--horizontal .el-menu-item:not(.is-disabled):focus, .el-menu--horizontal .el-menu-item:not(.is-disabled):hover{
    color: #424655;
  }
  .el-menu--horizontal>.el-submenu:focus .el-submenu__title, .el-menu--horizontal>.el-submenu:hover .el-submenu__title{
    color: #fff;
    span{
      display: inline-block;
      margin-top: -6px;
    }
  }
  .el-menu--horizontal>.el-submenu{
    height: 32px;
    line-height: 32px;
    border-radius: 18px;
    margin-right: 30px;
  }
  .el-menu--horizontal .el-menu .el-menu-item, .el-menu--horizontal .el-menu .el-submenu__title{
    background-color: rgba(0, 0, 0, 0);
    line-height: 50px;
    height: 50px;
    padding: 0;
    margin: 0 7px;
  }
  .el-menu--horizontal .el-menu .el-menu-item:not(:last-child){
    border-bottom: 1px solid #C3CAE7;
  }
  </style>
  