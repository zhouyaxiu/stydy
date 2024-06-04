<template>
  <!-- 接种单位合并 -->
  <div>
    <ss-condition-area :isShowBtn="false"></ss-condition-area>
    <ss-custom-area class="flex">
      <div class="tree-area flex">
        <div class="title">源接种单位</div>
        <ss-area-areaTreeExpand @nodeClick="nodeClickSource" class="tree" />
      </div>
      <el-button class="button" @click="moveArea" type="primary">合并</el-button>
      <div class="tree-area flex">
        <div class="title">目标接种单位</div>
        <ss-area-areaTreeExpand :getToLevelNum="5" @nodeClick="nodeClickTarget" class="tree" />
      </div>
    </ss-custom-area>
  </div>
</template>
<script setup>
import { inoculationMerge } from '@/api/apis/setting'
import { areaTree } from '@/utils/common/area.js'
import { ElMessage, ElMessageBox } from 'element-plus'
const sourceCode = ref('')
const isSourceLeaf = ref('')
const targetCode = ref('')
const isTargetLeaf = ref('')
const nodeClickSource = (data) => {
  sourceCode.value = data.areaCode
  isSourceLeaf.value = data.isLeaf
}
const nodeClickTarget = (data) => {
  targetCode.value = data.areaCode
  isTargetLeaf.value = data.isLeaf
}
const resetFields = () => {
  sourceCode.value = ''
  isSourceLeaf.value = ''
  targetCode.value = ''
  isTargetLeaf.value = ''
}
const moveArea = () => {
  if (!isSourceLeaf.value) {
    ElMessage.error('源接种单位请选择到接种单位！')
    return false
  }
  if (sourceCode.value === '') {
    ElMessage.error('请选择源接种单位！')
    return false
  }
  if (!isTargetLeaf.value) {
    ElMessage.error('目标接种单位请选择到接种单位')
    return false
  }
  if (targetCode.value === '') {
    ElMessage.error('请选择目标接种单位！')
    return false
  }
  if (sourceCode.value === targetCode.value) {
    ElMessage.error('目标接种单位不能为源接种单位！')
    return false
  }
  ElMessageBox.confirm('确定要合并接种单位吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  })
    .then(() => {
      const data = {
        sourceCode: sourceCode.value,
        targetCode: targetCode.value,
      }
      inoculationMerge(data)
        .then(res => {
          if (res.data.code === 200) {
            areaTree()
            ElMessage.success(res.data.data.data)
            resetFields()
          } else {
            ElMessage.error(res.data.popMsg)
            resetFields()
          }
        })
        .catch(() => {
        })
    })
    .catch(() => {
      ElMessage.info('已取消合并！')
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
