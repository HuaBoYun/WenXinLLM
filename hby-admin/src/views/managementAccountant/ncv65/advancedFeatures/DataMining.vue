<template>
  <div class="data-mining">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>数据挖掘管理</h2>
      <p>预算数据挖掘和模式识别，基于机器学习算法发现数据中的隐藏模式和规律</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateMining">创建挖掘</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-cpu" @click="handleRunMining">执行挖掘</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewResults">挖掘结果</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">挖掘设置</el-button>
            <el-button icon="el-icon-document" @click="handleReports">挖掘报告</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据挖掘统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ miningStats.totalMiningTasks }}</div>
            <div class="stat-label">挖掘任务</div>
            <div class="stat-description">总挖掘任务数量</div>
            <div class="stat-trend">
              <i class="el-icon-data-analysis"></i>
              <span>智能挖掘</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-data-analysis"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card patterns-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ miningStats.discoveredPatterns }}</div>
            <div class="stat-label">发现模式</div>
            <div class="stat-description">识别的数据模式</div>
            <div class="stat-trend">
              <i class="el-icon-search"></i>
              <span>模式识别</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-search"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card accuracy-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ miningStats.accuracy }}%</div>
            <div class="stat-label">挖掘准确率</div>
            <div class="stat-description">算法挖掘准确度</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>高精度</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card insights-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ miningStats.actionableInsights }}</div>
            <div class="stat-label">可行洞察</div>
            <div class="stat-description">可执行的业务洞察</div>
            <div class="stat-trend">
              <i class="el-icon-magic-stick"></i>
              <span>业务价值</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-magic-stick"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 挖掘类型选择 -->
    <el-card class="mining-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>挖掘类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshMiningTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="miningType in miningTypes" :key="miningType.id">
          <el-card
            class="mining-type-item"
            shadow="hover"
            @click.native="handleSelectMiningType(miningType)"
            :class="{ 'selected': selectedMiningType === miningType.id }"
          >
            <div class="mining-type-icon">
              <i :class="miningType.icon"></i>
            </div>
            <div class="mining-type-title">{{ miningType.name }}</div>
            <div class="mining-type-description">{{ miningType.description }}</div>
            <div class="mining-type-stats">
              <span class="task-count">{{ miningType.taskCount }} 个任务</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据挖掘任务列表 -->
    <el-card class="mining-tasks-card" shadow="never">
      <div slot="header" class="card-header">
        <span>挖掘任务</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索任务"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getMiningTaskList"
            clearable
            @clear="getMiningTaskList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getMiningTaskList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="miningTaskList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="taskName" label="任务名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.taskName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="miningType" label="挖掘类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getMiningTypeColor(scope.row.miningType)" size="mini">
              {{ getMiningTypeText(scope.row.miningType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="algorithm" label="算法" width="120" align="center">
          <template slot-scope="scope">
            <el-tag type="info" size="mini">{{ getAlgorithmText(scope.row.algorithm) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataSource" label="数据量" width="100" align="center">
          <template slot-scope="scope">
            <span class="data-size">{{ getDataSourceText(scope.row.dataSource) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="taskStatus" label="执行进度" width="150" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="getProgressByStatus(scope.row.taskStatus)"
              :color="getProgressColor(getProgressByStatus(scope.row.taskStatus))"
              :stroke-width="6"
              :show-text="false"
            />
            <span class="progress-text">{{ getProgressByStatus(scope.row.taskStatus) }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="patternsFound" label="发现模式" width="100" align="center">
          <template slot-scope="scope">
            <span class="patterns-count">{{ scope.row.patternsFound != null ? scope.row.patternsFound : 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="taskStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.taskStatus)" size="mini">
              {{ getStatusText(scope.row.taskStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-video-play"
              @click.stop="handleRun(scope.row)"
              :disabled="scope.row.taskStatus === 'RUNNING'"
            >执行</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)" @click.stop.native>
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="stop">停止</el-dropdown-item>
                <el-dropdown-item command="export">导出</el-dropdown-item>
                <el-dropdown-item command="visualize">可视化</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 任务详情抽屉 -->
    <el-drawer
      title="挖掘任务详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentTask">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="任务基本信息" :column="2" border>
              <el-descriptions-item label="任务名称">{{ currentTask.taskName }}</el-descriptions-item>
              <el-descriptions-item label="挖掘类型">{{ getMiningTypeText(currentTask.miningType) }}</el-descriptions-item>
              <el-descriptions-item label="算法">{{ getAlgorithmText(currentTask.algorithm) }}</el-descriptions-item>
              <el-descriptions-item label="数据源">{{ getDataSourceText(currentTask.dataSource) }}</el-descriptions-item>
              <el-descriptions-item label="执行进度">{{ getProgressByStatus(currentTask.taskStatus) }}%</el-descriptions-item>
              <el-descriptions-item label="最小支持度">{{ currentTask.minSupport != null ? (currentTask.minSupport * 100).toFixed(0) + '%' : '-' }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentTask.taskStatus)" size="mini">
                  {{ getStatusText(currentTask.taskStatus) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentTask.createTime }}</el-descriptions-item>
              <el-descriptions-item label="任务描述" :span="2">{{ currentTask.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="算法配置" name="config">
            <el-form label-width="120px" size="small">
              <el-form-item label="数据源">
                <el-input :value="currentTask.dataSource" readonly />
              </el-form-item>
              <el-form-item label="特征选择">
                <el-input :value="currentTask.featureSelection" readonly type="textarea" :rows="2" />
              </el-form-item>
              <el-form-item label="算法参数">
                <el-input :value="currentTask.algorithmParameters" readonly type="textarea" :rows="3" />
              </el-form-item>
              <el-form-item label="评估指标">
                <el-input :value="currentTask.evaluationMetrics" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="挖掘结果" name="results">
            <el-table :data="miningResults" border size="mini" max-height="400">
              <el-table-column prop="patternId" label="模式ID" width="100" />
              <el-table-column prop="patternType" label="模式类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getPatternTypeColor(scope.row.patternType)" size="mini">
                    {{ getPatternTypeText(scope.row.patternType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="confidence" label="置信度" width="100" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.row.confidence }}%</span>
                </template>
              </el-table-column>
              <el-table-column prop="support" label="支持度" width="100" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.row.support }}%</span>
                </template>
              </el-table-column>
              <el-table-column prop="description" label="模式描述" />
              <el-table-column prop="businessValue" label="业务价值" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getValueColor(scope.row.businessValue)" size="mini">
                    {{ getValueText(scope.row.businessValue) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="执行日志" name="logs">
            <el-table :data="executionLogs" border size="mini">
              <el-table-column prop="logTime" label="时间" width="150" />
              <el-table-column prop="logLevel" label="级别" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getLogLevelColor(scope.row.logLevel)" size="mini">
                    {{ scope.row.logLevel }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="logMessage" label="日志信息" />
              <el-table-column prop="algorithm" label="算法" width="120" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑任务对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="taskForm"
        :model="taskForm"
        :rules="taskRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="任务名称" prop="taskName">
              <el-input v-model="taskForm.taskName" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="挖掘类型" prop="miningType">
              <el-select v-model="taskForm.miningType" placeholder="请选择挖掘类型" style="width: 100%">
                <el-option value="ASSOCIATION_RULES" label="关联规则" />
                <el-option value="CLUSTERING" label="聚类分析" />
                <el-option value="CLASSIFICATION" label="分类预测" />
                <el-option value="ANOMALY_DETECTION" label="异常检测" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="算法" prop="algorithm">
              <el-select v-model="taskForm.algorithm" placeholder="请选择算法" style="width: 100%">
                <el-option value="APRIORI" label="Apriori" />
                <el-option value="FP_GROWTH" label="FP-Growth" />
                <el-option value="K_MEANS" label="K-Means" />
                <el-option value="DBSCAN" label="DBSCAN" />
                <el-option value="RANDOM_FOREST" label="Random Forest" />
                <el-option value="SVM" label="SVM" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据源" prop="dataSource">
              <el-select v-model="taskForm.dataSource" placeholder="请选择数据源" style="width: 100%">
                <el-option value="BUDGET_DATA" label="预算数据" />
                <el-option value="ACTUAL_DATA" label="实际数据" />
                <el-option value="HISTORICAL_DATA" label="历史数据" />
                <el-option value="EXTERNAL_DATA" label="外部数据" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最小支持度" prop="minSupport">
              <el-input-number v-model="taskForm.minSupport" :min="0.01" :max="1" :step="0.01" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最小置信度" prop="minConfidence">
              <el-input-number v-model="taskForm.minConfidence" :min="0.01" :max="1" :step="0.01" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="特征选择" prop="featureSelection">
          <el-input
            v-model="taskForm.featureSelection"
            type="textarea"
            :rows="2"
            placeholder="请输入特征选择条件"
          />
        </el-form-item>
        <el-form-item label="算法参数" prop="algorithmParameters">
          <el-input
            v-model="taskForm.algorithmParameters"
            type="textarea"
            :rows="3"
            placeholder="请输入算法参数（JSON格式）"
          />
        </el-form-item>
        <el-form-item label="任务描述" prop="description">
          <el-input
            v-model="taskForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入任务描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 执行挖掘弹窗 -->
    <el-dialog title="执行挖掘" :visible.sync="runMiningDialogVisible" width="500px" :close-on-click-modal="false">
      <div style="padding: 10px 0;">
        <p style="margin-bottom:16px;color:#606266;">请从任务列表中选择任务后点击"执行"按钮，或在此处选择任务执行：</p>
        <el-select v-model="runMiningTaskId" placeholder="请选择要执行的任务" style="width:100%">
          <el-option
            v-for="item in miningTaskList"
            :key="item.taskId"
            :label="item.taskName"
            :value="item.taskId"
            :disabled="item.taskStatus === 'RUNNING'"
          />
        </el-select>
      </div>
      <div slot="footer">
        <el-button @click="runMiningDialogVisible = false">取消</el-button>
        <el-button type="primary" :disabled="!runMiningTaskId" @click="confirmRunMining">确定执行</el-button>
      </div>
    </el-dialog>

    <!-- 挖掘结果弹窗 -->
    <el-dialog title="挖掘结果" :visible.sync="resultsDialogVisible" width="700px">
      <el-select v-model="resultsTaskId" placeholder="请选择任务查看结果" style="width:100%;margin-bottom:16px" @change="loadResultsForTask">
        <el-option
          v-for="item in miningTaskList"
          :key="item.taskId"
          :label="item.taskName"
          :value="item.taskId"
        />
      </el-select>
      <el-table :data="dialogMiningResults" border size="mini" max-height="350">
        <el-table-column prop="patternId" label="模式ID" width="100" />
        <el-table-column prop="patternType" label="模式类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getPatternTypeColor(scope.row.patternType)" size="mini">{{ getPatternTypeText(scope.row.patternType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="confidence" label="置信度" width="90" align="center">
          <template slot-scope="scope"><span>{{ scope.row.confidence }}%</span></template>
        </el-table-column>
        <el-table-column prop="support" label="支持度" width="90" align="center">
          <template slot-scope="scope"><span>{{ scope.row.support }}%</span></template>
        </el-table-column>
        <el-table-column prop="description" label="模式描述" />
      </el-table>
      <div slot="footer">
        <el-button @click="resultsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 可视化弹窗 -->
    <el-dialog
      title="任务可视化"
      :visible.sync="visualizeDialogVisible"
      width="680px"
      :close-on-click-modal="false"
    >
      <div v-if="visualizeTask" class="visualize-content">
        <!-- 基本信息 -->
        <el-descriptions :column="2" border size="small" style="margin-bottom:20px">
          <el-descriptions-item label="任务名称">{{ visualizeTask.taskName }}</el-descriptions-item>
          <el-descriptions-item label="挖掘类型">
            <el-tag :type="getMiningTypeColor(visualizeTask.miningType)" size="mini">{{ getMiningTypeText(visualizeTask.miningType) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="算法">{{ getAlgorithmText(visualizeTask.algorithm) }}</el-descriptions-item>
          <el-descriptions-item label="数据源">{{ getDataSourceText(visualizeTask.dataSource) }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusColor(visualizeTask.taskStatus)" size="mini">{{ getStatusText(visualizeTask.taskStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ visualizeTask.createTime }}</el-descriptions-item>
        </el-descriptions>

        <!-- 参数指标 -->
        <div class="visualize-section-title">算法参数</div>
        <el-row :gutter="16" style="margin-bottom:20px">
          <el-col :span="12">
            <div class="visualize-metric-card">
              <div class="metric-label">最小支持度</div>
              <el-progress
                :percentage="visualizeTask.minSupport != null ? Math.round(visualizeTask.minSupport * 100) : 0"
                :color="'#409EFF'"
                :stroke-width="12"
              />
              <div class="metric-value">{{ visualizeTask.minSupport != null ? (visualizeTask.minSupport * 100).toFixed(0) + '%' : '-' }}</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="visualize-metric-card">
              <div class="metric-label">最小置信度</div>
              <el-progress
                :percentage="visualizeTask.minConfidence != null ? Math.round(visualizeTask.minConfidence * 100) : 0"
                :color="'#67C23A'"
                :stroke-width="12"
              />
              <div class="metric-value">{{ visualizeTask.minConfidence != null ? (visualizeTask.minConfidence * 100).toFixed(0) + '%' : '-' }}</div>
            </div>
          </el-col>
        </el-row>

        <!-- 执行进度 -->
        <div class="visualize-section-title">执行状态</div>
        <div class="visualize-metric-card" style="margin-bottom:16px">
          <div class="metric-label">执行进度</div>
          <el-progress
            :percentage="getProgressByStatus(visualizeTask.taskStatus)"
            :color="getProgressColor(getProgressByStatus(visualizeTask.taskStatus))"
            :stroke-width="14"
            :format="p => p + '%'"
          />
        </div>

        <!-- 描述 -->
        <div v-if="visualizeTask.description" class="visualize-section-title">任务描述</div>
        <div v-if="visualizeTask.description" class="visualize-desc">{{ visualizeTask.description }}</div>
      </div>
      <div slot="footer">
        <el-button @click="visualizeDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'

export default {
  name: 'DataMining',
  data() {
    return {
      // 统计数据
      miningStats: {
        totalMiningTasks: 0,
        discoveredPatterns: 0,
        accuracy: 0,
        actionableInsights: 0
      },

      // 挖掘类型
      miningTypes: [
        { id: 1, name: '关联规则', description: '发现数据项之间的关联', icon: 'el-icon-connection', taskCount: 0 },
        { id: 2, name: '聚类分析', description: '数据分组和模式识别', icon: 'el-icon-s-grid', taskCount: 0 },
        { id: 3, name: '分类预测', description: '基于历史数据预测分类', icon: 'el-icon-data-analysis', taskCount: 0 },
        { id: 4, name: '异常检测', description: '识别异常数据和模式', icon: 'el-icon-warning', taskCount: 0 }
      ],
      selectedMiningType: null,

      // 任务列表
      miningTaskList: [],
      loading: false,
      searchKeyword: '',

      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentTask: null,
      miningResults: [],
      executionLogs: [],

      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,

      // 执行挖掘弹窗
      runMiningDialogVisible: false,
      runMiningTaskId: '',

      // 挖掘结果弹窗
      resultsDialogVisible: false,
      resultsTaskId: '',
      dialogMiningResults: [],

      // 可视化弹窗
      visualizeDialogVisible: false,
      visualizeTask: null,

      // 表单数据
      taskForm: {
        taskName: '',
        miningType: '',
        algorithm: '',
        dataSource: '',
        minSupport: 0.1,
        minConfidence: 0.8,
        featureSelection: '',
        algorithmParameters: '',
        description: ''
      },

      // 表单验证规则
      taskRules: {
        taskName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        miningType: [
          { required: true, message: '请选择挖掘类型', trigger: 'change' }
        ],
        algorithm: [
          { required: true, message: '请选择算法', trigger: 'change' }
        ],
        dataSource: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ]
      }
    }
  },

  created() {
    this.getMiningTaskList()
    this.getMiningStats()
  },

  methods: {
    // 获取任务列表
    async getMiningTaskList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword }
        if (this.selectedMiningType) {
          params.miningType = this.selectedMiningType
        }
        const response = await advancedFeaturesApi.getDataMiningTaskList(params)
        if (response && response.code === 1) {
          // 后端返回分页结构 { list, total }，取 list 字段
          const data = response.data
          this.miningTaskList = (data && data.list) ? data.list : (Array.isArray(data) ? data : [])
          // 统计各挖掘类型任务数，更新卡片显示
          const typeKeyMap = { 1: 'ASSOCIATION_RULES', 2: 'CLUSTERING', 3: 'CLASSIFICATION', 4: 'ANOMALY_DETECTION' }
          this.miningTypes = this.miningTypes.map(t => ({
            ...t,
            taskCount: this.miningTaskList.filter(item => item.miningType === typeKeyMap[t.id]).length
          }))
        }
      } catch (error) {
        this.$message.error('获取任务列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getMiningStats() {
      try {
        const response = await advancedFeaturesApi.getDataMiningStats()
        if (response && response.code === 1 && response.data) {
          const d = response.data
          // 兼容后端字段名（totalTasks）与前端展示字段名（totalMiningTasks）
          this.miningStats = {
            totalMiningTasks: d.totalTasks != null ? d.totalTasks : (d.totalMiningTasks || 0),
            discoveredPatterns: d.discoveredPatterns || 0,
            accuracy: d.accuracy != null ? d.accuracy : 0,
            actionableInsights: d.completedTasks != null ? d.completedTasks : (d.actionableInsights || 0)
          }
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    // 创建挖掘
    handleCreateMining() {
      this.dialogTitle = '创建数据挖掘任务'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑任务
    handleEdit(row) {
      this.dialogTitle = '编辑数据挖掘任务'
      this.dialogVisible = true
      this.taskForm = { ...row }
    },

    // 查看详情
    async handleView(row) {
      this.currentTask = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await this.getMiningResults(row.taskId)
      await this.getExecutionLogs(row.taskId)
    },

    // 获取挖掘结果
    async getMiningResults(taskId) {
      try {
        const response = await advancedFeaturesApi.getDataMiningResults(taskId)
        this.miningResults = response.data
      } catch (error) {
        console.error('获取挖掘结果失败：', error)
      }
    },

    // 获取执行日志
    async getExecutionLogs(taskId) {
      try {
        const response = await advancedFeaturesApi.getDataMiningLogs(taskId)
        this.executionLogs = response.data
      } catch (error) {
        console.error('获取执行日志失败：', error)
      }
    },

    // 执行挖掘
    async handleRun(row) {
      this.$confirm('确定执行该数据挖掘任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.runDataMining(row.taskId)
          this.$message.success('挖掘任务已启动')
          this.getMiningTaskList()
        } catch (error) {
          this.$message.error('执行失败：' + error.message)
        }
      })
    },

    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'stop':
          this.handleStopMining(row)
          break
        case 'export':
          this.handleExportMining(row)
          break
        case 'visualize':
          this.handleVisualizeMining(row)
          break
        case 'copy':
          this.handleCopyMining(row)
          break
        case 'delete':
          this.handleDeleteMining(row)
          break
      }
    },

    // 停止挖掘
    async handleStopMining(row) {
      try {
        await advancedFeaturesApi.stopDataMining(row.taskId)
        this.$message.success('挖掘任务已停止')
        this.getMiningTaskList()
      } catch (error) {
        this.$message.error('停止失败：' + error.message)
      }
    },

    // 导出挖掘 - 将任务数据导出为 JSON 文件
    handleExportMining(row) {
      try {
        const exportData = {
          taskId: row.taskId,
          taskName: row.taskName,
          miningType: this.getMiningTypeText(row.miningType),
          algorithm: this.getAlgorithmText(row.algorithm),
          dataSource: this.getDataSourceText(row.dataSource),
          minSupport: row.minSupport,
          minConfidence: row.minConfidence,
          taskStatus: this.getStatusText(row.taskStatus),
          description: row.description,
          createTime: row.createTime,
          exportTime: new Date().toLocaleString()
        }
        const json = JSON.stringify(exportData, null, 2)
        const blob = new Blob([json], { type: 'application/json;charset=utf-8' })
        const url = URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `数据挖掘任务_${row.taskName || row.taskId}_${Date.now()}.json`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 可视化挖掘 - 改为弹窗
    handleVisualizeMining(row) {
      this.visualizeTask = row
      this.visualizeDialogVisible = true
    },

    // 复制挖掘
    async handleCopyMining(row) {
      try {
        await advancedFeaturesApi.copyDataMining(row.taskId)
        this.$message.success('复制成功')
        this.getMiningTaskList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 删除挖掘
    handleDeleteMining(row) {
      this.$confirm('确定删除该数据挖掘任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteDataMining(row.taskId)
          this.$message.success('删除成功')
          this.getMiningTaskList()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 提交表单
    async handleSubmitForm() {
      this.$refs.taskForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            if (this.taskForm.id) {
              await advancedFeaturesApi.updateDataMining(this.taskForm)
              this.$message.success('更新成功')
            } else {
              await advancedFeaturesApi.createDataMining(this.taskForm)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getMiningTaskList()
          } catch (error) {
            this.$message.error('操作失败：' + error.message)
          } finally {
            this.submitLoading = false
          }
        }
      })
    },

    // 重置表单
    resetForm() {
      this.taskForm = {
        taskName: '',
        miningType: '',
        algorithm: '',
        dataSource: '',
        minSupport: 0.1,
        minConfidence: 0.8,
        featureSelection: '',
        algorithmParameters: '',
        description: ''
      }
      this.$nextTick(() => {
        this.$refs.taskForm && this.$refs.taskForm.clearValidate()
      })
    },

    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },

    // 刷新
    handleRefresh() {
      this.getMiningTaskList()
      this.getMiningStats()
    },

    // 执行挖掘 - 改为弹窗
    handleRunMining() {
      this.runMiningTaskId = ''
      this.runMiningDialogVisible = true
    },

    // 确认执行挖掘
    async confirmRunMining() {
      if (!this.runMiningTaskId) return
      try {
        await advancedFeaturesApi.runDataMining(this.runMiningTaskId)
        this.$message.success('挖掘任务已启动')
        this.runMiningDialogVisible = false
        this.getMiningTaskList()
      } catch (error) {
        this.$message.error('执行失败：' + error.message)
      }
    },

    // 查看结果 - 改为弹窗
    handleViewResults() {
      this.resultsTaskId = ''
      this.dialogMiningResults = []
      this.resultsDialogVisible = true
    },

    // 弹窗中加载指定任务的挖掘结果
    async loadResultsForTask(taskId) {
      if (!taskId) return
      try {
        const response = await advancedFeaturesApi.getDataMiningResults(taskId)
        if (response && response.data) {
          const d = response.data
          this.dialogMiningResults = Array.isArray(d) ? d : (d.data || [])
        } else {
          this.dialogMiningResults = []
        }
      } catch (error) {
        this.dialogMiningResults = []
        console.error('获取挖掘结果失败：', error)
      }
    },

    // 挖掘设置
    handleSettings() {
      this.$router.push('/managementAccountant/ncv65/advancedFeatures/miningSettings')
    },

    // 挖掘报告
    handleReports() {
      this.$router.push('/managementAccountant/ncv65/advancedFeatures/miningReports')
    },

    // 帮助
    handleHelp() {
      this.$router.push('/managementAccountant/ncv65/advancedFeatures/miningHelp')
    },

    // 刷新挖掘类型
    refreshMiningTypes() {
      this.getMiningTaskList()
      this.getMiningStats()
      this.$message.success('已刷新')
    },

    // 选择挖掘类型
    handleSelectMiningType(miningType) {
      this.selectedMiningType = miningType.id
      this.getMiningTaskList()
    },

    // 格式化数据大小
    formatDataSize(size) {
      if (size < 1024) return size + ' B'
      if (size < 1024 * 1024) return (size / 1024).toFixed(1) + ' KB'
      if (size < 1024 * 1024 * 1024) return (size / (1024 * 1024)).toFixed(1) + ' MB'
      return (size / (1024 * 1024 * 1024)).toFixed(1) + ' GB'
    },

    // 获取挖掘类型颜色
    getMiningTypeColor(type) {
      const colorMap = {
        'ASSOCIATION_RULES': 'primary',
        'CLUSTERING': 'success',
        'CLASSIFICATION': 'warning',
        'ANOMALY_DETECTION': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 获取挖掘类型文本
    getMiningTypeText(type) {
      const textMap = {
        'ASSOCIATION_RULES': '关联规则',
        'CLUSTERING': '聚类分析',
        'CLASSIFICATION': '分类预测',
        'ANOMALY_DETECTION': '异常检测'
      }
      return textMap[type] || type
    },

    // 获取算法中文名
    getAlgorithmText(algorithm) {
      const textMap = {
        'APRIORI': 'Apriori关联',
        'FP_GROWTH': 'FP-Growth',
        'K_MEANS': 'K-Means聚类',
        'DBSCAN': 'DBSCAN密度',
        'RANDOM_FOREST': '随机森林',
        'SVM': '支持向量机'
      }
      return textMap[algorithm] || algorithm || '-'
    },

    // 获取数据源中文名（用于数据量列展示）
    getDataSourceText(dataSource) {
      const textMap = {
        'BUDGET_DATA': '预算数据',
        'ACTUAL_DATA': '实际数据',
        'HISTORICAL_DATA': '历史数据',
        'EXTERNAL_DATA': '外部数据'
      }
      return textMap[dataSource] || dataSource || '-'
    },

    // 根据任务状态推算进度百分比
    getProgressByStatus(status) {
      const progressMap = {
        'PENDING': 0,
        'RUNNING': 50,
        'COMPLETED': 100,
        'FAILED': 0,
        'STOPPED': 30
      }
      return progressMap[status] != null ? progressMap[status] : 0
    },

    // 获取模式类型颜色
    getPatternTypeColor(type) {
      const colorMap = {
        'FREQUENT': 'success',
        'SEQUENTIAL': 'primary',
        'CLUSTER': 'warning',
        'ANOMALY': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 获取模式类型文本
    getPatternTypeText(type) {
      const textMap = {
        'FREQUENT': '频繁模式',
        'SEQUENTIAL': '序列模式',
        'CLUSTER': '聚类模式',
        'ANOMALY': '异常模式'
      }
      return textMap[type] || type
    },

    // 获取业务价值颜色
    getValueColor(value) {
      const colorMap = {
        'HIGH': 'success',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return colorMap[value] || 'info'
    },

    // 获取业务价值文本
    getValueText(value) {
      const textMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return textMap[value] || value
    },

    // 获取进度颜色
    getProgressColor(progress) {
      if (progress >= 90) return '#67C23A'
      if (progress >= 60) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'RUNNING': 'primary',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'STOPPED': 'warning',
        'PENDING': 'info'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'RUNNING': '运行中',
        'COMPLETED': '已完成',
        'FAILED': '失败',
        'STOPPED': '已停止',
        'PENDING': '待执行'
      }
      return textMap[status] || status
    },

    // 获取日志级别颜色
    getLogLevelColor(level) {
      const colorMap = {
        'INFO': 'primary',
        'WARN': 'warning',
        'ERROR': 'danger',
        'DEBUG': 'info'
      }
      return colorMap[level] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.data-mining {
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

      &.patterns-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.accuracy-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.insights-card {
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

  .mining-types-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .mining-type-item {
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

      .mining-type-icon {
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

      .mining-type-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .mining-type-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
      }

      .mining-type-stats {
        .task-count {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .mining-tasks-card {
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

    .data-size {
      color: #409EFF;
      font-weight: 500;
    }

    .patterns-count {
      color: #67C23A;
      font-weight: 500;
    }

    .progress-text {
      margin-left: 8px;
      font-size: 12px;
      color: #606266;
    }
  }

  .detail-content {
    padding: 20px;
  }

  .dialog-footer {
    text-align: right;
  }

  .visualize-content {
    padding: 4px 0;

    .visualize-section-title {
      font-size: 13px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 12px;
      padding-left: 8px;
      border-left: 3px solid #409EFF;
    }

    .visualize-metric-card {
      background: #f5f7fa;
      border-radius: 6px;
      padding: 12px 16px;

      .metric-label {
        font-size: 12px;
        color: #909399;
        margin-bottom: 8px;
      }

      .metric-value {
        font-size: 13px;
        font-weight: 600;
        color: #303133;
        margin-top: 6px;
        text-align: right;
      }
    }

    .visualize-desc {
      background: #f5f7fa;
      border-radius: 6px;
      padding: 12px 16px;
      font-size: 13px;
      color: #606266;
      line-height: 1.6;
    }
  }
}
</style>