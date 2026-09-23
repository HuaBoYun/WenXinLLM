<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :model="queryForm" :inline="true" label-width="100px">
      <el-form-item label="预算模型">
        <el-select v-model="queryForm.modelId" placeholder="请选择预算模型" clearable filterable>
          <el-option
            v-for="item in modelList"
            :key="item.modelId"
            :label="item.modelName"
            :value="item.modelId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="表单编码">
        <el-input v-model="queryForm.formCode" placeholder="请输入表单编码" clearable />
      </el-form-item>
      <el-form-item label="表单名称">
        <el-input v-model="queryForm.formName" placeholder="请输入表单名称" clearable />
      </el-form-item>
      <el-form-item label="表单类型">
        <el-select v-model="queryForm.formType" placeholder="请选择表单类型" clearable>
          <el-option label="编制表单" value="BUDGET" />
          <el-option label="调整表单" value="ADJUST" />
          <el-option label="查询表单" value="QUERY" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
          <el-option label="草稿" value="DRAFT" />
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
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" :disabled="multiple" @click="handleBatchDelete">批量删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="表单编码" prop="formCode" width="150" />
      <el-table-column label="表单名称" prop="formName" width="200" />
      <el-table-column label="预算模型" prop="modelName" width="150" />
      <el-table-column label="表单类型" prop="formType" width="120" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.formType === 'BUDGET'" type="primary" size="mini">编制表单</el-tag>
          <el-tag v-else-if="scope.row.formType === 'ADJUST'" type="warning" size="mini">调整表单</el-tag>
          <el-tag v-else-if="scope.row.formType === 'QUERY'" type="info" size="mini">查询表单</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'DRAFT'" type="info" size="mini">草稿</el-tag>
          <el-tag v-else-if="scope.row.status === 'ACTIVE'" type="success" size="mini">启用</el-tag>
          <el-tag v-else type="danger" size="mini">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="描述" prop="description" show-overflow-tooltip />
      <el-table-column label="创建时间" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" width="320" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-document-copy" @click="handleCopy(scope.row)">复制</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handlePreview(scope.row)">预览</el-button>
          <el-button 
            v-if="scope.row.status === 'DRAFT' || scope.row.status === 'INACTIVE'" 
            size="mini" 
            type="text" 
            icon="el-icon-check" 
            @click="handleEnable(scope.row)"
          >启用</el-button>
          <el-button 
            v-if="scope.row.status === 'ACTIVE'" 
            size="mini" 
            type="text" 
            icon="el-icon-close" 
            @click="handleDisable(scope.row)"
          >停用</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="1200px" @close="handleDialogClose">
      <el-form ref="formForm" :model="formForm" :rules="formRules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="表单编码" prop="formCode">
              <el-input v-model="formForm.formCode" placeholder="请输入表单编码" :disabled="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="表单名称" prop="formName">
              <el-input v-model="formForm.formName" placeholder="请输入表单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预算模型" prop="modelId">
              <el-select v-model="formForm.modelId" placeholder="请选择预算模型" style="width: 100%" filterable>
                <el-option
                  v-for="item in modelList"
                  :key="item.modelId"
                  :label="item.modelName"
                  :value="item.modelId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="表单类型" prop="formType">
              <el-select v-model="formForm.formType" placeholder="请选择表单类型" style="width: 100%">
                <el-option label="编制表单" value="BUDGET" />
                <el-option label="调整表单" value="ADJUST" />
                <el-option label="查询表单" value="QUERY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="排序号" prop="sortOrder">
              <el-input-number v-model="formForm.sortOrder" :min="0" :max="9999" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述">
          <el-input v-model="formForm.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        
        <!-- 表单字段配置 -->
        <el-divider content-position="left">表单字段配置</el-divider>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddField" style="margin-bottom: 10px">添加字段</el-button>
        
        <el-table :data="formConfig.fields" border style="width: 100%">
          <el-table-column label="序号" type="index" width="60" align="center" />
          <el-table-column label="字段编码" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.fieldCode" placeholder="字段编码" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="字段名称" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.fieldName" placeholder="字段名称" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="字段类型" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.fieldType" placeholder="字段类型" size="small">
                <el-option label="文本" value="text" />
                <el-option label="数字" value="number" />
                <el-option label="日期" value="date" />
                <el-option label="下拉" value="select" />
                <el-option label="多选" value="checkbox" />
                <el-option label="文本域" value="textarea" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="是否必填" width="100" align="center">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.required" />
            </template>
          </el-table-column>
          <el-table-column label="是否可编辑" width="100" align="center">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.editable" />
            </template>
          </el-table-column>
          <el-table-column label="是否显示" width="100" align="center">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.visible" />
            </template>
          </el-table-column>
          <el-table-column label="默认值" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.defaultValue" placeholder="默认值" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-top" @click="handleMoveUp(scope.$index)" :disabled="scope.$index === 0">上移</el-button>
              <el-button size="mini" type="text" icon="el-icon-bottom" @click="handleMoveDown(scope.$index)" :disabled="scope.$index === formConfig.fields.length - 1">下移</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteField(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 复制对话框 -->
    <el-dialog title="复制预算表单配置" :visible.sync="copyDialogVisible" width="500px">
      <el-form ref="copyForm" :model="copyForm" :rules="copyRules" label-width="120px">
        <el-form-item label="源表单">
          <el-input v-model="copySourceName" disabled />
        </el-form-item>
        <el-form-item label="新表单编码" prop="newFormCode">
          <el-input v-model="copyForm.newFormCode" placeholder="请输入新表单编码" />
        </el-form-item>
        <el-form-item label="新表单名称" prop="newFormName">
          <el-input v-model="copyForm.newFormName" placeholder="请输入新表单名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="copyLoading" @click="handleCopySubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog title="表单预览" :visible.sync="previewDialogVisible" width="800px">
      <el-form :model="previewData" label-width="120px">
        <el-form-item
          v-for="field in previewFields"
          :key="field.fieldCode"
          :label="field.fieldName"
          :required="field.required"
        >
          <el-input v-if="field.fieldType === 'text'" :placeholder="'请输入' + field.fieldName" :disabled="!field.editable" />
          <el-input-number v-else-if="field.fieldType === 'number'" :placeholder="'请输入' + field.fieldName" :disabled="!field.editable" style="width: 100%" />
          <el-date-picker v-else-if="field.fieldType === 'date'" type="date" :placeholder="'请选择' + field.fieldName" :disabled="!field.editable" style="width: 100%" />
          <el-select v-else-if="field.fieldType === 'select'" :placeholder="'请选择' + field.fieldName" :disabled="!field.editable" style="width: 100%">
            <el-option label="选项1" value="1" />
            <el-option label="选项2" value="2" />
          </el-select>
          <el-checkbox-group v-else-if="field.fieldType === 'checkbox'" :disabled="!field.editable">
            <el-checkbox label="选项1" />
            <el-checkbox label="选项2" />
          </el-checkbox-group>
          <el-input v-else-if="field.fieldType === 'textarea'" type="textarea" :rows="3" :placeholder="'请输入' + field.fieldName" :disabled="!field.editable" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFormList, addForm, updateForm, deleteForm, batchDeleteForm, copyForm, enableForm, disableForm } from '@/api/financialSharing/budgetPlanning/budgetForm'
