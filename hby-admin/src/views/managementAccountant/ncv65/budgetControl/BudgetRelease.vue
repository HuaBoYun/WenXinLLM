<template>
  <div class="budget-release">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算释放管理</h2>
      <p>管理预算释放操作，包括释放申请、审批流程、释放监控和资金回收</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-unlock" @click="handleCreateRelease">申请释放</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportReleases">导入释放</el-button>
            <el-button type="warning" icon="el-icon-check" @click="handleBatchApprove">批量审批</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportReleases">导出释放</el-button>
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

    <!-- 释放统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ releaseStats.totalReleases }}</div>
            <div class="stat-label">释放总数</div>
            <div class="stat-description">所有释放申请数量</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-unlock"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card approved-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ releaseStats.approvedReleases }}</div>
            <div class="stat-label">已审批</div>
            <div class="stat-description">已审批的释放申请</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="releaseStats.approvalRate" 
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
        <el-card class="stat-card released-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ releaseStats.releasedAmount }}</div>
            <div class="stat-label">释放金额</div>
            <div class="stat-description">已释放的预算金额</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="releaseStats.releaseRate" 
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
            <div class="stat-number">{{ releaseStats.pendingCount }}</div>
            <div class="stat-label">待处理</div>
            <div class="stat-description">待处理的释放申请</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="releaseStats.pendingRate" 
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
        <el-form-item label="释放标题">
          <el-input
            v-model="queryForm.releaseTitle"
            placeholder="请输入释放标题"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="释放类型">
          <el-select
            v-model="queryForm.releaseType"
            placeholder="请选择释放类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in releaseTypeOptions"
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
        <el-form-item label="释放状态">
          <el-select
            v-model="queryForm.releaseStatus"
            placeholder="请选择释放状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in releaseStatusOptions"
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

    <!-- 释放列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算释放列表</span>
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
        :data="releaseList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column v-if="columnVisible.releaseCode" prop="releaseCode" label="释放编码" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.releaseTitle" prop="releaseTitle" label="释放标题" min-width="200" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.releaseType" prop="releaseType" label="释放类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getReleaseTypeColor(scope.row.releaseType)">
              {{ getReleaseTypeText(scope.row.releaseType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.organizationName" prop="organizationName" label="组织单元" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.budgetAccountName" prop="budgetAccountName" label="预算科目" width="150" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.releaseAmount" prop="releaseAmount" label="释放金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.releaseAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.releaseReason" prop="releaseReason" label="释放原因" width="150" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.releaseStatus" prop="releaseStatus" label="释放状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getReleaseStatusType(scope.row.releaseStatus)" size="mini">
              {{ getReleaseStatusText(scope.row.releaseStatus) }}
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
        <el-table-column v-if="columnVisible.releaseDate" prop="releaseDate" label="释放日期" width="120" align="center" />
        
        <el-table-column label="操作" width="260" align="center" fixed="right">
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
              v-if="canApprove(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-check"
              class="success-text"
              @click.stop="handleApprove(scope.row)"
            >审批</el-button>
            <el-button
              v-if="canExecute(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-unlock"
              class="warning-text"
              @click.stop="handleExecute(scope.row)"
            >执行</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini" @click.stop>
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
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

    <!-- 新增/编辑释放对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="releaseForm"
        :model="releaseForm"
        :rules="releaseRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="释放标题" prop="releaseTitle">
              <el-input
                v-model="releaseForm.releaseTitle"
                placeholder="请输入释放标题"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="释放编码" prop="releaseCode">
              <el-input
                v-model="releaseForm.releaseCode"
                placeholder="请输入释放编码"
                :disabled="!!releaseForm.id"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="释放类型" prop="releaseType">
              <el-select
                v-model="releaseForm.releaseType"
                placeholder="请选择释放类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in releaseTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="释放金额" prop="releaseAmount">
              <el-input-number
                v-model="releaseForm.releaseAmount"
                :precision="2"
                :min="0"
                placeholder="请输入释放金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组织单元" prop="organizationId">
              <el-cascader
                v-model="releaseForm.organizationId"
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
                v-model="releaseForm.budgetAccountId"
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
                v-model="releaseForm.applyDate"
                type="date"
                placeholder="请选择申请日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划释放日期" prop="plannedReleaseDate">
              <el-date-picker
                v-model="releaseForm.plannedReleaseDate"
                type="date"
                placeholder="请选择计划释放日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="释放原因" prop="releaseReason">
          <el-input
            v-model="releaseForm.releaseReason"
            type="textarea"
            :rows="3"
            placeholder="请输入释放原因"
          />
        </el-form-item>
        
        <el-form-item label="释放说明" prop="releaseDescription">
          <el-input
            v-model="releaseForm.releaseDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入释放说明"
          />
        </el-form-item>
        
        <!-- 审批配置 -->
        <el-form-item label="审批配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="紧急程度" prop="urgencyLevel">
                <el-select
                  v-model="releaseForm.urgencyLevel"
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
                  v-model="releaseForm.approver"
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
                  v-model="releaseForm.notificationMethod"
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
        
        <!-- 释放配置 -->
        <el-form-item label="释放配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="释放方式" prop="releaseMethod">
                <el-select
                  v-model="releaseForm.releaseMethod"
                  placeholder="释放方式"
                  style="width: 100%"
                >
                  <el-option value="IMMEDIATE" label="立即释放" />
                  <el-option value="SCHEDULED" label="定时释放" />
                  <el-option value="CONDITIONAL" label="条件释放" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="回收方式" prop="recoveryMethod">
                <el-select
                  v-model="releaseForm.recoveryMethod"
                  placeholder="回收方式"
                  style="width: 100%"
                >
                  <el-option value="RETURN" label="返回原账户" />
                  <el-option value="REALLOCATE" label="重新分配" />
                  <el-option value="RESERVE" label="转入储备" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="优先级" prop="priority">
                <el-select
                  v-model="releaseForm.priority"
                  placeholder="优先级"
                  style="width: 100%"
                >
                  <el-option value="HIGH" label="高" />
                  <el-option value="MEDIUM" label="中" />
                  <el-option value="LOW" label="低" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>
        
        <el-form-item label="释放选项">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="releaseForm.autoApprove">自动审批</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="releaseForm.sendNotification">发送通知</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="releaseForm.trackProgress">跟踪进度</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleValidateRelease">验证释放</el-button>
        <el-button type="primary" @click="handleSubmitForm">提交申请</el-button>
      </div>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog title="释放申请详情" :visible.sync="detailDialogVisible" width="800px" :close-on-click-modal="false">
      <el-descriptions v-if="detailRow" :column="2" border size="small">
        <el-descriptions-item label="释放编码">{{ detailRow.releaseCode }}</el-descriptions-item>
        <el-descriptions-item label="释放标题">{{ detailRow.releaseTitle || detailRow.releaseName }}</el-descriptions-item>
        <el-descriptions-item label="释放类型">{{ getReleaseTypeText(detailRow.releaseType) }}</el-descriptions-item>
        <el-descriptions-item label="释放金额">{{ formatAmount(detailRow.releaseAmount) }}</el-descriptions-item>
        <el-descriptions-item label="组织单元">{{ detailRow.organizationName }}</el-descriptions-item>
        <el-descriptions-item label="预算科目">{{ detailRow.budgetAccountName }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ detailRow.applicant }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ detailRow.applyDate }}</el-descriptions-item>
        <el-descriptions-item label="计划释放日期">{{ detailRow.plannedReleaseDate }}</el-descriptions-item>
        <el-descriptions-item label="释放日期">{{ detailRow.releaseDate }}</el-descriptions-item>
        <el-descriptions-item label="释放状态">
          <el-tag :type="getReleaseStatusType(detailRow.releaseStatus)" size="mini">{{ getReleaseStatusText(detailRow.releaseStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审批状态">
          <el-tag :type="getApprovalStatusType(detailRow.approvalStatus)" size="mini">{{ getApprovalStatusText(detailRow.approvalStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="释放原因" :span="2">{{ detailRow.releaseReason }}</el-descriptions-item>
        <el-descriptions-item label="释放说明" :span="2">{{ detailRow.releaseDescription }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button type="primary" icon="el-icon-edit" @click="handleEditFromDetail">编辑</el-button>
      </div>
    </el-dialog>

    <!-- 批量审批弹窗 -->
    <el-dialog title="批量审批" :visible.sync="batchApproveDialogVisible" width="500px" :close-on-click-modal="false">
      <p>已选择 <strong>{{ selectedRows.length }}</strong> 条记录，确认批量审批通过？</p>
      <el-table :data="selectedRows" size="mini" border max-height="260">
        <el-table-column prop="releaseCode" label="释放编码" width="140" />
        <el-table-column prop="releaseTitle" label="释放标题" show-overflow-tooltip />
        <el-table-column prop="releaseAmount" label="金额" width="120" align="right">
          <template slot-scope="scope">{{ formatAmount(scope.row.releaseAmount) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="getReleaseStatusType(scope.row.releaseStatus)" size="mini">{{ getReleaseStatusText(scope.row.releaseStatus) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="batchApproveDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="batchApproveLoading" @click="doConfirmBatchApprove">确认审批</el-button>
      </div>
    </el-dialog>

    <!-- 导入释放对话框 -->
    <el-dialog title="导入释放申请" :visible.sync="importDialogVisible" width="500px" :close-on-click-modal="false">
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
import { budgetReleaseApi } from '@/api/managementAccountant/ncv65/budgetControl'

export default {
  name: 'BudgetRelease',
  data() {
    return {
      // 查询参数
      queryForm: {
        releaseTitle: '',
        releaseType: '',
        organizationPath: '',
        budgetAccount: '',
        releaseStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      releaseList: [],
      total: 0,
      selectedRows: [],
      
      // 实时监控
      realTimeMonitor: false,
      monitorTimer: null,
      
      // 统计数据
      releaseStats: {
        totalReleases: 0,
        approvedReleases: 0,
        releasedAmount: 0,
        pendingCount: 0,
        approvalRate: 0,
        releaseRate: 0,
        pendingRate: 0
      },
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      releaseForm: {
        id: null,
        releaseTitle: '',
        releaseCode: '',
        releaseType: '',
        releaseAmount: 0,
        organizationId: '',
        budgetAccountId: '',
        applyDate: '',
        plannedReleaseDate: '',
        releaseReason: '',
        releaseDescription: '',
        urgencyLevel: 'MEDIUM',
        approver: '',
        notificationMethod: 'SYSTEM',
        releaseMethod: 'IMMEDIATE',
        recoveryMethod: 'RETURN',
        priority: 'MEDIUM',
        autoApprove: false,
        sendNotification: true,
        trackProgress: true
      },
      releaseRules: {
        releaseTitle: [
          { required: true, message: '请输入释放标题', trigger: 'blur' }
        ],
        releaseCode: [
          { required: true, message: '请输入释放编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '释放编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        releaseType: [
          { required: true, message: '请选择释放类型', trigger: 'change' }
        ],
        releaseAmount: [
          { required: true, message: '请输入释放金额', trigger: 'blur' }
        ]
      },
      
      // 选项数据
      releaseTypeOptions: [
        { value: 'COMPLETION', label: '项目完成释放' },
        { value: 'CANCELLATION', label: '项目取消释放' },
        { value: 'ADJUSTMENT', label: '预算调整释放' },
        { value: 'SURPLUS', label: '结余资金释放' },
        { value: 'REALLOCATION', label: '重新分配释放' },
        { value: 'CUSTOM', label: '自定义释放' }
      ],
      releaseStatusOptions: [
        { value: 'PENDING', label: '待审批' },
        { value: 'APPROVED', label: '已审批' },
        { value: 'EXECUTING', label: '执行中' },
        { value: 'COMPLETED', label: '已完成' },
        { value: 'REJECTED', label: '已拒绝' }
      ],
      organizationOptions: [],
      budgetAccountOptions: [],
      userOptions: [],

      // 详情弹窗
      detailDialogVisible: false,
      detailRow: null,

      // 批量审批弹窗
      batchApproveDialogVisible: false,
      batchApproveLoading: false,

      // 导入
      importDialogVisible: false,
      importFileList: [],
      importLoading: false,
      importFile: null,

      // 列设置
      columnSettingVisible: false,
      columnOptions: [
        { key: 'releaseCode', label: '释放编码' },
        { key: 'releaseTitle', label: '释放标题' },
        { key: 'releaseType', label: '释放类型' },
        { key: 'organizationName', label: '组织单元' },
        { key: 'budgetAccountName', label: '预算科目' },
        { key: 'releaseAmount', label: '释放金额' },
        { key: 'releaseReason', label: '释放原因' },
        { key: 'releaseStatus', label: '释放状态' },
        { key: 'approvalStatus', label: '审批状态' },
        { key: 'applicant', label: '申请人' },
        { key: 'applyDate', label: '申请日期' },
        { key: 'releaseDate', label: '释放日期' }
      ],
      columnChecked: ['releaseCode', 'releaseTitle', 'releaseType', 'organizationName', 'budgetAccountName', 'releaseAmount', 'releaseReason', 'releaseStatus', 'approvalStatus', 'applicant', 'applyDate', 'releaseDate'],
      columnVisible: {
        releaseCode: true, releaseTitle: true, releaseType: true,
        organizationName: true, budgetAccountName: true, releaseAmount: true,
        releaseReason: true, releaseStatus: true, approvalStatus: true,
        applicant: true, applyDate: true, releaseDate: true
      }
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
        const response = await budgetReleaseApi.getStatistics()
        if (response.code === 1 && response.data) {
          const d = response.data
          const total = d.totalCount || 0
          const approved = d.approvedCount || 0
          this.releaseStats = {
            totalReleases: total,
            approvedReleases: approved,
            releasedAmount: d.releasedAmount || 0,
            pendingCount: d.pendingCount || 0,
            approvalRate: total > 0 ? parseFloat(((approved / total) * 100).toFixed(1)) : 0,
            releaseRate: d.releaseRate || 0,
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
        const response = await budgetReleaseApi.getPage(params)
        this.releaseList = response.data.tlist || response.data.list || []
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
        const response = await budgetReleaseApi.getOrganizations()
        this.organizationOptions = response.data
      } catch (error) {
        console.error('加载组织选项失败：', error)
      }
    },
    
    // 加载预算科目选项
    async loadBudgetAccountOptions() {
      try {
        const response = await budgetReleaseApi.getBudgetAccounts()
        this.budgetAccountOptions = response.data
      } catch (error) {
        console.error('加载预算科目选项失败：', error)
      }
    },
    
    // 加载用户选项
    async loadUserOptions() {
      try {
        const response = await budgetReleaseApi.getUsers()
        this.userOptions = response.data
      } catch (error) {
        console.error('加载用户选项失败：', error)
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
        releaseTitle: '',
        releaseType: '',
        organizationPath: '',
        budgetAccount: '',
        releaseStatus: ''
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
    
    // 创建释放
    handleCreateRelease() {
      this.dialogTitle = '申请预算释放'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 查看释放（详情弹窗）
    handleView(row) {
      this.detailRow = row
      this.detailDialogVisible = true
    },

    // 编辑释放（打开新增/编辑弹窗，回填数据）
    handleEdit(row) {
      this.dialogTitle = '编辑预算释放'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.releaseForm = {
          id: row.releaseId,
          releaseTitle: row.releaseTitle || row.releaseName || '',
          releaseCode: row.releaseCode || '',
          releaseType: row.releaseType || '',
          releaseAmount: row.releaseAmount || 0,
          organizationId: row.organizationId || '',
          budgetAccountId: row.budgetAccountId || '',
          applyDate: row.applyDate || '',
          plannedReleaseDate: row.plannedReleaseDate || '',
          releaseReason: row.releaseReason || '',
          releaseDescription: row.releaseDescription || '',
          urgencyLevel: row.urgencyLevel || 'MEDIUM',
          approver: row.approveBy || '',
          notificationMethod: row.notificationMethod || 'SYSTEM',
          releaseMethod: row.releaseMethod || 'IMMEDIATE',
          recoveryMethod: row.recoveryMethod || 'RETURN',
          priority: row.priority || 'MEDIUM',
          autoApprove: row.autoApprove || false,
          sendNotification: row.sendNotification !== false,
          trackProgress: row.trackProgress !== false
        }
      })
    },

    // 从详情弹窗点击编辑
    handleEditFromDetail() {
      this.detailDialogVisible = false
      this.handleEdit(this.detailRow)
    },

    // 审批释放（在详情弹窗内审批，或行内直接审批确认）
    async handleApprove(row) {
      try {
        await this.$confirm(`确认审批通过「${row.releaseTitle || row.releaseName}」？`, '审批确认', {
          type: 'warning',
          confirmButtonText: '确认通过',
          cancelButtonText: '取消'
        })
        await budgetReleaseApi.approve(row.releaseId, { approveStatus: 'APPROVED' })
        this.$message.success('审批成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('审批失败：' + (error.message || error))
        }
      }
    },
    
    // 执行释放
    async handleExecute(row) {
      try {
        await this.$confirm('确认执行该预算释放？执行后资金将按设定方式进行处理', '提示', {
          type: 'warning'
        })
        await budgetReleaseApi.execute(row.releaseId)
        this.$message.success('执行成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('执行失败：' + error.message)
        }
      }
    },
    
    // 验证释放
    async handleValidateRelease() {
      try {
        const params = {
          releaseAmount: this.releaseForm.releaseAmount,
          organizationId: this.releaseForm.organizationId,
          budgetAccountId: this.releaseForm.budgetAccountId
        }
        const response = await budgetReleaseApi.validateRelease(params)
        const validation = response.data
        
        this.$alert(`
          <p>可释放金额：${this.formatAmount(validation.availableAmount)}</p>
          <p>申请释放金额：${this.formatAmount(validation.requestAmount)}</p>
          <p>验证结果：${validation.valid ? '通过' : '不通过'}</p>
          <p>验证说明：${validation.message}</p>
        `, '释放验证结果', {
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
        await this.$refs.releaseForm.validate()
        
        const params = { ...this.releaseForm }
        
        if (this.releaseForm.id) {
          await budgetReleaseApi.update(this.releaseForm.id, params)
          this.$message.success('更新成功')
        } else {
          await budgetReleaseApi.create(params)
          this.$message.success('申请提交成功')
        }
        
        this.dialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },
    
    // 重置表单
    resetForm() {
      this.releaseForm = {
        id: null,
        releaseTitle: '',
        releaseCode: '',
        releaseType: '',
        releaseAmount: 0,
        organizationId: '',
        budgetAccountId: '',
        applyDate: '',
        plannedReleaseDate: '',
        releaseReason: '',
        releaseDescription: '',
        urgencyLevel: 'MEDIUM',
        approver: '',
        notificationMethod: 'SYSTEM',
        releaseMethod: 'IMMEDIATE',
        recoveryMethod: 'RETURN',
        priority: 'MEDIUM',
        autoApprove: false,
        sendNotification: true,
        trackProgress: true
      }
      this.$nextTick(() => {
        this.$refs.releaseForm && this.$refs.releaseForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入释放
    handleImportReleases() {
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
        const response = await budgetReleaseApi.import(formData)
        if (response.code === 1) {
          this.$message.success(`导入成功，共导入 ${response.data || 0} 条记录`)
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
    
    // 批量审批 —— 弹出确认弹窗
    handleBatchApprove() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请先勾选要审批的记录')
        return
      }
      this.batchApproveDialogVisible = true
    },

    // 确认批量审批
    async doConfirmBatchApprove() {
      this.batchApproveLoading = true
      try {
        // 使用 releaseId（后端字段），不是 id
        const ids = this.selectedRows.map(row => row.releaseId)
        const res = await budgetReleaseApi.batchApprove(ids)
        if (res && (res.code === 1 || res.code === 200)) {
          const d = res.data || {}
          this.$message.success(`批量审批完成，成功 ${d.successCount || ids.length} 条`)
        } else {
          this.$message.success('批量审批完成')
        }
        this.batchApproveDialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('批量审批失败：' + error.message)
      } finally {
        this.batchApproveLoading = false
      }
    },

    // 导出释放 —— 有选中则导出选中，否则导出当前页
    async handleExportReleases() {
      try {
        let params
        if (this.selectedRows.length > 0) {
          // 导出已选中数据：传选中的 releaseId 列表
          params = {
            releaseIds: this.selectedRows.map(r => r.releaseId),
            pageSize: this.selectedRows.length
          }
        } else {
          // 导出当前页：传当前查询条件 + 当前分页大小
          params = {
            ...this.queryForm,
            pageNum: this.queryParams.pageNum,
            pageSize: this.queryParams.pageSize
          }
        }
        const blob = await budgetReleaseApi.export(params)
        // 触发文件下载
        const url = window.URL.createObjectURL(new Blob([blob]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `预算释放数据_${new Date().toLocaleDateString()}.xlsx`)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success(`导出成功，共 ${this.selectedRows.length > 0 ? this.selectedRows.length : this.queryParams.pageSize} 条`)
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
      this.$router.push('/managementAccountant/ncv65/budgetControl/releaseSettings')
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
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

    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算释放'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.releaseForm = {
          id: null,
          releaseTitle: row.releaseTitle || row.releaseName || '',
          releaseCode: '',
          releaseType: row.releaseType || '',
          releaseAmount: row.releaseAmount || 0,
          organizationId: row.organizationId || '',
          budgetAccountId: row.budgetAccountId || '',
          applyDate: '',
          plannedReleaseDate: '',
          releaseReason: row.releaseReason || '',
          releaseDescription: row.releaseDescription || '',
          urgencyLevel: 'MEDIUM',
          approver: '',
          notificationMethod: 'SYSTEM',
          releaseMethod: 'IMMEDIATE',
          recoveryMethod: 'RETURN',
          priority: 'MEDIUM',
          autoApprove: false,
          sendNotification: true,
          trackProgress: true
        }
      })
    },

    // 导出单个
    async handleExportSingle(row) {
      try {
        const blob = await budgetReleaseApi.exportSingle(row.releaseId)
        const url = window.URL.createObjectURL(new Blob([blob]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `释放_${row.releaseCode || row.releaseId}.xlsx`)
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
        await this.$confirm('确认删除该释放记录？删除后不可恢复', '提示', {
          type: 'warning'
        })
        await budgetReleaseApi.delete(row.releaseId)
        this.$message.success('删除成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 行点击（不再自动打开详情，避免和 checkbox / 按钮 click 冲突）
    handleRowClick(row) {
      // 空实现，需要看详情请点击操作列的「详情」按钮
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

    // 应用列设置
    handleApplyColumns() {
      this.columnOptions.forEach(col => {
        this.$set(this.columnVisible, col.key, this.columnChecked.includes(col.key))
      })
      this.columnSettingVisible = false
    },

    // 重置列设置
    handleResetColumns() {
      this.columnChecked = this.columnOptions.map(col => col.key)
    },
    
    // 判断是否可以审批（状态为 PENDING 即可审批）
    canApprove(row) {
      return row.releaseStatus === 'PENDING' || row.approvalStatus === 'PENDING'
    },
    
    // 判断是否可以执行
    canExecute(row) {
      return row.releaseStatus === 'APPROVED'
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取释放类型颜色
    getReleaseTypeColor(type) {
      const colorMap = {
        'COMPLETION': 'success',
        'CANCELLATION': 'warning',
        'ADJUSTMENT': 'primary',
        'SURPLUS': 'info',
        'REALLOCATION': 'danger',
        'CUSTOM': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取释放类型文本
    getReleaseTypeText(type) {
      const item = this.releaseTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取释放状态类型
    getReleaseStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'primary',
        'EXECUTING': 'info',
        'COMPLETED': 'success',
        'REJECTED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取释放状态文本
    getReleaseStatusText(status) {
      const item = this.releaseStatusOptions.find(opt => opt.value === status)
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
.budget-release {
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
      
      &.approved-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.released-card {
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
