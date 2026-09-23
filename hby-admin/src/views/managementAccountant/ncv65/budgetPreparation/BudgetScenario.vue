<template>
  <div class="budget-scenario">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算场景管理</h2>
      <p>管理预算场景设置，支持场景创建、对比分析、假设分析和场景审批</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateScenario">创建场景</el-button>
            <el-button type="success" icon="el-icon-copy-document" @click="handleCopyScenario">复制场景</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete">删除场景</el-button>
            <el-button type="warning" icon="el-icon-s-data" @click="handleCompareScenario">场景对比</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportScenario">导出场景</el-button>
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

    <!-- 场景统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ scenarioStats.totalScenarios }}</div>
            <div class="stat-label">场景总数</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-marketing"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ scenarioStats.activeScenarios }}</div>
            <div class="stat-label">活跃场景</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="scenarioStats.activeRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#67C23A"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-video-play"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card approved-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ scenarioStats.approvedScenarios }}</div>
            <div class="stat-label">已审批</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="scenarioStats.approvedRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#409EFF"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-check"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card baseline-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ scenarioStats.baselineScenarios }}</div>
            <div class="stat-label">基准场景</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="scenarioStats.baselineRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#E6A23C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-star-on"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="场景名称">
          <el-input
            v-model="queryForm.scenarioName"
            placeholder="请输入场景名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="场景类型">
          <el-select
            v-model="queryForm.scenarioType"
            placeholder="请选择场景类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in scenarioTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="场景状态">
          <el-select
            v-model="queryForm.scenarioStatus"
            placeholder="请选择场景状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in scenarioStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
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

    <!-- 场景列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算场景列表</span>
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
        :data="scenarioList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column v-if="visibleColumns.scenarioCode" prop="scenarioCode" label="场景编码" width="150" show-overflow-tooltip />
        <el-table-column v-if="visibleColumns.scenarioName" prop="scenarioName" label="场景名称" width="200" show-overflow-tooltip />
        
        <el-table-column v-if="visibleColumns.scenarioType" prop="scenarioType" label="场景类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getScenarioTypeColor(scope.row.scenarioType)">
              {{ getScenarioTypeText(scope.row.scenarioType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="visibleColumns.isBaseline" prop="isBaseline" label="基准场景" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isBaseline || scope.row.isDefault || isBaselineScenarioType(scope.row.scenarioType)" type="warning" size="mini">
              <i class="el-icon-star-on"></i> 基准
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        
        <el-table-column v-if="visibleColumns.budgetAmount" prop="budgetAmount" label="预算金额" width="150" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.budgetAmount) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column v-if="visibleColumns.varianceAmount" prop="varianceAmount" label="差异金额" width="150" align="right" sortable="custom">
          <template slot-scope="scope">
            <span :class="getVarianceClass(scope.row.varianceAmount)">
              {{ formatAmount(scope.row.varianceAmount) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column v-if="visibleColumns.varianceRate" prop="varianceRate" label="差异率" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <div>
              <el-progress
                :percentage="Math.abs(Number(scope.row.varianceRate || 0))"
                :stroke-width="6"
                :text-inside="true"
                :color="getVarianceRateColor(scope.row.varianceRate)"
              />
              <div class="variance-rate-text">{{ formatRate(scope.row.varianceRate) }}</div>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column v-if="visibleColumns.scenarioStatus" prop="scenarioStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getScenarioStatusType(scope.row.scenarioStatus)" size="mini">
              {{ getScenarioStatusText(scope.row.scenarioStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="visibleColumns.creator" prop="creator" label="创建人" width="100" show-overflow-tooltip />
        <el-table-column v-if="visibleColumns.createTime" prop="createTime" label="创建时间" width="150" align="center" />
        
        <el-table-column label="操作" width="290" align="center" fixed="right">
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
              v-if="canAnalyze(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-s-data"
              class="primary-text"
              @click.stop="handleAnalyze(scope.row)"
            >分析</el-button>
            <el-button
              v-if="!isScenarioBaseline(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-star-off"
              class="warning-text"
              @click.stop="handleSetBaseline(scope.row)"
            >设基准</el-button>
            <el-button
              v-else
              type="text"
              size="mini"
              icon="el-icon-star-on"
              class="baseline-text"
              disabled
            >已基准</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
              @click.native.stop
            >
              <el-button type="text" size="mini" @click.stop>
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="compare" icon="el-icon-s-data">场景对比</el-dropdown-item>
                <el-dropdown-item command="approve" icon="el-icon-check">提交审批</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
                <el-dropdown-item command="delete" icon="el-icon-delete">删除</el-dropdown-item>
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

    <!-- 新增/编辑场景对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="scenarioForm"
        :model="scenarioForm"
        :rules="scenarioRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="场景名称" prop="scenarioName">
              <el-input
                v-model="scenarioForm.scenarioName"
                placeholder="请输入场景名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="场景编码" prop="scenarioCode">
              <el-input
                v-model="scenarioForm.scenarioCode"
                placeholder="请输入场景编码"
                :disabled="!!scenarioForm.id"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="场景类型" prop="scenarioType">
              <el-select
                v-model="scenarioForm.scenarioType"
                placeholder="请选择场景类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in scenarioTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="基准场景" prop="baselineScenarioId">
              <el-select
                v-model="scenarioForm.baselineScenarioId"
                placeholder="请选择基准场景"
                style="width: 100%"
                clearable
              >
                <el-option
                  v-for="scenario in baselineScenarios"
                  :key="scenario.id"
                  :label="scenario.scenarioName"
                  :value="scenario.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="场景描述" prop="scenarioDescription">
          <el-input
            v-model="scenarioForm.scenarioDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入场景描述"
          />
        </el-form-item>
        
        <!-- 假设条件配置 -->
        <el-form-item label="假设条件" prop="assumptions">
          <div class="assumptions-config">
            <div class="assumptions-header">
              <el-button type="primary" size="mini" @click="handleAddAssumption">添加假设</el-button>
              <el-button type="success" size="mini" @click="handleImportAssumptions">导入假设</el-button>
            </div>
            
            <el-table
              :data="scenarioForm.assumptions"
              border
              size="mini"
              max-height="300"
            >
              <el-table-column type="index" label="序号" width="60" align="center" />
              
              <el-table-column label="假设名称" width="150">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.assumptionName"
                    placeholder="假设名称"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="假设类型" width="120">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.assumptionType"
                    placeholder="假设类型"
                    size="mini"
                    style="width: 100%"
                  >
                    <el-option value="PERCENTAGE" label="百分比" />
                    <el-option value="AMOUNT" label="金额" />
                    <el-option value="RATIO" label="比率" />
                    <el-option value="FACTOR" label="系数" />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="假设值" width="120">
                <template slot-scope="scope">
                  <el-input-number
                    v-model="scope.row.assumptionValue"
                    :precision="2"
                    size="mini"
                    style="width: 100%"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="影响科目" min-width="150">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.affectedAccounts"
                    placeholder="影响科目"
                    size="mini"
                    style="width: 100%"
                    multiple
                  >
                    <el-option
                      v-for="account in accountOptions"
                      :key="account.value"
                      :label="account.label"
                      :value="account.value"
                    />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    class="danger-text"
                    @click="handleRemoveAssumption(scope.$index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form-item>
        
        <el-form-item label="场景配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="scenarioForm.isBaseline">设为基准场景</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="scenarioForm.autoCalculate">自动计算</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="scenarioForm.allowEdit">允许编辑</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleCalculateScenario">计算场景</el-button>
        <el-button type="primary" @click="handleSubmitForm">保存场景</el-button>
      </div>
    </el-dialog>

    <!-- 场景详情对话框 -->
    <el-dialog
      title="场景详情"
      :visible.sync="detailDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div v-if="currentScenarioDetail" class="scenario-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="场景名称">{{ currentScenarioDetail.scenarioName }}</el-descriptions-item>
          <el-descriptions-item label="场景编码">{{ currentScenarioDetail.scenarioCode }}</el-descriptions-item>
          <el-descriptions-item label="场景类型">{{ getScenarioTypeText(currentScenarioDetail.scenarioType) }}</el-descriptions-item>
          <el-descriptions-item label="场景状态">
            <el-tag :type="getScenarioStatusType(currentScenarioDetail.scenarioStatus)">
              {{ getScenarioStatusText(currentScenarioDetail.scenarioStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建人">{{ currentScenarioDetail.creator || currentScenarioDetail.createBy || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentScenarioDetail.createTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="预算金额">{{ formatAmount(currentScenarioDetail.budgetAmount) }}</el-descriptions-item>
          <el-descriptions-item label="差异金额">{{ formatAmount(currentScenarioDetail.varianceAmount) }}</el-descriptions-item>
          <el-descriptions-item label="差异率">{{ currentScenarioDetail.varianceRate || 0 }}%</el-descriptions-item>
          <el-descriptions-item label="基准场景">{{ currentScenarioDetail.isBaseline ? '是' : '否' }}</el-descriptions-item>
          <el-descriptions-item label="场景描述" :span="2">{{ currentScenarioDetail.scenarioDescription || '-' }}</el-descriptions-item>
        </el-descriptions>

        <el-card class="detail-assumption-card" shadow="never">
          <div slot="header">假设条件</div>
          <el-table :data="detailAssumptions" border size="mini" empty-text="暂无假设条件">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="assumptionName" label="假设名称" min-width="160" />
            <el-table-column prop="assumptionType" label="假设类型" width="120" />
            <el-table-column prop="assumptionValue" label="假设值" width="120" align="right" />
            <el-table-column label="影响科目" min-width="180">
              <template slot-scope="scope">
                {{ formatAffectedAccounts(scope.row.affectedAccounts) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 场景分析对话框 -->
    <el-dialog
      title="场景分析"
      :visible.sync="analysisDialogVisible"
      width="1000px"
      :close-on-click-modal="false"
    >
      <div v-if="currentScenarioAnalysis" class="scenario-analysis-dialog">
        <el-row :gutter="20" class="analysis-overview-row">
          <el-col :span="8">
            <el-card shadow="never" class="analysis-metric-card">
              <div class="metric-label">预算金额</div>
              <div class="metric-value">{{ formatAmount(currentScenarioAnalysis.budgetAmount) }}</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="never" class="analysis-metric-card">
              <div class="metric-label">差异金额</div>
              <div :class="['metric-value', getVarianceClass(currentScenarioAnalysis.varianceAmount)]">
                {{ formatAmount(currentScenarioAnalysis.varianceAmount) }}
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="never" class="analysis-metric-card">
              <div class="metric-label">差异率</div>
              <div class="metric-value">{{ currentScenarioAnalysis.varianceRate || 0 }}%</div>
            </el-card>
          </el-col>
        </el-row>

        <el-card shadow="never" class="analysis-info-card">
          <div slot="header">分析结论</div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="场景名称">{{ currentScenarioAnalysis.scenarioName }}</el-descriptions-item>
            <el-descriptions-item label="场景类型">{{ getScenarioTypeText(currentScenarioAnalysis.scenarioType) }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getScenarioStatusType(currentScenarioAnalysis.scenarioStatus)">
                {{ getScenarioStatusText(currentScenarioAnalysis.scenarioStatus) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="风险等级">
              <el-tag :type="getAnalysisRiskLevelType(currentScenarioAnalysis)">
                {{ getAnalysisRiskLevelText(currentScenarioAnalysis) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="分析说明" :span="2">
              {{ buildScenarioAnalysisSummary(currentScenarioAnalysis) }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <el-card shadow="never" class="analysis-assumption-card">
          <div slot="header">敏感因素</div>
          <el-table :data="analysisAssumptions" border size="mini" empty-text="暂无敏感因素">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="assumptionName" label="因素名称" min-width="180" />
            <el-table-column prop="assumptionType" label="因素类型" width="120" />
            <el-table-column prop="assumptionValue" label="变化值" width="120" align="right" />
            <el-table-column label="影响方向" width="120" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.assumptionValue >= 0 ? 'success' : 'danger'" size="mini">
                  {{ scope.row.assumptionValue >= 0 ? '正向' : '负向' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="analysisDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="列设置"
      :visible.sync="columnSettingDialogVisible"
      width="520px"
      :close-on-click-modal="false"
    >
      <el-checkbox-group v-model="checkedColumnKeys" class="column-setting-group">
        <el-checkbox v-for="column in columnOptions" :key="column.key" :label="column.key">
          {{ column.label }}
        </el-checkbox>
      </el-checkbox-group>
      <div slot="footer" class="dialog-footer">
        <el-button @click="columnSettingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveColumnSetting">确定</el-button>
      </div>
    </el-dialog>

    <!-- 场景对比对话框 -->
    <el-dialog
      title="场景对比分析"
      :visible.sync="compareDialogVisible"
      width="1200px"
      :close-on-click-modal="false"
    >
      <div class="scenario-compare">
        <div class="compare-header">
          <el-form :inline="true" size="small">
            <el-form-item label="对比场景">
              <el-select
                v-model="compareScenarios"
                placeholder="请选择对比场景"
                multiple
                style="width: 300px"
              >
                <el-option
                  v-for="scenario in scenarioList"
                  :key="scenario.id"
                  :label="scenario.scenarioName"
                  :value="scenario.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleExecuteCompare">执行对比</el-button>
            </el-form-item>
          </el-form>
        </div>
        
        <div class="compare-result" v-if="compareResult">
          <el-table :data="compareResult" border>
            <el-table-column prop="accountName" label="科目名称" width="200" />
            <el-table-column
              v-for="scenario in selectedScenarios"
              :key="scenario.id"
              :label="scenario.scenarioName"
              width="150"
              align="right"
            >
              <template slot-scope="scope">
                <span class="amount-text">{{ formatAmount(scope.row[scenario.id]) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="最大差异" width="150" align="right">
              <template slot-scope="scope">
                <span :class="getVarianceClass(scope.row.maxVariance)">
                  {{ formatAmount(scope.row.maxVariance) }}
                </span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="compareDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleExportCompare">导出对比</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="场景设置"
      :visible.sync="settingsDialogVisible"
      width="640px"
      :close-on-click-modal="false"
    >
      <el-form :model="settingsForm" label-width="170px" size="small">
        <el-form-item label="默认分页大小">
          <el-select v-model="settingsForm.defaultPageSize" style="width: 100%">
            <el-option v-for="size in [10, 20, 50, 100]" :key="size" :label="`${size} 条/页`" :value="size" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建场景默认自动计算">
          <el-switch v-model="settingsForm.defaultAutoCalculate" />
        </el-form-item>
        <el-form-item label="创建场景默认允许编辑">
          <el-switch v-model="settingsForm.defaultAllowEdit" />
        </el-form-item>
        <el-form-item label="启用行点击查看详情">
          <el-switch v-model="settingsForm.enableRowClickView" />
        </el-form-item>
        <el-form-item label="计算后自动刷新列表">
          <el-switch v-model="settingsForm.refreshListAfterCalculate" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button @click="handleResetSettings">恢复默认</el-button>
        <el-button type="primary" @click="handleSaveSettings">保存设置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetScenarioApi } from '@/api/managementAccountant/ncv65/budgetPreparation'
import { downloadByBlob } from '@/utils/download'

export default {
  name: 'BudgetScenario',
  data() {
    return {
      // 查询参数
      queryForm: {
        scenarioName: '',
        scenarioType: '',
        scenarioStatus: '',
        creator: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      scenarioList: [],
      total: 0,
      selectedRows: [],
      
      // 统计数据
      scenarioStats: {
        totalScenarios: 0,
        activeScenarios: 0,
        approvedScenarios: 0,
        baselineScenarios: 0,
        activeRate: 0,
        approvedRate: 0,
        baselineRate: 0
      },
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      detailDialogVisible: false,
      analysisDialogVisible: false,
      compareDialogVisible: false,
      settingsDialogVisible: false,
      columnSettingDialogVisible: false,
      currentScenarioDetail: null,
      currentScenarioAnalysis: null,
      settingsForm: {
        defaultPageSize: 20,
        defaultAutoCalculate: true,
        defaultAllowEdit: true,
        enableRowClickView: true,
        refreshListAfterCalculate: true
      },
      visibleColumns: {
        scenarioCode: true,
        scenarioName: true,
        scenarioType: true,
        isBaseline: true,
        budgetAmount: true,
        varianceAmount: true,
        varianceRate: true,
        scenarioStatus: true,
        creator: true,
        createTime: true
      },
      columnOptions: [
        { key: 'scenarioCode', label: '场景编码' },
        { key: 'scenarioName', label: '场景名称' },
        { key: 'scenarioType', label: '场景类型' },
        { key: 'isBaseline', label: '基准场景' },
        { key: 'budgetAmount', label: '预算金额' },
        { key: 'varianceAmount', label: '差异金额' },
        { key: 'varianceRate', label: '差异率' },
        { key: 'scenarioStatus', label: '状态' },
        { key: 'creator', label: '创建人' },
        { key: 'createTime', label: '创建时间' }
      ],
      checkedColumnKeys: ['scenarioCode', 'scenarioName', 'scenarioType', 'isBaseline', 'budgetAmount', 'varianceAmount', 'varianceRate', 'scenarioStatus', 'creator', 'createTime'],
      scenarioForm: {
        id: null,
        scenarioId: null,
        scenarioName: '',
        scenarioCode: '',
        scenarioType: '',
        baselineScenarioId: null,
        scenarioDescription: '',
        assumptions: [],
        isBaseline: false,
        autoCalculate: true,
        allowEdit: true
      },
      scenarioRules: {
        scenarioName: [
          { required: true, message: '请输入场景名称', trigger: 'blur' }
        ],
        scenarioCode: [
          { required: true, message: '请输入场景编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '场景编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        scenarioType: [
          { required: true, message: '请选择场景类型', trigger: 'change' }
        ]
      },
      
      // 场景对比
      compareScenarios: [],
      compareResult: null,
      selectedScenarios: [],
      
      // 基准场景和科目选项
      baselineScenarios: [],
      accountOptions: [],
      
      // 选项数据
      scenarioTypeOptions: [
        { value: 'BASELINE', label: '基准场景' },
        { value: 'OPTIMISTIC', label: '乐观场景' },
        { value: 'PESSIMISTIC', label: '悲观场景' },
        { value: 'REALISTIC', label: '现实场景' },
        { value: 'WHAT_IF', label: '假设场景' }
      ],
      scenarioStatusOptions: [
        { value: 'DRAFT', label: '草稿' },
        { value: 'ACTIVE', label: '活跃' },
        { value: 'APPROVED', label: '已审批' },
        { value: 'ARCHIVED', label: '已归档' }
      ]
    }
  },
  
  created() {
    this.applySettings()
    this.getList()
    this.loadBaselineScenarios()
    this.loadAccountOptions()
  },

  methods: {
    getDefaultScenarioForm() {
      return {
        id: null,
        scenarioId: null,
        scenarioName: '',
        scenarioCode: '',
        scenarioType: '',
        baselineScenarioId: null,
        scenarioDescription: '',
        assumptions: [],
        isBaseline: false,
        autoCalculate: this.settingsForm.defaultAutoCalculate,
        allowEdit: this.settingsForm.defaultAllowEdit
      }
    },

    applySettings() {
      this.queryParams.pageSize = this.settingsForm.defaultPageSize
    },

    handleSaveSettings() {
      this.applySettings()
      this.settingsDialogVisible = false
      this.$message.success('设置已保存')
    },

    handleResetSettings() {
      this.settingsForm = {
        defaultPageSize: 20,
        defaultAutoCalculate: true,
        defaultAllowEdit: true,
        enableRowClickView: true,
        refreshListAfterCalculate: true
      }
      this.applySettings()
      this.$message.success('已恢复默认设置')
    },
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.queryParams
        }
        const response = await budgetScenarioApi.getPage(params)
        const pageData = response.data || {}
        this.scenarioList = pageData.records || []
        this.total = pageData.total || 0
        this.scenarioStats = {
          ...this.scenarioStats,
          ...(pageData.stats || {})
        }
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载基准场景
    async loadBaselineScenarios() {
      try {
        const response = await budgetScenarioApi.getBaselineScenarios()
        this.baselineScenarios = response.data
      } catch (error) {
        console.error('加载基准场景失败：', error)
      }
    },
    
    // 加载科目选项
    async loadAccountOptions() {
      try {
        const response = await budgetScenarioApi.getAccountOptions()
        this.accountOptions = response.data
      } catch (error) {
        console.error('加载科目选项失败：', error)
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
        scenarioName: '',
        scenarioType: '',
        scenarioStatus: '',
        creator: ''
      }
      this.handleQuery()
    },
    
    // 创建场景
    handleCreateScenario() {
      this.dialogTitle = '创建预算场景'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑场景
    handleEdit(row) {
      this.dialogTitle = '编辑预算场景'
      this.dialogVisible = true
      this.scenarioForm = { ...row, id: row.id || row.scenarioId, scenarioId: row.scenarioId || row.id }
    },
    
    // 查看场景
    async handleView(row) {
      const scenarioId = row.scenarioId || row.id
      try {
        if (!scenarioId) {
          this.currentScenarioDetail = { ...row }
        } else {
          const response = await budgetScenarioApi.detail(scenarioId)
          this.currentScenarioDetail = {
            ...row,
            ...(response.data || {}),
            id: scenarioId,
            scenarioId
          }
        }
        this.detailDialogVisible = true
      } catch (error) {
        this.currentScenarioDetail = { ...row }
        this.detailDialogVisible = true
        this.$message.warning('场景详情加载失败，已展示列表数据')
      }
    },
    
    // 分析场景
    async handleAnalyze(row) {
      const scenarioId = row.scenarioId || row.id
      try {
        if (!scenarioId) {
          this.currentScenarioAnalysis = { ...row }
        } else {
          const response = await budgetScenarioApi.detail(scenarioId)
          this.currentScenarioAnalysis = {
            ...row,
            ...(response.data || {}),
            id: scenarioId,
            scenarioId
          }
        }
        this.analysisDialogVisible = true
      } catch (error) {
        this.currentScenarioAnalysis = { ...row }
        this.analysisDialogVisible = true
        this.$message.warning('场景详情加载失败，已展示列表数据')
      }
    },
    
    // 添加假设
    handleAddAssumption() {
      this.scenarioForm.assumptions.push({
        assumptionName: '',
        assumptionType: 'PERCENTAGE',
        assumptionValue: 0,
        affectedAccounts: []
      })
    },
    
    // 删除假设
    handleRemoveAssumption(index) {
      this.scenarioForm.assumptions.splice(index, 1)
    },
    
    // 导入假设
    handleImportAssumptions() {
      this.$message.info('假设导入功能开发中...')
    },
    
    // 计算场景
    async handleCalculateScenario() {
      if (this.scenarioForm.assumptions.length === 0) {
        this.$message.warning('请先添加假设条件')
        return
      }

      try {
        const params = {
          scenarioId: this.scenarioForm.scenarioId || this.scenarioForm.id,
          assumptions: this.scenarioForm.assumptions
        }
        const response = await budgetScenarioApi.calculateScenario(params)
        this.applyCalculatedScenarioResult(response.data || {})
        if (this.settingsForm.refreshListAfterCalculate) {
          await this.getList()
        }
        this.$message.success('场景计算完成')
      } catch (error) {
        this.$message.error('场景计算失败：' + error.message)
      }
    },

    applyCalculatedScenarioResult(result) {
      const scenarioId = result.scenarioId || this.scenarioForm.scenarioId || this.scenarioForm.id
      const patch = {
        budgetAmount: result.budgetAmount || 0,
        varianceAmount: result.varianceAmount || 0,
        varianceRate: result.varianceRate || 0,
        scenarioId,
        id: scenarioId
      }

      this.scenarioForm = {
        ...this.scenarioForm,
        ...patch
      }

      this.scenarioList = this.scenarioList.map(item => {
        if ((item.scenarioId || item.id) !== scenarioId) {
          return item
        }
        return {
          ...item,
          ...patch
        }
      })

      if (this.currentScenarioDetail && (this.currentScenarioDetail.scenarioId || this.currentScenarioDetail.id) === scenarioId) {
        this.currentScenarioDetail = {
          ...this.currentScenarioDetail,
          ...patch
        }
      }

      if (this.currentScenarioAnalysis && (this.currentScenarioAnalysis.scenarioId || this.currentScenarioAnalysis.id) === scenarioId) {
        this.currentScenarioAnalysis = {
          ...this.currentScenarioAnalysis,
          ...patch
        }
      }
    },

    applyBaselineScenarioResult(scenarioId) {
      const targetId = scenarioId || this.scenarioForm.scenarioId || this.scenarioForm.id
      if (!targetId) {
        return
      }

      this.scenarioList = this.scenarioList.map(item => {
        const currentId = item.scenarioId || item.id
        const isCurrent = currentId === targetId
        return {
          ...item,
          isBaseline: isCurrent,
          isDefault: isCurrent
        }
      })

      if (this.scenarioForm && (this.scenarioForm.scenarioId || this.scenarioForm.id)) {
        const isCurrentForm = (this.scenarioForm.scenarioId || this.scenarioForm.id) === targetId
        this.scenarioForm = {
          ...this.scenarioForm,
          isBaseline: isCurrentForm,
          isDefault: isCurrentForm
        }
      }

      if (this.currentScenarioDetail && (this.currentScenarioDetail.scenarioId || this.currentScenarioDetail.id)) {
        const isCurrentDetail = (this.currentScenarioDetail.scenarioId || this.currentScenarioDetail.id) === targetId
        this.currentScenarioDetail = {
          ...this.currentScenarioDetail,
          isBaseline: isCurrentDetail,
          isDefault: isCurrentDetail
        }
      }

      if (this.currentScenarioAnalysis && (this.currentScenarioAnalysis.scenarioId || this.currentScenarioAnalysis.id)) {
        const isCurrentAnalysis = (this.currentScenarioAnalysis.scenarioId || this.currentScenarioAnalysis.id) === targetId
        this.currentScenarioAnalysis = {
          ...this.currentScenarioAnalysis,
          isBaseline: isCurrentAnalysis,
          isDefault: isCurrentAnalysis
        }
      }
    },
    
    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.scenarioForm.validate()

        const payload = {
          scenarioId: this.scenarioForm.scenarioId || this.scenarioForm.id || undefined,
          scenarioName: this.scenarioForm.scenarioName,
          scenarioCode: this.scenarioForm.scenarioCode,
          scenarioType: this.scenarioForm.scenarioType,
          scenarioDescription: this.scenarioForm.scenarioDescription,
          assumptions: Array.isArray(this.scenarioForm.assumptions)
            ? JSON.stringify(this.scenarioForm.assumptions)
            : (this.scenarioForm.assumptions || '[]'),
          parameters: this.scenarioForm.parameters || `budgetAmount=${this.scenarioForm.budgetAmount || 0}`,
          isDefault: !!this.scenarioForm.isBaseline,
          isEnabled: this.scenarioForm.scenarioStatus === 'ACTIVE' || this.scenarioForm.scenarioStatus === 'APPROVED'
        }

        if (payload.scenarioId) {
          await budgetScenarioApi.update(payload)
          this.$message.success('更新成功')
        } else {
          delete payload.scenarioId
          await budgetScenarioApi.create(payload)
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
      this.scenarioForm = this.getDefaultScenarioForm()
      this.$nextTick(() => {
        this.$refs.scenarioForm && this.$refs.scenarioForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 复制场景
    handleCopyScenario() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要复制的场景')
        return
      }
      
      const row = this.selectedRows[0]
      this.dialogTitle = '复制预算场景'
      this.dialogVisible = true
      this.scenarioForm = { ...row, id: null, scenarioId: null, scenarioCode: null, isBaseline: false, isDefault: false }
    },
    
    // 场景对比
    handleCompareScenario() {
      this.compareDialogVisible = true
      this.compareScenarios = []
      this.compareResult = null
    },
    
    // 执行对比
    async handleExecuteCompare() {
      if (this.compareScenarios.length < 2) {
        this.$message.warning('请至少选择2个场景进行对比')
        return
      }
      
      try {
        const params = {
          scenarioIds: this.compareScenarios
        }
        const response = await budgetScenarioApi.compareScenarios(params)
        this.compareResult = response.data.compareData
        this.selectedScenarios = response.data.scenarios
      } catch (error) {
        this.$message.error('场景对比失败：' + error.message)
      }
    },
    
    // 导出对比
    async handleExportCompare() {
      if (!this.compareResult) {
        this.$message.warning('请先执行场景对比')
        return
      }
      
      try {
        const params = {
          scenarioIds: this.compareScenarios,
          compareData: this.compareResult
        }
        const blob = await budgetScenarioApi.exportCompare(params)
        downloadByBlob(blob, 'budget_scenario_compare.xlsx')
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 导出场景
    async handleExportScenario() {
      try {
        const params = { ...this.queryForm }
        const blob = await budgetScenarioApi.export(params)
        downloadByBlob(blob, 'budget_scenario.xlsx')
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
      this.settingsDialogVisible = true
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopyRow(row)
          break
        case 'baseline':
          this.handleSetBaseline(row)
          break
        case 'compare':
          this.handleCompareRow(row)
          break
        case 'approve':
          this.handleApprove(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
        case 'delete':
          this.handleDeleteRow(row)
          break
      }
    },
    
    // 复制行
    handleCopyRow(row) {
      this.dialogTitle = '复制预算场景'
      this.dialogVisible = true
      this.scenarioForm = { ...row, id: null, scenarioId: null, scenarioCode: null, isBaseline: false, isDefault: false }
    },
    
    // 设为基准
    async handleSetBaseline(row) {
      try {
        const scenarioId = row.scenarioId || row.id
        const isAlreadyBaseline = this.isScenarioBaseline(row)
        if (isAlreadyBaseline) {
          this.$message.info('当前已是基准场景')
          return
        }

        await this.$confirm('确认设置为基准场景吗？', '提示', {
          type: 'warning'
        })
        await budgetScenarioApi.setBaseline(scenarioId)
        this.applyBaselineScenarioResult(scenarioId)
        this.$message.success('设置成功')
        this.getList()
        this.loadBaselineScenarios()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('设置失败：' + error.message)
        }
      }
    },
    
    // 对比行
    handleCompareRow(row) {
      this.compareDialogVisible = true
      this.compareScenarios = [row.scenarioId || row.id]
      this.compareResult = null
    },
    
    // 提交审批
    async handleApprove(row) {
      try {
        await this.$confirm('确认提交审批吗？', '提示', {
          type: 'warning'
        })
        await budgetScenarioApi.submitApproval(row.scenarioId || row.id)
        this.$message.success('提交成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('提交失败：' + error.message)
        }
      }
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        const scenarioId = row.scenarioId || row.id
        const blob = await budgetScenarioApi.exportSingle(scenarioId)
        downloadByBlob(blob, `budget_scenario_${scenarioId}.xlsx`)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    async handleDeleteRow(row) {
      try {
        await this.$confirm('确认删除当前场景吗？', '提示', {
          type: 'warning'
        })
        await budgetScenarioApi.delete(row.scenarioId || row.id)
        this.$message.success('删除成功')
        await this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    async handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的场景')
        return
      }
      try {
        await this.$confirm(`确认删除选中的 ${this.selectedRows.length} 个场景吗？`, '提示', {
          type: 'warning'
        })
        await Promise.all(this.selectedRows.map(row => budgetScenarioApi.delete(row.scenarioId || row.id)))
        this.$message.success('批量删除成功')
        this.selectedRows = []
        await this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败：' + error.message)
        }
      }
    },
    
    // 行点击
    handleRowClick(row) {
      if (!this.settingsForm.enableRowClickView) {
        return
      }
      this.handleView(row)
    },
    
    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 排序改变
    handleSortChange({ prop, order }) {
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
      this.checkedColumnKeys = this.columnOptions
        .filter(column => this.visibleColumns[column.key])
        .map(column => column.key)
      this.columnSettingDialogVisible = true
    },

    handleSaveColumnSetting() {
      const nextVisibleColumns = {}
      this.columnOptions.forEach(column => {
        nextVisibleColumns[column.key] = this.checkedColumnKeys.includes(column.key)
      })
      if (!Object.values(nextVisibleColumns).some(Boolean)) {
        this.$message.warning('至少保留一列展示')
        return
      }
      this.visibleColumns = nextVisibleColumns
      this.columnSettingDialogVisible = false
      this.$message.success('列设置已生效')
    },
    
    // 判断是否可以编辑
    canEdit(row) {
      return ['DRAFT', 'ACTIVE'].includes(row.scenarioStatus) && (row.creator || row.createBy) === this.$store.getters.name
    },
    
    // 判断是否可以分析
    canAnalyze(row) {
      return ['ACTIVE', 'APPROVED'].includes(row.scenarioStatus)
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取差异样式类
    getVarianceClass(variance) {
      if (!variance) return 'amount-text'
      return variance > 0 ? 'positive-variance' : 'negative-variance'
    },
    
    // 获取差异率颜色
    getVarianceRateColor(rate) {
      if (!rate) return '#909399'
      const absRate = Math.abs(rate)
      if (absRate <= 5) return '#67C23A'
      if (absRate <= 15) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取场景类型颜色
    getScenarioTypeColor(type) {
      const colorMap = {
        'BASE': 'warning',
        'BASELINE': 'warning',
        'OPTIMISTIC': 'success',
        'PESSIMISTIC': 'danger',
        'REALISTIC': 'primary',
        'CUSTOM': 'info',
        'WHAT_IF': 'info'
      }
      const normalizedType = (type || '').toString().toUpperCase()
      return colorMap[normalizedType] || 'info'
    },
    
    // 获取场景类型文本
    getScenarioTypeText(type) {
      const normalizedType = (type || '').toString().toUpperCase()
      const typeMap = {
        BASE: '基准场景',
        BASELINE: '基准场景',
        OPTIMISTIC: '乐观场景',
        PESSIMISTIC: '悲观场景',
        REALISTIC: '现实场景',
        CUSTOM: '自定义场景',
        WHAT_IF: '假设场景'
      }
      return typeMap[normalizedType] || type || '-'
    },

    isBaselineScenarioType(type) {
      const normalizedType = (type || '').toString().toUpperCase()
      return normalizedType === 'BASE' || normalizedType === 'BASELINE'
    },

    isScenarioBaseline(row) {
      if (!row) {
        return false
      }
      return !!(row.isBaseline || row.isDefault || this.isBaselineScenarioType(row.scenarioType))
    },

    formatRate(rate) {
      const value = Number(rate || 0)
      return `${value.toFixed(1)}%`
    },
    
    // 获取场景状态类型
    getScenarioStatusType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'ACTIVE': 'success',
        'APPROVED': 'primary',
        'ARCHIVED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取场景状态文本
    getScenarioStatusText(status) {
      const item = this.scenarioStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    },

    formatAffectedAccounts(accounts) {
      if (!accounts) return '-'
      if (Array.isArray(accounts)) return accounts.join('、') || '-'
      return accounts
    },

    getAnalysisRiskLevelType(row) {
      const absRate = Math.abs(Number(row.varianceRate || 0))
      if (absRate <= 5) return 'success'
      if (absRate <= 15) return 'warning'
      return 'danger'
    },

    getAnalysisRiskLevelText(row) {
      const absRate = Math.abs(Number(row.varianceRate || 0))
      if (absRate <= 5) return '低风险'
      if (absRate <= 15) return '中风险'
      return '高风险'
    },

    buildScenarioAnalysisSummary(row) {
      const varianceRate = Number(row.varianceRate || 0)
      if (varianceRate > 0) {
        return `当前场景相较基准呈正向偏差，预算影响率约为 ${varianceRate}% ，建议重点关注增量驱动因素。`
      }
      if (varianceRate < 0) {
        return `当前场景相较基准呈负向偏差，预算影响率约为 ${Math.abs(varianceRate)}% ，建议及时复核关键假设条件。`
      }
      return '当前场景与基准场景差异较小，整体预算表现平稳。'
    }
  },

  computed: {
    detailAssumptions() {
      const assumptions = this.currentScenarioDetail && this.currentScenarioDetail.assumptions
      if (Array.isArray(assumptions)) return assumptions
      if (typeof assumptions === 'string') {
        try {
          return JSON.parse(assumptions)
        } catch (e) {
          return []
        }
      }
      return []
    },

    analysisAssumptions() {
      const assumptions = this.currentScenarioAnalysis && this.currentScenarioAnalysis.assumptions
      if (Array.isArray(assumptions)) return assumptions
      if (typeof assumptions === 'string') {
        try {
          return JSON.parse(assumptions)
        } catch (e) {
          return []
        }
      }
      return []
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-scenario {
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
      
      &.approved-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.baseline-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
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
  
  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #67C23A;
  }
  
  .positive-variance {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #67C23A;
  }
  
  .negative-variance {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #F56C6C;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .assumptions-config {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    
    .assumptions-header {
      padding: 12px;
      background-color: #F5F7FA;
      border-bottom: 1px solid #EBEEF5;
      display: flex;
      gap: 8px;
    }
  }
  
  .scenario-compare {
    .compare-header {
      margin-bottom: 20px;
      padding-bottom: 15px;
      border-bottom: 1px solid #EBEEF5;
    }
    
    .compare-result {
      max-height: 400px;
      overflow-y: auto;
    }
  }

  .primary-text {
    color: #409EFF;
  }

  .warning-text {
    color: #E6A23C;
  }

  .baseline-text {
    color: #E6A23C;
    font-weight: 500;
  }

  .danger-text {
    color: #F56C6C;
  }

  .variance-rate-text {
    font-size: 12px;
    color: #909399;
    margin-top: 4px;
  }

  .text-right {
    text-align: right;
  }
}
</style>
