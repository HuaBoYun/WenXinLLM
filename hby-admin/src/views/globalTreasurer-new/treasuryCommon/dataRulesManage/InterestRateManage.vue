<template>
  <div class="interest-rate-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-money"></i>
            利率管理
          </h2>
          <p class="page-description">管理各类金融产品利率配置，包括基准利率、浮动利率和历史利率数据</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增利率
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleSyncRates">
            同步利率
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 利率概览卡片 -->
    <div class="rate-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon benchmark-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">基准利率</div>
                <div class="card-value">{{ benchmarkRate }}%</div>
                <div class="card-change">央行基准</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon deposit-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-info">
                <div class="card-title">存款利率</div>
                <div class="card-value">{{ depositRate }}%</div>
                <div class="card-change positive">年化收益</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon loan-icon">
                <i class="el-icon-wallet"></i>
              </div>
              <div class="card-info">
                <div class="card-title">贷款利率</div>
                <div class="card-value">{{ loanRate }}%</div>
                <div class="card-change negative">年化成本</div>
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
                <div class="card-change">利率数据</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="listQuery" class="search-form">
        <el-row :gutter="16">
          <el-col :span="4">
            <el-form-item label="利率类型">
              <el-select v-model="listQuery.rateType" placeholder="请选择" clearable style="width: 100%;">
                <el-option label="存款" value="DEPOSIT" />
                <el-option label="贷款" value="LOAN" />
                <el-option label="同业" value="INTERBANK" />
                <el-option label="债券" value="BOND" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="币种">
              <el-select v-model="listQuery.currencyCode" placeholder="请选择" clearable style="width: 100%;">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
                <el-option label="英镑" value="GBP" />
                <el-option label="港币" value="HKD" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="期限">
              <el-input v-model="listQuery.term" placeholder="请输入期限" clearable style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="期限单位">
              <el-select v-model="listQuery.termUnit" placeholder="请选择" clearable style="width: 100%;">
                <el-option label="天" value="DAY" />
                <el-option label="月" value="MONTH" />
                <el-option label="年" value="YEAR" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="利率来源">
              <el-select v-model="listQuery.rateSource" placeholder="请选择" clearable style="width: 100%;">
                <el-option label="央行" value="PBOC" />
                <el-option label="银行" value="BANK" />
                <el-option label="市场" value="MARKET" />
                <el-option label="第三方" value="THIRD_PARTY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label=" " label-width="16px">
              <el-button type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
              <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      style="width: 100%;"
    >
      <el-table-column label="利率类型" prop="rateType" align="center" min-width="100">
        <template slot-scope="scope">
          <el-tag :type="getRateTypeColor(scope.row.rateType)">
            {{ getRateTypeName(scope.row.rateType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="币种" prop="currencyCode" align="center" min-width="80">
        <template slot-scope="scope">
          <span>{{ getCurrencyName(scope.row.currencyCode) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="期限" align="center" min-width="90">
        <template slot-scope="scope">
          <span>{{ scope.row.term }}{{ getTermUnitName(scope.row.termUnit) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="利率" align="center" min-width="100">
        <template slot-scope="scope">
          <span class="rate-value">{{ scope.row.interestRate }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="利率日期" align="center" min-width="110">
        <template slot-scope="scope">
          <span>{{ scope.row.rateDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="利率来源" align="center" min-width="100">
        <template slot-scope="scope">
          <span>{{ getRateSourceName(scope.row.rateSource) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" align="center" min-width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isActive === 1 ? 'success' : 'danger'">
            {{ scope.row.isActive === 1 ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row, scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      :title="dialogStatus === 'create' ? '新增利率' : '编辑利率'"
      :visible.sync="interestRateDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form ref="interestRateForm" :model="currentInterestRate" :rules="formRules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="利率类型" prop="rateType">
              <el-select v-model="currentInterestRate.rateType" placeholder="请选择利率类型" style="width: 100%;">
                <el-option label="存款" value="DEPOSIT" />
                <el-option label="贷款" value="LOAN" />
                <el-option label="同业" value="INTERBANK" />
                <el-option label="债券" value="BOND" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="currentInterestRate.currencyCode" placeholder="请选择币种" style="width: 100%;">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
                <el-option label="英镑" value="GBP" />
                <el-option label="港币" value="HKD" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="期限" prop="term">
              <el-input-number v-model="currentInterestRate.term" :min="0" :max="100" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="期限单位" prop="termUnit">
              <el-select v-model="currentInterestRate.termUnit" placeholder="请选择期限单位" style="width: 100%;">
                <el-option label="天" value="DAY" />
                <el-option label="月" value="MONTH" />
                <el-option label="年" value="YEAR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="利率(%)" prop="interestRate">
              <el-input-number v-model="currentInterestRate.interestRate" :precision="4" :step="0.01" :min="0" :max="100" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="利率日期" prop="rateDate">
              <el-date-picker v-model="currentInterestRate.rateDate" type="date" placeholder="选择日期" style="width: 100%;" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="利率来源" prop="rateSource">
              <el-select v-model="currentInterestRate.rateSource" placeholder="请选择利率来源" style="width: 100%;">
                <el-option label="央行" value="PBOC" />
                <el-option label="银行" value="BANK" />
                <el-option label="市场" value="MARKET" />
                <el-option label="第三方" value="THIRD_PARTY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="isActive">
              <el-switch v-model="currentInterestRate.isActive" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="停用" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="currentInterestRate.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="interestRateDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getInterestRateList,
  createInterestRate,
  updateInterestRate,
  deleteInterestRate,
  batchDeleteInterestRate,
  getInterestRateDetail,
  syncInterestRate,
  exportInterestRate,
  updateInterestRateStatus,
  getInterestRateStatistics
} from '@/api/globalTreasurer/xjgl/dataRulesManage/rate'

export default {
  name: 'InterestRateManage',
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
      list: [],
      total: 0,
      listLoading: true,
      submitLoading: false,
      listQuery: {
        page: 1,
        limit: 20,
        rateType: undefined,
        currencyCode: undefined,
        term: undefined,
        termUnit: undefined,
        rateSource: undefined
      },
      totalRates: 0,
      todayRates: 0,
      activeRates: 0,
      updateCount: 0,
      interestRateDialogVisible: false,
      dialogStatus: 'create',
      currentInterestRate: {
        rateType: '',
        currencyCode: 'CNY',
        term: 1,
        termUnit: 'YEAR',
        interestRate: 0,
        rateDate: '',
        rateSource: 'PBOC',
        isActive: 1,
        remark: ''
      },
      formRules: {
        rateType: [{ required: true, message: '请选择利率类型', trigger: 'change' }],
        currencyCode: [{ required: true, message: '请选择币种', trigger: 'change' }],
        term: [{ required: true, message: '请输入期限', trigger: 'blur' }],
        termUnit: [{ required: true, message: '请选择期限单位', trigger: 'change' }],
        interestRate: [{ required: true, message: '请输入利率', trigger: 'blur' }],
        rateDate: [{ required: true, message: '请选择利率日期', trigger: 'change' }],
        rateSource: [{ required: true, message: '请选择利率来源', trigger: 'change' }]
      },
      benchmarkRate: '2.75',
      depositRate: '2.50',
      loanRate: '4.35',
      lastUpdateTime: '09:30'
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getRateTypeName(type) {
      const typeMap = {
        'DEPOSIT': '存款',
        'LOAN': '贷款',
        'INTERBANK': '同业',
        'BOND': '债券'
      }
      return typeMap[type] || type
    },
    getRateTypeColor(type) {
      const colorMap = {
        'DEPOSIT': 'success',
        'LOAN': 'warning',
        'INTERBANK': 'info',
        'BOND': 'primary'
      }
      return colorMap[type] || 'default'
    },
    getTermUnitName(unit) {
      const unitMap = {
        'DAY': '天',
        'MONTH': '月',
        'YEAR': '年'
      }
      return unitMap[unit] || unit
    },
    getCurrencyName(code) {
      const currencyMap = {
        'CNY': '人民币',
        'USD': '美元',
        'EUR': '欧元',
        'JPY': '日元',
        'GBP': '英镑',
        'HKD': '港币'
      }
      return currencyMap[code] || code
    },
    getRateSourceName(source) {
      const sourceMap = {
        'PBOC': '央行',
        'BANK': '银行',
        'MARKET': '市场',
        'THIRD_PARTY': '第三方'
      }
      return sourceMap[source] || source
    },
      async fetchData() {
      this.listLoading = true
      try {
        // 构建请求参数，后端用 @RequestParam 接收，page/pageSize 为分页，其余为筛选
        const query = {
          page: this.listQuery.page,
          pageSize: this.listQuery.limit
        }
        if (this.listQuery.rateType) query.rateType = this.listQuery.rateType
        if (this.listQuery.currencyCode) query.currencyCode = this.listQuery.currencyCode
        if (this.listQuery.term !== undefined && this.listQuery.term !== null && this.listQuery.term !== '') {
          query.term = this.listQuery.term
        }
        if (this.listQuery.termUnit) query.termUnit = this.listQuery.termUnit
        if (this.listQuery.rateSource) query.rateSource = this.listQuery.rateSource

        const response = await getInterestRateList(query)

        if (response.code === 1) {
          const rawData = response.data || {}
          this.list = rawData.tlist || rawData.list || []
          this.total = Number(rawData.totalRecord || rawData.total) || this.list.length
        } else {
          this.$message.error(response.msg || response.message || '获取数据失败')
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取利率数据列表失败:', error)
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }

      // 获取统计信息
      this.fetchStatistics()
    },
    /**
     * 前端过滤：根据搜索条件对列表数据进行二次筛选
     * 当后端未处理筛选参数时作为兜底
     */
    applyFrontendFilter(list) {
      const { rateType, currencyCode, term, termUnit, rateSource } = this.listQuery
      return list.filter(item => {
        if (rateType && item.rateType !== rateType) return false
        if (currencyCode && item.currencyCode !== currencyCode) return false
        if (term !== undefined && term !== null && term !== '' && String(item.term) !== String(term)) return false
        if (termUnit && item.termUnit !== termUnit) return false
        if (rateSource && item.rateSource !== rateSource) return false
        return true
      })
    },

    /**
     * 获取利率统计信息
     */
    async fetchStatistics() {
      try {
        const response = await getInterestRateStatistics()
        const successCodes = [200, '200', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          const stats = response.data
          this.benchmarkRate = stats.benchmarkRate || '2.75'
          this.depositRate = stats.depositRate || '2.50'
          this.loanRate = stats.loanRate || '4.35'
          this.lastUpdateTime = stats.lastUpdateTime || new Date().toLocaleTimeString()
        }
      } catch (error) {
        console.error('获取统计信息失败:', error)
        // 使用默认值
        this.benchmarkRate = '2.75'
        this.depositRate = '2.50'
        this.loanRate = '4.35'
        this.lastUpdateTime = new Date().toLocaleTimeString()
      }
    },

    getList(paginationPayload) {
      if (paginationPayload) {
        this.listQuery.page = paginationPayload.page
        this.listQuery.limit = paginationPayload.limit
      }
      this.fetchData()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        rateType: undefined,
        currencyCode: undefined,
        term: undefined,
        termUnit: undefined,
        rateSource: undefined
      }
      this.getList()
    },
    handleCreate() {
      this.dialogStatus = 'create'
      this.currentInterestRate = {
        rateType: '',
        currencyCode: 'CNY',
        term: 1,
        termUnit: 'YEAR',
        interestRate: 0,
        rateDate: '',
        rateSource: 'PBOC',
        isActive: 1,
        remark: ''
      }
      this.interestRateDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.interestRateForm) {
          this.$refs.interestRateForm.clearValidate()
        }
      })
    },
    handleUpdate(row) {
      this.dialogStatus = 'edit'
      this.currentInterestRate = { ...row }
      this.interestRateDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.interestRateForm) {
          this.$refs.interestRateForm.clearValidate()
        }
      })
    },
    async handleSubmit() {
      this.$refs.interestRateForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            let response
            if (this.dialogStatus === 'create') {
              response = await createInterestRate(this.currentInterestRate)
            } else {
              response = await updateInterestRate(this.currentInterestRate)
            }
            const successCodes = [200, '200', '1', 1, 2]
            if (successCodes.includes(response.code)) {
              this.$notify({
                title: '成功',
                message: this.dialogStatus === 'create' ? '新增成功' : '更新成功',
                type: 'success',
                duration: 2000
              })
              this.interestRateDialogVisible = false
              this.fetchData()
            } else {
              this.$message.error(response.msg || response.message || (this.dialogStatus === 'create' ? '新增失败' : '更新失败'))
            }
          } catch (error) {
            console.error('保存利率数据失败:', error)
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    async handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteInterestRate({ rateId: row.rateId })
          const successCodes = [200, '200', '1', 1, 2]
          if (successCodes.includes(response.code)) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            // 重新获取数据
            this.fetchData()
          } else {
            this.$message.error(response.msg || response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除利率数据失败:', error)
        }
      })
    },
    async handleSyncRates() {
      const loading = this.$loading({
        lock: true,
        text: '正在同步利率数据...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      try {
        const response = await syncInterestRate({ sourceType: 'PBOC' })
        const successCodes = [200, '200', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.lastUpdateTime = new Date().toLocaleTimeString()
          this.getList()
          this.$message.success('利率同步成功')
        } else {
          this.$message.error(response.msg || response.message || '同步失败')
        }
      } catch (error) {
        console.error('同步利率数据失败:', error)
      } finally {
        loading.close()
      }
    },
    handleExport() {
      this.$message.success('导出成功')
      // 模拟文件下载
      const dataStr = JSON.stringify(this.list, null, 2)
      const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr)
      const exportFileDefaultName = `利率管理数据_${new Date().toISOString().split('T')[0]}.json`
      const linkElement = document.createElement('a')
      linkElement.setAttribute('href', dataUri)
      linkElement.setAttribute('download', exportFileDefaultName)
      linkElement.click()
    }
  }
}
</script>

<style lang="scss" scoped>
.interest-rate-manage {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        .page-title {
          font-size: 20px;
          font-weight: 600;
          color: #303133;
          margin: 0 0 8px 0;

          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }

        .page-description {
          font-size: 14px;
          color: #909399;
          margin: 0;
        }
      }
    }
  }

  .rate-overview {
    margin-bottom: 20px;

    .overview-card {
      border-radius: 8px;

      .card-content {
        display: flex;
        align-items: center;

        .card-icon {
          width: 56px;
          height: 56px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;

          i {
            font-size: 24px;
            color: #fff;
          }

          &.benchmark-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.deposit-icon {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
          }

          &.loan-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &.update-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }
        }

        .card-info {
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 4px;
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
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;

    .search-form {
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }

  .rate-value {
    font-weight: 600;
    color: #409EFF;
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>
<style lang="scss" scoped>
.interest-rate-manage {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        .page-title {
          font-size: 20px;
          font-weight: 600;
          color: #303133;
          margin: 0 0 8px 0;

          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }

        .page-description {
          font-size: 14px;
          color: #909399;
          margin: 0;
        }
      }
    }
  }

  .rate-overview {
    margin-bottom: 20px;

    .overview-card {
      border-radius: 8px;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }

      .card-content {
        display: flex;
        align-items: center;

        .card-icon {
          width: 56px;
          height: 56px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;

          i {
            font-size: 24px;
            color: #fff;
          }

          &.benchmark-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.deposit-icon {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
          }

          &.loan-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &.update-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }
        }

        .card-info {
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 4px;
          }

          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
          }

          .card-change {
            font-size: 12px;
            color: #909399;
            margin-top: 4px;

            &.positive {
              color: #67C23A;
            }

            &.negative {
              color: #F56C6C;
            }
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;

    .search-form {
      .el-form-item {
        display: flex;
        flex-direction: column;
        margin-bottom: 0;
        width: 100%;

        ::v-deep .el-form-item__label {
          text-align: left;
          line-height: 28px;
          padding: 0;
        }

        ::v-deep .el-form-item__content {
          margin-left: 0 !important;
          width: 100%;
        }
      }
    }
  }

  .rate-value {
    font-weight: 600;
    color: #409EFF;
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>
