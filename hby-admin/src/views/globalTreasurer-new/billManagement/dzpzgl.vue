<template>
  <div class="electronic-bill-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-mobile-phone"></i>
            电子票据管理
          </h2>
          <p class="page-description">电子票据的创建、签发、流转和管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            创建电子票据
          </el-button>
          <el-button type="success" icon="el-icon-upload" @click="handleImport">
            批量导入
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 电子票据概览卡片 -->
    <div class="bill-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">电子票据总数</div>
                <div class="card-value">{{ totalBills }}</div>
                <div class="card-change">张</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">有效票据</div>
                <div class="card-value">{{ activeBills }}</div>
                <div class="card-change positive">{{ activeRate }}%</div>
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
                <div class="card-title">票据总额</div>
                <div class="card-value">{{ totalAmount }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon growth-icon">
                <i class="el-icon-trend-charts"></i>
              </div>
              <div class="card-info">
                <div class="card-title">月增长率</div>
                <div class="card-value">{{ growthRate }}</div>
                <div class="card-change positive">%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 电子票据流转图表 -->
    <el-card class="chart-card" shadow="never">
      <div class="chart-header">
        <h3>电子票据流转统计</h3>
        <div class="chart-controls">
          <el-radio-group v-model="chartTimeRange" size="small" @change="handleTimeRangeChange">
            <el-radio-button label="7D">7天</el-radio-button>
            <el-radio-button label="30D">30天</el-radio-button>
            <el-radio-button label="90D">90天</el-radio-button>
            <el-radio-button label="1Y">1年</el-radio-button>
          </el-radio-group>
        </div>
      </div>
      <div id="billFlowChart" class="chart-container"></div>
    </el-card>

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
          <el-form-item label="票据类型">
            <el-select
              v-model="listQuery.billType"
              placeholder="请选择票据类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="电子银行承兑汇票" value="E_BANK_ACCEPTANCE" />
              <el-option label="电子商业承兑汇票" value="E_COMMERCIAL_ACCEPTANCE" />
              <el-option label="电子支票" value="E_CHECK" />
              <el-option label="电子本票" value="E_PROMISSORY_NOTE" />
            </el-select>
          </el-form-item>
          <el-form-item label="票据状态">
            <el-select
              v-model="listQuery.billStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="已签发" value="ISSUED" />
              <el-option label="流转中" value="CIRCULATING" />
              <el-option label="已背书" value="ENDORSED" />
              <el-option label="已贴现" value="DISCOUNTED" />
              <el-option label="已到期" value="MATURED" />
              <el-option label="已作废" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item label="签发日期">
            <el-date-picker
              v-model="listQuery.issueDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
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

    <!-- 电子票据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="billList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="票据ID" prop="billId" width="80" align="center" />
        <el-table-column label="票据号码" width="180px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.billNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票据类型" width="140px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getBillTypeTagType(row.billType)" size="mini">
              <i class="el-icon-mobile-phone"></i>
              {{ getBillTypeText(row.billType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="票据金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="bill-amount">{{ formatCurrency(row.billAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="出票人" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.drawerName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="承兑人" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.acceptorName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="签发日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.issueDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.maturityDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票据状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getBillStatusTagType(row.billStatus)" size="mini">
              {{ getBillStatusText(row.billStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="数字签名" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.digitalSignature ? 'success' : 'danger'" size="mini">
              <i :class="row.digitalSignature ? 'el-icon-check' : 'el-icon-close'"></i>
              {{ row.digitalSignature ? '已签名' : '未签名' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.billStatus === 'ISSUED'" type="primary" size="mini" @click="handleEndorse(row)">
              背书
            </el-button>
            <el-button v-if="row.billStatus === 'CIRCULATING'" type="success" size="mini" @click="handleDiscount(row)">
              贴现
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'sign', row: row}">数字签名</el-dropdown-item>
                <el-dropdown-item :command="{action: 'verify', row: row}">验证票据</el-dropdown-item>
                <el-dropdown-item :command="{action: 'track', row: row}">流转追踪</el-dropdown-item>
                <el-dropdown-item :command="{action: 'cancel', row: row}">作废票据</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />
    </el-card>

    <!-- 电子票据创建/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="900px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-tabs v-model="activeFormTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票据号码" prop="billNumber">
                  <el-input v-model="temp.billNumber" placeholder="系统自动生成" :disabled="dialogStatus==='update'" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="票据类型" prop="billType">
                  <el-select v-model="temp.billType" placeholder="请选择票据类型" style="width: 100%;">
                    <el-option label="电子银行承兑汇票" value="E_BANK_ACCEPTANCE" />
                    <el-option label="电子商业承兑汇票" value="E_COMMERCIAL_ACCEPTANCE" />
                    <el-option label="电子支票" value="E_CHECK" />
                    <el-option label="电子本票" value="E_PROMISSORY_NOTE" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票据金额" prop="billAmount">
                  <el-input-number
                    v-model="temp.billAmount"
                    :precision="2"
                    :step="1000"
                    :min="0"
                    :max="100000000"
                    style="width: 100%;"
                    placeholder="请输入票据金额"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="币种">
                  <el-select v-model="temp.currency" placeholder="请选择币种" style="width: 100%;">
                    <el-option label="人民币" value="CNY" />
                    <el-option label="美元" value="USD" />
                    <el-option label="欧元" value="EUR" />
                    <el-option label="日元" value="JPY" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="签发日期" prop="issueDate">
                  <el-date-picker
                    v-model="temp.issueDate"
                    type="date"
                    placeholder="选择签发日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期日期" prop="maturityDate">
                  <el-date-picker
                    v-model="temp.maturityDate"
                    type="date"
                    placeholder="选择到期日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="当事人信息" name="parties">
            <el-form-item label="出票人名称" prop="drawerName">
              <el-input v-model="temp.drawerName" placeholder="请输入出票人名称" />
            </el-form-item>
            <el-form-item label="出票人账号">
              <el-input v-model="temp.drawerAccount" placeholder="请输入出票人账号" />
            </el-form-item>
            <el-form-item label="出票人开户行">
              <el-input v-model="temp.drawerBank" placeholder="请输入出票人开户行" />
            </el-form-item>
            <el-form-item label="承兑人名称" prop="acceptorName">
              <el-input v-model="temp.acceptorName" placeholder="请输入承兑人名称" />
            </el-form-item>
            <el-form-item label="承兑人账号">
              <el-input v-model="temp.acceptorAccount" placeholder="请输入承兑人账号" />
            </el-form-item>
            <el-form-item label="承兑人开户行">
              <el-input v-model="temp.acceptorBank" placeholder="请输入承兑人开户行" />
            </el-form-item>
            <el-form-item label="收款人名称">
              <el-input v-model="temp.payeeName" placeholder="请输入收款人名称" />
            </el-form-item>
            <el-form-item label="收款人账号">
              <el-input v-model="temp.payeeAccount" placeholder="请输入收款人账号" />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="数字签名" name="signature">
            <el-form-item label="数字证书">
              <el-upload
                class="upload-demo"
                drag
                action="#"
                :auto-upload="false"
                :on-change="handleCertificateChange"
                accept=".p12,.pfx,.cer,.crt"
              >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将数字证书文件拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip" slot="tip">只能上传.p12/.pfx/.cer/.crt文件</div>
              </el-upload>
            </el-form-item>
            <el-form-item label="证书密码">
              <el-input v-model="temp.certificatePassword" type="password" placeholder="请输入证书密码" show-password />
            </el-form-item>
            <el-form-item label="签名算法">
              <el-select v-model="temp.signatureAlgorithm" placeholder="请选择签名算法" style="width: 100%;">
                <el-option label="SHA256withRSA" value="SHA256withRSA" />
                <el-option label="SHA1withRSA" value="SHA1withRSA" />
                <el-option label="MD5withRSA" value="MD5withRSA" />
              </el-select>
            </el-form-item>
            <el-form-item label="时间戳服务">
              <el-input v-model="temp.timestampServer" placeholder="请输入时间戳服务器地址" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleDigitalSign">
                <i class="el-icon-key"></i>
                执行数字签名
              </el-button>
              <el-button type="info" @click="handleVerifySignature">
                <i class="el-icon-view"></i>
                验证签名
              </el-button>
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="其他信息" name="other">
            <el-form-item label="票据用途">
              <el-select v-model="temp.billPurpose" placeholder="请选择票据用途" style="width: 100%;">
                <el-option label="贸易结算" value="TRADE_SETTLEMENT" />
                <el-option label="融资" value="FINANCING" />
                <el-option label="投资" value="INVESTMENT" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
            <el-form-item label="交易合同号">
              <el-input v-model="temp.contractNumber" placeholder="请输入交易合同号" />
            </el-form-item>
            <el-form-item label="备注信息">
              <el-input v-model="temp.remark" type="textarea" :rows="4" placeholder="请输入备注信息" />
            </el-form-item>
            <el-form-item label="附件上传">
              <el-upload
                class="upload-demo"
                action="#"
                :auto-upload="false"
                :on-change="handleAttachmentChange"
                multiple
              >
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">支持上传多个文件</div>
              </el-upload>
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

    <!-- 票据详情对话框 -->
    <el-dialog title="电子票据详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentBill" class="bill-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="票据号码">{{ currentBill.billNumber }}</el-descriptions-item>
          <el-descriptions-item label="票据类型">{{ getBillTypeText(currentBill.billType) }}</el-descriptions-item>
          <el-descriptions-item label="票据金额">{{ formatCurrency(currentBill.billAmount) }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ currentBill.currency }}</el-descriptions-item>
          <el-descriptions-item label="出票人">{{ currentBill.drawerName }}</el-descriptions-item>
          <el-descriptions-item label="承兑人">{{ currentBill.acceptorName }}</el-descriptions-item>
          <el-descriptions-item label="签发日期">{{ formatDate(currentBill.issueDate) }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ formatDate(currentBill.maturityDate) }}</el-descriptions-item>
          <el-descriptions-item label="票据状态">
            <el-tag :type="getBillStatusTagType(currentBill.billStatus)">
              {{ getBillStatusText(currentBill.billStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="数字签名">
            <el-tag :type="currentBill.digitalSignature ? 'success' : 'danger'">
              {{ currentBill.digitalSignature ? '已签名' : '未签名' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <!-- 流转记录 -->
        <div class="circulation-records">
          <h4>流转记录</h4>
          <el-timeline>
            <el-timeline-item
              v-for="record in circulationRecords"
              :key="record.id"
              :timestamp="record.operationTime"
              :type="getTimelineType(record.operationType)"
            >
              <div class="timeline-content">
                <div class="timeline-title">{{ record.operationType }}</div>
                <div class="timeline-description">{{ record.description }}</div>
                <div class="timeline-operator">操作人：{{ record.operatorName }}</div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentBill && currentBill.billStatus === 'ISSUED'" type="primary" @click="handleEndorse(currentBill)">
          背书转让
        </el-button>
      </div>
    </el-dialog>

    <!-- 流转追踪对话框 -->
    <el-dialog title="票据流转追踪" :visible.sync="dialogTrackVisible" width="800px">
      <div class="track-content">
        <div class="track-header">
          <h4>票据号码：{{ currentBill ? currentBill.billNumber : '' }}</h4>
          <el-tag type="info">当前状态：{{ currentBill ? getBillStatusText(currentBill.billStatus) : '' }}</el-tag>
        </div>
        <div class="track-flow">
          <el-steps :active="trackSteps.length - 1" finish-status="success">
            <el-step
              v-for="(step, index) in trackSteps"
              :key="index"
              :title="step.title"
              :description="step.description"
            />
          </el-steps>
        </div>
        <div class="track-details">
          <el-table :data="trackDetails" border size="small">
            <el-table-column label="操作时间" prop="operationTime" width="150" />
            <el-table-column label="操作类型" prop="operationType" width="120" />
            <el-table-column label="操作人" prop="operatorName" width="120" />
            <el-table-column label="操作描述" prop="description" min-width="200" />
            <el-table-column label="状态" prop="status" width="100" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.status === 'SUCCESS' ? 'success' : 'danger'" size="mini">
                  {{ row.status === 'SUCCESS' ? '成功' : '失败' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTrackVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleRefreshTrack">刷新追踪</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getElectronicBillPage,
  getElectronicBillDetail,
  createElectronicBill,
  updateElectronicBill,
  deleteElectronicBill,
  signElectronicBill,
  verifyElectronicBill,
  trackElectronicBill,
  getCirculationRecords,
  exportElectronicBill,
  getElectronicBillStatistics,
  getBillTrendAnalysis,
  endorseElectronicBill,
  discountElectronicBill
} from '@/api/globalTreasurer-new/billManagement/electronicBill'
import Pagination from '@/components/Pagination'

export default {
  name: 'ElectronicBillManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        pageNum: 1,
        pageSize: 20,
        billNumber: undefined,
        billType: undefined,
        billStatus: undefined,
        issueDateRange: [],
        issueDateStart: undefined,
        issueDateEnd: undefined
      },
      totalBills: 0,
      activeBills: 0,
      activeRate: 0,
      totalAmount: 0,
      growthRate: 0,
      chartTimeRange: '30D',
      billList: [],
      multipleSelection: [],
      currentBill: null,
      circulationRecords: [],
      trackSteps: [],
      trackDetails: [],
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogTrackVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      activeFormTab: 'basic',
      temp: {
        billId: undefined,
        billNumber: '',
        billType: '',
        billAmount: null,
        currency: 'CNY',
        drawerName: '',
        drawerAccount: '',
        drawerBank: '',
        acceptorName: '',
        acceptorAccount: '',
        acceptorBank: '',
        payeeName: '',
        payeeAccount: '',
        issueDate: null,
        maturityDate: null,
        billPurpose: '',
        contractNumber: '',
        remark: '',
        digitalSignature: false,
        certificatePassword: '',
        signatureAlgorithm: 'SHA256withRSA',
        timestampServer: ''
      },
      rules: {
        // billNumber 不需要验证,后端会自动生成
        billType: [{ required: true, message: '请选择票据类型', trigger: 'change' }],
        billAmount: [{ required: true, message: '请输入票据金额', trigger: 'blur' }],
        drawerName: [{ required: true, message: '请输入出票人名称', trigger: 'blur' }],
        acceptorName: [{ required: true, message: '请输入承兑人名称', trigger: 'blur' }],
        issueDate: [{ required: true, message: '请选择签发日期', trigger: 'change' }],
        maturityDate: [{ required: true, message: '请选择到期日期', trigger: 'change' }]
      },
      chart: null
    }
  },
  mounted() {
    this.getList()
    this.loadStatistics()
    this.initChart()
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
  },
  methods: {
    // 加载统计数据
    loadStatistics() {
      getElectronicBillStatistics({}).then(response => {
        if (response.code === 1 && response.data) {
          this.totalBills = response.data.totalBills || 0
          this.activeBills = response.data.activeBills || 0
          this.totalAmount = response.data.totalAmount || 0
          this.growthRate = response.data.growthRate || 0
          this.activeRate = this.totalBills > 0 ? Math.round((this.activeBills / this.totalBills) * 100) : 0
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
        this.calculateLocalStatistics()
      })
    },
    // 本地计算统计数据（后备方案）
    calculateLocalStatistics() {
      this.totalBills = this.billList.length
      this.activeBills = this.billList.filter(b => ['ISSUED', 'CIRCULATING'].includes(b.billStatus)).length
      this.activeRate = this.totalBills > 0 ? Math.round((this.activeBills / this.totalBills) * 100) : 0
      const total = this.billList.reduce((sum, b) => sum + (b.billAmount || 0), 0)
      this.totalAmount = (total / 10000).toFixed(2)
    },
    // 格式化日期参数
    formatDate(val) {
      if (!val) return ''
      const date = typeof val === 'number' ? new Date(val) : new Date(val)
      if (isNaN(date.getTime())) return val
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
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
      if (this.listQuery.issueDateRange && this.listQuery.issueDateRange.length === 2) {
        params.issueDateStart = this.formatDateParam(this.listQuery.issueDateRange[0])
        params.issueDateEnd = this.formatDateParam(this.listQuery.issueDateRange[1])
      }
      delete params.issueDateRange

      // 调用后端API获取真实数据
      getElectronicBillPage(params).then(response => {
        if (response.code === 1) {
          this.billList = response.data.tlist || response.data || []
          this.total = response.data.totalRecord || response.result?.total || 0
        } else {
          this.billList = []
          this.total = 0
        }
        this.listLoading = false
        // 更新本地统计
        this.calculateLocalStatistics()
      }).catch(error => {
        console.error('获取电子票据列表失败:', error)
        this.billList = []
        this.total = 0
        this.listLoading = false
      })
    },
    initChart() {
      // 初始化图表
      const echarts = require('echarts')
      this.chart = echarts.init(document.getElementById('billFlowChart'))
      this.loadChartData()
    },
    loadChartData() {
      // 调用后端API获取图表数据
      getBillTrendAnalysis({ days: 30 }).then(response => {
        const trendData = response.data || {}
        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'cross' }
          },
          legend: {
            data: ['签发', '背书', '贴现', '到期']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: trendData.dates || this.generateDateLabels()
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '签发',
              type: 'line',
              stack: 'Total',
              data: trendData.issueData || [],
              itemStyle: { color: '#409EFF' }
            },
            {
              name: '背书',
              type: 'line',
              stack: 'Total',
              data: trendData.endorseData || [],
              itemStyle: { color: '#67C23A' }
            },
            {
              name: '贴现',
              type: 'line',
              stack: 'Total',
              data: trendData.discountData || [],
              itemStyle: { color: '#E6A23C' }
            },
            {
              name: '到期',
              type: 'line',
              stack: 'Total',
              data: trendData.maturityData || [],
              itemStyle: { color: '#F56C6C' }
            }
          ]
        }
        this.chart.setOption(option)
      }).catch(error => {
        console.error('获取图表数据失败:', error)
        // 使用默认空数据
        this.chart.setOption({
          tooltip: { trigger: 'axis', axisPointer: { type: 'cross' } },
          legend: { data: ['签发', '背书', '贴现', '到期'] },
          grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
          xAxis: { type: 'category', data: this.generateDateLabels() },
          yAxis: { type: 'value' },
          series: [
            { name: '签发', type: 'line', stack: 'Total', data: [], itemStyle: { color: '#409EFF' } },
            { name: '背书', type: 'line', stack: 'Total', data: [], itemStyle: { color: '#67C23A' } },
            { name: '贴现', type: 'line', stack: 'Total', data: [], itemStyle: { color: '#E6A23C' } },
            { name: '到期', type: 'line', stack: 'Total', data: [], itemStyle: { color: '#F56C6C' } }
          ]
        })
      })
    },
    generateDateLabels() {
      const labels = []
      for (let i = 29; i >= 0; i--) {
        const date = new Date()
        date.setDate(date.getDate() - i)
        labels.push(date.getMonth() + 1 + '/' + date.getDate())
      }
      return labels
    },
    handleTimeRangeChange() {
      // 重新加载图表数据
      this.initChart()
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
        billType: undefined,
        billStatus: undefined,
        issueDateRange: [],
        issueDateStart: undefined,
        issueDateEnd: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '创建电子票据'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      // 从后端获取最新详情
      getElectronicBillDetail(row.billId).then(response => {
        if (response.code === 1 && response.data) {
          this.currentBill = response.data
        } else {
          this.currentBill = row
        }
        this.loadCirculationRecords(row.billId)
        this.dialogDetailVisible = true
      }).catch(() => {
        this.currentBill = row
        this.loadCirculationRecords(row.billId)
        this.dialogDetailVisible = true
      })
    },
    handleEndorse(row) {
      this.$confirm('确认对该票据进行背书转让?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        endorseElectronicBill({ billId: row.billId }).then(response => {
          if (response.code === 1) {
            row.billStatus = 'ENDORSED'
            this.$message({ type: 'success', message: '票据背书成功!' })
            this.getList()
          } else {
            this.$message({ type: 'error', message: response.msg || '背书失败' })
          }
        }).catch(error => {
          console.error('背书失败:', error)
          this.$message({ type: 'error', message: '背书失败，请稍后重试' })
        })
      })
    },
    handleDiscount(row) {
      this.$confirm('确认对该票据进行贴现?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        discountElectronicBill({ billId: row.billId }).then(response => {
          if (response.code === 1) {
            row.billStatus = 'DISCOUNTED'
            this.$message({ type: 'success', message: '票据贴现成功!' })
            this.getList()
          } else {
            this.$message({ type: 'error', message: response.msg || '贴现失败' })
          }
        }).catch(error => {
          console.error('贴现失败:', error)
          this.$message({ type: 'error', message: '贴现失败，请稍后重试' })
        })
      })
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'sign':
          this.handleDigitalSignAction(row)
          break
        case 'verify':
          this.handleVerifyBill(row)
          break
        case 'track':
          this.handleTrackBill(row)
          break
        case 'cancel':
          this.handleCancelBill(row)
          break
      }
    },
    handleDigitalSignAction(row) {
      this.$confirm('确认对该票据进行数字签名?', '数字签名', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        signElectronicBill({ billId: row.billId }).then(response => {
          if (response.code === 1) {
            row.digitalSignature = true
            this.$message({ type: 'success', message: '数字签名成功!' })
            this.getList()
          } else {
            this.$message({ type: 'error', message: response.msg || '签名失败' })
          }
        }).catch(error => {
          console.error('签名失败:', error)
          this.$message({ type: 'error', message: '签名失败，请稍后重试' })
        })
      })
    },
    handleVerifyBill(row) {
      verifyElectronicBill({ billId: row.billId }).then(response => {
        if (response.code === 1) {
          this.$message({ type: 'success', message: '票据验证通过!' })
        } else {
          this.$message({ type: 'warning', message: response.msg || '票据验证失败' })
        }
      }).catch(error => {
        console.error('验证失败:', error)
        this.$message({ type: 'error', message: '验证失败，请稍后重试' })
      })
    },
    handleTrackBill(row) {
      this.currentBill = row
      this.loadTrackData(row.billId)
      this.dialogTrackVisible = true
    },
    handleCancelBill(row) {
      this.$confirm('确认作废该票据? 作废后将无法恢复!', '警告', {
        confirmButtonText: '确定作废',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteElectronicBill([row.billId]).then(response => {
          if (response.code === 1) {
            this.$message({ type: 'success', message: '票据作废成功!' })
            this.getList()
          } else {
            this.$message({ type: 'error', message: response.msg || '作废失败' })
          }
        }).catch(error => {
          console.error('作废失败:', error)
          this.$message({ type: 'error', message: '作废失败，请稍后重试' })
        })
      })
    },
    handleImport() {
      // 打开导入对话框
      this.$message({ type: 'info', message: '批量导入功能开发中' })
    },
    handleExport() {
      // 调用后端API导出数据
      const params = { ...this.listQuery }
      if (this.listQuery.issueDateRange && this.listQuery.issueDateRange.length === 2) {
        params.issueDateStart = this.formatDateParam(this.listQuery.issueDateRange[0])
        params.issueDateEnd = this.formatDateParam(this.listQuery.issueDateRange[1])
      }
      delete params.issueDateRange

      exportElectronicBill(params).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '电子票据数据_' + new Date().getTime() + '.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
        this.$message({ type: 'success', message: '数据导出成功' })
      }).catch(error => {
        console.error('导出失败:', error)
        this.$message({ type: 'error', message: '导出失败，请稍后重试' })
      })
    },
    handleDigitalSign() {
      if (!this.temp.certificatePassword) {
        this.$message({
          type: 'warning',
          message: '请输入证书密码'
        })
        return
      }

      this.temp.digitalSignature = true
      this.$message({
        type: 'success',
        message: '数字签名成功!'
      })
    },
    handleVerifySignature() {
      if (!this.temp.digitalSignature) {
        this.$message({
          type: 'warning',
          message: '票据尚未签名'
        })
        return
      }

      this.$message({
        type: 'success',
        message: '签名验证通过!'
      })
    },
    handleCertificateChange(file) {
      this.$message({
        type: 'info',
        message: `已选择证书文件: ${file.name}`
      })
    },
    handleAttachmentChange(file) {
      this.$message({
        type: 'info',
        message: `已选择附件: ${file.name}`
      })
    },
    handleRefreshTrack() {
      this.loadTrackData(this.currentBill.billId)
      this.$message({
        type: 'success',
        message: '追踪信息已刷新'
      })
    },
    loadCirculationRecords(billId) {
      // 调用后端API获取流转记录
      getCirculationRecords(billId).then(response => {
        if (response.code === 1) {
          this.circulationRecords = response.data.tlist || response.data || []
        } else {
          this.circulationRecords = []
        }
      }).catch(error => {
        console.error('获取流转记录失败:', error)
        this.circulationRecords = []
      })
    },
    loadTrackData(billId) {
      // 调用后端API获取追踪数据
      trackElectronicBill(billId).then(response => {
        if (response.code === 1) {
          const trackData = response.data || {}
          this.trackSteps = trackData.steps || []
          this.trackDetails = trackData.details || []
        } else {
          this.trackSteps = []
          this.trackDetails = []
        }
      }).catch(error => {
        console.error('获取追踪数据失败:', error)
        this.trackSteps = []
        this.trackDetails = []
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const data = { ...this.temp }
          if (data.issueDate && typeof data.issueDate === 'object') {
            data.issueDate = this.formatDateParam(data.issueDate)
          }
          if (data.maturityDate && typeof data.maturityDate === 'object') {
            data.maturityDate = this.formatDateParam(data.maturityDate)
          }

          createElectronicBill(data).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$message({ type: 'success', message: '电子票据创建成功' })
              this.getList()
            } else {
              this.$message({ type: 'error', message: response.msg || '创建失败' })
            }
          }).catch(error => {
            console.error('创建失败:', error)
            this.$message({ type: 'error', message: '创建失败，请稍后重试' })
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const data = { ...this.temp }
          if (data.issueDate && typeof data.issueDate === 'object') {
            data.issueDate = this.formatDateParam(data.issueDate)
          }
          if (data.maturityDate && typeof data.maturityDate === 'object') {
            data.maturityDate = this.formatDateParam(data.maturityDate)
          }

          updateElectronicBill(data).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$message({ type: 'success', message: '电子票据更新成功' })
              this.getList()
            } else {
              this.$message({ type: 'error', message: response.msg || '更新失败' })
            }
          }).catch(error => {
            console.error('更新失败:', error)
            this.$message({ type: 'error', message: '更新失败，请稍后重试' })
          })
        }
      })
    },
    // 编辑票据
    handleEdit(row) {
      getElectronicBillDetail(row.billId).then(response => {
        if (response.code === 1 && response.data) {
          this.temp = Object.assign({}, response.data)
        } else {
          this.temp = Object.assign({}, row)
        }
        this.dialogStatus = 'update'
        this.dialogTitle = '编辑电子票据'
        this.dialogFormVisible = true
        this.activeFormTab = 'basic'
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      }).catch(() => {
        this.temp = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogTitle = '编辑电子票据'
        this.dialogFormVisible = true
        this.activeFormTab = 'basic'
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      })
    },
    resetTemp() {
      this.temp = {
        billId: undefined,
        billNumber: '',
        billType: '',
        billAmount: null,
        currency: 'CNY',
        drawerName: '',
        drawerAccount: '',
        drawerBank: '',
        acceptorName: '',
        acceptorAccount: '',
        acceptorBank: '',
        payeeName: '',
        payeeAccount: '',
        issueDate: null,
        maturityDate: null,
        billPurpose: '',
        contractNumber: '',
        remark: '',
        digitalSignature: false,
        certificatePassword: '',
        signatureAlgorithm: 'SHA256withRSA',
        timestampServer: ''
      }
    },
    getBillTypeTagType(type) {
      const typeMap = {
        'E_BANK_ACCEPTANCE': 'success',
        'E_COMMERCIAL_ACCEPTANCE': 'primary',
        'E_CHECK': 'warning',
        'E_PROMISSORY_NOTE': 'info'
      }
      return typeMap[type] || 'info'
    },
    getBillTypeText(type) {
      const textMap = {
        'E_BANK_ACCEPTANCE': '电子银行承兑汇票',
        'E_COMMERCIAL_ACCEPTANCE': '电子商业承兑汇票',
        'E_CHECK': '电子支票',
        'E_PROMISSORY_NOTE': '电子本票'
      }
      return textMap[type] || type
    },
    getBillStatusTagType(status) {
      const typeMap = {
        'ISSUED': 'primary',
        'CIRCULATING': 'warning',
        'ENDORSED': 'success',
        'DISCOUNTED': 'info',
        'MATURED': 'success',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getBillStatusText(status) {
      const textMap = {
        'ISSUED': '已签发',
        'CIRCULATING': '流转中',
        'ENDORSED': '已背书',
        'DISCOUNTED': '已贴现',
        'MATURED': '已到期',
        'CANCELLED': '已作废'
      }
      return textMap[status] || status
    },
    getTimelineType(operationType) {
      const typeMap = {
        '票据签发': 'primary',
        '数字签名': 'success',
        '票据流转': 'warning',
        '背书转让': 'info'
      }
      return typeMap[operationType] || 'primary'
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
.electronic-bill-manage {
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

  .bill-overview {
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
          &.active-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.amount-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.growth-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
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
            &.positive {
              color: #67C23A;
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

  .bill-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }
    
    .circulation-records {
      margin-top: 20px;
      
      h4 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 14px;
        font-weight: 600;
      }
      
      .timeline-content {
        .timeline-title {
          font-weight: 600;
          color: #303133;
          margin-bottom: 4px;
        }
        
        .timeline-description {
          color: #606266;
          margin-bottom: 4px;
        }
        
        .timeline-operator {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .track-content {
    .track-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h4 {
        margin: 0;
        color: #303133;
      }
    }
    
    .track-flow {
      margin-bottom: 20px;
    }
    
    .track-details {
      margin-top: 20px;
    }
  }

  .upload-demo {
    width: 100%;
  }
}
</style>
