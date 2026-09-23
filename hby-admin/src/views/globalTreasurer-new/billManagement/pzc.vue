<template>
  <div class="bill-pool-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-collection"></i>
            票据池管理
          </h2>
          <p class="page-description">票据池的创建、管理和质押融资服务</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreatePool">
            创建票据池
          </el-button>
          <el-button type="success" icon="el-icon-upload" @click="handleAddBills">
            入池票据
          </el-button>
          <el-button type="warning" icon="el-icon-money" @click="handleFinancing">
            质押融资
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 票据池概览卡片 -->
    <div class="pool-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-collection"></i>
              </div>
              <div class="card-info">
                <div class="card-title">票据池数量</div>
                <div class="card-value">{{ totalPools }}</div>
                <div class="card-change">个</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon bills-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">池内票据</div>
                <div class="card-value">{{ totalBillsInPool }}</div>
                <div class="card-change">张</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">池内总额</div>
                <div class="card-value">{{ totalPoolAmount }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon financing-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-info">
                <div class="card-title">融资余额</div>
                <div class="card-value">{{ financingBalance }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 票据池分析图表 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>票据池构成分析</h3>
            <div class="chart-controls">
              <el-radio-group v-model="compositionChartType" size="small" @change="handleCompositionChartTypeChange">
                <el-radio-button label="pie">饼图</el-radio-button>
                <el-radio-button label="bar">柱状图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="poolCompositionChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>融资使用情况</h3>
            <div class="chart-controls">
              <el-radio-group v-model="financingPeriod" size="small" @change="handleFinancingPeriodChange">
                <el-radio-button label="7D">7天</el-radio-button>
                <el-radio-button label="30D">30天</el-radio-button>
                <el-radio-button label="90D">90天</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="financingChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="票据池名称">
            <el-input
              v-model="listQuery.poolName"
              placeholder="请输入票据池名称"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="池状态">
            <el-select
              v-model="listQuery.poolStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="正常" value="NORMAL" />
              <el-option label="质押中" value="PLEDGED" />
              <el-option label="已冻结" value="FROZEN" />
              <el-option label="已关闭" value="CLOSED" />
            </el-select>
          </el-form-item>
          <el-form-item label="创建人">
            <el-input
              v-model="listQuery.creatorName"
              placeholder="请输入创建人"
              style="width: 120px;"
            />
          </el-form-item>
          <el-form-item label="创建日期">
            <el-date-picker
              v-model="listQuery.createDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 票据池表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="poolList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="池ID" prop="poolId" width="80" align="center" />
        <el-table-column label="票据池名称" width="200px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.poolName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="池状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getPoolStatusTagType(row.poolStatus)" size="mini">
              <i :class="getPoolStatusIcon(row.poolStatus)"></i>
              {{ getPoolStatusText(row.poolStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="票据数量" width="100px" align="center">
          <template slot-scope="{row}">
            <span class="bill-count">{{ row.billCount || 0 }}张</span>
          </template>
        </el-table-column>
        <el-table-column label="池内总额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="pool-amount">{{ formatCurrency(row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="质押率" width="100px" align="center">
          <template slot-scope="{row}">
            <span :class="getPledgeRateClass(row.pledgeRate)">{{ row.pledgeRate || 0 }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="融资金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="financing-amount">{{ formatCurrency(row.financingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.createUser || row.ownerName || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.poolStatus === 'NORMAL'" type="primary" size="mini" @click="handleManageBills(row)">
              管理票据
            </el-button>
            <el-button v-if="row.poolStatus === 'NORMAL'" type="warning" size="mini" @click="handlePledgeFinancing(row)">
              质押融资
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'edit', row: row}">编辑池信息</el-dropdown-item>
                <el-dropdown-item :command="{action: 'freeze', row: row}">冻结票据池</el-dropdown-item>
                <el-dropdown-item :command="{action: 'close', row: row}">关闭票据池</el-dropdown-item>
                <el-dropdown-item :command="{action: 'report', row: row}">生成报告</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />
    </el-card>

    <!-- 票据池创建/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="700px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-form-item label="票据池名称" prop="poolName">
          <el-input v-model="temp.poolName" placeholder="请输入票据池名称" />
        </el-form-item>
        <el-form-item label="池类型" prop="poolType">
          <el-select v-model="temp.poolType" placeholder="请选择池类型" style="width: 100%;">
            <el-option label="质押融资池" value="PLEDGE_FINANCING" />
            <el-option label="流动性管理池" value="LIQUIDITY_MANAGEMENT" />
            <el-option label="风险分散池" value="RISK_DIVERSIFICATION" />
            <el-option label="投资组合池" value="PORTFOLIO" />
          </el-select>
        </el-form-item>
        <el-form-item label="票据数量" prop="billCount">
          <el-input-number v-model="temp.billCount" :min="0" :max="10000" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="池内总额" prop="totalAmount">
          <el-input-number
            v-model="temp.totalAmount"
            :precision="2"
            :step="100000"
            :min="0"
            :max="10000000000"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="质押率(%)" prop="pledgeRate">
          <el-input-number
            v-model="temp.pledgeRate"
            :precision="2"
            :step="5"
            :min="0"
            :max="100"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="融资金额" prop="financingAmount">
          <el-input-number
            v-model="temp.financingAmount"
            :precision="2"
            :step="100000"
            :min="0"
            :max="10000000000"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="最大池金额">
          <el-input-number
            v-model="temp.maxPoolValue"
            :precision="2"
            :step="1000000"
            :min="0"
            :max="10000000000"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="池描述">
          <el-input v-model="temp.poolDescription" type="textarea" :rows="3" placeholder="请输入票据池描述" />
        </el-form-item>
        <el-form-item label="风险控制">
          <el-checkbox-group v-model="temp.riskControls">
            <el-checkbox label="CREDIT_RATING">信用评级控制</el-checkbox>
            <el-checkbox label="MATURITY_LIMIT">到期期限控制</el-checkbox>
            <el-checkbox label="AMOUNT_LIMIT">单票金额控制</el-checkbox>
            <el-checkbox label="INDUSTRY_LIMIT">行业集中度控制</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 票据池详情对话框 -->
    <el-dialog title="票据池详情" :visible.sync="dialogDetailVisible" width="1000px">
      <div v-if="currentPool" class="pool-detail">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="票据池名称">{{ currentPool.poolName }}</el-descriptions-item>
          <el-descriptions-item label="池类型">{{ getPoolTypeText(currentPool.poolType) }}</el-descriptions-item>
          <el-descriptions-item label="池状态">
            <el-tag :type="getPoolStatusTagType(currentPool.poolStatus)">
              {{ getPoolStatusText(currentPool.poolStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="票据数量">{{ currentPool.billCount || 0 }}张</el-descriptions-item>
          <el-descriptions-item label="池内总额">{{ formatCurrency(currentPool.totalAmount) }}</el-descriptions-item>
          <el-descriptions-item label="质押率">{{ currentPool.pledgeRate || 0 }}%</el-descriptions-item>
          <el-descriptions-item label="融资金额">{{ formatCurrency(currentPool.financingAmount) }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ currentPool.createUser || currentPool.ownerName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建日期">{{ formatDate(currentPool.createTime) }}</el-descriptions-item>
        </el-descriptions>
        
        <!-- 池内票据列表 -->
        <div class="pool-bills">
          <h4>池内票据列表</h4>
          <el-table :data="poolBills" border size="small" max-height="300">
            <el-table-column label="票据号码" prop="billNumber" width="150" />
            <el-table-column label="票据类型" prop="billType" width="120" />
            <el-table-column label="票据金额" prop="billAmount" width="120" align="right" />
            <el-table-column label="承兑人" prop="acceptorName" width="150" />
            <el-table-column label="到期日期" prop="maturityDate" width="120" />
            <el-table-column label="入池日期" prop="poolDate" width="120" />
            <el-table-column label="状态" prop="status" width="100" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.status === 'NORMAL' ? 'success' : 'warning'" size="mini">
                  {{ row.status === 'NORMAL' ? '正常' : '质押中' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
        
        <!-- 融资记录 -->
        <div class="financing-records">
          <h4>融资记录</h4>
          <el-table :data="financingRecords" border size="small" max-height="200">
            <el-table-column label="融资日期" prop="financingDate" width="120" />
            <el-table-column label="融资金额" prop="financingAmount" width="120" align="right" />
            <el-table-column label="融资期限" prop="financingTerm" width="100" />
            <el-table-column label="融资利率" prop="financingRate" width="100" />
            <el-table-column label="融资银行" prop="financingBank" width="150" />
            <el-table-column label="状态" prop="status" width="100" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'" size="mini">
                  {{ row.status === 'ACTIVE' ? '生效中' : '已结清' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentPool && currentPool.poolStatus === 'NORMAL'" type="primary" @click="handleManageBills(currentPool)">
          管理票据
        </el-button>
      </div>
    </el-dialog>

    <!-- 票据管理对话框 -->
    <el-dialog title="票据管理" :visible.sync="dialogBillManageVisible" width="800px">
      <div class="bill-manage-content">
        <div class="manage-header">
          <el-button type="primary" size="small" @click="openAddBillsDialog">
            <i class="el-icon-plus"></i>
            添加票据
          </el-button>
          <el-button type="warning" size="small" @click="handleRemoveBillsFromPool">
            <i class="el-icon-minus"></i>
            移除票据
          </el-button>
          <el-button type="info" size="small" @click="handleRefreshPoolBills">
            <i class="el-icon-refresh"></i>
            刷新
          </el-button>
        </div>
        <el-table
          :data="poolBills"
          border
          size="small"
          @selection-change="handleBillSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="票据号码" prop="billNumber" width="150" />
          <el-table-column label="票据金额" prop="billAmount" width="120" align="right" />
          <el-table-column label="承兑人" prop="acceptorName" width="150" />
          <el-table-column label="到期日期" prop="maturityDate" width="120" />
          <el-table-column label="入池日期" prop="poolDate" width="120" />
          <el-table-column label="状态" prop="status" width="100" align="center">
            <template slot-scope="{row}">
              <el-tag :type="row.status === 'NORMAL' ? 'success' : 'warning'" size="mini">
                {{ row.status === 'NORMAL' ? '正常' : '质押中' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogBillManageVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 添加票据选择对话框 -->
    <el-dialog title="选择可用票据" :visible.sync="dialogAddBillsVisible" width="900px" append-to-body>
      <el-table
        :data="availableBills"
        border
        size="small"
        max-height="400"
        v-loading="availableBillsLoading"
        @selection-change="handleAvailableBillSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="票据号码" prop="billNumber" width="160" />
        <el-table-column label="票据类型" prop="billType" width="120" />
        <el-table-column label="票据金额" prop="billAmount" width="130" align="right">
          <template slot-scope="{row}">
            {{ formatCurrency(row.billAmount) }}
          </template>
        </el-table-column>
        <el-table-column label="承兑人" prop="acceptorName" width="150" />
        <el-table-column label="到期日期" prop="maturityDate" width="120" />
        <el-table-column label="状态" prop="status" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag type="success" size="mini">可入池</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 12px; color: #909399; font-size: 13px;">
        已选择 <span style="color: #409EFF; font-weight: 600;">{{ selectedBillsForPool.length }}</span> 张票据
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogAddBillsVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddBillsToPool">确认添加</el-button>
      </div>
    </el-dialog>

    <!-- 质押融资对话框 -->
    <el-dialog title="质押融资申请" :visible.sync="dialogFinancingVisible" width="600px">
      <el-form ref="financingForm" :model="financingForm" label-width="120px">
        <el-form-item label="融资金额" prop="financingAmount">
          <el-input-number
            v-model="financingForm.financingAmount"
            :precision="2"
            :step="10000"
            :min="0"
            style="width: 100%;"
          />
          <div class="financing-tip">
            <span>最大可融资金额：{{ formatCurrency(maxFinancingAmount) }}</span>
          </div>
        </el-form-item>
        <el-form-item label="融资期限">
          <el-select v-model="financingForm.financingTerm" placeholder="请选择融资期限" style="width: 100%;">
            <el-option label="1个月" value="1M" />
            <el-option label="3个月" value="3M" />
            <el-option label="6个月" value="6M" />
            <el-option label="12个月" value="12M" />
          </el-select>
        </el-form-item>
        <el-form-item label="融资银行">
          <el-select v-model="financingForm.financingBank" placeholder="请选择融资银行" style="width: 100%;">
            <el-option label="中国工商银行" value="ICBC" />
            <el-option label="中国建设银行" value="CCB" />
            <el-option label="中国银行" value="BOC" />
            <el-option label="招商银行" value="CMB" />
          </el-select>
        </el-form-item>
        <el-form-item label="融资用途">
          <el-input v-model="financingForm.financingPurpose" type="textarea" :rows="3" placeholder="请输入融资用途" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFinancingVisible = false">取消</el-button>
        <el-button type="primary" @click="submitFinancing">提交申请</el-button>
      </div>
    </el-dialog>

    <!-- 票据池报告对话框 -->
    <el-dialog title="票据池报告" :visible.sync="dialogReportVisible" width="900px" top="5vh">
      <div v-loading="reportLoading">
        <template v-if="reportData">
          <!-- 基本信息 -->
          <h4 style="margin: 0 0 10px 0;">票据池基本信息</h4>
          <el-descriptions :column="3" border size="small" style="margin-bottom: 16px;">
            <el-descriptions-item label="池名称">{{ reportData.poolInfo.poolName }}</el-descriptions-item>
            <el-descriptions-item label="池编号">{{ reportData.poolInfo.poolCode }}</el-descriptions-item>
            <el-descriptions-item label="池类型">{{ reportData.poolInfo.poolType === 'COMMERCIAL' ? '商业票据池' : reportData.poolInfo.poolType === 'BANK' ? '银行票据池' : '混合票据池' }}</el-descriptions-item>
            <el-descriptions-item label="票据数量">{{ reportData.poolInfo.billCount }}</el-descriptions-item>
            <el-descriptions-item label="总金额">{{ formatCurrency(reportData.poolInfo.totalAmount) }}</el-descriptions-item>
            <el-descriptions-item label="质押率">{{ reportData.poolInfo.pledgeRate }}%</el-descriptions-item>
            <el-descriptions-item label="融资金额">{{ formatCurrency(reportData.poolInfo.financingAmount) }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="reportData.poolInfo.poolStatus === 'ACTIVE' ? 'success' : reportData.poolInfo.poolStatus === 'FROZEN' ? 'warning' : 'info'" size="mini">
                {{ reportData.poolInfo.poolStatus === 'ACTIVE' ? '活跃' : reportData.poolInfo.poolStatus === 'FROZEN' ? '冻结' : '已关闭' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ reportData.poolInfo.createTime }}</el-descriptions-item>
          </el-descriptions>

          <!-- 统计摘要 -->
          <h4 style="margin: 16px 0 10px 0;">统计摘要</h4>
          <el-row :gutter="16" style="margin-bottom: 16px;">
            <el-col :span="6">
              <el-card shadow="never" class="report-stat-card">
                <div class="stat-label">票据总数</div>
                <div class="stat-value">{{ reportData.summary.billCount }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="never" class="report-stat-card">
                <div class="stat-label">票据总金额</div>
                <div class="stat-value">{{ formatCurrency(reportData.summary.totalBillAmount) }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="never" class="report-stat-card">
                <div class="stat-label">融资笔数</div>
                <div class="stat-value">{{ reportData.summary.financingCount }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="never" class="report-stat-card">
                <div class="stat-label">融资总额</div>
                <div class="stat-value">{{ formatCurrency(reportData.summary.totalFinancingAmount) }}</div>
              </el-card>
            </el-col>
          </el-row>

          <!-- 池内票据 -->
          <h4 style="margin: 16px 0 10px 0;">池内票据明细</h4>
          <el-table :data="reportData.bills" border size="mini" style="margin-bottom: 16px;" max-height="250">
            <el-table-column prop="billNumber" label="票据编号" min-width="140" />
            <el-table-column prop="billType" label="票据类型" width="100">
              <template slot-scope="scope">
                {{ scope.row.billType === 'COMMERCIAL' ? '商业承兑' : scope.row.billType === 'BANK' ? '银行承兑' : scope.row.billType }}
              </template>
            </el-table-column>
            <el-table-column prop="billAmount" label="票面金额" width="120" align="right">
              <template slot-scope="scope">{{ formatCurrency(scope.row.billAmount) }}</template>
            </el-table-column>
            <el-table-column prop="joinDate" label="入池日期" width="110" />
            <el-table-column prop="detailStatus" label="状态" width="80">
              <template slot-scope="scope">
                <el-tag :type="scope.row.detailStatus === 'IN_POOL' ? 'success' : 'info'" size="mini">
                  {{ scope.row.detailStatus === 'IN_POOL' ? '在池' : '已出池' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>

          <!-- 融资记录 -->
          <h4 style="margin: 16px 0 10px 0;">融资记录</h4>
          <el-table :data="reportData.financingRecords" border size="mini" max-height="250">
            <el-table-column prop="financingNumber" label="融资编号" min-width="140" />
            <el-table-column prop="financingType" label="融资类型" width="100" />
            <el-table-column prop="pledgeAmount" label="质押金额" width="120" align="right">
              <template slot-scope="scope">{{ formatCurrency(scope.row.pledgeAmount) }}</template>
            </el-table-column>
            <el-table-column prop="financingAmount" label="融资金额" width="120" align="right">
              <template slot-scope="scope">{{ formatCurrency(scope.row.financingAmount) }}</template>
            </el-table-column>
            <el-table-column prop="interestRate" label="利率" width="80" align="right">
              <template slot-scope="scope">{{ scope.row.interestRate }}%</template>
            </el-table-column>
            <el-table-column prop="repayDate" label="还款日期" width="110" />
            <el-table-column prop="repayStatus" label="还款状态" width="90">
              <template slot-scope="scope">
                <el-tag :type="scope.row.repayStatus === 'REPAID' ? 'success' : 'warning'" size="mini">
                  {{ scope.row.repayStatus === 'REPAID' ? '已还款' : '待还款' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </template>
        <el-empty v-if="!reportLoading && !reportData" description="暂无报告数据" />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogReportVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBillPoolPage,
  getBillPoolDetail,
  createBillPool,
  updateBillPool,
  deleteBillPool,
  addBillsToPool,
  removeBillsFromPool,
  getPoolBills,
  pledgeFinancing,
  getFinancingRecords,
  exportBillPool,
  getBillPoolStatistics,
  getAvailableBillsForPool,
  freezeBillPool,
  closeBillPool,
  generatePoolReport
} from '@/api/globalTreasurer-new/billManagement/billPool'
import Pagination from '@/components/Pagination'

export default {
  name: 'BillPoolManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        pageNum: 1,
        pageSize: 20,
        poolName: undefined,
        poolStatus: undefined,
        creatorName: undefined,
        createDateRange: [],
        createDateStart: undefined,
        createDateEnd: undefined
      },
      totalPools: 0,
      totalBillsInPool: 0,
      totalPoolAmount: 0,
      financingBalance: 0,
      poolTypeStats: [],
      compositionChartType: 'pie',
      financingPeriod: '30D',
      poolList: [],
      poolBills: [],
      financingRecords: [],
      multipleSelection: [],
      billSelection: [],
      currentPool: null,
      maxFinancingAmount: 0,
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogBillManageVisible: false,
      dialogFinancingVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      temp: {
        poolId: undefined,
        poolName: '',
        poolType: '',
        billCount: 0,
        totalAmount: 0,
        pledgeRate: 0,
        financingAmount: 0,
        maxPoolValue: 10000000,
        poolDescription: '',
        riskControls: []
      },
      financingForm: {
        financingAmount: null,
        financingTerm: '',
        financingBank: '',
        financingPurpose: ''
      },
      rules: {
        poolName: [{ required: true, message: '票据池名称不能为空', trigger: 'blur' }],
        poolType: [{ required: true, message: '请选择池类型', trigger: 'change' }]
      },
      compositionChart: null,
      financingChart: null,
      dialogAddBillsVisible: false,
      availableBills: [],
      availableBillsLoading: false,
      selectedBillsForPool: [],
      dialogReportVisible: false,
      reportData: null,
      reportLoading: false
    }
  },
  mounted() {
    this.getList()
    this.loadStatistics()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.compositionChart) {
      this.compositionChart.dispose()
    }
    if (this.financingChart) {
      this.financingChart.dispose()
    }
  },
  methods: {
    // 加载统计数据
    loadStatistics() {
      getBillPoolStatistics({}).then(response => {
        if (response.code === 1 && response.data) {
          this.totalPools = response.data.totalPools || 0
          this.totalBillsInPool = response.data.totalBillsInPool || 0
          this.totalPoolAmount = response.data.totalPoolAmount || 0
          this.financingBalance = response.data.financingBalance || 0
          // 保存类型统计数据用于图表
          this.poolTypeStats = response.data.poolTypeStats || []
          // 更新图表
          this.updateCompositionChart()
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
      })
    },
    // 格式化日期参数
    formatDateParam(date) {
      if (!date) return undefined
      if (typeof date === 'string') return date
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    getList() {
      this.listLoading = true
      // 处理日期范围
      const params = { ...this.listQuery }
      if (this.listQuery.createDateRange && this.listQuery.createDateRange.length === 2) {
        params.createDateStart = this.formatDateParam(this.listQuery.createDateRange[0])
        params.createDateEnd = this.formatDateParam(this.listQuery.createDateRange[1])
      }
      delete params.createDateRange

      // 调用后端API获取真实数据
      getBillPoolPage(params).then(response => {
        if (response.code === 1) {
          this.poolList = response.data.tlist || response.data || []
          this.total = response.data.totalRecord || response.result?.total || 0
        } else {
          this.poolList = []
          this.total = 0
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取票据池列表失败:', error)
        this.poolList = []
        this.total = 0
        this.listLoading = false
      })
    },
    initCharts() {
      const echarts = require('echarts')

      // 初始化票据池构成图表
      this.compositionChart = echarts.init(document.getElementById('poolCompositionChart'))
      this.updateCompositionChart()

      // 初始化融资使用图表
      this.financingChart = echarts.init(document.getElementById('financingChart'))
      this.updateFinancingChart()
    },
    updateCompositionChart() {
      // 使用从后端获取的真实数据
      const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
      let data = []

      if (this.poolTypeStats && this.poolTypeStats.length > 0) {
        // 使用后端返回的真实数据
        data = this.poolTypeStats.map((item, index) => ({
          name: item.typeName || item.type,
          value: item.count || 0,
          itemStyle: { color: colors[index % colors.length] }
        }))
      } else {
        // 如果没有数据，显示空状态
        data = [{ name: '暂无数据', value: 0, itemStyle: { color: '#909399' } }]
      }

      let option = {}

      if (this.compositionChartType === 'pie') {
        option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c}张 ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 10,
            data: data.map(item => item.name)
          },
          series: [
            {
              name: '票据池类型',
              type: 'pie',
              radius: ['50%', '70%'],
              center: ['60%', '50%'],
              data: data,
              label: {
                show: data.length > 0 && data[0].value > 0
              },
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        }
      } else {
        option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: data.map(item => item.name),
            axisLabel: {
              rotate: 45
            }
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '票据数量',
              type: 'bar',
              data: data.map(item => ({
                value: item.value,
                itemStyle: item.itemStyle
              }))
            }
          ]
        }
      }

      this.compositionChart.setOption(option, true)
    },
    updateFinancingChart() {
      // 根据选择的时间周期生成日期标签
      let days = 30
      if (this.financingPeriod === '7D') {
        days = 7
      } else if (this.financingPeriod === '90D') {
        days = 90
      }

      const dates = this.generateDateLabels(days)

      // 基于实际融资余额生成趋势数据（模拟）
      // TODO: 后续可以从后端获取真实的融资记录数据
      const baseAmount = this.financingBalance * 10000 // 转回元
      const financingData = []
      const repaymentData = []

      // 生成模拟的趋势数据
      for (let i = 0; i < days; i++) {
        // 融资金额：随机波动
        const financing = baseAmount > 0 ? Math.round(baseAmount * (0.01 + Math.random() * 0.05)) : 0
        financingData.push(financing)

        // 还款金额：略小于融资金额
        const repayment = financing > 0 ? Math.round(financing * (0.6 + Math.random() * 0.3)) : 0
        repaymentData.push(repayment)
      }

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          formatter: function(params) {
            let result = params[0].axisValue + '<br/>'
            params.forEach(param => {
              const value = param.value ? (param.value / 10000).toFixed(2) + '万元' : '0万元'
              result += param.marker + param.seriesName + ': ' + value + '<br/>'
            })
            return result
          }
        },
        legend: {
          data: ['融资金额', '还款金额']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: dates,
          axisLabel: {
            rotate: days > 30 ? 45 : 0,
            interval: days > 30 ? Math.floor(days / 15) : 0
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: function(value) {
              return (value / 10000).toFixed(0) + '万'
            }
          }
        },
        series: [
          {
            name: '融资金额',
            type: 'bar',
            data: financingData,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '还款金额',
            type: 'bar',
            data: repaymentData,
            itemStyle: { color: '#67C23A' }
          }
        ]
      }

      this.financingChart.setOption(option, true)
    },
    generateDateLabels(days) {
      const labels = []
      for (let i = days - 1; i >= 0; i--) {
        const date = new Date()
        date.setDate(date.getDate() - i)
        labels.push((date.getMonth() + 1) + '/' + date.getDate())
      }
      return labels
    },
    handleCompositionChartTypeChange() {
      this.updateCompositionChart()
    },
    handleFinancingPeriodChange() {
      this.updateFinancingChart()
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        pageNum: 1,
        pageSize: 20,
        poolName: undefined,
        poolStatus: undefined,
        creatorName: undefined,
        createDateRange: [],
        createDateStart: undefined,
        createDateEnd: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleBillSelectionChange(val) {
      this.billSelection = val
    },
    handleCreatePool() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '创建票据池'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      // 从后端获取最新的票据池详情
      getBillPoolDetail(row.poolId).then(response => {
        if (response.code === 1 && response.data) {
          this.currentPool = response.data
        } else {
          this.currentPool = row
        }
        this.loadPoolBills(row.poolId)
        this.loadFinancingRecords(row.poolId)
        this.dialogDetailVisible = true
      }).catch(error => {
        console.error('获取票据池详情失败:', error)
        this.currentPool = row
        this.loadPoolBills(row.poolId)
        this.loadFinancingRecords(row.poolId)
        this.dialogDetailVisible = true
      })
    },
    handleManageBills(row) {
      this.currentPool = row
      this.loadPoolBills(row.poolId)
      this.dialogBillManageVisible = true
    },
    handlePledgeFinancing(row) {
      // 从后端获取最新的票据池信息计算最大融资金额
      getBillPoolDetail(row.poolId).then(response => {
        if (response.code === 1 && response.data) {
          this.currentPool = response.data
          this.maxFinancingAmount = (response.data.totalAmount || 0) * (response.data.maxPoolValue || 80) / 100
        } else {
          this.currentPool = row
          this.maxFinancingAmount = (row.totalAmount || 0) * (row.maxPoolValue || 80) / 100
        }
      }).catch(() => {
        this.currentPool = row
        this.maxFinancingAmount = (row.totalAmount || 0) * (row.maxPoolValue || 80) / 100
      })
      this.financingForm = {
        poolId: row.poolId,
        financingAmount: null,
        financingTerm: '',
        financingBank: '',
        financingPurpose: ''
      }
      this.dialogFinancingVisible = true
    },
    handleAddBills() {
      // 打开入池票据对话框，加载可入池票据
      this.loadAvailableBills()
      this.dialogAddBillsVisible = true
    },
    openAddBillsDialog() {
      // 从票据管理对话框中点击"添加票据"，打开选择可用票据对话框
      this.selectedBillsForPool = []
      this.loadAvailableBills()
      this.dialogAddBillsVisible = true
    },
    handleAvailableBillSelectionChange(val) {
      this.selectedBillsForPool = val
    },
    loadAvailableBills() {
      this.availableBillsLoading = true
      getAvailableBillsForPool({ poolId: this.currentPool ? this.currentPool.poolId : undefined }).then(response => {
        if (response.code === 1) {
          this.availableBills = response.data || []
        } else {
          this.availableBills = []
        }
        this.availableBillsLoading = false
      }).catch(error => {
        console.error('获取可入池票据失败:', error)
        this.availableBills = []
        this.availableBillsLoading = false
      })
    },
    handleFinancing() {
      // 打开质押融资对话框
      if (this.multipleSelection.length === 0) {
        this.$message({ type: 'warning', message: '请先选择票据池' })
        return
      }
      this.currentPool = this.multipleSelection[0]
      this.handlePledgeFinancing(this.currentPool)
    },
    handleExport() {
      // 调用后端API导出数据
      const params = { ...this.listQuery }
      if (this.listQuery.createDateRange && this.listQuery.createDateRange.length === 2) {
        params.createDateStart = this.formatDateParam(this.listQuery.createDateRange[0])
        params.createDateEnd = this.formatDateParam(this.listQuery.createDateRange[1])
      }
      delete params.createDateRange

      exportBillPool(params).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '票据池数据_' + new Date().getTime() + '.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
        this.$message({ type: 'success', message: '数据导出成功' })
      }).catch(error => {
        console.error('导出失败:', error)
        this.$message({ type: 'error', message: '导出失败，请稍后重试' })
      })
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'edit':
          this.handleEdit(row)
          break
        case 'freeze':
          this.handleFreeze(row)
          break
        case 'close':
          this.handleClose(row)
          break
        case 'report':
          this.handleGenerateReport(row)
          break
      }
    },
    handleEdit(row) {
      // 从后端获取最新数据后编辑
      getBillPoolDetail(row.poolId).then(response => {
        if (response.code === 1 && response.data) {
          this.temp = Object.assign({}, response.data)
        } else {
          this.temp = Object.assign({}, row)
        }
        this.dialogStatus = 'update'
        this.dialogTitle = '编辑票据池'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      }).catch(() => {
        this.temp = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogTitle = '编辑票据池'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      })
    },
    handleFreeze(row) {
      this.$confirm('确认冻结该票据池?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        freezeBillPool(row.poolId).then(response => {
          if (response.code === 1) {
            row.poolStatus = 'FROZEN'
            this.$message({
              type: 'success',
              message: '票据池冻结成功!'
            })
            this.getList()
          } else {
            this.$message({
              type: 'error',
              message: response.msg || '冻结失败'
            })
          }
        }).catch(error => {
          console.error('冻结票据池失败:', error)
          this.$message({
            type: 'error',
            message: '冻结失败，请稍后重试'
          })
        })
      })
    },
    handleClose(row) {
      this.$confirm('确认关闭该票据池?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        closeBillPool(row.poolId).then(response => {
          if (response.code === 1) {
            row.poolStatus = 'CLOSED'
            this.$message({
              type: 'success',
              message: '票据池关闭成功!'
            })
            this.getList()
          } else {
            this.$message({
              type: 'error',
              message: response.msg || '关闭失败'
            })
          }
        }).catch(error => {
          console.error('关闭票据池失败:', error)
          this.$message({
            type: 'error',
            message: '关闭失败，请稍后重试'
          })
        })
      })
    },
    handleGenerateReport(row) {
      this.reportLoading = true
      this.reportData = null
      this.dialogReportVisible = true
      generatePoolReport(row.poolId).then(response => {
        if (response.code === 1 && response.data) {
          this.reportData = response.data
        } else {
          this.$message.error(response.msg || '生成报告失败')
          this.dialogReportVisible = false
        }
      }).catch(() => {
        this.$message.error('生成报告失败')
        this.dialogReportVisible = false
      }).finally(() => {
        this.reportLoading = false
      })
    },
    handleAddBillsToPool() {
      if (this.selectedBillsForPool.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要添加的票据'
        })
        return
      }

      const billIds = this.selectedBillsForPool.map(bill => bill.billId)
      addBillsToPool({
        poolId: this.currentPool.poolId,
        billIds: billIds
      }).then(response => {
        if (response.code === 1) {
          this.$message({
            type: 'success',
            message: '票据添加成功!'
          })
          this.dialogAddBillsVisible = false
          this.loadPoolBills(this.currentPool.poolId)
          this.getList()
        } else {
          this.$message({
            type: 'error',
            message: response.msg || '添加失败'
          })
        }
      }).catch(error => {
        console.error('添加票据失败:', error)
        this.$message({
          type: 'error',
          message: '添加失败，请稍后重试'
        })
      })
    },
    handleRemoveBillsFromPool() {
      if (this.billSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要移除的票据'
        })
        return
      }

      this.$confirm('确认移除选中的票据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const billIds = this.billSelection.map(bill => bill.billId)
        removeBillsFromPool({
          poolId: this.currentPool.poolId,
          billIds: billIds
        }).then(response => {
          if (response.code === 1) {
            this.$message({
              type: 'success',
              message: '票据移除成功!'
            })
            this.loadPoolBills(this.currentPool.poolId)
            this.getList()
          } else {
            this.$message({
              type: 'error',
              message: response.msg || '移除失败'
            })
          }
        }).catch(error => {
          console.error('移除票据失败:', error)
          this.$message({
            type: 'error',
            message: '移除失败，请稍后重试'
          })
        })
      })
    },
    handleRefreshPoolBills() {
      this.loadPoolBills(this.currentPool.poolId)
    },
    submitFinancing() {
      if (!this.financingForm.financingAmount) {
        this.$message({
          type: 'warning',
          message: '请输入融资金额'
        })
        return
      }

      if (this.financingForm.financingAmount > this.maxFinancingAmount) {
        this.$message({
          type: 'warning',
          message: '融资金额超过最大可融资金额'
        })
        return
      }

      if (!this.financingForm.financingTerm) {
        this.$message({
          type: 'warning',
          message: '请选择融资期限'
        })
        return
      }

      if (!this.financingForm.financingBank) {
        this.$message({
          type: 'warning',
          message: '请选择融资银行'
        })
        return
      }

      // 调用后端API提交融资申请
      // 将融资期限转换为天数
      const termToDays = { '1M': 30, '3M': 90, '6M': 180, '12M': 365 }
      const financingPeriod = termToDays[this.financingForm.financingTerm] || 90
      // 质押金额取票据池总额，质押率从融资金额和质押金额反算
      const pledgeAmount = this.currentPool.totalAmount || 0
      const pledgeRate = pledgeAmount > 0 ? (this.financingForm.financingAmount / pledgeAmount) : 0.7
      pledgeFinancing({
        poolId: this.currentPool.poolId,
        financingAmount: this.financingForm.financingAmount,
        pledgeAmount: pledgeAmount,
        pledgeRate: parseFloat(pledgeRate.toFixed(4)),
        financingPeriod: financingPeriod,
        description: this.financingForm.financingPurpose || ''
      }).then(response => {
        if (response.code === 1) {
          this.dialogFinancingVisible = false
          this.$message({
            type: 'success',
            message: '融资申请提交成功!'
          })
          this.getList()
        } else {
          this.$message({
            type: 'error',
            message: response.msg || '融资申请失败'
          })
        }
      }).catch(error => {
        console.error('融资申请失败:', error)
        this.$message({
          type: 'error',
          message: '融资申请失败，请稍后重试'
        })
      })
    },
    loadPoolBills(poolId) {
      // 调用后端API获取池内票据
      getPoolBills(poolId).then(response => {
        if (response.code === 1) {
          this.poolBills = response.data || []
        } else {
          this.poolBills = []
        }
      }).catch(error => {
        console.error('获取池内票据失败:', error)
        this.poolBills = []
      })
    },
    loadFinancingRecords(poolId) {
      // 调用后端API获取融资记录
      getFinancingRecords(poolId).then(response => {
        if (response.code === 1) {
          this.financingRecords = response.data || []
        } else {
          this.financingRecords = []
        }
      }).catch(error => {
        console.error('获取融资记录失败:', error)
        this.financingRecords = []
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 调用后端API创建票据池
          createBillPool(this.temp).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$message({
                type: 'success',
                message: '票据池创建成功'
              })
              this.getList()
              this.loadStatistics()
            } else {
              this.$message({
                type: 'error',
                message: response.msg || '创建失败'
              })
            }
          }).catch(error => {
            console.error('创建票据池失败:', error)
            this.$message({
              type: 'error',
              message: '创建失败，请稍后重试'
            })
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 调用后端API更新票据池
          updateBillPool(this.temp).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$message({
                type: 'success',
                message: '票据池更新成功'
              })
              this.getList()
            } else {
              this.$message({
                type: 'error',
                message: response.msg || '更新失败'
              })
            }
          }).catch(error => {
            console.error('更新票据池失败:', error)
            this.$message({
              type: 'error',
              message: '更新失败，请稍后重试'
            })
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        poolId: undefined,
        poolName: '',
        poolType: '',
        billCount: 0,
        totalAmount: 0,
        pledgeRate: 0,
        financingAmount: 0,
        maxPoolValue: 10000000,
        poolDescription: '',
        riskControls: []
      }
    },
    getPoolStatusTagType(status) {
      const typeMap = {
        'NORMAL': 'success',
        'PLEDGED': 'warning',
        'FROZEN': 'info',
        'CLOSED': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getPoolStatusText(status) {
      const textMap = {
        'NORMAL': '正常',
        'PLEDGED': '质押中',
        'FROZEN': '已冻结',
        'CLOSED': '已关闭'
      }
      return textMap[status] || status
    },
    getPoolStatusIcon(status) {
      const iconMap = {
        'NORMAL': 'el-icon-success',
        'PLEDGED': 'el-icon-warning',
        'FROZEN': 'el-icon-lock',
        'CLOSED': 'el-icon-error'
      }
      return iconMap[status] || ''
    },
    getPoolTypeText(type) {
      const textMap = {
        'PLEDGE_FINANCING': '质押融资池',
        'LIQUIDITY_MANAGEMENT': '流动性管理池',
        'RISK_DIVERSIFICATION': '风险分散池',
        'PORTFOLIO': '投资组合池'
      }
      return textMap[type] || type
    },
    getPledgeRateClass(rate) {
      if (rate >= 80) return 'high-pledge-rate'
      if (rate >= 60) return 'medium-pledge-rate'
      return 'low-pledge-rate'
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    },
    formatDate(dateVal) {
      if (!dateVal) return '-'
      const date = new Date(dateVal)
      if (isNaN(date.getTime())) return '-'
      const y = date.getFullYear()
      const m = String(date.getMonth() + 1).padStart(2, '0')
      const d = String(date.getDate()).padStart(2, '0')
      return `${y}-${m}-${d}`
    }
  }
}
</script>

<style lang="scss" scoped>
.bill-pool-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .pool-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.bills-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.amount-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.financing-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          .card-change {
            font-size: 12px;
            color: #909399;
          }
        }
      }
    }
  }

  .chart-card, .search-card, .table-card {
    margin-bottom: 20px;
  }

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h3 {
      margin: 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .chart-container {
    height: 300px;
    width: 100%;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .bill-count, .pool-amount, .financing-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .high-pledge-rate {
    color: #F56C6C;
    font-weight: 600;
  }

  .medium-pledge-rate {
    color: #E6A23C;
    font-weight: 600;
  }

  .low-pledge-rate {
    color: #67C23A;
    font-weight: 600;
  }

  .pool-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }
    
    .pool-bills, .financing-records {
      margin-top: 20px;
      
      h4 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 14px;
        font-weight: 600;
        border-left: 3px solid #409EFF;
        padding-left: 8px;
      }
    }
  }

  .bill-manage-content {
    .manage-header {
      margin-bottom: 16px;
      padding-bottom: 16px;
      border-bottom: 1px solid #EBEEF5;
    }
  }

  .financing-tip {
    margin-top: 8px;
    font-size: 12px;
    color: #909399;
  }
}

.report-stat-card {
  text-align: center;
  .stat-label {
    font-size: 12px;
    color: #909399;
    margin-bottom: 4px;
  }
  .stat-value {
    font-size: 18px;
    font-weight: bold;
    color: #303133;
  }
}
</style>
