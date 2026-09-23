<template>
  <div class="fund-concentration-strategy">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-setting"></i>
            归集策略配置
          </h2>
          <p class="page-description">配置企业资金归集策略，包括归集规则、触发条件和执行参数</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增策略
          </el-button>
          <el-button type="success" icon="el-icon-check" @click="handleBatchEnable">
            批量启用
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 策略概览卡片 -->
    <div class="strategy-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总策略数</div>
                <div class="card-value">{{ totalStrategies }}</div>
                <div class="card-change">个策略</div>
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
                <div class="card-title">启用策略</div>
                <div class="card-value">{{ activeStrategies }}</div>
                <div class="card-change positive">运行中</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon inactive-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">停用策略</div>
                <div class="card-value">{{ inactiveStrategies }}</div>
                <div class="card-change warning">已停用</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon coverage-icon">
                <i class="el-icon-pie-chart"></i>
              </div>
              <div class="card-info">
                <div class="card-title">覆盖账户</div>
                <div class="card-value">{{ coveredAccounts }}</div>
                <div class="card-change">个账户</div>
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
          <el-form-item label="策略名称">
            <el-input
              v-model="listQuery.strategyName"
              placeholder="请输入策略名称"
              style="width: 150px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="策略类型">
            <el-select
              v-model="listQuery.strategyType"
              placeholder="请选择策略类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="定时归集" value="SCHEDULED" />
              <el-option label="余额触发" value="BALANCE_TRIGGER" />
              <el-option label="比例归集" value="RATIO_BASED" />
              <el-option label="智能归集" value="INTELLIGENT" />
            </el-select>
          </el-form-item>
          <el-form-item label="策略状态">
            <el-select
              v-model="listQuery.strategyStatus"
              placeholder="请选择状态"
              clearable
              style="width: 100px;"
            >
              <el-option label="启用" value="ACTIVE" />
              <el-option label="停用" value="INACTIVE" />
              <el-option label="测试" value="TESTING" />
            </el-select>
          </el-form-item>
          <el-form-item label="归集频率">
            <el-select
              v-model="listQuery.frequency"
              placeholder="请选择频率"
              clearable
              style="width: 120px;"
            >
              <el-option label="每日" value="DAILY" />
              <el-option label="每周" value="WEEKLY" />
              <el-option label="每月" value="MONTHLY" />
              <el-option label="实时" value="REALTIME" />
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

    <!-- 策略表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="strategyList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="策略ID" prop="strategyId" width="80" align="center" />
        <el-table-column label="策略名称" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.strategyName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="策略类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getStrategyTypeTagType(row.strategyType)" size="mini">
              {{ getStrategyTypeText(row.strategyType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="归集频率" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ getFrequencyText(row.frequency) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="源账户数" width="100px" align="center">
          <template slot-scope="{row}">
            <span class="account-count">{{ row.sourceAccountCount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="目标账户" width="180px" align="center">
          <template slot-scope="{row}">
            <div>
              <div class="account-name">{{ row.targetAccountName }}</div>
              <div class="account-number">{{ row.targetAccountNumber }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="归集条件" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="condition-text">{{ row.concentrationCondition }}</span>
          </template>
        </el-table-column>
        <el-table-column label="策略状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getStatusTagType(row.strategyStatus)" size="mini">
              {{ getStatusText(row.strategyStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最后执行" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.lastExecutionTime || '未执行' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.creatorName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button v-if="row.strategyStatus === 'INACTIVE'" size="mini" type="success" @click="handleEnable(row)">
              启用
            </el-button>
            <el-button v-if="row.strategyStatus === 'ACTIVE'" size="mini" type="warning" @click="handleDisable(row)">
              停用
            </el-button>
            <el-button size="mini" type="info" @click="handleTest(row)">
              测试
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑策略对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-tabs v-model="activeFormTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="策略名称" prop="strategyName">
                  <el-input v-model="temp.strategyName" placeholder="请输入策略名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="策略类型" prop="strategyType">
                  <el-select v-model="temp.strategyType" placeholder="请选择策略类型" style="width: 100%;">
                    <el-option label="定时归集" value="SCHEDULED" />
                    <el-option label="余额触发" value="BALANCE_TRIGGER" />
                    <el-option label="比例归集" value="RATIO_BASED" />
                    <el-option label="智能归集" value="INTELLIGENT" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="归集频率" prop="frequency">
                  <el-select v-model="temp.frequency" placeholder="请选择归集频率" style="width: 100%;">
                    <el-option label="每日" value="DAILY" />
                    <el-option label="每周" value="WEEKLY" />
                    <el-option label="每月" value="MONTHLY" />
                    <el-option label="实时" value="REALTIME" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="执行时间">
                  <el-time-picker
                    v-model="temp.executionTime"
                    placeholder="选择执行时间"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="策略描述">
              <el-input v-model="temp.strategyDescription" type="textarea" :rows="3" placeholder="请输入策略描述" />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="账户配置" name="accounts">
            <el-form-item label="目标账户" prop="targetAccountId">
              <el-select v-model="temp.targetAccountId" placeholder="请选择目标账户" style="width: 100%;">
                <el-option
                  v-for="account in availableAccounts"
                  :key="account.accountId"
                  :label="`${account.accountName} - ${account.accountNumber}`"
                  :value="account.accountId"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="源账户">
              <el-transfer
                v-model="temp.sourceAccountIds"
                :data="availableAccounts"
                :titles="['可选账户', '已选账户']"
                :props="{
                  key: 'accountId',
                  label: 'accountName'
                }"
                style="text-align: left; display: inline-block"
              />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="归集规则" name="rules">
            <el-form-item label="归集条件" prop="concentrationCondition">
              <el-input v-model="temp.concentrationCondition" placeholder="请输入归集条件" />
            </el-form-item>
            <el-form-item label="最小归集金额">
              <el-input-number
                v-model="temp.minConcentrationAmount"
                :precision="2"
                :min="0"
                style="width: 100%;"
                placeholder="请输入最小归集金额"
              />
            </el-form-item>
            <el-form-item label="保留余额">
              <el-input-number
                v-model="temp.retainBalance"
                :precision="2"
                :min="0"
                style="width: 100%;"
                placeholder="请输入保留余额"
              />
            </el-form-item>
            <el-form-item label="归集比例(%)">
              <el-slider
                v-model="temp.concentrationRatio"
                :min="0"
                :max="100"
                :step="5"
                show-input
                style="width: 100%;"
              />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="风险控制" name="risk">
            <el-form-item label="单日限额">
              <el-input-number
                v-model="temp.dailyLimit"
                :precision="2"
                :min="0"
                style="width: 100%;"
                placeholder="请输入单日限额"
              />
            </el-form-item>
            <el-form-item label="单笔限额">
              <el-input-number
                v-model="temp.singleLimit"
                :precision="2"
                :min="0"
                style="width: 100%;"
                placeholder="请输入单笔限额"
              />
            </el-form-item>
            <el-form-item label="风险等级">
              <el-select v-model="temp.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
                <el-option label="低风险" value="LOW" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="高风险" value="HIGH" />
              </el-select>
            </el-form-item>
            <el-form-item label="异常处理">
              <el-checkbox-group v-model="temp.exceptionHandling">
                <el-checkbox label="EMAIL_NOTIFY">邮件通知</el-checkbox>
                <el-checkbox label="SMS_NOTIFY">短信通知</el-checkbox>
                <el-checkbox label="AUTO_SUSPEND">自动暂停</el-checkbox>
                <el-checkbox label="MANUAL_REVIEW">人工审核</el-checkbox>
              </el-checkbox-group>
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

    <!-- 策略详情对话框 -->
    <el-dialog title="策略详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentStrategy" class="strategy-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="策略名称">{{ currentStrategy.strategyName }}</el-descriptions-item>
          <el-descriptions-item label="策略类型">{{ getStrategyTypeText(currentStrategy.strategyType) }}</el-descriptions-item>
          <el-descriptions-item label="归集频率">{{ getFrequencyText(currentStrategy.frequency) }}</el-descriptions-item>
          <el-descriptions-item label="策略状态">
            <el-tag :type="getStatusTagType(currentStrategy.strategyStatus)">
              {{ getStatusText(currentStrategy.strategyStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="源账户数">{{ currentStrategy.sourceAccountCount }}</el-descriptions-item>
          <el-descriptions-item label="目标账户">{{ currentStrategy.targetAccountName }}</el-descriptions-item>
          <el-descriptions-item label="最后执行">{{ currentStrategy.lastExecutionTime || '未执行' }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ currentStrategy.creatorName }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px;">
          <h4>策略描述</h4>
          <p>{{ currentStrategy.strategyDescription }}</p>
        </div>
        <div style="margin-top: 20px;">
          <h4>归集条件</h4>
          <p>{{ currentStrategy.concentrationCondition }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleEdit(currentStrategy)">编辑策略</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFundConcentrationStrategyPage, createFundConcentrationStrategy, updateFundConcentrationStrategy } from '@/api/globalTreasurer/zjjz'
import Pagination from '@/components/Pagination'

export default {
  name: 'FundConcentrationStrategy',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        strategyName: undefined,
        strategyType: undefined,
        strategyStatus: undefined,
        frequency: undefined
      },
      totalStrategies: 28,
      activeStrategies: 23,
      inactiveStrategies: 5,
      coveredAccounts: 156,
      strategyList: [],
      multipleSelection: [],
      availableAccounts: [],
      currentStrategy: null,
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      activeFormTab: 'basic',
      temp: {
        strategyId: undefined,
        strategyName: '',
        strategyType: '',
        frequency: '',
        executionTime: null,
        strategyDescription: '',
        targetAccountId: undefined,
        sourceAccountIds: [],
        concentrationCondition: '',
        minConcentrationAmount: 0,
        retainBalance: 0,
        concentrationRatio: 80,
        dailyLimit: 0,
        singleLimit: 0,
        riskLevel: 'LOW',
        exceptionHandling: []
      },
      rules: {
        strategyName: [{ required: true, message: '策略名称不能为空', trigger: 'blur' }],
        strategyType: [{ required: true, message: '请选择策略类型', trigger: 'change' }],
        frequency: [{ required: true, message: '请选择归集频率', trigger: 'change' }],
        targetAccountId: [{ required: true, message: '请选择目标账户', trigger: 'change' }],
        concentrationCondition: [{ required: true, message: '归集条件不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.loadAvailableAccounts()
  },
  methods: {
    getList() {
      this.listLoading = true
      getFundConcentrationStrategyPage({
        current: this.listQuery.page,
        size: this.listQuery.limit,
        strategyName: this.listQuery.strategyName,
        strategyType: this.listQuery.strategyType,
        strategyStatus: this.listQuery.strategyStatus
      }).then(response => {
        if (response.code === 1) {
          this.strategyList = response.data.tlist
          this.total = response.data.totalRecord
        } else {
          this.$message.error(response.msg || '获取策略列表失败')
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取策略列表失败:', error)
        this.$message.error('获取策略列表失败')
        this.listLoading = false
      })
    },
    loadAvailableAccounts() {
      // 模拟可用账户数据
      this.availableAccounts = [
        { accountId: 1, accountName: '示例云科技集团总账户', accountNumber: '1234567890123456789' },
        { accountId: 2, accountName: '示例云投资专户', accountNumber: '9876543210987654321' },
        { accountId: 3, accountName: '示例云贸易账户', accountNumber: '5555666677778888999' }
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
        strategyName: undefined,
        strategyType: undefined,
        strategyStatus: undefined,
        frequency: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '新增归集策略'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑归集策略'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentStrategy = row
      this.dialogDetailVisible = true
    },
    handleEnable(row) {
      this.$confirm('确认启用该归集策略?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.strategyStatus = 'ACTIVE'
        this.$message({
          type: 'success',
          message: '策略已启用!'
        })
      })
    },
    handleDisable(row) {
      this.$confirm('确认停用该归集策略?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.strategyStatus = 'INACTIVE'
        this.$message({
          type: 'success',
          message: '策略已停用!'
        })
      })
    },
    handleTest(row) {
      this.$message({
        type: 'info',
        message: '策略测试已启动，请查看执行日志'
      })
    },
    handleBatchEnable() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要启用的策略'
        })
        return
      }
      this.$confirm(`确认批量启用选中的${this.multipleSelection.length}个策略?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '批量启用成功!'
        })
        this.getList()
      })
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '策略配置导出成功'
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.strategyId = parseInt(Math.random() * 100) + 1024
          this.temp.strategyStatus = 'INACTIVE'
          this.temp.creatorName = '当前用户'
          this.temp.lastExecutionTime = null
          this.strategyList.unshift(this.temp)
          this.total = this.strategyList.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '归集策略创建成功'
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const index = this.strategyList.findIndex(v => v.strategyId === this.temp.strategyId)
          this.strategyList.splice(index, 1, this.temp)
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '归集策略更新成功'
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        strategyId: undefined,
        strategyName: '',
        strategyType: '',
        frequency: '',
        executionTime: null,
        strategyDescription: '',
        targetAccountId: undefined,
        sourceAccountIds: [],
        concentrationCondition: '',
        minConcentrationAmount: 0,
        retainBalance: 0,
        concentrationRatio: 80,
        dailyLimit: 0,
        singleLimit: 0,
        riskLevel: 'LOW',
        exceptionHandling: []
      }
    },
    getStrategyTypeTagType(type) {
      const typeMap = {
        'SCHEDULED': 'primary',
        'BALANCE_TRIGGER': 'success',
        'RATIO_BASED': 'warning',
        'INTELLIGENT': 'info'
      }
      return typeMap[type] || 'info'
    },
    getStrategyTypeText(type) {
      const textMap = {
        'SCHEDULED': '定时归集',
        'BALANCE_TRIGGER': '余额触发',
        'RATIO_BASED': '比例归集',
        'INTELLIGENT': '智能归集'
      }
      return textMap[type] || type
    },
    getFrequencyText(frequency) {
      const textMap = {
        'DAILY': '每日',
        'WEEKLY': '每周',
        'MONTHLY': '每月',
        'REALTIME': '实时'
      }
      return textMap[frequency] || frequency
    },
    getStatusTagType(status) {
      const typeMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'TESTING': 'warning'
      }
      return typeMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '启用',
        'INACTIVE': '停用',
        'TESTING': '测试'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.fund-concentration-strategy {
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

  .strategy-overview {
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
          &.inactive-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.coverage-icon {
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

  .account-count {
    font-weight: 600;
    color: #409EFF;
  }

  .account-name {
    font-weight: 600;
    color: #303133;
  }

  .account-number {
    font-size: 12px;
    color: #909399;
    margin-top: 2px;
  }

  .condition-text {
    font-size: 12px;
    color: #606266;
  }

  .strategy-detail {
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
