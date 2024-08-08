<template>
	<div class="div-area" style="width:100%;">
		<div class="title title-bg" :style="bgHeight">
			<ss-icon iconType="leftTop"/>
			<ss-icon iconType="rightTop"/>
		</div>
		<div :style="'display:flex;justify-content: space-between;align-items: center;margin-top:-'+(mTopHeight+2)+'px;height:'+(mTopHeight)+'px'">
			<div class="title-text" :style="textStyle">{{title}}</div>
			<div :class="mySelectClass" style="margin-right:16px;margin-top: 2px;" v-if="showSelect">
				<div class="immuno">
					<div class="immuno-tab" v-if="showImmuno">
						<div @click="changeImmuno(0)" :class="immunoVal==0?'immuno-tab-active immuno-tab-l':'immuno-tab-l'">{{(oldWidth==1680 || oldWidth==1600 || oldScale<0.9)?'免疫规划':'免疫规划疫苗'}}</div>
						<div @click="changeImmuno(1)" :class="immunoVal==1?'immuno-tab-active immuno-tab-r':'immuno-tab-r'">{{(oldWidth==1680 || oldWidth==1600 || oldScale<0.9)?'非免疫规划':'非免疫规划疫苗'}}</div>
					</div>
					<el-select :popper-class="props.title=='实时库存'?'poperclass1':'poperclass'" :class="props.title=='实时库存'?'poperclass1':'poperclass'" v-model="val" style="width:100%;" @change="changeValue" v-if="showSelect" placeholder="请选择">
						<el-option v-for="item in dataList" :key="item.value" :label="item.label" :value="item.value"></el-option>
					</el-select>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
	import {ref,watch,onMounted,getCurrentInstance} from "vue"
	import ssIcon from './icon.vue';
	const { appContext } = getCurrentInstance();
	const globalProxy = appContext.config.globalProperties;
	const emits = defineEmits(['changeValue',"changeImmuno"])

	const oldScale=window.oldScale
	const mySelectClass=window.oldScale<0.9?'mySelect2':'mySelect'
	const oldWidth=window.oldWidth
	
	const textStyle=ref('')
	const bgHeight=ref('')
	const mTopHeight=ref("")
	const sHeight=ref(29)
	const immunoVal=ref(0)

	const props=defineProps({
		title: {
			type: String,
			required: true,
		},
		showSelect: {
			type: Boolean,
			required: false,
		},
		showImmuno: {
			type: Boolean,
			required: false,
		},
		selectCode: {
			type: String,
			required: false,
			default: ''
		},
		immunoName: {
			type: String,
			required: false,
			default: 'ym'
		},
		dataList:{
			type:Array,
			default:()=>[]
		}
	})
	const val=ref(props.selectCode)
	const dataList=ref(props.dataList)
	watch(()=>props.selectCode, (newVal, oldVal) => {
		if(newVal && newVal!=''){
			val.value = newVal
		}else{
			val.value = ""
		}
	},{immediate: true})

	const changeValue=(val)=>{
		emits('changeValue', val)
	}

	const changeImmuno=(val)=>{
		immunoVal.value=val;
		emits('changeImmuno', val)
	}

	onMounted(()=> {
		if(oldScale<0.9){
			textStyle.value = 'font-size:'+15*oldScale+'px;line-height:'+35*oldScale+'px';
			bgHeight.value = 'height:'+35*oldScale+'px';
			mTopHeight.value=35*oldScale
			sHeight.value = 29*oldScale;
		}else{
			textStyle.value = 'font-size:'+17+'px;';
			bgHeight.value = 'height:'+38+'px';
			mTopHeight.value=38
			sHeight.value = 29;
		}
		if(oldWidth == 1680){
			textStyle.value = 'font-size:'+16+'px;';
			bgHeight.value = 'height:'+38+'px';
			mTopHeight.value=38
			sHeight.value = 29;
		}
	})

	// 公开数据
	defineExpose({
		dataList,
	})
</script>

<style scoped>
	.div-area{
		border-top: 1px solid rgba(6, 176, 182, 0.4);
	}
	.div-area .title{
		display:flex;
		justify-content: space-between;
	}
	.div-area .title-bg{
		background: url(../../assets/img/index/bgimage-title.png) left top no-repeat;
		background-size: cover;
		background-position: left top;
		width: 100%;
		text-align: left;
	}
	.div-area .title-text{
		color: #ffffff;
	    font-weight: bold;
	    font-size: 17px;
	    padding-left: 20px;
	    letter-spacing: 0.5px;
		white-space: nowrap;
    	text-overflow: ellipsis;
		overflow: hidden;
	}
	.div-area .title-bg2{
		background: url(../../assets/img/index/bgimage-title.png) left top no-repeat;
		background-size: cover;
		background-position: left top;
		width: 100%;
		height: 36px;
		text-align: left;
	}
	.div-area .title-text2{
	    color: #ffffff;
	    font-weight: bold;
	    font-size: 15px;
	    margin-top: -40px;
	    padding-left: 20px;
	    line-height: 35px;
	    letter-spacing: 1px;
	}
	.div-area .title-bg3{
		background: url(../../assets/img/index/bgimage-title.png) left top no-repeat;
		background-size: cover;
		background-position: left top;
		width: 100%;
		height: 28px;
		text-align: left;
	}
	.div-area .title-text3{
	    color: #ffffff;
	    font-weight: bold;
	    font-size: 15px;
	    margin-top: -33px;
	    padding-left: 20px;
	    line-height: 35px;
	    letter-spacing: 1px;
	}
	.immuno{
		display: flex;
	}
	.immuno-tab{
		display: flex;align-items: center;
		vertical-align: middle;
	}
	.immuno-tab-l,.immuno-tab-r{
		text-wrap: nowrap;margin-right:-1px;border:1px solid #358ac9;padding:0 6px;
		cursor: pointer;
		color:#358ac9;
		white-space: nowrap;
		height:23px;
		line-height: 23px;
	}
	.immuno-tab-active{
		box-shadow: inset 0 0 4px 2px #2575b9;
		color:#fff;
		border-color:#1888e7;
		
	}
	.immuno .el-select{
		margin-left: 6px;
		height:25px;
	}
	
</style>
<style>
	.immuno .poperclass .el-select__wrapper,.poperclass .el-select__wrapper{
		box-shadow:none;
		background: url(../../assets/img/index/select_bg.png);
		background-size: cover;
		background-position: center center;
		outline: none;
		width: 82px;
		height: 25px;
		min-height: 25px;
		font-size: 11px;
		border-radius: 0!important;
	}
	.immuno .el-select__wrapper,.immuno .el-select__wrapper.is-focused{
		box-shadow: none!important;
	}
	.immuno .poperclass1 .el-select__wrapper,.poperclass1 .el-select__wrapper{
		box-shadow:none;
		background: url(../../assets/img/index/select_bg1.png);
		background-size: cover;
		background-position: center center;
		outline: none;
		width: 127px;
		height: 25px;
		min-height: 25px;
		font-size: 11px;
		border-radius: 0!important;
	}
	.immuno .el-select__placeholder,.poperclass .el-select__placeholder{
		color:#fff;
	}
	.immuno .el-select-dropdown__item.is-hovering,.poperclass .el-select-dropdown__item.is-hovering,.poperclass1 .el-select-dropdown__item.is-hovering{
		background: transparent!important;
	}
	.immuno .el-select-dropdown__item.is-selected,.poperclass .el-select-dropdown__item.is-selected,.poperclass1 .el-select-dropdown__item.is-selected{
		color:#22c4e8!important;
	}
</style>