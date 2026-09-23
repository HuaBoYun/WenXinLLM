<template>
  <div class="performance-calibration-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-page-header @back="handleBack" :content="pageTitle" />
      <div class="header-actions">
        <el-button 
          v-if="calibrationData.calibrationStatus === 'PLANNED'" 
          type="primary" 
          @click="handleStart"
        >
          开始校准
        </el-button>
        <el-button 
          v-if="calibrationData.calibrationStatus === 'ONGOING'" 
          type="success" 
          @click="handleComplete"
        >
          完成校准
        </el-button>
        <el-button 
          v-if="calibrationData.calibrationStatus !== 'COMPLETED' && calibrationData.calibrationStatus !== 'CANCELLED'" 
          type="warning" 
          @click="handleCancel"
        >
          取消校准
        </el-button>
        <el-button type="info" @click="handleEdit">编辑</el-button>
        <el-dropdown @command="handleDropdownCommand">
          <el-button>
            更多操作<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="copy">复制校准</el-dropdown-item>
            <el-dropdown-item command="report">生成报告</el-dropdown-item>
            <el-dropdown-item command="notification">发送通知</el-dropdown-item>
            <el-dropdown-item command="analysis">智能分析</el-dropdown-item>
            <el-dropdown-item command="optimization">优化建议</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!-- 主要内容 -->
    <div class="detail-content" v-loading="loading">
      <el-tabs v-model="activeTab" type="card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-card shadow="never">
            <div class="info-grid">
              <div class="info-item">
                <label>校准编码：</label>
                <span>{{ calibrationData.calibrationCode }}</span>
              </div>
              <div class="info-item">
                <label>校准标题：</label>
                <span>{{ calibrationData.calibrationTitle }}</span>
              </div>
              <div class="info-item">
                <label>校准类型：</label>
                <el-tag :type="getTypeColor(calibrationData.calibrationType)">
                  {{ formatCalibrationType(calibrationData.calibrationType) }}
                </el-tag>
              </div>
              <div class="info-item">
                <label>校准状态：</label>
                <el-tag :type="getStatusColor(calibrationData.calibrationStatus)">
                  {{ formatCalibrationStatus(calibrationData.calibrationStatus) }}
                </el-tag>
              </div>
              <div class="info-item">
                <label>校准年度：</label>
                <span>{{ calibrationData.calibrationYear }}</span>
              </div>
              <div class="info-item">
                <label>校准季度：</label>
                <span>{{ calibrationData.calibrationQuarter || '-' }}</span>
              </div>
              <div class="info-item">
                <label>校准月份：</label>
                <span>{{ calibrationData.calibrationMonth || '-' }}</span>
              </div>
              <div class="info-item">
                <label>校准范围：</label>
                <span>{{ calibrationData.calibrationScope }}</span>
              </div>
              <div class="info-item">
                <label>目标部门：</label>
                <span>{{ calibrationData.targetDeptName }}</span>
              </div>
              <div class="info-item">
                <label>校准负责人：</label>
                <span>{{ calibrationData.calibrationOwnerName }}</span>
              </div>
              <div class="info-item">
                <label>优先级：</label>
                <el-tag :type="getPriorityColor(calibrationData.priorityLevel)" size="mini">
                  {{ formatPriorityLevel(calibrationData.priorityLevel) }}
                </el-tag>
              </div>
              <div class="info-item">
                <label>完成率：</label>
                <el-progress 
                  :percentage="calibrationData.completionRate || 0" 
                  :stroke-width="6"
                />
              </div>
            </div>
          </el-card>

          <!-- 时间信息 -->
          <el-card shadow="never" style="margin-top: 20px;">
            <div slot="header">
              <span>时间信息</span>
            </div>
            <div class="info-grid">
              <div class="info-item">
                <label>计划开始时间：</label>
                <span>{{ formatDateTime(calibrationData.plannedStartTime) }}</span>
              </div>
              <div class="info-item">
                <label>计划结束时间：</label>
                <span :class="{ 'overdue-text': isOverdue(calibrationData) }">
                  {{ formatDateTime(calibrationData.plannedEndTime) }}
                </span>
              </div>
              <div class="info-item">
                <label>实际开始时间：</label>
                <span>{{ formatDateTime(calibrationData.actualStartTime) }}</span>
              </div>
              <div class="info-item">
                <label>实际结束时间：</label>
                <span>{{ formatDateTime(calibrationData.actualEndTime) }}</span>
              </div>
              <div class="info-item">
                <label>校准时长：</label>
                <span>{{ getCalibrationDuration(calibrationData) }}</span>
              </div>
            </div>
          </el-card>

          <!-- 校准目标和原则 -->
          <el-card shadow="never" style="margin-top: 20px;">
            <div slot="header">
              <span>校准目标和原则</span>
            </div>
            <div class="content-section">
              <div class="content-item">
                <h4>校准目标</h4>
                <p>{{ calibrationData.calibrationObjective || '暂无' }}</p>
              </div>
              <div class="content-item">
                <h4>校准原则</h4>
                <p>{{ calibrationData.calibrationPrinciples || '暂无' }}</p>
              </div>
              <div class="content-item">
                <h4>校准标准</h4>
                <p>{{ calibrationData.calibrationStandards || '暂无' }}</p>
              </div>
              <div class="content-item">
                <h4>校准规则</h4>
                <p>{{ calibrationData.calibrationRules || '暂无' }}</p>
              </div>
            </div>
          </el-card>
        </el-tab-pane>

        <!-- 校准过程 -->
        <el-tab-pane label="校准过程" name="process">
          <el-card shadow="never">
            <div class="process-info">
              <div class="info-grid">
                <div class="info-item">
                  <label>校准方法：</label>
                  <span>{{ calibrationData.calibrationMethod || '-' }}</span>
                </div>
                <div class="info-item">
                  <label>校准地点：</label>
                  <span>{{ calibrationData.calibrationLocation || '-' }}</span>
                </div>
                <div class="info-item">
                  <label>参与人数：</label>
                  <span>{{ calibrationData.participantCount || '-' }}</span>
                </div>
                <div class="info-item">
                  <label>校准对象数量：</label>
                  <span>{{ calibrationData.calibrationObjectCount || '-' }}</span>
                </div>
              </div>
            </div>
          </el-card>

          <!-- 校准记录 -->
          <el-card shadow="never" style="margin-top: 20px;">
            <div slot="header">
              <span>校准记录</span>
              <el-button 
                style="float: right; padding: 3px 0" 
                type="text" 
                @click="handleEditRecord"
              >
                编辑记录
              </el-button>
            </div>
            <div class="content-section">
              <div class="content-item">
                <h4>校准笔记</h4>
                <p>{{ calibrationData.calibrationNotes || '暂无记录' }}</p>
              </div>
              <div class="content-item">
                <h4>校准总结</h4>
                <p>{{ calibrationData.calibrationSummary || '暂无总结' }}</p>
              </div>
              <div class="content-item">
                <h4>主要调整</h4>
                <p>{{ calibrationData.majorAdjustments || '暂无调整' }}</p>
              </div>
              <div class="content-item">
                <h4>争议解决</h4>
                <p>{{ calibrationData.disputeResolution || '无争议' }}</p>
              </div>
            </div>
          </el-card>
        </el-tab-pane>

        <!-- 校准结果 -->
        <el-tab-pane label="校准结果" name="result">
          <el-card shadow="never">
            <div class="result-metrics">
              <div class="metric-card">
                <div class="metric-value">{{ calibrationData.preCalibrationAvgScore || '-' }}</div>
                <div class="metric-label">校准前平均分</div>
              </div>
              <div class="metric-card">
                <div class="metric-value">{{ calibrationData.postCalibrationAvgScore || '-' }}</div>
                <div class="metric-label">校准后平均分</div>
              </div>
              <div class="metric-card">
                <div class="metric-value">{{ calibrationData.consistencyIndex || '-' }}</div>
                <div class="metric-label">一致性指数</div>
              </div>
              <div class="metric-card">
                <div class="metric-value">
                  <el-rate 
                    v-model="calibrationData.effectivenessRating" 
                    disabled 
                    show-score 
                    text-color="#ff9900"
                  />
                </div>
                <div class="metric-label">效果评级</div>
              </div>
            </div>
          </el-card>

          <!-- 改进建议 -->
          <el-card shadow="never" style="margin-top: 20px;">
            <div slot="header">
              <span>改进建议</span>
            </div>
            <div class="content-section">
              <p>{{ calibrationData.improvementSuggestions || '暂无改进建议' }}</p>
            </div>
          </el-card>

          <!-- 培训需求 -->
          <el-card shadow="never" style="margin-top: 20px;">
            <div slot="header">
              <span>培训需求</span>
            </div>
            <div class="content-section">
              <p>{{ calibrationData.trainingNeeds || '暂无培训需求' }}</p>
            </div>
          </el-card>
        </el-tab-pane>

        <!-- 跟进管理 -->
        <el-tab-pane label="跟进管理" name="followup">
          <el-card shadow="never">
            <div class="followup-info">
              <div class="info-grid">
                <div class="info-item">
                  <label>是否需要跟进：</label>
                  <el-tag :type="calibrationData.needFollowUp ? 'warning' : 'info'">
                    {{ calibrationData.needFollowUp ? '是' : '否' }}
                  </el-tag>
                </div>
                <div class="info-item" v-if="calibrationData.needFollowUp">
                  <label>跟进状态：</label>
                  <el-tag :type="getFollowUpStatusColor(calibrationData.followUpStatus)">
                    {{ formatFollowUpStatus(calibrationData.followUpStatus) }}
                  </el-tag>
                </div>
                <div class="info-item" v-if="calibrationData.needFollowUp">
                  <label>跟进截止时间：</label>
                  <span>{{ formatDateTime(calibrationData.followUpDeadline) }}</span>
                </div>
              </div>
            </div>
          </el-card>

          <!-- 跟进计划 -->
          <el-card shadow="never" style="margin-top: 20px;" v-if="calibrationData.needFollowUp">
            <div slot="header">
              <span>跟进计划</span>
              <el-button 
                style="float: right; padding: 3px 0" 
                type="text" 
                @click="handleEditFollowUp"
              >
                编辑跟进
              </el-button>
            </div>
            <div class="content-section">
              <p>{{ calibrationData.followUpPlan || '暂无跟进计划' }}</p>
            </div>
          </el-card>

          <!-- 跟进记录 -->
          <el-card shadow="never" style="margin-top: 20px;" v-if="calibrationData.needFollowUp">
            <div slot="header">
              <span>跟进记录</span>
            </div>
            <div class="content-section">
              <p>{{ calibrationData.followUpRecord || '暂无跟进记录' }}</p>
            </div>
          </el-card>
        </el-tab-pane>

        <!-- 统计分析 -->
        <el-tab-pane label="统计分析" name="analysis">
          <el-card shadow="never">
            <div slot="header">
              <span>校准统计</span>
              <el-button 
                style="float: right; padding: 3px 0" 
                type="text" 
                @click="handleRefreshAnalysis"
              >
                刷新分析
              </el-button>
            </div>
            <div class="analysis-content" v-loading="analysisLoading">
              <!-- 这里可以添加图表组件 -->
              <div class="analysis-placeholder">
                <p>校准分析数据将在这里显示</p>
                <p>包括参与者统计、校准对象统计、质量指标等</p>
              </div>
            </div>
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 编辑记录对话框 -->
    <el-dialog
      title="编辑校准记录"
      :visible.sync="recordDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="recordForm" label-width="120px">
        <el-form-item label="校准笔记">
          <el-input 
            v-model="recordForm.calibrationNotes" 
            type="textarea" 
            :rows="4"
            placeholder="请输入校准笔记"
          />
        </el-form-item>
        <el-form-item label="校准总结">
          <el-input 
            v-model="recordForm.calibrationSummary" 
            type="textarea" 
            :rows="4"
            placeholder="请输入校准总结"
          />
        </el-form-item>
        <el-form-item label="主要调整">
          <el-input 
            v-model="recordForm.majorAdjustments" 
            type="textarea" 
            :rows="3"
            placeholder="请输入主要调整"
          />
        </el-form-item>
        <el-form-item label="争议解决">
          <el-input 
            v-model="recordForm.disputeResolution" 
            type="textarea" 
            :rows="3"
            placeholder="请输入争议解决方案"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="recordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveRecord">保存</el-button>
      </div>
    </el-dialog>

    <!-- 编辑跟进对话框 -->
    <el-dialog
      title="编辑跟进计划"
      :visible.sync="followUpDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="followUpForm" label-width="120px">
        <el-form-item label="跟进状态">
          <el-select v-model="followUpForm.followUpStatus" placeholder="请选择跟进状态">
            <el-option label="待跟进" value="PENDING" />
            <el-option label="跟进中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="跟进截止时间">
          <el-date-picker
            v-model="followUpForm.followUpDeadline"
            type="datetime"
            placeholder="请选择跟进截止时间"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="跟进计划">
          <el-input 
            v-model="followUpForm.followUpPlan" 
            type="textarea" 
            :rows="4"
            placeholder="请输入跟进计划"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="followUpDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveFollowUp">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCalibrationDetail,
  startCalibration,
  completeCalibration,
  cancelCalibration,
  copyCalibration,
  generateCalibrationReport,
  sendCalibrationNotification,
  intelligentCalibrationAnalysis,
  getCalibrationOptimizationSuggestions,
  saveCalibrationRecord,
  updateFollowUpStatus,
  formatCalibrationStatus,
  formatCalibrationType,
  formatPriorityLevel,
  formatFollowUpStatus,
  getStatusColor,
  getPriorityColor,
  isOverdue,
  getCalibrationDuration
} from '@/api/managementAccountant/pm/performanceCalibration'

