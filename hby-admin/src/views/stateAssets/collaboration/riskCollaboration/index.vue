<template>
  <div class="risk-collaboration-container">
    <!-- 统计概览 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-warning" style="color: #F56C6C"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ statistics.totalRisks || 0 }}</div>
              <div class="statistics-label">总风险数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-error" style="color: #F56C6C"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ statistics.highRisks || 0 }}</div>
              <div class="statistics-label">高风险</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-warning-outline" style="color: #E6A23C"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ statistics.mediumRisks || 0 }}</div>
              <div class="statistics-label">中风险</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-info" style="color: #67C23A"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ statistics.lowRisks || 0 }}</div>
              <div class="statistics-label">低风险</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表展示 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card title="风险等级分布">
          <div ref="levelChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="风险类型分析">
          <div ref="typeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="企业名称">
          <el-input v-model="searchForm.enterpriseName" placeholder="请输入企业名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="风险类型">
          <el-select v-model="searchForm.riskType" placeholder="请选择风险类型" clearable>
            <el-option label="财务风险" value="FINANCIAL_RISK"></el-option>
            <el-option label="经营风险" value="OPERATIONAL_RISK"></el-option>
            <el-option label="合规风险" value="COMPLIANCE_RISK"></el-option>
            <el-option label="市场风险" value="MARKET_RISK"></el-option>
            <el-option label="信用风险" value="CREDIT_RISK"></el-option>
            <el-option label="技术风险" value="TECHNICAL_RISK"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="searchForm.riskLevel" placeholder="请选择风险等级" clearable>
            <el-option label="高风险" value="HIGH"></el-option>
            <el-option label="中风险" value="MEDIUM"></el-option>
            <el-option label="低风险" value="LOW"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="协同状态">
          <el-select v-model="searchForm.collaborationStatus" placeholder="请选择协同状态" clearable>
            <el-option label="待协同" value="PENDING"></el-option>
            <el-option label="协同中" value="IN_PROGRESS"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已暂停" value="PAUSED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="识别时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增协同</el-button>
          <el-button type="warning" @click="handleBatchShare">批量共享</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        :data="tableData"
        v-loading="tableLoading"
        stripe
        border
        style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="enterpriseName" label="企业名称" width="150"></el-table-column>
        <el-table-column prop="riskName" label="风险名称" width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="riskType" label="风险类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getRiskTypeTagType(scope.row.riskType)">
              {{ getRiskTypeText(scope.row.riskType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelTagType(scope.row.riskLevel)">
              {{ getRiskLevelText(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskScore" label="风险评分" width="100">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.riskScore"
              :color="getRiskScoreColor(scope.row.riskScore)"
              :show-text="false">
            </el-progress>
            <span style="margin-left: 10px;">{{ scope.row.riskScore }}分</span>
          </template>
        </el-table-column>
        <el-table-column prop="collaborationStatus" label="协同状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getCollaborationStatusTagType(scope.row.collaborationStatus)">
              {{ getCollaborationStatusText(scope.row.collaborationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="identificationDate" label="识别时间" width="150"></el-table-column>
        <el-table-column prop="lastUpdateTime" label="更新时间" width="150"></el-table-column>
        <el-table-column prop="responsiblePerson" label="负责人" width="100"></el-table-column>
        <el-table-column label="操作" width="320" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handleShare(scope.row)">共享</el-button>
            <el-button size="mini" type="warning" @click="handleWarning(scope.row)">预警</el-button>
            <el-button size="mini" type="info" @click="handleResponse(scope.row)">应急</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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
    </el-card>

    <!-- 对话框组件 -->
    <RiskCollaborationDialog
      :visible.sync="dialogVisible"
      :dialog-type="dialogType"
      :risk-data="currentRisk"
      @refresh="loadTableData"
    />

    <RiskSharingDialog
      :visible.sync="sharingDialogVisible"
      :risk-data="currentRisk"
      @refresh="loadTableData"
    />

    <RiskWarningDialog
      :visible.sync="warningDialogVisible"
      :risk-data="currentRisk"
      @refresh="loadTableData"
    />

    <EmergencyResponseDialog
      :visible.sync="responseDialogVisible"
      :risk-data="currentRisk"
      @refresh="loadTableData"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getRiskCollaborationList,
  getRiskCollaborationStatistics,
  getRiskLevelDistribution,
  getRiskTypeAnalysis,
  deleteRiskCollaboration,
  shareRiskInformation,
  setCollaborativeWarning,
  initiateEmergencyResponse
} from '@/api/stateAssets/riskCollaboration'

export default {
  name: 'RiskCollaboration',
  components: {
    RiskCollaborationDialog: () => import('./components/RiskCollaborationDialog'),
    RiskSharingDialog: () => import('./components/RiskSharingDialog'),
    RiskWarningDialog: () => import('./components/RiskWarningDialog'),
    EmergencyResponseDialog: () => import('./components/EmergencyResponseDialog')
  },
  data() {
    return {
      // 统计数据
      statistics: {},
      
      // 搜索表单
      searchForm: {
        enterpriseName: '',
        riskType: '',
        riskLevel: '',
        collaborationStatus: '',
        dateRange: []
      },
      
      // 表格数据
      tableData: [],
      tableLoading: false,
      
      // 分页
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 对话框
      dialogVisible: false,
      sharingDialogVisible: false,
      warningDialogVisible: false,
      responseDialogVisible: false,
      dialogType: 'add',
      currentRisk: {},
      
      // 图表实例
      levelChart: null,
      typeChart: null
    }
  },
  mounted() {
    this.loadStatistics()
    this.loadTableData()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.levelChart) {
      this.levelChart.dispose()
    }
    if (this.typeChart) {
      this.typeChart.dispose()
    }
  },
  methods: {
    // 加载统计数据
    async loadStatistics() {
      try {
        const response = await getRiskCollaborationStatistics()
        this.statistics = response.data
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    
    // 加载表格数据
    async loadTableData() {
      this.tableLoading = true
      try {
        const params = {
          ...this.searchForm,
          page: this.pagination.currentPage,
          size: this.pagination.pageSize
        }
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }
        delete params.dateRange
        
        const response = await getRiskCollaborationList(params)
        this.tableData = response.data.records
        this.pagination.total = response.data.total
      } catch (error) {
        this.$message.warning('加载数据暂未开放')
        console.error('加载表格数据失败:', error)
      } finally {
        this.tableLoading = false
      }
    },
    
    // 初始化图表
    async initCharts() {
      await this.$nextTick()
      this.initLevelChart()
      this.initTypeChart()
    },

    // 初始化风险等级分布图表
    async initLevelChart() {
      try {
        const response = await getRiskLevelDistribution()
        const data = response.data

        this.levelChart = echarts.init(this.$refs.levelChart)
        const option = {
          title: {
            text: '风险等级分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          series: [{
            name: '风险等级',
            type: 'pie',
            radius: '50%',
            data: data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }]
        }
        this.levelChart.setOption(option)
      } catch (error) {
        console.error('初始化等级图表失败:', error)
      }
    },

    // 初始化风险类型分析图表
    async initTypeChart() {
      try {
        const response = await getRiskTypeAnalysis()
        const data = response.data

        this.typeChart = echarts.init(this.$refs.typeChart)
        const option = {
          title: {
            text: '风险类型分析',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          xAxis: {
            type: 'category',
            data: data.map(item => item.name),
            axisLabel: {
              rotate: 45
            }
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            name: '风险数量',
            type: 'bar',
            data: data.map(item => item.value),
            itemStyle: {
              color: '#F56C6C'
            }
          }]
        }
        this.typeChart.setOption(option)
      } catch (error) {
        console.error('初始化类型图表失败:', error)
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadTableData()
    },

    // 重置
    handleReset() {
      this.searchForm = {
        enterpriseName: '',
        riskType: '',
        riskLevel: '',
        collaborationStatus: '',
        dateRange: []
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.currentRisk = {}
      this.dialogType = 'add'
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentRisk = { ...row }
      this.dialogType = 'edit'
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.currentRisk = { ...row }
      this.dialogType = 'view'
      this.dialogVisible = true
    },

    // 共享
    async handleShare(row) {
      this.currentRisk = { ...row }
      this.sharingDialogVisible = true
    },

    // 预警
    async handleWarning(row) {
      this.currentRisk = { ...row }
      this.warningDialogVisible = true
    },

    // 应急响应
    async handleResponse(row) {
      this.currentRisk = { ...row }
      this.responseDialogVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该风险协同记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteRiskCollaboration(row.id)
          this.$message.success('删除成功')
          this.loadTableData()
        } catch (error) {
          this.$message.error('删除失败')
          console.error('删除失败:', error)
        }
      })
    },

    // 批量共享
    handleBatchShare() {
      this.$message.info('批量共享功能开发中...')
    },

    // 分页
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadTableData()
    },

    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadTableData()
    },

    // 辅助方法
    getRiskTypeText(type) {
      const typeMap = {
        'FINANCIAL_RISK': '财务风险',
        'OPERATIONAL_RISK': '经营风险',
        'COMPLIANCE_RISK': '合规风险',
        'MARKET_RISK': '市场风险',
        'CREDIT_RISK': '信用风险',
        'TECHNICAL_RISK': '技术风险'
      }
      return typeMap[type] || type
    },

    getRiskTypeTagType(type) {
      const typeMap = {
        'FINANCIAL_RISK': 'danger',
        'OPERATIONAL_RISK': 'warning',
        'COMPLIANCE_RISK': 'primary',
        'MARKET_RISK': 'success',
        'CREDIT_RISK': 'info',
        'TECHNICAL_RISK': ''
      }
      return typeMap[type] || ''
    },

    getRiskLevelText(level) {
      const levelMap = {
        'HIGH': '高风险',
        'MEDIUM': '中风险',
        'LOW': '低风险'
      }
      return levelMap[level] || level
    },

    getRiskLevelTagType(level) {
      const levelMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return levelMap[level] || ''
    },

    getCollaborationStatusText(status) {
      const statusMap = {
        'PENDING': '待协同',
        'IN_PROGRESS': '协同中',
        'COMPLETED': '已完成',
        'PAUSED': '已暂停'
      }
      return statusMap[status] || status
    },

    getCollaborationStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'PAUSED': ''
      }
      return statusMap[status] || ''
    },

    getRiskScoreColor(score) {
      if (score >= 80) return '#F56C6C'
      if (score >= 60) return '#E6A23C'
      return '#67C23A'
    }
  }
}
</script>

<style scoped>
.risk-collaboration-container {
  padding: 20px;
}

.statistics-row {
  margin-bottom: 20px;
}

.statistics-card {
  height: 100px;
}

.statistics-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.statistics-icon {
  font-size: 40px;
  margin-right: 20px;
}

.statistics-info {
  flex: 1;
}

.statistics-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.statistics-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.charts-row {
  margin-bottom: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 0;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>
