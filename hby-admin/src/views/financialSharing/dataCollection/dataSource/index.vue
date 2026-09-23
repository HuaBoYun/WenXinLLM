<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="数据源编码">
        <el-input v-model="queryForm.sourceCode" placeholder="请输入数据源编码" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="数据源名称">
        <el-input v-model="queryForm.sourceName" placeholder="请输入数据源名称" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="数据源类型">
        <el-select v-model="queryForm.sourceType" placeholder="请选择数据源类型" clearable style="width: 150px">
          <el-option label="数据库" value="DATABASE" />
          <el-option label="API接口" value="API" />
          <el-option label="文件" value="FILE" />
          <el-option label="财务共享" value="FINANCIAL_SHARING" />
        </el-select>
      </el-form-item>
      <el-form-item label="启用状态">
        <el-select v-model="queryForm.isEnabled" placeholder="请选择启用状态" clearable style="width: 120px">
          <el-option label="启用" value="Y" />
          <el-option label="禁用" value="N" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="dataList" border>
      <el-table-column label="数据源编码" prop="sourceCode" width="150" />
      <el-table-column label="数据源名称" prop="sourceName" width="180" show-overflow-tooltip />
      <el-table-column label="数据源类型" prop="sourceType" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.sourceType === 'DATABASE'" type="primary">数据库</el-tag>
          <el-tag v-else-if="scope.row.sourceType === 'API'" type="success">API接口</el-tag>
          <el-tag v-else-if="scope.row.sourceType === 'FILE'" type="warning">文件</el-tag>
          <el-tag v-else-if="scope.row.sourceType === 'FINANCIAL_SHARING'" type="info">财务共享</el-tag>
          <el-tag v-else>{{ scope.row.sourceType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="连接类型" prop="connectionType" width="100" />
      <el-table-column label="主机地址" prop="host" width="150" show-overflow-tooltip />
      <el-table-column label="端口" prop="port" width="80" align="center" />
      <el-table-column label="数据库名" prop="databaseName" width="120" show-overflow-tooltip />
      <el-table-column label="用户名" prop="username" width="120" show-overflow-tooltip />
      <el-table-column label="启用状态" prop="isEnabled" width="100" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isEnabled"
            active-value="Y"
            inactive-value="N"
            @change="handleToggleEnabled(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="160">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="240">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="warning" @click="handleTest(scope.row)">测试</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px" @close="closeDialog">
      <el-form ref="dataSourceForm" :model="dataSourceForm" :rules="dataSourceRules" label-width="120px">
        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据源编码" prop="sourceCode">
              <el-input v-model="dataSourceForm.sourceCode" placeholder="请输入数据源编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据源名称" prop="sourceName">
              <el-input v-model="dataSourceForm.sourceName" placeholder="请输入数据源名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据源类型" prop="sourceType">
              <el-select v-model="dataSourceForm.sourceType" placeholder="请选择数据源类型" style="width: 100%" @change="handleSourceTypeChange">
                <el-option label="数据库" value="DATABASE" />
                <el-option label="API接口" value="API" />
                <el-option label="文件" value="FILE" />
                <el-option label="财务共享" value="FINANCIAL_SHARING" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="连接类型" prop="connectionType">
              <el-select v-model="dataSourceForm.connectionType" placeholder="请选择连接类型" style="width: 100%">
                <el-option label="JDBC" value="JDBC" />
                <el-option label="HTTP" value="HTTP" />
                <el-option label="FTP" value="FTP" />
                <el-option label="SFTP" value="SFTP" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 数据库配置 -->
        <div v-if="dataSourceForm.sourceType === 'DATABASE'">
          <el-divider content-position="left">数据库配置</el-divider>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="主机地址" prop="host">
                <el-input v-model="dataSourceForm.host" placeholder="请输入主机地址" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="端口" prop="port">
                <el-input v-model.number="dataSourceForm.port" placeholder="请输入端口" type="number" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="数据库名" prop="databaseName">
                <el-input v-model="dataSourceForm.databaseName" placeholder="请输入数据库名" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="dataSourceForm.username" placeholder="请输入用户名" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="密码" prop="password">
                <el-input v-model="dataSourceForm.password" type="password" placeholder="请输入密码" show-password />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="连接池大小">
                <el-input v-model.number="dataSourceForm.connectionPoolSize" placeholder="默认10" type="number" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- API配置 -->
        <div v-if="dataSourceForm.sourceType === 'API' || dataSourceForm.sourceType === 'FINANCIAL_SHARING'">
          <el-divider content-position="left">API配置</el-divider>
          <el-form-item label="API地址" prop="apiUrl">
            <el-input v-model="dataSourceForm.apiUrl" placeholder="请输入API地址" />
          </el-form-item>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="请求方法">
                <el-select v-model="dataSourceForm.apiMethod" placeholder="请选择请求方法" style="width: 100%">
                  <el-option label="GET" value="GET" />
                  <el-option label="POST" value="POST" />
                  <el-option label="PUT" value="PUT" />
                  <el-option label="DELETE" value="DELETE" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="认证类型">
                <el-select v-model="dataSourceForm.authType" placeholder="请选择认证类型" style="width: 100%">
                  <el-option label="无" value="NONE" />
                  <el-option label="Basic" value="BASIC" />
                  <el-option label="Token" value="TOKEN" />
                  <el-option label="OAuth2" value="OAUTH2" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="认证令牌">
            <el-input v-model="dataSourceForm.authToken" type="textarea" :rows="2" placeholder="请输入认证令牌" />
          </el-form-item>
          <el-form-item label="请求头">
            <el-input v-model="dataSourceForm.apiHeaders" type="textarea" :rows="3" placeholder="请输入请求头（JSON格式）" />
          </el-form-item>
        </div>

        <!-- 文件配置 -->
        <div v-if="dataSourceForm.sourceType === 'FILE'">
          <el-divider content-position="left">文件配置</el-divider>
          <el-form-item label="文件路径" prop="filePath">
            <el-input v-model="dataSourceForm.filePath" placeholder="请输入文件路径" />
          </el-form-item>
          <el-form-item label="文件类型">
            <el-select v-model="dataSourceForm.fileType" placeholder="请选择文件类型" style="width: 100%">
              <el-option label="Excel" value="EXCEL" />
              <el-option label="CSV" value="CSV" />
              <el-option label="XML" value="XML" />
              <el-option label="JSON" value="JSON" />
            </el-select>
          </el-form-item>
        </div>

        <!-- 其他配置 -->
        <el-divider content-position="left">其他配置</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="超时时间(秒)">
              <el-input v-model.number="dataSourceForm.timeout" placeholder="默认30秒" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用">
              <el-switch v-model="dataSourceForm.isEnabled" active-value="Y" inactive-value="N" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import {
  queryDataSourcePage,
  queryDataSourceById,
  saveDataSource,
  deleteDataSource,
  toggleDataSourceEnabled,
  testDataSourceConnection
} from '@/api/financialSharing/dataCollection'

export default {
  name: 'DataSource',
  components: { Pagination },
  data() {
    return {
      loading: false,
      dataList: [],
      total: 0,
      queryForm: {
        sourceCode: '',
        sourceName: '',
        sourceType: '',
        isEnabled: '',
        pageNumber: 1,
        pageSize: 10
      },
      dialogVisible: false,
      dialogTitle: '',
      dataSourceForm: {
        sourceId: null,
        sourceCode: '',
        sourceName: '',
        sourceType: '',
        connectionType: '',
        host: '',
        port: null,
        databaseName: '',
        username: '',
        password: '',
        apiUrl: '',
        apiMethod: 'GET',
        apiHeaders: '',
        authType: 'NONE',
        authToken: '',
        filePath: '',
        fileType: '',
        connectionPoolSize: 10,
        timeout: 30,
        isEnabled: 'Y'
      },
      dataSourceRules: {
        sourceCode: [
          { required: true, message: '请输入数据源编码', trigger: 'blur' }
        ],
        sourceName: [
          { required: true, message: '请输入数据源名称', trigger: 'blur' }
        ],
        sourceType: [
          { required: true, message: '请选择数据源类型', trigger: 'change' }
        ]
      }
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    // 获取列表数据
    getList() {
      this.loading = true
      queryDataSourcePage(this.queryForm).then(response => {
        if (response.code === 1) {
          this.dataList = response.data.records || []
          this.total = response.data.total || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },
    // 重置
    handleReset() {
      this.queryForm = {
        sourceCode: '',
        sourceName: '',
        sourceType: '',
        isEnabled: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.getList()
    },
    // 新增
    handleAdd() {
      this.dialogTitle = '新增数据源配置'
      this.dialogVisible = true
      this.dataSourceForm = {
        sourceId: null,
        sourceCode: '',
        sourceName: '',
        sourceType: '',
        connectionType: '',
        host: '',
        port: null,
        databaseName: '',
        username: '',
        password: '',
        apiUrl: '',
        apiMethod: 'GET',
        apiHeaders: '',
        authType: 'NONE',
        authToken: '',
        filePath: '',
        fileType: '',
        connectionPoolSize: 10,
        timeout: 30,
        isEnabled: 'Y'
      }
      this.$nextTick(() => {
        if (this.$refs.dataSourceForm) {
          this.$refs.dataSourceForm.clearValidate()
        }
      })
    },
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑数据源配置'
      queryDataSourceById({ sourceId: row.sourceId }).then(response => {
        if (response.code === 1) {
          this.dataSourceForm = { ...response.data }
          this.dialogVisible = true
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      })
    },
    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该数据源配置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDataSource({ sourceId: row.sourceId }).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    // 启用/禁用
    handleToggleEnabled(row) {
      const action = row.isEnabled === 'Y' ? '启用' : '禁用'
      toggleDataSourceEnabled({
        sourceId: row.sourceId,
        isEnabled: row.isEnabled
      }).then(response => {
        if (response.code === 1) {
          this.$message.success(action + '成功')
          this.getList()
        } else {
          this.$message.error(response.msg || action + '失败')
          // 恢复原状态
          row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
        }
      }).catch(() => {
        // 恢复原状态
        row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
      })
    },
    // 测试连接
    handleTest(row) {
      this.$confirm('确定要测试该数据源连接吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        testDataSourceConnection({ sourceId: row.sourceId }).then(response => {
          if (response.code === 1) {
            this.$alert(response.data, '测试结果', {
              confirmButtonText: '确定',
              type: 'success'
            })
          } else {
            this.$message.error(response.msg || '测试失败')
          }
        })
      }).catch(() => {})
    },
    // 数据源类型变化
    handleSourceTypeChange(value) {
      // 根据数据源类型设置默认连接类型
      if (value === 'DATABASE') {
        this.dataSourceForm.connectionType = 'JDBC'
      } else if (value === 'API' || value === 'FINANCIAL_SHARING') {
        this.dataSourceForm.connectionType = 'HTTP'
      } else if (value === 'FILE') {
        this.dataSourceForm.connectionType = 'FTP'
      }
    },
    // 提交表单
    submitForm() {
      this.$refs.dataSourceForm.validate(valid => {
        if (valid) {
          saveDataSource(this.dataSourceForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '保存成功')
              this.dialogVisible = false
              this.getList()
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          })
        }
      })
    },
    // 关闭对话框
    closeDialog() {
      this.$refs.dataSourceForm.resetFields()
    },
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return ''
      const date = new Date(dateTime)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.query-form {
  background: #f5f5f5;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.el-table {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}

.el-divider {
  margin: 20px 0;
}
</style>

