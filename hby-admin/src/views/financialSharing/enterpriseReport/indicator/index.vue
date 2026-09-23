<template>
  <div class="indicator-container">
    <!-- 查询表单 -->
    <el-form :model="queryForm" :inline="true" class="query-form">
      <el-form-item label="指标编码">
        <el-input v-model="queryForm.indicatorCode" placeholder="请输入指标编码" clearable />
      </el-form-item>
      <el-form-item label="指标名称">
        <el-input v-model="queryForm.indicatorName" placeholder="请输入指标名称" clearable />
      </el-form-item>
      <el-form-item label="指标类型">
        <el-select v-model="queryForm.indicatorType" placeholder="请选择指标类型" clearable>
          <el-option label="货币" value="CURRENCY" />
          <el-option label="数量" value="QUANTITY" />
          <el-option label="价格" value="PRICE" />
          <el-option label="百分比" value="PERCENT" />
          <el-option label="文本" value="TEXT" />
          <el-option label="枚举" value="ENUM" />
          <el-option label="参照" value="REFERENCE" />
          <el-option label="日期" value="DATE" />
          <el-option label="长文本" value="LONGTEXT" />
          <el-option label="附件" value="ATTACHMENT" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
          <el-option label="启用" value="ACTIVE" />
          <el-option label="停用" value="INACTIVE" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="selectedRows.length === 0">批量删除</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      :data="tableData"
      border
      stripe
      @selection-change="handleSelectionChange"
      v-loading="loading"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="indicatorCode" label="指标编码" width="150" />
      <el-table-column prop="indicatorName" label="指标名称" width="200" />
      <el-table-column prop="indicatorType" label="指标类型" width="120">
        <template slot-scope="scope">
          {{ getIndicatorTypeLabel(scope.row.indicatorType) }}
        </template>
      </el-table-column>
      <el-table-column prop="aggregateType" label="汇总属性" width="120">
        <template slot-scope="scope">
          {{ getAggregateTypeLabel(scope.row.aggregateType) }}
        </template>
      </el-table-column>
      <el-table-column prop="dataLength" label="数据长度" width="100" align="center" />
      <el-table-column prop="dataPrecision" label="数据精度" width="100" align="center" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
            {{ scope.row.status === 'ACTIVE' ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleView(scope.row)">查看</el-button>
          <el-button type="text" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="mini" @click="handleDelete(scope.row)">删除</el-button>
          <el-button 
            type="text" 
            size="mini" 
            @click="handleToggleStatus(scope.row)"
          >
            {{ scope.row.status === 'ACTIVE' ? '停用' : '启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.pageNumber"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px; text-align: right"
    />

    <!-- 指标表单对话框 -->
    <indicator-form
      v-if="formVisible"
      :visible.sync="formVisible"
      :form-type="formType"
      :form-data="formData"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script>
import { 
  getIndicatorPage, 
  deleteIndicator, 
  batchDeleteIndicator,
  updateIndicatorStatus 
} from '@/api/financialSharing/enterpriseReport/indicator'
import IndicatorForm from './components/IndicatorForm.vue'

export default {
  name: 'Indicator',
  components: {
    IndicatorForm
  },
  data() {
    return {
      loading: false,
      queryForm: {
        indicatorCode: '',
        indicatorName: '',
        indicatorType: '',
        status: ''
      },
      tableData: [],
      selectedRows: [],
      pagination: {
        pageNumber: 1,
        pageSize: 20,
        total: 0
      },
      formVisible: false,
      formType: 'add',
      formData: null
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 查询数据
    async fetchData() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      try {
        const params = {
          ...this.queryForm,
          pageNumber: this.pagination.pageNumber,
          pageSize: this.pagination.pageSize
        }
        const res = await getIndicatorPage(params)
        if (res.code === 1 || res.code === 200) {
          this.tableData = res.data.list || res.data.records || []
          this.pagination.total = res.data.total || res.data.totalRecord || 0
        } else {
          this.$message.error(res.msg || '查询失败')
          this.tableData = []
          this.pagination.total = 0
        }
      } catch (error) {
        console.error('查询失败:', error)
        this.tableData = []
        this.pagination.total = 0
      } finally {
        this.loading = false
      }
    },

    // 查询
    handleQuery() {
      this.pagination.pageNumber = 1
      this.fetchData()
    },

    // 重置
    handleReset() {
      this.queryForm = {
        indicatorCode: '',
        indicatorName: '',
        indicatorType: '',
        status: ''
      }
      this.handleQuery()
    },

    // 新增
    handleAdd() {
      this.formType = 'add'
      this.formData = null
      this.formVisible = true
    },

    // 查看
    handleView(row) {
      this.formType = 'view'
      this.formData = { ...row }
      this.formVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.formType = 'edit'
      this.formData = { ...row }
      this.formVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该指标吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteIndicator(row.indicatorId)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除指标失败:', error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },

    // 批量删除
    handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请先选择要删除的数据')
        return
      }
      this.$confirm(`确定要删除选中的${this.selectedRows.length}条数据吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const indicatorIds = this.selectedRows.map(row => row.indicatorId)
          const res = await batchDeleteIndicator(indicatorIds)
          if (res.code === 1) {
            this.$message.success('批量删除成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '批量删除失败')
          }
        } catch (error) {
          console.error('批量删除指标失败:', error)
          this.$message.error('批量删除失败')
        }
      }).catch(() => {})
    },

    // 切换状态
    handleToggleStatus(row) {
      const newStatus = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
      const statusText = newStatus === 'ACTIVE' ? '启用' : '停用'
      this.$confirm(`确定要${statusText}该指标吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await updateIndicatorStatus(row.indicatorId, newStatus)
          if (res.code === 1) {
            this.$message.success(`${statusText}成功`)
            this.fetchData()
          } else {
            this.$message.error(res.msg || `${statusText}失败`)
          }
        } catch (error) {
          console.error('更新指标状态失败:', error)
          this.$message.error(`${statusText}失败`)
        }
      }).catch(() => {})
    },

    // 表格选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 每页大小改变
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.fetchData()
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.pagination.pageNumber = val
      this.fetchData()
    },

    // 表单提交成功
    handleFormSuccess() {
      this.formVisible = false
      this.fetchData()
    },

    // 获取指标类型标签
    getIndicatorTypeLabel(type) {
      const typeMap = {
        'CURRENCY': '货币',
        'QUANTITY': '数量',
        'PRICE': '价格',
        'PERCENT': '百分比',
        'TEXT': '文本',
        'ENUM': '枚举',
        'REFERENCE': '参照',
        'DATE': '日期',
        'LONGTEXT': '长文本',
        'ATTACHMENT': '附件'
      }
      return typeMap[type] || type
    },

    // 获取汇总属性标签
    getAggregateTypeLabel(type) {
      const typeMap = {
        'SUM': '求和',
        'AVG': '平均',
        'MAX': '最大',
        'MIN': '最小',
        'NONE': '不汇总'
      }
      return typeMap[type] || type
    }
  }
}
</script>

<style scoped lang="scss">
.indicator-container {
  padding: 20px;

  .query-form {
    background: #fff;
    padding: 20px 20px 0;
    margin-bottom: 20px;
    border-radius: 4px;
  }

  .toolbar {
    background: #fff;
    padding: 15px 20px;
    margin-bottom: 20px;
    border-radius: 4px;
  }
}
</style>

