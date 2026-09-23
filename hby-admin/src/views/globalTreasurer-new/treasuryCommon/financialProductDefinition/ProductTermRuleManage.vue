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
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.isEnabled"
              placeholder="请选择状态"
              clearable
              style="width: 100px;"
            >
              <el-option label="全部" value="" />
              <el-option label="启用" value="1" />
              <el-option label="禁用" value="0" />
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
          <span>{{ row.termRuleCode || row.ruleCode || row.code || '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="期限规则名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.termRuleName || row.ruleName || row.name || '' }}</span>
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
          <span>{{ (row.earlyRedemptionFeeRate || 0) }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ formatCreateTime(row.createTime) }}</span>
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
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新增期限规则' : '编辑期限规则'" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-width="120px">
        <el-form-item label="期限规则编码" prop="termRuleCode">
          <el-input v-model="temp.termRuleCode" placeholder="请输入期限规则编码" :disabled="dialogStatus === 'update'" />
        </el-form-item>
        <el-form-item label="期限规则名称" prop="termRuleName">
          <el-input v-model="temp.termRuleName" placeholder="请输入期限规则名称" />
        </el-form-item>
        <el-form-item label="产品类型" prop="productType">
          <el-select v-model="temp.productType" placeholder="请选择产品类型" style="width: 100%;">
            <el-option label="银行理财" value="BANK_WEALTH" />
            <el-option label="债券投资" value="BOND" />
            <el-option label="股票投资" value="EQUITY" />
            <el-option label="基金投资" value="FUND" />
            <el-option label="存款产品" value="DEPOSIT" />
          </el-select>
        </el-form-item>
        <el-form-item label="期限类型" prop="termType">
          <el-select v-model="temp.termType" placeholder="请选择期限类型" style="width: 100%;">
            <el-option label="固定期限" value="FIXED" />
            <el-option label="开放式" value="OPEN_ENDED" />
            <el-option label="定期开放" value="PERIODIC_OPEN" />
            <el-option label="滚动期限" value="ROLLING" />
          </el-select>
        </el-form-item>
        <el-form-item label="期限单位" prop="termUnit">
          <el-select v-model="temp.termUnit" placeholder="请选择期限单位" style="width: 100%;">
            <el-option label="天" value="DAYS" />
            <el-option label="月" value="MONTHS" />
            <el-option label="年" value="YEARS" />
          </el-select>
        </el-form-item>
        <el-form-item label="最小期限">
          <el-input-number v-model="temp.minTerm" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="最大期限">
          <el-input-number v-model="temp.maxTerm" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="锁定期">
          <el-input-number v-model="temp.lockPeriod" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="提前赎回费率(%)">
          <el-input-number v-model="temp.earlyRedemptionFeeRate" :min="0" :max="100" :precision="2" style="width: 100%;" />
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

    <!-- 期限计算对话框 -->
    <el-dialog
      title="期限计算"
      :visible.sync="calculateDialogVisible"
      width="600px"
      :close-on-click-modal="false">
      <el-form ref="calculateForm" :model="calculateForm" label-width="120px">
        <el-form-item label="期限规则" prop="ruleId">
          <el-select v-model="calculateForm.ruleId" placeholder="请选择期限规则" filterable>
            <el-option
              v-for="rule in list"
              :key="rule.termRuleId || rule.ruleId || rule.id"
              :label="`${rule.termRuleCode || rule.ruleCode || rule.code || ''} - ${rule.termRuleName || rule.ruleName || rule.name || ''}`"
              :value="rule.termRuleId || rule.ruleId || rule.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="起始日期" prop="startDate">
          <el-date-picker
            v-model="calculateForm.startDate"
            type="date"
            placeholder="选择起始日期"
            value-format="yyyy-MM-dd"
            :picker-options="datePickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="期限设置">
          <el-radio-group v-model="calculateForm.calculateType">
            <el-radio label="天数">
              <el-input-number
                v-model="calculateForm.termDays"
                :min="1"
                placeholder="天数"
                :disabled="calculateForm.calculateType !== 'days'" />
            </el-radio>
            <el-radio label="月数">
              <el-input-number
                v-model="calculateForm.termMonths"
                :min="1"
                placeholder="月数"
                :disabled="calculateForm.calculateType !== 'months'" />
            </el-radio>
            <el-radio label="年数">
              <el-input-number
                v-model="calculateForm.termYears"
                :min="1"
                placeholder="年数"
                :disabled="calculateForm.calculateType !== 'years'" />
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="calculateDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="calculateLoading" @click="handleCalculate">计算</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getProductTermRuleList,
  deleteProductTermRule,
  getProductTermRuleStatistics,
  calculateTerm
} from '@/api/globalTreasurer/financialProductDefinition/productTermRuleManage'

