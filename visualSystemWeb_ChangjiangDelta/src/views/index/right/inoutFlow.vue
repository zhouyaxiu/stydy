<!-- 跨域累计接种情况统计（疫苗/人次数） -->
<template>
  <div class="in-out-flow">
    <div ref="inoutFlow" id="inoutFlow"></div>
  </div>
</template>
<script setup>
import { ref, getCurrentInstance, watch,onBeforeUnmount } from "vue";
import { vaccineQuery } from '@/api/apis/index'
const inoutFlow = ref();
const data = ref([])
var timer=null
let vaccines = []
let outflows = []
let inflows =  []
const { proxy } = getCurrentInstance();

const props = defineProps({
  type: {
    type: String,
    default: 'immunityPlan'
  }
})
// 监听事件
watch(() => props.type, (newVal, oldVal) => {
  if (newVal != oldVal) {
   clearInterval(timer)
   getData()
  }
}, { deep: true })

const getData = async () => {
  vaccineQuery({type:props.type==='immunityPlan'?'0':'1'}).then(res => {
    if (res.data.code===200) {
      data.value = res.data.data.data
      draw()
    }
  });
}

const draw = async () => {
  vaccines = [...data.value.vaccines]
  outflows = [...data.value.outflows]
  inflows =  [...data.value.inflows]
  let option = {
    tooltip: {
      show: true,
      // appendToBody: true,
      backgroundColor: '#0e3f63',
      formatter: (params) => {
        let str = `
              <div style="font-size:13px;padding:6px;">
              	<span style='color:#FFFB00'>${vaccines[params.dataIndex]}</span>
              	</br>
                <span style='color:#4ee2e5'>
                ${params.seriesName}
                </span>
                <span style='color:#FFFFFF'>
                /${params.value}
                </span>
              </div>
            `;
        return str;
      },
    },
    legend: {
      data: [
        {name:'流出'},
        {name:'流入'},
      ],
      left: 150,
      icon: 'none',
      padding:[20,0,0,0],
      textStyle: {
        fontSize: 14,
        color: '#4ee2e5',
      },
      itemGap: 40,
    },
    dataZoom: {
      show: true, // 为true 滚动条出现
      yAxisIndex: [0,1,2],
      realtime: true, // 实时更新
      type: 'inside',
      startValue: 0,
      endValue: 6,
      zoomOnMouseWheel: false, // 关闭滚轮缩放
      moveOnMouseWheel: true, // 开启滚轮平移
      moveOnMouseMove: false, // 鼠标移动能触发数据窗口平移
    },
    grid: [{
  		show: false,
  		left: '10%',
  		top: '19%',
  		bottom: '16%',
  		containLabel: true,
  		width: 157,
  		z: 1
  	}, 
  	{
  		show: false,
  		right: '10%',
  		top: '19%',
  		bottom: '16%',
  		containLabel: true,
  		width: 157,
  		z: 2
  	},
    {
  		show: true,
  		left: 186,
  		top: '18%',
  		bottom: '16%',
  		containLabel: true,
  		borderColor: 'none',
	  	width: 72,
	  	z: 3
  	}],
    xAxis: [
      {
        type: 'value',
        name: '流出量',
        nameTextStyle:{
        	color: '#FFFFFF',
        	fontSize: 14,
          fontWeight: 100,
          lineHeight: 26,
          verticalAlign: 'bottom',
        },
        nameGap: -10,
        gridIndex: 0,
        inverse: true,
        offset: 20,
        axisTick: {
          show: true,
          inside: true
        },
        axisLabel: {
          margin: -20,
          color: '#bdc1c8',
          fontSize: 12,
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: "#bdc1c8",
          },
        },
        splitLine: {
          show: false
        },
        position: 'top',
      },
      {
        type: 'value',
        name: '流入量',
        nameTextStyle:{
        	color: '#FFFFFF',
        	fontSize: 14,
          fontWeight: 100,
          lineHeight: 26,
          verticalAlign: 'bottom',
        },
        nameGap: -10,
        gridIndex: 1,
        offset: 20,
        axisTick: {
          show: true,
          inside: true
        },
        axisLabel: {
          margin: -20,
          color: '#bdc1c8',
          fontSize: 12,
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: "#bdc1c8",
          },
        },
        splitLine: {
          show: false
        },
        position: 'top',
      },
      {
  			gridIndex: 2,
  			show: false,
  		}, 
    ],
    yAxis: [
	  	{
	  		gridIndex: 0,
	  		type: 'category',
	  		inverse: true,
	  		position: 'right',
	  		axisLine: {
	  			show: false,
	  		},
	  		axisTick: {
	  			show: false,
	  		},
	  		axisLabel: {
	  			show: false,
	  		},
	  		z: 1
	  	}, 
			{
	  		gridIndex: 1,
	  		type: 'category',
	  		inverse: true,
	  		position: 'left',
	  		axisLine: {
	  			show: false,
	  		},
	  		axisTick: {
	  			show: false,
	  		},
	  		axisLabel: {
	  			show: false,
	  		},
	  		z: 2
	  	}, 
	  	{
	  		gridIndex: 2,
	  		type: 'category',
	  		inverse: true,
	  		position: 'left',
	  		axisLine: {
	  			show: false,
	  		},
	  		axisTick: {
	  			show: false,
	  		},
	  		axisLabel: {
	  			show: true,
	  			fontSize: 12,
	  			color: '#fff',
	  			align: 'center',
	  			interval: 0,
          formatter: function (name) {
              return (name.length > 4 ? (name.slice(0,4)+"...") : name ); 
          },
	  		},
	  		data: vaccines,
	  		z: 3
	  	}
    ],
    series: [
      {
      	xAxisIndex: 0,
      	yAxisIndex: 0,
        name: '流出',
        type: 'bar',
        barWidth: 12,
        itemGap: 0,
        itemStyle: {
          color: new proxy.$echarts.graphic.LinearGradient(
                    0, 0, 1, 0,
                    [
                        {offset: 0, color: '#ffffff'},
                        {offset: 0.05, color: '#6687E6'},
                        {offset: 1, color: '#6687E6'}
                    ]
                )
        },
        data: outflows
      },
      {
      	xAxisIndex: 1,
      	yAxisIndex: 1,
        name: '流入',
        type: 'bar',
        barWidth: 12,
        itemGap: 0,
        itemStyle: {
          color: new proxy.$echarts.graphic.LinearGradient(
                    1, 0, 0, 0,
                    [
                        {offset: 0, color: '#ffffff'},
                        {offset: 0.05, color: '#20B0C8'},
                        {offset: 1, color: '#20B0C8'}
                    ]
                )
        },
        data: inflows
      }
    ]
  };
  
  proxy.$echarts.dispose(document.getElementById('inoutFlow'))
  let inoutFlow = proxy.$echarts.init(document.getElementById('inoutFlow'), null, { width: 'auto', height: 364 });
  let vaccLen=vaccines.length
  if(vaccLen>6){
    timer=setInterval(()=>{
      vaccLen=vaccines.length
      if(option.dataZoom.endValue==vaccLen-1){
        vaccines=vaccines.concat(data.value.vaccines)
        outflows=outflows.concat(data.value.outflows)
        inflows=inflows.concat(data.value.inflows)
        option.yAxis[2].data=vaccines
        option.series[0].data=outflows
        option.series[1].data=inflows
      }
      option.dataZoom.endValue=option.dataZoom.endValue+1;
      option.dataZoom.startValue=option.dataZoom.startValue+1;
      inoutFlow.setOption(option, true);
    },2500)
  }
  inoutFlow.clear();
  inoutFlow.setOption(option, true);
}
onBeforeUnmount(() => {
  clearInterval(timer)
})

defineExpose({
  getData
})

</script>
<style lang="scss" scoped>
.in-out-flow {
  position: relative;
  img{
    position: absolute;
    top: 151px;
    left: 218px;
    z-index: 1;
  }
}

#inoutFlow {
  position: relative;
  z-index: 1;
  padding-top: 80px;
}
</style>