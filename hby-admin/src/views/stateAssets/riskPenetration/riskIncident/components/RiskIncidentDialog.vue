<template>
  <el-dialog
    :title="dialogType === 'add' ? '报告风险事件' : '编辑风险事件'"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseId">
            <CompanyTreeModal
              v-model="form.enterpriseId"
              :company-name.sync="form.enterpriseName"
              placeholder="请选择企业"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="事件标题" prop="incidentTitle">
            <el-input
              v-model="form.incidentTitle"
              placeholder="请输入事件标题"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="事件类型" prop="incidentType">
            <el-select v-model="form.incidentType" placeholder="请选择事件类型" style="width: 100%">
              <el-option label="财务风险" value="FINANCIAL" />
              <el-option label="运营风险" value="OPERATIONAL" />
              <el-option label="合规风险" value="COMPLIANCE" />
              <el-option label="信息安全" value="INFORMATION_SECURITY" />
              <el-option label="市场风险" value="MARKET" />
              <el-option label="信用风险" value="CREDIT" />
              <el-option label="其他" value="OTHER" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级" prop="riskLevel">
            <el-select v-model="form.riskLevel" placeholder="请选择风险等级" style="width: 100%">
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
              <el-option label="极高风险" value="CRITICAL" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="发生时间" prop="occurrenceTime">
            <el-date-picker
              v-model="form.occurrenceTime"
              type="datetime"
              placeholder="选择发生时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发现时间" prop="discoveryTime">
            <el-date-picker
              v-model="form.discoveryTime"
              type="datetime"
              placeholder="选择发现时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="事件描述" prop="incidentDescription">
        <el-input
          v-model="form.incidentDescription"
          type="textarea"
          :rows="4"
          placeholder="请详细描述事件的具体情况、发生过程等"
          maxlength="1000"
          show-word-limit
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="影响范围" prop="impactScope">
            <el-select v-model="form.impactScope" placeholder="请选择影响范围" style="width: 100%">
              <el-option label="部门级" value="DEPARTMENT" />
              <el-option label="企业级" value="ENTERPRISE" />
              <el-option label="集团级" value="GROUP" />
              <el-option label="行业级" value="INDUSTRY" />
              <el-option label="社会级" value="SOCIETY" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="紧急程度" prop="urgencyLevel">
            <el-select v-model="form.urgencyLevel" placeholder="请选择紧急程度" style="width: 100%">
              <el-option label="一般" value="NORMAL" />
              <el-option label="紧急" value="URGENT" />
              <el-option label="非常紧急" value="VERY_URGENT" />
              <el-option label="特急" value="CRITICAL" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input
              v-model="form.responsiblePerson"
              placeholder="请输入负责人姓名"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input
              v-model="form.contactPhone"
              placeholder="请输入联系电话"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 经济损失评估 -->
      <div class="section-title">
        <h4>经济损失评估</h4>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="直接损失" prop="directLoss">
            <el-input-number
              v-model="form.directLoss"
              :precision="2"
              :min="0"
              placeholder="直接损失金额"
              style="width: 100%"
              @change="calculateTotalLoss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="间接损失" prop="indirectLoss">
            <el-input-number
              v-model="form.indirectLoss"
              :precision="2"
              :min="0"
              placeholder="间接损失金额"
              style="width: 100%"
              @change="calculateTotalLoss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="总损失" prop="economicLoss">
            <el-input-number
              v-model="form.economicLoss"
              :precision="2"
              :min="0"
              placeholder="总损失金额"
              style="width: 100%"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 风险因素 -->
      <el-form-item label="风险因素" prop="riskFactors">
        <el-input
          v-model="form.riskFactors"
          type="textarea"
          :rows="3"
          placeholder="请描述导致事件发生的风险因素，多个因素用分号分隔"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <!-- 初步原因分析 -->
      <el-form-item label="初步原因分析" prop="preliminaryCause">
        <el-input
          v-model="form.preliminaryCause"
          type="textarea"
          :rows="3"
          placeholder="请进行初步的原因分析"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <!-- 已采取措施 -->
      <el-form-item label="已采取措施" prop="actionsTaken">
        <el-input
          v-model="form.actionsTaken"
          type="textarea"
          :rows="3"
          placeholder="请描述已经采取的应急措施和处理行动"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <!-- 后续计划 -->
      <el-form-item label="后续计划" prop="followUpPlan">
        <el-input
          v-model="form.followUpPlan"
          type="textarea"
          :rows="3"
          placeholder="请描述后续的处理计划和预防措施"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <!-- 相关附件 -->
      <el-form-item label="相关附件" prop="attachments">
        <el-upload
          ref="upload"
          :action="uploadUrl"
          :headers="uploadHeaders"
          :file-list="fileList"
          :on-success="handleUploadSuccess"
          :on-remove="handleRemoveFile"
          :before-upload="beforeUpload"
          multiple
          :limit="10"
        >
          <el-button size="small" type="primary">
            <i class="el-icon-upload"></i>
            上传附件
          </el-button>
          <div slot="tip" class="el-upload__tip">
            支持上传图片、文档等文件，单个文件不超过20MB，最多10个文件
          </div>
        </el-upload>
      </el-form-item>

      <!-- 是否需要立即处理 -->
      <el-form-item label="处理设置">
        <el-checkbox v-model="form.requireImmediateAction">需要立即处理</el-checkbox>
        <el-checkbox v-model="form.notifyManagement">通知管理层</el-checkbox>
        <el-checkbox v-model="form.reportToRegulator">上报监管部门</el-checkbox>
      </el-form-item>

      <!-- 备注 -->
      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="2"
          placeholder="其他需要说明的情况"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        {{ dialogType === 'add' ? '提交报告' : '更新事件' }}
      </el-button>
      <el-button
        v-if="dialogType === 'add'"
        type="success"
        @click="handleSubmitAndProcess"
        :loading="loading"
      >
        提交并开始处理
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addRiskIncident, updateRiskIncident, startProcessing } from '@/api/stateAssets/riskIncident'
import CompanyTreeModal from '@/components/CompanyTreeModal'

