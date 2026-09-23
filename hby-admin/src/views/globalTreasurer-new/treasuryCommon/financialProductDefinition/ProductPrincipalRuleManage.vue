<template>
  <div class="product-principal-rule-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-wallet"></i>
            产品本金规则管理
          </h2>
          <p class="page-description">管理金融产品本金处理规则，包括本金归还方式、分期规则、提前还款和违约处理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增规则
          </el-button>
          <el-button type="success" icon="el-icon-calculator" @click="handleCalculate">
            本金计算
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
                <i class="el-icon-wallet"></i>
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
              <div class="card-icon lump-sum-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">一次性还款</div>
                <div class="card-value">{{ lumpSumRules }}</div>
                <div class="card-change positive">到期还本</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon installment-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">分期还款</div>
                <div class="card-value">{{ installmentRules }}</div>
                <div class="card-change">分期规则</div>
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
                <div class="card-title">启用规则</div>
                <div class="card-value">{{ activeRules }}</div>
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
          <el-form-item label="产品类型">
            <el-select
              v-model="listQuery.productType"
              placeholder="请选择产品类型"
              style="width: 150px;"
              clearable
            >
              <el-option label="银行理财" value="BANK_WEALTH" />
              <el-option label="债券投资" value="BOND" />
              <el-option label="股票投资" value="EQUITY" />
              <el-option label="基金投资" value="FUND" />
              <el-option label="存款产品" value="DEPOSIT" />
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
      <el-table-column label="产品类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getProductTypeColor(row.productType)" size="small">
            {{ getProductTypeName(row.productType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="本金处理方式" width="150px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getPrincipalHandlingColor(row.repaymentMethod)" size="small">
            {{ getPrincipalHandlingName(row.repaymentMethod) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getRiskLevelColor(row.riskLevel)" size="small">
            {{ getRiskLevelName(row.riskLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最小投资金额" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.minInvestmentAmount | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最大投资金额" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.maxInvestmentAmount | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="本金保障率" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.principalGuaranteeRate }}%</span>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新增本金规则' : '编辑本金规则'" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-width="120px">
        <el-form-item label="规则编码" prop="ruleCode">
          <el-input v-model="temp.ruleCode" placeholder="请输入规则编码" :disabled="dialogStatus === 'update'" />
        </el-form-item>
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="temp.ruleName" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="产品类型">
          <el-select v-model="temp.productType" placeholder="请选择产品类型" style="width: 100%;">
            <el-option label="银行理财" value="BANK_WEALTH" />
            <el-option label="债券投资" value="BOND" />
            <el-option label="股票投资" value="EQUITY" />
            <el-option label="基金投资" value="FUND" />
            <el-option label="存款产品" value="DEPOSIT" />
          </el-select>
        </el-form-item>
        <el-form-item label="本金处理方式">
          <el-select v-model="temp.repaymentMethod" placeholder="请选择本金处理方式" style="width: 100%;">
            <el-option label="到期一次性返还" value="MATURITY_LUMP_SUM" />
            <el-option label="分期返还" value="INSTALLMENT" />
            <el-option label="随时赎回" value="ANYTIME_REDEMPTION" />
            <el-option label="保本保息" value="PRINCIPAL_GUARANTEED" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="temp.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
            <el-option label="低风险" value="LOW" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="高风险" value="HIGH" />
          </el-select>
        </el-form-item>
        <el-form-item label="最小投资金额">
          <el-input-number v-model="temp.minInvestmentAmount" :min="0" :precision="2" placeholder="请输入最小投资金额" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="最大投资金额">
          <el-input-number v-model="temp.maxInvestmentAmount" :min="0" :precision="2" placeholder="请输入最大投资金额" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="本金保障率(%)">
          <el-input-number v-model="temp.principalGuaranteeRate" :min="0" :max="100" :precision="2" placeholder="请输入本金保障率" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="temp.isEnabled" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="temp.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 本金计算对话框 -->
    <el-dialog title="本金计算" :visible.sync="calculateDialogVisible" width="900px">
      <el-form ref="calculateForm" :rules="calculateRules" :model="calculateForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="选择规则" prop="ruleId">
              <el-select v-model="calculateForm.ruleId" placeholder="请选择规则" style="width: 100%;" filterable>
                <el-option
                  v-for="item in list"
                  :key="item.ruleId"
                  :label="`${item.ruleCode} - ${item.ruleName}`"
                  :value="item.ruleId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投资金额" prop="investmentAmount">
              <el-input-number v-model="calculateForm.investmentAmount" :min="0" :precision="2" placeholder="请输入投资金额" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="投资期限" prop="investmentPeriod">
              <el-input-number v-model="calculateForm.investmentPeriod" :min="1" placeholder="请输入投资期限" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="期限单位" prop="periodUnit">
              <el-select v-model="calculateForm.periodUnit" placeholder="请选择期限单位" style="width: 100%;">
                <el-option label="天" value="day" />
                <el-option label="月" value="month" />
                <el-option label="年" value="year" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年化收益率(%)">
              <el-input-number v-model="calculateForm.annualRate" :min="0" :max="100" :precision="2" placeholder="请输入年化收益率" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" :loading="calculateLoading" @click="handleDoCalculate" style="width: 200px;">开始计算</el-button>
        </el-form-item>
      </el-form>

      <!-- 计算结果 -->
      <div v-if="calculateResult" class="calculate-result">
        <el-divider content-position="left">计算结果</el-divider>
        <el-row :gutter="20" class="result-cards">
          <el-col :span="6">
            <el-card class="result-card">
              <div class="result-label">投资金额</div>
              <div class="result-value">{{ formatCurrency(calculateResult.investmentAmount) }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="result-card">
              <div class="result-label">预期收益</div>
              <div class="result-value income">{{ formatCurrency(calculateResult.expectedIncome) }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="result-card">
              <div class="result-label">本金+收益</div>
              <div class="result-value total">{{ formatCurrency(calculateResult.totalAmount) }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="result-card">
              <div class="result-label">保障金额</div>
              <div class="result-value guaranteed">{{ formatCurrency(calculateResult.guaranteedAmount) }}</div>
            </el-card>
          </el-col>
        </el-row>

        <el-divider content-position="left">详细信息</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="规则编码">{{ calculateResult.ruleCode }}</el-descriptions-item>
          <el-descriptions-item label="规则名称">{{ calculateResult.ruleName }}</el-descriptions-item>
          <el-descriptions-item label="本金处理方式">{{ calculateResult.repaymentMethodName }}</el-descriptions-item>
          <el-descriptions-item label="投资期限">{{ calculateResult.investmentPeriod }} {{ calculateResult.periodUnit === 'day' ? '天' : calculateResult.periodUnit === 'month' ? '月' : '年' }}</el-descriptions-item>
          <el-descriptions-item label="年化收益率">{{ calculateResult.annualRate }}%</el-descriptions-item>
          <el-descriptions-item label="本金保障率">{{ calculateResult.principalGuaranteeRate }}%</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">返还计划</el-divider>
        <el-table :data="calculateResult.repaymentPlans" border style="width: 100%;">
          <el-table-column label="期数" prop="period" width="80" align="center" />
          <el-table-column label="应还日期" width="120" align="center">
            <template slot-scope="{row}">
              {{ formatDate(row.repaymentDate) }}
            </template>
          </el-table-column>
          <el-table-column label="应还本金" width="150" align="right">
            <template slot-scope="{row}">
              {{ formatCurrency(row.principal) }}
            </template>
          </el-table-column>
          <el-table-column label="应还收益" width="150" align="right">
            <template slot-scope="{row}">
              {{ formatCurrency(row.income) }}
            </template>
          </el-table-column>
          <el-table-column label="应还总额" width="150" align="right">
            <template slot-scope="{row}">
              <span style="color: #E6A23C; font-weight: bold;">{{ formatCurrency(row.total) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="remark" show-overflow-tooltip />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getPrincipalRuleList,
  createPrincipalRule,
  updatePrincipalRule,
  deletePrincipalRule,
  calculatePrincipal
} from '@/api/globalTreasurer-new/financialProductDefinition/principalRule'

export default {
  name: 'ProductPrincipalRuleManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    },
    currency(value) {
      if (!value) return '¥0.00'
      return '¥' + Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    parseTime(time, cFormat) {
      if (!time) return ''
      const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
      let date
      if (typeof time === 'string') {
        date = new Date(time)
      } else if (typeof time === 'object') {
        date = time
      } else {
        date = new Date(parseInt(time))
      }
      const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
      }
      const time_str = format.replace(/{([ymdhisa])+}/g, (result, key) => {
        const value = formatObj[key]
        if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
        return value.toString().padStart(2, '0')
      })
      return time_str
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
        productType: undefined
      },
      // 统计数据
      totalRules: 0,
      lumpSumRules: 0,
      installmentRules: 0,
      activeRules: 0,
      // 弹窗相关
      dialogFormVisible: false,
      dialogStatus: '',
      submitLoading: false,
      temp: {
        ruleId: undefined,
        ruleCode: '',
        ruleName: '',
        productType: 'BANK_WEALTH',
        repaymentMethod: 'MATURITY_LUMP_SUM',
        riskLevel: 'LOW',
        minInvestmentAmount: 0,
        maxInvestmentAmount: 0,
        principalGuaranteeRate: 100,
        description: '',
        isEnabled: 1
      },
      rules: {
        ruleCode: [{ required: true, message: '请输入规则编码', trigger: 'blur' }],
        ruleName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }]
      },
      // 计算对话框相关
      calculateDialogVisible: false,
      calculateForm: {
        ruleId: undefined,
        investmentAmount: 100000,
        investmentPeriod: 12,
        periodUnit: 'month',
        annualRate: 4.5
      },
      calculateRules: {
        ruleId: [{ required: true, message: '请选择规则', trigger: 'change' }],
        investmentAmount: [{ required: true, message: '请输入投资金额', trigger: 'blur' }],
        investmentPeriod: [{ required: true, message: '请输入投资期限', trigger: 'blur' }],
        periodUnit: [{ required: true, message: '请选择期限单位', trigger: 'change' }]
      },
      calculateResult: null,
      calculateLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        // 清理空字符串参数
        const params = {
          page: this.listQuery.page,
          limit: this.listQuery.limit
        }
        if (this.listQuery.ruleCode) {
          params.ruleCode = this.listQuery.ruleCode
        }
        if (this.listQuery.ruleName) {
          params.ruleName = this.listQuery.ruleName
        }
        if (this.listQuery.productType) {
          params.productType = this.listQuery.productType
        }

        console.log('前端查询参数:', params)
        const response = await getPrincipalRuleList(params)
        console.log('后端返回的完整数据:', response)
        const data = response.data || {}
        console.log('response.data:', data)
        console.log('data.tlist:', data.tlist)
        this.list = data.tlist || []
        this.total = data.totalRecord || 0
        console.log('赋值后的this.list:', this.list)
        console.log('this.list.length:', this.list.length)

        // 更新统计数据
        this.updateStatistics()
      } catch (error) {
        console.error('获取产品本金规则列表失败:', error)
        this.$message.error('获取产品本金规则列表失败')
      } finally {
        this.listLoading = false
      }
    },
    updateStatistics() {
      this.totalRules = this.list.length
      this.lumpSumRules = this.list.filter(item => item.repaymentMethod === 'MATURITY_LUMP_SUM').length
      this.installmentRules = this.list.filter(item => item.repaymentMethod === 'INSTALLMENT').length
      this.activeRules = this.list.filter(item => item.isEnabled === 1).length
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
        productType: undefined
      }
      this.getList()
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'] && this.$refs['dataForm'].clearValidate()
      })
    },
    resetTemp() {
      this.temp = {
        ruleId: undefined,
        ruleCode: '',
        ruleName: '',
        productType: 'BANK_WEALTH',
        repaymentMethod: 'MATURITY_LUMP_SUM',
        riskLevel: 'LOW',
        minInvestmentAmount: 0,
        maxInvestmentAmount: 0,
        principalGuaranteeRate: 100,
        description: '',
        isEnabled: 1
      }
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'] && this.$refs['dataForm'].clearValidate()
      })
    },
    async createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            this.submitLoading = true
            const response = await createPrincipalRule(this.temp)
            if (response && response.code === 1) {
              this.$message.success('新增成功')
              this.dialogFormVisible = false
              this.getList()
            } else {
              this.$message.error(response.message || '新增失败')
            }
          } catch (error) {
            console.error('新增失败:', error)
            this.$message.error('新增失败')
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    async updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            this.submitLoading = true
            const response = await updatePrincipalRule(this.temp)
            if (response && response.code === 1) {
              this.$message.success('更新成功')
              this.dialogFormVisible = false
              this.getList()
            } else {
              this.$message.error(response.message || '更新失败')
            }
          } catch (error) {
            console.error('更新失败:', error)
            this.$message.error('更新失败')
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    async handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          await deletePrincipalRule(row.ruleId)
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      })
    },
    handleCalculate() {
      this.calculateDialogVisible = true
      this.calculateResult = null
      this.calculateForm = {
        ruleId: undefined,
        investmentAmount: 100000,
        investmentPeriod: 12,
        periodUnit: 'month',
        annualRate: 4.5
      }
      this.$nextTick(() => {
        this.$refs['calculateForm'] && this.$refs['calculateForm'].clearValidate()
      })
    },
    handleDoCalculate() {
      this.$refs['calculateForm'].validate(async(valid) => {
        if (valid) {
          try {
            this.calculateLoading = true
            const response = await calculatePrincipal(this.calculateForm)
            if (response && response.code === 1) {
              this.calculateResult = response.data
              this.$message.success('计算成功')
            } else {
              this.$message.error(response.message || '计算失败')
            }
          } catch (error) {
            console.error('计算失败:', error)
            this.$message.error('计算失败')
          } finally {
            this.calculateLoading = false
          }
        }
      })
    },
    formatCurrency(value) {
      if (value === null || value === undefined) return '¥0.00'
      return '¥' + Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    handleExport() {
      try {
        const data = this.list.map(item => ({
          规则编码: item.ruleCode || '',
          规则名称: item.ruleName || '',
          本金处理方式: this.getPrincipalHandlingName(item.repaymentMethod) || '',
          描述: item.description || '',
          是否启用: item.isEnabled === 1 ? '是' : '否'
        }))
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `本金规则数据_${new Date().getTime()}.json`
        link.click()
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },
    getPrincipalHandlingColor(method) {
      const colorMap = {
        'MATURITY_LUMP_SUM': 'success',
        'INSTALLMENT': 'warning',
        'ANYTIME_REDEMPTION': 'info',
        'PRINCIPAL_GUARANTEED': ''
      }
      return colorMap[method] || ''
    },
    getPrincipalHandlingName(method) {
      const nameMap = {
        'MATURITY_LUMP_SUM': '到期一次性返还',
        'INSTALLMENT': '分期返还',
        'ANYTIME_REDEMPTION': '随时赎回',
        'PRINCIPAL_GUARANTEED': '保本保息'
      }
      return nameMap[method] || method
    },
    getProductTypeColor(productType) {
      const colorMap = {
        'BANK_WEALTH': '',
        'BOND': 'success',
        'EQUITY': 'warning',
        'FUND': 'info',
        'DEPOSIT': ''
      }
      return colorMap[productType] || ''
    },
    getProductTypeName(productType) {
      const nameMap = {
        'BANK_WEALTH': '银行理财',
        'BOND': '债券投资',
        'EQUITY': '股票投资',
        'FUND': '基金投资',
        'DEPOSIT': '存款产品'
      }
      return nameMap[productType] || productType
    },
    getRiskLevelColor(level) {
      const colorMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return colorMap[level] || ''
    },
    getRiskLevelName(level) {
      const nameMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      }
      return nameMap[level] || level
    }
  }
}
</script>

<style lang="scss" scoped>
.product-principal-rule-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        h2 {
          margin: 0 0 10px 0;
          font-size: 24px;
          color: #303133;
        }

        p {
          margin: 0;
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }

  .rule-overview {
    margin-bottom: 20px;

    .overview-card {
      .card-content {
        display: flex;
        align-items: center;

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 28px;
          margin-right: 15px;

          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
          }

          &.lump-sum-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
          }

          &.installment-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
          }

          &.active-icon {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
            color: white;
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }

          .card-value {
            font-size: 28px;
            font-weight: bold;
            color: #303133;
            margin-bottom: 4px;
          }

          .card-change {
            font-size: 12px;
            color: #67C23A;

            &.positive {
              color: #67C23A;
            }
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .calculate-result {
    margin-top: 20px;

    .result-cards {
      margin-bottom: 20px;

      .result-card {
        text-align: center;

        .result-label {
          font-size: 14px;
          color: #909399;
          margin-bottom: 10px;
        }

        .result-value {
          font-size: 24px;
          font-weight: bold;
          color: #303133;

          &.income {
            color: #67C23A;
          }

          &.total {
            color: #E6A23C;
          }

          &.guaranteed {
            color: #409EFF;
          }
        }
      }
    }
  }
}
</style>
