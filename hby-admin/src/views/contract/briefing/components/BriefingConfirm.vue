<template>
  <el-dialog
    title="交底确认"
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
      <el-form-item label="交底标题">
        <el-input v-model="briefingInfo.briefingTitle" disabled />
      </el-form-item>
      
      <el-form-item label="交底类型">
        <el-input v-model="briefingTypeName" disabled />
      </el-form-item>
      
      <el-form-item label="交底人">
        <el-input v-model="briefingInfo.briefingPersonName" disabled />
      </el-form-item>

      <el-form-item label="接收人">
        <el-input v-model="briefingInfo.receiverName" disabled />
      </el-form-item>

      <el-form-item label="交底内容">
        <el-input
          v-model="briefingInfo.briefingContent"
          type="textarea"
          :rows="4"
          disabled
        />
      </el-form-item>

      <el-form-item label="技术要求">
        <el-input
          v-model="briefingInfo.technicalRequirements"
          type="textarea"
          :rows="3"
          disabled
        />
      </el-form-item>

      <el-form-item label="安全要求">
        <el-input
          v-model="briefingInfo.safetyRequirements"
          type="textarea"
          :rows="3"
          disabled
        />
      </el-form-item>

      <el-form-item label="确认结果" prop="confirmationResult">
        <el-radio-group v-model="form.confirmationResult">
          <el-radio :label="1">确认接受</el-radio>
          <el-radio :label="2">有疑问</el-radio>
          <el-radio :label="3">不接受</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="确认意见" prop="confirmationComments">
        <el-input
          v-model="form.confirmationComments"
          type="textarea"
          :rows="4"
          placeholder="请输入确认意见"
        />
      </el-form-item>

      <el-form-item label="疑问或建议" prop="questionsOrSuggestions" v-if="form.confirmationResult === 2">
        <el-input
          v-model="form.questionsOrSuggestions"
          type="textarea"
          :rows="3"
          placeholder="请输入疑问或建议"
        />
      </el-form-item>

      <el-form-item label="不接受原因" prop="rejectionReason" v-if="form.confirmationResult === 3">
        <el-input
          v-model="form.rejectionReason"
          type="textarea"
          :rows="3"
          placeholder="请输入不接受原因"
        />
      </el-form-item>

      <el-form-item label="确认日期" prop="confirmationDate">
        <el-date-picker
          v-model="form.confirmationDate"
          type="datetime"
          placeholder="选择确认日期"
          style="width: 100%"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="确认人签名" prop="confirmationSignature">
        <el-input v-model="form.confirmationSignature" placeholder="请输入确认人签名" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { confirmProjectBriefing } from '@/api/contract/briefing'

  export default {
    name: 'BriefingConfirm',
    data() {
      return {
        dialogVisible: false,
        briefingInfo: {},
        form: {
          id: null,
          confirmationResult: null,
          confirmationComments: '',
          questionsOrSuggestions: '',
          rejectionReason: '',
          confirmationDate: '',
          confirmationSignature: ''
        },
        rules: {
          confirmationResult: [
            { required: true, message: '请选择确认结果', trigger: 'change' }
          ],
          confirmationComments: [
            { required: true, message: '请输入确认意见', trigger: 'blur' }
          ],
          confirmationDate: [
            { required: true, message: '请选择确认日期', trigger: 'change' }
          ],
          confirmationSignature: [
            { required: true, message: '请输入确认人签名', trigger: 'blur' }
          ]
        }
      }
    },
    computed: {
      briefingTypeName() {
        const typeMap = {
          1: '技术交底',
          2: '安全交底',
          3: '质量交底',
          4: '环保交底'
        }
        return typeMap[this.briefingInfo.briefingType] || '未知'
      }
    },
    methods: {
      showEdit(data) {
        this.dialogVisible = true
        this.briefingInfo = { ...data }
        this.form.id = data.id
        this.form.confirmationResult = null
        this.form.confirmationComments = ''
        this.form.questionsOrSuggestions = ''
        this.form.rejectionReason = ''
        this.form.confirmationDate = ''
        this.form.confirmationSignature = ''
      },
      
      handleClose() {
        this.dialogVisible = false
        this.form = {
          id: null,
          confirmationResult: null,
          confirmationComments: '',
          questionsOrSuggestions: '',
          rejectionReason: '',
          confirmationDate: '',
          confirmationSignature: ''
        }
        this.briefingInfo = {}
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate()
        })
      },
      
      async handleSave() {
        try {
          await this.$refs.form.validate()
          
          const response = await confirmProjectBriefing(this.form.id, this.form)
          
          if (response.code === 200) {
            this.$message.success('确认完成')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.message || '确认失败')
          }
        } catch (error) {
          if (error.message) {
            this.$message.error('确认失败：' + error.message)
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
