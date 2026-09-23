<template>
  <el-dialog
    title="批量导入利率"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="import-container">
      <!-- 导入说明 -->
      <el-alert
        title="导入说明"
        type="info"
        :closable="false"
        show-icon
      >
        <div slot="default">
          <p>1. 请下载模板文件，按照模板格式填写利率数据</p>
          <p>2. 支持的文件格式：Excel (.xlsx, .xls)</p>
          <p>3. 必填字段：利率类型、币种、期限类型、利率值、利率日期</p>
          <p>4. 利率类型：DEPOSIT(存款利率)、LOAN(贷款利率)、INTERBANK(同业拆借)、BENCHMARK(央行基准)</p>
          <p>5. 期限类型：DEMAND(活期)、1M(1个月)、3M(3个月)、6M(6个月)、1Y(1年)、3Y(3年)、5Y(5年)</p>
          <p>6. 计息方式：SIMPLE(单利)、COMPOUND(复利)</p>
          <p>7. 计息基础：ACT_360、ACT_365、30_360</p>
        </div>
      </el-alert>
      
      <!-- 模板下载 -->
      <div class="template-section">
        <el-button
          type="primary"
          icon="el-icon-download"
          @click="downloadTemplate"
        >
          下载导入模板
        </el-button>
      </div>
      
      <!-- 文件上传 -->
      <div class="upload-section">
        <el-upload
          ref="upload"
          class="upload-demo"
          drag
          :action="uploadUrl"
          :headers="uploadHeaders"
          :data="uploadData"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :before-upload="beforeUpload"
          :file-list="fileList"
          :auto-upload="false"
          accept=".xlsx,.xls"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
        </el-upload>
      </div>
      
      <!-- 导入选项 -->
      <div class="options-section">
        <el-form :model="importOptions" label-width="120px">
          <el-form-item label="数据来源">
            <el-select
              v-model="importOptions.dataSource"
              placeholder="请选择数据来源"
              style="width: 200px"
            >
              <el-option label="手工录入" value="MANUAL" />
              <el-option label="系统获取" value="SYSTEM" />
              <el-option label="银行接口" value="BANK" />
              <el-option label="外部接口" value="EXTERNAL" />
            </el-select>
          </el-form-item>
          <el-form-item label="重复数据处理">
            <el-radio-group v-model="importOptions.duplicateHandling">
              <el-radio value="skip">跳过</el-radio>
              <el-radio value="update">更新</el-radio>
              <el-radio value="error">报错</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="默认状态">
            <el-radio-group v-model="importOptions.defaultStatus">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="默认计息方式">
            <el-select
              v-model="importOptions.defaultInterestMethod"
              placeholder="请选择默认计息方式"
              style="width: 200px"
            >
              <el-option label="单利" value="SIMPLE" />
              <el-option label="复利" value="COMPOUND" />
            </el-select>
          </el-form-item>
          <el-form-item label="默认计息基础">
            <el-select
              v-model="importOptions.defaultDayCountBasis"
              placeholder="请选择默认计息基础"
              style="width: 200px"
            >
              <el-option label="ACT/360" value="ACT_360" />
              <el-option label="ACT/365" value="ACT_365" />
              <el-option label="30/360" value="30_360" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 导入结果 -->
      <div v-if="importResult" class="result-section">
        <el-alert
          :title="importResult.title"
          :type="importResult.type"
          :closable="false"
          show-icon
        >
          <div slot="default">
            <p v-if="importResult.successCount > 0">成功导入：{{ importResult.successCount }} 条</p>
            <p v-if="importResult.failCount > 0">导入失败：{{ importResult.failCount }} 条</p>
            <p v-if="importResult.skipCount > 0">跳过重复：{{ importResult.skipCount }} 条</p>
            <div v-if="importResult.errors && importResult.errors.length > 0">
              <p>错误详情：</p>
              <ul>
                <li v-for="(error, index) in importResult.errors" :key="index">
                  第{{ error.row }}行：{{ error.message }}
                </li>
              </ul>
            </div>
          </div>
        </el-alert>
      </div>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">
        关闭
      </el-button>
      <el-button
        type="primary"
        @click="handleImport"
        :loading="importing"
        :disabled="fileList.length === 0"
      >
        开始导入
      </el-button>
      <el-button @click="handleClear">
        清空
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { batchImportInterestRate } from '@/api/globalTreasurer/czgg'

