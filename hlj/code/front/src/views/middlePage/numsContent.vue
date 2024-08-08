<template>
	<!-- 地图下面的数字框 -->
	<div class="map-bottom-num-area areaBg" :style="'height:'+(props.height-36)+'px;'">
		<div style="width:100%;height: 25px;display:flex;justify-content: space-between;">
			<ss-icon iconType="divLeftTop"/>
			<ss-icon iconType="divRightTop"/>
		</div>
		<div class="dqStyle" style="height:calc(100% - 25px);">
			<div class="row-line row-line1" :style="specRow1Style">
				
				<div class="item-bg" :style="bgStyle">
					<div class="label-text" :style="labelStyle">当日接种人数</div>
					<div class="num-text" :style="itemStyle">{{globalProxy.$util.numFormat(props.numsData.todayShotPeopleNumber)}}</div>
				</div>
				<div class="item-bg" :style="bgStyle">
					<div class="label-text" :style="labelStyle">当日接种剂次数</div>
					<div class="num-text" :style="itemStyle">{{globalProxy.$util.numFormat(props.numsData.todayShotJCNumber)}}</div>
				</div>
				<div class="item-bg mR0" :style="bgStyle">
					<div class="label-text" :style="labelStyle">当日建档人数</div>
					<div class="num-text" :style="itemStyle">{{globalProxy.$util.numFormat(props.numsData.todayChildBookNumber)}}</div>
				</div>
			</div>
			<div class="row-line row-line1" :style="specRow2Style">
				<div class="item-bg" :style="bgStyle">
					<div class="label-text" :style="labelStyle">总建档数</div>
					<div class="num-text" :style="itemStyle">{{globalProxy.$util.numFormat(props.numsData.childBookNumber)}}</div>
				</div>
				<div class="item-bg" :style="bgStyle">
					<div class="label-text" :style="labelStyle">0-6岁建档数</div>
					<div class="num-text" :style="itemStyle">{{globalProxy.$util.numFormat(props.numsData.childBook06Number)}}</div>
				</div>
				<div class="item-bg" :style="bgStyle">
					<div class="label-text" :style="labelStyle">7-18岁建档数</div>
					<div class="num-text" :style="itemStyle">{{globalProxy.$util.numFormat(props.numsData.childBook718Number)}}</div>
				</div>
				<div class="item-bg mR0" :style="bgStyle">
					<div class="label-text" :style="labelStyle">18岁以上建档数</div>
					<div class="num-text" :style="itemStyle">{{globalProxy.$util.numFormat(props.numsData.childBook18Number)}}</div>
				</div>
			</div>
		</div>
	</div>
</template>
<script setup>
	import {ref,getCurrentInstance,onMounted,watch} from "vue"
	//自定义全局公共方法引用
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;
	import ssIcon from '@/components/common/icon.vue';
	const scale=window.scale
	const oldScale=window.oldScale
	const bl=window.bl
	const oldWidth=window.oldWidth
	const oldHeight=window.oldHeight
	const bl1=globalProxy.$util.checkIsWideFlag(window.bl)

	const pTop=ref(14)
	const pBottom=ref(24)
	const bgStyle=ref('')
	const itemStyle=ref('')
	const labelStyle=ref('')
	const specRow1Style=ref('')
	const specRow2Style=ref('')
	
	const props=defineProps({
		numsData: {
			type: Object,
			required: true,
		},
		height: {
			type: Number,
			required: true,
		},
	})

	watch(props.height,(val)=>{
		let oneRowHeight = 73*oldScale;
		bgStyle.value = 'height:'+oneRowHeight+'px;';
		let tempNum = (2*oneRowHeight - props.height) + 3;
		specRow1Style.value = "margin-top:-"+tempNum+"px;"
		if((oldScale>=1 && tempNum<0)){
			specRow1Style.value = "";
		}
		if(oldScale<1){
			specRow1Style.value = "margin-top:-"+10*oldScale+"px;"
		}
		if(oldWidth==1920){
			specRow1Style.value = "margin-top:-12px;"
			specRow2Style.value = "margin-top:-10px;"
			if(oldHeight<1000){
				specRow1Style.value = "margin-top:-20px;"
			}
		}
		if(oldWidth==1680){
			specRow1Style.value = "margin-top:-10px;"
			specRow2Style.value = "margin-top:0px;"
			if(oldHeight<1000){
				specRow2Style.value = "margin-top:-10px;"
				specRow1Style.value = "margin-top:-12px;"
			}
		}
		if(oldWidth==1600){
			specRow1Style.value = "margin-top:-8px;"
			specRow2Style.value = "margin-top:0px;"
			if(oldHeight<800){
				specRow2Style.value = "margin-top:-10px;"
				specRow1Style.value = "margin-top:-16px;"
			}
		}
		if(oldWidth==1366){
			specRow1Style.value = "margin-top:-8px;"
			if(oldHeight<700){
				specRow1Style.value = "margin-top:-24px;"
			}
		}
		if(oldWidth==1280){
			specRow1Style.value = "margin-top:0px;"
			if(oldHeight<1000){
				specRow1Style.value = "margin-top:-12px;"
			}
			if(oldHeight<820){
				specRow1Style.value = "margin-top:0px;"
			}
		}
		
	},{immediate:true})
	
	onMounted(() =>{
		if(oldScale<0.9){
			itemStyle.value = 'font-size:'+17*oldScale+'px;width:'+165*oldScale+'px;line-height:'+35*oldScale+'px'
			labelStyle.value = 'font-size:'+14*oldScale+'px;line-height:'+30*oldScale+'px'
		}else{
			itemStyle.value = 'width:'+150*oldScale+'px;line-height:'+14*oldScale+'px'
			labelStyle.value = 'line-height:'+35*oldScale+'px'
		}
		if(oldWidth==1366){
			itemStyle.value = 'font-size:'+15*oldScale+'px;width:'+165*oldScale+'px;line-height:'+35*oldScale+'px'
			labelStyle.value = 'font-size:'+13*oldScale+'px;line-height:'+28*oldScale+'px'
		}
	})
</script>
<style scoped>
	.row-line1{
		margin-bottom: 0px !important;
	}
	.areaBg{
		background: #0C2B66;
		border: 1px solid #06B0B6;
		opacity: 0.92;
	}
	.mR0{
		margin-right:0px;
	}
	.dqStyle{
		display:flex;
		flex-direction:column;
		justify-content: space-between;
	}
	.map-bottom-num-area .item-bg{
		color: #5BFFBF;
		font-weight: bold;
		text-align: center;
		background-image: url(../../assets/img/index/mapBottom-num1.png);
	  	background-repeat: no-repeat;
	  	background-size: contain;
	  	/*width: 171px;*/
	    height: 73px;
	}
	.map-bottom-num-area .label-text{
		color: #CEFFFA;
    	font-size: 15px;
    	text-align: center;
    	line-height: 36px;
    	white-space: nowrap;
		font-weight: 400;
		text-shadow: 0px 3px 7px rgba(0,0,0,0.71);
	}
	.map-bottom-num-area .num-text{
		font-size: 20px;
		font-weight: bold;
		color: #5BFFBF;
		line-height: 27px;
	}
</style>