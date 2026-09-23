<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="560px"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="审核类型">
        <el-radio-group v-model="form.auditType">
          <el-radio label="initial">初审</el-radio>
          <el-radio label="review">复审</el-radio>
          <el-radio label="final">终审</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="审核结果" prop="auditResult">
        <el-radio-group v-model="form.auditResult">
          <el-radio label="approved">
            <span style="color:#67C23A;">通过</span>
          </el-radio>
          <el-radio label="rejected">
            <span style="color:#F56C6C;">拒绝</span>
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="审核意见" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入审核意见"
        ></el-input>
      </el-form-item>

      <!-- 只有非终审且通过时才需要选下一审核人 -->
      <el-form-item
        label="下一审核人"
        prop="nextAuditor"
        v-if="form.auditResult === 'approved' && form.auditType !== 'final'"
      >
        <el-input v-model="form.nextAuditor" placeholder="请输入下一审核人姓名"></el-input>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { approveDataAudit, rejectDataAudit } from '@/api/enterprise/data'

export default {
  name: 'AuditActionDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    auditData: {
      type: Object,
      default: () => ({})
    },
    actionType: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      form: {
        auditType: 'initial',
        auditResult: '',
        remark: '',
        nextAuditor: ''
      },
      rules: {
        auditResult: [
          { required: true, message: '请选择审核结果', trigger: 'change' }
        ],
        remark: [
          { required: true, message: '请输入审核意见', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    dialogTitle() {
      if (this.actionType === 'approve') return '审核通过'
      if (this.actionType === 'reject') return '审核拒绝'
      return '审核操作'
    }
  },
  watch: {
    visible(val) {
      if (val) this.initForm()
    }
  },
  methods: {
    initForm() {
      this.form = {
        auditType: 'initial',
        // 根据外部传入的 actionType 预设结果
        auditResult: this.actionType === 'approve' ? 'approved'
          : this.actionType === 'reject' ? 'rejected' : '',
        remark: '',
        nextAuditor: ''
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.form && this.$refs.form.resetFields()
    },
    handleConfirm() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.loading = true
        try {
          const params = {
            id: this.auditData.id,
            result: this.form.auditResult,
            auditor: this.form.nextAuditor || '',
            remark: this.form.remark
          }
          let response
          if (this.form.auditResult === 'approved') {
            response = await approveDataAudit(params)
          } else {
            response = await rejectDataAudit(params)
          }
          if (response && response.result != 500) {
            this.$message.success(
              this.form.auditResult === 'approved' ? '审核通过' : '已拒绝'
            )
            this.handleClose()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '审核操作失败')
          }
        } catch (error) {
          this.$message.error(error.message || '审核操作失败')
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>
