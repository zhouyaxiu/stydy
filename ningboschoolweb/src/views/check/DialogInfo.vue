<template>
    <el-dialog
      title="受种者明细"
      :visible.sync="visible"
      width="60%"
      :before-close="close"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <div class="areaInfoDialog" :style="{ height: height - 50 + 'px' }" style="overflow-y: auto;overflow-x: hidden;">
        <el-row :gutter="20">
          <el-col :span="8">
            <ss-custom-title type="info" text="基本信息"></ss-custom-title>
            <custom-descriptions :items="baseList" style="overflow:scroll"></custom-descriptions>
          </el-col>
          <el-col :span="8">
            <ss-custom-title type="record" text="接种记录"></ss-custom-title>
            <custom-descriptions :items="jzList" style="overflow:scroll"></custom-descriptions>
          </el-col>
          <el-col :span="8">
            <ss-custom-title type="instead" text="替代疫苗"></ss-custom-title>
            <custom-descriptions :items="tdList" style="overflow:scroll"></custom-descriptions>
          </el-col>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
      </span>
    </el-dialog>
  </template>
  <script>
    import { getOneStudent } from '@/utils/axios/apis/check'
    import CustomDescriptions from "@/components/common/customDescriptions.vue"
    export default {
      name: 'receptionInfoDialog',
      components: { CustomDescriptions },
      data () {
        return {
          editorVisible: false,
          height: window.winHeight - 15 / window.scale - 500,
          baseList: [
            { name: '受种者预防接种档案编号', code: 'stuNo', value: '' },
            { name: '受种者信息介质识别码', code: 'stuImuno', value: '' },
            { name: '受种者姓名', code: 'stuName', value: '' },
            { name: '出生日期', code: 'stuBirth', value: '' },
            { name: '所属预防接种单位', code: 'stuStationname', value: '' },
            { name: '上次入托/入学扫码日期', code: 'lastStuSmrq', value: '' },
            { name: '上次入托/入学扫码学校', code: 'lastStuSchname', value: '' },
            { name: '性别', code: 'stuSex', value: '' },
            { name: '户籍类型', code: 'stuType', value: '' },
            { name: '有无接种证', code: 'stuIsjzz', value: '' },
            { name: '父亲/母亲/监护人姓名', code: 'stuParentname', value: '' },
            { name: '数据来源', code: 'stuSourceid', value: '' },
            { name: '轮次', code: 'stuSeq', value: '' },
          ],
          jzList: [
            { name: '乙肝1', code: 'B063', value: '' },
            { name: '乙肝2', code: 'B064', value: '' },
            { name: '乙肝3', code: 'B065', value: '' },
            { name: '甲肝减毒1', code: 'B069', value: '' },
            { name: '脊灰减毒1', code: 'B009', value: '' },
            { name: '脊灰减毒2', code: 'B010', value: '' },
            { name: '脊灰减毒3', code: 'B011', value: '' },
            { name: '脊灰减毒4', code: 'B012', value: '' },
            { name: '百白破1', code: 'B015', value: '' },
            { name: '百白破2', code: 'B016', value: '' },
            { name: '百白破3', code: 'B017', value: '' },
            { name: '百白破4', code: 'B018', value: '' },
            { name: '白破1', code: 'B037', value: '' },
            { name: '麻疹1', code: 'B059', value: '' },
            { name: '麻疹2', code: 'B060', value: '' },
            { name: 'A群流脑1', code: 'B040', value: '' },
            { name: 'A群流脑2', code: 'B041', value: '' },
            { name: 'A+C群流脑1', code: 'B045', value: '' },
            { name: 'A+C群流脑2', code: 'B046', value: '' },
            { name: '乙脑减毒1', code: 'B032', value: '' },
            { name: '乙脑减毒2', code: 'B033', value: '' }
          ],
          tdList: [
            { name: '脊灰灭活1', code: 'B169', value: '' },
            { name: '脊灰灭活2', code: 'B170', value: '' },
            { name: '脊灰灭活3', code: 'B171', value: '' },
            { name: '脊灰灭活4', code: 'B172', value: '' },
            { name: '五联苗1', code: 'B163', value: '' },
            { name: '五联苗2', code: 'B164', value: '' },
            { name: '五联苗3', code: 'B165', value: '' },
            { name: '五联苗4', code: 'B166', value: '' },
            { name: '四联苗1', code: 'B157', value: '' },
            { name: '四联苗2', code: 'B158', value: '' },
            { name: '四联苗3', code: 'B159', value: '' },
            { name: '四联苗4', code: 'B160', value: '' },
            { name: '乙脑灭活1', code: 'B027', value: '' },
            { name: '乙脑灭活2', code: 'B028', value: '' },
            { name: '乙脑灭活3', code: 'B029', value: '' },
            { name: '乙脑灭活4', code: 'B030', value: '' },
            { name: 'AC流脑Hib联合1', code: 'B427', value: '' },
            { name: 'AC流脑Hib联合2', code: 'B428', value: '' },
            { name: 'AC流脑Hib联合3', code: 'B429', value: '' },
            { name: 'A+C结合流脑1', code: 'B138', value: '' },
            { name: 'A+C结合流脑2', code: 'B139', value: '' },
            { name: 'A+C结合流脑3', code: 'B140', value: '' },
            { name: 'A+C结合流脑4', code: 'B141', value: '' },
            { name: 'ACYW流脑1', code: 'B147', value: '' },
            { name: 'ACYW流脑2', code: 'B148', value: '' },
            { name: '甲肝灭活1', code: 'B073', value: '' },
            { name: '甲肝灭活2', code: 'B074', value: '' },
            { name: '甲乙肝1', code: 'B133', value: '' },
            { name: '甲乙肝2', code: 'B327', value: '' },
            { name: '麻风1', code: 'B093', value: '' },
            { name: '麻风2', code: 'B094', value: '' },
            { name: '麻腮风1', code: 'B089', value: '' },
            { name: '麻腮风2', code: 'B090', value: '' },
            { name: '麻腮1', code: 'B096', value: '' },
            { name: '麻腮2', code: 'B097', value: '' },
            { name: '青少年百白破1', code: 'B155', value: '' }
          ],
        }
      },
      props: {
        stuId: {
          type: String,
          default: ''
        },
        visible: {
          type: Boolean,
          required: true,
          default: false
        },
      },
      watch: {
        stuId: {
          immediate: true,
          handler (val) {
            if (val) this.getDetailInfo()
          }
        }
      },
      methods: {
        getDetailInfo () {
          if (!this.stuId || this.stuId === '') return false
          this.openFullScreen()
          getOneStudent({ stuId:this.stuId })
            .then((res) => {
              if (res && res.data) {
                for(let key in res.data){
                  this.jzList.map(item=>{
                    if(item.code==key){
                      item.value=res.data[key]
                    }
                  })
                  this.baseList.map(item=>{
                    if(item.code==key){
                      item.value=res.data[key]
                      if(item.code=='stuSourceid'){
                        let value=item.value==0?'信息系统提供':item.value==1?'excel导入':'校医录入'
                        item.value=value;
                      }
                      if(item.code=='stuIsjzz'){
                        let value=item.value==0?'有':'无'
                        item.value=value;
                      }
                      if(item.code=='stuSeq'){
                        let value=item.value==1?'春季':'秋季'
                        item.value=value;
                      }
                    }
                  })
                  this.tdList.map(item=>{
                    if(item.code==key){
                      item.value=res.data[key]
                    }
                  })
                }
              } else {
                this.$util.errorMsg('信息被删除或者不存在！')
              }
              this.closeFullScreen()
            })
            .catch(() => {
              this.closeFullScreen()
            })
        },
        close () {
          this.$emit('close', true)
        }
      },
    }
    </script>