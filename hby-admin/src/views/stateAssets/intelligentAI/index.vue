<template>
  <div class="app-container">
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-cpu" style="color: #409eff"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.totalModels || 0 }}</div>
              <div class="statistics-label">AI模型总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-data-analysis" style="color: #67c23a"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.runningModels || 0 }}</div>
              <div class="statistics-label">运行中模型</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-magic-stick" style="color: #e6a23c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.todayPredictions || 0 }}</div>
              <div class="statistics-label">今日预测次数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-trophy" style="color: #f56c6c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.avgAccuracy || 0 }}%</div>
              <div class="statistics-label">平均准确率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能标签页 -->
    <el-card>
      <el-tabs v-model="activeTab" type="border-card">
        <!-- AI模型管理 -->
        <el-tab-pane label="AI模型管理" name="models">
          <div class="tab-content">
            <!-- 查询表单 -->
            <el-form :model="modelsQuery" ref="modelsForm" :inline="true" label-width="100px" class="mb-20">
              <el-form-item label="模型名称">
                <el-input v-model="modelsQuery.modelName" placeholder="请输入模型名称" style="width: 200px"></el-input>
              </el-form-item>
              <el-form-item label="模型类型">
                <el-select v-model="modelsQuery.modelType" placeholder="请选择模型类型" clearable style="width: 150px">
                  <el-option label="风险预测" value="RISK_PREDICTION"></el-option>
                  <el-option label="财务分析" value="FINANCIAL_ANALYSIS"></el-option>
                  <el-option label="异常检测" value="ANOMALY_DETECTION"></el-option>
                  <el-option label="智能推荐" value="RECOMMENDATION"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="模型状态">
                <el-select v-model="modelsQuery.status" placeholder="请选择状态" clearable style="width: 120px">
                  <el-option label="运行中" value="RUNNING"></el-option>
                  <el-option label="已停止" value="STOPPED"></el-option>
                  <el-option label="训练中" value="TRAINING"></el-option>
                  <el-option label="已部署" value="DEPLOYED"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getModelsList" icon="el-icon-search">查询</el-button>
                <el-button @click="resetModelsQuery" icon="el-icon-refresh">重置</el-button>
                <el-button type="success" @click="handleAddModel" icon="el-icon-plus">新增模型</el-button>
                <el-button type="warning" @click="handleTrainModel" icon="el-icon-cpu">模型训练</el-button>
                <el-button type="info" @click="handleExportModels" icon="el-icon-download">导出</el-button>
              </el-form-item>
            </el-form>

            <!-- 模型列表表格 -->
            <el-table v-loading="modelsLoading" :data="modelsList" stripe border style="width: 100%">
              <el-table-column type="selection" width="55" align="center"></el-table-column>
              <el-table-column prop="modelName" label="模型名称" min-width="200" show-overflow-tooltip></el-table-column>
              <el-table-column prop="modelType" label="模型类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getModelTypeTag(scope.row.modelType)">
                    {{ getModelTypeText(scope.row.modelType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="version" label="版本" width="100" align="center"></el-table-column>
              <el-table-column prop="accuracy" label="准确率" width="100" align="center">
                <template slot-scope="scope">
                  <el-progress
                    :percentage="scope.row.accuracy"
                    :color="getAccuracyColor(scope.row.accuracy)"
                    :show-text="false"
                    style="width: 60px"
                  ></el-progress>
                  <span style="margin-left: 10px">{{ scope.row.accuracy }}%</span>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusTag(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="lastTrainTime" label="最后训练时间" width="160" align="center"></el-table-column>
              <el-table-column label="操作" width="300" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewModel(scope.row)" icon="el-icon-view">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEditModel(scope.row)" icon="el-icon-edit">编辑</el-button>
                  <el-button size="mini" type="success" @click="handleDeployModel(scope.row)" icon="el-icon-upload2">部署</el-button>
                  <el-button size="mini" type="warning" @click="handleTestModel(scope.row)" icon="el-icon-cpu">测试</el-button>
                  <el-button size="mini" type="danger" @click="handleDeleteModel(scope.row)" icon="el-icon-delete">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页组件 -->
            <pagination
              v-show="modelsTotal > 0"
              :total="modelsTotal"
              :page.sync="modelsQuery.pageNum"
              :limit.sync="modelsQuery.pageSize"
              @pagination="getModelsList"
            />
          </div>
        </el-tab-pane>

        <!-- 智能预测分析 -->
        <el-tab-pane label="智能预测分析" name="predictions">
          <div class="tab-content">
            <!-- 预测配置 -->
            <el-card class="mb-20">
              <div slot="header" class="card-header">
                <span>预测配置</span>
              </div>
              <el-form :model="predictionForm" :inline="true" label-width="120px">
                <el-form-item label="预测模型">
                  <el-select v-model="predictionForm.modelId" placeholder="请选择预测模型" style="width: 200px">
                    <el-option
                      v-for="model in availableModels"
                      :key="model.id"
                      :label="model.name"
                      :value="model.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="预测目标">
                  <el-select v-model="predictionForm.target" placeholder="请选择预测目标" style="width: 200px">
                    <el-option label="财务风险" value="FINANCIAL_RISK"></el-option>
                    <el-option label="经营风险" value="OPERATIONAL_RISK"></el-option>
                    <el-option label="市场风险" value="MARKET_RISK"></el-option>
                    <el-option label="信用风险" value="CREDIT_RISK"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="预测周期">
                  <el-select v-model="predictionForm.period" placeholder="请选择预测周期" style="width: 150px">
                    <el-option label="1个月" value="1M"></el-option>
                    <el-option label="3个月" value="3M"></el-option>
                    <el-option label="6个月" value="6M"></el-option>
                    <el-option label="1年" value="1Y"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleStartPrediction" icon="el-icon-magic-stick">开始预测</el-button>
                  <el-button type="success" @click="handleBatchPrediction" icon="el-icon-s-data">批量预测</el-button>
                </el-form-item>
              </el-form>
            </el-card>

            <!-- 预测结果 -->
            <el-card>
              <div slot="header" class="card-header">
                <span>预测结果</span>
                <el-button type="text" @click="refreshPredictions">刷新</el-button>
              </div>
              
              <!-- 预测结果图表 -->
              <div class="prediction-charts">
                <el-row :gutter="20">
                  <el-col :span="12">
                    <div class="chart-container">
                      <div class="chart-title">风险趋势预测</div>
                      <div ref="riskTrendChart" style="height: 300px;"></div>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <div class="chart-container">
                      <div class="chart-title">风险分布预测</div>
                      <div ref="riskDistributionChart" style="height: 300px;"></div>
                    </div>
                  </el-col>
                </el-row>
              </div>

              <!-- 预测结果列表 -->
              <el-table :data="predictionsList" stripe border style="width: 100%; margin-top: 20px;">
                <el-table-column prop="enterpriseName" label="企业名称" min-width="200" show-overflow-tooltip></el-table-column>
                <el-table-column prop="predictionType" label="预测类型" width="120" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getPredictionTypeTag(scope.row.predictionType)">
                      {{ getPredictionTypeText(scope.row.predictionType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="riskLevel" label="风险等级" width="120" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getRiskLevelTag(scope.row.riskLevel)">
                      {{ scope.row.riskLevel }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="probability" label="风险概率" width="120" align="center">
                  <template slot-scope="scope">
                    <el-progress
                      :percentage="scope.row.probability"
                      :color="getProbabilityColor(scope.row.probability)"
                      :show-text="false"
                      style="width: 60px"
                    ></el-progress>
                    <span style="margin-left: 10px">{{ scope.row.probability }}%</span>
                  </template>
                </el-table-column>
                <el-table-column prop="confidence" label="置信度" width="100" align="center">
                  <template slot-scope="scope">
                    {{ scope.row.confidence }}%
                  </template>
                </el-table-column>
                <el-table-column prop="predictionTime" label="预测时间" width="160" align="center"></el-table-column>
                <el-table-column label="操作" width="200" align="center">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="handleViewPrediction(scope.row)" icon="el-icon-view">查看详情</el-button>
                    <el-button size="mini" type="primary" @click="handleAnalyzePrediction(scope.row)" icon="el-icon-data-analysis">分析</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- 智能推荐系统 -->
        <el-tab-pane label="智能推荐系统" name="recommendations">
          <div class="tab-content">
            <!-- 推荐配置 -->
            <el-card class="mb-20">
              <div slot="header" class="card-header">
                <span>推荐配置</span>
              </div>
              <el-form :model="recommendationForm" :inline="true" label-width="120px">
                <el-form-item label="推荐类型">
                  <el-select v-model="recommendationForm.type" placeholder="请选择推荐类型" style="width: 200px">
                    <el-option label="监管措施推荐" value="SUPERVISION_MEASURES"></el-option>
                    <el-option label="风险处置推荐" value="RISK_DISPOSAL"></el-option>
                    <el-option label="投资决策推荐" value="INVESTMENT_DECISION"></el-option>
                    <el-option label="合规建议推荐" value="COMPLIANCE_ADVICE"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="目标企业">
                  <CompanyTreeModal
                    v-model="recommendationForm.enterpriseId"
                    :enterprise-name.sync="recommendationForm.enterpriseName"
                    placeholder="请选择企业"
                    style="width: 200px"
                  />
                </el-form-item>
                <el-form-item label="推荐数量">
                  <el-input-number
                    v-model="recommendationForm.count"
                    :min="1"
                    :max="20"
                    style="width: 120px"
                  ></el-input-number>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleGenerateRecommendations" icon="el-icon-magic-stick">生成推荐</el-button>
                  <el-button type="success" @click="handleRefreshRecommendations" icon="el-icon-refresh">刷新推荐</el-button>
                </el-form-item>
              </el-form>
            </el-card>

            <!-- 推荐结果 -->
            <el-card>
              <div slot="header" class="card-header">
                <span>推荐结果</span>
              </div>
              
              <el-table :data="recommendationsList" stripe border style="width: 100%;">
                <el-table-column prop="title" label="推荐标题" min-width="250" show-overflow-tooltip></el-table-column>
                <el-table-column prop="type" label="推荐类型" width="150" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getRecommendationTypeTag(scope.row.type)">
                      {{ getRecommendationTypeText(scope.row.type) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="priority" label="优先级" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getPriorityTag(scope.row.priority)">
                      {{ scope.row.priority }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="confidence" label="置信度" width="100" align="center">
                  <template slot-scope="scope">
                    {{ scope.row.confidence }}%
                  </template>
                </el-table-column>
                <el-table-column prop="applicableScope" label="适用范围" width="150" align="center"></el-table-column>
                <el-table-column prop="generateTime" label="生成时间" width="160" align="center"></el-table-column>
                <el-table-column label="操作" width="250" align="center">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="handleViewRecommendation(scope.row)" icon="el-icon-view">查看</el-button>
                    <el-button size="mini" type="primary" @click="handleApplyRecommendation(scope.row)" icon="el-icon-check">采纳</el-button>
                    <el-button size="mini" type="warning" @click="handleFeedbackRecommendation(scope.row)" icon="el-icon-chat-dot-round">反馈</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- 模型训练监控 -->
        <el-tab-pane label="模型训练监控" name="training">
          <div class="tab-content">
            <!-- 训练任务列表 -->
            <el-card>
              <div slot="header" class="card-header">
                <span>训练任务</span>
                <el-button type="primary" @click="handleCreateTrainingTask">创建训练任务</el-button>
              </div>
              
              <el-table :data="trainingTasksList" stripe border style="width: 100%;">
                <el-table-column prop="taskName" label="任务名称" min-width="200" show-overflow-tooltip></el-table-column>
                <el-table-column prop="modelType" label="模型类型" width="120" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getModelTypeTag(scope.row.modelType)">
                      {{ getModelTypeText(scope.row.modelType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="训练状态" width="120" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getTrainingStatusTag(scope.row.status)">
                      {{ getTrainingStatusText(scope.row.status) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="progress" label="训练进度" width="150" align="center">
                  <template slot-scope="scope">
                    <el-progress :percentage="scope.row.progress" :status="getProgressStatus(scope.row.progress)"></el-progress>
                  </template>
                </el-table-column>
                <el-table-column prop="accuracy" label="当前准确率" width="120" align="center">
                  <template slot-scope="scope">
                    {{ scope.row.accuracy }}%
                  </template>
                </el-table-column>
                <el-table-column prop="startTime" label="开始时间" width="160" align="center"></el-table-column>
                <el-table-column prop="estimatedTime" label="预计完成" width="160" align="center"></el-table-column>
                <el-table-column label="操作" width="200" align="center">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="handleViewTrainingTask(scope.row)" icon="el-icon-view">查看</el-button>
                    <el-button size="mini" type="warning" @click="handleStopTraining(scope.row)" icon="el-icon-video-pause" v-if="scope.row.status === 'RUNNING'">停止</el-button>
                    <el-button size="mini" type="success" @click="handleResumeTraining(scope.row)" icon="el-icon-video-play" v-if="scope.row.status === 'PAUSED'">继续</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 对话框组件 -->
    <AIModelDialog
      :visible.sync="modelDialogVisible"
      :model-data="currentModel"
      :dialog-type="dialogType"
      @refresh="getModelsList"
    />

    <PredictionDetailDialog
      :visible.sync="predictionDetailVisible"
      :prediction-data="currentPrediction"
    />

    <RecommendationDetailDialog
      :visible.sync="recommendationDetailVisible"
      :recommendation-data="currentRecommendation"
    />

    <TrainingTaskDialog
      :visible.sync="trainingTaskDialogVisible"
      :task-data="currentTrainingTask"
      :dialog-type="dialogType"
      @refresh="getTrainingTasksList"
    />
  </div>
</template>

<script>
import { getIntelligentAIStatistics, getAIModelsList, getPredictionsList, getRecommendationsList, getTrainingTasksList } from '@/api/stateAssets/intelligentAI'
import CompanyTreeModal from '@/components/CompanyTreeModal'
import Pagination from '@/components/Pagination'
import AIModelDialog from './components/AIModelDialog'
import PredictionDetailDialog from './components/PredictionDetailDialog'
import RecommendationDetailDialog from './components/RecommendationDetailDialog'
import TrainingTaskDialog from './components/TrainingTaskDialog'
import * as echarts from 'echarts'

export default {
  name: 'IntelligentAI',
  components: {
    CompanyTreeModal,
    Pagination,
    AIModelDialog,
    PredictionDetailDialog,
    RecommendationDetailDialog,
    TrainingTaskDialog
  },
  data() {
    return {
      activeTab: 'models',
      statistics: {},
      
      // AI模型相关
      modelsLoading: false,
      modelsList: [],
      modelsTotal: 0,
      modelsQuery: {
        pageNum: 1,
        pageSize: 10,
        modelName: '',
        modelType: '',
        status: ''
      },
      
      // 智能预测相关
      predictionForm: {
        modelId: '',
        target: '',
        period: ''
      },
      predictionsList: [],
      availableModels: [],
      
      // 智能推荐相关
      recommendationForm: {
        type: '',
        enterpriseId: '',
        enterpriseName: '',
        count: 5
      },
      recommendationsList: [],
      
      // 模型训练相关
      trainingTasksList: [],
      
      // 对话框相关
      modelDialogVisible: false,
      predictionDetailVisible: false,
      recommendationDetailVisible: false,
      trainingTaskDialogVisible: false,
      dialogType: 'add',
      currentModel: {},
      currentPrediction: {},
      currentRecommendation: {},
      currentTrainingTask: {}
    }
  },
  created() {
    this.getStatistics()
    this.getModelsList()
    this.initCharts()
  },
  methods: {
    // 获取统计数据
    getStatistics() {
      getIntelligentAIStatistics().then(response => {
        if (response.code === 1) {
          this.statistics = response.data || {}
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
      })
    },

    // AI模型相关方法
    getModelsList() {
      this.modelsLoading = true
      getAIModelsList(this.modelsQuery).then(response => {
        if (response.code === 1) {
          this.modelsList = response.data.list || []
          this.modelsTotal = response.data.total || 0
          this.availableModels = this.modelsList.filter(model => model.status === 'DEPLOYED')
        } else {
          this.$message.error(response.msg || '获取AI模型列表失败')
        }
        this.modelsLoading = false
      }).catch(error => {
        console.error('获取AI模型列表异常:', error)
        this.$message.warning('获取数据暂未开放')
        this.modelsLoading = false
      })
    },

    resetModelsQuery() {
      this.$refs.modelsForm.resetFields()
      this.modelsQuery = {
        pageNum: 1,
        pageSize: 10,
        modelName: '',
        modelType: '',
        status: ''
      }
      this.getModelsList()
    },

    handleAddModel() {
      this.currentModel = {}
      this.dialogType = 'add'
      this.modelDialogVisible = true
    },

    handleViewModel(row) {
      this.currentModel = { ...row }
      this.dialogType = 'view'
      this.modelDialogVisible = true
    },

    handleEditModel(row) {
      this.currentModel = { ...row }
      this.dialogType = 'edit'
      this.modelDialogVisible = true
    },

    handleDeployModel(row) {
      this.$message.success(`正在部署模型：${row.modelName}`)
    },

    handleTestModel(row) {
      this.$message.success(`正在测试模型：${row.modelName}`)
    },

    handleDeleteModel(row) {
      this.$confirm(`确定要删除模型"${row.modelName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.getModelsList()
      }).catch(() => {
          // 用户取消操作
        })
      },

    handleTrainModel() {
      this.$message.success('正在启动模型训练...')
    },

    handleExportModels() {
      this.$message.success('正在导出AI模型数据...')
    },

    // 智能预测相关方法
    handleStartPrediction() {
      if (!this.predictionForm.modelId || !this.predictionForm.target || !this.predictionForm.period) {
        this.$message.warning('请完善预测配置信息')
        return
      }
      this.$message.success('正在开始预测分析...')
      this.getPredictionsList()
    },

    handleBatchPrediction() {
      this.$message.success('正在进行批量预测...')
    },

    getPredictionsList() {
      getPredictionsList(this.predictionForm).then(response => {
        if (response.code === 1) {
          this.predictionsList = response.data.list || []
        }
      }).catch(error => {
        console.error('获取预测结果失败:', error)
      })
    },

    refreshPredictions() {
      this.getPredictionsList()
      this.$message.success('预测结果已刷新')
    },

    handleViewPrediction(row) {
      this.currentPrediction = { ...row }
      this.predictionDetailVisible = true
    },

    handleAnalyzePrediction(row) {
      this.$message.success(`正在分析预测结果：${row.enterpriseName}`)
    },

    // 智能推荐相关方法
    handleGenerateRecommendations() {
      if (!this.recommendationForm.type) {
        this.$message.warning('请选择推荐类型')
        return
      }
      this.$message.success('正在生成智能推荐...')
      this.getRecommendationsList()
    },

    handleRefreshRecommendations() {
      this.getRecommendationsList()
      this.$message.success('推荐结果已刷新')
    },

    getRecommendationsList() {
      getRecommendationsList(this.recommendationForm).then(response => {
        if (response.code === 1) {
          this.recommendationsList = response.data.list || []
        }
      }).catch(error => {
        console.error('获取推荐结果失败:', error)
      })
    },

    handleViewRecommendation(row) {
      this.currentRecommendation = { ...row }
      this.recommendationDetailVisible = true
    },

    handleApplyRecommendation(row) {
      this.$message.success(`正在采纳推荐：${row.title}`)
    },

    handleFeedbackRecommendation(row) {
      this.$message.success(`正在提交反馈：${row.title}`)
    },

    // 模型训练相关方法
    getTrainingTasksList() {
      getTrainingTasksList().then(response => {
        if (response.code === 1) {
          this.trainingTasksList = response.data.list || []
        }
      }).catch(error => {
        console.error('获取训练任务列表失败:', error)
      })
    },

    handleCreateTrainingTask() {
      this.currentTrainingTask = {}
      this.dialogType = 'add'
      this.trainingTaskDialogVisible = true
    },

    handleViewTrainingTask(row) {
      this.currentTrainingTask = { ...row }
      this.dialogType = 'view'
      this.trainingTaskDialogVisible = true
    },

    handleStopTraining(row) {
      this.$message.success(`正在停止训练任务：${row.taskName}`)
    },

    handleResumeTraining(row) {
      this.$message.success(`正在继续训练任务：${row.taskName}`)
    },

    // 图表初始化
    initCharts() {
      this.$nextTick(() => {
        this.initRiskTrendChart()
        this.initRiskDistributionChart()
      })
    },

    initRiskTrendChart() {
      if (!this.$refs.riskTrendChart) return
      const chart = echarts.init(this.$refs.riskTrendChart)
      const option = {
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: { type: 'value' },
        series: [{
          name: '风险指数',
          type: 'line',
          data: [65, 59, 80, 81, 56, 55],
          smooth: true
        }]
      }
      chart.setOption(option)
    },

    initRiskDistributionChart() {
      if (!this.$refs.riskDistributionChart) return
      const chart = echarts.init(this.$refs.riskDistributionChart)
      const option = {
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          radius: '70%',
          data: [
            { value: 35, name: '低风险' },
            { value: 45, name: '中风险' },
            { value: 20, name: '高风险' }
          ]
        }]
      }
      chart.setOption(option)
    },

    // 工具方法
    getModelTypeTag(type) {
      const tagMap = {
        'RISK_PREDICTION': 'danger',
        'FINANCIAL_ANALYSIS': 'primary',
        'ANOMALY_DETECTION': 'warning',
        'RECOMMENDATION': 'success'
      }
      return tagMap[type] || 'info'
    },

    getModelTypeText(type) {
      const textMap = {
        'RISK_PREDICTION': '风险预测',
        'FINANCIAL_ANALYSIS': '财务分析',
        'ANOMALY_DETECTION': '异常检测',
        'RECOMMENDATION': '智能推荐'
      }
      return textMap[type] || type
    },

    getStatusTag(status) {
      const tagMap = {
        'RUNNING': 'success',
        'STOPPED': 'info',
        'TRAINING': 'warning',
        'DEPLOYED': 'primary'
      }
      return tagMap[status] || 'info'
    },

    getStatusText(status) {
      const textMap = {
        'RUNNING': '运行中',
        'STOPPED': '已停止',
        'TRAINING': '训练中',
        'DEPLOYED': '已部署'
      }
      return textMap[status] || status
    },

    getAccuracyColor(accuracy) {
      if (accuracy >= 90) return '#67c23a'
      if (accuracy >= 80) return '#e6a23c'
      return '#f56c6c'
    },

    getPredictionTypeTag(type) {
      const tagMap = {
        'FINANCIAL_RISK': 'primary',
        'OPERATIONAL_RISK': 'warning',
        'MARKET_RISK': 'success',
        'CREDIT_RISK': 'danger'
      }
      return tagMap[type] || 'info'
    },

    getPredictionTypeText(type) {
      const textMap = {
        'FINANCIAL_RISK': '财务风险',
        'OPERATIONAL_RISK': '经营风险',
        'MARKET_RISK': '市场风险',
        'CREDIT_RISK': '信用风险'
      }
      return textMap[type] || type
    },

    getRiskLevelTag(level) {
      const tagMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return tagMap[level] || 'info'
    },

    getProbabilityColor(probability) {
      if (probability >= 80) return '#f56c6c'
      if (probability >= 60) return '#e6a23c'
      return '#67c23a'
    },

    getRecommendationTypeTag(type) {
      const tagMap = {
        'SUPERVISION_MEASURES': 'primary',
        'RISK_DISPOSAL': 'danger',
        'INVESTMENT_DECISION': 'success',
        'COMPLIANCE_ADVICE': 'warning'
      }
      return tagMap[type] || 'info'
    },

    getRecommendationTypeText(type) {
      const textMap = {
        'SUPERVISION_MEASURES': '监管措施推荐',
        'RISK_DISPOSAL': '风险处置推荐',
        'INVESTMENT_DECISION': '投资决策推荐',
        'COMPLIANCE_ADVICE': '合规建议推荐'
      }
      return textMap[type] || type
    },

    getPriorityTag(priority) {
      const tagMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return tagMap[priority] || 'info'
    },

    getTrainingStatusTag(status) {
      const tagMap = {
        'RUNNING': 'success',
        'COMPLETED': 'primary',
        'FAILED': 'danger',
        'PAUSED': 'warning'
      }
      return tagMap[status] || 'info'
    },

    getTrainingStatusText(status) {
      const textMap = {
        'RUNNING': '训练中',
        'COMPLETED': '已完成',
        'FAILED': '训练失败',
        'PAUSED': '已暂停'
      }
      return textMap[status] || status
    },

    getProgressStatus(progress) {
      if (progress === 100) return 'success'
      if (progress >= 80) return null
      return null
    }
  }
}
</script>

<style scoped>
.statistics-card {
  margin-bottom: 20px;
}

.statistics-content {
  display: flex;
  align-items: center;
}

.statistics-icon {
  font-size: 40px;
  margin-right: 20px;
}

.statistics-info {
  flex: 1;
}

.statistics-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.statistics-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.tab-content {
  padding: 20px 0;
}

.mb-20 {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.prediction-charts {
  margin-bottom: 20px;
}

.chart-container {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 20px;
}

.chart-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  text-align: center;
}
</style>
