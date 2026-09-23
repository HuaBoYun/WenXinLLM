<template>
  <el-form :model="form" :rules="rules" ref="form" label-width="100px">
    <el-form-item label="客户编码" prop="code">
      <el-input v-model="form.code" placeholder="请输入客户编码" :disabled="isEdit"></el-input>
    </el-form-item>
    <el-form-item label="客户名称" prop="name">
      <el-input v-model="form.name" placeholder="请输入客户名称"></el-input>
    </el-form-item>
    <el-form-item label="联系人" prop="contact">
      <el-input v-model="form.contact" placeholder="请输入联系人"></el-input>
    </el-form-item>
    <el-form-item label="联系电话" prop="phone">
      <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
    </el-form-item>
    <el-form-item label="邮箱地址" prop="email">
      <el-input v-model="form.email" placeholder="请输入邮箱地址"></el-input>
    </el-form-item>
    <el-form-item label="客户地址" prop="address">
      <el-input type="textarea" v-model="form.address" placeholder="请输入客户地址" rows="2"></el-input>
    </el-form-item>
    <el-form-item label="信用额度" prop="creditLimit">
      <el-input-number
        v-model="form.creditLimit"
        :min="0"
        :precision="2"
        placeholder="请输入信用额度"
        style="width: 100%">
      </el-input-number>
    </el-form-item>
    <el-form-item label="客户描述" prop="description">
      <el-input type="textarea" v-model="form.description" placeholder="请输入客户描述" rows="3"></el-input>
    </el-form-item>
    <el-form-item label="状态" prop="status">
      <el-radio-group v-model="form.status">
        <el-radio label="active">启用</el-radio>
        <el-radio label="inactive">停用</el-radio>
      </el-radio-group>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: 'CustomerForm',
  props: {
    formData: {
      type: Object,
      default: () => ({})
    },
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      form: {
        id: '',
        code: '',
        name: '',
        contact: '',
        phone: '',
        email: '',
        address: '',
        creditLimit: 0,
        description: '',
        status: 'active'
      },
      rules: {
        code: [
          { required: true, message: '请输入客户编码', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入客户名称', trigger: 'blur' }
        ],
        contact: [
          { required: true, message: '请输入联系人', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码格式', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        creditLimit: [
          { required: true, message: '请输入信用额度', trigger: 'blur' },
          { type: 'number', min: 0, message: '信用额度不能小于0', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    formData: {
      handler(newVal) {
        this.form = { ...this.form, ...newVal }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    validate(callback) {
      this.$refs.form.validate(callback)
    }
  }
}
</script>