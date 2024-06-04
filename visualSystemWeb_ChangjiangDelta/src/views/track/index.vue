<!-- 受种者长三角接种轨迹回溯 -->
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
      <div class="main">
        <div class="legend">
          <div class="item">
            <img class="icon" :src="proxy.$util.getImageUrl('common/js.png')" />
            <div class="text">浙江嘉善</div>
          </div>
          <div class="item">
            <img class="icon" :src="proxy.$util.getImageUrl('common/wj.png')" />
            <div class="text">江苏吴江</div>
          </div>
          <div class="item">
            <img class="icon" :src="proxy.$util.getImageUrl('common/qp.png')" />
            <div class="text">上海青浦</div>
          </div>
        </div>
        <div class="outerBox" ref="scrollRef">
          <template v-for="(item,index) in data" :key="index">
            <div :class="[isAction? 'action text' : '']">
              <img v-if="item.stationType === '0'" class="icon" :src="proxy.$util.getImageUrl('common/js.png')" />
              <img v-if="item.stationType === '1'" class="icon" :src="proxy.$util.getImageUrl('common/wj.png')" />
              <img v-if="item.stationType === '2'" class="icon" :src="proxy.$util.getImageUrl('common/qp.png')" />
              <img v-if="item.stationType === null" class="icon" :src="proxy.$util.getImageUrl('common/qp.png')" style="opacity: 0;" />
              <img class="point" :src="proxy.$util.getImageUrl('track/point.png')" />
              <span class="ym" :title="item.vaccineName">{{ item.vaccineName }}</span>
              <span class="date">{{ item.date }}</span>
              <span class="addr" :title="item.stationName">{{ item.stationName }}</span>
            </div>
            <div :class="[isAction ? 'action line' : '']"></div>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance, reactive } from "vue";
import { useNow, useDateFormat } from '@vueuse/core'
import { queryTimeAxis } from "@/api/apis/index";

const { proxy } = getCurrentInstance();

// 时钟
const { now } = useNow({controls: true})
const time = useDateFormat(now, 'HH:mm:ss')
const date = useDateFormat(now, 'YYYY/MM/DD')
const week = useDateFormat(now, 'dddd')

const data = ref([])
const scrollRef = ref(null)
const imuno = ref('')
const fchildno = ref('')
const cid = ref('')
// 用于查询后动画重新执行
const isAction = ref(false)

// animation动画效果展示完成后，可以使用鼠标移动
const useMouse = ref(false)
let timer = null

const cidBorder = ref('1px solid #3B879A')
const fchildnoBorder = ref('1px solid #3B879A')
// 身份证正则
let p = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
// 左右滑动
const scrollInit = (nav) => {
  let flag; // 鼠标按下
  let downX; // 鼠标点击的x下标
  let scrollLeft; // 当前元素滚动条的偏移量
  nav.addEventListener("mousedown", function (event) {
    flag = true;
    clearInterval(timer)
    downX = event.clientX; // 获取到点击的x下标
    scrollLeft = nav.scrollLeft; // 获取当前元素滚动条的偏移量
  });
  nav.addEventListener("mousemove", function (event) {
    if (flag && useMouse.value) { // 判断是否是鼠标按下滚动元素区域
      let moveX = event.clientX; // 获取移动的x轴
      let scrollX = moveX - downX; // 当前移动的x轴下标减去刚点击下去的x轴下标得到鼠标滑动距离
      nav.scrollLeft = scrollLeft - scrollX // 鼠标按下的滚动条偏移量减去当前鼠标的滑动距离
    }
  });
  // 鼠标抬起停止拖动
  nav.addEventListener("mouseup", function () {
    flag = false;
  });
  // 鼠标离开元素停止拖动
  nav.addEventListener("mouseleave", function (event) {
    flag = false;
  });
}

// 鼠标滚轮事件处理
const mouseHandle = (event,nav) => {
  if (useMouse.value) {
      clearInterval(timer)
      let scrollLeft = nav.scrollLeft
      let delta = Math.max(-1, Math.min(1, (event.wheelDelta || -event.detail)));
      // 在此处理滚轮事件
      if (delta > 0) {
        // 向上滚动
        if(scrollLeft > 0){
          nav.scrollLeft = scrollLeft - 50
        }
      } else if (delta < 0) {
        if(scrollLeft < nav.scrollWidth){
          nav.scrollLeft = scrollLeft + 50
        }
      }
    }
}

