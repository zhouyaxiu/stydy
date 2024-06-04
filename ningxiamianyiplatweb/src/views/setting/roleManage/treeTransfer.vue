 <!-- 自定义树形穿梭框组件
  /** 父组件传参
    * v-leftCardName {Number}  左侧标题
    * v-rightCardName {Boolean}  右侧标题
    * v-treeData {String}  树形结构数据
    * v-defaultProps {Object}  配置选项
    {
        label:'label',       指定节点标签为节点对象的某个属性值
        children:'children'  指定子树为节点对象的某个属性值
    }
    * v-defValue {Array}  默认值
    * @callabck {Function}  子组件回调父组件方法
 */ -->
<template>
  <el-row :gutter="20">
    <el-col :span="10">
      <el-card class="tree-transfer-card" :shadow="shadow">
        <template #header>
          <div class="card-header">
            <el-checkbox v-model="leftChecked" :label="leftCardName" size="large" @change="checkAll('left')" />
          </div>
        </template>
        <el-scrollbar height="240px">
          <el-tree ref="leftTreeRef" :data="ltTreeData" show-checkbox default-expand-all node-key="id"
            @check-change="changeBtnStatus" highlight-current :props="props.defaultProps" />
        </el-scrollbar>
      </el-card>
    </el-col>
    <el-col :span="4">
      <div class="center-btn-con">
        <el-button type="primary" style="margin-left: 10px" @click="clickHandle(eventType['2right'])"
          :disabled="rightBtnStatus">
          到右边<ss-common-icon elIcon="ArrowRight"></ss-common-icon>
        </el-button>
        <el-button type="primary" @click="clickHandle(eventType['2left'])" :disabled="leftBtnStatus">
          <ss-common-icon elIcon="ArrowLeft"></ss-common-icon>到左边
        </el-button>
        <!-- <el-button type="primary" @click="clickHandle(eventType['reset'])">
          <ss-common-icon elIcon="Refresh"></ss-common-icon>重 置
        </el-button> -->
      </div>
    </el-col>
    <el-col :span="10">
      <el-card class="tree-transfer-card">
        <template #header>
          <div class="card-header">
            <el-checkbox v-model="rightChecked" :label="rightCardName" size="large" @change="checkAll('right')" />
          </div>
        </template>
        <el-scrollbar height="240px">
          <el-tree ref="rightTreeRef" :data="rtTreeData" show-checkbox default-expand-all node-key="id"
            @check-change="changeBtnStatus" highlight-current :props="props.defaultProps" />
        </el-scrollbar>
      </el-card>
    </el-col>
  </el-row>
</template>
<script setup>
import { ref, watch } from 'vue';
//一些响应变量声明...
const leftChecked = ref(false)
const rightChecked = ref(false)
const leftTreeRef = ref()
const rightTreeRef = ref()
const rtTreeData = ref([])
const ltTreeData = ref([])
const rightBtnStatus = ref(true)
const leftBtnStatus = ref(true)
//事件类型
const eventType = {
  '2right': 1,
  '2left': 2,
  'initRight': 3,
  'reset': 4
}
// 监听传过来的数据初始化穿梭框
watch([() => { props.treeData }], () => {
  initLeft()
}, { deep: true })
watch(() => { props.defValue }, () => {
  initCheckedKeys()
}, { deep: true })


