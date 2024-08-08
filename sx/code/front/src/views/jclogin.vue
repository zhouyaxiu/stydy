<template>
  <div class="loginVality">
    <el-form label-width="150px" :model="unitManageForm" ref="unitManageForm" inline>
      <ss-common-area-group :columnCount="2" style="margin-left: 0;width:100%">
        <ss-common-area-item label="所属地区:" prop="areaRange" >
          <areaTreeStationNew ref="areaTreePage" style="width: 400px;" :treeH="'320px'" />
        </ss-common-area-item>
        <ss-common-area-item label="用户显示名:" prop="username">
          <el-input v-model="unitManageForm.username" style="width: 400px;" readonly></el-input>
        </ss-common-area-item>
        <ss-common-area-item label="登录账号:" prop="fbUser">
          <el-input clearable style="width: 400px;" v-model="unitManageForm.title" readonly></el-input>
        </ss-common-area-item>
      </ss-common-area-group>
    </el-form>
    <div class="transfer_bom_container">
      <div class="transfer_container">
        <!-- 左侧框 -->
        <div class="left_transfer border">
          <header class="header_con">
            <div class="header_left">
              <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll"
                @change="handleCheckAllChange">已分配角色</el-checkbox>
              <span class="left_total">{{ checkedData.length }}/{{ dataleft.length }}</span>
            </div>
          </header>
          <div class="transfer_content">
            <el-checkbox-group v-model="checkedData" @change="handlecheckedDataChange" class="transfer_group">
              <el-checkbox v-for="item in dataleft" :label="item.id" :key="item.id">
                <span class="span_left">{{ item.roleName }}</span>
              </el-checkbox>
            </el-checkbox-group>
          </div>
        </div>
        <!-- 穿梭按键 -->
        <el-row class="middle_btn">
          <el-button type="info" plain icon="el-icon-d-arrow-right" circle @click="goRightAll"></el-button>
          <el-button type="info" plain icon="el-icon-d-arrow-left" circle @click="goLeftAll"></el-button>
          <el-button type="info" plain icon="el-icon-arrow-right" circle @click="goRight" :disabled="checkedData.length == 0"></el-button>
          <el-button type="info" plain icon="el-icon-arrow-left" circle @click="deleteSignle" :disabled="checkedDataRight.length == 0"></el-button>
        </el-row>
        <!-- 右侧框 -->
        <div class="right_transfer border">
          <header class="header_con">
            <div class="header_left">
              <el-checkbox :indeterminate="isIndeterminateRight" v-model="checkAllRight"
                @change="handleCheckAllChangeRight">未分配角色</el-checkbox>
              <span class="left_total">{{ checkedDataRight.length }}/{{ dataright.length }}</span>
            </div>
          </header>
          <div class="transfer_content">
            <el-checkbox-group v-model="checkedDataRight" @change="handlecheckedDataChangeRight" class="transfer_group">
              <el-checkbox v-for="item in dataright" :label="item.id" :key="item.id">
                <span class="span_left">{{ item.roleName }}</span>
              </el-checkbox>
            </el-checkbox-group>
            <div v-if="dataright.length == 0" style="color:#999;padding:20px 0 0;text-align:center;">无数据</div>
          </div>
        </div>
      </div>
    </div>
    <div class="dialog-footer">
      <el-button @click="confirm" type="primary">确 定</el-button>
      <!-- <el-button @click="close">取 消</el-button> -->
    </div>
  </div>
</template>