// 自动向右滑动
const scrollLeft = (scrollContainer, speed = 10, step = 10) => {
  let scrollLeftNum = scrollContainer.scrollLeft
  timer = setInterval(() => {
    scrollLeftNum += speed
    if (scrollLeftNum >= scrollContainer.scrollWidth) {
      clearInterval(timer)
      // 可循环滚动
      // scrollRef.value.scrollLeft = 0
      // setTimeout(() => {
      //   scrollLeft(scrollRef.value)
      // }, 1000);
    } else {
      scrollContainer.scrollLeft = scrollLeftNum;
    }
  }, step);
}

// 点击搜索按钮
const search = () =>{
  fchildnoBorder.value = '1px solid #3B879A'
  cidBorder.value = '1px solid #3B879A'
  if(!imuno.value && !fchildno.value && !cid.value){
    return
  }
  isAction.value = false
  data.value = []
  
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
  queryTimeAxis(params).then(res => {
    if (res.data.code===200) {
      // res.data.data.data[0].vaccineName = "建档"
      data.value = res.data.data.data
      isAction.value = true
    }else {
      data.value = []
    }
  });
  clearInterval(timer)
  scrollRef.value.scrollLeft = 0
  scrollInit(scrollRef.value)
  useMouse.value = false
  setTimeout(() => {
    useMouse.value = true
    scrollLeft(scrollRef.value)
  }, 8000);
}

onMounted(() => {
  // 浏览器滚轮事件
  window.addEventListener('mousewheel', (event) => mouseHandle(event,scrollRef.value));
  // 火狐浏览器滚轮事件
  window.addEventListener("DOMMouseScroll", (event) => mouseHandle(event,scrollRef.value));
})
</script>
<style scoped lang="scss">


.container {
  background: url(@/assets/img/track/bg.png) no-repeat;
  background-position: left top;
  background-size: cover;

  .main {
    width: 100%;
    height: calc(100% - 200px);
    padding-top: 60px;
    background: url(/src/assets/img/track/bg-line.png) no-repeat;
    background-position: center bottom;
    background-size: auto;

    .legend {
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 22px;

      .item+.item {
        margin-left: 60px;
      }

      .item {
        display: flex;
        align-items: center;

        img {
          height: 48px;
          margin-right: 10px;
        }
      }
    }
  }
}

.outerBox {
  position: relative;
  width: 80%;
  margin: 0 auto;
  overflow: auto;
  cursor: pointer;
  display: flex;
  align-items: center;
  flex-wrap: nowrap;
  margin-top: 160px;
  user-select: none;
  scrollbar-width: none;
  .point{
    margin: 15px 0
  }
  .text{
    display: flex;
    align-items: center;
    flex-direction: column;
    font-size: 22px;
    width: 330px;
    .ym{
      font-size: 30px;
      min-height: 44px;
      font-weight: bold;
      color: #59FFFF;
      width: 290px;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
      text-align: center;
    }
    .addr{
      width: 290px;
      min-height: 32px;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
      text-align: center;
    }
  }
}

::-webkit-scrollbar {
  display: none;
}

.line {
  display: inline-block;
  height: 3px;
  width: 260px;
  background: #65ECED;
  margin: 0 -143px;
}

.action {
  animation: showIn 1s linear;
  flex: none
}

.action:nth-child(1) {
  animation-duration: 1s;
}

.action:nth-child(2) {
  animation-duration: 2s
}

.action:nth-child(3) {
  animation-duration: 3s
}

.action:nth-child(4) {
  animation-duration: 4s
}

.action:nth-child(5) {
  animation-duration: 5s
}

.action:nth-child(6) {
  animation-duration: 6s
}

.action:nth-child(7) {
  animation-duration: 7s
}

.action:nth-child(8) {
  animation-duration: 8s
}

.action:nth-child(9) {
  animation-duration: 9s
}

.action:nth-child(10) {
  animation-duration: 10s
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
