<template>
  <div class="warning-table-card">
    <div class="card-header">
      <span class="card-title">
        <i class="el-icon-warning"></i> 风险预警数据
        <el-tag v-if="selectedModelId" type="warning" size="mini" closable @close="clearFilter" class="filter-tag">
          已过滤
        </el-tag>
      </span>
      <div class="header-actions">
        <el-button type="text" size="small" @click="goToFullPage" class="full-btn">
          <i class="el-icon-rank"></i> 查看全部
        </el-button>
      </div>
    </div>

    <div class="card-body" v-loading="loading">
      <el-table
        :data="warningList"
        border
        stripe
        size="mini"
        style="width: 100%"
        :row-class-name="getRowClass"
        max-height="320"
      >
        <el-table-column prop="warningCode" label="预警编码" width="130" show-overflow-tooltip />
        <el-table-column label="预警类型" width="110">
          <template slot-scope="{ row }">
            <el-tag :type="getTypeTag(row.warningType)" size="mini">
              {{ getTypeText(row.warningType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警级别" width="90">
          <template slot-scope="{ row }">
            <el-tag :type="getLevelTag(row.warningLevel)" size="mini">
              {{ getLevelText(row.warningLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template slot-scope="{ row }">
            <el-tag :type="getStatusTag(row.warningStatus)" size="mini">
              {{ getStatusText(row.warningStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="companyName" label="企业名称" min-width="120" show-overflow-tooltip />
        <el-table-column prop="warningDescription" label="预警描述" min-width="160" show-overflow-tooltip />
        <el-table-column prop="warningTime" label="预警时间" width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="mini" @click="viewDetail(row)">查看</el-button>
            <el-button
              type="text"
              size="mini"
              style="color: #f57c00"
              @click="goDispose(row)"
            >处置</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :total="total"
          :page-size="20"
          :current-page="currentPage"
          small
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WarningTableCard',
  props: {
    warningList: {
      type: Array,
      default: () => [],
    },
    total: {
      type: Number,
      default: 0,
    },
    loading: {
      type: Boolean,
      default: false,
    },
    selectedModelId: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      currentPage: 1,
    }
  },
  methods: {
    getRowClass({ row }) {
      if (row.warningLevel === 'HIGH') return 'row-high'
      if (row.warningLevel === 'MEDIUM') return 'row-medium'
      return ''
    },
    getTypeTag(type) {
      const map = {
        FINANCIAL_RISK: 'danger',
        PROCUREMENT_RISK: 'warning',
        CREDIT_RISK: '',
        COMPLIANCE_RISK: 'warning',
      }
      return map[type] || 'info'
    },
    getTypeText(type) {
      const map = {
        FINANCIAL_RISK: '财务风险',
        PROCUREMENT_RISK: '采购风险',
        CREDIT_RISK: '信用风险',
        COMPLIANCE_RISK: '合规风险',
        COMBINATION_EXECUTION: '组合执行',
        DATA_MODEL_EXECUTION: '模型执行',
        AUTO_GENERATED: '自动生成',
        MANUAL_CREATED: '手动创建',
        THRESHOLD: '阈值预警',
        TREND: '趋势预警',
        ANOMALY: '异常预警',
      }
      return map[type] || type || '-'
    },
    getLevelTag(level) {
      const map = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }
      return map[level] || 'info'
    },
    getLevelText(level) {
      const map = { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }
      return map[level] || level || '-'
    },
    getStatusTag(status) {
      const map = { PENDING: 'warning', PROCESSING: '', PROCESSED: 'success', IGNORED: 'info' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { PENDING: '待处理', PROCESSING: '处理中', PROCESSED: '已处理', IGNORED: '已忽略' }
      return map[status] || status || '-'
    },
    clearFilter() {
      this.$emit('filter-model', '')
    },
    goToFullPage() {
      this.$router.push({ path: '/risk/mxgl/fxyjgl' }).catch(() => {})
    },
    viewDetail(row) {
      this.$router.push({
        path: '/risk/mxgl/fxyjgl',
        query: { warningId: row.warningId },
      }).catch(() => {})
    },
    goDispose(row) {
      this.$router.push({
        path: '/risk/mxgl/fxyjgl',
        query: { warningId: row.warningId, action: 'dispose' },
      }).catch(() => {})
    },
    handlePageChange(page) {
      this.currentPage = page
      this.$emit('page-change', page)
    },
  },
}
</script>

<style lang="scss" scoped>
.warning-table-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 16px;
    background: linear-gradient(90deg, #f5f7fa 0%, #ffffff 100%);
    border-bottom: 2px solid #fff3e0;

    .card-title {
      font-size: 14px;
      font-weight: 600;
      color: #e65100;
      display: flex;
      align-items: center;
      gap: 6px;

      .filter-tag {
        margin-left: 4px;
      }
    }

    .full-btn {
      color: #1976d2;
      font-size: 12px;
    }
  }

  .card-body {
    padding: 12px;

    :deep(.row-high) td {
      background: #fff8f8 !important;
    }

    :deep(.row-medium) td {
      background: #fffbf0 !important;
    }

    .pagination-wrap {
      margin-top: 10px;
      display: flex;
      justify-content: flex-end;
    }
  }
}
</style>
