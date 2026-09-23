<template>
  <el-dialog
    title="更新监控数据"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" v-loading="loading">
      <el-form-item label="监控ID">
        <el-input v-model="form.monitoringId" disabled />
      </el-form-item>
      <el-form-item label="预警类型" prop="alertType">
        <el-select v-model="form.alertType" placeholder="请选择预警类型" style="width: 100%">
          <el-option label="风险预警" value="RISK" />
          <el-option label="合规预警" value="COMPLIANCE" />
          <el-option label="还款预警" value="PAYMENT" />
          <el-option label="到期预警" value="MATURITY" />
        </el-select>
      </el-form-item>
      <el-form-item label="预警级别" prop="alertLevel">
        <el-select v-model="form.alertLevel" placeholder="请选择预警级别" style="width: 100%">
          <el-option label="高风险" value="HIGH">
            <span style="color: #F56C6C">● 高风险</span>
          </el-option>
          <el-option label="中风险" value="MEDIUM">
            <span style="color: #E6A23C">● 中风险</span>
          </el-option>
          <el-option label="低风险" value="LOW">
            <span style="color: #67C23A">● 低风险</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="预警状态" prop="alertStatus">
        <el-select v-model="form.alertStatus" placeholder="请选择预警状态" style="width: 100%">
          <el-option label="待处理" value="PENDING" />
          <el-option label="已处理" value="HANDLED" />
          <el-option label="已关闭" value="CLOSED" />
        </el-select>
      </el-form-item>
      <el-form-item label="预警内容" prop="alertMessage">
        <el-input v-model="form.alertMessage" type="textarea" :rows="3" placeholder="请输入预警内容" maxlength="500" show-word-limit />
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注信息" maxlength="200" show-word-limit />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateFinancingMonitoring } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'MonitoringUpdateDialog',
  props: {
    visible: { type: Boolean, default: false },
    monitoringData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      submitting: false,
      form: {
        monitoringId: '',
        alertType: '',
        alertLevel: '',
        alertStatus: '',
        alertMessage: '',
        remark: ''
      },
      rules: {
        alertType: [{ required: true, message: '请选择预警类型', trigger: 'change' }],
        alertLevel: [{ required: true, message: '请选择预警级别', trigger: 'change' }],
        alertStatus: [{ required: true, message: '请选择预警状态', trigger: 'change' }],
        alertMessage: [{ required: true, message: '请输入预警内容', trigger: 'blur' }]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      const data = this.monitoringData || {}
      this.form = {
        monitoringId: data.monitoringId || '',
        alertType: data.alertType || '',
        alertLevel: data.alertLevel || '',
        alertStatus: data.alertStatus || '',
        alertMessage: data.alertMessage || '',
        remark: data.remark || ''
      }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        this.submitting = true
        updateFinancingMonitoring(this.form).then(response => {
          if (response && [1, 200, '1', '200'].includes(response.code)) {
            this.$message.success('更新成功')
            this.$emit('success')
            this.handleClose()
          } else {
            this.$message.error(response?.msg || '更新失败')
          }
        }).catch(error => {
          console.error('更新监控数据失败:', error)
          this.$message.error('更新失败，请稍后重试')
        }).finally(() => {
          this.submitting = false
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-form-item__label {
  font-weight: bold;
}
</style>

