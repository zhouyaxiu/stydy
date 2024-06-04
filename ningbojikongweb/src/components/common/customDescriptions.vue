<template>
  <el-descriptions :column="1" border>
    <el-descriptions-item v-for="item in items" :key="item.name" :label="item.name">{{ item.value
    }}</el-descriptions-item>
  </el-descriptions>
</template>
  
<script>
export default {
  name: 'selectArea',
  data() {
    return {
    }
  },
  props: {
    items: {
      type: Array,
      default: () => []
    }
  },
  methods: {

  }
}
</script>

<style >
.el-descriptions-item__label.is-bordered-label {
  background: #fbfcff;
  /* width: 50%; */
  color: #333;
}

.el-descriptions .is-bordered .el-descriptions-item__cell {
  border-color: #E8EDFF
}
</style>
  