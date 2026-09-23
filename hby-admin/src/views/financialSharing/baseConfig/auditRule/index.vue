<template>
  <div class="audit-rule-container">
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
        <el-form-item label="规则类型" prop="ruleType">
          <el-select
            v-model="searchForm.ruleType"
            placeholder="请选择规则类型"
            clearable
            style="width: 150px"
          >
            <el-option label="金额校验" value="AMOUNT_CHECK" />
            <el-option label="重复检查" value="DUPLICATE_CHECK" />
            <el-option label="发票校验" value="INVOICE_CHECK" />
            <el-option label="时间校验" value="TIME_CHECK" />
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
        <el-table-column prop="ruleTypeName" label="规则类型" width="120" />
        <el-table-column prop="priority" label="优先级" width="80" />
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
            <el-button size="mini" type="info" @click="handleViewConditions(scope.row)">条件</el-button>
            <el-button size="mini" type="success" @click="handleTest(scope.row)">测试</el-button>
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
            <el-form-item label="规则类型" prop="ruleType">
              <el-select v-model="formData.ruleType" placeholder="请选择规则类型" style="width: 100%">
                <el-option label="金额校验" value="AMOUNT_CHECK" />
                <el-option label="重复检查" value="DUPLICATE_CHECK" />
                <el-option label="发票校验" value="INVOICE_CHECK" />
                <el-option label="时间校验" value="TIME_CHECK" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-input-number v-model="formData.priority" :min="1" :max="100" style="width: 100%" />
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
        <el-form-item label="规则表达式" prop="ruleExpression">
          <el-input
            v-model="formData.ruleExpression"
            type="textarea"
            :rows="3"
            placeholder="请输入规则表达式，如：invoice.amount <= 5000"
          />
        </el-form-item>
        <el-form-item label="警告消息" prop="warningMessage">
          <el-input v-model="formData.warningMessage" placeholder="请输入警告消息" />
        </el-form-item>
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

    <!-- 测试规则对话框 -->
    <el-dialog
      title="测试稽核规则"
      :visible.sync="testDialogVisible"
      width="600px"
    >
      <el-form :model="testForm" ref="testFormRef" label-width="120px">
        <el-form-item label="测试数据" prop="testData">
          <el-input
            v-model="testForm.testData"
            type="textarea"
            :rows="8"
            placeholder="请输入JSON格式的测试数据，如：{&quot;amount&quot;: 3000, &quot;invoiceType&quot;: &quot;VAT_INVOICE&quot;}"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmTest" :loading="testLoading">执行测试</el-button>
      </div>
    </el-dialog>

    <!-- 复制对话框 -->
    <el-dialog
      title="复制稽核规则"
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

    <!-- 条件管理对话框 -->
    <conditions-dialog
      :visible.sync="conditionsDialogVisible"
      :rule-id="currentRuleId"
      :rule-name="currentRuleName"
      @close="handleConditionsDialogClose"
    />
  </div>
</template>

<script>
import { auditRuleApi } from '@/api/financialSharing/baseConfig'
import ConditionsDialog from './components/ConditionsDialog.vue'

