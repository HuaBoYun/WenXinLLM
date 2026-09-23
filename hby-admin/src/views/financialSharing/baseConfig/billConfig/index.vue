<template>
  <div class="bill-config-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="配置名称" prop="configName">
          <el-input
            v-model="searchForm.configName"
            placeholder="请输入配置名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="账单类型" prop="billType">
          <el-select
            v-model="searchForm.billType"
            placeholder="请选择账单类型"
            clearable
            style="width: 150px"
          >
            <el-option label="发票" value="INVOICE" />
            <el-option label="行程单" value="ITINERARY" />
            <el-option label="火车票" value="TRAIN_TICKET" />
            <el-option label="飞机票" value="FLIGHT_TICKET" />
            <el-option label="出租车票" value="TAXI_RECEIPT" />
            <el-option label="住宿费" value="ACCOMMODATION" />
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
        <el-table-column prop="configCode" label="配置编码" width="150" />
        <el-table-column prop="configName" label="配置名称" min-width="200" />
        <el-table-column prop="billTypeName" label="账单类型" width="120" />
        <el-table-column prop="ocrEnabled" label="OCR识别" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.ocrEnabled ? 'success' : 'info'">
              {{ scope.row.ocrEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditEnabled" label="智能稽核" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.auditEnabled ? 'success' : 'info'">
              {{ scope.row.auditEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="autoMatchEnabled" label="自动匹配" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.autoMatchEnabled ? 'success' : 'info'">
              {{ scope.row.autoMatchEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="duplicateCheckEnabled" label="重复检查" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.duplicateCheckEnabled ? 'success' : 'info'">
              {{ scope.row.duplicateCheckEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'">
              {{ scope.row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orgName" label="所属组织" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="400" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="info" @click="handleFieldMapping(scope.row)">字段映射</el-button>
            <el-button size="mini" type="success" @click="handleAuditRules(scope.row)">稽核规则</el-button>
            <el-button size="mini" type="warning" @click="handleTest(scope.row)">测试</el-button>
            <el-button size="mini" type="primary" @click="handleCopy(scope.row)">复制</el-button>
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
            <el-form-item label="配置编码" prop="configCode">
              <el-input v-model="formData.configCode" placeholder="请输入配置编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="配置名称" prop="configName">
              <el-input v-model="formData.configName" placeholder="请输入配置名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账单类型" prop="billType">
              <el-select v-model="formData.billType" placeholder="请选择账单类型" style="width: 100%">
                <el-option label="发票" value="INVOICE" />
                <el-option label="行程单" value="ITINERARY" />
                <el-option label="火车票" value="TRAIN_TICKET" />
                <el-option label="飞机票" value="FLIGHT_TICKET" />
                <el-option label="出租车票" value="TAXI_RECEIPT" />
                <el-option label="住宿费" value="ACCOMMODATION" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="OCR提供商" prop="ocrProvider">
              <el-select v-model="formData.ocrProvider" placeholder="请选择OCR提供商" style="width: 100%">
                <el-option label="百度OCR" value="BAIDU" />
                <el-option label="腾讯OCR" value="TENCENT" />
                <el-option label="阿里OCR" value="ALIBABA" />
                <el-option label="华为OCR" value="HUAWEI" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="OCR识别">
              <el-switch v-model="formData.ocrEnabled" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="智能稽核">
              <el-switch v-model="formData.auditEnabled" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="自动匹配">
              <el-switch v-model="formData.autoMatchEnabled" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="重复检查">
              <el-switch v-model="formData.duplicateCheckEnabled" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="formData.isEnabled" />
        </el-form-item>
        <el-form-item label="配置描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入配置描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 测试对话框 -->
    <el-dialog
      title="账单配置测试"
      :visible.sync="testDialogVisible"
      width="600px"
    >
      <el-form :model="testForm" ref="testFormRef" label-width="120px">
        <el-form-item label="测试数据" prop="testData">
          <el-input
            v-model="testForm.testData"
            type="textarea"
            :rows="8"
            placeholder="请输入JSON格式的测试数据，如：{&quot;billNumber&quot;: &quot;INV001&quot;, &quot;amount&quot;: 1000}"
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
      title="复制账单配置"
      :visible.sync="copyDialogVisible"
      width="500px"
    >
      <el-form :model="copyForm" ref="copyFormRef" label-width="120px">
        <el-form-item label="新配置名称" prop="configName" :rules="[{required: true, message: '请输入新配置名称'}]">
          <el-input v-model="copyForm.configName" placeholder="请输入新配置名称" />
        </el-form-item>
        <el-form-item label="新配置编码" prop="configCode" :rules="[{required: true, message: '请输入新配置编码'}]">
          <el-input v-model="copyForm.configCode" placeholder="请输入新配置编码" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmCopy" :loading="copyLoading">确定复制</el-button>
      </div>
    </el-dialog>

    <!-- 字段映射对话框 -->
    <el-dialog
      :title="`字段映射 - ${currentMappingRow ? currentMappingRow.configName : ''}`"
      :visible.sync="fieldMappingDialogVisible"
      width="1000px"
      @close="fieldMappingData = []"
    >
      <div style="margin-bottom: 15px;">
        <el-button type="primary" size="small" @click="handleAddFieldMapping">新增映射</el-button>
      </div>

      <el-table
        :data="fieldMappingData"
        v-loading="fieldMappingLoading"
        border
        stripe
      >
        <el-table-column prop="sourceField" label="源字段" width="150" />
        <el-table-column prop="targetField" label="目标字段" width="150" />
        <el-table-column prop="transformRule" label="转换规则" min-width="200" show-overflow-tooltip />
        <el-table-column prop="defaultValue" label="默认值" width="120" />
        <el-table-column prop="isRequired" label="是否必填" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isRequired ? 'success' : 'info'" size="small">
              {{ scope.row.isRequired ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEditFieldMapping(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDeleteFieldMapping(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="fieldMappingDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 字段映射表单对话框 -->
    <el-dialog
      :title="fieldMappingFormTitle"
      :visible.sync="fieldMappingFormVisible"
      width="600px"
    >
      <el-form
        :model="fieldMappingForm"
        :rules="fieldMappingRules"
        ref="fieldMappingFormRef"
        label-width="120px"
      >
        <el-form-item label="源字段" prop="sourceField">
          <el-input v-model="fieldMappingForm.sourceField" placeholder="请输入源字段名称，如：billNumber" />
        </el-form-item>
        <el-form-item label="目标字段" prop="targetField">
          <el-input v-model="fieldMappingForm.targetField" placeholder="请输入目标字段名称，如：bill_no" />
        </el-form-item>
        <el-form-item label="转换规则">
          <el-input
            v-model="fieldMappingForm.transformRule"
            type="textarea"
            :rows="3"
            placeholder="请输入转换规则，如：value.toUpperCase()"
          />
        </el-form-item>
        <el-form-item label="默认值">
          <el-input v-model="fieldMappingForm.defaultValue" placeholder="请输入默认值" />
        </el-form-item>
        <el-form-item label="是否必填">
          <el-switch v-model="fieldMappingForm.isRequired" />
        </el-form-item>
        <el-form-item label="排序号">
          <el-input-number v-model="fieldMappingForm.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="fieldMappingForm.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fieldMappingFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveFieldMapping">保存</el-button>
      </div>
    </el-dialog>

    <!-- 稽核规则对话框 -->
    <el-dialog
      :title="`稽核规则 - ${currentAuditRulesRow ? currentAuditRulesRow.configName : ''}`"
      :visible.sync="auditRulesDialogVisible"
      width="1200px"
      @close="auditRulesData = []"
    >
      <div style="margin-bottom: 15px;">
        <el-button type="primary" size="small" @click="handleAddAuditRule">新增规则</el-button>
      </div>

      <el-table
        :data="auditRulesData"
        v-loading="auditRulesLoading"
        border
        stripe
      >
        <el-table-column prop="ruleName" label="规则名称" width="150" />
        <el-table-column prop="ruleType" label="规则类型" width="120">
          <template slot-scope="scope">
            {{ getRuleTypeText(scope.row.ruleType) }}
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" />
        <el-table-column prop="ruleExpression" label="规则表达式" min-width="200" show-overflow-tooltip />
        <el-table-column prop="warningMessage" label="警告消息" width="150" show-overflow-tooltip />
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'info'" size="small">
              {{ scope.row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEditAuditRule(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDeleteAuditRule(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="auditRulesDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 稽核规则表单对话框 -->
    <el-dialog
      :title="auditRulesFormTitle"
      :visible.sync="auditRulesFormVisible"
      width="600px"
      append-to-body
    >
      <el-form
        :model="auditRulesForm"
        :rules="auditRulesRules"
        ref="auditRulesFormRef"
        label-width="100px"
      >
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="auditRulesForm.ruleName" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="规则类型" prop="ruleType">
          <el-select v-model="auditRulesForm.ruleType" placeholder="请选择规则类型" style="width: 100%">
            <el-option
              v-for="item in ruleTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-input-number v-model="auditRulesForm.priority" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="规则表达式" prop="ruleExpression">
          <el-input
            v-model="auditRulesForm.ruleExpression"
            type="textarea"
            :rows="3"
            placeholder="请输入规则表达式，如：amount > 5000"
          />
        </el-form-item>
        <el-form-item label="警告消息">
          <el-input
            v-model="auditRulesForm.warningMessage"
            placeholder="请输入警告消息"
          />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="auditRulesForm.isEnabled" />
        </el-form-item>
        <el-form-item label="规则描述">
          <el-input
            v-model="auditRulesForm.description"
            type="textarea"
            :rows="2"
            placeholder="请输入规则描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="auditRulesFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveAuditRule">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { billConfigApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'BillConfig',
  data() {
    return {
      loading: false,
      saveLoading: false,
      testLoading: false,
      copyLoading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        configName: '',
        billType: '',
        isEnabled: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增账单配置',
      formData: {
        configId: null,
        configCode: '',
        configName: '',
        billType: '',
        ocrProvider: '',
        ocrEnabled: true,
        auditEnabled: true,
        autoMatchEnabled: true,
        duplicateCheckEnabled: true,
        isEnabled: true,
        description: ''
      },
      formRules: {
        configCode: [
          { required: true, message: '请输入配置编码', trigger: 'blur' }
        ],
        configName: [
          { required: true, message: '请输入配置名称', trigger: 'blur' }
        ],
        billType: [
          { required: true, message: '请选择账单类型', trigger: 'change' }
        ]
      },
      testDialogVisible: false,
      testForm: {
        testData: ''
      },
      currentTestRow: null,
      copyDialogVisible: false,
      copyForm: {
        configName: '',
        configCode: ''
      },
      currentCopyRow: null,
      // 账单类型映射
      billTypeMap: {
        'INVOICE': '发票',
        'ITINERARY': '行程单',
        'TRAIN_TICKET': '火车票',
        'FLIGHT_TICKET': '飞机票',
        'TAXI_RECEIPT': '出租车票',
        'ACCOMMODATION': '住宿费'
      },
      // 字段映射相关
      fieldMappingDialogVisible: false,
      currentMappingRow: null,
      fieldMappingLoading: false,
      fieldMappingData: [],
      fieldMappingForm: {
        mappingId: null,
        sourceField: '',
        targetField: '',
        transformRule: '',
        defaultValue: '',
        isRequired: false,
        sortOrder: 0,
        remark: ''
      },
      fieldMappingFormVisible: false,
      fieldMappingFormTitle: '新增字段映射',
      fieldMappingRules: {
        sourceField: [
          { required: true, message: '请输入源字段', trigger: 'blur' }
        ],
        targetField: [
          { required: true, message: '请输入目标字段', trigger: 'blur' }
        ]
      },
      // 稽核规则相关
      auditRulesDialogVisible: false,
      currentAuditRulesRow: null,
      auditRulesLoading: false,
      auditRulesData: [],
      auditRulesForm: {
        ruleId: null,
        ruleName: '',
        ruleType: '',
        priority: 1,
        ruleExpression: '',
        warningMessage: '',
        isEnabled: true,
        description: ''
      },
      auditRulesFormVisible: false,
      auditRulesFormTitle: '新增稽核规则',
      auditRulesRules: {
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' }
        ],
        ruleType: [
          { required: true, message: '请选择规则类型', trigger: 'change' }
        ],
        ruleExpression: [
          { required: true, message: '请输入规则表达式', trigger: 'blur' }
        ]
      },
      ruleTypeOptions: [
        { label: '金额校验', value: 'AMOUNT_CHECK' },
        { label: '重复检查', value: 'DUPLICATE_CHECK' },
        { label: '发票校验', value: 'INVOICE_CHECK' },
        { label: '时间校验', value: 'TIME_CHECK' },
        { label: '其他', value: 'OTHER' }
      ]
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
        const response = await billConfigApi.getList(params)
        if (response.code === 1) {
          // 添加 billTypeName 字段用于显示
          this.tableData = (response.data.tlist || []).map(item => ({
            ...item,
            billTypeName: this.billTypeMap[item.billType] || item.billType
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
      this.dialogTitle = '新增账单配置'
      this.formData = {
        configId: null,
        configCode: '',
        configName: '',
        billType: '',
        ocrProvider: '',
        ocrEnabled: true,
        auditEnabled: true,
        autoMatchEnabled: true,
        duplicateCheckEnabled: true,
        isEnabled: true,
        description: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑账单配置'
      // 转换 Integer 为 Boolean (1 -> true, 0 -> false)
      this.formData = {
        ...row,
        ocrEnabled: row.ocrEnabled === 1,
        auditEnabled: row.auditEnabled === 1,
        autoMatchEnabled: row.autoMatchEnabled === 1,
        duplicateCheckEnabled: row.duplicateCheckEnabled === 1,
        isEnabled: row.isEnabled === 1
      }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true

        // 转换 Boolean 为 Integer (true -> 1, false -> 0)
        const submitData = {
          ...this.formData,
          ocrEnabled: this.formData.ocrEnabled ? 1 : 0,
          auditEnabled: this.formData.auditEnabled ? 1 : 0,
          autoMatchEnabled: this.formData.autoMatchEnabled ? 1 : 0,
          duplicateCheckEnabled: this.formData.duplicateCheckEnabled ? 1 : 0,
          isEnabled: this.formData.isEnabled ? 1 : 0
        }

        const response = await billConfigApi.save(submitData)
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
        await this.$confirm('确定要删除该账单配置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await billConfigApi.delete(row.configId)
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
        await this.$confirm('确定要删除选中的账单配置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const configIds = this.multipleSelection.map(item => item.configId)
        const response = await billConfigApi.batchDelete(configIds)
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
        const response = await billConfigApi.updateStatus(row.configId, !row.isEnabled)
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
    handleFieldMapping(row) {
      this.currentMappingRow = row
      this.fieldMappingDialogVisible = true
      this.loadFieldMappings(row.configId)
    },
    handleAuditRules(row) {
      this.currentAuditRulesRow = row
      this.auditRulesDialogVisible = true
      this.loadAuditRules(row.configId)
    },
    handleTest(row) {
      this.currentTestRow = row
      this.testForm = {
        testData: '{\n  "billNumber": "INV001",\n  "amount": 1000,\n  "billDate": "2024-12-19"\n}'
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
        
        const response = await billConfigApi.test(this.currentTestRow.configId, testData)
        if (response.code === 1) {
          const result = response.data
          this.$alert(
            `测试结果：${result.testResult}\n测试消息：${result.testMessage}\nOCR结果：${result.ocrResult}\n稽核结果：${result.auditResult}\n匹配结果：${result.matchResult}`,
            '测试结果',
            { type: result.testResult === 'SUCCESS' ? 'success' : 'warning' }
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
        configName: row.configName + '_副本',
        configCode: row.configCode + '_COPY'
      }
      this.copyDialogVisible = true
    },
    async handleConfirmCopy() {
      try {
        await this.$refs.copyFormRef.validate()
        this.copyLoading = true
        const response = await billConfigApi.copy(this.currentCopyRow.configId, this.copyForm)
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
    },
    // 字段映射相关方法
    async loadFieldMappings(configId) {
      this.fieldMappingLoading = true
      try {
        const response = await billConfigApi.getFieldMapping(configId)
        if (response.code === 1) {
          this.fieldMappingData = response.data || []
        } else {
          this.$message.error(response.msg || '加载字段映射失败')
        }
      } catch (error) {
        this.$message.error('加载字段映射失败：' + error.message)
      } finally {
        this.fieldMappingLoading = false
      }
    },
    handleAddFieldMapping() {
      this.fieldMappingFormTitle = '新增字段映射'
      this.fieldMappingForm = {
        mappingId: null,
        sourceField: '',
        targetField: '',
        transformRule: '',
        defaultValue: '',
        isRequired: false,
        sortOrder: this.fieldMappingData.length + 1,
        remark: ''
      }
      this.fieldMappingFormVisible = true
    },
    handleEditFieldMapping(row) {
      this.fieldMappingFormTitle = '编辑字段映射'
      this.fieldMappingForm = {
        ...row,
        isRequired: row.isRequired === 1
      }
      this.fieldMappingFormVisible = true
    },
    async handleSaveFieldMapping() {
      try {
        await this.$refs.fieldMappingFormRef.validate()

        const submitData = {
          ...this.fieldMappingForm,
          configId: this.currentMappingRow.configId,
          isRequired: this.fieldMappingForm.isRequired ? 1 : 0
        }

        const mappings = this.fieldMappingForm.mappingId
          ? this.fieldMappingData.map(item =>
              item.mappingId === this.fieldMappingForm.mappingId ? submitData : item
            )
          : [...this.fieldMappingData, submitData]

        const response = await billConfigApi.saveFieldMapping(
          this.currentMappingRow.configId,
          mappings
        )

        if (response.code === 1) {
          this.$message.success('保存成功')
          this.fieldMappingFormVisible = false
          this.loadFieldMappings(this.currentMappingRow.configId)
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('保存失败：' + error.message)
        }
      }
    },
    async handleDeleteFieldMapping(row) {
      try {
        await this.$confirm('确定要删除该字段映射吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const mappings = this.fieldMappingData.filter(item => item.mappingId !== row.mappingId)
        const response = await billConfigApi.saveFieldMapping(
          this.currentMappingRow.configId,
          mappings
        )

        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadFieldMappings(this.currentMappingRow.configId)
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    // 稽核规则相关方法
    async loadAuditRules(configId) {
      this.auditRulesLoading = true
      try {
        const response = await billConfigApi.getAuditRules(configId)
        if (response.code === 1) {
          this.auditRulesData = response.data || []
        } else {
          this.$message.error(response.msg || '加载稽核规则失败')
        }
      } catch (error) {
        this.$message.error('加载稽核规则失败：' + error.message)
      } finally {
        this.auditRulesLoading = false
      }
    },
    handleAddAuditRule() {
      this.auditRulesFormTitle = '新增稽核规则'
      this.auditRulesForm = {
        ruleId: null,
        ruleName: '',
        ruleType: '',
        priority: this.auditRulesData.length + 1,
        ruleExpression: '',
        warningMessage: '',
        isEnabled: true,
        description: ''
      }
      this.auditRulesFormVisible = true
    },
    handleEditAuditRule(row) {
      this.auditRulesFormTitle = '编辑稽核规则'
      this.auditRulesForm = {
        ...row,
        isEnabled: row.isEnabled === 1 || row.isEnabled === true
      }
      this.auditRulesFormVisible = true
    },
    async handleSaveAuditRule() {
      try {
        await this.$refs.auditRulesFormRef.validate()

        const submitData = {
          ...this.auditRulesForm,
          configId: this.currentAuditRulesRow.configId,
          isEnabled: this.auditRulesForm.isEnabled ? 1 : 0
        }

        const rules = this.auditRulesForm.ruleId
          ? this.auditRulesData.map(item =>
              item.ruleId === this.auditRulesForm.ruleId ? submitData : item
            )
          : [...this.auditRulesData, submitData]

        const response = await billConfigApi.saveAuditRules(
          this.currentAuditRulesRow.configId,
          rules
        )

        if (response.code === 1) {
          this.$message.success('保存成功')
          this.auditRulesFormVisible = false
          this.loadAuditRules(this.currentAuditRulesRow.configId)
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('保存失败：' + error.message)
        }
      }
    },
    async handleDeleteAuditRule(row) {
      try {
        await this.$confirm('确定要删除该稽核规则吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const rules = this.auditRulesData.filter(item => item.ruleId !== row.ruleId)
        const response = await billConfigApi.saveAuditRules(
          this.currentAuditRulesRow.configId,
          rules
        )

        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadAuditRules(this.currentAuditRulesRow.configId)
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    getRuleTypeText(ruleType) {
      const option = this.ruleTypeOptions.find(opt => opt.value === ruleType)
      return option ? option.label : ruleType
    }
  }
}
</script>

<style scoped>
.bill-config-container {
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
