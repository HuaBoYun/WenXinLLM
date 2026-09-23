<template>
  <div class="product-accounting-attr-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-notebook-2"></i>
            产品会计属性管理
          </h2>
          <p class="page-description">管理金融产品会计属性配置，包括科目映射、核算规则、报表分类和税务处理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增属性
          </el-button>
          <el-button type="success" icon="el-icon-setting" @click="handleMapping">
            科目映射
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 属性统计卡片 -->
    <div class="attr-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-notebook-2"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总属性数</div>
                <div class="card-value">{{ totalAttributes }}</div>
                <div class="card-change">已配置属性</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon subject-icon">
                <i class="el-icon-menu"></i>
              </div>
              <div class="card-info">
                <div class="card-title">会计科目</div>
                <div class="card-value">{{ accountingSubjects }}</div>
                <div class="card-change positive">科目映射</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon rule-icon">
                <i class="el-icon-s-operation"></i>
              </div>
              <div class="card-info">
                <div class="card-title">核算规则</div>
                <div class="card-value">{{ accountingRules }}</div>
                <div class="card-change">规则配置</div>
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
                <div class="card-title">启用属性</div>
                <div class="card-value">{{ activeAttributes }}</div>
                <div class="card-change positive">正常使用</div>
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
          <el-form-item label="属性编码">
            <el-input
              v-model="listQuery.attrCode"
              placeholder="请输入属性编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="属性名称">
            <el-input
              v-model="listQuery.attrName"
              placeholder="请输入属性名称"
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
              <el-option label="衍生品投资" value="DERIVATIVE" />
            </el-select>
          </el-form-item>
          <el-form-item label="会计科目类型">
            <el-select
              v-model="listQuery.accountingSubjectType"
              placeholder="请选择会计科目类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="资产类" value="ASSET" />
              <el-option label="负债类" value="LIABILITY" />
              <el-option label="权益类" value="EQUITY" />
              <el-option label="损益类" value="PROFIT_LOSS" />
              <el-option label="成本类" value="COST" />
            </el-select>
          </el-form-item>
          <el-form-item label="核算方式">
            <el-select
              v-model="listQuery.accountingMethod"
              placeholder="请选择核算方式"
              clearable
              style="width: 120px;"
            >
              <el-option label="摊余成本" value="AMORTIZED_COST" />
              <el-option label="公允价值" value="FAIR_VALUE" />
              <el-option label="权益法" value="EQUITY_METHOD" />
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
      <el-table-column label="属性编码" prop="attrCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.attrCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="属性名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.attrName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="产品类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getProductTypeColor(row.productType)" size="small">
            {{ getProductTypeName(row.productType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="会计科目编码" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.accountingSubjectCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会计科目名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.accountingSubjectName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会计科目类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getAccountingSubjectTypeColor(row.accountingSubjectType)" size="small">
            {{ getAccountingSubjectTypeName(row.accountingSubjectType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="核算方式" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getAccountingMethodColor(row.accountingMethod)" size="small">
            {{ getAccountingMethodName(row.accountingMethod) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.updateTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
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
  name: 'ProductAccountingAttrManage',
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
        attrCode: undefined,
        attrName: undefined,
        productType: undefined,
        accountingSubjectType: undefined,
        accountingMethod: undefined
      },
      totalAttrs: 0,
      enabledAttrs: 0,
      assetAttrs: 0,
      liabilityAttrs: 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
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
    getAccountingSubjectTypeName(type) {
      const typeMap = {
        'ASSET': '资产类',
        'LIABILITY': '负债类',
        'EQUITY': '权益类',
        'PROFIT_LOSS': '损益类',
        'COST': '成本类'
      }
      return typeMap[type] || type
    },
    getAccountingSubjectTypeColor(type) {
      const colorMap = {
        'ASSET': 'success',
        'LIABILITY': 'warning',
        'EQUITY': 'primary',
        'PROFIT_LOSS': 'info',
        'COST': 'danger'
      }
      return colorMap[type] || 'default'
    },
    getAccountingMethodName(method) {
      const methodMap = {
        'AMORTIZED_COST': '摊余成本',
        'FAIR_VALUE': '公允价值',
        'EQUITY_METHOD': '权益法'
      }
      return methodMap[method] || method
    },
    getAccountingMethodColor(method) {
      const colorMap = {
        'AMORTIZED_COST': 'primary',
        'FAIR_VALUE': 'success',
        'EQUITY_METHOD': 'warning'
      }
      return colorMap[method] || 'default'
    },
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            attrId: 1,
            attrCode: 'ATTR_BANK_WEALTH_001',
            attrName: '银行理财产品核算属性',
            productType: 'BANK_WEALTH',
            accountingSubjectCode: '1101',
            accountingSubjectName: '银行存款',
            accountingSubjectType: 'ASSET',
            accountingMethod: 'AMORTIZED_COST',
            isEnabled: 1,
            createTime: '2024-01-01 10:00:00',
            updateTime: '2024-09-25 15:30:00',
            description: '银行理财产品的核算属性配置'
          },
          {
            attrId: 2,
            attrCode: 'ATTR_BOND_002',
            attrName: '债券投资核算属性',
            productType: 'BOND',
            accountingSubjectCode: '1503',
            accountingSubjectName: '债权投资',
            accountingSubjectType: 'ASSET',
            accountingMethod: 'FAIR_VALUE',
            isEnabled: 1,
            createTime: '2024-01-15 14:30:00',
            updateTime: '2024-09-25 15:30:00',
            description: '债券投资产品的核算属性配置'
          },
          {
            attrId: 3,
            attrCode: 'ATTR_FUND_003',
            attrName: '基金投资核算属性',
            productType: 'FUND',
            accountingSubjectCode: '1504',
            accountingSubjectName: '其他权益工具投资',
            accountingSubjectType: 'ASSET',
            accountingMethod: 'FAIR_VALUE',
            isEnabled: 1,
            createTime: '2024-02-01 09:00:00',
            updateTime: '2024-09-25 15:30:00',
            description: '基金投资产品的核算属性配置'
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
