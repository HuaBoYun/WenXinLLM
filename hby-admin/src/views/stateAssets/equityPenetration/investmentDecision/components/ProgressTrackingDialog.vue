<template>
  <el-dialog
    title="投资进度跟踪"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <div v-loading="loading">
      <!-- 项目基本信息 -->
      <el-card shadow="never" class="mb-16">
        <div slot="header"><span>项目信息</span></div>
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="项目名称">{{ projectData.projectName }}</el-descriptions-item>
          <el-descriptions-item label="投资企业">{{ projectData.companyName }}</el-descriptions-item>
          <el-descriptions-item label="被投资企业">{{ projectData.targetCompany }}</el-descriptions-item>
          <el-descriptions-item label="投资金额">{{ projectData.investAmount }}万元</el-descriptions-item>
          <el-descriptions-item label="项目状态">
            <el-tag :type="getStatusTag(projectData.projectStatus)" size="mini">
              {{ getStatusText(projectData.projectStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审批日期">{{ projectData.approvalDate || '未审批' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 进度概览 -->
      <el-row :gutter="16" class="mb-16">
        <el-col :span="8">
          <el-card shadow="never" class="progress-stat-card">
            <div class="stat-item">
              <div class="stat-value" style="color:#409EFF;">{{ progressData.totalMilestones || 0 }}</div>
              <div class="stat-label">总里程碑数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never" class="progress-stat-card">
            <div class="stat-item">
              <div class="stat-value" style="color:#67C23A;">{{ progressData.completedMilestones || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never" class="progress-stat-card">
            <div class="stat-item">
              <div class="stat-value" style="color:#E6A23C;">{{ progressData.pendingMilestones || 0 }}</div>
              <div class="stat-label">待完成</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 整体进度条 -->
      <el-card shadow="never" class="mb-16">
        <div slot="header"><span>整体进度</span></div>
        <div style="padding: 10px 0;">
          <el-progress
            :percentage="progressData.overallProgress || 0"
            :status="getProgressStatus(progressData.overallProgress)"
            :stroke-width="20"
            :format="(p) => p + '%'"
          />
          <div style="margin-top:8px;color:#909399;font-size:13px;">
            进度说明：{{ progressData.progressDesc || getDefaultProgressDesc() }}
          </div>
        </div>
      </el-card>

      <!-- 里程碑列表 -->
      <el-card shadow="never">
        <div slot="header"><span>进度记录</span></div>
        <el-table :data="milestoneList" border stripe size="small">
          <el-table-column label="项目名称" prop="projectName" min-width="150">
            <template slot-scope="scope">{{ scope.row.projectName || projectData.projectName }}</template>
          </el-table-column>
          <el-table-column label="计划进度" prop="planProgress" width="110" align="center">
            <template slot-scope="scope">
              <el-progress
                v-if="scope.row.planProgress != null"
                :percentage="Number(scope.row.planProgress)"
                :stroke-width="8"
                :show-text="true"
              />
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="实际进度" prop="actualProgress" width="110" align="center">
            <template slot-scope="scope">
              <el-progress
                v-if="scope.row.actualProgress != null"
                :percentage="Number(scope.row.actualProgress)"
                :stroke-width="8"
                :status="Number(scope.row.actualProgress) >= Number(scope.row.planProgress || 0) ? 'success' : 'exception'"
                :show-text="true"
              />
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="计划完成日期" prop="planDate" width="130" />
          <el-table-column label="逾期天数" prop="delayDays" width="90" align="center">
            <template slot-scope="scope">
              <span :style="{ color: scope.row.delayDays > 0 ? '#F56C6C' : '#67C23A' }">
                {{ scope.row.delayDays > 0 ? '+' + scope.row.delayDays + '天' : '正常' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="状态" prop="status" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getMilestoneStatusTag(scope.row.status)" size="mini">
                {{ getMilestoneStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="记录时间" prop="createTime" width="160" />
        </el-table>
        <div v-if="milestoneList.length === 0" style="text-align:center;padding:30px;color:#909399;">
          暂无进度记录，请先插入测试数据
        </div>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getInvestmentProgress } from '@/api/stateAssets/investmentDecision'

export default {
  name: 'ProgressTrackingDialog',
  props: {
    visible: { type: Boolean, default: false },
    projectData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      progressData: {},
      milestoneList: []
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  watch: {
    visible(val) {
      if (val && this.projectData.projectId) {
        this.loadProgressData()
      }
    }
  },
  methods: {
    async loadProgressData() {
      this.loading = true
      try {
        const res = await getInvestmentProgress({ projectId: this.projectData.projectId })
        if (res.result === 200 && res.data) {
          const d = res.data
          const list = d.tlist || d.list || (Array.isArray(d) ? d : [])
          this.milestoneList = list
          if (list.length > 0) {
            // 用最新一条记录的 actualProgress 作为整体进度
            const latestActual = Math.max(...list.map(m => Number(m.actualProgress) || 0))
            const normalCount = list.filter(m => m.status === 'NORMAL').length
            const delayedCount = list.filter(m => m.status === 'DELAYED' || m.status === 'AT_RISK').length
            this.progressData = {
              totalMilestones: list.length,
              completedMilestones: normalCount,
              pendingMilestones: delayedCount,
              overallProgress: Math.min(100, latestActual),
              progressDesc: d.progressDesc || null
            }
          } else {
            this.progressData = {
              totalMilestones: 0,
              completedMilestones: 0,
              pendingMilestones: 0,
              overallProgress: this.getStatusProgress(),
              progressDesc: null
            }
          }
        } else {
          this.milestoneList = []
          this.progressData = {
            totalMilestones: 0,
            completedMilestones: 0,
            pendingMilestones: 0,
            overallProgress: this.getStatusProgress(),
            progressDesc: null
          }
        }
      } catch (e) {
        this.$message.error('加载进度数据失败')
      } finally {
        this.loading = false
      }
    },
    getStatusProgress() {
      const map = { PLANNING: 10, APPROVED: 30, EXECUTING: 60, COMPLETED: 100, SUSPENDED: 0 }
      return map[this.projectData.projectStatus] || 0
    },
    getDefaultProgressDesc() {
      const map = {
        PLANNING: '项目规划中，尚未启动',
        APPROVED: '项目已审批，准备执行',
        EXECUTING: '项目执行中，请关注进度',
        COMPLETED: '项目已完成',
        SUSPENDED: '项目已暂停'
      }
      return map[this.projectData.projectStatus] || '暂无进度说明'
    },
    getProgressStatus(p) {
      if (p >= 100) return 'success'
      if (p >= 60) return ''
      if (p >= 30) return 'warning'
      return 'exception'
    },
    getStatusTag(s) {
      return { PLANNING: 'info', APPROVED: 'success', EXECUTING: 'primary', COMPLETED: 'success', SUSPENDED: 'warning' }[s] || 'info'
    },
    getStatusText(s) {
      return { PLANNING: '规划中', APPROVED: '已审批', EXECUTING: '执行中', COMPLETED: '已完成', SUSPENDED: '已暂停' }[s] || s
    },
    getMilestoneStatusTag(s) {
      return { COMPLETED: 'success', DONE: 'success', IN_PROGRESS: 'primary', PENDING: 'info', OVERDUE: 'danger' }[s] || 'info'
    },
    getMilestoneStatusText(s) {
      return { COMPLETED: '已完成', DONE: '已完成', IN_PROGRESS: '进行中', PENDING: '待开始', OVERDUE: '已逾期' }[s] || s
    },
    handleClose() { this.dialogVisible = false }
  }
}
</script>

<style scoped>
.mb-16 { margin-bottom: 16px; }
.progress-stat-card .stat-item { text-align: center; padding: 10px; }
.progress-stat-card .stat-value { font-size: 28px; font-weight: bold; line-height: 1; }
.progress-stat-card .stat-label { font-size: 13px; color: #909399; margin-top: 6px; }
.dialog-footer { text-align: right; }
</style>
