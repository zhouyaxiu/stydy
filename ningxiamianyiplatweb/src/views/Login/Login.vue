<template>
  <div class="login-page" :style="{ color: $color - primary, background: 'var(--main-background)' }">
    <div class="login-left"></div>
    <div class="login-right"></div>
    <div class="login-form">
      <el-form :model="form" :rules="rules" ref="loginRef" label-position="top">
        <div class="login-title">欢迎登录</div>
        <div class="login-title" v-if="isCKJC === false">免疫规划信息系统</div>
        <div class="login-title" v-else>免疫规划产院接种信息系统</div>
        <div class="mt_50">
          <el-form-item label="" prop="name">
            <el-input placeholder="请输入用户名" v-model.trim="form.name" />
          </el-form-item>
          <el-form-item label="" prop="pass">
            <el-input placeholder="请输入密码" v-model.trim="form.pass" show-password />
          </el-form-item>
          <el-form-item label="" prop="valid">
            <div class="valid">
              <el-input placeholder="请输入验证码" v-model="form.valid" class="input" @keyup.enter.native="onSubmit" />
              <img :src="img_src" class="verifyImg" alt="点击刷新" title="点击刷新" @click="getVerifyImg" />
            </div>
          </el-form-item>
          <div class="login-btn" @click="onSubmit"></div>
        </div>
      </el-form>
    </div>
    <div class="login-footer">
      <div>建议使用谷歌浏览器，360浏览器或搜狗浏览器</div>
      <div>xx科技（苏州）股份有限公司研制</div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { mainStore } from "@/store/index";
