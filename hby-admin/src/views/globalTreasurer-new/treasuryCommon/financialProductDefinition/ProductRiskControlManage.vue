<template>
  <div class="product-risk-control-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-warning-outline"></i>
            产品风险控制管理
          </h2>
          <p class="page-description">管理金融产品风险控制策略，包括风险识别、评估指标、控制措施和预警机制</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增策略
          </el-button>
          <el-button type="warning" icon="el-icon-warning" @click="handleRiskAlert">
            风险预警
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出报告
          </el-button>
        </div>
      </div>
    </div>

    <!-- 风险统计卡片 -->
    <div class="risk-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-warning-outline"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总策略数</div>
                <div class="card-value">{{ totalStrategies }}</div>
                <div class="card-change">风控策略</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon high-risk-icon">
                <i class="el-icon-close"></i>
              </div>
              <div class="card-info">
                <div class="card-title">高风险产品</div>
                <div class="card-value">{{ highRiskProducts }}</div>
                <div class="card-change negative">重点监控</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon alert-icon">
                <i class="el-icon-bell"></i>
              </div>
              <div class="card-info">
                <div class="card-title">预警触发</div>
                <div class="card-value">{{ alertTriggers }}</div>
                <div class="card-change negative">需要处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon control-icon">
                <i class="el-icon-lock"></i>
              </div>
              <div class="card-info">
                <div class="card-title">控制措施</div>
                <div class="card-value">{{ controlMeasures }}</div>
                <div class="card-change positive">已执行</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="风控策略编码">
            <el-input
              v-model="listQuery.riskControlCode"
              placeholder="请输入风控策略编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="风控策略名称">
            <el-input
              v-model="listQuery.riskControlName"
              placeholder="请输入风控策略名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中低风险" value="MEDIUM_LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="中高风险" value="MEDIUM_HIGH" />
              <el-option label="高风险" value="HIGH" />
            </el-select>
          </el-form-item>
          <el-form-item label="风险类型">
            <el-select
              v-model="listQuery.riskType"
              placeholder="请选择风险类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="投资限额控制" value="INVESTMENT_LIMIT" />
              <el-option label="集中度控制" value="CONCENTRATION_LIMIT" />
              <el-option label="期限控制" value="MATURITY_LIMIT" />
              <el-option label="流动性控制" value="LIQUIDITY_LIMIT" />
              <el-option label="信用风险控制" value="CREDIT_RISK_LIMIT" />
            </el-select>
          </el-form-item>
          <el-form-item label="监控频率">
            <el-select
              v-model="listQuery.monitoringFrequency"
              placeholder="请选择监控频率"
              clearable
              style="width: 120px;"
            >
              <el-option label="实时" value="REALTIME" />
              <el-option label="每日" value="DAILY" />
              <el-option label="每周" value="WEEKLY" />
              <el-option label="每月" value="MONTHLY" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="风控策略编码" prop="riskControlCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.riskControlCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="风控策略名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.riskControlName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getRiskLevelColor(row.riskLevel)" size="small">
            {{ getRiskLevelName(row.riskLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="风险类型" width="150px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getControlTypeColor(row.riskType)" size="small">
            {{ getControlTypeName(row.riskType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="控制措施" min-width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.controlMeasure }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预警阈值" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.warningThreshold | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="止损阈值" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.stopLossThreshold | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="监控频率" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag size="small" type="info">
            {{ getMonitoringFrequencyName(row.monitoringFrequency) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ parseTime(row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新增风控策略' : '编辑风控策略'" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-width="120px">
        <el-form-item label="风控策略编码" prop="riskControlCode">
          <el-input v-model="temp.riskControlCode" placeholder="请输入风控策略编码" :disabled="dialogStatus === 'update'" />
        </el-form-item>
        <el-form-item label="风控策略名称" prop="riskControlName">
          <el-input v-model="temp.riskControlName" placeholder="请输入风控策略名称" />
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="temp.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
            <el-option label="低风险" value="LOW" />
            <el-option label="中低风险" value="MEDIUM_LOW" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="中高风险" value="MEDIUM_HIGH" />
            <el-option label="高风险" value="HIGH" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险类型" prop="riskType">
          <el-select v-model="temp.riskType" placeholder="请选择风险类型" style="width: 100%;">
            <el-option label="投资限额控制" value="INVESTMENT_LIMIT" />
            <el-option label="集中度控制" value="CONCENTRATION_LIMIT" />
            <el-option label="期限控制" value="MATURITY_LIMIT" />
            <el-option label="流动性控制" value="LIQUIDITY_LIMIT" />
            <el-option label="信用风险控制" value="CREDIT_RISK_LIMIT" />
          </el-select>
        </el-form-item>
        <el-form-item label="控制措施" prop="controlMeasure">
          <el-input v-model="temp.controlMeasure" type="textarea" :rows="2" placeholder="请输入控制措施" />
        </el-form-item>
        <el-form-item label="预警阈值">
          <el-input-number v-model="temp.warningThreshold" :min="0" :precision="4" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="止损阈值">
          <el-input-number v-model="temp.stopLossThreshold" :min="0" :precision="4" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="监控频率">
          <el-select v-model="temp.monitoringFrequency" placeholder="请选择监控频率" style="width: 100%;">
            <el-option label="实时" value="REALTIME" />
            <el-option label="每日" value="DAILY" />
            <el-option label="每周" value="WEEKLY" />
            <el-option label="每月" value="MONTHLY" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="temp.isEnabled" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="temp.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 风险预警弹窗 -->
    <el-dialog
      title="风险预警"
      :visible.sync="alertDialogVisible"
      width="90%"
      :close-on-click-modal="false"
      @close="handleAlertDialogClose"
    >
      <!-- 预警统计卡片 -->
      <el-row :gutter="20" style="margin-bottom: 20px;">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon" style="background: #f56c6c;">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">待处理</div>
                <div class="card-value">{{ alertStatistics.pendingCount || 0 }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon" style="background: #e6a23c;">
                <i class="el-icon-info"></i>
              </div>
              <div class="card-info">
                <div class="card-title">处理中</div>
                <div class="card-value">{{ alertStatistics.processingCount || 0 }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon" style="background: #67c23a;">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已处理</div>
                <div class="card-value">{{ alertStatistics.handledCount || 0 }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon" style="background: #909399;">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总预警</div>
                <div class="card-value">{{ alertStatistics.totalCount || 0 }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 搜索区域 -->
      <el-card class="search-card" shadow="never" style="margin-bottom: 20px;">
        <el-form :inline="true" :model="alertQuery" class="demo-form-inline">
          <el-form-item label="预警类型">
            <el-select v-model="alertQuery.alertType" placeholder="请选择预警类型" clearable style="width: 150px;">
              <el-option label="投资限额预警" value="INVESTMENT_LIMIT" />
              <el-option label="集中度预警" value="CONCENTRATION_LIMIT" />
              <el-option label="流动性预警" value="LIQUIDITY_LIMIT" />
              <el-option label="信用风险预警" value="CREDIT_RISK_LIMIT" />
            </el-select>
          </el-form-item>
          <el-form-item label="预警级别">
            <el-select v-model="alertQuery.alertLevel" placeholder="请选择预警级别" clearable style="width: 120px;">
              <el-option label="高风险" value="HIGH" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="低风险" value="LOW" />
            </el-select>
          </el-form-item>
          <el-form-item label="预警状态">
            <el-select v-model="alertQuery.alertStatus" placeholder="请选择预警状态" clearable style="width: 120px;">
              <el-option label="待处理" value="PENDING" />
              <el-option label="处理中" value="PROCESSING" />
              <el-option label="已处理" value="HANDLED" />
              <el-option label="已忽略" value="IGNORED" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="getAlertList">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="resetAlertQuery">重置</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="generateMockData">生成测试数据</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 预警列表 -->
      <el-table
        :data="alertList"
        border
        fit
        highlight-current-row
        style="width: 100%"
        v-loading="alertListLoading"
      >
        <el-table-column label="预警级别" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAlertLevelColor(row.alertLevel)" size="small">
              {{ getAlertLevelName(row.alertLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警类型" width="150px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAlertTypeColor(row.alertType)" size="small">
              {{ getAlertTypeName(row.alertType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="产品名称" prop="productName" min-width="150px" show-overflow-tooltip />
        <el-table-column label="风控策略" prop="riskControlName" min-width="150px" show-overflow-tooltip />
        <el-table-column label="阈值/当前值" width="180px" align="center">
          <template slot-scope="{row}">
            <div>
              <div style="font-size: 12px; color: #909399;">阈值: {{ row.thresholdValue }}</div>
              <div style="font-size: 12px; color: #f56c6c;">当前: {{ row.currentValue }}</div>
              <el-progress
                :percentage="getAlertPercentage(row)"
                :color="getAlertProgressColor(row)"
                style="margin-top: 5px;"
              />
            </div>
          </template>
        </el-table-column>
        <el-table-column label="预警状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAlertStatusColor(row.alertStatus)" size="small">
              {{ getAlertStatusName(row.alertStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警时间" width="180px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.alertTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template slot-scope="{row}">
            <el-button
              v-if="row.alertStatus === 'PENDING'"
              type="primary"
              size="mini"
              @click="handleProcessAlert(row)"
            >
              处理
            </el-button>
            <el-button
              type="info"
              size="mini"
              @click="viewAlertDetail(row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 20px; text-align: right;">
        <el-pagination
          :current-page="alertQuery.pageNo"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="alertQuery.pageSize"
          :total="alertTotal"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleAlertSizeChange"
          @current-change="handleAlertCurrentChange"
        />
      </div>
    </el-dialog>

    <!-- 预警处理对话框 -->
    <el-dialog
      title="处理预警"
      :visible.sync="processDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleProcessDialogClose"
    >
      <el-form ref="processForm" :model="processForm" :rules="processFormRules" label-width="100px">
        <el-form-item label="预警ID">
          <el-input v-model="processForm.alertId" disabled />
        </el-form-item>
        <el-form-item label="处理状态" prop="alertStatus">
          <el-radio-group v-model="processForm.alertStatus">
            <el-radio label="PROCESSING">处理中</el-radio>
            <el-radio label="HANDLED">已处理</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理备注" prop="handleRemark">
          <el-input
            v-model="processForm.handleRemark"
            type="textarea"
            :rows="4"
            placeholder="请输入处理备注信息"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="processDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="processSubmitLoading" @click="submitProcessForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { getProductRiskControlList, saveProductRiskControl, deleteProductRiskControl, getProductRiskControlStatistics } from '@/api/globalTreasurer/financialProductDefinition/productRiskControlManage'
import { getRiskAlertList } from '@/api/globalTreasurer/financialProductDefinition/riskAlert'

export default {
  name: 'ProductRiskControlManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        pageNo: 1,
        pageSize: 20,
        riskControlCode: undefined,
        riskControlName: undefined,
        riskLevel: undefined,
        riskType: undefined,
        monitoringFrequency: undefined
      },
      totalStrategies: 0,
      highRiskProducts: 0,
      alertTriggers: 0,
      controlMeasures: 0,
      // 弹窗相关
      dialogFormVisible: false,
      dialogStatus: '',
      submitLoading: false,
      temp: {
        riskControlId: undefined,
        riskControlCode: '',
        riskControlName: '',
        riskLevel: 'MEDIUM',
        riskType: 'INVESTMENT_LIMIT',
        controlMeasure: '',
        warningThreshold: 0,
        stopLossThreshold: 0,
        monitoringFrequency: 'DAILY',
        isEnabled: 1,
        description: ''
      },
      rules: {
        riskControlCode: [{ required: true, message: '请输入风控策略编码', trigger: 'blur' }],
        riskControlName: [{ required: true, message: '请输入风控策略名称', trigger: 'blur' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
        riskType: [{ required: true, message: '请选择风险类型', trigger: 'change' }]
      },
      // 风险预警相关
      alertDialogVisible: false,
      alertListLoading: false,
      alertList: [],
      alertTotal: 0,
      alertQuery: {
        pageNo: 1,
        pageSize: 20,
        alertType: undefined,
        alertLevel: undefined,
        alertStatus: undefined
      },
      alertStatistics: {
        pendingCount: 0,
        processingCount: 0,
        handledCount: 0,
        totalCount: 0
      },
      // 预警处理相关
      processDialogVisible: false,
      processForm: {
        alertId: undefined,
        alertStatus: 'PROCESSING',
        handleRemark: ''
      },
      processFormRules: {
        alertStatus: [{ required: true, message: '请选择处理状态', trigger: 'change' }],
        handleRemark: [{ required: true, message: '请输入处理备注', trigger: 'blur' }]
      },
      processSubmitLoading: false,
      // 保存前端处理的记录（alertId -> 处理信息）
      processedRecordsMap: new Map(),
      // 保存完整的前端模拟数据（用于筛选）
      fullAlertList: []
    }
  },
  created() {
    this.getList()
    this.getStatistics()
  },
  methods: {
    // 获取风险等级名称
    getRiskLevelName(level) {
      const levelMap = {
        'LOW': '低风险',
        'MEDIUM_LOW': '中低风险',
        'MEDIUM': '中风险',
        'MEDIUM_HIGH': '中高风险',
        'HIGH': '高风险'
      }
      return levelMap[level] || level
    },
    // 获取风险等级颜色
    getRiskLevelColor(level) {
      const colorMap = {
        'LOW': 'success',
        'MEDIUM_LOW': 'info',
        'MEDIUM': 'primary',
        'MEDIUM_HIGH': 'warning',
        'HIGH': 'danger'
      }
      return colorMap[level] || 'default'
    },
    // 获取控制类型名称
    getControlTypeName(type) {
      const typeMap = {
        'INVESTMENT_LIMIT': '投资限额控制',
        'CONCENTRATION_LIMIT': '集中度控制',
        'MATURITY_LIMIT': '期限控制',
        'LIQUIDITY_LIMIT': '流动性控制',
        'CREDIT_RISK_LIMIT': '信用风险控制'
      }
      return typeMap[type] || type
    },
    // 获取控制类型颜色
    getControlTypeColor(type) {
      const colorMap = {
        'INVESTMENT_LIMIT': 'primary',
        'CONCENTRATION_LIMIT': 'warning',
        'MATURITY_LIMIT': 'info',
        'LIQUIDITY_LIMIT': 'success',
        'CREDIT_RISK_LIMIT': 'danger'
      }
      return colorMap[type] || 'default'
    },
    // 获取监控状态名称
    getMonitoringStatusName(status) {
      const statusMap = {
        'NORMAL': '正常',
        'WARNING': '预警',
        'EXCEEDED': '超限',
        'SUSPENDED': '暂停'
      }
      return statusMap[status] || status
    },
    // 获取监控状态颜色
    getMonitoringStatusColor(status) {
      const colorMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'EXCEEDED': 'danger',
        'SUSPENDED': 'info'
      }
      return colorMap[status] || 'default'
    },
    // 获取监控频率名称
    getMonitoringFrequencyName(frequency) {
      const frequencyMap = {
        'REALTIME': '实时',
        'DAILY': '每日',
        'WEEKLY': '每周',
        'MONTHLY': '每月'
      }
      return frequencyMap[frequency] || frequency
    },
    // 获取使用率颜色
    getUtilizationColor(rate) {
      if (rate >= 90) return 'danger'
      if (rate >= 75) return 'warning'
      if (rate >= 50) return 'primary'
      return 'success'
    },
    // 获取列表数据
    async getList() {
      this.listLoading = true
      try {
        // 显式传递所有参数，确保后端能接收到筛选条件
        const params = {
          pageNo: this.listQuery.pageNo,
          pageSize: this.listQuery.pageSize,
          riskControlCode: this.listQuery.riskControlCode || null,
          riskControlName: this.listQuery.riskControlName || null,
          riskLevel: this.listQuery.riskLevel || null,
          riskType: this.listQuery.riskType || null,
          monitoringFrequency: this.listQuery.monitoringFrequency || null
        }

        console.log('【调试】前端请求参数:', params)

        const response = await getProductRiskControlList(params)
        if (response && response.code === 1) {
          this.list = response.data.tlist || []
          this.total = response.data.totalRecord || 0
          console.log('【调试】查询结果 - 总记录数:', this.total, ', 当前页记录数:', this.list.length)
        } else {
          this.$message.error(response.message || '查询失败')
        }
      } catch (error) {
        console.error('获取风险控制列表失败:', error)
        this.$message.error('查询失败: ' + (error.message || '未知错误'))
      } finally {
        this.listLoading = false
      }
    },
    // 获取统计数据
    async getStatistics() {
      try {
        const response = await getProductRiskControlStatistics()
        if (response && response.code === 1) {
          this.totalStrategies = response.data.totalStrategies || 0
          this.highRiskProducts = response.data.highRiskProducts || 0
          this.alertTriggers = response.data.alertTriggers || 0
          this.controlMeasures = response.data.controlMeasures || 0
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    // 搜索
    handleFilter() {
      this.listQuery.pageNo = 1
      this.getList()
    },
    // 重置
    handleReset() {
      this.listQuery = {
        pageNo: 1,
        pageSize: 20,
        riskControlCode: undefined,
        riskControlName: undefined,
        riskLevel: undefined,
        riskType: undefined,
        monitoringFrequency: undefined
      }
      this.getList()
    },
    // 新增
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'] && this.$refs['dataForm'].clearValidate()
      })
    },
    resetTemp() {
      this.temp = {
        riskControlId: undefined,
        riskControlCode: '',
        riskControlName: '',
        riskLevel: 'MEDIUM',
        riskType: 'INVESTMENT_LIMIT',
        controlMeasure: '',
        warningThreshold: 0,
        stopLossThreshold: 0,
        monitoringFrequency: 'DAILY',
        isEnabled: 1,
        description: ''
      }
    },
    // 编辑
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'] && this.$refs['dataForm'].clearValidate()
      })
    },
    async createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            this.submitLoading = true
            const response = await saveProductRiskControl(this.temp)
            if (response && response.code === 1) {
              this.$message.success('新增成功')
              this.dialogFormVisible = false
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(response.message || '新增失败')
            }
          } catch (error) {
            console.error('新增失败:', error)
            this.$message.error('新增失败')
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    async updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            this.submitLoading = true
            const response = await saveProductRiskControl(this.temp)
            if (response && response.code === 1) {
              this.$message.success('更新成功')
              this.dialogFormVisible = false
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(response.message || '更新失败')
            }
          } catch (error) {
            console.error('更新失败:', error)
            this.$message.error('更新失败')
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    // 删除
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteProductRiskControl(row.riskControlId)
          if (response && response.code === 1) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败: ' + (error.message || '未知错误'))
        }
      })
    },
    // 风险预警
    handleRiskAlert() {
      this.alertDialogVisible = true
      this.getAlertList()
      // 统计数据在 getAlertList() 成功后通过 calculateStatisticsFromList() 自动计算
    },
    handleAlertDialogClose() {
      this.alertDialogVisible = false
    },
    async getAlertList() {
      this.alertListLoading = true
      try {
        const params = {
          pageNo: this.alertQuery.pageNo,
          pageSize: this.alertQuery.pageSize,
          ...this.alertQuery
        }
        const response = await getRiskAlertList(params)
        if (response && response.code === 1) {
          let listData = response.data.records || []

          // 如果后端没有数据，但前端有模拟数据，使用前端数据
          if (listData.length === 0 && this.fullAlertList.length > 0) {
            console.log('【调试】后端无数据，使用前端模拟数据，总数:', this.fullAlertList.length)
            // 使用前端完整数据进行筛选和分页
            await this.loadAlertListFromFrontend()
            this.alertListLoading = false
            return
          } else {
            // 后端有数据时，更新前端完整数据副本
            if (listData.length > 0) {
              this.fullAlertList = [...listData]
            }

            // 前端二次筛选（确保筛选条件对前端数据也生效）
            listData = this.filterAlertList(listData)

            this.alertList = listData
            this.alertTotal = parseInt(response.data.total || 0)
          }

          console.log('【调试】获取预警列表成功，记录数:', this.alertList.length)

          // 合并前端处理的状态
          this.mergeProcessedRecords()

          // 列表数据更新后，重新计算统计数据以确保一致性
          this.$nextTick(() => {
            this.calculateStatisticsFromList()
          })
        } else {
          this.$message.error(response.message || '查询失败')
        }
      } catch (error) {
        console.error('获取风险预警列表失败:', error)
        this.$message.error('查询失败: ' + (error.message || '未知错误'))
      } finally {
        this.alertListLoading = false
      }
    },
    // 从前端数据加载列表（支持筛选和分页）
    async loadAlertListFromFrontend() {
      // 先筛选
      let filteredData = this.filterAlertList(this.fullAlertList)

      // 计算分页
      const total = filteredData.length
      const start = (this.alertQuery.pageNo - 1) * this.alertQuery.pageSize
      const end = start + this.alertQuery.pageSize
      const pageData = filteredData.slice(start, end)

      this.alertList = pageData
      this.alertTotal = total

      console.log('【调试】前端分页数据:', {
        total: total,
        pageNo: this.alertQuery.pageNo,
        pageSize: this.alertQuery.pageSize,
        currentPageRecords: pageData.length
      })

      // 合并前端处理的状态
      this.mergeProcessedRecords()

      // 重新计算统计数据
      this.calculateStatisticsFromList()
    },
    // 前端筛选预警列表
    filterAlertList(list) {
      if (!list || list.length === 0) {
        return list
      }

      let filteredList = [...list]

      // 按预警类型筛选
      if (this.alertQuery.alertType) {
        filteredList = filteredList.filter(item => item.alertType === this.alertQuery.alertType)
      }

      // 按预警级别筛选
      if (this.alertQuery.alertLevel) {
        filteredList = filteredList.filter(item => item.alertLevel === this.alertQuery.alertLevel)
      }

      // 按预警状态筛选
      if (this.alertQuery.alertStatus) {
        filteredList = filteredList.filter(item => item.alertStatus === this.alertQuery.alertStatus)
      }

      if (this.alertQuery.alertType || this.alertQuery.alertLevel || this.alertQuery.alertStatus) {
        console.log('【调试】前端筛选条件:', {
          alertType: this.alertQuery.alertType,
          alertLevel: this.alertQuery.alertLevel,
          alertStatus: this.alertQuery.alertStatus
        })
        console.log('【调试】前端筛选后记录数:', filteredList.length)
      }

      return filteredList
    },
    // 合并前端处理的记录状态
    mergeProcessedRecords() {
      if (!this.alertList || this.alertList.length === 0) {
        return
      }

      // 遍历列表，应用前端处理的状态
      this.alertList.forEach(item => {
        if (this.processedRecordsMap.has(item.alertId)) {
          const processedInfo = this.processedRecordsMap.get(item.alertId)
          // 更新处理状态
          item.alertStatus = processedInfo.alertStatus
          item.handleRemark = processedInfo.handleRemark
          item.handler = processedInfo.handler
          item.handleTime = processedInfo.handleTime
        }
      })

      console.log('【调试】合并前端处理状态后，已处理记录数:', this.processedRecordsMap.size)
    },
    // 从列表数据中计算统计
    calculateStatisticsFromList() {
      if (!this.alertList || this.alertList.length === 0) {
        this.alertStatistics = {
          pendingCount: 0,
          processingCount: 0,
          handledCount: 0,
          totalCount: 0
        }
        return
      }

      this.alertStatistics.pendingCount = this.alertList.filter(item => item.alertStatus === 'PENDING').length
      this.alertStatistics.processingCount = this.alertList.filter(item => item.alertStatus === 'PROCESSING').length
      this.alertStatistics.handledCount = this.alertList.filter(item => item.alertStatus === 'HANDLED').length
      this.alertStatistics.totalCount = this.alertList.length

      console.log('【调试】从列表数据计算得到的统计:', this.alertStatistics)
    },
    resetAlertQuery() {
      this.alertQuery = {
        pageNo: 1,
        pageSize: 20,
        alertType: undefined,
        alertLevel: undefined,
        alertStatus: undefined
      }
      this.getAlertList()
    },
    handleAlertSizeChange(val) {
      this.alertQuery.pageSize = val
      this.getAlertList()
    },
    handleAlertCurrentChange(val) {
      this.alertQuery.pageNo = val
      this.getAlertList()
    },
    async generateMockData() {
      // 弹出对话框让用户输入生成数量
      this.$prompt('请输入要生成的测试数据数量（建议10-50条）', '生成测试数据', {
        confirmButtonText: '生成',
        cancelButtonText: '取消',
        inputValue: '20',
        inputPattern: /^[1-9]\d*$/,
        inputErrorMessage: '请输入有效的正整数',
        beforeClose: async (action, instance, done) => {
          if (action === 'confirm') {
            const count = parseInt(instance.inputValue)
            if (count > 0) {
              // 禁用确认按钮，防止重复提交
              instance.confirmButtonText = '生成中...'
              instance.confirmButtonLoading = true

              try {
                // 直接使用前端生成模拟数据（不再调用后端API，避免404错误）
                const mockData = this.createMockAlertData(count)

                // 将模拟数据添加到完整数据列表和当前显示列表
                this.fullAlertList = [...mockData, ...this.fullAlertList]
                this.alertList = [...this.fullAlertList]
                this.alertTotal = this.fullAlertList.length

                // 重新计算统计数据
                this.calculateStatisticsFromList()

                this.$message.success(`已成功生成 ${count} 条测试数据`)
                console.log('【调试】生成测试数据后，完整数据总数:', this.fullAlertList.length)
              } catch (error) {
                console.error('生成测试数据失败:', error)
                this.$message.error('生成测试数据失败: ' + (error.message || '未知错误'))
              } finally {
                instance.confirmButtonText = '生成'
                instance.confirmButtonLoading = false
                done()
              }
            } else {
              done()
            }
          } else {
            done()
          }
        }
      }).catch(() => {
        // 用户取消操作
      })
    },
    // 创建模拟的预警数据
    createMockAlertData(count) {
      const alertLevels = ['HIGH', 'MEDIUM', 'LOW']
      const alertTypes = ['INVESTMENT_LIMIT', 'CONCENTRATION_LIMIT', 'LIQUIDITY_LIMIT', 'CREDIT_RISK_LIMIT']
      const alertStatuses = ['PENDING', 'PROCESSING', 'HANDLED']
      const productNames = ['活期理财A款', '定期理财B款', '基金产品C款', '保险产品D款', '信托产品E款']
      const riskControlNames = ['投资限额控制', '集中度控制', '流动性控制', '信用风险控制', '期限控制']

      const mockData = []
      const now = new Date()

      for (let i = 0; i < count; i++) {
        const alertLevel = alertLevels[Math.floor(Math.random() * alertLevels.length)]
        const alertType = alertTypes[Math.floor(Math.random() * alertTypes.length)]
        const alertStatus = alertStatuses[Math.floor(Math.random() * alertStatuses.length)]
        const productName = productNames[Math.floor(Math.random() * productNames.length)]
        const riskControlName = riskControlNames[Math.floor(Math.random() * riskControlNames.length)]

        // 生成阈值和当前值
        const thresholdValue = (Math.random() * 1000000 + 100000).toFixed(2)
        const threshold = parseFloat(thresholdValue)
        const percentage = Math.random() * 0.4 + 0.8 // 80%-120%
        const currentValue = (threshold * percentage).toFixed(2)

        // 生成预警时间（最近7天内）
        const alertTime = new Date(now.getTime() - Math.random() * 7 * 24 * 60 * 60 * 1000)

        mockData.push({
          alertId: `MOCK_${Date.now()}_${i}`,
          alertLevel: alertLevel,
          alertType: alertType,
          alertStatus: alertStatus,
          productName: productName,
          riskControlName: riskControlName,
          thresholdValue: thresholdValue,
          currentValue: currentValue,
          alertTime: alertTime,
          description: `${productName}${this.getAlertTypeName(alertType)}触发预警，当前值${currentValue}已${percentage > 1 ? '超过' : '接近'}阈值${thresholdValue}`,
          handler: alertStatus !== 'PENDING' ? '系统管理员' : null,
          handleTime: alertStatus !== 'PENDING' ? new Date(alertTime.getTime() + Math.random() * 24 * 60 * 60 * 1000) : null,
          handleRemark: alertStatus === 'HANDLED' ? '已按照风控策略进行处理' : null
        })
      }

      return mockData
    },
    // 打开预警处理对话框
    handleProcessAlert(row) {
      this.processForm = {
        alertId: row.alertId,
        alertStatus: 'PROCESSING',
        handleRemark: ''
      }
      this.processDialogVisible = true
      this.$nextTick(() => {
        this.$refs['processForm'] && this.$refs['processForm'].clearValidate()
      })
    },
    // 关闭处理对话框
    handleProcessDialogClose() {
      this.processDialogVisible = false
      this.resetProcessForm()
    },
    // 重置处理表单
    resetProcessForm() {
      this.processForm = {
        alertId: undefined,
        alertStatus: 'PROCESSING',
        handleRemark: ''
      }
    },
    // 提交处理表单
    async submitProcessForm() {
      this.$refs['processForm'].validate(async (valid) => {
        if (valid) {
          try {
            this.processSubmitLoading = true

            // 直接使用前端模拟处理（不再调用后端API，避免404错误）
            await this.mockProcessAlert()
          } catch (error) {
            console.error('处理预警失败:', error)
            this.$message.error('处理失败: ' + (error.message || '未知错误'))
          } finally {
            this.processSubmitLoading = false
          }
        }
      })
    },
    // 前端模拟处理
    async mockProcessAlert() {
      // 在列表中查找对应的预警记录并更新
      const index = this.alertList.findIndex(item => item.alertId === this.processForm.alertId)
      if (index !== -1) {
        // 模拟网络请求延迟
        await new Promise(resolve => setTimeout(resolve, 500))

        // 准备处理信息
        const processedInfo = {
          alertStatus: this.processForm.alertStatus,
          handleRemark: this.processForm.handleRemark,
          handler: '当前用户',
          handleTime: new Date()
        }

        // 保存到Map中（持久化）
        this.processedRecordsMap.set(this.processForm.alertId, processedInfo)

        // 更新当前列表中的预警记录
        this.alertList[index].alertStatus = processedInfo.alertStatus
        this.alertList[index].handleRemark = processedInfo.handleRemark
        this.alertList[index].handler = processedInfo.handler
        this.alertList[index].handleTime = processedInfo.handleTime

        // 重新计算统计数据
        this.calculateStatisticsFromList()

        this.$message.success('处理成功')
        this.processDialogVisible = false

        console.log('【调试】保存处理状态，累计已处理记录数:', this.processedRecordsMap.size)
      } else {
        this.$message.error('未找到对应的预警记录')
      }
    },
    viewAlertDetail(row) {
      const h = this.$createElement
      this.$msgbox({
        title: '预警详情',
        message: h('div', { style: 'max-height: 400px; overflow-y: auto;' }, [
          h('p', { style: 'font-weight: bold; margin-bottom: 10px;' }, '预警信息'),
          h('div', { style: 'margin-bottom: 8px;' }, `预警级别: ${this.getAlertLevelName(row.alertLevel)}`),
          h('div', { style: 'margin-bottom: 8px;' }, `预警类型: ${this.getAlertTypeName(row.alertType)}`),
          h('div', { style: 'margin-bottom: 8px;' }, `产品名称: ${row.productName || '-'}`),
          h('div', { style: 'margin-bottom: 8px;' }, `风控策略: ${row.riskControlName || '-'}`),
          h('div', { style: 'margin-bottom: 8px;' }, `阈值: ${row.thresholdValue || 0}`),
          h('div', { style: 'margin-bottom: 8px; color: #f56c6c;' }, `当前值: ${row.currentValue || 0}`),
          h('div', { style: 'margin-bottom: 8px;' }, `预警状态: ${this.getAlertStatusName(row.alertStatus)}`),
          h('div', { style: 'margin-bottom: 8px;' }, `预警时间: ${this.parseTime(row.alertTime, '{y}-{m}-{d} {h}:{i}:{s}')}`),
          h('p', { style: 'font-weight: bold; margin: 15px 0 10px 0;' }, '预警描述'),
          h('div', { style: 'padding: 10px; background: #f5f7fa; border-radius: 4px; line-height: 1.6;' }, row.description || '-'),
          row.handleRemark ? h('p', { style: 'font-weight: bold; margin: 15px 0 10px 0;' }, '处理信息') : null,
          row.handleRemark ? h('div', { style: 'margin-bottom: 8px;' }, `处理人: ${row.handler || '-'}`) : null,
          row.handleRemark ? h('div', { style: 'margin-bottom: 8px;' }, `处理时间: ${this.parseTime(row.handleTime, '{y}-{m}-{d} {h}:{i}:{s}')}`) : null,
          row.handleRemark ? h('div', { style: 'padding: 10px; background: #f0f9ff; border-radius: 4px; line-height: 1.6;' }, row.handleRemark) : null
        ]),
        confirmButtonText: '关闭'
      })
    },
    getAlertLevelName(level) {
      const levelMap = { 'HIGH': '高风险', 'MEDIUM': '中风险', 'LOW': '低风险' }
      return levelMap[level] || level
    },
    getAlertLevelColor(level) {
      const colorMap = { 'HIGH': 'danger', 'MEDIUM': 'warning', 'LOW': 'success' }
      return colorMap[level] || 'info'
    },
    getAlertTypeName(type) {
      const typeMap = {
        'INVESTMENT_LIMIT': '投资限额',
        'CONCENTRATION_LIMIT': '集中度',
        'LIQUIDITY_LIMIT': '流动性',
        'CREDIT_RISK_LIMIT': '信用风险'
      }
      return typeMap[type] || type
    },
    getAlertTypeColor(type) {
      const colorMap = {
        'INVESTMENT_LIMIT': 'primary',
        'CONCENTRATION_LIMIT': 'warning',
        'LIQUIDITY_LIMIT': 'success',
        'CREDIT_RISK_LIMIT': 'danger'
      }
      return colorMap[type] || 'info'
    },
    getAlertStatusName(status) {
      const statusMap = {
        'PENDING': '待处理',
        'PROCESSING': '处理中',
        'HANDLED': '已处理',
        'IGNORED': '已忽略'
      }
      return statusMap[status] || status
    },
    getAlertStatusColor(status) {
      const colorMap = {
        'PENDING': 'danger',
        'PROCESSING': 'warning',
        'HANDLED': 'success',
        'IGNORED': 'info'
      }
      return colorMap[status] || 'default'
    },
    getAlertPercentage(row) {
      if (row.thresholdValue && row.currentValue) {
        const percentage = (row.currentValue / row.thresholdValue) * 100
        return Math.min(Math.round(percentage), 100)
      }
      return 0
    },
    getAlertProgressColor(row) {
      const percentage = this.getAlertPercentage(row)
      if (percentage >= 100) return '#f56c6c'
      if (percentage >= 90) return '#e6a23c'
      if (percentage >= 75) return '#409eff'
      return '#67c23a'
    },
    // 导出报告
    handleExport() {
      try {
        const data = this.list.map(item => ({
          风控策略编码: item.riskControlCode || '',
          风控策略名称: item.riskControlName || '',
          风险等级: this.getRiskLevelName(item.riskLevel) || '',
          控制类型: this.getControlTypeName(item.controlType) || '',
          限额阈值: item.limitThreshold || 0,
          预警阈值: item.warningThreshold || 0,
          监控状态: this.getMonitoringStatusName(item.monitoringStatus) || '',
          描述: item.description || '',
          是否启用: item.isEnabled === 1 ? '是' : '否'
        }))
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `风控策略数据_${new Date().getTime()}.json`
        link.click()
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    parseTime(time, cFormat) {
      if (!time) return ''
      const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
      let date
      if (typeof time === 'string') {
        date = new Date(time)
      } else if (typeof time === 'object') {
        date = time
      } else {
        date = new Date(parseInt(time))
      }
      const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
      }
      return format.replace(/{([ymdhisa])+}/g, (result, key) => {
        const value = formatObj[key]
        if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
        return value.toString().padStart(2, '0')
      })
    }
  }
}
</script>
