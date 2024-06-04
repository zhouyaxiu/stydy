<template>
  <!-- 户籍类型复选框勾选 联动效果
  /** 父组件传参
    * v-disabled {Number}  禁用/启用复选框
  */
   -->
  <el-checkbox-group v-model="resiType" :disabled="disabled" style="white-space: nowrap;">
    <el-checkbox v-for="item in resiTypeList" :label="item.id" :key="item.id"
      @change="(val) => changeResiTypeFun(val, item)">
      {{ item.name }}
    </el-checkbox>
  </el-checkbox-group>
</template>
<script setup>
import { queryShowResiType, changeResiType } from '@/api/apis/common'
//父组件传参--按钮类型
const props = defineProps({
  disabled: {
    type: Boolean,
    required: false,
    default: false
  }
})
const resiType = ref([])
const resiTypeList = ref([])
const needResiType = ref([])

const changeResiTypeFun = (val, item) => {
  if (item.id == '445') {
    // 全部
    if (val) {
      resiType.value = []
      resiTypeList.value.forEach((item) => {
        resiType.value.push(item.id)
      })
    } else {
      resiType.value = []
    }
  } else {
    if (!val) {
      delItem(445)
    }
    if (item.id == '447') {
      // 全部流动
      resiTypeList.value.forEach((item) => {
        if (item.supid == '447') {
          delItem(item.id)
        }
      })

      if (val) {
        resiTypeList.value.forEach((item) => {
          if (item.supid == '447') {
            resiType.value.push(item.id)
          }
        })

      } else {
        delItem(447)
      }
    }
    if (item.supid == '447') {
      // 全部流动
      if (!val) {
        delItem(447)
      }
    }
  }
  if (val) {
    delItem(item.id)
    resiType.value.push(item.id)
    if (
      findItem(450) &&
      findItem(451) &&
      findItem(452) &&
      !findItem(447)
    ) {
      resiType.value.push(447)
    }
    if (resiType.value.length === resiTypeList.value.length - 1) {
      resiType.value.push(445)
    }
  }
  if (resiType.value.length > 0) {
    // changeResiType({ ids: resiType.value.join(",") }).then((res) => {
    //   if (res.data.code == 200) {
    //     needResiType.value = res.data.data.data
    //   }
    // })
  } else {
    resiType.value = []
    needResiType.value = []
  }
}
const findItem = (id) => {
  return resiType.value.findIndex((item) => item === id) !== -1
}
const delItem = (id) => {
  let index = resiType.value.findIndex((item) => item === id)
  index !== -1 && resiType.value.splice(index, 1)
}
onMounted(() => {
  needResiType.value = [
    "常住",
    "县区内流动",
    "市地内流动",
    "本省内流动",
    "省际内流动",
    "国际流动",
    "其他",
    "临时"
  ]

  resiTypeList.value = [
    {
      "id": 445,
      "name": "全部",
      "supid": null
    },
    {
      "id": 446,
      "name": "本地",
      "supid": "445"
    },
    {
      "id": 447,
      "name": "全部流动",
      "supid": "445"
    },
    {
      "id": 450,
      "name": "本市外县",
      "supid": "447"
    },
    {
      "id": 451,
      "name": "本省外市",
      "supid": "447"
    },
    {
      "id": 452,
      "name": "外省", "supid": "447"
    },
    {
      "id": 453,
      "name": "其他",
      "supid": "445"
    },
    {
      "id": 454,
      "name": "临时",
      "supid": "445"
    }
  ]

  resiTypeList.value.forEach((item) => {
    resiType.value.push(item.id)
  })

  // queryShowResiType({}).then((res) => {
  //   if (res.data.code == 200) {
  //     resiTypeList.value = res.data.data.data;
  //     resiTypeList.value.forEach((item) => {
  //       resiType.value.push(item.id)
  //     })
  //     changeResiType({ ids: resiType.value.join(",") }).then((res) => {
  //       if (res.data.code == 200) {
  //         needResiType.value = res.data.data.data
  //       }
  //     })
  //   }
  // })
})
// 公开数据
defineExpose({
  resiType,
  needResiType,
  resiTypeList
})
</script>

<style scoped></style>
