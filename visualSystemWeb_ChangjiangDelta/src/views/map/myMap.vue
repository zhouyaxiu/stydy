<!-- 地图 -->
<template>
	<div class="myMap" style="width:100%;height:100%;">
		<div ref="myMapPage" class="myMapPage" style="width:100%;height:100%;"></div>
	</div>
</template>
<script lang="ts" setup>
import mapJsonData from '@/utils/changjiangsanjiao.json';
import { areaData } from "./regionData.js";
import { yellowPoint, purplePoint, bluePoint } from "./symbol.js";
import { onMounted, ref, getCurrentInstance, watch } from 'vue';
const { proxy } = getCurrentInstance();
const myMapPage = ref()
const preClickStreet = ref(null)

const props = defineProps({
	mapInfo: {
    type: Object,
    default: {
			lineData: [],
			allPointData: [],
			clickIndex: null,
  		clickStreet: null,
		}
  },
})
// 监听线条变化
watch(() => props.mapInfo.lineData, (newVal, oldVal) => {
	addData(0, props.mapInfo.lineData)
}, { deep: true })

// 监听定位点变化
watch(() => props.mapInfo.allPointData, (newVal, oldVal) => {
	addData(1, props.mapInfo.allPointData)
}, { deep: true })

// 点击左栏某个行动记录时
watch(() => props.mapInfo.clickIndex, (newVal, oldVal) => {
	if (oldVal === null || newVal < oldVal) {
		// 删除数据 删除文字颜色
		let deleteColorData = props.mapInfo.allPointData.slice(newVal+1, oldVal && oldVal+1||props.mapInfo.allPointData.length)
		deleteData(props.mapInfo.clickIndex,deleteColorData)
	}
	if (newVal > oldVal && oldVal !== null) {
		// 添加数据
		let addLineData = props.mapInfo.lineData.slice(oldVal, newVal)
		let addPointData = props.mapInfo.allPointData.slice(oldVal +1, newVal + 1)
		addData(0, addLineData)
		addData(1, addPointData)
	}
	if(props.mapInfo.clickStreet === preClickStreet.value){
		// 相同街道区域高亮
		lightenArea()
	}
}, { deep: true })

// 监听是否是同一个街道
watch(() => props.mapInfo.clickStreet, (newVal, oldVal) => {
	preClickStreet.value = newVal
}, { deep: true })

onMounted(() => {
	drawMap()
})
let myChart = null;
let myOption = null;
const currentCity = ref({
	level: '0',
	zoomNum: 18,
	aspectScale: 1.15,
	centerPoint: [120.89, 30.94]
});