export default {
  name: 'PerformanceCalibrationDetail',
  data() {
    return {
      loading: false,
      analysisLoading: false,
      calibrationData: {},
      activeTab: 'basic',
      recordDialogVisible: false,
      followUpDialogVisible: false,
      recordForm: {
        calibrationNotes: '',
        calibrationSummary: '',
        majorAdjustments: '',
        disputeResolution: ''
      },
      followUpForm: {
        followUpStatus: '',
        followUpDeadline: '',
        followUpPlan: ''
      }
    }
  },
  computed: {
    calibrationId() {
      return this.$route.params.id
    },
    pageTitle() {
      return this.calibrationData.calibrationTitle || '校准详情'
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const response = await getCalibrationDetail(this.calibrationId)
        if (response.success) {
          this.calibrationData = response.data
        } else {
          this.$message.error(response.message || '加载失败')
        }
      } catch (error) {
        console.error('加载校准详情失败:', error)
        this.$message.error('加载失败')
      } finally {
        this.loading = false
      }
    },

    // 返回
    handleBack() {
      this.$router.go(-1)
    },

    // 编辑
    handleEdit() {
      this.$router.push({
        name: 'PerformanceCalibrationEdit',
        params: { id: this.calibrationId }
      })
    },

    // 开始校准
    async handleStart() {
      try {
        const response = await startCalibration(this.calibrationId, {
          calibrationMethod: 'MEETING',
          calibrationLocation: '会议室'
        })
        if (response.success) {
          this.$message.success('校准开始成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '开始失败')
        }
      } catch (error) {
        console.error('开始校准失败:', error)
        this.$message.error('开始失败')
      }
    },

    // 完成校准
    async handleComplete() {
      this.$prompt('请输入校准总结', '完成校准', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputPlaceholder: '请输入校准总结'
      }).then(async ({ value }) => {
        try {
          const response = await completeCalibration(this.calibrationId, {
            calibrationSummary: value,
            effectivenessRating: 4,
            needFollowUp: false
          })
          if (response.success) {
            this.$message.success('校准完成成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '完成失败')
          }
        } catch (error) {
          console.error('完成校准失败:', error)
          this.$message.error('完成失败')
        }
      })
    },

    // 取消校准
    async handleCancel() {
      this.$prompt('请输入取消原因', '取消校准', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '请输入取消原因'
      }).then(async ({ value }) => {
        try {
          const response = await cancelCalibration(this.calibrationId, {
            cancelReason: value
          })
          if (response.success) {
            this.$message.success('校准取消成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '取消失败')
          }
        } catch (error) {
          console.error('取消校准失败:', error)
          this.$message.error('取消失败')
        }
      })
    },

    // 下拉菜单命令
    async handleDropdownCommand(command) {
      switch (command) {
        case 'copy':
          await this.handleCopy()
          break
        case 'report':
          await this.handleReport()
          break
        case 'notification':
          await this.handleNotification()
          break
        case 'analysis':
          await this.handleAnalysis()
          break
        case 'optimization':
          await this.handleOptimization()
          break
      }
    },

    // 复制校准
    async handleCopy() {
      try {
        const response = await copyCalibration(this.calibrationId, {
          calibrationTitle: this.calibrationData.calibrationTitle + ' - 副本'
        })
        if (response.success) {
          this.$message.success('复制成功')
        } else {
          this.$message.error(response.message || '复制失败')
        }
      } catch (error) {
        console.error('复制校准失败:', error)
        this.$message.error('复制失败')
      }
    },

    // 生成报告
    async handleReport() {
      try {
        const response = await generateCalibrationReport(this.calibrationId, {})
        if (response.success) {
          this.$message.success('报告生成成功')
        } else {
          this.$message.error(response.message || '生成报告失败')
        }
      } catch (error) {
        console.error('生成报告失败:', error)
        this.$message.error('生成报告失败')
      }
    },

    // 发送通知
    async handleNotification() {
      try {
        const response = await sendCalibrationNotification(this.calibrationId, {
          notificationType: 'EMAIL',
          notificationContent: '校准通知'
        })
        if (response.success) {
          this.$message.success('通知发送成功')
        } else {
          this.$message.error(response.message || '发送通知失败')
        }
      } catch (error) {
        console.error('发送通知失败:', error)
        this.$message.error('发送通知失败')
      }
    },

    // 智能分析
    async handleAnalysis() {
      try {
        const response = await intelligentCalibrationAnalysis(this.calibrationId, {})
        if (response.success) {
          this.$message.success('智能分析完成')
          // 可以在这里显示分析结果
        } else {
          this.$message.error(response.message || '智能分析失败')
        }
      } catch (error) {
        console.error('智能分析失败:', error)
        this.$message.error('智能分析失败')
      }
    },

    // 优化建议
    async handleOptimization() {
      try {
        const response = await getCalibrationOptimizationSuggestions(this.calibrationId)
        if (response.success) {
          const suggestions = response.data.suggestions
          if (suggestions && suggestions.length > 0) {
            this.$alert(suggestions.join('\n'), '优化建议', {
              confirmButtonText: '确定',
              type: 'info'
            })
          } else {
            this.$message.info('暂无优化建议')
          }
        } else {
          this.$message.error(response.message || '获取优化建议失败')
        }
      } catch (error) {
        console.error('获取优化建议失败:', error)
        this.$message.error('获取优化建议失败')
      }
    },

    // 编辑记录
    handleEditRecord() {
      this.recordForm = {
        calibrationNotes: this.calibrationData.calibrationNotes || '',
        calibrationSummary: this.calibrationData.calibrationSummary || '',
        majorAdjustments: this.calibrationData.majorAdjustments || '',
        disputeResolution: this.calibrationData.disputeResolution || ''
      }
      this.recordDialogVisible = true
    },

    // 保存记录
    async handleSaveRecord() {
      try {
        const response = await saveCalibrationRecord(this.calibrationId, this.recordForm)
        if (response.success) {
          this.$message.success('保存成功')
          this.recordDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '保存失败')
        }
      } catch (error) {
        console.error('保存记录失败:', error)
        this.$message.error('保存失败')
      }
    },

    // 编辑跟进
    handleEditFollowUp() {
      this.followUpForm = {
        followUpStatus: this.calibrationData.followUpStatus || 'PENDING',
        followUpDeadline: this.calibrationData.followUpDeadline || '',
        followUpPlan: this.calibrationData.followUpPlan || ''
      }
      this.followUpDialogVisible = true
    },

    // 保存跟进
    async handleSaveFollowUp() {
      try {
        const response = await updateFollowUpStatus(
          this.calibrationId, 
          this.followUpForm.followUpStatus, 
          this.followUpForm
        )
        if (response.success) {
          this.$message.success('保存成功')
          this.followUpDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '保存失败')
        }
      } catch (error) {
        console.error('保存跟进失败:', error)
        this.$message.error('保存失败')
      }
    },

    // 刷新分析
    handleRefreshAnalysis() {
      this.analysisLoading = true
      // 模拟加载分析数据
      setTimeout(() => {
        this.analysisLoading = false
        this.$message.success('分析数据已刷新')
      }, 2000)
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return this.$moment(dateTime).format('YYYY-MM-DD HH:mm:ss')
    },

    // 获取跟进状态颜色
    getFollowUpStatusColor(status) {
      const colorMap = {
        'PENDING': 'warning',
        'IN_PROGRESS': 'primary',
        'COMPLETED': 'success',
        'CANCELLED': 'info'
      }
      return colorMap[status] || 'info'
    },

    // 获取类型颜色
    getTypeColor(type) {
      const colorMap = {
        'ANNUAL': 'primary',
        'QUARTERLY': 'success',
        'MONTHLY': 'info',
        'PROJECT': 'warning',
        'SPECIAL': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 格式化方法
    formatCalibrationStatus,
    formatCalibrationType,
    formatPriorityLevel,
    formatFollowUpStatus,
    getStatusColor,
    getPriorityColor,
    isOverdue,
    getCalibrationDuration
  }
}
</script>

