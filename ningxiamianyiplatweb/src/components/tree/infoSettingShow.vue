<template>
  <div class="info-setting">
    <div class="transfer" v-loading="loading">
      <div class="title">
        <el-radio-group v-model="radioValue" @change="setPage(item)">
          <el-radio-button v-for="item in pageList" :label="item.pageId">{{
            item.pageName }}</el-radio-button>
        </el-radio-group>
        <div>
          <ss-custom-button :type="'update'" @click="save"></ss-custom-button>
          <ss-custom-button :type="'cancel'" @click="cancel"></ss-custom-button>
        </div>
      </div>
      <div class="main-content">
        <div class="column-button">
          <div class="button-transfer trangle trangle-top" @click="buttonHandler('left-top')"></div>
          <div class="button-transfer trangle trangle-top border" @click="buttonHandler('left-up')"></div>
          <div class="button-transfer trangle trangle-bottom border" @click="buttonHandler('left-down')"></div>
          <div class="button-transfer trangle trangle-bottom" @click="buttonHandler('left-bottom')"></div>
        </div>
        <div class="top_header">
          <div class="header_title">固定显示列</div>
          <div class="column-transfer" :style="{ height: height }">
            <div v-for="item in currentDefault" :key="item.columnId" class="col-item"
              :class="checkSelected1(item) ? 'col-selected' : ''" @click="pushSelected1(item)">
              {{ item.columnName }}
            </div>
          </div>
        </div>

        <div class="column-button">
          <div class="button-transfer arrow arrow-right" @click="buttonHandler('left-to-right-end-1')"></div>
          <div class="button-transfer arrow arrow-left" @click="buttonHandler('right-to-left-end-1')"></div>
          <div class="button-transfer arrow arrow-right single" @click="buttonHandler('left-to-right')"></div>
          <div class="button-transfer arrow arrow-left single" @click="buttonHandler('left-to-left')"></div>
        </div>
        <div class="top_header">
          <div class="header_title">未选列</div>
          <div class="column-transfer" :style="{ height: height }">
            <div v-for="item in currentNoUse" :key="item.columnId" class="col-item"
              :class="checkSelected2(item) ? 'col-selected' : ''" @click="pushSelected2(item)">
              {{ item.columnName }}
            </div>
          </div>
        </div>
        <div class="column-button">
          <div class="button-transfer arrow arrow-right" @click="buttonHandler('left-to-right-end-2')"></div>
          <div class="button-transfer arrow arrow-left" @click="buttonHandler('right-to-left-end-2')"></div>
          <div class="button-transfer arrow arrow-right single" @click=" buttonHandler('right-to-right')"></div>
          <div class="button-transfer arrow arrow-left single" @click=" buttonHandler('right-to-left')"></div>
        </div>
        <div class="top_header">
          <div class="header_title">活动显示列</div>
          <div class="column-transfer" :style="{ height: height }">
            <div v-for="item in currentFloat" :key="item.columnId" class="col-item"
              :class="checkSelected3(item) ? 'col-selected' : ''" @click="pushSelected3(item)">
              {{ item.columnName }}
            </div>
          </div>
        </div>
        <div class="column-button">
          <div class="button-transfer trangle trangle-top" @click="buttonHandler('right-top')"></div>
          <div class="button-transfer trangle trangle-top border" @click="buttonHandler('right-up')"></div>
          <div class="button-transfer trangle trangle-bottom border" @click="buttonHandler('right-down')"></div>
          <div class="button-transfer trangle trangle-bottom" @click="buttonHandler('right-bottom')"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { getShowInfoSetting, saveShowInfoSetting } from '@/api/apis/common'
