<template>
  <div class="futures-transaction-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>期货交易管理</h2>
      <p>管理期货合约的创建、保证金管理、每日结算和交割</p>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="合约编号">
          <el-input v-model="searchForm.contractCode" placeholder="请输入合约编号" clearable />
        </el-form-item>
        <el-form-item label="期货类型">
          <el-select v-model="searchForm.futuresType" placeholder="请选择期货类型" clearable>
            <el-option label="商品期货" value="COMMODITY" />
            <el-option label="金融期货" value="FINANCIAL" />
            <el-option label="股指期货" value="INDEX" />
            <el-option label="外汇期货" value="FX" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易方向">
          <el-select v-model="searchForm.direction" placeholder="请选择交易方向" clearable>
            <el-option label="多头" value="LONG" />
            <el-option label="空头" value="SHORT" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易状态">
          <el-select v-model="searchForm.status" placeholder="请选择交易状态" clearable>
            <el-option label="待生效" value="PENDING" />
            <el-option label="持仓中" value="OPEN" />
            <el-option label="已平仓" value="CLOSED" />
            <el-option label="已交割" value="SETTLED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="到期日期">
          <el-date-picker
            v-model="searchForm.expiryDateRange"
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
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建期货</el-button>
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
          <el-button type="success" icon="el-icon-s-data" size="mini" @click="handleDailySettlement">每日结算</el-button>
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
        <el-table-column label="期货类型" prop="futuresType" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getFuturesTypeTag(scope.row.futuresType)">
              {{ getFuturesTypeText(scope.row.futuresType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="标的资产" prop="underlyingAsset" width="120" show-overflow-tooltip />
        <el-table-column label="交易方向" prop="direction" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.direction === 'LONG' ? 'success' : 'danger'" size="mini">
              {{ scope.row.direction === 'LONG' ? '多头' : '空头' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="合约数量" prop="contractSize" width="100" align="right">
          <template slot-scope="scope">
            <span class="quantity-text">{{ formatNumber(scope.row.contractSize) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="开仓价格" prop="openPrice" width="100" align="right">
          <template slot-scope="scope">
            <span class="price-text">{{ formatPrice(scope.row.openPrice) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="当前价格" prop="currentPrice" width="100" align="right">
          <template slot-scope="scope">
            <span class="price-text">{{ formatPrice(scope.row.currentPrice) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="保证金" prop="margin" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.margin) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="浮动盈亏" prop="unrealizedPnL" width="120" align="right">
          <template slot-scope="scope">
            <span :class="getPnLClass(scope.row.unrealizedPnL)">
              {{ formatAmount(scope.row.unrealizedPnL) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" prop="expiryDate" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.expiryDate, '{y}-{m}-{d}') }}</span>
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
                <el-dropdown-item command="margin" icon="el-icon-money">保证金</el-dropdown-item>
                <el-dropdown-item command="close" v-if="scope.row.status === 'OPEN'" icon="el-icon-close">平仓</el-dropdown-item>
                <el-dropdown-item command="settle" v-if="scope.row.status === 'OPEN'" icon="el-icon-finished">交割</el-dropdown-item>
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
                <el-form-item label="期货类型" prop="futuresType">
                  <el-select v-model="form.futuresType" placeholder="请选择期货类型" style="width: 100%">
                    <el-option label="商品期货" value="COMMODITY" />
                    <el-option label="金融期货" value="FINANCIAL" />
                    <el-option label="股指期货" value="INDEX" />
                    <el-option label="外汇期货" value="FX" />
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
                <el-form-item label="交易方向" prop="direction">
                  <el-select v-model="form.direction" placeholder="请选择交易方向" style="width: 100%">
                    <el-option label="多头" value="LONG" />
                    <el-option label="空头" value="SHORT" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="合约数量" prop="contractSize">
                  <el-input v-model="form.contractSize" placeholder="请输入合约数量" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="开仓价格" prop="openPrice">
                  <el-input v-model="form.openPrice" placeholder="请输入开仓价格" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
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
              <el-col :span="12">
                <el-form-item label="到期日期" prop="expiryDate">
                  <el-date-picker
                    :value="form.expiryDate"
                    @input="form.expiryDate = $event"
                    type="date"
                    placeholder="选择到期日期"
                    format="yyyy-MM-dd"
                    value-format="timestamp"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="保证金信息" name="margin">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="初始保证金" prop="initialMargin">
                  <el-input v-model="form.initialMargin" placeholder="请输入初始保证金" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="维持保证金" prop="maintenanceMargin">
                  <el-input v-model="form.maintenanceMargin" placeholder="请输入维持保证金" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="保证金比例" prop="marginRatio">
                  <el-input v-model="form.marginRatio" placeholder="请输入保证金比例(%)" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="交易所" prop="exchange">
                  <el-select v-model="form.exchange" placeholder="请选择交易所" style="width: 100%">
                    <el-option label="上海期货交易所" value="SHFE" />
                    <el-option label="大连商品交易所" value="DCE" />
                    <el-option label="郑州商品交易所" value="CZCE" />
                    <el-option label="中国金融期货交易所" value="CFFEX" />
                    <el-option label="上海国际能源交易中心" value="INE" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="交易对手" name="counterparty">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="期货公司" prop="broker">
                  <el-input v-model="form.broker" placeholder="请输入期货公司" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="交易员" prop="trader">
                  <el-input v-model="form.trader" placeholder="请输入交易员" />
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
    <el-dialog title="期货交易详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="合约编号">{{ detailData.contractCode }}</el-descriptions-item>
        <el-descriptions-item label="期货类型">
          <el-tag :type="getFuturesTypeTag(detailData.futuresType)">
            {{ getFuturesTypeText(detailData.futuresType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="交易方向">
          <el-tag :type="detailData.direction === 'LONG' ? 'success' : 'danger'" size="mini">
            {{ detailData.direction === 'LONG' ? '多头' : '空头' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="标的资产">{{ detailData.underlyingAsset }}</el-descriptions-item>
        <el-descriptions-item label="合约数量">{{ formatNumber(detailData.contractSize) }}</el-descriptions-item>
        <el-descriptions-item label="开仓价格">{{ formatPrice(detailData.openPrice) }}</el-descriptions-item>
        <el-descriptions-item label="当前价格">{{ formatPrice(detailData.currentPrice) }}</el-descriptions-item>
        <el-descriptions-item label="保证金">{{ formatAmount(detailData.margin) }}</el-descriptions-item>
        <el-descriptions-item label="浮动盈亏">
          <span :class="getPnLClass(detailData.unrealizedPnL)">{{ formatAmount(detailData.unrealizedPnL) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="交易所">{{ detailData.exchange }}</el-descriptions-item>
        <el-descriptions-item label="期货公司">{{ detailData.broker }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(detailData.status)">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="开仓日期">{{ parseTime(detailData.tradeDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ parseTime(detailData.expiryDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 保证金管理对话框 -->
    <el-dialog title="保证金管理" :visible.sync="marginOpen" width="600px" append-to-body>
      <el-form ref="marginForm" :model="marginForm" label-width="120px">
        <el-form-item label="当前保证金">
          <el-input v-model="marginForm.currentMargin" disabled />
        </el-form-item>
        <el-form-item label="维持保证金">
          <el-input v-model="marginForm.maintenanceMargin" disabled />
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="marginForm.operationType" placeholder="请选择操作类型" style="width: 100%">
            <el-option label="追加保证金" value="ADD" />
            <el-option label="提取保证金" value="WITHDRAW" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作金额">
          <el-input v-model="marginForm.amount" placeholder="请输入操作金额" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="marginForm.remark" type="textarea" placeholder="请输入备注" :rows="2" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMarginForm">确 定</el-button>
        <el-button @click="marginOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getFuturesTransactionPage,
  getFuturesTransaction,
  createFuturesContract,
  updateFuturesContract,
  deleteFuturesTransaction,
  cancelFuturesContract,
  manageFuturesMargin,
  getFuturesMarginInfo,
  settleFuturesContract,
  dailySettlement,
  download
} from '@/api/globalTreasurer/yspx'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'

export default {
  name: 'FuturesTransaction',
  components: {
    Pagination
  },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      transactionList: [],
      title: '',
      open: false,
      detailOpen: false,
      marginOpen: false,
      detailData: {},
      activeTab: 'basic',
      queryParams: {
        current: 1,
        size: 10,
        contractCode: null,
        futuresType: null,
        direction: null,
        status: null,
        expiryDateStart: null,
        expiryDateEnd: null,
        orgId: this.$store.getters.orgId
      },
      searchForm: {
        contractCode: '',
        futuresType: '',
        direction: '',
        status: '',
        expiryDateRange: []
      },
      form: {},
      marginForm: {},
      rules: {
        contractCode: [{ required: true, message: '合约编号不能为空', trigger: 'blur' }],
        futuresType: [{ required: true, message: '期货类型不能为空', trigger: 'change' }],
        underlyingAsset: [{ required: true, message: '标的资产不能为空', trigger: 'blur' }],
        direction: [{ required: true, message: '交易方向不能为空', trigger: 'change' }],
        contractSize: [{ required: true, message: '合约数量不能为空', trigger: 'blur' }],
        openPrice: [{ required: true, message: '开仓价格不能为空', trigger: 'blur' }],
        currency: [{ required: true, message: '币种不能为空', trigger: 'change' }],
        expiryDate: [{ required: true, message: '到期日期不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    getList() {
      this.loading = true
      getFuturesTransactionPage(this.queryParams).then(response => {
        this.transactionList = response.data.records || []
        this.total = response.data.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleSearch() {
      this.queryParams.current = 1
      // 先处理日期范围
      if (this.searchForm.expiryDateRange && this.searchForm.expiryDateRange.length === 2) {
        this.queryParams.expiryDateStart = this.searchForm.expiryDateRange[0]
        this.queryParams.expiryDateEnd = this.searchForm.expiryDateRange[1]
      } else {
        this.queryParams.expiryDateStart = null
        this.queryParams.expiryDateEnd = null
      }
      // 复制其他搜索字段（排除 expiryDateRange）
      const { expiryDateRange, ...otherSearchParams } = this.searchForm
      Object.assign(this.queryParams, otherSearchParams)
      this.getList()
    },
    handleReset() {
      this.searchForm = { contractCode: '', futuresType: '', direction: '', status: '', expiryDateRange: [] }
      this.queryParams = { current: 1, size: 10, contractCode: null, futuresType: null, direction: null, status: null, expiryDateStart: null, expiryDateEnd: null, orgId: this.$store.getters.orgId }
      this.getList()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新建期货合约'
      this.activeTab = 'basic'
    },
    handleUpdate(row) {
      this.reset()
      const transactionId = row.transactionId || this.ids
      getFuturesTransaction(transactionId).then(response => {
        const data = response.data
        // 处理日期字段：将时间戳或字符串转换为Date对象
        if (data.expiryDate) {
          data.expiryDate = new Date(data.expiryDate)
        }
        this.form = data
        this.open = true
        this.title = '修改期货合约'
        this.activeTab = 'basic'
      })
    },
    handleView(row) {
      this.detailData = row
      this.detailOpen = true
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.transactionId != null) {
            updateFuturesContract(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            createFuturesContract(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const transactionIds = row.transactionId || this.ids
      const ids = Array.isArray(transactionIds) ? transactionIds : [transactionIds]
      this.$modal.confirm('是否确认删除期货交易编号为"' + ids.join(',') + '"的数据项？').then(() => {
        return deleteFuturesTransaction(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      download('derivatives/futures/export', { ...this.queryParams }, `futures_transaction_${new Date().getTime()}.xlsx`)
    },
    handleMarginManagement() {
      this.marginForm = { currentMargin: '', maintenanceMargin: '', operationType: '', amount: '', remark: '' }
      this.marginOpen = true
    },
    handleDailySettlement() {
      this.$modal.confirm('是否确认执行每日结算？').then(() => {
        dailySettlement(null, { orgId: this.$store.getters.orgId }).then(() => {
          this.$modal.msgSuccess('每日结算完成')
          this.getList()
        })
      }).catch(() => {})
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.transactionId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleRowClick(row) {
      if (this.$refs.table) {
        this.$refs.table.toggleRowSelection(row)
      }
    },
    handleCommand(command, row) {
      console.log('handleCommand 被调用, command:', command, 'row:', row)
      switch (command) {
        case 'margin': this.handleMarginDetail(row); break
        case 'close': this.handleClose(row); break
        case 'settle': this.handleSettle(row); break
        case 'cancel': this.handleCancel(row); break
      }
    },
    handleMarginDetail(row) {
      console.log('=== 保证金管理 - 开始 ===')
      console.log('transactionId:', row.transactionId)
      getFuturesMarginInfo(row.transactionId).then(response => {
        console.log('后端返回完整数据:', response)
        console.log('response.data:', response.data)
        const data = response.data || {}
        console.log('data:', data)
        const transaction = data.transaction || {}
        console.log('transaction:', transaction)
        console.log('transaction.margin:', transaction.margin)
        console.log('transaction.maintenanceMargin:', transaction.maintenanceMargin)

        this.marginForm = {
          transactionId: row.transactionId,
          currentMargin: transaction.margin || 0,
          maintenanceMargin: transaction.maintenanceMargin || 0,
          operationType: '',
          amount: '',
          remark: ''
        }
        console.log('设置的 marginForm:', this.marginForm)
        this.marginOpen = true
      })
    },
    handleClose(row) {
      this.$modal.confirm('是否确认平仓期货合约"' + row.contractCode + '"？').then(() => {
        this.$modal.msgSuccess('平仓成功')
        this.getList()
      }).catch(() => {})
    },
    handleSettle(row) {
      this.$modal.confirm('是否确认交割期货合约"' + row.contractCode + '"？').then(() => {
        return settleFuturesContract(row.transactionId, {})
      }).then(() => {
        this.$modal.msgSuccess('交割成功')
        this.getList()
      }).catch(() => {})
    },
    handleCancel(row) {
      this.$prompt('请输入取消原因', '取消合约', { confirmButtonText: '确定', cancelButtonText: '取消', inputPattern: /.+/ }).then(({ value }) => {
        if (!value || value.trim() === '') {
          this.$message.warning('请输入取消原因')
          return
        }
        const transactionId = row.transactionId
        console.log('取消期货，transactionId:', transactionId)
        return cancelFuturesContract(transactionId, { reason: value.trim() })
      }).then(response => {
        console.log('取消期货响应:', response)
        this.$modal.msgSuccess('取消成功')
        this.getList()
      }).catch(error => {
        console.error('取消期货失败:', error)
        this.$message.error('取消失败，请稍后重试')
      })
    },
    submitMarginForm() {
      manageFuturesMargin(this.marginForm.transactionId, this.marginForm).then(() => {
        this.$modal.msgSuccess('保证金操作成功')
        this.marginOpen = false
        this.getList()
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { transactionId: null, contractCode: null, futuresType: null, underlyingAsset: null, direction: null, contractSize: null, openPrice: null, currency: null, expiryDate: null, initialMargin: null, maintenanceMargin: null, marginRatio: null, exchange: null, broker: null, trader: null, remark: null, orgId: this.$store.getters.orgId }
      this.resetForm('form')
    },
    getFuturesTypeTag(type) {
      const tagMap = { 'COMMODITY': 'primary', 'FINANCIAL': 'success', 'INDEX': 'warning', 'FX': 'info' }
      return tagMap[type] || ''
    },
    getFuturesTypeText(type) {
      const textMap = { 'COMMODITY': '商品期货', 'FINANCIAL': '金融期货', 'INDEX': '股指期货', 'FX': '外汇期货' }
      return textMap[type] || type
    },
    getStatusTag(status) {
      const tagMap = { 'PENDING': 'info', 'OPEN': 'success', 'CLOSED': 'warning', 'SETTLED': 'primary', 'CANCELLED': 'danger' }
      return tagMap[status] || ''
    },
    getStatusText(status) {
      const textMap = { 'PENDING': '待生效', 'OPEN': '持仓中', 'CLOSED': '已平仓', 'SETTLED': '已交割', 'CANCELLED': '已取消' }
      return textMap[status] || status
    },
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    formatPrice(price) {
      if (price == null) return '0.0000'
      return parseFloat(price).toFixed(4)
    },
    formatNumber(number) {
      if (number == null) return '0'
      return parseFloat(number).toLocaleString('zh-CN')
    },
    getPnLClass(pnl) {
      if (pnl > 0) return 'profit'
      if (pnl < 0) return 'loss'
      return 'neutral'
    },
    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields()
      }
    }
  }
}
</script>

<style scoped>
.futures-transaction-container { padding: 20px; }
.page-header { margin-bottom: 20px; }
.page-header h2 { margin: 0 0 10px 0; color: #303133; }
.page-header p { margin: 0; color: #909399; font-size: 14px; }
.search-card, .operation-card, .table-card { margin-bottom: 20px; }
.amount-text { font-weight: bold; color: #409eff; }
.price-text { font-weight: bold; color: #67c23a; }
.quantity-text { font-weight: bold; color: #303133; }
.profit { color: #67c23a; font-weight: bold; }
.loss { color: #f56c6c; font-weight: bold; }
.neutral { color: #909399; }
.el-dropdown-link { cursor: pointer; color: #409eff; }
.el-dropdown-link:hover { color: #66b1ff; }
</style>

