<template>
  <div class="budget-reserve">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算储备管理</h2>
      <p>管理预算储备资金的分配、使用和释放，确保资金的合理配置和有效利用</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateReserve">创建储备</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportReserves">导入储备</el-button>
            <el-button type="warning" icon="el-icon-unlock" @click="handleBatchRelease">批量释放</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportReserves">导出储备</el-button>
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

    <!-- 储备统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ reserveStats.totalReserves }}</div>
            <div class="stat-label">储备总数</div>
            <div class="stat-description">所有预算储备数量</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-bank-card"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card reserved-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ reserveStats.reservedAmount }}</div>
            <div class="stat-label">储备金额</div>
            <div class="stat-description">已储备的资金总额</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="reserveStats.reserveRate" 
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
            <div class="stat-number">{{ reserveStats.usedAmount }}</div>
            <div class="stat-label">已用金额</div>
            <div class="stat-description">已使用的储备金额</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="reserveStats.usageRate" 
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
        <el-card class="stat-card available-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ reserveStats.availableAmount }}</div>
            <div class="stat-label">可用金额</div>
            <div class="stat-description">可使用的储备余额</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="reserveStats.availableRate" 
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
        <el-form-item label="储备名称">
          <el-input
            v-model="queryForm.reserveName"
            placeholder="请输入储备名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="储备类型">
          <el-select
            v-model="queryForm.reserveType"
            placeholder="请选择储备类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in reserveTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="组织单元">
          <el-cascader
            v-model="queryForm.organizationId"
            :options="organizationOptions"
            :props="{ checkStrictly: true, emitPath: false }"
            placeholder="请选择组织单元"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="预算科目">
          <el-select
            v-model="queryForm.budgetAccountId"
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
        <el-form-item label="储备状态">
          <el-select
            v-model="queryForm.reserveStatus"
            placeholder="请选择储备状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in reserveStatusOptions"
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

    <!-- 储备列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算储备列表</span>
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
        :data="reserveList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column v-if="columnVisible.reserveCode" prop="reserveCode" label="储备编码" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.reserveName" prop="reserveName" label="储备名称" min-width="200" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.reserveType" prop="reserveType" label="储备类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getReserveTypeColor(scope.row.reserveType)">
              {{ getReserveTypeText(scope.row.reserveType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.organizationName" prop="organizationName" label="组织单元" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.budgetAccountName" prop="budgetAccountName" label="预算科目" width="150" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.reserveAmount" prop="reserveAmount" label="储备金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.reserveAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.usedAmount" prop="usedAmount" label="已用金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.usedAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.availableAmount" prop="availableAmount" label="可用金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span :class="getAvailableAmountClass(scope.row.availableAmount)">
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

        <el-table-column v-if="columnVisible.reserveStatus" prop="reserveStatus" label="储备状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getReserveStatusType(scope.row.reserveStatus)" size="mini">
              {{ getReserveStatusText(scope.row.reserveStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.createDate" prop="createDate" label="创建日期" width="120" align="center" />
        <el-table-column v-if="columnVisible.expiryDate" prop="expiryDate" label="到期日期" width="120" align="center" />
        
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
              class="primary-text"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              v-if="canUse(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-money"
              class="success-text"
              @click="handleUse(scope.row)"
            >使用</el-button>
            <el-button
              v-if="canRelease(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-unlock"
              class="warning-text"
              @click="handleRelease(scope.row)"
            >释放</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="history" icon="el-icon-time">使用历史</el-dropdown-item>
                <el-dropdown-item command="transfer" icon="el-icon-sort">转移储备</el-dropdown-item>
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

    <!-- 新增/编辑储备对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="reserveForm"
        :model="reserveForm"
        :rules="reserveRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="储备名称" prop="reserveName">
              <el-input
                v-model="reserveForm.reserveName"
                placeholder="请输入储备名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="储备编码" prop="reserveCode">
              <el-input
                v-model="reserveForm.reserveCode"
                placeholder="请输入储备编码"
                :disabled="!!reserveForm.reserveId"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="储备类型" prop="reserveType">
              <el-select
                v-model="reserveForm.reserveType"
                placeholder="请选择储备类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in reserveTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="储备金额" prop="reserveAmount">
              <el-input-number
                v-model="reserveForm.reserveAmount"
                :precision="2"
                :min="0"
                placeholder="请输入储备金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组织单元" prop="organizationId">
              <el-cascader
                v-model="reserveForm.organizationId"
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
                v-model="reserveForm.budgetAccountId"
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
            <el-form-item label="创建日期" prop="createDate">
              <el-date-picker
                v-model="reserveForm.createDate"
                type="date"
                placeholder="请选择创建日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="expiryDate">
              <el-date-picker
                v-model="reserveForm.expiryDate"
                type="date"
                placeholder="请选择到期日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="储备描述" prop="reserveDescription">
          <el-input
            v-model="reserveForm.reserveDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入储备描述"
          />
        </el-form-item>
        
        <!-- 使用规则 -->
        <el-form-item label="使用规则">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="使用方式" prop="usageMethod">
                <el-select
                  v-model="reserveForm.usageMethod"
                  placeholder="使用方式"
                  style="width: 100%"
                >
                  <el-option value="APPROVAL" label="审批使用" />
                  <el-option value="AUTO" label="自动使用" />
                  <el-option value="MANUAL" label="手动使用" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="单次限额" prop="singleLimit">
                <el-input-number
                  v-model="reserveForm.singleLimit"
                  :precision="2"
                  :min="0"
                  placeholder="单次使用限额"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="优先级" prop="priority">
                <el-select
                  v-model="reserveForm.priority"
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
        
        <el-form-item label="储备配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="reserveForm.isActive">启用储备</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="reserveForm.allowTransfer">允许转移</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="reserveForm.autoRelease">自动释放</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleCalculateReserve">计算储备</el-button>
        <el-button type="primary" @click="handleSubmitForm">保存储备</el-button>
      </div>
    </el-dialog>

    <!-- 储备使用对话框 -->
    <el-dialog
      title="储备使用"
      :visible.sync="useDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="reserve-use" v-if="currentReserve">
        <div class="reserve-info">
          <h4>储备信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="储备名称">{{ currentReserve.reserveName }}</el-descriptions-item>
            <el-descriptions-item label="储备金额">{{ formatAmount(currentReserve.reserveAmount) }}</el-descriptions-item>
            <el-descriptions-item label="已用金额">{{ formatAmount(currentReserve.usedAmount) }}</el-descriptions-item>
            <el-descriptions-item label="可用金额">{{ formatAmount(currentReserve.availableAmount) }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <el-form :model="useForm" label-width="120px" size="small">
          <el-form-item label="使用金额" prop="useAmount">
            <el-input-number
              v-model="useForm.useAmount"
              :precision="2"
              :min="0"
              :max="currentReserve.availableAmount"
              placeholder="请输入使用金额"
              style="width: 100%"
            />
          </el-form-item>
          
          <el-form-item label="使用用途" prop="usePurpose">
            <el-input
              v-model="useForm.usePurpose"
              placeholder="请输入使用用途"
            />
          </el-form-item>
          
          <el-form-item label="使用说明" prop="useRemark">
            <el-input
              v-model="useForm.useRemark"
              type="textarea"
              :rows="4"
              placeholder="请输入使用说明"
            />
          </el-form-item>
        </el-form>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="useDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitUse">确认使用</el-button>
      </div>
    </el-dialog>

    <!-- 储备释放对话框 -->
    <el-dialog
      title="储备释放"
      :visible.sync="releaseDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="reserve-release" v-if="currentReserve">
        <div class="reserve-info">
          <el-alert
            :title="`确认释放储备：${currentReserve.reserveName}`"
            type="warning"
            :description="`释放金额：${formatAmount(currentReserve.availableAmount)}`"
            show-icon
          />
        </div>
        
        <el-form :model="releaseForm" label-width="120px" size="small">
          <el-form-item label="释放方式" prop="releaseType">
            <el-radio-group v-model="releaseForm.releaseType">
              <el-radio label="PARTIAL">部分释放</el-radio>
              <el-radio label="FULL">全部释放</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item 
            v-if="releaseForm.releaseType === 'PARTIAL'"
            label="释放金额" 
            prop="releaseAmount"
          >
            <el-input-number
              v-model="releaseForm.releaseAmount"
              :precision="2"
              :min="0"
              :max="currentReserve.availableAmount"
              placeholder="请输入释放金额"
              style="width: 100%"
            />
          </el-form-item>
          
          <el-form-item label="释放说明" prop="releaseRemark">
            <el-input
              v-model="releaseForm.releaseRemark"
              type="textarea"
              :rows="4"
              placeholder="请输入释放说明"
            />
          </el-form-item>
        </el-form>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="releaseDialogVisible = false">取消</el-button>
        <el-button type="warning" @click="handleSubmitRelease">确认释放</el-button>
      </div>
    </el-dialog>

    <!-- 导入储备对话框 -->
    <el-dialog title="导入储备" :visible.sync="importDialogVisible" width="500px" :close-on-click-modal="false">
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

    <!-- 转移储备对话框 -->
    <el-dialog title="转移储备" :visible.sync="transferDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form v-if="currentReserve" :model="transferForm" label-width="120px" size="small">
        <el-form-item label="储备名称">
          <span>{{ currentReserve.reserveName }}</span>
        </el-form-item>
        <el-form-item label="可用金额">
          <span class="amount-text">{{ formatAmount(currentReserve.availableAmount) }}</span>
        </el-form-item>
        <el-form-item label="转移金额" prop="transferAmount">
          <el-input-number
            v-model="transferForm.transferAmount"
            :precision="2"
            :min="0.01"
            :max="currentReserve.availableAmount"
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

    <!-- 使用历史对话框 -->
    <el-dialog
      :title="'使用历史 - ' + (historyReserve ? historyReserve.reserveName || historyReserve.reserveCode : '')"
      :visible.sync="historyDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <el-table
        v-loading="historyLoading"
        :data="historyList"
        border
        stripe
        size="small"
        style="width: 100%"
        empty-text="暂无操作历史记录"
      >
        <el-table-column prop="operateTime" label="操作时间" width="170" align="center">
          <template slot-scope="scope">
            {{ scope.row.operateTime ? new Date(scope.row.operateTime).toLocaleString('zh-CN') : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="operationType" label="操作类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getHistoryTypeTag(scope.row.operationType)" size="mini">
              {{ getHistoryTypeText(scope.row.operationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="操作金额" width="130" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.amount" class="amount-text">{{ formatAmount(scope.row.amount) }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="operationDesc" label="操作描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="beforeValue" label="操作前" width="120" show-overflow-tooltip />
        <el-table-column prop="afterValue" label="操作后" width="120" show-overflow-tooltip />
        <el-table-column prop="operator" label="操作人" width="100" align="center" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
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
import { budgetReserveApi } from '@/api/managementAccountant/ncv65/budgetControl'

export default {
  name: 'BudgetReserve',
  data() {
    return {
      // 查询参数
      queryForm: {
        reserveName: '',
        reserveType: '',
        organizationId: '',
        budgetAccountId: '',
        reserveStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      reserveList: [],
      total: 0,
      selectedRows: [],
      
      // 实时监控
      realTimeMonitor: false,
      monitorTimer: null,
      
      // 统计数据
      reserveStats: {
        totalReserves: 0,
        reservedAmount: 0,
        usedAmount: 0,
        availableAmount: 0,
        reserveRate: 0,
        usageRate: 0,
        availableRate: 0
      },
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      useDialogVisible: false,
      releaseDialogVisible: false,
      currentReserve: null,
      reserveForm: {
        reserveId: null,
        reserveName: '',
        reserveCode: '',
        reserveType: '',
        reserveAmount: 0,
        budgetId: 'DEFAULT',
        reserveReason: '',
        expiryDate: '',
        reserveDescription: '',
        reserveBy: '',
        // 使用规则
        usageMethod: '',
        singleLimit: 0,
        priority: '',
        // 储备配置
        isActive: false,
        allowTransfer: false,
        autoRelease: false
      },
      reserveRules: {
        reserveName: [
          { required: true, message: '请输入储备名称', trigger: 'blur' }
        ],
        reserveCode: [
          { required: true, message: '请输入储备编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '储备编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        reserveType: [
          { required: true, message: '请选择储备类型', trigger: 'change' }
        ],
        reserveAmount: [
          { required: true, message: '请输入储备金额', trigger: 'blur' }
        ]
      },
      
      // 储备使用
      useForm: {
        useAmount: 0,
        usePurpose: '',
        useRemark: ''
      },
      
      // 储备释放
      releaseForm: {
        releaseType: 'PARTIAL',
        releaseAmount: 0,
        releaseRemark: ''
      },
      
      // 选项数据
      reserveTypeOptions: [
        { value: 'CONTINGENCY', label: '应急储备' },
        { value: 'STRATEGIC', label: '战略储备' },
        { value: 'OPERATIONAL', label: '运营储备' }
      ],
      reserveStatusOptions: [
        { value: 'PENDING', label: '待审批' },
        { value: 'APPROVED', label: '已审批' },
        { value: 'EXECUTED', label: '已执行' },
        { value: 'RELEASED', label: '已释放' },
        { value: 'EXPIRED', label: '已过期' }
      ],
      organizationOptions: [],
      budgetAccountOptions: [],

      // 导入
      importDialogVisible: false,
      importFileList: [],
      importLoading: false,
      importFile: null,

      // 转移
      transferDialogVisible: false,
      transferLoading: false,
      transferForm: {
        transferAmount: 0,
        targetOrganizationId: '',
        targetAccountId: '',
        transferRemark: ''
      },

      // 使用历史
      historyDialogVisible: false,
      historyLoading: false,
      historyList: [],
      historyReserve: null,

      // 列设置
      columnSettingVisible: false,
      columnOptions: [
        { key: 'reserveCode', label: '储备编码' },
        { key: 'reserveName', label: '储备名称' },
        { key: 'reserveType', label: '储备类型' },
        { key: 'organizationName', label: '组织单元' },
        { key: 'budgetAccountName', label: '预算科目' },
        { key: 'reserveAmount', label: '储备金额' },
        { key: 'usedAmount', label: '已用金额' },
        { key: 'availableAmount', label: '可用金额' },
        { key: 'usageRate', label: '使用率' },
        { key: 'reserveStatus', label: '储备状态' },
        { key: 'createDate', label: '创建日期' },
        { key: 'expiryDate', label: '到期日期' }
      ],
      columnChecked: ['reserveCode', 'reserveName', 'reserveType', 'organizationName', 'budgetAccountName', 'reserveAmount', 'usedAmount', 'availableAmount', 'usageRate', 'reserveStatus', 'createDate', 'expiryDate'],
      columnVisible: {
        reserveCode: true, reserveName: true, reserveType: true,
        organizationName: true, budgetAccountName: true, reserveAmount: true,
        usedAmount: true, availableAmount: true, usageRate: true,
        reserveStatus: true, createDate: true, expiryDate: true
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
        const response = await budgetReserveApi.getStatistics()
        if (response.code === 1 && response.data) {
          const d = response.data
          const total = d.totalCount || 0
          this.reserveStats = {
            totalReserves: total,
            reservedAmount: d.reservedAmount || 0,
            usedAmount: d.usedAmount || 0,
            availableAmount: d.availableAmount || 0,
            reserveRate: d.reserveRate || 0,
            usageRate: d.usageRate || 0,
            availableRate: d.availableRate || 0
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
        const response = await budgetReserveApi.getPage(params)
        this.reserveList = response.data.tlist || response.data.list || []
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
        const response = await budgetReserveApi.getOrganizations()
        if (response.code === 1 && response.data) {
          this.organizationOptions = response.data
        } else {
          console.warn('加载组织选项返回异常：', response.msg)
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
        const response = await budgetReserveApi.getBudgetAccounts()
        if (response.code === 1 && response.data) {
          this.budgetAccountOptions = response.data
        } else {
          console.warn('加载预算科目选项返回异常：', response.msg)
          this.budgetAccountOptions = []
        }
      } catch (error) {
        console.error('加载预算科目选项失败：', error)
        this.budgetAccountOptions = []
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
        reserveName: '',
        reserveType: '',
        organizationId: '',
        budgetAccountId: '',
        reserveStatus: ''
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
    
    // 创建储备
    handleCreateReserve() {
      this.dialogTitle = '创建预算储备'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 查看储备
    handleView(row) {
      this.currentReserve = row
      this.dialogTitle = '查看预算储备'
      this.reserveForm = this.rowToForm(row)
      this.dialogVisible = true
    },

    // 编辑储备
    handleEdit(row) {
      this.currentReserve = row
      this.dialogTitle = '编辑预算储备'
      this.reserveForm = this.rowToForm(row)
      this.dialogVisible = true
    },
    
    // 使用储备
    handleUse(row) {
      this.useDialogVisible = true
      this.currentReserve = row
      this.useForm = {
        useAmount: 0,
        usePurpose: '',
        useRemark: ''
      }
    },
    
    // 释放储备
    handleRelease(row) {
      this.releaseDialogVisible = true
      this.currentReserve = row
      this.releaseForm = {
        releaseType: 'PARTIAL',
        releaseAmount: 0,
        releaseRemark: ''
      }
    },
    
    // 计算储备
    async handleCalculateReserve() {
      try {
        const params = {
          reserveAmount: this.reserveForm.reserveAmount,
          reserveType: this.reserveForm.reserveType,
          organizationId: this.reserveForm.organizationId
        }
        const response = await budgetReserveApi.calculateReserve(params)
        const calculation = response.data
        
        this.$alert(`
          <p>储备金额：${this.formatAmount(calculation.reserveAmount)}</p>
          <p>储备类型：${calculation.reserveType}</p>
          <p>建议期限：${calculation.suggestedPeriod}天</p>
          <p>预计使用率：${calculation.expectedUsageRate}%</p>
        `, '储备计算结果', {
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
        await this.$refs.reserveForm.validate()

        const params = { ...this.reserveForm }
        // boolean 转 0/1 给后端
        params.isActive = params.isActive ? 1 : 0
        params.allowTransfer = params.allowTransfer ? 1 : 0
        params.autoRelease = params.autoRelease ? 1 : 0

        if (this.reserveForm.reserveId) {
          await budgetReserveApi.update(this.reserveForm.reserveId, params)
          this.$message.success('更新成功')
        } else {
          await budgetReserveApi.create(params)
          this.$message.success('创建成功')
        }

        this.dialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },
    
    // 提交使用
    async handleSubmitUse() {
      try {
        const params = {
          reserveId: this.currentReserve.reserveId,
          useAmount: this.useForm.useAmount,
          usePurpose: this.useForm.usePurpose,
          useRemark: this.useForm.useRemark
        }
        await budgetReserveApi.useReserve(params)
        this.$message.success('使用成功')
        this.useDialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('使用失败：' + error.message)
      }
    },
    
    // 提交释放
    async handleSubmitRelease() {
      try {
        const params = {
          reserveId: this.currentReserve.reserveId,
          releaseType: this.releaseForm.releaseType,
          releaseAmount: this.releaseForm.releaseAmount,
          releaseRemark: this.releaseForm.releaseRemark
        }
        await budgetReserveApi.releaseReserve(params)
        this.$message.success('释放成功')
        this.releaseDialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('释放失败：' + error.message)
      }
    },
    
    // 获取表单默认值
    getDefaultReserveForm() {
      return {
        reserveId: null,
        reserveName: '',
        reserveCode: '',
        reserveType: '',
        reserveAmount: 0,
        budgetId: 'DEFAULT',
        reserveReason: '',
        expiryDate: '',
        reserveDescription: '',
        reserveBy: '',
        usageMethod: '',
        singleLimit: 0,
        priority: '',
        isActive: false,
        allowTransfer: false,
        autoRelease: false
      }
    },

    // 将后端行数据转为表单数据（0/1 转 boolean）
    rowToForm(row) {
      const form = { ...this.getDefaultReserveForm(), ...row }
      form.isActive = !!form.isActive
      form.allowTransfer = !!form.allowTransfer
      form.autoRelease = !!form.autoRelease
      return form
    },

    // 重置表单
    resetForm() {
      this.reserveForm = this.getDefaultReserveForm()
      this.$nextTick(() => {
        this.$refs.reserveForm && this.$refs.reserveForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入储备
    handleImportReserves() {
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
        const response = await budgetReserveApi.import(formData)
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
    
    // 批量释放
    async handleBatchRelease() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要释放的储备')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.reserveId)
        await budgetReserveApi.batchRelease(ids)
        this.$message.success('批量释放成功')
        this.getList()
      } catch (error) {
        this.$message.error('批量释放失败：' + error.message)
      }
    },
    
    // 导出储备
    async handleExportReserves() {
      try {
        const params = { ...this.queryForm }
        const response = await budgetReserveApi.export(params)
        // 处理 blob 下载
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算储备数据.xlsx'
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
      this.columnChecked = Object.keys(this.columnVisible).filter(k => this.columnVisible[k])
      this.columnSettingVisible = true
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
      this.historyReserve = row
      this.historyDialogVisible = true
      this.historyLoading = true
      this.historyList = []
      try {
        const response = await budgetReserveApi.getHistory(row.reserveId)
        if (response.code === 1 && response.data) {
          this.historyList = response.data
        } else {
          this.$message.warning(response.msg || '查询历史记录失败')
        }
      } catch (error) {
        this.$message.error('查询历史记录失败：' + error.message)
      } finally {
        this.historyLoading = false
      }
    },
    
    // 转移储备
    handleTransfer(row) {
      this.currentReserve = row
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
          reserveId: this.currentReserve.reserveId,
          ...this.transferForm
        }
        const response = await budgetReserveApi.transfer(params)
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
      this.dialogTitle = '复制预算储备'
      this.dialogVisible = true
      this.reserveForm = { ...this.rowToForm(row), reserveId: null, reserveCode: null }
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        const response = await budgetReserveApi.exportSingle(row.reserveId)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `储备_${row.reserveCode || row.reserveId}.xlsx`
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
        await this.$confirm('确认删除该储备记录？删除后不可恢复', '提示', {
          type: 'warning'
        })
        await budgetReserveApi.delete(row.reserveId)
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
    
    // 判断是否可以使用
    canUse(row) {
      return ['APPROVED', 'EXECUTED'].includes(row.reserveStatus) && row.availableAmount > 0
    },

    // 判断是否可以释放
    canRelease(row) {
      return ['APPROVED', 'EXECUTED'].includes(row.reserveStatus) && row.availableAmount > 0
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取可用金额样式类
    getAvailableAmountClass(amount) {
      if (amount <= 0) return 'zero-amount'
      if (amount < 1000) return 'warning-amount'
      return 'positive-amount'
    },
    
    // 获取使用率颜色
    getUsageRateColor(rate) {
      if (rate >= 90) return '#F56C6C'
      if (rate >= 70) return '#E6A23C'
      return '#67C23A'
    },
    
    // 获取储备类型颜色
    getReserveTypeColor(type) {
      const colorMap = {
        'CONTINGENCY': 'warning',
        'STRATEGIC': 'primary',
        'OPERATIONAL': 'success'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取储备类型文本
    getReserveTypeText(type) {
      const item = this.reserveTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取储备状态类型
    getReserveStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'EXECUTED': '',
        'RELEASED': 'info',
        'EXPIRED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取储备状态文本
    getReserveStatusText(status) {
      const item = this.reserveStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    },

    // 获取历史操作类型标签颜色
    getHistoryTypeTag(type) {
      const map = {
        'USE': 'success',
        'RELEASE': 'warning',
        'TRANSFER': '',
        'CREATE': 'info',
        'UPDATE': 'info',
        'DELETE': 'danger',
        'APPROVE': 'success',
        'EXECUTE': ''
      }
      return map[type] || 'info'
    },

    // 获取历史操作类型文本
    getHistoryTypeText(type) {
      const map = {
        'USE': '使用',
        'RELEASE': '释放',
        'TRANSFER': '转移',
        'CREATE': '创建',
        'UPDATE': '更新',
        'DELETE': '删除',
        'APPROVE': '审批',
        'EXECUTE': '执行'
      }
      return map[type] || type
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-reserve {
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
      
      &.reserved-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.used-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.available-card {
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
  
  .zero-amount {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #909399;
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
  
  .reserve-use,
  .reserve-release {
    .reserve-info {
      margin-bottom: 20px;
      
      h4 {
        color: #303133;
        font-size: 14px;
        margin: 0 0 10px 0;
      }
    }
  }
  
  .text-right {
    text-align: right;
  }
}
</style>
