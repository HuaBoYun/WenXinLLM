<template>
  <el-dialog
    title="预算审批"
    :visible.sync="dialogVisible"
    width="60%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item label="预算名称">
        <el-input v-model="budgetInfo.budgetName" disabled />
      </el-form-item>
      
      <el-form-item label="预算类型">
        <el-input v-model="budgetTypeName" disabled />
      </el-form-item>
      
      <el-form-item label="总预算">
        <el-input v-model="totalBudgetText" disabled />
      </el-form-item>

      <el-form-item label="预算编制人">
        <el-input v-model="budgetInfo.budgeterName" disabled />
      </el-form-item>

      <el-form-item label="预算依据">
        <el-input
          v-model="budgetInfo.budgetBasis"
          type="textarea"
          :rows="2"
          disabled
        />
      </el-form-item>

      <el-form-item label="审批结果" prop="approvalResult">
        <el-radio-group v-model="form.approvalResult">
          <el-radio :label="1">审批通过</el-radio>
          <el-radio :label="2">需要修改</el-radio>
          <el-radio :label="3">审批不通过</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="审批意见" prop="approvalComments">
        <el-input
          v-model="form.approvalComments"
          type="textarea"
          :rows="4"
          placeholder="请输入审批意见"
        />
      </el-form-item>

      <el-form-item label="审批日期" prop="approvalDate">
        <el-date-picker
          v-model="form.approvalDate"
          type="datetime"
          placeholder="选择审批日期"
          style="width: 100%"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { approveProjectBudget } from '@/api/contract/budget'

  export default {
    name: 'BudgetApproval',
    data() {
      return {
        dialogVisible: false,
        budgetInfo: {},
        form: {
          id: null,
          approvalResult: null,
          approvalComments: '',
          approvalDate: ''
        },
        rules: {
          approvalResult: [
            { required: true, message: '请选择审批结果', trigger: 'change' }
          ],
          approvalComments: [
            { required: true, message: '请输入审批意见', trigger: 'blur' }
          ],
          approvalDate: [
            { required: true, message: '请选择审批日期', trigger: 'change' }
          ]
        }
      }
    },
    computed: {
      budgetTypeName() {
        const typeMap = {
          1: '初步预算',
          2: '详细预算',
          3: '执行预算'
        }
        return typeMap[this.budgetInfo.budgetType] || '未知'
      },
      totalBudgetText() {
        if (!this.budgetInfo.totalBudget) return '0'
        return (this.budgetInfo.totalBudget / 10000).toFixed(2) + '万元'
      }
    },
    methods: {
      showEdit(data) {
        this.dialogVisible = true
        this.budgetInfo = { ...data }
        this.form.id = data.id
        this.form.approvalResult = null
        this.form.approvalComments = ''
        this.form.approvalDate = ''
      },
      
      handleClose() {
        this.dialogVisible = false
        this.form = {
          id: null,
          approvalResult: null,
          approvalComments: '',
          approvalDate: ''
        }
        this.budgetInfo = {}
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate()
        })
      },
      
      async handleSave() {
        try {
          await this.$refs.form.validate()
          
          const response = await approveProjectBudget(this.form.id, this.form)
          
          console.log('项目预算审批响应:', response)
          if (response.code === 1) {  // 1表示成功
            this.$message.success('审批完成')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.msg || '审批失败')  // 使用msg字段
          }
        } catch (error) {
          if (error.message) {
            this.$message.error('审批失败：' + error.message)
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
