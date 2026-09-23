<template>
  <div class="data-collaboration-container">
    <!-- 统计概览 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-connection" style="color: #409EFF"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ statistics.totalCollaborations || 0 }}</div>
              <div class="statistics-label">总协同数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-check" style="color: #67C23A"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ statistics.successfulSync || 0 }}</div>
              <div class="statistics-label">同步成功</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-loading" style="color: #E6A23C"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ statistics.pendingSync || 0 }}</div>
              <div class="statistics-label">待同步</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-warning" style="color: #F56C6C"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ statistics.failedSync || 0 }}</div>
              <div class="statistics-label">同步失败</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表展示 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card title="协同状态分布">
          <div ref="statusChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="协同趋势分析">
          <div ref="trendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="企业名称">
          <el-input v-model="searchForm.enterpriseName" placeholder="请输入企业名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="协同类型">
          <el-select v-model="searchForm.collaborationType" placeholder="请选择协同类型" clearable>
            <el-option label="数据报送" value="DATA_SUBMISSION"></el-option>
            <el-option label="数据同步" value="DATA_SYNC"></el-option>
            <el-option label="质量检查" value="QUALITY_CHECK"></el-option>
            <el-option label="标准统一" value="STANDARD_UNIFY"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="协同状态">
          <el-select v-model="searchForm.status" placeholder="请选择协同状态" clearable>
            <el-option label="进行中" value="IN_PROGRESS"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已失败" value="FAILED"></el-option>
            <el-option label="已暂停" value="PAUSED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
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
          <el-button type="warning" @click="handleBatchSync">批量同步</el-button>
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
        <el-table-column prop="enterpriseName" label="企业名称" width="200"></el-table-column>
        <el-table-column prop="collaborationType" label="协同类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getCollaborationTypeTagType(scope.row.collaborationType)">
              {{ getCollaborationTypeText(scope.row.collaborationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataVolume" label="数据量" width="100">
          <template slot-scope="scope">
            {{ formatDataVolume(scope.row.dataVolume) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="协同状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="进度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.progress" :status="getProgressStatus(scope.row.status)"></el-progress>
          </template>
        </el-table-column>
        <el-table-column prop="qualityScore" label="质量评分" width="100">
          <template slot-scope="scope">
            <el-rate
              v-model="scope.row.qualityScore"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}分">
            </el-rate>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="150"></el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="150"></el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handleSync(scope.row)">同步</el-button>
            <el-button size="mini" type="warning" @click="handleQualityCheck(scope.row)">质检</el-button>
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
    <DataCollaborationDialog
      :visible.sync="dialogVisible"
      :dialog-type="dialogType"
      :collaboration-data="currentCollaboration"
      @refresh="loadTableData"
    />

    <SyncProgressDialog
      :visible.sync="syncDialogVisible"
      :collaboration-data="currentCollaboration"
      @refresh="loadTableData"
    />

    <QualityCheckDialog
      :visible.sync="qualityDialogVisible"
      :collaboration-data="currentCollaboration"
      @refresh="loadTableData"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getDataCollaborationList,
  getCollaborationStatistics,
  getCollaborationStatusDistribution,
  getCollaborationTrend,
  deleteDataCollaboration,
  executeSyncTask,
  executeQualityCheck
} from '@/api/stateAssets/dataCollaboration'

export default {
  name: 'DataCollaboration',
  components: {
    DataCollaborationDialog: () => import('./components/DataCollaborationDialog'),
    SyncProgressDialog: () => import('./components/SyncProgressDialog'),
    QualityCheckDialog: () => import('./components/QualityCheckDialog')
  },
  data() {
    return {
      // 统计数据
      statistics: {},
      
      // 搜索表单
      searchForm: {
        enterpriseName: '',
        collaborationType: '',
        status: '',
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
      syncDialogVisible: false,
      qualityDialogVisible: false,
      dialogType: 'add',
      currentCollaboration: {},
      
      // 图表实例
      statusChart: null,
      trendChart: null
    }
  },
  mounted() {
    this.loadStatistics()
    this.loadTableData()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.statusChart) {
      this.statusChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    // 加载统计数据
    async loadStatistics() {
      try {
        const response = await getCollaborationStatistics()
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
        
        const response = await getDataCollaborationList(params)
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
      this.initStatusChart()
      this.initTrendChart()
    },
    
    // 初始化状态分布图表
    async initStatusChart() {
      try {
        const response = await getCollaborationStatusDistribution()
        const data = response.data
        
        this.statusChart = echarts.init(this.$refs.statusChart)
        const option = {
          title: {
            text: '协同状态分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          series: [{
            name: '协同状态',
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
        this.statusChart.setOption(option)
      } catch (error) {
        console.error('初始化状态图表失败:', error)
      }
    },

    // 初始化趋势图表
    async initTrendChart() {
      try {
        const response = await getCollaborationTrend({ days: 30 })
        const data = response.data

        this.trendChart = echarts.init(this.$refs.trendChart)
        const option = {
          title: {
            text: '协同趋势分析',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            data: data.dates
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            name: '协同数量',
            type: 'line',
            data: data.counts,
            smooth: true
          }]
        }
        this.trendChart.setOption(option)
      } catch (error) {
        console.error('初始化趋势图表失败:', error)
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
        collaborationType: '',
        status: '',
        dateRange: []
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.currentCollaboration = {}
      this.dialogType = 'add'
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentCollaboration = { ...row }
      this.dialogType = 'edit'
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.currentCollaboration = { ...row }
      this.dialogType = 'view'
      this.dialogVisible = true
    },

    // 同步
    async handleSync(row) {
      try {
        await executeSyncTask({ collaborationId: row.id })
        this.$message.success('同步任务已启动')
        this.currentCollaboration = { ...row }
        this.syncDialogVisible = true
        this.loadTableData()
      } catch (error) {
        this.$message.warning('启动同步暂未开放')
        console.error('同步失败:', error)
      }
    },

    // 质量检查
    async handleQualityCheck(row) {
      try {
        await executeQualityCheck({ collaborationId: row.id })
        this.$message.success('质量检查已启动')
        this.currentCollaboration = { ...row }
        this.qualityDialogVisible = true
        this.loadTableData()
      } catch (error) {
        this.$message.warning('启动质量检查暂未开放')
        console.error('质量检查失败:', error)
      }
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该协同记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteDataCollaboration(row.id)
          this.$message.success('删除成功')
          this.loadTableData()
        } catch (error) {
          this.$message.error('删除失败')
          console.error('删除失败:', error)
        }
      })
    },

    // 批量同步
    handleBatchSync() {
      this.$message.info('批量同步功能开发中...')
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
    getCollaborationTypeText(type) {
      const typeMap = {
        'DATA_SUBMISSION': '数据报送',
        'DATA_SYNC': '数据同步',
        'QUALITY_CHECK': '质量检查',
        'STANDARD_UNIFY': '标准统一'
      }
      return typeMap[type] || type
    },

    getCollaborationTypeTagType(type) {
      const typeMap = {
        'DATA_SUBMISSION': 'primary',
        'DATA_SYNC': 'success',
        'QUALITY_CHECK': 'warning',
        'STANDARD_UNIFY': 'info'
      }
      return typeMap[type] || ''
    },

    getStatusText(status) {
      const statusMap = {
        'IN_PROGRESS': '进行中',
        'COMPLETED': '已完成',
        'FAILED': '已失败',
        'PAUSED': '已暂停'
      }
      return statusMap[status] || status
    },

    getStatusTagType(status) {
      const statusMap = {
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'PAUSED': 'info'
      }
      return statusMap[status] || ''
    },

    getProgressStatus(status) {
      if (status === 'COMPLETED') return 'success'
      if (status === 'FAILED') return 'exception'
      return null
    },

    formatDataVolume(volume) {
      if (volume >= 1000000) {
        return (volume / 1000000).toFixed(1) + 'M'
      } else if (volume >= 1000) {
        return (volume / 1000).toFixed(1) + 'K'
      }
      return volume.toString()
    }
  }
}
</script>

<style scoped>
.data-collaboration-container {
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
