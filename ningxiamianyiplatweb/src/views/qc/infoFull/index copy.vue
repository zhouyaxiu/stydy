<!-- 质量控制-档案基本信息完整性 -->
<template>
  <div class="drag-table">
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="required label">单位选择：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" @nodeClick="nodeClick" :showSelect="true"
            @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="required label">是否合格：</span>
          <el-select v-model="qualified">
            <el-option :label="item.name" v-for="item in qualifiedOptions" :key="item.code" :value="item.code" />
          </el-select>
        </div>
        <div class="item">
          <span class="required label">受种者标识：</span>
          <el-select v-model="fisadult">
            <el-option v-for="item in fisadultOptions" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <span class="required label">出生日期：</span>
          <ss-month-range-new ref="birthRef" v-model:defaultValue="monthValue"></ss-month-range-new>
        </div>
        <div class="item">
          <span class="required label">户籍类型：</span>
          <ss-check-resiType ref="checkResiTypePage"></ss-check-resiType>
        </div>
      </div>
      <ss-query-bottom :statisTics="true" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <!-- <ss-sortable></ss-sortable> -->
    <div class="drag-table">
      <div class="container">
        <el-table ref="sortable" :data="article" :stripe="true">
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
        <div class="sort">
          <div>
            <el-tag>默认顺序</el-tag><span>[{{ oldIndexArr.toString() }}]</span>
          </div>
          <div>
            <el-tag>拖拽顺序</el-tag><span>[{{ newIndexArr.toString() }}]</span>
          </div>
        </div>
      </div>
    </div>
    <ss-remark-text
      remarkText="① 单项完整为档案中输入此项信息即可，联系电话为2个电话信息只要有一项即算完整；<br>② 总体完整为各单项完整均输入。<br>③ 当选择不合格并且以接种单位查看的时候，可以点击数字查看详情。"
      v-if="tableData && tableData.length > 0"
      style="padding:0 16px 16px;background: #fff;margin-top: -16px;"></ss-remark-text>
    <dialog-info :visible="dialogVisible" v-if="dialogVisible" @close="close" :detailData="detailData"></dialog-info>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { infofullList, infoOutput } from "@/api/apis/qc";
import dialogInfo from "./dialogInfo.vue";
import ssRemarkText from '@/components/common/RemarkText.vue';
import ssSortable from '@/components/table/sortable.vue';
import Sortable from "sortablejs";
const article = ref([]);
let newIndexArr = ref([]);
let oldIndexArr = [];
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { mainStore } from "@/store/index";
const store = mainStore()
const tableData = ref([]);
const secTime = ref(0);
const dialogVisible = ref(false);
const isNoQuality = ref(false)
const isJzd = ref(true)//是否接种点
const qualified = ref("1");//是否合格
const fisadult = ref("0");//受种者标识
const detailData = ref({});//详情页参数
const pageIndex = ref(1);
// //受种者标识选项 全部""  儿童0  成人 1
const fisadultOptions = globalProxy.$commonOptions.heraStateList;
// 是否合格选项  合格1  不合格0
const qualifiedOptions = globalProxy.$commonOptions.qualifiedList;

//出生日期默认日期
const birthRef = ref(null);
const monthValue = ref([globalProxy.$dateUtil.lastMonth(), globalProxy.$dateUtil.currentMonth()]);

// 户籍类型
const checkResiTypePage = ref(null);
const searchData = ref(null);
const sortable = ref(null);

