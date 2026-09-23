<template>
  <el-dialog
    title="批量操作"
    :visible.sync="dialogVisible"
    width="60%"
    :before-close="handleClose"
  >
    <div>
      <!-- 操作类型选择 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>选择操作类型</span>
        </div>
        <el-radio-group v-model="operationType" @change="handleOperationTypeChange">
          <el-radio label="updateStatus">批量更新状态</el-radio>
          <el-radio label="delete">批量删除</el-radio>
          <el-radio label="export">批量导出</el-radio>
          <el-radio label="audit">批量审核</el-radio>
        </el-radio-group>
      </el-card>

      <!-- 选中的项目 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>已选中项目 ({{ selectedItems.length }})</span>
        </div>
        <el-table :data="selectedItems" border style="width: 100%" max-height="200">
          <el-table-column prop="processName" label="流程名称" />
          <el-table-column prop="statementType" label="报表类型" width="120" />
          <el-table-column prop="processStatus" label="当前状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.processStatus)">
                {{ scope.row.processStatus }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 操作参数 -->
      <el-card class="box-card" v-if="operationType">
        <div slot="header" class="clearfix">
          <span>操作参数</span>
        </div>

        <!-- 批量更新状态 -->
        <div v-if="operationType === 'updateStatus'">
          <el-form :model="statusForm" label-width="120px">
            <el-form-item label="目标状态">
              <el-select v-model="statusForm.targetStatus" placeholder="请选择目标状态">
                <el-option label="待开始" value="待开始" />
                <el-option label="进行中" value="进行中" />
                <el-option label="已完成" value="已完成" />
                <el-option label="已暂停" value="已暂停" />
                <el-option label="已取消" value="已取消" />
              </el-select>
            </el-form-item>
            <el-form-item label="操作原因">
              <el-input
                v-model="statusForm.reason"
                type="textarea"
                :rows="3"
                placeholder="请输入状态变更原因"
              />
            </el-form-item>
          </el-form>
        </div>

        <!-- 批量删除 -->
        <div v-if="operationType === 'delete'">
          <el-alert
            title="警告"
            type="warning"
            description="删除操作不可恢复，请确认是否继续？"
            show-icon
            :closable="false"
          />
          <el-form :model="deleteForm" label-width="120px" style="margin-top: 20px;">
            <el-form-item label="删除原因">
              <el-input
                v-model="deleteForm.reason"
                type="textarea"
                :rows="3"
                placeholder="请输入删除原因"
              />
            </el-form-item>
          </el-form>
        </div>

        <!-- 批量导出 -->
        <div v-if="operationType === 'export'">
          <el-form :model="exportForm" label-width="120px">
            <el-form-item label="导出格式">
              <el-radio-group v-model="exportForm.format">
                <el-radio label="excel">Excel格式</el-radio>
                <el-radio label="pdf">PDF格式</el-radio>
                <el-radio label="csv">CSV格式</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="导出内容">
              <el-checkbox-group v-model="exportForm.fields">
                <el-checkbox label="基本信息">基本信息</el-checkbox>
                <el-checkbox label="状态信息">状态信息</el-checkbox>
                <el-checkbox label="质量评分">质量评分</el-checkbox>
                <el-checkbox label="审核记录">审核记录</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form>
        </div>

        <!-- 批量审核 -->
        <div v-if="operationType === 'audit'">
          <el-form :model="auditForm" label-width="120px">
            <el-form-item label="审核类型">
              <el-select v-model="auditForm.auditType" placeholder="请选择审核类型">
                <el-option label="初审" value="初审" />
                <el-option label="复审" value="复审" />
                <el-option label="终审" value="终审" />
              </el-select>
            </el-form-item>
            <el-form-item label="审核结果">
              <el-radio-group v-model="auditForm.result">
                <el-radio label="通过">审核通过</el-radio>
                <el-radio label="不通过">审核不通过</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="审核意见">
              <el-input
                v-model="auditForm.opinion"
                type="textarea"
                :rows="3"
                placeholder="请输入审核意见"
              />
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading" :disabled="!operationType">
        {{ getSubmitButtonText() }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import {
  batchUpdateStatementStatus,
  batchDeleteStatement,
  exportFinancialStatement,
  batchAuditStatement
} from '@/api/enterprise/financial'

export default {
  name: 'BatchOperationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    selectedItems: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      loading: false,
      operationType: '',
      statusForm: {
        targetStatus: '',
        reason: ''
      },
      deleteForm: {
        reason: ''
      },
      exportForm: {
        format: 'excel',
        fields: ['基本信息', '状态信息']
      },
      auditForm: {
        auditType: '',
        result: '通过',
        opinion: ''
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
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      this.operationType = ''
      this.statusForm = { targetStatus: '', reason: '' }
      this.deleteForm = { reason: '' }
      this.exportForm = { format: 'excel', fields: ['基本信息', '状态信息'] }
      this.auditForm = { auditType: '', result: '通过', opinion: '' }
    },
    handleOperationTypeChange() {
      // 重置相关表单
    },
    handleSubmit() {
      if (!this.operationType) {
        this.$message.warning('请选择操作类型')
        return
      }

      if (this.selectedItems.length === 0) {
        this.$message.warning('请选择要操作的项目')
        return
      }

      this.loading = true
      const ids = this.selectedItems.map(item => item.statementId || item.id)

      let apiCall
      switch (this.operationType) {
        case 'updateStatus':
          apiCall = batchUpdateStatementStatus({ ids, targetStatus: this.statusForm.targetStatus, reason: this.statusForm.reason })
          break
        case 'delete':
          apiCall = batchDeleteStatement({ ids, reason: this.deleteForm.reason })
          break
        case 'export':
          apiCall = exportFinancialStatement({ ids, format: this.exportForm.format, fields: this.exportForm.fields })
          break
        case 'audit':
          apiCall = batchAuditStatement({ ids, auditType: this.auditForm.auditType, result: this.auditForm.result, opinion: this.auditForm.opinion })
          break
        default:
          this.loading = false
          return
      }

      apiCall.then(() => {
        this.$message.success(`${this.getSubmitButtonText()}成功`)
        this.$emit('refresh')
        this.handleClose()
      }).catch(() => {
        this.$message.error(`${this.getSubmitButtonText()}失败，请稍后重试`)
      }).finally(() => {
        this.loading = false
      })
    },
    getSubmitButtonText() {
      const textMap = {
        updateStatus: '批量更新',
        delete: '批量删除',
        export: '开始导出',
        audit: '批量审核'
      }
      return textMap[this.operationType] || '确定'
    },
    getStatusType(status) {
      const statusMap = {
        '待开始': 'info',
        '进行中': 'warning',
        '已完成': 'success',
        '已暂停': 'warning',
        '已取消': 'danger'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}
</style>
