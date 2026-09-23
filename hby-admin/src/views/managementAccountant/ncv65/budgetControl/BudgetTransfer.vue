<template>
  <div class="budget-transfer">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算转移管理</h2>
      <p>管理预算转移操作，包括转移申请、审批流程、转移执行和资金调配</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-sort" @click="handleCreateTransfer">申请转移</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportTransfers">导入转移</el-button>
            <el-button type="warning" icon="el-icon-check" @click="handleBatchApprove">批量审批</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportTransfers">导出转移</el-button>
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

    <!-- 转移统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ transferStats.totalTransfers }}</div>
            <div class="stat-label">转移总数</div>
            <div class="stat-description">所有转移申请数量</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-sort"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card approved-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ transferStats.approvedTransfers }}</div>
            <div class="stat-label">已审批</div>
            <div class="stat-description">已审批的转移申请</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="transferStats.approvalRate" 
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
        <el-card class="stat-card transferred-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ transferStats.transferredAmount }}</div>
            <div class="stat-label">转移金额</div>
            <div class="stat-description">已转移的预算金额</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="transferStats.transferRate" 
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
        <el-card class="stat-card processing-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ transferStats.processingCount }}</div>
            <div class="stat-label">处理中</div>
            <div class="stat-description">正在处理的转移申请</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="transferStats.processingRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#F56C6C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-loading"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="转移标题">
          <el-input
            v-model="queryForm.transferTitle"
            placeholder="请输入转移标题"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="转移类型">
          <el-select
            v-model="queryForm.transferType"
            placeholder="请选择转移类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in transferTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="转出单元">
          <el-select
            v-model="queryForm.fromOrganization"
            placeholder="请选择转出单元"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in organizationOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="转入单元">
          <el-select
            v-model="queryForm.toOrganization"
            placeholder="请选择转入单元"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in organizationOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="转移状态">
          <el-select
            v-model="queryForm.transferStatus"
            placeholder="请选择转移状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in transferStatusOptions"
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

    <!-- 转移列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算转移列表</span>
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
        :data="transferList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column v-if="columnVisible.transferCode" prop="transferCode" label="转移编码" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.transferTitle" prop="transferTitle" label="转移标题" min-width="200" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.transferType" prop="transferType" label="转移类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getTransferTypeColor(scope.row.transferType)">
              {{ getTransferTypeText(scope.row.transferType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.fromOrganizationName" prop="fromOrganizationName" label="转出单元" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.toOrganizationName" prop="toOrganizationName" label="转入单元" width="150" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.transferAmount" prop="transferAmount" label="转移金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.transferAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.transferReason" prop="transferReason" label="转移原因" width="150" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.transferStatus" prop="transferStatus" label="转移状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTransferStatusType(scope.row.transferStatus)" size="mini">
              {{ getTransferStatusText(scope.row.transferStatus) }}
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
        <el-table-column v-if="columnVisible.transferDate" label="转移日期" width="120" align="center">
          <template slot-scope="scope">
            {{ scope.row.transferDate || scope.row.plannedTransferDate || '-' }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >详情</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              v-if="canApprove(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-check"
              class="success-text"
              @click="handleApprove(scope.row)"
            >审批</el-button>
            <el-button
              v-if="canExecute(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-sort"
              class="warning-text"
              @click="handleExecute(scope.row)"
            >执行</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="history" icon="el-icon-time">转移历史</el-dropdown-item>
                <el-dropdown-item command="track" icon="el-icon-location">跟踪进度</el-dropdown-item>
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

    <!-- 新增/编辑转移对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1000px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="transferForm"
        :model="transferForm"
        :rules="transferRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="转移标题" prop="transferTitle">
              <el-input
                v-model="transferForm.transferTitle"
                placeholder="请输入转移标题"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="转移编码" prop="transferCode">
              <el-input
                v-model="transferForm.transferCode"
                placeholder="请输入转移编码"
                :disabled="!!transferForm.transferId"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="转移类型" prop="transferType">
              <el-select
                v-model="transferForm.transferType"
                placeholder="请选择转移类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in transferTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="转移金额" prop="transferAmount">
              <el-input-number
                v-model="transferForm.transferAmount"
                :precision="2"
                :min="0"
                placeholder="请输入转移金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 转出信息 -->
        <el-divider content-position="left">转出信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="转出单元" prop="fromOrganizationId">
              <el-select
                v-model="transferForm.fromOrganizationId"
                placeholder="请选择转出单元"
                filterable
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
            <el-form-item label="转出科目" prop="fromBudgetAccountId">
              <el-select
                v-model="transferForm.fromBudgetAccountId"
                placeholder="请选择转出科目"
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
        
        <!-- 转入信息 -->
        <el-divider content-position="left">转入信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="转入单元" prop="toOrganizationId">
              <el-select
                v-model="transferForm.toOrganizationId"
                placeholder="请选择转入单元"
                filterable
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
            <el-form-item label="转入科目" prop="toBudgetAccountId">
              <el-select
                v-model="transferForm.toBudgetAccountId"
                placeholder="请选择转入科目"
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
                v-model="transferForm.applyDate"
                type="date"
                placeholder="请选择申请日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划转移日期" prop="plannedTransferDate">
              <el-date-picker
                v-model="transferForm.plannedTransferDate"
                type="date"
                placeholder="请选择计划转移日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="转移原因" prop="transferReason">
          <el-input
            v-model="transferForm.transferReason"
            type="textarea"
            :rows="3"
            placeholder="请输入转移原因"
          />
        </el-form-item>
        
        <el-form-item label="转移说明" prop="transferDescription">
          <el-input
            v-model="transferForm.transferDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入转移说明"
          />
        </el-form-item>
        
        <!-- 审批配置 -->
        <el-form-item label="审批配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="紧急程度" prop="urgencyLevel">
                <el-select
                  v-model="transferForm.urgencyLevel"
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
                  v-model="transferForm.approver"
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
                  v-model="transferForm.notificationMethod"
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
        
        <!-- 转移配置 -->
        <el-form-item label="转移配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="转移方式" prop="transferMethod">
                <el-select
                  v-model="transferForm.transferMethod"
                  placeholder="转移方式"
                  style="width: 100%"
                >
                  <el-option value="IMMEDIATE" label="立即转移" />
                  <el-option value="SCHEDULED" label="定时转移" />
                  <el-option value="CONDITIONAL" label="条件转移" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="转移比例" prop="transferRatio">
                <el-input-number
                  v-model="transferForm.transferRatio"
                  :precision="1"
                  :min="0"
                  :max="100"
                  placeholder="百分比"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="优先级" prop="priority">
                <el-select
                  v-model="transferForm.priority"
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
        
        <el-form-item label="转移选项">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="transferForm.autoApprove">自动审批</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="transferForm.sendNotification">发送通知</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="transferForm.trackProgress">跟踪进度</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleValidateTransfer">验证转移</el-button>
        <el-button type="primary" @click="handleSubmitForm">提交申请</el-button>
      </div>
    </el-dialog>

    <!-- 导入转移对话框 -->
    <el-dialog title="导入转移申请" :visible.sync="importDialogVisible" width="500px" :close-on-click-modal="false">
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

    <!-- 详情对话框 -->
    <el-dialog title="转移详情" :visible.sync="detailDialogVisible" width="800px">
      <el-descriptions :column="2" border size="small" v-if="detailData">
        <el-descriptions-item label="转移编码">{{ detailData.transferCode }}</el-descriptions-item>
        <el-descriptions-item label="转移标题">{{ detailData.transferTitle }}</el-descriptions-item>
        <el-descriptions-item label="转移类型">
          <el-tag size="mini" :type="getTransferTypeColor(detailData.transferType)">{{ getTransferTypeText(detailData.transferType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="转移金额">
          <span class="amount-text">{{ formatAmount(detailData.transferAmount) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="转出单元">{{ detailData.fromOrganizationName }}</el-descriptions-item>
        <el-descriptions-item label="转入单元">{{ detailData.toOrganizationName }}</el-descriptions-item>
        <el-descriptions-item label="转移状态">
          <el-tag :type="getTransferStatusType(detailData.transferStatus)" size="mini">{{ getTransferStatusText(detailData.transferStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审批状态">
          <el-tag :type="getApprovalStatusType(detailData.approvalStatus)" size="mini">{{ getApprovalStatusText(detailData.approvalStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="申请人">{{ detailData.applicant }}</el-descriptions-item>
        <el-descriptions-item label="审批人">{{ detailData.approver }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ detailData.applyDate }}</el-descriptions-item>
        <el-descriptions-item label="转移日期">{{ detailData.transferDate }}</el-descriptions-item>
        <el-descriptions-item label="计划转移日期">{{ detailData.plannedTransferDate }}</el-descriptions-item>
        <el-descriptions-item label="紧急程度">{{ detailData.urgencyLevel }}</el-descriptions-item>
        <el-descriptions-item label="转移原因" :span="2">{{ detailData.transferReason }}</el-descriptions-item>
        <el-descriptions-item label="转移说明" :span="2">{{ detailData.transferDescription }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog title="审批转移" :visible.sync="approveDialogVisible" width="600px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small" v-if="approveData" style="margin-bottom: 20px;">
        <el-descriptions-item label="转移编码">{{ approveData.transferCode }}</el-descriptions-item>
        <el-descriptions-item label="转移标题">{{ approveData.transferTitle }}</el-descriptions-item>
        <el-descriptions-item label="转移金额">
          <span class="amount-text">{{ formatAmount(approveData.transferAmount) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="申请人">{{ approveData.applicant }}</el-descriptions-item>
      </el-descriptions>
      <el-form :model="approveForm" label-width="80px" size="small">
        <el-form-item label="审批意见">
          <el-radio-group v-model="approveForm.action">
            <el-radio label="APPROVED">通过</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批备注">
          <el-input v-model="approveForm.remark" type="textarea" :rows="3" placeholder="请输入审批备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="approveDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="approveLoading" @click="handleSubmitApprove">确认</el-button>
      </div>
    </el-dialog>

    <!-- 执行对话框 -->
    <el-dialog title="执行转移" :visible.sync="executeDialogVisible" width="600px" :close-on-click-modal="false">
      <el-alert title="确认执行该预算转移？执行后资金将从转出单元转移到转入单元，此操作不可撤销。" type="warning" :closable="false" show-icon style="margin-bottom: 20px;" />
      <el-descriptions :column="2" border size="small" v-if="executeData">
        <el-descriptions-item label="转移编码">{{ executeData.transferCode }}</el-descriptions-item>
        <el-descriptions-item label="转移标题">{{ executeData.transferTitle }}</el-descriptions-item>
        <el-descriptions-item label="转出单元">{{ executeData.fromOrganizationName }}</el-descriptions-item>
        <el-descriptions-item label="转入单元">{{ executeData.toOrganizationName }}</el-descriptions-item>
        <el-descriptions-item label="转移金额">
          <span class="amount-text">{{ formatAmount(executeData.transferAmount) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="转移类型">{{ getTransferTypeText(executeData.transferType) }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="executeDialogVisible = false">取消</el-button>
        <el-button type="warning" :loading="executeLoading" @click="handleSubmitExecute">确认执行</el-button>
      </div>
    </el-dialog>

    <!-- 转移历史对话框 -->
    <el-dialog title="转移历史" :visible.sync="historyDialogVisible" width="700px">
      <el-timeline v-if="historyList.length > 0">
        <el-timeline-item
          v-for="(item, index) in historyList"
          :key="index"
          :timestamp="item.time"
          :type="item.type"
          placement="top"
        >
          <el-card shadow="never" class="timeline-card">
            <h4>{{ item.title }}</h4>
            <p>{{ item.content }}</p>
            <p v-if="item.operator" style="color: #909399; font-size: 12px;">操作人：{{ item.operator }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-else description="暂无历史记录" />
      <div slot="footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 进度跟踪对话框 -->
    <el-dialog title="进度跟踪" :visible.sync="trackDialogVisible" width="600px">
      <el-steps :active="trackActiveStep" finish-status="success" align-center style="margin-bottom: 30px;">
        <el-step title="提交申请" />
        <el-step title="审批中" />
        <el-step title="执行中" />
        <el-step title="已完成" />
      </el-steps>
      <el-descriptions :column="2" border size="small" v-if="trackData">
        <el-descriptions-item label="当前状态">
          <el-tag :type="getTransferStatusType(trackData.transferStatus)" size="mini">{{ getTransferStatusText(trackData.transferStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="转移编码">{{ trackData.transferCode }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ trackData.applyDate }}</el-descriptions-item>
        <el-descriptions-item label="转移日期">{{ trackData.transferDate || '待执行' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="trackDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetTransferApi } from '@/api/managementAccountant/ncv65/budgetControl'

export default {
  name: 'BudgetTransfer',
  data() {
    return {
      // 查询参数
      queryForm: {
        transferTitle: '',
        transferType: '',
        fromOrganization: '',
        toOrganization: '',
        transferStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      transferList: [],
      total: 0,
      selectedRows: [],
      
      // 实时监控
      realTimeMonitor: false,
      monitorTimer: null,
      
      // 统计数据
      transferStats: {
        totalTransfers: 0,
        approvedTransfers: 0,
        transferredAmount: 0,
        processingCount: 0,
        approvalRate: 0,
        transferRate: 0,
        processingRate: 0
      },
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      transferForm: {
        transferId: null,
        transferTitle: '',
        transferCode: '',
        transferType: '',
        transferAmount: 0,
        fromOrganizationId: '',
        fromBudgetAccountId: '',
        toOrganizationId: '',
        toBudgetAccountId: '',
        applyDate: '',
        plannedTransferDate: '',
        transferReason: '',
        transferDescription: '',
        urgencyLevel: 'MEDIUM',
        approver: '',
        notificationMethod: 'SYSTEM',
        transferMethod: 'IMMEDIATE',
        transferRatio: 100,
        priority: 'MEDIUM',
        autoApprove: false,
        sendNotification: true,
        trackProgress: true
      },
      transferRules: {
        transferTitle: [
          { required: true, message: '请输入转移标题', trigger: 'blur' }
        ],
        transferCode: [
          { required: true, message: '请输入转移编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '转移编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        transferType: [
          { required: true, message: '请选择转移类型', trigger: 'change' }
        ],
        transferAmount: [
          { required: true, message: '请输入转移金额', trigger: 'blur' }
        ]
      },
      
      // 选项数据
      transferTypeOptions: [
        { value: 'DEPARTMENT', label: '部门间转移' },
        { value: 'PROJECT', label: '项目间转移' },
        { value: 'ACCOUNT', label: '科目间转移' },
        { value: 'PERIOD', label: '期间间转移' },
        { value: 'EMERGENCY', label: '紧急转移' },
        { value: 'CUSTOM', label: '自定义转移' }
      ],
      transferStatusOptions: [
        { value: 'PENDING', label: '待审批' },
        { value: 'APPROVED', label: '已审批' },
        { value: 'EXECUTING', label: '执行中' },
        { value: 'COMPLETED', label: '已完成' },
        { value: 'REJECTED', label: '已拒绝' }
      ],
      organizationOptions: [],
      budgetAccountOptions: [],
      userOptions: [],

      // 导入
      importDialogVisible: false,
      importFileList: [],
      importLoading: false,
      importFile: null,

      // 列设置
      columnSettingVisible: false,
      columnOptions: [
        { key: 'transferCode', label: '转移编码' },
        { key: 'transferTitle', label: '转移标题' },
        { key: 'transferType', label: '转移类型' },
        { key: 'fromOrganizationName', label: '转出单元' },
        { key: 'toOrganizationName', label: '转入单元' },
        { key: 'transferAmount', label: '转移金额' },
        { key: 'transferReason', label: '转移原因' },
        { key: 'transferStatus', label: '转移状态' },
        { key: 'approvalStatus', label: '审批状态' },
        { key: 'applicant', label: '申请人' },
        { key: 'applyDate', label: '申请日期' },
        { key: 'transferDate', label: '转移日期' }
      ],
      columnChecked: ['transferCode', 'transferTitle', 'transferType', 'fromOrganizationName', 'toOrganizationName', 'transferAmount', 'transferReason', 'transferStatus', 'approvalStatus', 'applicant', 'applyDate', 'transferDate'],
      columnVisible: {
        transferCode: true, transferTitle: true, transferType: true,
        fromOrganizationName: true, toOrganizationName: true, transferAmount: true,
        transferReason: true, transferStatus: true, approvalStatus: true,
        applicant: true, applyDate: true, transferDate: true
      },

      // 详情弹窗
      detailDialogVisible: false,
      detailData: null,

      // 审批弹窗
      approveDialogVisible: false,
      approveData: null,
      approveLoading: false,
      approveForm: { action: 'APPROVED', remark: '' },

      // 执行弹窗
      executeDialogVisible: false,
      executeData: null,
      executeLoading: false,

      // 历史弹窗
      historyDialogVisible: false,
      historyList: [],

      // 进度跟踪弹窗
      trackDialogVisible: false,
      trackData: null,
      trackActiveStep: 0
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
        const response = await budgetTransferApi.getStatistics()
        if (response.code === 1 && response.data) {
          const d = response.data
          const total = d.totalCount || 0
          const approved = d.approvedCount || 0
          this.transferStats = {
            totalTransfers: total,
            approvedTransfers: approved,
            transferredAmount: d.transferredAmount || 0,
            processingCount: d.processingCount || 0,
            approvalRate: total > 0 ? parseFloat(((approved / total) * 100).toFixed(1)) : 0,
            transferRate: d.transferRate || 0,
            processingRate: total > 0 ? parseFloat((((d.processingCount || 0) / total) * 100).toFixed(1)) : 0
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
        const response = await budgetTransferApi.getPage(params)
        this.transferList = response.data.tlist || response.data.list || []
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
        const response = await budgetTransferApi.getOrganizations()
        if (response.code === 1 && response.data) {
          this.organizationOptions = response.data.map(item => ({
            value: item.id,
            label: item.name
          }))
        }
      } catch (error) {
        console.error('加载组织选项失败：', error)
      }
    },

    // 加载预算科目选项
    async loadBudgetAccountOptions() {
      try {
        const response = await budgetTransferApi.getBudgetAccounts()
        if (response.code === 1 && response.data) {
          this.budgetAccountOptions = response.data.map(item => ({
            value: item.id,
            label: item.name
          }))
        }
      } catch (error) {
        console.error('加载预算科目选项失败：', error)
      }
    },

    // 加载用户选项
    async loadUserOptions() {
      try {
        const response = await budgetTransferApi.getUsers()
        if (response.code === 1 && response.data) {
          this.userOptions = response.data
        }
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
        transferTitle: '',
        transferType: '',
        fromOrganization: '',
        toOrganization: '',
        transferStatus: ''
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
    
    // 创建转移
    handleCreateTransfer() {
      this.dialogTitle = '申请预算转移'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 查看转移（弹窗）
    handleView(row) {
      this.detailData = { ...row }
      this.detailDialogVisible = true
    },

    // 编辑转移
    handleEdit(row) {
      this.dialogTitle = '编辑预算转移'
      this.transferForm = { ...row }
      this.dialogVisible = true
    },

    // 审批转移（弹窗）
    handleApprove(row) {
      this.approveData = { ...row }
      this.approveForm = { action: 'APPROVED', remark: '' }
      this.approveDialogVisible = true
    },

    // 提交审批
    async handleSubmitApprove() {
      if (!this.approveData) return
      this.approveLoading = true
      try {
        const apiMethod = this.approveForm.action === 'APPROVED'
          ? budgetTransferApi.approve
          : budgetTransferApi.reject
        await apiMethod(this.approveData.transferId, { remark: this.approveForm.remark })
        this.$message.success(this.approveForm.action === 'APPROVED' ? '审批通过' : '已拒绝')
        this.approveDialogVisible = false
        this.getList()
        this.loadStats()
      } catch (error) {
        this.$message.error('审批失败：' + error.message)
      } finally {
        this.approveLoading = false
      }
    },

    // 执行转移（弹窗）
    handleExecute(row) {
      this.executeData = { ...row }
      this.executeDialogVisible = true
    },

    // 提交执行
    async handleSubmitExecute() {
      if (!this.executeData) return
      this.executeLoading = true
      try {
        await budgetTransferApi.execute(this.executeData.transferId)
        this.$message.success('执行成功')
        this.executeDialogVisible = false
        this.getList()
        this.loadStats()
      } catch (error) {
        this.$message.error('执行失败：' + error.message)
      } finally {
        this.executeLoading = false
      }
    },
    
    // 验证转移
    async handleValidateTransfer() {
      try {
        const params = {
          transferAmount: this.transferForm.transferAmount,
          fromOrganizationId: this.transferForm.fromOrganizationId,
          fromBudgetAccountId: this.transferForm.fromBudgetAccountId,
          toOrganizationId: this.transferForm.toOrganizationId,
          toBudgetAccountId: this.transferForm.toBudgetAccountId
        }
        const response = await budgetTransferApi.validateTransfer(params)
        const validation = response.data
        
        this.$alert(`
          <p>转出可用金额：${this.formatAmount(validation.fromAvailableAmount)}</p>
          <p>转入可接收金额：${this.formatAmount(validation.toAcceptableAmount)}</p>
          <p>申请转移金额：${this.formatAmount(validation.requestAmount)}</p>
          <p>验证结果：${validation.valid ? '通过' : '不通过'}</p>
          <p>验证说明：${validation.message}</p>
        `, '转移验证结果', {
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
        await this.$refs.transferForm.validate()

        const params = { ...this.transferForm }

        if (this.transferForm.transferId) {
          await budgetTransferApi.update(this.transferForm.transferId, params)
          this.$message.success('更新成功')
        } else {
          await budgetTransferApi.create(params)
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
      this.transferForm = {
        transferId: null,
        transferTitle: '',
        transferCode: '',
        transferType: '',
        transferAmount: 0,
        fromOrganizationId: '',
        fromBudgetAccountId: '',
        toOrganizationId: '',
        toBudgetAccountId: '',
        applyDate: '',
        plannedTransferDate: '',
        transferReason: '',
        transferDescription: '',
        urgencyLevel: 'MEDIUM',
        approver: '',
        notificationMethod: 'SYSTEM',
        transferMethod: 'IMMEDIATE',
        transferRatio: 100,
        priority: 'MEDIUM',
        autoApprove: false,
        sendNotification: true,
        trackProgress: true
      }
      this.$nextTick(() => {
        this.$refs.transferForm && this.$refs.transferForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入转移
    handleImportTransfers() {
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
        const response = await budgetTransferApi.import(formData)
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
    
    // 批量审批
    async handleBatchApprove() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要审批的项目')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.transferId)
        await budgetTransferApi.batchApprove(ids)
        this.$message.success('批量审批成功')
        this.getList()
        this.loadStats()
      } catch (error) {
        this.$message.error('批量审批失败：' + error.message)
      }
    },

    // 导出转移
    async handleExportTransfers() {
      try {
        let params
        if (this.selectedRows.length > 0) {
          // 有选中数据，导出选中数据
          params = {
            ...this.queryForm,
            ids: this.selectedRows.map(row => row.transferId).join(',')
          }
        } else {
          // 没有选中数据，导出当前页面大小的数据
          params = {
            ...this.queryForm,
            ...this.queryParams
          }
        }
        const response = await budgetTransferApi.export(params)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `预算转移_${new Date().toLocaleDateString()}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      }
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
    },
    
    // 设置
    handleSettings() {
      this.$router.push('/managementAccountant/ncv65/budgetControl/transferSettings')
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'history':
          this.handleHistory(row)
          break
        case 'track':
          this.handleTrack(row)
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
    // 转移历史（弹窗）
    handleHistory(row) {
      const statusMap = {
        PENDING: { title: '提交申请', type: 'primary' },
        APPROVED: { title: '审批通过', type: 'success' },
        REJECTED: { title: '审批拒绝', type: 'danger' },
        EXECUTING: { title: '开始执行', type: 'warning' },
        COMPLETED: { title: '执行完成', type: 'success' }
      }
      const list = []
      if (row.applyDate) {
        list.push({ time: row.applyDate, title: '提交申请', content: `申请人 ${row.applicant || '-'} 提交了预算转移申请`, operator: row.applicant, type: 'primary' })
      }
      if (row.transferStatus === 'APPROVED' || row.transferStatus === 'EXECUTING' || row.transferStatus === 'COMPLETED') {
        list.push({ time: row.approvalDate || row.applyDate, title: '审批通过', content: `审批人 ${row.approver || '-'} 审批通过`, operator: row.approver, type: 'success' })
      }
      if (row.transferStatus === 'REJECTED') {
        list.push({ time: row.approvalDate || row.applyDate, title: '审批拒绝', content: `审批人 ${row.approver || '-'} 拒绝了申请`, operator: row.approver, type: 'danger' })
      }
      if (row.transferStatus === 'EXECUTING' || row.transferStatus === 'COMPLETED') {
        list.push({ time: row.transferDate || row.applyDate, title: '开始执行', content: '预算转移开始执行', type: 'warning' })
      }
      if (row.transferStatus === 'COMPLETED') {
        list.push({ time: row.completionDate || row.transferDate || row.applyDate, title: '执行完成', content: '预算转移已完成', type: 'success' })
      }
      this.historyList = list
      this.historyDialogVisible = true
    },

    // 跟踪进度（弹窗）
    handleTrack(row) {
      const stepMap = { PENDING: 0, APPROVED: 1, EXECUTING: 2, COMPLETED: 3, REJECTED: 1 }
      this.trackActiveStep = stepMap[row.transferStatus] || 0
      this.trackData = { ...row }
      this.trackDialogVisible = true
    },

    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算转移'
      this.dialogVisible = true
      this.transferForm = { ...row, transferId: null, transferCode: '' }
    },

    // 导出单个
    async handleExportSingle(row) {
      try {
        const response = await budgetTransferApi.exportSingle(row.transferId)
        const blob = new Blob([response], { type: 'text/csv;charset=utf-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `预算转移_${row.transferCode || row.transferId}.csv`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该转移记录？删除后不可恢复', '提示', {
          type: 'warning'
        })
        await budgetTransferApi.delete(row.transferId)
        this.$message.success('删除成功')
        this.getList()
        this.loadStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 行点击（排除操作列按钮点击）
    handleRowClick(row, column, event) {
      // 如果点击的是操作列或按钮/下拉菜单，则不弹详情
      if (column && column.label === '操作') return
      const target = event && event.target
      if (target && (target.closest('.el-button') || target.closest('.el-dropdown') || target.closest('.el-checkbox'))) return
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
    
    // 判断是否可以审批
    canApprove(row) {
      return row.approvalStatus === 'PENDING' && row.approver === this.$store.getters.userId
    },
    
    // 判断是否可以执行
    canExecute(row) {
      return row.transferStatus === 'APPROVED'
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取转移类型颜色
    getTransferTypeColor(type) {
      const colorMap = {
        'DEPARTMENT': 'primary',
        'PROJECT': 'success',
        'ACCOUNT': 'warning',
        'PERIOD': 'info',
        'EMERGENCY': 'danger',
        'CUSTOM': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取转移类型文本
    getTransferTypeText(type) {
      const item = this.transferTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取转移状态类型
    getTransferStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'primary',
        'EXECUTING': 'info',
        'COMPLETED': 'success',
        'REJECTED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取转移状态文本
    getTransferStatusText(status) {
      const item = this.transferStatusOptions.find(opt => opt.value === status)
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
.budget-transfer {
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
      
      &.transferred-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.processing-card {
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
