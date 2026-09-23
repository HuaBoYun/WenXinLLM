<template>
  <div class="account-freeze-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-lock"></i>
            账户冻结记录管理
          </h2>
          <p class="page-description">管理银行账户冻结和解冻记录，包括冻结原因、冻结金额、解冻操作等功能</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增冻结
          </el-button>
          <el-button type="success" icon="el-icon-unlock" @click="handleBatchUnfreeze">
            批量解冻
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 冻结统计卡片 -->
    <div class="freeze-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总记录数</div>
                <div class="card-value">{{ totalRecords }}</div>
                <div class="card-change">条记录</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon frozen-icon">
                <i class="el-icon-lock"></i>
              </div>
              <div class="card-info">
                <div class="card-title">冻结中</div>
                <div class="card-value">{{ frozenRecords }}</div>
                <div class="card-change warning">待解冻</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon unfrozen-icon">
                <i class="el-icon-unlock"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已解冻</div>
                <div class="card-value">{{ unfrozenRecords }}</div>
                <div class="card-change positive">解冻率{{ unfreezeRate }}%</div>
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
                <div class="card-title">冻结金额</div>
                <div class="card-value">{{ totalFrozenAmount }}</div>
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
          <el-form-item label="记录ID">
            <el-input
              v-model="listQuery.recordId"
              placeholder="请输入记录ID"
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
          <el-form-item label="冻结类型">
            <el-select
              v-model="listQuery.freezeType"
              placeholder="请选择冻结类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="司法冻结" value="JUDICIAL" />
              <el-option label="风险冻结" value="RISK" />
              <el-option label="系统冻结" value="SYSTEM" />
              <el-option label="手动冻结" value="MANUAL" />
              <el-option label="其他冻结" value="OTHER" />
            </el-select>
          </el-form-item>
          <el-form-item label="冻结状态">
            <el-select
              v-model="listQuery.freezeStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="冻结中" value="FROZEN" />
              <el-option label="已解冻" value="UNFROZEN" />
              <el-option label="部分解冻" value="PARTIAL_UNFROZEN" />
            </el-select>
          </el-form-item>
          <el-form-item label="冻结日期">
            <el-date-picker
              v-model="listQuery.freezeDateRange"
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

    <!-- 冻结记录表格 -->
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
        <el-table-column label="记录ID" prop="recordId" sortable="custom" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.recordId }}</span>
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
        <el-table-column label="冻结类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getFreezeTypeTagType(row.freezeType)" size="mini">
              {{ getFreezeTypeText(row.freezeType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="冻结金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="freeze-amount">{{ formatCurrency(row.freezeAmount, row.currencyCode) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已解冻金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="unfreeze-amount">{{ formatCurrency(row.unfreezeAmount, row.currencyCode) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="冻结状态" class-name="status-col" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getFreezeStatusTagType(row.freezeStatus)">
              {{ getFreezeStatusText(row.freezeStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="冻结日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.freezeDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="解冻日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.unfreezeDate || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="冻结原因" min-width="150px">
          <template slot-scope="{row}">
            <span class="reason-text">{{ row.freezeReason }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              查看
            </el-button>
            <el-button v-if="row.freezeStatus === 'FROZEN'" size="mini" type="success" @click="handleUnfreeze(row)">
              解冻
            </el-button>
            <el-button v-if="row.freezeStatus === 'FROZEN'" size="mini" type="warning" @click="handlePartialUnfreeze(row)">
              部分解冻
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增冻结对话框 -->
    <el-dialog title="新增账户冻结" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-form-item label="选择账户" prop="accountId">
          <el-select v-model="temp.accountId" placeholder="请选择要冻结的账户" style="width: 100%;" @change="handleAccountChange">
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
        <el-row :gutter="20" v-if="temp.accountId">
          <el-col :span="12">
            <el-form-item label="可用余额">
              <el-input v-model="temp.availableBalanceDisplay" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种">
              <el-input v-model="temp.currencyCode" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="冻结类型" prop="freezeType">
              <el-select v-model="temp.freezeType" placeholder="请选择冻结类型" style="width: 100%;">
                <el-option label="司法冻结" value="JUDICIAL" />
                <el-option label="风险冻结" value="RISK" />
                <el-option label="系统冻结" value="SYSTEM" />
                <el-option label="手动冻结" value="MANUAL" />
                <el-option label="其他冻结" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="冻结金额" prop="freezeAmount">
              <el-input-number
                v-model="temp.freezeAmount"
                :precision="2"
                :min="0"
                :max="temp.availableBalance"
                style="width: 100%;"
                placeholder="请输入冻结金额"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="冻结原因" prop="freezeReason">
          <el-input v-model="temp.freezeReason" type="textarea" :rows="3" placeholder="请详细说明冻结原因" />
        </el-form-item>
        <el-form-item label="冻结期限">
          <el-date-picker
            v-model="temp.freezeExpireDate"
            type="date"
            placeholder="选择冻结到期日期（可选）"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="执行机构">
          <el-input v-model="temp.executeOrganization" placeholder="请输入执行机构名称" />
        </el-form-item>
        <el-form-item label="执行文书号">
          <el-input v-model="temp.executeDocumentNo" placeholder="请输入执行文书号" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="createData">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 记录详情对话框 -->
    <el-dialog title="冻结记录详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentRecord" class="record-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="记录ID">{{ currentRecord.recordId }}</el-descriptions-item>
          <el-descriptions-item label="冻结状态">
            <el-tag :type="getFreezeStatusTagType(currentRecord.freezeStatus)">
              {{ getFreezeStatusText(currentRecord.freezeStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="账户号码">{{ currentRecord.accountNumber }}</el-descriptions-item>
          <el-descriptions-item label="账户名称">{{ currentRecord.accountName }}</el-descriptions-item>
          <el-descriptions-item label="冻结类型">{{ getFreezeTypeText(currentRecord.freezeType) }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ currentRecord.currencyCode }}</el-descriptions-item>
          <el-descriptions-item label="冻结金额">{{ formatCurrency(currentRecord.freezeAmount, currentRecord.currencyCode) }}</el-descriptions-item>
          <el-descriptions-item label="已解冻金额">{{ formatCurrency(currentRecord.unfreezeAmount, currentRecord.currencyCode) }}</el-descriptions-item>
          <el-descriptions-item label="冻结日期">{{ currentRecord.freezeDate }}</el-descriptions-item>
          <el-descriptions-item label="解冻日期">{{ currentRecord.unfreezeDate || '未解冻' }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ currentRecord.freezeExpireDate || '无期限' }}</el-descriptions-item>
          <el-descriptions-item label="执行机构">{{ currentRecord.executeOrganization || '-' }}</el-descriptions-item>
          <el-descriptions-item label="执行文书号">{{ currentRecord.executeDocumentNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="冻结人">{{ currentRecord.freezeUserName }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px;">
          <h4>冻结原因</h4>
          <p>{{ currentRecord.freezeReason }}</p>
        </div>
        <div v-if="currentRecord.unfreezeReason" style="margin-top: 20px;">
          <h4>解冻原因</h4>
          <p>{{ currentRecord.unfreezeReason }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentRecord && currentRecord.freezeStatus === 'FROZEN'" type="success" @click="handleUnfreeze(currentRecord)">
          解冻
        </el-button>
      </div>
    </el-dialog>

    <!-- 解冻对话框 -->
    <el-dialog title="账户解冻" :visible.sync="dialogUnfreezeVisible" width="600px">
      <el-form ref="unfreezeForm" :model="unfreezeForm" label-width="120px">
        <el-form-item label="解冻类型" prop="unfreezeType">
          <el-radio-group v-model="unfreezeForm.unfreezeType">
            <el-radio label="FULL">全部解冻</el-radio>
            <el-radio label="PARTIAL">部分解冻</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="unfreezeForm.unfreezeType === 'PARTIAL'" label="解冻金额" prop="unfreezeAmount">
          <el-input-number
            v-model="unfreezeForm.unfreezeAmount"
            :precision="2"
            :min="0"
            :max="currentRecord ? (currentRecord.freezeAmount - currentRecord.unfreezeAmount) : 0"
            style="width: 100%;"
            placeholder="请输入解冻金额"
          />
        </el-form-item>
        <el-form-item label="解冻原因" prop="unfreezeReason">
          <el-input v-model="unfreezeForm.unfreezeReason" type="textarea" :rows="4" placeholder="请输入解冻原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogUnfreezeVisible = false">取消</el-button>
        <el-button type="primary" @click="submitUnfreeze">确认解冻</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFreezeRecordPage, createFreezeRecord, unfreezeRecord, deleteFreezeRecord } from '@/api/globalTreasurer/zhgl'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'AccountFreezeManage',
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
        recordId: undefined,
        accountNumber: undefined,
        freezeType: undefined,
        freezeStatus: undefined,
        freezeDateRange: undefined
      },
      totalRecords: 0,
      frozenRecords: 0,
      unfrozenRecords: 0,
      unfreezeRate: 0,
      totalFrozenAmount: 0,
      multipleSelection: [],
      availableAccounts: [],
      temp: {
        recordId: undefined,
        accountId: undefined,
        accountNumber: '',
        accountName: '',
        currencyCode: '',
        availableBalance: 0,
        availableBalanceDisplay: '',
        freezeType: '',
        freezeAmount: 0,
        freezeReason: '',
        freezeExpireDate: null,
        executeOrganization: '',
        executeDocumentNo: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogUnfreezeVisible: false,
      currentRecord: null,
      unfreezeForm: {
        unfreezeType: 'FULL',
        unfreezeAmount: 0,
        unfreezeReason: ''
      },
      rules: {
        accountId: [{ required: true, message: '请选择账户', trigger: 'change' }],
        freezeType: [{ required: true, message: '请选择冻结类型', trigger: 'change' }],
        freezeAmount: [{ required: true, message: '请输入冻结金额', trigger: 'blur' }],
        freezeReason: [{ required: true, message: '请输入冻结原因', trigger: 'blur' }]
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
            recordId: 1,
            accountId: 1,
            accountNumber: '1234567890123456789',
            accountName: '示例云科技有限公司',
            currencyCode: 'CNY',
            freezeType: 'JUDICIAL',
            freezeAmount: 100000.00,
            unfreezeAmount: 0,
            freezeStatus: 'FROZEN',
            freezeDate: '2024-09-20',
            unfreezeDate: null,
            freezeExpireDate: '2024-12-20',
            freezeReason: '法院执行案件，冻结企业银行账户',
            unfreezeReason: null,
            executeOrganization: '北京市朝阳区人民法院',
            executeDocumentNo: '(2024)京0105执1234号',
            freezeUser: 1,
            freezeUserName: '系统管理员',
            unfreezeUser: null,
            remark: '司法冻结',
            createTime: '2024-09-20 10:30:00'
          },
          {
            recordId: 2,
            accountId: 2,
            accountNumber: '9876543210987654321',
            accountName: '示例云投资管理有限公司',
            currencyCode: 'USD',
            freezeType: 'RISK',
            freezeAmount: 50000.00,
            unfreezeAmount: 50000.00,
            freezeStatus: 'UNFROZEN',
            freezeDate: '2024-09-15',
            unfreezeDate: '2024-09-22',
            freezeExpireDate: null,
            freezeReason: '风险控制，临时冻结账户资金',
            unfreezeReason: '风险排除，恢复账户正常使用',
            executeOrganization: null,
            executeDocumentNo: null,
            freezeUser: 1,
            freezeUserName: '风控专员',
            unfreezeUser: 1,
            remark: '风险冻结',
            createTime: '2024-09-15 14:20:00'
          }
        ]
        this.total = this.list.length
        this.listLoading = false
      }, 1000)
    },
    updateStatistics() {
      this.totalRecords = 234
      this.frozenRecords = 45
      this.unfrozenRecords = 189
      this.unfreezeRate = Math.round((this.unfrozenRecords / this.totalRecords) * 100)
      this.totalFrozenAmount = 1250.8
    },
    loadAvailableAccounts() {
      // 模拟可用账户数据
      this.availableAccounts = [
        { accountId: 1, accountNumber: '1234567890123456789', accountName: '示例云科技有限公司', availableBalance: 500000, currencyCode: 'CNY' },
        { accountId: 2, accountNumber: '9876543210987654321', accountName: '示例云投资管理有限公司', availableBalance: 200000, currencyCode: 'USD' },
        { accountId: 3, accountNumber: '5555666677778888999', accountName: '示例云贸易有限公司', availableBalance: 800000, currencyCode: 'CNY' }
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
        recordId: undefined,
        accountNumber: undefined,
        freezeType: undefined,
        freezeStatus: undefined,
        freezeDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentRecord = row
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
        this.temp.currencyCode = account.currencyCode
        this.temp.availableBalance = account.availableBalance
        this.temp.availableBalanceDisplay = this.formatCurrency(account.availableBalance, account.currencyCode)
      }
    },
    handleUnfreeze(row) {
      this.currentRecord = row
      this.unfreezeForm = {
        unfreezeType: 'FULL',
        unfreezeAmount: 0,
        unfreezeReason: ''
      }
      this.dialogUnfreezeVisible = true
    },
    handlePartialUnfreeze(row) {
      this.currentRecord = row
      this.unfreezeForm = {
        unfreezeType: 'PARTIAL',
        unfreezeAmount: 0,
        unfreezeReason: ''
      }
      this.dialogUnfreezeVisible = true
    },
    handleDelete(row, index) {
      this.$confirm('确认删除该冻结记录?', '提示', {
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
    handleBatchUnfreeze() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要解冻的记录'
        })
        return
      }
      const frozenRecords = this.multipleSelection.filter(record => record.freezeStatus === 'FROZEN')
      if (frozenRecords.length === 0) {
        this.$message({
          type: 'warning',
          message: '选中的记录中没有冻结状态的记录'
        })
        return
      }
      this.$confirm(`确认批量解冻选中的${frozenRecords.length}个冻结记录?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        frozenRecords.forEach(record => {
          record.freezeStatus = 'UNFROZEN'
          record.unfreezeDate = new Date().toISOString().slice(0, 10)
          record.unfreezeAmount = record.freezeAmount
        })
        this.$message({
          type: 'success',
          message: '批量解冻成功!'
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
          this.temp.recordId = parseInt(Math.random() * 100) + 1024
          this.temp.freezeStatus = 'FROZEN'
          this.temp.freezeDate = new Date().toISOString().slice(0, 10)
          this.temp.unfreezeAmount = 0
          this.temp.freezeUserName = '当前用户'
          this.temp.createTime = new Date().toLocaleString()
          this.list.unshift(this.temp)
          this.total = this.list.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '冻结记录创建成功'
          })
        }
      })
    },
    submitUnfreeze() {
      if (!this.unfreezeForm.unfreezeReason) {
        this.$message({
          type: 'warning',
          message: '请输入解冻原因'
        })
        return
      }
      if (this.unfreezeForm.unfreezeType === 'PARTIAL' && !this.unfreezeForm.unfreezeAmount) {
        this.$message({
          type: 'warning',
          message: '请输入解冻金额'
        })
        return
      }
      
      // 更新记录状态
      const index = this.list.findIndex(v => v.recordId === this.currentRecord.recordId)
      if (index !== -1) {
        if (this.unfreezeForm.unfreezeType === 'FULL') {
          this.list[index].freezeStatus = 'UNFROZEN'
          this.list[index].unfreezeAmount = this.list[index].freezeAmount
        } else {
          const newUnfreezeAmount = this.list[index].unfreezeAmount + this.unfreezeForm.unfreezeAmount
          this.list[index].unfreezeAmount = newUnfreezeAmount
          if (newUnfreezeAmount >= this.list[index].freezeAmount) {
            this.list[index].freezeStatus = 'UNFROZEN'
          } else {
            this.list[index].freezeStatus = 'PARTIAL_UNFROZEN'
          }
        }
        this.list[index].unfreezeDate = new Date().toISOString().slice(0, 10)
        this.list[index].unfreezeReason = this.unfreezeForm.unfreezeReason
      }
      
      this.dialogUnfreezeVisible = false
      this.dialogDetailVisible = false
      this.$message({
        type: 'success',
        message: '解冻操作完成'
      })
    },
    resetTemp() {
      this.temp = {
        recordId: undefined,
        accountId: undefined,
        accountNumber: '',
        accountName: '',
        currencyCode: '',
        availableBalance: 0,
        availableBalanceDisplay: '',
        freezeType: '',
        freezeAmount: 0,
        freezeReason: '',
        freezeExpireDate: null,
        executeOrganization: '',
        executeDocumentNo: '',
        remark: ''
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'recordId') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.list.sort((a, b) => a.recordId - b.recordId)
      } else {
        this.list.sort((a, b) => b.recordId - a.recordId)
      }
    },
    getFreezeTypeTagType(freezeType) {
      const typeMap = {
        'JUDICIAL': 'danger',
        'RISK': 'warning',
        'SYSTEM': 'primary',
        'MANUAL': 'success',
        'OTHER': 'info'
      }
      return typeMap[freezeType] || 'info'
    },
    getFreezeTypeText(freezeType) {
      const textMap = {
        'JUDICIAL': '司法冻结',
        'RISK': '风险冻结',
        'SYSTEM': '系统冻结',
        'MANUAL': '手动冻结',
        'OTHER': '其他冻结'
      }
      return textMap[freezeType] || freezeType
    },
    getFreezeStatusTagType(status) {
      const statusMap = {
        'FROZEN': 'danger',
        'UNFROZEN': 'success',
        'PARTIAL_UNFROZEN': 'warning'
      }
      return statusMap[status] || 'info'
    },
    getFreezeStatusText(status) {
      const textMap = {
        'FROZEN': '冻结中',
        'UNFROZEN': '已解冻',
        'PARTIAL_UNFROZEN': '部分解冻'
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
.account-freeze-manage {
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
            color: #F56C6C;
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

  .freeze-overview {
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
          &.frozen-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.unfrozen-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.amount-icon {
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

  .reason-text {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.4;
  }

  .freeze-amount {
    color: #F56C6C;
    font-weight: 600;
  }

  .unfreeze-amount {
    color: #67C23A;
    font-weight: 600;
  }

  .record-detail {
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
