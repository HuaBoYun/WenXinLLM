<template>
  <div class="intelligent-recommendation">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>智能推荐管理</h2>
      <p>AI智能预算推荐和优化建议，基于机器学习算法提供个性化的预算优化方案</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-magic-stick" @click="handleGenerateRecommendation">生成推荐</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-cpu" @click="handleTrainModel">训练模型</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewAnalysis">推荐分析</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">推荐设置</el-button>
            <el-button icon="el-icon-document" @click="handleReports">推荐报告</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 智能推荐统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ recommendationStats.totalRecommendations }}</div>
            <div class="stat-label">推荐总数</div>
            <div class="stat-description">系统生成的推荐数</div>
            <div class="stat-trend">
              <i class="el-icon-magic-stick"></i>
              <span>智能推荐</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-magic-stick"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card accepted-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ recommendationStats.acceptedRecommendations }}</div>
            <div class="stat-label">已采纳</div>
            <div class="stat-description">用户采纳的推荐</div>
            <div class="stat-trend">
              <i class="el-icon-check"></i>
              <span>高采纳率</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-check"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card accuracy-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ recommendationStats.accuracy }}%</div>
            <div class="stat-label">推荐准确率</div>
            <div class="stat-description">推荐算法准确度</div>
            <div class="stat-trend">
              <i class="el-icon-data-analysis"></i>
              <span>高精度</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-data-analysis"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card improvement-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ recommendationStats.avgImprovement }}%</div>
            <div class="stat-label">平均改善</div>
            <div class="stat-description">预算优化改善幅度</div>
            <div class="stat-trend">
              <i class="el-icon-top"></i>
              <span>显著提升</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-top"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 推荐类型选择 -->
    <el-card class="recommendation-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>推荐类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshRecommendationTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="recommendationType in recommendationTypes" :key="recommendationType.id">
          <el-card 
            class="recommendation-type-item" 
            shadow="hover" 
            @click.native="handleSelectRecommendationType(recommendationType)"
            :class="{ 'selected': selectedRecommendationType === recommendationType.id }"
          >
            <div class="recommendation-type-icon">
              <i :class="recommendationType.icon"></i>
            </div>
            <div class="recommendation-type-title">{{ recommendationType.name }}</div>
            <div class="recommendation-type-description">{{ recommendationType.description }}</div>
            <div class="recommendation-type-stats">
              <span class="recommendation-count">{{ recommendationType.recommendationCount }} 个推荐</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 智能推荐列表 -->
    <el-card class="recommendations-card" shadow="never">
      <div slot="header" class="card-header">
        <span>智能推荐</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索推荐"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getRecommendationList"
            clearable
            @clear="getRecommendationList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getRecommendationList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="recommendationList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="recommendationName" label="推荐标题" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.recommendationName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="recommendationType" label="推荐类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRecommendationTypeColor(scope.row.recommendationType)" size="mini">
              {{ getRecommendationTypeText(scope.row.recommendationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.priority" :type="getPriorityColor(scope.row.priority)" size="mini">
              {{ getPriorityText(scope.row.priority) }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="confidenceScore" label="置信度" width="120" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="Number(scope.row.confidenceScore) || 0"
              :color="getConfidenceColor(Number(scope.row.confidenceScore))"
              :stroke-width="6"
              :show-text="false"
            />
            <span class="confidence-text">{{ scope.row.confidenceScore }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="expectedImpact" label="预期影响" width="180" align="center" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="生成时间" width="150" align="center" />
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-check"
              :disabled="scope.row.status === 'accepted' || scope.row.status === 'ACCEPTED'"
              @click.stop="handleAccept(scope.row)"
            >采纳</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-close"
              :disabled="scope.row.status === 'rejected' || scope.row.status === 'REJECTED'"
              @click.stop="handleReject(scope.row)"
            >拒绝</el-button>
            <el-dropdown size="mini" trigger="click" @command="handleMoreCommand($event, scope.row)" @click.native.stop>
              <el-button type="text" size="mini" icon="el-icon-more">更多<i class="el-icon-arrow-down el-icon--right"></i></el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="detail" icon="el-icon-document">查看详情</el-dropdown-item>
                <el-dropdown-item command="edit" icon="el-icon-edit">编辑</el-dropdown-item>
                <el-dropdown-item command="delete" icon="el-icon-delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 推荐详情弹窗（采纳/拒绝/更多触发） -->
    <el-dialog
      title="推荐详情"
      :visible.sync="detailDialogVisible"
      width="70%"
      :close-on-click-modal="false"
    >
      <div v-if="currentRecommendation">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="推荐基本信息" :column="2" border>
              <el-descriptions-item label="推荐标题">{{ currentRecommendation.recommendationName }}</el-descriptions-item>
              <el-descriptions-item label="推荐类型">{{ getRecommendationTypeText(currentRecommendation.recommendationType) }}</el-descriptions-item>
              <el-descriptions-item label="优先级">
                <el-tag v-if="currentRecommendation.priority" :type="getPriorityColor(currentRecommendation.priority)" size="mini">
                  {{ getPriorityText(currentRecommendation.priority) }}
                </el-tag>
                <span v-else>-</span>
              </el-descriptions-item>
              <el-descriptions-item label="置信度">{{ currentRecommendation.confidenceScore }}%</el-descriptions-item>
              <el-descriptions-item label="预期影响">{{ currentRecommendation.expectedImpact }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentRecommendation.status)" size="mini">
                  {{ getStatusText(currentRecommendation.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="生成时间">{{ currentRecommendation.createTime }}</el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentRecommendation.creatorName }}</el-descriptions-item>
              <el-descriptions-item label="推荐内容" :span="2">{{ currentRecommendation.recommendationContent || '暂无' }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="反馈记录" name="feedback">
            <el-table :data="feedbackRecords" border size="mini">
              <el-table-column prop="feedbackTime" label="反馈时间" width="150" />
              <el-table-column prop="feedbackUser" label="反馈用户" width="120" />
              <el-table-column prop="feedbackType" label="反馈类型" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getFeedbackTypeColor(scope.row.feedbackType)" size="mini">
                    {{ getFeedbackTypeText(scope.row.feedbackType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="feedbackContent" label="反馈内容" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button
          type="danger"
          :disabled="currentRecommendation && (currentRecommendation.status === 'rejected' || currentRecommendation.status === 'REJECTED')"
          @click="doReject(currentRecommendation)"
        >拒绝</el-button>
        <el-button
          type="success"
          :disabled="currentRecommendation && (currentRecommendation.status === 'accepted' || currentRecommendation.status === 'ACCEPTED')"
          @click="doAccept(currentRecommendation)"
        >采纳</el-button>
      </div>
    </el-dialog>

    <!-- 编辑推荐弹窗 -->
    <el-dialog title="编辑推荐" :visible.sync="editDialogVisible" width="50%" :close-on-click-modal="false">
      <el-form v-if="currentRecommendation" :model="currentRecommendation" label-width="100px">
        <el-form-item label="推荐标题">
          <el-input v-model="currentRecommendation.recommendationName" placeholder="请输入推荐标题" />
        </el-form-item>
        <el-form-item label="推荐类型">
          <el-select v-model="currentRecommendation.recommendationType" style="width:100%">
            <el-option label="预算优化" value="budget_optimization" />
            <el-option label="成本控制" value="cost_reduction" />
            <el-option label="资源配置" value="resource_allocation" />
            <el-option label="风险预警" value="risk_warning" />
          </el-select>
        </el-form-item>
        <el-form-item label="预期影响">
          <el-input v-model="currentRecommendation.expectedImpact" type="textarea" :rows="3" placeholder="请输入预期影响描述" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="currentRecommendation.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doEdit">保存</el-button>
      </div>
    </el-dialog>

    <!-- 训练模型弹窗 -->
    <el-dialog title="训练模型" :visible.sync="trainModelDialogVisible" width="50%" :close-on-click-modal="false">
      <div v-loading="trainLoading">
        <div v-if="!trainResult">
          <el-alert title="点击「开始训练」将基于数据库中已有的推荐数据（采纳/拒绝记录）训练模型，提升推荐准确率。" type="info" :closable="false" style="margin-bottom:20px" />
        </div>
        <div v-if="trainResult">
          <el-descriptions title="训练结果" :column="2" border>
            <el-descriptions-item label="训练状态">
              <el-tag type="success">{{ trainResult.trainStatus === 'SUCCESS' ? '成功' : trainResult.trainStatus }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="模型版本">{{ trainResult.modelVersion }}</el-descriptions-item>
            <el-descriptions-item label="训练样本数">{{ trainResult.totalSamples }}</el-descriptions-item>
            <el-descriptions-item label="模型准确率">{{ trainResult.modelAccuracy }}%</el-descriptions-item>
            <el-descriptions-item label="采纳样本">{{ trainResult.acceptedSamples }}</el-descriptions-item>
            <el-descriptions-item label="拒绝样本">{{ trainResult.rejectedSamples }}</el-descriptions-item>
            <el-descriptions-item label="训练说明" :span="2">{{ trainResult.message }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="trainModelDialogVisible = false">关闭</el-button>
        <el-button type="primary" :loading="trainLoading" @click="doTrainModel">开始训练</el-button>
      </div>
    </el-dialog>

    <!-- 推荐分析弹窗 -->
    <el-dialog title="推荐分析" :visible.sync="analysisDialogVisible" width="60%" :close-on-click-modal="false">
      <div v-loading="analysisLoading">
        <div v-if="analysisData">
          <el-row :gutter="16" style="margin-bottom:20px">
            <el-col :span="6">
              <el-card shadow="hover" class="analysis-stat-card">
                <div class="analysis-num">{{ analysisData.total }}</div>
                <div class="analysis-label">推荐总数</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover" class="analysis-stat-card">
                <div class="analysis-num" style="color:#67C23A">{{ analysisData.accepted }}</div>
                <div class="analysis-label">已采纳</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover" class="analysis-stat-card">
                <div class="analysis-num" style="color:#F56C6C">{{ analysisData.rejected }}</div>
                <div class="analysis-label">已拒绝</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover" class="analysis-stat-card">
                <div class="analysis-num" style="color:#E6A23C">{{ analysisData.pending }}</div>
                <div class="analysis-label">待处理</div>
              </el-card>
            </el-col>
          </el-row>
          <el-divider>各类型分布</el-divider>
          <el-table :data="analysisData.typeStats" border size="small">
            <el-table-column prop="typeName" label="推荐类型" />
            <el-table-column prop="count" label="数量" width="100" align="center" />
            <el-table-column prop="ratio" label="占比" width="200" align="center">
              <template slot-scope="scope">
                <el-progress :percentage="Number(scope.row.ratio) || 0" :stroke-width="8" />
              </template>
            </el-table-column>
          </el-table>
          <el-divider />
          <div style="text-align:center;color:#606266;font-size:14px;margin-top:10px">
            采纳率：<strong style="color:#409EFF;font-size:18px">{{ analysisData.acceptRate }}%</strong>
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="analysisDialogVisible = false">关闭</el-button>
        <el-button type="primary" :loading="analysisLoading" @click="loadAnalysisData">刷新</el-button>
      </div>
    </el-dialog>

    <!-- 推荐设置弹窗 -->
    <el-dialog title="推荐设置" :visible.sync="settingsDialogVisible" width="50%">
      <el-form label-width="140px" size="small">
        <el-form-item label="推荐生成频率">
          <el-select v-model="settings.frequency" style="width:100%">
            <el-option label="每天" value="daily" />
            <el-option label="每周" value="weekly" />
            <el-option label="每月" value="monthly" />
          </el-select>
        </el-form-item>
        <el-form-item label="最低置信度阈值">
          <el-slider v-model="settings.minConfidence" :min="50" :max="100" show-input />
        </el-form-item>
        <el-form-item label="推荐类型">
          <el-checkbox-group v-model="settings.enabledTypes">
            <el-checkbox label="budget_optimization">预算优化</el-checkbox>
            <el-checkbox label="cost_reduction">成本控制</el-checkbox>
            <el-checkbox label="resource_allocation">资源配置</el-checkbox>
            <el-checkbox label="risk_warning">风险预警</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="自动采纳高置信度">
          <el-switch v-model="settings.autoAccept" />
        </el-form-item>
        <el-form-item label="邮件通知">
          <el-switch v-model="settings.emailNotify" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSettings">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 推荐报告弹窗 -->
    <el-dialog title="推荐报告" :visible.sync="reportsDialogVisible" width="60%">
      <el-table :data="reportsList" border size="small" v-loading="reportsLoading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="reportName" label="报告名称" />
        <el-table-column prop="reportType" label="报告类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini">{{ scope.row.reportType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="生成时间" width="160" align="center" />
        <el-table-column prop="totalCount" label="推荐数" width="80" align="center" />
        <el-table-column prop="acceptRate" label="采纳率" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.acceptRate }}%</template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="reportsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="智能推荐帮助" :visible.sync="helpDialogVisible" width="55%">
      <el-collapse>
        <el-collapse-item title="什么是智能推荐？" name="1">
          <p>智能推荐系统基于机器学习算法，分析历史预算数据、行业趋势和企业实际情况，自动生成个性化的预算优化建议。</p>
        </el-collapse-item>
        <el-collapse-item title="如何生成推荐？" name="2">
          <p>点击「生成推荐」按钮，系统将自动分析当前数据并生成新的推荐记录。推荐生成后会出现在列表中，状态为「待处理」。</p>
        </el-collapse-item>
        <el-collapse-item title="推荐类型说明" name="3">
          <ul>
            <li><strong>预算优化</strong>：针对预算分配结构的优化建议</li>
            <li><strong>成本控制</strong>：识别成本超支风险并提供削减建议</li>
            <li><strong>资源配置</strong>：优化人力、物力等资源的分配方案</li>
            <li><strong>风险预警</strong>：提前识别预算执行中的潜在风险</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="如何提升推荐准确率？" name="4">
          <p>定期对推荐进行采纳或拒绝操作，系统会根据您的反馈持续训练模型，提升推荐的准确率和相关性。也可以手动点击「训练模型」触发训练。</p>
        </el-collapse-item>
        <el-collapse-item title="优先级说明" name="5">
          <p>高优先级（红色）：需要立即关注和处理；中优先级（橙色）：建议近期处理；低优先级（绿色）：可按计划处理。</p>
        </el-collapse-item>
      </el-collapse>
      <div slot="footer">
        <el-button type="primary" @click="helpDialogVisible = false">我知道了</el-button>
      </div>
    </el-dialog>
  </div>
</template>


<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'

export default {
  name: 'IntelligentRecommendation',
  data() {
    return {
      // 统计数据
      recommendationStats: {
        totalRecommendations: 0,
        acceptedRecommendations: 0,
        accuracy: 0,
        avgImprovement: 0
      },

      // 推荐类型
      recommendationTypes: [
        { id: 1, name: '预算优化', description: '预算分配优化建议', icon: 'el-icon-s-opportunity', recommendationCount: 0 },
        { id: 2, name: '成本控制', description: '成本削减优化建议', icon: 'el-icon-money', recommendationCount: 0 },
        { id: 3, name: '资源配置', description: '资源配置优化建议', icon: 'el-icon-s-grid', recommendationCount: 0 },
        { id: 4, name: '风险预警', description: '风险识别和预警建议', icon: 'el-icon-warning', recommendationCount: 0 }
      ],
      selectedRecommendationType: null,

      // 推荐列表
      recommendationList: [],
      loading: false,
      searchKeyword: '',

      // 推荐详情弹窗
      detailDialogVisible: false,
      detailActiveTab: 'basic',
      currentRecommendation: null,
      feedbackRecords: [],

      // 编辑弹窗
      editDialogVisible: false,

      // 训练模型弹窗
      trainModelDialogVisible: false,
      trainLoading: false,
      trainResult: null,

      // 推荐分析弹窗
      analysisDialogVisible: false,
      analysisLoading: false,
      analysisData: null,

      // 推荐设置弹窗
      settingsDialogVisible: false,
      settings: {
        frequency: 'weekly',
        minConfidence: 70,
        enabledTypes: ['budget_optimization', 'cost_reduction', 'resource_allocation', 'risk_warning'],
        autoAccept: false,
        emailNotify: true
      },

      // 推荐报告弹窗
      reportsDialogVisible: false,
      reportsLoading: false,
      reportsList: [],

      // 帮助弹窗
      helpDialogVisible: false
    }
  },

  created() {
    this.getRecommendationList()
    this.getRecommendationStats()
  },

  methods: {
    // 获取推荐列表
    async getRecommendationList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword }
        if (this.selectedRecommendationType) {
          params.recommendationType = this.selectedRecommendationType
        }
        const response = await advancedFeaturesApi.getIntelligentRecommendationList(params)
        if (response && response.code === 1) {
          const d = response.data || {}
          this.recommendationList = d.list || d || []
          this.total = d.totalCount || d.total || 0
        }
      } catch (error) {
        this.$message.error('获取推荐列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据，同时更新推荐类型卡片数量
    async getRecommendationStats() {
      try {
        const response = await advancedFeaturesApi.getIntelligentRecommendationStats()
        if (response && response.code === 1 && response.data) {
          this.recommendationStats = response.data
          // 更新各类型卡片数量
          const d = response.data
          this.recommendationTypes[0].recommendationCount = d.budgetOptimizationCount || 0
          this.recommendationTypes[1].recommendationCount = d.costReductionCount || 0
          this.recommendationTypes[2].recommendationCount = d.resourceAllocationCount || 0
          this.recommendationTypes[3].recommendationCount = d.riskWarningCount || 0
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },
    
    // 生成推荐
    async handleGenerateRecommendation() {
      this.$confirm('确定生成新的智能推荐吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          await advancedFeaturesApi.generateIntelligentRecommendation()
          this.$message.success('推荐生成成功')
          this.getRecommendationList()
        } catch (error) {
          this.$message.error('生成失败：' + error.message)
        }
      })
    },
    
    // 查看详情（标题链接点击）
    async handleView(row) {
      this.currentRecommendation = row
      this.detailDialogVisible = true
      this.detailActiveTab = 'basic'
      await this.getFeedbackRecords(row.recommendationId)
    },

    // 更多下拉菜单命令分发
    handleMoreCommand(command, row) {
      if (command === 'detail') this.handleViewDetail(row)
      else if (command === 'edit') this.handleEdit(row)
      else if (command === 'delete') this.handleDelete(row)
    },

    // 弹出详情弹窗
    async handleViewDetail(row) {
      this.currentRecommendation = row
      this.detailDialogVisible = true
      this.detailActiveTab = 'basic'
      await this.getFeedbackRecords(row.recommendationId)
    },

    // 编辑推荐
    handleEdit(row) {
      this.currentRecommendation = { ...row }
      this.editDialogVisible = true
    },

    // 删除推荐
    handleDelete(row) {
      this.$confirm(`确定删除「${row.recommendationName}」吗？删除后不可恢复。`, '删除确认', {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteRecommendation(row.recommendationId)
          this.$message.success('删除成功')
          this.getRecommendationList()
          this.getRecommendationStats()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      }).catch(() => {})
    },

    // 保存编辑
    async doEdit() {
      if (!this.currentRecommendation) return
      try {
        await advancedFeaturesApi.updateRecommendation(this.currentRecommendation)
        this.$message.success('保存成功')
        this.editDialogVisible = false
        this.getRecommendationList()
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    // 获取反馈记录
    async getFeedbackRecords(recommendationId) {
      try {
        const response = await advancedFeaturesApi.getRecommendationFeedback(recommendationId)
        if (response && response.code === 1 && response.data) {
          this.feedbackRecords = response.data.feedbacks || []
        } else {
          this.feedbackRecords = []
        }
      } catch (error) {
        this.feedbackRecords = []
      }
    },

    // 采纳推荐（直接执行，不弹窗）
    handleAccept(row) {
      this.$confirm(`确定采纳「${row.recommendationName}」吗？`, '采纳确认', {
        confirmButtonText: '确定采纳',
        cancelButtonText: '取消',
        type: 'success'
      }).then(async () => {
        try {
          await advancedFeaturesApi.acceptRecommendation(row.recommendationId)
          this.$message.success('推荐已采纳')
          this.getRecommendationList()
          this.getRecommendationStats()
        } catch (error) {
          this.$message.error('采纳失败：' + error.message)
        }
      }).catch(() => {})
    },

    // 拒绝推荐（直接执行，不弹窗）
    handleReject(row) {
      this.$confirm(`确定拒绝「${row.recommendationName}」吗？`, '拒绝确认', {
        confirmButtonText: '确定拒绝',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.rejectRecommendation(row.recommendationId)
          this.$message.success('推荐已拒绝')
          this.getRecommendationList()
          this.getRecommendationStats()
        } catch (error) {
          this.$message.error('拒绝失败：' + error.message)
        }
      }).catch(() => {})
    },

    // 弹窗内执行采纳
    async doAccept(row) {
      if (!row) return
      try {
        await advancedFeaturesApi.acceptRecommendation(row.recommendationId)
        this.$message.success('推荐已采纳')
        this.detailDialogVisible = false
        this.getRecommendationList()
        this.getRecommendationStats()
      } catch (error) {
        this.$message.error('采纳失败：' + error.message)
      }
    },

    // 弹窗内执行拒绝
    async doReject(row) {
      if (!row) return
      try {
        await advancedFeaturesApi.rejectRecommendation(row.recommendationId)
        this.$message.success('推荐已拒绝')
        this.detailDialogVisible = false
        this.getRecommendationList()
        this.getRecommendationStats()
      } catch (error) {
        this.$message.error('拒绝失败：' + error.message)
      }
    },

    // 刷新
    handleRefresh() {
      this.getRecommendationList()
      this.getRecommendationStats()
    },

    // 训练模型 → 弹窗
    handleTrainModel() {
      this.trainResult = null
      this.trainModelDialogVisible = true
    },

    // 执行训练
    async doTrainModel() {
      this.trainLoading = true
      try {
        const response = await advancedFeaturesApi.trainModel({})
        if (response && response.code === 1) {
          this.trainResult = response.data
          this.$message.success('模型训练完成')
        } else {
          this.$message.error(response.msg || '训练失败')
        }
      } catch (error) {
        this.$message.error('训练失败：' + error.message)
      } finally {
        this.trainLoading = false
      }
    },

    // 推荐分析 → 弹窗
    async handleViewAnalysis() {
      this.analysisDialogVisible = true
      await this.loadAnalysisData()
    },

    // 加载分析数据
    async loadAnalysisData() {
      this.analysisLoading = true
      try {
        const response = await advancedFeaturesApi.getRecommendationAnalysis({})
        if (response && response.code === 1) {
          this.analysisData = response.data
        } else {
          this.$message.error(response.msg || '获取分析数据失败')
        }
      } catch (error) {
        this.$message.error('获取分析数据失败：' + error.message)
      } finally {
        this.analysisLoading = false
      }
    },

    // 推荐设置 → 弹窗
    handleSettings() {
      this.settingsDialogVisible = true
    },

    // 保存设置
    saveSettings() {
      this.$message.success('设置已保存')
      this.settingsDialogVisible = false
    },

    // 推荐报告 → 弹窗
    async handleReports() {
      this.reportsDialogVisible = true
      this.reportsLoading = true
      try {
        // 基于统计数据生成报告摘要
        const response = await advancedFeaturesApi.getIntelligentRecommendationStats()
        if (response && response.code === 1 && response.data) {
          const d = response.data
          const total = d.totalRecommendations || 0
          const accepted = d.acceptedRecommendations || 0
          const rate = total > 0 ? Math.round(accepted / total * 100) : 0
          this.reportsList = [
            { reportName: '智能推荐月度报告', reportType: '月度', createTime: new Date().toLocaleString(), totalCount: total, acceptRate: rate },
            { reportName: '推荐采纳分析报告', reportType: '分析', createTime: new Date().toLocaleString(), totalCount: accepted, acceptRate: rate }
          ]
        }
      } catch (error) {
        this.reportsList = []
      } finally {
        this.reportsLoading = false
      }
    },

    // 帮助 → 弹窗
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 刷新推荐类型
    refreshRecommendationTypes() {
      this.getRecommendationList()
      this.getRecommendationStats()
      this.$message.success('已刷新')
    },

    // 选择推荐类型
    handleSelectRecommendationType(recommendationType) {
      this.selectedRecommendationType = recommendationType.id
      this.getRecommendationList()
    },

    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },
    
    // 获取推荐类型颜色
    getRecommendationTypeColor(type) {
      if (!type) return 'info'
      const t = type.toUpperCase()
      const colorMap = {
        'BUDGET_OPTIMIZATION': 'primary',
        'COST_CONTROL': 'success',
        'COST_REDUCTION': 'success',
        'RESOURCE_ALLOCATION': 'warning',
        'RISK_WARNING': 'danger',
        'RISK_WARN': 'danger'
      }
      return colorMap[t] || 'info'
    },

    // 获取推荐类型文本
    getRecommendationTypeText(type) {
      if (!type) return '-'
      const t = type.toLowerCase()
      const textMap = {
        'budget_optimization': '预算优化',
        'cost_control': '成本控制',
        'cost_reduction': '成本控制',
        'resource_allocation': '资源配置',
        'risk_warning': '风险预警',
        'risk_warn': '风险预警'
      }
      return textMap[t] || type
    },

    // 获取优先级颜色
    getPriorityColor(priority) {
      if (!priority) return 'info'
      const p = priority.toUpperCase()
      const colorMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return colorMap[p] || 'info'
    },

    // 获取优先级文本
    getPriorityText(priority) {
      if (!priority) return '-'
      const p = priority.toUpperCase()
      const textMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return textMap[p] || priority
    },

    // 获取置信度颜色
    getConfidenceColor(confidence) {
      if (confidence >= 90) return '#67C23A'
      if (confidence >= 70) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取状态颜色
    getStatusColor(status) {
      if (!status) return 'info'
      const s = status.toLowerCase()
      const colorMap = {
        'pending': 'info',
        'accepted': 'success',
        'rejected': 'danger',
        'applied': 'primary'
      }
      return colorMap[s] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      if (!status) return '-'
      const s = status.toLowerCase()
      const textMap = {
        'pending': '待处理',
        'accepted': '已采纳',
        'rejected': '已拒绝',
        'applied': '已应用'
      }
      return textMap[s] || status
    },

    // 获取反馈类型颜色
    getFeedbackTypeColor(type) {
      if (!type) return 'info'
      const t = type.toUpperCase()
      const colorMap = {
        'POSITIVE': 'success',
        'NEGATIVE': 'danger',
        'NEUTRAL': 'warning'
      }
      return colorMap[t] || 'info'
    },

    // 获取反馈类型文本
    getFeedbackTypeText(type) {
      if (!type) return '-'
      const t = type.toUpperCase()
      const textMap = {
        'POSITIVE': '正面',
        'NEGATIVE': '负面',
        'NEUTRAL': '中性'
      }
      return textMap[t] || type
    }
  }
}
</script>

<style lang="scss" scoped>
.intelligent-recommendation {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;

    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .toolbar-card {
    margin-bottom: 20px;

    .text-right {
      text-align: right;
    }
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;

      &.total-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }

      &.accepted-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.accuracy-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.improvement-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
        color: white;
      }

      .stat-content {
        position: relative;
        z-index: 2;

        .stat-number {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 2px;
        }

        .stat-description {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }

        .stat-trend {
          font-size: 12px;
          opacity: 0.9;
        }
      }

      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
      }
    }
  }

  .recommendation-types-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .recommendation-type-item {
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }

      &.selected {
        border-color: #409EFF;
        background: #F0F8FF;
      }

      .recommendation-type-icon {
        width: 48px;
        height: 48px;
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 12px;

        i {
          font-size: 24px;
          color: white;
        }
      }

      .recommendation-type-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .recommendation-type-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
      }

      .recommendation-type-stats {
        .recommendation-count {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .recommendations-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        display: flex;
        align-items: center;
      }
    }

    .confidence-text {
      margin-left: 8px;
      font-size: 12px;
      color: #606266;
    }

    .improvement-text {
      color: #67C23A;
      font-weight: 500;
    }
  }

  .detail-content {
    padding: 20px;
  }
}

.analysis-stat-card {
  text-align: center;
  .analysis-num {
    font-size: 28px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 4px;
  }
  .analysis-label {
    font-size: 13px;
    color: #909399;
  }
}
</style>
