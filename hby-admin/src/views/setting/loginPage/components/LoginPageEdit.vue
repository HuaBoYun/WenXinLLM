<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="close"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="主题名称" prop="loginName">
            <el-input v-model.trim="form.loginName" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="技术支持热线" prop="technicalSupportHotline">
            <el-input v-model.trim="form.technicalSupportHotline" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="登录页文字一" prop="loginTextOne">
            <el-input v-model.trim="form.loginTextOne" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="登录页文字二" prop="loginTextTwo">
            <el-input v-model.trim="form.loginTextTwo" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="首页文字" prop="homeText">
            <el-input v-model.trim="form.homeText" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="登录文字" prop="moduleText">
            <el-input v-model.trim="form.moduleText" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="登录页背景图">
            <el-upload
              v-model="form.homePicture"
              class="avatar-uploader"
              :action="baseApi + api"
              :headers="headers"
              list-type="picture-card"
              :show-file-list="false"
              :on-success="handlePicSuccess"
            >
              <img
                v-if="form.homePicture"
                class="avatar"
                :src="form.homePicture"
              />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="首页logo" prop="leftUpperPicture">
            <el-upload
              v-model="form.leftUpperPicture"
              class="avatar-uploader"
              :action="baseApi + api"
              :headers="headers"
              list-type="picture-card"
              :show-file-list="false"
              :on-success="handleLogoSuccess"
            >
              <img
                v-if="form.leftUpperPicture"
                class="avatar"
                :src="form.leftUpperPicture"
              />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模块页面logo" prop="logoPicture">
            <el-upload
              v-model="form.logoPicture"
              class="avatar-uploader"
              :action="baseApi + api"
              :headers="headers"
              list-type="picture-card"
              :show-file-list="false"
              :on-success="handleMlogoSuccess"
            >
              <img
                v-if="form.logoPicture"
                class="avatar"
                :src="form.logoPicture"
              />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import {
    saveLoginPage,
    getPrivewAttInfo,
    getDefaultLoginInfo,
  } from '@/api/setting/loginPage'
  import FileUpload from '@/components/FileUpload.vue'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']

  export default {
    name: 'LoginPageEdit',
    components: { FileUpload },
    data() {
      return {
        form: {
          loginName: '',
          salesHotline: '',
          technicalSupportHotline: '',
          loginTextOne: '',
          loginTextTwo: '',
          homeText: '',
          homePicture: '',
          leftUpperPicture: '',
          logoPicture: '',
          MPicture: '',
          id: '',
          moduleText: '',
        },
        rules: {},
        title: '',
        dialogFormVisible: false,
        api: '/setting/fileManage/upload/image',
        headers: { token: token },
        baseApi: baseURL,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          getDefaultLoginInfo({ id: row.id }).then((res) => {
            Object.keys(this.form).forEach((key) => {
              this.form[key] = res.data[key]
            })
          })
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const type = this.form.id ? '修改' : '保存'
            const form = {
              ...this.form,
            }
            const res = await saveLoginPage(form)
            if (res.code == 200) {
              this.$baseMessage(
                `${type}成功`,
                'success',
                'vab-hey-message-success'
              )
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
      async handlePicSuccess(res) {
        this.form.homePicture = res.data
      },
      async handleLogoSuccess(res) {
        this.form.leftUpperPicture = res.data
      },
      async handleMlogoSuccess(res) {
        this.form.logoPicture = res.data
      },
    },
  }
</script>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 100%;
    display: block;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
</style>
