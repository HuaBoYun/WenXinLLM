<template>
  <div class="financial-statement-tab">
    <!-- 查询表单 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px" class="mb-20">
      <el-form-item label="报表类型">
        <el-select v-model="queryForm.statementType" placeholder="请选择报表类型" clearable style="width: 150px;">
          <el-option label="资产负债表" value="BALANCE_SHEET"></el-option>
          <el-option label="利润表" value="INCOME_STATEMENT"></el-option>
          <el-option label="现金流量表" value="CASH_FLOW"></el-option>
          <el-option label="所有者权益变动表" value="EQUITY_CHANGE"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="报告期间">
        <el-select v-model="queryForm.reportPeriod" placeholder="请选择报告期间" clearable style="width: 150px;">
          <el-option label="月报" value="MONTHLY"></el-option>
          <el-option label="季报" value="QUARTERLY"></el-option>
          <el-option label="半年报" value="SEMI_ANNUAL"></el-option>
          <el-option label="年报" value="ANNUAL"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态">
        <el-select v-model="queryForm.auditStatus" placeholder="请选择审核状态" clearable style="width: 150px;">
          <el-option label="草稿" value="DRAFT"></el-option>
          <el-option label="已提交" value="SUBMITTED"></el-option>
          <el-option label="已审核" value="AUDITED"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="报告年度">
        <el-date-picker
          v-model="queryForm.reportYear"
          type="year"
          placeholder="选择年份"
          style="width: 120px;">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
        <el-button type="success" @click="handleCreate" icon="el-icon-plus">新建报表</el-button>
        <el-button type="warning" @click="handleImport" icon="el-icon-upload2">导入报表</el-button>
      </el-form-item>
    </el-form>

    <!-- 财务概览统计 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="financial-card">
          <div class="financial-content">
            <div class="financial-icon revenue">
              <i class="el-icon-money"></i>
            </div>
            <div class="financial-info">
              <div class="financial-amount">{{ formatAmount(financialStats.totalRevenue) }}</div>
              <div class="financial-label">营业收入</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="financial-card">
          <div class="financial-content">
            <div class="financial-icon profit">
              <i class="el-icon-coin"></i>
            </div>
            <div class="financial-info">
              <div class="financial-amount">{{ formatAmount(financialStats.netProfit) }}</div>
              <div class="financial-label">净利润</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="financial-card">
          <div class="financial-content">
            <div class="financial-icon assets">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="financial-info">
              <div class="financial-amount">{{ formatAmount(financialStats.totalAssets) }}</div>
              <div class="financial-label">总资产</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="financial-card">
          <div class="financial-content">
            <div class="financial-icon equity">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="financial-info">
              <div class="financial-amount">{{ formatAmount(financialStats.netAssets) }}</div>
              <div class="financial-label">所有者权益</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @selection-change="handleSelectionChange"
      @sort-change="handleSortChange"
      stripe
      border
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="reportType" label="报表类型" width="140" align="center">
        <template slot-scope="scope">
          <el-tag :type="getStatementTypeTag(scope.row.reportType)">
            {{ getStatementTypeText(scope.row.reportType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reportPeriod" label="报告期间" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="getReportPeriodTag(scope.row.reportPeriod)" size="mini">
            {{ getReportPeriodText(scope.row.reportPeriod) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reportYear" label="报告年度" width="100" align="center"></el-table-column>
      <el-table-column prop="operatingRevenue" label="营业收入" width="120" align="right">
        <template slot-scope="scope">
          <span class="amount-text">{{ formatAmount(scope.row.operatingRevenue) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="netProfit" label="净利润" width="120" align="right">
        <template slot-scope="scope">
          <span :class="getProfitClass(scope.row.netProfit)">{{ formatAmount(scope.row.netProfit) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="totalAssets" label="总资产" width="120" align="right">
        <template slot-scope="scope">
          <span class="amount-text">{{ formatAmount(scope.row.totalAssets) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="enterpriseName" label="企业名称" width="140" align="center"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" align="center"></el-table-column>
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="getAuditStatusTag(scope.row.status)">
            {{ getAuditStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)" icon="el-icon-edit" v-if="scope.row.status === 'DRAFT' || scope.row.status === 'SUBMITTED'">编辑</el-button>
          <el-button size="mini" type="success" @click="handleSubmit(scope.row)" icon="el-icon-check" v-if="scope.row.status === 'DRAFT'">提交</el-button>
          <el-dropdown @command="handleCommand" style="margin-left: 10px;">
            <el-button size="mini" type="info">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="{action: 'compare', row: scope.row}" icon="el-icon-data-analysis">对比分析</el-dropdown-item>
              <el-dropdown-item :command="{action: 'export', row: scope.row}" icon="el-icon-download">导出报表</el-dropdown-item>
              <el-dropdown-item :command="{action: 'print', row: scope.row}" icon="el-icon-printer">打印报表</el-dropdown-item>
              <el-dropdown-item :command="{action: 'copy', row: scope.row}" icon="el-icon-document-copy">复制报表</el-dropdown-item>
              <el-dropdown-item :command="{action: 'history', row: scope.row}" icon="el-icon-time">版本历史</el-dropdown-item>
              <el-dropdown-item :command="{action: 'delete', row: scope.row}" icon="el-icon-delete" divided v-if="scope.row.status === 'DRAFT'">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 财务报表对话框 -->
    <FinancialStatementDialog
      :visible.sync="statementDialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      :enterprise-id="enterpriseId"
      :enterprise-name="enterpriseName"
      @refresh="getList"
    />

    <!-- 报表详情对话框 -->
    <StatementDetailDialog
      :visible.sync="detailDialogVisible"
      :statement-data="currentRow"
    />

    <!-- 报表对比对话框 -->
    <StatementCompareDialog
      :visible.sync="compareDialogVisible"
      :statement-data="currentRow"
    />

    <!-- 导入报表对话框 -->
    <ImportStatementDialog
      :visible.sync="importDialogVisible"
      :enterprise-id="enterpriseId"
      @refresh="getList"
    />

    <!-- 版本历史对话框 -->
    <el-dialog title="版本历史" :visible.sync="historyDialogVisible" width="600px" append-to-body>
      <div v-loading="historyLoading">
        <el-timeline v-if="historyList.length > 0">
          <el-timeline-item
            v-for="(item, index) in historyList"
            :key="index"
            :timestamp="item.time"
            :type="getHistoryItemType(item.action)"
            :icon="getHistoryItemIcon(item.action)"
            placement="top"
          >
            <el-card shadow="never" class="history-card">
              <h4 style="margin: 0 0 6px 0;">{{ item.actionText }}</h4>
              <p style="margin: 0; color: #909399; font-size: 13px;">{{ item.description }}</p>
              <el-tag size="mini" :type="getAuditStatusTag(item.status)" style="margin-top: 6px;">
                {{ getAuditStatusText(item.status) }}
              </el-tag>
            </el-card>
          </el-timeline-item>
        </el-timeline>
        <el-empty v-else description="暂无版本历史记录"></el-empty>
      </div>
      <span slot="footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getFinancialStatementList,
  deleteFinancialStatement,
  submitFinancialStatement,
  exportFinancialStatement,
  getFinancialStatistics,
  getStatementHistory
} from '@/api/enterprise/financial'
import Pagination from '@/components/Pagination'
import FinancialStatementDialog from './FinancialStatementDialog'
import StatementDetailDialog from './StatementDetailDialog'
import StatementCompareDialog from './StatementCompareDialog'
import ImportStatementDialog from './ImportStatementDialog'

export default {
  name: 'FinancialStatementTab',
  components: {
    Pagination,
    FinancialStatementDialog,
    StatementDetailDialog,
    StatementCompareDialog,
    ImportStatementDialog
  },
  props: {
    enterpriseId: {
      type: String,
      default: ''
    },
    enterpriseName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      multipleSelection: [],
      financialStats: {},
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        statementType: '',
        reportPeriod: '',
        auditStatus: '',
        reportYear: null
      },
      
      // 对话框状态
      statementDialogVisible: false,
      detailDialogVisible: false,
      compareDialogVisible: false,
      importDialogVisible: false,
      historyDialogVisible: false,
      dialogType: 'add',
      currentRow: {},

      // 版本历史
      historyLoading: false,
      historyList: []
    }
  },
  watch: {
    enterpriseId: {
      handler(val) {
        if (val) {
          this.getList()
          this.loadFinancialStats()
        }
      },
      immediate: true
    }
  },
  methods: {
    // 获取列表数据
    getList() {
      this.loading = true
      const params = {
        ...this.queryForm,
        enterpriseId: this.enterpriseId
      }
      // 后端字段名是 reportType，前端查询表单用的是 statementType
      if (params.statementType) {
        params.reportType = params.statementType
      }
      delete params.statementType
      // 后端字段名是 status，前端查询表单用的是 auditStatus
      if (params.auditStatus) {
        params.status = params.auditStatus
      }
      delete params.auditStatus
      // reportYear 需要格式化为字符串年份
      if (params.reportYear instanceof Date) {
        params.reportYear = params.reportYear.getFullYear().toString()
      } else if (params.reportYear && typeof params.reportYear === 'string' && params.reportYear.length > 4) {
        params.reportYear = params.reportYear.substring(0, 4)
      }

      getFinancialStatementList(params).then(response => {
        // 后端 PageResult 返回 tlist/totalRecord，兼容 records/total
        const data = response.data || {}
        this.tableData = data.tlist || data.records || []
        this.total = data.totalRecord || data.total || 0
        this.loading = false
      }).catch((error) => {
        this.loading = false
        console.error('查询财务报表列表失败:', error)
        this.$message.error('查询财务报表列表失败，请检查网络连接')
      })
    },

    // 加载财务统计
    loadFinancialStats() {
      getFinancialStatistics({ enterpriseId: this.enterpriseId }).then(response => {
        const data = response.data || {}
        // 后端statistics返回: totalRevenue, netProfit, totalAssets, totalLiabilities, netAssets, liabilityRatio, roe, roa, revenueGrowth, profitGrowth
        this.financialStats = {
          totalRevenue: data.totalRevenue || 0,
          netProfit: data.netProfit || 0,
          totalAssets: data.totalAssets || 0,
          netAssets: data.netAssets || 0
        }
      }).catch(() => {
        this.financialStats = {}
      })
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    // 重置查询
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        pageNumber: 1,
        pageSize: 10,
        statementType: '',
        reportPeriod: '',
        auditStatus: '',
        reportYear: null
      }
      this.getList()
    },

    // 新建报表
    handleCreate() {
      this.currentRow = {}
      this.dialogType = 'add'
      this.statementDialogVisible = true
    },

    // 导入报表
    handleImport() {
      this.importDialogVisible = true
    },

    // 查看
    handleView(row) {
      this.currentRow = { ...row }
      this.detailDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentRow = { ...row }
      this.dialogType = 'edit'
      this.statementDialogVisible = true
    },

    // 提交审核
    async handleSubmit(row) {
      this.$confirm('确认提交该财务报表进行审核？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await submitFinancialStatement({ id: row.id })
          this.$message.success('提交成功')
          this.getList()
          this.$emit('refresh')
        } catch (error) {
          this.$message.error('提交失败')
        }
      })
    },

    // 下拉菜单命令处理
    handleCommand(command) {
      const { action, row } = command
      this.currentRow = { ...row }

      switch (action) {
        case 'compare':
          this.compareDialogVisible = true
          break
        case 'export':
          this.handleExport(row)
          break
        case 'print':
          this.handlePrint(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'history':
          this.handleHistory(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 导出报表
    async handleExport(row) {
      try {
        const response = await exportFinancialStatement({ id: row.id, enterpriseId: this.enterpriseId })
        // 后端返回blob，触发文件下载
        if (response && response.size > 0) {
          const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const typeText = this.getStatementTypeText(row.reportType)
          const fileName = typeText + '_' + (row.reportYear || '') + '_' + (row.enterpriseName || '') + '.xlsx'
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = fileName
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          URL.revokeObjectURL(link.href)
          this.$message.success('导出成功')
        } else {
          this.$message.warning('导出数据为空')
        }
      } catch (error) {
        this.$message.error('导出失败')
      }
    },

    // 打印报表
    handlePrint(row) {
      const typeText = this.getStatementTypeText(row.reportType)
      const periodText = this.getReportPeriodText(row.reportPeriod)
      const statusText = this.getAuditStatusText(row.status)
      // 预先计算所有金额，避免模板字符串中this引用问题
      const revenue = this.formatAmount(row.operatingRevenue)
      const profit = this.formatAmount(row.netProfit)
      const assets = this.formatAmount(row.totalAssets)
      const liabilities = this.formatAmount(row.totalLiabilities)
      const netAssets = this.formatAmount(row.netAssets)
      const cash = this.formatAmount(row.cashFlow)
      const enterpriseName = row.enterpriseName || '-'
      const reportYear = row.reportYear || '-'
      const createTime = row.createTime || '-'

      const printContent = `<!DOCTYPE html>
<html>
<head>
  <title>${typeText} - ${reportYear}${periodText}</title>
  <style>
    body { font-family: "Microsoft YaHei", sans-serif; padding: 40px; color: #333; }
    h1 { text-align: center; font-size: 22px; margin-bottom: 8px; }
    .subtitle { text-align: center; color: #666; margin-bottom: 30px; font-size: 14px; }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 10px 14px; text-align: left; font-size: 14px; }
    th { background-color: #f5f7fa; font-weight: bold; width: 140px; }
    .amount { text-align: right; font-family: monospace; }
    .footer { margin-top: 40px; font-size: 12px; color: #999; text-align: center; }
    @media print { body { padding: 20px; } }
  </style>
</head>
<body>
  <h1>${typeText}</h1>
  <p class="subtitle">${enterpriseName} | ${reportYear}年${periodText} | 状态：${statusText}</p>
  <table>
    <tr><th>企业名称</th><td>${enterpriseName}</td></tr>
    <tr><th>报表类型</th><td>${typeText}</td></tr>
    <tr><th>报告期间</th><td>${periodText}</td></tr>
    <tr><th>报告年度</th><td>${reportYear}</td></tr>
    <tr><th>营业收入</th><td class="amount">${revenue}</td></tr>
    <tr><th>净利润</th><td class="amount">${profit}</td></tr>
    <tr><th>总资产</th><td class="amount">${assets}</td></tr>
    <tr><th>总负债</th><td class="amount">${liabilities}</td></tr>
    <tr><th>净资产</th><td class="amount">${netAssets}</td></tr>
    <tr><th>现金流量</th><td class="amount">${cash}</td></tr>
    <tr><th>审核状态</th><td>${statusText}</td></tr>
    <tr><th>创建时间</th><td>${createTime}</td></tr>
  </table>
  <p class="footer">打印时间：${new Date().toLocaleString('zh-CN')}</p>
</body>
</html>`
      const printWindow = window.open('', '_blank')
      if (printWindow) {
        printWindow.document.write(printContent)
        printWindow.document.close()
        setTimeout(() => {
          printWindow.print()
        }, 300)
      } else {
        this.$message.warning('无法打开打印窗口，请检查浏览器是否阻止了弹出窗口')
      }
    },

    // 版本历史
    async handleHistory(row) {
      this.historyDialogVisible = true
      this.historyLoading = true
      this.historyList = []
      try {
        const response = await getStatementHistory(row.id)
        this.historyList = response.data || []
      } catch (error) {
        this.$message.error('获取版本历史失败')
      } finally {
        this.historyLoading = false
      }
    },

    // 获取历史记录时间线节点类型
    getHistoryItemType(action) {
      const typeMap = {
        'CREATE': 'primary',
        'UPDATE': 'warning',
        'SUBMIT': 'info',
        'AUDIT': 'success'
      }
      return typeMap[action] || 'info'
    },

    // 获取历史记录时间线节点图标
    getHistoryItemIcon(action) {
      const iconMap = {
        'CREATE': 'el-icon-document-add',
        'UPDATE': 'el-icon-edit',
        'SUBMIT': 'el-icon-s-promotion',
        'AUDIT': 'el-icon-circle-check'
      }
      return iconMap[action] || 'el-icon-info'
    },

    // 复制报表
    handleCopy(row) {
      this.currentRow = { ...row, id: '', status: 'DRAFT' }
      this.dialogType = 'add'
      this.statementDialogVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该财务报表？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteFinancialStatement(row.id)
          this.$message.success('删除成功')
          this.getList()
          this.$emit('refresh')
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },

    // 多选变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 排序变化
    handleSortChange({ column, prop, order }) {
      this.queryForm.orderBy = prop
      this.queryForm.orderDirection = order === 'ascending' ? 'ASC' : 'DESC'
      this.getList()
    },

    // 格式化金额（后端返回元，转换为万元显示）
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0'
      if (amount === 0) return '0'
      const num = typeof amount === 'string' ? parseFloat(amount) : amount
      if (isNaN(num)) return '0'
      return (num / 10000).toFixed(2) + '万'
    },

    // 获取利润样式类
    getProfitClass(profit) {
      if (profit > 0) return 'profit-positive'
      if (profit < 0) return 'profit-negative'
      return 'profit-zero'
    },

    // 获取报表类型标签
    getStatementTypeTag(type) {
      const tagMap = {
        'BALANCE_SHEET': 'primary',
        'INCOME_STATEMENT': 'success',
        'CASH_FLOW': 'warning',
        'EQUITY_CHANGE': 'info'
      }
      return tagMap[type] || 'info'
    },

    // 获取报表类型文本
    getStatementTypeText(type) {
      const textMap = {
        'BALANCE_SHEET': '资产负债表',
        'INCOME_STATEMENT': '利润表',
        'CASH_FLOW': '现金流量表',
        'EQUITY_CHANGE': '所有者权益变动表'
      }
      return textMap[type] || type
    },

    // 获取报告期间标签
    getReportPeriodTag(period) {
      const tagMap = {
        'MONTHLY': 'info',
        'QUARTERLY': 'primary',
        'SEMI_ANNUAL': 'warning',
        'ANNUAL': 'success'
      }
      return tagMap[period] || 'info'
    },

    // 获取报告期间文本
    getReportPeriodText(period) {
      const textMap = {
        'MONTHLY': '月报',
        'QUARTERLY': '季报',
        'SEMI_ANNUAL': '半年报',
        'ANNUAL': '年报'
      }
      return textMap[period] || period
    },

    // 获取审核状态标签
    getAuditStatusTag(status) {
      const tagMap = {
        'DRAFT': 'info',
        'SUBMITTED': 'warning',
        'PENDING': 'warning',
        'AUDITED': 'primary',
        'PUBLISHED': 'success'
      }
      return tagMap[status] || 'info'
    },

    // 获取审核状态文本
    getAuditStatusText(status) {
      const textMap = {
        'DRAFT': '草稿',
        'SUBMITTED': '已提交',
        'PENDING': '待审核',
        'AUDITED': '已审核',
        'PUBLISHED': '已发布'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style scoped>
.financial-statement-tab {
  padding: 20px 0;
}

.mb-20 {
  margin-bottom: 20px;
}

.financial-card {
  height: 100px;
}

.financial-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.financial-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  margin-right: 15px;
}

.financial-icon.revenue {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.financial-icon.profit {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.financial-icon.assets {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.financial-icon.equity {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.financial-info {
  flex: 1;
}

.financial-amount {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 5px;
}

.financial-label {
  font-size: 12px;
  color: #909399;
}

.amount-text {
  color: #303133;
  font-weight: 500;
}

.profit-positive {
  color: #67C23A;
  font-weight: bold;
}

.profit-negative {
  color: #F56C6C;
  font-weight: bold;
}

.profit-zero {
  color: #909399;
}

.history-card {
  padding: 0;
}

.history-card ::v-deep .el-card__body {
  padding: 12px 16px;
}
</style>
