<template>
  <div class="app-container">
    <!-- 自动创建区域 -->
    <el-card class="mb8">
      <div slot="header">
        <span>自动创建差异分析</span>
      </div>
      <el-form :model="createForm" :inline="true" label-width="100px">
        <el-form-item label="合并模型">
          <el-select v-model="createForm.modelId" placeholder="请选择合并模型" style="width: 200px">
            <el-option
              v-for="item in modelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="期间">
          <el-input v-model="createForm.period" placeholder="请输入期间(如202401)" style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-s-operation" :loading="creating" @click="handleAutoCreate">
            自动创建
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计卡片 -->
    <el-row v-if="showStatistics" :gutter="20" class="mb8">
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="statistics-item">
            <div class="statistics-label">总数</div>
            <div class="statistics-value">{{ statistics.totalCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="statistics-item">
            <div class="statistics-label">待处理</div>
            <div class="statistics-value pending">{{ statistics.pendingCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="statistics-item">
            <div class="statistics-label">处理中</div>
            <div class="statistics-value processing">{{ statistics.processingCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="statistics-item">
            <div class="statistics-label">已处理</div>
            <div class="statistics-value processed">{{ statistics.processedCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="statistics-item">
            <div class="statistics-label">已关闭</div>
            <div class="statistics-value closed">{{ statistics.closedCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-form :model="queryForm" :inline="true" label-width="100px">
      <el-form-item label="合并模型">
        <el-select v-model="queryForm.modelId" placeholder="请选择合并模型" clearable @change="handleModelChange">
          <el-option
            v-for="item in modelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="期间">
        <el-input v-model="queryForm.period" placeholder="请输入期间" clearable @change="handlePeriodChange" />
      </el-form-item>
      <el-form-item label="差异原因">
        <el-select v-model="queryForm.diffReason" placeholder="请选择差异原因" clearable>
          <el-option label="时间性差异" value="TIMING" />
          <el-option label="金额差异" value="AMOUNT" />
          <el-option label="科目差异" value="ACCOUNT" />
          <el-option label="其他" value="OTHER" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
          <el-option label="待处理" value="PENDING" />
          <el-option label="处理中" value="PROCESSING" />
          <el-option label="已处理" value="PROCESSED" />
          <el-option label="已关闭" value="CLOSED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="info" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column label="期间" prop="period" width="100" />
      <el-table-column label="甲方公司" prop="companyAName" width="150" show-overflow-tooltip />
      <el-table-column label="乙方公司" prop="companyBName" width="150" show-overflow-tooltip />
      <el-table-column label="交易类型" prop="transactionType" width="120" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.transactionType === 'RECEIVABLE_PAYABLE'" type="primary" size="mini">应收应付</el-tag>
          <el-tag v-else-if="scope.row.transactionType === 'REVENUE_COST'" type="success" size="mini">收入成本</el-tag>
          <el-tag v-else-if="scope.row.transactionType === 'INVENTORY'" type="warning" size="mini">存货</el-tag>
          <el-tag v-else-if="scope.row.transactionType === 'INVESTMENT'" type="info" size="mini">投资</el-tag>
          <el-tag v-else size="mini">其他</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="差异金额" prop="diffAmount" width="120" align="right">
        <template slot-scope="scope">
          <span class="diff-amount">{{ scope.row.diffAmount | numberFormat }}</span>
        </template>
      </el-table-column>
      <el-table-column label="差异原因" prop="diffReason" width="120" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.diffReason === 'TIMING'" type="info" size="mini">时间性差异</el-tag>
          <el-tag v-else-if="scope.row.diffReason === 'AMOUNT'" type="warning" size="mini">金额差异</el-tag>
          <el-tag v-else-if="scope.row.diffReason === 'ACCOUNT'" type="danger" size="mini">科目差异</el-tag>
          <el-tag v-else size="mini">其他</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="差异描述" prop="diffDescription" min-width="200" show-overflow-tooltip />
      <el-table-column label="处理方案" prop="solution" width="150" show-overflow-tooltip />
      <el-table-column label="调整金额" prop="adjustAmount" width="120" align="right">
        <template slot-scope="scope">
          {{ scope.row.adjustAmount | numberFormat }}
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'PENDING'" type="info" size="mini">待处理</el-tag>
          <el-tag v-else-if="scope.row.status === 'PROCESSING'" type="warning" size="mini">处理中</el-tag>
          <el-tag v-else-if="scope.row.status === 'PROCESSED'" type="success" size="mini">已处理</el-tag>
          <el-tag v-else type="info" size="mini">已关闭</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="处理人" prop="handlerName" width="100" />
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status === 'PENDING' || scope.row.status === 'PROCESSING'" type="text" icon="el-icon-check" @click="handleProcess(scope.row)">处理</el-button>
          <el-button v-if="scope.row.status === 'PROCESSED'" type="text" icon="el-icon-close" @click="handleClose(scope.row)">关闭</el-button>
          <el-button type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNum"
      :limit.sync="queryForm.pageSize"
      @pagination="loadData"
    />

    <!-- 处理对话框 -->
    <el-dialog title="处理差异" :visible.sync="processDialogVisible" width="600px">
      <el-form :model="processForm" label-width="100px">
        <el-form-item label="差异描述">
          <el-input v-model="processForm.diffDescription" type="textarea" :rows="3" disabled />
        </el-form-item>
        <el-form-item label="处理方案">
          <el-input v-model="processForm.solution" type="textarea" :rows="4" placeholder="请输入处理方案" />
        </el-form-item>
        <el-form-item label="调整金额">
          <el-input-number v-model="processForm.adjustAmount" :precision="2" :step="0.01" placeholder="请输入调整金额" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="processDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitProcess">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAnalysisList, handleDifference, closeDifference, deleteAnalysis, getStatistics, autoCreateAnalysis } from '@/api/financialSharing/consolidationReport/reconciliationAnalysis'
import { getModelList } from '@/api/financialSharing/consolidationReport/consolidationModel'
import Pagination from '@/components/Pagination'

export default {
  name: 'ReconciliationAnalysis',
  components: {
    Pagination
  },
  filters: {
    numberFormat(value) {
      if (!value) return '0.00'
      return parseFloat(value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    }
  },
  data() {
    return {
      // 创建参数
      createForm: {
        modelId: '',
        period: ''
      },
      // 创建中状态
      creating: false,
      // 查询参数
      queryForm: {
        modelId: '',
        period: '',
        diffReason: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      },
      // 加载状态
      loading: false,
      // 表格数据
      tableData: [],
      // 总记录数
      total: 0,
      // 模型选项
      modelOptions: [],
      // 统计数据
      statistics: {},
      // 是否显示统计
      showStatistics: false,
      // 处理对话框
      processDialogVisible: false,
      // 处理表单
      processForm: {
        analysisId: '',
        diffDescription: '',
        solution: '',
        adjustAmount: null
      }
    }
  },
  created() {
    this.loadModelOptions()
  },
  methods: {
    /** 加载模型选项 */
    loadModelOptions() {
      getModelList({ status: 'ACTIVE', pageNum: 1, pageSize: 1000 }).then(res => {
        if (res.code === 200 && res.data && res.data.list) {
          this.modelOptions = res.data.list.map(item => ({
            value: item.modelId,
            label: item.modelName
          }))
        }
      })
    },
    /** 自动创建 */
    handleAutoCreate() {
      if (!this.createForm.modelId) {
        this.$message.warning('请选择合并模型')
        return
      }
      if (!this.createForm.period) {
        this.$message.warning('请输入期间')
        return
      }

      this.$confirm('确认自动创建差异分析吗?将从有差异的对账数据中自动创建', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.creating = true
        autoCreateAnalysis(this.createForm).then(res => {
          this.creating = false
          if (res.code === 200) {
            this.$message.success(res.msg || '创建成功')
            // 自动查询创建的数据
            this.queryForm.modelId = this.createForm.modelId
            this.queryForm.period = this.createForm.period
            this.loadData()
            this.loadStatistics()
          } else {
            this.$message.error(res.msg || '创建失败')
          }
        }).catch(() => {
          this.creating = false
          this.$message.error('创建失败')
        })
      }).catch(() => {})
    },
    /** 模型变化 */
    handleModelChange() {
      this.loadStatistics()
    },
    /** 期间变化 */
    handlePeriodChange() {
      this.loadStatistics()
    },
    /** 加载统计数据 */
    loadStatistics() {
      if (this.queryForm.modelId && this.queryForm.period) {
        getStatistics({
          modelId: this.queryForm.modelId,
          period: this.queryForm.period
        }).then(res => {
          if (res.code === 200) {
            this.statistics = res.data
            this.showStatistics = true
          }
        })
      } else {
        this.showStatistics = false
      }
    },
    /** 查询按钮 */
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },
    /** 重置按钮 */
    resetQuery() {
      this.queryForm = {
        modelId: '',
        period: '',
        diffReason: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      }
      this.tableData = []
      this.total = 0
      this.showStatistics = false
    },
    /** 加载数据 */
    loadData() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getAnalysisList(this.queryForm).then(res => {
        this.loading = false
        if (res.code === 1 || res.code === 200) {
          this.tableData = res.data.list || res.data.records || []
          this.total = res.data.total || res.data.totalRecord || 0
        } else {
          this.$message.error(res.msg || '查询失败')
          this.tableData = []
          this.total = 0
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.tableData = []
        this.total = 0
      })
    },
    /** 处理按钮 */
    handleProcess(row) {
      this.processForm = {
        analysisId: row.analysisId,
        diffDescription: row.diffDescription,
        solution: row.solution || '',
        adjustAmount: row.adjustAmount || null
      }
      this.processDialogVisible = true
    },
    /** 提交处理 */
    submitProcess() {
      if (!this.processForm.solution) {
        this.$message.warning('请输入处理方案')
        return
      }

      handleDifference({
        analysisId: this.processForm.analysisId,
        solution: this.processForm.solution,
        adjustAmount: this.processForm.adjustAmount
      }).then(res => {
        if (res.code === 200) {
          this.$message.success('处理成功')
          this.processDialogVisible = false
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(res.msg || '处理失败')
        }
      })
    },
    /** 关闭按钮 */
    handleClose(row) {
      this.$confirm('确认关闭该差异分析吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        closeDifference({ analysisId: row.analysisId }).then(res => {
          if (res.code === 200) {
            this.$message.success('关闭成功')
            this.loadData()
            this.loadStatistics()
          } else {
            this.$message.error(res.msg || '关闭失败')
          }
        })
      }).catch(() => {})
    },
    /** 删除按钮 */
    handleDelete(row) {
      this.$confirm('确认删除该差异分析吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAnalysis({ analysisId: row.analysisId }).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.loadData()
            this.loadStatistics()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    /** 刷新按钮 */
    handleRefresh() {
      this.loadData()
      this.loadStatistics()
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}

.statistics-item {
  text-align: center;
}

.statistics-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.statistics-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.statistics-value.pending {
  color: #909399;
}

.statistics-value.processing {
  color: #e6a23c;
}

.statistics-value.processed {
  color: #67c23a;
}

.statistics-value.closed {
  color: #909399;
}

.diff-amount {
  color: #f56c6c;
  font-weight: bold;
}
</style>


