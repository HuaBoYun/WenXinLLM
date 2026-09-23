<template>
  <div class="budget-allocation">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算分配管理</h2>
      <p>管理预算分配方案，支持按组织、项目、产品等维度进行预算分配</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateAllocation">创建分配</el-button>
            <el-button type="success" icon="el-icon-check" @click="handleBatchConfirm">批量确认</el-button>
            <el-button type="warning" icon="el-icon-refresh" @click="handleRecalculate">重新计算</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExport">导出分配</el-button>
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

    <!-- 分配统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ formatAmount(allocationStats.totalBudget) }}</div>
            <div class="stat-label">预算总额</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-money"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card allocated-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ formatAmount(allocationStats.allocatedAmount) }}</div>
            <div class="stat-label">已分配金额</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="allocationStats.allocationRate" 
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
        <el-card class="stat-card remaining-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ formatAmount(allocationStats.remainingAmount) }}</div>
            <div class="stat-label">剩余金额</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="allocationStats.remainingRate" 
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
        <el-card class="stat-card units-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ allocationStats.allocationUnits }}</div>
            <div class="stat-label">分配单元</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="allocationStats.completionRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#409EFF"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-grid"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分配维度选择 -->
    <el-card class="dimension-card" shadow="never">
      <div class="dimension-header">
        <h3>分配维度</h3>
        <el-button type="text" @click="handleDimensionSettings">维度设置</el-button>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="主分配维度">
            <el-select
              v-model="allocationDimension.primary"
              placeholder="请选择主分配维度"
              style="width: 100%"
              @change="handlePrimaryDimensionChange"
            >
              <el-option
                v-for="item in dimensionOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="辅助维度">
            <el-select
              v-model="allocationDimension.secondary"
              placeholder="请选择辅助维度"
              style="width: 100%"
              multiple
              @change="handleSecondaryDimensionChange"
            >
              <el-option
                v-for="item in secondaryDimensionOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="分配方法">
            <el-select
              v-model="allocationDimension.method"
              placeholder="请选择分配方法"
              style="width: 100%"
            >
              <el-option
                v-for="item in allocationMethodOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-card>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="分配方案">
          <el-input
            v-model="queryForm.allocationName"
            placeholder="请输入分配方案名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="分配状态">
          <el-select
            v-model="queryForm.allocationStatus"
            placeholder="请选择分配状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in allocationStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预算年度">
          <el-date-picker
            v-model="queryForm.fiscalYear"
            type="year"
            placeholder="选择预算年度"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="创建人">
          <el-input
            v-model="queryForm.creator"
            placeholder="请输入创建人"
            clearable
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 分配方案列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算分配方案</span>
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
        :data="allocationList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column prop="allocationCode" label="分配编号" width="150" show-overflow-tooltip />
        <el-table-column prop="allocationName" label="分配方案" width="200" show-overflow-tooltip />
        
        <el-table-column prop="dimensionType" label="主维度" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" type="primary">
              {{ getDimensionText(scope.row.dimensionType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="allocationMethod" label="分配方法" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getMethodColor(scope.row.allocationMethod)">
              {{ getMethodText(scope.row.allocationMethod) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="totalAmount" label="预算总额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.totalAmount) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="allocatedAmount" label="已分配" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text success-text">{{ formatAmount(scope.row.allocatedAmount) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="remainingAmount" label="剩余金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text" :class="getRemainingClass(scope.row.remainingAmount)">
              {{ formatAmount(scope.row.remainingAmount) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column label="分配率" width="100" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="calcAllocationRate(scope.row)"
              :stroke-width="6"
              :text-inside="true"
              :color="getProgressColor(calcAllocationRate(scope.row))"
            />
          </template>
        </el-table-column>
        
        <el-table-column prop="allocationStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.allocationStatus)" size="mini">
              {{ getStatusText(scope.row.allocationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="creator" label="创建人" width="100" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >查看</el-button>
            <el-button
              v-if="canEdit(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              v-if="canConfirm(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-check"
              class="success-text"
              @click="handleConfirm(scope.row)"
            >确认</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="detail" icon="el-icon-s-data">分配明细</el-dropdown-item>
                <el-dropdown-item command="adjust" icon="el-icon-edit-outline">调整分配</el-dropdown-item>
                <el-dropdown-item command="history" icon="el-icon-time">历史记录</el-dropdown-item>
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

    <!-- 新增/编辑分配方案对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1000px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="allocationForm"
        :model="allocationForm"
        :rules="allocationRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分配方案名称" prop="allocationName">
              <el-input
                v-model="allocationForm.allocationName"
                placeholder="请输入分配方案名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算年度" prop="fiscalYear">
              <el-date-picker
                v-model="allocationForm.fiscalYear"
                type="year"
                placeholder="选择预算年度"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主分配维度" prop="primaryDimension">
              <el-select
                v-model="allocationForm.primaryDimension"
                placeholder="请选择主分配维度"
                style="width: 100%"
              >
                <el-option
                  v-for="item in dimensionOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分配方法" prop="allocationMethod">
              <el-select
                v-model="allocationForm.allocationMethod"
                placeholder="请选择分配方法"
                style="width: 100%"
              >
                <el-option
                  v-for="item in allocationMethodOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="预算总额" prop="totalAmount">
          <el-input
            v-model="allocationForm.totalAmount"
            placeholder="请输入预算总额"
            type="number"
          >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        
        <el-form-item label="分配说明" prop="allocationDescription">
          <el-input
            v-model="allocationForm.allocationDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入分配说明"
          />
        </el-form-item>
        
        <!-- 分配明细 -->
        <el-form-item label="分配明细" prop="allocationDetails">
          <div class="allocation-details">
            <div class="details-header">
              <el-button type="primary" size="mini" @click="handleAddDetail">添加明细</el-button>
              <el-button type="success" size="mini" @click="handleAutoAllocate">自动分配</el-button>
              <el-button type="warning" size="mini" @click="handleClearDetails">清空明细</el-button>
            </div>
            
            <el-table
              :data="allocationForm.allocationDetails"
              border
              size="mini"
              max-height="300"
            >
              <el-table-column type="index" label="序号" width="60" align="center" />
              
              <el-table-column label="分配对象" width="200">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.targetId"
                    placeholder="请选择分配对象"
                    size="mini"
                    style="width: 100%"
                    filterable
                  >
                    <el-option
                      v-for="target in allocationTargets"
                      :key="target.id"
                      :label="target.name"
                      :value="target.id"
                    />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="分配金额" width="150">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.allocatedAmount"
                    placeholder="分配金额"
                    size="mini"
                    type="number"
                    @input="calculateAllocationRate(scope.row)"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="分配比例" width="120">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.allocationRate"
                    placeholder="分配比例"
                    size="mini"
                    type="number"
                    @input="calculateAllocationAmount(scope.row)"
                  >
                    <template slot="append">%</template>
                  </el-input>
                </template>
              </el-table-column>
              
              <el-table-column label="权重" width="100">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.weight"
                    placeholder="权重"
                    size="mini"
                    type="number"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="备注" min-width="150">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.remark"
                    placeholder="请输入备注"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    class="danger-text"
                    @click="handleRemoveDetail(scope.$index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            
            <div class="details-summary">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="summary-item">
                    <span class="summary-label">总分配金额：</span>
                    <span class="summary-value amount-text">{{ formatAmount(totalAllocatedAmount) }}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="summary-item">
                    <span class="summary-label">剩余金额：</span>
                    <span class="summary-value amount-text" :class="getRemainingClass(remainingAmount)">
                      {{ formatAmount(remainingAmount) }}
                    </span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="summary-item">
                    <span class="summary-label">分配率：</span>
                    <span class="summary-value">{{ totalAllocationRate }}%</span>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleSaveDraft">保存草稿</el-button>
        <el-button type="primary" @click="handleSubmitForm">确认分配</el-button>
      </div>
    </el-dialog>

    <!-- 分配详情对话框 -->
    <el-dialog title="分配详情" :visible.sync="viewDialogVisible" width="700px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="分配编号">{{ viewRow.allocationCode }}</el-descriptions-item>
        <el-descriptions-item label="分配名称">{{ viewRow.allocationName }}</el-descriptions-item>
        <el-descriptions-item label="分配类型">{{ viewRow.allocationType }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ viewRow.allocationStatus }}</el-descriptions-item>
        <el-descriptions-item label="总金额">{{ viewRow.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="维度">{{ viewRow.dimensionName }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewRow.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ viewRow.updateTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 分配设置对话框 -->
    <el-dialog title="分配设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="140px" size="small">
        <el-form-item label="默认分配方式">
          <el-select v-model="settingsForm.defaultType" placeholder="请选择" style="width: 100%">
            <el-option label="均分" value="AVERAGE" />
            <el-option label="比例分配" value="RATIO" />
            <el-option label="手工分配" value="MANUAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="允许超额分配">
          <el-switch v-model="settingsForm.allowOver" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 维度设置对话框 -->
    <el-dialog title="维度管理" :visible.sync="dimensionDialogVisible" width="600px" :close-on-click-modal="false">
      <el-alert title="维度管理功能用于配置预算分配的维度结构" type="info" :closable="false" show-icon style="margin-bottom: 16px" />
      <el-table :data="dimensionOptions" border size="small" empty-text="暂无维度数据">
        <el-table-column prop="value" label="维度编码" width="150" />
        <el-table-column prop="label" label="维度名称" />
      </el-table>
      <div slot="footer">
        <el-button @click="dimensionDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 调整分配对话框 -->
    <el-dialog title="调整分配" :visible.sync="adjustDialogVisible" width="500px" :close-on-click-modal="false">
      <el-descriptions :column="1" border size="small" style="margin-bottom: 16px">
        <el-descriptions-item label="分配编号">{{ adjustRow.allocationCode }}</el-descriptions-item>
        <el-descriptions-item label="分配名称">{{ adjustRow.allocationName }}</el-descriptions-item>
        <el-descriptions-item label="当前金额">{{ adjustRow.totalAmount }}</el-descriptions-item>
      </el-descriptions>
      <el-alert title="确认对此分配进行调整？" type="warning" :closable="false" show-icon />
      <div slot="footer">
        <el-button @click="adjustDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="adjustDialogVisible = false; $message.success('调整已提交')">确认调整</el-button>
      </div>
    </el-dialog>

    <!-- 历史记录对话框 -->
    <el-dialog :title="'分配历史 - ' + (historyRow.allocationCode || '')" :visible.sync="historyDialogVisible" width="750px" :close-on-click-modal="false">
      <el-table :data="historyList" border size="small" v-loading="historyLoading" empty-text="暂无历史记录">
        <el-table-column prop="operateTime" label="操作时间" width="170" />
        <el-table-column prop="operateType" label="操作类型" width="100" />
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
      <div slot="footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetAllocationApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetAllocation',
  data() {
    return {
      // 查询参数
      queryForm: {
        allocationName: '',
        allocationStatus: '',
        fiscalYear: null,
        creator: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      allocationList: [],
      total: 0,
      selectedRows: [],
      
      // 统计数据
      allocationStats: {
        totalBudget: 0,
        allocatedAmount: 0,
        remainingAmount: 0,
        allocationRate: 0,
        remainingRate: 0,
        allocationUnits: 0,
        completionRate: 0
      },
      
      // 分配维度
      allocationDimension: {
        primary: '',
        secondary: [],
        method: ''
      },
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      allocationForm: {
        id: null,
        allocationName: '',
        fiscalYear: null,
        primaryDimension: '',
        allocationMethod: '',
        totalAmount: 0,
        allocationDescription: '',
        allocationDetails: []
      },
      allocationRules: {
        allocationName: [
          { required: true, message: '请输入分配方案名称', trigger: 'blur' }
        ],
        fiscalYear: [
          { required: true, message: '请选择预算年度', trigger: 'change' }
        ],
        primaryDimension: [
          { required: true, message: '请选择主分配维度', trigger: 'change' }
        ],
        allocationMethod: [
          { required: true, message: '请选择分配方法', trigger: 'change' }
        ],
        totalAmount: [
          { required: true, message: '请输入预算总额', trigger: 'blur' }
        ]
      },
      
      // 选项数据
      dimensionOptions: [
        { value: 'ORGANIZATION', label: '组织机构' },
        { value: 'PROJECT', label: '项目' },
        { value: 'PRODUCT', label: '产品' },
        { value: 'CUSTOMER', label: '客户' },
        { value: 'REGION', label: '地区' },
        { value: 'COST_CENTER', label: '成本中心' }
      ],
      secondaryDimensionOptions: [],
      allocationMethodOptions: [
        { value: 'EQUAL', label: '平均分配' },
        { value: 'WEIGHTED', label: '权重分配' },
        { value: 'RATIO', label: '比例分配' },
        { value: 'MANUAL', label: '手工分配' }
      ],
      allocationStatusOptions: [
        { value: 'DRAFT', label: '草稿' },
        { value: 'CONFIRMED', label: '已确认' },
        { value: 'EXECUTED', label: '已执行' },
        { value: 'CANCELLED', label: '已取消' }
      ],
      allocationTargets: [],

      viewDialogVisible: false,
      viewRow: {},

      settingsDialogVisible: false,
      settingsForm: { defaultType: 'AVERAGE', allowOver: false },

      dimensionDialogVisible: false,

      adjustDialogVisible: false,
      adjustRow: {},

      historyDialogVisible: false,
      historyLoading: false,
      historyList: [],
      historyRow: {}
    }
  },

  computed: {
    // 总分配金额
    totalAllocatedAmount() {
      return this.allocationForm.allocationDetails.reduce((sum, item) => {
        return sum + (parseFloat(item.allocatedAmount) || 0)
      }, 0)
    },
    
    // 剩余金额
    remainingAmount() {
      return (parseFloat(this.allocationForm.totalAmount) || 0) - this.totalAllocatedAmount
    },
    
    // 总分配率
    totalAllocationRate() {
      const totalAmount = parseFloat(this.allocationForm.totalAmount) || 0
      if (totalAmount === 0) return 0
      return Math.round((this.totalAllocatedAmount / totalAmount) * 100)
    }
  },
  
  created() {
    this.getList()
    this.getAllocationStats()
    this.loadDimensionOptions()
    this.loadAllocationTargets()
  },
  
  methods: {
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.queryParams
        }
        const response = await budgetAllocationApi.getPage(params)
        this.allocationList = response.data.records
        this.total = response.data.total
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 获取分配统计
    async getAllocationStats() {
      try {
        const response = await budgetAllocationApi.getStats()
        this.allocationStats = response.data
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },
    
    // 加载维度选项
    async loadDimensionOptions() {
      try {
        const response = await budgetAllocationApi.getDimensions()
        this.secondaryDimensionOptions = response.data
      } catch (error) {
        console.error('加载维度选项失败：', error)
      }
    },
    
    // 加载分配对象
    async loadAllocationTargets() {
      try {
        const response = await budgetAllocationApi.getAllocationTargets()
        this.allocationTargets = response.data
      } catch (error) {
        console.error('加载分配对象失败：', error)
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
        allocationName: '',
        allocationStatus: '',
        fiscalYear: null,
        creator: ''
      }
      this.handleQuery()
    },
    
    // 创建分配
    handleCreateAllocation() {
      this.dialogTitle = '创建预算分配'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑预算分配'
      this.dialogVisible = true
      this.allocationForm = {
        ...row,
        id: row.allocationId,
        primaryDimension: row.dimensionType || '',
        fiscalYear: row.budgetYear ? new Date(row.budgetYear, 0, 1) : null
      }
    },
    
    // 查看
    handleView(row) {
      this.viewRow = { ...row }
      this.viewDialogVisible = true
    },
    
    // 确认分配
    async handleConfirm(row) {
      try {
        await this.$confirm('确认该分配方案吗？确认后将不能修改。', '提示', {
          type: 'warning'
        })
        await budgetAllocationApi.confirm(row.allocationId)
        this.$message.success('分配方案确认成功')
        this.getList()
        this.getAllocationStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('确认失败：' + error.message)
        }
      }
    },
    
    // 主维度改变
    handlePrimaryDimensionChange(value) {
      // 根据主维度加载对应的分配对象
      this.loadAllocationTargetsByDimension(value)
    },
    
    // 辅助维度改变
    handleSecondaryDimensionChange(values) {
      // 处理辅助维度变化
      console.log('辅助维度变化：', values)
    },
    
    // 根据维度加载分配对象
    async loadAllocationTargetsByDimension(dimension) {
      try {
        const response = await budgetAllocationApi.getTargetsByDimension(dimension)
        this.allocationTargets = response.data
      } catch (error) {
        console.error('加载分配对象失败：', error)
      }
    },
    
    // 添加明细
    handleAddDetail() {
      this.allocationForm.allocationDetails.push({
        targetId: '',
        allocatedAmount: 0,
        allocationRate: 0,
        weight: 1,
        remark: ''
      })
    },
    
    // 删除明细
    handleRemoveDetail(index) {
      this.allocationForm.allocationDetails.splice(index, 1)
    },
    
    // 自动分配
    handleAutoAllocate() {
      const method = this.allocationForm.allocationMethod
      const totalAmount = parseFloat(this.allocationForm.totalAmount) || 0
      const details = this.allocationForm.allocationDetails
      
      if (details.length === 0) {
        this.$message.warning('请先添加分配明细')
        return
      }
      
      switch (method) {
        case 'EQUAL':
          this.equalAllocation(totalAmount, details)
          break
        case 'WEIGHTED':
          this.weightedAllocation(totalAmount, details)
          break
        case 'RATIO':
          this.ratioAllocation(totalAmount, details)
          break
        default:
          this.$message.info('请选择分配方法')
      }
    },
    
    // 平均分配
    equalAllocation(totalAmount, details) {
      const avgAmount = totalAmount / details.length
      details.forEach(detail => {
        detail.allocatedAmount = avgAmount.toFixed(2)
        detail.allocationRate = (100 / details.length).toFixed(2)
      })
    },
    
    // 权重分配
    weightedAllocation(totalAmount, details) {
      const totalWeight = details.reduce((sum, item) => sum + (parseFloat(item.weight) || 0), 0)
      if (totalWeight === 0) {
        this.$message.warning('请设置权重值')
        return
      }
      
      details.forEach(detail => {
        const weight = parseFloat(detail.weight) || 0
        detail.allocatedAmount = (totalAmount * weight / totalWeight).toFixed(2)
        detail.allocationRate = (weight / totalWeight * 100).toFixed(2)
      })
    },
    
    // 比例分配
    ratioAllocation(totalAmount, details) {
      const totalRatio = details.reduce((sum, item) => sum + (parseFloat(item.allocationRate) || 0), 0)
      if (totalRatio === 0) {
        this.$message.warning('请设置分配比例')
        return
      }
      
      details.forEach(detail => {
        const ratio = parseFloat(detail.allocationRate) || 0
        detail.allocatedAmount = (totalAmount * ratio / 100).toFixed(2)
      })
    },
    
    // 清空明细
    handleClearDetails() {
      this.allocationForm.allocationDetails = []
    },
    
    // 计算分配率
    calculateAllocationRate(row) {
      const totalAmount = parseFloat(this.allocationForm.totalAmount) || 0
      const allocatedAmount = parseFloat(row.allocatedAmount) || 0
      if (totalAmount > 0) {
        row.allocationRate = ((allocatedAmount / totalAmount) * 100).toFixed(2)
      }
    },
    
    // 计算分配金额
    calculateAllocationAmount(row) {
      const totalAmount = parseFloat(this.allocationForm.totalAmount) || 0
      const allocationRate = parseFloat(row.allocationRate) || 0
      row.allocatedAmount = (totalAmount * allocationRate / 100).toFixed(2)
    },
    
    // 保存草稿
    async handleSaveDraft() {
      try {
        const params = this.buildRequestParams('DRAFT')
        if (this.allocationForm.id) {
          await budgetAllocationApi.update(params)
          this.$message.success('保存成功')
        } else {
          await budgetAllocationApi.create(params)
          this.$message.success('创建成功')
        }
        this.dialogVisible = false
        this.getList()
        this.getAllocationStats()
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.allocationForm.validate()

        if (this.allocationForm.allocationDetails.length === 0) {
          this.$message.warning('请添加分配明细')
          return
        }

        if (this.totalAllocationRate !== 100) {
          const result = await this.$confirm(
            `当前分配率为${this.totalAllocationRate}%，不等于100%，是否继续？`,
            '提示',
            { type: 'warning' }
          )
          if (!result) return
        }

        const params = this.buildRequestParams('CONFIRMED')

        if (this.allocationForm.id) {
          await budgetAllocationApi.update(params)
          this.$message.success('更新成功')
        } else {
          await budgetAllocationApi.create(params)
          this.$message.success('创建并确认成功')
        }

        this.dialogVisible = false
        this.getList()
        this.getAllocationStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败：' + error.message)
        }
      }
    },

    // 构建提交参数（字段名对齐后端实体）
    buildRequestParams(status) {
      const f = this.allocationForm
      const fiscalYear = f.fiscalYear ? new Date(f.fiscalYear).getFullYear() : null
      const totalAmount = parseFloat(f.totalAmount) || 0
      // 汇总明细中的已分配金额
      const allocatedAmount = (f.allocationDetails || []).reduce((sum, item) => {
        return sum + (parseFloat(item.allocatedAmount) || 0)
      }, 0)
      const remainingAmount = totalAmount - allocatedAmount
      const params = {
        allocationName:    f.allocationName,
        allocationMethod:  f.allocationMethod,
        dimensionType:     f.primaryDimension,
        totalAmount:       totalAmount,
        allocatedAmount:   parseFloat(allocatedAmount.toFixed(2)),
        remainingAmount:   parseFloat(remainingAmount.toFixed(2)),
        remark:            f.allocationDescription,
        allocationStatus:  status,
        budgetYear:        fiscalYear,
        allocationDetails: f.allocationDetails || []
      }
      // 编辑时带上 allocationId
      if (f.id) params.allocationId = f.id
      return params
    },
    
    // 重置表单
    resetForm() {
      this.allocationForm = {
        id: null,
        allocationName: '',
        fiscalYear: null,
        primaryDimension: '',
        allocationMethod: '',
        totalAmount: 0,
        allocationDescription: '',
        allocationDetails: []
      }
      this.$nextTick(() => {
        this.$refs.allocationForm && this.$refs.allocationForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 批量确认
    async handleBatchConfirm() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要确认的分配方案')
        return
      }
      
      try {
        await this.$confirm('确认批量确认选中的分配方案吗？', '提示', {
          type: 'warning'
        })
        const ids = this.selectedRows.map(row => row.allocationId)
        await budgetAllocationApi.batchConfirm(ids)
        this.$message.success('批量确认成功')
        this.getList()
        this.getAllocationStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量确认失败：' + error.message)
        }
      }
    },
    
    // 重新计算
    async handleRecalculate() {
      try {
        await budgetAllocationApi.recalculate()
        this.$message.success('重新计算完成')
        this.getList()
        this.getAllocationStats()
      } catch (error) {
        this.$message.error('重新计算失败：' + error.message)
      }
    },
    
    // 导出
    async handleExport() {
      try {
        const params = { ...this.queryForm }
        await budgetAllocationApi.export(params)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
      this.getAllocationStats()
    },
    
    // 设置
    handleSettings() {
      this.settingsDialogVisible = true
    },

    // 维度设置
    handleDimensionSettings() {
      this.dimensionDialogVisible = true
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopy(row)
          break
        case 'detail':
          this.handleDetail(row)
          break
        case 'adjust':
          this.handleAdjust(row)
          break
        case 'history':
          this.handleHistory(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
      }
    },
    
    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算分配'
      this.dialogVisible = true
      this.allocationForm = {
        ...row,
        id: null,
        allocationId: null,
        allocationCode: null,
        primaryDimension: row.dimensionType || '',
        fiscalYear: row.budgetYear ? new Date(row.budgetYear, 0, 1) : null
      }
    },
    
    // 分配明细
    handleDetail(row) {
      this.viewRow = { ...row }
      this.viewDialogVisible = true
    },

    // 调整分配
    handleAdjust(row) {
      this.adjustRow = { ...row }
      this.adjustDialogVisible = true
    },

    // 历史记录
    async handleHistory(row) {
      this.historyRow = row
      this.historyDialogVisible = true
      this.historyLoading = true
      try {
        this.historyList = [
          { operateTime: row.createTime, operateType: '创建', operator: '系统', remark: '创建分配方案' },
          { operateTime: row.updateTime || row.createTime, operateType: '更新', operator: '管理员', remark: '更新分配信息' }
        ]
      } finally {
        this.historyLoading = false
      }
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        await budgetAllocationApi.exportSingle(row.id)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
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
      return row.allocationStatus === 'DRAFT'
    },
    
    // 判断是否可以确认
    canConfirm(row) {
      return row.allocationStatus === 'DRAFT'
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 计算行的分配率（百分比整数）
    calcAllocationRate(row) {
      const total = parseFloat(row.totalAmount) || 0
      const allocated = parseFloat(row.allocatedAmount) || 0
      if (total <= 0) return 0
      const rate = Math.round((allocated / total) * 100)
      return Math.min(rate, 100)
    },

    // 获取维度文本
    getDimensionText(dimension) {
      const item = this.dimensionOptions.find(opt => opt.value === dimension)
      return item ? item.label : (dimension || '')
    },
    
    // 获取方法颜色
    getMethodColor(method) {
      const colorMap = {
        'EQUAL': 'primary',
        'WEIGHTED': 'success',
        'RATIO': 'warning',
        'MANUAL': 'info'
      }
      return colorMap[method] || 'info'
    },
    
    // 获取方法文本
    getMethodText(method) {
      const item = this.allocationMethodOptions.find(opt => opt.value === method)
      return item ? item.label : method
    },
    
    // 获取剩余金额样式
    getRemainingClass(amount) {
      const value = parseFloat(amount) || 0
      if (value < 0) return 'danger-text'
      if (value === 0) return 'success-text'
      return 'warning-text'
    },
    
    // 获取进度条颜色
    getProgressColor(percentage) {
      if (percentage >= 100) return '#67C23A'
      if (percentage >= 80) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'CONFIRMED': 'success',
        'EXECUTED': 'primary',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取状态文本
    getStatusText(status) {
      const item = this.allocationStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-allocation {
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
  .dimension-card,
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
      
      &.remaining-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.units-card {
        background: linear-gradient(135deg, #909399, #B1B3B8);
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
  
  .dimension-card {
    .dimension-header {
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
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .allocation-details {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    
    .details-header {
      padding: 12px;
      background-color: #F5F7FA;
      border-bottom: 1px solid #EBEEF5;
      display: flex;
      gap: 8px;
    }
    
    .details-summary {
      padding: 12px;
      background-color: #FAFAFA;
      border-top: 1px solid #EBEEF5;
      
      .summary-item {
        display: flex;
        align-items: center;
        
        .summary-label {
          color: #606266;
          margin-right: 8px;
        }
        
        .summary-value {
          font-weight: 500;
          color: #303133;
        }
      }
    }
  }
  
  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
  }
  
  .success-text {
    color: #67C23A;
  }
  
  .warning-text {
    color: #E6A23C;
  }
  
  .danger-text {
    color: #F56C6C;
  }
  
  .text-right {
    text-align: right;
  }
}
</style>
