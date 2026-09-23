<template>
  <div class="app-container invest-page">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-monitor"></i><span>核查工作台</span></div>
      <div class="page-header-desc">巡视核查人员工作台，管理待处理任务与调查记录</div>
    </div>
    <el-card shadow="never">
      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-label">待处理任务</div><div class="stat-value warning">{{ stats.pendingCount || 0 }}</div></div></el-card></el-col>
        <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-label">进行中</div><div class="stat-value primary">{{ stats.inProgressCount || 0 }}</div></div></el-card></el-col>
        <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-label">已完成</div><div class="stat-value success">{{ stats.completedCount || 0 }}</div></div></el-card></el-col>
        <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-label">逾期任务</div><div class="stat-value danger">{{ stats.overdueCount || 0 }}</div></div></el-card></el-col>
      </el-row>
    </el-card>
    <el-card shadow="never" style="margin-top: 10px">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="我的任务" name="myTask">
          <el-table v-loading="loading" :data="list" border style="width: 100%">
            <el-table-column label="任务名称" prop="taskName" min-width="160" show-overflow-tooltip />
            <el-table-column label="任务类型" prop="taskType" width="100" align="center" />
            <el-table-column label="目标企业" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="状态" prop="taskStatus" width="80" align="center">
              <template slot-scope="scope"><el-tag :type="{ PENDING: 'info', IN_PROGRESS: 'warning', COMPLETED: 'success', OVERDUE: 'danger' }[scope.row.taskStatus]" size="small">{{ { PENDING: '待处理', IN_PROGRESS: '进行中', COMPLETED: '已完成', OVERDUE: '逾期' }[scope.row.taskStatus] || scope.row.taskStatus }}</el-tag></template>
            </el-table-column>
            <el-table-column label="截止日期" prop="deadline" width="120" align="center" />
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="handleProcess(scope.row)">处理</el-button>
                <el-button size="mini" type="text" @click="handleUploadEvidence(scope.row)">上传证据</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="调查记录" name="record"><el-empty description="暂无调查记录" /></el-tab-pane>
        <el-tab-pane label="报告编写" name="report"><el-empty description="暂无报告" /></el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
import { getInvestigationTaskList } from '@/api/stateAssets/investigationPenetration'
export default {
  name: 'InvestigationWorkbench',
  data() { return { loading: false, list: [], stats: {}, activeTab: 'myTask' } },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getInvestigationTaskList({ pageNumber: 1, pageSize: 50, assignee: 'current' })
        if (res && res.result === 200) this.list = (res.data && res.data.tlist) || []
      } catch (e) { this.list = [] } finally { this.loading = false }
    },
    handleProcess(row) { this.$message.info('处理任务: ' + row.taskName) },
    handleUploadEvidence(row) { this.$message.info('上传证据: ' + row.taskName) },
  },
}
</script>
<style scoped>.stat-item { text-align: center; padding: 10px; } .stat-label { color: #909399; font-size: 14px; } .stat-value { font-size: 28px; font-weight: bold; margin-top: 8px; } .stat-value.warning { color: #E6A23C; } .stat-value.primary { color: #409EFF; } .stat-value.success { color: #67C23A; } .stat-value.danger { color: #F56C6C; }</style>
<style lang="scss" scoped>
.invest-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, #d46b08 0%, #fa8c16 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: #fff7e6 !important; }
::v-deep .el-card { border-radius: 6px; }
</style>

