<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <el-form :inline="true" :model="queryParams" size="small" label-width="100px">
          <el-form-item label="配置名称">
            <el-input v-model="queryParams.configName" placeholder="请输入配置名称" clearable style="width: 200px" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="配置代码">
            <el-input v-model="queryParams.configCode" placeholder="请输入配置代码" clearable style="width: 200px" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="数据源类型">
            <el-select v-model="queryParams.dataSourceType" placeholder="请选择数据源类型" clearable style="width: 200px">
              <el-option v-for="item in dataSourceTypes" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.isEnabled" placeholder="请选择状态" clearable style="width: 120px">
              <el-option label="启用" :value="true" />
              <el-option label="禁用" :value="false" />
            </el-select>
          </el-form-item>
          <el-button type="primary" size="small" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          <el-button style="margin-left: 10px" size="small" icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form>
      </div>
      <div style="margin-top: 20px">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增数据源</el-button>
        <el-button type="success" size="small" icon="el-icon-download" @click="handleImport">导入</el-button>
        <el-button type="warning" size="small" icon="el-icon-upload2" @click="handleExport">导出</el-button>
        <el-button :disabled="multiple" type="danger" size="small" icon="el-icon-delete" @click="handleBatchDelete">批量删除</el-button>
      </div>
    </el-card>

    <el-card class="table-container" shadow="never">
      <el-table
        v-loading="listLoading"
        :data="list"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="配置名称" prop="configName" min-width="120" show-overflow-tooltip />
        <el-table-column label="配置代码" prop="configCode" width="150" />
        <el-table-column label="数据源类型" prop="dataSourceType" width="120" />
        <el-table-column label="连接信息" prop="connectionUrl" min-width="200" show-overflow-tooltip />
        <el-table-column label="表名/文件路径" prop="tableName" min-width="150" show-overflow-tooltip />
        <el-table-column label="测试状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isTested" type="success">已测试</el-tag>
            <el-tag v-else type="info">未测试</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="160" align="center" />
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button type="text" size="small" icon="el-icon-connection" @click="handleTest(scope.row)">测试</el-button>
            <el-button type="text" size="small" icon="el-icon-data-line" @click="handlePreview(scope.row)">预览</el-button>
            <el-button type="text" size="small" icon="el-icon-delete" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="queryParams.pageNo"
          :page-sizes="[10, 20, 50, 100]"
          :page-size.sync="queryParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="配置名称" prop="configName">
              <el-input v-model="form.configName" placeholder="请输入配置名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="配置代码" prop="configCode">
              <el-input v-model="form.configCode" placeholder="请输入配置代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据源类型" prop="dataSourceType">
              <el-select v-model="form.dataSourceType" placeholder="请选择数据源类型" @change="handleDataSourceTypeChange">
                <el-option v-for="item in dataSourceTypes" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用">
              <el-switch v-model="form.isEnabled" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 数据库类型配置 -->
        <div v-if="['MYSQL', 'ORACLE', 'DM', 'POSTGRESQL'].includes(form.dataSourceType)">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="连接URL" prop="connectionUrl">
                <el-input v-model="form.connectionUrl" placeholder="请输入数据库连接URL" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" placeholder="请输入用户名" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="驱动类" prop="driverClass">
                <el-input v-model="form.driverClass" placeholder="请输入驱动类名" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="表名" prop="tableName">
                <el-input v-model="form.tableName" placeholder="请输入表名" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="自定义SQL">
                <el-input v-model="form.sqlQuery" type="textarea" rows="3" placeholder="可选：自定义SQL查询语句" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- API类型配置 -->
        <div v-if="form.dataSourceType === 'API'">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="API地址" prop="apiUrl">
                <el-input v-model="form.apiUrl" placeholder="请输入API地址" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="请求方法" prop="apiMethod">
                <el-select v-model="form.apiMethod" placeholder="请选择请求方法">
                  <el-option label="GET" value="GET" />
                  <el-option label="POST" value="POST" />
                  <el-option label="PUT" value="PUT" />
                  <el-option label="DELETE" value="DELETE" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="请求头">
                <el-input v-model="form.apiHeaders" type="textarea" rows="3" placeholder="JSON格式的请求头" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="请求参数">
                <el-input v-model="form.apiParams" type="textarea" rows="3" placeholder="JSON格式的请求参数" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 文件类型配置 -->
        <div v-if="['CSV', 'EXCEL', 'TXT', 'JSON'].includes(form.dataSourceType)">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="文件路径" prop="filePath">
                <el-input v-model="form.filePath" placeholder="请输入文件路径" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="文件编码">
                <el-select v-model="form.fileEncoding" placeholder="请选择文件编码">
                  <el-option label="UTF-8" value="UTF-8" />
                  <el-option label="GBK" value="GBK" />
                  <el-option label="GB2312" value="GB2312" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.dataSourceType === 'CSV' || form.dataSourceType === 'TXT'">
              <el-form-item label="分隔符">
                <el-input v-model="form.delimiter" placeholder="请输入分隔符" style="width: 100px" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述">
              <el-input v-model="form.description" type="textarea" rows="2" placeholder="请输入描述" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog title="导入数据源配置" :visible.sync="importDialogVisible" width="500px">
      <el-upload
        class="upload-demo"
        drag
        action=""
        :auto-upload="false"
        :on-change="handleFileChange"
        :file-list="fileList"
        accept=".json"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传json文件，且不超过10mb</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleImportSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDataSourceList, getDataSourceConfig, saveDataSourceConfig, updateDataSourceConfig, deleteDataSourceConfig, testDataSourceConnection, previewDataSourceData, getDataSourceTypes, batchOperationDataSource } from '@/api/cwgx/dataSource'

