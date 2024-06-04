<template>
  <div class="scroll-item" :style="'display:flex;height:' + tabHeight + 'px'">
    <!-- :default-active="defaultActive" -->
    <el-menu :default-openeds="defaultOpenMenu" :default-active="$route.path" v-if="!foldFlag" class="menu-plan"
      :style="'overflow:auto;width:320px;max-height:' + leftHeight + 'px;'">
      <!-- :index="indexTop+''" -->
      <template v-for="(itemTop, indexTop) in menus">
        <el-submenu :key="'menu' + indexTop" :index="indexTop + ''"
          v-if="itemTop.supModules && itemTop.supModules.length > 0">
          <template slot="title">
            <div @click="itemTop.moduleUrl ? clickHandler(indexTop + '-', itemTop) : ''
              ">
              <i class="el-icon-level1"></i>
              <span :style="'color:#4B5C8C;font-weight:bold'">
                {{ itemTop.moduleName }}
              </span>
            </div>
          </template>
          <template v-for="(itemSub, indexSub) in itemTop.supModules">
            <!-- 有三级菜单 -->
            <!-- <el-submenu :index="indexTop + '-' + indexSub" :key="'menu' + indexTop + '-' + indexSub"
              v-if="itemSub.supModules && itemSub.supModules.length > 0">
              <template slot="title">
                <span :style="'color:#717E89'">{{itemSub.moduleName}}</span>
              </template> -->
            <!-- :index="indexTop + '-' + indexSub + '-' + indexSub2" -->
            <!-- <el-menu-item v-for="(itemSub2, indexSub2) in itemSub.supModules" :index="itemSub2.moduleUrl"
                :key="'menu' + indexTop + '-' + indexSub + '-' + indexSub2"
                @click="clickHandler(indexTop + '-' + indexSub + '-' + indexSub2, temSub2)"
                :class="indexTop + '-' + indexSub + '-' + indexSub2 == defaultActive ? 'activeItem' : ''">
                <span>{{ itemSub2.moduleName }}</span>
              </el-menu-item>
            </el-submenu> -->
            <!-- 无三级菜单 -->
            <!-- :index="indexTop + '-' + indexSub" -->
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
            <el-menu-item :key="'menu' + indexTop + 'top'" :index="itemTop.moduleUrl">
              <template slot="title">
                <div @click="itemTop.moduleUrl ? clickHandler(indexTop, itemTop) : ''
                  ">
                  <i class="el-icon-level1"></i>
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
    <div :style="'width:20px;background:#fff;height:' + leftHeight" v-show="foldFlag">
      &nbsp;
    </div>
    <div class="menu-fold-btn" @click="foldHandler" :style="'top:' + (leftHeight / 2 - 62) + 'px'">
      <div class="btn-bg">
        <div v-if="!foldFlag" class="btn-zd"></div>
        <div v-else class="btn-zk"></div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'menuLeft',
  components: {},
  data() {
    return {
      currentUrl: '',
      heightNum: 'calc(' + 100 / window.scale + 'vh)',
      scale: window.scale,
      defaultOpenMenu: ['0'],
      defaultActive: '',
      tabHeight: 0,
      msgPubPermission: false,//等于4为门诊：true，其他是疾控：false
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
    menus() {
      let modules = this.$store.state.modules
      return modules
    }
  },
  methods: {
    // 递归
    recursiveRes(list, url) {
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
    foldHandler() {
      this.$emit('foldHandler', !this.foldFlag)
    },
    clickHandler(titleIndex, item) {
      this.$store.commit(this.$types.SET_TITLE, {
        moduleId: item.id,
        title: item.moduleName,
        breadcrumb: item.breadcrumb
      })
      this.$router.push(item.moduleUrl)
      sessionStorage.areacodeSelect=JSON.parse(sessionStorage.getItem('response')).userinfo.areaCode
      sessionStorage.areaLevelSelect=JSON.parse(sessionStorage.getItem('response')).arealevel
    },
    setHeight() {
      let top = this.$util.getAbsoluteTop(this.$el)
      if (!top) {
        this.tabHeight = null
        return
      }
      this.tabHeight = window.winHeight - top - 14 / window.scale
    }
  },
  mounted() {
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
    this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) == 4
    console.log(this.msgPubPermission)
    // 等于4为门诊：true，其他是疾控：false
  },
  updated() {
    this.setHeight()
    this.setScrollBar()
  }
}
</script>

<style scoped>
.menu-plan {
  box-shadow: 0 2px 8px 2px rgb(0 0 0 / 10%);
  background: #fff;
}

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

.menu-fold-btn .btn-bg {
  background: url(../../assets/img/button/menu-btn.png) left center no-repeat;
  background-size: contain;
  background-position: left center;
  width: 22px;
  height: 62px;
  margin-left: -1px;
}

.menu-fold-btn .btn-zk {
  background: url(../../assets/img/button/menu-btn-zk.png) left center no-repeat;
  background-size: contain;
  background-position: left center;
  width: 10px;
  height: 10px;
  position: relative;
  top: 25px;
}

.menu-fold-btn .btn-zd {
  background: url(../../assets/img/button/menu-btn-zd.png) left center no-repeat;
  background-size: contain;
  background-position: left center;
  width: 10px;
  height: 10px;
  position: relative;
  top: 25px;
}

.el-icon-level1 {
  width: 14px;
  height: 14px;
  text-align: center;
  background: url(../../assets/img/icon/icon_menu.png) left center no-repeat;
  background-size: contain;
  background-position: left center;
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
