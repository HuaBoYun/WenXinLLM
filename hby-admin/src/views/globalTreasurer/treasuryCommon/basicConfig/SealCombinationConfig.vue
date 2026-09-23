<template>
  <div class="seal-combination-config">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-connection"></i>
            印鉴组合配置
          </h2>
          <p class="page-description">管理印鉴组合使用规则，包括组合类型、使用场景、权限控制和审批流程</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增组合
          </el-button>
          <el-button type="success" icon="el-icon-setting" @click="handleRuleConfig">
            规则配置
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 组合统计卡片 -->
    <div class="combination-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-connection"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总组合数</div>
                <div class="card-value">{{ totalCombinations }}</div>
                <div class="card-change">已配置组合</div>
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
                <div class="card-title">启用组合</div>
                <div class="card-value">{{ activeCombinations }}</div>
                <div class="card-change positive">正常使用</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon scenario-icon">
                <i class="el-icon-menu"></i>
              </div>
              <div class="card-info">
                <div class="card-title">使用场景</div>
                <div class="card-value">{{ usageScenarios }}</div>
                <div class="card-change">业务场景</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon usage-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">今日使用</div>
                <div class="card-value">{{ todayUsage }}</div>
                <div class="card-change">使用次数</div>
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
          <el-form-item label="组合编码">
            <el-input
              v-model="listQuery.combinationCode"
              placeholder="请输入组合编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="组合名称">
            <el-input
              v-model="listQuery.combinationName"
              placeholder="请输入组合名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="组合类型">
            <el-select
              v-model="listQuery.combinationType"
              placeholder="请选择组合类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="单印鉴" value="SINGLE_SEAL" />
              <el-option label="双印鉴" value="DOUBLE_SEAL" />
              <el-option label="三印鉴" value="TRIPLE_SEAL" />
              <el-option label="多印鉴" value="MULTIPLE_SEAL" />
              <el-option label="特殊组合" value="SPECIAL_COMBINATION" />
            </el-select>
          </el-form-item>
          <el-form-item label="业务类型">
            <el-select
              v-model="listQuery.businessType"
              placeholder="请选择业务类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="资金划转" value="FUND_TRANSFER" />
              <el-option label="投资交易" value="INVESTMENT_TRANSACTION" />
              <el-option label="票据业务" value="BILL_BUSINESS" />
              <el-option label="合同签署" value="CONTRACT_SIGNING" />
              <el-option label="授权审批" value="AUTHORIZATION_APPROVAL" />
            </el-select>
          </el-form-item>
          <el-form-item label="权限级别">
            <el-select
              v-model="listQuery.authorityLevel"
              placeholder="请选择权限级别"
              clearable
              style="width: 120px;"
            >
              <el-option label="一级权限" value="LEVEL_1" />
              <el-option label="二级权限" value="LEVEL_2" />
              <el-option label="三级权限" value="LEVEL_3" />
              <el-option label="特殊权限" value="SPECIAL" />
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
      <el-table-column label="组合编码" prop="combinationCode" sortable="custom" align="center" min-width="120">
        <template slot-scope="{row}">
          <span>{{ row.combinationCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="组合名称" min-width="150" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.combinationName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="组合类型" min-width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getCombinationTypeColor(row.combinationType)" size="small">
            {{ getCombinationTypeName(row.combinationType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="业务类型" min-width="120" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getBusinessTypeColor(row.businessType)" size="small">
            {{ getBusinessTypeName(row.businessType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="权限级别" min-width="90" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getAuthorityLevelColor(row.authorityLevel)" size="small">
            {{ getAuthorityLevelName(row.authorityLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="金额限制" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.maxAmountLimit | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="使用次数" min-width="80" align="center">
        <template slot-scope="{row}">
          <span>{{ row.usageCount }}次</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" min-width="140" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" min-width="80" align="center">
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
import { generateSealCombinationData, handleApiError } from '@/utils/mockData'
import { getSealCombinationList } from '@/api/globalTreasurer/czgg'

export default {
  name: 'SealCombinationConfig',
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
        combinationCode: undefined,
        combinationName: undefined,
        combinationType: undefined,
        businessType: undefined,
        authorityLevel: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        // 尝试调用真实API
        const response = await getSealCombinationList(this.listQuery)
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.list = response.data?.tlist || response.data || []
          this.total = response.data?.totalRecord || response.totalRecord || this.list.length
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        // API调用失败时使用模拟数据
        console.warn('印鉴组合配置API调用失败，使用模拟数据:', error)
        const mockResponse = handleApiError(error, generateSealCombinationData, 4)
        this.list = mockResponse.data.tlist
        this.total = mockResponse.data.totalRecord

        // 显示友好提示
        this.$message({
          message: '当前显示模拟数据，请检查网络连接或联系管理员',
          type: 'warning',
          duration: 3000
        })
      } finally {
        this.listLoading = false
      }
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
    },
    // 组合类型名称映射
    getCombinationTypeName(type) {
      const typeMap = {
        'SINGLE_SEAL': '单印鉴',
        'DUAL_SEAL': '双印鉴',
        'MULTI_SEAL': '多印鉴'
      }
      return typeMap[type] || type
    },
    // 组合类型颜色映射
    getCombinationTypeColor(type) {
      const colorMap = {
        'SINGLE_SEAL': 'success',
        'DUAL_SEAL': 'warning',
        'MULTI_SEAL': 'danger'
      }
      return colorMap[type] || 'info'
    },
    // 业务类型名称映射
    getBusinessTypeName(type) {
      const typeMap = {
        'FUND_TRANSFER': '资金划转',
        'INVESTMENT_TRADE': '投资交易',
        'BILL_BUSINESS': '票据业务',
        'CONTRACT_SIGN': '合同签署',
        'AUTHORIZATION': '授权审批'
      }
      return typeMap[type] || type
    },
    // 业务类型颜色映射
    getBusinessTypeColor(type) {
      const colorMap = {
        'FUND_TRANSFER': 'primary',
        'INVESTMENT_TRADE': 'success',
        'BILL_BUSINESS': 'warning',
        'CONTRACT_SIGN': 'danger',
        'AUTHORIZATION': 'info'
      }
      return colorMap[type] || 'info'
    },
    // 权限级别名称映射
    getAuthorityLevelName(level) {
      const levelMap = {
        'LOW': '低级',
        'MEDIUM': '中级',
        'HIGH': '高级',
        'SUPER': '超级'
      }
      return levelMap[level] || level
    },
    // 权限级别颜色映射
    getAuthorityLevelColor(level) {
      const colorMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'SUPER': 'primary'
      }
      return colorMap[level] || 'info'
    }
  }
}
</script>
