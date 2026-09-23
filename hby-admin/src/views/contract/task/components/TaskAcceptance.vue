<template>
  <el-dialog
    title="任务书验收"
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

      <el-form-item label="交付成果">
        <el-input
          v-model="taskInfo.deliverables"
          type="textarea"
          :rows="2"
          disabled
        />
      </el-form-item>

      <el-form-item label="验收标准">
        <el-input
          v-model="taskInfo.acceptanceCriteria"
          type="textarea"
          :rows="2"
          disabled
        />
      </el-form-item>

      <el-form-item label="验收结果" prop="acceptanceResult">
        <el-radio-group v-model="form.acceptanceResult">
          <el-radio :label="1">验收通过</el-radio>
          <el-radio :label="2">需要整改</el-radio>
          <el-radio :label="3">验收不通过</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="验收意见" prop="acceptanceComments">
        <el-input
          v-model="form.acceptanceComments"
          type="textarea"
          :rows="4"
          placeholder="请输入验收意见"
        />
      </el-form-item>

      <el-form-item label="质量评价" prop="qualityEvaluation">
        <el-rate
          v-model="form.qualityEvaluation"
          :max="5"
          show-text
          :texts="['很差', '较差', '一般', '良好', '优秀']"
        />
      </el-form-item>

      <el-form-item label="进度评价" prop="scheduleEvaluation">
        <el-rate
          v-model="form.scheduleEvaluation"
          :max="5"
          show-text
          :texts="['很差', '较差', '一般', '良好', '优秀']"
        />
      </el-form-item>

      <el-form-item label="成本控制评价" prop="costControlEvaluation">
        <el-rate
          v-model="form.costControlEvaluation"
          :max="5"
          show-text
          :texts="['很差', '较差', '一般', '良好', '优秀']"
        />
      </el-form-item>

      <el-form-item label="验收日期" prop="acceptanceDate">
        <el-date-picker
          v-model="form.acceptanceDate"
          type="datetime"
          placeholder="选择验收日期"
          style="width: 100%"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="验收人员" prop="acceptancePersonnel">
        <el-input
          v-model="form.acceptancePersonnel"
          placeholder="请输入验收人员"
        />
      </el-form-item>

      <el-form-item label="整改要求" prop="rectificationRequirements" v-if="form.acceptanceResult === 2">
        <el-input
          v-model="form.rectificationRequirements"
          type="textarea"
          :rows="3"
          placeholder="请输入整改要求"
        />
      </el-form-item>

      <el-form-item label="整改期限" prop="rectificationDeadline" v-if="form.acceptanceResult === 2">
        <el-date-picker
          v-model="form.rectificationDeadline"
          type="date"
          placeholder="选择整改期限"
          style="width: 100%"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
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
  import { acceptTaskAssignment } from '@/api/contract/task'

  export default {
    name: 'TaskAcceptance',
    data() {
      return {
        dialogVisible: false,
        taskInfo: {},
        form: {
          id: null,
          acceptanceResult: null,
          acceptanceComments: '',
          qualityEvaluation: 5,
          scheduleEvaluation: 5,
          costControlEvaluation: 5,
          acceptanceDate: '',
          acceptancePersonnel: '',
          rectificationRequirements: '',
          rectificationDeadline: ''
        },
        rules: {
          acceptanceResult: [
            { required: true, message: '请选择验收结果', trigger: 'change' }
          ],
          acceptanceComments: [
            { required: true, message: '请输入验收意见', trigger: 'blur' }
          ],
          acceptanceDate: [
            { required: true, message: '请选择验收日期', trigger: 'change' }
          ],
          acceptancePersonnel: [
            { required: true, message: '请输入验收人员', trigger: 'blur' }
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
        this.form.acceptanceResult = null
        this.form.acceptanceComments = ''
        this.form.qualityEvaluation = 5
        this.form.scheduleEvaluation = 5
        this.form.costControlEvaluation = 5
        this.form.acceptanceDate = ''
        this.form.acceptancePersonnel = ''
        this.form.rectificationRequirements = ''
        this.form.rectificationDeadline = ''
      },
      
      handleClose() {
        this.dialogVisible = false
        this.form = {
          id: null,
          acceptanceResult: null,
          acceptanceComments: '',
          qualityEvaluation: 5,
          scheduleEvaluation: 5,
          costControlEvaluation: 5,
          acceptanceDate: '',
          acceptancePersonnel: '',
          rectificationRequirements: '',
          rectificationDeadline: ''
        }
        this.taskInfo = {}
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate()
        })
      },
      
      async handleSave() {
        try {
          await this.$refs.form.validate()
          
          const response = await acceptTaskAssignment(this.form.taskId, this.form)
          
          if (response.code === 200) {
            this.$message.success('验收完成')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.message || '验收失败')
          }
        } catch (error) {
          if (error.message) {
            this.$message.error('验收失败：' + error.message)
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
