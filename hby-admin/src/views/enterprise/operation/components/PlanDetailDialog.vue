<template>
  <el-dialog
    title="经营计划详情"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-if="planData">
      <!-- 基本信息 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>计划基本信息</span>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="计划ID">{{ planData.planId }}</el-descriptions-item>
          <el-descriptions-item label="计划名称">{{ planData.planName }}</el-descriptions-item>
          <el-descriptions-item label="计划类型">{{ planData.planType }}</el-descriptions-item>
          <el-descriptions-item label="计划年度">{{ planData.planYear }}</el-descriptions-item>
          <el-descriptions-item label="制定状态">
            <el-tag :type="getFormulationStatusType(planData.planStatus)">
              {{ planData.planStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审批状态">
            <el-tag :type="getApprovalStatusType(planData.approvalStatus)">
              {{ planData.approvalStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="执行状态">
            <el-tag :type="getExecutionStatusType(planData.executionStatus)">
              {{ planData.executionStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="制定负责人">{{ planData.planManager }}</el-descriptions-item>
          <el-descriptions-item label="执行负责人">{{ planData.monitoringManager }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 计划进度 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>计划执行进度</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="stat-item">
              <div class="stat-title"><i class="el-icon-edit-outline" style="color: #409EFF"></i> 制定进度</div>
              <div class="stat-value">{{ planData.formulationProgress || 0 }}%</div>
            </div>
            <el-progress
              :percentage="planData.formulationProgress || 0"
              :status="planData.formulationProgress === 100 ? 'success' : null"
              style="margin-top: 10px;"
            />
          </el-col>
          <el-col :span="8">
            <div class="stat-item">
              <div class="stat-title"><i class="el-icon-data-line" style="color: #67C23A"></i> 执行进度</div>
              <div class="stat-value">{{ planData.executionProgress || 0 }}%</div>
            </div>
            <el-progress
              :percentage="planData.executionProgress || 0"
              :status="planData.executionProgress === 100 ? 'success' : null"
              style="margin-top: 10px;"
            />
          </el-col>
          <el-col :span="8">
            <div class="stat-item">
              <div class="stat-title"><i class="el-icon-trophy" style="color: #E6A23C"></i> 总体完成度</div>
              <div class="stat-value">{{ overallProgress }}%</div>
            </div>
            <el-progress
              :percentage="overallProgress"
              :status="overallProgress === 100 ? 'success' : null"
              style="margin-top: 10px;"
            />
          </el-col>
        </el-row>
      </el-card>

      <!-- 计划目标 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>计划目标指标</span>
        </div>
        <el-table :data="planTargets" border style="width: 100%">
          <el-table-column prop="targetName" label="目标名称" />
          <el-table-column prop="targetValue" label="目标值" width="120">
            <template slot-scope="scope">
              {{ scope.row.targetValue }} {{ scope.row.unit }}
            </template>
          </el-table-column>
          <el-table-column prop="currentValue" label="当前值" width="120">
            <template slot-scope="scope">
              {{ scope.row.currentValue || 0 }} {{ scope.row.unit }}
            </template>
          </el-table-column>
          <el-table-column label="完成率" width="100">
            <template slot-scope="scope">
              <span :style="{ color: getCompletionRateColor(scope.row) }">
                {{ getCompletionRate(scope.row) }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getTargetStatusType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remarks" label="备注" />
        </el-table>
      </el-card>

      <!-- 关键里程碑 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>关键里程碑</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(milestone, index) in milestones"
            :key="index"
            :timestamp="milestone.plannedDate"
            :type="getMilestoneType(milestone.status)"
          >
            <el-card>
              <h4>{{ milestone.milestoneName }}</h4>
              <p>{{ milestone.description }}</p>
              <el-tag :type="getMilestoneStatusType(milestone.status)">
                {{ milestone.status }}
              </el-tag>
              <span v-if="milestone.actualDate" style="margin-left: 10px; color: #909399;">
                实际完成: {{ milestone.actualDate }}
              </span>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-card>

      <!-- 计划描述 -->
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>计划说明</span>
        </div>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="计划描述">
            {{ planData.planDescription || '暂无描述' }}
          </el-descriptions-item>
          <el-descriptions-item label="关键措施">
            {{ planData.keyMeasures || '暂无关键措施' }}
          </el-descriptions-item>
          <el-descriptions-item label="风险分析">
            {{ planData.riskAnalysis || '暂无风险分析' }}
          </el-descriptions-item>
          <el-descriptions-item label="备注信息">
            {{ planData.remarks || '暂无备注' }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button type="primary" @click="exportDetail">导出详情</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'PlanDetailDialog',
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
      planTargets: [],
      milestones: []
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
    overallProgress() {
      const formulation = this.planData.formulationProgress || 0
      const execution = this.planData.executionProgress || 0
      return Math.round((formulation * 0.3 + execution * 0.7))
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    exportDetail() {
      this.$message.success('计划详情导出成功')
    },
    getFormulationStatusType(status) {
      const statusMap = {
        '待制定': 'info',
        '制定中': 'warning',
        '已制定': 'success',
        '已暂停': 'warning',
        '已取消': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getApprovalStatusType(status) {
      const statusMap = {
        '待审批': 'warning',
        '初审中': 'primary',
        '终审中': 'primary',
        '审批通过': 'success',
        '审批不通过': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getExecutionStatusType(status) {
      const statusMap = {
        '未开始': 'info',
        '执行中': 'warning',
        '已完成': 'success',
        '已暂停': 'warning'
      }
      return statusMap[status] || 'info'
    },
    getTargetStatusType(status) {
      const statusMap = {
        '未开始': 'info',
        '进行中': 'warning',
        '已完成': 'success',
        '延期': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getCompletionRate(target) {
      const targetValue = target.targetValue || 0
      const currentValue = target.currentValue || 0
      return targetValue > 0 ? Math.round((currentValue / targetValue) * 100) : 0
    },
    getCompletionRateColor(target) {
      const rate = this.getCompletionRate(target)
      if (rate >= 100) return '#67C23A'
      if (rate >= 80) return '#E6A23C'
      return '#F56C6C'
    },
    getMilestoneType(status) {
      const typeMap = {
        '已完成': 'success',
        '进行中': 'primary',
        '未开始': 'info',
        '延期': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getMilestoneStatusType(status) {
      const statusMap = {
        '已完成': 'success',
        '进行中': 'warning',
        '未开始': 'info',
        '延期': 'danger'
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
