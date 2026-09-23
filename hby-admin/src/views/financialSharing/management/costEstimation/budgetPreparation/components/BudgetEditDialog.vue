<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
    append-to-body
  >
    <el-form
      ref="budgetForm"
      :model="form"
      :rules="rules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算编号" prop="budgetCode">
            <el-input
              v-model="form.budgetCode"
              placeholder="请输入预算编号"
              :disabled="isEdit"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算名称" prop="budgetName">
            <el-input
              v-model="form.budgetName"
              placeholder="请输入预算名称"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算类型" prop="budgetType">
            <el-select v-model="form.budgetType" placeholder="请选择预算类型" style="width: 100%">
              <el-option label="年度预算" value="1" />
              <el-option label="季度预算" value="2" />
              <el-option label="月度预算" value="3" />
              <el-option label="项目预算" value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算金额" prop="budgetAmount">
            <el-input-number
              v-model="form.budgetAmount"
              :precision="2"
              :min="0"
              :max="999999999.99"
              placeholder="请输入预算金额"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开始日期" prop="startDate">
            <el-date-picker
              v-model="form.startDate"
              type="date"
              placeholder="选择开始日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束日期" prop="endDate">
            <el-date-picker
              v-model="form.endDate"
              type="date"
              placeholder="选择结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="负责部门" prop="responsibleDept">
            <el-input
              v-model="form.responsibleDept"
              placeholder="请输入负责部门"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input
              v-model="form.responsiblePerson"
              placeholder="请输入负责人"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="预算描述" prop="budgetDescription">
        <el-input
          v-model="form.budgetDescription"
          type="textarea"
          :rows="3"
          placeholder="请输入预算描述"
        />
      </el-form-item>
      
      <!-- 预算明细 -->
      <el-form-item label="预算明细">
        <div class="budget-details">
          <div class="detail-header">
            <el-button type="primary" size="small" icon="el-icon-plus" @click="addBudgetItem">
              添加明细
            </el-button>
          </div>
          <el-table :data="form.budgetItems" border style="width: 100%; margin-top: 10px;">
            <el-table-column label="科目编码" width="120">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.subjectCode"
                  placeholder="科目编码"
                  size="small"
                />
              </template>
            </el-table-column>
            <el-table-column label="科目名称" width="150">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.subjectName"
                  placeholder="科目名称"
                  size="small"
                />
              </template>
            </el-table-column>
            <el-table-column label="预算金额" width="120">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.amount"
                  :precision="2"
                  :min="0"
                  size="small"
                  style="width: 100%"
                />
              </template>
            </el-table-column>
            <el-table-column label="备注">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.remark"
                  placeholder="备注"
                  size="small"
                />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80" align="center">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  size="small"
                  icon="el-icon-delete"
                  @click="removeBudgetItem(scope.$index)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOrUpdateBudgetPreparation, getBudgetPreparationById } from '@/api/financialSharing/costEstimation'

export default {
  name: 'BudgetEditDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    budgetData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      form: {
        budgetId: null,
        budgetCode: '',
        budgetName: '',
        budgetType: '',
        budgetAmount: 0,
        startDate: '',
        endDate: '',
        responsibleDept: '',
        responsiblePerson: '',
        budgetDescription: '',
        budgetItems: []
      },
      rules: {
        budgetCode: [
          { required: true, message: '预算编号不能为空', trigger: 'blur' }
        ],
        budgetName: [
          { required: true, message: '预算名称不能为空', trigger: 'blur' }
        ],
        budgetType: [
          { required: true, message: '预算类型不能为空', trigger: 'change' }
        ],
        budgetAmount: [
          { required: true, message: '预算金额不能为空', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: '开始日期不能为空', trigger: 'change' }
        ],
        endDate: [
          { required: true, message: '结束日期不能为空', trigger: 'change' }
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
      return this.isEdit ? '编辑预算' : '新增预算'
    },
    isEdit() {
      return this.budgetData && this.budgetData.budgetId
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      if (this.isEdit) {
        this.loading = true
        getBudgetPreparationById(this.budgetData.budgetId).then(response => {
          this.form = { ...response.data }
          if (!this.form.budgetItems) {
            this.form.budgetItems = []
          }
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      } else {
        this.form = {
          budgetId: null,
          budgetCode: '',
          budgetName: '',
          budgetType: '',
          budgetAmount: 0,
          startDate: '',
          endDate: '',
          responsibleDept: '',
          responsiblePerson: '',
          budgetDescription: '',
          budgetItems: []
        }
      }
    },
    addBudgetItem() {
      this.form.budgetItems.push({
        subjectCode: '',
        subjectName: '',
        amount: 0,
        remark: ''
      })
    },
    removeBudgetItem(index) {
      this.form.budgetItems.splice(index, 1)
    },
    handleSubmit() {
      this.$refs.budgetForm.validate(valid => {
        if (valid) {
          this.submitLoading = true
          saveOrUpdateBudgetPreparation(this.form).then(() => {
            this.$message.success(this.isEdit ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.$emit('refresh')
          }).finally(() => {
            this.submitLoading = false
          })
        }
      })
    },
    handleClose() {
      this.$refs.budgetForm.resetFields()
      this.dialogVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-details {
  .detail-header {
    margin-bottom: 10px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
