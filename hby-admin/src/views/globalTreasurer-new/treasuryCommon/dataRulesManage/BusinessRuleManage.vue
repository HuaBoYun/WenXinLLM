<template>
  <div class="business-rule-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-s-operation"></i>
            业务规则管理
          </h2>
          <p class="page-description">管理业务流程规则配置，包括审批规则、风控规则和业务逻辑配置</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增规则
          </el-button>
          <el-button type="success" icon="el-icon-upload2" @click="handleImport">
            导入规则
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出规则
          </el-button>
        </div>
      </div>
    </div>

    <!-- 规则统计卡片 -->
    <div class="rule-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-s-operation"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总规则数</div>
                <div class="card-value">{{ totalRules }}</div>
                <div class="card-change">已配置规则</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">生效规则</div>
                <div class="card-value">{{ activeRules }}</div>
                <div class="card-change positive">正常运行</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon pending-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">待审核规则</div>
                <div class="card-value">{{ pendingRules }}</div>
                <div class="card-change negative">需要审核</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon update-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">最后更新</div>
                <div class="card-value">{{ lastUpdateTime }}</div>
                <div class="card-change">规则配置</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="规则编码">
            <el-input
              v-model="listQuery.ruleCode"
              placeholder="请输入规则编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="规则名称">
            <el-input
              v-model="listQuery.ruleName"
              placeholder="请输入规则名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="规则类型">
            <el-select
              v-model="listQuery.ruleType"
              placeholder="请选择规则类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="审批规则" value="APPROVAL" />
              <el-option label="风控规则" value="RISK_CONTROL" />
              <el-option label="业务规则" value="BUSINESS" />
              <el-option label="计算规则" value="CALCULATION" />
              <el-option label="验证规则" value="VALIDATION" />
            </el-select>
          </el-form-item>
          <el-form-item label="适用模块">
            <el-select
              v-model="listQuery.moduleCode"
              placeholder="请选择适用模块"
              clearable
              style="width: 150px;"
            >
              <el-option label="账户管理" value="ACCOUNT" />
              <el-option label="资金管理" value="FUND" />
              <el-option label="投资理财" value="INVESTMENT" />
              <el-option label="风险管理" value="RISK" />
            </el-select>
          </el-form-item>
          <el-form-item label="规则状态">
            <el-select
              v-model="listQuery.ruleStatus"
              placeholder="请选择规则状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="启用" value="ENABLED" />
              <el-option label="禁用" value="DISABLED" />
              <el-option label="测试中" value="TESTING" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      style="width: 100%; margin-top: 12px;"
    >
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column label="规则编码" prop="ruleCode" align="center" width="150" show-overflow-tooltip />
      <el-table-column label="规则名称" prop="ruleName" align="center" min-width="160" show-overflow-tooltip />
      <el-table-column label="规则类型" align="center" width="110">
        <template slot-scope="scope">
          <el-tag :type="getRuleTypeColor(scope.row.ruleType)" size="small">{{ getRuleTypeName(scope.row.ruleType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="适用模块" align="center" width="110">
        <template slot-scope="scope">
          <span>{{ getModuleName(scope.row.moduleCode) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="优先级" align="center" width="80">
        <template slot-scope="scope">
          <el-tag :type="getPriorityColor(scope.row.priority)" size="small">{{ getPriorityName(scope.row.priority) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="生效时间" align="center" width="170">
        <template slot-scope="scope">
          <span>{{ formatDateTime(scope.row.effectiveTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="失效时间" align="center" width="170">
        <template slot-scope="scope">
          <span>{{ formatDateTime(scope.row.expireTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则状态" align="center" width="90">
        <template slot-scope="scope">
          <el-tag :type="getRuleStatusColor(scope.row.ruleStatus)" size="small">{{ getRuleStatusName(scope.row.ruleStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row, scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 业务规则对话框 -->
    <business-rule-dialog
      :visible.sync="ruleDialogVisible"
      :rule-data="currentRule"
      :status="dialogStatus"
      @close="handleDialogClose"
      @success="handleDialogSuccess"
    />

    <!-- 批量导入对话框 -->
    <business-rule-import-dialog
      :visible.sync="importDialogVisible"
      @close="handleImportDialogClose"
      @import-success="handleImportSuccess"
    />
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import BusinessRuleDialog from './components/BusinessRuleDialog'
import BusinessRuleImportDialog from './components/BusinessRuleImportDialog'
import { getBusinessRuleList, createBusinessRule, updateBusinessRule, deleteBusinessRule, getBusinessRuleStatistics } from '@/api/globalTreasurer/xjgl/dataRulesManage/businessRule'
import { parseTime } from '@/utils'

export default {
  name: 'BusinessRuleManage',
  components: {
    Pagination,
    BusinessRuleDialog,
    BusinessRuleImportDialog
  },
  directives: { waves },
  filters: {
    parseTime,
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        ruleCode: undefined,
        ruleName: undefined,
        ruleType: undefined,
        moduleCode: undefined,
        ruleStatus: undefined
      },
      totalRules: 0,
      enabledRules: 0,
      pendingRules: 0,
      activeRules: 0,
      testingRules: 0,
      lastUpdateTime: '',
      ruleDialogVisible: false,
      importDialogVisible: false,
      dialogStatus: 'create',
      currentRule: {}
    }
  },
  watch: {},
  created() {
    this.getList()
  },
  methods: {
    formatDateTime(time) {
      if (!time) return '--'
      return parseTime(time, '{y}-{m}-{d} {h}:{i}:{s}')
    },
    getRuleTypeName(type) {
      const typeMap = {
        'APPROVAL': '审批规则',
        'RISK_CONTROL': '风控规则',
        'BUSINESS': '业务规则',
        'CALCULATION': '计算规则',
        'VALIDATION': '验证规则'
      }
      return typeMap[type] || type
    },
    getRuleTypeColor(type) {
      const colorMap = {
        'APPROVAL': 'primary',
        'RISK_CONTROL': 'danger',
        'BUSINESS': 'success',
        'CALCULATION': 'warning',
        'VALIDATION': 'info'
      }
      return colorMap[type] || 'default'
    },
    getModuleName(code) {
      const moduleMap = {
        'ACCOUNT': '账户管理',
        'FUND': '资金管理',
        'INVESTMENT': '投资理财',
        'RISK': '风险管理'
      }
      return moduleMap[code] || code
    },
    getPriorityName(priority) {
      const priorityMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return priorityMap[priority] || priority
    },
    getPriorityColor(priority) {
      const colorMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return colorMap[priority] || 'default'
    },
    getRuleStatusName(status) {
      const statusMap = {
        'ENABLED': '启用',
        'DISABLED': '禁用',
        'TESTING': '测试中'
      }
      return statusMap[status] || status
    },
    getRuleStatusColor(status) {
      const colorMap = {
        'ENABLED': 'success',
        'DISABLED': 'danger',
        'TESTING': 'warning'
      }
      return colorMap[status] || 'default'
    },
    async getList(paginationPayload) {
      if (paginationPayload) {
        this.listQuery.page = paginationPayload.page
        this.listQuery.limit = paginationPayload.limit
      }
      this.listLoading = true
      try {
        const response = await getBusinessRuleList(this.listQuery)
        if (response && response.code === 1) {
          const rawData = response.data || {}
          this.list = rawData.tlist || rawData.list || []
          this.total = Number(rawData.totalRecord || rawData.total) || this.list.length
        } else {
          this.$message.error((response && response.msg) || '获取数据失败')
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取业务规则数据列表失败:', error)
        this.$message.error('获取数据失败')
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
      this.fetchStatistics()
    },

    /**
     * 获取业务规则统计信息，失败时用列表数据兜底
     */
    async fetchStatistics() {
      try {
        const response = await getBusinessRuleStatistics()
        if (response && response.code === 1 && response.data) {
          const stats = response.data
          this.totalRules = Number(stats.totalRules) || 0
          this.activeRules = Number(stats.enabledRules || stats.activeRules) || 0
          this.pendingRules = Number(stats.pendingRules) || 0
          this.lastUpdateTime = stats.lastUpdateTime || new Date().toLocaleString()
        } else {
          this.updateStatisticsFromList()
        }
      } catch (error) {
        this.updateStatisticsFromList()
      }
    },
    updateStatisticsFromList() {
      this.totalRules = this.list.length
      this.activeRules = this.list.filter(r => r.ruleStatus === 'ENABLED').length
      this.pendingRules = this.list.filter(r => r.ruleStatus === 'TESTING').length
      this.lastUpdateTime = new Date().toLocaleString()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        ruleCode: undefined,
        ruleName: undefined,
        ruleType: undefined,
        moduleCode: undefined,
        ruleStatus: undefined
      }
      this.getList()
    },
    handleCreate() {
      this.dialogStatus = 'create'
      this.currentRule = {}
      this.ruleDialogVisible = true
    },
    handleUpdate(row) {
      this.dialogStatus = 'edit'
      this.currentRule = { ...row }
      this.ruleDialogVisible = true
    },
    handleImport() {
      this.importDialogVisible = true
    },
    handleExport() {
      this.exportRules()
    },
    async exportRules() {
      try {
        // 模拟导出数据
        this.$message({
          type: 'success',
          message: '导出成功'
        })

        // 创建下载链接
        const dataStr = JSON.stringify(this.list, null, 2)
        const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr)

        const exportFileDefaultName = `业务规则数据_${new Date().toISOString().split('T')[0]}.json`

        const linkElement = document.createElement('a')
        linkElement.setAttribute('href', dataUri)
        linkElement.setAttribute('download', exportFileDefaultName)
        linkElement.click()

      } catch (error) {
        this.$message.error('导出失败，请重试')
      }
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const response = await deleteBusinessRule({ ruleId: row.ruleId })
          if (response && response.code === 1) {
            this.$notify({ title: '成功', message: '删除成功', type: 'success', duration: 2000 })
            this.getList()
          } else {
            this.$message.error((response && response.msg) || '删除失败')
          }
        } catch (error) {
          console.error('删除业务规则失败:', error)
          this.$message.error('删除失败')
        }
      })
    },
    handleDialogClose() {
      this.ruleDialogVisible = false
    },
    async handleDialogSuccess(ruleData) {
      try {
        let response
        if (this.dialogStatus === 'create') {
          response = await createBusinessRule(ruleData)
        } else {
          response = await updateBusinessRule(ruleData)
        }
        if (response && response.code === 1) {
          this.$message.success(this.dialogStatus === 'create' ? '创建成功' : '更新成功')
          this.ruleDialogVisible = false
          this.getList()
        } else {
          this.$message.error((response && response.msg) || '操作失败')
        }
      } catch (error) {
        console.error('保存业务规则失败:', error)
        this.$message.error('保存失败，请重试')
      }
    },
    handleImportDialogClose() {
      this.importDialogVisible = false
    },
    handleImportSuccess(count) {
      this.$message.success(`成功导入 ${count} 条数据`)
      this.getList()
      this.importDialogVisible = false
    }
  }
}
</script>
