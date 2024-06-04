<!-- 国家督察-档案变化情况统计 -->
<template>
  <div>
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="required label">地区选择：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" :showSelect="true" @nodeClick="nodeClick"
            @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="required label">日期范围：</span>
          <ss-month-range-new v-model:defaultValue="monthValue"
            :uptoDate="globalProxy.$consts.ENDDATEMONTH"></ss-month-range-new>
        </div>
        <div class="item">
          <span class="required label">自定义select：</span>
          <ss-custom-select></ss-custom-select>
        </div>
      </div>
      <ss-query-bottom :statisTics="true" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>


    <ss-custom-table :tableData="tableData" :columnList="columnList" @openDetailDialog="openDetailDialog"
      remarkText="①省、市、县、乡、接种单位采用静态计算。"></ss-custom-table>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { queryBaseChangedList, outputBaseChangedList } from "@/api/apis/gjdc";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { mainStore } from "@/store/index";
const store = mainStore()
const tableData = ref([]);
const secTime = ref(0)
const columnList = reactive([
  {
    label: '地区',
    prop: 'areaName',
    minWidth: '120',
  },
  {
    label: '新增',
    hasChildrenTitle: true,
    list: [
      {
        label: '档案',
        prop: 'addinfoCount',
        minWidth: '60'
      },
      {
        label: '接种信息',
        prop: 'addjzjlCount',
        minWidth: '60'
      }
    ]
  },
  {
    label: '修改',
    minWidth: '120',
    hasChildrenTitle: true,
    list: [
      {
        label: '档案',
        prop: 'modifyinfoCount',
        minWidth: '60'
      },
      {
        label: '接种信息',
        prop: 'modifyjzjlCount',
        minWidth: '60'
      },
    ]
  },
  {
    label: '删除',
    minWidth: '120',
    hasChildrenTitle: true,
    list: [
      {
        label: '档案',
        prop: 'delinfoCount',
        minWidth: '60'
      },
      {
        label: '接种信息',
        prop: 'deljzjlCount',
        minWidth: '60'
      },
    ]
  },
  {
    label: '迁入',
    prop: 'qrCount',
  },
  {
    label: '迁出',
    prop: 'qcCount',
  },
]);
const searchData = ref(null);
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1),
  areaName: store.userInfo.areaName,
});
const monthValue = ref([globalProxy.$dateUtil.currentMonth(), globalProxy.$dateUtil.currentMonth()]);
// 查询
const search = () => {
  let startTime = monthValue.value[0]
  let endTime = monthValue.value[1]

  if (!startTime || !endTime) {
    return globalProxy.$util.showMsg('请选择日期范围！', 'error', 5000, true);
  }
  let { areaCode, areaId, selectAreaLevel: areaLevel, areaName, skipValue } = areaTreeValue
  const params = { areaCode, areaId, areaLevel, areaName, endTime: endTime, skipValue, startTime: startTime }
  searchData.value = params;
  let res = {
    "total": 22,
    "list": [
      {
        "areaName": "银川市 6401000000",
        "areaCode": "640100000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "石嘴山市 6402000000",
        "areaCode": "640200000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "吴忠市 6403000000",
        "areaCode": "640300000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "固原市 6404000000",
        "areaCode": "640400000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "中卫市 6405000000",
        "areaCode": "640500000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "济南市 6421000000",
        "areaCode": "642100000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "青岛市 6422000000",
        "areaCode": "642200000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "淄博市 6423000000",
        "areaCode": "642300000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "枣庄市 6424000000",
        "areaCode": "642400000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "东营市 6425000000",
        "areaCode": "642500000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "济宁市 6428000000",
        "areaCode": "642800000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "泰安市 6429000000",
        "areaCode": "642900000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "日照市 6431000000",
        "areaCode": "643100000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "莱芜市 6432000000",
        "areaCode": "643200000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "临沂市 6433000000",
        "areaCode": "643300000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "德州市 6434000000",
        "areaCode": "643400000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "滨州市 6436000000",
        "areaCode": "643600000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "自测市A 6438000000",
        "areaCode": "643800000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "测试删除某接种单位数据 6455000000",
        "areaCode": "645500000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "原地区 6488000000",
        "areaCode": "648800000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "测试新增地区 6492000000",
        "areaCode": "649200000000",
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      },
      {
        "areaName": "合计",
        "areaCode": null,
        "areaLevel": null,
        "childCount": 0,
        "addinfoCount": 0,
        "addjzjlCount": 0,
        "modifyinfoCount": 0,
        "modifyjzjlCount": 0,
        "delinfoCount": 0,
        "deljzjlCount": 0,
        "qrCount": 0,
        "qcCount": 0
      }
    ],
    "pageNum": 1,
    "pageSize": 22,
    "size": 22,
    "startRow": 0,
    "endRow": 21,
    "pages": 1,
    "prePage": 0,
    "nextPage": 0,
    "isFirstPage": true,
    "isLastPage": true,
    "hasPreviousPage": false,
    "hasNextPage": false,
    "navigatePages": 8,
    "navigatepageNums": [
      1
    ],
    "navigateFirstPage": 1,
    "navigateLastPage": 1
  }
  tableData.value = res.list;
  return
  queryBaseChangedList(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data.list;
      secTime.value = res.data.sec;
    } else {
      if (res.data.popMsg) {
        tableData.value = [];
        secTime.value = "";
        globalProxy.$util.showMsg(res.data.popMsg, 'error');
      }
    }
  })
}
const nodeClick = (node) => {
  areaTreeValue = node;
  areaTreeValue.skipValue = String(Number(areaTreeValue.selectAreaLevel) + 1);
}
const selectClick = (node) => {
  areaTreeValue.skipValue = node;
}
// 导出excel
const exportExcel = () => {
  if (!searchData.value || searchData.value.areaCode != areaTreeValue.areaCode) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  let params = JSON.parse(JSON.stringify(searchData.value))
  params.areaName = params.areaName + " " + params.areaCode.slice(0, 10);
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT;
  outputBaseChangedList(params);
}
</script>
<style scoped lang='scss'></style>