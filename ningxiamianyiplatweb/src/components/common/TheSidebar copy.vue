<template>
  <div class="sidebar">
    <div class="flex">
      <div class="sider-scrollbar">
        <el-menu class="sidebar-el-menu" v-if="!sidebar.collapse" :default-active="onRoutes" background-color="#fff"
          text-color="#606266" active-text-color="#20a0ff" router :default-openeds="defaultOpens" @open="handleOpen"
          @close="handleClose" style="height:800px" :menu-trigger="click">
          <TheSidebarItem v-for="item in menuList" :key="item.index" :item="item" />
          <!-- <template v-for="item in menuList">
            <template v-if="item.supModules && item.supModules.length">
              <el-sub-menu class="menu-1" :index="item.index" :key="item.index">
                <template #title>
                  <ss-icon class="mlr_6" v-if="item.id === '35'" iconType="userSetting" />
                  <ss-icon class="mlr_6" v-else-if="item.id === '38'" iconType="sysManage" />
                  <ss-icon class="mlr_6" v-else-if="item.id === '68'" iconType="sysSetting" />
                  <ss-icon class="mlr_6" v-else-if="item.id === '102'" iconType="jzmz" />
                  <ss-icon class="mlr_6" v-else-if="item.id === '200'" iconType="cy" />
                  <ss-icon class="mlr_6" v-else iconType="jzmz" />
                  <span>{{ item.moduleName }}</span>
                </template>
<template v-for="(subItem, inx) in item.supModules" :key="inx">
                  <el-sub-menu class="menu-2" v-if="subItem.supModules && subItem.supModules.length"
                    :index="subItem.index">
                    <template #title>
                      <ss-icon class="mlr_6" iconType="file" />
                      {{ subItem.moduleName }}
                    </template>
<el-menu-item class="menu-3" v-for="(threeItem, i) in subItem.supModules" :key="i" :index="threeItem.index"
  @click="clickHandler(threeItem)">
  <template #title>
                        <span>{{ threeItem.moduleName }}</span>
                      </template>
</el-menu-item>
</el-sub-menu>
<el-menu-item class="menu-2-0" v-else :index="subItem.index" :key="subItem.index + '_else'"
  @click="clickHandler(subItem)">
  <template #title>
                      <ss-icon class="mlr_6" iconType="file" />
                      <span>{{ subItem.moduleName }}</span>
                    </template>
</el-menu-item>
</template>
</el-sub-menu>
</template>
<template v-else>
              <el-menu-item class="menu-1-0" :index="item.index" :key="item.index" @click="clickHandler(item)">
                <template #title>
                  <ss-icon class="mlr_6" v-if="item.id === '35'" iconType="userSetting" />
                  <ss-icon class="mlr_6" v-else-if="item.id === '38'" iconType="sysManage" />
                  <ss-icon class="mlr_6" v-else-if="item.id === '68'" iconType="sysSetting" />
                  <ss-icon class="mlr_6" v-else-if="item.id === '102'" iconType="jzmz" />
                  <ss-icon class="mlr_6" v-else-if="item.id === '200'" iconType="cy" />
                  <ss-icon class="mlr_6" v-else iconType="jzmz" />
                  <span class="pl_16">{{ item.moduleName }} </span>
                </template>
</el-menu-item>
</template>
</template> -->
        </el-menu>
      </div>
      <!-- 折叠按钮 -->
      <div class="menu-fold-btn" :class="{ 'menu-collapse': sidebar.collapse }" @click="collapseChage">
        <div class="btn-bg">
          <div v-if="!sidebar.collapse" class="btn-zk"></div>
          <div v-else class="btn-zd"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useSidebarStore } from '../../store/sidebar'
import { useRoute } from "vue-router";
import { mainStore } from "@/store/index";
import TheSidebarItem from "./TheSidebarItem.vue"
const store = mainStore()
const route = useRoute();
const menuList = store.getMenus
// const onRoutes = computed(() => {
//   return route.path;
// });
const onRoutes = ref(route.path)
const defaultOpens = ref([])
const sidebar = useSidebarStore();
// 侧边栏折叠
const collapseChage = () => {
  console.log(2222222)
  sidebar.handleCollapse();
};

onMounted(() => {
  if (document.body.clientWidth < 1500) {
    // collapseChage();
  }
});
//缓存选中的导航菜单信息
const clickHandler = (item, indexPath) => {
  store.setSelectedMenu(item)
  onRoutes.value = item
  defaultOpens.value = ['102', '10']
  console.log(item, indexPath, 1111)
}
const handleOpen = (index, indexPath) => {
  console.log(indexPath, index, 'indexpath')
  defaultOpens.value = ['102', '10']
  // 防止一级菜单折叠
  // if (indexPath.length == 2) {
  //   defaultOpens.value = [indexPath[1]];
  // } else {
  //   defaultOpens.value = indexPath
  // }
  // defaultOpens.value = indexPath
}
const handleClose = (index, indexPath) => {
  defaultOpens.value = ['102', '10']
  console.log(index, indexPath, 'close indexpath')
}
</script>

