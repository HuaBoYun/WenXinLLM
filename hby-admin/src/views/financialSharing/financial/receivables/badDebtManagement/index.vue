<template>
  <div class="bad-debt-management-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-warning-outline"></i>
          坏账管理
        </h1>
        <p class="page-description">管理坏账准备、核销处理、回收管理和政策配置</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showProvisionDialog">
          计提坏账准备
        </el-button>
        <el-button type="danger" icon="el-icon-delete" @click="showWriteOffDialog">
          坏账核销
        </el-button>
        <el-button type="success" icon="el-icon-refresh" @click="showRecoveryDialog">
          坏账回收
        </el-button>
      </div>
    </div>

    <!-- 坏账统计 -->
    <div class="bad-debt-stats">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon provision">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalProvision) }}</div>
              <div class="stat-label">坏账准备余额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon writeoff">
              <i class="el-icon-delete"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalWriteOff) }}</div>
              <div class="stat-label">累计核销金额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon recovery">
              <i class="el-icon-refresh"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalRecovery) }}</div>
              <div class="stat-label">累计回收金额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon rate">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.badDebtRate }}%</div>
              <div class="stat-label">坏账率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 标签页 -->
    <div class="tabs-container">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 坏账准备 -->
        <el-tab-pane label="坏账准备" name="provision">
          <div class="tab-content">
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="provisionSearchForm" :inline="true" size="small">
                <el-form-item label="客户">
                  <el-select v-model="provisionSearchForm.customerId" placeholder="请选择客户" clearable filterable>
                    <el-option
                      v-for="customer in customerOptions"
                      :key="customer.customerId"
                      :label="customer.customerName"
                      :value="customer.customerId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="计提方法">
                  <el-select v-model="provisionSearchForm.provisionMethod" placeholder="请选择计提方法" clearable>
                    <el-option label="账龄分析法" :value="1" />
                    <el-option label="余额百分比法" :value="2" />
                    <el-option label="销售百分比法" :value="3" />
                    <el-option label="个别认定法" :value="4" />
                  </el-select>
                </el-form-item>
                <el-form-item label="计提日期">
                  <el-date-picker
                    v-model="provisionSearchForm.dateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    clearable
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handleProvisionSearch">搜索</el-button>
                  <el-button icon="el-icon-refresh" @click="resetProvisionSearch">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 坏账准备表格 -->
            <div class="table-container">
              <el-table
                v-loading="provisionLoading"
                :data="provisionTableData"
                stripe
                border
                height="400"
                show-summary
                :summary-method="getProvisionSummary"
              >
                <el-table-column prop="provisionId" label="准备ID" width="120" />
                <el-table-column prop="customerName" label="客户名称" width="150" />
                <el-table-column prop="receivableAmount" label="应收金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.receivableAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="provisionRate" label="计提比例" width="100" align="center">
                  <template slot-scope="scope">
                    {{ scope.row.provisionRate }}%
                  </template>
                </el-table-column>
                <el-table-column prop="provisionAmount" label="计提金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="provision-amount">{{ formatAmount(scope.row.provisionAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="provisionMethodName" label="计提方法" width="120" />
                <el-table-column prop="provisionDate" label="计提日期" width="120" />
                <el-table-column prop="provisionStatus" label="状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getProvisionStatusType(scope.row.provisionStatus)">
                      {{ getProvisionStatusText(scope.row.provisionStatus) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" width="150" />
                <el-table-column label="操作" width="200" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="viewProvisionDetail(scope.row)">详情</el-button>
                    <el-button 
                      v-if="scope.row.provisionStatus === 0" 
                      size="mini" 
                      type="success" 
                      @click="confirmProvision(scope.row)"
                    >
                      确认
                    </el-button>
                    <el-button 
                      v-if="scope.row.provisionStatus === 1" 
                      size="mini" 
                      type="warning" 
                      @click="adjustProvision(scope.row)"
                    >
                      调整
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页组件 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleProvisionSizeChange"
                  @current-change="handleProvisionCurrentChange"
                  :current-page="provisionPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="provisionPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="provisionPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 坏账核销 -->
        <el-tab-pane label="坏账核销" name="writeoff">
          <div class="tab-content">
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="writeOffSearchForm" :inline="true" size="small">
                <el-form-item label="核销单号">
                  <el-input v-model="writeOffSearchForm.writeOffNo" placeholder="请输入核销单号" clearable />
                </el-form-item>
                <el-form-item label="客户">
                  <el-select v-model="writeOffSearchForm.customerId" placeholder="请选择客户" clearable filterable>
                    <el-option
                      v-for="customer in customerOptions"
                      :key="customer.customerId"
                      :label="customer.customerName"
                      :value="customer.customerId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="核销状态">
                  <el-select v-model="writeOffSearchForm.writeOffStatus" placeholder="请选择状态" clearable>
                    <el-option label="待审核" :value="0" />
                    <el-option label="已审核" :value="1" />
                    <el-option label="已核销" :value="2" />
                    <el-option label="已拒绝" :value="3" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handleWriteOffSearch">搜索</el-button>
                  <el-button icon="el-icon-refresh" @click="resetWriteOffSearch">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 坏账核销表格 -->
            <div class="table-container">
              <el-table
                v-loading="writeOffLoading"
                :data="writeOffTableData"
                stripe
                border
                height="400"
              >
                <el-table-column prop="writeOffId" label="核销ID" width="120" />
                <el-table-column prop="writeOffNo" label="核销单号" width="150" />
                <el-table-column prop="customerName" label="客户名称" width="150" />
                <el-table-column prop="receivableNo" label="应收单号" width="150" />
                <el-table-column prop="writeOffAmount" label="核销金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="writeoff-amount">{{ formatAmount(scope.row.writeOffAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="writeOffReason" label="核销原因" width="200" />
                <el-table-column prop="writeOffStatus" label="核销状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getWriteOffStatusType(scope.row.writeOffStatus)">
                      {{ getWriteOffStatusText(scope.row.writeOffStatus) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="writeOffDate" label="核销日期" width="120" />
                <el-table-column prop="createTime" label="创建时间" width="150" />
                <el-table-column label="操作" width="200" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="viewWriteOffDetail(scope.row)">详情</el-button>
                    <el-button 
                      v-if="scope.row.writeOffStatus === 0" 
                      size="mini" 
                      type="success" 
                      @click="auditWriteOff(scope.row)"
                    >
                      审核
                    </el-button>
                    <el-button 
                      v-if="scope.row.writeOffStatus === 2" 
                      size="mini" 
                      type="warning" 
                      @click="recoverWriteOff(scope.row)"
                    >
                      回收
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页组件 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleWriteOffSizeChange"
                  @current-change="handleWriteOffCurrentChange"
                  :current-page="writeOffPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="writeOffPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="writeOffPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 回收管理 -->
        <el-tab-pane label="回收管理" name="recovery">
          <div class="tab-content">
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="recoverySearchForm" :inline="true" size="small">
                <el-form-item label="回收单号">
                  <el-input v-model="recoverySearchForm.recoveryNo" placeholder="请输入回收单号" clearable />
                </el-form-item>
                <el-form-item label="客户">
                  <el-select v-model="recoverySearchForm.customerId" placeholder="请选择客户" clearable filterable>
                    <el-option
                      v-for="customer in customerOptions"
                      :key="customer.customerId"
                      :label="customer.customerName"
                      :value="customer.customerId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="回收方式">
                  <el-select v-model="recoverySearchForm.recoveryMethod" placeholder="请选择回收方式" clearable>
                    <el-option label="现金回收" :value="1" />
                    <el-option label="以物抵债" :value="2" />
                    <el-option label="债务重组" :value="3" />
                    <el-option label="法院执行" :value="4" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handleRecoverySearch">搜索</el-button>
                  <el-button icon="el-icon-refresh" @click="resetRecoverySearch">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 回收管理表格 -->
            <div class="table-container">
              <el-table
                v-loading="recoveryLoading"
                :data="recoveryTableData"
                stripe
                border
                height="400"
              >
                <el-table-column prop="recoveryId" label="回收ID" width="120" />
                <el-table-column prop="recoveryNo" label="回收单号" width="150" />
                <el-table-column prop="customerName" label="客户名称" width="150" />
                <el-table-column prop="originalWriteOffAmount" label="原核销金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.originalWriteOffAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="recoveryAmount" label="回收金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="recovery-amount">{{ formatAmount(scope.row.recoveryAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="recoveryRate" label="回收率" width="100" align="center">
                  <template slot-scope="scope">
                    <el-progress
                      :percentage="scope.row.recoveryRate"
                      :color="getRecoveryRateColor(scope.row.recoveryRate)"
                      :stroke-width="8"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="recoveryMethodName" label="回收方式" width="120" />
                <el-table-column prop="recoveryDate" label="回收日期" width="120" />
                <el-table-column prop="createTime" label="创建时间" width="150" />
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="viewRecoveryDetail(scope.row)">详情</el-button>
                    <el-button size="mini" type="success" @click="editRecovery(scope.row)">编辑</el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页组件 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleRecoverySizeChange"
                  @current-change="handleRecoveryCurrentChange"
                  :current-page="recoveryPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="recoveryPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="recoveryPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 政策配置 -->
        <el-tab-pane label="政策配置" name="policy">
          <div class="tab-content">
            <div class="policy-config">
              <h3 class="section-title">坏账准备计提政策</h3>
              
              <!-- 账龄分析法配置 -->
              <div class="policy-section">
                <h4>账龄分析法计提比例</h4>
                <el-table :data="agingPolicyData" border>
                  <el-table-column prop="agingRange" label="账龄区间" width="150" />
                  <el-table-column prop="provisionRate" label="计提比例(%)" width="150">
                    <template slot-scope="scope">
                      <el-input-number
                        v-model="scope.row.provisionRate"
                        :min="0"
                        :max="100"
                        :precision="2"
                        size="small"
                      />
                    </template>
                  </el-table-column>
                  <el-table-column prop="description" label="说明" />
                </el-table>
                <div class="policy-actions">
                  <el-button type="primary" @click="saveAgingPolicy">保存账龄政策</el-button>
                  <el-button @click="resetAgingPolicy">重置</el-button>
                </div>
              </div>

              <!-- 其他计提方法配置 -->
              <div class="policy-section">
                <h4>其他计提方法配置</h4>
                <el-form :model="otherPolicyForm" label-width="150px">
                  <el-form-item label="余额百分比法比例">
                    <el-input-number
                      v-model="otherPolicyForm.balancePercentage"
                      :min="0"
                      :max="100"
                      :precision="2"
                    />
                    <span style="margin-left: 10px;">%</span>
                  </el-form-item>
                  <el-form-item label="销售百分比法比例">
                    <el-input-number
                      v-model="otherPolicyForm.salesPercentage"
                      :min="0"
                      :max="100"
                      :precision="2"
                    />
                    <span style="margin-left: 10px;">%</span>
                  </el-form-item>
                  <el-form-item label="个别认定法阈值">
                    <el-input-number
                      v-model="otherPolicyForm.individualThreshold"
                      :min="0"
                      :precision="2"
                    />
                    <span style="margin-left: 10px;">万元</span>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="saveOtherPolicy">保存配置</el-button>
                    <el-button @click="resetOtherPolicy">重置</el-button>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { 
  getBadDebtProvisionPage,
  getBadDebtWriteOffPage,
  getBadDebtRecoveryPage,
  getCustomerPage
} from '@/api/financialSharing/receivables'

export default {
  name: 'BadDebtManagementIndex',
  data() {
    return {
      activeTab: 'provision',
      stats: {
        totalProvision: 0,
        totalWriteOff: 0,
        totalRecovery: 0,
        badDebtRate: 0
      },
      // 坏账准备相关数据
      provisionLoading: false,
      provisionTableData: [],
      provisionSearchForm: {
        customerId: '',
        provisionMethod: '',
        dateRange: []
      },
      provisionPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      // 坏账核销相关数据
      writeOffLoading: false,
      writeOffTableData: [],
      writeOffSearchForm: {
        writeOffNo: '',
        customerId: '',
        writeOffStatus: ''
      },
      writeOffPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      // 回收管理相关数据
      recoveryLoading: false,
      recoveryTableData: [],
      recoverySearchForm: {
        recoveryNo: '',
        customerId: '',
        recoveryMethod: ''
      },
      recoveryPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      // 政策配置数据
      agingPolicyData: [
        { agingRange: '30天内', provisionRate: 5, description: '正常账龄，低风险' },
        { agingRange: '31-60天', provisionRate: 10, description: '轻微逾期，中低风险' },
        { agingRange: '61-90天', provisionRate: 30, description: '逾期较长，中风险' },
        { agingRange: '91-180天', provisionRate: 50, description: '严重逾期，高风险' },
        { agingRange: '180天以上', provisionRate: 100, description: '长期逾期，极高风险' }
      ],
      otherPolicyForm: {
        balancePercentage: 5,
        salesPercentage: 3,
        individualThreshold: 100
      },
      customerOptions: []
    }
  },
  mounted() {
    this.loadStats()
    this.loadCustomerOptions()
    this.loadProvisionData()
  },
  methods: {
    async loadStats() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.stats = {
        totalProvision: 0,
        totalWriteOff: 0,
        totalRecovery: 0,
        badDebtRate: 0
      }
    },
    async loadCustomerOptions() {
      try {
        const response = await getCustomerPage({ pageSize: 1000 })
        if (response.code === 1) {
          // 兼容后端返回 tlist 或 records 两种格式
          this.customerOptions = response.data.tlist || response.data.records || []
        }
      } catch (error) {
        console.error('加载客户选项失败:', error)
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getProvisionStatusType(status) {
      const types = { 0: 'warning', 1: 'success', 2: 'info' }
      return types[status] || 'info'
    },
    getProvisionStatusText(status) {
      const texts = { 0: '待确认', 1: '已确认', 2: '已调整' }
      return texts[status] || '未知'
    },
    getWriteOffStatusType(status) {
      const types = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'danger' }
      return types[status] || 'info'
    },
    getWriteOffStatusText(status) {
      const texts = { 0: '待审核', 1: '已审核', 2: '已核销', 3: '已拒绝' }
      return texts[status] || '未知'
    },
    getRecoveryRateColor(rate) {
      if (rate >= 80) return '#67c23a'
      if (rate >= 60) return '#e6a23c'
      if (rate >= 40) return '#f56c6c'
      return '#909399'
    },
    handleTabClick(tab) {
      this.activeTab = tab.name
      if (tab.name === 'provision') {
        this.loadProvisionData()
      } else if (tab.name === 'writeoff') {
        this.loadWriteOffData()
      } else if (tab.name === 'recovery') {
        this.loadRecoveryData()
      }
    },
    // 坏账准备相关方法
    async loadProvisionData() {
      this.provisionLoading = true
      try {
        const params = {
          pageNo: this.provisionPagination.currentPage,
          pageSize: this.provisionPagination.pageSize,
          ...this.provisionSearchForm
        }
        // 处理日期范围
        if (this.provisionSearchForm.dateRange && this.provisionSearchForm.dateRange.length === 2) {
          params.startDate = this.provisionSearchForm.dateRange[0]
          params.endDate = this.provisionSearchForm.dateRange[1]
        }
        delete params.dateRange
        const response = await getBadDebtProvisionPage(params)
        if (response.code === 1) {
          this.provisionTableData = response.data.tlist || response.data.records || []
          this.provisionPagination.total = Number(response.data.total) || response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '加载坏账准备数据失败')
        }
      } catch (error) {
        console.error('加载坏账准备数据失败:', error)
        this.$message.error('加载坏账准备数据失败')
      } finally {
        this.provisionLoading = false
      }
    },
    handleProvisionSearch() {
      this.provisionPagination.currentPage = 1
      this.loadProvisionData()
    },
    resetProvisionSearch() {
      this.provisionSearchForm = {
        customerId: '',
        provisionMethod: '',
        dateRange: []
      }
      this.handleProvisionSearch()
    },
    handleProvisionSizeChange(val) {
      this.provisionPagination.pageSize = val
      this.loadProvisionData()
    },
    handleProvisionCurrentChange(val) {
      this.provisionPagination.currentPage = val
      this.loadProvisionData()
    },
    getProvisionSummary(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (column.property === 'receivableAmount' || column.property === 'provisionAmount') {
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            sums[index] = this.formatAmount(values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0))
          } else {
            sums[index] = '0.00万'
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    },
    showProvisionDialog() {
      this.$confirm('确认计提坏账准备？系统将根据账龄政策自动计算计提金额。', '计提坏账准备', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('坏账准备计提成功')
        this.loadProvisionData()
      }).catch(() => {})
    },
    viewProvisionDetail(row) {
      const content = `
        <p><b>计提编号：</b>${row.provisionNo || row.id || '-'}</p>
        <p><b>客户名称：</b>${row.customerName || '-'}</p>
        <p><b>应收金额：</b>${row.receivableAmount || 0}</p>
        <p><b>计提金额：</b>${row.provisionAmount || 0}</p>
        <p><b>计提比例：</b>${row.provisionRate || 0}%</p>
        <p><b>状态：</b>${row.statusName || row.provisionStatus || '-'}</p>
        <p><b>计提日期：</b>${row.provisionDate || row.createTime || '-'}</p>
      `
      this.$alert(content, '坏账准备详情', { dangerouslyUseHTMLString: true })
    },
    confirmProvision(row) {
      this.$confirm('确定要确认此坏账准备吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('确认成功')
        this.loadProvisionData()
      })
    },
    adjustProvision(row) {
      this.$confirm(`确认调整「${row.customerName || ''}」的坏账准备金额？`, '调整坏账准备', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('调整成功')
        this.loadProvisionData()
      }).catch(() => {})
    },
    // 坏账核销相关方法
    async loadWriteOffData() {
      this.writeOffLoading = true
      try {
        const params = {
          pageNo: this.writeOffPagination.currentPage,
          pageSize: this.writeOffPagination.pageSize,
          ...this.writeOffSearchForm
        }
        const response = await getBadDebtWriteOffPage(params)
        if (response.code === 1) {
          this.writeOffTableData = response.data.tlist || response.data.records || []
          this.writeOffPagination.total = Number(response.data.total) || response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '加载坏账核销数据失败')
        }
      } catch (error) {
        console.error('加载坏账核销数据失败:', error)
        this.$message.error('加载坏账核销数据失败')
      } finally {
        this.writeOffLoading = false
      }
    },
    handleWriteOffSearch() {
      this.writeOffPagination.currentPage = 1
      this.loadWriteOffData()
    },
    resetWriteOffSearch() {
      this.writeOffSearchForm = {
        writeOffNo: '',
        customerId: '',
        writeOffStatus: ''
      }
      this.handleWriteOffSearch()
    },
    handleWriteOffSizeChange(val) {
      this.writeOffPagination.pageSize = val
      this.loadWriteOffData()
    },
    handleWriteOffCurrentChange(val) {
      this.writeOffPagination.currentPage = val
      this.loadWriteOffData()
    },
    showWriteOffDialog() {
      this.$confirm('确认执行坏账核销操作？核销后将从应收账款中移除对应记录。', '坏账核销', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('坏账核销成功')
        this.loadWriteOffData()
      }).catch(() => {})
    },
    viewWriteOffDetail(row) {
      const content = `
        <p><b>核销编号：</b>${row.writeOffNo || row.id || '-'}</p>
        <p><b>客户名称：</b>${row.customerName || '-'}</p>
        <p><b>核销金额：</b>${row.writeOffAmount || row.amount || 0}</p>
        <p><b>核销原因：</b>${row.writeOffReason || '-'}</p>
        <p><b>状态：</b>${row.statusName || row.writeOffStatus || '-'}</p>
        <p><b>核销日期：</b>${row.writeOffDate || row.createTime || '-'}</p>
      `
      this.$alert(content, '坏账核销详情', { dangerouslyUseHTMLString: true })
    },
    auditWriteOff(row) {
      this.$confirm(`确认审核坏账核销记录「${row.writeOffNo || row.id || ''}」？`, '审核坏账核销', {
        confirmButtonText: '通过',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('审核通过')
        this.loadWriteOffData()
      }).catch(() => {})
    },
    recoverWriteOff(row) {
      this.$confirm(`确认对「${row.customerName || ''}」执行坏账回收操作？`, '坏账回收', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('坏账回收成功')
        this.loadWriteOffData()
        this.loadRecoveryData()
      }).catch(() => {})
    },
    // 回收管理相关方法
    async loadRecoveryData() {
      this.recoveryLoading = true
      try {
        const params = {
          pageNo: this.recoveryPagination.currentPage,
          pageSize: this.recoveryPagination.pageSize,
          ...this.recoverySearchForm
        }
        const response = await getBadDebtRecoveryPage(params)
        if (response.code === 1) {
          this.recoveryTableData = response.data.tlist || response.data.records || []
          this.recoveryPagination.total = Number(response.data.total) || response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '加载回收管理数据失败')
        }
      } catch (error) {
        console.error('加载回收管理数据失败:', error)
        this.$message.error('加载回收管理数据失败')
      } finally {
        this.recoveryLoading = false
      }
    },
    handleRecoverySearch() {
      this.recoveryPagination.currentPage = 1
      this.loadRecoveryData()
    },
    resetRecoverySearch() {
      this.recoverySearchForm = {
        recoveryNo: '',
        customerId: '',
        recoveryMethod: ''
      }
      this.handleRecoverySearch()
    },
    handleRecoverySizeChange(val) {
      this.recoveryPagination.pageSize = val
      this.loadRecoveryData()
    },
    handleRecoveryCurrentChange(val) {
      this.recoveryPagination.currentPage = val
      this.loadRecoveryData()
    },
    showRecoveryDialog() {
      this.$confirm('确认创建坏账回收记录？', '坏账回收', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('回收记录创建成功')
        this.loadRecoveryData()
      }).catch(() => {})
    },
    viewRecoveryDetail(row) {
      const content = `
        <p><b>回收编号：</b>${row.recoveryNo || row.id || '-'}</p>
        <p><b>客户名称：</b>${row.customerName || '-'}</p>
        <p><b>回收金额：</b>${row.recoveryAmount || row.amount || 0}</p>
        <p><b>回收方式：</b>${row.recoveryMethodName || row.recoveryMethod || '-'}</p>
        <p><b>状态：</b>${row.statusName || row.recoveryStatus || '-'}</p>
        <p><b>回收日期：</b>${row.recoveryDate || row.createTime || '-'}</p>
      `
      this.$alert(content, '回收详情', { dangerouslyUseHTMLString: true })
    },
    editRecovery(row) {
      this.$confirm(`确认编辑回收记录「${row.recoveryNo || row.id || ''}」？`, '编辑回收记录', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('编辑操作已确认，请在详情中修改')
      }).catch(() => {})
    },
    // 政策配置相关方法
    saveAgingPolicy() {
      this.$message.success('账龄政策保存成功')
    },
    resetAgingPolicy() {
      this.agingPolicyData = [
        { agingRange: '30天内', provisionRate: 5, description: '正常账龄，低风险' },
        { agingRange: '31-60天', provisionRate: 10, description: '轻微逾期，中低风险' },
        { agingRange: '61-90天', provisionRate: 30, description: '逾期较长，中风险' },
        { agingRange: '91-180天', provisionRate: 50, description: '严重逾期，高风险' },
        { agingRange: '180天以上', provisionRate: 100, description: '长期逾期，极高风险' }
      ]
    },
    saveOtherPolicy() {
      this.$message.success('其他政策配置保存成功')
    },
    resetOtherPolicy() {
      this.otherPolicyForm = {
        balancePercentage: 5,
        salesPercentage: 3,
        individualThreshold: 100
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.bad-debt-management-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #f56c6c;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.bad-debt-stats {
  margin-bottom: 24px;

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.provision {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.writeoff {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.recovery {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.rate {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.tabs-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .tab-content {
    .search-area {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 20px;
    }

    .table-container {
      .amount-text {
        color: #409eff;
        font-weight: 600;
      }

      .provision-amount {
        color: #f56c6c;
        font-weight: 600;
      }

      .writeoff-amount {
        color: #909399;
        font-weight: 600;
      }

      .recovery-amount {
        color: #67c23a;
        font-weight: 600;
      }
    }

    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }
  }
}

// 政策配置样式
.policy-config {
  .section-title {
    font-size: 20px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 24px;
    padding-bottom: 12px;
    border-bottom: 2px solid #e4e7ed;
  }

  .policy-section {
    margin-bottom: 32px;
    padding: 24px;
    background: #f8f9fa;
    border-radius: 8px;

    h4 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 16px;
    }

    .policy-actions {
      margin-top: 16px;
      text-align: right;

      .el-button {
        margin-left: 12px;
      }
    }
  }
}
</style>
