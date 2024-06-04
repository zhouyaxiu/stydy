<!-- 长三角数据交换统计情况大屏 -->
<template>
    <div class="bg">
        <div class="container" ref="appRef">
            <div class="content">
                <div class="custom-header">
                    <div class="left"><span>{{ date }}</span><span>{{ week }}</span></div>
                    <div class="right">{{ time }}</div>
                </div>
                <!-- 内容 -->
                <div class="index-content">
                    <!-- 内容--左侧 -->
                    <div class="index-con-left">
                        <!--数据统计-->
                        <div class="card-l1">
                            <statistic ref="statisticPage"></statistic>
                        </div>
                        <!--档案流入流出情况统计-->
                        <div class="card-l2">
                            <div class="card-head"><span>档案流入流出情况统计</span></div>
                            <div class="card-content"></div>
                            <div slot="titleRightPic" class="right-title">
                                <div :class="[inFlowId == 'docInFlow' ? 'right-content right-content-active' : 'right-content']"
                                    @click="clickInFlowBtns('docInFlow')">档案流入</div>
                                <div :class="[inFlowId == 'docOutFlow' ? 'right-content right-content-active' : 'right-content']"
                                    @click="clickInFlowBtns('docOutFlow')">档案流出</div>
                            </div>
                            <inFlow :type="inFlowId" ref="inFlowPage"></inFlow>
                        </div>
                    </div>
                    <!-- 内容--中间 -->
                    <div class="index-con-center">
                        <!-- 地图模式 -->
                        <myMap ref="mapPage" />
                    </div>
                    <!-- 内容--右侧 -->
                    <div class="index-con-right">
                        <!--跨域累计接种情况统计（年龄组/人次数）-->
                        <div class="card-r1">
                            <div class="card-head"><span>跨域累计接种情况统计（年龄组/人次数）</span></div>
                            <div class="card-content"></div>
                            <div slot="titleRightPic" class="right-title inoutFlow">
                                <div :class="[outFlowId == 'js' ? 'right-content right-content-active' : 'right-content']"
                                    @click="clickOutFlowBtns('js')">嘉善</div>
                                <div :class="[outFlowId == 'wj' ? 'right-content right-content-active' : 'right-content']"
                                    @click="clickOutFlowBtns('wj')">吴江</div>
                                <div :class="[outFlowId == 'qp' ? 'right-content right-content-active' : 'right-content']"
                                    @click="clickOutFlowBtns('qp')">青浦</div>
                            </div>
                            <outFlow :type="outFlowId" ref="outFlowPage"></outFlow>
                        </div>
                        <!--跨域累计接种情况统计（疫苗/人次数）-->
                        <div class="card-r2">
                            <div class="card-head"><span>跨域累计接种情况统计（疫苗/人次数）</span></div>
                            <div class="card-content"></div>
                            <div slot="titleRightPic" class="right-title">
                                <div :class="[inoutFlowId == 'immunityPlan' ? 'right-content right-content-active' : 'right-content']"
                                    @click="clickBtns('immunityPlan')">免规</div>
                                <div :class="[inoutFlowId == 'nonImmunityPlan' ? 'right-content right-content-active' : 'right-content']"
                                    @click="clickBtns('nonImmunityPlan')">非免规</div>
                            </div>
                            <inoutFlow :type="inoutFlowId" ref="inoutFlowPage"></inoutFlow>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import { useNow, useDateFormat } from '@vueuse/core'

import statistic from "./left/statistic.vue";
import inFlow from "./left/inFlow.vue";
import outFlow from "./right/outFlow.vue";
import inoutFlow from "./right/inoutFlow.vue";

import myMap from "./middle/map.vue";

const mapPage = ref();

const { now } = useNow({ controls: true })
const time = useDateFormat(now, 'HH : mm : ss')
const date = useDateFormat(now, 'YYYY-MM-DD')
const week = useDateFormat(now, 'dddd')

const statisticPage = ref();
const inFlowPage = ref();
const outFlowPage = ref();
const inoutFlowPage = ref();

let inFlowId = ref('docInFlow');
let outFlowId = ref('js');
let inoutFlowId = ref('immunityPlan');

let l2timer = null
let r1timer = null
let r2timer = null

