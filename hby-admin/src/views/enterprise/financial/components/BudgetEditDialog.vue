<template>
  <el-dialog
    title="编辑预算"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算名称" prop="budgetName">
            <el-input v-model="form.budgetName" placeholder="请输入预算名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算类型" prop="budgetType">
            <el-select v-model="form.budgetType" placeholder="请选择预算类型" style="width: 100%">
              <el-option label="年度预算" value="年度预算" />
              <el-option label="季度预算" value="季度预算" />
              <el-option label="月度预算" value="月度预算" />
              <el-option label="项目预算" value="项目预算" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算年度" prop="budgetYear">
            <el-date-picker
              v-model="form.budgetYear"
              type="year"
              placeholder="选择预算年度"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input v-model="form.responsiblePerson" placeholder="请输入负责人姓名" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算总额" prop="totalBudgetAmount">
            <el-input-number
              v-model="form.totalBudgetAmount"
              :precision="2"
              :step="1"
              :min="0"
              style="width: 100%"
              placeholder="请输入预算总额(万元)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算部门" prop="budgetDepartment">
            <el-input v-model="form.budgetDepartment" placeholder="请输入预算部门" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计划开始时间" prop="plannedStartTime">
            <el-date-picker
              v-model="form.plannedStartTime"
              type="date"
              placeholder="选择开始时间"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划结束时间" prop="plannedEndTime">
            <el-date-picker
              v-model="form.plannedEndTime"
              type="date"
              placeholder="选择结束时间"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="预算描述" prop="budgetDescription">
        <el-input
          v-model="form.budgetDescription"
          type="textarea"
          :rows="4"
          placeholder="请输入预算描述"
        />
      </el-form-item>

      <!-- 预算分项 -->
      <el-form-item label="预算分项">
        <el-button type="primary" size="small" @click="addBudgetItem" style="margin-bottom: 10px;">
          添加预算项
        </el-button>
        <el-table :data="form.budgetItems" border style="width: 100%">
          <el-table-column label="项目名称" width="200">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemName" placeholder="项目名称" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="预算金额(万元)" width="150">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.budgetAmount"
                :precision="2"
                :min="0"
                size="small"
                style="width: 100%"
              />
            </template>
          </el-table-column>
          <el-table-column label="备注">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remarks" placeholder="备注" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button
                type="danger"
                size="mini"
                @click="removeBudgetItem(scope.$index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 10px; text-align: right;">
          <strong>分项总计: {{ budgetItemsTotal.toFixed(2) }} 万元</strong>
        </div>
      </el-form-item>

      <el-form-item label="备注信息">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
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
      form: {
        budgetName: '',
        budgetType: '',
        budgetYear: '',
        responsiblePerson: '',
        totalBudgetAmount: 0,
        budgetDepartment: '',
        plannedStartTime: '',
        plannedEndTime: '',
        budgetDescription: '',
        remarks: '',
        budgetItems: []
      },
      rules: {
        budgetName: [
          { required: true, message: '请输入预算名称', trigger: 'blur' }
        ],
        budgetType: [
          { required: true, message: '请选择预算类型', trigger: 'change' }
        ],
        budgetYear: [
          { required: true, message: '请选择预算年度', trigger: 'change' }
        ],
        responsiblePerson: [
          { required: true, message: '请输入负责人', trigger: 'blur' }
        ],
        totalBudgetAmount: [
          { required: true, message: '请输入预算总额', trigger: 'blur' }
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
    budgetItemsTotal() {
      return this.form.budgetItems.reduce((total, item) => {
        return total + (item.budgetAmount || 0)
      }, 0)
    }
  },
  watch: {
    budgetData: {
      handler(newVal) {
        if (newVal && Object.keys(newVal).length > 0) {
          this.form = { 
            ...newVal,
            budgetItems: newVal.budgetItems || []
          }
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.form = {
        budgetName: '',
        budgetType: '',
        budgetYear: '',
        responsiblePerson: '',
        totalBudgetAmount: 0,
        budgetDepartment: '',
        plannedStartTime: '',
        plannedEndTime: '',
        budgetDescription: '',
        remarks: '',
        budgetItems: []
      }
    },
    addBudgetItem() {
      this.form.budgetItems.push({
        itemName: '',
        budgetAmount: 0,
        remarks: ''
      })
    },
    removeBudgetItem(index) {
      this.form.budgetItems.splice(index, 1)
    },
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          // 验证预算分项总额
          if (this.budgetItemsTotal > this.form.totalBudgetAmount) {
            this.$message.warning('预算分项总额不能超过预算总额')
            return
          }
          
          this.loading = true
          // 这里应该调用API保存数据
          setTimeout(() => {
            this.loading = false
            this.$message.success('保存成功')
            this.$emit('refresh')
            this.handleClose()
          }, 1000)
        }
      })
    }
  }
}
</script>

<style scoped>
.el-table {
  margin-top: 10px;
}
</style>
