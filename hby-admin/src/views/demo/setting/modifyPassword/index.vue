<template>
  <div class="personal-center-container">
    <el-row :gutter="20">
      <el-col :lg="8" :md="12" :sm="24" :xl="8" :xs="24"></el-col>
      <el-col :lg="16" :md="12" :sm="24" :xl="16" :xs="24">
        <el-card shadow="hover">
          <el-tabs v-model="activeName">
            <el-tab-pane label="基本信息" name="first">
              <el-col :lg="12" :md="16" :sm="24" :xl="12" :xs="24">
                <el-form ref="form" label-width="100px" :model="form">
                  <el-form-item label="用户名">
                    <el-col>
                      <el-input disabled v-model="form.username" />
                    </el-col>
                  </el-form-item>
                  <el-form-item label="真实姓名">
                    <el-col>
                      <el-input disabled v-model="form.realname" />
                    </el-col>
                  </el-form-item>
                  <el-form-item label="旧密码">
                    <el-col>
                      <el-input v-model="form.oldpassord" />
                    </el-col>
                  </el-form-item>
                  <el-form-item label="新密码">
                    <el-col>
                      <el-input v-model="form.newpassword" />
                    </el-col>
                  </el-form-item>
                  <el-form-item label="新密码确认">
                    <el-col>
                      <el-input v-model="form.twopassword" />
                    </el-col>
                  </el-form-item>
                  </el-form-item>
                    <el-button type="primary" @click="onSubmit">保存</el-button>
                    <el-button type="primary" @click="onBack">返回</el-button>
                  </el-form-item>
                </el-form>
              </el-col>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
    <vab-cropper ref="vabCropper" />
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import VabCropper from '@/extra/VabCropper'
  import store from '@/store'

  // import { modifyUserPassword } from '@/api/audit/plan'
import axios from 'axios'



  export default {
    name: 'PersonalCenter',
    components: { VabCropper },
    data() {
      return {
        staffId:"",
        activeName: 'first',
        form: {
          username:"",
          realname:"",
          oldpassord:"",
          newpassword:'',
          surenewpassword:'',
          twopassword:''
        },
      }
    },
    computed: {
      ...mapGetters({
        avatar: 'user/avatar',
      }),
    },
    created(){
      this.getUserInfoFun()
    },

    methods: {
      onSubmit(){
        this.$baseMessage('保存成功', 'success', 'vab-hey-message-success')
        // axios({
        //   method:'post',
        //   url:'http://w2s2vk.natappfree.example/setting/user/modifyUserPassWord',
        //   headers:{
        //     token:store.getters['user/token']
        //   },
        //   params:{
        //     staffId:this.staffId,
        //     oldPassWord:this.form.oldpassord,
        //     newPassWord:this.form.newpassword,
        //     twoPassWord:this.form.twopassword
        //   }
        // }).then((res)=>{
        //   // let res = {"code":1,"msg":"原密码错误"}
        //   // 
        // })
      },
      // async onSubmit() {
      //   
      //   let res = await modifyUserPassword({
      //     staffId:this.staffId,
      //     oldPassWord:this.form.oldpassord,
      //     newPassWord:this.form.newpassword
      //   })
        
      //   

      // },
      openDialog() {
        this.$refs['vabCropper'].dialogVisible = true
      },
      onBack(){
        this.$router.go(-1)
      }
    },
  }
</script>

<style lang="scss" scoped>
  $base: '.personal-center';
  #{$base}-container {
    padding: 0 !important;
    background: $base-color-background !important;

    #{$base}-user-info {
      padding: $base-padding;
      text-align: center;

      ::v-deep {
        .el-avatar {
          img {
            cursor: pointer;
          }
        }
      }

      &-full-name {
        margin-top: 15px;
        font-size: 24px;
        font-weight: 500;
        color: #262626;
      }

      &-description {
        margin-top: 8px;
      }

      &-follow {
        margin-top: 15px;
      }

      &-list {
        margin-top: 18px;
        line-height: 30px;
        text-align: left;
        list-style: none;

        h5 {
          margin: -20px 0 5px 0;
        }

        ::v-deep {
          .el-tag {
            margin-right: 10px !important;
          }

          .el-tag + .el-tag {
            margin-left: 0;
          }
        }
      }
    }

    #{$base}-item {
      display: flex;

      i {
        font-size: 40px;
      }

      &-content {
        box-sizing: border-box;
        flex: 1;
        margin-left: $base-margin;

        &-second {
          margin-top: 8px;
        }
      }
    }
  }
</style>
