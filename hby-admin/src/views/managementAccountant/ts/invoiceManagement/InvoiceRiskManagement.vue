<template>
  <div class="invoice-risk-management">
    <!-- 页面标题和操作按钮 -->
    <div class="page-header">
      <h3>发票风险管理</h3>
      <div class="header-actions">
        <el-button type="primary" size="small" @click="handleRiskAnalysis">
          <i class="el-icon-data-analysis"></i> 风险分析
        </el-button>
        <el-button type="warning" size="small" @click="handleRiskAlert">
          <i class="el-icon-warning"></i> 风险预警
        </el-button>
        <el-button type="info" size="small" @click="refreshData">
          <i class="el-icon-refresh"></i> 刷新
        </el-button>
      </div>
    </div>

    <!-- 风险概览 -->
    <div class="risk-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="risk-card high-risk">
            <div class="risk-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="risk-content">
              <div class="risk-number">{{ riskData.highRisk || 0 }}</div>
              <div class="risk-label">高风险发票</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="risk-card medium-risk">
            <div class="risk-icon">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="risk-content">
              <div class="risk-number">{{ riskData.mediumRisk || 0 }}</div>
              <div class="risk-label">中风险发票</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="risk-card low-risk">
            <div class="risk-icon">
              <i class="el-icon-info"></i>
            </div>
            <div class="risk-content">
              <div class="risk-number">{{ riskData.lowRisk || 0 }}</div>
              <div class="risk-label">低风险发票</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="risk-card safe">
            <div class="risk-icon">
              <i class="el-icon-check"></i>
            </div>
            <div class="risk-content">
              <div class="risk-number">{{ riskData.safe || 0 }}</div>
              <div class="risk-label">安全发票</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 风险类型分析 -->
    <div class="risk-analysis-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="analysis-card">
            <h4>风险类型分布</h4>
            <div class="risk-types">
              <div class="risk-type-item" v-for="item in riskTypes" :key="item.type">
                <div class="type-info">
                  <span class="type-name">{{ item.name }}</span>
                  <span class="type-count">{{ item.count }}</span>
                </div>
                <el-progress
                  :percentage="item.percentage"
                  :status="getRiskTypeStatus(item.type)"
                  :stroke-width="8"
                />
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="analysis-card">
            <h4>风险趋势分析</h4>
            <div id="riskTrendChart" style="height: 300px;">
              <div class="chart-placeholder">
                <i class="el-icon-data-line"></i>
                <p>风险趋势图表开发中...</p>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 风险发票列表 -->
    <div class="risk-table-section">
      <div class="table-header">
        <h4>风险发票列表</h4>
        <div class="table-filters">
          <el-select v-model="filterRiskLevel" placeholder="风险等级" size="small" @change="handleFilter">
            <el-option label="全部" value="" />
            <el-option label="高风险" value="HIGH" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="低风险" value="LOW" />
          </el-select>
          <el-select v-model="filterRiskType" placeholder="风险类型" size="small" @change="handleFilter">
            <el-option label="全部" value="" />
            <el-option label="虚假发票" value="FAKE" />
            <el-option label="重复发票" value="DUPLICATE" />
            <el-option label="异常金额" value="ABNORMAL_AMOUNT" />
            <el-option label="过期发票" value="EXPIRED" />
          </el-select>
        </div>
      </div>
      
      <el-table :data="riskTableData" v-loading="loading" border stripe>
        <el-table-column prop="invoiceCode" label="发票代码" width="120" />
        <el-table-column prop="invoiceNumber" label="发票号码" width="120" />
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelTagType(scope.row.riskLevel)" size="mini">
              {{ formatRiskLevel(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskType" label="风险类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getRiskTypeTagType(scope.row.riskType)" size="mini">
              {{ formatRiskType(scope.row.riskType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskScore" label="风险评分" width="100">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.riskScore"
              :status="getRiskScoreStatus(scope.row.riskScore)"
              :stroke-width="6"
            />
          </template>
        </el-table-column>
        <el-table-column prop="invoiceAmount" label="发票金额" width="120">
          <template slot-scope="scope">
            ¥{{ formatAmount(scope.row.invoiceAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="issueDate" label="开票日期" width="120" />
        <el-table-column prop="riskReason" label="风险原因" min-width="200" show-overflow-tooltip />
        <el-table-column prop="detectTime" label="检测时间" width="150" />
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="handleRiskDetail(scope.row)">
              详情
            </el-button>
            <el-button type="warning" size="mini" @click="handleRiskHandle(scope.row)">
              处理
            </el-button>
            <el-button type="success" size="mini" @click="handleRiskIgnore(scope.row)">
              忽略
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 风险处理对话框 -->
    <el-dialog
      title="风险处理"
      :visible.sync="handleDialogVisible"
      width="600px"
      @closed="handleDialogClosed"
    >
      <el-form :model="handleForm" :rules="handleRules" ref="handleForm" label-width="100px">
        <el-form-item label="处理方式" prop="handleType">
          <el-select v-model="handleForm.handleType" placeholder="请选择处理方式">
            <el-option label="标记为安全" value="MARK_SAFE" />
            <el-option label="加入黑名单" value="BLACKLIST" />
            <el-option label="人工审核" value="MANUAL_REVIEW" />
            <el-option label="系统忽略" value="IGNORE" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理说明" prop="handleRemark">
          <el-input
            v-model="handleForm.handleRemark"
            type="textarea"
            :rows="4"
            placeholder="请输入处理说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmRiskHandle">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'InvoiceRiskManagement',
  data() {
    return {
      loading: false,
      handleDialogVisible: false,
      filterRiskLevel: '',
      filterRiskType: '',
      riskData: {
        highRisk: 23,
        mediumRisk: 67,
        lowRisk: 145,
        safe: 1021
      },
      riskTypes: [
        { type: 'FAKE', name: '虚假发票', count: 23, percentage: 35 },
        { type: 'DUPLICATE', name: '重复发票', count: 18, percentage: 27 },
        { type: 'ABNORMAL_AMOUNT', name: '异常金额', count: 15, percentage: 23 },
        { type: 'EXPIRED', name: '过期发票', count: 10, percentage: 15 }
      ],
      riskTableData: [
        {
          id: 1,
          invoiceCode: '144031909110',
          invoiceNumber: '19134556',
          riskLevel: 'HIGH',
          riskType: 'FAKE',
          riskScore: 95,
          invoiceAmount: 11800.00,
          issueDate: '2024-01-15',
          riskReason: '发票代码与税务局记录不符，疑似虚假发票',
          detectTime: '2024-01-15 14:30:25'
        },
        {
          id: 2,
          invoiceCode: '144031909111',
          invoiceNumber: '19134557',
          riskLevel: 'MEDIUM',
          riskType: 'DUPLICATE',
          riskScore: 75,
          invoiceAmount: 5600.00,
          issueDate: '2024-01-15',
          riskReason: '发现相同发票号码的重复记录',
          detectTime: '2024-01-15 15:20:18'
        },
        {
          id: 3,
          invoiceCode: '144031909112',
          invoiceNumber: '19134558',
          riskLevel: 'LOW',
          riskType: 'ABNORMAL_AMOUNT',
          riskScore: 45,
          invoiceAmount: 2300.00,
          issueDate: '2024-01-14',
          riskReason: '发票金额与历史交易模式存在偏差',
          detectTime: '2024-01-14 16:45:32'
        }
      ],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 235
      },
      handleForm: {
        handleType: '',
        handleRemark: ''
      },
      handleRules: {
        handleType: [
          { required: true, message: '请选择处理方式', trigger: 'change' }
        ],
        handleRemark: [
          { required: true, message: '请输入处理说明', trigger: 'blur' }
        ]
      },
      currentRiskRow: null
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      // 模拟API调用
      setTimeout(() => {
        this.loading = false
      }, 1000)
    },
    refreshData() {
      this.loadData()
      this.$message.success('数据刷新成功')
    },
    handleFilter() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },
    handleRiskAnalysis() {
      this.$message.info('风险分析功能开发中...')
    },
    handleRiskAlert() {
      this.$message.info('风险预警功能开发中...')
    },
    handleRiskDetail(row) {
      this.$message.info(`查看风险详情: ${row.invoiceNumber}`)
    },
    handleRiskHandle(row) {
      this.currentRiskRow = row
      this.handleDialogVisible = true
    },
    handleRiskIgnore(row) {
      this.$confirm('确认忽略此风险发票?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('风险已忽略')
        this.loadData()
      })
    },
    handleConfirmRiskHandle() {
      this.$refs.handleForm.validate((valid) => {
        if (valid) {
          this.$message.success('风险处理成功')
          this.handleDialogVisible = false
          this.loadData()
        }
      })
    },
    handleDialogClosed() {
      this.$refs.handleForm.resetFields()
      this.currentRiskRow = null
    },
    formatRiskLevel(level) {
      const levelMap = {
        'HIGH': '高风险',
        'MEDIUM': '中风险',
        'LOW': '低风险'
      }
      return levelMap[level] || level
    },
    getRiskLevelTagType(level) {
      const typeMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return typeMap[level] || ''
    },
    formatRiskType(type) {
      const typeMap = {
        'FAKE': '虚假发票',
        'DUPLICATE': '重复发票',
        'ABNORMAL_AMOUNT': '异常金额',
        'EXPIRED': '过期发票'
      }
      return typeMap[type] || type
    },
    getRiskTypeTagType(type) {
      const typeMap = {
        'FAKE': 'danger',
        'DUPLICATE': 'warning',
        'ABNORMAL_AMOUNT': 'info',
        'EXPIRED': 'success'
      }
      return typeMap[type] || ''
    },
    getRiskScoreStatus(score) {
      if (score >= 80) return 'exception'
      if (score >= 60) return 'warning'
      return 'success'
    },
    getRiskTypeStatus(type) {
      const statusMap = {
        'FAKE': 'exception',
        'DUPLICATE': 'warning',
        'ABNORMAL_AMOUNT': 'warning',
        'EXPIRED': 'success'
      }
      return statusMap[type] || ''
    },
    formatAmount(amount) {
      return amount ? amount.toLocaleString() : '0'
    }
  }
}
</script>

<style lang="scss" scoped>
.invoice-risk-management {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  h3 {
    margin: 0;
    color: #303133;
  }

  .header-actions {
    display: flex;
    gap: 10px;
  }
}

.risk-overview {
  margin-bottom: 20px;
}

.risk-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;

  &:hover {
    transform: translateY(-2px);
  }

  &.high-risk {
    border-left: 4px solid #f56c6c;
  }

  &.medium-risk {
    border-left: 4px solid #e6a23c;
  }

  &.low-risk {
    border-left: 4px solid #409eff;
  }

  &.safe {
    border-left: 4px solid #67c23a;
  }
}

.risk-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;

  i {
    font-size: 20px;
  }

  .high-risk & {
    background: #f56c6c;
    color: white;
  }

  .medium-risk & {
    background: #e6a23c;
    color: white;
  }

  .low-risk & {
    background: #409eff;
    color: white;
  }

  .safe & {
    background: #67c23a;
    color: white;
  }
}

.risk-content {
  flex: 1;
}

.risk-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.risk-label {
  font-size: 14px;
  color: #909399;
}

.risk-analysis-section {
  margin-bottom: 20px;
}

.analysis-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  height: 400px;

  h4 {
    margin: 0 0 20px 0;
    color: #303133;
  }
}

.risk-types {
  .risk-type-item {
    margin-bottom: 20px;

    .type-info {
      display: flex;
      justify-content: space-between;
      margin-bottom: 8px;

      .type-name {
        color: #303133;
        font-weight: 500;
      }

      .type-count {
        color: #909399;
        font-size: 14px;
      }
    }
  }
}

.chart-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;

  i {
    font-size: 48px;
    margin-bottom: 10px;
  }
}

.risk-table-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  h4 {
    margin: 0;
    color: #303133;
  }

  .table-filters {
    display: flex;
    gap: 10px;
  }
}

.pagination-section {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
