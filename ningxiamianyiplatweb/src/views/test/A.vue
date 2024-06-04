<template>
  A
</template>

<script setup>
import { defineExpose } from "vue"
const Afunction = () => {
  console.log("我是a组件方法")
}
defineExpose({
  Afunction
})
</script>