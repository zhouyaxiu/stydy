<template>
  <el-container class="main-container">
    <el-main
      class="scroll-item"
      :style="'width:100%;height:'+heightNum"
    >
      <ss-menu-top />
      <div class="content">
        <div>
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
import {} from '@/utils/axios/api'
export default {
  name: 'index',
  mixins: [],
  components: {
    'ss-menu-top': menuTop
  },
  data () {
    return {
      isShow: true,
      heightNum: 'calc(' + 100 / window.scale + 'vh)',
      timestamp: new Date().getTime(),
      scale: window.scale,
      isIE: window.isIE(),
      foldFlag: false
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
    }
  },
  mounted () {
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
  },
  updated () {
    this.$root.setScrollBar(true)
  }
}
</script>
<style scoped>
.content{
  margin: -180px 80px 0 80px;
}
.main-container {
  background: #F2F4FF;
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
