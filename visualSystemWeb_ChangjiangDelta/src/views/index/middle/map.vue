<template>
	<div ref="myMapPage" class="map">
		<div class="top" @click="clickMap('0')">
			<div class="wj hover-div" @click.stop="clickMap('2')" title="江苏吴江"></div>
			<img :src="proxy.$util.getImageUrl('index/map/wj-area.png')" class="wj img">
			<div class="qp hover-div" @click.stop="clickMap('3')" title="上海青浦"></div>
			<img :src="proxy.$util.getImageUrl('index/map/qp-area.png')" class="qp img">
			<div class="js hover-div" @click.stop="clickMap('1')" title="浙江嘉善"></div>
			<img :src="proxy.$util.getImageUrl('index/map/js-area.png')" class="js img">
			<img :src="proxy.$util.getImageUrl('index/map/map-line.png')" class="track">
		</div>
		<div class="bottom">
			<div class="item"><img :src="proxy.$util.getImageUrl('index/map/clinic-icon.png')">
				<vue-number-roll-plus :number="clinicNum" background="transparent" :isSemicolon="true" class="number"></vue-number-roll-plus>
			</div>
			<div class="item"><img :src="proxy.$util.getImageUrl('index/map/doc-icon.png')">
				<vue-number-roll-plus :number="docNum" background="transparent" :isSemicolon="true" class="number"></vue-number-roll-plus>
			</div>
		</div>
	</div>
</template>
<script setup>
import VueNumberRollPlus from "vue3-number-roll-plus"
import "vue3-number-roll-plus/main.css"
import { onMounted, ref, watch, getCurrentInstance } from "vue";
import { centreQuery } from "@/api/apis/index";
const { proxy } = getCurrentInstance();
const myMapPage = ref()
const clinicNum = ref(0)
const docNum = ref(0)
const clickKey = ref('1')

// 监听事件
watch(() => clickKey.value, (newVal, oldVal) => {
	getData(newVal)
}, { deep: true })

// 获取数据
const getData = (val)=>{
	centreQuery({type: val}).then(res => {
    if (res.data.code===200) {
			clinicNum.value = res.data.data.data.stationNum
			docNum.value = res.data.data.data.recordNum
    }
  });
}
const clickMap = (val) => {
	clickKey.value = val
}

onMounted(() => {
	getData('1')
})
</script>
<style lang="scss">
.map{
	height: 100%;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	.top{
		width: 790px;
    height: 768px;
    position: relative;
		border: 1px solid #0b243d;
		background-color: rgba(11,36,61,.3);
		cursor: pointer;
		.wj{
			position: absolute;
			width: 404px;
			height: 377px;
			left: -4px;
			top: 20px;
		}
		.qp{
			position: absolute;
			width: 460px;
			height: 353px;
			right: -10px;
    	top: 70px;
		}
		.js{
			position: absolute;
			width: 382px;
			height: 405px;
			top: 345px;
    	left: 100px;
		}
		.img{
			transform: scale(1);
      transition: all 0.3s;
		}
		.hover-div:hover + .img {
			transform: scale(1.01);
		}
		.hover-div{
			z-index: 3;
		}
		.img{
			z-index: 1;
		}
		.track{
			position: absolute;
			top: 160px;
			left: 150px;
			z-index: 2;
		}
	}
	.bottom{
		display: flex;
    justify-content: space-around;
		.item{
			display: flex;
			align-items: center;
			color: #E2B634;
			font-size: 32px;
			img{
				margin-right: 48px;
			}
			.number{
				transform: scale(1.6);
				margin-top: 20px;
				min-width: 140px;
    		text-align: left;
			}
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
