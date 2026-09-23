<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>资金计划调整管理</h2>
      <p>管理资金计划的调整申请，包括审批流程、影响分析和风险评估</p>
    </div>

    <!-- 查询条件 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.adjustmentType"
        placeholder="调整类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in adjustmentTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        v-model="listQuery.approvalStatus"
        placeholder="审批状态"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in approvalStatusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-date-picker
        v-model="listQuery.startDate"
        type="date"
        placeholder="开始日期"
        class="filter-item"
        style="width: 140px"
        value-format="yyyy-MM-dd"
        @change="handleFilter"
      />
      <el-date-picker
        v-model="listQuery.endDate"
        type="date"
        placeholder="结束日期"
        class="filter-item"
        style="width: 140px"
        value-format="yyyy-MM-dd"
        @change="handleFilter"
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
        新建调整
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="success"
        icon="el-icon-data-analysis"
        @click="showImpactAnalysis"
      >
        影响分析
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="warning"
        icon="el-icon-document-checked"
        @click="showPendingApproval"
      >
        待审批
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
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.totalCount || 0 }}</div>
            <div class="statistics-label">调整总数</div>
          </div>
          <i class="el-icon-edit-outline statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.pendingCount || 0 }}</div>
            <div class="statistics-label">待审批</div>
          </div>
          <i class="el-icon-time statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.approvedCount || 0 }}</div>
            <div class="statistics-label">已审批</div>
          </div>
          <i class="el-icon-circle-check statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ formatAmount(summaryInfo.totalAdjustmentAmount) }}</div>
            <div class="statistics-label">调整总金额</div>
          </div>
          <i class="el-icon-money statistics-icon"></i>
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
        label="调整编号"
        prop="adjustmentNo"
        width="140"
        align="center"
      >
        <template slot-scope="{row}">
          <el-link type="primary" @click="showDetail(row)">
            {{ row.adjustmentNo }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="调整类型"
        prop="adjustmentType"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="adjustmentTypeTagMap[row.adjustmentType]">
            {{ adjustmentTypeMap[row.adjustmentType] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="原始金额"
        prop="originalAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ formatAmount(row.originalAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="调整后金额"
        prop="adjustedAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ formatAmount(row.adjustedAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="调整金额"
        prop="adjustmentAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span :class="getAmountClass(row.adjustmentAmount)">
            {{ formatAmount(row.adjustmentAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="审批状态"
        prop="approvalStatus"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="approvalStatusTagMap[row.approvalStatus]">
            {{ approvalStatusMap[row.approvalStatus] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="调整原因"
        prop="adjustmentReason"
        width="200"
        align="center"
        show-overflow-tooltip
      >
        <template slot-scope="{row}">
          <span>{{ row.adjustmentReason || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="影响评估"
        prop="impactAssessment"
        width="150"
        align="center"
        show-overflow-tooltip
      >
        <template slot-scope="{row}">
          <span>{{ row.impactAssessment || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="风险等级"
        prop="riskEvaluation"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag v-if="row.riskEvaluation" :type="riskTagMap[row.riskEvaluation]" size="mini">
            {{ riskMap[row.riskEvaluation] }}
          </el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="申请时间"
        prop="createdTime"
        width="160"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span>{{ row.createdTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="审批时间"
        prop="approveTime"
        width="160"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.approveTime">{{ row.approveTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
          <span v-else class="text-muted">-</span>
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
            v-if="row.approvalStatus === 'PENDING'"
            type="success"
            size="mini"
            @click="handleApprove(row)"
          >
            审批
          </el-button>
          <el-button
            v-if="row.approvalStatus === 'PENDING'"
            type="danger"
            size="mini"
            @click="handleReject(row)"
          >
            拒绝
          </el-button>
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
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
              <el-dropdown-item command="copy">复制调整</el-dropdown-item>
              <el-dropdown-item command="delete">删除</el-dropdown-item>
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

    <!-- 新增/编辑调整对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新建调整' : '编辑调整'" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-width="100px" style="width:500px;margin-left:30px;">
        <el-form-item label="关联计划" prop="planId">
          <el-select v-model="temp.planId" placeholder="请选择关联计划" style="width:100%" filterable>
            <el-option v-for="item in planList" :key="item.planId" :label="item.planName || item.planNo" :value="item.planId" />
          </el-select>
        </el-form-item>
        <el-form-item label="调整编号" prop="adjustmentNo">
          <el-input v-model="temp.adjustmentNo" placeholder="请输入调整编号" />
        </el-form-item>
        <el-form-item label="调整类型" prop="adjustmentType">
          <el-select v-model="temp.adjustmentType" placeholder="请选择调整类型" style="width:100%">
            <el-option v-for="item in adjustmentTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="调整金额" prop="adjustmentAmount">
          <el-input-number v-model="temp.adjustmentAmount" :precision="2" style="width:100%" />
        </el-form-item>
        <el-form-item label="调整原因">
          <el-input v-model="temp.adjustmentReason" type="textarea" :rows="3" placeholder="请输入调整原因" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog
      title="调整审批"
      :visible.sync="approvalDialogVisible"
      width="500px"
      @close="resetApprovalForm"
    >
      <el-form
        ref="approvalForm"
        :model="approvalForm"
        :rules="approvalRules"
        label-width="100px"
      >
        <el-form-item label="调整编号">
          <el-input v-model="approvalForm.adjustmentNo" disabled />
        </el-form-item>
        <el-form-item label="调整类型">
          <el-input v-model="approvalForm.adjustmentTypeText" disabled />
        </el-form-item>
        <el-form-item label="调整金额">
          <el-input v-model="approvalForm.adjustmentAmount" disabled>
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="审批结果" prop="approvalResult">
          <el-radio-group v-model="approvalForm.approvalResult">
            <el-radio label="APPROVED">同意</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见">
          <el-input
            v-model="approvalForm.approveComment"
            type="textarea"
            :rows="3"
            placeholder="请输入审批意见"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="approvalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmApproval">确定</el-button>
      </div>
    </el-dialog>

    <!-- 影响分析弹窗 -->
    <el-dialog title="调整影响分析" :visible.sync="impactAnalysisVisible" width="700px">
      <div class="impact-analysis">
        <el-row :gutter="16" style="margin-bottom:20px;">
          <el-col :span="8">
            <el-card shadow="never" class="impact-card">
              <div class="impact-num">{{ impactData.total }}</div>
              <div class="impact-label">调整总数</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="never" class="impact-card">
              <div class="impact-num text-success">{{ impactData.approveRate }}%</div>
              <div class="impact-label">审批通过率</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="never" class="impact-card">
              <div class="impact-num text-danger">{{ impactData.highRiskCount }}</div>
              <div class="impact-label">高风险调整数</div>
            </el-card>
          </el-col>
        </el-row>

        <el-divider content-position="left">调整类型分布</el-divider>
        <div v-for="item in impactData.typeStats" :key="item.type" class="stat-row">
          <span class="stat-label">{{ adjustmentTypeMap[item.type] || item.type }}</span>
          <el-progress :percentage="item.percent" :stroke-width="14" style="flex:1;margin:0 12px;" />
          <span class="stat-count">{{ item.count }} 条</span>
        </div>

        <el-divider content-position="left">风险等级分布</el-divider>
        <div v-for="item in impactData.riskStats" :key="item.risk" class="stat-row">
          <el-tag :type="riskTagMap[item.risk]" size="small" style="width:56px;text-align:center;">{{ riskMap[item.risk] || item.risk }}</el-tag>
          <el-progress :percentage="item.percent" :stroke-width="14" :color="item.color" style="flex:1;margin:0 12px;" />
          <span class="stat-count">{{ item.count }} 条</span>
        </div>

        <el-divider content-position="left">金额影响</el-divider>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="调整总金额">{{ formatAmount(summaryInfo.totalAdjustmentAmount) }}</el-descriptions-item>
          <el-descriptions-item label="已审批金额">{{ formatAmount(summaryInfo.approvedAmount) }}</el-descriptions-item>
          <el-descriptions-item label="待审批金额">{{ formatAmount(impactData.pendingAmount) }}</el-descriptions-item>
          <el-descriptions-item label="平均调整金额">{{ formatAmount(impactData.avgAmount) }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="impactAnalysisVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFundPlanAdjustmentPage, createFundPlanAdjustment, updateFundPlanAdjustment, deleteFundPlanAdjustment,
         getFundPlanAdjustmentSummary, approveFundPlanAdjustment, rejectFundPlanAdjustment, getFundPlanPage } from '@/api/globalTreasurer/zjjh'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils'

export default {
  name: 'FundPlanAdjustment',
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
        adjustmentType: undefined,
        approvalStatus: undefined,
        startDate: undefined,
        endDate: undefined,
        sort: '-createTime'
      },
      summaryInfo: {},
      planList: [],
      impactAnalysisVisible: false,
      impactData: {},
      approvalDialogVisible: false,
      dialogFormVisible: false,
      dialogStatus: 'create',
      multipleSelection: [],
      temp: {
        adjustmentId: undefined,
        planId: undefined,
        adjustmentNo: '',
        adjustmentType: '',
        adjustmentAmount: 0,
        adjustmentReason: ''
      },
      rules: {
        planId: [{ required: true, message: '请选择关联计划', trigger: 'change' }],
        adjustmentNo: [{ required: true, message: '请输入调整编号', trigger: 'blur' }],
        adjustmentType: [{ required: true, message: '请选择调整类型', trigger: 'change' }],
        adjustmentAmount: [{ required: true, message: '请输入调整金额', trigger: 'blur' }]
      },
      approvalForm: {
        adjustmentId: null,
        adjustmentNo: '',
        adjustmentTypeText: '',
        adjustmentAmount: '',
        approvalResult: '',
        approveComment: ''
      },
      approvalRules: {
        approvalResult: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ]
      },
      adjustmentTypeOptions: [
        { label: '金额调整', value: 'AMOUNT' },
        { label: '时间调整', value: 'DATE' },
        { label: '项目调整', value: 'PROJECT' },
        { label: '紧急调整', value: 'EMERGENCY' },
        { label: '取消调整', value: 'CANCEL' }
      ],
      approvalStatusOptions: [
        { label: '待审批', value: 'PENDING' },
        { label: '已审批', value: 'APPROVED' },
        { label: '已拒绝', value: 'REJECTED' }
      ],
      adjustmentTypeMap: {
        'AMOUNT': '金额调整',
        'DATE': '时间调整',
        'PROJECT': '项目调整',
        'EMERGENCY': '紧急调整',
        'CANCEL': '取消调整'
      },
      adjustmentTypeTagMap: {
        'AMOUNT': 'primary',
        'DATE': 'success',
        'PROJECT': 'info',
        'EMERGENCY': 'danger',
        'CANCEL': 'warning'
      },
      approvalStatusMap: {
        'PENDING': '待审批',
        'APPROVED': '已审批',
        'REJECTED': '已拒绝'
      },
      approvalStatusTagMap: {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      },
      riskMap: {
        'HIGH': '高风险',
        'MEDIUM': '中风险',
        'LOW': '低风险'
      },
      riskTagMap: {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
    }
  },
  created() {
    this.getList()
    this.getSummaryInfo()
    this.getPlanList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getFundPlanAdjustmentPage(this.listQuery).then(response => {
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
      getFundPlanAdjustmentSummary(this.listQuery).then(response => {
        if (response.code === 1) {
          this.summaryInfo = response.data
        }
      })
    },
    getPlanList() {
      getFundPlanPage({ current: 1, size: 200 }).then(response => {
        if (response.code === 1) {
          this.planList = response.data.rows || response.data.records || []
        }
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
      this.getSummaryInfo()
    },

    sortChange(data) {
      const { prop, order } = data
      if (prop === 'createdTime') {
        this.sortByCreateTime(order)
      }
    },
    sortByCreateTime(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+createdTime'
      } else {
        this.listQuery.sort = '-createdTime'
      }
      this.handleFilter()
    },
    handleCreate() {
      this.temp = {
        adjustmentId: undefined,
        adjustmentNo: '',
        adjustmentType: '',
        adjustmentAmount: 0,
        adjustmentReason: ''
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
          createFundPlanAdjustment(this.temp).then(response => {
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
          updateFundPlanAdjustment(this.temp).then(response => {
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
        Promise.all(this.multipleSelection.map(row => deleteFundPlanAdjustment(row.adjustmentId))).then(() => {
          this.$message.success('批量删除成功')
          this.multipleSelection = []
          this.getList()
          this.getSummaryInfo()
        })
      })
    },
    handleApprove(row) {
      this.approvalForm = {
        adjustmentId: row.adjustmentId,
        adjustmentNo: row.adjustmentNo,
        adjustmentTypeText: this.adjustmentTypeMap[row.adjustmentType],
        adjustmentAmount: row.adjustmentAmount,
        approvalResult: 'APPROVED',
        approveComment: ''
      }
      this.approvalDialogVisible = true
    },
    handleReject(row) {
      this.approvalForm = {
        adjustmentId: row.adjustmentId,
        adjustmentNo: row.adjustmentNo,
        adjustmentTypeText: this.adjustmentTypeMap[row.adjustmentType],
        adjustmentAmount: row.adjustmentAmount,
        approvalResult: 'REJECTED',
        approveComment: ''
      }
      this.approvalDialogVisible = true
    },
    confirmApproval() {
      this.$refs.approvalForm.validate(valid => {
        if (valid) {
          const approvalData = {
            adjustmentId: this.approvalForm.adjustmentId,
            approveComment: this.approvalForm.approveComment
          }

          const apiCall = this.approvalForm.approvalResult === 'APPROVED'
            ? approveFundPlanAdjustment(this.approvalForm.adjustmentId, true, this.approvalForm.approveComment)
            : rejectFundPlanAdjustment(this.approvalForm.adjustmentId, this.approvalForm.approveComment)

          apiCall.then(response => {
            if (response.code === 1) {
              this.$message.success(this.approvalForm.approvalResult === 'APPROVED' ? '审批成功' : '拒绝成功')
              this.approvalDialogVisible = false
              this.getList()
              this.getSummaryInfo()
            } else {
              this.$message.error(response.msg || '操作失败')
            }
          })
        }
      })
    },
    resetApprovalForm() {
      this.approvalForm = {
        adjustmentId: null,
        adjustmentNo: '',
        adjustmentTypeText: '',
        adjustmentAmount: '',
        approvalResult: '',
        approveComment: ''
      }
      if (this.$refs.approvalForm) {
        this.$refs.approvalForm.resetFields()
      }
    },
    handleCommand(command, row) {
      switch (command) {
        case 'detail':
          this.showDetail(row)
          break
        case 'copy':
          this.copyAdjustment(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    showDetail(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    copyAdjustment(row) {
      this.temp = Object.assign({}, row)
      delete this.temp.adjustmentId
      this.temp.adjustmentNo = `${row.adjustmentNo}_COPY`
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该调整记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFundPlanAdjustment(row.adjustmentId).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
            this.getSummaryInfo()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      })
    },
    showImpactAnalysis() {
      const data = this.list
      const total = data.length
      const approved = data.filter(r => r.approvalStatus === 'APPROVED')
      const pending = data.filter(r => r.approvalStatus === 'PENDING')
      const approveRate = total > 0 ? Math.round(approved.length / total * 100) : 0

      const typeCount = {}
      const riskCount = {}
      data.forEach(r => {
        if (r.adjustmentType) typeCount[r.adjustmentType] = (typeCount[r.adjustmentType] || 0) + 1
        if (r.riskEvaluation) riskCount[r.riskEvaluation] = (riskCount[r.riskEvaluation] || 0) + 1
      })

      const typeStats = Object.keys(typeCount).map(type => ({
        type,
        count: typeCount[type],
        percent: total > 0 ? Math.round(typeCount[type] / total * 100) : 0
      }))

      const riskColorMap = { HIGH: '#F56C6C', MEDIUM: '#E6A23C', LOW: '#67C23A' }
      const riskStats = Object.keys(riskCount).map(risk => ({
        risk,
        count: riskCount[risk],
        percent: total > 0 ? Math.round(riskCount[risk] / total * 100) : 0,
        color: riskColorMap[risk] || '#909399'
      }))

      const pendingAmount = pending.reduce((s, r) => s + (r.adjustmentAmount || 0), 0)
      const avgAmount = total > 0 ? data.reduce((s, r) => s + (r.adjustmentAmount || 0), 0) / total : 0

      this.impactData = {
        total,
        approveRate,
        highRiskCount: riskCount['HIGH'] || 0,
        typeStats,
        riskStats,
        pendingAmount,
        avgAmount
      }
      this.impactAnalysisVisible = true
    },
    showPendingApproval() {
      this.listQuery.approvalStatus = 'PENDING'
      this.handleFilter()
    },
    getAmountClass(amount) {
      if (amount > 0) return 'text-success'
      if (amount < 0) return 'text-danger'
      return ''
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
.impact-card {
  text-align: center;
  padding: 8px 0;
}
.impact-num {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
.impact-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}
.stat-row {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
.stat-label {
  width: 70px;
  font-size: 13px;
  color: #606266;
  flex-shrink: 0;
}
.stat-count {
  width: 50px;
  text-align: right;
  font-size: 13px;
  color: #606266;
  flex-shrink: 0;
}
</style>
