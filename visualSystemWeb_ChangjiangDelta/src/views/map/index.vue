<!-- 受种者长三角接种轨迹大屏 -->
<template>
  <div class="bg">
    <div class="container" ref="appRef">
      <div class="header">
        <div class="time">
          <div class="time-left">{{ time }}</div>
          <div class="time-right">
            <span class="week">{{ week }}</span>
            <span class="date">{{ date }}</span>
          </div>
        </div>
        <div class="search">
          <div class="item">
            <div class="label">疫苗追溯码</div>
            <input type="text" v-model.trim="imuno" @keyup.enter.native="search()">
          </div>
          <div class="item">
            <div class="label">身份证号</div>
            <input type="text" :style="{border: cidBorder}" v-model.trim="cid" @keyup.enter.native="search()">
          </div>
          <div class="item">
            <div class="label">受种者编码</div>
            <input type="text" :style="{border: fchildnoBorder}" v-model.trim="fchildno" @keyup.enter.native="search()">
          </div>
          <div class="item">
            <el-button @click="search()">查询</el-button>
          </div>
        </div>
      </div>
      <div class="echart">
        <my-map ref="myMapPage" :mapInfo="mapInfo" @click="clickBg()"></my-map>
      </div>
      <div class="outerBox" ref="scrollRef">
          <template v-for="(item, index) in data"  :key="(index)">
            <div :class="[index != currentBlock && currentBlock !== null ? 'opacity' : '', isAction ? 'action text': '']"
              @click.stop="clickBlock(index,item.supName)">
              <div v-if="item.stationType === '0'" :class="[item.showArea ? 'visible' : 'hidden', 'icon icon-js']">浙江嘉善</div>
              <div v-if="item.stationType === '1'" :class="[item.showArea ? 'visible' : 'hidden', 'icon icon-wj']">江苏吴江</div>
              <div v-if="item.stationType === '2'" :class="[item.showArea ? 'visible' : 'hidden', 'icon icon-qp']">上海青浦</div>
              <div v-if="item.stationType === null" :class="['hidden', 'icon icon-qp']">上海青浦</div>
              <img class="point" :src="proxy.$util.getImageUrl('map/point.png')" />
              <div class="text-right">
                <div class="top">
                  <span class="date">{{ item.date }}</span>
                  <span class="addr" :title="item.stationName">{{ item.stationName }}</span>
                </div>
                <span class="ym">{{ item.vaccineName }}</span>
              </div>
            </div>
            <div :class="[index != currentBlock && currentBlock !== null ? 'opacity' : '',isAction ? 'action line': '']"></div>
          </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, getCurrentInstance, reactive,TransitionGroup} from "vue";
import { useNow, useDateFormat } from '@vueuse/core'
import myMap from "@/views/map/myMap.vue";
import { queryAreaTrail } from "@/api/apis/index";
const { proxy } = getCurrentInstance();
const { now } = useNow({ controls: true })

const time = useDateFormat(now, 'HH:mm:ss')
const date = useDateFormat(now, 'YYYY/MM/DD')
const week = useDateFormat(now, 'dddd')

const currentBlock = ref(null)
const myMapPage = ref(null)
const originLineData = ref([])
const lineData = ref([])
const data = ref([])
const imuno = ref('')
const fchildno = ref('')
const cid = ref('')
// 用于动画重新执行
const  isAction = ref(false) 
const scrollRef = ref(null)

let timer = null

// 身份证正则
let  p = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;

let mapInfo = reactive({
  lineData: [],
  allPointData: [],
  clickIndex: null,
  clickStreet: null,
});

const cidBorder = ref('1px solid #3B879A')
const fchildnoBorder = ref('1px solid #3B879A')

// 点击查询按钮
const search = () => {
  fchildnoBorder.value = '1px solid #3B879A'
  cidBorder.value = '1px solid #3B879A'

  if(!imuno.value && !fchildno.value && !cid.value){
    return
  }
  // 身份证号码和受种者编码格式不正确边框变红
  if(fchildno.value && fchildno.value.length !== 18) {
    fchildnoBorder.value = '1px solid #994060'
  }
  if(cid.value && !(p.test(cid.value))) {
    cidBorder.value = '1px solid #994060'
  }
  let params = {
    imuno: imuno.value,
    fchildno: fchildno.value,
    cid: cid.value
  }
  isAction.value = false
  queryAreaTrail(params).then(res => {
    if (res.data.code===200) {
      // res.data.data.data[0].vaccineName = "建档"
      let newData = res.data.data.data
      let preArea = ''
      newData.forEach((item, index) => {
        // 是否展示轨迹块左侧的区域标识
        if (item.stationType !== preArea && item.stationType !== null) {
          item.showArea = true
          preArea = item.stationType
        } else {
          item.showArea = false
        }
      })
      isAction.value = true
      data.value = newData

      mapInfo.allPointData = []
      mapInfo.lineData = []
      mapInfo.clickIndex = null
      currentBlock.value = null
      lineDataFun()
      handlePointGroup()
      myMapPage.value.drawMap();
    }else {
      data.value = []
      mapInfo.allPointData = []
      mapInfo.lineData = []
      mapInfo.clickIndex = null
      currentBlock.value = null
      lineDataFun()
      handlePointGroup()
      myMapPage.value.drawMap();

      // myMapPage.value.clearOption();
    }
  });
  clearInterval(timer)
  scrollRef.value.scrollTop = 0
  // scrollInit(scrollRef.value)
  // useMouse.value = false
  setTimeout(() => {
    // useMouse.value = true
    scrollBottom(scrollRef.value)
  }, 5000);
}

