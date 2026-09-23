<template>
  <div class="bill-risk-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-warning"></i>
            票据风险管理
          </h2>
          <p class="page-description">票据风险识别、评估、预警和处置管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreateAssessment">
            新增评估
          </el-button>
          <el-button type="warning" icon="el-icon-bell" @click="handleViewAlerts">
            风险预警
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出报告
          </el-button>
        </div>
      </div>
    </div>

    <!-- 风险概览卡片 -->
    <div class="risk-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">风险票据</div>
                <div class="card-value">{{ riskBills }}</div>
                <div class="card-change">张票据</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon high-risk-icon">
                <i class="el-icon-error"></i>
              </div>
              <div class="card-info">
                <div class="card-title">高风险</div>
                <div class="card-value">{{ highRiskBills }}</div>
                <div class="card-change danger">需关注</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon medium-risk-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">中风险</div>
                <div class="card-value">{{ mediumRiskBills }}</div>
                <div class="card-change warning">需监控</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">风险金额</div>
                <div class="card-value">{{ riskAmount }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 风险分析图表 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>风险等级分布</h3>
            <div class="chart-controls">
              <el-radio-group v-model="riskChartType" size="small" @change="handleRiskChartTypeChange">
                <el-radio-button label="pie">饼图</el-radio-button>
                <el-radio-button label="bar">柱状图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="riskDistributionChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>风险趋势分析</h3>
            <div class="chart-controls">
              <el-radio-group v-model="trendPeriod" size="small" @change="handleTrendPeriodChange">
                <el-radio-button label="7D">7天</el-radio-button>
                <el-radio-button label="30D">30天</el-radio-button>
                <el-radio-button label="90D">90天</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="riskTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="票据号码">
            <el-input
              v-model="listQuery.billNumber"
              placeholder="请输入票据号码"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="高风险" value="HIGH" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="低风险" value="LOW" />
            </el-select>
          </el-form-item>
          <el-form-item label="风险类型">
            <el-select
              v-model="listQuery.riskType"
              placeholder="请选择风险类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="信用风险" value="CREDIT" />
              <el-option label="流动性风险" value="LIQUIDITY" />
              <el-option label="操作风险" value="OPERATIONAL" />
              <el-option label="市场风险" value="MARKET" />
            </el-select>
          </el-form-item>
          <el-form-item label="评估状态">
            <el-select
              v-model="listQuery.assessmentStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待评估" value="PENDING" />
              <el-option label="已评估" value="ASSESSED" />
              <el-option label="需复评" value="REASSESS" />
              <el-option label="已处置" value="DISPOSED" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 风险评估表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="riskList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="评估ID" prop="assessmentId" width="80" align="center" />
        <el-table-column label="票据号码" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.billNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票据金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="bill-amount">{{ formatCurrency(row.billAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRiskLevelTagType(row.riskLevel)" size="mini">
              <i :class="getRiskLevelIcon(row.riskLevel)"></i>
              {{ getRiskLevelText(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险类型" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRiskTypeTagType(row.riskType)" size="mini">
              {{ getRiskTypeText(row.riskType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险评分" width="100px" align="center">
          <template slot-scope="{row}">
            <span :class="getRiskScoreClass(row.riskScore)">{{ row.riskScore }}分</span>
          </template>
        </el-table-column>
        <el-table-column label="承兑人" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.acceptorName || '--' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="评估日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.assessmentDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="评估状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAssessmentStatusTagType(row.assessmentStatus)" size="mini">
              {{ getAssessmentStatusText(row.assessmentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="评估人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.assessorName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.assessmentStatus === 'PENDING'" type="primary" size="mini" @click="handleAssess(row)">
              评估
            </el-button>
            <el-button v-if="row.riskLevel === 'HIGH'" type="warning" size="mini" @click="handleDispose(row)">
              处置
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'reassess', row: row}">重新评估</el-dropdown-item>
                <el-dropdown-item :command="{action: 'monitor', row: row}">加入监控</el-dropdown-item>
                <el-dropdown-item :command="{action: 'history', row: row}">评估历史</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />
    </el-card>

    <!-- 风险评估对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-tabs v-model="activeFormTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票据号码" prop="billNumber">
                  <el-input v-model="temp.billNumber" placeholder="请输入票据号码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="票据金额" prop="billAmount">
                  <el-input v-model="temp.billAmount" type="number" placeholder="请输入票据金额" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="承兑人" prop="acceptorName">
                  <el-input v-model="temp.acceptorName" placeholder="请输入承兑人" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期日期">
                  <el-date-picker
                    v-model="temp.maturityDate"
                    type="date"
                    placeholder="选择到期日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="评估日期" prop="assessmentDate">
                  <el-date-picker
                    v-model="temp.assessmentDate"
                    type="date"
                    placeholder="选择评估日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="评估人">
                  <el-input v-model="temp.assessorName" placeholder="请输入评估人" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="风险评估" name="assessment">
            <el-form-item label="风险类型" prop="riskType">
              <el-select v-model="temp.riskType" placeholder="请选择风险类型" style="width: 100%;">
                <el-option label="信用风险" value="CREDIT" />
                <el-option label="流动性风险" value="LIQUIDITY" />
                <el-option label="操作风险" value="OPERATIONAL" />
                <el-option label="市场风险" value="MARKET" />
              </el-select>
            </el-form-item>
            <el-form-item label="风险等级" prop="riskLevel">
              <el-radio-group v-model="temp.riskLevel">
                <el-radio label="LOW">低风险</el-radio>
                <el-radio label="MEDIUM">中风险</el-radio>
                <el-radio label="HIGH">高风险</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="风险评分" prop="riskScore">
              <el-slider
                v-model="temp.riskScore"
                :min="0"
                :max="100"
                :step="1"
                show-stops
                show-input
                style="width: 100%;"
              />
              <div class="score-description">
                <span class="score-range low">0-30分：低风险</span>
                <span class="score-range medium">31-70分：中风险</span>
                <span class="score-range high">71-100分：高风险</span>
              </div>
            </el-form-item>
            <el-form-item label="风险因素">
              <el-checkbox-group v-model="temp.riskFactors">
                <el-checkbox label="CREDIT_RATING">信用评级下降</el-checkbox>
                <el-checkbox label="FINANCIAL_STATUS">财务状况恶化</el-checkbox>
                <el-checkbox label="INDUSTRY_RISK">行业风险</el-checkbox>
                <el-checkbox label="GUARANTEE_RISK">担保风险</el-checkbox>
                <el-checkbox label="MATURITY_RISK">到期风险</el-checkbox>
                <el-checkbox label="LIQUIDITY_RISK">流动性风险</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="风险描述" name="description">
            <el-form-item label="风险描述" prop="riskDescription">
              <el-input v-model="temp.riskDescription" type="textarea" :rows="4" placeholder="请详细描述识别的风险点" />
            </el-form-item>
            <el-form-item label="影响分析">
              <el-input v-model="temp.impactAnalysis" type="textarea" :rows="3" placeholder="请分析风险可能造成的影响" />
            </el-form-item>
            <el-form-item label="应对措施">
              <el-input v-model="temp.mitigationMeasures" type="textarea" :rows="3" placeholder="请提出风险应对措施建议" />
            </el-form-item>
            <el-form-item label="监控建议">
              <el-input v-model="temp.monitoringSuggestions" type="textarea" :rows="3" placeholder="请提出后续监控建议" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 风险详情对话框 -->
    <el-dialog title="风险评估详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentRisk" class="risk-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="票据号码">{{ currentRisk.billNumber }}</el-descriptions-item>
          <el-descriptions-item label="票据金额">{{ formatCurrency(currentRisk.billAmount) }}</el-descriptions-item>
          <el-descriptions-item label="承兑人">{{ currentRisk.acceptorName }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ formatDate(currentRisk.maturityDate) }}</el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="getRiskLevelTagType(currentRisk.riskLevel)">
              {{ getRiskLevelText(currentRisk.riskLevel) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="风险类型">
            <el-tag :type="getRiskTypeTagType(currentRisk.riskType)">
              {{ getRiskTypeText(currentRisk.riskType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="风险评分">{{ currentRisk.riskScore }}分</el-descriptions-item>
          <el-descriptions-item label="评估日期">{{ formatDate(currentRisk.assessmentDate) }}</el-descriptions-item>
          <el-descriptions-item label="评估人">{{ currentRisk.assessorName }}</el-descriptions-item>
          <el-descriptions-item label="评估状态">
            <el-tag :type="getAssessmentStatusTagType(currentRisk.assessmentStatus)">
              {{ getAssessmentStatusText(currentRisk.assessmentStatus) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="risk-analysis">
          <h4>风险分析</h4>
          <div class="analysis-section">
            <h5>风险描述</h5>
            <p>{{ currentRisk.riskDescription || '无' }}</p>
          </div>
          <div class="analysis-section">
            <h5>影响分析</h5>
            <p>{{ currentRisk.impactAnalysis || '无' }}</p>
          </div>
          <div class="analysis-section">
            <h5>应对措施</h5>
            <p>{{ currentRisk.mitigationMeasures || '无' }}</p>
          </div>
          <div class="analysis-section">
            <h5>监控建议</h5>
            <p>{{ currentRisk.monitoringSuggestions || '无' }}</p>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentRisk && currentRisk.riskLevel === 'HIGH'" type="warning" @click="handleDispose(currentRisk)">
          风险处置
        </el-button>
      </div>
    </el-dialog>

    <!-- 风险预警对话框 -->
    <el-dialog title="风险预警" :visible.sync="dialogAlertsVisible" width="800px">
      <el-table :data="alertList" border size="small" max-height="400">
        <el-table-column label="预警时间" prop="alertTime" width="150" />
        <el-table-column label="预警级别" prop="alertLevel" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAlertLevelTagType(row.alertLevel)" size="mini">
              {{ row.alertLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="票据号码" prop="billNumber" width="150" />
        <el-table-column label="预警类型" prop="alertType" width="120" />
        <el-table-column label="预警内容" prop="alertMessage" min-width="200" />
        <el-table-column label="状态" prop="status" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.status === 'RESOLVED' ? 'success' : 'warning'" size="mini">
              {{ row.status === 'RESOLVED' ? '已处理' : '待处理' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogAlertsVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleMarkAllResolved">全部标记为已处理</el-button>
      </div>
    </el-dialog>

    <!-- 风险处置对话框 -->
    <el-dialog title="风险处置" :visible.sync="dialogDisposeVisible" width="600px">
      <el-form ref="disposeForm" :model="disposeForm" label-width="100px">
        <el-form-item label="处置方式" prop="disposeType">
          <el-radio-group v-model="disposeForm.disposeType">
            <el-radio label="MONITOR">加强监控</el-radio>
            <el-radio label="GUARANTEE">增加担保</el-radio>
            <el-radio label="EARLY_COLLECTION">提前托收</el-radio>
            <el-radio label="TRANSFER">风险转移</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处置说明" prop="disposeDescription">
          <el-input v-model="disposeForm.disposeDescription" type="textarea" :rows="4" placeholder="请输入处置说明" />
        </el-form-item>
        <el-form-item label="预期效果">
          <el-input v-model="disposeForm.expectedEffect" type="textarea" :rows="3" placeholder="请描述预期处置效果" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDisposeVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDispose">确认处置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBillRiskPage,
  getBillRiskDetail,
  createBillRiskAssessment,
  updateBillRiskAssessment,
  deleteBillRiskAssessment,
  getBillRiskAlerts,
  disposeBillRisk,
  markAlertResolved,
  exportBillRisk,
  getBillRiskStatistics,
  getRiskTrendData,
  getRiskDistributionData,
  getRiskAssessmentHistory
} from '@/api/globalTreasurer-new/billManagement/billRisk'
import Pagination from '@/components/Pagination'

export default {
  name: 'BillRiskManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        pageNum: 1,
        pageSize: 20,
        billNumber: undefined,
        riskLevel: undefined,
        riskType: undefined,
        assessmentStatus: undefined,
        assessmentDateRange: [],
        assessmentDateStart: undefined,
        assessmentDateEnd: undefined
      },
      riskBills: 0,
      highRiskBills: 0,
      mediumRiskBills: 0,
      riskAmount: 0,
      riskChartType: 'pie',
      trendPeriod: '30D',
      riskList: [],
      alertList: [],
      multipleSelection: [],
      currentRisk: null,
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogAlertsVisible: false,
      dialogDisposeVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      activeFormTab: 'basic',
      temp: {
        assessmentId: undefined,
        billNumber: '',
        billAmount: 0,
        acceptorName: '',
        maturityDate: '',
        riskType: '',
        riskLevel: '',
        riskScore: 50,
        riskFactors: [],
        assessmentDate: null,
        assessorName: '当前用户',
        riskDescription: '',
        impactAnalysis: '',
        mitigationMeasures: '',
        monitoringSuggestions: ''
      },
      disposeForm: {
        disposeType: '',
        disposeDescription: '',
        expectedEffect: ''
      },
      rules: {
        billNumber: [{ required: true, message: '票据号码不能为空', trigger: 'blur' }],
        riskType: [{ required: true, message: '请选择风险类型', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
        assessmentDate: [{ required: true, message: '请选择评估日期', trigger: 'change' }],
        riskDescription: [{ required: true, message: '请输入风险描述', trigger: 'blur' }]
      },
      riskChart: null,
      trendChart: null
    }
  },
  mounted() {
    this.getList()
    this.loadStatistics()
    this.initCharts()
    this.loadAlerts()
  },
  beforeDestroy() {
    if (this.riskChart) {
      this.riskChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    // 加载统计数据
    loadStatistics() {
      getBillRiskStatistics({}).then(response => {
        if (response.code === 1 && response.data) {
          this.riskBills = response.data.riskBills || 0
          this.highRiskBills = response.data.highRiskBills || 0
          this.mediumRiskBills = response.data.mediumRiskBills || 0
          this.riskAmount = response.data.riskAmount || 0
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
      })
    },
    // 格式化日期参数
    formatDateParam(date) {
      if (!date) return undefined
      if (typeof date === 'string') return date
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    getList() {
      this.listLoading = true
      // 处理日期范围
      const params = { ...this.listQuery }
      if (this.listQuery.assessmentDateRange && this.listQuery.assessmentDateRange.length === 2) {
        params.assessmentDateStart = this.formatDateParam(this.listQuery.assessmentDateRange[0])
        params.assessmentDateEnd = this.formatDateParam(this.listQuery.assessmentDateRange[1])
      }
      delete params.assessmentDateRange

      // 调用后端API获取真实数据
      getBillRiskPage(params).then(response => {
        if (response.code === 1) {
          this.riskList = response.data.tlist || response.data || []
          this.total = response.data.totalRecord || response.result?.total || 0
        } else {
          this.riskList = []
          this.total = 0
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取数据失败:', error)
        this.riskList = []
        this.total = 0
        this.listLoading = false
      })
    },
    initCharts() {
      const echarts = require('echarts')

      // 初始化风险分布图表
      this.riskChart = echarts.init(document.getElementById('riskDistributionChart'))
      this.loadDistributionData()

      // 初始化风险趋势图表
      this.trendChart = echarts.init(document.getElementById('riskTrendChart'))
      this.loadTrendData()
    },
    loadDistributionData() {
      getRiskDistributionData({}).then(response => {
        if (response.code === 1 && response.data) {
          const data = [
            { name: '高风险', value: response.data.highRisk || 0, itemStyle: { color: '#F56C6C' } },
            { name: '中风险', value: response.data.mediumRisk || 0, itemStyle: { color: '#E6A23C' } },
            { name: '低风险', value: response.data.lowRisk || 0, itemStyle: { color: '#67C23A' } }
          ]
          this.renderRiskChart(data)
        } else {
          // 使用默认数据
          this.renderRiskChart([
            { name: '高风险', value: 0, itemStyle: { color: '#F56C6C' } },
            { name: '中风险', value: 0, itemStyle: { color: '#E6A23C' } },
            { name: '低风险', value: 0, itemStyle: { color: '#67C23A' } }
          ])
        }
      }).catch(error => {
        console.error('获取风险分布数据失败:', error)
        this.renderRiskChart([
          { name: '高风险', value: 0, itemStyle: { color: '#F56C6C' } },
          { name: '中风险', value: 0, itemStyle: { color: '#E6A23C' } },
          { name: '低风险', value: 0, itemStyle: { color: '#67C23A' } }
        ])
      })
    },
    renderRiskChart(data) {
      let option = {}

      if (this.riskChartType === 'pie') {
        option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c}张 ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 10,
            data: data.map(item => item.name)
          },
          series: [
            {
              name: '风险等级',
              type: 'pie',
              radius: ['50%', '70%'],
              center: ['60%', '50%'],
              data: data
            }
          ]
        }
      } else {
        option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: data.map(item => item.name)
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '票据数量',
              type: 'bar',
              data: data.map(item => ({
                value: item.value,
                itemStyle: item.itemStyle
              }))
            }
          ]
        }
      }

      this.riskChart.setOption(option)
    },
    loadTrendData() {
      const days = this.trendPeriod === '7D' ? 7 : this.trendPeriod === '30D' ? 30 : 90
      getRiskTrendData({ days }).then(response => {
        if (response.code === 1 && response.data) {
          const dates = response.data.labels || this.generateDateLabels(days)
          const highRiskData = response.data.highRiskData || []
          const mediumRiskData = response.data.mediumRiskData || []
          const lowRiskData = response.data.lowRiskData || []
          this.renderTrendChart(dates, highRiskData, mediumRiskData, lowRiskData)
        } else {
          this.renderTrendChart(this.generateDateLabels(days), [], [], [])
        }
      }).catch(error => {
        console.error('获取趋势数据失败:', error)
        this.renderTrendChart(this.generateDateLabels(days), [], [], [])
      })
    },
    renderTrendChart(dates, highRiskData, mediumRiskData, lowRiskData) {
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['高风险', '中风险', '低风险']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: dates
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '高风险',
            type: 'line',
            data: highRiskData,
            itemStyle: { color: '#F56C6C' }
          },
          {
            name: '中风险',
            type: 'line',
            data: mediumRiskData,
            itemStyle: { color: '#E6A23C' }
          },
          {
            name: '低风险',
            type: 'line',
            data: lowRiskData,
            itemStyle: { color: '#67C23A' }
          }
        ]
      }

      this.trendChart.setOption(option)
    },
    loadAlerts() {
      // 调用后端API获取预警数据
      getBillRiskAlerts({}).then(response => {
        if (response.code === 1 && response.data) {
          this.alertList = response.data || []
        } else {
          this.alertList = []
        }
      }).catch(error => {
        console.error('获取预警数据失败:', error)
        this.alertList = []
      })
    },
    generateDateLabels(days) {
      const labels = []
      for (let i = days - 1; i >= 0; i--) {
        const date = new Date()
        date.setDate(date.getDate() - i)
        labels.push((date.getMonth() + 1) + '/' + date.getDate())
      }
      return labels
    },
    handleRiskChartTypeChange() {
      this.loadDistributionData()
    },
    handleTrendPeriodChange() {
      this.loadTrendData()
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        pageNum: 1,
        pageSize: 20,
        billNumber: undefined,
        riskLevel: undefined,
        riskType: undefined,
        assessmentStatus: undefined,
        assessmentDateRange: [],
        assessmentDateStart: undefined,
        assessmentDateEnd: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreateAssessment() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '新增风险评估'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleAssess(row) {
      // 从后端获取最新详情
      getBillRiskDetail(row.assessmentId).then(response => {
        if (response.code === 1 && response.data) {
          this.temp = Object.assign({}, response.data)
          // 将 riskFactors JSON 字符串转换为数组
          this.parseRiskFactors()
        } else {
          this.temp = Object.assign({}, row)
          this.parseRiskFactors()
        }
        this.dialogStatus = 'update'
        this.dialogTitle = '风险评估'
        this.dialogFormVisible = true
        this.activeFormTab = 'assessment'
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      }).catch(() => {
        this.temp = Object.assign({}, row)
        this.parseRiskFactors()
        this.dialogStatus = 'update'
        this.dialogTitle = '风险评估'
        this.dialogFormVisible = true
        this.activeFormTab = 'assessment'
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      })
    },
    // 解析 riskFactors JSON 字符串为数组
    parseRiskFactors() {
      if (this.temp.riskFactors && typeof this.temp.riskFactors === 'string') {
        try {
          this.temp.riskFactors = JSON.parse(this.temp.riskFactors)
        } catch (e) {
          this.temp.riskFactors = []
        }
      } else if (!Array.isArray(this.temp.riskFactors)) {
        this.temp.riskFactors = []
      }
    },
    handleViewDetail(row) {
      // 从后端获取最新详情
      getBillRiskDetail(row.assessmentId).then(response => {
        if (response.code === 1 && response.data) {
          this.currentRisk = response.data
        } else {
          this.currentRisk = row
        }
        this.dialogDetailVisible = true
      }).catch(() => {
        this.currentRisk = row
        this.dialogDetailVisible = true
      })
    },
    handleDispose(row) {
      this.currentRisk = row
      this.disposeForm = {
        assessmentId: row.assessmentId,
        billId: row.billId,
        disposeType: '',
        disposeDescription: '',
        expectedEffect: ''
      }
      this.dialogDisposeVisible = true
    },
    handleViewAlerts() {
      this.loadAlerts()
      this.dialogAlertsVisible = true
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'reassess':
          this.handleAssess(row)
          break
        case 'monitor':
          this.$message({
            type: 'success',
            message: '已加入风险监控列表'
          })
          break
        case 'history':
          // 调用后端API获取评估历史记录，优先使用 billNumber，其次使用 assessmentId
          const historyParam = row.billNumber || row.assessmentId
          if (!historyParam) {
            this.$message({ type: 'warning', message: '无法获取历史记录：缺少票据信息' })
            break
          }
          getRiskAssessmentHistory(historyParam).then(response => {
            if (response.code === 1) {
              const historyData = response.data || []
              if (historyData.length > 0) {
                this.$alert(
                  historyData.map(item => `${item.assessmentDate}: ${item.riskLevel} - ${item.assessorName}`).join('<br/>'),
                  '评估历史记录',
                  { dangerouslyUseHTMLString: true, confirmButtonText: '确定' }
                )
              } else {
                this.$message({ type: 'info', message: '暂无历史记录' })
              }
            } else {
              this.$message({ type: 'info', message: '暂无历史记录' })
            }
          }).catch(error => {
            console.error('获取历史记录失败:', error)
            this.$message({ type: 'error', message: '获取历史记录失败' })
          })
          break
      }
    },
    handleExport() {
      // 调用后端API导出数据
      const params = { ...this.listQuery }
      if (this.listQuery.assessmentDateRange && this.listQuery.assessmentDateRange.length === 2) {
        params.assessmentDateStart = this.formatDateParam(this.listQuery.assessmentDateRange[0])
        params.assessmentDateEnd = this.formatDateParam(this.listQuery.assessmentDateRange[1])
      }
      delete params.assessmentDateRange

      exportBillRisk(params).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '票据风险报告_' + new Date().getTime() + '.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
        this.$message({ type: 'success', message: '风险报告导出成功' })
      }).catch(error => {
        console.error('导出失败:', error)
        this.$message({ type: 'error', message: '导出失败，请稍后重试' })
      })
    },
    handleMarkAllResolved() {
      const pendingAlerts = this.alertList.filter(alert => alert.status === 'PENDING')
      if (pendingAlerts.length === 0) {
        this.$message({ type: 'info', message: '没有待处理的预警' })
        return
      }

      const alertIds = pendingAlerts.map(alert => alert.alertId)
      markAlertResolved({ alertIds }).then(response => {
        if (response.code === 1) {
          this.alertList.forEach(alert => {
            alert.status = 'RESOLVED'
          })
          this.$message({
            type: 'success',
            message: '所有预警已标记为已处理'
          })
        } else {
          this.$message({
            type: 'error',
            message: response.msg || '操作失败'
          })
        }
      }).catch(error => {
        console.error('标记预警失败:', error)
        this.$message({
          type: 'error',
          message: '操作失败，请稍后重试'
        })
      })
    },
    submitDispose() {
      if (!this.disposeForm.disposeType) {
        this.$message({
          type: 'warning',
          message: '请选择处置方式'
        })
        return
      }

      // 调用后端API处置风险
      disposeBillRisk({
        assessmentId: this.currentRisk.assessmentId,
        billId: this.currentRisk.billId,
        disposeType: this.disposeForm.disposeType,
        disposeDescription: this.disposeForm.disposeDescription,
        expectedEffect: this.disposeForm.expectedEffect
      }).then(response => {
        if (response.code === 1) {
          this.dialogDisposeVisible = false
          this.dialogDetailVisible = false
          this.$message({
            type: 'success',
            message: '风险处置完成!'
          })
          this.getList()
        } else {
          this.$message({
            type: 'error',
            message: response.msg || '处置失败'
          })
        }
      }).catch(error => {
        console.error('处置失败:', error)
        this.$message({
          type: 'error',
          message: '处置失败，请稍后重试'
        })
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const data = { ...this.temp }
          if (data.assessmentDate && typeof data.assessmentDate === 'object') {
            data.assessmentDate = this.formatDateParam(data.assessmentDate)
          }
          // 将 riskFactors 数组转换为 JSON 字符串
          if (Array.isArray(data.riskFactors)) {
            data.riskFactors = JSON.stringify(data.riskFactors)
          }

          createBillRiskAssessment(data).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$message({
                type: 'success',
                message: '风险评估创建成功'
              })
              this.getList()
              this.loadStatistics()
            } else {
              this.$message({
                type: 'error',
                message: response.msg || '创建失败'
              })
            }
          }).catch(error => {
            console.error('创建失败:', error)
            this.$message({
              type: 'error',
              message: '创建失败，请稍后重试'
            })
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const data = { ...this.temp }
          if (data.assessmentDate && typeof data.assessmentDate === 'object') {
            data.assessmentDate = this.formatDateParam(data.assessmentDate)
          }
          // 将 riskFactors 数组转换为 JSON 字符串
          if (Array.isArray(data.riskFactors)) {
            data.riskFactors = JSON.stringify(data.riskFactors)
          }

          updateBillRiskAssessment(data).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$message({
                type: 'success',
                message: '风险评估更新成功'
              })
              this.getList()
              this.loadStatistics()
            } else {
              this.$message({
                type: 'error',
                message: response.msg || '更新失败'
              })
            }
          }).catch(error => {
            console.error('更新失败:', error)
            this.$message({
              type: 'error',
              message: '更新失败，请稍后重试'
            })
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        assessmentId: undefined,
        billNumber: '',
        billAmount: 0,
        acceptorName: '',
        maturityDate: '',
        riskType: '',
        riskLevel: '',
        riskScore: 50,
        riskFactors: [],
        assessmentDate: null,
        assessorName: '当前用户',
        riskDescription: '',
        impactAnalysis: '',
        mitigationMeasures: '',
        monitoringSuggestions: ''
      }
    },
    getRiskLevelTagType(level) {
      const typeMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return typeMap[level] || 'info'
    },
    getRiskLevelText(level) {
      const textMap = {
        'HIGH': '高风险',
        'MEDIUM': '中风险',
        'LOW': '低风险'
      }
      return textMap[level] || level
    },
    getRiskLevelIcon(level) {
      const iconMap = {
        'HIGH': 'el-icon-error',
        'MEDIUM': 'el-icon-warning',
        'LOW': 'el-icon-success'
      }
      return iconMap[level] || ''
    },
    getRiskTypeTagType(type) {
      const typeMap = {
        'CREDIT': 'danger',
        'LIQUIDITY': 'warning',
        'OPERATIONAL': 'primary',
        'MARKET': 'info'
      }
      return typeMap[type] || 'info'
    },
    getRiskTypeText(type) {
      const textMap = {
        'CREDIT': '信用风险',
        'LIQUIDITY': '流动性风险',
        'OPERATIONAL': '操作风险',
        'MARKET': '市场风险'
      }
      return textMap[type] || type
    },
    getAssessmentStatusTagType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'ASSESSED': 'success',
        'REASSESS': 'primary',
        'DISPOSED': 'info'
      }
      return typeMap[status] || 'info'
    },
    getAssessmentStatusText(status) {
      const textMap = {
        'PENDING': '待评估',
        'ASSESSED': '已评估',
        'REASSESS': '需复评',
        'DISPOSED': '已处置'
      }
      return textMap[status] || status
    },
    getRiskScoreClass(score) {
      if (score >= 71) return 'high-risk-score'
      if (score >= 31) return 'medium-risk-score'
      return 'low-risk-score'
    },
    getAlertLevelTagType(level) {
      const typeMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return typeMap[level] || 'info'
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    },
    // 格式化日期：将时间戳或日期字符串转为 yyyy-MM-dd 格式
    formatDate(value) {
      if (!value) return '--'
      // 已经是 yyyy-MM-dd 格式，直接返回
      if (typeof value === 'string' && /^\d{4}-\d{2}-\d{2}$/.test(value)) {
        return value
      }
      // 包含日期的字符串（如 yyyy-MM-dd HH:mm:ss），截取日期部分
      if (typeof value === 'string' && /^\d{4}-\d{2}-\d{2}/.test(value)) {
        return value.substring(0, 10)
      }
      // 时间戳（数字），转为日期
      const date = new Date(value)
      if (isNaN(date.getTime())) return '--'
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
  }
}
</script>

<style lang="scss" scoped>
.bill-risk-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .risk-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.high-risk-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.medium-risk-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.amount-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          .card-change {
            font-size: 12px;
            color: #909399;
            &.danger {
              color: #F56C6C;
            }
            &.warning {
              color: #E6A23C;
            }
          }
        }
      }
    }
  }

  .chart-card, .search-card, .table-card {
    margin-bottom: 20px;
  }

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h3 {
      margin: 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .chart-container {
    height: 300px;
    width: 100%;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .bill-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .high-risk-score {
    color: #F56C6C;
    font-weight: 600;
  }

  .medium-risk-score {
    color: #E6A23C;
    font-weight: 600;
  }

  .low-risk-score {
    color: #67C23A;
    font-weight: 600;
  }

  .score-description {
    margin-top: 10px;
    display: flex;
    justify-content: space-between;
    
    .score-range {
      font-size: 12px;
      padding: 2px 8px;
      border-radius: 4px;
      
      &.low {
        background: #f0f9ff;
        color: #67C23A;
      }
      
      &.medium {
        background: #fdf6ec;
        color: #E6A23C;
      }
      
      &.high {
        background: #fef0f0;
        color: #F56C6C;
      }
    }
  }

  .risk-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }
    
    .risk-analysis {
      margin-top: 20px;
      
      h4 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 16px;
        font-weight: 600;
        border-left: 3px solid #409EFF;
        padding-left: 8px;
      }
      
      .analysis-section {
        margin-bottom: 16px;
        
        h5 {
          margin: 0 0 8px 0;
          color: #606266;
          font-size: 14px;
          font-weight: 600;
        }
        
        p {
          margin: 0;
          color: #606266;
          line-height: 1.6;
          padding: 8px 12px;
          background: #f8f9fa;
          border-radius: 4px;
        }
      }
    }
  }
}
</style>
