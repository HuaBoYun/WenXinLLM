<template>
  <div class="investment-decision-container equity-page" :style="themeVars">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-s-finance"></i><span>投资决策管理</span></div>
      <div class="page-header-desc">管理投资项目审批、风险评估与进度跟踪</div>
    </div>
    <!-- 统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-s-finance" style="color: #409EFF;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.totalProjects }}</div>
              <div class="stats-label">投资项目总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-time" style="color: #E6A23C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.pendingApproval }}</div>
              <div class="stats-label">待审批项目</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-success" style="color: #67C23A;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.approvedProjects }}</div>
              <div class="stats-label">已批准项目</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-warning" style="color: #F56C6C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.riskProjects }}</div>
              <div class="stats-label">风险项目</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card shadow="never" class="mb-20">
      <div slot="header">
        <span>查询条件</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="resetSearchForm"
        >
          重置
        </el-button>
      </div>
      
      <el-form
        ref="searchForm"
        :model="searchForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="项目名称">
          <el-input
            v-model="searchForm.projectName"
            placeholder="请输入项目名称"
            clearable
            style="width: 200px;"
          />
        </el-form-item>
        
        <el-form-item label="投资企业">
          <el-input
            v-model="searchForm.companyName"
            placeholder="请输入投资企业名称"
            clearable
            style="width: 200px;"
          />
        </el-form-item>

        <el-form-item label="项目状态">
          <el-select
            v-model="searchForm.projectStatus"
            placeholder="请选择项目状态"
            clearable
            style="width: 150px;"
          >
            <el-option label="规划中" value="PLANNING"></el-option>
            <el-option label="已审批" value="APPROVED"></el-option>
            <el-option label="执行中" value="EXECUTING"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已暂停" value="SUSPENDED"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="投资类型">
          <el-select
            v-model="searchForm.investType"
            placeholder="请选择投资类型"
            clearable
            style="width: 120px;"
          >
            <el-option label="股权" value="EQUITY"></el-option>
            <el-option label="债权" value="DEBT"></el-option>
            <el-option label="混合" value="MIXED"></el-option>
            <el-option label="基金" value="FUND"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="投资金额(万元)">
          <el-input
            v-model="searchForm.minAmount"
            placeholder="最小金额"
            style="width: 110px;"
          />
          <span style="margin: 0 8px;">-</span>
          <el-input
            v-model="searchForm.maxAmount"
            placeholder="最大金额"
            style="width: 110px;"
          />
        </el-form-item>

        <el-form-item label="审批日期">
          <el-date-picker
            v-model="searchForm.approvalDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 240px;"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i> 查询
          </el-button>
          <el-button @click="resetSearchForm">
            <i class="el-icon-refresh"></i> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card shadow="never" class="mb-20">
      <el-button type="primary" @click="handleAdd">
        <i class="el-icon-plus"></i> 新增投资项目
      </el-button>
      <el-button
        type="success"
        :disabled="selectedProjects.length === 0"
        @click="handleBatchApproval"
      >
        <i class="el-icon-check"></i> 批量审批
      </el-button>
      <el-button
        type="warning"
        :disabled="selectedProjects.length === 0"
        @click="handleBatchRiskAssessment"
      >
        <i class="el-icon-warning"></i> 批量风险评估
      </el-button>
      <el-button type="info" @click="handleExport">
        <i class="el-icon-download"></i> 导出数据
      </el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <div slot="header">
        <span>投资决策项目列表</span>
        <span class="table-count">（共 {{ total }} 条）</span>
      </div>
      
      <el-table
        v-loading="loading"
        :data="projectsList"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />

        <el-table-column label="项目名称" prop="projectName" min-width="200">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.projectName }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column label="投资企业" prop="companyName" min-width="180" />

        <el-table-column label="被投资企业" prop="targetCompany" min-width="180" />

        <el-table-column label="投资金额" prop="investAmount" width="120" align="right">
          <template slot-scope="scope">
            {{ scope.row.investAmount }}万元
          </template>
        </el-table-column>

        <el-table-column label="投资类型" prop="investType" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getInvestTypeTag(scope.row.investType)" size="mini">
              {{ getInvestTypeText(scope.row.investType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="项目状态" prop="projectStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.projectStatus)" size="mini">
              {{ getStatusText(scope.row.projectStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="是否主业" prop="isMainBiz" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isMainBiz === 'Y' ? 'success' : 'info'" size="mini">
              {{ scope.row.isMainBiz === 'Y' ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="审批日期" prop="approvalDate" width="120" />

        <el-table-column label="预期收益率" prop="expectedReturn" width="120" align="center">
          <template slot-scope="scope">
            {{ scope.row.expectedReturn != null ? scope.row.expectedReturn + '%' : '-' }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              v-if="scope.row.approvalStatus !== 'APPROVED'"
              size="mini"
              type="success"
              @click="handleApproval(scope.row)"
            >审批</el-button>
            <el-dropdown @command="handleCommand($event, scope.row)">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="riskAssessment">风险评估</el-dropdown-item>
                <el-dropdown-item command="progressTracking">进度跟踪</el-dropdown-item>
                <el-dropdown-item command="effectAnalysis">效果分析</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <Pagination
          v-show="total > 0"
          :total="total"
          :page.sync="searchForm.pageNum"
          :limit.sync="searchForm.pageSize"
          @pagination="getProjectsList"
        />
      </div>
    </el-card>

    <!-- 对话框组件 -->
    <InvestmentProjectDialog
      :visible.sync="projectDialogVisible"
      :project-data="currentProject"
      :dialog-type="dialogType"
      @refresh="handleRefresh"
    />

    <InvestmentApprovalDialog
      :visible.sync="approvalDialogVisible"
      :project-data="currentProject"
      @refresh="handleRefresh"
    />

    <RiskAssessmentDialog
      :visible.sync="riskDialogVisible"
      :project-data="currentProject"
      @refresh="handleRefresh"
    />

    <ProgressTrackingDialog
      :visible.sync="progressDialogVisible"
      :project-data="currentProject"
    />

    <EffectAnalysisDialog
      :visible.sync="effectDialogVisible"
      :project-data="currentProject"
    />
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { mapGetters } from 'vuex'
import InvestmentProjectDialog from './components/InvestmentProjectDialog'
import InvestmentApprovalDialog from './components/InvestmentApprovalDialog'
import RiskAssessmentDialog from './components/RiskAssessmentDialog'
import ProgressTrackingDialog from './components/ProgressTrackingDialog'
import EffectAnalysisDialog from './components/EffectAnalysisDialog'
import {
  getInvestmentProjectsList,
  getInvestmentStatistics,
  deleteInvestmentProject,
  exportInvestmentData,
  batchApproveProjects,
  batchRiskAssessment
} from '@/api/stateAssets/investmentDecision'
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
  name: 'InvestmentDecision',
  mixins: [investThemeMixin],
  components: {
    Pagination,
    InvestmentProjectDialog,
    InvestmentApprovalDialog,
    RiskAssessmentDialog,
    ProgressTrackingDialog,
    EffectAnalysisDialog
  },
  data() {
    return {
      loading: false,
      projectsList: [],
      selectedProjects: [],
      total: 0,
      statistics: {
        totalProjects: 0,
        pendingApproval: 0,
        approvedProjects: 0,
        riskProjects: 0
      },
      searchForm: {
        projectName: '',
        companyName: '',
        projectStatus: '',
        investType: '',
        minAmount: '',
        maxAmount: '',
        approvalDateRange: [],
        pageNum: 1,
        pageSize: 10
      },
      projectDialogVisible: false,
      approvalDialogVisible: false,
      riskDialogVisible: false,
      progressDialogVisible: false,
      effectDialogVisible: false,
      currentProject: {},
      dialogType: 'add'
    }
  },
  created() {
    this.getStatistics()
    this.getProjectsList()
  },
  methods: {
    // 获取统计数据
    async getStatistics() {
      try {
        const response = await getInvestmentStatistics()
        if (response.result === 200 && response.data) {
          const d = response.data
          this.statistics = {
            totalProjects: d.totalProjects || 0,
            pendingApproval: d.pendingApproval || d.executingCount || 0,
            approvedProjects: d.approvedProjects || d.completedCount || 0,
            riskProjects: d.riskProjects || d.highRiskCount || 0
          }
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },

    // 获取项目列表
    async getProjectsList() {
      this.loading = true
      try {
        const params = { ...this.searchForm }
        // 处理审批日期范围
        if (params.approvalDateRange && params.approvalDateRange.length === 2) {
          params.startDate = params.approvalDateRange[0]
          params.endDate = params.approvalDateRange[1]
        }
        delete params.approvalDateRange

        const response = await getInvestmentProjectsList(params)
        if (response.result === 200) {
          this.projectsList = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '获取项目列表失败')
        }
      } catch (error) {
        console.error('获取项目列表失败:', error)
        this.$message.error('获取项目列表失败')
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.searchForm.pageNum = 1
      this.getProjectsList()
    },

    // 重置搜索表单
    resetSearchForm() {
      this.$refs.searchForm.resetFields()
      this.searchForm = {
        projectName: '',
        companyName: '',
        projectStatus: '',
        investType: '',
        minAmount: '',
        maxAmount: '',
        approvalDateRange: [],
        pageNum: 1,
        pageSize: 10
      }
      this.getProjectsList()
    },

    // 刷新列表和统计
    handleRefresh() {
      this.getProjectsList()
      this.getStatistics()
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedProjects = selection
    },

    // 新增项目
    handleAdd() {
      this.currentProject = {}
      this.dialogType = 'add'
      this.projectDialogVisible = true
    },

    // 查看项目
    handleView(row) {
      this.currentProject = { ...row }
      this.dialogType = 'view'
      this.projectDialogVisible = true
    },

    // 编辑项目
    handleEdit(row) {
      this.currentProject = { ...row }
      this.dialogType = 'edit'
      this.projectDialogVisible = true
    },

    // 审批项目
    handleApproval(row) {
      this.currentProject = { ...row }
      this.approvalDialogVisible = true
    },

    // 批量审批
    async handleBatchApproval() {
      if (this.selectedProjects.length === 0) {
        this.$message.warning('请选择要审批的项目')
        return
      }
      try {
        const ids = this.selectedProjects.map(item => item.projectId)
        const response = await batchApproveProjects({ projectIds: ids })
        if (response.result === 200) {
          this.$message.success('批量审批成功')
          this.getProjectsList()
          this.getStatistics()
        } else {
          this.$message.error(response.msg || '批量审批失败')
        }
      } catch (error) {
        this.$message.error('批量审批失败')
      }
    },

    // 批量风险评估
    async handleBatchRiskAssessment() {
      if (this.selectedProjects.length === 0) {
        this.$message.warning('请选择要评估的项目')
        return
      }
      try {
        const ids = this.selectedProjects.map(item => item.projectId)
        const response = await batchRiskAssessment({ projectIds: ids })
        if (response.result === 200) {
          this.$message.success('批量风险评估完成')
          this.getProjectsList()
        } else {
          this.$message.error(response.msg || '批量风险评估失败')
        }
      } catch (error) {
        this.$message.error('批量风险评估失败')
      }
    },

    // 导出数据
    async handleExport() {
      const loading = this.$loading({ lock: true, text: '正在导出数据...', background: 'rgba(0, 0, 0, 0.7)' })
      try {
        const exportParams = {}
        if (this.searchForm.projectName) exportParams.projectName = this.searchForm.projectName
        if (this.searchForm.companyName) exportParams.companyName = this.searchForm.companyName
        if (this.searchForm.projectStatus) exportParams.projectStatus = this.searchForm.projectStatus
        if (this.searchForm.investType) exportParams.investType = this.searchForm.investType
        const res = await exportInvestmentData(exportParams)
        loading.close()
        // 拦截器对 blob 返回整个 response 对象，真实数据在 res.data
        const blobData = res && res.data ? res.data : res
        if (blobData) {
          const blob = new Blob([blobData], { type: 'application/vnd.ms-excel' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `投资决策项目数据_${new Date().getTime()}.xls`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        }
      } catch (error) {
        loading.close()
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    // 下拉菜单命令处理
    handleCommand(command, row) {
      switch (command) {
        case 'riskAssessment':
          this.currentProject = { ...row }
          this.riskDialogVisible = true
          break
        case 'progressTracking':
          this.currentProject = { ...row }
          this.progressDialogVisible = true
          break
        case 'effectAnalysis':
          this.currentProject = { ...row }
          this.effectDialogVisible = true
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 删除项目
    handleDelete(row) {
      this.$confirm('确认删除该投资项目？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteInvestmentProject({ projectId: row.projectId })
          if (response.result === 200) {
            this.$message.success('删除成功')
            this.getProjectsList()
            this.getStatistics()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },

    // 获取状态标签类型
    getStatusTag(status) {
      const tagMap = {
        'PLANNING': 'info',
        'APPROVED': 'success',
        'EXECUTING': 'primary',
        'COMPLETED': 'success',
        'SUSPENDED': 'warning'
      }
      return tagMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'PLANNING': '规划中',
        'APPROVED': '已审批',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成',
        'SUSPENDED': '已暂停'
      }
      return textMap[status] || status
    },

    // 获取投资类型标签类型
    getInvestTypeTag(type) {
      const tagMap = {
        'EQUITY': '',
        'DEBT': 'success',
        'MIXED': 'warning',
        'FUND': 'primary'
      }
      return tagMap[type] || 'info'
    },

    // 获取投资类型文本
    getInvestTypeText(type) {
      const textMap = {
        'EQUITY': '股权',
        'DEBT': '债权',
        'MIXED': '混合',
        'FUND': '基金'
      }
      return textMap[type] || type
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

.mb-20 { margin-bottom: 20px; }
.stats-card {
  .stats-content { display: flex; align-items: center;
    .stats-icon { font-size: 40px; margin-right: 20px; }
    .stats-info {
      .stats-value { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }
      .stats-label { font-size: 14px; color: #909399; margin-top: 5px; }
    }
  }
}
.table-count { color: #909399; font-size: 14px; }
.pagination-container { margin-top: 20px; text-align: right; }
</style>
