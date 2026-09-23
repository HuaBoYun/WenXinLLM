<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.configCode"
        placeholder="配置编码"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-input
        v-model="listQuery.configName"
        placeholder="配置名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
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
      <el-table-column label="配置编码" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.configCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="配置名称" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.configName }}</span>
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
      <el-table-column label="数据来源" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.dataSource || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新频率" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag type="info">{{ getUpdateFrequencyName(row.updateFrequency) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="自动更新" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.autoUpdate === 1 ? 'success' : 'info'">
            {{ row.autoUpdate === 1 ? '是' : '否' }}
          </el-tag>
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
    <el-dialog :title="dialogStatus === 'create' ? '添加汇率配置' : '编辑汇率配置'" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style="width: 90%; margin-left:50px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="配置编码" prop="configCode">
              <el-input v-model="temp.configCode" placeholder="请输入配置编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="配置名称" prop="configName">
              <el-input v-model="temp.configName" placeholder="请输入配置名称" />
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
            <el-form-item label="汇率类型" prop="rateType">
              <el-select v-model="temp.rateType" placeholder="请选择汇率类型" style="width: 100%">
                <el-option v-for="item in rateTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据来源">
              <el-input v-model="temp.dataSource" placeholder="请输入数据来源" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="更新频率">
              <el-select v-model="temp.updateFrequency" placeholder="请选择更新频率" style="width: 100%">
                <el-option v-for="item in updateFrequencyOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
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
            <el-form-item label="自动更新">
              <el-switch
                v-model="temp.autoUpdate"
                :active-value="1"
                :inactive-value="0"
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
    <el-dialog title="查看汇率配置详情" :visible.sync="dialogViewVisible" width="60%">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="配置编码">{{ viewData.configCode }}</el-descriptions-item>
        <el-descriptions-item label="配置名称">{{ viewData.configName }}</el-descriptions-item>
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
        <el-descriptions-item label="数据来源">{{ viewData.dataSource || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新频率">
          <el-tag type="info">{{ getUpdateFrequencyName(viewData.updateFrequency) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="自动更新">
          <el-tag :type="viewData.autoUpdate === 1 ? 'success' : 'info'">
            {{ viewData.autoUpdate === 1 ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.isEnabled === 1 ? 'success' : 'info'">
            {{ viewData.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(viewData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(viewData.updateTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogViewVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getExchangeRateConfigList, createExchangeRateConfig, updateExchangeRateConfig, deleteExchangeRateConfig, batchDeleteExchangeRateConfig, toggleExchangeRateConfigStatus, exportExchangeRateConfig } from '@/api/globalTreasurer/exchangeRateConfigManage'
import waves from '@/directive/waves'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'

export default {
  name: 'ExchangeRateConfigManage',
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
        configCode: undefined,
        configName: undefined,
        baseCurrency: undefined,
        targetCurrency: undefined,
        rateType: undefined,
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
      updateFrequencyOptions: [
        { label: '实时', value: 'REAL_TIME' },
        { label: '每分钟', value: 'MINUTELY' },
        { label: '每小时', value: 'HOURLY' },
        { label: '每日', value: 'DAILY' },
        { label: '每周', value: 'WEEKLY' },
        { label: '每月', value: 'MONTHLY' }
      ],
      multipleSelection: [],
      temp: {
        id: undefined,
        configCode: '',
        configName: '',
        baseCurrency: '',
        targetCurrency: '',
        rateType: 'MIDDLE',
        dataSource: '',
        updateFrequency: 'DAILY',
        autoUpdate: 0,
        isEnabled: 1,
        orgId: 1,
        remark: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      dialogViewVisible: false,
      viewData: {},
      rules: {
        configCode: [{ required: true, message: '请输入配置编码', trigger: 'blur' }],
        configName: [{ required: true, message: '请输入配置名称', trigger: 'blur' }],
        baseCurrency: [{ required: true, message: '请选择基础币种', trigger: 'change' }],
        targetCurrency: [{ required: true, message: '请选择目标币种', trigger: 'change' }],
        rateType: [{ required: true, message: '请选择汇率类型', trigger: 'change' }]
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
      getExchangeRateConfigList(this.listQuery).then(response => {
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
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        configCode: '',
        configName: '',
        baseCurrency: '',
        targetCurrency: '',
        rateType: 'MIDDLE',
        dataSource: '',
        updateFrequency: 'DAILY',
        autoUpdate: 0,
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
          createExchangeRateConfig(this.temp).then(response => {
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
          const tempData = Object.assign({}, this.temp)
          updateExchangeRateConfig(tempData).then(response => {
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
        deleteExchangeRateConfig(row.id).then(response => {
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
        batchDeleteExchangeRateConfig(ids).then(response => {
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
      this.$confirm(`确定要${statusText}该配置吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        toggleExchangeRateConfigStatus({ id: row.id, isEnabled: status }).then(response => {
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
      exportExchangeRateConfig(this.listQuery).then(response => {
        const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `汇率配置管理_${parseTime(new Date(), '{y}{m}{d}')}.xlsx`
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
    },
    getUpdateFrequencyName(frequency) {
      const frequencyMap = {
        'REAL_TIME': '实时',
        'MINUTELY': '每分钟',
        'HOURLY': '每小时',
        'DAILY': '每日',
        'WEEKLY': '每周',
        'MONTHLY': '每月'
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