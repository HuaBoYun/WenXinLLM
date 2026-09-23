<template>
  <div class="app-container equity-page" :style="themeVars">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-refresh"></i><span>股权变动监控</span></div>
      <div class="page-header-desc">监控股权转让、增减资、质押等变动情况</div>
    </div>
    <!-- 统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-s-data" style="color: #409eff"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.totalChanges || 0 }}</div>
              <div class="statistics-label">变动总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-warning" style="color: #e6a23c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.pendingChanges || 0 }}</div>
              <div class="statistics-label">待审核</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-circle-check" style="color: #67c23a"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.approvedChanges || 0 }}</div>
              <div class="statistics-label">已审核</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-close" style="color: #f56c6c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.rejectedChanges || 0 }}</div>
              <div class="statistics-label">已拒绝</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card shadow="never" class="mb-20">
      <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="企业名称" prop="enterpriseName">
          <el-input
            v-model="queryParams.enterpriseName"
            placeholder="请输入企业名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="变动类型" prop="changeType">
          <el-select v-model="queryParams.changeType" placeholder="请选择变动类型" clearable style="width: 150px">
            <el-option label="股权转让" value="TRANSFER"></el-option>
            <el-option label="增资扩股" value="INCREASE"></el-option>
            <el-option label="减资" value="DECREASE"></el-option>
            <el-option label="股权质押" value="PLEDGE"></el-option>
            <el-option label="股权解押" value="UNPLEDGE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态" prop="approvalStatus">
          <el-select v-model="queryParams.approvalStatus" placeholder="请选择审核状态" clearable style="width: 150px">
            <el-option label="待审核" value="PENDING"></el-option>
            <el-option label="已审核" value="APPROVED"></el-option>
            <el-option label="已拒绝" value="REJECTED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="warningLevel">
          <el-select v-model="queryParams.warningLevel" placeholder="请选择风险等级" clearable style="width: 150px">
            <el-option label="高风险" value="HIGH"></el-option>
            <el-option label="中风险" value="MEDIUM"></el-option>
            <el-option label="低风险" value="LOW"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="变动时间">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <div class="table-header">
        <div class="table-title">股权变动监控列表</div>
        <div class="table-actions">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增变动记录</el-button>
          <el-button type="warning" icon="el-icon-refresh" @click="handleBatchAnalyze" :disabled="!multipleSelection.length">
            批量分析
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="changesList"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="被投资企业" prop="investeeEnterpriseName" min-width="150" show-overflow-tooltip />
        <el-table-column label="变动类型" prop="changeType" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getChangeTypeTag(scope.row.changeType)" size="mini">
              {{ getChangeTypeText(scope.row.changeType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="变动前比例" prop="beforeShareholdingRatio" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.beforeShareholdingRatio }}%</template>
        </el-table-column>
        <el-table-column label="变动后比例" prop="afterShareholdingRatio" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.afterShareholdingRatio }}%</template>
        </el-table-column>
        <el-table-column label="变动金额(万)" prop="changeAmount" width="110" align="right">
          <template slot-scope="scope">
            {{ scope.row.changeAmount ? Number(scope.row.changeAmount).toLocaleString() : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="转让方" prop="transferorEnterpriseName" width="130" show-overflow-tooltip />
        <el-table-column label="受让方" prop="transfereeEnterpriseName" width="130" show-overflow-tooltip />
        <el-table-column label="变动日期" prop="changeDate" width="100" align="center" />
        <el-table-column label="审核状态" prop="approvalStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.approvalStatus)" size="mini">
              {{ getStatusText(scope.row.approvalStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="warningLevel" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRiskTag(scope.row.warningLevel)" size="mini" v-if="scope.row.warningLevel">
              {{ {LOW:'低风险',MEDIUM:'中风险',HIGH:'高风险',CRITICAL:'严重'}[scope.row.warningLevel] || scope.row.warningLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)" icon="el-icon-view">
              查看
            </el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)" icon="el-icon-edit">
              编辑
            </el-button>
            <el-button size="mini" type="text" @click="handleAnalyze(scope.row)" icon="el-icon-data-analysis">
              分析
            </el-button>
            <el-button
              size="mini"
              type="text"
              style="color: #67c23a"
              @click="handleApprove(scope.row)"
              icon="el-icon-check"
              v-if="scope.row.approvalStatus === 'PENDING'"
            >
              审核
            </el-button>
            <el-button size="mini" type="text" style="color: #f56c6c" @click="handleDelete(scope.row)" icon="el-icon-delete">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 变动详情对话框 -->
    <EquityChangeDialog
      :visible.sync="dialogVisible"
      :change-data="currentRow"
      :dialog-type="dialogType"
      @refresh="getList"
    />

    <!-- 影响分析对话框 -->
    <EquityImpactAnalysisDialog
      :visible.sync="analysisDialogVisible"
      :change-data="currentRow"
    />

    <!-- 审核对话框 -->
    <EquityApprovalDialog
      :visible.sync="approvalDialogVisible"
      :change-data="currentRow"
      @refresh="getList"
    />
  </div>
</template>

<script>
import {
  getEquityChangesList,
  deleteEquityChange,
  getEquityChangesStatistics,
  batchAnalyzeEquityChanges,
  exportEquityChangesData
} from '@/api/stateAssets/equityChanges'
import Pagination from '@/components/Pagination'
import { mapGetters } from 'vuex'
import EquityChangeDialog from './components/EquityChangeDialog'
import EquityImpactAnalysisDialog from './components/EquityImpactAnalysisDialog'
import EquityApprovalDialog from './components/EquityApprovalDialog'
import { investThemeMixin } from '../../themeMixin'

export default {
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
  name: 'EquityChanges',
  mixins: [investThemeMixin],
  components: {
    Pagination,
    EquityChangeDialog,
    EquityImpactAnalysisDialog,
    EquityApprovalDialog
  },
  data() {
    return {
      loading: true,
      changesList: [],
      total: 0,
      multipleSelection: [],
      dateRange: [],
      statistics: {},
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        enterpriseName: '',
        changeType: '',
        approvalStatus: '',
        warningLevel: '',
        startDate: '',
        endDate: ''
      },
      dialogVisible: false,
      analysisDialogVisible: false,
      approvalDialogVisible: false,
      dialogType: 'add',
      currentRow: {}
    }
  },
  created() {
    this.getStatistics()
    this.getList()
  },
  methods: {
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = { ...this.queryParams }
        if (this.dateRange && this.dateRange.length === 2) {
          params.startDate = this.dateRange[0]
          params.endDate = this.dateRange[1]
        }
        const response = await getEquityChangesList(params)
        if (response.result === 200) {
          this.changesList = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '获取列表失败')
        }
      } catch (error) {
        console.error('获取股权变动列表失败:', error)
        this.$message.error('获取股权变动列表失败')
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getStatistics() {
      try {
        const response = await getEquityChangesStatistics()
        if (response.result === 200 && response.data) {
          const d = response.data
          this.statistics = {
            totalChanges: d.totalChanges || d.TOTALCHANGES || 0,
            pendingChanges: d.pendingApprovals || d.PENDINGAPPROVALS || 0,
            approvedChanges: d.approvedChanges || d.APPROVEDCHANGES || 0,
            rejectedChanges: d.warningChanges || d.WARNINGCHANGES || 0,
          }
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },

    // 搜索
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    // 重置
    resetQuery() {
      this.dateRange = []
      this.$refs.queryForm.resetFields()
      this.handleQuery()
    },

    // 新增
    handleAdd() {
      this.currentRow = {}
      this.dialogType = 'add'
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.currentRow = { ...row }
      this.dialogType = 'view'
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentRow = { ...row }
      this.dialogType = 'edit'
      this.dialogVisible = true
    },

    // 分析影响
    handleAnalyze(row) {
      this.currentRow = { ...row }
      this.analysisDialogVisible = true
    },

    // 审核
    handleApprove(row) {
      this.currentRow = { ...row }
      this.approvalDialogVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该股权变动记录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteEquityChange(row.changeId)
          if (response.result === 200) {
            this.$message.success('删除成功')
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },

    // 批量分析
    async handleBatchAnalyze() {
      const loading = this.$loading({ lock: true, text: '正在执行批量分析...', background: 'rgba(0, 0, 0, 0.7)' })
      try {
        const ids = this.multipleSelection.map(item => item.changeId)
        const response = await batchAnalyzeEquityChanges({ changeIds: ids })
        if (response.result === 200) {
          this.$message.success(`批量分析完成，共分析 ${this.multipleSelection.length} 条记录`)
          this.getList()
        } else {
          this.$message.error(response.msg || '批量分析失败')
        }
      } catch (error) {
        this.$message.error('批量分析失败')
      } finally {
        loading.close()
      }
    },

    // 导出
    async handleExport() {
      const loading = this.$loading({ lock: true, text: '正在导出数据...', background: 'rgba(0, 0, 0, 0.7)' })
      try {
        const res = await exportEquityChangesData(this.queryParams)
        loading.close()
        if (res) {
          // axios blob响应拦截器返回完整response对象，取res.data获取实际blob
          const blobData = res.data || res
          const blob = new Blob([blobData], { type: 'text/csv;charset=UTF-8' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `股权变动数据_${new Date().getTime()}.csv`
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        }
      } catch (error) {
        loading.close()
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    // 多选
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 获取变动类型标签
    getChangeTypeTag(type) {
      const tagMap = {
        'TRANSFER': 'primary',
        'INCREASE': 'success',
        'DECREASE': 'warning',
        'PLEDGE': 'info',
        'UNPLEDGE': 'success'
      }
      return tagMap[type] || 'info'
    },

    // 获取变动类型文本
    getChangeTypeText(type) {
      const textMap = {
        'TRANSFER': '股权转让',
        'INCREASE': '增资扩股',
        'DECREASE': '减资',
        'PLEDGE': '股权质押',
        'UNPLEDGE': '股权解押'
      }
      return textMap[type] || type
    },

    // 获取状态标签
    getStatusTag(status) {
      const tagMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return tagMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'PENDING': '待审核',
        'APPROVED': '已审核',
        'REJECTED': '已拒绝'
      }
      return textMap[status] || status
    },

    // 获取风险标签
    getRiskTag(level) {
      const tagMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return tagMap[level] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.equity-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-card { border-radius: 6px; }

.statistics-card {
  .statistics-content { display: flex; align-items: center;
    .statistics-icon { font-size: 24px; margin-right: 15px; }
    .statistics-info {
      .statistics-number { font-size: 24px; font-weight: bold; color: #303133; }
      .statistics-label { font-size: 14px; color: #909399; margin-top: 5px; }
    }
  }
}
.table-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;
  .table-title { font-size: 16px; font-weight: bold; color: #303133; }
}
.text-success { color: #67c23a; }
.text-danger { color: #f56c6c; }
.mb-20 { margin-bottom: 20px; }
</style>
