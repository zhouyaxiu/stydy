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
  margin-top: 275px;
}
.indexPage .bg {
  background: url(../assets/img/bg-welcome.png);
  background-size: contain;
  background-position: right bottom;
  background-repeat: no-repeat;
  width: 848px;
  height: 387px;
  margin: 0 auto;
  margin-top: 82px;
}
.indexPage .txt {
  height: 60px;
  width: 258px;
  background: url(../assets/img/bg-welcome-txt.png);
  background-size: contain;
  background-repeat: no-repeat;
  margin-left: 400px;
}
</style>
