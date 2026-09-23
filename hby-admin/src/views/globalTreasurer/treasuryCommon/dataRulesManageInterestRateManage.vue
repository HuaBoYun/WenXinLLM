<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-date-picker
        v-model="listQuery.rateDate"
        type="date"
        placeholder="利率日期"
        style="width: 150px;"
        class="filter-item"
        @change="handleDateChange"
      />
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
        v-model="listQuery.interestRateType"
        placeholder="利率类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option v-for="item in interestRateTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-select
        v-model="listQuery.termPeriod"
        placeholder="期限"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option v-for="item in termPeriodOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        icon="el-icon-download"
        @click="handleExport"
      >
        导出
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
      <el-table-column label="利率日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.rateDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="币种" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag type="primary">{{ row.currency }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="利率类型" width="150px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getInterestRateTypeTag(row.interestRateType)">
            {{ getInterestRateTypeName(row.interestRateType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="期限" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag type="info">{{ getTermPeriodName(row.termPeriod) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="年利率" width="120px" align="right">
        <template slot-scope="{row}">
          <span style="font-weight: bold; color: #409EFF;">{{ formatRate(row.annualRate) }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="名义利率" width="120px" align="right">
        <template slot-scope="{row}">
          <span>{{ row.nominalRate ? formatRate(row.nominalRate) + '%' : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际利率" width="120px" align="right">
        <template slot-scope="{row}">
          <span>{{ row.effectiveRate ? formatRate(row.effectiveRate) + '%' : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="市场参考" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.marketReference || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" width="160px" align="center">
        <template slot-scope="{row}">
          <span>{{ parseTime(row.publishTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled | statusFilter">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
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
    <el-dialog :title="dialogStatus === 'create' ? '添加利率' : '编辑利率'" :visible.sync="dialogFormVisible" width="70%">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style="width: 90%; margin-left:50px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="利率日期" prop="rateDate">
              <el-date-picker
                v-model="temp.rateDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currency">
              <el-select v-model="temp.currency" placeholder="请选择币种" style="width: 100%">
                <el-option v-for="item in currencyOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="利率类型" prop="interestRateType">
              <el-select v-model="temp.interestRateType" placeholder="请选择利率类型" style="width: 100%">
                <el-option v-for="item in interestRateTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="期限" prop="termPeriod">
              <el-select v-model="temp.termPeriod" placeholder="请选择期限" style="width: 100%">
                <el-option v-for="item in termPeriodOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="年利率" prop="annualRate">
              <el-input-number
                v-model="temp.annualRate"
                :precision="6"
                :step="0.01"
                :min="0"
                :max="100"
                style="width: 100%"
              />
              <span style="margin-left: 5px;">%</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="名义利率">
              <el-input-number
                v-model="temp.nominalRate"
                :precision="6"
                :step="0.01"
                :min="0"
                :max="100"
                style="width: 100%"
              />
              <span style="margin-left: 5px;">%</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实际利率">
              <el-input-number
                v-model="temp.effectiveRate"
                :precision="6"
                :step="0.01"
                :min="0"
                :max="100"
                style="width: 100%"
              />
              <span style="margin-left: 5px;">%</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="市场参考">
              <el-input v-model="temp.marketReference" placeholder="请输入市场参考" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发布时间">
              <el-date-picker
                v-model="temp.publishTime"
                type="datetime"
                placeholder="选择时间"
                style="width: 100%"
              />
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
    <el-dialog title="查看利率详情" :visible.sync="dialogViewVisible" width="70%">
      <el-descriptions :column="3" border>
        <el-descriptions-item label="利率日期">{{ viewData.rateDate }}</el-descriptions-item>
        <el-descriptions-item label="币种">
          <el-tag type="primary">{{ viewData.currency }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="利率类型">
          <el-tag :type="getInterestRateTypeTag(viewData.interestRateType)">
            {{ getInterestRateTypeName(viewData.interestRateType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="期限">
          <el-tag type="info">{{ getTermPeriodName(viewData.termPeriod) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="年利率">
          <span style="font-weight: bold; color: #409EFF;">{{ formatRate(viewData.annualRate) }}%</span>
        </el-descriptions-item>
        <el-descriptions-item label="市场参考">{{ viewData.marketReference || '-' }}</el-descriptions-item>
        <el-descriptions-item label="名义利率">{{ formatRate(viewData.nominalRate) }}%</el-descriptions-item>
        <el-descriptions-item label="实际利率">{{ formatRate(viewData.effectiveRate) }}%</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.isEnabled === 1 ? 'success' : 'info'">
            {{ viewData.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="发布时间">{{ parseTime(viewData.publishTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(viewData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(viewData.updateTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ viewData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogViewVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getInterestRateManageList, createInterestRateManage, updateInterestRateManage, deleteInterestRateManage, batchDeleteInterestRateManage, toggleInterestRateManageStatus, exportInterestRateManage } from '@/api/globalTreasurer/interestRateManage'
import waves from '@/directive/waves'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'

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
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        pageNum: 1,
        pageSize: 20,
        rateDate: undefined,
        currency: undefined,
        interestRateType: undefined,
        termPeriod: undefined,
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
      interestRateTypeOptions: [
        { label: '贷款市场报价利率', value: 'LPR' },
        { label: '基准利率', value: 'BASE_RATE' },
        { label: '再贷款利率', value: 'REDISCOUNT_RATE' },
        { label: '同业拆借利率', value: 'LIBOR' },
        { label: '存款利率', value: 'DEPOSIT_RATE' },
        { label: '贷款利率', value: 'LOAN_RATE' }
      ],
      termPeriodOptions: [
        { label: '隔夜', value: 'ON' },
        { label: '1周', value: '1W' },
        { label: '1个月', value: '1M' },
        { label: '3个月', value: '3M' },
        { label: '6个月', value: '6M' },
        { label: '9个月', value: '9M' },
        { label: '1年', value: '1Y' },
        { label: '2年', value: '2Y' },
        { label: '3年', value: '3Y' },
        { label: '5年', value: '5Y' }
      ],
      multipleSelection: [],
      temp: {
        id: undefined,
        rateDate: '',
        currency: '',
        interestRateType: '',
        termPeriod: '',
        annualRate: 0,
        nominalRate: undefined,
        effectiveRate: undefined,
        marketReference: '',
        publishTime: '',
        isEnabled: 1,
        orgId: 1,
        remark: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      dialogViewVisible: false,
      viewData: {},
      rules: {
        rateDate: [{ required: true, message: '请选择利率日期', trigger: 'change' }],
        currency: [{ required: true, message: '请选择币种', trigger: 'change' }],
        interestRateType: [{ required: true, message: '请选择利率类型', trigger: 'change' }],
        termPeriod: [{ required: true, message: '请选择期限', trigger: 'change' }],
        annualRate: [{ required: true, message: '请输入年利率', trigger: 'blur' }]
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
      getInterestRateManageList(this.listQuery).then(response => {
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
        this.listQuery.rateDate = this.formatDate(value)
      } else {
        this.listQuery.rateDate = undefined
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
        currency: '',
        interestRateType: '',
        termPeriod: '',
        annualRate: 0,
        nominalRate: undefined,
        effectiveRate: undefined,
        marketReference: '',
        publishTime: '',
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
          if (this.temp.publishTime) {
            this.temp.publishTime = this.parseDateTime(this.temp.publishTime)
          }
          createInterestRateManage(this.temp).then(response => {
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
          if (this.temp.publishTime) {
            this.temp.publishTime = this.parseDateTime(this.temp.publishTime)
          }
          const tempData = Object.assign({}, this.temp)
          updateInterestRateManage(tempData).then(response => {
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
        deleteInterestRateManage(row.id).then(response => {
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
        batchDeleteInterestRateManage(ids).then(response => {
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
      this.$confirm(`确定要${statusText}该利率吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        toggleInterestRateManageStatus({ id: row.id, isEnabled: status }).then(response => {
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
    handleExport() {
      exportInterestRateManage(this.listQuery).then(response => {
        const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `利率管理_${parseTime(new Date(), '{y}{m}{d}')}.xlsx`
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
    parseDateTime(date) {
      if (!date) return ''
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    },
    formatRate(rate) {
      if (rate === null || rate === undefined) return '-'
      return parseFloat(rate).toFixed(6)
    },
    getInterestRateTypeName(type) {
      const typeMap = {
        'LPR': '贷款市场报价利率',
        'BASE_RATE': '基准利率',
        'REDISCOUNT_RATE': '再贷款利率',
        'LIBOR': '同业拆借利率',
        'DEPOSIT_RATE': '存款利率',
        'LOAN_RATE': '贷款利率'
      }
      return typeMap[type] || type
    },
    getInterestRateTypeTag(type) {
      const tagMap = {
        'LPR': 'primary',
        'BASE_RATE': 'success',
        'REDISCOUNT_RATE': 'warning',
        'LIBOR': 'info',
        'DEPOSIT_RATE': 'danger',
        'LOAN_RATE': 'warning'
      }
      return tagMap[type] || ''
    },
    getTermPeriodName(period) {
      const periodMap = {
        'ON': '隔夜',
        '1W': '1周',
        '1M': '1个月',
        '3M': '3个月',
        '6M': '6个月',
        '9M': '9个月',
        '1Y': '1年',
        '2Y': '2年',
        '3Y': '3年',
        '5Y': '5年'
      }
      return periodMap[period] || period
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