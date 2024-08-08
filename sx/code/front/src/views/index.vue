<template>
  <!-- 主页面 -->
  <div class="indexPage">
    <div class="txt"></div>
    <div class="bg"></div>
  </div>
</template>
<script>
import {} from '@/utils/axios/api'
export default {
  name: 'index',
  components: {},
  mixins: [],
  data () {
    return {}
  },
  methods: {},
  mounted () {},
  created () {}
}
</script>
<style scoped>
.indexPage {
  width: 100%;
  height: 100%;
}
.indexPage .bg {
  width: 100%;
  height: 100%;
  background: url(../assets/img/bg-welcome.png);
  background-size: contain;
  background-position: right bottom;
  background-repeat: no-repeat;
  max-width: 1088px;
  max-height: 525px;
  margin: 0 auto;
  margin-top: 10%;
}
.indexPage .txt {
  position: relative;
  top: 10%;
  left: 10%;
  height: 82px;
  width: 354px;
  background: url(../assets/img/bg-welcome-txt.png);
  background-size: contain;
  background-repeat: no-repeat;
}
</style>
