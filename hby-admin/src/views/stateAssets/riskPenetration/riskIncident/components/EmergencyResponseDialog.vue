<template>
  <el-dialog
    title="应急响应处理"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <div class="emergency-response">
      <!-- 事件信息 -->
      <div class="incident-info">
        <h4>事件信息</h4>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>事件编号：</label>
              <span class="incident-number">{{ incidentData.incidentNumber || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>企业名称：</label>
              <span>{{ incidentData.enterpriseName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>风险等级：</label>
              <el-tag :type="getRiskLevelTagType(incidentData.riskLevel)" size="small">
                {{ getRiskLevelLabel(incidentData.riskLevel) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>事件标题：</label>
              <span>{{ incidentData.incidentTitle || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>紧急程度：</label>
              <el-tag :type="getUrgencyLevelTagType(incidentData.urgencyLevel)" size="small">
                {{ getUrgencyLevelLabel(incidentData.urgencyLevel) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>发生时间：</label>
              <span>{{ incidentData.occurrenceTime || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 应急响应表单 -->
      <div class="response-form">
        <h4>应急响应处理</h4>
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          label-width="120px"
          v-loading="loading"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="响应级别" prop="responseLevel">
                <el-select v-model="form.responseLevel" placeholder="请选择响应级别" style="width: 100%">
                  <el-option label="一级响应" value="LEVEL_1" />
                  <el-option label="二级响应" value="LEVEL_2" />
                  <el-option label="三级响应" value="LEVEL_3" />
                  <el-option label="四级响应" value="LEVEL_4" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="响应时间" prop="responseTime">
                <el-date-picker
                  v-model="form.responseTime"
                  type="datetime"
                  placeholder="选择响应时间"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="响应负责人" prop="responseLeader">
                <el-input
                  v-model="form.responseLeader"
                  placeholder="请输入响应负责人"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="contactPhone">
                <el-input
                  v-model="form.contactPhone"
                  placeholder="请输入联系电话"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="响应团队" prop="responseTeam">
            <el-input
              v-model="form.responseTeam"
              type="textarea"
              :rows="2"
              placeholder="请输入响应团队成员，多个成员用分号分隔"
            />
          </el-form-item>

          <!-- 应急措施 -->
          <el-form-item label="应急措施" prop="emergencyMeasures">
            <div class="measures-container">
              <div
                v-for="(measure, index) in form.emergencyMeasures"
                :key="index"
                class="measure-item"
              >
                <el-row :gutter="10">
                  <el-col :span="4">
                    <el-select v-model="measure.category" placeholder="措施类别">
                      <el-option label="人员疏散" value="EVACUATION" />
                      <el-option label="设备关停" value="SHUTDOWN" />
                      <el-option label="资金冻结" value="FUND_FREEZE" />
                      <el-option label="信息隔离" value="ISOLATION" />
                      <el-option label="外部支援" value="EXTERNAL_SUPPORT" />
                      <el-option label="其他" value="OTHER" />
                    </el-select>
                  </el-col>
                  <el-col :span="16">
                    <el-input
                      v-model="measure.description"
                      placeholder="请描述具体的应急措施"
                    />
                  </el-col>
                  <el-col :span="3">
                    <el-select v-model="measure.priority" placeholder="优先级">
                      <el-option label="高" value="HIGH" />
                      <el-option label="中" value="MEDIUM" />
                      <el-option label="低" value="LOW" />
                    </el-select>
                  </el-col>
                  <el-col :span="1">
                    <el-button
                      type="danger"
                      icon="el-icon-delete"
                      size="small"
                      @click="removeMeasure(index)"
                      :disabled="form.emergencyMeasures.length <= 1"
                    />
                  </el-col>
                </el-row>
              </div>
              <el-button
                type="dashed"
                icon="el-icon-plus"
                @click="addMeasure"
                style="width: 100%; margin-top: 10px;"
              >
                添加应急措施
              </el-button>
            </div>
          </el-form-item>

          <!-- 资源调配 -->
          <el-form-item label="资源调配" prop="resourceAllocation">
            <el-input
              v-model="form.resourceAllocation"
              type="textarea"
              :rows="3"
              placeholder="请描述需要调配的人力、物力、财力等资源"
            />
          </el-form-item>

          <!-- 通知范围 -->
          <el-form-item label="通知范围" prop="notificationScope">
            <el-checkbox-group v-model="form.notificationScope">
              <el-checkbox label="INTERNAL_MANAGEMENT">内部管理层</el-checkbox>
              <el-checkbox label="BOARD_DIRECTORS">董事会</el-checkbox>
              <el-checkbox label="REGULATORY_AUTHORITY">监管部门</el-checkbox>
              <el-checkbox label="MEDIA">媒体</el-checkbox>
              <el-checkbox label="PUBLIC">公众</el-checkbox>
              <el-checkbox label="STAKEHOLDERS">利益相关方</el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <!-- 预期效果 -->
          <el-form-item label="预期效果" prop="expectedOutcome">
            <el-input
              v-model="form.expectedOutcome"
              type="textarea"
              :rows="3"
              placeholder="请描述采取应急措施后的预期效果"
            />
          </el-form-item>

          <!-- 风险评估 -->
          <el-form-item label="风险评估" prop="riskAssessment">
            <el-input
              v-model="form.riskAssessment"
              type="textarea"
              :rows="3"
              placeholder="请评估当前风险状况和可能的后续风险"
            />
          </el-form-item>

          <!-- 时间要求 -->
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="预计完成时间" prop="estimatedCompletionTime">
                <el-date-picker
                  v-model="form.estimatedCompletionTime"
                  type="datetime"
                  placeholder="选择预计完成时间"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="跟进频率" prop="followUpFrequency">
                <el-select v-model="form.followUpFrequency" placeholder="请选择跟进频率" style="width: 100%">
                  <el-option label="每小时" value="HOURLY" />
                  <el-option label="每4小时" value="EVERY_4_HOURS" />
                  <el-option label="每8小时" value="EVERY_8_HOURS" />
                  <el-option label="每12小时" value="EVERY_12_HOURS" />
                  <el-option label="每日" value="DAILY" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 备注 -->
          <el-form-item label="备注" prop="remarks">
            <el-input
              v-model="form.remarks"
              type="textarea"
              :rows="2"
              placeholder="其他需要说明的情况"
            />
          </el-form-item>

          <!-- 附件上传 -->
          <el-form-item label="相关附件" prop="attachments">
            <el-upload
              ref="upload"
              :action="uploadUrl"
              :headers="uploadHeaders"
              :file-list="fileList"
              :on-success="handleUploadSuccess"
              :on-remove="handleRemoveFile"
              :before-upload="beforeUpload"
              multiple
              :limit="5"
            >
              <el-button size="small" type="primary">
                <i class="el-icon-upload"></i>
                上传附件
              </el-button>
              <div slot="tip" class="el-upload__tip">
                支持上传应急预案、处理方案等文件，单个文件不超过10MB
              </div>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>

      <!-- 响应预览 -->
      <div class="response-preview">
        <h4>响应方案预览</h4>
        <div class="preview-content">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="preview-card">
                <div class="card-header">响应级别</div>
                <div class="card-content">
                  <el-tag :type="getResponseLevelTagType(form.responseLevel)" size="medium">
                    {{ getResponseLevelLabel(form.responseLevel) }}
                  </el-tag>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="preview-card">
                <div class="card-header">应急措施</div>
                <div class="card-content">
                  <span class="measures-count">{{ form.emergencyMeasures.length }}项措施</span>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="preview-card">
                <div class="card-header">通知范围</div>
                <div class="card-content">
                  <span class="notification-count">{{ form.notificationScope.length }}个对象</span>
                </div>
              </div>
            </el-col>
          </el-row>

          <div class="measures-summary">
            <h5>应急措施汇总</h5>
            <el-table :data="form.emergencyMeasures" border stripe size="small">
              <el-table-column prop="category" label="类别" width="100">
                <template slot-scope="scope">
                  {{ getMeasureCategoryLabel(scope.row.category) }}
                </template>
              </el-table-column>
              <el-table-column prop="description" label="措施描述" show-overflow-tooltip />
              <el-table-column prop="priority" label="优先级" width="80">
                <template slot-scope="scope">
                  <el-tag :type="getPriorityTagType(scope.row.priority)" size="mini">
                    {{ scope.row.priority }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="warning" @click="handleSaveDraft" :loading="loading">
        保存草稿
      </el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        启动应急响应
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { initiateEmergencyResponse, saveEmergencyDraft } from '@/api/stateAssets/riskIncident'

export default {
  name: 'EmergencyResponseDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    incidentData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        responseLevel: '',
        responseTime: '',
        responseLeader: '',
        contactPhone: '',
        responseTeam: '',
        emergencyMeasures: [
          {
            category: '',
            description: '',
            priority: 'MEDIUM'
          }
        ],
        resourceAllocation: '',
        notificationScope: [],
        expectedOutcome: '',
        riskAssessment: '',
        estimatedCompletionTime: '',
        followUpFrequency: '',
        remarks: '',
        attachments: []
      },
      rules: {
        responseLevel: [
          { required: true, message: '请选择响应级别', trigger: 'change' }
        ],
        responseTime: [
          { required: true, message: '请选择响应时间', trigger: 'change' }
        ],
        responseLeader: [
          { required: true, message: '请输入响应负责人', trigger: 'blur' }
        ],
        contactPhone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        resourceAllocation: [
          { required: true, message: '请描述资源调配情况', trigger: 'blur' }
        ],
        expectedOutcome: [
          { required: true, message: '请描述预期效果', trigger: 'blur' }
        ],
        riskAssessment: [
          { required: true, message: '请进行风险评估', trigger: 'blur' }
        ],
        estimatedCompletionTime: [
          { required: true, message: '请选择预计完成时间', trigger: 'change' }
        ],
        followUpFrequency: [
          { required: true, message: '请选择跟进频率', trigger: 'change' }
        ]
      },
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/common/upload',
      uploadHeaders: {
        'Authorization': 'Bearer ' + this.$store.getters.token
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
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      this.form = {
        responseLevel: this.getDefaultResponseLevel(),
        responseTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
        responseLeader: this.$store.getters.userInfo.userName,
        contactPhone: '',
        responseTeam: '',
        emergencyMeasures: [
          {
            category: '',
            description: '',
            priority: 'MEDIUM'
          }
        ],
        resourceAllocation: '',
        notificationScope: ['INTERNAL_MANAGEMENT'],
        expectedOutcome: '',
        riskAssessment: '',
        estimatedCompletionTime: '',
        followUpFrequency: 'EVERY_4_HOURS',
        remarks: '',
        attachments: []
      }
      this.fileList = []
    },

    // 根据风险等级确定默认响应级别
    getDefaultResponseLevel() {
      const riskLevel = this.incidentData.riskLevel
      switch (riskLevel) {
        case 'CRITICAL': return 'LEVEL_1'
        case 'HIGH': return 'LEVEL_2'
        case 'MEDIUM': return 'LEVEL_3'
        case 'LOW': return 'LEVEL_4'
        default: return 'LEVEL_3'
      }
    },

    // 添加应急措施
    addMeasure() {
      this.form.emergencyMeasures.push({
        category: '',
        description: '',
        priority: 'MEDIUM'
      })
    },

    // 移除应急措施
    removeMeasure(index) {
      if (this.form.emergencyMeasures.length > 1) {
        this.form.emergencyMeasures.splice(index, 1)
      }
    },

    // 保存草稿
    async handleSaveDraft() {
      try {
        this.loading = true

        const formData = {
          riskIncidentId: this.incidentData.riskIncidentId,
          ...this.form,
          emergencyMeasures: JSON.stringify(this.form.emergencyMeasures),
          notificationScope: this.form.notificationScope.join(','),
          attachments: this.form.attachments.join(','),
          createBy: this.$store.getters.userInfo.userName
        }

        const response = await saveEmergencyDraft(formData)
        
        if (response.code === 200) {
          this.$message.success('应急响应草稿保存成功')
        }
      } catch (error) {
        this.$message.error('保存草稿失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 提交应急响应
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        
        await this.$confirm('确定要启动应急响应吗？启动后将立即执行相关措施。', '提示', {
          confirmButtonText: '确定启动',
          cancelButtonText: '取消',
          type: 'warning'
        })

        this.loading = true

        const formData = {
          riskIncidentId: this.incidentData.riskIncidentId,
          ...this.form,
          emergencyMeasures: JSON.stringify(this.form.emergencyMeasures),
          notificationScope: this.form.notificationScope.join(','),
          attachments: this.form.attachments.join(','),
          initiateBy: this.$store.getters.userInfo.userName,
          initiateTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
        }

        const response = await initiateEmergencyResponse(formData)
        
        if (response.code === 200) {
          this.$message.success('应急响应已启动')
          this.$emit('success')
          this.handleClose()
        }
      } catch (error) {
        if (error !== 'cancel' && error !== 'validation failed') {
          this.$message.error('启动应急响应失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 文件上传处理
    beforeUpload(file) {
      const isValidType = ['image/jpeg', 'image/png', 'image/gif', 'application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'].includes(file.type)
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isValidType) {
        this.$message.error('只能上传图片、PDF、Word文档格式的文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      return true
    },

    handleUploadSuccess(response, file) {
      if (response.code === 200) {
        this.form.attachments.push(response.data.url)
        this.$message.success('文件上传成功')
      } else {
        this.$message.error('文件上传失败：' + response.message)
      }
    },

    handleRemoveFile(file, fileList) {
      const index = this.form.attachments.indexOf(file.response?.data?.url)
      if (index > -1) {
        this.form.attachments.splice(index, 1)
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
      this.fileList = []
    },

    // 工具方法
    getRiskLevelTagType(level) {
      const levelMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return levelMap[level] || ''
    },

    getRiskLevelLabel(level) {
      const labelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '极高风险'
      }
      return labelMap[level] || level
    },

    getUrgencyLevelTagType(level) {
      const levelMap = {
        'NORMAL': 'info',
        'URGENT': 'warning',
        'VERY_URGENT': 'danger',
        'CRITICAL': 'danger'
      }
      return levelMap[level] || ''
    },

    getUrgencyLevelLabel(level) {
      const labelMap = {
        'NORMAL': '一般',
        'URGENT': '紧急',
        'VERY_URGENT': '非常紧急',
        'CRITICAL': '特急'
      }
      return labelMap[level] || level
    },

    getResponseLevelTagType(level) {
      const levelMap = {
        'LEVEL_1': 'danger',
        'LEVEL_2': 'warning',
        'LEVEL_3': 'primary',
        'LEVEL_4': 'info'
      }
      return levelMap[level] || ''
    },

    getResponseLevelLabel(level) {
      const labelMap = {
        'LEVEL_1': '一级响应',
        'LEVEL_2': '二级响应',
        'LEVEL_3': '三级响应',
        'LEVEL_4': '四级响应'
      }
      return labelMap[level] || level
    },

    getMeasureCategoryLabel(category) {
      const labelMap = {
        'EVACUATION': '人员疏散',
        'SHUTDOWN': '设备关停',
        'FUND_FREEZE': '资金冻结',
        'ISOLATION': '信息隔离',
        'EXTERNAL_SUPPORT': '外部支援',
        'OTHER': '其他'
      }
      return labelMap[category] || category
    },

    getPriorityTagType(priority) {
      const priorityMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return priorityMap[priority] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.emergency-response {
  .incident-info,
  .response-form,
  .response-preview {
    margin-bottom: 24px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 6px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .info-item {
    margin-bottom: 12px;
    
    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
    }

    .incident-number {
      font-family: 'Courier New', monospace;
      color: #409eff;
      font-weight: 600;
    }
  }

  .measures-container {
    .measure-item {
      margin-bottom: 12px;
      padding: 12px;
      background: white;
      border-radius: 6px;
      border: 1px solid #ebeef5;
    }
  }

  .response-preview {
    .preview-content {
      .preview-card {
        text-align: center;
        padding: 16px;
        background: white;
        border-radius: 6px;
        border: 1px solid #ebeef5;

        .card-header {
          font-size: 14px;
          color: #909399;
          margin-bottom: 12px;
        }

        .card-content {
          .measures-count,
          .notification-count {
            font-size: 16px;
            font-weight: 600;
            color: #303133;
          }
        }
      }

      .measures-summary {
        margin-top: 20px;
        padding: 16px;
        background: white;
        border-radius: 6px;

        h5 {
          margin: 0 0 16px 0;
          color: #303133;
          font-size: 14px;
          font-weight: 600;
        }
      }
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
