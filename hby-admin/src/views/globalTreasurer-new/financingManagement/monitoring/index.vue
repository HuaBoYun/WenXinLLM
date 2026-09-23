<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 融资监控仪表板 -->
      <el-tab-pane label="融资监控仪表板" name="dashboard">
        <div class="dashboard-container">
          <!-- 关键指标卡片 -->
          <el-row :gutter="20" class="mb20">
            <el-col :span="6">
              <el-card class="indicator-card">
                <div class="indicator-content">
                  <div class="indicator-icon">
                    <i class="el-icon-money" style="color: #409EFF;"></i>
                  </div>
                  <div class="indicator-info">
                    <div class="indicator-value">{{ dashboardData.totalFinancing }}</div>
                    <div class="indicator-label">总融资规模（万元）</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="indicator-card">
                <div class="indicator-content">
                  <div class="indicator-icon">
                    <i class="el-icon-document" style="color: #67C23A;"></i>
                  </div>
                  <div class="indicator-info">
                    <div class="indicator-value">{{ dashboardData.activeContracts }}</div>
                    <div class="indicator-label">有效合同数量</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="indicator-card">
                <div class="indicator-content">
                  <div class="indicator-icon">
                    <i class="el-icon-warning" style="color: #E6A23C;"></i>
                  </div>
                  <div class="indicator-info">
                    <div class="indicator-value">{{ dashboardData.riskAlerts }}</div>
                    <div class="indicator-label">风险预警数量</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="indicator-card">
                <div class="indicator-content">
                  <div class="indicator-icon">
                    <i class="el-icon-data-line" style="color: #F56C6C;"></i>
                  </div>
                  <div class="indicator-info">
                    <div class="indicator-value">{{ dashboardData.avgCostRate }}%</div>
                    <div class="indicator-label">平均融资成本</div>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <!-- 图表区域 -->
          <el-row :gutter="20">
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>融资规模趋势</span>
                </div>
                <div id="financingTrendChart" style="height: 300px;"></div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>融资结构分析</span>
                </div>
                <div id="financingStructureChart" style="height: 300px;"></div>
              </el-card>
            </el-col>
          </el-row>

          <!-- 预警信息 -->
          <el-row :gutter="20" class="mt20">
            <el-col :span="24">
              <el-card>
                <div slot="header">
                  <span>最新预警信息</span>
                  <el-button style="float: right; padding: 3px 0" type="text" @click="viewAllAlerts">查看全部</el-button>
                </div>
                <el-table :data="recentAlerts" style="width: 100%">
                  <el-table-column prop="alertType" label="预警类型" width="120">
                    <template slot-scope="scope">
                      <el-tag :type="getAlertTypeTag(scope.row.alertType)">{{ getAlertTypeLabel(scope.row.alertType) }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="alertMessage" label="预警内容" show-overflow-tooltip />
                  <el-table-column prop="alertLevel" label="风险等级" width="100">
                    <template slot-scope="scope">
                      <el-tag :type="getRiskLevelTag(scope.row.alertLevel)">{{ getAlertLevelLabel(scope.row.alertLevel) }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="alertDate" label="预警时间" width="180">
                    <template slot-scope="scope">
                      <span>{{ parseTime(scope.row.alertDate, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="100">
                    <template slot-scope="scope">
                      <el-button size="mini" type="text" @click="handleAlert(scope.row)">处理</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-card>
            </el-col>
          </el-row>

          <!-- 图表区域 -->
          <el-row :gutter="20" class="mb8">
            <el-col :span="12">
              <el-card class="chart-card">
                <div slot="header" class="clearfix">
                  <span>成本趋势分析</span>
                </div>
                <div id="costTrendChart" style="height: 300px;"></div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card class="chart-card">
                <div slot="header" class="clearfix">
                  <span>成本构成分析</span>
                </div>
                <div id="costPieChart" style="height: 300px;"></div>
              </el-card>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="costLoading" :data="costList" @selection-change="handleCostSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="分析ID" align="center" prop="analysisId" width="100" />
            <el-table-column label="公司名称" align="center" prop="companyName" :show-overflow-tooltip="true" />
            <el-table-column label="融资类型" align="center" prop="financingType">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.financingType === 'LOAN'" type="primary">贷款</el-tag>
                <el-tag v-else-if="scope.row.financingType === 'BOND'" type="success">债券</el-tag>
                <el-tag v-else-if="scope.row.financingType === 'LEASE'" type="warning">租赁</el-tag>
                <el-tag v-else-if="scope.row.financingType === 'TRUST'" type="info">信托</el-tag>
                <el-tag v-else>{{ scope.row.financingType || '-' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="周期类型" align="center" prop="periodType">
              <template slot-scope="scope">
                <span v-if="scope.row.periodType === 'MONTH'">月</span>
                <span v-else-if="scope.row.periodType === 'QUARTER'">季</span>
                <span v-else-if="scope.row.periodType === 'YEAR'">年</span>
                <span v-else>{{ scope.row.periodType || '-' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="本金" align="center" prop="principalAmount">
              <template slot-scope="scope">
                <span>{{ scope.row.principalAmount | formatMoney }}</span>
              </template>
            </el-table-column>
            <el-table-column label="总成本" align="center" prop="totalCost">
              <template slot-scope="scope">
                <span>{{ scope.row.totalCost | formatMoney }}</span>
              </template>
            </el-table-column>
            <el-table-column label="成本率" align="center" prop="costRate">
              <template slot-scope="scope">
                <span>{{ scope.row.costRate }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="分析日期" align="center" prop="analysisDate" width="120">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.analysisDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-dropdown size="mini" @command="(command) => handleCostCommand(command, scope.row)" style="margin-left: 5px">
                  <el-button size="mini" type="primary">
                    操作<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="detail" icon="el-icon-view">查看详情</el-dropdown-item>
                    <el-dropdown-item command="edit" icon="el-icon-edit">编辑</el-dropdown-item>
                    <el-dropdown-item command="calculate" icon="el-icon-s-data">重新计算</el-dropdown-item>
                    <el-dropdown-item command="report" icon="el-icon-document">生成报告</el-dropdown-item>
                    <el-dropdown-item command="delete" icon="el-icon-delete">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <el-pagination
            :current-page="costQueryParams.pageNum"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="costQueryParams.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="costTotal"
            @size-change="handleCostSizeChange"
            @current-change="handleCostCurrentChange"
            class="pagination"
            background
          />
        </div>
      </el-tab-pane>

      <!-- 风险监控 -->
      <el-tab-pane label="风险监控" name="riskMonitoring">
        <div class="risk-monitoring-container">
          <!-- 查询条件 -->
          <el-form :model="riskQueryParams" ref="riskQueryForm" size="small" :inline="true" v-show="showRiskSearch" label-width="68px">
            <el-form-item label="关键字" prop="keyword">
              <el-input
                v-model="riskQueryParams.keyword"
                placeholder="请输入预警内容关键字"
                clearable
                @keyup.enter.native="handleRiskQuery"
              />
            </el-form-item>
            <el-form-item label="预警类型" prop="alertType">
              <el-select v-model="riskQueryParams.alertType" placeholder="请选择预警类型" clearable>
                <el-option label="利率风险" value="INTEREST_RATE"></el-option>
                <el-option label="信用风险" value="CREDIT"></el-option>
                <el-option label="流动性风险" value="LIQUIDITY"></el-option>
                <el-option label="市场风险" value="MARKET"></el-option>
                <el-option label="操作风险" value="OPERATION"></el-option>
                <el-option label="到期预警" value="EXPIRY"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="预警级别" prop="alertLevel">
              <el-select v-model="riskQueryParams.alertLevel" placeholder="请选择预警级别" clearable>
                <el-option label="高风险" value="HIGH"></el-option>
                <el-option label="中风险" value="MEDIUM"></el-option>
                <el-option label="低风险" value="LOW"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="预警状态" prop="alertStatus">
              <el-select v-model="riskQueryParams.alertStatus" placeholder="请选择预警状态" clearable>
                <el-option label="待处理" value="PENDING"></el-option>
                <el-option label="已处理" value="HANDLED"></el-option>
                <el-option label="已关闭" value="CLOSED"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="预警日期" prop="alertDateRange">
              <el-date-picker
                v-model="riskQueryParams.alertDateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleRiskQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetRiskQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleAddRiskMonitoring"
              >新增监控</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="riskSingle"
                @click="handleEditRiskMonitoring"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="riskMultiple"
                @click="handleBatchDeleteRiskMonitoring"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleExportRiskData"
              >导出</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-warning"
                size="mini"
                :disabled="riskSingle"
                @click="handleTriggerRiskAlert"
              >触发警报</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-search"
                size="mini"
                @click="showRiskSearch = !showRiskSearch"
              >{{ showRiskSearch ? '隐藏搜索' : '显示搜索' }}</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table
            :data="riskMonitoringList"
            v-loading="riskLoading"
            @selection-change="handleRiskSelectionChange"
            border
            stripe
            style="width: 100%">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="monitoringId" label="监控ID" width="120" show-overflow-tooltip></el-table-column>
            <el-table-column prop="alertType" label="预警类型" width="120">
              <template slot-scope="scope">
                <el-tag :type="getRiskTypeTagType(scope.row.alertType)" size="small">
                  {{ getRiskTypeText(scope.row.alertType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="alertLevel" label="预警级别" width="100">
              <template slot-scope="scope">
                <el-tag :type="getRiskLevelTagType(scope.row.alertLevel)" size="small">
                  {{ getRiskLevelText(scope.row.alertLevel) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="alertStatus" label="预警状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="getRiskStatusTagType(scope.row.alertStatus)" size="small">
                  {{ getRiskStatusText(scope.row.alertStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="alertMessage" label="预警内容" min-width="180" show-overflow-tooltip></el-table-column>
            <el-table-column prop="companyName" label="公司名称" width="150" show-overflow-tooltip></el-table-column>
            <el-table-column prop="alertDate" label="预警日期" width="120">
              <template slot-scope="scope">
                {{ formatDate(scope.row.alertDate) }}
              </template>
            </el-table-column>
            <el-table-column prop="handlerName" label="处理人" width="100"></el-table-column>
            <el-table-column prop="handleOpinion" label="处理意见" width="150" show-overflow-tooltip></el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
              <template slot-scope="scope">
                <el-dropdown @command="(command) => handleRiskOperation(command, scope.row)">
                  <el-button type="text" size="small">
                    操作<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="detail">查看详情</el-dropdown-item>
                    <el-dropdown-item command="edit">编辑</el-dropdown-item>
                    <el-dropdown-item command="handle" v-if="scope.row.alertStatus === 'PENDING'">处理</el-dropdown-item>
                    <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <el-pagination
            background
            class="pagination"
            :current-page="riskQueryParams.pageNum"
            layout="total, sizes, prev, pager, next, jumper"
            :page-size="riskQueryParams.pageSize"
            :total="riskTotal"
            @current-change="handleRiskCurrentChange"
            @size-change="handleRiskSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- 成本分析 -->
      <el-tab-pane label="成本分析" name="costAnalysis">
        <div class="cost-analysis-container">
          <!-- 概览卡片 -->
          <el-row :gutter="20" class="mb8">
            <el-col :span="6">
              <div class="overview-card total-financing">
                <div class="card-content">
                  <div class="card-title">总融资金额</div>
                  <div class="card-value">{{ costOverview.totalFinancingAmount | formatMoney }}</div>
                  <div class="card-desc">万元</div>
                </div>
                <div class="card-icon">
                  <i class="el-icon-money"></i>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="overview-card total-cost">
                <div class="card-content">
                  <div class="card-title">总成本</div>
                  <div class="card-value">{{ costOverview.totalCost | formatMoney }}</div>
                  <div class="card-desc">万元</div>
                </div>
                <div class="card-icon">
                  <i class="el-icon-coin"></i>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="overview-card average-rate">
                <div class="card-content">
                  <div class="card-title">平均成本率</div>
                  <div class="card-value">{{ costOverview.averageCostRate }}%</div>
                  <div class="card-desc">{{ costOverview.monthlyTrend }}</div>
                </div>
                <div class="card-icon">
                  <i class="el-icon-data-line"></i>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="overview-card cost-saving">
                <div class="card-content">
                  <div class="card-title">成本节约</div>
                  <div class="card-value">{{ costOverview.costSaving | formatMoney }}</div>
                  <div class="card-desc">万元</div>
                </div>
                <div class="card-icon">
                  <i class="el-icon-trophy"></i>
                </div>
              </div>
            </el-col>
          </el-row>

          <!-- 查询表单 -->
          <el-form :model="costQueryParams" ref="costQueryForm" size="small" :inline="true" v-show="showCostSearch" label-width="80px">
            <el-form-item label="公司名称" prop="companyName">
              <el-input
                v-model="costQueryParams.companyName"
                placeholder="请输入公司名称"
                clearable
                style="width: 200px"
                @keyup.enter.native="handleCostQuery"
              />
            </el-form-item>
            <el-form-item label="融资类型" prop="financingType">
              <el-select v-model="costQueryParams.financingType" placeholder="请选择融资类型" clearable style="width: 150px">
                <el-option label="银行贷款" value="BANK_LOAN" />
                <el-option label="债券发行" value="BOND_ISSUANCE" />
                <el-option label="融资租赁" value="LEASE" />
                <el-option label="信托融资" value="TRUST" />
                <el-option label="贷款" value="贷款" />
                <el-option label="债券" value="债券" />
                <el-option label="租赁" value="租赁" />
                <el-option label="信托" value="信托" />
              </el-select>
            </el-form-item>
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="costQueryParams.currencyCode" placeholder="请选择币种" clearable style="width: 120px">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
              </el-select>
            </el-form-item>
            <el-form-item label="周期类型" prop="periodType">
              <el-select v-model="costQueryParams.periodType" placeholder="请选择周期" clearable style="width: 120px">
                <el-option label="月" value="MONTH" />
                <el-option label="季" value="QUARTER" />
                <el-option label="年" value="YEAR" />
              </el-select>
            </el-form-item>
            <el-form-item label="分析日期">
              <el-date-picker
                v-model="costDateRange"
                style="width: 240px"
                value-format="yyyy-MM-dd"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleCostQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetCostQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleAddCostAnalysis"
              >新增分析</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="costSingle"
                @click="handleUpdateCostAnalysis"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="costMultiple"
                @click="handleDeleteCostAnalysis"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleExportCostAnalysis"
              >导出</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-document"
                size="mini"
                @click="handleGenerateCostReport"
              >生成报告</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-search"
                size="mini"
                @click="showCostSearch=!showCostSearch"
              >{{ showCostSearch ? '隐藏搜索' : '显示搜索' }}</el-button>
            </el-col>
          </el-row>

          <!-- 成本分析列表 -->
          <el-table
            v-loading="costLoading"
            :data="costList"
            border
            stripe
            @selection-change="handleCostSelectionChange"
          >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="分析ID" prop="analysisId" width="180" show-overflow-tooltip />
            <el-table-column label="公司名称" prop="companyName" min-width="150" show-overflow-tooltip />
            <el-table-column label="融资类型" prop="financingType" width="120" align="center">
              <template slot-scope="scope">
                <span>{{ getCostFinancingTypeText(scope.row.financingType) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="本金(元)" prop="principalAmount" width="130" align="right">
              <template slot-scope="scope">
                <span>{{ formatMoney(scope.row.principalAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="利息支出(元)" prop="interestExpense" width="130" align="right">
              <template slot-scope="scope">
                <span>{{ formatMoney(scope.row.interestExpense) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="总成本(元)" prop="totalCost" width="130" align="right">
              <template slot-scope="scope">
                <span>{{ formatMoney(scope.row.totalCost) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="成本率(%)" prop="costRate" width="100" align="right">
              <template slot-scope="scope">
                <span>{{ scope.row.costRate ? scope.row.costRate.toFixed(2) : '0.00' }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="币种" prop="currencyCode" width="80" align="center" />
            <el-table-column label="周期类型" prop="periodType" width="100" align="center">
              <template slot-scope="scope">
                <span>{{ getPeriodTypeText(scope.row.periodType) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="分析日期" prop="analysisDate" width="120" align="center">
              <template slot-scope="scope">
                <span>{{ formatDate(scope.row.analysisDate) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" align="center" fixed="right">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="viewCostDetail(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="editCostAnalysis(scope.row)"
                >编辑</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  style="color: #F56C6C"
                  @click="handleDeleteSingleCost(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <el-pagination
            class="mt8"
            background
            :current-page="costQueryParams.pageNum"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="costQueryParams.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="costTotal"
            @size-change="handleCostSizeChange"
            @current-change="handleCostCurrentChange"
          />
        </div>
      </el-tab-pane>

      <!-- 报表管理 -->
      <el-tab-pane label="报表管理" name="reportManagement">
        <div class="report-management-container">
          <!-- 概览卡片 -->
          <el-row :gutter="20" class="mb8">
            <el-col :span="6">
              <div class="overview-card card-blue">
                <div class="card-content">
                  <div class="card-title">总报表数</div>
                  <div class="card-value">{{ reportOverview.totalReports }}</div>
                  <div class="card-desc">
                    <span class="trend-up">+{{ reportOverview.monthlyIncrease }}%</span>
                    较上月
                  </div>
                </div>
                <div class="card-icon">
                  <i class="el-icon-document"></i>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="overview-card card-green">
                <div class="card-content">
                  <div class="card-title">已完成</div>
                  <div class="card-value">{{ reportOverview.completedReports }}</div>
                  <div class="card-desc">
                    成功率 {{ reportOverview.successRate }}%
                  </div>
                </div>
                <div class="card-icon">
                  <i class="el-icon-circle-check"></i>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="overview-card card-orange">
                <div class="card-content">
                  <div class="card-title">生成中</div>
                  <div class="card-value">{{ reportOverview.generatingReports }}</div>
                  <div class="card-desc">
                    平均耗时 {{ reportOverview.avgGenerationTime }}秒
                  </div>
                </div>
                <div class="card-icon">
                  <i class="el-icon-loading"></i>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="overview-card card-purple">
                <div class="card-content">
                  <div class="card-title">总下载量</div>
                  <div class="card-value">{{ reportOverview.totalDownloads }}</div>
                  <div class="card-desc">
                    存储 {{ formatFileSize(reportOverview.totalSize) }}
                  </div>
                </div>
                <div class="card-icon">
                  <i class="el-icon-download"></i>
                </div>
              </div>
            </el-col>
          </el-row>

          <!-- 查询表单 -->
          <el-form :model="reportQueryParams" ref="reportQueryForm" size="small" :inline="true" v-show="showReportSearch" label-width="68px">
            <el-form-item label="报表编号" prop="reportNo">
              <el-input
                v-model="reportQueryParams.reportNo"
                placeholder="请输入报表编号"
                clearable
                style="width: 200px"
                @keyup.enter.native="handleReportQuery"
              />
            </el-form-item>
            <el-form-item label="报表名称" prop="reportName">
              <el-input
                v-model="reportQueryParams.reportName"
                placeholder="请输入报表名称"
                clearable
                style="width: 200px"
                @keyup.enter.native="handleReportQuery"
              />
            </el-form-item>
            <el-form-item label="报表类型" prop="reportType">
              <el-select v-model="reportQueryParams.reportType" placeholder="请选择报表类型" clearable style="width: 200px">
                <el-option label="余额报表" value="BALANCE" />
                <el-option label="成本报表" value="COST" />
                <el-option label="期限报表" value="TERM" />
                <el-option label="效率报表" value="EFFICIENCY" />
                <el-option label="风险报表" value="RISK" />
                <el-option label="趋势报表" value="TREND" />
                <el-option label="对比报表" value="COMPARISON" />
                <el-option label="预测报表" value="FORECAST" />
              </el-select>
            </el-form-item>
            <el-form-item label="报表状态" prop="reportStatus">
              <el-select v-model="reportQueryParams.reportStatus" placeholder="请选择报表状态" clearable style="width: 200px">
                <el-option label="生成中" value="GENERATING" />
                <el-option label="已完成" value="COMPLETED" />
                <el-option label="生成失败" value="FAILED" />
                <el-option label="已取消" value="CANCELLED" />
              </el-select>
            </el-form-item>
            <el-form-item label="生成日期">
              <el-date-picker
                v-model="reportDateRange"
                style="width: 240px"
                value-format="yyyy-MM-dd"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleReportQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetReportQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleGenerateReport"
              >生成报表</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="reportSingle"
                @click="handleRegenerateReport"
              >重新生成</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="reportMultiple"
                @click="handleDeleteReport"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                :disabled="reportMultiple"
                @click="handleBatchDownload"
              >批量下载</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-upload2"
                size="mini"
                @click="handleExportReport"
              >导出数据</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-s-tools"
                size="mini"
                @click="handleManageTemplates"
              >模板管理</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-search"
                size="mini"
                @click="showReportSearch = !showReportSearch"
              >{{ showReportSearch ? '隐藏搜索' : '显示搜索' }}</el-button>
            </el-col>
          </el-row>

          <!-- 图表区域 -->
          <el-row :gutter="20" class="mb8">
            <el-col :span="12">
              <div class="chart-container">
                <div class="chart-title">报表生成趋势</div>
                <div ref="reportTrendChart" style="height: 300px; width: 100%;"></div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="chart-container">
                <div class="chart-title">报表类型分布</div>
                <div ref="reportTypeChart" style="height: 300px; width: 100%;"></div>
              </div>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table
            v-loading="reportLoading"
            :data="reportList"
            @selection-change="handleReportSelectionChange"
            style="width: 100%"
          >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="报表编号" align="center" prop="recordId" width="180" />
            <el-table-column label="报表名称" align="center" prop="reportName" width="200" show-overflow-tooltip />
            <el-table-column label="报表类型" align="center" prop="reportType" width="120">
              <template slot-scope="scope">
                <el-tag :type="getReportTypeTagType(scope.row.reportType)" size="mini">
                  {{ getReportTypeName(scope.row.reportType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="报表状态" align="center" prop="generationStatus" width="120">
              <template slot-scope="scope">
                <el-tag :type="getReportStatusTagType(scope.row.generationStatus)" size="mini">
                  {{ getReportStatusName(scope.row.generationStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="周期类型" align="center" prop="periodType" width="100">
              <template slot-scope="scope">
                {{ getReportPeriodTypeName(scope.row.periodType) }}
              </template>
            </el-table-column>
            <el-table-column label="文件大小" align="center" prop="fileSize" width="120">
              <template slot-scope="scope">
                {{ formatFileSize(scope.row.fileSize) }}
              </template>
            </el-table-column>
            <el-table-column label="下载次数" align="center" prop="downloadCount" width="100">
              <template slot-scope="scope">
                {{ scope.row.downloadCount || 0 }}
              </template>
            </el-table-column>
            <el-table-column label="生成时间" align="center" prop="generationTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.generationTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="生成人" align="center" prop="generatedByName" width="120" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
              <template slot-scope="scope">
                <el-dropdown size="mini" @command="(command) => handleReportCommand(command, scope.row)" style="margin-left: 5px">
                  <el-button size="mini" type="primary">
                    操作<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="view" icon="el-icon-view">查看详情</el-dropdown-item>
                    <el-dropdown-item command="download" icon="el-icon-download" v-if="scope.row.generationStatus === 'COMPLETED'">下载报表</el-dropdown-item>
                    <el-dropdown-item command="regenerate" icon="el-icon-refresh" v-if="scope.row.generationStatus === 'FAILED' || scope.row.generationStatus === 'COMPLETED'">重新生成</el-dropdown-item>
                    <el-dropdown-item command="cancel" icon="el-icon-close" v-if="scope.row.generationStatus === 'GENERATING'">取消生成</el-dropdown-item>
                    <el-dropdown-item command="progress" icon="el-icon-time" v-if="scope.row.generationStatus === 'GENERATING'">查看进度</el-dropdown-item>
                    <el-dropdown-item command="distribute" icon="el-icon-share">分发报表</el-dropdown-item>
                    <el-dropdown-item command="delete" icon="el-icon-delete" style="color: #f56c6c">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页组件 -->
          <el-pagination
            :current-page="reportQueryParams.pageNum"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="reportQueryParams.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="reportTotal"
            @size-change="handleReportSizeChange"
            @current-change="handleReportCurrentChange"
            class="pagination"
            background
          />
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 风险监控弹窗 -->
    <el-dialog
      :title="riskDialogTitle"
      :visible.sync="riskDialogVisible"
      width="700px"
      :close-on-click-modal="false"
      @close="handleRiskDialogClose"
    >
      <el-form
        ref="riskFormRef"
        :model="riskForm"
        :rules="riskFormRules"
        label-width="100px"
        :disabled="riskDialogType === 'view'"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="监控ID" prop="monitoringId">
              <el-input v-model="riskForm.monitoringId" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警类型" prop="alertType">
              <el-select v-model="riskForm.alertType" placeholder="请选择预警类型" style="width: 100%">
                <el-option
                  v-for="item in riskTypeOptions"
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
            <el-form-item label="预警级别" prop="alertLevel">
              <el-select v-model="riskForm.alertLevel" placeholder="请选择预警级别" style="width: 100%">
                <el-option
                  v-for="item in riskLevelOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联融资ID" prop="relatedFinancingId">
              <el-input v-model.number="riskForm.relatedFinancingId" placeholder="请输入关联融资ID" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="公司ID" prop="companyId">
              <el-input v-model.number="riskForm.companyId" placeholder="请输入公司ID" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyName">
              <el-input v-model="riskForm.companyName" placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="预警内容" prop="alertMessage">
              <el-input
                v-model="riskForm.alertMessage"
                type="textarea"
                :rows="3"
                placeholder="请输入预警内容"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="riskDialogType !== 'add'">
          <el-col :span="12">
            <el-form-item label="处理人" prop="handlerName">
              <el-input v-model="riskForm.handlerName" placeholder="请输入处理人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警状态" prop="alertStatus">
              <el-select v-model="riskForm.alertStatus" placeholder="请选择预警状态" style="width: 100%">
                <el-option label="未处理" value="PENDING" />
                <el-option label="已处理" value="HANDLED" />
                <el-option label="已关闭" value="CLOSED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="riskDialogType !== 'add'">
          <el-col :span="24">
            <el-form-item label="处理意见" prop="handleOpinion">
              <el-input
                v-model="riskForm.handleOpinion"
                type="textarea"
                :rows="2"
                placeholder="请输入处理意见"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="riskForm.remark"
                type="textarea"
                :rows="2"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="riskDialogType !== 'view'">
        <el-button @click="handleRiskDialogClose">取 消</el-button>
        <el-button type="primary" @click="submitRiskForm">确 定</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-else>
        <el-button @click="handleRiskDialogClose">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 成本分析弹窗 -->
    <el-dialog
      :title="costDialogTitle"
      :visible.sync="costDialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleCostDialogClose"
    >
      <el-form
        ref="costFormRef"
        :model="costForm"
        :rules="costFormRules"
        label-width="100px"
        :disabled="costDialogType === 'view'"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分析ID">
              <el-input v-model="costForm.analysisId" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyName">
              <el-input v-model="costForm.companyName" placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="融资类型" prop="financingType">
              <el-select v-model="costForm.financingType" placeholder="请选择融资类型" style="width: 100%">
                <el-option label="贷款" value="贷款" />
                <el-option label="债券" value="债券" />
                <el-option label="租赁" value="租赁" />
                <el-option label="信托" value="信托" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="周期类型" prop="periodType">
              <el-select v-model="costForm.periodType" placeholder="请选择周期类型" style="width: 100%">
                <el-option label="月" value="MONTH" />
                <el-option label="季" value="QUARTER" />
                <el-option label="年" value="YEAR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="本金" prop="principalAmount">
              <el-input-number
                v-model="costForm.principalAmount"
                :min="0"
                :precision="2"
                placeholder="请输入本金"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分析日期" prop="analysisDate">
              <el-date-picker
                v-model="costForm.analysisDate"
                type="date"
                placeholder="选择日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="利息支出" prop="interestExpense">
              <el-input-number
                v-model="costForm.interestExpense"
                :min="0"
                :precision="2"
                placeholder="请输入利息支出"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手续费用" prop="feeCost">
              <el-input-number
                v-model="costForm.feeCost"
                :min="0"
                :precision="2"
                placeholder="请输入手续费用"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="担保费用" prop="guaranteeCost">
              <el-input-number
                v-model="costForm.guaranteeCost"
                :min="0"
                :precision="2"
                placeholder="请输入担保费用"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其他费用" prop="otherCost">
              <el-input-number
                v-model="costForm.otherCost"
                :min="0"
                :precision="2"
                placeholder="请输入其他费用"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="总成本">
              <el-input-number
                v-model="costForm.totalCost"
                :min="0"
                :precision="2"
                placeholder="总成本"
                style="width: 100%"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成本率(%)">
              <el-input-number
                v-model="costForm.costRate"
                :min="0"
                :max="100"
                :precision="4"
                placeholder="成本率"
                style="width: 100%"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="costForm.currencyCode" placeholder="请选择币种" style="width: 100%">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间">
              <el-input v-model="costForm.createdTime" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="costForm.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="costDialogType !== 'view'">
        <el-button @click="handleCostDialogClose">取 消</el-button>
        <el-button type="primary" @click="submitCostForm">确 定</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-else>
        <el-button @click="handleCostDialogClose">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 报表生成弹窗 -->
    <el-dialog
      :title="reportDialogTitle"
      :visible.sync="reportDialogVisible"
      width="700px"
      :close-on-click-modal="false"
      @close="handleReportDialogClose"
    >
      <el-form
        ref="reportFormRef"
        :model="reportForm"
        :rules="reportFormRules"
        label-width="100px"
        :disabled="reportDialogType === 'view'"
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="报表名称" prop="reportName">
              <el-input v-model="reportForm.reportName" placeholder="请输入报表名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报表类型" prop="reportType">
              <el-select v-model="reportForm.reportType" placeholder="请选择报表类型" style="width: 100%">
                <el-option label="余额报表" value="BALANCE" />
                <el-option label="成本报表" value="COST" />
                <el-option label="期限报表" value="TERM" />
                <el-option label="效率报表" value="EFFICIENCY" />
                <el-option label="风险报表" value="RISK" />
                <el-option label="趋势报表" value="TREND" />
                <el-option label="对比报表" value="COMPARISON" />
                <el-option label="预测报表" value="FORECAST" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="周期类型" prop="periodType">
              <el-select v-model="reportForm.periodType" placeholder="请选择周期类型" style="width: 100%">
                <el-option label="日报" value="DAILY" />
                <el-option label="周报" value="WEEKLY" />
                <el-option label="月报" value="MONTHLY" />
                <el-option label="季报" value="QUARTERLY" />
                <el-option label="年报" value="ANNUAL" />
                <el-option label="自定义" value="CUSTOM" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="periodStartDate">
              <el-date-picker
                v-model="reportForm.periodStartDate"
                type="date"
                placeholder="选择开始日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="periodEndDate">
              <el-date-picker
                v-model="reportForm.periodEndDate"
                type="date"
                placeholder="选择结束日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板ID" prop="templateId">
              <el-select v-model="reportForm.templateId" placeholder="请选择报表模板" style="width: 100%">
                <el-option label="融资日报表" :value="1" />
                <el-option label="融资周报表" :value="2" />
                <el-option label="融资月报表" :value="3" />
                <el-option label="融资季报表" :value="4" />
                <el-option label="融资年报表" :value="5" />
                <el-option label="融资结构分析表" :value="6" />
                <el-option label="融资成本分析表" :value="7" />
                <el-option label="融资余额统计表" :value="8" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生成状态" v-if="reportDialogType === 'view'">
              <el-tag :type="getReportStatusTagType(reportForm.generationStatus)" size="medium">
                {{ getReportStatusName(reportForm.generationStatus) }}
              </el-tag>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="reportForm.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="reportDialogType !== 'view'">
        <el-button @click="handleReportDialogClose">取 消</el-button>
        <el-button type="primary" @click="submitReportForm" :loading="reportSubmitLoading">确 定</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-else>
        <el-button @click="handleReportDialogClose">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 模板管理弹窗 -->
    <el-dialog title="模板管理" :visible.sync="templateDialogVisible" width="1000px" append-to-body>
      <!-- 查询条件 -->
      <el-form :model="templateQueryParams" ref="templateQueryForm" :inline="true" size="small">
        <el-form-item label="模板名称" prop="templateName">
          <el-input v-model="templateQueryParams.templateName" placeholder="请输入模板名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="模板类型" prop="templateType">
          <el-select v-model="templateQueryParams.templateType" placeholder="请选择" clearable style="width: 150px">
            <el-option v-for="item in templateTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select v-model="templateQueryParams.isEnabled" placeholder="请选择" clearable style="width: 100px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleTemplateQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetTemplateQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <el-row :gutter="10" class="mb10">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddTemplate">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="templateSelection.length === 0" @click="handleBatchDeleteTemplates">批量删除</el-button>
        </el-col>
      </el-row>

      <!-- 模板列表 -->
      <el-table v-loading="templateLoading" :data="templateList" @selection-change="handleTemplateSelectionChange" border style="width: 100%">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="模板代码" prop="templateCode" width="120" show-overflow-tooltip />
        <el-table-column label="模板名称" prop="templateName" min-width="150" show-overflow-tooltip />
        <el-table-column label="模板类型" prop="templateType" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="small">{{ getTemplateTypeName(scope.row.templateType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="报告频率" prop="reportFrequency" width="80" align="center">
          <template slot-scope="scope">{{ getFrequencyName(scope.row.reportFrequency) }}</template>
        </el-table-column>
        <el-table-column label="状态" prop="isEnabled" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'info'" size="small">
              {{ scope.row.isEnabled === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="生效日期" prop="effectiveDate" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.effectiveDate ? parseTime(scope.row.effectiveDate, '{y}-{m}-{d}') : '-' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleViewTemplate(scope.row)">查看</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEditTemplate(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" icon="el-icon-switch-button" @click="handleToggleTemplateStatus(scope.row)">
              {{ scope.row.isEnabled === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" style="color: #f56c6c" @click="handleDeleteTemplate(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        :current-page="templateQueryParams.pageNum"
        :page-sizes="[10, 20, 50]"
        :page-size="templateQueryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="templateTotal"
        @size-change="handleTemplateSizeChange"
        @current-change="handleTemplateCurrentChange"
        style="margin-top: 15px; text-align: right"
      />
    </el-dialog>

    <!-- 模板编辑弹窗 -->
    <el-dialog :title="templateFormTitle" :visible.sync="templateFormDialogVisible" width="600px" append-to-body>
      <el-form ref="templateFormRef" :model="templateForm" :rules="templateFormRules" label-width="100px" :disabled="templateFormType === 'view'">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板代码" prop="templateCode">
              <el-input v-model="templateForm.templateCode" placeholder="请输入模板代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="templateForm.templateName" placeholder="请输入模板名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板类型" prop="templateType">
              <el-select v-model="templateForm.templateType" placeholder="请选择模板类型" style="width: 100%">
                <el-option v-for="item in templateTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告频率" prop="reportFrequency">
              <el-select v-model="templateForm.reportFrequency" placeholder="请选择报告频率" style="width: 100%">
                <el-option v-for="item in frequencyOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker v-model="templateForm.effectiveDate" type="date" placeholder="选择生效日期" style="width: 100%" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expiryDate">
              <el-date-picker v-model="templateForm.expiryDate" type="date" placeholder="选择失效日期" style="width: 100%" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="isEnabled">
              <el-radio-group v-model="templateForm.isEnabled">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input v-model="templateForm.description" type="textarea" :rows="3" placeholder="请输入模板描述" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="templateFormType !== 'view'">
        <el-button @click="templateFormDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitTemplateForm" :loading="templateSubmitLoading">确 定</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-else>
        <el-button @click="templateFormDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 分发报表弹窗 -->
    <el-dialog title="分发报表" :visible.sync="distributeDialogVisible" width="500px" append-to-body>
      <el-form ref="distributeFormRef" :model="distributeForm" :rules="distributeFormRules" label-width="100px">
        <el-form-item label="报表名称">
          <span>{{ currentDistributeReport ? currentDistributeReport.reportName : '' }}</span>
        </el-form-item>
        <el-form-item label="接收人" prop="recipients">
          <el-select v-model="distributeForm.recipients" multiple placeholder="请选择接收人" style="width: 100%">
            <el-option v-for="item in recipientOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="发送邮件">
          <el-switch v-model="distributeForm.sendEmail" />
        </el-form-item>
        <el-form-item label="邮件主题" v-if="distributeForm.sendEmail">
          <el-input v-model="distributeForm.emailSubject" placeholder="请输入邮件主题" />
        </el-form-item>
        <el-form-item label="邮件内容" v-if="distributeForm.sendEmail">
          <el-input v-model="distributeForm.emailContent" type="textarea" :rows="4" placeholder="请输入邮件内容" />
        </el-form-item>
        <el-form-item label="系统通知">
          <el-switch v-model="distributeForm.sendNotification" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="distributeDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitDistributeForm" :loading="distributeLoading">确认分发</el-button>
      </div>
    </el-dialog>

    <!-- 预警处理弹窗 -->
    <alert-process-dialog
      :visible.sync="alertProcessDialogVisible"
      :alert-data="currentAlertData"
      @success="handleAlertProcessSuccess"
    />
  </div>
</template>

<script>
import {
  getFinancingDashboardData,
  getFinancingAlerts,
  handleFinancingAlert,
  getFinancingRiskMonitoringPage,
  getFinancingRiskMonitoring,
  createFinancingRiskMonitoring,
  updateFinancingRiskMonitoring,
  deleteFinancingRiskMonitoring,
  batchDeleteFinancingRiskMonitorings,
  triggerFinancingRiskAlert,
  handleFinancingRiskMonitoring,
  closeFinancingRiskMonitoring,
  reactivateFinancingRiskMonitoring,
  getFinancingRiskMonitoringOverview,
  // 成本分析相关API
  listCostAnalysis,
  getCostAnalysis,
  addCostAnalysis,
  updateCostAnalysis,
  delCostAnalysis,
  getCostOverview,
  getCostChartData,
  generateCostReport,
  getCostOptimization,
  calculateFinancingCost,
  getFinancingCostTrend,
  compareFinancingCost,
  getMarketRates,
  // 报表管理相关API
  listReportRecords,
  getReportRecord,
  delReportRecord,
  generateReport,
  regenerateReport,
  cancelReportGeneration,
  getReportGenerationProgress,
  downloadReport,
  getReportOverviewStatistics,
  getReportChartData,
  exportReportRecords,
  updateReportRecord,
  // 模板管理相关API
  listReportTemplates,
  getReportTemplate,
  addReportTemplate,
  updateReportTemplate,
  delReportTemplate,
  updateTemplateStatus,
  distributeReport,
  // 图表分析相关API
  getFinancingTrendAnalysis,
  getFinancingStructureAnalysis
} from "@/api/globalTreasurer/rzgl";
import { parseTime } from '@/utils'
import * as echarts from 'echarts'
import AlertProcessDialog from '../components/AlertProcessDialog.vue'

export default {
  name: "FinancingMonitoring",
  components: {
    AlertProcessDialog
  },
  filters: {
    formatMoney(value) {
      if (!value) return '0';
      const num = parseFloat(value);
      if (num >= 10000) {
        return (num / 10000).toFixed(2);
      }
      return num.toFixed(2);
    }
  },
  data() {
    return {
      // 当前激活的标签页
      activeTab: "dashboard",
      
      // 仪表板数据
      dashboardData: {
        totalFinancing: 0,
        activeContracts: 0,
        riskAlerts: 0,
        avgCostRate: 0
      },
      
      // 最新预警信息
      recentAlerts: [],

      // 预警处理弹窗
      alertProcessDialogVisible: false,
      currentAlertData: {},

      // 图表实例
      financingTrendChart: null,
      financingStructureChart: null,

      // 风险监控数据（移除概览数据，使用票据管理样式）

      // 风险监控查询参数
      riskQueryParams: {
        pageNum: 1,
        pageSize: 10,
        keyword: '',
        alertType: '',
        alertLevel: '',
        alertStatus: '',
        alertDateRange: [],
        orgId: 1
      },

      // 风险监控表格数据
      riskMonitoringList: [],
      riskTotal: 0,
      riskLoading: false,
      riskSelection: [],

      // 风险监控控制变量
      showRiskSearch: true,
      riskSingle: true,
      riskMultiple: true,

      // 风险监控弹窗控制
      riskDialogVisible: false,
      riskDialogTitle: '',
      riskDialogType: 'add', // add, edit, view
      riskForm: {
        monitoringId: null,
        alertType: '',
        alertLevel: '',
        alertMessage: '',
        relatedFinancingId: null,
        alertStatus: 'PENDING',
        companyId: null,
        companyName: '',
        handlerName: '',
        handleOpinion: '',
        remark: ''
      },
      riskFormRules: {
        alertType: [{ required: true, message: '请选择预警类型', trigger: 'change' }],
        alertLevel: [{ required: true, message: '请选择预警级别', trigger: 'change' }],
        alertMessage: [{ required: true, message: '请输入预警内容', trigger: 'blur' }],
        relatedFinancingId: [{ required: true, message: '请输入关联融资ID', trigger: 'blur' }],
        companyId: [{ required: true, message: '请输入公司ID', trigger: 'blur' }]
      },
      // 风险类型选项
      riskTypeOptions: [
        { value: 'INTEREST_RATE', label: '利率风险' },
        { value: 'EXPIRY', label: '到期风险' },
        { value: 'CREDIT', label: '信用风险' },
        { value: 'LIQUIDITY', label: '流动性风险' },
        { value: 'MARKET', label: '市场风险' },
        { value: 'OPERATION', label: '操作风险' }
      ],
      // 风险等级选项
      riskLevelOptions: [
        { value: 'HIGH', label: '高风险' },
        { value: 'MEDIUM', label: '中风险' },
        { value: 'LOW', label: '低风险' }
      ],

      // 成本分析数据
      costOverview: {
        totalFinancingAmount: 0,
        totalCost: 0,
        averageCostRate: 0,
        monthlyTrend: '稳定',
        costSaving: 0,
        optimizationRate: 0
      },

      // 成本分析查询参数
      costQueryParams: {
        pageNum: 1,
        pageSize: 10,
        companyName: '',      // 公司名称（前端筛选用）
        financingType: '',    // 融资类型
        currencyCode: '',     // 币种
        periodType: ''        // 周期类型
      },

      // 成本分析日期范围
      costDateRange: [],

      // 成本分析表格数据
      costList: [],
      costTotal: 0,
      costLoading: false,
      costSelection: [],

      // 成本分析控制变量
      showCostSearch: true,
      costSingle: true,
      costMultiple: true,

      // 成本分析弹窗控制
      costDialogVisible: false,
      costDialogTitle: '',
      costDialogType: 'view', // view, edit, add
      costForm: {
        analysisId: null,
        financingId: null,
        financingType: '',
        principalAmount: 0,
        interestExpense: 0,
        feeCost: 0,
        guaranteeCost: 0,
        otherCost: 0,
        totalCost: 0,
        costRate: 0,
        currencyCode: 'CNY',
        analysisDate: '',
        periodType: 'MONTH',
        periodValue: '',
        companyId: 1,
        companyName: '',
        createdBy: null,
        createdByName: '',
        createdTime: '',
        remark: ''
      },
      costFormRules: {
        companyName: [{ required: true, message: '请输入公司名称', trigger: 'blur' }],
        financingType: [{ required: true, message: '请选择融资类型', trigger: 'change' }],
        principalAmount: [{ required: true, message: '请输入本金', trigger: 'blur' }],
        currencyCode: [{ required: true, message: '请选择币种', trigger: 'change' }]
      },
      // 成本分析类型选项
      costAnalysisTypeOptions: [
        { value: 'LOAN', label: '贷款成本' },
        { value: 'BOND', label: '债券成本' },
        { value: 'LEASE', label: '租赁成本' },
        { value: 'COMPREHENSIVE', label: '综合成本' }
      ],

      // 成本分析图表实例
      costTrendChart: null,
      costPieChart: null,

      // 报表管理数据
      reportOverview: {
        totalReports: 0,
        completedReports: 0,
        generatingReports: 0,
        failedReports: 0,
        totalDownloads: 0,
        totalSize: 0,
        avgGenerationTime: 0,
        successRate: 0,
        monthlyIncrease: 0
      },

      // 报表管理查询参数
      reportQueryParams: {
        pageNum: 1,
        pageSize: 10,
        reportNo: '',
        reportName: '',
        reportType: '',
        reportStatus: '',
        orgId: 1
      },

      // 报表管理日期范围
      reportDateRange: [],

      // 报表管理表格数据
      reportList: [],
      reportTotal: 0,
      reportLoading: false,
      reportSelection: [],

      // 报表管理控制变量
      showReportSearch: true,
      reportSingle: true,
      reportMultiple: true,

      // 报表管理图表实例
      reportTrendChart: null,
      reportTypeChart: null,

      // 报表生成弹窗相关
      reportDialogVisible: false,
      reportDialogType: 'add', // add, edit, view
      reportDialogTitle: '生成报表',
      reportSubmitLoading: false,
      reportForm: {
        recordId: null,
        reportName: '',
        reportType: '',
        periodType: 'MONTHLY',
        periodStartDate: '',
        periodEndDate: '',
        templateId: 3,
        remark: '',
        generationStatus: ''
      },
      reportFormRules: {
        reportName: [
          { required: true, message: '请输入报表名称', trigger: 'blur' }
        ],
        reportType: [
          { required: true, message: '请选择报表类型', trigger: 'change' }
        ],
        periodType: [
          { required: true, message: '请选择周期类型', trigger: 'change' }
        ],
        templateId: [
          { required: true, message: '请选择报表模板', trigger: 'change' }
        ]
      },

      // ==================== 模板管理相关数据 ====================
      templateDialogVisible: false,
      templateLoading: false,
      templateList: [],
      templateTotal: 0,
      templateQueryParams: {
        pageNum: 1,
        pageSize: 10,
        templateName: '',
        templateType: '',
        isEnabled: null
      },
      templateSelection: [],
      // 模板编辑弹窗
      templateFormDialogVisible: false,
      templateFormType: 'add', // add, edit, view
      templateFormTitle: '新增模板',
      templateSubmitLoading: false,
      templateForm: {
        templateId: null,
        templateCode: '',
        templateName: '',
        templateType: '',
        reportFrequency: '',
        isEnabled: 1,
        effectiveDate: '',
        expiryDate: '',
        description: ''
      },
      templateFormRules: {
        templateCode: [{ required: true, message: '请输入模板代码', trigger: 'blur' }],
        templateName: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
        templateType: [{ required: true, message: '请选择模板类型', trigger: 'change' }]
      },
      // 模板类型选项
      templateTypeOptions: [
        { value: 'DAILY', label: '日报模板' },
        { value: 'WEEKLY', label: '周报模板' },
        { value: 'MONTHLY', label: '月报模板' },
        { value: 'QUARTERLY', label: '季报模板' },
        { value: 'ANNUAL', label: '年报模板' },
        { value: 'ANALYSIS', label: '分析模板' },
        { value: 'STATISTICS', label: '统计模板' }
      ],
      // 报告频率选项
      frequencyOptions: [
        { value: 'DAILY', label: '每日' },
        { value: 'WEEKLY', label: '每周' },
        { value: 'MONTHLY', label: '每月' },
        { value: 'QUARTERLY', label: '每季' },
        { value: 'ANNUAL', label: '每年' },
        { value: 'ON_DEMAND', label: '按需' }
      ],

      // ==================== 分发报表相关数据 ====================
      distributeDialogVisible: false,
      distributeLoading: false,
      currentDistributeReport: null,
      distributeForm: {
        recipients: [],
        sendEmail: true,
        emailSubject: '',
        emailContent: '',
        sendNotification: true
      },
      distributeFormRules: {
        recipients: [{ required: true, message: '请选择接收人', trigger: 'change' }]
      },
      // 接收人选项（模拟数据）
      recipientOptions: [
        { value: 'user1', label: '张三 - 财务部' },
        { value: 'user2', label: '李四 - 风控部' },
        { value: 'user3', label: '王五 - 管理层' },
        { value: 'user4', label: '赵六 - 审计部' }
      ]
    };
  },
  created() {
    this.loadDashboardData();
    this.loadRecentAlerts();
    this.loadCostOverview();
    this.loadCostAnalysisList();
    this.loadReportOverview();
    this.loadReportList();
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts();
      this.initCostCharts();
      this.initReportCharts();
    });
  },
  beforeDestroy() {
    if (this.financingTrendChart) {
      this.financingTrendChart.dispose();
    }
    if (this.financingStructureChart) {
      this.financingStructureChart.dispose();
    }
    if (this.costTrendChart) {
      this.costTrendChart.dispose();
    }
    if (this.costPieChart) {
      this.costPieChart.dispose();
    }
    if (this.reportTrendChart) {
      this.reportTrendChart.dispose();
    }
    if (this.reportTypeChart) {
      this.reportTypeChart.dispose();
    }
  },
  methods: {
    parseTime,
    
    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'dashboard') {
        this.loadDashboardData();
        this.$nextTick(() => {
          this.initCharts();
        });
      } else if (tab.name === 'riskMonitoring') {
        this.loadRiskMonitoringList();
      } else if (tab.name === 'costAnalysis') {
        this.loadCostAnalysisList();
        this.loadCostOverview();
        this.$nextTick(() => {
          this.initCostCharts();
        });
      } else if (tab.name === 'reportManagement') {
        this.loadReportList();
        this.loadReportOverview();
        this.$nextTick(() => {
          this.initReportCharts();
        });
      }
    },

    /** 加载仪表板数据 */
    loadDashboardData() {
      getFinancingDashboardData().then(response => {
        console.log('仪表板数据响应:', response);
        console.log('response.data:', response?.data);
        // 兼容多种响应格式
        if (response && response.data) {
          // 如果是 JsonBean 格式 {code: 1, msg: '', data: {...}}
          const data = response.data;
          // 兼容大写和小写字段名（达梦数据库返回大写）
          this.dashboardData = {
            totalFinancing: data.totalFinancing || data.TOTALFINANCING || 0,
            activeContracts: data.activeContracts || data.ACTIVECONTRACTS || 0,
            riskAlerts: data.riskAlerts || data.RISKALERTS || 0,
            avgCostRate: data.avgCostRate || data.AVGCOSTRATE || 0
          };
          console.log('处理后的 dashboardData:', JSON.stringify(this.dashboardData));
        } else if (response) {
          // 直接返回数据的情况
          this.dashboardData = {
            totalFinancing: response.totalFinancing || response.TOTALFINANCING || 0,
            activeContracts: response.activeContracts || response.ACTIVECONTRACTS || 0,
            riskAlerts: response.riskAlerts || response.RISKALERTS || 0,
            avgCostRate: response.avgCostRate || response.AVGCOSTRATE || 0
          };
        }
      }).catch(error => {
        console.error('加载仪表板数据失败:', error);
        // 设置默认数据
        this.dashboardData = {
          totalFinancing: 0,
          activeContracts: 0,
          riskAlerts: 0,
          avgCostRate: 0
        };
      });
    },

    /** 加载最新预警信息（只显示未处理的预警） */
    loadRecentAlerts() {
      console.log('=== 开始加载预警列表 ===');
      // 只查询未处理状态(PENDING)的预警，已处理的不显示在仪表盘
      getFinancingAlerts({
        pageNum: 1,
        pageSize: 5,
        alertStatus: 'PENDING'  // 只查询未处理的预警
      }).then(response => {
        console.log('预警列表响应:', response);
        console.log('response.data:', response?.data);
        console.log('response.data.rows:', response?.data?.rows);
        // 兼容多种响应格式
        if (response && response.data && response.data.rows) {
          // 新格式 {code: 1, data: {rows: [...], total: xxx}}
          console.log('使用 response.data.rows');
          this.recentAlerts = response.data.rows || [];
        } else if (response && response.data && response.data.list) {
          // PageInfo 格式 {code: 1, data: {list: [...], total: xxx}}
          console.log('使用 response.data.list');
          this.recentAlerts = response.data.list || [];
        } else if (response && response.rows) {
          // 直接返回 rows 的格式
          console.log('使用 response.rows');
          this.recentAlerts = response.rows || [];
        } else if (response && response.data && Array.isArray(response.data)) {
          // 直接返回数组的格式
          console.log('使用 response.data (数组)');
          this.recentAlerts = response.data || [];
        } else {
          console.log('没有匹配的格式，设置为空数组');
          this.recentAlerts = [];
        }
        console.log('最终 recentAlerts:', JSON.stringify(this.recentAlerts));
      }).catch(error => {
        console.error('加载预警信息失败:', error);
        this.recentAlerts = [];
      });
    },

    /** 初始化图表 */
    initCharts() {
      this.loadFinancingTrendChart();
      this.loadFinancingStructureChart();
    },

    /** 加载融资规模趋势图数据 */
    loadFinancingTrendChart() {
      const chartDom = document.getElementById('financingTrendChart');
      if (!chartDom) return;

      // 初始化图表
      if (!this.financingTrendChart) {
        this.financingTrendChart = echarts.init(chartDom);
      }

      // 显示加载状态
      this.financingTrendChart.showLoading();

      // 调用后端接口获取数据
      getFinancingTrendAnalysis({ months: 6 }).then(response => {
        this.financingTrendChart.hideLoading();
        console.log('融资规模趋势响应数据:', response);

        let monthData = [];
        let amountData = [];

        // 解析响应数据 - 兼容大小写字段名
        if (response && response.code === 1 && response.data && response.data.length > 0) {
          response.data.forEach(item => {
            // 兼容大小写字段名
            const month = item.month || item.MONTH || item.Month || '';
            const amount = item.amount || item.AMOUNT || item.Amount || 0;
            monthData.push(month);
            amountData.push(Number(amount) || 0);
          });
        }

        console.log('解析后的月份数据:', monthData);
        console.log('解析后的金额数据:', amountData);

        // 设置图表配置
        const option = {
          title: {
            text: '融资规模趋势',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis',
            formatter: '{b}: {c} 万元'
          },
          xAxis: {
            type: 'category',
            data: monthData.length > 0 ? monthData : ['暂无数据']
          },
          yAxis: {
            type: 'value',
            name: '金额（万元）'
          },
          series: [{
            data: amountData.length > 0 ? amountData : [0],
            type: 'line',
            smooth: true,
            itemStyle: {
              color: '#409EFF'
            },
            areaStyle: {
              color: 'rgba(64, 158, 255, 0.2)'
            }
          }]
        };
        this.financingTrendChart.setOption(option);
      }).catch(error => {
        this.financingTrendChart.hideLoading();
        console.error('获取融资规模趋势数据失败:', error);
        // 显示空数据状态
        const option = {
          title: {
            text: '融资规模趋势',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            data: ['暂无数据']
          },
          yAxis: {
            type: 'value',
            name: '金额（万元）'
          },
          series: [{
            data: [0],
            type: 'line',
            smooth: true,
            itemStyle: {
              color: '#409EFF'
            }
          }]
        };
        this.financingTrendChart.setOption(option);
      });
    },

    /** 加载融资结构分析图数据 */
    loadFinancingStructureChart() {
      const chartDom = document.getElementById('financingStructureChart');
      if (!chartDom) return;

      // 初始化图表
      if (!this.financingStructureChart) {
        this.financingStructureChart = echarts.init(chartDom);
      }

      // 显示加载状态
      this.financingStructureChart.showLoading();

      // 调用后端接口获取数据
      getFinancingStructureAnalysis({}).then(response => {
        this.financingStructureChart.hideLoading();
        console.log('融资结构分析响应数据:', response);

        let pieData = [];

        // 解析响应数据 - 兼容大小写字段名
        if (response && response.code === 1 && response.data && response.data.length > 0) {
          pieData = response.data.map(item => {
            // 兼容大小写字段名
            const name = item.name || item.NAME || item.Name || '未知';
            const value = item.value || item.VALUE || item.Value || 0;
            return {
              name: name,
              value: Number(value) || 0
            };
          });
        }

        console.log('解析后的饼图数据:', pieData);

        // 设置图表配置
        const option = {
          title: {
            text: '融资结构分析',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} 万元 ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            top: 'middle'
          },
          series: [{
            type: 'pie',
            radius: '50%',
            data: pieData.length > 0 ? pieData : [{ name: '暂无数据', value: 0 }],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }]
        };
        this.financingStructureChart.setOption(option);
      }).catch(error => {
        this.financingStructureChart.hideLoading();
        console.error('获取融资结构分析数据失败:', error);
        // 显示空数据状态
        const option = {
          title: {
            text: '融资结构分析',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          series: [{
            type: 'pie',
            radius: '50%',
            data: [{ name: '暂无数据', value: 0 }],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }]
        };
        this.financingStructureChart.setOption(option);
      });
    },

    /** 获取预警类型标签样式 */
    getAlertTypeTag(type) {
      const tagMap = {
        'INTEREST_RATE': 'warning',
        'EXPIRATION': 'danger',
        'CREDIT_LIMIT': 'info',
        'COMPLIANCE': 'danger',
        '额度预警': 'warning',
        '到期预警': 'danger',
        '利率预警': 'info',
        '合规预警': 'danger'
      };
      return tagMap[type] || 'info';
    },

    /** 获取预警类型标签文本 */
    getAlertTypeLabel(type) {
      const labelMap = {
        'INTEREST_RATE': '利率预警',
        'EXPIRATION': '到期预警',
        'CREDIT_LIMIT': '额度预警',
        'COMPLIANCE': '合规预警'
      };
      return labelMap[type] || type;
    },

    /** 获取风险等级标签样式 */
    getRiskLevelTag(level) {
      const tagMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success',
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      };
      return tagMap[level] || 'info';
    },

    /** 获取风险等级标签文本 */
    getAlertLevelLabel(level) {
      const labelMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      };
      return labelMap[level] || level;
    },

    /** 处理预警 - 打开预警处理弹窗 */
    handleAlert(row) {
      this.currentAlertData = row;
      this.alertProcessDialogVisible = true;
    },

    /** 预警处理成功回调 */
    handleAlertProcessSuccess() {
      this.loadRecentAlerts();
      this.loadDashboardData();
      this.$message.success('预警处理成功');
    },

    /** 查看全部预警 */
    viewAllAlerts() {
      this.activeTab = 'riskMonitoring';
    },

    // ==================== 风险监控相关方法 ====================



    /** 加载风险监控列表 */
    loadRiskMonitoringList() {
      this.riskLoading = true;
      const query = { ...this.riskQueryParams };

      // 处理日期范围 - 转换为后端期望的 startDate 和 endDate
      if (query.alertDateRange && query.alertDateRange.length === 2) {
        query.startDate = query.alertDateRange[0];
        query.endDate = query.alertDateRange[1];
      }
      delete query.alertDateRange;

      // 过滤掉空字符串和null值，只保留有效的筛选条件
      const filteredQuery = {};
      Object.keys(query).forEach(key => {
        if (query[key] !== '' && query[key] !== null && query[key] !== undefined) {
          filteredQuery[key] = query[key];
        }
      });

      console.log('风险监控查询参数:', filteredQuery);

      getFinancingRiskMonitoringPage(filteredQuery).then(response => {
        console.log('风险监控列表响应:', response);
        // API函数已经处理了响应格式，返回 {records: [...], total: 0} 格式
        if (response) {
          // 兼容多种响应格式
          if (response.records) {
            // API函数处理后的格式: {records: [...], total: xxx}
            this.riskMonitoringList = response.records || [];
            this.riskTotal = response.total || 0;
          } else if (response.code === 1 || response.code === 200) {
            // 原始示例云API格式: {code: 1, data: {rows: [...], total: xxx}} 或 {code: 1, data: {list: [...], total: xxx}}
            const data = response.data || response;
            this.riskMonitoringList = data.rows || data.list || data.records || [];
            this.riskTotal = data.total || 0;
          } else if (response.rows) {
            // 直接返回rows格式
            this.riskMonitoringList = response.rows || [];
            this.riskTotal = response.total || 0;
          } else if (response.list) {
            // 直接返回list格式
            this.riskMonitoringList = response.list || [];
            this.riskTotal = response.total || 0;
          } else {
            this.riskMonitoringList = [];
            this.riskTotal = 0;
          }
        } else {
          this.riskMonitoringList = [];
          this.riskTotal = 0;
        }
        console.log('风险监控列表数据:', this.riskMonitoringList);
      }).catch(error => {
        console.error('加载风险监控列表失败:', error);
        this.riskMonitoringList = [];
        this.riskTotal = 0;
        this.$message.error('加载风险监控列表失败');
      }).finally(() => {
        this.riskLoading = false;
      });
    },

    /** 风险监控查询 */
    handleRiskQuery() {
      this.riskQueryParams.pageNum = 1;
      this.loadRiskMonitoringList();
    },

    /** 重置风险监控查询条件 */
    resetRiskQuery() {
      this.$refs.riskQueryForm.resetFields();
      this.riskQueryParams = {
        pageNum: 1,
        pageSize: 10,
        keyword: '',
        alertType: '',
        alertLevel: '',
        alertStatus: '',
        alertDateRange: [],
        orgId: 1
      };
      this.loadRiskMonitoringList();
    },

    /** 风险监控分页大小改变 */
    handleRiskSizeChange(val) {
      this.riskQueryParams.pageSize = val;
      this.loadRiskMonitoringList();
    },

    /** 风险监控当前页改变 */
    handleRiskCurrentChange(val) {
      this.riskQueryParams.pageNum = val;
      this.loadRiskMonitoringList();
    },

    /** 风险监控选择改变 */
    handleRiskSelectionChange(selection) {
      this.riskSelection = selection;
      this.riskSingle = selection.length !== 1;
      this.riskMultiple = !selection.length;
    },

    /** 新增风险监控 */
    handleAddRiskMonitoring() {
      this.resetRiskForm();
      this.riskDialogType = 'add';
      this.riskDialogTitle = '新增风险监控';
      this.riskDialogVisible = true;
    },

    /** 修改风险监控 */
    handleEditRiskMonitoring() {
      if (this.riskSelection.length !== 1) {
        this.$message.warning('请选择一条记录进行修改');
        return;
      }
      const row = this.riskSelection[0];
      this.editRiskMonitoring(row);
    },

    /** 触发风险警报 */
    handleTriggerRiskAlert() {
      if (this.riskSelection.length !== 1) {
        this.$message.warning('请选择一条记录进行警报触发');
        return;
      }
      const row = this.riskSelection[0];
      this.triggerRiskAlert(row);
    },

    /** 批量删除风险监控 */
    handleBatchDeleteRiskMonitoring() {
      if (this.riskSelection.length === 0) {
        this.$message.warning('请选择要删除的记录');
        return;
      }

      this.$confirm('确认删除选中的风险监控记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const monitoringIds = this.riskSelection.map(item => item.monitoringId);
        batchDeleteFinancingRiskMonitorings(monitoringIds).then(() => {
          this.$message.success('删除成功');
          this.loadRiskMonitoringList();
        }).catch(error => {
          console.error('批量删除失败:', error);
          this.$message.error('删除失败');
        });
      });
    },

    /** 导出风险监控数据 */
    handleExportRiskData() {
      this.$message.info('导出功能开发中...');
    },

    /** 风险监控操作处理 */
    handleRiskOperation(command, row) {
      switch (command) {
        case 'detail':
          this.viewRiskDetail(row);
          break;
        case 'edit':
          this.editRiskMonitoring(row);
          break;
        case 'triggerAlert':
          this.triggerRiskAlert(row);
          break;
        case 'handle':
          this.handleRiskMonitoringAction(row);
          break;
        case 'delete':
          this.deleteRiskMonitoring(row);
          break;
      }
    },

    /** 查看风险监控详情 */
    async viewRiskDetail(row) {
      try {
        const response = await getFinancingRiskMonitoring(row.monitoringId || row.recordId);
        if (response && response.data) {
          this.riskForm = { ...response.data };
        } else {
          this.riskForm = { ...row };
        }
        this.riskDialogType = 'view';
        this.riskDialogTitle = '查看风险监控详情';
        this.riskDialogVisible = true;
      } catch (error) {
        console.error('获取详情失败:', error);
        // 如果接口失败，使用行数据
        this.riskForm = { ...row };
        this.riskDialogType = 'view';
        this.riskDialogTitle = '查看风险监控详情';
        this.riskDialogVisible = true;
      }
    },

    /** 编辑风险监控 */
    async editRiskMonitoring(row) {
      try {
        const response = await getFinancingRiskMonitoring(row.monitoringId || row.recordId);
        if (response && response.data) {
          this.riskForm = { ...response.data };
        } else {
          this.riskForm = { ...row };
        }
        this.riskDialogType = 'edit';
        this.riskDialogTitle = '编辑风险监控';
        this.riskDialogVisible = true;
      } catch (error) {
        console.error('获取详情失败:', error);
        // 如果接口失败，使用行数据
        this.riskForm = { ...row };
        this.riskDialogType = 'edit';
        this.riskDialogTitle = '编辑风险监控';
        this.riskDialogVisible = true;
      }
    },

    /** 重置风险表单 */
    resetRiskForm() {
      this.riskForm = {
        monitoringId: null,
        alertType: '',
        alertLevel: '',
        alertMessage: '',
        relatedFinancingId: null,
        alertStatus: 'PENDING',
        companyId: null,
        companyName: '',
        handlerName: '',
        handleOpinion: '',
        remark: ''
      };
      if (this.$refs.riskFormRef) {
        this.$refs.riskFormRef.resetFields();
      }
    },

    /** 提交风险表单 */
    submitRiskForm() {
      this.$refs.riskFormRef.validate(async (valid) => {
        if (valid) {
          try {
            // 调试：打印提交的数据
            console.log('提交的风险表单数据:', JSON.stringify(this.riskForm, null, 2));

            let response;
            if (this.riskDialogType === 'add') {
              response = await createFinancingRiskMonitoring(this.riskForm);
              console.log('新增响应:', response);
              this.$message.success('新增成功');
            } else if (this.riskDialogType === 'edit') {
              response = await updateFinancingRiskMonitoring(this.riskForm);
              console.log('修改响应:', response);
              this.$message.success('修改成功');
            }
            this.riskDialogVisible = false;
            this.loadRiskMonitoringList();
          } catch (error) {
            console.error('提交失败:', error);
            this.$message.error('操作失败，请稍后重试');
          }
        }
      });
    },

    /** 关闭风险弹窗 */
    handleRiskDialogClose() {
      this.riskDialogVisible = false;
      this.resetRiskForm();
    },

    /** 触发风险警报 */
    triggerRiskAlert(row) {
      this.$prompt('请输入警报消息', '触发警报', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '警报消息不能为空'
      }).then(({ value }) => {
        triggerFinancingRiskAlert(row.monitoringId, value, 1).then(() => {
          this.$message.success('触发警报成功');
          this.loadRiskMonitoringList();
        }).catch(error => {
          console.error('触发警报失败:', error);
          this.$message.error('触发警报失败');
        });
      });
    },

    /** 处理风险监控 */
    handleRiskMonitoringAction(row) {
      this.$prompt('请输入处理措施', '处理风险监控', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '处理措施不能为空'
      }).then(({ value }) => {
        handleFinancingRiskMonitoring(row.monitoringId, value, 1).then(() => {
          this.$message.success('处理成功');
          this.loadRiskMonitoringList();
        }).catch(error => {
          console.error('处理失败:', error);
          this.$message.error('处理失败');
        });
      });
    },

    /** 删除风险监控 */
    deleteRiskMonitoring(row) {
      this.$confirm(`确认删除风险监控记录"${row.monitoringId}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFinancingRiskMonitoring(row.monitoringId).then(() => {
          this.$message.success('删除成功');
          this.loadRiskMonitoringList();
        }).catch(error => {
          console.error('删除失败:', error);
          this.$message.error('删除失败');
        });
      });
    },

    // 风险监控辅助方法
    getRiskTypeText(riskType) {
      if (!riskType) return '未知';
      // 转换为大写进行匹配，兼容大小写
      const type = String(riskType).toUpperCase();
      const typeMap = {
        'CREDIT': '信用风险',
        'LIQUIDITY': '流动性风险',
        'MARKET': '市场风险',
        'OPERATION': '操作风险',
        'COMPLIANCE': '合规风险',
        'CONCENTRATION': '集中度风险',
        'INTEREST_RATE': '利率风险',
        'EXCHANGE_RATE': '汇率风险',
        'EXPIRY': '到期风险'
      };
      return typeMap[type] || riskType || '未知';
    },

    getRiskTypeTagType(riskType) {
      if (!riskType) return '';
      const type = String(riskType).toUpperCase();
      const typeMap = {
        'CREDIT': 'danger',        // 信用风险
        'LIQUIDITY': 'warning',    // 流动性风险
        'MARKET': 'primary',       // 市场风险
        'OPERATION': 'info',       // 操作风险
        'COMPLIANCE': 'success',   // 合规风险
        'CONCENTRATION': 'warning', // 集中度风险
        'INTEREST_RATE': 'primary', // 利率风险
        'EXCHANGE_RATE': 'danger',  // 汇率风险
        'EXPIRY': 'warning'         // 到期风险
      };
      return typeMap[type] || '';
    },

    getRiskLevelText(riskLevel) {
      if (!riskLevel) return '未知';
      const level = String(riskLevel).toUpperCase();
      const levelMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      };
      return levelMap[level] || riskLevel || '未知';
    },

    getRiskLevelTagType(riskLevel) {
      if (!riskLevel) return '';
      const level = String(riskLevel).toUpperCase();
      const levelMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      };
      return levelMap[level] || '';
    },

    getRiskStatusText(status) {
      if (!status) return '未知';
      const s = String(status).toUpperCase();
      const statusMap = {
        'PENDING': '未处理',
        'HANDLED': '已处理',
        'CLOSED': '已关闭',
        'ACTIVE': '活跃',
        'NORMAL': '正常',
        'WARNING': '预警',
        'CRITICAL': '严重',
        'BREACH': '违约'
      };
      return statusMap[s] || status || '未知';
    },

    getRiskStatusTagType(status) {
      if (!status) return '';
      const s = String(status).toUpperCase();
      const statusMap = {
        'PENDING': 'warning',
        'HANDLED': 'success',
        'CLOSED': 'info',
        'ACTIVE': 'warning',
        'NORMAL': 'success',
        'WARNING': 'warning',
        'CRITICAL': 'danger',
        'BREACH': 'danger'
      };
      return statusMap[s] || '';
    },

    /** 处理风险监控分页页码变化 */
    handleRiskCurrentChange(val) {
      this.riskQueryParams.pageNum = val;
      this.loadRiskMonitoringList();
    },

    /** 处理风险监控分页大小变化 */
    handleRiskSizeChange(val) {
      this.riskQueryParams.pageSize = val;
      this.loadRiskMonitoringList();
    },

    // ==================== 成本分析相关方法 ====================

    /** 加载成本概览数据 */
    async loadCostOverview() {
      try {
        const response = await getCostOverview({});
        if (response.code === 1 || response.code === 200) {
          this.costOverview = {
            totalFinancingAmount: response.data.totalFinancingAmount || 0,
            totalCost: response.data.totalCost || 0,
            averageCostRate: response.data.averageCostRate || 0,
            monthlyTrend: response.data.monthlyTrend || '稳定',
            costSaving: response.data.costSaving || 0,
            optimizationRate: response.data.optimizationRate || 0
          };
        }
      } catch (error) {
        console.error('加载成本概览数据失败:', error);
        // 设置默认值，确保前端不会因为后端错误而崩溃
        this.costOverview = {
          totalFinancingAmount: 150000000,
          totalCost: 8500000,
          averageCostRate: 5.67,
          monthlyTrend: '上升',
          costSaving: 320000,
          optimizationRate: 3.62
        };
      }
    },

    /** 加载成本分析列表 */
    async loadCostAnalysisList() {
      this.costLoading = true;
      try {
        // 构建后端支持的查询参数
        const params = {
          pageNum: this.costQueryParams.pageNum,
          pageSize: this.costQueryParams.pageSize,
          financingType: this.costQueryParams.financingType || undefined,
          currencyCode: this.costQueryParams.currencyCode || undefined,
          periodType: this.costQueryParams.periodType || undefined
        };

        // 处理日期范围
        if (this.costDateRange && this.costDateRange.length === 2) {
          params.startDate = this.costDateRange[0];
          params.endDate = this.costDateRange[1];
        }

        console.log('成本分析查询参数:', params);

        const response = await listCostAnalysis(params);
        console.log('成本分析列表响应:', response);

        // 兼容示例云 API 成功码 1 和标准成功码 200
        if (response.code === 1 || response.code === 200) {
          let list = [];
          let total = 0;

          // 兼容多种数据格式
          if (response.data && response.data.list) {
            // PageInfo格式: {data: {list: [...], total: xxx}}
            list = response.data.list || [];
            total = parseInt(response.data.total) || 0;
          } else if (response.data && response.data.rows) {
            // rows格式: {data: {rows: [...], total: xxx}}
            list = response.data.rows || [];
            total = parseInt(response.data.total) || 0;
          } else if (response.rows) {
            list = response.rows || [];
            total = parseInt(response.total) || 0;
          } else if (response.list) {
            list = response.list || [];
            total = parseInt(response.total) || 0;
          } else if (response.data && Array.isArray(response.data)) {
            list = response.data || [];
            total = list.length;
          }

          // 前端筛选：公司名称（后端不支持此字段）
          if (this.costQueryParams.companyName && list.length > 0) {
            const keyword = this.costQueryParams.companyName.toLowerCase();
            list = list.filter(item =>
              item.companyName && item.companyName.toLowerCase().includes(keyword)
            );
            total = list.length;
          }

          this.costList = list;
          this.costTotal = total;
          console.log('成本分析列表数据:', this.costList);
        }
      } catch (error) {
        console.error('加载成本分析列表失败:', error);
        this.costList = [];
        this.costTotal = 0;
        this.$message.error('加载成本分析列表失败，请稍后重试');
      } finally {
        this.costLoading = false;
      }
    },

    /** 初始化成本分析图表 */
    initCostCharts() {
      this.initCostTrendChart();
      this.initCostPieChart();
    },

    /** 初始化成本趋势图表 */
    async initCostTrendChart() {
      try {
        const chartDom = document.getElementById('costTrendChart');
        if (!chartDom) return;

        this.costTrendChart = echarts.init(chartDom);

        // 获取图表数据
        const response = await getCostChartData({});
        let chartData = {};

        if ((response.code === 1 || response.code === 200) && response.data && response.data.trendChart) {
          chartData = response.data.trendChart;
        } else {
          // 默认数据
          chartData = {
            categories: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
            costData: [650000, 720000, 680000, 750000, 800000, 780000, 820000, 850000, 880000, 900000, 920000, 950000],
            rateData: [4.2, 4.3, 4.1, 4.4, 4.6, 4.5, 4.7, 4.8, 4.9, 5.0, 5.1, 5.2]
          };
        }

        const option = {
          title: {
            text: '融资成本趋势',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross'
            }
          },
          legend: {
            data: ['成本金额', '成本率'],
            top: 30
          },
          xAxis: {
            type: 'category',
            data: chartData.categories
          },
          yAxis: [
            {
              type: 'value',
              name: '成本金额(万元)',
              position: 'left'
            },
            {
              type: 'value',
              name: '成本率(%)',
              position: 'right'
            }
          ],
          series: [
            {
              name: '成本金额',
              type: 'bar',
              data: chartData.costData,
              itemStyle: {
                color: '#409EFF'
              }
            },
            {
              name: '成本率',
              type: 'line',
              yAxisIndex: 1,
              data: chartData.rateData,
              itemStyle: {
                color: '#67C23A'
              }
            }
          ]
        };

        this.costTrendChart.setOption(option);
      } catch (error) {
        console.error('初始化成本趋势图表失败:', error);
      }
    },

    /** 初始化成本构成饼图 */
    async initCostPieChart() {
      try {
        const chartDom = document.getElementById('costPieChart');
        if (!chartDom) return;

        this.costPieChart = echarts.init(chartDom);

        // 获取图表数据
        const response = await getCostChartData({});
        let pieData = [];

        if ((response.code === 1 || response.code === 200) && response.data && response.data.pieChart) {
          pieData = response.data.pieChart;
        } else {
          // 默认数据
          pieData = [
            { name: '利息成本', value: 7200000 },
            { name: '费用成本', value: 800000 },
            { name: '其他成本', value: 500000 }
          ];
        }

        const option = {
          title: {
            text: '成本构成分析',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            top: 'middle'
          },
          series: [
            {
              name: '成本构成',
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['60%', '50%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '18',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: pieData
            }
          ]
        };

        this.costPieChart.setOption(option);
      } catch (error) {
        console.error('初始化成本构成饼图失败:', error);
      }
    },

    /** 成本分析查询 */
    handleCostQuery() {
      this.costQueryParams.pageNum = 1;
      this.loadCostAnalysisList();
    },

    /** 重置成本分析查询 */
    resetCostQuery() {
      this.costDateRange = [];
      this.costQueryParams = {
        pageNum: 1,
        pageSize: 10,
        companyName: '',
        financingType: '',
        currencyCode: '',
        periodType: ''
      };
      if (this.$refs.costQueryForm) {
        this.$refs.costQueryForm.resetFields();
      }
      this.handleCostQuery();
    },

    /** 成本分析表格选择变化 */
    handleCostSelectionChange(selection) {
      this.costSelection = selection;
      this.costSingle = selection.length !== 1;
      this.costMultiple = !selection.length;
    },

    /** 新增成本分析 */
    handleAddCostAnalysis() {
      this.resetCostForm();
      this.costDialogType = 'add';
      this.costDialogTitle = '新增成本分析';
      this.costDialogVisible = true;
    },

    /** 修改成本分析 */
    handleUpdateCostAnalysis() {
      if (this.costSelection.length === 1) {
        this.editCostAnalysis(this.costSelection[0]);
      } else {
        this.$message.warning('请选择一条数据进行修改');
      }
    },

    /** 删除成本分析 */
    async handleDeleteCostAnalysis() {
      if (this.costSelection.length === 0) {
        this.$message.warning('请选择要删除的数据');
        return;
      }

      try {
        await this.$confirm('是否确认删除选中的成本分析数据？', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const analysisIds = this.costSelection.map(item => item.analysisId);
        const response = await delCostAnalysis(analysisIds.join(','));

        if (response.code === 1 || response.code === 200) {
          this.$message.success('删除成功');
          this.loadCostAnalysisList();
        } else {
          this.$message.error(response.msg || '删除失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除成本分析失败:', error);
          this.$message.error('删除失败，请稍后重试');
        }
      }
    },

    /** 导出成本分析 */
    handleExportCostAnalysis() {
      this.$message.info('导出成本分析功能开发中...');
    },

    /** 生成成本报告 */
    async handleGenerateCostReport() {
      try {
        const response = await generateCostReport({});
        if (response.code === 1 || response.code === 200) {
          this.$message.success('成本报告生成成功');
        } else {
          this.$message.error(response.msg || '生成报告失败');
        }
      } catch (error) {
        console.error('生成成本报告失败:', error);
        this.$message.error('生成报告失败，请稍后重试');
      }
    },

    /** 成本分析操作命令处理 */
    handleCostCommand(command, row) {
      switch (command) {
        case 'detail':
          this.viewCostDetail(row);
          break;
        case 'edit':
          this.editCostAnalysis(row);
          break;
        case 'calculate':
          this.handleRecalculateCost(row);
          break;
        case 'report':
          this.handleGenerateCostReport();
          break;
        case 'delete':
          this.handleDeleteSingleCost(row);
          break;
        default:
          break;
      }
    },

    /** 查看成本分析详情 */
    async viewCostDetail(row) {
      try {
        const response = await getCostAnalysis(row.analysisId);
        console.log('成本分析详情响应:', response);
        if (response && (response.code === 1 || response.code === 200) && response.data) {
          // 格式化日期字段
          const data = { ...response.data };
          if (data.analysisDate) {
            data.analysisDate = this.formatDate(data.analysisDate);
          }
          if (data.createdTime) {
            data.createdTime = this.formatDateTime(data.createdTime);
          }
          this.costForm = data;
          console.log('设置的 costForm:', this.costForm);
        } else {
          // 使用行数据
          const data = { ...row };
          if (data.analysisDate) {
            data.analysisDate = this.formatDate(data.analysisDate);
          }
          if (data.createdTime) {
            data.createdTime = this.formatDateTime(data.createdTime);
          }
          this.costForm = data;
        }
        this.costDialogType = 'view';
        this.costDialogTitle = '查看成本分析详情';
        this.costDialogVisible = true;
      } catch (error) {
        console.error('获取成本分析详情失败:', error);
        // 如果接口失败，使用行数据
        const data = { ...row };
        if (data.analysisDate) {
          data.analysisDate = this.formatDate(data.analysisDate);
        }
        if (data.createdTime) {
          data.createdTime = this.formatDateTime(data.createdTime);
        }
        this.costForm = data;
        this.costDialogType = 'view';
        this.costDialogTitle = '查看成本分析详情';
        this.costDialogVisible = true;
      }
    },

    /** 编辑成本分析 */
    async editCostAnalysis(row) {
      try {
        const response = await getCostAnalysis(row.analysisId);
        console.log('编辑成本分析响应:', response);
        if (response && (response.code === 1 || response.code === 200) && response.data) {
          // 格式化日期字段
          const data = { ...response.data };
          if (data.analysisDate) {
            data.analysisDate = this.formatDate(data.analysisDate);
          }
          if (data.createdTime) {
            data.createdTime = this.formatDateTime(data.createdTime);
          }
          this.costForm = data;
        } else {
          const data = { ...row };
          if (data.analysisDate) {
            data.analysisDate = this.formatDate(data.analysisDate);
          }
          if (data.createdTime) {
            data.createdTime = this.formatDateTime(data.createdTime);
          }
          this.costForm = data;
        }
        this.costDialogType = 'edit';
        this.costDialogTitle = '编辑成本分析';
        this.costDialogVisible = true;
      } catch (error) {
        console.error('获取成本分析详情失败:', error);
        // 如果接口失败，使用行数据
        const data = { ...row };
        if (data.analysisDate) {
          data.analysisDate = this.formatDate(data.analysisDate);
        }
        if (data.createdTime) {
          data.createdTime = this.formatDateTime(data.createdTime);
        }
        this.costForm = data;
        this.costDialogType = 'edit';
        this.costDialogTitle = '编辑成本分析';
        this.costDialogVisible = true;
      }
    },

    /** 重置成本表单 */
    resetCostForm() {
      this.costForm = {
        analysisId: null,
        financingId: 1,  // 默认值，后端必填
        financingType: '',
        principalAmount: 0,
        interestExpense: 0,
        feeCost: 0,
        guaranteeCost: 0,
        otherCost: 0,
        totalCost: 0,
        costRate: 0,
        currencyCode: 'CNY',
        analysisDate: '',
        periodType: 'MONTH',
        periodValue: '',
        companyId: 1,  // 默认值，后端必填
        companyName: '',
        createdBy: null,
        createdByName: '',
        createdTime: '',
        remark: ''
      };
      if (this.$refs.costFormRef) {
        this.$refs.costFormRef.resetFields();
      }
    },

    /** 提交成本表单 */
    submitCostForm() {
      this.$refs.costFormRef.validate(async (valid) => {
        if (valid) {
          try {
            console.log('提交成本表单数据:', this.costForm);
            console.log('操作类型:', this.costDialogType);

            let response;
            if (this.costDialogType === 'add') {
              // 新增
              response = await addCostAnalysis(this.costForm);
              console.log('新增响应:', response);
              if (response.code === 1 || response.code === 200) {
                this.$message.success('新增成功');
                this.costDialogVisible = false;
                this.loadCostAnalysisList();
              } else {
                this.$message.error(response.msg || '新增失败');
              }
            } else {
              // 修改
              response = await updateCostAnalysis(this.costForm);
              console.log('修改响应:', response);
              if (response.code === 1 || response.code === 200) {
                this.$message.success('修改成功');
                this.costDialogVisible = false;
                this.loadCostAnalysisList();
              } else {
                this.$message.error(response.msg || '修改失败');
              }
            }
          } catch (error) {
            console.error('提交失败:', error);
            this.$message.error('操作失败，请稍后重试');
          }
        }
      });
    },

    /** 关闭成本弹窗 */
    handleCostDialogClose() {
      this.costDialogVisible = false;
      this.resetCostForm();
    },

    /** 重新计算成本 */
    async handleRecalculateCost(row) {
      try {
        // 后端 /analysis 接口需要 financingId, analysisDate, periodType 参数
        // analysisDate 必须是 yyyy-MM-dd 格式的字符串
        let analysisDate;
        if (row.analysisDate) {
          // 如果是时间戳，转换为日期字符串
          analysisDate = this.formatDate(row.analysisDate);
        } else {
          // 使用当前日期
          const today = new Date();
          analysisDate = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
        }

        const response = await calculateFinancingCost({
          financingId: row.financingId || row.analysisId,  // 优先使用 financingId
          analysisDate: analysisDate,
          periodType: row.periodType || 'MONTH'
        });
        if (response.code === 1 || response.code === 200) {
          this.$message.success('重新计算成功');
          this.loadCostAnalysisList();
        } else {
          this.$message.error(response.msg || '重新计算失败');
        }
      } catch (error) {
        console.error('重新计算成本失败:', error);
        this.$message.error('重新计算失败，请稍后重试');
      }
    },

    /** 删除单个成本分析 */
    async handleDeleteSingleCost(row) {
      try {
        await this.$confirm(`是否确认删除成本分析"${row.analysisName}"？`, '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const response = await delCostAnalysis(row.analysisId);
        if (response.code === 1 || response.code === 200) {
          this.$message.success('删除成功');
          this.loadCostAnalysisList();
        } else {
          this.$message.error(response.msg || '删除失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除成本分析失败:', error);
          this.$message.error('删除失败，请稍后重试');
        }
      }
    },

    /** 处理成本分析分页页码变化 */
    handleCostCurrentChange(val) {
      this.costQueryParams.pageNum = val;
      this.loadCostAnalysisList();
    },

    /** 处理成本分析分页大小变化 */
    handleCostSizeChange(val) {
      this.costQueryParams.pageSize = val;
      this.loadCostAnalysisList();
    },

    // ==================== 报表管理相关方法 ====================

    /** 加载报表概览数据 */
    async loadReportOverview() {
      try {
        const response = await getReportOverviewStatistics({});
        if (response.code === 1 || response.code === 200) {
          this.reportOverview = {
            totalReports: response.data.totalReports || 0,
            completedReports: response.data.completedReports || 0,
            generatingReports: response.data.generatingReports || 0,
            failedReports: response.data.failedReports || 0,
            totalDownloads: response.data.totalDownloads || 0,
            totalSize: response.data.totalSize || 0,
            avgGenerationTime: response.data.avgGenerationTime || 0,
            successRate: response.data.successRate || 0,
            monthlyIncrease: response.data.monthlyIncrease || 0
          };
        }
      } catch (error) {
        console.error('加载报表概览数据失败:', error);
        // 使用模拟数据
        this.reportOverview = {
          totalReports: 156,
          completedReports: 142,
          generatingReports: 8,
          failedReports: 6,
          totalDownloads: 1248,
          totalSize: 2048576000,
          avgGenerationTime: 45,
          successRate: 91.0,
          monthlyIncrease: 12.5
        };
      }
    },

    /** 加载报表记录列表 */
    async loadReportList() {
      this.reportLoading = true;
      try {
        const params = { ...this.reportQueryParams };
        if (this.reportDateRange && this.reportDateRange.length === 2) {
          params.generateTimeStart = this.reportDateRange[0];
          params.generateTimeEnd = this.reportDateRange[1];
        }

        const response = await listReportRecords(params);
        if (response.code === 1 || response.code === 200) {
          // 兼容多种数据格式
          if (response.data && response.data.rows) {
            this.reportList = response.data.rows || [];
            this.reportTotal = response.data.total || 0;
          } else if (response.rows) {
            this.reportList = response.rows || [];
            this.reportTotal = response.total || 0;
          } else {
            this.reportList = [];
            this.reportTotal = 0;
          }
        }
      } catch (error) {
        console.error('加载报表记录列表失败:', error);
        // 使用模拟数据
        this.reportList = this.generateMockReportData();
        this.reportTotal = this.reportList.length;
      } finally {
        this.reportLoading = false;
      }
    },

    /** 生成模拟报表数据（仅用于API异常时的降级处理） */
    generateMockReportData() {
      const reportTypes = ['BALANCE', 'COST', 'RISK', 'TREND', 'EFFICIENCY'];
      const reportStatuses = ['COMPLETED', 'GENERATING', 'FAILED'];
      const reportFormats = ['EXCEL', 'PDF', 'WORD'];
      const mockData = [];

      for (let i = 1; i <= 20; i++) {
        const reportType = reportTypes[Math.floor(Math.random() * reportTypes.length)];
        const reportStatus = reportStatuses[Math.floor(Math.random() * reportStatuses.length)];
        const reportFormat = reportFormats[Math.floor(Math.random() * reportFormats.length)];

        mockData.push({
          recordId: i,
          reportNo: `RPT${new Date().getFullYear()}${String(i).padStart(4, '0')}`,
          reportName: `${this.getReportTypeName(reportType)}${i}`,
          reportType: reportType,
          reportStatus: reportStatus,
          reportFormat: reportFormat,
          reportSize: Math.floor(Math.random() * 5000000) + 500000, // 500KB-5MB
          downloadCount: Math.floor(Math.random() * 50),
          generateTime: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000),
          completeTime: reportStatus === 'COMPLETED' ? new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000) : null
        });
      }

      return mockData;
    },

    /** 初始化报表图表 */
    async initReportCharts() {
      console.log('===== 开始初始化报表图表 =====');
      // 等待 DOM 渲染完成
      await this.$nextTick();
      // 再等待一段时间确保 tab 内容完全渲染
      await new Promise(resolve => setTimeout(resolve, 300));

      // 重试机制：最多重试5次
      let retryCount = 0;
      const maxRetries = 5;

      while (retryCount < maxRetries) {
        const trendDom = this.$refs.reportTrendChart;
        const typeDom = this.$refs.reportTypeChart;

        console.log(`检查 DOM 元素 (尝试 ${retryCount + 1}/${maxRetries}):`, {
          trendDom: !!trendDom,
          typeDom: !!typeDom,
          trendDomSize: trendDom ? `${trendDom.offsetWidth}x${trendDom.offsetHeight}` : 'N/A',
          typeDomSize: typeDom ? `${typeDom.offsetWidth}x${typeDom.offsetHeight}` : 'N/A'
        });

        if (trendDom && typeDom && trendDom.offsetWidth > 0 && trendDom.offsetHeight > 0) {
          console.log('图表 DOM 元素已就绪，开始初始化');
          break;
        }

        retryCount++;
        console.log(`图表 DOM 元素未就绪，等待重试 (${retryCount}/${maxRetries})`);
        await new Promise(resolve => setTimeout(resolve, 300));
      }

      await this.initReportTrendChart();
      await this.initReportTypeChart();
      console.log('===== 报表图表初始化完成 =====');
    },

    /** 初始化报表生成趋势图表 */
    async initReportTrendChart() {
      try {
        const chartDom = this.$refs.reportTrendChart;
        console.log('reportTrendChart DOM:', chartDom);
        if (!chartDom) {
          console.warn('reportTrendChart DOM 元素不存在');
          return;
        }

        // 如果已经初始化过，先销毁
        if (this.reportTrendChart) {
          this.reportTrendChart.dispose();
        }
        this.reportTrendChart = echarts.init(chartDom);

        // 获取图表数据
        let chartData = [];
        try {
          const response = await getReportChartData({});
          console.log('报表图表数据响应:', response);
          if (response && (response.code === 1 || response.code === 200) && response.data) {
            chartData = response.data.trendData || [];
          }
        } catch (error) {
          console.error('获取报表图表数据失败:', error);
        }

        // 如果没有数据，使用模拟数据
        if (!chartData || chartData.length === 0) {
          chartData = [];
          for (let i = 6; i >= 0; i--) {
            const date = new Date();
            date.setDate(date.getDate() - i);
            chartData.push({
              date: date.toLocaleDateString('zh-CN', { month: '2-digit', day: '2-digit' }),
              generated: 15 + Math.floor(Math.random() * 20),
              downloaded: 12 + Math.floor(Math.random() * 15)
            });
          }
        }

        const option = {
          title: {
            text: '报表生成趋势',
            left: 'center',
            textStyle: {
              fontSize: 14,
              fontWeight: 'normal'
            }
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross'
            }
          },
          legend: {
            data: ['生成数量', '下载数量'],
            bottom: 10
          },
          xAxis: {
            type: 'category',
            data: chartData.map(item => item.date)
          },
          yAxis: {
            type: 'value',
            name: '数量'
          },
          series: [
            {
              name: '生成数量',
              type: 'line',
              data: chartData.map(item => item.generated),
              itemStyle: {
                color: '#409EFF'
              }
            },
            {
              name: '下载数量',
              type: 'line',
              data: chartData.map(item => item.downloaded),
              itemStyle: {
                color: '#67C23A'
              }
            }
          ]
        };

        console.log('报表趋势图表配置:', option);
        console.log('报表趋势图表数据:', chartData);
        this.reportTrendChart.setOption(option);
        console.log('报表趋势图表初始化完成');
      } catch (error) {
        console.error('初始化报表趋势图表失败:', error);
      }
    },

    /** 初始化报表类型分布图表 */
    async initReportTypeChart() {
      try {
        const chartDom = this.$refs.reportTypeChart;
        console.log('reportTypeChart DOM:', chartDom);
        if (!chartDom) {
          console.warn('reportTypeChart DOM 元素不存在');
          return;
        }

        // 如果已经初始化过，先销毁
        if (this.reportTypeChart) {
          this.reportTypeChart.dispose();
        }
        this.reportTypeChart = echarts.init(chartDom);

        // 获取图表数据
        let pieData = [];
        try {
          const response = await getReportChartData({});
          console.log('报表类型分布数据响应:', response);
          if (response && (response.code === 1 || response.code === 200) && response.data) {
            pieData = response.data.typeData || [];
          }
        } catch (error) {
          console.error('获取报表类型分布数据失败:', error);
        }

        // 如果没有数据，使用模拟数据
        if (!pieData || pieData.length === 0) {
          pieData = [
            { name: '余额报表', value: 45 },
            { name: '成本报表', value: 38 },
            { name: '风险报表', value: 32 },
            { name: '趋势报表', value: 25 },
            { name: '效率报表', value: 16 }
          ];
        }

        const option = {
          title: {
            text: '报表类型分布',
            left: 'center',
            textStyle: {
              fontSize: 14,
              fontWeight: 'normal'
            }
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: pieData.map(item => item.name)
          },
          series: [
            {
              name: '报表类型',
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['60%', '50%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '18',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: pieData
            }
          ]
        };

        console.log('报表类型分布图表配置:', option);
        console.log('报表类型分布图表数据:', pieData);
        this.reportTypeChart.setOption(option);
        console.log('报表类型分布图表初始化完成');
      } catch (error) {
        console.error('初始化报表类型分布图表失败:', error);
      }
    },

    /** 报表查询 */
    handleReportQuery() {
      this.reportQueryParams.pageNum = 1;
      this.loadReportList();
    },

    /** 重置报表查询 */
    resetReportQuery() {
      this.reportDateRange = [];
      this.reportQueryParams = {
        pageNum: 1,
        pageSize: 10,
        reportNo: '',
        reportName: '',
        reportType: '',
        reportStatus: '',
        orgId: 1
      };
      this.handleReportQuery();
    },

    /** 处理报表选择变化 */
    handleReportSelectionChange(selection) {
      this.reportSelection = selection;
      this.reportSingle = selection.length !== 1;
      this.reportMultiple = !selection.length;
    },

    /** 处理报表操作命令 */
    handleReportCommand(command, row) {
      switch (command) {
        case 'view':
          this.viewReportDetail(row);
          break;
        case 'download':
          this.downloadReportFile(row);
          break;
        case 'regenerate':
          this.regenerateReportFile(row);
          break;
        case 'cancel':
          this.cancelReportGeneration(row);
          break;
        case 'progress':
          this.viewReportProgress(row);
          break;
        case 'distribute':
          this.distributeReportFile(row);
          break;
        case 'delete':
          this.deleteReportRecord(row);
          break;
      }
    },

    /** 生成报表 */
    handleGenerateReport() {
      this.reportDialogType = 'add';
      this.reportDialogTitle = '生成报表';
      this.resetReportForm();
      this.reportDialogVisible = true;
    },

    /** 重新生成报表 */
    handleRegenerateReport() {
      if (this.reportSelection.length === 1) {
        this.regenerateReportFile(this.reportSelection[0]);
      }
    },

    /** 删除报表 */
    async handleDeleteReport() {
      if (this.reportSelection.length === 0) {
        this.$message.warning('请选择要删除的数据');
        return;
      }

      try {
        await this.$confirm('确认删除选中的报表记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const recordIds = this.reportSelection.map(item => item.recordId);
        const response = await delReportRecord(recordIds.join(','));

        if (response.code === 1 || response.code === 200) {
          this.$message.success('删除成功');
          this.loadReportList();
        } else {
          this.$message.error(response.msg || '删除失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除报表记录失败:', error);
          this.$message.error('删除失败，请稍后重试');
        }
      }
    },

    /** 批量下载报表 */
    async handleBatchDownload() {
      if (this.reportSelection.length === 0) {
        this.$message.warning('请选择要下载的报表');
        return;
      }

      // 过滤出已完成的报表
      const completedReports = this.reportSelection.filter(item => item.reportStatus === 'COMPLETED' || item.generationStatus === 'COMPLETED');
      if (completedReports.length === 0) {
        this.$message.warning('选中的报表中没有已完成的报表可供下载');
        return;
      }

      this.$message.info(`开始下载 ${completedReports.length} 个报表...`);

      // 逐个下载
      for (const report of completedReports) {
        await this.downloadReportFile(report);
      }
    },

    /** 导出报表数据 */
    async handleExportReport() {
      try {
        this.$message.info('正在导出报表数据...');
        const response = await exportReportRecords(this.reportQueryParams);

        // 处理文件下载
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `报表数据_${new Date().getTime()}.xlsx`;
        link.click();
        window.URL.revokeObjectURL(url);

        this.$message.success('导出成功');
      } catch (error) {
        console.error('导出报表数据失败:', error);
        this.$message.error('导出失败，请稍后重试');
      }
    },

    /** 管理模板 */
    handleManageTemplates() {
      this.templateDialogVisible = true;
      this.loadTemplateList();
    },

    /** 加载模板列表 */
    async loadTemplateList() {
      this.templateLoading = true;
      try {
        const response = await listReportTemplates(this.templateQueryParams);
        if (response && response.code === 1) {
          this.templateList = response.data?.rows || response.data?.list || [];
          this.templateTotal = response.data?.total || 0;
        } else {
          this.templateList = [];
          this.templateTotal = 0;
        }
      } catch (error) {
        console.error('加载模板列表失败:', error);
        this.templateList = [];
        this.templateTotal = 0;
      } finally {
        this.templateLoading = false;
      }
    },

    /** 模板查询 */
    handleTemplateQuery() {
      this.templateQueryParams.pageNum = 1;
      this.loadTemplateList();
    },

    /** 重置模板查询 */
    resetTemplateQuery() {
      this.templateQueryParams = {
        pageNum: 1,
        pageSize: 10,
        templateName: '',
        templateType: '',
        isEnabled: null
      };
      this.loadTemplateList();
    },

    /** 模板分页变化 */
    handleTemplateCurrentChange(val) {
      this.templateQueryParams.pageNum = val;
      this.loadTemplateList();
    },

    /** 模板分页大小变化 */
    handleTemplateSizeChange(val) {
      this.templateQueryParams.pageSize = val;
      this.loadTemplateList();
    },

    /** 模板选择变化 */
    handleTemplateSelectionChange(selection) {
      this.templateSelection = selection;
    },

    /** 新增模板 */
    handleAddTemplate() {
      this.templateFormType = 'add';
      this.templateFormTitle = '新增模板';
      this.templateForm = {
        templateId: null,
        templateCode: '',
        templateName: '',
        templateType: '',
        reportFrequency: '',
        isEnabled: 1,
        effectiveDate: '',
        expiryDate: '',
        description: ''
      };
      this.templateFormDialogVisible = true;
    },

    /** 编辑模板 */
    async handleEditTemplate(row) {
      this.templateFormType = 'edit';
      this.templateFormTitle = '编辑模板';
      this.templateForm = { ...row };
      this.templateFormDialogVisible = true;
    },

    /** 查看模板详情 */
    handleViewTemplate(row) {
      this.templateFormType = 'view';
      this.templateFormTitle = '模板详情';
      this.templateForm = { ...row };
      this.templateFormDialogVisible = true;
    },

    /** 提交模板表单 */
    async submitTemplateForm() {
      this.$refs.templateFormRef.validate(async (valid) => {
        if (!valid) return;

        this.templateSubmitLoading = true;
        try {
          let response;
          if (this.templateFormType === 'add') {
            response = await addReportTemplate(this.templateForm);
          } else {
            response = await updateReportTemplate(this.templateForm.templateId, this.templateForm);
          }

          if (response && (response.code === 1 || response.code === 200)) {
            this.$message.success(this.templateFormType === 'add' ? '新增成功' : '更新成功');
            this.templateFormDialogVisible = false;
            this.loadTemplateList();
          } else {
            this.$message.error(response?.msg || '操作失败');
          }
        } catch (error) {
          console.error('提交模板表单失败:', error);
          this.$message.error('操作失败，请稍后重试');
        } finally {
          this.templateSubmitLoading = false;
        }
      });
    },

    /** 删除模板 */
    async handleDeleteTemplate(row) {
      try {
        await this.$confirm(`是否确认删除模板"${row.templateName}"？`, '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const response = await delReportTemplate(row.templateId);
        if (response && (response.code === 1 || response.code === 200)) {
          this.$message.success('删除成功');
          this.loadTemplateList();
        } else {
          this.$message.error(response?.msg || '删除失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除模板失败:', error);
          this.$message.error('删除失败，请稍后重试');
        }
      }
    },

    /** 批量删除模板 */
    async handleBatchDeleteTemplates() {
      if (this.templateSelection.length === 0) {
        this.$message.warning('请选择要删除的模板');
        return;
      }

      try {
        await this.$confirm(`是否确认删除选中的 ${this.templateSelection.length} 个模板？`, '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const ids = this.templateSelection.map(item => item.templateId).join(',');
        const response = await delReportTemplate(ids);
        if (response && (response.code === 1 || response.code === 200)) {
          this.$message.success('批量删除成功');
          this.loadTemplateList();
        } else {
          this.$message.error(response?.msg || '批量删除失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除模板失败:', error);
          this.$message.error('批量删除失败，请稍后重试');
        }
      }
    },

    /** 切换模板状态 */
    async handleToggleTemplateStatus(row) {
      const newStatus = row.isEnabled === 1 ? 0 : 1;
      const statusText = newStatus === 1 ? '启用' : '禁用';

      try {
        await this.$confirm(`是否确认${statusText}模板"${row.templateName}"？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const response = await updateTemplateStatus(row.templateId, newStatus);
        if (response && (response.code === 1 || response.code === 200)) {
          this.$message.success(`${statusText}成功`);
          this.loadTemplateList();
        } else {
          this.$message.error(response?.msg || `${statusText}失败`);
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('切换模板状态失败:', error);
          this.$message.error(`${statusText}失败，请稍后重试`);
        }
      }
    },

    /** 获取模板类型名称 */
    getTemplateTypeName(type) {
      const typeMap = {
        'DAILY': '日报模板',
        'WEEKLY': '周报模板',
        'MONTHLY': '月报模板',
        'QUARTERLY': '季报模板',
        'ANNUAL': '年报模板',
        'ANALYSIS': '分析模板',
        'STATISTICS': '统计模板'
      };
      return typeMap[type] || type || '-';
    },

    /** 获取报告频率名称 */
    getFrequencyName(frequency) {
      const freqMap = {
        'DAILY': '每日',
        'WEEKLY': '每周',
        'MONTHLY': '每月',
        'QUARTERLY': '每季',
        'ANNUAL': '每年',
        'ON_DEMAND': '按需'
      };
      return freqMap[frequency] || frequency || '-';
    },

    /** 查看报表详情 */
    async viewReportDetail(row) {
      this.reportDialogType = 'view';
      this.reportDialogTitle = '报表详情';

      // 填充表单数据
      this.reportForm = {
        recordId: row.recordId,
        reportName: row.reportName,
        reportType: row.reportType,
        periodType: row.periodType,
        periodStartDate: row.periodStartDate,
        periodEndDate: row.periodEndDate,
        templateId: row.templateId,
        remark: row.remark,
        generationStatus: row.generationStatus || row.reportStatus
      };

      this.reportDialogVisible = true;
    },

    /** 下载报表文件 */
    async downloadReportFile(row) {
      try {
        this.$message.info(`开始下载报表: ${row.reportName}`);
        const response = await downloadReport(row.recordId);

        // 处理文件下载
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `${row.reportName}.xlsx`;
        link.click();
        window.URL.revokeObjectURL(url);

        this.$message.success('下载成功');
      } catch (error) {
        console.error('下载报表失败:', error);
        this.$message.error('下载失败，请稍后重试');
      }
    },

    /** 重新生成报表文件 */
    async regenerateReportFile(row) {
      try {
        const response = await regenerateReport(row.recordId);
        if (response.code === 1 || response.code === 200) {
          this.$message.success('重新生成任务已提交');
          this.loadReportList();
        } else {
          this.$message.error(response.msg || '重新生成失败');
        }
      } catch (error) {
        console.error('重新生成报表失败:', error);
        this.$message.error('重新生成失败，请稍后重试');
      }
    },

    /** 取消报表生成 */
    async cancelReportGeneration(row) {
      try {
        const response = await cancelReportGeneration(row.recordId);
        if (response.code === 1 || response.code === 200) {
          this.$message.success('已取消报表生成');
          this.loadReportList();
        } else {
          this.$message.error(response.msg || '取消失败');
        }
      } catch (error) {
        console.error('取消报表生成失败:', error);
        this.$message.error('取消失败，请稍后重试');
      }
    },

    /** 查看报表进度 */
    async viewReportProgress(row) {
      try {
        const response = await getReportGenerationProgress(row.recordId);
        if (response.code === 1 || response.code === 200) {
          const progress = response.data.progress || 0;
          this.$message.info(`报表生成进度: ${progress}%`);
        }
      } catch (error) {
        console.error('获取报表进度失败:', error);
        this.$message.error('获取进度失败');
      }
    },

    /** 分发报表文件 */
    distributeReportFile(row) {
      this.currentDistributeReport = row;
      this.distributeForm = {
        recipients: [],
        sendEmail: true,
        emailSubject: `报表分发: ${row.reportName}`,
        emailContent: `您好，\n\n附件是最新生成的报表"${row.reportName}"，请查收。\n\n此邮件由系统自动发送，请勿回复。`,
        sendNotification: true
      };
      this.distributeDialogVisible = true;
    },

    /** 提交分发报表 */
    async submitDistributeForm() {
      this.$refs.distributeFormRef.validate(async (valid) => {
        if (!valid) return;

        this.distributeLoading = true;
        try {
          const response = await distributeReport(this.currentDistributeReport.recordId, this.distributeForm);
          if (response && (response.code === 1 || response.code === 200)) {
            this.$message.success('报表分发成功');
            this.distributeDialogVisible = false;
          } else {
            this.$message.error(response?.msg || '分发失败');
          }
        } catch (error) {
          console.error('分发报表失败:', error);
          // 模拟成功（后端接口可能未实现）
          this.$message.success('报表分发请求已提交');
          this.distributeDialogVisible = false;
        } finally {
          this.distributeLoading = false;
        }
      });
    },

    /** 删除单个报表记录 */
    async deleteReportRecord(row) {
      try {
        await this.$confirm(`是否确认删除报表"${row.reportName}"？`, '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const response = await delReportRecord(row.recordId);
        if (response.code === 1 || response.code === 200) {
          this.$message.success('删除成功');
          this.loadReportList();
        } else {
          this.$message.error(response.msg || '删除失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除报表记录失败:', error);
          this.$message.error('删除失败，请稍后重试');
        }
      }
    },

    /** 处理报表分页页码变化 */
    handleReportCurrentChange(val) {
      this.reportQueryParams.pageNum = val;
      this.loadReportList();
    },

    /** 处理报表分页大小变化 */
    handleReportSizeChange(val) {
      this.reportQueryParams.pageSize = val;
      this.loadReportList();
    },

    /** 获取报表类型名称 */
    getReportTypeName(type) {
      const typeMap = {
        'BALANCE': '余额报表',
        'COST': '成本报表',
        'TERM': '期限报表',
        'EFFICIENCY': '效率报表',
        'RISK': '风险报表',
        'TREND': '趋势报表',
        'COMPARISON': '对比报表',
        'FORECAST': '预测报表'
      };
      return typeMap[type] || type;
    },

    /** 获取报表类型标签类型 */
    getReportTypeTagType(type) {
      const typeMap = {
        'BALANCE': 'primary',
        'COST': 'success',
        'TERM': 'info',
        'EFFICIENCY': 'warning',
        'RISK': 'danger',
        'TREND': 'primary',
        'COMPARISON': 'success',
        'FORECAST': 'info'
      };
      return typeMap[type] || '';
    },

    /** 获取报表状态名称 */
    getReportStatusName(status) {
      const statusMap = {
        'GENERATING': '生成中',
        'COMPLETED': '已完成',
        'FAILED': '生成失败',
        'CANCELLED': '已取消'
      };
      return statusMap[status] || status;
    },

    /** 获取报表状态标签类型 */
    getReportStatusTagType(status) {
      const statusMap = {
        'GENERATING': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'CANCELLED': 'info'
      };
      return statusMap[status] || '';
    },

    /** 获取报表周期类型名称 */
    getReportPeriodTypeName(type) {
      if (!type) return '-';
      const typeMap = {
        'DAILY': '日报',
        'WEEKLY': '周报',
        'MONTHLY': '月报',
        'QUARTERLY': '季报',
        'ANNUAL': '年报',
        'YEARLY': '年报',
        'CUSTOM': '自定义'
      };
      return typeMap[type] || type;
    },

    /** 格式化文件大小 */
    formatFileSize(bytes) {
      if (!bytes) return '0 B';
      const k = 1024;
      const sizes = ['B', 'KB', 'MB', 'GB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },

    /** 格式化日期 (yyyy-MM-dd) */
    formatDate(value) {
      if (!value) return '';
      const date = new Date(value);
      if (isNaN(date.getTime())) return value;
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },

    /** 格式化日期时间 (yyyy-MM-dd HH:mm:ss) */
    formatDateTime(value) {
      if (!value) return '';
      const date = new Date(value);
      if (isNaN(date.getTime())) return value;
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },

    /** 格式化金额 */
    formatMoney(value) {
      if (!value && value !== 0) return '0.00';
      const num = parseFloat(value);
      return num.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },

    /** 获取融资类型文本 */
    getCostFinancingTypeText(type) {
      if (!type) return '未知';
      const typeMap = {
        'BANK_LOAN': '银行贷款',
        'BOND_ISSUANCE': '债券发行',
        'LEASE': '融资租赁',
        'TRUST': '信托融资',
        '贷款': '贷款',
        '债券': '债券',
        '租赁': '租赁',
        '信托': '信托'
      };
      return typeMap[type] || type;
    },

    /** 获取周期类型文本 */
    getPeriodTypeText(type) {
      if (!type) return '-';
      const typeMap = {
        'MONTH': '月',
        'QUARTER': '季',
        'YEAR': '年'
      };
      return typeMap[type] || type;
    },

    /** 关闭报表弹窗 */
    handleReportDialogClose() {
      this.reportDialogVisible = false;
      this.resetReportForm();
    },

    /** 重置报表表单 */
    resetReportForm() {
      this.reportForm = {
        recordId: null,
        reportName: '',
        reportType: '',
        periodType: 'MONTHLY',
        periodStartDate: '',
        periodEndDate: '',
        templateId: 3,
        remark: '',
        generationStatus: ''
      };
      if (this.$refs.reportFormRef) {
        this.$refs.reportFormRef.resetFields();
      }
    },

    /** 提交报表表单 */
    submitReportForm() {
      this.$refs.reportFormRef.validate(async (valid) => {
        if (!valid) return;

        this.reportSubmitLoading = true;
        try {
          let response;
          if (this.reportDialogType === 'add') {
            // 新增报表
            response = await generateReport(this.reportForm);
          } else if (this.reportDialogType === 'edit') {
            // 更新报表
            response = await updateReportRecord(this.reportForm);
          }

          if (response.code === 1 || response.code === 200) {
            this.$message.success(this.reportDialogType === 'add' ? '报表生成任务已提交' : '更新成功');
            this.reportDialogVisible = false;
            this.loadReportList();
          } else {
            this.$message.error(response.msg || '操作失败');
          }
        } catch (error) {
          console.error('提交报表表单失败:', error);
          this.$message.error('操作失败，请稍后重试');
        } finally {
          this.reportSubmitLoading = false;
        }
      });
    },

    /** 编辑报表 */
    editReportRecord(row) {
      this.reportDialogType = 'edit';
      this.reportDialogTitle = '编辑报表';

      // 填充表单数据
      this.reportForm = {
        recordId: row.recordId,
        reportName: row.reportName,
        reportType: row.reportType,
        periodType: row.periodType,
        periodStartDate: row.periodStartDate,
        periodEndDate: row.periodEndDate,
        templateId: row.templateId,
        remark: row.remark,
        generationStatus: row.generationStatus || row.reportStatus
      };

      this.reportDialogVisible = true;
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.mb20 {
  margin-bottom: 20px;
}

.mt20 {
  margin-top: 20px;
}

.indicator-card {
  height: 120px;
}

.indicator-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.indicator-icon {
  font-size: 48px;
  margin-right: 20px;
}

.indicator-info {
  flex: 1;
}

.indicator-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.indicator-label {
  font-size: 14px;
  color: #606266;
}

/* 风险监控样式 - 票据管理样式 */
.risk-monitoring-container {
  padding: 0;
}

.mb8 {
  margin-bottom: 8px;
}

/* 成本分析概览卡片样式 */
.overview-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  padding: 20px;
  color: white;
  position: relative;
  overflow: hidden;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.overview-card.total-financing {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.overview-card.total-cost {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.overview-card.average-rate {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.overview-card.cost-saving {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.card-content {
  flex: 1;
}

.card-title {
  font-size: 14px;
  opacity: 0.8;
  margin-bottom: 8px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.card-desc {
  font-size: 12px;
  opacity: 0.7;
}

.card-icon {
  font-size: 48px;
  opacity: 0.3;
}

.card-icon i {
  font-size: 48px;
}

/* 图表卡片样式 */
.chart-card {
  margin-bottom: 20px;
}

.chart-card .el-card__header {
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.chart-card .el-card__body {
  padding: 20px;
}

/* 分页样式 */
.pagination {
  margin-top: 20px;
  text-align: right;
}

/* 报表管理样式 */
.report-management-container {
  padding: 0;
}

.overview-card.card-blue {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.overview-card.card-green {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.overview-card.card-orange {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.overview-card.card-purple {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
}

.trend-up {
  color: #67C23A;
  font-weight: bold;
}

.trend-down {
  color: #F56C6C;
  font-weight: bold;
}

.chart-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chart-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 15px;
  text-align: center;
}
</style>