import { mainStore } from "@/store/index";
const store = mainStore()
const props = defineProps({
  height: {
    default: '100%'
  },
  showCloseBtn: {
    type: Boolean,
    default: false
  }
})
let loading = ref(false)
let defaultCurrentPage = ref({
  pageName: '受种者个案',
  pageId: 1,
  pageDefaultColumns: [ // 左侧数据
  ],
  pageFloatColumns: [ // 右侧数据
  ],
  totalColumnList: [ // 全部的数据
  ]
})
let currentPage = ref({})
const selectedDefault = ref([]) // 左侧选中项
const selectedNoUse = ref([]) // 中间选中项
const selectedFloat = ref([]) // 右侧选中项
const radioValue = ref(0)
let pageList = ref([
  {
    pageName: '个案查询',
    pageId: 0,
    pageDefaultColumns: [ // 左侧数据--固定列
    ],
    pageFloatColumns: [ // 右侧数据--活动列
    ],
    totalColumnList: [ // 全部的数据
    ]
  },
  {
    pageName: '变迁情况查询',
    pageId: 1,
    pageDefaultColumns: [],
    pageFloatColumns: [],
    totalColumnList: [
    ]
  },
  // {
  //   pageName: '新生儿',
  //   pageId: 2,
  //   pageDefaultColumns: [],
  //   pageFloatColumns: [],
  //   totalColumnList: [
  //   ]
  // }
])
onMounted(() => {
  getSetting()
})
// 左侧数据
const currentDefault = computed(() => {
  if (!currentPage.value || !currentPage.value.pageDefaultColumns) {
    return []
  }
  return currentPage.value.pageDefaultColumns
})
// 中间数据
const currentNoUse = computed(() => {
  if (!currentPage.value.totalColumnList || !currentPage.value || !currentPage.value.pageDefaultColumns || !currentPage.value.pageFloatColumns) {
    return []
  }
  return currentPage.value.totalColumnList.filter(item => {
    return !checkColumn(item)
  })
})
// 右侧数据
const currentFloat = computed(() => {
  if (!currentPage.value || !currentPage.value.pageFloatColumns) {
    return []
  }
  return currentPage.value.pageFloatColumns
})
const getSetting = () => {
  loading.value = true
  getShowInfoSetting(
    {
      userId: store.userInfo.id,
      condition: radioValue.value
    }
  ).then(res => {
    loading.value = false
    let data = res.data.data.data
    for (const key in data) {
      if (key !== 'dbFields') {
        data[key] = data[key].map(el => {
          el.columnName = el.fieldCNName
          el.columnId = el.fieldName
          return el || []
        })
      }
    }
    defaultCurrentPage.value = {
      pageName: pageList.value[radioValue.value].pageName,
      pageId: radioValue.value,
      pageDefaultColumns: data.infoFixed,
      pageFloatColumns: data.infoFloat,
      totalColumnList: data.infoFixed.concat(data.infoFloat, data.infoNoSelect)
    }
    currentPage.value = defaultCurrentPage.value
  }).catch(() => {
    loading.value = false
  })
}
const getName = (list) => {
  let arr = []
  list.forEach(item => {
    arr.push(item.fieldName)
  })
  return arr.toString()
}
const isContained = (aa, bb) => {
  if (!(aa instanceof Array) || !(bb instanceof Array) || ((aa.length < bb.length))) {
    return false
  }
  var aaStr = aa.toString()
  for (var i = 0; i < bb.length; i++) {
    if (aaStr.indexOf(bb[i]) < 0) return false
  }
  return true
}
const save = () => {
  let list = []
  currentPage.value.pageDefaultColumns.forEach(item => {
    list.push(item.fieldName)
  })
  currentPage.value.pageFloatColumns.forEach(item => {
    list.push(item.fieldName)
  })
  list = Array.from(new Set(list))
  let saveParams = {
    userId: store.userInfo.id,
    condition: radioValue.value,
    floatCloumnVal: getName(currentPage.value.pageFloatColumns),// 活动列
    fixColumnVal: getName(currentPage.value.pageDefaultColumns)// 固定列
  }
  if (currentPage.value.pageId === 0) {
    const fixedName = ['EPI_CHILDINFO.FCHILDNO', 'EPI_CHILDINFO.NAME', 'EPI_CHILDINFO.BIRTH']
    const fixedCNName = ['编码', '姓名', '生日']
    if (!isContained(list, fixedName)) {
      ElMessage.error(`固定显示列或活动显示列必须包含{${fixedCNName.toString()}}`)
      // cancel()
      return
    }
  } else if (currentPage.value.pageId === 1) {
    const fixedName = ['EPI_TRANS.FCHILDNO', 'EPI_TRANS.NAME', 'EPI_TRANS.BIRTH', 'EPI_TRANS.TRANSTIME']
    const fixedCNName = ['编码', '姓名', '生日', '迁移时间']
    if (!isContained(list, fixedName)) {
      ElMessage.error(`固定显示列或活动显示列必须包含{${fixedCNName.toString()}}`)
      // cancel()
      return
    }
  } else if (currentPage.value.pageId === 2) {
    const fixedName = ['YY_CHILDINFO.CHILDNO', 'YY_CHILDINFO.UNITCODE', 'YY_CHILDINFO.NAME', 'YY_CHILDINFO.BIRTH', 'YY_CHILDINFO.SEX']
    const fixedCNName = ['编码', '建档地', '儿童姓名', '儿童出生日期', '儿童性别']
    if (!isContained(list, fixedName)) {
      ElMessage.error(`固定显示列或活动显示列必须包含{${fixedCNName.toString()}}`)
      // cancel()
      return
    }
  }
  loading.value = true
  saveShowInfoSetting(saveParams).then(res => {
    loading.value = false
    if (res.data.code === 200) {
      ElMessage.success(res.data.data.data)
    } else {
      ElMessage.error(res.data.data.data)
      setPage()
    }
  }).catch(() => {
    loading.value = false
    setPage()
  })
}
const cancel = () => {
  currentPage.value = JSON.parse(JSON.stringify(defaultCurrentPage.value))
  if (props.showCloseBtn) {
    close()
  }
}
// 只判断两个数组的某个值是否相等
const checkBeforeAndAfterName = (beforeArr, afterArr) => {
  return beforeArr.length === afterArr.length && beforeArr.every(function (v, i) { return v.columnName === afterArr[i].columnName })
}
// 判断两个数组转化前后是否相等
const checkBeforeAndAfter = (beforeArr, afterArr) => {
  return beforeArr.length === afterArr.length && beforeArr.every(function (v, i) { return v === afterArr[i] })
}
const buttonHandler = (type) => {
  if (!currentPage.value) {
    return
  }
  let arr = []
  let leftArr = JSON.parse(JSON.stringify(currentPage.value.pageDefaultColumns))
  let rigArr = JSON.parse(JSON.stringify(currentPage.value.pageFloatColumns))
  switch (type) {
    case 'left-top':
      if (selectedDefault.value.length === 0) {
        ElMessage.warning('请选中列表中的项')
        return
      }
      for (let item of currentPage.value.pageDefaultColumns) {
        if (checkSelected1(item)) {
          arr.push(item)
        }
      }
      for (let item of currentPage.value.pageDefaultColumns) {
        if (!checkSelected1(item)) {
          arr.push(item)
        }
      }
      if (checkBeforeAndAfter(currentPage.value.pageDefaultColumns, arr)) {
        ElMessage.warning(`${arr[0].columnName}已经是第一个`)
      }
      currentPage.value.pageDefaultColumns = arr
      selectedDefault.value = []
      break
    case 'left-up':
      if (selectedDefault.value.length === 0) {
        ElMessage.warning('请选中列表中的项')
        return
      }

      for (let i = 0; i < currentPage.value.pageDefaultColumns.length; i++) {
        if (checkSelected1(currentPage.value.pageDefaultColumns[i]) && i > 0) {
          let item = currentPage.value.pageDefaultColumns[i]
          currentPage.value.pageDefaultColumns.splice(i, 1)
          currentPage.value.pageDefaultColumns.splice(i - 1, 0, item)
        }
      }
      if (checkBeforeAndAfterName(leftArr, currentPage.value.pageDefaultColumns)) {
        ElMessage.warning(`${leftArr[0].columnName}已经是第一个`)
      }

      selectedDefault.value = []
      break
    case 'left-down':
      if (selectedDefault.value.length === 0) {
        ElMessage.warning('请选中列表中的项')
        return
      }
      //  const leftArr = JSON.parse(JSON.stringify(currentPage.value.pageDefaultColumns))
      for (let i = currentPage.value.pageDefaultColumns.length - 1; i >= 0; i--) {
        if (checkSelected1(currentPage.value.pageDefaultColumns[i]) && i < currentPage.value.pageDefaultColumns.length - 1) {
          let item = currentPage.value.pageDefaultColumns[i]
          currentPage.value.pageDefaultColumns.splice(i + 2, 0, item)
          currentPage.value.pageDefaultColumns.splice(i, 1)
        }
      }
      if (checkBeforeAndAfterName(leftArr, currentPage.value.pageDefaultColumns)) {
        ElMessage.warning(`${leftArr[leftArr.length - 1].columnName}已经是最后一个`)
      }
      selectedDefault.value = []
      break
    case 'left-bottom':
      if (selectedDefault.value.length === 0) {
        ElMessage.warning('请选中列表中的项')
        return
      }
      for (let item of currentPage.value.pageDefaultColumns) {
        if (!checkSelected1(item)) {
          arr.push(item)
        }
      }
      for (let item of currentPage.value.pageDefaultColumns) {
        if (checkSelected1(item)) {
          arr.push(item)
        }
      }
      if (checkBeforeAndAfter(currentPage.value.pageDefaultColumns, arr)) {
        ElMessage.warning(`${arr[arr.length - 1].columnName}已经是最后一个`)
      }
      currentPage.value.pageDefaultColumns = arr
      selectedDefault.value = []
      break
    case 'right-top':
      if (selectedFloat.value.length === 0) {
        ElMessage.warning('请选中列表中的项')
        return
      }
      for (let item of currentPage.value.pageFloatColumns) {
        if (checkSelected3(item)) {
          arr.push(item)
        }
      }
      for (let item of currentPage.value.pageFloatColumns) {
        if (!checkSelected3(item)) {
          arr.push(item)
        }
      }
      if (checkBeforeAndAfter(currentPage.value.pageFloatColumns, arr)) {
        ElMessage.warning(`${arr[0].columnName}已经是第一个`)
      }
      currentPage.value.pageFloatColumns = arr
      selectedFloat.value = []
      break
    case 'right-up':
      if (selectedFloat.value.length === 0) {
        ElMessage.warning('请选中列表中的项')
        return
      }
      for (let i = 0; i < currentPage.value.pageFloatColumns.length; i++) {
        if (checkSelected3(currentPage.value.pageFloatColumns[i]) && i > 0) {
          let item = currentPage.value.pageFloatColumns[i]
          currentPage.value.pageFloatColumns.splice(i, 1)
          currentPage.value.pageFloatColumns.splice(i - 1, 0, item)
        }
      }
      if (checkBeforeAndAfterName(rigArr, currentPage.value.pageFloatColumns)) {
        ElMessage.warning(`${rigArr[0].columnName}已经是第一个`)
      }
      selectedFloat.value = []
      break
    case 'right-down':
      if (selectedFloat.value.length === 0) {
        ElMessage.warning('请选中列表中的项')
        return
      }
      for (let i = currentPage.value.pageFloatColumns.length - 1; i >= 0; i--) {
        if (checkSelected3(currentPage.value.pageFloatColumns[i]) && i < currentPage.value.pageFloatColumns.length - 1) {
          let item = currentPage.value.pageFloatColumns[i]
          currentPage.value.pageFloatColumns.splice(i + 2, 0, item)
          currentPage.value.pageFloatColumns.splice(i, 1)
        }
      }
      if (checkBeforeAndAfterName(rigArr, currentPage.value.pageFloatColumns)) {
        ElMessage.warning(`${rigArr[rigArr.length - 1].columnName}已经是最后一个`)
      }
      selectedFloat.value = []
      break
    case 'right-bottom':
      if (selectedFloat.value.length === 0) {
        ElMessage.warning('请选中列表中的项')
        return
      }
      for (let item of currentPage.value.pageFloatColumns) {
        if (!checkSelected3(item)) {
          arr.push(item)
        }
      }
      for (let item of currentPage.value.pageFloatColumns) {
        if (checkSelected3(item)) {
          arr.push(item)
        }
      }
      if (checkBeforeAndAfter(currentPage.value.pageFloatColumns, arr)) {
        ElMessage.warning(`${arr[arr.length - 1].columnName}已经是最后一个`)
      }
      currentPage.value.pageFloatColumns = arr
      selectedFloat.value = []
      break
    case 'left-to-right-end-1':
      currentPage.value.pageDefaultColumns = []
      selectedDefault.value = []
      break
    case 'right-to-left-end-1':
      currentPage.value.pageDefaultColumns = currentPage.value.totalColumnList.filter(item => {
        return !checkSelected4(item)
      })
      selectedNoUse.value = []
      break
    case 'left-to-right-end-2':
      currentPage.value.pageFloatColumns = currentPage.value.totalColumnList.filter(item => {
        return !checkSelected5(item)
      })
      selectedNoUse.value = []
      break
    case 'right-to-left-end-2':
      currentPage.value.pageFloatColumns = []
      selectedFloat.value = []
      break
    case 'left-to-right':
      if (selectedDefault.value.length === 0) {
        ElMessage.warning('请选中左侧列表中的项')
        return
      }
      currentPage.value.pageDefaultColumns = currentPage.value.pageDefaultColumns.filter(item => {
        return !checkSelected1(item)
      })
      selectedDefault.value = []
      break
    case 'left-to-left':
      if (selectedNoUse.value.length === 0) {
        ElMessage.warning('请选中中间列表中的项')
        return
      }
      currentPage.value.pageDefaultColumns = [...currentPage.value.pageDefaultColumns, ...selectedNoUse.value]
      selectedNoUse.value = []
      break
    case 'right-to-left':
      if (selectedFloat.value.length === 0) {
        ElMessage.warning('请选中右侧列表中的项')
        return
      }
      currentPage.value.pageFloatColumns = currentPage.value.pageFloatColumns.filter(item => {
        return !checkSelected3(item)
      })
      selectedFloat.value = []
      break
    case 'right-to-right':
      if (selectedNoUse.value.length === 0) {
        ElMessage.warning('请选中中间列表中的项')
        return
      }
      currentPage.value.pageFloatColumns = [...currentPage.value.pageFloatColumns, ...selectedNoUse.value]
      selectedNoUse.value = []
      break
    default:
      break
  }
}
const pushSelected1 = (item) => {
  if (checkSelected1(item)) {
    selectedDefault.value = selectedDefault.value.filter(col => {
      return col.columnId !== item.columnId
    })
    return
  }
  selectedDefault.value.push(item)
}
const pushSelected2 = (item) => {
  if (checkSelected2(item)) {
    selectedNoUse.value = selectedNoUse.value.filter(col => {
      return col.columnId !== item.columnId
    })
    return
  }
  selectedNoUse.value.push(item)
}
const pushSelected3 = (item) => {
  if (checkSelected3(item)) {
    selectedFloat.value = selectedFloat.value.filter(col => {
      return col.columnId !== item.columnId
    })
    return
  }
  selectedFloat.value.push(item)
}
const checkSelected1 = (item) => {
  return !!(selectedDefault.value.filter(col => {
    return col.columnId === item.columnId
  }).length)
}
const checkSelected2 = (item) => {
  return !!(selectedNoUse.value.filter(col => {
    return col.columnId === item.columnId
  }).length)
}
const checkSelected3 = (item) => {
  return !!(selectedFloat.value.filter(col => {
    return col.columnId === item.columnId
  }).length)
}
const checkSelected4 = (item) => {
  return !!(currentPage.value.pageFloatColumns.filter(col => {
    return col.columnId === item.columnId
  }).length)
}
const checkSelected5 = (item) => {
  return !!(currentPage.value.pageDefaultColumns.filter(col => {
    return col.columnId === item.columnId
  }).length)
}
// 左侧数据与中间数据互不相等处理
const checkColumn = (item) => {
  if (!currentPage.value.totalColumnList || !currentPage.value || !currentPage.value.pageDefaultColumns || !currentPage.value.pageFloatColumns) {
    return false
  }
  for (let col of currentPage.value.pageDefaultColumns) {
    if (col.columnId === item.columnId) {
      return true
    }
  }
  for (let col of currentPage.value.pageFloatColumns) {
    if (col.columnId === item.columnId) {
      return true
    }
  }
  return false
}
const setPage = () => {
  getSetting()
  selectedDefault.value = []
  selectedNoUse.value = []
  selectedFloat.value = []
}
const close = () => {
  store.setInfoSettingClose(false)
}
</script>

