<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.businessType"
        placeholder="业务类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in businessTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        v-model="listQuery.executionStatus"
        placeholder="执行状态"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in executionStatusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>

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
        v-waves
        class="filter-item"
        type="success"
        icon="el-icon-pie-chart"
        @click="showAnalysis"
      >
        分析报告
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.totalCount || 0 }}</div>
            <div class="statistics-label">明细总数</div>
          </div>
          <i class="el-icon-document statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ formatAmount(summaryInfo.totalPlannedAmount) }}</div>
            <div class="statistics-label">计划金额</div>
          </div>
          <i class="el-icon-money statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ formatAmount(summaryInfo.totalActualAmount) }}</div>
            <div class="statistics-label">实际金额</div>
          </div>
          <i class="el-icon-coin statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.avgExecutionRate || 0 }}%</div>
            <div class="statistics-label">平均执行率</div>
          </div>
          <i class="el-icon-data-analysis statistics-icon"></i>
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
    >
      <el-table-column
        label="业务类型"
        prop="businessType"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="businessTypeTagMap[row.businessType]">
            {{ businessTypeMap[row.businessType] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="业务项目"
        prop="businessItem"
        width="200"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.businessItem }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="计划日期"
        prop="plannedDate"
        width="120"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span>{{ formatDate(row.plannedDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="计划金额"
        prop="plannedAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ formatAmount(row.plannedAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="实际日期"
        prop="actualDate"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ formatDate(row.actualDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="实际金额"
        prop="actualAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.actualAmount ? formatAmount(row.actualAmount) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="差异金额"
        prop="varianceAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.varianceAmount !== null" 
                :class="row.varianceAmount >= 0 ? 'text-success' : 'text-danger'">
            {{ formatAmount(row.varianceAmount) }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="差异率"
        prop="varianceRate"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.varianceRate !== null" 
                :class="Math.abs(row.varianceRate) > 10 ? 'text-danger' : 'text-success'">
            {{ row.varianceRate }}%
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="执行状态"
        prop="executionStatus"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="executionStatusTagMap[row.executionStatus]">
            {{ executionStatusMap[row.executionStatus] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="执行率"
        prop="executionRate"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.executionRate !== null">{{ row.executionRate }}%</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="币种"
        prop="currencyCode"
        width="80"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.currencyCode || 'CNY' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="200"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{row,$index}">

          <el-button
            v-if="row.executionStatus === 'PENDING'"
            size="mini"
            type="success"
            @click="handleExecute(row)"
          >
            执行
          </el-button>
          <el-button
            v-if="['PENDING', 'PROCESSING'].includes(row.executionStatus)"
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
              <el-dropdown-item command="copy">复制明细</el-dropdown-item>
              <el-dropdown-item
                v-if="row.executionStatus === 'EXECUTED'"
                command="adjust"
              >
                调整明细
              </el-dropdown-item>
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

    <!-- 添加/编辑明细对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="700px" append-to-body>
      <el-form ref="detailForm" :model="detailForm" :rules="detailRules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="业务类型" prop="businessType">
              <el-select v-model="detailForm.businessType" placeholder="请选择业务类型" style="width: 100%">
                <el-option v-for="item in businessTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务项目" prop="businessItem">
              <el-input v-model="detailForm.businessItem" placeholder="请输入业务项目名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划日期" prop="plannedDate">
              <el-date-picker v-model="detailForm.plannedDate" type="date" placeholder="选择计划日期" value-format="yyyy-MM-dd" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划金额" prop="plannedAmount">
              <el-input-number v-model="detailForm.plannedAmount" :min="0" :precision="2" :step="10000" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="币种">
              <el-select v-model="detailForm.currencyCode" placeholder="请选择币种" style="width: 100%">
                <el-option label="人民币 (CNY)" value="CNY" />
                <el-option label="美元 (USD)" value="USD" />
                <el-option label="欧元 (EUR)" value="EUR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="detailForm.description" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitDetailForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="明细详情" :visible.sync="detailVisible" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="业务类型">
          <el-tag :type="businessTypeTagMap[viewDetail.businessType]">{{ businessTypeMap[viewDetail.businessType] }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="业务项目">{{ viewDetail.businessItem }}</el-descriptions-item>
        <el-descriptions-item label="计划日期">{{ formatDate(viewDetail.plannedDate) }}</el-descriptions-item>
        <el-descriptions-item label="计划金额">{{ formatAmount(viewDetail.plannedAmount) }}</el-descriptions-item>
        <el-descriptions-item label="实际日期">{{ formatDate(viewDetail.actualDate) }}</el-descriptions-item>
        <el-descriptions-item label="实际金额">{{ viewDetail.actualAmount ? formatAmount(viewDetail.actualAmount) : '-' }}</el-descriptions-item>
        <el-descriptions-item label="差异金额">
          <span v-if="viewDetail.varianceAmount !== null" :class="viewDetail.varianceAmount >= 0 ? 'text-success' : 'text-danger'">{{ formatAmount(viewDetail.varianceAmount) }}</span>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="差异率">
          <span v-if="viewDetail.varianceRate !== null">{{ viewDetail.varianceRate }}%</span>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="执行状态">
          <el-tag :type="executionStatusTagMap[viewDetail.executionStatus]">{{ executionStatusMap[viewDetail.executionStatus] }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="执行率">
          <span v-if="viewDetail.executionRate !== null">{{ viewDetail.executionRate }}%</span>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="币种">{{ viewDetail.currencyCode || 'CNY' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ viewDetail.description || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 执行明细对话框 -->
    <el-dialog title="执行明细" :visible.sync="executeVisible" width="500px" append-to-body>
      <el-form ref="executeForm" :model="executeForm" :rules="executeRules" label-width="100px">
        <el-form-item label="业务项目">
          <span>{{ executeForm.businessItem }}</span>
        </el-form-item>
        <el-form-item label="计划金额">
          <span>{{ formatAmount(executeForm.plannedAmount) }}</span>
        </el-form-item>
        <el-form-item label="实际日期" prop="actualDate">
          <el-date-picker v-model="executeForm.actualDate" type="date" placeholder="选择实际日期" value-format="yyyy-MM-dd" style="width: 100%" />
        </el-form-item>
        <el-form-item label="实际金额" prop="actualAmount">
          <el-input-number v-model="executeForm.actualAmount" :min="0" :precision="2" :step="10000" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="executeVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitExecute">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 调整明细对话框 -->
    <el-dialog title="调整明细" :visible.sync="adjustVisible" width="500px" append-to-body>
      <el-form ref="adjustForm" :model="adjustForm" :rules="adjustRules" label-width="100px">
        <el-form-item label="业务项目">
          <span>{{ adjustForm.businessItem }}</span>
        </el-form-item>
        <el-form-item label="原计划金额">
          <span>{{ formatAmount(adjustForm.originalAmount) }}</span>
        </el-form-item>
        <el-form-item label="调整后金额" prop="plannedAmount">
          <el-input-number v-model="adjustForm.plannedAmount" :min="0" :precision="2" :step="10000" style="width: 100%" />
        </el-form-item>
        <el-form-item label="调整后日期" prop="plannedDate">
          <el-date-picker v-model="adjustForm.plannedDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%" />
        </el-form-item>
        <el-form-item label="调整原因" prop="remark">
          <el-input v-model="adjustForm.remark" type="textarea" :rows="3" placeholder="请输入调整原因" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="adjustVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitAdjust">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 分析报告对话框 -->
    <el-dialog title="分析报告" :visible.sync="analysisVisible" width="800px" append-to-body>
      <el-tabs v-model="analysisTab">
        <el-tab-pane label="差异分析" name="variance">
          <el-table :data="varianceData" border style="width: 100%">
            <el-table-column prop="businessItem" label="业务项目" />
            <el-table-column prop="businessType" label="业务类型">
              <template slot-scope="{row}">{{ businessTypeMap[row.businessType] }}</template>
            </el-table-column>
            <el-table-column prop="plannedAmount" label="计划金额">
              <template slot-scope="{row}">{{ formatAmount(row.plannedAmount) }}</template>
            </el-table-column>
            <el-table-column prop="actualAmount" label="实际金额">
              <template slot-scope="{row}">{{ formatAmount(row.actualAmount) }}</template>
            </el-table-column>
            <el-table-column prop="varianceAmount" label="差异金额">
              <template slot-scope="{row}">
                <span :class="row.varianceAmount >= 0 ? 'text-success' : 'text-danger'">{{ formatAmount(row.varianceAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="varianceRate" label="差异率">
              <template slot-scope="{row}">{{ row.varianceRate }}%</template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="执行分析" name="execution">
          <el-row :gutter="20" v-if="executionData.statusStats">
            <el-col :span="6" v-for="(count, status) in executionData.statusStats" :key="status">
              <el-card shadow="hover">
                <div style="text-align: center;">
                  <div style="font-size: 24px; font-weight: bold;">{{ count }}</div>
                  <div style="color: #909399; margin-top: 5px;">{{ executionStatusMap[status] }}</div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <el-table v-if="executionData.typeStats" :data="typeStatsArray" border style="width: 100%; margin-top: 20px;">
            <el-table-column prop="type" label="业务类型">
              <template slot-scope="{row}">{{ businessTypeMap[row.type] }}</template>
            </el-table-column>
            <el-table-column prop="count" label="明细数" />
            <el-table-column prop="plannedAmount" label="计划金额">
              <template slot-scope="{row}">{{ formatAmount(row.plannedAmount) }}</template>
            </el-table-column>
            <el-table-column prop="actualAmount" label="实际金额">
              <template slot-scope="{row}">{{ formatAmount(row.actualAmount) }}</template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import { getFundPlanDetailPage, createFundPlanDetail, updateFundPlanDetail, deleteFundPlanDetail,
         executeFundPlanDetail, adjustFundPlanDetail,
         getFundPlanDetailSummary, getFundPlanDetailVarianceAnalysis, getFundPlanDetailExecutionAnalysis } from '@/api/globalTreasurer/zjjh'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'

export default {
  name: 'FundPlanDetail',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      submitLoading: false,
      listQuery: {
        page: 1,
        limit: 20,
        planId: null,
        businessType: undefined,
        executionStatus: undefined,
        startDate: undefined,
        endDate: undefined,
        sort: '+plannedDate'
      },
      dateRange: [],
      summaryInfo: {},
      // 添加/编辑弹窗
      dialogFormVisible: false,
      dialogTitle: '',
      dialogStatus: '', // create / update
      detailForm: {
        detailId: null,
        planId: null,
        businessType: '',
        businessItem: '',
        plannedDate: '',
        plannedAmount: 0,
        currencyCode: 'CNY',
        description: ''
      },
      detailRules: {
        businessType: [{ required: true, message: '请选择业务类型', trigger: 'change' }],
        businessItem: [{ required: true, message: '请输入业务项目', trigger: 'blur' }],
        plannedDate: [{ required: true, message: '请选择计划日期', trigger: 'change' }],
        plannedAmount: [{ required: true, message: '请输入计划金额', trigger: 'blur' }]
      },
      // 查看详情
      detailVisible: false,
      viewDetail: {},
      // 执行弹窗
      executeVisible: false,
      executeForm: {
        detailId: null,
        businessItem: '',
        plannedAmount: 0,
        actualDate: '',
        actualAmount: 0
      },
      executeRules: {
        actualDate: [{ required: true, message: '请选择实际日期', trigger: 'change' }],
        actualAmount: [{ required: true, message: '请输入实际金额', trigger: 'blur' }]
      },
      // 调整弹窗
      adjustVisible: false,
      adjustForm: {
        detailId: null,
        businessItem: '',
        originalAmount: 0,
        plannedAmount: 0,
        plannedDate: '',
        remark: ''
      },
      adjustRules: {
        plannedAmount: [{ required: true, message: '请输入调整后金额', trigger: 'blur' }],
        plannedDate: [{ required: true, message: '请选择调整后日期', trigger: 'change' }],
        remark: [{ required: true, message: '请输入调整原因', trigger: 'blur' }]
      },
      // 分析报告
      analysisVisible: false,
      analysisTab: 'variance',
      varianceData: [],
      executionData: {},
      businessTypeOptions: [
        { label: '收入', value: 'INCOME' },
        { label: '支出', value: 'EXPENSE' },
        { label: '投资', value: 'INVESTMENT' },
        { label: '融资', value: 'FINANCING' },
        { label: '其他', value: 'OTHER' }
      ],
      executionStatusOptions: [
        { label: '待执行', value: 'PENDING' },
        { label: '执行中', value: 'PROCESSING' },
        { label: '已执行', value: 'EXECUTED' },
        { label: '已取消', value: 'CANCELLED' }
      ],
      businessTypeMap: {
        'INCOME': '收入',
        'EXPENSE': '支出',
        'INVESTMENT': '投资',
        'FINANCING': '融资',
        'OTHER': '其他'
      },
      businessTypeTagMap: {
        'INCOME': 'success',
        'EXPENSE': 'danger',
        'INVESTMENT': 'warning',
        'FINANCING': 'info',
        'OTHER': ''
      },
      executionStatusMap: {
        'PENDING': '待执行',
        'PROCESSING': '执行中',
        'EXECUTED': '已执行',
        'CANCELLED': '已取消'
      },
      executionStatusTagMap: {
        'PENDING': 'warning',
        'PROCESSING': 'info',
        'EXECUTED': 'success',
        'CANCELLED': 'danger'
      }
    }
  },
  computed: {
    typeStatsArray() {
      if (!this.executionData.typeStats) return []
      return Object.keys(this.executionData.typeStats).map(key => ({
        type: key,
        ...this.executionData.typeStats[key]
      }))
    }
  },
  created() {
    this.listQuery.planId = this.$route.query.planId || this.$route.params.planId
    this.getList()
    this.getSummaryInfo()
  },
  methods: {
    getList() {
      this.listLoading = true
      getFundPlanDetailPage(this.listQuery).then(response => {
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
      getFundPlanDetailSummary({ planId: this.listQuery.planId }).then(response => {
        if (response.code === 1) {
          const d = response.data || {}
          this.summaryInfo = {
            totalCount: d.TOTALCOUNT || d.totalCount || 0,
            totalPlannedAmount: d.TOTALPLANNEDAMOUNT || d.totalPlannedAmount || 0,
            totalActualAmount: d.TOTALACTUALAMOUNT || d.totalActualAmount || 0,
            avgExecutionRate: d.AVGEXECUTIONRATE != null ? Number(d.AVGEXECUTIONRATE).toFixed(2) : (d.avgExecutionRate || 0)
          }
        }
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
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
      if (prop === 'plannedDate') {
        this.sortByPlannedDate(order)
      }
    },
    sortByPlannedDate(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+plannedDate'
      } else {
        this.listQuery.sort = '-plannedDate'
      }
      this.handleFilter()
    },
    // ---- 添加/编辑明细 ----
    resetDetailForm() {
      this.detailForm = {
        detailId: null,
        planId: this.listQuery.planId,
        businessType: '',
        businessItem: '',
        plannedDate: '',
        plannedAmount: 0,
        currencyCode: 'CNY',
        description: ''
      }
    },
    handleCreate() {
      this.resetDetailForm()
      this.dialogStatus = 'create'
      this.dialogTitle = '添加明细'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.detailForm && this.$refs.detailForm.clearValidate()
      })
    },
    handleUpdate(row) {
      this.detailForm = {
        detailId: row.detailId,
        planId: row.planId,
        businessType: row.businessType,
        businessItem: row.businessItem,
        plannedDate: row.plannedDate,
        plannedAmount: row.plannedAmount,
        currencyCode: row.currencyCode || 'CNY',
        description: row.description || ''
      }
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑明细'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.detailForm && this.$refs.detailForm.clearValidate()
      })
    },
    submitDetailForm() {
      this.$refs.detailForm.validate(valid => {
        if (!valid) return
        // 确保 planId 存在
        if (!this.detailForm.planId) {
          this.detailForm.planId = this.listQuery.planId
        }
        if (!this.detailForm.planId) {
          this.$message.error('缺少计划ID，请从资金计划列表进入')
          return
        }
        this.submitLoading = true
        const api = this.dialogStatus === 'create' ? createFundPlanDetail : updateFundPlanDetail
        api(this.detailForm).then(response => {
          if (response.code === 1) {
            this.$message.success(this.dialogStatus === 'create' ? '添加成功' : '更新成功')
            this.dialogFormVisible = false
            this.getList()
            this.getSummaryInfo()
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        }).finally(() => {
          this.submitLoading = false
        })
      })
    },
    // ---- 执行明细 ----
    handleExecute(row) {
      this.executeForm = {
        detailId: row.detailId,
        businessItem: row.businessItem,
        plannedAmount: row.plannedAmount,
        actualDate: '',
        actualAmount: row.plannedAmount
      }
      this.executeVisible = true
      this.$nextTick(() => {
        this.$refs.executeForm && this.$refs.executeForm.clearValidate()
      })
    },
    submitExecute() {
      this.$refs.executeForm.validate(valid => {
        if (!valid) return
        this.submitLoading = true
        executeFundPlanDetail(this.executeForm.detailId, {
          actualDate: this.executeForm.actualDate,
          actualAmount: this.executeForm.actualAmount
        }).then(response => {
          if (response.code === 1) {
            this.$message.success('执行成功')
            this.executeVisible = false
            this.getList()
            this.getSummaryInfo()
          } else {
            this.$message.error(response.msg || '执行失败')
          }
        }).finally(() => {
          this.submitLoading = false
        })
      })
    },
    // ---- 删除 ----
    handleDelete(row, index) {
      this.$confirm('确认删除该明细?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFundPlanDetail(row.detailId).then(response => {
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
    // ---- 更多操作 ----
    handleCommand(command, row) {
      switch (command) {
        case 'detail':
          this.showDetail(row)
          break
        case 'copy':
          this.copyDetail(row)
          break
        case 'adjust':
          this.adjustDetail(row)
          break
      }
    },
    showDetail(row) {
      this.viewDetail = { ...row }
      this.detailVisible = true
    },
    copyDetail(row) {
      const copyData = { ...row }
      delete copyData.detailId
      copyData.planId = copyData.planId || this.listQuery.planId
      if (!copyData.planId) {
        this.$message.error('缺少计划ID，无法复制')
        return
      }
      copyData.businessItem = `${row.businessItem}(复制)`
      copyData.executionStatus = 'PENDING'
      copyData.actualDate = null
      copyData.actualAmount = null
      copyData.varianceAmount = null
      copyData.varianceRate = null
      copyData.executionRate = null
      this.submitLoading = true
      createFundPlanDetail(copyData).then(response => {
        if (response.code === 1) {
          this.$message.success('复制成功')
          this.getList()
          this.getSummaryInfo()
        } else {
          this.$message.error(response.msg || '复制失败')
        }
      }).finally(() => {
        this.submitLoading = false
      })
    },
    // ---- 调整明细 ----
    adjustDetail(row) {
      this.adjustForm = {
        detailId: row.detailId,
        businessItem: row.businessItem,
        originalAmount: row.plannedAmount,
        plannedAmount: row.plannedAmount,
        plannedDate: row.plannedDate,
        remark: ''
      }
      this.adjustVisible = true
      this.$nextTick(() => {
        this.$refs.adjustForm && this.$refs.adjustForm.clearValidate()
      })
    },
    submitAdjust() {
      this.$refs.adjustForm.validate(valid => {
        if (!valid) return
        this.submitLoading = true
        adjustFundPlanDetail(this.adjustForm.detailId, {
          plannedAmount: this.adjustForm.plannedAmount,
          plannedDate: this.adjustForm.plannedDate,
          remark: this.adjustForm.remark
        }).then(response => {
          if (response.code === 1) {
            this.$message.success('调整成功')
            this.adjustVisible = false
            this.getList()
            this.getSummaryInfo()
          } else {
            this.$message.error(response.msg || '调整失败')
          }
        }).finally(() => {
          this.submitLoading = false
        })
      })
    },
    // ---- 分析报告 ----
    showAnalysis() {
      this.analysisVisible = true
      this.analysisTab = 'variance'
      getFundPlanDetailVarianceAnalysis({ planId: this.listQuery.planId }).then(response => {
        if (response.code === 1) {
          this.varianceData = response.data || []
        }
      })
      getFundPlanDetailExecutionAnalysis({ planId: this.listQuery.planId }).then(response => {
        if (response.code === 1) {
          this.executionData = response.data || {}
        }
      })
    },
    formatDate(val) {
      if (!val) return '-'
      const d = new Date(val)
      if (isNaN(d.getTime())) return val
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${y}-${m}-${day}`
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

.plan-info-card {
  margin-bottom: 20px;

  .info-item {
    display: flex;
    align-items: center;
    margin-bottom: 8px;

    .label {
      font-weight: 500;
      color: #606266;
      min-width: 80px;
    }

    .value {
      color: #303133;

      &.text-success {
        color: #67C23A;
        font-weight: 500;
      }

      &.text-danger {
        color: #F56C6C;
        font-weight: 500;
      }
    }
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
}

.text-danger {
  color: #F56C6C !important;
}

.text-warning {
  color: #E6A23C !important;
}

.text-info {
  color: #909399 !important;
}

::v-deep .el-table {
  .text-success {
    color: #67C23A;
  }

  .text-danger {
    color: #F56C6C;
  }

  .text-warning {
    color: #E6A23C;
  }
}
</style>
