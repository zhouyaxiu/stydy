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
            <input type="text" v-model.trim="imuno">
          </div>
          <div class="item">
            <div class="label">身份证号</div>
            <input type="text" v-model.trim="cid">
          </div>
          <div class="item">
            <div class="label">受种者编码</div>
            <input type="text" v-model.trim="fchildno">
          </div>
          <div class="item">
            <el-button @click="search()">查询</el-button>
          </div>
        </div>
      </div>
      <div class="echart">
        <canvasMap ref="myMapPage" :mapInfo="mapInfo" @click="clickBg()"></canvasMap>
      </div>
      <div class="outerBox">
        <template v-for="(item, index) in data">
          <div :class="['text action', index != currentBlock && currentBlock !== null ? 'opacity' : '']"
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
          <div :class="['line action', index != currentBlock && currentBlock !== null ? 'opacity' : '']"></div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, getCurrentInstance, reactive } from "vue";
import { useNow, useDateFormat } from '@vueuse/core'
import canvasMap from "@/views/canvas/canvasMap.vue";
import { dataList } from "./regionData.js";
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

let mapInfo = reactive({
  lineData: [],
  clickIndex: null,
  clickStreet: null,
});

// 点击查询按钮
const search = () => {
  // if(!imuno.value && !fchildno.value && !cid.value){
  //   return
  // }
  let params = {
    imuno: imuno.value,
    fchildno: fchildno.value,
    cid: cid.value
  }

let cs = [
  {
    date: "2012年03月20日",
    fchildNo: "330421310420120926",
    stationCode: "330421031001",
    stationLocation: "120.895718,30.848702",
    stationName: "魏塘街道社区卫生服务中心",
    stationType: "0",
    supCode: "330421310000",
    supName: "魏塘街道",
    vaccineName: '建档',
  },
  {
    date: "2012年03月20日",
    fchildNo: "330421310420120926",
    stationCode: "330421031001",
    stationLocation: "120.895718,30.848702",
    stationName: "干窑镇社区卫生服务中心",
    stationType: "0",
    supCode: "330421310000",
    supName: "干窑镇",
    vaccineName: '百白破疫苗（无细胞）',
  },
  {
    date: "2012年03月20日",
    fchildNo: "330421310420120926",
    stationCode: "330421031001",
    stationLocation: "120.895718,30.848702",
    stationName: "金泽镇社区卫生服务中心",
    stationType: "0",
    supCode: "330421310000",
    supName: "金泽镇",
    vaccineName: '百白破疫苗（无细胞）',
  },
  {
    date: "2012年03月20日",
    fchildNo: "330421310420120926",
    stationCode: "330421031001",
    stationLocation: "120.895718,30.848702",
    stationName: "朱家角社区卫生服务中心",
    stationType: "0",
    supCode: "330421310000",
    supName: "朱家角",
    vaccineName: '百白破疫苗（无细胞）',
  }
]

  // queryAreaTrail(params).then(res => {
  //   if (res.data.code===200) {
      // res.data.data.data[0].vaccineName = "建档"
      // let newData = res.data.data.data
      let newData = cs
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
      data.value = newData

      mapInfo.lineData = []
      mapInfo.clickIndex = null
      currentBlock.value = null
      lineDataFun()
  //   }
  // });
}

// 处理坐标生成轨迹数组
const lineDataFun = () => {
  originLineData.value = []
  lineData.value = []
  data.value.forEach(item1 =>
    dataList.forEach(item2 => {
      if (item2.name === item1.supName) {
        originLineData.value.push(item2)
      }
    })
  );
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
  margin: 90px 20px 0 450px;
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