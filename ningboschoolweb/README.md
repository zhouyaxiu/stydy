# my-project

> A Vue.js project

## Build Setup

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report
```

For a detailed explanation on how things work, check out the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).

## 插件vue-quill-editor
``` bash
基于 Quill、适用于 Vue 的富文本编辑器，支持服务端渲染和单页应用。
参考地址：https://www.kancloud.cn/liuwave/quill/1434140

# 安装
npm install vue-quill-editor --save

# 引用
import VueQuillEditor from 'vue-quill-editor'

# 引入样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

# 挂载
Vue.use(VueQuillEditor)

# 使用
<quill-editor style="width:600px; height:200px" v-model="content" ref="myQuillEditor" :options="editorOption"></quill-editor>

# 使用工具栏配置和样式进行汉化

```