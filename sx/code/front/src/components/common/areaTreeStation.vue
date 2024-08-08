<template>
  <!-- 树形选择
  /** 父组件传参
    * v-getToLevelNum {Number}  获取到哪个层级
    * v-disabled {Boolean}  输入框是否启用
    * v-showSelect {Boolean}  是否显示类型下拉
    * v-treeH {String}  展开树区域高度，最高为500px
    * v-noDefAreaCode {Boolean}  是否显示默认值
    * v-queryNode {Object}  查询条件选中的节点
    * v-isObstetrics {Number}  判断是否是产科，默认false
    * @nodeClick {Function}  子组件回调父组件方法
 */
  -->
  <div class="china-tree">
    <div style="display: flex">
      <el-input v-model.trim="areaInput" class="areaInput" :readonly="true" placeholder="" :title="areaInput"
        @click.native="showTreeFlag = true" v-clickoutsideList="closeAllTreePlan" :disabled="disabled" />
      <el-select v-model="selectType" style="width: 250px; margin-left: 10px"
        v-if="showSelect && changeSelectType && selectTypeList.length > 0">
        <el-option v-for="item in selectTypeList" :key="item.code" :label="item.name" :value="item.code" />
      </el-select>
    </div>
    <div v-show="!disabled && showTreeFlag" :style="'position: absolute;z-index: 2006;'">
      <el-tree class="tree-area" id="addressTree" :style="'width:' + treeW + ';min-width:300px;overflow-y:scroll;max-height:500px;height:' + treeH
        " ref="chinaTree" :show-checkbox="showCheckbox" node-key="areacode" :default-expanded-keys="defaultExpand"
        :highlight-current="true" :check-on-click-node="true" :auto-expand-parent="true" :accordion="true"
        :props="defaultProps" :load="loadNode" lazy @node-click="nodeClick" @node-expand="nodeExpand">
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <div v-if="selectAreaCode == data.areacode">
            <ss-icon iconType="yes" size="16px" slot="reference" style="margin-left: 0; margin-right: 4px" />{{ node.label
            }}&nbsp;{{ data.areacode }}&nbsp;&nbsp;&nbsp;&nbsp;
            <!-- {{ data.newBaseCode }} -->
          </div>
          <div v-else>
            {{ node.label }}&nbsp;{{ data.areacode }}&nbsp;&nbsp;&nbsp;&nbsp;
            <!-- {{ data.newBaseCode }} -->
          </div>
        </span>
      </el-tree>
    </div>
  </div>
</template>

