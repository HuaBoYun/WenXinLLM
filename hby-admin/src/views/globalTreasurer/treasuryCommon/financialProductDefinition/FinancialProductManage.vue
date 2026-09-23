<template>
  <div class="financial-product-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-goods"></i>
            金融产品管理
          </h2>
          <p class="page-description">管理各类金融产品信息，包括产品定义、风险等级、收益配置和生命周期管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增产品
          </el-button>
          <el-button type="success" icon="el-icon-upload2" @click="handleImport">
            批量导入
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 产品概览卡片 -->
    <div class="product-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总产品数</div>
                <div class="card-value">{{ totalProducts }}</div>
                <div class="card-change">已上架产品</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">在售产品</div>
                <div class="card-value">{{ activeProducts }}</div>
                <div class="card-change positive">正常销售</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon risk-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">高风险产品</div>
                <div class="card-value">{{ highRiskProducts }}</div>
                <div class="card-change negative">需要关注</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon revenue-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">平均收益率</div>
                <div class="card-value">{{ avgYield }}%</div>
                <div class="card-change">年化收益</div>
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
          <el-form-item label="产品代码">
            <el-input
              v-model="listQuery.productCode"
              placeholder="请输入产品代码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="产品名称">
            <el-input
              v-model="listQuery.productName"
              placeholder="请输入产品名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="产品类型">
            <el-select
              v-model="listQuery.productType"
              placeholder="请选择产品类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="银行理财" value="BANK_WEALTH" />
              <el-option label="债券" value="BOND" />
              <el-option label="股票" value="EQUITY" />
              <el-option label="基金" value="FUND" />
              <el-option label="衍生品" value="DERIVATIVE" />
            </el-select>
          </el-form-item>
          <el-form-item label="发行机构">
            <el-input
              v-model="listQuery.issuer"
              placeholder="请输入发行机构"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="币种">
            <el-select
              v-model="listQuery.currencyCode"
              placeholder="请选择币种"
              clearable
              style="width: 100px;"
            >
              <el-option label="人民币" value="CNY" />
              <el-option label="美元" value="USD" />
              <el-option label="欧元" value="EUR" />
              <el-option label="日元" value="JPY" />
            </el-select>
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
            </el-select>
          </el-form-item>
          <el-form-item label="产品状态">
            <el-select
              v-model="listQuery.productStatus"
              placeholder="请选择产品状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="活跃" value="ACTIVE" />
              <el-option label="非活跃" value="INACTIVE" />
              <el-option label="暂停" value="SUSPENDED" />
              <el-option label="到期" value="MATURED" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <div class="table-title">
          <span class="title-text">金融产品列表</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="getList">刷新</el-button>
            <el-button size="small" icon="el-icon-setting" @click="handleTableSetting">设置</el-button>
          </el-button-group>
        </div>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        :row-class-name="tableRowClassName"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />

        <el-table-column label="产品代码" prop="productCode" align="center" width="150" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.productCode }}</span>
          </template>
        </el-table-column>

        <el-table-column label="产品名称" prop="productName" align="center" width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.productName }}</span>
          </template>
        </el-table-column>

        <el-table-column label="产品类型" prop="productType" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getProductTypeColor(row.productType)" size="small">
              {{ getProductTypeText(row.productType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="发行机构" prop="issuer" align="center" width="180" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.issuer }}</span>
          </template>
        </el-table-column>

        <el-table-column label="币种" prop="currencyCode" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.currencyCode }}</span>
          </template>
        </el-table-column>

        <el-table-column label="最小投资金额" prop="minInvestmentAmount" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.minInvestmentAmount | formatMoney }}</span>
          </template>
        </el-table-column>

        <el-table-column label="最大投资金额" prop="maxInvestmentAmount" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.maxInvestmentAmount | formatMoney }}</span>
          </template>
        </el-table-column>

        <el-table-column label="预期收益率" prop="expectedReturnRate" align="center" width="120">
          <template slot-scope="{row}">
            <span class="rate-value">{{ row.expectedReturnRate }}%</span>
          </template>
        </el-table-column>

        <el-table-column label="风险等级" prop="riskLevel" align="center" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getRiskLevelColor(row.riskLevel)" size="small">
              {{ getRiskLevelText(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="投资期限" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.investmentTerm }}{{ getTermUnitText(row.termUnit) }}</span>
          </template>
        </el-table-column>

        <el-table-column label="认购开始日期" prop="subscriptionStartDate" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.subscriptionStartDate | parseTime('{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>

        <el-table-column label="认购结束日期" prop="subscriptionEndDate" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.subscriptionEndDate | parseTime('{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>

        <el-table-column label="产品状态" prop="productStatus" align="center" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getProductStatusColor(row.productStatus)" size="small">
              {{ getProductStatusText(row.productStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="风险等级" prop="riskLevel" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getRiskLevelColor(row.riskLevel)" size="small">
              {{ getRiskLevelText(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="预期收益率" prop="expectedYield" align="center" width="120">
          <template slot-scope="{row}">
            <div class="yield-info">
              <span class="yield-value">{{ row.expectedYield }}%</span>
              <span class="yield-period">年化</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="投资期限" prop="investmentPeriod" align="center" width="120">
          <template slot-scope="{row}">
            <span class="period-text">{{ row.investmentPeriod || '灵活' }}</span>
          </template>
        </el-table-column>

        <el-table-column label="起购金额" prop="minAmount" align="center" width="120">
          <template slot-scope="{row}">
            <span class="amount-text">{{ formatAmount(row.minAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column label="产品状态" prop="productStatus" align="center" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getProductStatusColor(row.productStatus)" size="small">
              {{ getProductStatusText(row.productStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="状态" class-name="status-col" width="80" align="center">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>

        <el-table-column label="创建时间" prop="createTime" align="center" width="160">
          <template slot-scope="{row}">
            <span class="create-time">
              <i class="el-icon-time"></i>
              {{ formatTime(row.createTime) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="240" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">
                编辑
              </el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(row)">
                详情
              </el-button>
              <el-button type="success" size="mini" icon="el-icon-copy-document" @click="handleCopy(row)">
                复制
              </el-button>
              <el-button
                v-if="row.status !== 'deleted'"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                @click="handleDelete(row,$index)"
              >
                删除
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
      </div>
    </el-card>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'

export default {
  name: 'FinancialProductManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        productCode: undefined,
        productName: undefined,
        productType: undefined,
        issuer: undefined,
        currencyCode: undefined,
        riskLevel: undefined,
        productStatus: undefined
      },
      totalProducts: 156,
      activeProducts: 128,
      highRiskProducts: 23,
      totalRevenue: 2.85
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            productId: 1,
            productCode: 'HBY-WM-001',
            productName: '示例云稳健理财产品A',
            productType: 'BANK_WEALTH',
            issuer: '示例云银行',
            currencyCode: 'CNY',
            minInvestmentAmount: 10000,
            maxInvestmentAmount: 1000000,
            expectedReturnRate: 4.5,
            riskLevel: 'LOW',
            investmentTerm: 90,
            termUnit: 'DAY',
            subscriptionStartDate: '2024-09-01',
            subscriptionEndDate: '2024-12-31',
            valuationDate: '2024-09-01',
            maturityDate: '2024-12-30',
            earlyRedemption: 1,
            productStatus: 'ACTIVE',
            createTime: '2024-01-01 10:00:00'
          },
          {
            productId: 2,
            productCode: 'HBY-BD-002',
            productName: '企业债券投资产品B',
            productType: 'BOND',
            issuer: '中国建设银行',
            currencyCode: 'CNY',
            minInvestmentAmount: 50000,
            maxInvestmentAmount: 5000000,
            expectedReturnRate: 6.2,
            riskLevel: 'MEDIUM',
            investmentTerm: 1,
            termUnit: 'YEAR',
            subscriptionStartDate: '2024-08-01',
            subscriptionEndDate: '2024-11-30',
            valuationDate: '2024-08-01',
            maturityDate: '2025-08-01',
            earlyRedemption: 0,
            productStatus: 'ACTIVE',
            createTime: '2024-01-15 14:30:00'
          }
        ]
        this.total = 2
        this.listLoading = false
      }, 1000)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
      this.$message.info('创建功能待实现')
    },
    handleUpdate(row) {
      this.$message.info('编辑功能待实现')
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.list.splice(index, 1)
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
      })
    },
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        productCode: undefined,
        productName: undefined,
        productType: undefined,
        issuer: undefined,
        currencyCode: undefined,
        riskLevel: undefined,
        productStatus: undefined
      }
      this.getList()
    },
    getProductTypeText(type) {
      const typeMap = {
        'BANK_WEALTH': '银行理财',
        'BOND': '债券',
        'EQUITY': '股票',
        'FUND': '基金',
        'DERIVATIVE': '衍生品'
      }
      return typeMap[type] || type
    },
    getProductTypeColor(type) {
      const colorMap = {
        'BANK_WEALTH': 'success',
        'BOND': 'primary',
        'EQUITY': 'warning',
        'FUND': 'info',
        'DERIVATIVE': 'danger'
      }
      return colorMap[type] || 'default'
    },
    getRiskLevelText(level) {
      const levelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      }
      return levelMap[level] || level
    },
    getRiskLevelColor(level) {
      const colorMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return colorMap[level] || 'default'
    },
    getProductStatusText(status) {
      const statusMap = {
        'ACTIVE': '活跃',
        'INACTIVE': '非活跃',
        'SUSPENDED': '暂停',
        'MATURED': '到期'
      }
      return statusMap[status] || status
    },
    getProductStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'SUSPENDED': 'warning',
        'MATURED': 'danger'
      }
      return colorMap[status] || 'default'
    },
    getTermUnitText(unit) {
      const unitMap = {
        'DAY': '天',
        'MONTH': '月',
        'YEAR': '年'
      }
      return unitMap[unit] || unit
    },
    handleImport() {
      this.$message.info('批量导入功能待实现')
    },
    handleExport() {
      this.$message.info('导出功能待实现')
    },
    handleView(row) {
      this.$message.info('查看详情功能待实现')
    },
    handleCopy(row) {
      this.$message.info('复制功能待实现')
    },
    handleStatusChange(row) {
      this.$message.success('状态更新成功')
    },
    handleSelectionChange(selection) {
      console.log('选择变化:', selection)
    },
    handleTableSetting() {
      this.$message.info('表格设置功能待实现')
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.status === 0) {
        return 'warning-row'
      }
      return ''
    },
    getProductIcon(type) {
      const iconMap = {
        'WEALTH': 'el-icon-money',
        'FUND': 'el-icon-data-line',
        'INSURANCE': 'el-icon-umbrella',
        'BOND': 'el-icon-document'
      }
      return iconMap[type] || 'el-icon-goods'
    },
    getProductTypeColor(type) {
      const colorMap = {
        'WEALTH': 'success',
        'FUND': 'primary',
        'INSURANCE': 'warning',
        'BOND': 'info'
      }
      return colorMap[type] || ''
    },
    getProductTypeText(type) {
      const textMap = {
        'WEALTH': '理财产品',
        'FUND': '基金产品',
        'INSURANCE': '保险产品',
        'BOND': '债券产品'
      }
      return textMap[type] || type
    },
    getRiskLevelColor(level) {
      const colorMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return colorMap[level] || ''
    },
    getRiskLevelText(level) {
      const textMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      }
      return textMap[level] || level
    },
    formatTime(time) {
      return time || '2023-12-01 10:00:00'
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-product-manage {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;

  .page-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 12px;
    padding: 24px;
    margin-bottom: 20px;
    color: white;
    box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        .page-title {
          font-size: 24px;
          font-weight: 600;
          margin: 0 0 8px 0;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            font-size: 28px;
          }
        }

        .page-description {
          font-size: 14px;
          opacity: 0.9;
          margin: 0;
        }
      }

      .header-right {
        display: flex;
        gap: 12px;

        .el-button {
          border: 1px solid rgba(255, 255, 255, 0.3);
          background: rgba(255, 255, 255, 0.1);
          color: white;

          &:hover {
            background: rgba(255, 255, 255, 0.2);
            border-color: rgba(255, 255, 255, 0.5);
          }
        }
      }
    }
  }

  .product-overview {
    margin-bottom: 20px;

    .overview-card {
      border: none;
      border-radius: 12px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
      }

      .card-content {
        display: flex;
        align-items: center;
        padding: 20px;

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 12px;
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

          &.active-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &.high-risk-icon {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
          }

          &.revenue-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 14px;
            color: #666;
            margin-bottom: 8px;
          }

          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #333;
            margin-bottom: 4px;
          }

          .card-change {
            font-size: 12px;
            color: #999;

            &.positive {
              color: #67c23a;
            }

            &.negative {
              color: #f56c6c;
            }
          }
        }
      }
    }
  }

  .search-card, .table-card {
    border: none;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;

    .search-form {
      padding: 20px;

      .demo-form-inline {
        .el-form-item {
          margin-bottom: 0;
        }
      }
    }

    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px 20px 0;

      .table-title {
        .title-text {
          font-size: 16px;
          font-weight: 600;
          color: #333;
        }

        .title-count {
          font-size: 14px;
          color: #666;
          margin-left: 8px;
        }
      }
    }

    .el-table {
      margin: 20px;
      width: calc(100% - 40px);

      .product-info {
        display: flex;
        align-items: center;

        .product-avatar {
          width: 40px;
          height: 40px;
          border-radius: 8px;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;

          i {
            font-size: 18px;
            color: white;
          }
        }

        .product-details {
          .product-name {
            font-weight: 600;
            color: #333;
            margin-bottom: 4px;
          }

          .product-code {
            font-size: 12px;
            color: #999;
          }
        }
      }

      .create-time {
        display: flex;
        align-items: center;
        color: #666;

        i {
          margin-right: 4px;
        }
      }
    }

    .pagination-wrapper {
      padding: 20px;
      text-align: right;
    }
  }
}

::v-deep .el-table .warning-row {
  background: #fdf6ec;
}

::v-deep .el-table .success-row {
  background: #f0f9ff;
}
</style>
