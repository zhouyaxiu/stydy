<template>
    <div class="shadow-card-outer" :style="'width:'+width+';height:'+height">
        <div class="one-card-side">
            <div class="shadow-card" :style="'width:'+width+';height:'+height+';'+(showBorder?'':'border:0;padding:0;')+(backgroundImg?'background-image:url('+backgroundImg+')':'background-color:'+color)">
                <slot></slot>
            </div>
            <div class="show-shadow" v-show="shadow"></div>
        </div>
        <div class="two-card-side">
            <div class="menu-class">
                <slot name="menu"></slot>
            </div>
            <div class="show-shadow" v-show="shadow"></div>
        </div>
    </div>
</template>

<script>
export default {
  name: 'shadowCard',
  data () {
    return {
    }
  },
  props: {
    width: {
      type: String,
      required: false,
      default: 'calc(100% - 22px)'
      /* default:"calc(100% - 35px)" */
    },
    height: {
      type: String,
      required: false,
      default: 'auto'
    },
    color: {
      type: String,
      required: false,
      default: 'white'
    },
    showBorder: {
      type: Boolean,
      required: false,
      default: true
    },
    shadow: {
      type: Boolean,
      required: false,
      default: false
    },
    backgroundImg: {
      type: String,
      required: false
    }
  }
}
</script>

<style scoped>
    .shadow-card-outer{
        width: 100% !important;
        position: relative;
        overflow: visible;
    }
    .shadow-card{
        position: relative;
        margin: 9px 0;
        border:  1px solid #100d9f;
        line-height: 40px;
        padding: 20px 10px;
        z-index: 0;
        background-size: cover;
        overflow: hidden;
    }
    .menu-class{
        position: relative;
        margin: 0 0 9px 1px;
        z-index: 0;
        overflow: visible;
    }
    .show-shadow{
        position: absolute;
        z-index: -1;
        left: 8px;
        top: 8px;
        right: -8px;
        bottom: -8px;
        background-color: rgba(67,101,227,.2);
    }
    .one-card-side,.two-card-side{
        position: relative;
        z-index: 0;
        overflow: visible;
    }
    .two-card-side{
        width: intrinsic;           /* Safari/WebKit uses a non-standard name */
        width: fit-content;
        width: -moz-max-content;    /* Firefox/Gecko */
        width: -webkit-max-content; /* Chrome */
    }
</style>
