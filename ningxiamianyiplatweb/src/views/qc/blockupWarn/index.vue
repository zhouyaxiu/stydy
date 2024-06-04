<template>
  <el-table ref="elTableRef" :data="tableData" style="width: 100%" row-key="date" :key="forceUpdate">
    <el-table-column prop="date" label="Date" width="180" />
    <el-table-column prop="name" label="Name" width="180" />
    <el-table-column prop="address" label="Address" />
  </el-table>

</template>
<script setup>
import Sortable from 'sortablejs';
import { onMounted, ref } from 'vue';
import {ElMessage,ElMessageBox} from "element-plus"
const tableData=ref([]);
const elTableRef=ref();
const forceUpdate=ref(0);
const setSort=()=>{
  const el=elTableRef?.value?.$el.querySelector("tbody");
  new Sortable(el,{
    animation:180,
    delay:0,
    // 列表内元素顺序更新的时候触发
onUpdate: function (/**Event*/evt) {
  ElMessageBox.confirm(
    '是否拖拽到此位置?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      ElMessage({
        type: 'success',
        message: '排序成功',
      })
      return true;
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消排序',
      })
      // return false;
      forceUpdate.value = Date.now();
                  // 重新调用下排序
                    nextTick(() => {
                        setSort();
                    })

    })
},

    onEnd:(e)=>{
      const targetRow=tableData.value.splice(e.oldIndex,1);
      tableData.value.splice(e.newIndex,0,targetRow[0]);
    }
  })
}

onMounted(()=>{
  
  setTimeout(() => {
        tableData.value = [
            {
                date: '2016-05-01',
                name: 'Tom',
                address: 'No. 189, Grove St, Los Angeles',
            },
            {
                date: '2016-05-02',
                name: 'Tom',
                address: 'No. 189, Grove St, Los Angeles',
            },
            {
                date: '2016-05-03',
                name: 'Tom',
                address: 'No. 189, Grove St, Los Angeles',
            },
            {
                date: '2016-05-04',
                name: 'Tom',
                address: 'No. 189, Grove St, Los Angeles',
            },
        ]
    }, 200)
    setSort();
})
</script>