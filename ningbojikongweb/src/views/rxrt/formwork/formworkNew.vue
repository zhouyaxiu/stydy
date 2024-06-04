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
          <div class="maincontent">
            <div><span class="bluetext">XXX</span>家长：</div>
            <div class="mt_10">
              <span v-if="afterInfo || formData.maintype==1"><span class="bluetext">{{ formData.schName }}</span>学校<span class="bluetext">XXXXXX</span></span>
              学生（姓名）<span class="bluetext">XXXX</span> 出生日期<span class="bluetext">XXXX-XX-XX</span>
              家长（姓名）<span class="bluetext">XXX XXX</span>，
              <span v-if="formData.maintype==0">
                经仔细查验预防接种证：发现该学生有下列疫苗未按规定程序完成预防接种，请您携带孩子凭此通知单到
                <span class="bluetext" v-if="!afterInfo">XXXXXXXXXXXXXXXXXXXXXXXXXXX</span>社区卫生服务中心办理补种手续。
                <div class="mt_10" v-if="formData.maintype==0">
                  <el-table
                    :data="tableData"
                    border
                    style="width: 100%">
                    <el-table-column
                      prop="date"
                      type="index"
                      label="序号"
                      width="180">
                    </el-table-column>
                    <el-table-column
                      prop="name"
                      label="疫苗名称"
                      width="180">
                    </el-table-column>
                    <el-table-column
                      prop="address"
                      label="剂次">
                    </el-table-column>
                  </el-table>
                </div>
              </span>
              <!-- <span v-if="formData.maintype==1 && afterInfo">
                经仔细查验发现该学生无接种证，请您携带有效证件凭此通知单到<span class="bluetext">XXXXXXXXXXXXXXXXXX</span>社区卫生服务中心办理补证手续。
              </span> -->
              <!-- <span v-if="formData.maintype==1 && !afterInfo">
                <span class="bluetext">XXXXXXXXXXXXXXXXXXXXXXXXXXX</span>社区卫生服务中心办理补证手续。
              </span> -->
              <!-- <span v-if="formData.maintype!=2">
                <span class="bluetext" v-if="!afterInfo">XXXXXXXXXXXXXXXXXXXXXXXXXXX</span>社区卫生服务中心办理补证手续。
                <span class="bluetext" v-if="afterInfo">XXXXXXXXXXXXXXXXXX</span>社区卫生服务中心办理补证手续。
              </span> -->
            </div>
            <div class="mt_10" v-if="formData.maintype==0">社区卫生服务中心地址：<span class="bluetext">XXXXXX</span></div>
              <div v-if="formData.maintype==0">联系电话：<span class="bluetext">XXXX-XXXXXXXX</span></div>
              <div class="mt_20"><span class="redtext">红色内容</span>可编辑（建议不超300个字）</div>
              <div class="redtext mt_10 edit">
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 6, maxRows: 6}"
                  placeholder=""
                  v-model="formData.content"
                  resize="none"
                  maxlength="300">
                </el-input>
              </div>
          </div>
          <el-divider></el-divider>
          <div v-if="formData.maintype==0">*为自愿自费疫苗</div>
          <div class="flex"><span>XXXXXXXXXXXXXXXXXXXX 接种单位盖章</span>XXXX年XX月XX日</div>
          <slot name="edit"></slot>
          <div class="btn" v-if="formData.maintype!=3" @click="editHandler(formData.maintype)"><el-button>保存编辑内容</el-button></div>
        </div>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
  
<script>
  import { upNoticeMess } from '@/utils/axios/apis/notice/index'
  export default {
    name: 'icon',
    data () {
      return {
        tableData: [{
          name: '乙肝疫苗',
          address: '第一剂次'
        }, {
          name: '*水痘疫苗',
          address: '第一剂次'
        }],
      }
    },
    props: {
      title: {
        type: String,
        required: true
      },
      afterInfo: {
        type: Boolean,
        required: false,
      },
      formData: {
        type: Object,
        default: {}
      }
    },
    methods:{
      editHandler(maintype){
        let params={
          schId:this.formData.schoolId,
          noticeType:maintype | 0,
          noticeContent:this.formData.content,
          operateType:this.$consts.OPTYPE_UPDATE,
        }
        upNoticeMess(params).then(res => {
          this.$message({
            message: res.data || '保存成功!',
            type: 'success'
          })
          this.closeFullScreen()
        }).catch(() => {
          this.closeFullScreen()
        })
      }
    }
  }
  </script>