<style scoped lang="scss">
@mixin arrow-cls {
  content: "";
  border-color: #3A4EAC;
  border-style: solid;
  transform: matrix(0.71, 0.71, -0.71, 0.71, 0, 0);
  position: absolute;
}

.trangle-top::before {
  content: '';
  position: absolute;
  top: -1px;
  border-top: 10px solid transparent;
  border-right: 9px solid transparent;
  border-left: 9px solid transparent;
  border-bottom: 10px solid #ffffff;
}

.trangle-top.border::after {
  content: "";
  position: absolute;
  top: -1px;
  left: 8px;
  border-top: 12px solid transparent;
  border-left: 6px solid transparent;
  border-right: 6px solid transparent;
  border-bottom: 7px solid #3A4EAC;
}

.trangle-bottom::before {
  content: "";
  position: absolute;
  top: 10px;
  border-top: 10px solid #ffffff;
  border-right: 9px solid transparent;
  border-left: 9px solid transparent;
  border-bottom: 10px solid transparent;
}

.trangle-bottom.border::after {
  content: "";
  position: absolute;
  top: 11px;
  left: 7px;
  border-top: 7px solid #3A4EAC;
  border-left: 7px solid transparent;
  border-right: 7px solid transparent;
  border-bottom: 6px solid transparent;
}

