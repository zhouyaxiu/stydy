<template>
  <!-- 接种单位迁移 -->
  <div>
    <ss-condition-area :isShowBtn="false"></ss-condition-area>
    <ss-custom-area class="flex">
      <div class="tree-area flex">
        <div class="title">接种单位</div>
        <ss-area-areaTreeExpand @nodeClick="nodeClickSource" class="tree" />
      </div>
      <el-button class="button" @click="moveArea" type="primary">迁移</el-button>
      <div class="ml_30 tree-area flex">
        <div class="title">目标区划</div>
        <ss-area-areaTreeExpand :getToLevelNum="4" @nodeClick="nodeClickTarget" class="tree" />
      </div>
    </ss-custom-area>
  </div>
</template>
<script setup>
import { areaTree } from '@/utils/common/area.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { inoculationMove } from '@/api/apis/setting'
const sourceCode = ref('')
const sourceValue = ref(null)
const isSourceLeaf = ref('')
const targetCode = ref('')
const targetValue = ref(null)
const isTargetLeaf = ref('')
const nodeClickSource = (data) => {
  sourceValue.value = data
  sourceCode.value = data.areaCode
  isSourceLeaf.value = data.isLeaf
}
const nodeClickTarget = (data) => {
  targetValue.value = data
  targetCode.value = data.areaCode
  isTargetLeaf.value = data.isLeaf
}
const resetFields = () => {
  sourceCode.value = ''
  sourceValue.value = null
  isSourceLeaf.value = ''
  targetCode.value = ''
  targetValue.value = null
  isTargetLeaf.value = ''
}
const moveArea = () => {
  if (!isSourceLeaf.value) {
    ElMessage.error('请选择到接种单位！')
    return false
  }
  if (sourceCode.value === '') {
    ElMessage.error('请选择接种单位！')
    return false
  }
  if (!isTargetLeaf.value) {
    ElMessage.error('目标区划请选择到乡镇！')
    return false
  }
  if (targetCode.value === '') {
    ElMessage.error('请选择目标区划(乡镇)！')
    return false
  }
  ElMessageBox.confirm('确定要迁移该接种单位吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  })
    .then(() => {
      const data = {
        sourceCode: sourceCode.value,
        sourceId: sourceValue.value.areaId,
        sourceSupId: sourceValue.value.supId,
        targetId: targetValue.value.areaId,
        targetCode: targetCode.value
      }
      inoculationMove(data)
        .then(res => {
          if (res.data.code === 200) {
            areaTree()
            ElMessage.success(res.data.data.data)
          } else {
            ElMessage.error(res.data.popMsg)
          }
          resetFields()
        })
        .catch(() => {
        })
    })
    .catch(() => {
      ElMessage.info('已取消迁移！')
      resetFields()
    })
}
</script>
<style scoped lang="scss">
.tree-area {
  width: 592px;
  height: calc(100% - 20px);
  padding: 16px;
  margin-left: 50px;
  flex-direction: column;
  align-items: center;
  overflow: hidden;
  background: #ECF6FF;
  box-shadow: 0px 4px 8px 1px #E1E6EC;
  border-radius: 14px;

  .tree {
    width: 100%;
    height: 100%;
    padding: 40px;
    margin-top: 16px;
    border-radius: 10px;
    background: #ffffff;
    overflow: auto;
  }

  .title {
    width: 300px;
    height: 42px;
    font-size: 16px;
    font-weight: bold;
    line-height: 42px;
    color: #ffffff;
    background: #3877E3;
    box-shadow: 0px 4px 8px 1px #E1E6EC;
    border-radius: 21px 21px 21px 21px;
    border: 1px solid #FFFFFF;
    text-align: center;
  }
}

.button {
  width: 96px;
  height: 40px;
  background: #3385FF;
  border-radius: 5px;
  margin: 300px 30px auto 80px;
}
</style>
