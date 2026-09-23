<template>
  <div class="transaction-type-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-s-finance"></i>
            交易类型管理
          </h2>
          <p class="page-description">管理金融交易类型定义，包括交易分类、交易规则和业务流程配置</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增交易类型
          </el-button>
          <el-button type="success" icon="el-icon-sort" @click="handleSort">
            排序管理
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 交易类型统计卡片 -->
    <div class="transaction-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-s-finance"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总交易类型</div>
                <div class="card-value">{{ totalTransactionTypes }}</div>
                <div class="card-change">已配置类型</div>
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
                <div class="card-title">启用类型</div>
                <div class="card-value">{{ activeTransactionTypes }}</div>
                <div class="card-change positive">正常使用</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon category-icon">
                <i class="el-icon-menu"></i>
              </div>
              <div class="card-info">
                <div class="card-title">交易分类</div>
                <div class="card-value">{{ transactionCategories }}</div>
                <div class="card-change">分类数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon usage-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">今日交易</div>
                <div class="card-value">{{ todayTransactions }}</div>
                <div class="card-change">交易笔数</div>
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
          <el-form-item label="交易类型编码">
            <el-input
              v-model="listQuery.transactionTypeCode"
              placeholder="请输入交易类型编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="交易类型名称">
            <el-input
              v-model="listQuery.transactionTypeName"
              placeholder="请输入交易类型名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="交易分类">
            <el-select
              v-model="listQuery.transactionCategory"
              placeholder="请选择交易分类"
              clearable
              style="width: 150px;"
            >
              <el-option label="投资申购" value="INVESTMENT_PURCHASE" />
              <el-option label="投资赎回" value="INVESTMENT_REDEMPTION" />
              <el-option label="资金划转" value="FUND_TRANSFER" />
              <el-option label="利息收入" value="INTEREST_INCOME" />
              <el-option label="本金回收" value="PRINCIPAL_RECOVERY" />
              <el-option label="费用支出" value="FEE_EXPENSE" />
            </el-select>
          </el-form-item>
          <el-form-item label="交易方向">
            <el-select
              v-model="listQuery.transactionDirection"
              placeholder="请选择交易方向"
              clearable
              style="width: 120px;"
            >
              <el-option label="收入" value="INCOME" />
              <el-option label="支出" value="EXPENSE" />
              <el-option label="双向" value="BOTH" />
            </el-select>
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
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
      <el-table-column label="交易类型编码" prop="transactionTypeCode" sortable="custom" align="center" min-width="140">
        <template slot-scope="{row}">
          <span>{{ row.transactionTypeCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交易类型名称" min-width="150" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.transactionTypeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交易分类" min-width="120" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getTransactionCategoryColor(row.transactionCategory)" size="small">
            {{ getTransactionCategoryName(row.transactionCategory) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="交易方向" min-width="90" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getTransactionDirectionColor(row.transactionDirection)" size="small">
            {{ getTransactionDirectionName(row.transactionDirection) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" min-width="90" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getRiskLevelColor(row.riskLevel)" size="small">
            {{ getRiskLevelName(row.riskLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="会计科目" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.accountingSubject || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审批要求" min-width="90" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.requiresApproval === '1' ? 'warning' : 'success'" size="small">
            {{ row.requiresApproval === '1' ? '需审批' : '无需审批' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="限额控制" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ formatCurrency(row.limitAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" min-width="80" align="center">
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
    <el-dialog :title="dialogStatus === 'create' ? '新增交易类型' : '编辑交易类型'" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-width="120px">
        <el-form-item label="交易类型编码" prop="transactionTypeCode">
          <el-input v-model="temp.transactionTypeCode" placeholder="请输入交易类型编码" :disabled="dialogStatus === 'update'" />
        </el-form-item>
        <el-form-item label="交易类型名称" prop="transactionTypeName">
          <el-input v-model="temp.transactionTypeName" placeholder="请输入交易类型名称" />
        </el-form-item>
        <el-form-item label="交易分类" prop="transactionCategory">
          <el-select v-model="temp.transactionCategory" placeholder="请选择交易分类" style="width: 100%;">
            <el-option label="投资申购" value="INVESTMENT_PURCHASE" />
            <el-option label="投资赎回" value="INVESTMENT_REDEMPTION" />
            <el-option label="资金划转" value="FUND_TRANSFER" />
            <el-option label="利息收入" value="INTEREST_INCOME" />
            <el-option label="本金回收" value="PRINCIPAL_RECOVERY" />
            <el-option label="费用支出" value="FEE_EXPENSE" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易方向" prop="transactionDirection">
          <el-select v-model="temp.transactionDirection" placeholder="请选择交易方向" style="width: 100%;">
            <el-option label="收入" value="INCOME" />
            <el-option label="支出" value="EXPENSE" />
            <el-option label="双向" value="BOTH" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="temp.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
            <el-option label="低风险" value="LOW" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="高风险" value="HIGH" />
          </el-select>
        </el-form-item>
        <el-form-item label="会计科目">
          <el-input v-model="temp.accountingSubject" placeholder="请输入会计科目" />
        </el-form-item>
        <el-form-item label="审批要求">
          <el-switch v-model="temp.requiresApproval" active-value="1" inactive-value="0" active-text="需审批" inactive-text="无需审批" />
        </el-form-item>
        <el-form-item label="限额控制">
          <el-input-number v-model="temp.limitAmount" :min="0" :precision="2" :step="1000" style="width: 100%;" placeholder="请输入限额金额" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="temp.sortOrder" :min="0" :max="999" style="width: 100%;" placeholder="请输入排序号" />
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

    <!-- 排序管理对话框 -->
    <el-dialog title="排序管理" :visible.sync="sortDialogVisible" width="600px">
      <div class="sort-container">
        <draggable v-model="sortList" @end="onSortEnd" handle="drag-handle">
          <transition-group type="transition" name="flip-list">
            <div v-for="(item, index) in sortList" :key="item.transactionTypeId" class="sort-item">
              <i class="el-icon-rank drag-handle"></i>
              <span class="sort-index">{{ index + 1 }}</span>
              <span class="sort-name">{{ item.transactionTypeName }}</span>
              <span class="sort-code">{{ item.transactionTypeCode }}</span>
            </div>
          </transition-group>
        </draggable>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="sortDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="saveSort()">保存排序</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import draggable from 'vuedraggable'
import {
  getTransactionTypeList,
  deleteTransactionType,
  getTransactionTypeStatistics,
  createTransactionType,
  updateTransactionType,
  sortTransactionTypes
} from '@/api/globalTreasurer/financialProductDefinition/transactionTypeManage'

export default {
  name: 'TransactionTypeManage',
  components: { Pagination, draggable },
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
        transactionTypeCode: undefined,
        transactionTypeName: undefined,
        transactionCategory: undefined,
        transactionDirection: undefined,
        riskLevel: undefined,
        isEnabled: undefined
      },
      totalTransactionTypes: 0,
      activeTransactionTypes: 0,
      transactionCategories: 0,
      todayTransactions: 0,
      // 弹窗相关
      dialogFormVisible: false,
      dialogStatus: '',
      submitLoading: false,
      temp: {
        transactionTypeId: undefined,
        transactionTypeCode: '',
        transactionTypeName: '',
        transactionCategory: 'INVESTMENT_PURCHASE',
        transactionDirection: 'INCOME',
        riskLevel: 'LOW',
        accountingSubject: '',
        requiresApproval: '0',
        limitAmount: 0,
        sortOrder: 0,
        isEnabled: 1,
        description: ''
      },
      rules: {
        transactionTypeCode: [{ required: true, message: '请输入交易类型编码', trigger: 'blur' }],
        transactionTypeName: [{ required: true, message: '请输入交易类型名称', trigger: 'blur' }],
        transactionCategory: [{ required: true, message: '请选择交易分类', trigger: 'change' }],
        transactionDirection: [{ required: true, message: '请选择交易方向', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }]
      },
      // 排序相关
      sortDialogVisible: false,
      sortList: []
    }
  },
  created() {
    this.getList()
    this.getStatistics()
  },
  methods: {
    /**
     * 获取交易类型列表
     */
    getList() {
      this.listLoading = true
      const params = {
        pageNo: this.listQuery.pageNo,
        pageSize: this.listQuery.pageSize,
        transactionTypeCode: this.listQuery.transactionTypeCode,
        transactionTypeName: this.listQuery.transactionTypeName,
        transactionCategory: this.listQuery.transactionCategory,
        transactionDirection: this.listQuery.transactionDirection,
        riskLevel: this.listQuery.riskLevel,
        isEnabled: this.listQuery.isEnabled
      }

      getTransactionTypeList(params).then(response => {
        if (response.code === 1) {
          this.list = response.data.tlist
          this.total = response.data.totalRecord
        } else {
          this.$message.error(response.message || '获取交易类型列表失败')
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取交易类型列表失败:', error)
        this.$message.error('获取交易类型列表失败')
        this.listLoading = false
      })
    },

    /**
     * 获取统计数据
     */
    getStatistics() {
      getTransactionTypeStatistics().then(response => {
        if (response.code === 1) {
          this.totalTransactionTypes = response.data.total || 0
          this.activeTransactionTypes = response.data.enabled || 0
          this.transactionCategories = response.data.categories || 0
          this.todayTransactions = response.data.today || 0
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
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
        transactionTypeCode: undefined,
        transactionTypeName: undefined,
        transactionCategory: undefined,
        transactionDirection: undefined,
        riskLevel: undefined,
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
        transactionTypeId: undefined,
        transactionTypeCode: '',
        transactionTypeName: '',
        transactionCategory: 'INVESTMENT_PURCHASE',
        transactionDirection: 'INCOME',
        riskLevel: 'LOW',
        accountingSubject: '',
        requiresApproval: '0',
        limitAmount: 0,
        sortOrder: 0,
        isEnabled: 1,
        description: ''
      }
    },

    /**
     * 排序管理
     */
    handleSort() {
      this.sortList = JSON.parse(JSON.stringify(this.list))
      this.sortDialogVisible = true
    },

    /**
     * 排序结束
     */
    onSortEnd() {
      console.log('排序结束:', this.sortList)
    },

    /**
     * 保存排序
     */
    saveSort() {
      try {
        this.submitLoading = true
        const sortData = this.sortList.map((item, index) => ({
          transactionTypeId: item.transactionTypeId,
          sortOrder: index
        }))

        sortTransactionTypes(sortData).then(response => {
          if (response.code === 1) {
            this.$message.success('排序保存成功')
            this.sortDialogVisible = false
            this.getList()
          } else {
            this.$message.error(response.message || '排序保存失败')
          }
        }).catch(error => {
          console.error('排序保存失败:', error)
          this.$message.error('排序保存失败')
        }).finally(() => {
          this.submitLoading = false
        })
      } catch (error) {
        console.error('排序保存失败:', error)
        this.$message.error('排序保存失败')
        this.submitLoading = false
      }
    },

    /**
     * 导出配置
     */
    handleExport() {
      try {
        const data = this.list.map(item => ({
          交易类型编码: item.transactionTypeCode || '',
          交易类型名称: item.transactionTypeName || '',
          交易分类: this.getTransactionCategoryName(item.transactionCategory) || '',
          交易方向: this.getTransactionDirectionName(item.transactionDirection) || '',
          风险等级: this.getRiskLevelName(item.riskLevel) || '',
          会计科目: item.accountingSubject || '',
          审批要求: item.requiresApproval === '1' ? '需要' : '不需要',
          限额控制: item.limitAmount || 0,
          描述: item.description || '',
          是否启用: item.isEnabled === 1 ? '是' : '否'
        }))
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `交易类型数据_${new Date().getTime()}.json`
        link.click()
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    /**
     * 编辑
     */
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      // 确保 limitAmount 是数字
      if (this.temp.limitAmount === null || this.temp.limitAmount === undefined) {
        this.temp.limitAmount = 0
      }
      // 确保 sortOrder 是数字
      if (this.temp.sortOrder === null || this.temp.sortOrder === undefined) {
        this.temp.sortOrder = 0
      }
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
            const response = await createTransactionType(this.temp)
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
            const response = await updateTransactionType(this.temp)
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

    /**
     * 删除
     */
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTransactionType(row.transactionTypeId).then(response => {
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
          this.$message.error('删除失败')
        })
      })
    },

    /**
     * 获取交易分类名称
     */
    getTransactionCategoryName(category) {
      const categoryMap = {
        'INVESTMENT_PURCHASE': '投资申购',
        'INVESTMENT_REDEMPTION': '投资赎回',
        'FUND_TRANSFER': '资金划转',
        'INTEREST_INCOME': '利息收入',
        'PRINCIPAL_RECOVERY': '本金回收',
        'FEE_EXPENSE': '费用支出'
      }
      return categoryMap[category] || category || '-'
    },

    /**
     * 获取交易分类颜色
     */
    getTransactionCategoryColor(category) {
      const colorMap = {
        'INVESTMENT_PURCHASE': 'primary',
        'INVESTMENT_REDEMPTION': 'warning',
        'FUND_TRANSFER': 'info',
        'INTEREST_INCOME': 'success',
        'PRINCIPAL_RECOVERY': 'success',
        'FEE_EXPENSE': 'danger'
      }
      return colorMap[category] || 'default'
    },

    /**
     * 获取交易方向名称
     */
    getTransactionDirectionName(direction) {
      const directionMap = {
        'INCOME': '收入',
        'EXPENSE': '支出',
        'BOTH': '双向'
      }
      return directionMap[direction] || direction || '-'
    },

    /**
     * 获取交易方向颜色
     */
    getTransactionDirectionColor(direction) {
      const colorMap = {
        'INCOME': 'success',
        'EXPENSE': 'danger',
        'BOTH': 'info'
      }
      return colorMap[direction] || 'default'
    },

    /**
     * 获取风险等级名称
     */
    getRiskLevelName(level) {
      const levelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      }
      return levelMap[level] || level || '-'
    },

    /**
     * 获取风险等级颜色
     */
    getRiskLevelColor(level) {
      const colorMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return colorMap[level] || 'default'
    },

    /**
     * 格式化货币
     */
    formatCurrency(value) {
      if (value === null || value === undefined || value === '') {
        return '0.00'
      }
      return parseFloat(value).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.transaction-type-manage {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  flex: 1;
}

.page-title {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 500;
  color: #303133;

  i {
    margin-right: 10px;
    color: #409eff;
  }
}

.page-description {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.transaction-overview {
  margin-bottom: 20px;
}

.overview-card {
  border-radius: 4px;
  overflow: hidden;
}

.card-content {
  display: flex;
  align-items: center;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  margin-right: 16px;
}

.total-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.active-icon {
  background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
  color: #fff;
}

.category-icon {
  background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%);
  color: #fff;
}

.usage-icon {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 99%, #fecfef 100%);
  color: #fff;
}

.card-info {
  flex: 1;
}

.card-title {
  color: #909399;
  font-size: 14px;
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
}

.search-card {
  margin-bottom: 20px;
}

.sort-container {
  max-height: 400px;
  overflow-y: auto;
}

.sort-item {
  display: flex;
  align-items: center;
  padding: 12px;
  background: #f5f7fa;
  margin-bottom: 8px;
  border-radius: 4px;
  cursor: move;

  &:hover {
    background: #e6f7ff;
  }
}

.drag-handle {
  margin-right: 12px;
  cursor: move;
  color: #909399;
}

.sort-index {
  width: 30px;
  height: 30px;
  background: #409eff;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 500;
  margin-right: 12px;
}

.sort-name {
  flex: 1;
  font-size: 14px;
  color: #303133;
}

.sort-code {
  color: #909399;
  font-size: 12px;
}

.flip-list-move {
  transition: transform 0.5s;
}

.flip-list-enter-active,
.flip-list-leave-active {
  transition: all 0.5s;
}

.flip-list-enter,
.flip-list-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
