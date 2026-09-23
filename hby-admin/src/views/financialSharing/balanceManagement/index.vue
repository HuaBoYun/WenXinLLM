<template>
  <div class="app-container">
    <el-page-header content="余额管理" @back="goBack" />

    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <!-- 余额查询标签页 -->
      <el-tab-pane label="余额查询" name="balance">
        <!-- 查询表单 -->
        <vab-query-form>
          <vab-query-form-top-panel>
            <el-form :inline="true" :model="balanceQueryForm" @submit.native.prevent>
              <el-form-item label="科目编码">
                <el-input
                  v-model="balanceQueryForm.accountCode"
                  placeholder="请输入科目编码"
                  clearable
                  @keyup.enter.native="handleBalanceQuery"
                />
              </el-form-item>
              <el-form-item label="科目名称">
                <el-input
                  v-model="balanceQueryForm.accountName"
                  placeholder="请输入科目名称"
                  clearable
                  @keyup.enter.native="handleBalanceQuery"
                />
              </el-form-item>
              <el-form-item label="会计期间">
                <el-date-picker
                  v-model="balanceQueryForm.period"
                  type="month"
                  placeholder="选择期间"
                  format="yyyyMM"
                  value-format="yyyyMM"
                />
              </el-form-item>
              <el-form-item label="币种">
                <el-select v-model="balanceQueryForm.currencyCode" placeholder="请选择" clearable>
                  <el-option label="CNY-人民币" value="CNY" />
                  <el-option label="USD-美元" value="USD" />
                  <el-option label="EUR-欧元" value="EUR" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleBalanceQuery">
                  查询
                </el-button>
                <el-button icon="el-icon-refresh-left" @click="resetBalanceQuery">
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-top-panel>

          <vab-query-form-left-panel>
            <el-button type="primary" icon="el-icon-refresh" @click="handleRefreshBalance">
              刷新余额
            </el-button>
            <el-button type="success" icon="el-icon-download" @click="handleExportBalance">
              导出余额
            </el-button>
            <el-button type="warning" icon="el-icon-warning" @click="handleValidateBalance">
              数据校验
            </el-button>
          </vab-query-form-left-panel>
        </vab-query-form>

        <!-- 余额表格 -->
        <el-table
          v-loading="balanceLoading"
          :data="balanceList"
          :element-loading-text="elementLoadingText"
          show-summary
        >
          <el-table-column label="科目编码" prop="accountCode" width="120" />
          <el-table-column label="科目名称" prop="accountName" width="200" show-overflow-tooltip />
          <el-table-column label="期间" prop="period" width="80" />
          <el-table-column label="币种" prop="currencyCode" width="80" />
          <el-table-column label="期初余额" align="right">
            <el-table-column label="借方" prop="beginningDebit" width="120" align="right">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.beginningDebit) }}
              </template>
            </el-table-column>
            <el-table-column label="贷方" prop="beginningCredit" width="120" align="right">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.beginningCredit) }}
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column label="本期发生" align="right">
            <el-table-column label="借方" prop="periodDebit" width="120" align="right">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.periodDebit) }}
              </template>
            </el-table-column>
            <el-table-column label="贷方" prop="periodCredit" width="120" align="right">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.periodCredit) }}
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column label="期末余额" align="right">
            <el-table-column label="借方" prop="endingDebit" width="120" align="right">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.endingDebit) }}
              </template>
            </el-table-column>
            <el-table-column label="贷方" prop="endingCredit" width="120" align="right">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.endingCredit) }}
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column label="更新时间" prop="updateTime" width="150" />
        </el-table>

        <!-- 分页 -->
        <el-pagination
          background
          :current-page="balanceQueryForm.pageNo"
          :page-size="balanceQueryForm.pageSize"
          :layout="layout"
          :total="balanceTotal"
          @size-change="handleBalanceSizeChange"
          @current-change="handleBalanceCurrentChange"
        />
      </el-tab-pane>

      <!-- 余额调整标签页 -->
      <el-tab-pane label="余额调整" name="adjustment">
        <!-- 调整查询表单 -->
        <vab-query-form>
          <vab-query-form-top-panel>
            <el-form :inline="true" :model="adjustmentQueryForm" @submit.native.prevent>
              <el-form-item label="科目编码">
                <el-input
                  v-model="adjustmentQueryForm.accountCode"
                  placeholder="请输入科目编码"
                  clearable
                  @keyup.enter.native="handleAdjustmentQuery"
                />
              </el-form-item>
              <el-form-item label="调整类型">
                <el-select v-model="adjustmentQueryForm.adjustmentType" placeholder="请选择" clearable>
                  <el-option
                    v-for="item in adjustmentTypes"
                    :key="item.typeCode"
                    :label="item.typeName"
                    :value="item.typeCode"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="状态">
                <el-select v-model="adjustmentQueryForm.status" placeholder="请选择" clearable>
                  <el-option label="待审批" value="PENDING" />
                  <el-option label="已通过" value="APPROVED" />
                  <el-option label="已拒绝" value="REJECTED" />
                  <el-option label="已撤销" value="CANCELLED" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleAdjustmentQuery">
                  查询
                </el-button>
                <el-button icon="el-icon-refresh-left" @click="resetAdjustmentQuery">
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-top-panel>

          <vab-query-form-left-panel>
            <el-button type="primary" icon="el-icon-plus" @click="handleAddAdjustment">
              新增调整
            </el-button>
            <el-button
              type="success"
              icon="el-icon-check"
              @click="handleBatchApprove"
              :disabled="adjustmentSelection.length === 0"
            >
              批量审批
            </el-button>
          </vab-query-form-left-panel>
        </vab-query-form>

        <!-- 调整表格 -->
        <el-table
          v-loading="adjustmentLoading"
          :data="adjustmentList"
          :element-loading-text="elementLoadingText"
          @selection-change="handleAdjustmentSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column label="科目编码" prop="accountCode" width="120" />
          <el-table-column label="科目名称" prop="accountName" width="200" show-overflow-tooltip />
          <el-table-column label="期间" prop="period" width="80" />
          <el-table-column label="调整类型" prop="adjustmentType" width="120">
            <template slot-scope="scope">
              <el-tag :type="getAdjustmentTypeTag(scope.row.adjustmentType)">
                {{ getAdjustmentTypeName(scope.row.adjustmentType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="调整金额" prop="adjustmentAmount" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.adjustmentAmount) }}
            </template>
          </el-table-column>
          <el-table-column label="状态" prop="status" width="80">
            <template slot-scope="scope">
              <el-tag :type="getAdjustmentStatusTag(scope.row.status)">
                {{ getAdjustmentStatusName(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="申请人" prop="applicantName" width="100" />
          <el-table-column label="申请时间" prop="applyTime" width="150" />
          <el-table-column label="审批人" prop="approverName" width="100" />
          <el-table-column label="操作" width="200" fixed="right">
            <template slot-scope="scope">
              <el-button type="text" @click="handleViewAdjustment(scope.row)">
                查看
              </el-button>
              <el-button
                v-if="scope.row.status === 'PENDING'"
                type="text"
                @click="handleApproveAdjustment(scope.row)"
              >
                审批
              </el-button>
              <el-button
                v-if="scope.row.status === 'PENDING'"
                type="text"
                style="color: #f56c6c"
                @click="handleCancelAdjustment(scope.row)"
              >
                撤销
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination
          background
          :current-page="adjustmentQueryForm.pageNo"
          :page-size="adjustmentQueryForm.pageSize"
          :layout="layout"
          :total="adjustmentTotal"
          @size-change="handleAdjustmentSizeChange"
          @current-change="handleAdjustmentCurrentChange"
        />
      </el-tab-pane>
    </el-tabs>

    <!-- 余额调整对话框 -->
    <el-dialog
      title="新增余额调整"
      :visible.sync="adjustmentDialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleAdjustmentDialogClose"
    >
      <el-form
        ref="adjustmentForm"
        :model="adjustmentForm"
        :rules="adjustmentRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="科目编码" prop="accountCode">
              <el-input v-model="adjustmentForm.accountCode" placeholder="请输入科目编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="科目名称" prop="accountName">
              <el-input v-model="adjustmentForm.accountName" placeholder="请输入科目名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="会计期间" prop="period">
              <el-date-picker
                v-model="adjustmentForm.period"
                type="month"
                placeholder="选择期间"
                format="yyyyMM"
                value-format="yyyyMM"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="adjustmentForm.currencyCode" placeholder="请选择币种">
                <el-option label="CNY-人民币" value="CNY" />
                <el-option label="USD-美元" value="USD" />
                <el-option label="EUR-欧元" value="EUR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调整类型" prop="adjustmentType">
              <el-select v-model="adjustmentForm.adjustmentType" placeholder="请选择调整类型">
                <el-option
                  v-for="item in adjustmentTypes"
                  :key="item.typeCode"
                  :label="item.typeName"
                  :value="item.typeCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调整金额" prop="adjustmentAmount">
              <el-input-number
                v-model="adjustmentForm.adjustmentAmount"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="原期初借方">
              <el-input-number
                v-model="adjustmentForm.originalBeginningDebit"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原期初贷方">
              <el-input-number
                v-model="adjustmentForm.originalBeginningCredit"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="新期初借方">
              <el-input-number
                v-model="adjustmentForm.newBeginningDebit"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="新期初贷方">
              <el-input-number
                v-model="adjustmentForm.newBeginningCredit"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="调整原因" prop="adjustmentReason">
          <el-input
            v-model="adjustmentForm.adjustmentReason"
            type="textarea"
            placeholder="请输入调整原因"
          />
        </el-form-item>

        <el-form-item label="备注">
          <el-input
            v-model="adjustmentForm.remark"
            type="textarea"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="adjustmentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveAdjustment">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBalanceList,
  refreshBalance,
  exportBalanceData,
  validateBalanceData,
  getBalanceAdjustmentList,
  createBalanceAdjustment,
  approveBalanceAdjustment,
  cancelBalanceAdjustment,
  getBalanceAdjustmentTypes,
  getBalanceSummary
} from '@/api/financialSharing/balanceManagement'

export default {
  name: 'BalanceManagement',
  data() {
    return {
      activeTab: 'balance',
      balanceLoading: true,
      adjustmentLoading: true,
      elementLoadingText: '正在加载...',
      layout: 'total, sizes, prev, pager, next, jumper',

      // 余额查询相关
      balanceList: [],
      balanceTotal: 0,
      balanceQueryForm: {
        pageNo: 1,
        pageSize: 10,
        accountCode: '',
        accountName: '',
        period: '',
        currencyCode: ''
      },

      // 余额调整相关
      adjustmentList: [],
      adjustmentTotal: 0,
      adjustmentSelection: [],
      adjustmentQueryForm: {
        pageNo: 1,
        pageSize: 10,
        accountCode: '',
        adjustmentType: '',
        status: ''
      },
      adjustmentTypes: [],
      adjustmentDialogVisible: false,
      adjustmentForm: {
        accountCode: '',
        accountName: '',
        period: '',
        currencyCode: 'CNY',
        adjustmentType: '',
        adjustmentAmount: 0,
        originalBeginningDebit: 0,
        originalBeginningCredit: 0,
        newBeginningDebit: 0,
        newBeginningCredit: 0,
        adjustmentReason: '',
        remark: ''
      },
      adjustmentRules: {
        accountCode: [
          { required: true, message: '请输入科目编码', trigger: 'blur' }
        ],
        accountName: [
          { required: true, message: '请输入科目名称', trigger: 'blur' }
        ],
        period: [
          { required: true, message: '请选择会计期间', trigger: 'change' }
        ],
        currencyCode: [
          { required: true, message: '请选择币种', trigger: 'change' }
        ],
        adjustmentType: [
          { required: true, message: '请选择调整类型', trigger: 'change' }
        ],
        adjustmentAmount: [
          { required: true, message: '请输入调整金额', trigger: 'blur' }
        ],
        adjustmentReason: [
          { required: true, message: '请输入调整原因', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.fetchBalanceData()
    this.fetchAdjustmentData()
    this.fetchAdjustmentTypes()
  },
  methods: {
    handleTabClick(tab) {
      if (tab.name === 'balance') {
        this.fetchBalanceData()
      } else {
        this.fetchAdjustmentData()
      }
    },
    async fetchBalanceData() {
      this.balanceLoading = true
      try {
        const { data } = await getBalanceList(this.balanceQueryForm)
        this.balanceList = data.tlist
        this.balanceTotal = data.totalRecord
      } catch (error) {
        this.$message.error('获取余额数据失败')
      }
      this.balanceLoading = false
    },
    async fetchAdjustmentData() {
      this.adjustmentLoading = true
      try {
        const { data } = await getBalanceAdjustmentList(this.adjustmentQueryForm)
        this.adjustmentList = data.tlist
        this.adjustmentTotal = data.totalRecord
      } catch (error) {
        this.$message.error('获取调整数据失败')
      }
      this.adjustmentLoading = false
    },
    async fetchAdjustmentTypes() {
      try {
        const { data } = await getBalanceAdjustmentTypes()
        this.adjustmentTypes = data
      } catch (error) {
        this.$message.error('获取调整类型失败')
      }
    },
    handleBalanceQuery() {
      this.balanceQueryForm.pageNo = 1
      this.fetchBalanceData()
    },
    resetBalanceQuery() {
      this.balanceQueryForm = {
        pageNo: 1,
        pageSize: 10,
        accountCode: '',
        accountName: '',
        period: '',
        currencyCode: ''
      }
      this.fetchBalanceData()
    },
    handleAdjustmentQuery() {
      this.adjustmentQueryForm.pageNo = 1
      this.fetchAdjustmentData()
    },
    resetAdjustmentQuery() {
      this.adjustmentQueryForm = {
        pageNo: 1,
        pageSize: 10,
        accountCode: '',
        adjustmentType: '',
        status: ''
      }
      this.fetchAdjustmentData()
    },
    handleBalanceSizeChange(val) {
      this.balanceQueryForm.pageSize = val
      this.fetchBalanceData()
    },
    handleBalanceCurrentChange(val) {
      this.balanceQueryForm.pageNo = val
      this.fetchBalanceData()
    },
    handleAdjustmentSizeChange(val) {
      this.adjustmentQueryForm.pageSize = val
      this.fetchAdjustmentData()
    },
    handleAdjustmentCurrentChange(val) {
      this.adjustmentQueryForm.pageNo = val
      this.fetchAdjustmentData()
    },
    handleAdjustmentSelectionChange(val) {
      this.adjustmentSelection = val
    },
    async handleRefreshBalance() {
      try {
        this.$confirm('确定要刷新余额吗？', '提示', {
          type: 'warning'
        })
        // 刷新余额逻辑
        this.$message.success('余额刷新任务已启动')
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('刷新余额失败')
        }
      }
    },
    handleExportBalance() {
      // 导出余额逻辑
    },
    handleValidateBalance() {
      // 数据校验逻辑
    },
    handleAddAdjustment() {
      this.adjustmentForm = {
        accountCode: '',
        accountName: '',
        period: '',
        currencyCode: 'CNY',
        adjustmentType: '',
        adjustmentAmount: 0,
        originalBeginningDebit: 0,
        originalBeginningCredit: 0,
        newBeginningDebit: 0,
        newBeginningCredit: 0,
        adjustmentReason: '',
        remark: ''
      }
      this.adjustmentDialogVisible = true
    },
    handleViewAdjustment(row) {
      // 查看调整详情逻辑
    },
    handleApproveAdjustment(row) {
      // 审批调整逻辑
    },
    handleCancelAdjustment(row) {
      // 撤销调整逻辑
    },
    handleBatchApprove() {
      // 批量审批逻辑
    },
    async handleSaveAdjustment() {
      this.$refs.adjustmentForm.validate(async (valid) => {
        if (valid) {
          try {
            await createBalanceAdjustment(this.adjustmentForm)
            this.$message.success('调整申请创建成功')
            this.adjustmentDialogVisible = false
            this.fetchAdjustmentData()
          } catch (error) {
            this.$message.error('创建调整申请失败')
          }
        }
      })
    },
    handleAdjustmentDialogClose() {
      this.$refs.adjustmentForm.resetFields()
    },
    goBack() {
      this.$router.back()
    },
    formatAmount(amount) {
      if (amount == null || amount === 0) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    getAdjustmentTypeTag(type) {
      const tagMap = {
        BEGINNING_ADJUST: 'warning',
        PERIOD_ADJUST: 'danger',
        OTHER: ''
      }
      return tagMap[type] || ''
    },
    getAdjustmentTypeName(type) {
      const item = this.adjustmentTypes.find(item => item.typeCode === type)
      return item ? item.typeName : type
    },
    getAdjustmentStatusTag(status) {
      const tagMap = {
        PENDING: 'warning',
        APPROVED: 'success',
        REJECTED: 'danger',
        CANCELLED: 'info'
      }
      return tagMap[status] || ''
    },
    getAdjustmentStatusName(status) {
      const nameMap = {
        PENDING: '待审批',
        APPROVED: '已通过',
        REJECTED: '已拒绝',
        CANCELLED: '已撤销'
      }
      return nameMap[status] || status
    }
  }
}
</script>

<style scoped>
.el-tabs {
  margin-top: 20px;
}
.el-table {
  margin-top: 20px;
}
.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>