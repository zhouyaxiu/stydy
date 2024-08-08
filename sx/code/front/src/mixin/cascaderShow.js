import {
} from '@/utils/axios/api'
export default {
  data () {
    return {
    }
  },
  methods: {
    clickSubcat (node) {
      // 获取dom 元素，他是一个数组
      var el_l = document.querySelectorAll(
        '.el-popper.el-cascader__dropdown.cascaderShow .el-cascader-panel .el-cascader-menu'
      )
      if (el_l[node.level] && el_l.length > 0 && !node.isLeaf) {
        if (Number(node.level) === 1) {
          el_l[node.level].style.display = 'block'
          if (el_l[node.level + 1]) {
            el_l[node.level + 1].style.display = 'block'
          }
        } else if (Number(node.level) === 2) {
          el_l[node.level].style.display = 'block'
        } else {
          el_l[node.level].style.display = 'block'
        }
        // el_l[node.level].style.display = "block";
      } else {
        if (el_l[node.level] && el_l.length > 0) {
          this.getlevel(el_l, node, node.level)
        }
      }
    },
    getlevel (el_l, node, num) {
      if (el_l[num] !== undefined && el_l[num] !== '') {
        el_l[num].style.display = 'none'
        num++
        this.getlevel(el_l, node, num)
      }
    }
  }
}
