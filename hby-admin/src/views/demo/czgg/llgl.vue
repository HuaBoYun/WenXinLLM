<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.rateType"
        placeholder="利率类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option label="存款利率" value="DEPOSIT" />
        <el-option label="贷款利率" value="LOAN" />
        <el-option label="同业拆借" value="INTERBANK" />
        <el-option label="央行基准" value="BENCHMARK" />
      </el-select>
      <el-select
        v-model="listQuery.currencyCode"
        placeholder="币种"
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
        v-model="listQuery.termType"
        placeholder="期限类型"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="活期" value="DEMAND" />
        <el-option label="1个月" value="1M" />
        <el-option label="3个月" value="3M" />
        <el-option label="6个月" value="6M" />
        <el-option label="1年" value="1Y" />
        <el-option label="3年" value="3Y" />
        <el-option label="5年" value="5Y" />
      </el-select>
      <el-date-picker
        v-model="listQuery.rateDate"
        type="date"
        placeholder="利率日期"
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
        @click="handleInterestCalculate"
      >
        利息计算
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
      <el-table-column label="利率类型" prop="rateType" align="center" width="120">
        <template slot-scope="{row}">
          <el-tag :type="getRateTypeTagType(row.rateType)">
            {{ getRateTypeText(row.rateType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="币种" prop="currencyCode" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.currencyCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="期限类型" prop="termType" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getTermTypeTagType(row.termType)">
            {{ getTermTypeText(row.termType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="利率值(%)" prop="rateValue" align="center" width="120">
        <template slot-scope="{row}">
          <span class="rate-value">{{ row.rateValue }}</span>
        </template>
      </el-table-column>
      <el-table-column label="利率日期" prop="rateDate" align="center" width="120">
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
    <InterestRateEditDialog
      ref="editDialog"
      @refresh="getList"
    />

    <!-- 利息计算对话框 -->
    <InterestCalculateDialog
      ref="calculateDialog"
    />

    <!-- 批量导入对话框 -->
    <InterestRateBatchImportDialog
      ref="batchImportDialog"
      @refresh="getList"
    />
  </div>
</template>

<script>
import { getInterestRateList, deleteInterestRate, batchDeleteInterestRate, updateInterestRateStatus } from '@/api/globalTreasurer/czgg'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import InterestRateEditDialog from './components/InterestRateEditDialog'
import InterestCalculateDialog from './components/InterestCalculateDialog'
import InterestRateBatchImportDialog from './components/InterestRateBatchImportDialog'

export default {
  name: 'InterestRateManagement',
  components: { 
    Pagination, 
    InterestRateEditDialog, 
    InterestCalculateDialog, 
    InterestRateBatchImportDialog 
  },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [
        {
          rateId: 1,
          rateType: 'DEPOSIT',
          rateTypeName: '存款利率',
          currencyCode: 'CNY',
          termType: '3M',
          termTypeName: '3个月',
          baseRate: 2.75,
          adjustRate: 0.25,
          finalRate: 3.00,
          rateDate: '2025-01-15',
          effectiveDate: '2025-01-16',
          expiryDate: '2025-12-31',
          isEnabled: 1,
          remark: '三个月定期存款利率',
          createTime: '2025-01-15 10:00:00',
          updateTime: '2025-01-15 10:00:00'
        },
        {
          rateId: 2,
          rateType: 'LOAN',
          rateTypeName: '贷款利率',
          currencyCode: 'USD',
          termType: '1Y',
          termTypeName: '1年',
          baseRate: 4.50,
          adjustRate: 0.50,
          finalRate: 5.00,
          rateDate: '2025-01-15',
          effectiveDate: '2025-01-16',
          expiryDate: '2025-12-31',
          isEnabled: 1,
          remark: '一年期美元贷款利率',
          createTime: '2025-01-15 11:00:00',
          updateTime: '2025-01-15 11:00:00'
        }
      ],
      total: 2,
      listLoading: false,
      listQuery: {
        pageNumber: 1,
        pageSize: 20,
        rateType: '',
        currencyCode: '',
        termType: '',
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
      getInterestRateList(this.listQuery).then(response => {
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
      this.$confirm('确定要删除该利率吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteInterestRate(row.rateId).then(response => {
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
        batchDeleteInterestRate(ids).then(response => {
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
      this.$confirm(`确定要${statusText}该利率吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateInterestRateStatus({
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
    handleInterestCalculate() {
      this.$refs.calculateDialog.show()
    },
    handleBatchImport() {
      this.$refs.batchImportDialog.show()
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    getRateTypeText(type) {
      const map = {
        'DEPOSIT': '存款利率',
        'LOAN': '贷款利率',
        'INTERBANK': '同业拆借',
        'BENCHMARK': '央行基准'
      }
      return map[type] || type
    },
    getRateTypeTagType(type) {
      const map = {
        'DEPOSIT': 'success',
        'LOAN': 'warning',
        'INTERBANK': 'info',
        'BENCHMARK': 'primary'
      }
      return map[type] || ''
    },
    getTermTypeText(type) {
      const map = {
        'DEMAND': '活期',
        '1M': '1个月',
        '3M': '3个月',
        '6M': '6个月',
        '1Y': '1年',
        '3Y': '3年',
        '5Y': '5年'
      }
      return map[type] || type
    },
    getTermTypeTagType(type) {
      const map = {
        'DEMAND': 'info',
        '1M': 'success',
        '3M': 'success',
        '6M': 'warning',
        '1Y': 'warning',
        '3Y': 'danger',
        '5Y': 'danger'
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
  color: #E6A23C;
}
</style>
