<template>
  <div>
    <el-dialog title="详情" ref="dialogBox" :modelValue="dialogVisible" :before-close="handleClose" @close="closeDialog">
      <p>暂无数据！</p>
      <template #footer>
        <div>
          <el-button @click="handleClose">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref} from "vue"
const props=defineProps({
  dialogVisible:{
    type:Boolean,
    default:false
  },
  pwd:{
    type:String,
    default:''
  }
})

const emit=defineEmits(["closeEvent"])

// 关闭弹框
function handleClose(){
  emit("closeEvent",false)
}

const dialogBox=ref()
function closeDialog(){
  dialogBox.value.resetFields()
}
</script>