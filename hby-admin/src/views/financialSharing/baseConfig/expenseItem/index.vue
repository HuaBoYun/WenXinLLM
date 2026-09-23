<template>
  <div class="expense-item-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="费用项目名称" prop="itemName">
          <el-input
            v-model="searchForm.itemName"
            placeholder="请输入费用项目名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="项目类型" prop="itemType">
          <el-select
            v-model="searchForm.itemType"
            placeholder="请选择项目类型"
            clearable
            style="width: 150px"
          >
            <el-option label="差旅类" value="TRAVEL" />
            <el-option label="办公类" value="OFFICE" />
            <el-option label="招待类" value="ENTERTAINMENT" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select
            v-model="searchForm.isEnabled"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="启用" :value="true" />
            <el-option label="禁用" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
      <el-button @click="handleTreeView">树形视图</el-button>
    </div>

    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="itemCode" label="项目编码" width="120" />
        <el-table-column prop="itemName" label="项目名称" min-width="150" />
        <el-table-column prop="itemTypeName" label="项目类型" width="100" />
        <el-table-column prop="parentItemName" label="上级项目" width="150" />
        <el-table-column prop="orgName" label="所属组织" width="120" />
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'">
              {{ scope.row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            <el-button 
              size="mini" 
              :type="scope.row.isEnabled ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.isEnabled ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

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
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="formRef"
        label-width="120px"
      >
        <el-form-item label="项目编码" prop="itemCode">
          <el-input v-model="formData.itemCode" placeholder="请输入项目编码" />
        </el-form-item>
        <el-form-item label="项目名称" prop="itemName">
          <el-input v-model="formData.itemName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目类型" prop="itemType">
          <el-select v-model="formData.itemType" placeholder="请选择项目类型" style="width: 100%">
            <el-option label="差旅类" value="TRAVEL" />
            <el-option label="办公类" value="OFFICE" />
            <el-option label="招待类" value="ENTERTAINMENT" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="上级项目" prop="parentItemId">
          <el-select v-model="formData.parentItemId" placeholder="请选择上级项目" style="width: 100%" clearable>
            <el-option
              v-for="item in parentItems"
              :key="item.itemId"
              :label="item.itemName"
              :value="item.itemId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="formData.isEnabled" />
        </el-form-item>
        <el-form-item label="预算控制" prop="budgetControlEnabled">
          <el-switch v-model="formData.budgetControlEnabled" />
        </el-form-item>
        <el-form-item label="标准控制" prop="standardControlEnabled">
          <el-switch v-model="formData.standardControlEnabled" />
        </el-form-item>
        <el-form-item label="稽核规则" prop="auditRuleEnabled">
          <el-switch v-model="formData.auditRuleEnabled" />
        </el-form-item>
        <el-form-item label="项目描述" prop="itemDesc">
          <el-input
            v-model="formData.itemDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入项目描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { expenseItemApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'ExpenseItem',
  data() {
    return {
      loading: false,
      saveLoading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        itemName: '',
        itemType: '',
        isEnabled: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增费用项目',
      formData: {
        itemId: null,
        itemCode: '',
        itemName: '',
        itemType: '',
        parentItemId: null,
        isEnabled: true,
        budgetControlEnabled: false,
        standardControlEnabled: false,
        auditRuleEnabled: false,
        itemDesc: ''
      },
      formRules: {
        itemCode: [
          { required: true, message: '请输入项目编码', trigger: 'blur' }
        ],
        itemName: [
          { required: true, message: '请输入项目名称', trigger: 'blur' }
        ],
        itemType: [
          { required: true, message: '请选择项目类型', trigger: 'change' }
        ]
      },
      parentItems: []
    }
  },
  computed: {
    
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await expenseItemApi.getList(params)
        if (response.code === 1) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },
    handleAdd() {
      this.dialogTitle = '新增费用项目'
      this.formData = {
        itemId: null,
        itemCode: '',
        itemName: '',
        itemType: '',
        parentItemId: null,
        isEnabled: true,
        budgetControlEnabled: false,
        standardControlEnabled: false,
        auditRuleEnabled: false,
        itemDesc: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑费用项目'
      this.formData = { ...row }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true
        const response = await expenseItemApi.save(this.formData)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.saveLoading = false
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该费用项目吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await expenseItemApi.delete(row.itemId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    async handleBatchDelete() {
      try {
        await this.$confirm('确定要删除选中的费用项目吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const itemIds = this.multipleSelection.map(item => item.itemId)
        const response = await expenseItemApi.batchDelete(itemIds)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败：' + error.message)
        }
      }
    },
    async handleToggleStatus(row) {
      try {
        const response = await expenseItemApi.updateStatus(row.itemId, !row.isEnabled)
        if (response.code === 1) {
          this.$message.success('状态更新成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '状态更新失败')
        }
      } catch (error) {
        this.$message.error('状态更新失败：' + error.message)
      }
    },
    handleTreeView() {
      // 切换到树形视图模式，重新加载树形数据
      this.viewMode = 'tree'
      this.loadData()
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },
    handleDialogClose() {
      this.$refs.formRef.resetFields()
    }
  }
}
</script>

<style scoped>
.expense-item-container {
  padding: 20px;
}

.search-container {
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
}

.table-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
