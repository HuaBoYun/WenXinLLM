<template>
  <div class="product-transaction-event-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-s-order"></i>
            产品交易事件管理
          </h2>
          <p class="page-description">管理金融产品交易事件配置，包括事件类型、触发条件、处理流程和通知机制</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增事件
          </el-button>
          <el-button type="warning" icon="el-icon-bell" @click="handleEventMonitor">
            事件监控
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 事件统计卡片 -->
    <div class="event-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-s-order"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总事件数</div>
                <div class="card-value">{{ totalEvents }}</div>
                <div class="card-change">交易事件</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon buy-icon">
                <i class="el-icon-shopping-cart-2"></i>
              </div>
              <div class="card-info">
                <div class="card-title">申购事件</div>
                <div class="card-value">{{ purchaseEvents }}</div>
                <div class="card-change positive">买入交易</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon sell-icon">
                <i class="el-icon-sold-out"></i>
              </div>
              <div class="card-info">
                <div class="card-title">赎回事件</div>
                <div class="card-value">{{ redemptionEvents }}</div>
                <div class="card-change negative">卖出交易</div>
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
                <div class="card-title">启用事件</div>
                <div class="card-value">{{ activeEvents }}</div>
                <div class="card-change positive">正常监控</div>
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
          <el-form-item label="事件编码">
            <el-input
              v-model="listQuery.eventCode"
              placeholder="请输入事件编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="事件名称">
            <el-input
              v-model="listQuery.eventName"
              placeholder="请输入事件名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="事件类型">
            <el-select
              v-model="listQuery.eventType"
              placeholder="请选择事件类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="投资申购" value="INVESTMENT_PURCHASE" />
              <el-option label="投资赎回" value="INVESTMENT_REDEMPTION" />
              <el-option label="利息收入" value="INTEREST_INCOME" />
              <el-option label="本金回收" value="PRINCIPAL_RECOVERY" />
              <el-option label="分红收入" value="DIVIDEND_INCOME" />
              <el-option label="投资到期" value="INVESTMENT_MATURITY" />
              <el-option label="投资损失" value="INVESTMENT_LOSS" />
            </el-select>
          </el-form-item>
          <el-form-item label="产品类型">
            <el-select
              v-model="listQuery.productType"
              placeholder="请选择产品类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="银行理财" value="BANK_WEALTH" />
              <el-option label="债券投资" value="BOND" />
              <el-option label="股票投资" value="EQUITY" />
              <el-option label="基金投资" value="FUND" />
              <el-option label="衍生品投资" value="DERIVATIVE" />
            </el-select>
          </el-form-item>
          <el-form-item label="事件状态">
            <el-select
              v-model="listQuery.eventStatus"
              placeholder="请选择事件状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="活跃" value="ACTIVE" />
              <el-option label="暂停" value="PAUSED" />
              <el-option label="停用" value="INACTIVE" />
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
      <el-table-column label="事件编码" prop="eventCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.eventCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="事件名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.eventName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="事件类型" width="150px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getEventTypeColor(row.eventType)" size="small">
            {{ getEventTypeName(row.eventType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="产品类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getProductTypeColor(row.productType)" size="small">
            {{ getProductTypeName(row.productType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="触发条件" min-width="200px" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.triggerCondition }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会计科目" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.accountingSubject }}</span>
        </template>
      </el-table-column>
      <el-table-column label="影响方向" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getImpactDirectionColor(row.impactDirection)" size="small">
            {{ getImpactDirectionName(row.impactDirection) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="优先级" width="80px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getPriorityColor(row.priority)" size="small">
            {{ row.priority }}
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
          <el-tag :type="getEventStatusColor(row.eventStatus)">
            {{ getEventStatusName(row.eventStatus) }}
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
  name: 'ProductTransactionEventManage',
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
        eventCode: undefined,
        eventName: undefined,
        eventType: undefined,
        productType: undefined,
        eventStatus: undefined
      },
      totalEvents: 0,
      activeEvents: 0,
      purchaseEvents: 0,
      redemptionEvents: 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getEventTypeName(type) {
      const typeMap = {
        'INVESTMENT_PURCHASE': '投资申购',
        'INVESTMENT_REDEMPTION': '投资赎回',
        'INTEREST_INCOME': '利息收入',
        'PRINCIPAL_RECOVERY': '本金回收',
        'DIVIDEND_INCOME': '分红收入',
        'INVESTMENT_MATURITY': '投资到期',
        'INVESTMENT_LOSS': '投资损失'
      }
      return typeMap[type] || type
    },
    getEventTypeColor(type) {
      const colorMap = {
        'INVESTMENT_PURCHASE': 'primary',
        'INVESTMENT_REDEMPTION': 'warning',
        'INTEREST_INCOME': 'success',
        'PRINCIPAL_RECOVERY': 'info',
        'DIVIDEND_INCOME': 'success',
        'INVESTMENT_MATURITY': 'default',
        'INVESTMENT_LOSS': 'danger'
      }
      return colorMap[type] || 'default'
    },
    getProductTypeName(type) {
      const typeMap = {
        'BANK_WEALTH': '银行理财',
        'BOND': '债券投资',
        'EQUITY': '股票投资',
        'FUND': '基金投资',
        'DERIVATIVE': '衍生品投资'
      }
      return typeMap[type] || type
    },
    getProductTypeColor(type) {
      const colorMap = {
        'BANK_WEALTH': 'primary',
        'BOND': 'success',
        'EQUITY': 'warning',
        'FUND': 'info',
        'DERIVATIVE': 'danger'
      }
      return colorMap[type] || 'default'
    },
    getEventStatusName(status) {
      const statusMap = {
        'ACTIVE': '活跃',
        'PAUSED': '暂停',
        'INACTIVE': '停用'
      }
      return statusMap[status] || status
    },
    getEventStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'PAUSED': 'warning',
        'INACTIVE': 'danger'
      }
      return colorMap[status] || 'default'
    },
    getImpactDirectionName(direction) {
      const directionMap = {
        'DEBIT': '借方',
        'CREDIT': '贷方'
      }
      return directionMap[direction] || direction
    },
    getImpactDirectionColor(direction) {
      const colorMap = {
        'DEBIT': 'primary',
        'CREDIT': 'success'
      }
      return colorMap[direction] || 'default'
    },
    getPriorityColor(priority) {
      if (priority >= 1 && priority <= 3) return 'danger'
      if (priority >= 4 && priority <= 6) return 'warning'
      if (priority >= 7 && priority <= 10) return 'success'
      return 'default'
    },
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            eventId: 1,
            eventCode: 'EVENT_PURCHASE_001',
            eventName: '银行理财产品申购',
            eventType: 'INVESTMENT_PURCHASE',
            productType: 'BANK_WEALTH',
            triggerCondition: '申购金额 > 0 且 产品状态 = 在售',
            accountingSubject: '1503',
            impactDirection: 'DEBIT',
            priority: 1,
            eventStatus: 'ACTIVE',
            createTime: '2024-01-01 10:00:00',
            description: '银行理财产品申购事件处理'
          },
          {
            eventId: 2,
            eventCode: 'EVENT_REDEMPTION_001',
            eventName: '银行理财产品赎回',
            eventType: 'INVESTMENT_REDEMPTION',
            productType: 'BANK_WEALTH',
            triggerCondition: '赎回金额 > 0 且 产品状态 = 可赎回',
            accountingSubject: '1101',
            impactDirection: 'CREDIT',
            priority: 2,
            eventStatus: 'ACTIVE',
            createTime: '2024-01-15 14:30:00',
            description: '银行理财产品赎回事件处理'
          },
          {
            eventId: 3,
            eventCode: 'EVENT_INTEREST_001',
            eventName: '债券投资利息收入',
            eventType: 'INTEREST_INCOME',
            productType: 'BOND',
            triggerCondition: '结息日期 = 当前日期',
            accountingSubject: '6051',
            impactDirection: 'CREDIT',
            priority: 3,
            eventStatus: 'ACTIVE',
            createTime: '2024-02-01 09:00:00',
            description: '债券投资利息收入事件处理'
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
