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
            <span class="bill-count">{{ row.billCount }}张</span>
          </template>
        </el-table-column>
        <el-table-column label="池内总额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="pool-amount">{{ formatCurrency(row.poolAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="质押率" width="100px" align="center">
          <template slot-scope="{row}">
            <span :class="getPledgeRateClass(row.pledgeRate)">{{ row.pledgeRate }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="融资金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="financing-amount">{{ formatCurrency(row.financingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.creatorName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.createDate }}</span>
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

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
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
        <el-form-item label="最大票据数">
          <el-input-number v-model="temp.maxBillCount" :min="1" :max="1000" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="最大池金额">
          <el-input-number
            v-model="temp.maxPoolAmount"
            :precision="2"
            :step="1000000"
            :min="0"
            :max="1000000000"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="质押率上限">
          <el-slider
            v-model="temp.maxPledgeRate"
            :min="0"
            :max="100"
            :step="5"
            show-stops
            show-input
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
          <el-descriptions-item label="票据数量">{{ currentPool.billCount }}张</el-descriptions-item>
          <el-descriptions-item label="池内总额">{{ formatCurrency(currentPool.poolAmount) }}</el-descriptions-item>
          <el-descriptions-item label="质押率">{{ currentPool.pledgeRate }}%</el-descriptions-item>
          <el-descriptions-item label="融资金额">{{ formatCurrency(currentPool.financingAmount) }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ currentPool.creatorName }}</el-descriptions-item>
          <el-descriptions-item label="创建日期">{{ currentPool.createDate }}</el-descriptions-item>
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
          <el-button type="primary" size="small" @click="handleAddBillsToPool">
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
  </div>
</template>

