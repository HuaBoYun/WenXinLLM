<template>
  <div class="tax-planning-detail" v-loading="loading">
    <div v-if="planningData">
      <!-- 头部信息 -->
      <div class="detail-header">
        <div class="header-left">
          <h3 class="planning-title">
            {{ planningData.planningName }}
            <el-tag :type="getPlanningStatusTagType(planningData.planningStatus)" size="small">
              {{ getPlanningStatusLabel(planningData.planningStatus) }}
            </el-tag>
          </h3>
          <div class="planning-meta">
            <span class="meta-item">
              <i class="el-icon-document"></i>
              {{ planningData.planningCode }}
            </span>
            <span class="meta-item">
              <i class="el-icon-user"></i>
              {{ planningData.responsiblePerson }}
            </span>
            <span class="meta-item">
              <i class="el-icon-time"></i>
              {{ formatDate(planningData.createdTime) }}
            </span>
          </div>
        </div>
        <div class="header-right">
          <el-button
            v-if="canEdit"
            type="primary"
            icon="el-icon-edit"
            @click="handleEdit"
          >
            编辑
          </el-button>
          <el-button
            v-if="canExecute"
            type="success"
            icon="el-icon-video-play"
            @click="handleStartExecution"
          >
            开始执行
          </el-button>
          <el-dropdown @command="handleCommand">
            <el-button>
              更多操作<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="calculate">计算效益</el-dropdown-item>
              <el-dropdown-item command="assess">风险评估</el-dropdown-item>
              <el-dropdown-item command="optimize">优化方案</el-dropdown-item>
              <el-dropdown-item command="report">生成报告</el-dropdown-item>
              <el-dropdown-item command="copy">复制筹划</el-dropdown-item>
              <el-dropdown-item command="archive" divided>归档</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>

      <!-- 关键指标卡片 -->
      <div class="metrics-cards">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="metric-card">
              <div class="metric-icon tax-saving">
                <i class="el-icon-coin"></i>
              </div>
              <div class="metric-content">
                <div class="metric-value">{{ formatAmount(planningData.taxSavingAmount) }}</div>
                <div class="metric-label">节税金额(万元)</div>
              </div>
            </div>
          </el-col>
          
          <el-col :span="6">
            <div class="metric-card">
              <div class="metric-icon net-benefit">
                <i class="el-icon-trophy"></i>
              </div>
              <div class="metric-content">
                <div class="metric-value" :class="getBenefitClass(planningData.netBenefit)">
                  {{ formatAmount(planningData.netBenefit) }}
                </div>
                <div class="metric-label">净收益(万元)</div>
              </div>
            </div>
          </el-col>
          
          <el-col :span="6">
            <div class="metric-card">
              <div class="metric-icon roi">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="metric-content">
                <div class="metric-value" :class="getRoiClass(planningData.roi)">
                  {{ formatPercentage(planningData.roi) }}
                </div>
                <div class="metric-label">投资回报率</div>
              </div>
            </div>
          </el-col>
          
          <el-col :span="6">
            <div class="metric-card">
              <div class="metric-icon progress">
                <i class="el-icon-loading"></i>
              </div>
              <div class="metric-content">
                <div class="metric-value">{{ planningData.executionProgress || 0 }}%</div>
                <div class="metric-label">执行进度</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 详细信息标签页 -->
      <div class="detail-tabs">
        <el-tabs v-model="activeTab">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
            <div class="info-section">
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-group">
                    <h4>筹划信息</h4>
                    <div class="info-item">
                      <label>筹划编号：</label>
                      <span>{{ planningData.planningCode }}</span>
                    </div>
                    <div class="info-item">
                      <label>筹划名称：</label>
                      <span>{{ planningData.planningName }}</span>
                    </div>
                    <div class="info-item">
                      <label>筹划类型：</label>
                      <span>{{ getPlanningTypeLabel(planningData.planningType) }}</span>
                    </div>
                    <div class="info-item">
                      <label>税种：</label>
                      <span>{{ getTaxTypeLabel(planningData.taxType) }}</span>
                    </div>
                    <div class="info-item">
                      <label>优先级：</label>
                      <el-tag :type="getPriorityTagType(planningData.priority)" size="small">
                        {{ getPriorityLabel(planningData.priority) }}
                      </el-tag>
                    </div>
                    <div class="info-item">
                      <label>风险等级：</label>
                      <el-tag :type="getRiskLevelTagType(planningData.riskLevel)" size="small">
                        {{ getRiskLevelLabel(planningData.riskLevel) }}
                      </el-tag>
                    </div>
                  </div>
                </el-col>
                
                <el-col :span="12">
                  <div class="info-group">
                    <h4>执行信息</h4>
                    <div class="info-item">
                      <label>执行状态：</label>
                      <el-tag :type="getExecutionStatusTagType(planningData.executionStatus)" size="small">
                        {{ getExecutionStatusLabel(planningData.executionStatus) }}
                      </el-tag>
                    </div>
                    <div class="info-item">
                      <label>责任人：</label>
                      <span>{{ planningData.responsiblePerson }}</span>
                    </div>
                    <div class="info-item">
                      <label>责任部门：</label>
                      <span>{{ planningData.responsibleDepartment }}</span>
                    </div>
                    <div class="info-item">
                      <label>开始时间：</label>
                      <span>{{ formatDate(planningData.startTime) }}</span>
                    </div>
                    <div class="info-item">
                      <label>结束时间：</label>
                      <span>{{ formatDate(planningData.endTime) }}</span>
                    </div>
                    <div class="info-item">
                      <label>执行进度：</label>
                      <el-progress
                        :percentage="Number(planningData.executionProgress || 0)"
                        :stroke-width="8"
                        style="width: 200px"
                      />
                    </div>
                  </div>
                </el-col>
              </el-row>
              
              <div class="info-group full-width">
                <h4>筹划描述</h4>
                <div class="description-content">
                  {{ planningData.planningDescription || '暂无描述' }}
                </div>
              </div>
              
              <div class="info-group full-width">
                <h4>筹划目标</h4>
                <div class="description-content">
                  {{ planningData.planningObjective || '暂无目标' }}
                </div>
              </div>
            </div>
          </el-tab-pane>

          <!-- 财务分析 -->
          <el-tab-pane label="财务分析" name="financial">
            <div class="financial-section">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-card header="成本分析" class="analysis-card">
                    <div class="analysis-item">
                      <label>实施成本：</label>
                      <span class="amount-text">{{ formatAmount(planningData.implementationCost) }}万元</span>
                    </div>
                    <div class="analysis-item">
                      <label>维护成本：</label>
                      <span class="amount-text">{{ formatAmount(planningData.maintenanceCost) }}万元</span>
                    </div>
                    <div class="analysis-item">
                      <label>合规成本：</label>
                      <span class="amount-text">{{ formatAmount(planningData.complianceCost) }}万元</span>
                    </div>
                    <div class="analysis-item">
                      <label>总成本：</label>
                      <span class="amount-text total">{{ formatAmount(planningData.totalCost) }}万元</span>
                    </div>
                  </el-card>
                </el-col>
                
                <el-col :span="12">
                  <el-card header="收益分析" class="analysis-card">
                    <div class="analysis-item">
                      <label>节税金额：</label>
                      <span class="amount-text success">{{ formatAmount(planningData.taxSavingAmount) }}万元</span>
                    </div>
                    <div class="analysis-item">
                      <label>其他收益：</label>
                      <span class="amount-text">{{ formatAmount(planningData.otherBenefits) }}万元</span>
                    </div>
                    <div class="analysis-item">
                      <label>总收益：</label>
                      <span class="amount-text">{{ formatAmount(planningData.totalBenefits) }}万元</span>
                    </div>
                    <div class="analysis-item">
                      <label>净收益：</label>
                      <span class="amount-text" :class="getBenefitClass(planningData.netBenefit)">
                        {{ formatAmount(planningData.netBenefit) }}万元
                      </span>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
              
              <el-row :gutter="20" style="margin-top: 20px">
                <el-col :span="8">
                  <el-card header="投资回报" class="analysis-card">
                    <div class="analysis-item">
                      <label>投资回报率：</label>
                      <span class="percentage-text" :class="getRoiClass(planningData.roi)">
                        {{ formatPercentage(planningData.roi) }}
                      </span>
                    </div>
                    <div class="analysis-item">
                      <label>回收期：</label>
                      <span>{{ planningData.paybackPeriod || '-' }}个月</span>
                    </div>
                  </el-card>
                </el-col>
                
                <el-col :span="8">
                  <el-card header="风险评估" class="analysis-card">
                    <div class="analysis-item">
                      <label>风险等级：</label>
                      <el-tag :type="getRiskLevelTagType(planningData.riskLevel)" size="small">
                        {{ getRiskLevelLabel(planningData.riskLevel) }}
                      </el-tag>
                    </div>
                    <div class="analysis-item">
                      <label>风险评分：</label>
                      <span>{{ planningData.riskScore || '-' }}分</span>
                    </div>
                  </el-card>
                </el-col>
                
                <el-col :span="8">
                  <el-card header="可行性分析" class="analysis-card">
                    <div class="analysis-item">
                      <label>可行性等级：</label>
                      <el-tag :type="getFeasibilityTagType(planningData.feasibilityLevel)" size="small">
                        {{ getFeasibilityLabel(planningData.feasibilityLevel) }}
                      </el-tag>
                    </div>
                    <div class="analysis-item">
                      <label>可行性评分：</label>
                      <span>{{ planningData.feasibilityScore || '-' }}分</span>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>

          <!-- 执行跟踪 -->
          <el-tab-pane label="执行跟踪" name="execution">
            <div class="execution-section">
              <div class="execution-timeline">
                <el-timeline>
                  <el-timeline-item
                    v-for="(item, index) in executionHistory"
                    :key="index"
                    :timestamp="formatDateTime(item.timestamp)"
                    :type="getTimelineType(item.type)"
                  >
                    <h4>{{ item.title }}</h4>
                    <p>{{ item.description }}</p>
                    <div v-if="item.operator" class="operator-info">
                      操作人：{{ item.operator }}
                    </div>
                  </el-timeline-item>
                </el-timeline>
              </div>
            </div>
          </el-tab-pane>

          <!-- 相关文档 -->
          <el-tab-pane label="相关文档" name="documents">
            <div class="documents-section">
              <div class="upload-area">
                <el-upload
                  class="upload-demo"
                  drag
                  action="/api/upload"
                  multiple
                  :on-success="handleUploadSuccess"
                  :on-error="handleUploadError"
                >
                  <i class="el-icon-upload"></i>
                  <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                  <div class="el-upload__tip" slot="tip">只能上传jpg/png/pdf文件，且不超过10MB</div>
                </el-upload>
              </div>
              
              <div class="documents-list">
                <el-table :data="documents" stripe>
                  <el-table-column prop="fileName" label="文件名" />
                  <el-table-column prop="fileSize" label="文件大小" :formatter="formatFileSize" />
                  <el-table-column prop="uploadTime" label="上传时间" :formatter="formatDate" />
                  <el-table-column prop="uploader" label="上传人" />
                  <el-table-column label="操作" width="150">
                    <template slot-scope="scope">
                      <el-button type="text" size="small" @click="handleDownload(scope.row)">
                        下载
                      </el-button>
                      <el-button type="text" size="small" @click="handlePreview(scope.row)">
                        预览
                      </el-button>
                      <el-button type="text" size="small" @click="handleDeleteDoc(scope.row)">
                        删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getPlanningDetail,
  calculatePlanningBenefit,
  assessPlanningRisk,
  optimizePlanningScheme,
  generatePlanningReport,
  copyPlanning,
  batchArchivePlannings,
  startPlanningExecution,
  getPlanningTypeLabel,
  getPlanningStatusLabel,
  getExecutionStatusLabel,
  getRiskLevelLabel,
  getPriorityLabel,
  getTaxTypeLabel,
  formatAmount,
  formatPercentage
} from '@/api/managementAccountant/ts/taxPlanning'

