<template>
  <el-form :model="form" :rules="rules" ref="form" label-width="100px">
    <el-form-item label="供应商编码" prop="code">
      <el-input v-model="form.code" placeholder="请输入供应商编码" :disabled="isEdit"></el-input>
    </el-form-item>
    <el-form-item label="供应商名称" prop="name">
      <el-input v-model="form.name" placeholder="请输入供应商名称"></el-input>
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
    <el-form-item label="供应商地址" prop="address">
      <el-input type="textarea" v-model="form.address" placeholder="请输入供应商地址" rows="2"></el-input>
    </el-form-item>
    <el-form-item label="付款条件" prop="paymentTerms">
      <el-select v-model="form.paymentTerms" placeholder="请选择付款条件" style="width: 100%">
        <el-option label="NET30" value="NET30"></el-option>
        <el-option label="NET60" value="NET60"></el-option>
        <el-option label="NET90" value="NET90"></el-option>
        <el-option label="COD" value="COD"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="供应商描述" prop="description">
      <el-input type="textarea" v-model="form.description" placeholder="请输入供应商描述" rows="3"></el-input>
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
  name: 'SupplierForm',
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
        paymentTerms: 'NET30',
        description: '',
        status: 'active'
      },
      rules: {
        code: [
          { required: true, message: '请输入供应商编码', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入供应商名称', trigger: 'blur' }
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
        paymentTerms: [
          { required: true, message: '请选择付款条件', trigger: 'change' }
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