export default {
  name: 'ProductTermRuleManage',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        pageNo: 1,
        pageSize: 10,
        termRuleCode: undefined,
        termRuleName: undefined,
        productType: undefined,
        termType: undefined,
        termUnit: undefined,
        isEnabled: undefined
      },
      // 统计数据
      totalRules: 0,
      shortTermRules: 0,
      mediumTermRules: 0,
      longTermRules: 0,
      // 弹窗相关
      dialogFormVisible: false,
      dialogStatus: '',
      submitLoading: false,
      temp: {
        termRuleId: undefined,
        termRuleCode: '',
        termRuleName: '',
        productType: 'BANK_WEALTH',
        termType: 'FIXED',
        termUnit: 'MONTHS',
        minTerm: 0,
        maxTerm: 0,
        lockPeriod: 0,
        earlyRedemptionFeeRate: 0,
        isEnabled: 1,
        description: ''
      },
      rules: {
        termRuleCode: [{ required: true, message: '请输入期限规则编码', trigger: 'blur' }],
        termRuleName: [{ required: true, message: '请输入期限规则名称', trigger: 'blur' }],
        productType: [{ required: true, message: '请选择产品类型', trigger: 'change' }],
        termType: [{ required: true, message: '请选择期限类型', trigger: 'change' }],
        termUnit: [{ required: true, message: '请选择期限单位', trigger: 'change' }]
      },
      // 期限计算相关
      calculateDialogVisible: false,
      calculateLoading: false,
      calculateForm: {
        ruleId: undefined,
        startDate: '',
        termDays: undefined,
        termMonths: undefined,
        termYears: undefined,
        calculateType: 'days'
      },
      datePickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        }
      }
    }
  },
  created() {
    this.getList()
    this.getStatistics()
  },
  methods: {
    /**
     * 解析期限值 - 处理"1year"、"2months"等格式
     */
    parseTermValue(value) {
      if (!value) return { value: 0, unit: 'MONTHS' }

      // 如果是数字，直接返回
      if (typeof value === 'number') {
        return { value, unit: 'MONTHS' }
      }

      // 如果是字符串，解析格式
      if (typeof value === 'string') {
        const lowerValue = value.toLowerCase().trim()

        console.log('解析期限值:', value, '->', lowerValue)

        // 匹配数字+单位格式 (如 "1year", "2 months", "3days", "1 year")
        const match = lowerValue.match(/^(\d+)\s*(year|years|month|months|day|days)?/)

        console.log('匹配结果:', match)

        if (match) {
          const numValue = parseInt(match[1], 10)
          const unitStr = match[2] || ''

          console.log('数值:', numValue, '单位字符串:', unitStr)

          // 映射单位
          let unit = 'MONTHS'
          if (unitStr.includes('year')) {
            unit = 'YEARS'
          } else if (unitStr.includes('month')) {
            unit = 'MONTHS'
          } else if (unitStr.includes('day')) {
            unit = 'DAYS'
          }

          console.log('映射后单位:', unit)
          return { value: numValue, unit }
        }

        // 尝试直接转换为数字
        const numValue = parseInt(value, 10)
        if (!isNaN(numValue)) {
          return { value: numValue, unit: 'MONTHS' }
        }
      }

      console.log('解析失败，返回默认值')
      return { value: 0, unit: 'MONTHS' }
    },

    /**
     * 字段映射函数 - 将后端字段映射到前端期望的字段名
     */
    mapFieldNames(list) {
      if (!Array.isArray(list)) return []
      return list.map(item => {
        console.log('原始数据:', item)

        // 解析期限值
        const minTermParsed = this.parseTermValue(item.minTerm)
        const maxTermParsed = this.parseTermValue(item.maxTerm)
        const lockPeriodParsed = this.parseTermValue(item.lockPeriod)

        // 获取产品类型 - 尝试多种可能的字段名
        // 注意：如果数据库中没有PRODUCT_TYPE字段，会使用默认值或根据规则编码推断
        const productType = item.productType || item.PRODUCT_TYPE ||
          this.inferProductTypeFromRuleCode(item.ruleCode || item.RULE_CODE) ||
          'BANK_WEALTH'

        // 获取期限类型 - 尝试多种可能的字段名
        // 注意：如果数据库中没有TERM_TYPE字段，会使用默认值或根据规则名称推断
        const termType = item.termType || item.TERM_TYPE ||
          this.inferTermTypeFromRuleName(item.ruleName || item.RULE_NAME) ||
          'FIXED'

        // 获取期限单位 - 尝试多种可能的字段名
        const termUnit = item.termUnit || item.TERM_UNIT || item.unit || item.UNIT || minTermParsed.unit || 'MONTHS'

        const mapped = {
          termRuleId: item.termRuleId || item.ruleId || item.id || item.RULE_ID || '',
          termRuleCode: item.termRuleCode || item.ruleCode || item.code || item.RULE_CODE || '',
          termRuleName: item.termRuleName || item.ruleName || item.name || item.RULE_NAME || '',
          productType: productType,
          termType: termType,
          termUnit: termUnit,
          minTerm: minTermParsed.value,
          maxTerm: maxTermParsed.value,
          lockPeriod: lockPeriodParsed.value,
          earlyRedemptionFeeRate: item.earlyRedemptionFeeRate || item.EARLY_REDEMPTION_FEE_RATE || item.redemptionFeeRate || 0,
          isEnabled: item.isEnabled !== undefined ? item.isEnabled : (item.enabled !== undefined ? item.enabled : (item.IS_ENABLED !== undefined ? item.IS_ENABLED : 1)),
          description: item.description || item.DESCRIPTION || item.remark || '',
          createTime: item.createTime || item.CREATE_TIME || item.createdTime || '',
          updateTime: item.updateTime || item.UPDATE_TIME || item.updatedTime || ''
        }

        console.log('映射后数据:', mapped)
        console.log('产品类型:', productType, '期限类型:', termType, 'ruleId:', mapped.termRuleId)
        return mapped
      })
    },

    /**
     * 根据规则编码推断产品类型（临时方案）
     */
    inferProductTypeFromRuleCode(ruleCode) {
      if (!ruleCode) return null
      const code = ruleCode.toUpperCase()
      if (code.includes('BANK') || code.includes('BANK_WEALTH')) return 'BANK_WEALTH'
      if (code.includes('BOND')) return 'BOND'
      if (code.includes('EQUITY') || code.includes('STOCK')) return 'EQUITY'
      if (code.includes('FUND')) return 'FUND'
      if (code.includes('DEPOSIT')) return 'DEPOSIT'
      return null
    },

    /**
     * 根据规则名称推断期限类型（临时方案）
     */
    inferTermTypeFromRuleName(ruleName) {
      if (!ruleName) return null
      const name = ruleName.toUpperCase()
      if (name.includes('FIXED') || name.includes('固定')) return 'FIXED'
      if (name.includes('OPEN') || name.includes('开放式')) return 'OPEN_ENDED'
      if (name.includes('PERIODIC') || name.includes('定期')) return 'PERIODIC_OPEN'
      if (name.includes('ROLLING') || name.includes('滚动')) return 'ROLLING'
      return null
    },
    /**
     * 获取期限规则列表
     */
    getList() {
      this.listLoading = true
      const params = {
        pageNo: this.listQuery.pageNo,
        pageSize: this.listQuery.pageSize,
        ruleCode: this.listQuery.termRuleCode,
        ruleName: this.listQuery.termRuleName,
        productType: this.listQuery.productType,
        termType: this.listQuery.termType,
        termUnit: this.listQuery.termUnit,
        isEnabled: this.listQuery.isEnabled
      }

      getProductTermRuleList(params).then(response => {
        if (response.code === 1) {
          // 兼容多种数据格式
          const data = response.data || {}
          // 支持 records、list、tlist 等多种字段名
          const rawList = data.records || data.list || data.tlist || []
          // 映射字段名
          this.list = this.mapFieldNames(rawList)
          // 支持 total、totalRecord 等多种字段名
          // 如果totalRecord是字符串"0"但列表有数据，使用列表长度作为总数
          const totalFromBackend = data.total || data.totalRecord || 0
          const totalNum = typeof totalFromBackend === 'string' ? parseInt(totalFromBackend, 10) : totalFromBackend
          // 如果后端返回的total是0但列表有数据，使用列表长度作为总数
          this.total = (totalNum === 0 && rawList.length > 0) ? rawList.length : totalNum
          console.log('期限规则列表数据:', this.list, '总数:', this.total, '后端返回总数:', totalFromBackend)
        } else {
          this.$message.error(response.msg || response.message || '获取期限规则列表失败')
          this.list = []
          this.total = 0
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取期限规则列表失败:', error)
        this.$message.error('获取期限规则列表失败')
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },

    /**
     * 获取统计数据
     */
    getStatistics() {
      getProductTermRuleStatistics().then(response => {
        if (response.code === 1) {
          const data = response.data || {}
          this.totalRules = data.totalRules || data.total || 0
          this.shortTermRules = data.shortTermRules || data.shortTerm || 0
          this.mediumTermRules = data.mediumTermRules || data.mediumTerm || 0
          this.longTermRules = data.longTermRules || data.longTerm || 0
          console.log('统计数据:', data)
        } else {
          console.error('获取统计数据失败:', response.msg || response.message)
          // 使用默认值
          this.totalRules = 0
          this.shortTermRules = 0
          this.mediumTermRules = 0
          this.longTermRules = 0
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
        // 使用默认值
        this.totalRules = 0
        this.shortTermRules = 0
        this.mediumTermRules = 0
        this.longTermRules = 0
      })
    },

    /**
     * 搜索
     */
    handleFilter() {
      this.listQuery.pageNo = 1
      this.getList()
    },

    /**
     * 重置搜索条件
     */
    handleReset() {
      this.listQuery = {
        pageNo: 1,
        pageSize: 10,
        termRuleCode: undefined,
        termRuleName: undefined,
        productType: undefined,
        termType: undefined,
        termUnit: undefined,
        isEnabled: undefined
      }
      this.getList()
    },

    /**
     * 新增
     */
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
        termRuleId: undefined,
        termRuleCode: '',
        termRuleName: '',
        productType: 'BANK_WEALTH',
        termType: 'FIXED',
        termUnit: 'MONTHS',
        minTerm: 0,
        maxTerm: 0,
        lockPeriod: 0,
        earlyRedemptionFeeRate: 0,
        isEnabled: 1,
        description: ''
      }
    },

    /**
     * 转换请求参数 - 将前端字段映射到后端期望的字段名和格式
     */
    transformRequestParams(data) {
      const params = {
        // Java实体类使用驼峰命名
        ruleCode: data.termRuleCode || '',
        ruleName: data.termRuleName || '',
        productType: data.productType || 'BANK_WEALTH',
        termType: data.termType || 'FIXED',
        termUnit: data.termUnit || 'MONTHS',

        // 期限值
        minTerm: data.minTerm || 0,
        maxTerm: data.maxTerm || 0,
        lockPeriod: data.lockPeriod || 0,

        // 费率
        earlyRedemptionFeeRate: data.earlyRedemptionFeeRate || 0,

        // 状态转换：确保使用正确的格式
        isEnabled: data.isEnabled !== undefined ? (data.isEnabled === 1 || data.isEnabled === true ? 1 : 0) : 1,

        description: data.description || ''
      }

      // 如果有ID，添加到参数中（更新时需要）
      if (data.termRuleId) {
        params.ruleId = data.termRuleId
      }
      if (data.ruleId) {
        params.ruleId = data.ruleId
      }
      if (data.id) {
        params.ruleId = data.id
      }

      console.log('转换前:', data)
      console.log('转换后:', params)

      return params
    },

    /**
     * 编辑
     */
    handleUpdate(row) {
      // 解析期限值
      const minTermParsed = this.parseTermValue(row.minTerm)
      const maxTermParsed = this.parseTermValue(row.maxTerm)
      const lockPeriodParsed = this.parseTermValue(row.lockPeriod)

      // 确保所有必需字段都存在
      this.temp = {
        termRuleId: row.termRuleId || row.ruleId || row.id,
        termRuleCode: row.termRuleCode || row.ruleCode || row.code || '',
        termRuleName: row.termRuleName || row.ruleName || row.name || '',
        productType: row.productType || 'BANK_WEALTH',
        termType: row.termType || 'FIXED',
        termUnit: row.termUnit || row.unit || minTermParsed.unit || 'MONTHS',
        minTerm: minTermParsed.value,
        maxTerm: maxTermParsed.value,
        lockPeriod: lockPeriodParsed.value,
        earlyRedemptionFeeRate: row.earlyRedemptionFeeRate || row.redemptionFeeRate || 0,
        isEnabled: row.isEnabled !== undefined ? row.isEnabled : (row.enabled !== undefined ? row.enabled : 1),
        description: row.description || row.remark || ''
      }
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

            // 转换参数格式以匹配后端期望
            const params = this.transformRequestParams(this.temp)
            console.log('新增请求参数:', params)

            const { createProductTermRule } = await import('@/api/globalTreasurer/financialProductDefinition/productTermRuleManage')
            const response = await createProductTermRule(params)

            console.log('新增响应:', response)

            if (response && (response.code === 1 || response.code === 200)) {
              this.$message.success('新增成功')
              this.dialogFormVisible = false
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(response.msg || response.message || '新增失败')
            }
          } catch (error) {
            console.error('新增失败:', error)
            this.$message.error('新增失败: ' + (error.response?.data?.msg || error.message || '未知错误'))
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

            // 转换参数格式以匹配后端期望
            const params = this.transformRequestParams(this.temp)
            console.log('更新请求参数:', params)

            const { updateProductTermRule } = await import('@/api/globalTreasurer/financialProductDefinition/productTermRuleManage')
            const response = await updateProductTermRule(params)

            console.log('更新响应:', response)

            if (response && (response.code === 1 || response.code === 200)) {
              this.$message.success('更新成功')
              this.dialogFormVisible = false
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(response.msg || response.message || '更新失败')
            }
          } catch (error) {
            console.error('更新失败:', error)
            this.$message.error('更新失败: ' + (error.response?.data?.msg || error.message || '未知错误'))
          } finally {
            this.submitLoading = false
          }
        }
      })
    },

    /**
     * 删除
     */
    handleDelete(row, index) {
      const deleteId = row.termRuleId || row.ruleId || row.id
      console.log('handleDelete - row数据:', row)
      console.log('handleDelete - deleteId:', deleteId, '类型:', typeof deleteId)
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        console.log('发起删除请求，ID:', deleteId)
        deleteProductTermRule(deleteId).then(response => {
          console.log('删除响应:', response)
          if (response.code === 1) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(response.message || response.msg || '删除失败')
          }
        }).catch(error => {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        })
      })
    },

    /**
     * 期限计算
     */
    handleTermCalculate() {
      this.resetCalculateForm()
      this.calculateDialogVisible = true
    },

    /**
     * 重置期限计算表单
     */
    resetCalculateForm() {
      this.calculateForm = {
        ruleId: undefined,
        startDate: '',
        termDays: undefined,
        termMonths: undefined,
        termYears: undefined,
        calculateType: 'days'
      }
    },

    /**
     * 执行期限计算
     */
    handleCalculate() {
      if (!this.calculateForm.ruleId) {
        this.$message.warning('请选择期限规则')
        return
      }

      this.calculateLoading = true

      const params = {
        ruleId: this.calculateForm.ruleId,
        startDate: this.calculateForm.startDate,
        termDays: this.calculateForm.calculateType === 'days' ? this.calculateForm.termDays : undefined,
        termMonths: this.calculateForm.calculateType === 'months' ? this.calculateForm.termMonths : undefined,
        termYears: this.calculateForm.calculateType === 'years' ? this.calculateForm.termYears : undefined
      }

      calculateTerm(params).then(response => {
        this.calculateLoading = false
        if (response.code === 1 && response.data) {
          const result = response.data
          const h = this.$createElement

          this.$msgbox({
            title: '期限计算结果',
            message: h('div', { style: 'max-height: 400px; overflow-y: auto;' }, [
              h('p', null, `起始日期: ${result.startDate || '-'}`),
              h('p', null, `期限规则: ${result.ruleInfo?.ruleCode || '-'} - ${result.ruleInfo?.ruleName || '-'}`),
              h('p', null, `总期限: ${result.totalDays || 0}天 / ${result.totalMonths || 0}月 / ${result.totalYears || 0}年`),
              h('p', null, `到期日期: ${result.maturityDate || '-'}`),
              h('p', null, `锁定期: ${result.lockPeriod || 0}天`),
              h('p', null, `锁定到期日: ${result.lockExpiryDate || '-'}`),
              h('p', null, `剩余期限: ${result.remainingDays || 0}天 / ${result.remainingMonths || 0}月 / ${result.remainingYears || 0}年`)
            ]),
            confirmButtonText: '确定'
          })
        } else {
          this.$message.error(response.message || '计算失败')
        }
      }).catch(error => {
        this.calculateLoading = false
        console.error('期限计算失败:', error)
        this.$message.error('计算失败: ' + (error.message || '未知错误'))
      })
    },

    /**
     * 导出
     */
    handleExport() {
      try {
        const data = this.list.map(item => ({
          期限规则编码: item.termRuleCode || '',
          期限规则名称: item.termRuleName || '',
          产品类型: this.getProductTypeName(item.productType) || '',
          期限类型: this.getTermTypeName(item.termType) || '',
          最小期限: item.minTerm || 0,
          最大期限: item.maxTerm || 0,
          锁定期: item.lockPeriod || 0,
          提前赎回费率: item.earlyRedemptionFeeRate || 0,
          描述: item.description || '',
          是否启用: item.isEnabled === 1 ? '是' : '否'
        }))
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `期限规则数据_${new Date().getTime()}.json`
        link.click()
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    /**
     * 获取产品类型名称
     */
    getProductTypeName(productType) {
      const map = {
        'BANK_WEALTH': '银行理财',
        'BOND': '债券投资',
        'EQUITY': '股票投资',
        'FUND': '基金投资',
        'DEPOSIT': '存款产品'
      }
      const name = map[productType] || productType || '未知'
      console.log('getProductTypeName:', productType, '->', name)
      return name
    },

    /**
     * 获取产品类型颜色
     */
    getProductTypeColor(productType) {
      const map = {
        'BANK_WEALTH': 'primary',
        'BOND': 'success',
        'EQUITY': 'warning',
        'FUND': 'info',
        'DEPOSIT': ''
      }
      return map[productType] || ''
    },

    /**
     * 获取期限类型名称
     */
    getTermTypeName(termType) {
      const map = {
        'FIXED': '固定期限',
        'OPEN_ENDED': '开放式',
        'PERIODIC_OPEN': '定期开放',
        'ROLLING': '滚动期限'
      }
      const name = map[termType] || termType || '未知'
      console.log('getTermTypeName:', termType, '->', name)
      return name
    },

    /**
     * 获取期限类型颜色
     */
    getTermTypeColor(termType) {
      const map = {
        'FIXED': 'primary',
        'OPEN_ENDED': 'success',
        'PERIODIC_OPEN': 'warning',
        'ROLLING': 'info'
      }
      return map[termType] || ''
    },

    /**
     * 获取期限单位名称
     */
    getTermUnitName(termUnit) {
      const map = {
        'DAYS': '天',
        'MONTHS': '月',
        'YEARS': '年'
      }
      return map[termUnit] || termUnit
    },

    /**
     * 格式化创建时间 - 处理时间戳和各种时间格式
     */
    formatCreateTime(time) {
      if (!time) return ''

      // 如果是时间戳（数字）
      if (typeof time === 'number') {
        // 判断是秒还是毫秒
        const timestamp = time < 10000000000 ? time * 1000 : time
        const date = new Date(timestamp)
        return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
      }

      // 如果是字符串
      if (typeof time === 'string') {
        // 已经是标准格式，直接返回日期部分
        if (time.includes('T')) {
          return time.split('T')[0]
        }
        if (time.includes(' ')) {
          return time.split(' ')[0]
        }
        // 如果包含 - 或 /，可能是已格式化的日期
        if (time.includes('-') || time.includes('/')) {
          return time
        }
        // 尝试作为时间戳处理
        const num = parseInt(time)
        if (!isNaN(num)) {
          const timestamp = num < 10000000000 ? num * 1000 : num
          const date = new Date(timestamp)
          return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
        }
      }

      return time
    }
  }
}
</script>
