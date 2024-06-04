<template>
  <el-select v-model="form.ages" placeholder="请选择" multiple collapse-tags collapse-tags-tooltip>
    <el-checkbox v-model="checked" @change="handleCheckAllChange" />全选
    <el-option v-for="item in ageList" :key="item" :label="item" :value="item" :disabled="checkAge"></el-option>
  </el-select>
  <el-select v-model="hobbys.hobby1" placeholder="请选择爱好">
    <el-option v-for="item in hobbyList" :key="item" :label="item" :value="item"
      :disabled="checkHobby(item)"></el-option>
  </el-select>

</template>
<script setup>
import { ref } from "vue";
const ageList = ref(["全部", 19, 20, 21, 22])
const form = ref({ ages: [] })
const checked = ref(false)
const hobbyList = ref(["听歌", "动漫", "前端"])
const hobbys = ref({
  hobby1: "",
  hobby2: "",
  hobby3: "",
})
const checkAge = () => {
  if (form.value.ages.length === 0) {
    return false;
  }
  if (form.value.ages.includes("全部") && item !== "全部") {
    // 选择了`全部`，此时禁选`非全部的选项`
    return true;
  } else if (!form.value.ages.includes("全部") && item === "全部") {
    // 选择了`非全部的选项`，此时`禁选全部`
    return true;
  }

  return false;
};

const checkHobby = (item) => {
  for (const hobbyKey in hobbys.value) {
    // 如果已经有选中过该选项的下拉框，则禁止再次选择
    if (item === hobbys.value[hobbyKey]) {
      return true;
    }
  }

  return false;
};
const handleCheckAllChange = () => {
  if (checked.value) {
    form.value.ages = ageList.value;
  } else {
    form.value.ages = [];
  }
};
watch(
  () => form.value.ages,
  (newValue) => {
    checked.value = newValue.length === ageList.value.length;
  }
);
</script>