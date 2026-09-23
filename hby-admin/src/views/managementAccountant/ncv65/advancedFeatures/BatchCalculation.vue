<template>
  <div class="batch-calculation">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>批量计算管理</h2>
      <p>预算数据批量计算和公式执行，支持大规模数据的高效计算处理</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateTask">创建任务</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-video-play" @click="handleExecuteTask">执行计算</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewResults">计算结果</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">计算设置</el-button>
            <el-button icon="el-icon-document" @click="handleReports">计算报告</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 批量计算统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ calculationStats.totalTasks }}</div>
            <div class="stat-label">计算任务</div>
            <div class="stat-description">总计算任务数量</div>
            <div class="stat-trend">
              <i class="el-icon-cpu"></i>
              <span>高效计算</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-cpu"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card running-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ calculationStats.runningTasks }}</div>
            <div class="stat-label">运行任务</div>
            <div class="stat-description">正在执行的任务</div>
            <div class="stat-trend">
              <i class="el-icon-loading"></i>
              <span>执行中</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-loading"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card performance-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ calculationStats.avgExecutionTime }}s</div>
            <div class="stat-label">平均耗时</div>
            <div class="stat-description">任务平均执行时间</div>
            <div class="stat-trend">
              <i class="el-icon-timer"></i>
              <span>高性能</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-timer"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card success-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ calculationStats.successRate }}%</div>
            <div class="stat-label">成功率</div>
            <div class="stat-description">计算任务成功率</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>高可靠</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 计算类型选择 -->
    <el-card class="calculation-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>计算类型</span>
        <el-button v-if="selectedCalculationType" type="text" size="mini" @click="selectedCalculationType = null; getCalculationTaskList()">清除筛选</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshCalculationTypes">刷新</el-button>
      </div>
      <div v-if="calculationTypes.length === 0" class="empty-types">
        <i class="el-icon-loading"></i> 加载中...
      </div>
      <el-row :gutter="12" v-else>
        <el-col :span="4" v-for="calculationType in calculationTypes" :key="calculationType.id" style="margin-bottom: 12px;">
          <el-card
            class="calculation-type-item"
            shadow="hover"
            @click.native="handleSelectCalculationType(calculationType)"
            :class="{ 'selected': selectedCalculationType === calculationType.id }"
          >
            <div class="calculation-type-icon">
              <i :class="calculationType.icon"></i>
            </div>
            <div class="calculation-type-title">{{ calculationType.name }}</div>
            <div class="calculation-type-stats">
              <span class="task-count">{{ calculationType.taskCount }} 个任务</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 批量计算任务列表 -->
    <el-card class="calculation-tasks-card" shadow="never">
      <div slot="header" class="card-header">
        <span>计算任务</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索任务"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getCalculationTaskList"
            clearable
            @clear="getCalculationTaskList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getCalculationTaskList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="calculationTaskList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="calculationName" label="任务名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.calculationName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="calculationType" label="计算类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getCalculationTypeColor(scope.row.calculationType)" size="mini">
              {{ getCalculationTypeText(scope.row.calculationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalItems" label="数据量" width="100" align="center">
          <template slot-scope="scope">
            <span class="data-count">{{ formatNumber(scope.row.totalItems || 0) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="completedItems" label="执行进度" width="150" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="getProgress(scope.row)"
              :color="getProgressColor(getProgress(scope.row))"
              :stroke-width="6"
              :show-text="false"
            />
            <span class="progress-text">{{ getProgress(scope.row) }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="executionTime" label="执行时间" width="120" align="center">
          <template slot-scope="scope">
            <span class="execution-time">{{ scope.row.executionTime ? (scope.row.executionTime / 1000).toFixed(1) : 0 }}s</span>
          </template>
        </el-table-column>
        <el-table-column prop="calculationStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.calculationStatus)" size="mini">
              {{ getStatusText(scope.row.calculationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建人" width="100" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-video-play"
              @click="handleExecute(scope.row)"
              :disabled="scope.row.calculationStatus === 'RUNNING' || scope.row.calculationStatus === 'IN_PROGRESS'"
            >执行</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="stop">停止</el-dropdown-item>
                <el-dropdown-item command="retry">重试</el-dropdown-item>
                <el-dropdown-item command="export">导出</el-dropdown-item>
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
      title="计算任务详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentTask">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="任务基本信息" :column="2" border>
              <el-descriptions-item label="任务名称">{{ currentTask.calculationName }}</el-descriptions-item>
              <el-descriptions-item label="计算类型">{{ getCalculationTypeText(currentTask.calculationType) }}</el-descriptions-item>
              <el-descriptions-item label="数据量">{{ formatNumber(currentTask.totalItems || 0) }}</el-descriptions-item>
              <el-descriptions-item label="执行进度">{{ getProgress(currentTask) }}%</el-descriptions-item>
              <el-descriptions-item label="执行时间">{{ currentTask.executionTime ? (currentTask.executionTime / 1000).toFixed(1) : 0 }}s</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentTask.calculationStatus)" size="mini">
                  {{ getStatusText(currentTask.calculationStatus) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentTask.createBy }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentTask.createTime }}</el-descriptions-item>
              <el-descriptions-item label="任务描述" :span="2">{{ currentTask.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="计算配置" name="config">
            <el-form label-width="120px" size="small">
              <el-form-item label="数据源">
                <el-input :value="currentTask.dataSource" readonly />
              </el-form-item>
              <el-form-item label="计算公式">
                <el-input :value="currentTask.calculationFormula" readonly type="textarea" :rows="3" />
              </el-form-item>
              <el-form-item label="并发数">
                <el-input :value="currentTask.concurrency" readonly />
              </el-form-item>
              <el-form-item label="批次大小">
                <el-input :value="currentTask.batchSize" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="执行结果" name="results">
            <el-table :data="calculationResults" border size="mini" max-height="400">
              <el-table-column prop="batchId" label="批次ID" width="100" />
              <el-table-column prop="processedCount" label="处理数量" width="120" align="center" />
              <el-table-column prop="successCount" label="成功数量" width="120" align="center" />
              <el-table-column prop="failureCount" label="失败数量" width="120" align="center" />
              <el-table-column prop="executionTime" label="执行时间" width="120" align="center" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusColor(scope.row.status)" size="mini">
                    {{ getStatusText(scope.row.status) }}
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
              <el-table-column prop="batchId" label="批次ID" width="100" />
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
            <el-form-item label="任务名称" prop="calculationName">
              <el-input v-model="taskForm.calculationName" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计算编码" prop="calculationCode">
              <el-input v-model="taskForm.calculationCode" placeholder="请输入计算编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计算类型" prop="calculationType">
              <el-select v-model="taskForm.calculationType" placeholder="请选择计算类型" style="width: 100%" filterable>
                <el-option value="FORMULA" label="公式计算" />
                <el-option value="AGGREGATION" label="聚合计算" />
                <el-option value="ALLOCATION" label="分摊计算" />
                <el-option value="CONVERSION" label="转换计算" />
                <el-option value="DEPRECIATION" label="折旧计算" />
                <el-option value="CURRENCY" label="币种换算" />
                <el-option value="PERFORMANCE" label="绩效计算" />
                <el-option value="VARIANCE" label="差异分析" />
                <el-option value="FORECAST" label="预测计算" />
                <el-option value="CARRY_FORWARD" label="结转计算" />
                <el-option value="COMPARISON" label="对比分析" />
                <el-option value="CONSOLIDATION" label="合并计算" />
                <el-option value="REPORT" label="报表计算" />
                <el-option value="SENSITIVITY" label="敏感性分析" />
                <el-option value="SIMULATION" label="模拟计算" />
                <el-option value="VALIDATION" label="数据校验" />
                <el-option value="INITIALIZATION" label="数据初始化" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据源" prop="dataSource">
              <el-select v-model="taskForm.dataSource" placeholder="请选择数据源" style="width: 100%">
                <el-option value="BUDGET_DB" label="预算数据库" />
                <el-option value="FINANCE_DB" label="财务数据库" />
                <el-option value="ASSET_DB" label="资产数据库" />
                <el-option value="HR_DB" label="人力资源数据库" />
                <el-option value="REPORT_DB" label="报表数据库" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="并发数" prop="concurrency">
              <el-input-number v-model="taskForm.concurrency" :min="1" :max="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="批次大小" prop="batchSize">
              <el-input-number v-model="taskForm.batchSize" :min="100" :max="10000" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="超时时间" prop="timeout">
              <el-input-number v-model="taskForm.timeout" :min="60" :max="3600" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="计算公式" prop="calculationFormula">
          <el-input
            v-model="taskForm.calculationFormula"
            type="textarea"
            :rows="4"
            placeholder="请输入计算公式"
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

    <!-- 执行计算弹窗 -->
    <el-dialog title="执行计算" :visible.sync="executeDialogVisible" width="600px">
      <div v-if="calculationTaskList.length > 0">
        <p style="margin-bottom: 15px; color: #606266;">请选择要执行的计算任务：</p>
        <el-table :data="calculationTaskList.filter(t => t.calculationStatus !== 'RUNNING' && t.calculationStatus !== 'IN_PROGRESS')" border size="mini" max-height="300" @row-click="handleSelectExecuteRow">
          <el-table-column width="55" align="center">
            <template slot-scope="scope">
              <el-radio :label="scope.row.calculationId" v-model="selectedTaskId">&nbsp;</el-radio>
            </template>
          </el-table-column>
          <el-table-column prop="calculationName" label="任务名称" />
          <el-table-column prop="calculationType" label="类型" width="100">
            <template slot-scope="scope">
              <el-tag :type="getCalculationTypeColor(scope.row.calculationType)" size="mini">{{ getCalculationTypeText(scope.row.calculationType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="calculationStatus" label="状态" width="80">
            <template slot-scope="scope">
              <el-tag :type="getStatusColor(scope.row.calculationStatus)" size="mini">{{ getStatusText(scope.row.calculationStatus) }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="executeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmExecuteTask" :disabled="!selectedTaskId">执行</el-button>
      </div>
    </el-dialog>

    <!-- 计算结果弹窗 -->
    <el-dialog title="计算结果" :visible.sync="resultsDialogVisible" width="800px">
      <el-table :data="calculationTaskList.filter(t => t.calculationStatus === 'COMPLETED')" border size="mini" max-height="400">
        <el-table-column prop="calculationName" label="任务名称" />
        <el-table-column prop="calculationType" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getCalculationTypeColor(scope.row.calculationType)" size="mini">{{ getCalculationTypeText(scope.row.calculationType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalItems" label="总数据量" width="100" align="center" />
        <el-table-column prop="completedItems" label="完成数" width="80" align="center" />
        <el-table-column prop="failedItems" label="失败数" width="80" align="center" />
        <el-table-column label="完成率" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ getProgress(scope.row) }}%</span>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="resultsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 计算设置弹窗 -->
    <el-dialog title="计算设置" :visible.sync="settingsDialogVisible" width="600px">
      <el-form label-width="120px" size="small">
        <el-form-item label="默认并发数">
          <el-input-number :value="2" :min="1" :max="10" disabled />
          <span style="margin-left: 10px; color: #909399;">系统默认值</span>
        </el-form-item>
        <el-form-item label="默认批次大小">
          <el-input-number :value="1000" :min="100" :max="10000" disabled />
          <span style="margin-left: 10px; color: #909399;">系统默认值</span>
        </el-form-item>
        <el-form-item label="默认超时时间">
          <el-input-number :value="300" :min="60" :max="3600" disabled />
          <span style="margin-left: 10px; color: #909399;">秒</span>
        </el-form-item>
        <el-form-item label="任务总数">
          <span>{{ calculationStats.totalTasks }}</span>
        </el-form-item>
        <el-form-item label="运行中任务">
          <span>{{ calculationStats.runningTasks }}</span>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 计算报告弹窗 -->
    <el-dialog title="计算报告" :visible.sync="reportsDialogVisible" width="700px">
      <el-descriptions title="计算统计报告" :column="2" border>
        <el-descriptions-item label="总任务数">{{ calculationStats.totalTasks }}</el-descriptions-item>
        <el-descriptions-item label="运行中任务">{{ calculationStats.runningTasks }}</el-descriptions-item>
        <el-descriptions-item label="平均执行时间">{{ calculationStats.avgExecutionTime }}s</el-descriptions-item>
        <el-descriptions-item label="成功率">{{ calculationStats.successRate }}%</el-descriptions-item>
      </el-descriptions>
      <div style="margin-top: 20px;">
        <h4 style="margin-bottom: 10px;">各类型任务统计</h4>
        <el-table :data="calculationTypes" border size="mini">
          <el-table-column prop="name" label="计算类型" />
          <el-table-column prop="taskCount" label="任务数量" width="120" align="center" />
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="reportsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>批量计算管理</h4>
        <p>本模块用于管理预算数据的批量计算任务，支持以下功能：</p>
        <ul>
          <li><strong>公式计算</strong>：基于自定义公式对预算数据进行批量计算</li>
          <li><strong>聚合计算</strong>：对多维度预算数据进行汇总聚合</li>
          <li><strong>分摊计算</strong>：按规则将费用分摊到各部门或项目</li>
          <li><strong>转换计算</strong>：数据格式转换和币种换算</li>
        </ul>
        <h4>操作说明</h4>
        <ul>
          <li>点击"创建任务"新建计算任务</li>
          <li>点击计算类型卡片可筛选对应类型的任务</li>
          <li>在任务列表中可执行、编辑、删除任务</li>
          <li>点击任务名称可查看详细信息</li>
        </ul>
      </div>
      <div slot="footer">
        <el-button @click="helpDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'
import { export_json_to_excel } from '@/utils/excel'

export default {
  name: 'BatchCalculation',
  data() {
    return {
      // 统计数据
      calculationStats: {
        totalTasks: 0,
        runningTasks: 0,
        avgExecutionTime: 0,
        successRate: 0
      },

      // 计算类型（从数据库动态构建，不再硬编码）
      calculationTypes: [],
      selectedCalculationType: null,

      // 弹窗控制
      executeDialogVisible: false,
      resultsDialogVisible: false,
      settingsDialogVisible: false,
      reportsDialogVisible: false,
      helpDialogVisible: false,
      selectedTaskForExecute: null,
      selectedTaskId: '',
      
      // 任务列表
      calculationTaskList: [],
      loading: false,
      searchKeyword: '',
      
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentTask: null,
      calculationResults: [],
      executionLogs: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      
      // 表单数据
      taskForm: {
        calculationId: '',
        calculationName: '',
        calculationCode: '',
        calculationType: '',
        dataSource: '',
        concurrency: 2,
        batchSize: 1000,
        timeout: 300,
        calculationFormula: '',
        description: ''
      },

      // 表单验证规则
      taskRules: {
        calculationName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        calculationType: [
          { required: true, message: '请选择计算类型', trigger: 'change' }
        ],
        dataSource: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ],
        calculationFormula: [
          { required: true, message: '请输入计算公式', trigger: 'blur' }
        ]
      }
    }
  },
  
  created() {
    this.getCalculationTaskList()
    this.getCalculationStats()
  },
  
  methods: {
    // 获取任务列表
    async getCalculationTaskList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword }
        if (this.selectedCalculationType) {
          params.calculationType = this.selectedCalculationType
        }
        const response = await advancedFeaturesApi.getBatchCalculationTaskList(params)
        if (response && response.code === 1 && response.data) {
          this.calculationTaskList = response.data.list || []
        }
      } catch (error) {
        this.$message.error('获取任务列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getCalculationStats() {
      try {
        const response = await advancedFeaturesApi.getBatchCalculationStats()
        if (response && response.code === 1 && response.data) {
          const data = response.data
          this.calculationStats = {
            totalTasks: data.totalTasks || 0,
            runningTasks: data.runningTasks || 0,
            avgExecutionTime: data.avgExecutionTime || 0,
            successRate: data.successRate || 0
          }
          // 从数据库动态构建计算类型卡片
          if (data.typeCounts && Object.keys(data.typeCounts).length > 0) {
            this.calculationTypes = Object.keys(data.typeCounts).map(typeId => ({
              id: typeId,
              name: this.getCalculationTypeText(typeId),
              description: this.getCalculationTypeDescription(typeId),
              icon: this.getCalculationTypeIcon(typeId),
              taskCount: data.typeCounts[typeId] || 0
            }))
            // 按任务数量降序排列
            this.calculationTypes.sort((a, b) => b.taskCount - a.taskCount)
          }
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },
    
    // 创建任务
    handleCreateTask() {
      this.dialogTitle = '创建计算任务'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑任务
    handleEdit(row) {
      this.dialogTitle = '编辑计算任务'
      this.dialogVisible = true
      this.taskForm = { ...row }
    },
    
    // 查看详情
    async handleView(row) {
      this.currentTask = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await this.getCalculationResults(row.calculationId)
      await this.getExecutionLogs(row.calculationId)
    },
    
    // 获取计算结果
    async getCalculationResults(taskId) {
      try {
        const response = await advancedFeaturesApi.getBatchCalculationResults(taskId)
        this.calculationResults = response.data
      } catch (error) {
        console.error('获取计算结果失败：', error)
      }
    },
    
    // 获取执行日志
    async getExecutionLogs(taskId) {
      try {
        const response = await advancedFeaturesApi.getBatchCalculationLogs(taskId)
        this.executionLogs = response.data
      } catch (error) {
        console.error('获取执行日志失败：', error)
      }
    },
    
    // 执行任务
    async handleExecute(row) {
      this.$confirm('确定执行该计算任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.executeBatchCalculation(row.calculationId)
          this.$message.success('任务已启动')
          this.getCalculationTaskList()
        } catch (error) {
          this.$message.error('执行失败：' + error.message)
        }
      })
    },
    
    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'stop':
          this.handleStopTask(row)
          break
        case 'retry':
          this.handleRetryTask(row)
          break
        case 'export':
          this.handleExportTask(row)
          break
        case 'copy':
          this.handleCopyTask(row)
          break
        case 'delete':
          this.handleDeleteTask(row)
          break
      }
    },
    
    // 停止任务
    async handleStopTask(row) {
      try {
        const res = await advancedFeaturesApi.stopBatchCalculation(row.calculationId)
        if (res && res.code === 1) {
          this.$message.success('任务已停止')
          this.getCalculationTaskList()
          this.getCalculationStats()
        } else {
          this.$message.error((res && res.msg) || '停止失败')
        }
      } catch (error) {
        this.$message.error('停止失败：' + error.message)
      }
    },

    // 重试任务
    async handleRetryTask(row) {
      try {
        const res = await advancedFeaturesApi.retryBatchCalculation(row.calculationId)
        if (res && res.code === 1) {
          this.$message.success('任务已重置为待执行')
          this.getCalculationTaskList()
          this.getCalculationStats()
        } else {
          this.$message.error((res && res.msg) || '重试失败')
        }
      } catch (error) {
        this.$message.error('重试失败：' + error.message)
      }
    },

    // 导出任务
    async handleExportTask(row) {
      try {
        const response = await advancedFeaturesApi.exportBatchCalculation(row.calculationId)
        if (response && response.code === 1 && response.data) {
          const d = response.data
          const typeMap = {
            'FORMULA': '公式计算', 'AGGREGATION': '聚合计算', 'ALLOCATION': '分摊计算',
            'CONVERSION': '转换计算', 'DEPRECIATION': '折旧计算', 'CURRENCY': '币种换算',
            'PERFORMANCE': '绩效计算', 'VARIANCE': '差异分析', 'FORECAST': '预测计算',
            'CARRY_FORWARD': '结转计算', 'COMPARISON': '对比分析', 'CONSOLIDATION': '合并计算',
            'REPORT': '报表计算', 'SENSITIVITY': '敏感性分析', 'SIMULATION': '模拟计算', 'VALIDATION': '数据校验',
            'INITIALIZATION': '数据初始化'
          }
          const statusMap = {
            'PENDING': '待执行', 'RUNNING': '执行中', 'IN_PROGRESS': '执行中',
            'COMPLETED': '已完成', 'FAILED': '失败', 'STOPPED': '已停止'
          }
          export_json_to_excel({
            header: ['任务编号', '任务名称', '计算类型', '状态', '总条数', '完成条数', '失败条数', '执行时长(ms)', '开始时间', '结束时间', '数据来源', '描述', '创建人', '创建时间'],
            data: [[
              d.calculationCode || '',
              d.calculationName || '',
              typeMap[d.calculationType] || d.calculationType || '',
              statusMap[d.calculationStatus] || d.calculationStatus || '',
              d.totalItems || 0,
              d.completedItems || 0,
              d.failedItems || 0,
              d.executionTime || 0,
              d.startTime || '',
              d.endTime || '',
              d.dataSource || '',
              d.description || '',
              d.createBy || '',
              d.createTime || ''
            ]],
            filename: `批量计算任务_${d.calculationCode || d.calculationId}`,
            autoWidth: true
          })
          this.$message.success('导出成功')
        } else {
          this.$message.error((response && response.msg) || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 复制任务
    async handleCopyTask(row) {
      try {
        await advancedFeaturesApi.copyBatchCalculation(row.calculationId)
        this.$message.success('复制成功')
        this.getCalculationTaskList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },
    
    // 删除任务
    handleDeleteTask(row) {
      this.$confirm('确定删除该计算任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteBatchCalculation(row.calculationId)
          this.$message.success('删除成功')
          this.getCalculationTaskList()
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
            if (this.taskForm.calculationId) {
              await advancedFeaturesApi.updateBatchCalculation(this.taskForm)
              this.$message.success('更新成功')
            } else {
              await advancedFeaturesApi.createBatchCalculation(this.taskForm)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getCalculationTaskList()
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
        calculationId: '',
        calculationName: '',
        calculationCode: '',
        calculationType: '',
        dataSource: '',
        concurrency: 2,
        batchSize: 1000,
        timeout: 300,
        calculationFormula: '',
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
      this.getCalculationTaskList()
      this.getCalculationStats()
    },
    
    // 执行计算
    handleExecuteTask() {
      if (this.calculationTaskList.length === 0) {
        this.$message.warning('暂无可执行的计算任务')
        return
      }
      this.executeDialogVisible = true
    },

    // 查看结果
    handleViewResults() {
      this.resultsDialogVisible = true
    },

    // 计算设置
    handleSettings() {
      this.settingsDialogVisible = true
    },

    // 计算报告
    handleReports() {
      this.reportsDialogVisible = true
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },
    
    // 刷新计算类型
    refreshCalculationTypes() {
      this.getCalculationTaskList()
      this.getCalculationStats()
      this.$message.success('已刷新')
    },

    // 选择计算类型
    handleSelectCalculationType(calculationType) {
      if (this.selectedCalculationType === calculationType.id) {
        // 再次点击取消选择
        this.selectedCalculationType = null
      } else {
        this.selectedCalculationType = calculationType.id
      }
      this.getCalculationTaskList()
    },
    
    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },
    
    // 格式化数字
    formatNumber(num) {
      return new Intl.NumberFormat('zh-CN').format(num)
    },
    
    // 获取计算类型颜色
    getCalculationTypeColor(type) {
      const colorMap = {
        'FORMULA': 'primary',
        'AGGREGATION': 'success',
        'ALLOCATION': 'warning',
        'CONVERSION': 'info',
        'DEPRECIATION': 'danger',
        'CURRENCY': '',
        'PERFORMANCE': 'success',
        'VARIANCE': 'warning',
        'FORECAST': 'primary',
        'CARRY_FORWARD': 'info',
        'COMPARISON': 'primary',
        'CONSOLIDATION': 'success',
        'REPORT': 'info',
        'SENSITIVITY': 'warning',
        'SIMULATION': 'danger',
        'VALIDATION': '',
        'INITIALIZATION': 'primary'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取计算类型文本
    getCalculationTypeText(type) {
      const textMap = {
        'FORMULA': '公式计算',
        'AGGREGATION': '聚合计算',
        'ALLOCATION': '分摊计算',
        'CONVERSION': '转换计算',
        'DEPRECIATION': '折旧计算',
        'CURRENCY': '币种换算',
        'PERFORMANCE': '绩效计算',
        'VARIANCE': '差异分析',
        'FORECAST': '预测计算',
        'CARRY_FORWARD': '结转计算',
        'COMPARISON': '对比分析',
        'CONSOLIDATION': '合并计算',
        'REPORT': '报表计算',
        'SENSITIVITY': '敏感性分析',
        'SIMULATION': '模拟计算',
        'VALIDATION': '数据校验',
        'INITIALIZATION': '数据初始化'
      }
      return textMap[type] || type
    },

    // 获取计算类型图标
    getCalculationTypeIcon(type) {
      const iconMap = {
        'FORMULA': 'el-icon-edit-outline',
        'AGGREGATION': 'el-icon-pie-chart',
        'ALLOCATION': 'el-icon-share',
        'CONVERSION': 'el-icon-refresh',
        'DEPRECIATION': 'el-icon-bottom',
        'CURRENCY': 'el-icon-money',
        'PERFORMANCE': 'el-icon-data-line',
        'VARIANCE': 'el-icon-sort',
        'FORECAST': 'el-icon-trend-charts',
        'CARRY_FORWARD': 'el-icon-right',
        'COMPARISON': 'el-icon-c-scale-to-original',
        'CONSOLIDATION': 'el-icon-files',
        'REPORT': 'el-icon-document',
        'SENSITIVITY': 'el-icon-aim',
        'SIMULATION': 'el-icon-magic-stick',
        'VALIDATION': 'el-icon-circle-check',
        'INITIALIZATION': 'el-icon-setting'
      }
      return iconMap[type] || 'el-icon-cpu'
    },

    // 获取计算类型描述
    getCalculationTypeDescription(type) {
      const descMap = {
        'FORMULA': '基于公式的批量计算',
        'AGGREGATION': '数据聚合和汇总计算',
        'ALLOCATION': '费用分摊和分配计算',
        'CONVERSION': '数据格式转换计算',
        'DEPRECIATION': '固定资产折旧批量计算',
        'CURRENCY': '多币种汇率转换计算',
        'PERFORMANCE': '绩效评分批量计算',
        'VARIANCE': '预算执行差异分析',
        'FORECAST': '基于历史数据趋势预测',
        'CARRY_FORWARD': '科目余额结转计算',
        'COMPARISON': '预算版本数据对比',
        'CONSOLIDATION': '子公司数据合并计算',
        'REPORT': '综合报表数据生成',
        'SENSITIVITY': '关键参数敏感性分析',
        'SIMULATION': '多场景预算模拟计算',
        'VALIDATION': '数据完整性批量校验',
        'INITIALIZATION': '预算基础数据初始化'
      }
      return descMap[type] || '批量计算任务'
    },

    getProgressColor(progress) {
      if (progress >= 90) return '#67C23A'
      if (progress >= 60) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'RUNNING': 'primary',
        'IN_PROGRESS': 'primary',
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
        'IN_PROGRESS': '运行中',
        'COMPLETED': '已完成',
        'FAILED': '失败',
        'STOPPED': '已停止',
        'PENDING': '待执行'
      }
      return textMap[status] || status
    },

    // 计算进度百分比
    getProgress(row) {
      if (!row || !row.totalItems || row.totalItems === 0) return 0
      return Math.round((row.completedItems || 0) * 100 / row.totalItems)
    },

    // 行点击选中执行任务
    handleSelectExecuteRow(row) {
      this.selectedTaskForExecute = row
      this.selectedTaskId = row.calculationId
    },

    // 确认执行任务
    async confirmExecuteTask() {
      if (!this.selectedTaskId) return
      try {
        await advancedFeaturesApi.executeBatchCalculation(this.selectedTaskId)
        this.$message.success('任务已启动')
        this.executeDialogVisible = false
        this.selectedTaskForExecute = null
        this.selectedTaskId = ''
        this.getCalculationTaskList()
        this.getCalculationStats()
      } catch (error) {
        this.$message.error('执行失败：' + error.message)
      }
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
.batch-calculation {
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

      &.running-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.performance-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
        color: white;
      }

      &.success-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
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

  .calculation-types-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .calculation-type-item {
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

      .calculation-type-icon {
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

      .calculation-type-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .calculation-type-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
      }

      .calculation-type-stats {
        .task-count {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .calculation-tasks-card {
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

    .data-count {
      color: #409EFF;
      font-weight: 500;
    }

    .execution-time {
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
}
</style>
