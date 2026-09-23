<template>
  <div class="budget-approval-detail">
    <!-- 头部信息 -->
    <el-card class="header-card">
      <div slot="header" class="header-title">
        <span>{{ budgetDetail.budgetName || '预算详情' }}</span>
        <el-button-group class="header-actions">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-check"
            @click="handleApprove"
            :disabled="budgetDetail.status !== 'pending'"
          >
            通过
          </el-button>
          <el-button
            type="danger"
            size="small"
            icon="el-icon-close"
            @click="handleReject"
            :disabled="budgetDetail.status !== 'pending'"
          >
            拒绝
          </el-button>
          <el-button
            type="warning"
            size="small"
            icon="el-icon-back"
            @click="handleReturn"
            :disabled="budgetDetail.status !== 'pending'"
          >
            退回修改
          </el-button>
          <el-button
            size="small"
            icon="el-icon-printer"
            @click="handlePrint"
          >
            打印
          </el-button>
          <el-button
            size="small"
            icon="el-icon-back"
            @click="handleGoBack"
          >
            返回
          </el-button>
        </el-button-group>
      </div>

      <!-- 基本信息卡片 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>预算编号：</label>
            <span>{{ budgetDetail.budgetCode }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>预算类型：</label>
            <el-tag :type="getBudgetTypeTag(budgetDetail.budgetType)">
              {{ budgetDetail.budgetType }}
            </el-tag>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>预算期间：</label>
            <span>{{ budgetDetail.period }}</span>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 16px;">
        <el-col :span="8">
          <div class="info-item">
            <label>预算金额：</label>
            <span class="amount-text">{{ formatCurrency(budgetDetail.budgetAmount) }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>申请人：</label>
            <span>{{ budgetDetail.applicant }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>申请部门：</label>
            <span>{{ budgetDetail.department }}</span>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 16px;">
        <el-col :span="8">
          <div class="info-item">
            <label>申请时间：</label>
            <span>{{ budgetDetail.applyTime | formatDate }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>紧急程度：</label>
            <el-tag :type="getUrgencyTag(budgetDetail.urgency)">
              {{ getUrgencyText(budgetDetail.urgency) }}
            </el-tag>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>当前状态：</label>
            <el-tag :type="getStatusTag(budgetDetail.status)">
              {{ getStatusText(budgetDetail.status) }}
            </el-tag>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 审批历史 -->
    <el-card class="history-card">
      <div slot="header">
        <span>审批历史</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          icon="el-icon-s-operation"
          @click="handleViewProcess"
        >
          查看流程图
        </el-button>
      </div>

      <el-timeline>
        <el-timeline-item
          v-for="(history, index) in approvalHistory"
          :key="index"
          :timestamp="history.approvalTime"
          :type="getTimelineType(history.result)"
        >
          <el-card class="history-item">
            <div class="history-header">
              <span class="node-name">{{ history.nodeName }}</span>
              <el-tag
                :type="getApprovalResultTag(history.result)"
                size="small"
              >
                {{ getApprovalResultText(history.result) }}
              </el-tag>
            </div>
            <div class="history-content">
              <div class="approver-info">
                <span class="approver">审批人：{{ history.approver }}</span>
                <span class="department">({{ history.department }})</span>
              </div>
              <div class="approval-comment" v-if="history.comment">
                <strong>审批意见：</strong>{{ history.comment }}
              </div>
              <div class="attachments" v-if="history.attachments && history.attachments.length > 0">
                <strong>附件：</strong>
                <el-link
                  v-for="(file, fileIndex) in history.attachments"
                  :key="fileIndex"
                  :href="file.url"
                  target="_blank"
                  type="primary"
                  class="attachment-link"
                >
                  {{ file.name }}
                </el-link>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <!-- 预算明细 -->
    <el-card class="detail-card">
      <div slot="header">预算明细</div>

      <el-table
        :data="budgetDetail.details || []"
        border
        stripe
        show-summary
        :summary-method="getSummaries"
      >
        <el-table-column type="index" width="60" label="序号" align="center" />
        <el-table-column prop="item" label="预算项目" min-width="150" show-overflow-tooltip />
        <el-table-column prop="category" label="费用类别" width="120" align="center" />
        <el-table-column prop="unit" label="计量单位" width="100" align="center" />
        <el-table-column prop="quantity" label="数量" width="100" align="right" />
        <el-table-column prop="unitPrice" label="单价" width="120" align="right">
          <template slot-scope="scope">
            {{ formatCurrency(scope.row.unitPrice) }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatCurrency(scope.row.amount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
      </el-table>
    </el-card>

    <!-- 相关附件 -->
    <el-card class="attachment-card" v-if="budgetDetail.attachments && budgetDetail.attachments.length > 0">
      <div slot="header">相关附件</div>

      <el-table :data="budgetDetail.attachments" border>
        <el-table-column type="index" width="60" label="序号" align="center" />
        <el-table-column prop="name" label="文件名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="size" label="文件大小" width="100" align="center" />
        <el-table-column prop="uploadTime" label="上传时间" width="150" align="center">
          <template slot-scope="scope">
            {{ scope.row.uploadTime | formatDate }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-download"
              @click="handleDownloadFile(scope.row)"
            >
              下载
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 审批对话框 -->
    <el-dialog
      :title="approvalDialogTitle"
      :visible.sync="approvalDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleApprovalDialogClose"
    >
      <el-form
        ref="approvalForm"
        :model="approvalForm"
        :rules="approvalRules"
        label-width="100px"
        size="small"
      >
        <el-form-item label="审批结果" prop="result">
          <el-radio-group v-model="approvalForm.result">
            <el-radio label="approved">通过</el-radio>
            <el-radio label="rejected">拒绝</el-radio>
            <el-radio label="returned">退回修改</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="审批意见" prop="comment">
          <el-input
            type="textarea"
            v-model="approvalForm.comment"
            :rows="4"
            placeholder="请输入审批意见"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="附件上传">
          <el-upload
            ref="upload"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :file-list="approvalForm.attachments"
            :on-success="handleUploadSuccess"
            :on-remove="handleUploadRemove"
            :before-upload="beforeUpload"
            multiple
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              支持任意文件格式，单个文件不超过10MB
            </div>
          </el-upload>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="approvalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitApproval" :loading="submitting">
          确认提交
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetApi } from '@/api/financialSharing/budget'
import { formatCurrency, formatDate } from '@/utils/index'

export default {
  name: 'BudgetApprovalDetail',
  data() {
    return {
      // 预算详情
      budgetDetail: {},
      loading: false,

      // 审批历史
      approvalHistory: [],

      // 审批对话框
      approvalDialogVisible: false,
      approvalDialogTitle: '预算审批',
      approvalForm: {
        budgetId: '',
        result: 'approved',
        comment: '',
        attachments: []
      },
      approvalRules: {
        result: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ],
        comment: [
          { required: true, message: '请输入审批意见', trigger: 'blur' },
          { min: 5, max: 500, message: '审批意见长度在 5 到 500 个字符', trigger: 'blur' }
        ]
      },

      // 上传配置
      uploadUrl: process.env.VUE_APP_BASE_API + '/file/upload',
      uploadHeaders: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      },

      // 提交状态
      submitting: false
    }
  },

  created() {
    this.budgetId = this.$route.query.id
    if (this.budgetId) {
      this.fetchBudgetDetail()
      this.fetchApprovalHistory()
    } else {
      this.$message.error('缺少预算ID参数')
      this.handleGoBack()
    }
  },

  filters: {
    formatDate(time) {
      return formatDate(time, 'yyyy-MM-dd HH:mm')
    }
  },

  methods: {
    // 格式化货币
    formatCurrency,

    // 获取预算详情
    async fetchBudgetDetail() {
      this.loading = true
      try {
        const response = await budgetApi.getBudgetDetail(this.budgetId)
        if (response.code === 1) {
          this.budgetDetail = response.data || {}
        } else {
          this.$message.error(response.message || '获取预算详情失败')
        }
      } catch (error) {
        console.error('获取预算详情异常:', error)
        this.$message.error('获取预算详情失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    // 获取审批历史
    async fetchApprovalHistory() {
      try {
        const response = await budgetApi.getApprovalHistory(this.budgetId)
        if (response.code === 1) {
          this.approvalHistory = response.data || []
        } else {
          this.$message.error(response.message || '获取审批历史失败')
        }
      } catch (error) {
        console.error('获取审批历史异常:', error)
        this.$message.error('获取审批历史失败，请稍后重试')
      }
    },

    // 通过
    handleApprove() {
      this.approvalDialogTitle = '预算审批 - 通过'
      this.approvalForm = {
        budgetId: this.budgetId,
        result: 'approved',
        comment: '',
        attachments: []
      }
      this.approvalDialogVisible = true
    },

    // 拒绝
    handleReject() {
      this.approvalDialogTitle = '预算审批 - 拒绝'
      this.approvalForm = {
        budgetId: this.budgetId,
        result: 'rejected',
        comment: '',
        attachments: []
      }
      this.approvalDialogVisible = true
    },

    // 退回修改
    handleReturn() {
      this.approvalDialogTitle = '预算审批 - 退回修改'
      this.approvalForm = {
        budgetId: this.budgetId,
        result: 'returned',
        comment: '',
        attachments: []
      }
      this.approvalDialogVisible = true
    },

    // 打印
    handlePrint() {
      window.print()
    },

    // 返回
    handleGoBack() {
      this.$router.go(-1)
    },

    // 查看流程图
    handleViewProcess() {
      this.$router.push({
        path: '/financialSharing/budget/approve/process',
        query: { id: this.budgetId }
      })
    },

    // 提交审批
    async handleSubmitApproval() {
      try {
        await this.$refs.approvalForm.validate()

        this.submitting = true
        const response = await budgetApi.approveBudget(this.approvalForm)

        if (response.code === 1) {
          this.$message.success('审批提交成功')
          this.approvalDialogVisible = false
          // 刷新页面数据
          this.fetchBudgetDetail()
          this.fetchApprovalHistory()
        } else {
          this.$message.error(response.message || '审批提交失败')
        }
      } catch (error) {
        if (error !== false) {
          console.error('审批提交异常:', error)
          this.$message.error('审批提交失败，请稍后重试')
        }
      } finally {
        this.submitting = false
      }
    },

    // 审批对话框关闭
    handleApprovalDialogClose() {
      this.$refs.approvalForm && this.$refs.approvalForm.resetFields()
      this.approvalForm.attachments = []
    },

    // 上传前校验
    beforeUpload(file) {
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
      }
      return isLt10M
    },

    // 上传成功
    handleUploadSuccess(response, file, fileList) {
      if (response.code === 1) {
        this.$message.success('文件上传成功')
      } else {
        this.$message.error(response.message || '文件上传失败')
      }
    },

    // 移除文件
    handleUploadRemove(file, fileList) {
      this.approvalForm.attachments = fileList
    },

    // 下载文件
    handleDownloadFile(file) {
      const link = document.createElement('a')
      link.href = file.url
      link.download = file.name
      link.click()
    },

    // 合计行
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (column.property === 'amount') {
          const values = data.map(item => Number(item.amount))
          if (!values.every(value => isNaN(value))) {
            sums[index] = formatCurrency(values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0))
          } else {
            sums[index] = '-'
          }
        } else {
          sums[index] = '-'
        }
      })
      return sums
    },

    // 获取预算类型标签
    getBudgetTypeTag(type) {
      const tagMap = {
        '年度预算': '',
        '季度预算': 'success',
        '月度预算': 'warning',
        '项目预算': 'info',
        '部门预算': 'danger'
      }
      return tagMap[type] || ''
    },

    // 获取紧急程度标签
    getUrgencyTag(urgency) {
      const tagMap = {
        'normal': 'info',
        'important': 'warning',
        'urgent': 'danger'
      }
      return tagMap[urgency] || 'info'
    },

    // 获取紧急程度文本
    getUrgencyText(urgency) {
      const textMap = {
        'normal': '普通',
        'important': '重要',
        'urgent': '紧急'
      }
      return textMap[urgency] || '普通'
    },

    // 获取状态标签
    getStatusTag(status) {
      const tagMap = {
        'pending': 'warning',
        'approved': 'success',
        'rejected': 'danger',
        'returned': 'info'
      }
      return tagMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'pending': '待审批',
        'approved': '已通过',
        'rejected': '已拒绝',
        'returned': '已退回'
      }
      return textMap[status] || '未知'
    },

    // 获取时间线类型
    getTimelineType(result) {
      const typeMap = {
        'approved': 'success',
        'rejected': 'danger',
        'returned': 'warning',
        'pending': 'primary'
      }
      return typeMap[result] || 'primary'
    },

    // 获取审批结果标签
    getApprovalResultTag(result) {
      const tagMap = {
        'approved': 'success',
        'rejected': 'danger',
        'returned': 'warning',
        'pending': 'info'
      }
      return tagMap[result] || 'info'
    },

    // 获取审批结果文本
    getApprovalResultText(result) {
      const textMap = {
        'approved': '通过',
        'rejected': '拒绝',
        'returned': '退回',
        'pending': '待审批'
      }
      return textMap[result] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-approval-detail {
  padding: 20px;

  .header-card {
    margin-bottom: 16px;

    .header-title {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-actions {
        display: flex;
        gap: 8px;
      }
    }

    .info-item {
      display: flex;
      align-items: center;
      margin-bottom: 8px;

      label {
        font-weight: 500;
        color: #606266;
        min-width: 80px;
      }

      .amount-text {
        font-weight: 600;
        color: #E6A23C;
        font-size: 16px;
      }
    }
  }

  .history-card {
    margin-bottom: 16px;

    .history-item {
      margin-bottom: 0;

      .history-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;

        .node-name {
          font-weight: 600;
          color: #303133;
        }
      }

      .history-content {
        .approver-info {
          margin-bottom: 8px;
          color: #606266;

          .approver {
            font-weight: 500;
          }

          .department {
            margin-left: 8px;
            color: #909399;
          }
        }

        .approval-comment {
          margin-bottom: 8px;
          line-height: 1.6;

          strong {
            color: #303133;
          }
        }

        .attachments {
          .attachment-link {
            margin-right: 12px;
          }
        }
      }
    }
  }

  .detail-card {
    margin-bottom: 16px;
  }

  .attachment-card {
    margin-bottom: 16px;
  }
}

@media print {
  .header-actions {
    display: none !important;
  }
}
</style>