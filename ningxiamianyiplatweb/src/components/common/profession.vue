<template>
  <div class="ss-tree">
    <el-tree-select v-model="treeValue" check-strictly lazy :load="loadNode" class="tree-select-cls"
      @node-click="nodeClick">
    </el-tree-select>
  </div>
</template>
<script setup>
import { queryProfessions } from '@/api/apis/common';
let treeValue = ref("1");
let treeName = ref("全部职业");
let childs = reactive([]);
const loadNode = async (node, resolve) => {
  if (node.data.leaf) return resolve([])
  let level = node.level;
  let item = node;
  if (level == 0) {
    await queryProfessions({
      professionsCode: treeValue.value
    }).then((res) => {
      let arr = [];
      arr.push({
        value: res.data.data.professionCode,
        label: res.data.data.professionName,
      })
      childs.value = Object.assign([], res.data.data.childs)
      return resolve(arr)
    })
  } else if (level == 1) {
    childs.value.forEach((items) => {
      items.value = items.professionCode,
        items.label = items.professionName,
        items.leaf = items.leaf,
        items.children = items.childs
    })
    return resolve(childs.value)
  } else {
    await queryProfessions({
      professionsCode: item.data.professionCode || node.data.value
    }).then((res) => {
      res.data.data.childs.forEach((items) => {
        items.value = items.professionCode,
          items.label = items.professionName,
          items.leaf = items.leaf,
          items.children = items.childs
      })
      if (res.data.data.leaf) {
        item.isLeaf = true
        return resolve(res.data.data.childs)
      }
      return resolve(res.data.data.childs)
    })
  }
}
const nodeClick = (node) => {
  treeName.value = node.professionName;
}
defineExpose({
  treeValue,
  treeName
})
</script>
<style lang="scss">
.ss-tree {
  .tree-select-cls {
    width: 100%;
  }

  .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #E5F2FF;
    color: #3385FF;

    .el-tree-node__expand-icon.is-leaf {
      color: #3385FF;

      &::before {
        content: '';
        width: 8px;
        height: 8px;
        background-color: #3385FF;
        border-radius: 50%;
      }

      svg {
        display: none;
      }

    }
  }
}
</style>
