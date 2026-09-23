<template>
  <el-dialog
    title="经营计划审批"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <div v-if="planData">
      <!-- 计划基本信息 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>计划基本信息</span>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="计划名称">{{ planData.planName }}</el-descriptions-item>
          <el-descriptions-item label="计划类型">{{ planData.planType }}</el-descriptions-item>
          <el-descriptions-item label="计划年度">{{ planData.planYear }}</el-descriptions-item>
          <el-descriptions-item label="制定负责人">{{ planData.planManager }}</el-descriptions-item>
          <el-descriptions-item label="计划状态">
            <el-tag :type="getFormulationStatusType(planData.planStatus)">
              {{ planData.planStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="当前审批状态">
            <el-tag :type="getApprovalStatusType(planData.approvalStatus)">
              {{ planData.approvalStatus }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 审批表单 -->
      <el-form ref="approvalForm" :model="approvalForm" :rules="approvalRules" label-width="120px">
        <el-form-item label="审批类型" prop="approvalType">
          <el-radio-group v-model="approvalForm.approvalType">
            <el-radio label="初审">初审</el-radio>
            <el-radio label="终审">终审</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="审批结果" prop="approvalResult">
          <el-radio-group v-model="approvalForm.approvalResult">
            <el-radio label="通过">审批通过</el-radio>
            <el-radio label="不通过">审批不通过</el-radio>
            <el-radio label="需要修改">需要修改</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="审批意见" prop="approvalOpinion">
          <el-input
            v-model="approvalForm.approvalOpinion"
            type="textarea"
            :rows="4"
            placeholder="请输入审批意见"
          />
        </el-form-item>

        <el-form-item label="问题描述" v-if="approvalForm.approvalResult !== '通过'">
          <el-input
            v-model="approvalForm.issueDescription"
            type="textarea"
            :rows="3"
            placeholder="请描述发现的问题"
          />
        </el-form-item>

        <el-form-item label="修改建议" v-if="approvalForm.approvalResult !== '通过'">
          <el-input
            v-model="approvalForm.modificationSuggestion"
            type="textarea"
            :rows="3"
            placeholder="请提出修改建议"
          />
        </el-form-item>
      </el-form>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">提交审批</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { firstApproval, finalApproval } from '@/api/enterprise/operationPlan'

export default {
  name: 'PlanApprovalDialog',
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
      approvalForm: {
        approvalType: '初审',
        approvalResult: '通过',
        approvalOpinion: '',
        issueDescription: '',
        modificationSuggestion: ''
      },
      approvalRules: {
        approvalType: [
          { required: true, message: '请选择审批类型', trigger: 'change' }
        ],
        approvalResult: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ],
        approvalOpinion: [
          { required: true, message: '请输入审批意见', trigger: 'blur' }
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
      this.$refs.approvalForm.resetFields()
      this.approvalForm = {
        approvalType: '初审',
        approvalResult: '通过',
        approvalOpinion: '',
        issueDescription: '',
        modificationSuggestion: ''
      }
    },
    handleSubmit() {
      this.$refs.approvalForm.validate(async(valid) => {
        if (valid) {
          this.loading = true
          try {
            const updateBy = this.$store && this.$store.getters && this.$store.getters.name ? this.$store.getters.name : 'admin'
            const planId = this.planData.planId
            const approver = updateBy
            const opinion = this.approvalForm.approvalOpinion
            const approvalResult = this.approvalForm.approvalResult
            if (this.approvalForm.approvalType === '初审') {
              await firstApproval(planId, approver, opinion, approvalResult, updateBy)
            } else {
              await finalApproval(planId, approver, opinion, approvalResult, updateBy)
            }
            this.$message.success('审批提交成功')
            this.$emit('refresh')
            this.handleClose()
          } catch (error) {
            this.$message.error('审批提交失败: ' + (error.message || '未知错误'))
          } finally {
            this.loading = false
          }
        }
      })
    },
    getFormulationStatusType(status) {
      const statusMap = {
        '待制定': 'info',
        '制定中': 'warning',
        '已制定': 'success',
        '已暂停': 'warning',
        '已取消': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getApprovalStatusType(status) {
      const statusMap = {
        '待审批': 'warning',
        '初审中': 'primary',
        '终审中': 'primary',
        '审批通过': 'success',
        '审批不通过': 'danger'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}
</style>
