# 代码框架 V1.0

Vue3.0 + element Plus

## 下载

git clone http://192.168.3.80:28080/zhoujingdong/v1.0.git

## 安装步骤

```
// 安装项目依赖
  npm install

// 开启服务器，浏览器访问
  npm run dev

// 执行构建命令，生成的dist-xxx文件夹放在服务器下即可访问，默认生产环境

// 同时构建测试(dist-test)和生产(dist-production)环境包
  npm run build 

// 单独构建测试环境命令
  npm run build--test 

// 单独构建生产环境命令
  npm run build--prod  
  
```

## 环境配置文件

vite.config.js

## 注意：

本框架包含一些基本的登陆相关
不同的项目要根据情况来增删，不需要的文件就删掉，避免不必要的冗余

## 代码规则

建议查看一些官方文档
需要掌握 vue3.0 组合式 api 写法

vue3.0 https://www.javascriptc.com/vue3js/guide/introduction.html

element-plus https://element-plus.gitee.io/zh-CN/component/button.html

### pinia 状态版本管理需要看下文档，更加简单便捷的状态数据管理方式

@/components/CountButton 组件有简易方法可快速查看

官网文档：https://pinia.vuejs.org/zh/introduction.html#basic-example

## 风格指南(代码规范)：

https://www.javascriptc.com/vue3js/style-guide/

## css scss 均可使用

记得根据情况删除不需要的文件

## 版本问题

node 建议使用 v15 以下版本
安装时候注意一下 node 与 node-sass 版本对应问题 （https://www.npmjs.com/package/node-sass）
