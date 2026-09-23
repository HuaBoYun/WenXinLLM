<template>
  <div class="budget-monitor">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算监控管理</h2>
      <p>实时监控预算执行情况，提供全方位的预算监控、分析和预警功能</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-view" @click="handleCreateMonitor">创建监控</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportMonitors">导入监控</el-button>
            <el-button type="warning" icon="el-icon-refresh" @click="handleRefreshAll">刷新全部</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportMonitors">导出监控</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button icon="el-icon-setting" @click="handleSettings">设置</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 监控统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ monitorStats.totalMonitors }}</div>
            <div class="stat-label">监控总数</div>
            <div class="stat-description">所有监控项目数量</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-view"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ monitorStats.activeMonitors }}</div>
            <div class="stat-label">活跃监控</div>
            <div class="stat-description">正在运行的监控项目</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="monitorStats.activeRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#67C23A"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card alert-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ monitorStats.alertCount }}</div>
            <div class="stat-label">预警数量</div>
            <div class="stat-description">触发的预警事件数量</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="monitorStats.alertRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#E6A23C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card coverage-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ monitorStats.coverageRate }}%</div>
            <div class="stat-label">覆盖率</div>
            <div class="stat-description">预算监控覆盖率</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="monitorStats.coverageRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#409EFF"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-pie-chart"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 实时监控面板 -->
    <el-row :gutter="20" class="monitor-panel">
      <el-col :span="16">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>实时监控图表</span>
            <div class="header-tools">
              <el-radio-group v-model="chartType" size="mini">
                <el-radio-button label="execution">执行监控</el-radio-button>
                <el-radio-button label="variance">差异监控</el-radio-button>
                <el-radio-button label="trend">趋势监控</el-radio-button>
              </el-radio-group>
              <el-button icon="el-icon-refresh" size="mini" @click="refreshChart" />
            </div>
          </div>
          <div ref="monitorChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="alert-card" shadow="never">
          <div slot="header" class="card-header">
            <span>实时预警</span>
            <el-badge :value="realtimeAlerts.length" class="alert-badge">
              <el-button icon="el-icon-bell" size="mini" @click="handleViewAllAlerts" />
            </el-badge>
          </div>
          <div class="alert-list">
            <div
              v-for="alert in realtimeAlerts"
              :key="alert.id"
              class="alert-item"
              :class="getAlertClass(alert.level)"
              @click="handleViewAlert(alert)"
            >
              <div class="alert-icon">
                <i :class="getAlertIcon(alert.level)"></i>
              </div>
              <div class="alert-content">
                <div class="alert-title">{{ alert.title }}</div>
                <div class="alert-time">{{ alert.time }}</div>
              </div>
              <div class="alert-action">
                <el-button type="text" size="mini" @click.stop="handleDismissAlert(alert)">
                  忽略
                </el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="监控名称">
          <el-input
            v-model="queryForm.monitorName"
            placeholder="请输入监控名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="监控类型">
          <el-select
            v-model="queryForm.monitorType"
            placeholder="请选择监控类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in monitorTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="组织单元">
          <el-select
            v-model="queryForm.organizationId"
            placeholder="请选择组织单元"
            clearable
            filterable
            style="width: 200px"
          >
            <el-option
              v-for="item in organizationOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预算科目">
          <el-select
            v-model="queryForm.budgetId"
            placeholder="请选择预算科目"
            clearable
            filterable
            style="width: 150px"
          >
            <el-option
              v-for="item in budgetAccountOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="监控状态">
          <el-select
            v-model="queryForm.monitorStatus"
            placeholder="请选择监控状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in monitorStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 监控列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算监控列表</span>
        <div class="table-tools">
          <el-tooltip content="自动刷新" placement="top">
            <el-switch
              v-model="autoRefresh"
              active-text="自动刷新"
              @change="handleAutoRefreshChange"
            />
          </el-tooltip>
          <el-tooltip content="刷新间隔" placement="top">
            <el-select v-model="refreshInterval" size="mini" style="width: 80px">
              <el-option label="10s" :value="10" />
              <el-option label="30s" :value="30" />
              <el-option label="60s" :value="60" />
            </el-select>
          </el-tooltip>
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getList" />
          </el-tooltip>
          <el-tooltip content="列设置" placement="top">
            <el-button icon="el-icon-setting" size="mini" @click="handleColumnSetting" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="monitorList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column v-if="columnVisible.monitorCode" prop="monitorCode" label="监控编码" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.monitorName" prop="monitorName" label="监控名称" min-width="200" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.monitorType" prop="monitorType" label="监控类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getMonitorTypeColor(scope.row.monitorType)">
              {{ getMonitorTypeText(scope.row.monitorType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.organizationId" prop="organizationId" label="组织单元" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ getOrganizationName(scope.row.organizationId) }}
          </template>
        </el-table-column>
        <el-table-column v-if="columnVisible.budgetId" prop="budgetId" label="预算科目" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ getBudgetAccountName(scope.row.budgetId) }}
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.currentValue" prop="currentValue" label="监控值" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span :class="getMonitorValueClass(scope.row.currentValue, scope.row.thresholdValue)">
              {{ formatMonitorValue(scope.row.currentValue, scope.row.monitorType) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.thresholdValue" prop="thresholdValue" label="阈值" width="100" align="right">
          <template slot-scope="scope">
            <span class="threshold-text">{{ formatThreshold(scope.row.thresholdValue, scope.row.monitorType) }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.monitorStatus" prop="monitorStatus" label="监控状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getMonitorStatusType(scope.row.monitorStatus)" size="mini">
              {{ getMonitorStatusText(scope.row.monitorStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.lastCheckTime" prop="lastCheckTime" label="最后检查" width="160" align="center">
          <template slot-scope="scope">
            {{ formatTime(scope.row.lastCheckTime) }}
          </template>
        </el-table-column>
        <el-table-column v-if="columnVisible.checkInterval" prop="checkInterval" label="检查间隔" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.checkInterval ? scope.row.checkInterval + '秒' : '-' }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click.stop="handleView(scope.row)"
            >详情</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-delete"
              class="danger-text"
              @click.stop="handleDelete(scope.row)"
            >删除</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
              @click.native.stop
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-if="canStart(scope.row)" command="start" icon="el-icon-video-play">启动</el-dropdown-item>
                <el-dropdown-item v-if="canStop(scope.row)" command="stop" icon="el-icon-video-pause">停止</el-dropdown-item>
                <el-dropdown-item command="history" icon="el-icon-time">监控历史</el-dropdown-item>
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
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

    <!-- 新增/编辑监控对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="monitorForm"
        :model="monitorForm"
        :rules="monitorRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="监控名称" prop="monitorName">
              <el-input
                v-model="monitorForm.monitorName"
                placeholder="请输入监控名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监控编码" prop="monitorCode">
              <el-input
                v-model="monitorForm.monitorCode"
                placeholder="请输入监控编码（留空自动生成）"
                :disabled="!!monitorForm.monitorId"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="监控类型" prop="monitorType">
              <el-select
                v-model="monitorForm.monitorType"
                placeholder="请选择监控类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in monitorTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="告警级别" prop="alertLevel">
              <el-select
                v-model="monitorForm.alertLevel"
                placeholder="请选择告警级别"
                style="width: 100%"
              >
                <el-option value="INFO" label="信息" />
                <el-option value="WARNING" label="警告" />
                <el-option value="ERROR" label="错误" />
                <el-option value="CRITICAL" label="严重" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组织单元" prop="organizationId">
              <el-select
                v-model="monitorForm.organizationId"
                placeholder="请选择组织单元"
                filterable
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="item in organizationOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算科目" prop="budgetId">
              <el-select
                v-model="monitorForm.budgetId"
                placeholder="请选择预算科目"
                filterable
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="item in budgetAccountOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 监控条件 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="阈值" prop="thresholdValue">
              <el-input-number
                v-model="monitorForm.thresholdValue"
                :precision="2"
                :min="0"
                placeholder="请输入阈值"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查间隔(秒)" prop="checkInterval">
              <el-input-number
                v-model="monitorForm.checkInterval"
                :min="10"
                :step="10"
                placeholder="检查间隔"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="monitorForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleTestMonitor">测试监控</el-button>
        <el-button type="primary" @click="handleSubmitForm">保存监控</el-button>
      </div>
    </el-dialog>

    <!-- 列设置对话框 -->
    <el-dialog title="列设置" :visible.sync="columnSettingVisible" width="400px">
      <el-checkbox-group v-model="columnChecked">
        <el-row :gutter="10">
          <el-col v-for="col in columnOptions" :key="col.key" :span="12">
            <el-checkbox :label="col.key" style="margin-bottom: 8px;">{{ col.label }}</el-checkbox>
          </el-col>
        </el-row>
      </el-checkbox-group>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleResetColumns">重置</el-button>
        <el-button @click="columnSettingVisible = false">取消</el-button>
        <el-button type="primary" @click="handleApplyColumns">确定</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="监控详情" :visible.sync="detailVisible" width="700px" :close-on-click-modal="true">
      <el-descriptions v-if="detailData" :column="2" border size="medium">
        <el-descriptions-item label="监控编码">{{ detailData.monitorCode || '-' }}</el-descriptions-item>
        <el-descriptions-item label="监控名称">{{ detailData.monitorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="监控类型">
          <el-tag size="mini" :type="getMonitorTypeColor(detailData.monitorType)">
            {{ getMonitorTypeText(detailData.monitorType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="监控状态">
          <el-tag size="mini" :type="getMonitorStatusType(detailData.monitorStatus)">
            {{ getMonitorStatusText(detailData.monitorStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="组织单元">{{ getOrganizationName(detailData.organizationId) }}</el-descriptions-item>
        <el-descriptions-item label="预算科目">{{ getBudgetAccountName(detailData.budgetId) }}</el-descriptions-item>
        <el-descriptions-item label="当前监控值">{{ detailData.currentValue != null ? detailData.currentValue : '-' }}</el-descriptions-item>
        <el-descriptions-item label="阈值">{{ detailData.thresholdValue != null ? detailData.thresholdValue : '-' }}</el-descriptions-item>
        <el-descriptions-item label="告警级别">{{ detailData.alertLevel || '-' }}</el-descriptions-item>
        <el-descriptions-item label="检查间隔">{{ detailData.checkInterval ? detailData.checkInterval + '秒' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="最后检查时间" :span="2">{{ formatTime(detailData.lastCheckTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatTime(detailData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新人">{{ detailData.updateBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatTime(detailData.updateTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="primary" @click="detailVisible = false; handleEdit(detailData)">编辑</el-button>
      </div>
    </el-dialog>

    <!-- 导入监控对话框 -->
    <el-dialog title="导入监控数据" :visible.sync="importDialogVisible" width="500px" :close-on-click-modal="false">
      <el-upload
        ref="importUpload"
        action="#"
        :auto-upload="false"
        :limit="1"
        accept=".xlsx,.xls"
        :on-change="handleImportFileChange"
        :file-list="importFileList"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">只能上传 xlsx/xls 文件，且不超过 10MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="importLoading" @click="handleSubmitImport">确认导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { budgetMonitorApi } from '@/api/managementAccountant/ncv65/budgetControl'

export default {
  name: 'BudgetMonitor',
  data() {
    return {
      // 查询参数
      queryForm: {
        monitorName: '',
        monitorType: '',
        organizationId: '',
        budgetId: '',
        monitorStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      monitorList: [],
      total: 0,
      selectedRows: [],
      
      // 自动刷新
      autoRefresh: false,
      refreshInterval: 30,
      refreshTimer: null,
      
      // 图表
      chartType: 'execution',
      
      // 统计数据
      monitorStats: {
        totalMonitors: 0,
        activeMonitors: 0,
        alertCount: 0,
        coverageRate: 0,
        activeRate: 0,
        alertRate: 0
      },

      // 实时预警
      realtimeAlerts: [],
      
      // 导入对话框
      importDialogVisible: false,
      importFile: null,
      importFileList: [],
      importLoading: false,

      // 详情对话框
      detailVisible: false,
      detailData: null,

      // 新增/编辑对话框
      dialogVisible: false,
      dialogTitle: '',
      monitorForm: {
        monitorName: '',
        monitorCode: '',
        monitorType: '',
        organizationId: '',
        budgetId: '',
        thresholdValue: 0,
        alertLevel: 'WARNING',
        checkInterval: 60,
        remark: ''
      },
      monitorRules: {
        monitorName: [
          { required: true, message: '请输入监控名称', trigger: 'blur' }
        ],
        monitorType: [
          { required: true, message: '请选择监控类型', trigger: 'change' }
        ],
        thresholdValue: [
          { required: true, message: '请输入阈值', trigger: 'blur' }
        ]
      },
      
      // 选项数据
      monitorTypeOptions: [
        { value: 'EXECUTION_RATE', label: '执行率监控' },
        { value: 'VARIANCE_RATE', label: '差异率监控' },
        { value: 'BUDGET_BALANCE', label: '预算余额监控' },
        { value: 'SPENDING_SPEED', label: '支出速度监控' },
        { value: 'APPROVAL_DELAY', label: '审批延迟监控' },
        { value: 'CUSTOM', label: '自定义监控' }
      ],
      monitorStatusOptions: [
        { value: 'ACTIVE', label: '运行中' },
        { value: 'WARNING', label: '预警' },
        { value: 'ALERT', label: '告警' },
        { value: 'CRITICAL', label: '严重' },
        { value: 'STOPPED', label: '已停止' }
      ],
      organizationOptions: [],
      budgetAccountOptions: [],
      userOptions: [],

      // 列设置
      columnSettingVisible: false,
      columnOptions: [
        { key: 'monitorCode', label: '监控编码' },
        { key: 'monitorName', label: '监控名称' },
        { key: 'monitorType', label: '监控类型' },
        { key: 'organizationId', label: '组织单元' },
        { key: 'budgetId', label: '预算科目' },
        { key: 'currentValue', label: '监控值' },
        { key: 'thresholdValue', label: '阈值' },
        { key: 'monitorStatus', label: '监控状态' },
        { key: 'lastCheckTime', label: '最后检查' },
        { key: 'checkInterval', label: '检查间隔' }
      ],
      columnChecked: ['monitorCode', 'monitorName', 'monitorType', 'organizationId', 'budgetId', 'currentValue', 'thresholdValue', 'monitorStatus', 'lastCheckTime', 'checkInterval'],
      columnVisible: {
        monitorCode: true, monitorName: true, monitorType: true,
        organizationId: true, budgetId: true, currentValue: true,
        thresholdValue: true, monitorStatus: true, lastCheckTime: true,
        checkInterval: true
      }
    }
  },
  
  created() {
    this.getList()
    this.loadStats()
    this.loadAlerts()
    this.loadOrganizationOptions()
    this.loadBudgetAccountOptions()
    this.loadUserOptions()
  },

  mounted() {
    this.initChart()
  },
  
  beforeDestroy() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
    // 销毁图表实例
    const chartDom = this.$refs.monitorChart
    if (chartDom) {
      const chart = echarts.getInstanceByDom(chartDom)
      if (chart) chart.dispose()
    }
  },
  
  methods: {
    // 加载统计数据
    async loadStats() {
      try {
        const response = await budgetMonitorApi.getStatistics()
        if (response.code === 1 && response.data) {
          const d = response.data
          const total = d.totalCount || 0
          const active = d.activeCount || 0
          const alert = (d.warningCount || 0) + (d.alertCount || 0) + (d.criticalCount || 0)
          this.monitorStats = {
            totalMonitors: total,
            activeMonitors: active,
            alertCount: alert,
            coverageRate: total > 0 ? parseFloat(((active / total) * 100).toFixed(1)) : 0,
            activeRate: total > 0 ? parseFloat(((active / total) * 100).toFixed(1)) : 0,
            alertRate: total > 0 ? parseFloat(((alert / total) * 100).toFixed(1)) : 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败', error)
      }
    },
    // 加载实时预警
    async loadAlerts() {
      try {
        const response = await budgetMonitorApi.getAlerts()
        if (response.code === 1 && response.data) {
          this.realtimeAlerts = (response.data || []).slice(0, 10).map(item => ({
            id: item.monitorId || item.id,
            title: item.monitorName || item.description || '预算预警',
            level: item.monitorStatus || 'WARNING',
            time: item.lastCheckTime || ''
          }))
        }
      } catch (error) {
        console.error('加载预警数据失败', error)
      }
    },
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.queryParams
        }
        const response = await budgetMonitorApi.getPage(params)
        this.monitorList = response.data.tlist || response.data.list || []
        this.total = response.data.totalRecord || response.data.total || 0
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载组织选项
    async loadOrganizationOptions() {
      try {
        const response = await budgetMonitorApi.getOrganizations()
        if (response.code === 1 && response.data) {
          this.organizationOptions = (response.data || []).map(item => ({
            value: item.id,
            label: item.name || item.code || item.id
          }))
        }
      } catch (error) {
        console.error('加载组织选项失败：', error)
      }
    },

    // 加载预算科目选项
    async loadBudgetAccountOptions() {
      try {
        const response = await budgetMonitorApi.getBudgetAccounts()
        if (response.code === 1 && response.data) {
          this.budgetAccountOptions = (response.data || []).map(item => ({
            value: item.id,
            label: item.name || item.code || item.id
          }))
        }
      } catch (error) {
        console.error('加载预算科目选项失败：', error)
      }
    },

    // 加载用户选项
    async loadUserOptions() {
      try {
        const response = await budgetMonitorApi.getUsers()
        if (response.code === 1 && response.data) {
          this.userOptions = (response.data || []).map(item => ({
            id: item.id,
            name: item.name || item.id
          }))
        }
      } catch (error) {
        console.error('加载用户选项失败：', error)
      }
    },
    
    // 初始化图表
    async initChart() {
      // 先加载统计数据用于图表
      let statsData = {}
      try {
        const response = await budgetMonitorApi.getStatistics()
        if (response.code === 1 && response.data) {
          statsData = response.data
        }
      } catch (e) {
        console.error('加载图表数据失败', e)
      }
      this.$nextTick(() => {
        const chartDom = this.$refs.monitorChart
        if (!chartDom) return
        // 销毁旧实例
        const oldChart = echarts.getInstanceByDom(chartDom)
        if (oldChart) oldChart.dispose()
        const myChart = echarts.init(chartDom)
        this.updateChart(myChart, statsData)
      })
    },

    // 更新图表
    updateChart(chart, statsData) {
      const total = statsData.totalCount || 0
      const active = statsData.activeCount || 0
      const warning = statsData.warningCount || 0
      const alert = statsData.alertCount || 0
      const critical = statsData.criticalCount || 0
      const stopped = total - active - warning - alert - critical

      const option = {
        title: {
          text: '监控状态分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['运行中', '预警', '告警', '严重', '已停止']
        },
        series: [
          {
            name: '监控状态',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
            label: { show: true, formatter: '{b}: {c}' },
            data: [
              { value: active, name: '运行中', itemStyle: { color: '#67C23A' } },
              { value: warning, name: '预警', itemStyle: { color: '#E6A23C' } },
              { value: alert, name: '告警', itemStyle: { color: '#F56C6C' } },
              { value: critical, name: '严重', itemStyle: { color: '#C03639' } },
              { value: stopped > 0 ? stopped : 0, name: '已停止', itemStyle: { color: '#909399' } }
            ].filter(d => d.value > 0)
          }
        ]
      }
      chart.setOption(option)
    },

    // 刷新图表
    refreshChart() {
      this.initChart()
    },
    
    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 重置
    handleReset() {
      this.queryForm = {
        monitorName: '',
        monitorType: '',
        organizationId: '',
        budgetId: '',
        monitorStatus: ''
      }
      this.handleQuery()
    },
    
    // 自动刷新切换
    handleAutoRefreshChange(value) {
      if (value) {
        this.refreshTimer = setInterval(() => {
          this.getList()
          this.refreshChart()
        }, this.refreshInterval * 1000)
        this.$message.success('已开启自动刷新')
      } else {
        if (this.refreshTimer) {
          clearInterval(this.refreshTimer)
          this.refreshTimer = null
        }
        this.$message.info('已关闭自动刷新')
      }
    },
    
    // 创建监控
    handleCreateMonitor() {
      this.dialogTitle = '创建预算监控'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 查看监控详情（弹窗）
    handleView(row) {
      this.detailData = { ...row }
      this.detailVisible = true
    },

    // 编辑监控
    handleEdit(row) {
      this.dialogTitle = '编辑预算监控'
      this.dialogVisible = true
      this.monitorForm = { ...row }
    },

    // 启动监控
    async handleStart(row) {
      try {
        await budgetMonitorApi.start(row.monitorId)
        this.$message.success('监控启动成功')
        this.getList()
      } catch (error) {
        this.$message.error('启动失败：' + error.message)
      }
    },
    
    // 停止监控
    async handleStop(row) {
      try {
        await this.$confirm('确认停止该监控？停止后将不再进行监控检查', '提示', {
          type: 'warning'
        })
        await budgetMonitorApi.stop(row.monitorId)
        this.$message.success('监控停止成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('停止失败：' + error.message)
        }
      }
    },
    
    // 测试监控
    async handleTestMonitor() {
      try {
        const params = {
          monitorType: this.monitorForm.monitorType,
          thresholdValue: this.monitorForm.thresholdValue,
          organizationId: this.monitorForm.organizationId,
          budgetId: this.monitorForm.budgetId
        }
        const response = await budgetMonitorApi.testMonitor(params)
        const result = response.data
        
        this.$alert(`
          <p>监控类型：${result.monitorType}</p>
          <p>当前值：${result.currentValue}</p>
          <p>阈值：${result.threshold}</p>
          <p>测试结果：${result.triggered ? '触发预警' : '正常'}</p>
          <p>测试说明：${result.message}</p>
        `, '监控测试结果', {
          dangerouslyUseHTMLString: true,
          type: result.triggered ? 'warning' : 'success'
        })
      } catch (error) {
        this.$message.error('测试失败：' + error.message)
      }
    },
    
    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.monitorForm.validate()
        
        const params = { ...this.monitorForm }
        
        if (this.monitorForm.monitorId) {
          await budgetMonitorApi.update(this.monitorForm.monitorId, params)
          this.$message.success('更新成功')
        } else {
          await budgetMonitorApi.create(params)
          this.$message.success('创建成功')
        }
        
        this.dialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },
    
    // 重置表单
    resetForm() {
      this.monitorForm = {
        monitorName: '',
        monitorCode: '',
        monitorType: '',
        organizationId: '',
        budgetId: '',
        thresholdValue: 0,
        alertLevel: 'WARNING',
        checkInterval: 60,
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.monitorForm && this.$refs.monitorForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 查看预警
    handleViewAlert(alert) {
      this.$router.push({
        path: '/managementAccountant/ncv65/budgetControl/alertDetail',
        query: { id: alert.id }
      })
    },
    
    // 忽略预警
    handleDismissAlert(alert) {
      const index = this.realtimeAlerts.findIndex(item => item.id === alert.id)
      if (index > -1) {
        this.realtimeAlerts.splice(index, 1)
        this.$message.success('已忽略预警')
      }
    },
    
    // 查看所有预警
    handleViewAllAlerts() {
      this.$router.push('/managementAccountant/ncv65/budgetControl/alerts')
    },
    
    // 导入监控
    handleImportMonitors() {
      this.importFileList = []
      this.importFile = null
      this.importDialogVisible = true
    },

    // 文件选择
    handleImportFileChange(file) {
      this.importFile = file.raw
    },

    // 提交导入
    async handleSubmitImport() {
      if (!this.importFile) {
        this.$message.warning('请选择要导入的文件')
        return
      }
      this.importLoading = true
      try {
        const formData = new FormData()
        formData.append('file', this.importFile)
        const response = await budgetMonitorApi.import(formData)
        if (response.code === 1) {
          this.$message.success(`导入成功，共导入 ${response.data ? response.data.successCount : 0} 条记录`)
          this.importDialogVisible = false
          this.getList()
        } else {
          this.$message.error(response.msg || '导入失败')
        }
      } catch (error) {
        this.$message.error('导入失败：' + error.message)
      } finally {
        this.importLoading = false
      }
    },
    
    // 刷新全部
    async handleRefreshAll() {
      try {
        await budgetMonitorApi.refreshAll()
        this.$message.success('刷新全部监控成功')
        this.getList()
      } catch (error) {
        this.$message.error('刷新失败：' + error.message)
      }
    },
    
    // 导出监控
    async handleExportMonitors() {
      try {
        const params = { ...this.queryForm }
        const response = await budgetMonitorApi.export(params)
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算监控数据.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
    },
    
    // 设置
    handleSettings() {
      this.$router.push('/managementAccountant/ncv65/budgetControl/monitorSettings')
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'start':
          this.handleStart(row)
          break
        case 'stop':
          this.handleStop(row)
          break
        case 'history':
          this.handleHistory(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
      }
    },
    handleHistory(row) {
      this.$router.push({
        path: '/managementAccountant/ncv65/budgetControl/monitorHistory',
        query: { monitorId: row.monitorId }
      })
    },
    
    // 配置监控
    handleConfig(row) {
      this.dialogTitle = '配置预算监控'
      this.dialogVisible = true
      this.monitorForm = { ...row }
    },
    
    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算监控'
      this.dialogVisible = true
      this.monitorForm = { ...row, monitorId: null, monitorCode: '' }
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        const response = await budgetMonitorApi.exportSingle(row.monitorId)
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `监控_${row.monitorName || row.monitorCode || row.monitorId}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该监控记录？删除后不可恢复', '提示', {
          type: 'warning'
        })
        await budgetMonitorApi.delete(row.monitorId)
        this.$message.success('删除成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },
    
    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 排序改变
    handleSortChange({ column, prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getList()
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    
    // 列设置
    handleColumnSetting() {
      this.columnChecked = Object.keys(this.columnVisible).filter(k => this.columnVisible[k])
      this.columnSettingVisible = true
    },
    handleApplyColumns() {
      this.columnOptions.forEach(col => {
        this.$set(this.columnVisible, col.key, this.columnChecked.includes(col.key))
      })
      this.columnSettingVisible = false
    },
    handleResetColumns() {
      this.columnChecked = this.columnOptions.map(col => col.key)
    },
    
    // 判断是否可以启动
    canStart(row) {
      return row.monitorStatus !== 'ACTIVE'
    },
    
    // 判断是否可以停止（非 STOPPED 状态都可以停止）
    canStop(row) {
      return row.monitorStatus !== 'STOPPED'
    },
    
    // 格式化监控值
    formatMonitorValue(value, type) {
      if (!value) return '0'
      if (type === 'EXECUTION_RATE' || type === 'VARIANCE_RATE') {
        return value + '%'
      }
      return parseFloat(value).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 格式化阈值
    formatThreshold(value, type) {
      if (!value) return '0'
      if (type === 'EXECUTION_RATE' || type === 'VARIANCE_RATE') {
        return value + '%'
      }
      return parseFloat(value).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取监控值样式类
    getMonitorValueClass(value, threshold) {
      if (value > threshold) return 'warning-value'
      return 'normal-value'
    },
    
    // 获取预警样式类
    getAlertClass(level) {
      return `alert-${level.toLowerCase()}`
    },
    
    // 获取预警图标
    getAlertIcon(level) {
      const iconMap = {
        'INFO': 'el-icon-info',
        'WARNING': 'el-icon-warning',
        'ERROR': 'el-icon-error',
        'CRITICAL': 'el-icon-close'
      }
      return iconMap[level] || 'el-icon-info'
    },
    
    // 获取监控类型颜色
    getMonitorTypeColor(type) {
      const colorMap = {
        'EXECUTION_RATE': 'primary',
        'VARIANCE_RATE': 'warning',
        'BUDGET_BALANCE': 'success',
        'SPENDING_SPEED': 'info',
        'APPROVAL_DELAY': 'danger',
        'CUSTOM': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取监控类型文本
    getMonitorTypeText(type) {
      const item = this.monitorTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取监控状态类型
    getMonitorStatusType(status) {
      const statusMap = {
        'ACTIVE': 'success',
        'WARNING': 'warning',
        'ALERT': 'danger',
        'CRITICAL': 'danger',
        'STOPPED': 'info'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取监控状态文本
    getMonitorStatusText(status) {
      const item = this.monitorStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    },

    // 根据组织ID获取组织名称
    getOrganizationName(orgId) {
      if (!orgId) return '-'
      const item = this.organizationOptions.find(opt => opt.value === orgId)
      return item ? item.label : orgId
    },

    // 根据预算ID获取科目名称
    getBudgetAccountName(budgetId) {
      if (!budgetId) return '-'
      const item = this.budgetAccountOptions.find(opt => opt.value === budgetId)
      return item ? item.label : budgetId
    },

    // 格式化时间
    formatTime(time) {
      if (!time) return '-'
      if (typeof time === 'string') return time.replace('T', ' ')
      return time
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-monitor {
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
  .search-card,
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
      
      &.total-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.active-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.alert-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.coverage-card {
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
        
        .stat-progress {
          margin-top: 8px;
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
  
  .monitor-panel {
    margin-bottom: 20px;
    
    .chart-card {
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
        height: 400px;
      }
    }
    
    .alert-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .alert-badge {
          margin-left: 10px;
        }
      }
      
      .alert-list {
        max-height: 400px;
        overflow-y: auto;
        
        .alert-item {
          display: flex;
          align-items: center;
          padding: 12px;
          margin-bottom: 8px;
          border-radius: 4px;
          cursor: pointer;
          transition: all 0.3s;
          
          &:hover {
            background-color: #f5f7fa;
          }
          
          &.alert-info {
            border-left: 4px solid #409EFF;
          }
          
          &.alert-warning {
            border-left: 4px solid #E6A23C;
          }
          
          &.alert-error {
            border-left: 4px solid #F56C6C;
          }
          
          &.alert-critical {
            border-left: 4px solid #F56C6C;
            background-color: #fef0f0;
          }
          
          .alert-icon {
            margin-right: 12px;
            font-size: 18px;
            
            &.alert-info { color: #409EFF; }
            &.alert-warning { color: #E6A23C; }
            &.alert-error { color: #F56C6C; }
            &.alert-critical { color: #F56C6C; }
          }
          
          .alert-content {
            flex: 1;
            
            .alert-title {
              font-size: 14px;
              color: #303133;
              margin-bottom: 4px;
            }
            
            .alert-time {
              font-size: 12px;
              color: #909399;
            }
          }
          
          .alert-action {
            margin-left: 12px;
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
  
  .normal-value {
    color: #67C23A;
    font-weight: 500;
  }
  
  .warning-value {
    color: #E6A23C;
    font-weight: 500;
  }
  
  .threshold-text {
    color: #909399;
    font-size: 12px;
  }
  
  .success-text {
    color: #67C23A;
  }
  
  .warning-text {
    color: #E6A23C;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .text-right {
    text-align: right;
  }
}
</style>
