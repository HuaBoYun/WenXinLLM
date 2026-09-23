<template>
  <div class="supervision-instruction-container">
    <!-- 统计概览 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-document" style="color: #409EFF"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ statistics.totalInstructions || 0 }}</div>
              <div class="statistics-label">总指令数</div>
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
              <div class="statistics-value">{{ statistics.completedInstructions || 0 }}</div>
              <div class="statistics-label">已完成</div>
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
              <div class="statistics-value">{{ statistics.pendingInstructions || 0 }}</div>
              <div class="statistics-label">执行中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-time" style="color: #F56C6C"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ statistics.overdueInstructions || 0 }}</div>
              <div class="statistics-label">已逾期</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表展示 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card title="指令状态分布">
          <div ref="statusChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="指令类型分析">
          <div ref="typeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="指令标题">
          <el-input v-model="searchForm.instructionTitle" placeholder="请输入指令标题" clearable></el-input>
        </el-form-item>
        <el-form-item label="目标企业">
          <el-input v-model="searchForm.targetEnterprise" placeholder="请输入目标企业" clearable></el-input>
        </el-form-item>
        <el-form-item label="指令类型">
          <el-select v-model="searchForm.instructionType" placeholder="请选择指令类型" clearable>
            <el-option label="数据报送" value="DATA_SUBMISSION"></el-option>
            <el-option label="风险整改" value="RISK_RECTIFICATION"></el-option>
            <el-option label="合规检查" value="COMPLIANCE_CHECK"></el-option>
            <el-option label="财务审计" value="FINANCIAL_AUDIT"></el-option>
            <el-option label="专项调研" value="SPECIAL_RESEARCH"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="执行状态">
          <el-select v-model="searchForm.executionStatus" placeholder="请选择执行状态" clearable>
            <el-option label="待执行" value="PENDING"></el-option>
            <el-option label="执行中" value="IN_PROGRESS"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已逾期" value="OVERDUE"></el-option>
            <el-option label="已撤销" value="REVOKED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="searchForm.priority" placeholder="请选择优先级" clearable>
            <el-option label="紧急" value="URGENT"></el-option>
            <el-option label="高" value="HIGH"></el-option>
            <el-option label="中" value="MEDIUM"></el-option>
            <el-option label="低" value="LOW"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="下发时间">
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
          <el-button type="success" @click="handleAdd">下发指令</el-button>
          <el-button type="warning" @click="handleBatchIssue">批量下发</el-button>
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
        <el-table-column prop="instructionTitle" label="指令标题" width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="targetEnterprise" label="目标企业" width="150"></el-table-column>
        <el-table-column prop="instructionType" label="指令类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getInstructionTypeTagType(scope.row.instructionType)">
              {{ getInstructionTypeText(scope.row.instructionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80">
          <template slot-scope="scope">
            <el-tag :type="getPriorityTagType(scope.row.priority)" size="small">
              {{ getPriorityText(scope.row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executionStatus" label="执行状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.executionStatus)">
              {{ getStatusText(scope.row.executionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="执行进度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.progress" :status="getProgressStatus(scope.row.executionStatus)"></el-progress>
          </template>
        </el-table-column>
        <el-table-column prop="issueDate" label="下发时间" width="150"></el-table-column>
        <el-table-column prop="deadline" label="截止时间" width="150">
          <template slot-scope="scope">
            <span :class="{ 'overdue-text': isOverdue(scope.row.deadline) }">
              {{ scope.row.deadline }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="responsiblePerson" label="负责人" width="100"></el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handleTrack(scope.row)">跟踪</el-button>
            <el-button size="mini" type="warning" @click="handleConfirm(scope.row)">确认</el-button>
            <el-button size="mini" type="info" @click="handleRevoke(scope.row)">撤销</el-button>
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
    <SupervisionInstructionDialog
      :visible.sync="dialogVisible"
      :dialog-type="dialogType"
      :instruction-data="currentInstruction"
      @refresh="loadTableData"
    />

    <ExecutionTrackingDialog
      :visible.sync="trackingDialogVisible"
      :instruction-data="currentInstruction"
      @refresh="loadTableData"
    />

    <InstructionConfirmDialog
      :visible.sync="confirmDialogVisible"
      :instruction-data="currentInstruction"
      @refresh="loadTableData"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getSupervisionInstructionList,
  getInstructionStatistics,
  getInstructionStatusDistribution,
  getInstructionTypeAnalysis,
  deleteSupervisionInstruction,
  revokeInstruction
} from '@/api/stateAssets/supervisionInstruction'

export default {
  name: 'SupervisionInstruction',
  components: {
    SupervisionInstructionDialog: () => import('./components/SupervisionInstructionDialog'),
    ExecutionTrackingDialog: () => import('./components/ExecutionTrackingDialog'),
    InstructionConfirmDialog: () => import('./components/InstructionConfirmDialog')
  },
  data() {
    return {
      // 统计数据
      statistics: {},
      
      // 搜索表单
      searchForm: {
        instructionTitle: '',
        targetEnterprise: '',
        instructionType: '',
        executionStatus: '',
        priority: '',
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
      trackingDialogVisible: false,
      confirmDialogVisible: false,
      dialogType: 'add',
      currentInstruction: {},
      
      // 图表实例
      statusChart: null,
      typeChart: null
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
    if (this.typeChart) {
      this.typeChart.dispose()
    }
  },
  methods: {
    // 加载统计数据
    async loadStatistics() {
      try {
        const response = await getInstructionStatistics()
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
        
        const response = await getSupervisionInstructionList(params)
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
      this.initTypeChart()
    },

    // 初始化状态分布图表
    async initStatusChart() {
      try {
        const response = await getInstructionStatusDistribution()
        const data = response.data

        this.statusChart = echarts.init(this.$refs.statusChart)
        const option = {
          title: {
            text: '指令状态分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          series: [{
            name: '指令状态',
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

    // 初始化类型分析图表
    async initTypeChart() {
      try {
        const response = await getInstructionTypeAnalysis()
        const data = response.data

        this.typeChart = echarts.init(this.$refs.typeChart)
        const option = {
          title: {
            text: '指令类型分析',
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
            data: data.map(item => item.name)
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            name: '指令数量',
            type: 'bar',
            data: data.map(item => item.value),
            itemStyle: {
              color: '#409EFF'
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
        instructionTitle: '',
        targetEnterprise: '',
        instructionType: '',
        executionStatus: '',
        priority: '',
        dateRange: []
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.currentInstruction = {}
      this.dialogType = 'add'
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentInstruction = { ...row }
      this.dialogType = 'edit'
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.currentInstruction = { ...row }
      this.dialogType = 'view'
      this.dialogVisible = true
    },

    // 跟踪
    handleTrack(row) {
      this.currentInstruction = { ...row }
      this.trackingDialogVisible = true
    },

    // 确认
    handleConfirm(row) {
      this.currentInstruction = { ...row }
      this.confirmDialogVisible = true
    },

    // 撤销
    handleRevoke(row) {
      this.$prompt('请输入撤销原因', '撤销指令', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '撤销原因不能为空'
      }).then(async ({ value }) => {
        try {
          await revokeInstruction(row.id, value)
          this.$message.success('指令已撤销')
          this.loadTableData()
        } catch (error) {
          this.$message.warning('撤销暂未开放')
          console.error('撤销失败:', error)
        }
      })
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该指令吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteSupervisionInstruction(row.id)
          this.$message.success('删除成功')
          this.loadTableData()
        } catch (error) {
          this.$message.error('删除失败')
          console.error('删除失败:', error)
        }
      })
    },

    // 批量下发
    handleBatchIssue() {
      this.$message.info('批量下发功能开发中...')
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
    getInstructionTypeText(type) {
      const typeMap = {
        'DATA_SUBMISSION': '数据报送',
        'RISK_RECTIFICATION': '风险整改',
        'COMPLIANCE_CHECK': '合规检查',
        'FINANCIAL_AUDIT': '财务审计',
        'SPECIAL_RESEARCH': '专项调研'
      }
      return typeMap[type] || type
    },

    getInstructionTypeTagType(type) {
      const typeMap = {
        'DATA_SUBMISSION': 'primary',
        'RISK_RECTIFICATION': 'danger',
        'COMPLIANCE_CHECK': 'warning',
        'FINANCIAL_AUDIT': 'success',
        'SPECIAL_RESEARCH': 'info'
      }
      return typeMap[type] || ''
    },

    getPriorityText(priority) {
      const priorityMap = {
        'URGENT': '紧急',
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return priorityMap[priority] || priority
    },

    getPriorityTagType(priority) {
      const priorityMap = {
        'URGENT': 'danger',
        'HIGH': 'warning',
        'MEDIUM': 'primary',
        'LOW': 'info'
      }
      return priorityMap[priority] || ''
    },

    getStatusText(status) {
      const statusMap = {
        'PENDING': '待执行',
        'IN_PROGRESS': '执行中',
        'COMPLETED': '已完成',
        'OVERDUE': '已逾期',
        'REVOKED': '已撤销'
      }
      return statusMap[status] || status
    },

    getStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'OVERDUE': 'danger',
        'REVOKED': ''
      }
      return statusMap[status] || ''
    },

    getProgressStatus(status) {
      if (status === 'COMPLETED') return 'success'
      if (status === 'OVERDUE') return 'exception'
      return null
    },

    isOverdue(deadline) {
      if (!deadline) return false
      const now = new Date()
      const deadlineDate = new Date(deadline)
      return deadlineDate < now
    }
  }
}
</script>

<style scoped>
.supervision-instruction-container {
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

.overdue-text {
  color: #F56C6C;
  font-weight: bold;
}
</style>
