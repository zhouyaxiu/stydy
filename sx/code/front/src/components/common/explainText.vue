<template>
  <!-- 颜色注释
    * v-typeList {Array}  颜色列表,默认白、绿、黄
          [{
            text {String}  颜色+文字描述
            style {Object}  background颜色
          }]
     -->
  <div class="explain-text">
    <template v-for="(item, idx) in typeList">
      <div style="display: flex" v-if="idx == 0" :key="idx">
        <div class="circle-area" :style="item.style"></div>
        {{ item.text }}
      </div>
      <div class="spec" style="display: flex" v-else :key="idx">
        <div class="circle-area" :style="item.style"></div>
        {{ item.text }}
      </div>
    </template>
  </div>
</template>

<script>
export default {
  name: 'explainText',
  data () {
    return {}
  },
  props: {
    typeList: {
      type: Array,
      default: () => {
        return [
          {
            text: '白色：查询数据',
            style: { background: '#fff' }
          },
          {
            text: '蓝色：同步数据',
            style: { background: '#DFF5FF' }
          }
        ]
      }
    }
  }
}
</script>

<style scoped>
.explain-text {
  width: 100%;
  display: flex;
  font-size: 12px;
  background: #fff;
  height: 40px;
  margin-top: 10px;
}

.explain-text .spec {
  margin-left: 15px;
}

.explain-text .circle-area {
  border-radius: 12px;
  border: 1px solid #dadde4;
  width: 12px;
  height: 12px;
  margin-top: 4px;
  margin-right: 10px;
}
</style>