<style lang="scss" scoped>
.performance-calibration-detail {
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 20px;
    background: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0,0,0,.12), 0 0 6px rgba(0,0,0,.04);

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .detail-content {
    .info-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 20px;
      margin-bottom: 20px;

      .info-item {
        display: flex;
        align-items: center;

        label {
          font-weight: bold;
          margin-right: 10px;
          min-width: 120px;
          color: #606266;
        }
      }
    }

    .content-section {
      .content-item {
        margin-bottom: 20px;

        h4 {
          color: #303133;
          margin-bottom: 10px;
        }

        p {
          color: #606266;
          line-height: 1.6;
          margin: 0;
        }
      }
    }

    .result-metrics {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 20px;

      .metric-card {
        text-align: center;
        padding: 20px;
        background: #f8f9fa;
        border-radius: 8px;

        .metric-value {
          font-size: 24px;
          font-weight: bold;
          color: #409eff;
          margin-bottom: 8px;
        }

        .metric-label {
          color: #606266;
          font-size: 14px;
        }
      }
    }

    .process-info, .followup-info {
      margin-bottom: 20px;
    }

    .analysis-content {
      min-height: 200px;

      .analysis-placeholder {
        text-align: center;
        color: #909399;
        padding: 60px 0;

        p {
          margin: 10px 0;
        }
      }
    }
  }

  .overdue-text {
    color: #f56c6c;
    font-weight: bold;
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>
