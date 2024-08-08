<template>
  <router-view  />
</template>

<script setup>
  import { onMounted } from 'vue';
  onMounted(() => {
    sessionStorage.setItem("lastWidth",window.outerWidth);
      window.onresize = () => {
        setTimeout(function(){
          if(!sessionStorage.lastWidth||sessionStorage.lastWidth!=window.outerWidth
            ||window.screen.availWidth<parseInt(sessionStorage.lastWidth)){
            sessionStorage.setItem("lastWidth",window.outerWidth);
            window.location.reload()
          }
        },200)
      }
  })
</script>