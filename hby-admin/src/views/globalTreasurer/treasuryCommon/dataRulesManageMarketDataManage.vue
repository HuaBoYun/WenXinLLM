<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.dataCode"
        placeholder="数据编码"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-input
        v-model="listQuery.dataName"
        placeholder="数据名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-date-picker
        v-model="listQuery.dataDate"
        type="date"
        placeholder="数据日期"
        style="width: 150px;"
        class="filter-item"
        @change="handleDateChange"
      />
      <el-select
        v-model="listQuery.dataType"
        placeholder="数据类型"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option v-for="item in dataTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-select
        v-model="listQuery.dataCategory"
        placeholder="数据类别"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option v-for="item in dataCategoryOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-select
        v-model="listQuery.market"
        placeholder="市场"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option v-for="item in marketOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-select
        v-model="listQuery.currency"
        placeholder="币种"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option v-for="item in currencyOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        @click="handleSyncData"
      >
        数据同步
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
      <el-table-column label="数据编码" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.dataCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据名称" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.dataName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getDataTypeTag(row.dataType)">
            {{ getDataTypeName(row.dataType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="数据类别" width="150px" align="center">
        <template slot-scope="{row}">
          <el-tag type="info">{{ getDataCategoryName(row.dataCategory) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="数据值" width="150px" align="right">
        <template slot-scope="{row}">
          <span style="font-weight: bold; color: #409EFF;">{{ formatDataValue(row.dataValue) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.dataDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据来源" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.dataSource || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="市场" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag type="warning">{{ getMarketName(row.market) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="币种" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag type="primary">{{ row.currency || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="单位" width="80px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.dataUnit || '-' }}</span>
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
    <el-dialog :title="dialogStatus === 'create' ? '添加市场数据' : '编辑市场数据'" :visible.sync="dialogFormVisible" width="70%">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style="width: 90%; margin-left:50px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据编码" prop="dataCode">
              <el-input v-model="temp.dataCode" placeholder="请输入数据编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据名称" prop="dataName">
              <el-input v-model="temp.dataName" placeholder="请输入数据名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据类型" prop="dataType">
              <el-select v-model="temp.dataType" placeholder="请选择数据类型" style="width: 100%">
                <el-option v-for="item in dataTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据类别" prop="dataCategory">
              <el-select v-model="temp.dataCategory" placeholder="请选择数据类别" style="width: 100%">
                <el-option v-for="item in dataCategoryOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据值" prop="dataValue">
              <el-input-number
                v-model="temp.dataValue"
                :precision="10"
                :step="0.01"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据日期" prop="dataDate">
              <el-date-picker
                v-model="temp.dataDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="市场">
              <el-select v-model="temp.market" placeholder="请选择市场" style="width: 100%">
                <el-option v-for="item in marketOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="币种">
              <el-select v-model="temp.currency" placeholder="请选择币种" style="width: 100%">
                <el-option v-for="item in currencyOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单位">
              <el-input v-model="temp.dataUnit" placeholder="请输入单位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据来源">
              <el-input v-model="temp.dataSource" placeholder="请输入数据来源" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据频率">
              <el-select v-model="temp.dataFrequency" placeholder="请选择数据频率" style="width: 100%">
                <el-option v-for="item in dataFrequencyOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="temp.isEnabled">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="附加数据">
          <el-input v-model="temp.additionalData" type="textarea" :rows="3" placeholder="请输入附加数据" />
        </el-form-item>
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
    <el-dialog title="查看市场数据详情" :visible.sync="dialogViewVisible" width="70%">
      <el-descriptions :column="3" border>
        <el-descriptions-item label="数据编码">{{ viewData.dataCode }}</el-descriptions-item>
        <el-descriptions-item label="数据名称">{{ viewData.dataName }}</el-descriptions-item>
        <el-descriptions-item label="数据类型">
          <el-tag :type="getDataTypeTag(viewData.dataType)">
            {{ getDataTypeName(viewData.dataType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="数据类别">
          <el-tag type="info">{{ getDataCategoryName(viewData.dataCategory) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="数据值">
          <span style="font-weight: bold; color: #409EFF;">{{ formatDataValue(viewData.dataValue) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="数据日期">{{ viewData.dataDate }}</el-descriptions-item>
        <el-descriptions-item label="市场">
          <el-tag type="warning">{{ getMarketName(viewData.market) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="币种">
          <el-tag type="primary">{{ viewData.currency || '-' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="单位">{{ viewData.dataUnit || '-' }}</el-descriptions-item>
        <el-descriptions-item label="数据来源">{{ viewData.dataSource || '-' }}</el-descriptions-item>
        <el-descriptions-item label="数据频率">
          <el-tag type="info">{{ getDataFrequencyName(viewData.dataFrequency) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.isEnabled === 1 ? 'success' : 'info'">
            {{ viewData.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(viewData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(viewData.updateTime) }}</el-descriptions-item>
        <el-descriptions-item label="附加数据" :span="3">
          <pre style="white-space: pre-wrap; margin: 0; max-height: 100px; overflow-y: auto;">{{ viewData.additionalData || '-' }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ viewData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogViewVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getMarketDataManageList, createMarketDataManage, updateMarketDataManage, deleteMarketDataManage, batchDeleteMarketDataManage, toggleMarketDataManageStatus, exportMarketDataManage, syncMarketData } from '@/api/globalTreasurer/marketDataManage'
import waves from '@/directive/waves'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'

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
        pageNum: 1,
        pageSize: 20,
        dataCode: undefined,
        dataName: undefined,
        dataDate: undefined,
        dataType: undefined,
        dataCategory: undefined,
        market: undefined,
        currency: undefined,
        isEnabled: undefined
      },
      dataTypeOptions: [
        { label: '指数', value: 'INDEX' },
        { label: '股票', value: 'STOCK' },
        { label: '债券', value: 'BOND' },
        { label: '商品', value: 'COMMODITY' },
        { label: '外汇', value: 'FOREX' },
        { label: '基金', value: 'FUND' },
        { label: '期货', value: 'FUTURES' },
        { label: '期权', value: 'OPTION' }
      ],
      dataCategoryOptions: [
        { label: '股票指数', value: 'STOCK_INDEX' },
        { label: '大宗商品', value: 'OIL' },
        { label: '贵金属', value: 'METAL' },
        { label: '农产品', value: 'AGRICULTURE' },
        { label: '能源', value: 'ENERGY' },
        { label: '工业品', value: 'INDUSTRIAL' },
        { label: '化工品', value: 'CHEMICAL' }
      ],
      marketOptions: [
        { label: '上交所', value: 'SSE' },
        { label: '深交所', value: 'SZSE' },
        { label: '上期所', value: 'SHFE' },
        { label: '郑商所', value: 'CZCE' },
        { label: '大商所', value: 'DCE' },
        { label: '中金所', value: 'CFFEX' },
        { label: '纽交所', value: 'NYSE' },
        { label: '纳斯达克', value: 'NASDAQ' },
        { label: '伦敦金属交易所', value: 'LME' },
        { label: '芝加哥商品交易所', value: 'CME' },
        { label: '纽约商品交易所', value: 'NYMEX' },
        { label: '纽约期货交易所', value: 'COMEX' }
      ],
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
      dataFrequencyOptions: [
        { label: '实时', value: 'REAL_TIME' },
        { label: '分钟', value: 'MINUTELY' },
        { label: '小时', value: 'HOURLY' },
        { label: '每日', value: 'DAILY' },
        { label: '每周', value: 'WEEKLY' },
        { label: '每月', value: 'MONTHLY' },
        { label: '每季', value: 'QUARTERLY' },
        { label: '每年', value: 'YEARLY' }
      ],
      multipleSelection: [],
      temp: {
        id: undefined,
        dataCode: '',
        dataName: '',
        dataType: '',
        dataCategory: '',
        dataValue: 0,
        dataDate: '',
        dataSource: '',
        dataFrequency: '',
        dataUnit: '',
        market: '',
        currency: '',
        isEnabled: 1,
        orgId: 1,
        remark: '',
        additionalData: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      dialogViewVisible: false,
      viewData: {},
      rules: {
        dataCode: [{ required: true, message: '请输入数据编码', trigger: 'blur' }],
        dataName: [{ required: true, message: '请输入数据名称', trigger: 'blur' }],
        dataType: [{ required: true, message: '请选择数据类型', trigger: 'change' }],
        dataCategory: [{ required: true, message: '请选择数据类别', trigger: 'change' }],
        dataValue: [{ required: true, message: '请输入数据值', trigger: 'blur' }],
        dataDate: [{ required: true, message: '请选择数据日期', trigger: 'change' }]
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
      getMarketDataManageList(this.listQuery).then(response => {
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
    handleDateChange(value) {
      if (value) {
        this.listQuery.dataDate = this.formatDate(value)
      } else {
        this.listQuery.dataDate = undefined
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
        dataCode: '',
        dataName: '',
        dataType: '',
        dataCategory: '',
        dataValue: 0,
        dataDate: '',
        dataSource: '',
        dataFrequency: '',
        dataUnit: '',
        market: '',
        currency: '',
        isEnabled: 1,
        orgId: 1,
        remark: '',
        additionalData: ''
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
          if (this.temp.dataDate) {
            this.temp.dataDate = this.formatDate(this.temp.dataDate)
          }
          createMarketDataManage(this.temp).then(response => {
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
          if (this.temp.dataDate) {
            this.temp.dataDate = this.formatDate(this.temp.dataDate)
          }
          const tempData = Object.assign({}, this.temp)
          updateMarketDataManage(tempData).then(response => {
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
        deleteMarketDataManage(row.id).then(response => {
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
        batchDeleteMarketDataManage(ids).then(response => {
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
      this.$confirm(`确定要${statusText}该市场数据吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        toggleMarketDataManageStatus({ id: row.id, isEnabled: status }).then(response => {
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
      exportMarketDataManage(this.listQuery).then(response => {
        const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `市场数据管理_${parseTime(new Date(), '{y}{m}{d}')}.xlsx`
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
    handleSyncData() {
      this.$confirm('确定要同步市场数据吗? 此操作可能需要较长时间。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        syncMarketData().then(response => {
          if (response.code === 1) {
            this.$notify({
              title: '成功',
              message: '数据同步成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          }
        }).catch(() => {
          this.$notify({
            title: '失败',
            message: '数据同步失败',
            type: 'error',
            duration: 2000
          })
        })
      })
    },
    formatDataValue(value) {
      if (value === null || value === undefined) return '-'
      return parseFloat(value).toLocaleString()
    },
    getDataTypeName(type) {
      const typeMap = {
        'INDEX': '指数',
        'STOCK': '股票',
        'BOND': '债券',
        'COMMODITY': '商品',
        'FOREX': '外汇',
        'FUND': '基金',
        'FUTURES': '期货',
        'OPTION': '期权'
      }
      return typeMap[type] || type
    },
    getDataTypeTag(type) {
      const tagMap = {
        'INDEX': 'primary',
        'STOCK': 'success',
        'BOND': 'info',
        'COMMODITY': 'warning',
        'FOREX': 'danger',
        'FUND': 'success',
        'FUTURES': 'warning',
        'OPTION': 'danger'
      }
      return tagMap[type] || ''
    },
    getDataCategoryName(category) {
      const categoryMap = {
        'STOCK_INDEX': '股票指数',
        'OIL': '大宗商品',
        'METAL': '贵金属',
        'AGRICULTURE': '农产品',
        'ENERGY': '能源',
        'INDUSTRIAL': '工业品',
        'CHEMICAL': '化工品'
      }
      return categoryMap[category] || category
    },
    getMarketName(market) {
      const marketMap = {
        'SSE': '上交所',
        'SZSE': '深交所',
        'SHFE': '上期所',
        'CZCE': '郑商所',
        'DCE': '大商所',
        'CFFEX': '中金所',
        'NYSE': '纽交所',
        'NASDAQ': '纳斯达克',
        'LME': '伦敦金属交易所',
        'CME': '芝加哥商品交易所',
        'NYMEX': '纽约商品交易所',
        'COMEX': '纽约期货交易所'
      }
      return marketMap[market] || market
    },
    getDataFrequencyName(frequency) {
      const frequencyMap = {
        'REAL_TIME': '实时',
        'MINUTELY': '分钟',
        'HOURLY': '小时',
        'DAILY': '每日',
        'WEEKLY': '每周',
        'MONTHLY': '每月',
        'QUARTERLY': '每季',
        'YEARLY': '每年'
      }
      return frequencyMap[frequency] || frequency
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