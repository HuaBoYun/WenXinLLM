<template>
  <div class="bank-reconciliation">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-document-checked"></i>
            银行对账管理
          </h2>
          <p class="page-description">管理企业银行账户对账业务，确保账实相符</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-refresh" @click="handleAutoReconcile">
            自动对账
          </el-button>
          <el-button type="success" icon="el-icon-upload" @click="handleImportStatement">
            导入对账单
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出报告
          </el-button>
        </div>
      </div>
    </div>

    <!-- 对账概览卡片 -->
    <div class="reconciliation-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总对账数</div>
                <div class="card-value">{{ totalReconciliations }}</div>
                <div class="card-change">笔对账</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon matched-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已匹配</div>
                <div class="card-value">{{ matchedRecords }}</div>
                <div class="card-change positive">匹配率{{ matchRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon unmatched-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">未匹配</div>
                <div class="card-value">{{ unmatchedRecords }}</div>
                <div class="card-change warning">待处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon difference-icon">
                <i class="el-icon-minus"></i>
              </div>
              <div class="card-info">
                <div class="card-title">差异金额</div>
                <div class="card-value">{{ differenceAmount }}</div>
                <div class="card-change negative">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="对账期间">
            <el-date-picker
              v-model="listQuery.reconciliationPeriod"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
              @change="handleFilter"
            />
          </el-form-item>
          <el-form-item label="银行账户">
            <el-select
              v-model="listQuery.bankAccount"
              placeholder="请选择银行账户"
              clearable
              style="width: 200px;"
              @change="handleFilter"
            >
              <el-option
                v-for="account in bankAccounts"
                :key="account.accountId"
                :label="`${account.bankName} - ${account.accountNumber}`"
                :value="account.accountId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="对账状态">
            <el-select
              v-model="listQuery.reconciliationStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
              @change="handleFilter"
            >
              <el-option label="待对账" value="PENDING" />
              <el-option label="对账中" value="PROCESSING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="有差异" value="DIFFERENCE" />
            </el-select>
          </el-form-item>
          <el-form-item label="匹配状态">
            <el-select
              v-model="listQuery.matchStatus"
              placeholder="请选择匹配状态"
              clearable
              style="width: 120px;"
              @change="handleFilter"
            >
              <el-option label="全部匹配" value="FULLY_MATCHED" />
              <el-option label="部分匹配" value="PARTIALLY_MATCHED" />
              <el-option label="未匹配" value="UNMATCHED" />
            </el-select>
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

    <!-- 对账表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="reconciliationList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="对账ID" prop="reconciliationId" width="80" align="center" />
        <el-table-column label="对账期间" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.reconciliationPeriod }}</span>
          </template>
        </el-table-column>
        <el-table-column label="银行账户" width="200px" align="center">
          <template slot-scope="{row}">
            <div>
              <div class="bank-name">{{ row.bankName }}</div>
              <div class="account-number">{{ row.accountNumber }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="账面余额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="book-balance">{{ formatCurrency(row.bookBalance) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="银行余额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="bank-balance">{{ formatCurrency(row.bankBalance) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="差异金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="difference-amount" :class="row.differenceAmount !== 0 ? 'has-difference' : ''">
              {{ formatCurrency(row.differenceAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="匹配状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getMatchStatusTagType(row.matchStatus)" size="mini">
              {{ getMatchStatusText(row.matchStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="对账状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getReconciliationStatusTagType(row.reconciliationStatus)" size="mini">
              {{ getReconciliationStatusText(row.reconciliationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="对账日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDateCN(row.reconciliationDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.operatorName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              查看详情
            </el-button>
            <el-button v-if="row.reconciliationStatus === 'PENDING'" size="mini" type="success" @click="handleReconcile(row)">
              开始对账
            </el-button>
            <el-button v-if="row.matchStatus === 'UNMATCHED'" size="mini" type="warning" @click="handleManualMatch(row)">
              手工匹配
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 对账详情对话框 -->
    <el-dialog title="对账详情" :visible.sync="dialogDetailVisible" width="1200px">
      <div v-if="currentReconciliation" class="reconciliation-detail">
        <!-- 基本信息 -->
        <el-descriptions :column="3" border class="detail-descriptions">
          <el-descriptions-item label="对账期间">{{ currentReconciliation.reconciliationPeriod }}</el-descriptions-item>
          <el-descriptions-item label="银行账户">{{ currentReconciliation.bankName }} - {{ currentReconciliation.accountNumber }}</el-descriptions-item>
          <el-descriptions-item label="对账状态">
            <el-tag :type="getReconciliationStatusTagType(currentReconciliation.reconciliationStatus)">
              {{ getReconciliationStatusText(currentReconciliation.reconciliationStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="账面余额">{{ formatCurrency(currentReconciliation.bookBalance) }}</el-descriptions-item>
          <el-descriptions-item label="银行余额">{{ formatCurrency(currentReconciliation.bankBalance) }}</el-descriptions-item>
          <el-descriptions-item label="差异金额">
            <span :class="currentReconciliation.differenceAmount !== 0 ? 'has-difference' : ''">
              {{ formatCurrency(currentReconciliation.differenceAmount) }}
            </span>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 明细表格 -->
        <div class="detail-tables">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="已匹配记录" name="matched">
              <el-table :data="matchedRecordsList" border size="small" max-height="300">
                <el-table-column label="交易日期" prop="transactionDate" width="100" />
                <el-table-column label="摘要" prop="description" min-width="150" />
                <el-table-column label="账面金额" prop="bookAmount" width="120" align="right">
                  <template slot-scope="{row}">
                    <span>{{ formatCurrency(row.bookAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="银行金额" prop="bankAmount" width="120" align="right">
                  <template slot-scope="{row}">
                    <span>{{ formatCurrency(row.bankAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="匹配方式" prop="matchType" width="100" align="center">
                  <template slot-scope="{row}">
                    <el-tag :type="row.matchType === 'AUTO' ? 'success' : 'warning'" size="mini">
                      {{ row.matchType === 'AUTO' ? '自动' : '手工' }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="未匹配记录" name="unmatched">
              <el-table :data="unmatchedRecordsList" border size="small" max-height="300">
                <el-table-column type="selection" width="55" />
                <el-table-column label="来源" prop="source" width="80" align="center">
                  <template slot-scope="{row}">
                    <el-tag :type="row.source === 'BOOK' ? 'primary' : 'success'" size="mini">
                      {{ row.source === 'BOOK' ? '账面' : '银行' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="交易日期" prop="transactionDate" width="100" />
                <el-table-column label="摘要" prop="description" min-width="150" />
                <el-table-column label="金额" prop="amount" width="120" align="right">
                  <template slot-scope="{row}">
                    <span>{{ formatCurrency(row.amount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100" align="center">
                  <template slot-scope="{row}">
                    <el-button type="text" size="mini" @click="handleMatchRecord(row)">匹配</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="差异分析" name="analysis">
              <div class="difference-analysis">
                <el-row :gutter="20">
                  <el-col :span="12">
                    <h4>差异原因分析</h4>
                    <ul class="analysis-list">
                      <li v-for="reason in differenceReasons" :key="reason.type">
                        <span class="reason-type">{{ reason.type }}:</span>
                        <span class="reason-amount">{{ formatCurrency(reason.amount) }}</span>
                        <span class="reason-desc">{{ reason.description }}</span>
                      </li>
                    </ul>
                  </el-col>
                  <el-col :span="12">
                    <h4>调节建议</h4>
                    <ul class="suggestion-list">
                      <li v-for="suggestion in adjustmentSuggestions" :key="suggestion.id">
                        {{ suggestion.description }}
                      </li>
                    </ul>
                  </el-col>
                </el-row>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentReconciliation && currentReconciliation.reconciliationStatus === 'DIFFERENCE'" type="primary" @click="handleAdjustment">
          生成调节表
        </el-button>
      </div>
    </el-dialog>

    <!-- 导入对账单对话框 -->
    <el-dialog title="导入银行对账单" :visible.sync="dialogImportVisible" width="600px">
      <el-form ref="importForm" :model="importForm" label-width="120px">
        <el-form-item label="银行账户" prop="bankAccountId">
          <el-select v-model="importForm.bankAccountId" placeholder="请选择银行账户" style="width: 100%;">
            <el-option
              v-for="account in bankAccounts"
              :key="account.accountId"
              :label="`${account.bankName} - ${account.accountNumber}`"
              :value="account.accountId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="对账期间" prop="period">
          <el-date-picker
            v-model="importForm.period"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="对账单文件">
          <el-upload
            class="upload-demo"
            drag
            action="#"
            :auto-upload="false"
            :on-change="handleFileChange"
            accept=".xlsx,.xls,.csv"
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传xlsx/xls/csv文件，且不超过10MB</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogImportVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImport">确认导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBankReconciliationPage, startReconciliation, importBankStatement, getReconciliationOverview } from '@/api/globalTreasurer/xjgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'BankReconciliation',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        reconciliationPeriod: undefined,
        bankAccount: undefined,
        reconciliationStatus: undefined,
        matchStatus: undefined
      },
      totalReconciliations: 0,
      matchedRecords: 0,
      unmatchedRecords: 0,
      matchRate: 0,
      differenceAmount: 0,
      reconciliationList: [],
      multipleSelection: [],
      bankAccounts: [],
      currentReconciliation: null,
      dialogDetailVisible: false,
      dialogImportVisible: false,
      activeTab: 'matched',
      matchedRecordsList: [],
      unmatchedRecordsList: [],
      differenceReasons: [],
      adjustmentSuggestions: [],
      importForm: {
        bankAccountId: undefined,
        period: undefined,
        file: null
      }
    }
  },
  async created() {
    await this.loadReconciliationOverview()
    this.getList()
    this.loadBankAccounts()
  },
  methods: {
    /** 加载对账概览统计 */
    async loadReconciliationOverview() {
      try {
        const response = await getReconciliationOverview({ orgId: this.$store.getters.orgId })
        if (response.code === 200 || response.code === 1) {
          this.totalReconciliations = response.data.totalReconciliations || 0
          this.matchedRecords = response.data.matchedRecords || 0
          this.unmatchedRecords = response.data.unmatchedRecords || 0
          this.matchRate = response.data.matchRate || 0
          this.differenceAmount = response.data.differenceAmount || 0
        }
      } catch (error) {
        console.error('加载对账概览失败:', error)
      }
    },

    getList() {
      this.listLoading = true
      // 调用API获取对账列表数据
      getBankReconciliationPage(this.listQuery).then(response => {
        if (response.code === 200 || response.code === 1) {
          this.reconciliationList = response.data.list || []
          this.total = response.data.total || 0
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取对账列表失败:', error)
        this.listLoading = false
      })
    },
    loadBankAccounts() {
      // 从API或store中获取银行账户列表
      // 这里暂时保留空数组,等待后端接口
      this.bankAccounts = []
    },
    handleFilter() {
      this.listQuery.page = 1

      // 处理日期范围参数
      if (this.listQuery.reconciliationPeriod && Array.isArray(this.listQuery.reconciliationPeriod) && this.listQuery.reconciliationPeriod.length === 2) {
        // 将日期范围数组转换为 startDate 和 endDate 参数
        this.listQuery.reconciliationPeriodStart = this.listQuery.reconciliationPeriod[0]
        this.listQuery.reconciliationPeriodEnd = this.listQuery.reconciliationPeriod[1]
      } else {
        // 清除日期范围参数
        this.listQuery.reconciliationPeriodStart = undefined
        this.listQuery.reconciliationPeriodEnd = undefined
      }

      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        reconciliationPeriod: undefined,
        bankAccount: undefined,
        reconciliationStatus: undefined,
        matchStatus: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleAutoReconcile() {
      this.$confirm('确认开始自动对账?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '自动对账已启动'
        })
      })
    },
    handleImportStatement() {
      this.dialogImportVisible = true
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '对账报告导出成功'
      })
    },
    handleViewDetail(row) {
      this.currentReconciliation = row
      this.loadReconciliationDetail(row.reconciliationId)
      this.dialogDetailVisible = true
    },
    handleReconcile(row) {
      this.$confirm('确认开始对账?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.reconciliationStatus = 'PROCESSING'
        setTimeout(() => {
          row.reconciliationStatus = 'COMPLETED'
          row.matchStatus = 'FULLY_MATCHED'
        }, 3000)
        this.$message({
          type: 'success',
          message: '对账已启动'
        })
      })
    },
    handleManualMatch(row) {
      this.$message({
        type: 'info',
        message: '进入手工匹配模式'
      })
    },
    handleMatchRecord(row) {
      this.$message({
        type: 'success',
        message: '记录匹配成功'
      })
    },
    handleAdjustment() {
      this.$message({
        type: 'success',
        message: '银行存款余额调节表已生成'
      })
    },
    handleFileChange(file) {
      this.importForm.file = file.raw
    },
    handleImport() {
      if (!this.importForm.bankAccountId) {
        this.$message({
          type: 'warning',
          message: '请选择银行账户'
        })
        return
      }
      if (!this.importForm.period) {
        this.$message({
          type: 'warning',
          message: '请选择对账期间'
        })
        return
      }
      if (!this.importForm.file) {
        this.$message({
          type: 'warning',
          message: '请选择对账单文件'
        })
        return
      }
      
      this.dialogImportVisible = false
      this.$message({
        type: 'success',
        message: '对账单导入成功'
      })
    },
    loadReconciliationDetail(reconciliationId) {
      // 模拟加载详情数据
      this.matchedRecordsList = [
        {
          transactionDate: '2024-09-20',
          description: '销售回款',
          bookAmount: 100000.00,
          bankAmount: 100000.00,
          matchType: 'AUTO'
        },
        {
          transactionDate: '2024-09-21',
          description: '供应商付款',
          bookAmount: -50000.00,
          bankAmount: -50000.00,
          matchType: 'AUTO'
        }
      ]
      
      this.unmatchedRecordsList = [
        {
          source: 'BOOK',
          transactionDate: '2024-09-22',
          description: '银行手续费',
          amount: -15.00
        },
        {
          source: 'BANK',
          transactionDate: '2024-09-23',
          description: '利息收入',
          amount: 1500.00
        }
      ]
      
      this.differenceReasons = [
        {
          type: '未达账项',
          amount: -1500.00,
          description: '企业已付银行未付'
        }
      ]
      
      this.adjustmentSuggestions = [
        { id: 1, description: '核实银行手续费扣款情况' },
        { id: 2, description: '确认利息收入入账时间' },
        { id: 3, description: '检查未达账项的后续到账情况' }
      ]
    },
    getMatchStatusTagType(status) {
      const typeMap = {
        'FULLY_MATCHED': 'success',
        'PARTIALLY_MATCHED': 'warning',
        'UNMATCHED': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getMatchStatusText(status) {
      const textMap = {
        'FULLY_MATCHED': '全部匹配',
        'PARTIALLY_MATCHED': '部分匹配',
        'UNMATCHED': '未匹配'
      }
      return textMap[status] || status
    },
    getReconciliationStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info',
        'PROCESSING': 'warning',
        'COMPLETED': 'success',
        'DIFFERENCE': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getReconciliationStatusText(status) {
      const textMap = {
        'PENDING': '待对账',
        'PROCESSING': '对账中',
        'COMPLETED': '已完成',
        'DIFFERENCE': '有差异'
      }
      return textMap[status] || status
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

    /** 格式化日期为 yyyy年MM月dd日 格式 */
    formatDateCN(date) {
      if (!date) return '-'
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}年${month}月${day}日`
    }
  }
}
</script>

<style lang="scss" scoped>
.bank-reconciliation {
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

  .reconciliation-overview {
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
          &.matched-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.unmatched-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.difference-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
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
            &.positive {
              color: #67C23A;
            }
            &.negative {
              color: #F56C6C;
            }
            &.warning {
              color: #E6A23C;
            }
          }
        }
      }
    }
  }

  .search-card, .table-card {
    margin-bottom: 20px;
  }

  .bank-name {
    font-weight: 600;
    color: #303133;
  }

  .account-number {
    font-size: 12px;
    color: #909399;
    margin-top: 2px;
  }

  .book-balance, .bank-balance {
    font-weight: 600;
    color: #303133;
  }

  .difference-amount {
    font-weight: 600;
    &.has-difference {
      color: #F56C6C;
    }
  }

  .reconciliation-detail {
    .detail-descriptions {
      margin-bottom: 20px;
    }
    
    .detail-tables {
      margin-top: 20px;
    }
    
    .difference-analysis {
      .analysis-list, .suggestion-list {
        list-style: none;
        padding: 0;
        margin: 0;
        
        li {
          padding: 8px 0;
          border-bottom: 1px solid #f0f0f0;
          
          &:last-child {
            border-bottom: none;
          }
        }
      }
      
      .reason-type {
        font-weight: 600;
        color: #303133;
        margin-right: 8px;
      }
      
      .reason-amount {
        color: #F56C6C;
        font-weight: 600;
        margin-right: 8px;
      }
      
      .reason-desc {
        color: #606266;
      }
      
      h4 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 14px;
        font-weight: 600;
      }
    }
  }

  .has-difference {
    color: #F56C6C;
    font-weight: 600;
  }
}
</style>
