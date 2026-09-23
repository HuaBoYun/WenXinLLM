<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-card class="mb8">
      <div slot="header">
        <span>合并报表查询</span>
      </div>
      <el-form :model="queryForm" :inline="true" label-width="100px">
        <el-form-item label="合并模型">
          <el-select v-model="queryForm.modelId" placeholder="请选择合并模型" clearable @change="handleModelChange">
            <el-option
              v-for="item in modelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="期间">
          <el-input v-model="queryForm.period" placeholder="请输入期间" clearable @change="handlePeriodChange" />
        </el-form-item>
        <el-form-item label="报表类型">
          <el-select v-model="queryForm.reportType" placeholder="请选择报表类型" clearable>
            <el-option label="资产负债表" value="BALANCE_SHEET" />
            <el-option label="利润表" value="INCOME_STATEMENT" />
            <el-option label="现金流量表" value="CASH_FLOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已确认" value="CONFIRMED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb8">
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #409eff;">
              <i class="el-icon-document" />
            </div>
            <div class="stat-content">
              <div class="stat-title">已生成报表类型</div>
              <div class="stat-value">{{ reportTypeCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #67c23a;">
              <i class="el-icon-check" />
            </div>
            <div class="stat-content">
              <div class="stat-title">已确认报表</div>
              <div class="stat-value">{{ confirmedCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #e6a23c;">
              <i class="el-icon-edit" />
            </div>
            <div class="stat-content">
              <div class="stat-title">草稿报表</div>
              <div class="stat-value">{{ draftCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-download" @click="handleExport">导出Excel</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-s-data" @click="showCompareDialog">期间对比</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table 
      v-loading="loading" 
      :data="tableData" 
      border 
      :tree-props="{children: 'children'}" 
      row-key="itemCode"
      :default-expand-all="false"
    >
      <el-table-column label="项目编码" prop="itemCode" width="120" />
      <el-table-column label="项目名称" prop="itemName" width="200" />
      <el-table-column label="母公司金额" prop="parentAmount" width="150" align="right">
        <template slot-scope="scope">
          {{ scope.row.parentAmount | numberFormat }}
        </template>
      </el-table-column>
      <el-table-column label="子公司金额" prop="subsidiaryAmount" width="150" align="right">
        <template slot-scope="scope">
          {{ scope.row.subsidiaryAmount | numberFormat }}
        </template>
      </el-table-column>
      <el-table-column label="抵消金额" prop="eliminationAmount" width="150" align="right">
        <template slot-scope="scope">
          <span :class="{'elimination-amount': scope.row.eliminationAmount && scope.row.eliminationAmount != 0}">
            {{ scope.row.eliminationAmount | numberFormat }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="合并金额" prop="consolidatedAmount" width="150" align="right">
        <template slot-scope="scope">
          <span class="consolidated-amount">{{ scope.row.consolidatedAmount | numberFormat }}</span>
        </template>
      </el-table-column>
      <el-table-column label="报表类型" prop="reportType" width="120" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.reportType === 'BALANCE_SHEET'" type="primary" size="mini">资产负债表</el-tag>
          <el-tag v-else-if="scope.row.reportType === 'INCOME_STATEMENT'" type="success" size="mini">利润表</el-tag>
          <el-tag v-else-if="scope.row.reportType === 'CASH_FLOW'" type="warning" size="mini">现金流量表</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'DRAFT'" type="info" size="mini">草稿</el-tag>
          <el-tag v-else type="success" size="mini">已确认</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="160" />
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNum"
      :limit.sync="queryForm.pageSize"
      @pagination="loadData"
    />

    <!-- 期间对比对话框 -->
    <el-dialog title="期间对比" :visible.sync="compareDialogVisible" width="80%" top="5vh">
      <el-form :model="compareForm" :inline="true" label-width="100px" class="mb8">
        <el-form-item label="合并模型">
          <el-select v-model="compareForm.modelId" placeholder="请选择合并模型">
            <el-option
              v-for="item in modelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="期间1">
          <el-input v-model="compareForm.period1" placeholder="请输入期间1" />
        </el-form-item>
        <el-form-item label="期间2">
          <el-input v-model="compareForm.period2" placeholder="请输入期间2" />
        </el-form-item>
        <el-form-item label="报表类型">
          <el-select v-model="compareForm.reportType" placeholder="请选择报表类型">
            <el-option label="资产负债表" value="BALANCE_SHEET" />
            <el-option label="利润表" value="INCOME_STATEMENT" />
            <el-option label="现金流量表" value="CASH_FLOW" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleCompare">对比</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="compareLoading" :data="compareData" border>
        <el-table-column label="项目编码" prop="itemCode" width="120" />
        <el-table-column label="项目名称" prop="itemName" width="200" />
        <el-table-column :label="compareForm.period1 + '金额'" prop="amount1" width="150" align="right">
          <template slot-scope="scope">
            {{ scope.row.amount1 | numberFormat }}
          </template>
        </el-table-column>
        <el-table-column :label="compareForm.period2 + '金额'" prop="amount2" width="150" align="right">
          <template slot-scope="scope">
            {{ scope.row.amount2 | numberFormat }}
          </template>
        </el-table-column>
        <el-table-column label="差异金额" prop="diffAmount" width="150" align="right">
          <template slot-scope="scope">
            <span :class="{'diff-positive': scope.row.diffAmount > 0, 'diff-negative': scope.row.diffAmount < 0}">
              {{ scope.row.diffAmount | numberFormat }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="差异率(%)" prop="diffRate" width="120" align="right">
          <template slot-scope="scope">
            <span :class="{'diff-positive': scope.row.diffRate > 0, 'diff-negative': scope.row.diffRate < 0}">
              {{ scope.row.diffRate | percentFormat }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { getReportList, getReportTypeList, exportReport, compareReports } from '@/api/financialSharing/consolidationReport/consolidatedReportQuery'
import { getModelList } from '@/api/financialSharing/consolidationReport/consolidationModel'
import Pagination from '@/components/Pagination'

export default {
  name: 'ConsolidatedReportQuery',
  components: {
    Pagination
  },
  filters: {
    numberFormat(value) {
      if (!value) return '0.00'
      return parseFloat(value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    },
    percentFormat(value) {
      if (!value) return '0.00'
      return parseFloat(value).toFixed(2)
    }
  },
  data() {
    return {
      // 查询参数
      queryForm: {
        modelId: '',
        period: '',
        reportType: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      },
      // 加载状态
      loading: false,
      // 表格数据
      tableData: [],
      // 总记录数
      total: 0,
      // 模型选项
      modelOptions: [],
      // 统计数据
      reportTypeCount: 0,
      confirmedCount: 0,
      draftCount: 0,
      // 对比对话框
      compareDialogVisible: false,
      compareLoading: false,
      compareForm: {
        modelId: '',
        period1: '',
        period2: '',
        reportType: ''
      },
      compareData: []
    }
  },
  created() {
    this.loadModelOptions()
  },
  methods: {
    /** 加载模型选项 */
    loadModelOptions() {
      getModelList({ status: 'ACTIVE', pageNum: 1, pageSize: 1000 }).then(res => {
        if (res.code === 200 && res.data && res.data.list) {
          this.modelOptions = res.data.list.map(item => ({
            value: item.modelId,
            label: item.modelName
          }))
        }
      })
    },
    /** 模型变化 */
    handleModelChange() {
      if (this.queryForm.modelId && this.queryForm.period) {
        this.loadStatistics()
      }
    },
    /** 期间变化 */
    handlePeriodChange() {
      if (this.queryForm.modelId && this.queryForm.period) {
        this.loadStatistics()
      }
    },
    /** 加载统计数据 */
    loadStatistics() {
      getReportTypeList({
        modelId: this.queryForm.modelId,
        period: this.queryForm.period
      }).then(res => {
        if (res.code === 200) {
          this.reportTypeCount = res.data ? res.data.length : 0
        }
      })

      // 统计已确认和草稿数量
      getReportList({
        modelId: this.queryForm.modelId,
        period: this.queryForm.period,
        pageNum: 1,
        pageSize: 1000
      }).then(res => {
        if (res.code === 200 && res.data && res.data.list) {
          const list = res.data.list
          this.confirmedCount = list.filter(item => item.status === 'CONFIRMED').length
          this.draftCount = list.filter(item => item.status === 'DRAFT').length
        }
      })
    },
    /** 查询按钮 */
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },
    /** 重置按钮 */
    resetQuery() {
      this.queryForm = {
        modelId: '',
        period: '',
        reportType: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      }
      this.tableData = []
      this.total = 0
      this.reportTypeCount = 0
      this.confirmedCount = 0
      this.draftCount = 0
    },
    /** 加载数据 */
    loadData() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getReportList(this.queryForm).then(res => {
        this.loading = false
        if (res.code === 1 || res.code === 200) {
          this.tableData = res.data.list || res.data.records || []
          this.total = res.data.total || res.data.totalRecord || 0
        } else {
          this.$message.error(res.msg || '查询失败')
          this.tableData = []
          this.total = 0
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.tableData = []
        this.total = 0
      })
    },
    /** 导出按钮 */
    handleExport() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }
      if (!this.queryForm.period) {
        this.$message.warning('请先输入期间')
        return
      }
      if (!this.queryForm.reportType) {
        this.$message.warning('请先选择报表类型')
        return
      }

      this.$confirm('确认导出该报表吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const loading = this.$loading({
          lock: true,
          text: '正在导出...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        exportReport({
          modelId: this.queryForm.modelId,
          period: this.queryForm.period,
          reportType: this.queryForm.reportType
        }).then(response => {
          loading.close()

          // 创建下载链接
          const blob = new Blob([response], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
          })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url

          // 获取报表类型名称
          let reportTypeName = ''
          if (this.queryForm.reportType === 'BALANCE_SHEET') {
            reportTypeName = '资产负债表'
          } else if (this.queryForm.reportType === 'INCOME_STATEMENT') {
            reportTypeName = '利润表'
          } else if (this.queryForm.reportType === 'CASH_FLOW') {
            reportTypeName = '现金流量表'
          }

          link.download = reportTypeName + '_' + this.queryForm.period + '.xlsx'
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)

          this.$message.success('导出成功')
        }).catch(() => {
          loading.close()
          this.$message.error('导出失败')
        })
      }).catch(() => {})
    },
    /** 显示对比对话框 */
    showCompareDialog() {
      this.compareDialogVisible = true
      this.compareForm = {
        modelId: this.queryForm.modelId || '',
        period1: '',
        period2: '',
        reportType: this.queryForm.reportType || ''
      }
      this.compareData = []
    },
    /** 对比按钮 */
    handleCompare() {
      if (!this.compareForm.modelId) {
        this.$message.warning('请选择合并模型')
        return
      }
      if (!this.compareForm.period1) {
        this.$message.warning('请输入期间1')
        return
      }
      if (!this.compareForm.period2) {
        this.$message.warning('请输入期间2')
        return
      }
      if (!this.compareForm.reportType) {
        this.$message.warning('请选择报表类型')
        return
      }

      this.compareLoading = true
      compareReports(this.compareForm).then(res => {
        this.compareLoading = false
        if (res.code === 200) {
          this.compareData = res.data || []
          if (this.compareData.length === 0) {
            this.$message.warning('没有找到对比数据')
          } else {
            this.$message.success('对比成功')
          }
        } else {
          this.$message.error(res.msg || '对比失败')
        }
      }).catch(() => {
        this.compareLoading = false
        this.$message.error('对比失败')
      })
    },
    /** 刷新按钮 */
    handleRefresh() {
      this.loadData()
      if (this.queryForm.modelId && this.queryForm.period) {
        this.loadStatistics()
      }
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}

.stat-card {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  margin-right: 15px;
}

.stat-content {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.elimination-amount {
  color: #e6a23c;
  font-weight: bold;
}

.consolidated-amount {
  color: #409eff;
  font-weight: bold;
}

.diff-positive {
  color: #67c23a;
  font-weight: bold;
}

.diff-negative {
  color: #f56c6c;
  font-weight: bold;
}
</style>


