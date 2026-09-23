<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="visible"
    width="800px"
    :before-close="handleClose"
    @closed="handleClosed"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
      size="small"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算编码" prop="budgetCode">
            <el-input
              v-model="formData.budgetCode"
              placeholder="请输入预算编码"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算名称" prop="budgetName">
            <el-input
              v-model="formData.budgetName"
              placeholder="请输入预算名称"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算类型" prop="budgetType">
            <el-select
              v-model="formData.budgetType"
              placeholder="请选择预算类型"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="收入预算" value="REVENUE" />
              <el-option label="支出预算" value="EXPENSE" />
              <el-option label="投资预算" value="INVESTMENT" />
              <el-option label="现金流预算" value="CASHFLOW" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算状态" prop="budgetStatus">
            <el-select
              v-model="formData.budgetStatus"
              placeholder="请选择预算状态"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="草稿" value="DRAFT" />
              <el-option label="审批中" value="APPROVING" />
              <el-option label="已批准" value="APPROVED" />
              <el-option label="执行中" value="EXECUTING" />
              <el-option label="已完成" value="COMPLETED" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算年度" prop="budgetYear">
            <el-date-picker
              v-model="formData.budgetYear"
              type="year"
              placeholder="请选择预算年度"
              :disabled="isView"
              style="width: 100%"
              format="yyyy"
              value-format="yyyy"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算周期" prop="budgetPeriod">
            <el-select
              v-model="formData.budgetPeriod"
              placeholder="请选择预算周期"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="年度" value="YEARLY" />
              <el-option label="季度" value="QUARTERLY" />
              <el-option label="月度" value="MONTHLY" />
              <el-option label="周度" value="WEEKLY" />
            </el-select>
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
              placeholder="请输入预算金额"
              :disabled="isView"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="币种" prop="currency">
            <el-select
              v-model="formData.currency"
              placeholder="请选择币种"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="人民币" value="CNY" />
              <el-option label="美元" value="USD" />
              <el-option label="欧元" value="EUR" />
              <el-option label="日元" value="JPY" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="预算描述" prop="budgetDescription">
            <el-input
              v-model="formData.budgetDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入预算描述"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="负责部门" prop="responsibleDept">
            <el-input
              v-model="formData.responsibleDept"
              placeholder="请输入负责部门"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input
              v-model="formData.responsiblePerson"
              placeholder="请输入负责人"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="!isView" type="primary" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BudgetDataForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    mode: {
      type: String,
      default: 'create' // create, edit, view
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        create: '新增预算数据',
        edit: '编辑预算数据',
        view: '查看预算数据'
      }
      return titleMap[this.mode] || '预算数据'
    },
    isView() {
      return this.mode === 'view'
    }
  },
  data() {
    return {
      rules: {
        budgetCode: [
          { required: true, message: '请输入预算编码', trigger: 'blur' }
        ],
        budgetName: [
          { required: true, message: '请输入预算名称', trigger: 'blur' }
        ],
        budgetType: [
          { required: true, message: '请选择预算类型', trigger: 'change' }
        ],
        budgetStatus: [
          { required: true, message: '请选择预算状态', trigger: 'change' }
        ],
        budgetYear: [
          { required: true, message: '请选择预算年度', trigger: 'change' }
        ],
        budgetAmount: [
          { required: true, message: '请输入预算金额', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$emit('submit', this.formData)
        }
      })
    },
    handleClose() {
      this.$emit('update:visible', false)
    },
    handleClosed() {
      this.$refs.form.resetFields()
      this.$emit('closed')
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>
