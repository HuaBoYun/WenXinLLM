<template>
  <div class="market-data-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-data-line"></i>
            市场数据管理
          </h2>
          <p class="page-description">管理各类金融市场数据，包括汇率、利率、债券收益率、股价和大宗商品价格</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增数据
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleSyncData">
            同步数据
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 市场数据概览卡片 -->
    <div class="data-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon exchange-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">汇率数据</div>
                <div class="card-value">{{ exchangeRateCount }}</div>
                <div class="card-change">条记录</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon interest-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-info">
                <div class="card-title">利率数据</div>
                <div class="card-value">{{ interestRateCount }}</div>
                <div class="card-change">条记录</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon bond-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">债券收益率</div>
                <div class="card-value">{{ bondYieldCount }}</div>
                <div class="card-change">条记录</div>
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
                <div class="card-change">数据同步</div>
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
          <el-form-item label="数据类型">
            <el-select
              v-model="listQuery.dataType"
              placeholder="请选择数据类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="汇率" value="EXCHANGE_RATE" />
              <el-option label="利率" value="INTEREST_RATE" />
              <el-option label="债券收益率" value="BOND_YIELD" />
              <el-option label="股价" value="STOCK_PRICE" />
              <el-option label="大宗商品" value="COMMODITY" />
            </el-select>
          </el-form-item>
          <el-form-item label="数据代码">
            <el-input
              v-model="listQuery.symbol"
              placeholder="请输入数据代码"
              style="width: 150px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="数据来源">
            <el-select
              v-model="listQuery.dataSource"
              placeholder="请选择数据来源"
              clearable
              style="width: 150px;"
            >
              <el-option label="央行" value="PBOC" />
              <el-option label="银行间市场" value="CIBM" />
              <el-option label="上交所" value="SSE" />
              <el-option label="深交所" value="SZSE" />
              <el-option label="彭博" value="BLOOMBERG" />
              <el-option label="路透" value="REUTERS" />
            </el-select>
          </el-form-item>
          <el-form-item label="数据日期">
            <el-date-picker
              v-model="listQuery.dataDate"
              type="date"
              placeholder="选择日期"
              style="width: 150px;"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.isActive"
              placeholder="请选择状态"
              clearable
              style="width: 100px;"
            >
              <el-option label="有效" :value="1" />
              <el-option label="无效" :value="0" />
            </el-select>
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

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="listLoading"
        :data="list"
        border
        style="width: 100%;"
        @sort-change="sortChange"
      >
        <el-table-column label="ID" prop="dataId" sortable="custom" align="center" min-width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.dataId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据类型" align="center" min-width="100">
          <template slot-scope="scope">
            <el-tag :type="getDataTypeTagType(scope.row.dataType)">
              {{ getDataTypeText(scope.row.dataType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="数据代码" align="center" min-width="110">
          <template slot-scope="scope">
            <span class="link-type" @click="handleUpdate(scope.row)">{{ scope.row.symbol }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据名称" min-width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.symbolName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据值" align="center" min-width="110">
          <template slot-scope="scope">
            <span class="data-value">{{ formatDataValue(scope.row.dataValue, scope.row.dataType) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据日期" align="center" min-width="110">
          <template slot-scope="scope">
            <span>{{ formatDate(scope.row.dataDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据来源" align="center" min-width="100">
          <template slot-scope="scope">
            <el-tag size="mini">{{ getDataSourceText(scope.row.dataSource) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" class-name="status-col" align="center" min-width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isActive | statusFilter">
              {{ scope.row.isActive ? '有效' : '无效' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" min-width="150">
          <template slot-scope="scope">
            <span>{{ formatDate(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width" fixed="right">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button v-if="scope.row.isActive" size="mini" type="warning" @click="handleModifyStatus(scope.row,'disable')">禁用</el-button>
            <el-button v-else size="mini" type="success" @click="handleModifyStatus(scope.row,'enable')">启用</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row,scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据类型" prop="dataType">
              <el-select v-model="temp.dataType" placeholder="请选择数据类型" style="width: 100%;">
                <el-option label="汇率" value="EXCHANGE_RATE" />
                <el-option label="利率" value="INTEREST_RATE" />
                <el-option label="债券收益率" value="BOND_YIELD" />
                <el-option label="股价" value="STOCK_PRICE" />
                <el-option label="大宗商品" value="COMMODITY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据代码" prop="symbol">
              <el-input v-model="temp.symbol" placeholder="请输入数据代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据名称" prop="symbolName">
              <el-input v-model="temp.symbolName" placeholder="请输入数据名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据值" prop="dataValue">
              <el-input-number v-model="temp.dataValue" :precision="6" :step="0.000001" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据日期" prop="dataDate">
              <el-date-picker v-model="temp.dataDate" type="date" placeholder="选择日期" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据来源" prop="dataSource">
              <el-select v-model="temp.dataSource" placeholder="请选择数据来源" style="width: 100%;">
                <el-option label="央行" value="PBOC" />
                <el-option label="银行间市场" value="CIBM" />
                <el-option label="上交所" value="SSE" />
                <el-option label="深交所" value="SZSE" />
                <el-option label="彭博" value="BLOOMBERG" />
                <el-option label="路透" value="REUTERS" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态">
          <el-radio-group v-model="temp.isActive">
            <el-radio :label="1">有效</el-radio>
            <el-radio :label="0">无效</el-radio>
          </el-radio-group>
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
  </div>
</template>

<script>
import { getMarketDataPage, createMarketData, updateMarketData, deleteMarketData, importMarketData, exportMarketData, syncMarketData, getMarketDataStatistics } from '@/api/globalTreasurer/treasuryCommon'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'MarketDataManage',
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
      listQuery: {
        page: 1,
        limit: 20,
        dataType: undefined,
        symbol: undefined,
        dataSource: undefined,
        dataDate: undefined,
        isActive: undefined
      },
      exchangeRateCount: 0,
      interestRateCount: 0,
      bondYieldCount: 0,
      lastUpdateTime: '',
      temp: {
        dataId: undefined,
        dataType: '',
        symbol: '',
        symbolName: '',
        dataValue: 0,
        dataDate: '',
        dataSource: '',
        isActive: 1
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑市场数据',
        create: '新增市场数据'
      },
      rules: {
        dataType: [{ required: true, message: '数据类型不能为空', trigger: 'change' }],
        symbol: [{ required: true, message: '数据代码不能为空', trigger: 'blur' }],
        symbolName: [{ required: true, message: '数据名称不能为空', trigger: 'blur' }],
        dataValue: [{ required: true, message: '数据值不能为空', trigger: 'blur' }],
        dataDate: [{ required: true, message: '数据日期不能为空', trigger: 'change' }],
        dataSource: [{ required: true, message: '数据来源不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.updateStatistics()
  },
  methods: {
    async getList(paginationPayload) {
      if (paginationPayload) {
        this.listQuery.page = paginationPayload.page
        this.listQuery.limit = paginationPayload.limit
      }
      this.listLoading = true
      try {
        const params = Object.assign({}, this.listQuery)
        if (params.dataDate instanceof Date) {
          params.dataDate = this.formatDate(params.dataDate)
        }
        const response = await getMarketDataPage(params)
        if (response && response.code === 1) {
          const rawData = response.data || {}
          this.list = rawData.tlist || rawData.list || []
          this.total = Number(rawData.totalRecord || rawData.total) || this.list.length
        } else {
          this.$message.error(response?.msg || '获取数据失败')
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取市场数据失败:', error)
        this.$message.error('获取市场数据失败: ' + (error.message || '网络错误'))
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },
    async updateStatistics() {
      try {
        const response = await getMarketDataStatistics()
        if (response && [200, 1, '200', '1'].includes(response.code)) {
          const data = response.data || {}
          this.exchangeRateCount = data.exchangeRateCount || 0
          this.interestRateCount = data.interestRateCount || 0
          this.bondYieldCount = data.bondYieldCount || 0
          if (data.lastUpdateTime) {
            const date = new Date(data.lastUpdateTime)
            this.lastUpdateTime = date.getHours().toString().padStart(2, '0') + ':' + date.getMinutes().toString().padStart(2, '0')
          } else {
            this.lastUpdateTime = '--:--'
          }
        }
      } catch (error) {
        console.error('获取市场数据统计失败:', error)
        this.exchangeRateCount = 0
        this.interestRateCount = 0
        this.bondYieldCount = 0
        this.lastUpdateTime = '--:--'
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        dataType: undefined,
        symbol: undefined,
        dataSource: undefined,
        dataDate: undefined,
        isActive: undefined
      }
      this.getList()
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
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row, index) {
      this.$confirm('确认删除该市场数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const response = await deleteMarketData(row.dataId)
          if (response && [200, 1, '200', '1'].includes(response.code)) {
            this.$message({ type: 'success', message: '删除成功!' })
            this.getList()
            this.updateStatistics()
          } else {
            this.$message.error(response?.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除市场数据失败:', error)
          this.$message.error('删除失败: ' + (error.message || '网络错误'))
        }
      })
    },
    handleModifyStatus(row, status) {
      const message = status === 'enable' ? '启用' : '禁用'
      this.$confirm(`确认${message}该市场数据?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const data = Object.assign({}, row, { isActive: status === 'enable' ? 1 : 0 })
          const response = await updateMarketData(data)
          if (response && [200, 1, '200', '1'].includes(response.code)) {
            this.$message({ type: 'success', message: `${message}成功!` })
            this.getList()
          } else {
            this.$message.error(response?.msg || `${message}失败`)
          }
        } catch (error) {
          console.error(`${message}市场数据失败:`, error)
          this.$message.error(`${message}失败: ` + (error.message || '网络错误'))
        }
      })
    },
    createData() {
      this.$refs['dataForm'].validate(async(valid) => {
        if (valid) {
          try {
            const data = Object.assign({}, this.temp)
            if (data.dataDate instanceof Date) {
              data.dataDate = data.dataDate.toISOString().split('T')[0]
            }
            const response = await createMarketData(data)
            if (response && [200, 1, '200', '1'].includes(response.code)) {
              this.dialogFormVisible = false
              this.$message({ type: 'success', message: '创建成功' })
              this.getList()
              this.updateStatistics()
            } else {
              this.$message.error(response?.msg || '创建失败')
            }
          } catch (error) {
            console.error('创建市场数据失败:', error)
            this.$message.error('创建失败: ' + (error.message || '网络错误'))
          }
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(async(valid) => {
        if (valid) {
          try {
            const data = Object.assign({}, this.temp)
            if (data.dataDate instanceof Date) {
              data.dataDate = data.dataDate.toISOString().split('T')[0]
            }
            const response = await updateMarketData(data)
            if (response && [200, 1, '200', '1'].includes(response.code)) {
              this.dialogFormVisible = false
              this.$message({ type: 'success', message: '更新成功' })
              this.getList()
            } else {
              this.$message.error(response?.msg || '更新失败')
            }
          } catch (error) {
            console.error('更新市场数据失败:', error)
            this.$message.error('更新失败: ' + (error.message || '网络错误'))
          }
        }
      })
    },
    resetTemp() {
      this.temp = {
        dataId: undefined,
        dataType: '',
        symbol: '',
        symbolName: '',
        dataValue: 0,
        dataDate: '',
        dataSource: '',
        isActive: 1
      }
    },
    async handleSyncData() {
      const loading = this.$loading({
        lock: true,
        text: '正在同步市场数据...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      try {
        await syncMarketData()
        await this.getList()
        await this.updateStatistics()
        this.$message({ type: 'success', message: '数据同步成功' })
      } catch (error) {
        console.error('同步市场数据失败:', error)
        this.$message.error('同步失败: ' + (error.message || '网络错误'))
      } finally {
        loading.close()
      }
    },
    async handleExport() {
      try {
        const response = await exportMarketData(this.listQuery)
        if (response) {
          const blob = new Blob([response], { type: 'application/octet-stream' })
          const url = window.URL.createObjectURL(blob)
          const linkElement = document.createElement('a')
          linkElement.setAttribute('href', url)
          linkElement.setAttribute('download', `市场数据管理_${new Date().toISOString().split('T')[0]}.xlsx`)
          linkElement.click()
          window.URL.revokeObjectURL(url)
          this.$message({ type: 'success', message: '数据导出成功' })
        }
      } catch (error) {
        console.error('导出市场数据失败:', error)
        this.$message.error('导出失败: ' + (error.message || '网络错误'))
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'dataId') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.list.sort((a, b) => a.dataId - b.dataId)
      } else {
        this.list.sort((a, b) => b.dataId - a.dataId)
      }
    },
    getDataTypeTagType(dataType) {
      const typeMap = {
        'EXCHANGE_RATE': 'primary',
        'INTEREST_RATE': 'success',
        'BOND_YIELD': 'warning',
        'STOCK_PRICE': 'danger',
        'COMMODITY': 'info'
      }
      return typeMap[dataType] || 'info'
    },
    getDataTypeText(dataType) {
      const textMap = {
        'EXCHANGE_RATE': '汇率',
        'INTEREST_RATE': '利率',
        'BOND_YIELD': '债券收益率',
        'STOCK_PRICE': '股价',
        'COMMODITY': '大宗商品'
      }
      return textMap[dataType] || dataType
    },
    getDataSourceText(source) {
      const textMap = {
        'PBOC': '央行',
        'CIBM': '银行间市场',
        'SSE': '上交所',
        'SZSE': '深交所',
        'BLOOMBERG': '彭博',
        'REUTERS': '路透'
      }
      return textMap[source] || source
    },
    formatDataValue(value, dataType) {
      if (dataType === 'EXCHANGE_RATE') {
        return value.toFixed(4)
      } else if (dataType === 'INTEREST_RATE' || dataType === 'BOND_YIELD') {
        return value.toFixed(4) + '%'
      } else {
        return value.toFixed(2)
      }
    },
    formatDate(val) {
      if (!val) return ''
      const d = new Date(val)
      if (isNaN(d.getTime())) return val
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${y}-${m}-${day}`
    }
  }
}
</script>

<style lang="scss" scoped>
.market-data-manage {
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

  .data-overview {
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
          &.exchange-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.interest-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }
          &.bond-icon {
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
        }
      }
    }
  }

  .search-card, .table-card {
    margin-bottom: 20px;
  }

  .data-value {
    font-weight: 600;
    color: #409EFF;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }
}
</style>
