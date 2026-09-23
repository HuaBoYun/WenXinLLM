<template>
  <div class="app-container">
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-setting" style="color: #409eff"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.totalRules || 0 }}</div>
              <div class="statistics-label">监管规则总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-data-analysis" style="color: #67c23a"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.totalIndicators || 0 }}</div>
              <div class="statistics-label">监管指标数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-connection" style="color: #e6a23c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.activeProcesses || 0 }}</div>
              <div class="statistics-label">活跃流程数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-warning" style="color: #f56c6c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.pendingTasks || 0 }}</div>
              <div class="statistics-label">待处理任务</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能标签页 -->
    <el-card>
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 监管规则配置 -->
        <el-tab-pane label="监管规则配置" name="rules">
          <div class="tab-content">
            <!-- 查询表单 -->
            <el-form :model="rulesQuery" ref="rulesForm" :inline="true" label-width="100px" class="mb-20">
              <el-form-item label="规则名称">
                <el-input v-model="rulesQuery.ruleName" placeholder="请输入规则名称" style="width: 200px"></el-input>
              </el-form-item>
              <el-form-item label="规则类型">
                <el-select v-model="rulesQuery.ruleType" placeholder="请选择规则类型" clearable style="width: 150px">
                  <el-option label="股权监管" value="EQUITY"></el-option>
                  <el-option label="资产监管" value="ASSET"></el-option>
                  <el-option label="财务监管" value="FINANCIAL"></el-option>
                  <el-option label="风险监管" value="RISK"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="规则状态">
                <el-select v-model="rulesQuery.status" placeholder="请选择状态" clearable style="width: 120px">
                  <el-option label="启用" value="ACTIVE"></el-option>
                  <el-option label="禁用" value="INACTIVE"></el-option>
                  <el-option label="草稿" value="DRAFT"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getRulesList" icon="el-icon-search">查询</el-button>
                <el-button @click="resetRulesQuery" icon="el-icon-refresh">重置</el-button>
                <el-button type="success" @click="handleAddRule" icon="el-icon-plus">新增规则</el-button>
                <el-button type="info" @click="handleExportRules" icon="el-icon-download">导出</el-button>
              </el-form-item>
            </el-form>

            <!-- 规则列表表格 -->
            <el-table v-loading="rulesLoading" :data="rulesList" stripe border style="width: 100%">
              <el-table-column type="selection" width="55" align="center"></el-table-column>
              <el-table-column prop="ruleName" label="规则名称" min-width="200" show-overflow-tooltip></el-table-column>
              <el-table-column prop="ruleType" label="规则类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getRuleTypeTag(scope.row.ruleType)">
                    {{ getRuleTypeText(scope.row.ruleType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="priority" label="优先级" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getPriorityTag(scope.row.priority)">
                    {{ scope.row.priority }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusTag(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="160" align="center"></el-table-column>
              <el-table-column label="操作" width="250" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewRule(scope.row)" icon="el-icon-view">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEditRule(scope.row)" icon="el-icon-edit">编辑</el-button>
                  <el-button size="mini" type="success" @click="handleTestRule(scope.row)" icon="el-icon-cpu">测试</el-button>
                  <el-button size="mini" type="danger" @click="handleDeleteRule(scope.row)" icon="el-icon-delete">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页组件 -->
            <pagination
              v-show="rulesTotal > 0"
              :total="rulesTotal"
              :page.sync="rulesQuery.pageNum"
              :limit.sync="rulesQuery.pageSize"
              @pagination="getRulesList"
            />
          </div>
        </el-tab-pane>

        <!-- 监管指标配置 -->
        <el-tab-pane label="监管指标配置" name="indicators">
          <div class="tab-content">
            <!-- 查询表单 -->
            <el-form :model="indicatorsQuery" ref="indicatorsForm" :inline="true" label-width="100px" class="mb-20">
              <el-form-item label="指标名称">
                <el-input v-model="indicatorsQuery.indicatorName" placeholder="请输入指标名称" style="width: 200px"></el-input>
              </el-form-item>
              <el-form-item label="指标分类">
                <el-select v-model="indicatorsQuery.category" placeholder="请选择分类" clearable style="width: 150px">
                  <el-option label="财务指标" value="FINANCIAL"></el-option>
                  <el-option label="风险指标" value="RISK"></el-option>
                  <el-option label="效率指标" value="EFFICIENCY"></el-option>
                  <el-option label="合规指标" value="COMPLIANCE"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getIndicatorsList" icon="el-icon-search">查询</el-button>
                <el-button @click="resetIndicatorsQuery" icon="el-icon-refresh">重置</el-button>
                <el-button type="success" @click="handleAddIndicator" icon="el-icon-plus">新增指标</el-button>
                <el-button type="info" @click="handleExportIndicators" icon="el-icon-download">导出</el-button>
              </el-form-item>
            </el-form>

            <!-- 指标列表表格 -->
            <el-table v-loading="indicatorsLoading" :data="indicatorsList" stripe border style="width: 100%">
              <el-table-column type="selection" width="55" align="center"></el-table-column>
              <el-table-column prop="indicatorName" label="指标名称" min-width="200" show-overflow-tooltip></el-table-column>
              <el-table-column prop="category" label="指标分类" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getCategoryTag(scope.row.category)">
                    {{ getCategoryText(scope.row.category) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="unit" label="计量单位" width="100" align="center"></el-table-column>
              <el-table-column prop="threshold" label="预警阈值" width="120" align="center"></el-table-column>
              <el-table-column prop="frequency" label="监控频率" width="120" align="center"></el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusTag(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="250" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewIndicator(scope.row)" icon="el-icon-view">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEditIndicator(scope.row)" icon="el-icon-edit">编辑</el-button>
                  <el-button size="mini" type="success" @click="handleCalculateIndicator(scope.row)" icon="el-icon-data-analysis">计算</el-button>
                  <el-button size="mini" type="danger" @click="handleDeleteIndicator(scope.row)" icon="el-icon-delete">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页组件 -->
            <pagination
              v-show="indicatorsTotal > 0"
              :total="indicatorsTotal"
              :page.sync="indicatorsQuery.pageNum"
              :limit.sync="indicatorsQuery.pageSize"
              @pagination="getIndicatorsList"
            />
          </div>
        </el-tab-pane>

        <!-- 监管流程配置 -->
        <el-tab-pane label="监管流程配置" name="processes">
          <div class="tab-content">
            <!-- 查询表单 -->
            <el-form :model="processesQuery" ref="processesForm" :inline="true" label-width="100px" class="mb-20">
              <el-form-item label="流程名称">
                <el-input v-model="processesQuery.processName" placeholder="请输入流程名称" style="width: 200px"></el-input>
              </el-form-item>
              <el-form-item label="流程类型">
                <el-select v-model="processesQuery.processType" placeholder="请选择类型" clearable style="width: 150px">
                  <el-option label="审批流程" value="APPROVAL"></el-option>
                  <el-option label="监管流程" value="SUPERVISION"></el-option>
                  <el-option label="预警流程" value="WARNING"></el-option>
                  <el-option label="处置流程" value="DISPOSAL"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getProcessesList" icon="el-icon-search">查询</el-button>
                <el-button @click="resetProcessesQuery" icon="el-icon-refresh">重置</el-button>
                <el-button type="success" @click="handleAddProcess" icon="el-icon-plus">新增流程</el-button>
                <el-button type="info" @click="handleExportProcesses" icon="el-icon-download">导出</el-button>
              </el-form-item>
            </el-form>

            <!-- 流程列表表格 -->
            <el-table v-loading="processesLoading" :data="processesList" stripe border style="width: 100%">
              <el-table-column type="selection" width="55" align="center"></el-table-column>
              <el-table-column prop="processName" label="流程名称" min-width="200" show-overflow-tooltip></el-table-column>
              <el-table-column prop="processType" label="流程类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getProcessTypeTag(scope.row.processType)">
                    {{ getProcessTypeText(scope.row.processType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="stepCount" label="步骤数量" width="100" align="center"></el-table-column>
              <el-table-column prop="avgDuration" label="平均耗时" width="120" align="center"></el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusTag(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="280" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewProcess(scope.row)" icon="el-icon-view">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEditProcess(scope.row)" icon="el-icon-edit">编辑</el-button>
                  <el-button size="mini" type="success" @click="handleDesignProcess(scope.row)" icon="el-icon-connection">设计</el-button>
                  <el-button size="mini" type="warning" @click="handleDeployProcess(scope.row)" icon="el-icon-upload2">部署</el-button>
                  <el-button size="mini" type="danger" @click="handleDeleteProcess(scope.row)" icon="el-icon-delete">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页组件 -->
            <pagination
              v-show="processesTotal > 0"
              :total="processesTotal"
              :page.sync="processesQuery.pageNum"
              :limit.sync="processesQuery.pageSize"
              @pagination="getProcessesList"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 对话框组件 -->
    <SupervisionRuleDialog
      :visible.sync="ruleDialogVisible"
      :rule-data="currentRule"
      :dialog-type="dialogType"
      @refresh="getRulesList"
    />

    <SupervisionIndicatorDialog
      :visible.sync="indicatorDialogVisible"
      :indicator-data="currentIndicator"
      :dialog-type="dialogType"
      @refresh="getIndicatorsList"
    />

    <SupervisionProcessDialog
      :visible.sync="processDialogVisible"
      :process-data="currentProcess"
      :dialog-type="dialogType"
      @refresh="getProcessesList"
    />
  </div>
</template>

<script>
import { getSupervisionConfigStatistics, getSupervisionRulesList, getSupervisionIndicatorsList, getSupervisionProcessesList } from '@/api/stateAssets/supervisionConfig'
import Pagination from '@/components/Pagination'
import SupervisionRuleDialog from './components/SupervisionRuleDialog'
import SupervisionIndicatorDialog from './components/SupervisionIndicatorDialog'
import SupervisionProcessDialog from './components/SupervisionProcessDialog'

export default {
  name: 'SupervisionConfig',
  components: {
    Pagination,
    SupervisionRuleDialog,
    SupervisionIndicatorDialog,
    SupervisionProcessDialog
  },
  data() {
    return {
      activeTab: 'rules',
      statistics: {},
      
      // 监管规则相关
      rulesLoading: false,
      rulesList: [],
      rulesTotal: 0,
      rulesQuery: {
        pageNum: 1,
        pageSize: 10,
        ruleName: '',
        ruleType: '',
        status: ''
      },
      
      // 监管指标相关
      indicatorsLoading: false,
      indicatorsList: [],
      indicatorsTotal: 0,
      indicatorsQuery: {
        pageNum: 1,
        pageSize: 10,
        indicatorName: '',
        category: ''
      },
      
      // 监管流程相关
      processesLoading: false,
      processesList: [],
      processesTotal: 0,
      processesQuery: {
        pageNum: 1,
        pageSize: 10,
        processName: '',
        processType: ''
      },
      
      // 对话框相关
      ruleDialogVisible: false,
      indicatorDialogVisible: false,
      processDialogVisible: false,
      dialogType: 'add',
      currentRule: {},
      currentIndicator: {},
      currentProcess: {}
    }
  },
  created() {
    this.getStatistics()
    this.getRulesList()
  },
  methods: {
    // 获取统计数据
    getStatistics() {
      getSupervisionConfigStatistics().then(response => {
        if (response.code === 1) {
          this.statistics = response.data || {}
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
      })
    },

    // 监管规则相关方法
    getRulesList() {
      this.rulesLoading = true
      getSupervisionRulesList(this.rulesQuery).then(response => {
        if (response.code === 1) {
          this.rulesList = response.data.list || []
          this.rulesTotal = response.data.total || 0
        } else {
          this.$message.error(response.msg || '获取监管规则列表失败')
        }
        this.rulesLoading = false
      }).catch(error => {
        console.error('获取监管规则列表异常:', error)
        this.$message.warning('获取数据暂未开放')
        this.rulesLoading = false
      })
    },

    resetRulesQuery() {
      this.$refs.rulesForm.resetFields()
      this.rulesQuery = {
        pageNum: 1,
        pageSize: 10,
        ruleName: '',
        ruleType: '',
        status: ''
      }
      this.getRulesList()
    },

    handleAddRule() {
      this.currentRule = {}
      this.dialogType = 'add'
      this.ruleDialogVisible = true
    },

    handleViewRule(row) {
      this.currentRule = { ...row }
      this.dialogType = 'view'
      this.ruleDialogVisible = true
    },

    handleEditRule(row) {
      this.currentRule = { ...row }
      this.dialogType = 'edit'
      this.ruleDialogVisible = true
    },

    handleTestRule(row) {
      this.$message.success(`正在测试规则：${row.ruleName}`)
    },

    handleDeleteRule(row) {
      this.$confirm(`确定要删除规则"${row.ruleName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.getRulesList()
      }).catch(() => {
          // 用户取消操作
        })
      },

    handleExportRules() {
      this.$message.success('正在导出监管规则数据...')
    },

    // 监管指标相关方法
    getIndicatorsList() {
      this.indicatorsLoading = true
      getSupervisionIndicatorsList(this.indicatorsQuery).then(response => {
        if (response.code === 1) {
          this.indicatorsList = response.data.list || []
          this.indicatorsTotal = response.data.total || 0
        } else {
          this.$message.error(response.msg || '获取监管指标列表失败')
        }
        this.indicatorsLoading = false
      }).catch(error => {
        console.error('获取监管指标列表异常:', error)
        this.$message.warning('获取数据暂未开放')
        this.indicatorsLoading = false
      })
    },

    resetIndicatorsQuery() {
      this.$refs.indicatorsForm.resetFields()
      this.indicatorsQuery = {
        pageNum: 1,
        pageSize: 10,
        indicatorName: '',
        category: ''
      }
      this.getIndicatorsList()
    },

    handleAddIndicator() {
      this.currentIndicator = {}
      this.dialogType = 'add'
      this.indicatorDialogVisible = true
    },

    handleViewIndicator(row) {
      this.currentIndicator = { ...row }
      this.dialogType = 'view'
      this.indicatorDialogVisible = true
    },

    handleEditIndicator(row) {
      this.currentIndicator = { ...row }
      this.dialogType = 'edit'
      this.indicatorDialogVisible = true
    },

    handleCalculateIndicator(row) {
      this.$message.success(`正在计算指标：${row.indicatorName}`)
    },

    handleDeleteIndicator(row) {
      this.$confirm(`确定要删除指标"${row.indicatorName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.getIndicatorsList()
      }).catch(() => {
          // 用户取消操作
        })
      },

    handleExportIndicators() {
      this.$message.success('正在导出监管指标数据...')
    },

    // 监管流程相关方法
    getProcessesList() {
      this.processesLoading = true
      getSupervisionProcessesList(this.processesQuery).then(response => {
        if (response.code === 1) {
          this.processesList = response.data.list || []
          this.processesTotal = response.data.total || 0
        } else {
          this.$message.error(response.msg || '获取监管流程列表失败')
        }
        this.processesLoading = false
      }).catch(error => {
        console.error('获取监管流程列表异常:', error)
        this.$message.warning('获取数据暂未开放')
        this.processesLoading = false
      })
    },

    resetProcessesQuery() {
      this.$refs.processesForm.resetFields()
      this.processesQuery = {
        pageNum: 1,
        pageSize: 10,
        processName: '',
        processType: ''
      }
      this.getProcessesList()
    },

    handleAddProcess() {
      this.currentProcess = {}
      this.dialogType = 'add'
      this.processDialogVisible = true
    },

    handleViewProcess(row) {
      this.currentProcess = { ...row }
      this.dialogType = 'view'
      this.processDialogVisible = true
    },

    handleEditProcess(row) {
      this.currentProcess = { ...row }
      this.dialogType = 'edit'
      this.processDialogVisible = true
    },

    handleDesignProcess(row) {
      this.$message.success(`正在设计流程：${row.processName}`)
    },

    handleDeployProcess(row) {
      this.$message.success(`正在部署流程：${row.processName}`)
    },

    handleDeleteProcess(row) {
      this.$confirm(`确定要删除流程"${row.processName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.getProcessesList()
      }).catch(() => {
          // 用户取消操作
        })
      },

    handleExportProcesses() {
      this.$message.success('正在导出监管流程数据...')
    },

    // 工具方法
    getRuleTypeTag(type) {
      const tagMap = {
        'EQUITY': 'primary',
        'ASSET': 'success',
        'FINANCIAL': 'warning',
        'RISK': 'danger'
      }
      return tagMap[type] || 'info'
    },

    getRuleTypeText(type) {
      const textMap = {
        'EQUITY': '股权监管',
        'ASSET': '资产监管',
        'FINANCIAL': '财务监管',
        'RISK': '风险监管'
      }
      return textMap[type] || type
    },

    getPriorityTag(priority) {
      const tagMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return tagMap[priority] || 'info'
    },

    getStatusTag(status) {
      const tagMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'DRAFT': 'warning'
      }
      return tagMap[status] || 'info'
    },

    getStatusText(status) {
      const textMap = {
        'ACTIVE': '启用',
        'INACTIVE': '禁用',
        'DRAFT': '草稿'
      }
      return textMap[status] || status
    },

    getCategoryTag(category) {
      const tagMap = {
        'FINANCIAL': 'primary',
        'RISK': 'danger',
        'EFFICIENCY': 'success',
        'COMPLIANCE': 'warning'
      }
      return tagMap[category] || 'info'
    },

    getCategoryText(category) {
      const textMap = {
        'FINANCIAL': '财务指标',
        'RISK': '风险指标',
        'EFFICIENCY': '效率指标',
        'COMPLIANCE': '合规指标'
      }
      return textMap[category] || category
    },

    getProcessTypeTag(type) {
      const tagMap = {
        'APPROVAL': 'primary',
        'SUPERVISION': 'success',
        'WARNING': 'warning',
        'DISPOSAL': 'danger'
      }
      return tagMap[type] || 'info'
    },

    getProcessTypeText(type) {
      const textMap = {
        'APPROVAL': '审批流程',
        'SUPERVISION': '监管流程',
        'WARNING': '预警流程',
        'DISPOSAL': '处置流程'
      }
      return textMap[type] || type
    }
  }
}
</script>

<style scoped>
.statistics-card {
  margin-bottom: 20px;
}

.statistics-content {
  display: flex;
  align-items: center;
}

.statistics-icon {
  font-size: 40px;
  margin-right: 20px;
}

.statistics-info {
  flex: 1;
}

.statistics-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.statistics-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.tab-content {
  padding: 20px 0;
}

.mb-20 {
  margin-bottom: 20px;
}
</style>
