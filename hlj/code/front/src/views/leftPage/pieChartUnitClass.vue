<!-- 接种单位分类  柱图-双柱 -->
<template>
	<div :style="'width:'+props.width+'px;height:'+props.height+'px;'">
		<div class="unit">
			<div class="unit-item">
				<img src="../../assets/img/sum.png" alt="">
				<div class="unit-sum">
					<div class="unit-sum-title">门诊总数</div>
					<div class="unit-sum-text">{{ unitData.total }}</div>
				</div>
			</div>
			<div class="unit-item">
				<img src="../../assets/img/now.png" alt="">
				<div class="unit-sum">
					<div class="unit-sum-title">当日开诊门诊总数</div>
					<div class="unit-sum-text">{{ unitData.nowTotal }}</div>
				</div>
			</div>
		</div>
		<div id="myChartPage" class="myChartPage" :style="'width:'+props.width+'px;height:'+(props.height-60)+'px;'"></div>
	</div>
</template>
<script setup>
	import {ref,getCurrentInstance,onMounted,nextTick,onUnmounted,markRaw} from "vue"
	// import * as echarts from 'echarts'
	//自定义全局公共方法引用
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;
	import { mainStore } from "@/store/store";
	const store = mainStore();

	const oldScale=window.oldScale
	const oldWidth=window.oldWidth

	const myChart=ref(null)
	const chartOption=ref({})
	const xData=ref(['常规接种门诊', '产科接种室', '成人接种门诊', '狂犬病暴露预防处置门诊', '新冠疫苗接种点'])
	const yData1=ref([])
	const yData2=ref([])
	const fontSize=ref(12)
	const unitData=ref({})
	const gridY2=ref(30)
	const fixNums=ref(6)

	const props=defineProps({
		width: {
			type: Number,
			required: true,
		},
		height: {
			type: Number,
			required: true,
		},
	})

	const drawChart=()=>{
		let myChartPage = document.getElementById('myChartPage');
		myChart.value = markRaw(globalProxy.$echarts.init(myChartPage));
		chartOption.value = {
			backgroundColor: 'rgba(0,0,0,0)',
			grid: {
				left: 45,
				top: 35,
				right: 20,
				bottom: gridY2.value
			},
			legend: {
				orient: 'horizontal',
				x: 'right',
				y: 'top',
				padding: [8, 14, 0, 10],
				itemWidth: 8,
				itemHeight: 8,
				textStyle: { fontSize: fontSize.value,color: store.textColor, },
				icon: 'rect',
				data: ['门诊总数', '当日开诊门诊总数']
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
							content += "<div class='oneRow'>"+item.seriesName+"：<div class='text'>"+globalProxy.$util.numFormat(val)+'</div></div>';
						});
					content += "</div>";
					return content;
				}
			},
			xAxis: [{
				name: '',
				type: 'category',
				nameTextStyle: {
					fontSize: fontSize.value,
					color: store.textColor,
				},
				data: xData.value,
				axisLabel: {
					interval: 0,
					showMaxLabel: true,
					formatter(val) {
						let fixNum=window.innerWidth>1600?fixNums.value:5
						return '{a|' + `${val.slice(0, fixNum)}\n${val.slice(fixNum, fixNum*2)}\n${val.slice(fixNum*2, fixNum*3)}\n${val.slice(fixNum*3, fixNum*4)}` +'}'
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
					show: true,
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
				name: '数量',
				nameTextStyle: {
					padding: [0, 0, 0, -40],
					color: '#B0DFFF',
					fontSize: fontSize.value,
				},
				type: 'value',
				position: 'left',
				splitLine: { show: true, lineStyle: { color: '#295c88', type: 'dotted', width: 1 } },
				axisLine: {
					lineStyle: { color: ['#7cc5f7'], type: 'dotted', width: 0, fontSize: 12 }
				},
				axisTick: { show: false },
				axisLabel: {
					showMinLabel: true,
					textStyle: {fontSize: fontSize.value,color: '#7cc5f7'},
					formatter: function (value, index) {
						return value.toLocaleString('en-US')
					}
				},
				min: 0,
				minInterval: 1,
				splitNumber: 4,
			},
		],
			series: [
				{
					name: '门诊总数',
					type: 'bar',
					data: yData1.value,
					barWidth: 12,
					itemStyle: {
						normal: {
							color: new globalProxy.$echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#01c5fd' }, { offset: 0.5, color: '#01c5fd' }, { offset: 1, color: '#0c5496' }]),
						}
					},
				},
				{
					name: '当日开诊门诊总数',
					type: 'bar',
					data: yData2.value,
					barWidth: 12,
					itemStyle: {
						normal: {
							color: new globalProxy.$echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#ffe789' }, { offset: 0.5, color: '#ffe789' }, { offset: 1, color: '#9f7b1c' }]),
						}
					},
					barGap: "0",
				}
			]
		};
		myChart.value.clear();
		myChart.value.setOption(chartOption.value, true);
	}

	const addChartData=(objectData)=>{
		unitData.value=objectData;
		if(chartOption.value.series){
			chartOption.value.series[0].data = [unitData.value.commonUnitNum,unitData.value.hosUnitNum,unitData.value.adultUnitNum,unitData.value.rabiesUnitNum,unitData.value.COVIDUnitNum];
			chartOption.value.series[1].data = [unitData.value.nowCommonUnitNum,unitData.value.nowHosUnitNum,unitData.value.nowAdultUnitNum,unitData.value.nowRabiesUnitNum,unitData.value.nowCOVIDUnitNum];
			myChart.value.clear();
			myChart.value.hideLoading();
			myChart.value.setOption(chartOption.value, true);
		}
	}

	onMounted(()=>{
		fontSize.value = 12*oldScale
		fontSize.value = fontSize.value>13?13:fontSize.value;
		if(oldScale<0.9){
			gridY2.value = 68*oldScale
		}else{
			gridY2.value = 40
		}
		if(oldWidth == 1366){
			gridY2.value = 40
		}
		nextTick(()=>{
			drawChart();
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
.unit{
	display: flex;
	color:#CDFAFF;
	padding:6px 10px 0;
}
.unit-item{
	flex:1;
	display: flex;
}
.unit-item img{
	width:46px;
	margin-right: 6px;
	vertical-align: middle;
	height: 46px;
}
.unit-sum-title{
	white-space: nowrap;
}
.unit-sum-text{
	color:#5BFFBF;
	font-size:20px;
	font-weight:500;
	margin-bottom: 4px;
}
</style>