//子组件回调父组件方法
const emits = defineEmits(['callabck'])
//按钮事件
const clickHandle = (type) => {
  switch (type) {
    case eventType['2left']:
    case eventType['2right']:
      //左右两棵树根据keys隐藏节点
      let refTree = null
      let setRefTree = null
      if (type == eventType['2right']) {
        refTree = leftTreeRef
        setRefTree = rightTreeRef
      } else {
        refTree = rightTreeRef
        setRefTree = leftTreeRef
      }
      let treeData = refTree.value.root.childNodes
      const rightCheckedKeys = rightTreeRef.value.getCheckedKeys()
      let arr = getSelectNodes(treeData, [], type)
      nextTick(() => {
        if (type == eventType['2left']) {
          rightCheckedKeys.forEach(i => {
            const node = rightTreeRef.value.getNode(i)
            resetHalfCheckedNodes(node)
          })
        }
      })
      setSelectNodes(setRefTree.value.root.childNodes, arr, type)
      break;
    case eventType['reset']:
      ltTreeData.value = []
      rtTreeData.value = []
      nextTick(() => {
        initLeft()
        initCheckedKeys()
      })
      break;
    default:
      break;
  }
  //重置全选状态
  rightChecked.value = false
  leftChecked.value = false
  //获取所有选中节点值，回调
  nextTick(() => {
    setTimeout(() => {
      emits('callabck', getSelectedValue(rightTreeRef.value.root.childNodes, []), getSelectedKeyValue(rightTreeRef.value.root.childNodes, []))
    }, 1000)
  })
};
//获取右侧穿梭框所有叶子节点值
const getSelectedKeyValue = (nodes, keys) => {
  nodes.forEach(i => {
    if (i.isLeaf && i.visible) {
      keys.push(i.data[props.keyCode])
    }
    if (i.childNodes.length && i.visible) {
      getSelectedKeyValue(i.childNodes, keys)
    }
  })
  return keys
}
//获取右侧穿梭框所有叶子节点
const getSelectedValue = (nodes, nodeList) => {
  nodes.forEach(i => {
    if (i.isLeaf && i.visible) {
      nodeList.push(toRaw(i.data))
    }
    if (i.childNodes.length && i.visible) {
      getSelectedValue(i.childNodes, nodeList)
    }
  })
  return nodeList
}
//重置半选中节点状态
const resetHalfCheckedNodes = (node) => {
  const cNodes = node.parent.childNodes
  let hasNodeShow = false
  for (let i = 0; i < cNodes.length; i++) {
    if (cNodes[i].visible) {
      hasNodeShow = true
      break
    }
  }
  if (!hasNodeShow) {
    node.parent.visible = false
    if (node.parent.parent && node.parent.parent.level != 0) {
      resetHalfCheckedNodes(node.parent)
    }
  }
}
//获取所有选中/半选中节点值集合
const getSelectNodes = (nodes, keys, type) => {
  let arr = nodes.filter(i => {
    if (i.checked || i.indeterminate) {
      keys.push(i.data[props.keyCode])
    }
    return i.checked || i.indeterminate
  })
  arr.forEach(k => {
    k.checked ? k.visible = false : k.visible = true
    // k.checked = false
    if (type != eventType['initRight']) k.indeterminate = false
    return k.childNodes && (k.children = getSelectNodes(k.childNodes, keys, type))
  })
  return keys
}
//设置选中节点
const setSelectNodes = (nodes, keys, type) => {
  nodes.forEach(i => {
    if (keys.indexOf(i.data[props.keyCode]) > -1) {
      i.visible = true
      i.checked = false
    } else {
      if (type == eventType['initRight']) {
        i.visible = false
        i.checked = true//解决父节点全选问题
      }
    }
    if (i.childNodes && i.childNodes.length) {
      setSelectNodes(i.childNodes, keys, type)
    }
  })
}
//动态修改按钮启用状态
const changeBtnStatus = (node, checked, hasChecked) => {
  let rightCheckedKeys = removeHiddenKeys(rightTreeRef)
  let leftCheckedKeys = removeHiddenKeys(leftTreeRef)
  rightBtnStatus.value = leftCheckedKeys.length ? false : true
  leftBtnStatus.value = rightCheckedKeys.length ? false : true
}

//过滤掉隐藏节点
const removeHiddenKeys = (tree) => {
  let keys = tree.value.getCheckedKeys()
  for (let i = 0; i < keys.length; i++) {
    let node = tree.value.getNode(keys[i])
    if (!node.visible) {
      keys.splice(i, 1)
      i--
    }
  }
  return keys
}
//父组件传参--按钮类型
const props = defineProps({
  leftCardName: {
    type: String,
    required: false,
    default: '未授权模块'
  },
  rightCardName: {
    type: String,
    required: false,
    default: '已授权模块'
  },
  treeData: {
    type: Array,
    default: () => {
      return []
    }
  },
  defaultProps: {
    type: Object,
    default: () => {
      return {
        id: 'id',
        children: 'children',
        label: 'name'
      }
    }
  },
  defValue: {
    type: Array,
    default: () => []
  },
  keyCode: {
    type: String,
    default: 'id'
  },
  shadow: {
    type: String,
    default: 'never'
  }
})
//初始化/重置
const initCheckedKeys = () => {
  leftTreeRef.value.setCheckedKeys(props.defValue, false)
  nextTick(() => {
    let arr = getSelectNodes(leftTreeRef.value.root.childNodes, [], eventType['2right'])
    setSelectNodes(rightTreeRef.value.root.childNodes, arr, eventType['initRight'])
  })
}
const initLeft = () => {
  ltTreeData.value = props.treeData
  rtTreeData.value = props.treeData
}
//全部选中
const checkAll = (type) => {
  switch (type) {
    case 'left':
      if (leftChecked.value) {
        checkAllFun(leftTreeRef.value, ltTreeData.value)
      } else {
        checkAllFun(leftTreeRef.value, ltTreeData.value, [])
      }
      break;
    case 'right':
      if (rightChecked.value) {
        checkAllFun(rightTreeRef.value, rtTreeData.value)
      } else {
        checkAllFun(rightTreeRef.value, rtTreeData.value, [])
      }
      break
    default:
      break;
  }
}
const checkAllFun = (tree, data, arr) => {
  if (!arr) {
    arr = []
    data.forEach(i => {
      arr.push(i.id)
    })
  }
  tree.setCheckedKeys(arr)
}
</script>
<style lang="scss">
.center-btn-con {
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  height: 100%;

  .el-button {
    margin-bottom: 14px;
  }
}

.tree-transfer-card {
  .el-card__header {
    padding: 0 !important;
    line-height: 40px;
  }

  .card-header {
    background: #EEF4FF;
    padding: 0 20px;

    .el-checkbox__label {
      color: #213255;
    }
  }
}
</style>
