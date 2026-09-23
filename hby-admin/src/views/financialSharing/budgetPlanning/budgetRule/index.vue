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
      <el-form-item label="规则编码">
        <el-input v-model="queryForm.ruleCode" placeholder="请输入规则编码" clearable />
      </el-form-item>
      <el-form-item label="规则名称">
        <el-input v-model="queryForm.ruleName" placeholder="请输入规则名称" clearable />
      </el-form-item>
      <el-form-item label="规则类型">
        <el-select v-model="queryForm.ruleType" placeholder="请选择规则类型" clearable>
          <el-option label="计算规则" value="CALCULATE" />
          <el-option label="校验规则" value="VALIDATE" />
          <el-option label="分摊规则" value="ALLOCATE" />
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
      <el-table-column label="规则编码" prop="ruleCode" width="150" />
      <el-table-column label="规则名称" prop="ruleName" width="200" />
      <el-table-column label="预算模型" prop="modelName" width="150" />
      <el-table-column label="规则类型" prop="ruleType" width="120" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.ruleType === 'CALCULATE'" type="primary" size="mini">计算规则</el-tag>
          <el-tag v-else-if="scope.row.ruleType === 'VALIDATE'" type="warning" size="mini">校验规则</el-tag>
          <el-tag v-else-if="scope.row.ruleType === 'ALLOCATE'" type="success" size="mini">分摊规则</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="优先级" prop="priority" width="100" align="center" />
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'DRAFT'" type="info" size="mini">草稿</el-tag>
          <el-tag v-else-if="scope.row.status === 'ACTIVE'" type="success" size="mini">启用</el-tag>
          <el-tag v-else type="danger" size="mini">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="描述" prop="description" show-overflow-tooltip />
      <el-table-column label="创建时间" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-document-copy" @click="handleCopy(scope.row)">复制</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="1000px" @close="handleDialogClose">
      <el-form ref="ruleForm" :model="ruleForm" :rules="ruleRules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="规则编码" prop="ruleCode">
              <el-input v-model="ruleForm.ruleCode" placeholder="请输入规则编码" :disabled="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="ruleForm.ruleName" placeholder="请输入规则名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预算模型" prop="modelId">
              <el-select v-model="ruleForm.modelId" placeholder="请选择预算模型" style="width: 100%" filterable>
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
            <el-form-item label="规则类型" prop="ruleType">
              <el-select v-model="ruleForm.ruleType" placeholder="请选择规则类型" style="width: 100%" @change="handleRuleTypeChange">
                <el-option label="计算规则" value="CALCULATE" />
                <el-option label="校验规则" value="VALIDATE" />
                <el-option label="分摊规则" value="ALLOCATE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="优先级" prop="priority">
              <el-input-number v-model="ruleForm.priority" :min="1" :max="999" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="排序号" prop="sortOrder">
              <el-input-number v-model="ruleForm.sortOrder" :min="0" :max="9999" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述">
          <el-input v-model="ruleForm.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        
        <!-- 规则表达式配置 -->
        <el-divider content-position="left">规则表达式配置</el-divider>
        
        <!-- 计算规则 -->
        <div v-if="ruleForm.ruleType === 'CALCULATE'">
          <el-form-item label="源字段">
            <el-select v-model="ruleExpression.sourceFields" multiple placeholder="请选择源字段" style="width: 100%">
              <el-option label="字段1" value="field1" />
              <el-option label="字段2" value="field2" />
              <el-option label="字段3" value="field3" />
            </el-select>
          </el-form-item>
          <el-form-item label="目标字段">
            <el-select v-model="ruleExpression.targetField" placeholder="请选择目标字段" style="width: 100%">
              <el-option label="字段A" value="fieldA" />
              <el-option label="字段B" value="fieldB" />
              <el-option label="字段C" value="fieldC" />
            </el-select>
          </el-form-item>
          <el-form-item label="计算公式">
            <el-input v-model="ruleExpression.formula" placeholder="例如: field1 + field2 * field3" />
          </el-form-item>
        </div>
        
        <!-- 校验规则 -->
        <div v-if="ruleForm.ruleType === 'VALIDATE'">
          <el-form-item label="校验字段">
            <el-select v-model="ruleExpression.validateField" placeholder="请选择校验字段" style="width: 100%">
              <el-option label="字段1" value="field1" />
              <el-option label="字段2" value="field2" />
              <el-option label="字段3" value="field3" />
            </el-select>
          </el-form-item>
          <el-form-item label="校验条件">
            <el-input v-model="ruleExpression.condition" placeholder="例如: > 0 或 != null" />
          </el-form-item>
          <el-form-item label="错误提示">
            <el-input v-model="ruleExpression.errorMessage" placeholder="请输入校验失败时的错误提示" />
          </el-form-item>
        </div>
        
        <!-- 分摊规则 -->
        <div v-if="ruleForm.ruleType === 'ALLOCATE'">
          <el-form-item label="分摊基数">
            <el-select v-model="ruleExpression.baseField" placeholder="请选择分摊基数字段" style="width: 100%">
              <el-option label="总金额" value="totalAmount" />
              <el-option label="总数量" value="totalQuantity" />
            </el-select>
          </el-form-item>
          <el-form-item label="分摊维度">
            <el-select v-model="ruleExpression.dimensions" multiple placeholder="请选择分摊维度" style="width: 100%">
              <el-option label="部门" value="department" />
              <el-option label="项目" value="project" />
              <el-option label="产品" value="product" />
            </el-select>
          </el-form-item>
          <el-form-item label="分摊方式">
            <el-select v-model="ruleExpression.method" placeholder="请选择分摊方式" style="width: 100%">
              <el-option label="平均分摊" value="average" />
              <el-option label="按比例分摊" value="proportion" />
              <el-option label="按权重分摊" value="weight" />
            </el-select>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 复制对话框 -->
    <el-dialog title="复制业务规则" :visible.sync="copyDialogVisible" width="500px">
      <el-form ref="copyForm" :model="copyForm" :rules="copyRules" label-width="120px">
        <el-form-item label="源规则">
          <el-input v-model="copySourceName" disabled />
        </el-form-item>
        <el-form-item label="新规则编码" prop="newRuleCode">
          <el-input v-model="copyForm.newRuleCode" placeholder="请输入新规则编码" />
        </el-form-item>
        <el-form-item label="新规则名称" prop="newRuleName">
          <el-input v-model="copyForm.newRuleName" placeholder="请输入新规则名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="copyLoading" @click="handleCopySubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getRuleList, addRule, updateRule, deleteRule, batchDeleteRule, copyRule, enableRule, disableRule } from '@/api/financialSharing/budgetPlanning/budgetRule'
