<template>
  <el-form :model="form" :rules="rules" ref="form" label-width="100px">
    <el-form-item label="项目编码" prop="code">
      <el-input v-model="form.code" placeholder="请输入项目编码" :disabled="isEdit"></el-input>
    </el-form-item>
    <el-form-item label="项目名称" prop="name">
      <el-input v-model="form.name" placeholder="请输入项目名称"></el-input>
    </el-form-item>
    <el-form-item label="项目类型" prop="type">
      <el-select v-model="form.type" placeholder="请选择项目类型" style="width: 100%">
        <el-option label="研发项目" value="研发项目"></el-option>
        <el-option label="市场项目" value="市场项目"></el-option>
        <el-option label="内部项目" value="内部项目"></el-option>
        <el-option label="客户项目" value="客户项目"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="项目经理" prop="manager">
      <el-input v-model="form.manager" placeholder="请输入项目经理"></el-input>
    </el-form-item>
    <el-form-item label="开始日期" prop="startDate">
      <el-date-picker
        v-model="form.startDate"
        type="date"
        placeholder="请选择开始日期"
        format="yyyy-MM-dd"
        value-format="yyyy-MM-dd"
        style="width: 100%">
      </el-date-picker>
    </el-form-item>
    <el-form-item label="结束日期" prop="endDate">
      <el-date-picker
        v-model="form.endDate"
        type="date"
        placeholder="请选择结束日期"
        format="yyyy-MM-dd"
        value-format="yyyy-MM-dd"
        style="width: 100%">
      </el-date-picker>
    </el-form-item>
    <el-form-item label="预算金额" prop="budget">
      <el-input-number
        v-model="form.budget"
        :min="0"
        :precision="2"
        placeholder="请输入预算金额"
        style="width: 100%">
      </el-input-number>
    </el-form-item>
    <el-form-item label="项目状态" prop="status">
      <el-select v-model="form.status" placeholder="请选择项目状态" style="width: 100%">
        <el-option label="规划中" value="planning"></el-option>
        <el-option label="进行中" value="in_progress"></el-option>
        <el-option label="已完成" value="completed"></el-option>
        <el-option label="已取消" value="cancelled"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="项目描述" prop="description">
      <el-input type="textarea" v-model="form.description" placeholder="请输入项目描述" rows="3"></el-input>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: 'ProjectForm',
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
        type: '',
        manager: '',
        startDate: '',
        endDate: '',
        budget: 0,
        status: 'planning',
        description: ''
      },
      rules: {
        code: [
          { required: true, message: '请输入项目编码', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入项目名称', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择项目类型', trigger: 'change' }
        ],
        manager: [
          { required: true, message: '请输入项目经理', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ],
        endDate: [
          { required: true, message: '请选择结束日期', trigger: 'change' }
        ],
        budget: [
          { required: true, message: '请输入预算金额', trigger: 'blur' },
          { type: 'number', min: 0, message: '预算金额不能小于0', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择项目状态', trigger: 'change' }
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