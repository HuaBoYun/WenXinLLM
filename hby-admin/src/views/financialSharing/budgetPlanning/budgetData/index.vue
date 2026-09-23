<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :model="queryForm" :inline="true" label-width="100px">
      <el-form-item label="预算模型">
        <el-select v-model="queryForm.modelId" placeholder="请选择预算模型" clearable filterable @change="handleModelChange">
          <el-option
            v-for="item in modelList"
            :key="item.modelId"
            :label="item.modelName"
            :value="item.modelId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="预算表单">
        <el-select v-model="queryForm.formId" placeholder="请选择预算表单" clearable filterable>
          <el-option
            v-for="item in formList"
            :key="item.formId"
            :label="item.formName"
            :value="item.formId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="预算期间">
        <el-input v-model="queryForm.period" placeholder="请输入预算期间" clearable />
      </el-form-item>
      <el-form-item label="预算版本">
        <el-input v-model="queryForm.version" placeholder="请输入预算版本" clearable />
      </el-form-item>
      <el-form-item label="科目编码">
        <el-input v-model="queryForm.subjectCode" placeholder="请输入科目编码" clearable />
      </el-form-item>
      <el-form-item label="主体编码">
        <el-input v-model="queryForm.organizationCode" placeholder="请输入主体编码" clearable />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
          <el-option label="草稿" value="DRAFT" />
          <el-option label="已提交" value="SUBMITTED" />
          <el-option label="已审批" value="APPROVED" />
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
        <el-button type="success" icon="el-icon-upload2" :disabled="multiple" @click="handleBatchSubmit">批量提交</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" :disabled="multiple" @click="handleBatchDelete">批量删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="预算模型" prop="modelName" width="150" />
      <el-table-column label="预算表单" prop="formName" width="150" />
      <el-table-column label="预算期间" prop="period" width="120" />
      <el-table-column label="预算版本" prop="version" width="120" />
      <el-table-column label="科目编码" prop="subjectCode" width="120" />
      <el-table-column label="主体编码" prop="organizationCode" width="120" />
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'DRAFT'" type="info" size="mini">草稿</el-tag>
          <el-tag v-else-if="scope.row.status === 'SUBMITTED'" type="warning" size="mini">已提交</el-tag>
          <el-tag v-else type="success" size="mini">已审批</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="提交人" prop="submitUserName" width="100">
        <template slot-scope="scope">
          {{ scope.row.submitUserName || scope.row.submitUser || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="提交时间" prop="submitTime" width="160" />
      <el-table-column label="创建时间" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" width="350" fixed="right">
        <template slot-scope="scope">
          <el-button 
            v-if="scope.row.status === 'DRAFT'" 
            size="mini" 
            type="text" 
            icon="el-icon-edit" 
            @click="handleEdit(scope.row)"
          >编辑</el-button>
          <el-button 
            size="mini" 
            type="text" 
            icon="el-icon-view" 
            @click="handleView(scope.row)"
          >查看</el-button>
          <el-button 
            v-if="scope.row.status === 'DRAFT'" 
            size="mini" 
            type="text" 
            icon="el-icon-upload2" 
            @click="handleSubmit(scope.row)"
          >提交</el-button>
          <el-button 
            v-if="scope.row.status === 'SUBMITTED'" 
            size="mini" 
            type="text" 
            icon="el-icon-download" 
            @click="handleWithdraw(scope.row)"
          >撤回</el-button>
          <el-button 
            v-if="scope.row.status === 'SUBMITTED'" 
            size="mini" 
            type="text" 
            icon="el-icon-check" 
            @click="handleApprove(scope.row)"
          >审批</el-button>
          <el-button 
            v-if="scope.row.status === 'DRAFT'" 
            size="mini" 
            type="text" 
            icon="el-icon-delete" 
            @click="handleDelete(scope.row)"
          >删除</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="1000px" @close="handleDialogClose">
      <el-form ref="dataForm" :model="dataForm" :rules="dataRules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="预算模型" prop="modelId">
              <el-select v-model="dataForm.modelId" placeholder="请选择预算模型" style="width: 100%" filterable :disabled="isEdit || isView" @change="handleDataModelChange">
                <el-option
                  v-for="item in modelList"
                  :key="item.modelId"
                  :label="item.modelName"
                  :value="item.modelId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预算表单" prop="formId">
              <el-select v-model="dataForm.formId" placeholder="请选择预算表单" style="width: 100%" filterable :disabled="isEdit || isView" @change="handleFormChange">
                <el-option
                  v-for="item in dialogFormList"
                  :key="item.formId"
                  :label="item.formName"
                  :value="item.formId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预算期间" prop="period">
              <el-input v-model="dataForm.period" placeholder="请输入预算期间" :disabled="isEdit || isView" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="预算版本" prop="version">
              <el-input v-model="dataForm.version" placeholder="请输入预算版本" :disabled="isEdit || isView" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="科目编码" prop="subjectCode">
              <el-input v-model="dataForm.subjectCode" placeholder="请输入科目编码" :disabled="isEdit || isView" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="主体编码" prop="organizationCode">
              <el-input v-model="dataForm.organizationCode" placeholder="请输入主体编码" :disabled="isEdit || isView" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述">
          <el-input v-model="dataForm.description" type="textarea" :rows="2" placeholder="请输入描述" :disabled="isView" />
        </el-form-item>
        
        <!-- 动态表单字段 -->
        <el-divider content-position="left">数据录入</el-divider>
        <div v-if="formFields.length > 0">
          <el-row :gutter="20">
            <el-col v-for="field in formFields" :key="field.fieldCode" :span="8">
              <el-form-item :label="field.fieldName">
                <!-- 文本输入 -->
                <el-input 
                  v-if="field.fieldType === 'text'" 
                  v-model="dataValues[field.fieldCode]" 
                  :placeholder="'请输入' + field.fieldName"
                  :disabled="isView"
                />
                <!-- 数字输入 -->
                <el-input-number 
                  v-else-if="field.fieldType === 'number'" 
                  v-model="dataValues[field.fieldCode]" 
                  style="width: 100%"
                  :disabled="isView"
                />
                <!-- 日期选择 -->
                <el-date-picker 
                  v-else-if="field.fieldType === 'date'" 
                  v-model="dataValues[field.fieldCode]" 
                  type="date" 
                  placeholder="选择日期"
                  style="width: 100%"
                  :disabled="isView"
                />
                <!-- 下拉选择 -->
                <el-select 
                  v-else-if="field.fieldType === 'select'" 
                  v-model="dataValues[field.fieldCode]" 
                  :placeholder="'请选择' + field.fieldName"
                  style="width: 100%"
                  :disabled="isView"
                >
                  <el-option
                    v-for="option in field.options"
                    :key="option.value"
                    :label="option.label"
                    :value="option.value"
                  />
                </el-select>
                <!-- 复选框 -->
                <el-checkbox 
                  v-else-if="field.fieldType === 'checkbox'" 
                  v-model="dataValues[field.fieldCode]"
                  :disabled="isView"
                >{{ field.fieldName }}</el-checkbox>
                <!-- 多行文本 -->
                <el-input 
                  v-else-if="field.fieldType === 'textarea'" 
                  v-model="dataValues[field.fieldCode]" 
                  type="textarea" 
                  :rows="2"
                  :placeholder="'请输入' + field.fieldName"
                  :disabled="isView"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div v-else>
          <el-empty description="请先选择预算表单" />
        </div>
      </el-form>
      <div v-if="!isView" slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleDataSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 提交审批对话框 -->
    <el-dialog
      title="提交审批"
      :visible.sync="approvalDialogVisible"
      width="600px"
      @close="handleApprovalDialogClose"
    >
      <el-form ref="approvalForm" :model="approvalForm" :rules="approvalRules" label-width="100px">
        <el-form-item label="审批人" prop="approverIds">
          <el-input
            v-model="approvalForm.approverIds"
            placeholder="请输入审批人ID,多个用逗号分隔"
          />
          <span class="form-tip">提示: 多个审批人按顺序审批,用英文逗号分隔</span>
        </el-form-item>
        <el-form-item label="提交说明" prop="comment">
          <el-input
            v-model="approvalForm.comment"
            type="textarea"
            :rows="4"
            placeholder="请输入提交说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="approvalDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="approvalSubmitLoading" @click="handleConfirmSubmitApproval">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDataList, addData, updateData, deleteData, batchDeleteData, submitData, withdrawData, batchSubmitData, getDataById } from '@/api/financialSharing/budgetPlanning/budgetData'
import { submitApproval } from '@/api/financialSharing/budgetPlanning/budgetApproval'
import { getModelListNoPage } from '@/api/financialSharing/budgetPlanning/budgetModel'
import { getFormListNoPage, getFormById } from '@/api/financialSharing/budgetPlanning/budgetForm'
import Pagination from '@/components/Pagination'

export default {
  name: 'BudgetData',
  components: {
    Pagination
  },
  data() {
    return {
      // 查询参数
      queryForm: {
        modelId: '',
        formId: '',
        period: '',
        version: '',
        subjectCode: '',
        organizationCode: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      },
      // 加载状态
      loading: false,
      // 表格数据
      tableData: [],
      // 总记录数
      total: 0,
      // 选中的行
      selectedRows: [],
      // 是否禁用批量操作
      multiple: true,
      // 预算模型列表
      modelList: [],
      // 预算表单列表
      formList: [],
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      isView: false,
      submitLoading: false,
      // 数据表单
      dataForm: {
        dataId: '',
        modelId: '',
        formId: '',
        period: '',
        version: '',
        subjectCode: '',
        organizationCode: '',
        dataValues: '',
        description: ''
      },
      // 表单字段
      formFields: [],
      // 数据值
      dataValues: {},
      // 对话框中的表单列表
      dialogFormList: [],
      // 表单验证规则
      dataRules: {
        modelId: [
          { required: true, message: '请选择预算模型', trigger: 'change' }
        ],
        formId: [
          { required: true, message: '请选择预算表单', trigger: 'change' }
        ],
        period: [
          { required: true, message: '请输入预算期间', trigger: 'blur' }
        ],
        version: [
          { required: true, message: '请输入预算版本', trigger: 'blur' }
        ],
        subjectCode: [
          { required: true, message: '请输入科目编码', trigger: 'blur' }
        ],
        organizationCode: [
          { required: true, message: '请输入主体编码', trigger: 'blur' }
        ]
      },
      // 审批对话框
      approvalDialogVisible: false,
      approvalSubmitLoading: false,
      currentRow: null,
      approvalForm: {
        approverIds: '',
        comment: ''
      },
      approvalRules: {
        approverIds: [
          { required: true, message: '请输入审批人ID', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadModelList()
    // 预加载全量启用的预算表单，使"预算表单"下拉在未选预算模型时也可用
    this.loadFormList()
    this.loadData()
  },
  methods: {
    /** 加载预算模型列表 */
    loadModelList() {
      getModelListNoPage({ status: 'ACTIVE' }).then(res => {
        if (res.code === 1) {
          this.modelList = res.data || []
        }
      })
    },
    /** 模型改变时加载表单列表 */
    handleModelChange() {
      this.queryForm.formId = ''
      this.loadFormList(this.queryForm.modelId)
    },
    /** 加载预算表单列表
     *  - 传入 modelId: 按该模型过滤
     *  - 不传 modelId: 加载全量启用表单（后端 SQL 已对 modelId 做空值判断）
     */
    loadFormList(modelId) {
      const params = { status: 'ACTIVE' }
      if (modelId) {
        params.modelId = modelId
      }
      getFormListNoPage(params).then(res => {
        if (res.code === 1) {
          this.formList = res.data || []
        } else {
          this.formList = []
        }
      }).catch(() => {
        this.formList = []
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
        formId: '',
        period: '',
        version: '',
        subjectCode: '',
        organizationCode: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      }
      // 重置后恢复全量启用表单，保持下拉可用
      this.loadFormList()
      this.loadData()
    },
    /** 加载数据 */
    loadData() {
      this.loading = true
      getDataList(this.queryForm).then(res => {
        this.loading = false
        if (res.code === 1) {
          this.tableData = res.data.list || []
          this.total = res.data.total || 0
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      }).catch(() => {
        this.loading = false
        this.$message.error('查询失败')
      })
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.selectedRows = selection
      this.multiple = !selection.length
    },
    /** 新增按钮 */
    handleAdd() {
      this.resetForm()
      this.dialogTitle = '新增预算数据'
      this.isEdit = false
      this.isView = false
      // 预加载全量启用的预算表单，让"预算表单"下拉在未选预算模型时也可用
      this.loadDialogFormList()
      this.dialogVisible = true
    },
    /** 编辑按钮 */
    handleEdit(row) {
      this.resetForm()
      this.dialogTitle = '编辑预算数据'
      this.isEdit = true
      this.isView = false

      // 填充表单数据
      this.dataForm = {
        dataId: row.dataId,
        modelId: row.modelId,
        formId: row.formId,
        period: row.period,
        version: row.version,
        subjectCode: row.subjectCode,
        organizationCode: row.organizationCode,
        description: row.description
      }

      // 解析数据值
      if (row.dataValues) {
        try {
          this.dataValues = JSON.parse(row.dataValues)
        } catch (e) {
          console.error('解析数据值失败', e)
        }
      }

      // 必须等表单列表加载完成,才能从中查到对应表单的 formConfig 解析字段
      this.loadDialogFormList(row.modelId).then(() => {
        this.loadFormFields(row.formId)
      })

      this.dialogVisible = true
    },
    /** 查看按钮 */
    handleView(row) {
      this.resetForm()
      this.dialogTitle = '查看预算数据'
      this.isEdit = false
      this.isView = true

      // 填充表单数据
      this.dataForm = {
        dataId: row.dataId,
        modelId: row.modelId,
        formId: row.formId,
        period: row.period,
        version: row.version,
        subjectCode: row.subjectCode,
        organizationCode: row.organizationCode,
        description: row.description
      }

      // 解析数据值
      if (row.dataValues) {
        try {
          this.dataValues = JSON.parse(row.dataValues)
        } catch (e) {
          console.error('解析数据值失败', e)
        }
      }

      // 必须等表单列表加载完成,才能从中查到对应表单的 formConfig 解析字段
      this.loadDialogFormList(row.modelId).then(() => {
        this.loadFormFields(row.formId)
      })

      this.dialogVisible = true
    },
    /** 对话框中模型改变 */
    handleDataModelChange() {
      this.dataForm.formId = ''
      this.formFields = []
      this.dataValues = {}
      this.loadDialogFormList(this.dataForm.modelId)
    },
    /** 加载对话框中的表单列表
     *  - 传入 modelId: 按该模型过滤
     *  - 不传 modelId: 加载全量启用表单（后端 SQL 已对 modelId 做空值判断）
     *  - 返回 Promise, 便于调用方在加载完成后衔接后续动作
     */
    loadDialogFormList(modelId) {
      const params = { status: 'ACTIVE' }
      if (modelId) {
        params.modelId = modelId
      }
      return getFormListNoPage(params).then(res => {
        if (res.code === 1) {
          this.dialogFormList = res.data || []
        } else {
          this.dialogFormList = []
        }
      }).catch(() => {
        this.dialogFormList = []
      })
    },
    /** 表单改变时加载字段，并反向同步所属的预算模型，保持数据一致 */
    handleFormChange() {
      this.formFields = []
      // 切换表单时数据值需要重置, 避免遗留前一张表单的字段
      this.dataValues = {}
      if (this.dataForm.formId) {
        const selectedForm = this.dialogFormList.find(f => f.formId === this.dataForm.formId)
        if (selectedForm && selectedForm.modelId) {
          this.dataForm.modelId = selectedForm.modelId
        }
      }
      this.loadFormFields(this.dataForm.formId)
    },
    /** 解析表单配置, 提取字段定义 */
    parseFormConfig(formConfig) {
      if (!formConfig) return []
      try {
        const config = JSON.parse(formConfig)
        return config.fields || []
      } catch (e) {
        console.error('解析表单配置失败', e)
        return []
      }
    },
    /** 加载表单字段
     *  - 优先从已加载的 dialogFormList 取（节省一次请求）
     *  - 列表里没有则远程拉取 (兜底, 消除 dialogFormList 加载完成的隐式依赖)
     */
    loadFormFields(formId) {
      if (!formId) {
        this.formFields = []
        return
      }
      const cached = this.dialogFormList.find(f => f.formId === formId)
      if (cached && cached.formConfig) {
        this.formFields = this.parseFormConfig(cached.formConfig)
        return
      }
      // 兜底: 直接根据 formId 拉取表单详情
      getFormById({ formId }).then(res => {
        if (res.code === 1 && res.data) {
          this.formFields = this.parseFormConfig(res.data.formConfig)
        } else {
          this.formFields = []
        }
      }).catch(() => {
        this.formFields = []
      })
    },
    /** 删除按钮 */
    handleDelete(row) {
      this.$confirm('确认删除该预算数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteData({ dataId: row.dataId }).then(res => {
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    /** 批量删除按钮 */
    handleBatchDelete() {
      const dataIds = this.selectedRows.map(item => item.dataId)
      this.$confirm('确认删除选中的预算数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchDeleteData({ dataIds }).then(res => {
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    /** 提交按钮 */
    handleSubmit(row) {
      this.$confirm('确认提交该预算数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        submitData({ dataId: row.dataId }).then(res => {
          if (res.code === 1) {
            this.$message.success('提交成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '提交失败')
          }
        })
      }).catch(() => {})
    },
    /** 批量提交按钮 */
    handleBatchSubmit() {
      const dataIds = this.selectedRows.map(item => item.dataId)
      this.$confirm('确认提交选中的预算数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchSubmitData({ dataIds }).then(res => {
          if (res.code === 1) {
            this.$message.success('提交成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '提交失败')
          }
        })
      }).catch(() => {})
    },
    /** 撤回按钮 */
    handleWithdraw(row) {
      this.$confirm('确认撤回该预算数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        withdrawData({ dataId: row.dataId }).then(res => {
          if (res.code === 1) {
            this.$message.success('撤回成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '撤回失败')
          }
        })
      }).catch(() => {})
    },
    /** 审批按钮 - 提交审批 */
    handleApprove(row) {
      this.currentRow = row
      this.approvalDialogVisible = true
    },
    /** 确认提交审批 */
    handleConfirmSubmitApproval() {
      this.$refs.approvalForm.validate(valid => {
        if (!valid) return

        this.approvalSubmitLoading = true
        submitApproval({
          dataId: this.currentRow.dataId,
          approverIds: this.approvalForm.approverIds,
          comment: this.approvalForm.comment
        }).then(res => {
          this.approvalSubmitLoading = false
          if (res.code === 1) {
            this.$message.success('提交审批成功')
            this.approvalDialogVisible = false
            this.loadData()
          } else {
            this.$message.error(res.msg || '提交审批失败')
          }
        }).catch(() => {
          this.approvalSubmitLoading = false
          this.$message.error('提交审批失败')
        })
      })
    },
    /** 关闭审批对话框 */
    handleApprovalDialogClose() {
      this.$refs.approvalForm.resetFields()
      this.currentRow = null
    },
    /** 提交表单 */
    handleDataSubmit() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.submitLoading = true
          
          // 将数据值转换为JSON字符串
          const formData = {
            ...this.dataForm,
            dataValues: JSON.stringify(this.dataValues)
          }
          
          const apiMethod = this.isEdit ? updateData : addData
          apiMethod(formData).then(res => {
            this.submitLoading = false
            if (res.code === 1) {
              this.$message.success(this.isEdit ? '修改成功' : '新增成功')
              this.dialogVisible = false
              this.loadData()
            } else {
              this.$message.error(res.msg || (this.isEdit ? '修改失败' : '新增失败'))
            }
          }).catch(() => {
            this.submitLoading = false
            this.$message.error(this.isEdit ? '修改失败' : '新增失败')
          })
        }
      })
    },
    /** 重置表单 */
    resetForm() {
      this.dataForm = {
        dataId: '',
        modelId: '',
        formId: '',
        period: '',
        version: '',
        subjectCode: '',
        organizationCode: '',
        dataValues: '',
        description: ''
      }
      this.formFields = []
      this.dataValues = {}
      this.dialogFormList = []
      if (this.$refs.dataForm) {
        this.$refs.dataForm.resetFields()
      }
    },
    /** 对话框关闭 */
    handleDialogClose() {
      this.resetForm()
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  line-height: 1.5;
  display: block;
  margin-top: 5px;
}
</style>

