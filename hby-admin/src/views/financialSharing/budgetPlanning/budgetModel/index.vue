<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :model="queryForm" :inline="true" label-width="100px">
      <el-form-item label="模型编码">
        <el-input v-model="queryForm.modelCode" placeholder="请输入模型编码" clearable />
      </el-form-item>
      <el-form-item label="模型名称">
        <el-input v-model="queryForm.modelName" placeholder="请输入模型名称" clearable />
      </el-form-item>
      <el-form-item label="预算年度">
        <el-input v-model="queryForm.budgetYear" placeholder="请输入预算年度" clearable />
      </el-form-item>
      <el-form-item label="预算类型">
        <el-select v-model="queryForm.budgetType" placeholder="请选择预算类型" clearable>
          <el-option label="年度预算" value="ANNUAL" />
          <el-option label="滚动预测" value="ROLLING" />
          <el-option label="专项预算" value="SPECIAL" />
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
      <el-table-column label="模型编码" prop="modelCode" width="150" />
      <el-table-column label="模型名称" prop="modelName" width="200" />
      <el-table-column label="预算年度" prop="budgetYear" width="100" align="center" />
      <el-table-column label="预算类型" prop="budgetType" width="120" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.budgetType === 'ANNUAL'" type="primary" size="mini">年度预算</el-tag>
          <el-tag v-else-if="scope.row.budgetType === 'ROLLING'" type="success" size="mini">滚动预测</el-tag>
          <el-tag v-else-if="scope.row.budgetType === 'SPECIAL'" type="warning" size="mini">专项预算</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预算周期" prop="budgetCycle" width="100" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.budgetCycle === 'YEAR'">年</span>
          <span v-else-if="scope.row.budgetCycle === 'HALF_YEAR'">半年</span>
          <span v-else-if="scope.row.budgetCycle === 'QUARTER'">季</span>
          <span v-else-if="scope.row.budgetCycle === 'MONTH'">月</span>
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
      <el-table-column label="操作" align="center" width="280" fixed="right">
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" @close="handleDialogClose">
      <el-form ref="modelForm" :model="modelForm" :rules="modelRules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模型编码" prop="modelCode">
              <el-input v-model="modelForm.modelCode" placeholder="请输入模型编码" :disabled="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型名称" prop="modelName">
              <el-input v-model="modelForm.modelName" placeholder="请输入模型名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算年度" prop="budgetYear">
              <el-input v-model="modelForm.budgetYear" placeholder="请输入预算年度(如2024)" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算类型" prop="budgetType">
              <el-select v-model="modelForm.budgetType" placeholder="请选择预算类型" style="width: 100%">
                <el-option label="年度预算" value="ANNUAL" />
                <el-option label="滚动预测" value="ROLLING" />
                <el-option label="专项预算" value="SPECIAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算周期" prop="budgetCycle">
              <el-select v-model="modelForm.budgetCycle" placeholder="请选择预算周期" style="width: 100%">
                <el-option label="年" value="YEAR" />
                <el-option label="半年" value="HALF_YEAR" />
                <el-option label="季" value="QUARTER" />
                <el-option label="月" value="MONTH" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序号" prop="sortOrder">
              <el-input-number v-model="modelForm.sortOrder" :min="0" :max="9999" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="维度配置">
          <el-card shadow="never" style="background-color: #f5f7fa;">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-checkbox v-model="dimensionConfig.subject.enabled">科目维度</el-checkbox>
                <el-form-item v-if="dimensionConfig.subject.enabled" label="层级" label-width="80px" style="margin-top: 10px;">
                  <el-input-number v-model="dimensionConfig.subject.level" :min="1" :max="10" size="small" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-checkbox v-model="dimensionConfig.organization.enabled">主体维度</el-checkbox>
                <el-form-item v-if="dimensionConfig.organization.enabled" label="层级" label-width="80px" style="margin-top: 10px;">
                  <el-input-number v-model="dimensionConfig.organization.level" :min="1" :max="10" size="small" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 15px;">
              <el-col :span="12">
                <el-checkbox v-model="dimensionConfig.period.enabled">期间维度</el-checkbox>
                <el-form-item v-if="dimensionConfig.period.enabled" label="类型" label-width="80px" style="margin-top: 10px;">
                  <el-select v-model="dimensionConfig.period.type" size="small" style="width: 100%">
                    <el-option label="年" value="YEAR" />
                    <el-option label="半年" value="HALF_YEAR" />
                    <el-option label="季" value="QUARTER" />
                    <el-option label="月" value="MONTH" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-checkbox v-model="dimensionConfig.version.enabled">版本维度</el-checkbox>
              </el-col>
            </el-row>
          </el-card>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="modelForm.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 复制对话框 -->
    <el-dialog title="复制预算模型" :visible.sync="copyDialogVisible" width="500px">
      <el-form ref="copyForm" :model="copyForm" :rules="copyRules" label-width="120px">
        <el-form-item label="源模型">
          <el-input v-model="copySourceName" disabled />
        </el-form-item>
        <el-form-item label="新模型编码" prop="newModelCode">
          <el-input v-model="copyForm.newModelCode" placeholder="请输入新模型编码" />
        </el-form-item>
        <el-form-item label="新模型名称" prop="newModelName">
          <el-input v-model="copyForm.newModelName" placeholder="请输入新模型名称" />
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
import { getModelList, addModel, updateModel, deleteModel, batchDeleteModel, copyModel, enableModel, disableModel } from '@/api/financialSharing/budgetPlanning/budgetModel'
import Pagination from '@/components/Pagination'

