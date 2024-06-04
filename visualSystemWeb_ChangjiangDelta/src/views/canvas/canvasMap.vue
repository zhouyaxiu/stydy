<!-- 地图 -->
<template>
	<div class="myMap" style="width:100%;height:100%;">
		<img :src="proxy.$util.getImageUrl('map/map.png')">
		<canvas id="canvas" ref="canvas" class="canvas"></canvas>
		<canvas id="canvas2" ref="canvas" class="canvas"></canvas>
		<canvas id="canvas3" ref="canvas" class="canvas"></canvas>
	</div>
</template>
<script lang="ts" setup>
import { dataList } from "./regionData.js";
import js from "@/assets/img/common/js.png";
import wj from "@/assets/img/common/wj.png";
import qp from "@/assets/img/common/qp.png";
import { onMounted, ref, getCurrentInstance, watch } from 'vue';
const { proxy } = getCurrentInstance();
const preClickStreet = ref(null)

const props = defineProps({
	mapInfo: {
		type: Object,
		default: {
			lineData: [],
			clickIndex: null,
			clickStreet: null,
		}
	},
})
let width = 1450
let height = 820
let canvas;
let canvasLine
let canvasText
let ctx;
let ctxLine;
let ctxText;

// 监听线条变化
watch(() => props.mapInfo.lineData, (newVal, oldVal) => {
	resetMap()
	drawLine(props.mapInfo.lineData, 1000)
	drawTextFun(props.mapInfo.lineData, 1000)
}, { deep: true })

// 点击左栏某个行动记录时
watch(() => props.mapInfo.clickIndex, (newVal, oldVal) => {
	if (oldVal === null || newVal < oldVal) {
		// 删除数据 删除文字颜色
		resetMap()
		let newData = props.mapInfo.lineData.slice(0, newVal + 1)
		drawLineStatic(newData, 0)
		drawTextFun(newData, 0)
	}
	if (newVal > oldVal && oldVal !== null) {
		// 添加数据
		ctx.drawImage(canvasLine, 0, 0)
		drawLine(props.mapInfo.lineData.slice(oldVal, newVal + 1), 800)
		drawTextFun(props.mapInfo.lineData.slice(oldVal, newVal + 1), 800)
	}
	if (props.mapInfo.clickStreet === preClickStreet.value) {
		// 相同街道区域高亮
		// lightenArea()
	}
}, { deep: true })

// 监听是否是同一个街道
watch(() => props.mapInfo.clickStreet, (newVal, oldVal) => {
	preClickStreet.value = newVal
}, { deep: true })

onMounted(() => {
	drawMap()
})

// 初始化地图
const drawMap = () => {
	canvas = document.getElementById('canvas')
	ctx = canvas.getContext('2d');
	canvas.width = width;
	canvas.height = height;

	
	canvasLine = document.getElementById('canvas2')
	canvasLine.width = width;
	canvasLine.height = height;
	ctxLine = canvasLine.getContext('2d');

	canvasText = document.getElementById('canvas3')
	canvasText.width = width;
	canvasText.height = height;
	ctxText = canvasText.getContext('2d');

	drawDefaultTextFun()
}
// 重置
const resetMap = () => {
	ctx.clearRect(0, 0, canvas.width, canvas.height)
	ctxLine.clearRect(0, 0, canvas.width, canvas.height)
	ctxText.clearRect(0, 0, canvas.width, canvas.height)
	drawDefaultTextFun()
}
// 绘制定位点和文字
const drawTextFun = async (data, time) => {
	drawText(data[0])
	drawPoint(data[0])
	for (let i = 1; i < data.length; i++) {
		await new Promise<void>((resolve) => {
			setTimeout(() => {
				drawText(data[i])
				drawPoint(data[i])
				resolve()
			}, time);
		})
	}
}
// 绘制地图默认街道
const drawDefaultTextFun = async () => {
	for (let i = 0; i < dataList.length; i++) {
		drawDefaultText(dataList[i])
	}
}

