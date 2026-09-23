<template>
  <div class="strategy-performance-evaluation" :style="themeVars">
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="mini-stat-card">
          <div class="stat-value">{{ statistics.total || 0 }}</div>
          <div class="stat-label">总评估数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="mini-stat-card">
          <div class="stat-value" :style="{ color: ipBright }">{{ statistics.avgKpiScore || 0 }}</div>
          <div class="stat-label">平均KPI</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="mini-stat-card">
          <div class="stat-value" style="color: #67C23A;">{{ statistics.excellentCount || 0 }}</div>
          <div class="stat-label">优秀数量</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="mini-stat-card">
          <div class="stat-value" style="color: #E6A23C;">{{ statistics.pendingCount || 0 }}</div>
          <div class="stat-label">待评估</div>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="评估编号" prop="evaluationNo">
          <el-input v-model="queryForm.evaluationNo" placeholder="请输入评估编号" clearable />
        </el-form-item>
        <el-form-item label="战略计划" prop="planName">
          <el-input v-model="queryForm.planName" placeholder="请输入战略计划" clearable />
        </el-form-item>
        <el-form-item label="评估类型" prop="evaluationType">
          <el-select v-model="queryForm.evaluationType" placeholder="请选择评估类型" clearable>
            <el-option label="季度评估" value="季度评估" />
            <el-option label="半年评估" value="半年评估" />
            <el-option label="年度评估" value="年度评估" />
            <el-option label="专项评估" value="专项评估" />
          </el-select>
        </el-form-item>
        <el-form-item label="评估状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择评估状态" clearable>
            <el-option label="待评估" value="待评估" />
            <el-option label="评估中" value="评估中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="需复评" value="需复评" />
          </el-select>
        </el-form-item>
        <el-form-item label="评估人" prop="evaluator">
          <el-input v-model="queryForm.evaluator" placeholder="请输入评估人" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card shadow="never" class="action-card">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增评估</el-button>
      <el-button type="danger" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        :data="evaluationList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="evaluationNo" label="评估编号" width="120" />
        <el-table-column prop="planName" label="战略计划" width="180" show-overflow-tooltip />
        <el-table-column prop="evaluationType" label="评估类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTypeColor(scope.row.evaluationType)">{{ scope.row.evaluationType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="evaluator" label="评估人" width="100" />
        <el-table-column prop="evaluationDate" label="评估日期" width="120" />
        <el-table-column prop="targetCompletion" label="目标完成率" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.targetCompletion || 0" :color="getCompletionColor(scope.row.targetCompletion)" />
          </template>
        </el-table-column>
        <el-table-column prop="kpiScore" label="KPI得分" width="100">
          <template slot-scope="scope">
            <span :class="getScoreClass(scope.row.kpiScore)">{{ scope.row.kpiScore }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="financialPerformance" label="财务绩效" width="140">
          <template slot-scope="scope">
            <el-rate :value="scope.row.financialPerformance" disabled show-score />
          </template>
        </el-table-column>
        <el-table-column prop="operationalEfficiency" label="运营效率" width="140">
          <template slot-scope="scope">
            <el-rate :value="scope.row.operationalEfficiency" disabled show-score />
          </template>
        </el-table-column>
        <el-table-column prop="customerSatisfaction" label="客户满意度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.customerSatisfaction || 0" :color="ipBright" />
          </template>
        </el-table-column>
        <el-table-column prop="innovationIndex" label="创新指数" width="100">
          <template slot-scope="scope">
            <span class="innovation-index">{{ scope.row.innovationIndex }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="overallRating" label="综合评级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRatingType(scope.row.overallRating)">{{ scope.row.overallRating }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="improvementSuggestions" label="改进建议" width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="评估状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="warning" @click="handleEvaluate(scope.row)" :disabled="scope.row.status === '已完成'">评估</el-button>
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
    <el-dialog
      :title="dialogType === 'add' ? '新增绩效评估' : '编辑绩效评估'"
      :visible.sync="editDialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form :model="editForm" :rules="editRules" ref="editForm" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="战略计划" prop="planName">
              <el-select v-model="editForm.planName" placeholder="请选择战略计划" filterable style="width: 100%;">
                <el-option v-for="item in planOptions" :key="item.id" :label="item.planName" :value="item.planName" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估类型" prop="evaluationType">
              <el-select v-model="editForm.evaluationType" placeholder="请选择评估类型" style="width: 100%;">
                <el-option label="季度评估" value="季度评估" />
                <el-option label="半年评估" value="半年评估" />
                <el-option label="年度评估" value="年度评估" />
                <el-option label="专项评估" value="专项评估" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="评估人" prop="evaluator">
              <el-input v-model="editForm.evaluator" placeholder="请输入评估人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估日期" prop="evaluationDate">
              <el-date-picker
                v-model="editForm.evaluationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择评估日期"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="目标完成率" prop="targetCompletion">
              <el-slider v-model="editForm.targetCompletion" :max="100" show-input />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="KPI得分" prop="kpiScore">
              <el-input-number v-model="editForm.kpiScore" :min="0" :max="100" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="财务绩效" prop="financialPerformance">
              <el-rate v-model="editForm.financialPerformance" show-score />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="运营效率" prop="operationalEfficiency">
              <el-rate v-model="editForm.operationalEfficiency" show-score />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="客户满意度" prop="customerSatisfaction">
              <el-slider v-model="editForm.customerSatisfaction" :max="100" show-input />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创新指数" prop="innovationIndex">
              <el-input-number v-model="editForm.innovationIndex" :min="0" :max="100" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="综合评级" prop="overallRating">
              <el-select v-model="editForm.overallRating" placeholder="请选择综合评级" style="width: 100%;">
                <el-option label="优秀" value="优秀" />
                <el-option label="良好" value="良好" />
                <el-option label="一般" value="一般" />
                <el-option label="较差" value="较差" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估状态" prop="status">
              <el-select v-model="editForm.status" placeholder="请选择评估状态" style="width: 100%;">
                <el-option label="待评估" value="待评估" />
                <el-option label="评估中" value="评估中" />
                <el-option label="已完成" value="已完成" />
                <el-option label="需复评" value="需复评" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="改进建议" prop="improvementSuggestions">
          <el-input
            v-model="editForm.improvementSuggestions"
            type="textarea"
            :rows="3"
            placeholder="请输入改进建议"
          />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="绩效评估详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border v-if="selectedEvaluation">
        <el-descriptions-item label="评估编号">{{ selectedEvaluation.evaluationNo }}</el-descriptions-item>
        <el-descriptions-item label="战略计划">{{ selectedEvaluation.planName }}</el-descriptions-item>
        <el-descriptions-item label="评估类型">
          <el-tag :type="getTypeColor(selectedEvaluation.evaluationType)">{{ selectedEvaluation.evaluationType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="评估人">{{ selectedEvaluation.evaluator }}</el-descriptions-item>
        <el-descriptions-item label="评估日期">{{ selectedEvaluation.evaluationDate }}</el-descriptions-item>
        <el-descriptions-item label="目标完成率">
          <el-progress :percentage="selectedEvaluation.targetCompletion || 0" :color="getCompletionColor(selectedEvaluation.targetCompletion)" />
        </el-descriptions-item>
        <el-descriptions-item label="KPI得分">
          <span :class="getScoreClass(selectedEvaluation.kpiScore)">{{ selectedEvaluation.kpiScore }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="财务绩效">
          <el-rate :value="selectedEvaluation.financialPerformance" disabled show-score />
        </el-descriptions-item>
        <el-descriptions-item label="运营效率">
          <el-rate :value="selectedEvaluation.operationalEfficiency" disabled show-score />
        </el-descriptions-item>
        <el-descriptions-item label="客户满意度">
          <el-progress :percentage="selectedEvaluation.customerSatisfaction || 0" :color="ipBright" />
        </el-descriptions-item>
        <el-descriptions-item label="创新指数">
          <span class="innovation-index">{{ selectedEvaluation.innovationIndex }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="综合评级">
          <el-tag :type="getRatingType(selectedEvaluation.overallRating)">{{ selectedEvaluation.overallRating }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="评估状态">
          <el-tag :type="getStatusType(selectedEvaluation.status)">{{ selectedEvaluation.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="改进建议" :span="2">{{ selectedEvaluation.improvementSuggestions }}</el-descriptions-item>
      </el-descriptions>
      <span slot="footer">
        <el-button @click="detailDialogVisible = false">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getPerformanceList,
  addPerformance,
  updatePerformance,
  deletePerformance,
  getPerformanceStatistics,
  getPlanningAll,
  evaluatePerformance
} from '@/api/enterprise/strategy'
import { investThemeMixin } from '@/views/stateAssets/themeMixin'

export default {
  name: 'StrategyPerformanceEvaluation',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      queryForm: {
        evaluationNo: '',
        planName: '',
        evaluationType: '',
        status: '',
        evaluator: ''
      },
      evaluationList: [],
      multipleSelection: [],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      statistics: {
        total: 0,
        avgKpiScore: 0,
        excellentCount: 0,
        pendingCount: 0
      },
      detailDialogVisible: false,
      editDialogVisible: false,
      dialogType: 'add',
      selectedEvaluation: null,
      editForm: {
        planName: '',
        evaluationType: '',
        evaluator: '',
        evaluationDate: '',
        targetCompletion: 0,
        kpiScore: 0,
        financialPerformance: 0,
        operationalEfficiency: 0,
        customerSatisfaction: 0,
        innovationIndex: 0,
        overallRating: '',
        improvementSuggestions: '',
        status: ''
      },
      editRules: {
        planName: [{ required: true, message: '请输入战略计划名称', trigger: 'blur' }]
      },
      planOptions: []
    }
  },
  mounted() {
    this.loadEvaluationList()
    this.loadStatistics()
    this.loadPlanOptions()
  },
  methods: {
    // 加载评估列表
    async loadEvaluationList() {
      this.loading = true
      try {
        const res = await getPerformanceList({
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          evaluationNo: this.queryForm.evaluationNo,
          planName: this.queryForm.planName,
          evaluationType: this.queryForm.evaluationType,
          status: this.queryForm.status,
          evaluator: this.queryForm.evaluator
        })
        if (res && res.result === 200 && res.data) {
          this.evaluationList = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        } else {
          this.evaluationList = []
          this.pagination.total = 0
        }
      } catch (error) {
        console.error('加载绩效评估列表失败', error)
        this.$message.error('加载数据失败')
        this.evaluationList = []
        this.pagination.total = 0
      } finally {
        this.loading = false
      }
    },

    // 加载统计数据
    async loadStatistics() {
      try {
        const res = await getPerformanceStatistics()
        if (res && res.result === 200 && res.data) {
          this.statistics = res.data
        }
      } catch (error) {
        console.error('加载统计数据失败', error)
      }
    },

    // 加载战略规划选项
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

    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadEvaluationList()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.pagination.currentPage = 1
      this.loadEvaluationList()
    },

    // 新增评估
    handleAdd() {
      this.dialogType = 'add'
      this.resetEditForm()
      this.editDialogVisible = true
      this.$nextTick(() => {
        this.$refs.editForm && this.$refs.editForm.clearValidate()
      })
    },

    // 编辑
    handleEdit(row) {
      this.dialogType = 'edit'
      this.editForm = { ...row }
      this.editDialogVisible = true
      this.$nextTick(() => {
        this.$refs.editForm && this.$refs.editForm.clearValidate()
      })
    },

    // 查看详情
    handleView(row) {
      this.selectedEvaluation = row
      this.detailDialogVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该绩效评估记录？删除后不可恢复。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deletePerformance(row.id)
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.loadEvaluationList()
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

    // 执行评估
    handleEvaluate(row) {
      this.$confirm(`确定要对「${row.planName}」执行评估吗？`, '评估确认', {
        confirmButtonText: '确定评估',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const res = await evaluatePerformance({ id: row.id })
          if (res && res.result === 200) {
            this.$message.success('评估完成')
            this.loadEvaluationList()
            this.loadStatistics()
          } else {
            this.$message.error(res.msg || '评估失败')
          }
        } catch (error) {
          console.error('评估失败', error)
          this.$message.error('评估失败')
        }
      }).catch(() => {})
    },

    // 批量删除
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的记录')
        return
      }
      this.$confirm(`确认删除选中的 ${this.multipleSelection.length} 条记录？删除后不可恢复。`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          for (const item of this.multipleSelection) {
            await deletePerformance(item.id)
          }
          this.$message.success('批量删除成功')
          this.loadEvaluationList()
          this.loadStatistics()
        } catch (error) {
          console.error('批量删除失败', error)
          this.$message.error('批量删除失败')
        }
      }).catch(() => {})
    },

    // 提交表单
    handleSubmit() {
      this.$refs.editForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          let res
          if (this.dialogType === 'add') {
            res = await addPerformance(this.editForm)
          } else {
            res = await updatePerformance(this.editForm)
          }
          if (res && res.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '编辑成功')
            this.editDialogVisible = false
            this.loadEvaluationList()
            this.loadStatistics()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (error) {
          console.error('提交失败', error)
          this.$message.error('操作失败')
        } finally {
          this.submitLoading = false
        }
      })
    },

    // 重置编辑表单
    resetEditForm() {
      this.editForm = {
        planName: '',
        evaluationType: '',
        evaluator: '',
        evaluationDate: '',
        targetCompletion: 0,
        kpiScore: 0,
        financialPerformance: 0,
        operationalEfficiency: 0,
        customerSatisfaction: 0,
        innovationIndex: 0,
        overallRating: '',
        improvementSuggestions: '',
        status: ''
      }
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.loadEvaluationList()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadEvaluationList()
    },

    // 获取评估类型颜色
    getTypeColor(type) {
      const typeMap = {
        '季度评估': 'primary',
        '半年评估': 'success',
        '年度评估': 'warning',
        '专项评估': 'info'
      }
      return typeMap[type] || 'info'
    },

    // 获取完成率颜色
    getCompletionColor(completion) {
      if (completion >= 90) return '#67c23a'
      if (completion >= 70) return '#e6a23c'
      return '#f56c6c'
    },

    // 获取评分样式
    getScoreClass(score) {
      if (score >= 90) return 'score-excellent'
      if (score >= 80) return 'score-good'
      if (score >= 70) return 'score-normal'
      return 'score-poor'
    },

    // 获取评级类型
    getRatingType(rating) {
      const ratingMap = {
        '优秀': 'success',
        '良好': 'primary',
        '一般': 'warning',
        '较差': 'danger'
      }
      return ratingMap[rating] || 'info'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '待评估': 'info',
        '评估中': 'primary',
        '已完成': 'success',
        '需复评': 'warning'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.strategy-performance-evaluation {
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

  .score-excellent {
    color: #67C23A;
    font-weight: bold;
    font-size: 16px;
  }

  .score-good {
    color: var(--ip-bright);
    font-weight: 500;
  }

  .score-normal {
    color: #E6A23C;
  }

  .score-poor {
    color: #F56C6C;
  }

  .innovation-index {
    color: #9C27B0;
    font-weight: 500;
    font-size: 16px;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
