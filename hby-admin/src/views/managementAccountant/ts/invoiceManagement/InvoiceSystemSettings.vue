<template>
  <div class="invoice-system-settings">
    <div class="page-header">
      <h3>发票系统设置</h3>
      <el-button type="primary" size="small" @click="handleSave">
        <i class="el-icon-check"></i> 保存设置
      </el-button>
    </div>

    <el-tabs v-model="activeTab" type="card">
      <!-- 基础设置 -->
      <el-tab-pane label="基础设置" name="basic">
        <el-form :model="basicSettings" label-width="150px">
          <el-form-item label="发票识别引擎">
            <el-select v-model="basicSettings.ocrEngine" placeholder="请选择OCR引擎">
              <el-option label="百度OCR" value="BAIDU" />
              <el-option label="腾讯OCR" value="TENCENT" />
              <el-option label="阿里OCR" value="ALIBABA" />
              <el-option label="自研OCR" value="CUSTOM" />
            </el-select>
          </el-form-item>
          <el-form-item label="自动验证发票">
            <el-switch v-model="basicSettings.autoVerification" />
            <span class="setting-tip">开启后将自动验证发票真伪</span>
          </el-form-item>
          <el-form-item label="重复发票检测">
            <el-switch v-model="basicSettings.duplicateDetection" />
            <span class="setting-tip">自动检测重复发票</span>
          </el-form-item>
          <el-form-item label="风险评估">
            <el-switch v-model="basicSettings.riskAssessment" />
            <span class="setting-tip">启用发票风险评估功能</span>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <!-- OCR设置 -->
      <el-tab-pane label="OCR设置" name="ocr">
        <el-form :model="ocrSettings" label-width="150px">
          <el-form-item label="识别置信度阈值">
            <el-slider
              v-model="ocrSettings.confidenceThreshold"
              :min="50"
              :max="100"
              show-input
            />
            <span class="setting-tip">低于此阈值的识别结果将标记为需要人工确认</span>
          </el-form-item>
          <el-form-item label="批量处理数量">
            <el-input-number
              v-model="ocrSettings.batchSize"
              :min="1"
              :max="100"
              controls-position="right"
            />
            <span class="setting-tip">单次批量处理的发票数量</span>
          </el-form-item>
          <el-form-item label="超时时间(秒)">
            <el-input-number
              v-model="ocrSettings.timeout"
              :min="10"
              :max="300"
              controls-position="right"
            />
          </el-form-item>
          <el-form-item label="重试次数">
            <el-input-number
              v-model="ocrSettings.retryCount"
              :min="0"
              :max="5"
              controls-position="right"
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <!-- 存储设置 -->
      <el-tab-pane label="存储设置" name="storage">
        <el-form :model="storageSettings" label-width="150px">
          <el-form-item label="存储路径">
            <el-input v-model="storageSettings.storagePath" placeholder="请输入存储路径" />
          </el-form-item>
          <el-form-item label="文件命名规则">
            <el-select v-model="storageSettings.namingRule" placeholder="请选择命名规则">
              <el-option label="发票号码" value="INVOICE_NUMBER" />
              <el-option label="时间戳" value="TIMESTAMP" />
              <el-option label="UUID" value="UUID" />
              <el-option label="自定义" value="CUSTOM" />
            </el-select>
          </el-form-item>
          <el-form-item label="自动归档">
            <el-switch v-model="storageSettings.autoArchive" />
            <span class="setting-tip">自动将处理完成的发票归档</span>
          </el-form-item>
          <el-form-item label="归档周期(天)">
            <el-input-number
              v-model="storageSettings.archiveDays"
              :min="1"
              :max="365"
              controls-position="right"
              :disabled="!storageSettings.autoArchive"
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <!-- 通知设置 -->
      <el-tab-pane label="通知设置" name="notification">
        <el-form :model="notificationSettings" label-width="150px">
          <el-form-item label="邮件通知">
            <el-switch v-model="notificationSettings.emailNotification" />
          </el-form-item>
          <el-form-item label="短信通知">
            <el-switch v-model="notificationSettings.smsNotification" />
          </el-form-item>
          <el-form-item label="系统通知">
            <el-switch v-model="notificationSettings.systemNotification" />
          </el-form-item>
          <el-form-item label="通知接收人">
            <el-input
              v-model="notificationSettings.recipients"
              type="textarea"
              :rows="3"
              placeholder="请输入邮箱地址，多个用逗号分隔"
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <!-- 安全设置 -->
      <el-tab-pane label="安全设置" name="security">
        <el-form :model="securitySettings" label-width="150px">
          <el-form-item label="数据加密">
            <el-switch v-model="securitySettings.dataEncryption" />
            <span class="setting-tip">对敏感发票数据进行加密存储</span>
          </el-form-item>
          <el-form-item label="访问日志">
            <el-switch v-model="securitySettings.accessLog" />
            <span class="setting-tip">记录发票数据访问日志</span>
          </el-form-item>
          <el-form-item label="权限验证">
            <el-switch v-model="securitySettings.permissionCheck" />
            <span class="setting-tip">启用严格的权限验证</span>
          </el-form-item>
          <el-form-item label="数据备份">
            <el-switch v-model="securitySettings.dataBackup" />
          </el-form-item>
          <el-form-item label="备份周期(小时)">
            <el-input-number
              v-model="securitySettings.backupInterval"
              :min="1"
              :max="168"
              controls-position="right"
              :disabled="!securitySettings.dataBackup"
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: 'InvoiceSystemSettings',
  data() {
    return {
      activeTab: 'basic',
      basicSettings: {
        ocrEngine: 'BAIDU',
        autoVerification: true,
        duplicateDetection: true,
        riskAssessment: true
      },
      ocrSettings: {
        confidenceThreshold: 85,
        batchSize: 20,
        timeout: 60,
        retryCount: 3
      },
      storageSettings: {
        storagePath: '/data/invoices',
        namingRule: 'INVOICE_NUMBER',
        autoArchive: true,
        archiveDays: 30
      },
      notificationSettings: {
        emailNotification: true,
        smsNotification: false,
        systemNotification: true,
        recipients: 'admin@company.com'
      },
      securitySettings: {
        dataEncryption: true,
        accessLog: true,
        permissionCheck: true,
        dataBackup: true,
        backupInterval: 24
      }
    }
  },
  methods: {
    handleSave() {
      const settings = {
        basic: this.basicSettings,
        ocr: this.ocrSettings,
        storage: this.storageSettings,
        notification: this.notificationSettings,
        security: this.securitySettings
      }
      
      // 模拟保存设置
      this.$message.success('设置保存成功')
      console.log('保存的设置:', settings)
    }
  }
}
</script>

<style lang="scss" scoped>
.invoice-system-settings {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  h3 {
    margin: 0;
    color: #303133;
  }
}

.el-tabs {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.setting-tip {
  margin-left: 10px;
  font-size: 12px;
  color: #909399;
}

.el-form {
  max-width: 600px;
}

.el-form-item {
  margin-bottom: 25px;
}
</style>
