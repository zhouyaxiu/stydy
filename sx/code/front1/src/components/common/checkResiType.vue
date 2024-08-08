<template>
  <!-- 户籍类型复选框勾选 联动效果
  /** 父组件传参
    * v-disabled {Number}  禁用/启用复选框
  */
   -->
  <div class="flex">
    <el-checkbox :disabled="disabled" :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
    <el-checkbox-group :disabled="disabled" v-model="resiType" @change="handleCheckedChange" class="flex">
      <el-checkbox v-for="type in typeOptions" :label="type" :key="type">{{type}}</el-checkbox>
    </el-checkbox-group>
	</div>
</template>
<script>
import commonOptions from '@/mixin/commonOptions'
export default {
  name: 'checkResiType',
  mixins: [commonOptions],
  data () {
    return {
      // resiType: [446, 447, 450, 451, 452, 453, 454],
      // checkAll: true,
      typeOptions: ['市地内流动', '省际内流动', '国际流动', '县区内流动', '其他', '临时', '常住', '本省内流动'],
      resiType: ['市地内流动', '省际内流动', '国际流动', '县区内流动', '其他', '临时', '常住', '本省内流动'],
      checkAll: true,
      isIndeterminate: false,
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
    handleCheckAllChange (val) {
      this.resiType = val ? this.typeOptions : []
      this.isIndeterminate = false
    },
    handleCheckedChange (value) {
      let checkedCount = value.length
      this.checkAll = checkedCount === this.typeOptions.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.typeOptions.length
    },
    // changeResiType(val, item) {
		// 		let that = this
		// 		if(item.id === 445) { // 全部
		// 			if(val) {
		// 				that.resiType = []
		// 				that.resiTypeList.forEach(item => {
		// 					if(item.id !== 445){
		// 						that.resiType.push(item.id)
		// 					}
		// 				})
		// 			} else {
		// 				that.resiType = []
		// 			}
		// 		} else {
		// 			if(!val) {
		// 				that.delItem(445)
		// 				that.checkAll = false
		// 			}
		// 			if(item.id === 447) { // 全部流动
		// 				that.resiTypeList.forEach(item => {
		// 					if(item.supid === 447) {
		// 						that.delItem(item.id)
		// 					}
		// 				})
		// 				if(val) {
		// 					that.resiTypeList.forEach(item => {
		// 						if(item.supid === 447) {
		// 							that.resiType.push(item.id)
		// 						}
		// 					})
		// 				} else {
		// 					that.delItem(447)
		// 				}
		// 			}
		// 			if(item.supid === 447) { // 全部流动
		// 				if(!val) {
		// 					that.delItem(447)
		// 				}
		// 			}
		// 		}
		// 		if(val) {
		// 			that.delItem(item.id)
		// 			item.id!==445?that.resiType.push(item.id):''
		// 			if(that.findItem(450) && that.findItem(451) && that.findItem(452) && !that.findItem(447)) {
		// 				that.resiType.push(447)
		// 			}
		// 			if(that.resiType.length === that.resiTypeList.length - 1) {
		// 				// that.resiType.push(445)
		// 				that.checkAll = true
		// 			}
		// 		}
		// 	},
		// 	findItem(id) {
		// 		return(this.resiType.findIndex(item => item === id)) !== -1
		// 	},
		// 	delItem(id) {
		// 		let index = this.resiType.findIndex(item => item === id)
		// 		index !== -1 && this.resiType.splice(index, 1)
		// 	}
  },
  mounted () {
    // this.getResiTypeList()
  //   this.resiTypeList=[
  //       {
  //           "id": 445,
  //           "domicilename": "全部",
  //           "supid": null
  //       },
  //       {
  //           "id": 446,
  //           "domicilename": "本地",
  //           "supid": 445
  //       },
  //       {
  //           "id": 447,
  //           "domicilename": "全部流动",
  //           "supid": 445
  //       },
  //       {
  //           "id": 450,
  //           "domicilename": "本市外县",
  //           "supid": 447
  //       },
  //       {
  //           "id": 451,
  //           "domicilename": "本省外市",
  //           "supid": 447
  //       },
  //       {
  //           "id": 452,
  //           "domicilename": "外省",
  //           "supid": 447
  //       },
  //       {
  //           "id": 453,
  //           "domicilename": "其他",
  //           "supid": 445
  //       },
  //       {
  //           "id": 454,
  //           "domicilename": "临时",
  //           "supid": 445
  //       }
  //   ]
  }
}
</script>

<style scoped>
</style>
