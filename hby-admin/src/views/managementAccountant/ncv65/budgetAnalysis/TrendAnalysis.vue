<template>
  <div class="trend-analysis">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算趋势分析</h2>
      <p>分析预算执行的历史趋势和未来预测，提供多维度趋势分析和预测建议</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-trend-charts" @click="handleCreateTrendAnalysis">创建趋势分析</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefreshTrend">刷新趋势</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExportTrend">导出趋势</el-button>
            <el-button type="info" icon="el-icon-data-line" @click="handleForecast">趋势预测</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleTrendSettings">趋势设置</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 趋势分析概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card growth-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ trendStats.averageGrowthRate }}%</div>
            <div class="stat-label">平均增长率</div>
            <div class="stat-description">同比平均增长率</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>较上期增长3.2%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-trend-charts"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card volatility-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ trendStats.completedTrends }}</div>
            <div class="stat-label">已完成分析</div>
            <div class="stat-description">已完成趋势分析数</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-down trend-down"></i>
              <span>较上期下降1.8%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-data-line"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card correlation-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ trendStats.totalTrends }}</div>
            <div class="stat-label">分析总数</div>
            <div class="stat-description">趋势分析记录总数</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>相关性增强</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-connection"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card forecast-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ trendStats.forecastAccuracy }}%</div>
            <div class="stat-label">预测准确率</div>
            <div class="stat-description">趋势预测准确率</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>准确率提升2.5%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-aim"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 趋势分析条件 -->
    <el-card class="search-card" shadow="never">
      <div class="search-header">
        <span class="search-title">趋势分析条件</span>
        <el-button type="text" @click="handleResetConditions">重置条件</el-button>
      </div>
      <el-form :model="trendForm" :inline="true" size="small">
        <el-form-item label="分析周期">
          <el-select
            v-model="trendForm.analysisType"
            placeholder="请选择分析周期"
            style="width: 150px"
          >
            <el-option value="MONTHLY" label="月度趋势" />
            <el-option value="QUARTERLY" label="季度趋势" />
            <el-option value="YEARLY" label="年度趋势" />
            <el-option value="CUSTOM" label="自定义" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="trendForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="组织单元">
          <el-cascader
            v-model="trendForm.organizationPath"
            :options="organizationOptions"
            :props="{ checkStrictly: true, emitPath: false }"
            placeholder="请选择组织单元"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="预算科目">
          <el-select
            v-model="trendForm.budgetAccount"
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
        <el-form-item label="趋势指标">
          <el-select
            v-model="trendForm.trendIndicator"
            placeholder="请选择趋势指标"
            style="width: 150px"
          >
            <el-option value="BUDGET_AMOUNT" label="预算金额" />
            <el-option value="ACTUAL_AMOUNT" label="实际金额" />
            <el-option value="EXECUTION_RATE" label="执行率" />
            <el-option value="VARIANCE_RATE" label="差异率" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleAnalyzeTrend">分析趋势</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 趋势图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card class="main-chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>趋势分析图表</span>
            <div class="header-tools">
              <el-radio-group v-model="chartViewType" size="mini">
                <el-radio-button label="line">线性图</el-radio-button>
                <el-radio-button label="bar">柱状图</el-radio-button>
                <el-radio-button label="area">面积图</el-radio-button>
                <el-radio-button label="combined">组合图</el-radio-button>
              </el-radio-group>
              <el-button icon="el-icon-full-screen" size="mini" @click="handleFullScreen">全屏</el-button>
            </div>
          </div>
          <div id="mainTrendChart" class="main-chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 子图表区域 -->
    <el-row :gutter="20" class="sub-chart-row">
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>增长率趋势</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshGrowthChart" />
          </div>
          <div id="growthTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>波动率分析</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshVolatilityChart" />
          </div>
          <div id="volatilityChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>预测分析</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshForecastChart" />
          </div>
          <div id="forecastChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 趋势分析结果 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">趋势分析结果</span>
        <div class="table-tools">
          <el-tooltip content="实时更新" placement="top">
            <el-switch
              v-model="realTimeUpdate"
              active-text="实时更新"
              @change="handleRealTimeUpdateChange"
            />
          </el-tooltip>
          <el-tooltip content="显示预测" placement="top">
            <el-switch
              v-model="showForecast"
              active-text="显示预测"
              @change="handleShowForecastChange"
            />
          </el-tooltip>
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getTrendResults" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="trendResults"
        border
        stripe
        highlight-current-row
        @sort-change="handleSortChange"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />

        <!-- 时间周期：startPeriod ~ endPeriod -->
        <el-table-column label="时间周期" width="160" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.startPeriod || '-' }} ~ {{ scope.row.endPeriod || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="organizationName" label="组织单元" width="150" show-overflow-tooltip />
        <!-- 预算科目：accountName -->
        <el-table-column prop="accountName" label="预算科目" width="130" show-overflow-tooltip />
        <el-table-column prop="trendType" label="趋势类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" type="info">{{ trendTypeText(scope.row.trendType) }}</el-tag>
          </template>
        </el-table-column>

        <!-- 当前值：averageValue -->
        <el-table-column label="当前值(均值)" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ fmtNum(scope.row.averageValue) }}</span>
          </template>
        </el-table-column>

        <!-- 上期值：minValue -->
        <el-table-column label="最小值" width="110" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ fmtNum(scope.row.minValue) }}</span>
          </template>
        </el-table-column>

        <!-- 变化量：maxValue - minValue -->
        <el-table-column label="最大值" width="110" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ fmtNum(scope.row.maxValue) }}</span>
          </template>
        </el-table-column>

        <!-- 变化率：growthRate -->
        <el-table-column label="增长率" width="100" align="right">
          <template slot-scope="scope">
            <span :class="getChangeRateClass(scope.row.growthRate)">
              {{ formatChangeRate(scope.row.growthRate) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column prop="trendDirection" label="趋势方向" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTrendDirectionColor(scope.row.trendDirection)" size="mini">
              <i :class="getTrendDirectionIcon(scope.row.trendDirection)"></i>
              {{ getTrendDirectionText(scope.row.trendDirection) }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 预测值：trendStrength 作为趋势强度展示 -->
        <el-table-column label="趋势强度" width="100" align="center" v-if="showForecast">
          <template slot-scope="scope">
            <el-tag size="mini" :type="strengthType(scope.row.trendStrength)">
              {{ strengthText(scope.row.trendStrength) }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 置信度：根据 trendStrength 换算 -->
        <el-table-column label="置信度" width="110" align="center" v-if="showForecast">
          <template slot-scope="scope">
            <el-progress
              :percentage="strengthConfidence(scope.row.trendStrength)"
              :stroke-width="6"
              :show-text="false"
              :color="getConfidenceColor(strengthConfidence(scope.row.trendStrength))"
            />
            <span class="confidence-text">{{ strengthConfidence(scope.row.trendStrength) }}%</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="230" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-view"
              @click="handleViewTrendDetail(scope.row)">详情</el-button>
            <el-button type="text" size="mini" icon="el-icon-edit"
              @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="mini" icon="el-icon-delete" style="color:#F56C6C"
              @click="handleDelete(scope.row)">删除</el-button>
            <el-dropdown trigger="click" @command="(cmd) => handleCommand(cmd, scope.row)">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="drill" icon="el-icon-data-line">钻取分析</el-dropdown-item>
                <el-dropdown-item command="forecast" icon="el-icon-aim">预测分析</el-dropdown-item>
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

    <!-- 趋势详情抽屉 -->
    <el-drawer
      title="趋势详情分析"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="60%"
    >
      <div class="detail-content" v-if="currentTrendDetail" style="padding:20px">
        <el-tabs v-model="activeTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="趋势基本信息" :column="2" border>
              <el-descriptions-item label="分析名称">{{ currentTrendDetail.analysisName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="趋势类型">{{ trendTypeText(currentTrendDetail.trendType) }}</el-descriptions-item>
              <el-descriptions-item label="时间周期">{{ currentTrendDetail.startPeriod || '-' }} ~ {{ currentTrendDetail.endPeriod || '-' }}</el-descriptions-item>
              <el-descriptions-item label="预算年度">{{ currentTrendDetail.budgetYear || '-' }}</el-descriptions-item>
              <el-descriptions-item label="组织单元">{{ currentTrendDetail.organizationName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="预算科目">{{ currentTrendDetail.accountName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="趋势方向">
                <el-tag :type="getTrendDirectionColor(currentTrendDetail.trendDirection)" size="mini">
                  {{ getTrendDirectionText(currentTrendDetail.trendDirection) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="趋势强度">
                <el-tag :type="strengthType(currentTrendDetail.trendStrength)" size="mini">
                  {{ strengthText(currentTrendDetail.trendStrength) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="分析状态">{{ currentTrendDetail.analysisStatus || '-' }}</el-descriptions-item>
              <el-descriptions-item label="分析人">{{ currentTrendDetail.analyzedBy || '-' }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>

          <el-tab-pane label="趋势图表" name="chart">
            <div id="detailTrendChart" style="height: 400px; width: 100%;"></div>
          </el-tab-pane>

          <el-tab-pane label="统计分析" name="statistics">
            <div class="statistics-content">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-card shadow="never">
                    <div slot="header">数值统计</div>
                    <el-descriptions :column="1" border>
                      <el-descriptions-item label="均值">{{ fmtNum(currentTrendDetail.averageValue) }}</el-descriptions-item>
                      <el-descriptions-item label="最大值">{{ fmtNum(currentTrendDetail.maxValue) }}</el-descriptions-item>
                      <el-descriptions-item label="最小值">{{ fmtNum(currentTrendDetail.minValue) }}</el-descriptions-item>
                      <el-descriptions-item label="增长率">{{ formatChangeRate(currentTrendDetail.growthRate) }}</el-descriptions-item>
                    </el-descriptions>
                  </el-card>
                </el-col>
                <el-col :span="12">
                  <el-card shadow="never">
                    <div slot="header">分析说明</div>
                    <el-descriptions :column="1" border>
                      <el-descriptions-item label="趋势方向">{{ getTrendDirectionText(currentTrendDetail.trendDirection) }}</el-descriptions-item>
                      <el-descriptions-item label="趋势强度">{{ strengthText(currentTrendDetail.trendStrength) }}</el-descriptions-item>
                      <el-descriptions-item label="分析描述">{{ currentTrendDetail.analysisDescription || '-' }}</el-descriptions-item>
                      <el-descriptions-item label="预测建议">{{ currentTrendDetail.forecastSuggestion || '-' }}</el-descriptions-item>
                    </el-descriptions>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>
    <!-- 设置对话框 -->
    <el-dialog title="趋势分析设置" :visible.sync="settingsDialogVisible" width="500px">
      <el-form label-width="120px" size="small">
        <el-form-item label="每页显示条数">
          <el-select v-model="queryParams.pageSize" style="width: 100%">
            <el-option label="10条" :value="10" />
            <el-option label="20条" :value="20" />
            <el-option label="50条" :value="50" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false; $message.success('设置已保存')">保存</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="趋势分析帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>趋势分析用于追踪预算执行的时间变化趋势，帮助预测未来走势。</p>
        <h4>操作指南</h4>
        <p>1. 使用顶部工具栏的按钮进行创建、刷新和导出操作。</p>
        <p>2. 使用筛选条件缩小分析范围。</p>
        <p>3. 点击表格行查看详细信息。</p>
      </div>
      <div slot="footer"><el-button type="primary" @click="helpDialogVisible = false">我知道了</el-button></div>
    </el-dialog>

    <!-- 创建趋势分析对话框 -->
    <el-dialog title="创建趋势分析" :visible.sync="createDialogVisible" width="620px" @close="resetCreateForm">
      <el-form :model="createForm" :rules="createRules" ref="createFormRef" label-width="100px" size="small">
        <el-form-item label="分析名称" prop="analysisName">
          <el-input v-model="createForm.analysisName" placeholder="请输入分析名称" />
        </el-form-item>
        <el-form-item label="趋势类型" prop="trendType">
          <el-select v-model="createForm.trendType" placeholder="请选择" style="width:100%">
            <el-option value="MONTHLY" label="月度趋势" />
            <el-option value="QUARTERLY" label="季度趋势" />
            <el-option value="YEARLY" label="年度趋势" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算年度" prop="budgetYear">
          <el-date-picker v-model="createForm.budgetYear" type="year" value-format="yyyy" placeholder="请选择年度" style="width:100%" />
        </el-form-item>
        <el-form-item label="组织单元" prop="organizationId">
          <el-select v-model="createForm.organizationId" placeholder="请选择组织单元" filterable clearable style="width:100%"
            @change="onCreateOrgChange">
            <el-option
              v-for="org in organizationFlatOptions"
              :key="org.value"
              :label="org.label"
              :value="org.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预算科目" prop="accountId">
          <el-select v-model="createForm.accountId" placeholder="请选择预算科目" filterable clearable style="width:100%"
            @change="onCreateAccountChange">
            <el-option
              v-for="item in budgetAccountOptions"
              :key="item.value || item.id"
              :label="item.label || item.name"
              :value="item.value || item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker v-model="createForm.dateRange" type="daterange" range-separator="至"
            start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="createForm.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="createLoading" @click="submitCreateForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 趋势预测对话框 -->
    <el-dialog title="趋势预测分析" :visible.sync="forecastDialogVisible" width="800px">
      <el-form :model="forecastForm" :inline="true" size="small" style="margin-bottom:16px">
        <el-form-item label="预测周期">
          <el-select v-model="forecastForm.forecastPeriods" style="width:120px">
            <el-option :value="3" label="未来3期" />
            <el-option :value="6" label="未来6期" />
            <el-option :value="12" label="未来12期" />
          </el-select>
        </el-form-item>
        <el-form-item label="预测方法">
          <el-select v-model="forecastForm.method" style="width:140px">
            <el-option value="LINEAR" label="线性回归" />
            <el-option value="MOVING_AVG" label="移动平均" />
            <el-option value="EXPONENTIAL" label="指数平滑" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="forecastLoading" @click="runForecast">开始预测</el-button>
        </el-form-item>
      </el-form>
      <div id="forecastDialogChart" style="height:350px;"></div>
      <div v-if="forecastResult.length > 0" style="margin-top:16px">
        <el-table :data="forecastResult" border size="small" max-height="200">
          <el-table-column prop="period" label="预测周期" width="120" align="center" />
          <el-table-column prop="forecastValue" label="预测值" align="right">
            <template slot-scope="scope">{{ formatTrendValue(scope.row.forecastValue, trendForm.trendIndicator) }}</template>
          </el-table-column>
          <el-table-column prop="lowerBound" label="下限" align="right">
            <template slot-scope="scope">{{ formatTrendValue(scope.row.lowerBound, trendForm.trendIndicator) }}</template>
          </el-table-column>
          <el-table-column prop="upperBound" label="上限" align="right">
            <template slot-scope="scope">{{ formatTrendValue(scope.row.upperBound, trendForm.trendIndicator) }}</template>
          </el-table-column>
          <el-table-column prop="confidence" label="置信度" width="100" align="center">
            <template slot-scope="scope">{{ scope.row.confidence }}%</template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="forecastDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="exportForecastResult" :disabled="forecastResult.length === 0">导出预测结果</el-button>
      </div>
    </el-dialog>

    <!-- 编辑趋势分析对话框 -->
    <el-dialog title="编辑趋势分析" :visible.sync="editDialogVisible" width="620px">
      <el-form :model="editForm" :rules="createRules" ref="editFormRef" label-width="100px" size="small">
        <el-form-item label="分析名称" prop="analysisName">
          <el-input v-model="editForm.analysisName" placeholder="请输入分析名称" />
        </el-form-item>
        <el-form-item label="趋势类型" prop="trendType">
          <el-select v-model="editForm.trendType" style="width:100%">
            <el-option value="MONTHLY" label="月度趋势" />
            <el-option value="QUARTERLY" label="季度趋势" />
            <el-option value="YEARLY" label="年度趋势" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算年度" prop="budgetYear">
          <el-date-picker v-model="editForm.budgetYear" type="year" value-format="yyyy" style="width:100%" />
        </el-form-item>
        <el-form-item label="组织单元" prop="organizationId">
          <el-select v-model="editForm.organizationId" filterable clearable style="width:100%" @change="onEditOrgChange">
            <el-option v-for="org in organizationFlatOptions" :key="org.value" :label="org.label" :value="org.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算科目" prop="accountId">
          <el-select v-model="editForm.accountId" filterable clearable style="width:100%" @change="onEditAccountChange">
            <el-option v-for="item in budgetAccountOptions" :key="item.value||item.id" :label="item.label||item.name" :value="item.value||item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="editForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="editLoading" @click="submitEditForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { budgetAnalysisApi } from '@/api/managementAccountant/ncv65/budgetAnalysis'

export default {
  name: 'TrendAnalysis',
  data() {
    return {
      // 趋势分析条件（analysisType 默认为空，不过滤类型，查全部）
      trendForm: {
        analysisType: '',
        dateRange: [],
        organizationPath: '',
        budgetAccount: '',
        trendIndicator: 'EXECUTION_RATE'
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      trendResults: [],
      total: 0,
      
      // 图表类型
      chartViewType: 'line',
      // 缓存后端返回的图表数据，切换图表类型时无需重新请求
      cachedChartData: null,
      
      // 控制开关
      realTimeUpdate: false,
      showForecast: true,
      updateTimer: null,
      
      // 统计数据（字段名与后端 getTrendStats 返回保持一致）
      trendStats: {
        averageGrowthRate: 0,
        completedTrends: 0,
        totalTrends: 0,
        forecastAccuracy: 0
      },
      
      // 抽屉
      detailDrawerVisible: false,
      currentTrendDetail: null,
      activeTab: 'basic',
      
      // 选项数据
      organizationOptions: [],
      budgetAccountOptions: [],

      // 对话框控制
      createDialogVisible: false,
      editDialogVisible: false,
      settingsDialogVisible: false,
      helpDialogVisible: false,
      forecastDialogVisible: false,

      // 创建表单
      createLoading: false,
      createForm: {
        analysisName: '',
        trendType: 'MONTHLY',
        budgetYear: new Date().getFullYear().toString(),
        organizationId: '',
        organizationName: '',
        accountId: '',
        accountName: '',
        dateRange: [],
        remark: ''
      },
      createRules: {
        analysisName: [{ required: true, message: '请输入分析名称', trigger: 'blur' }],
        trendType: [{ required: true, message: '请选择趋势类型', trigger: 'change' }],
        budgetYear: [{ required: true, message: '请选择预算年度', trigger: 'change' }],
        organizationId: [{ required: true, message: '请选择组织单元', trigger: 'change' }],
        accountId: [{ required: true, message: '请选择预算科目', trigger: 'change' }]
      },

      // 编辑表单
      editLoading: false,
      editForm: {
        id: '',
        analysisName: '',
        trendType: 'MONTHLY',
        budgetYear: '',
        organizationId: '',
        organizationName: '',
        accountId: '',
        accountName: '',
        remark: ''
      },

      // 预测对话框
      forecastLoading: false,
      forecastForm: {
        forecastPeriods: 6,
        method: 'LINEAR'
      },
      forecastResult: []
    }
  },
  
  mounted() {
    this.getTrendResults()
    this.loadOrganizationOptions()
    this.loadBudgetAccountOptions()
    this.loadTrendStats()
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  
  beforeDestroy() {
    if (this.updateTimer) {
      clearInterval(this.updateTimer)
    }
    // 销毁所有 ECharts 实例，防止内存泄漏
    const chartIds = ['mainTrendChart', 'growthTrendChart', 'volatilityChart', 'forecastChart', 'detailTrendChart', 'forecastDialogChart']
    chartIds.forEach(id => {
      const dom = document.getElementById(id)
      if (dom) {
        const instance = echarts.getInstanceByDom(dom)
        if (instance) instance.dispose()
      }
    })
  },

  computed: {
    // 将树形组织选项展平，供创建/编辑表单的 el-select 使用
    organizationFlatOptions() {
      const flatten = (nodes) => {
        let result = []
        for (const node of nodes || []) {
          result.push({ value: node.value, label: node.label, id: node.id, name: node.name })
          if (node.children && node.children.length > 0) {
            result = result.concat(flatten(node.children))
          }
        }
        return result
      }
      return flatten(this.organizationOptions)
    }
  },

  watch: {
    // 详情抽屉切换到"趋势图表" tab 时才初始化图表（此时 DOM 才存在）
    activeTab(val) {
      if (val === 'chart') {
        this.$nextTick(() => {
          this.initDetailChart()
        })
      }
    },
    // 切换图表类型时，用缓存数据重新渲染主图表
    chartViewType() {
      if (this.cachedChartData) {
        this.renderMainChart(this.cachedChartData.mainChart || {})
      }
    }
  },

  methods: {
    // 获取趋势结果
    async getTrendResults() {
      this.loading = true
      try {
        const params = {
          ...this.trendForm,
          ...this.queryParams
        }
        const response = await budgetAnalysisApi.getTrendAnalysis(params)
        this.trendResults = response.data.tlist || response.data.list || []
        this.total = response.data.totalRecord || response.data.total || 0
      } catch (error) {
        this.$message.error('获取趋势分析结果失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载组织选项（树形结构，供 el-cascader 使用）
    async loadOrganizationOptions() {
      try {
        const response = await budgetAnalysisApi.getTrendOrganizations()
        if (response.code === 1 && response.data) {
          this.organizationOptions = response.data
        }
      } catch (error) {
        console.error('加载组织选项失败：', error)
      }
    },

    // 加载预算科目选项（平铺列表，供 el-select 使用）
    async loadBudgetAccountOptions() {
      try {
        const response = await budgetAnalysisApi.getTrendBudgetAccounts()
        if (response.code === 1 && response.data) {
          this.budgetAccountOptions = response.data
        }
      } catch (error) {
        console.error('加载预算科目选项失败：', error)
      }
    },
    
    // 初始化图表 —— 直接从后端加载数据并渲染，不再先初始化空图表
    initCharts() {
      this.$nextTick(() => {
        this.loadTrendChartData()
      })
    },

    // 加载统计数据
    async loadTrendStats() {
      try {
        const response = await budgetAnalysisApi.getTrendStats()
        if (response.code === 1 && response.data) {
          this.trendStats = { ...this.trendStats, ...response.data }
        }
      } catch (error) {
        console.error('加载趋势统计数据失败：', error)
      }
    },

    // 加载图表数据（从后端获取真实数据后完整替换图表 option）
    async loadTrendChartData() {
      try {
        const response = await budgetAnalysisApi.getTrendChartData(this.trendForm)
        if (response.code === 1 && response.data) {
          this.cachedChartData = response.data
          const d = response.data
          // 主图表
          this.renderMainChart(d.mainChart || {})
          // 增长率图
          this.renderGrowthChart(d.growthChart || {})
          // 波动率图
          this.renderVolatilityChart(d.volatilityChart || {})
          // 预测图
          this.renderForecastChart(d.forecastChart || {})
        }
      } catch (error) {
        console.error('加载趋势图表数据失败：', error)
      }
    },

    /**
     * 渲染主图表 —— 根据 chartViewType 决定 series 的 type / areaStyle
     * 支持 line / bar / area / combined 四种模式
     */
    renderMainChart(mc) {
      const mainDom = document.getElementById('mainTrendChart')
      if (!mainDom) return
      const chart = echarts.getInstanceByDom(mainDom) || echarts.init(mainDom)
      const seriesNames = ['平均值', '最大值', '增长率']
      const seriesColors = ['#409EFF', '#67C23A', '#E6A23C']
      const vt = this.chartViewType

      const buildType = (idx) => {
        if (vt === 'combined') return idx === 2 ? 'line' : 'bar'
        if (vt === 'area') return 'line'
        if (vt === 'bar') return 'bar'
        return 'line'
      }
      const buildAreaStyle = (idx) => {
        if (vt === 'area') return { opacity: 0.35 }
        return null
      }

      chart.setOption({
        title: { text: '预算执行趋势分析', left: 'center' },
        tooltip: { trigger: 'axis', axisPointer: { type: 'cross' } },
        legend: { data: seriesNames, top: 30 },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', boundaryGap: vt === 'bar' || vt === 'combined', data: mc.xAxis || [] },
        yAxis: [
          { type: 'value', name: '数值', position: 'left' },
          { type: 'value', name: '增长率(%)', position: 'right' }
        ],
        series: (mc.series || []).map((s, i) => ({
          name: s.name || seriesNames[i] || `系列${i + 1}`,
          type: buildType(i),
          yAxisIndex: i === 2 ? 1 : 0,
          data: s.data || [],
          smooth: true,
          areaStyle: buildAreaStyle(i),
          itemStyle: { color: seriesColors[i] || '#409EFF' }
        }))
      }, true)
      chart.resize()
    },

    // 渲染增长率图
    renderGrowthChart(gc) {
      const dom = document.getElementById('growthTrendChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      chart.setOption({
        title: { text: '增长率趋势', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: gc.xAxis || [] },
        yAxis: { type: 'value', name: '增长率(%)' },
        series: (gc.series || []).map(s => ({
          name: s.name || '增长率',
          type: 'line',
          data: s.data || [],
          smooth: true,
          itemStyle: { color: '#409EFF' },
          areaStyle: { opacity: 0.15 }
        }))
      }, true)
      chart.resize()
    },

    // 渲染波动率图
    renderVolatilityChart(vc) {
      const dom = document.getElementById('volatilityChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      chart.setOption({
        title: { text: '波动率分析', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: vc.xAxis || [] },
        yAxis: { type: 'value', name: '波动率(%)' },
        series: (vc.series || []).map(s => ({
          name: s.name || '波动率',
          type: 'bar',
          data: s.data || [],
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#E6A23C' },
              { offset: 1, color: '#EEBE77' }
            ])
          }
        }))
      }, true)
      chart.resize()
    },

    // 渲染预测图
    renderForecastChart(fc) {
      const dom = document.getElementById('forecastChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const seriesColors = ['#67C23A', '#F56C6C']
      chart.setOption({
        title: { text: '预测分析', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: ['历史值', '预测值'], bottom: 0 },
        xAxis: { type: 'category', data: fc.xAxis || [] },
        yAxis: { type: 'value', name: '执行率(%)' },
        series: (fc.series || []).map((s, i) => ({
          name: s.name || (i === 0 ? '历史值' : '预测值'),
          type: 'line',
          data: s.data || [],
          itemStyle: { color: seriesColors[i] || '#409EFF' },
          lineStyle: i === 1 ? { type: 'dashed' } : {},
          areaStyle: i === 0 ? { opacity: 0.1 } : null
        }))
      }, true)
      chart.resize()
    },


    // 分析趋势
    handleAnalyzeTrend() {
      this.queryParams.pageNum = 1
      this.getTrendResults()
      this.initCharts()
    },
    
    // 重置
    handleReset() {
      this.trendForm = {
        analysisType: '',
        dateRange: [],
        organizationPath: '',
        budgetAccount: '',
        trendIndicator: 'EXECUTION_RATE'
      }
      this.handleAnalyzeTrend()
    },
    
    // 重置条件
    handleResetConditions() {
      this.handleReset()
    },
    
    // 实时更新切换
    handleRealTimeUpdateChange(value) {
      if (value) {
        this.updateTimer = setInterval(() => {
          this.getTrendResults()
          this.initCharts()
        }, 60000) // 1分钟更新一次
        this.$message.success('已开启实时更新')
      } else {
        if (this.updateTimer) {
          clearInterval(this.updateTimer)
          this.updateTimer = null
        }
        this.$message.info('已关闭实时更新')
      }
    },
    
    // 显示预测切换
    handleShowForecastChange(value) {
      this.$message.info(value ? '已显示预测值' : '已隐藏预测值')
    },
    
    // 创建趋势分析
    handleCreateTrendAnalysis() {
      this.createDialogVisible = true
    },
    
    // 刷新趋势
    handleRefreshTrend() {
      this.getTrendResults()
      this.initCharts()
    },
    
    // 导出趋势
    async handleExportTrend() {
      try {
        const params = { ...this.trendForm }
        const response = await budgetAnalysisApi.exportTrendAnalysis(params)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '趋势分析报告.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 趋势预测
    handleForecast() {
      this.forecastResult = []
      this.forecastDialogVisible = true
    },
    
    // 趋势设置
    handleTrendSettings() {
      this.settingsDialogVisible = true
    },
    
    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },
    
    // 全屏 —— 对整个卡片容器全屏，并在进入/退出时 resize 图表
    handleFullScreen() {
      const card = this.$el.querySelector('.main-chart-card')
      if (!card) return
      const chartDom = document.getElementById('mainTrendChart')
      const resizeChart = () => {
        if (chartDom) {
          const chart = echarts.getInstanceByDom(chartDom)
          if (chart) {
            setTimeout(() => chart.resize(), 200)
          }
        }
      }
      if (!document.fullscreenElement) {
        const req = card.requestFullscreen || card.webkitRequestFullscreen || card.mozRequestFullScreen
        if (req) {
          req.call(card).then(resizeChart).catch(() => {})
        }
      } else {
        document.exitFullscreen().then(resizeChart).catch(() => {})
      }
      // 监听 fullscreenchange 以便退出时也 resize
      const onFsChange = () => {
        resizeChart()
        if (!document.fullscreenElement) {
          document.removeEventListener('fullscreenchange', onFsChange)
        }
      }
      document.addEventListener('fullscreenchange', onFsChange)
    },
    
    // 刷新增长图表（重新加载数据）
    refreshGrowthChart() {
      if (this.cachedChartData) {
        this.renderGrowthChart(this.cachedChartData.growthChart || {})
      } else {
        this.loadTrendChartData()
      }
    },

    // 刷新波动率图表（重新加载数据）
    refreshVolatilityChart() {
      if (this.cachedChartData) {
        this.renderVolatilityChart(this.cachedChartData.volatilityChart || {})
      } else {
        this.loadTrendChartData()
      }
    },

    // 刷新预测图表（重新加载数据）
    refreshForecastChart() {
      if (this.cachedChartData) {
        this.renderForecastChart(this.cachedChartData.forecastChart || {})
      } else {
        this.loadTrendChartData()
      }
    },
    
    // 查看趋势详情
    handleViewTrendDetail(row) {
      this.currentTrendDetail = row
      this.activeTab = 'basic'   // 重置到基本信息 tab，切换到图表 tab 时由 watch 初始化图表
      this.detailDrawerVisible = true
    },
    
    // 钻取分析 → 打开详情抽屉
    handleDrillDown(row) {
      this.handleViewTrendDetail(row)
    },
    
    // 初始化详情图表（使用真实数据，DOM 必须存在时才调用）
    initDetailChart() {
      const chartDom = document.getElementById('detailTrendChart')
      if (!chartDom) return
      const detail = this.currentTrendDetail || {}
      // 用当前记录的数值字段构造图表数据
      const labels = ['均值', '最大值', '最小值', '增长率(%)']
      const values = [
        parseFloat(detail.averageValue) || 0,
        parseFloat(detail.maxValue) || 0,
        parseFloat(detail.minValue) || 0,
        parseFloat(detail.growthRate) || 0
      ]
      const myChart = echarts.getInstanceByDom(chartDom) || echarts.init(chartDom)
      myChart.setOption({
        title: {
          text: detail.analysisName || '详细趋势图',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: labels
        },
        yAxis: { type: 'value' },
        series: [
          {
            name: '趋势指标',
            type: 'bar',
            data: values,
            itemStyle: { color: '#409EFF' },
            label: { show: true, position: 'top' }
          }
        ]
      }, true)
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'drill':
          this.handleViewTrendDetail(row)
          break
        case 'forecast':
          this.handleForecastAnalysis(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
      }
    },

    // 编辑
    handleEdit(row) {
      this.editForm = {
        id: row.id,
        analysisName: row.analysisName || '',
        trendType: row.trendType || 'MONTHLY',
        budgetYear: row.budgetYear ? String(row.budgetYear) : '',
        organizationId: row.organizationId || '',
        organizationName: row.organizationName || '',
        accountId: row.accountId || '',
        accountName: row.accountName || '',
        remark: row.remark || ''
      }
      this.editDialogVisible = true
    },

    // 提交编辑
    submitEditForm() {
      this.$refs.editFormRef.validate(async (valid) => {
        if (!valid) return
        this.editLoading = true
        try {
          await budgetAnalysisApi.updateTrendAnalysis(this.editForm)
          this.$message.success('更新成功')
          this.editDialogVisible = false
          this.getTrendResults()
          this.loadTrendStats()
        } catch (error) {
          this.$message.error('更新失败：' + error.message)
        } finally {
          this.editLoading = false
        }
      })
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm(`确定删除「${row.analysisName || row.id}」吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
      } catch {
        return // 用户点取消
      }
      try {
        const res = await budgetAnalysisApi.deleteTrendAnalysis(row.id)
        if (res && res.code === 1) {
          this.$message.success('删除成功')
          this.getTrendResults()
          this.loadTrendStats()
        } else {
          this.$message.error((res && res.msg) || '删除失败')
        }
      } catch (error) {
        this.$message.error('删除失败：' + (error.message || error))
      }
    },

    // 编辑时组织单元变化
    onEditOrgChange(val) {
      const opt = this.organizationFlatOptions.find(o => o.value === val)
      this.editForm.organizationName = opt ? opt.name : ''
    },

    // 编辑时预算科目变化
    onEditAccountChange(val) {
      const opt = this.budgetAccountOptions.find(o => (o.value || o.id) === val)
      this.editForm.accountName = opt ? (opt.label || opt.name) : ''
    },
    
    // 预测分析 → 打开预测对话框
    handleForecastAnalysis(row) {
      this.forecastResult = []
      this.forecastDialogVisible = true
    },

    // 对比分析 → 暂时查看详情
    handleCompareAnalysis(row) {
      this.handleViewTrendDetail(row)
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        await budgetAnalysisApi.exportSingleTrend(row.id)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 行点击
    handleRowClick(row) {
      this.handleViewTrendDetail(row)
    },
    
    // 排序改变
    handleSortChange({ column, prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getTrendResults()
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getTrendResults()
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getTrendResults()
    },
    
    // 格式化趋势值
    formatTrendValue(value, indicator) {
      if (!value) return '0.00'
      if (indicator === 'EXECUTION_RATE' || indicator === 'VARIANCE_RATE') {
        return `${parseFloat(value).toFixed(2)}%`
      }
      return parseFloat(value).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 格式化变化量
    formatChangeAmount(amount, indicator) {
      if (!amount) return '0.00'
      const formatted = this.formatTrendValue(Math.abs(amount), indicator)
      return amount >= 0 ? `+${formatted}` : `-${formatted}`
    },
    
    // 格式化变化率
    formatChangeRate(rate) {
      if (!rate) return '0.00%'
      const formatted = `${parseFloat(Math.abs(rate)).toFixed(2)}%`
      return rate >= 0 ? `+${formatted}` : `-${formatted}`
    },
    
    // 获取变化量样式类
    getChangeAmountClass(amount) {
      if (amount > 0) return 'positive-change'
      if (amount < 0) return 'negative-change'
      return 'zero-change'
    },
    
    // 获取变化率样式类
    getChangeRateClass(rate) {
      if (rate > 0) return 'positive-change'
      if (rate < 0) return 'negative-change'
      return 'zero-change'
    },
    
    // 获取趋势方向颜色
    getTrendDirectionColor(direction) {
      const colorMap = {
        'UP': 'success',
        'DOWN': 'danger',
        'STABLE': 'info'
      }
      return colorMap[direction] || 'info'
    },
    
    // 获取趋势方向图标
    getTrendDirectionIcon(direction) {
      const iconMap = {
        'UP': 'el-icon-top',
        'DOWN': 'el-icon-bottom',
        'STABLE': 'el-icon-minus'
      }
      return iconMap[direction] || 'el-icon-minus'
    },
    
    // 获取趋势方向文本
    getTrendDirectionText(direction) {
      const textMap = {
        'UP': '上升',
        'DOWN': '下降',
        'STABLE': '稳定'
      }
      return textMap[direction] || direction
    },
    
    // 趋势类型中文
    trendTypeText(type) {
      const map = { MONTHLY: '月度', QUARTERLY: '季度', YEARLY: '年度' }
      return map[type] || type || '-'
    },

    // 格式化数字（两位小数）
    fmtNum(val) {
      if (val === null || val === undefined || val === '') return '-'
      return parseFloat(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },

    // 趋势强度 tag 类型
    strengthType(s) {
      return { strong: 'danger', moderate: 'warning', weak: 'info' }[s] || 'info'
    },

    // 趋势强度中文
    strengthText(s) {
      return { strong: '强', moderate: '中', weak: '弱' }[s] || (s || '-')
    },

    // 趋势强度换算置信度
    strengthConfidence(s) {
      return { strong: 90, moderate: 75, weak: 60 }[s] || 60
    },

    // 获取置信度颜色
    getConfidenceColor(confidence) {
      if (confidence >= 90) return '#67C23A'
      if (confidence >= 70) return '#E6A23C'
      return '#F56C6C'
    },

    // 重置创建表单
    resetCreateForm() {
      this.createForm = {
        analysisName: '',
        trendType: 'MONTHLY',
        budgetYear: new Date().getFullYear().toString(),
        organizationId: '',
        organizationName: '',
        accountId: '',
        accountName: '',
        dateRange: [],
        remark: ''
      }
      this.$refs.createFormRef && this.$refs.createFormRef.clearValidate()
    },

    // 组织单元选择时同步保存 name
    onCreateOrgChange(val) {
      const opt = this.organizationFlatOptions.find(o => o.value === val)
      this.createForm.organizationName = opt ? opt.name : ''
    },

    // 预算科目选择时同步保存 name
    onCreateAccountChange(val) {
      const opt = this.budgetAccountOptions.find(o => (o.value || o.id) === val)
      this.createForm.accountName = opt ? (opt.label || opt.name) : ''
    },

    // 提交创建表单
    submitCreateForm() {
      this.$refs.createFormRef.validate(async (valid) => {
        if (!valid) return
        this.createLoading = true
        try {
          const params = {
            ...this.createForm,
            startDate: this.createForm.dateRange && this.createForm.dateRange[0],
            endDate: this.createForm.dateRange && this.createForm.dateRange[1]
          }
          await budgetAnalysisApi.createTrendAnalysis(params)
          this.$message.success('创建成功')
          this.createDialogVisible = false
          this.getTrendResults()
          this.loadTrendStats()
        } catch (error) {
          this.$message.error('创建失败：' + error.message)
        } finally {
          this.createLoading = false
        }
      })
    },

    // 执行预测
    async runForecast() {
      this.forecastLoading = true
      try {
        const params = {
          ...this.trendForm,
          forecastPeriods: this.forecastForm.forecastPeriods,
          method: this.forecastForm.method
        }
        const response = await budgetAnalysisApi.getTrendChartData(params)
        if (response.code === 1 && response.data && response.data.forecastChart) {
          const fc = response.data.forecastChart
          // 构建预测结果表格数据
          const xAxis = fc.xAxis || []
          const forecastSeries = (fc.series || []).find(s => s.name === '预测值') || {}
          const historySeries = (fc.series || []).find(s => s.name === '历史值') || {}
          this.forecastResult = xAxis.map((period, i) => ({
            period,
            forecastValue: (forecastSeries.data || [])[i] || 0,
            lowerBound: ((forecastSeries.data || [])[i] || 0) * 0.9,
            upperBound: ((forecastSeries.data || [])[i] || 0) * 1.1,
            confidence: 85
          }))
          // 渲染预测图表
          this.$nextTick(() => {
            const dom = document.getElementById('forecastDialogChart')
            if (dom) {
              const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
              chart.setOption({
                tooltip: { trigger: 'axis' },
                legend: { data: ['历史值', '预测值'], bottom: 0 },
                xAxis: { type: 'category', data: xAxis },
                yAxis: { type: 'value' },
                series: [
                  { name: '历史值', type: 'line', data: historySeries.data || [], itemStyle: { color: '#67C23A' } },
                  { name: '预测值', type: 'line', data: forecastSeries.data || [], itemStyle: { color: '#F56C6C' }, lineStyle: { type: 'dashed' } }
                ]
              })
            }
          })
        } else {
          this.$message.warning('暂无预测数据，请先录入趋势分析数据')
        }
      } catch (error) {
        this.$message.error('预测失败：' + error.message)
      } finally {
        this.forecastLoading = false
      }
    },

    // 导出预测结果
    async exportForecastResult() {
      try {
        const params = { ...this.trendForm, forecastOnly: true }
        const response = await budgetAnalysisApi.exportTrendAnalysis(params)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '趋势预测结果.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.trend-analysis {
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
      
      &.growth-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.volatility-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.correlation-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.forecast-card {
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
  
  .chart-row,
  .sub-chart-row {
    margin-bottom: 20px;
    
    .chart-card,
    .main-chart-card {
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
        height: 250px;
      }
      
      .main-chart-container {
        height: 450px;
      }
    }
  }

  /* 全屏模式下图表撑满 */
  .main-chart-card:fullscreen {
    background: #fff;
    padding: 20px;

    .main-chart-container {
      height: calc(100vh - 80px) !important;
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
  
  .forecast-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #909399;
    font-style: italic;
  }
  
  .confidence-text {
    font-size: 12px;
    margin-left: 8px;
  }
  
  .positive-change {
    color: #67C23A;
    font-weight: 500;
  }
  
  .negative-change {
    color: #F56C6C;
    font-weight: 500;
  }
  
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
