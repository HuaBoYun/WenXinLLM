<template>
  <el-dialog
    :title="isEdit ? '编辑数据源' : '新增数据源'"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      v-loading="loading"
    >
      <el-form-item label="数据源名称" prop="sourceName">
        <el-input
          v-model="form.sourceName"
          placeholder="请输入数据源名称"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="数据源类型" prop="sourceType">
        <el-select
          v-model="form.sourceType"
          placeholder="请选择数据源类型"
          style="width: 100%"
          @change="handleSourceTypeChange"
        >
          <el-option label="达梦数据库" value="DM" />
          <el-option label="Oracle数据库" value="ORACLE" />
          <el-option label="MySQL数据库" value="MYSQL" />
        </el-select>
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="16">
          <el-form-item label="主机IP" prop="hostIp">
            <el-input
              v-model="form.hostIp"
              placeholder="请输入主机IP地址"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="端口" prop="port" label-width="50px">
            <el-input-number
              v-model="form.port"
              :min="1"
              :max="65535"
              placeholder="端口"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="数据库名" prop="databaseName">
        <el-input
          v-model="form.databaseName"
          placeholder="请输入数据库名称"
          maxlength="50"
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="用户名" prop="username">
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              maxlength="200"
              show-password
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio label="ACTIVE">活跃</el-radio>
          <el-radio label="INACTIVE">禁用</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="描述">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入描述信息"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <!-- 连接URL预览 -->
      <el-form-item label="连接URL">
        <el-input
          :value="connectionUrl"
          readonly
          placeholder="连接URL将自动生成"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="info" @click="handleTestConnection" :loading="testLoading">测试连接</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveDataSource, testDataSourceConnection } from '@/api/mxgl'

export default {
  name: 'DataSourceDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      testLoading: false,
      submitLoading: false,
      form: {
        sourceId: '',
        sourceName: '',
        sourceType: '',
        hostIp: '',
        port: null,
        databaseName: '',
        username: '',
        password: '',
        status: 'ACTIVE',
        description: ''
      },
      rules: {
        sourceName: [
          { required: true, message: '请输入数据源名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        sourceType: [
          { required: true, message: '请选择数据源类型', trigger: 'change' }
        ],
        hostIp: [
          { required: true, message: '请输入主机IP', trigger: 'blur' },
          {
            pattern: /^((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)$/,
            message: '请输入正确的IP地址格式',
            trigger: 'blur'
          }
        ],
        port: [
          { required: true, message: '请输入端口号', trigger: 'blur' },
          { type: 'number', min: 1, max: 65535, message: '端口号范围为 1-65535', trigger: 'blur' }
        ],
        databaseName: [
          { required: true, message: '请输入数据库名', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 200, message: '长度在 1 到 200 个字符', trigger: 'blur' }
        ]
      },
      // 默认端口配置
      defaultPorts: {
        'DM': 5236,
        'ORACLE': 1521,
        'MYSQL': 3306
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    // 连接URL预览
    connectionUrl() {
      if (!this.form.sourceType || !this.form.hostIp || !this.form.port || !this.form.databaseName) {
        return ''
      }

      switch (this.form.sourceType) {
        case 'DM':
          return `jdbc:dm://${this.form.hostIp}:${this.form.port}/${this.form.databaseName}`
        case 'ORACLE':
          return `jdbc:oracle:thin:@${this.form.hostIp}:${this.form.port}:${this.form.databaseName}`
        case 'MYSQL':
          return `jdbc:mysql://${this.form.hostIp}:${this.form.port}/${this.form.databaseName}?useUnicode=true&characterEncoding=utf8&useSSL=false`
        default:
          return ''
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      if (this.isEdit && this.formData.sourceId) {
        // 编辑模式
        this.form = {
          sourceId: this.formData.sourceId,
          sourceName: this.formData.sourceName || '',
          sourceType: this.formData.sourceType || '',
          hostIp: this.formData.hostIp || '',
          port: this.formData.port || null,
          databaseName: this.formData.databaseName || '',
          username: this.formData.username || '',
          password: '', // 编辑时密码为空，需要重新输入
          status: this.formData.status || 'ACTIVE',
          description: this.formData.description || ''
        }
      } else {
        // 新增模式
        this.form = {
          sourceId: '',
          sourceName: '',
          sourceType: '',
          hostIp: '',
          port: null,
          databaseName: '',
          username: '',
          password: '',
          status: 'ACTIVE',
          description: ''
        }
      }

      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },

    // 数据源类型变化
    handleSourceTypeChange(sourceType) {
      // 设置默认端口
      if (this.defaultPorts[sourceType]) {
        this.form.port = this.defaultPorts[sourceType]
      }
    },

    // 测试连接
    async handleTestConnection() {
      // 先验证必填字段
      try {
        await this.$refs.form.validateField(['sourceType', 'hostIp', 'port', 'databaseName', 'username', 'password'])
      } catch (error) {
        this.$message.warning('请先完善连接信息')
        return
      }

      this.testLoading = true
      try {
        // 如果是新增，需要先保存再测试
        if (!this.isEdit) {
          this.$message.info('新增数据源需要先保存后才能测试连接')
          return
        }

        const response = await testDataSourceConnection(this.form.sourceId)
        if (response.code === 1) {
          this.$message.success('连接测试成功')
        } else {
          this.$message.error(response.msg || '连接测试失败')
        }
      } catch (error) {
        this.$message.error('连接测试失败')
        console.error('测试连接失败:', error)
      } finally {
        this.testLoading = false
      }
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
      } catch (error) {
        return
      }

      this.submitLoading = true
      try {
        const response = await saveDataSource(this.form)
        if (response.code === 1) {
          this.$message.success(this.isEdit ? '更新成功' : '保存成功')
          this.$emit('success')
          this.handleClose()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败')
        console.error('保存数据源失败:', error)
      } finally {
        this.submitLoading = false
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.loading = false
      this.testLoading = false
      this.submitLoading = false
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}

.el-form-item {
  margin-bottom: 20px;
}
</style>
