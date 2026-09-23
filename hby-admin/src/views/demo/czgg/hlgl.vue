<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.baseCurrency"
        placeholder="基础币种"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="CNY" value="CNY" />
        <el-option label="USD" value="USD" />
        <el-option label="EUR" value="EUR" />
        <el-option label="JPY" value="JPY" />
        <el-option label="GBP" value="GBP" />
      </el-select>
      <el-select
        v-model="listQuery.targetCurrency"
        placeholder="目标币种"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="CNY" value="CNY" />
        <el-option label="USD" value="USD" />
        <el-option label="EUR" value="EUR" />
        <el-option label="JPY" value="JPY" />
        <el-option label="GBP" value="GBP" />
      </el-select>
      <el-select
        v-model="listQuery.rateType"
        placeholder="汇率类型"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="即期汇率" value="SPOT" />
        <el-option label="远期汇率" value="FORWARD" />
        <el-option label="中间价" value="MIDDLE" />
      </el-select>
      <el-date-picker
        v-model="listQuery.rateDate"
        type="date"
        placeholder="汇率日期"
        style="width: 150px"
        class="filter-item"
        format="yyyy-MM-dd"
        value-format="yyyy-MM-dd"
      />
      <el-select
        v-model="listQuery.isEnabled"
        placeholder="状态"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="启用" value="1" />
        <el-option label="禁用" value="0" />
      </el-select>
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        查询
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-plus"
        @click="handleCreate"
      >
        新增
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="success"
        icon="el-icon-upload2"
        @click="handleBatchImport"
      >
        批量导入
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="info"
        icon="el-icon-money"
        @click="handleCurrencyConvert"
      >
        汇率换算
      </el-button>
      <el-button
        v-show="multipleSelection.length > 0"
        class="filter-item"
        style="margin-left: 10px;"
        type="danger"
        icon="el-icon-delete"
        @click="handleBatchDelete"
      >
        批量删除
      </el-button>
    </div>

    <!-- 表格 -->
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="基础币种" prop="baseCurrency" align="center" width="100">
        <template slot-scope="{row}">
          <span>{{ row.baseCurrency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="目标币种" prop="targetCurrency" align="center" width="100">
        <template slot-scope="{row}">
          <span>{{ row.targetCurrency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="汇率值" prop="rateValue" align="center" width="120">
        <template slot-scope="{row}">
          <span class="rate-value">{{ row.rateValue }}</span>
        </template>
      </el-table-column>
      <el-table-column label="汇率类型" prop="rateType" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getRateTypeTagType(row.rateType)">
            {{ getRateTypeText(row.rateType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="汇率日期" prop="rateDate" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ row.rateDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据来源" prop="dataSource" align="center" width="120">
        <template slot-scope="{row}">
          <el-tag :type="getDataSourceTagType(row.dataSource)">
            {{ getDataSourceText(row.dataSource) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="生效时间" prop="effectiveTime" align="center" width="160">
        <template slot-scope="{row}">
          <span>{{ row.effectiveTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="isEnabled" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="center" width="160">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button
            v-if="row.isEnabled === 1"
            size="mini"
            type="warning"
            @click="handleModifyStatus(row, 0)"
          >
            禁用
          </el-button>
          <el-button
            v-else
            size="mini"
            type="success"
            @click="handleModifyStatus(row, 1)"
          >
            启用
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.pageNumber"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

    <!-- 编辑对话框 -->
    <ExchangeRateEditDialog
      ref="editDialog"
      @refresh="getList"
    />

    <!-- 汇率换算对话框 -->
    <CurrencyConvertDialog
      ref="convertDialog"
    />

    <!-- 批量导入对话框 -->
    <ExchangeRateBatchImportDialog
      ref="batchImportDialog"
      @refresh="getList"
    />
  </div>
</template>

<script>
import { getExchangeRateList, deleteExchangeRate, batchDeleteExchangeRate, updateExchangeRateStatus } from '@/api/globalTreasurer/czgg'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import ExchangeRateEditDialog from './components/ExchangeRateEditDialog'
import CurrencyConvertDialog from './components/CurrencyConvertDialog'
import ExchangeRateBatchImportDialog from './components/ExchangeRateBatchImportDialog'

export default {
  name: 'ExchangeRateManagement',
  components: { 
    Pagination, 
    ExchangeRateEditDialog, 
    CurrencyConvertDialog, 
    ExchangeRateBatchImportDialog 
  },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [
        {
          rateId: 1,
          baseCurrency: 'CNY',
          targetCurrency: 'USD',
          currencyPair: 'CNY/USD',
          buyRate: 7.2150,
          sellRate: 7.2350,
          middleRate: 7.2250,
          rateDate: '2025-01-15',
          rateTime: '09:30:00',
          rateSource: 'PBOC',
          rateSourceName: '中国人民银行',
          isEnabled: 1,
          remark: '人民币兑美元汇率',
          createTime: '2025-01-15 09:30:00',
          updateTime: '2025-01-15 09:30:00'
        },
        {
          rateId: 2,
          baseCurrency: 'CNY',
          targetCurrency: 'EUR',
          currencyPair: 'CNY/EUR',
          buyRate: 7.8500,
          sellRate: 7.8800,
          middleRate: 7.8650,
          rateDate: '2025-01-15',
          rateTime: '09:30:00',
          rateSource: 'ECB',
          rateSourceName: '欧洲央行',
          isEnabled: 1,
          remark: '人民币兑欧元汇率',
          createTime: '2025-01-15 09:30:00',
          updateTime: '2025-01-15 09:30:00'
        }
      ],
      total: 2,
      listLoading: false,
      listQuery: {
        pageNumber: 1,
        pageSize: 20,
        baseCurrency: '',
        targetCurrency: '',
        rateType: '',
        rateDate: '',
        isEnabled: null
      },
      multipleSelection: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getExchangeRateList(this.listQuery).then(response => {
        if (response.success) {
          this.list = response.data.records
          this.total = response.data.total
        } else {
          this.$message.error(response.message || '查询失败')
        }
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.pageNumber = 1
      this.getList()
    },
    handleCreate() {
      this.$refs.editDialog.show()
    },
    handleUpdate(row) {
      this.$refs.editDialog.show(row)
    },
    handleDelete(row) {
      this.$confirm('确定要删除该汇率吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteExchangeRate(row.rateId).then(response => {
          if (response.success) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        })
      })
    },
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm(`确定要删除选中的${this.multipleSelection.length}条数据吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = this.multipleSelection.map(item => item.rateId)
        batchDeleteExchangeRate(ids).then(response => {
          if (response.success) {
            this.$message.success('批量删除成功')
            this.getList()
          } else {
            this.$message.error(response.message || '批量删除失败')
          }
        })
      })
    },
    handleModifyStatus(row, status) {
      const statusText = status === 1 ? '启用' : '禁用'
      this.$confirm(`确定要${statusText}该汇率吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateExchangeRateStatus({
          id: row.rateId,
          isEnabled: status,
          updateUser: this.$store.getters.userId
        }).then(response => {
          if (response.success) {
            this.$message.success(`${statusText}成功`)
            this.getList()
          } else {
            this.$message.error(response.message || `${statusText}失败`)
          }
        })
      })
    },
    handleCurrencyConvert() {
      this.$refs.convertDialog.show()
    },
    handleBatchImport() {
      this.$refs.batchImportDialog.show()
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    getRateTypeText(type) {
      const map = {
        'SPOT': '即期汇率',
        'FORWARD': '远期汇率',
        'MIDDLE': '中间价'
      }
      return map[type] || type
    },
    getRateTypeTagType(type) {
      const map = {
        'SPOT': 'success',
        'FORWARD': 'warning',
        'MIDDLE': 'info'
      }
      return map[type] || ''
    },
    getDataSourceText(source) {
      const map = {
        'MANUAL': '手工录入',
        'SYSTEM': '系统获取',
        'BANK': '银行接口',
        'EXTERNAL': '外部接口'
      }
      return map[source] || source
    },
    getDataSourceTagType(source) {
      const map = {
        'MANUAL': 'info',
        'SYSTEM': 'success',
        'BANK': 'primary',
        'EXTERNAL': 'warning'
      }
      return map[source] || ''
    }
  }
}
</script>

<style scoped>
.filter-container {
  padding-bottom: 10px;
}
.filter-item {
  display: inline-block;
  vertical-align: middle;
  margin-bottom: 10px;
  margin-right: 10px;
}
.rate-value {
  font-weight: bold;
  color: #409EFF;
}
</style>
