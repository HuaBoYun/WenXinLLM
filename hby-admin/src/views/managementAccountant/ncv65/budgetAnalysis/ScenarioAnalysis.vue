<template>
  <div class="scenario-analysis">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算场景分析</h2>
      <p>基于不同假设条件的多场景预算分析，提供What-if分析和敏感性测试</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateScenario">创建场景</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefreshScenarios">刷新场景</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExportScenarios">导出场景</el-button>
            <el-button type="info" icon="el-icon-data-analysis" @click="handleCompareScenarios">场景对比</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleScenarioSettings">场景设置</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 场景分析概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card scenarios-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ scenarioStats.totalScenarios }}</div>
            <div class="stat-label">总场景数</div>
            <div class="stat-description">已创建的分析场景</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>较上期增长25%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-data-analysis"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ scenarioStats.activeScenarios }}</div>
            <div class="stat-label">活跃场景</div>
            <div class="stat-description">当前运行中的场景</div>
            <div class="stat-trend">
              <i class="el-icon-video-play"></i>
              <span>实时分析中</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-video-play"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card variables-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ scenarioStats.totalVariables }}</div>
            <div class="stat-label">分析变量</div>
            <div class="stat-description">场景分析变量总数</div>
            <div class="stat-trend">
              <i class="el-icon-s-data"></i>
              <span>多维度分析</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-data"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card accuracy-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ scenarioStats.accuracy }}%</div>
            <div class="stat-label">分析准确率</div>
            <div class="stat-description">场景分析准确率</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>准确率稳定</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 场景配置区域 -->
    <el-card class="config-card" shadow="never">
      <div class="config-header">
        <span class="config-title">场景配置</span>
        <el-button type="text" @click="handleResetConfig">重置配置</el-button>
      </div>
      <el-form :model="scenarioForm" :inline="true" size="small">
        <el-form-item label="场景类型">
          <el-select
            v-model="scenarioForm.scenarioType"
            placeholder="请选择场景类型"
            style="width: 150px"
          >
            <el-option value="OPTIMISTIC" label="乐观场景" />
            <el-option value="PESSIMISTIC" label="悲观场景" />
            <el-option value="REALISTIC" label="现实场景" />
            <el-option value="STRESS_TEST" label="压力测试" />
            <el-option value="SENSITIVITY" label="敏感性分析" />
            <el-option value="CUSTOM" label="自定义场景" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析期间">
          <el-date-picker
            v-model="scenarioForm.analysisPeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="组织单元">
          <el-cascader
            v-model="scenarioForm.organizationPath"
            :options="organizationOptions"
            :props="{ checkStrictly: true, emitPath: false }"
            placeholder="请选择组织单元"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="分析变量">
          <el-select
            v-model="scenarioForm.analysisVariables"
            placeholder="请选择分析变量"
            multiple
            style="width: 200px"
          >
            <el-option value="REVENUE" label="收入" />
            <el-option value="COST" label="成本" />
            <el-option value="MARKET_SHARE" label="市场份额" />
            <el-option value="INFLATION_RATE" label="通胀率" />
            <el-option value="EXCHANGE_RATE" label="汇率" />
            <el-option value="INTEREST_RATE" label="利率" />
          </el-select>
        </el-form-item>
        <el-form-item label="置信水平">
          <el-select
            v-model="scenarioForm.confidenceLevel"
            placeholder="请选择置信水平"
            style="width: 120px"
          >
            <el-option value="90" label="90%" />
            <el-option value="95" label="95%" />
            <el-option value="99" label="99%" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleAnalyzeScenarios">分析场景</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 场景变量设置 -->
    <el-card class="variables-card" shadow="never">
      <div slot="header" class="card-header">
        <span>场景变量设置</span>
        <div class="header-tools">
          <el-button icon="el-icon-plus" size="mini" @click="handleAddVariable">添加变量</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="handleRefreshVariables">刷新</el-button>
        </div>
      </div>
      <el-table
        :data="scenarioVariables"
        border
        size="mini"
        style="width: 100%"
      >
        <el-table-column prop="variableName" label="变量名称" width="150" />
        <el-table-column prop="variableType" label="变量类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getVariableTypeColor(scope.row.variableType)" size="mini">
              {{ getVariableTypeText(scope.row.variableType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="baseValue" label="基准值" width="100" align="right">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.baseValue"
              :precision="2"
              size="mini"
              style="width: 100%"
              @change="handleVariableChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="optimisticValue" label="乐观值" width="100" align="right">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.optimisticValue"
              :precision="2"
              size="mini"
              style="width: 100%"
              @change="handleVariableChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="pessimisticValue" label="悲观值" width="100" align="right">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.pessimisticValue"
              :precision="2"
              size="mini"
              style="width: 100%"
              @change="handleVariableChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="distribution" label="分布类型" width="120">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.distribution"
              size="mini"
              style="width: 100%"
              @change="handleVariableChange(scope.row)"
            >
              <el-option value="NORMAL" label="正态分布" />
              <el-option value="UNIFORM" label="均匀分布" />
              <el-option value="TRIANGULAR" label="三角分布" />
              <el-option value="BETA" label="贝塔分布" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="correlation" label="相关性" width="100" align="center">
          <template slot-scope="scope">
            <el-rate
              v-model="scope.row.correlation"
              :max="5"
              size="mini"
              @change="handleVariableChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEditVariable(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-delete"
              @click="handleDeleteVariable(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 场景分析图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>场景对比分析</span>
            <div class="header-tools">
              <el-radio-group v-model="chartType" size="mini">
                <el-radio-button label="bar">柱状图</el-radio-button>
                <el-radio-button label="line">折线图</el-radio-button>
                <el-radio-button label="radar">雷达图</el-radio-button>
                <el-radio-button label="tornado">龙卷风图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="scenarioComparisonChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>敏感性分析</span>
            <div class="header-tools">
              <el-radio-group v-model="sensitivityChartType" size="mini">
                <el-radio-button label="spider">蜘蛛图</el-radio-button>
                <el-radio-button label="waterfall">瀑布图</el-radio-button>
                <el-radio-button label="heatmap">热力图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="sensitivityChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 概率分析 -->
    <el-row :gutter="20" class="probability-row">
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>概率分布</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshProbabilityChart" />
          </div>
          <div id="probabilityChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>累积概率</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshCumulativeChart" />
          </div>
          <div id="cumulativeChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="risk-card" shadow="never">
          <div slot="header" class="card-header">
            <span>风险指标</span>
            <el-button icon="el-icon-edit" size="mini" @click="handleEditRiskMetrics" />
          </div>
          <div class="risk-metrics">
            <div class="risk-item">
              <div class="risk-label">VaR (95%)</div>
              <div class="risk-value">{{ riskMetrics.var95 }}万元</div>
            </div>
            <div class="risk-item">
              <div class="risk-label">CVaR (95%)</div>
              <div class="risk-value">{{ riskMetrics.cvar95 }}万元</div>
            </div>
            <div class="risk-item">
              <div class="risk-label">标准差</div>
              <div class="risk-value">{{ riskMetrics.standardDeviation }}万元</div>
            </div>
            <div class="risk-item">
              <div class="risk-label">偏度</div>
              <div class="risk-value">{{ riskMetrics.skewness }}</div>
            </div>
            <div class="risk-item">
              <div class="risk-label">峰度</div>
              <div class="risk-value">{{ riskMetrics.kurtosis }}</div>
            </div>
            <div class="risk-item">
              <div class="risk-label">风险等级</div>
              <div class="risk-value">
                <el-tag :type="getRiskLevelColor(riskMetrics.riskLevel)" size="mini">
                  {{ getRiskLevelText(riskMetrics.riskLevel) }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 场景分析结果表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">场景分析结果</span>
        <div class="table-tools">
          <el-tooltip content="显示概率分布" placement="top">
            <el-switch
              v-model="showProbability"
              active-text="概率分布"
              @change="handleShowProbabilityChange"
            />
          </el-tooltip>
          <el-tooltip content="显示风险指标" placement="top">
            <el-switch
              v-model="showRiskMetrics"
              active-text="风险指标"
              @change="handleShowRiskMetricsChange"
            />
          </el-tooltip>
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getScenarioResults" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="scenarioResults"
        border
        stripe
        highlight-current-row
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column prop="scenarioName" label="场景名称" width="150" show-overflow-tooltip />
        <el-table-column prop="scenarioType" label="场景类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getScenarioTypeColor(scope.row.scenarioType)" size="mini">
              {{ getScenarioTypeText(scope.row.scenarioType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="baselineValue" label="基准值" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.baselineValue) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="scenarioValue" label="场景值" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="scenario-value">{{ formatAmount(scope.row.scenarioValue) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="variance" label="差异" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span :class="getVarianceClass(scope.row.variance)">
              {{ formatVariance(scope.row.variance) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column prop="varianceRate" label="差异率" width="100" align="right" sortable="custom">
          <template slot-scope="scope">
            <span :class="getVarianceRateClass(scope.row.varianceRate)">
              {{ formatVarianceRate(scope.row.varianceRate) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column prop="probability" label="概率" width="100" align="center" v-if="showProbability">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.probability"
              :stroke-width="6"
              :color="getProbabilityColor(scope.row.probability)"
            />
          </template>
        </el-table-column>

        <el-table-column prop="riskLevel" label="风险等级" width="100" align="center" v-if="showRiskMetrics">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelColor(scope.row.riskLevel)" size="mini">
              {{ getRiskLevelText(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="confidence" label="置信度" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <span :class="getConfidenceClass(scope.row.confidence)">{{ scope.row.confidence }}%</span>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">
              <i :class="getStatusIcon(scope.row.status)"></i>
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createDate" label="创建日期" width="120" align="center" />

        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEditScenario(scope.row)"
            >编辑</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy" icon="el-icon-copy-document">复制场景</el-dropdown-item>
                <el-dropdown-item command="sensitivity" icon="el-icon-data-analysis">敏感性分析</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
                <el-dropdown-item command="delete" icon="el-icon-delete">删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          :total="total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 创建场景对话框 -->
    <el-dialog
      title="创建场景"
      :visible.sync="createScenarioDialogVisible"
      width="800px"
      @close="handleCloseCreateDialog"
    >
      <el-form :model="createScenarioForm" :rules="createScenarioRules" ref="createScenarioForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="场景名称" prop="scenarioName">
              <el-input v-model="createScenarioForm.scenarioName" placeholder="请输入场景名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="场景类型" prop="scenarioType">
              <el-select v-model="createScenarioForm.scenarioType" placeholder="请选择场景类型" style="width: 100%">
                <el-option value="OPTIMISTIC" label="乐观场景" />
                <el-option value="PESSIMISTIC" label="悲观场景" />
                <el-option value="REALISTIC" label="现实场景" />
                <el-option value="STRESS_TEST" label="压力测试" />
                <el-option value="SENSITIVITY" label="敏感性分析" />
                <el-option value="CUSTOM" label="自定义场景" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分析期间" prop="analysisPeriod">
              <el-date-picker
                v-model="createScenarioForm.analysisPeriod"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="置信水平" prop="confidenceLevel">
              <el-select v-model="createScenarioForm.confidenceLevel" placeholder="请选择置信水平" style="width: 100%">
                <el-option value="90" label="90%" />
                <el-option value="95" label="95%" />
                <el-option value="99" label="99%" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="场景描述" prop="description">
          <el-input
            v-model="createScenarioForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入场景描述"
          />
        </el-form-item>
        <el-form-item label="分析变量" prop="analysisVariables">
          <el-select
            v-model="createScenarioForm.analysisVariables"
            multiple
            placeholder="请选择分析变量"
            style="width: 100%"
          >
            <el-option value="REVENUE" label="收入" />
            <el-option value="COST" label="成本" />
            <el-option value="MARKET_SHARE" label="市场份额" />
            <el-option value="INFLATION_RATE" label="通胀率" />
            <el-option value="EXCHANGE_RATE" label="汇率" />
            <el-option value="INTEREST_RATE" label="利率" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createScenarioDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmCreateScenario" :loading="creatingScenario">
          {{ creatingScenario ? '创建中...' : '确定创建' }}
        </el-button>
      </div>
    </el-dialog>

    <!-- 编辑场景对话框 -->
    <el-dialog
      title="编辑场景"
      :visible.sync="editScenarioDialogVisible"
      width="600px"
    >
      <el-form :model="editScenarioForm" :rules="editScenarioRules" ref="editScenarioFormRef" label-width="100px">
        <el-form-item label="场景名称" prop="scenarioName">
          <el-input v-model="editScenarioForm.scenarioName" placeholder="请输入场景名称" />
        </el-form-item>
        <el-form-item label="场景类型" prop="scenarioType">
          <el-select v-model="editScenarioForm.scenarioType" placeholder="请选择场景类型" style="width: 100%">
            <el-option value="OPTIMISTIC" label="乐观场景" />
            <el-option value="PESSIMISTIC" label="悲观场景" />
            <el-option value="REALISTIC" label="现实场景" />
            <el-option value="STRESS_TEST" label="压力测试" />
            <el-option value="SENSITIVITY" label="敏感性分析" />
            <el-option value="CUSTOM" label="自定义场景" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析状态">
          <el-select v-model="editScenarioForm.analysisStatus" placeholder="请选择状态" style="width: 100%">
            <el-option value="ACTIVE" label="活跃" />
            <el-option value="RUNNING" label="运行中" />
            <el-option value="COMPLETED" label="已完成" />
            <el-option value="FAILED" label="失败" />
          </el-select>
        </el-form-item>
        <el-form-item label="场景描述">
          <el-input
            v-model="editScenarioForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入场景描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editScenarioDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmEditScenario" :loading="editingScenario">
          {{ editingScenario ? '保存中...' : '保存' }}
        </el-button>
      </div>
    </el-dialog>

    <!-- 添加/编辑变量弹窗 -->
    <el-dialog
      :title="variableDialogTitle"
      :visible.sync="variableDialogVisible"
      width="700px"
      @close="handleCloseVariableDialog"
    >
      <el-form :model="variableForm" :rules="variableRules" ref="variableFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="变量名称" prop="variableName">
              <el-input v-model="variableForm.variableName" placeholder="请输入变量名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变量类型" prop="variableType">
              <el-select v-model="variableForm.variableType" placeholder="请选择变量类型" style="width:100%">
                <el-option value="REVENUE" label="收入" />
                <el-option value="COST" label="成本" />
                <el-option value="MARKET_SHARE" label="市场份额" />
                <el-option value="INFLATION_RATE" label="通胀率" />
                <el-option value="EXCHANGE_RATE" label="汇率" />
                <el-option value="INTEREST_RATE" label="利率" />
                <el-option value="CUSTOM" label="自定义" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="基准值" prop="baseValue">
              <el-input-number v-model="variableForm.baseValue" :precision="4" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="乐观值">
              <el-input-number v-model="variableForm.optimisticValue" :precision="4" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="悲观值">
              <el-input-number v-model="variableForm.pessimisticValue" :precision="4" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分布类型">
              <el-select v-model="variableForm.distribution" placeholder="请选择分布类型" style="width:100%">
                <el-option value="NORMAL" label="正态分布" />
                <el-option value="UNIFORM" label="均匀分布" />
                <el-option value="TRIANGULAR" label="三角分布" />
                <el-option value="BETA" label="Beta分布" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="相关性">
              <el-input-number v-model="variableForm.correlation" :min="-1" :max="1" :precision="4" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述">
          <el-input v-model="variableForm.description" type="textarea" :rows="2" placeholder="请输入变量描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="variableDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmVariable" :loading="savingVariable">
          {{ savingVariable ? '保存中...' : '保存' }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { budgetAnalysisApi } from '@/api/managementAccountant/ncv65/budgetAnalysis'

export default {
  name: 'ScenarioAnalysis',
  data() {
    return {
      createDialogVisible: false,
      settingsDialogVisible: false,
      helpDialogVisible: false,
      // 场景筛选表单（用户主动查询时才传给后端）
      scenarioForm: {
        scenarioType: '',
        analysisPeriod: [],
        organizationPath: '',
        analysisVariables: ['REVENUE', 'COST'],
        confidenceLevel: '95'
      },
      // 当前生效的筛选条件（点击"分析场景"后才更新）
      activeFilters: {},
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },

      // 表格数据
      loading: false,
      scenarioResults: [],
      total: 0,

      // 图表类型
      cachedChartData: null,
      chartType: 'bar',
      sensitivityChartType: 'spider',

      // 控制开关
      showProbability: true,
      showRiskMetrics: true,

      // 统计数据
      scenarioStats: {
        totalScenarios: 0,
        activeScenarios: 0,
        totalVariables: 0,
        accuracy: 0
      },

      // 场景变量
      scenarioVariables: [],

      // 风险指标
      riskMetrics: {
        var95: 0,
        cvar95: 0,
        standardDeviation: 0,
        skewness: 0,
        kurtosis: 0,
        riskLevel: ''
      },

      // 创建场景对话框
      createScenarioDialogVisible: false,
      creatingScenario: false,
      createScenarioForm: {
        scenarioName: '',
        scenarioType: '',
        analysisPeriod: [],
        confidenceLevel: '95',
        description: '',
        analysisVariables: []
      },
      createScenarioRules: {
        scenarioName: [
          { required: true, message: '请输入场景名称', trigger: 'blur' }
        ],
        scenarioType: [
          { required: true, message: '请选择场景类型', trigger: 'change' }
        ],
        analysisPeriod: [
          { required: true, message: '请选择分析期间', trigger: 'change' }
        ]
      },

      // 编辑场景对话框
      editScenarioDialogVisible: false,
      editingScenario: false,
      editScenarioForm: {
        id: '',
        scenarioName: '',
        scenarioType: '',
        description: '',
        analysisStatus: ''
      },
      editScenarioRules: {
        scenarioName: [
          { required: true, message: '请输入场景名称', trigger: 'blur' }
        ],
        scenarioType: [
          { required: true, message: '请选择场景类型', trigger: 'change' }
        ]
      },

      // 选项数据
      organizationOptions: [],

      // 变量弹窗
      variableDialogVisible: false,
      variableDialogTitle: '添加变量',
      savingVariable: false,
      editingVariableId: null,
      variableForm: {
        variableName: '',
        variableType: 'CUSTOM',
        baseValue: 0,
        optimisticValue: 0,
        pessimisticValue: 0,
        distribution: 'NORMAL',
        correlation: 0,
        description: ''
      },
      variableRules: {
        variableName: [{ required: true, message: '请输入变量名称', trigger: 'blur' }],
        variableType: [{ required: true, message: '请选择变量类型', trigger: 'change' }],
        baseValue: [{ required: true, message: '请输入基准值', trigger: 'blur' }]
      }
    }
  },

  created() {
    this.getScenarioResults()
    this.loadOrganizationOptions()
    this.loadScenarioStats()
    this.loadVariableList()
    this.initCharts()
  },

  watch: {
    chartType() {
      if (this.cachedChartData && this.cachedChartData.comparisonChart) {
        this.renderScenarioComparisonChart(this.cachedChartData.comparisonChart)
      }
    },
    sensitivityChartType() {
      if (this.cachedChartData && this.cachedChartData.sensitivityChart) {
        this.renderSensitivityChart(this.cachedChartData.sensitivityChart)
      }
    }
  },

  beforeDestroy() {
    const ids = ['scenarioComparisonChart', 'sensitivityChart', 'probabilityChart', 'cumulativeChart']
    ids.forEach(id => {
      const dom = document.getElementById(id)
      if (dom) {
        const inst = echarts.getInstanceByDom(dom)
        if (inst) inst.dispose()
      }
    })
  },

  methods: {
    // 获取场景结果
    async getScenarioResults() {
      this.loading = true
      try {
        // 只传已生效的筛选条件 + 分页参数，不合并 scenarioForm 避免默认值干扰
        const params = {
          ...this.activeFilters,
          ...this.queryParams
        }
        const response = await budgetAnalysisApi.getScenarioAnalysis(params)
        const rawList = response.data.tlist || response.data.list || []
        // 字段映射：后端字段 → 前端字段
        this.scenarioResults = rawList.map(item => ({
          ...item,
          scenarioName: item.analysisName || item.scenarioName || '',
          baselineValue: item.baseAmount != null ? item.baseAmount : item.baselineValue,
          scenarioValue: item.scenarioAmount != null ? item.scenarioAmount : item.scenarioValue,
          variance: item.differenceAmount != null ? item.differenceAmount : item.variance,
          varianceRate: item.differenceRate != null ? item.differenceRate : item.varianceRate,
          status: item.analysisStatus || item.status || '',
          createDate: item.createTime ? (typeof item.createTime === 'string' ? item.createTime.substring(0, 10) : new Date(item.createTime).toISOString().substring(0, 10)) : item.createDate || '',
          riskLevel: item.riskAssessment || item.riskLevel || '',
          confidence: item.probability != null ? item.probability : (item.confidence != null ? item.confidence : 0)
        }))
        this.total = response.data.totalRecord || response.data.total || 0
      } catch (error) {
        this.$message.error('获取场景分析结果失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 加载组织选项
    async loadOrganizationOptions() {
      try {
        const response = await budgetAnalysisApi.getOrganizations()
        this.organizationOptions = response.data
      } catch (error) {
        console.error('加载组织选项失败：', error)
      }
    },

    // 初始化图表 —— 直接加载数据并渲染
    initCharts() {
      this.$nextTick(() => {
        this.loadScenarioChartData()
      })
    },

    // 加载统计数据
    async loadScenarioStats() {
      try {
        const response = await budgetAnalysisApi.getScenarioStats()
        if (response.code === 1 && response.data) {
          this.scenarioStats = { ...this.scenarioStats, ...response.data }
          // 不再从 stats 取 variables，由 loadVariableList 单独加载
          if (response.data.riskMetrics) this.riskMetrics = { ...this.riskMetrics, ...response.data.riskMetrics }
        }
      } catch (error) {
        console.error('加载场景分析统计数据失败：', error)
      }
    },

    // 从后端加载场景变量列表
    async loadVariableList() {
      try {
        const response = await budgetAnalysisApi.getVariableList({ pageNum: 1, pageSize: 100 })
        if (response.code === 1 && response.data) {
          this.scenarioVariables = response.data.list || response.data.tlist || []
        }
      } catch (error) {
        console.error('加载场景变量列表失败：', error)
      }
    },

    // 加载图表数据（缓存 + 渲染）
    async loadScenarioChartData() {
      try {
        const response = await budgetAnalysisApi.getScenarioChartData(this.scenarioForm)
        if (response.code === 1 && response.data) {
          this.cachedChartData = response.data
          const d = response.data
          if (d.comparisonChart) this.renderScenarioComparisonChart(d.comparisonChart)
          if (d.sensitivityChart) this.renderSensitivityChart(d.sensitivityChart)
          if (d.probabilityChart) this.renderProbabilityChart(d.probabilityChart)
          if (d.cumulativeChart) this.renderCumulativeChart(d.cumulativeChart)
        }
      } catch (error) {
        console.error('加载场景分析图表数据失败：', error)
      }
    },

    /**
     * 渲染场景对比图表 —— bar / line / radar / tornado
     */
    renderScenarioComparisonChart(cd) {
      const dom = document.getElementById('scenarioComparisonChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const xData = cd.xAxis || []
      const seriesRaw = cd.series || []
      const legendData = cd.legend || seriesRaw.map(s => s.name)
      const colors = ['#67C23A', '#409EFF', '#F56C6C', '#E6A23C']
      let option = {}

      if (this.chartType === 'bar' || this.chartType === 'line') {
        option = {
          title: { text: this.chartType === 'bar' ? '场景对比分析' : '场景趋势对比', left: 'center' },
          tooltip: { trigger: 'axis', axisPointer: { type: this.chartType === 'bar' ? 'shadow' : 'cross' } },
          legend: { data: legendData, top: 30 },
          grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
          xAxis: { type: 'category', data: xData },
          yAxis: { type: 'value', name: '金额(万元)' },
          series: seriesRaw.map((s, i) => ({
            name: s.name,
            type: this.chartType,
            data: s.data || [],
            smooth: this.chartType === 'line',
            itemStyle: { color: colors[i % colors.length] }
          }))
        }
      } else if (this.chartType === 'radar') {
        const indicators = xData.map(name => {
          const maxVal = Math.max(...seriesRaw.map(s => Math.max(...(s.data || []).map(Number).filter(v => !isNaN(v)), 0)))
          return { name, max: Math.ceil(maxVal * 1.2) || 100 }
        })
        option = {
          title: { text: '场景雷达图', left: 'center' },
          tooltip: {},
          legend: { data: legendData, top: 30 },
          radar: { indicator: indicators },
          series: [{
            name: '场景对比',
            type: 'radar',
            data: seriesRaw.map((s, i) => ({
              value: s.data || [],
              name: s.name,
              itemStyle: { color: colors[i % colors.length] }
            }))
          }]
        }
      } else if (this.chartType === 'tornado') {
        // 龙卷风图：用第一个和最后一个 series 做正负对比
        const positive = (seriesRaw[0] || {}).data || []
        const negative = (seriesRaw[seriesRaw.length - 1] || seriesRaw[0] || {}).data || []
        option = {
          title: { text: '龙卷风图', left: 'center' },
          tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
          grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
          xAxis: { type: 'value' },
          yAxis: { type: 'category', data: xData },
          series: [
            { name: '负向影响', type: 'bar', stack: 'total', data: negative.map(v => -(Number(v) || 0)), itemStyle: { color: '#F56C6C' } },
            { name: '正向影响', type: 'bar', stack: 'total', data: positive.map(v => Number(v) || 0), itemStyle: { color: '#67C23A' } }
          ]
        }
      }
      chart.setOption(option, true)
      chart.resize()
    },

    /**
     * 渲染敏感性图表 —— spider / waterfall / heatmap
     */
    renderSensitivityChart(sd) {
      const dom = document.getElementById('sensitivityChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const xData = sd.xAxis || []
      const seriesRaw = sd.series || []
      const legendData = sd.legend || seriesRaw.map(s => s.name)
      let option = {}

      if (this.sensitivityChartType === 'spider') {
        option = {
          title: { text: '敏感性蜘蛛图', left: 'center' },
          tooltip: { trigger: 'axis' },
          legend: { data: legendData, top: 30 },
          xAxis: { type: 'category', data: xData },
          yAxis: { type: 'value', name: '影响程度(%)' },
          series: seriesRaw.map((s, i) => ({
            name: s.name,
            type: 'line',
            data: s.data || [],
            itemStyle: { color: i === 0 ? '#F56C6C' : '#67C23A' }
          }))
        }
      } else if (this.sensitivityChartType === 'waterfall') {
        const rawData = (seriesRaw[0] || {}).data || []
        const helperData = []
        const stepData = []
        let cumulative = 0
        rawData.forEach((v, i) => {
          const val = Number(v) || 0
          if (i === 0) { helperData.push(0); stepData.push(val); cumulative = val }
          else {
            const diff = val - (Number(rawData[i - 1]) || 0)
            helperData.push(diff >= 0 ? cumulative : cumulative + diff)
            stepData.push(Math.abs(diff))
            cumulative = val
          }
        })
        option = {
          title: { text: '敏感性瀑布图', left: 'center' },
          tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
          xAxis: { type: 'category', data: xData },
          yAxis: { type: 'value', name: '影响程度(%)' },
          series: [
            { name: '辅助', type: 'bar', stack: 'total', data: helperData, itemStyle: { color: 'transparent' }, emphasis: { itemStyle: { color: 'transparent' } } },
            { name: '变化量', type: 'bar', stack: 'total', data: stepData, itemStyle: { color: '#409EFF' } }
          ]
        }
      } else if (this.sensitivityChartType === 'heatmap') {
        const yLabels = seriesRaw.map(s => s.name)
        const heatData = []
        seriesRaw.forEach((s, yi) => {
          ;(s.data || []).forEach((v, xi) => {
            heatData.push([xi, yi, v != null ? Number(v) : 0])
          })
        })
        option = {
          title: { text: '敏感性热力图', left: 'center' },
          tooltip: { position: 'top' },
          grid: { height: '50%', top: '10%' },
          xAxis: { type: 'category', data: xData, splitArea: { show: true } },
          yAxis: { type: 'category', data: yLabels, splitArea: { show: true } },
          visualMap: { min: -20, max: 20, calculable: true, orient: 'horizontal', left: 'center', bottom: '15%' },
          series: [{ name: '敏感性', type: 'heatmap', data: heatData, label: { show: true } }]
        }
      }
      chart.setOption(option, true)
      chart.resize()
    },

    /**
     * 渲染概率分布图
     */
    renderProbabilityChart(pd) {
      const dom = document.getElementById('probabilityChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const option = {
        title: { text: '概率分布', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: pd.xAxis || [] },
        yAxis: { type: 'value', name: '概率密度' },
        series: [{
          name: '概率分布',
          type: 'line',
          data: pd.data || [],
          itemStyle: { color: '#409EFF' },
          smooth: true,
          areaStyle: { opacity: 0.3 }
        }]
      }
      chart.setOption(option, true)
      chart.resize()
    },

    /**
     * 渲染累积概率图
     */
    renderCumulativeChart(cd) {
      const dom = document.getElementById('cumulativeChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const option = {
        title: { text: '累积概率', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: cd.xAxis || [] },
        yAxis: { type: 'value', name: '累积概率', max: 1 },
        series: [{
          name: '累积概率',
          type: 'line',
          data: cd.data || [],
          itemStyle: { color: '#67C23A' },
          smooth: true
        }]
      }
      chart.setOption(option, true)
      chart.resize()
    },

    // 分析场景（用户点击"分析场景"按钮时才应用筛选条件）
    handleAnalyzeScenarios() {
      // 将当前 scenarioForm 中非空的字段提取为生效筛选条件
      const filters = {}
      if (this.scenarioForm.scenarioType) filters.scenarioType = this.scenarioForm.scenarioType
      if (this.scenarioForm.analysisPeriod && this.scenarioForm.analysisPeriod.length) filters.analysisPeriod = this.scenarioForm.analysisPeriod
      if (this.scenarioForm.organizationPath) filters.organizationId = this.scenarioForm.organizationPath
      this.activeFilters = filters
      this.queryParams.pageNum = 1
      this.getScenarioResults()
      this.initCharts()
    },

    // 重置
    handleReset() {
      this.scenarioForm = {
        scenarioType: '',
        analysisPeriod: [],
        organizationPath: '',
        analysisVariables: ['REVENUE', 'COST'],
        confidenceLevel: '95'
      }
      this.activeFilters = {}
      this.queryParams.pageNum = 1
      this.getScenarioResults()
    },

    // 重置配置
    handleResetConfig() {
      this.handleReset()
    },

    // 创建场景
    handleCreateScenario() {
      this.createScenarioDialogVisible = true
    },

    // 确认创建场景
    async handleConfirmCreateScenario() {
      this.$refs.createScenarioForm.validate(async (valid) => {
        if (valid) {
          this.creatingScenario = true
          try {
            await budgetAnalysisApi.createScenario(this.createScenarioForm)
            this.$message.success('场景创建成功')
            this.createScenarioDialogVisible = false
            this.getScenarioResults()
          } catch (error) {
            this.$message.error('场景创建失败：' + error.message)
          } finally {
            this.creatingScenario = false
          }
        }
      })
    },

    // 关闭创建对话框
    handleCloseCreateDialog() {
      this.$refs.createScenarioForm.resetFields()
    },

    // 刷新场景
    handleRefreshScenarios() {
      this.getScenarioResults()
      this.initCharts()
    },

    // 导出场景
    async handleExportScenarios() {
      try {
        const params = { ...this.scenarioForm }
        await budgetAnalysisApi.exportScenarioAnalysis(params)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 场景对比
    handleCompareScenarios() {
      this.$message.info('对比功能开发中')
    },

    // 场景设置
    handleScenarioSettings() {
      this.settingsDialogVisible = true
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 添加变量（打开弹窗）
    handleAddVariable() {
      this.variableDialogTitle = '添加变量'
      this.editingVariableId = null
      this.variableForm = {
        variableName: '',
        variableType: 'CUSTOM',
        baseValue: 0,
        optimisticValue: 0,
        pessimisticValue: 0,
        distribution: 'NORMAL',
        correlation: 0,
        description: ''
      }
      this.variableDialogVisible = true
    },

    // 刷新变量
    handleRefreshVariables() {
      this.loadVariableList()
      this.$message.success('变量已刷新')
    },

    // 变量改变
    handleVariableChange(variable) {
      console.log('变量更新:', variable)
    },

    // 编辑变量（打开弹窗）
    handleEditVariable(variable) {
      this.variableDialogTitle = '编辑变量'
      this.editingVariableId = variable.id
      this.variableForm = {
        variableName: variable.variableName || '',
        variableType: variable.variableType || 'CUSTOM',
        baseValue: variable.baseValue != null ? Number(variable.baseValue) : 0,
        optimisticValue: variable.optimisticValue != null ? Number(variable.optimisticValue) : 0,
        pessimisticValue: variable.pessimisticValue != null ? Number(variable.pessimisticValue) : 0,
        distribution: variable.distribution || 'NORMAL',
        correlation: variable.correlation != null ? Number(variable.correlation) : 0,
        description: variable.description || ''
      }
      this.variableDialogVisible = true
    },

    // 关闭变量弹窗
    handleCloseVariableDialog() {
      this.$refs.variableFormRef && this.$refs.variableFormRef.resetFields()
      this.editingVariableId = null
    },

    // 确认保存变量
    async handleConfirmVariable() {
      this.$refs.variableFormRef.validate(async (valid) => {
        if (!valid) return
        this.savingVariable = true
        try {
          if (this.editingVariableId) {
            await budgetAnalysisApi.updateVariable({ ...this.variableForm, id: this.editingVariableId })
            this.$message.success('变量更新成功')
          } else {
            await budgetAnalysisApi.createVariable(this.variableForm)
            this.$message.success('变量添加成功')
          }
          this.variableDialogVisible = false
          this.loadVariableList()
        } catch (error) {
          this.$message.error('保存失败：' + error.message)
        } finally {
          this.savingVariable = false
        }
      })
    },

    // 删除变量（调用后端接口）
    handleDeleteVariable(variable) {
      this.$confirm('确定删除该变量吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await budgetAnalysisApi.deleteVariable(variable.id)
          this.$message.success('删除成功')
          this.loadVariableList()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 刷新概率图表
    refreshProbabilityChart() {
      if (this.cachedChartData && this.cachedChartData.probabilityChart) {
        this.renderProbabilityChart(this.cachedChartData.probabilityChart)
      }
    },

    // 刷新累积图表
    refreshCumulativeChart() {
      if (this.cachedChartData && this.cachedChartData.cumulativeChart) {
        this.renderCumulativeChart(this.cachedChartData.cumulativeChart)
      }
    },

    // 编辑风险指标（从后端重新加载）
    handleEditRiskMetrics() {
      this.loadScenarioStats()
      this.$message.success('风险指标已刷新')
    },

    // 显示概率分布切换
    handleShowProbabilityChange(value) {
      this.$message.info(value ? '已显示概率分布' : '已隐藏概率分布')
    },

    // 显示风险指标切换
    handleShowRiskMetricsChange(value) {
      this.$message.info(value ? '已显示风险指标' : '已隐藏风险指标')
    },

    // 查看详情（已去除弹窗，改为打开编辑弹窗）
    handleViewDetail(row) {
      this.handleEditScenario(row)
    },

    // 编辑场景
    handleEditScenario(row) {
      this.editScenarioForm = {
        id: row.id,
        scenarioName: row.scenarioName || row.analysisName || '',
        scenarioType: row.scenarioType || '',
        description: row.scenarioDescription || row.description || '',
        analysisStatus: row.status || row.analysisStatus || ''
      }
      this.editScenarioDialogVisible = true
    },

    // 确认编辑场景
    async handleConfirmEditScenario() {
      this.$refs.editScenarioFormRef.validate(async (valid) => {
        if (!valid) return
        this.editingScenario = true
        try {
          const res = await budgetAnalysisApi.updateScenario(this.editScenarioForm)
          if (res.code === 1) {
            this.$message.success('场景更新成功')
            this.editScenarioDialogVisible = false
            this.getScenarioResults()
          } else {
            this.$message.error(res.msg || '更新失败')
          }
        } catch (error) {
          this.$message.error('场景更新失败：' + error.message)
        } finally {
          this.editingScenario = false
        }
      })
    },

    // 敏感性分析（打开编辑弹窗查看场景信息）
    handleSensitivityAnalysis(row) {
      this.handleEditScenario(row)
    },
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopyScenario(row)
          break
        case 'sensitivity':
          this.handleSensitivityAnalysis(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
        case 'delete':
          this.handleDeleteScenario(row)
          break
      }
    },

    // 复制场景
    async handleCopyScenario(row) {
      try {
        await budgetAnalysisApi.copyScenario(row.id)
        this.$message.success('场景复制成功')
        this.getScenarioResults()
      } catch (error) {
        this.$message.error('场景复制失败：' + error.message)
      }
    },

    // 敏感性分析（打开编辑弹窗查看场景信息）
    handleSensitivityAnalysis(row) {
      this.handleEditScenario(row)
    },

    // 导出单个
    async handleExportSingle(row) {
      try {
        await budgetAnalysisApi.exportSingleScenario(row.id)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 删除场景
    handleDeleteScenario(row) {
      this.$confirm('确定删除该场景吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await budgetAnalysisApi.deleteScenario(row.id)
          this.$message.success('删除成功')
          this.getScenarioResults()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 行点击（不再弹出详情，保留方法避免模板报错）
    handleRowClick(row) {
      // 行点击不做操作，通过操作列按钮进行编辑
    },

    // 排序改变
    handleSortChange({ column, prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getScenarioResults()
    },

    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getScenarioResults()
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getScenarioResults()
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 格式化差异
    formatVariance(variance) {
      if (!variance) return '0.00'
      const formatted = this.formatAmount(Math.abs(variance))
      return variance >= 0 ? `+${formatted}` : `-${formatted}`
    },

    // 格式化差异率
    formatVarianceRate(rate) {
      if (!rate) return '0.00%'
      const formatted = `${parseFloat(Math.abs(rate)).toFixed(2)}%`
      return rate >= 0 ? `+${formatted}` : `-${formatted}`
    },

    // 获取变量类型颜色
    getVariableTypeColor(type) {
      const colorMap = {
        'REVENUE': 'success',
        'COST': 'warning',
        'MARKET_SHARE': 'primary',
        'INFLATION_RATE': 'info',
        'EXCHANGE_RATE': 'danger',
        'INTEREST_RATE': 'success'
      }
      return colorMap[type] || 'info'
    },

    // 获取变量类型文本
    getVariableTypeText(type) {
      const textMap = {
        'REVENUE': '收入',
        'COST': '成本',
        'MARKET_SHARE': '市场份额',
        'INFLATION_RATE': '通胀率',
        'EXCHANGE_RATE': '汇率',
        'INTEREST_RATE': '利率'
      }
      return textMap[type] || type
    },

    // 获取场景类型颜色
    getScenarioTypeColor(type) {
      const colorMap = {
        'OPTIMISTIC': 'success',
        'PESSIMISTIC': 'danger',
        'REALISTIC': 'primary',
        'STRESS_TEST': 'warning',
        'SENSITIVITY': 'info',
        'CUSTOM': 'success'
      }
      return colorMap[type] || 'info'
    },

    // 获取场景类型文本
    getScenarioTypeText(type) {
      const textMap = {
        'OPTIMISTIC': '乐观场景',
        'PESSIMISTIC': '悲观场景',
        'REALISTIC': '现实场景',
        'STRESS_TEST': '压力测试',
        'SENSITIVITY': '敏感性分析',
        'CUSTOM': '自定义场景'
      }
      return textMap[type] || type
    },

    // 获取差异样式类
    getVarianceClass(variance) {
      if (variance > 0) return 'positive-variance'
      if (variance < 0) return 'negative-variance'
      return 'zero-variance'
    },

    // 获取差异率样式类
    getVarianceRateClass(rate) {
      if (rate > 0) return 'positive-rate'
      if (rate < 0) return 'negative-rate'
      return 'zero-rate'
    },

    // 获取概率颜色
    getProbabilityColor(probability) {
      if (probability >= 80) return '#67C23A'
      if (probability >= 60) return '#409EFF'
      if (probability >= 40) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取风险等级颜色
    getRiskLevelColor(level) {
      const colorMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return colorMap[level] || 'info'
    },

    // 获取风险等级文本
    getRiskLevelText(level) {
      const textMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '极高风险'
      }
      return textMap[level] || level
    },

    // 获取置信度样式类
    getConfidenceClass(confidence) {
      if (confidence >= 95) return 'high-confidence'
      if (confidence >= 90) return 'medium-confidence'
      return 'low-confidence'
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'RUNNING': 'warning',
        'COMPLETED': 'primary',
        'FAILED': 'danger'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态图标
    getStatusIcon(status) {
      const iconMap = {
        'ACTIVE': 'el-icon-check',
        'RUNNING': 'el-icon-loading',
        'COMPLETED': 'el-icon-success',
        'FAILED': 'el-icon-close'
      }
      return iconMap[status] || 'el-icon-minus'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'RUNNING': '运行中',
        'COMPLETED': '已完成',
        'FAILED': '失败'
      }
      return textMap[status] || status
    },

    // 获取影响程度样式类
    getImpactClass(impact) {
      if (Math.abs(impact) >= 20) return 'high-impact'
      if (Math.abs(impact) >= 10) return 'medium-impact'
      return 'low-impact'
    }
  }
}
</script>

<style lang="scss" scoped>
.scenario-analysis {
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

  .toolbar-card,
  .config-card,
  .variables-card,
  .table-card {
    margin-bottom: 20px;
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;

      &.scenarios-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }

      &.active-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.variables-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.accuracy-card {
        background: linear-gradient(135deg, #909399, #B3B6BC);
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

          .trend-up {
            color: #F56C6C;
          }
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

  .config-card,
  .variables-card {
    .config-header,
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .config-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
      }

      .header-tools {
        display: flex;
        align-items: center;
        gap: 10px;
      }
    }
  }

  .chart-row,
  .probability-row {
    margin-bottom: 20px;

    .chart-card,
    .risk-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .header-tools {
          display: flex;
          align-items: center;
          gap: 10px;
        }
      }

      .chart-container {
        height: 300px;
      }
    }

    .risk-card {
      .risk-metrics {
        .risk-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 12px 0;
          border-bottom: 1px solid #EBEEF5;

          &:last-child {
            border-bottom: none;
          }

          .risk-label {
            font-size: 14px;
            color: #606266;
          }

          .risk-value {
            font-size: 14px;
            color: #303133;
            font-weight: 500;
          }
        }
      }
    }
  }

  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    .table-title {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }

    .table-tools {
      display: flex;
      align-items: center;
      gap: 12px;
    }
  }

  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #409EFF;
  }

  .scenario-value {
    font-family: 'Courier New', monospace;
    font-weight: 600;
    color: #67C23A;
  }

  .positive-variance,
  .positive-rate {
    color: #67C23A;
    font-weight: 500;
  }

  .negative-variance,
  .negative-rate {
    color: #F56C6C;
    font-weight: 500;
  }

  .zero-variance,
  .zero-rate {
    color: #909399;
    font-weight: 500;
  }

  .high-confidence { color: #67C23A; font-weight: 500; }
  .medium-confidence { color: #409EFF; font-weight: 500; }
  .low-confidence { color: #F56C6C; font-weight: 500; }

  .high-impact { color: #F56C6C; font-weight: 600; }
  .medium-impact { color: #E6A23C; font-weight: 500; }
  .low-impact { color: #67C23A; font-weight: 500; }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }

  .text-right {
    text-align: right;
  }

  .detail-content {
    padding: 20px;

    .variables-analysis,
    .risk-assessment {
      margin-top: 20px;
    }
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>