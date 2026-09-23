<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="60%"
    :close-on-click-modal="false"
  >
    <el-form
      ref="processForm"
      :model="processForm"
      :rules="processRules"
      label-width="120px"
      v-loading="loading"
    >
      <!-- 预警信息展示 -->
      <el-card shadow="never" class="warning-info-card">
        <div slot="header" class="card-header">
          <span>预警信息</span>
        </div>
        <div v-if="!isBatchProcess">
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="info-item">
                <label>项目名称：</label>
                <span>{{ currentWarning.assessmentName }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <label>风险等级：</label>
                <el-tag :type="getWarningTypeTagType(currentWarning.overallRiskLevel)">
                  {{ getRiskLevelName(currentWarning.overallRiskLevel) }}
                </el-tag>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <label>风险评分：</label>
                <span :class="getRiskScoreClass(currentWarning.overallRiskScore)">
                  {{ currentWarning.overallRiskScore?.toFixed(1) || '0.0' }}
                </span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <label>预警时间：</label>
                <span>{{ formatDate(currentWarning.createTime) }}</span>
              </div>
            </el-col>
            <el-col :span="24">
              <div class="info-item">
                <label>预警信息：</label>
                <span>{{ currentWarning.warningMessage }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
        <div v-else>
          <el-alert
            :title="`批量处理 ${batchWarnings.length} 个预警`"
            type="info"
            :closable="false"
            show-icon
          />
          <div class="batch-warning-list">
            <el-tag
              v-for="warning in batchWarnings"
              :key="warning.id"
              :type="getWarningTypeTagType(warning.overallRiskLevel)"
              class="batch-warning-tag"
            >
              {{ warning.assessmentName }}
            </el-tag>
          </div>
        </div>
      </el-card>

      <!-- 处理表单 -->
      <el-form-item label="处理动作" prop="action">
        <el-radio-group v-model="processForm.action">
          <el-radio :label="1">忽略预警</el-radio>
          <el-radio :label="2">标记为处理中</el-radio>
          <el-radio :label="3">标记为已处理</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="处理人" prop="processorId">
        <el-select
          v-model="processForm.processorId"
          placeholder="请选择处理人"
          filterable
          style="width: 100%"
        >
          <el-option
            v-for="user in userList"
            :key="user.id"
            :label="user.name"
            :value="user.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="预计完成时间" prop="expectedCompletionTime" v-if="processForm.action === 2">
        <el-date-picker
          v-model="processForm.expectedCompletionTime"
          type="datetime"
          placeholder="选择预计完成时间"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="处理措施" prop="processingMeasures">
        <el-input
          v-model="processForm.processingMeasures"
          type="textarea"
          :rows="4"
          placeholder="请描述具体的处理措施和计划"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="处理备注" prop="remarks">
        <el-input
          v-model="processForm.remarks"
          type="textarea"
          :rows="3"
          placeholder="请输入处理备注"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>

      <!-- 风险缓解建议 -->
      <el-form-item label="缓解建议" v-if="mitigationSuggestions.length > 0">
        <div class="suggestions-container">
          <el-alert
            v-for="(suggestion, index) in mitigationSuggestions"
            :key="index"
            :title="suggestion"
            type="info"
            :closable="false"
            show-icon
            class="suggestion-item"
          />
        </div>
      </el-form-item>

      <!-- 附件上传 -->
      <el-form-item label="相关附件">
        <el-upload
          ref="upload"
          :action="uploadUrl"
          :headers="uploadHeaders"
          :file-list="fileList"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :on-remove="handleFileRemove"
          :before-upload="beforeUpload"
          multiple
          :limit="5"
        >
          <el-button size="small" type="primary">
            <i class="el-icon-upload"></i> 上传附件
          </el-button>
          <div slot="tip" class="el-upload__tip">
            只能上传jpg/png/pdf/doc/docx文件，且不超过10MB，最多5个文件
          </div>
        </el-upload>
      </el-form-item>

      <!-- 通知设置 -->
      <el-form-item label="通知设置">
        <el-checkbox-group v-model="processForm.notificationSettings">
          <el-checkbox label="email">邮件通知</el-checkbox>
          <el-checkbox label="sms">短信通知</el-checkbox>
          <el-checkbox label="system">系统通知</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <!-- 跟进提醒 -->
      <el-form-item label="跟进提醒" v-if="processForm.action === 2">
        <el-checkbox v-model="processForm.followUpReminder">
          设置跟进提醒
        </el-checkbox>
        <el-select
          v-if="processForm.followUpReminder"
          v-model="processForm.reminderInterval"
          placeholder="提醒间隔"
          style="margin-left: 10px; width: 150px"
        >
          <el-option label="每天" :value="1" />
          <el-option label="每3天" :value="3" />
          <el-option label="每周" :value="7" />
        </el-select>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
        确定处理
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  handleRiskWarning,
  getRiskMitigationSuggestions
} from '@/api/contract/riskAssessment'

export default {
  name: 'RiskWarningProcess',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      submitLoading: false,
      isBatchProcess: false,
      currentWarning: {},
      batchWarnings: [],
      processForm: {
        action: 2,
        processorId: null,
        expectedCompletionTime: null,
        processingMeasures: '',
        remarks: '',
        notificationSettings: ['system'],
        followUpReminder: false,
        reminderInterval: 3
      },
      processRules: {
        action: [
          { required: true, message: '请选择处理动作', trigger: 'change' }
        ],
        processorId: [
          { required: true, message: '请选择处理人', trigger: 'change' }
        ],
        processingMeasures: [
          { required: true, message: '请描述处理措施', trigger: 'blur' }
        ]
      },
      userList: [],
      mitigationSuggestions: [],
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/contract/common/upload',
      uploadHeaders: {
        'Authorization': 'Bearer ' + this.$store.getters.token
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isBatchProcess ? '批量处理预警' : '处理风险预警'
    }
  },
  created() {
    this.fetchUserList()
  },
  methods: {
    async showProcess(warning) {
      this.isBatchProcess = false
      this.currentWarning = warning
      this.dialogVisible = true
      this.resetForm()
      await this.fetchMitigationSuggestions(warning)
    },

    async showBatchProcess(warnings) {
      this.isBatchProcess = true
      this.batchWarnings = warnings
      this.dialogVisible = true
      this.resetForm()
    },

    resetForm() {
      this.processForm = {
        action: 2,
        processorId: null,
        expectedCompletionTime: null,
        processingMeasures: '',
        remarks: '',
        notificationSettings: ['system'],
        followUpReminder: false,
        reminderInterval: 3
      }
      this.fileList = []
      this.mitigationSuggestions = []
      if (this.$refs.processForm) {
        this.$refs.processForm.clearValidate()
      }
    },

    async fetchUserList() {
      // 暂时使用静态用户列表数据
      this.userList = [
        {
          id: '1',
          username: 'admin',
          realname: '系统管理员',
          orgname: '总公司'
        },
        {
          id: '2',
          username: 'zhangsan',
          realname: '张三',
          orgname: '技术部'
        },
        {
          id: '3',
          username: 'lisi',
          realname: '李四',
          orgname: '财务部'
        },
        {
          id: '4',
          username: 'wangwu',
          realname: '王五',
          orgname: '人事部'
        },
        {
          id: '5',
          username: 'zhaoliu',
          realname: '赵六',
          orgname: '市场部'
        }
      ]
      console.log('使用静态用户列表:', this.userList)
    },

    async fetchMitigationSuggestions(warning) {
      if (!warning.riskCategory || !warning.overallRiskLevel) return
      
      try {
        const response = await getRiskMitigationSuggestions(
          warning.riskCategory,
          warning.overallRiskLevel
        )
        if (response.code === 200) {
          this.mitigationSuggestions = response.data || []
        }
      } catch (error) {
        console.error('获取缓解建议失败：', error)
      }
    },

    async handleSubmit() {
      try {
        await this.$refs.processForm.validate()
        
        this.submitLoading = true
        
        if (this.isBatchProcess) {
          await this.processBatchWarnings()
        } else {
          await this.processSingleWarning()
        }
        
        this.$message.success('处理成功')
        this.dialogVisible = false
        this.$emit('fetch-data')
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('处理失败：' + error.message)
        }
      } finally {
        this.submitLoading = false
      }
    },

    async processSingleWarning() {
      const response = await handleRiskWarning(
        this.currentWarning.id,
        this.processForm.action,
        this.processForm.remarks
      )
      
      if (response.code !== 200) {
        throw new Error(response.message || '处理失败')
      }
    },

    async processBatchWarnings() {
      const promises = this.batchWarnings.map(warning =>
        handleRiskWarning(
          warning.id,
          this.processForm.action,
          this.processForm.remarks
        )
      )
      
      const results = await Promise.allSettled(promises)
      const failedCount = results.filter(result => result.status === 'rejected').length
      
      if (failedCount > 0) {
        throw new Error(`批量处理完成，${failedCount}个处理失败`)
      }
    },

    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.$message.success('文件上传成功')
        this.fileList = fileList
      } else {
        this.$message.error('文件上传失败：' + response.message)
      }
    },

    handleUploadError(error, file, fileList) {
      this.$message.error('文件上传失败：' + error.message)
    },

    handleFileRemove(file, fileList) {
      this.fileList = fileList
    },

    beforeUpload(file) {
      const isValidType = ['image/jpeg', 'image/png', 'application/pdf', 
                          'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document']
                          .includes(file.type)
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isValidType) {
        this.$message.error('只能上传jpg/png/pdf/doc/docx格式的文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      return true
    },

    getWarningTypeTagType(riskLevel) {
      const typeMap = {
        1: 'info',
        2: 'warning',
        3: 'danger',
        4: 'danger'
      }
      return typeMap[riskLevel] || 'info'
    },

    getRiskScoreClass(score) {
      if (score >= 85) return 'risk-score-extreme'
      if (score >= 70) return 'risk-score-high'
      if (score >= 50) return 'risk-score-medium'
      return 'risk-score-low'
    },

    getRiskLevelName(level) {
      const levelMap = {
        1: '低风险',
        2: '中风险',
        3: '高风险',
        4: '极高风险'
      }
      return levelMap[level] || '未知'
    },

    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.warning-info-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-item {
  margin-bottom: 10px;
}

.info-item label {
  font-weight: bold;
  color: #606266;
  margin-right: 10px;
}

.batch-warning-list {
  margin-top: 10px;
}

.batch-warning-tag {
  margin-right: 8px;
  margin-bottom: 8px;
}

.suggestions-container {
  max-height: 200px;
  overflow-y: auto;
}

.suggestion-item {
  margin-bottom: 8px;
}

.risk-score-extreme {
  color: #f56c6c;
  font-weight: bold;
}

.risk-score-high {
  color: #e6a23c;
  font-weight: bold;
}

.risk-score-medium {
  color: #409eff;
}

.risk-score-low {
  color: #67c23a;
}

.dialog-footer {
  text-align: right;
}
</style>
