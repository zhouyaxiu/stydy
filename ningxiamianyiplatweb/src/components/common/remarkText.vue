<template>
  <!-- 查询提示信息
    * remarkText 表格下方是否显示备注内容
    * 每个备注换行需要添加<br>  ① 技术职称为空不统计；<br>② 省、市、县、乡、接种单位采用静态计算。
-->
  <div class="remark-text">
    <div class="remark-text-left">
      <!-- 备注： -->
      <img src="@/assets/img/common/remark.png" class="verifyImg" alt="备注：" title="备注：" />
    </div>
    <div class="remark-text-right">
      <div v-for="item in remarkData" :key="item">{{ item }}</div>
    </div>
  </div>
</template>

<script setup>
//父组件传参--按钮类型
const props = defineProps({
  remarkText: {
    type: String,
    required: false,
    default: ''
  }
})
const remarkData = computed(() => {
  let arr = props.remarkText.indexOf("<br>") > -1 ? props.remarkText.split("<br>") : [props.remarkText];
  return arr;
}) 
</script>

<style scoped>
.remark-text img {
  margin-top: 10px;
  width: 150px;
}

.remark-text-right div {
  font-size: 14px;
  color: #515B68;
}
</style>
