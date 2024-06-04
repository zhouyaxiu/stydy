<!-- 跨域累计接种情况统计（年龄组/人次数） -->
<template>
  <div class="out-flow">
    <div ref="outFlow" id="outFlow"></div>
  </div>
</template>
<script setup>
import { ref, getCurrentInstance, watch } from "vue";
import { ageQuery } from "@/api/apis/index";
const outFlow = ref();
const data = ref([]);
const { proxy } = getCurrentInstance();
const props = defineProps({
  type: {
    type: String,
    default: 'js'
  },
})
// 监听事件
watch(() => props.type, (newVal, oldVal) => {
  if (newVal != oldVal) {
    getData()
  }
}, { deep: true })

const getData = async () => {
  let type = ''
  switch (props.type) {
    case 'js':
      type = '1'
      break;
    case 'wj':
      type = '2'
      break;
    case 'qp':
      type = '3'
      break;
    default:
      break;
  }
  ageQuery({type:type}).then(res => {
    if (res.data.code===200) {
      data.value = res.data.data.data
      draw()
    }
  });
}

const draw = async () => {
  let option = {
    tooltip: {
      trigger: 'axis',
      position: function (point, params, dom, rect, size) {
        // 固定位置：tooltip 始终显示在柱状图上方
        return {
          left: point[0] - size.contentSize[0] / 2,
          top: point[1] - size.contentSize[1] - 10
        };
      },
      axisPointer: {
        type: 'none',
      },
      backgroundColor: '#0e3f63',
      formatter: params => {
        let str = `
              <div style="font-size:12px">
                <span style='color:#FFFB00'>
                ${params[1].data}
                </span>
                <span style='color:#FFFFFF'>
                /${params[0].data}
                </span>
                <span style='display:none;background-color:#FFFFFF;color:#07213E;border-radius:4px;margin-left: 4px;padding:0 4px'>
                ${params[2].data}%
                </span>
              </div>
            `;
        return str;
      },
    },
    grid: {
      left: 60,
      right: 60,
    },
    legend: {
      data: [
        {name: '接种总数',icon: 'image://data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAGUlEQVQoU2P8//+/MQMRgHFUIb5Qon7wAADcEh/rQtrWjAAAAABJRU5ErkJggg=='},
        {name: '接种次数',icon: 'image://data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAGUlEQVQoU2NMPPPgPwMRgHFUIb5Qon7wAACM0R6DREuMbgAAAABJRU5ErkJggg=='},
        {name: '接种次数/接种总数',icon: 'image://data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAALCAYAAACprHcmAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAIGNIUk0AAHolAACAgwAA+f8AAIDpAAB1MAAA6mAAADqYAAAXb5JfxUYAAAB7SURBVHjatJGxDoNADEPzgyQshX74wRdAffvhW5BIt/baBqkdasnbi+XI4u7yreUnuFWFXgibmG0nNJWlGyRSWbqBsIMwb3yUVa8f8AZNb6AT5tvN5gcUAWdujuJkokl+PteHnWsOOouIlNVGwmZCd0JTzTa+AH8b5T4A4eQX3T8btbwAAAAASUVORK5CYII='},
      ],
      x: 'center', // 图例水平居中
      y: 'bottom',
      textStyle: {
        fontSize: 12,
        color: '#bdc1c8',
      },
      itemGap: 50,
      itemHeight: 11, // 图例icon高度
      itemWidth: 11, // 图例icon宽度
    },
    xAxis: [
      {
        type: 'category',
        boundaryGap: true,
        axisTick: {
          show: false
        },
        axisLabel: {
          interval: 0,
          color: '#bdc1c8'
        },
        axisLine: {
          lineStyle: {
            color: "#bdc1c8",
          }
        },
        data: ['0-6岁', '7-17岁', '18-39岁', '40-59岁', '60-69岁', '70岁及以上']
      },
    ],
    yAxis: [
      {
        type: 'value',
        scale: true,
        name: '剂次数',
        nameTextStyle: {
          color: "#579EE5",
          fontSize: 12,
          align: 'right',
        },
        axisLabel: {
          color: '#ffffff',
          fontWeight: 100,
        },
        min: 0,
        axisLine: {
          show: false
        },
        axisTick: {
          show: false
        },
        splitLine: {
          show: true,
          lineStyle: {
            type: 'dashed',
            color: 'rgba(255,255,255,0.1)'
          }
        }
      },
      {
        type: 'value',
        axisLine: {
          show: false
        },
        axisTick: {
          show: false
        },
        splitLine: {
          show: false
        },
        nameTextStyle: {
          color: "#579EE5",
          fontSize: 12,
          align: 'left',
        },
        scale: true,
        name: '占比/%',
        min: 0,
        max: 100,
        interval: 20,
        axisLabel: {
          color: '#ffffff',
          fontWeight: 100,
          formatter: '{value}%'
        }
      }
    ],
    series: [
      {
        name: '接种总数',
        type: 'bar',
        barWidth: 12,
        yAxisIndex: 0,
        itemStyle: {
          color: 'rgba(255,255,255,0.3)'
        },
        data: data.value.total
      },
      {
        name: '接种次数',
        type: 'bar',
        barWidth: 12,
        yAxisIndex: 0,
        barGap: '-100%',
        itemStyle: {
          color: new proxy.$echarts.graphic.LinearGradient(
                    0, 0, 0, 1,
                    [
                        {offset: 0, color: '#ffffff'},
                        {offset: 0.05, color: '#20B0C8'},
                        {offset: 1, color: '#20B0C8'}
                    ]
                )
        },
        data: data.value.single
      },
      {
        name: '接种次数/接种总数',
        yAxisIndex: 1,
        type: 'line',
        itemStyle: {
          borderColor: "#F5E74F",
          color: "#F5E74F",
        },
        data: data.value.rate
      }
    ]
  };
  proxy.$echarts.dispose(document.getElementById('outFlow'))
  let outFlow = proxy.$echarts.init(document.getElementById('outFlow'), null, { width: 'auto', height: 420 });
  outFlow.clear();
  outFlow.setOption(option, true);
}
defineExpose({
  getData
})

</script>
<style lang="scss" scoped>
.out-flow {
  position: relative;
}

#outFlow {
  position: relative;
  z-index: 1;
  padding-top: 56px;
}
</style>