// 页面初始化
const initData = () => {
    //获取数据
    statisticPage.value.getData();
    inFlowPage.value.getData();
    outFlowPage.value.getData();
    inoutFlowPage.value.getData();
}
// 档案流入流出情况统计按钮
const clickInFlowBtns = (value) => {
    inFlowId.value = value;
}

// 跨域累计接种情况统计（年龄组/人次数）按钮
const clickOutFlowBtns = (value) => {
    outFlowId.value = value;
}
// 跨域累计接种情况统计（疫苗/人次数）
const clickBtns = (value) => {
    inoutFlowId.value = value;
}
// 定时器
const timerFun = () => {
    l2timer = setInterval(() => {
        if (inFlowId.value === 'docInFlow') {
            inFlowId.value = 'docOutFlow'
        } else {
            inFlowId.value = 'docInFlow'
        }
    }, 60000);
    r1timer = setInterval(() => {
        if (outFlowId.value === 'js') {
            outFlowId.value = 'wj'
        } else if (outFlowId.value === 'wj') {
            outFlowId.value = 'qp'
        } else if (outFlowId.value === 'qp') {
            outFlowId.value = 'js'
        }
    }, 20000);
    r2timer = setInterval(() => {
        if (inoutFlowId.value === 'immunityPlan') {
            inoutFlowId.value = 'nonImmunityPlan'
        } else {
            inoutFlowId.value = 'immunityPlan'
        }
    }, 60000);
}

onMounted(() => {
    initData()
    timerFun()
})

onBeforeUnmount(() => {
    clearInterval(l2timer)
    clearInterval(r1timer)
    clearInterval(r2timer)
})

</script>

<style lang="scss" scoped>
.container {
    background: url(@/assets/img/index/bg.png) no-repeat;
    background-size: 100% 100%;
    width: 1920px;
    height: 1080px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    transform-origin: left top;
    overflow: hidden;
}

.content {
    width: 1800px;
    height: 100%;
    margin: 0 auto;
}

.index-content {
    display: flex;
    justify-content: space-between;
    height: calc(100% - 174px);
    margin: 40px 0;

    .index-con-left,
    .index-con-right {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }
}

.card-head {
    position: absolute;
    width: 100%;
    height: 43px;
    font-size: 22px;
    font-weight: bold;
    background: url(@/assets/img/index/card-head-bg.png) no-repeat;
    background-position: bottom;

    span {
        display: block;
        color: transparent;
        background: linear-gradient(to bottom, #BDEAFF, #FFFFFF);
        -webkit-background-clip: text;
        background-clip: text;
        transform: skewX(-10deg);
        padding-left: 45px;
        margin-top: -6px;
    }
}

.card-content {
    position: absolute;
    height: calc(100% - 43px);
    width: 100%;
    top: 43px;
    background: linear-gradient(90deg, rgba(10, 36, 65, 0.7) 0%, rgba(4, 22, 46, 0.7) 100%);
}

.card-l1 {
    background: url(@/assets/img/index/l1.png) no-repeat;
    height: 403px;
    width: 487px;
}

.card-l2 {
    position: relative;
    width: 487px;
    height: 489px;
}

.card-r1 {
    position: relative;
    width: 487px;
    height: 489px;
}

.card-r2 {
    position: relative;
    width: 487px;
    height: 406px;
}

.custom-header {
    width: 100%;
    height: 94px;
    background: url(@/assets/img/index/head.png) no-repeat;
    background-position: left bottom;
    background-size: cover;
    display: flex;
    align-items: center;
    justify-content: space-between;
    color: #ffffff;

    .left {
        font-size: 22px;

        span {
            font-weight: 400
        }

        span+span {
            margin-left: 22px;
        }
    }

    .right {
        font-size: 26px;
        font-weight: 800
    }
}

.right-title {
    display: flex;
    color: #FFFFFF;
    z-index: 100;
    margin-top: 60px;
    position: absolute;
    margin-left: 50%;
    left: -100px;

    .right-content {
        font-size: 14px;
        width: 100px;
        height: 16px;
        line-height: 16px;
        text-align: center;
        cursor: pointer;
        font-style: italic;
        opacity: 0.3;
    }

    .right-content-active {
        background: linear-gradient(90deg, #08203B 0%, #18668D 51%, #08203D 100%);
        opacity: 1;
    }
}

.right-title.inoutFlow {
    left: -150px;
}
</style>