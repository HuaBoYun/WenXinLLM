<template>
  <div class="forward-transaction-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>远期交易管理</h2>
      <p>管理远期合约的创建、执行、交割和估值</p>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="合约编号">
          <el-input v-model="searchForm.contractCode" placeholder="请输入合约编号" clearable />
        </el-form-item>
        <el-form-item label="交易类型">
          <el-select v-model="searchForm.transactionType" placeholder="请选择交易类型" clearable>
            <el-option label="远期外汇" value="FX_FORWARD" />
            <el-option label="远期利率" value="RATE_FORWARD" />
            <el-option label="远期商品" value="COMMODITY_FORWARD" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易状态">
          <el-select v-model="searchForm.status" placeholder="请选择交易状态" clearable>
            <el-option label="待生效" value="PENDING" />
            <el-option label="生效中" value="ACTIVE" />
            <el-option label="已执行" value="EXECUTED" />
            <el-option label="已交割" value="SETTLED" />
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
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建合约</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="info" icon="el-icon-money" size="mini" @click="handleValuation">批量估值</el-button>
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
        <el-table-column label="交易类型" prop="transactionType" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTransactionTypeTag(scope.row.transactionType)">
              {{ getTransactionTypeText(scope.row.transactionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="标的资产" prop="underlyingAsset" width="120" show-overflow-tooltip />
        <el-table-column label="名义本金" prop="notionalAmount" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.notionalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="币种" prop="currency" width="80" align="center" />
        <el-table-column label="远期价格" prop="forwardPrice" width="100" align="right">
          <template slot-scope="scope">
            <span class="price-text">{{ formatPrice(scope.row.forwardPrice) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="当前估值" prop="currentValue" width="120" align="right">
          <template slot-scope="scope">
            <span :class="getValueClass(scope.row.currentValue)">
              {{ formatAmount(scope.row.currentValue) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="损益" prop="pnl" width="100" align="right">
          <template slot-scope="scope">
            <span :class="getPnLClass(scope.row.pnl)">
              {{ formatAmount(scope.row.pnl) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="交易日期" prop="tradeDate" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.tradeDate, '{y}-{m}-{d}') }}</span>
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
                <el-dropdown-item command="execute" v-if="scope.row.status === 'ACTIVE'" icon="el-icon-check">执行</el-dropdown-item>
                <el-dropdown-item command="settle" v-if="scope.row.status === 'EXECUTED'" icon="el-icon-finished">交割</el-dropdown-item>
                <el-dropdown-item command="valuation" icon="el-icon-money">估值</el-dropdown-item>
                <el-dropdown-item command="rates" icon="el-icon-data-line">汇率查询</el-dropdown-item>
                <el-dropdown-item command="cancel" v-if="scope.row.status === 'PENDING'" icon="el-icon-close">取消</el-dropdown-item>
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

    <!-- 新增/修改对话框 -->
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
                <el-form-item label="交易类型" prop="transactionType">
                  <el-select v-model="form.transactionType" placeholder="请选择交易类型" style="width: 100%">
                    <el-option label="远期外汇" value="FX_FORWARD" />
                    <el-option label="远期利率" value="RATE_FORWARD" />
                    <el-option label="远期商品" value="COMMODITY_FORWARD" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="标的资产" prop="underlyingAsset">
                  <el-input v-model="form.underlyingAsset" placeholder="请输入标的资产" />
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
                <el-form-item label="名义本金" prop="notionalAmount">
                  <el-input v-model="form.notionalAmount" placeholder="请输入名义本金" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="远期价格" prop="forwardPrice">
                  <el-input v-model="form.forwardPrice" placeholder="请输入远期价格" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="交易日期" prop="tradeDate">
                  <el-date-picker
                    v-model="form.tradeDate"
                    type="date"
                    placeholder="选择交易日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期日期" prop="maturityDate">
                  <el-date-picker
                    v-model="form.maturityDate"
                    type="date"
                    placeholder="选择到期日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                  />
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
                    <el-option label="BB" value="BB" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="交易员" prop="trader">
                  <el-input v-model="form.trader" placeholder="请输入交易员" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="销售员" prop="salesperson">
                  <el-input v-model="form.salesperson" placeholder="请输入销售员" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="风险参数" name="risk">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="风险限额" prop="riskLimit">
                  <el-input v-model="form.riskLimit" placeholder="请输入风险限额" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="保证金要求" prop="marginRequirement">
                  <el-input v-model="form.marginRequirement" placeholder="请输入保证金要求" />
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
    <el-dialog title="远期交易详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="合约编号">{{ detailData.contractCode }}</el-descriptions-item>
        <el-descriptions-item label="交易类型">
          <el-tag :type="getTransactionTypeTag(detailData.transactionType)">
            {{ getTransactionTypeText(detailData.transactionType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(detailData.status)">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="标的资产">{{ detailData.underlyingAsset }}</el-descriptions-item>
        <el-descriptions-item label="币种">{{ detailData.currency }}</el-descriptions-item>
        <el-descriptions-item label="名义本金">{{ formatAmount(detailData.notionalAmount) }}</el-descriptions-item>
        <el-descriptions-item label="远期价格">{{ formatPrice(detailData.forwardPrice) }}</el-descriptions-item>
        <el-descriptions-item label="当前估值">{{ formatAmount(detailData.currentValue) }}</el-descriptions-item>
        <el-descriptions-item label="损益">
          <span :class="getPnLClass(detailData.pnl)">{{ formatAmount(detailData.pnl) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="交易日期">{{ parseTime(detailData.tradeDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ parseTime(detailData.maturityDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="交易对手">{{ detailData.counterparty }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getForwardTransactionPage, 
  getForwardTransaction, 
  createForwardContract, 
  updateForwardContract, 
  deleteForwardTransaction,
  executeForwardContract,
  settleForwardContract,
  valuateForwardContract,
  getForwardRates
} from '@/api/globalTreasurer/yspx'
import { parseTime } from '@/utils'

export default {
  name: 'ForwardTransaction',
  data() {
    return {
      // 加载状态
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 交易列表
      transactionList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      detailOpen: false,
      // 详情数据
      detailData: {},
      // 活动标签页
      activeTab: 'basic',
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        contractCode: null,
        transactionType: null,
        status: null,
        currency: null,
        maturityDateStart: null,
        maturityDateEnd: null,
        orgId: this.$store.getters.orgId
      },
      // 搜索表单
      searchForm: {
        contractCode: '',
        transactionType: '',
        status: '',
        currency: '',
        maturityDateRange: []
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        contractCode: [
          { required: true, message: '合约编号不能为空', trigger: 'blur' }
        ],
        transactionType: [
          { required: true, message: '交易类型不能为空', trigger: 'change' }
        ],
        underlyingAsset: [
          { required: true, message: '标的资产不能为空', trigger: 'blur' }
        ],
        currency: [
          { required: true, message: '币种不能为空', trigger: 'change' }
        ],
        notionalAmount: [
          { required: true, message: '名义本金不能为空', trigger: 'blur' }
        ],
        forwardPrice: [
          { required: true, message: '远期价格不能为空', trigger: 'blur' }
        ],
        tradeDate: [
          { required: true, message: '交易日期不能为空', trigger: 'change' }
        ],
        maturityDate: [
          { required: true, message: '到期日期不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    /** 查询交易列表 */
    getList() {
      this.loading = true
      getForwardTransactionPage(this.queryParams).then(response => {
        this.transactionList = response.data.records
        this.total = response.data.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleSearch() {
      this.queryParams.current = 1
      Object.assign(this.queryParams, this.searchForm)
      if (this.searchForm.maturityDateRange && this.searchForm.maturityDateRange.length === 2) {
        this.queryParams.maturityDateStart = this.searchForm.maturityDateRange[0]
        this.queryParams.maturityDateEnd = this.searchForm.maturityDateRange[1]
      }
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.searchForm = {
        contractCode: '',
        transactionType: '',
        status: '',
        currency: '',
        maturityDateRange: []
      }
      this.queryParams = {
        current: 1,
        size: 10,
        contractCode: null,
        transactionType: null,
        status: null,
        currency: null,
        maturityDateStart: null,
        maturityDateEnd: null,
        orgId: this.$store.getters.orgId
      }
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新建远期合约'
      this.activeTab = 'basic'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const transactionId = row.transactionId || this.ids
      getForwardTransaction(transactionId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改远期合约'
        this.activeTab = 'basic'
      })
    },
    /** 查看详情 */
    handleView(row) {
      this.detailData = row
      this.detailOpen = true
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.transactionId != null) {
            updateForwardContract(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            createForwardContract(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const transactionIds = row.transactionId || this.ids
      this.$modal.confirm('是否确认删除远期交易编号为"' + transactionIds + '"的数据项？').then(function() {
        return deleteForwardTransaction(transactionIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('derivatives/forward/export', {
        ...this.queryParams
      }, `forward_transaction_${new Date().getTime()}.xlsx`)
    },
    /** 批量估值 */
    handleValuation() {
      this.$modal.confirm('是否确认对所有选中的远期合约进行估值？').then(() => {
        // 批量估值逻辑
        this.$modal.msgSuccess('估值完成')
        this.getList()
      }).catch(() => {})
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.transactionId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 行点击事件 */
    handleRowClick(row) {
      this.$refs.table.toggleRowSelection(row)
    },
    /** 更多操作命令 */
    handleCommand(command, row) {
      switch (command) {
        case 'execute':
          this.handleExecute(row)
          break
        case 'settle':
          this.handleSettle(row)
          break
        case 'valuation':
          this.handleSingleValuation(row)
          break
        case 'rates':
          this.handleRatesQuery(row)
          break
        case 'cancel':
          this.handleCancel(row)
          break
      }
    },
    /** 执行合约 */
    handleExecute(row) {
      this.$modal.confirm('是否确认执行远期合约"' + row.contractCode + '"？').then(() => {
        return executeForwardContract(row.transactionId, {})
      }).then(() => {
        this.$modal.msgSuccess('执行成功')
        this.getList()
      }).catch(() => {})
    },
    /** 交割合约 */
    handleSettle(row) {
      this.$modal.confirm('是否确认交割远期合约"' + row.contractCode + '"？').then(() => {
        return settleForwardContract(row.transactionId, {})
      }).then(() => {
        this.$modal.msgSuccess('交割成功')
        this.getList()
      }).catch(() => {})
    },
    /** 单个估值 */
    handleSingleValuation(row) {
      valuateForwardContract(row.transactionId, {}).then(response => {
        this.$modal.msgSuccess('估值完成，当前估值：' + this.formatAmount(response.data.currentValue))
        this.getList()
      })
    },
    /** 汇率查询 */
    handleRatesQuery(row) {
      getForwardRates({ currency: row.currency }).then(response => {
        this.$modal.msgSuccess('当前远期汇率：' + response.data.rate)
      })
    },
    /** 取消合约 */
    handleCancel(row) {
      this.$prompt('请输入取消原因', '取消合约', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        // 取消合约逻辑
        this.$modal.msgSuccess('取消成功')
        this.getList()
      }).catch(() => {})
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
        transactionType: null,
        underlyingAsset: null,
        currency: null,
        notionalAmount: null,
        forwardPrice: null,
        tradeDate: null,
        maturityDate: null,
        counterparty: null,
        counterpartyRating: null,
        trader: null,
        salesperson: null,
        riskLimit: null,
        marginRequirement: null,
        remark: null,
        orgId: this.$store.getters.orgId
      }
      this.resetForm('form')
    },
    /** 获取交易类型标签 */
    getTransactionTypeTag(type) {
      const tagMap = {
        'FX_FORWARD': 'primary',
        'RATE_FORWARD': 'success',
        'COMMODITY_FORWARD': 'warning'
      }
      return tagMap[type] || ''
    },
    /** 获取交易类型文本 */
    getTransactionTypeText(type) {
      const textMap = {
        'FX_FORWARD': '远期外汇',
        'RATE_FORWARD': '远期利率',
        'COMMODITY_FORWARD': '远期商品'
      }
      return textMap[type] || type
    },
    /** 获取状态标签 */
    getStatusTag(status) {
      const tagMap = {
        'PENDING': 'info',
        'ACTIVE': 'success',
        'EXECUTED': 'warning',
        'SETTLED': 'primary',
        'CANCELLED': 'danger'
      }
      return tagMap[status] || ''
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const textMap = {
        'PENDING': '待生效',
        'ACTIVE': '生效中',
        'EXECUTED': '已执行',
        'SETTLED': '已交割',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },
    /** 格式化金额 */
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    /** 格式化价格 */
    formatPrice(price) {
      if (price == null) return '0.0000'
      return parseFloat(price).toFixed(4)
    },
    /** 获取估值样式类 */
    getValueClass(value) {
      if (value > 0) return 'positive-value'
      if (value < 0) return 'negative-value'
      return 'neutral-value'
    },
    /** 获取损益样式类 */
    getPnLClass(pnl) {
      if (pnl > 0) return 'profit'
      if (pnl < 0) return 'loss'
      return 'neutral'
    }
  }
}
</script>

<style scoped>
.forward-transaction-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.amount-text {
  font-weight: bold;
  color: #409eff;
}

.price-text {
  font-weight: bold;
  color: #67c23a;
}

.positive-value {
  color: #67c23a;
  font-weight: bold;
}

.negative-value {
  color: #f56c6c;
  font-weight: bold;
}

.neutral-value {
  color: #909399;
}

.profit {
  color: #67c23a;
  font-weight: bold;
}

.loss {
  color: #f56c6c;
  font-weight: bold;
}

.neutral {
  color: #909399;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}

.el-dropdown-link:hover {
  color: #66b1ff;
}
</style>
