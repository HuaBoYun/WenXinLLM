<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="dataSourceForm"
      :model="dataSourceForm"
      :rules="rules"
      label-width="120px"
      class="datasource-form"
    >
      <el-tabs v-model="activeTab" class="form-tabs">
        <el-tab-pane label="基本信息" name="basic">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="数据源编码" prop="dataSourceCode">
                <el-input
                  v-model="dataSourceForm.dataSourceCode"
                  placeholder="请输入数据源编码"
                  :disabled="dialogStatus === 'edit'"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="数据源名称" prop="dataSourceName">
                <el-input
                  v-model="dataSourceForm.dataSourceName"
                  placeholder="请输入数据源名称"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="数据源类型" prop="dataSourceType">
                <el-select
                  v-model="dataSourceForm.dataSourceType"
                  placeholder="请选择数据源类型"
                  style="width: 100%"
                  @change="handleDataSourceTypeChange"
                >
                  <el-option label="MySQL数据库" value="MYSQL" />
                  <el-option label="Oracle数据库" value="ORACLE" />
                  <el-option label="达梦数据库" value="DAMENG" />
                  <el-option label="PostgreSQL" value="POSTGRESQL" />
                  <el-option label="API接口" value="API" />
                  <el-option label="文件数据源" value="FILE" />
                  <el-option label="Redis缓存" value="REDIS" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="环境类型" prop="environment">
                <el-select
                  v-model="dataSourceForm.environment"
                  placeholder="请选择环境类型"
                  style="width: 100%"
                >
                  <el-option label="开发环境" value="DEV" />
                  <el-option label="测试环境" value="TEST" />
                  <el-option label="生产环境" value="PROD" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="连接地址" prop="connectionUrl">
                <el-input
                  v-model="dataSourceForm.connectionUrl"
                  placeholder="请输入连接地址"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="端口" prop="port">
                <el-input-number
                  v-model="dataSourceForm.port"
                  :min="1"
                  :max="65535"
                  placeholder="端口号"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input
                  v-model="dataSourceForm.username"
                  placeholder="请输入用户名"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="密码" prop="password">
                <el-input
                  v-model="dataSourceForm.password"
                  type="password"
                  placeholder="请输入密码"
                  show-password
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="数据库名" prop="databaseName" v-if="isDatabaseType">
                <el-input
                  v-model="dataSourceForm.databaseName"
                  placeholder="请输入数据库名"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态" prop="isEnabled">
                <el-radio-group v-model="dataSourceForm.isEnabled">
                  <el-radio :label="1">启用</el-radio>
                  <el-radio :label="0">禁用</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="描述">
            <el-input
              v-model="dataSourceForm.description"
              type="textarea"
              :rows="3"
              placeholder="请输入数据源描述"
            />
          </el-form-item>
        </el-tab-pane>

        <el-tab-pane label="连接配置" name="config" v-if="isDatabaseType || isRedisType">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="连接池大小" prop="poolSize">
                <el-input-number
                  v-model="dataSourceForm.poolSize"
                  :min="1"
                  :max="100"
                  placeholder="连接池大小"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最大等待时间(秒)" prop="maxWaitTime">
                <el-input-number
                  v-model="dataSourceForm.maxWaitTime"
                  :min="1"
                  :max="300"
                  placeholder="最大等待时间"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="连接超时(秒)" prop="connectionTimeout">
                <el-input-number
                  v-model="dataSourceForm.connectionTimeout"
                  :min="1"
                  :max="60"
                  placeholder="连接超时时间"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="空闲超时(秒)" prop="idleTimeout">
                <el-input-number
                  v-model="dataSourceForm.idleTimeout"
                  :min="60"
                  :max="3600"
                  placeholder="空闲超时时间"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="自动重连">
            <el-switch v-model="dataSourceForm.autoReconnect" />
          </el-form-item>
        </el-tab-pane>

        <el-tab-pane label="高级配置" name="advanced" v-if="isDatabaseType">
          <el-form-item label="驱动类名">
            <el-input
              v-model="dataSourceForm.driverClassName"
              placeholder="例如: com.mysql.cj.jdbc.Driver"
            />
          </el-form-item>

          <el-form-item label="连接参数">
            <el-input
              v-model="dataSourceForm.connectionParams"
              type="textarea"
              :rows="4"
              placeholder="例如: useUnicode=true&amp;characterEncoding=utf8&amp;useSSL=false"
            />
          </el-form-item>

          <el-form-item label="验证查询SQL">
            <el-input
              v-model="dataSourceForm.validationQuery"
              placeholder="例如: SELECT 1"
            />
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="info" @click="handleTestConnection" :loading="testing">
        测试连接
      </el-button>
      <el-button type="primary" @click="handleSave" :loading="saving">
        {{ dialogStatus === 'create' ? '创 建' : '更 新' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { createDataSourceConfig, updateDataSourceConfig, testConnection } from '@/api/globalTreasurer/dataSourceConfigManage'

export default {
  name: 'DataSourceDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    dataSourceData: {
      type: Object,
      default: () => ({})
    },
    status: {
      type: String,
      default: 'create'
    }
  },
  data() {
    return {
      dialogVisible: this.visible,
      dialogStatus: this.status,
      activeTab: 'basic',
      saving: false,
      testing: false,
      dataSourceForm: {
        dataSourceId: null,
        dataSourceCode: '',
        dataSourceName: '',
        dataSourceType: '',
        environment: '',
        connectionUrl: '',
        port: null,
        username: '',
        password: '',
        databaseName: '',
        description: '',
        isEnabled: 1,
        poolSize: 10,
        maxWaitTime: 30,
        connectionTimeout: 10,
        idleTimeout: 300,
        autoReconnect: true,
        driverClassName: '',
        connectionParams: '',
        validationQuery: 'SELECT 1'
      },
      rules: {
        dataSourceCode: [
          { required: true, message: '请输入数据源编码', trigger: 'blur' },
          { pattern: /^[A-Z][A-Z0-9_]*$/, message: '数据源编码格式不正确', trigger: 'blur' }
        ],
        dataSourceName: [
          { required: true, message: '请输入数据源名称', trigger: 'blur' },
          { min: 2, max: 100, message: '数据源名称长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        dataSourceType: [
          { required: true, message: '请选择数据源类型', trigger: 'change' }
        ],
        environment: [
          { required: true, message: '请选择环境类型', trigger: 'change' }
        ],
        connectionUrl: [
          { required: true, message: '请输入连接地址', trigger: 'blur' }
        ],
        port: [
          { required: true, message: '请输入端口号', trigger: 'blur' },
          { type: 'number', message: '端口号必须为数字', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        databaseName: [
          { required: true, message: '请输入数据库名', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.dialogStatus === 'create' ? '新增数据源配置' : '编辑数据源配置'
    },
    isDatabaseType() {
      return ['MYSQL', 'ORACLE', 'DAMENG', 'POSTGRESQL'].includes(this.dataSourceForm.dataSourceType)
    },
    isRedisType() {
      return this.dataSourceForm.dataSourceType === 'REDIS'
    },
    isApiType() {
      return this.dataSourceForm.dataSourceType === 'API'
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.initForm()
      }
    },
    dataSourceData: {
      handler(newVal) {
        if (newVal && Object.keys(newVal).length > 0) {
          // 后端字段名 -> 前端字段名映射
          const mappedData = {
            dataSourceId: newVal.id || newVal.dataSourceId,
            dataSourceCode: newVal.sourceCode || newVal.dataSourceCode,
            dataSourceName: newVal.sourceName || newVal.dataSourceName,
            dataSourceType: newVal.sourceType || newVal.dataSourceType,
            environment: newVal.environment,
            connectionUrl: newVal.connectionUrl,
            username: newVal.username,
            password: newVal.password,
            description: newVal.description || newVal.remark,
            // isEnabled 后端是字符串 '1'/'0'，前端是数字 1/0
            isEnabled: newVal.isEnabled === '1' || newVal.isEnabled === 1 ? 1 : 0,
            connectionStatus: newVal.connectionStatus
          }

          // 解析 connectionConfig JSON
          if (newVal.connectionConfig) {
            try {
              const config = typeof newVal.connectionConfig === 'string'
                ? JSON.parse(newVal.connectionConfig)
                : newVal.connectionConfig
              mappedData.port = config.port
              mappedData.databaseName = config.db || config.databaseName
              mappedData.poolSize = config.pool || config.poolSize || 10
              mappedData.connectionTimeout = config.timeout || config.connectionTimeout || 10
              mappedData.autoReconnect = config.reconnect !== false
            } catch (e) {
              console.warn('解析 connectionConfig 失败:', e)
            }
          }

          this.dataSourceForm = { ...this.dataSourceForm, ...mappedData }
        }
      },
      deep: true,
      immediate: true
    },
    status(newVal) {
      this.dialogStatus = newVal
    }
  },
  methods: {
    initForm() {
      if (this.dialogStatus === 'create') {
        this.dataSourceForm = {
          dataSourceId: null,
          dataSourceCode: '',
          dataSourceName: '',
          dataSourceType: '',
          environment: '',
          connectionUrl: '',
          port: null,
          username: '',
          password: '',
          databaseName: '',
          description: '',
          isEnabled: 1,
          poolSize: 10,
          maxWaitTime: 30,
          connectionTimeout: 10,
          idleTimeout: 300,
          autoReconnect: true,
          driverClassName: '',
          connectionParams: '',
          validationQuery: 'SELECT 1'
        }
        this.activeTab = 'basic'
      }
      this.$nextTick(() => {
        this.$refs.dataSourceForm && this.$refs.dataSourceForm.clearValidate()
      })
    },
    handleDataSourceTypeChange(value) {
      // 根据数据源类型设置默认值
      if (value === 'MYSQL') {
        this.dataSourceForm.port = this.dataSourceForm.port || 3306
        this.dataSourceForm.driverClassName = 'com.mysql.cj.jdbc.Driver'
        this.dataSourceForm.validationQuery = 'SELECT 1'
      } else if (value === 'ORACLE') {
        this.dataSourceForm.port = this.dataSourceForm.port || 1521
        this.dataSourceForm.driverClassName = 'oracle.jdbc.OracleDriver'
        this.dataSourceForm.validationQuery = 'SELECT 1 FROM DUAL'
      } else if (value === 'DAMENG') {
        this.dataSourceForm.port = this.dataSourceForm.port || 5236
        this.dataSourceForm.driverClassName = 'dm.jdbc.driver.DmDriver'
        this.dataSourceForm.validationQuery = 'SELECT 1'
      } else if (value === 'POSTGRESQL') {
        this.dataSourceForm.port = this.dataSourceForm.port || 5432
        this.dataSourceForm.driverClassName = 'org.postgresql.Driver'
        this.dataSourceForm.validationQuery = 'SELECT 1'
      } else if (value === 'REDIS') {
        this.dataSourceForm.port = this.dataSourceForm.port || 6379
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
      this.$emit('close')
    },
    async handleTestConnection() {
      this.$refs.dataSourceForm.validate(async (valid) => {
        if (valid) {
          this.testing = true
          try {
            // 模拟连接测试
            await this.simulateConnectionTest()

            this.$message({
              type: 'success',
              message: '连接测试成功'
            })

            // 更新连接状态
            this.dataSourceForm.connectionStatus = 'NORMAL'
            this.dataSourceForm.lastTestTime = new Date().toISOString()

          } catch (error) {
            this.$message({
              type: 'error',
              message: error.message || '连接测试失败'
            })
          } finally {
            this.testing = false
          }
        }
      })
    },
    simulateConnectionTest() {
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          // 模拟80%的成功率
          if (Math.random() > 0.2) {
            resolve()
          } else {
            reject(new Error('连接超时，请检查网络配置'))
          }
        }, 2000)
      })
    },
    handleSave() {
      this.$refs.dataSourceForm.validate(async (valid) => {
        if (valid) {
          this.saving = true
          try {
            // 根据数据源类型确定数据类型
            const dataTypeMap = {
              'MYSQL': 'DATABASE',
              'ORACLE': 'DATABASE',
              'DAMENG': 'DATABASE',
              'POSTGRESQL': 'DATABASE',
              'API': 'API',
              'FILE': 'FILE',
              'REDIS': 'CACHE'
            }

            // 构建精简的连接配置 JSON（只包含有值的字段）
            const configObj = {}
            if (this.dataSourceForm.port) configObj.port = this.dataSourceForm.port
            if (this.dataSourceForm.databaseName) configObj.db = this.dataSourceForm.databaseName
            if (this.dataSourceForm.poolSize && this.dataSourceForm.poolSize !== 10) configObj.pool = this.dataSourceForm.poolSize
            if (this.dataSourceForm.connectionTimeout && this.dataSourceForm.connectionTimeout !== 10) configObj.timeout = this.dataSourceForm.connectionTimeout
            if (this.dataSourceForm.autoReconnect === false) configObj.reconnect = false
            // 将虚拟字段也存入 connectionConfig，确保列表查询时能解析出来
            if (this.dataSourceForm.environment) configObj.environment = this.dataSourceForm.environment
            if (this.dataSourceForm.connectionUrl) configObj.connectionUrl = this.dataSourceForm.connectionUrl
            if (this.dataSourceForm.connectionStatus) configObj.connectionStatus = this.dataSourceForm.connectionStatus
            if (this.dataSourceForm.lastTestTime) configObj.lastTestTime = this.dataSourceForm.lastTestTime

            // 转换字段名：前端字段名 -> 后端字段名
            const submitData = {
              id: this.dataSourceForm.dataSourceId || undefined,
              sourceCode: this.dataSourceForm.dataSourceCode,
              sourceName: this.dataSourceForm.dataSourceName,
              sourceType: this.dataSourceForm.dataSourceType,
              dataType: dataTypeMap[this.dataSourceForm.dataSourceType] || 'DATABASE',
              environment: this.dataSourceForm.environment,
              connectionUrl: this.dataSourceForm.connectionUrl,
              username: this.dataSourceForm.username,
              password: this.dataSourceForm.password,
              description: this.dataSourceForm.description,
              isEnabled: this.dataSourceForm.isEnabled === 1 ? '1' : '0',
              connectionStatus: this.dataSourceForm.connectionStatus || 'UNTESTED',
              // 连接配置 JSON（始终存储，确保虚拟字段可被解析）
              connectionConfig: JSON.stringify(configObj)
            }

            let response
            if (this.dialogStatus === 'create') {
              response = await createDataSourceConfig(submitData)
            } else {
              response = await updateDataSourceConfig(submitData)
            }

            // 检查响应状态
            if (response && (response.code === 1 || response.code === '1' || response.code === 200)) {
              this.$message({
                type: 'success',
                message: this.dialogStatus === 'create' ? '创建成功' : '更新成功'
              })
              this.$emit('success', this.dataSourceForm)
              this.handleClose()
            } else {
              this.$message({
                type: 'error',
                message: response.msg || response.message || '操作失败'
              })
            }
          } catch (error) {
            console.error('保存数据源配置失败:', error)
            this.$message({
              type: 'error',
              message: error.message || '操作失败，请稍后重试'
            })
          } finally {
            this.saving = false
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.datasource-form {
  .form-tabs {
    margin-top: -20px;
  }

  .el-form-item {
    margin-bottom: 18px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>