// 绘制定位点
const drawPoint = (item) => {
	const image = new Image();
	switch (item.stationType) {
		case '0':
			image.src = js;
			break;
		case '1':
			image.src = wj;
			break;
		case '2':
			image.src = qp;
			break;
		default:
			break;
	}
	image.addEventListener("load", () => {
		//按照设定的尺寸，将原图缩放在其中
		ctxText.shadowBlur = 0;
		if (item.small) {
			ctxText.drawImage(image, item.x - item.w / 2 - 10, item.y - item.h - 28, 20, 28);
		} else {
			ctxText.drawImage(image, item.x - item.w / 2 - 22, item.y - item.h - 57, 45, 57);
		}
	  ctx.drawImage(canvasText, 0, 0)
	});
}
// 绘制路径动画
const drawLine = async (data, time) => {
	let preNode = data[0]
	for (let i = 0; i < data.length; i++) {
		let item = data[i]
		let y = preNode.y
		let x = preNode.x
		function loop() {
			ctxLine.beginPath();
			let gradient = ctxLine.createLinearGradient(0, 0, 0, 700);
			gradient.addColorStop(1, 'rgba(124, 234, 70, 1)')
			gradient.addColorStop(0, 'rgba(200, 240, 255, 1)')

			ctxLine.fillStyle = gradient;
			ctxLine.clearRect(0, 0, width, height);
			ctxLine.moveTo(preNode.x, preNode.y);
			if (item.y - preNode.y < 0) {
				y -= 5
			} else {
				y += 5
			}
			x = Math.abs(y - preNode.y) * Math.abs(item.x - preNode.x) / Math.abs(item.y - preNode.y)
			if (item.x - item.pathWidth - preNode.x > 0) {
				x = preNode.x + x
			} else {
				x = preNode.x - x
			}
			ctxLine.fillStyle = gradient;
			ctxLine.lineTo(x, y);
			ctxLine.lineTo(x - item.pathWidth, y);
			ctxLine.lineTo(preNode.x - preNode.pathWidth + 4, preNode.y);
			ctxLine.closePath();
			ctxLine.shadowBlur = 10;
			ctxLine.shadowColor = '#E4FFD6'
			ctxLine.lineWidth = 3
			ctxLine.strokeStyle = '#E4FFD6';
			ctxLine.stroke();
			ctxLine.fill()
			if (item.y - preNode.y < 0 && y > item.y || item.y - preNode.y > 0 && y < item.y) {
				requestAnimationFrame(loop)
			}else{
				ctx.drawImage(canvasLine, 0, 0)
			}
		}
		loop()

		await new Promise<void>((resolve) => {
			setTimeout(() => {
				preNode = item
				ctxLine.beginPath();
				ctxLine.moveTo(item.x, item.y);
				resolve()
			}, time);
		})
	}
}
// 静态绘制路径
const drawLineStatic = async(data,time) => {
	ctxLine.beginPath();
	let preNode = data[0]
	let gradient = ctxLine.createLinearGradient(0, 0, 0, 700);
	gradient.addColorStop(1, 'rgba(124, 234, 70, 1)')
	gradient.addColorStop(0, 'rgba(200, 240, 255, 1)')
	ctxLine.fillStyle = gradient;
	ctxLine.moveTo(preNode.x, preNode.y);
	for (let i = 1; i < data.length; i++) {
		let item = data[i]
	  ctxLine.fillStyle = gradient;
		ctxLine.lineTo(item.x, item.y);
		ctxLine.lineTo(item.x - item.pathWidth, item.y);
		ctxLine.lineTo(preNode.x - preNode.pathWidth, preNode.y);
		ctxLine.closePath();
		ctxLine.shadowBlur = 10; 
		ctxLine.shadowColor = '#E4FFD6'
		ctxLine.lineWidth = 3
		ctxLine.strokeStyle = '#E4FFD6';
    ctxLine.stroke();
		ctxLine.fill()
		await new Promise<void>((resolve) => {
			setTimeout(() => {
				preNode = item
				ctxLine.beginPath();
				ctxLine.moveTo(item.x, item.y);
				resolve()
			}, time);
		})
	}
}
// 绘制文字
const drawText = (item) => {
	ctxText.beginPath();
	ctxText.font = item.font
	ctxText.textAlign = 'right';
	ctxText.textBaseline = 'bottom';
	ctxText.strokeStyle = 'rgba(29, 90, 138, 0.66)'
	ctxText.shadowBlur = 0;
	ctxText.shadowColor = '#E4FFD6'
	ctxText.lineWidth = item.lineWidth
	ctxText.strokeText(item.name, item.x, item.y)
	let gradient1 = ctxText.createLinearGradient(item.x - item.w, item.y - item.h, item.x - item.w, item.y);
	gradient1.addColorStop(0.4, "rgba(255, 243, 88, 1)")
	gradient1.addColorStop(1, "rgba(253, 160, 0, 1)")
	ctxText.fillStyle = gradient1;
	ctxText.fillText(item.name, item.x, item.y)
	ctxText.closePath();
	ctx.drawImage(canvasText, 0, 0)
}

// 绘制默认文字
const drawDefaultText = (item) => {
	ctx.beginPath();
	ctx.font = item.font
	ctx.textAlign = 'right';
	ctx.textBaseline = 'bottom';
	ctx.strokeStyle = 'rgba(29, 90, 138, 0.66)'
	ctx.shadowBlur = 0;
	ctx.shadowColor = '#E4FFD6'
	ctx.lineWidth = item.lineWidth
	ctx.strokeText(item.name, item.x, item.y)
	let gradient1 = ctx.createLinearGradient(item.x - item.w, item.y - item.h, item.x - item.w, item.y);
	gradient1.addColorStop(0.4, "#FFFFFF")
	gradient1.addColorStop(1, "#AFF1FD")
	ctx.fillStyle = gradient1;
	ctx.fillText(item.name, item.x, item.y)
	ctx.closePath();
}

// 相同街道区域高亮
// const lightenArea = () => {
// }

defineExpose({
	drawMap
})
</script>
<style lang="scss" scoped>
.myMap img {
	width: 100%;
	height: 100%;
}

.canvas {
	position: absolute;
	left: 0;
}
</style>