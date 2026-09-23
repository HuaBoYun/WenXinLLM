<template>
  <div class="proxy-delegation-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="委托人" prop="delegatorName">
          <el-input
            v-model="searchForm.delegatorName"
            placeholder="请输入委托人"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="代理人" prop="proxyName">
          <el-input
            v-model="searchForm.proxyName"
            placeholder="请输入代理人"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="委托类型" prop="delegationType">
          <el-select
            v-model="searchForm.delegationType"
            placeholder="请选择委托类型"
            clearable
            style="width: 150px"
          >
            <el-option label="全权委托" value="FULL" />
            <el-option label="部分委托" value="PARTIAL" />
            <el-option label="临时委托" value="TEMPORARY" />
            <el-option label="紧急委托" value="EMERGENCY" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
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
        <el-table-column prop="delegationCode" label="委托编码" width="150" />
        <el-table-column prop="delegatorName" label="委托人" width="120" />
        <el-table-column prop="delegatorDept" label="委托人部门" width="120" />
        <el-table-column prop="proxyName" label="代理人" width="120" />
        <el-table-column prop="proxyDept" label="代理人部门" width="120" />
        <el-table-column prop="delegationType" label="委托类型" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.delegationType === 'FULL'">全权委托</span>
            <span v-else-if="scope.row.delegationType === 'PARTIAL'">部分委托</span>
            <span v-else-if="scope.row.delegationType === 'TEMPORARY'">临时委托</span>
            <span v-else-if="scope.row.delegationType === 'EMERGENCY'">紧急委托</span>
            <span v-else>{{ scope.row.delegationType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="delegationScope" label="委托范围" min-width="180" show-overflow-tooltip />
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'danger'">
              {{ scope.row.isEnabled === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="350" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="info" @click="handlePermissions(scope.row)">权限配置</el-button>
            <el-button size="mini" type="success" @click="handleHistory(scope.row)">历史记录</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            <el-button
              size="mini"
              :type="scope.row.isEnabled === 1 ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.isEnabled === 1 ? '禁用' : '启用' }}
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
            <el-form-item label="委托编码" prop="delegationCode">
              <el-input v-model="formData.delegationCode" placeholder="请输入委托编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="委托类型" prop="delegationType">
              <el-select v-model="formData.delegationType" placeholder="请选择委托类型" style="width: 100%">
                <el-option label="全权委托" value="FULL" />
                <el-option label="部分委托" value="PARTIAL" />
                <el-option label="临时委托" value="TEMPORARY" />
                <el-option label="紧急委托" value="EMERGENCY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="委托人" prop="delegatorId">
              <el-select v-model="formData.delegatorId" placeholder="请选择委托人" style="width: 100%" filterable @change="handleDelegatorChange">
                <el-option
                  v-for="user in userList"
                  :key="user.userId"
                  :label="user.userName"
                  :value="user.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="代理人" prop="proxyId">
              <el-select v-model="formData.proxyId" placeholder="请选择代理人" style="width: 100%" filterable @change="handleProxyChange">
                <el-option
                  v-for="user in userList"
                  :key="user.userId"
                  :label="user.userName"
                  :value="user.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="formData.startDate"
                type="date"
                placeholder="请选择开始日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="formData.endDate"
                type="date"
                placeholder="请选择结束日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="委托范围" prop="delegationScope">
          <el-input v-model="formData.delegationScope" placeholder="请输入委托范围" />
        </el-form-item>
        <el-form-item label="权限范围" prop="permissionScope">
          <el-checkbox-group v-model="formData.permissionScope">
            <el-checkbox label="EXPENSE_REPORT">费用报销</el-checkbox>
            <el-checkbox label="LOAN_APPLICATION">借款申请</el-checkbox>
            <el-checkbox label="PREPAYMENT">预付款</el-checkbox>
            <el-checkbox label="PROVISION">费用预提</el-checkbox>
            <el-checkbox label="APPROVAL">审批权限</el-checkbox>
            <el-checkbox label="QUERY">查询权限</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="金额限制" prop="amountLimit">
          <el-input-number 
            v-model="formData.amountLimit" 
            :min="0" 
            :precision="2" 
            placeholder="请输入金额限制"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="formData.isEnabled" />
        </el-form-item>
        <el-form-item label="委托说明" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入委托说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 历史记录对话框 -->
    <el-dialog
      title="代理委托历史记录"
      :visible.sync="historyDialogVisible"
      width="800px"
    >
      <el-table :data="historyData" v-loading="historyLoading">
        <el-table-column prop="operationType" label="操作类型" width="120" />
        <el-table-column prop="operationTime" label="操作时间" width="160" />
        <el-table-column prop="operator" label="操作人" width="120" />
        <el-table-column prop="operationContent" label="操作内容" min-width="200" />
        <el-table-column prop="remark" label="备注" min-width="150" />
      </el-table>
    </el-dialog>

    <!-- 权限配置对话框 -->
    <el-dialog
      title="权限配置"
      :visible.sync="permissionsDialogVisible"
      width="900px"
      @close="handlePermissionsDialogClose"
    >
      <el-form :model="permissionsForm" label-width="120px">
        <el-form-item label="委托信息">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="委托编号">{{ currentPermissionsRow.delegationCode }}</el-descriptions-item>
            <el-descriptions-item label="委托类型">
              <span v-if="currentPermissionsRow.delegationType === 'FULL'">全权委托</span>
              <span v-else-if="currentPermissionsRow.delegationType === 'PARTIAL'">部分委托</span>
              <span v-else-if="currentPermissionsRow.delegationType === 'TEMPORARY'">临时委托</span>
              <span v-else-if="currentPermissionsRow.delegationType === 'EMERGENCY'">紧急委托</span>
            </el-descriptions-item>
            <el-descriptions-item label="委托人">{{ currentPermissionsRow.delegatorName }}</el-descriptions-item>
            <el-descriptions-item label="代理人">{{ currentPermissionsRow.proxyName }}</el-descriptions-item>
          </el-descriptions>
        </el-form-item>

        <el-form-item label="权限范围">
          <el-checkbox-group v-model="permissionsForm.permissionScope">
            <el-checkbox label="EXPENSE_REPORT">费用报销</el-checkbox>
            <el-checkbox label="PREPAYMENT">预付款</el-checkbox>
            <el-checkbox label="PROVISION">费用预提</el-checkbox>
            <el-checkbox label="QUERY">查询权限</el-checkbox>
            <el-checkbox label="APPROVAL">审批权限</el-checkbox>
            <el-checkbox label="EXPORT">导出权限</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="详细权限配置">
          <el-table :data="permissionsForm.detailPermissions" border style="width: 100%">
            <el-table-column prop="permissionType" label="权限类型" width="150">
              <template slot-scope="scope">
                <el-select v-model="scope.row.permissionType" placeholder="请选择">
                  <el-option label="费用报销" value="EXPENSE_REPORT"></el-option>
                  <el-option label="预付款" value="PREPAYMENT"></el-option>
                  <el-option label="费用预提" value="PROVISION"></el-option>
                  <el-option label="查询权限" value="QUERY"></el-option>
                  <el-option label="审批权限" value="APPROVAL"></el-option>
                  <el-option label="导出权限" value="EXPORT"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="permissionLevel" label="权限级别" width="150">
              <template slot-scope="scope">
                <el-select v-model="scope.row.permissionLevel" placeholder="请选择">
                  <el-option label="只读" value="READ"></el-option>
                  <el-option label="编辑" value="EDIT"></el-option>
                  <el-option label="完全控制" value="FULL"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="amountLimit" label="金额限制" width="150">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.amountLimit" :min="0" :precision="2" style="width: 100%"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注">
              <template slot-scope="scope">
                <el-input v-model="scope.row.remark" placeholder="请输入备注"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="danger" @click="handleDeletePermission(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-button type="primary" size="small" style="margin-top: 10px" @click="handleAddPermission">添加权限</el-button>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="permissionsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePermissions" :loading="permissionsSaving">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { proxyDelegationApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'ProxyDelegation',
  data() {
    return {
      loading: false,
      saveLoading: false,
      historyLoading: false,
      tableData: [],
      multipleSelection: [],
      userList: [
        { userId: 'USER001', userName: '张三' },
        { userId: 'USER002', userName: '李四' },
        { userId: 'USER003', userName: '王五' },
        { userId: 'USER004', userName: '赵六' }
      ],
      searchForm: {
        delegatorName: '',
        proxyName: '',
        delegationType: '',
        status: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增代理委托',
      formData: {
        delegationId: null,
        delegationCode: '',
        delegationType: '',
        delegatorId: '',
        delegatorName: '',
        proxyId: '',
        proxyName: '',
        startDate: '',
        endDate: '',
        delegationScope: '',
        permissionScope: [],
        amountLimit: 0,
        isEnabled: true,
        description: ''
      },
      formRules: {
        delegationCode: [
          { required: true, message: '请输入委托编码', trigger: 'blur' }
        ],
        delegationType: [
          { required: true, message: '请选择委托类型', trigger: 'change' }
        ],
        delegatorId: [
          { required: true, message: '请选择委托人', trigger: 'change' }
        ],
        proxyId: [
          { required: true, message: '请选择代理人', trigger: 'change' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ],
        endDate: [
          { required: true, message: '请选择结束日期', trigger: 'change' }
        ]
      },
      historyDialogVisible: false,
      historyData: [],
      currentHistoryRow: null,
      permissionsDialogVisible: false,
      permissionsSaving: false,
      currentPermissionsRow: {},
      permissionsForm: {
        permissionScope: [],
        detailPermissions: []
      }
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
        const response = await proxyDelegationApi.getList(params)
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
      this.dialogTitle = '新增代理委托'
      this.formData = {
        delegationId: null,
        delegationCode: '',
        delegationType: '',
        delegatorId: '',
        delegatorName: '',
        proxyId: '',
        proxyName: '',
        startDate: '',
        endDate: '',
        delegationScope: '',
        permissionScope: [],
        amountLimit: 0,
        isEnabled: true,
        description: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑代理委托'
      // 深拷贝并转换数据格式
      this.formData = { ...row }

      // 转换权限范围：字符串 -> 数组
      if (this.formData.permissionScope && typeof this.formData.permissionScope === 'string') {
        this.formData.permissionScope = this.formData.permissionScope.split(',')
      }

      // 转换是否启用：Integer -> Boolean
      if (typeof this.formData.isEnabled === 'number') {
        this.formData.isEnabled = this.formData.isEnabled === 1
      }

      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true
        const response = await proxyDelegationApi.save(this.formData)
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
        await this.$confirm('确定要删除该代理委托吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await proxyDelegationApi.delete(row.delegationId)
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
        await this.$confirm('确定要删除选中的代理委托吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const delegationIds = this.multipleSelection.map(item => item.delegationId)
        const response = await proxyDelegationApi.batchDelete(delegationIds)
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
        // 根据当前状态调用激活或停用接口
        const response = row.isEnabled === 1
          ? await proxyDelegationApi.deactivate(row.delegationId)
          : await proxyDelegationApi.activate(row.delegationId)

        if (response.code === 1) {
          this.$message.success(row.isEnabled === 1 ? '停用成功' : '激活成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },
    async handlePermissions(row) {
      this.currentPermissionsRow = row
      this.permissionsDialogVisible = true
      this.permissionsSaving = false

      try {
        // 获取权限配置
        const response = await proxyDelegationApi.getPermissions(row.delegationId)
        if (response.code === 1 && response.data) {
          // 如果有数据，填充表单
          this.permissionsForm.detailPermissions = response.data || []

          // 从详细权限中提取权限范围
          const permissionTypes = new Set()
          this.permissionsForm.detailPermissions.forEach(item => {
            if (item.permissionType) {
              permissionTypes.add(item.permissionType)
            }
          })
          this.permissionsForm.permissionScope = Array.from(permissionTypes)
        } else {
          // 如果没有数据，初始化为空
          this.permissionsForm.permissionScope = []
          this.permissionsForm.detailPermissions = []
        }
      } catch (error) {
        this.$message.error('获取权限配置失败：' + error.message)
        this.permissionsForm.permissionScope = []
        this.permissionsForm.detailPermissions = []
      }
    },
    handlePermissionsDialogClose() {
      // 重置表单
      this.permissionsForm = {
        permissionScope: [],
        detailPermissions: []
      }
      this.currentPermissionsRow = {}
    },
    handleAddPermission() {
      this.permissionsForm.detailPermissions.push({
        permissionType: '',
        permissionLevel: 'READ',
        amountLimit: 0,
        remark: ''
      })
    },
    handleDeletePermission(index) {
      this.permissionsForm.detailPermissions.splice(index, 1)
    },
    async handleSavePermissions() {
      // 验证数据
      if (this.permissionsForm.detailPermissions.length === 0) {
        this.$message.warning('请至少添加一条权限配置')
        return
      }

      // 验证每条权限是否完整
      for (let i = 0; i < this.permissionsForm.detailPermissions.length; i++) {
        const item = this.permissionsForm.detailPermissions[i]
        if (!item.permissionType) {
          this.$message.warning(`第 ${i + 1} 条权限的类型不能为空`)
          return
        }
        if (!item.permissionLevel) {
          this.$message.warning(`第 ${i + 1} 条权限的级别不能为空`)
          return
        }
      }

      this.permissionsSaving = true
      try {
        const response = await proxyDelegationApi.savePermissions(
          this.currentPermissionsRow.delegationId,
          this.permissionsForm.detailPermissions
        )

        if (response.code === 1) {
          this.$message.success('保存权限配置成功')
          this.permissionsDialogVisible = false
        } else {
          this.$message.error(response.msg || '保存权限配置失败')
        }
      } catch (error) {
        this.$message.error('保存权限配置失败：' + error.message)
      } finally {
        this.permissionsSaving = false
      }
    },
    async handleHistory(row) {
      this.currentHistoryRow = row
      this.historyDialogVisible = true
      this.historyLoading = true
      try {
        const response = await proxyDelegationApi.getHistory(row.delegationId)
        if (response.code === 1) {
          this.historyData = response.data || []
        } else {
          this.$message.error(response.msg || '获取历史记录失败')
        }
      } catch (error) {
        this.$message.error('获取历史记录失败：' + error.message)
      } finally {
        this.historyLoading = false
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
    handleDelegatorChange(userId) {
      // 根据选择的委托人ID，设置委托人姓名
      const user = this.userList.find(u => u.userId === userId)
      if (user) {
        this.formData.delegatorName = user.userName
      }
    },
    handleProxyChange(userId) {
      // 根据选择的代理人ID，设置代理人姓名
      const user = this.userList.find(u => u.userId === userId)
      if (user) {
        this.formData.proxyName = user.userName
      }
    }
  }
}
</script>

<style scoped>
.proxy-delegation-container {
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
