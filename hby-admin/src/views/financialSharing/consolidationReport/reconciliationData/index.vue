<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row v-if="showStatistics" :gutter="20" class="mb8">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="statistics-item">
            <div class="statistics-label">总数</div>
            <div class="statistics-value">{{ statistics.totalCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="statistics-item">
            <div class="statistics-label">待对账</div>
            <div class="statistics-value pending">{{ statistics.pendingCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="statistics-item">
            <div class="statistics-label">已对账</div>
            <div class="statistics-value matched">{{ statistics.matchedCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="statistics-item">
            <div class="statistics-label">有差异</div>
            <div class="statistics-value diff">{{ statistics.diffCount || 0 }}</div>
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
      <el-form-item label="甲方公司">
        <el-input v-model="queryForm.companyAName" placeholder="请输入甲方公司名称" clearable />
      </el-form-item>
      <el-form-item label="乙方公司">
        <el-input v-model="queryForm.companyBName" placeholder="请输入乙方公司名称" clearable />
      </el-form-item>
      <el-form-item label="交易类型">
        <el-select v-model="queryForm.transactionType" placeholder="请选择交易类型" clearable>
          <el-option label="应收应付" value="RECEIVABLE_PAYABLE" />
          <el-option label="收入成本" value="REVENUE_COST" />
          <el-option label="存货" value="INVENTORY" />
          <el-option label="投资" value="INVESTMENT" />
          <el-option label="其他" value="OTHER" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
          <el-option label="待对账" value="PENDING" />
          <el-option label="已对账" value="MATCHED" />
          <el-option label="有差异" value="DIFF" />
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
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-upload2" @click="handleImport">导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete">批量删除</el-button>
      </el-col>
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
      <el-table-column label="甲方金额" prop="amountA" width="120" align="right">
        <template slot-scope="scope">
          {{ scope.row.amountA | numberFormat }}
        </template>
      </el-table-column>
      <el-table-column label="乙方金额" prop="amountB" width="120" align="right">
        <template slot-scope="scope">
          {{ scope.row.amountB | numberFormat }}
        </template>
      </el-table-column>
      <el-table-column label="差异金额" prop="diffAmount" width="120" align="right">
        <template slot-scope="scope">
          <span :class="{'diff-amount': scope.row.diffAmount && scope.row.diffAmount != 0}">
            {{ scope.row.diffAmount | numberFormat }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="科目编码" prop="accountCode" width="120" />
      <el-table-column label="科目名称" prop="accountName" width="150" show-overflow-tooltip />
      <el-table-column label="摘要" prop="description" min-width="150" show-overflow-tooltip />
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'PENDING'" type="info" size="mini">待对账</el-tag>
          <el-tag v-else-if="scope.row.status === 'MATCHED'" type="success" size="mini">已对账</el-tag>
          <el-tag v-else type="danger" size="mini">有差异</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
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
  </div>
</template>

<script>
import { getReconciliationList, saveReconciliation, updateReconciliation, deleteReconciliation, deleteByModelIdAndPeriod, getStatistics } from '@/api/financialSharing/consolidationReport/reconciliationData'
import { getModelList } from '@/api/financialSharing/consolidationReport/consolidationModel'
import Pagination from '@/components/Pagination'

export default {
  name: 'ReconciliationData',
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
      // 查询参数
      queryForm: {
        modelId: '',
        period: '',
        companyAName: '',
        companyBName: '',
        transactionType: '',
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
      showStatistics: false
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
        companyAName: '',
        companyBName: '',
        transactionType: '',
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
      getReconciliationList(this.queryForm).then(res => {
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
    /** 新增按钮 */
    handleAdd() {
      this.$message.info('新增功能待实现')
    },
    /** 编辑按钮 */
    handleEdit(row) {
      this.$message.info('编辑功能待实现')
    },
    /** 删除按钮 */
    handleDelete(row) {
      this.$confirm('确认删除该对账数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteReconciliation({ reconciliationId: row.reconciliationId }).then(res => {
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
    /** 批量删除按钮 */
    handleBatchDelete() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }
      if (!this.queryForm.period) {
        this.$message.warning('请先输入期间')
        return
      }

      this.$confirm('确认删除该模型和期间的所有对账数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteByModelIdAndPeriod({
          modelId: this.queryForm.modelId,
          period: this.queryForm.period
        }).then(res => {
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
    /** 导入按钮 */
    handleImport() {
      this.$message.info('导入功能待实现')
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

.statistics-value.matched {
  color: #67c23a;
}

.statistics-value.diff {
  color: #f56c6c;
}

.diff-amount {
  color: #f56c6c;
  font-weight: bold;
}
</style>


