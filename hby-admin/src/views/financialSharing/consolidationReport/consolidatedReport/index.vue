<template>
  <div class="app-container">
    <!-- 生成参数表单 -->\n    <el-card class="mb8">
      <div slot="header">
        <span>合并报表生成</span>
      </div>
      <el-form :model="generateForm" :inline="true" label-width="100px">
        <el-form-item label="合并模型">
          <el-select v-model="generateForm.modelId" placeholder="请选择合并模型" style="width: 200px">
            <el-option
              v-for="item in modelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="期间">
          <el-input v-model="generateForm.period" placeholder="请输入期间(如202401)" style="width: 200px" />
        </el-form-item>
        <el-form-item label="报表类型">
          <el-select v-model="generateForm.reportType" placeholder="请选择报表类型(可选)" clearable style="width: 200px">
            <el-option label="资产负债表" value="BALANCE_SHEET" />
            <el-option label="利润表" value="INCOME_STATEMENT" />
            <el-option label="现金流量表" value="CASH_FLOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="重新生成">
          <el-switch v-model="generateForm.regenerate" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-s-operation" :loading="generating" @click="handleGenerate">
            生成报表
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 查询表单 -->
    <el-form :model="queryForm" :inline="true" label-width="100px">
      <el-form-item label="合并模型">
        <el-select v-model="queryForm.modelId" placeholder="请选择合并模型" clearable>
          <el-option
            v-for="item in modelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="期间">
        <el-input v-model="queryForm.period" placeholder="请输入期间" clearable />
      </el-form-item>
      <el-form-item label="报表类型">
        <el-select v-model="queryForm.reportType" placeholder="请选择报表类型" clearable>
          <el-option label="资产负债表" value="BALANCE_SHEET" />
          <el-option label="利润表" value="INCOME_STATEMENT" />
          <el-option label="现金流量表" value="CASH_FLOW" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
          <el-option label="草稿" value="DRAFT" />
          <el-option label="已确认" value="CONFIRMED" />
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
        <el-button type="success" icon="el-icon-check" @click="handleConfirm">确认报表</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" @click="handleDelete">删除报表</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" @click="handleExport">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border :tree-props="{children: 'children'}" row-key="itemCode">
      <el-table-column label="项目编码" prop="itemCode" width="120" />
      <el-table-column label="项目名称" prop="itemName" width="200" />
      <el-table-column label="母公司金额" prop="parentAmount" width="150" align="right">
        <template slot-scope="scope">
          {{ scope.row.parentAmount | numberFormat }}
        </template>
      </el-table-column>
      <el-table-column label="子公司金额" prop="subsidiaryAmount" width="150" align="right">
        <template slot-scope="scope">
          {{ scope.row.subsidiaryAmount | numberFormat }}
        </template>
      </el-table-column>
      <el-table-column label="抵消金额" prop="eliminationAmount" width="150" align="right">
        <template slot-scope="scope">
          <span :class="{'elimination-amount': scope.row.eliminationAmount && scope.row.eliminationAmount != 0}">
            {{ scope.row.eliminationAmount | numberFormat }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="合并金额" prop="consolidatedAmount" width="150" align="right">
        <template slot-scope="scope">
          <span class="consolidated-amount">{{ scope.row.consolidatedAmount | numberFormat }}</span>
        </template>
      </el-table-column>
      <el-table-column label="报表类型" prop="reportType" width="120" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.reportType === 'BALANCE_SHEET'" type="primary" size="mini">资产负债表</el-tag>
          <el-tag v-else-if="scope.row.reportType === 'INCOME_STATEMENT'" type="success" size="mini">利润表</el-tag>
          <el-tag v-else-if="scope.row.reportType === 'CASH_FLOW'" type="warning" size="mini">现金流量表</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'DRAFT'" type="info" size="mini">草稿</el-tag>
          <el-tag v-else type="success" size="mini">已确认</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="160" />
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
import { getReportList, generateReport, deleteReport, confirmReport } from '@/api/financialSharing/consolidationReport/consolidatedReport'
import { getModelList } from '@/api/financialSharing/consolidationReport/consolidationModel'
import Pagination from '@/components/Pagination'

export default {
  name: 'ConsolidatedReport',
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
      // 生成参数
      generateForm: {
        modelId: '',
        period: '',
        reportType: '',
        regenerate: false
      },
      // 生成中状态
      generating: false,
      // 查询参数
      queryForm: {
        modelId: '',
        period: '',
        reportType: '',
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
      modelOptions: []
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
    /** 生成报表 */
    handleGenerate() {
      if (!this.generateForm.modelId) {
        this.$message.warning('请选择合并模型')
        return
      }
      if (!this.generateForm.period) {
        this.$message.warning('请输入期间')
        return
      }

      const confirmMsg = this.generateForm.regenerate
        ? '确认重新生成合并报表吗?将删除该模型和期间的所有旧报表'
        : '确认生成合并报表吗?'

      this.$confirm(confirmMsg, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.generating = true
        generateReport(this.generateForm).then(res => {
          this.generating = false
          if (res.code === 200) {
            this.$message.success(res.msg || '生成成功')
            // 自动查询生成的报表
            this.queryForm.modelId = this.generateForm.modelId
            this.queryForm.period = this.generateForm.period
            this.queryForm.reportType = this.generateForm.reportType
            this.loadData()
          } else {
            this.$message.error(res.msg || '生成失败')
          }
        }).catch(() => {
          this.generating = false
          this.$message.error('生成失败')
        })
      }).catch(() => {})
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
        reportType: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      }
      this.tableData = []
      this.total = 0
    },
    /** 加载数据 */
    loadData() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getReportList(this.queryForm).then(res => {
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
    /** 确认报表 */
    handleConfirm() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }
      if (!this.queryForm.period) {
        this.$message.warning('请先输入期间')
        return
      }

      this.$confirm('确认该模型和期间的合并报表吗?确认后不可修改', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        confirmReport({
          modelId: this.queryForm.modelId,
          period: this.queryForm.period,
          reportType: this.queryForm.reportType
        }).then(res => {
          if (res.code === 200) {
            this.$message.success('确认成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '确认失败')
          }
        })
      }).catch(() => {})
    },
    /** 删除报表 */
    handleDelete() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }
      if (!this.queryForm.period) {
        this.$message.warning('请先输入期间')
        return
      }

      this.$confirm('确认删除该模型和期间的合并报表吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteReport({
          modelId: this.queryForm.modelId,
          period: this.queryForm.period,
          reportType: this.queryForm.reportType
        }).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    /** 导出按钮 */
    handleExport() {
      this.$message.info('导出功能待实现')
    },
    /** 刷新按钮 */
    handleRefresh() {
      this.loadData()
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}

.elimination-amount {
  color: #e6a23c;
  font-weight: bold;
}

.consolidated-amount {
  color: #409eff;
  font-weight: bold;
}
</style>


