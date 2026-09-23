<template>
  <div class="non-main-page" :style="themeVars">
    <div class="page-banner">
      <div class="banner-left">
        <i class="el-icon-pie-chart banner-icon" />
        <div>
          <h2>非主业投资分析</h2>
          <p>实时监控非主业投资占比，预警超限风险，推进非主业资产清退</p>
        </div>
      </div>
      <div class="banner-right">
        <el-tag v-if="overLimitCount > 0" type="danger" effect="dark"><i class="el-icon-warning" /> {{ overLimitCount }} 家企业非主业占比超限</el-tag>
        <el-tag type="info" effect="plain" style="margin-left:8px">预警线：占比 > 20%</el-tag>
      </div>
    </div>

    <!-- 各企业占比仪表盘 -->
    <div class="section-title"><i class="el-icon-odometer" /> 各企业非主业投资占比总览</div>
    <el-row :gutter="16" class="gauge-row">
      <el-col :span="4" v-for="c in companyStats" :key="c.company">
        <div class="gauge-card" :class="{ 'gauge-danger': c.ratio > 20, 'gauge-warn': c.ratio > 10 && c.ratio <= 20 }">
          <div class="gc-company">{{ c.company }}</div>
          <div class="gc-gauge">
            <el-progress type="dashboard" :percentage="c.ratio"
              :color="c.ratio > 20 ? '#FF4D4F' : c.ratio > 10 ? '#FA8C16' : '#52C41A'"
              :width="110" :stroke-width="10" />
          </div>
          <div class="gc-bottom">
            <div class="gc-detail">非主业金额：{{ c.nonMainAmount }}万</div>
            <div class="gc-detail">总投资：{{ c.totalAmount }}万</div>
            <el-tag :type="c.ratio > 20 ? 'danger' : c.ratio > 10 ? 'warning' : 'success'" size="mini">
              {{ c.ratio > 20 ? '超限预警' : c.ratio > 10 ? '关注' : '正常' }}
            </el-tag>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 主体：tab切换 -->
    <el-card shadow="never" style="margin-top:16px">
      <el-tabs v-model="activeTab">
        <!-- Tab1: 趋势分析 -->
        <el-tab-pane label="趋势分析" name="trend">
          <div class="tab-desc">集团整体及各企业非主业占比12个月滚动趋势，红色虚线为20%预警线</div>
          <div ref="trendChart" style="height:320px"></div>
        </el-tab-pane>

        <!-- Tab2: 非主业项目清单 -->
        <el-tab-pane label="非主业项目清单" name="list">
          <el-table :data="nonMainProjects" border size="small" style="width:100%">
            <el-table-column label="项目编号" prop="projectId" width="130" />
            <el-table-column label="项目名称" prop="projectName" min-width="160" show-overflow-tooltip />
            <el-table-column label="投资企业" prop="company" width="90" />
            <el-table-column label="投资金额(万)" width="110" align="right">
              <template slot-scope="{row}"><span class="amount-text">{{ row.investAmount.toLocaleString() }}</span></template>
            </el-table-column>
            <el-table-column label="占比贡献" prop="ratioContrib" width="90" align="right">
              <template slot-scope="{row}"><span style="color:#FA8C16;font-weight:600">{{ row.ratioContrib }}%</span></template>
            </el-table-column>
            <el-table-column label="实际收益率" width="100" align="right">
              <template slot-scope="{row}">
                <span :style="{color: row.actualReturn < row.expectedReturn ? '#FF4D4F' : '#52C41A', fontWeight:600}">{{ row.actualReturn }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="清退状态" width="100" align="center">
              <template slot-scope="{row}">
                <el-tag :type="exitStatusMap[row.exitStatus].type" size="mini">{{ exitStatusMap[row.exitStatus].label }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="计划清退日期" prop="planExitDate" width="110" />
            <el-table-column label="操作" width="120" align="center">
              <template slot-scope="{row}">
                <el-button size="mini" type="text" @click="handleViewProject(row)">查看</el-button>
                <el-button v-if="row.exitStatus === 'NOT_PLANNED'" size="mini" type="text" style="color:#FA8C16" @click="handlePlanExit(row)">制定清退计划</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- Tab3: 清退追踪 -->
        <el-tab-pane label="清退进度追踪" name="exit">
          <el-alert type="info" :closable="false" style="margin-bottom:12px">
            <span slot="title">清退原则：非主业占比 > 20% 的投资，应制定3年清退计划；超过30%需启动紧急清退程序</span>
          </el-alert>
          <el-table :data="exitPlanList" border size="small" style="width:100%">
            <el-table-column label="项目名称" prop="projectName" min-width="150" show-overflow-tooltip />
            <el-table-column label="投资企业" prop="company" width="90" />
            <el-table-column label="投资金额(万)" width="110" align="right">
              <template slot-scope="{row}"><span class="amount-text">{{ row.investAmount.toLocaleString() }}</span></template>
            </el-table-column>
            <el-table-column label="清退进度" width="160">
              <template slot-scope="{row}">
                <el-progress :percentage="row.exitProgress"
                  :color="row.exitProgress >= 80 ? '#52C41A' : row.exitProgress >= 40 ? '#FA8C16' : '#FF4D4F'"
                  :stroke-width="10" />
              </template>
            </el-table-column>
            <el-table-column label="已回收金额(万)" width="120" align="right">
              <template slot-scope="{row}"><span style="color:#52C41A;font-weight:600">{{ row.recoveredAmount.toLocaleString() }}</span></template>
            </el-table-column>
            <el-table-column label="计划完成时间" prop="planExitDate" width="110" />
            <el-table-column label="负责人" prop="owner" width="80" />
            <el-table-column label="清退状态" width="90" align="center">
              <template slot-scope="{row}">
                <el-tag :type="exitStatusMap[row.exitStatus].type" size="mini">{{ exitStatusMap[row.exitStatus].label }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 项目详情弹窗 -->
    <el-dialog title="非主业项目详情" :visible.sync="viewDialogVisible" width="580px" :close-on-click-modal="false">
      <div v-if="viewProject">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="项目编号">{{ viewProject.projectId }}</el-descriptions-item>
          <el-descriptions-item label="项目名称">{{ viewProject.projectName }}</el-descriptions-item>
          <el-descriptions-item label="投资企业">{{ viewProject.company }}</el-descriptions-item>
          <el-descriptions-item label="投资金额(万)">{{ viewProject.investAmount ? viewProject.investAmount.toLocaleString() : 0 }}</el-descriptions-item>
          <el-descriptions-item label="非主业占比">
            <span style="color:#FA8C16;font-weight:600">{{ viewProject.ratioContrib }}%</span>
          </el-descriptions-item>
          <el-descriptions-item label="清退状态">
            <el-tag :type="exitStatusMap[viewProject.exitStatus] ? exitStatusMap[viewProject.exitStatus].type : 'info'" size="mini">
              {{ exitStatusMap[viewProject.exitStatus] ? exitStatusMap[viewProject.exitStatus].label : viewProject.exitStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="计划清退日期">{{ viewProject.planExitDate || '未计划' }}</el-descriptions-item>
          <el-descriptions-item label="预期收益率">{{ viewProject.expectedReturn }}%</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="viewDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getNonMainBizList, getNonMainBizStats, getNonMainBizTrend } from '@/api/stateAssets/investPenetration'
import { investThemeMixin } from '../themeMixin'

export default {
  name: 'InvestNonMainBiz',
  mixins: [investThemeMixin],
  data() {
    return {
      activeTab: 'trend',
      companyId: '',
      companyStats: [],
      nonMainProjects: [],
      exitPlanList: [],
      trendData: [],
      exitStatusMap: {
        NOT_PLANNED: { type: 'danger', label: '未计划' },
        PLANNED: { type: 'warning', label: '已计划' },
        IN_PROGRESS: { type: '', label: '清退中' },
        COMPLETED: { type: 'success', label: '已清退' },
      },
      viewDialogVisible: false,
      viewProject: null,
    }
  },
  computed: {
    overLimitCount() { return this.companyStats.filter(c => c.ratio > 20).length },
  },
  created() {
    this.initCompanyId()
    this.loadStats()
    this.loadProjects()
  },
  mounted() {
    this.$nextTick(() => { this.loadTrend() })
  },
  beforeDestroy() {
    if (this.$refs.trendChart) { const c = echarts.getInstanceByDom(this.$refs.trendChart); c && c.dispose() }
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    initCompanyId() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const org = userInfo.currentOrg || {}
        if (org.id) this.companyId = String(org.id)
      } catch (e) { console.warn('[initCompanyId]', e) }
    },
    async loadStats() {
      try {
        const res = await getNonMainBizStats({ companyId: this.companyId })
        if (res.result === 200 && res.data) {
          this.companyStats = (res.data.companyStats || []).map(c => ({
            company: c.company,
            ratio: Number(c.ratio) || 0,
            nonMainAmount: (Number(c.nonMainAmount) / 10000).toFixed(2),
            totalAmount: (Number(c.totalAmount) / 10000).toFixed(1),
          }))
        }
      } catch (e) {
        console.error(e)
      }
    },
    async loadProjects() {
      try {
        const res = await getNonMainBizList({ pageNumber: 1, pageSize: 50, companyId: this.companyId })
        if (res.result === 200 && res.data) {
          this.nonMainProjects = (res.data.tlist || []).map(item => ({
            projectId: item.id,
            projectName: item.projectName,
            company: item.companyName,
            investAmount: Number(item.investAmount) || 0,
            ratioContrib: Number(item.nonMainRatio) || 0,
            expectedReturn: 8.0,
            actualReturn: 0,
            exitStatus: item.riskLevel === 'HIGH' ? 'IN_PROGRESS' : item.riskLevel === 'MEDIUM' ? 'PLANNED' : 'NOT_PLANNED',
            planExitDate: null,
          }))
          this.exitPlanList = this.nonMainProjects
            .filter(p => p.exitStatus !== 'NOT_PLANNED')
            .map(p => ({
              ...p,
              exitProgress: p.exitStatus === 'COMPLETED' ? 100 : p.exitStatus === 'IN_PROGRESS' ? 35 : 0,
              recoveredAmount: 0,
              owner: '-',
            }))
        }
      } catch (e) {
        console.error(e)
      }
    },
    async loadTrend() {
      try {
        const res = await getNonMainBizTrend()
        if (res.result === 200 && res.data) {
          this.trendData = res.data
          this.initTrendChart()
        } else {
          this.initTrendChart()
        }
      } catch (e) {
        this.initTrendChart()
      }
    },
    initTrendChart() {
      if (!this.$refs.trendChart) return
      const chart = echarts.init(this.$refs.trendChart)
      const years = this.trendData.length > 0 ? this.trendData.map(t => t.year) : ['2021', '2022', '2023', '2024', '2025']
      const ratios = this.trendData.length > 0 ? this.trendData.map(t => Number(t.nonMainRatio) || 0) : [0, 0, 0, 0, 0]
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['集团整体', '预警线(20%)'], top: 0 },
        grid: { top: 40, left: 50, right: 20, bottom: 30 },
        xAxis: { type: 'category', data: years },
        yAxis: { type: 'value', name: '占比(%)', max: 45 },
        series: [
          { name: '预警线(20%)', type: 'line', data: Array(years.length).fill(20), lineStyle: { type: 'dashed', color: '#FF4D4F', width: 2 }, itemStyle: { color: '#FF4D4F' }, symbol: 'none' },
          { name: '集团整体', type: 'line', data: ratios, smooth: true, itemStyle: { color: this.ipPrimary } },
        ],
      })
      window.addEventListener('resize', this.handleResize)
    },
    handleResize() {
      if (this.$refs.trendChart) { const c = echarts.getInstanceByDom(this.$refs.trendChart); c && c.resize() }
    },
    handleViewProject(row) {
      this.viewProject = row
      this.viewDialogVisible = true
    },
    handlePlanExit(row) {
      this.$confirm('确认为 "' + row.projectName + '" 制定清退计划？', '制定清退计划', { type: 'warning' }).then(() => {
        row.exitStatus = 'PLANNED'
        this.$message.success('已提交清退计划申请')
      }).catch(() => {})
    },
  },
}
</script>