import { getModelListNoPage } from '@/api/financialSharing/budgetPlanning/budgetModel'
import Pagination from '@/components/Pagination'

export default {
  name: 'BudgetRule',
  components: {
    Pagination
  },
  data() {
    return {
      // 查询参数
      queryForm: {
        modelId: '',
        ruleCode: '',
        ruleName: '',
        ruleType: '',
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
      // 规则表单
      ruleForm: {
        ruleId: '',
        modelId: '',
        ruleCode: '',
        ruleName: '',
        ruleType: '',
        ruleExpression: '',
        priority: 1,
        description: '',
        sortOrder: 0
      },
      // 规则表达式
      ruleExpression: {
        // 计算规则
        sourceFields: [],
        targetField: '',
        formula: '',
        // 校验规则
        validateField: '',
        condition: '',
        errorMessage: '',
        // 分摊规则
        baseField: '',
        dimensions: [],
        method: ''
      },
      // 表单验证规则
      ruleRules: {
        ruleCode: [
          { required: true, message: '请输入规则编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        modelId: [
          { required: true, message: '请选择预算模型', trigger: 'change' }
        ],
        ruleType: [
          { required: true, message: '请选择规则类型', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请输入优先级', trigger: 'blur' }
        ]
      },
      // 复制对话框
      copyDialogVisible: false,
      copyLoading: false,
      copySourceId: '',
      copySourceName: '',
      copyForm: {
        newRuleCode: '',
        newRuleName: ''
      },
      copyRules: {
        newRuleCode: [
          { required: true, message: '请输入新规则编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        newRuleName: [
          { required: true, message: '请输入新规则名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ]
      }
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
        ruleCode: '',
        ruleName: '',
        ruleType: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      }
      this.loadData()
    },
    /** 加载数据 */
    loadData() {
      this.loading = true
      getRuleList(this.queryForm).then(res => {
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
      this.dialogTitle = '新增业务规则'
      this.isEdit = false
      this.dialogVisible = true
    },
    /** 编辑按钮 */
    handleEdit(row) {
      this.resetForm()
      this.dialogTitle = '编辑业务规则'
      this.isEdit = true
      
      // 填充表单数据
      this.ruleForm = {
        ruleId: row.ruleId,
        modelId: row.modelId,
        ruleCode: row.ruleCode,
        ruleName: row.ruleName,
        ruleType: row.ruleType,
        priority: row.priority || 1,
        description: row.description,
        sortOrder: row.sortOrder || 0
      }
      
      // 解析规则表达式
      if (row.ruleExpression) {
        try {
          this.ruleExpression = JSON.parse(row.ruleExpression)
        } catch (e) {
          console.error('解析规则表达式失败', e)
        }
      }
      
      this.dialogVisible = true
    },
    /** 规则类型改变 */
    handleRuleTypeChange() {
      // 重置规则表达式
      this.ruleExpression = {
        sourceFields: [],
        targetField: '',
        formula: '',
        validateField: '',
        condition: '',
        errorMessage: '',
        baseField: '',
        dimensions: [],
        method: ''
      }
    },
    /** 复制按钮 */
    handleCopy(row) {
      this.copySourceId = row.ruleId
      this.copySourceName = row.ruleName
      this.copyForm = {
        newRuleCode: '',
        newRuleName: ''
      }
      this.copyDialogVisible = true
    },
    /** 删除按钮 */
    handleDelete(row) {
      this.$confirm('确认删除该业务规则吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRule({ ruleId: row.ruleId }).then(res => {
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
      const ruleIds = this.selectedRows.map(item => item.ruleId)
      this.$confirm('确认删除选中的业务规则吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchDeleteRule({ ruleIds }).then(res => {
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
      this.$confirm('确认启用该业务规则吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        enableRule({ ruleId: row.ruleId }).then(res => {
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
      this.$confirm('确认停用该业务规则吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        disableRule({ ruleId: row.ruleId }).then(res => {
          if (res.code === 1) {
            this.$message.success('停用成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '停用失败')
          }
        })
      }).catch(() => {})
    },
    /** 提交表单 */
    handleSubmit() {
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          // 验证规则表达式
          if (this.ruleForm.ruleType === 'CALCULATE') {
            if (!this.ruleExpression.sourceFields || this.ruleExpression.sourceFields.length === 0) {
              this.$message.warning('请选择源字段')
              return
            }
            if (!this.ruleExpression.targetField) {
              this.$message.warning('请选择目标字段')
              return
            }
            if (!this.ruleExpression.formula) {
              this.$message.warning('请输入计算公式')
              return
            }
          } else if (this.ruleForm.ruleType === 'VALIDATE') {
            if (!this.ruleExpression.validateField) {
              this.$message.warning('请选择校验字段')
              return
            }
            if (!this.ruleExpression.condition) {
              this.$message.warning('请输入校验条件')
              return
            }
            if (!this.ruleExpression.errorMessage) {
              this.$message.warning('请输入错误提示')
              return
            }
          } else if (this.ruleForm.ruleType === 'ALLOCATE') {
            if (!this.ruleExpression.baseField) {
              this.$message.warning('请选择分摊基数')
              return
            }
            if (!this.ruleExpression.dimensions || this.ruleExpression.dimensions.length === 0) {
              this.$message.warning('请选择分摊维度')
              return
            }
            if (!this.ruleExpression.method) {
              this.$message.warning('请选择分摊方式')
              return
            }
          }
          
          this.submitLoading = true
          
          // 将规则表达式转换为JSON字符串
          const formData = {
            ...this.ruleForm,
            ruleExpression: JSON.stringify(this.ruleExpression)
          }
          
          const apiMethod = this.isEdit ? updateRule : addRule
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
          copyRule({
            ruleId: this.copySourceId,
            newRuleCode: this.copyForm.newRuleCode,
            newRuleName: this.copyForm.newRuleName
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
      this.ruleForm = {
        ruleId: '',
        modelId: '',
        ruleCode: '',
        ruleName: '',
        ruleType: '',
        ruleExpression: '',
        priority: 1,
        description: '',
        sortOrder: 0
      }
      this.ruleExpression = {
        sourceFields: [],
        targetField: '',
        formula: '',
        validateField: '',
        condition: '',
        errorMessage: '',
        baseField: '',
        dimensions: [],
        method: ''
      }
      if (this.$refs.ruleForm) {
        this.$refs.ruleForm.resetFields()
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