<script>
import { rxrtShowSysUserUpdate,updateRxrtUser } from '@/utils/axios/api';
import areaTreeStationNew from '@/components/common/areaTreeStationNew';
export default {
  name: 'treeTransfer',
  data() {
    return {
      // searchLeft: "",//左侧搜索条件
      checkAll: false,
      checkedData: [],
      isIndeterminate: false,
      // currentPage: 1,
      goright: true,
      goleft: true,
      dataleft: [],
      // totalleft: 0,
      // totalright: 0,
      dataright: [],
      // searchRight: "",//右侧搜索条件
      checkedDataRight: [],
      isIndeterminateRight: false,
      checkAllRight: false,
      unitManageForm: {
        title: '',
        username: ''
      },
      rules: {},
    };
  },
  components:{areaTreeStationNew},
  mounted() {
    this.getData()
  },
  watch: {
    checkedData: {
      deep: true,
      handler: function (value) {
        if (value.length > 0) {
          this.goright = false
        } else {
          this.goright = true
        }
      },
      immediate: true
    },
    checkedDataRight: {
      deep: true,
      handler: function (value) {
        if (value.length > 0) {
          this.goleft = false
        } else {
          this.goleft = true
        }
      },
      immediate: true
    },
  },
  methods: {
    getData() {
      rxrtShowSysUserUpdate({userId:this.$route.query.userId}).then(res => {
        this.dataleft = res.data.selSysRoleList;
        this.dataright = res.data.noSelSysRoleList;
        this.unitManageForm.title = this.$route.query.loginName || res.data.userInfo.loginName;
        this.unitManageForm.username = this.$route.query.userName || res.data.userInfo.userName;
        this.id = res.data.userInfo.id
        // if(res.data.userInfo.areaCode!=null){
        //   this.$store.state.userCode = res.data.userInfo.areaCode;
        //   sessionStorage.userCode = res.data.userInfo.areaCode;
        //   this.$store.state.areacode = res.data.userInfo.areaCode;
        //   sessionStorage.areacode = res.data.userInfo.areaCode;
        // }else{
        //   this.$store.state.userCode = sessionStorage.userCode;
        //   this.$store.state.areacode = sessionStorage.areacode;
        // }
        // if(res.data.userInfo.areaName!=null){
        //   this.$store.state.areaname = res.data.userInfo.areaName;
        //   sessionStorage.areaname = res.data.userInfo.areaName;
        // }
      })
    },
    // close() {
    //   this.$emit('close', true)
    // },
    //左侧全选
    handleCheckAllChange(val) {
      if (val) {
        this.dataleft.forEach(item => {
          this.checkedData.push(item.id)
        })
        this.checkedData=[...new Set(this.checkedData)]
      } else {
        this.checkedData = [];
      }
      this.isIndeterminate = false;
    },
    // 左侧单独选中
    handlecheckedDataChange() {
      let checkedCount = this.checkedData.length;
      this.checkAll = checkedCount === this.dataleft.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.dataleft.length;
    },

    // 左边全部到右边
    goRightAll() {
      if(this.dataleft.length==0) return
      this.dataleft.forEach(item => {
        this.checkedData.push(item.id);
      });
      this.goRight();
    },
    // 左边穿梭到右边
    goRight() {
      for (let i = this.dataleft.length - 1; i >= 0; i--) {
        if (this.checkedData.includes(this.dataleft[i].id)) {
          this.dataright.push(this.dataleft[i]);
          // this.checkedDataRight.push(this.dataleft[i].fieldname)
          this.dataleft.splice(i, 1);
          this.checkAll = false;
          this.isIndeterminate = false;
        }
      }
      this.checkedData = [];
      this.checkAll = false;
      this.isIndeterminate = false;
    },

    // 右侧方法
    //右侧全选
    handleCheckAllChangeRight(val) {
      if (val) {
        this.dataright.forEach(item => {
          this.checkedDataRight.push(item.id);
        })
        this.checkedDataRight=[...new Set(this.checkedDataRight)]
      } else {
        this.checkedDataRight = [];
      }
      this.isIndeterminateRight = false;
    },
    // 右侧单独选中
    handlecheckedDataChangeRight() {
      let checkedCount = this.checkedDataRight.length;
      this.checkAllRight = checkedCount === this.dataright.length;
      this.isIndeterminateRight = checkedCount > 0 && checkedCount < this.dataright.length;
    },
    // 右侧选中左移
    deleteSignle() {
      for (let i = this.dataright.length - 1; i >= 0; i--) {
        if (this.checkedDataRight.includes(this.dataright[i].id)) {
          this.dataleft.push(this.dataright[i]);
          this.dataright.splice(i, 1);
          this.checkAllRight=false;
          this.isIndeterminateRight=false;
        }
      }
      this.checkedDataRight = [];
      this.checkAllRight=false;
    },
    // 右侧全部左移
    goLeftAll() {
      if(this.dataright.length==0) return
      for (let i = this.dataright.length - 1; i >= 0; i--) {
        this.dataleft.push(this.dataright[i]);
        // this.checkedData.push(this.dataright[i].fieldname)
        this.dataright.splice(i, 1);
      }
      this.checkedData=[];
      this.checkedDataRight=[];
      this.isIndeterminateRight=false;
      this.checkAllRight=false;
      // this.dataleft=this.dataright;
    },
    // 右边穿梭到左边
    goLeft() {
      for (let i = this.dataright.length - 1; i >= 0; i--) {
        if (this.checkedDataRight.includes(this.dataright[i].id)) {
          this.dataleft.push(this.dataright[i]);
          this.dataright.splice(i, 1);
        }
      }
      this.goleft = true;
      this.checkedDataRight = [];
      this.isIndeterminateRight = false;
      this.checkAllRight = false;
    },
    confirm() {
      if(!this.unitManageForm.title && !this.unitManageForm.username){
        this.$util.errorMsg('用户名和登录账号必填！');
        return false;
      }
      if (this.dataleft.length == 0) {
        this.$util.errorMsg('已选择列至少选中一个！');
        return false;
      }
      this.checkedData = [];
      for (let i = this.dataleft.length - 1; i >= 0; i--) {
        this.checkedData.push(this.dataleft[i].id);
      }
      updateRxrtUser({ userId: this.id, selSysRoleIds: this.checkedData.join(",") }).then(res => {
        if(res.msg.includes('修改成功')){
          this.$util.successMsg(res.msg);
        }else{
          this.$util.errorMsg(res.msg);
        }
      }).catch(() => {

      })
    },
  }
};
</script>

