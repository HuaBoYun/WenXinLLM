<template>
  <el-dialog
    title="计划监控"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-if="planData">
      <!-- 监控概览 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>计划执行监控概览</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-title"><i class="el-icon-edit-outline" style="color: #409EFF"></i> 制定进度</div>
              <div class="stat-value">{{ planData.formulationProgress || 0 }}%</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-title"><i class="el-icon-data-line" style="color: #67C23A"></i> 执行进度</div>
              <div class="stat-value">{{ planData.executionProgress || 0 }}%</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-title"><i class="el-icon-trophy" style="color: #E6A23C"></i> 目标完成率</div>
              <div class="stat-value">{{ targetCompletionRate }}%</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-title"><i class="el-icon-flag" style="color: #909399"></i> 里程碑完成</div>
              <div class="stat-value">{{ milestoneCompletionCount }}个</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 监控设置 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>监控设置</span>
        </div>
        <el-form :model="monitoringForm" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="监控频率">
                <el-select v-model="monitoringForm.frequency" style="width: 100%">
                  <el-option label="每日" value="daily" />
                  <el-option label="每周" value="weekly" />
                  <el-option label="每月" value="monthly" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预警阈值">
                <el-input-number
                  v-model="monitoringForm.warningThreshold"
                  :min="0"
                  :max="100"
                  :precision="0"
                  style="width: 100%"
                />
                <span style="margin-left: 10px; color: #909399;">%</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="监控人员">
            <el-input v-model="monitoringForm.monitoringPersonnel" placeholder="请输入监控人员" />
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 监控记录 -->
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>监控记录</span>
        </div>
        <el-table :data="monitoringRecords" border style="width: 100%">
          <el-table-column prop="monitoringDate" label="监控时间" width="150" />
          <el-table-column prop="monitoringType" label="监控类型" width="120">
            <template slot-scope="scope">
              <el-tag :type="getMonitoringType(scope.row.monitoringType)">
                {{ scope.row.monitoringType }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="monitoringContent" label="监控内容" />
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remarks" label="备注" />
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button size="mini" @click="viewRecord(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 监控记录详情弹窗 -->
    <el-dialog
      title="监控记录详情"
      :visible.sync="recordDetailVisible"
      width="500px"
      append-to-body
      :close-on-click-modal="true"
    >
      <el-descriptions :column="1" border v-if="currentRecord">
        <el-descriptions-item label="监控时间">{{ currentRecord.monitoringDate }}</el-descriptions-item>
        <el-descriptions-item label="监控类型">
          <el-tag :type="getMonitoringType(currentRecord.monitoringType)" size="small">{{ currentRecord.monitoringType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="监控内容">{{ currentRecord.monitoringContent }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentRecord.status)" size="small">{{ currentRecord.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ currentRecord.remarks || '无' }}</el-descriptions-item>
        <el-descriptions-item label="关联计划">{{ planData.planName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="计划状态">{{ planData.planStatus || '-' }}</el-descriptions-item>
        <el-descriptions-item label="执行进度">{{ planData.executionProgress || 0 }}%</el-descriptions-item>
      </el-descriptions>
      <span slot="footer">
        <el-button @click="recordDetailVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button type="primary" @click="saveMonitoringSettings">保存设置</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { monitorPlan } from '@/api/enterprise/operationPlan'

export default {
  name: 'PlanMonitoringDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    planData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      recordDetailVisible: false,
      currentRecord: null,
      monitoringForm: {
        frequency: 'weekly',
        warningThreshold: 80,
        monitoringPersonnel: ''
      },
      monitoringRecords: [
        {
          monitoringDate: '2024-03-15',
          monitoringType: '进度监控',
          monitoringContent: '第一季度目标执行情况检查',
          status: '正常',
          remarks: '按计划执行'
        },
        {
          monitoringDate: '2024-03-10',
          monitoringType: '风险监控',
          monitoringContent: '市场风险评估',
          status: '预警',
          remarks: '需要关注市场变化'
        }
      ]
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
    targetCompletionRate() {
      if (!this.planData) return 0
      const formulation = this.planData.formulationProgress || 0
      const execution = this.planData.executionProgress || 0
      return Math.round((formulation + execution) / 2)
    },
    milestoneCompletionCount() {
      if (!this.planData) return 0
      // 基于执行进度估算里程碑完成数
      const progress = this.planData.executionProgress || 0
      if (progress >= 100) return 4
      if (progress >= 75) return 3
      if (progress >= 50) return 2
      if (progress >= 25) return 1
      return 0
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    async saveMonitoringSettings() {
      try {
        const updateBy = this.$store && this.$store.getters && this.$store.getters.name ? this.$store.getters.name : 'admin'
        await monitorPlan(this.planData.planId, updateBy)
        this.$message.success('监控设置保存成功')
        this.$emit('refresh')
        this.handleClose()
      } catch (error) {
        this.$message.error('保存监控设置失败: ' + (error.message || '未知错误'))
      }
    },
    viewRecord(record) {
      this.currentRecord = { ...record }
      this.recordDetailVisible = true
    },
    getMonitoringType(type) {
      const typeMap = {
        '进度监控': 'primary',
        '风险监控': 'warning',
        '质量监控': 'success'
      }
      return typeMap[type] || 'info'
    },
    getStatusType(status) {
      const statusMap = {
        '正常': 'success',
        '预警': 'warning',
        '异常': 'danger'
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
.stat-item {
  text-align: center;
  padding: 10px 0;
}
.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}
</style>
