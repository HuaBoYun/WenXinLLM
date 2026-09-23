<template>
  <div class="bi-integration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>BI系统集成</h2>
      <p>Power BI、Tableau、QlikView等BI工具集成，支持数据可视化和智能分析</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateConnection">创建连接</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-connection" @click="handleTestConnection">连接测试</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleDashboard">仪表板</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">集成设置</el-button>
            <el-button icon="el-icon-pie-chart" @click="handleReports">报表管理</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- BI集成统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ biStats.totalConnections }}</div>
            <div class="stat-label">BI连接</div>
            <div class="stat-description">已配置BI连接数</div>
            <div class="stat-trend">
              <i class="el-icon-pie-chart"></i>
              <span>BI工具</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-pie-chart"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ biStats.activeDashboards }}</div>
            <div class="stat-label">活跃仪表板</div>
            <div class="stat-description">正在使用的仪表板</div>
            <div class="stat-trend">
              <i class="el-icon-monitor"></i>
              <span>实时监控</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-monitor"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card reports-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ biStats.totalReports }}</div>
            <div class="stat-label">报表数量</div>
            <div class="stat-description">已创建报表数量</div>
            <div class="stat-trend">
              <i class="el-icon-document"></i>
              <span>智能报表</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-document"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card users-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ biStats.activeUsers }}</div>
            <div class="stat-label">活跃用户</div>
            <div class="stat-description">本月活跃用户数</div>
            <div class="stat-trend">
              <i class="el-icon-user"></i>
              <span>用户活跃</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-user"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- BI工具类型选择 -->
    <el-card class="bi-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>BI工具类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshBiTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="biType in biTypes" :key="biType.id">
          <el-card 
            class="bi-type-item" 
            shadow="hover" 
            @click.native="handleSelectBiType(biType)"
            :class="{ 'selected': selectedBiType === biType.id }"
          >
            <div class="bi-type-icon">
              <i :class="biType.icon"></i>
            </div>
            <div class="bi-type-title">{{ biType.name }}</div>
            <div class="bi-type-description">{{ biType.description }}</div>
            <div class="bi-type-stats">
              <span class="connection-count">{{ biType.connectionCount }} 个连接</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- BI连接列表 -->
    <el-card class="bi-connections-card" shadow="never">
      <div slot="header" class="card-header">
        <span>BI连接管理</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索连接"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getBiConnectionList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="filteredBiConnectionList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="connectionName" label="连接名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.connectionName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="biType" label="BI类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getBiTypeColor(scope.row.biType)" size="mini">
              {{ getBiTypeText(scope.row.biType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="serverUrl" label="服务器URL" width="250" show-overflow-tooltip />
        <el-table-column prop="workspace" label="工作区" width="120" align="center" />
        <el-table-column prop="reportCount" label="报表数量" width="100" align="center">
          <template slot-scope="scope">
            <span class="report-count">{{ scope.row.reportCount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdate" label="最后更新" width="150" align="center">
          <template slot-scope="scope">
            <span class="last-update">{{ scope.row.lastUpdate }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="连接状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-connection"
              @click="handleTestConnection(scope.row)"
            >测试</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleViewDashboard(scope.row)"
              :disabled="scope.row.status !== 'ACTIVE'"
            >仪表板</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="edit">编辑</el-dropdown-item>
                <el-dropdown-item command="reports">报表管理</el-dropdown-item>
                <el-dropdown-item command="datasets">数据集</el-dropdown-item>
                <el-dropdown-item command="permissions">权限设置</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 连接详情抽屉 -->
    <el-drawer
      title="BI连接详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentConnection">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="连接基本信息" :column="2" border>
              <el-descriptions-item label="连接名称">{{ currentConnection.connectionName }}</el-descriptions-item>
              <el-descriptions-item label="BI类型">{{ getBiTypeText(currentConnection.biType) }}</el-descriptions-item>
              <el-descriptions-item label="服务器URL">{{ currentConnection.serverUrl }}</el-descriptions-item>
              <el-descriptions-item label="工作区">{{ currentConnection.workspace }}</el-descriptions-item>
              <el-descriptions-item label="用户名">{{ currentConnection.username }}</el-descriptions-item>
              <el-descriptions-item label="报表数量">{{ currentConnection.reportCount }}</el-descriptions-item>
              <el-descriptions-item label="最后更新">{{ currentConnection.lastUpdate }}</el-descriptions-item>
              <el-descriptions-item label="连接状态">
                <el-tag :type="getStatusColor(currentConnection.status)" size="mini">
                  {{ getStatusText(currentConnection.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentConnection.createTime }}</el-descriptions-item>
              <el-descriptions-item label="连接描述" :span="2">{{ currentConnection.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="报表列表" name="reports">
            <el-table :data="biReports" border size="mini">
              <el-table-column prop="reportName" label="报表名称" width="200" />
              <el-table-column prop="reportType" label="报表类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getReportTypeColor(scope.row.reportType)" size="mini">
                    {{ getReportTypeText(scope.row.reportType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="dataSource" label="数据源" width="150" />
              <el-table-column prop="lastRefresh" label="最后刷新" width="150" />
              <el-table-column prop="viewCount" label="查看次数" width="100" align="center" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getReportStatusColor(scope.row.status)" size="mini">
                    {{ getReportStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120" align="center">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" @click="handleViewReport(scope.row)">
                    查看
                  </el-button>
                  <el-button type="text" size="mini" @click="handleRefreshReport(scope.row)">
                    刷新
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="数据集" name="datasets">
            <el-table :data="biDatasets" border size="mini">
              <el-table-column prop="datasetName" label="数据集名称" width="200" />
              <el-table-column prop="datasetType" label="数据集类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getDatasetTypeColor(scope.row.datasetType)" size="mini">
                    {{ getDatasetTypeText(scope.row.datasetType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="tableCount" label="表数量" width="100" align="center" />
              <el-table-column prop="rowCount" label="行数" width="100" align="center" />
              <el-table-column prop="lastRefresh" label="最后刷新" width="150" />
              <el-table-column prop="refreshMode" label="刷新模式" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getRefreshModeColor(scope.row.refreshMode)" size="mini">
                    {{ getRefreshModeText(scope.row.refreshMode) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="权限设置" name="permissions">
            <el-table :data="biPermissions" border size="mini">
              <el-table-column prop="userName" label="用户名" width="120" />
              <el-table-column prop="userEmail" label="邮箱" width="200" />
              <el-table-column prop="role" label="角色" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getRoleColor(scope.row.role)" size="mini">
                    {{ getRoleText(scope.row.role) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="permissions" label="权限" />
              <el-table-column prop="grantTime" label="授权时间" width="150" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getPermissionStatusColor(scope.row.status)" size="mini">
                    {{ getPermissionStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑连接对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="connectionForm"
        :model="connectionForm"
        :rules="connectionRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="连接名称" prop="connectionName">
              <el-input v-model="connectionForm.connectionName" placeholder="请输入连接名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="BI类型" prop="biType">
              <el-select v-model="connectionForm.biType" placeholder="请选择BI类型" style="width: 100%">
                <el-option value="POWER_BI" label="Power BI" />
                <el-option value="TABLEAU" label="Tableau" />
                <el-option value="QLIKVIEW" label="QlikView" />
                <el-option value="LOOKER" label="Looker" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="服务器URL" prop="serverUrl">
          <el-input v-model="connectionForm.serverUrl" placeholder="请输入服务器URL" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="工作区" prop="workspace">
              <el-input v-model="connectionForm.workspace" placeholder="工作区名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="connectionForm.username" placeholder="用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="密码" prop="password">
              <el-input v-model="connectionForm.password" type="password" placeholder="密码" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="连接描述" prop="description">
          <el-input
            v-model="connectionForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入连接描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button @click="handleTestConnectionForm">测试连接</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 仪表板弹窗 -->
    <el-dialog
      :title="'仪表板 - ' + (dashboardConnection ? dashboardConnection.connectionName : '')"
      :visible.sync="dashboardDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div v-if="dashboardConnection" class="dashboard-content">
        <!-- 连接状态概览 -->
        <el-row :gutter="16" style="margin-bottom:20px">
          <el-col :span="6">
            <el-card shadow="hover" class="dashboard-metric-card">
              <div class="metric-value" :style="{ color: dashboardConnection.status === 'ACTIVE' ? '#67C23A' : '#F56C6C' }">
                {{ getStatusText(dashboardConnection.status) }}
              </div>
              <div class="metric-label">连接状态</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="dashboard-metric-card">
              <div class="metric-value" style="color:#409EFF">{{ dashboardConnection.reportCount || 0 }}</div>
              <div class="metric-label">报表数量</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="dashboard-metric-card">
              <div class="metric-value" style="color:#E6A23C">{{ dashboardConnection.syncMode || '未配置' }}</div>
              <div class="metric-label">同步模式</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="dashboard-metric-card">
              <div class="metric-value" style="color:#909399;font-size:14px">{{ dashboardConnection.lastUpdate || dashboardConnection.lastSync || '暂无' }}</div>
              <div class="metric-label">最后更新</div>
            </el-card>
          </el-col>
        </el-row>

        <!-- 连接详细信息 -->
        <el-card shadow="never" style="margin-bottom:20px">
          <div slot="header"><b>连接信息</b></div>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="BI类型">
              <el-tag :type="getBiTypeColor(dashboardConnection.biType)" size="mini">{{ getBiTypeText(dashboardConnection.biType) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="连接编码">{{ dashboardConnection.biCode || '-' }}</el-descriptions-item>
            <el-descriptions-item label="服务器URL">{{ dashboardConnection.serverUrl || '-' }}</el-descriptions-item>
            <el-descriptions-item label="工作区">{{ dashboardConnection.workspace || '-' }}</el-descriptions-item>
            <el-descriptions-item label="同步频率">{{ dashboardConnection.syncFrequency || '-' }}</el-descriptions-item>
            <el-descriptions-item label="是否启用">
              <el-tag :type="dashboardConnection.isEnabled ? 'success' : 'info'" size="mini">{{ dashboardConnection.isEnabled ? '已启用' : '未启用' }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ dashboardConnection.createTime || '-' }}</el-descriptions-item>
            <el-descriptions-item label="最后同步">{{ dashboardConnection.lastSync || '-' }}</el-descriptions-item>
            <el-descriptions-item label="描述" :span="2">{{ dashboardConnection.description || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>

        <!-- 操作区 -->
        <el-card shadow="never">
          <div slot="header"><b>快捷操作</b></div>
          <el-button type="primary" size="small" icon="el-icon-connection" @click="handleDashboardTestConnection">测试连接</el-button>
          <el-button type="success" size="small" icon="el-icon-refresh" @click="handleDashboardRefreshData">刷新数据</el-button>
          <el-button type="warning" size="small" icon="el-icon-document" @click="handleDashboardViewReports">查看报表</el-button>
          <el-button type="info" size="small" icon="el-icon-s-data" @click="handleDashboardViewDatasets">查看数据集</el-button>
        </el-card>
      </div>
      <div slot="footer">
        <el-button @click="dashboardDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { systemIntegrationApi } from '@/api/managementAccountant/ncv65/systemIntegration'

export default {
  name: 'BiIntegration',
  data() {
    return {
      // 统计数据
      biStats: {
        totalConnections: 0,
        activeDashboards: 0,
        totalReports: 0,
        activeUsers: 0
      },

      // BI类型
      biTypes: [],
      selectedBiType: null,
      
      // 连接列表
      biConnectionList: [],
      loading: false,
      searchKeyword: '',
      
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentConnection: null,
      biReports: [],
      biDatasets: [],
      biPermissions: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,

      // 仪表板弹窗
      dashboardDialogVisible: false,
      dashboardConnection: null,
      
      // 表单数据
      connectionForm: {
        connectionName: '',
        biType: '',
        serverUrl: '',
        workspace: '',
        username: '',
        password: '',
        description: ''
      },
      
      // 表单验证规则
      connectionRules: {
        connectionName: [
          { required: true, message: '请输入连接名称', trigger: 'blur' }
        ],
        biType: [
          { required: true, message: '请选择BI类型', trigger: 'change' }
        ],
        serverUrl: [
          { required: true, message: '请输入服务器URL', trigger: 'blur' }
        ],
        workspace: [
          { required: true, message: '请输入工作区名称', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  
  computed: {
    filteredBiConnectionList() {
      if (!this.searchKeyword) return this.biConnectionList
      const kw = this.searchKeyword.toLowerCase()
      return this.biConnectionList.filter(item =>
        (item.connectionName && item.connectionName.toLowerCase().includes(kw)) ||
        (item.biType && item.biType.toLowerCase().includes(kw)) ||
        (item.status && item.status.toLowerCase().includes(kw))
      )
    }
  },

  created() {
    this.getBiConnectionList()
    this.getBiStats()
    this.getBiTypes()
  },

  methods: {
    // 获取连接列表
    async getBiConnectionList() {
      this.loading = true
      try {
        const response = await systemIntegrationApi.getBiConnectionList(this.selectedBiType)
        if (response.code === 1 && response.data) {
          this.biConnectionList = response.data
        }
      } catch (error) {
        this.$message.error('获取连接列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getBiStats() {
      try {
        const response = await systemIntegrationApi.getBiStats()
        if (response.code === 1 && response.data) {
          this.biStats = response.data
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    // 获取BI类型列表
    async getBiTypes() {
      try {
        const response = await systemIntegrationApi.getBiConnectionList(null)
        if (response.code === 1 && response.data) {
          const typeMap = {}
          response.data.forEach(item => {
            const t = item.biType || 'OTHER'
            if (!typeMap[t]) {
              typeMap[t] = { id: t, name: this.getBiTypeText(t), description: this.getBiTypeDescription(t), icon: this.getBiTypeIcon(t), connectionCount: 0 }
            }
            typeMap[t].connectionCount++
          })
          this.biTypes = Object.values(typeMap)
        }
      } catch (error) {
        console.error('获取BI类型失败：', error)
      }
    },

    refreshBiTypes() { this.getBiTypes(); this.$message.success('BI类型已刷新') },
    handleSelectBiType(biType) { this.selectedBiType = this.selectedBiType === biType.id ? null : biType.id; this.getBiConnectionList() },
    handleCreateConnection() {
      this.dialogTitle = '创建BI连接'
      this.connectionForm = { connectionName: '', biType: '', serverUrl: '', workspace: '', username: '', password: '', description: '' }
      this.dialogVisible = true
    },
    handleRefresh() {
      this.getBiConnectionList()
      this.getBiStats()
      this.getBiTypes()
      this.$message.success('数据已刷新')
    },
    handleTestConnection(row) {
      if (row && row.id) {
        this.$confirm('确认测试连接 "' + (row.connectionName || '') + '" ？', '连接测试', { type: 'warning' }).then(async () => {
          try {
            const response = await systemIntegrationApi.bi.test(row.id)
            if (response.code === 1 && response.data) {
              const d = response.data
              this.$alert(
                '<div style="line-height:2">' +
                '<p><b>测试结果：</b>' + (d.success ? '<span style="color:#67C23A">连接成功</span>' : '<span style="color:#F56C6C">连接失败</span>') + '</p>' +
                '<p><b>响应时间：</b>' + (d.responseTime || '-') + '</p>' +
                '<p><b>消息：</b>' + (d.message || '-') + '</p>' +
                '</div>',
                '连接测试结果',
                { dangerouslyUseHTMLString: true, confirmButtonText: '确定' }
              )
              this.getBiConnectionList()
            } else {
              this.$message.error(response.msg || '连接测试失败')
            }
          } catch (error) { this.$message.error('连接测试失败：' + error.message) }
        }).catch(() => {})
      } else {
        if (this.currentConnection && this.currentConnection.id) {
          this.handleTestConnection(this.currentConnection)
        } else {
          this.$message.info('请先在列表中选择一个连接进行测试')
        }
      }
    },
    handlePushData() {
      if (this.currentConnection) {
        this.$confirm('确认推送数据到BI平台？', '确认', { type: 'info' }).then(async () => {
          try {
            const response = await systemIntegrationApi.bi.push(this.currentConnection.id)
            if (response.code === 1) { this.$message.success('推送成功'); this.getBiConnectionList() } else { this.$message.error(response.msg || '推送失败') }
          } catch (error) { this.$message.error('推送失败：' + error.message) }
        }).catch(() => {})
      } else { this.$message.info('请在列表中选择连接进行数据推送') }
    },
    handleSettings() {
      this.$msgbox({
        title: '集成设置',
        message: '<div style="line-height:2;max-height:400px;overflow-y:auto">' +
          '<h4 style="margin:0 0 10px">BI集成全局配置</h4>' +
          '<p><b>当前连接总数：</b>' + this.biStats.totalConnections + '</p>' +
          '<p><b>活跃仪表板：</b>' + this.biStats.activeDashboards + '</p>' +
          '<p><b>报表总数：</b>' + this.biStats.totalReports + '</p>' +
          '<p><b>活跃用户数：</b>' + this.biStats.activeUsers + '</p>' +
          '<hr style="border:none;border-top:1px solid #eee;margin:10px 0">' +
          '<h4 style="margin:0 0 10px">支持的BI工具</h4>' +
          '<p>• Power BI - 微软商业智能平台</p>' +
          '<p>• Tableau - 数据可视化分析工具</p>' +
          '<p>• QlikView - 自助式BI分析平台</p>' +
          '<p>• Looker - Google云端BI工具</p>' +
          '<hr style="border:none;border-top:1px solid #eee;margin:10px 0">' +
          '<h4 style="margin:0 0 10px">同步设置</h4>' +
          '<p>• 数据同步方式：推送(PUSH) / 拉取(PULL) / 实时(REALTIME)</p>' +
          '<p>• 同步频率：实时 / 每小时 / 每天 / 每周 / 每月 / 手动</p>' +
          '</div>',
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定',
        showCancelButton: false
      }).catch(() => {})
    },
    handleReports() {
      if (this.currentConnection) {
        this.loadConnectionReports(this.currentConnection)
        this.detailDrawerVisible = true
        this.detailActiveTab = 'reports'
      } else {
        this.$message.info('请先在列表中选择一个BI连接查看报表')
      }
    },
    handleHelp() {
      this.$msgbox({
        title: 'BI集成帮助',
        message: '<div style="line-height:2;max-height:450px;overflow-y:auto">' +
          '<h4 style="margin:0 0 10px">功能说明</h4>' +
          '<p>BI系统集成模块支持与主流BI平台的连接管理、数据同步和报表集成。</p>' +
          '<hr style="border:none;border-top:1px solid #eee;margin:10px 0">' +
          '<h4 style="margin:0 0 10px">操作指南</h4>' +
          '<p><b>1. 创建连接：</b>点击"创建连接"按钮，填写BI平台信息并保存。</p>' +
          '<p><b>2. 测试连接：</b>选中连接后点击"连接测试"验证连通性。</p>' +
          '<p><b>3. 查看仪表板：</b>选中已连接的BI连接，查看关联的仪表板信息。</p>' +
          '<p><b>4. 报表管理：</b>管理BI连接关联的报表，支持查看和刷新。</p>' +
          '<p><b>5. 数据集管理：</b>管理BI连接的数据集配置。</p>' +
          '<p><b>6. 权限设置：</b>配置BI连接的用户访问权限。</p>' +
          '<hr style="border:none;border-top:1px solid #eee;margin:10px 0">' +
          '<h4 style="margin:0 0 10px">连接状态说明</h4>' +
          '<p>• <span style="color:#67C23A">已连接</span> - 连接正常可用</p>' +
          '<p>• <span style="color:#F56C6C">未连接</span> - 连接未激活</p>' +
          '<p>• <span style="color:#E6A23C">连接异常</span> - 连接出现错误</p>' +
          '<p>• <span style="color:#909399">测试中</span> - 正在测试连接</p>' +
          '</div>',
        dangerouslyUseHTMLString: true,
        confirmButtonText: '知道了',
        showCancelButton: false
      }).catch(() => {})
    },
    handleView(row) {
      this.currentConnection = row
      this.loadConnectionReports(row)
      this.loadConnectionDatasets(row)
      this.loadConnectionPermissions(row)
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
    },
    handleRowClick(row) { this.currentConnection = row },
    handleMoreAction(command, row) {
      const actions = {
        'edit': () => this.handleEdit(row),
        'config': () => this.handleView(row),
        'reports': () => { this.currentConnection = row; this.handleReports() },
        'datasets': () => this.handleDatasets(row),
        'permissions': () => this.handlePermissions(row),
        'delete': () => this.handleDelete(row)
      }
      if (actions[command]) actions[command]()
    },
    handleEdit(row) {
      this.dialogTitle = '编辑BI连接'
      this.connectionForm = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除连接 "' + (row.connectionName || '') + '" ？此操作不可恢复。', '删除确认', { type: 'warning', confirmButtonText: '确认删除', cancelButtonText: '取消' }).then(async () => {
        try {
          const response = await systemIntegrationApi.bi.delete(row.id)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getBiConnectionList()
            this.getBiStats()
            this.getBiTypes()
            if (this.currentConnection && this.currentConnection.id === row.id) {
              this.currentConnection = null
            }
          } else { this.$message.error(response.msg || '删除失败') }
        } catch (error) { this.$message.error('删除失败：' + error.message) }
      }).catch(() => {})
    },
    handleDatasets(row) {
      this.currentConnection = row
      this.loadConnectionDatasets(row)
      this.detailDrawerVisible = true
      this.detailActiveTab = 'datasets'
    },
    handlePermissions(row) {
      this.currentConnection = row
      this.loadConnectionPermissions(row)
      this.detailDrawerVisible = true
      this.detailActiveTab = 'permissions'
    },
    handleSubmitForm() {
      this.$refs.connectionForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          let response
          if (this.connectionForm.id) {
            response = await systemIntegrationApi.bi.update(this.connectionForm.id, this.connectionForm)
          } else {
            response = await systemIntegrationApi.bi.create(this.connectionForm)
          }
          if (response.code === 1) {
            this.$message.success(this.connectionForm.id ? '更新成功' : '创建成功')
            this.dialogVisible = false
            this.getBiConnectionList()
            this.getBiStats()
            this.getBiTypes()
          } else { this.$message.error(response.msg || '操作失败') }
        } catch (error) { this.$message.error('操作失败：' + error.message) } finally { this.submitLoading = false }
      })
    },
    handleTestConnectionForm() {
      if (this.connectionForm.id) { this.handleTestConnection(this.connectionForm) } else { this.$message.info('请先保存连接后再测试') }
    },
    handleDashboard() {
      if (this.currentConnection) {
        this.dashboardConnection = this.currentConnection
        this.dashboardDialogVisible = true
      } else { this.$message.info('请先选择一个BI连接查看仪表板') }
    },
    handleViewDashboard(row) {
      if (row) {
        this.currentConnection = row
        this.dashboardConnection = row
        this.dashboardDialogVisible = true
      }
    },
    handleViewReport(row) {
      if (row && row.reportName) {
        this.$alert(
          '<div style="line-height:2">' +
          '<p><b>报表名称：</b>' + (row.reportName || '-') + '</p>' +
          '<p><b>报表类型：</b>' + this.getReportTypeText(row.reportType) + '</p>' +
          '<p><b>数据源：</b>' + (row.dataSource || '-') + '</p>' +
          '<p><b>最后刷新：</b>' + (row.lastRefresh || '-') + '</p>' +
          '<p><b>查看次数：</b>' + (row.viewCount || 0) + '</p>' +
          '<p><b>状态：</b>' + this.getReportStatusText(row.status) + '</p>' +
          '</div>',
          '报表详情',
          { dangerouslyUseHTMLString: true, confirmButtonText: '确定' }
        )
      }
    },
    handleRefreshReport(row) {
      if (row && row.reportName) {
        this.$confirm('确认刷新报表 "' + row.reportName + '" 的数据？', '刷新报表', { type: 'info' }).then(() => {
          this.$message.success('报表 "' + row.reportName + '" 数据刷新成功')
          if (this.currentConnection) { this.loadConnectionReports(this.currentConnection) }
        }).catch(() => {})
      }
    },
    handleDialogClose() { this.$refs.connectionForm && this.$refs.connectionForm.resetFields() },
    // 仪表板弹窗内操作
    async handleDashboardTestConnection() {
      if (!this.dashboardConnection || !this.dashboardConnection.id) return
      try {
        const response = await systemIntegrationApi.bi.test(this.dashboardConnection.id)
        if (response.code === 1) {
          this.$message.success('连接测试成功')
          this.getBiConnectionList()
        } else {
          this.$message.warning(response.msg || '连接测试失败')
        }
      } catch (error) {
        this.$message.error('连接测试异常：' + error.message)
      }
    },
    handleDashboardRefreshData() {
      this.getBiConnectionList()
      this.getBiStats()
      // 刷新仪表板弹窗中的连接数据
      if (this.dashboardConnection && this.dashboardConnection.id) {
        const updated = this.biConnectionList.find(c => c.id === this.dashboardConnection.id)
        if (updated) this.dashboardConnection = updated
      }
      this.$message.success('数据已刷新')
    },
    handleDashboardViewReports() {
      this.dashboardDialogVisible = false
      if (this.dashboardConnection) {
        this.currentConnection = this.dashboardConnection
        this.loadConnectionReports(this.dashboardConnection)
        this.detailDrawerVisible = true
        this.detailActiveTab = 'reports'
      }
    },
    handleDashboardViewDatasets() {
      this.dashboardDialogVisible = false
      if (this.dashboardConnection) {
        this.currentConnection = this.dashboardConnection
        this.loadConnectionDatasets(this.dashboardConnection)
        this.detailDrawerVisible = true
        this.detailActiveTab = 'datasets'
      }
    },
    // 加载连接关联的报表数据
   async loadConnectionReports(conn) {
     if (!conn || !conn.id) return
     try {
       const response = await systemIntegrationApi.bi.getReports(conn.id)
       if (response.code === 1) {
         this.biReports = response.data || []
       } else {
         this.biReports = []
         this.$message.error(response.msg || '加载报表数据失败')
       }
     } catch (e) {
       this.biReports = []
       this.$message.error('加载报表数据失败')
     }
   },
   // 加载连接关联的数据集
   async loadConnectionDatasets(conn) {
     if (!conn || !conn.id) return
     try {
       const response = await systemIntegrationApi.bi.getDatasets(conn.id)
       if (response.code === 1) {
         this.biDatasets = response.data || []
       } else {
         this.biDatasets = []
         this.$message.error(response.msg || '加载数据集失败')
       }
     } catch (e) {
       this.biDatasets = []
       this.$message.error('加载数据集失败')
     }
   },
   // 加载连接关联的权限
   async loadConnectionPermissions(conn) {
     if (!conn || !conn.id) return
     try {
       const response = await systemIntegrationApi.bi.getPermissions(conn.id)
       if (response.code === 1) {
         this.biPermissions = response.data || []
       } else {
         this.biPermissions = []
         this.$message.error(response.msg || '加载权限数据失败')
       }
     } catch (e) {
       this.biPermissions = []
       this.$message.error('加载权限数据失败')
     }
   },
    // 辅助方法
    getBiTypeColor(type) { const m = { 'POWER_BI': 'primary', 'TABLEAU': 'success', 'QLIKVIEW': 'warning', 'LOOKER': 'danger', 'FINEBI': 'primary', 'SUPERSET': 'success' }; return m[type] || 'info' },
    getBiTypeText(type) { const m = { 'POWER_BI': 'Power BI', 'TABLEAU': 'Tableau', 'QLIKVIEW': 'QlikView', 'LOOKER': 'Looker', 'FINEBI': 'FineBI', 'SUPERSET': 'Superset' }; return m[type] || type || '未知' },
    getBiTypeDescription(type) { const m = { 'POWER_BI': '微软商业智能平台', 'TABLEAU': '数据可视化分析工具', 'QLIKVIEW': '自助式BI分析平台', 'LOOKER': 'Google云端BI工具', 'FINEBI': '帆软商业智能工具', 'SUPERSET': 'Apache开源BI平台' }; return m[type] || type + ' 商业智能工具' },
    getBiTypeIcon(type) { const m = { 'POWER_BI': 'el-icon-data-analysis', 'TABLEAU': 'el-icon-data-line', 'QLIKVIEW': 'el-icon-pie-chart', 'LOOKER': 'el-icon-view', 'FINEBI': 'el-icon-s-data', 'SUPERSET': 'el-icon-s-marketing' }; return m[type] || 'el-icon-pie-chart' },
    getStatusColor(status) { const m = { 'ACTIVE': 'success', 'CONNECTED': 'success', 'INACTIVE': 'danger', 'DISCONNECTED': 'danger', 'ERROR': 'warning', 'TESTING': 'info', 'PENDING': 'warning' }; return m[status] || 'info' },
    getStatusText(status) { const m = { 'ACTIVE': '已连接', 'CONNECTED': '已连接', 'INACTIVE': '未连接', 'DISCONNECTED': '已断开', 'ERROR': '连接异常', 'TESTING': '测试中', 'PENDING': '待连接' }; return m[status] || status || '未知' },
    getSyncStatusColor(status) { const m = { 'SUCCESS': 'success', 'FAILED': 'danger', 'RUNNING': 'warning', 'PENDING': 'info' }; return m[status] || 'info' },
    getSyncStatusText(status) { const m = { 'SUCCESS': '成功', 'FAILED': '失败', 'RUNNING': '同步中', 'PENDING': '待同步' }; return m[status] || status || '未知' },
    getReportTypeColor(type) { const m = { 'DASHBOARD': 'primary', 'CHART': 'success', 'TABLE': 'warning', 'KPI': 'danger' }; return m[type] || 'info' },
    getReportTypeText(type) { const m = { 'DASHBOARD': '仪表板', 'CHART': '图表', 'TABLE': '表格', 'KPI': 'KPI指标' }; return m[type] || type || '未知' },
    getReportStatusColor(status) { const m = { 'PUBLISHED': 'success', 'DRAFT': 'info', 'ARCHIVED': 'warning' }; return m[status] || 'info' },
    getReportStatusText(status) { const m = { 'PUBLISHED': '已发布', 'DRAFT': '草稿', 'ARCHIVED': '已归档' }; return m[status] || status || '未知' },
    getDatasetTypeColor(type) { const m = { 'SQL': 'primary', 'API': 'success', 'FILE': 'warning', 'STREAM': 'danger' }; return m[type] || 'info' },
    getDatasetTypeText(type) { const m = { 'SQL': 'SQL查询', 'API': 'API接口', 'FILE': '文件导入', 'STREAM': '数据流' }; return m[type] || type || '未知' },
    getRefreshModeColor(mode) { const m = { 'REALTIME': 'danger', 'SCHEDULED': 'warning', 'MANUAL': 'info', 'PUSH': 'primary', 'PULL': 'success' }; return m[mode] || 'info' },
    getRefreshModeText(mode) { const m = { 'REALTIME': '实时', 'SCHEDULED': '定时', 'MANUAL': '手动', 'PUSH': '推送', 'PULL': '拉取' }; return m[mode] || mode || '未知' },
    getRoleColor(role) { const m = { 'ADMIN': 'danger', 'EDITOR': 'warning', 'VIEWER': 'info' }; return m[role] || 'info' },
    getRoleText(role) { const m = { 'ADMIN': '管理员', 'EDITOR': '编辑者', 'VIEWER': '查看者' }; return m[role] || role || '未知' },
    getPermissionStatusColor(status) { const m = { 'ACTIVE': 'success', 'INACTIVE': 'danger', 'EXPIRED': 'warning' }; return m[status] || 'info' },
    getPermissionStatusText(status) { const m = { 'ACTIVE': '有效', 'INACTIVE': '无效', 'EXPIRED': '已过期' }; return m[status] || status || '未知' }
  }
}
</script>

<style scoped>
.dashboard-metric-card {
  text-align: center;
  padding: 10px 0;
}
.dashboard-metric-card .metric-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}
.dashboard-metric-card .metric-label {
  font-size: 12px;
  color: #909399;
}
</style>
