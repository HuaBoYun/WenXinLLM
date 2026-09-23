<template>
  <div class="comparison-analysis">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算对比分析</h2>
      <p>多维度预算对比分析，支持同期对比、环比对比、预实对比等多种对比方式</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-data-analysis" @click="handleCreateComparison">创建对比</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefreshComparison">刷新对比</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExportComparison">导出对比</el-button>
            <el-button type="info" icon="el-icon-s-data" @click="handleBatchComparison">批量对比</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleComparisonSettings">对比设置</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 对比分析概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card comparison-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ comparisonStats.totalComparisons }}</div>
            <div class="stat-label">对比项目</div>
            <div class="stat-description">总对比分析项目数</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>较上期增长15.3%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-data-analysis"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card significant-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ comparisonStats.significantDifferences }}</div>
            <div class="stat-label">显著差异</div>
            <div class="stat-description">存在显著差异的项目</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-down trend-down"></i>
              <span>较上期下降8.7%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card accuracy-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ comparisonStats.accuracy }}%</div>
            <div class="stat-label">对比准确率</div>
            <div class="stat-description">对比分析准确率</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>准确率提升3.2%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card coverage-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ comparisonStats.coverage }}%</div>
            <div class="stat-label">覆盖率</div>
            <div class="stat-description">预算对比覆盖率</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>覆盖率提升5.1%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-pie-chart"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 对比条件设置 -->
    <el-card class="search-card" shadow="never">
      <div class="search-header">
        <span class="search-title">对比条件设置</span>
        <el-button type="text" @click="handleResetConditions">重置条件</el-button>
      </div>
      <el-form :model="comparisonForm" :inline="true" size="small">
        <el-form-item label="对比类型">
          <el-select
            v-model="comparisonForm.comparisonType"
            placeholder="请选择对比类型"
            clearable
            style="width: 150px"
            @change="handleComparisonTypeChange"
          >
            <el-option value="" label="全部" />
            <el-option value="PERIOD" label="期间对比" />
            <el-option value="BUDGET_ACTUAL" label="预实对比" />
            <el-option value="SEQUENTIAL" label="环比对比" />
            <el-option value="DEPARTMENT" label="部门对比" />
            <el-option value="PROJECT" label="项目对比" />
            <el-option value="ACCOUNT" label="科目对比" />
            <el-option value="CUSTOM" label="自定义对比" />
          </el-select>
        </el-form-item>
        
        <!-- 基准期间 -->
        <el-form-item label="基准期间">
          <el-date-picker
            v-model="comparisonForm.basePeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="基准开始日期"
            end-placeholder="基准结束日期"
            style="width: 240px"
          />
        </el-form-item>
        
        <!-- 对比期间 -->
        <el-form-item label="对比期间">
          <el-date-picker
            v-model="comparisonForm.comparePeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="对比开始日期"
            end-placeholder="对比结束日期"
            style="width: 240px"
          />
        </el-form-item>
        
        <el-form-item label="组织单元">
          <el-select
            v-model="comparisonForm.organizationPath"
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
            v-model="comparisonForm.budgetAccount"
            placeholder="请选择预算科目"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in budgetAccountOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="对比维度">
          <el-select
            v-model="comparisonForm.comparisonDimension"
            placeholder="请选择对比维度"
            multiple
            style="width: 200px"
          >
            <el-option value="AMOUNT" label="金额" />
            <el-option value="RATE" label="比率" />
            <el-option value="GROWTH" label="增长率" />
            <el-option value="VARIANCE" label="差异" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleStartComparison">开始对比</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 对比图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>对比分析图表</span>
            <div class="header-tools">
              <el-radio-group v-model="chartType" size="mini">
                <el-radio-button label="bar">柱状图</el-radio-button>
                <el-radio-button label="line">折线图</el-radio-button>
                <el-radio-button label="radar">雷达图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="comparisonChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>差异分析图表</span>
            <div class="header-tools">
              <el-radio-group v-model="differenceChartType" size="mini">
                <el-radio-button label="waterfall">瀑布图</el-radio-button>
                <el-radio-button label="scatter">散点图</el-radio-button>
                <el-radio-button label="heatmap">热力图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="differenceChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 对比结果表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">对比分析结果</span>
        <div class="table-tools">
          <el-tooltip content="显示差异" placement="top">
            <el-switch
              v-model="showDifference"
              active-text="显示差异"
              @change="handleShowDifferenceChange"
            />
          </el-tooltip>
          <el-tooltip content="显示百分比" placement="top">
            <el-switch
              v-model="showPercentage"
              active-text="显示百分比"
              @change="handleShowPercentageChange"
            />
          </el-tooltip>
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getComparisonResults" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        ref="comparisonTable"
        v-loading="loading"
        :data="comparisonResults"
        border
        stripe
        highlight-current-row
        @sort-change="handleSortChange"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column prop="comparisonItem" label="对比项目" width="150" show-overflow-tooltip />
        <el-table-column prop="organizationName" label="组织单元" width="120" show-overflow-tooltip />
        <el-table-column prop="budgetAccountName" label="预算科目" width="120" show-overflow-tooltip />
        
        <el-table-column prop="baseValue" label="基准值" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatComparisonValue(scope.row.baseValue, scope.row.valueType) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="compareValue" label="对比值" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatComparisonValue(scope.row.compareValue, scope.row.valueType) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="difference" label="差异" width="120" align="right" sortable="custom" v-if="showDifference">
          <template slot-scope="scope">
            <span :class="getDifferenceClass(scope.row.difference)">
              {{ formatDifference(scope.row.difference, scope.row.valueType) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column prop="changeRate" label="变化率" width="100" align="right" sortable="custom" v-if="showPercentage">
          <template slot-scope="scope">
            <span :class="getChangeRateClass(scope.row.changeRate)">
              {{ formatChangeRate(scope.row.changeRate) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column prop="significance" label="显著性" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSignificanceColor(scope.row.significance)" size="mini">
              {{ getSignificanceText(scope.row.significance) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="comparisonResult" label="对比结果" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getComparisonResultColor(scope.row.comparisonResult)" size="mini">
              <i :class="getComparisonResultIcon(scope.row.comparisonResult)"></i>
              {{ getComparisonResultText(scope.row.comparisonResult) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="analysisDate" label="分析日期" width="120" align="center" />
        
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleViewDetail(scope.row)"
            >详情</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEditComparison(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-data-line"
              @click="handleDrillDown(scope.row)"
            >钻取</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="trend" icon="el-icon-trend-charts">趋势分析</el-dropdown-item>
                <el-dropdown-item command="variance" icon="el-icon-data-analysis">差异分析</el-dropdown-item>
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

    <!-- 对比详情抽屉 -->
    <el-drawer
      title="对比详情分析"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="60%"
    >
      <div class="detail-content" v-if="currentComparisonDetail">
        <el-tabs v-model="activeTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="对比基本信息" :column="2" border>
              <el-descriptions-item label="对比项目">{{ currentComparisonDetail.comparisonItem }}</el-descriptions-item>
              <el-descriptions-item label="对比类型">{{ currentComparisonDetail.comparisonType }}</el-descriptions-item>
              <el-descriptions-item label="组织单元">{{ currentComparisonDetail.organizationName }}</el-descriptions-item>
              <el-descriptions-item label="预算科目">{{ currentComparisonDetail.budgetAccountName }}</el-descriptions-item>
              <el-descriptions-item label="基准值">{{ formatComparisonValue(currentComparisonDetail.baseValue, currentComparisonDetail.valueType) }}</el-descriptions-item>
              <el-descriptions-item label="对比值">{{ formatComparisonValue(currentComparisonDetail.compareValue, currentComparisonDetail.valueType) }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          
          <el-tab-pane label="对比图表" name="chart">
            <div id="detailComparisonChart" style="height: 400px;"></div>
          </el-tab-pane>
          
          <el-tab-pane label="统计分析" name="statistics">
            <div class="statistics-content">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-card shadow="never">
                    <div slot="header">统计指标</div>
                    <el-descriptions :column="1" border>
                      <el-descriptions-item label="差异值">{{ formatDifference(currentComparisonDetail.difference, currentComparisonDetail.valueType) }}</el-descriptions-item>
                      <el-descriptions-item label="变化率">{{ formatChangeRate(currentComparisonDetail.changeRate) }}</el-descriptions-item>
                      <el-descriptions-item label="显著性">{{ getSignificanceText(currentComparisonDetail.significance) }}</el-descriptions-item>
                      <el-descriptions-item label="置信度">{{ currentComparisonDetail.confidence || 'N/A' }}%</el-descriptions-item>
                    </el-descriptions>
                  </el-card>
                </el-col>
                <el-col :span="12">
                  <el-card shadow="never">
                    <div slot="header">对比结论</div>
                    <el-descriptions :column="1" border>
                      <el-descriptions-item label="对比结果">{{ getComparisonResultText(currentComparisonDetail.comparisonResult) }}</el-descriptions-item>
                      <el-descriptions-item label="影响因素">{{ currentComparisonDetail.influenceFactors || 'N/A' }}</el-descriptions-item>
                      <el-descriptions-item label="建议措施">{{ currentComparisonDetail.recommendations || 'N/A' }}</el-descriptions-item>
                      <el-descriptions-item label="风险评估">{{ currentComparisonDetail.riskAssessment || 'N/A' }}</el-descriptions-item>
                    </el-descriptions>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 创建对比对话框 -->
    <el-dialog title="创建对比分析" :visible.sync="createDialogVisible" width="650px" :close-on-click-modal="false">
      <el-form ref="createForm" :model="createForm" :rules="createRules" label-width="100px" size="small">
        <el-form-item label="对比名称" prop="comparisonName">
          <el-input v-model="createForm.comparisonName" placeholder="请输入对比名称" />
        </el-form-item>
        <el-form-item label="对比类型" prop="comparisonType">
          <el-select v-model="createForm.comparisonType" style="width: 100%">
            <el-option label="同期对比" value="PERIOD" />
            <el-option label="预实对比" value="BUDGET_ACTUAL" />
            <el-option label="环比对比" value="SEQUENTIAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="基准期间" prop="basePeriod">
          <el-date-picker v-model="createForm.basePeriod" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" style="width: 100%" />
        </el-form-item>
        <el-form-item label="对比期间" prop="comparePeriod">
          <el-date-picker v-model="createForm.comparePeriod" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" style="width: 100%" />
        </el-form-item>
        <el-form-item label="组织单元">
          <el-select v-model="createForm.organizationId" placeholder="请选择组织单元" filterable clearable style="width: 100%">
            <el-option v-for="item in organizationOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算科目">
          <el-select v-model="createForm.budgetAccountId" placeholder="请选择预算科目" filterable clearable style="width: 100%">
            <el-option v-for="item in budgetAccountOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="createLoading" @click="handleSubmitCreate">确定</el-button>
      </div>
    </el-dialog>

    <!-- 编辑对比对话框 -->
    <el-dialog title="编辑对比分析" :visible.sync="editDialogVisible" width="650px" :close-on-click-modal="false">
      <el-form ref="editForm" :model="editForm" :rules="editRules" label-width="100px" size="small">
        <el-form-item label="对比名称" prop="comparisonName">
          <el-input v-model="editForm.comparisonName" placeholder="请输入对比名称" />
        </el-form-item>
        <el-form-item label="对比类型" prop="comparisonType">
          <el-select v-model="editForm.comparisonType" style="width: 100%">
            <el-option label="同期对比" value="PERIOD" />
            <el-option label="预实对比" value="BUDGET_ACTUAL" />
            <el-option label="环比对比" value="SEQUENTIAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="组织单元">
          <el-select v-model="editForm.organizationId" placeholder="请选择组织单元" filterable clearable style="width: 100%">
            <el-option v-for="item in organizationOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算科目">
          <el-select v-model="editForm.budgetAccountId" placeholder="请选择预算科目" filterable clearable style="width: 100%">
            <el-option v-for="item in budgetAccountOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="基准值">
          <el-input-number v-model="editForm.baseValue" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="对比值">
          <el-input-number v-model="editForm.compareValue" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="editLoading" @click="handleSubmitEdit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 设置对话框 -->
    <el-dialog title="对比分析设置" :visible.sync="settingsDialogVisible" width="500px">
      <el-form :model="settingsForm" label-width="120px" size="small">
        <el-form-item label="显示差异值">
          <el-switch v-model="settingsForm.showDifference" />
        </el-form-item>
        <el-form-item label="显示百分比">
          <el-switch v-model="settingsForm.showPercentage" />
        </el-form-item>
        <el-form-item label="每页条数">
          <el-select v-model="settingsForm.pageSize" style="width: 100%">
            <el-option label="10条" :value="10" />
            <el-option label="20条" :value="20" />
            <el-option label="50条" :value="50" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSettings">保存</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="对比分析帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>对比分析用于在不同维度上对预算数据进行比较，支持同期对比、预实对比和环比分析。</p>
        <el-collapse>
          <el-collapse-item title="1. 创建对比分析" name="1"><p>点击"创建对比"按钮，选择对比类型和期间进行分析。</p></el-collapse-item>
          <el-collapse-item title="2. 查看对比结果" name="2"><p>在表格和图表中查看对比结果，支持多维度切换。</p></el-collapse-item>
          <el-collapse-item title="3. 导出报告" name="3"><p>支持导出对比结果为Excel文件。</p></el-collapse-item>
        </el-collapse>
      </div>
      <div slot="footer"><el-button type="primary" @click="helpDialogVisible = false">我知道了</el-button></div>
    </el-dialog>

    <!-- 钻取分析抽屉 -->
    <el-drawer title="钻取分析" :visible.sync="drillDrawerVisible" direction="rtl" size="60%">
      <div v-if="drillData">
        <el-table :data="drillData.details || []" border stripe size="small">
          <el-table-column prop="itemName" label="明细项目" />
          <el-table-column prop="baseValue" label="基准值" align="right" />
          <el-table-column prop="compareValue" label="对比值" align="right" />
          <el-table-column prop="difference" label="差异" align="right" />
          <el-table-column prop="changeRate" label="变化率" align="right" />
        </el-table>
      </div>
      <el-empty v-else description="暂无数据" />
    </el-drawer>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { budgetAnalysisApi } from '@/api/managementAccountant/ncv65/budgetAnalysis'

export default {
  name: 'ComparisonAnalysis',
  data() {
    return {
      // 对比条件
      comparisonForm: {
        comparisonType: '',
        basePeriod: [],
        comparePeriod: [],
        organizationPath: '',
        budgetAccount: '',
        comparisonDimension: ['AMOUNT']
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      comparisonResults: [],
      total: 0,
      
      // 图表类型
      chartType: 'bar',
      differenceChartType: 'waterfall',
      // 缓存后端返回的图表数据，切换图表类型时无需重新请求
      cachedChartData: null,
      
      // 控制开关
      showDifference: true,
      showPercentage: true,
      
      // 统计数据
      comparisonStats: {
        totalComparisons: 0,
        significantDifferences: 0,
        accuracy: 0,
        coverage: 0
      },
      
      // 抽屉与对话框
      detailDrawerVisible: false,
      createDialogVisible: false,
      editDialogVisible: false,
      settingsDialogVisible: false,
      helpDialogVisible: false,
      drillDrawerVisible: false,
      createLoading: false,
      editLoading: false,
      currentComparisonDetail: null,
      activeTab: 'basic',
      drillData: null,
      createForm: {
        comparisonName: '',
        comparisonType: 'PERIOD',
        basePeriod: [],
        comparePeriod: [],
        organizationId: '',
        budgetAccountId: ''
      },
      createRules: {
        comparisonName: [{ required: true, message: '请输入对比名称', trigger: 'blur' }],
        comparisonType: [{ required: true, message: '请选择对比类型', trigger: 'change' }]
      },
      editForm: {
        id: '',
        comparisonName: '',
        comparisonType: '',
        organizationId: '',
        budgetAccountId: '',
        baseValue: 0,
        compareValue: 0
      },
      editRules: {
        comparisonName: [{ required: true, message: '请输入对比名称', trigger: 'blur' }],
        comparisonType: [{ required: true, message: '请选择对比类型', trigger: 'change' }]
      },
      settingsForm: {
        showDifference: true,
        showPercentage: true,
        pageSize: 20
      },

      // 选项数据
      organizationOptions: [],
      budgetAccountOptions: [],
      selectedRows: []
    }
  },
  
  created() {
    this.getComparisonResults()
    this.loadOrganizationOptions()
    this.loadBudgetAccountOptions()
    this.loadComparisonStats()
    this.initCharts()
  },

  watch: {
    chartType() {
      if (this.cachedChartData) {
        this.renderComparisonChart(this.cachedChartData.comparisonChart || {})
      }
    },
    differenceChartType() {
      if (this.cachedChartData) {
        this.renderDifferenceChart(this.cachedChartData.differenceChart || {})
      }
    }
  },

  beforeDestroy() {
    const ids = ['comparisonChart', 'differenceChart', 'detailComparisonChart']
    ids.forEach(id => {
      const dom = document.getElementById(id)
      if (dom) {
        const inst = echarts.getInstanceByDom(dom)
        if (inst) inst.dispose()
      }
    })
  },

  methods: {
    // 获取对比结果
    async getComparisonResults() {
      this.loading = true
      try {
        const params = {
          ...this.comparisonForm,
          ...this.queryParams
        }
        const response = await budgetAnalysisApi.getComparisonAnalysis(params)
        this.comparisonResults = response.data.tlist || response.data.list || []
        this.total = response.data.totalRecord || response.data.total || 0
      } catch (error) {
        this.$message.error('获取对比分析结果失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载组织选项
    async loadOrganizationOptions() {
      try {
        const response = await budgetAnalysisApi.getOrganizations()
        const data = response.data || []
        // 扁平化树形数据为 el-select 使用的 { value, label } 列表
        const flatten = (items, prefix = '') => {
          const result = []
          items.forEach(item => {
            result.push({ value: item.value || item.id, label: (prefix ? prefix + ' / ' : '') + (item.label || item.name) })
            if (item.children && item.children.length) {
              result.push(...flatten(item.children, item.label || item.name))
            }
          })
          return result
        }
        this.organizationOptions = flatten(data)
      } catch (error) {
        console.error('加载组织选项失败：', error)
      }
    },

    // 加载预算科目选项
    async loadBudgetAccountOptions() {
      try {
        const response = await budgetAnalysisApi.getBudgetAccounts()
        const data = response.data || []
        this.budgetAccountOptions = data.map(item => ({
          value: item.value || item.id,
          label: item.label || item.name
        }))
      } catch (error) {
        console.error('加载预算科目选项失败：', error)
      }
    },
    
    // 初始化图表 —— 直接加载数据并渲染
    initCharts() {
      this.$nextTick(() => {
        this.loadComparisonChartData()
      })
    },

    // 加载统计数据
    async loadComparisonStats() {
      try {
        const response = await budgetAnalysisApi.getComparisonStats()
        if (response.code === 1 && response.data) {
          this.comparisonStats = { ...this.comparisonStats, ...response.data }
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
      }
    },

    // 加载图表数据（缓存 + 渲染）
    async loadComparisonChartData() {
      try {
        const response = await budgetAnalysisApi.getComparisonChartData(this.comparisonForm)
        if (response.code === 1 && response.data) {
          this.cachedChartData = response.data
          this.renderComparisonChart(response.data.comparisonChart || {})
          this.renderDifferenceChart(response.data.differenceChart || {})
        }
      } catch (error) {
        console.error('加载图表数据失败：', error)
      }
    },

    /**
     * 渲染对比图表 —— 根据 chartType 决定 bar / line / radar
     */
    renderComparisonChart(cd) {
      const dom = document.getElementById('comparisonChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const xData = cd.xAxis || []
      const baseSeries = (cd.series || []).find(s => s.name === '基准值') || {}
      const compareSeries = (cd.series || []).find(s => s.name === '对比值') || {}
      const baseData = baseSeries.data || []
      const compareData = compareSeries.data || []
      let option = {}

      if (this.chartType === 'radar') {
        const maxVal = Math.max(...baseData, ...compareData, 1) * 1.2
        option = {
          title: { text: '预算对比雷达图', left: 'center' },
          tooltip: {},
          legend: { data: ['基准值', '对比值'], top: 30 },
          radar: {
            indicator: xData.map(name => ({ name, max: maxVal }))
          },
          series: [{
            name: '预算对比',
            type: 'radar',
            data: [
              { value: baseData, name: '基准值', itemStyle: { color: '#409EFF' } },
              { value: compareData, name: '对比值', itemStyle: { color: '#67C23A' } }
            ]
          }]
        }
      } else {
        // bar 或 line
        const sType = this.chartType === 'line' ? 'line' : 'bar'
        const titleText = sType === 'line' ? '预算对比趋势' : '预算对比分析'
        option = {
          title: { text: titleText, left: 'center' },
          tooltip: { trigger: 'axis', axisPointer: { type: sType === 'bar' ? 'shadow' : 'cross' } },
          legend: { data: ['基准值', '对比值'], top: 30 },
          grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
          xAxis: { type: 'category', data: xData, boundaryGap: sType === 'bar' },
          yAxis: { type: 'value', name: '金额(万元)' },
          series: [
            { name: '基准值', type: sType, data: baseData, smooth: true, itemStyle: { color: '#409EFF' } },
            { name: '对比值', type: sType, data: compareData, smooth: true, itemStyle: { color: '#67C23A' } }
          ]
        }
      }
      chart.setOption(option, true)
      chart.resize()
    },

    /**
     * 渲染差异图表 —— 根据 differenceChartType 决定 waterfall / scatter / heatmap
     */
    renderDifferenceChart(dd) {
      const dom = document.getElementById('differenceChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const xData = dd.xAxis || []
      const diffSeries = (dd.series || []).find(s => s.name === '差异值') || {}
      const diffData = diffSeries.data || []
      // 同时从缓存中取基准值和对比值（scatter 需要）
      const compChart = (this.cachedChartData || {}).comparisonChart || {}
      const baseSeries = (compChart.series || []).find(s => s.name === '基准值') || {}
      const compareSeries = (compChart.series || []).find(s => s.name === '对比值') || {}
      const baseData = baseSeries.data || []
      const compareData = compareSeries.data || []
      let option = {}

      if (this.differenceChartType === 'waterfall') {
        // 瀑布图：用辅助透明柱 + 正负差异柱实现
        const total = diffData.reduce((a, b) => a + (Number(b) || 0), 0)
        let cumulative = 0
        const helperData = []
        const posData = []
        const negData = []
        diffData.forEach(v => {
          const val = Number(v) || 0
          if (val >= 0) {
            helperData.push(cumulative)
            posData.push(val)
            negData.push('-')
          } else {
            helperData.push(cumulative + val)
            posData.push('-')
            negData.push(Math.abs(val))
          }
          cumulative += val
        })
        // 追加合计柱
        const finalXData = [...xData, '合计']
        helperData.push(0)
        posData.push(total >= 0 ? total : '-')
        negData.push(total < 0 ? Math.abs(total) : '-')

        option = {
          title: { text: '差异瀑布图', left: 'center' },
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' },
            formatter: function(params) {
              const idx = params[0].dataIndex
              const val = idx < diffData.length ? diffData[idx] : total
              return finalXData[idx] + '：' + Number(val).toLocaleString()
            }
          },
          grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
          xAxis: { type: 'category', data: finalXData },
          yAxis: { type: 'value', name: '金额(万元)' },
          series: [
            { name: '辅助', type: 'bar', stack: 'total', data: helperData, itemStyle: { color: 'transparent' }, emphasis: { itemStyle: { color: 'transparent' } } },
            { name: '正差异', type: 'bar', stack: 'total', data: posData, itemStyle: { color: '#67C23A' }, label: { show: true, position: 'top', formatter: p => p.value === '-' ? '' : p.value } },
            { name: '负差异', type: 'bar', stack: 'total', data: negData, itemStyle: { color: '#F56C6C' }, label: { show: true, position: 'bottom', formatter: p => p.value === '-' ? '' : '-' + p.value } }
          ]
        }
      } else if (this.differenceChartType === 'scatter') {
        const scatterData = baseData.map((b, i) => [Number(b) || 0, Number(compareData[i]) || 0])
        const allVals = [...baseData, ...compareData].map(Number).filter(v => !isNaN(v))
        const maxVal = Math.max(...allVals, 1)
        option = {
          title: { text: '差异散点图', left: 'center' },
          tooltip: {
            trigger: 'item',
            formatter: function(p) { return xData[p.dataIndex] + '<br/>基准值：' + p.value[0] + '<br/>对比值：' + p.value[1] }
          },
          grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
          xAxis: { type: 'value', name: '基准值', max: maxVal * 1.1 },
          yAxis: { type: 'value', name: '对比值', max: maxVal * 1.1 },
          series: [
            { name: '对比分析', type: 'scatter', data: scatterData, symbolSize: 14, itemStyle: { color: '#409EFF' } },
            { name: '基准线', type: 'line', data: [[0, 0], [maxVal, maxVal]], lineStyle: { type: 'dashed', color: '#E6A23C' }, symbol: 'none', tooltip: { show: false } }
          ]
        }
      } else if (this.differenceChartType === 'heatmap') {
        // 热力图：单行热力图展示各科目差异幅度
        const heatData = diffData.map((v, i) => [i, 0, Number(v) || 0])
        const absMax = Math.max(...diffData.map(v => Math.abs(Number(v) || 0)), 1)
        option = {
          title: { text: '差异热力图', left: 'center' },
          tooltip: {
            position: 'top',
            formatter: function(p) { return xData[p.value[0]] + '：' + p.value[2].toLocaleString() }
          },
          grid: { left: '3%', right: '4%', bottom: '10%', top: 60, containLabel: true },
          xAxis: { type: 'category', data: xData, splitArea: { show: true } },
          yAxis: { type: 'category', data: ['差异'], splitArea: { show: true } },
          visualMap: {
            min: -absMax, max: absMax, calculable: true, orient: 'horizontal', left: 'center', bottom: 0,
            inRange: { color: ['#F56C6C', '#FFFFFF', '#67C23A'] }
          },
          series: [{
            name: '差异值',
            type: 'heatmap',
            data: heatData,
            label: { show: true, formatter: function(p) { return Number(p.value[2]).toFixed(0) } },
            emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.5)' } }
          }]
        }
      }
      chart.setOption(option, true)
      chart.resize()
    },
    
    // 对比类型改变
    handleComparisonTypeChange(value) {
      // 根据对比类型调整表单字段
      this.$message.info(`已切换到${value}对比模式`)
    },
    
    // 开始对比
    handleStartComparison() {
      this.queryParams.pageNum = 1
      this.getComparisonResults()
      this.initCharts()
    },
    
    // 重置
    handleReset() {
      this.comparisonForm = {
        comparisonType: '',
        basePeriod: [],
        comparePeriod: [],
        organizationPath: '',
        budgetAccount: '',
        comparisonDimension: ['AMOUNT']
      }
      this.handleStartComparison()
    },
    
    // 重置条件
    handleResetConditions() {
      this.handleReset()
    },
    
    // 显示差异切换
    handleShowDifferenceChange(value) {
      this.$message.info(value ? '已显示差异列' : '已隐藏差异列')
    },
    
    // 显示百分比切换
    handleShowPercentageChange(value) {
      this.$message.info(value ? '已显示百分比列' : '已隐藏百分比列')
    },
    
    // 创建对比
    handleCreateComparison() {
      this.createForm = { comparisonName: '', comparisonType: 'PERIOD', basePeriod: [], comparePeriod: [], organizationId: '', budgetAccountId: '' }
      this.createDialogVisible = true
    },
    async handleSubmitCreate() {
      try {
        await this.$refs.createForm.validate()
        this.createLoading = true
        // 找到选中项的名称一起传过去
        const orgItem = this.organizationOptions.find(o => o.value === this.createForm.organizationId)
        const accItem = this.budgetAccountOptions.find(a => a.value === this.createForm.budgetAccountId)
        const submitData = {
          ...this.createForm,
          organizationName: orgItem ? orgItem.label : '',
          budgetAccountName: accItem ? accItem.label : ''
        }
        await budgetAnalysisApi.createBudgetComparison(submitData)
        this.$message.success('创建成功')
        this.createDialogVisible = false
        this.getComparisonResults()
        this.loadComparisonStats()
      } catch (error) {
        if (error !== false) this.$message.error('创建失败：' + (error.message || '未知错误'))
      } finally {
        this.createLoading = false
      }
    },

    // 编辑对比
    handleEditComparison(row) {
      this.editForm = {
        id: row.id,
        comparisonName: row.comparisonItem || row.comparisonName || '',
        comparisonType: row.comparisonType || '',
        organizationId: row.organizationId || '',
        budgetAccountId: row.budgetAccountId || '',
        baseValue: row.baseValue || 0,
        compareValue: row.compareValue || 0
      }
      this.editDialogVisible = true
    },
    async handleSubmitEdit() {
      try {
        await this.$refs.editForm.validate()
        this.editLoading = true
        const orgItem = this.organizationOptions.find(o => o.value === this.editForm.organizationId)
        const accItem = this.budgetAccountOptions.find(a => a.value === this.editForm.budgetAccountId)
        const submitData = {
          ...this.editForm,
          organizationName: orgItem ? orgItem.label : '',
          budgetAccountName: accItem ? accItem.label : ''
        }
        await budgetAnalysisApi.updateComparison(submitData)
        this.$message.success('更新成功')
        this.editDialogVisible = false
        this.getComparisonResults()
        this.loadComparisonStats()
      } catch (error) {
        if (error !== false) this.$message.error('更新失败：' + (error.message || '未知错误'))
      } finally {
        this.editLoading = false
      }
    },

    // 刷新对比
    handleRefreshComparison() {
      this.getComparisonResults()
      this.initCharts()
    },
    
    // 导出对比
    async handleExportComparison() {
      try {
        let params = {}
        if (this.selectedRows.length > 0) {
          params = { ids: this.selectedRows.map(r => r.id) }
          this.$message.info(`正在导出${this.selectedRows.length}条选中数据...`)
        } else {
          params = { ...this.comparisonForm, ...this.queryParams }
          this.$message.info('未选中数据，正在导出当前页数据...')
        }
        const res = await budgetAnalysisApi.exportComparisonAnalysis(params)
        if (res.code === 1) {
          const fileName = (res.data && res.data.fileName) || '对比分析报告.xlsx'
          this.$message.success(`导出成功：${fileName}`)
        } else {
          this.$message.error('导出失败：' + (res.msg || '未知错误'))
        }
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      }
    },

    // 批量对比
    handleBatchComparison() {
      if (this.selectedRows.length < 2) {
        this.$message.warning('请至少选择两条数据进行批量对比')
        return
      }
      const names = this.selectedRows.map(r => r.comparisonItem).join('、')
      this.$confirm(`确定要对以下${this.selectedRows.length}条数据进行批量对比？\n${names}`, '批量对比', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const ids = this.selectedRows.map(r => r.id)
          const res = await budgetAnalysisApi.batchComparison(ids)
          if (res.code === 1) {
            const data = res.data || {}
            this.$alert(
              `批量对比完成：共${data.count || 0}条记录\n` +
              `基准值合计：${data.totalBase || 0}\n` +
              `对比值合计：${data.totalCompare || 0}\n` +
              `差异合计：${data.totalDifference || 0}\n` +
              `总变化率：${data.totalChangeRate || 0}%`,
              '批量对比结果',
              { confirmButtonText: '确定', type: 'success' }
            )
            this.getComparisonResults()
          } else {
            this.$message.error('批量对比失败：' + (res.msg || '未知错误'))
          }
        } catch (error) {
          this.$message.error('批量对比失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {})
    },

    // 表格多选变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 对比设置
    handleComparisonSettings() {
      this.settingsDialogVisible = true
    },
    handleSaveSettings() {
      this.showDifference = this.settingsForm.showDifference
      this.showPercentage = this.settingsForm.showPercentage
      this.queryParams.pageSize = this.settingsForm.pageSize
      this.settingsDialogVisible = false
      this.$message.success('设置已保存')
      this.getComparisonResults()
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },
    
    // 查看详情
    handleViewDetail(row) {
      this.currentComparisonDetail = row
      this.activeTab = 'basic'
      this.detailDrawerVisible = true
      this.$nextTick(() => {
        this.initDetailChart()
      })
    },
    
    // 钻取分析
    async handleDrillDown(row) {
      try {
        const res = await budgetAnalysisApi.getBudgetComparison(row.id)
        this.drillData = res.data || { details: [] }
        this.drillDrawerVisible = true
      } catch (error) {
        this.$message.error('获取钻取数据失败')
      }
    },
    
    // 初始化详情图表
    initDetailChart() {
      const chartDom = document.getElementById('detailComparisonChart')
      if (chartDom) {
        const myChart = echarts.init(chartDom)
        const option = {
          title: {
            text: '详细对比图',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['基准值', '对比值'],
            top: 30
          },
          xAxis: {
            type: 'category',
            data: ['1月', '2月', '3月', '4月', '5月', '6月']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '基准值',
              type: 'bar',
              data: [820, 932, 901, 934, 1290, 1330],
              itemStyle: { color: '#409EFF' }
            },
            {
              name: '对比值',
              type: 'bar',
              data: [780, 980, 950, 1000, 1350, 1400],
              itemStyle: { color: '#67C23A' }
            }
          ]
        }
        myChart.setOption(option)
      }
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'trend':
          this.handleTrendAnalysis(row)
          break
        case 'variance':
          this.handleVarianceAnalysis(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
        case 'delete':
          this.handleDeleteComparison(row)
          break
      }
    },

    // 趋势分析 - 打开详情抽屉并切换到趋势tab
    async handleTrendAnalysis(row) {
      try {
        const res = await budgetAnalysisApi.getBudgetComparison(row.id)
        if (res.code === 1 && res.data) {
          this.currentComparisonDetail = { ...row, ...res.data }
        } else {
          this.currentComparisonDetail = { ...row }
        }
      } catch (e) {
        this.currentComparisonDetail = { ...row }
      }
      this.activeTab = 'chart'
      this.detailDrawerVisible = true
    },

    // 差异分析 - 打开详情抽屉并切换到分析tab
    async handleVarianceAnalysis(row) {
      try {
        const res = await budgetAnalysisApi.getBudgetComparison(row.id)
        if (res.code === 1 && res.data) {
          this.currentComparisonDetail = { ...row, ...res.data }
        } else {
          this.currentComparisonDetail = { ...row }
        }
      } catch (e) {
        this.currentComparisonDetail = { ...row }
      }
      this.activeTab = 'analysis'
      this.detailDrawerVisible = true
    },

    // 导出单个
    async handleExportSingle(row) {
      try {
        await budgetAnalysisApi.exportSingleComparison(row.id)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      }
    },

    // 删除对比分析
    handleDeleteComparison(row) {
      this.$confirm(`确定要删除对比分析「${row.comparisonItem || row.analysisName || ''}」吗？删除后不可恢复。`, '删除确认', {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await budgetAnalysisApi.deleteComparison(row.id)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.getComparisonResults()
            this.loadComparisonStats()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {})
    },
    
    // 排序改变
    handleSortChange({ column, prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getComparisonResults()
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getComparisonResults()
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getComparisonResults()
    },
    
    // 格式化对比值
    formatComparisonValue(value, type) {
      if (!value) return '0.00'
      if (type === 'RATE' || type === 'PERCENTAGE') {
        return `${parseFloat(value).toFixed(2)}%`
      }
      return parseFloat(value).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 格式化差异
    formatDifference(difference, type) {
      if (!difference) return '0.00'
      const formatted = this.formatComparisonValue(Math.abs(difference), type)
      return difference >= 0 ? `+${formatted}` : `-${formatted}`
    },
    
    // 格式化变化率
    formatChangeRate(rate) {
      if (!rate) return '0.00%'
      const formatted = `${parseFloat(Math.abs(rate)).toFixed(2)}%`
      return rate >= 0 ? `+${formatted}` : `-${formatted}`
    },
    
    // 获取差异样式类
    getDifferenceClass(difference) {
      if (difference > 0) return 'positive-difference'
      if (difference < 0) return 'negative-difference'
      return 'zero-difference'
    },
    
    // 获取变化率样式类
    getChangeRateClass(rate) {
      if (rate > 0) return 'positive-change'
      if (rate < 0) return 'negative-change'
      return 'zero-change'
    },
    
    // 获取显著性颜色
    getSignificanceColor(significance) {
      const colorMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success',
        'NONE': 'info'
      }
      return colorMap[significance] || 'info'
    },
    
    // 获取显著性文本
    getSignificanceText(significance) {
      const textMap = {
        'HIGH': '高显著',
        'MEDIUM': '中显著',
        'LOW': '低显著',
        'NONE': '不显著'
      }
      return textMap[significance] || significance
    },
    
    // 获取对比结果颜色
    getComparisonResultColor(result) {
      const colorMap = {
        'BETTER': 'success',
        'WORSE': 'danger',
        'SIMILAR': 'info'
      }
      return colorMap[result] || 'info'
    },
    
    // 获取对比结果图标
    getComparisonResultIcon(result) {
      const iconMap = {
        'BETTER': 'el-icon-top',
        'WORSE': 'el-icon-bottom',
        'SIMILAR': 'el-icon-minus'
      }
      return iconMap[result] || 'el-icon-minus'
    },
    
    // 获取对比结果文本
    getComparisonResultText(result) {
      const textMap = {
        'BETTER': '优于基准',
        'WORSE': '劣于基准',
        'SIMILAR': '接近基准'
      }
      return textMap[result] || result
    }
  }
}
</script>

<style lang="scss" scoped>
.comparison-analysis {
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
      
      &.comparison-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.significant-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
        color: white;
      }
      
      &.accuracy-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.coverage-card {
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
          margin-bottom: 2px;
        }
        
        .stat-description {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }
        
        .stat-trend {
          font-size: 12px;
          opacity: 0.9;
          
          .trend-up {
            color: #F56C6C;
          }
          
          .trend-down {
            color: #67C23A;
          }
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
  
  .search-card {
    .search-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      
      .search-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
      }
    }
  }
  
  .chart-row {
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
        height: 350px;
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
  
  .positive-difference,
  .positive-change {
    color: #67C23A;
    font-weight: 500;
  }
  
  .negative-difference,
  .negative-change {
    color: #F56C6C;
    font-weight: 500;
  }
  
  .zero-difference,
  .zero-change {
    color: #909399;
    font-weight: 500;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .text-right {
    text-align: right;
  }
  
  .detail-content {
    padding: 20px;
    
    .statistics-content {
      margin-top: 20px;
    }
  }
}
</style>
