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
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        @sort-change="sortChange"
      >
        <el-table-column label="ID" prop="dataId" sortable="custom" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.dataId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getDataTypeTagType(row.dataType)">
              {{ getDataTypeText(row.dataType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="数据代码" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleUpdate(row)">{{ row.symbol }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据名称" min-width="150px">
          <template slot-scope="{row}">
            <span>{{ row.symbolName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据值" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="data-value">{{ formatDataValue(row.dataValue, row.dataType) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.dataDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据来源" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag size="mini">{{ row.dataSource }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" class-name="status-col" width="80">
          <template slot-scope="{row}">
            <el-tag :type="row.isActive | statusFilter">
              {{ row.isActive ? '有效' : '无效' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="160px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.createTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button v-if="row.isActive" size="mini" type="warning" @click="handleModifyStatus(row,'disable')">
              禁用
            </el-button>
            <el-button v-else size="mini" type="success" @click="handleModifyStatus(row,'enable')">
              启用
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
              删除
            </el-button>
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
import { getMarketDataPage, createMarketData, updateMarketData, deleteMarketData, importMarketData, exportMarketData, syncMarketData } from '@/api/globalTreasurer/treasuryCommon'
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
      tableKey: 0,
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
    getList() {
      this.listLoading = true
      // 使用模拟数据
      setTimeout(() => {
        this.list = [
          {
            dataId: 1,
            dataType: 'EXCHANGE_RATE',
            symbol: 'USDCNY',
            symbolName: '美元兑人民币',
            dataValue: 7.1234,
            dataDate: '2024-09-25',
            dataTime: '2024-09-25 09:30:00',
            dataSource: 'PBOC',
            isActive: 1,
            createTime: '2024-09-25 09:30:00'
          },
          {
            dataId: 2,
            dataType: 'INTEREST_RATE',
            symbol: 'SHIBOR_1M',
            symbolName: '上海银行间同业拆放利率1个月',
            dataValue: 2.3450,
            dataDate: '2024-09-25',
            dataTime: '2024-09-25 11:30:00',
            dataSource: 'CIBM',
            isActive: 1,
            createTime: '2024-09-25 11:30:00'
          },
          {
            dataId: 3,
            dataType: 'BOND_YIELD',
            symbol: 'CN10Y',
            symbolName: '中国10年期国债收益率',
            dataValue: 2.6780,
            dataDate: '2024-09-25',
            dataTime: '2024-09-25 15:00:00',
            dataSource: 'CIBM',
            isActive: 1,
            createTime: '2024-09-25 15:00:00'
          }
        ]
        this.total = this.list.length
        this.listLoading = false
      }, 1000)
    },
    updateStatistics() {
      this.exchangeRateCount = 156
      this.interestRateCount = 89
      this.bondYieldCount = 234
      this.lastUpdateTime = '09:30'
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
      }).then(() => {
        this.list.splice(index, 1)
        this.total = this.list.length
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      })
    },
    handleModifyStatus(row, status) {
      const message = status === 'enable' ? '启用' : '禁用'
      this.$confirm(`确认${message}该市场数据?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.isActive = status === 'enable' ? 1 : 0
        this.$message({
          type: 'success',
          message: `${message}成功!`
        })
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.dataId = parseInt(Math.random() * 100) + 1024
          this.temp.createTime = new Date().toLocaleString()
          this.list.unshift(this.temp)
          this.total = this.list.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '创建成功'
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          const index = this.list.findIndex(v => v.dataId === this.temp.dataId)
          this.list.splice(index, 1, tempData)
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '更新成功'
          })
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
    handleSyncData() {
      this.$message({
        type: 'success',
        message: '数据同步成功'
      })
      this.getList()
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '数据导出成功'
      })
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
    formatDataValue(value, dataType) {
      if (dataType === 'EXCHANGE_RATE') {
        return value.toFixed(4)
      } else if (dataType === 'INTEREST_RATE' || dataType === 'BOND_YIELD') {
        return value.toFixed(4) + '%'
      } else {
        return value.toFixed(2)
      }
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