<style lang="scss" scoped>
.non-main-page { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.page-banner {
  background: linear-gradient(135deg, var(--ip-primary, #1A3A6B) 0%, var(--ip-secondary, #2A5298) 100%);
  border-radius: 8px; padding: 16px 24px; margin-bottom: 14px; color: #fff;
  display: flex; justify-content: space-between; align-items: center;
  .banner-left { display: flex; align-items: center; gap: 14px; }
  .banner-icon { font-size: 32px; color: var(--ip-accent, #FAAD14); }
  h2 { margin: 0; font-size: 18px; }
  p { margin: 4px 0 0; font-size: 12px; opacity: 0.8; }
}
.section-title { font-size: 14px; font-weight: 600; color: var(--ip-primary, #1A3A6B); margin-bottom: 10px; i { margin-right: 6px; } }
.gauge-row { margin-bottom: 0; }
.gauge-card {
  background: #fff; border-radius: 8px; padding: 14px 10px; text-align: center;
  box-shadow: 0 1px 6px rgba(0,0,0,.06); border: 2px solid #52C41A;
  transition: all 0.2s;
  &.gauge-warn { border-color: #FA8C16; }
  &.gauge-danger { border-color: #FF4D4F; background: #fff8f8; }
  .gc-company { font-size: 13px; font-weight: 600; color: var(--ip-primary, #1A3A6B); margin-bottom: 8px; }
  .gc-gauge { display: flex; justify-content: center; }
  .gc-bottom { margin-top: 8px; }
  .gc-detail { font-size: 11px; color: #888; }
}
.tab-desc { font-size: 12px; color: #888; margin-bottom: 10px; }
.amount-text { font-weight: 600; color: var(--ip-primary, #1A3A6B); }
::v-deep .el-table th { background: #f5f7ff; color: var(--ip-primary, #1A3A6B); }
</style>