.arrow {
  position: relative;
}

.arrow-right {
  &::before {
    @include arrow-cls;
    height: 10px;
    width: 10px;
    border-width: 2px 2px 0 0;
    left: 8px;
  }

  &::after {
    @include arrow-cls;
    height: 8px;
    width: 8px;
    left: 3px;
    border-width: 2px 2px 0 0;
  }

  &.single {
    &::after {
      display: none;
    }

    &::before {
      left: 5px;
    }
  }
}

.arrow-left {
  &::before {
    @include arrow-cls;
    height: 10px;
    width: 10px;
    left: 9px;
    border-width: 0 0 2px 2px;
  }

  &::after {
    @include arrow-cls;
    height: 8px;
    width: 8px;
    left: 16px;
    border-width: 0 0 2px 2px;
  }

  &.single {
    &::after {
      display: none;
    }

    &::before {
      left: 12px;
    }
  }
}

.info-setting,
.transfer {
  height: 100%;
}

.title {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #3385FF;

  .title-item {
    margin: 12px 5px;
    padding: 8px 15px;

    &:hover {
      box-shadow: 0 0 5px darkgray;
    }

    .el-radio__label {
      font-size: 16px !important;
    }
  }
}

.main-content {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  color: #333333;
  height: calc(100% - 100px);
  margin-top: 55px;

  .top_header {
    position: relative;
    width: 300px;

    .header_title {
      position: absolute;
      top: -40px;
      z-index: 0;
      width: 150px;
      height: 55px;
      line-height: 40px;
      text-align: center;
      font-size: 15px;
      font-weight: bold;
      color: #243271;
      background: #E1E6FF;
      border-radius: 10px 10px 0px 0px;
      border: 1px solid #72ABFF;
    }
  }

  .column-button,
  .column-transfer {
    display: flex;
    flex-direction: column;
    height: 100%;
    flex-grow: 0;
    flex-shrink: 0;
  }

  .column-button {
    justify-content: center;
    margin: 0 36px;

    .button-transfer {
      position: relative;
      width: 30px;
      height: 30px;
      margin-bottom: 10px;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #ffffff;
      background-color: #3A4EAC;
      border: 1px solid #3A4EAC;
      cursor: pointer;

      &.arrow {
        background-color: #DFECFF;
      }
    }
  }

  .column-transfer {
    position: relative;
    min-height: 300px;
    border-radius: 10px;
    padding: 20px;
    border: 1px solid #72ABFF;
    background-color: #ffffff;
    overflow-y: auto;

    .col-item {
      padding: 0 10px;
      line-height: 34px;
      word-wrap: break-word;
      word-break: break-all;
      cursor: pointer;
    }

    .col-selected {
      background-color: #E5F2FF;
      color: #3385FF;
    }
  }



}
</style>

<style lang="scss">
.info-setting {
  .el-radio-button+.el-radio-button {
    margin-left: 16px;
  }

  .el-radio-button {
    min-width: 140px;
    height: 40px;
  }

  .el-radio-button:last-child .el-radio-button__inner,
  .el-radio-button:first-child .el-radio-button__inner,
  .el-radio-button__inner {
    min-width: 140px;
    height: 40px;
    color: #526074;
    font-weight: bold;
    line-height: 24px;
    border: 1px solid #3385FF;
    border-bottom: 0;
    border-radius: 10px 10px 0px 0px;
  }

  .el-radio-button.is-active .el-radio-button__inner {
    color: #ffffff;
    background-color: #3385FF;
    border-color: #3385FF;
  }
}
</style>
