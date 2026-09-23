<template>
  <div class="strategy-execution-monitoring" :style="themeVars">
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="mini-stat-card">
          <div class="stat-value">{{ stats.total || 0 }}</div>
          <div class="stat-label">总监控项</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="mini-stat-card">
          <div class="stat-value" :style="{ color: ipBright }">{{ stats.normalCount || 0 }}</div>
          <div class="stat-label">正常执行</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="mini-stat-card">
          <div class="stat-value" style="color: #F56C6C;">{{ stats.delayedCount || 0 }}</div>
          <div class="stat-label">延期执行</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="mini-stat-card">
          <div class="stat-value" style="color: #67C23A;">{{ stats.avgProgress || 0 }}%</div>
          <div class="stat-label">平均进度</div>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="执行编号" prop="executionNo">
          <el-input v-model="queryForm.executionNo" placeholder="请输入执行编号" clearable />
        </el-form-item>
        <el-form-item label="战略计划" prop="planName">
          <el-input v-model="queryForm.planName" placeholder="请输入战略计划" clearable />
        </el-form-item>
        <el-form-item label="执行阶段" prop="executionPhase">
          <el-select v-model="queryForm.executionPhase" placeholder="请选择执行阶段" clearable>
            <el-option label="启动阶段" value="启动阶段" />
            <el-option label="实施阶段" value="实施阶段" />
            <el-option label="监控阶段" value="监控阶段" />
            <el-option label="收尾阶段" value="收尾阶段" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择执行状态" clearable>
            <el-option label="正常执行" value="正常执行" />
            <el-option label="延期执行" value="延期执行" />
            <el-option label="风险预警" value="风险预警" />
            <el-option label="暂停执行" value="暂停执行" />
            <el-option label="执行完成" value="执行完成" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="executor">
          <el-input v-model="queryForm.executor" placeholder="请输入负责人" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card shadow="never" class="action-card">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增监控</el-button>
      <el-button type="danger" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="executionNo" label="执行编号" width="130" />
        <el-table-column prop="planName" label="战略计划" width="180" show-overflow-tooltip />
        <el-table-column prop="executionPhase" label="执行阶段" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPhaseColor(scope.row.executionPhase)" size="small">{{ scope.row.executionPhase }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executor" label="负责人" width="90" />
        <el-table-column prop="department" label="部门" width="110" />
        <el-table-column prop="startDate" label="开始日期" width="110" />
        <el-table-column prop="planEndDate" label="计划结束日期" width="120" />
        <el-table-column prop="actualProgress" label="实际进度" width="140">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.actualProgress || 0" :color="getProgressColor(scope.row.actualProgress)" />
          </template>
        </el-table-column>
        <el-table-column prop="planProgress" label="计划进度" width="140">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.planProgress || 0" color="#909399" />
          </template>
        </el-table-column>
        <el-table-column prop="progressDeviation" label="进度偏差" width="100">
          <template slot-scope="scope">
            <span :class="getDeviationClass(scope.row.progressDeviation)">{{ scope.row.progressDeviation }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="budgetUsed" label="预算使用(万元)" width="120">
          <template slot-scope="scope">
            <span class="budget-used">{{ scope.row.budgetUsed }}万元</span>
          </template>
        </el-table-column>
        <el-table-column label="里程碑" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.completedMilestones || 0 }}/{{ scope.row.totalMilestones || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="riskCount" label="风险数量" width="90">
          <template slot-scope="scope">
            <el-tag :type="getRiskCountType(scope.row.riskCount)" size="mini">{{ scope.row.riskCount || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="执行状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdateDate" label="最后更新" width="110" />
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="editDialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="战略计划" prop="planName">
              <el-select v-model="editForm.planName" placeholder="请选择战略计划" filterable style="width: 100%;">
                <el-option v-for="item in planOptions" :key="item.id" :label="item.planName" :value="item.planName" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行阶段" prop="executionPhase">
              <el-select v-model="editForm.executionPhase" placeholder="请选择执行阶段" style="width: 100%;">
                <el-option label="启动阶段" value="启动阶段" />
                <el-option label="实施阶段" value="实施阶段" />
                <el-option label="监控阶段" value="监控阶段" />
                <el-option label="收尾阶段" value="收尾阶段" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人" prop="executor">
              <el-input v-model="editForm.executor" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门" prop="department">
              <el-input v-model="editForm.department" placeholder="请输入部门" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker v-model="editForm.startDate" type="date" placeholder="请选择开始日期" value-format="yyyy-MM-dd" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划结束" prop="planEndDate">
              <el-date-picker v-model="editForm.planEndDate" type="date" placeholder="请选择计划结束日期" value-format="yyyy-MM-dd" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="实际进度" prop="actualProgress">
              <el-slider v-model="editForm.actualProgress" :min="0" :max="100" show-input />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划进度" prop="planProgress">
              <el-slider v-model="editForm.planProgress" :min="0" :max="100" show-input />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算使用" prop="budgetUsed">
              <el-input-number v-model="editForm.budgetUsed" :min="0" :precision="2" placeholder="万元" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总里程碑" prop="totalMilestones">
              <el-input-number v-model="editForm.totalMilestones" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="已完成碑" prop="completedMilestones">
              <el-input-number v-model="editForm.completedMilestones" :min="0" :max="editForm.totalMilestones" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险数量" prop="riskCount">
              <el-input-number v-model="editForm.riskCount" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="执行状态" prop="status">
              <el-select v-model="editForm.status" placeholder="请选择执行状态" style="width: 100%;">
                <el-option label="正常执行" value="正常执行" />
                <el-option label="延期执行" value="延期执行" />
                <el-option label="风险预警" value="风险预警" />
                <el-option label="暂停执行" value="暂停执行" />
                <el-option label="执行完成" value="执行完成" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="执行监控详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border v-if="selectedItem">
        <el-descriptions-item label="执行编号">{{ selectedItem.executionNo }}</el-descriptions-item>
        <el-descriptions-item label="战略计划">{{ selectedItem.planName }}</el-descriptions-item>
        <el-descriptions-item label="执行阶段">
          <el-tag :type="getPhaseColor(selectedItem.executionPhase)">{{ selectedItem.executionPhase }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="执行状态">
          <el-tag :type="getStatusType(selectedItem.status)">{{ selectedItem.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="负责人">{{ selectedItem.executor }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ selectedItem.department }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ selectedItem.startDate }}</el-descriptions-item>
        <el-descriptions-item label="计划结束日期">{{ selectedItem.planEndDate }}</el-descriptions-item>
        <el-descriptions-item label="实际进度">
          <el-progress :percentage="selectedItem.actualProgress || 0" :color="getProgressColor(selectedItem.actualProgress)" />
        </el-descriptions-item>
        <el-descriptions-item label="计划进度">
          <el-progress :percentage="selectedItem.planProgress || 0" color="#909399" />
        </el-descriptions-item>
        <el-descriptions-item label="进度偏差">
          <span :class="getDeviationClass(selectedItem.progressDeviation)">{{ selectedItem.progressDeviation }}%</span>
        </el-descriptions-item>
        <el-descriptions-item label="预算使用">{{ selectedItem.budgetUsed }}万元</el-descriptions-item>
        <el-descriptions-item label="里程碑完成">{{ selectedItem.completedMilestones || 0 }}/{{ selectedItem.totalMilestones || 0 }}</el-descriptions-item>
        <el-descriptions-item label="风险数量">
          <el-tag :type="getRiskCountType(selectedItem.riskCount)" size="mini">{{ selectedItem.riskCount || 0 }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="最后更新">{{ selectedItem.lastUpdateDate || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getExecutionList, addExecution, updateExecution, deleteExecution, getExecutionStatistics, getPlanningAll } from '@/api/enterprise/strategy'
import { investThemeMixin } from '@/views/stateAssets/themeMixin'

export default {
  name: 'StrategyExecutionMonitoring',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      stats: {
        total: 0,
        normalCount: 0,
        delayedCount: 0,
        avgProgress: 0
      },
      queryForm: {
        executionNo: '',
        planName: '',
        executionPhase: '',
        status: '',
        executor: ''
      },
      tableData: [],
      multipleSelection: [],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      editDialogVisible: false,
      detailDialogVisible: false,
      isEdit: false,
      editForm: {
        id: null,
        planName: '',
        executionPhase: '',
        executor: '',
        department: '',
        startDate: '',
        planEndDate: '',
        actualProgress: 0,
        planProgress: 0,
        budgetUsed: 0,
        totalMilestones: 0,
        completedMilestones: 0,
        riskCount: 0,
        status: ''
      },
      editRules: {
        planName: [{ required: true, message: '请输入战略计划名称', trigger: 'blur' }]
      },
      selectedItem: null,
      planOptions: []
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑执行监控' : '新增执行监控'
    }
  },
  mounted() {
    this.loadList()
    this.loadStats()
    this.loadPlanOptions()
  },
  methods: {
    async loadList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }
        const res = await getExecutionList(params)
        if (res && res.result === 200 && res.data) {
          this.tableData = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        }
      } catch (error) {
        console.error('加载执行监控列表失败', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadStats() {
      try {
        const res = await getExecutionStatistics()
        if (res && res.result === 200 && res.data) {
          this.stats = res.data
        }
      } catch (error) {
        console.error('加载统计数据失败', error)
      }
    },
    async loadPlanOptions() {
      try {
        const res = await getPlanningAll()
        if (res && res.result === 200 && res.data) {
          this.planOptions = res.data
        }
      } catch (error) {
        console.error('加载战略规划列表失败', error)
      }
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadList()
    },
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.pagination.currentPage = 1
      this.loadList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadList()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadList()
    },
    handleAdd() {
      this.isEdit = false
      this.editForm = {
        id: null,
        planName: '',
        executionPhase: '',
        executor: '',
        department: '',
        startDate: '',
        planEndDate: '',
        actualProgress: 0,
        planProgress: 0,
        budgetUsed: 0,
        totalMilestones: 0,
        completedMilestones: 0,
        riskCount: 0,
        status: ''
      }
      this.editDialogVisible = true
      this.$nextTick(() => {
        this.$refs.editFormRef && this.$refs.editFormRef.clearValidate()
      })
    },
    handleEdit(row) {
      this.isEdit = true
      this.editForm = {
        id: row.id,
        planName: row.planName,
        executionPhase: row.executionPhase,
        executor: row.executor,
        department: row.department,
        startDate: row.startDate,
        planEndDate: row.planEndDate,
        actualProgress: row.actualProgress || 0,
        planProgress: row.planProgress || 0,
        budgetUsed: row.budgetUsed || 0,
        totalMilestones: row.totalMilestones || 0,
        completedMilestones: row.completedMilestones || 0,
        riskCount: row.riskCount || 0,
        status: row.status
      }
      this.editDialogVisible = true
      this.$nextTick(() => {
        this.$refs.editFormRef && this.$refs.editFormRef.clearValidate()
      })
    },

    handleDelete(row) {
      this.$confirm('确认删除该执行监控记录？删除后不可恢复。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteExecution(row.id)
          this.$message.success('删除成功')
          this.loadList()
          this.loadStats()
        } catch (error) {
          console.error('删除失败', error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    handleBatchDelete() {
      this.$confirm(`确认删除选中的 ${this.multipleSelection.length} 条记录？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          for (const item of this.multipleSelection) {
            await deleteExecution(item.id)
          }
          this.$message.success('批量删除成功')
          this.loadList()
          this.loadStats()
        } catch (error) {
          console.error('批量删除失败', error)
          this.$message.error('批量删除失败')
        }
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.editFormRef.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const submitData = {
            ...this.editForm,
            progressDeviation: this.editForm.actualProgress - this.editForm.planProgress
          }
          if (this.isEdit) {
            await updateExecution(submitData)
            this.$message.success('更新成功')
          } else {
            await addExecution(submitData)
            this.$message.success('新增成功')
          }
          this.editDialogVisible = false
          this.loadList()
          this.loadStats()
        } catch (error) {
          console.error('提交失败', error)
          this.$message.error(this.isEdit ? '更新失败' : '新增失败')
        } finally {
          this.submitLoading = false
        }
      })
    },
    handleView(row) {
      this.selectedItem = row
      this.detailDialogVisible = true
    },
    getPhaseColor(phase) {
      return { '启动阶段': 'primary', '实施阶段': 'success', '监控阶段': 'warning', '收尾阶段': 'info' }[phase] || 'info'
    },
    getProgressColor(p) {
      if (p >= 90) return '#67c23a'
      if (p >= 70) return '#e6a23c'
      return '#f56c6c'
    },
    getDeviationClass(d) {
      if (d > 10) return 'deviation-positive'
      if (d < -10) return 'deviation-negative'
      return 'deviation-normal'
    },
    getRiskCountType(c) {
      if (c === 0) return 'success'
      if (c <= 3) return 'warning'
      return 'danger'
    },
    getStatusType(s) {
      return { '正常执行': 'success', '延期执行': 'warning', '风险预警': 'danger', '暂停执行': 'info', '执行完成': 'primary' }[s] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.strategy-execution-monitoring {
  .stats-row {
    margin-bottom: 16px;
  }

  .mini-stat-card {
    background: white;
    border-radius: 8px;
    padding: 16px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    text-align: center;

    .stat-value {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
    }

    .stat-label {
      font-size: 13px;
      color: #909399;
      margin-top: 4px;
    }
  }

  .search-card, .action-card, .table-card {
    margin-bottom: 16px;
  }

  .budget-used {
    color: #E6A23C;
    font-weight: 500;
  }

  .deviation-positive {
    color: #67C23A;
    font-weight: 500;
  }

  .deviation-negative {
    color: #F56C6C;
    font-weight: 500;
  }

  .deviation-normal {
    color: #909399;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}
</style>