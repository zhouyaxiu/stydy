<!-- 流入情况统计 -->
<template>
  <div class="in-flow">
    <div ref="inFlow" id="inFlow"></div>
    <img :src="proxy.$util.getImageUrl('index/l2-bg.png')">
    <div class="con">
      <span>{{ total }}</span>
      <span>{{props.type==='docInFlow'?'流入人数':'流出人数'}}</span>
    </div>
  </div>
</template>
<script setup>
import { ref, getCurrentInstance, watch,computed } from "vue";
import l2label from '@/assets/img/index/l2-label.png';
import l2labelpoint from '@/assets/img/index/l2-labelpoint.png';
import l2labeltop from '@/assets/img/index/l2-label-top.png';
import { recordFlowQuery } from "@/api/apis/index";
const inFlow = ref();
const { proxy } = getCurrentInstance();
const props = defineProps({
  type: {
    type: String,
    default: 'docInFlow'
  }
})
const total = ref(0)
const data = ref([])

// 监听事件
watch(() => props.type, (newVal, oldVal) => {
  if (newVal != oldVal) {
    getData()
  }
}, { deep: true })

const getData = async () => {
  recordFlowQuery({type:props.type==='docInFlow'?'0':'1'}).then(res => {
    if (res.data.code===200) {
      let resdata=res.data.data.data
      if(window.isMockData){
        // if(resdata.length<=2){
          if(props.type==='docInFlow'){
            window.recordFlowData0=JSON.parse(sessionStorage.getItem("docInFlowtemp0"))
            resdata=window.recordFlowData0
          }else{
            window.recordFlowData1=JSON.parse(sessionStorage.getItem("docInFlowtemp1"))
            resdata=window.recordFlowData1
          }
        // }
      }
      total.value = resdata.reduce((sum,item)=>sum+Number(item.value),0)
      // total.value = resdata[resdata.length-1].value
      // resdata.pop()
      data.value = resdata
      totalPercent()
      draw()
    }
  });
}

