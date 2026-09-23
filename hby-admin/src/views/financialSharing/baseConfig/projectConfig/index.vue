<template>
  <div class="project-config-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="项目名称" prop="projectName">
          <el-input
            v-model="searchForm.projectName"
            placeholder="请输入项目名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="项目状态" prop="projectStatus">
          <el-select
            v-model="searchForm.projectStatus"
            placeholder="请选择项目状态"
            clearable
            style="width: 150px"
          >
            <el-option label="规划中" value="PLANNING" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已暂停" value="SUSPENDED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目经理" prop="projectManager">
          <el-input
            v-model="searchForm.projectManager"
            placeholder="请输入项目经理"
            clearable
            style="width: 150px"
          />
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
        <el-table-column prop="projectCode" label="项目编码" width="150" />
        <el-table-column prop="projectName" label="项目名称" min-width="200" />
        <el-table-column prop="projectType" label="项目类型" width="120">
          <template slot-scope="scope">
            {{ getProjectTypeName(scope.row.projectType) }}
          </template>
        </el-table-column>
        <el-table-column prop="projectManagerName" label="项目经理" width="120" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="budgetAmount" label="预算金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.budgetAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="usedAmount" label="已用金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.usedAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="projectStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.projectStatus)">
              {{ getStatusName(scope.row.projectStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orgName" label="所属组织" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="350" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="info" @click="handleViewMembers(scope.row)">成员</el-button>
            <el-button size="mini" type="success" @click="handleViewBudget(scope.row)">预算</el-button>
            <el-button size="mini" type="warning" @click="handleSettlement(scope.row)">结算</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'updateStatus', row: scope.row}">
                  更新状态
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'statistics', row: scope.row}">
                  统计信息
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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
      width="800px"
      @close="handleDialogClose"
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="formRef"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="项目编码" prop="projectCode">
              <el-input v-model="formData.projectCode" placeholder="请输入项目编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="formData.projectName" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="项目类型" prop="projectType">
              <el-select v-model="formData.projectType" placeholder="请选择项目类型" style="width: 100%">
                <el-option label="研发项目" value="DEVELOPMENT" />
                <el-option label="营销项目" value="MARKETING" />
                <el-option label="运营项目" value="OPERATION" />
                <el-option label="其他项目" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目经理" prop="projectManager">
              <el-input v-model="formData.projectManager" placeholder="请输入项目经理" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="formData.startDate"
                type="date"
                placeholder="选择开始日期"
                style="width: 100%"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="formData.endDate"
                type="date"
                placeholder="选择结束日期"
                style="width: 100%"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算金额" prop="budgetAmount">
              <el-input-number 
                v-model="formData.budgetAmount" 
                :min="0" 
                :precision="2"
                style="width: 100%" 
                placeholder="请输入预算金额"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目状态" prop="projectStatus">
              <el-select v-model="formData.projectStatus" placeholder="请选择项目状态" style="width: 100%">
                <el-option label="规划中" value="PLANNING" />
                <el-option label="进行中" value="IN_PROGRESS" />
                <el-option label="已完成" value="COMPLETED" />
                <el-option label="已暂停" value="SUSPENDED" />
                <el-option label="已取消" value="CANCELLED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="项目描述" prop="description">
          <el-input
            v-model="formData.description"
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

    <!-- 项目结算对话框 -->
    <el-dialog
      title="项目结算"
      :visible.sync="settlementDialogVisible"
      width="600px"
    >
      <el-form :model="settlementForm" ref="settlementFormRef" label-width="120px">
        <el-form-item label="结算日期" prop="settlementDate" :rules="[{required: true, message: '请选择结算日期'}]">
          <el-date-picker
            v-model="settlementForm.settlementDate"
            type="date"
            placeholder="选择结算日期"
            style="width: 100%"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item label="结算金额" prop="settlementAmount" :rules="[{required: true, message: '请输入结算金额'}]">
          <el-input-number 
            v-model="settlementForm.settlementAmount" 
            :min="0" 
            :precision="2"
            style="width: 100%" 
            placeholder="请输入结算金额"
          />
        </el-form-item>
        <el-form-item label="结算说明" prop="settlementRemark">
          <el-input
            v-model="settlementForm.settlementRemark"
            type="textarea"
            :rows="3"
            placeholder="请输入结算说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="settlementDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmSettlement" :loading="settlementLoading">确定结算</el-button>
      </div>
    </el-dialog>

    <!-- 更新状态对话框 -->
    <el-dialog
      title="更新项目状态"
      :visible.sync="statusDialogVisible"
      width="400px"
    >
      <el-form :model="statusForm" ref="statusFormRef" label-width="100px">
        <el-form-item label="项目状态" prop="projectStatus" :rules="[{required: true, message: '请选择项目状态'}]">
          <el-select v-model="statusForm.projectStatus" placeholder="请选择项目状态" style="width: 100%">
            <el-option label="规划中" value="PLANNING" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已暂停" value="SUSPENDED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmUpdateStatus" :loading="statusLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { projectConfigApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'ProjectConfig',
  data() {
    return {
      loading: false,
      saveLoading: false,
      settlementLoading: false,
      statusLoading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        projectName: '',
        projectStatus: '',
        projectManager: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增项目配置',
      formData: {
        projectId: null,
        projectCode: '',
        projectName: '',
        projectType: '',
        projectManager: '',
        startDate: '',
        endDate: '',
        budgetAmount: 0,
        projectStatus: 'PLANNING',
        description: ''
      },
      formRules: {
        projectCode: [
          { required: true, message: '请输入项目编码', trigger: 'blur' }
        ],
        projectName: [
          { required: true, message: '请输入项目名称', trigger: 'blur' }
        ],
        projectType: [
          { required: true, message: '请选择项目类型', trigger: 'change' }
        ],
        projectManager: [
          { required: true, message: '请输入项目经理', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ],
        budgetAmount: [
          { required: true, message: '请输入预算金额', trigger: 'blur' }
        ]
      },
      settlementDialogVisible: false,
      settlementForm: {
        settlementDate: '',
        settlementAmount: 0,
        settlementRemark: ''
      },
      currentSettlementRow: null,
      statusDialogVisible: false,
      statusForm: {
        projectStatus: ''
      },
      currentStatusRow: null
    }
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
        const response = await projectConfigApi.getList(params)
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
      this.dialogTitle = '新增项目配置'
      this.formData = {
        projectId: null,
        projectCode: '',
        projectName: '',
        projectType: '',
        projectManager: '',
        startDate: '',
        endDate: '',
        budgetAmount: 0,
        projectStatus: 'PLANNING',
        description: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑项目配置'
      this.formData = { ...row }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true
        const response = await projectConfigApi.save(this.formData)
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
        await this.$confirm('确定要删除该项目配置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await projectConfigApi.delete(row.projectId)
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
        await this.$confirm('确定要删除选中的项目配置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const projectIds = this.multipleSelection.map(item => item.projectId)
        const response = await projectConfigApi.batchDelete(projectIds)
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
    handleViewMembers(row) {
      // TODO: 跳转到项目成员页面
      this.$router.push({
        path: '/financialSharing/baseConfig/projectConfig/members',
        query: { projectId: row.projectId }
      })
    },
    handleViewBudget(row) {
      // TODO: 跳转到项目预算页面
      this.$router.push({
        path: '/financialSharing/baseConfig/projectConfig/budget',
        query: { projectId: row.projectId }
      })
    },
    handleSettlement(row) {
      this.currentSettlementRow = row
      this.settlementForm = {
        settlementDate: '',
        settlementAmount: row.usedAmount || 0,
        settlementRemark: ''
      }
      this.settlementDialogVisible = true
    },
    async handleConfirmSettlement() {
      try {
        await this.$refs.settlementFormRef.validate()
        this.settlementLoading = true
        const response = await projectConfigApi.settlement(this.currentSettlementRow.projectId, this.settlementForm)
        if (response.code === 1) {
          this.$message.success('项目结算成功')
          this.settlementDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '项目结算失败')
        }
      } catch (error) {
        this.$message.error('项目结算失败：' + error.message)
      } finally {
        this.settlementLoading = false
      }
    },
    handleCommand(command) {
      const { action, row } = command
      if (action === 'updateStatus') {
        this.currentStatusRow = row
        this.statusForm = {
          projectStatus: row.projectStatus
        }
        this.statusDialogVisible = true
      } else if (action === 'statistics') {
        // TODO: 显示统计信息
        this.showStatistics(row)
      }
    },
    async handleConfirmUpdateStatus() {
      try {
        await this.$refs.statusFormRef.validate()
        this.statusLoading = true
        const response = await projectConfigApi.updateStatus(this.currentStatusRow.projectId, this.statusForm.projectStatus)
        if (response.code === 1) {
          this.$message.success('状态更新成功')
          this.statusDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '状态更新失败')
        }
      } catch (error) {
        this.$message.error('状态更新失败：' + error.message)
      } finally {
        this.statusLoading = false
      }
    },
    async showStatistics(row) {
      try {
        const response = await projectConfigApi.getStatistics(row.projectId)
        if (response.code === 1) {
          const stats = response.data
          this.$alert(
            `项目统计信息：\n总预算：¥${stats.totalBudget}\n已用金额：¥${stats.usedAmount}\n剩余金额：¥${stats.remainAmount}\n预算使用率：${stats.budgetUsageRate}%\n项目成员数：${stats.memberCount}人`,
            '项目统计',
            { type: 'info' }
          )
        } else {
          this.$message.error(response.msg || '获取统计信息失败')
        }
      } catch (error) {
        this.$message.error('获取统计信息失败：' + error.message)
      }
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
    },
    getStatusType(status) {
      const statusMap = {
        'PLANNING': 'info',
        'IN_PROGRESS': 'primary',
        'COMPLETED': 'success',
        'SUSPENDED': 'warning',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getStatusName(status) {
      const statusMap = {
        'PLANNING': '规划中',
        'IN_PROGRESS': '进行中',
        'COMPLETED': '已完成',
        'SUSPENDED': '已暂停',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },
    getProjectTypeName(type) {
      const typeMap = {
        'DEVELOPMENT': '研发项目',
        'MARKETING': '营销项目',
        'OPERATION': '运营项目',
        'OTHER': '其他项目'
      }
      return typeMap[type] || type || '-'
    }
  }
}
</script>

<style scoped>
.project-config-container {
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
