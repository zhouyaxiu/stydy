<template>
  <div class="custom-area" :style="'height:' + height + 'px'">
    <slot></slot>
  </div>
</template>

<script setup>
const height = ref(0)
const setheight = () => {
  let documentHeight = document.body.clientHeight;
  let headerHeight = document.getElementsByClassName('header')[0].clientHeight
  let conditionHeight = document.getElementsByClassName('condition-area')[0].clientHeight
  height.value = documentHeight - headerHeight - conditionHeight - 40
}
onMounted(() => {
  setheight()
})

</script>
<style scoped lang='scss'>
.custom-area {
  padding: 16px;
  background-image: url(@/assets/img/common/bg.png);
  background-repeat: no-repeat;
  background-color: #ffffff;
  background-size: auto;
  background-position: right bottom;
  cursor: pointer;
  overflow: auto;
}
</style>