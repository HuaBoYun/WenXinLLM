<template>
  <div class="automation-workflow">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>自动化工作流</h2>
      <p>智能预算工作流自动化，支持流程设计、触发条件配置和执行监控</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateWorkflow">创建工作流</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-video-play" @click="handleRunWorkflow">执行工作流</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewExecution">执行监控</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">工作流设置</el-button>
            <el-button icon="el-icon-document" @click="handleTemplates">流程模板</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 工作流统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ workflowStats.totalWorkflows }}</div>
            <div class="stat-label">工作流总数</div>
            <div class="stat-description">已创建工作流数量</div>
            <div class="stat-trend">
              <i class="el-icon-s-operation"></i>
              <span>自动化流程</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-operation"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ workflowStats.activeWorkflows }}</div>
            <div class="stat-label">活跃工作流</div>
            <div class="stat-description">正在运行的工作流</div>
            <div class="stat-trend">
              <i class="el-icon-loading"></i>
              <span>运行中</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-loading"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card executions-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ workflowStats.totalExecutions }}</div>
            <div class="stat-label">执行次数</div>
            <div class="stat-description">累计执行次数</div>
            <div class="stat-trend">
              <i class="el-icon-cpu"></i>
              <span>高频执行</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-cpu"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card success-rate-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ workflowStats.successRate }}%</div>
            <div class="stat-label">成功率</div>
            <div class="stat-description">工作流执行成功率</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>高成功率</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 工作流类型选择 -->
    <el-card class="workflow-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>工作流类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshWorkflowTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="wt in workflowTypes" :key="wt.typeKey">
          <el-card
            class="workflow-type-item"
            shadow="hover"
            @click.native="handleSelectWorkflowType(wt)"
            :class="{ 'selected': selectedWorkflowType === wt.typeKey }"
          >
            <div class="workflow-type-icon">
              <i :class="wt.icon"></i>
            </div>
            <div class="workflow-type-title">{{ wt.name }}</div>
            <div class="workflow-type-description">{{ wt.description }}</div>
            <div class="workflow-type-stats">
              <span class="workflow-count">{{ wt.workflowCount }} 个工作流</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 工作流列表 -->
    <el-card class="workflows-card" shadow="never">
      <div slot="header" class="card-header">
        <span>自动化工作流</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索工作流"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getWorkflowList"
            clearable
            @clear="getWorkflowList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getWorkflowList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="workflowList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="workflowName" label="工作流名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.workflowName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="workflowType" label="工作流类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getWorkflowTypeColor(scope.row.workflowType)" size="mini">
              {{ getWorkflowTypeText(scope.row.workflowType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="triggerType" label="触发方式" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTriggerTypeColor(scope.row.triggerType)" size="mini">
              {{ getTriggerTypeText(scope.row.triggerType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executionCount" label="执行次数" width="100" align="center">
          <template slot-scope="scope">
            <span class="execution-count">{{ scope.row.executionCount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastExecution" label="最后执行" width="150" align="center">
          <template slot-scope="scope">
            <span class="last-execution">{{ scope.row.lastExecution }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="创建人" width="100" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-video-play"
              @click="handleRun(scope.row)"
              :disabled="scope.row.status === 'RUNNING'"
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
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="export">导出</el-dropdown-item>
                <el-dropdown-item command="logs">执行日志</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- ========== 创建/编辑工作流弹窗 ========== -->
    <el-dialog :title="formMode === 'create' ? '创建工作流' : '编辑工作流'" :visible.sync="formVisible" width="600px" :close-on-click-modal="false">
      <el-form ref="workflowForm" :model="workflowForm" :rules="formRules" label-width="100px">
        <el-form-item label="工作流名称" prop="workflowName">
          <el-input v-model="workflowForm.workflowName" placeholder="请输入工作流名称" />
        </el-form-item>
        <el-form-item label="工作流类型" prop="workflowType">
          <el-select v-model="workflowForm.workflowType" placeholder="请选择类型" style="width:100%">
            <el-option label="预算审批流程" value="APPROVAL_WORKFLOW" />
            <el-option label="数据同步流程" value="DATA_SYNC_WORKFLOW" />
            <el-option label="报告生成流程" value="REPORT_WORKFLOW" />
            <el-option label="预警通知流程" value="NOTIFICATION_WORKFLOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="触发方式" prop="triggerType">
          <el-select v-model="workflowForm.triggerType" placeholder="请选择触发方式" style="width:100%">
            <el-option label="手动触发" value="MANUAL" />
            <el-option label="定时触发" value="SCHEDULED" />
            <el-option label="事件触发" value="EVENT_DRIVEN" />
            <el-option label="API触发" value="API_TRIGGER" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="workflowForm.status" placeholder="请选择状态" style="width:100%">
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="非活跃" value="INACTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="workflowForm.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button type="primary" :loading="formSubmitting" @click="submitWorkflowForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- ========== 详情弹窗 ========== -->
    <el-dialog title="工作流详情" :visible.sync="detailVisible" width="600px">
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="工作流名称">{{ detailData.workflowName }}</el-descriptions-item>
        <el-descriptions-item label="工作流类型">
          <el-tag :type="getWorkflowTypeColor(detailData.workflowType)" size="mini">{{ getWorkflowTypeText(detailData.workflowType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="触发方式">
          <el-tag :type="getTriggerTypeColor(detailData.triggerType)" size="mini">{{ getTriggerTypeText(detailData.triggerType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusColor(detailData.status)" size="mini">{{ getStatusText(detailData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="执行次数">{{ detailData.executionCount }}</el-descriptions-item>
        <el-descriptions-item label="最后执行">{{ detailData.lastExecution || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.creator }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ detailData.description || '暂无描述' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer"><el-button @click="detailVisible = false">关 闭</el-button></div>
    </el-dialog>

    <!-- ========== 执行工作流弹窗 ========== -->
    <el-dialog title="执行工作流" :visible.sync="runDialogVisible" width="600px">
      <p style="margin-bottom:12px;color:#606266;">选择要执行的工作流：</p>
      <el-table :data="workflowList" border stripe highlight-current-row max-height="400" @row-click="selectRunRow" :row-class-name="runRowClassName">
        <el-table-column prop="workflowName" label="工作流名称" />
        <el-table-column prop="workflowType" label="类型" width="120" align="center">
          <template slot-scope="scope"><el-tag :type="getWorkflowTypeColor(scope.row.workflowType)" size="mini">{{ getWorkflowTypeText(scope.row.workflowType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope"><el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag></template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="runDialogVisible = false">取 消</el-button>
        <el-button type="primary" :disabled="!selectedRunRow" @click="confirmRunWorkflow">执 行</el-button>
      </div>
    </el-dialog>

    <!-- ========== 执行监控弹窗 ========== -->
    <el-dialog title="执行监控" :visible.sync="monitorVisible" width="750px" v-loading="monitorLoading">
      <el-row :gutter="16" style="margin-bottom:16px;">
        <el-col :span="8"><el-statistic title="运行中" :value="monitorData.runningCount || 0" /></el-col>
        <el-col :span="8"><el-statistic title="累计执行" :value="monitorData.totalExecutions || 0" /></el-col>
        <el-col :span="8"><el-statistic title="成功率" :value="(monitorData.successRate || 0) + '%'" /></el-col>
      </el-row>
      <el-table :data="monitorData.runningWorkflows || []" border stripe max-height="300">
        <el-table-column prop="workflowName" label="工作流名称" />
        <el-table-column prop="workflowType" label="类型" width="120" align="center">
          <template slot-scope="scope"><el-tag size="mini">{{ getWorkflowTypeText(scope.row.workflowType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="executionCount" label="执行次数" width="100" align="center" />
        <el-table-column prop="lastExecution" label="最后执行" width="160" align="center" />
      </el-table>
      <div slot="footer"><el-button @click="monitorVisible = false">关 闭</el-button></div>
    </el-dialog>

    <!-- ========== 工作流设置弹窗 ========== -->
    <el-dialog title="工作流设置" :visible.sync="settingsVisible" width="550px" :close-on-click-modal="false">
      <el-form :model="settingsForm" label-width="140px">
        <el-form-item label="最大并发数"><el-input-number v-model="settingsForm.maxConcurrentWorkflows" :min="1" :max="50" /></el-form-item>
        <el-form-item label="默认超时(秒)"><el-input-number v-model="settingsForm.defaultTimeout" :min="60" :max="86400" :step="60" /></el-form-item>
        <el-form-item label="重试次数"><el-input-number v-model="settingsForm.retryCount" :min="0" :max="10" /></el-form-item>
        <el-form-item label="失败时通知"><el-switch v-model="settingsForm.notifyOnFailure" /></el-form-item>
        <el-form-item label="成功时通知"><el-switch v-model="settingsForm.notifyOnSuccess" /></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsVisible = false">取 消</el-button>
        <el-button type="primary" :loading="settingsSaving" @click="saveSettings">保 存</el-button>
      </div>
    </el-dialog>

    <!-- ========== 流程模板弹窗 ========== -->
    <el-dialog title="流程模板" :visible.sync="templatesVisible" width="700px" v-loading="templatesLoading">
      <el-table :data="templatesList" border stripe>
        <el-table-column prop="templateName" label="模板名称" />
        <el-table-column prop="workflowType" label="类型" width="140" align="center">
          <template slot-scope="scope"><el-tag :type="getWorkflowTypeColor(scope.row.workflowType)" size="mini">{{ getWorkflowTypeText(scope.row.workflowType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column label="操作" width="100" align="center">
          <template slot-scope="scope"><el-button type="text" size="mini" @click="useTemplate(scope.row)">使用模板</el-button></template>
        </el-table-column>
      </el-table>
      <div slot="footer"><el-button @click="templatesVisible = false">关 闭</el-button></div>
    </el-dialog>

    <!-- ========== 帮助弹窗 ========== -->
    <el-dialog title="帮助" :visible.sync="helpVisible" width="600px">
      <div class="help-content">
        <h4>自动化工作流使用指南</h4>
        <p><b>1. 创建工作流</b>：点击"创建工作流"按钮，填写名称、类型、触发方式等信息。</p>
        <p><b>2. 工作流类型</b>：</p>
        <ul>
          <li><b>预算审批流程</b> - 自动化预算审批，支持多级审批链路</li>
          <li><b>数据同步流程</b> - 定时或事件驱动的数据同步</li>
          <li><b>报告生成流程</b> - 自动生成和分发财务报告</li>
          <li><b>预警通知流程</b> - 预算超支等异常情况的自动预警</li>
        </ul>
        <p><b>3. 触发方式</b>：手动触发、定时触发、事件触发、API触发。</p>
        <p><b>4. 执行监控</b>：实时查看工作流运行状态和执行历史。</p>
      </div>
      <div slot="footer"><el-button @click="helpVisible = false">关 闭</el-button></div>
    </el-dialog>

    <!-- ========== 执行日志弹窗 ========== -->
    <el-dialog title="执行日志" :visible.sync="logsVisible" width="700px" v-loading="logsLoading">
      <el-table :data="logsList" border stripe>
        <el-table-column prop="logId" label="日志ID" width="180" show-overflow-tooltip />
        <el-table-column prop="executionTime" label="执行时间" width="160" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope"><el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="executionCount" label="执行次数" width="100" align="center" />
      </el-table>
      <div slot="footer"><el-button @click="logsVisible = false">关 闭</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'

export default {
  name: 'AutomationWorkflow',
  data() {
    return {
      // 统计数据
      workflowStats: { totalWorkflows: 0, activeWorkflows: 0, totalExecutions: 0, successRate: 0 },
      // 工作流类型（typeKey 对应后端 WORKFLOW_TYPE 字段）
      workflowTypes: [
        { typeKey: 'APPROVAL_WORKFLOW', name: '预算审批流程', description: '自动化预算审批流程', icon: 'el-icon-s-check', workflowCount: 0 },
        { typeKey: 'DATA_SYNC_WORKFLOW', name: '数据同步流程', description: '自动数据同步和更新', icon: 'el-icon-refresh', workflowCount: 0 },
        { typeKey: 'REPORT_WORKFLOW', name: '报告生成流程', description: '定时报告生成和分发', icon: 'el-icon-document', workflowCount: 0 },
        { typeKey: 'NOTIFICATION_WORKFLOW', name: '预警通知流程', description: '异常预警和通知', icon: 'el-icon-bell', workflowCount: 0 }
      ],
      selectedWorkflowType: null,
      // 列表
      workflowList: [],
      loading: false,
      searchKeyword: '',
      // 创建/编辑弹窗
      formVisible: false,
      formMode: 'create',
      formSubmitting: false,
      workflowForm: { workflowName: '', workflowType: '', triggerType: '', status: 'ACTIVE', description: '' },
      formRules: {
        workflowName: [{ required: true, message: '请输入工作流名称', trigger: 'blur' }],
        workflowType: [{ required: true, message: '请选择工作流类型', trigger: 'change' }],
        triggerType: [{ required: true, message: '请选择触发方式', trigger: 'change' }]
      },
      // 详情弹窗
      detailVisible: false,
      detailData: null,
      // 执行工作流弹窗
      runDialogVisible: false,
      selectedRunRow: null,
      // 执行监控弹窗
      monitorVisible: false,
      monitorLoading: false,
      monitorData: {},
      // 设置弹窗
      settingsVisible: false,
      settingsSaving: false,
      settingsForm: { maxConcurrentWorkflows: 10, defaultTimeout: 3600, retryCount: 3, notifyOnFailure: true, notifyOnSuccess: false },
      // 模板弹窗
      templatesVisible: false,
      templatesLoading: false,
      templatesList: [],
      // 帮助弹窗
      helpVisible: false,
      // 日志弹窗
      logsVisible: false,
      logsLoading: false,
      logsList: []
    }
  },
  created() {
    this.getWorkflowList()
    this.getWorkflowStats()
  },

  methods: {
    // ===== 数据获取 =====
    async getWorkflowList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword }
        if (this.selectedWorkflowType) params.workflowType = this.selectedWorkflowType
        const res = await advancedFeaturesApi.getAutomationWorkflowList(params)
        if (res && res.code === 1) {
          const d = res.data || {}
          this.workflowList = d.list || d || []
        }
      } catch (e) {
        this.$message.error('获取工作流列表失败：' + e.message)
      } finally {
        this.loading = false
      }
    },
    async getWorkflowStats() {
      try {
        const res = await advancedFeaturesApi.getAutomationWorkflowStats()
        if (res && res.code === 1 && res.data) {
          this.workflowStats = {
            totalWorkflows: res.data.totalWorkflows || 0,
            activeWorkflows: res.data.activeWorkflows || 0,
            totalExecutions: res.data.totalExecutions || 0,
            successRate: res.data.successRate || 0
          }
          // 更新类型卡片计数
          const tc = res.data.typeCount || {}
          this.workflowTypes.forEach(wt => { wt.workflowCount = tc[wt.typeKey] || 0 })
        }
      } catch (e) {
        console.error('获取统计数据失败：', e)
      }
    },

    // ===== 工具栏按钮 =====
    handleCreateWorkflow() {
      this.formMode = 'create'
      this.workflowForm = { workflowName: '', workflowType: '', triggerType: '', status: 'ACTIVE', description: '' }
      this.formVisible = true
      this.$nextTick(() => { this.$refs.workflowForm && this.$refs.workflowForm.clearValidate() })
    },
    handleRefresh() {
      this.getWorkflowList()
      this.getWorkflowStats()
    },
    handleRunWorkflow() {
      this.selectedRunRow = null
      this.runDialogVisible = true
    },
    async handleViewExecution() {
      this.monitorVisible = true
      this.monitorLoading = true
      try {
        const res = await advancedFeaturesApi.getWorkflowExecutionMonitor()
        if (res && res.code === 1) this.monitorData = res.data || {}
      } catch (e) {
        this.$message.error('获取监控数据失败')
      } finally {
        this.monitorLoading = false
      }
    },
    async handleSettings() {
      this.settingsVisible = true
      try {
        const res = await advancedFeaturesApi.getWorkflowSettings()
        if (res && res.code === 1 && res.data) this.settingsForm = res.data
      } catch (e) {
        console.error('获取设置失败', e)
      }
    },
    async handleTemplates() {
      this.templatesVisible = true
      this.templatesLoading = true
      try {
        const res = await advancedFeaturesApi.getWorkflowTemplates()
        if (res && res.code === 1) this.templatesList = res.data || []
      } catch (e) {
        this.$message.error('获取模板失败')
      } finally {
        this.templatesLoading = false
      }
    },
    handleHelp() { this.helpVisible = true },

    // ===== 创建/编辑表单提交 =====
    submitWorkflowForm() {
      this.$refs.workflowForm.validate(async (valid) => {
        if (!valid) return
        this.formSubmitting = true
        try {
          const api = this.formMode === 'create'
            ? advancedFeaturesApi.createAutomationWorkflow
            : advancedFeaturesApi.updateAutomationWorkflow
          const res = await api(this.workflowForm)
          if (res && res.code === 1) {
            this.$message.success(this.formMode === 'create' ? '创建成功' : '更新成功')
            this.formVisible = false
            this.getWorkflowList()
            this.getWorkflowStats()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败：' + e.message)
        } finally {
          this.formSubmitting = false
        }
      })
    },

    // ===== 列表行操作 =====
    async handleView(row) {
      this.detailVisible = true
      this.detailData = null
      try {
        const res = await advancedFeaturesApi.getAutomationWorkflowDetail(row.workflowId)
        if (res && res.code === 1) this.detailData = res.data
        else this.detailData = row
      } catch (e) {
        this.detailData = row
      }
    },
    handleEdit(row) {
      this.formMode = 'edit'
      this.workflowForm = {
        workflowId: row.workflowId,
        workflowName: row.workflowName,
        workflowType: row.workflowType,
        triggerType: row.triggerType,
        status: row.status || 'ACTIVE',
        description: row.description || ''
      }
      this.formVisible = true
      this.$nextTick(() => { this.$refs.workflowForm && this.$refs.workflowForm.clearValidate() })
    },
    async handleRun(row) {
      this.$confirm('确定执行该工作流吗？', '提示', { type: 'warning' }).then(async () => {
        try {
          await advancedFeaturesApi.runAutomationWorkflow(row.workflowId)
          this.$message.success('工作流已启动')
          this.getWorkflowList()
          this.getWorkflowStats()
        } catch (e) {
          this.$message.error('执行失败：' + e.message)
        }
      }).catch(() => {})
    },
    handleMoreAction(command, row) {
      const actions = { stop: this.handleStopWorkflow, copy: this.handleCopyWorkflow, export: this.handleExportWorkflow, logs: this.handleViewLogs, delete: this.handleDeleteWorkflow }
      if (actions[command]) actions[command](row)
    },
    async handleStopWorkflow(row) {
      try {
        await advancedFeaturesApi.stopAutomationWorkflow(row.workflowId)
        this.$message.success('工作流已停止')
        this.getWorkflowList()
      } catch (e) { this.$message.error('停止失败：' + e.message) }
    },
    async handleCopyWorkflow(row) {
      try {
        await advancedFeaturesApi.copyAutomationWorkflow(row.workflowId)
        this.$message.success('复制成功')
        this.getWorkflowList()
      } catch (e) { this.$message.error('复制失败：' + e.message) }
    },
    async handleExportWorkflow(row) {
      try {
        const res = await advancedFeaturesApi.exportAutomationWorkflow(row.workflowId)
        if (res && res.code === 1 && res.data) {
          const jsonStr = JSON.stringify(res.data, null, 2)
          const blob = new Blob([jsonStr], { type: 'application/json' })
          const url = window.URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = (row.workflowName || 'workflow') + '.json'
          document.body.appendChild(a)
          a.click()
          document.body.removeChild(a)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error((res && res.msg) || '导出失败')
        }
      } catch (e) { this.$message.error('导出失败：' + e.message) }
    },
    async handleViewLogs(row) {
      this.logsVisible = true
      this.logsLoading = true
      try {
        const res = await advancedFeaturesApi.getWorkflowLogs(row.workflowId)
        if (res && res.code === 1) this.logsList = res.data || []
      } catch (e) { this.$message.error('获取日志失败') }
      finally { this.logsLoading = false }
    },
    handleDeleteWorkflow(row) {
      this.$confirm('确定删除该工作流吗？删除后不可恢复。', '提示', { type: 'warning' }).then(async () => {
        try {
          await advancedFeaturesApi.deleteAutomationWorkflow(row.workflowId)
          this.$message.success('删除成功')
          this.getWorkflowList()
          this.getWorkflowStats()
        } catch (e) { this.$message.error('删除失败：' + e.message) }
      }).catch(() => {})
    },
    handleRowClick(row) { this.handleView(row) },

    // ===== 类型筛选（点击切换） =====
    handleSelectWorkflowType(wt) {
      this.selectedWorkflowType = this.selectedWorkflowType === wt.typeKey ? null : wt.typeKey
      this.getWorkflowList()
    },
    refreshWorkflowTypes() {
      this.getWorkflowList()
      this.getWorkflowStats()
      this.$message.success('已刷新')
    },

    // ===== 执行工作流弹窗 =====
    selectRunRow(row) { this.selectedRunRow = row },
    runRowClassName({ row }) { return this.selectedRunRow && this.selectedRunRow.workflowId === row.workflowId ? 'selected-row' : '' },
    async confirmRunWorkflow() {
      if (!this.selectedRunRow) return
      try {
        await advancedFeaturesApi.runAutomationWorkflow(this.selectedRunRow.workflowId)
        this.$message.success('工作流已启动')
        this.runDialogVisible = false
        this.getWorkflowList()
        this.getWorkflowStats()
      } catch (e) { this.$message.error('执行失败：' + e.message) }
    },

    // ===== 设置保存 =====
    async saveSettings() {
      this.settingsSaving = true
      try {
        const res = await advancedFeaturesApi.saveWorkflowSettings(this.settingsForm)
        if (res && res.code === 1) {
          this.$message.success('保存成功')
          this.settingsVisible = false
        } else { this.$message.error(res.msg || '保存失败') }
      } catch (e) { this.$message.error('保存失败：' + e.message) }
      finally { this.settingsSaving = false }
    },

    // ===== 使用模板 =====
    useTemplate(tpl) {
      this.templatesVisible = false
      this.formMode = 'create'
      this.workflowForm = {
        workflowName: tpl.templateName,
        workflowType: tpl.workflowType,
        triggerType: 'MANUAL',
        status: 'ACTIVE',
        description: tpl.description || ''
      }
      this.formVisible = true
    },
    getWorkflowTypeColor(type) {
      const colorMap = {
        'APPROVAL_WORKFLOW': 'primary',
        'DATA_SYNC_WORKFLOW': 'success',
        'REPORT_WORKFLOW': 'warning',
        'NOTIFICATION_WORKFLOW': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 获取工作流类型文本
    getWorkflowTypeText(type) {
      const textMap = {
        'APPROVAL_WORKFLOW': '审批流程',
        'DATA_SYNC_WORKFLOW': '数据同步',
        'REPORT_WORKFLOW': '报告生成',
        'NOTIFICATION_WORKFLOW': '通知预警'
      }
      return textMap[type] || type
    },

    // 获取触发类型颜色
    getTriggerTypeColor(type) {
      const colorMap = {
        'MANUAL': 'primary',
        'SCHEDULED': 'success',
        'EVENT_DRIVEN': 'warning',
        'API_TRIGGER': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 获取触发类型文本
    getTriggerTypeText(type) {
      const textMap = {
        'MANUAL': '手动触发',
        'SCHEDULED': '定时触发',
        'EVENT_DRIVEN': '事件触发',
        'API_TRIGGER': 'API触发'
      }
      return textMap[type] || type
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'RUNNING': 'primary',
        'ERROR': 'danger',
        'STOPPED': 'info'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'INACTIVE': '非活跃',
        'RUNNING': '运行中',
        'ERROR': '错误',
        'STOPPED': '已停止'
      }
      return textMap[status] || status
    }
  }
}
</script>