<template>
  <div v-loading="showLoading" element-loading-text="数据加载中...">
    <p>
      当前显示：{{ `${rowCount}行${customColCount + 2}列` }}， 显示/隐藏 table：
      <el-switch :model-value="showTable" @click="switchTableShow"></el-switch>
    </p>
    <el-table v-if="showTable" :data="tableData" style="width: 100%; height: 85%; ">
      <el-table-column prop="info" label="信息" width="80" fixed />
      <el-table-column prop="status" label="状态" width="80" fixed>
        <template #default="scope">
          <el-switch v-model="scope.row.status" @change="statusChange" />
        </template>
      </el-table-column>
      <el-table-column v-for="item in customColumns" :key="item.prop" :prop="item.prop" :label="item.label">
        <template #default="scope">
          <span v-if="!(item.prop === 'column1' && scope.row[item.prop])">
            {{ scope.row[item.prop] }}
          </span>
          <el-tooltip v-else placement="top-start">
            <template #content>
              <span>{{ "tooltip显示" + scope.row[item.prop] }}</span>
            </template>
            <span>{{ scope.row[item.prop] }}</span>
          </el-tooltip>
          <!-- <el-tooltip placement="top-start" :disabled="!(item.prop === 'column1' && scope.row[item.prop])">
            <template #content>
              <span>{{ "tooltip显示" + scope.row[item.prop] }}</span>
            </template>
            <span>{{ scope.row[item.prop] }}</span>
          </el-tooltip> -->
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import ElTable from "./table/src/table.vue";
import { ElMessage } from "element-plus";
// 假数据逻辑
const customColCount = 18; // 自定义列数
const rowCount = 200; // 行数
const customColumns = ref([])
const tableData = ref([])
const showLoading = ref(false)
const showTable = ref(false)
onBeforeMount(() => {
  // 初始化自定义列数据
  let temp = [];
  for (let i = 0; i < customColCount; i++) {
    temp.push({ prop: `column${i + 1}`, label: `第${i + 1}列` });
  }
  customColumns.value = temp;
  console.log(customColumns, 'customColumnscustomColumns')
  // 初始化表格数据
  let dataTemp = [];
  for (let i = 0; i < rowCount; i++) {
    let row = { info: `第${i + 1}行`, status: true };
    i === 0 && (row.status = false);
    for (let j = 0; j < customColCount + 2; j++) {
      row[`column${j + 1}`] = `第${i + 1}行${j + 1}列`;
    }
    dataTemp.push(row);
  }
  tableData.value = dataTemp;
});

// 显示/隐藏 table，计算 table 渲染耗时
const switchTableShow = () => {
  // 先展示 loading
  showLoading.value = true;

  // 200ms 后再修改 table 是否显示，防止和 loading 合并到一个渲染周期，导致 loading 不显示
  setTimeout(() => {
    let startTime = +new Date();
    showTable.value = !showTable.value; // 修改 table 显示，会形成 script 阻塞
    showLoading.value = false; // 这里的 loading 关闭，会在 table 阻塞完成后渲染关闭 dom
    // 创建一个宏任务，等上面阻塞的微任务执行完成后，再显示计算耗时
    setTimeout(() => {
      let endTime = +new Date();
      ElMessage.success(`渲染耗时：${(endTime - startTime) / 1000}s`);
    }, 0);
  }, 200);
};
const statusChange = () => {
  let startTime = +new Date();
  // 加延时模拟业务逻辑：请求接口设置开关状态，成功后请求列表接口刷新数据
  setTimeout(() => {
    // 3s 后操作成功，且拿到了新的列表接口数据，进行列表更新
    tableData.value = JSON.parse(JSON.stringify(tableData.value));
    setTimeout(() => {
      let endTime = +new Date();
      ElMessage.success(`渲染耗时：${(endTime - startTime) / 1000}s`);
    }, 0);
  }, 300);
};
</script>
