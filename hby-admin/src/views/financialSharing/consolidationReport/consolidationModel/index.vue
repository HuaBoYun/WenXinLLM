<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :model="queryForm" :inline="true" label-width="100px">
      <el-form-item label="模型编码">
        <el-input v-model="queryForm.modelCode" placeholder="请输入模型编码" clearable />
      </el-form-item>
      <el-form-item label="模型名称">
        <el-input v-model="queryForm.modelName" placeholder="请输入模型名称" clearable />
      </el-form-item>
      <el-form-item label="合并类型">
        <el-select v-model="queryForm.consolidationType" placeholder="请选择合并类型" clearable>
          <el-option label="完全合并" value="FULL" />
          <el-option label="比例合并" value="PROPORTIONAL" />
          <el-option label="权益法" value="EQUITY" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
          <el-option label="启用" value="ACTIVE" />
          <el-option label="停用" value="INACTIVE" />
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
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="modelList" border>
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column label="模型编码" prop="modelCode" width="150" />
      <el-table-column label="模型名称" prop="modelName" width="200" />
      <el-table-column label="合并类型" prop="consolidationType" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.consolidationType === 'FULL'" type="success">完全合并</el-tag>
          <el-tag v-else-if="scope.row.consolidationType === 'PROPORTIONAL'" type="warning">比例合并</el-tag>
          <el-tag v-else-if="scope.row.consolidationType === 'EQUITY'" type="info">权益法</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="母公司ID" prop="parentOrgId" width="150" />
      <el-table-column label="周期类型" prop="periodType" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.periodType === 'YEAR'">年</span>
          <span v-else-if="scope.row.periodType === 'HALF_YEAR'">半年</span>
          <span v-else-if="scope.row.periodType === 'QUARTER'">季度</span>
          <span v-else-if="scope.row.periodType === 'MONTH'">月</span>
        </template>
      </el-table-column>
      <el-table-column label="起始期间" prop="startPeriod" width="100" />
      <el-table-column label="终止期间" prop="endPeriod" width="100" />
      <el-table-column label="记账本位币" prop="currencyCode" width="120" />
      <el-table-column label="汇率类型" prop="exchangeRateType" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.exchangeRateType === 'SPOT'">即期</span>
          <span v-else-if="scope.row.exchangeRateType === 'AVERAGE'">平均</span>
          <span v-else-if="scope.row.exchangeRateType === 'FIXED'">固定</span>
        </template>
      </el-table-column>
      <el-table-column label="自动抵消" prop="isAutoElimination" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isAutoElimination === 'Y'" type="success">是</el-tag>
          <el-tag v-else type="info">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="80" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'ACTIVE'" type="success">启用</el-tag>
          <el-tag v-else type="info">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            v-if="scope.row.status === 'ACTIVE'"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleUpdateStatus(scope.row, 'INACTIVE')"
          >停用</el-button>
          <el-button
            v-else
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleUpdateStatus(scope.row, 'ACTIVE')"
          >启用</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNum"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 表单对话框 -->
    <consolidation-model-form
      v-if="formVisible"
      :visible.sync="formVisible"
      :model-id="currentModelId"
      :form-type="formType"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script>
import { getModelList, deleteModel, updateModelStatus } from '@/api/financialSharing/consolidationReport/consolidationModel'
import ConsolidationModelForm from './components/ConsolidationModelForm.vue'
import Pagination from '@/components/Pagination'

export default {
  name: 'ConsolidationModel',
  components: {
    ConsolidationModelForm,
    Pagination
  },
  data() {
    return {
      // 查询参数
      queryForm: {
        modelCode: '',
        modelName: '',
        consolidationType: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      },
      // 加载状态
      loading: false,
      // 数据列表
      modelList: [],
      // 总记录数
      total: 0,
      // 表单对话框
      formVisible: false,
      // 当前模型ID
      currentModelId: null,
      // 表单类型：add/edit/view
      formType: 'add'
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getModelList(this.queryForm).then(res => {
        this.loading = false
        if (res.code === 1 || res.code === 200) {
          this.modelList = res.data.list || res.data.records || []
          this.total = res.data.total || res.data.totalRecord || 0
        } else {
          this.$message.error(res.msg || '查询失败')
          this.modelList = []
          this.total = 0
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.modelList = []
        this.total = 0
      })
    },
    /** 查询按钮 */
    handleQuery() {
      this.queryForm.pageNum = 1
      this.getList()
    },
    /** 重置按钮 */
    resetQuery() {
      this.queryForm = {
        modelCode: '',
        modelName: '',
        consolidationType: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      }
      this.getList()
    },
    /** 新增按钮 */
    handleAdd() {
      this.currentModelId = null
      this.formType = 'add'
      this.formVisible = true
    },
    /** 查看按钮 */
    handleView(row) {
      this.currentModelId = row.modelId
      this.formType = 'view'
      this.formVisible = true
    },
    /** 编辑按钮 */
    handleEdit(row) {
      this.currentModelId = row.modelId
      this.formType = 'edit'
      this.formVisible = true
    },
    /** 删除按钮 */
    handleDelete(row) {
      this.$confirm('确认删除该合并模型吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteModel({ modelId: row.modelId }).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    /** 更新状态 */
    handleUpdateStatus(row, status) {
      const statusText = status === 'ACTIVE' ? '启用' : '停用'
      this.$confirm(`确认${statusText}该合并模型吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateModelStatus({ modelId: row.modelId, status }).then(res => {
          if (res.code === 200) {
            this.$message.success(`${statusText}成功`)
            this.getList()
          } else {
            this.$message.error(res.msg || `${statusText}失败`)
          }
        })
      }).catch(() => {})
    },
    /** 表单提交成功 */
    handleFormSuccess() {
      this.formVisible = false
      this.getList()
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>