export default {
  name: 'InterestRateBatchImportDialog',
  data() {
    return {
      dialogVisible: false,
      fileList: [],
      importing: false,
      importOptions: {
        dataSource: 'MANUAL',
        duplicateHandling: 'skip',
        defaultStatus: 1,
        defaultInterestMethod: 'SIMPLE',
        defaultDayCountBasis: 'ACT_365'
      },
      importResult: null,
      uploadUrl: process.env.VUE_APP_BASE_API + '/centralaudit/treasury-common/interest-rate/upload',
      uploadHeaders: {
        'Authorization': 'Bearer ' + (localStorage.getItem('token') || '')
      },
      uploadData: {}
    }
  },
  methods: {
    show() {
      this.dialogVisible = true
      this.resetData()
      this.uploadData = {
        orgId: this.$store.getters.orgId,
        createUser: this.$store.getters.userId
      }
    },
    
    resetData() {
      this.fileList = []
      this.importing = false
      this.importResult = null
      this.importOptions = {
        dataSource: 'MANUAL',
        duplicateHandling: 'skip',
        defaultStatus: 1,
        defaultInterestMethod: 'SIMPLE',
        defaultDayCountBasis: 'ACT_365'
      }
    },
    
    downloadTemplate() {
      // 创建模板数据
      const templateData = [
        {
          '利率类型': 'DEPOSIT',
          '币种': 'CNY',
          '期限类型': '1Y',
          '利率值': '3.5000',
          '利率日期': '2025-09-22',
          '数据来源': 'MANUAL',
          '生效时间': '2025-09-22 09:00:00',
          '失效时间': '',
          '计息方式': 'SIMPLE',
          '计息基础': 'ACT_365',
          '状态': '1',
          '备注': '一年期存款利率'
        },
        {
          '利率类型': 'LOAN',
          '币种': 'CNY',
          '期限类型': '1Y',
          '利率值': '4.3500',
          '利率日期': '2025-09-22',
          '数据来源': 'MANUAL',
          '生效时间': '2025-09-22 09:00:00',
          '失效时间': '',
          '计息方式': 'SIMPLE',
          '计息基础': 'ACT_365',
          '状态': '1',
          '备注': '一年期贷款利率'
        }
      ]
      
      // 转换为CSV格式并下载
      const csvContent = this.convertToCSV(templateData)
      const blob = new Blob(['\ufeff' + csvContent], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      const url = URL.createObjectURL(blob)
      link.setAttribute('href', url)
      link.setAttribute('download', '利率导入模板.csv')
      link.style.visibility = 'hidden'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    },
    
    convertToCSV(data) {
      if (!data || data.length === 0) return ''
      
      const headers = Object.keys(data[0])
      const csvRows = []
      
      // 添加表头
      csvRows.push(headers.join(','))
      
      // 添加数据行
      for (const row of data) {
        const values = headers.map(header => {
          const value = row[header]
          return `"${value}"`
        })
        csvRows.push(values.join(','))
      }
      
      return csvRows.join('\n')
    },
    
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                     file.type === 'application/vnd.ms-excel'
      const isLt10M = file.size / 1024 / 1024 < 10
      
      if (!isExcel) {
        this.$message.error('只能上传Excel文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过10MB!')
        return false
      }
      return true
    },
    
    handleUploadSuccess(response, file, fileList) {
      if (response.success) {
        this.importResult = {
          title: '导入成功',
          type: 'success',
          successCount: response.data.successCount || 0,
          failCount: response.data.failCount || 0,
          skipCount: response.data.skipCount || 0,
          errors: response.data.errors || []
        }
        this.$emit('refresh')
      } else {
        this.importResult = {
          title: '导入失败',
          type: 'error',
          successCount: 0,
          failCount: 0,
          skipCount: 0,
          errors: [{ row: 0, message: response.message || '导入失败' }]
        }
      }
      this.importing = false
    },
    
    handleUploadError(err, file, fileList) {
      this.importing = false
      this.importResult = {
        title: '导入失败',
        type: 'error',
        successCount: 0,
        failCount: 0,
        skipCount: 0,
        errors: [{ row: 0, message: '文件上传失败' }]
      }
    },
    
    handleImport() {
      if (this.fileList.length === 0) {
        this.$message.warning('请先选择要导入的文件')
        return
      }
      
      this.importing = true
      this.importResult = null
      
      // 设置上传参数
      this.uploadData = {
        ...this.importOptions,
        orgId: this.$store.getters.orgId,
        createUser: this.$store.getters.userId
      }
      
      // 开始上传
      this.$refs.upload.submit()
    },
    
    handleClear() {
      this.fileList = []
      this.importResult = null
      this.$refs.upload.clearFiles()
    },
    
    handleClose() {
      this.resetData()
    }
  }
}
</script>

<style scoped>
.import-container {
  padding: 20px 0;
}

.template-section {
  margin: 20px 0;
  text-align: center;
}

.upload-section {
  margin: 20px 0;
}

.options-section {
  margin: 20px 0;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.result-section {
  margin: 20px 0;
}

.dialog-footer {
  text-align: right;
}

.upload-demo {
  width: 100%;
}
</style>