export default {
  name: 'RiskIncidentDialog',
  components: {
    CompanyTreeModal
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add'
    }
  },
  data() {
    return {
      loading: false,
      form: {
        enterpriseId: '',
        enterpriseName: '',
        incidentTitle: '',
        incidentType: '',
        riskLevel: '',
        occurrenceTime: '',
        discoveryTime: '',
        incidentDescription: '',
        impactScope: '',
        urgencyLevel: '',
        responsiblePerson: '',
        contactPhone: '',
        directLoss: null,
        indirectLoss: null,
        economicLoss: null,
        riskFactors: '',
        preliminaryCause: '',
        actionsTaken: '',
        followUpPlan: '',
        attachments: [],
        requireImmediateAction: false,
        notifyManagement: false,
        reportToRegulator: false,
        remarks: ''
      },
      rules: {
        enterpriseId: [
          { required: true, message: '请选择企业', trigger: 'change' }
        ],
        incidentTitle: [
          { required: true, message: '请输入事件标题', trigger: 'blur' }
        ],
        incidentType: [
          { required: true, message: '请选择事件类型', trigger: 'change' }
        ],
        riskLevel: [
          { required: true, message: '请选择风险等级', trigger: 'change' }
        ],
        occurrenceTime: [
          { required: true, message: '请选择发生时间', trigger: 'change' }
        ],
        incidentDescription: [
          { required: true, message: '请输入事件描述', trigger: 'blur' }
        ],
        impactScope: [
          { required: true, message: '请选择影响范围', trigger: 'change' }
        ],
        urgencyLevel: [
          { required: true, message: '请选择紧急程度', trigger: 'change' }
        ],
        responsiblePerson: [
          { required: true, message: '请输入负责人', trigger: 'blur' }
        ],
        contactPhone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ]
      },
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/common/upload',
      uploadHeaders: {
        'Authorization': 'Bearer ' + this.$store.getters.token
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
    // 初始化表单
    initForm() {
      if (this.dialogType === 'edit' && this.formData.riskIncidentId) {
        this.form = {
          ...this.formData,
          attachments: this.formData.attachments ? this.formData.attachments.split(',') : [],
          requireImmediateAction: this.formData.requireImmediateAction || false,
          notifyManagement: this.formData.notifyManagement || false,
          reportToRegulator: this.formData.reportToRegulator || false
        }
        this.fileList = this.form.attachments.map((url, index) => ({
          name: `附件${index + 1}`,
          url: url
        }))
      } else {
        this.form = {
          enterpriseId: '',
          enterpriseName: '',
          incidentTitle: '',
          incidentType: '',
          riskLevel: '',
          occurrenceTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
          discoveryTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
          incidentDescription: '',
          impactScope: '',
          urgencyLevel: '',
          responsiblePerson: this.$store.getters.userInfo.userName,
          contactPhone: '',
          directLoss: null,
          indirectLoss: null,
          economicLoss: null,
          riskFactors: '',
          preliminaryCause: '',
          actionsTaken: '',
          followUpPlan: '',
          attachments: [],
          requireImmediateAction: false,
          notifyManagement: false,
          reportToRegulator: false,
          remarks: ''
        }
        this.fileList = []
      }
    },

    // 计算总损失
    calculateTotalLoss() {
      const directLoss = this.form.directLoss || 0
      const indirectLoss = this.form.indirectLoss || 0
      this.form.economicLoss = directLoss + indirectLoss
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.loading = true

        const formData = {
          ...this.form,
          attachments: this.form.attachments.join(','),
          reportBy: this.$store.getters.userInfo.userName,
          reportTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
        }

        let response
        if (this.dialogType === 'add') {
          response = await addRiskIncident(formData)
        } else {
          response = await updateRiskIncident(formData)
        }

        if (response.code === 200) {
          this.$message.success(this.dialogType === 'add' ? '事件报告提交成功' : '事件更新成功')
          this.$emit('success')
          this.handleClose()
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('操作失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 提交并开始处理
    async handleSubmitAndProcess() {
      try {
        await this.$refs.form.validate()
        this.loading = true

        const formData = {
          ...this.form,
          attachments: this.form.attachments.join(','),
          reportBy: this.$store.getters.userInfo.userName,
          reportTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
        }

        // 先提交事件
        const addResponse = await addRiskIncident(formData)
        
        if (addResponse.code === 200) {
          // 再开始处理
          const processResponse = await startProcessing({
            riskIncidentId: addResponse.data.riskIncidentId,
            startBy: this.$store.getters.userInfo.userName,
            processingStartTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
          })
          
          if (processResponse.code === 200) {
            this.$message.success('事件报告提交成功，已开始处理')
            this.$emit('success')
            this.handleClose()
          }
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('操作失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 文件上传处理
    beforeUpload(file) {
      const isValidType = ['image/jpeg', 'image/png', 'image/gif', 'application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'].includes(file.type)
      const isLt20M = file.size / 1024 / 1024 < 20

      if (!isValidType) {
        this.$message.error('只能上传图片、PDF、Word、Excel格式的文件!')
        return false
      }
      if (!isLt20M) {
        this.$message.error('上传文件大小不能超过 20MB!')
        return false
      }
      return true
    },

    handleUploadSuccess(response, file) {
      if (response.code === 200) {
        this.form.attachments.push(response.data.url)
        this.$message.success('文件上传成功')
      } else {
        this.$message.error('文件上传失败：' + response.message)
      }
    },

    handleRemoveFile(file, fileList) {
      const index = this.form.attachments.indexOf(file.response?.data?.url || file.url)
      if (index > -1) {
        this.form.attachments.splice(index, 1)
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
      this.fileList = []
    }
  }
}
</script>

<style lang="scss" scoped>
.section-title {
  margin: 20px 0 10px 0;
  padding-bottom: 8px;
  border-bottom: 1px solid #ebeef5;

  h4 {
    margin: 0;
    color: #303133;
    font-size: 16px;
    font-weight: 600;
  }
}

.dialog-footer {
  text-align: right;
}

.el-upload__tip {
  color: #909399;
  font-size: 12px;
  line-height: 1.5;
}
</style>
