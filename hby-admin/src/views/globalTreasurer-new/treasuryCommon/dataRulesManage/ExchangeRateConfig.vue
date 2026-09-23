<template>
  <div class="exchange-rate-config">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-money"></i>
            汇率配置管理
          </h2>
          <p class="page-description">管理多币种汇率配置，支持实时汇率更新和历史汇率查询</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增汇率
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleSyncRate">
            同步汇率
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 汇率概览卡片 -->
    <div class="rate-overview">
      <el-row :gutter="20">
        <el-col v-for="(item, index) in rateOverview" :key="index" :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div :class="['card-icon', rateIconClass[index]]">
                <i :class="rateIconList[index]"></i>
              </div>
              <div class="card-info">
                <div class="card-title">{{ item.baseCurrency }}/{{ item.targetCurrency }}</div>
                <div class="card-value">{{ item.rate }}</div>
                <div
                  class="card-change"
                  :class="{ positive: item.change > 0, negative: item.change < 0 }"
                >
                  {{ item.change >= 0 ? '+' : '' }}{{ item.change.toFixed(4) }}
                  ({{ item.changePercent >= 0 ? '+' : '' }}{{ item.changePercent.toFixed(2) }}%)
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon update-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">最后更新</div>
                <div class="card-value">{{ lastUpdateTime }}</div>
                <div class="card-change">实时汇率</div>
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
          <el-form-item label="基准币种">
            <el-select
              v-model="listQuery.baseCurrency"
              placeholder="请选择基准币种"
              clearable
              style="width: 150px;"
            >
              <el-option label="人民币 (CNY)" value="CNY">
                <span class="currency-option">
                  <span class="currency-flag">¥</span>
                  人民币 (CNY)
                </span>
              </el-option>
              <el-option label="美元 (USD)" value="USD">
                <span class="currency-option">
                  <span class="currency-flag">$</span>
                  美元 (USD)
                </span>
              </el-option>
              <el-option label="欧元 (EUR)" value="EUR">
                <span class="currency-option">
                  <span class="currency-flag">€</span>
                  欧元 (EUR)
                </span>
              </el-option>
              <el-option label="日元 (JPY)" value="JPY">
                <span class="currency-option">
                  <span class="currency-flag">¥</span>
                  日元 (JPY)
                </span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="目标币种">
            <el-select
              v-model="listQuery.targetCurrency"
              placeholder="请选择目标币种"
              clearable
              style="width: 150px;"
            >
              <el-option label="人民币 (CNY)" value="CNY">
                <span class="currency-option">
                  <span class="currency-flag">¥</span>
                  人民币 (CNY)
                </span>
              </el-option>
              <el-option label="美元 (USD)" value="USD">
                <span class="currency-option">
                  <span class="currency-flag">$</span>
                  美元 (USD)
                </span>
              </el-option>
              <el-option label="欧元 (EUR)" value="EUR">
                <span class="currency-option">
                  <span class="currency-flag">€</span>
                  欧元 (EUR)
                </span>
              </el-option>
              <el-option label="日元 (JPY)" value="JPY">
                <span class="currency-option">
                  <span class="currency-flag">¥</span>
                  日元 (JPY)
                </span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="生效日期">
            <el-date-picker
              v-model="listQuery.effectiveDate"
              type="date"
              placeholder="请选择生效日期"
              style="width: 150px;"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
          <el-form-item label="汇率类型">
            <el-select
              v-model="listQuery.rateType"
              placeholder="请选择汇率类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="现汇" value="SPOT" />
              <el-option label="现钞" value="CASH" />
              <el-option label="中间价" value="MIDDLE" />
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

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <div class="table-title">
          <span class="title-text">汇率配置列表</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="fetchData">刷新</el-button>
            <el-button size="small" icon="el-icon-setting" @click="handleTableSetting">设置</el-button>
          </el-button-group>
        </div>
      </div>

      <el-table
        v-loading="listLoading"
        :data="list"
        border
        style="width: 100%;"
        :row-class-name="tableRowClassName"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />
        
        <el-table-column v-if="isColumnVisible('currencyPair')" label="货币对" prop="currencyPair" align="center" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <div class="currency-pair">
              <span class="base-currency">{{ scope.row.baseCurrency }}</span>
              <i class="el-icon-right"></i>
              <span class="target-currency">{{ scope.row.targetCurrency }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('exchangeRate')" label="汇率" prop="exchangeRate" align="center" width="120" :sortable="getColumnSortable('exchangeRate') ? 'custom' : false">
          <template slot-scope="scope">
            <div class="rate-value">
              <span class="rate-number">{{ formatRate(scope.row.exchangeRate) }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('rateType')" label="汇率类型" prop="rateType" align="center" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRateTypeColor(scope.row.rateType)" size="small">
              {{ getRateTypeText(scope.row.rateType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('effectiveDate')" label="生效日期" prop="effectiveDate" align="center" width="120" :sortable="getColumnSortable('effectiveDate') ? 'custom' : false">
          <template slot-scope="scope">
            <span class="effective-date">{{ formatDate(scope.row.effectiveDate) }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('expireDate')" label="失效日期" prop="expireDate" align="center" width="120" :sortable="getColumnSortable('expireDate') ? 'custom' : false">
          <template slot-scope="scope">
            <span class="expire-date">{{ scope.row.expireDate ? formatDate(scope.row.expireDate) : '永久有效' }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('dataSource')" label="数据来源" prop="dataSource" align="center" width="120">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getSourceColor(scope.row.dataSource)">
              {{ getSourceText(scope.row.dataSource) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('status')" label="状态" class-name="status-col" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('updateTime')" label="更新时间" prop="updateTime" align="center" width="160" :sortable="getColumnSortable('updateTime') ? 'custom' : false">
          <template slot-scope="scope">
            <span class="update-time">
              <i class="el-icon-time"></i>
              {{ formatTime(scope.row.updateTime) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width" fixed="right">
          <template slot-scope="scope">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
              <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row, scope.$index)">删除</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          background
          :current-page="listQuery.pageNum"
          :layout="layout"
          :page-size="listQuery.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑汇率对话框 -->
    <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="exchangeRateDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      append-to-body
      @open="handleDialogOpen"
    >
      <el-form ref="dataForm" :rules="rules" :model="currentExchangeRate" label-position="left" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="基准币种" prop="baseCurrency">
              <el-select v-model="currentExchangeRate.baseCurrency" placeholder="请选择基准币种" style="width: 100%;">
                <el-option label="人民币 (CNY)" value="CNY" />
                <el-option label="美元 (USD)" value="USD" />
                <el-option label="欧元 (EUR)" value="EUR" />
                <el-option label="日元 (JPY)" value="JPY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标币种" prop="targetCurrency">
              <el-select v-model="currentExchangeRate.targetCurrency" placeholder="请选择目标币种" style="width: 100%;">
                <el-option label="人民币 (CNY)" value="CNY" />
                <el-option label="美元 (USD)" value="USD" />
                <el-option label="欧元 (EUR)" value="EUR" />
                <el-option label="日元 (JPY)" value="JPY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="汇率" prop="exchangeRate">
              <el-input-number v-model="currentExchangeRate.exchangeRate" :precision="4" :step="0.0001" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="汇率类型" prop="rateType">
              <el-select v-model="currentExchangeRate.rateType" placeholder="请选择汇率类型" style="width: 100%;">
                <el-option label="现汇" value="SPOT" />
                <el-option label="现钞" value="CASH" />
                <el-option label="中间价" value="MIDDLE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="currentExchangeRate.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                style="width: 100%;"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期">
              <el-date-picker
                v-model="currentExchangeRate.expireDate"
                type="date"
                placeholder="选择失效日期（可选）"
                style="width: 100%;"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="数据来源" prop="dataSource">
          <el-select v-model="currentExchangeRate.dataSource" placeholder="请选择数据来源" style="width: 100%;">
            <el-option label="手工录入" value="MANUAL" />
            <el-option label="自动获取" value="AUTO" />
            <el-option label="API接口" value="API" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="exchangeRateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确认</el-button>
      </div>
    </el-dialog>

    <!-- 查看汇率详情对话框 -->
    <el-dialog title="汇率详情" :visible.sync="viewDialogVisible" width="500px">
      <div v-if="currentExchangeRate" class="view-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="货币对">
            {{ currentExchangeRate.baseCurrency }}/{{ currentExchangeRate.targetCurrency }}
          </el-descriptions-item>
          <el-descriptions-item label="汇率">
            {{ formatRate(currentExchangeRate.exchangeRate) }}
          </el-descriptions-item>
          <el-descriptions-item label="汇率类型">
            <el-tag :type="getRateTypeColor(currentExchangeRate.rateType)" size="small">
              {{ getRateTypeText(currentExchangeRate.rateType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="生效日期">
            {{ formatDate(currentExchangeRate.effectiveDate) }}
          </el-descriptions-item>
          <el-descriptions-item label="失效日期">
            {{ currentExchangeRate.expireDate ? formatDate(currentExchangeRate.expireDate) : '永久有效' }}
          </el-descriptions-item>
          <el-descriptions-item label="数据来源">
            <el-tag :type="getSourceColor(currentExchangeRate.dataSource)" size="small">
              {{ getSourceText(currentExchangeRate.dataSource) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentExchangeRate.status === 1 ? 'success' : 'info'" size="small">
              {{ currentExchangeRate.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleEditFromView">编辑</el-button>
      </div>
    </el-dialog>

    <!-- 表格设置对话框 -->
    <el-dialog
      title="表格设置"
      :visible.sync="tableSettingVisible"
      width="500px"
      :close-on-click-modal="false"
      @close="handleTableSettingClose"
    >
      <div class="table-setting-content">
        <el-alert
          title="您可以通过勾选下面的选项来自定义表格显示的列"
          type="info"
          show-icon
          :closable="false"
          style="margin-bottom: 20px;"
        />

        <el-checkbox-group v-model="visibleColumns" @change="handleColumnChange">
          <div class="column-group">
            <h4>基础信息</h4>
            <el-checkbox
              v-for="column in basicColumns"
              :key="column.prop"
              :label="column.prop"
              class="column-checkbox"
            >
              {{ column.label }}
            </el-checkbox>
          </div>

          <div class="column-group">
            <h4>日期时间</h4>
            <el-checkbox
              v-for="column in dateColumns"
              :key="column.prop"
              :label="column.prop"
              class="column-checkbox"
            >
              {{ column.label }}
            </el-checkbox>
          </div>

          <div class="column-group">
            <h4>其他信息</h4>
            <el-checkbox
              v-for="column in otherColumns"
              :key="column.prop"
              :label="column.prop"
              class="column-checkbox"
            >
              {{ column.label }}
            </el-checkbox>
          </div>
        </el-checkbox-group>

        <div class="setting-actions">
          <el-button size="small" @click="selectAllColumns">全选</el-button>
          <el-button size="small" @click="selectNoneColumns">全不选</el-button>
          <el-button size="small" @click="resetColumns">重置默认</el-button>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleTableSettingClose">取 消</el-button>
        <el-button type="primary" @click="handleTableSettingSave">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import {
  getExchangeRateConfigList,
  deleteExchangeRateConfig,
  createExchangeRateConfig,
  updateExchangeRateConfig,
  syncExchangeRateConfig,
  updateExchangeRateConfigStatus,
  getExchangeRateConfigStatistics
} from '@/api/globalTreasurer/xjgl/dataRulesManage/rate'

export default {
  name: 'ExchangeRateConfig',
  components: {},
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
      list: [],
      total: 0,
      listLoading: true,
      layout: 'total, sizes, prev, pager, next, jumper',
      listQuery: {
        pageNum: 1,
        pageSize: 20,
        baseCurrency: undefined,
        targetCurrency: undefined,
        effectiveDate: undefined,
        rateType: undefined
      },
      multipleSelection: [],
      lastUpdateTime: new Date().toLocaleString(),
      rateOverview: [
        { baseCurrency: 'USD', targetCurrency: 'CNY', rate: '--', change: 0, changePercent: 0 },
        { baseCurrency: 'EUR', targetCurrency: 'CNY', rate: '--', change: 0, changePercent: 0 },
        { baseCurrency: 'JPY', targetCurrency: 'CNY', rate: '--', change: 0, changePercent: 0 }
      ],
      rateIconClass: ['usd-icon', 'eur-icon', 'jpy-icon'],
      rateIconList: ['el-icon-money', 'el-icon-coin', 'el-icon-wallet'],
      exchangeRateDialogVisible: false,
      viewDialogVisible: false,
      tableSettingVisible: false,
      dialogStatus: 'create',
      currentExchangeRate: {
        baseCurrency: '',
        targetCurrency: '',
        exchangeRate: null,
        rateType: '',
        effectiveDate: '',
        expireDate: '',
        dataSource: '',
        status: 1
      },
      textMap: {
        update: '编辑汇率',
        create: '新增汇率'
      },
      rules: {
        baseCurrency: [{ required: true, message: '请选择基准币种', trigger: 'change' }],
        targetCurrency: [{ required: true, message: '请选择目标币种', trigger: 'change' }],
        exchangeRate: [{ required: true, message: '请输入汇率', trigger: 'blur' }],
        rateType: [{ required: true, message: '请选择汇率类型', trigger: 'change' }],
        effectiveDate: [{ required: true, message: '请选择生效日期', trigger: 'change' }],
        dataSource: [{ required: true, message: '请选择数据来源', trigger: 'change' }]
      },
      tableColumns: [
        { prop: 'currencyPair', label: '货币对', visible: true, sortable: true },
        { prop: 'exchangeRate', label: '汇率', visible: true, sortable: true },
        { prop: 'rateType', label: '汇率类型', visible: true, sortable: false },
        { prop: 'effectiveDate', label: '生效日期', visible: true, sortable: true },
        { prop: 'expireDate', label: '失效日期', visible: true, sortable: true },
        { prop: 'dataSource', label: '数据来源', visible: true, sortable: false },
        { prop: 'status', label: '状态', visible: true, sortable: false },
        { prop: 'updateTime', label: '更新时间', visible: true, sortable: true }
      ]
    }
  },
  computed: {
    visibleColumns: {
      get() {
        return this.tableColumns.filter(col => col.visible).map(col => col.prop)
      },
      set(value) {
        this.tableColumns.forEach(col => {
          col.visible = value.includes(col.prop)
        })
      }
    },
    basicColumns() {
      return this.tableColumns.filter(col =>
        ['currencyPair', 'exchangeRate', 'rateType', 'dataSource', 'status'].includes(col.prop)
      )
    },
    dateColumns() {
      return this.tableColumns.filter(col =>
        ['effectiveDate', 'expireDate', 'updateTime'].includes(col.prop)
      )
    },
    otherColumns() {
      return this.tableColumns.filter(col =>
        !this.basicColumns.some(bc => bc.prop === col.prop) &&
        !this.dateColumns.some(dc => dc.prop === col.prop)
      )
    },
  },
  created() {
    this.loadTableSettings()
    this.fetchData()
    this.fetchRateOverview()
  },
  methods: {
    // 获取列的可见性
    isColumnVisible(columnProp) {
      const column = this.tableColumns.find(col => col.prop === columnProp)
      return column ? column.visible : true
    },
    // 获取列的排序状态
    getColumnSortable(columnProp) {
      const column = this.tableColumns.find(col => col.prop === columnProp)
      return column ? column.sortable : false
    },
    async fetchData() {
      this.listLoading = true
      try {
        const response = await getExchangeRateConfigList(this.listQuery)
        if (response.code === 1) {
          const rawData = response.data || {}
          this.list = rawData.tlist || rawData.list || []
          this.total = Number(rawData.totalRecord || rawData.total) || this.list.length
        } else {
          this.$message.error(response.msg || '获取数据失败')
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取汇率配置列表失败:', error)
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },
    async fetchRateOverview() {
      try {
        const response = await getExchangeRateConfigStatistics()
        const successCodes = [200, '200', '1', 1, 2]
        if (successCodes.includes(response.code) && response.data) {
          // 汇率概览卡片数据
          if (response.data.rateOverview && Array.isArray(response.data.rateOverview)) {
            this.rateOverview = response.data.rateOverview.map(item => ({
              baseCurrency: item.baseCurrency || '--',
              targetCurrency: item.targetCurrency || '--',
              rate: item.rate != null ? parseFloat(item.rate).toFixed(4) : '--',
              change: item.change != null ? parseFloat(item.change) : 0,
              changePercent: item.changePercent != null ? parseFloat(item.changePercent) : 0
            }))
          }
          // 最后更新时间
          if (response.data.lastUpdateTime) {
            this.lastUpdateTime = new Date(response.data.lastUpdateTime).toLocaleString()
          }
        }
      } catch (error) {
        console.error('获取汇率概览数据失败:', error)
      }
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.fetchData()
    },
    handleReset() {
      this.listQuery = {
        pageNum: 1,
        pageSize: 20,
        baseCurrency: undefined,
        targetCurrency: undefined,
        effectiveDate: undefined,
        rateType: undefined
      }
      this.fetchData()
    },
    async handleSyncRate() {
      try {
        const response = await syncExchangeRateConfig({ sourceType: 'API' })
        const successCodes = [200, '200', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.$message.success(response.data || '汇率同步成功')
          this.lastUpdateTime = new Date().toLocaleString()
          this.fetchData()
          this.fetchRateOverview()
        } else {
          this.$message.error(response.msg || response.message || '同步失败')
        }
      } catch (error) {
        console.error('同步汇率失败:', error)
        this.$message.error('同步失败，请重试')
      }
    },
    handleExport() {
      this.exportExchangeRates()
    },
    handleTableSetting() {
      this.tableSettingVisible = true
    },
    async exportExchangeRates() {
      try {
        this.$message({
          type: 'success',
          message: '导出成功'
        })

        // 创建下载链接
        const dataStr = JSON.stringify(this.list, null, 2)
        const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr)

        const exportFileDefaultName = `汇率配置数据_${new Date().toISOString().split('T')[0]}.json`

        const linkElement = document.createElement('a')
        linkElement.setAttribute('href', dataUri)
        linkElement.setAttribute('download', exportFileDefaultName)
        linkElement.click()

      } catch (error) {
        this.$message.error('导出失败，请重试')
      }
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.status === 0) {
        return 'disabled-row'
      }
      return ''
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    formatRate(rate) {
      return parseFloat(rate).toFixed(4)
    },
    getRateTypeColor(type) {
      const colorMap = {
        'SPOT': 'success',
        'CASH': 'warning',
        'MIDDLE': 'info'
      }
      return colorMap[type] || 'info'
    },
    getRateTypeText(type) {
      const textMap = {
        'SPOT': '现汇',
        'CASH': '现钞',
        'MIDDLE': '中间价'
      }
      return textMap[type] || '未知'
    },
    getSourceColor(source) {
      const colorMap = {
        'MANUAL': 'warning',
        'AUTO': 'success',
        'API': 'info'
      }
      return colorMap[source] || 'info'
    },
    getSourceText(source) {
      const textMap = {
        'MANUAL': '手工录入',
        'AUTO': '自动获取',
        'API': 'API接口'
      }
      return textMap[source] || '未知'
    },
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      if (isNaN(d.getTime())) return date
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${y}-${m}-${day}`
    },
    formatTime(time) {
      if (!time) return '-'
      return new Date(time).toLocaleString()
    },
    async handleStatusChange(row) {
      try {
        const response = await updateExchangeRateConfigStatus({ id: row.id, status: row.status })
        const successCodes = [200, '200', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.$message.success('状态更新成功')
        } else {
          this.$message.error(response.msg || response.message || '状态更新失败')
          // 回滚状态
          row.status = row.status === 1 ? 0 : 1
        }
      } catch (error) {
        console.error('状态更新失败:', error)
        this.$message.error('状态更新失败，请重试')
        row.status = row.status === 1 ? 0 : 1
      }
    },
    handleView(row) {
      this.currentExchangeRate = { ...row }
      this.viewDialogVisible = true
    },
    handleCreate() {
      this.dialogStatus = 'create'
      this.resetCurrentExchangeRate()
      this.exchangeRateDialogVisible = true
    },
    handleUpdate(row) {
      this.dialogStatus = 'update'
      this.currentExchangeRate = { ...row }
      this.exchangeRateDialogVisible = true
    },
    /**
     * 重置当前汇率表单数据
     */
    resetCurrentExchangeRate() {
      this.currentExchangeRate = {
        baseCurrency: '',
        targetCurrency: '',
        exchangeRate: null,
        rateType: '',
        effectiveDate: '',
        expireDate: '',
        dataSource: '',
        status: 1
      }
    },
    /**
     * 对话框打开时的处理
     */
    handleDialogOpen() {
      // 确保表单重置
      this.$nextTick(() => {
        if (this.$refs.dataForm) {
          this.$refs.dataForm.clearValidate()
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const response = await deleteExchangeRateConfig({ id: row.id })
          const successCodes = [200, '200', '1', 1, 2]
          if (successCodes.includes(response.code)) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(response.msg || response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除汇率配置失败:', error)
          this.$message.error('删除失败，请重试')
        }
      })
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val
      this.fetchData()
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      this.fetchData()
    },
    handleDialogClose() {
      this.exchangeRateDialogVisible = false
    },
    handleViewDialogClose() {
      this.viewDialogVisible = false
    },
    handleEditFromView(exchangeRate) {
      this.dialogStatus = 'update'
      if (exchangeRate && exchangeRate.id) {
        this.currentExchangeRate = { ...exchangeRate }
      }
      this.viewDialogVisible = false
      this.exchangeRateDialogVisible = true
    },
    createData() {
      this.$refs.dataForm.validate(async(valid) => {
        if (valid) {
          try {
            const response = await createExchangeRateConfig(this.currentExchangeRate)
            const successCodes = [200, '200', '1', 1, 2]
            if (successCodes.includes(response.code)) {
              this.exchangeRateDialogVisible = false
              this.$message.success('创建成功')
              this.fetchData()
            } else {
              this.$message.error(response.msg || response.message || '创建失败')
            }
          } catch (error) {
            console.error('创建汇率配置失败:', error)
            this.$message.error('创建失败，请重试')
          }
        }
      })
    },
    updateData() {
      this.$refs.dataForm.validate(async(valid) => {
        if (valid) {
          try {
            const response = await updateExchangeRateConfig(this.currentExchangeRate)
            const successCodes = [200, '200', '1', 1, 2]
            if (successCodes.includes(response.code)) {
              this.exchangeRateDialogVisible = false
              this.$message.success('更新成功')
              this.fetchData()
            } else {
              this.$message.error(response.msg || response.message || '更新失败')
            }
          } catch (error) {
            console.error('更新汇率配置失败:', error)
            this.$message.error('更新失败，请重试')
          }
        }
      })
    },
    loadTableSettings() {
      // 从localStorage加载表格设置
      const savedColumns = localStorage.getItem('exchangeRateTableColumns')
      if (savedColumns) {
        try {
          this.tableColumns = JSON.parse(savedColumns)
        } catch (e) {
          console.warn('表格设置加载失败，使用默认配置')
        }
      }
    },
    // 表格设置相关方法
    handleTableSettingClose() {
      this.tableSettingVisible = false
    },
    handleTableSettingSave() {
      // 保存表格设置到localStorage
      localStorage.setItem('exchangeRateTableColumns', JSON.stringify(this.tableColumns))
      this.$message.success('表格设置已保存')
      this.tableSettingVisible = false
    },
    handleColumnChange(value) {
      // 处理列显示状态变化
      this.visibleColumns = value
    },
    selectAllColumns() {
      this.tableColumns.forEach(col => {
        col.visible = true
      })
    },
    selectNoneColumns() {
      this.tableColumns.forEach(col => {
        col.visible = false
      })
    },
    resetColumns() {
      // 重置为默认列配置
      this.tableColumns = [
        { prop: 'currencyPair', label: '货币对', visible: true, sortable: true },
        { prop: 'exchangeRate', label: '汇率', visible: true, sortable: true },
        { prop: 'rateType', label: '汇率类型', visible: true, sortable: false },
        { prop: 'effectiveDate', label: '生效日期', visible: true, sortable: true },
        { prop: 'expireDate', label: '失效日期', visible: true, sortable: true },
        { prop: 'dataSource', label: '数据来源', visible: true, sortable: false },
        { prop: 'status', label: '状态', visible: true, sortable: false },
        { prop: 'updateTime', label: '更新时间', visible: true, sortable: true }
      ]
    },
  }
}
</script>

<style lang="scss" scoped>
.exchange-rate-config {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 8px;
      color: white;

      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            font-size: 28px;
          }
        }

        .page-description {
          margin: 0;
          opacity: 0.9;
          font-size: 14px;
        }
      }

      .header-right {
        .el-button {
          margin-left: 12px;
        }
      }
    }
  }

  .rate-overview {
    margin-bottom: 20px;

    .overview-card {
      border-radius: 8px;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      }

      .card-content {
        display: flex;
        align-items: center;
        padding: 10px;

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;

          i {
            font-size: 24px;
            color: white;
          }

          &.usd-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.eur-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &.jpy-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &.update-icon {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 4px;
          }

          .card-value {
            font-size: 20px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }

          .card-change {
            font-size: 12px;

            &.positive {
              color: #67c23a;
            }

            &.negative {
              color: #f56c6c;
            }
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;

    .currency-option {
      display: flex;
      align-items: center;

      .currency-flag {
        margin-right: 8px;
        font-weight: bold;
        color: #409eff;
      }
    }
  }

  .table-card {
    border-radius: 8px;

    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .table-title {
        .title-text {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }

        .title-count {
          margin-left: 12px;
          color: #909399;
          font-size: 14px;
        }
      }
    }

    .currency-pair {
      display: flex;
      align-items: center;
      justify-content: center;

      .base-currency {
        font-weight: 600;
        color: #409eff;
      }

      i {
        margin: 0 8px;
        color: #909399;
      }

      .target-currency {
        font-weight: 600;
        color: #67c23a;
      }
    }

    .rate-value {
      .rate-number {
        font-family: 'Monaco', 'Menlo', monospace;
        font-size: 16px;
        font-weight: 600;
        color: #e6a23c;
      }
    }

    .effective-date,
    .expire-date {
      color: #606266;
      font-size: 12px;
    }

    .update-time {
      display: flex;
      align-items: center;
      color: #909399;
      font-size: 12px;

      i {
        margin-right: 4px;
      }
    }

    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}

// 全局样式
::v-deep .el-table {
  .disabled-row {
    background-color: #f5f7fa;
    color: #c0c4cc;
  }

  .el-table__row:hover {
    background-color: #f5f7fa;
  }
}

::v-deep .el-card__body {
  padding: 20px;
}

::v-deep .el-form--inline .el-form-item {
  margin-right: 20px;
  margin-bottom: 0;
}

::v-deep .el-button-group .el-button {
  margin-left: 0;
}

// 表格设置对话框样式
.table-setting-content {
  .column-group {
    margin-bottom: 20px;

    h4 {
      margin: 0 0 10px 0;
      font-size: 14px;
      color: #303133;
      border-bottom: 1px solid #ebeef5;
      padding-bottom: 5px;
    }

    .column-checkbox {
      margin-right: 20px;
      margin-bottom: 8px;
    }
  }

  .setting-actions {
    margin-top: 20px;
    text-align: center;
    padding-top: 15px;
    border-top: 1px solid #ebeef5;

    .el-button {
      margin: 0 5px;
    }
  }
}
</style>

