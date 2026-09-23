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
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="规则编码" prop="ruleCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.ruleCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.ruleName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getRuleTypeColor(row.ruleType)" size="small">
            {{ getRuleTypeName(row.ruleType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="适用模块" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ getModuleName(row.moduleCode) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="优先级" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getPriorityColor(row.priority)" size="small">
            {{ getPriorityName(row.priority) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="生效时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.effectiveTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="失效时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.expireTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getRuleStatusColor(row.ruleStatus)">
            {{ getRuleStatusName(row.ruleStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'

export default {
  name: 'BusinessRuleManage',
  components: { Pagination },
  directives: { waves },
  filters: {
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
      tableKey: 0,
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
      activeRules: 0,
      testingRules: 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
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
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            ruleId: 1,
            ruleCode: 'RULE_ACCOUNT_001',
            ruleName: '账户余额不足检查规则',
            ruleType: 'VALIDATION',
            moduleCode: 'ACCOUNT',
            priority: 'HIGH',
            ruleStatus: 'ENABLED',
            effectiveTime: '2024-01-01 00:00:00',
            expireTime: '2025-12-31 23:59:59',
            description: '检查账户余额是否足够支付',
            createTime: '2024-01-01 10:00:00'
          },
          {
            ruleId: 2,
            ruleCode: 'RULE_RISK_002',
            ruleName: '大额交易风险控制规则',
            ruleType: 'RISK_CONTROL',
            moduleCode: 'FUND',
            priority: 'HIGH',
            ruleStatus: 'ENABLED',
            effectiveTime: '2024-01-01 00:00:00',
            expireTime: '2025-12-31 23:59:59',
            description: '对大额交易进行风险评估和控制',
            createTime: '2024-01-15 14:30:00'
          },
          {
            ruleId: 3,
            ruleCode: 'RULE_APPROVAL_003',
            ruleName: '投资审批流程规则',
            ruleType: 'APPROVAL',
            moduleCode: 'INVESTMENT',
            priority: 'MEDIUM',
            ruleStatus: 'TESTING',
            effectiveTime: '2024-09-01 00:00:00',
            expireTime: '2025-12-31 23:59:59',
            description: '投资项目的审批流程和权限控制',
            createTime: '2024-08-15 09:00:00'
          }
        ]
        this.total = 3
        this.listLoading = false
      }, 1000)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
      this.$message.info('创建功能待实现')
    },
    handleUpdate(row) {
      this.$message.info('编辑功能待实现')
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.list.splice(index, 1)
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
      })
    }
  }
}
</script>
