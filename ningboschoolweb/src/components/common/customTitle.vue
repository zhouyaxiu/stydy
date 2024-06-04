<template>
  <!-- 自定义页面标题-->
  <div class="icon-block" :class="type">
    <span class="icon"><i :class="'icon-'+ type"></i></span>
    <span class="text">{{ text }}</span>
  </div>
</template>

<script>
export default {
  name: 'customTitle',
  data () {
    return {}
  },
  props: {
    type: {
      type: String,
      required: true
    },
    text: {
      type: String,
      required: true
    }
  },
  methods: {}
}
</script>

<style scoped lang="less">
.btn-util-cls(){
  background-size: contain;
  background-position: left center;
  width: 14px;
  height: 14px;
}
.icon-block{
  display: flex;
  align-items: center;
  width: 128px;
  height: 26px;
  border-radius: 20px;
  margin-bottom: 16px;
}
.icon{
  width: 26px;
  height: 26px;
  display: flex;
  border-radius: 50%;
  border: 1px solid #FFFFFF;
  align-items: center;
  justify-content: center;
}
.text{
  font-size: 16px;
  color: #FFFFFF;
  margin-left: 8px;
  line-height: 26px;
}
.info{
  background: linear-gradient(90deg, #2E5BFF 0%, #ACB2FF 100%);
  .icon{
    background: #2E5BFF;
  }
}
.icon-info {
  background: url(../../assets/img/icon/icon_info.png) left top no-repeat;
  .btn-util-cls
}
.record{
  background: linear-gradient(90deg, #13B98C 0%, #AFEFFF 100%);
  .icon{
    background: #13B98C;
  }
}
.icon-record {
  background: url(../../assets/img/icon/icon_record.png) left top no-repeat;
  .btn-util-cls
}
.instead{
  background: linear-gradient(90deg, #8778FD 0%, #F4CFFF 100%);
  .icon{
    background: #8778FD;
  }
}
.icon-instead {
  background: url(../../assets/img/icon/icon_instead.png) left top no-repeat;
  .btn-util-cls
}
</style>
