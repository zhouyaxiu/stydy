<template>
  <el-dialog title="导出字段设置" :visible.sync="visible" width="80%" :before-close="close" :close-on-click-modal="false" :destroy-on-close="true">
    <div class="transfer_container">
      <!-- 左侧框 -->
      <div class="left_transfer border">
        <header class="header_con">
          <div class="header_left">
            <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll"
              @change="handleCheckAllChange">未选择的列</el-checkbox>
            <span class="left_total">{{ dataleft.length }}</span>
          </div>
        </header>
        <div class="transfer_content">
          <el-checkbox-group v-model="checkedData" @change="handlecheckedDataChange" class="transfer_group">
            <el-checkbox v-for="item in dataleft" :label="item.fieldname" :key="item.fieldname">
              <span class="span_left">{{ item.fieldcnname }}</span>
            </el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
      <!-- 穿梭按键 -->
      <div class="middle_btn">
        <el-button circle class="middle_button" @click="goRight">选择</el-button>
        <el-button circle class="middle_button" @click="deleteSignle">移除</el-button>
        <el-button circle class="middle_button" @click="goRightAll">添加全部</el-button>
        <el-button circle class="middle_button" @click="deleteAll">移除全部</el-button>
      </div>
      <!-- 右侧框 -->
      <div class="right_transfer border">
        <header class="header_con">
          <div class="header_left">
            <el-checkbox :indeterminate="isIndeterminateRight" v-model="checkAllRight"
              @change="handleCheckAllChangeRight">已选择的列</el-checkbox>
            <span class="left_total">{{ dataright.length }}</span>
          </div>
        </header>
        <div class="transfer_content">
          <el-checkbox-group v-model="checkedDataRight" @change="handlecheckedDataChangeRight" class="transfer_group">
            <el-checkbox v-for="item in dataright" :label="item.fieldname" :key="item.fieldname">
              <span class="span_left">{{ item.fieldcnname }}</span>
            </el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="confirm" type="primary">确 定</el-button>
      <el-button @click="close">取 消</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { fingExportColumnBySchId, saveExportColumnBySchId } from '@/utils/axios/apis/check'
