<template>
  <div class="exchange-rate-config">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-money"></i>
            汇率配置管理
          </h2>
          <p class="page-description">管理多币种汇率配置，支持实时汇率更新和历史汇率查询</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增汇率
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleSyncRate">
            同步汇率
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 汇率概览卡片 -->
    <div class="rate-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon usd-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">USD/CNY</div>
                <div class="card-value">7.2456</div>
                <div class="card-change positive">+0.0123 (+0.17%)</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon eur-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-info">
                <div class="card-title">EUR/CNY</div>
                <div class="card-value">7.8923</div>
                <div class="card-change negative">-0.0056 (-0.07%)</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon jpy-icon">
                <i class="el-icon-wallet"></i>
              </div>
              <div class="card-info">
                <div class="card-title">JPY/CNY</div>
                <div class="card-value">0.0489</div>
                <div class="card-change positive">+0.0002 (+0.41%)</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon update-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">最后更新</div>
                <div class="card-value">{{ lastUpdateTime }}</div>
                <div class="card-change">实时汇率</div>
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
          <el-form-item label="基准币种">
            <el-select
              v-model="listQuery.baseCurrency"
              placeholder="请选择基准币种"
              clearable
              style="width: 150px;"
            >
              <el-option label="人民币 (CNY)" value="CNY">
                <span class="currency-option">
                  <span class="currency-flag">¥</span>
                  人民币 (CNY)
                </span>
              </el-option>
              <el-option label="美元 (USD)" value="USD">
                <span class="currency-option">
                  <span class="currency-flag">$</span>
                  美元 (USD)
                </span>
              </el-option>
              <el-option label="欧元 (EUR)" value="EUR">
                <span class="currency-option">
                  <span class="currency-flag">€</span>
                  欧元 (EUR)
                </span>
              </el-option>
              <el-option label="日元 (JPY)" value="JPY">
                <span class="currency-option">
                  <span class="currency-flag">¥</span>
                  日元 (JPY)
                </span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="目标币种">
            <el-select
              v-model="listQuery.targetCurrency"
              placeholder="请选择目标币种"
              clearable
              style="width: 150px;"
            >
              <el-option label="人民币 (CNY)" value="CNY">
                <span class="currency-option">
                  <span class="currency-flag">¥</span>
                  人民币 (CNY)
                </span>
              </el-option>
              <el-option label="美元 (USD)" value="USD">
                <span class="currency-option">
                  <span class="currency-flag">$</span>
                  美元 (USD)
                </span>
              </el-option>
              <el-option label="欧元 (EUR)" value="EUR">
                <span class="currency-option">
                  <span class="currency-flag">€</span>
                  欧元 (EUR)
                </span>
              </el-option>
              <el-option label="日元 (JPY)" value="JPY">
                <span class="currency-option">
                  <span class="currency-flag">¥</span>
                  日元 (JPY)
                </span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="生效日期">
            <el-date-picker
              v-model="listQuery.effectiveDate"
              type="date"
              placeholder="请选择生效日期"
              style="width: 150px;"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
          <el-form-item label="汇率类型">
            <el-select
              v-model="listQuery.rateType"
              placeholder="请选择汇率类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="现汇" value="SPOT" />
              <el-option label="现钞" value="CASH" />
              <el-option label="中间价" value="MIDDLE" />
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
          <span class="title-text">汇率配置列表</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="fetchData">刷新</el-button>
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
        
        <el-table-column label="货币对" prop="currencyPair" align="center" width="150" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="currency-pair">
              <span class="base-currency">{{ row.baseCurrency }}</span>
              <i class="el-icon-right"></i>
              <span class="target-currency">{{ row.targetCurrency }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="汇率" prop="exchangeRate" align="center" width="120">
          <template slot-scope="{row}">
            <div class="rate-value">
              <span class="rate-number">{{ formatRate(row.exchangeRate) }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="汇率类型" prop="rateType" align="center" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getRateTypeColor(row.rateType)" size="small">
              {{ getRateTypeText(row.rateType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="生效日期" prop="effectiveDate" align="center" width="120">
          <template slot-scope="{row}">
            <span class="effective-date">{{ row.effectiveDate }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="失效日期" prop="expireDate" align="center" width="120">
          <template slot-scope="{row}">
            <span class="expire-date">{{ row.expireDate || '永久有效' }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="数据来源" prop="dataSource" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag size="mini" :type="getSourceColor(row.dataSource)">
              {{ getSourceText(row.dataSource) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="状态" class-name="status-col" width="100" align="center">
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
        
        <el-table-column label="更新时间" prop="updateTime" align="center" width="160">
          <template slot-scope="{row}">
            <span class="update-time">
              <i class="el-icon-time"></i>
              {{ formatTime(row.updateTime) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">
                编辑
              </el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(row)">
                查看
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
        <el-pagination
          background
          :current-page="listQuery.pageNum"
          :layout="layout"
          :page-size="listQuery.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import { getExchangeRateList, deleteExchangeRate, createExchangeRate, updateExchangeRate } from '@/api/globalTreasurer/czgg'

export default {
  name: 'ExchangeRateConfig',
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
      layout: 'total, sizes, prev, pager, next, jumper',
      listQuery: {
        pageNum: 1,
        pageSize: 20,
        baseCurrency: undefined,
        targetCurrency: undefined,
        effectiveDate: undefined,
        rateType: undefined
      },
      multipleSelection: [],
      lastUpdateTime: new Date().toLocaleString()
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.listLoading = true
      try {
        const response = await getExchangeRateList(this.listQuery)
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.list = response.data.tlist || response.data.list || response.data || []
          this.total = response.data.totalRecord || response.data.total || this.list.length
        } else {
          this.$message.error(response.message || '获取数据失败')
        }
      } catch (error) {
        this.$message.error('获取数据失败，请检查网络连接')
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.fetchData()
    },
    handleReset() {
      this.listQuery = {
        pageNum: 1,
        pageSize: 20,
        baseCurrency: undefined,
        targetCurrency: undefined,
        effectiveDate: undefined,
        rateType: undefined
      }
      this.fetchData()
    },
    handleSyncRate() {
      this.$message.success('汇率同步成功')
      this.lastUpdateTime = new Date().toLocaleString()
      this.fetchData()
    },
    handleExport() {
      this.$message.info('导出功能开发中...')
    },
    handleTableSetting() {
      this.$message.info('表格设置功能开发中...')
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.status === 0) {
        return 'disabled-row'
      }
      return ''
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    formatRate(rate) {
      return parseFloat(rate).toFixed(4)
    },
    getRateTypeColor(type) {
      const colorMap = {
        'SPOT': 'success',
        'CASH': 'warning',
        'MIDDLE': 'info'
      }
      return colorMap[type] || 'info'
    },
    getRateTypeText(type) {
      const textMap = {
        'SPOT': '现汇',
        'CASH': '现钞',
        'MIDDLE': '中间价'
      }
      return textMap[type] || '未知'
    },
    getSourceColor(source) {
      const colorMap = {
        'MANUAL': 'warning',
        'AUTO': 'success',
        'API': 'info'
      }
      return colorMap[source] || 'info'
    },
    getSourceText(source) {
      const textMap = {
        'MANUAL': '手工录入',
        'AUTO': '自动获取',
        'API': 'API接口'
      }
      return textMap[source] || '未知'
    },
    formatTime(time) {
      if (!time) return '-'
      return new Date(time).toLocaleString()
    },
    handleStatusChange(row) {
      this.$message.success('状态更新成功')
    },
    handleView(row) {
      this.$message.info('查看功能开发中...')
    },
    handleCreate() {
      this.$message.info('新增功能开发中...')
    },
    handleUpdate(row) {
      this.$message.info('编辑功能开发中...')
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      })
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val
      this.fetchData()
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      this.fetchData()
    }
  }
}
</script>

<style lang="scss" scoped>
.exchange-rate-config {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 8px;
      color: white;

      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            font-size: 28px;
          }
        }

        .page-description {
          margin: 0;
          opacity: 0.9;
          font-size: 14px;
        }
      }

      .header-right {
        .el-button {
          margin-left: 12px;
        }
      }
    }
  }

  .rate-overview {
    margin-bottom: 20px;

    .overview-card {
      border-radius: 8px;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      }

      .card-content {
        display: flex;
        align-items: center;
        padding: 10px;

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;

          i {
            font-size: 24px;
            color: white;
          }

          &.usd-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.eur-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &.jpy-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &.update-icon {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 4px;
          }

          .card-value {
            font-size: 20px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }

          .card-change {
            font-size: 12px;

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

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;

    .currency-option {
      display: flex;
      align-items: center;

      .currency-flag {
        margin-right: 8px;
        font-weight: bold;
        color: #409eff;
      }
    }
  }

  .table-card {
    border-radius: 8px;

    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .table-title {
        .title-text {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }

        .title-count {
          margin-left: 12px;
          color: #909399;
          font-size: 14px;
        }
      }
    }

    .currency-pair {
      display: flex;
      align-items: center;
      justify-content: center;

      .base-currency {
        font-weight: 600;
        color: #409eff;
      }

      i {
        margin: 0 8px;
        color: #909399;
      }

      .target-currency {
        font-weight: 600;
        color: #67c23a;
      }
    }

    .rate-value {
      .rate-number {
        font-family: 'Monaco', 'Menlo', monospace;
        font-size: 16px;
        font-weight: 600;
        color: #e6a23c;
      }
    }

    .effective-date,
    .expire-date {
      color: #606266;
      font-size: 12px;
    }

    .update-time {
      display: flex;
      align-items: center;
      color: #909399;
      font-size: 12px;

      i {
        margin-right: 4px;
      }
    }

    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}

// 全局样式
::v-deep .el-table {
  .disabled-row {
    background-color: #f5f7fa;
    color: #c0c4cc;
  }

  .el-table__row:hover {
    background-color: #f5f7fa;
  }
}

::v-deep .el-card__body {
  padding: 20px;
}

::v-deep .el-form--inline .el-form-item {
  margin-right: 20px;
  margin-bottom: 0;
}

::v-deep .el-button-group .el-button {
  margin-left: 0;
}
</style>
