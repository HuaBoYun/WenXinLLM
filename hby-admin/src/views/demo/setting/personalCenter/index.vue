<template>
  <div class="personal-center-container">
    <el-row :gutter="20">
      <!-- <el-col :lg="16" :md="12" :sm="24" :xl="16" :xs="24"> -->
        <el-card shadow="hover">
          <el-tabs v-model="activeName">
            <el-tab-pane label="基本信息" name="first">
              <el-col :lg="12" :md="16" :sm="24" :xl="12" :xs="24">
                <el-form ref="form" label-width="100px" :model="form">
                  <el-form-item label="用户名">
                    <el-col :span="8">
                      <el-input disabled v-model="form.username" />
                    </el-col>
                  </el-form-item>
                  <el-form-item label="真实姓名">
                    <el-col :span="8">
                      <el-input disabled v-model="form.realname" />
                    </el-col>
                  </el-form-item>
                  <el-form-item label="用户信息地址">
                    <el-col :span="8">
                      <el-input disabled v-model="form.nickname" />
                    </el-col>
                  </el-form-item>
                  <el-form-item label="角色">
                    <el-col :span="8">
                      <el-input disabled v-model="form.roleNames" />
                    </el-col>
                  </el-form-item>
                  <el-form-item label="Email">
                    <el-col :span="8">
                      <el-input disabled v-model="form.email" />
                    </el-col>
                  </el-form-item>
                  <el-form-item label="所属机构">
                    <el-col :span="8">
                      <el-input disabled v-model="form.currentOrg.orgname" />
                    </el-col>
                  </el-form-item>
                  <el-form-item label="电话号码">
                    <el-col :span="8">
                      <el-input disabled v-model="form.miblephone" />
                    </el-col>
                  </el-form-item>
                  <el-form-item label="移动电话">
                    <el-col :span="8">
                      <el-input disabled v-model="form.nickname" />
                    </el-col>
                  </el-form-item>
                  <el-form-item label="简短描述">
                    <el-col :span="12">
                      <el-input disabled v-model="form.nickname" type="textarea" />
                    </el-col>
                  </el-form-item>
                  </el-form-item>
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

  export default {
    name: 'PersonalCenter',
    components: { VabCropper },
    data() {
      return {
        activeName: 'first',
        form: {},
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
      onSubmit() {
        this.$baseMessage('模拟保存成功', 'success', 'vab-hey-message-success')
      },
      openDialog() {
        this.$refs['vabCropper'].dialogVisible = true
      },
      onBack(){
        this.$router.go(-1)
      },
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
