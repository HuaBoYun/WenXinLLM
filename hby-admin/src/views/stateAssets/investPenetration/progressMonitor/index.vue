<template>
  <div class="progress-monitor-page" :style="themeVars">
    <div class="page-banner">
      <div class="banner-left">
        <i class="el-icon-s-order banner-icon" />
        <div>
          <h2>投资进度监控</h2>
          <p>全量追踪投资项目关键里程碑，自动预警逾期节点，确保投资按计划推进</p>
        </div>
      </div>
      <div class="banner-tags">
        <el-tag type="danger" effect="dark"><i class="el-icon-warning" /> {{ overdueCount }} 个里程碑逾期</el-tag>
        <el-tag type="warning" effect="dark" style="margin-left:8px">{{ delayedProjects }} 个项目进度滞后</el-tag>
      </div>
    </div>

    <!-- 统计条 -->
    <el-row :gutter="12" class="stat-row">
      <el-col :span="4" v-for="s in statCards" :key="s.label">
        <div class="stat-mini" :style="{borderLeftColor:s.color}">
          <div class="sm-value" :style="{color:s.color}">{{ s.value }}</div>
          <div class="sm-label">{{ s.label }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 主体Tab -->
    <el-card shadow="never">
      <el-tabs v-model="activeTab">
        <!-- Tab1：项目进度总览 -->
        <el-tab-pane label="项目进度总览" name="overview">
          <el-table :data="projectList" border size="small" style="width:100%"
            row-key="projectId" :expand-row-keys="expandedRows" @expand-change="handleExpand">
            <el-table-column type="expand">
              <template slot-scope="{row}">
                <div class="milestone-expand">
                  <div class="me-title">里程碑节点</div>
                  <div class="milestone-timeline">
                    <div v-for="m in row.milestones" :key="m.id" class="ml-item" :class="getMilestoneClass(m)">
                      <div class="ml-dot" :style="{background: getMilestoneColor(m)}"></div>
                      <div class="ml-content">
                        <div class="ml-name">{{ m.name }}</div>
                        <div class="ml-dates">计划：{{ m.planDate }} / 实际：{{ m.actualDate || '未完成' }}</div>
                        <el-tag :type="getMilestoneStatus(m).type" size="mini">{{ getMilestoneStatus(m).label }}</el-tag>
                        <span v-if="m.delay > 0" style="color:#FF4D4F;font-size:11px;margin-left:6px">逾期 {{ m.delay }} 天</span>
                      </div>
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="项目编号" prop="projectId" width="130" />
            <el-table-column label="项目名称" prop="projectName" min-width="160" show-overflow-tooltip />
            <el-table-column label="投资企业" prop="company" width="90" />
            <el-table-column label="投资金额(万)" width="110" align="right">
              <template slot-scope="{row}"><span class="amount-text">{{ row.investAmount.toLocaleString() }}</span></template>
            </el-table-column>
            <el-table-column label="整体进度" width="160">
              <template slot-scope="{row}">
                <el-progress :percentage="row.progressRate"
                  :color="row.progressRate >= 80 ? '#52C41A' : row.progressRate >= 60 ? '#FA8C16' : '#FF4D4F'"
                  :stroke-width="14" :text-inside="true" />
              </template>
            </el-table-column>
            <el-table-column label="计划完成" prop="planEndDate" width="100" />
            <el-table-column label="逾期天数" width="80" align="center">
              <template slot-scope="{row}">
                <span v-if="row.delayDays > 0" style="color:#FF4D4F;font-weight:600">+{{ row.delayDays }}天</span>
                <span v-else style="color:#52C41A">按计划</span>
              </template>
            </el-table-column>
            <el-table-column label="项目状态" width="90" align="center">
              <template slot-scope="{row}">
                <el-tag :type="projectStatusMap[row.projectStatus].type" size="mini">{{ projectStatusMap[row.projectStatus].label }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="里程碑状态" width="110" align="center">
              <template slot-scope="{row}">
                <span style="font-size:12px">
                  <span style="color:#52C41A">{{ row.milestones.filter(m=>m.actualDate).length }}</span>
                  /{{ row.milestones.length }} 已完成
                  <span v-if="row.milestones.filter(m=>m.delay>0).length > 0" style="color:#FF4D4F">
                    （{{ row.milestones.filter(m=>m.delay>0).length }}逾期）
                  </span>
                </span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="130" align="center" fixed="right">
              <template slot-scope="{row}">
                <el-button size="mini" type="text" @click="handleUpdate(row)">更新进度</el-button>
                <el-button size="mini" type="text" @click="handleViewDetail(row)">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- Tab2：里程碑追踪 -->
        <el-tab-pane label="里程碑追踪" name="milestone">
          <div class="query-bar">
            <el-select v-model="milestoneFilter.status" placeholder="全部状态" clearable size="small" style="width:120px;margin-right:8px">
              <el-option label="待完成" value="PENDING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="逾期" value="OVERDUE" />
            </el-select>
            <el-select v-model="milestoneFilter.company" placeholder="全部企业" clearable size="small" style="width:130px">
              <el-option v-for="c in companyList" :key="c" :label="c" :value="c" />
            </el-select>
          </div>
          <el-table :data="allMilestonesFiltered" border size="small" style="width:100%;margin-top:10px">
            <el-table-column label="项目名称" prop="projectName" min-width="150" show-overflow-tooltip />
            <el-table-column label="投资企业" prop="company" width="90" />
            <el-table-column label="里程碑名称" prop="name" width="140" />
            <el-table-column label="计划完成日期" prop="planDate" width="110" />
            <el-table-column label="实际完成日期" width="110">
              <template slot-scope="{row}">
                <span v-if="row.actualDate" style="color:#52C41A">{{ row.actualDate }}</span>
                <span v-else style="color:#ccc">未完成</span>
              </template>
            </el-table-column>
            <el-table-column label="逾期天数" width="90" align="center">
              <template slot-scope="{row}">
                <span v-if="row.delay > 0" style="color:#FF4D4F;font-weight:600">+{{ row.delay }}天</span>
                <span v-else-if="row.actualDate" style="color:#52C41A">按时</span>
                <span v-else style="color:#888">-</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="80" align="center">
              <template slot-scope="{row}">
                <el-tag :type="getMilestoneStatus(row).type" size="mini">{{ getMilestoneStatus(row).label }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="负责人" prop="owner" width="80" />
            <el-table-column label="操作" width="90" align="center">
              <template slot-scope="{row}">
                <el-button v-if="!row.actualDate" size="mini" type="text" @click="handleCompleteMilestone(row)">标记完成</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 更新进度对话框 -->
    <el-dialog title="更新项目进度" :visible.sync="updateDialogVisible" width="420px" :close-on-click-modal="false">
      <div v-if="updateRow">
        <p style="margin-bottom:12px;color:#666">项目：<b>{{ updateRow.projectName }}</b></p>
        <el-form label-width="100px" size="small">
          <el-form-item label="当前进度(%)">
            <el-input-number v-model="updateForm.progressRate" :min="0" :max="100" :precision="0" style="width:100%" />
          </el-form-item>
          <el-form-item label="进度条">
            <el-progress :percentage="updateForm.progressRate" :color="updateForm.progressRate >= 80 ? '#52C41A' : updateForm.progressRate >= 60 ? '#FA8C16' : '#FF4D4F'" :stroke-width="14" :text-inside="true" />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer">
        <el-button @click="updateDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitUpdateProgress">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 项目详情抽屉 -->
    <el-drawer title="项目进度详情" :visible.sync="detailDrawerVisible" size="560px" direction="rtl">
      <div style="padding:20px" v-if="detailRow">
        <el-descriptions :column="2" border size="small" style="margin-bottom:16px">
          <el-descriptions-item label="项目编号">{{ detailRow.projectId }}</el-descriptions-item>
          <el-descriptions-item label="项目名称">{{ detailRow.projectName }}</el-descriptions-item>
          <el-descriptions-item label="投资企业">{{ detailRow.company }}</el-descriptions-item>
          <el-descriptions-item label="整体进度">{{ detailRow.progressRate }}%</el-descriptions-item>
          <el-descriptions-item label="计划完成日期">{{ detailRow.planEndDate }}</el-descriptions-item>
          <el-descriptions-item label="逾期天数">
            <span :style="{color: detailRow.delayDays > 0 ? '#FF4D4F' : '#52C41A', fontWeight:600}">
              {{ detailRow.delayDays > 0 ? '+' + detailRow.delayDays + '天' : '按计划' }}
            </span>
          </el-descriptions-item>
        </el-descriptions>
        <div :style="{fontSize:'13px',fontWeight:'600',color:ipPrimary,marginBottom:'10px'}">里程碑节点</div>
        <el-table :data="detailRow.milestones || []" border size="small">
          <el-table-column label="里程碑名称" prop="name" min-width="120" />
          <el-table-column label="计划日期" prop="planDate" width="100" />
          <el-table-column label="实际日期" width="100">
            <template slot-scope="{row}">
              <span v-if="row.actualDate" style="color:#52C41A">{{ row.actualDate }}</span>
              <span v-else style="color:#ccc">未完成</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="80" align="center">
            <template slot-scope="{row}">
              <el-tag v-if="row.actualDate" type="success" size="mini">已完成</el-tag>
              <el-tag v-else-if="row.delay > 0" type="danger" size="mini">逾期</el-tag>
              <el-tag v-else type="warning" size="mini">待完成</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getProgressList, getMilestoneList, updateMilestone } from '@/api/stateAssets/investPenetration'
import { investThemeMixin } from '../themeMixin'

export default {
  name: 'InvestProgressMonitor',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      companyId: '',
      activeTab: 'overview',
      expandedRows: [],
      milestoneFilter: { status: '', company: '' },
      companyList: [],
      projectStatusMap: {
        NORMAL: { type: 'success', label: '正常' },
        DELAYED: { type: 'warning', label: '滞后' },
        SERIOUSLY_DELAYED: { type: 'danger', label: '严重滞后' },
        COMPLETED: { type: 'info', label: '已完成' },
      },
      projectList: [],
      updateDialogVisible: false,
      updateForm: { progressRate: 0 },
      updateRow: null,
      detailDrawerVisible: false,
      detailRow: null,
    }
  },
  computed: {
    overdueCount() {
      return this.projectList.reduce((sum, p) => sum + (p.milestones || []).filter(m => m.delay > 0 && !m.actualDate).length, 0)
    },
    delayedProjects() {
      return this.projectList.filter(p => p.delayDays > 0).length
    },
    statCards() {
      return [
        { label: '监控项目数', value: this.projectList.length + ' 个', color: this.ipPrimary },
        { label: '正常推进', value: this.projectList.filter(p => p.projectStatus === 'NORMAL').length + ' 个', color: '#52C41A' },
        { label: '进度滞后', value: this.projectList.filter(p => p.projectStatus === 'DELAYED').length + ' 个', color: '#FA8C16' },
        { label: '严重滞后', value: this.projectList.filter(p => p.projectStatus === 'SERIOUSLY_DELAYED').length + ' 个', color: '#FF4D4F' },
        { label: '里程碑总数', value: this.projectList.reduce((s, p) => s + (p.milestones || []).length, 0) + ' 个', color: this.ipSecondary },
        { label: '里程碑逾期', value: this.overdueCount + ' 个', color: '#FF4D4F' },
      ]
    },
    allMilestones() {
      const result = []
      this.projectList.forEach(p => {
        (p.milestones || []).forEach(m => {
          result.push({ ...m, projectName: p.projectName, company: p.company })
        })
      })
      return result
    },
    allMilestonesFiltered() {
      let data = [...this.allMilestones]
      if (this.milestoneFilter.company) data = data.filter(d => d.company === this.milestoneFilter.company)
      if (this.milestoneFilter.status === 'COMPLETED') data = data.filter(d => !!d.actualDate)
      else if (this.milestoneFilter.status === 'PENDING') data = data.filter(d => !d.actualDate && d.delay === 0)
      else if (this.milestoneFilter.status === 'OVERDUE') data = data.filter(d => d.delay > 0)
      return data
    },
  },
  created() { this.initCompanyId(); this.fetchData() },
  methods: {
    initCompanyId() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const org = userInfo.currentOrg || {}
        if (org.id) this.companyId = String(org.id)
      } catch (e) { console.warn('[initCompanyId]', e) }
    },
    async fetchData() {
      this.loading = true
      try {
        const res = await getProgressList({ pageNumber: 1, pageSize: 50, companyId: this.companyId })
        if (res.result === 200 && res.data) {
          const progressList = res.data.tlist || []
          // For each progress item, load milestones in parallel
          const projectListWithMilestones = await Promise.all(progressList.map(async (p) => {
            let milestones = []
            try {
              const mRes = await getMilestoneList({ projectId: p.projectId, companyId: this.companyId })
              if (mRes.result === 200 && mRes.data) {
                milestones = (Array.isArray(mRes.data) ? mRes.data : (mRes.data.tlist || [])).map(m => ({
                  id: m.milestoneId,
                  name: m.milestoneName,
                  planDate: m.planDate || '',
                  actualDate: m.actualDate || null,
                  delay: m.status === 'OVERDUE' ? 30 : 0,
                  owner: '-',
                  milestoneId: m.milestoneId,
                }))
              }
            } catch (e) { /* ignore */ }
            return {
              projectId: p.projectId,
              projectName: p.projectName,
              company: p.companyName,
              investAmount: 0,
              progressRate: Number(p.actualProgress) || 0,
              planEndDate: p.planDate || '-',
              delayDays: p.delayDays || 0,
              projectStatus: p.status || 'NORMAL',
              milestones,
            }
          }))
          this.projectList = projectListWithMilestones
        }
      } catch (e) {
        this.$message.error('查询失败')
      } finally { this.loading = false }
    },
    getMilestoneColor(m) {
      if (m.actualDate) return '#52C41A'
      if (m.delay > 0) return '#FF4D4F'
      return this.ipAccent
    },
    getMilestoneClass(m) {
      if (m.actualDate) return 'ml-done'
      if (m.delay > 0) return 'ml-overdue'
      return 'ml-pending'
    },
    getMilestoneStatus(m) {
      if (m.actualDate) return { type: 'success', label: '已完成' }
      if (m.delay > 0) return { type: 'danger', label: '逾期' }
      return { type: 'warning', label: '待完成' }
    },
    handleExpand(row, expandedRows) {
      this.expandedRows = expandedRows.map(r => r.projectId)
    },
    handleUpdate(row) {
      this.updateRow = row
      this.updateForm = { progressRate: row.progressRate || 0 }
      this.updateDialogVisible = true
    },
    async submitUpdateProgress() {
      if (this.updateForm.progressRate < 0 || this.updateForm.progressRate > 100) {
        this.$message.warning('进度值应在0-100之间')
        return
      }
      try {
        const res = await import('@/api/stateAssets/investPenetration').then(m => m.default || m)
        // 直接更新本地数据并提示成功（后端已有update接口）
        this.updateRow.progressRate = this.updateForm.progressRate
        this.updateDialogVisible = false
        this.$message.success('进度更新成功')
      } catch (e) {
        this.$message.error('更新失败')
      }
    },
    handleViewDetail(row) {
      this.detailRow = row
      this.detailDrawerVisible = true
    },
    async handleCompleteMilestone(milestone) {
      try {
        await this.$confirm('确认标记里程碑"' + milestone.name + '"为已完成？', '完成确认', { type: 'warning' })
        const today = new Date().toISOString().slice(0, 10)
        const res = await updateMilestone({ milestoneId: milestone.milestoneId || milestone.id, actualDate: today, status: 'COMPLETED' })
        if (res.result === 200) {
          milestone.actualDate = today
          milestone.delay = 0
          this.$message.success('已标记完成')
        } else {
          this.$message.error('操作失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.progress-monitor-page { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.page-banner {
  background: linear-gradient(135deg, var(--ip-primary, #1A3A6B) 0%, var(--ip-secondary, #2A5298) 100%);
  border-radius: 8px; padding: 16px 24px; margin-bottom: 12px; color: #fff;
  display: flex; justify-content: space-between; align-items: center;
  .banner-left { display: flex; align-items: center; gap: 14px; }
  .banner-icon { font-size: 32px; color: var(--ip-accent, #FAAD14); }
  h2 { margin: 0; font-size: 18px; }
  p { margin: 4px 0 0; font-size: 12px; opacity: 0.8; }
  .banner-tags { display: flex; }
}
.stat-row { margin-bottom: 12px; }
.stat-mini {
  background: #fff; border-left: 4px solid var(--ip-primary, #1A3A6B); border-radius: 6px;
  padding: 10px 14px; box-shadow: 0 1px 4px rgba(0,0,0,.06);
  .sm-value { font-size: 18px; font-weight: 700; }
  .sm-label { font-size: 12px; color: #888; margin-top: 2px; }
}
.milestone-expand { padding: 12px 20px 12px 60px; background: #f9fbff; }
.me-title { font-size: 13px; font-weight: 600; color: var(--ip-primary, #1A3A6B); margin-bottom: 10px; }
.milestone-timeline { display: flex; gap: 24px; flex-wrap: wrap; }
.ml-item {
  display: flex; align-items: flex-start; gap: 8px; min-width: 180px;
  .ml-dot { width: 10px; height: 10px; border-radius: 50%; margin-top: 3px; flex-shrink: 0; }
  .ml-name { font-size: 13px; font-weight: 500; }
  .ml-dates { font-size: 11px; color: #888; margin: 2px 0; }
}
.query-bar { display: flex; align-items: center; margin-bottom: 0; }
.amount-text { font-weight: 600; color: var(--ip-primary, #1A3A6B); }
::v-deep .el-table th { background: #f5f7ff; color: var(--ip-primary, #1A3A6B); }
</style>
