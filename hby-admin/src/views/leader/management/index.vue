<template>
  <div class="leader-management" :style="themeVars">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper"><i class="el-icon-user-solid"></i></div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalLeaders }}</div>
              <div class="label">负责人总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper"><i class="el-icon-office-building"></i></div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.activeLeaders }}</div>
              <div class="label">在职负责人</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper"><i class="el-icon-star-on"></i></div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.avgScore }}</div>
              <div class="label">平均考核分数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper"><i class="el-icon-trophy"></i></div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.excellentCount }}</div>
              <div class="label">优秀负责人</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 - 6个Tab -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">负责人信息管理</span>
        <div class="card-actions">
          <el-button type="primary" icon="el-icon-plus" @click="handleAddLeader">新增负责人</el-button>
          <el-button type="success" icon="el-icon-refresh" @click="refreshAllData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card" @tab-click="handleTabClick">
        <el-tab-pane label="基本信息管理" name="basic">
          <el-table :data="tabData.basic" stripe border size="small" style="width: 100%" v-loading="tabLoading.basic">
            <el-table-column prop="leaderName" label="姓名" min-width="100" />
            <el-table-column prop="enterpriseName" label="所属企业" min-width="180" />
            <el-table-column prop="position" label="职务" min-width="120" />
            <el-table-column prop="gender" label="性别" width="60" />
            <el-table-column prop="education" label="学历" width="80" />
            <el-table-column prop="politicalStatus" label="政治面貌" min-width="100" />
            <el-table-column prop="phone" label="联系电话" min-width="130" />
            <el-table-column prop="status" label="状态" width="80">
              <template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)" size="mini">{{ scope.row.status }}</el-tag></template>
            </el-table-column>
            <el-table-column prop="appointDate" label="任命日期" min-width="110" />
          </el-table>
          <el-pagination class="tab-pagination" :current-page="tabPage.basic.pageNumber" :page-size="tabPage.basic.pageSize" :total="tabPage.basic.total" layout="total, prev, pager, next" @current-change="(val) => handleTabPageChange('basic', val)" />
        </el-tab-pane>
        <el-tab-pane label="履历管理" name="resume">
          <el-table :data="tabData.resume" stripe border size="small" style="width: 100%" v-loading="tabLoading.resume">
            <el-table-column prop="leaderName" label="负责人" min-width="100" />
            <el-table-column prop="enterpriseName" label="所属企业" min-width="160" />
            <el-table-column prop="resumeType" label="履历类型" width="100"><template slot-scope="scope"><el-tag size="mini">{{ scope.row.resumeType }}</el-tag></template></el-table-column>
            <el-table-column prop="organization" label="单位/学校" min-width="180" />
            <el-table-column prop="positionHeld" label="职务/专业" min-width="140" />
            <el-table-column prop="startDate" label="开始时间" min-width="110" />
            <el-table-column prop="endDate" label="结束时间" min-width="110" />
            <el-table-column prop="description" label="描述" min-width="150" show-overflow-tooltip />
          </el-table>
          <el-pagination class="tab-pagination" :current-page="tabPage.resume.pageNumber" :page-size="tabPage.resume.pageSize" :total="tabPage.resume.total" layout="total, prev, pager, next" @current-change="(val) => handleTabPageChange('resume', val)" />
        </el-tab-pane>
        <el-tab-pane label="任职情况" name="position">
          <el-table :data="tabData.position" stripe border size="small" style="width: 100%" v-loading="tabLoading.position">
            <el-table-column prop="leaderName" label="负责人" min-width="100" />
            <el-table-column prop="enterpriseName" label="所属企业" min-width="160" />
            <el-table-column prop="positionName" label="职务名称" min-width="120" />
            <el-table-column prop="positionLevel" label="职务级别" width="80" />
            <el-table-column prop="department" label="所在部门" min-width="120" />
            <el-table-column prop="appointDate" label="任命日期" min-width="110" />
            <el-table-column prop="leaveDate" label="离任日期" min-width="110" />
            <el-table-column prop="status" label="状态" width="80"><template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)" size="mini">{{ scope.row.status }}</el-tag></template></el-table-column>
            <el-table-column prop="responsibility" label="职责范围" min-width="150" show-overflow-tooltip />
          </el-table>
          <el-pagination class="tab-pagination" :current-page="tabPage.position.pageNumber" :page-size="tabPage.position.pageSize" :total="tabPage.position.total" layout="total, prev, pager, next" @current-change="(val) => handleTabPageChange('position', val)" />
        </el-tab-pane>
        <el-tab-pane label="资质证书" name="certificate">
          <el-table :data="tabData.certificate" stripe border size="small" style="width: 100%" v-loading="tabLoading.certificate">
            <el-table-column prop="leaderName" label="负责人" min-width="100" />
            <el-table-column prop="enterpriseName" label="所属企业" min-width="160" />
            <el-table-column prop="certName" label="证书名称" min-width="160" />
            <el-table-column prop="certType" label="证书类型" width="100"><template slot-scope="scope"><el-tag size="mini">{{ scope.row.certType }}</el-tag></template></el-table-column>
            <el-table-column prop="certNo" label="证书编号" min-width="140" />
            <el-table-column prop="issueOrg" label="发证机构" min-width="160" />
            <el-table-column prop="issueDate" label="发证日期" min-width="110" />
            <el-table-column prop="expireDate" label="到期日期" min-width="110" />
            <el-table-column prop="status" label="状态" width="80"><template slot-scope="scope"><el-tag :type="getCertStatusType(scope.row.status)" size="mini">{{ scope.row.status }}</el-tag></template></el-table-column>
          </el-table>
          <el-pagination class="tab-pagination" :current-page="tabPage.certificate.pageNumber" :page-size="tabPage.certificate.pageSize" :total="tabPage.certificate.total" layout="total, prev, pager, next" @current-change="(val) => handleTabPageChange('certificate', val)" />
        </el-tab-pane>
        <el-tab-pane label="档案管理" name="archive">
          <el-table :data="tabData.archive" stripe border size="small" style="width: 100%" v-loading="tabLoading.archive">
            <el-table-column prop="leaderName" label="负责人" min-width="100" />
            <el-table-column prop="enterpriseName" label="所属企业" min-width="160" />
            <el-table-column prop="archiveType" label="档案类型" width="100"><template slot-scope="scope"><el-tag size="mini">{{ scope.row.archiveType }}</el-tag></template></el-table-column>
            <el-table-column prop="archiveNo" label="档案编号" min-width="130" />
            <el-table-column prop="archiveTitle" label="档案标题" min-width="200" show-overflow-tooltip />
            <el-table-column prop="fileDate" label="归档日期" min-width="110" />
            <el-table-column prop="keeper" label="保管人" min-width="100" />
            <el-table-column prop="status" label="状态" width="80"><template slot-scope="scope"><el-tag :type="getArchiveStatusType(scope.row.status)" size="mini">{{ scope.row.status }}</el-tag></template></el-table-column>
          </el-table>
          <el-pagination class="tab-pagination" :current-page="tabPage.archive.pageNumber" :page-size="tabPage.archive.pageSize" :total="tabPage.archive.total" layout="total, prev, pager, next" @current-change="(val) => handleTabPageChange('archive', val)" />
        </el-tab-pane>
        <el-tab-pane label="变更记录" name="change">
          <el-table :data="tabData.change" stripe border size="small" style="width: 100%" v-loading="tabLoading.change">
            <el-table-column prop="leaderName" label="负责人" min-width="100" />
            <el-table-column prop="enterpriseName" label="所属企业" min-width="150" />
            <el-table-column prop="changeType" label="变更类型" width="100"><template slot-scope="scope"><el-tag size="mini" type="warning">{{ scope.row.changeType }}</el-tag></template></el-table-column>
            <el-table-column prop="changeField" label="变更字段" min-width="100" />
            <el-table-column prop="oldValue" label="变更前" min-width="140" show-overflow-tooltip />
            <el-table-column prop="newValue" label="变更后" min-width="140" show-overflow-tooltip />
            <el-table-column prop="changeReason" label="变更原因" min-width="150" show-overflow-tooltip />
            <el-table-column prop="changeDate" label="变更日期" min-width="110" />
            <el-table-column prop="operator" label="操作人" min-width="100" />
          </el-table>
          <el-pagination class="tab-pagination" :current-page="tabPage.change.pageNumber" :page-size="tabPage.change.pageSize" :total="tabPage.change.total" layout="total, prev, pager, next" @current-change="(val) => handleTabPageChange('change', val)" />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">负责人信息列表</span>
        <div class="search-wrapper">
          <el-input v-model="searchText" placeholder="搜索负责人姓名或企业" prefix-icon="el-icon-search" style="width: 300px;" @input="handleSearch" clearable />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%" v-loading="loading">
        <el-table-column prop="leaderName" label="姓名" min-width="100" />
        <el-table-column prop="position" label="职务" min-width="130" />
        <el-table-column prop="enterpriseName" label="所属企业" min-width="180" />
        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column prop="education" label="学历" width="80" />
        <el-table-column prop="phone" label="联系电话" min-width="130" />
        <el-table-column prop="appointDate" label="任职时间" min-width="110" />
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)" size="mini">{{ scope.row.status }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewDetail(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="editLeader(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="openEvaluation(scope.row)">考核</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination class="main-pagination" :current-page="queryForm.pageNumber" :page-size="queryForm.pageSize" :total="total" :page-sizes="[10, 15, 20, 50]" layout="total, sizes, prev, pager, next, jumper" @current-change="handlePageChange" @size-change="handleSizeChange" />
    </el-card>

    <!-- 新增/编辑/查看 对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form ref="leaderForm" :model="form" :rules="formRules" label-width="100px" :disabled="dialogType === 'view'">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="leaderName"><el-input v-model="form.leaderName" placeholder="请输入姓名" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择"><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属企业" prop="enterpriseName"><el-input v-model="form.enterpriseName" placeholder="请输入企业名称" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职务" prop="position"><el-input v-model="form.position" placeholder="请输入职务" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学历" prop="education">
              <el-select v-model="form.education" placeholder="请选择"><el-option label="博士" value="博士" /><el-option label="硕士" value="硕士" /><el-option label="本科" value="本科" /><el-option label="大专" value="大专" /></el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业" prop="major"><el-input v-model="form.major" placeholder="请输入专业" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出生日期"><el-date-picker v-model="form.birthDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="政治面貌">
              <el-select v-model="form.politicalStatus" placeholder="请选择"><el-option label="中共党员" value="中共党员" /><el-option label="民主党派" value="民主党派" /><el-option label="群众" value="群众" /></el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="任命日期"><el-date-picker v-model="form.appointDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="form.status" placeholder="请选择"><el-option label="在任" value="在任" /><el-option label="离任" value="离任" /><el-option label="调任" value="调任" /><el-option label="退休" value="退休" /></el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话"><el-input v-model="form.phone" placeholder="请输入电话" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱"><el-input v-model="form.email" placeholder="请输入邮箱" /></el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确 定</el-button>
      </div>
      <div slot="footer" v-else>
        <el-button @click="dialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 考核对话框 -->
    <el-dialog title="负责人考核评价" :visible.sync="evalDialogVisible" width="650px" :close-on-click-modal="false">
      <el-form ref="evalForm" :model="evalForm" :rules="evalRules" label-width="100px">
        <el-form-item label="负责人"><el-input :value="evalForm.leaderName" disabled /></el-form-item>
        <el-form-item label="所属企业"><el-input :value="evalForm.enterpriseName" disabled /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="考核年度" prop="assessmentYear"><el-input v-model="evalForm.assessmentYear" placeholder="如：2024" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="考核类型" prop="assessmentType">
              <el-select v-model="evalForm.assessmentType" placeholder="请选择"><el-option label="年度考核" value="年度考核" /><el-option label="任期考核" value="任期考核" /><el-option label="专项考核" value="专项考核" /></el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="政治素质" prop="politicalScore"><el-input-number v-model="evalForm.politicalScore" :min="0" :max="100" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="经营业绩" prop="economicScore"><el-input-number v-model="evalForm.economicScore" :min="0" :max="100" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="管理能力" prop="managementScore"><el-input-number v-model="evalForm.managementScore" :min="0" :max="100" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="廉洁自律" prop="integrityScore"><el-input-number v-model="evalForm.integrityScore" :min="0" :max="100" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="考核等级" prop="grade">
          <el-select v-model="evalForm.grade" placeholder="请选择"><el-option label="优秀" value="优秀" /><el-option label="良好" value="良好" /><el-option label="称职" value="称职" /><el-option label="不称职" value="不称职" /></el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="evalDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEvalSubmit" :loading="evalSubmitLoading">提交考核</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getLeaderInfoList, getLeaderInfoDetail, addLeaderInfo, updateLeaderInfo, getLeaderInfoStatistics,
  getLeaderResumeList, getLeaderPositionList, getLeaderCertificateList, getLeaderArchiveList, getLeaderChangeRecordList,
  addLeaderEvaluation
} from '@/api/leader/index'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'LeaderManagement',
  mixins: [investThemeMixin],
  data() {
    return {
      activeTab: 'basic',
      searchText: '',
      loading: false,
      submitLoading: false,
      evalSubmitLoading: false,
      overviewData: { totalLeaders: 0, activeLeaders: 0, avgScore: 0, excellentCount: 0 },
      // 主表格
      tableData: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 15, leaderName: '', status: '' },
      // Tab数据
      tabData: { basic: [], resume: [], position: [], certificate: [], archive: [], change: [] },
      tabLoading: { basic: false, resume: false, position: false, certificate: false, archive: false, change: false },
      tabPage: {
        basic: { pageNumber: 1, pageSize: 10, total: 0 },
        resume: { pageNumber: 1, pageSize: 10, total: 0 },
        position: { pageNumber: 1, pageSize: 10, total: 0 },
        certificate: { pageNumber: 1, pageSize: 10, total: 0 },
        archive: { pageNumber: 1, pageSize: 10, total: 0 },
        change: { pageNumber: 1, pageSize: 10, total: 0 }
      },
      // 新增/编辑对话框
      dialogVisible: false,
      dialogType: 'add',
      form: {},
      formRules: {
        leaderName: [{ required: true, message: '请输入负责人姓名', trigger: 'blur' }],
        enterpriseName: [{ required: true, message: '请输入所属企业', trigger: 'blur' }],
        position: [{ required: true, message: '请输入职务', trigger: 'blur' }]
      },
      // 考核对话框
      evalDialogVisible: false,
      evalForm: { leaderId: '', leaderName: '', enterpriseName: '', assessmentYear: '', assessmentType: '', politicalScore: 80, economicScore: 80, managementScore: 80, integrityScore: 80, grade: '' },
      evalRules: {
        assessmentYear: [{ required: true, message: '请输入考核年度', trigger: 'blur' }],
        assessmentType: [{ required: true, message: '请选择考核类型', trigger: 'change' }],
        grade: [{ required: true, message: '请选择考核等级', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      const map = { add: '新增负责人', edit: '编辑负责人', view: '查看负责人详情' }
      return map[this.dialogType] || '负责人信息'
    }
  },
  mounted() {
    this.loadData()
    this.loadStatistics()
    this.loadTabData('basic')
  },
  methods: {
    // ========== 主表格数据 ==========
    async loadData() {
      this.loading = true
      try {
        const params = { ...this.queryForm }
        if (this.searchText) params.leaderName = this.searchText
        const res = await getLeaderInfoList(params)
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
        const res = await getLeaderInfoStatistics()
        if (res && res.data) this.overviewData = res.data
      } catch (e) { console.error('加载统计失败', e) }
    },
    // ========== Tab数据加载 ==========
    async loadTabData(tab) {
      const apiMap = {
        basic: getLeaderInfoList,
        resume: getLeaderResumeList,
        position: getLeaderPositionList,
        certificate: getLeaderCertificateList,
        archive: getLeaderArchiveList,
        change: getLeaderChangeRecordList
      }
      const api = apiMap[tab]
      if (!api) return
      this.tabLoading[tab] = true
      try {
        const params = { pageNumber: this.tabPage[tab].pageNumber, pageSize: this.tabPage[tab].pageSize }
        const res = await api(params)
        if (res && res.data) {
          this.tabData[tab] = res.data.tlist || []
          this.tabPage[tab].total = res.data.totalRecord || 0
        }
      } catch (e) { console.error('加载Tab数据失败', e) } finally { this.tabLoading[tab] = false }
    },
    handleTabClick() { this.loadTabData(this.activeTab) },
    handleTabPageChange(tab, page) { this.tabPage[tab].pageNumber = page; this.loadTabData(tab) },
    // ========== 刷新数据 ==========
    refreshAllData() {
      this.loadData()
      this.loadStatistics()
      this.loadTabData(this.activeTab)
      this.$message.success('数据已刷新')
    },
    // ========== 搜索与分页 ==========
    handleSearch() { this.queryForm.pageNumber = 1; this.loadData() },
    handlePageChange(val) { this.queryForm.pageNumber = val; this.loadData() },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.queryForm.pageNumber = 1; this.loadData() },

    // ========== 新增负责人 ==========
    handleAddLeader() {
      this.dialogType = 'add'
      this.form = { leaderName: '', gender: '', enterpriseName: '', position: '', education: '', major: '', birthDate: '', politicalStatus: '', appointDate: '', status: '在任', phone: '', email: '' }
      this.dialogVisible = true
      this.$nextTick(() => { if (this.$refs.leaderForm) this.$refs.leaderForm.clearValidate() })
    },
    // ========== 查看详情 ==========
    async viewDetail(row) {
      this.dialogType = 'view'
      try {
        const res = await getLeaderInfoDetail(row.id)
        if (res && res.data) { this.form = { ...res.data } } else { this.form = { ...row } }
      } catch (e) { this.form = { ...row } }
      this.dialogVisible = true
    },
    // ========== 编辑负责人 ==========
    async editLeader(row) {
      this.dialogType = 'edit'
      try {
        const res = await getLeaderInfoDetail(row.id)
        if (res && res.data) { this.form = { ...res.data } } else { this.form = { ...row } }
      } catch (e) { this.form = { ...row } }
      this.dialogVisible = true
      this.$nextTick(() => { if (this.$refs.leaderForm) this.$refs.leaderForm.clearValidate() })
    },
    // ========== 提交表单 ==========
    handleSubmit() {
      this.$refs.leaderForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          if (this.dialogType === 'add') {
            const res = await addLeaderInfo(this.form)
            if (res && res.data) {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.loadData()
              this.loadStatistics()
              // 级联刷新：基本信息、任职情况、变更记录
              this.loadTabData('basic')
              this.loadTabData('position')
              this.loadTabData('change')
            } else { this.$message.error(res.msg || '新增失败') }
          } else {
            const res = await updateLeaderInfo(this.form)
            if (res && res.data) {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadData()
              this.loadStatistics()
              // 级联刷新：基本信息、任职情况、变更记录
              this.loadTabData('basic')
              this.loadTabData('position')
              this.loadTabData('change')
            } else { this.$message.error(res.msg || '更新失败') }
          }
        } catch (e) { this.$message.error('操作失败：' + (e.message || '')) } finally { this.submitLoading = false }
      })
    },
    // ========== 考核 ==========
    openEvaluation(row) {
      this.evalForm = {
        leaderId: row.id,
        leaderName: row.leaderName,
        enterpriseName: row.enterpriseName,
        enterpriseId: row.enterpriseId || '',
        assessmentYear: new Date().getFullYear().toString(),
        assessmentType: '',
        politicalScore: 80,
        economicScore: 80,
        managementScore: 80,
        integrityScore: 80,
        grade: '',
        evaluator: ''
      }
      this.evalDialogVisible = true
      this.$nextTick(() => { if (this.$refs.evalForm) this.$refs.evalForm.clearValidate() })
    },
    handleEvalSubmit() {
      this.$refs.evalForm.validate(async (valid) => {
        if (!valid) return
        this.evalSubmitLoading = true
        try {
          const submitData = { ...this.evalForm }
          submitData.totalScore = ((submitData.politicalScore + submitData.economicScore + submitData.managementScore + submitData.integrityScore) / 4).toFixed(1)
          const res = await addLeaderEvaluation(submitData)
          if (res && res.data) {
            this.$message.success('考核提交成功')
            this.evalDialogVisible = false
            this.loadData()
            this.loadStatistics()
            // 级联刷新：档案管理Tab（考核会自动生成考核档案）
            this.loadTabData('archive')
          } else { this.$message.error(res.msg || '考核提交失败') }
        } catch (e) { this.$message.error('考核提交失败：' + (e.message || '')) } finally { this.evalSubmitLoading = false }
      })
    },
    // ========== 工具方法 ==========
    getStatusType(status) {
      const map = { '在任': 'success', '在职': 'success', '离任': 'info', '调任': 'warning', '退休': 'info', '离职': 'danger' }
      return map[status] || 'info'
    },
    getCertStatusType(status) {
      const map = { '有效': 'success', '已过期': 'danger', '已撤销': 'info' }
      return map[status] || 'info'
    },
    getArchiveStatusType(status) {
      const map = { '正常': 'success', '借阅中': 'warning', '已归还': 'info' }
      return map[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.leader-management {
  padding: 20px;
  .overview-cards {
    margin-bottom: 20px;
    .overview-card {
      height: 120px;
      &.gradient-theme {
        background: linear-gradient(135deg, var(--ip-primary, #667eea) 0%, var(--ip-secondary, #764ba2) 100%);
        color: white;
        .card-content {
          display: flex; align-items: center; height: 100%;
          .icon-wrapper { font-size: 40px; margin-right: 15px; opacity: 0.8; }
          .data-wrapper {
            .number { font-size: 28px; font-weight: bold; line-height: 1; }
            .label { font-size: 14px; margin-top: 5px; opacity: 0.9; }
          }
        }
      }
    }
  }
  .function-card, .data-card { margin-bottom: 20px; }
  .function-card {
    ::v-deep .el-tabs--border-card > .el-tabs__content { padding: 10px; }
    ::v-deep .el-table { width: 100% !important; }
  }
  .card-header {
    display: flex; justify-content: space-between; align-items: center;
    .card-title { font-size: 16px; font-weight: bold; }
  }
  .search-wrapper { display: flex; align-items: center; }
  .tab-pagination, .main-pagination { margin-top: 12px; text-align: right; padding-right: 0; }
  ::v-deep .el-tabs__content { padding: 10px 0 0 0; }
}
</style>