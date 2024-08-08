<template>
  <div>
  </div>
</template>

<script>
import router from '@/router'
import store from '@/store/store'
import * as types from '@/store/types'
export default {
  name: 'NoPermission',
  methods: {
    setError () {
      let msg = this.$message({
        showClose: true,
        duration: 0,
        center: true,
        offset: 100,
        message: '您没有权限查看该页面！',
        type: 'error'
      })
      this.$nextTick(() => {
        setTimeout(() => {
          msg.close()
          this.$router.go(-1)
        }, 1000)
      })
    }
  },
  mounted () {
    this.setError()
  },
  activated () {
    this.setError()
  },
  beforeRouteEnter (to, from, next) {
    // debugger
    // if (!from || !from.name) {
    //   store.commit(types.SET_ERROR, '您没有权限查看该页面！')
    //   router.push({ name: 'errorPage' })
    //   return
    // }
    next()
  }
}
</script>
