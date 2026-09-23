<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      :disabled="isDetail"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算名称" prop="budgetName">
            <el-input v-model="form.budgetName" placeholder="请输入预算名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算类型" prop="budgetType">
            <el-select v-model="form.budgetType" placeholder="请选择预算类型" style="width: 100%">
              <el-option label="初步预算" :value="1" />
              <el-option label="详细预算" :value="2" />
              <el-option label="执行预算" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目ID" prop="projectId">
            <el-input v-model="form.projectId" placeholder="请输入项目ID" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算版本" prop="budgetVersion">
            <el-input v-model="form.budgetVersion" placeholder="请输入预算版本" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="总预算" prop="totalBudget">
            <el-input-number
              v-model="form.totalBudget"
              :min="0"
              :precision="2"
              placeholder="请输入总预算"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算编制人" prop="budgeterId">
            <el-select v-model="form.budgeterId" placeholder="请选择预算编制人" style="width: 100%">
              <el-option label="张三" :value="1" />
              <el-option label="李四" :value="2" />
              <el-option label="王五" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算日期" prop="budgetDate">
            <el-date-picker
              v-model="form.budgetDate"
              type="date"
              placeholder="选择预算日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算期间开始" prop="budgetPeriodStart">
            <el-date-picker
              v-model="form.budgetPeriodStart"
              type="date"
              placeholder="选择预算期间开始"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算期间结束" prop="budgetPeriodEnd">
            <el-date-picker
              v-model="form.budgetPeriodEnd"
              type="date"
              placeholder="选择预算期间结束"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险准备金率(%)" prop="riskContingencyRate">
            <el-input-number
              v-model="form.riskContingencyRate"
              :min="0"
              :max="100"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险准备金" prop="riskContingencyAmount">
            <el-input-number
              v-model="form.riskContingencyAmount"
              :min="0"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="管理费率(%)" prop="managementFeeRate">
            <el-input-number
              v-model="form.managementFeeRate"
              :min="0"
              :max="100"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="管理费" prop="managementFeeAmount">
            <el-input-number
              v-model="form.managementFeeAmount"
              :min="0"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="利润率(%)" prop="profitRate">
            <el-input-number
              v-model="form.profitRate"
              :min="0"
              :max="100"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="利润" prop="profitAmount">
            <el-input-number
              v-model="form.profitAmount"
              :min="0"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="税率(%)" prop="taxRate">
            <el-input-number
              v-model="form.taxRate"
              :min="0"
              :max="100"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="税金" prop="taxAmount">
            <el-input-number
              v-model="form.taxAmount"
              :min="0"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算状态" prop="budgetStatus" v-if="isDetail">
            <el-select v-model="form.budgetStatus" disabled style="width: 100%">
              <el-option label="草稿" :value="1" />
              <el-option label="待审批" :value="2" />
              <el-option label="已审批" :value="3" />
              <el-option label="已批准" :value="4" />
              <el-option label="执行中" :value="5" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="预算依据" prop="budgetBasis">
        <el-input
          v-model="form.budgetBasis"
          type="textarea"
          :rows="2"
          placeholder="请输入预算依据"
        />
      </el-form-item>

      <el-form-item label="预算假设" prop="budgetAssumptions">
        <el-input
          v-model="form.budgetAssumptions"
          type="textarea"
          :rows="2"
          placeholder="请输入预算假设"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave" v-if="!isDetail">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    createProjectBudget,
    updateProjectBudget
  } from '@/api/contract/budget'

  export default {
    name: 'BudgetEdit',
    data() {
      return {
        dialogVisible: false,
        isDetail: false,
        title: '',
        form: {
          id: null,
          projectId: null,
          budgetName: '',
          budgetType: null,
          budgetVersion: '',
          totalBudget: null,
          budgetPeriodStart: '',
          budgetPeriodEnd: '',
          budgetBasis: '',
          budgetAssumptions: '',
          riskContingencyRate: 5.00,
          riskContingencyAmount: null,
          managementFeeRate: 8.00,
          managementFeeAmount: null,
          profitRate: 10.00,
          profitAmount: null,
          taxRate: 13.00,
          taxAmount: null,
          budgeterId: null,
          budgetDate: '',
          budgetStatus: 1
        },
        rules: {
          budgetName: [
            { required: true, message: '请输入预算名称', trigger: 'blur' }
          ],
          budgetType: [
            { required: true, message: '请选择预算类型', trigger: 'change' }
          ],
          projectId: [
            { required: true, message: '请输入项目ID', trigger: 'blur' }
          ],
          totalBudget: [
            { required: true, message: '请输入总预算', trigger: 'blur' },
            {
              validator: (rule, value, callback) => {
                if (value !== null && value !== undefined && value <= 0) {
                  callback(new Error('预算总金额必须大于0'))
                } else {
                  callback()
                }
              },
              trigger: 'blur'
            }
          ],
          budgeterId: [
            { required: true, message: '请选择预算编制人', trigger: 'change' }
          ],
          budgetDate: [
            { required: true, message: '请选择预算日期', trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      showEdit(type, data) {
        this.dialogVisible = true
        this.isDetail = type === 'detail'
        
        if (type === 'add') {
          this.title = '新建项目预算'
          this.resetForm()
        } else if (type === 'edit') {
          this.title = '编辑项目预算'
          this.form = { ...data }
        } else if (type === 'detail') {
          this.title = '项目预算详情'
          this.form = { ...data }
        }
      },
      
      resetForm() {
        this.form = {
          id: null,
          projectId: null,
          budgetName: '',
          budgetType: null,
          budgetVersion: '',
          totalBudget: null,
          budgetPeriodStart: '',
          budgetPeriodEnd: '',
          budgetBasis: '',
          budgetAssumptions: '',
          riskContingencyRate: 5.00,
          riskContingencyAmount: null,
          managementFeeRate: 8.00,
          managementFeeAmount: null,
          profitRate: 10.00,
          profitAmount: null,
          taxRate: 13.00,
          taxAmount: null,
          budgeterId: null,
          budgetDate: '',
          budgetStatus: 1
        }
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate()
        })
      },
      
      handleClose() {
        this.dialogVisible = false
        this.resetForm()
      },
      
      async handleSave() {
        try {
          await this.$refs.form.validate()
          
          let response
          if (this.form.id) {
            response = await updateProjectBudget(this.form)
          } else {
            response = await createProjectBudget(this.form)
          }
          
          console.log('项目预算保存响应:', response)
          if (response.code === 1) {  // 1表示成功
            this.$message.success(this.form.id ? '更新成功' : '创建成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.msg || '操作失败')  // 使用msg字段
          }
        } catch (error) {
          if (error.message) {
            this.$message.error('操作失败：' + error.message)
          }
        }
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