<script>
import scrollbar from '@/mixin/scrollbar'
import { queryArea } from '@/utils/axios/api'
import { getLevel } from '@/utils/common/area.js'
export default {
  name: 'areaTree',
  mixins: [scrollbar],
  data() {
    return {
      areaInput: '',
      x: 445,
      y: 150,
      // treeW: "450px",
      treeW: 'auto',
      // treeH: '500px',
      scale: window.scale,
      showTreeFlag: false,
      showCheckbox: false,
      defaultExpand: [2, 185],
      selectAreaId: '',
      selectAreaCode: this.$store.state.areacode || '',
      selectAreaName: this.$store.state.areaname || '',
      // newBaseCode是10位地区编码
      newBaseCode: '',
      // 上级地区编码
      supcode: '',
      selectAreaLevel: 1,
      // BUG 25264 地区选择办事处级别时，无以接种点为单位查看选项
      changeSelectType: true,
      address: '',
      selectType: '',
      defaultProps: {
        value: 'areacode',
        label: 'areaname',
        children: 'children',
        isLeaf: 'leaf'
      },
      type1: {
        code: '1',
        name: '以地市为单位查看'
      },
      type2: {
        code: '2',
        name: '以县区为单位查看'
      },
      type3: {
        code: '3',
        name: '以乡镇为单位查看'
      },
      type4: {
        code: '4',
        name: '以接种点为单位查看'
      },
      selectTypeList: []
    }
  },
  props: {
    // 判断是否是产科
    isObstetrics: {
      type: Boolean,
      required: false,
      default: false
    },
    // 获取到哪个层级
    getToLevelNum: {
      type: Number,
      required: false,
      default: 5
    },
    disabled: {
      type: Boolean,
      required: false,
      default: false
    },
    showSelect: {
      type: Boolean,
      required: false,
      default: false
    },
    treeH: {
      type: String,
      // default: "500px",
      default: 'auto'
    },
    // 是否显示默认值
    noDefAreaCode: {
      type: Boolean,
      required: false,
      default: false
    },
    // 查询条件选中的节点
    queryNode: {
      type: Object,
      default: () => ({})
    }
  },
  watch: {
    selectAreaLevel(newVal) {
      this.getSelectTypeList(newVal)
    }
  },
  created() {
    this.type4 = {
      code: '4',
      name: this.isObstetrics ? '以产科为单位查看' : '以接种点为单位查看'
    }
  },
  methods: {
    nodeExpand(data, checked, node) {
      let that = this
      // document.querySelector("#addressTree").scrollIntoView();
      this.$refs.chinaTree.$el.scrollIntoView()
      if (checked.level !== 5) {
        that.expendNode(checked)
      }
      that.$nextTick(() => {
        that.setScrollBar()
        that.setTreeStyle()
      })
    },
    // 改变节点的状态
    expendNode(currentNode) {
      var nodes = this.$refs.chinaTree.store.nodesMap
      let currentCode = currentNode.data.areacode
      for (var i in nodes) {
        let tempNode = nodes[i]
        if (currentNode.level === 1 && tempNode.data.areacode !== currentCode) {
          nodes[i].expanded = false
        }
        if (
          currentNode.level === 2 &&
          tempNode.data.areacode !== currentCode &&
          tempNode.data.areacode !== currentNode.parent.data.areacode
        ) {
          nodes[i].expanded = false
        }
        if (
          currentNode.level === 3 &&
          tempNode.data.areacode !== currentCode &&
          tempNode.data.areacode !== currentNode.parent.data.areacode &&
          tempNode.data.areacode !== currentNode.parent.parent.data.areacode
        ) {
          nodes[i].expanded = false
        }
        if (
          currentNode.level === 4 &&
          tempNode.data.areacode !== currentCode &&
          tempNode.data.areacode !== currentNode.parent.data.areacode &&
          tempNode.data.areacode !== currentNode.parent.parent.data.areacode &&
          tempNode.data.areacode !==
          currentNode.parent.parent.parent.data.areacode
        ) {
          nodes[i].expanded = false
        }
      }
    },
    nodeClick(data, checked, node) {
      let that = this
      that.findNodeData(data, checked)
      this.$emit('nodeClick', data, checked, node)
    },
    findNodeData(data, checked) {
      let that = this
      that.selectAreaName = data.areaname
      // 12位地区编码
      that.selectAreaCode = data.areacode
      // 10位地区编码
      that.newBaseCode = data.newBaseCode
      that.supcode = data.supcode
      that.areaInput = that.selectAreaName + ' ' + data.areacode // that.newBaseCode;
      that.showTreeFlag = false
      that.selectAreaLevel = getLevel(that.selectAreaCode)
      that.selectAreaId = data.id
      sessionStorage.areacodeSelect=data.areacode
      sessionStorage.areaLevelSelect=getLevel(that.selectAreaCode)
    },
    loadNode(node, resolve) {
      let that = this
      let level = node.level
      let item = node.data
      // let nodes = null
      if (level === 0) {
        queryArea({
          areaCode: that.$store.state.areacode
        }).then((res) => {
          let arr = []
          if (res.length === 0) that.treeH = '0px'
          if (res && res.length > 0) {
            let newBaseCode = that.$store.state.areacode
            if (newBaseCode !== '' && newBaseCode.length === 12) {
              newBaseCode = newBaseCode.substr(0, 10)
            }
            this.supcode = res[0].supcode
            arr.push({
              id: that.$store.state.areaId,
              areacode: that.$store.state.areacode,
              areaname: that.$store.state.areaname,
              newBaseCode: newBaseCode,
              leaf: false,
              children: res
            })
          }
          return resolve(arr)
        })
      } else if (level > 0 && level < this.getToLevelNum) {
        // 一级一级加载
        queryArea({
          areaCode: item.areacode
        }).then((res) => {
          if (level === this.getToLevelNum - 1) {
            res.forEach((item) => {
              item.leaf = true
            })
          }
          if (res.length === 0) {
            item.leaf = true
          }

          return resolve(res)
        })
      } else if (level >= this.getToLevelNum) {
        return resolve([])
      }
    },
    closeAllTreePlan() {
      this.showTreeFlag = false
    },
    getSelectTypeList(level) {
      let that = this
      that.selectType = ''
      that.changeSelectType = true
      if (that.showSelect) {
        if (level === '0') {
          // 省
          that.selectTypeList = [
            that.type1,
            that.type2,
            that.type3,
            that.type4
          ]
        } else if (level === '1') {
          // 市
          that.selectTypeList = [that.type2, that.type3, that.type4]
        } else if (level === '2') {
          // 县
          that.selectTypeList = [that.type3, that.type4]
        } else if (level === '3') {
          // 乡
          that.selectTypeList = [that.type4]
          that.changeSelectType = false
        } else {
          that.selectTypeList = []
        }
        if (that.selectTypeList && that.selectTypeList.length > 0) {
          that.selectType = that.selectTypeList[0].code
        }
      }
    },
    setTreeStyle() {
      let paddingRt = 0
      if (this.$refs.chinaTree.$el.querySelector('.el-tree-node__content')) {
        paddingRt = 14
      }
      this.$refs.chinaTree.$el.style.paddingRight = `${paddingRt}px`
    }
  },
  mounted() {
    let that = this
    // 查询条件是否有选中节点
    if (Object.keys(this.queryNode).length) {
      // console.log('queryNode', this.queryNode)
      that.newBaseCode = that.queryNode.areacode.substr(0, 12)
      that.areaInput = that.queryNode.areaname + ' ' + that.newBaseCode
      that.selectAreaLevel = getLevel(that.queryNode.areacode)
      that.getSelectTypeList(that.selectAreaLevel)
      that.selectAreaId = that.queryNode.id
      that.defaultExpand = [that.queryNode.areacode]
      that.selectAreaCode = that.queryNode.areacode
    } else {
      if (that.selectAreaCode !== '' && that.selectAreaCode.length === 12) {
        that.newBaseCode = that.selectAreaCode.substr(0, 12)
      }
      if (!this.noDefAreaCode) {
        that.areaInput = that.selectAreaName + ' ' + that.newBaseCode
      }
      that.selectAreaLevel = getLevel(that.selectAreaCode)
      that.getSelectTypeList(that.selectAreaLevel)
      that.selectAreaId = that.$store.state.areaId
      that.defaultExpand = [that.selectAreaCode]
    }
    that.setScrollBar()
  },
  directives: {
    clickoutsideList: {
      bind: function (el, binding, vnode) {
        function documentHandler(e) {
          if (el.contains(e.target)) {
            return false
          }
          if (e.target.className && e.target.className.indexOf('el-') > -1) {
            return false
          }
          if (binding.expression) {
            binding.value(e)
          }
        }
        el._vueClickOutside_ = documentHandler
        document.addEventListener('click', documentHandler)
      }
    }
  }
}
</script>
<style>
.china-tree .areaInput {
  cursor: pointer;
  width: 200px;
}

.china-tree .tree-area {
  color: #606266;
  /* border-radius: 2px; */
  box-sizing: border-box;
  border: solid 1px #e4e7ed;
}

.china-tree .el-tree-node__content {
  line-height: 36px !important;
  height: 36px !important;
}
</style>