export default {
  name: 'TaxPlanningDetail',
  props: {
    planningId: {
      type: [String, Number],
      required: true
    },
    tenantId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      loading: false,
      activeTab: 'basic',
      planningData: null,
      executionHistory: [],
      documents: []
    }
  },
  computed: {
    canEdit() {
      return this.planningData && ['DRAFT', 'UNDER_REVIEW'].includes(this.planningData.planningStatus)
    },
    
    canExecute() {
      return this.planningData && 
             this.planningData.planningStatus === 'APPROVED' && 
             this.planningData.executionStatus === 'NOT_STARTED'
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
        const response = await getPlanningDetail(this.tenantId, this.planningId)
        if (response.success) {
          this.planningData = response.data
          this.loadExecutionHistory()
          this.loadDocuments()
        } else {
          this.$message.error(response.message || '加载数据失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 加载执行历史
    loadExecutionHistory() {
      // 模拟执行历史数据
      this.executionHistory = [
        {
          title: '筹划创建',
          description: '创建了新的税务筹划方案',
          timestamp: this.planningData.createdTime,
          operator: this.planningData.createdBy,
          type: 'primary'
        },
        {
          title: '风险评估',
          description: '完成了风险评估，风险等级为' + this.getRiskLevelLabel(this.planningData.riskLevel),
          timestamp: this.planningData.riskAssessmentTime,
          operator: this.planningData.riskAssessor,
          type: 'warning'
        }
      ]
      
      if (this.planningData.executionStatus !== 'NOT_STARTED') {
        this.executionHistory.push({
          title: '开始执行',
          description: '筹划方案开始执行',
          timestamp: this.planningData.executionStartTime,
          operator: this.planningData.executor,
          type: 'success'
        })
      }
    },
    
    // 加载文档
    loadDocuments() {
      // 模拟文档数据
      this.documents = [
        {
          fileName: '筹划方案书.pdf',
          fileSize: 2048000,
          uploadTime: new Date(),
          uploader: this.planningData.responsiblePerson
        }
      ]
    },
    
    // 编辑
    handleEdit() {
      this.$message.info('编辑功能开发中')
    },
    
    // 开始执行
    async handleStartExecution() {
      try {
        await this.$confirm('确认开始执行该筹划方案吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await startPlanningExecution(this.tenantId, this.planningId)
        if (response.success) {
          this.$message.success('开始执行成功')
          this.loadData()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '开始执行失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('开始执行失败:', error)
          this.$message.error('开始执行失败')
        }
      }
    },
    
    // 更多操作
    async handleCommand(command) {
      switch (command) {
        case 'calculate':
          await this.handleCalculateBenefit()
          break
        case 'assess':
          await this.handleAssessRisk()
          break
        case 'optimize':
          await this.handleOptimize()
          break
        case 'report':
          await this.handleGenerateReport()
          break
        case 'copy':
          await this.handleCopy()
          break
        case 'archive':
          await this.handleArchive()
          break
      }
    },
    
    // 计算效益
    async handleCalculateBenefit() {
      try {
        const response = await calculatePlanningBenefit(this.tenantId, this.planningId)
        if (response.success) {
          const data = response.data
          this.$alert(
            `节税金额: ${formatAmount(data.taxSaving)}万元\n净收益: ${formatAmount(data.netBenefit)}万元\n投资回报率: ${formatPercentage(data.roi)}`,
            '效益计算结果',
            { type: 'success' }
          )
          this.loadData() // 重新加载数据
        } else {
          this.$message.error(response.message || '计算失败')
        }
      } catch (error) {
        console.error('计算效益失败:', error)
        this.$message.error('计算失败')
      }
    },
    
    // 风险评估
    async handleAssessRisk() {
      try {
        const response = await assessPlanningRisk(this.tenantId, this.planningId)
        if (response.success) {
          const data = response.data
          this.$alert(
            `风险等级: ${this.getRiskLevelLabel(data.riskLevel)}\n风险评分: ${data.riskScore}分\n风险因素: ${data.riskDescription}`,
            '风险评估结果',
            { type: 'warning' }
          )
          this.loadData() // 重新加载数据
        } else {
          this.$message.error(response.message || '评估失败')
        }
      } catch (error) {
        console.error('风险评估失败:', error)
        this.$message.error('评估失败')
      }
    },
    
    // 优化方案
    async handleOptimize() {
      try {
        const response = await optimizePlanningScheme(this.tenantId, this.planningId)
        if (response.success) {
          this.$message.success('方案优化成功')
          this.loadData() // 重新加载数据
        } else {
          this.$message.error(response.message || '优化失败')
        }
      } catch (error) {
        console.error('优化方案失败:', error)
        this.$message.error('优化失败')
      }
    },
    
    // 生成报告
    async handleGenerateReport() {
      try {
        const response = await generatePlanningReport(this.tenantId, this.planningId)
        if (response.success) {
          this.$message.success('报告生成成功')
        } else {
          this.$message.error(response.message || '生成失败')
        }
      } catch (error) {
        console.error('生成报告失败:', error)
        this.$message.error('生成失败')
      }
    },
    
    // 复制筹划
    async handleCopy() {
      try {
        const { value: newName } = await this.$prompt('请输入新筹划名称', '复制筹划', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: this.planningData.planningName + '_副本'
        })
        
        const response = await copyPlanning(this.tenantId, this.planningId, newName)
        if (response.success) {
          this.$message.success('复制成功')
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '复制失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('复制筹划失败:', error)
          this.$message.error('复制失败')
        }
      }
    },
    
    // 归档
    async handleArchive() {
      try {
        await this.$confirm('确认归档该筹划吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await batchArchivePlannings(this.tenantId, [this.planningId])
        if (response.success) {
          this.$message.success('归档成功')
          this.loadData()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '归档失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('归档失败:', error)
          this.$message.error('归档失败')
        }
      }
    },
    
    // 文件上传成功
    handleUploadSuccess(response, file, fileList) {
      this.$message.success('上传成功')
      this.loadDocuments()
    },
    
    // 文件上传失败
    handleUploadError(err, file, fileList) {
      this.$message.error('上传失败')
    },
    
    // 下载文件
    handleDownload(row) {
      this.$message.info('下载功能开发中')
    },
    
    // 预览文件
    handlePreview(row) {
      this.$message.info('预览功能开发中')
    },
    
    // 删除文档
    handleDeleteDoc(row) {
      this.$message.info('删除功能开发中')
    },
    
    // 格式化方法
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleDateString('zh-CN')
    },
    
    formatDateTime(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    },
    
    formatFileSize(row, column, cellValue) {
      if (!cellValue) return '-'
      const size = Number(cellValue)
      if (size < 1024) return size + 'B'
      if (size < 1024 * 1024) return (size / 1024).toFixed(1) + 'KB'
      return (size / (1024 * 1024)).toFixed(1) + 'MB'
    },
    
    // 标签类型方法
    getPlanningStatusTagType(status) {
      const typeMap = {
        'DRAFT': '',
        'UNDER_REVIEW': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'SUSPENDED': 'info',
        'CANCELLED': '',
        'ARCHIVED': 'info'
      }
      return typeMap[status] || ''
    },
    
    getExecutionStatusTagType(status) {
      const typeMap = {
        'NOT_STARTED': '',
        'PREPARING': 'warning',
        'EXECUTING': 'primary',
        'PAUSED': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'CANCELLED': ''
      }
      return typeMap[status] || ''
    },
    
    getRiskLevelTagType(level) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return typeMap[level] || ''
    },
    
    getPriorityTagType(priority) {
      const typeMap = {
        'LOW': 'info',
        'NORMAL': '',
        'HIGH': 'warning',
        'URGENT': 'danger'
      }
      return typeMap[priority] || ''
    },
    
    getFeasibilityTagType(level) {
      const typeMap = {
        'HIGH': 'success',
        'MEDIUM': 'warning',
        'LOW': 'danger'
      }
      return typeMap[level] || ''
    },
    
    getTimelineType(type) {
      const typeMap = {
        'primary': 'primary',
        'success': 'success',
        'warning': 'warning',
        'danger': 'danger'
      }
      return typeMap[type] || 'primary'
    },
    
    getBenefitClass(benefit) {
      if (!benefit) return ''
      return Number(benefit) >= 0 ? 'success' : 'danger'
    },
    
    getRoiClass(roi) {
      if (!roi) return ''
      const value = Number(roi)
      if (value >= 0.2) return 'success'
      if (value >= 0.1) return 'warning'
      return 'danger'
    },
    
    getFeasibilityLabel(level) {
      const labels = {
        'HIGH': '高可行性',
        'MEDIUM': '中等可行性',
        'LOW': '低可行性'
      }
      return labels[level] || level
    },
    
    // 工具方法
    getPlanningTypeLabel,
    getPlanningStatusLabel,
    getExecutionStatusLabel,
    getRiskLevelLabel,
    getPriorityLabel,
    getTaxTypeLabel,
    formatAmount,
    formatPercentage
  }
}
</script>

<style lang="scss" scoped>
.tax-planning-detail {
  .detail-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    padding: 20px;
    background: white;
    border-radius: 8px;
    margin-bottom: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    
    .header-left {
      .planning-title {
        margin: 0 0 12px 0;
        font-size: 20px;
        font-weight: 600;
        color: #303133;
        
        .el-tag {
          margin-left: 12px;
        }
      }
      
      .planning-meta {
        .meta-item {
          margin-right: 24px;
          color: #909399;
          font-size: 14px;
          
          i {
            margin-right: 4px;
          }
        }
      }
    }
    
    .header-right {
      .el-button {
        margin-left: 12px;
      }
    }
  }
  
  .metrics-cards {
    margin-bottom: 20px;
    
    .metric-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      
      .metric-icon {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;
        
        i {
          font-size: 24px;
          color: white;
        }
        
        &.tax-saving {
          background: linear-gradient(135deg, #4facfe, #00f2fe);
        }
        
        &.net-benefit {
          background: linear-gradient(135deg, #43e97b, #38f9d7);
        }
        
        &.roi {
          background: linear-gradient(135deg, #667eea, #764ba2);
        }
        
        &.progress {
          background: linear-gradient(135deg, #f093fb, #f5576c);
        }
      }
      
      .metric-content {
        .metric-value {
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          line-height: 1;
          margin-bottom: 4px;
          
          &.success {
            color: #67C23A;
          }
          
          &.warning {
            color: #E6A23C;
          }
          
          &.danger {
            color: #F56C6C;
          }
        }
        
        .metric-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }
  
  .detail-tabs {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    
    .el-tabs {
      padding: 20px;
    }
  }
  
  .info-section {
    .info-group {
      margin-bottom: 24px;
      
      &.full-width {
        width: 100%;
      }
      
      h4 {
        margin: 0 0 16px 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        border-bottom: 2px solid #409EFF;
        padding-bottom: 8px;
      }
      
      .info-item {
        display: flex;
        align-items: center;
        margin-bottom: 12px;
        
        label {
          width: 120px;
          color: #606266;
          font-weight: 500;
        }
        
        span {
          color: #303133;
        }
      }
      
      .description-content {
        padding: 16px;
        background: #f5f7fa;
        border-radius: 4px;
        color: #606266;
        line-height: 1.6;
        min-height: 60px;
      }
    }
  }
  
  .financial-section {
    .analysis-card {
      .analysis-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;
        
        label {
          color: #606266;
          font-weight: 500;
        }
        
        .amount-text {
          font-weight: 600;
          
          &.success {
            color: #67C23A;
          }
          
          &.danger {
            color: #F56C6C;
          }
          
          &.total {
            font-size: 16px;
            color: #303133;
          }
        }
        
        .percentage-text {
          font-weight: 600;
          
          &.success {
            color: #67C23A;
          }
          
          &.warning {
            color: #E6A23C;
          }
          
          &.danger {
            color: #F56C6C;
          }
        }
      }
    }
  }
  
  .execution-section {
    .execution-timeline {
      padding: 20px;
      
      .operator-info {
        font-size: 12px;
        color: #909399;
        margin-top: 8px;
      }
    }
  }
  
  .documents-section {
    .upload-area {
      margin-bottom: 20px;
    }
    
    .documents-list {
      .el-table {
        border: 1px solid #EBEEF5;
        border-radius: 4px;
      }
    }
  }
}
</style>
