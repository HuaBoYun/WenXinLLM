<template>
  <div class="bill-maturity-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-alarm-clock"></i>
            票据到期管理
          </h2>
          <p class="page-description">管理票据到期提醒、处理和统计分析</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-bell" @click="handleSendReminder">
            发送提醒
          </el-button>
          <el-button type="success" icon="el-icon-check" @click="handleBatchProcess">
            批量处理
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 到期概览卡片 -->
    <div class="maturity-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总票据数</div>
                <div class="card-value">{{ totalBills }}</div>
                <div class="card-change">张票据</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon expiring-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">即将到期</div>
                <div class="card-value">{{ expiringBills }}</div>
                <div class="card-change warning">30天内</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon matured-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已到期</div>
                <div class="card-value">{{ maturedBills }}</div>
                <div class="card-change negative">需处理</div>
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
                <div class="card-title">到期金额</div>
                <div class="card-value">{{ totalAmount }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 到期提醒图表 -->
    <el-card class="chart-card" shadow="never">
      <div class="chart-header">
        <h3>到期趋势分析</h3>
        <div class="chart-controls">
          <el-radio-group v-model="chartTimeRange" size="small" @change="handleTimeRangeChange">
            <el-radio-button label="7D">7天</el-radio-button>
            <el-radio-button label="30D">30天</el-radio-button>
            <el-radio-button label="90D">90天</el-radio-button>
            <el-radio-button label="1Y">1年</el-radio-button>
          </el-radio-group>
        </div>
      </div>
      <div id="maturityTrendChart" class="chart-container"></div>
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
              <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
              <el-option label="商业承兑汇票" value="COMMERCIAL_ACCEPTANCE" />
              <el-option label="支票" value="CHECK" />
              <el-option label="本票" value="PROMISSORY_NOTE" />
            </el-select>
          </el-form-item>
          <el-form-item label="到期状态">
            <el-select
              v-model="listQuery.maturityStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="未到期" value="NOT_MATURED" />
              <el-option label="即将到期" value="EXPIRING" />
              <el-option label="已到期" value="MATURED" />
              <el-option label="已处理" value="PROCESSED" />
              <el-option label="逾期" value="OVERDUE" />
            </el-select>
          </el-form-item>
          <el-form-item label="到期日期">
            <el-date-picker
              v-model="listQuery.maturityDateRange"
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

    <!-- 票据表格 -->
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
        <el-table-column label="票据号码" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.billNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票据类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getBillTypeTagType(row.billType)" size="mini">
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
        <el-table-column label="出票日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.issueDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span :class="getMaturityDateClass(row.maturityDate)">{{ row.maturityDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="剩余天数" width="100px" align="center">
          <template slot-scope="{row}">
            <span :class="getRemainingDaysClass(row.remainingDays)">{{ row.remainingDays }}天</span>
          </template>
        </el-table-column>
        <el-table-column label="到期状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getMaturityStatusTagType(row.maturityStatus)" size="mini">
              {{ getMaturityStatusText(row.maturityStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="提醒状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getReminderStatusTagType(row.reminderStatus)" size="mini">
              {{ getReminderStatusText(row.reminderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.maturityStatus === 'EXPIRING'" type="warning" size="mini" @click="handleSendSingleReminder(row)">
              提醒
            </el-button>
            <el-button v-if="row.maturityStatus === 'MATURED'" type="success" size="mini" @click="handleProcess(row)">
              处理
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'history', row: row}">处理历史</el-dropdown-item>
                <el-dropdown-item :command="{action: 'extend', row: row}">延期申请</el-dropdown-item>
                <el-dropdown-item :command="{action: 'collect', row: row}">托收申请</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 票据详情对话框 -->
    <el-dialog title="票据到期详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentBill" class="bill-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="票据号码">{{ currentBill.billNumber }}</el-descriptions-item>
          <el-descriptions-item label="票据类型">{{ getBillTypeText(currentBill.billType) }}</el-descriptions-item>
          <el-descriptions-item label="票据金额">{{ formatCurrency(currentBill.billAmount) }}</el-descriptions-item>
          <el-descriptions-item label="出票人">{{ currentBill.drawerName }}</el-descriptions-item>
          <el-descriptions-item label="承兑人">{{ currentBill.acceptorName }}</el-descriptions-item>
          <el-descriptions-item label="出票日期">{{ currentBill.issueDate }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ currentBill.maturityDate }}</el-descriptions-item>
          <el-descriptions-item label="剩余天数">{{ currentBill.remainingDays }}天</el-descriptions-item>
          <el-descriptions-item label="到期状态">
            <el-tag :type="getMaturityStatusTagType(currentBill.maturityStatus)">
              {{ getMaturityStatusText(currentBill.maturityStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="提醒状态">
            <el-tag :type="getReminderStatusTagType(currentBill.reminderStatus)">
              {{ getReminderStatusText(currentBill.reminderStatus) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <!-- 处理记录 -->
        <div class="process-records">
          <h4>处理记录</h4>
          <el-timeline>
            <el-timeline-item
              v-for="record in processRecords"
              :key="record.id"
              :timestamp="record.processTime"
              :type="getTimelineType(record.processType)"
            >
              <div class="timeline-content">
                <div class="timeline-title">{{ record.processType }}</div>
                <div class="timeline-description">{{ record.description }}</div>
                <div class="timeline-operator">操作人：{{ record.operatorName }}</div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentBill && currentBill.maturityStatus === 'MATURED'" type="success" @click="handleProcess(currentBill)">
          处理到期
        </el-button>
      </div>
    </el-dialog>

    <!-- 到期处理对话框 -->
    <el-dialog title="到期处理" :visible.sync="dialogProcessVisible" width="600px">
      <el-form ref="processForm" :model="processForm" label-width="100px">
        <el-form-item label="处理方式" prop="processType">
          <el-radio-group v-model="processForm.processType">
            <el-radio label="COLLECT">托收</el-radio>
            <el-radio label="EXTEND">延期</el-radio>
            <el-radio label="RETURN">退票</el-radio>
            <el-radio label="DISCOUNT">贴现</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="processForm.processType === 'EXTEND'" label="延期天数">
          <el-input-number v-model="processForm.extendDays" :min="1" :max="365" />
          <span style="margin-left: 8px;">天</span>
        </el-form-item>
        <el-form-item v-if="processForm.processType === 'COLLECT'" label="托收银行">
          <el-select v-model="processForm.collectBank" placeholder="请选择托收银行" style="width: 100%;">
            <el-option label="中国工商银行" value="ICBC" />
            <el-option label="中国建设银行" value="CCB" />
            <el-option label="中国银行" value="BOC" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理说明" prop="processDescription">
          <el-input v-model="processForm.processDescription" type="textarea" :rows="4" placeholder="请输入处理说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogProcessVisible = false">取消</el-button>
        <el-button type="primary" @click="submitProcess">确认处理</el-button>
      </div>
    </el-dialog>

    <!-- 批量提醒对话框 -->
    <el-dialog title="批量提醒设置" :visible.sync="dialogReminderVisible" width="600px">
      <el-form ref="reminderForm" :model="reminderForm" label-width="100px">
        <el-form-item label="提醒方式">
          <el-checkbox-group v-model="reminderForm.reminderMethods">
            <el-checkbox label="EMAIL">邮件</el-checkbox>
            <el-checkbox label="SMS">短信</el-checkbox>
            <el-checkbox label="SYSTEM">系统通知</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="提醒时间">
          <el-checkbox-group v-model="reminderForm.reminderTimes">
            <el-checkbox label="30">到期前30天</el-checkbox>
            <el-checkbox label="15">到期前15天</el-checkbox>
            <el-checkbox label="7">到期前7天</el-checkbox>
            <el-checkbox label="3">到期前3天</el-checkbox>
            <el-checkbox label="1">到期前1天</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="提醒内容">
          <el-input v-model="reminderForm.reminderContent" type="textarea" :rows="4" placeholder="请输入提醒内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogReminderVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReminder">发送提醒</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBillMaturityPage, processBillMaturity, sendMaturityReminder } from '@/api/globalTreasurer/pzgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'BillMaturityManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        billNumber: undefined,
        billType: undefined,
        maturityStatus: undefined,
        maturityDateRange: undefined
      },
      totalBills: 234,
      expiringBills: 18,
      maturedBills: 5,
      totalAmount: 18560.8,
      chartTimeRange: '30D',
      billList: [],
      multipleSelection: [],
      currentBill: null,
      processRecords: [],
      dialogDetailVisible: false,
      dialogProcessVisible: false,
      dialogReminderVisible: false,
      processForm: {
        processType: '',
        extendDays: 30,
        collectBank: '',
        processDescription: ''
      },
      reminderForm: {
        reminderMethods: ['EMAIL', 'SYSTEM'],
        reminderTimes: ['30', '7', '1'],
        reminderContent: '您的票据即将到期，请及时处理。'
      },
      chart: null
    }
  },
  mounted() {
    this.getList()
    this.initChart()
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
  },
  methods: {
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.billList = [
          {
            billId: 1,
            billNumber: 'BA20240925001',
            billType: 'BANK_ACCEPTANCE',
            billAmount: 1000000.00,
            drawerName: '示例云科技有限公司',
            acceptorName: '中国工商银行',
            issueDate: '2024-09-25',
            maturityDate: '2024-10-25',
            remainingDays: 30,
            maturityStatus: 'EXPIRING',
            reminderStatus: 'SENT'
          },
          {
            billId: 2,
            billNumber: 'CA20240920002',
            billType: 'COMMERCIAL_ACCEPTANCE',
            billAmount: 500000.00,
            drawerName: '客户B公司',
            acceptorName: '客户B公司',
            issueDate: '2024-08-20',
            maturityDate: '2024-09-20',
            remainingDays: -5,
            maturityStatus: 'MATURED',
            reminderStatus: 'PENDING'
          },
          {
            billId: 3,
            billNumber: 'BA20240915003',
            billType: 'BANK_ACCEPTANCE',
            billAmount: 800000.00,
            drawerName: '供应商C',
            acceptorName: '中国建设银行',
            issueDate: '2024-06-15',
            maturityDate: '2024-12-15',
            remainingDays: 80,
            maturityStatus: 'NOT_MATURED',
            reminderStatus: 'NOT_SENT'
          }
        ]
        this.total = this.billList.length
        this.listLoading = false
      }, 1000)
    },
    initChart() {
      // 初始化图表
      const echarts = require('echarts')
      this.chart = echarts.init(document.getElementById('maturityTrendChart'))
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['到期票据数量', '到期票据金额(万元)']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.generateDateLabels()
        },
        yAxis: [
          {
            type: 'value',
            name: '数量(张)',
            position: 'left'
          },
          {
            type: 'value',
            name: '金额(万元)',
            position: 'right'
          }
        ],
        series: [
          {
            name: '到期票据数量',
            type: 'bar',
            data: this.generateMockData(30, 0, 10),
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '到期票据金额(万元)',
            type: 'line',
            yAxisIndex: 1,
            data: this.generateMockData(30, 0, 1000),
            itemStyle: { color: '#E6A23C' }
          }
        ]
      }
      
      this.chart.setOption(option)
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
    generateMockData(count, min, max) {
      const data = []
      for (let i = 0; i < count; i++) {
        data.push(Math.floor(Math.random() * (max - min) + min))
      }
      return data
    },
    handleTimeRangeChange() {
      // 重新加载图表数据
      this.initChart()
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
        billType: undefined,
        maturityStatus: undefined,
        maturityDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleViewDetail(row) {
      this.currentBill = row
      this.loadProcessRecords(row.billId)
      this.dialogDetailVisible = true
    },
    handleProcess(row) {
      this.currentBill = row
      this.processForm = {
        processType: '',
        extendDays: 30,
        collectBank: '',
        processDescription: ''
      }
      this.dialogProcessVisible = true
    },
    handleSendReminder() {
      this.dialogReminderVisible = true
    },
    handleSendSingleReminder(row) {
      this.$confirm('确认发送到期提醒?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.reminderStatus = 'SENT'
        this.$message({
          type: 'success',
          message: '提醒发送成功!'
        })
      })
    },
    handleBatchProcess() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要处理的票据'
        })
        return
      }
      this.$confirm('确认批量处理选中的票据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.multipleSelection.forEach(item => {
          if (item.maturityStatus === 'MATURED') {
            item.maturityStatus = 'PROCESSED'
          }
        })
        this.$message({
          type: 'success',
          message: '批量处理成功!'
        })
      })
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'history':
          this.handleViewHistory(row)
          break
        case 'extend':
          this.handleExtend(row)
          break
        case 'collect':
          this.handleCollect(row)
          break
      }
    },
    handleViewHistory(row) {
      this.$message({
        type: 'info',
        message: '查看处理历史功能'
      })
    },
    handleExtend(row) {
      this.currentBill = row
      this.processForm.processType = 'EXTEND'
      this.dialogProcessVisible = true
    },
    handleCollect(row) {
      this.currentBill = row
      this.processForm.processType = 'COLLECT'
      this.dialogProcessVisible = true
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '到期数据导出成功'
      })
    },
    submitProcess() {
      if (!this.processForm.processType) {
        this.$message({
          type: 'warning',
          message: '请选择处理方式'
        })
        return
      }
      
      this.currentBill.maturityStatus = 'PROCESSED'
      this.dialogProcessVisible = false
      this.dialogDetailVisible = false
      
      this.$message({
        type: 'success',
        message: '到期处理完成!'
      })
    },
    submitReminder() {
      if (this.reminderForm.reminderMethods.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择提醒方式'
        })
        return
      }
      
      this.dialogReminderVisible = false
      this.$message({
        type: 'success',
        message: '批量提醒发送成功!'
      })
    },
    loadProcessRecords(billId) {
      // 模拟处理记录数据
      this.processRecords = [
        {
          id: 1,
          processType: '票据登记',
          description: '票据成功登记入系统',
          processTime: '2024-09-25 10:00:00',
          operatorName: '张三'
        },
        {
          id: 2,
          processType: '到期提醒',
          description: '发送到期提醒通知',
          processTime: '2024-09-20 09:00:00',
          operatorName: '系统自动'
        }
      ]
    },
    getBillTypeTagType(type) {
      const typeMap = {
        'BANK_ACCEPTANCE': 'success',
        'COMMERCIAL_ACCEPTANCE': 'primary',
        'CHECK': 'warning',
        'PROMISSORY_NOTE': 'info'
      }
      return typeMap[type] || 'info'
    },
    getBillTypeText(type) {
      const textMap = {
        'BANK_ACCEPTANCE': '银行承兑汇票',
        'COMMERCIAL_ACCEPTANCE': '商业承兑汇票',
        'CHECK': '支票',
        'PROMISSORY_NOTE': '本票'
      }
      return textMap[type] || type
    },
    getMaturityStatusTagType(status) {
      const typeMap = {
        'NOT_MATURED': 'info',
        'EXPIRING': 'warning',
        'MATURED': 'danger',
        'PROCESSED': 'success',
        'OVERDUE': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getMaturityStatusText(status) {
      const textMap = {
        'NOT_MATURED': '未到期',
        'EXPIRING': '即将到期',
        'MATURED': '已到期',
        'PROCESSED': '已处理',
        'OVERDUE': '逾期'
      }
      return textMap[status] || status
    },
    getReminderStatusTagType(status) {
      const typeMap = {
        'NOT_SENT': 'info',
        'SENT': 'success',
        'PENDING': 'warning'
      }
      return typeMap[status] || 'info'
    },
    getReminderStatusText(status) {
      const textMap = {
        'NOT_SENT': '未发送',
        'SENT': '已发送',
        'PENDING': '待发送'
      }
      return textMap[status] || status
    },
    getMaturityDateClass(maturityDate) {
      const today = new Date()
      const maturity = new Date(maturityDate)
      const diffDays = Math.ceil((maturity - today) / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) return 'expired-date'
      if (diffDays <= 30) return 'expiring-date'
      return ''
    },
    getRemainingDaysClass(days) {
      if (days < 0) return 'overdue-days'
      if (days <= 7) return 'urgent-days'
      if (days <= 30) return 'warning-days'
      return ''
    },
    getTimelineType(processType) {
      const typeMap = {
        '票据登记': 'primary',
        '到期提醒': 'warning',
        '到期处理': 'success',
        '延期申请': 'info'
      }
      return typeMap[processType] || 'primary'
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
.bill-maturity-manage {
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

  .maturity-overview {
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
          &.expiring-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.matured-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
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
            &.warning {
              color: #E6A23C;
            }
            &.negative {
              color: #F56C6C;
            }
          }
        }
      }
    }
  }

  .chart-card {
    margin-bottom: 20px;
    
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
  }

  .search-card, .table-card {
    margin-bottom: 20px;
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

  .expired-date {
    color: #F56C6C;
    font-weight: 600;
  }

  .expiring-date {
    color: #E6A23C;
    font-weight: 600;
  }

  .overdue-days {
    color: #F56C6C;
    font-weight: 600;
  }

  .urgent-days {
    color: #E6A23C;
    font-weight: 600;
  }

  .warning-days {
    color: #E6A23C;
  }

  .bill-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }
    
    .process-records {
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
}
</style>
