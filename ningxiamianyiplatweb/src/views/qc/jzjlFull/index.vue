<!-- 质量控制-档案接种信息完整性 -->
<template>
  <div>
    <ss-condition-area>
      <div class="condition-row jzjlfull">
        <div class="item">
          <span class="required label">地区选择：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" @nodeClick="nodeClick" :showSelect="true"
            @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <div class="flex">
            <el-checkbox v-model="birthDisabled" size="large" class="ml20">
              <span class="label" style="line-height:32px;height:32px;margin-left:4px;">出生日期：</span>
            </el-checkbox>
          </div>
          <ss-month-range-new ref="birthRef" v-model:defaultValue="monthValue"
            :disabledFlag="!birthDisabled"></ss-month-range-new>
        </div>
        <div class="item">
          <el-checkbox v-model="ageDisabled" size="large" class="ml20">
            <span class=" label" style="line-height:32px;height:32px;margin-left:4px;">年龄范围（岁）：</span>
          </el-checkbox>
          <el-input v-model="beginAge" maxlength="3" min="1" max="999" :readonly="!ageDisabled" style="width:80px" />
          <span class="util">至</span>
          <el-input v-model="endAge" maxlength="3" min="1" max="999" :readonly="!ageDisabled" style="width:80px" />
        </div>
        <div class="item">
          <span class="required label">接种日期：</span>
          <ss-month-range-new ref="jzRef" v-model:defaultValue="jzmonthValue"></ss-month-range-new>
        </div>
        <div class="item">
          <span class="required label">户籍类型：</span>
          <ss-check-resiType ref="checkResiTypePage"></ss-check-resiType>
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
      <ss-query-bottom :statisTics="true" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList" @openDetailDialog="openDetailDialog" :total="0"
      remarkText="① 单项完整为档案中输入此项信息即可；<br>② 总体完整为各单项完整均输入。"></ss-custom-table>
  </div>
</template>

<script setup>
import { ref } from 'vue';
// import { getStandardDataList } from "@/api/apis/basicCodeQuery";
const tableData = ref([]);
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { positive_interger } from "@/utils/common/regex";
import { mainStore } from "@/store/index";
const store = mainStore()
//分页
const total = ref(0);
const pageIndex = ref(1);
const pageSize = ref(20);
const secTime = ref(0);
const isNoQuality = ref(true)
//表格列
const columnList = reactive([
  {
    label: '地区',
    prop: 'areaName',
  },
  {
    label: '接种剂次数',
    prop: 'jzjlCount',
  },
  {
    label: '总体完整',
    hasChildrenTitle: true,
    list: [
      {
        label: '完整数',
        prop: 'all',
        minWidth: '60'
      },
      {
        label: '完整率(%)',
        prop: 'all_rate',
        minWidth: '60'
      }
    ]
  },
  {
    label: '单项完整数',
    minWidth: '120',
    hasChildrenTitle: true,
    list: [
      {
        label: '生产企业',
        prop: 'fenterno',
        minWidth: '60'
      },
      {
        label: '疫苗批号',
        prop: 'ymph',
        minWidth: '60'
      },
      {
        label: '疫苗有效期',
        prop: 'ymyxq',
        minWidth: '60'
      },
      {
        label: '接种部位',
        prop: 'fplacecode',
        minWidth: '60'
      },
      {
        label: '接种人员',
        prop: 'jzys',
        minWidth: '60'
      },
      {
        label: '接种单位名称',
        prop: 'basecode',
        minWidth: '60'
      }
    ]
  }
])
// //受种者标识选项
const fisadultOptions = globalProxy.$commonOptions.heraStateList;
// 是否合格选项
const qualifiedOptions = globalProxy.$commonOptions.qualifiedList;

const birthRef = ref(null);//出生日期ref
const jzRef = ref(null);//接种日期ref

const birthDisabled = ref(false);//出生日期显影
const ageDisabled = ref(false);//年龄范围显影

const qualified = ref("1");//是否合格
const fisadult = ref("0");//受种者标识
const beginAge = ref("");//年龄范围开始
const endAge = ref("");//年龄范围截至
const checkResiTypePage = ref(null);
//接种日期默认日期
const monthValue = ref([]);
const jzmonthValue = ref([globalProxy.$dateUtil.lastMonth(), globalProxy.$dateUtil.currentMonth()]);

let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1)
});
const nodeClick = (node) => {
  areaTreeValue = node;
};
watch(() => birthDisabled.value, (val) => {
  if (val) {
    monthValue.value = [globalProxy.$dateUtil.lastMonth(), globalProxy.$dateUtil.currentMonth()]
  } else {
    monthValue.value = []
  }
}, { immediate: true, deep: true })
watch(() => ageDisabled.value, (val) => {
  if (!val) {
    beginAge.value = ''
    endAge.value = ''
  }
}, { immediate: true, deep: true })
// 查询
const search = (pageNum) => {
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  if (!birthDisabled.value && !ageDisabled.value) {
    return globalProxy.$util.showMsg('出生日期或年龄范围(岁)必须选中一个!', 'error', 5000, true);
  }
  if (ageDisabled.value && ((!beginAge.value || !endAge.value) || (beginAge.value || endAge.value) && !positive_interger.test(beginAge.value) || !positive_interger.test(endAge.value))) {
    return globalProxy.$util.showMsg('年龄范围必须是大于等于0的整数!', 'error', 5000, true);
  }
  if (ageDisabled.value && beginAge.value > endAge.value) {
    return globalProxy.$util.showMsg('开始年龄必须小于等于截止年龄!', 'error', 5000, true);
  }
  if (checkResiTypePage.value.needResiType.length == 0) {
    return globalProxy.$util.showMsg('户籍类型必选！', 'error', 5000, true);
  }
  if (qualified.value == '0') {
    isNoQuality.value = false;
    columnList[2].list[0].label = '不完整数';
    columnList[2].list[1].label = '不完整率(%)';
    columnList[3].label = '单项不完整数';
  } else {
    isNoQuality.value = true;
    columnList[2].list[0].label = '完整数';
    columnList[2].list[1].label = '完整率(%)';
    columnList[3].label = '单项完整数';
  }
  return
  const params = {
    currentPage: pageIndex.value,
    pageSize: pageSize.value,
  }
  // getStandardDataList(params).then(res => {
  //   if (res.code === 200) {
  //     tableData.value = res.data
  //     total.value = res.pagination.total
  //     secTime.value = 111
  //   }
  // })
}
const selectClick = (node) => {
  areaTreeValue.selectAreaLevel = node - 1
}
// 导出excel
const exportExcel = () => {

}

</script>
<style scoped lang='scss'>
.jzjlfull {
  flex-wrap: wrap;
  margin-bottom: -10px;

  .item {
    margin-bottom: 10px;
  }
}
</style>