<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-date-picker
        v-model="listQuery.rateDateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        style="width: 240px;"
        class="filter-item"
        @change="handleDateRangeChange"
      />
      <el-select
        v-model="listQuery.baseCurrency"
        placeholder="基础币种"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option v-for="item in currencyOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-select
        v-model="listQuery.targetCurrency"
        placeholder="目标币种"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option v-for="item in currencyOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-select
        v-model="listQuery.rateType"
        placeholder="汇率类型"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option v-for="item in rateTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-select
        v-model="listQuery.dataSource"
        placeholder="数据来源"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option v-for="item in dataSourceOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-select
        v-model="listQuery.isEnabled"
        placeholder="状态"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        添加
      </el-button>
      <el-button
        v-if="multipleSelection.length > 0"
        class="filter-item"
        style="margin-left: 10px;"
        type="danger"
        icon="el-icon-delete"
        @click="handleBatchDelete"
      >
        批量删除
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="success"
        icon="el-icon-upload2"
        @click="handleImport"
      >
        导入
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="success"
        icon="el-icon-download"
        @click="handleExport"
      >
        导出
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="info"
        icon="el-icon-refresh"
        @click="handleConvertCurrency"
      >
        汇率换算
      </el-button>
    </div>

    <!-- 数据表格 -->
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
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="汇率日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.rateDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="基础币种" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag type="primary">{{ row.baseCurrency }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="目标币种" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag type="success">{{ row.targetCurrency }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="汇率类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getRateTypeTag(row.rateType)">
            {{ getRateTypeName(row.rateType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="汇率" width="120px" align="right">
        <template slot-scope="{row}">
          <span style="font-weight: bold;">{{ formatRate(row.exchangeRate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开盘价" width="100px" align="right">
        <template slot-scope="{row}">
          <span>{{ row.openingRate ? formatRate(row.openingRate) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="收盘价" width="100px" align="right">
        <template slot-scope="{row}">
          <span>{{ row.closingRate ? formatRate(row.closingRate) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最高价" width="100px" align="right">
        <template slot-scope="{row}">
          <span>{{ row.highestRate ? formatRate(row.highestRate) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最低价" width="100px" align="right">
        <template slot-scope="{row}">
          <span>{{ row.lowestRate ? formatRate(row.lowestRate) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据来源" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.dataSource || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled | statusFilter">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="160px" align="center">
        <template slot-scope="{row}">
          <span>{{ parseTime(row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button type="info" size="mini" @click="handleView(row)">
            查看
          </el-button>
          <el-button v-if="row.isEnabled==1" size="mini" type="warning" @click="handleToggleStatus(row, 0)">
            禁用
          </el-button>
          <el-button v-else size="mini" type="success" @click="handleToggleStatus(row, 1)">
            启用
          </el-button>
          <el-button v-if="row.isEnabled!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <!-- 添加/编辑对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '添加汇率' : '编辑汇率'" :visible.sync="dialogFormVisible" width="70%">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style="width: 90%; margin-left:50px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="汇率日期" prop="rateDate">
              <el-date-picker
                v-model="temp.rateDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="汇率类型">
              <el-select v-model="temp.rateType" placeholder="请选择汇率类型" style="width: 100%">
                <el-option v-for="item in rateTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="基础币种" prop="baseCurrency">
              <el-select v-model="temp.baseCurrency" placeholder="请选择基础币种" style="width: 100%">
                <el-option v-for="item in currencyOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标币种" prop="targetCurrency">
              <el-select v-model="temp.targetCurrency" placeholder="请选择目标币种" style="width: 100%">
                <el-option v-for="item in currencyOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="汇率" prop="exchangeRate">
              <el-input-number
                v-model="temp.exchangeRate"
                :precision="8"
                :step="0.00000001"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据来源">
              <el-input v-model="temp.dataSource" placeholder="请输入数据来源" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="开盘价">
              <el-input-number
                v-model="temp.openingRate"
                :precision="8"
                :step="0.00000001"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="收盘价">
              <el-input-number
                v-model="temp.closingRate"
                :precision="8"
                :step="0.00000001"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态">
              <el-radio-group v-model="temp.isEnabled">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最高价">
              <el-input-number
                v-model="temp.highestRate"
                :precision="8"
                :step="0.00000001"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最低价">
              <el-input-number
                v-model="temp.lowestRate"
                :precision="8"
                :step="0.00000001"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog title="查看汇率详情" :visible.sync="dialogViewVisible" width="70%">
      <el-descriptions :column="3" border>
        <el-descriptions-item label="汇率日期">{{ viewData.rateDate }}</el-descriptions-item>
        <el-descriptions-item label="基础币种">
          <el-tag type="primary">{{ viewData.baseCurrency }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="目标币种">
          <el-tag type="success">{{ viewData.targetCurrency }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="汇率类型">
          <el-tag :type="getRateTypeTag(viewData.rateType)">
            {{ getRateTypeName(viewData.rateType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="汇率">
          <span style="font-weight: bold; color: #409EFF;">{{ formatRate(viewData.exchangeRate) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="数据来源">{{ viewData.dataSource || '-' }}</el-descriptions-item>
        <el-descriptions-item label="开盘价">{{ formatRate(viewData.openingRate) }}</el-descriptions-item>
        <el-descriptions-item label="收盘价">{{ formatRate(viewData.closingRate) }}</el-descriptions-item>
        <el-descriptions-item label="最高价">{{ formatRate(viewData.highestRate) }}</el-descriptions-item>
        <el-descriptions-item label="最低价">{{ formatRate(viewData.lowestRate) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.isEnabled === 1 ? 'success' : 'info'">
            {{ viewData.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(viewData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(viewData.updateTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ viewData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogViewVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 汇率换算对话框 -->
    <el-dialog title="汇率换算" :visible.sync="dialogConvertVisible" width="50%">
      <el-form ref="convertForm" :model="convertForm" :rules="convertRules" label-width="120px" style="width: 90%; margin-left:50px;">
        <el-form-item label="金额" prop="amount">
          <el-input-number
            v-model="convertForm.amount"
            :precision="2"
            :step="1"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="原币种" prop="baseCurrency">
          <el-select v-model="convertForm.baseCurrency" placeholder="请选择原币种" style="width: 100%">
            <el-option v-for="item in currencyOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标币种" prop="targetCurrency">
          <el-select v-model="convertForm.targetCurrency" placeholder="请选择目标币种" style="width: 100%">
            <el-option v-for="item in currencyOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="汇率日期">
          <el-date-picker
            v-model="convertForm.rateDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="汇率类型">
          <el-select v-model="convertForm.rateType" placeholder="请选择汇率类型" style="width: 100%">
            <el-option v-for="item in rateTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
      </el-form>
      <div style="margin-top: 20px; padding: 20px; background-color: #f5f5f5; border-radius: 4px;">
        <h3>换算结果：</h3>
        <p style="font-size: 16px; color: #409EFF; margin-top: 10px;">
          {{ convertForm.amount || 0 }} {{ convertForm.baseCurrency || '' }} = {{ convertResult || 0 }} {{ convertForm.targetCurrency || '' }}
        </p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogConvertVisible = false">
          关闭
        </el-button>
        <el-button type="primary" @click="handleConvertCalculate">
          计算
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getExchangeRateManageList, createExchangeRateManage, updateExchangeRateManage, deleteExchangeRateManage, batchDeleteExchangeRateManage, toggleExchangeRateManageStatus, exportExchangeRateManage, batchImportExchangeRateManage, convertCurrency } from '@/api/globalTreasurer/exchangeRateManage'
import waves from '@/directive/waves'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'

export default {
  name: 'ExchangeRateManage',
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
        pageNum: 1,
        pageSize: 20,
        startDate: undefined,
        endDate: undefined,
        baseCurrency: undefined,
        targetCurrency: undefined,
        rateType: undefined,
        dataSource: undefined,
        isEnabled: undefined
      },
      currencyOptions: [
        { label: '人民币', value: 'CNY' },
        { label: '美元', value: 'USD' },
        { label: '欧元', value: 'EUR' },
        { label: '英镑', value: 'GBP' },
        { label: '日元', value: 'JPY' },
        { label: '港币', value: 'HKD' },
        { label: '澳元', value: 'AUD' },
        { label: '加元', value: 'CAD' }
      ],
      rateTypeOptions: [
        { label: '中间价', value: 'MIDDLE' },
        { label: '买入价', value: 'BUY' },
        { label: '卖出价', value: 'SELL' },
        { label: '现汇价', value: 'SPOT' },
        { label: '远期价', value: 'FORWARD' }
      ],
      dataSourceOptions: [
        { label: '银行', value: 'BANK' },
        { label: '外汇交易所', value: 'FOREX_EXCHANGE' },
        { label: '第三方数据源', value: 'THIRD_PARTY' },
        { label: '手动录入', value: 'MANUAL' }
      ],
      multipleSelection: [],
      temp: {
        id: undefined,
        rateDate: '',
        baseCurrency: '',
        targetCurrency: '',
        rateType: 'MIDDLE',
        exchangeRate: 0,
        openingRate: undefined,
        closingRate: undefined,
        highestRate: undefined,
        lowestRate: undefined,
        dataSource: '',
        isEnabled: 1,
        orgId: 1,
        remark: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      dialogViewVisible: false,
      dialogConvertVisible: false,
      viewData: {},
      convertResult: 0,
      convertForm: {
        amount: 0,
        baseCurrency: '',
        targetCurrency: '',
        rateDate: new Date(),
        rateType: 'MIDDLE'
      },
      convertRules: {
        amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
        baseCurrency: [{ required: true, message: '请选择原币种', trigger: 'change' }],
        targetCurrency: [{ required: true, message: '请选择目标币种', trigger: 'change' }]
      },
      rules: {
        rateDate: [{ required: true, message: '请选择汇率日期', trigger: 'change' }],
        baseCurrency: [{ required: true, message: '请选择基础币种', trigger: 'change' }],
        targetCurrency: [{ required: true, message: '请选择目标币种', trigger: 'change' }],
        exchangeRate: [{ required: true, message: '请输入汇率', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    getList() {
      this.listLoading = true
      getExchangeRateManageList(this.listQuery).then(response => {
        if (response.code === 1) {
          this.list = response.data.tlist
          this.total = response.data.totalRecord
        }
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleDateRangeChange(value) {
      if (value && value.length === 2) {
        this.listQuery.startDate = this.formatDate(value[0])
        this.listQuery.endDate = this.formatDate(value[1])
      } else {
        this.listQuery.startDate = undefined
        this.listQuery.endDate = undefined
      }
    },
    formatDate(date) {
      if (!date) return ''
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        rateDate: '',
        baseCurrency: '',
        targetCurrency: '',
        rateType: 'MIDDLE',
        exchangeRate: 0,
        openingRate: undefined,
        closingRate: undefined,
        highestRate: undefined,
        lowestRate: undefined,
        dataSource: '',
        isEnabled: 1,
        orgId: 1,
        remark: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 格式化日期
          if (this.temp.rateDate) {
            this.temp.rateDate = this.formatDate(this.temp.rateDate)
          }
          createExchangeRateManage(this.temp).then(response => {
            if (response.code === 1) {
              this.list.unshift(response.data)
              this.total++
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
            }
          })
        }
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
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 格式化日期
          if (this.temp.rateDate) {
            this.temp.rateDate = this.formatDate(this.temp.rateDate)
          }
          const tempData = Object.assign({}, this.temp)
          updateExchangeRateManage(tempData).then(response => {
            if (response.code === 1) {
              const index = this.list.findIndex(v => v.id === this.temp.id)
              this.list.splice(index, 1, response.data)
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
            }
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteExchangeRateManage(row.id).then(response => {
          if (response.code === 1) {
            this.list.splice(index, 1)
            this.total--
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
          }
        })
      })
    },
    handleBatchDelete() {
      this.$confirm('此操作将永久删除选中的记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = this.multipleSelection.map(item => item.id)
        batchDeleteExchangeRateManage(ids).then(response => {
          if (response.code === 1) {
            this.getList()
            this.$notify({
              title: '成功',
              message: '批量删除成功',
              type: 'success',
              duration: 2000
            })
          }
        })
      })
    },
    handleToggleStatus(row, status) {
      const statusText = status === 1 ? '启用' : '禁用'
      this.$confirm(`确定要${statusText}该汇率吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        toggleExchangeRateManageStatus({ id: row.id, isEnabled: status }).then(response => {
          if (response.code === 1) {
            row.isEnabled = status
            this.$notify({
              title: '成功',
              message: `${statusText}成功`,
              type: 'success',
              duration: 2000
            })
          }
        })
      })
    },
    handleView(row) {
      this.viewData = Object.assign({}, row)
      this.dialogViewVisible = true
    },
    handleImport() {
      this.$message.info('导入功能待实现')
    },
    handleExport() {
      exportExchangeRateManage(this.listQuery).then(response => {
        const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `汇率管理_${parseTime(new Date(), '{y}{m}{d}')}.xlsx`
        link.click()
        window.URL.revokeObjectURL(link.href)
        this.$notify({
          title: '成功',
          message: '导出成功',
          type: 'success',
          duration: 2000
        })
      })
    },
    handleConvertCurrency() {
      this.dialogConvertVisible = true
      this.convertResult = 0
    },
    handleConvertCalculate() {
      this.$refs['convertForm'].validate((valid) => {
        if (valid) {
          const params = {
            amount: this.convertForm.amount,
            baseCurrency: this.convertForm.baseCurrency,
            targetCurrency: this.convertForm.targetCurrency,
            rateDate: this.formatDate(this.convertForm.rateDate),
            rateType: this.convertForm.rateType,
            orgId: 1
          }
          convertCurrency(params).then(response => {
            if (response.code === 1) {
              this.convertResult = response.data
              this.$notify({
                title: '成功',
                message: '汇率换算成功',
                type: 'success',
                duration: 2000
              })
            }
          }).catch(() => {
            this.convertResult = 0
          })
        }
      })
    },
    formatRate(rate) {
      if (rate === null || rate === undefined) return '-'
      return parseFloat(rate).toFixed(8)
    },
    getRateTypeName(type) {
      const typeMap = {
        'MIDDLE': '中间价',
        'BUY': '买入价',
        'SELL': '卖出价',
        'SPOT': '现汇价',
        'FORWARD': '远期价'
      }
      return typeMap[type] || type
    },
    getRateTypeTag(type) {
      const tagMap = {
        'MIDDLE': '',
        'BUY': 'success',
        'SELL': 'danger',
        'SPOT': 'info',
        'FORWARD': 'warning'
      }
      return tagMap[type] || ''
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
}
</style>