<template>
  <div class="rolling-forecast-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card total">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
              <div class="stat-label">预测总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card draft">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-edit-outline"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.draftCount || 0 }}</div>
              <div class="stat-label">草稿</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card submitted">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-s-promotion"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.submittedCount || 0 }}</div>
              <div class="stat-label">已提交</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card approved">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.approvedCount || 0 }}</div>
              <div class="stat-label">已审批</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="预测编号">
          <el-input v-model="queryForm.forecastNo" placeholder="请输入预测编号" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="预测名称">
          <el-input v-model="queryForm.forecastName" placeholder="请输入预测名称" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="预测类型">
          <el-select v-model="queryForm.forecastType" placeholder="请选择" clearable style="width: 120px">
            <el-option label="月度预测" value="MONTHLY" />
            <el-option label="季度预测" value="QUARTERLY" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable style="width: 120px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已提交" value="SUBMITTED" />
            <el-option label="已审批" value="APPROVED" />
            <el-option label="已驳回" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <div slot="header" class="card-header">
        <span>预测任务列表</span>
        <div>
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="small"
            @click="handleCreate"
          >
            新建预测任务
          </el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="small"
            :disabled="multipleSelection.length === 0"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="forecastNo" label="预测编号" width="150" show-overflow-tooltip />
        <el-table-column prop="forecastName" label="预测名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="modelName" label="预算模型" min-width="150" show-overflow-tooltip />
        <el-table-column label="预测类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.forecastType === 'MONTHLY'" type="primary" size="small">月度预测</el-tag>
            <el-tag v-else type="success" size="small">季度预测</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startPeriod" label="开始期间" width="100" align="center" />
        <el-table-column prop="endPeriod" label="结束期间" width="100" align="center" />
        <el-table-column prop="version" label="版本" width="100" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'DRAFT'" type="info" size="small">草稿</el-tag>
            <el-tag v-else-if="scope.row.status === 'SUBMITTED'" type="warning" size="small">已提交</el-tag>
            <el-tag v-else-if="scope.row.status === 'APPROVED'" type="success" size="small">已审批</el-tag>
            <el-tag v-else type="danger" size="small">已驳回</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
        <el-table-column label="操作" width="300" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button v-if="scope.row.status === 'DRAFT'" type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="scope.row.status === 'DRAFT'" type="text" size="small" icon="el-icon-s-promotion" @click="handleSubmit(scope.row)">提交</el-button>
            <el-button v-if="scope.row.status === 'SUBMITTED'" type="text" size="small" icon="el-icon-check" @click="handleApprove(scope.row, true)">通过</el-button>
            <el-button v-if="scope.row.status === 'SUBMITTED'" type="text" size="small" icon="el-icon-close" @click="handleApprove(scope.row, false)">驳回</el-button>
            <el-button v-if="scope.row.status === 'SUBMITTED'" type="text" size="small" icon="el-icon-refresh-left" @click="handleWithdraw(scope.row)">撤销</el-button>
            <el-button v-if="scope.row.status === 'DRAFT'" type="text" size="small" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页(与其他页面统一使用项目公共组件) -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryForm.pageNum"
        :limit.sync="queryForm.pageSize"
        @pagination="loadData"
      />
    </el-card>

    <!-- 新建/编辑/查看 三合一对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1200px"
      :close-on-click-modal="false"
      @close="resetDialog"
    >
      <el-form
        ref="dialogForm"
        :model="dialogForm"
        :rules="dialogMode === 'view' ? {} : dialogRules"
        :disabled="dialogMode === 'view'"
        label-width="100px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预测名称" prop="forecastName">
              <el-input v-model="dialogForm.forecastName" placeholder="请输入预测名称" maxlength="100" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算模型" prop="modelId">
              <el-select v-model="dialogForm.modelId" placeholder="请选择预算模型" clearable filterable style="width: 100%">
                <el-option v-for="item in modelList" :key="item.modelId" :label="item.modelName" :value="item.modelId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预测类型" prop="forecastType">
              <el-select v-model="dialogForm.forecastType" placeholder="请选择" style="width: 100%">
                <el-option label="月度预测" value="MONTHLY" />
                <el-option label="季度预测" value="QUARTERLY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="基准类型" prop="baseType">
              <el-select v-model="dialogForm.baseType" placeholder="请选择" style="width: 100%">
                <el-option label="历史数据" value="HISTORY" />
                <el-option label="预算数据" value="BUDGET" />
                <el-option label="实际数据" value="ACTUAL" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始期间" prop="startPeriod">
              <el-input v-model="dialogForm.startPeriod" placeholder="例如 2026-01" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束期间" prop="endPeriod">
              <el-input v-model="dialogForm.endPeriod" placeholder="例如 2026-12" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预测版本" prop="version">
              <el-input v-model="dialogForm.version" placeholder="例如 V1.0" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="预测说明">
              <el-input v-model="dialogForm.forecastDesc" type="textarea" :rows="2" placeholder="请输入预测说明" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 预测明细 -->
        <div class="detail-toolbar">
          <span class="detail-title">预测明细</span>
          <el-button v-if="dialogMode !== 'view'" type="primary" size="mini" icon="el-icon-plus" @click="handleAddDetail">新增明细</el-button>
        </div>
        <el-table :data="dialogForm.dataList" border stripe size="small" style="width: 100%">
          <el-table-column type="index" label="#" width="50" align="center" />
          <el-table-column label="期间" min-width="100">
            <template slot-scope="{ row }">
              <el-input v-if="dialogMode !== 'view'" v-model="row.period" placeholder="如 2026-01" size="mini" />
              <span v-else>{{ row.period }}</span>
            </template>
          </el-table-column>
          <el-table-column label="科目编码" min-width="110">
            <template slot-scope="{ row }">
              <el-input v-if="dialogMode !== 'view'" v-model="row.subjectCode" placeholder="科目编码" size="mini" />
              <span v-else>{{ row.subjectCode }}</span>
            </template>
          </el-table-column>
          <el-table-column label="科目名称" min-width="130">
            <template slot-scope="{ row }">
              <el-input v-if="dialogMode !== 'view'" v-model="row.subjectName" placeholder="科目名称" size="mini" />
              <span v-else>{{ row.subjectName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="组织编码" min-width="110">
            <template slot-scope="{ row }">
              <el-input v-if="dialogMode !== 'view'" v-model="row.organizationCode" placeholder="组织编码" size="mini" />
              <span v-else>{{ row.organizationCode }}</span>
            </template>
          </el-table-column>
          <el-table-column label="基准值" min-width="110">
            <template slot-scope="{ row }">
              <el-input-number v-if="dialogMode !== 'view'" v-model="row.baseValue" :precision="2" :controls="false" size="mini" style="width: 100%" @change="recalcRow(row)" />
              <span v-else>{{ row.baseValue }}</span>
            </template>
          </el-table-column>
          <el-table-column label="预测值" min-width="110">
            <template slot-scope="{ row }">
              <el-input-number v-if="dialogMode !== 'view'" v-model="row.forecastValue" :precision="2" :controls="false" size="mini" style="width: 100%" @change="recalcRow(row)" />
              <span v-else>{{ row.forecastValue }}</span>
            </template>
          </el-table-column>
          <el-table-column label="实际值" min-width="110">
            <template slot-scope="{ row }">
              <el-input-number v-if="dialogMode !== 'view'" v-model="row.actualValue" :precision="2" :controls="false" size="mini" style="width: 100%" />
              <span v-else>{{ row.actualValue }}</span>
            </template>
          </el-table-column>
          <el-table-column label="差异值" min-width="100">
            <template slot-scope="{ row }">
              <span>{{ row.varianceValue || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="差异率(%)" min-width="100">
            <template slot-scope="{ row }">
              <span>{{ row.varianceRate || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="备注" min-width="120">
            <template slot-scope="{ row }">
              <el-input v-if="dialogMode !== 'view'" v-model="row.remark" placeholder="备注" size="mini" />
              <span v-else>{{ row.remark }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="dialogMode !== 'view'" label="操作" width="70" align="center">
            <template slot-scope="{ $index }">
              <el-button type="text" style="color: #F56C6C" size="mini" @click="handleRemoveDetail($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>

      <div slot="footer">
        <el-button @click="dialogVisible = false">{{ dialogMode === 'view' ? '关闭' : '取消' }}</el-button>
        <el-button v-if="dialogMode !== 'view'" type="primary" :loading="dialogLoading" @click="handleDialogConfirm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog
      :title="approveDialogTitle"
      :visible.sync="approveDialogVisible"
      width="500px"
    >
      <el-form :model="approveForm" label-width="100px">
        <el-form-item label="审批意见">
          <el-input
            v-model="approveForm.opinion"
            type="textarea"
            :rows="4"
            placeholder="请输入审批意见"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="approveDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmApprove">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getForecastList,
  getForecastWithData,
  createForecast,
  updateForecast,
  deleteForecast,
  batchDeleteForecast,
  submitForecast,
  approveForecast,
  withdrawForecast,
  getForecastStatistics
} from '@/api/financialSharing/budgetPlanning/rollingForecast'
import { getModelListNoPage } from '@/api/financialSharing/budgetPlanning/budgetModel'
import Pagination from '@/components/Pagination'

export default {
  name: 'RollingForecast',
  components: { Pagination },
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      multipleSelection: [],
      modelList: [],
      queryForm: {
        forecastNo: '',
        forecastName: '',
        forecastType: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      },
      statistics: {
        totalCount: 0,
        draftCount: 0,
        submittedCount: 0,
        approvedCount: 0,
        rejectedCount: 0
      },
      // 审批对话框
      approveDialogVisible: false,
      approveDialogTitle: '',
      approveForm: {
        forecastId: '',
        approved: true,
        opinion: ''
      },
      // 新建/编辑/查看 三合一对话框
      dialogVisible: false,
      dialogMode: 'create', // create | edit | view
      dialogLoading: false,
      dialogForm: this.buildEmptyForm(),
      dialogRules: {
        forecastName: [{ required: true, message: '请输入预测名称', trigger: 'blur' }],
        modelId: [{ required: true, message: '请选择预算模型', trigger: 'change' }],
        forecastType: [{ required: true, message: '请选择预测类型', trigger: 'change' }],
        baseType: [{ required: true, message: '请选择基准类型', trigger: 'change' }],
        startPeriod: [{ required: true, message: '请输入开始期间', trigger: 'blur' }],
        endPeriod: [{ required: true, message: '请输入结束期间', trigger: 'blur' }],
        version: [{ required: true, message: '请输入预测版本', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      const map = { create: '新建预测任务', edit: '编辑预测任务', view: '查看预测任务' }
      return map[this.dialogMode] || '预测任务'
    }
  },
  created() {
    this.loadModelList()
    this.loadStatistics()
    this.loadData()
  },
  methods: {
    // 构造空表单(给 data() 复用)
    buildEmptyForm() {
      return {
        forecastId: '',
        forecastNo: '',
        forecastName: '',
        modelId: '',
        forecastType: 'MONTHLY',
        baseType: 'BUDGET',
        startPeriod: '',
        endPeriod: '',
        version: '',
        forecastDesc: '',
        dataList: []
      }
    },
    // 加载预算模型列表
    async loadModelList() {
      try {
        const res = await getModelListNoPage({ status: 'ACTIVE' })
        if (res.code === 1) {
          this.modelList = res.data || []
        }
      } catch (error) {
        console.error('加载模型列表失败:', error)
      }
    },
    // 加载统计信息
    async loadStatistics() {
      try {
        const res = await getForecastStatistics()
        // 调试用: 浏览器 DevTools Console 直接看接口返回
        console.log('[RollingForecast] getForecastStatistics 返回:', res)
        if (res.code === 1 && res.data) {
          this.statistics = {
            totalCount: Number(res.data.totalCount) || 0,
            draftCount: Number(res.data.draftCount) || 0,
            submittedCount: Number(res.data.submittedCount) || 0,
            approvedCount: Number(res.data.approvedCount) || 0,
            rejectedCount: Number(res.data.rejectedCount) || 0
          }
        } else {
          this.$message.warning(res.msg || '加载统计信息失败, 请查看控制台')
        }
      } catch (error) {
        console.error('加载统计信息失败:', error)
        this.$message.error('加载统计信息失败: ' + (error.message || error))
      }
    },
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const res = await getForecastList(this.queryForm)
        if (res.code === 1) {
          this.tableData = res.data.list || []
          this.total = res.data.total || 0
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },
    // 查询
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },
    // 重置
    handleReset() {
      this.queryForm = {
        forecastNo: '',
        forecastName: '',
        forecastType: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      }
      this.loadData()
    },
    // 多选
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 新建 -> 弹窗
    handleCreate() {
      this.dialogMode = 'create'
      this.dialogForm = this.buildEmptyForm()
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.dialogForm && this.$refs.dialogForm.clearValidate()
      })
    },
    // 查看 -> 弹窗
    async handleView(row) {
      this.dialogMode = 'view'
      this.dialogForm = this.buildEmptyForm()
      this.dialogVisible = true
      await this.loadDialogData(row.forecastId)
    },
    // 编辑 -> 弹窗
    async handleEdit(row) {
      this.dialogMode = 'edit'
      this.dialogForm = this.buildEmptyForm()
      this.dialogVisible = true
      await this.loadDialogData(row.forecastId)
      this.$nextTick(() => {
        this.$refs.dialogForm && this.$refs.dialogForm.clearValidate()
      })
    },
    // 加载弹窗详情(查看/编辑共用)
    async loadDialogData(forecastId) {
      this.dialogLoading = true
      try {
        const res = await getForecastWithData({ forecastId })
        if (res.code === 1 && res.data) {
          const data = res.data
          this.dialogForm = {
            forecastId: data.forecastId,
            forecastNo: data.forecastNo,
            forecastName: data.forecastName,
            modelId: data.modelId,
            forecastType: data.forecastType,
            baseType: data.baseType,
            startPeriod: data.startPeriod,
            endPeriod: data.endPeriod,
            version: data.version,
            forecastDesc: data.forecastDesc,
            dataList: (data.dataList || []).map(d => ({ ...d }))
          }
        } else {
          this.$message.error(res.msg || '加载详情失败')
        }
      } catch (error) {
        this.$message.error('加载详情失败: ' + error.message)
      } finally {
        this.dialogLoading = false
      }
    },
    // 新增明细行
    handleAddDetail() {
      this.dialogForm.dataList.push({
        period: '',
        subjectCode: '',
        subjectName: '',
        organizationCode: '',
        organizationName: '',
        baseValue: 0,
        forecastValue: 0,
        actualValue: 0,
        varianceValue: 0,
        varianceRate: 0,
        remark: ''
      })
    },
    // 删除明细行
    handleRemoveDetail(index) {
      this.dialogForm.dataList.splice(index, 1)
    },
    // 重新计算: 差异值 = 预测值 - 基准值; 差异率 = 差异值 / 基准值 * 100%
    recalcRow(row) {
      const forecast = Number(row.forecastValue) || 0
      const base = Number(row.baseValue) || 0
      const variance = Number((forecast - base).toFixed(2))
      row.varianceValue = variance
      if (base !== 0) {
        row.varianceRate = Number((variance / base * 100).toFixed(2))
      } else {
        row.varianceRate = 0
      }
    },
    // 确认提交弹窗(新建或编辑)
    handleDialogConfirm() {
      this.$refs.dialogForm.validate(async valid => {
        if (!valid) return
        // 把所有明细的 variance 重算一遍, 防止用户没触发 change
        this.dialogForm.dataList.forEach(d => this.recalcRow(d))
        this.dialogLoading = true
        try {
          const api = this.dialogMode === 'create' ? createForecast : updateForecast
          const res = await api(this.dialogForm)
          if (res.code === 1) {
            this.$message.success(this.dialogMode === 'create' ? '新建成功' : '编辑成功')
            this.dialogVisible = false
            this.loadStatistics()
            this.loadData()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败: ' + error.message)
        } finally {
          this.dialogLoading = false
        }
      })
    },
    // 关闭对话框时复原
    resetDialog() {
      this.dialogForm = this.buildEmptyForm()
      this.dialogLoading = false
      if (this.$refs.dialogForm) {
        this.$refs.dialogForm.clearValidate()
      }
    },
    // 提交
    handleSubmit(row) {
      this.$confirm('确认提交该预测任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await submitForecast({ forecastId: row.forecastId })
          if (res.code === 1) {
            this.$message.success('提交成功')
            this.loadStatistics()
            this.loadData()
          } else {
            this.$message.error(res.msg || '提交失败')
          }
        } catch (error) {
          this.$message.error('提交失败: ' + error.message)
        }
      }).catch(() => {})
    },
    // 审批
    handleApprove(row, approved) {
      this.approveForm.forecastId = row.forecastId
      this.approveForm.approved = approved
      this.approveForm.opinion = ''
      this.approveDialogTitle = approved ? '审批通过' : '审批驳回'
      this.approveDialogVisible = true
    },
    async confirmApprove() {
      try {
        const res = await approveForecast(this.approveForm)
        if (res.code === 1) {
          this.$message.success('审批成功')
          this.approveDialogVisible = false
          this.loadStatistics()
          this.loadData()
        } else {
          this.$message.error(res.msg || '审批失败')
        }
      } catch (error) {
        this.$message.error('审批失败: ' + error.message)
      }
    },
    // 撤销
    handleWithdraw(row) {
      this.$confirm('确认撤销该预测任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await withdrawForecast({ forecastId: row.forecastId })
          if (res.code === 1) {
            this.$message.success('撤销成功')
            this.loadStatistics()
            this.loadData()
          } else {
            this.$message.error(res.msg || '撤销失败')
          }
        } catch (error) {
          this.$message.error('撤销失败: ' + error.message)
        }
      }).catch(() => {})
    },
    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该预测任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteForecast({ forecastId: row.forecastId })
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadStatistics()
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败: ' + error.message)
        }
      }).catch(() => {})
    },
    // 批量删除
    handleBatchDelete() {
      this.$confirm('确认批量删除选中的预测任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const forecastIds = this.multipleSelection.map(item => item.forecastId)
          const res = await batchDeleteForecast({ forecastIds })
          if (res.code === 1) {
            this.$message.success('批量删除成功')
            this.loadStatistics()
            this.loadData()
          } else {
            this.$message.error(res.msg || '批量删除失败')
          }
        } catch (error) {
          this.$message.error('批量删除失败: ' + error.message)
        }
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.rolling-forecast-container {
  padding: 20px;

  .statistics-row {
    margin-bottom: 20px;

    .stat-card {
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      .stat-content {
        display: flex;
        align-items: center;

        .stat-icon {
          font-size: 48px;
          margin-right: 20px;
        }

        .stat-info {
          flex: 1;

          .stat-value {
            font-size: 32px;
            font-weight: bold;
            margin-bottom: 5px;
          }

          .stat-label {
            font-size: 14px;
            color: #909399;
          }
        }
      }

      &.total {
        .stat-icon {
          color: #409EFF;
        }
        .stat-value {
          color: #409EFF;
        }
      }

      &.draft {
        .stat-icon {
          color: #909399;
        }
        .stat-value {
          color: #909399;
        }
      }

      &.submitted {
        .stat-icon {
          color: #E6A23C;
        }
        .stat-value {
          color: #E6A23C;
        }
      }

      &.approved {
        .stat-icon {
          color: #67C23A;
        }
        .stat-value {
          color: #67C23A;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }

  .detail-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 12px 0 8px;

    .detail-title {
      font-size: 14px;
      font-weight: bold;
      color: #303133;
    }
  }
}
</style>