// 处理定位点分组
const handlePointGroup = () => {
  mapInfo.allPointData = data.value
}

// 处理坐标生成轨迹数组
const lineDataFun = () => {
  originLineData.value = []
  lineData.value = []
  let arr = []
  data.value.forEach((item, index) => {
    if(item.value!==null){
      if (arr.length === 2) {
      originLineData.value.push({ coords: arr })
      let arr2 = []
      arr2.push(arr[1])
      arr = []
      arr.push(arr2[0])
      arr.push(item.value)
    } else {
      arr.push(item.value)
    }
    if (index === data.value.length - 1) {
      originLineData.value.push({ coords: arr })
    }
    }
  })
  mapInfo.lineData = originLineData.value
}

// 点击左侧特定轨迹块：左侧其他模块透明度降低，右侧地图特定板块高亮
const clickBlock = (index,street) => {
  currentBlock.value = index
  mapInfo.clickIndex = index
  mapInfo.clickStreet = street
}

// 点击空白区域时展示所有轨迹
const clickBg = () => {
  currentBlock.value = null
  mapInfo.clickStreet = null
  mapInfo.clickIndex = data.value.length
}
// 自动向下滑动
const scrollBottom= (scrollContainer, speed = 20, step = 10) => {
  let scrollBtmNum = scrollContainer.scrollTop
  timer = setInterval(() => {
    scrollBtmNum += step
    if (scrollBtmNum >= scrollContainer.scrollHeight) {
      clearInterval(timer)
      // 可循环滚动
      // scrollRef.value.scrollLeft = 0
      // setTimeout(() => {
      //   scrollLeft(scrollRef.value)
      // }, 1000);
    } else {
      scrollContainer.scrollTop = scrollBtmNum;
    }
  }, speed);
}

</script>

<style scoped lang="scss">
.container {
  background: url(@/assets/img/map/map-bg.png) no-repeat;
  background-position: left top;
  background-size: cover;
}

.header {
  background: url(@/assets/img/map/head.png) no-repeat;
}

.echart {
  position: relative;
  width: 85%;
  height: 90%;
  margin-left: 450px;
}

.visibe {
  visibility: visible;
}

.hidden {
  visibility: hidden;
}

.opacity {
  opacity: 0.3;
}

.outerBox {
  position: absolute;
  width: 600px;
  height: 80%;
  overflow: auto;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  flex-wrap: nowrap;
  user-select: none;
  left: 40px;
  top: 190px;
  font-size: 16px;
  color: #ffffff;
  scrollbar-width: none;

  .icon {
    width: 106px;
    height: 34px;
    line-height: 34px;
    text-align: center;
  }

  .icon-js {
    background: url(@/assets/img/map/js-bg.png) no-repeat;
    background-position: left top;
    background-size: cover;
  }

  .icon-wj {
    background: url(@/assets/img/map/wj-bg.png) no-repeat;
    background-position: left top;
    background-size: cover;
  }

  .icon-qp {
    background: url(@/assets/img/map/qp-bg.png) no-repeat;
    background-position: left top;
    background-size: cover;
  }

  .point {
    margin: 0 24px 0 10px
  }

  .text-right {
    width: 378px;
    height: 102px;
    background: url(@/assets/img/map/block-bg.png) no-repeat;
    background-position: left top;
    background-size: cover;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    padding-left: 20px;
  }

  .text {
    display: flex;
    align-items: center;

    .top {
      display: flex;
    }

    .ym {
      font-size: 20px;
      font-weight: bold;
      color: #59FFFF;
    }

    .addr {
      width: 200px;
      margin-left: 24px;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
      text-align: left;
    }
  }
}

::-webkit-scrollbar {
  display: none;
}

.line {
  display: inline-block;
  width: 6px;
  height: 160px;
  background: #16B6B2;
  margin: -35px 0 -37px 127px;
}

.action {
  animation: showIn 0.5s linear;
  flex: none
}

.action:nth-child(1) {
  animation-duration: 0.7s;
}

.action:nth-child(2) {
  animation-duration: 1.4s
}

.action:nth-child(3) {
  animation-duration: 2.1s
}

.action:nth-child(4) {
  animation-duration: 2.8s
}

.action:nth-child(5) {
  animation-duration: 3.5s
}

.action:nth-child(6) {
  animation-duration: 4.2s
}

.action:nth-child(7) {
  animation-duration: 4.9s
}

.action:nth-child(8) {
  animation-duration: 5.6s
}

.action:nth-child(9) {
  animation-duration: 6.3s
}

.action:nth-child(10) {
  animation-duration: 7s
}

.action:last-child {
  display: none
}

@keyframes showIn {

  0%,
  70% {
    opacity: 0;
  }

  100% {
    opacity: 1;
  }
}
</style>