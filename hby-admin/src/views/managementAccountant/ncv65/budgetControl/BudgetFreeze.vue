<template>
  <div class="budget-freeze">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算冻结管理</h2>
      <p>管理预算冻结操作，包括冻结申请、审批流程、冻结监控和解冻处理</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-lock" @click="handleCreateFreeze">申请冻结</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportFreezes">导入冻结</el-button>
            <el-button type="warning" icon="el-icon-unlock" @click="handleBatchUnfreeze">批量解冻</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportFreezes">导出冻结</el-button>
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

    <!-- 冻结统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ freezeStats.totalFreezes }}</div>
            <div class="stat-label">冻结总数</div>
            <div class="stat-description">所有冻结申请数量</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-lock"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ freezeStats.activeFreezes }}</div>
            <div class="stat-label">生效冻结</div>
            <div class="stat-description">当前生效的冻结数量</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="freezeStats.activeRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#67C23A"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-check"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card frozen-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ freezeStats.frozenAmount }}</div>
            <div class="stat-label">冻结金额</div>
            <div class="stat-description">已冻结的预算金额</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="freezeStats.frozenRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#E6A23C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-money"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card pending-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ freezeStats.pendingCount }}</div>
            <div class="stat-label">待审批</div>
            <div class="stat-description">待审批的冻结申请</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="freezeStats.pendingRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#F56C6C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-time"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="冻结标题">
          <el-input
            v-model="queryForm.freezeTitle"
            placeholder="请输入冻结标题"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="冻结类型">
          <el-select
            v-model="queryForm.freezeType"
            placeholder="请选择冻结类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in freezeTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="组织单元">
          <el-cascader
            v-model="queryForm.organizationPath"
            :options="organizationOptions"
            :props="{ checkStrictly: true, emitPath: false }"
            placeholder="请选择组织单元"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="预算科目">
          <el-select
            v-model="queryForm.budgetAccount"
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
        <el-form-item label="冻结状态">
          <el-select
            v-model="queryForm.freezeStatus"
            placeholder="请选择冻结状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in freezeStatusOptions"
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

    <!-- 冻结列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算冻结列表</span>
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
        :data="freezeList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column v-if="columnVisible.freezeCode" prop="freezeCode" label="冻结编码" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.freezeTitle" prop="freezeTitle" label="冻结标题" min-width="200" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.freezeType" prop="freezeType" label="冻结类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getFreezeTypeColor(scope.row.freezeType)">
              {{ getFreezeTypeText(scope.row.freezeType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.organizationName" prop="organizationName" label="组织单元" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.budgetAccountName" prop="budgetAccountName" label="预算科目" width="150" show-overflow-tooltip />
        
        <el-table-column v-if="columnVisible.freezeAmount" prop="freezeAmount" label="冻结金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.freezeAmount) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.freezeReason" prop="freezeReason" label="冻结原因" width="150" show-overflow-tooltip />
        
        <el-table-column v-if="columnVisible.freezeStatus" prop="freezeStatus" label="冻结状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getFreezeStatusType(scope.row.freezeStatus)" size="mini">
              {{ getFreezeStatusText(scope.row.freezeStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.approvalStatus" prop="approvalStatus" label="审批状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getApprovalStatusType(scope.row.approvalStatus)" size="mini">
              {{ getApprovalStatusText(scope.row.approvalStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.applicant" prop="applicant" label="申请人" width="100" align="center" />
        <el-table-column v-if="columnVisible.applyDate" prop="applyDate" label="申请日期" width="120" align="center" />
        <el-table-column v-if="columnVisible.freezeDate" prop="freezeDate" label="冻结日期" width="120" align="center" />
        
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
              v-if="canUnfreeze(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-unlock"
              class="warning-text"
              @click.stop="handleUnfreeze(scope.row)"
            >解冻</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini" @click.stop>
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="history" icon="el-icon-time">冻结历史</el-dropdown-item>
                <el-dropdown-item command="extend" icon="el-icon-timer">延期冻结</el-dropdown-item>
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

    <!-- 新增/编辑/详情冻结对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="freezeForm"
        :model="freezeForm"
        :rules="dialogMode === 'view' ? {} : freezeRules"
        label-width="120px"
        size="small"
        :disabled="dialogMode === 'view'"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="冻结标题" prop="freezeTitle">
              <el-input
                v-model="freezeForm.freezeTitle"
                placeholder="请输入冻结标题"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="冻结编码" prop="freezeCode">
              <el-input
                v-model="freezeForm.freezeCode"
                placeholder="请输入冻结编码"
                :disabled="!!freezeForm.freezeId"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="冻结类型" prop="freezeType">
              <el-select
                v-model="freezeForm.freezeType"
                placeholder="请选择冻结类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in freezeTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="冻结金额" prop="freezeAmount">
              <el-input-number
                v-model="freezeForm.freezeAmount"
                :precision="2"
                :min="0"
                placeholder="请输入冻结金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组织单元" prop="organizationId">
              <el-cascader
                v-model="freezeForm.organizationId"
                :options="organizationOptions"
                :props="{ checkStrictly: true, emitPath: false }"
                placeholder="请选择组织单元"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算科目" prop="budgetAccountId">
              <el-select
                v-model="freezeForm.budgetAccountId"
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
            <el-form-item label="申请日期" prop="applyDate">
              <el-date-picker
                v-model="freezeForm.applyDate"
                type="date"
                placeholder="请选择申请日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划解冻日期" prop="plannedUnfreezeDate">
              <el-date-picker
                v-model="freezeForm.plannedUnfreezeDate"
                type="date"
                placeholder="请选择计划解冻日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="冻结原因" prop="freezeReason">
          <el-input
            v-model="freezeForm.freezeReason"
            type="textarea"
            :rows="3"
            placeholder="请输入冻结原因"
          />
        </el-form-item>
        
        <el-form-item label="冻结说明" prop="freezeDescription">
          <el-input
            v-model="freezeForm.freezeDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入冻结说明"
          />
        </el-form-item>
        
        <!-- 审批配置 -->
        <el-form-item label="审批配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="紧急程度" prop="urgencyLevel">
                <el-select
                  v-model="freezeForm.urgencyLevel"
                  placeholder="紧急程度"
                  style="width: 100%"
                >
                  <el-option value="LOW" label="低" />
                  <el-option value="MEDIUM" label="中" />
                  <el-option value="HIGH" label="高" />
                  <el-option value="URGENT" label="紧急" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="审批人" prop="approver">
                <el-select
                  v-model="freezeForm.approver"
                  placeholder="请选择审批人"
                  style="width: 100%"
                >
                  <el-option
                    v-for="user in userOptions"
                    :key="user.id"
                    :label="user.name"
                    :value="user.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="通知方式" prop="notificationMethod">
                <el-select
                  v-model="freezeForm.notificationMethod"
                  placeholder="通知方式"
                  style="width: 100%"
                >
                  <el-option value="EMAIL" label="邮件" />
                  <el-option value="SMS" label="短信" />
                  <el-option value="SYSTEM" label="系统通知" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>
        
        <el-form-item label="冻结配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="freezeForm.autoApprove">自动审批</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="freezeForm.allowPartialUnfreeze">允许部分解冻</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="freezeForm.sendNotification">发送通知</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">{{ dialogMode === 'view' ? '关闭' : '取消' }}</el-button>
        <template v-if="dialogMode !== 'view'">
          <el-button type="info" @click="handleValidateFreeze">验证冻结</el-button>
          <el-button type="primary" @click="handleSubmitForm">{{ dialogMode === 'edit' ? '保存' : '提交申请' }}</el-button>
        </template>
      </div>
    </el-dialog>

    <!-- 冻结历史对话框 -->
    <el-dialog :title="'冻结历史 - ' + historyFreezeTitle" :visible.sync="historyDialogVisible" width="700px" append-to-body>
      <div v-loading="historyLoading">
        <el-empty v-if="!historyLoading && historyList.length === 0" description="暂无操作历史" />
        <el-timeline v-else>
          <el-timeline-item
            v-for="item in historyList"
            :key="item.historyId"
            :timestamp="item.operateTime"
            placement="top"
            :type="item.operationType === 'CREATE' ? 'success' : item.operationType === 'UNFREEZE' ? 'warning' : item.operationType === 'EXTEND' ? 'primary' : item.operationType === 'FREEZE' ? 'danger' : 'info'"
          >
            <el-card shadow="never" class="history-card">
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <el-tag size="small" :type="item.operationType === 'CREATE' ? 'success' : item.operationType === 'UNFREEZE' ? 'warning' : item.operationType === 'EXTEND' ? '' : item.operationType === 'FREEZE' ? 'danger' : 'info'">
                  {{ { CREATE: '创建', UPDATE: '更新', FREEZE: '冻结', UNFREEZE: '解冻', EXTEND: '延期', DELETE: '删除', APPROVE: '审批' }[item.operationType] || item.operationType }}
                </el-tag>
                <span style="color: #999; font-size: 12px;">操作人：{{ item.operator || '-' }}</span>
              </div>
              <p style="margin: 8px 0 4px; color: #606266;">{{ item.operationDesc || '-' }}</p>
              <div v-if="item.beforeValue || item.afterValue" style="font-size: 12px; color: #909399;">
                <span v-if="item.beforeValue">变更前：{{ item.beforeValue }}</span>
                <span v-if="item.beforeValue && item.afterValue"> → </span>
                <span v-if="item.afterValue">变更后：{{ item.afterValue }}</span>
              </div>
              <div v-if="item.amount" style="font-size: 12px; color: #909399; margin-top: 4px;">
                金额：{{ Number(item.amount).toLocaleString() }}
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
      <div slot="footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 延期冻结对话框 -->
    <el-dialog title="延期冻结" :visible.sync="extendDialogVisible" width="500px" append-to-body>
      <el-form ref="extendFormRef" :model="extendForm" :rules="extendRules" label-width="120px">
        <el-form-item label="冻结标题">
          <span>{{ extendForm.freezeTitle }}</span>
        </el-form-item>
        <el-form-item label="当前计划日期">
          <span>{{ extendForm.currentPlannedDate || '未设置' }}</span>
        </el-form-item>
        <el-form-item label="新解冻日期" prop="newPlannedUnfreezeDate">
          <el-date-picker
            v-model="extendForm.newPlannedUnfreezeDate"
            type="date"
            placeholder="选择新的计划解冻日期"
            value-format="yyyy-MM-dd"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="延期原因" prop="reason">
          <el-input
            v-model="extendForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入延期原因"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="extendDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="extendLoading" @click="handleSubmitExtend">确定</el-button>
      </div>
    </el-dialog>

    <!-- 导入冻结对话框 -->
    <el-dialog title="导入冻结" :visible.sync="importDialogVisible" width="500px" append-to-body>
      <el-upload
        drag
        action=""
        :auto-upload="false"
        :on-change="handleImportUpload"
        :limit="1"
        accept=".xls,.xlsx"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传 Excel 文件（.xls/.xlsx），且不超过 10MB</div>
      </el-upload>
      <div slot="footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="importLoading" @click="handleSubmitImport">确定导入</el-button>
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
import { budgetFreezeApi } from '@/api/managementAccountant/ncv65/budgetControl'
import { export_json_to_excel } from '@/utils/excel'

export default {
  name: 'BudgetFreeze',
  data() {
    return {
      // 查询参数
      queryForm: {
        freezeTitle: '',
        freezeType: '',
        organizationPath: '',
        budgetAccount: '',
        freezeStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      freezeList: [],
      total: 0,
      selectedRows: [],
      
      // 实时监控
      realTimeMonitor: false,
      monitorTimer: null,
      
      // 统计数据
      freezeStats: {
        totalFreezes: 0,
        activeFreezes: 0,
        frozenAmount: 0,
        pendingCount: 0,
        activeRate: 0,
        frozenRate: 0,
        pendingRate: 0
      },
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      dialogMode: 'create',
      freezeForm: {
        freezeId: null,
        freezeTitle: '',
        freezeCode: '',
        freezeType: '',
        freezeAmount: 0,
        organizationId: '',
        budgetAccountId: '',
        applyDate: '',
        plannedUnfreezeDate: '',
        freezeReason: '',
        freezeDescription: '',
        urgencyLevel: 'MEDIUM',
        approver: '',
        notificationMethod: 'SYSTEM',
        autoApprove: false,
        allowPartialUnfreeze: true,
        sendNotification: true
      },
      freezeRules: {
        freezeTitle: [
          { required: true, message: '请输入冻结标题', trigger: 'blur' }
        ],
        freezeCode: [
          { required: true, message: '请输入冻结编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '冻结编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        freezeType: [
          { required: true, message: '请选择冻结类型', trigger: 'change' }
        ],
        freezeAmount: [
          { required: true, message: '请输入冻结金额', trigger: 'blur' }
        ]
      },
      
      // 选项数据
      freezeTypeOptions: [
        { value: 'FULL', label: '全额冻结' },
        { value: 'PARTIAL', label: '部分冻结' }
      ],
      freezeStatusOptions: [
        { value: 'FROZEN', label: '冻结中' },
        { value: 'RELEASED', label: '已释放' },
        { value: 'PARTIAL_RELEASED', label: '部分释放' }
      ],
      organizationOptions: [],
      budgetAccountOptions: [],
      userOptions: [],

      // 列设置
      columnSettingVisible: false,
      columnOptions: [
        { key: 'freezeCode', label: '冻结编码' },
        { key: 'freezeTitle', label: '冻结标题' },
        { key: 'freezeType', label: '冻结类型' },
        { key: 'organizationName', label: '组织单元' },
        { key: 'budgetAccountName', label: '预算科目' },
        { key: 'freezeAmount', label: '冻结金额' },
        { key: 'freezeReason', label: '冻结原因' },
        { key: 'freezeStatus', label: '冻结状态' },
        { key: 'approvalStatus', label: '审批状态' },
        { key: 'applicant', label: '申请人' },
        { key: 'applyDate', label: '申请日期' },
        { key: 'freezeDate', label: '冻结日期' }
      ],
      columnChecked: ['freezeCode', 'freezeTitle', 'freezeType', 'organizationName', 'budgetAccountName', 'freezeAmount', 'freezeReason', 'freezeStatus', 'approvalStatus', 'applicant', 'applyDate', 'freezeDate'],
      columnVisible: {
        freezeCode: true, freezeTitle: true, freezeType: true,
        organizationName: true, budgetAccountName: true, freezeAmount: true,
        freezeReason: true, freezeStatus: true, approvalStatus: true,
        applicant: true, applyDate: true, freezeDate: true
      },

      // 冻结历史弹框
      historyDialogVisible: false,
      historyLoading: false,
      historyList: [],
      historyFreezeTitle: '',

      // 延期冻结弹框
      extendDialogVisible: false,
      extendLoading: false,
      extendForm: {
        freezeId: '',
        freezeTitle: '',
        currentPlannedDate: '',
        newPlannedUnfreezeDate: '',
        reason: ''
      },
      extendRules: {
        newPlannedUnfreezeDate: [
          { required: true, message: '请选择新的计划解冻日期', trigger: 'change' }
        ],
        reason: [
          { required: true, message: '请输入延期原因', trigger: 'blur' }
        ]
      },

      // 导入冻结弹框
      importDialogVisible: false,
      importLoading: false,
      importFile: null
    }
  },
  
  created() {
    this.getList()
    this.loadStats()
    this.loadOrganizationOptions()
    this.loadBudgetAccountOptions()
    this.loadUserOptions()
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
        const response = await budgetFreezeApi.getStatistics()
        if (response.code === 1 && response.data) {
          const d = response.data
          const total = d.totalCount || 0
          const active = d.activeCount || 0
          this.freezeStats = {
            totalFreezes: total,
            activeFreezes: active,
            frozenAmount: d.frozenAmount || 0,
            pendingCount: d.pendingCount || 0,
            activeRate: total > 0 ? parseFloat(((active / total) * 100).toFixed(1)) : 0,
            frozenRate: d.frozenRate || 0,
            pendingRate: total > 0 ? parseFloat((((d.pendingCount || 0) / total) * 100).toFixed(1)) : 0
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
        const response = await budgetFreezeApi.getPage(params)
        this.freezeList = response.data.tlist || response.data.list || []
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
        const response = await budgetFreezeApi.getOrganizations()
        if (response.code === 1 && Array.isArray(response.data)) {
          // 后端返回 {id, name, code}，cascader 需要 {value, label}
          this.organizationOptions = response.data.map(item => ({
            value: item.id,
            label: item.name || item.code
          }))
        } else {
          this.organizationOptions = []
        }
      } catch (error) {
        console.error('加载组织选项失败：', error)
        this.organizationOptions = []
      }
    },

    // 加载预算科目选项
    async loadBudgetAccountOptions() {
      try {
        const response = await budgetFreezeApi.getBudgetAccounts()
        if (response.code === 1 && Array.isArray(response.data)) {
          // 后端返回 {id, name, code}，el-option 需要 {value, label}
          this.budgetAccountOptions = response.data.map(item => ({
            value: item.id,
            label: item.name || item.code
          }))
        } else {
          this.budgetAccountOptions = []
        }
      } catch (error) {
        console.error('加载预算科目选项失败：', error)
        this.budgetAccountOptions = []
      }
    },

    // 加载用户选项
    async loadUserOptions() {
      try {
        const response = await budgetFreezeApi.getUsers()
        if (response.code === 1 && Array.isArray(response.data)) {
          this.userOptions = response.data
        } else {
          this.userOptions = []
        }
      } catch (error) {
        console.error('加载用户选项失败：', error)
        this.userOptions = []
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
        freezeTitle: '',
        freezeType: '',
        organizationPath: '',
        budgetAccount: '',
        freezeStatus: ''
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
    
    // 创建冻结
    handleCreateFreeze() {
      this.dialogTitle = '申请预算冻结'
      this.dialogMode = 'create'
      this.dialogVisible = true
      this.resetForm()
    },

    // 将行数据转为表单数据（0/1 转回 boolean 供 checkbox 使用）
    toFormData(row) {
      return {
        ...row,
        autoApprove: !!row.autoApprove,
        allowPartialUnfreeze: !!row.allowPartialUnfreeze,
        sendNotification: !!row.sendNotification,
      }
    },

    // 查看冻结（弹框详情）
    handleView(row) {
      this.dialogTitle = '冻结详情'
      this.dialogMode = 'view'
      this.freezeForm = this.toFormData(row)
      this.dialogVisible = true
    },

    // 编辑冻结
    handleEdit(row) {
      this.dialogTitle = '编辑预算冻结'
      this.dialogMode = 'edit'
      this.freezeForm = this.toFormData(row)
      this.dialogVisible = true
    },

    // 审批冻结（弹框详情）
    handleApprove(row) {
      this.dialogTitle = '审批冻结'
      this.dialogMode = 'view'
      this.freezeForm = this.toFormData(row)
      this.dialogVisible = true
    },
    
    // 解冻
    async handleUnfreeze(row) {
      try {
        await this.$confirm('确认解冻该预算？解冻后预算将恢复正常使用', '提示', {
          type: 'warning'
        })
        await budgetFreezeApi.unfreeze(row.freezeId)
        this.$message.success('解冻成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('解冻失败：' + error.message)
        }
      }
    },
    
    // 验证冻结
    async handleValidateFreeze() {
      try {
        const params = {
          freezeAmount: this.freezeForm.freezeAmount,
          organizationId: this.freezeForm.organizationId,
          budgetAccountId: this.freezeForm.budgetAccountId
        }
        const response = await budgetFreezeApi.validateFreeze(params)
        const validation = response.data
        
        this.$alert(`
          <p>可冻结金额：${this.formatAmount(validation.availableAmount)}</p>
          <p>申请冻结金额：${this.formatAmount(validation.requestAmount)}</p>
          <p>验证结果：${validation.valid ? '通过' : '不通过'}</p>
          <p>验证说明：${validation.message}</p>
        `, '冻结验证结果', {
          dangerouslyUseHTMLString: true,
          type: validation.valid ? 'success' : 'warning'
        })
      } catch (error) {
        this.$message.error('验证失败：' + error.message)
      }
    },
    
    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.freezeForm.validate()

        const params = { ...this.freezeForm }
        // 日期格式化为 YYYY-MM-DD，后端 @JsonFormat 要求此格式
        if (params.applyDate instanceof Date) {
          params.applyDate = params.applyDate.toISOString().slice(0, 10)
        } else if (typeof params.applyDate === 'string' && params.applyDate.length > 10) {
          params.applyDate = params.applyDate.slice(0, 10)
        }
        if (params.plannedUnfreezeDate instanceof Date) {
          params.plannedUnfreezeDate = params.plannedUnfreezeDate.toISOString().slice(0, 10)
        } else if (typeof params.plannedUnfreezeDate === 'string' && params.plannedUnfreezeDate.length > 10) {
          params.plannedUnfreezeDate = params.plannedUnfreezeDate.slice(0, 10)
        }
        // 后端字段类型适配：approver 为 String，布尔字段为 Integer(0/1)
        if (params.approver != null) {
          params.approver = String(params.approver)
        }
        params.autoApprove = params.autoApprove ? 1 : 0
        params.allowPartialUnfreeze = params.allowPartialUnfreeze ? 1 : 0
        params.sendNotification = params.sendNotification ? 1 : 0

        // 补充派生字段：根据选中的 ID 查找对应的名称
        if (params.organizationId) {
          const org = this.organizationOptions.find(o => o.value === params.organizationId)
          if (org) params.organizationName = org.label
        }
        if (params.budgetAccountId) {
          const acct = this.budgetAccountOptions.find(a => a.value === params.budgetAccountId)
          if (acct) params.budgetAccountName = acct.label
        }
        // 申请人：从审批人选项中取当前 approver 的名称，或使用当前登录用户
        if (!params.applicant) {
          if (params.approver) {
            const user = this.userOptions.find(u => String(u.id) === String(params.approver))
            if (user) params.applicant = user.name
          }
          if (!params.applicant) {
            params.applicant = this.$store.getters.name || this.$store.getters.username || '当前用户'
          }
        }
        // 冻结日期：新增时默认为今天
        if (!params.freezeDate) {
          params.freezeDate = new Date().toISOString().slice(0, 10)
        }

        if (this.freezeForm.freezeId) {
          await budgetFreezeApi.update(this.freezeForm.freezeId, params)
          this.$message.success('更新成功')
        } else {
          await budgetFreezeApi.create(params)
          this.$message.success('申请提交成功')
        }

        this.dialogVisible = false
        this.getList()
        this.loadStats()
      } catch (error) {
        if (error !== false) {
          this.$message.error('操作失败：' + (error.message || '表单验证未通过'))
        }
      }
    },
    
    // 重置表单
    resetForm() {
      this.freezeForm = {
        freezeId: null,
        freezeTitle: '',
        freezeCode: '',
        freezeType: '',
        freezeAmount: 0,
        organizationId: '',
        budgetAccountId: '',
        applyDate: '',
        plannedUnfreezeDate: '',
        freezeReason: '',
        freezeDescription: '',
        urgencyLevel: 'MEDIUM',
        approver: '',
        notificationMethod: 'SYSTEM',
        autoApprove: false,
        allowPartialUnfreeze: true,
        sendNotification: true
      }
      this.$nextTick(() => {
        this.$refs.freezeForm && this.$refs.freezeForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入冻结
    handleImportFreezes() {
      this.importDialogVisible = true
    },

    // 处理导入文件上传
    handleImportUpload(file) {
      const allowTypes = [
        'application/vnd.ms-excel',
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      ]
      if (!allowTypes.includes(file.raw.type)) {
        this.$message.error('只支持上传 Excel 文件（.xls / .xlsx）')
        return false
      }
      if (file.raw.size > 10 * 1024 * 1024) {
        this.$message.error('文件大小不能超过 10MB')
        return false
      }
      this.importFile = file.raw
    },

    async handleSubmitImport() {
      if (!this.importFile) {
        this.$message.warning('请先选择文件')
        return
      }
      this.importLoading = true
      try {
        const formData = new FormData()
        formData.append('file', this.importFile)
        const res = await budgetFreezeApi.importData(formData)
        if (res.code === 1) {
          const d = res.data || {}
          this.$message.success(`导入成功，共 ${d.successCount || 0} 条`)
          this.importDialogVisible = false
          this.importFile = null
          this.getList()
          this.loadStats()
        } else {
          this.$message.error(res.msg || '导入失败')
        }
      } catch (e) {
        console.error('导入异常', e)
        this.$message.error('导入失败：' + (e.message || '未知错误'))
      } finally {
        this.importLoading = false
      }
    },

    // 批量解冻
    async handleBatchUnfreeze() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要解冻的项目')
        return
      }

      try {
        await this.$confirm(`确认批量解冻选中的 ${this.selectedRows.length} 条记录？`, '提示', {
          type: 'warning'
        })
        const ids = this.selectedRows.map(row => row.freezeId)
        const response = await budgetFreezeApi.batchUnfreeze({ freezeIds: ids })
        if (response.code === 1) {
          this.$message.success('批量解冻成功')
          this.getList()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '批量解冻失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量解冻失败：' + (error.message || '未知错误'))
        }
      }
    },

    // 导出冻结
    handleExportFreezes() {
      // 如果有选中数据则导出选中数据，否则导出当前页面数据
      const exportData = this.selectedRows.length > 0 ? this.selectedRows : this.freezeList
      if (!exportData || exportData.length === 0) {
        this.$message.warning('没有可导出的数据')
        return
      }
      const header = ['冻结编码', '冻结标题', '冻结类型', '组织单元', '预算科目', '冻结金额', '冻结原因', '冻结状态', '审批状态', '申请人', '申请日期', '冻结日期']
      const filterVal = ['freezeCode', 'freezeTitle', 'freezeType', 'organizationName', 'budgetAccountName', 'freezeAmount', 'freezeReason', 'freezeStatus', 'approvalStatus', 'applicant', 'applyDate', 'freezeDate']
      const statusMap = { FROZEN: '冻结中', UNFROZEN: '已解冻', PENDING: '待审批', EXPIRED: '已过期' }
      const approvalMap = { APPROVED: '已审批', PENDING: '待审批', REJECTED: '已拒绝' }
      const typeMap = { FULL: '完全冻结', PARTIAL: '部分冻结', TEMPORARY: '临时冻结' }
      const data = exportData.map(row => filterVal.map(key => {
        if (key === 'freezeStatus') return statusMap[row[key]] || row[key] || ''
        if (key === 'approvalStatus') return approvalMap[row[key]] || row[key] || ''
        if (key === 'freezeType') return typeMap[row[key]] || row[key] || ''
        return row[key] != null ? String(row[key]) : ''
      }))
      const suffix = this.selectedRows.length > 0 ? '_选中' : '_当前页'
      export_json_to_excel({ header, data, filename: '预算冻结数据' + suffix, autoWidth: true })
      this.$message.success(`导出成功，共 ${exportData.length} 条数据`)
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
        case 'extend':
          this.handleExtend(row)
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
    async handleHistory(row) {
      this.historyFreezeTitle = row.freezeTitle || row.freezeCode || ''
      this.historyList = []
      this.historyDialogVisible = true
      this.historyLoading = true
      try {
        const res = await budgetFreezeApi.getHistory(row.freezeId)
        if (res.code === 1) {
          this.historyList = res.data || []
        } else {
          this.$message.warning(res.msg || '查询历史失败')
        }
      } catch (e) {
        console.error('查询冻结历史异常', e)
        this.$message.error('查询冻结历史失败')
      } finally {
        this.historyLoading = false
      }
    },

    // 延期冻结
    handleExtend(row) {
      if (row.freezeStatus !== 'FROZEN') {
        this.$message.warning('只有冻结中的记录才能延期')
        return
      }
      this.extendForm = {
        freezeId: row.freezeId,
        freezeTitle: row.freezeTitle || '',
        currentPlannedDate: row.plannedUnfreezeDate || '',
        newPlannedUnfreezeDate: '',
        reason: ''
      }
      this.extendDialogVisible = true
    },

    // 提交延期冻结
    async handleSubmitExtend() {
      this.$refs.extendFormRef.validate(async (valid) => {
        if (!valid) return
        this.extendLoading = true
        try {
          const res = await budgetFreezeApi.extendFreeze(this.extendForm.freezeId, {
            newPlannedUnfreezeDate: this.extendForm.newPlannedUnfreezeDate,
            reason: this.extendForm.reason
          })
          if (res.code === 1) {
            this.$message.success('延期成功')
            this.extendDialogVisible = false
            this.getList()
          } else {
            this.$message.error(res.msg || '延期失败')
          }
        } catch (e) {
          console.error('延期冻结异常', e)
          this.$message.error('延期冻结失败')
        } finally {
          this.extendLoading = false
        }
      })
    },

    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算冻结'
      this.dialogMode = 'create'
      this.dialogVisible = true
      this.freezeForm = { ...this.toFormData(row), freezeId: null, freezeCode: null }
    },
    
    // 导出单个
    handleExportSingle(row) {
      const header = ['冻结编码', '冻结标题', '冻结类型', '组织单元', '预算科目', '冻结金额', '冻结原因', '冻结状态', '审批状态', '申请人', '申请日期', '冻结日期']
      const filterVal = ['freezeCode', 'freezeTitle', 'freezeType', 'organizationName', 'budgetAccountName', 'freezeAmount', 'freezeReason', 'freezeStatus', 'approvalStatus', 'applicant', 'applyDate', 'freezeDate']
      const statusMap = { FROZEN: '冻结中', UNFROZEN: '已解冻', PENDING: '待审批', EXPIRED: '已过期' }
      const approvalMap = { APPROVED: '已审批', PENDING: '待审批', REJECTED: '已拒绝' }
      const typeMap = { FULL: '完全冻结', PARTIAL: '部分冻结', TEMPORARY: '临时冻结' }
      const data = [filterVal.map(key => {
        if (key === 'freezeStatus') return statusMap[row[key]] || row[key] || ''
        if (key === 'approvalStatus') return approvalMap[row[key]] || row[key] || ''
        if (key === 'freezeType') return typeMap[row[key]] || row[key] || ''
        return row[key] != null ? String(row[key]) : ''
      })]
      export_json_to_excel({ header, data, filename: '预算冻结_' + (row.freezeCode || row.freezeTitle), autoWidth: true })
      this.$message.success('导出成功')
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该冻结记录？删除后不可恢复', '提示', {
          type: 'warning'
        })
        await budgetFreezeApi.delete(row.freezeId)
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
    
    // 判断是否可以审批
    canApprove(row) {
      return row.approvalStatus === 'PENDING' && row.approver === this.$store.getters.userId
    },
    
    // 判断是否可以解冻
    canUnfreeze(row) {
      return row.freezeStatus === 'FROZEN'
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取冻结类型颜色
    getFreezeTypeColor(type) {
      const colorMap = {
        'FULL': 'danger',
        'PARTIAL': 'warning'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取冻结类型文本
    getFreezeTypeText(type) {
      const item = this.freezeTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取冻结状态类型
    getFreezeStatusType(status) {
      const statusMap = {
        'FROZEN': 'danger',
        'RELEASED': 'success',
        'PARTIAL_RELEASED': 'warning'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取冻结状态文本
    getFreezeStatusText(status) {
      const item = this.freezeStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    },
    
    // 获取审批状态类型
    getApprovalStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取审批状态文本
    getApprovalStatusText(status) {
      const statusMap = {
        'PENDING': '待审批',
        'APPROVED': '已审批',
        'REJECTED': '已拒绝'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-freeze {
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
      
      &.frozen-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.pending-card {
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
