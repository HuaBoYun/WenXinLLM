<template>
  <div class="budget-limit">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算限额管理</h2>
      <p>设置和管理预算限额，监控限额使用情况，提供限额调整和超限处理</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateLimit">设置限额</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportLimits">导入限额</el-button>
            <el-button type="warning" icon="el-icon-refresh" @click="handleBatchAdjust">批量调整</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportLimits">导出限额</el-button>
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

    <!-- 限额统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ limitStats.totalLimits }}</div>
            <div class="stat-label">限额总数</div>
            <div class="stat-description">所有预算限额数量</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-finance"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card used-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ limitStats.usedAmount }}</div>
            <div class="stat-label">已用金额</div>
            <div class="stat-description">已使用的限额金额</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="limitStats.usageRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#67C23A"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-money"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card warning-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ limitStats.warningCount }}</div>
            <div class="stat-label">预警限额</div>
            <div class="stat-description">接近限额的预算项</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="limitStats.warningRate" 
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
        <el-card class="stat-card exceed-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ limitStats.exceedCount }}</div>
            <div class="stat-label">超限项目</div>
            <div class="stat-description">超出限额的预算项</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="limitStats.exceedRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#F56C6C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning-outline"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="限额名称">
          <el-input
            v-model="queryForm.limitName"
            placeholder="请输入限额名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="限额类型">
          <el-select
            v-model="queryForm.limitType"
            placeholder="请选择限额类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in limitTypeOptions"
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
        <el-form-item label="限额状态">
          <el-select
            v-model="queryForm.limitStatus"
            placeholder="请选择限额状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in limitStatusOptions"
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

    <!-- 限额列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算限额列表</span>
        <div class="table-tools">
          <el-tooltip content="实时监控" placement="top">
            <el-switch
              v-model="realTimeMonitor"
              active-text="实时监控"
              @change="handleMonitorChange"
            />
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
        :data="limitList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column v-if="columnVisible.limitCode" prop="limitCode" label="限额编码" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.limitName" prop="limitName" label="限额名称" min-width="200" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.limitType" prop="limitType" label="限额类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getLimitTypeColor(scope.row.limitType)">
              {{ getLimitTypeText(scope.row.limitType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.organizationName" label="组织单元" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ getOrganizationName(scope.row.organizationId) }}
          </template>
        </el-table-column>
        <el-table-column v-if="columnVisible.budgetAccountName" label="预算科目" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ getBudgetAccountName(scope.row.budgetId) }}
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.limitAmount" prop="limitAmount" label="限额金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.limitAmount) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.usedAmount" prop="usedAmount" label="已用金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.usedAmount) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.availableAmount" prop="availableAmount" label="可用金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span :class="getRemainingAmountClass(scope.row.availableAmount)">
              {{ formatAmount(scope.row.availableAmount) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.usageRate" prop="usageRate" label="使用率" width="120" align="center" sortable="custom">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.usageRate || 0"
              :stroke-width="6"
              :text-inside="true"
              :color="getUsageRateColor(scope.row.usageRate)"
            />
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.status" prop="status" label="限额状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getLimitStatusType(scope.row.status)" size="mini">
              {{ getLimitStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.effectiveDate" prop="effectiveDate" label="生效日期" width="120" align="center" />
        <el-table-column v-if="columnVisible.expireDate" prop="expireDate" label="失效日期" width="120" align="center" />
        
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >详情</el-button>
            <el-button
              v-if="canAdjust(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              class="warning-text"
              @click="handleAdjust(scope.row)"
            >调整</el-button>
            <el-button
              v-if="canEdit(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="history" icon="el-icon-time">使用历史</el-dropdown-item>
                <el-dropdown-item command="freeze" icon="el-icon-lock">冻结限额</el-dropdown-item>
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
                <el-dropdown-item command="delete" icon="el-icon-delete" divided>删除</el-dropdown-item>
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

    <!-- 新增/编辑限额对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="limitForm"
        :model="limitForm"
        :rules="limitRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="限额名称" prop="limitName">
              <el-input
                v-model="limitForm.limitName"
                placeholder="请输入限额名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="限额编码" prop="limitCode">
              <el-input
                v-model="limitForm.limitCode"
                placeholder="请输入限额编码"
                :disabled="!!limitForm.limitId"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="限额类型" prop="limitType">
              <el-select
                v-model="limitForm.limitType"
                placeholder="请选择限额类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in limitTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="限额金额" prop="limitAmount">
              <el-input-number
                v-model="limitForm.limitAmount"
                :precision="2"
                :min="0"
                placeholder="请输入限额金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组织单元" prop="organizationId">
              <el-cascader
                v-model="limitForm.organizationId"
                :options="organizationOptions"
                :props="{ checkStrictly: true, emitPath: false }"
                placeholder="请选择组织单元"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算科目" prop="budgetId">
              <el-select
                v-model="limitForm.budgetId"
                placeholder="请选择预算科目"
                filterable
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
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="limitForm.effectiveDate"
                type="date"
                placeholder="请选择生效日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expireDate">
              <el-date-picker
                v-model="limitForm.expireDate"
                type="date"
                placeholder="请选择失效日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="limitForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入限额描述"
          />
        </el-form-item>
        
        <!-- 预警配置 -->
        <el-form-item label="预警配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="预警阈值" prop="warningThreshold">
                <el-input-number
                  v-model="limitForm.warningThreshold"
                  :precision="1"
                  :min="0"
                  :max="100"
                  placeholder="百分比"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="超限阈值" prop="exceedThreshold">
                <el-input-number
                  v-model="limitForm.exceedThreshold"
                  :precision="1"
                  :min="0"
                  :max="200"
                  placeholder="百分比"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="控制级别" prop="controlLevel">
                <el-select
                  v-model="limitForm.controlLevel"
                  placeholder="控制级别"
                  style="width: 100%"
                >
                  <el-option value="SOFT" label="软控制" />
                  <el-option value="HARD" label="硬控制" />
                  <el-option value="WARNING" label="仅预警" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>
        
        <el-form-item label="限额配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="limitForm.isActive">启用限额</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="limitForm.allowExceed">允许超限</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="limitForm.autoAdjust">自动调整</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleCalculateUsage">计算使用率</el-button>
        <el-button type="primary" @click="handleSubmitForm">保存限额</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="限额详情" :visible.sync="detailVisible" width="700px" :close-on-click-modal="true">
      <el-descriptions :column="2" border size="medium" v-if="detailRow">
        <el-descriptions-item label="限额编码">{{ detailRow.limitCode }}</el-descriptions-item>
        <el-descriptions-item label="限额名称">{{ detailRow.limitName }}</el-descriptions-item>
        <el-descriptions-item label="限额类型">{{ getLimitTypeText(detailRow.limitType) }}</el-descriptions-item>
        <el-descriptions-item label="限额状态">
          <el-tag :type="getLimitStatusType(detailRow.status)" size="mini">{{ getLimitStatusText(detailRow.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="组织单元">{{ getOrganizationName(detailRow.organizationId) }}</el-descriptions-item>
        <el-descriptions-item label="预算科目">{{ getBudgetAccountName(detailRow.budgetId) }}</el-descriptions-item>
        <el-descriptions-item label="限额金额">{{ formatAmount(detailRow.limitAmount) }}</el-descriptions-item>
        <el-descriptions-item label="已用金额">{{ formatAmount(detailRow.usedAmount) }}</el-descriptions-item>
        <el-descriptions-item label="冻结金额">{{ formatAmount(detailRow.frozenAmount) }}</el-descriptions-item>
        <el-descriptions-item label="可用金额">{{ formatAmount(detailRow.availableAmount) }}</el-descriptions-item>
        <el-descriptions-item label="生效日期">{{ detailRow.effectiveDate }}</el-descriptions-item>
        <el-descriptions-item label="失效日期">{{ detailRow.expireDate }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ detailRow.description }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailRow.remark }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailRow.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailRow.updateTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 使用历史对话框 -->
    <el-dialog :title="'使用历史 - ' + (historyLimitRow ? (historyLimitRow.limitName || historyLimitRow.limitCode) : '')" :visible.sync="historyVisible" width="900px" append-to-body>
      <el-table :data="historyList" v-loading="historyLoading" border stripe style="width: 100%" empty-text="暂无操作历史记录">
        <el-table-column prop="operationType" label="操作类型" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.operationType === 'CREATE' ? 'success' : row.operationType === 'DELETE' ? 'danger' : row.operationType === 'FREEZE' ? 'info' : row.operationType === 'ENABLE' ? 'success' : row.operationType === 'DISABLE' ? 'warning' : 'primary'" size="small">
              {{ { CREATE: '创建', UPDATE: '修改', DELETE: '删除', FREEZE: '冻结', ADJUST: '调整', ENABLE: '启用', DISABLE: '停用' }[row.operationType] || row.operationType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationDesc" label="操作描述" min-width="160" show-overflow-tooltip />
        <el-table-column prop="beforeValue" label="修改前" min-width="120" show-overflow-tooltip>
          <template slot-scope="{ row }">{{ row.beforeValue || '-' }}</template>
        </el-table-column>
        <el-table-column prop="afterValue" label="修改后" min-width="120" show-overflow-tooltip>
          <template slot-scope="{ row }">{{ row.afterValue || '-' }}</template>
        </el-table-column>
        <el-table-column prop="operator" label="操作人" width="100" align="center" />
        <el-table-column prop="operateTime" label="操作时间" width="170" align="center">
          <template slot-scope="{ row }">{{ row.operateTime ? new Date(row.operateTime).toLocaleString('zh-CN') : '-' }}</template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="historyVisible = false">关闭</el-button>
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
  </div>
</template>

<script>
import { budgetLimitApi } from '@/api/managementAccountant/ncv65/budgetControl'

export default {
  name: 'BudgetLimit',
  data() {
    return {
      // 查询参数
      queryForm: {
        limitName: '',
        limitType: '',
        organizationId: '',
        budgetId: '',
        limitStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },

      // 表格数据
      loading: false,
      limitList: [],
      total: 0,
      selectedRows: [],

      // 详情对话框
      detailVisible: false,
      detailRow: null,

      // 使用历史对话框
      historyVisible: false,
      historyLoading: false,
      historyList: [],
      historyLimitRow: null,
      
      // 实时监控
      realTimeMonitor: false,
      monitorTimer: null,
      
      // 统计数据
      limitStats: {
        totalLimits: 0,
        usedAmount: 0,
        warningCount: 0,
        exceedCount: 0,
        usageRate: 0,
        warningRate: 0,
        exceedRate: 0
      },
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      limitForm: {
        limitId: null,
        limitName: '',
        limitCode: '',
        limitType: '',
        limitAmount: 0,
        organizationId: '',
        budgetId: '',
        effectiveDate: '',
        expireDate: '',
        description: ''
      },
      limitRules: {
        limitName: [
          { required: true, message: '请输入限额名称', trigger: 'blur' }
        ],
        limitCode: [
          { required: true, message: '请输入限额编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '限额编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        limitType: [
          { required: true, message: '请选择限额类型', trigger: 'change' }
        ],
        limitAmount: [
          { required: true, message: '请输入限额金额', trigger: 'blur' }
        ]
      },
      
      // 选项数据
      limitTypeOptions: [
        { value: 'DAILY', label: '日限额' },
        { value: 'MONTHLY', label: '月度限额' },
        { value: 'QUARTERLY', label: '季度限额' },
        { value: 'YEARLY', label: '年度限额' }
      ],
      limitStatusOptions: [
        { value: 'NORMAL', label: '正常' },
        { value: 'EXCEEDED', label: '超限' },
        { value: 'FROZEN', label: '冻结' }
      ],
      organizationOptions: [],
      budgetAccountOptions: [],

      // 列设置
      columnSettingVisible: false,
      columnOptions: [
        { key: 'limitCode', label: '限额编码' },
        { key: 'limitName', label: '限额名称' },
        { key: 'limitType', label: '限额类型' },
        { key: 'organizationName', label: '组织单元' },
        { key: 'budgetAccountName', label: '预算科目' },
        { key: 'limitAmount', label: '限额金额' },
        { key: 'usedAmount', label: '已用金额' },
        { key: 'availableAmount', label: '可用金额' },
        { key: 'usageRate', label: '使用率' },
        { key: 'status', label: '限额状态' },
        { key: 'effectiveDate', label: '生效日期' },
        { key: 'expireDate', label: '失效日期' }
      ],
      columnChecked: ['limitCode', 'limitName', 'limitType', 'organizationName', 'budgetAccountName', 'limitAmount', 'usedAmount', 'availableAmount', 'usageRate', 'status', 'effectiveDate', 'expireDate'],
      columnVisible: {
        limitCode: true, limitName: true, limitType: true,
        organizationName: true, budgetAccountName: true, limitAmount: true,
        usedAmount: true, availableAmount: true, usageRate: true,
        status: true, effectiveDate: true, expireDate: true
      }
    }
  },
  
  created() {
    this.getList()
    this.loadStats()
    this.loadOrganizationOptions()
    this.loadBudgetAccountOptions()
  },
  
  beforeDestroy() {
    if (this.monitorTimer) {
      clearInterval(this.monitorTimer)
    }
  },
  
  methods: {
    // 加载统计数据
    async loadStats() {
      try {
        const response = await budgetLimitApi.getStatistics()
        if (response.code === 1 && response.data) {
          const d = response.data
          const total = d.totalCount || 0
          this.limitStats = {
            totalLimits: total,
            usedAmount: d.usedAmount || 0,
            warningCount: d.warningCount || 0,
            exceedCount: d.exceedCount || 0,
            usageRate: d.usageRate || 0,
            warningRate: total > 0 ? parseFloat((((d.warningCount || 0) / total) * 100).toFixed(1)) : 0,
            exceedRate: total > 0 ? parseFloat((((d.exceedCount || 0) / total) * 100).toFixed(1)) : 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败', error)
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
        const response = await budgetLimitApi.getPage(params)
        this.limitList = response.data.tlist || response.data.list || []
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
        const response = await budgetLimitApi.getOrganizations()
        if (response.code === 1 && response.data) {
          this.organizationOptions = (response.data || []).map(item => ({
            value: item.id,
            label: item.name || item.code
          }))
        }
      } catch (error) {
        console.error('加载组织选项失败：', error)
      }
    },
    
    // 加载预算科目选项
    async loadBudgetAccountOptions() {
      try {
        const response = await budgetLimitApi.getBudgetAccounts()
        if (response.code === 1 && response.data) {
          this.budgetAccountOptions = (response.data || []).map(item => ({
            value: item.id,
            label: item.name || item.code
          }))
        }
      } catch (error) {
        console.error('加载预算科目选项失败：', error)
      }
    },
    
    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 重置
    handleReset() {
      this.queryForm = {
        limitName: '',
        limitType: '',
        organizationId: '',
        budgetId: '',
        limitStatus: ''
      }
      this.handleQuery()
    },
    
    // 实时监控切换
    handleMonitorChange(value) {
      if (value) {
        this.monitorTimer = setInterval(() => {
          this.getList()
        }, 30000) // 30秒刷新一次
        this.$message.success('已开启实时监控')
      } else {
        if (this.monitorTimer) {
          clearInterval(this.monitorTimer)
          this.monitorTimer = null
        }
        this.$message.info('已关闭实时监控')
      }
    },
    
    // 创建限额
    handleCreateLimit() {
      this.dialogTitle = '设置预算限额'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑限额
    handleEdit(row) {
      this.dialogTitle = '编辑预算限额'
      this.dialogVisible = true
      this.limitForm = { ...row }
    },
    
    // 查看限额
    handleView(row) {
      this.detailRow = row
      this.detailVisible = true
    },
    
    // 调整限额
    handleAdjust(row) {
      this.dialogTitle = '调整预算限额'
      this.dialogVisible = true
      this.limitForm = { ...row }
    },
    
    // 计算使用率
    async handleCalculateUsage() {
      try {
        const params = {
          organizationId: this.limitForm.organizationId,
          budgetId: this.limitForm.budgetId,
          limitAmount: this.limitForm.limitAmount
        }
        const response = await budgetLimitApi.calculateUsage(params)
        const usage = response.data
        
        this.$alert(`
          <p>限额金额：${this.formatAmount(usage.limitAmount)}</p>
          <p>已用金额：${this.formatAmount(usage.usedAmount)}</p>
          <p>剩余金额：${this.formatAmount(usage.remainingAmount)}</p>
          <p>使用率：${usage.usageRate}%</p>
        `, '使用率计算结果', {
          dangerouslyUseHTMLString: true,
          type: 'info'
        })
      } catch (error) {
        this.$message.error('计算失败：' + error.message)
      }
    },
    
    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.limitForm.validate()

        // 只发送后端实体支持的字段
        const params = {
          limitName: this.limitForm.limitName,
          limitCode: this.limitForm.limitCode,
          limitType: this.limitForm.limitType,
          limitAmount: this.limitForm.limitAmount,
          organizationId: this.limitForm.organizationId,
          budgetId: this.limitForm.budgetId,
          effectiveDate: this.limitForm.effectiveDate,
          expireDate: this.limitForm.expireDate,
          description: this.limitForm.description
        }

        if (this.limitForm.limitId) {
          params.limitId = this.limitForm.limitId
          await budgetLimitApi.update(this.limitForm.limitId, params)
          this.$message.success('更新成功')
        } else {
          await budgetLimitApi.create(params)
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
      this.limitForm = {
        limitId: null,
        limitName: '',
        limitCode: '',
        limitType: '',
        limitAmount: 0,
        organizationId: '',
        budgetId: '',
        effectiveDate: '',
        expireDate: '',
        description: ''
      }
      this.$nextTick(() => {
        this.$refs.limitForm && this.$refs.limitForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入限额
    handleImportLimits() {
      this.$message.info('限额导入功能开发中...')
    },
    
    // 批量调整
    async handleBatchAdjust() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要调整的限额')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.limitId)
        await budgetLimitApi.batchAdjust(ids)
        this.$message.success('批量调整成功')
        this.getList()
      } catch (error) {
        this.$message.error('批量调整失败：' + error.message)
      }
    },
    
    // 导出限额
    async handleExportLimits() {
      try {
        const params = { ...this.queryForm }
        const response = await budgetLimitApi.export(params)
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算限额数据.xlsx'
        link.click()
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
      this.$message.info('设置功能开发中...')
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'history':
          this.handleHistory(row)
          break
        case 'freeze':
          this.handleFreeze(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    
    // 使用历史
    async handleHistory(row) {
      this.historyLimitRow = row
      this.historyVisible = true
      this.historyLoading = true
      this.historyList = []
      try {
        const response = await budgetLimitApi.getHistory(row.limitId)
        if (response.code === 1 && response.data) {
          this.historyList = response.data
        } else {
          this.$message.warning(response.msg || '查询历史失败')
        }
      } catch (error) {
        this.$message.error('查询使用历史失败：' + error.message)
      } finally {
        this.historyLoading = false
      }
    },
    
    // 冻结限额
    async handleFreeze(row) {
      try {
        await this.$confirm('确认冻结该限额？冻结后将无法使用', '提示', {
          type: 'warning'
        })
        await budgetLimitApi.freeze(row.limitId)
        this.$message.success('冻结成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('冻结失败：' + error.message)
        }
      }
    },
    
    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算限额'
      this.dialogVisible = true
      this.limitForm = { ...row, limitId: null, limitCode: null }
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        const response = await budgetLimitApi.exportSingle(row.limitId)
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `限额_${row.limitCode || row.limitId}.xlsx`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该限额记录？删除后不可恢复', '提示', {
          type: 'warning'
        })
        await budgetLimitApi.delete(row.limitId)
        this.$message.success('删除成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 行点击 - 弹出详情
    handleRowClick(row, column) {
      // 点击选择列不触发详情
      if (column && column.type === 'selection') return
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
    
    // 判断是否可以调整
    canAdjust(row) {
      return row.status === 'NORMAL'
    },
    
    // 判断是否可以编辑
    canEdit(row) {
      return row.creator === this.$store.getters.name || this.$store.getters.roles.includes('admin')
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取剩余金额样式类
    getRemainingAmountClass(amount) {
      if (amount < 0) return 'negative-amount'
      if (amount < 1000) return 'warning-amount'
      return 'positive-amount'
    },
    
    // 获取使用率颜色
    getUsageRateColor(rate) {
      if (rate >= 100) return '#F56C6C'
      if (rate >= 80) return '#E6A23C'
      return '#67C23A'
    },
    
    // 获取限额类型颜色
    getLimitTypeColor(type) {
      const colorMap = {
        'DAILY': 'danger',
        'MONTHLY': 'warning',
        'QUARTERLY': 'success',
        'YEARLY': 'primary'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取限额类型文本
    getLimitTypeText(type) {
      const item = this.limitTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },

    // 根据组织ID获取组织名称
    getOrganizationName(id) {
      if (!id) return ''
      const opt = this.organizationOptions.find(o => o.value === id)
      return opt ? opt.label : ''
    },

    // 根据预算科目ID获取科目名称
    getBudgetAccountName(id) {
      if (!id) return ''
      const opt = this.budgetAccountOptions.find(o => o.value === id)
      return opt ? opt.label : ''
    },

    // 获取限额状态类型
    getLimitStatusType(status) {
      const statusMap = {
        'NORMAL': 'success',
        'EXCEEDED': 'danger',
        'FROZEN': 'info'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取限额状态文本
    getLimitStatusText(status) {
      const item = this.limitStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-limit {
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
      
      &.used-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.warning-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.exceed-card {
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
  
  .positive-amount {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #67C23A;
  }
  
  .warning-amount {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #E6A23C;
  }
  
  .negative-amount {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #F56C6C;
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
