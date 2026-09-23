<template>
  <el-dialog
    title="数据导入"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-steps :active="currentStep" finish-status="success">
      <el-step title="选择文件" description="上传数据文件"></el-step>
      <el-step title="字段映射" description="配置字段对应关系"></el-step>
      <el-step title="数据预览" description="预览导入数据"></el-step>
      <el-step title="导入完成" description="确认导入结果"></el-step>
    </el-steps>
    
    <!-- 步骤1:选择文件 -->
    <div v-if="currentStep === 0" style="margin-top: 20px;">
      <el-form :model="form" label-width="120px">
        <el-form-item label="导入类型">
          <el-select v-model="form.importType" placeholder="请选择导入类型">
            <el-option label="财务数据" value="financial"></el-option>
            <el-option label="经营数据" value="operation"></el-option>
            <el-option label="人员数据" value="personnel"></el-option>
            <el-option label="资产数据" value="asset"></el-option>
          </el-select>
          <el-button type="primary" size="small" @click="handleDownloadTemplate" style="margin-left: 10px;">
            <i class="el-icon-download"></i> 下载模板
          </el-button>
        </el-form-item>
        
        <el-form-item label="文件上传">
          <el-upload
            class="upload-demo"
            drag
            :auto-upload="false"
            :on-change="handleFileChange"
            :show-file-list="true"
            accept=".xlsx,.xls,.csv"
            :limit="1"
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传xlsx/xls/csv文件，且不超过10MB</div>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="导入选项">
          <el-checkbox-group v-model="form.importOptions">
            <el-checkbox label="skipFirstRow">跳过第一行（表头）</el-checkbox>
            <el-checkbox label="validateData">数据验证</el-checkbox>
            <el-checkbox label="allowDuplicate">允许重复数据</el-checkbox>
            <el-checkbox label="createBackup">创建备份</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 步骤2：字段映射 -->
    <div v-if="currentStep === 1" style="margin-top: 20px;">
      <el-alert
        title="字段映射配置"
        description="请将Excel文件中的列与系统字段进行对应"
        type="info"
        show-icon
        :closable="false"
      ></el-alert>
      
      <el-table :data="fieldMapping" border style="margin-top: 15px;">
        <el-table-column prop="excelColumn" label="Excel列名" width="200"></el-table-column>
        <el-table-column label="系统字段" width="250">
          <template slot-scope="scope">
            <el-select v-model="scope.row.systemField" placeholder="请选择系统字段">
              <el-option
                v-for="field in systemFields"
                :key="field.value"
                :label="field.label"
                :value="field.value"
              ></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="dataType" label="数据类型" width="120"></el-table-column>
        <el-table-column prop="required" label="必填" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.required ? 'danger' : 'info'">
              {{ scope.row.required ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sampleData" label="示例数据"></el-table-column>
      </el-table>
    </div>
    
    <!-- 步骤3：数据预览 -->
    <div v-if="currentStep === 2" style="margin-top: 20px;">
      <el-alert
        title="数据预览"
        description="请确认导入数据的正确性"
        type="warning"
        show-icon
        :closable="false"
      ></el-alert>
      
      <el-row :gutter="20" style="margin-top: 15px;">
        <el-col :span="8">
          <el-card>
            <el-statistic title="总记录数" :value="previewData.totalRecords">
              <template slot="prefix">
                <i class="el-icon-document" style="color: #409EFF"></i>
              </template>
            </el-statistic>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <el-statistic title="有效记录" :value="previewData.validRecords">
              <template slot="prefix">
                <i class="el-icon-success" style="color: #67C23A"></i>
              </template>
            </el-statistic>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <el-statistic title="错误记录" :value="previewData.errorRecords">
              <template slot="prefix">
                <i class="el-icon-error" style="color: #F56C6C"></i>
              </template>
            </el-statistic>
          </el-card>
        </el-col>
      </el-row>
      
      <el-table :data="previewData.records" border style="margin-top: 15px;" max-height="300">
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="enterpriseName" label="企业名称" width="200"></el-table-column>
        <el-table-column prop="creditCode" label="统一社会信用代码" width="180"></el-table-column>
        <el-table-column prop="revenue" label="营业收入" width="120" align="right"></el-table-column>
        <el-table-column prop="profit" label="净利润" width="120" align="right"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'valid' ? 'success' : 'danger'">
              {{ scope.row.status === 'valid' ? '有效' : '错误' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="errorMsg" label="错误信息"></el-table-column>
      </el-table>
    </div>
    
    <!-- 步骤4：导入完成 -->
    <div v-if="currentStep === 3" style="margin-top: 20px;">
      <el-result
        icon="success"
        title="导入完成"
        :sub-title="`成功导入 ${importResult.successCount} 条记录，失败 ${importResult.failCount} 条记录`"
      >
        <template slot="extra">
          <el-button type="primary" @click="viewImportLog">查看导入日志</el-button>
          <el-button @click="handleClose">关闭</el-button>
        </template>
      </el-result>
    </div>
    
    <div slot="footer" class="dialog-footer" v-if="currentStep < 3">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="currentStep > 0" @click="prevStep">上一步</el-button>
      <el-button v-if="currentStep < 2" type="primary" @click="nextStep" :disabled="!canNext">下一步</el-button>
      <el-button v-if="currentStep === 2" type="primary" @click="startImport" :loading="importing">
        {{ importing ? '导入中...' : '开始导入' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { importData, importPreview, uploadFile, downloadTemplate } from '@/api/enterprise/data'

export default {
  name: 'DataImportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    enterpriseId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      currentStep: 0,
      importing: false,
      uploadLoading: false,
      uploadedFileId: '',
      uploadedFileName: '',
      form: {
        importType: 'financial',
        importOptions: ['skipFirstRow', 'validateData']
      },
      fieldMapping: [],
      systemFields: [
        { label: '企业名称', value: 'enterpriseName' },
        { label: '数据类型', value: 'dataType' },
        { label: '数据类别', value: 'dataCategory' },
        { label: '报告期间', value: 'reportPeriod' },
        { label: '报告年度', value: 'reportYear' },
        { label: '提交人', value: 'submitter' },
        { label: '备注', value: 'remark' }
      ],
      previewData: {
        totalRecords: 0,
        validRecords: 0,
        errorRecords: 0,
        records: []
      },
      importResult: {
        successCount: 0,
        failCount: 0
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
    canNext() {
      if (this.currentStep === 0) {
        return this.form.importType && this.uploadedFileId
      }
      if (this.currentStep === 1) {
        return this.fieldMapping.length > 0 && this.fieldMapping.every(item => item.systemField)
      }
      return true
    }
  },
  methods: {
    handleClose() {
      this.currentStep = 0
      this.uploadedFileId = ''
      this.uploadedFileName = ''
      this.dialogVisible = false
    },
    nextStep() {
      if (this.currentStep < 2) {
        this.currentStep++
      }
    },
    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },
    async handleDownloadTemplate() {
      try {
        const response = await downloadTemplate(this.form.importType)
        const url = window.URL.createObjectURL(new Blob([response]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `template_${this.form.importType}_${Date.now()}.xlsx`)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('模板下载成功')
      } catch (error) {
        this.$message.error('模板下载失败')
      }
    },
    async handleFileChange(file) {
      const isExcel = file.raw.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                     file.raw.type === 'application/vnd.ms-excel' ||
                     file.raw.type === 'text/csv'
      const isLt10M = file.raw.size / 1024 / 1024 < 10
      
      if (!isExcel) {
        this.$message.error('只能上传Excel或CSV文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过10MB!')
        return false
      }
      
      try {
        this.uploadLoading = true
        const response = await uploadFile(file.raw)
        if (response.result == 200) {
          this.uploadedFileId = response.data.fileId || ''
          this.uploadedFileName = file.name
          this.$message.success('文件上传成功')
          this.loadPreviewData()
        } else {
          this.$message.error((response && response.msg) || '文件上传失败')
        }
      } catch (error) {
        this.$message.error('文件上传失败')
      } finally {
        this.uploadLoading = false
      }
    },
    loadPreviewData() {
      const params = {
        fileId: this.uploadedFileId,
        importType: this.form.importType,
        importOptions: this.form.importOptions
      }
      importPreview(params).then(response => {
        if (response.data) {
          this.fieldMapping = response.data.fieldMapping || []
          this.previewData = {
            totalRecords: response.data.totalRecords || 0,
            validRecords: response.data.validRecords || 0,
            errorRecords: response.data.errorRecords || 0,
            records: response.data.records || []
          }
        }
      }).catch(() => {
        this.$message.error('获取预览数据失败')
      })
    },
    startImport() {
      this.importing = true

      const params = {
        fileId: this.uploadedFileId,
        importType: this.form.importType,
        importOptions: this.form.importOptions,
        fieldMapping: this.fieldMapping,
        enterpriseId: this.enterpriseId
      }
      importData(params).then(response => {
        this.importing = false
        this.importResult = response.data || { successCount: 0, failCount: 0 }
        this.currentStep = 3
        this.$emit('refresh')
      }).catch(() => {
        this.importing = false
        this.$message.error('数据导入失败，请稍后重试')
      })
    },
    viewImportLog() {
      this.$message.info('查看导入日志功能开发中...')
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
.upload-demo {
  margin-top: 10px;
}
</style>
