<template>
  <div class="app-container">
    <!-- 报表类型选择 -->
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="明细表" name="DETAIL"></el-tab-pane>
      <el-tab-pane label="汇总表" name="SUMMARY"></el-tab-pane>
      <el-tab-pane label="对比表" name="COMPARE"></el-tab-pane>
      <el-tab-pane label="趋势表" name="TREND"></el-tab-pane>
    </el-tabs>

    <!-- 查询表单 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px" class="query-form">
      <el-form-item label="预算模型" prop="modelId">
        <el-select v-model="queryParams.modelId" placeholder="请选择预算模型" clearable filterable>
          <el-option
            v-for="item in modelList"
            :key="item.modelId"
            :label="item.modelName"
            :value="item.modelId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="期间范围" prop="periodRange">
        <el-date-picker
          v-model="periodRange"
          type="monthrange"
          range-separator="至"
          start-placeholder="开始期间"
          end-placeholder="结束期间"
          value-format="yyyy-MM"
          @change="handlePeriodChange">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="预算年度" prop="budgetYear">
        <el-date-picker
          v-model="queryParams.budgetYear"
          type="year"
          placeholder="请选择年度"
          value-format="yyyy">
        </el-date-picker>
      </el-form-item>
      
      <!-- 汇总表特有参数 -->
      <el-form-item v-if="activeTab === 'SUMMARY'" label="汇总维度" prop="summaryDimension">
        <el-select v-model="queryParams.summaryDimension" placeholder="请选择汇总维度">
          <el-option label="按组织" value="ORG" />
          <el-option label="按科目" value="SUBJECT" />
          <el-option label="按期间" value="PERIOD" />
        </el-select>
      </el-form-item>

      <!-- 对比表特有参数 -->
      <el-form-item v-if="activeTab === 'COMPARE'" label="对比类型" prop="compareType">
        <el-select v-model="queryParams.compareType" placeholder="请选择对比类型">
          <el-option label="预算vs实际" value="BUDGET_VS_ACTUAL" />
          <el-option label="期间对比" value="PERIOD_VS_PERIOD" />
          <el-option label="年度对比" value="YEAR_VS_YEAR" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 - 明细表 -->
    <el-table
      v-if="activeTab === 'DETAIL'"
      v-loading="loading"
      :data="detailData"
      border
      stripe
      style="width: 100%">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="modelName" label="预算模型" min-width="150" show-overflow-tooltip />
      <el-table-column prop="orgName" label="组织" min-width="150" show-overflow-tooltip>
        <template slot-scope="scope">
          {{ scope.row.orgName || scope.row.orgCode || scope.row.bizOrgId || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="subjectCode" label="科目编码" min-width="120" show-overflow-tooltip />
      <el-table-column prop="subjectName" label="科目名称" min-width="150" show-overflow-tooltip>
        <template slot-scope="scope">
          {{ scope.row.subjectName || scope.row.subjectCode || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="period" label="期间" width="100" align="center" />
      <el-table-column prop="budgetYear" label="年度" width="80" align="center" />
      <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.budgetAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="adjustedAmount" label="调整后预算" width="120" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.adjustedAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="actualAmount" label="实际金额" width="120" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.actualAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="varianceAmount" label="差异金额" width="120" align="right">
        <template slot-scope="scope">
          <span :class="scope.row.varianceAmount < 0 ? 'text-danger' : ''">
            {{ formatMoney(scope.row.varianceAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="executionRate" label="执行率" width="100" align="right">
        <template slot-scope="scope">{{ formatPercent(scope.row.executionRate) }}</template>
      </el-table-column>
      <el-table-column prop="dataSource" label="数据来源" width="100" align="center" />
    </el-table>

    <!-- 数据表格 - 汇总表 -->
    <el-table
      v-if="activeTab === 'SUMMARY'"
      v-loading="loading"
      :data="summaryData"
      border
      stripe
      style="width: 100%">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="dimensionValue" label="维度值" min-width="150" show-overflow-tooltip>
        <template slot-scope="scope">
          {{ scope.row.dimensionName || scope.row.dimensionValue || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="totalBudgetAmount" label="预算金额合计" width="140" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.totalBudgetAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="totalAdjustedAmount" label="调整后预算合计" width="140" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.totalAdjustedAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="totalActualAmount" label="实际金额合计" width="140" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.totalActualAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="totalVarianceAmount" label="差异金额合计" width="140" align="right">
        <template slot-scope="scope">
          <span :class="scope.row.totalVarianceAmount < 0 ? 'text-danger' : ''">
            {{ formatMoney(scope.row.totalVarianceAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="executionRate" label="执行率" width="100" align="right">
        <template slot-scope="scope">
          {{ formatPercent(scope.row.executionRate) }}
        </template>
      </el-table-column>
    </el-table>

    <!-- 数据表格 - 对比表 -->
    <el-table
      v-if="activeTab === 'COMPARE'"
      v-loading="loading"
      :data="compareData"
      border
      stripe
      style="width: 100%">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="subjectCode" label="科目编码" min-width="120" show-overflow-tooltip />
      <el-table-column prop="subjectName" label="科目名称" min-width="150" show-overflow-tooltip>
        <template slot-scope="scope">
          {{ scope.row.subjectName || scope.row.subjectCode || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="period" label="期间" width="100" align="center" />
      <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.budgetAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="actualAmount" label="实际金额" width="120" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.actualAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="varianceAmount" label="差异金额" width="120" align="right">
        <template slot-scope="scope">
          <span :class="scope.row.varianceAmount < 0 ? 'text-danger' : ''">
            {{ formatMoney(scope.row.varianceAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="executionRate" label="执行率" width="100" align="right">
        <template slot-scope="scope">
          {{ formatPercent(scope.row.executionRate) }}
        </template>
      </el-table-column>
    </el-table>

    <!-- 数据表格 - 趋势表 -->
    <el-table
      v-if="activeTab === 'TREND'"
      v-loading="loading"
      :data="trendData"
      border
      stripe
      style="width: 100%">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="period" label="期间" width="100" align="center" />
      <el-table-column prop="budgetAmount" label="预算金额" width="140" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.budgetAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="adjustedAmount" label="调整后预算" width="140" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.adjustedAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="actualAmount" label="实际金额" width="140" align="right">
        <template slot-scope="scope">
          {{ formatMoney(scope.row.actualAmount) }}
        </template>
      </el-table-column>
      <el-table-column prop="varianceAmount" label="差异金额" width="140" align="right">
        <template slot-scope="scope">
          <span :class="scope.row.varianceAmount < 0 ? 'text-danger' : ''">
            {{ formatMoney(scope.row.varianceAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="executionRate" label="执行率" width="100" align="right">
        <template slot-scope="scope">
          {{ formatPercent(scope.row.executionRate) }}
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="activeTab === 'DETAIL' && total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="handleQuery"
    />
  </div>
</template>

<script>
import { queryDetailData, querySummaryData, queryCompareData, queryTrendData, exportData } from '@/api/financialSharing/budgetReport'
import { getBudgetModelList } from '@/api/financialSharing/budgetModel'
import Pagination from '@/components/Pagination'

/**
 * 达梦在 ResultType=Map 下会把所有 column key 压扁成"大写无下划线",
 * 即使 SQL 里写了 as "budgetAmount" 双引号 alias 也照样压扁.
 * 这里维护一份字段名映射, 让前端 normalizeRow 能把这种格式转回标准小驼峰.
 */
const FIELD_ALIAS_MAP = {
  // 明细字段
  EXECUTIONID: 'executionId',
  MODELID: 'modelId',
  MODELNAME: 'modelName',
  ORGID: 'orgId',
  BIZORGID: 'bizOrgId',
  ORGCODE: 'orgCode',
  ORGNAME: 'orgName',
  SUBJECTCODE: 'subjectCode',
  SUBJECTNAME: 'subjectName',
  PERIOD: 'period',
  BUDGETYEAR: 'budgetYear',
  VERSIONNO: 'versionNo',
  DIMENSION1CODE: 'dimension1Code',
  DIMENSION1NAME: 'dimension1Name',
  DIMENSION2CODE: 'dimension2Code',
  DIMENSION2NAME: 'dimension2Name',
  BUDGETAMOUNT: 'budgetAmount',
  ADJUSTEDAMOUNT: 'adjustedAmount',
  ACTUALAMOUNT: 'actualAmount',
  COMMITTEDAMOUNT: 'committedAmount',
  OCCUPIEDAMOUNT: 'occupiedAmount',
  AVAILABLEAMOUNT: 'availableAmount',
  VARIANCEAMOUNT: 'varianceAmount',
  VARIANCERATE: 'varianceRate',
  EXECUTIONRATE: 'executionRate',
  STATUS: 'status',
  WARNINGLEVEL: 'warningLevel',
  DATASOURCE: 'dataSource',
  // 汇总字段
  DIMENSIONVALUE: 'dimensionValue',
  DIMENSIONNAME: 'dimensionName',
  TOTALBUDGETAMOUNT: 'totalBudgetAmount',
  TOTALADJUSTEDAMOUNT: 'totalAdjustedAmount',
  TOTALACTUALAMOUNT: 'totalActualAmount',
  TOTALVARIANCEAMOUNT: 'totalVarianceAmount',
  // PageHelper 分页副产品
  ROW_ID: 'rowId'
}

export default {
  name: 'BudgetReportIndex',
  components: {
    Pagination
  },
  data() {
    return {
      // 当前激活的标签页
      activeTab: 'DETAIL',
      // 加载状态
      loading: false,
      // 明细数据
      detailData: [],
      // 汇总数据
      summaryData: [],
      // 对比数据
      compareData: [],
      // 趋势数据
      trendData: [],
      // 总记录数
      total: 0,
      // 期间范围
      periodRange: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        reportType: 'DETAIL',
        modelId: null,
        startPeriod: null,
        endPeriod: null,
        budgetYear: null,
        summaryDimension: 'ORG',
        compareType: 'BUDGET_VS_ACTUAL'
      },
      // 预算模型列表
      modelList: []
    }
  },
  created() {
    this.getModelList()
    this.handleQuery()
  },
  methods: {
    /**
     * 把后端返回的一行数据归一成 lowerCamelCase
     *
     * 【背景】达梦数据库在 MyBatis ResultType=java.util.Map 时, 会忽略 SQL 里的双引号 alias,
     * 把所有 key 强制压扁成"全大写、去掉下划线"形式: BUDGET_AMOUNT/budgetAmount 都会变成 BUDGETAMOUNT.
     * 这种 key 没有单词边界, 通用 _ 转驼峰规则无能为力, 必须用字段映射表硬转.
     *
     * 优先级: 1) 字段映射表 → 2) 下划线转驼峰 → 3) 直接 toLowerCase 兜底
     */
    normalizeRow(row) {
      if (!row || typeof row !== 'object') return row
      const out = {}
      Object.keys(row).forEach(k => {
        let camel
        if (FIELD_ALIAS_MAP[k]) {
          camel = FIELD_ALIAS_MAP[k]
        } else if (k.indexOf('_') >= 0) {
          camel = k.toLowerCase().replace(/_([a-z0-9])/g, (_, c) => c.toUpperCase())
        } else if (k === k.toUpperCase()) {
          // 大写无下划线 + 不在映射表中: 直接小写兜底, 至少能取出值
          camel = k.toLowerCase()
        } else {
          camel = k
        }
        if (out[camel] == null || out[camel] === '') {
          out[camel] = row[k]
        }
      })
      return out
    },
    normalizeList(list) {
      if (!Array.isArray(list)) return []
      return list.map(r => this.normalizeRow(r))
    },
    /** 查询预算模型列表 (getBudgetModelList = getModelListNoPage, 直接返回数组) */
    getModelList() {
      getBudgetModelList({ status: 'ACTIVE' }).then(response => {
        if (response.code === 1) {
          // 兼容两种响应: data 是数组 或 data.list 是数组
          const data = response.data
          if (Array.isArray(data)) {
            this.modelList = data
          } else if (data && Array.isArray(data.list)) {
            this.modelList = data.list
          } else {
            this.modelList = []
          }
        }
      }).catch(err => {
        console.error('加载预算模型列表失败:', err)
      })
    },
    /** 标签页切换 */
    handleTabClick(tab) {
      this.queryParams.reportType = tab.name
      this.handleQuery()
    },
    /** 期间范围变化 */
    handlePeriodChange(value) {
      if (value && value.length === 2) {
        this.queryParams.startPeriod = value[0]
        this.queryParams.endPeriod = value[1]
      } else {
        this.queryParams.startPeriod = null
        this.queryParams.endPeriod = null
      }
    },
    /** 查询按钮操作 */
    handleQuery() {
      this.loading = true

      switch (this.activeTab) {
        case 'DETAIL':
          this.queryDetail()
          break
        case 'SUMMARY':
          this.querySummary()
          break
        case 'COMPARE':
          this.queryCompare()
          break
        case 'TREND':
          this.queryTrend()
          break
        default:
          this.loading = false
      }
    },
    /** 查询明细数据 (PageInfo 结构: {list, total}) */
    queryDetail() {
      queryDetailData(this.queryParams).then(response => {
        console.log('[BudgetReport] queryDetailData 返回:', response)
        if (response.code === 1 && response.data) {
          const list = response.data.list || []
          if (list.length > 0) {
            console.log('[BudgetReport] DETAIL 第一行原始字段:', Object.keys(list[0]), list[0])
          }
          this.detailData = this.normalizeList(list)
          this.total = response.data.total || 0
        } else {
          this.detailData = []
          this.total = 0
          if (response.code !== 1) this.$message.error(response.msg || '查询失败')
        }
      }).catch(err => {
        console.error('查询明细数据失败:', err)
        this.$message.error('查询明细数据失败')
      }).finally(() => {
        this.loading = false
      })
    },
    /** 查询汇总数据 (data 直接是数组) */
    querySummary() {
      querySummaryData(this.queryParams).then(response => {
        console.log('[BudgetReport] querySummaryData 返回:', response)
        if (response.code === 1) {
          const list = Array.isArray(response.data) ? response.data : []
          if (list.length > 0) {
            console.log('[BudgetReport] SUMMARY 第一行原始字段:', Object.keys(list[0]), list[0])
          }
          this.summaryData = this.normalizeList(list)
        } else {
          this.summaryData = []
          this.$message.error(response.msg || '查询失败')
        }
      }).catch(err => {
        console.error('查询汇总数据失败:', err)
        this.$message.error('查询汇总数据失败')
      }).finally(() => {
        this.loading = false
      })
    },
    /** 查询对比数据 (data 直接是数组) */
    queryCompare() {
      queryCompareData(this.queryParams).then(response => {
        console.log('[BudgetReport] queryCompareData 返回:', response)
        if (response.code === 1) {
          const list = Array.isArray(response.data) ? response.data : []
          if (list.length > 0) {
            console.log('[BudgetReport] COMPARE 第一行原始字段:', Object.keys(list[0]), list[0])
          }
          this.compareData = this.normalizeList(list)
        } else {
          this.compareData = []
          this.$message.error(response.msg || '查询失败')
        }
      }).catch(err => {
        console.error('查询对比数据失败:', err)
        this.$message.error('查询对比数据失败')
      }).finally(() => {
        this.loading = false
      })
    },
    /** 查询趋势数据 (data 直接是数组) */
    queryTrend() {
      queryTrendData(this.queryParams).then(response => {
        console.log('[BudgetReport] queryTrendData 返回:', response)
        if (response.code === 1) {
          const list = Array.isArray(response.data) ? response.data : []
          if (list.length > 0) {
            console.log('[BudgetReport] TREND 第一行原始字段:', Object.keys(list[0]), list[0])
          }
          this.trendData = this.normalizeList(list)
        } else {
          this.trendData = []
          this.$message.error(response.msg || '查询失败')
        }
      }).catch(err => {
        console.error('查询趋势数据失败:', err)
        this.$message.error('查询趋势数据失败')
      }).finally(() => {
        this.loading = false
      })
    },
    /** 重置按钮操作 */
    resetQuery() {
      if (this.$refs.queryForm) {
        this.$refs.queryForm.resetFields()
      }
      this.periodRange = null
      this.queryParams.modelId = null
      this.queryParams.startPeriod = null
      this.queryParams.endPeriod = null
      this.queryParams.budgetYear = null
      this.queryParams.summaryDimension = 'ORG'
      this.queryParams.compareType = 'BUDGET_VS_ACTUAL'
      this.queryParams.pageNum = 1
      this.handleQuery()
    },
    /** 导出按钮操作 (用 in-page loading, 不再用 this.$loading 全屏黑) */
    handleExport() {
      this.$confirm('确认导出当前查询结果吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        exportData(this.queryParams).then(response => {
          if (response.code === 1) {
            this.$message.success('导出成功')
            // TODO: 后续接入真实文件下载流, 这里先把数据打到控制台便于调试
            console.log('[BudgetReport] 导出数据:', response.data)
          } else {
            this.$message.error(response.msg || '导出失败')
          }
        }).catch(err => {
          console.error('导出失败:', err)
          this.$message.error('导出失败')
        }).finally(() => {
          this.loading = false
        })
      }).catch(() => {})
    },
    /** 格式化金额 */
    formatMoney(value) {
      if (value == null) return '0.00'
      return parseFloat(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    /** 格式化百分比 */
    formatPercent(value) {
      if (value == null) return '0.00%'
      return parseFloat(value).toFixed(2) + '%'
    }
  }
}
</script>

<style scoped>
.query-form {
  background: #fff;
  padding: 20px;
  margin-bottom: 10px;
}

.text-danger {
  color: #F56C6C;
}
</style>

