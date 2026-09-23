<template>
  <el-dialog
    title="更新实施进度"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <div class="progress-update">
      <!-- 措施信息 -->
      <div class="measure-info">
        <h4>措施信息</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>企业名称：</label>
              <span>{{ measureData.enterpriseName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>措施名称：</label>
              <span>{{ measureData.measureName || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>当前进度：</label>
              <span class="current-progress">{{ measureData.implementationProgress || 0 }}%</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>实施状态：</label>
              <el-tag :type="getImplementationStatusTagType(measureData.implementationStatus)">
                {{ getImplementationStatusLabel(measureData.implementationStatus) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 进度更新表单 -->
      <div class="progress-form">
        <h4>进度更新</h4>
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          label-width="120px"
          v-loading="loading"
        >
          <el-form-item label="新进度" prop="newProgress">
            <div class="progress-input">
              <el-slider
                v-model="form.newProgress"
                :min="currentProgress"
                :max="100"
                :step="5"
                show-stops
                show-input
                :format-tooltip="formatTooltip"
                @change="handleProgressChange"
              />
            </div>
            <div class="progress-info">
              <span class="progress-change">
                进度变化：
                <span :class="getProgressChangeClass()">
                  {{ getProgressChangeText() }}
                </span>
              </span>
            </div>
          </el-form-item>

          <el-form-item label="更新日期" prop="updateDate">
            <el-date-picker
              v-model="form.updateDate"
              type="date"
              placeholder="选择更新日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>

          <el-form-item label="完成工作" prop="completedWork">
            <el-input
              v-model="form.completedWork"
              type="textarea"
              :rows="3"
              placeholder="请描述本次更新期间完成的主要工作"
            />
          </el-form-item>

          <el-form-item label="遇到问题" prop="encounteredProblems">
            <el-input
              v-model="form.encounteredProblems"
              type="textarea"
              :rows="2"
              placeholder="请描述实施过程中遇到的问题（可选）"
            />
          </el-form-item>

          <el-form-item label="解决方案" prop="solutions">
            <el-input
              v-model="form.solutions"
              type="textarea"
              :rows="2"
              placeholder="请描述问题的解决方案（可选）"
            />
          </el-form-item>

          <el-form-item label="下步计划" prop="nextSteps">
            <el-input
              v-model="form.nextSteps"
              type="textarea"
              :rows="2"
              placeholder="请描述下一步的工作计划"
            />
          </el-form-item>

          <el-form-item label="风险提示" prop="riskWarnings">
            <el-input
              v-model="form.riskWarnings"
              type="textarea"
              :rows="2"
              placeholder="请描述可能的风险和注意事项（可选）"
            />
          </el-form-item>

          <el-form-item label="资源需求" prop="resourceRequirements">
            <el-input
              v-model="form.resourceRequirements"
              placeholder="请描述额外的资源需求（可选）"
            />
          </el-form-item>

          <el-form-item label="预计完成时间" prop="estimatedCompletionDate">
            <el-date-picker
              v-model="form.estimatedCompletionDate"
              type="date"
              placeholder="选择预计完成时间"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>

          <el-form-item label="附件上传" prop="attachments">
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
                支持上传图片、文档等文件，单个文件不超过10MB，最多5个文件
              </div>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>

      <!-- 进度可视化 -->
      <div class="progress-visualization">
        <h4>进度可视化</h4>
        <div class="progress-chart">
          <div class="progress-bar-container">
            <div class="progress-bar">
              <div 
                class="progress-fill current" 
                :style="{ width: currentProgress + '%' }"
              >
                <span class="progress-label">当前 {{ currentProgress }}%</span>
              </div>
              <div 
                class="progress-fill new" 
                :style="{ 
                  left: currentProgress + '%',
                  width: (form.newProgress - currentProgress) + '%' 
                }"
              >
                <span v-if="form.newProgress > currentProgress" class="progress-label">
                  新增 {{ form.newProgress - currentProgress }}%
                </span>
              </div>
            </div>
            <div class="progress-markers">
              <div class="marker" style="left: 0%">0%</div>
              <div class="marker" style="left: 25%">25%</div>
              <div class="marker" style="left: 50%">50%</div>
              <div class="marker" style="left: 75%">75%</div>
              <div class="marker" style="left: 100%">100%</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 里程碑检查 -->
      <div v-if="milestones.length > 0" class="milestone-check">
        <h4>里程碑检查</h4>
        <div class="milestone-list">
          <div
            v-for="milestone in milestones"
            :key="milestone.id"
            class="milestone-item"
            :class="{ 'milestone-achieved': milestone.progress <= form.newProgress }"
          >
            <div class="milestone-progress">{{ milestone.progress }}%</div>
            <div class="milestone-content">
              <div class="milestone-name">{{ milestone.name }}</div>
              <div class="milestone-description">{{ milestone.description }}</div>
            </div>
            <div class="milestone-status">
              <el-tag 
                :type="milestone.progress <= form.newProgress ? 'success' : 'info'"
                size="small"
              >
                {{ milestone.progress <= form.newProgress ? '已达成' : '未达成' }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        更新进度
      </el-button>
      <el-button 
        v-if="form.newProgress === 100"
        type="success" 
        @click="handleCompleteImplementation" 
        :loading="loading"
      >
        标记为完成
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateImplementationProgress, completeImplementation } from '@/api/stateAssets/riskControlMeasure'

export default {
  name: 'ProgressUpdateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    measureData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      currentProgress: 0,
      form: {
        newProgress: 0,
        updateDate: '',
        completedWork: '',
        encounteredProblems: '',
        solutions: '',
        nextSteps: '',
        riskWarnings: '',
        resourceRequirements: '',
        estimatedCompletionDate: '',
        attachments: []
      },
      rules: {
        newProgress: [
          { required: true, message: '请设置新的进度', trigger: 'change' }
        ],
        updateDate: [
          { required: true, message: '请选择更新日期', trigger: 'change' }
        ],
        completedWork: [
          { required: true, message: '请描述完成的工作', trigger: 'blur' }
        ],
        nextSteps: [
          { required: true, message: '请描述下一步计划', trigger: 'blur' }
        ]
      },
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/common/upload',
      uploadHeaders: {
        'Authorization': 'Bearer ' + this.$store.getters.token
      },
      milestones: []
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
        this.loadMilestones()
      }
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      this.currentProgress = this.measureData.implementationProgress || 0
      this.form = {
        newProgress: this.currentProgress,
        updateDate: new Date().toISOString().split('T')[0],
        completedWork: '',
        encounteredProblems: '',
        solutions: '',
        nextSteps: '',
        riskWarnings: '',
        resourceRequirements: '',
        estimatedCompletionDate: this.measureData.plannedEndDate || '',
        attachments: []
      }
      this.fileList = []
    },

    // 加载里程碑
    loadMilestones() {
      // 模拟里程碑数据
      this.milestones = [
        {
          id: 1,
          name: '项目启动',
          description: '项目正式启动，团队组建完成',
          progress: 10
        },
        {
          id: 2,
          name: '需求分析',
          description: '完成详细需求分析',
          progress: 30
        },
        {
          id: 3,
          name: '方案设计',
          description: '完成实施方案设计',
          progress: 50
        },
        {
          id: 4,
          name: '实施部署',
          description: '完成系统部署和配置',
          progress: 80
        },
        {
          id: 5,
          name: '验收测试',
          description: '完成验收测试',
          progress: 100
        }
      ]
    },

    // 进度变化处理
    handleProgressChange(value) {
      // 确保新进度不小于当前进度
      if (value < this.currentProgress) {
        this.form.newProgress = this.currentProgress
        this.$message.warning('新进度不能小于当前进度')
      }
    },

    // 提交更新
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        
        if (this.form.newProgress === this.currentProgress) {
          this.$message.warning('进度没有变化，请调整进度值')
          return
        }

        this.loading = true

        const updateData = {
          riskControlMeasureId: this.measureData.riskControlMeasureId,
          previousProgress: this.currentProgress,
          newProgress: this.form.newProgress,
          updateDate: this.form.updateDate,
          completedWork: this.form.completedWork,
          encounteredProblems: this.form.encounteredProblems,
          solutions: this.form.solutions,
          nextSteps: this.form.nextSteps,
          riskWarnings: this.form.riskWarnings,
          resourceRequirements: this.form.resourceRequirements,
          estimatedCompletionDate: this.form.estimatedCompletionDate,
          attachments: this.form.attachments.join(','),
          updateBy: this.$store.getters.userInfo.userName
        }

        const response = await updateImplementationProgress(updateData)
        
        if (response.code === 200) {
          this.$message.success('进度更新成功')
          this.$emit('success')
          this.handleClose()
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('更新进度失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 标记为完成
    async handleCompleteImplementation() {
      try {
        await this.$refs.form.validate()
        
        await this.$confirm('确定要将此措施标记为完成吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        this.loading = true

        const completeData = {
          riskControlMeasureId: this.measureData.riskControlMeasureId,
          actualEndDate: this.form.updateDate,
          completionSummary: this.form.completedWork,
          finalProblems: this.form.encounteredProblems,
          finalSolutions: this.form.solutions,
          attachments: this.form.attachments.join(','),
          completeBy: this.$store.getters.userInfo.userName
        }

        const response = await completeImplementation(completeData)
        
        if (response.code === 200) {
          this.$message.success('措施已标记为完成')
          this.$emit('success')
          this.handleClose()
        }
      } catch (error) {
        if (error !== 'cancel' && error !== 'validation failed') {
          this.$message.error('标记完成失败：' + error.message)
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
      this.milestones = []
    },

    // 工具方法
    formatTooltip(value) {
      return `${value}%`
    },

    getProgressChangeClass() {
      const change = this.form.newProgress - this.currentProgress
      if (change > 0) return 'progress-increase'
      if (change < 0) return 'progress-decrease'
      return 'progress-stable'
    },

    getProgressChangeText() {
      const change = this.form.newProgress - this.currentProgress
      if (change > 0) return `+${change}%`
      if (change < 0) return `${change}%`
      return '无变化'
    },

    getImplementationStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'SUSPENDED': 'warning',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || ''
    },

    getImplementationStatusLabel(status) {
      const labelMap = {
        'PENDING': '待实施',
        'IN_PROGRESS': '实施中',
        'COMPLETED': '已完成',
        'SUSPENDED': '已暂停',
        'CANCELLED': '已取消'
      }
      return labelMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.progress-update {
  .measure-info,
  .progress-form,
  .progress-visualization,
  .milestone-check {
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

    .current-progress {
      font-weight: 600;
      color: #409eff;
      font-size: 16px;
    }
  }

  .progress-input {
    margin-bottom: 12px;
  }

  .progress-info {
    .progress-change {
      font-size: 14px;
      color: #606266;

      .progress-increase {
        color: #67c23a;
        font-weight: 600;
      }

      .progress-decrease {
        color: #f56c6c;
        font-weight: 600;
      }

      .progress-stable {
        color: #909399;
      }
    }
  }

  // 进度可视化样式
  .progress-chart {
    .progress-bar-container {
      position: relative;
      margin-bottom: 20px;

      .progress-bar {
        position: relative;
        height: 30px;
        background: #e4e7ed;
        border-radius: 15px;
        overflow: hidden;

        .progress-fill {
          position: absolute;
          top: 0;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          font-size: 12px;
          font-weight: 600;
          transition: all 0.3s ease;

          &.current {
            background: linear-gradient(90deg, #409eff, #66b1ff);
            z-index: 1;
          }

          &.new {
            background: linear-gradient(90deg, #67c23a, #95d475);
            z-index: 2;
          }

          .progress-label {
            white-space: nowrap;
          }
        }
      }

      .progress-markers {
        display: flex;
        justify-content: space-between;
        margin-top: 8px;
        position: relative;

        .marker {
          position: absolute;
          transform: translateX(-50%);
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  // 里程碑样式
  .milestone-list {
    .milestone-item {
      display: flex;
      align-items: center;
      padding: 12px;
      margin-bottom: 8px;
      background: white;
      border-radius: 6px;
      border: 1px solid #ebeef5;
      transition: all 0.3s ease;

      &.milestone-achieved {
        border-color: #67c23a;
        background: #f0f9ff;
      }

      .milestone-progress {
        width: 60px;
        text-align: center;
        font-weight: 600;
        color: #409eff;
        margin-right: 16px;
      }

      .milestone-content {
        flex: 1;

        .milestone-name {
          font-weight: 600;
          color: #303133;
          margin-bottom: 4px;
        }

        .milestone-description {
          font-size: 12px;
          color: #909399;
        }
      }

      .milestone-status {
        margin-left: 16px;
      }
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
