<template>
  <el-sub-menu v-if="props.item.supModules?.length" :class="'menu-' + props.level" :index="props.item.index"
    :key="props.item.index">
    <template #title>
      <child :id="item.id" v-if="props.level == 1 || props.level == 2"></child>
      <span>{{ item.moduleName }}</span>
    </template>
    <TheSidebarItem v-for="(subItem, inx) in props.item.supModules" :key="inx" :item="subItem" :level="2">
    </TheSidebarItem>
  </el-sub-menu>

  <el-menu-item v-else :class="'menu-' + props.level + '-0'" :index="props.item.index"
    @click="clickHandler(props.item)">
    <template #title>
      <child :id="item.id" v-if="props.level == 1"></child>
      <span class="pl_16">{{ item.moduleName }} </span>
    </template>
  </el-menu-item>
</template>

<script setup>
import { useSidebarStore } from '../../store/sidebar'
import { useRoute } from "vue-router";
import { mainStore } from "@/store/index";
const store = mainStore()
const route = useRoute();
// const menuList = store.getMenus
// const onRoutes = computed(() => {
//   return route.path;
// });
const defaultOpens = ref([])
// const sidebar = useSidebarStore();
// 侧边栏折叠
// const collapseChage = () => {
//   console.log(2222222)
//   sidebar.handleCollapse();
// };
const props = defineProps({
  item: {
    type: Object,
    default: {}
  },
  level: {
    type: Number,
    default: 1
  },
})
onMounted(() => {
  if (document.body.clientWidth < 1500) {
    // collapseChage();
  }
});
//缓存选中的导航菜单信息
const clickHandler = (item, indexPath) => {
  store.setSelectedMenu(item)
}
const handleOpen = (index, indexPath) => {
  defaultOpens.value = ['102', '10']
  // 防止一级菜单折叠
  // if (indexPath.length == 2) {
  //   defaultOpens.value = [indexPath[1]];
  // } else {
  //   defaultOpens.value = indexPath
  // }
  // defaultOpens.value = indexPath
}
// const handleClose = (index, indexPath) => {
//   console.log(index, indexPath, 'close indexpath')
// }
const child = defineComponent({
  props: {
    id: String
  },
  setup(props) {
    let arr = [
      { id: 35, type: 'userSetting' },
      { id: 38, type: 'sysManage' },
      { id: 68, type: 'sysSetting' },
      { id: 102, type: 'jzmz' },
      { id: 200, type: 'cy' },
    ]
    let ids = arr.filter(item => item.id == props.id)
    let type = ids?.length ? ids[0].type : 'jzmz';
    return () => h('i', { class: "el-icon mlr_6" }, h(
      'div', { class: "icon ss-icon-" + type },
      null
    ));
  }
});
</script>

<style scoped lang="scss">
.pl_16 {
  padding-left: 16px;
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
.sidebar .icon {
  position: relative;
  vertical-align: middle;
  display: inline-block;
  background-size: contain;
  outline: none;
  background-repeat: no-repeat;
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

    .el-menu-item {
      height: 36px;
      line-height: 36px;
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