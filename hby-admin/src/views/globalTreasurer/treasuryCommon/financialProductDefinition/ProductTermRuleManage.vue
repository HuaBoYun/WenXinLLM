<template>
  <div class="product-term-rule-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-time"></i>
            产品期限规则管理
          </h2>
          <p class="page-description">管理金融产品期限规则配置，包括投资期限、锁定期、赎回规则和期限调整</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增规则
          </el-button>
          <el-button type="success" icon="el-icon-date" @click="handleTermCalculate">
            期限计算
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出规则
          </el-button>
        </div>
      </div>
    </div>

    <!-- 期限统计卡片 -->
    <div class="term-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总规则数</div>
                <div class="card-value">{{ totalRules }}</div>
                <div class="card-change">期限规则</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon short-term-icon">
                <i class="el-icon-timer"></i>
              </div>
              <div class="card-info">
                <div class="card-title">短期产品</div>
                <div class="card-value">{{ shortTermRules }}</div>
                <div class="card-change positive">≤1年</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon medium-term-icon">
                <i class="el-icon-date"></i>
              </div>
              <div class="card-info">
                <div class="card-title">中期产品</div>
                <div class="card-value">{{ mediumTermRules }}</div>
                <div class="card-change">1-5年</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon long-term-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">长期产品</div>
                <div class="card-value">{{ longTermRules }}</div>
                <div class="card-change">＞5年</div>
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
          <el-form-item label="期限规则编码">
            <el-input
              v-model="listQuery.termRuleCode"
              placeholder="请输入期限规则编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="期限规则名称">
            <el-input
              v-model="listQuery.termRuleName"
              placeholder="请输入期限规则名称"
              style="width: 200px;"
              clearable
            />
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
              <el-option label="存款产品" value="DEPOSIT" />
            </el-select>
          </el-form-item>
          <el-form-item label="期限类型">
            <el-select
              v-model="listQuery.termType"
              placeholder="请选择期限类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="固定期限" value="FIXED" />
              <el-option label="开放式" value="OPEN_ENDED" />
              <el-option label="定期开放" value="PERIODIC_OPEN" />
              <el-option label="滚动期限" value="ROLLING" />
            </el-select>
          </el-form-item>
          <el-form-item label="期限单位">
            <el-select
              v-model="listQuery.termUnit"
              placeholder="请选择期限单位"
              clearable
              style="width: 100px;"
            >
              <el-option label="天" value="DAYS" />
              <el-option label="月" value="MONTHS" />
              <el-option label="年" value="YEARS" />
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
      <el-table-column label="期限规则编码" prop="termRuleCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.termRuleCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="期限规则名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.termRuleName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="产品类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getProductTypeColor(row.productType)" size="small">
            {{ getProductTypeName(row.productType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="期限类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getTermTypeColor(row.termType)" size="small">
            {{ getTermTypeName(row.termType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最小期限" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.minTerm }}{{ getTermUnitName(row.termUnit) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最大期限" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.maxTerm }}{{ getTermUnitName(row.termUnit) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="锁定期" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.lockPeriod }}{{ getTermUnitName(row.termUnit) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="提前赎回费率" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.earlyRedemptionFeeRate }}%</span>
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
  name: 'ProductTermRuleManage',
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
