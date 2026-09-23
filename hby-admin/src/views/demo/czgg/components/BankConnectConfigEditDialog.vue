<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="dataForm"
      :rules="rules"
      :model="temp"
      label-position="left"
      label-width="120px"
      style="width: 800px; margin-left:50px;"
    >
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
          <el-form-item label="银行编码" prop="bankCode">
            <el-input v-model="temp.bankCode" placeholder="请输入银行编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="银行名称" prop="bankName">
            <el-input v-model="temp.bankName" placeholder="请输入银行名称" />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="连接类型" prop="connectType">
            <el-select
              v-model="temp.connectType"
              placeholder="请选择连接类型"
              style="width: 100%"
            >
              <el-option label="HTTP" value="HTTP" />
              <el-option label="HTTPS" value="HTTPS" />
              <el-option label="FTP" value="FTP" />
              <el-option label="SFTP" value="SFTP" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="认证类型" prop="authType">
            <el-select
              v-model="temp.authType"
              placeholder="请选择认证类型"
              style="width: 100%"
              @change="handleAuthTypeChange"
            >
              <el-option label="证书认证" value="CERT" />
              <el-option label="令牌认证" value="TOKEN" />
              <el-option label="签名认证" value="SIGN" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="16">
          <el-form-item label="服务器地址" prop="serverUrl">
            <el-input v-model="temp.serverUrl" placeholder="请输入服务器地址" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="端口" prop="serverPort">
            <el-input-number
              v-model="temp.serverPort"
              :min="1"
              :max="65535"
              placeholder="端口"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <!-- 令牌认证字段 -->
      <div v-if="temp.authType === 'TOKEN'">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户端ID" prop="clientId">
              <el-input v-model="temp.clientId" placeholder="请输入客户端ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户端密钥" prop="clientSecret">
              <el-input
                v-model="temp.clientSecret"
                type="password"
                placeholder="请输入客户端密钥"
                show-password
              />
            </el-form-item>
          </el-col>
        </el-row>
      </div>
      
      <!-- 证书认证字段 -->
      <div v-if="temp.authType === 'CERT'">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="证书路径" prop="certPath">
              <el-input v-model="temp.certPath" placeholder="请输入证书文件路径" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="证书密码" prop="certPassword">
              <el-input
                v-model="temp.certPassword"
                type="password"
                placeholder="证书密码"
                show-password
              />
            </el-form-item>
          </el-col>
        </el-row>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="超时时间(秒)" prop="timeoutSeconds">
            <el-input-number
              v-model="temp.timeoutSeconds"
              :min="1"
              :max="300"
              placeholder="超时时间"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="重试次数" prop="retryCount">
            <el-input-number
              v-model="temp.retryCount"
              :min="0"
              :max="10"
              placeholder="重试次数"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="状态" prop="isEnabled">
            <el-radio-group v-model="temp.isEnabled">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">
        取消
      </el-button>
      <el-button type="primary" @click="handleSave">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOrUpdateBankConnectConfig } from '@/api/globalTreasurer/czgg'

export default {
  name: 'BankConnectConfigEditDialog',
  data() {
    return {
      dialogVisible: false,
      dialogTitle: '',
      temp: {
        configId: null,
        configCode: '',
        configName: '',
        bankCode: '',
        bankName: '',
        connectType: '',
        serverUrl: '',
        serverPort: 80,
        authType: '',
        clientId: '',
        clientSecret: '',
        certPath: '',
        certPassword: '',
        timeoutSeconds: 30,
        retryCount: 3,
        isEnabled: 1,
        orgId: null
      },
      rules: {
        configCode: [
          { required: true, message: '请输入配置编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        configName: [
          { required: true, message: '请输入配置名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        bankCode: [
          { required: true, message: '请输入银行编码', trigger: 'blur' }
        ],
        bankName: [
          { required: true, message: '请输入银行名称', trigger: 'blur' }
        ],
        connectType: [
          { required: true, message: '请选择连接类型', trigger: 'change' }
        ],
        authType: [
          { required: true, message: '请选择认证类型', trigger: 'change' }
        ],
        serverUrl: [
          { required: true, message: '请输入服务器地址', trigger: 'blur' }
        ],
        serverPort: [
          { required: true, message: '请输入端口', trigger: 'blur' }
        ],
        timeoutSeconds: [
          { required: true, message: '请输入超时时间', trigger: 'blur' }
        ],
        retryCount: [
          { required: true, message: '请输入重试次数', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    show(row) {
      this.dialogVisible = true
      this.resetTemp()
      
      if (row) {
        this.dialogTitle = '编辑银企直连配置'
        this.temp = Object.assign({}, row)
      } else {
        this.dialogTitle = '新增银企直连配置'
        this.temp.orgId = this.$store.getters.orgId
      }
      
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    
    resetTemp() {
      this.temp = {
        configId: null,
        configCode: '',
        configName: '',
        bankCode: '',
        bankName: '',
        connectType: '',
        serverUrl: '',
        serverPort: 80,
        authType: '',
        clientId: '',
        clientSecret: '',
        certPath: '',
        certPassword: '',
        timeoutSeconds: 30,
        retryCount: 3,
        isEnabled: 1,
        orgId: null
      }
    },
    
    handleAuthTypeChange(value) {
      // 清空认证相关字段
      if (value !== 'TOKEN') {
        this.temp.clientId = ''
        this.temp.clientSecret = ''
      }
      if (value !== 'CERT') {
        this.temp.certPath = ''
        this.temp.certPassword = ''
      }
    },
    
    handleSave() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 根据认证类型验证必填字段
          if (this.temp.authType === 'TOKEN') {
            if (!this.temp.clientId) {
              this.$message.error('请输入客户端ID')
              return
            }
            if (!this.temp.clientSecret) {
              this.$message.error('请输入客户端密钥')
              return
            }
          }
          
          if (this.temp.authType === 'CERT') {
            if (!this.temp.certPath) {
              this.$message.error('请输入证书路径')
              return
            }
          }
          
          // 设置创建/更新用户
          if (this.temp.configId) {
            this.temp.updateUser = this.$store.getters.userId
          } else {
            this.temp.createUser = this.$store.getters.userId
            this.temp.updateUser = this.$store.getters.userId
          }
          
          saveOrUpdateBankConnectConfig(this.temp).then(response => {
            if (response.success) {
              this.$message.success(this.temp.configId ? '更新成功' : '创建成功')
              this.dialogVisible = false
              this.$emit('refresh')
            } else {
              this.$message.error(response.message || '保存失败')
            }
          })
        }
      })
    },
    
    handleClose() {
      this.resetTemp()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
