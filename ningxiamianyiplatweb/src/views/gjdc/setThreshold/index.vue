<!-- 国家督察-设置阈值 -->
<template>
  <div class="threshold">
    <ss-condition-area></ss-condition-area>
    <div class="table-container">
      <div class="table-container-main">
        <div class="table-container-main-item">
          <span class="mr10 label title">录入及时阈值</span>
          <div class="flex mt20">
            <div class="flex flex1">
              <span class="mr10 label">建档</span>
              <el-input v-model="form.lrjdjsDay" clearable class="mr10" style="width:100px;" />
              <span class="label">天内录入为及时。</span>
            </div>
            <div class="flex flex1">
              <span class="mr10 label">接种</span>
              <el-input v-model="form.lrjzjsDay" clearable class="mr10" style="width:100px;" />
              <span class="label">天内录入为及时。</span>
            </div>
          </div>
        </div>
        <div class="table-container-main-item mt20">
          <span class="mr10 label title">上传及时阈值</span>
          <div class="flex mt20">
            <div class="flex flex1">
              <span class="mr10 label">建档</span>
              <el-input v-model="form.uploadjdjsDay" clearable class="mr10" style="width:100px;" />
              <span class="label">天内录入为及时。</span>
            </div>
            <div class="flex flex1">
              <span class="mr10 label">接种</span>
              <el-input v-model="form.uploadjzjsDay" clearable class="mr10" style="width:100px;" />
              <span class="label">天内录入为及时。</span>
            </div>
          </div>
        </div>
        <div style="margin-top: 40px;">
          <ss-custom-button :type="'update'" @click="saveHandler"></ss-custom-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { getThreshold, setThreshold } from "@/api/apis/gjdc";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { positive_interger } from "@/utils/common/regex";
const form = ref({})
// 查询
const GetThreshold = () => {
  getThreshold().then(res => {
    if (res.data.code === 200) {
      let resData = res.data.data.data;
      let { lrjdjsDay, lrjzjsDay, uploadjdjsDay, uploadjzjsDay } = resData;
      form.value = { lrjdjsDay, lrjzjsDay, uploadjdjsDay, uploadjzjsDay };
    }
  })
}
const saveHandler = () => {
  if (!form.value.lrjdjsDay || !form.value.lrjzjsDay || !form.value.uploadjdjsDay || !form.value.uploadjzjsDay) {
    return globalProxy.$util.showMsg('数字必填且不能为空！', 'error', 5000, true);
  }
  if (!positive_interger.test(form.value.lrjdjsDay) || !positive_interger.test(form.value.lrjzjsDay) || !positive_interger.test(form.value.uploadjdjsDay) || !positive_interger.test(form.value.uploadjzjsDay)) {
    return globalProxy.$util.showMsg('必须是大于等于0的整数！', 'error', 5000, true);
  }
  let { lrjdjsDay, lrjzjsDay, uploadjdjsDay, uploadjzjsDay } = form.value;
  const params = { lrjdjsDay, lrjzjsDay, uploadjdjsDay, uploadjzjsDay }
  setThreshold(params).then(res => {
    if (res.data.code === 200) {
      globalProxy.$util.showMsg(res.data.data.data, 'success');
      GetThreshold()
    } else {
      globalProxy.$util.showMsg(res.data.data.data || '设置失败！', 'error')
    }
  })
}
onMounted(() => {
  GetThreshold()
})
</script>
<style scoped lang='scss'>
.threshold {
  .table-container-main {
    margin: 30px auto;
    background: #fff;
    border-radius: 14px;
    padding: 60px 140px;
    border: 16px solid #ECF6FF;
    max-width: 1000px;

    .table-container-main-item {
      .title {
        border-left: 4px solid #3385FF;
        padding-left: 6px;
      }

      .flex1 {
        flex: 1;
      }
    }
  }
}
</style>
<style lang='scss'>
.threshold {
  .search-area {
    display: none !important;
  }
}
</style>