export default {
  name: 'treeTransfer',
  data () {
    return {
      // searchLeft: "",//左侧搜索条件
      checkAll: false,
      checkedData: [],
      isIndeterminate: false,
      currentPage: 1,
      goright: true,
      goleft: true,
      dataleft: [],
      totalleft: 0,
      totalright: 0,
      dataright: [],
      // searchRight: "",//右侧搜索条件
      checkedDataRight: [],
      isIndeterminateRight: false,
      checkAllRight: false,
      currentPageRight: 1,
      addLeft: 0
    }
  },
  props: {
    visible: {
      type: Boolean,
      required: true,
      default: false
    }
  },
  mounted () {
    this.getData()
  },
  watch: {
    // dataArr: {
    //   deep: true,
    //   handler: function (value) {
    //     if (value.length > 0) {
    //       this.dataleft = value[0];
    //       localStorage.setItem("originLeftData", JSON.stringify(this.dataleft))
    //       this.dataright = value[1];
    //       localStorage.setItem("originRighttData", JSON.stringify(this.dataright))
    //     }
    //   },
    //   immediate: true
    // },
    checkedData: {
      deep: true,
      handler: function (value) {
        if (value.length > 0) {
          this.goright = false
        } else {
          this.goright = true
        }
      },
      immediate: true
    },
    checkedDataRight: {
      deep: true,
      handler: function (value) {
        if (value.length > 0) {
          this.goleft = false
        } else {
          this.goleft = true
        }
      },
      immediate: true
    }
  },
  methods: {
    confirm () {
      if (this.dataright.length == 0) {
        this.$util.errorMsg('已选择列至少选中一个！')
        return false
      }
      this.checkedDataRight = []
      for (let i = this.dataright.length - 1; i >= 0; i--) {
        this.checkedDataRight.push(this.dataright[i].fieldname)
      }
      saveExportColumnBySchId({ schId: this.$store.state.schId, hasChoseColumn: this.checkedDataRight.join(',') }).then(res => {
        this.$emit('close', true)
        this.checkedDataRight = []
        this.getData()
      }).catch(() => {

      })
    },
    deleteSignle () {
      for (let i = this.dataright.length - 1; i >= 0; i--) {
        if (this.checkedDataRight.includes(this.dataright[i].fieldname)) {
          this.dataleft.push(this.dataright[i])
          this.dataright.splice(i, 1)
        }
      }
      this.checkedDataRight = []
    },
    deleteAll () {
      for (let i = this.dataright.length - 1; i >= 0; i--) {
        this.dataleft.push(this.dataright[i])
        this.checkedData.push(this.dataright[i].fieldname)
        this.dataright.splice(i, 1)
      }
    },
    getData () {
      fingExportColumnBySchId({ schId: this.$store.state.schId }).then(res => {
        this.dataleft = res.data.needChoseColumn
        this.dataright = res.data.hasChoseListColumn
      }).catch(() => {

      })
    },
    close () {
      this.$emit('close', true)
    },
    // 左侧全选
    handleCheckAllChange (val) {
      if (val) {
        this.dataleft.forEach(item => {
          this.checkedData.push(item.fieldname)
        })
      } else {
        this.checkedData = []
      }
      this.isIndeterminate = false
    },
    // 左侧单独选中
    handlecheckedDataChange () {
      let checkedCount = this.checkedData.length
      this.checkAll = checkedCount === this.dataleft.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.dataleft.length
    },
    // 搜索关键词
    // searchKeyLeft(val) {
    //   if (this.filterType == 1) {
    //     this.$emit("searchKeyLeft", val)
    //   } else {
    //     if (val != '') {
    //       let newData = [];
    //       this.dataleft.forEach(item => {
    //         if (item.label == val) {
    //           newData.push(item)
    //         } else if (item.label.includes(val)) {
    //           newData.push(item)
    //         }
    //       })
    //       this.dataleft = newData
    //     } else {
    //       this.dataleft = JSON.parse(localStorage.getItem("originLeftData"))
    //     }
    //   }
    // },
    // 右边穿梭到左边
    goLeft () {
      for (let i = this.dataright.length - 1; i >= 0; i--) {
        if (this.checkedDataRight.includes(this.dataright[i].fieldname)) {
          this.dataleft.push(this.dataright[i])
          this.dataright.splice(i, 1)
        }
      }
      // this.totalright = this.totalright - this.checkedDataRight.length
      // this.totalleft = this.totalleft + this.checkedDataRight.length
      // this.$emit("change", 'right', this.checkedDataRight, this.dataleft, this.dataright)
      this.goleft = true
      this.checkedDataRight = []
      this.isIndeterminateRight = false
      this.checkAllRight = false
      // localStorage.setItem("originLeftData", JSON.stringify(this.dataleft))
    },
    goRightAll () {
      this.dataleft.forEach(item => {
        this.checkedData.push(item.fieldname)
      })
      this.goRight()
    },
    // 左边穿梭到右边
    goRight () {
      for (let i = this.dataleft.length - 1; i >= 0; i--) {
        if (this.checkedData.includes(this.dataleft[i].fieldname)) {
          this.dataright.push(this.dataleft[i])
          // this.checkedDataRight.push(this.dataleft[i].fieldname)
          this.dataleft.splice(i, 1)
        }
      }
      console.log(this.checkedData, 'this.dataright')
      // this.totalleft = this.totalleft - this.checkedData.length
      // this.totalright = this.totalright + this.checkedData.length
      // this.$emit("change", 'right', this.checkedData, this.dataleft, this.dataright)
      // this.goright = true
      // this.checkedData = []
      // this.isIndeterminate = false;
      // this.checkAll = false
      // localStorage.setItem("originRighttData", JSON.stringify(this.dataright))
    },
    // 左边分页
    // _currentChangeLeft(currentPage) {
    //   this.$emit("currentChange", 'left', currentPage)
    // },
    // 右侧方法
    // 右侧全选
    handleCheckAllChangeRight (val) {
      if (val) {
        this.dataright.forEach(item => {
          this.checkedDataRight.push(item.fieldname)
        })
      } else {
        this.checkedDataRight = []
      }
      this.isIndeterminateRight = false
    },
    // 右侧单独选中
    handlecheckedDataChangeRight () {
      let checkedCount = this.checkedDataRight.length
      this.checkAllRight = checkedCount === this.dataright.length
      this.isIndeterminateRight = checkedCount > 0 && checkedCount < this.dataright.length
    },
    // 搜索关键词
    // searchKeyRight(val) {
    //   if (this.filterType == 1) {
    //     this.$emit("searchKeyRight", val)
    //   } else {
    //     if (val != '') {
    //       let newData = [];
    //       this.dataright.forEach(item => {
    //         if (item.label == val) {
    //           newData.push(item)
    //         } else if (item.label.includes(val)) {
    //           newData.push(item)
    //         }
    //       })
    //       this.dataright = newData
    //     } else {
    //       this.dataright = JSON.parse(localStorage.getItem("originRighttData"))
    //     }
    //   }
    // },
    _currentChangeRight (currentPage) {
      this.$emit('currentChange', 'right', currentPage)
    }
  }
}
</script>

<style>
.transfer_container {
  display: flex;
  min-width: 625px;
  overflow: hidden;
}

.border {
  border: 1px solid #ddd;
  width: 40%;
  min-width: 200px;
  min-height: 300px
}

.left_transfer .header_con,
.right_transfer .header_con {
  height: 40px;
  line-height: 40px;
  /* background: #F5F7FA; */
  border-bottom: 1px solid #EBEEF5;
  padding-left: 15px;
}

.header_left {
  position: relative;
}

.left_total {
  position: absolute;
  right: 15px;
  color: #909399;
  font-size: 12px;
  font-weight: 400;
}

.transfer_content {
  height: 245px;
  padding-left: 15px;
  overflow: auto
}

.transfer_search {
  margin-top: 10px
}

.transfer_group {
  display: flex;
  flex-direction: column;
}

.el-checkbox {
  margin-right: 0;
  margin-top: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.el-checkbox__label {
  display: inline;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  box-sizing: border-box;
  width: 100%
}

.span_left {
  width: 75%
}

.span_right {
  float: right;
  margin-right: 10px
}

.transfer_content::-webkit-scrollbar {
  width: 5px;
  height: 1px;
}

.transfer_content::-webkit-scrollbar-thumb {
  border-radius: 10px;
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #d2dfff;
}

.transfer_content::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 10px;
  background: #EDEDED;
}

.middle_btn {
  padding: 0 20px;
  display: flex;
  flex-direction: column;
  align-self: center;
}

.middle_button {
  margin: 5px 18px 5px 0;
}
</style>
