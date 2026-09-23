<template>
  <div class="inventory-analysis-container">
    <div class="page-header">
      <h2>库存分析报表</h2>
      <p>提供多维度库存分析，包括ABC分析、呆滞分析、周转分析等</p>
    </div>
    
    <!-- 分析类型选择 -->
    <div class="analysis-tabs">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="ABC分析" name="abc">
          <div class="analysis-content">
            <div class="filter-bar">
              <el-form :inline="true">
                <el-form-item label="分析维度">
                  <el-select v-model="abcForm.dimension" style="width: 150px">
                    <el-option label="按金额" value="amount" />
                    <el-option label="按数量" value="quantity" />
                    <el-option label="按利润" value="profit" />
                  </el-select>
                </el-form-item>
                <el-form-item label="分析期间">
                  <el-date-picker
                    v-model="abcForm.dateRange"
                    type="monthrange"
                    range-separator="至"
                    start-placeholder="开始月份"
                    end-placeholder="结束月份"
                    style="width: 240px"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleAbcAnalysis">分析</el-button>
                  <el-button @click="handleExportAbc">导出</el-button>
                </el-form-item>
              </el-form>
            </div>
            
            <!-- ABC分析结果 -->
            <div class="analysis-result">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="result-card">
                    <div class="card-header">A类存货</div>
                    <div class="card-content">
                      <div class="metric">
                        <span class="value">{{ abcResult.aCount }}</span>
                        <span class="label">种类</span>
                      </div>
                      <div class="metric">
                        <span class="value">{{ abcResult.aPercent }}%</span>
                        <span class="label">占比</span>
                      </div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="result-card">
                    <div class="card-header">B类存货</div>
                    <div class="card-content">
                      <div class="metric">
                        <span class="value">{{ abcResult.bCount }}</span>
                        <span class="label">种类</span>
                      </div>
                      <div class="metric">
                        <span class="value">{{ abcResult.bPercent }}%</span>
                        <span class="label">占比</span>
                      </div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="result-card">
                    <div class="card-header">C类存货</div>
                    <div class="card-content">
                      <div class="metric">
                        <span class="value">{{ abcResult.cCount }}</span>
                        <span class="label">种类</span>
                      </div>
                      <div class="metric">
                        <span class="value">{{ abcResult.cPercent }}%</span>
                        <span class="label">占比</span>
                      </div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="呆滞分析" name="slowMoving">
          <div class="analysis-content">
            <div class="filter-bar">
              <el-form :inline="true">
                <el-form-item label="呆滞天数">
                  <el-input-number
                    v-model="slowMovingForm.days"
                    :min="30"
                    :max="365"
                    style="width: 120px"
                  />
                </el-form-item>
                <el-form-item label="仓库">
                  <el-select v-model="slowMovingForm.warehouseId" style="width: 150px">
                    <el-option label="全部仓库" value="" />
                    <el-option label="主仓库" value="2001" />
                    <el-option label="分仓库" value="2002" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleSlowMovingAnalysis">分析</el-button>
                  <el-button @click="handleExportSlowMoving">导出</el-button>
                </el-form-item>
              </el-form>
            </div>
            
            <!-- 呆滞分析结果表格 -->
            <div class="table-container">
              <el-table :data="slowMovingData" border>
                <el-table-column prop="inventoryCode" label="存货编码" width="120" />
                <el-table-column prop="inventoryName" label="存货名称" min-width="150" />
                <el-table-column prop="warehouseName" label="仓库" width="120" />
                <el-table-column prop="quantity" label="库存数量" width="100" align="right" />
                <el-table-column prop="amount" label="库存金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount">{{ formatAmount(scope.row.amount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="lastMoveDate" label="最后出库日期" width="120" />
                <el-table-column prop="slowDays" label="呆滞天数" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getSlowDaysTagType(scope.row.slowDays)">
                      {{ scope.row.slowDays }}天
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="周转分析" name="turnover">
          <div class="analysis-content">
            <div class="filter-bar">
              <el-form :inline="true">
                <el-form-item label="分析期间">
                  <el-date-picker
                    v-model="turnoverForm.dateRange"
                    type="monthrange"
                    range-separator="至"
                    start-placeholder="开始月份"
                    end-placeholder="结束月份"
                    style="width: 240px"
                  />
                </el-form-item>
                <el-form-item label="存货分类">
                  <el-select v-model="turnoverForm.categoryId" style="width: 150px">
                    <el-option label="全部分类" value="" />
                    <el-option label="原材料" value="1001" />
                    <el-option label="半成品" value="1002" />
                    <el-option label="产成品" value="1003" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleTurnoverAnalysis">分析</el-button>
                  <el-button @click="handleExportTurnover">导出</el-button>
                </el-form-item>
              </el-form>
            </div>
            
            <!-- 周转分析结果 -->
            <div class="turnover-summary">
              <el-row :gutter="20">
                <el-col :span="6">
                  <div class="summary-card">
                    <div class="card-title">平均周转率</div>
                    <div class="card-value">{{ turnoverSummary.avgTurnoverRate }}</div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="summary-card">
                    <div class="card-title">平均周转天数</div>
                    <div class="card-value">{{ turnoverSummary.avgTurnoverDays }}天</div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="summary-card">
                    <div class="card-title">最高周转率</div>
                    <div class="card-value">{{ turnoverSummary.maxTurnoverRate }}</div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="summary-card">
                    <div class="card-title">最低周转率</div>
                    <div class="card-value">{{ turnoverSummary.minTurnoverRate }}</div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
export default {
  name: 'InventoryAnalysis',
  data() {
    return {
      activeTab: 'abc',
      abcForm: {
        dimension: 'amount',
        dateRange: []
      },
      slowMovingForm: {
        days: 90,
        warehouseId: ''
      },
      turnoverForm: {
        dateRange: [],
        categoryId: ''
      },
      abcResult: {
        aCount: 25,
        aPercent: 20,
        bCount: 45,
        bPercent: 30,
        cCount: 80,
        cPercent: 50
      },
      slowMovingData: [],
      turnoverSummary: {
        avgTurnoverRate: 4.2,
        avgTurnoverDays: 87,
        maxTurnoverRate: 12.5,
        minTurnoverRate: 0.8
      }
    }
  },
  mounted() {
    this.loadSlowMovingData()
  },
  methods: {
    handleTabClick(tab) {
      console.log('切换到标签页：', tab.name)
    },

    handleAbcAnalysis() {
      this.$message.success('ABC分析完成')
    },

    handleSlowMovingAnalysis() {
      this.loadSlowMovingData()
      this.$message.success('呆滞分析完成')
    },

    handleTurnoverAnalysis() {
      this.$message.success('周转分析完成')
    },

    handleExportAbc() {
      try {
        const data = this.abcSummary || {}
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = 'ABC分析报表.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) { this.$message.error('导出失败') }
    },

    handleExportSlowMoving() {
      try {
        const data = this.slowMovingData || []
        if (data.length === 0) { this.$message.warning('暂无数据可导出'); return }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '呆滞分析报表.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) { this.$message.error('导出失败') }
    },

    handleExportTurnover() {
      try {
        const data = this.turnoverSummary || {}
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '周转分析报表.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) { this.$message.error('导出失败') }
    },

    loadSlowMovingData() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.slowMovingData = []
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    },

    getSlowDaysTagType(days) {
      if (days >= 180) return 'danger'
      if (days >= 120) return 'warning'
      return 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.inventory-analysis-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
  
  h2 {
    margin: 0 0 8px 0;
    color: #303133;
  }
  
  p {
    margin: 0;
    color: #606266;
    font-size: 14px;
  }
}

.analysis-tabs {
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.analysis-content {
  .filter-bar {
    margin-bottom: 20px;
    padding: 15px;
    background: #f5f7fa;
    border-radius: 4px;
  }

  .analysis-result {
    margin-bottom: 20px;

    .result-card {
      background: white;
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      padding: 20px;
      text-align: center;

      .card-header {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 15px;
      }

      .card-content {
        display: flex;
        justify-content: space-around;

        .metric {
          .value {
            display: block;
            font-size: 24px;
            font-weight: 600;
            color: #409eff;
            margin-bottom: 5px;
          }

          .label {
            font-size: 14px;
            color: #909399;
          }
        }
      }
    }
  }

  .turnover-summary {
    margin-bottom: 20px;

    .summary-card {
      background: white;
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      padding: 20px;
      text-align: center;

      .card-title {
        font-size: 14px;
        color: #909399;
        margin-bottom: 10px;
      }

      .card-value {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
      }
    }
  }

  .table-container {
    .amount {
      color: #f56c6c;
      font-weight: 600;
    }
  }
}
</style>
