<template>
  <div class="budget-control-rule-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="规则名称" prop="ruleName">
          <el-input
            v-model="searchForm.ruleName"
            placeholder="请输入规则名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="控制类型" prop="controlType">
          <el-select
            v-model="searchForm.controlType"
            placeholder="请选择控制类型"
            clearable
            style="width: 150px"
          >
            <el-option label="部门月度控制" value="DEPARTMENT_MONTHLY" />
            <el-option label="部门年度控制" value="DEPARTMENT_YEARLY" />
            <el-option label="项目控制" value="PROJECT" />
            <el-option label="费用类型控制" value="EXPENSE_TYPE" />
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
        <el-table-column prop="ruleCode" label="规则编码" width="150" />
        <el-table-column prop="ruleName" label="规则名称" min-width="200" />
        <el-table-column prop="controlTypeName" label="控制类型" width="120" />
        <el-table-column prop="warningThreshold" label="预警阈值(%)" width="120" />
        <el-table-column prop="controlThreshold" label="控制阈值(%)" width="120" />
        <el-table-column prop="effectiveDate" label="生效日期" width="120" />
        <el-table-column prop="expiryDate" label="失效日期" width="120" />
        <el-table-column prop="orgName" label="所属组织" width="120" />
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'">
              {{ scope.row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="info" @click="handleViewScope(scope.row)">适用范围</el-button>
            <el-button size="mini" type="success" @click="handleCheck(scope.row)">检查</el-button>
            <el-button size="mini" type="warning" @click="handleCopy(scope.row)">复制</el-button>
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
            <el-form-item label="规则编码" prop="ruleCode">
              <el-input v-model="formData.ruleCode" placeholder="请输入规则编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="formData.ruleName" placeholder="请输入规则名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="控制类型" prop="controlType">
              <el-select v-model="formData.controlType" placeholder="请选择控制类型" style="width: 100%">
                <el-option label="部门月度控制" value="DEPARTMENT_MONTHLY" />
                <el-option label="部门年度控制" value="DEPARTMENT_YEARLY" />
                <el-option label="项目控制" value="PROJECT" />
                <el-option label="费用类型控制" value="EXPENSE_TYPE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警阈值(%)" prop="warningThreshold">
              <el-input-number 
                v-model="formData.warningThreshold" 
                :min="0" 
                :max="100" 
                :precision="2"
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控制阈值(%)" prop="controlThreshold">
              <el-input-number 
                v-model="formData.controlThreshold" 
                :min="0" 
                :max="200" 
                :precision="2"
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="formData.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                style="width: 100%"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expiryDate">
              <el-date-picker
                v-model="formData.expiryDate"
                type="date"
                placeholder="选择失效日期"
                style="width: 100%"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="formData.isEnabled" />
        </el-form-item>
        <el-form-item label="规则描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入规则描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 预算检查对话框 -->
    <el-dialog
      title="预算控制检查"
      :visible.sync="checkDialogVisible"
      width="600px"
    >
      <el-form :model="checkForm" ref="checkFormRef" label-width="120px">
        <el-form-item label="检查数据" prop="checkData">
          <el-input
            v-model="checkForm.checkData"
            type="textarea"
            :rows="8"
            placeholder="请输入JSON格式的检查数据，如：{&quot;departmentId&quot;: &quot;DEPT001&quot;, &quot;amount&quot;: 50000}"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="checkDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmCheck" :loading="checkLoading">执行检查</el-button>
      </div>
    </el-dialog>

    <!-- 复制对话框 -->
    <el-dialog
      title="复制预算管控规则"
      :visible.sync="copyDialogVisible"
      width="500px"
    >
      <el-form :model="copyForm" ref="copyFormRef" label-width="120px">
        <el-form-item label="新规则名称" prop="ruleName" :rules="[{required: true, message: '请输入新规则名称'}]">
          <el-input v-model="copyForm.ruleName" placeholder="请输入新规则名称" />
        </el-form-item>
        <el-form-item label="新规则编码" prop="ruleCode" :rules="[{required: true, message: '请输入新规则编码'}]">
          <el-input v-model="copyForm.ruleCode" placeholder="请输入新规则编码" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmCopy" :loading="copyLoading">确定复制</el-button>
      </div>
    </el-dialog>

    <!-- 适用范围对话框 -->
    <el-dialog
      title="配置适用范围"
      :visible.sync="scopeDialogVisible"
      width="900px"
      @close="handleScopeDialogClose"
    >
      <!-- 规则信息 -->
      <el-alert
        :title="`规则：${currentScopeRule.ruleName || ''} (${currentScopeRule.ruleCode || ''})`"
        type="info"
        :closable="false"
        style="margin-bottom: 20px"
      />

      <!-- 适用范围列表 -->
      <div class="scope-list-container">
        <div class="scope-toolbar">
          <el-button type="primary" size="small" @click="handleAddScopeItem">
            <i class="el-icon-plus"></i> 添加范围
          </el-button>
        </div>

        <el-table :data="scopeList" border stripe v-loading="scopeLoading" max-height="400">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="scopeTypeName" label="范围类型" width="120" />
          <el-table-column prop="scopeValue" label="范围值" min-width="300">
            <template slot-scope="scope">
              <el-tag
                v-for="(value, index) in scope.row.scopeValueList"
                :key="index"
                style="margin-right: 5px; margin-bottom: 5px"
                closable
                @close="handleRemoveScopeValue(scope.$index, index)"
              >
                {{ value }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleEditScopeItem(scope.row, scope.$index)">编辑</el-button>
              <el-button size="mini" type="danger" @click="handleDeleteScopeItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="scopeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveScope" :loading="scopeSaveLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 添加/编辑范围项对话框 -->
    <el-dialog
      :title="scopeItemDialogTitle"
      :visible.sync="scopeItemDialogVisible"
      width="600px"
      append-to-body
      @close="handleScopeItemDialogClose"
    >
      <el-form :model="scopeItemForm" :rules="scopeItemRules" ref="scopeItemFormRef" label-width="100px">
        <el-form-item label="范围类型" prop="scopeType">
          <el-select v-model="scopeItemForm.scopeType" placeholder="请选择范围类型" style="width: 100%">
            <el-option label="部门" value="DEPARTMENT" />
            <el-option label="项目" value="PROJECT" />
            <el-option label="费用类型" value="EXPENSE_TYPE" />
          </el-select>
        </el-form-item>
        <el-form-item label="范围值" prop="scopeValue">
          <el-input
            v-model="scopeItemForm.scopeValue"
            type="textarea"
            :rows="4"
            placeholder="请输入范围值，多个值用逗号分隔，如：DEPT001,DEPT002"
          />
          <div style="color: #909399; font-size: 12px; margin-top: 5px">
            提示：多个值用英文逗号分隔
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="scopeItemDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmScopeItem">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetControlRuleApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'BudgetControlRule',
  data() {
    return {
      loading: false,
      saveLoading: false,
      checkLoading: false,
      copyLoading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        ruleName: '',
        controlType: '',
        isEnabled: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增预算管控规则',
      formData: {
        ruleId: null,
        ruleCode: '',
        ruleName: '',
        controlType: '',
        warningThreshold: 80,
        controlThreshold: 100,
        effectiveDate: '',
        expiryDate: '',
        isEnabled: true,
        description: ''
      },
      formRules: {
        ruleCode: [
          { required: true, message: '请输入规则编码', trigger: 'blur' }
        ],
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' }
        ],
        controlType: [
          { required: true, message: '请选择控制类型', trigger: 'change' }
        ],
        warningThreshold: [
          { required: true, message: '请输入预警阈值', trigger: 'blur' }
        ],
        controlThreshold: [
          { required: true, message: '请输入控制阈值', trigger: 'blur' }
        ]
      },
      checkDialogVisible: false,
      checkForm: {
        checkData: ''
      },
      currentCheckRow: null,
      copyDialogVisible: false,
      copyForm: {
        ruleName: '',
        ruleCode: ''
      },
      currentCopyRow: null,
      // 适用范围相关
      scopeDialogVisible: false,
      scopeLoading: false,
      scopeSaveLoading: false,
      currentScopeRule: {},
      scopeList: [],
      scopeItemDialogVisible: false,
      scopeItemDialogTitle: '添加范围',
      currentScopeItemIndex: -1,
      scopeItemForm: {
        scopeType: '',
        scopeValue: ''
      },
      scopeItemRules: {
        scopeType: [
          { required: true, message: '请选择范围类型', trigger: 'change' }
        ],
        scopeValue: [
          { required: true, message: '请输入范围值', trigger: 'blur' }
        ]
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
        const response = await budgetControlRuleApi.getList(params)
        if (response.code === 1) {
          // 转换枚举值为中文显示
          this.tableData = (response.data.tlist || []).map(item => ({
            ...item,
            controlTypeName: this.getControlTypeText(item.controlType)
          }))
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
    getControlTypeText(type) {
      const typeMap = {
        'DEPARTMENT_MONTHLY': '部门月度控制',
        'DEPARTMENT_YEARLY': '部门年度控制',
        'PROJECT': '项目控制',
        'EXPENSE_TYPE': '费用类型控制'
      }
      return typeMap[type] || type
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
      this.dialogTitle = '新增预算管控规则'
      this.formData = {
        ruleId: null,
        ruleCode: '',
        ruleName: '',
        controlType: '',
        warningThreshold: 80,
        controlThreshold: 100,
        effectiveDate: '',
        expiryDate: '',
        isEnabled: true,
        description: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑预算管控规则'
      // 字段映射和类型转换
      this.formData = {
        ...row,
        description: row.remark || '',  // 后端 remark 映射到前端 description
        isEnabled: row.isEnabled === 1  // 整数转布尔值
      }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true

        // 转换数据格式和字段映射
        const saveData = {
          ...this.formData,
          isEnabled: this.formData.isEnabled ? 1 : 0,  // 布尔值转整数
          remark: this.formData.description  // 前端 description 映射到后端 remark
        }
        // 删除前端专用字段
        delete saveData.description

        const response = await budgetControlRuleApi.save(saveData)
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
        await this.$confirm('确定要删除该预算管控规则吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await budgetControlRuleApi.delete(row.ruleId)
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
        await this.$confirm('确定要删除选中的预算管控规则吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const ruleIds = this.multipleSelection.map(item => item.ruleId)
        const response = await budgetControlRuleApi.batchDelete(ruleIds)
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
        const response = await budgetControlRuleApi.updateStatus(row.ruleId, !row.isEnabled)
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
    async handleViewScope(row) {
      // 打开适用范围对话框
      this.currentScopeRule = { ...row }
      this.scopeDialogVisible = true
      this.loadScopeData(row.ruleId)
    },
    // 加载适用范围数据
    async loadScopeData(ruleId) {
      this.scopeLoading = true
      try {
        const response = await budgetControlRuleApi.getScope(ruleId)
        if (response.code === 1) {
          // 处理返回的数据，将scopeValue字符串转换为数组
          this.scopeList = (response.data || []).map(item => ({
            ...item,
            scopeTypeName: this.getScopeTypeText(item.scopeType),
            scopeValueList: item.scopeValue ? item.scopeValue.split(',').map(v => v.trim()) : []
          }))
        } else {
          this.$message.error(response.msg || '加载适用范围失败')
        }
      } catch (error) {
        this.$message.error('加载适用范围失败：' + error.message)
      } finally {
        this.scopeLoading = false
      }
    },
    // 获取范围类型文本
    getScopeTypeText(type) {
      const typeMap = {
        'DEPARTMENT': '部门',
        'PROJECT': '项目',
        'EXPENSE_TYPE': '费用类型'
      }
      return typeMap[type] || type
    },
    // 添加范围项
    handleAddScopeItem() {
      this.scopeItemDialogTitle = '添加范围'
      this.currentScopeItemIndex = -1
      this.scopeItemForm = {
        scopeType: '',
        scopeValue: ''
      }
      this.scopeItemDialogVisible = true
    },
    // 编辑范围项
    handleEditScopeItem(row, index) {
      this.scopeItemDialogTitle = '编辑范围'
      this.currentScopeItemIndex = index
      this.scopeItemForm = {
        scopeType: row.scopeType,
        scopeValue: row.scopeValue
      }
      this.scopeItemDialogVisible = true
    },
    // 删除范围项
    handleDeleteScopeItem(index) {
      this.$confirm('确定要删除该适用范围吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.scopeList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    // 移除范围值标签
    handleRemoveScopeValue(scopeIndex, valueIndex) {
      this.scopeList[scopeIndex].scopeValueList.splice(valueIndex, 1)
      // 更新scopeValue字符串
      this.scopeList[scopeIndex].scopeValue = this.scopeList[scopeIndex].scopeValueList.join(',')
    },
    // 确认添加/编辑范围项
    handleConfirmScopeItem() {
      this.$refs.scopeItemFormRef.validate(valid => {
        if (valid) {
          const scopeData = {
            scopeType: this.scopeItemForm.scopeType,
            scopeValue: this.scopeItemForm.scopeValue.trim(),
            scopeTypeName: this.getScopeTypeText(this.scopeItemForm.scopeType),
            scopeValueList: this.scopeItemForm.scopeValue.split(',').map(v => v.trim()).filter(v => v)
          }

          if (this.currentScopeItemIndex >= 0) {
            // 编辑模式
            this.$set(this.scopeList, this.currentScopeItemIndex, scopeData)
            this.$message.success('修改成功')
          } else {
            // 添加模式
            this.scopeList.push(scopeData)
            this.$message.success('添加成功')
          }

          this.scopeItemDialogVisible = false
        }
      })
    },
    // 保存适用范围
    async handleSaveScope() {
      if (this.scopeList.length === 0) {
        this.$message.warning('请至少添加一个适用范围')
        return
      }

      this.scopeSaveLoading = true
      try {
        // 转换数据格式，只保留必要字段
        const scopes = this.scopeList.map(item => ({
          scopeType: item.scopeType,
          scopeValue: item.scopeValue
        }))

        const response = await budgetControlRuleApi.saveScope(this.currentScopeRule.ruleId, scopes)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.scopeDialogVisible = false
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.scopeSaveLoading = false
      }
    },
    // 关闭适用范围对话框
    handleScopeDialogClose() {
      this.scopeList = []
      this.currentScopeRule = {}
    },
    // 关闭范围项对话框
    handleScopeItemDialogClose() {
      this.$refs.scopeItemFormRef.resetFields()
    },
    handleCheck(row) {
      this.currentCheckRow = row
      this.checkForm = {
        checkData: '{\n  "departmentId": "DEPT001",\n  "amount": 50000\n}'
      }
      this.checkDialogVisible = true
    },
    async handleConfirmCheck() {
      try {
        this.checkLoading = true
        let checkData
        try {
          checkData = JSON.parse(this.checkForm.checkData)
        } catch (e) {
          this.$message.error('检查数据格式错误，请输入有效的JSON格式')
          return
        }
        
        const response = await budgetControlRuleApi.check(this.currentCheckRow.ruleId, checkData)
        if (response.code === 1) {
          const result = response.data
          this.$alert(
            `检查结果：${result.checkResult}\n检查消息：${result.checkMessage}\n预算金额：${result.budgetAmount}\n已用金额：${result.usedAmount}\n剩余金额：${result.remainAmount}\n使用率：${result.usageRate}%`,
            '检查结果',
            { type: result.checkResult === 'PASS' ? 'success' : 'warning' }
          )
          this.checkDialogVisible = false
        } else {
          this.$message.error(response.msg || '检查失败')
        }
      } catch (error) {
        this.$message.error('检查失败：' + error.message)
      } finally {
        this.checkLoading = false
      }
    },
    handleCopy(row) {
      this.currentCopyRow = row
      this.copyForm = {
        ruleName: row.ruleName + '_副本',
        ruleCode: row.ruleCode + '_COPY'
      }
      this.copyDialogVisible = true
    },
    async handleConfirmCopy() {
      try {
        await this.$refs.copyFormRef.validate()
        this.copyLoading = true
        const response = await budgetControlRuleApi.copy(this.currentCopyRow.ruleId, this.copyForm)
        if (response.code === 1) {
          this.$message.success('复制成功')
          this.copyDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '复制失败')
        }
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      } finally {
        this.copyLoading = false
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
    }
  }
}
</script>

<style scoped>
.budget-control-rule-container {
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

.scope-list-container {
  margin-bottom: 20px;
}

.scope-toolbar {
  margin-bottom: 15px;
}
</style>
