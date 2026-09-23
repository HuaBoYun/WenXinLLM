<template>
  <div>
    <span @click="dialogVisible = true">
      <vab-icon icon="user-line" />
      修改密码
    </span>
    <el-dialog
      :append-to-body="true"
      title="修改密码"
      :visible.sync="dialogVisible"
      width="30%"
      :close-on-click-modal="false"
    >
      <el-row>
        <el-form ref="form" label-width="100px" :model="form" :rules="rules">
          <el-form-item label="用户名">
            <el-col :span="16">
              <el-input disabled v-model="form.username" />
            </el-col>
          </el-form-item>
          <el-form-item label="真实姓名">
            <el-col :span="16">
              <el-input disabled v-model="form.realname" />
            </el-col>
          </el-form-item>
          <el-form-item label="旧密码" prop="oldpassord">
            <el-col :span="16">
              <el-input type="password" v-model="form.oldpassord" />
            </el-col>
          </el-form-item>
          <el-form-item label="新密码" prop="newpassword">
            <el-col :span="16">
              <el-input type="password" v-model="form.newpassword" />
            </el-col>
          </el-form-item>
          <el-form-item label="新密码确认" prop="twopassword">
            <el-col :span="16">
              <el-input type="password" v-model="form.twopassword" />
            </el-col>
          </el-form-item>
        </el-form>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit('form')">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
  import { modifyUserPassword } from '@/api/audit/plan'

  export default {
    data() {
      let oldpassordVal = (rule, value, callback) => {
        if (!value) {
          return callback('请输入旧密码')
        } else if (value.length < 6 || value.length > 20) {
          return callback(new Error('输入长度6位到20位之间'))
        } else {
          callback()
        }
      }
      let newpasswordVal = (rule, value, callback) => {
        if (!value) {
          return callback('请输入新密码')
        } else if (value.length < 6 || value.length > 20) {
          return callback(new Error('输入长度6位到20位之间'))
        } else if (value == this.form.oldpassord) {
          return callback(new Error('新密码和旧密码相同!'))
        } else {
          callback()
        }
      }
      let twopasswordVal = (rule, value, callback) => {
        var a = /[0-9]/
        var b = /[A-Za-z]/
        if (!value) {
          return callback('请输入新密码确认')
        } else if (value.length < 6 || value.length > 20) {
          return callback(new Error('输入长度6位到20位之间'))
        } else if (!a.test(value)) {
          callback(new Error('密码必须包含数字和字母'))
        } else if (!b.test(value)) {
          callback(new Error('密码必须包含数字和字母'))
        } else if (value !== this.form.newpassword) {
          return callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      }
      return {
        dialogVisible: false,
        staffId: '',
        form: {
          username: '',
          realname: '',
          oldpassord: '',
          newpassword: '',
          twopassword: '',
        },
        rules: {
          oldpassord: [
            { validator: oldpassordVal, tigger: 'blur', required: true },
          ],
          newpassword: [
            { validator: newpasswordVal, tigger: 'blur', required: true },
          ],
          twopassword: [
            { validator: twopasswordVal, tigger: 'blur', required: true },
          ],
        },
      }
    },
    created() {
      let resL = JSON.parse(localStorage.getItem('userInfo'))
      this.form = resL
      this.staffId = resL.staffid
    },
    methods: {
      onSubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let { msg } = await modifyUserPassword({
              staffId: this.staffId,
              oldPassWord: this.form.oldpassord,
              newPassWord: this.form.newpassword,
              twoPassWord: this.form.twopassword,
            })
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.dialogVisible = false
          } else {
            return false
          }
        })
      },
    },
  }
</script>
