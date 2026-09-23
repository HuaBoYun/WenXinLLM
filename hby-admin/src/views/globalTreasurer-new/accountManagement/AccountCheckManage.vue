<template>
  <div class="account-check-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-search"></i>
            账户检查管理
          </h2>
          <p class="page-description">管理银行账户的定期检查和异常监控，包括余额核对、交易验证、状态检查等功能</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增检查
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleBatchCheck">
            批量检查
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出报告
          </el-button>
        </div>
      </div>
    </div>

    <!-- 检查统计卡片 -->
    <div class="check-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总检查数</div>
                <div class="card-value">{{ totalChecks }}</div>
                <div class="card-change">次检查</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon success-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">检查通过</div>
                <div class="card-value">{{ passedChecks }}</div>
                <div class="card-change positive">通过率{{ passRate }}%</div>
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
                <div class="card-title">异常检查</div>
                <div class="card-value">{{ abnormalChecks }}</div>
                <div class="card-change warning">需处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon processing-icon">
                <i class="el-icon-loading"></i>
              </div>
              <div class="card-info">
                <div class="card-title">检查中</div>
                <div class="card-value">{{ processingChecks }}</div>
                <div class="card-change">进行中</div>
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
          <el-form-item label="检查ID">
            <el-input
              v-model="listQuery.checkId"
              placeholder="请输入检查ID"
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
          <el-form-item label="检查类型">
            <el-select
              v-model="listQuery.checkType"
              placeholder="请选择检查类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="余额核对" value="BALANCE" />
              <el-option label="交易验证" value="TRANSACTION" />
              <el-option label="状态检查" value="STATUS" />
              <el-option label="合规检查" value="COMPLIANCE" />
              <el-option label="风险评估" value="RISK" />
            </el-select>
          </el-form-item>
          <el-form-item label="检查状态">
            <el-select
              v-model="listQuery.checkStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待检查" value="PENDING" />
              <el-option label="已完成" value="COMPLETED" />
            </el-select>
          </el-form-item>
          <el-form-item label="检查日期">
            <el-date-picker
              v-model="listQuery.checkDateRange"
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

    <!-- 检查记录表格 -->
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
        <el-table-column label="检查ID" prop="checkId" sortable="custom" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.checkId }}</span>
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
        <el-table-column label="检查类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getCheckTypeTagType(row.checkType)" size="mini">
              {{ getCheckTypeText(row.checkType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="检查状态" class-name="status-col" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getCheckStatusTagType(row.checkStatus)">
              {{ getCheckStatusText(row.checkStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="检查日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.checkDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="完成时间" width="180px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.checkStatus === 'COMPLETED' ? formatDate(row.updateTime) : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="检查结果" width="100px" align="center">
          <template slot-scope="{row}">
            <span v-if="row.checkResult" class="result-text" :class="getResultClass(row.checkResult)">{{ getCheckResultText(row.checkResult) }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="检查人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.checkerName || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="260" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">查看</el-button>
            <el-button v-if="row.checkStatus === 'PENDING'" size="mini" type="warning" @click="handleStop(row)">停止</el-button>
            <el-button v-else size="mini" type="success" @click="handleFillResult(row)">填写结果</el-button>
            <el-button v-if="row.checkResult === 'FAIL' || row.checkResult === 'WARNING'" size="mini" type="info" @click="handleRecheck(row)">重检</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(row,$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增检查对话框 -->
    <el-dialog title="新增账户检查" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-form-item label="选择账户" prop="accountId">
          <el-select v-model="temp.accountId" placeholder="请选择要检查的账户" style="width: 100%;" @change="handleAccountChange" :loading="accountsLoading" filterable no-data-text="暂无可用账户,请先创建账户">
            <el-option
              v-for="account in availableAccounts"
              :key="account.accountId"
              :label="`${account.accountNumber}${account.accountName ? ' - ' + account.accountName : ''}`"
              :value="account.accountId"
            />
          </el-select>
          <div v-if="availableAccounts.length === 0 && !accountsLoading" class="account-empty-tip">
            <i class="el-icon-warning"></i>
            <span>当前无可用的账户,请先在账户管理中创建账户</span>
          </div>
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
            <el-form-item label="检查类型" prop="checkType">
              <el-select v-model="temp.checkType" placeholder="请选择检查类型" style="width: 100%;">
                <el-option label="余额核对" value="BALANCE" />
                <el-option label="交易验证" value="TRANSACTION" />
                <el-option label="状态检查" value="STATUS" />
                <el-option label="合规检查" value="COMPLIANCE" />
                <el-option label="风险评估" value="RISK" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查日期" prop="checkDate">
              <el-date-picker
                v-model="temp.checkDate"
                type="date"
                placeholder="选择检查日期"
                value-format="yyyy-MM-dd"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="检查内容" prop="checkContent">
          <el-input v-model="temp.checkContent" type="textarea" :rows="3" placeholder="请详细描述检查内容和目的" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="createData">确认</el-button>
      </div>
    </el-dialog>

    <!-- 检查详情对话框 -->
    <el-dialog title="检查详情" :visible.sync="dialogDetailVisible" width="900px">
      <div v-if="currentCheck" class="check-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="检查ID">{{ currentCheck.checkId }}</el-descriptions-item>
          <el-descriptions-item label="检查状态">
            <el-tag :type="getCheckStatusTagType(currentCheck.checkStatus)">
              {{ getCheckStatusText(currentCheck.checkStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="账户号码">{{ currentCheck.accountNumber }}</el-descriptions-item>
          <el-descriptions-item label="账户名称">{{ currentCheck.accountName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="检查类型">{{ getCheckTypeText(currentCheck.checkType) }}</el-descriptions-item>
          <el-descriptions-item label="检查结果">
            <span :class="getResultClass(currentCheck.checkResult)">{{ getCheckResultText(currentCheck.checkResult) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="检查日期">{{ currentCheck.checkDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="完成时间">{{ currentCheck.checkStatus === 'COMPLETED' ? formatDate(currentCheck.updateTime) : '-' }}</el-descriptions-item>
          <el-descriptions-item label="检查人">{{ currentCheck.checkerName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="机构ID">{{ currentCheck.orgId || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(currentCheck.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ formatDate(currentCheck.updateTime) }}</el-descriptions-item>
        </el-descriptions>
        <div v-if="currentCheck.checkContent" style="margin-top: 20px;">
          <h4>检查内容</h4>
          <p>{{ currentCheck.checkContent }}</p>
        </div>
        <div v-if="currentCheck.problemFound" style="margin-top: 20px;">
          <h4>发现问题</h4>
          <p>{{ currentCheck.problemFound }}</p>
        </div>
        <div v-if="currentCheck.suggestion" style="margin-top: 20px;">
          <h4>处理建议</h4>
          <p>{{ currentCheck.suggestion }}</p>
        </div>
        <div v-if="currentCheck.remark" style="margin-top: 20px;">
          <h4>备注信息</h4>
          <p>{{ currentCheck.remark }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentCheck && currentCheck.checkStatus === 'PENDING'" type="warning" @click="handleStop(currentCheck)">
          停止检查
        </el-button>
        <el-button v-if="currentCheck && (currentCheck.checkResult === 'FAIL' || currentCheck.checkResult === 'WARNING')" type="success" @click="handleRecheck(currentCheck)">
          重新检查
        </el-button>
      </div>
    </el-dialog>

    <!-- 填写检查结果对话框 -->
    <el-dialog title="填写检查结果" :visible.sync="dialogResultVisible" width="600px">
      <el-form ref="resultForm" :rules="resultRules" :model="resultTemp" label-position="left" label-width="100px" style="padding: 0 20px;">
        <el-form-item label="账户号码">
          <el-input :value="resultTemp.accountNumber" disabled />
        </el-form-item>
        <el-form-item label="检查结果" prop="checkResult">
          <el-select v-model="resultTemp.checkResult" placeholder="请选择检查结果" style="width: 100%;">
            <el-option label="通过" value="PASS" />
            <el-option label="警告" value="WARNING" />
            <el-option label="不通过" value="FAIL" />
          </el-select>
        </el-form-item>
        <el-form-item label="检查状态" prop="checkStatus">
          <el-select v-model="resultTemp.checkStatus" placeholder="请选择检查状态" style="width: 100%;">
            <el-option label="待检查" value="PENDING" />
            <el-option label="已完成" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item label="发现问题">
          <el-input v-model="resultTemp.problemFound" type="textarea" :rows="3" placeholder="请描述发现的问题（无问题可不填）" />
        </el-form-item>
        <el-form-item label="处理建议">
          <el-input v-model="resultTemp.suggestion" type="textarea" :rows="3" placeholder="请填写处理建议（无建议可不填）" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogResultVisible = false">取消</el-button>
        <el-button type="primary" @click="submitResult">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAccountCheckPage, createAccountCheck, stopAccountCheck, recheckAccount, deleteAccountCheck, updateAccountCheck, getAccountList } from '@/api/globalTreasurer/zhgl'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'AccountCheckManage',
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
        checkId: undefined,
        accountNumber: undefined,
        checkType: undefined,
        checkStatus: undefined,
        checkDateRange: undefined
      },
      totalChecks: 0,
      passedChecks: 0,
      abnormalChecks: 0,
      processingChecks: 0,
      passRate: 0,
      multipleSelection: [],
      availableAccounts: [],
      accountsLoading: false,
      temp: {
        checkId: undefined,
        accountId: undefined,
        accountNumber: '',
        accountName: '',
        checkType: '',
        checkDate: null,
        checkContent: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogResultVisible: false,
      resultTemp: {
        checkId: undefined,
        accountNumber: '',
        checkResult: '',
        checkStatus: 'COMPLETED',
        problemFound: '',
        suggestion: ''
      },
      resultRules: {
        checkResult: [{ required: true, message: '请选择检查结果', trigger: 'change' }],
        checkStatus: [{ required: true, message: '请选择检查状态', trigger: 'change' }]
      },
      currentCheck: null,
      rules: {
        accountId: [{ required: true, message: '请选择账户', trigger: 'change' }],
        checkType: [{ required: true, message: '请选择检查类型', trigger: 'change' }],
        checkDate: [{ required: true, message: '请选择检查日期', trigger: 'change' }],
        checkContent: [{ required: true, message: '请输入检查内容', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.updateStatistics()
    this.loadAvailableAccounts()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        // 构造请求参数
        const params = {
          pageNo: this.listQuery.page,
          pageSize: this.listQuery.limit
        }

        // 只添加非空的查询条件
        if (this.listQuery.checkId) {
          params.checkId = this.listQuery.checkId
        }
        if (this.listQuery.accountNumber) {
          params.accountNumber = this.listQuery.accountNumber
        }
        if (this.listQuery.checkType) {
          params.checkType = this.listQuery.checkType
        }
        if (this.listQuery.checkStatus) {
          params.checkStatus = this.listQuery.checkStatus
        }
        // 处理日期范围
        if (this.listQuery.checkDateRange && this.listQuery.checkDateRange.length === 2) {
          const formatDateStr = (d) => {
            if (!d) return null
            if (typeof d === 'string') return d.substring(0, 10)
            const date = new Date(d)
            const y = date.getFullYear()
            const m = String(date.getMonth() + 1).padStart(2, '0')
            const day = String(date.getDate()).padStart(2, '0')
            return `${y}-${m}-${day}`
          }
          params.startDate = formatDateStr(this.listQuery.checkDateRange[0])
          params.endDate = formatDateStr(this.listQuery.checkDateRange[1])
        }

        const response = await getAccountCheckPage(params)
        if (response && response.code === 1) {
          const data = response.data
          this.list = data.tlist || data.records || data.list || []
          this.total = parseInt(data.totalRecord || data.total || 0) || this.list.length
          // 用已加载的账户列表补填 accountName
          this.fillAccountNames(this.list)
          this.updateStatisticsFromList(this.list)
        } else {
          this.list = []
          this.total = 0
          this.$message.error(response.message || response.msg || '获取数据失败')
        }
      } catch (error) {
        console.error('获取账户检查列表失败:', error)
        this.list = []
        this.total = 0
        this.$message.error('获取数据失败，请稍后重试')
      } finally {
        this.listLoading = false
      }
    },
    async updateStatistics() {
      // 统计数据由 updateStatisticsFromList 在列表加载后计算
    },
    updateStatisticsFromList(list) {
      this.totalChecks = list.length
      const passStatuses = ['PASSED', 'COMPLETED']
      const passResults = ['PASS']
      const abnormalResults = ['FAIL', 'WARNING']
      const pendingStatuses = ['PROCESSING', 'PENDING']
      this.passedChecks = list.filter(item =>
        passStatuses.includes(item.checkStatus) && passResults.includes(item.checkResult)
      ).length
      this.abnormalChecks = list.filter(item =>
        abnormalResults.includes(item.checkResult)
      ).length
      this.processingChecks = list.filter(item =>
        pendingStatuses.includes(item.checkStatus)
      ).length
      this.passRate = this.totalChecks > 0
        ? Math.round((this.passedChecks / this.totalChecks) * 100)
        : 0
    },
    async loadAvailableAccounts() {
      this.accountsLoading = true
      try {
        // 调用账户列表接口
        const response = await getAccountList()

        if (response && response.code === 1) {
          // 处理返回的数据,确保有 accountId, accountNumber, accountName 字段
          const accounts = response.data || []
          this.availableAccounts = accounts.map(account => ({
            accountId: account.accountId || account.id,
            accountNumber: account.accountNumber,
            accountName: account.accountName
          })).filter(account => account.accountId && account.accountNumber) // 过滤无效数据

          // 如果没有可用账户,给出提示
          if (this.availableAccounts.length === 0) {
            console.warn('暂无可用账户,请先创建账户')
          }
        } else {
          this.availableAccounts = []
          this.$message.error(response.message || response.msg || '获取账户列表失败')
        }
      } catch (error) {
        console.error('获取可用账户失败:', error)
        this.availableAccounts = []
        this.$message.error('获取可用账户失败,请稍后重试')
      } finally {
        this.accountsLoading = false
      }
    },
    // 根据 accountNumber 从账户列表补填 accountName
    fillAccountNames(list) {
      if (!this.availableAccounts.length) return
      list.forEach(item => {
        if (!item.accountName && item.accountNumber) {
          const matched = this.availableAccounts.find(a => a.accountNumber === item.accountNumber)
          if (matched) {
            this.$set(item, 'accountName', matched.accountName)
          }
        }
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        checkId: undefined,
        accountNumber: undefined,
        checkType: undefined,
        checkStatus: undefined,
        checkDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      // 打开对话框前重新加载账户列表,确保数据最新
      this.loadAvailableAccounts()
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentCheck = row
      this.dialogDetailVisible = true
    },
    handleFillResult(row) {
      this.resultTemp = {
        checkId: row.checkId,
        accountNumber: row.accountNumber,
        checkResult: row.checkResult || '',
        checkStatus: row.checkStatus === 'COMPLETED' ? 'COMPLETED' : 'COMPLETED',
        problemFound: row.problemFound || '',
        suggestion: row.suggestion || ''
      }
      this.dialogResultVisible = true
      this.$nextTick(() => {
        this.$refs['resultForm'] && this.$refs['resultForm'].clearValidate()
      })
    },
    async submitResult() {
      try {
        await this.$refs['resultForm'].validate()

        // 验证是否有选中检查
        if (!this.resultTemp.checkId) {
          this.$message.error('检查ID不能为空')
          return
        }

        const payload = {
          checkId: this.resultTemp.checkId,
          checkResult: this.resultTemp.checkResult,
          checkStatus: this.resultTemp.checkStatus,
          problemFound: this.resultTemp.problemFound || null,
          suggestion: this.resultTemp.suggestion || null,
          updateTime: new Date().toISOString()
        }

        const response = await updateAccountCheck(payload)
        if (response && response.code === 1) {
          this.$message.success('检查结果已保存')
          this.dialogResultVisible = false
          this.getList()
        } else {
          this.$message.error(response.msg || response.message || '保存失败')
        }
      } catch (error) {
        if (error !== false) {
          console.error('提交检查结果失败:', error)
          this.$message.error('保存失败，请稍后重试')
        }
      }
    },
    handleViewAccount(row) {
      this.$message.info(`账户：${row.accountName || row.accountNumber || ''}`)
    },
    handleAccountChange(accountId) {
      if (!accountId) {
        this.temp.accountNumber = ''
        this.temp.accountName = ''
        return
      }
      const account = this.availableAccounts.find(acc => acc.accountId === accountId)
      if (account) {
        this.temp.accountNumber = account.accountNumber
        this.temp.accountName = account.accountName
      }
    },
    async handleStop(row) {
      try {
        await this.$confirm('确认停止该检查任务?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await stopAccountCheck(row.checkId)
        if (response && response.code === 1) {
          this.$message.success('检查已停止!')
          this.getList()
        } else {
          this.$message.error(response.message || '停止失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('停止检查失败:', error)
          this.$message.error('停止失败，请稍后重试')
        }
      }
    },
    async handleRecheck(row) {
      try {
        await this.$confirm('确认重新检查该账户?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await recheckAccount(row.checkId)
        if (response && response.code === 1) {
          this.$message.success('重新检查已启动!')
          this.getList()
        } else {
          this.$message.error(response.message || '重新检查失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('重新检查失败:', error)
          this.$message.error('重新检查失败，请稍后重试')
        }
      }
    },
    async handleDelete(row, index) {
      try {
        await this.$confirm('确认删除该检查记录?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await deleteAccountCheck(row.checkId)
        if (response && response.code === 1) {
          this.$message.success('删除成功!')
          this.getList()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除检查失败:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      }
    },
    handleBatchCheck() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要检查的账户'
        })
        return
      }
      this.$confirm(`确认对选中的${this.multipleSelection.length}个账户执行批量检查?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '批量检查已启动!'
        })
        this.getList()
      })
    },
    handleExport() {
      if (!this.list || this.list.length === 0) {
        this.$message.warning('暂无数据可导出')
        return
      }
      const headers = ['检查ID', '账户号码', '账户名称', '检查类型', '检查状态', '检查日期', '完成时间', '检查结果', '检查人']
      const rows = this.list.map(row => [
        row.checkId || '',
        row.accountNumber || '',
        row.accountName || '',
        row.checkType || '',
        row.checkStatus || '',
        row.checkDate || '',
        row.completionTime || '',
        row.checkResult || '',
        row.checker || ''
      ])
      const csvContent = [headers, ...rows].map(r => r.map(v => `"${String(v).replace(/"/g, '""')}"`).join(',')).join('\n')
      const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = '账户检查列表.csv'
      a.click()
      URL.revokeObjectURL(url)
      this.$message.success('导出成功')
    },
    async createData() {
      try {
        await this.$refs['dataForm'].validate()

        // 检查是否选择了账户
        if (!this.temp.accountId) {
          this.$message.warning('请先选择要检查的账户')
          return
        }

        // 检查账户号码是否完整
        if (!this.temp.accountNumber) {
          this.$message.warning('账户信息不完整,请重新选择账户')
          return
        }

        const response = await createAccountCheck(this.temp)
        if (response && response.code === 1) {
          this.dialogFormVisible = false
          this.$message.success('检查任务创建成功')
          this.getList()
          this.updateStatistics()
        } else {
          this.$message.error(response.message || '创建失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('创建检查失败:', error)
          this.$message.error('创建失败，请稍后重试')
        }
      }
    },
    resetTemp() {
      this.temp = {
        checkId: undefined,
        accountId: undefined,
        accountNumber: '',
        accountName: '',
        checkType: '',
        checkDate: null,
        checkContent: '',
        remark: ''
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'checkId') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.list.sort((a, b) => a.checkId - b.checkId)
      } else {
        this.list.sort((a, b) => b.checkId - a.checkId)
      }
    },
    getCheckTypeTagType(checkType) {
      const typeMap = {
        'BALANCE': 'primary',
        'BALANCE_CHECK': 'primary',
        'TRANSACTION': 'danger',
        'TRANSACTION_CHECK': 'danger',
        'STATUS': 'success',
        'STATUS_CHECK': 'success',
        'PERMISSION': 'warning',
        'PERMISSION_CHECK': 'warning',
        'COMPLIANCE': 'info',
        'COMPLIANCE_CHECK': 'info',
        'RISK': 'warning'
      }
      return typeMap[checkType] || 'info'
    },
    getCheckTypeText(checkType) {
      const textMap = {
        'BALANCE': '余额核对',
        'BALANCE_CHECK': '余额核对',
        'TRANSACTION': '交易验证',
        'TRANSACTION_CHECK': '交易验证',
        'STATUS': '状态检查',
        'STATUS_CHECK': '状态检查',
        'PERMISSION': '权限检查',
        'PERMISSION_CHECK': '权限检查',
        'COMPLIANCE': '合规检查',
        'COMPLIANCE_CHECK': '合规检查',
        'RISK': '风险评估'
      }
      return textMap[checkType] || checkType
    },
    getCheckStatusTagType(status) {
      const statusMap = {
        'PROCESSING': 'warning',
        'PENDING': 'warning',
        'PASSED': 'success',
        'COMPLETED': 'success',
        'ABNORMAL': 'danger',
        'FAILED': 'danger',
        'FAIL': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getCheckStatusText(status) {
      const textMap = {
        'PROCESSING': '检查中',
        'PENDING': '待检查',
        'PASSED': '通过',
        'COMPLETED': '已完成',
        'ABNORMAL': '异常',
        'FAILED': '失败',
        'FAIL': '失败'
      }
      return textMap[status] || status
    },
    getPriorityText(priority) {
      const textMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return textMap[priority] || priority
    },
    getResultClass(checkResult) {
      const classMap = {
        'PASS': 'result-success',
        'WARNING': 'result-warning',
        'FAIL': 'result-error'
      }
      return classMap[checkResult] || ''
    },
    getCheckResultText(checkResult) {
      const textMap = {
        'PASS': '通过',
        'WARNING': '警告',
        'FAIL': '失败'
      }
      return textMap[checkResult] || checkResult || '-'
    },
    formatDate(date) {
      if (!date) return '-'
      try {
        // 如果是字符串，检查格式
        if (typeof date === 'string') {
          // 如果已经格式化为 yyyy-MM-dd HH:mm:ss，直接返回
          if (date.match(/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/)) {
            return date
          }
          // 如果是 ISO 格式或其他格式，转换为 Date 对象再格式化
          const d = new Date(date)
          if (!isNaN(d.getTime())) {
            const year = d.getFullYear()
            const month = String(d.getMonth() + 1).padStart(2, '0')
            const day = String(d.getDate()).padStart(2, '0')
            const hours = String(d.getHours()).padStart(2, '0')
            const minutes = String(d.getMinutes()).padStart(2, '0')
            const seconds = String(d.getSeconds()).padStart(2, '0')
            return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
          }
          // 无法转换，直接返回原字符串
          return date
        }
        // 如果是 Date 对象，格式化为 yyyy-MM-dd HH:mm:ss
        if (date instanceof Date) {
          if (isNaN(date.getTime())) return '-'
          const year = date.getFullYear()
          const month = String(date.getMonth() + 1).padStart(2, '0')
          const day = String(date.getDate()).padStart(2, '0')
          const hours = String(date.getHours()).padStart(2, '0')
          const minutes = String(date.getMinutes()).padStart(2, '0')
          const seconds = String(date.getSeconds()).padStart(2, '0')
          return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
        }
        // 其他情况，返回原始值
        return date
      } catch (e) {
        console.error('日期格式化失败:', e, date)
        return '-'
      }
    },
    gotoAccountManage() {
      this.dialogFormVisible = false
      this.$message.info('请前往账户管理页面查看')
    }
  }
}
</script>

<style lang="scss" scoped>
.account-check-manage {
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
            color: #909399;
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

  .check-overview {
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
          &.success-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.warning-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.processing-icon {
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

  .account-empty-tip {
    margin-top: 8px;
    padding: 12px;
    background-color: #fdf6ec;
    border: 1px solid #faecd8;
    border-radius: 4px;
    color: #e6a23c;
    font-size: 13px;
    line-height: 1.6;
    display: flex;
    align-items: center;
    gap: 8px;
    i {
      font-size: 16px;
    }
    span {
      flex: 1;
    }
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .result-text {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.4;
    
    &.result-success {
      color: #67C23A;
    }
    &.result-error {
      color: #F56C6C;
    }
    &.result-warning {
      color: #E6A23C;
    }
    &.result-info {
      color: #909399;
    }
  }

  .check-detail {
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
      
      &.result-success {
        color: #67C23A;
      }
      &.result-error {
        color: #F56C6C;
      }
      &.result-warning {
        color: #E6A23C;
      }
      &.result-info {
        color: #909399;
      }
    }
    .check-parameters, .check-details {
      background: #f5f7fa;
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      padding: 12px;
      font-size: 12px;
      color: #606266;
      white-space: pre-wrap;
      word-break: break-all;
      margin: 0;
    }
  }
}
</style>
