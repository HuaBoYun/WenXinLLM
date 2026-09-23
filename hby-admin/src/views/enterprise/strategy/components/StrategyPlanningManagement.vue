<template>
  <div class="strategy-planning-management" :style="themeVars">
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="mini-stat-card">
          <div class="stat-value">{{ statistics.total || 0 }}</div>
          <div class="stat-label">总计划数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="mini-stat-card">
          <div class="stat-value" :style="{ color: ipBright }">{{ statistics.executing || 0 }}</div>
          <div class="stat-label">执行中</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="mini-stat-card">
          <div class="stat-value" style="color: #67C23A;">{{ statistics.completed || 0 }}</div>
          <div class="stat-label">已完成</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="mini-stat-card">
          <div class="stat-value" style="color: #F56C6C;">{{ statistics.paused || 0 }}</div>
          <div class="stat-label">已暂停</div>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="计划编号" prop="planNo">
          <el-input v-model="queryForm.planNo" placeholder="请输入计划编号" clearable />
        </el-form-item>
        <el-form-item label="计划名称" prop="planName">
          <el-input v-model="queryForm.planName" placeholder="请输入计划名称" clearable />
        </el-form-item>
        <el-form-item label="计划类型" prop="planType">
          <el-select v-model="queryForm.planType" placeholder="请选择计划类型" clearable>
            <el-option label="年度战略" value="年度战略" />
            <el-option label="中期规划" value="中期规划" />
            <el-option label="长期愿景" value="长期愿景" />
            <el-option label="专项计划" value="专项计划" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择计划状态" clearable>
            <el-option label="草拟中" value="草拟中" />
            <el-option label="审核中" value="审核中" />
            <el-option label="已批准" value="已批准" />
            <el-option label="执行中" value="执行中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已暂停" value="已暂停" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="manager">
          <el-input v-model="queryForm.manager" placeholder="请输入负责人" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card shadow="never" class="action-card">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增计划</el-button>
          <el-button type="success" icon="el-icon-check" @click="handleBatchApprove" :disabled="multipleSelection.length === 0">批量审批</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出计划</el-button>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button type="danger" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        :data="planList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="planNo" label="计划编号" width="120" />
        <el-table-column prop="planName" label="计划名称" width="200" show-overflow-tooltip />
        <el-table-column prop="planType" label="计划类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTypeColor(scope.row.planType)">{{ scope.row.planType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="manager" label="负责人" width="100" />
        <el-table-column prop="department" label="负责部门" width="120" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="progress" label="完成进度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.progress || 0" :color="getProgressColor(scope.row.progress)" />
          </template>
        </el-table-column>
        <el-table-column prop="budget" label="预算金额" width="120">
          <template slot-scope="scope">
            <span class="budget-amount">{{ scope.row.budget }}万元</span>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPriorityType(scope.row.priority)" size="mini">{{ scope.row.priority }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelType(scope.row.riskLevel)" size="mini">{{ scope.row.riskLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="计划状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="completionDate" label="完成日期" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.completionDate">{{ scope.row.completionDate }}</span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
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
            <el-form-item label="计划名称" prop="planName">
              <el-input v-model="editForm.planName" placeholder="请输入计划名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划类型" prop="planType">
              <el-select v-model="editForm.planType" placeholder="请选择计划类型" style="width: 100%;">
                <el-option label="年度战略" value="年度战略" />
                <el-option label="中期规划" value="中期规划" />
                <el-option label="长期愿景" value="长期愿景" />
                <el-option label="专项计划" value="专项计划" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人" prop="manager">
              <el-input v-model="editForm.manager" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责部门" prop="department">
              <el-input v-model="editForm.department" placeholder="请输入负责部门" />
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
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker v-model="editForm.endDate" type="date" placeholder="请选择结束日期" value-format="yyyy-MM-dd" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算金额" prop="budget">
              <el-input-number v-model="editForm.budget" :min="0" :precision="2" placeholder="万元" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="editForm.priority" placeholder="请选择优先级" style="width: 100%;">
                <el-option label="高" value="高" />
                <el-option label="中" value="中" />
                <el-option label="低" value="低" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="editForm.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
                <el-option label="高" value="高" />
                <el-option label="中" value="中" />
                <el-option label="低" value="低" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划状态" prop="status">
              <el-select v-model="editForm.status" placeholder="请选择计划状态" style="width: 100%;">
                <el-option label="草拟中" value="草拟中" />
                <el-option label="审核中" value="审核中" />
                <el-option label="已批准" value="已批准" />
                <el-option label="执行中" value="执行中" />
                <el-option label="已完成" value="已完成" />
                <el-option label="已暂停" value="已暂停" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="isEdit">
          <el-col :span="24">
            <el-form-item label="完成进度" prop="progress">
              <el-slider v-model="editForm.progress" :min="0" :max="100" show-input />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="计划描述" prop="description">
              <el-input v-model="editForm.description" type="textarea" :rows="4" placeholder="请输入计划描述" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="计划详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border v-if="selectedPlan">
        <el-descriptions-item label="计划编号">{{ selectedPlan.planNo }}</el-descriptions-item>
        <el-descriptions-item label="计划名称">{{ selectedPlan.planName }}</el-descriptions-item>
        <el-descriptions-item label="计划类型">
          <el-tag :type="getTypeColor(selectedPlan.planType)">{{ selectedPlan.planType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="计划状态">
          <el-tag :type="getStatusType(selectedPlan.status)">{{ selectedPlan.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="负责人">{{ selectedPlan.manager }}</el-descriptions-item>
        <el-descriptions-item label="负责部门">{{ selectedPlan.department }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ selectedPlan.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ selectedPlan.endDate }}</el-descriptions-item>
        <el-descriptions-item label="预算金额">{{ selectedPlan.budget }}万元</el-descriptions-item>
        <el-descriptions-item label="完成进度">
          <el-progress :percentage="selectedPlan.progress || 0" :color="getProgressColor(selectedPlan.progress)" />
        </el-descriptions-item>
        <el-descriptions-item label="优先级">
          <el-tag :type="getPriorityType(selectedPlan.priority)" size="mini">{{ selectedPlan.priority }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="getRiskLevelType(selectedPlan.riskLevel)" size="mini">{{ selectedPlan.riskLevel }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="完成日期">{{ selectedPlan.completionDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ selectedPlan.createTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="计划描述" :span="2">{{ selectedPlan.description || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPlanningList, addPlanning, updatePlanning, deletePlanning, getPlanningStatistics, batchApprovePlanning } from '@/api/enterprise/strategy'
import request from '@/utils/request'
import { investThemeMixin } from '@/views/stateAssets/themeMixin'

export default {
  name: 'StrategyPlanningManagement',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      queryForm: {
        planNo: '',
        planName: '',
        planType: '',
        status: '',
        manager: ''
      },
      planList: [],
      multipleSelection: [],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      // 统计数据
      statistics: {
        total: 0,
        executing: 0,
        completed: 0,
        paused: 0
      },
      // 对话框
      detailDialogVisible: false,
      editDialogVisible: false,
      selectedPlan: null,
      isEdit: false,
      // 编辑表单
      editForm: {
        id: null,
        planName: '',
        planType: '',
        manager: '',
        department: '',
        startDate: '',
        endDate: '',
        budget: 0,
        priority: '',
        riskLevel: '',
        status: '草拟中',
        description: '',
        progress: 0
      },
      // 表单验证规则
      editRules: {
        planName: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
        planType: [{ required: true, message: '请选择计划类型', trigger: 'change' }],
        manager: [{ required: true, message: '请输入负责人', trigger: 'blur' }],
        department: [{ required: true, message: '请输入负责部门', trigger: 'blur' }],
        startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
        endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
        priority: [{ required: true, message: '请选择优先级', trigger: 'change' }],
        status: [{ required: true, message: '请选择计划状态', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑计划' : '新增计划'
    }
  },
  mounted() {
    this.loadPlanList()
    this.loadStatistics()
  },
  methods: {
    // 加载统计数据
    async loadStatistics() {
      try {
        const res = await getPlanningStatistics()
        if (res && res.result === 200 && res.data) {
          const data = res.data
          this.statistics.total = data.total || 0
          this.statistics.executing = data.executing || 0
          this.statistics.completed = data.completed || 0
          this.statistics.paused = data.paused || 0
        }
      } catch (error) {
        console.error('加载统计数据失败', error)
      }
    },

    // 加载计划列表
    async loadPlanList() {
      this.loading = true
      try {
        const res = await getPlanningList({
          ...this.queryForm,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        })
        if (res && res.result === 200 && res.data) {
          this.planList = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        } else {
          this.planList = []
          this.pagination.total = 0
        }
      } catch (error) {
        console.error('加载计划数据失败', error)
        this.$message.error('加载计划数据失败')
        this.planList = []
        this.pagination.total = 0
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadPlanList()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.pagination.currentPage = 1
      this.loadPlanList()
    },

    // 新增计划
    handleAdd() {
      this.isEdit = false
      this.resetEditForm()
      this.editDialogVisible = true
    },

    // 编辑计划
    handleEdit(row) {
      this.isEdit = true
      this.editForm = {
        id: row.id,
        planName: row.planName || '',
        planType: row.planType || '',
        manager: row.manager || '',
        department: row.department || '',
        startDate: row.startDate || '',
        endDate: row.endDate || '',
        budget: row.budget || 0,
        priority: row.priority || '',
        riskLevel: row.riskLevel || '',
        status: row.status || '草拟中',
        description: row.description || '',
        progress: row.progress || 0
      }
      this.editDialogVisible = true
    },

    // 查看详情
    handleView(row) {
      this.selectedPlan = { ...row }
      this.detailDialogVisible = true
    },

    // 删除计划
    handleDelete(row) {
      this.$confirm(`确定要删除计划「${row.planName}」吗？删除后不可恢复。`, '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deletePlanning(row.id)
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.loadPlanList()
            this.loadStatistics()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除失败', error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },

    // 提交表单
    submitForm() {
      this.$refs.editFormRef.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const formData = { ...this.editForm }
          let res
          if (this.isEdit) {
            res = await updatePlanning(formData)
          } else {
            delete formData.id
            delete formData.progress
            res = await addPlanning(formData)
          }
          if (res && res.result === 200) {
            this.$message.success(this.isEdit ? '编辑成功' : '新增成功')
            this.editDialogVisible = false
            this.loadPlanList()
            this.loadStatistics()
          } else {
            this.$message.error(res.msg || (this.isEdit ? '编辑失败' : '新增失败'))
          }
        } catch (error) {
          console.error('提交失败', error)
          this.$message.error('提交失败')
        } finally {
          this.submitLoading = false
        }
      })
    },

    // 重置编辑表单
    resetEditForm() {
      this.editForm = {
        id: null,
        planName: '',
        planType: '',
        manager: '',
        department: '',
        startDate: '',
        endDate: '',
        budget: 0,
        priority: '',
        riskLevel: '',
        status: '草拟中',
        description: '',
        progress: 0
      }
      this.$nextTick(() => {
        if (this.$refs.editFormRef) {
          this.$refs.editFormRef.clearValidate()
        }
      })
    },

    // 批量审批
    handleBatchApprove() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要审批的计划')
        return
      }
      this.$confirm(`确定要批量审批选中的 ${this.multipleSelection.length} 个计划吗？审批后状态将变为"已批准"。`, '批量审批确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.multipleSelection.map(item => item.id)
          const res = await batchApprovePlanning({ ids })
          if (res && res.result === 200) {
            this.$message.success('批量审批成功')
            this.loadPlanList()
            this.loadStatistics()
          } else {
            this.$message.error(res.msg || '批量审批失败')
          }
        } catch (error) {
          console.error('批量审批失败', error)
          this.$message.error('批量审批失败')
        }
      }).catch(() => {})
    },

    // 导出计划
    async handleExport() {
      try {
        const res = await request({
          url: '/monitor/v1/enterprise/strategy/planning/export',
          method: 'get',
          params: { ...this.queryForm },
          responseType: 'blob'
        })
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '战略规划计划.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
      } catch (error) {
        console.error('导出失败', error)
        this.$message.error('导出失败')
      }
    },

    // 批量删除
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的计划')
        return
      }
      this.$confirm(`确定要删除选中的 ${this.multipleSelection.length} 个计划吗？删除后不可恢复。`, '批量删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          for (const item of this.multipleSelection) {
            await deletePlanning(item.id)
          }
          this.$message.success('批量删除成功')
          this.loadPlanList()
          this.loadStatistics()
        } catch (error) {
          console.error('批量删除失败', error)
          this.$message.error('批量删除失败')
        }
      }).catch(() => {})
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.loadPlanList()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadPlanList()
    },

    // 获取计划类型颜色
    getTypeColor(type) {
      const typeMap = {
        '年度战略': 'primary',
        '中期规划': 'success',
        '长期愿景': 'warning',
        '专项计划': 'info'
      }
      return typeMap[type] || 'info'
    },

    // 获取进度颜色
    getProgressColor(progress) {
      if (progress >= 90) return '#67c23a'
      if (progress >= 70) return '#e6a23c'
      return '#f56c6c'
    },

    // 获取优先级类型
    getPriorityType(priority) {
      const priorityMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return priorityMap[priority] || 'info'
    },

    // 获取风险等级类型
    getRiskLevelType(level) {
      const levelMap = {
        '低': 'success',
        '中': 'warning',
        '高': 'danger'
      }
      return levelMap[level] || 'info'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '草拟中': 'info',
        '审核中': 'warning',
        '已批准': 'primary',
        '执行中': 'primary',
        '已完成': 'success',
        '已暂停': 'danger'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.strategy-planning-management {
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

  .text-right {
    text-align: right;
  }

  .text-muted {
    color: #999;
  }

  .budget-amount {
    color: #67C23A;
    font-weight: 500;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
