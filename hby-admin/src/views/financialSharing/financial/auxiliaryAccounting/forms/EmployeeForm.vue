<template>
  <el-form :model="form" :rules="rules" ref="form" label-width="100px">
    <el-form-item label="员工编码" prop="code">
      <el-input v-model="form.code" placeholder="请输入员工编码" :disabled="isEdit"></el-input>
    </el-form-item>
    <el-form-item label="员工姓名" prop="name">
      <el-input v-model="form.name" placeholder="请输入员工姓名"></el-input>
    </el-form-item>
    <el-form-item label="所属部门" prop="department">
      <el-select v-model="form.department" placeholder="请选择所属部门" style="width: 100%">
        <el-option label="研发部" value="研发部"></el-option>
        <el-option label="市场部" value="市场部"></el-option>
        <el-option label="财务部" value="财务部"></el-option>
        <el-option label="人事部" value="人事部"></el-option>
        <el-option label="运营部" value="运营部"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="职位" prop="position">
      <el-select v-model="form.position" placeholder="请选择职位" style="width: 100%">
        <el-option label="经理" value="经理"></el-option>
        <el-option label="主管" value="主管"></el-option>
        <el-option label="专员" value="专员"></el-option>
        <el-option label="助理" value="助理"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="联系电话" prop="phone">
      <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
    </el-form-item>
    <el-form-item label="邮箱地址" prop="email">
      <el-input v-model="form.email" placeholder="请输入邮箱地址"></el-input>
    </el-form-item>
    <el-form-item label="员工描述" prop="description">
      <el-input type="textarea" v-model="form.description" placeholder="请输入员工描述" rows="3"></el-input>
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
  name: 'EmployeeForm',
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
        department: '',
        position: '',
        phone: '',
        email: '',
        description: '',
        status: 'active'
      },
      rules: {
        code: [
          { required: true, message: '请输入员工编码', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入员工姓名', trigger: 'blur' }
        ],
        department: [
          { required: true, message: '请选择所属部门', trigger: 'change' }
        ],
        position: [
          { required: true, message: '请选择职位', trigger: 'change' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
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