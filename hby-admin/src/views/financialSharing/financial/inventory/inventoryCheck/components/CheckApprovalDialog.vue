<template>
  <el-dialog
    title="盘点审批"
    :visible.sync="visible"
    width="70%"
    :before-close="handleClose"
    class="check-approval-dialog"
  >
    <div class="dialog-content">
      <!-- 盘点基本信息 -->
      <div class="info-section">
        <h3>盘点信息</h3>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>盘点编号：</label>
              <span>{{ checkData.checkNumber }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>盘点名称：</label>
              <span>{{ checkData.checkName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>盘点类型：</label>
              <span>{{ checkData.checkTypeName }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>盘点日期：</label>
              <span>{{ checkData.checkDate }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>盘点仓库：</label>
              <span>{{ checkData.warehouseName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>操作人：</label>
              <span>{{ checkData.operatorName }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 盘点汇总 -->
      <div class="summary-section">
        <h3>盘点汇总</h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="summary-card">
              <div class="card-title">总存货数</div>
              <div class="card-value">{{ summary.totalCount }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="summary-card">
              <div class="card-title">盘盈数量</div>
              <div class="card-value surplus">{{ summary.surplusCount }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="summary-card">
              <div class="card-title">盘亏数量</div>
              <div class="card-value shortage">{{ summary.shortageCount }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="summary-card">
              <div class="card-title">净差异金额</div>
              <div :class="['card-value', getVarianceClass(summary.netVarianceAmount)]">
                {{ formatAmount(summary.netVarianceAmount) }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 重大差异明细 -->
      <div class="variance-section">
        <h3>重大差异明细（差异金额 > ¥1,000）</h3>
        <el-table :data="varianceData" border>
          <el-table-column prop="inventoryCode" label="存货编码" width="120" />
          <el-table-column prop="inventoryName" label="存货名称" min-width="150" />
          <el-table-column prop="bookQuantity" label="账面数量" width="100" align="right" />
          <el-table-column prop="actualQuantity" label="实盘数量" width="100" align="right" />
          <el-table-column prop="varianceQuantity" label="差异数量" width="100" align="right">
            <template slot-scope="scope">
              <span :class="getVarianceClass(scope.row.varianceQuantity)">
                {{ scope.row.varianceQuantity }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="varianceAmount" label="差异金额" width="120" align="right">
            <template slot-scope="scope">
              <span :class="['amount', getVarianceClass(scope.row.varianceAmount)]">
                {{ formatAmount(scope.row.varianceAmount) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        </el-table>
      </div>

      <!-- 审批意见 -->
      <div class="approval-section">
        <h3>审批意见</h3>
        <el-form :model="approvalForm" :rules="approvalRules" ref="approvalForm" label-width="100px">
          <el-form-item label="审批结果" prop="approvalResult">
            <el-radio-group v-model="approvalForm.approvalResult">
              <el-radio label="APPROVED">通过</el-radio>
              <el-radio label="REJECTED">驳回</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="审批意见" prop="approvalComment">
            <el-input
              v-model="approvalForm.approvalComment"
              type="textarea"
              :rows="4"
              placeholder="请输入审批意见"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
          <el-form-item v-if="approvalForm.approvalResult === 'APPROVED'" label="处理方式" prop="processMethod">
            <el-checkbox-group v-model="approvalForm.processMethod">
              <el-checkbox label="GENERATE_VOUCHER">生成调整凭证</el-checkbox>
              <el-checkbox label="UPDATE_INVENTORY">更新库存账</el-checkbox>
              <el-checkbox label="SEND_NOTIFICATION">发送通知</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
      </div>

      <!-- 审批历史 -->
      <div class="history-section">
        <h3>审批历史</h3>
        <el-timeline>
          <el-timeline-item
            v-for="(history, index) in approvalHistory"
            :key="index"
            :timestamp="history.approvalTime"
            :type="getHistoryType(history.approvalResult)"
          >
            <div class="history-content">
              <div class="history-title">
                {{ history.approverName }} - {{ getApprovalResultText(history.approvalResult) }}
              </div>
              <div class="history-comment">{{ history.approvalComment }}</div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmitApproval" :loading="submitting">
        提交审批
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getInventoryCheckById, getInventoryCheckResults, approveInventoryCheck } from '@/api/financialSharing/inventory'

export default {
  name: 'CheckApprovalDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    checkData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      submitting: false,
      summary: {},
      varianceData: [],
      approvalForm: {
        approvalResult: 'APPROVED',
        approvalComment: '',
        processMethod: ['GENERATE_VOUCHER', 'UPDATE_INVENTORY']
      },
      approvalRules: {
        approvalResult: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ],
        approvalComment: [
          { required: true, message: '请输入审批意见', trigger: 'blur' }
        ]
      },
      approvalHistory: []
    }
  },
  watch: {
    visible(val) {
      if (val && this.checkData.checkId) {
        this.loadApprovalData()
      }
    }
  },
  methods: {
    async loadApprovalData() {
      try {
        // 加载盘点详情
        const detailResponse = await getInventoryCheckById(this.checkData.checkId)
        if (detailResponse.code === 1 && detailResponse.data) {
          const data = detailResponse.data
          this.summary = {
            totalCount: data.inventoryCount || 0,
            surplusCount: data.surplusCount || 0,
            shortageCount: data.shortageCount || 0,
            netVarianceAmount: data.varianceAmount || 0
          }
        }

        // 加载重大差异明细（差异金额 > 1000）
        const resultResponse = await getInventoryCheckResults(this.checkData.checkId, {
          pageNumber: 1,
          pageSize: 100,
          minVarianceAmount: 1000
        })
        if (resultResponse.code === 1) {
          this.varianceData = resultResponse.data.tlist || []
        }

        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.approvalHistory = []
      } catch (error) {
        this.$message.error('加载审批数据失败：' + error.message)
      }
    },

    async handleSubmitApproval() {
      this.$refs.approvalForm.validate(async (valid) => {
        if (!valid) return

        this.submitting = true
        try {
          const response = await approveInventoryCheck(this.checkData.checkId, {
            approvalResult: this.approvalForm.approvalResult,
            approvalComment: this.approvalForm.approvalComment,
            processMethod: this.approvalForm.processMethod
          })

          if (response.code === 1) {
            this.$message.success('审批提交成功')
            this.$emit('approve', {
              checkId: this.checkData.checkId,
              approvalResult: this.approvalForm.approvalResult,
              approvalComment: this.approvalForm.approvalComment,
              processMethod: this.approvalForm.processMethod
            })
            this.handleClose()
          } else {
            this.$message.error(response.msg || '审批提交失败')
          }
        } catch (error) {
          this.$message.error('审批提交失败：' + error.message)
        } finally {
          this.submitting = false
        }
      })
    },

    handleClose() {
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

    getHistoryType(result) {
      const typeMap = {
        'SUBMITTED': 'primary',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return typeMap[result] || 'primary'
    },

    getApprovalResultText(result) {
      const textMap = {
        'SUBMITTED': '提交审批',
        'APPROVED': '审批通过',
        'REJECTED': '审批驳回'
      }
      return textMap[result] || result
    }
  }
}
</script>

<style lang="scss" scoped>
.check-approval-dialog {
  .dialog-content {
    max-height: 600px;
    overflow-y: auto;

    .info-section,
    .summary-section,
    .variance-section,
    .approval-section,
    .history-section {
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

    .summary-section {
      .summary-card {
        background: white;
        border: 1px solid #e4e7ed;
        border-radius: 4px;
        padding: 20px;
        text-align: center;

        .card-title {
          font-size: 14px;
          color: #909399;
          margin-bottom: 10px;
        }

        .card-value {
          font-size: 20px;
          font-weight: 600;
          color: #303133;

          &.surplus {
            color: #67c23a;
          }

          &.shortage {
            color: #f56c6c;
          }
        }
      }
    }

    .history-content {
      .history-title {
        font-weight: 600;
        color: #303133;
        margin-bottom: 5px;
      }

      .history-comment {
        color: #606266;
      }
    }
  }

  .dialog-footer {
    text-align: right;
  }
}

.amount {
  color: #f56c6c;
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