// 初始化地图
const drawMap = () => {
	myChart = proxy.$echarts.init(myMapPage.value);
	proxy.$echarts.registerMap('china', mapJsonData)
	myOption = {
		geo: [
			{
				show: true,
				center: currentCity.value.centerPoint,
				map: 'china',
				roam: false,
				move: true,
				z: 5,
				top: '9%',
				zoom: currentCity.value.zoomNum, //初始化大小
				aspectScale: currentCity.value.aspectScale,
				regions: areaData,
				itemStyle: {
					normal: {
						areaColor: '#21537e',
						borderColor: '#397B94',
						borderWidth: 2
					},
				},
				emphasis: {
					show: true,
					borderWidth: 1,
					label: {
						color: '#FAFAFA',
					},
					itemStyle: {
						areaColor: '#53e0ba',
					}
				},
				label: {
					show: true,
					textStyle: {
						color: '#FAFAFA',
						fontSize: '22px',
						overflow: 'breakAll',
						textBorderColor: '#1D5A8A',
						textBorderWidth: 2
					},
					formatter: function (val) {
						var str = val.name;
						return str;
					}
				},
			},
			{
				show: true,
				center: currentCity.value.centerPoint,
				map: 'china',
				roam: false,
				move: true,
				zoom: currentCity.value.zoomNum, //初始化大小
				aspectScale: currentCity.value.aspectScale,
				z: 1,
				top: '9.5%',
				itemStyle: {
					normal: {
						areaColor: '#224a7d',
						borderColor: 'rgba(0,0,0,0)',
						shadowColor: '#63cfe8',
						shadowOffsetX: 0,
						shadowOffsetY: 10,
						shadowBlur: 10
					},
				},
				emphasis: {
					disabled: true,
				},
			}
		],
		series: [
			{
				type: 'lines',
				zlevel: 2,
				symbolSize: [6, 4],
				label: {
					show: true,
					formatter: '{b}',
					textStyle: {
						color: '#FFF',
						fontSize: 15,
						overflow: 'breakAll',
					},
				},
				effect: {
					show: true,
					period: 6,
					trailLength: 0.6,
					symbolSize: 20,
					symbol: 'arrow',
					loop: true,
					color: '#82eb59',
					shadowBlur: 6
				},
				lineStyle: {
					normal: {
						color: {
								type: 'linear',
								x: 0,
								y: 1,
								x2: 0,
								y2: 0,
								colorStops: [{
										offset: 0, color: '#7CEA46' // 0% 处的颜色
								}, {
										offset: 1, color: '#C8F0FF' // 100% 处的颜色
								}],
						},
						width: 8, //尾迹线条宽度
						opacity: 0.9, //尾迹线条透明度
						curveness: 0.25, //尾迹线条曲直度
						cap: 'round',
						join: 'miter',
						miterLimit: 10
					},
				},
				data: []
			},
			{
				type: 'scatter',
				coordinateSystem: 'geo',
				legendHoverLink: true,
				zlevel: 3,
				animation: true,
				animationEasing: 'linear',
				symbol: (value, params) => {
					if (params.data && params.data.stationType === '2') {
						return yellowPoint
					} else if (params.data && params.data.stationType === '1') {
						return purplePoint
					} else {
						return bluePoint
					}
				},
				symbolSize: [35, 45],
				symbolOffset: [0, -40],
				data: [],
				label: {
					show: true,
					offset: [0, 40],
					textStyle: {
					  fontSize: 24,
						fontWeight: 'bold',
						color: "#fda809",
						textShadowColor: '#1D5A8A',
						textShadowOffsetX: 0,
						textShadowOffsetY: 2,
						textShadowBlur: 2,
						textBorderColor: '#1D5A8A',
						textBorderWidth: 2
					},
					formatter: function (params) {
						return params.data.supName;
					}
				}
			},
		]
	}
	myChart.setOption(myOption, true);
}

// 
const clearOption = () =>{
  myChart.clear()
}

// 添加新数据
const addData = async (index, newData) => {
  console.log(index, newData,'addDAta');
  if(newData.length) {
	myOption.series[index].data.push(newData[0]);
	myChart.setOption(myOption, true);
    for (let i = 1; i < newData.length; i++) {
      await new Promise<void>((resolve) => {
        setTimeout(() => {
          myOption.series[index].data.push(newData[i]);
          myChart.setOption(myOption, true);
          resolve()
        }, 1000);
      })
    }
  }
}

// 删除数据
const deleteData = (index,deleteColorData) => {
	// 计算删除最后几位
	let length1 = myOption.series[0].data.length - index;
	let length2 = deleteColorData.length;
	// 删除线条
	myOption.series[0].data.splice(-length1, length1);
	// 删除定位点
	myOption.series[1].data.splice(-length2, length2);
	myChart.setOption(myOption, true);
}

// 相同街道区域高亮
const lightenArea = () => {
	let newRegions = []
	areaData.forEach(item => {
		if (item.name === preClickStreet.value) {
			item = {
				name: item.name,
				itemStyle: {
					areaColor: '#53e0ba',
				}
			}
		}
		newRegions.push(item)
	})
	myOption.geo[0].regions = newRegions
	myChart.setOption(myOption, true);
	setTimeout(() => {
		myOption.geo[0].regions = areaData
		myChart.setOption(myOption, true);
	}, 1000);
}

defineExpose({
	drawMap,
  clearOption
})
</script>
<style lang="scss" scoped></style>