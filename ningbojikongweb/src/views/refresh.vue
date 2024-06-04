<template>
  <div></div>
</template>

<script>
export default {
  data () {
    return {}
  },
  beforeRouteEnter (ro, from, next) {
    next((vm) => {
      vm.$router.replace(from.path)
      // 跳转该路由界面后，再替换为from.path来源路径
    })
  }
}
</script>
<style scoped>
</style>
<style>
</style>