<style scoped lang="scss">
.pl_16 {
  padding-left: 16px;
}

.sidebar {
  width: 286px;
  height: calc(100% - 64px);
  box-shadow: 3px 3px 10px 1px #E1E6EC;

  .sider-scrollbar {
    height: 100%;
    overflow: auto;
  }

  .flex {
    height: 100%;
    align-items: end;
  }

  .el-menu-item {
    height: 32px;
    font-size: 14px;
    font-weight: normal;
    color: #333333;
  }
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 272px;
}

.sidebar>ul {
  height: 100%;
}

/*折叠按钮*/
.menu-collapse {
  left: 10px;
}

.menu-fold-btn {
  position: relative;
  height: 50px;
  top: 180px;
  z-index: 10;
  cursor: pointer;

  .btn-bg {
    width: 14px;
    height: 50px;
    background: url(@/assets/img/button/menu_btn.png) left center no-repeat;
    background-size: contain;
    background-position: left center;
  }

  .btn-zk {
    position: relative;
    top: 20px;
    left: 3px;
    width: 6px;
    height: 10px;
    background: url(@/assets/img/button/menu_btn_zd.png) left center no-repeat;
    background-size: contain;
    background-position: left center;
  }

  .btn-zd {
    position: relative;
    top: 20px;
    left: 3px;
    width: 6px;
    height: 10px;
    background: url(@/assets/img/button/menu_btn_zk.png) left center no-repeat;
    background-size: contain;
    background-position: left center;
    transform: rotateZ(0deg);
  }
}
</style>
<style lang="scss">
@mixin before {
  content: "";
  position: absolute;
  top: -10px;
  width: 1px;
  height: 100%;
  margin-left: 4px;
  border-left: 1px dashed #dddddd;
  z-index: 1;
}

@mixin after {
  content: "";
  position: absolute;
  top: 16px;
  width: 16px;
  height: 20px;
  margin-left: -16px;
  z-index: 1;
  border-top: 1px dashed #dddddd;
}

.sidebar {
  .menu-1:first-child::before {
    @include before;
    top: 20px;
    height: calc(100% - 35px);
    margin-left: 24px;
  }

  .menu-1:last-child .menu-2:last-child.is-opened::before {
    @include before;
    border-color: #ffffff;
    border-style: solid;
    margin-left: 24px;
    top: 80px;
  }

  .menu-1-0 {
    &::before {
      @include before;
    }

    &::after {
      @include after;
    }
  }

  .menu-2-0 {
    &::before {
      @include before;
      top: 10px;
      border-color: #ffffff;
      border-style: solid;
      z-index: -1;
    }

    &::after {
      @include after;
      width: 28px;
      z-index: 2;
    }
  }

  .menu-2 {

    .el-sub-menu__title::before,
    .el-menu-item::before {
      @include before;
    }

    .el-sub-menu__title::after,
    .el-menu-item::after {
      @include after;
    }
  }

  .menu-2 .menu-3 {

    &::before {
      @include before;
      margin-left: -16px;
    }

    &::after {
      @include after;
      width: 30px;
    }
  }

  .menu-2.is-opened+.menu-2 .el-sub-menu__title::before {
    top: 10px;
    border-color: #ffffff;
    border-style: solid;
    z-index: -1;
  }

  .menu-1.is-opened .menu-2:first-child .el-sub-menu__title::before {
    display: none;
  }

  .menu-2 .menu-3::before {
    top: -15px;
  }

  .menu-3 span {
    display: block;
    padding-left: 6px;
    margin-left: 16px;
    width: 100%;
    height: 32px;
    line-height: 32px;
  }



  .sidebar-el-menu {
    border: none !important;

    .el-icon {
      width: auto;
      margin: 0;
      z-index: 10;

      &.mlr_6 {
        margin: 0 6px;
      }
    }

    .menu-2-0 .el-icon {
      margin-left: 16px;
    }

    .el-menu-item:hover {
      background: none;
    }

    .el-menu-item:hover span {
      opacity: .9;
    }

    .el-menu-item.is-active span {
      color: #3385FF;
      background: rgba(51, 133, 255, .1);
      border-radius: 2px 2px 2px 2px;
    }

    .menu-2-0.el-menu-item.is-active span {
      background: none;
    }

    .el-sub-menu__title {
      height: 32px;
      font-size: 15px;
      font-weight: bold;
      color: #2B3340 !important;

      >.el-sub-menu__icon-arrow {
        position: initial;
        padding-left: 10px;
        order: -1;
        width: 10px;
        height: 10px;
        background: url(@/assets/img/icon/close.png) left center no-repeat;
        background-size: contain;
        background-position: left center;
        transform: none !important;
        z-index: 10;
      }

    }

    .menu-2 .el-sub-menu__title {
      font-weight: normal;
    }
  }

  .el-sub-menu.is-opened>.el-sub-menu__title .el-sub-menu__icon-arrow {
    background: url(@/assets/img/icon/open.png) left center no-repeat;
    background-size: contain;
  }

}
</style>
