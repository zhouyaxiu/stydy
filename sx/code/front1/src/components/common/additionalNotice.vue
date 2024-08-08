<template>
  <!-- 自定义图片图标
    * v-iconType {Boolean}  类型，包括show、top、bottom、hide、yes、tshi、down、redtips
    * v-size {Number}  图标大小
  -->
  <div class="notice">
    <el-descriptions direction="vertical" :column="1" border>
      <el-descriptions-item labelClassName="labelStyle">
        <template slot="label">
          <span class="title">{{title}}</span>
          <span class="info"><span class="bluetext">蓝色内容</span>展示的是获取的信息的示例，不可编辑</span>
        </template>
        <div style="color:#333;">
          <div><span class="bluetext">麦建邺</span>家长：</div>
          <div class="mt_10">
            <span v-if="afterInfo"><span class="bluetext">向日葵幼儿园</span>学校<span class="bluetext">幼儿园中班中一班</span></span>
            学生（姓名）<span class="bluetext">小麦</span> 出生日期<span class="bluetext">2017-09-19</span>
            家长（姓名）<span class="bluetext">麦建邺 谌美娜</span>，
            <span v-if="maintype==1">
              经仔细查验发现该学生无接种证，请您携带有效证件凭此通知单到
            </span>
            <span v-if="maintype==2">
              经仔细查验预防接种证，发现该学生有下列疫苗未按规定程序完成预防接种，请您携带孩子凭此通知单到
            </span>
            <span v-if="maintype==3">
              经仔细查验发现该学生无接种证，请您携带孩子及有效证件凭此通知单到居住地所在社区卫生服务中心办理建档手续。
            </span>
            <span class="bluetext">健康街道社区卫生服务中心</span>社区卫生服务中心办理补证手续。
          </div>
          <slot name="customcontent" v-if="customcontent"></slot>
          <div class="mt_10" v-if="maintype!=3">社区卫生服务中心地址：<span class="bluetext">健康街道12号</span></div>
          <div v-if="maintype!=3">联系电话：<span class="bluetext">0202-26569893</span></div>
          <div class="mt_20" v-if="maintype!=3"><span class="redtext">红色内容</span>可编辑（建议不超300个字）</div>
          <div class="redtext mt_10 edit" v-if="maintype!=3">
            <el-input
            type="textarea"
            :autosize="{ minRows: 10, maxRows: 10}"
            placeholder=""
            v-model="content"
            resize="none">
            </el-input>
          </div>
          <el-divider></el-divider>
          <div v-if="isZiFei">*为自愿自费疫苗</div>
          <div class="flex"><span>显示学校所在地社区医院 接种单位盖章</span>XXXX年XX月XX日</div>
          <div class="btn" v-if="maintype!=3"><el-button>保存编辑内容</el-button></div>
        </div>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
  
<script>
  export default {
    name: 'icon',
    data () {
      return {
        content:'各位家长到医院补证时请携带有效证件！如有问题请及时联系社区卫生服务中心接种门诊。'
      }
    },
    props: {
      title: {
        type: String,
        required: true
      },
      customcontent: {
        type: Boolean,
        required: false,
      },
      isZiFei: {
        type: Boolean,
        required: false,
      },
      afterInfo: {
        type: Boolean,
        required: false,
      },
      maintype: {
        type: Number|String,
        required: false,
      },
    }
  }
  </script>
  
  <style>
    .notice .btn{
      margin:100px 100px 20px;
      text-align: center;
    }
    .notice .btn .el-button{
      background: #4365e3;
      width:inherit;
      color:#fff;
      border-radius: 16px;
      padding: 0 20px;
      height: 48px;
      line-height: 48px;
      font-size: 16px!important;
    }
    .notice .flex{
      display:flex;font-size:14px;
    }
    .notice .flex span{
      flex:1
    }
    .notice .labelStyle{
      background:#ecf0ff;border-radius:6px 6px 0 0;
    }
    .notice .edit{
      min-height:200px;
    }
    .notice .el-descriptions-item__label,.notice tr:first-child th:last-child{
      height: inherit!important;
      display: flex;
    }
    .notice tr:first-child th:last-child .title{
      flex:1;
      color:#2337d8;
      font-size:16px;
      font-weight:bold;
    }
    .notice tr:first-child th:last-child .info{
      color:#333;
    }
    .notice .edit .el-textarea{
      width:95%;
    }
    .notice .edit .el-textarea textarea{
      border-color:#fff!important;
      color:red;
      text-indent:20px;
    }
    .notice .bluetext{
      color:#4365e3;
      text-decoration: underline;
    }
    .notice .redtext{
      color:red;
    }
  </style>
  