<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.sourceCode"
        placeholder="数据源编码"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-input
        v-model="listQuery.sourceName"
        placeholder="数据源名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select
        v-model="listQuery.sourceType"
        placeholder="数据源类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option v-for="item in sourceTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
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
      <el-table-column label="数据源编码" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sourceCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据源名称" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sourceName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据源类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getSourceTypeTag(row.sourceType)">
            {{ getSourceTypeName(row.sourceType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="连接地址" min-width="200px">
        <template slot-scope="{row}">
          <span>{{ row.connectionUrl }}</span>
        </template>
      </el-table-column>
      <el-table-column label="主机地址" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.hostAddress || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="端口" width="80px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.portNumber || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="连接池大小" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag type="info">{{ row.connectionPoolSize || 10 }}</el-tag>
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
      <el-table-column label="操作" align="center" width="280" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button type="info" size="mini" @click="handleView(row)">
            查看
          </el-button>
          <el-button type="warning" size="mini" @click="handleTestConnection(row)">
            测试连接
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
    <el-dialog :title="dialogStatus === 'create' ? '添加数据源配置' : '编辑数据源配置'" :visible.sync="dialogFormVisible" width="60%">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style="width: 90%; margin-left:50px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据源编码" prop="sourceCode">
              <el-input v-model="temp.sourceCode" placeholder="请输入数据源编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据源名称" prop="sourceName">
              <el-input v-model="temp.sourceName" placeholder="请输入数据源名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据源类型" prop="sourceType">
              <el-select v-model="temp.sourceType" placeholder="请选择数据源类型" style="width: 100%" @change="handleSourceTypeChange">
                <el-option v-for="item in sourceTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="驱动类">
              <el-input v-model="temp.driverClass" placeholder="请输入驱动类名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="连接地址" prop="connectionUrl">
          <el-input v-model="temp.connectionUrl" placeholder="请输入连接地址" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主机地址">
              <el-input v-model="temp.hostAddress" placeholder="请输入主机地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="端口">
              <el-input-number v-model="temp.portNumber" :min="1" :max="65535" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据库名称">
              <el-input v-model="temp.databaseName" placeholder="请输入数据库名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="连接池大小">
              <el-input-number v-model="temp.connectionPoolSize" :min="1" :max="100" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="连接超时(ms)">
              <el-input-number v-model="temp.connectionTimeout" :min="1000" :max="300000" style="width: 100%" />
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
            <el-form-item label="用户名">
              <el-input v-model="temp.username" placeholder="请输入用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码">
              <el-input v-model="temp.passwordEncrypted" type="password" placeholder="请输入密码" show-password />
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
        <el-button @click="handleTestConnectionInDialog" :loading="testLoading">
          测试连接
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog title="查看数据源配置详情" :visible.sync="dialogViewVisible" width="60%">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="数据源编码">{{ viewData.sourceCode }}</el-descriptions-item>
        <el-descriptions-item label="数据源名称">{{ viewData.sourceName }}</el-descriptions-item>
        <el-descriptions-item label="数据源类型">
          <el-tag :type="getSourceTypeTag(viewData.sourceType)">
            {{ getSourceTypeName(viewData.sourceType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="驱动类">{{ viewData.driverClass || '-' }}</el-descriptions-item>
        <el-descriptions-item label="连接地址" :span="2">{{ viewData.connectionUrl }}</el-descriptions-item>
        <el-descriptions-item label="主机地址">{{ viewData.hostAddress || '-' }}</el-descriptions-item>
        <el-descriptions-item label="端口">{{ viewData.portNumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="数据库名称">{{ viewData.databaseName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="连接池大小">
          <el-tag type="info">{{ viewData.connectionPoolSize || 10 }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="连接超时">{{ viewData.connectionTimeout || 30000 }}ms</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.isEnabled === 1 ? 'success' : 'info'">
            {{ viewData.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="用户名">{{ viewData.username || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(viewData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(viewData.updateTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogViewVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleTestConnection(viewData)">测试连接</el-button>
      </div>
    </el-dialog>

    <!-- 测试连接结果对话框 -->
    <el-dialog title="连接测试结果" :visible.sync="dialogTestResultVisible" width="40%">
      <div style="text-align: center; padding: 20px;">
        <i v-if="testResult.success" class="el-icon-success" style="font-size: 48px; color: #67C23A;"></i>
        <i v-else class="el-icon-error" style="font-size: 48px; color: #F56C6C;"></i>
        <h3 style="margin-top: 20px;">{{ testResult.success ? '连接成功' : '连接失败' }}</h3>
        <p v-if="testResult.message" style="margin-top: 10px; color: #666;">{{ testResult.message }}</p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTestResultVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDataSourceConfigList, createDataSourceConfig, updateDataSourceConfig, deleteDataSourceConfig, batchDeleteDataSourceConfig, toggleDataSourceConfigStatus, exportDataSourceConfig, testConnection } from '@/api/globalTreasurer/dataSourceConfigManage'
import waves from '@/directive/waves'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'

export default {
  name: 'DataSourceConfigManage',
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
        sourceCode: undefined,
        sourceName: undefined,
        sourceType: undefined,
        isEnabled: undefined
      },
      sourceTypeOptions: [
        { label: 'MySQL', value: 'MYSQL' },
        { label: 'Oracle', value: 'ORACLE' },
        { label: 'PostgreSQL', value: 'POSTGRESQL' },
        { label: 'SQL Server', value: 'SQLSERVER' },
        { label: '达梦数据库', value: 'DM' },
        { label: 'Redis', value: 'REDIS' },
        { label: 'Elasticsearch', value: 'ELASTICSEARCH' },
        { label: 'MongoDB', value: 'MONGODB' }
      ],
      multipleSelection: [],
      temp: {
        id: undefined,
        sourceCode: '',
        sourceName: '',
        sourceType: '',
        connectionUrl: '',
        driverClass: '',
        username: '',
        passwordEncrypted: '',
        databaseName: '',
        portNumber: undefined,
        hostAddress: '',
        connectionPoolSize: 10,
        connectionTimeout: 30000,
        isEnabled: 1,
        orgId: 1,
        remark: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      dialogViewVisible: false,
      dialogTestResultVisible: false,
      viewData: {},
      testResult: {
        success: false,
        message: ''
      },
      testLoading: false,
      rules: {
        sourceCode: [{ required: true, message: '请输入数据源编码', trigger: 'blur' }],
        sourceName: [{ required: true, message: '请输入数据源名称', trigger: 'blur' }],
        sourceType: [{ required: true, message: '请选择数据源类型', trigger: 'change' }],
        connectionUrl: [{ required: true, message: '请输入连接地址', trigger: 'blur' }]
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
      getDataSourceConfigList(this.listQuery).then(response => {
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
        sourceCode: '',
        sourceName: '',
        sourceType: '',
        connectionUrl: '',
        driverClass: '',
        username: '',
        passwordEncrypted: '',
        databaseName: '',
        portNumber: undefined,
        hostAddress: '',
        connectionPoolSize: 10,
        connectionTimeout: 30000,
        isEnabled: 1,
        orgId: 1,
        remark: ''
      }
    },
    handleSourceTypeChange(value) {
      // 根据数据源类型设置默认驱动类和连接地址格式
      const driverClassMap = {
        'MYSQL': 'com.mysql.cj.jdbc.Driver',
        'ORACLE': 'oracle.jdbc.OracleDriver',
        'POSTGRESQL': 'org.postgresql.Driver',
        'SQLSERVER': 'com.microsoft.sqlserver.jdbc.SQLServerDriver',
        'DM': 'dm.jdbc.driver.DmDriver'
      }

      if (driverClassMap[value]) {
        this.temp.driverClass = driverClassMap[value]
      }

      // 可以根据类型设置默认连接地址格式
      if (value === 'MYSQL' && this.temp.hostAddress && this.temp.databaseName) {
        this.temp.connectionUrl = `jdbc:mysql://${this.temp.hostAddress}:${this.temp.portNumber || 3306}/${this.temp.databaseName}?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8`
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
          createDataSourceConfig(this.temp).then(response => {
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
          updateDataSourceConfig(tempData).then(response => {
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
        deleteDataSourceConfig(row.id).then(response => {
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
        batchDeleteDataSourceConfig(ids).then(response => {
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
      this.$confirm(`确定要${statusText}该数据源吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        toggleDataSourceConfigStatus({ id: row.id, isEnabled: status }).then(response => {
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
    handleTestConnection(row) {
      this.testLoading = true
      testConnection({
        sourceType: row.sourceType,
        connectionUrl: row.connectionUrl,
        username: row.username,
        passwordEncrypted: row.passwordEncrypted,
        driverClass: row.driverClass
      }).then(response => {
        this.testResult = response.data
        this.dialogTestResultVisible = true
        this.testLoading = false
      }).catch(() => {
        this.testResult = {
          success: false,
          message: '连接测试失败'
        }
        this.dialogTestResultVisible = true
        this.testLoading = false
      })
    },
    handleTestConnectionInDialog() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.testLoading = true
          testConnection({
            sourceType: this.temp.sourceType,
            connectionUrl: this.temp.connectionUrl,
            username: this.temp.username,
            passwordEncrypted: this.temp.passwordEncrypted,
            driverClass: this.temp.driverClass
          }).then(response => {
            this.testResult = response.data
            this.dialogTestResultVisible = true
            this.testLoading = false
          }).catch(() => {
            this.testResult = {
              success: false,
              message: '连接测试失败'
            }
            this.dialogTestResultVisible = true
            this.testLoading = false
          })
        }
      })
    },
    handleExport() {
      exportDataSourceConfig(this.listQuery).then(response => {
        const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `数据源配置管理_${parseTime(new Date(), '{y}{m}{d}')}.xlsx`
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
    getSourceTypeName(type) {
      const typeMap = {
        'MYSQL': 'MySQL',
        'ORACLE': 'Oracle',
        'POSTGRESQL': 'PostgreSQL',
        'SQLSERVER': 'SQL Server',
        'DM': '达梦数据库',
        'REDIS': 'Redis',
        'ELASTICSEARCH': 'Elasticsearch',
        'MONGODB': 'MongoDB'
      }
      return typeMap[type] || type
    },
    getSourceTypeTag(type) {
      const tagMap = {
        'MYSQL': 'success',
        'ORACLE': 'danger',
        'POSTGRESQL': 'primary',
        'SQLSERVER': 'info',
        'DM': 'warning',
        'REDIS': 'danger',
        'ELASTICSEARCH': 'success',
        'MONGODB': 'primary'
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