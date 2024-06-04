<!-- dialog弹框 -->
<template>
	<transition name="fade">
		<el-dialog v-model="getDialog" :append-to-body="true" align-center top="0" :show-close="false" class="dialog-content"
			:close-on-click-modal="false" :destroy-on-close="true" :draggable="false">
			<template #header>
				<div class="title-area">
					<div style="display:flex;">
						<div class="icon"></div>
						<div class="text">{{ dialogTitle }}</div>
					</div>
					<div class="close" @click="closeDialog"></div>
				</div>
			</template>
			<slot></slot>
		</el-dialog>
	</transition>
</template>

<script setup>
import { computed } from 'vue';
const props = defineProps({
	dialogTitle: {
		type: String,
		default: '',
	},
	dialogVisible: {
		type: Boolean,
		default: false
	}
})
const emits = defineEmits(['update:dialogVisible'])
// 关闭弹框
const closeDialog = () => {
	emits("update:dialogVisible", false)
}
const getDialog = computed({
	get: () => props.dialogVisible,
	set: (val) => {
		emits('update:dialogVisible', val);
	}
})
</script>
	
<style lang="scss">
.el-overlay-dialog {
	overflow: hidden;
}

@keyframes dialog-fade-in {
	20% {
		transform: scale(0.5, 0.5);
	}
}

@keyframes dialog-fade-out {
	100% {
		transform: scale(1, 1);
		opacity: 1;
	}
}

.el-dialog__body {
	background: url(@/assets/img/common/dialog-body.png) no-repeat;
	width: 1114px;
	height: 538px;
	padding: 0px !important;
	margin-top: -3px !important;
}

.dialog-content {
	background: none !important;
	cursor: default;
	padding: 0px;
	margin: 0px;
	width: 1114px;
	height: 602px;

	.el-dialog__header {
		padding: 0px !important;
		cursor: normal;
	}

	.title-area {
		background: url(@/assets/img/common/dialog-head.png) no-repeat;
		width: 1108px;
		height: 66px;
		display: flex;
		justify-content: space-between;

		.icon {
			background: url(@/assets/img/common/dialog-icon.png) no-repeat;
			width: 22px;
			height: 24px;
			margin: 18px 20px 24px 20px;
		}

		.text {
			font-size: 16px;
			font-weight: bold;
			color: #fff;
			letter-spacing: 1px;
			line-height: 60px;
		}

		.close {
			background: url(@/assets/img/common/dialog-close.png) no-repeat;
			width: 26px;
			height: 26px;
			margin: 17px 24px 23px 20px;
			cursor: pointer;
		}

		.close:hover {
			color: red;
		}
	}
}</style>
