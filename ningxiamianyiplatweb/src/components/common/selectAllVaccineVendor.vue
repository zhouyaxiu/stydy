
<template>
    <ss-common-select :selectType="3" :dataInfo="dataInfo" :selectList="fenterList" @select-change="changeFun" />
</template>
<script setup>
import { xgRptAgeFactory } from '@/api/apis/common'
const fenterNo = ref("");
const fenterList = ref([]);
const dataInfo = ref({
    optsValue: 'code',
    optsLabel: 'shortName'
})
//父组件传参--按钮类型
const props = defineProps({
    // 0：vero 1：智飞 2：丽珠、三叶 3：康希诺 4：威斯克、神州、万泰、石药
    type: {
        type: Number,
        required: false
    }
})
//子组件回调父组件方法
const changeFun = (val, item) => {
    fenterNo.value = val
}
onMounted(() => {
    xgRptAgeFactory({ type: props.type }).then(res => {
        if (res.data.code === 200) {
            fenterList.value = res.data.data.data;
        }
    })
})
// 公开数据
defineExpose({
    fenterNo
})
</script>

<style scoped lang="scss"></style>
