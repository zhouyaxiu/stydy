 <!-- 自定义树形下拉选择组件
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
 */ -->
<template>
    <el-tree-select v-model="treeValue" lazy :load="loadFun" :props="treeProps" @node-click="clickHandle"
        :collapse-tags="collapseTags" check-strictly :multiple="multiple" :show-checkbox="multiple"
        class="tree-select-cls" />
    <el-select v-model="selectValue" v-if="showSelect">
        <el-option v-for="item in selectionOPTS" :key="item.code" :label="item.label" :value="item.code" />
    </el-select>
</template>
<script setup>
//子组件回调父组件方法
const emits = defineEmits(['nodeClick'])
const clickHandle = (node, treeNodeAttr, treeNode, event) => {//对应于节点点击的节点对象，TreeNode 的 node 属性, TreeNode和事件对象
    console.log(node)
    emits('nodeClick', node)
};
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
                label: 'label',
                children: 'children',
                isLeaf: 'isLeaf',
            }
        }
    }
})
const selectValue = ref('')
const treeValue = ref('')
const selectionOPTS = ref([{ code: '11', label: 'lable-11' }, { code: '22', label: 'lable-22' }])
let id = 0
const loadFun = (node, resolve) => {
    //接口获取节点
    if (node.isLeaf) return resolve([])
    setTimeout(() => {
        resolve([
            {
                value: ++id,
                label: `lazy load node${id}`,
            },
            {
                value: ++id,
                label: `lazy load node${id}`,
                isLeaf: true,
            },
        ])
    }, 400)
}
</script>

<style scoped lang="scss">
.tree-select-cls {
    min-width: 300px;
    margin-right: 10px;
}
</style>
