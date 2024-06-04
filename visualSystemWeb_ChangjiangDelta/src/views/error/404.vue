<template>
    <div class="error-page">
        <div class="error-code">{{store.errorCode}}</div> 
        <div class="error-desc">{{store.errorMsg}}</div>
        <img
        src="../../assets/img/common/error.png"
        style="margin: 20px; max-height: 50%"
        />
        <div class="error-handle">
            <router-link to="/index">
                <el-button type="primary" size="large">返回首页</el-button>
            </router-link>
     </div>
    </div>
</template>

<script setup>
import { mainStore } from "@//store/index";
const store = mainStore()
</script>


<style scoped>
.error-page {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    width: 100%;
    height: 100%;
    background: #f3f3f3;
    box-sizing: border-box;
}
.error-code {
    line-height: 1;
    font-size: 50px;
    font-weight: bolder;
    color: #2d8cf0;
}
.error-code span {
    color: #00a854;
}
.error-desc {
    font-size: 30px;
    color: #777;
}
.error-handle {
    margin-top: 30px;
    padding-bottom: 200px;
}
.error-btn {
    margin-left: 100px;
}
</style>
