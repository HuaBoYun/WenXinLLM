<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>资金预测管理</h2>
      <p>基于历史数据和多种预测模型，为资金计划提供科学的预测支持</p>
    </div>

    <!-- 查询条件 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.forecastType"
        placeholder="预测类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in forecastTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        v-model="listQuery.forecastMethod"
        placeholder="预测方法"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in forecastMethodOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        class="filter-item"
        style="width: 240px"
        @change="handleDateRangeChange"
      />
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        搜索
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        新建预测
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="success"
        icon="el-icon-data-analysis"
        @click="showAccuracyAnalysis"
      >
        准确率分析
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="warning"
        icon="el-icon-trend-charts"
        @click="showTrendAnalysis"
      >
        趋势分析
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="danger"
        icon="el-icon-delete"
        :disabled="multipleSelection.length === 0"
        @click="handleBatchDelete"
      >
        批量删除
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.totalCount || 0 }}</div>
            <div class="statistics-label">预测总数</div>
          </div>
          <i class="el-icon-data-line statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.verifiedCount || 0 }}</div>
            <div class="statistics-label">已验证预测</div>
          </div>
          <i class="el-icon-circle-check statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.avgAccuracy || 0 }}%</div>
            <div class="statistics-label">平均准确率</div>
          </div>
          <i class="el-icon-data-analysis statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.highAccuracyCount || 0 }}</div>
            <div class="statistics-label">高准确率预测</div>
          </div>
          <i class="el-icon-trophy statistics-icon"></i>
        </el-card>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="预测编号"
        prop="forecastNo"
        width="140"
        align="center"
      >
        <template slot-scope="{row}">
          <el-link type="primary" @click="showDetail(row)">
            {{ row.forecastNo }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="预测类型"
        prop="forecastType"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="forecastTypeTagMap[row.forecastType]">
            {{ forecastTypeMap[row.forecastType] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="预测方法"
        prop="forecastMethod"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="forecastMethodTagMap[row.forecastMethod]">
            {{ forecastMethodMap[row.forecastMethod] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="预测日期"
        prop="forecastDate"
        width="120"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span>{{ row.forecastDate }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="预测金额"
        prop="forecastAmount"
        width="140"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ formatAmount(row.forecastAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="实际金额"
        prop="actualAmount"
        width="140"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.actualAmount">{{ formatAmount(row.actualAmount) }}</span>
          <span v-else class="text-muted">待验证</span>
        </template>
      </el-table-column>
      <el-table-column
        label="预测准确率"
        prop="forecastAccuracy"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.forecastAccuracy !== null" 
                :class="getAccuracyClass(row.forecastAccuracy)">
            {{ row.forecastAccuracy }}%
          </span>
          <span v-else class="text-muted">-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="置信区间"
        prop="confidenceInterval"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.confidenceInterval">{{ row.confidenceInterval }}%</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="预测周期"
        prop="forecastPeriod"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.forecastPeriod || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="数据源"
        prop="dataSource"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag size="mini" :type="dataSourceTagMap[row.dataSource]">
            {{ dataSourceMap[row.dataSource] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        prop="createTime"
        width="160"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span>{{ row.createdTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="200"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button
            v-if="!row.actualAmount"
            size="mini"
            type="success"
            @click="handleVerify(row)"
          >
            验证
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >
            删除
          </el-button>
          <el-dropdown
            trigger="click"
            @command="(command) => handleCommand(command, row)"
          >
            <el-button size="mini">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="detail">查看详情</el-dropdown-item>
              <el-dropdown-item command="copy">复制预测</el-dropdown-item>
              <el-dropdown-item command="export">导出数据</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <!-- 新增/编辑预测对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新建预测' : '编辑预测'" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-width="100px" style="width:500px;margin-left:30px;">
        <el-form-item label="预测编号" prop="forecastNo">
          <el-input v-model="temp.forecastNo" placeholder="请输入预测编号" />
        </el-form-item>
        <el-form-item label="预测类型" prop="forecastType">
          <el-select v-model="temp.forecastType" placeholder="请选择预测类型" style="width:100%">
            <el-option v-for="item in forecastTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预测方法" prop="forecastMethod">
          <el-select v-model="temp.forecastMethod" placeholder="请选择预测方法" style="width:100%">
            <el-option v-for="item in forecastMethodOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预测金额" prop="forecastAmount">
          <el-input-number v-model="temp.forecastAmount" :precision="2" style="width:100%" />
        </el-form-item>
        <el-form-item label="预测日期" prop="forecastDate">
          <el-date-picker v-model="temp.forecastDate" type="date" placeholder="选择预测日期" style="width:100%" />
        </el-form-item>
        <el-form-item label="预测周期">
          <el-input v-model="temp.forecastPeriod" placeholder="请输入预测周期，如：月度、季度、年度" />
        </el-form-item>
        <el-form-item label="数据源">
          <el-select v-model="temp.dataSource" placeholder="请选择数据源" style="width:100%">
            <el-option label="系统数据" value="SYSTEM" />
            <el-option label="手工录入" value="MANUAL" />
            <el-option label="导入数据" value="IMPORT" />
            <el-option label="接口数据" value="API" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.forecastNotes" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 预测验证对话框 -->
    <el-dialog
      title="预测验证"
      :visible.sync="verifyDialogVisible"
      width="500px"
      @close="resetVerifyForm"
    >
      <el-form
        ref="verifyForm"
        :model="verifyForm"
        :rules="verifyRules"
        label-width="100px"
      >
        <el-form-item label="预测编号">
          <el-input v-model="verifyForm.forecastNo" disabled />
        </el-form-item>
        <el-form-item label="预测金额">
          <el-input v-model="verifyForm.forecastAmount" disabled>
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="实际金额" prop="actualAmount">
          <el-input
            v-model="verifyForm.actualAmount"
            type="number"
            placeholder="请输入实际金额"
          >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="验证说明">
          <el-input
            v-model="verifyForm.verifyNotes"
            type="textarea"
            :rows="3"
            placeholder="请输入验证说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="verifyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmVerify">确定</el-button>
      </div>
    </el-dialog>

    <!-- 准确率分析弹窗 -->
    <el-dialog title="预测准确率分析" :visible.sync="accuracyDialogVisible" width="800px" @opened="renderAccuracyChart">
      <div v-loading="accuracyLoading">
        <div ref="accuracyChart" style="width:100%;height:360px;" />
        <el-table :data="accuracyList" border size="small" style="margin-top:16px;">
          <el-table-column label="预测方法" prop="forecastMethod" align="center">
            <template slot-scope="{row}">{{ forecastMethodMap[row.forecastMethod] || row.forecastMethod }}</template>
          </el-table-column>
          <el-table-column label="预测总数" prop="totalCount" align="center" />
          <el-table-column label="已验证数" prop="verifiedCount" align="center" />
          <el-table-column label="平均准确率" prop="avgAccuracy" align="center">
            <template slot-scope="{row}">
              <span :class="getAccuracyClass(row.avgAccuracy)">{{ Number(row.avgAccuracy).toFixed(1) }}%</span>
            </template>
          </el-table-column>
          <el-table-column label="高准确(≥90%)" prop="highAccuracyCount" align="center" />
          <el-table-column label="中准确(80-90%)" prop="mediumAccuracyCount" align="center" />
          <el-table-column label="低准确(<80%)" prop="lowAccuracyCount" align="center" />
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="accuracyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 趋势分析弹窗 -->
    <el-dialog title="预测趋势分析" :visible.sync="trendDialogVisible" width="860px" @opened="renderTrendChart">
      <div v-loading="trendLoading">
        <div ref="trendChart" style="width:100%;height:360px;" />
        <el-table :data="trendList" border size="small" style="margin-top:16px;">
          <el-table-column label="月份" prop="month" align="center" />
          <el-table-column label="预测笔数" prop="forecastCount" align="center" />
          <el-table-column label="预测总金额" prop="totalForecastAmount" align="center">
            <template slot-scope="{row}">{{ formatAmount(row.totalForecastAmount) }}</template>
          </el-table-column>
          <el-table-column label="实际总金额" prop="totalActualAmount" align="center">
            <template slot-scope="{row}">{{ formatAmount(row.totalActualAmount) }}</template>
          </el-table-column>
          <el-table-column label="平均准确率" prop="avgAccuracy" align="center">
            <template slot-scope="{row}">
              <span v-if="row.avgAccuracy" :class="getAccuracyClass(row.avgAccuracy)">{{ Number(row.avgAccuracy).toFixed(1) }}%</span>
              <span v-else class="text-muted">-</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="trendDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFundForecastPage, createFundForecast, updateFundForecast, deleteFundForecast,
         getFundForecastSummary, getFundForecastAccuracyAnalysis, getFundForecastTrendAnalysis } from '@/api/globalTreasurer/zjjh'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils'
import * as echarts from 'echarts'

export default {
  name: 'FundForecast',
  components: { Pagination },
  directives: { waves },
  filters: {
    parseTime
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
        forecastType: undefined,
        forecastMethod: undefined,
        startDate: undefined,
        endDate: undefined,
        sort: '-forecastDate'
      },
      dateRange: [],
      summaryInfo: {},
      accuracyDialogVisible: false,
      accuracyLoading: false,
      accuracyList: [],
      trendDialogVisible: false,
      trendLoading: false,
      trendList: [],
      verifyDialogVisible: false,
      dialogFormVisible: false,
      dialogStatus: 'create',
      multipleSelection: [],
      temp: {
        forecastId: undefined,
        forecastNo: '',
        forecastType: '',
        forecastMethod: '',
        forecastAmount: 0,
        forecastDate: null,
        forecastPeriod: '',
        dataSource: '',
        forecastNotes: ''
      },
      rules: {
        forecastNo: [{ required: true, message: '请输入预测编号', trigger: 'blur' }],
        forecastType: [{ required: true, message: '请选择预测类型', trigger: 'change' }],
        forecastMethod: [{ required: true, message: '请选择预测方法', trigger: 'change' }],
        forecastAmount: [{ required: true, message: '请输入预测金额', trigger: 'blur' }],
        forecastDate: [{ required: true, message: '请选择预测日期', trigger: 'change' }]
      },
      verifyForm: {
        forecastId: null,
        forecastNo: '',
        forecastAmount: '',
        actualAmount: '',
        verifyNotes: ''
      },
      verifyRules: {
        actualAmount: [
          { required: true, message: '请输入实际金额', trigger: 'blur' },
          { validator: (rule, value, callback) => {
            if (value === '' || value === null || value === undefined) {
              callback(new Error('请输入实际金额'))
            } else if (isNaN(Number(value)) || Number(value) < 0) {
              callback(new Error('实际金额必须为有效数字'))
            } else {
              callback()
            }
          }, trigger: 'blur' }
        ]
      },
      forecastTypeOptions: [
        { label: '收入预测', value: 'INCOME' },
        { label: '支出预测', value: 'EXPENSE' },
        { label: '现金流预测', value: 'CASHFLOW' },
        { label: '投资预测', value: 'INVESTMENT' },
        { label: '融资预测', value: 'FINANCING' }
      ],
      forecastMethodOptions: [
        { label: '历史数据法', value: 'HISTORICAL' },
        { label: '回归分析法', value: 'REGRESSION' },
        { label: '季节性分析', value: 'SEASONAL' },
        { label: '移动平均法', value: 'MOVING_AVERAGE' },
        { label: '指数平滑法', value: 'EXPONENTIAL' },
        { label: '人工预测', value: 'MANUAL' }
      ],
      forecastTypeMap: {
        'INCOME': '收入预测',
        'EXPENSE': '支出预测',
        'CASHFLOW': '现金流预测',
        'INVESTMENT': '投资预测',
        'FINANCING': '融资预测'
      },
      forecastTypeTagMap: {
        'INCOME': 'success',
        'EXPENSE': 'danger',
        'CASHFLOW': 'info',
        'INVESTMENT': 'warning',
        'FINANCING': 'primary'
      },
      forecastMethodMap: {
        'HISTORICAL': '历史数据法',
        'REGRESSION': '回归分析法',
        'SEASONAL': '季节性分析',
        'MOVING_AVERAGE': '移动平均法',
        'EXPONENTIAL': '指数平滑法',
        'MANUAL': '人工预测'
      },
      forecastMethodTagMap: {
        'HISTORICAL': 'info',
        'REGRESSION': 'success',
        'SEASONAL': 'warning',
        'MOVING_AVERAGE': 'primary',
        'EXPONENTIAL': 'danger',
        'MANUAL': ''
      },
      dataSourceMap: {
        'SYSTEM': '系统数据',
        'MANUAL': '手工录入',
        'IMPORT': '导入数据',
        'API': '接口数据'
      },
      dataSourceTagMap: {
        'SYSTEM': 'success',
        'MANUAL': 'warning',
        'IMPORT': 'info',
        'API': 'primary'
      }
    }
  },
  created() {
    this.getList()
    this.getSummaryInfo()
  },
  methods: {
    getList() {
      this.listLoading = true
      getFundForecastPage(this.listQuery).then(response => {
        if (response.code === 1) {
          this.list = response.data.records
          this.total = response.data.total
        } else {
          this.$message.error(response.msg || '查询失败')
        }
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    getSummaryInfo() {
      getFundForecastSummary(this.listQuery).then(response => {
        if (response.code === 1) {
          const d = response.data || {}
          this.summaryInfo = {
            totalCount: d.totalCount || d.TOTALCOUNT || 0,
            verifiedCount: d.verifiedCount || d.VERIFIEDCOUNT || 0,
            avgAccuracy: d.avgAccuracy !== undefined ? d.avgAccuracy : (d.AVGACCURACY !== undefined ? Number(d.AVGACCURACY).toFixed(2) : 0),
            highAccuracyCount: d.highAccuracyCount || d.HIGHACCURACYCOUNT || 0
          }
        }
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
      this.getSummaryInfo()
    },
    handleDateRangeChange(val) {
      if (val) {
        this.listQuery.startDate = val[0]
        this.listQuery.endDate = val[1]
      } else {
        this.listQuery.startDate = undefined
        this.listQuery.endDate = undefined
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'forecastDate') {
        this.sortByForecastDate(order)
      } else if (prop === 'createTime') {
        this.sortByCreateTime(order)
      }
    },
    sortByForecastDate(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+forecastDate'
      } else {
        this.listQuery.sort = '-forecastDate'
      }
      this.handleFilter()
    },
    sortByCreateTime(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+createTime'
      } else {
        this.listQuery.sort = '-createTime'
      }
      this.handleFilter()
    },
    handleCreate() {
      this.temp = {
        forecastId: undefined,
        forecastNo: '',
        forecastType: '',
        forecastMethod: '',
        forecastAmount: 0,
        forecastDate: null,
        forecastPeriod: '',
        dataSource: '',
        forecastNotes: ''
      }
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'] && this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'] && this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          createFundForecast(this.temp).then(response => {
            if (response.code === 1) {
              this.$message.success('新建成功')
              this.dialogFormVisible = false
              this.getList()
              this.getSummaryInfo()
            } else {
              this.$message.error(response.msg || '新建失败')
            }
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updateFundForecast(this.temp).then(response => {
            if (response.code === 1) {
              this.$message.success('更新成功')
              this.dialogFormVisible = false
              this.getList()
              this.getSummaryInfo()
            } else {
              this.$message.error(response.msg || '更新失败')
            }
          })
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) return
      this.$confirm(`确认删除选中的 ${this.multipleSelection.length} 条记录?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        Promise.all(this.multipleSelection.map(row => deleteFundForecast(row.forecastId))).then(() => {
          this.$message.success('批量删除成功')
          this.multipleSelection = []
          this.getList()
          this.getSummaryInfo()
        })
      })
    },
    handleVerify(row) {
      this.verifyForm = {
        forecastId: row.forecastId,
        forecastNo: row.forecastNo,
        forecastAmount: row.forecastAmount,
        actualAmount: '',
        verifyNotes: ''
      }
      this.verifyDialogVisible = true
    },
    confirmVerify() {
      this.$refs.verifyForm.validate(valid => {
        if (valid) {
          // 调用验证API
          const verifyData = {
            forecastId: this.verifyForm.forecastId,
            actualAmount: parseFloat(this.verifyForm.actualAmount),
            verifyNotes: this.verifyForm.verifyNotes
          }

          updateFundForecast(verifyData).then(response => {
            if (response.code === 1) {
              this.$message.success('验证成功')
              this.verifyDialogVisible = false
              this.getList()
              this.getSummaryInfo()
            } else {
              this.$message.error(response.msg || '验证失败')
            }
          })
        }
      })
    },
    resetVerifyForm() {
      this.verifyForm = {
        forecastId: null,
        forecastNo: '',
        forecastAmount: '',
        actualAmount: '',
        verifyNotes: ''
      }
      if (this.$refs.verifyForm) {
        this.$refs.verifyForm.resetFields()
      }
    },
    handleDelete(row, index) {
      this.$confirm('确认删除该预测记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFundForecast(row.forecastId).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.list.splice(index, 1)
            this.total--
            this.getSummaryInfo()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      })
    },
    handleCommand(command, row) {
      switch (command) {
        case 'detail':
          this.showDetail(row)
          break
        case 'copy':
          this.copyForecast(row)
          break
        case 'export':
          this.exportForecast(row)
          break
      }
    },
    showDetail(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    copyForecast(row) {
      this.temp = Object.assign({}, row)
      delete this.temp.forecastId
      this.temp.forecastNo = `${row.forecastNo}_COPY`
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    exportForecast(row) {
      const typeMap = { INCOME: '收入预测', EXPENSE: '支出预测', CASHFLOW: '现金流预测', INVESTMENT: '投资预测', FINANCING: '融资预测' }
      const headers = ['预测编号', '预测类型', '预测方法', '预测日期', '预测金额(元)', '实际金额(元)', '预测准确率(%)', '预测周期', '数据源', '说明', '创建人', '创建时间']
      const row2csv = r => [
        r.forecastNo || '',
        typeMap[r.forecastType] || r.forecastType || '',
        this.forecastMethodMap[r.forecastMethod] || r.forecastMethod || '',
        r.forecastDate ? this.$options.filters ? r.forecastDate : r.forecastDate : '',
        r.forecastAmount || '',
        r.actualAmount || '',
        r.forecastAccuracy || '',
        r.forecastPeriod || '',
        r.dataSource || '',
        r.description || '',
        r.createdByName || '',
        r.createdTime ? new Date(r.createdTime).toLocaleString() : ''
      ].map(v => `"${String(v).replace(/"/g, '""')}"`).join(',')

      const csvContent = '\uFEFF' + headers.join(',') + '\n' + row2csv(row)
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `预测数据_${row.forecastNo || row.forecastId}.csv`
      link.click()
      URL.revokeObjectURL(link.href)
    },
    showAccuracyAnalysis() {
      this.accuracyDialogVisible = true
      this.accuracyLoading = true
      getFundForecastAccuracyAnalysis(this.listQuery).then(response => {
        if (response.code === 1) {
          this.accuracyList = (response.data || []).map(item => ({
            forecastMethod: item.forecastMethod || item.FORECASTMETHOD,
            totalCount: item.totalCount || item.TOTALCOUNT,
            verifiedCount: item.verifiedCount || item.VERIFIEDCOUNT,
            avgAccuracy: item.avgAccuracy !== undefined ? item.avgAccuracy : item.AVGACCURACY,
            highAccuracyCount: item.highAccuracyCount || item.HIGHACCURACYCOUNT,
            mediumAccuracyCount: item.mediumAccuracyCount || item.MEDIUMACCURACYCOUNT,
            lowAccuracyCount: item.lowAccuracyCount || item.LOWACCURACYCOUNT
          }))
        }
        this.accuracyLoading = false
      }).catch(() => { this.accuracyLoading = false })
    },
    renderAccuracyChart() {
      this.$nextTick(() => {
        if (!this.$refs.accuracyChart) return
        const chart = echarts.init(this.$refs.accuracyChart)
        const methodMap = this.forecastMethodMap
        const list = this.accuracyList
        chart.setOption({
          tooltip: { trigger: 'axis' },
          legend: { data: ['平均准确率', '高准确数', '中准确数', '低准确数'] },
          grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
          xAxis: { type: 'category', data: list.map(i => methodMap[i.forecastMethod] || i.forecastMethod) },
          yAxis: [
            { type: 'value', name: '准确率(%)', max: 100, axisLabel: { formatter: '{value}%' } },
            { type: 'value', name: '数量' }
          ],
          series: [
            { name: '平均准确率', type: 'line', yAxisIndex: 0, data: list.map(i => Number(i.avgAccuracy).toFixed(1)), itemStyle: { color: '#409EFF' } },
            { name: '高准确数', type: 'bar', yAxisIndex: 1, stack: 'count', data: list.map(i => i.highAccuracyCount), itemStyle: { color: '#67C23A' } },
            { name: '中准确数', type: 'bar', yAxisIndex: 1, stack: 'count', data: list.map(i => i.mediumAccuracyCount), itemStyle: { color: '#E6A23C' } },
            { name: '低准确数', type: 'bar', yAxisIndex: 1, stack: 'count', data: list.map(i => i.lowAccuracyCount), itemStyle: { color: '#F56C6C' } }
          ]
        })
      })
    },
    showTrendAnalysis() {
      this.trendDialogVisible = true
      this.trendLoading = true
      getFundForecastTrendAnalysis(this.listQuery).then(response => {
        if (response.code === 1) {
          this.trendList = (response.data || []).map(item => ({
            month: item.month || item.MONTH,
            forecastCount: item.forecastCount || item.FORECASTCOUNT,
            totalForecastAmount: item.totalForecastAmount !== undefined ? item.totalForecastAmount : item.TOTALFORECASTAMOUNT,
            totalActualAmount: item.totalActualAmount !== undefined ? item.totalActualAmount : item.TOTALACTUALAMOUNT,
            avgAccuracy: item.avgAccuracy !== undefined ? item.avgAccuracy : item.AVGACCURACY
          }))
        }
        this.trendLoading = false
      }).catch(() => { this.trendLoading = false })
    },
    renderTrendChart() {
      this.$nextTick(() => {
        if (!this.$refs.trendChart) return
        const chart = echarts.init(this.$refs.trendChart)
        const list = this.trendList
        chart.setOption({
          tooltip: { trigger: 'axis' },
          legend: { data: ['预测总金额', '实际总金额', '平均准确率'] },
          grid: { left: '3%', right: '6%', bottom: '3%', containLabel: true },
          xAxis: { type: 'category', data: list.map(i => i.month) },
          yAxis: [
            { type: 'value', name: '金额(元)' },
            { type: 'value', name: '准确率(%)', max: 100, axisLabel: { formatter: '{value}%' } }
          ],
          series: [
            { name: '预测总金额', type: 'bar', yAxisIndex: 0, data: list.map(i => i.totalForecastAmount), itemStyle: { color: '#409EFF' } },
            { name: '实际总金额', type: 'bar', yAxisIndex: 0, data: list.map(i => i.totalActualAmount), itemStyle: { color: '#67C23A' } },
            { name: '平均准确率', type: 'line', yAxisIndex: 1, data: list.map(i => i.avgAccuracy ? Number(i.avgAccuracy).toFixed(1) : null), itemStyle: { color: '#E6A23C' } }
          ]
        })
      })
    },
    getAccuracyClass(accuracy) {
      if (accuracy >= 90) return 'text-success'
      if (accuracy >= 80) return 'text-warning'
      return 'text-danger'
    },
    formatAmount(amount) {
      if (amount === null || amount === undefined) return '-'
      return new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      }).format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;

  h2 {
    margin: 0 0 8px 0;
    color: #303133;
    font-size: 24px;
    font-weight: 500;
  }

  p {
    margin: 0;
    color: #909399;
    font-size: 14px;
  }
}

.filter-container {
  padding: 10px 0;
  margin-bottom: 20px;

  .filter-item {
    display: inline-block;
    vertical-align: middle;
    margin-bottom: 10px;
    margin-right: 10px;
  }
}

.statistics-row {
  margin-bottom: 20px;
}

.statistics-card {
  position: relative;
  overflow: hidden;

  .statistics-content {
    padding: 20px;

    .statistics-value {
      font-size: 28px;
      font-weight: bold;
      color: #303133;
      line-height: 1;
      margin-bottom: 8px;
    }

    .statistics-label {
      font-size: 14px;
      color: #909399;
    }
  }

  .statistics-icon {
    position: absolute;
    right: 20px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 40px;
    color: #E4E7ED;
  }
}

.text-success {
  color: #67C23A !important;
  font-weight: 500;
}

.text-warning {
  color: #E6A23C !important;
  font-weight: 500;
}

.text-danger {
  color: #F56C6C !important;
  font-weight: 500;
}

.text-muted {
  color: #C0C4CC !important;
}

::v-deep .el-table {
  .text-success {
    color: #67C23A;
    font-weight: 500;
  }

  .text-warning {
    color: #E6A23C;
    font-weight: 500;
  }

  .text-danger {
    color: #F56C6C;
    font-weight: 500;
  }

  .text-muted {
    color: #C0C4CC;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
