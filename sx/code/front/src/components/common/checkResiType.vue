<template>
  <!-- 户籍类型复选框勾选 联动效果
  /** 父组件传参
    * v-disabled {Number}  禁用/启用复选框
  */
   -->
  <el-checkbox-group v-model="resiType" :disabled="disabled">
    <el-checkbox
      v-for="item in resiTypeList"
      :label="item.id"
      :key="item.id"
      @change="(val) => changeResiType(val, item)"
    >
      {{ item.domicilename }}
    </el-checkbox>
  </el-checkbox-group>
</template>
<script>
import commonOptions from '@/mixin/commonOptions'
export default {
  name: 'checkResiType',
  mixins: [commonOptions],
  data () {
    return {
      resiType: []
    }
  },
  props: {
    disabled: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  methods: {
    changeResiType (val, item) {
      let that = this
      if (item.id === 445) {
        // 全部
        if (val) {
          that.resiType = []
          that.resiTypeList.forEach((item) => {
            that.resiType.push(item.id)
          })
        } else {
          that.resiType = []
        }
      } else {
        if (!val) {
          that.delItem(445)
        }
        if (item.id === 447) {
          // 全部流动
          that.resiTypeList.forEach((item) => {
            if (item.supid === 447) {
              that.delItem(item.id)
            }
          })
          if (val) {
            that.resiTypeList.forEach((item) => {
              if (item.supid === 447) {
                that.resiType.push(item.id)
              }
            })
          } else {
            that.delItem(447)
          }
        }
        if (item.supid === 447) {
          // 全部流动
          if (!val) {
            that.delItem(447)
          }
        }
      }
      if (val) {
        that.delItem(item.id)
        that.resiType.push(item.id)
        if (
          that.findItem(450) &&
          that.findItem(451) &&
          that.findItem(452) &&
          !that.findItem(447)
        ) {
          that.resiType.push(447)
        }
        if (that.resiType.length === that.resiTypeList.length - 1) {
          that.resiType.push(445)
        }
      }
    },
    findItem (id) {
      return this.resiType.findIndex((item) => item === id) !== -1
    },
    delItem (id) {
      let index = this.resiType.findIndex((item) => item === id)
      index !== -1 && this.resiType.splice(index, 1)
    }
  },
  mounted () {
    this.getResiTypeList()
  }
}
</script>

<style scoped>
</style>
