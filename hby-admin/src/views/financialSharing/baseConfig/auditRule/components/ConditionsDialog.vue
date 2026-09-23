<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="conditions-container">
      <!-- 工具栏 -->
      <div class="toolbar">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">
          添加条件
        </el-button>
      </div>

      <!-- 条件列表 -->
      <el-table
        :data="conditionList"
        border
        style="width: 100%; margin-top: 10px"
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="conditionName" label="条件名称" width="150" />
        <el-table-column prop="conditionField" label="条件字段" width="120" />
        <el-table-column prop="conditionOperator" label="操作符" width="100" align="center">
          <template slot-scope="scope">
            {{ getOperatorText(scope.row.conditionOperator) }}
          </template>
        </el-table-column>
        <el-table-column prop="conditionValue" label="条件值" />
        <el-table-column prop="logicOperator" label="逻辑操作符" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.logicOperator || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 条件编辑对话框 -->
    <el-dialog
      :title="conditionDialogTitle"
      :visible.sync="conditionDialogVisible"
      width="600px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form
        ref="conditionFormRef"
        :model="conditionForm"
        :rules="conditionRules"
        label-width="100px"
      >
        <el-form-item label="条件名称" prop="conditionName">
          <el-input v-model="conditionForm.conditionName" placeholder="请输入条件名称" />
        </el-form-item>
        <el-form-item label="条件字段" prop="conditionField">
          <el-input v-model="conditionForm.conditionField" placeholder="请输入条件字段，如：amount" />
        </el-form-item>
        <el-form-item label="操作符" prop="conditionOperator">
          <el-select v-model="conditionForm.conditionOperator" placeholder="请选择操作符" style="width: 100%">
            <el-option label="等于 (=)" value="EQUAL" />
            <el-option label="大于 (>)" value="GREATER" />
            <el-option label="小于 (<)" value="LESS" />
            <el-option label="大于等于 (>=)" value="GREATER_EQUAL" />
            <el-option label="小于等于 (<=)" value="LESS_EQUAL" />
            <el-option label="不等于 (!=)" value="NOT_EQUAL" />
            <el-option label="包含 (LIKE)" value="CONTAIN" />
            <el-option label="不包含 (NOT LIKE)" value="NOT_CONTAIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="条件值" prop="conditionValue">
          <el-input v-model="conditionForm.conditionValue" placeholder="请输入条件值" />
        </el-form-item>
        <el-form-item label="逻辑操作符" prop="logicOperator">
          <el-select v-model="conditionForm.logicOperator" placeholder="请选择逻辑操作符" clearable style="width: 100%">
            <el-option label="且 (AND)" value="AND" />
            <el-option label="或 (OR)" value="OR" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序号" prop="sortOrder">
          <el-input-number v-model="conditionForm.sortOrder" :min="0" :max="999" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="conditionForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="conditionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveCondition" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { auditRuleApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'ConditionsDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    ruleId: {
      type: String,
      default: ''
    },
    ruleName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      saveLoading: false,
      conditionList: [],
      conditionDialogVisible: false,
      conditionDialogTitle: '添加条件',
      conditionForm: {
        conditionId: null,
        ruleId: '',
        conditionName: '',
        conditionField: '',
        conditionOperator: '',
        conditionValue: '',
        logicOperator: '',
        sortOrder: 0,
        remark: ''
      },
      conditionRules: {
        conditionName: [
          { required: true, message: '请输入条件名称', trigger: 'blur' }
        ],
        conditionField: [
          { required: true, message: '请输入条件字段', trigger: 'blur' }
        ],
        conditionOperator: [
          { required: true, message: '请选择操作符', trigger: 'change' }
        ],
        conditionValue: [
          { required: true, message: '请输入条件值', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    dialogTitle() {
      return `规则条件管理 - ${this.ruleName}`
    }
  },
  watch: {
    visible(val) {
      if (val && this.ruleId) {
        this.loadConditions()
      }
    }
  },
  methods: {
    async loadConditions() {
      try {
        this.loading = true
        const response = await auditRuleApi.getConditions(this.ruleId)
        if (response.code === 1) {
          this.conditionList = response.data || []
        } else {
          this.$message.error(response.msg || '加载条件列表失败')
        }
      } catch (error) {
        this.$message.error('加载条件列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    handleAdd() {
      this.conditionDialogTitle = '添加条件'
      this.conditionForm = {
        conditionId: null,
        ruleId: this.ruleId,
        conditionName: '',
        conditionField: '',
        conditionOperator: '',
        conditionValue: '',
        logicOperator: '',
        sortOrder: 0,
        remark: ''
      }
      this.conditionDialogVisible = true
    },
    handleEdit(row) {
      this.conditionDialogTitle = '编辑条件'
      this.conditionForm = { ...row }
      this.conditionDialogVisible = true
    },
    async handleSaveCondition() {
      try {
        await this.$refs.conditionFormRef.validate()
        this.saveLoading = true

        const response = await auditRuleApi.saveCondition(this.conditionForm)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.conditionDialogVisible = false
          this.loadConditions()
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
        await this.$confirm('确定要删除该条件吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await auditRuleApi.deleteCondition(row.conditionId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadConditions()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    getOperatorText(operator) {
      const operatorMap = {
        'EQUAL': '等于 (=)',
        'GREATER': '大于 (>)',
        'LESS': '小于 (<)',
        'GREATER_EQUAL': '大于等于 (>=)',
        'LESS_EQUAL': '小于等于 (<=)',
        'NOT_EQUAL': '不等于 (!=)',
        'CONTAIN': '包含 (LIKE)',
        'NOT_CONTAIN': '不包含 (NOT LIKE)'
      }
      return operatorMap[operator] || operator
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('close')
    }
  }
}
</script>

<style scoped>
.conditions-container {
  padding: 10px 0;
}

.toolbar {
  margin-bottom: 10px;
}
</style>

