<template>
    <div :class="'icon ss-icon-'+props.iconType" :style="'margin-top:-2px;width:'+props.size+';height:'+props.size"></div>
</template>

<script setup>
  const props=defineProps({
    iconType:{
      type:String,
      required:true
    },
    size:{
      type:String,
      required:false,
      default:"14px",
    },
  })
</script>

<style scoped>
    .icon {
        position: relative;
        vertical-align: middle;
        display: inline-block;
        background-size: contain;
        outline: none;
        background-repeat: no-repeat;
    }
</style>
