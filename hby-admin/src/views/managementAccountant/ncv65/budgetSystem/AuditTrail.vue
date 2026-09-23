<template>
  <div class="audit-trail">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>审计跟踪</h2>
      <p>记录和查看预算系统中所有用户操作、数据变更和系统事件的详细审计日志</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="success" icon="el-icon-download" @click="handleExport">导出日志</el-button>
            <el-button type="warning" icon="el-icon-delete" @click="handleCleanup">清理日志</el-button>
            <el-button type="info" icon="el-icon-setting" @click="handleSettings">审计设置</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-data-analysis" @click="handleAnalysis">日志分析</el-button>
            <el-button icon="el-icon-warning" @click="handleAlerts">异常告警</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 审计统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ auditStats.totalLogs }}</div>
            <div class="stat-label">总日志数</div>
            <div class="stat-description">系统记录的审计日志</div>
            <div class="stat-trend">
              <i class="el-icon-document"></i>
              <span>完整记录</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-document"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card today-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ auditStats.todayLogs }}</div>
            <div class="stat-label">今日日志</div>
            <div class="stat-description">今日新增的日志</div>
            <div class="stat-trend">
              <i class="el-icon-time"></i>
              <span>实时记录</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-time"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card users-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ auditStats.activeUsers }}</div>
            <div class="stat-label">活跃用户</div>
            <div class="stat-description">今日活跃用户数</div>
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
      <el-col :span="6">
        <el-card class="stat-card risk-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ auditStats.riskEvents }}</div>
            <div class="stat-label">风险事件</div>
            <div class="stat-description">需要关注的风险</div>
            <div class="stat-trend">
              <i class="el-icon-warning"></i>
              <span>需要关注</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="操作类型">
          <el-select v-model="queryForm.operationType" placeholder="请选择操作类型" clearable>
            <el-option label="登录" value="LOGIN" />
            <el-option label="登出" value="LOGOUT" />
            <el-option label="查询" value="QUERY" />
            <el-option label="新增" value="CREATE" />
            <el-option label="修改" value="UPDATE" />
            <el-option label="删除" value="DELETE" />
            <el-option label="导入" value="IMPORT" />
            <el-option label="导出" value="EXPORT" />
            <el-option label="审批" value="APPROVE" />
            <el-option label="拒绝" value="REJECT" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作模块">
          <el-select v-model="queryForm.module" placeholder="请选择操作模块" clearable>
            <el-option label="预算编制" value="BUDGET_PREPARATION" />
            <el-option label="预算分析" value="BUDGET_ANALYSIS" />
            <el-option label="预算控制" value="BUDGET_CONTROL" />
            <el-option label="预算体系" value="BUDGET_SYSTEM" />
            <el-option label="用户管理" value="USER_MANAGEMENT" />
            <el-option label="权限管理" value="PERMISSION_MANAGEMENT" />
            <el-option label="系统配置" value="SYSTEM_CONFIG" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作用户">
          <el-input v-model="queryForm.operator" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="IP地址">
          <el-input v-model="queryForm.ipAddress" placeholder="请输入IP地址" clearable />
        </el-form-item>
        <el-form-item label="操作时间">
          <el-date-picker
            v-model="queryForm.operationTime"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="风险级别">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择风险级别" clearable>
            <el-option label="低风险" value="LOW" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="高风险" value="HIGH" />
            <el-option label="严重风险" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 审计日志列表 -->
    <el-card class="audit-list-card" shadow="never">
      <div slot="header" class="card-header">
        <span>审计日志</span>
        <div class="header-tools">
          <el-button icon="el-icon-refresh" size="mini" @click="getAuditList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="auditList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="operationTime" label="操作时间" width="150" align="center" />
        <el-table-column prop="operatorName" label="操作用户" width="120" align="center" />
        <el-table-column prop="operationType" label="操作类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getOperationTypeColor(scope.row.operationType)" size="mini">
              {{ getOperationTypeText(scope.row.operationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="moduleName" label="操作模块" width="120" align="center">
          <template slot-scope="scope">
            <el-tag type="info" size="mini">
              {{ getModuleText(scope.row.moduleName) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationDescription" label="操作描述" show-overflow-tooltip />
        <el-table-column prop="ipAddress" label="IP地址" width="120" align="center" />
        <el-table-column prop="userAgent" label="浏览器" width="150" show-overflow-tooltip />
        <el-table-column prop="riskLevel" label="风险级别" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelColor(scope.row.riskLevel)" size="mini">
              {{ getRiskLevelText(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="result" label="操作结果" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.result === 'SUCCESS' ? 'success' : 'danger'" size="mini">
              {{ scope.row.result === 'SUCCESS' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </el-card>

    <!-- 审计详情抽屉 -->
    <el-drawer
      title="审计详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="50%"
    >
      <div class="detail-content" v-if="currentAudit">
        <el-descriptions title="基本信息" :column="2" border>
          <el-descriptions-item label="操作时间">{{ currentAudit.operationTime }}</el-descriptions-item>
          <el-descriptions-item label="操作用户">{{ currentAudit.operatorName }}</el-descriptions-item>
          <el-descriptions-item label="操作类型">
            <el-tag :type="getOperationTypeColor(currentAudit.operationType)" size="mini">
              {{ getOperationTypeText(currentAudit.operationType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="操作模块">
            <el-tag type="info" size="mini">
              {{ getModuleText(currentAudit.moduleName) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="IP地址">{{ currentAudit.ipAddress }}</el-descriptions-item>
          <el-descriptions-item label="风险级别">
            <el-tag :type="getRiskLevelColor(currentAudit.riskLevel)" size="mini">
              {{ getRiskLevelText(currentAudit.riskLevel) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="操作结果">
            <el-tag :type="currentAudit.result === 'SUCCESS' ? 'success' : 'danger'" size="mini">
              {{ currentAudit.result === 'SUCCESS' ? '成功' : '失败' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="会话ID">{{ currentAudit.sessionId }}</el-descriptions-item>
          <el-descriptions-item label="操作描述" :span="2">{{ currentAudit.operationDescription }}</el-descriptions-item>
          <el-descriptions-item label="浏览器信息" :span="2">{{ currentAudit.userAgent }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">操作详情</el-divider>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="请求URL">{{ currentAudit.requestUrl }}</el-descriptions-item>
          <el-descriptions-item label="请求方法">{{ currentAudit.requestMethod }}</el-descriptions-item>
          <el-descriptions-item label="请求参数">
            <pre>{{ formatJson(currentAudit.requestParams) }}</pre>
          </el-descriptions-item>
          <el-descriptions-item label="响应结果">
            <pre>{{ formatJson(currentAudit.responseData) }}</pre>
          </el-descriptions-item>
          <el-descriptions-item label="执行时间">{{ currentAudit.executionTime }}ms</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">数据变更</el-divider>
        <el-table :data="currentAudit.dataChanges" border size="mini" v-if="currentAudit.dataChanges">
          <el-table-column prop="fieldName" label="字段名称" width="150" />
          <el-table-column prop="oldValue" label="原值" />
          <el-table-column prop="newValue" label="新值" />
          <el-table-column prop="changeType" label="变更类型" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getChangeTypeColor(scope.row.changeType)" size="mini">
                {{ getChangeTypeText(scope.row.changeType) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-drawer>

    <!-- 清理日志对话框 -->
    <el-dialog title="清理历史日志" :visible.sync="cleanupDialogVisible" width="450px">
      <el-form label-width="100px">
        <el-form-item label="保留天数">
          <el-input-number v-model="cleanupForm.retentionDays" :min="7" :max="365" :step="30" />
        </el-form-item>
        <el-alert type="warning" :closable="false" show-icon style="margin-top: 10px">
          将清理 {{ cleanupForm.retentionDays }} 天前的审计日志，此操作不可撤销。
        </el-alert>
      </el-form>
      <div slot="footer">
        <el-button @click="cleanupDialogVisible = false">取消</el-button>
        <el-button type="danger" :loading="cleanupLoading" @click="doCleanup">确认清理</el-button>
      </div>
    </el-dialog>

    <!-- 审计设置对话框 -->
    <el-dialog title="审计设置" :visible.sync="settingsDialogVisible" width="750px">
      <el-button type="primary" size="small" icon="el-icon-plus" style="margin-bottom: 12px" @click="handleAddSetting">新增设置</el-button>
      <el-table :data="settingsList" border size="small" v-loading="settingsLoading">
        <el-table-column prop="settingName" label="设置名称" width="150" />
        <el-table-column prop="settingKey" label="设置键" width="180" />
        <el-table-column prop="settingValue" label="设置值" />
        <el-table-column prop="settingType" label="类型" width="100" />
        <el-table-column prop="isEnabled" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'info'" size="mini">
              {{ scope.row.isEnabled === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="handleEditSetting(scope.row)">编辑</el-button>
            <el-button type="text" size="mini" style="color: #F56C6C" @click="handleDeleteSetting(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 设置编辑子对话框 -->
      <el-dialog title="编辑设置" :visible.sync="settingEditVisible" width="500px" append-to-body>
        <el-form :model="settingForm" label-width="90px" size="small">
          <el-form-item label="设置名称"><el-input v-model="settingForm.settingName" /></el-form-item>
          <el-form-item label="设置键"><el-input v-model="settingForm.settingKey" :disabled="!!settingForm.settingId" /></el-form-item>
          <el-form-item label="设置值"><el-input v-model="settingForm.settingValue" type="textarea" :rows="2" /></el-form-item>
          <el-form-item label="类型"><el-input v-model="settingForm.settingType" /></el-form-item>
          <el-form-item label="描述"><el-input v-model="settingForm.description" type="textarea" :rows="2" /></el-form-item>
          <el-form-item label="启用">
            <el-switch v-model="settingForm.isEnabled" :active-value="1" :inactive-value="0" />
          </el-form-item>
        </el-form>
        <div slot="footer">
          <el-button @click="settingEditVisible = false">取消</el-button>
          <el-button type="primary" @click="doSaveSetting">保存</el-button>
        </div>
      </el-dialog>
    </el-dialog>

    <!-- 日志分析对话框 -->
    <el-dialog title="日志分析" :visible.sync="analysisDialogVisible" width="750px" v-loading="analysisLoading">
      <el-row :gutter="20" v-if="analysisData">
        <el-col :span="12">
          <h4>操作类型分布</h4>
          <el-table :data="analysisData.operationTypeStats || []" border size="mini">
            <el-table-column prop="type" label="操作类型">
              <template slot-scope="scope">{{ getOperationTypeText(scope.row.type) }}</template>
            </el-table-column>
            <el-table-column prop="count" label="数量" width="80" align="center" />
          </el-table>
        </el-col>
        <el-col :span="12">
          <h4>模块分布</h4>
          <el-table :data="analysisData.moduleStats || []" border size="mini">
            <el-table-column prop="module" label="模块">
              <template slot-scope="scope">{{ getModuleText(scope.row.module) }}</template>
            </el-table-column>
            <el-table-column prop="count" label="数量" width="80" align="center" />
          </el-table>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px" v-if="analysisData">
        <el-col :span="12">
          <h4>风险级别分布</h4>
          <el-table :data="analysisData.riskLevelStats || []" border size="mini">
            <el-table-column prop="level" label="风险级别">
              <template slot-scope="scope">{{ getRiskLevelText(scope.row.level) }}</template>
            </el-table-column>
            <el-table-column prop="count" label="数量" width="80" align="center" />
          </el-table>
        </el-col>
        <el-col :span="12">
          <h4>操作结果统计</h4>
          <div v-if="analysisData.resultStats" style="padding: 10px">
            <p>成功：<el-tag type="success" size="small">{{ analysisData.resultStats.success }}</el-tag></p>
            <p>失败：<el-tag type="danger" size="small">{{ analysisData.resultStats.failure }}</el-tag></p>
          </div>
        </el-col>
      </el-row>
    </el-dialog>

    <!-- 异常告警对话框 -->
    <el-dialog title="异常告警" :visible.sync="alertsDialogVisible" width="850px">
      <el-row :gutter="16" style="margin-bottom: 16px" v-if="alertStatsData">
        <el-col :span="6">
          <el-card shadow="never" class="alert-stat-card"><div class="alert-stat-value">{{ alertStatsData.total || 0 }}</div><div class="alert-stat-label">总告警</div></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="never" class="alert-stat-card"><div class="alert-stat-value" style="color:#F56C6C">{{ alertStatsData.pending || 0 }}</div><div class="alert-stat-label">待处理</div></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="never" class="alert-stat-card"><div class="alert-stat-value" style="color:#E6A23C">{{ alertStatsData.processing || 0 }}</div><div class="alert-stat-label">处理中</div></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="never" class="alert-stat-card"><div class="alert-stat-value" style="color:#67C23A">{{ alertStatsData.resolved || 0 }}</div><div class="alert-stat-label">已解决</div></el-card>
        </el-col>
      </el-row>
      <el-table :data="alertList" border size="small" v-loading="alertsLoading">
        <el-table-column prop="alertTitle" label="告警标题" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.alertTitle || scope.row.alertContent || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="alertType" label="类型" width="100" />
        <el-table-column prop="alertLevel" label="级别" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelColor(scope.row.alertLevel)" size="mini">{{ scope.row.alertLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertStatus" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.alertStatus === 'RESOLVED' ? 'success' : scope.row.alertStatus === 'PENDING' ? 'danger' : 'warning'" size="mini">
              {{ scope.row.alertStatus === 'PENDING' ? '待处理' : scope.row.alertStatus === 'PROCESSING' ? '处理中' : '已解决' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" />
        <el-table-column label="操作" width="100" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.alertStatus !== 'RESOLVED'" type="text" size="mini" @click="handleAlertResolve(scope.row)">处理</el-button>
            <span v-else style="color: #909399; font-size: 12px">已处理</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="审计跟踪帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>审计跟踪模块记录预算系统中所有用户操作、数据变更和系统事件的详细日志。</p>
        <h4>操作指南</h4>
        <ul>
          <li><b>查询</b>：通过操作类型、模块、用户、时间等条件筛选日志</li>
          <li><b>导出</b>：将筛选后的日志导出为CSV文件</li>
          <li><b>清理</b>：清理指定天数前的历史日志</li>
          <li><b>设置</b>：配置审计相关参数</li>
          <li><b>分析</b>：查看日志统计分析</li>
          <li><b>告警</b>：查看和处理异常告警</li>
        </ul>
        <h4>风险级别说明</h4>
        <ul>
          <li><el-tag type="success" size="mini">低风险</el-tag> 常规操作，如查询、登录</li>
          <li><el-tag type="warning" size="mini">中风险</el-tag> 数据修改操作</li>
          <li><el-tag type="danger" size="mini">高风险</el-tag> 删除、权限变更等敏感操作</li>
          <li><el-tag type="danger" size="mini">严重</el-tag> 系统级异常或安全事件</li>
        </ul>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetSystemApi } from '@/api/managementAccountant/ncv65/budgetSystem'

export default {
  name: 'AuditTrail',
  data() {
    return {
      // 统计数据
      auditStats: {
        totalLogs: 0,
        todayLogs: 0,
        activeUsers: 0,
        riskEvents: 0
      },

      // 查询条件
      queryForm: {
        operationType: '',
        module: '',
        operator: '',
        ipAddress: '',
        operationTime: [],
        riskLevel: ''
      },

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },

      // 审计日志列表
      auditList: [],
      total: 0,
      loading: false,

      // 详情抽屉
      detailDrawerVisible: false,
      currentAudit: null,

      // 清理日志
      cleanupDialogVisible: false,
      cleanupLoading: false,
      cleanupForm: { retentionDays: 90 },

      // 审计设置
      settingsDialogVisible: false,
      settingsLoading: false,
      settingsList: [],
      settingEditVisible: false,
      settingForm: {},

      // 日志分析
      analysisDialogVisible: false,
      analysisLoading: false,
      analysisData: null,

      // 异常告警
      alertsDialogVisible: false,
      alertsLoading: false,
      alertList: [],
      alertStatsData: null,

      // 帮助
      helpDialogVisible: false
    }
  },

  created() {
    this.getAuditList()
    this.getAuditStats()
  },

  methods: {
    // 获取审计日志列表
    async getAuditList() {
      this.loading = true
      try {
        const params = {
          ...this.queryParams,
          ...this.queryForm
        }
        const response = await budgetSystemApi.getAuditTrailList(params)
        if (response.code === 1 && response.data) {
          this.auditList = response.data.list || response.data.tlist || []
          this.total = response.data.total || response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('获取审计日志失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getAuditStats() {
      try {
        const response = await budgetSystemApi.getAuditStats()
        if (response.code === 1 && response.data) {
          this.auditStats = response.data
        }
      } catch (error) {
        this.$message.error('获取统计数据失败')
      }
    },

    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getAuditList()
    },

    // 重置
    handleReset() {
      this.queryForm = {
        operationType: '',
        module: '',
        operator: '',
        ipAddress: '',
        operationTime: [],
        riskLevel: ''
      }
      this.queryParams.pageNum = 1
      this.getAuditList()
    },

    // 查看详情
    async handleView(row) {
      try {
        const response = await budgetSystemApi.getAuditDetail(row.auditId)
        this.currentAudit = response.data
        this.detailDrawerVisible = true
      } catch (error) {
        this.$message.error('获取详情失败：' + error.message)
      }
    },

    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getAuditList()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getAuditList()
    },

    // 刷新
    handleRefresh() {
      this.getAuditList()
      this.getAuditStats()
    },

    // 导出日志
    async handleExport() {
      try {
        const params = { ...this.queryForm }
        const response = await budgetSystemApi.exportAuditLogs(params)
        const blob = new Blob([response], { type: 'text/csv;charset=utf-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = 'audit_logs_' + new Date().getTime() + '.csv'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 清理日志
    handleCleanup() {
      this.cleanupForm.retentionDays = 90
      this.cleanupDialogVisible = true
    },

    // 执行清理
    async doCleanup() {
      this.cleanupLoading = true
      try {
        const response = await budgetSystemApi.cleanupAuditLogs({ retentionDays: this.cleanupForm.retentionDays })
        if (response.code === 1) {
          this.$message.success(response.msg || '清理成功')
          this.cleanupDialogVisible = false
          this.getAuditList()
          this.getAuditStats()
        } else {
          this.$message.error(response.msg || '清理失败')
        }
      } catch (error) {
        this.$message.error('清理失败：' + error.message)
      } finally {
        this.cleanupLoading = false
      }
    },

    // 审计设置
    async handleSettings() {
      this.settingsDialogVisible = true
      this.settingsLoading = true
      try {
        const response = await budgetSystemApi.getAuditSettings()
        if (response.code === 1) {
          this.settingsList = response.data || []
        }
      } catch (error) {
        this.$message.error('获取设置失败')
      } finally {
        this.settingsLoading = false
      }
    },

    // 新增设置
    handleAddSetting() {
      this.settingForm = { settingName: '', settingKey: '', settingValue: '', settingType: 'GENERAL', description: '', isEnabled: 1 }
      this.settingEditVisible = true
    },

    // 编辑设置
    handleEditSetting(row) {
      this.settingForm = { ...row }
      this.settingEditVisible = true
    },

    // 保存设置
    async doSaveSetting() {
      try {
        const response = await budgetSystemApi.saveAuditSetting(this.settingForm)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.settingEditVisible = false
          this.handleSettings()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    // 删除设置
    handleDeleteSetting(row) {
      this.$confirm('确认删除该设置？', '提示', { type: 'warning' }).then(async () => {
        try {
          const response = await budgetSystemApi.deleteAuditSetting(row.settingId)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.handleSettings()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      }).catch(() => {})
    },

    // 日志分析
    async handleAnalysis() {
      this.analysisDialogVisible = true
      this.analysisLoading = true
      try {
        const response = await budgetSystemApi.getLogAnalysis()
        if (response.code === 1) {
          this.analysisData = response.data
        }
      } catch (error) {
        this.$message.error('获取分析数据失败')
      } finally {
        this.analysisLoading = false
      }
    },

    // 异常告警
    async handleAlerts() {
      this.alertsDialogVisible = true
      this.alertsLoading = true
      try {
        const [listRes, statsRes] = await Promise.all([
          budgetSystemApi.getAuditAlertList({ pageNum: 1, pageSize: 50 }),
          budgetSystemApi.getAlertStats()
        ])
        if (listRes.code === 1) {
          this.alertList = (listRes.data && (listRes.data.list || listRes.data.tlist)) || []
        }
        if (statsRes.code === 1) {
          this.alertStatsData = statsRes.data
        }
      } catch (error) {
        this.$message.error('获取告警数据失败')
      } finally {
        this.alertsLoading = false
      }
    },

    // 处理告警
    async handleAlertResolve(row) {
      try {
        const response = await budgetSystemApi.handleAlert(row.alertId, {
          handlerId: 'current_user',
          handlerName: '当前用户',
          handleRemark: '已处理',
          alertStatus: 'RESOLVED'
        })
        if (response.code === 1) {
          this.$message.success('处理成功')
          this.handleAlerts()
        } else {
          this.$message.error(response.msg || '处理失败')
        }
      } catch (error) {
        this.$message.error('处理失败：' + error.message)
      }
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 格式化JSON
    formatJson(jsonStr) {
      try {
        return JSON.stringify(JSON.parse(jsonStr), null, 2)
      } catch (e) {
        return jsonStr
      }
    },

    // 获取操作类型颜色
    getOperationTypeColor(type) {
      const colorMap = {
        'LOGIN': 'success',
        'LOGOUT': 'info',
        'QUERY': 'primary',
        'CREATE': 'success',
        'UPDATE': 'warning',
        'DELETE': 'danger',
        'IMPORT': 'info',
        'EXPORT': 'warning',
        'APPROVE': 'success',
        'REJECT': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 获取操作类型文本
    getOperationTypeText(type) {
      const textMap = {
        'LOGIN': '登录',
        'LOGOUT': '登出',
        'QUERY': '查询',
        'CREATE': '新增',
        'UPDATE': '修改',
        'DELETE': '删除',
        'IMPORT': '导入',
        'EXPORT': '导出',
        'APPROVE': '审批',
        'REJECT': '拒绝'
      }
      return textMap[type] || type
    },

    // 获取模块文本
    getModuleText(module) {
      const textMap = {
        'BUDGET_PREPARATION': '预算编制',
        'BUDGET_ANALYSIS': '预算分析',
        'BUDGET_CONTROL': '预算控制',
        'BUDGET_SYSTEM': '预算体系',
        'USER_MANAGEMENT': '用户管理',
        'PERMISSION_MANAGEMENT': '权限管理',
        'SYSTEM_CONFIG': '系统配置'
      }
      return textMap[module] || module
    },

    // 获取风险级别颜色
    getRiskLevelColor(level) {
      const colorMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return colorMap[level] || 'info'
    },

    // 获取风险级别文本
    getRiskLevelText(level) {
      const textMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '严重风险'
      }
      return textMap[level] || level
    },

    // 获取变更类型颜色
    getChangeTypeColor(type) {
      const colorMap = {
        'INSERT': 'success',
        'UPDATE': 'warning',
        'DELETE': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 获取变更类型文本
    getChangeTypeText(type) {
      const textMap = {
        'INSERT': '新增',
        'UPDATE': '修改',
        'DELETE': '删除'
      }
      return textMap[type] || type
    }
  }
}
</script>


<style scoped>
.audit-trail { padding: 16px; }
.page-header { margin-bottom: 16px; }
.page-header h2 { margin: 0 0 4px; font-size: 20px; }
.page-header p { margin: 0; color: #909399; font-size: 13px; }
.toolbar-card { margin-bottom: 16px; }
.text-right { text-align: right; }
.stats-row { margin-bottom: 16px; }
.stat-card { position: relative; overflow: hidden; }
.stat-content { position: relative; z-index: 1; }
.stat-number { font-size: 28px; font-weight: bold; color: #303133; }
.stat-label { font-size: 14px; color: #606266; margin-top: 4px; }
.stat-description { font-size: 12px; color: #909399; margin-top: 2px; }
.stat-trend { font-size: 12px; color: #909399; margin-top: 8px; }
.stat-icon { position: absolute; right: 16px; top: 50%; transform: translateY(-50%); font-size: 48px; opacity: 0.1; }
.total-card .stat-number { color: #409EFF; }
.today-card .stat-number { color: #67C23A; }
.users-card .stat-number { color: #E6A23C; }
.risk-card .stat-number { color: #F56C6C; }
.search-card { margin-bottom: 16px; }
.audit-list-card { margin-bottom: 16px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.pagination-container { margin-top: 16px; text-align: right; }
.detail-content { padding: 0 20px 20px; }
.detail-content pre { background: #f5f7fa; padding: 8px; border-radius: 4px; font-size: 12px; max-height: 200px; overflow: auto; }
.help-content h4 { margin: 16px 0 8px; color: #303133; }
.help-content h4:first-child { margin-top: 0; }
.help-content p { color: #606266; line-height: 1.6; }
.help-content ul { padding-left: 20px; color: #606266; }
.help-content li { margin-bottom: 6px; line-height: 1.6; }
.alert-stat-card { text-align: center; }
.alert-stat-value { font-size: 24px; font-weight: bold; color: #303133; }
.alert-stat-label { font-size: 12px; color: #909399; margin-top: 4px; }
</style>