import { getModelListNoPage } from '@/api/financialSharing/budgetPlanning/budgetModel'
import Pagination from '@/components/Pagination'

export default {
  name: 'BudgetForm',
  components: {
    Pagination
  },
  data() {
    return {
      // 查询参数
      queryForm: {
        modelId: '',
        formCode: '',
        formName: '',
        formType: '',
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
      // 是否禁用批量删除
      multiple: true,
      // 预算模型列表
      modelList: [],
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      submitLoading: false,
      // 表单表单
      formForm: {
        formId: '',
        modelId: '',
        formCode: '',
        formName: '',
        formType: '',
        formConfig: '',
        description: '',
        sortOrder: 0
      },
      // 表单配置
      formConfig: {
        fields: []
      },
      // 表单验证规则
      formRules: {
        formCode: [
          { required: true, message: '请输入表单编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        formName: [
          { required: true, message: '请输入表单名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        modelId: [
          { required: true, message: '请选择预算模型', trigger: 'change' }
        ],
        formType: [
          { required: true, message: '请选择表单类型', trigger: 'change' }
        ]
      },
      // 复制对话框
      copyDialogVisible: false,
      copyLoading: false,
      copySourceId: '',
      copySourceName: '',
      copyForm: {
        newFormCode: '',
        newFormName: ''
      },
      copyRules: {
        newFormCode: [
          { required: true, message: '请输入新表单编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        newFormName: [
          { required: true, message: '请输入新表单名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ]
      },
      // 预览对话框
      previewDialogVisible: false,
      previewFields: [],
      previewData: {}
    }
  },
  created() {
    this.loadModelList()
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
    /** 查询按钮 */
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },
    /** 重置按钮 */
    resetQuery() {
      this.queryForm = {
        modelId: '',
        formCode: '',
        formName: '',
        formType: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      }
      this.loadData()
    },
    /** 加载数据 */
    loadData() {
      this.loading = true
      getFormList(this.queryForm).then(res => {
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
      this.dialogTitle = '新增预算表单配置'
      this.isEdit = false
      this.dialogVisible = true
    },
    /** 编辑按钮 */
    handleEdit(row) {
      this.resetForm()
      this.dialogTitle = '编辑预算表单配置'
      this.isEdit = true

      // 填充表单数据
      this.formForm = {
        formId: row.formId,
        modelId: row.modelId,
        formCode: row.formCode,
        formName: row.formName,
        formType: row.formType,
        description: row.description,
        sortOrder: row.sortOrder || 0
      }

      // 解析表单配置
      if (row.formConfig) {
        try {
          this.formConfig = JSON.parse(row.formConfig)
        } catch (e) {
          console.error('解析表单配置失败', e)
        }
      }

      this.dialogVisible = true
    },
    /** 复制按钮 */
    handleCopy(row) {
      this.copySourceId = row.formId
      this.copySourceName = row.formName
      this.copyForm = {
        newFormCode: '',
        newFormName: ''
      }
      this.copyDialogVisible = true
    },
    /** 预览按钮 */
    handlePreview(row) {
      if (row.formConfig) {
        try {
          const config = JSON.parse(row.formConfig)
          this.previewFields = config.fields.filter(f => f.visible)
        } catch (e) {
          this.$message.error('解析表单配置失败')
          return
        }
      } else {
        this.previewFields = []
      }
      this.previewData = {}
      this.previewDialogVisible = true
    },
    /** 删除按钮 */
    handleDelete(row) {
      this.$confirm('确认删除该预算表单配置吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteForm({ formId: row.formId }).then(res => {
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
      const formIds = this.selectedRows.map(item => item.formId)
      this.$confirm('确认删除选中的预算表单配置吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchDeleteForm({ formIds }).then(res => {
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    /** 启用按钮 */
    handleEnable(row) {
      this.$confirm('确认启用该预算表单配置吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        enableForm({ formId: row.formId }).then(res => {
          if (res.code === 1) {
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
      this.$confirm('确认停用该预算表单配置吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        disableForm({ formId: row.formId }).then(res => {
          if (res.code === 1) {
            this.$message.success('停用成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '停用失败')
          }
        })
      }).catch(() => {})
    },
    /** 添加字段 */
    handleAddField() {
      this.formConfig.fields.push({
        fieldCode: '',
        fieldName: '',
        fieldType: 'text',
        required: false,
        editable: true,
        visible: true,
        defaultValue: ''
      })
    },
    /** 删除字段 */
    handleDeleteField(index) {
      this.formConfig.fields.splice(index, 1)
    },
    /** 上移字段 */
    handleMoveUp(index) {
      if (index > 0) {
        const temp = this.formConfig.fields[index]
        this.$set(this.formConfig.fields, index, this.formConfig.fields[index - 1])
        this.$set(this.formConfig.fields, index - 1, temp)
      }
    },
    /** 下移字段 */
    handleMoveDown(index) {
      if (index < this.formConfig.fields.length - 1) {
        const temp = this.formConfig.fields[index]
        this.$set(this.formConfig.fields, index, this.formConfig.fields[index + 1])
        this.$set(this.formConfig.fields, index + 1, temp)
      }
    },
    /** 提交表单 */
    handleSubmit() {
      this.$refs.formForm.validate(valid => {
        if (valid) {
          // 验证字段配置
          if (this.formConfig.fields.length === 0) {
            this.$message.warning('请至少添加一个字段')
            return
          }

          for (let i = 0; i < this.formConfig.fields.length; i++) {
            const field = this.formConfig.fields[i]
            if (!field.fieldCode) {
              this.$message.warning(`第${i + 1}个字段的字段编码不能为空`)
              return
            }
            if (!field.fieldName) {
              this.$message.warning(`第${i + 1}个字段的字段名称不能为空`)
              return
            }
          }

          this.submitLoading = true

          // 将表单配置转换为JSON字符串
          const formData = {
            ...this.formForm,
            formConfig: JSON.stringify(this.formConfig)
          }

          const apiMethod = this.isEdit ? updateForm : addForm
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
    /** 复制提交 */
    handleCopySubmit() {
      this.$refs.copyForm.validate(valid => {
        if (valid) {
          this.copyLoading = true
          copyForm({
            formId: this.copySourceId,
            newFormCode: this.copyForm.newFormCode,
            newFormName: this.copyForm.newFormName
          }).then(res => {
            this.copyLoading = false
            if (res.code === 1) {
              this.$message.success('复制成功')
              this.copyDialogVisible = false
              this.loadData()
            } else {
              this.$message.error(res.msg || '复制失败')
            }
          }).catch(() => {
            this.copyLoading = false
            this.$message.error('复制失败')
          })
        }
      })
    },
    /** 重置表单 */
    resetForm() {
      this.formForm = {
        formId: '',
        modelId: '',
        formCode: '',
        formName: '',
        formType: '',
        formConfig: '',
        description: '',
        sortOrder: 0
      }
      this.formConfig = {
        fields: []
      }
      if (this.$refs.formForm) {
        this.$refs.formForm.resetFields()
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
</style>

