<template>
  <el-dialog
    title="预算执行管理"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="执行操作" prop="executionAction">
        <el-radio-group v-model="form.executionAction">
          <el-radio label="启动执行">启动执行</el-radio>
          <el-radio label="暂停执行">暂停执行</el-radio>
          <el-radio label="恢复执行">恢复执行</el-radio>
          <el-radio label="完成执行">完成执行</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="执行说明" prop="executionDescription">
        <el-input
          v-model="form.executionDescription"
          type="textarea"
          :rows="4"
          placeholder="请输入执行说明"
        />
      </el-form-item>

      <el-form-item label="备注信息">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'BudgetExecutionDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    budgetData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        executionAction: '启动执行',
        executionDescription: '',
        remarks: ''
      },
      rules: {
        executionAction: [
          { required: true, message: '请选择执行操作', trigger: 'change' }
        ],
        executionDescription: [
          { required: true, message: '请输入执行说明', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.form = {
        executionAction: '启动执行',
        executionDescription: '',
        remarks: ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true
          setTimeout(() => {
            this.loading = false
            this.$message.success('执行操作提交成功')
            this.$emit('refresh')
            this.handleClose()
          }, 1000)
        }
      })
    }
  }
}
</script>
