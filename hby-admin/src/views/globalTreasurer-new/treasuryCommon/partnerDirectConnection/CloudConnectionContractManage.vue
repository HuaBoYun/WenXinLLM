<template>
  <div class="cloud-connection-contract-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-document-copy"></i>
            云连接合同管理
          </h2>
          <p class="page-description">管理云连接服务合同，包括合同签署、条款管理、履约监控和续约提醒</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增合同
          </el-button>
          <el-button type="warning" icon="el-icon-bell" @click="handleRenewalAlert">
            续约提醒
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出合同
          </el-button>
          <el-button
            v-if="multipleSelection.length > 0"
            type="danger"
            icon="el-icon-delete"
            @click="handleBatchDelete"
          >
            批量删除({{ multipleSelection.length }})
          </el-button>
        </div>
      </div>
    </div>

    <!-- 合同统计卡片 -->
    <div class="contract-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总合同数</div>
                <div class="card-value">{{ totalContracts }}</div>
                <div class="card-change">已签署合同</div>
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
                <div class="card-title">执行中</div>
                <div class="card-value">{{ activeContracts }}</div>
                <div class="card-change positive">正常履约</div>
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
                <div class="card-value">{{ expiringContracts }}</div>
                <div class="card-change negative">需要续约</div>
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
                <div class="card-title">合同总额</div>
                <div class="card-value">{{ totalAmount }}</div>
                <div class="card-change">万元</div>
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
          <el-form-item label="合同编号">
            <el-input
              v-model="listQuery.contractNumber"
              placeholder="请输入合同编号"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="合同名称">
            <el-input
              v-model="listQuery.contractName"
              placeholder="请输入合同名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="服务提供商">
            <el-select
              v-model="listQuery.serviceProvider"
              placeholder="请选择服务提供商"
              clearable
              style="width: 150px;"
            >
              <el-option label="阿里云" value="ALIYUN" />
              <el-option label="腾讯云" value="TENCENT_CLOUD" />
              <el-option label="华为云" value="HUAWEI_CLOUD" />
              <el-option label="百度云" value="BAIDU_CLOUD" />
              <el-option label="京东云" value="JDCLOUD" />
              <el-option label="其他" value="OTHER" />
            </el-select>
          </el-form-item>
          <el-form-item label="服务类型">
            <el-select
              v-model="listQuery.serviceType"
              placeholder="请选择服务类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="云服务器" value="ECS" />
              <el-option label="云数据库" value="RDS" />
              <el-option label="云存储" value="OSS" />
              <el-option label="CDN服务" value="CDN" />
              <el-option label="负载均衡" value="SLB" />
              <el-option label="API网关" value="API_GATEWAY" />
            </el-select>
          </el-form-item>
          <el-form-item label="合同状态">
            <el-select
              v-model="listQuery.contractStatus"
              placeholder="请选择合同状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="草稿" value="DRAFT" />
              <el-option label="待审批" value="PENDING_APPROVAL" />
              <el-option label="已签署" value="SIGNED" />
              <el-option label="执行中" value="ACTIVE" />
              <el-option label="已到期" value="EXPIRED" />
              <el-option label="已终止" value="TERMINATED" />
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
      @selection-change="handleSelectionChange"
      :row-class-name="tableRowClassName"
      :cell-class-name="tableCellClassName"
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"
      />
      <el-table-column label="合同编号" prop="contractNumber" sortable="custom" align="center" width="150" />
      <el-table-column label="合同名称" prop="contractName" width="200px" align="center" show-overflow-tooltip />
      <el-table-column label="服务提供商" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getServiceProviderColor(row.serviceProvider)" size="small">
            {{ getServiceProviderName(row.serviceProvider) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="服务类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getServiceTypeColor(row.serviceType)" size="small">
            {{ getServiceTypeName(row.serviceType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="合同金额" prop="contractAmount" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.contractAmount | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="签署日期" prop="signDate" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.signDate | formatDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生效日期" prop="effectiveDate" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.effectiveDate | formatDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到期日期" prop="expiryDate" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.expiryDate | formatDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同状态" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getContractStatusColor(row.contractStatus)" size="small">
            {{ getContractStatusName(row.contractStatus) }}
          </el-tag>
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

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      :title="dialogStatus === 'create' ? '新增云连接合同' : '编辑云连接合同'"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      width="800px"
      @close="resetForm"
    >
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="formData"
        label-position="right"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractNumber">
              <el-input v-model="formData.contractNumber" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="formData.contractName" placeholder="请输入合同名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="服务提供商" prop="serviceProvider">
              <el-select v-model="formData.serviceProvider" placeholder="请选择服务提供商" style="width: 100%;">
                <el-option label="阿里云" value="ALIYUN" />
                <el-option label="腾讯云" value="TENCENT_CLOUD" />
                <el-option label="华为云" value="HUAWEI_CLOUD" />
                <el-option label="百度云" value="BAIDU_CLOUD" />
                <el-option label="京东云" value="JDCLOUD" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务类型" prop="serviceType">
              <el-select v-model="formData.serviceType" placeholder="请选择服务类型" style="width: 100%;">
                <el-option label="云服务器" value="ECS" />
                <el-option label="云数据库" value="RDS" />
                <el-option label="云存储" value="OSS" />
                <el-option label="CDN服务" value="CDN" />
                <el-option label="负载均衡" value="SLB" />
                <el-option label="API网关" value="API_GATEWAY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合同金额" prop="contractAmount">
              <el-input-number
                v-model="formData.contractAmount"
                :min="0"
                :precision="2"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currency">
              <el-select v-model="formData.currency" placeholder="请选择币种" style="width: 100%;">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="港币" value="HKD" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签署日期" prop="signDate">
              <el-date-picker
                v-model="formData.signDate"
                type="date"
                placeholder="选择签署日期"
                value-format="yyyy-MM-dd"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="formData.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                value-format="yyyy-MM-dd"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="到期日期" prop="expiryDate">
              <el-date-picker
                v-model="formData.expiryDate"
                type="date"
                placeholder="选择到期日期"
                value-format="yyyy-MM-dd"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同状态" prop="contractStatus">
              <el-select v-model="formData.contractStatus" placeholder="请选择合同状态" style="width: 100%;">
                <el-option label="草稿" value="DRAFT" />
                <el-option label="待审批" value="PENDING_APPROVAL" />
                <el-option label="已签署" value="SIGNED" />
                <el-option label="执行中" value="ACTIVE" />
                <el-option label="已到期" value="EXPIRED" />
                <el-option label="已终止" value="TERMINATED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="付款方式" prop="paymentMethod">
              <el-select v-model="formData.paymentMethod" placeholder="请选择付款方式" style="width: 100%;">
                <el-option label="预付" value="PREPAID" />
                <el-option label="后付" value="POSTPAID" />
                <el-option label="分期" value="INSTALLMENT" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款周期" prop="paymentCycle">
              <el-select v-model="formData.paymentCycle" placeholder="请选择付款周期" style="width: 100%;">
                <el-option label="月付" value="MONTHLY" />
                <el-option label="季付" value="QUARTERLY" />
                <el-option label="半年付" value="SEMI_ANNUAL" />
                <el-option label="年付" value="ANNUALLY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="服务级别协议(SLA)" prop="sla">
              <el-input
                v-model="formData.sla"
                type="textarea"
                :rows="2"
                placeholder="请输入服务级别协议内容"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="合同描述" prop="description">
              <el-input
                v-model="formData.description"
                type="textarea"
                :rows="3"
                placeholder="请输入合同描述"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleDialogSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 续约提醒弹窗 -->
    <el-dialog
      title="续约提醒"
      :visible.sync="renewalDialogVisible"
      width="1000px"
    >
      <el-table
        :data="renewalList"
        border
        style="width: 100%;"
      >
        <el-table-column label="合同编号" prop="contractNumber" width="150" align="center" />
        <el-table-column label="合同名称" prop="contractName" width="200" align="center" show-overflow-tooltip />
        <el-table-column label="服务提供商" width="120" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getServiceProviderColor(row.serviceProvider)" size="small">
              {{ getServiceProviderName(row.serviceProvider) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="合同金额" width="120" align="center">
          <template slot-scope="{row}">
            {{ row.contractAmount | currency }}
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120" align="center">
          <template slot-scope="{row}">
            <span style="color: #f56c6c;">{{ row.expiryDate | parseTime('{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="剩余天数" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag type="danger" size="small">{{ getDaysRemaining(row.expiryDate) }}天</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="120">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleRenew(row)">续约</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="renewalDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { getCloudContractList, getCloudContractDetail, createCloudContract, updateCloudContract, deleteCloudContract, batchDeleteCloudContract, exportCloudContract, getCloudContractStatistics, getContractRenewalAlert, toggleCloudContract } from '@/api/globalTreasurer/xjgl/partnerDirectConnection/contract'

export default {
  name: 'CloudConnectionContractManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    },
    // 格式化金额显示
    currency(value) {
      if (value === null || value === undefined || value === '') {
        return '0.00'
      }
      const num = parseFloat(value)
      if (isNaN(num)) {
        return '0.00'
      }
      return num.toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }) + '元'
    },
    // 格式化日期显示
    formatDate(value) {
      if (!value) return ''
      // 处理时间戳格式（Long类型）
      if (typeof value === 'number') {
        const date = new Date(value)
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        return `${year}-${month}-${day}`
      }
      // 处理字符串格式（YYYY-MM-DD）
      if (typeof value === 'string') {
        return value.substring(0, 10)
      }
      return value
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        contractNumber: undefined,
        contractName: undefined,
        serviceProvider: undefined,
        serviceType: undefined,
        contractStatus: undefined
      },
      // 统计数据
      totalContracts: 0,
      activeContracts: 0,
      expiringContracts: 0,
      totalAmount: '0',
      // 批量选择
      multipleSelection: [],
      // 弹窗相关
      dialogFormVisible: false,
      dialogStatus: 'create',
      submitLoading: false,
      formData: {
        contractId: undefined,
        contractNumber: '',
        contractName: '',
        serviceProvider: '',
        serviceType: '',
        contractAmount: 0,
        currency: 'CNY',
        signDate: '',
        effectiveDate: '',
        expiryDate: '',
        contractStatus: 'DRAFT',
        paymentMethod: '',
        paymentCycle: '',
        sla: '',
        description: ''
      },
      // 续约提醒
      renewalDialogVisible: false,
      renewalList: [],
      // 表单验证规则
      rules: {
        contractNumber: [
          { required: true, message: '请输入合同编号', trigger: 'blur' }
        ],
        contractName: [
          { required: true, message: '请输入合同名称', trigger: 'blur' }
        ],
        serviceProvider: [
          { required: true, message: '请选择服务提供商', trigger: 'change' }
        ],
        serviceType: [
          { required: true, message: '请选择服务类型', trigger: 'change' }
        ],
        contractAmount: [
          { required: true, message: '请输入合同金额', trigger: 'blur' },
          { type: 'number', min: 0, message: '合同金额不能小于0', trigger: 'blur' }
        ],
        currency: [
          { required: true, message: '请选择币种', trigger: 'change' }
        ],
        signDate: [
          { required: true, message: '请选择签署日期', trigger: 'change' }
        ],
        effectiveDate: [
          { required: true, message: '请选择生效日期', trigger: 'change' }
        ],
        expiryDate: [
          { required: true, message: '请选择到期日期', trigger: 'change' }
        ],
        contractStatus: [
          { required: true, message: '请选择合同状态', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    // 使用$nextTick确保组件完全渲染后再发起请求，避免并发请求导致的参数错误
    this.$nextTick(() => {
      this.getList()
    })
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const query = {
          pageNo: this.listQuery.page,
          pageSize: this.listQuery.limit,
          contractNumber: this.listQuery.contractNumber,
          contractName: this.listQuery.contractName,
          serviceProvider: this.listQuery.serviceProvider,
          serviceType: this.listQuery.serviceType,
          contractStatus: this.listQuery.contractStatus
        }

        const response = await getCloudContractList(query)
        if (response && response.code === 1) {
          // 后端返回的数据结构
          const rawData = response.data || {}
          // 处理数据：后端返回 tlist，前端期望数组
          const dataList = Array.isArray(rawData.tlist) ? rawData.tlist : []

          // 直接使用后端返回的数据，不做字段映射
          this.list = dataList

          this.total = parseInt(rawData.totalRecord) || 0
          this.tableKey = this.tableKey + 1
          await this.getStatistics()
          console.log('数据加载成功:', {
            listLength: this.list.length,
            total: this.total,
            list: this.list
          })
        } else {
          this.$message.error(response?.message || '获取数据失败')
          console.error('获取数据失败:', response)
        }
      } catch (error) {
        console.error('获取云连接合同列表失败:', error)
        this.$message.error('获取数据失败，请稍后重试')
      } finally {
        this.listLoading = false
      }
    },
    async getStatistics() {
      try {
        const response = await getCloudContractStatistics()
        if (response && response.code === 1) {
          const stats = response.data
          // 后端返回的字段名是大写格式
          this.totalContracts = parseInt(stats.TOTALCONTRACTS || stats.totalContracts || 0)
          this.activeContracts = parseInt(stats.ACTIVECONTRACTS || stats.activeContracts || 0)
          this.expiringContracts = parseInt(stats.EXPIRINGCONTRACTS || stats.expiringContracts || 0)
          this.totalAmount = (parseFloat(stats.TOTALAMOUNT || stats.totalAmount || 0) / 10000).toFixed(2) + '万元'
          console.log('统计数据:', {
            totalContracts: this.totalContracts,
            activeContracts: this.activeContracts,
            expiringContracts: this.expiringContracts,
            totalAmount: this.totalAmount
          })
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.list = []
      this.getList()
    },
    handleCreate() {
      this.dialogStatus = 'create'
      this.resetForm()
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm && this.$refs.dataForm.clearValidate()
      })
    },
    handleUpdate(row) {
      this.dialogStatus = 'update'
      // 获取详情
      getCloudContractDetail(row.contractId).then(response => {
        if (response && response.code === 1) {
          const data = response.data || {}
          // 处理日期格式：将后端返回的日期转换为 yyyy-MM-dd 格式
          this.formData = {
            ...data,
            signDate: this.formatDateForPicker(data.signDate),
            effectiveDate: this.formatDateForPicker(data.effectiveDate),
            expiryDate: this.formatDateForPicker(data.expiryDate)
          }
          console.log('编辑表单数据:', this.formData)
          this.dialogFormVisible = true
          this.$nextTick(() => {
            this.$refs.dataForm && this.$refs.dataForm.clearValidate()
          })
        } else {
          this.$message.error(response?.message || '获取详情失败')
        }
      }).catch(error => {
        console.error('获取详情失败:', error)
        this.$message.error('获取详情失败，请稍后重试')
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要删除的记录')
        return
      }
      this.$confirm(`确定要删除选中的 ${this.multipleSelection.length} 条记录吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = this.multipleSelection.map(item => item.contractId)
        batchDeleteCloudContract(ids).then(response => {
          if (response && response.code === 1) {
            this.$notify({
              title: '成功',
              message: '批量删除成功',
              type: 'success',
              duration: 2000
            })
            this.multipleSelection = []
            this.getList()
          } else {
            this.$message.error(response?.message || '批量删除失败')
          }
        }).catch(error => {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败，请稍后重试')
        })
      }).catch(() => {
        // 取消删除
      })
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCloudContract(row.contractId).then(response => {
          if (response && response.code === 1) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          } else {
            this.$message.error(response?.message || '删除失败')
          }
        }).catch(error => {
          console.error('删除失败:', error)
          this.$message.error('删除失败，请稍后重试')
        })
      }).catch(() => {
        // 取消删除
      })
    },
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        contractNumber: undefined,
        contractName: undefined,
        serviceProvider: undefined,
        serviceType: undefined,
        contractStatus: undefined
      }
      this.getList()
    },
    // 表单提交
    handleDialogSubmit() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.submitLoading = true
          const isEdit = this.dialogStatus === 'update'
          const apiMethod = isEdit ? updateCloudContract : createCloudContract

          apiMethod(this.formData).then(response => {
            this.submitLoading = false
            if (response && response.code === 1) {
              this.$message.success(isEdit ? '更新成功' : '创建成功')
              this.dialogFormVisible = false
              this.getList()
            } else {
              this.$message.error(response?.message || (isEdit ? '更新失败' : '创建失败'))
            }
          }).catch(error => {
            this.submitLoading = false
            console.error('提交失败:', error)
            this.$message.error('操作失败，请稍后重试')
          })
        } else {
          return false
        }
      })
    },
    // 重置表单
    resetForm() {
      this.formData = {
        contractId: undefined,
        contractNumber: '',
        contractName: '',
        serviceProvider: '',
        serviceType: '',
        contractAmount: 0,
        currency: 'CNY',
        signDate: '',
        effectiveDate: '',
        expiryDate: '',
        contractStatus: 'DRAFT',
        paymentMethod: '',
        paymentCycle: '',
        sla: '',
        description: ''
      }
      this.$nextTick(() => {
        this.$refs.dataForm && this.$refs.dataForm.clearValidate()
      })
    },
    handleRenewalAlert() {
      this.renewalList = []
      getContractRenewalAlert().then(response => {
        if (response && response.code === 1) {
          this.renewalList = response.data || []
          if (this.renewalList.length === 0) {
            this.$message.info('暂无即将到期的合同')
          } else {
            this.renewalDialogVisible = true
          }
        } else {
          this.$message.error(response?.message || '获取续约提醒失败')
        }
      }).catch(error => {
        console.error('获取续约提醒失败:', error)
        this.$message.error('获取续约提醒失败，请稍后重试')
      })
    },
    handleRenew(row) {
      // 续约功能，可以打开编辑弹窗并修改到期日期
      this.renewalDialogVisible = false
      this.handleUpdate(row)
      this.$message.info('请在编辑弹窗中修改到期日期完成续约')
    },
    handleExport() {
      const loading = this.$loading({
        lock: true,
        text: '正在导出，请稍候...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      exportCloudContract({
        contractNumber: this.listQuery.contractNumber,
        contractName: this.listQuery.contractName,
        serviceProvider: this.listQuery.serviceProvider,
        serviceType: this.listQuery.serviceType,
        contractStatus: this.listQuery.contractStatus
      }).then(response => {
        loading.close()
        // 创建下载链接
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `云连接合同_${new Date().getTime()}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      }).catch(error => {
        loading.close()
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      })
    },
    getContractStatusColor(status) {
      const colorMap = {
        'PENDING': 'info',
        'ACTIVE': 'success',
        'SUSPENDED': 'warning',
        'TERMINATED': 'danger',
        'EXPIRED': 'info'
      }
      return colorMap[status] || 'info'
    },
    getContractStatusName(status) {
      const nameMap = {
        'PENDING': '待生效',
        'ACTIVE': '执行中',
        'SUSPENDED': '已暂停',
        'TERMINATED': '已终止',
        'EXPIRED': '已到期'
      }
      return nameMap[status] || status
    },
    getServiceProviderName(provider) {
      const nameMap = {
        'ALIYUN': '阿里云',
        'TENCENT': '腾讯云',
        'TENCENT_CLOUD': '腾讯云',
        'HUAWEI': '华为云',
        'HUAWEI_CLOUD': '华为云',
        'BAIDU_CLOUD': '百度云',
        'JDCLOUD': '京东云',
        'AWS': 'AWS',
        'AZURE': 'Azure',
        'OTHER': '其他'
      }
      return nameMap[provider] || provider
    },
    getServiceProviderColor(provider) {
      const colorMap = {
        'ALIYUN': '',
        'TENCENT': 'success',
        'TENCENT_CLOUD': 'success',
        'HUAWEI': 'warning',
        'HUAWEI_CLOUD': 'warning',
        'BAIDU_CLOUD': 'info',
        'JDCLOUD': 'danger',
        'AWS': '',
        'AZURE': 'success',
        'OTHER': 'info'
      }
      return colorMap[provider] || 'info'
    },
    getServiceTypeName(type) {
      const nameMap = {
        'IaaS': '基础设施',
        'PaaS': '平台服务',
        'SaaS': '软件服务',
        'ECS': '云服务器',
        'RDS': '云数据库',
        'OSS': '云存储',
        'CDN': 'CDN服务',
        'SLB': '负载均衡',
        'API_GATEWAY': 'API网关'
      }
      return nameMap[type] || type
    },
    getServiceTypeColor(type) {
      const colorMap = {
        'IaaS': '',
        'PaaS': 'success',
        'SaaS': 'warning',
        'ECS': '',
        'RDS': 'success',
        'OSS': 'warning',
        'CDN': 'info',
        'SLB': 'danger',
        'API_GATEWAY': ''
      }
      return colorMap[type] || 'info'
    },
    // 计算剩余天数
    getDaysRemaining(expiryDate) {
      if (!expiryDate) return 0
      const now = new Date()
      const expiry = new Date(expiryDate)
      const diff = expiry.getTime() - now.getTime()
      const days = Math.ceil(diff / (1000 * 60 * 60 * 24))
      return days > 0 ? days : 0
    },
    // 格式化日期为 yyyy-MM-dd 格式，供 el-date-picker 使用
    formatDateForPicker(date) {
      if (!date) return ''
      // 如果已经是 yyyy-MM-dd 格式的字符串，直接返回
      if (typeof date === 'string' && /^\d{4}-\d{2}-\d{2}$/.test(date)) {
        return date
      }
      // 如果是时间戳或 Date 对象，转换为 yyyy-MM-dd 格式
      try {
        const d = new Date(date)
        if (isNaN(d.getTime())) return ''
        const year = d.getFullYear()
        const month = String(d.getMonth() + 1).padStart(2, '0')
        const day = String(d.getDate()).padStart(2, '0')
        return `${year}-${month}-${day}`
      } catch (e) {
        console.error('日期格式化失败:', date, e)
        return ''
      }
    }
  }
}
</script>