<script>
import { getBillPoolPage, createBillPool, updateBillPool } from '@/api/globalTreasurer/pzgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'BillPoolManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        poolName: undefined,
        poolStatus: undefined,
        creatorName: undefined,
        createDateRange: undefined
      },
      totalPools: 12,
      totalBillsInPool: 186,
      totalPoolAmount: 35680.5,
      financingBalance: 18560.2,
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
        maxBillCount: 100,
        maxPoolAmount: 10000000,
        maxPledgeRate: 80,
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
      financingChart: null
    }
  },
  mounted() {
    this.getList()
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
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.poolList = [
          {
            poolId: 1,
            poolName: '质押融资池A',
            poolType: 'PLEDGE_FINANCING',
            poolStatus: 'NORMAL',
            billCount: 25,
            poolAmount: 8500000.00,
            pledgeRate: 75,
            financingAmount: 6375000.00,
            creatorName: '张三',
            createDate: '2024-09-01'
          },
          {
            poolId: 2,
            poolName: '流动性管理池B',
            poolType: 'LIQUIDITY_MANAGEMENT',
            poolStatus: 'PLEDGED',
            billCount: 18,
            poolAmount: 5200000.00,
            pledgeRate: 80,
            financingAmount: 4160000.00,
            creatorName: '李四',
            createDate: '2024-09-15'
          },
          {
            poolId: 3,
            poolName: '风险分散池C',
            poolType: 'RISK_DIVERSIFICATION',
            poolStatus: 'NORMAL',
            billCount: 32,
            poolAmount: 12800000.00,
            pledgeRate: 70,
            financingAmount: 8960000.00,
            creatorName: '王五',
            createDate: '2024-09-20'
          }
        ]
        this.total = this.poolList.length
        this.listLoading = false
      }, 1000)
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
      const data = [
        { name: '银行承兑汇票', value: 120, itemStyle: { color: '#409EFF' } },
        { name: '商业承兑汇票', value: 45, itemStyle: { color: '#67C23A' } },
        { name: '电子票据', value: 21, itemStyle: { color: '#E6A23C' } }
      ]
      
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
              name: '票据类型',
              type: 'pie',
              radius: ['50%', '70%'],
              center: ['60%', '50%'],
              data: data
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
      
      this.compositionChart.setOption(option)
    },
    updateFinancingChart() {
      const dates = this.generateDateLabels(30)
      const financingData = this.generateMockData(30, 1000, 5000)
      const repaymentData = this.generateMockData(30, 500, 3000)
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
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
          data: dates
        },
        yAxis: {
          type: 'value'
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
      
      this.financingChart.setOption(option)
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
    generateMockData(count, min, max) {
      const data = []
      for (let i = 0; i < count; i++) {
        data.push(Math.floor(Math.random() * (max - min) + min))
      }
      return data
    },
    handleCompositionChartTypeChange() {
      this.updateCompositionChart()
    },
    handleFinancingPeriodChange() {
      this.updateFinancingChart()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        poolName: undefined,
        poolStatus: undefined,
        creatorName: undefined,
        createDateRange: undefined
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
      this.currentPool = row
      this.loadPoolBills(row.poolId)
      this.loadFinancingRecords(row.poolId)
      this.dialogDetailVisible = true
    },
    handleManageBills(row) {
      this.currentPool = row
      this.loadPoolBills(row.poolId)
      this.dialogBillManageVisible = true
    },
    handlePledgeFinancing(row) {
      this.currentPool = row
      this.maxFinancingAmount = row.poolAmount * row.maxPledgeRate / 100
      this.financingForm = {
        financingAmount: null,
        financingTerm: '',
        financingBank: '',
        financingPurpose: ''
      }
      this.dialogFinancingVisible = true
    },
    handleAddBills() {
      this.$message({
        type: 'info',
        message: '入池票据功能'
      })
    },
    handleFinancing() {
      this.$message({
        type: 'info',
        message: '质押融资功能'
      })
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '数据导出成功'
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
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑票据池'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleFreeze(row) {
      this.$confirm('确认冻结该票据池?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.poolStatus = 'FROZEN'
        this.$message({
          type: 'success',
          message: '票据池冻结成功!'
        })
      })
    },
    handleClose(row) {
      this.$confirm('确认关闭该票据池?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.poolStatus = 'CLOSED'
        this.$message({
          type: 'success',
          message: '票据池关闭成功!'
        })
      })
    },
    handleGenerateReport(row) {
      this.$message({
        type: 'success',
        message: '报告生成成功'
      })
    },
    handleAddBillsToPool() {
      this.$message({
        type: 'info',
        message: '添加票据到池功能'
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
        this.billSelection.forEach(bill => {
          const index = this.poolBills.findIndex(item => item.billNumber === bill.billNumber)
          if (index !== -1) {
            this.poolBills.splice(index, 1)
          }
        })
        this.$message({
          type: 'success',
          message: '票据移除成功!'
        })
      })
    },
    handleRefreshPoolBills() {
      this.loadPoolBills(this.currentPool.poolId)
      this.$message({
        type: 'success',
        message: '票据列表已刷新'
      })
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
      
      this.currentPool.poolStatus = 'PLEDGED'
      this.currentPool.financingAmount = this.financingForm.financingAmount
      this.dialogFinancingVisible = false
      
      this.$message({
        type: 'success',
        message: '融资申请提交成功!'
      })
    },
    loadPoolBills(poolId) {
      // 模拟池内票据数据
      this.poolBills = [
        {
          billNumber: 'BA20240925001',
          billType: '银行承兑汇票',
          billAmount: '¥1,000,000.00',
          acceptorName: '中国工商银行',
          maturityDate: '2024-12-25',
          poolDate: '2024-09-01',
          status: 'NORMAL'
        },
        {
          billNumber: 'CA20240920002',
          billType: '商业承兑汇票',
          billAmount: '¥500,000.00',
          acceptorName: '客户B公司',
          maturityDate: '2024-10-20',
          poolDate: '2024-09-05',
          status: 'PLEDGED'
        }
      ]
    },
    loadFinancingRecords(poolId) {
      // 模拟融资记录数据
      this.financingRecords = [
        {
          financingDate: '2024-09-10',
          financingAmount: '¥5,000,000.00',
          financingTerm: '3个月',
          financingRate: '4.5%',
          financingBank: '中国工商银行',
          status: 'ACTIVE'
        },
        {
          financingDate: '2024-08-15',
          financingAmount: '¥3,000,000.00',
          financingTerm: '6个月',
          financingRate: '4.8%',
          financingBank: '中国建设银行',
          status: 'SETTLED'
        }
      ]
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.poolId = parseInt(Math.random() * 100) + 1024
          this.temp.poolStatus = 'NORMAL'
          this.temp.billCount = 0
          this.temp.poolAmount = 0
          this.temp.pledgeRate = 0
          this.temp.financingAmount = 0
          this.temp.creatorName = '当前用户'
          this.temp.createDate = new Date().toISOString().slice(0, 10)
          this.poolList.unshift(this.temp)
          this.total = this.poolList.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '票据池创建成功'
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const index = this.poolList.findIndex(v => v.poolId === this.temp.poolId)
          this.poolList.splice(index, 1, this.temp)
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '票据池更新成功'
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        poolId: undefined,
        poolName: '',
        poolType: '',
        maxBillCount: 100,
        maxPoolAmount: 10000000,
        maxPledgeRate: 80,
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
</style>
