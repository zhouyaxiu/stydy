<template>
	<div class="time-refresh">
		<div class="date-text" :style="'font-size:'+fontSize+'px;'">{{currentTime.substr(11, 8)}}</div>
		<div class="date-right">
			<div class="time-text" :style="timeTextStyle">{{currentTime.substr(0, 10)}} {{currentWeek}}</div>
		</div>
	</div>
</template>

<script>
	import { getLunar } from 'chinese-lunar-calendar'
	export default {
		name: "timeRefresh",
		data() {
			return {
				//定时器
				timer: null,
				//当前时间
				currentTime: '',
				weeks: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
				currentWeek: '',
				currentLunar: '',
				year: new Date().getFullYear(),
		      	month: new Date().getMonth() + 1,
		      	currdate: new Date().getDate(),
		      	oldScale: window.oldScale,
				fontSize: 35,
				timeTextStyle: 'font-size: 16px;margin-top: 20px;'
				
			}
		},
		props: {},
		methods: {
		},
		created() {
			var that = this;
			this.timer = setInterval(function() {
				that.currentTime = that.$util.getCurrDateTime('yyyy/MM/dd hh:mm:ss');
			}, 1000);
		},
		mounted() {
			let that = this;
			let myDate = new Date();
			that.currentWeek = that.weeks[myDate.getDay()];
			that.currentLunar = getLunar(that.year, that.month, that.currdate);
			that.currentTime = that.$util.getCurrDateTime('yyyy/MM/dd hh:mm:ss');
			if(that.oldScale<0.9){
				that.fontSize = 35*that.oldScale
				that.timeTextStyle = 'font-size: '+16*that.oldScale+'px;margin-top: '+15*that.oldScale+'px;'
			}else{
				that.fontSize = 35
				that.timeTextStyle = 'font-size: 16px;margin-top: 20px;'
			}
		},
		beforeDestroy() {
			if(this.timer) {
				clearInterval(this.timer);
			}
		},
	}
</script>

<style scoped>
	.time-refresh {
		margin-top: 15px;
		display: flex;
	}
	.date-text{
		color: #fff;
		font-weight: bold;
		background-image: -webkit-linear-gradient(#fff, #fff, #09c2ff, #09c2ff);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
	}
	.date-right{
		margin-left: 10px;
	}
	.time-text{
		color: #fff;
		line-height: 22px;
	}
	.lunar-text{
		color: #06f7ff;
		font-size: 14px;
		line-height: 22px;
	}
</style>