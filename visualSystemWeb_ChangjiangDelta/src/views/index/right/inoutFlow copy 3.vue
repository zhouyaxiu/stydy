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
const { proxy } = getCurrentInstance();
const props = defineProps({
  type: {
    type: String,
    default: 'immunityPlan'
  }
})
let timer=null;
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
  function getPageList(arrays, pageNum, pageSize){
			//计算分页页数
			pageNum = pageNum - 1;
			const dataList = [];
			const pageRows = arrays.length;
			//循环判断值
			let startIndex = pageNum <= 0 ?  0 : pageNum * pageSize;
			let endIndex = (pageNum + 1) * pageSize > pageRows ? pageRows : (pageNum + 1) * pageSize;
			if(startIndex > pageRows) return dataList;
			for(startIndex; startIndex < endIndex; startIndex ++){
			  dataList.push(arrays[startIndex]);
			}
			return dataList;
		}
		
		let vaccines = data.value.vaccines		
		let outflows = data.value.outflows		
		let inflows = data.value.inflows		
		var pageNum = 1, pageSize = 6;
		var VaccinesRows = getPageList(vaccines, pageNum, pageSize);
		var OutflowsRows = getPageList(outflows, pageNum, pageSize);
		var InflowsRows = getPageList(inflows, pageNum, pageSize);

  let option = {
    tooltip: {
      show: true,
      // appendToBody: true,
      backgroundColor: '#0e3f63',
      formatter: (params) => {
        let str = `
              <div style="font-size:13px;padding:6px;">
              	<span style='color:#FFFB00'>${VaccinesRows[params.dataIndex]}</span>
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
    // dataZoom: {
    //   show: true, // 为true 滚动条出现
    //   yAxisIndex: [0,1,2],
    //   realtime: true, // 实时更新
    //   type: 'inside',
    //   startValue: 0,
    //   endValue: 6,
    //   zoomOnMouseWheel: false, // 关闭滚轮缩放
    //   moveOnMouseWheel: true, // 开启滚轮平移
    //   moveOnMouseMove: false, // 鼠标移动能触发数据窗口平移
    //   throttle:50
    // },
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
	  			color: '#bdc1c8',
	  			align: 'center',
	  			interval: 0,
          formatter: function (name) {
              return (name.length > 4 ? (name.slice(0,4)+"...") : name ); 
          },
	  		},
	  		data: data.value.vaccines,
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
        data: data.value.outflows
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
        data: data.value.inflows
      }
    ],
    // animation:true,
    // animationEasing:"backIn",
    // animationDurationUpdate:10000,
    // animationDurationUpdate: function (idx) {
    //   // 越往后的数据时长越大
    //   return idx * 100;
    // }
    // animationDuration:0
  };
  proxy.$echarts.dispose(document.getElementById('inoutFlow'))
  let inoutFlow = proxy.$echarts.init(document.getElementById('inoutFlow'), null, { width: 'auto', height: 364 });
  

  // let vaccLen=data.value.vaccines.length
  // if(vaccLen>6){
  //   timer=setInterval(()=>{
  //     // console.log(,'inoutFlowinoutFlow')
  //     pageNum++;
	// 		var rows = getPageList(data.value.vaccines, pageSize + pageNum, 1);
	// 		var rowsinflows = getPageList(data.value.inflows, pageSize + pageNum, 1);
	// 		var rowsoutflows = getPageList(data.value.outflows, pageSize + pageNum, 1);
	// 		if(rows.length ==0){
	// 			pageSize = 0;
	// 			pageNum = 0;
	// 			return;
	// 		}
	// 		//原始数组
	// 		VaccinesRows.splice(0,1);
	// 		VaccinesRows.push(...rows);
	// 		OutflowsRows.splice(0,1);
	// 		OutflowsRows.push(...rowsoutflows);
	// 		InflowsRows.splice(0,1);
	// 		InflowsRows.push(...rowsinflows);
	
  //     option.yAxis[2].data=VaccinesRows
  //     option.series[0].data=OutflowsRows
  //     option.series[1].data=InflowsRows
  //     // console.log(inoutFlow.dispatchAction({type:"datazoom"}),'dispatchAction')
  //     // console.log(inoutFlow.getDom(),'getDom')
  //     inoutFlow.on("dataZoom",function(event){
  //       console.log(event,'00000000')
  //     })
  //     // console.log(inoutFlow.on(),'oooppp')
  //     // console.log(inoutFlow.updateLabelLayout(),'updateLabelLayout')
  // // inoutFlow.dispatchAction({
  // //   type:"scroll",
  // //   // scrollDelta:-inoutFlow.getHeight() / 5 * 3,
  // //   // animationDurationUpdate:10000,
  // //   scrollDataIndex:100
  // // })
  //     inoutFlow.setOption(option, true);

  //   //   inoutFlow.getZr().on('dataZoom', function (event) {
  //   //     var offset = event.startIndex;
  //   //     console.log(offset,'0000')
  //   //     // 应用你的自定义偏移逻辑
  //   //     // 例如，可以是滚动一个 DOM 元素或者其他
  //   // });
  //   },3000)
  // }
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
:root{
  --h:36;
  --speed:1.2s
}
@keyframes liMove{
  0%{
    transform: translate(0,0);
  }
  80%,
  100%{
    transform: translate(0,-36px);
  }
}
</style>