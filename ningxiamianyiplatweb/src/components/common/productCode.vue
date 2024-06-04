<template>
  <!-- 下拉选择制品编码  -->
  <el-select v-model="vaccNo" clearable style="position: relative;" @change="getName">
    <el-option v-for="item in vaccNoList" :key="item.code" :label="item.name" :value="item.code"></el-option>
  </el-select>
</template>
<script setup>
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const vaccNo = ref("-1");
const vaccName = ref("全部");
//父组件传参--疫苗编码
// const props = defineProps({
//   vaccNo: {
//     type: String,
//     default: ""
//   }
// })
// 制品编码列表
const vaccNoList = globalProxy.$commonOptions.vaccNoList;

const getName = (value) => {
  if (!value) { return vaccNo.value = "", vaccName.value = ""; }
  let name = vaccNoList.filter(item => item.code == value)[0].name;
  vaccName.value = name;
}
// 公开数据
defineExpose({
  vaccNo,
  vaccName
})
</script>

<style scoped></style>