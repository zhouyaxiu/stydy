 <!-- 自定义按钮组件
 /** 父组件传参
    * v-type {String}  按钮类型 增、删、改、查、导出等
    * @click {Function}  子组件回调父组件方法
 */ -->
<template>
    <div :class="type + '-btn ss-btn'" @click="clickHandle(type)" v-btnPermi="type" v-show="false" v-btnDebounce>
    </div>
</template>
<script setup>
//子组件回调父组件方法
const emits = defineEmits(['click'])
const clickHandle = (type) => {
    emits('click', type)
};
//父组件传参--按钮类型
const props = defineProps({
    type: {
        type: String,
        default: 'insert',
        required: true
    }
})
</script>
<style scoped lang="scss">
@mixin btn-util-cls {
    background-size: contain;
    background-position: left top;
    min-width: 75px;
    min-height: 33px;
    cursor: pointer;
}

.insert-btn {
    background: url(@/assets/img/button/button_add.png) left top no-repeat;
    @include btn-util-cls
}

.update-btn {
    background: url(@/assets/img/button/button_save.png) left top no-repeat;
    @include btn-util-cls
}

.delete-btn {
    background: url(@/assets/img/button/button_delete.png) left top no-repeat;
    @include btn-util-cls
}

.query-btn {
    background: url(@/assets/img/button/button_search.png) left top no-repeat;
    @include btn-util-cls
}

.output-btn {
    background: url(@/assets/img/button/button_export.png) left top no-repeat;
    @include btn-util-cls;
    min-width: 113px;
}

.clear-btn {
    background: url(@/assets/img/button/button_clear.png) left top no-repeat;
    @include btn-util-cls;
    min-width: 78px;
}

.cancel-btn {
    background: url(@/assets/img/button/button_cancel.png) left top no-repeat;
    @include btn-util-cls;
}

.statics-btn {
    background: url(@/assets/img/button/button_statics.png) left top no-repeat;
    @include btn-util-cls;
}
</style>
