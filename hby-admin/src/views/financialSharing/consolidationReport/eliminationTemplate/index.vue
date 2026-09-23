<template>
  <div class="app-container">
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
      <el-form-item label="模板编码">
        <el-input v-model="queryForm.templateCode" placeholder="请输入模板编码" clearable />
      </el-form-item>
      <el-form-item label="模板名称">
        <el-input v-model="queryForm.templateName" placeholder="请输入模板名称" clearable />
      </el-form-item>
      <el-form-item label="抵消类型">
        <el-select v-model="queryForm.eliminationType" placeholder="请选择抵消类型" clearable>
          <el-option label="内部交易抵消" value="INTERNAL_TRANSACTION" />
          <el-option label="内部债权债务抵消" value="INTERNAL_DEBT" />
          <el-option label="未实现利润抵消" value="UNREALIZED_PROFIT" />
          <el-option label="长期股权投资抵消" value="INVESTMENT_ELIMINATION" />
          <el-option label="所有者权益抵消" value="EQUITY_ELIMINATION" />
          <el-option label="其他" value="OTHER" />
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
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column label="模型名称" prop="modelName" width="150" />
      <el-table-column label="模板编码" prop="templateCode" width="120" />
      <el-table-column label="模板名称" prop="templateName" width="150" />
      <el-table-column label="抵消类型" prop="eliminationType" width="150">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.eliminationType === 'INTERNAL_TRANSACTION'" type="primary" size="mini">
            内部交易抵消
          </el-tag>
          <el-tag v-else-if="scope.row.eliminationType === 'INTERNAL_DEBT'" type="success" size="mini">
            内部债权债务抵消
          </el-tag>
          <el-tag v-else-if="scope.row.eliminationType === 'UNREALIZED_PROFIT'" type="warning" size="mini">
            未实现利润抵消
          </el-tag>
          <el-tag v-else-if="scope.row.eliminationType === 'INVESTMENT_ELIMINATION'" type="danger" size="mini">
            长期股权投资抵消
          </el-tag>
          <el-tag v-else-if="scope.row.eliminationType === 'EQUITY_ELIMINATION'" type="info" size="mini">
            所有者权益抵消
          </el-tag>
          <el-tag v-else size="mini">其他</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="借方科目" prop="debitAccount" width="150" show-overflow-tooltip />
      <el-table-column label="贷方科目" prop="creditAccount" width="150" show-overflow-tooltip />
      <el-table-column label="状态" prop="isActive" width="80" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isActive === 'Y'" type="success" size="mini">启用</el-tag>
          <el-tag v-else type="info" size="mini">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="排序号" prop="sortOrder" width="80" align="center" />
      <el-table-column label="创建时间" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">
            查看
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button
            v-if="scope.row.isActive === 'Y'"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleDisable(scope.row)"
          >
            停用
          </el-button>
          <el-button
            v-else
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleEnable(scope.row)"
          >
            启用
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">
            删除
          </el-button>
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

    <!-- 表单对话框 -->
    <elimination-template-form
      v-if="formVisible"
      :visible.sync="formVisible"
      :mode="formMode"
      :template-id="currentTemplateId"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script>
import { getTemplateList, deleteTemplate, updateTemplateStatus } from '@/api/financialSharing/consolidationReport/eliminationTemplate'
import { getModelList } from '@/api/financialSharing/consolidationReport/consolidationModel'
import EliminationTemplateForm from './components/EliminationTemplateForm'
import Pagination from '@/components/Pagination'

export default {
  name: 'EliminationTemplate',
  components: {
    EliminationTemplateForm,
    Pagination
  },
  data() {
    return {
      // 查询参数
      queryForm: {
        modelId: '',
        templateCode: '',
        templateName: '',
        eliminationType: '',
        isActive: '',
        pageNum: 1,
        pageSize: 10
      },
      // 加载状态
      loading: false,
      // 表格数据
      tableData: [],
      // 总记录数
      total: 0,
      // 表单对话框
      formVisible: false,
      // 表单模式：add/edit/view
      formMode: 'add',
      // 当前模板ID
      currentTemplateId: '',
      // 模型选项
      modelOptions: []
    }
  },
  created() {
    this.loadModelOptions()
    this.loadData()
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
    /** 查询按钮 */
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },
    /** 重置按钮 */
    resetQuery() {
      this.queryForm = {
        modelId: '',
        templateCode: '',
        templateName: '',
        eliminationType: '',
        isActive: '',
        pageNum: 1,
        pageSize: 10
      }
      this.loadData()
    },
    /** 加载数据 */
    loadData() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getTemplateList(this.queryForm).then(res => {
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
      this.formMode = 'add'
      this.currentTemplateId = ''
      this.formVisible = true
    },
    /** 查看按钮 */
    handleView(row) {
      this.formMode = 'view'
      this.currentTemplateId = row.templateId
      this.formVisible = true
    },
    /** 编辑按钮 */
    handleEdit(row) {
      this.formMode = 'edit'
      this.currentTemplateId = row.templateId
      this.formVisible = true
    },
    /** 启用按钮 */
    handleEnable(row) {
      this.$confirm('确认启用该模板吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateTemplateStatus({ templateId: row.templateId, isActive: 'Y' }).then(res => {
          if (res.code === 200) {
            this.$message.success('启用成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '启用失败')
          }
        })
      }).catch(() => {})
    },
    /** 停用按钮 */
    handleDisable(row) {
      this.$confirm('确认停用该模板吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateTemplateStatus({ templateId: row.templateId, isActive: 'N' }).then(res => {
          if (res.code === 200) {
            this.$message.success('停用成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '停用失败')
          }
        })
      }).catch(() => {})
    },
    /** 删除按钮 */
    handleDelete(row) {
      this.$confirm('确认删除该模板吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTemplate({ templateId: row.templateId }).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    /** 表单提交成功 */
    handleFormSuccess() {
      this.formVisible = false
      this.loadData()
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>


