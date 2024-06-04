<template>
  <!-- 下拉选择疫苗生产企业  -->
  <el-select v-model="fenterNo" clearable filterable style="position: relative;" @change="getName"
    :collapse-tags="collapseTags" :multiple="multiple" :show-checkbox="multiple">
    <el-option v-for="item in fenterList" :key="item.gjCode" :label="item.shortName" :value="item.gjCode"></el-option>
  </el-select>
</template>
<script setup>
import { xgVaccineFactory } from '@/api/apis/common'
const fenterNo = ref("");
const fenterName = ref("");
const fenterList = ref([]);
//父组件传参--按钮类型
const props = defineProps({
  multiple: {
    type: Boolean,
    default: false
  },
  collapseTags: {
    type: Boolean,
    default: false
  },
})
onMounted(() => {
  xgVaccineFactory().then(res => {
    if (res.data.code === 200) {
      fenterList.value = res.data.data.data;
    }
  })
})
const getName = (value) => {
  if (!value) { return fenterNo.value = "", fenterName.value = ""; }
  if (!props.multiple) {
    let name = fenterList.value.filter(item => item.gjCode == value)[0].shortName;
    fenterName.value = name;
  }
}
// 公开数据
defineExpose({
  fenterNo,
  fenterName
})
</script>

<style scoped></style>