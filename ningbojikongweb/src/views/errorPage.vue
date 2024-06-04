<template>
  <div class="error-page">
    <div class="msg-text">{{ errorMsg }}</div>
    <button class="back-button" @click="goLogin">返回</button>
  </div>
</template>

<script>
export default {
  name: 'errorPage',
  computed: {
    errorMsg () {
      return this.$store.state.errorMsg
    }
  },
  methods: {
    goLogin () {
      this.$router.push({ name: 'login' })
    }
  },
  created(){
    if(sessionStorage.getItem('isJC')=='true'){
      this.$router.back();
      window.history.go(-1);
    }
  }
}
</script>

<style scoped>
.error-page {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  background-image: url(../assets/img/error_back.png);
  background-size: cover;
  background-position: top center;
  background-repeat: no-repeat;
}
.msg-text {
  font-size: 24px;
  margin-top: 8%;
}
.back-button {
  cursor: pointer;
  color: #fff;
  background-color: #3068fd;
  border: 1px solid #3068fd;
  border-radius: 40px;
  padding: 9px 50px;
  margin: 10px;
  height: 40px;
}
</style>