// 计算当前模块所处的占比位置---及鼠标移入时引导线的位置，确定数据背景图展示
const totalPercent = () =>{
  let percentSum = 0
  data.value.forEach(item=> {
    let percent = item.value/total.value*100
    item.percentSumHalf = percentSum + percent/2
    percentSum += percent
  })
}
const draw = async () => {
  let option = {
    // 不能删
    tooltip: {
      formatter: function (names){
      }
    },
    legend: {
      type: 'scroll',
      orient: 'vertical',
      pageIconColor: '#04152c',
      pageIconInactiveColor: '#04152c',
      pageTextStyle:{
        color: '#04152c'
      },
      itemHeight: 10,
      itemWidth: 10,
      textStyle: { fontSize: 14, color: '#FFF' },
      right: 20,
      top: 140,
      bottom: 30,
      height: 310,
      itemGap: 20,
      itemStyle:{
        opacity: 1,
        lineHeight: 20
      },
      formatter: function (names) {
        return [
          "{a|" +
          proxy.$echarts.format.truncateText(
            names,
            80,
            "…"//如果宽度超过会出现...
          ) +
          "}"
        ].join(" ");
      },
      tooltip: {
        show: true//这个为true的话悬停到legend上会出现tooltip
      },
      // 不能删
      textStyle: {
        rich: {
          a: {
            color: "white",
            width: 80,
            align: "left"
          },
        }
      },
    },
    series: [
    {
        name: '透明层',
        type: 'pie',
        radius: ['36%', '56%'],
        center: ["40%", "60%"],//中心点
        padAngle: 3,
        minAngle: 6,
        zlevel: 4,
        selectedMode: 'single',
        avoidLabelOverlap: false,
        itemStyle: {
          opacity: 0,
        },
        label: {
          show: false,
          position: "outer",
          distanceToLabelLine: 0,
          formatter: params => {
            if(params.data.percentSumHalf<=50){
              return `{hr|}{valueStyle|${params.data.value}人}`;
            } else if(59.5<params.data.percentSumHalf && params.data.percentSumHalf<87.5){
              return `{valueStyleTop|${params.data.value}人}`;
            }else{
              return `{valueStyle|${params.data.value}人}{hr|}`;
            }
          },
          color: "#d0aa34",
          lineHeight: 41,
          fontSize: 14,
          rich: {
            hr: {
              height: 15,
              width: 15,
              backgroundColor: {
                image: l2labelpoint //注意路径带不带/
              },
            },
            // 数字样式
            valueStyle: {
              color: '#c09e37',
              fontSize: 16,
              height: 41,
              align: 'center',
              verticalAlign: 'middle',
              backgroundColor: {
                image: l2label, //注意路径带不带/
              },
            },
            valueStyleTop: {
              color: '#c09e37',
              fontSize: 16,
              height: 47,
              align: 'center',
              verticalAlign: 'middle',
              backgroundColor: {
                image: l2labeltop, //注意路径带不带/
              },
            },
          }
        },
        labelLayout: params=>{
          if(params.labelLinePoints[0][0]<120){
            return{
              labelLinePoints:[
                params.labelLinePoints[0],
                [params.labelLinePoints[1][0],params.labelLinePoints[0][1]],
                [params.labelLinePoints[2][0],params.labelLinePoints[0][1] - 40],
            ],
              x: params.labelLinePoints[2][0] + 46,
              y: params.labelLinePoints[0][1] - 40
            }
          }
        },
        emphasis: {
          scale: false,
          label: {
            show: true,
            opacity: 1,
          },
        },
        labelLine: {
          lineStyle: {
            length: 1,
            length2: 1,
            width: 1,
            color: "#4ef5ff",
            type: "solid",
          },
        },
        data: data.value
      },
      {
        name: '外环',
        type: 'pie',
        radius: ['36%', '56%'],
        center: ["40%", "60%"],//中心点
        padAngle: 3,
        minAngle: 6,
        zlevel: 2,
        selectedMode: 'single',
        avoidLabelOverlap: false,
        itemStyle: {
          opacity: 0.3,
        },
        label: {
          show: false,
        },
        emphasis: {
          scale: false,
          label: {
            show: false,
          },
        },
        data: data.value
      },
      {
        name: '内环',
        type: 'pie',
        selectedMode: 'single',
        avoidLabelOverlap: false,
        radius: ['52%', '56%'],
        center: ["40%", "60%"],//中心点
        zlevel: 1,
        padAngle: 3,
        minAngle: 6,
        label: {
          show: false,
          position: 'outside'
        },
        emphasis: {
          scale: false,
          label: {
            show: false
          }
        },
        data: data.value
      },
      {
        name: '中间背景',
        type: 'pie',
        avoidLabelOverlap: false,
        radius: ['48%', '52%'],
        center: ["40%", "60%"],//中心点
        zlevel: 3,
        minAngle: 6,
        label: {
          show: false,
          position: 'outside'
        },
        emphasis: {
          scale: false,
          label: {
            show: false
          }
        },
        itemStyle: {
          color: '#061932'
        },
        data: data.value
      }
    ]
  };
  proxy.$echarts.dispose(document.getElementById('inFlow'))
  let inFlowChart = proxy.$echarts.init(document.getElementById('inFlow'), null, { width: 'auto', height: 480 });
  inFlowChart.clear();
  inFlowChart.setOption(option, true);

  const legend = option.series[0].data
  let index = 0 // 初始位置为0，legend翻页数为2
  inFlowChart.getZr().on('mousewheel', function ({ wheelDelta }) {
    index = index - wheelDelta
    index = Math.max(0, Math.min(Math.floor(legend.length / 2) - 1, index))
    // 阻止默认滚动事件
    event.preventDefault()
    inFlowChart.dispatchAction({
      type: 'legendScroll',
      scrollDataIndex: index * 2
    })
  })
}
defineExpose({
  getData
})

</script>
<style lang="scss" scoped>
.in-flow {
  position: relative;
  height: 100%;

  .con {
    position: absolute;
    width: 112px;
    top: 50%;
    display: flex;
    flex-direction: column;
    align-items: center;
    color: #FFFFFF;
    left: 50%;
    margin-left: -100px;

    span:first-child {
      font-size: 40px;
    }
  }

  img {
    position: absolute;
    width: 334px;
    height: 334px;
    top: 122px;
    left: 28px;
    z-index: 0;

  }
}

#inFlow {
  position: relative;
  z-index: 1;
}
</style>