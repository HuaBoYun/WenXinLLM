<template>
  <el-dialog
    title="预算调整"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="调整类型" prop="adjustmentType">
        <el-radio-group v-model="form.adjustmentType">
          <el-radio label="增加预算">增加预算</el-radio>
          <el-radio label="减少预算">减少预算</el-radio>
          <el-radio label="调整分配">调整分配</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="调整金额" prop="adjustmentAmount">
        <el-input-number
          v-model="form.adjustmentAmount"
          :precision="2"
          :min="0"
          style="width: 100%"
          placeholder="请输入调整金额(万元)"
        />
      </el-form-item>

      <el-form-item label="调整原因" prop="adjustmentReason">
        <el-input
          v-model="form.adjustmentReason"
          type="textarea"
          :rows="4"
          placeholder="请输入调整原因"
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
  name: 'BudgetAdjustmentDialog',
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
        adjustmentType: '增加预算',
        adjustmentAmount: 0,
        adjustmentReason: '',
        remarks: ''
      },
      rules: {
        adjustmentType: [
          { required: true, message: '请选择调整类型', trigger: 'change' }
        ],
        adjustmentAmount: [
          { required: true, message: '请输入调整金额', trigger: 'blur' }
        ],
        adjustmentReason: [
          { required: true, message: '请输入调整原因', trigger: 'blur' }
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
        adjustmentType: '增加预算',
        adjustmentAmount: 0,
        adjustmentReason: '',
        remarks: ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true
          setTimeout(() => {
            this.loading = false
            this.$message.success('预算调整提交成功')
            this.$emit('refresh')
            this.handleClose()
          }, 1000)
        }
      })
    }
  }
}
</script>
