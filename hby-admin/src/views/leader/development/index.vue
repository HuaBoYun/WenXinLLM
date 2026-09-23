<template>
  <div class="leader-development" :style="themeVars">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper"><i class="el-icon-reading"></i></div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalPrograms }}</div>
              <div class="label">培训项目</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper"><i class="el-icon-user"></i></div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.participantCount }}</div>
              <div class="label">参训人数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper"><i class="el-icon-time"></i></div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalHours }}</div>
              <div class="label">培训时长(小时)</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper"><i class="el-icon-trophy"></i></div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.completionRate }}%</div>
              <div class="label">完成率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">负责人发展</span>
        <div class="card-actions">
          <el-button type="primary" icon="el-icon-plus" @click="createProgram">创建培训</el-button>
          <el-button type="success" icon="el-icon-refresh" @click="loadAllData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card" @tab-click="handleTabClick">
        <el-tab-pane v-for="tab in tabConfig" :key="tab.name" :label="tab.label" :name="tab.name">
          <el-table :data="tabData[tab.name]" stripe border size="small" style="width: 100%" v-loading="tabLoading[tab.name]">
            <el-table-column prop="leaderName" label="负责人" min-width="100" />
            <el-table-column prop="enterpriseName" label="所属企业" min-width="160" />
            <el-table-column prop="developmentName" label="培训项目" min-width="180" />
            <el-table-column prop="institution" label="培训机构" min-width="140" />
            <el-table-column prop="startDate" label="开始时间" width="110" />
            <el-table-column prop="endDate" label="结束时间" width="110" />
            <el-table-column prop="duration" label="时长(h)" width="80" />
            <el-table-column prop="progress" label="进度" width="120">
              <template slot-scope="scope">
                <el-progress :percentage="scope.row.progress || 0" :stroke-width="8" />
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="90">
              <template slot-scope="scope">
                <el-tag :type="getStatusType(scope.row.status)" size="small">{{ scope.row.status || '待开始' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" @click="viewDetail(scope.row)">查看</el-button>
                <el-button size="mini" type="primary" @click="editProgram(scope.row)">编辑</el-button>
                <el-button size="mini" type="warning" @click="viewProgress(scope.row)">进度</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination style="margin-top: 15px; text-align: right;" @current-change="(val) => handleTabPageChange(tab.name, val)" :current-page="tabQuery[tab.name].pageNumber" :page-size="tabQuery[tab.name].pageSize" :total="tabTotal[tab.name]" layout="total, prev, pager, next" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
    <!-- 图表分析 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">培训类型分布</div>
          <div ref="typeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">培训完成趋势</div>
          <div ref="progressChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">发展记录</span>
        <div class="search-wrapper">
          <el-input v-model="searchText" placeholder="搜索负责人或培训项目" prefix-icon="el-icon-search" style="width: 300px;" @input="handleSearch" />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%" v-loading="loading">
        <el-table-column prop="leaderName" label="负责人" width="120" />
        <el-table-column prop="company" label="所属企业" width="180" />
        <el-table-column prop="programName" label="培训项目" width="200" />
        <el-table-column prop="trainingType" label="培训类型" width="120" />
        <el-table-column prop="startDate" label="开始时间" width="120" />
        <el-table-column prop="duration" label="培训时长" width="100" />
        <el-table-column prop="progress" label="进度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.progress || 0" :stroke-width="8" />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status || '待开始' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewDetail(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="editProgram(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="viewProgress(scope.row)">进度</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top: 15px; text-align: right;" @current-change="handlePageChange" :current-page="queryForm.pageNumber" :page-size="queryForm.pageSize" :total="total" layout="total, prev, pager, next, jumper" />
    </el-card>
    <!-- 创建/编辑培训弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="formDialogVisible" width="650px" @close="resetForm">
      <el-form ref="programForm" :model="formData" :rules="formRules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="负责人" prop="leaderName"><el-input v-model="formData.leaderName" placeholder="请输入负责人姓名" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="所属企业" prop="enterpriseName"><el-input v-model="formData.enterpriseName" placeholder="请输入所属企业" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="培训类型" prop="developmentType"><el-select v-model="formData.developmentType" placeholder="请选择" style="width:100%;"><el-option v-for="t in typeOptions" :key="t" :label="t" :value="t" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="培训项目" prop="developmentName"><el-input v-model="formData.developmentName" placeholder="请输入培训项目名称" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="培训机构"><el-input v-model="formData.institution" placeholder="请输入培训机构" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="培训时长"><el-input-number v-model="formData.duration" :min="0" style="width:100%;" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="开始日期"><el-date-picker v-model="formData.startDate" type="date" value-format="yyyy-MM-dd" style="width:100%;" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="结束日期"><el-date-picker v-model="formData.endDate" type="date" value-format="yyyy-MM-dd" style="width:100%;" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="培训描述"><el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入培训描述" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="formDialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button></div>
    </el-dialog>
    <!-- 查看详情弹窗 -->
    <el-dialog title="培训详情" :visible.sync="detailDialogVisible" width="600px">
      <el-descriptions :column="2" border size="medium">
        <el-descriptions-item label="负责人">{{ detailData.leaderName }}</el-descriptions-item>
        <el-descriptions-item label="所属企业">{{ detailData.enterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="培训类型">{{ detailData.developmentType }}</el-descriptions-item>
        <el-descriptions-item label="培训项目">{{ detailData.developmentName }}</el-descriptions-item>
        <el-descriptions-item label="培训机构">{{ detailData.institution }}</el-descriptions-item>
        <el-descriptions-item label="培训时长">{{ detailData.duration }} 小时</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ detailData.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ detailData.endDate }}</el-descriptions-item>
        <el-descriptions-item label="进度"><el-progress :percentage="detailData.progress || 0" :stroke-width="10" /></el-descriptions-item>
        <el-descriptions-item label="状态"><el-tag :type="getStatusType(detailData.status)">{{ detailData.status || '待开始' }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="成果">{{ detailData.achievement || '-' }}</el-descriptions-item>
        <el-descriptions-item label="描述">{{ detailData.description || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer"><el-button @click="detailDialogVisible = false">关闭</el-button></div>
    </el-dialog>
    <!-- 进度管理弹窗 -->
    <el-dialog title="进度管理" :visible.sync="progressDialogVisible" width="500px">
      <el-form label-width="80px">
        <el-form-item label="当前进度"><el-slider v-model="progressForm.progress" :step="5" show-stops /></el-form-item>
        <el-form-item label="成绩"><el-input-number v-model="progressForm.score" :min="0" :max="100" style="width:100%;" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="progressForm.status" style="width:100%;">
            <el-option label="待开始" value="待开始" />
            <el-option label="进行中" value="进行中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已暂停" value="已暂停" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="progressDialogVisible = false">取消</el-button><el-button type="primary" :loading="progressSubmitLoading" @click="submitProgress">保存</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getLeaderDevelopmentList,
  getLeaderDevelopmentTabList,
  getLeaderDevelopmentDetail,
  addLeaderDevelopment,
  updateLeaderDevelopment,
  updateLeaderDevelopmentProgress,
  getLeaderDevelopmentStatistics,
  getLeaderDevelopmentTypeChart,
  getLeaderDevelopmentTrendChart
} from '@/api/leader/index'
import * as echarts from 'echarts'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'LeaderDevelopment',
  mixins: [investThemeMixin],
  data() {
    return {
      activeTab: 'training',
      searchText: '',
      loading: false,
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 15 },
      overviewData: { totalPrograms: 0, participantCount: 0, totalHours: 0, completionRate: 0 },
      tableData: [],
      // Tab配置
      tabConfig: [
        { name: 'training', label: '培训计划', type: '培训计划' },
        { name: 'capability', label: '能力提升', type: '能力提升' },
        { name: 'career', label: '职业发展', type: '职业发展' },
        { name: 'mentorship', label: '导师制度', type: '导师制度' },
        { name: 'resources', label: '学习资源', type: '学习资源' },
        { name: 'evaluation', label: '效果评估', type: '效果评估' }
      ],
      tabData: { training: [], capability: [], career: [], mentorship: [], resources: [], evaluation: [] },
      tabLoading: { training: false, capability: false, career: false, mentorship: false, resources: false, evaluation: false },
      tabQuery: {
        training: { pageNumber: 1, pageSize: 10 }, capability: { pageNumber: 1, pageSize: 10 },
        career: { pageNumber: 1, pageSize: 10 }, mentorship: { pageNumber: 1, pageSize: 10 },
        resources: { pageNumber: 1, pageSize: 10 }, evaluation: { pageNumber: 1, pageSize: 10 }
      },
      tabTotal: { training: 0, capability: 0, career: 0, mentorship: 0, resources: 0, evaluation: 0 },
      // 弹窗
      formDialogVisible: false,
      detailDialogVisible: false,
      progressDialogVisible: false,
      dialogTitle: '创建培训',
      submitLoading: false,
      progressSubmitLoading: false,
      isEdit: false,
      formData: { leaderName: '', enterpriseName: '', developmentType: '', developmentName: '', institution: '', duration: 0, startDate: '', endDate: '', description: '' },
      formRules: {
        leaderName: [{ required: true, message: '请输入负责人', trigger: 'blur' }],
        developmentType: [{ required: true, message: '请选择培训类型', trigger: 'change' }],
        developmentName: [{ required: true, message: '请输入培训项目', trigger: 'blur' }]
      },
      detailData: {},
      progressForm: { id: '', progress: 0, score: 0, status: '进行中' },
      typeOptions: ['培训计划', '能力提升', '职业发展', '导师制度', '学习资源', '效果评估'],
      // 图表实例
      typeChartInstance: null,
      progressChartInstance: null
    }
  },
  mounted() {
    this.loadAllData()
  },
  watch: {
    ipBright() {
      this.$nextTick(() => this.loadCharts())
    },
  },
  beforeDestroy() {
    if (this.typeChartInstance) this.typeChartInstance.dispose()
    if (this.progressChartInstance) this.progressChartInstance.dispose()
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    loadAllData() {
      this.loadData()
      this.loadStatistics()
      this.loadTabData(this.activeTab)
      this.loadCharts()
    },
    async loadData() {
      this.loading = true
      try {
        const params = { ...this.queryForm, searchText: this.searchText }
        const res = await getLeaderDevelopmentList(params)
        if (res && res.data) {
          this.tableData = res.data.tlist || []
          this.total = res.data.totalRecord || 0
        }
      } catch (e) {
        console.error('加载数据失败', e)
      } finally {
        this.loading = false
      }
    },
    async loadStatistics() {
      try {
        const res = await getLeaderDevelopmentStatistics()
        if (res && res.data) {
          this.overviewData = res.data
        }
      } catch (e) {
        console.error('加载统计失败', e)
      }
    },
    async loadTabData(tabName) {
      const tab = this.tabConfig.find(t => t.name === tabName)
      if (!tab) return
      this.tabLoading[tabName] = true
      try {
        const params = { ...this.tabQuery[tabName], tabType: tab.type }
        const res = await getLeaderDevelopmentTabList(params)
        if (res && res.data) {
          this.tabData[tabName] = res.data.tlist || []
          this.tabTotal[tabName] = res.data.totalRecord || 0
        }
      } catch (e) {
        console.error('加载Tab数据失败', e)
      } finally {
        this.tabLoading[tabName] = false
      }
    },
    async loadCharts() {
      await this.$nextTick()
      this.loadTypeChart()
      this.loadProgressChart()
      window.addEventListener('resize', this.handleResize)
    },
    async loadTypeChart() {
      try {
        const res = await getLeaderDevelopmentTypeChart()
        const dom = this.$refs.typeChart
        if (!dom) return
        if (!this.typeChartInstance) this.typeChartInstance = echarts.init(dom)
        const data = (res && res.data) ? res.data : []
        this.typeChartInstance.setOption({
          tooltip: { trigger: 'item', formatter: '{b}: {c}人 ({d}%)' },
          legend: { bottom: 10, left: 'center' },
          series: [{ type: 'pie', radius: ['35%', '60%'], center: ['50%', '45%'], label: { formatter: '{b}\n{c}人' }, data: data }]
        })
      } catch (e) {
        console.error('加载图表失败', e)
      }
    },
    async loadProgressChart() {
      try {
        const res = await getLeaderDevelopmentTrendChart()
        const dom = this.$refs.progressChart
        if (!dom) return
        if (!this.progressChartInstance) this.progressChartInstance = echarts.init(dom)
        const chartData = (res && res.data) ? res.data : { months: [], totalData: [], completedData: [] }
        this.progressChartInstance.setOption({
          tooltip: { trigger: 'axis' },
          legend: { data: ['培训总数', '已完成'], top: 5 },
          grid: { left: 50, right: 20, bottom: 30, top: 45 },
          xAxis: { type: 'category', data: chartData.months || [] },
          yAxis: { type: 'value', name: '数量' },
          series: [
            { name: '培训总数', type: 'bar', data: chartData.totalData || [], itemStyle: { color: this.ipBright } },
            { name: '已完成', type: 'bar', data: chartData.completedData || [], itemStyle: { color: '#52C41A' } }
          ]
        })
      } catch (e) {
        console.error('加载趋势图失败', e)
      }
    },
    handleResize() {
      if (this.typeChartInstance) this.typeChartInstance.resize()
      if (this.progressChartInstance) this.progressChartInstance.resize()
    },
    handleTabClick(tab) {
      this.loadTabData(tab.name)
    },
    handleTabPageChange(tabName, page) {
      this.tabQuery[tabName].pageNumber = page
      this.loadTabData(tabName)
    },
    handlePageChange(page) {
      this.queryForm.pageNumber = page
      this.loadData()
    },
    handleSearch() {
      this.queryForm.pageNumber = 1
      this.loadData()
    },
    createProgram() {
      this.isEdit = false
      this.dialogTitle = '创建培训'
      this.formData = { leaderName: '', enterpriseName: '', developmentType: '', developmentName: '', institution: '', duration: 0, startDate: '', endDate: '', description: '' }
      this.formDialogVisible = true
    },
    editProgram(row) {
      this.isEdit = true
      this.dialogTitle = '编辑培训'
      this.formData = { ...row }
      this.formDialogVisible = true
    },
    async submitForm() {
      this.$refs.programForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          if (this.isEdit) {
            await updateLeaderDevelopment(this.formData)
            this.$message.success('更新成功')
          } else {
            await addLeaderDevelopment(this.formData)
            this.$message.success('创建成功')
          }
          this.formDialogVisible = false
          this.loadAllData()
        } catch (e) {
          this.$message.error('操作失败')
        } finally {
          this.submitLoading = false
        }
      })
    },
    resetForm() {
      if (this.$refs.programForm) this.$refs.programForm.resetFields()
    },
    async viewDetail(row) {
      try {
        const res = await getLeaderDevelopmentDetail(row.id)
        if (res && res.data) {
          this.detailData = res.data
        } else {
          this.detailData = row
        }
        this.detailDialogVisible = true
      } catch (e) {
        this.detailData = row
        this.detailDialogVisible = true
      }
    },
    viewProgress(row) {
      this.progressForm = { id: row.id, progress: row.progress || 0, score: row.score || 0, status: row.status || '进行中' }
      this.progressDialogVisible = true
    },
    async submitProgress() {
      this.progressSubmitLoading = true
      try {
        await updateLeaderDevelopmentProgress(this.progressForm)
        this.$message.success('进度更新成功')
        this.progressDialogVisible = false
        this.loadAllData()
      } catch (e) {
        this.$message.error('更新失败')
      } finally {
        this.progressSubmitLoading = false
      }
    },
    getScoreType(score) {
      if (score >= 90) return 'success'
      if (score >= 80) return 'warning'
      if (score >= 70) return 'info'
      return 'danger'
    },
    getStatusType(status) {
      const map = { '进行中': 'warning', '已完成': 'success', '待开始': 'info', '已暂停': 'danger' }
      return map[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.leader-development {
  padding: 20px;
  .overview-cards {
    margin-bottom: 20px;
    .overview-card {
      height: 120px;
      &.gradient-theme {
        background: linear-gradient(135deg, var(--ip-secondary) 0%, var(--ip-bright) 100%);
        color: #fff;
        .card-content {
          display: flex;
          align-items: center;
          height: 100%;
          .icon-wrapper { font-size: 40px; margin-right: 15px; opacity: 0.8; }
          .data-wrapper {
            .number { font-size: 28px; font-weight: bold; line-height: 1; }
            .label { font-size: 14px; margin-top: 5px; opacity: 0.8; }
          }
        }
      }
    }
  }
  .function-card, .chart-section, .data-card { margin-bottom: 20px; }
  .chart-section { .chart-card { height: 380px; } }
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .card-title { font-size: 16px; font-weight: bold; }
  }
  .search-wrapper { display: flex; align-items: center; }
  ::v-deep .el-tabs__content { padding: 0; }
}
</style>