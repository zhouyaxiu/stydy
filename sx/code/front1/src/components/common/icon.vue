<template>
  <!-- 自定义图片图标
    * v-iconType {Boolean}  类型，包括show、top、bottom、hide、yes、tshi、down、redtips
    * v-size {Number}  图标大小
 -->
  <div
    :class="'icon ss-icon-' + iconType"
    :style="'margin-top:-2px;width:' + size + ';height:' + size"
  ></div>
</template>

<script>
export default {
  name: 'icon',
  data () {
    return {}
  },
  props: {
    iconType: {
      type: String,
      required: true
    },
    size: {
      type: String,
      required: false,
      default: '14px'
    }
  }
}
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
