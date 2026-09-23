<template>
  <el-dialog
    title="股权变动审核"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <div v-loading="loading">
      <!-- 变动信息 -->
      <el-card shadow="never" class="mb-20">
        <div slot="header">
          <span>变动信息</span>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="企业名称">{{ changeData.investeeEnterpriseName }}</el-descriptions-item>
          <el-descriptions-item label="变动类型">
            <el-tag :type="getChangeTypeTag(changeData.changeType)" size="mini">
              {{ getChangeTypeText(changeData.changeType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="转让方">{{ changeData.transferorEnterpriseName }}</el-descriptions-item>
          <el-descriptions-item label="受让方">{{ changeData.transfereeEnterpriseName }}</el-descriptions-item>
          <el-descriptions-item label="变动前比例">{{ changeData.beforeShareholdingRatio }}%</el-descriptions-item>
          <el-descriptions-item label="变动后比例">{{ changeData.afterShareholdingRatio }}%</el-descriptions-item>
          <el-descriptions-item label="变动幅度">
            <span :class="(Number(changeData.afterShareholdingRatio) - Number(changeData.beforeShareholdingRatio)) >= 0 ? 'text-success' : 'text-danger'">
              {{ (Number(changeData.afterShareholdingRatio) - Number(changeData.beforeShareholdingRatio)) > 0 ? '+' : '' }}{{ (Number(changeData.afterShareholdingRatio) - Number(changeData.beforeShareholdingRatio)).toFixed(2) }}%
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="变动日期">{{ changeData.changeDate }}</el-descriptions-item>
          <el-descriptions-item label="交易金额">{{ changeData.changeAmount }}万元</el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="getRiskTag(changeData.warningLevel)" size="mini">
              {{ {LOW:'低风险',MEDIUM:'中风险',HIGH:'高风险'}[changeData.warningLevel] || changeData.warningLevel }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <div style="margin-top: 15px;">
          <div class="info-label">变动原因：</div>
          <div class="info-content">{{ changeData.changeReason || '无' }}</div>
        </div>
        
        <div style="margin-top: 10px;">
          <div class="info-label">风险说明：</div>
          <div class="info-content">{{ changeData.warningReason || '无' }}</div>
        </div>
      </el-card>

      <!-- 审核表单 -->
      <el-form
        ref="approvalForm"
        :model="approvalForm"
        :rules="approvalRules"
        label-width="100px"
      >
        <el-form-item label="审核结果" prop="approvalResult">
          <el-radio-group v-model="approvalForm.approvalResult">
            <el-radio label="APPROVED">通过</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="审核意见" prop="approvalComments">
          <el-input
            v-model="approvalForm.approvalComments"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="风险评级" prop="riskRating" v-if="approvalForm.approvalResult === 'APPROVED'">
          <el-select v-model="approvalForm.riskRating" placeholder="请选择风险评级" style="width: 200px;">
            <el-option label="低风险" value="LOW"></el-option>
            <el-option label="中风险" value="MEDIUM"></el-option>
            <el-option label="高风险" value="HIGH"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="监管措施" prop="supervisionMeasures" v-if="approvalForm.approvalResult === 'APPROVED'">
          <el-checkbox-group v-model="approvalForm.supervisionMeasures">
            <el-checkbox label="REGULAR_REPORT">定期报告</el-checkbox>
            <el-checkbox label="SPECIAL_AUDIT">专项审计</el-checkbox>
            <el-checkbox label="RISK_MONITORING">风险监控</el-checkbox>
            <el-checkbox label="COMPLIANCE_CHECK">合规检查</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="后续要求" prop="followUpRequirements">
          <el-input
            v-model="approvalForm.followUpRequirements"
            type="textarea"
            :rows="3"
            placeholder="请输入后续监管要求"
            maxlength="300"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="审核期限" prop="approvalDeadline">
          <el-date-picker
            v-model="approvalForm.approvalDeadline"
            type="date"
            placeholder="选择审核期限"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 200px;"
          />
        </el-form-item>
      </el-form>

      <!-- 审核历史 -->
      <el-card shadow="never" v-if="approvalHistory.length > 0">
        <div slot="header">
          <span>审核历史</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(history, index) in approvalHistory"
            :key="index"
            :timestamp="history.approvalTime"
            placement="top"
          >
            <el-card>
              <div class="history-item">
                <div class="history-header">
                  <span class="history-approver">{{ history.approverName }}</span>
                  <el-tag :type="history.approvalResult === 'APPROVED' ? 'success' : 'danger'" size="mini">
                    {{ history.approvalResult === 'APPROVED' ? '通过' : '拒绝' }}
                  </el-tag>
                </div>
                <div class="history-content">{{ history.approvalComments }}</div>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        type="primary"
        @click="handleSubmitApproval"
        :loading="submitLoading"
      >
        提交审核
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  approveEquityChange,
  rejectEquityChange,
  getEquityChangeApprovalHistory
} from '@/api/stateAssets/equityChanges'

export default {
  name: 'EquityApprovalDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    changeData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      approvalHistory: [],
      approvalForm: {
        changeId: '',
        approvalResult: 'APPROVED',
        approvalComments: '',
        riskRating: 'LOW',
        supervisionMeasures: [],
        followUpRequirements: '',
        approvalDeadline: ''
      },
      approvalRules: {
        approvalResult: [
          { required: true, message: '请选择审核结果', trigger: 'change' }
        ],
        approvalComments: [
          { required: true, message: '请输入审核意见', trigger: 'blur' },
          { min: 10, message: '审核意见至少10个字符', trigger: 'blur' }
        ],
        riskRating: [
          { required: true, message: '请选择风险评级', trigger: 'change' }
        ]
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
      if (val && this.changeData.changeId) {
        this.initForm()
        this.loadApprovalHistory()
      }
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      this.approvalForm = {
        changeId: this.changeData.changeId,
        approvalResult: 'APPROVED',
        approvalComments: '',
        riskRating: 'LOW',
        supervisionMeasures: [],
        followUpRequirements: '',
        approvalDeadline: ''
      }
      
      this.$nextTick(() => {
        this.$refs.approvalForm && this.$refs.approvalForm.clearValidate()
      })
    },

    // 加载审核历史
    async loadApprovalHistory() {
      this.loading = true
      try {
        const response = await getEquityChangeApprovalHistory({
          changeId: this.changeData.changeId
        })
        this.approvalHistory = response.data || []
      } catch (error) {
        console.error('加载审核历史失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 提交审核
    handleSubmitApproval() {
      this.$refs.approvalForm.validate(async (valid) => {
        if (!valid) return

        this.submitLoading = true
        try {
          if (this.approvalForm.approvalResult === 'APPROVED') {
            await approveEquityChange(this.approvalForm)
            this.$message.success('审核通过')
          } else {
            await rejectEquityChange(this.approvalForm)
            this.$message.success('审核拒绝')
          }
          
          this.handleClose()
          this.$emit('refresh')
        } catch (error) {
          this.$message.error('审核提交失败')
        } finally {
          this.submitLoading = false
        }
      })
    },

    // 获取变动类型标签
    getChangeTypeTag(type) {
      const tagMap = {
        'TRANSFER': 'primary',
        'INCREASE': 'success',
        'DECREASE': 'warning',
        'PLEDGE': 'info',
        'UNPLEDGE': 'success'
      }
      return tagMap[type] || 'info'
    },

    // 获取变动类型文本
    getChangeTypeText(type) {
      const textMap = {
        'TRANSFER': '股权转让',
        'INCREASE': '增资扩股',
        'DECREASE': '减资',
        'PLEDGE': '股权质押',
        'UNPLEDGE': '股权解押'
      }
      return textMap[type] || type
    },

    // 获取风险标签
    getRiskTag(level) {
      const tagMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return tagMap[level] || 'info'
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.initForm()
    }
  }
}
</script>

<style scoped>
.mb-20 {
  margin-bottom: 20px;
}

.info-label {
  font-weight: bold;
  color: #606266;
  margin-bottom: 5px;
}

.info-content {
  color: #303133;
  line-height: 1.5;
}

.text-success {
  color: #67c23a;
}

.text-danger {
  color: #f56c6c;
}

.history-item {
  .history-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    
    .history-approver {
      font-weight: bold;
      color: #303133;
    }
  }
  
  .history-content {
    color: #606266;
    line-height: 1.5;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
