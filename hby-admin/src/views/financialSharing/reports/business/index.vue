<template>
  <div class="business-analysis-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-data-analysis"></i>
          业务分析
        </h1>
        <p class="page-description">提供多维度业务分析和数据洞察，支持成本、收入、财务指标等分析</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="createAnalysis">
          新建分析
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportAnalysis">
          导出分析
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="refreshData">
          刷新
        </el-button>
      </div>
    </div>

    <!-- 分析类型选择 -->
    <div class="analysis-types">
      <el-row :gutter="24">
        <el-col :span="6" v-for="type in analysisTypes" :key="type.key">
          <div 
            class="analysis-type-card" 
            :class="{ active: selectedType === type.key }"
            @click="selectAnalysisType(type.key)">
            <div class="type-icon">
              <i :class="type.icon"></i>
            </div>
            <div class="type-content">
              <h3>{{ type.title }}</h3>
              <p>{{ type.description }}</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 分析参数设置 -->
    <div class="analysis-params">
      <el-form :model="analysisForm" :inline="true" label-width="80px">
        <el-form-item label="分析期间">
          <el-date-picker
            v-model="analysisForm.dateRange"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份"
            format="yyyy-MM"
            value-format="yyyy-MM">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="对比期间">
          <el-date-picker
            v-model="analysisForm.compareRange"
            type="monthrange"
            range-separator="至"
            start-placeholder="对比开始"
            end-placeholder="对比结束"
            format="yyyy-MM"
            value-format="yyyy-MM">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="分析维度">
          <el-select v-model="analysisForm.dimension" placeholder="请选择分析维度">
            <el-option label="按月份" value="month"></el-option>
            <el-option label="按季度" value="quarter"></el-option>
            <el-option label="按年度" value="year"></el-option>
            <el-option label="按部门" value="department"></el-option>
            <el-option label="按产品" value="product"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="performAnalysis">开始分析</el-button>
          <el-button @click="resetParams">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 分析结果展示 -->
    <div v-if="analysisResult" class="analysis-results">
      <!-- 成本分析 -->
      <div v-if="selectedType === 'cost'" class="cost-analysis">
        <CostAnalysisChart :data="analysisResult" />
      </div>

      <!-- 收入分析 -->
      <div v-if="selectedType === 'revenue'" class="revenue-analysis">
        <RevenueAnalysisChart :data="analysisResult" />
      </div>

      <!-- 财务指标分析 -->
      <div v-if="selectedType === 'indicators'" class="indicators-analysis">
        <FinancialIndicatorsChart :data="analysisResult" />
      </div>

      <!-- 经营分析 -->
      <div v-if="selectedType === 'operational'" class="operational-analysis">
        <OperationalAnalysisChart :data="analysisResult" />
      </div>
    </div>

    <!-- 分析报告列表 -->
    <div class="analysis-reports">
      <h3 class="section-title">历史分析报告</h3>
      <el-table
        v-loading="loading"
        :data="reportList"
        stripe
        border
        style="width: 100%">
        <el-table-column prop="reportName" label="报告名称" min-width="200"></el-table-column>
        <el-table-column prop="analysisType" label="分析类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getAnalysisTypeTag(scope.row.analysisType)">
              {{ getAnalysisTypeName(scope.row.analysisType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="period" label="分析期间" width="180"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
        <el-table-column prop="creator" label="创建人" width="120"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="viewReport(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="editReport(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" @click="exportReport(scope.row)">导出</el-button>
            <el-button size="mini" type="text" style="color: #f56c6c" @click="deleteReport(scope.row)">删除</el-button>
          </template>
        </el-table-column>
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
          :total="pagination.total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getBusinessAnalysis,
  getCostAnalysis,
  getRevenueAnalysis,
  getFinancialIndicators,
  getOperationalAnalysis
} from '@/api/financialSharing/reports'
import CostAnalysisChart from './components/CostAnalysisChart'
import RevenueAnalysisChart from './components/RevenueAnalysisChart'
import FinancialIndicatorsChart from './components/FinancialIndicatorsChart'
import OperationalAnalysisChart from './components/OperationalAnalysisChart'

export default {
  name: 'BusinessAnalysisIndex',
  components: {
    CostAnalysisChart,
    RevenueAnalysisChart,
    FinancialIndicatorsChart,
    OperationalAnalysisChart
  },
  data() {
    return {
      loading: false,
      selectedType: 'cost',
      analysisResult: null,
      reportList: [],
      analysisTypes: [
        {
          key: 'cost',
          title: '成本分析',
          description: '分析成本构成、变动趋势和控制效果',
          icon: 'el-icon-coin'
        },
        {
          key: 'revenue',
          title: '收入分析',
          description: '分析收入来源、增长趋势和结构变化',
          icon: 'el-icon-trophy'
        },
        {
          key: 'indicators',
          title: '财务指标',
          description: '分析盈利能力、偿债能力和营运能力',
          icon: 'el-icon-data-line'
        },
        {
          key: 'operational',
          title: '经营分析',
          description: '分析经营效率、市场表现和竞争力',
          icon: 'el-icon-pie-chart'
        }
      ],
      analysisForm: {
        dateRange: [],
        compareRange: [],
        dimension: 'month'
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      }
    }
  },
  mounted() {
    this.loadReportList()
    this.initDefaultParams()
  },
  methods: {
    initDefaultParams() {
      const now = new Date()
      const currentMonth = now.getFullYear() + '-' + String(now.getMonth() + 1).padStart(2, '0')
      const lastMonth = now.getFullYear() + '-' + String(now.getMonth()).padStart(2, '0')
      
      this.analysisForm.dateRange = [lastMonth, currentMonth]
      this.analysisForm.compareRange = []
    },
    selectAnalysisType(type) {
      this.selectedType = type
      this.analysisResult = null
    },
    async performAnalysis() {
      if (!this.analysisForm.dateRange || this.analysisForm.dateRange.length === 0) {
        this.$message.warning('请选择分析期间')
        return
      }

      this.loading = true
      try {
        const params = {
          analysisType: this.selectedType,
          startPeriod: this.analysisForm.dateRange[0],
          endPeriod: this.analysisForm.dateRange[1],
          dimension: this.analysisForm.dimension
        }

        if (this.analysisForm.compareRange && this.analysisForm.compareRange.length > 0) {
          params.compareStartPeriod = this.analysisForm.compareRange[0]
          params.compareEndPeriod = this.analysisForm.compareRange[1]
        }

        let response
        switch (this.selectedType) {
          case 'cost':
            response = await getCostAnalysis(params)
            break
          case 'revenue':
            response = await getRevenueAnalysis(params)
            break
          case 'indicators':
            response = await getFinancialIndicators(params)
            break
          case 'operational':
            response = await getOperationalAnalysis(params)
            break
          default:
            response = await getBusinessAnalysis(params)
        }

        if (response.code === 200) {
          this.analysisResult = response.data
          this.$message.success('分析完成')
        }
      } catch (error) {
        this.$message.error('分析失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    resetParams() {
      this.analysisForm = {
        dateRange: [],
        compareRange: [],
        dimension: 'month'
      }
      this.analysisResult = null
      this.initDefaultParams()
    },
    async loadReportList() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }
        const response = await getBusinessAnalysis(params)
        if (response.code === 1) {
          this.reportList = response.data.tlist || response.data.list || response.data || []
          this.pagination.total = response.data.total || this.reportList.length
        } else {
          this.$message.error(response.msg || '加载报告列表失败')
        }
      } catch (error) {
        this.$message.error('加载报告列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    createAnalysis() {
      this.$message.success('新建分析功能已触发')
    },
    exportAnalysis() {
      this.$message.success('导出分析功能已触发')
    },
    refreshData() {
      this.loadReportList()
    },
    viewReport(row) {
      this.$message.success('查看报告功能已触发')
    },
    editReport(row) {
      this.$message.success('编辑报告功能已触发')
    },
    exportReport(row) {
      this.$message.success('导出报告功能已触发')
    },
    deleteReport(row) {
      this.$message.success('删除报告功能已触发')
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadReportList()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadReportList()
    },
    getAnalysisTypeTag(type) {
      const tags = {
        cost: 'primary',
        revenue: 'success',
        indicators: 'warning',
        operational: 'info'
      }
      return tags[type] || 'default'
    },
    getAnalysisTypeName(type) {
      const names = {
        cost: '成本分析',
        revenue: '收入分析',
        indicators: '财务指标',
        operational: '经营分析'
      }
      return names[type] || type
    }
  }
}
</script>

<style lang="scss" scoped>
.business-analysis-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #67c23a;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.analysis-types {
  margin-bottom: 24px;

  .analysis-type-card {
    background: white;
    border-radius: 12px;
    padding: 20px;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    &.active {
      border-color: #409eff;
      background: linear-gradient(135deg, #409eff 0%, #36a3f7 100%);
      color: white;

      .type-icon i {
        color: white;
      }
    }

    .type-icon {
      width: 48px;
      height: 48px;
      border-radius: 12px;
      background: #f0f9ff;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 24px;
        color: #409eff;
      }
    }

    .type-content {
      h3 {
        font-size: 16px;
        font-weight: 600;
        margin: 0 0 4px 0;
      }

      p {
        font-size: 12px;
        margin: 0;
        opacity: 0.8;
      }
    }
  }
}

.analysis-params {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.analysis-results {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.analysis-reports {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .section-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 16px 0;
    padding-bottom: 8px;
    border-bottom: 2px solid #67c23a;
  }

  .el-table {
    border-radius: 8px;
    overflow: hidden;
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