export default {
  name: 'BudgetModel',
  components: {
    Pagination
  },
  data() {
    return {
      // 查询参数
      queryForm: {
        modelCode: '',
        modelName: '',
        budgetYear: '',
        budgetType: '',
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
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      submitLoading: false,
      // 模型表单
      modelForm: {
        modelId: '',
        modelCode: '',
        modelName: '',
        budgetYear: '',
        budgetType: '',
        budgetCycle: '',
        dimensionConfig: '',
        description: '',
        sortOrder: 0
      },
      // 维度配置
      dimensionConfig: {
        subject: {
          enabled: true,
          required: true,
          level: 3
        },
        organization: {
          enabled: true,
          required: true,
          level: 2
        },
        period: {
          enabled: true,
          required: true,
          type: 'MONTH'
        },
        version: {
          enabled: false,
          required: false
        }
      },
      // 表单验证规则
      modelRules: {
        modelCode: [
          { required: true, message: '请输入模型编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        modelName: [
          { required: true, message: '请输入模型名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        budgetYear: [
          { required: true, message: '请输入预算年度', trigger: 'blur' },
          { pattern: /^\d{4}$/, message: '请输入4位年份', trigger: 'blur' }
        ],
        budgetType: [
          { required: true, message: '请选择预算类型', trigger: 'change' }
        ],
        budgetCycle: [
          { required: true, message: '请选择预算周期', trigger: 'change' }
        ]
      },
      // 复制对话框
      copyDialogVisible: false,
      copyLoading: false,
      copySourceId: '',
      copySourceName: '',
      copyForm: {
        newModelCode: '',
        newModelName: ''
      },
      copyRules: {
        newModelCode: [
          { required: true, message: '请输入新模型编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        newModelName: [
          { required: true, message: '请输入新模型名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    /** 查询按钮 */
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },
    /** 重置按钮 */
    resetQuery() {
      this.queryForm = {
        modelCode: '',
        modelName: '',
        budgetYear: '',
        budgetType: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      }
      this.loadData()
    },
    /** 加载数据 */
    loadData() {
      this.loading = true
      getModelList(this.queryForm).then(res => {
        this.loading = false
        // 诊断日志：定位分页页码不显示问题，验证后可删除
        console.log('[BudgetModel] getModelList response:', res)
        if (res.code === 1) {
          this.tableData = res.data.list || []
          this.total = res.data.total || 0
          console.log('[BudgetModel] total=', this.total, 'typeof=', typeof this.total,
            'pageSize=', this.queryForm.pageSize, 'pageNum=', this.queryForm.pageNum)
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
      this.dialogTitle = '新增预算模型'
      this.isEdit = false
      this.dialogVisible = true
    },
    /** 编辑按钮 */
    handleEdit(row) {
      this.resetForm()
      this.dialogTitle = '编辑预算模型'
      this.isEdit = true
      
      // 填充表单数据
      this.modelForm = {
        modelId: row.modelId,
        modelCode: row.modelCode,
        modelName: row.modelName,
        budgetYear: row.budgetYear,
        budgetType: row.budgetType,
        budgetCycle: row.budgetCycle,
        description: row.description,
        sortOrder: row.sortOrder || 0
      }
      
      // 解析维度配置
      if (row.dimensionConfig) {
        try {
          this.dimensionConfig = JSON.parse(row.dimensionConfig)
        } catch (e) {
          console.error('解析维度配置失败', e)
        }
      }
      
      this.dialogVisible = true
    },
    /** 复制按钮 */
    handleCopy(row) {
      this.copySourceId = row.modelId
      this.copySourceName = row.modelName
      this.copyForm = {
        newModelCode: '',
        newModelName: ''
      }
      this.copyDialogVisible = true
    },
    /** 删除按钮 */
    handleDelete(row) {
      this.$confirm('确认删除该预算模型吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteModel({ modelId: row.modelId }).then(res => {
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
      const modelIds = this.selectedRows.map(item => item.modelId)
      this.$confirm('确认删除选中的预算模型吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchDeleteModel({ modelIds }).then(res => {
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
      this.$confirm('确认启用该预算模型吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        enableModel({ modelId: row.modelId }).then(res => {
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
      this.$confirm('确认停用该预算模型吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        disableModel({ modelId: row.modelId }).then(res => {
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
      this.$refs.modelForm.validate(valid => {
        if (valid) {
          this.submitLoading = true
          
          // 将维度配置转换为JSON字符串
          const formData = {
            ...this.modelForm,
            dimensionConfig: JSON.stringify(this.dimensionConfig)
          }
          
          const apiMethod = this.isEdit ? updateModel : addModel
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
          copyModel({
            modelId: this.copySourceId,
            newModelCode: this.copyForm.newModelCode,
            newModelName: this.copyForm.newModelName
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
      this.modelForm = {
        modelId: '',
        modelCode: '',
        modelName: '',
        budgetYear: '',
        budgetType: '',
        budgetCycle: '',
        dimensionConfig: '',
        description: '',
        sortOrder: 0
      }
      this.dimensionConfig = {
        subject: {
          enabled: true,
          required: true,
          level: 3
        },
        organization: {
          enabled: true,
          required: true,
          level: 2
        },
        period: {
          enabled: true,
          required: true,
          type: 'MONTH'
        },
        version: {
          enabled: false,
          required: false
        }
      }
      if (this.$refs.modelForm) {
        this.$refs.modelForm.resetFields()
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