export default {
  name: 'DataSourceConfig',
  data() {
    return {
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        configName: '',
        configCode: '',
        dataSourceType: '',
        isEnabled: null
      },
      list: [],
      total: 0,
      listLoading: true,
      multiple: true,
      multipleSelection: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {},
      rules: {
        configName: [
          { required: true, message: '请输入配置名称', trigger: 'blur' }
        ],
        configCode: [
          { required: true, message: '请输入配置代码', trigger: 'blur' }
        ],
        dataSourceType: [
          { required: true, message: '请选择数据源类型', trigger: 'change' }
        ]
      },
      dataSourceTypes: [],
      importDialogVisible: false,
      fileList: []
    }
  },
  created() {
    this.getList()
    this.getDataSourceTypes()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const res = await getDataSourceList(this.queryParams)
        if (res.code === 1) {
          this.list = res.data.tlist || []
          this.total = res.data.totalRecord || 0
        } else {
          this.$message.error(res.message || '获取数据失败')
        }
      } catch (error) {
        console.error(error)
        this.$message.error('获取数据失败')
      }
      this.listLoading = false
    },
    async getDataSourceTypes() {
      try {
        const res = await getDataSourceTypes()
        if (res.code === 1) {
          this.dataSourceTypes = res.data || []
        }
      } catch (error) {
        console.error(error)
      }
    },
    handleQuery() {
      this.queryParams.pageNo = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        pageNo: 1,
        pageSize: 10,
        configName: '',
        configCode: '',
        dataSourceType: '',
        isEnabled: null
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
      this.multiple = !val.length
    },
    handleAdd() {
      this.dialogTitle = '新增数据源'
      this.form = {
        isEnabled: true,
        fileEncoding: 'UTF-8',
        delimiter: ',',
        apiMethod: 'GET'
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    async handleView(row) {
      try {
        const res = await getDataSourceConfig(row.configId)
        if (res.code === 1) {
          this.form = res.data
          this.dialogTitle = '查看数据源'
          this.dialogVisible = true
        }
      } catch (error) {
        console.error(error)
        this.$message.error('获取详情失败')
      }
    },
    async handleUpdate(row) {
      try {
        const res = await getDataSourceConfig(row.configId)
        if (res.code === 1) {
          this.form = res.data
          this.dialogTitle = '编辑数据源'
          this.dialogVisible = true
          this.$nextTick(() => {
            this.$refs.form && this.$refs.form.clearValidate()
          })
        }
      } catch (error) {
        console.error(error)
        this.$message.error('获取详情失败')
      }
    },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            let res
            if (this.form.configId) {
              res = await updateDataSourceConfig(this.form.configId, this.form)
            } else {
              res = await saveDataSourceConfig(this.form)
            }
            if (res.code === 1) {
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.getList()
            } else {
              this.$message.error(res.message || '保存失败')
            }
          } catch (error) {
            console.error(error)
            this.$message.error('保存失败')
          }
        }
      })
    },
    async handleDelete(row) {
      this.$confirm('确认删除该数据源配置吗？', '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteDataSourceConfig(row.configId)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(res.message || '删除失败')
          }
        } catch (error) {
          console.error(error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    async handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm(`确认删除选中的${this.multipleSelection.length}条数据吗？`, '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          const configIds = this.multipleSelection.map(item => item.configId)
          const res = await batchOperationDataSource({
            operationType: 'DELETE',
            configIds: configIds
          })
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(res.message || '删除失败')
          }
        } catch (error) {
          console.error(error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    async handleTest(row) {
      try {
        const res = await testDataSourceConnection(row)
        if (res.code === 1) {
          this.$message.success('连接测试成功')
          this.getList()
        } else {
          this.$message.error(res.message || '连接测试失败')
        }
      } catch (error) {
        console.error(error)
        this.$message.error('连接测试失败')
      }
    },
    async handlePreview(row) {
      try {
        const res = await previewDataSourceData(row)
        if (res.code === 1) {
          // 显示预览数据对话框
          this.$alert(JSON.stringify(res.data, null, 2), '数据预览', {
            confirmButtonText: '确定'
          })
        } else {
          this.$message.error(res.message || '获取预览数据失败')
        }
      } catch (error) {
        console.error(error)
        this.$message.error('获取预览数据失败')
      }
    },
    async handleStatusChange(row) {
      try {
        const res = await batchOperationDataSource({
          operationType: row.isEnabled ? 'ENABLE' : 'DISABLE',
          configIds: [row.configId]
        })
        if (res.code !== 1) {
          row.isEnabled = !row.isEnabled
          this.$message.error(res.message || '状态更新失败')
        }
      } catch (error) {
        row.isEnabled = !row.isEnabled
        console.error(error)
        this.$message.error('状态更新失败')
      }
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNo = val
      this.getList()
    },
    handleDataSourceTypeChange(value) {
      // 根据数据源类型设置默认值
      if (value === 'MYSQL') {
        this.form.driverClass = 'com.mysql.cj.jdbc.Driver'
      } else if (value === 'ORACLE') {
        this.form.driverClass = 'oracle.jdbc.OracleDriver'
      } else if (value === 'DM') {
        this.form.driverClass = 'dm.jdbc.driver.DmDriver'
      } else if (value === 'POSTGRESQL') {
        this.form.driverClass = 'org.postgresql.Driver'
      }
    },
    handleImport() {
      this.importDialogVisible = true
      this.fileList = []
    },
    handleExport() {
      this.$message.info('导出功能开发中...')
    },
    handleFileChange(file) {
      this.fileList = [file]
    },
    handleImportSubmit() {
      if (this.fileList.length === 0) {
        this.$message.warning('请选择要导入的文件')
        return
      }
      this.$message.info('导入功能开发中...')
    }
  }
}
</script>

<style scoped>
.filter-container {
  margin-bottom: 20px;
}

.table-container {
  margin-bottom: 20px;
}

.pagination-container {
  text-align: right;
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>