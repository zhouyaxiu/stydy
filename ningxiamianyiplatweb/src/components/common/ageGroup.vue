<template>
  <span class="label" style="display:flex;align-items:center;">
    <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate"
      @change="handleCheckAllChange"></el-checkbox>&nbsp;&nbsp;年龄组：
  </span>
  <el-checkbox-group v-model="ageData" @change="handleCheckedCitiesChange" :disabled="!checkAll">
    <el-checkbox v-for="item in agesList" :label="item" :key="item.id" :value="item">{{ item.name
      }}</el-checkbox>
  </el-checkbox-group>
</template>

<script setup>
// import { defineProps, getCurrentInstance } from "vue"
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const checkAll = ref(false);//是否选择年龄组
const isIndeterminate = ref(false);
// 年龄组
const agesList = globalProxy.$commonOptions.agesList;

const emit = defineEmits("update:modelValue");

// 全选事件
const handleCheckAllChange = (val) => {
  ageData.value = val ? agesList : []
  isIndeterminate.value = false;
  emit('update:modelValue', ageData.value)
}

const handleCheckedCitiesChange = (value) => {
  const checkedCount = value.length
  ageData.value = value
  isIndeterminate.value = checkedCount > 0 && checkedCount < agesList.length ? true : false
  if (checkedCount == 0) checkAll.value = false;
  emit('update:modelValue', ageData.value)
}

const props = defineProps({
  ageData: {
    type: Array,
    default: () => {
      return {
      }
    }
  },
})
const ageData = ref(props.ageData)
</script>