import md5 from 'js-md5'
import { login, getSessionid } from "@/api/apis/login"
import { getLevel, areaTree } from '@/utils/common/area.js'
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const BASE_URL = window.pathCommon;
const isCKJC = window.ISCKJC;
const store = mainStore()
const router = useRouter();
const isAble = ref(false)
const sessionid = ref('')
const cnt = ref(60)
const time = ref(new Date().getTime())
const img_src = ref('')
const loginRef = ref(null);
const form = reactive({
  name: '',
  pass: '',
  valid: ''
})
const rules = {
  name: [
    {
      required: true,
      message: "请输入用户名",
      trigger: "blur",
    },
  ],
  pass: [
    {
      required: true,
      message: "请输入密码",
      trigger: "blur"
    },
  ],
  valid: [
    {
      required: true,
      message: "请输入验证码",
      trigger: "blur"
    },
  ],
};
const onSubmit = () => {
  let res = {
    "data": {
      "roleModules": [
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "257",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "1",
          "moduleId": "1010011160",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "1",
          "moduleId": "1010011161",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "1",
          "moduleId": "1010011162",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "1",
          "moduleId": "1010011163",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "1",
          "moduleId": "1010011176",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "1",
          "update": "1",
          "moduleId": "1010011147",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "0",
          "query": "0",
          "insert": "0",
          "update": "0",
          "moduleId": "165",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "0",
          "query": "0",
          "insert": "0",
          "update": "0",
          "moduleId": "159",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "0",
          "query": "0",
          "insert": "0",
          "update": "0",
          "moduleId": "158",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "1",
          "moduleId": "1010011149",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "0",
          "query": "0",
          "insert": "0",
          "update": "0",
          "moduleId": "30",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "0",
          "query": "0",
          "insert": "0",
          "update": "0",
          "moduleId": "109",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "0",
          "query": "0",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011183",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "161",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "162",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "163",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "164",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "8",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "155",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "156",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "69",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "13",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "14",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1313",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "4655",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "4654",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "25",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "101",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "108",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011117",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "4659",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "4660",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011113",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011114",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011115",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011116",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011118",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011119",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011120",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011122",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011123",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011127",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011133",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011128",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011129",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011130",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011134",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011135",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011154",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011155",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011156",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "151",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "152",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "153",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "154",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "1",
          "update": "1",
          "moduleId": "4648",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "4649",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "1",
          "update": "1",
          "moduleId": "39",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "1",
          "update": "1",
          "moduleId": "49",
          "delete": "1"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "52",
          "delete": "1"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "53",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "1",
          "update": "1",
          "moduleId": "40",
          "delete": "1"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "98",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "42",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "43",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "1",
          "update": "1",
          "moduleId": "45",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "1",
          "update": "1",
          "moduleId": "46",
          "delete": "1"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "4040",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "202",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "204",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "205",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "206",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "0",
          "insert": "0",
          "update": "0",
          "moduleId": "208",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "0",
          "insert": "0",
          "update": "0",
          "moduleId": "209",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "0",
          "insert": "0",
          "update": "0",
          "moduleId": "210",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "0",
          "insert": "0",
          "update": "0",
          "moduleId": "211",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "0",
          "insert": "0",
          "update": "0",
          "moduleId": "212",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "1",
          "update": "1",
          "moduleId": "36",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "1",
          "update": "1",
          "moduleId": "4541",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "1",
          "update": "1",
          "moduleId": "4542",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "213",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "214",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "215",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011112",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011164",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011165",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011166",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011167",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011168",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "68",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "1",
          "update": "1",
          "moduleId": "4652",
          "delete": "1"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011177",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011178",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011179",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "136",
          "delete": "0"
        },
        {
          "output": "0",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "99",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1010011182",
          "delete": "0"
        },
        {
          "output": "1",
          "print": "1",
          "query": "1",
          "insert": "0",
          "update": "0",
          "moduleId": "1115765",
          "delete": "0"
        }
      ],
      "userarea": "640000000000",
      "supareaid": "0",
      "userinfo": {
        "updateDate": "2020-12-12 00:00:00",
        "userPassword": "77585115c290196e09d09e6ae25f9493",
        "invalidStatus": 0,
        "updateUser": "ninxia",
        "userName": "超级管理员1",
        "tableStatus": "0",
        "areaCode": "640000000000",
        "areaId": "1",
        "supAreaId": "0",
        "areaName": "",
        "userStyle": "blueStyle",
        "loginName": "ADMIN",
        "invalidCount": 0,
        "id": "1",
        "userType": 0,
        "onOff": "1.0"
      },
      "modules": [
        {
          "supModules": [
            {
              "supModules": [
                {
                  "supId": "160",
                  "updateDate": "2020-11-03 16:08:33",
                  "moduleCode": "M0161",
                  "moduleShortName": "接种率调查",
                  "moduleName": "接种率调查",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "接种率调查",
                  "containOptions": "6",
                  "id": "161",
                  "moduleUrl": "inoculation/coverageReport",
                  "onOff": "1",
                  "index": "/inoculation/coverageReport"
                },
                {
                  "supId": "161",
                  "updateDate": "2020-11-03 16:08:33",
                  "moduleCode": "M0161",
                  "moduleShortName": "导出水印excel",
                  "moduleName": "导出水印excel",
                  "updateUser": "导出水印excel",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "导出水印excel",
                  "containOptions": "6",
                  "id": "161",
                  "moduleUrl": "excelwater",
                  "onOff": "1",
                  "index": "/excelwater"
                }
              ],
              "supId": "102",
              "updateDate": "2012-08-27 09:26:38",
              "moduleCode": "M0160",
              "moduleShortName": "接种率调查",
              "moduleExplain": "接种率调查",
              "moduleName": "接种率调查",
              "updateUser": "超级管理员",
              "id": "160",
              "tableStatus": "0",
              "onOff": "1",
              "index": "160"
            },
            {
              "supModules": [
                {
                  "supId": "2",
                  "updateDate": "2014-08-15 14:13:53",
                  "moduleCode": "M0212",
                  "moduleShortName": "档案基本信息完整性",
                  "moduleName": "档案基本信息完整性",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "档案基本信息完整性",
                  "containOptions": "6",
                  "id": "155",
                  "moduleUrl": "qc/infofull",
                  "onOff": "1",
                  "index": "/qc/infofull"
                },
                {
                  "supId": "2",
                  "updateDate": "2014-08-15 14:16:39",
                  "moduleCode": "M0213",
                  "moduleShortName": "档案接种信息完整性",
                  "moduleName": "档案接种信息完整性",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "档案接种信息完整性",
                  "containOptions": "6",
                  "id": "156",
                  "moduleUrl": "qc/jzjlfull",
                  "onOff": "1",
                  "index": "/qc/jzjlfull"
                },
                {
                  "supId": "2",
                  "updateDate": "2024-04-07 14:16:39",
                  "moduleCode": "M0216",
                  "moduleShortName": "接种记录补录统计",
                  "moduleName": "接种记录补录统计",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "接种记录补录统计",
                  "containOptions": "6",
                  "id": "158",
                  "moduleUrl": "qc/jzjlbl",
                  "onOff": "1",
                  "index": "/qc/jzjlbl"
                },
                {
                  "supId": "2",
                  "updateDate": "2024-04-07 14:16:39",
                  "moduleCode": "M0217",
                  "moduleShortName": "停用接种单位预警",
                  "moduleName": "停用接种单位预警",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "停用接种单位预警",
                  "containOptions": "6",
                  "id": "159",
                  "moduleUrl": "qc/blockupWarn",
                  "onOff": "1",
                  "index": "/qc/blockupWarn"
                },
                {
                  "supId": "2",
                  "updateDate": "2024-04-07 14:16:39",
                  "moduleCode": "M0218",
                  "moduleShortName": "接种记录上传及时性统计",
                  "moduleName": "接种记录上传及时性统计",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "接种记录上传及时性统计",
                  "containOptions": "6",
                  "id": "165",
                  "moduleUrl": "qc/jzjlUploedTimely",
                  "onOff": "1",
                  "index": "/qc/jzjlUploedTimely"
                }
              ],
              "supId": "102",
              "updateDate": "2014-12-30 14:58:30",
              "moduleCode": "M02",
              "moduleShortName": "质量控制",
              "moduleName": "质量控制",
              "updateUser": "超级管理员",
              "id": "2",
              "tableStatus": "0",
              "onOff": "1",
              "index": "2"
            },
            {
              "supModules": [
                {
                  "supId": "10",
                  "updateDate": "2009-11-18 14:18:18",
                  "moduleCode": "M0300",
                  "moduleShortName": "受种者数查询",
                  "moduleName": "受种者数查询",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "受种者数查询",
                  "containOptions": "6",
                  "id": "69",
                  "moduleUrl": "childrendata/ChildrenCount",
                  "onOff": "1",
                  "index": "/childrendata/ChildrenCount"
                },
                {
                  "supId": "10",
                  "updateDate": "2009-11-18 14:18:18",
                  "moduleCode": "M0303",
                  "moduleShortName": "档案查询",
                  "moduleName": "档案查询",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "档案查询",
                  "containOptions": "6",
                  "id": "13",
                  "moduleUrl": "childrendata/ChildrenCase",
                  "onOff": "1",
                  "index": "/childrendata/ChildrenCase"
                },
                {
                  "supId": "10",
                  "updateDate": "2009-11-18 14:18:18",
                  "moduleCode": "M0304",
                  "moduleShortName": "变迁情况查询",
                  "moduleName": "变迁情况查询",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "变迁情况查询",
                  "containOptions": "6",
                  "id": "14",
                  "moduleUrl": "childrendata/Transfer",
                  "onOff": "1",
                  "index": "/childrendata/Transfer"
                },
                {
                  "supId": "10",
                  "updateDate": "2017-01-16 10:31:58",
                  "moduleCode": "M0306",
                  "moduleShortName": "重复档案受种者查询",
                  "moduleName": "重复档案受种者查询",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "重复档案受种者查询",
                  "containOptions": "6",
                  "id": "4655",
                  "moduleUrl": "childrendata/SameChildren_init",
                  "onOff": "1",
                  "index": "/childrendata/SameChildren_init"
                }
              ],
              "supId": "102",
              "updateDate": "2012-08-27 09:26:18",
              "moduleCode": "M03",
              "moduleShortName": "受种者资料查询",
              "moduleExplain": "受种者资料查询",
              "moduleName": "受种者资料查询",
              "updateUser": "超级管理员",
              "id": "10",
              "tableStatus": "0",
              "onOff": "1",
              "index": "10"
            },
            {
              "supModules": [
                {
                  "supId": "24",
                  "updateDate": "2012-05-21 15:54:38",
                  "moduleCode": "M0501",
                  "moduleShortName": "疫苗接种人次（按接种时间）",
                  "moduleName": "疫苗接种人次（按接种时间）",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "疫苗接种人次（按接种时间）",
                  "containOptions": "6",
                  "id": "25",
                  "moduleUrl": "stat/staticRpt",
                  "onOff": "1",
                  "index": "/stat/staticRpt"
                },
                {
                  "supId": "24",
                  "updateDate": "2020-11-03 16:08:33",
                  "moduleCode": "M050112",
                  "moduleShortName": "疫苗接种人次（按出生时间）",
                  "moduleName": "疫苗接种人次（按出生时间）",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "疫苗接种人次（按出生时间）",
                  "containOptions": "6",
                  "id": "101",
                  "moduleUrl": "stat/staticRptBirth",
                  "onOff": "1",
                  "index": "/stat/staticRptBirth"
                },
                {
                  "supId": "24",
                  "updateDate": "2020-11-03 16:08:33",
                  "moduleCode": "M0503",
                  "moduleShortName": "疫苗接种人次（按生产企业批号查询）",
                  "moduleName": "疫苗接种人次（按生产企业批号查询）",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "疫苗接种人次（按生产企业批号查询）",
                  "containOptions": "6",
                  "id": "108",
                  "moduleUrl": "stat/staticRptBirth",
                  "onOff": "1",
                  "index": "/stat/staticRptBirth"
                },
                {
                  "supId": "24",
                  "updateDate": "2023-12-12 09:50:08",
                  "moduleCode": "M0506",
                  "moduleShortName": "数字门诊预约和接种情况的监控统计表",
                  "moduleName": "数字门诊预约和接种情况的监控统计表",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "数字门诊预约和接种情况的监控统计表",
                  "containOptions": "6",
                  "id": "1010011183",
                  "moduleUrl": "stat/szmzOrderAndjzjc",
                  "onOff": "1",
                  "index": "/stat/szmzOrderAndjzjc"
                },
                {
                  "supId": "24",
                  "updateDate": "2012-05-21 15:04:42",
                  "moduleCode": "M0507",
                  "moduleShortName": "乙肝感染孕产妇所生儿童乙肝疫苗全程接种率",
                  "moduleName": "乙肝感染孕产妇所生儿童乙肝疫苗全程接种率",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "乙肝感染孕产妇所生儿童乙肝疫苗全程接种率",
                  "containOptions": "6",
                  "id": "30",
                  "moduleUrl": "stat/mhbsagRpt",
                  "onOff": "1",
                  "index": "/stat/mhbsagRpt"
                },
                {
                  "supId": "24",
                  "updateDate": "2020-11-03 16:08:33",
                  "moduleCode": "M0510",
                  "moduleShortName": "查漏补种统计表",
                  "moduleName": "查漏补种统计表",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "查漏补种统计表",
                  "containOptions": "6",
                  "id": "109",
                  "moduleUrl": "gjdc/immunePlatStatistics_query",
                  "onOff": "1",
                  "index": "/gjdc/immunePlatStatistics_query"
                }
              ],
              "supId": "102",
              "updateDate": "2012-08-27 09:26:38",
              "moduleCode": "M05",
              "moduleShortName": "统计分析",
              "moduleExplain": "统计分析",
              "moduleName": "统计分析",
              "updateUser": "超级管理员",
              "id": "24",
              "tableStatus": "0",
              "onOff": "1",
              "index": "24"
            },
            {
              "supModules": [
                {
                  "supId": "1010011160",
                  "updateDate": "2022-07-06 12:34:56",
                  "moduleCode": "M06001",
                  "moduleShortName": "国家免疫规划常规免疫接种率月报表",
                  "moduleName": "国家免疫规划常规免疫接种率月报表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "国家免疫规划常规免疫接种率月报表",
                  "containOptions": "6",
                  "id": "1010011161",
                  "moduleUrl": "inoculation/routineJzlMonthRpt_query",
                  "onOff": "1",
                  "index": "/inoculation/routineJzlMonthRpt_query"
                },
                {
                  "supId": "1010011160",
                  "updateDate": "2023-07-25 16:31:05",
                  "moduleCode": "M060012",
                  "moduleShortName": "国家免疫规划常规免疫接种率月报表（横表）",
                  "moduleName": "国家免疫规划常规免疫接种率月报表（横表）",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "国家免疫规划常规免疫接种率月报表（横表）",
                  "containOptions": "6",
                  "id": "1010011178",
                  "moduleUrl": "inoculation/routineJzlMonthRptHorizontal_query",
                  "onOff": "1",
                  "index": "/inoculation/routineJzlMonthRptHorizontal_query"
                },
                {
                  "supId": "1010011160",
                  "updateDate": "2022-07-06 12:34:56",
                  "moduleCode": "M06002",
                  "moduleShortName": "非国家免疫规划疫苗接种数月报表",
                  "moduleName": "非国家免疫规划疫苗接种数月报表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "非国家免疫规划疫苗接种数月报表",
                  "containOptions": "6",
                  "id": "1010011162",
                  "moduleUrl": "inoculation/categoryTwoJzsMonthRpt_query",
                  "onOff": "1",
                  "index": "/inoculation/categoryTwoJzsMonthRpt_query"
                },
                {
                  "supId": "1010011160",
                  "updateDate": "2023-07-25 16:31:05",
                  "moduleCode": "M060022",
                  "moduleShortName": "非国家免疫规划疫苗接种数月报表（横表）",
                  "moduleName": "非国家免疫规划疫苗接种数月报表（横表）",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "非国家免疫规划疫苗接种数月报表（横表）",
                  "containOptions": "6",
                  "id": "1010011179",
                  "moduleUrl": "inoculation/categoryTwoJzsMonthRptHorizontal_query",
                  "onOff": "1",
                  "index": "/inoculation/categoryTwoJzsMonthRptHorizontal_query"
                },
                {
                  "supId": "1010011160",
                  "updateDate": "2022-07-16 12:34:56",
                  "moduleCode": "M06003",
                  "moduleShortName": "国家免疫规划常规免疫出生队列接种率报表",
                  "moduleName": "国家免疫规划常规免疫出生队列接种率报表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "国家免疫规划常规免疫出生队列接种率报表",
                  "containOptions": "6",
                  "id": "1010011163",
                  "moduleUrl": "inoculation/BirthQueueRpt_query",
                  "onOff": "1",
                  "index": "/inoculation/BirthQueueRpt_query"
                },
                {
                  "supId": "1010011160",
                  "updateDate": "2022-07-16 12:34:56",
                  "moduleCode": "M06004",
                  "moduleShortName": "国家免疫规划常规免疫出生队列接种率报表（横表）",
                  "moduleName": "国家免疫规划常规免疫出生队列接种率报表（横表）",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "国家免疫规划常规免疫出生队列接种率报表（横表）",
                  "containOptions": "6",
                  "id": "1010011177",
                  "moduleUrl": "inoculation/BirthQueueAreaRpt_query",
                  "onOff": "1",
                  "index": "/inoculation/BirthQueueAreaRpt_query"
                }
              ],
              "supId": "102",
              "updateDate": "2022-07-06 12:34:56",
              "moduleCode": "M06000",
              "moduleShortName": "国家接种监测",
              "moduleExplain": "国家接种监测",
              "moduleName": "国家接种监测",
              "updateUser": "集成超级管理员",
              "id": "1010011160",
              "tableStatus": "0",
              "onOff": "1",
              "index": "1010011160"
            },
            {
              "supModules": [
                {
                  "supModules": [],
                  "supId": "4658",
                  "updateDate": "2021-07-26 14:41:13",
                  "moduleCode": "M0801",
                  "moduleShortName": "扫码接种率报表",
                  "moduleName": "扫码接种率报表",
                  "updateUser": "超级管理员",
                  "containOptions": "6",
                  "id": "4659",
                  "tableStatus": "0",
                  "moduleUrl": "childrendata/scan",
                  "onOff": "1",
                  "index": "/childrendata/scan"
                },
                {
                  "supModules": [],
                  "supId": "4658",
                  "updateDate": "2021-07-26 14:41:13",
                  "moduleCode": "M0802",
                  "moduleShortName": "电子监管码扫码接种统计",
                  "moduleName": "电子监管码扫码接种统计",
                  "updateUser": "超级管理员",
                  "containOptions": "6",
                  "id": "4660",
                  "tableStatus": "0",
                  "moduleUrl": "childrendata/dzjgm",
                  "onOff": "1",
                  "index": "/childrendata/dzjgm"
                }
              ],
              "supId": "102",
              "updateDate": "2021-07-26 14:41:13",
              "moduleCode": "M08",
              "moduleShortName": "扫码接种率报表",
              "moduleName": "扫码接种率报表",
              "updateUser": "超级管理员",
              "containOptions": "6",
              "id": "4658",
              "tableStatus": "0",
              "onOff": "1",
              "index": "4658"
            },
            {
              "supModules": [
                {
                  "supId": "1010011112",
                  "updateDate": "2020-09-20 16:01:45",
                  "moduleCode": "M1501",
                  "moduleShortName": "档案变化情况统计",
                  "moduleName": "档案变化情况统计",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "档案变化情况统计",
                  "containOptions": "6",
                  "id": "1010011113",
                  "moduleUrl": "gjdc/basechanged",
                  "onOff": "1",
                  "index": "/gjdc/basechanged"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-09-20 16:01:45",
                  "moduleCode": "M1503",
                  "moduleShortName": "有效接种率统计",
                  "moduleName": "有效接种率统计",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "有效接种率统计",
                  "containOptions": "6",
                  "id": "1010011115",
                  "moduleUrl": "gjdc/baseeffect",
                  "onOff": "1",
                  "index": "/gjdc/baseeffect"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-09-20 16:01:45",
                  "moduleCode": "M1504",
                  "moduleShortName": "不同疫苗剂次间脱漏率统计",
                  "moduleName": "不同疫苗剂次间脱漏率统计",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "不同疫苗剂次间脱漏率统计",
                  "containOptions": "6",
                  "id": "1010011116",
                  "moduleUrl": "gjdc/basediffer",
                  "onOff": "1",
                  "index": "/gjdc/basediffer"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-09-20 16:01:45",
                  "moduleCode": "M1507",
                  "moduleShortName": "设置阈值",
                  "moduleName": "设置阈值",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "设置阈值",
                  "containOptions": "6",
                  "id": "1010011118",
                  "moduleUrl": "gjdc/setThreshold",
                  "onOff": "1",
                  "index": "/gjdc/setThreshold"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-09-20 16:01:45",
                  "moduleCode": "M1508",
                  "moduleShortName": "新冠病毒疫苗分年龄、分企业接种信息汇总表",
                  "moduleName": "新冠病毒疫苗分年龄、分企业接种信息汇总表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新冠病毒疫苗分年龄、分企业接种信息汇总表",
                  "containOptions": "6",
                  "id": "1010011119",
                  "moduleUrl": "gjdc/xgRptAgeAndFenterno",
                  "onOff": "1",
                  "index": "/gjdc/xgRptAgeAndFenterno"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-09-20 16:01:45",
                  "moduleCode": "M1509",
                  "moduleShortName": "按人群分类的新冠病毒疫苗接种信息汇总表",
                  "moduleName": "按人群分类的新冠病毒疫苗接种信息汇总表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "按人群分类的新冠病毒疫苗接种信息汇总表",
                  "containOptions": "6",
                  "id": "1010011120",
                  "moduleUrl": "gjdc/xgRptCrowdkind",
                  "onOff": "1",
                  "index": "/gjdc/xgRptCrowdkind"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2021-01-28 16:10:05",
                  "moduleCode": "M1511",
                  "moduleShortName": "按职业分类的新冠病毒疫苗接种信息汇总表",
                  "moduleName": "按职业分类的新冠病毒疫苗接种信息汇总表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "按职业分类的新冠病毒疫苗接种信息汇总表",
                  "containOptions": "6",
                  "id": "1010011122",
                  "moduleUrl": "gjdc/xgRptProfession",
                  "onOff": "1",
                  "index": "/gjdc/xgRptProfession"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2021-01-28 16:10:05",
                  "moduleCode": "M1512",
                  "moduleShortName": "新冠病毒疫苗分地区接种信息汇总表",
                  "moduleName": "新冠病毒疫苗分地区接种信息汇总表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新冠病毒疫苗分地区接种信息汇总表",
                  "containOptions": "6",
                  "id": "1010011123",
                  "moduleUrl": "gjdc/xgRptArea_query",
                  "onOff": "1",
                  "index": "/gjdc/xgRptArea_query"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-12-17 09:00:00",
                  "moduleCode": "M1514",
                  "moduleShortName": "新型冠状病毒疫苗接种信息汇总表",
                  "moduleName": "新型冠状病毒疫苗接种信息汇总表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新型冠状病毒疫苗接种信息汇总表",
                  "containOptions": "6",
                  "id": "1010011127",
                  "moduleUrl": "gjdc/xgRptJzjlInfo_query",
                  "onOff": "1",
                  "index": "/gjdc/xgRptJzjlInfo_query"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-09-20 16:01:45",
                  "moduleCode": "M1517",
                  "moduleShortName": "新冠病毒疫苗到期未种统计表",
                  "moduleName": "新冠病毒疫苗到期未种统计表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新冠病毒疫苗到期未种统计表",
                  "containOptions": "6",
                  "id": "1010011133",
                  "moduleUrl": "gjdc/xgRptWz_query",
                  "onOff": "1",
                  "index": "/gjdc/xgRptWz_query"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-09-20 16:01:45",
                  "moduleCode": "M1518",
                  "moduleShortName": "新冠病毒疫苗免疫程序完成情况统计表",
                  "moduleName": "新冠病毒疫苗免疫程序完成情况统计表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新冠病毒疫苗免疫程序完成情况统计表",
                  "containOptions": "6",
                  "id": "1010011128",
                  "moduleUrl": "gjdc/xgRptFinished_query",
                  "onOff": "1",
                  "index": "/gjdc/xgRptFinished_query"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-09-20 16:01:45",
                  "moduleCode": "M1519",
                  "moduleShortName": "新冠病毒疫苗基本信息质量控制报表",
                  "moduleName": "新冠病毒疫苗基本信息质量控制报表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新冠病毒疫苗基本信息质量控制报表",
                  "containOptions": "6",
                  "id": "1010011129",
                  "moduleUrl": "gjdc/xgRptZlkz_infofull",
                  "onOff": "1",
                  "index": "/gjdc/xgRptZlkz_infofull"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-09-20 16:01:45",
                  "moduleCode": "M1520",
                  "moduleShortName": "新冠病毒疫苗接种记录质量控制报表",
                  "moduleName": "新冠病毒疫苗接种记录质量控制报表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新冠病毒疫苗接种记录质量控制报表",
                  "containOptions": "6",
                  "id": "1010011130",
                  "moduleUrl": "gjdc/xgRptZlkz_jzjlfull",
                  "onOff": "1",
                  "index": "/gjdc/xgRptZlkz_jzjlfull"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2021-11-04 17:00:00",
                  "moduleCode": "M1522",
                  "moduleShortName": "按月提供新冠疫苗数据",
                  "moduleName": "按月提供新冠疫苗数据",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "按月提供新冠疫苗数据",
                  "containOptions": "6",
                  "id": "1010011135",
                  "moduleUrl": "gjdc/xgRptJzjlInfoByMonth_query",
                  "onOff": "1",
                  "index": "/gjdc/xgRptJzjlInfoByMonth_query"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-11-03 16:08:33",
                  "moduleCode": "M1524",
                  "moduleShortName": "国家数据上传监测统计表",
                  "moduleName": "国家数据上传监测统计表",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "国家数据上传监测统计表",
                  "containOptions": "6",
                  "id": "213",
                  "moduleUrl": "gjdc/gjUploadCheckRpt_query",
                  "onOff": "1",
                  "index": "/gjdc/gjUploadCheckRpt_query"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-11-03 16:08:33",
                  "moduleCode": "M1525",
                  "moduleShortName": "国家数据上传错误统计表",
                  "moduleName": "国家数据上传错误统计表",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "国家数据上传错误统计表",
                  "containOptions": "6",
                  "id": "214",
                  "moduleUrl": "gjdc/gjUploadWrongRpt_query",
                  "onOff": "1",
                  "index": "/gjdc/gjUploadWrongRpt_query"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2020-11-03 16:08:33",
                  "moduleCode": "M1526",
                  "moduleShortName": "国家数据不合格未上传统计表",
                  "moduleName": "国家数据不合格未上传统计表",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "国家数据不合格未上传统计表",
                  "containOptions": "6",
                  "id": "215",
                  "moduleUrl": "gjdc/gjNoPackageRpt_query",
                  "onOff": "1",
                  "index": "/gjdc/gjNoPackageRpt_query"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2022-06-27 09:29:09",
                  "moduleCode": "M1528",
                  "moduleShortName": "各地不同年龄段新冠病毒疫苗接种人数汇总表",
                  "moduleName": "各地不同年龄段新冠病毒疫苗接种人数汇总表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "各地不同年龄段新冠病毒疫苗接种人数汇总表",
                  "containOptions": "6",
                  "id": "1010011147",
                  "moduleUrl": "gjdc/xgRptJzrs_query",
                  "onOff": "1",
                  "index": "/gjdc/xgRptJzrs_query"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2022-09-29 12:34:56",
                  "moduleCode": "M1530",
                  "moduleShortName": "流感疫苗接种情况统计表",
                  "moduleName": "流感疫苗接种情况统计表",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "流感疫苗接种情况统计表",
                  "containOptions": "6",
                  "id": "1010011149",
                  "moduleUrl": "gjdc/lgRptJzjl_query",
                  "onOff": "1",
                  "index": "/gjdc/lgRptJzjl_query"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2022-09-29 12:34:56",
                  "moduleCode": "M1531",
                  "moduleShortName": "新冠疫苗加强第2针分年龄汇总表（vero）",
                  "moduleName": "新冠疫苗加强第2针分年龄汇总表（vero）",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新冠疫苗加强第2针分年龄汇总表（vero）",
                  "containOptions": "6",
                  "id": "1010011164",
                  "moduleUrl": "gjdc/xgRptAgeJq_veroQuery",
                  "onOff": "1",
                  "index": "/gjdc/xgRptAgeJq_veroQuery"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2022-09-29 12:34:56",
                  "moduleCode": "M1532",
                  "moduleShortName": "新冠疫苗加强第2针分年龄汇总表（智飞）",
                  "moduleName": "新冠疫苗加强第2针分年龄汇总表（智飞）",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新冠疫苗加强第2针分年龄汇总表（智飞）",
                  "containOptions": "6",
                  "id": "1010011165",
                  "moduleUrl": "gjdc/xgRptAgeJq_zfQuery",
                  "onOff": "1",
                  "index": "/gjdc/xgRptAgeJq_zfQuery"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2022-09-29 12:34:56",
                  "moduleCode": "M1533",
                  "moduleShortName": "新冠疫苗加强第2针分年龄汇总表（丽珠、三叶）",
                  "moduleName": "新冠疫苗加强第2针分年龄汇总表（丽珠、三叶）",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新冠疫苗加强第2针分年龄汇总表（丽珠、三叶）",
                  "containOptions": "6",
                  "id": "1010011166",
                  "moduleUrl": "gjdc/xgRptAgeJq_lsQuery",
                  "onOff": "1",
                  "index": "/gjdc/xgRptAgeJq_lsQuery"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2022-09-29 12:34:56",
                  "moduleCode": "M1534",
                  "moduleShortName": "新冠疫苗加强第2针分年龄汇总表（康希诺）",
                  "moduleName": "新冠疫苗加强第2针分年龄汇总表（康希诺）",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新冠疫苗加强第2针分年龄汇总表（康希诺）",
                  "containOptions": "6",
                  "id": "1010011167",
                  "moduleUrl": "gjdc/xgRptAgeJq_kxnQuery",
                  "onOff": "1",
                  "index": "/gjdc/xgRptAgeJq_kxnQuery"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2022-09-29 12:34:56",
                  "moduleCode": "M1535",
                  "moduleShortName": "新冠疫苗加强第2针分年龄汇总表（威斯克、神州、万泰）",
                  "moduleName": "新冠疫苗加强第2针分年龄汇总表（威斯克、神州、万泰）",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新冠疫苗加强第2针分年龄汇总表（威斯克、神州、万泰）",
                  "containOptions": "6",
                  "id": "1010011168",
                  "moduleUrl": "gjdc/xgRptAgeJq_wswQuery",
                  "onOff": "1",
                  "index": "/gjdc/xgRptAgeJq_wswQuery"
                },
                {
                  "supId": "1010011112",
                  "updateDate": "2013-01-04 12:34:56",
                  "moduleCode": "M1566",
                  "moduleShortName": "新冠疫苗分年龄接种情况",
                  "moduleName": "新冠疫苗分年龄接种情况",
                  "updateUser": "集成超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "新冠疫苗分年龄接种情况",
                  "containOptions": "6",
                  "id": "1010011176",
                  "moduleUrl": "gjdc/xgRptAgeShot_query",
                  "onOff": "1",
                  "index": "/gjdc/xgRptAgeShot_query"
                }
              ],
              "supId": "102",
              "updateDate": "2020-09-20 16:01:45",
              "moduleCode": "M154",
              "moduleShortName": "国家督察查询",
              "moduleExplain": "国家督察查询报表",
              "moduleName": "国家督察查询",
              "updateUser": "集成超级管理员",
              "containOptions": "6",
              "id": "1010011112",
              "tableStatus": "0",
              "onOff": "1",
              "index": "1010011112"
            },
            {
              "supModules": [
                {
                  "supModules": [],
                  "supId": "150",
                  "updateDate": "2019-01-20 16:01:45",
                  "moduleCode": "M2001",
                  "moduleShortName": "数据汇总",
                  "moduleExplain": "数据汇总",
                  "moduleName": "数据汇总",
                  "updateUser": "超级管理员",
                  "id": "151",
                  "tableStatus": "0",
                  "moduleUrl": "datawash/initData",
                  "onOff": "1",
                  "index": "/datawash/initData"
                },
                {
                  "supId": "150",
                  "updateDate": "2019-01-20 16:01:45",
                  "moduleCode": "M2002",
                  "moduleShortName": "重复儿童推送",
                  "moduleName": "重复儿童推送",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "重复儿童推送",
                  "containOptions": "6",
                  "id": "152",
                  "moduleUrl": "datawash/taskStart",
                  "onOff": "1",
                  "index": "/datawash/taskStart"
                },
                {
                  "supId": "150",
                  "updateDate": "2019-01-20 16:01:45",
                  "moduleCode": "M2003",
                  "moduleShortName": "督导查询",
                  "moduleName": "督导查询",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "督导查询",
                  "containOptions": "6",
                  "id": "153",
                  "moduleUrl": "datawash/taskQuery",
                  "onOff": "1",
                  "index": "/datawash/taskQuery"
                },
                {
                  "supId": "150",
                  "updateDate": "2019-01-20 16:01:45",
                  "moduleCode": "M2004",
                  "moduleShortName": "重复档案处理后档案查询",
                  "moduleName": "重复档案处理后档案查询",
                  "updateUser": "超级管理员",
                  "tableStatus": "0",
                  "supModules": [],
                  "moduleExplain": "重复档案处理后档案查询",
                  "containOptions": "6",
                  "id": "154",
                  "moduleUrl": "datawash/taskStat",
                  "onOff": "1",
                  "index": "/datawash/taskStat"
                }
              ],
              "supId": "102",
              "updateDate": "2019-01-20 16:01:45",
              "moduleCode": "M2000",
              "moduleShortName": "重复档案处理",
              "moduleExplain": "重复档案处理",
              "moduleName": "重复档案处理",
              "updateUser": "超级管理员",
              "id": "150",
              "tableStatus": "0",
              "onOff": "1",
              "index": "150"
            }
          ],
          "supId": "0",
          "updateDate": "2021-09-18 10:46:39",
          "moduleCode": "M00",
          "moduleShortName": "预防接种单位管理",
          "moduleExplain": "预防接种单位管理",
          "moduleName": "预防接种单位管理",
          "updateUser": "超级管理员1",
          "containOptions": "6",
          "id": "102",
          "tableStatus": "0",
          "onOff": "1",
          "index": "102"
        },
        {
          "supModules": [
            {
              "supId": "35",
              "updateDate": "2009-11-18 14:18:18",
              "moduleCode": "M0601",
              "moduleShortName": "受种者列表显示设置",
              "moduleName": "受种者列表显示设置",
              "updateUser": "超级管理员",
              "tableStatus": "0",
              "supModules": [],
              "moduleExplain": "受种者列表显示设置",
              "containOptions": "2",
              "id": "36",
              "moduleUrl": "usersetting/openChildrenSetting",
              "onOff": "1",
              "index": "/usersetting/openChildrenSetting"
            }
          ],
          "supId": "0",
          "updateDate": "2015-06-17 13:45:34",
          "moduleCode": "M06",
          "moduleShortName": "用户设置",
          "moduleExplain": "用户设置",
          "moduleName": "用户设置",
          "updateUser": "超级管理员",
          "id": "35",
          "tableStatus": "0",
          "onOff": "1",
          "index": "35"
        },
        {
          "supModules": [
            {
              "supId": "38",
              "updateDate": "2016-01-20 09:27:12",
              "moduleCode": "M0701",
              "moduleShortName": "地区管理",
              "moduleName": "地区管理",
              "updateUser": "超级管理员",
              "tableStatus": "0",
              "supModules": [],
              "moduleExplain": "地区管理",
              "containOptions": "123",
              "id": "39",
              "moduleUrl": "systemsetting/showAdministrativeQuery",
              "onOff": "1",
              "index": "/systemsetting/showAdministrativeQuery"
            },
            {
              "supId": "38",
              "updateDate": "2016-01-20 08:58:26",
              "moduleCode": "M07012",
              "moduleShortName": "公告板管理",
              "moduleName": "公告板管理",
              "updateUser": "超级管理员",
              "tableStatus": "0",
              "supModules": [],
              "moduleExplain": "公告板管理",
              "containOptions": "1236",
              "id": "49",
              "moduleUrl": "systemsetting/openBBSQueryPage",
              "onOff": "1",
              "index": "/systemsetting/openBBSQueryPage"
            },
            {
              "supId": "38",
              "updateDate": "2009-11-18 14:18:18",
              "moduleCode": "M07015",
              "moduleShortName": "删除某接种单位数据",
              "moduleName": "删除某接种单位数据",
              "updateUser": "超级管理员",
              "tableStatus": "0",
              "supModules": [],
              "moduleExplain": "删除某接种单位数据",
              "containOptions": "3",
              "id": "52",
              "moduleUrl": "systemsetting/showInoculationUnitDel",
              "onOff": "1",
              "index": "/systemsetting/showInoculationUnitDel"
            },
            {
              "supModules": [],
              "supId": "38",
              "updateDate": "2009-11-18 14:18:18",
              "moduleCode": "M07016",
              "moduleShortName": "系统日志查看",
              "moduleExplain": "系统日志查看",
              "moduleName": "系统日志查看",
              "updateUser": "超级管理员",
              "id": "53",
              "tableStatus": "0",
              "moduleUrl": "systemsetting/openSysLogQuery",
              "onOff": "1",
              "index": "/systemsetting/openSysLogQuery"
            },
            {
              "supId": "38",
              "updateDate": "2016-01-19 16:33:05",
              "moduleCode": "M0702",
              "moduleShortName": "接种单位管理",
              "moduleName": "接种单位管理",
              "updateUser": "超级管理员",
              "tableStatus": "0",
              "supModules": [],
              "moduleExplain": "接种单位管理",
              "containOptions": "1236",
              "id": "40",
              "moduleUrl": "systemsetting/showInoculationUnitQuery",
              "onOff": "1",
              "index": "/systemsetting/showInoculationUnitQuery"
            },
            {
              "supModules": [],
              "supId": "38",
              "updateDate": "2011-04-14 15:41:13",
              "moduleCode": "M07021",
              "moduleShortName": "登录用户查询",
              "moduleExplain": "登录用户查询",
              "moduleName": "登录用户查询",
              "updateUser": "超级管理员",
              "id": "98",
              "tableStatus": "0",
              "moduleUrl": "systemsetting/sysLoginLogAction",
              "onOff": "1",
              "index": "/systemsetting/sysLoginLogAction"
            },
            {
              "supModules": [],
              "supId": "38",
              "updateDate": "2009-11-18 14:18:18",
              "moduleCode": "M0704",
              "moduleShortName": "接种单位迁移",
              "moduleExplain": "接种单位迁移",
              "moduleName": "接种单位迁移",
              "updateUser": "超级管理员",
              "id": "42",
              "tableStatus": "0",
              "moduleUrl": "systemsetting/showInoculationUnitMove",
              "onOff": "1",
              "index": "/systemsetting/showInoculationUnitMove"
            },
            {
              "supModules": [],
              "supId": "38",
              "updateDate": "2009-11-18 14:18:18",
              "moduleCode": "M0705",
              "moduleShortName": "接种单位合并",
              "moduleExplain": "接种单位合并",
              "moduleName": "接种单位合并",
              "updateUser": "超级管理员",
              "id": "43",
              "tableStatus": "0",
              "moduleUrl": "systemsetting/showInoculationUnitMerge",
              "onOff": "1",
              "index": "/systemsetting/showInoculationUnitMerge"
            },
            {
              "supId": "38",
              "updateDate": "2009-11-18 14:18:18",
              "moduleCode": "M0707",
              "moduleShortName": "系统角色管理",
              "moduleName": "系统角色管理",
              "updateUser": "超级管理员",
              "tableStatus": "0",
              "supModules": [],
              "moduleExplain": "系统角色管理",
              "containOptions": "123",
              "id": "45",
              "moduleUrl": "systemsetting/showSysRole",
              "onOff": "1",
              "index": "/systemsetting/showSysRole"
            },
            {
              "supId": "38",
              "updateDate": "2016-03-25 14:38:35",
              "moduleCode": "M0708",
              "moduleShortName": "系统用户管理",
              "moduleName": "系统用户管理",
              "updateUser": "超级管理员",
              "tableStatus": "0",
              "supModules": [],
              "moduleExplain": "系统用户管理",
              "containOptions": "1236",
              "id": "46",
              "moduleUrl": "systemsetting/showSysUserQuery",
              "onOff": "1",
              "index": "/systemsetting/showSysUserQuery"
            },
            {
              "supModules": [],
              "supId": "38",
              "updateDate": "2021-09-10 11:08:22",
              "moduleCode": "M0740",
              "moduleShortName": "产院接种单位管理",
              "moduleExplain": "产院接种单位管理",
              "moduleName": "产院接种单位管理",
              "updateUser": "超级管理员",
              "id": "4040",
              "tableStatus": "0",
              "moduleUrl": "systemsetting/showCyInoculationUnitQuery",
              "onOff": "1",
              "index": "/systemsetting/showCyInoculationUnitQuery"
            },
            {
              "supModules": [],
              "supId": "38",
              "updateDate": "2023-09-05 09:38:13",
              "moduleCode": "M0741",
              "moduleShortName": "完整性校验日志",
              "moduleExplain": "完整性校验日志",
              "moduleName": "完整性校验日志",
              "updateUser": "超级管理员",
              "id": "99",
              "tableStatus": "0",
              "moduleUrl": "systemsetting/encryptLogAction",
              "onOff": "1",
              "index": "/systemsetting/encryptLogAction"
            }
          ],
          "supId": "0",
          "updateDate": "2015-06-15 14:55:24",
          "moduleCode": "M07",
          "moduleShortName": "系统管理",
          "moduleExplain": "系统管理",
          "moduleName": "系统管理",
          "updateUser": "超级管理员",
          "id": "38",
          "tableStatus": "0",
          "onOff": "1",
          "index": "38"
        },
        {
          "supModules": [
            {
              "supId": "68",
              "updateDate": "2016-03-21 11:09:43",
              "moduleCode": "M07011",
              "moduleShortName": "统计规则管理",
              "moduleName": "统计规则管理",
              "updateUser": "超级管理员",
              "tableStatus": "0",
              "supModules": [],
              "moduleExplain": "统计规则管理",
              "containOptions": "1236",
              "id": "4652",
              "moduleUrl": "systemsetting/openVaccineRule",
              "onOff": "1",
              "index": "/systemsetting/openVaccineRule"
            }
          ],
          "supId": "0",
          "updateDate": "2016-08-09 16:26:10",
          "moduleCode": "M070",
          "moduleShortName": "系统设置",
          "moduleExplain": "系统设置",
          "moduleName": "系统设置",
          "updateUser": "超级管理员",
          "id": "68",
          "tableStatus": "0",
          "onOff": "1",
          "index": "68"
        }
      ],
      "token": "9e59893a75105d6a583fe852240f18ab"
    },
    // "status": 200,
    // "statusText": "",
    // "headers": {
    //   "content-type": "application/json"
    // },
    // "config": {
    //   "url": "http://localhost:9999/ningXiaMianYi/user/login",
    //   "method": "post",
    //   "data": "params=b6c902facb35c3e38f8aecb15f223856f32a014d61a4148b1ddfc04a02d425cf493870f97dc35e45ac2519fa9bfdc9a4e31026a6a32d07c7de2f54b48b14c175924129cf919ca14bad4148aa64ec2a3d3ecc53d06420197c20fb3386a0288eb2f85674b9984fbd3e6c65964eb1600d23",
    //   "headers": {
    //     "Accept": "application/json, text/plain, */*",
    //     "Content-Type": "application/x-www-form-urlencoded"
    //   },
    //   "transformRequest": [
    //     null
    //   ],
    //   "transformResponse": [
    //     null
    //   ],
    //   "timeout": 0,
    //   "withCredentials": true,
    //   "xsrfCookieName": "XSRF-TOKEN",
    //   "xsrfHeaderName": "X-XSRF-TOKEN",
    //   "maxContentLength": -1,
    //   "maxBodyLength": -1,
    //   "transitional": {
    //     "silentJSONParsing": true,
    //     "forcedJSONParsing": true,
    //     "clarifyTimeoutError": false
    //   }
    // },
    // "request": {}
  }
  store.userInfo = res.data.userinfo
  store.currentLevel = String(Number(getLevel(res.data.userinfo.areaCode)))
  sessionStorage.setItem("currentLevel", store.currentLevel);
  sessionStorage.setItem("username", res.data.userinfo.loginName);
  sessionStorage.setItem("userInfo", JSON.stringify(res.data.userinfo));
  sessionStorage.setItem("token", res.data.token);
  sessionStorage.setItem("roleModules", JSON.stringify(res.data.roleModules));
  store.setToken(res.data.token)
  recursiveRes(res.data.modules)
  store.setMenus(res.data.modules);
  areaTree()
  nextTick(() => {
    ElMessage.success("登录成功");
    router.push("/index");
  })

  // if (isAble.value) return false
  // if (!form.valid || form.valid.length != 4) {
  //   isAble.value = false
  //   ElMessage.error("验证码为4位数！");
  //   return
  // }
  // isAble.value = true
  // loginRef.value.validate(valid => {
  //   if (valid) {
  //     let params = {
  //       userName: form.name,
  //       password: form.pass ? md5(form.pass) : '',
  //       validCode: form.valid
  //     }
  //     isAble.value = false
  //     login(params).then(res => {
  //       if (res) {
  //         store.userInfo = res.data.userinfo
  //         store.currentLevel = String(Number(getLevel(res.data.userinfo.areaCode)))
  //         sessionStorage.setItem("currentLevel", store.currentLevel);
  //         sessionStorage.setItem("username", res.data.userinfo.loginName);
  //         sessionStorage.setItem("userInfo", JSON.stringify(res.data.userinfo));
  //         sessionStorage.setItem("token", res.data.token);
  //         sessionStorage.setItem("roleModules", JSON.stringify(res.data.roleModules));
  //         store.setToken(res.data.token)
  //         recursiveRes(res.data.modules)
  //         store.setMenus(res.data.modules);
  //         areaTree()
  //         ElMessage.success("登录成功");
  //         router.push("/index");
  //       } else {
  //         ElMessage.error("登录失败");
  //         getVerifyImg()
  //       }
  //     }).catch(res => {
  //       ElMessage.error("登录失败");
  //       getVerifyImg()
  //     })
  //   } else {
  //     getVerifyImg()
  //     ElMessage.error("登录失败");
  //     return false;
  //   }
  // })
}
// 处理菜单路径的递归
const recursiveRes = (list) => {
  for (let i = 0; i < list.length; i++) {
    if (list[i].supModules.length > 0) {
      list[i].index = list[i].id
      let supList = list[i].supModules
      for (let j = 0; j < supList.length; j++) {
        let item = supList[j]
        item.index = '/' + item.moduleUrl
      }
      recursiveRes(list[i].supModules)
    }
  }
}
const getVerifyImg = () => {
  let param = { jessionid: sessionid.value }
  time.value = new Date().getTime()
  form.valid = ''
  cnt.value = 60
  img_src.value = BASE_URL + '/user/getVerifyImg?' + time.value + '&params=' + globalProxy.$util.sm4(JSON.stringify(param))
}

