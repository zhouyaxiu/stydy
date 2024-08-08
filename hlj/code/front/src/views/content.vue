<template>
	<div :style="'height:'+heightNum">
		<!-- 头部 -->
		<ss-header :jcptFlag="!jcptFlag" @changeCityCode="changeCityCode"/>
		<!-- 内容区域 -->
		<div class="content">
			<div class="left-area" style='height:calc(100vh - 93px)'>
				<ss-left-page :width="leftWidthNum" ref="leftPage"/>
			</div>
			<div class="middle-area" :style="'padding: 0px 14px;height:'+middleHeight+'px'">
				<ss-middle-page :width="middleWidthNum-28" :middleHeight="middleHeight" ref="middlePage" @changeCityCode="changeCityCode" />
			</div>
			<div class="right-area" style='height:calc(100vh - 93px)'>
				<ss-right-page :width="leftWidthNum" ref="rightPage"/>
			</div>
		</div>
	</div>
</template>

<script setup>
	import { ref,onMounted, getCurrentInstance, onUnmounted,watch, nextTick } from 'vue';
	
	//自定义全局公共方法引用
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;
	// import { mainStore } from "@/store/store";
	// const store = mainStore()
	
	import ssHeader from '@/components/myHeader.vue';
	import ssLeftPage from '@/views/leftPage/index.vue';
	import ssRightPage from '@/views/rightPage/index.vue';
	import ssMiddlePage from '@/views/middlePage/index.vue';

	const scale=window.scale
	const bl=window.bl
	const heightNum='calc(' + (100 / window.scale) + 'vh - 83px)'
	const widthNum=window.winWidth - 40
	const middleWidthNum=window.midW * window.scale
	const middleHeight=window.winHeight - 83
	const bl1=globalProxy.$util.checkIsWideFlag(window.bl)
	const oldWidth=window.oldWidth
	// const demoDataFlag=JSON.parse(process.env.VITE_DEMODATAFLAG)
	const leftWidthNum=ref(0)
	const activeTab=ref('page1')
	const flag=ref(false)

	const specMTop=ref(20)
	const interTime=ref(null)
	const interTime2=ref(null)
	
	const leftPage = ref(null);
	const rightPage = ref(null);
	const middlePage = ref(null);
	
	const props = defineProps({
		jcptFlag: {
			type: Boolean,
			required: true
		},
	})

	const changeCityCode=()=>{
		leftPage.value && leftPage.value.initQueryData();
		rightPage.value && rightPage.value.initQueryData();
		middlePage.value && middlePage.value.initQueryData();
	}
	const myFunction=()=>{
		if(window.innerWidth>=1600){
			leftPage.value.chartHeight=(window.innerHeight-83-50*4)/4
			rightPage.value.chartHeight=(window.innerHeight-83-50*4)/4
		}
		if(window.innerWidth>=1280 && window.innerWidth<1600){
			leftPage.value.chartHeight=(window.innerHeight-83-42*4)/4
			rightPage.value.chartHeight=(window.innerHeight-83-42*4)/4
		}
		if(window.innerWidth<1280){
			leftPage.value.chartHeight=(window.innerHeight-83-30*4)/4
			rightPage.value.chartHeight=(window.innerHeight-83-30*4)/4
		}
		// window.location.reload()
	}
	onMounted(()=> {
		if(scale < 1){
			specMTop.value = 16*scale;
			if(oldWidth == 1440){
				specMTop.value = 20*scale;
			}
		}else{
			specMTop.value = 15;
		}
		if(oldWidth == 1400){
			specMTop.value = 24*scale;
		}
		leftWidthNum.value = (widthNum - middleWidthNum)/2;
		interTime.value = setInterval(changeCityCode, window.refreshTime*1000);
		window.addEventListener('resize', myFunction())
	})
	
	onUnmounted(()=>{
		clearInterval(interTime.value);
		interTime.value = null;
		window.removeEventListener('resize', myFunction())
	})
</script>