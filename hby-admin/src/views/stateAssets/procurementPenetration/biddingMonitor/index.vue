<template>
  <div class="app-container procurement-page" :style="themeVars">
    <!-- Header -->
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-s-order"></i><span>招标过程监控</span></div>
      <div class="page-header-desc">监控招标项目流程、投标人数与围标串标风险</div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="12" class="stat-row">
      <el-col :span="6" v-for="s in stats" :key="s.label">
        <div class="stat-card" :class="s.cls">
          <div class="stat-icon"><i :class="s.icon"></i></div>
          <div class="stat-body">
            <div class="stat-value">{{ s.value }}</div>
            <div class="stat-label">{{ s.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 筛选 -->
    <el-card shadow="never" class="filter-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="项目名称"><el-input v-model="queryForm.projectName" placeholder="请输入" clearable style="width:180px" /></el-form-item>
        <el-form-item label="招标方式">
          <el-select v-model="queryForm.biddingType" placeholder="请选择" clearable style="width:140px">
            <el-option label="公开招标" value="PUBLIC" /><el-option label="邀请招标" value="INVITED" />
            <el-option label="竞争性谈判" value="NEGOTIATION" /><el-option label="单一来源" value="SOLE_SOURCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="围标风险">
          <el-select v-model="queryForm.collusionRisk" placeholder="请选择" clearable style="width:120px">
            <el-option label="高" value="HIGH" /><el-option label="中" value="MEDIUM" /><el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker v-model="queryForm.dateRange" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" value-format="yyyy-MM-dd" style="width:220px" size="small" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 项目列表 -->
    <el-card shadow="never" class="table-card">
      <el-table v-loading="loading" :data="list" border style="width:100%" :row-class-name="rowClassName">
        <el-table-column label="项目名称" prop="projectName" min-width="160" show-overflow-tooltip />
        <el-table-column label="招标方式" width="110" align="center">
          <template slot-scope="{ row }">{{ { PUBLIC: '公开招标', INVITED: '邀请招标', NEGOTIATION: '竞争性谈判', SOLE_SOURCE: '单一来源' }[row.biddingType] || row.biddingType }}</template>
        </el-table-column>
        <el-table-column label="预算金额(万元)" prop="budgetAmount" width="130" align="right" />
        <el-table-column label="投标人数" prop="bidderCount" width="80" align="center">
          <template slot-scope="{ row }">
            <span :style="row.bidderCount < 3 ? 'color:#e6a23c;font-weight:600' : ''">{{ row.bidderCount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="文件相似度" width="160" align="center">
          <template slot-scope="{ row }">
            <el-progress :percentage="row.fileSimilarity || 0" :color="row.fileSimilarity > 80 ? '#f56c6c' : row.fileSimilarity > 60 ? '#e6a23c' : '#67c23a'" :stroke-width="10" />
          </template>
        </el-table-column>
        <el-table-column label="围标风险" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ HIGH:'danger', MEDIUM:'warning', LOW:'success' }[row.collusionRisk]" size="small">
              {{ { HIGH:'高', MEDIUM:'中', LOW:'低' }[row.collusionRisk] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-button size="mini" type="text" @click="handleDetail(row)">详情</el-button>
            <el-divider direction="vertical" />
            <el-button size="mini" type="text" style="color:#e6a23c" @click="handleDispatch(row)">派单</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top:12px;text-align:right" :current-page="queryForm.pageNumber" :page-sizes="[10,20,50]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="v => { queryForm.pageSize = v; fetchData() }" @current-change="v => { queryForm.pageNumber = v; fetchData() }" />
    </el-card>

    <!-- 下方两列：关联关系 + 预警记录 -->
    <el-row :gutter="14" style="margin-top:14px">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header"><i class="el-icon-share" style="margin-right:6px"></i>投标人关联关系分析</div>
          <el-table v-loading="relationLoading" :data="relationList" border size="small" style="width:100%">
            <el-table-column label="投标人A" prop="bidderA" min-width="130" show-overflow-tooltip />
            <el-table-column label="投标人B" prop="bidderB" min-width="130" show-overflow-tooltip />
            <el-table-column label="关联类型" prop="relationType" width="110" align="center" />
            <el-table-column label="关联强度" width="100" align="center">
              <template slot-scope="{ row }">
                <el-tag :type="row.strength === '强' ? 'danger' : row.strength === '中' ? 'warning' : 'info'" size="small">{{ row.strength }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header"><i class="el-icon-bell" style="margin-right:6px"></i>近期预警记录</div>
          <el-timeline>
            <el-timeline-item v-for="(w, i) in warningList" :key="i" :color="w.color" :timestamp="w.time" placement="top">
              <el-tag :type="w.type" size="mini">{{ w.level }}</el-tag>
              <span style="margin-left:8px;font-size:13px">{{ w.content }}</span>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详情抽屉 -->
    <el-drawer title="招标项目详情" :visible.sync="drawerVisible" size="600px" direction="rtl">
      <div v-if="currentRow" style="padding:0 20px 20px">
        <el-divider content-position="left">基本信息</el-divider>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="项目名称">{{ currentRow.projectName }}</el-descriptions-item>
          <el-descriptions-item label="招标方式">{{ { PUBLIC: '公开招标', INVITED: '邀请招标', NEGOTIATION: '竞争性谈判', SOLE_SOURCE: '单一来源' }[currentRow.biddingType] || currentRow.biddingType }}</el-descriptions-item>
          <el-descriptions-item label="预算金额">{{ currentRow.budgetAmount }} 万元</el-descriptions-item>
          <el-descriptions-item label="投标人数">{{ currentRow.bidderCount }}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="left">文件相似度分析</el-divider>
        <el-alert :title="`投标文件相似度：${currentRow.fileSimilarity || 0}%，${currentRow.fileSimilarity > 80 ? '存在围标串标高风险！' : '风险可控'}`"
          :type="currentRow.fileSimilarity > 80 ? 'error' : 'success'" show-icon :closable="false" />
        <el-divider content-position="left">报价异常分析</el-divider>
        <p style="font-size:13px;color:#666;padding:0 4px">{{ currentRow.priceAnalysis || '暂无分析数据' }}</p>
        <el-divider content-position="left">处置建议</el-divider>
        <el-alert :title="currentRow.suggestion || '暂无建议'" type="warning" show-icon :closable="false" />
      </div>
    </el-drawer>

    <!-- 派单对话框 -->
    <el-dialog title="围标串标核查派单" :visible.sync="dispatchVisible" width="460px">
      <el-form :model="dispatchForm" label-width="100px" size="small">
        <el-form-item label="核查责任人">
          <el-select v-model="dispatchForm.owner" placeholder="请选择" style="width:100%">
            <el-option label="张伟" value="张伟" />
            <el-option label="李明" value="李明" />
            <el-option label="王芳" value="王芳" />
            <el-option label="刘洋" value="刘洋" />
            <el-option label="陈静" value="陈静" />
          </el-select>
        </el-form-item>
        <el-form-item label="核查期限">
          <el-date-picker v-model="dispatchForm.deadline" type="date" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="核查要点"><el-input v-model="dispatchForm.keyPoints" type="textarea" :rows="3" placeholder="请输入核查要点" /></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dispatchVisible = false">取消</el-button>
        <el-button type="primary" :loading="dispatchLoading" @click="submitDispatch">确认派单</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getBiddingMonitorList, getBiddingMonitorDetail, dispatchBiddingMonitor, getBidderRelationList } from '@/api/stateAssets/procurementPenetration'
import { getProcurementWarnings } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'ProcurementBiddingMonitor',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      relationLoading: false,
      dispatchLoading: false,
      list: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, projectName: '', biddingType: '', collusionRisk: '', dateRange: null },
      drawerVisible: false,
      currentRow: null,
      dispatchVisible: false,
      dispatchForm: { owner: '', deadline: '', keyPoints: '' },
      stats: [
        { label: '招标项目总数', value: 0, icon: 'el-icon-s-order', cls: 'card-blue' },
        { label: '高风险项目数', value: 0, icon: 'el-icon-warning', cls: 'card-red' },
        { label: '围标串标疑似数', value: 0, icon: 'el-icon-connection', cls: 'card-orange' },
        { label: '本月预警数', value: 0, icon: 'el-icon-bell', cls: 'card-yellow' },
      ],
      relationList: [],
      warningList: [],
    }
  },
  created() {
    this.fetchData()
    this.fetchRelationList()
    this.fetchWarnings()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getBiddingMonitorList(this.queryForm)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
          this.total = (res.data && res.data.totalRecord) || 0
          this.updateStats()
        } else {
          this.list = []
          this.total = 0
        }
      } catch (e) {
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    updateStats() {
      const highRisk = this.list.filter(r => r.collusionRisk === 'HIGH').length
      const mediumRisk = this.list.filter(r => r.collusionRisk === 'MEDIUM').length
      this.stats = [
        { label: '招标项目总数', value: this.total, icon: 'el-icon-s-order', cls: 'card-blue' },
        { label: '高风险项目数', value: highRisk, icon: 'el-icon-warning', cls: 'card-red' },
        { label: '围标串标疑似数', value: highRisk + mediumRisk, icon: 'el-icon-connection', cls: 'card-orange' },
        { label: '本月预警数', value: this.warningList.length, icon: 'el-icon-bell', cls: 'card-yellow' },
      ]
    },
    async fetchRelationList() {
      this.relationLoading = true
      try {
        const res = await getBidderRelationList({ pageNumber: 1, pageSize: 20 })
        if (res && res.result === 200) {
          this.relationList = (res.data && res.data.tlist) || []
        }
      } catch (e) {
        this.relationList = []
      } finally {
        this.relationLoading = false
      }
    },
    async fetchWarnings() {
      try {
        const res = await getProcurementWarnings()
        if (res && res.result === 200) {
          const warnings = (res.data && (Array.isArray(res.data) ? res.data : res.data.tlist)) || []
          this.warningList = warnings.map(w => ({
            time: w.warningTime || w.createTime || '',
            level: w.level === 'HIGH' ? '红色预警' : '橙色预警',
            content: w.content || w.warningContent || '',
            type: w.level === 'HIGH' ? 'danger' : 'warning',
            color: w.level === 'HIGH' ? '#ff4d4f' : '#fa8c16',
          }))
          this.stats[3].value = this.warningList.length
        }
      } catch (e) {
        this.warningList = []
      }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() {
      this.queryForm = { pageNumber: 1, pageSize: 10, projectName: '', biddingType: '', collusionRisk: '', dateRange: null }
      this.fetchData()
    },
    rowClassName({ row }) {
      if (row.fileSimilarity > 80) return 'row-danger'
      if (row.bidderCount < 3) return 'row-warn'
      return ''
    },
    async handleDetail(row) {
      try {
        const res = await getBiddingMonitorDetail(row.id)
        if (res && res.result === 200 && res.data) {
          this.currentRow = res.data
        } else {
          this.currentRow = row
        }
      } catch (e) {
        this.currentRow = row
      }
      this.drawerVisible = true
    },
    handleDispatch(row) {
      this.currentRow = row
      this.dispatchForm = { owner: '', deadline: '', keyPoints: '' }
      this.dispatchVisible = true
    },
    async submitDispatch() {
      if (!this.dispatchForm.owner || !this.dispatchForm.deadline) {
        this.$message.warning('请填写完整派单信息')
        return
      }
      this.dispatchLoading = true
      try {
        const res = await dispatchBiddingMonitor({
          projectId: this.currentRow.id,
          ...this.dispatchForm,
        })
        if (res && res.result === 200) {
          this.$message.success('核查派单已提交')
          this.dispatchVisible = false
          this.fetchData()
        } else {
          this.$message.error(res.msg || '派单失败')
        }
      } catch (e) {
        this.$message.error('派单失败')
      } finally {
        this.dispatchLoading = false
      }
    },
  },
}
</script>
<style lang="scss" scoped>
.procurement-page { padding:16px; background:#f0f2f5; min-height:calc(100vh - 84px); }
.page-header { display:flex; align-items:center; justify-content:space-between; margin-bottom:14px; padding:14px 20px; background:linear-gradient(135deg,var(--ip-primary, #003A6C) 0%,var(--ip-secondary, #0050A0) 60%,var(--ip-bright, #1677FF) 100%); border-radius:6px; color:#fff;
  .page-header-left { display:flex; align-items:center; font-size:16px; font-weight:600; i { font-size:22px; margin-right:10px; } }
  .page-header-desc { font-size:13px; opacity:.85; }
}
.stat-row { margin-bottom:14px; }
.stat-card { display:flex; align-items:center; padding:16px; border-radius:8px; background:#fff; box-shadow:0 1px 4px rgba(0,0,0,.08);
  .stat-icon { font-size:30px; padding:10px; border-radius:8px; margin-right:12px; }
  .stat-value { font-size:26px; font-weight:bold; }
  .stat-label { font-size:12px; color:#909399; margin-top:2px; }
  &.card-blue  { .stat-icon { color:#1890ff; background:#e6f7ff; } .stat-value { color:#1890ff; } }
  &.card-red   { .stat-icon { color:#ff4d4f; background:#fff1f0; } .stat-value { color:#ff4d4f; } }
  &.card-orange{ .stat-icon { color:#fa8c16; background:#fff7e6; } .stat-value { color:#fa8c16; } }
  &.card-yellow{ .stat-icon { color:#faad14; background:#fffbe6; } .stat-value { color:#faad14; } }
}
.filter-card { margin-bottom:12px; }
.table-card { margin-bottom:14px; }
.card-header { font-size:14px; font-weight:600; color:#303133; }
::v-deep .row-danger td { background:#fff1f0 !important; }
::v-deep .row-warn td { background:#fff7e6 !important; }
::v-deep .el-table th { background:var(--ip-light-bg, #EBF1FF) !important; color:var(--ip-secondary, #0050A0); font-weight:600; }
::v-deep .el-card { border-radius:6px; }
</style>
