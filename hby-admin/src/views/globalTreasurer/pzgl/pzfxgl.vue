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
            <span>{{ row.acceptorName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="评估日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.assessmentDate }}</span>
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

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
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
                <el-form-item label="票据金额">
                  <el-input v-model="temp.billAmount" :disabled="true" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="承兑人">
                  <el-input v-model="temp.acceptorName" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期日期">
                  <el-input v-model="temp.maturityDate" :disabled="true" />
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
                  <el-input v-model="temp.assessorName" placeholder="当前用户" :disabled="true" />
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
          <el-descriptions-item label="到期日期">{{ currentRisk.maturityDate }}</el-descriptions-item>
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
          <el-descriptions-item label="评估日期">{{ currentRisk.assessmentDate }}</el-descriptions-item>
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
import { getBillRiskPage, createBillRiskAssessment, getBillRiskAlerts } from '@/api/globalTreasurer/pzgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'BillRiskManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        billNumber: undefined,
        riskLevel: undefined,
        riskType: undefined,
        assessmentStatus: undefined
      },
      riskBills: 45,
      highRiskBills: 8,
      mediumRiskBills: 23,
      riskAmount: 8560.5,
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
        billAmount: '',
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
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.riskList = [
          {
            assessmentId: 1,
            billNumber: 'BA20240925001',
            billAmount: 1000000.00,
            acceptorName: '中国工商银行',
            maturityDate: '2024-12-25',
            riskType: 'CREDIT',
            riskLevel: 'HIGH',
            riskScore: 85,
            assessmentDate: '2024-09-25',
            assessmentStatus: 'ASSESSED',
            assessorName: '张三',
            riskDescription: '承兑人信用评级下降，存在较高信用风险',
            impactAnalysis: '可能导致到期无法兑付，造成资金损失',
            mitigationMeasures: '建议提前托收或寻求担保',
            monitoringSuggestions: '密切关注承兑人财务状况变化'
          },
          {
            assessmentId: 2,
            billNumber: 'CA20240920002',
            billAmount: 500000.00,
            acceptorName: '客户B公司',
            maturityDate: '2024-10-20',
            riskType: 'LIQUIDITY',
            riskLevel: 'MEDIUM',
            riskScore: 65,
            assessmentDate: '2024-09-20',
            assessmentStatus: 'ASSESSED',
            assessorName: '李四',
            riskDescription: '承兑人现金流紧张，存在流动性风险',
            impactAnalysis: '可能影响票据正常兑付',
            mitigationMeasures: '加强监控，必要时采取保全措施',
            monitoringSuggestions: '定期跟踪承兑人资金状况'
          }
        ]
        this.total = this.riskList.length
        this.listLoading = false
      }, 1000)
    },
    initCharts() {
      const echarts = require('echarts')
      
      // 初始化风险分布图表
      this.riskChart = echarts.init(document.getElementById('riskDistributionChart'))
      this.updateRiskChart()
      
      // 初始化风险趋势图表
      this.trendChart = echarts.init(document.getElementById('riskTrendChart'))
      this.updateTrendChart()
    },
    updateRiskChart() {
      const data = [
        { name: '高风险', value: 8, itemStyle: { color: '#F56C6C' } },
        { name: '中风险', value: 23, itemStyle: { color: '#E6A23C' } },
        { name: '低风险', value: 14, itemStyle: { color: '#67C23A' } }
      ]
      
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
    updateTrendChart() {
      const dates = this.generateDateLabels(30)
      const highRiskData = this.generateMockData(30, 0, 5)
      const mediumRiskData = this.generateMockData(30, 5, 15)
      const lowRiskData = this.generateMockData(30, 3, 10)
      
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
      // 模拟预警数据
      this.alertList = [
        {
          alertTime: '2024-09-25 18:15:00',
          alertLevel: 'HIGH',
          billNumber: 'BA20240925001',
          alertType: '信用风险',
          alertMessage: '承兑人信用评级下降至BB级',
          status: 'PENDING'
        },
        {
          alertTime: '2024-09-25 17:30:00',
          alertLevel: 'MEDIUM',
          billNumber: 'CA20240920002',
          alertType: '流动性风险',
          alertMessage: '承兑人现金流出现紧张',
          status: 'RESOLVED'
        }
      ]
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
    generateMockData(count, min, max) {
      const data = []
      for (let i = 0; i < count; i++) {
        data.push(Math.floor(Math.random() * (max - min) + min))
      }
      return data
    },
    handleRiskChartTypeChange() {
      this.updateRiskChart()
    },
    handleTrendPeriodChange() {
      this.updateTrendChart()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        billNumber: undefined,
        riskLevel: undefined,
        riskType: undefined,
        assessmentStatus: undefined
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
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '风险评估'
      this.dialogFormVisible = true
      this.activeFormTab = 'assessment'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentRisk = row
      this.dialogDetailVisible = true
    },
    handleDispose(row) {
      this.currentRisk = row
      this.disposeForm = {
        disposeType: '',
        disposeDescription: '',
        expectedEffect: ''
      }
      this.dialogDisposeVisible = true
    },
    handleViewAlerts() {
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
          this.$message({
            type: 'info',
            message: '查看评估历史功能'
          })
          break
      }
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '风险报告导出成功'
      })
    },
    handleMarkAllResolved() {
      this.alertList.forEach(alert => {
        alert.status = 'RESOLVED'
      })
      this.$message({
        type: 'success',
        message: '所有预警已标记为已处理'
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
      
      this.currentRisk.assessmentStatus = 'DISPOSED'
      this.dialogDisposeVisible = false
      this.dialogDetailVisible = false
      
      this.$message({
        type: 'success',
        message: '风险处置完成!'
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.assessmentId = parseInt(Math.random() * 100) + 1024
          this.temp.assessmentStatus = 'ASSESSED'
          if (this.temp.assessmentDate) {
            this.temp.assessmentDate = this.temp.assessmentDate.toISOString().slice(0, 10)
          }
          this.riskList.unshift(this.temp)
          this.total = this.riskList.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '风险评估创建成功'
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.temp.assessmentDate && typeof this.temp.assessmentDate === 'object') {
            this.temp.assessmentDate = this.temp.assessmentDate.toISOString().slice(0, 10)
          }
          const index = this.riskList.findIndex(v => v.assessmentId === this.temp.assessmentId)
          this.riskList.splice(index, 1, this.temp)
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '风险评估更新成功'
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        assessmentId: undefined,
        billNumber: '',
        billAmount: '',
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
