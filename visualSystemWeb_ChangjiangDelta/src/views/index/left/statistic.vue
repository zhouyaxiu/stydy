<!-- 数据统计 -->
<template>
  <div class="shot-monitor">
    <div class="monitor">
      <div class="icon-sum"></div>
      <div class="right-content">
        <div class="line-bg"><span>总交换量</span></div>
        <div class="line-text">
          <span class="time-num">
            <vue-number-roll-plus :number="flowChange" background="transparent" :isSemicolon="true" class="number"></vue-number-roll-plus>
          </span>
          <span>次</span>
        </div>
      </div>
    </div>
    <div class="monitor">
      <div class="icon-inflow"></div>
      <div class="right-content">
        <div class="line-bg"><span>流入</span></div>
        <div class="line-text">
          <span class="time-num">
            <vue-number-roll-plus :number="recordInflow" background="transparent" :isSemicolon="true" class="number"></vue-number-roll-plus>
          </span>
          <span>次</span>
        </div>
      </div>
    </div>
    <div class="monitor">
      <div class="icon-outflow"></div>
      <div class="right-content">
        <div class="line-bg"><span>流出</span></div>
        <div class="line-text">
          <span class="time-num">
            <vue-number-roll-plus :number="recordOutflow" background="transparent" :isSemicolon="true" class="number"></vue-number-roll-plus>
          </span>
          <span>次</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from "vue";
import VueNumberRollPlus from "vue3-number-roll-plus"
import "vue3-number-roll-plus/main.css"
import { upperLeftQuery } from "@/api/apis/index";
// 流出
let recordOutflow = ref(0);
// 流入
let recordInflow = ref(0);
// 总交换量
let flowChange = ref(0);

const getData = async () => {
  upperLeftQuery().then(res => {
    if (res.data.code===200) {
      recordOutflow.value = res.data.data.data.recordOutflow
      recordInflow.value = res.data.data.data.recordInflow
      flowChange.value = res.data.data.data.flowChange
    }
  });
}
defineExpose({
  getData
})
</script>
<style lang="scss" scoped>
.shot-monitor {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  height: 100%;
  padding: 20px 0;
  color: #ffffff;


  .monitor {
    display: flex;
    justify-content: center;
    align-items: center;
    @mixin icon-box{
      width: 66px;
      height: 80px;
      margin-right: 10px;
      background-position: left top;
      background-size: contain;
    }
    .icon-sum {
      background: url(@/assets/img/index/icon-sum.png) no-repeat;
      @include icon-box;
    }

    .icon-inflow {
      background: url(@/assets/img/index/icon-inflow.png) no-repeat;
      @include icon-box;
    }

    .icon-outflow {
      background: url(@/assets/img/index/icon-outflow.png) no-repeat;
      @include icon-box;
    }

    .right-content {
      display: flex;
      flex-direction: column;
      justify-content: center;
    }

    .line-bg {
      background: linear-gradient(90deg, #081C35 0%, #113258 50%, #061933 100%);
      width: 289px;
      height: 23px;
      font-weight: bold;
      span{
        display: block;
        color: transparent;
        background: linear-gradient(to bottom, #BDEAFF, #FFFFFF);
        -webkit-background-clip: text;
        background-clip: text;
        transform: skewX(-10deg);
      }
    }

    .line-text {
      font-size: 12px;
      color: #fff;
      display: flex;
      align-items: center;
      span:last-child {
        margin-bottom: -18px;
      }
    }

    .time-num {
      color: #F7C42A;
      font-size: 34px;
      margin: 8px 8px 0 0;
      transform: scale(1.1);
    }
    .time-num.white{
      color: #ffffff;
    }
  }
}
.real-time-num-item div{
	display: inline-block;
}
.real-time-num{
	width: min-content !important;
	margin-left: 0 !important;
}
</style>