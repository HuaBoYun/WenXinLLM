<template>
  <div class="swap-transaction-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>掉期交易管理</h2>
      <p>管理利率掉期、货币掉期的创建、估值和现金流交换</p>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="合约编号">
          <el-input v-model="searchForm.contractCode" placeholder="请输入合约编号" clearable />
        </el-form-item>
        <el-form-item label="掉期类型">
          <el-select v-model="searchForm.swapType" placeholder="请选择掉期类型" clearable>
            <el-option label="利率掉期" value="INTEREST_RATE" />
            <el-option label="货币掉期" value="CURRENCY" />
            <el-option label="商品掉期" value="COMMODITY" />
            <el-option label="信用违约掉期" value="CDS" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易状态">
          <el-select v-model="searchForm.status" placeholder="请选择交易状态" clearable>
            <el-option label="待生效" value="PENDING" />
            <el-option label="生效中" value="ACTIVE" />
            <el-option label="已终止" value="TERMINATED" />
            <el-option label="已到期" value="MATURED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="币种">
          <el-select v-model="searchForm.currency" placeholder="请选择币种" clearable>
            <el-option label="人民币" value="CNY" />
            <el-option label="美元" value="USD" />
            <el-option label="欧元" value="EUR" />
            <el-option label="日元" value="JPY" />
          </el-select>
        </el-form-item>
        <el-form-item label="到期日期">
          <el-date-picker
            v-model="searchForm.maturityDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="operation-card" shadow="never">
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddInterestRateSwap">利率掉期</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" icon="el-icon-plus" size="mini" @click="handleAddCurrencySwap">货币掉期</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="info" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-money" size="mini" @click="handleBatchValuation">批量估值</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="transactionList"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        stripe
        border
        height="500"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="合约编号" prop="contractCode" width="150" show-overflow-tooltip />
        <el-table-column label="掉期类型" prop="swapType" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSwapTypeTag(scope.row.swapType)">
              {{ getSwapTypeText(scope.row.swapType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="名义本金" prop="notionalAmount" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.notionalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="币种" prop="currency" width="80" align="center">
          <template slot-scope="scope">{{ getCurrencyText(scope.row.currency) }}</template>
        </el-table-column>
        <el-table-column label="固定利率" prop="fixedRate" width="100" align="right">
          <template slot-scope="scope">
            <span class="rate-text">{{ formatRate(scope.row.fixedRate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="浮动利率基准" prop="floatingRateBasis" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini">{{ scope.row.floatingRateBasis || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="当前估值" prop="currentValue" width="120" align="right">
          <template slot-scope="scope">
            <span :class="getValueClass(scope.row.currentValue)">
              {{ formatAmount(scope.row.currentValue) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="下次支付日" prop="nextPaymentDate" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.nextPaymentDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" prop="maturityDate" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.maturityDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" style="margin-left: 10px">
              <span class="el-dropdown-link">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="valuation" icon="el-icon-money">估值</el-dropdown-item>
                <el-dropdown-item command="cashflow" icon="el-icon-s-data">现金流</el-dropdown-item>
                <el-dropdown-item command="payment" v-if="scope.row.status === 'ACTIVE'" icon="el-icon-wallet">支付处理</el-dropdown-item>
                <el-dropdown-item command="terminate" v-if="scope.row.status === 'ACTIVE'" icon="el-icon-close">终止</el-dropdown-item>
                <el-dropdown-item command="cancel" v-if="scope.row.status === 'PENDING'" icon="el-icon-circle-close">取消</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.current"
        :limit.sync="queryParams.size"
        @pagination="getList"
      />
    </el-card>

    <!-- 利率掉期对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="合约编号" prop="contractCode">
                  <el-input v-model="form.contractCode" placeholder="请输入合约编号" :disabled="form.transactionId != null" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="掉期类型" prop="swapType">
                  <el-select v-model="form.swapType" placeholder="请选择掉期类型" style="width: 100%" disabled>
                    <el-option label="利率掉期" value="INTEREST_RATE" />
                    <el-option label="货币掉期" value="CURRENCY" />
                    <el-option label="商品掉期" value="COMMODITY" />
                    <el-option label="信用违约掉期" value="CDS" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="名义本金" prop="notionalAmount">
                  <el-input v-model="form.notionalAmount" placeholder="请输入名义本金" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="币种" prop="currency">
                  <el-select v-model="form.currency" placeholder="请选择币种" style="width: 100%">
                    <el-option label="人民币" value="CNY" />
                    <el-option label="美元" value="USD" />
                    <el-option label="欧元" value="EUR" />
                    <el-option label="日元" value="JPY" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="生效日期" prop="effectiveDate">
                  <el-date-picker :value="form.effectiveDate" @input="form.effectiveDate = $event" type="date" placeholder="选择生效日期" format="yyyy-MM-dd" value-format="timestamp" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期日期" prop="maturityDate">
                  <el-date-picker :value="form.maturityDate" @input="form.maturityDate = $event" type="date" placeholder="选择到期日期" format="yyyy-MM-dd" value-format="timestamp" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="利率信息" name="rate" v-if="form.swapType === 'INTEREST_RATE' || form.swapType === 'CURRENCY'">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="固定利率" prop="fixedRate">
                  <el-input v-model="form.fixedRate" placeholder="请输入固定利率(%)" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="浮动利率基准" prop="floatingRateBasis">
                  <el-select v-model="form.floatingRateBasis" placeholder="请选择浮动利率基准" style="width: 100%">
                    <el-option label="SHIBOR" value="SHIBOR" />
                    <el-option label="LPR" value="LPR" />
                    <el-option label="LIBOR" value="LIBOR" />
                    <el-option label="SOFR" value="SOFR" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="利差" prop="spread">
                  <el-input v-model="form.spread" placeholder="请输入利差(bp)" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="支付频率" prop="paymentFrequency">
                  <el-select v-model="form.paymentFrequency" placeholder="请选择支付频率" style="width: 100%">
                    <el-option label="月度" value="MONTHLY" />
                    <el-option label="季度" value="QUARTERLY" />
                    <el-option label="半年" value="SEMI_ANNUAL" />
                    <el-option label="年度" value="ANNUAL" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="计息基准" prop="dayCountBasis">
                  <el-select v-model="form.dayCountBasis" placeholder="请选择计息基准" style="width: 100%">
                    <el-option label="ACT/360" value="ACT_360" />
                    <el-option label="ACT/365" value="ACT_365" />
                    <el-option label="30/360" value="30_360" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="支付方向" prop="payDirection">
                  <el-select v-model="form.payDirection" placeholder="请选择支付方向" style="width: 100%">
                    <el-option label="支付固定/收取浮动" value="PAY_FIXED" />
                    <el-option label="收取固定/支付浮动" value="RECEIVE_FIXED" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="货币信息" name="currency" v-if="form.swapType === 'CURRENCY'">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="支付币种" prop="payCurrency">
                  <el-select v-model="form.payCurrency" placeholder="请选择支付币种" style="width: 100%">
                    <el-option label="人民币" value="CNY" />
                    <el-option label="美元" value="USD" />
                    <el-option label="欧元" value="EUR" />
                    <el-option label="日元" value="JPY" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="收取币种" prop="receiveCurrency">
                  <el-select v-model="form.receiveCurrency" placeholder="请选择收取币种" style="width: 100%">
                    <el-option label="人民币" value="CNY" />
                    <el-option label="美元" value="USD" />
                    <el-option label="欧元" value="EUR" />
                    <el-option label="日元" value="JPY" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="支付本金" prop="payNotional">
                  <el-input v-model="form.payNotional" placeholder="请输入支付本金" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="收取本金" prop="receiveNotional">
                  <el-input v-model="form.receiveNotional" placeholder="请输入收取本金" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="初始汇率" prop="initialExchangeRate">
                  <el-input v-model="form.initialExchangeRate" placeholder="请输入初始汇率" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否交换本金" prop="exchangePrincipal">
                  <el-switch v-model="form.exchangePrincipal" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="交易对手" name="counterparty">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="交易对手" prop="counterparty">
                  <el-input v-model="form.counterparty" placeholder="请输入交易对手" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="对手评级" prop="counterpartyRating">
                  <el-select v-model="form.counterpartyRating" placeholder="请选择对手评级" style="width: 100%">
                    <el-option label="AAA" value="AAA" />
                    <el-option label="AA" value="AA" />
                    <el-option label="A" value="A" />
                    <el-option label="BBB" value="BBB" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="3" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="掉期交易详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="合约编号">{{ detailData.contractCode }}</el-descriptions-item>
        <el-descriptions-item label="掉期类型">
          <el-tag :type="getSwapTypeTag(detailData.swapType)">{{ getSwapTypeText(detailData.swapType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(detailData.status)">{{ getStatusText(detailData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="名义本金">{{ formatAmount(detailData.notionalAmount) }}</el-descriptions-item>
        <el-descriptions-item label="币种">{{ getCurrencyText(detailData.currency) }}</el-descriptions-item>
        <el-descriptions-item label="固定利率">{{ formatRate(detailData.fixedRate) }}</el-descriptions-item>
        <el-descriptions-item label="浮动利率基准">{{ detailData.floatingRateBasis }}</el-descriptions-item>
        <el-descriptions-item label="当前估值">
          <span :class="getValueClass(detailData.currentValue)">{{ formatAmount(detailData.currentValue) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="支付频率">{{ detailData.paymentFrequency }}</el-descriptions-item>
        <el-descriptions-item label="生效日期">{{ parseTime(detailData.effectiveDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ parseTime(detailData.maturityDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="交易对手">{{ detailData.counterparty }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 现金流对话框 -->
    <el-dialog title="现金流明细" :visible.sync="cashflowOpen" width="900px" append-to-body>
      <el-table :data="cashflowList" stripe border height="400">
        <el-table-column label="支付日期" prop="paymentDate" width="120" align="center">
          <template slot-scope="scope">{{ parseTime(scope.row.paymentDate, '{y}-{m}-{d}') }}</template>
        </el-table-column>
        <el-table-column label="支付类型" prop="paymentType" width="100" align="center" />
        <el-table-column label="支付金额" prop="payAmount" width="120" align="right">
          <template slot-scope="scope"><span class="loss">{{ formatAmount(scope.row.payAmount) }}</span></template>
        </el-table-column>
        <el-table-column label="收取金额" prop="receiveAmount" width="120" align="right">
          <template slot-scope="scope"><span class="profit">{{ formatAmount(scope.row.receiveAmount) }}</span></template>
        </el-table-column>
        <el-table-column label="净现金流" prop="netCashflow" width="120" align="right">
          <template slot-scope="scope"><span :class="getPnLClass(scope.row.netCashflow)">{{ formatAmount(scope.row.netCashflow) }}</span></template>
        </el-table-column>
        <el-table-column label="状态" prop="status" width="100" align="center">
          <template slot-scope="scope"><el-tag :type="scope.row.status === 'PAID' ? 'success' : 'info'" size="mini">{{ scope.row.status === 'PAID' ? '已支付' : '待支付' }}</el-tag></template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { listSwapTransaction, getSwapTransaction, delSwapTransaction, addSwapTransaction, updateSwapTransaction, cancelSwapTransaction, valuationSwapTransaction, batchValuationSwapTransaction, getCashflow, paymentSwapTransaction, terminateSwapTransaction } from '@/api/globalTreasurer-new/derivatives/dqjy'
import request from '@/utils/request'

export default {
  name: 'Dqjy',
  data() {
    return {
      // 搜索表单
      searchForm: {
        contractCode: null,
        swapType: null,
        status: null,
        currency: null,
        maturityDateRange: null
      },
      // 查询参数
      queryParams: {
        current: 1,
        size: 10
      },
      // 加载状态
      loading: false,
      // 交易列表
      transactionList: [],
      // 总条数
      total: 0,
      // 弹窗标题
      title: '',
      // 是否显示弹窗
      open: false,
      // 是否显示详情弹窗
      detailOpen: false,
      // 是否显示现金流弹窗
      cashflowOpen: false,
      // 详情数据
      detailData: {},
      // 现金流列表
      cashflowList: [],
      // 当前激活的tab
      activeTab: 'basic',
      // 表单数据
      form: {},
      // 表单校验
      rules: {
        contractCode: [
          { required: true, message: '合约编号不能为空', trigger: 'blur' }
        ],
        swapType: [
          { required: true, message: '掉期类型不能为空', trigger: 'change' }
        ],
        notionalAmount: [
          { required: true, message: '名义本金不能为空', trigger: 'blur' }
        ],
        currency: [
          { required: true, message: '币种不能为空', trigger: 'change' }
        ],
        effectiveDate: [
          { required: true, message: '生效日期不能为空', trigger: 'change' }
        ],
        maturityDate: [
          { required: true, message: '到期日期不能为空', trigger: 'change' }
        ],
        fixedRate: [
          { required: true, message: '固定利率不能为空', trigger: 'blur' }
        ]
      },
      // 单个选中
      single: true,
      // 多个选中
      multiple: true,
      // 选中的行
      selectedRows: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      // 处理日期范围，避免传递数组字段
      const { maturityDateRange, ...otherSearchParams } = this.searchForm
      const params = {
        ...this.queryParams,
        ...otherSearchParams
      }
      // 单独处理日期范围
      if (maturityDateRange && maturityDateRange.length === 2) {
        params.maturityDateStart = maturityDateRange[0]
        params.maturityDateEnd = maturityDateRange[1]
      } else {
        params.maturityDateStart = null
        params.maturityDateEnd = null
      }
      listSwapTransaction(params).then(response => {
        this.transactionList = response.data.tlist || []
        this.total = response.data.totalRecord || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleSearch() {
      this.queryParams.current = 1
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.searchForm = {
        contractCode: null,
        swapType: null,
        status: null,
        currency: null,
        maturityDateRange: null
      }
      this.handleSearch()
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.selectedRows = selection
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 行点击事件 */
    handleRowClick(row) {
      this.$refs.table && this.$refs.table.toggleRowSelection(row)
    },
    /** 新增利率掉期 */
    handleAddInterestRateSwap() {
      this.reset()
      this.form.swapType = 'INTEREST_RATE'
      this.title = '新增利率掉期'
      this.open = true
      this.activeTab = 'basic'
    },
    /** 新增货币掉期 */
    handleAddCurrencySwap() {
      this.reset()
      this.form.swapType = 'CURRENCY'
      this.title = '新增货币掉期'
      this.open = true
      this.activeTab = 'basic'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const transactionId = row.transactionId || this.selectedRows[0]?.transactionId
      if (!transactionId) {
        this.$message.warning('请选择要修改的记录')
        return
      }
      this.reset()
      getSwapTransaction(transactionId).then(response => {
        const data = response.data
        // 处理日期字段：将时间戳或字符串转换为Date对象
        if (data.effectiveDate) {
          data.effectiveDate = new Date(data.effectiveDate)
        }
        if (data.maturityDate) {
          data.maturityDate = new Date(data.maturityDate)
        }
        this.form = data
        this.title = '修改掉期交易'
        this.open = true
        this.activeTab = 'basic'
      })
    },
    /** 删除按钮操作 */
    handleDelete() {
      const transactionIds = this.selectedRows.map(item => item.transactionId)
      if (transactionIds.length === 0) {
        this.$message.warning('请选择要删除的记录')
        return
      }
      this.$confirm('是否确认删除选中的掉期交易记录?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return delSwapTransaction(transactionIds)
      }).then(() => {
        this.getList()
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$confirm('是否确认导出所有掉期交易数据?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        console.log('开始导出...')

        // 处理日期范围参数
        const { maturityDateRange, ...otherSearchParams } = this.searchForm
        const params = {
          ...otherSearchParams,
          orgId: this.$store.getters.orgId
        }
        // 单独处理日期范围
        if (maturityDateRange && maturityDateRange.length === 2) {
          params.maturityDateStart = maturityDateRange[0]
          params.maturityDateEnd = maturityDateRange[1]
        } else {
          params.maturityDateStart = null
          params.maturityDateEnd = null
        }

        console.log('导出参数:', params)

        // 使用原生 axios 下载文件，避免 request 拦截器处理 blob 响应
        const axios = require('axios')
        axios({
          url: '/vab-mock-server/qqsk/derivatives/swap/export',
          method: 'get',
          params: params,
          responseType: 'blob'
        }).then(response => {
          console.log('导出响应状态:', response.status)
          console.log('导出响应头:', response.headers)
          console.log('导出数据类型:', response.data.type)
          console.log('导出数据大小:', response.data.size)

          // 从响应头获取文件名
          let filename = `掉期交易_${new Date().getTime()}.xlsx`
          const contentDisposition = response.headers['content-disposition']
          if (contentDisposition) {
            const filenameMatch = contentDisposition.match(/filename[^;=\n]*((['"]).*?\2|[^;\n]*)/)
            if (filenameMatch && filenameMatch[1]) {
              filename = filenameMatch[1].replace(/['"]/g, '')
            }
          }

          // 创建下载链接
          const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = filename
          link.style.display = 'none'
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(link.href)
          this.$message.success('导出成功')
        }).catch(error => {
          console.error('导出失败:', error)
          console.error('错误详情:', error.response)
          this.$message.error('导出失败：' + (error.message || '未知错误'))
        })
      }).catch(() => {})
    },
    /** 批量估值 */
    handleBatchValuation() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要估值的记录')
        return
      }
      const transactionIds = this.selectedRows.map(item => item.transactionId)
      this.$confirm('是否确认对选中的掉期交易进行估值?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.loading = true
        return batchValuationSwapTransaction(transactionIds)
      }).then(() => {
        this.getList()
        this.$message.success('批量估值成功')
        this.loading = false
      }).catch(error => {
        console.error('批量估值失败:', error)
        this.$message.error('批量估值失败，请稍后重试')
        this.loading = false
      })
    },
    /** 查看详情 */
    handleView(row) {
      const transactionId = row.transactionId
      getSwapTransaction(transactionId).then(response => {
        this.detailData = response.data
        this.detailOpen = true
      })
    },
    /** 更多操作 */
    handleCommand(command, row) {
      switch (command) {
        case 'valuation':
          this.handleValuation(row)
          break
        case 'cashflow':
          this.handleCashflow(row)
          break
        case 'payment':
          this.handlePayment(row)
          break
        case 'terminate':
          this.handleTerminate(row)
          break
        case 'cancel':
          this.handleCancel(row)
          break
      }
    },
    /** 估值 */
    handleValuation(row) {
      valuationSwapTransaction(row.transactionId).then(response => {
        this.$message.success('估值成功')
        this.getList()
      }).catch(error => {
        console.error('估值失败:', error)
        this.$message.error('估值失败，请稍后重试')
      })
    },
    /** 现金流 */
    handleCashflow(row) {
      getCashflow(row.transactionId).then(response => {
        this.cashflowList = response.data || []
        this.cashflowOpen = true
      }).catch(error => {
        console.error('获取现金流失败:', error)
        this.$message.error('获取现金流失败，请稍后重试')
      })
    },
    /** 支付处理 */
    handlePayment(row) {
      this.$confirm('是否确认进行支付处理?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        paymentSwapTransaction({ transactionId: row.transactionId }).then(response => {
          this.$message.success('支付处理成功')
          this.getList()
        }).catch(error => {
          console.error('支付处理失败:', error)
          this.$message.error('支付处理失败，请稍后重试')
        })
      }).catch(() => {})
    },
    /** 终止 */
    handleTerminate(row) {
      this.$confirm('是否确认终止该掉期交易?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        terminateSwapTransaction(row.transactionId).then(response => {
          this.$message.success('终止成功')
          this.getList()
        }).catch(error => {
          console.error('终止失败:', error)
          this.$message.error('终止失败，请稍后重试')
        })
      }).catch(() => {})
    },
    /** 取消 */
    handleCancel(row) {
      this.$confirm('是否确认取消该掉期交易?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const transactionId = row.transactionId
        console.log('取消掉期，transactionId:', transactionId)
        return cancelSwapTransaction(transactionId)
      }).then(response => {
        console.log('取消掉期响应:', response)
        this.$message.success('取消成功')
        this.getList()
      }).catch(error => {
        console.error('取消掉期失败:', error)
        this.$message.error('取消失败，请稍后重试')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.transactionId != null) {
            updateSwapTransaction(this.form).then(() => {
              this.$message.success('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addSwapTransaction(this.form).then(() => {
              this.$message.success('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        transactionId: null,
        contractCode: null,
        swapType: null,
        notionalAmount: null,
        currency: null,
        effectiveDate: null,
        maturityDate: null,
        fixedRate: null,
        floatingRateBasis: null,
        spread: null,
        paymentFrequency: null,
        dayCountBasis: null,
        payDirection: null,
        payCurrency: null,
        receiveCurrency: null,
        payNotional: null,
        receiveNotional: null,
        initialExchangeRate: null,
        exchangePrincipal: false,
        counterparty: null,
        counterpartyRating: null,
        remark: null
      }
      this.resetForm('form')
    },
    /** 获取币种文本 */
    getCurrencyText(currency) {
      const textMap = {
        'CNY': '人民币',
        'USD': '美元',
        'EUR': '欧元',
        'JPY': '日元',
        'GBP': '英镑',
        'HKD': '港币'
      }
      return textMap[currency] || currency
    },
    /** 获取掉期类型标签样式 */
    getSwapTypeTag(type) {
      const tagMap = {
        'INTEREST_RATE': 'primary',
        'CURRENCY': 'success',
        'COMMODITY': 'warning',
        'CDS': 'danger'
      }
      return tagMap[type] || 'info'
    },
    /** 获取掉期类型文本 */
    getSwapTypeText(type) {
      const textMap = {
        'INTEREST_RATE': '利率掉期',
        'CURRENCY': '货币掉期',
        'COMMODITY': '商品掉期',
        'CDS': '信用违约掉期'
      }
      return textMap[type] || type
    },
    /** 获取状态标签样式 */
    getStatusTag(status) {
      const tagMap = {
        'PENDING': 'info',
        'ACTIVE': 'success',
        'TERMINATED': 'warning',
        'MATURED': 'info',
        'CANCELLED': 'danger'
      }
      return tagMap[status] || 'info'
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const textMap = {
        'PENDING': '待生效',
        'ACTIVE': '生效中',
        'TERMINATED': '已终止',
        'MATURED': '已到期',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },
    /** 格式化金额 */
    formatAmount(amount) {
      if (amount == null) return '-'
      return Number(amount).toLocaleString('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
    },
    /** 格式化利率 */
    formatRate(rate) {
      if (rate == null) return '-'
      return rate + '%'
    },
    /** 获取金额样式类 */
    getValueClass(value) {
      if (value == null) return ''
      return value >= 0 ? 'profit' : 'loss'
    },
    /** 获取盈亏样式类 */
    getPnLClass(value) {
      if (value == null) return ''
      return value >= 0 ? 'profit' : 'loss'
    },
    /** 时间解析 */
    parseTime(time, pattern) {
      if (!time) return null
      const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
      let date
      if (typeof time === 'object') {
        date = time
      } else {
        if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
          time = parseInt(time)
        }
        if ((typeof time === 'number') && (time.toString().length === 10)) {
          time = time * 1000
        }
        date = new Date(time)
      }
      const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
      }
      const time_str = format.replace(/{([ymdhisa])+}/g, (result, key) => {
        const value = formatObj[key]
        if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
        return value.toString().padStart(2, '0')
      })
      return time_str
    },
    /** 重置表单 */
    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields()
      }
    }
  }
}
</script>

<style scoped lang="scss">
.swap-transaction-container {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    padding: 20px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 4px;
    color: #fff;

    h2 {
      margin: 0 0 10px 0;
      font-size: 24px;
      font-weight: 600;
    }

    p {
      margin: 0;
      font-size: 14px;
      opacity: 0.9;
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .operation-card {
    margin-bottom: 20px;
  }

  .table-card {
    margin-bottom: 20px;
  }

  .amount-text {
    font-weight: 600;
    color: #303133;
  }

  .rate-text {
    font-weight: 600;
    color: #409eff;
  }

  .profit {
    color: #f56c6c;
    font-weight: 600;
  }

  .loss {
    color: #67c23a;
    font-weight: 600;
  }

  .el-dropdown-link {
    cursor: pointer;
    color: #409eff;
    font-size: 12px;

    &:hover {
      color: #66b1ff;
    }
  }

  ::v-deep .el-dialog__body {
    padding: 20px;
  }

  ::v-deep .el-descriptions-item__label {
    font-weight: 600;
  }
}
</style>
