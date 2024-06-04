<template>
  <div class="drag-table">
    <div class="container" ref="table_ref">
      <!-- v-dragable="dragOptions" -->
      <el-table row-key="id" :data="article" :stripe="true" max-height="1000" height="800">
        <el-table-column prop="id" label="文章ID" width="80" align="center"></el-table-column>
        <el-table-column prop="created_at" label="创建时间" width="200" align="center"></el-table-column>
        <el-table-column prop="title" label="标题" align="center"></el-table-column>
        <el-table-column prop="author" label="作者" width="200" align="center"></el-table-column>
        <el-table-column prop="pageviews" label="阅读量" width="100" align="center"> </el-table-column>
        <el-table-column prop="type" label="类别" width="100" align="center"> </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'published' ? 'success' : 'info'">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default>
            <!-- <el-icon class="iconfont icon-drag" style="font-size: 20px; cursor: pointer"></el-icon> -->
            操作
          </template>
        </el-table-column>
      </el-table>
      <!-- <div class="sort">
        <div>
          <el-tag>默认顺序</el-tag><span>[{{ oldIndexArr.toString() }}]</span>
        </div>
        <div>
          <el-tag>拖拽顺序</el-tag><span>[{{ newIndexArr.toString() }}]</span>
        </div>
      </div> -->
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, nextTick } from "vue";
import Sortable from "sortablejs";
import { vDragable } from "element-plus-table-dragable";
const article = ref([]);
let newIndexArr = ref([]);
let oldIndexArr = [];
const table_ref = ref(null);
onMounted(() => {
  nextTick(() => {
    initDrapTable()
  })
})
const dragOptions = [
  {
    selector: "tbody", // css选择器的字符串，定义哪些列表单元可以进行拖放
    option: {
      animation: 150,//number 单位：ms，定义排序动画的时间；
      onEnd: (evt) => {//function 列表单元拖放结束后的回调函数
        console.log(evt);
        const itemEl = evt.item;  // dragged HTMLElement
        console.log(itemEl);
        console.log("222", evt.oldIndex, evt.newIndex);
        const copyRow = JSON.parse(JSON.stringify(article.value[evt.oldIndex]));//拷贝拖动的一行数据存起来
        article.value.splice(evt.oldIndex, 1);//删除拖动的一行数据
        article.value.splice(parseInt(evt.newIndex), 0, copyRow);//插入到拖动的位置

      },
    },
  },
]
const getData = async () => {
  // const res=await Artiee.getArhhd()
  let datas = [
    { id: 1, title: "aa聚精会神放假回家好家伙估计哈手机很尬啊", author: "admin", type: "react", content: "", status: "draft", pageviews: 111, created_at: "2024-11-11" },
    { id: 2, title: "bb更好HDHG啊火锅店AHGHGFD F", author: "admin", type: "Laravel", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 3, title: "cc阿富汗撒法规好嘎嘎撒护肤撒谎干饭干饭我请客", author: "admin", type: "Laravel", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 4, title: "dd阿帆萨科技复健科卡积分凯撒反馈萨芬", author: "admin", type: "vue", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 5, title: "ee科技阿贾克斯副科级萨卡会计法是卡死放", author: "admin", type: "vue", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 6, title: "ff啊发发口算卡九分裤刷卡附件", author: "admin", type: "Laravel", content: "", status: "draft", pageviews: 111, created_at: "2024-11-11" },
    { id: 8, title: "hh啊合景峰汇结合实际阿帆萨芬就撒发货", author: "admin", type: "react", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 9, title: "ii武器无i去i人委屈我去", author: "admin", type: "vue", content: "", status: "draft", pageviews: 111, created_at: "2024-11-11" },
    { id: 10, title: "jj阿萨法撒可富卡收款方啊首付款", author: "admin", type: "thinkphp", content: "", status: "draft", pageviews: 111, created_at: "2024-11-11" },
    { id: 11, title: "kk阿里卡菲利克斯康复科萨法立卡达萨罗反馈", author: "admin", type: "vue", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 12, title: "ll阿萨法考拉时空裂缝拉屎弗利萨反馈卡死付了款撒付款了", author: "admin", type: "Laravel", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 13, title: "mm阿是副科级阿是会计法卡是否凯撒副科级萨芬萨法", author: "admin", type: "vue", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 14, title: "nn阿帆撒反馈经济开发科技阿是分开发撒可富", author: "admin", type: "Laravel", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 15, title: "ooo啊发放贷款甲方科技萨法快进到放", author: "admin", type: "thinkphp", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
  ]
  article.value = datas;
  // article.value = datas.splice(0, 10);
  for (let i = 0, len = article.value.length; i < len; i++) {
    oldIndexArr.push(article.value[i].id)
  }
  newIndexArr.value = oldIndexArr
}
getData()
const initDrapTable = () => {
  const tbody = document.querySelector('.drag-table .el-table__body-wrapper tbody');
  // const tbody = table_ref.value.querySelector('.el-table__body tbody')
  // const tbody = document.querySelectorAll('.drag-table .el-table__body-wrapper table > tbody')[0]
  Sortable.create(tbody, {
    animation: 100,
    draggable: ".drag-table .el-table__row",
    onEnd({ newIndex, oldIndex }) {
      //  可被拖拽的子元素
      const currRow = article.value.splice(oldIndex, 1)[0];
      article.value.splice(newIndex, 0, currRow);
      // let newArr = article.value
      // let oldIndex = evt.oldIndex || 0
      // let newIndex = evt.newIndex || 0
      // const currentRow = article.value.splice(oldIndex, 1)[0]
      // // const currentRow = newArr.splice(oldIndex, 1)[0]
      // article.value.splice(newIndex, 0, currentRow)
      // let sortArr = []
      // let order = 0
      // article.value.forEach(item => {
      //   sortArr.push({ ...item, orderNum: order })
      //   order++
      // })
      // article.value = sortArr
      // nextTick(() => {
      //   // article.value = newArr
      //   // newIndexArr.value = []
      //   // for (let i = 0, len = article.value.length; i < len; i++) {
      //   //   newIndexArr.value.push(article.value[i].id)
      //   // }
      // })
    }
  })
}

</script>

<style lang="scss">
.drag-table {
  height: 100%;
  padding: 20px;
  overflow: auto;

  .container {
    padding: 20px;
    background: #ffffff;
  }

  .sort {
    padding: 20px 20px 0px;

    div {
      padding-bottom: 15px;

      span {
        padding-left: 10px;
      }
    }
  }
}
</style>