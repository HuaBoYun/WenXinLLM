<template>
  <div class="account-limit-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-warning-outline"></i>
            账户限额管理
          </h2>
          <p class="page-description">管理银行账户的各类限额设置，包括交易限额、日限额、月限额等风险控制参数</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增限额
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleBatchUpdate">
            批量更新
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 限额统计卡片 -->
    <div class="limit-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总限额数</div>
                <div class="card-value">{{ totalLimits }}</div>
                <div class="card-change">个限额</div>
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
                <div class="card-title">有效限额</div>
                <div class="card-value">{{ activeLimits }}</div>
                <div class="card-change positive">{{ activeRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon warning-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">接近限额</div>
                <div class="card-value">{{ nearLimitAccounts }}</div>
                <div class="card-change warning">需关注</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon exceeded-icon">
                <i class="el-icon-error"></i>
              </div>
              <div class="card-info">
                <div class="card-title">超限账户</div>
                <div class="card-value">{{ exceededAccounts }}</div>
                <div class="card-change negative">需处理</div>
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
          <el-form-item label="限额ID">
            <el-input
              v-model="listQuery.limitId"
              placeholder="请输入限额ID"
              style="width: 120px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="账户号码">
            <el-input
              v-model="listQuery.accountNumber"
              placeholder="请输入账户号码"
              style="width: 180px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="限额类型">
            <el-select
              v-model="listQuery.limitType"
              placeholder="请选择限额类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="单笔限额" value="SINGLE_TRANSACTION" />
              <el-option label="日限额" value="DAILY_LIMIT" />
              <el-option label="月限额" value="MONTHLY_LIMIT" />
              <el-option label="年限额" value="YEARLY_LIMIT" />
              <el-option label="余额限额" value="BALANCE_LIMIT" />
            </el-select>
          </el-form-item>
          <el-form-item label="限额状态">
            <el-select
              v-model="listQuery.limitStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="有效" value="ACTIVE" />
              <el-option label="无效" value="INACTIVE" />
              <el-option label="暂停" value="SUSPENDED" />
              <el-option label="过期" value="EXPIRED" />
            </el-select>
          </el-form-item>
          <el-form-item label="创建日期">
            <el-date-picker
              v-model="listQuery.createDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button v-waves class="filter-item" type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 限额表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        @sort-change="sortChange"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="限额ID" prop="limitId" sortable="custom" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.limitId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="账户号码" width="180px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewAccount(row)">{{ row.accountNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="账户名称" min-width="150px">
          <template slot-scope="{row}">
            <span>{{ row.accountName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="限额类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getLimitTypeTagType(row.limitType)" size="mini">
              {{ getLimitTypeText(row.limitType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="限额金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="limit-amount">{{ formatCurrency(row.limitAmount, row.currencyCode) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已使用" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="used-amount">{{ formatCurrency(row.usedAmount, row.currencyCode) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="使用率" width="100px" align="center">
          <template slot-scope="{row}">
            <el-progress 
              :percentage="getUsagePercentage(row)" 
              :status="getUsageStatus(row)"
              :stroke-width="8"
              :show-text="false"
            />
            <div class="usage-text" :class="getUsageClass(row)">
              {{ getUsagePercentage(row) }}%
            </div>
          </template>
        </el-table-column>
        <el-table-column label="限额状态" class-name="status-col" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getLimitStatusTagType(row.limitStatus)">
              {{ getLimitStatusText(row.limitStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="生效日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.effectiveDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.expireDate || '永久' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              查看
            </el-button>
            <el-button size="mini" type="warning" @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button v-if="row.limitStatus === 'ACTIVE'" size="mini" type="info" @click="handleSuspend(row)">
              暂停
            </el-button>
            <el-button v-if="row.limitStatus === 'SUSPENDED'" size="mini" type="success" @click="handleActivate(row)">
              激活
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑限额对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-form-item label="选择账户" prop="accountId">
          <el-select v-model="temp.accountId" placeholder="请选择要设置限额的账户" style="width: 100%;" @change="handleAccountChange">
            <el-option
              v-for="account in availableAccounts"
              :key="account.accountId"
              :label="`${account.accountNumber} - ${account.accountName}`"
              :value="account.accountId"
            />
          </el-select>
        </el-form-item>
        <el-row :gutter="20" v-if="temp.accountId">
          <el-col :span="12">
            <el-form-item label="账户号码">
              <el-input v-model="temp.accountNumber" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户名称">
              <el-input v-model="temp.accountName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="限额类型" prop="limitType">
              <el-select v-model="temp.limitType" placeholder="请选择限额类型" style="width: 100%;">
                <el-option label="单笔限额" value="SINGLE_TRANSACTION" />
                <el-option label="日限额" value="DAILY_LIMIT" />
                <el-option label="月限额" value="MONTHLY_LIMIT" />
                <el-option label="年限额" value="YEARLY_LIMIT" />
                <el-option label="余额限额" value="BALANCE_LIMIT" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="限额状态" prop="limitStatus">
              <el-select v-model="temp.limitStatus" placeholder="请选择限额状态" style="width: 100%;">
                <el-option label="有效" value="ACTIVE" />
                <el-option label="无效" value="INACTIVE" />
                <el-option label="暂停" value="SUSPENDED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="限额金额" prop="limitAmount">
              <el-input-number
                v-model="temp.limitAmount"
                :precision="2"
                :min="0"
                style="width: 100%;"
                placeholder="请输入限额金额"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="temp.currencyCode" placeholder="请选择币种" style="width: 100%;">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
                <el-option label="英镑" value="GBP" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="temp.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期">
              <el-date-picker
                v-model="temp.expireDate"
                type="date"
                placeholder="选择到期日期（可选）"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="预警阈值(%)">
          <el-slider
            v-model="temp.warningThreshold"
            :min="50"
            :max="95"
            :step="5"
            show-stops
            show-input
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="限额描述">
          <el-input v-model="temp.limitDescription" type="textarea" :rows="3" placeholder="请输入限额描述" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
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

    <!-- 限额详情对话框 -->
    <el-dialog title="限额详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentLimit" class="limit-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="限额ID">{{ currentLimit.limitId }}</el-descriptions-item>
          <el-descriptions-item label="限额状态">
            <el-tag :type="getLimitStatusTagType(currentLimit.limitStatus)">
              {{ getLimitStatusText(currentLimit.limitStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="账户号码">{{ currentLimit.accountNumber }}</el-descriptions-item>
          <el-descriptions-item label="账户名称">{{ currentLimit.accountName }}</el-descriptions-item>
          <el-descriptions-item label="限额类型">{{ getLimitTypeText(currentLimit.limitType) }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ currentLimit.currencyCode }}</el-descriptions-item>
          <el-descriptions-item label="限额金额">{{ formatCurrency(currentLimit.limitAmount, currentLimit.currencyCode) }}</el-descriptions-item>
          <el-descriptions-item label="已使用金额">{{ formatCurrency(currentLimit.usedAmount, currentLimit.currencyCode) }}</el-descriptions-item>
          <el-descriptions-item label="剩余金额">{{ formatCurrency(currentLimit.limitAmount - currentLimit.usedAmount, currentLimit.currencyCode) }}</el-descriptions-item>
          <el-descriptions-item label="使用率">
            <el-progress 
              :percentage="getUsagePercentage(currentLimit)" 
              :status="getUsageStatus(currentLimit)"
              :stroke-width="8"
            />
          </el-descriptions-item>
          <el-descriptions-item label="预警阈值">{{ currentLimit.warningThreshold }}%</el-descriptions-item>
          <el-descriptions-item label="生效日期">{{ currentLimit.effectiveDate }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ currentLimit.expireDate || '永久有效' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentLimit.createTime }}</el-descriptions-item>
        </el-descriptions>
        <div v-if="currentLimit.limitDescription" style="margin-top: 20px;">
          <h4>限额描述</h4>
          <p>{{ currentLimit.limitDescription }}</p>
        </div>
        <div v-if="currentLimit.remark" style="margin-top: 20px;">
          <h4>备注信息</h4>
          <p>{{ currentLimit.remark }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentLimit && currentLimit.limitStatus === 'ACTIVE'" type="warning" @click="handleSuspend(currentLimit)">
          暂停限额
        </el-button>
        <el-button v-if="currentLimit && currentLimit.limitStatus === 'SUSPENDED'" type="success" @click="handleActivate(currentLimit)">
          激活限额
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAccountLimitPage, createAccountLimit, updateAccountLimit, deleteAccountLimit } from '@/api/globalTreasurer/zhgl'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'AccountLimitManage',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        limitId: undefined,
        accountNumber: undefined,
        limitType: undefined,
        limitStatus: undefined,
        createDateRange: undefined
      },
      totalLimits: 0,
      activeLimits: 0,
      nearLimitAccounts: 0,
      exceededAccounts: 0,
      activeRate: 0,
      multipleSelection: [],
      availableAccounts: [],
      temp: {
        limitId: undefined,
        accountId: undefined,
        accountNumber: '',
        accountName: '',
        limitType: '',
        limitStatus: 'ACTIVE',
        limitAmount: 0,
        currencyCode: 'CNY',
        effectiveDate: null,
        expireDate: null,
        warningThreshold: 80,
        limitDescription: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑限额配置',
        create: '新增限额配置'
      },
      currentLimit: null,
      rules: {
        accountId: [{ required: true, message: '请选择账户', trigger: 'change' }],
        limitType: [{ required: true, message: '请选择限额类型', trigger: 'change' }],
        limitStatus: [{ required: true, message: '请选择限额状态', trigger: 'change' }],
        limitAmount: [{ required: true, message: '请输入限额金额', trigger: 'blur' }],
        currencyCode: [{ required: true, message: '请选择币种', trigger: 'change' }],
        effectiveDate: [{ required: true, message: '请选择生效日期', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.updateStatistics()
    this.loadAvailableAccounts()
  },
  methods: {
    getList() {
      this.listLoading = true
      // 使用模拟数据
      setTimeout(() => {
        this.list = [
          {
            limitId: 1,
            accountId: 1,
            accountNumber: '1234567890123456789',
            accountName: '示例云科技有限公司',
            limitType: 'DAILY_LIMIT',
            limitStatus: 'ACTIVE',
            limitAmount: 1000000.00,
            usedAmount: 750000.00,
            currencyCode: 'CNY',
            effectiveDate: '2024-01-01',
            expireDate: null,
            warningThreshold: 80,
            limitDescription: '日交易限额控制',
            remark: '高风险账户限额',
            createTime: '2024-01-01 10:00:00'
          },
          {
            limitId: 2,
            accountId: 2,
            accountNumber: '9876543210987654321',
            accountName: '示例云投资管理有限公司',
            limitType: 'SINGLE_TRANSACTION',
            limitStatus: 'ACTIVE',
            limitAmount: 500000.00,
            usedAmount: 450000.00,
            currencyCode: 'USD',
            effectiveDate: '2024-06-01',
            expireDate: '2024-12-31',
            warningThreshold: 85,
            limitDescription: '单笔交易限额控制',
            remark: '投资业务限额',
            createTime: '2024-06-01 14:30:00'
          },
          {
            limitId: 3,
            accountId: 3,
            accountNumber: '5555666677778888999',
            accountName: '示例云贸易有限公司',
            limitType: 'MONTHLY_LIMIT',
            limitStatus: 'SUSPENDED',
            limitAmount: 2000000.00,
            usedAmount: 1200000.00,
            currencyCode: 'CNY',
            effectiveDate: '2024-03-01',
            expireDate: '2024-11-30',
            warningThreshold: 75,
            limitDescription: '月交易限额控制',
            remark: '贸易业务限额，因风险暂停',
            createTime: '2024-03-01 09:15:00'
          }
        ]
        this.total = this.list.length
        this.listLoading = false
      }, 1000)
    },
    updateStatistics() {
      this.totalLimits = 456
      this.activeLimits = 389
      this.nearLimitAccounts = 23
      this.exceededAccounts = 5
      this.activeRate = Math.round((this.activeLimits / this.totalLimits) * 100)
    },
    loadAvailableAccounts() {
      // 模拟可用账户数据
      this.availableAccounts = [
        { accountId: 1, accountNumber: '1234567890123456789', accountName: '示例云科技有限公司' },
        { accountId: 2, accountNumber: '9876543210987654321', accountName: '示例云投资管理有限公司' },
        { accountId: 3, accountNumber: '5555666677778888999', accountName: '示例云贸易有限公司' }
      ]
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        limitId: undefined,
        accountNumber: undefined,
        limitType: undefined,
        limitStatus: undefined,
        createDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.temp.effectiveDate = new Date(row.effectiveDate)
      if (row.expireDate) {
        this.temp.expireDate = new Date(row.expireDate)
      }
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentLimit = row
      this.dialogDetailVisible = true
    },
    handleViewAccount(row) {
      // 跳转到账户详情页面
      this.$router.push({ path: '/globalTreasurerAccount/zhxx', query: { accountId: row.accountId } })
    },
    handleAccountChange(accountId) {
      const account = this.availableAccounts.find(acc => acc.accountId === accountId)
      if (account) {
        this.temp.accountNumber = account.accountNumber
        this.temp.accountName = account.accountName
      }
    },
    handleSuspend(row) {
      this.$confirm('确认暂停该限额配置?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.limitStatus = 'SUSPENDED'
        this.$message({
          type: 'success',
          message: '限额已暂停!'
        })
      })
    },
    handleActivate(row) {
      this.$confirm('确认激活该限额配置?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.limitStatus = 'ACTIVE'
        this.$message({
          type: 'success',
          message: '限额已激活!'
        })
      })
    },
    handleDelete(row, index) {
      this.$confirm('确认删除该限额配置?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.list.splice(index, 1)
        this.total = this.list.length
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      })
    },
    handleBatchUpdate() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要更新的限额'
        })
        return
      }
      this.$confirm(`确认批量更新选中的${this.multipleSelection.length}个限额配置?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '批量更新成功!'
        })
        this.getList()
      })
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '数据导出成功'
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.limitId = parseInt(Math.random() * 100) + 1024
          this.temp.usedAmount = 0
          this.temp.effectiveDate = this.temp.effectiveDate.toISOString().slice(0, 10)
          if (this.temp.expireDate) {
            this.temp.expireDate = this.temp.expireDate.toISOString().slice(0, 10)
          }
          this.temp.createTime = new Date().toLocaleString()
          this.list.unshift(this.temp)
          this.total = this.list.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '限额配置创建成功'
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.effectiveDate = tempData.effectiveDate.toISOString().slice(0, 10)
          if (tempData.expireDate) {
            tempData.expireDate = tempData.expireDate.toISOString().slice(0, 10)
          }
          const index = this.list.findIndex(v => v.limitId === this.temp.limitId)
          this.list.splice(index, 1, tempData)
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '限额配置更新成功'
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        limitId: undefined,
        accountId: undefined,
        accountNumber: '',
        accountName: '',
        limitType: '',
        limitStatus: 'ACTIVE',
        limitAmount: 0,
        currencyCode: 'CNY',
        effectiveDate: null,
        expireDate: null,
        warningThreshold: 80,
        limitDescription: '',
        remark: ''
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'limitId') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.list.sort((a, b) => a.limitId - b.limitId)
      } else {
        this.list.sort((a, b) => b.limitId - a.limitId)
      }
    },
    getUsagePercentage(row) {
      if (row.limitAmount === 0) return 0
      return Math.round((row.usedAmount / row.limitAmount) * 100)
    },
    getUsageStatus(row) {
      const percentage = this.getUsagePercentage(row)
      if (percentage >= 100) return 'exception'
      if (percentage >= row.warningThreshold) return 'warning'
      return 'success'
    },
    getUsageClass(row) {
      const percentage = this.getUsagePercentage(row)
      if (percentage >= 100) return 'usage-exceeded'
      if (percentage >= row.warningThreshold) return 'usage-warning'
      return 'usage-normal'
    },
    getLimitTypeTagType(limitType) {
      const typeMap = {
        'SINGLE_TRANSACTION': 'primary',
        'DAILY_LIMIT': 'success',
        'MONTHLY_LIMIT': 'warning',
        'YEARLY_LIMIT': 'info',
        'BALANCE_LIMIT': 'danger'
      }
      return typeMap[limitType] || 'info'
    },
    getLimitTypeText(limitType) {
      const textMap = {
        'SINGLE_TRANSACTION': '单笔限额',
        'DAILY_LIMIT': '日限额',
        'MONTHLY_LIMIT': '月限额',
        'YEARLY_LIMIT': '年限额',
        'BALANCE_LIMIT': '余额限额'
      }
      return textMap[limitType] || limitType
    },
    getLimitStatusTagType(status) {
      const statusMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'SUSPENDED': 'warning',
        'EXPIRED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getLimitStatusText(status) {
      const textMap = {
        'ACTIVE': '有效',
        'INACTIVE': '无效',
        'SUSPENDED': '暂停',
        'EXPIRED': '过期'
      }
      return textMap[status] || status
    },
    formatCurrency(amount, currency) {
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: currency || 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.account-limit-manage {
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
            color: #E6A23C;
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

  .limit-overview {
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
          &.warning-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.exceeded-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
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
            &.negative {
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

  .limit-amount {
    font-weight: 600;
    color: #E6A23C;
  }

  .used-amount {
    font-weight: 600;
    color: #909399;
  }

  .usage-text {
    font-size: 12px;
    margin-top: 4px;
    font-weight: 600;
    
    &.usage-normal {
      color: #67C23A;
    }
    &.usage-warning {
      color: #E6A23C;
    }
    &.usage-exceeded {
      color: #F56C6C;
    }
  }

  .limit-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }
    h4 {
      margin: 16px 0 8px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }
    p {
      margin: 0;
      color: #606266;
      line-height: 1.5;
    }
  }
}
</style>
