<template>
  <div id="app">
    <router-view @destoryScrollbar="destoryScrollbar"/>
  </div>
</template>

<script>
import PScrollbar from 'perfect-scrollbar'
import 'perfect-scrollbar/css/perfect-scrollbar.css'
export default {
  name: 'App',
  data () {
    return {
      scrollbar: null
    }
  },
  mounted () {
    sessionStorage.setItem('lastWidth', window.outerWidth)
    window.onresize = () => {
      /* if(isIE()){
            window.location.reload()
          }else{ */
      setTimeout(function () {
        if (!sessionStorage.lastWidth || parseInt(sessionStorage.lastWidth) !== window.outerWidth ||
                window.screen.availWidth < parseInt(sessionStorage.lastWidth)) {
          sessionStorage.setItem('lastWidth', window.outerWidth)
          window.location.reload()
        }
      }, 200)
      /* } */
    }
  },
  updated () {
    if (this.scrollbar) {
      this.scrollbar.update()
    }
  },
  methods: {
    destoryScrollbar () {
      if (this.scrollbar) {
        this.scrollbar.destroy()
        this.scrollbar = null
      }
    },
    createScrollbar () {
      if (!this.scrollbar) {
        this.scrollbar = new PScrollbar(this.$el)
      }
    }
  }
}
</script>
<style>
.my-el-custom-spinner {
    background: url("./assets/img/loading.gif");
    background-size: 200px 150px;
    width: 200px;
    height: 150px;
    display: inline-block;
}
.el-loading-spinner .el-loading-text{
	color: #FFFFFF;
}
.el-loading-mask.is-fullscreen .el-loading-spinner{
	margin-top: -100px;
}
</style>
