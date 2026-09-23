<template>
  <el-dialog
    title="数据穿透分析"
    :visible.sync="visible"
    width="90%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :append-to-body="true"
    :modal-append-to-body="true"
    class="warning-drill-down-dialog"
    @close="handleClose"
  >
    <!-- 预警基本信息 -->
    <div class="warning-info-section" v-if="warningInfo">
      <h3 class="section-title">
        <i class="el-icon-warning"></i>
        预警基本信息
      </h3>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="info-item">
            <span class="label">预警编码：</span>
            <span class="value">{{ warningInfo.warningCode }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <span class="label">风险等级：</span>
            <el-tag :type="getRiskLevelType(warningInfo.warningLevel)">
              {{ getRiskLevelText(warningInfo.warningLevel) }}
            </el-tag>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <span class="label">预警状态：</span>
            <el-tag :type="getStatusType(warningInfo.warningStatus)">
              {{ getStatusText(warningInfo.warningStatus) }}
            </el-tag>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <span class="label">预警时间：</span>
            <span class="value">{{ overrideWarningTime ? formatDateTime(overrideWarningTime) : formatDateTime(warningInfo.warningTime) }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 10px;">
        <el-col :span="8">
          <div class="info-item">
            <span class="label">企业名称：</span>
            <span class="value">{{ warningInfo.companyName || '未知' }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <span class="label">预警值：</span>
            <span class="value">{{ warningInfo.warningValue }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <span class="label">阈值：</span>
            <span class="value">{{ warningInfo.thresholdValue }}</span>
          </div>
        </el-col>
      </el-row>
      <div class="info-item" style="margin-top: 10px;">
        <span class="label">预警描述：</span>
        <span class="value">{{ warningInfo.warningDescription }}</span>
      </div>
    </div>

    <!-- 数据源信息 -->
    <div class="data-source-section" v-if="dataSourceInfo">
      <h3 class="section-title">
        <i class="el-icon-database"></i>
        数据源信息
      </h3>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <span class="label">评估模型：</span>
            <span class="value">{{ dataSourceInfo.evalModelName || '未知' }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <span class="label">模型类型：</span>
            <span class="value">{{ dataSourceInfo.modelType || '未知' }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <span class="label">数据更新时间：</span>
            <span class="value">{{ formatDateTime(dataSourceInfo.dataUpdateTime) }}</span>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 原始业务数据（支持多表） -->
    <div class="business-data-section">
      <h3 class="section-title">
        <i class="el-icon-document"></i>
        原始业务数据
        <span v-if="businessDataTables.length > 0" class="table-count-badge">
          {{ businessDataTables.length }} 个数据表
        </span>
        <div class="section-actions">
          <el-button
            v-show="false"
            type="primary"
            size="small"
            icon="el-icon-download"
            @click="handleExport('excel')"
            :loading="exportLoading"
          >
            导出Excel
          </el-button>
          <el-button
            v-show="false"
            type="success"
            size="small"
            icon="el-icon-download"
            @click="handleExport('csv')"
            :loading="exportLoading"
          >
            导出CSV
          </el-button>
          <el-button
            v-show="false"
            type="info"
            size="small"
            icon="el-icon-refresh"
            @click="loadDrillDownData"
            :loading="loading"
          >
            刷新
          </el-button>
        </div>
      </h3>

      <!-- 多个数据表 -->
      <div v-if="businessDataTables.length > 0" class="multi-table-container">
        <div
          v-for="(tableData, index) in businessDataTables"
          :key="index"
          class="table-section"
        >
          <!-- 表格标题 -->
          <div class="table-header">
            <h4 class="table-title">
              <i class="el-icon-s-grid"></i>
              <span class="table-label">{{ tableData.tableLabel || tableData.tableName }}</span>
              <span class="table-name-separator">-</span>
              <span class="table-name">{{ tableData.tableName }}</span>
            </h4>
            <div class="table-info">
              <span class="info-text">共 {{ tableData.total || 0 }} 条数据</span>
            </div>
          </div>

          <!-- 数据表格 -->
          <el-table
            :data="tableData.list"
            v-loading="loading"
            size="small"
            border
            max-height="400"
            :empty-text="loading ? '加载中...' : '暂无数据'"
            class="data-table"
          >
            <el-table-column
              v-for="column in tableData.columns"
              :key="column.prop"
              :prop="column.prop"
              :label="column.label"
              :width="column.width"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <span v-if="column.prop === 'riskLevel' || column.prop === 'RISK_LEVEL'">
                  <el-tag :type="getRiskLevelType(scope.row[column.prop])">
                    {{ getRiskLevelText(scope.row[column.prop]) }}
                  </el-tag>
                </span>
                <span v-else-if="column.prop === 'status' || column.prop === 'STATUS'">
                  <el-tag :type="scope.row[column.prop] === '正常' || scope.row[column.prop] === 'ACTIVE' ? 'success' : 'danger'">
                    {{ scope.row[column.prop] }}
                  </el-tag>
                </span>
                <span v-else-if="column.prop.toUpperCase().includes('TIME') || column.prop.toUpperCase().includes('DATE')">
                  {{ formatDateTime(scope.row[column.prop]) }}
                </span>
                <span v-else-if="typeof scope.row[column.prop] === 'number'">
                  {{ formatNumber(scope.row[column.prop]) }}
                </span>
                <span v-else>
                  {{ scope.row[column.prop] || '-' }}
                </span>
              </template>
            </el-table-column>
          </el-table>

          <!-- 表格分页 -->
          <div class="table-pagination" v-if="tableData.total > pagination.pageSize">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="pagination.pageNum"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="pagination.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="tableData.total"
              small
            />
          </div>
        </div>
      </div>

      <!-- 无数据提示 -->
      <div v-else class="no-data-tip">
        <el-empty description="暂无原始业务数据" />
      </div>
    </div>

    <!-- 底部按钮 -->
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getWarningDrillDownData, exportWarningDrillDownData } from '@/api/risk/warning'

export default {
  name: 'WarningDrillDownDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    warningId: {
      type: String,
      default: ''
    },
    // 外部传入的时间，用于覆盖预警时间显示（如柱状图点击时传入执行结束时间）
    overrideWarningTime: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      exportLoading: false,
      warningInfo: null,
      dataSourceInfo: null,
      indicators: [],
      businessDataTables: [],  // 🔧 修改：支持多个表的数据
      pagination: {
        pageNum: 1,
        pageSize: 20,
        total: 0
      }
    }
  },
  watch: {
    visible(newVal) {
      if (newVal && this.warningId) {
        this.loadDrillDownData()
      }
    },
    warningId(newVal) {
      if (newVal && this.visible) {
        this.loadDrillDownData()
      }
    }
  },
  methods: {
    /**
     * TBL_OVERDEBT_RISK_INDICATOR 字段名 → 中文标签映射表
     * 依据 合同开发文档/1.md 中的 COMMENT 内容
     */
    getOverdebtColumnLabelMap() {
      return {
        RECORD_ID: '记录ID',
        COMPANY_ID: '企业ID',
        COMPANY_NAME: '企业名称',
        ASSESS_DATE: '评估基准日期',
        ASSET_LIABILITY_RATIO: '资产负债率(%)',
        DEBT_CAPITALIZATION_RATIO: '债务资本化比率(%)',
        LONG_TERM_DEBT_CAPITALIZATION_RATIO: '长期债务资本化比率(%)',
        SHORT_TERM_DEBT_RATIO: '短期债务占比(%)',
        INTEREST_COVERAGE_RATIO: '利息保障倍数',
        OPERATING_CASHFLOW_DEBT_RATIO: '经营现金流量债务比',
        TOTAL_ASSETS: '总资产(元)',
        TOTAL_DEBT: '总债务(元)',
        LONG_TERM_DEBT: '长期债务(元)',
        SHORT_TERM_DEBT: '短期债务(元)',
        NET_ASSETS: '净资产(元)',
        TOTAL_LIABILITIES: '总负债(元)',
        CURRENT_ASSETS: '流动资产(元)',
        CURRENT_LIABILITIES: '流动负债(元)',
        CASH_BALANCE: '货币资金余额(元)',
        EBIT: '息税前利润(元)',
        INTEREST_EXPENSE: '利息费用(元)',
        CURRENT_RATIO: '流动比率',
        QUICK_RATIO: '速动比率',
        COMPOSITE_SCORE: '综合风险评分',
        RISK_LEVEL: '风险等级',
        RISK_SCENARIO: '风险场景描述',
        RISK_COLOR: '风险颜色标识',
        ASSESS_TIME: '评估执行时间',
        CREATE_BY: '创建人',
        UPDATE_TIME: '最后更新时间',
        REMARK: '备注'
      }
    },

    /**
     * 对表格列名做中文覆盖
     * 优先使用 getOverdebtColumnLabelMap 中的映射，未命中则保留后端返回的 label
     */
    applyColumnLabelMap(tables) {
      const labelMap = this.getOverdebtColumnLabelMap()
      return tables.map(table => {
        if (!table.columns) return table
        return {
          ...table,
          columns: table.columns.map(col => {
            const upperProp = (col.prop || '').toUpperCase()
            return labelMap[upperProp]
              ? { ...col, label: labelMap[upperProp] }
              : col
          })
        }
      })
    },

    // 加载穿透数据（支持多表）
    async loadDrillDownData() {
      if (!this.warningId) {
        this.$message.error('预警ID不能为空')
        return
      }

      this.loading = true
      try {
        const params = {
          warningId: this.warningId,
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }

        const response = await getWarningDrillDownData(params)
        if (response.code === 1) {
          const data = response.data
          this.warningInfo = data.warningInfo
          this.dataSourceInfo = data.dataSourceInfo
          this.indicators = data.indicators || []

          // 🔧 修改：处理多表数据
          if (data.businessData && data.businessData.tables) {
            this.businessDataTables = data.businessData.tables || []
            console.log('加载到 ' + this.businessDataTables.length + ' 个数据表')
          } else {
            // 兼容旧格式（单表）
            this.businessDataTables = data.businessData ? [data.businessData] : []
          }

          // 对列名做中文覆盖（将英文字段名替换为中文标签）
          this.businessDataTables = this.applyColumnLabelMap(this.businessDataTables)

          // 计算总数据量
          this.pagination.total = this.businessDataTables.reduce((sum, table) => sum + (table.total || 0), 0)

          console.log('数据穿透分析加载成功:', data)
          console.log('业务数据表:', this.businessDataTables)
        } else {
          this.$message.error(response.msg || '加载失败')
        }
      } catch (error) {
        console.error('加载数据穿透分析失败:', error)
        this.$message.error('加载失败: ' + (error.message || '网络错误'))
      } finally {
        this.loading = false
      }
    },

    // 导出数据
    async handleExport(format) {
      if (!this.warningId) {
        this.$message.error('预警ID不能为空')
        return
      }

      this.exportLoading = true
      try {
        const params = {
          warningId: this.warningId,
          exportFormat: format
        }

        const response = await exportWarningDrillDownData(params)
        if (response.code === 1) {
          this.$message.success('导出成功')
          // 这里可以添加下载逻辑
          console.log('导出结果:', response.data)
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败: ' + (error.message || '网络错误'))
      } finally {
        this.exportLoading = false
      }
    },

    // 分页大小改变
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.pageNum = 1
      this.loadDrillDownData()
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.pagination.pageNum = val
      this.loadDrillDownData()
    },

    // 关闭对话框
    handleClose() {
      this.$emit('update:visible', false)
      this.resetData()
    },

    // 重置数据
    resetData() {
      this.warningInfo = null
      this.dataSourceInfo = null
      this.indicators = []
      this.businessDataTables = []  // 🔧 修改：重置多表数据
      this.pagination = { pageNum: 1, pageSize: 20, total: 0 }
    },

    // 获取风险等级类型
    getRiskLevelType(level) {
      const typeMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return typeMap[level] || 'info'
    },

    // 获取风险等级文本
    getRiskLevelText(level) {
      const textMap = {
        'HIGH': '高风险',
        'MEDIUM': '中风险',
        'LOW': '低风险'
      }
      return textMap[level] || level
    },

    // 获取状态类型
    getStatusType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'PROCESSING': 'primary',
        'PROCESSED': 'success',
        'IGNORED': 'info'
      }
      return typeMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'PENDING': '待处理',
        'PROCESSING': '处理中',
        'PROCESSED': '已处理',
        'IGNORED': '已忽略'
      }
      return textMap[status] || status
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      const date = new Date(dateTime)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    },

    // 格式化数字
    formatNumber(value) {
      if (value === null || value === undefined) return '-'
      if (typeof value !== 'number') return value

      // 如果是整数，直接返回
      if (Number.isInteger(value)) {
        return value.toLocaleString('zh-CN')
      }

      // 如果是小数，保留2位小数
      return value.toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.warning-drill-down-dialog {
  .section-title {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin: 0 0 15px 0;
    padding: 10px 0;
    border-bottom: 2px solid #e6e6e6;
    font-size: 16px;
    font-weight: bold;
    color: #333;

    i {
      margin-right: 8px;
      color: #409eff;
    }

    .table-count-badge {
      margin-left: 10px;
      padding: 2px 10px;
      background: #409eff;
      color: white;
      border-radius: 12px;
      font-size: 12px;
      font-weight: normal;
    }

    .section-actions {
      display: flex;
      gap: 8px;
    }
  }

  .warning-info-section,
  .data-source-section,
  .indicators-section,
  .business-data-section {
    margin-bottom: 25px;
    padding: 15px;
    background: #f9f9f9;
    border-radius: 6px;
    border: 1px solid #e6e6e6;
  }

  .info-item {
    display: flex;
    align-items: center;
    margin-bottom: 8px;

    .label {
      font-weight: 500;
      color: #666;
      min-width: 100px;
      margin-right: 10px;
    }

    .value {
      color: #333;
      flex: 1;
    }
  }

  // 多表容器样式
  .multi-table-container {
    .table-section {
      margin-bottom: 30px;
      padding: 15px;
      background: white;
      border-radius: 6px;
      border: 1px solid #e6e6e6;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

      &:last-child {
        margin-bottom: 0;
      }

      .table-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 1px solid #e6e6e6;

        .table-title {
          display: flex;
          align-items: center;
          margin: 0;
          font-size: 14px;
          font-weight: 600;
          color: #333;

          i {
            margin-right: 8px;
            color: #67c23a;
          }

          .table-label {
            color: #409eff;
            font-weight: 700;
            font-size: 15px;
          }

          .table-name-separator {
            margin: 0 8px;
            color: #909399;
            font-weight: 400;
          }

          .table-name {
            color: #606266;
            font-weight: 500;
            font-size: 13px;
            font-family: 'Courier New', monospace;
            background-color: #f5f7fa;
            padding: 2px 8px;
            border-radius: 3px;
          }

          .el-tag {
            margin-left: 10px;
          }
        }

        .table-info {
          .info-text {
            font-size: 12px;
            color: #909399;
          }
        }
      }

      .data-table {
        margin-bottom: 10px;
      }

      .table-pagination {
        display: flex;
        justify-content: flex-end;
        margin-top: 15px;
        padding-top: 10px;
        border-top: 1px solid #f0f0f0;
      }
    }
  }

  .no-data-tip {
    padding: 40px 0;
    text-align: center;
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 20px;
    padding: 15px 0;
  }

  .dialog-footer {
    text-align: center;
    padding: 15px 0;
  }

  // 表格样式优化
  ::v-deep .el-table {
    .el-table__header {
      background-color: #f5f7fa;

      th {
        background-color: #f5f7fa !important;
        color: #333;
        font-weight: 600;
      }
    }

    .el-table__row {
      &:hover {
        background-color: #f0f9ff;
      }
    }
  }

  // 标签样式优化
  ::v-deep .el-tag {
    border-radius: 4px;
    font-size: 12px;
    padding: 0 8px;
    height: 24px;
    line-height: 22px;
  }

  // 按钮样式优化
  ::v-deep .el-button {
    border-radius: 4px;

    &.el-button--small {
      padding: 7px 15px;
      font-size: 12px;
    }
  }

  // 对话框样式优化
  ::v-deep .el-dialog {
    border-radius: 8px;

    .el-dialog__header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 20px 24px;
      border-radius: 8px 8px 0 0;

      .el-dialog__title {
        color: white;
        font-size: 18px;
        font-weight: 600;
      }

      .el-dialog__close {
        color: white;
        font-size: 20px;

        &:hover {
          color: #f0f0f0;
        }
      }
    }

    .el-dialog__body {
      padding: 24px;
      max-height: 70vh;
      overflow-y: auto;
    }

    .el-dialog__footer {
      padding: 15px 24px;
      border-top: 1px solid #e6e6e6;
    }
  }

  // 响应式设计
  @media (max-width: 768px) {
    .section-title {
      flex-direction: column;
      align-items: flex-start;
      gap: 10px;

      .section-actions {
        width: 100%;
        justify-content: flex-end;
      }
    }

    .info-item {
      flex-direction: column;
      align-items: flex-start;

      .label {
        min-width: auto;
        margin-bottom: 5px;
      }
    }
  }
}

// 全局样式优化
::v-deep .el-loading-mask {
  background-color: rgba(255, 255, 255, 0.8);
}

::v-deep .el-pagination {
  .el-pagination__total,
  .el-pagination__jump {
    color: #666;
  }

  .el-pager li {
    &.active {
      background-color: #409eff;
      color: white;
    }
  }
}
</style>
