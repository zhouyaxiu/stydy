 <!-- 自定义树形下拉选择组件---可多选
  /** 父组件传参
    * v-getToLevelNum {Number}  获取到哪个层级
    * v-showSelect {Boolean}  是否显示下拉
    * v-multiple {Boolean}  是否可多选
    * v-defTreeValue {String}  默认值
    * v-collapseTags {Boolean}  选择任意级别
    * v-treeProps {Object}  树节点配置项
     {
        label:'label',       指定节点标签为节点对象的某个属性值
        children:'children'  指定子树为节点对象的某个属性值
        isLeaf:'isLeaf'      指定子节点是否为叶子节点的某个属性值
    }
    * @nodeClick {Function}  子组件回调父组件方法
    * @selectClick {Function}  子组件回调父组件方法
 */ -->
<template>
  <div class="ss-tree">
    <el-tree-select v-model="treeValue" node-key="id" :data="dataList" :props="treeProps" @node-click="clickHandle"
      :check-on-click-node="true" :collapse-tags="collapseTags" check-strictly :multiple="multiple"
      :show-checkbox="multiple" @check="check" class="tree-select-cls">
    </el-tree-select>
    <el-select v-model="selectValue" v-if="showSelect && changeSelectType" style="margin-left: 10px;width: 180px;"
      @change="changeSelectValue">
      <el-option v-for="item in selectTypeList" :key="item.code" :label="item.name" :value="item.code" />
    </el-select>
  </div>
</template>
<script setup>
import { mainStore } from "@/store/index";
//子组件回调父组件方法
const emits = defineEmits(['nodeClick', 'selectClick', 'checkClick'])
const clickHandle = (node, treeNodeAttr, treeNode, event) => {//对应于节点点击的节点对象，TreeNode 的 node 属性, TreeNode和事件对象
  selectAreaLevel.value = getLevel(node.areacode || node.areaCode)
  node.selectAreaLevel = selectAreaLevel.value
  getSelectTypeList(selectAreaLevel.value)
  // 显示下拉，节点发生改变时返回下拉数据
  if (props.showSelect) {
    selectValue.value = String(Number(selectAreaLevel.value) + 1)
    emits('selectClick', selectValue.value)
  }
  node.areaName = node.areaname || node.areaName
  node.areaCode = node.areacode || node.areaCode
  node.areaId = node.id
  node.isLeaf = node.leaf
  emits('nodeClick', node)
};
// 多选时传areaId数组
const check = (val1, val2) => {
  emits('checkClick', val2.checkedKeys)
}
// 显示下拉时返回下拉数据
const changeSelectValue = () => {
  emits('selectClick', selectValue.value)
}
//父组件传参--按钮类型
const props = defineProps({
  defTreeValue: {
    type: String,
    required: false,
    default: ''
  },
  // 获取到哪个层级
  getToLevelNum: {
    type: Number,
    required: false,
    default: 5
  },
  showSelect: {
    type: Boolean,
    required: false,
    default: false
  },
  multiple: {
    type: Boolean,
    default: false
  },
  collapseTags: {
    type: Boolean,
    default: true
  },
  treeProps: {
    type: Object,
    default: () => {
      return {
        value: 'id',
        label: (data, node) => {
          return (data.areaname || data.areaName) + ' ' + (data.areacode || data.areaCode)
        },
        children: 'childs',
        isLeaf: 'leaf',
      }
    }
  },
  // 1:以地市为单位查看  2:以县区为单位查看  3;以乡镇为单位查看  4:以接种点为单位查看
  selectType: {
    type: Number,
    default: 2
  }
})
const store = mainStore()
let treeValue = ref(props.defTreeValue || store.userInfo.areaId)
const selectAreaCode = ref(store.userInfo.areaCode)
const selectAreaLevel = ref('1')
const dataList = ref([])
const defaultDataList = ref([])
const changeSelectType = ref(true)
const selectValue = ref('1')
const selectTypeList = ref([])
const type1 = reactive({
  code: '1',
  name: '以地市为单位查看'
})
const type2 = reactive({
  code: '2',
  name: '以区县为单位查看'
})
const type3 = reactive({
  code: '3',
  name: '以乡镇为单位查看'
})
const type4 = reactive({
  code: '4',
  name: props.isObstetrics ? '以产科为单位查看' : '以接种点为单位查看'
})
const getSelectTypeList = (level) => {
  changeSelectType.value = true
  if (props.showSelect) {
    if (level === '0') {
      // 省
      selectTypeList.value = [type1, type2, type3, type4]
    } else if (level === '1') {
      // 市
      selectTypeList.value = [type2, type3, type4]
    } else if (level === '2') {
      // 县
      selectTypeList.value = [type3, type4]
    } else if (level === '3') {
      // 乡
      selectTypeList.value = [type4]
    } else {
      selectTypeList.value = []
      changeSelectType.value = false
    }
  }
}
const getLevel = (code) => {
  let level = '-1'
  if (!code) {
    level = '-1'
  }
  if (code.substr(2, 12) === '0000000000') {
    // 省
    level = '0'
  } else if (code.substr(4, 12) === '00000000') {
    // 市
    level = '1'
  } else if (code.substr(6, 12) === '000000') {
    // 县
    level = '2'
  } else if (code.substr(8, 12) === '0000') {
    // 乡
    level = '3'
  } else if (code.substr(10, 12) === '00') {
    // 接种点
    level = '4'
  } else {
    level = '5'
  }
  return level
}
// 监听数据变化
store.$subscribe((params, state) => {
  if (params.events.key === 'treeArr') {
    getSelect(state.treeArr)
  }
})
const getSelect = (val) => {
  dataList.value = val || store.treeArr
  defaultDataList.value = JSON.parse(JSON.stringify(store.treeArr))
  changeSelectTypeNew(props.getToLevelNum - 1)
}
const changeSelectTypeNew = (val) => {
  let list = JSON.parse(JSON.stringify(defaultDataList.value))
  dataList.value = dealdata(val, list)
}
const dealdata = (num, list) => {
  if (num === 0) {
    for (let i = 0; i < list.length; i++) {
      list[i].leaf = true
      delete list[i].childs
    }
  } else {
    num--
    for (let i = 0; i < list.length; i++) {
      dealdata(num, list[i].childs ? list[i].childs : [])
    }
  }

  return list
}

onMounted(() => {
  selectAreaLevel.value = getLevel(selectAreaCode.value)
  getSelectTypeList(selectAreaLevel.value)
  getSelect()
})

watch(() => props.defTreeValue, (val) => {
  treeValue.value = val
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
