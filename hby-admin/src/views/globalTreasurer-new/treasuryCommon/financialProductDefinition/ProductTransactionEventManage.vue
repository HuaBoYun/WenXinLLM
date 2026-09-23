<template>
  <div class="product-transaction-event-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-s-order"></i>
            产品交易事件管理
          </h2>
          <p class="page-description">管理金融产品交易事件配置，包括事件类型、触发条件、处理流程和通知机制</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增事件
          </el-button>
          <el-button type="warning" icon="el-icon-bell" @click="handleEventMonitor">
            事件监控
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 事件统计卡片 -->
    <div class="event-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-s-order"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总事件数</div>
                <div class="card-value">{{ totalEvents }}</div>
                <div class="card-change">交易事件</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon buy-icon">
                <i class="el-icon-shopping-cart-2"></i>
              </div>
              <div class="card-info">
                <div class="card-title">申购事件</div>
                <div class="card-value">{{ purchaseEvents }}</div>
                <div class="card-change positive">买入交易</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon sell-icon">
                <i class="el-icon-sold-out"></i>
              </div>
              <div class="card-info">
                <div class="card-title">赎回事件</div>
                <div class="card-value">{{ redemptionEvents }}</div>
                <div class="card-change negative">卖出交易</div>
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
                <div class="card-title">启用事件</div>
                <div class="card-value">{{ activeEvents }}</div>
                <div class="card-change positive">正常监控</div>
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
          <el-form-item label="事件编码">
            <el-input
              v-model="listQuery.eventCode"
              placeholder="请输入事件编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="事件名称">
            <el-input
              v-model="listQuery.eventName"
              placeholder="请输入事件名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="事件类型">
            <el-select
              v-model="listQuery.eventType"
              placeholder="请选择事件类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="投资申购" value="INVESTMENT_PURCHASE" />
              <el-option label="投资赎回" value="INVESTMENT_REDEMPTION" />
              <el-option label="利息收入" value="INTEREST_INCOME" />
              <el-option label="本金回收" value="PRINCIPAL_RECOVERY" />
              <el-option label="分红收入" value="DIVIDEND_INCOME" />
              <el-option label="投资到期" value="INVESTMENT_MATURITY" />
              <el-option label="投资损失" value="INVESTMENT_LOSS" />
            </el-select>
          </el-form-item>
          <el-form-item label="产品类型">
            <el-select
              v-model="listQuery.productType"
              placeholder="请选择产品类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="银行理财" value="BANK_WEALTH" />
              <el-option label="债券投资" value="BOND" />
              <el-option label="股票投资" value="EQUITY" />
              <el-option label="基金投资" value="FUND" />
              <el-option label="衍生品投资" value="DERIVATIVE" />
            </el-select>
          </el-form-item>
          <el-form-item label="事件状态">
            <el-select
              v-model="listQuery.eventStatus"
              placeholder="请选择事件状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="活跃" value="ACTIVE" />
              <el-option label="暂停" value="PAUSED" />
              <el-option label="停用" value="INACTIVE" />
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
      <el-table-column label="事件编码" prop="eventCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.eventCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="事件名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.eventName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="事件类型" width="150px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getEventTypeColor(row.eventType)" size="small">
            {{ getEventTypeName(row.eventType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="产品类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getProductTypeColor(row.productType)" size="small">
            {{ getProductTypeName(row.productType) || '-' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="触发条件" min-width="200px" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.triggerCondition }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会计科目" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.accountingSubject || row.accountingSubject || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="影响方向" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getImpactDirectionColor(row.impactDirection)" size="small">
            {{ getImpactDirectionName(row.impactDirection) || '-' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="优先级" width="80px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getPriorityColor(row.priority)" size="small">
            {{ row.priority || row.sortOrder || '-' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ parseTime(row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getEventStatusColor(row.eventStatus)">
            {{ getEventStatusName(row.eventStatus) }}
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
    <el-dialog :title="dialogStatus === 'create' ? '新增交易事件' : '编辑交易事件'" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-width="120px">
        <el-form-item label="事件编码" prop="eventCode">
          <el-input v-model="temp.eventCode" placeholder="请输入事件编码" :disabled="dialogStatus === 'update'" />
        </el-form-item>
        <el-form-item label="事件名称" prop="eventName">
          <el-input v-model="temp.eventName" placeholder="请输入事件名称" />
        </el-form-item>
        <el-form-item label="事件类型" prop="eventType">
          <el-select v-model="temp.eventType" placeholder="请选择事件类型" style="width: 100%;">
            <el-option label="投资申购" value="INVESTMENT_PURCHASE" />
            <el-option label="投资赎回" value="INVESTMENT_REDEMPTION" />
            <el-option label="利息收入" value="INTEREST_INCOME" />
            <el-option label="本金回收" value="PRINCIPAL_RECOVERY" />
            <el-option label="分红收入" value="DIVIDEND_INCOME" />
            <el-option label="投资到期" value="INVESTMENT_MATURITY" />
            <el-option label="投资损失" value="INVESTMENT_LOSS" />
          </el-select>
        </el-form-item>
        <el-form-item label="产品类型" prop="productType">
          <el-select v-model="temp.productType" placeholder="请选择产品类型" style="width: 100%;">
            <el-option label="银行理财" value="BANK_WEALTH" />
            <el-option label="债券投资" value="BOND" />
            <el-option label="股票投资" value="EQUITY" />
            <el-option label="基金投资" value="FUND" />
            <el-option label="衍生品投资" value="DERIVATIVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="触发条件" prop="triggerCondition">
          <el-input v-model="temp.triggerCondition" type="textarea" :rows="2" placeholder="请输入触发条件" />
        </el-form-item>
        <el-form-item label="会计科目" prop="accountingSubject">
          <el-input v-model="temp.accountingSubject" placeholder="请输入会计科目" />
        </el-form-item>
        <el-form-item label="影响方向" prop="impactDirection">
          <el-select v-model="temp.impactDirection" placeholder="请选择影响方向" style="width: 100%;">
            <el-option label="增加" value="INCREASE" />
            <el-option label="减少" value="DECREASE" />
            <el-option label="不变" value="NEUTRAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-input-number v-model="temp.priority" :min="1" :max="10" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="事件状态">
          <el-select v-model="temp.eventStatus" placeholder="请选择事件状态" style="width: 100%;">
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="暂停" value="PAUSED" />
            <el-option label="停用" value="INACTIVE" />
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

    <!-- 事件监控对话框 -->
    <el-dialog title="事件监控" :visible.sync="monitorDialogVisible" width="900px" v-loading="monitorLoading">
      <div class="monitor-dialog">
        <div class="monitor-overview">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card class="monitor-card">
                <div class="card-content">
                  <div class="card-icon total-icon">
                    <i class="el-icon-s-order"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">总事件数</div>
                    <div class="card-value">{{ monitorData.total || 0 }}</div>
                    <div class="card-change">全部事件</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="monitor-card">
                <div class="card-content">
                  <div class="card-icon enabled-icon">
                    <i class="el-icon-success"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">启用事件</div>
                    <div class="card-value">{{ monitorData.enabled || 0 }}</div>
                    <div class="card-change positive">可用监控</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="monitor-card">
                <div class="card-content">
                  <div class="card-icon active-icon">
                    <i class="el-icon-s-flag"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">活跃事件</div>
                    <div class="card-value">{{ monitorData.active || 0 }}</div>
                    <div class="card-change positive">运行中</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="monitor-card">
                <div class="card-content">
                  <div class="card-icon paused-icon">
                    <i class="el-icon-video-pause"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">暂停事件</div>
                    <div class="card-value">{{ monitorData.paused || 0 }}</div>
                    <div class="card-change warning">已暂停</div>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="6">
              <el-card class="monitor-card">
                <div class="card-content">
                  <div class="card-icon inactive-icon">
                    <i class="el-icon-remove-outline"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">停用事件</div>
                    <div class="card-value">{{ monitorData.inactive || 0 }}</div>
                    <div class="card-change negative">不可用</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="monitor-card">
                <div class="card-content">
                  <div class="card-icon purchase-icon">
                    <i class="el-icon-shopping-cart-2"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">申购事件</div>
                    <div class="card-value">{{ monitorData.purchaseEvents || 0 }}</div>
                    <div class="card-change positive">买入交易</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="monitor-card">
                <div class="card-content">
                  <div class="card-icon redemption-icon">
                    <i class="el-icon-sold-out"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">赎回事件</div>
                    <div class="card-value">{{ monitorData.redemptionEvents || 0 }}</div>
                    <div class="card-change negative">卖出交易</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="monitor-card">
                <div class="card-content">
                  <div class="card-icon enabled-rate-icon">
                    <i class="el-icon-pie-chart"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">启用率</div>
                    <div class="card-value">{{ monitorData.enabledRate || 0 }}%</div>
                    <div class="card-change positive">可用占比</div>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <div class="monitor-details">
          <h3>事件类型统计</h3>
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="statistic-item">
                <span class="statistic-label">投资申购事件</span>
                <span class="statistic-value primary">{{ monitorData.purchaseEvents || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="statistic-item">
                <span class="statistic-label">投资赎回事件</span>
                <span class="statistic-value warning">{{ monitorData.redemptionEvents || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="statistic-item">
                <span class="statistic-label">利息收入事件</span>
                <span class="statistic-value success">{{ monitorData.interestEvents || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="statistic-item">
                <span class="statistic-label">本金回收事件</span>
                <span class="statistic-value info">{{ monitorData.principalEvents || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="statistic-item">
                <span class="statistic-label">分红收入事件</span>
                <span class="statistic-value success">{{ monitorData.dividendEvents || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="statistic-item">
                <span class="statistic-label">投资到期事件</span>
                <span class="statistic-value default">{{ monitorData.maturityEvents || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="statistic-item">
                <span class="statistic-label">投资损失事件</span>
                <span class="statistic-value danger">{{ monitorData.lossEvents || 0 }}</span>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="monitor-details">
          <h3>产品类型统计</h3>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="statistic-item">
                <span class="statistic-label">银行理财</span>
                <span class="statistic-value primary">{{ monitorData.bankWealthEvents || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="statistic-item">
                <span class="statistic-label">债券投资</span>
                <span class="statistic-value success">{{ monitorData.bondEvents || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="statistic-item">
                <span class="statistic-label">股票投资</span>
                <span class="statistic-value warning">{{ monitorData.equityEvents || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="statistic-item">
                <span class="statistic-label">基金投资</span>
                <span class="statistic-value info">{{ monitorData.fundEvents || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="statistic-item">
                <span class="statistic-label">衍生品投资</span>
                <span class="statistic-value danger">{{ monitorData.derivativeEvents || 0 }}</span>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="monitor-info">
          <el-alert title="监控说明" type="info" :closable="false" show-icon>
            <div>• 总事件数：系统中所有配置的事件总数</div>
            <div>• 启用事件：已启用且可用于监控的事件</div>
            <div>• 活跃事件：状态为活跃（ACTIVE）且已启用的事件</div>
            <div>• 暂停事件：状态为暂停（PAUSED）的事件</div>
            <div>• 停用事件：状态为停用（INACTIVE）的事件</div>
            <div>• 启用率：已启用事件占总事件数的百分比</div>
          </el-alert>
          <el-alert title="使用提示" type="warning" :closable="false" show-icon>
            <div>• 当前统计基于事件配置数据，反映系统中配置的事件情况</div>
            <div>• 定期检查事件状态，确保活跃事件正常运行</div>
            <div>• 建议定期清理停用事件，保持配置清晰</div>
          </el-alert>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="monitorDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleRefreshMonitor">
          <i class="el-icon-refresh"></i> 刷新数据
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getProductTransactionEventList,
  deleteProductTransactionEvent,
  getProductTransactionEventStatistics,
  getEventMonitorList
} from '@/api/globalTreasurer/financialProductDefinition/productTransactionEventManage'

export default {
  name: 'ProductTransactionEventManage',
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
        eventCode: undefined,
        eventName: undefined,
        eventType: undefined,
        productType: undefined,
        eventStatus: undefined,
        isEnabled: undefined
      },
      totalEvents: 0,
      activeEvents: 0,
      purchaseEvents: 0,
      redemptionEvents: 0,
      // 监控相关
      monitorDialogVisible: false,
      monitorData: {
        total: 0,
        enabled: 0,
        active: 0,
        paused: 0,
        inactive: 0,
        purchaseEvents: 0,
        redemptionEvents: 0,
        interestEvents: 0,
        principalEvents: 0,
        dividendEvents: 0,
        maturityEvents: 0,
        lossEvents: 0,
        bankWealthEvents: 0,
        bondEvents: 0,
        equityEvents: 0,
        fundEvents: 0,
        derivativeEvents: 0,
        enabledRate: 0
      },
      monitorLoading: false,
      // 弹窗相关
      dialogFormVisible: false,
      dialogStatus: '',
      submitLoading: false,
      temp: {
        eventId: undefined,
        eventCode: '',
        eventName: '',
        eventType: 'INVESTMENT_PURCHASE',
        productType: 'BANK_WEALTH',
        triggerCondition: '',
        accountingSubject: '',
        impactDirection: 'INCREASE',
        priority: 5,
        eventStatus: 'ACTIVE',
        isEnabled: 1,
        description: ''
      },
      rules: {
        eventCode: [{ required: true, message: '请输入事件编码', trigger: 'blur' }],
        eventName: [{ required: true, message: '请输入事件名称', trigger: 'blur' }],
        eventType: [{ required: true, message: '请选择事件类型', trigger: 'change' }],
        productType: [{ required: true, message: '请选择产品类型', trigger: 'change' }],
        triggerCondition: [{ required: true, message: '请输入触发条件', trigger: 'blur' }],
        accountingSubject: [{ required: true, message: '请输入会计科目', trigger: 'blur' }],
        impactDirection: [{ required: true, message: '请选择影响方向', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getStatistics()
  },
  methods: {
    /**
     * 获取交易事件列表
     */
    getList() {
      this.listLoading = true
      const params = {
        pageNo: this.listQuery.pageNo,
        pageSize: this.listQuery.pageSize,
        eventCode: this.listQuery.eventCode,
        eventName: this.listQuery.eventName,
        eventType: this.listQuery.eventType,
        productType: this.listQuery.productType,
        eventStatus: this.listQuery.eventStatus,
        isEnabled: this.listQuery.isEnabled
      }

      getProductTransactionEventList(params).then(response => {
        if (response.code === 1) {
          this.list = response.data.tlist
          this.total = response.data.totalRecord
        } else {
          this.$message.error(response.message || '获取交易事件列表失败')
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取交易事件列表失败:', error)
        // 检查是否是404错误（接口不存在）
        if (error && (error.status === 404 || error.response?.status === 404)) {
          console.log('列表接口尚未实现，显示空列表')
          this.list = []
          this.total = 0
          this.$message.info('接口尚未实现，暂无数据')
        } else {
          this.$message.error('获取交易事件列表失败')
          this.list = []
          this.total = 0
        }
        this.listLoading = false
      })
    },

    /**
     * 获取统计数据
     */
    getStatistics() {
      getProductTransactionEventStatistics().then(response => {
        if (response.code === 1) {
          this.totalEvents = response.data.total || 0
          this.activeEvents = response.data.active || 0
          this.purchaseEvents = response.data.purchase || 0
          this.redemptionEvents = response.data.redemption || 0
        } else {
          this.$message.error(response.message || '获取统计数据失败')
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
        // 检查是否是404错误（接口不存在）
        if (error && (error.status === 404 || error.response?.status === 404)) {
          console.log('统计接口尚未实现，使用默认数据')
          this.totalEvents = 0
          this.activeEvents = 0
          this.purchaseEvents = 0
          this.redemptionEvents = 0
        } else {
          this.totalEvents = 0
          this.activeEvents = 0
          this.purchaseEvents = 0
          this.redemptionEvents = 0
        }
      })
    },

    /**
     * 搜索
     */
    handleFilter() {
      this.listQuery.pageNo = 1
      this.getList()
    },

    /**
     * 重置搜索条件
     */
    handleReset() {
      this.listQuery = {
        pageNo: 1,
        pageSize: 20,
        eventCode: undefined,
        eventName: undefined,
        eventType: undefined,
        productType: undefined,
        eventStatus: undefined,
        isEnabled: undefined
      }
      this.getList()
    },

    /**
     * 新增
     */
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
        eventId: undefined,
        eventCode: '',
        eventName: '',
        eventType: 'INVESTMENT_PURCHASE',
        productType: 'BANK_WEALTH',
        triggerCondition: '',
        accountingSubject: '',
        impactDirection: 'INCREASE',
        priority: 5,
        eventStatus: 'ACTIVE',
        isEnabled: 1,
        description: ''
      }
    },

    /**
     * 编辑
     */
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
            const { createProductTransactionEvent } = await import('@/api/globalTreasurer/financialProductDefinition/productTransactionEventManage')
            const response = await createProductTransactionEvent(this.temp)
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
            // 检查是否是404错误（接口不存在）
            if (error && (error.status === 404 || error.response?.status === 404)) {
              this.$message.info('新增功能接口尚未实现')
            } else {
              this.$message.error('新增失败')
            }
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
            const { updateProductTransactionEvent } = await import('@/api/globalTreasurer/financialProductDefinition/productTransactionEventManage')
            const response = await updateProductTransactionEvent(this.temp)
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
            // 检查是否是404错误（接口不存在）
            if (error && (error.status === 404 || error.response?.status === 404)) {
              this.$message.info('更新功能接口尚未实现')
            } else {
              this.$message.error('更新失败')
            }
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    /**
     * 删除
     */
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteProductTransactionEvent(row.eventId).then(response => {
          if (response.code === 1) {
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
        }).catch(error => {
          console.error('删除失败:', error)
          // 检查是否是404错误（接口不存在）
          if (error && (error.status === 404 || error.response?.status === 404)) {
            this.$message.info('删除功能接口尚未实现')
          } else {
            this.$message.error('删除失败')
          }
        })
      })
    },
    getEventTypeName(type) {
      const typeMap = {
        'INVESTMENT_PURCHASE': '投资申购',
        'INVESTMENT_REDEMPTION': '投资赎回',
        'INTEREST_INCOME': '利息收入',
        'PRINCIPAL_RECOVERY': '本金回收',
        'DIVIDEND_INCOME': '分红收入',
        'INVESTMENT_MATURITY': '投资到期',
        'INVESTMENT_LOSS': '投资损失'
      }
      return typeMap[type] || type
    },

    /**
     * 获取事件类型颜色
     */
    getEventTypeColor(type) {
      const colorMap = {
        'INVESTMENT_PURCHASE': 'primary',
        'INVESTMENT_REDEMPTION': 'warning',
        'INTEREST_INCOME': 'success',
        'PRINCIPAL_RECOVERY': 'info',
        'DIVIDEND_INCOME': 'success',
        'INVESTMENT_MATURITY': 'default',
        'INVESTMENT_LOSS': 'danger'
      }
      return colorMap[type] || 'default'
    },

    /**
     * 获取产品类型名称
     */
    getProductTypeName(type) {
      const typeMap = {
        'BANK_WEALTH': '银行理财',
        'BOND': '债券投资',
        'EQUITY': '股票投资',
        'FUND': '基金投资',
        'DERIVATIVE': '衍生品投资'
      }
      return typeMap[type] || type
    },

    /**
     * 获取产品类型颜色
     */
    getProductTypeColor(type) {
      const colorMap = {
        'BANK_WEALTH': 'primary',
        'BOND': 'success',
        'EQUITY': 'warning',
        'FUND': 'info',
        'DERIVATIVE': 'danger'
      }
      return colorMap[type] || 'default'
    },

    /**
     * 获取事件状态名称
     */
    getEventStatusName(status) {
      // 支持两种格式：字符串(ACTIVE)或数字(1/0)
      if (typeof status === 'number') {
        return status === 1 ? '启用' : '停用'
      }
      const statusMap = {
        'ACTIVE': '活跃',
        'PAUSED': '暂停',
        'INACTIVE': '停用'
      }
      return statusMap[status] || status
    },

    /**
     * 获取事件状态颜色
     */
    getEventStatusColor(status) {
      // 支持两种格式：字符串(ACTIVE)或数字(1/0)
      if (typeof status === 'number') {
        return status === 1 ? 'success' : 'info'
      }
      const colorMap = {
        'ACTIVE': 'success',
        'PAUSED': 'warning',
        'INACTIVE': 'danger'
      }
      return colorMap[status] || 'default'
    },

    /**
     * 获取影响方向名称
     */
    getImpactDirectionName(direction) {
      const directionMap = {
        'INCREASE': '增加',
        'DECREASE': '减少',
        'NEUTRAL': '不变'
      }
      return directionMap[direction] || direction
    },

    /**
     * 获取影响方向颜色
     */
    getImpactDirectionColor(direction) {
      const colorMap = {
        'INCREASE': 'success',
        'DECREASE': 'danger',
        'NEUTRAL': 'info'
      }
      return colorMap[direction] || 'default'
    },

    /**
     * 获取优先级颜色
     */
    getPriorityColor(priority) {
      if (priority >= 8) return 'danger'
      if (priority >= 5) return 'warning'
      return 'success'
    },

    /**
     * 事件监控
     */
    handleEventMonitor() {
      this.monitorDialogVisible = true
      this.loadMonitorData()
    },

    /**
     * 加载监控数据
     */
    loadMonitorData() {
      this.monitorLoading = true
      getEventMonitorList({
        pageNo: 1,
        pageSize: 20
      }).then(response => {
        this.monitorLoading = false
        if (response.code === 1 && response.data) {
          this.monitorData = response.data
          // 计算启用率
          if (response.data.total > 0) {
            this.monitorData.enabledRate = ((response.data.enabled / response.data.total) * 100).toFixed(1)
          } else {
            this.monitorData.enabledRate = 0
          }
        } else {
          this.$message.error(response.message || '获取监控数据失败')
        }
      }).catch(error => {
        this.monitorLoading = false
        console.error('获取事件监控数据失败:', error)
        this.$message.error('获取监控数据失败，请稍后重试')
      })
    },

    /**
     * 刷新监控数据
     */
    handleRefreshMonitor() {
      this.loadMonitorData()
      this.$message.success('监控数据已刷新')
    },

    /**
     * 导出配置
     */
    handleExport() {
      const { getProductTransactionEventList } = require('@/api/globalTreasurer/financialProductDefinition/productTransactionEventManage')
      // 导出所有数据，不分页
      getProductTransactionEventList({
        pageNo: 1,
        pageSize: 9999,
        eventCode: this.listQuery.eventCode,
        eventName: this.listQuery.eventName,
        eventType: this.listQuery.eventType,
        productType: this.listQuery.productType,
        eventStatus: this.listQuery.eventStatus,
        isEnabled: this.listQuery.isEnabled
      }).then(response => {
        if (response.code === 1 && response.data.tlist) {
          this.exportToExcel(response.data.tlist)
        } else {
          this.$message.error(response.message || '导出失败')
        }
      }).catch(error => {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      })
    },

    /**
     * 导出到Excel
     */
    exportToExcel(data) {
      // 准备Excel数据
      const excelData = data.map(item => ({
        '事件编码': item.eventCode,
        '事件名称': item.eventName,
        '事件类型': this.getEventTypeName(item.eventType),
        '产品类型': this.getProductTypeName(item.productType),
        '触发条件': item.triggerCondition,
        '会计科目': item.accountingSubject,
        '影响方向': this.getImpactDirectionName(item.impactDirection),
        '优先级': item.priority,
        '事件状态': this.getEventStatusName(item.eventStatus),
        '是否启用': item.isEnabled === 1 ? '是' : '否',
        '创建时间': this.parseTime(item.createTime)
      }))

      // 使用xlsx导出
      import('xlsx').then(xlsx => {
        const worksheet = xlsx.utils.json_to_sheet(excelData)
        const workbook = xlsx.utils.book_new()
        xlsx.utils.book_append_sheet(workbook, worksheet, '产品交易事件配置')
        xlsx.writeFile(workbook, `产品交易事件配置_${new Date().getTime()}.xlsx`)
        this.$message.success('导出成功')
      }).catch(error => {
        console.error('导出Excel失败:', error)
        this.$message.error('导出Excel失败')
      })
    },

    /**
     * 格式化时间
     */
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

<style lang="scss" scoped>
.product-transaction-event-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        h2 {
          margin: 0 10px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;

          i {
            margin-right: 8px;
          }
        }
      }

      .header-right {
        .el-button {
          margin-left: 10px;
        }
      }

      .page-description {
        margin: 5px 0 20px 0;
        color: #909399;
        font-size: 14px;
      }
    }
  }

  .event-overview {
    margin-bottom: 20px;

    .overview-card {
      border-radius: 8px;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
      }

      .card-content {
        display: flex;
        align-items: center;

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 15px;

          i {
            font-size: 28px;
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 14px;
            color: #606266;
            margin-bottom: 5px;
          }

          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 5px;
          }

          .card-change {
            font-size: 12px;
          }
        }
      }

      .total-icon i { color: #409EFF; }
      .buy-icon i { color: #67C23A; }
      .sell-icon i { color: #E6A23C; }
      .active-icon i { color: #409EFF; }
    }
  }

  .search-card {
    margin-bottom: 20px;

    .search-form {
      display: flex;
      flex-wrap: wrap;

      .el-form-item {
        margin-bottom: 0;
      }
    }
  }

  /* 监控对话框样式 */
  .monitor-dialog {
    .monitor-overview {
      margin-bottom: 30px;

      .monitor-card {
        border-radius: 8px;
        transition: all 0.3s;

        &:hover {
          transform: translateY(-5px);
          box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
        }

        .card-content {
          display: flex;
          align-items: center;

          .card-icon {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;

            i {
              font-size: 28px;
            }
          }

          .card-info {
            flex: 1;

            .card-title {
              font-size: 14px;
              color: #606266;
              margin-bottom: 5px;
            }

            .card-value {
              font-size: 24px;
              font-weight: 600;
              color: #303133;
              margin-bottom: 5px;
            }

            .card-change {
              font-size: 12px;

              &.positive {
                color: #67C23A;
              }

              &.warning {
                color: #E6A23C;
              }

              &.negative {
                color: #F56C6C;
              }
            }
          }
        }

        .total-icon i { color: #409EFF; }
        .enabled-icon i { color: #67C23A; }
        .active-icon i { color: #409EFF; }
        .paused-icon i { color: #E6A23C; }
        .inactive-icon i { color: #909399; }
        .purchase-icon i { color: #67C23A; }
        .redemption-icon i { color: #E6A23C; }
        .enabled-rate-icon i { color: #67C23A; }
      }
    }

    .monitor-details {
      margin-top: 30px;

      h3 {
        font-size: 18px;
        color: #303133;
        margin-bottom: 20px;
        padding-bottom: 10px;
        border-bottom: 2px solid #409EFF;
      }

      .statistic-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15px 20px;
        background: #f5f7fa;
        border-radius: 6px;
        margin-bottom: 10px;
        transition: all 0.3s;

        &:hover {
          background: #ecf5ff;
        }

        .statistic-label {
          font-size: 14px;
          color: #606266;
          font-weight: 500;
        }

        .statistic-value {
          font-size: 20px;
          font-weight: 600;
          padding: 4px 12px;
          border-radius: 4px;

          &.primary {
            color: #409EFF;
            background: rgba(64, 158, 255, 0.1);
          }

          &.success {
            color: #67C23A;
            background: rgba(103, 194, 58, 0.1);
          }

          &.warning {
            color: #E6A23C;
            background: rgba(230, 162, 60, 0.1);
          }

          &.info {
            color: #909399;
            background: rgba(144, 147, 153, 0.1);
          }

          &.default {
            color: #606266;
            background: rgba(96, 98, 102, 0.1);
          }

          &.danger {
            color: #F56C6C;
            background: rgba(245, 108, 108, 0.1);
          }
        }
      }
    }

    .monitor-info {
      margin-top: 30px;

      .el-alert {
        margin-bottom: 10px;

        div {
          line-height: 1.8;
          margin-bottom: 5px;
        }
      }
    }
  }
}
</style>
