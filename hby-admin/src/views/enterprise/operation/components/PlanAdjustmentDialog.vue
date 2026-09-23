<template>
  <el-dialog
    title="计划调整"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="调整类型" prop="adjustmentType">
        <el-radio-group v-model="form.adjustmentType">
          <el-radio label="目标调整">目标调整</el-radio>
          <el-radio label="时间调整">时间调整</el-radio>
          <el-radio label="资源调整">资源调整</el-radio>
          <el-radio label="策略调整">策略调整</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="调整原因" prop="adjustmentReason">
        <el-input
          v-model="form.adjustmentReason"
          type="textarea"
          :rows="4"
          placeholder="请输入调整原因"
        />
      </el-form-item>

      <el-form-item label="调整内容" prop="adjustmentContent">
        <el-input
          v-model="form.adjustmentContent"
          type="textarea"
          :rows="4"
          placeholder="请输入具体调整内容"
        />
      </el-form-item>

      <el-form-item label="预期效果">
        <el-input
          v-model="form.expectedEffect"
          type="textarea"
          :rows="3"
          placeholder="请输入预期效果"
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
import { adjustPlan } from '@/api/enterprise/operationPlan'

export default {
  name: 'PlanAdjustmentDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    planData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        adjustmentType: '目标调整',
        adjustmentReason: '',
        adjustmentContent: '',
        expectedEffect: '',
        remarks: ''
      },
      rules: {
        adjustmentType: [
          { required: true, message: '请选择调整类型', trigger: 'change' }
        ],
        adjustmentReason: [
          { required: true, message: '请输入调整原因', trigger: 'blur' }
        ],
        adjustmentContent: [
          { required: true, message: '请输入调整内容', trigger: 'blur' }
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
        adjustmentType: '目标调整',
        adjustmentReason: '',
        adjustmentContent: '',
        expectedEffect: '',
        remarks: ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async(valid) => {
        if (valid) {
          this.loading = true
          try {
            const updateBy = this.$store && this.$store.getters && this.$store.getters.name ? this.$store.getters.name : 'admin'
            await adjustPlan(
              this.planData.planId,
              this.form.adjustmentReason,
              this.form.adjustmentContent,
              updateBy
            )
            this.$message.success('计划调整提交成功')
            this.$emit('refresh')
            this.handleClose()
          } catch (error) {
            this.$message.error('计划调整失败: ' + (error.message || '未知错误'))
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>
