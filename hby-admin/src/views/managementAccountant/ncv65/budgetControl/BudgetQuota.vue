<template>
  <div class="budget-quota">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算配额管理</h2>
      <p>管理预算配额分配和使用，监控配额执行情况，提供配额调整和报告</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateQuota">分配配额</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportQuotas">导入配额</el-button>
            <el-button type="warning" icon="el-icon-refresh" @click="handleBatchAdjust">批量调整</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportQuotas">导出配额</el-button>
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

    <!-- 配额统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ quotaStats.totalQuotas }}</div>
            <div class="stat-label">配额总数</div>
            <div class="stat-description">所有预算配额数量</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-pie-chart"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card allocated-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ quotaStats.allocatedAmount }}</div>
            <div class="stat-label">已分配金额</div>
            <div class="stat-description">已分配的配额金额</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="quotaStats.allocationRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#67C23A"
              />
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
            <div class="stat-number">{{ quotaStats.usedAmount }}</div>
            <div class="stat-label">已用金额</div>
            <div class="stat-description">已使用的配额金额</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="quotaStats.usageRate" 
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
        <el-card class="stat-card remaining-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ quotaStats.remainingAmount }}</div>
            <div class="stat-label">剩余金额</div>
            <div class="stat-description">可用的配额余额</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="quotaStats.remainingRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#409EFF"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-wallet"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="配额名称">
          <el-input
            v-model="queryForm.quotaName"
            placeholder="请输入配额名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="配额类型">
          <el-select
            v-model="queryForm.quotaType"
            placeholder="请选择配额类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in quotaTypeOptions"
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
        <el-form-item label="配额状态">
          <el-select
            v-model="queryForm.quotaStatus"
            placeholder="请选择配额状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in quotaStatusOptions"
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

    <!-- 配额列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算配额列表</span>
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
        :data="quotaList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column v-if="columnVisible.quotaCode" prop="quotaCode" label="配额编码" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.quotaName" prop="quotaName" label="配额名称" min-width="200" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.quotaType" prop="quotaType" label="配额类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getQuotaTypeColor(scope.row.quotaType)">
              {{ getQuotaTypeText(scope.row.quotaType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.organizationName" prop="organizationName" label="组织单元" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.budgetAccountName" prop="budgetAccountName" label="预算科目" width="150" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.quotaAmount" prop="quotaAmount" label="配额金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.quotaAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.usedAmount" prop="usedAmount" label="已用金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.usedAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.remainingAmount" prop="remainingAmount" label="剩余金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span :class="getRemainingAmountClass(scope.row.remainingAmount)">
              {{ formatAmount(scope.row.remainingAmount) }}
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

        <el-table-column v-if="columnVisible.quotaStatus" prop="quotaStatus" label="配额状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getQuotaStatusType(scope.row.quotaStatus)" size="mini">
              {{ getQuotaStatusText(scope.row.quotaStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.effectiveDate" prop="effectiveDate" label="生效日期" width="120" align="center" />
        <el-table-column v-if="columnVisible.expiryDate" prop="expiryDate" label="失效日期" width="120" align="center" />
        
        <el-table-column label="操作" width="220" align="center" fixed="right" class-name="operation-column">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click.stop="handleView(scope.row)"
            >详情</el-button>
            <el-button
              v-if="canAdjust(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              class="warning-text"
              @click.stop="handleAdjust(scope.row)"
            >调整</el-button>
            <el-button
              v-if="canEdit(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
              @click.native.stop
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="history" icon="el-icon-time">使用历史</el-dropdown-item>
                <el-dropdown-item command="transfer" icon="el-icon-sort">转移配额</el-dropdown-item>
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

    <!-- 新增/编辑配额对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="quotaForm"
        :model="quotaForm"
        :rules="quotaRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="配额名称" prop="quotaName">
              <el-input
                v-model="quotaForm.quotaName"
                placeholder="请输入配额名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="配额编码" prop="quotaCode">
              <el-input
                v-model="quotaForm.quotaCode"
                placeholder="请输入配额编码"
                :disabled="!!quotaForm.quotaId"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="配额类型" prop="quotaType">
              <el-select
                v-model="quotaForm.quotaType"
                placeholder="请选择配额类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in quotaTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="配额金额" prop="quotaAmount">
              <el-input-number
                v-model="quotaForm.quotaAmount"
                :precision="2"
                :min="0"
                placeholder="请输入配额金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组织单元" prop="organizationId">
              <el-cascader
                v-model="quotaForm.organizationId"
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
                v-model="quotaForm.budgetAccountId"
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
                v-model="quotaForm.effectiveDate"
                type="date"
                placeholder="请选择生效日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expiryDate">
              <el-date-picker
                v-model="quotaForm.expiryDate"
                type="date"
                placeholder="请选择失效日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="配额描述" prop="quotaDescription">
          <el-input
            v-model="quotaForm.quotaDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入配额描述"
          />
        </el-form-item>
        
        <!-- 分配策略 -->
        <el-form-item label="分配策略">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="分配方式" prop="allocationMethod">
                <el-select
                  v-model="quotaForm.allocationMethod"
                  placeholder="分配方式"
                  style="width: 100%"
                >
                  <el-option value="EQUAL" label="平均分配" />
                  <el-option value="RATIO" label="比例分配" />
                  <el-option value="MANUAL" label="手动分配" />
                  <el-option value="FORMULA" label="公式分配" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="预警阈值" prop="warningThreshold">
                <el-input-number
                  v-model="quotaForm.warningThreshold"
                  :precision="1"
                  :min="0"
                  :max="100"
                  placeholder="百分比"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="控制级别" prop="controlLevel">
                <el-select
                  v-model="quotaForm.controlLevel"
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
        
        <el-form-item label="配额配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="quotaForm.isActive">启用配额</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="quotaForm.allowTransfer">允许转移</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="quotaForm.autoAdjust">自动调整</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleCalculateAllocation">计算分配</el-button>
        <el-button type="primary" @click="handleSubmitForm">保存配额</el-button>
      </div>
    </el-dialog>

    <!-- 导入配额对话框 -->
    <el-dialog title="导入配额" :visible.sync="importDialogVisible" width="500px" :close-on-click-modal="false">
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

    <!-- 转移配额对话框 -->
    <el-dialog title="转移配额" :visible.sync="transferDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form v-if="currentQuota" :model="transferForm" label-width="120px" size="small">
        <el-form-item label="配额名称">
          <span>{{ currentQuota.quotaName }}</span>
        </el-form-item>
        <el-form-item label="剩余金额">
          <span class="amount-text">{{ formatAmount(currentQuota.remainingAmount) }}</span>
        </el-form-item>
        <el-form-item label="转移金额" prop="transferAmount">
          <el-input-number
            v-model="transferForm.transferAmount"
            :precision="2"
            :min="0.01"
            :max="currentQuota.remainingAmount"
            placeholder="请输入转移金额"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="目标组织" prop="targetOrganizationId">
          <el-cascader
            v-model="transferForm.targetOrganizationId"
            :options="organizationOptions"
            :props="{ checkStrictly: true, emitPath: false }"
            placeholder="请选择目标组织"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="目标科目" prop="targetAccountId">
          <el-select v-model="transferForm.targetAccountId" placeholder="请选择目标预算科目" style="width: 100%">
            <el-option v-for="item in budgetAccountOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="转移说明" prop="transferRemark">
          <el-input v-model="transferForm.transferRemark" type="textarea" :rows="3" placeholder="请输入转移说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="transferDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="transferLoading" @click="handleSubmitTransfer">确认转移</el-button>
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
    <el-dialog title="配额详情" :visible.sync="detailDialogVisible" width="800px">
      <el-descriptions :column="2" border size="small" v-if="detailData">
        <el-descriptions-item label="配额编码">{{ detailData.quotaCode }}</el-descriptions-item>
        <el-descriptions-item label="配额名称">{{ detailData.quotaName }}</el-descriptions-item>
        <el-descriptions-item label="配额类型">
          <el-tag :type="getQuotaTypeColor(detailData.quotaType)" size="mini">{{ getQuotaTypeText(detailData.quotaType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="配额状态">
          <el-tag :type="getQuotaStatusType(detailData.quotaStatus)" size="mini">{{ getQuotaStatusText(detailData.quotaStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="配额金额">{{ formatAmount(detailData.quotaAmount) }}</el-descriptions-item>
        <el-descriptions-item label="已用金额">{{ formatAmount(detailData.usedAmount) }}</el-descriptions-item>
        <el-descriptions-item label="可用金额">{{ formatAmount(detailData.availableAmount) }}</el-descriptions-item>
        <el-descriptions-item label="已分配金额">{{ formatAmount(detailData.allocatedAmount) }}</el-descriptions-item>
        <el-descriptions-item label="生效日期">{{ detailData.effectiveDate }}</el-descriptions-item>
        <el-descriptions-item label="失效日期">{{ detailData.expiryDate }}</el-descriptions-item>
        <el-descriptions-item label="是否启用">
          <el-tag :type="detailData.isEnabled ? 'success' : 'info'" size="mini">{{ detailData.isEnabled ? '是' : '否' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="配额周期">{{ detailData.quotaPeriod }}</el-descriptions-item>
        <el-descriptions-item label="配额描述" :span="2">{{ detailData.quotaDescription || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新人">{{ detailData.updateBy }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 使用历史对话框 -->
    <el-dialog title="配额使用历史" :visible.sync="historyDialogVisible" width="700px">
      <div v-if="historyQuota" style="margin-bottom: 16px;">
        <el-tag>{{ historyQuota.quotaCode }}</el-tag>
        <span style="margin-left: 8px;">{{ historyQuota.quotaName }}</span>
      </div>
      <el-table :data="historyList" border size="small" empty-text="暂无操作历史" v-loading="historyLoading">
        <el-table-column prop="operateTime" label="操作时间" width="180" />
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.operationType === 'CREATE' ? 'success' : scope.row.operationType === 'DELETE' ? 'danger' : scope.row.operationType === 'ADJUST' ? 'warning' : 'info'" size="small">
              {{ { CREATE: '创建', UPDATE: '更新', DELETE: '删除', ADJUST: '调整', TRANSFER: '转移', ENABLE: '启用', DISABLE: '停用' }[scope.row.operationType] || scope.row.operationType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationDesc" label="操作描述" show-overflow-tooltip />
        <el-table-column prop="amount" label="金额" width="120">
          <template slot-scope="scope">{{ formatAmount(scope.row.amount) }}</template>
        </el-table-column>
        <el-table-column prop="operator" label="操作人" width="100" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { budgetQuotaApi } from '@/api/managementAccountant/ncv65/budgetControl'

export default {
  name: 'BudgetQuota',
  data() {
    return {
      // 查询参数
      queryForm: {
        quotaName: '',
        quotaType: '',
        organizationPath: '',
        budgetAccount: '',
        quotaStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      quotaList: [],
      total: 0,
      selectedRows: [],
      
      // 实时监控
      realTimeMonitor: false,
      monitorTimer: null,
      
      // 统计数据
      quotaStats: {
        totalQuotas: 0,
        allocatedAmount: 0,
        usedAmount: 0,
        remainingAmount: 0,
        allocationRate: 0,
        usageRate: 0,
        remainingRate: 0
      },
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      quotaForm: {
        quotaId: null,
        quotaName: '',
        quotaCode: '',
        quotaType: '',
        quotaAmount: 0,
        organizationId: '',
        budgetAccountId: '',
        effectiveDate: '',
        expiryDate: '',
        quotaDescription: '',
        allocationMethod: 'EQUAL',
        warningThreshold: 80,
        controlLevel: 'SOFT',
        isActive: true,
        allowTransfer: true,
        autoAdjust: false
      },
      quotaRules: {
        quotaName: [
          { required: true, message: '请输入配额名称', trigger: 'blur' }
        ],
        quotaCode: [
          { required: true, message: '请输入配额编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '配额编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        quotaType: [
          { required: true, message: '请选择配额类型', trigger: 'change' }
        ],
        quotaAmount: [
          { required: true, message: '请输入配额金额', trigger: 'blur' }
        ]
      },
      
      // 选项数据
      quotaTypeOptions: [
        { value: 'ANNUAL', label: '年度配额' },
        { value: 'QUARTERLY', label: '季度配额' },
        { value: 'MONTHLY', label: '月度配额' },
        { value: 'PROJECT', label: '项目配额' },
        { value: 'DEPARTMENT', label: '部门配额' },
        { value: 'CUSTOM', label: '自定义配额' }
      ],
      quotaStatusOptions: [
        { value: 'ACTIVE', label: '生效' },
        { value: 'INACTIVE', label: '停用' },
        { value: 'EXPIRED', label: '过期' }
      ],
      organizationOptions: [],
      budgetAccountOptions: [],

      // 导入
      importDialogVisible: false,
      importFileList: [],
      importLoading: false,
      importFile: null,

      // 详情对话框
      detailDialogVisible: false,
      detailData: {},

      // 使用历史对话框
      historyDialogVisible: false,
      historyQuota: null,
      historyList: [],
      historyLoading: false,

      // 转移
      transferDialogVisible: false,
      transferLoading: false,
      currentQuota: null,
      transferForm: {
        transferAmount: 0,
        targetOrganizationId: '',
        targetAccountId: '',
        transferRemark: ''
      },

      // 列设置
      columnSettingVisible: false,
      columnOptions: [
        { key: 'quotaCode', label: '配额编码' },
        { key: 'quotaName', label: '配额名称' },
        { key: 'quotaType', label: '配额类型' },
        { key: 'organizationName', label: '组织单元' },
        { key: 'budgetAccountName', label: '预算科目' },
        { key: 'quotaAmount', label: '配额金额' },
        { key: 'usedAmount', label: '已用金额' },
        { key: 'remainingAmount', label: '剩余金额' },
        { key: 'usageRate', label: '使用率' },
        { key: 'quotaStatus', label: '配额状态' },
        { key: 'effectiveDate', label: '生效日期' },
        { key: 'expiryDate', label: '失效日期' }
      ],
      columnChecked: ['quotaCode', 'quotaName', 'quotaType', 'organizationName', 'budgetAccountName', 'quotaAmount', 'usedAmount', 'remainingAmount', 'usageRate', 'quotaStatus', 'effectiveDate', 'expiryDate'],
      columnVisible: {
        quotaCode: true, quotaName: true, quotaType: true,
        organizationName: true, budgetAccountName: true, quotaAmount: true,
        usedAmount: true, remainingAmount: true, usageRate: true,
        quotaStatus: true, effectiveDate: true, expiryDate: true
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
        const response = await budgetQuotaApi.getStatistics()
        if (response.code === 1 && response.data) {
          const d = response.data
          this.quotaStats = {
            totalQuotas: d.totalCount || 0,
            allocatedAmount: d.allocatedAmount || 0,
            usedAmount: d.usedAmount || 0,
            remainingAmount: d.remainingAmount || 0,
            allocationRate: d.allocationRate || 0,
            usageRate: d.usageRate || 0,
            remainingRate: d.remainingRate || 0
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
        const response = await budgetQuotaApi.getPage(params)
        this.quotaList = response.data.tlist || response.data.list || []
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
        const response = await budgetQuotaApi.getOrganizations()
        const data = response.data || []
        // 后端返回 {id, name, code}，转换为 cascader 需要的 {value, label}
        this.organizationOptions = data.map(item => ({
          value: item.id || item.value,
          label: item.name || item.label,
          children: item.children ? item.children.map(c => ({ value: c.id || c.value, label: c.name || c.label })) : undefined
        }))
      } catch (error) {
        console.error('加载组织选项失败：', error)
      }
    },

    // 加载预算科目选项
    async loadBudgetAccountOptions() {
      try {
        const response = await budgetQuotaApi.getBudgetAccounts()
        const data = response.data || []
        // 后端返回 {id, name, code}，转换为 select 需要的 {value, label}
        this.budgetAccountOptions = data.map(item => ({
          value: item.id || item.value,
          label: item.name || item.label
        }))
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
        quotaName: '',
        quotaType: '',
        organizationPath: '',
        budgetAccount: '',
        quotaStatus: ''
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
    
    // 创建配额
    handleCreateQuota() {
      this.dialogTitle = '分配预算配额'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑配额
    handleEdit(row) {
      this.dialogTitle = '编辑预算配额'
      this.dialogVisible = true
      this.quotaForm = {
        ...row,
        // 后端字段 budgetId → 表单字段 budgetAccountId
        budgetAccountId: row.budgetAccountId || row.budgetId,
        // 分配策略字段回填（后端有值则用，否则给默认值）
        allocationMethod: row.allocationMethod || 'EQUAL',
        warningThreshold: row.warningThreshold != null ? row.warningThreshold : 80,
        controlLevel: row.controlLevel || 'SOFT'
      }
    },

    // 查看配额详情（弹出对话框）
    handleView(row) {
      this.detailData = { ...row }
      this.detailDialogVisible = true
    },

    // 调整配额
    handleAdjust(row) {
      this.dialogTitle = '调整预算配额'
      this.dialogVisible = true
      this.quotaForm = {
        ...row,
        budgetAccountId: row.budgetAccountId || row.budgetId,
        allocationMethod: row.allocationMethod || 'EQUAL',
        warningThreshold: row.warningThreshold != null ? row.warningThreshold : 80,
        controlLevel: row.controlLevel || 'SOFT'
      }
    },
    
    // 计算分配
    async handleCalculateAllocation() {
      try {
        const params = {
          quotaAmount: this.quotaForm.quotaAmount,
          allocationMethod: this.quotaForm.allocationMethod,
          organizationId: this.quotaForm.organizationId
        }
        const response = await budgetQuotaApi.calculateAllocation(params)
        const allocation = response.data

        this.$alert(`
          <p>配额总额：${this.formatAmount(allocation.totalQuota)}</p>
          <p>已分配金额：${this.formatAmount(allocation.allocatedAmount)}</p>
          <p>剩余可分配：${this.formatAmount(allocation.remainingAmount)}</p>
          <p>分配率：${allocation.allocationRate || 0}%</p>
        `, '分配计算结果', {
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
        await this.$refs.quotaForm.validate()

        // 只提取实体类有的字段，避免后端反序列化报错
        // 前端表单用 budgetAccountId，实体类字段是 budgetId，需要映射
        const { quotaId, quotaName, quotaCode, quotaType, quotaAmount,
                effectiveDate, expiryDate, quotaDescription, quotaStatus,
                isEnabled, remark, organizationId,
                allocationMethod, warningThreshold, controlLevel } = this.quotaForm
        // 优先用表单字段 budgetAccountId（用户实际选择的值），回退才用 budgetId
        const budgetId = this.quotaForm.budgetAccountId || this.quotaForm.budgetId
        const params = { quotaId, quotaName, quotaCode, quotaType, quotaAmount,
                         effectiveDate, expiryDate, quotaDescription, quotaStatus,
                         isEnabled, remark, budgetId, organizationId,
                         allocationMethod, warningThreshold, controlLevel }

        if (this.quotaForm.quotaId) {
          await budgetQuotaApi.update(this.quotaForm.quotaId, params)
          this.$message.success('更新成功')
        } else {
          await budgetQuotaApi.create(params)
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
      this.quotaForm = {
        quotaId: null,
        quotaName: '',
        quotaCode: '',
        quotaType: '',
        quotaAmount: 0,
        organizationId: '',
        budgetAccountId: '',
        effectiveDate: '',
        expiryDate: '',
        quotaDescription: '',
        allocationMethod: 'EQUAL',
        warningThreshold: 80,
        controlLevel: 'SOFT',
        isActive: true,
        allowTransfer: true,
        autoAdjust: false
      }
      this.$nextTick(() => {
        this.$refs.quotaForm && this.$refs.quotaForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入配额
    handleImportQuotas() {
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
        const response = await budgetQuotaApi.import(formData)
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
    
    // 批量调整
    async handleBatchAdjust() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要调整的配额')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.quotaId)
        await budgetQuotaApi.batchAdjust({ quotaIds: ids })
        this.$message.success('批量调整成功')
        this.getList()
      } catch (error) {
        this.$message.error('批量调整失败：' + error.message)
      }
    },
    
    // 导出配额
    async handleExportQuotas() {
      try {
        const params = { ...this.queryForm }
        const response = await budgetQuotaApi.export(params)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `预算配额_${new Date().getTime()}.xlsx`
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
      this.$router.push('/managementAccountant/ncv65/budgetControl/quotaSettings')
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'history':
          this.handleHistory(row)
          break
        case 'transfer':
          this.handleTransfer(row)
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
      this.historyQuota = row
      this.historyList = []
      this.historyDialogVisible = true
      this.historyLoading = true
      try {
        const response = await budgetQuotaApi.getHistory(row.quotaId)
        this.historyList = response.data || []
      } catch (error) {
        console.error('加载操作历史失败：', error)
        this.$message.error('加载操作历史失败')
      } finally {
        this.historyLoading = false
      }
    },
    
    // 转移配额
    handleTransfer(row) {
      this.currentQuota = row
      this.transferForm = {
        transferAmount: 0,
        targetOrganizationId: '',
        targetAccountId: '',
        transferRemark: ''
      }
      this.transferDialogVisible = true
    },

    // 提交转移
    async handleSubmitTransfer() {
      if (!this.transferForm.transferAmount || this.transferForm.transferAmount <= 0) {
        this.$message.warning('请输入有效的转移金额')
        return
      }
      if (!this.transferForm.targetOrganizationId) {
        this.$message.warning('请选择目标组织')
        return
      }
      this.transferLoading = true
      try {
        const params = {
          quotaId: this.currentQuota.quotaId,
          ...this.transferForm
        }
        const response = await budgetQuotaApi.transfer(params)
        if (response.code === 1) {
          this.$message.success('转移成功')
          this.transferDialogVisible = false
          this.getList()
        } else {
          this.$message.error(response.msg || '转移失败')
        }
      } catch (error) {
        this.$message.error('转移失败：' + error.message)
      } finally {
        this.transferLoading = false
      }
    },
    
    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算配额'
      this.dialogVisible = true
      this.quotaForm = { ...row, quotaId: null, quotaCode: null }
    },

    // 导出单个
    async handleExportSingle(row) {
      try {
        const response = await budgetQuotaApi.exportSingle(row.quotaId)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `配额_${row.quotaCode || row.quotaId}.xlsx`
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
        await this.$confirm('确认删除该配额记录？删除后不可恢复', '提示', {
          type: 'warning'
        })
        await budgetQuotaApi.delete(row.quotaId)
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
    
    // 判断是否可以调整
    canAdjust(row) {
      return row.quotaStatus === 'ACTIVE'
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
    
    // 获取配额类型颜色
    getQuotaTypeColor(type) {
      const colorMap = {
        'ANNUAL': 'primary',
        'QUARTERLY': 'success',
        'MONTHLY': 'warning',
        'PROJECT': 'info',
        'DEPARTMENT': 'danger',
        'CUSTOM': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取配额类型文本
    getQuotaTypeText(type) {
      const item = this.quotaTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取配额状态类型
    getQuotaStatusType(status) {
      const statusMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'EXPIRED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取配额状态文本
    getQuotaStatusText(status) {
      const item = this.quotaStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-quota {
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
      
      &.allocated-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.used-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.remaining-card {
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
