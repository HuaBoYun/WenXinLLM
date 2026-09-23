<template>
  <el-dialog
    title="投资效果分析"
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
          <el-descriptions-item label="投资金额">{{ projectData.investAmount }}万元</el-descriptions-item>
          <el-descriptions-item label="投资类型">{{ getInvestTypeText(projectData.investType) }}</el-descriptions-item>
          <el-descriptions-item label="项目状态">
            <el-tag :type="getStatusTag(projectData.projectStatus)" size="mini">
              {{ getStatusText(projectData.projectStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="是否主业">
            <el-tag :type="projectData.isMainBiz === 'Y' ? 'success' : 'info'" size="mini">
              {{ projectData.isMainBiz === 'Y' ? '是' : '否' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 收益对比 -->
      <el-row :gutter="16" class="mb-16">
        <el-col :span="8">
          <el-card shadow="never" class="effect-stat-card">
            <div class="stat-item">
              <div class="stat-value" style="color:#409EFF;">
                {{ effectData.expectedReturn != null ? effectData.expectedReturn + '%' : '-' }}
              </div>
              <div class="stat-label">预期收益率</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never" class="effect-stat-card">
            <div class="stat-item">
              <div class="stat-value" :style="{ color: getReturnColor(effectData.actualReturn, effectData.expectedReturn) }">
                {{ effectData.actualReturn != null ? effectData.actualReturn + '%' : '暂无数据' }}
              </div>
              <div class="stat-label">实际收益率</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never" class="effect-stat-card">
            <div class="stat-item">
              <div class="stat-value" :style="{ color: effectData.deviation >= 0 ? '#67C23A' : '#F56C6C' }">
                {{ effectData.deviation != null ? (effectData.deviation >= 0 ? '+' : '') + effectData.deviation + '%' : '-' }}
              </div>
              <div class="stat-label">收益偏差</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 效果评估 -->
      <el-card shadow="never" class="mb-16">
        <div slot="header"><span>效果评估</span></div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="投资回报评估">
            <el-tag :type="getEffectLevelTag(effectData.effectLevel)" size="small">
              {{ getEffectLevelText(effectData.effectLevel) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="评估时间">{{ effectData.evaluateTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="评估说明" :span="2">
            {{ effectData.effectDesc || getDefaultEffectDesc() }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 后评价记录 -->
      <el-card shadow="never">
        <div slot="header"><span>后评价记录</span></div>
        <el-table :data="evalList" border stripe size="small">
          <el-table-column label="评价期间" prop="evalPeriod" width="120" />
          <el-table-column label="预期收益率" prop="expectedReturn" width="120" align="right">
            <template slot-scope="scope">{{ scope.row.expectedReturn != null ? scope.row.expectedReturn + '%' : '-' }}</template>
          </el-table-column>
          <el-table-column label="实际收益率" prop="actualReturn" width="120" align="right">
            <template slot-scope="scope">{{ scope.row.actualReturn != null ? scope.row.actualReturn + '%' : '-' }}</template>
          </el-table-column>
          <el-table-column label="评价结论" prop="evalConclusion" min-width="150" />
          <el-table-column label="评价时间" prop="createTime" width="160" />
        </el-table>
        <div v-if="evalList.length === 0" style="text-align:center;padding:30px;color:#909399;">
          暂无后评价记录
        </div>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { analyzeInvestmentEffect } from '@/api/stateAssets/investmentDecision'

export default {
  name: 'EffectAnalysisDialog',
  props: {
    visible: { type: Boolean, default: false },
    projectData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      effectData: {},
      evalList: []
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
        this.loadEffectData()
      }
    }
  },
  methods: {
    async loadEffectData() {
      this.loading = true
      try {
        const res = await analyzeInvestmentEffect({ projectId: this.projectData.projectId })
        if (res.result === 200 && res.data) {
          const d = res.data
          this.effectData = {
            expectedReturn: d.expectedReturn != null ? Number(d.expectedReturn) : null,
            actualReturn: d.actualReturn != null ? Number(d.actualReturn) : null,
            deviation: d.deviation != null ? Number(d.deviation) : null,
            effectLevel: d.effectLevel || null,
            effectDesc: d.effectDesc || null,
            evaluateTime: d.evaluateTime || null
          }
          this.evalList = d.evalList || d.tlist || []
        } else {
          // 降级：使用 projectData 中的数据
          const exp = this.projectData.expectedReturn != null ? Number(this.projectData.expectedReturn) : null
          const act = this.projectData.actualReturn != null ? Number(this.projectData.actualReturn) : null
          const dev = (exp != null && act != null) ? Number((act - exp).toFixed(2)) : null
          this.effectData = {
            expectedReturn: exp,
            actualReturn: act,
            deviation: dev,
            effectLevel: dev != null ? (dev >= 0 ? 'GOOD' : 'POOR') : null,
            effectDesc: null,
            evaluateTime: null
          }
          this.evalList = []
        }
      } catch (e) {
        this.$message.error('加载效果分析数据失败')
      } finally {
        this.loading = false
      }
    },
    getDefaultEffectDesc() {
      const { expectedReturn, actualReturn } = this.effectData
      if (expectedReturn == null && actualReturn == null) return '暂无收益数据，待项目执行后评估'
      if (actualReturn == null) return `预期收益率 ${expectedReturn}%，实际收益率待统计`
      if (actualReturn >= expectedReturn) return `实际收益率(${actualReturn}%)达到预期(${expectedReturn}%)，投资效果良好`
      return `实际收益率(${actualReturn}%)低于预期(${expectedReturn}%)，需关注投资回报`
    },
    getReturnColor(actual, expected) {
      if (actual == null) return '#909399'
      if (expected == null) return '#409EFF'
      return actual >= expected ? '#67C23A' : '#F56C6C'
    },
    getEffectLevelTag(level) {
      return { GOOD: 'success', POOR: 'danger', AVERAGE: 'warning' }[level] || 'info'
    },
    getEffectLevelText(level) {
      return { GOOD: '达到预期', POOR: '未达预期', AVERAGE: '基本达标' }[level] || '待评估'
    },
    getInvestTypeText(t) {
      return { EQUITY: '股权', DEBT: '债权', MIXED: '混合', FUND: '基金' }[t] || t
    },
    getStatusTag(s) {
      return { PLANNING: 'info', APPROVED: 'success', EXECUTING: 'primary', COMPLETED: 'success', SUSPENDED: 'warning' }[s] || 'info'
    },
    getStatusText(s) {
      return { PLANNING: '规划中', APPROVED: '已审批', EXECUTING: '执行中', COMPLETED: '已完成', SUSPENDED: '已暂停' }[s] || s
    },
    handleClose() { this.dialogVisible = false }
  }
}
</script>

<style scoped>
.mb-16 { margin-bottom: 16px; }
.effect-stat-card .stat-item { text-align: center; padding: 10px; }
.effect-stat-card .stat-value { font-size: 28px; font-weight: bold; line-height: 1; }
.effect-stat-card .stat-label { font-size: 13px; color: #909399; margin-top: 6px; }
.dialog-footer { text-align: right; }
</style>
