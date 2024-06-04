<template>
  <div ref="dropdown"
    class="el-select-dropdown el-popper"
    :class="[{ 'is-multiple': $parent.multiple }, popperClass]"
    :style="{ minWidth: minWidth, height: height, transform: transform, transformOrigin: 'left top' }">
    <slot></slot>
  </div>
</template>

<script type="text/babel">
  import Popper from 'element-ui/src/utils/vue-popper';

  export default {
    name: 'ElSelectDropdown',

    componentName: 'ElSelectDropdown',

    mixins: [Popper],

    props: {
      placement: {
        default: 'bottom-start'
      },

      boundariesPadding: {
        default: 0
      },

      popperOptions: {
        default() {
          return {
            gpuAcceleration: false
          };
        }
      },

      visibleArrow: {
        default: true
      },

      appendToBody: {
        type: Boolean,
        default: true
      }
    },

    data() {
      return {
        minWidth: '',
        height: 'auto',
        transform: (navigator.userAgent.indexOf('Firefox') > -1 ? 'scale(' + (1 / window.scale) + ')' : 'none')
      };
    },

    computed: {
      popperClass() {
        return this.$parent.popperClass;
      }
    },

    watch: {
      '$parent.inputWidth'() {
        this.minWidth = this.$parent.$el.getBoundingClientRect().width + 'px';
        if (window.isIE && window.isIE() && window.scale) {
          this.minWidth = this.$parent.$el.getBoundingClientRect().width / window.scale + 'px';
        }
      },
      '$parent.visible'(val) {
        this.minWidth = this.$parent.$el.getBoundingClientRect().width + 'px';
        if (window.isIE && window.isIE() && window.scale) {
          this.minWidth = this.$parent.$el.getBoundingClientRect().width / window.scale + 'px';
          if (this.height === 'auto') {
            this.height = this.$parent.$el.getClientRects().height / window.scale + 'px';
          }
        }
        this.$refs.dropdown.style.transformOrigin = 'left top';
        if (val) {
          this.$nextTick(() => {
            this.$refs.dropdown.style.transform = this.transform;
          });
        }
      }
    },

    mounted() {
      this.referenceElm = this.$parent.$refs.reference.$el;
      this.$parent.popperElm = this.popperElm = this.$el;
      this.$on('updatePopper', () => {
        if (this.$parent.visible) this.updatePopper();
      });
      this.$on('destroyPopper', this.destroyPopper);
    }
  };
</script>
