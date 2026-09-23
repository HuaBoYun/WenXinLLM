<template>
  <div class="product-interest-rule-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-coin"></i>
            产品利息规则管理
          </h2>
          <p class="page-description">管理金融产品利息计算规则，包括计息方式、利率配置、复利规则和结息周期</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增规则
          </el-button>
          <el-button type="success" icon="el-icon-calculator" @click="handleCalculate">
            利息计算
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
                <i class="el-icon-coin"></i>
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
              <div class="card-icon simple-icon">
                <i class="el-icon-s-finance"></i>
              </div>
              <div class="card-info">
                <div class="card-title">单利规则</div>
                <div class="card-value">{{ simpleInterestRules }}</div>
                <div class="card-change positive">简单计息</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon compound-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">复利规则</div>
                <div class="card-value">{{ compoundInterestRules }}</div>
                <div class="card-change">复合计息</div>
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
          <el-form-item label="利率类型">
            <el-select
              v-model="listQuery.interestRateType"
              placeholder="请选择利率类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="固定利率" value="FIXED" />
              <el-option label="浮动利率" value="FLOATING" />
            </el-select>
          </el-form-item>
          <el-form-item label="计息方式">
            <el-select
              v-model="listQuery.interestCalculationMethod"
              placeholder="请选择计息方式"
              clearable
              style="width: 120px;"
            >
              <el-option label="单利" value="SIMPLE" />
              <el-option label="复利" value="COMPOUND" />
              <el-option label="贴现" value="DISCOUNT" />
              <el-option label="浮动利率" value="FLOATING" />
            </el-select>
          </el-form-item>
          <el-form-item label="利息频率">
            <el-select
              v-model="listQuery.interestFrequency"
              placeholder="请选择利息频率"
              clearable
              style="width: 120px;"
            >
              <el-option label="按日" value="DAILY" />
              <el-option label="按月" value="MONTHLY" />
              <el-option label="按季" value="QUARTERLY" />
              <el-option label="按年" value="YEARLY" />
              <el-option label="到期一次性" value="MATURITY" />
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
      <el-table-column label="规则编码" prop="ruleCode" sortable="custom" align="center" min-width="120">
        <template slot-scope="{row}">
          <span>{{ row.ruleCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则名称" min-width="150" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.ruleName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="利率类型" min-width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getInterestRateTypeColor(row.interestRateType)" size="small">
            {{ getInterestRateTypeName(row.interestRateType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="利率(%)" min-width="90" align="center">
        <template slot-scope="{row}">
          <span>{{ row.interestRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计息方式" min-width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getCalculationMethodColor(row.interestCalculationMethod)" size="small">
            {{ getCalculationMethodName(row.interestCalculationMethod) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="利息频率" min-width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getInterestFrequencyColor(row.interestFrequency)" size="small">
            {{ getInterestFrequencyName(row.interestFrequency) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ parseTime(row.createTime, '{y}-{m}-{d}') }}</span>
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

    <!-- 创建/编辑利息规则对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新增利息规则' : '编辑利息规则'" :visible.sync="dialogFormVisible" width="900px" :close-on-click-modal="false">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="140px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则编码" prop="ruleCode">
              <el-input v-model="temp.ruleCode" placeholder="请输入规则编码" :disabled="dialogStatus === 'update'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="temp.ruleName" placeholder="请输入规则名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计息方式" prop="interestCalculationMethod">
              <el-select v-model="temp.interestCalculationMethod" placeholder="请选择计息方式" style="width: 100%;">
                <el-option label="单利" value="SIMPLE" />
                <el-option label="复利" value="COMPOUND" />
                <el-option label="贴现" value="DISCOUNT" />
                <el-option label="浮动利率" value="FLOATING" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="利率类型" prop="interestRateType">
              <el-select v-model="temp.interestRateType" placeholder="请选择利率类型" style="width: 100%;">
                <el-option label="固定利率" value="FIXED" />
                <el-option label="浮动利率" value="FLOATING" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="利率(%)" prop="interestRate">
              <el-input-number v-model="temp.interestRate" :precision="4" :min="0" :max="100" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="利息频率" prop="interestFrequency">
              <el-select v-model="temp.interestFrequency" placeholder="请选择利息频率" style="width: 100%;">
                <el-option label="按日" value="DAILY" />
                <el-option label="按月" value="MONTHLY" />
                <el-option label="按季" value="QUARTERLY" />
                <el-option label="按年" value="YEARLY" />
                <el-option label="到期一次性" value="MATURITY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="天数计算惯例" prop="dayCountConvention">
              <el-select v-model="temp.dayCountConvention" placeholder="请选择天数计算惯例" style="width: 100%;">
                <el-option label="实际天数/365" value="ACT_365" />
                <el-option label="实际天数/360" value="ACT_360" />
                <el-option label="30/360" value="30_360" />
                <el-option label="实际天数/实际天数" value="ACT_ACT" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="复利方法" prop="compoundingMethod">
              <el-select v-model="temp.compoundingMethod" placeholder="请选择复利方法" style="width: 100%;">
                <el-option label="按日复利" value="DAILY" />
                <el-option label="按月复利" value="MONTHLY" />
                <el-option label="按季复利" value="QUARTERLY" />
                <el-option label="按年复利" value="YEARLY" />
                <el-option label="连续复利" value="CONTINUOUS" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="isEnabled">
              <el-switch v-model="temp.isEnabled" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述" prop="description">
          <el-input v-model="temp.description" type="textarea" :rows="3" placeholder="请输入描述信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 利息计算对话框 -->
    <el-dialog title="利息计算器" :visible.sync="calculateDialogVisible" width="700px" :close-on-click-modal="false">
      <el-form ref="calculateForm" :model="calculateForm" label-position="right" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="本金金额" prop="principal">
              <el-input-number v-model="calculateForm.principal" :precision="2" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年利率(%)" prop="annualRate">
              <el-input-number v-model="calculateForm.annualRate" :precision="4" :min="0" :max="100" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="投资期限(天)" prop="termDays">
              <el-input-number v-model="calculateForm.termDays" :min="1" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计息方式" prop="calculationMethod">
              <el-select v-model="calculateForm.calculationMethod" placeholder="请选择计息方式" style="width: 100%;">
                <el-option label="单利" value="SIMPLE" />
                <el-option label="复利" value="COMPOUND" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计息基准" prop="interestBasis">
              <el-select v-model="calculateForm.interestBasis" placeholder="请选择计息基准" style="width: 100%;">
                <el-option label="实际天数/365" value="ACT_365" />
                <el-option label="实际天数/360" value="ACT_360" />
                <el-option label="30/360" value="30_360" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="复利频率" prop="compoundFrequency">
              <el-select v-model="calculateForm.compoundFrequency" placeholder="请选择复利频率" style="width: 100%;">
                <el-option label="按年复利" value="YEARLY" />
                <el-option label="按月复利" value="MONTHLY" />
                <el-option label="按日复利" value="DAILY" />
                <el-option label="连续复利" value="CONTINUOUS" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div class="calculate-result" v-if="calculateResult">
        <el-card class="result-card">
          <div slot="header">
            <span>计算结果</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="result-item">
                <div class="result-label">利息收入</div>
                <div class="result-value">¥{{ calculateResult.interest.toFixed(2) }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="result-item">
                <div class="result-label">本息合计</div>
                <div class="result-value">¥{{ calculateResult.total.toFixed(2) }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="result-item">
                <div class="result-label">收益率</div>
                <div class="result-value">{{ calculateResult.rateOfReturn.toFixed(4) }}%</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="calculateDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="performCalculation" :loading="calculating">计算</el-button>
      </div>
    </el-dialog>

    <!-- 导出配置对话框 -->
    <el-dialog title="导出利息规则配置" :visible.sync="exportDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form ref="exportForm" :model="exportForm" label-position="right" label-width="100px">
        <el-form-item label="导出范围" prop="exportRange">
          <el-radio-group v-model="exportForm.exportRange">
            <el-radio label="all">全部数据</el-radio>
            <el-radio label="filtered">当前筛选结果</el-radio>
            <el-radio label="selected">选中数据</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="文件格式" prop="fileFormat">
          <el-radio-group v-model="exportForm.fileFormat">
            <el-radio label="xlsx">Excel格式(.xlsx)</el-radio>
            <el-radio label="csv">CSV格式(.csv)</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="exportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmExport" :loading="exportLoading">导出</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getInterestRuleList,
  createInterestRule,
  updateInterestRule,
  deleteInterestRule
} from '@/api/globalTreasurer-new/financialProductDefinition/interestRule'

export default {
  name: 'ProductInterestRuleManage',
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
        interestRateType: undefined,
        interestCalculationMethod: undefined,
        interestFrequency: undefined
      },
      totalRules: 0,
      enabledRules: 0,
      activeRules: 0,
      simpleInterestRules: 0,
      compoundInterestRules: 0,

      // 对话框相关
      dialogFormVisible: false,
      dialogStatus: '',
      calculateDialogVisible: false,
      exportDialogVisible: false,
      calculating: false,
      exportLoading: false,
      calculateResult: null,

      // 临时数据
      temp: {
        ruleId: undefined,
        ruleCode: '',
        ruleName: '',
        interestCalculationMethod: '',
        interestRateType: '',
        interestRate: 0,
        interestFrequency: '',
        dayCountConvention: 'ACT_365',
        compoundingMethod: 'YEARLY',
        description: '',
        isEnabled: 1
      },

      // 计算表单
      calculateForm: {
        principal: 100000,
        annualRate: 3.5,
        termDays: 365,
        calculationMethod: 'SIMPLE',
        interestBasis: 'ACT_365',
        compoundFrequency: 'YEARLY'
      },

      // 导出表单
      exportForm: {
        exportRange: 'all',
        fileFormat: 'xlsx'
      },

      // 表单验证规则
      rules: {
        ruleCode: [
          { required: true, message: '请输入规则编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        interestCalculationMethod: [
          { required: true, message: '请选择计息方式', trigger: 'change' }
        ],
        interestRateType: [
          { required: true, message: '请选择利率类型', trigger: 'change' }
        ],
        interestRate: [
          { required: true, message: '请输入利率', trigger: 'blur' },
          { type: 'number', min: 0, max: 100, message: '利率必须在0-100之间', trigger: 'blur' }
        ],
        interestFrequency: [
          { required: true, message: '请选择利息频率', trigger: 'change' }
        ]
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
        // 转换参数格式以匹配后端接口
        // 后端只支持: pageNo, pageSize, ruleCode, ruleName, productType, isEnabled
        const params = {
          pageNo: this.listQuery.page,
          pageSize: this.listQuery.limit,
          ruleCode: this.listQuery.ruleCode,
          ruleName: this.listQuery.ruleName
        }
        // 调用真实API
        const response = await getInterestRuleList(params)
        console.log('后端返回的完整数据:', response)
        // 响应拦截器返回的是完整JsonBean对象,需要取data字段
        const data = response.data || {}
        console.log('response.data:', data)
        console.log('data.tlist:', data.tlist)
        let list = data.tlist || []

        // 前端进行额外的筛选（因为后端暂不支持这些参数）
        if (this.listQuery.interestRateType) {
          list = list.filter(item => item.interestRateType === this.listQuery.interestRateType)
        }
        if (this.listQuery.interestCalculationMethod) {
          list = list.filter(item => item.interestCalculationMethod === this.listQuery.interestCalculationMethod)
        }
        if (this.listQuery.interestFrequency) {
          list = list.filter(item => item.interestFrequency === this.listQuery.interestFrequency)
        }

        this.list = list
        this.total = data.totalRecord || 0
        console.log('赋值后的this.list:', this.list)
        console.log('this.list.length:', this.list.length)
        this.updateStatistics()
      } catch (error) {
        console.error('获取产品利息规则列表失败:', error)
        this.$message.error('获取数据失败')
      } finally {
        this.listLoading = false
      }
    },
    updateStatistics() {
      this.totalRules = this.list.length
      this.activeRules = this.list.filter(item => item.isEnabled === 1).length
      this.simpleInterestRules = this.list.filter(item => item.interestCalculationMethod === 'SIMPLE').length
      this.compoundInterestRules = this.list.filter(item => item.interestCalculationMethod === 'COMPOUND').length
    },
    getInterestRateTypeName(type) {
      const typeMap = {
        'FIXED': '固定利率',
        'FLOATING': '浮动利率'
      }
      return typeMap[type] || type
    },
    getInterestRateTypeColor(type) {
      const colorMap = {
        'FIXED': 'success',
        'FLOATING': 'warning'
      }
      return colorMap[type] || 'default'
    },
    getCalculationMethodName(method) {
      const methodMap = {
        'SIMPLE': '单利',
        'COMPOUND': '复利',
        'DISCOUNT': '贴现',
        'FLOATING': '浮动利率'
      }
      return methodMap[method] || method
    },
    getCalculationMethodColor(method) {
      const colorMap = {
        'SIMPLE': 'primary',
        'COMPOUND': 'success',
        'DISCOUNT': 'warning',
        'FLOATING': 'info'
      }
      return colorMap[method] || 'default'
    },
    getInterestFrequencyName(frequency) {
      const frequencyMap = {
        'DAILY': '按日',
        'MONTHLY': '按月',
        'QUARTERLY': '按季',
        'YEARLY': '按年',
        'MATURITY': '到期一次性'
      }
      return frequencyMap[frequency] || frequency
    },
    getInterestFrequencyColor(frequency) {
      const colorMap = {
        'DAILY': 'info',
        'MONTHLY': 'primary',
        'QUARTERLY': 'success',
        'YEARLY': 'warning',
        'MATURITY': 'danger'
      }
      return colorMap[frequency] || 'default'
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
        interestRateType: undefined,
        interestCalculationMethod: undefined,
        interestFrequency: undefined
      }
      this.getList()
    },
    // 打开创建对话框
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 重置临时数据
    resetTemp() {
      this.temp = {
        ruleId: undefined,
        ruleCode: '',
        ruleName: '',
        interestCalculationMethod: '',
        interestRateType: '',
        interestRate: 0,
        interestFrequency: '',
        dayCountConvention: 'ACT_365',
        compoundingMethod: 'YEARLY',
        description: '',
        isEnabled: 1
      }
    },

    // 创建数据
    async createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await createInterestRule(this.temp)
            const data = response.data || {}
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            } else {
              this.$message.error(data.message || '创建失败')
            }
          } catch (error) {
            console.error('创建失败:', error)
            this.$message.error('创建失败')
          }
        }
      })
    },

    // 打开编辑对话框
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 更新数据
    async updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await updateInterestRule(this.temp)
            const data = response.data || {}
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            } else {
              this.$message.error(data.message || '更新失败')
            }
          } catch (error) {
            console.error('更新失败:', error)
            this.$message.error('更新失败')
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteInterestRule(row.ruleId)
          if (response.code === 1) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      })
    },

    // 利息计算
    handleCalculate() {
      this.calculateResult = null
      this.calculateDialogVisible = true
    },

    // 执行利息计算
    performCalculation() {
      this.calculating = true

      setTimeout(() => {
        const { principal, annualRate, termDays, calculationMethod, interestBasis, compoundFrequency } = this.calculateForm

        // 计算天数基准
        let dayBasis = 365
        if (interestBasis === 'ACT_360') {
          dayBasis = 360
        } else if (interestBasis === '30_360') {
          dayBasis = 360
        }

        let interest = 0

        if (calculationMethod === 'SIMPLE') {
          // 单利计算
          interest = principal * (annualRate / 100) * (termDays / dayBasis)
        } else if (calculationMethod === 'COMPOUND') {
          // 复利计算
          let n = 1 // 复利次数
          if (compoundFrequency === 'MONTHLY') {
            n = 12
          } else if (compoundFrequency === 'DAILY') {
            n = 365
          } else if (compoundFrequency === 'CONTINUOUS') {
            // 连续复利
            interest = principal * (Math.exp((annualRate / 100) * (termDays / dayBasis)) - 1)
          }

          if (compoundFrequency !== 'CONTINUOUS') {
            const r = annualRate / 100 / n
            const t = termDays / dayBasis
            interest = principal * (Math.pow(1 + r, n * t) - 1)
          }
        }

        const total = principal + interest
        const rateOfReturn = (interest / principal) * 100

        this.calculateResult = {
          interest,
          total,
          rateOfReturn
        }

        this.calculating = false
      }, 1000)
    },

    // 导出配置
    handleExport() {
      this.exportDialogVisible = true
    },

    // 确认导出
    async confirmExport() {
      this.exportLoading = true
      try {
        const params = {
          exportRange: this.exportForm.exportRange,
          fileFormat: this.exportForm.fileFormat
        }
        // TODO: 调用导出API
        // const response = await this.exportInterestRules(params)
        this.$message.info('导出功能开发中...')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      } finally {
        this.exportLoading = false
      }
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
      return format.replace(/{([ymdhisa])+}/g, (result, key) => {
        const value = formatObj[key]
        if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
        return value.toString().padStart(2, '0')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.product-interest-rule-manage {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;

  .page-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 30px;
    border-radius: 12px;
    margin-bottom: 20px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        .page-title {
          margin: 0 0 10px 0;
          font-size: 28px;
          font-weight: 600;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            font-size: 32px;
          }
        }

        .page-description {
          margin: 0;
          font-size: 14px;
          opacity: 0.9;
          line-height: 1.5;
        }
      }

      .header-right {
        .el-button {
          border-radius: 20px;
          padding: 10px 20px;
          font-weight: 500;

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
          }
        }
      }
    }
  }

  .rule-overview {
    margin-bottom: 20px;

    .overview-card {
      border-radius: 12px;
      border: none;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
      }

      .card-content {
        display: flex;
        align-items: center;

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 15px;
          font-size: 24px;
          color: white;

          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.enabled-icon {
            background: linear-gradient(135deg, #00c851 0%, #00a846 100%);
          }

          &.simple-icon {
            background: linear-gradient(135deg, #33b5e5 0%, #0099cc 100%);
          }

          &.compound-icon {
            background: linear-gradient(135deg, #ffbb33 0%, #ff8800 100%);
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 12px;
            color: #8c8c8c;
            margin-bottom: 5px;
            font-weight: 500;
          }

          .card-value {
            font-size: 24px;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 5px;
          }

          .card-change {
            font-size: 12px;
            color: #8c8c8c;
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 12px;
    border: none;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    .search-form {
      .el-form-item {
        margin-bottom: 0;

        .el-input, .el-select {
          .el-input__inner, .el-select__tags {
            border-radius: 8px;
            border: 1px solid #d9d9d9;
            transition: all 0.3s ease;

            &:hover {
              border-color: #667eea;
            }

            &:focus {
              border-color: #667eea;
              box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
            }
          }
        }

        .el-button {
          border-radius: 8px;
          padding: 9px 15px;

          &.el-button--primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;

            &:hover {
              background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
              transform: translateY(-1px);
            }
          }
        }
      }
    }
  }

  .el-table {
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    th {
      background-color: #fafafa;
      font-weight: 600;
      color: #2c3e50;
    }

    td {
      padding: 12px 0;
    }

    .el-tag {
      border-radius: 12px;
      font-weight: 500;
      border: none;

      &.el-tag--success {
        background: linear-gradient(135deg, #00c851 0%, #00a846 100%);
      }

      &.el-tag--danger {
        background: linear-gradient(135deg, #ff4444 0%, #cc0000 100%);
      }

      &.el-tag--primary {
        background: linear-gradient(135deg, #33b5e5 0%, #0099cc 100%);
      }

      &.el-tag--warning {
        background: linear-gradient(135deg, #ffbb33 0%, #ff8800 100%);
      }

      &.el-tag--info {
        background: linear-gradient(135deg, #aa66cc 0%, #9933cc 100%);
      }
    }

    .el-button {
      border-radius: 6px;

      &.el-button--primary {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border: none;

        &:hover {
          background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
        }
      }

      &.el-button--danger {
        background: linear-gradient(135deg, #ff4444 0%, #cc0000 100%);
        border: none;

        &:hover {
          background: linear-gradient(135deg, #ff6666 0%, #ff1a1a 100%);
        }
      }

      &.el-button--success {
        background: linear-gradient(135deg, #00c851 0%, #00a846 100%);
        border: none;

        &:hover {
          background: linear-gradient(135deg, #00e060 0%, #00c040 100%);
        }
      }
    }
  }

  .el-dialog {
    border-radius: 12px;
    overflow: hidden;

    .el-dialog__header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 20px 30px;

      .el-dialog__title {
        font-size: 18px;
        font-weight: 600;
      }

      .el-dialog__headerbtn {
        .el-dialog__close {
          color: white;

          &:hover {
            color: #f0f0f0;
          }
        }
      }
    }

    .el-dialog__body {
      padding: 30px;

      .el-form-item {
        margin-bottom: 22px;

        .el-form-item__label {
          font-weight: 600;
          color: #2c3e50;
        }

        .el-input, .el-select, .el-input-number {
          .el-input__inner, .el-select__tags {
            border-radius: 8px;
            border: 1px solid #d9d9d9;
            transition: all 0.3s ease;

            &:hover {
              border-color: #667eea;
            }

            &:focus {
              border-color: #667eea;
              box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
            }
          }
        }

        .el-switch {
          .el-switch__core {
            border-radius: 12px;
          }

          &.is-checked .el-switch__core {
            background-color: #667eea;
          }
        }

        .el-radio-group {
          .el-radio {
            margin-bottom: 8px;

            .el-radio__inner {
              border-radius: 4px;
            }

            &.is-checked .el-radio__inner {
              background-color: #667eea;
              border-color: #667eea;
            }
          }
        }
      }
    }

    .el-dialog__footer {
      padding: 20px 30px;
      border-top: 1px solid #f0f0f0;

      .dialog-footer {
        text-align: right;

        .el-button {
          border-radius: 8px;
          padding: 10px 20px;
          font-weight: 500;
          margin-left: 10px;

          &.el-button--primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;

            &:hover {
              background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
              transform: translateY(-1px);
            }
          }
        }
      }
    }
  }

  .calculate-result {
    margin-top: 20px;

    .result-card {
      border-radius: 12px;
      border: none;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);

      .el-card__header {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        font-weight: 600;
      }

      .result-item {
        text-align: center;

        .result-label {
          font-size: 14px;
          color: #8c8c8c;
          margin-bottom: 8px;
        }

        .result-value {
          font-size: 20px;
          font-weight: 700;
          color: #2c3e50;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .product-interest-rule-manage {
    padding: 10px;

    .page-header .header-content {
      flex-direction: column;
      text-align: center;

      .header-right {
        margin-top: 20px;

        .el-button {
          width: 100%;
          margin-bottom: 10px;
        }
      }
    }

    .rule-overview {
      .el-col {
        margin-bottom: 15px;
      }
    }

    .search-form {
      .el-form-item {
        margin-bottom: 10px;
      }
    }
  }
}
</style>
