<template>
  <div class="budget-period">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算期间管理</h2>
      <p>管理预算期间设置，支持会计年度、期间开启关闭、期间状态管理和期间锁定</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreatePeriod">创建期间</el-button>
            <el-button type="success" icon="el-icon-unlock" @click="handleBatchOpen">批量开启</el-button>
            <el-button type="warning" icon="el-icon-lock" @click="handleBatchClose">批量关闭</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportPeriod">导出期间</el-button>
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

    <!-- 期间统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ periodStats.totalPeriods }}</div>
            <div class="stat-label">期间总数</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-date"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card open-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ periodStats.openPeriods }}</div>
            <div class="stat-label">开启期间</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="periodStats.openRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#67C23A"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-unlock"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card current-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ periodStats.currentPeriod }}</div>
            <div class="stat-label">当前期间</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="periodStats.currentProgress" 
                :show-text="false" 
                stroke-width="4" 
                color="#409EFF"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-time"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card locked-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ periodStats.lockedPeriods }}</div>
            <div class="stat-label">锁定期间</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="periodStats.lockedRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#F56C6C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-lock"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 会计年度选择 -->
    <el-card class="year-card" shadow="never">
      <div class="year-header">
        <h3>会计年度</h3>
        <el-button type="text" @click="handleYearSettings">年度设置</el-button>
      </div>
      <el-select
        v-model="selectedYear"
        placeholder="请选择会计年度"
        size="small"
        style="width: 200px"
        @change="handleYearChange"
      >
        <el-option
          v-for="year in fiscalYears"
          :key="year.value"
          :label="year.label"
          :value="year.value"
        />
      </el-select>
    </el-card>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="期间名称">
          <el-input
            v-model="queryForm.periodName"
            placeholder="请输入期间名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="期间类型">
          <el-select
            v-model="queryForm.periodType"
            placeholder="请选择期间类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in periodTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="期间状态">
          <el-select
            v-model="queryForm.periodStatus"
            placeholder="请选择期间状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in periodStatusOptions"
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

    <!-- 期间列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算期间列表</span>
        <div class="table-tools">
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
        :data="periodList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column prop="periodCode" label="期间编码" width="120" show-overflow-tooltip />
        <el-table-column prop="periodName" label="期间名称" width="150" show-overflow-tooltip />

        <el-table-column prop="budgetYear" label="会计年度" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" type="primary">{{ scope.row.budgetYear }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="periodType" label="期间类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getPeriodTypeColor(scope.row.periodType)">
              {{ getPeriodTypeText(scope.row.periodType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="startDate" label="开始日期" width="120" align="center" />
        <el-table-column prop="endDate" label="结束日期" width="120" align="center" />
        
        <el-table-column prop="periodDays" label="期间天数" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <span class="number-text">{{ scope.row.periodDays }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="periodStatus" label="期间状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getPeriodStatusType(scope.row.periodStatus)" size="mini">
              {{ getPeriodStatusText(scope.row.periodStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="isLocked" label="锁定状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isLocked"
              :disabled="!canLock(scope.row)"
              @change="handleLockChange(scope.row)"
              @click.native.stop
            />
          </template>
        </el-table-column>
        
        <el-table-column prop="isCurrent" label="当前期间" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isCurrent" type="success" size="mini">
              <i class="el-icon-check"></i> 当前
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="lastModifyTime" label="最后修改" width="150" align="center" />
        
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click.stop="handleView(scope.row)"
            >查看</el-button>
            <el-button
              v-if="canEdit(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              v-if="canOpen(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-unlock"
              class="success-text"
              @click.stop="handleOpen(scope.row)"
            >开启</el-button>
            <el-button
              v-if="canClose(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-lock"
              class="warning-text"
              @click.stop="handleClose(scope.row)"
            >关闭</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
              @click.native.stop
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="setCurrent" icon="el-icon-time">设为当前</el-dropdown-item>
                <el-dropdown-item command="history" icon="el-icon-time">操作历史</el-dropdown-item>
                <el-dropdown-item command="report" icon="el-icon-s-data">期间报告</el-dropdown-item>
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

    <!-- 新增/编辑期间对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="periodForm"
        :model="periodForm"
        :rules="periodRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="期间名称" prop="periodName">
              <el-input
                v-model="periodForm.periodName"
                placeholder="请输入期间名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="期间编码" prop="periodCode">
              <el-input
                v-model="periodForm.periodCode"
                placeholder="请输入期间编码"
                :disabled="!!periodForm.id"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="会计年度" prop="budgetYear">
              <el-select
                v-model="periodForm.budgetYear"
                placeholder="请选择会计年度"
                style="width: 100%"
              >
                <el-option
                  v-for="year in fiscalYears"
                  :key="year.value"
                  :label="year.label"
                  :value="year.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="期间类型" prop="periodType">
              <el-select
                v-model="periodForm.periodType"
                placeholder="请选择期间类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in periodTypeOptions"
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
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="periodForm.startDate"
                type="date"
                placeholder="选择开始日期"
                style="width: 100%"
                @change="handleDateChange"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="periodForm.endDate"
                type="date"
                placeholder="选择结束日期"
                style="width: 100%"
                @change="handleDateChange"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="期间描述" prop="periodDescription">
          <el-input
            v-model="periodForm.periodDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入期间描述"
          />
        </el-form-item>
        
        <el-form-item label="期间配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="periodForm.isCurrent">设为当前期间</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="periodForm.autoOpen">自动开启</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="periodForm.allowAdjustment">允许调整</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
        
        <el-form-item label="期间权限">
          <el-checkbox-group v-model="periodForm.permissions">
            <el-checkbox label="BUDGET_ENTRY">预算录入</el-checkbox>
            <el-checkbox label="BUDGET_ADJUST">预算调整</el-checkbox>
            <el-checkbox label="BUDGET_APPROVE">预算审批</el-checkbox>
            <el-checkbox label="BUDGET_REPORT">预算报告</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm">保存期间</el-button>
      </div>
    </el-dialog>

    <!-- 年度设置对话框 -->
    <el-dialog
      title="年度设置"
      :visible.sync="yearDialogVisible"
      width="500px"
      :close-on-click-modal="false"
      @close="handleYearDialogClose"
    >
      <div class="year-settings">
        <el-form :model="yearForm" label-width="80px" size="small">
          <el-form-item label="新增年度">
            <el-row :gutter="10">
              <el-col :span="14">
                <el-input
                  v-model="yearForm.yearValue"
                  placeholder="请输入年份"
                  type="number"
                  :min="2000"
                  :max="2100"
                />
              </el-col>
              <el-col :span="10">
                <el-button type="primary" @click="handleAddYear" style="width: 100%">添加</el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>

        <div class="year-list">
          <h4>可用年度</h4>
          <div v-for="year in fiscalYears" :key="year.value" class="year-item">
            <div class="year-label">
              {{ year.label }}
              <el-tag v-if="year.isCurrent === 1" type="success" size="mini" style="margin-left:8px">当前</el-tag>
            </div>
            <div class="year-actions">
              <el-button
                v-if="year.isCurrent !== 1"
                type="text"
                size="mini"
                icon="el-icon-check"
                @click="handleSetCurrentYear(year)"
              >选为当前</el-button>
              <el-button
                type="text"
                icon="el-icon-delete"
                size="mini"
                class="delete-btn"
                @click="handleDeleteYear(year.yearId)"
              >删除</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 期间详情对话框 -->
    <el-dialog
      title="期间详情"
      :visible.sync="detailDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="period-detail">
        <div class="detail-section">
          <h4>基本信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="期间名称">{{ periodDetail.periodName }}</el-descriptions-item>
            <el-descriptions-item label="期间编码">{{ periodDetail.periodCode }}</el-descriptions-item>
            <el-descriptions-item label="会计年度">{{ periodDetail.budgetYear }}</el-descriptions-item>
            <el-descriptions-item label="期间类型">{{ getPeriodTypeText(periodDetail.periodType) }}</el-descriptions-item>
            <el-descriptions-item label="开始日期">{{ periodDetail.startDate }}</el-descriptions-item>
            <el-descriptions-item label="结束日期">{{ periodDetail.endDate }}</el-descriptions-item>
            <el-descriptions-item label="期间天数">{{ periodDetail.periodDays }}</el-descriptions-item>
            <el-descriptions-item label="期间状态">
              <el-tag :type="getPeriodStatusType(periodDetail.periodStatus)" size="mini">
                {{ getPeriodStatusText(periodDetail.periodStatus) }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>
        
        <div class="detail-section">
          <h4>期间统计</h4>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="stat-item">
                <span class="stat-label">预算任务数：</span>
                <span class="stat-value">{{ periodDetail.budgetTaskCount || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="stat-item">
                <span class="stat-label">预算金额：</span>
                <span class="stat-value amount-text">{{ formatAmount(periodDetail.budgetAmount) }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 操作历史弹窗 -->
    <el-dialog
      :title="`操作历史 - ${historyPeriod.periodName || ''}`"
      :visible.sync="historyDialogVisible"
      width="780px"
      :close-on-click-modal="false"
    >
      <el-table :data="historyList" v-loading="historyLoading" border size="small" style="width:100%">
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getOpTypeTag(scope.row.operationType)" size="mini">
              {{ getOpTypeText(scope.row.operationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationDesc" label="操作描述" min-width="140" />
        <el-table-column prop="beforeValue" label="修改前" width="110" show-overflow-tooltip />
        <el-table-column prop="afterValue" label="修改后" width="110" show-overflow-tooltip />
        <el-table-column prop="operator" label="操作人" width="90" />
        <el-table-column prop="operateTime" label="操作时间" width="160">
          <template slot-scope="scope">{{ formatDate(scope.row.operateTime) }}</template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 期间报告弹窗 -->
    <el-dialog
      title="期间报告"
      :visible.sync="reportDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div v-loading="reportLoading">
        <el-row :gutter="16" style="margin-bottom:16px">
          <el-col :span="6">
            <el-card shadow="never" class="report-stat-card">
              <div class="report-stat-num">{{ reportData.budgetTaskCount || 0 }}</div>
              <div class="report-stat-label">预算任务数</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="never" class="report-stat-card">
              <div class="report-stat-num">{{ formatAmount(reportData.budgetAmount) }}</div>
              <div class="report-stat-label">预算金额</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="never" class="report-stat-card">
              <div class="report-stat-num">{{ formatAmount(reportData.executionAmount) }}</div>
              <div class="report-stat-label">执行金额</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="never" class="report-stat-card">
              <div class="report-stat-num">{{ reportData.executionRate || 0 }}%</div>
              <div class="report-stat-label">执行率</div>
            </el-card>
          </el-col>
        </el-row>
        <el-table :data="reportDetailList" border size="small" style="width:100%">
          <el-table-column prop="accountCode" label="科目编码" width="110" />
          <el-table-column prop="accountName" label="科目名称" min-width="140" show-overflow-tooltip />
          <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
            <template slot-scope="scope">{{ formatAmount(scope.row.budgetAmount) }}</template>
          </el-table-column>
          <el-table-column prop="executionAmount" label="执行金额" width="120" align="right">
            <template slot-scope="scope">{{ formatAmount(scope.row.executionAmount) }}</template>
          </el-table-column>
          <el-table-column prop="variance" label="差异" width="110" align="right">
            <template slot-scope="scope">
              <span :style="{ color: scope.row.variance < 0 ? '#F56C6C' : '#67C23A' }">
                {{ formatAmount(scope.row.variance) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="varianceRate" label="差异率" width="90" align="right">
            <template slot-scope="scope">{{ scope.row.varianceRate }}%</template>
          </el-table-column>
        </el-table>
        <div style="text-align:right;margin-top:10px">
          <el-pagination
            :current-page="reportDetailParams.pageNum"
            :page-size="reportDetailParams.pageSize"
            :total="reportDetailTotal"
            layout="total, prev, pager, next"
            @current-change="handleReportDetailPageChange"
          />
        </div>
      </div>
      <div slot="footer">
        <el-button @click="reportDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 期间设置对话框 -->
    <el-dialog title="期间设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="120px" size="small">
        <el-form-item label="默认期间类型">
          <el-select v-model="settingsForm.defaultType" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in periodTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="自动开启期间">
          <el-switch v-model="settingsForm.autoOpen" />
        </el-form-item>
        <el-form-item label="允许调整">
          <el-switch v-model="settingsForm.allowAdjustment" />
        </el-form-item>
        <el-form-item label="锁定已关闭期间">
          <el-switch v-model="settingsForm.lockClosed" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetPeriodApi, budgetYearApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetPeriod',
  data() {
    return {
      // 查询参数
      queryForm: {
        periodName: '',
        periodType: '',
        periodStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        orderByColumn: 'createTime',
        isAsc: 'desc'
      },
      
      // 表格数据
      loading: false,
      periodList: [],
      total: 0,
      selectedRows: [],
      
      // 统计数据
      periodStats: {
        totalPeriods: 0,
        openPeriods: 0,
        currentPeriod: '',
        lockedPeriods: 0,
        openRate: 0,
        currentProgress: 0,
        lockedRate: 0
      },
      
      // 年度相关
      selectedYear: '',
      currentYearStatus: 'OPEN',
      fiscalYears: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      detailDialogVisible: false,
      yearDialogVisible: false,
      yearForm: {
        yearValue: '',
        yearLabel: ''
      },
      periodForm: {
        id: null,
        periodName: '',
        periodCode: '',
        budgetYear: '',
        periodType: '',
        startDate: null,
        endDate: null,
        periodDescription: '',
        isCurrent: false,
        autoOpen: false,
        allowAdjustment: true,
        permissions: []
      },
      periodRules: {
        periodName: [
          { required: true, message: '请输入期间名称', trigger: 'blur' }
        ],
        periodCode: [
          { required: true, message: '请输入期间编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '期间编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        budgetYear: [
          { required: true, message: '请选择会计年度', trigger: 'change' }
        ],
        periodType: [
          { required: true, message: '请选择期间类型', trigger: 'change' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ],
        endDate: [
          { required: true, message: '请选择结束日期', trigger: 'change' }
        ]
      },

      // 期间详情
      periodDetail: {},

      // 操作历史弹窗
      historyDialogVisible: false,
      historyLoading: false,
      historyList: [],
      historyPeriod: {},

      // 期间报告弹窗
      reportDialogVisible: false,
      reportLoading: false,
      reportData: {},
      reportDetailList: [],
      reportDetailTotal: 0,
      reportDetailParams: { pageNum: 1, pageSize: 10, periodId: '' },
      
      // 选项数据
      periodTypeOptions: [
        { value: 'MONTHLY', label: '月' },
        { value: 'QUARTERLY', label: '季' },
        { value: 'YEARLY', label: '年' },
        { value: 'CUSTOM', label: '自定义' }
      ],
      periodStatusOptions: [
        { value: 'OPEN', label: '开启' },
        { value: 'CLOSED', label: '关闭' },
        { value: 'LOCKED', label: '锁定' }
      ],

      settingsDialogVisible: false,
      settingsForm: {
        defaultType: 'MONTHLY',
        autoOpen: false,
        allowAdjustment: true,
        lockClosed: false
      }
    }
  },
  
  async created() {
    await this.loadFiscalYears()
    this.getList()
  },

  methods: {
    // 获取统计数据（从列表数据中计算，不依赖独立接口）
    calcStats() {
      const list = this.periodList
      const total = list.length
      const open = list.filter(i => i.periodStatus === 'OPEN').length
      const locked = list.filter(i => i.isLocked).length
      this.periodStats = {
        totalPeriods: this.total || total,
        openPeriods: open,
        currentPeriod: this.selectedYear || '无',
        lockedPeriods: locked,
        openRate: total ? Math.round((open / total) * 100) : 0,
        currentProgress: 50,
        lockedRate: total ? Math.round((locked / total) * 100) : 0
      }
    },

    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.queryParams,
          budgetYear: this.selectedYear
        }
        const res = await budgetPeriodApi.getPage(params)
        const pageData = res && res.data
        const records = (pageData && pageData.records) || []
        // Integer (0/1) 转 Boolean，兼容数字和字符串
        this.periodList = records.map(item => ({
          ...item,
          isCurrent: Number(item.isCurrent) === 1,
          isLocked: Number(item.isLocked) === 1,
          autoOpen: Number(item.autoOpen) === 1,
          allowAdjustment: Number(item.allowAdjustment) === 1
        }))
        this.total = (pageData && pageData.total) || 0
        this.calcStats()
      } catch (error) {
        console.error('获取列表错误：', error)
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
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
        periodName: '',
        periodType: '',
        periodStatus: ''
      }
      this.handleQuery()
    },

    // 年改变
    handleYearChange(value) {
      this.selectedYear = value
      this.queryParams.pageNum = 1
      // 同步更新年度状态
      const year = this.fiscalYears.find(y => y.value === value)
      this.currentYearStatus = year ? (year.yearStatus || 'OPEN') : 'OPEN'
      this.getList()
    },

    // 创建期间
    handleCreatePeriod() {
      this.dialogTitle = '创建预算期间'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑期间
    handleEdit(row) {
      this.dialogTitle = '编辑预算期间'
      this.dialogVisible = true
      // 从 localStorage 恢复该期间的权限设置
      const savedPermissions = localStorage.getItem(`period_permissions_${row.periodId}`)
      this.periodForm = {
        ...row,
        permissions: savedPermissions ? JSON.parse(savedPermissions) : []
      }
    },

    // 查看期间
    handleView(row) {
      this.periodDetail = row
      this.detailDialogVisible = true
    },

    // 开启期间
    async handleOpen(row) {
      try {
        await this.$confirm('确认开启该期间吗？', '提示', {
          type: 'warning'
        })
        await budgetPeriodApi.openPeriod(row.periodId)
        this.$message.success('期间开启成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('期间开启失败：' + error.message)
        }
      }
    },

    // 关闭期间
    async handleClose(row) {
      try {
        await this.$confirm('确认关闭该期间吗？关闭后将无法进行预算操作。', '提示', {
          type: 'warning'
        })
        await budgetPeriodApi.closePeriod(row.periodId)
        this.$message.success('期间关闭成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('期间关闭失败：' + error.message)
        }
      }
    },

    // 锁状态改变
    async handleLockChange(row) {
      try {
        await budgetPeriodApi.updateLockStatus(row.periodId, row.isLocked)
        this.$message.success('锁状态更新成功')
        this.getList()
      } catch (error) {
        this.$message.error('锁状态更新失败：' + error.message)
        // 恢复原状态
        row.isLocked = !row.isLocked
      }
    },

    // 日期改变
    handleDateChange() {
      if (this.periodForm.startDate && this.periodForm.endDate) {
        const start = new Date(this.periodForm.startDate)
        const end = new Date(this.periodForm.endDate)
        const diffTime = Math.abs(end - start)
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1
        this.periodForm.periodDays = diffDays
      }
    },

    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.periodForm.validate()

        if (new Date(this.periodForm.startDate) >= new Date(this.periodForm.endDate)) {
          this.$message.error('结束日期必须大于开始日期')
          return
        }

        const params = { ...this.periodForm }

        // 保存 permissions 到 localStorage，后端实体不支持该字段
        const permissions = params.permissions || []
        delete params.permissions

        // 类型转换：Boolean 转 Integer
        if (params.isCurrent !== undefined) {
          params.isCurrent = params.isCurrent ? 1 : 0
        }
        if (params.isLocked !== undefined) {
          params.isLocked = params.isLocked ? 1 : 0
        }
        if (params.autoOpen !== undefined) {
          params.autoOpen = params.autoOpen ? 1 : 0
        }
        if (params.allowAdjustment !== undefined) {
          params.allowAdjustment = params.allowAdjustment ? 1 : 0
        }

        // 类型转换：String 转 Integer（budgetYear）
        if (params.budgetYear !== undefined && typeof params.budgetYear === 'string') {
          params.budgetYear = parseInt(params.budgetYear, 10)
        }

        if (this.periodForm.periodId) {
          // 更新操作
          await budgetPeriodApi.update(params)
          localStorage.setItem(`period_permissions_${params.periodId}`, JSON.stringify(permissions))
          this.$message.success('更新成功')
        } else {
          // 新增操作，移除 id 字段
          delete params.id
          const res = await budgetPeriodApi.create(params)
          // 新增成功后用返回的 periodId 存储权限
          const newId = (res && res.data && res.data.periodId) || params.periodId
          if (newId) localStorage.setItem(`period_permissions_${newId}`, JSON.stringify(permissions))
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
      this.periodForm = {
        id: null,
        periodName: '',
        periodCode: '',
        budgetYear: this.selectedYear,
        periodType: '',
        startDate: null,
        endDate: null,
        periodDescription: '',
        isCurrent: false,
        autoOpen: false,
        allowAdjustment: true,
        permissions: []
      }
      this.$nextTick(() => {
        this.$refs.periodForm && this.$refs.periodForm.clearValidate()
      })
    },

    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },

    // 初始化年度
    async handleInitYear() {
      try {
        await this.$confirm('确认初始化当前年度吗？这将创建12个月度期间年吗？这将创建12个月度期间。', '提示', {
          type: 'warning'
        })
        await budgetPeriodApi.initYear(this.selectedYear)
        this.$message.success('年初始化成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('年初始化失败：' + error.message)
        }
      }
    },

    // 关闭年度
    async handleCloseYear() {
      try {
        await this.$confirm('确认关闭当前年度吗？这将关闭该年度的所有期间。', '提示', {
          type: 'warning'
        })
        await budgetPeriodApi.closeYear(this.selectedYear)
        this.$message.success('年度关闭成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('年度关闭失败：' + error.message)
        }
      }
    },

    // 批量开启
    async handleBatchOpen() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要开启的期间')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.periodId)
        await budgetPeriodApi.batchOpen(ids)
        this.$message.success('批量开启完成')
        this.getList()
      } catch (error) {
        this.$message.error('批量开启失败：' + error.message)
      }
    },

    // 批量关闭
    async handleBatchClose() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要关闭的期间')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.periodId)
        await budgetPeriodApi.batchClose(ids)
        this.$message.success('批量关闭完成')
        this.getList()
      } catch (error) {
        this.$message.error('批量关闭失败：' + error.message)
      }
    },

    // 导出期间
    async handleExportPeriod() {
      try {
        let exportData
        if (this.selectedRows.length > 0) {
          // 有勾选则导出勾选数据
          exportData = this.selectedRows
        } else {
          // 无勾选则导出当前页数据
          exportData = this.periodList
        }

        const params = {
          budgetYear: this.selectedYear,
          pageSize: this.queryParams.pageSize,
          pageNum: this.queryParams.pageNum,
          periodIds: exportData.map(row => row.periodId)
        }

        const res = await budgetPeriodApi.export(params)
        // 触发文件下载
        const blob = new Blob([res], { type: 'text/csv;charset=utf-8' })
        const url = URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `预算期间_${this.selectedYear}_${new Date().toLocaleDateString()}.csv`
        link.click()
        URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || ''))
      }
    },

    // 刷新
    handleRefresh() {
      this.getList()
    },

    // 设置
    handleSettings() {
      this.settingsDialogVisible = true
    },

    // 年设置
    handleYearSettings() {
      this.yearDialogVisible = true
    },

    // 加载年度列表（从后端）
    async loadFiscalYears() {
      try {
        const res = await budgetYearApi.getAll()
        const list = (res && res.data) || []
        this.fiscalYears = list.map(y => ({
          yearId: y.yearId,
          value: String(y.budgetYear),
          label: y.budgetYear + '年',
          isCurrent: y.isCurrent,
          yearStatus: y.yearStatus
        }))
        // 自动选中标记为当前的年度，否则取第一个
        const current = this.fiscalYears.find(y => y.isCurrent === 1)
        const selected = current || this.fiscalYears[0]
        this.selectedYear = selected ? selected.value : ''
        this.currentYearStatus = selected ? (selected.yearStatus || 'OPEN') : 'OPEN'
      } catch (e) {
        this.$message.error('加载年度列表失败')
      }
    },

    // 添加年度
    async handleAddYear() {
      const val = String(this.yearForm.yearValue || '').trim()
      if (!val || val.length !== 4 || isNaN(val)) {
        this.$message.warning('请输入有效的4位年份')
        return
      }
      try {
        await budgetYearApi.create({ budgetYear: parseInt(val) })
        this.$message.success('年度添加成功')
        this.yearForm.yearValue = ''
        await this.loadFiscalYears()
      } catch (e) {
        this.$message.error('添加失败：' + (e.msg || e.message || ''))
      }
    },

    // 删除年度
    handleDeleteYear(yearId) {
      this.$confirm('确认删除该年度吗？', '提示', { type: 'warning' }).then(async () => {
        try {
          await budgetYearApi.delete(yearId)
          this.$message.success('年度删除成功')
          await this.loadFiscalYears()
          // 如果删除的是当前选中年度，切换到第一个
          if (!this.fiscalYears.find(y => y.value === this.selectedYear) && this.fiscalYears.length > 0) {
            this.selectedYear = this.fiscalYears[0].value
            this.getList()
          }
        } catch (e) {
          this.$message.error(e.msg || '删除失败')
        }
      }).catch(() => {})
    },

    // 设为当前年度
    async handleSetCurrentYear(year) {
      try {
        await budgetYearApi.setCurrent(year.yearId)
        this.$message.success(`已切换到 ${year.label}`)
        await this.loadFiscalYears()
        this.handleYearChange(year.value)
      } catch (e) {
        this.$message.error('切换失败')
      }
    },

    // 关闭年度对话框
    handleYearDialogClose() {
      this.yearForm.yearValue = ''
    },

    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopy(row)
          break
        case 'setCurrent':
          this.handleSetCurrent(row)
          break
        case 'history':
          this.handleHistory(row)
          break
        case 'report':
          this.handleReport(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
      }
    },

    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算期间'
      this.dialogVisible = true
      this.periodForm = { ...row, id: null, periodCode: null, isCurrent: false }
    },

    // 设为当前
    async handleSetCurrent(row) {
      try {
        await this.$confirm('确认设置为当前期间吗？', '提示', {
          type: 'warning'
        })
        await budgetPeriodApi.setCurrent(row.periodId)
        this.$message.success('设置成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('设置失败：' + error.message)
        }
      }
    },

    // 操作历史
    async handleHistory(row) {
      this.historyPeriod = row
      this.historyDialogVisible = true
      this.historyLoading = true
      try {
        const res = await budgetPeriodApi.getHistory(row.periodId)
        this.historyList = (res && res.data) || []
      } catch (e) {
        this.$message.error('获取操作历史失败')
      } finally {
        this.historyLoading = false
      }
    },

    // 期间报告
    async handleReport(row) {
      this.reportDialogVisible = true
      this.reportLoading = true
      this.reportDetailParams = { pageNum: 1, pageSize: 10, periodId: row.periodId }
      try {
        const [reportRes, detailRes] = await Promise.all([
          budgetPeriodApi.getReport(row.periodId),
          budgetPeriodApi.getReportDetail(this.reportDetailParams)
        ])
        this.reportData = (reportRes && reportRes.data) || {}
        const detail = (detailRes && detailRes.data) || {}
        this.reportDetailList = detail.records || []
        this.reportDetailTotal = detail.total || 0
      } catch (e) {
        this.$message.error('获取期间报告失败')
      } finally {
        this.reportLoading = false
      }
    },

    // 期间报告详情翻页
    async handleReportDetailPageChange(page) {
      this.reportDetailParams.pageNum = page
      const res = await budgetPeriodApi.getReportDetail(this.reportDetailParams)
      const detail = (res && res.data) || {}
      this.reportDetailList = detail.records || []
      this.reportDetailTotal = detail.total || 0
    },

    // 导出单个
    async handleExportSingle(row) {
      try {
        await budgetPeriodApi.exportSingle(row.periodId)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
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
      this.$message.info('列设置功能开发中...')
    },

    // 判断是否可以编辑
    canEdit(row) {
      return row.periodStatus !== 'LOCKED'
    },

    // 判断是否可以开启
    canOpen(row) {
      return row.periodStatus === 'CLOSED'
    },

    // 判断是否可以关闭
    canClose(row) {
      return row.periodStatus === 'OPEN'
    },

    // 判断是否可以锁
    canLock(row) {
      return row.periodStatus === 'CLOSED'
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN', { hour12: false })
    },

    // 操作类型 tag 颜色
    getOpTypeTag(type) {
      const map = { CREATE: 'success', UPDATE: '', DELETE: 'danger', OPEN: 'success', CLOSE: 'warning', LOCK: 'danger', UNLOCK: 'info' }
      return map[type] || 'info'
    },

    // 操作类型文本
    getOpTypeText(type) {
      const map = { CREATE: '创建', UPDATE: '更新', DELETE: '删除', OPEN: '开启', CLOSE: '关闭', LOCK: '锁定', UNLOCK: '解锁' }
      return map[type] || type
    },

    // 获取年状态类型
    getYearStatusType(status) {
      const statusMap = {
        'OPEN': 'success',
        'CLOSED': 'warning',
        'LOCKED': 'danger'
      }
      return statusMap[status] || 'info'
    },

    // 获取年度状态文本
    getYearStatusText(status) {
      const textMap = {
        'OPEN': '开启',
        'CLOSED': '关闭',
        'LOCKED': '锁定'
      };
      return textMap[status] || status
    },

    // 获取期间类型颜色
    getPeriodTypeColor(type) {
      const colorMap = {
        'MONTHLY': 'primary',
        'QUARTERLY': 'success',
        'YEARLY': 'warning',
        'CUSTOM': 'info'
      }
      return colorMap[type] || 'info'
    },

    // 获取期间类型文本
    getPeriodTypeText(type) {
      const item = this.periodTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },

    // 获取期间状态类型
    getPeriodStatusType(status) {
      const statusMap = {
        'OPEN': 'success',
        'CLOSED': 'warning',
        'LOCKED': 'danger'
      }
      return statusMap[status] || 'info'
    },

    // 获取期间状态文本
    getPeriodStatusText(status) {
      const item = this.periodStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    }
  }
}
</script>
<style lang="scss" scoped>
.budget-period {
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
  .year-card,
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
      
      &.open-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.current-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.locked-card {
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
  
  .year-card {
    .year-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h3 {
        color: #303133;
        margin: 0;
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
      gap: 8px;
    }
  }
  
  .number-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #409EFF;
  }
  
  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #67C23A;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .period-detail {
    .detail-section {
      margin-bottom: 20px;
      
      h4 {
        color: #303133;
        margin: 0 0 15px 0;
        padding-bottom: 8px;
        border-bottom: 1px solid #EBEEF5;
      }
      
      .stat-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 8px 0;
        
        .stat-label {
          color: #606266;
        }
        
        .stat-value {
          font-weight: 500;
          color: #303133;
        }
      }
    }
  }
  
  .success-text {
    color: #67C23A;
  }
  
  .warning-text {
    color: #E6A23C;
  }
  
  .text-right {
    text-align: right;
  }

  .year-settings {
    .year-list {
      margin-top: 20px;
      max-height: 400px;
      overflow-y: auto;

      h4 {
        color: #303133;
        margin: 0 0 15px 0;
        font-size: 16px;
      }

      .year-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px 0;
        border-bottom: 1px solid #EBEEF5;

        &:last-child {
          border-bottom: none;
        }

        .year-actions {
          display: flex;
          align-items: center;
          gap: 4px;
        }

        .delete-btn {
          color: #F56C6C;

          &:hover {
            color: #F56C6C;
          }
        }
      }
    }
  }
}

.report-stat-card {
  text-align: center;
  padding: 8px 0;
  .report-stat-num {
    font-size: 20px;
    font-weight: bold;
    color: #303133;
  }
  .report-stat-label {
    font-size: 12px;
    color: #909399;
    margin-top: 4px;
  }
}
</style>
