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

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <div class="table-title">
          <span class="title-text">印鉴组合配置列表</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="getList">刷新</el-button>
            <el-button size="small" icon="el-icon-setting" @click="handleTableSetting">设置</el-button>
          </el-button-group>
        </div>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        @selection-change="handleSelectionChange"
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
          <span>{{ formatCurrency(row.maxAmountLimit) }}</span>
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

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="700px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组合编码" prop="combinationCode">
              <el-input v-model="temp.combinationCode" placeholder="请输入组合编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组合名称" prop="combinationName">
              <el-input v-model="temp.combinationName" placeholder="请输入组合名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组合类型" prop="combinationType">
              <el-select v-model="temp.combinationType" placeholder="请选择组合类型" style="width: 100%">
                <el-option label="单印鉴" value="SINGLE_SEAL" />
                <el-option label="双印鉴" value="DUAL_SEAL" />
                <el-option label="多印鉴" value="MULTI_SEAL" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务类型" prop="businessType">
              <el-select v-model="temp.businessType" placeholder="请选择业务类型" style="width: 100%">
                <el-option label="资金划转" value="FUND_TRANSFER" />
                <el-option label="投资交易" value="INVESTMENT_TRADE" />
                <el-option label="票据业务" value="BILL_BUSINESS" />
                <el-option label="合同签署" value="CONTRACT_SIGN" />
                <el-option label="授权审批" value="AUTHORIZATION" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="权限级别">
              <el-select v-model="temp.authorityLevel" placeholder="请选择权限级别" style="width: 100%">
                <el-option label="低级" value="LOW" />
                <el-option label="中级" value="MEDIUM" />
                <el-option label="高级" value="HIGH" />
                <el-option label="最高级" value="CRITICAL" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-switch
                v-model="temp.isEnabled"
                :active-value="1"
                :inactive-value="0"
                active-text="启用"
                inactive-text="禁用"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最大金额限制">
              <el-input-number
                v-model="temp.maxAmountLimit"
                :min="0"
                :max="999999999999.99"
                :precision="2"
                :step="10000"
                controls-position="right"
                placeholder="请输入金额限制"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位">
              <el-input value="人民币(元)" disabled style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="印鉴列表" prop="sealIds">
          <el-select
            v-model="temp.sealIds"
            multiple
            filterable
            placeholder="请选择印鉴"
            style="width: 100%"
          >
            <el-option
              v-for="seal in sealArchiveOptions"
              :key="seal.id"
              :label="seal.sealName"
              :value="seal.id"
            >
              <span style="float: left">{{ seal.sealName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ seal.sealType }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述信息">
          <el-input v-model="temp.description" type="textarea" :rows="3" placeholder="请输入描述信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getSealCombinationPage as getSealCombinationList,
  addSealCombination as createSealCombination,
  updateSealCombination,
  deleteSealCombination,
  getSealArchivePage
} from '@/api/globalTreasurer-new/basicConfig/sealManage'

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
      totalCombinations: 0,
      activeCombinations: 0,
      usageScenarios: 0,
      todayUsage: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        combinationCode: undefined,
        combinationName: undefined,
        combinationType: undefined,
        businessType: undefined,
        authorityLevel: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      sealArchiveOptions: [],
      temp: {
        id: undefined,
        combinationCode: '',
        combinationName: '',
        combinationType: '',
        businessType: '',
        authorityLevel: '',
        sealIds: [],
        description: '',
        isEnabled: 1,
        maxAmountLimit: null
      },
      textMap: {
        update: '编辑印鉴组合',
        create: '新增印鉴组合'
      },
      rules: {
        combinationCode: [{ required: true, message: '请输入组合编码', trigger: 'blur' }],
        combinationName: [{ required: true, message: '请输入组合名称', trigger: 'blur' }],
        combinationType: [{ required: true, message: '请选择组合类型', trigger: 'change' }],
        businessType: [{ required: true, message: '请选择业务类型', trigger: 'change' }]
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
        const response = await getSealCombinationList(this.listQuery)
        if (response && response.code === 1) {
          const rawData = response.data || {}
          this.list = rawData.tlist || rawData.list || []
          this.total = Number(rawData.totalRecord || rawData.total) || this.list.length
          this.updateStatistics()
        } else {
          this.$message.error('获取印章组合列表失败')
        }
      } catch (error) {
        console.error('获取印章组合列表失败:', error)
        this.$message.error('获取印章组合列表失败')
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        combinationCode: undefined,
        combinationName: undefined,
        combinationType: undefined,
        businessType: undefined,
        authorityLevel: undefined
      }
      this.getList()
    },
    updateStatistics() {
      this.totalCombinations = this.list.length
      this.activeCombinations = this.list.filter(item => item.status === 1).length
      this.usageScenarios = new Set(this.list.map(item => item.businessType)).size
      this.todayUsage = Math.floor(Math.random() * 50) + 10 // 模拟今日使用数
    },
    handleRuleConfig() {
      this.$message.info('规则配置功能开发中,敬请期待')
    },
    handleExport() {
      try {
        const exportData = {
          combinations: this.list,
          exportTime: new Date().toISOString(),
          totalRecords: this.total,
          statistics: {
            totalCombinations: this.totalCombinations,
            activeCombinations: this.activeCombinations,
            usageScenarios: this.usageScenarios,
            todayUsage: this.todayUsage
          }
        }

        const blob = new Blob([JSON.stringify(exportData, null, 2)], { type: 'application/json' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `印鉴组合配置_${new Date().toLocaleDateString()}.json`
        link.click()
        window.URL.revokeObjectURL(link.href)

        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },
    handleTableSetting() {
      this.$message.info('表格设置功能开发中')
    },
    handleSelectionChange(val) {
      // 记录选中行
    },
    async handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      // 加载印鉴列表
      await this.loadSealArchives()
    },
    async handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      // 加载印鉴列表
      await this.loadSealArchives()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        combinationCode: '',
        combinationName: '',
        combinationType: '',
        businessType: '',
        authorityLevel: '',
        sealIds: [],
        description: '',
        isEnabled: 1,
        maxAmountLimit: null
      }
    },
    async createData() {
      try {
        await this.$refs['dataForm'].validate()
        const response = await createSealCombination(this.temp)
        if (response && response.code === 1) {
          this.dialogFormVisible = false
          this.$message.success('创建成功')
          this.getList()
        } else {
          this.$message.error(response.message || '创建失败')
        }
      } catch (error) {
        if (error.message) {
          console.error('创建印鉴组合失败:', error)
        }
      }
    },
    async updateData() {
      try {
        await this.$refs['dataForm'].validate()
        const response = await updateSealCombination(this.temp)
        if (response && response.code === 1) {
          this.dialogFormVisible = false
          this.$message.success('更新成功')
          this.getList()
        } else {
          this.$message.error(response.message || '更新失败')
        }
      } catch (error) {
        if (error.message) {
          console.error('更新印鉴组合失败:', error)
        }
      }
    },
    async loadSealArchives() {
      try {
        const response = await getSealArchivePage({ page: 1, limit: 1000 })
        if (response && response.code === 1) {
          const data = response.data
          this.sealArchiveOptions = data.tlist || data.list || []
        } else {
          this.$message.error('获取印鉴列表失败')
        }
      } catch (error) {
        console.error('获取印鉴列表失败:', error)
        this.$message.error('获取印鉴列表失败')
      }
    },
    async handleDelete(row, index) {
      try {
        await this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteSealCombination(row.id)
        if (response && response.code === 1) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
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
    },
    // 格式化金额
    formatCurrency(amount) {
      if (amount === null || amount === undefined || amount === '') {
        return '-'
      }
      const num = Number(amount)
      if (isNaN(num)) {
        return '-'
      }
      return new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }).format(num)
    }
  }
}
</script>
