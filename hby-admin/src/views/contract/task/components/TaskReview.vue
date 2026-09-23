<template>
  <el-dialog
    title="任务书审核"
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
      <el-form-item label="任务书名称">
        <el-input v-model="taskInfo.taskName" disabled />
      </el-form-item>
      
      <el-form-item label="任务书类型">
        <el-input v-model="taskTypeName" disabled />
      </el-form-item>
      
      <el-form-item label="任务负责人">
        <el-input v-model="taskInfo.taskLeaderName" disabled />
      </el-form-item>

      <el-form-item label="任务目标">
        <el-input
          v-model="taskInfo.taskObjectives"
          type="textarea"
          :rows="3"
          disabled
        />
      </el-form-item>

      <el-form-item label="任务范围">
        <el-input
          v-model="taskInfo.taskScope"
          type="textarea"
          :rows="3"
          disabled
        />
      </el-form-item>

      <el-form-item label="交付成果">
        <el-input
          v-model="taskInfo.deliverables"
          type="textarea"
          :rows="2"
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

      <el-form-item label="审核日期" prop="reviewDate">
        <el-date-picker
          v-model="form.reviewDate"
          type="datetime"
          placeholder="选择审核日期"
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
  import { reviewTaskAssignment } from '@/api/contract/task'

  export default {
    name: 'TaskReview',
    data() {
      return {
        dialogVisible: false,
        taskInfo: {},
        form: {
          id: null,
          reviewResult: null,
          reviewComments: '',
          reviewDate: ''
        },
        rules: {
          reviewResult: [
            { required: true, message: '请选择审核结果', trigger: 'change' }
          ],
          reviewComments: [
            { required: true, message: '请输入审核意见', trigger: 'blur' }
          ],
          reviewDate: [
            { required: true, message: '请选择审核日期', trigger: 'change' }
          ]
        }
      }
    },
    computed: {
      taskTypeName() {
        const typeMap = {
          1: '勘察任务书',
          2: '设计任务书',
          3: '施工任务书',
          4: '监理任务书'
        }
        return typeMap[this.taskInfo.taskType] || '未知'
      }
    },
    methods: {
      showEdit(data) {
        this.dialogVisible = true
        this.taskInfo = { ...data }
        this.form.id = data.id
        this.form.reviewResult = null
        this.form.reviewComments = ''
        this.form.reviewDate = ''
      },
      
      handleClose() {
        this.dialogVisible = false
        this.form = {
          id: null,
          reviewResult: null,
          reviewComments: '',
          reviewDate: ''
        }
        this.taskInfo = {}
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate()
        })
      },
      
      async handleSave() {
        try {
          await this.$refs.form.validate()
          
          const response = await reviewTaskAssignment(this.form.taskId, this.form)
          
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
