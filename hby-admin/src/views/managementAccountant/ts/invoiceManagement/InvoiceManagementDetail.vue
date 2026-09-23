<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="invoice-detail">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form
            ref="basicForm"
            :model="formData"
            :rules="formRules"
            label-width="120px"
            size="small"
          >
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="发票代码" prop="invoiceCode">
                  <el-input
                    v-model="formData.invoiceCode"
                    :disabled="isViewMode"
                    placeholder="请输入发票代码"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发票号码" prop="invoiceNumber">
                  <el-input
                    v-model="formData.invoiceNumber"
                    :disabled="isViewMode"
                    placeholder="请输入发票号码"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="发票类型" prop="invoiceType">
                  <el-select
                    v-model="formData.invoiceType"
                    :disabled="isViewMode"
                    placeholder="请选择发票类型"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="item in INVOICE_TYPES"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="开票日期" prop="invoiceDate">
                  <el-date-picker
                    v-model="formData.invoiceDate"
                    :disabled="isViewMode"
                    type="datetime"
                    placeholder="请选择开票日期"
                    style="width: 100%"
                    value-format="yyyy-MM-dd HH:mm:ss"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="销售方名称" prop="sellerName">
                  <el-input
                    v-model="formData.sellerName"
                    :disabled="isViewMode"
                    placeholder="请输入销售方名称"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="销售方税号" prop="sellerTaxNumber">
                  <el-input
                    v-model="formData.sellerTaxNumber"
                    :disabled="isViewMode"
                    placeholder="请输入销售方纳税人识别号"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="销售方地址电话" prop="sellerAddressPhone">
                  <el-input
                    v-model="formData.sellerAddressPhone"
                    :disabled="isViewMode"
                    placeholder="请输入销售方地址电话"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="销售方开户行" prop="sellerBankAccount">
                  <el-input
                    v-model="formData.sellerBankAccount"
                    :disabled="isViewMode"
                    placeholder="请输入销售方开户行及账号"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="购买方名称" prop="buyerName">
                  <el-input
                    v-model="formData.buyerName"
                    :disabled="isViewMode"
                    placeholder="请输入购买方名称"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="购买方税号" prop="buyerTaxNumber">
                  <el-input
                    v-model="formData.buyerTaxNumber"
                    :disabled="isViewMode"
                    placeholder="请输入购买方纳税人识别号"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="购买方地址电话" prop="buyerAddressPhone">
                  <el-input
                    v-model="formData.buyerAddressPhone"
                    :disabled="isViewMode"
                    placeholder="请输入购买方地址电话"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="购买方开户行" prop="buyerBankAccount">
                  <el-input
                    v-model="formData.buyerBankAccount"
                    :disabled="isViewMode"
                    placeholder="请输入购买方开户行及账号"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <!-- 商品信息 -->
        <el-tab-pane label="商品信息" name="goods">
          <el-form
            ref="goodsForm"
            :model="formData"
            label-width="120px"
            size="small"
          >
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="货物名称" prop="goodsName">
                  <el-input
                    v-model="formData.goodsName"
                    :disabled="isViewMode"
                    placeholder="请输入货物或应税劳务名称"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="规格型号" prop="specification">
                  <el-input
                    v-model="formData.specification"
                    :disabled="isViewMode"
                    placeholder="请输入规格型号"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="单位" prop="unit">
                  <el-input
                    v-model="formData.unit"
                    :disabled="isViewMode"
                    placeholder="请输入单位"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="数量" prop="quantity">
                  <el-input-number
                    v-model="formData.quantity"
                    :disabled="isViewMode"
                    :precision="2"
                    style="width: 100%"
                    placeholder="请输入数量"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="单价" prop="unitPrice">
                  <el-input-number
                    v-model="formData.unitPrice"
                    :disabled="isViewMode"
                    :precision="2"
                    style="width: 100%"
                    placeholder="请输入单价"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="金额" prop="amount">
                  <el-input-number
                    v-model="formData.amount"
                    :disabled="isViewMode"
                    :precision="2"
                    style="width: 100%"
                    placeholder="请输入金额"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="税率" prop="taxRate">
                  <el-input-number
                    v-model="formData.taxRate"
                    :disabled="isViewMode"
                    :precision="2"
                    :max="1"
                    style="width: 100%"
                    placeholder="请输入税率"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="税额" prop="taxAmount">
                  <el-input-number
                    v-model="formData.taxAmount"
                    :disabled="isViewMode"
                    :precision="2"
                    style="width: 100%"
                    placeholder="请输入税额"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="价税合计" prop="totalAmount">
                  <el-input-number
                    v-model="formData.totalAmount"
                    :disabled="isViewMode"
                    :precision="2"
                    style="width: 100%"
                    placeholder="请输入价税合计"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="价税合计(大写)" prop="totalAmountChinese">
                  <el-input
                    v-model="formData.totalAmountChinese"
                    :disabled="isViewMode"
                    placeholder="请输入价税合计大写"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <!-- 状态信息 -->
        <el-tab-pane label="状态信息" name="status">
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="发票状态">
              <el-tag :type="getInvoiceStatusColor(formData.invoiceStatus)" size="small">
                {{ formatInvoiceStatus(formData.invoiceStatus).text }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="处理状态">
              <el-tag :type="formatProcessingStatus(formData.processingStatus).color" size="small">
                {{ formatProcessingStatus(formData.processingStatus).text }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="OCR状态">
              <el-tag :type="formatOcrStatus(formData.ocrStatus).color" size="small">
                {{ formatOcrStatus(formData.ocrStatus).text }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="验真状态">
              <el-tag :type="formatVerificationStatus(formData.verificationStatus).color" size="small">
                {{ formatVerificationStatus(formData.verificationStatus).text }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="归档状态">
              <el-tag :type="formatArchiveStatus(formData.archiveStatus).color" size="small">
                {{ formatArchiveStatus(formData.archiveStatus).text }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="风险等级">
              <el-tag :type="getRiskLevelColor(formData.riskLevel)" size="small">
                {{ formatRiskLevel(formData.riskLevel).text }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="OCR置信度">
              {{ formatConfidence(formData.ocrConfidence) }}
            </el-descriptions-item>
            <el-descriptions-item label="重试次数">
              {{ formData.retryCount || 0 }} / {{ formData.maxRetryCount || 3 }}
            </el-descriptions-item>
            <el-descriptions-item label="OCR识别时间">
              {{ formatDateTime(formData.ocrTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="验真时间">
              {{ formatDateTime(formData.verificationTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="归档时间">
              {{ formatDateTime(formData.archiveTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="风险评估时间">
              {{ formatDateTime(formData.riskAssessmentTime) }}
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 业务信息 -->
        <el-tab-pane label="业务信息" name="business">
          <el-form
            ref="businessForm"
            :model="formData"
            label-width="120px"
            size="small"
          >
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="业务分类" prop="businessCategory">
                  <el-select
                    v-model="formData.businessCategory"
                    :disabled="isViewMode"
                    placeholder="请选择业务分类"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="item in BUSINESS_CATEGORIES"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="会计科目" prop="accountingSubject">
                  <el-input
                    v-model="formData.accountingSubject"
                    :disabled="isViewMode"
                    placeholder="请输入会计科目"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="成本中心" prop="costCenter">
                  <el-input
                    v-model="formData.costCenter"
                    :disabled="isViewMode"
                    placeholder="请输入成本中心"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="项目编号" prop="projectCode">
                  <el-input
                    v-model="formData.projectCode"
                    :disabled="isViewMode"
                    placeholder="请输入项目编号"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="合同编号" prop="contractNumber">
                  <el-input
                    v-model="formData.contractNumber"
                    :disabled="isViewMode"
                    placeholder="请输入合同编号"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="优先级" prop="priority">
                  <el-select
                    v-model="formData.priority"
                    :disabled="isViewMode"
                    placeholder="请选择优先级"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="item in PRIORITIES"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="标签" prop="tags">
                  <el-input
                    v-model="formData.tags"
                    :disabled="isViewMode"
                    placeholder="请输入标签，多个标签用逗号分隔"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="备注" prop="remark">
                  <el-input
                    v-model="formData.remark"
                    :disabled="isViewMode"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入备注"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <!-- 文件信息 -->
        <el-tab-pane label="文件信息" name="file" v-if="!isCreateMode">
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="文件名称">
              {{ formData.fileName || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="文件大小">
              {{ formatFileSize(formData.fileSize) }}
            </el-descriptions-item>
            <el-descriptions-item label="文件类型">
              {{ formData.fileType || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="文件路径">
              {{ formData.filePath || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="归档路径">
              {{ formData.archivePath || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="验真来源">
              {{ formatVerificationSource(formData.verificationSource) }}
            </el-descriptions-item>
          </el-descriptions>
          
          <div v-if="formData.ocrResult" class="ocr-result-section">
            <h4>OCR识别结果</h4>
            <el-input
              :value="formData.ocrResult"
              type="textarea"
              :rows="6"
              readonly
            />
          </div>
          
          <div v-if="formData.verificationResult" class="verification-result-section">
            <h4>验真结果</h4>
            <el-input
              :value="formData.verificationResult"
              type="textarea"
              :rows="4"
              readonly
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="!isViewMode" type="primary" @click="handleSave" :loading="saving">
        {{ isCreateMode ? '创建' : '保存' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getInvoiceDetail,
  createInvoice,
  updateInvoice,
  INVOICE_TYPES,
  BUSINESS_CATEGORIES,
  PRIORITIES,
  utils
} from '@/api/managementAccountant/ts/invoiceManagement'

export default {
  name: 'InvoiceManagementDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    invoiceId: {
      type: [String, Number],
      default: null
    },
    mode: {
      type: String,
      default: 'view' // view, edit, create
    }
  },
  data() {
    return {
      loading: false,
      saving: false,
      activeTab: 'basic',
      formData: this.getDefaultFormData(),
      formRules: {
        invoiceCode: [
          { required: true, message: '请输入发票代码', trigger: 'blur' }
        ],
        invoiceNumber: [
          { required: true, message: '请输入发票号码', trigger: 'blur' }
        ],
        invoiceType: [
          { required: true, message: '请选择发票类型', trigger: 'change' }
        ],
        sellerName: [
          { required: true, message: '请输入销售方名称', trigger: 'blur' }
        ],
        buyerName: [
          { required: true, message: '请输入购买方名称', trigger: 'blur' }
        ]
      },
      INVOICE_TYPES,
      BUSINESS_CATEGORIES,
      PRIORITIES
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
    dialogTitle() {
      const titleMap = {
        view: '查看发票',
        edit: '编辑发票',
        create: '新建发票'
      }
      return titleMap[this.mode] || '发票详情'
    },
    isViewMode() {
      return this.mode === 'view'
    },
    isCreateMode() {
      return this.mode === 'create'
    },
    isEditMode() {
      return this.mode === 'edit'
    },
    tenantId() {
      return this.$store.getters.tenantId || 1
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initData()
      }
    },
    invoiceId(val) {
      if (val && this.visible) {
        this.loadData()
      }
    }
  },
  methods: {
    // 初始化数据
    initData() {
      this.activeTab = 'basic'
      if (this.isCreateMode) {
        this.formData = this.getDefaultFormData()
      } else if (this.invoiceId) {
        this.loadData()
      }
    },

    // 获取默认表单数据
    getDefaultFormData() {
      return {
        tenantId: this.tenantId,
        invoiceCode: '',
        invoiceNumber: '',
        invoiceType: '',
        invoiceStatus: 'DRAFT',
        invoiceDate: null,
        sellerName: '',
        sellerTaxNumber: '',
        sellerAddressPhone: '',
        sellerBankAccount: '',
        buyerName: '',
        buyerTaxNumber: '',
        buyerAddressPhone: '',
        buyerBankAccount: '',
        goodsName: '',
        specification: '',
        unit: '',
        quantity: null,
        unitPrice: null,
        amount: null,
        taxRate: null,
        taxAmount: null,
        totalAmount: null,
        totalAmountChinese: '',
        businessCategory: '',
        accountingSubject: '',
        costCenter: '',
        projectCode: '',
        contractNumber: '',
        priority: 'NORMAL',
        tags: '',
        remark: ''
      }
    },

    // 加载数据
    async loadData() {
      if (!this.invoiceId) return
      
      this.loading = true
      try {
        const response = await getInvoiceDetail(this.tenantId, this.invoiceId)
        if (response.success) {
          this.formData = { ...this.getDefaultFormData(), ...response.data }
        } else {
          this.$message.error('加载发票详情失败: ' + response.message)
        }
      } catch (error) {
        this.$message.error('加载发票详情失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 保存
    async handleSave() {
      try {
        // 验证表单
        await this.$refs.basicForm.validate()
        
        this.saving = true
        let response
        
        if (this.isCreateMode) {
          response = await createInvoice(this.formData)
        } else {
          response = await updateInvoice(this.formData)
        }
        
        if (response.success) {
          this.$message.success(this.isCreateMode ? '创建成功' : '保存成功')
          this.$emit('refresh')
          this.handleClose()
        } else {
          this.$message.error((this.isCreateMode ? '创建失败' : '保存失败') + ': ' + response.message)
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error((this.isCreateMode ? '创建失败' : '保存失败') + ': ' + error.message)
        }
      } finally {
        this.saving = false
      }
    },

    // 关闭
    handleClose() {
      this.dialogVisible = false
      this.formData = this.getDefaultFormData()
      this.activeTab = 'basic'
      
      // 清除表单验证
      this.$nextTick(() => {
        if (this.$refs.basicForm) {
          this.$refs.basicForm.clearValidate()
        }
      })
    },

    // 格式化方法
    formatInvoiceStatus: utils.formatInvoiceStatus,
    formatOcrStatus: utils.formatOcrStatus,
    formatVerificationStatus: utils.formatVerificationStatus,
    formatRiskLevel: utils.formatRiskLevel,
    formatProcessingStatus: utils.formatProcessingStatus,
    formatFileSize: utils.formatFileSize,
    formatConfidence: utils.formatConfidence,
    getInvoiceStatusColor: utils.getInvoiceStatusColor,
    getRiskLevelColor: utils.getRiskLevelColor,

    // 格式化归档状态
    formatArchiveStatus(status) {
      const statusMap = {
        'PENDING': { text: '待归档', color: 'warning' },
        'ARCHIVED': { text: '已归档', color: 'success' },
        'FAILED': { text: '归档失败', color: 'danger' }
      }
      return statusMap[status] || { text: status, color: 'info' }
    },

    // 格式化验真来源
    formatVerificationSource(source) {
      const sourceMap = {
        'TAX_BUREAU': '税务局',
        'THIRD_PARTY': '第三方'
      }
      return sourceMap[source] || source || '-'
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return this.$moment(dateTime).format('YYYY-MM-DD HH:mm:ss')
    }
  }
}
</script>

<style lang="scss" scoped>
.invoice-detail {
  .ocr-result-section,
  .verification-result-section {
    margin-top: 20px;
    
    h4 {
      margin-bottom: 10px;
      color: #303133;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
