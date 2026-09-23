<template>
  <el-dialog
    title="保证金退还"
    :visible.sync="visible"
    width="700px"
    :before-close="handleClose"
    append-to-body
  >
    <div v-loading="loading" class="refund-form">
      <!-- 保证金信息 -->
      <el-card class="info-card" shadow="never">
        <div slot="header" class="card-header">
          <span class="card-title">保证金信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>项目名称：</label>
              <span>{{ depositInfo.projectName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>保证金类型：</label>
              <span>{{ getDepositTypeText(depositInfo.depositType) }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>原缴金额：</label>
              <span class="amount">{{ formatAmount(depositInfo.depositAmount) }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>缴纳单位：</label>
              <span>{{ depositInfo.payerName || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 退还表单 -->
      <el-form
        ref="refundForm"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        size="small"
      >
        <el-form-item label="退还金额" prop="refundAmount">
          <el-input-number
            v-model="formData.refundAmount"
            :precision="2"
            :min="0"
            :max="depositInfo.depositAmount"
            controls-position="right"
            style="width: 100%"
            placeholder="请输入退还金额"
          />
          <div class="form-tip">
            最大可退还金额：{{ formatAmount(depositInfo.depositAmount) }}
          </div>
        </el-form-item>

        <el-form-item label="退还方式" prop="refundMethod">
          <el-select
            v-model="formData.refundMethod"
            placeholder="请选择退还方式"
            style="width: 100%"
          >
            <el-option label="银行转账" value="BANK_TRANSFER" />
            <el-option label="现金退还" value="CASH" />
            <el-option label="支票退还" value="CHECK" />
          </el-select>
        </el-form-item>

        <el-form-item label="退还日期" prop="refundDate">
          <el-date-picker
            v-model="formData.refundDate"
            type="date"
            placeholder="请选择退还日期"
            style="width: 100%"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <el-form-item label="收款账户" prop="receiverAccount" v-if="showAccountInfo">
          <el-input
            v-model="formData.receiverAccount"
            placeholder="请输入收款银行账户信息"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="收款人" prop="receiverName">
          <el-input
            v-model="formData.receiverName"
            placeholder="请输入收款人姓名"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="联系电话" prop="receiverPhone">
          <el-input
            v-model="formData.receiverPhone"
            placeholder="请输入收款人联系电话"
            maxlength="20"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="退还原因" prop="refundReason">
          <el-select
            v-model="formData.refundReason"
            placeholder="请选择退还原因"
            style="width: 100%"
          >
            <el-option label="项目完成" value="PROJECT_COMPLETED" />
            <el-option label="合同履行完毕" value="CONTRACT_FULFILLED" />
            <el-option label="项目取消" value="PROJECT_CANCELLED" />
            <el-option label="投标失败" value="BID_FAILED" />
            <el-option label="其他原因" value="OTHER" />
          </el-select>
        </el-form-item>

        <el-form-item label="经办人" prop="handler">
          <el-input
            v-model="formData.handler"
            placeholder="请输入经办人姓名"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="凭证号码" prop="voucherNumber">
          <el-input
            v-model="formData.voucherNumber"
            placeholder="请输入退还凭证号"
            maxlength="50"
            show-word-limit
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
            <div slot="tip" class="el-upload__tip">
              支持上传退还凭证、申请书等相关文件，单个文件不超过10MB，最多5个文件
            </div>
          </el-upload>
        </el-form-item>

        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="formData.remarks"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
        确认退还
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { refundDeposit, getDepositById } from '@/api/contract/bidding'

export default {
  name: 'DepositRefund',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    depositId: {
      type: [String, Number],
      default: null
    }
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      depositInfo: {},
      formData: {
        refundAmount: null,
        refundMethod: '',
        refundDate: '',
        receiverAccount: '',
        receiverName: '',
        receiverPhone: '',
        refundReason: '',
        handler: '',
        voucherNumber: '',
        attachments: [],
        remarks: ''
      },
      formRules: {
        refundAmount: [
          { required: true, message: '请输入退还金额', trigger: 'blur' }
        ],
        refundMethod: [
          { required: true, message: '请选择退还方式', trigger: 'change' }
        ],
        refundDate: [
          { required: true, message: '请选择退还日期', trigger: 'change' }
        ],
        receiverName: [
          { required: true, message: '请输入收款人姓名', trigger: 'blur' }
        ],
        receiverPhone: [
          { required: true, message: '请输入收款人联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        refundReason: [
          { required: true, message: '请选择退还原因', trigger: 'change' }
        ],
        handler: [
          { required: true, message: '请输入经办人姓名', trigger: 'blur' }
        ]
      },
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/contract/common/upload',
      uploadHeaders: {
        'Authorization': 'Bearer ' + this.$store.getters.token
      }
    }
  },
  computed: {
    showAccountInfo() {
      return ['BANK_TRANSFER', 'CHECK'].includes(this.formData.refundMethod)
    }
  },
  watch: {
    visible(val) {
      if (val && this.depositId) {
        this.loadDepositInfo()
      } else {
        this.resetForm()
      }
    }
  },
  methods: {
    async loadDepositInfo() {
      if (!this.depositId) return

      this.loading = true
      try {
        const response = await getDepositById(this.depositId)
        this.depositInfo = response.data || {}
        // 设置默认退还金额为原缴金额
        this.formData.refundAmount = this.depositInfo.depositAmount
        // 设置默认收款人信息
        this.formData.receiverName = this.depositInfo.contactPerson
        this.formData.receiverPhone = this.depositInfo.contactPhone
      } catch (error) {
        console.error('加载保证金信息失败:', error)
        this.$message.error('加载信息失败')
      } finally {
        this.loading = false
      }
    },
    getDepositTypeText(type) {
      const typeMap = {
        'BIDDING': '投标保证金',
        'PERFORMANCE': '履约保证金',
        'QUALITY': '质量保证金',
        'OTHER': '其他保证金'
      }
      return typeMap[type] || '未知'
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return `¥${Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
    },
    beforeUpload(file) {
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
      }
      return isLt10M
    },
    handleUploadSuccess(response, file) {
      if (response.code === 200) {
        this.formData.attachments.push({
          name: file.name,
          url: response.data.url
        })
        this.$message.success('文件上传成功')
      } else {
        this.$message.error(response.message || '文件上传失败')
      }
    },
    handleRemoveFile(file, fileList) {
      const index = this.formData.attachments.findIndex(item => item.name === file.name)
      if (index > -1) {
        this.formData.attachments.splice(index, 1)
      }
    },
    resetForm() {
      this.formData = {
        refundAmount: null,
        refundMethod: '',
        refundDate: '',
        receiverAccount: '',
        receiverName: '',
        receiverPhone: '',
        refundReason: '',
        handler: '',
        voucherNumber: '',
        attachments: [],
        remarks: ''
      }
      this.fileList = []
      this.depositInfo = {}
      this.$nextTick(() => {
        this.$refs.refundForm && this.$refs.refundForm.clearValidate()
      })
    },
    handleSubmit() {
      this.$refs.refundForm.validate(async (valid) => {
        if (!valid) return

        this.submitLoading = true
        try {
          const submitData = {
            ...this.formData,
            depositId: this.depositId
          }
          await refundDeposit(submitData)
          this.$message.success('退还成功')
          this.$emit('success')
          this.handleClose()
        } catch (error) {
          console.error('退还失败:', error)
          this.$message.error(error.message || '退还失败')
        } finally {
          this.submitLoading = false
        }
      })
    },
    handleClose() {
      this.$emit('update:visible', false)
      this.resetForm()
    }
  }
}
</script>

<style scoped>
.refund-form {
  max-height: 600px;
  overflow-y: auto;
}

.info-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-weight: bold;
  font-size: 16px;
}

.info-item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.info-item label {
  font-weight: bold;
  color: #606266;
  min-width: 100px;
  margin-right: 10px;
}

.info-item span {
  color: #303133;
}

.amount {
  font-weight: bold;
  color: #E6A23C;
  font-size: 16px;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.dialog-footer {
  text-align: right;
}
</style>