const getSessionidFun = () => {
  getSessionid().then(res => {
    sessionid.value = sessionStorage.getItem('sessionid')
    getVerifyImg()
    setInterval(function () {
      cnt.value--
      if (cnt.value <= 0) {
        getVerifyImg()
      }
    }, 1000)
  })
    .catch(res => {
      getVerifyImg()
    })
}

onMounted(() => {
  getSessionidFun()
})
</script>

<style scoped lang='scss'>
.mt_50 {
  margin-top: 50px;
}

.login-page {
  height: 100%;
  padding: 0;
}

.login-left {
  background-image: url(@/assets/img/login/login1.png);
  background-size: contain;
  background-position: left bottom;
  background-repeat: no-repeat;
  position: absolute;
  bottom: 0px;
  width: 60%;
  height: 88%;
}

.login-right {
  background-image: url(@/assets/img/login/login2.png);
  background-size: contain;
  background-position: right top;
  background-repeat: no-repeat;
  position: absolute;
  width: 311px;
  height: 185px;
  right: 0;
}

.login-btn {
  background-image: url(@/assets/img/login/login3.png);
  background-size: contain;
  background-position: center bottom;
  background-repeat: no-repeat;
  width: 402px;
  height: 56px;
  cursor: pointer;
}

.login-form {
  position: absolute;
  margin: 0 20% 8% 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  text-align: center;
  display: flex;
  justify-content: center;
}

.valid {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;

  .input {
    width: 270px;
  }

  .verifyImg {
    height: 40px;
  }
}

.login-title {
  font-size: 33px;
  font-weight: bolder;
  letter-spacing: 3px;
  text-align: center;
}

.login-footer {
  width: 402px;
  position: absolute;
  right: 0;
  bottom: 30px;
  text-align: center;
  font-size: 12px;
  margin-right: 20%;

  div:first-child {
    color: #1E6EC4;
    line-height: 30px;
  }

  div:last-child {
    color: #C8C8C8;
  }
}
</style>
<style>
.login-form .el-input__inner {
  border-radius: 10px !important;
  height: 50px !important;
  line-height: 50px !important;
}
</style>