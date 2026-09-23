<template>
  <el-form :model="form" :rules="rules" ref="form" label-width="100px">
    <el-form-item label="上级部门" prop="parentId" v-if="!isEdit">
      <el-cascader
        v-model="form.parentId"
        :options="departmentOptions"
        :props="{
          label: 'name',
          value: 'id',
          children: 'children',
          checkStrictly: true,
          emitPath: false
        }"
        placeholder="请选择上级部门"
        clearable>
      </el-cascader>
    </el-form-item>
    <el-form-item label="部门编码" prop="code">
      <el-input v-model="form.code" placeholder="请输入部门编码" :disabled="isEdit"></el-input>
    </el-form-item>
    <el-form-item label="部门名称" prop="name">
      <el-input v-model="form.name" placeholder="请输入部门名称"></el-input>
    </el-form-item>
    <el-form-item label="部门负责人" prop="manager">
      <el-input v-model="form.manager" placeholder="请输入部门负责人"></el-input>
    </el-form-item>
    <el-form-item label="联系电话" prop="phone">
      <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
    </el-form-item>
    <el-form-item label="部门描述" prop="description">
      <el-input type="textarea" v-model="form.description" placeholder="请输入部门描述" rows="3"></el-input>
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
  name: 'DepartmentForm',
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
        parentId: '',
        code: '',
        name: '',
        manager: '',
        phone: '',
        description: '',
        status: 'active'
      },
      departmentOptions: [],
      rules: {
        code: [
          { required: true, message: '请输入部门编码', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入部门名称', trigger: 'blur' }
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