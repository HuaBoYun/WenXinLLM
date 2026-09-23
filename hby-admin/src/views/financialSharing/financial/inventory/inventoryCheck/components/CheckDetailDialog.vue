<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="visible"
    width="90%"
    :before-close="handleClose"
    class="check-detail-dialog"
  >
    <div class="dialog-content">
      <!-- 盘点基本信息 -->
      <div class="info-section">
        <h3>盘点信息</h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="info-item">
              <label>盘点编号：</label>
              <span>{{ checkData.checkNumber }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>盘点名称：</label>
              <span>{{ checkData.checkName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>盘点类型：</label>
              <span>{{ checkData.checkTypeName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>盘点日期：</label>
              <span>{{ checkData.checkDate }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="info-item">
              <label>盘点仓库：</label>
              <span>{{ checkData.warehouseName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>存货数量：</label>
              <span>{{ checkData.inventoryCount }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>盘点进度：</label>
              <el-progress :percentage="checkData.checkProgress" :stroke-width="8" />
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>状态：</label>
              <el-tag :type="getStatusTagType(checkData.status)">
                {{ getStatusText(checkData.status) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 盘点明细 -->
      <div class="detail-section">
        <div class="section-header">
          <h3>盘点明细</h3>
          <div class="header-actions">
            <el-button size="small" @click="handleRefreshDetail">刷新</el-button>
            <el-button size="small" type="primary" @click="handleExportDetail">导出</el-button>
          </div>
        </div>
        
        <!-- 筛选条件 -->
        <div class="filter-bar">
          <el-form :inline="true" size="small">
            <el-form-item label="差异状态">
              <el-select v-model="detailFilter.varianceStatus" placeholder="全部" style="width: 120px">
                <el-option label="全部" value="" />
                <el-option label="无差异" value="NONE" />
                <el-option label="盘盈" value="SURPLUS" />
                <el-option label="盘亏" value="SHORTAGE" />
              </el-select>
            </el-form-item>
            <el-form-item label="盘点状态">
              <el-select v-model="detailFilter.checkStatus" placeholder="全部" style="width: 120px">
                <el-option label="全部" value="" />
                <el-option label="未盘点" value="UNCHECKED" />
                <el-option label="已盘点" value="CHECKED" />
                <el-option label="已确认" value="CONFIRMED" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadDetailData">查询</el-button>
            </el-form-item>
          </el-form>
        </div>

        <el-table :data="detailData" border v-loading="detailLoading">
          <el-table-column prop="inventoryCode" label="存货编码" width="120" />
          <el-table-column prop="inventoryName" label="存货名称" min-width="150" show-overflow-tooltip />
          <el-table-column prop="specification" label="规格型号" width="120" />
          <el-table-column prop="unit" label="单位" width="80" />
          <el-table-column prop="bookQuantity" label="账面数量" width="100" align="right" />
          <el-table-column prop="actualQuantity" label="实盘数量" width="100" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.actualQuantity !== null">{{ scope.row.actualQuantity }}</span>
              <span v-else class="text-muted">未盘点</span>
            </template>
          </el-table-column>
          <el-table-column prop="varianceQuantity" label="差异数量" width="100" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.varianceQuantity !== null" 
                    :class="getVarianceClass(scope.row.varianceQuantity)">
                {{ scope.row.varianceQuantity }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="unitCost" label="单位成本" width="100" align="right">
            <template slot-scope="scope">
              <span class="amount">{{ formatAmount(scope.row.unitCost) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="varianceAmount" label="差异金额" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.varianceAmount !== null" 
                    :class="['amount', getVarianceClass(scope.row.varianceAmount)]">
                {{ formatAmount(scope.row.varianceAmount) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="checkStatus" label="盘点状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getCheckStatusTagType(scope.row.checkStatus)" size="small">
                {{ getCheckStatusText(scope.row.checkStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="checkerName" label="盘点人" width="100" />
          <el-table-column prop="checkTime" label="盘点时间" width="140" />
        </el-table>

        <!-- 分页 -->
        <div class="pagination-container">
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

      <!-- 统计汇总 -->
      <div class="summary-section">
        <h3>盘点汇总</h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="summary-card">
              <div class="card-title">总存货数</div>
              <div class="card-value">{{ summary.totalCount }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="summary-card">
              <div class="card-title">已盘点数</div>
              <div class="card-value checked">{{ summary.checkedCount }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="summary-card">
              <div class="card-title">盘盈金额</div>
              <div class="card-value surplus">{{ formatAmount(summary.surplusAmount) }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="summary-card">
              <div class="card-title">盘亏金额</div>
              <div class="card-value shortage">{{ formatAmount(summary.shortageAmount) }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button v-if="checkData.status === 'CHECKING'" type="primary" @click="handleInputResult">
        录入盘点结果
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getInventoryCheckById, getInventoryCheckResults } from '@/api/financialSharing/inventory'

export default {
  name: 'CheckDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    checkData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      detailData: [],
      detailLoading: false,
      detailFilter: {
        varianceStatus: '',
        checkStatus: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      summary: {
        totalCount: 0,
        checkedCount: 0,
        surplusAmount: 0,
        shortageAmount: 0
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.checkData.checkNumber ? `盘点详情 - ${this.checkData.checkNumber}` : '盘点详情'
    }
  },
  watch: {
    visible(val) {
      if (val && this.checkData.checkId) {
        this.loadDetailData()
        this.loadSummaryData()
      }
    }
  },
  methods: {
    async loadDetailData() {
      this.detailLoading = true
      try {
        const params = {
          checkId: this.checkData.checkId,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.detailFilter
        }
        const response = await getInventoryCheckResults(this.checkData.checkId, params)
        if (response.code === 1) {
          this.detailData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '加载盘点明细失败')
        }
      } catch (error) {
        this.$message.error('加载盘点明细失败：' + error.message)
      } finally {
        this.detailLoading = false
      }
    },

    async loadSummaryData() {
      try {
        // 从盘点详情中获取汇总数据
        const response = await getInventoryCheckById(this.checkData.checkId)
        if (response.code === 1 && response.data) {
          const data = response.data
          this.summary = {
            totalCount: data.inventoryCount || 0,
            checkedCount: Math.round((data.inventoryCount || 0) * (data.checkProgress || 0) / 100),
            surplusAmount: data.surplusAmount || 0,
            shortageAmount: data.shortageAmount || 0
          }
        }
      } catch (error) {
        console.error('加载汇总数据失败：', error)
      }
    },

    handleRefreshDetail() {
      this.loadDetailData()
    },

    handleExportDetail() {
      try {
        const data = this.detailData || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `盘点明细_${this.checkData.checkNumber || ''}.json`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
      }
    },

    handleInputResult() {
      this.$emit('input-result', this.checkId)
    },

    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadDetailData()
    },

    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadDetailData()
    },

    handleClose() {
      this.$emit('update:visible', false)
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    },

    getStatusTagType(status) {
      const typeMap = {
        'PLANNING': 'info',
        'CHECKING': 'warning',
        'COMPLETED': 'success',
        'APPROVED': 'primary',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'default'
    },

    getStatusText(status) {
      const textMap = {
        'PLANNING': '计划中',
        'CHECKING': '盘点中',
        'COMPLETED': '已完成',
        'APPROVED': '已审批',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },

    getCheckStatusTagType(status) {
      const typeMap = {
        'UNCHECKED': 'info',
        'CHECKED': 'success',
        'CONFIRMED': 'primary'
      }
      return typeMap[status] || 'default'
    },

    getCheckStatusText(status) {
      const textMap = {
        'UNCHECKED': '未盘点',
        'CHECKED': '已盘点',
        'CONFIRMED': '已确认'
      }
      return textMap[status] || status
    },

    getVarianceClass(variance) {
      if (variance > 0) return 'surplus'
      if (variance < 0) return 'shortage'
      return ''
    }
  }
}
</script>

<style lang="scss" scoped>
.check-detail-dialog {
  .dialog-content {
    max-height: 700px;
    overflow-y: auto;

    .info-section,
    .detail-section,
    .summary-section {
      margin-bottom: 30px;

      h3 {
        margin: 0 0 15px 0;
        color: #303133;
        font-size: 16px;
        border-bottom: 1px solid #e4e7ed;
        padding-bottom: 8px;
      }
    }

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;

      h3 {
        margin: 0;
        border: none;
        padding: 0;
      }
    }

    .info-item {
      margin-bottom: 10px;

      label {
        font-weight: 600;
        color: #606266;
        margin-right: 8px;
      }
    }

    .filter-bar {
      margin-bottom: 15px;
      padding: 15px;
      background: #f5f7fa;
      border-radius: 4px;
    }

    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }

    .summary-section {
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

          &.checked {
            color: #67c23a;
          }

          &.surplus {
            color: #67c23a;
          }

          &.shortage {
            color: #f56c6c;
          }
        }
      }
    }
  }

  .dialog-footer {
    text-align: right;
  }
}

.amount {
  color: #f56c6c;
  font-weight: 600;
}

.surplus {
  color: #67c23a;
  font-weight: 600;
}

.shortage {
  color: #f56c6c;
  font-weight: 600;
}

.text-muted {
  color: #c0c4cc;
}
</style>
