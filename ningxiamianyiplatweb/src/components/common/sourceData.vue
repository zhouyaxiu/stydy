<template>
  <el-select v-model="sourceData" value-key="code" @change="$emit('update:modelValue', sourceData)" clearable
    style="position: relative;">
    <el-option v-for="item in sourceList" :key="item.code" :label="item.name" :value="item"></el-option>
  </el-select>
</template>

<script setup>
// import { defineProps, getCurrentInstance } from "vue"
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const emit = defineEmits("update:modelValue")
// 数据来源
const sourceList = globalProxy.$commonOptions.sourceList;
const props = defineProps({
  sourceData: {
    type: Object,
    required: true,
    default: () => {
      return {
        code: "", name: "全部"
      }
    }
  },
})
const sourceData = ref(props.sourceData)
</script>