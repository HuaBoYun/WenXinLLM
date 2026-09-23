<template>
  <el-dialog
    title="预算审批"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <div v-if="budgetData">
      <!-- 预算基本信息 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>预算基本信息</span>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="预算名称">{{ budgetData.budgetName }}</el-descriptions-item>
          <el-descriptions-item label="预算类型">{{ budgetData.budgetType }}</el-descriptions-item>
          <el-descriptions-item label="预算年度">{{ budgetData.budgetYear }}</el-descriptions-item>
          <el-descriptions-item label="预算总额">{{ budgetData.totalBudgetAmount }}万元</el-descriptions-item>
          <el-descriptions-item label="负责人">{{ budgetData.responsiblePerson }}</el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="getStatusType(budgetData.budgetStatus)">
              {{ budgetData.budgetStatus }}
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
export default {
  name: 'BudgetApprovalDialog',
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
      this.$refs.approvalForm.validate((valid) => {
        if (valid) {
          this.loading = true
          // 这里应该调用API提交审批
          setTimeout(() => {
            this.loading = false
            this.$message.success('审批提交成功')
            this.$emit('refresh')
            this.handleClose()
          }, 1000)
        }
      })
    },
    getStatusType(status) {
      const statusMap = {
        '待编制': 'info',
        '编制中': 'warning',
        '已编制': 'success',
        '已暂停': 'warning',
        '已取消': 'danger'
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
