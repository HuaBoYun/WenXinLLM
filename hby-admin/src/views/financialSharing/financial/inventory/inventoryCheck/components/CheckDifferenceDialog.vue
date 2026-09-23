<template>
  <el-dialog
    title="处理盘点差异"
    :visible.sync="visible"
    width="60%"
    :before-close="handleClose"
    class="check-difference-dialog"
  >
    <div class="dialog-content">
      <!-- 差异信息 -->
      <div class="info-section">
        <h3>差异信息</h3>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>存货编码：</label>
              <span>{{ differenceData.inventoryCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>存货名称：</label>
              <span>{{ differenceData.inventoryName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>规格型号：</label>
              <span>{{ differenceData.specification }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>账面数量：</label>
              <span>{{ differenceData.bookQuantity }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>实盘数量：</label>
              <span>{{ differenceData.actualQuantity }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>差异数量：</label>
              <span :class="getVarianceClass(differenceData.varianceQuantity)">
                {{ differenceData.varianceQuantity }}
              </span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>单位成本：</label>
              <span>{{ formatAmount(differenceData.unitCost) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>差异金额：</label>
              <span :class="['amount', getVarianceClass(differenceData.varianceAmount)]">
                {{ formatAmount(differenceData.varianceAmount) }}
              </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>差异类型：</label>
              <el-tag :type="getVarianceTypeTag(differenceData.varianceQuantity)">
                {{ getVarianceTypeText(differenceData.varianceQuantity) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 处理方案 -->
      <div class="process-section">
        <h3>处理方案</h3>
        <el-form :model="processForm" :rules="processRules" ref="processForm" label-width="120px">
          <el-form-item label="处理方式" prop="processMethod">
            <el-radio-group v-model="processForm.processMethod">
              <el-radio label="ADJUST_INVENTORY">调整库存账</el-radio>
              <el-radio label="GENERATE_VOUCHER">生成调整凭证</el-radio>
              <el-radio label="BOTH">同时执行</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="差异原因" prop="reason">
            <el-select v-model="processForm.reason" placeholder="请选择差异原因" style="width: 100%">
              <el-option label="盘点误差" value="COUNT_ERROR" />
              <el-option label="单据延迟" value="DOCUMENT_DELAY" />
              <el-option label="损耗" value="LOSS" />
              <el-option label="盗窃" value="THEFT" />
              <el-option label="其他" value="OTHER" />
            </el-select>
          </el-form-item>

          <el-form-item label="处理说明" prop="description">
            <el-input
              v-model="processForm.description"
              type="textarea"
              :rows="4"
              placeholder="请输入处理说明"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>

          <el-form-item v-if="processForm.processMethod !== 'ADJUST_INVENTORY'" label="会计科目">
            <el-select v-model="processForm.accountSubject" placeholder="请选择会计科目" style="width: 100%">
              <el-option label="待处理财产损溢" value="1901" />
              <el-option label="管理费用" value="6602" />
              <el-option label="营业外支出" value="6711" />
              <el-option label="营业外收入" value="6051" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">
        提交处理
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { processInventoryCheckDifference } from '@/api/financialSharing/inventory'

export default {
  name: 'CheckDifferenceDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    differenceData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      submitting: false,
      processForm: {
        processMethod: 'BOTH',
        reason: '',
        description: '',
        accountSubject: ''
      },
      processRules: {
        processMethod: [
          { required: true, message: '请选择处理方式', trigger: 'change' }
        ],
        reason: [
          { required: true, message: '请选择差异原因', trigger: 'change' }
        ],
        description: [
          { required: true, message: '请输入处理说明', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    async handleSubmit() {
      this.$refs.processForm.validate(async (valid) => {
        if (!valid) return

        this.submitting = true
        try {
          const response = await processInventoryCheckDifference({
            resultId: this.differenceData.resultId,
            checkId: this.differenceData.checkId,
            inventoryId: this.differenceData.inventoryId,
            processMethod: this.processForm.processMethod,
            reason: this.processForm.reason,
            description: this.processForm.description,
            accountSubject: this.processForm.accountSubject
          })

          if (response.code === 1) {
            this.$message.success('差异处理成功')
            this.$emit('success')
            this.handleClose()
          } else {
            this.$message.error(response.msg || '处理失败')
          }
        } catch (error) {
          this.$message.error('处理失败：' + error.message)
        } finally {
          this.submitting = false
        }
      })
    },

    handleClose() {
      this.$refs.processForm && this.$refs.processForm.resetFields()
      this.$emit('update:visible', false)
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    },

    getVarianceClass(variance) {
      if (variance > 0) return 'surplus'
      if (variance < 0) return 'shortage'
      return ''
    },

    getVarianceTypeTag(variance) {
      if (variance > 0) return 'success'
      if (variance < 0) return 'danger'
      return 'info'
    },

    getVarianceTypeText(variance) {
      if (variance > 0) return '盘盈'
      if (variance < 0) return '盘亏'
      return '正常'
    }
  }
}
</script>

<style lang="scss" scoped>
.check-difference-dialog {
  .dialog-content {
    .info-section,
    .process-section {
      margin-bottom: 30px;

      h3 {
        margin: 0 0 15px 0;
        color: #303133;
        font-size: 16px;
        border-bottom: 1px solid #e4e7ed;
        padding-bottom: 8px;
      }
    }

    .info-item {
      margin-bottom: 10px;

      label {
        font-weight: 600;
        color: #606266;
        margin-right: 8px;
      }
    }
  }

  .dialog-footer {
    text-align: right;
  }
}

.amount {
  font-weight: 600;
}

.surplus {
  color: #67c23a;
  font-weight: 600;
}

.shortage {
  color: #f56c6c;
  font-weight: 600;
}
</style>

