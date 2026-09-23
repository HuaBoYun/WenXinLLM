<template>
  <div class="industry-financial" :style="themeVars">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card financial-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-bank-card"></i></div>
            <div class="card-info">
              <div class="card-title">监管金融机构</div>
              <div class="card-value">{{ overviewData.totalInstitutions }}家</div>
              <div class="card-desc">银行 {{ overviewData.bankCount }}家</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card financial-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-money"></i></div>
            <div class="card-info">
              <div class="card-title">总资产规模</div>
              <div class="card-value">{{ overviewData.totalAssets }}亿</div>
              <div class="card-desc">平均资本充足率 {{ overviewData.capitalRatio }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card financial-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-warning"></i></div>
            <div class="card-info">
              <div class="card-title">风险预警</div>
              <div class="card-value">{{ overviewData.highRiskCount }}项</div>
              <div class="card-desc">高风险机构占比</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card financial-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-s-data"></i></div>
            <div class="card-info">
              <div class="card-title">合规率</div>
              <div class="card-value">{{ overviewData.complianceRate }}%</div>
              <div class="card-desc">{{ overviewData.complianceRate >= 90 ? '优秀等级' : '待提升' }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="机构名称" prop="enterpriseName">
          <el-input v-model="queryForm.enterpriseName" placeholder="请输入机构名称" clearable />
        </el-form-item>
        <el-form-item label="金融类型" prop="industryType">
          <el-select v-model="queryForm.industryType" placeholder="请选择金融类型" clearable>
            <el-option label="银行" value="银行" />
            <el-option label="保险" value="保险" />
            <el-option label="证券" value="证券" />
            <el-option label="基金" value="基金" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择风险等级" clearable>
            <el-option label="正常" value="正常" />
            <el-option label="预警" value="预警" />
            <el-option label="高风险" value="风险" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">新增监管</el-button>
          <el-button type="warning" @click="handleExport" icon="el-icon-download">导出报告</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">金融类国企监管功能</span>
        <span class="card-total">共 {{ total }} 条记录</span>
      </div>

      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="银行业监管" name="banking">
          <el-table :data="getTabData('银行')" border size="small" :header-cell-style="{ background: '#F0F5FF', color: '#1d39c4' }">
            <el-table-column label="机构名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产规模(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="不良率%" prop="badDebtRatio" width="90" align="center" />
            <el-table-column label="资本充足率%" prop="capitalRatio" width="110" align="center" />
            <el-table-column label="合规状态" prop="complianceStatus" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.complianceStatus === '合规' ? 'success' : s.row.complianceStatus === '整改中' ? 'warning' : 'danger'" size="small">{{ s.row.complianceStatus }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="保险业监管" name="insurance">
          <el-table :data="getTabData('保险')" border size="small" :header-cell-style="{ background: '#F0F5FF', color: '#1d39c4' }">
            <el-table-column label="机构名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产规模(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="资本充足率%" prop="capitalRatio" width="110" align="center" />
            <el-table-column label="偿付能力" prop="solvency" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.solvency === '充足' ? 'success' : s.row.solvency === '一般' ? 'warning' : 'danger'" size="small">{{ s.row.solvency }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="证券业监管" name="securities">
          <el-table :data="getTabData('证券')" border size="small" :header-cell-style="{ background: '#F0F5FF', color: '#1d39c4' }">
            <el-table-column label="机构名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产规模(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="资本充足率%" prop="capitalRatio" width="110" align="center" />
            <el-table-column label="合规状态" prop="complianceStatus" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.complianceStatus === '合规' ? 'success' : s.row.complianceStatus === '整改中' ? 'warning' : 'danger'" size="small">{{ s.row.complianceStatus }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="基金业监管" name="fund">
          <el-table :data="getTabData('基金')" border size="small" :header-cell-style="{ background: '#F0F5FF', color: '#1d39c4' }">
            <el-table-column label="机构名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产规模(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="资本充足率%" prop="capitalRatio" width="110" align="center" />
            <el-table-column label="不良率%" prop="badDebtRatio" width="90" align="center" />
            <el-table-column label="合规状态" prop="complianceStatus" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.complianceStatus === '合规' ? 'success' : s.row.complianceStatus === '整改中' ? 'warning' : 'danger'" size="small">{{ s.row.complianceStatus }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="金融风险监控" name="riskMonitoring">
          <el-table :data="tabDataList" border size="small" :header-cell-style="{ background: '#F0F5FF', color: '#1d39c4' }">
            <el-table-column label="机构名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="金融类型" prop="type" width="90" align="center" />
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
            </el-table-column>
            <el-table-column label="不良率%" prop="badDebtRatio" width="90" align="center" />
            <el-table-column label="合规状态" prop="complianceStatus" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.complianceStatus === '合规' ? 'success' : s.row.complianceStatus === '整改中' ? 'warning' : 'danger'" size="small">{{ s.row.complianceStatus }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="资本管理" name="capitalManagement">
          <el-table :data="tabDataList" border size="small" :header-cell-style="{ background: '#F0F5FF', color: '#1d39c4' }">
            <el-table-column label="机构名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="金融类型" prop="type" width="90" align="center" />
            <el-table-column label="资本充足率%" prop="capitalRatio" width="110" align="center" />
            <el-table-column label="资产规模(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="评级" prop="rating" width="70" align="center">
              <template slot-scope="s"><el-tag :type="s.row.rating === 'A' ? 'success' : s.row.rating === 'B' ? 'primary' : 'warning'" size="small">{{ s.row.rating }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="合规管理" name="complianceManagement">
          <el-table :data="tabDataList" border size="small" :header-cell-style="{ background: '#F0F5FF', color: '#1d39c4' }">
            <el-table-column label="机构名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
            <el-table-column label="金融类型" prop="type" width="90" align="center" />
            <el-table-column label="合规状态" prop="complianceStatus" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.complianceStatus === '合规' ? 'success' : s.row.complianceStatus === '整改中' ? 'warning' : 'danger'" size="small">{{ s.row.complianceStatus }}</el-tag></template>
            </el-table-column>
            <el-table-column label="不良率%" prop="badDebtRatio" width="90" align="center" />
            <el-table-column label="资本充足率%" prop="capitalRatio" width="110" align="center" />
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 新增监管弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="formDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-form-item label="机构名称" prop="companyName">
          <el-input v-model="formData.companyName" placeholder="请输入机构名称" />
        </el-form-item>
        <el-form-item label="金融类型" prop="subType">
          <el-select v-model="formData.subType" placeholder="请选择金融类型" style="width: 100%">
            <el-option label="银行" value="银行" />
            <el-option label="保险" value="保险" />
            <el-option label="证券" value="证券" />
            <el-option label="基金" value="基金" />
          </el-select>
        </el-form-item>
        <el-form-item label="资产规模(亿)">
          <el-input-number v-model="formData.totalAssets" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="不良率(%)">
          <el-input-number v-model="formData.badDebtRatio" :min="0" :max="100" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="资本充足率(%)">
          <el-input-number v-model="formData.capitalRatio" :min="0" :max="100" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="净利率(%)">
          <el-input-number v-model="formData.netMargin" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="合规状态">
          <el-select v-model="formData.complianceStatus" placeholder="请选择合规状态" style="width: 100%">
            <el-option label="合规" value="COMPLIANT" />
            <el-option label="整改中" value="WARNING" />
            <el-option label="违规" value="VIOLATION" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="formData.riskLevel" placeholder="请选择风险等级" style="width: 100%">
            <el-option label="正常(低风险)" value="LOW" />
            <el-option label="预警(中风险)" value="MEDIUM" />
            <el-option label="高风险" value="HIGH" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="formDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="formLoading" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog title="监管详情" :visible.sync="viewDialogVisible" width="600px">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="机构名称">{{ viewData.companyName }}</el-descriptions-item>
        <el-descriptions-item label="金融类型">{{ viewData.subType }}</el-descriptions-item>
        <el-descriptions-item label="资产规模(亿)">{{ viewData.totalAssets }}</el-descriptions-item>
        <el-descriptions-item label="不良率(%)">{{ viewData.badDebtRatio }}</el-descriptions-item>
        <el-descriptions-item label="资本充足率(%)">{{ viewData.capitalRatio }}</el-descriptions-item>
        <el-descriptions-item label="净利率(%)">{{ viewData.netMargin }}</el-descriptions-item>
        <el-descriptions-item label="合规状态">{{ { COMPLIANT: '合规', WARNING: '整改中', VIOLATION: '违规' }[viewData.complianceStatus] || viewData.complianceStatus }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="viewData.riskLevel === 'LOW' ? 'success' : viewData.riskLevel === 'MEDIUM' ? 'warning' : 'danger'" size="small">
            {{ viewData.riskLevel === 'LOW' ? '正常' : viewData.riskLevel === 'MEDIUM' ? '预警' : '高风险' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewData.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="viewDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFinancialList, addFinancialMonitor, getFinancialDetail, updateFinancialMonitor, deleteFinancialMonitor, exportFinancialData } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'IndustryFinancial',
  mixins: [investThemeMixin],
  components: {},
  data() {
    return {
      activeTab: 'banking',
      loading: false,
      total: 0,
      queryForm: {
        enterpriseName: '',
        industryType: '',
        status: '',
        pageNum: 1,
        pageSize: 200
      },
      overviewData: {
        totalInstitutions: 0,
        bankCount: 0,
        totalAssets: 0,
        capitalRatio: 0,
        highRiskCount: 0,
        complianceRate: 0
      },
      tabDataList: [],
      // 表单弹窗(新增/编辑共用)
      formDialogVisible: false,
      formLoading: false,
      dialogTitle: '新增金融监管',
      isEdit: false,
      formData: {
        id: '',
        companyName: '',
        subType: '',
        totalAssets: 0,
        badDebtRatio: 0,
        capitalRatio: 0,
        netMargin: 0,
        complianceStatus: 'COMPLIANT',
        riskLevel: 'LOW'
      },
      formRules: {
        companyName: [{ required: true, message: '请输入机构名称', trigger: 'blur' }],
        subType: [{ required: true, message: '请选择金融类型', trigger: 'change' }]
      },
      // 查看详情
      viewDialogVisible: false,
      viewData: {}
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    getTabData(keyword) {
      return this.tabDataList.filter(r => r.type && r.type.includes(keyword))
    },

    async loadData() {
      this.loading = true
      try {
        const params = {
          enterpriseName: this.queryForm.enterpriseName,
          subType: this.queryForm.industryType,
          status: this.queryForm.status,
          pageNum: this.queryForm.pageNum,
          pageSize: this.queryForm.pageSize
        }
        const res = await getFinancialList(params)
        if (res && res.result === 200 && res.data) {
          const list = res.data.tlist || res.data.list || []
          this.total = res.data.totalRecord || list.length

          const highRiskCount = list.filter(item => item.riskLevel === 'HIGH').length
          const complianceCount = list.filter(item => !item.complianceStatus || item.complianceStatus === 'COMPLIANT' || item.complianceStatus === '合规').length
          const avgCapital = list.length > 0
            ? (list.reduce((s, r) => s + (Number(r.capitalRatio) || 0), 0) / list.length).toFixed(1) : 0

          this.overviewData = {
            totalInstitutions: this.total,
            bankCount: list.filter(item => (item.subType || item.finType || '').includes('银行')).length,
            totalAssets: list.reduce((sum, item) => sum + (Number(item.totalAssets) || 0), 0).toFixed(1),
            capitalRatio: avgCapital,
            highRiskCount: highRiskCount,
            complianceRate: list.length > 0 ? ((complianceCount / list.length) * 100).toFixed(1) : 0
          }

          this.tabDataList = list.map(item => ({
            id: item.id,
            enterpriseName: item.companyName,
            type: item.subType || item.finType || '银行',
            totalAssets: item.totalAssets || 0,
            badDebtRatio: item.badDebtRatio || 0,
            capitalRatio: item.capitalRatio || 0,
            netMargin: item.netMargin || 0,
            complianceStatus: item.complianceStatus === 'COMPLIANT' ? '合规' : item.complianceStatus === 'WARNING' ? '整改中' : item.complianceStatus === 'VIOLATION' ? '违规' : (item.complianceStatus || '合规'),
            complianceStatusRaw: item.complianceStatus,
            riskLevel: item.riskLevel === 'HIGH' ? '高' : item.riskLevel === 'MEDIUM' ? '中' : '低',
            riskLevelRaw: item.riskLevel,
            solvency: item.riskLevel === 'HIGH' ? '不足' : item.riskLevel === 'MEDIUM' ? '一般' : '充足',
            rating: item.riskLevel === 'HIGH' ? 'C' : item.riskLevel === 'MEDIUM' ? 'B' : 'A'
          }))
        }
      } catch (error) {
        console.error('获取金融行业数据异常:', error)
        this.$message.error('获取数据失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },

    handleReset() {
      this.queryForm = { enterpriseName: '', industryType: '', status: '', pageNum: 1, pageSize: 200 }
      this.$nextTick(() => {
        if (this.$refs.queryForm) this.$refs.queryForm.resetFields()
        this.loadData()
      })
    },

    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增金融监管'
      this.formData = { id: '', companyName: '', subType: '', totalAssets: 0, badDebtRatio: 0, capitalRatio: 0, netMargin: 0, complianceStatus: 'COMPLIANT', riskLevel: 'LOW' }
      this.formDialogVisible = true
      this.$nextTick(() => { if (this.$refs.formRef) this.$refs.formRef.clearValidate() })
    },

    async handleView(row) {
      try {
        const res = await getFinancialDetail(row.id)
        if (res && res.result === 200 && res.data) {
          this.viewData = res.data
        } else {
          this.viewData = { companyName: row.enterpriseName, subType: row.type, totalAssets: row.totalAssets, badDebtRatio: row.badDebtRatio, capitalRatio: row.capitalRatio, complianceStatus: row.complianceStatus, riskLevel: row.riskLevelRaw || 'LOW', createTime: '' }
        }
      } catch (e) {
        this.viewData = { companyName: row.enterpriseName, subType: row.type, totalAssets: row.totalAssets, badDebtRatio: row.badDebtRatio, capitalRatio: row.capitalRatio, complianceStatus: row.complianceStatus, riskLevel: row.riskLevelRaw || 'LOW', createTime: '' }
      }
      this.viewDialogVisible = true
    },

    async handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑金融监管'
      try {
        const res = await getFinancialDetail(row.id)
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.formData = { id: d.id, companyName: d.companyName, subType: d.subType, totalAssets: Number(d.totalAssets) || 0, badDebtRatio: Number(d.badDebtRatio) || 0, capitalRatio: Number(d.capitalRatio) || 0, netMargin: Number(d.netMargin) || 0, complianceStatus: d.complianceStatus || 'COMPLIANT', riskLevel: d.riskLevel || 'LOW' }
        } else {
          this.formData = { id: row.id, companyName: row.enterpriseName, subType: row.type, totalAssets: Number(row.totalAssets) || 0, badDebtRatio: Number(row.badDebtRatio) || 0, capitalRatio: Number(row.capitalRatio) || 0, netMargin: Number(row.netMargin) || 0, complianceStatus: row.complianceStatusRaw || 'COMPLIANT', riskLevel: row.riskLevelRaw || 'LOW' }
        }
      } catch (e) {
        this.formData = { id: row.id, companyName: row.enterpriseName, subType: row.type, totalAssets: Number(row.totalAssets) || 0, badDebtRatio: Number(row.badDebtRatio) || 0, capitalRatio: Number(row.capitalRatio) || 0, netMargin: Number(row.netMargin) || 0, complianceStatus: row.complianceStatusRaw || 'COMPLIANT', riskLevel: row.riskLevelRaw || 'LOW' }
      }
      this.formDialogVisible = true
      this.$nextTick(() => { if (this.$refs.formRef) this.$refs.formRef.clearValidate() })
    },

    submitForm() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        this.formLoading = true
        try {
          const apiFn = this.isEdit ? updateFinancialMonitor : addFinancialMonitor
          const res = await apiFn(this.formData)
          if (res && res.result === 200) {
            this.$message.success(this.isEdit ? '编辑成功' : '新增成功')
            this.formDialogVisible = false
            this.loadData()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败，请稍后重试')
        } finally {
          this.formLoading = false
        }
      })
    },

    handleDelete(row) {
      this.$confirm(`确定要删除机构「${row.enterpriseName}」的监管记录吗？`, '删除确认', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteFinancialMonitor(row.id)
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败，请稍后重试')
        }
      }).catch(() => {})
    },

    async handleExport() {
      const loading = this.$loading({ lock: true, text: '正在导出报告...', spinner: 'el-icon-loading', background: 'rgba(0, 0, 0, 0.7)' })
      try {
        const params = {
          companyName: this.queryForm.enterpriseName,
          subType: this.queryForm.industryType,
          riskLevel: this.queryForm.status === '正常' ? 'LOW' : this.queryForm.status === '预警' ? 'MEDIUM' : this.queryForm.status === '风险' ? 'HIGH' : ''
        }
        const res = await exportFinancialData(params)
        if (res && res.result === 200 && res.data) {
          this.downloadCsv(res.data)
          this.$message.success('导出成功')
        } else {
          this.$message.error(res.msg || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败，请稍后重试')
      } finally {
        loading.close()
      }
    },

    downloadCsv(data) {
      if (!data || data.length === 0) { this.$message.warning('暂无数据可导出'); return }
      const headers = Object.keys(data[0])
      const csvContent = [
        headers.map(h => `"${h}"`).join(','),
        ...data.map(row => headers.map(h => {
          const val = row[h] != null ? String(row[h]) : ''
          return `"${val.replace(/"/g, '""')}"`
        }).join(','))
      ].join('\n')
      const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `金融行业监管报告_${new Date().toISOString().slice(0, 10)}.csv`
      link.click()
      URL.revokeObjectURL(link.href)
    }
  }
}
</script>

<style lang="scss" scoped>
.industry-financial {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;

    .overview-card {
      height: 120px;
      border: none;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

      &.financial-gradient {
        background: linear-gradient(135deg, var(--ip-primary, #667eea) 0%, var(--ip-secondary, #764ba2) 100%);
        color: white;

        .card-content {
          display: flex;
          align-items: center;
          height: 100%;

          .card-icon {
            font-size: 36px;
            margin-right: 16px;
            opacity: 0.8;
          }

          .card-info {
            flex: 1;

            .card-title {
              font-size: 14px;
              margin-bottom: 8px;
              opacity: 0.9;
            }

            .card-value {
              font-size: 24px;
              font-weight: bold;
              margin-bottom: 4px;
            }

            .card-desc {
              font-size: 12px;
              opacity: 0.8;
            }
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .function-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .card-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }

      .card-total {
        font-size: 13px;
        color: #909399;
      }
    }
  }

  ::v-deep .el-tabs__content {
    padding: 20px;
    min-height: 400px;
  }

  ::v-deep .el-card__body {
    padding: 0;
  }
}
</style>
