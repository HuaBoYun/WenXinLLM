<template>
  <el-dialog
    title="项目策划审核"
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
      <el-form-item label="策划名称">
        <el-input v-model="planningInfo.planningName" disabled />
      </el-form-item>
      
      <el-form-item label="策划类型">
        <el-input v-model="planningTypeName" disabled />
      </el-form-item>
      
      <el-form-item label="策划人">
        <el-input v-model="planningInfo.plannerName" disabled />
      </el-form-item>

      <el-form-item label="项目概述">
        <el-input
          v-model="planningInfo.projectOverview"
          type="textarea"
          :rows="3"
          disabled
        />
      </el-form-item>

      <el-form-item label="项目目标">
        <el-input
          v-model="planningInfo.projectObjectives"
          type="textarea"
          :rows="3"
          disabled
        />
      </el-form-item>

      <el-form-item label="审核结果" prop="reviewResult">
        <el-radio-group v-model="form.reviewResult">
          <el-radio :label="1">审核通过</el-radio>
          <el-radio :label="2">需要修改</el-radio>
          <el-radio :label="3">审核不通过</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="审核意见" prop="reviewComments">
        <el-input
          v-model="form.reviewComments"
          type="textarea"
          :rows="4"
          placeholder="请输入审核意见"
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
  import { reviewProjectPlanning } from '@/api/contract/planning'

  export default {
    name: 'PlanningReview',
    data() {
      return {
        dialogVisible: false,
        planningInfo: {},
        form: {
          id: null,
          reviewResult: null,
          reviewComments: '',
          approvalDate: ''
        },
        rules: {
          reviewResult: [
            { required: true, message: '请选择审核结果', trigger: 'change' }
          ],
          reviewComments: [
            { required: true, message: '请输入审核意见', trigger: 'blur' }
          ],
          approvalDate: [
            { required: true, message: '请选择审批日期', trigger: 'change' }
          ]
        }
      }
    },
    computed: {
      planningTypeName() {
        const typeMap = {
          1: '初步策划',
          2: '详细策划',
          3: '实施策划'
        }
        return typeMap[this.planningInfo.planningType] || '未知'
      }
    },
    methods: {
      showEdit(data) {
        this.dialogVisible = true
        this.planningInfo = { ...data }
        this.form.id = data.id
        this.form.reviewResult = null
        this.form.reviewComments = ''
        this.form.approvalDate = ''
      },
      
      handleClose() {
        this.dialogVisible = false
        this.form = {
          id: null,
          reviewResult: null,
          reviewComments: '',
          approvalDate: ''
        }
        this.planningInfo = {}
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate()
        })
      },
      
      async handleSave() {
        try {
          await this.$refs.form.validate()
          
          const response = await reviewProjectPlanning(this.form)
          
          if (response.code === 200) {
            this.$message.success('审核完成')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.message || '审核失败')
          }
        } catch (error) {
          if (error.message) {
            this.$message.error('审核失败：' + error.message)
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