<style lang="less">
.loginVality {

  .el-input,
  .areaInput {
    width: 400px;
  }

  .transfer_bom_container {
    width: 560px !important;
    text-align: center;
    margin: 20px auto 0;

  }

  .transfer_container {
    display: flex;
    min-width: 560px;
    overflow: hidden;
    padding-left: 66px;
    // justify-content: center;
  }

  .el-form {
    text-align: center;

    .area-group {
      width: 560px !important;
      text-align: center;
      margin: 120px auto 0;

      .common-area-item {
        text-align: left;
      }

    }

  }

  .dialog-footer {
    padding: 30px;
    text-align: center;
    text-indent: 50px;
  }

  .el-button {
    margin-bottom: 10px;
  }

  .el-button+.el-button {
    margin-left: 0;
  }

  .border {
    border: 1px solid #ddd;
    width: 200px;
    min-width: 200px;
    min-height: 300px;
    text-align: left;
  }

  .left_transfer .header_con,
  .right_transfer .header_con {
    height: 40px;
    line-height: 40px;
    background: #F5F7FA;
    border-bottom: 1px solid #EBEEF5;
    padding-left: 15px;
  }

  .left_transfer .header_con .el-checkbox,
  .right_transfer .header_con .el-checkbox {
    margin-top: 0;
  }

  .header_left {
    position: relative;
  }

  .left_total {
    position: absolute;
    right: 15px;
    color: #909399;
    font-size: 12px;
    font-weight: 400;
  }

  .transfer_content {
    height: 245px;
    padding-left: 15px;
    overflow: auto;

  }

  .transfer_search {
    margin-top: 10px
  }

  .transfer_group {
    display: flex;
    flex-direction: column;
    line-height: 26px;
  }

  .el-checkbox {
    margin-right: 0;
    margin-top: 5px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .el-checkbox__label {
    display: inline;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    box-sizing: border-box;
    width: 100%
  }

  .span_left {
    width: 75%
  }

  .span_right {
    float: right;
    margin-right: 10px
  }

  .transfer_content::-webkit-scrollbar {
    width: 5px;
    height: 1px;
  }

  .transfer_content::-webkit-scrollbar-thumb {
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    background: #d2dfff;
  }

  .transfer_content::-webkit-scrollbar-track {
    -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    border-radius: 10px;
    background: #EDEDED;
  }

  .middle_btn {
    padding: 0 20px;
    display: flex;
    flex-direction: column;
    align-self: center;
  }

  .middle_button {
    margin: 5px 18px 5px 0;
  }
}
</style>
