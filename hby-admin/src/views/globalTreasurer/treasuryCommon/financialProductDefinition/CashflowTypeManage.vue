<template>
  <div class="cashflow-type-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-data-analysis"></i>
            现金流类型管理
          </h2>
          <p class="page-description">管理现金流类型定义，包括流入流出分类、业务场景配置和会计科目映射</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增类型
          </el-button>
          <el-button type="success" icon="el-icon-sort" @click="handleSort">
            排序管理
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 现金流统计卡片 -->
    <div class="cashflow-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总类型数</div>
                <div class="card-value">{{ totalCashflowTypes }}</div>
                <div class="card-change">已配置类型</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon inflow-icon">
                <i class="el-icon-top"></i>
              </div>
              <div class="card-info">
                <div class="card-title">流入类型</div>
                <div class="card-value">{{ inflowTypes }}</div>
                <div class="card-change positive">资金流入</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon outflow-icon">
                <i class="el-icon-bottom"></i>
              </div>
              <div class="card-info">
                <div class="card-title">流出类型</div>
                <div class="card-value">{{ outflowTypes }}</div>
                <div class="card-change negative">资金流出</div>
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
                <div class="card-title">启用类型</div>
                <div class="card-value">{{ activeTypes }}</div>
                <div class="card-change">正常使用</div>
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
          <el-form-item label="现金流类型编码">
            <el-input
              v-model="listQuery.cashflowTypeCode"
              placeholder="请输入现金流类型编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="现金流类型名称">
            <el-input
              v-model="listQuery.cashflowTypeName"
              placeholder="请输入现金流类型名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="现金流方向">
            <el-select
              v-model="listQuery.cashflowDirection"
              placeholder="请选择现金流方向"
              clearable
              style="width: 120px;"
            >
              <el-option label="流入" value="INFLOW" />
              <el-option label="流出" value="OUTFLOW" />
              <el-option label="双向" value="BOTH" />
            </el-select>
          </el-form-item>
          <el-form-item label="业务分类">
            <el-select
              v-model="listQuery.businessCategory"
              placeholder="请选择业务分类"
              clearable
              style="width: 150px;"
            >
              <el-option label="经营活动" value="OPERATING" />
              <el-option label="投资活动" value="INVESTING" />
              <el-option label="筹资活动" value="FINANCING" />
            </el-select>
          </el-form-item>
          <el-form-item label="影响类型">
            <el-select
              v-model="listQuery.impactType"
              placeholder="请选择影响类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="直接影响" value="DIRECT" />
              <el-option label="间接影响" value="INDIRECT" />
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
      <el-table-column label="现金流类型编码" prop="cashflowTypeCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.cashflowTypeCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现金流类型名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.cashflowTypeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现金流方向" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getCashflowDirectionColor(row.cashflowDirection)" size="small">
            {{ getCashflowDirectionName(row.cashflowDirection) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="业务分类" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getBusinessCategoryColor(row.businessCategory)" size="small">
            {{ getBusinessCategoryName(row.businessCategory) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="影响类型" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getImpactTypeColor(row.impactType)" size="small">
            {{ getImpactTypeName(row.impactType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="会计科目" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.accountingSubject }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预测权重" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.forecastWeight }}%</span>
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
  name: 'CashflowTypeManage',
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
        cashflowTypeCode: undefined,
        cashflowTypeName: undefined,
        cashflowDirection: undefined,
        businessCategory: undefined,
        impactType: undefined
      },
      totalTypes: 0,
      enabledTypes: 0,
      inflowTypes: 0,
      outflowTypes: 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getCashflowDirectionName(direction) {
      const directionMap = {
        'INFLOW': '流入',
        'OUTFLOW': '流出',
        'BOTH': '双向'
      }
      return directionMap[direction] || direction
    },
    getCashflowDirectionColor(direction) {
      const colorMap = {
        'INFLOW': 'success',
        'OUTFLOW': 'danger',
        'BOTH': 'info'
      }
      return colorMap[direction] || 'default'
    },
    getBusinessCategoryName(category) {
      const categoryMap = {
        'OPERATING': '经营活动',
        'INVESTING': '投资活动',
        'FINANCING': '筹资活动'
      }
      return categoryMap[category] || category
    },
    getBusinessCategoryColor(category) {
      const colorMap = {
        'OPERATING': 'primary',
        'INVESTING': 'success',
        'FINANCING': 'warning'
      }
      return colorMap[category] || 'default'
    },
    getImpactTypeName(type) {
      const typeMap = {
        'DIRECT': '直接影响',
        'INDIRECT': '间接影响'
      }
      return typeMap[type] || type
    },
    getImpactTypeColor(type) {
      const colorMap = {
        'DIRECT': 'primary',
        'INDIRECT': 'info'
      }
      return colorMap[type] || 'default'
    },
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            cashflowTypeId: 1,
            cashflowTypeCode: 'CF_INVEST_PURCHASE',
            cashflowTypeName: '投资申购现金流',
            cashflowDirection: 'OUTFLOW',
            businessCategory: 'INVESTING',
            impactType: 'DIRECT',
            accountingSubject: '1503',
            forecastWeight: 85.5,
            isEnabled: 1,
            createTime: '2024-01-01 10:00:00',
            description: '投资产品申购产生的现金流出'
          },
          {
            cashflowTypeId: 2,
            cashflowTypeCode: 'CF_INVEST_REDEMPTION',
            cashflowTypeName: '投资赎回现金流',
            cashflowDirection: 'INFLOW',
            businessCategory: 'INVESTING',
            impactType: 'DIRECT',
            accountingSubject: '1101',
            forecastWeight: 90.2,
            isEnabled: 1,
            createTime: '2024-01-15 14:30:00',
            description: '投资产品赎回产生的现金流入'
          },
          {
            cashflowTypeId: 3,
            cashflowTypeCode: 'CF_INTEREST_INCOME',
            cashflowTypeName: '利息收入现金流',
            cashflowDirection: 'INFLOW',
            businessCategory: 'INVESTING',
            impactType: 'DIRECT',
            accountingSubject: '6051',
            forecastWeight: 95.8,
            isEnabled: 1,
            createTime: '2024-02-01 09:00:00',
            description: '投资产品利息收入产生的现金流入'
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
