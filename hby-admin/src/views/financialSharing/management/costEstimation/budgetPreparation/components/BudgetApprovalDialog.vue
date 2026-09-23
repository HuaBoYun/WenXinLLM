<template>
  <el-dialog
    title="预算审批"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
    append-to-body
  >
    <div class="approval-container">
      <!-- 预算基本信息 -->
      <div class="budget-info">
        <h3 class="info-title">预算信息</h3>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>预算编号：</label>
              <span>{{ budgetData.budgetCode }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>预算名称：</label>
              <span>{{ budgetData.budgetName }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>预算类型：</label>
              <el-tag :type="getBudgetTypeTag(budgetData.budgetType)">
                {{ getBudgetTypeText(budgetData.budgetType) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>预算金额：</label>
              <span class="amount-text">{{ formatAmount(budgetData.budgetAmount) }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>负责部门：</label>
              <span>{{ budgetData.responsibleDept }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>负责人：</label>
              <span>{{ budgetData.responsiblePerson }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div class="info-item">
              <label>预算描述：</label>
              <p class="description-text">{{ budgetData.budgetDescription || '暂无描述' }}</p>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 审批表单 -->
      <div class="approval-form">
        <h3 class="form-title">审批意见</h3>
        <el-form
          ref="approvalForm"
          :model="form"
          :rules="rules"
          label-width="100px"
        >
          <el-form-item label="审批结果" prop="approvalResult">
            <el-radio-group v-model="form.approvalResult">
              <el-radio label="1">通过</el-radio>
              <el-radio label="0">驳回</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="审批意见" prop="approvalComment">
            <el-input
              v-model="form.approvalComment"
              type="textarea"
              :rows="4"
              placeholder="请输入审批意见"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
          
          <el-form-item label="下一审批人" prop="nextApprover" v-if="form.approvalResult === '1'">
            <el-select
              v-model="form.nextApprover"
              placeholder="请选择下一审批人"
              clearable
              style="width: 100%"
            >
              <el-option
                v-for="user in approverList"
                :key="user.userId"
                :label="user.userName"
                :value="user.userId"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="审批级别" prop="approvalLevel">
            <el-select
              v-model="form.approvalLevel"
              placeholder="请选择审批级别"
              style="width: 100%"
            >
              <el-option label="部门审批" value="1" />
              <el-option label="财务审批" value="2" />
              <el-option label="总经理审批" value="3" />
              <el-option label="董事会审批" value="4" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="紧急程度" prop="urgencyLevel">
            <el-radio-group v-model="form.urgencyLevel">
              <el-radio label="1">普通</el-radio>
              <el-radio label="2">紧急</el-radio>
              <el-radio label="3">特急</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="预计完成时间" prop="expectedCompleteTime" v-if="form.approvalResult === '1'">
            <el-date-picker
              v-model="form.expectedCompleteTime"
              type="datetime"
              placeholder="选择预计完成时间"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
          
          <el-form-item label="附件上传">
            <el-upload
              ref="upload"
              :action="uploadUrl"
              :headers="uploadHeaders"
              :file-list="fileList"
              :on-success="handleUploadSuccess"
              :on-remove="handleRemoveFile"
              :before-upload="beforeUpload"
              multiple
              :limit="5"
            >
              <el-button size="small" type="primary">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png/pdf文件，且不超过10MB</div>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>

      <!-- 历史审批记录 -->
      <div class="approval-history" v-if="approvalHistory.length > 0">
        <h3 class="history-title">审批历史</h3>
        <el-timeline>
          <el-timeline-item
            v-for="(record, index) in approvalHistory"
            :key="index"
            :timestamp="record.approvalTime"
            placement="top"
          >
            <el-card>
              <div class="history-record">
                <div class="record-header">
                  <span class="approver">{{ record.approver }}</span>
                  <el-tag :type="record.result === '1' ? 'success' : 'danger'" size="small">
                    {{ record.result === '1' ? '通过' : '驳回' }}
                  </el-tag>
                </div>
                <div class="record-content" v-if="record.comment">
                  <p>{{ record.comment }}</p>
                </div>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitLoading">提 交</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { approveBudgetPreparation, getBudgetApprovalHistory, getApproverList } from '@/api/financialSharing/costEstimation'

export default {
  name: 'BudgetApprovalDialog',
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
      submitLoading: false,
      form: {
        budgetId: null,
        approvalResult: '1',
        approvalComment: '',
        nextApprover: '',
        approvalLevel: '1',
        urgencyLevel: '1',
        expectedCompleteTime: '',
        attachments: []
      },
      rules: {
        approvalResult: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ],
        approvalComment: [
          { required: true, message: '请输入审批意见', trigger: 'blur' }
        ]
      },
      approverList: [],
      approvalHistory: [],
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/zbgl/common/upload',
      uploadHeaders: {
        Authorization: 'Bearer ' + this.$store.getters.token
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
        this.initData()
      }
    }
  },
  methods: {
    initData() {
      this.form.budgetId = this.budgetData.budgetId
      this.loadApproverList()
      this.loadApprovalHistory()
    },
    loadApproverList() {
      getApproverList().then(response => {
        this.approverList = response.data || []
      })
    },
    loadApprovalHistory() {
      if (this.budgetData.budgetId) {
        getBudgetApprovalHistory(this.budgetData.budgetId).then(response => {
          this.approvalHistory = response.data || []
        })
      }
    },
    getBudgetTypeTag(type) {
      const tags = { '1': 'primary', '2': 'success', '3': 'warning', '4': 'info' }
      return tags[type] || 'info'
    },
    getBudgetTypeText(type) {
      const texts = { '1': '年度预算', '2': '季度预算', '3': '月度预算', '4': '项目预算' }
      return texts[type] || '未知'
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    beforeUpload(file) {
      const isValidType = ['image/jpeg', 'image/png', 'application/pdf'].includes(file.type)
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isValidType) {
        this.$message.error('上传文件只能是 JPG/PNG/PDF 格式!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      return true
    },
    handleUploadSuccess(response, file) {
      if (response.code === 200) {
        this.form.attachments.push({
          fileName: file.name,
          fileUrl: response.data.url
        })
      }
    },
    handleRemoveFile(file, fileList) {
      this.fileList = fileList
      // 从附件列表中移除
      const index = this.form.attachments.findIndex(item => item.fileName === file.name)
      if (index > -1) {
        this.form.attachments.splice(index, 1)
      }
    },
    handleSubmit() {
      this.$refs.approvalForm.validate(valid => {
        if (valid) {
          this.submitLoading = true
          approveBudgetPreparation(this.form).then(() => {
            this.$message.success('审批提交成功')
            this.dialogVisible = false
            this.$emit('refresh')
          }).finally(() => {
            this.submitLoading = false
          })
        }
      })
    },
    handleClose() {
      this.$refs.approvalForm.resetFields()
      this.fileList = []
      this.form.attachments = []
      this.dialogVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.approval-container {
  .budget-info, .approval-form, .approval-history {
    margin-bottom: 30px;
    
    .info-title, .form-title, .history-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 15px;
      padding-bottom: 8px;
      border-bottom: 2px solid #409EFF;
    }
    
    .info-item {
      margin-bottom: 15px;
      
      label {
        font-weight: 600;
        color: #606266;
        margin-right: 8px;
      }
      
      .description-text {
        margin: 8px 0 0 0;
        color: #606266;
        line-height: 1.6;
      }
    }
    
    .history-record {
      .record-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8px;
        
        .approver {
          font-weight: 600;
          color: #303133;
        }
      }
      
      .record-content {
        p {
          margin: 0;
          color: #606266;
          line-height: 1.6;
        }
      }
    }
  }
  
  .amount-text {
    font-weight: 600;
    color: #E6A23C;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