let areaTreeValue = reactive({
  areaCode: store.userInfo.areaCode,
  areaName: store.userInfo.areaName,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1)
});
const nodeClick = (node) => {
  areaTreeValue = node;
  areaTreeValue.skipValue = String(Number(areaTreeValue.selectAreaLevel) + 1)
};
const selectClick = (node) => {
  areaTreeValue.skipValue = node;
}
// 查询
const search = (pageNum) => {
  if (checkResiTypePage.value.needResiType.length == 0) {
    return globalProxy.$util.showMsg('户籍类型必选！', 'error', 5000, true);
  }
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  const params = {
    areaCode: areaTreeValue.areaCode,
    areaName: areaTreeValue.areaName,
    skipLevel: areaTreeValue.skipValue,
    birthStart: monthValue.value[0],
    birthEnd: monthValue.value[1],
    fisadult: fisadult.value,
    queryType: qualified.value,
    regtype: checkResiTypePage.value.needResiType.join(","),
  }
  searchData.value = params;

  let res = [
    {
      "areaName": "银川市 6401000000",
      "areaCode": "640100000000",
      "areaLevel": null,
      "childCount": 10,
      "name": 45,
      "sex": 6,
      "birth": 10,
      "cid": 3,
      "fathername": 23,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 43,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "石嘴山市 6402000000",
      "areaCode": "640200000000",
      "areaLevel": null,
      "childCount": 23,
      "name": 1,
      "sex": 1,
      "birth": 21,
      "cid": 22,
      "fathername": 1,
      "mothername": 6,
      "regaddr": 10,
      "dwelladdr": 30,
      "phone": 40,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "吴忠市 6403000000",
      "areaCode": "640300000000",
      "areaLevel": null,
      "childCount": 40,
      "name": 30,
      "sex": 4,
      "birth": 5,
      "cid": 6,
      "fathername": 7,
      "mothername": 8,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "固原市 6404000000",
      "areaCode": "640400000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "中卫市 6405000000",
      "areaCode": "640500000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "济南市 6421000000",
      "areaCode": "642100000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "青岛市 6422000000",
      "areaCode": "642200000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "淄博市 6423000000",
      "areaCode": "642300000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "枣庄市 6424000000",
      "areaCode": "642400000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "东营市 6425000000",
      "areaCode": "642500000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "济宁市 6428000000",
      "areaCode": "642800000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "泰安市 6429000000",
      "areaCode": "642900000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "日照市 6431000000",
      "areaCode": "643100000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "莱芜市 6432000000",
      "areaCode": "643200000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "临沂市 6433000000",
      "areaCode": "643300000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "德州市 6434000000",
      "areaCode": "643400000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "滨州市 6436000000",
      "areaCode": "643600000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "自测市A 6438000000",
      "areaCode": "643800000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "测试删除某接种单位数据 6455000000",
      "areaCode": "645500000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "原地区 6488000000",
      "areaCode": "648800000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "测试新增地区 6492000000",
      "areaCode": "649200000000",
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    },
    {
      "areaName": "合计",
      "areaCode": null,
      "areaLevel": null,
      "childCount": 0,
      "name": 0,
      "sex": 0,
      "birth": 0,
      "cid": 0,
      "fathername": 0,
      "mothername": 0,
      "regaddr": 0,
      "dwelladdr": 0,
      "phone": 0,
      "all": 0,
      "all_rate": "-"
    }
  ]

  // if (qualified.value == '0') isNoQuality.value = false;
  // else isNoQuality.value = true;
  // if (areaTreeValue.skipValue == '4' || areaTreeValue.skipValue == '5') isJzd.value = true;
  // else isJzd.value = false;
  tableData.value = res;
  secTime.value = 0.01;
  // infofullList(params).then(res => {
  //   if (qualified.value == '0') isNoQuality.value = false;
  //   else isNoQuality.value = true;
  //   if (areaTreeValue.skipValue == '4' || areaTreeValue.skipValue == '5') isJzd.value = true;
  //   else isJzd.value = false;
  //   tableData.value = res.data.data;
  //   secTime.value = res.data.sec;
  // })
}
// 导出excel
const exportExcel = () => {
  let params = JSON.parse(JSON.stringify(searchData.value));
  if (!searchData.value || searchData.value.areaCode != areaTreeValue.areaCode || checkResiTypePage.value.needResiType.join(",") != params.regtype) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  let regtypesName = []
  checkResiTypePage.value.resiTypeList.map(item => checkResiTypePage.value.resiType.map(itm => { if (item.id == itm) { regtypesName.push(item.name) } }))
  params.regtypeCn = regtypesName.join(",");
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT;
  infoOutput(params)
}
// 点击弹框查看明细
const openDetailDialog = (areaCode, countType) => {
  detailData.value = {
    areaCode: areaCode,
    birthStart: searchData.value.birthStart,
    birthEnd: searchData.value.birthEnd,
    fisadult: searchData.value.fisadult,
    queryType: searchData.value.queryType,
    regtype: searchData.value.regtype,
    countType: countType,
    pageNum: pageIndex.value
  }
  dialogVisible.value = true;
}
// 关闭弹框
const close = () => {
  dialogVisible.value = false;
}


onMounted(() => {
  nextTick(() => {
    initDrapTable()
  })
})

const getData = async () => {
  // const res=await Artiee.getArhhd()
  let datas = [
    { id: 1, title: "aa", author: "admin", type: "react", content: "", status: "draft", pageviews: 111, created_at: "2024-11-11" },
    { id: 2, title: "bb", author: "admin", type: "Laravel", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 3, title: "cc", author: "admin", type: "Laravel", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 4, title: "dd", author: "admin", type: "vue", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 5, title: "ee", author: "admin", type: "vue", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 6, title: "ff", author: "admin", type: "Laravel", content: "", status: "draft", pageviews: 111, created_at: "2024-11-11" },
    { id: 8, title: "hh", author: "admin", type: "react", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    { id: 9, title: "ii", author: "admin", type: "vue", content: "", status: "draft", pageviews: 111, created_at: "2024-11-11" },
    { id: 10, title: "jj", author: "admin", type: "thinkphp", content: "", status: "draft", pageviews: 111, created_at: "2024-11-11" },
    // { id: 11, title: "kk", author: "admin", type: "vue", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    // { id: 12, title: "ll", author: "admin", type: "Laravel", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    // { id: 13, title: "mm", author: "admin", type: "vue", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    // { id: 14, title: "nn", author: "admin", type: "Laravel", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
    // { id: 15, title: "ooo", author: "admin", type: "thinkphp", content: "", status: "published", pageviews: 111, created_at: "2024-11-11" },
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
  console.log(sortable.value, 'sortable.valuesortable.value')
  const tbody = sortable.value.$el.querySelector('.el-table__body tbody')
  // const tbody = document.querySelectorAll('.drag-table .el-table__body-wrapper table > tbody')[0]
  Sortable.create(tbody, {
    animation: 100,
    delay: 0,
    onEnd({ newIndex, oldIndex }) {
      console.log(newIndex, oldIndex, '0000')
      let newArr = article.value
      // let oldIndex = evt.oldIndex || 0
      // let newIndex = evt.newIndex || 0
      const currentRow = newArr.splice(oldIndex, 1)[0]
      newArr.splice(newIndex, 0, currentRow)
      article.value = []
      nextTick(() => {
        article.value = newArr
        newIndexArr.value = []
        for (let i = 0, len = article.value.length; i < len; i++) {
          newIndexArr.value.push(article.value[i].id)
        }
      })
    }
  })
}

</script>
<style scoped lang='scss'>
.pointer {
  color: #3385ff
}
</style>
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