<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="100px" :model="form" :rules="rules">
      <el-form-item label="用户名" prop="username">
        <el-input v-model.trim="form.username" />
        <span class="color-red">
          合法的账号名应该由a-z的英文字母、0-9的数字组成。长度为5-16个字符之间(一个英文字母或数字算一个字符，请勿使用空白键，请选择不会引起歧义的账号名).例如:abc007
        </span>
      </el-form-item>
      <el-form-item v-if="title !== '编辑'" label="密码" prop="password">
        <el-input v-model.trim="form.password" class="input-psword" />
        <span class="color-red">
          为了保证您的密码安全,请设定密码长度为6个字符以上,由a-z的英文字母(注意区分大小写)、0-9的数字组成.同时请保管好您的密码
        </span>
      </el-form-item>
      <el-form-item v-if="title !== '编辑'" label="密码确认" prop="password1">
        <el-input v-model.trim="form.password1" class="input-psword" />
        <span class="color-red">请再次输入您的密码，以便确认没输错密码</span>
      </el-form-item>
      <el-form-item label="真实姓名" prop="realname">
        <el-input v-model.trim="form.realname" />
      </el-form-item>
      <el-form-item label="email">
        <el-input v-model.trim="form.email" />
      </el-form-item>
      <el-form-item label="移动电话">
        <el-input v-model.trim="form.miblephone" />
      </el-form-item>
      <el-form-item label="是否禁用">
        <el-radio-group v-model="form.status">
          <el-radio
            v-for="(item, index) in iszyoptions"
            :key="index"
            :label="item.value"
          >
            {{ item.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="单位" prop="company">
        <el-input v-model.trim="form.company" />
      </el-form-item>
      <el-form-item label="专长">
        <el-input v-model.trim="form.expertise" />
      </el-form-item>
      <el-form-item label="资格证书">
        <el-input v-model.trim="form.qualification" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { sjzjkAdd, sjzjkToadd } from '@/api/setting/specialist'

  export default {
    name: 'ExternalEdit',
    data() {
      return {
        form: {
          username: '',
          password: '',
          password1: '',
          realname: '',
          email: '',
          miblephone: '',
          qualification: '',
          expertise: '',
          company: '',
          oid: undefined,
        },
        list: [],
        rules: {
          username: [
            { required: true, trigger: 'blur', message: '请输入用户名' },
          ],
          password: [
            { required: true, trigger: 'blur', message: '请输入密码' },
          ],
          password1: [
            { required: true, trigger: 'blur', message: '请确认密码' },
          ],
          realname: [
            { required: true, trigger: 'blur', message: '请输入真实姓名' },
          ],
          company: [{ required: true, trigger: 'blur', message: '请输入单位' }],
        },
        title: '',
        dialogFormVisible: false,
        iszyoptions: [
          {
            value: 1,
            label: '是',
          },
          {
            value: 0,
            label: '否',
          },
        ],
      }
    },
    created() {},
    methods: {
      showEdit(row, oid) {
        if (!row) {
          this.title = '添加'
          this.form.oid = oid
        } else {
          this.title = '编辑'
          this.sjzjkToadd1(row)
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
            const { msg } = await sjzjkAdd(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      async sjzjkToadd1(row) {
        const { staff, expert } = await sjzjkToadd({ exterid: row.exterid })
        this.form = Object.assign(staff, expert)
      },
    },
  }
</script>
<style scoped>
  .input-psword {
    -webkit-text-security: disc;
  }
</style>
