 <!-- 自定义本地文本文档上传组件
  /** 父组件传参
    * v-dataInfo {Object}  组件信息
    * @click {Function}  子组件回调父组件方法
 */ -->
<template>
    <el-space wrap class="upload-space">
        <el-input v-model="inputVal" placeholder="请选择上传txt文档" />
        <el-button type="primary" @click="clickHandle">选择文件</el-button>
        <input type="file" ref="fileInput" @change="fileChange" style="display: none" />
    </el-space>
</template>
<script setup>
import { onMounted } from 'vue';

//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const inputVal = ref('')
const fileInput = ref()
//子组件回调父组件方法
const emits = defineEmits(['upload-txt-change'])
const clickHandle = () => {
    fileInput.value.click()
};
//父组件传参--按钮类型
const props = defineProps({
    dataInfo: {
        type: Object,
        required: true
    }
})
watch(inputVal, (newVal, oldVal) => {
    emits('upload-txt-change', newVal, props.dataInfo)
})
const fileChange = (e) => {
    let file = e.target.files.length ? e.target.files[0] : null
    let file_name = file.name
    let file_suffix = /\.[^\.]+$/.exec(file_name)
    if (file) {
        if (typeof FileReader === 'undefined') {
            globalProxy.$util.showMsg('你的浏览器不支持txt文件读取!', 'error')
        } else {
            if (file_suffix[0] == '.txt' || file_suffix[0] == '.TXT') {
                const reader = new FileReader()
                reader.onload = function () {
                    let txtPre = reader.result // 获取的数据data
                    txtPre = txtPre.replace(/\s+/g, '') // 去掉空格符
                    txtPre = txtPre.replace(/[\r\n]/g, '') // 去掉换行符
                    if (inputVal.value) {
                        let last_str = inputVal.value.charAt(
                            inputVal.value.length - 1
                        )
                        inputVal.value =
                            last_str == ','
                                ? inputVal.value + txtPre
                                : inputVal.value + ',' + txtPre
                    } else {
                        inputVal.value = txtPre
                    }
                }
                reader.readAsText(file, 'UTF-8') // 如果出现乱码，可以更换编码 UTF-8
            } else {
                globalProxy.$util.showMsg('仅支持txt文件读取!', 'warning')
            }
            e.target.value = ''
        }
    }
}
onMounted(() => {
    inputVal.value = props.dataInfo.value
})
</script>
<style scoped lang="scss">
.upload-space {
    padding-top: 8px;
}
</style>
