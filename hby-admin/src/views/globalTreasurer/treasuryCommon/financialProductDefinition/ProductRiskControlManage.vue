<template>
  <div class="product-risk-control-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-warning-outline"></i>
            产品风险控制管理
          </h2>
          <p class="page-description">管理金融产品风险控制策略，包括风险识别、评估指标、控制措施和预警机制</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增策略
          </el-button>
          <el-button type="warning" icon="el-icon-warning" @click="handleRiskAlert">
            风险预警
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出报告
          </el-button>
        </div>
      </div>
    </div>

    <!-- 风险统计卡片 -->
    <div class="risk-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-warning-outline"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总策略数</div>
                <div class="card-value">{{ totalStrategies }}</div>
                <div class="card-change">风控策略</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon high-risk-icon">
                <i class="el-icon-close"></i>
              </div>
              <div class="card-info">
                <div class="card-title">高风险产品</div>
                <div class="card-value">{{ highRiskProducts }}</div>
                <div class="card-change negative">重点监控</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon alert-icon">
                <i class="el-icon-bell"></i>
              </div>
              <div class="card-info">
                <div class="card-title">预警触发</div>
                <div class="card-value">{{ alertTriggers }}</div>
                <div class="card-change negative">需要处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon control-icon">
                <i class="el-icon-lock"></i>
              </div>
              <div class="card-info">
                <div class="card-title">控制措施</div>
                <div class="card-value">{{ controlMeasures }}</div>
                <div class="card-change positive">已执行</div>
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
          <el-form-item label="风控策略编码">
            <el-input
              v-model="listQuery.riskControlCode"
              placeholder="请输入风控策略编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="风控策略名称">
            <el-input
              v-model="listQuery.riskControlName"
              placeholder="请输入风控策略名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中低风险" value="MEDIUM_LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="中高风险" value="MEDIUM_HIGH" />
              <el-option label="高风险" value="HIGH" />
            </el-select>
          </el-form-item>
          <el-form-item label="控制类型">
            <el-select
              v-model="listQuery.controlType"
              placeholder="请选择控制类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="投资限额控制" value="INVESTMENT_LIMIT" />
              <el-option label="集中度控制" value="CONCENTRATION_LIMIT" />
              <el-option label="期限控制" value="MATURITY_LIMIT" />
              <el-option label="流动性控制" value="LIQUIDITY_LIMIT" />
              <el-option label="信用风险控制" value="CREDIT_RISK_LIMIT" />
            </el-select>
          </el-form-item>
          <el-form-item label="监控状态">
            <el-select
              v-model="listQuery.monitoringStatus"
              placeholder="请选择监控状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="正常" value="NORMAL" />
              <el-option label="预警" value="WARNING" />
              <el-option label="超限" value="EXCEEDED" />
              <el-option label="暂停" value="SUSPENDED" />
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
      <el-table-column label="风控策略编码" prop="riskControlCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.riskControlCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="风控策略名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.riskControlName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getRiskLevelColor(row.riskLevel)" size="small">
            {{ getRiskLevelName(row.riskLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="控制类型" width="150px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getControlTypeColor(row.controlType)" size="small">
            {{ getControlTypeName(row.controlType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="限额阈值" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.limitThreshold | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预警阈值" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.warningThreshold | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="当前使用率" width="100px" align="center">
        <template slot-scope="{row}">
          <el-progress :percentage="row.currentUtilizationRate" :color="getUtilizationColor(row.currentUtilizationRate)" />
        </template>
      </el-table-column>
      <el-table-column label="监控状态" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getMonitoringStatusColor(row.monitoringStatus)" size="small">
            {{ getMonitoringStatusName(row.monitoringStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
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
  name: 'ProductRiskControlManage',
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
        name: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            id: 1,
            name: '示例数据',
            description: '这是一个示例数据',
            status: 1
          }
        ]
        this.total = 1
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
