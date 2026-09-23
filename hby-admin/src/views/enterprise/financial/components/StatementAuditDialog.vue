<template>
  <el-dialog
    title="财务报表编制流程审核"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <div v-if="processData">
      <!-- 流程基本信息 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>流程基本信息</span>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="流程名称">{{ processData.processName }}</el-descriptions-item>
          <el-descriptions-item label="报表类型">{{ processData.statementType }}</el-descriptions-item>
          <el-descriptions-item label="报告期间">{{ processData.reportPeriod }}</el-descriptions-item>
          <el-descriptions-item label="负责人">{{ processData.responsiblePerson }}</el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="getStatusType(processData.processStatus)">
              {{ processData.processStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审核状态">
            <el-tag :type="getAuditStatusType(processData.auditStatus)">
              {{ processData.auditStatus }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 审核表单 -->
      <el-form ref="auditForm" :model="auditForm" :rules="auditRules" label-width="120px">
        <el-form-item label="审核类型" prop="auditType">
          <el-radio-group v-model="auditForm.auditType">
            <el-radio label="初审">初审</el-radio>
            <el-radio label="复审">复审</el-radio>
            <el-radio label="终审">终审</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="审核结果" prop="auditResult">
          <el-radio-group v-model="auditForm.auditResult">
            <el-radio label="通过">审核通过</el-radio>
            <el-radio label="不通过">审核不通过</el-radio>
            <el-radio label="需要修改">需要修改</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="质量评分">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="准确性" prop="accuracyScore">
                <el-input-number v-model="auditForm.accuracyScore" :min="0" :max="100" />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="完整性" prop="completenessScore">
                <el-input-number v-model="auditForm.completenessScore" :min="0" :max="100" />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="及时性" prop="timelinessScore">
                <el-input-number v-model="auditForm.timelinessScore" :min="0" :max="100" />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="合规性" prop="complianceScore">
                <el-input-number v-model="auditForm.complianceScore" :min="0" :max="100" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item label="审核意见" prop="auditOpinion">
          <el-input
            v-model="auditForm.auditOpinion"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
          />
        </el-form-item>

        <el-form-item label="问题描述" v-if="auditForm.auditResult !== '通过'">
          <el-input
            v-model="auditForm.issueDescription"
            type="textarea"
            :rows="3"
            placeholder="请描述发现的问题"
          />
        </el-form-item>

        <el-form-item label="整改建议" v-if="auditForm.auditResult !== '通过'">
          <el-input
            v-model="auditForm.improvementSuggestion"
            type="textarea"
            :rows="3"
            placeholder="请提出整改建议"
          />
        </el-form-item>
      </el-form>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">提交审核</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'StatementAuditDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    processData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      auditForm: {
        auditType: '初审',
        auditResult: '通过',
        accuracyScore: 100,
        completenessScore: 100,
        timelinessScore: 100,
        complianceScore: 100,
        auditOpinion: '',
        issueDescription: '',
        improvementSuggestion: ''
      },
      auditRules: {
        auditType: [
          { required: true, message: '请选择审核类型', trigger: 'change' }
        ],
        auditResult: [
          { required: true, message: '请选择审核结果', trigger: 'change' }
        ],
        auditOpinion: [
          { required: true, message: '请输入审核意见', trigger: 'blur' }
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
      this.$refs.auditForm.resetFields()
      this.auditForm = {
        auditType: '初审',
        auditResult: '通过',
        accuracyScore: 100,
        completenessScore: 100,
        timelinessScore: 100,
        complianceScore: 100,
        auditOpinion: '',
        issueDescription: '',
        improvementSuggestion: ''
      }
    },
    handleSubmit() {
      this.$refs.auditForm.validate((valid) => {
        if (valid) {
          this.loading = true
          // 这里应该调用API提交审核
          setTimeout(() => {
            this.loading = false
            this.$message.success('审核提交成功')
            this.$emit('refresh')
            this.handleClose()
          }, 1000)
        }
      })
    },
    getStatusType(status) {
      const statusMap = {
        '待开始': 'info',
        '进行中': 'warning',
        '已完成': 'success',
        '已暂停': 'warning',
        '已取消': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getAuditStatusType(status) {
      const statusMap = {
        '待审核': 'warning',
        '初审中': 'primary',
        '复审中': 'primary',
        '终审中': 'primary',
        '审核通过': 'success',
        '审核不通过': 'danger'
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