export default {
  name: 'AuditRule',
  components: {
    ConditionsDialog
  },
  data() {
    return {
      loading: false,
      saveLoading: false,
      testLoading: false,
      copyLoading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        ruleName: '',
        ruleType: '',
        isEnabled: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增稽核规则',
      formData: {
        ruleId: null,
        ruleCode: '',
        ruleName: '',
        ruleType: '',
        priority: 1,
        effectiveDate: '',
        expiryDate: '',
        ruleExpression: '',
        warningMessage: '',
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
        ruleType: [
          { required: true, message: '请选择规则类型', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请输入优先级', trigger: 'blur' }
        ],
        ruleExpression: [
          { required: true, message: '请输入规则表达式', trigger: 'blur' }
        ]
      },
      testDialogVisible: false,
      testForm: {
        testData: ''
      },
      currentTestRow: null,
      copyDialogVisible: false,
      copyForm: {
        ruleName: '',
        ruleCode: ''
      },
      currentCopyRow: null,
      conditionsDialogVisible: false,
      currentRuleId: '',
      currentRuleName: ''
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    // 规则类型枚举转换
    getRuleTypeText(ruleType) {
      const typeMap = {
        'AMOUNT_CHECK': '金额校验',
        'DUPLICATE_CHECK': '重复检查',
        'INVOICE_CHECK': '发票校验',
        'TIME_CHECK': '时间校验',
        'OTHER': '其他'
      }
      return typeMap[ruleType] || ruleType
    },

    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await auditRuleApi.getList(params)
        if (response.code === 1) {
          // 转换枚举值为中文显示
          this.tableData = (response.data.tlist || []).map(item => ({
            ...item,
            ruleTypeName: this.getRuleTypeText(item.ruleType)
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
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },
    handleAdd() {
      this.dialogTitle = '新增稽核规则'
      this.formData = {
        ruleId: null,
        ruleCode: '',
        ruleName: '',
        ruleType: '',
        priority: 1,
        effectiveDate: '',
        expiryDate: '',
        ruleExpression: '',
        warningMessage: '',
        isEnabled: true,
        description: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑稽核规则'
      // 转换数据格式：整数转布尔值，remark映射为description
      this.formData = {
        ...row,
        isEnabled: row.isEnabled === 1,
        description: row.remark || '' // 后端的remark映射为前端的description
      }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true

        // 转换数据格式：布尔值转整数，添加必填字段，description映射为remark
        const saveData = {
          ...this.formData,
          isEnabled: this.formData.isEnabled ? 1 : 0,
          ruleAction: this.formData.ruleAction || 'WARNING', // 默认为警告
          remark: this.formData.description || '' // 前端的description映射为后端的remark
        }

        // 删除前端特有的字段
        delete saveData.description
        delete saveData.ruleTypeName

        const response = await auditRuleApi.save(saveData)
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
        await this.$confirm('确定要删除该稽核规则吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await auditRuleApi.delete(row.ruleId)
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
        await this.$confirm('确定要删除选中的稽核规则吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const ruleIds = this.multipleSelection.map(item => item.ruleId)
        const response = await auditRuleApi.batchDelete(ruleIds)
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
        // 将布尔值转换为整数：当前是启用(1)则改为禁用(0)，当前是禁用(0)则改为启用(1)
        const newStatus = row.isEnabled === 1 ? 0 : 1
        const response = await auditRuleApi.updateStatus(row.ruleId, newStatus)
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
    handleViewConditions(row) {
      // 打开条件管理对话框
      this.currentRuleId = row.ruleId
      this.currentRuleName = row.ruleName
      this.conditionsDialogVisible = true
    },
    handleConditionsDialogClose() {
      this.conditionsDialogVisible = false
      this.currentRuleId = ''
      this.currentRuleName = ''
    },
    handleTest(row) {
      this.currentTestRow = row
      this.testForm = {
        testData: '{\n  "amount": 3000,\n  "invoiceType": "VAT_INVOICE"\n}'
      }
      this.testDialogVisible = true
    },
    async handleConfirmTest() {
      try {
        this.testLoading = true
        let testData
        try {
          testData = JSON.parse(this.testForm.testData)
        } catch (e) {
          this.$message.error('测试数据格式错误，请输入有效的JSON格式')
          return
        }
        
        const response = await auditRuleApi.test(this.currentTestRow.ruleId, testData)
        if (response.code === 1) {
          const result = response.data
          this.$alert(
            `测试结果：${result.testResult}\n测试消息：${result.testMessage}\n执行时间：${result.executionTime}ms`,
            '测试结果',
            { type: result.testResult === 'PASS' ? 'success' : 'warning' }
          )
          this.testDialogVisible = false
        } else {
          this.$message.error(response.msg || '测试失败')
        }
      } catch (error) {
        this.$message.error('测试失败：' + error.message)
      } finally {
        this.testLoading = false
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
        const response = await auditRuleApi.copy(this.currentCopyRow.ruleId, this.copyForm)
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
.audit-rule-container {
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
