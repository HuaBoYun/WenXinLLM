<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :model="queryForm" :inline="true" label-width="100px">
      <el-form-item label="合并模型">
        <el-select v-model="queryForm.modelId" placeholder="请选择合并模型" clearable @change="handleQuery">
          <el-option
            v-for="item in modelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="组织名称">
        <el-input v-model="queryForm.orgName" placeholder="请输入组织名称" clearable />
      </el-form-item>
      <el-form-item label="合并方法">
        <el-select v-model="queryForm.consolidationMethod" placeholder="请选择合并方法" clearable>
          <el-option label="完全合并" value="FULL" />
          <el-option label="比例合并" value="PROPORTIONAL" />
          <el-option label="权益法" value="EQUITY" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.isActive" placeholder="请选择状态" clearable>
          <el-option label="启用" value="Y" />
          <el-option label="停用" value="N" />
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
        <el-button type="success" icon="el-icon-edit" @click="handleBatchConfig">批量配置</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="scopeList" border>
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column label="模型名称" prop="modelName" width="200" />
      <el-table-column label="组织ID" prop="orgId" width="150" />
      <el-table-column label="组织名称" prop="orgName" width="200" />
      <el-table-column label="合并方法" prop="consolidationMethod" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.consolidationMethod === 'FULL'" type="success">完全合并</el-tag>
          <el-tag v-else-if="scope.row.consolidationMethod === 'PROPORTIONAL'" type="warning">比例合并</el-tag>
          <el-tag v-else-if="scope.row.consolidationMethod === 'EQUITY'" type="info">权益法</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="生效起始期间" prop="effectiveStartPeriod" width="120" />
      <el-table-column label="生效终止期间" prop="effectiveEndPeriod" width="120" />
      <el-table-column label="排序号" prop="sortOrder" width="80" align="center" />
      <el-table-column label="状态" prop="isActive" width="80" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isActive === 'Y'" type="success">启用</el-tag>
          <el-tag v-else type="info">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" min-width="150" show-overflow-tooltip />
      <el-table-column label="创建时间" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            v-if="scope.row.isActive === 'Y'"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleUpdateStatus(scope.row, 'N')"
          >停用</el-button>
          <el-button
            v-else
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleUpdateStatus(scope.row, 'Y')"
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
    <consolidation-scope-form
      v-if="formVisible"
      :visible.sync="formVisible"
      :scope-id="currentScopeId"
      :model-id="queryForm.modelId"
      :form-type="formType"
      @success="handleFormSuccess"
    />

    <!-- 批量配置对话框 -->
    <batch-config-dialog
      v-if="batchConfigVisible"
      :visible.sync="batchConfigVisible"
      :model-id="queryForm.modelId"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script>
import { getScopeList, deleteScope, updateScopeStatus } from '@/api/financialSharing/consolidationReport/consolidationScope'
import { getModelList } from '@/api/financialSharing/consolidationReport/consolidationModel'
import ConsolidationScopeForm from './components/ConsolidationScopeForm.vue'
import BatchConfigDialog from './components/BatchConfigDialog.vue'
import Pagination from '@/components/Pagination'

export default {
  name: 'ConsolidationScope',
  components: {
    ConsolidationScopeForm,
    BatchConfigDialog,
    Pagination
  },
  data() {
    return {
      // 查询参数
      queryForm: {
        modelId: '',
        orgName: '',
        consolidationMethod: '',
        isActive: '',
        pageNum: 1,
        pageSize: 10
      },
      // 加载状态
      loading: false,
      // 数据列表
      scopeList: [],
      // 总记录数
      total: 0,
      // 表单对话框
      formVisible: false,
      // 当前范围配置ID
      currentScopeId: null,
      // 表单类型：add/edit/view
      formType: 'add',
      // 批量配置对话框
      batchConfigVisible: false,
      // 模型选项
      modelOptions: []
    }
  },
  created() {
    this.loadModelOptions()
    this.getList()
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
    /** 查询列表 */
    getList() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getScopeList(this.queryForm).then(res => {
        this.loading = false
        if (res.code === 1 || res.code === 200) {
          this.scopeList = res.data.list || res.data.records || []
          this.total = res.data.total || res.data.totalRecord || 0
        } else {
          this.$message.error(res.msg || '查询失败')
          this.scopeList = []
          this.total = 0
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.scopeList = []
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
        modelId: '',
        orgName: '',
        consolidationMethod: '',
        isActive: '',
        pageNum: 1,
        pageSize: 10
      }
      this.getList()
    },
    /** 新增按钮 */
    handleAdd() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }
      this.currentScopeId = null
      this.formType = 'add'
      this.formVisible = true
    },
    /** 查看按钮 */
    handleView(row) {
      this.currentScopeId = row.scopeId
      this.formType = 'view'
      this.formVisible = true
    },
    /** 编辑按钮 */
    handleEdit(row) {
      this.currentScopeId = row.scopeId
      this.formType = 'edit'
      this.formVisible = true
    },
    /** 删除按钮 */
    handleDelete(row) {
      this.$confirm('确认删除该合并范围配置吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteScope({ scopeId: row.scopeId }).then(res => {
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
    handleUpdateStatus(row, isActive) {
      const statusText = isActive === 'Y' ? '启用' : '停用'
      this.$confirm(`确认${statusText}该合并范围配置吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateScopeStatus({ scopeId: row.scopeId, isActive }).then(res => {
          if (res.code === 200) {
            this.$message.success(`${statusText}成功`)
            this.getList()
          } else {
            this.$message.error(res.msg || `${statusText}失败`)
          }
        })
      }).catch(() => {})
    },
    /** 批量配置按钮 */
    handleBatchConfig() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }
      this.batchConfigVisible = true
    },
    /** 表单提交成功 */
    handleFormSuccess() {
      this.formVisible = false
      this.batchConfigVisible = false
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


