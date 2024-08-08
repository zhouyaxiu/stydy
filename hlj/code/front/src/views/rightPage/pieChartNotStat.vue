<template>
	<!-- 柱图-双柱 当年非免疫规划疫苗接种统计-->
	<div id="bar2ChartPeopleArea" class="chartBg" :style="'width:'+props.width+'px;height:'+(props.height)+'px;'">
		<div id="bar2ChartPeople"></div>
	</div>
</template>

<script setup>
	import { nextTick, watch,onMounted,onUnmounted,getCurrentInstance,ref,markRaw } from 'vue';
	//自定义全局公共方法引用
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;
	import { mainStore } from "@/store/store";
	const store = mainStore();
	
	const oldScale=window.oldScale
	const oldWidth=window.oldWidth

	const myChart=ref(null)
	const chartOption=ref({})
	const showDataZoom=ref(true)
	const xData=ref([])
	const yData=ref([])
	// const showNum=ref(7)
	const fontSize=ref(12)
	const gridY=ref(40)
	const gridY2=ref(30)
	const lastHeight=ref(globalProxy.height)
	const spliceNumber=ref(8)
		
	const props=defineProps( {
		width: {
			type: Number,
			required: true,
		},
		height: {
			type: Number,
			required: true,
		},
	})

	watch(props.height,(val,oldVal)=>{
		if(oldWidth==1280){
			lastHeight.value = props.height + 20
		}
		if(oldWidth==1366 || oldWidth==1360){
			lastHeight.value = props.height + 30
		}
		if(oldWidth==1440 || oldWidth==1400){
			lastHeight.value = props.height + 35
		}
	},{immediate: true})

	const initChart=()=> {
		let chart = document.getElementById('bar2ChartPeople');
		let chartArea = document.getElementById('bar2ChartPeopleArea');
		var resizeWorldMapContainer = function() {
			chart.style.width = chartArea.clientWidth + 'px';
			chart.style.height = (chartArea.clientHeight) + 'px';
		};
		resizeWorldMapContainer();
		myChart.value = markRaw(globalProxy.$echarts.init(chart));
		chartOption.value = {
			backgroundColor: 'rgba(0,0,0,0)',
			grid: {
				left: 35,
				top: 20,
				right: 20,
				bottom: 40
			},
			tooltip: {
				show: true,
				trigger: 'axis',
				backgroundColor: 'rgba(0,0,0,0)',
				borderWidth:0,
				axisPointer: {
					type: 'none',
					lineStyle: {
						color: '#D2D4EA',
						width: 1,
						type: 'dashed'
					}
				},
				formatter: function(params) {
					let name = params[0].name;
					let content = "<div class='comm-chart-tip-area'>";
						content += "<div class='oneRow'><div class='text'>"+name+'</div></div>';
						params.forEach(item=>{
							let val = item.value||'0';
							content += "<div class='oneRow'>人数：<div class='text'>"+globalProxy.$util.numFormat(val)+'</div></div>';
						});
					content += "</div>";
					return content;
				}
			},
			xAxis: [{
				name: '',
				position: 'right',
				type: 'category',
				// nameTextStyle: {
				// 	fontSize: fontSize.value,
				// 	color: '#333',
				// },
				// triggerEvent:true,
				data: xData.value,
				axisLabel: {
					interval: 0,
					showMaxLabel: true,
					// color:'#333',
					// fontSize: fontSize.value,
					// rotate:45,
					formatter(val) {
						// if(val.includes("疫苗")){
						// 	val=`${val.slice(0,-2)}`
						// }
						return '{a|' + `${val.slice(0, spliceNumber.value)}\n${val.slice(spliceNumber.value, spliceNumber.value*2)}\n${val.slice(spliceNumber.value*2, spliceNumber.value*3)}\n${val.slice(spliceNumber.value*3, spliceNumber.value*4)}` +'}'
					},
					rich: {
						a: {
							/* 增加行高 避免太拥挤*/
							height: 16,
							fontSize: fontSize.value,
							color: store.textStyleColor,
							align: 'center',
						}
					}
				},
				splitLine: {
					show: false
				},
				axisLine: {
					show: xData.value.length==0?false:true,
					onZero: false,
					lineStyle: {
						color: '#5378A7',
						type: 'dotted',
						width: 1
					}
				},
				axisTick: {
					show: false
				},
			}],
			yAxis: [{
				type: 'value',
				position: 'left',
				splitLine: {show: true, lineStyle: {color: '#295c88', type:'dotted', width:1}},
				axisLine:{
					lineStyle: {color: ['#7cc5f7'], type:'dotted', width:0, fontSize: 12}
				},
				axisTick:{show: false},
				axisLabel:{
					showMinLabel: true,
					textStyle: {fontSize: fontSize.value,color: '#7cc5f7',},
					formatter: function(value,index){
						return value.toLocaleString('en-US')
					}
				},
				min: 0,
				// max: 100,
				splitNumber: 5,
				// minInterval: 1,
			}],
			series: [{
					type: 'bar',
					data: yData.value,
					barWidth: 10,
					itemStyle: { 
						normal:{
							color: new globalProxy.$echarts.graphic.LinearGradient(0, 1, 0, 0,[{offset: 0, color: '#905187'}, {offset: 0.5, color: '#b06fc1'}, {offset: 1, color: '#d28dfd'}]),
						}
					},
					barGap:"-2",
				}
			],
			dataZoom: {
				show: showDataZoom.value,
				realtime: true, //是否实时更新
				type: 'slider',
				filterMode: 'filter',
				maxValueSpan: 4, //窗口的大小，显示数据的条数的
				handleSize: 0,//滑动条的 左右2个滑动条的大小
				height: 8,//组件高度
				right: 15,
				left: 45,
				bottom: 5,
				brushSelect:false,//是否开启刷性功能
				borderColor: "rgba(0,0,0,0)",
				fillerColor: '#004E95',
				backgroundColor: 'rgba(0,0,0,0)',//两边未选中的滑动条区域的颜色
				showDataShadow: false,//是否显示数据阴影 默认auto
				showDetail: false,//即拖拽时候是否显示详细数值信息 默认true
			},				
		};
		myChart.value.clear();
		myChart.value.setOption(chartOption.value);
	}
	
	const addChartData=(objectData)=>{
		let xData=objectData.map(item=>{return item.vaccinationName})
		let yData=objectData.map(item=>{return item.count})
		let maxNum=Math.max(...yData);
		if(chartOption.value.xAxis){
			chartOption.value.xAxis[0].data = xData;
			chartOption.value.series[0].data = yData;
			chartOption.value.yAxis[0].max = maxNum;
			if(maxNum>100) chartOption.value.grid.left=55;
			else chartOption.value.grid.left=35;
			chartOption.value.yAxis[0].minInterval = Math.ceil(maxNum/5);
			myChart.value.clear();
			myChart.value.hideLoading();
			myChart.value.setOption(chartOption.value, true);	
		}
	}

	const showChartLoading=()=>{
		myChart.value.showLoading({
			text: 'loading',
			color: '#c23531',
			textColor: '#fff',
			maskColor: 'rgba(255, 255, 255, 0.2)',
			zlevel: 0,
		});
	}

	onMounted(()=> {
		// interval.value=0
		spliceNumber.value=8
		fontSize.value = 12*oldScale
		fontSize.value = fontSize.value>13?13:fontSize.value;
		if(oldScale<0.9){
			gridY.value = 20*oldScale
			gridY2.value = 25*oldScale
			spliceNumber.value=7
		}else{
			gridY.value = 20
			gridY2.value = 15
			spliceNumber.value=8
		}
		if(oldWidth==1366 || oldWidth==1360){
			gridY.value = 20
			gridY2.value = 15
			spliceNumber.value=8
		}
		nextTick(()=>{
			initChart()
		})
	})

	onUnmounted(()=>{
		myChart.value = null;
	})
	
	// 公开数据
	defineExpose({
		addChartData
	})
</script>
<style scoped>

</style>