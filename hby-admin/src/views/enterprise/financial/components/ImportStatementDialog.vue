<template>
  <el-dialog
    title="导入财务报表"
    :visible.sync="dialogVisible"
    width="700px"
    @close="handleClose"
  >
    <el-steps :active="currentStep" finish-status="success" align-center>
      <el-step title="选择文件" description="上传Excel文件"></el-step>
      <el-step title="数据预览" description="预览导入数据"></el-step>
      <el-step title="导入结果" description="查看导入结果"></el-step>
    </el-steps>
    
    <div style="margin: 20px 0;">
      <!-- 步骤1：文件上传 -->
      <div v-if="currentStep === 0">
        <el-form :model="form" label-width="120px">
          <el-form-item label="报表类型">
            <el-select v-model="form.statementType" placeholder="请选择报表类型">
              <el-option label="资产负债表" value="balance"></el-option>
              <el-option label="利润表" value="income"></el-option>
              <el-option label="现金流量表" value="cashflow"></el-option>
              <el-option label="所有者权益变动表" value="equity"></el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="报告期">
            <el-date-picker
              v-model="form.reportPeriod"
              type="month"
              placeholder="选择报告期"
              format="yyyy年MM月"
              value-format="yyyy-MM"
            ></el-date-picker>
          </el-form-item>
          
          <el-form-item label="文件上传">
            <el-upload
              class="upload-demo"
              drag
              :action="uploadUrl"
              :on-success="handleUploadSuccess"
              :on-error="handleUploadError"
              :before-upload="beforeUpload"
              :file-list="fileList"
              accept=".xlsx,.xls"
            >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div class="el-upload__tip" slot="tip">
                只能上传xlsx/xls文件，且不超过10MB
              </div>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 步骤2：数据预览 -->
      <div v-if="currentStep === 1">
        <el-alert
          title="数据预览"
          type="info"
          :closable="false"
          style="margin-bottom: 20px;"
        >
          <template slot="description">
            共解析到 <strong>{{ previewData.length }}</strong> 条数据记录，请确认数据无误后进行导入
          </template>
        </el-alert>
        
        <el-table :data="previewData" border max-height="400">
          <el-table-column label="序号" width="60">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="itemCode" label="科目代码" width="120"></el-table-column>
          <el-table-column prop="itemName" label="科目名称" width="200"></el-table-column>
          <el-table-column prop="currentAmount" label="本期金额" width="150" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.currentAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="previousAmount" label="上期金额" width="150" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.previousAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <!-- 步骤3：导入结果 -->
      <div v-if="currentStep === 2">
        <el-result
          :icon="importResult.success ? 'success' : 'error'"
          :title="importResult.success ? '导入成功' : '导入失败'"
          :sub-title="importResult.message"
        >
          <template slot="extra">
            <el-row :gutter="20" style="margin-bottom: 20px;">
              <el-col :span="8">
                <el-statistic title="总记录数" :value="importResult.total || 0">
                  <template slot="prefix">
                    <i class="el-icon-s-data" style="color: #409EFF"></i>
                  </template>
                </el-statistic>
              </el-col>
              <el-col :span="8">
                <el-statistic title="成功导入" :value="importResult.success_count || 0">
                  <template slot="prefix">
                    <i class="el-icon-success" style="color: #67C23A"></i>
                  </template>
                </el-statistic>
              </el-col>
              <el-col :span="8">
                <el-statistic title="失败记录" :value="importResult.error_count || 0">
                  <template slot="prefix">
                    <i class="el-icon-error" style="color: #F56C6C"></i>
                  </template>
                </el-statistic>
              </el-col>
            </el-row>
            
            <div v-if="importResult.errors && importResult.errors.length > 0">
              <h4>错误详情：</h4>
              <el-table :data="importResult.errors" border max-height="200">
                <el-table-column prop="row" label="行号" width="80"></el-table-column>
                <el-table-column prop="field" label="字段" width="120"></el-table-column>
                <el-table-column prop="error" label="错误信息"></el-table-column>
              </el-table>
            </div>
          </template>
        </el-result>
      </div>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="currentStep > 0" @click="prevStep">上一步</el-button>
      <el-button 
        type="primary" 
        @click="nextStep" 
        :loading="loading"
        v-if="currentStep < 2"
        :disabled="!canNext"
      >
        下一步
      </el-button>
      <el-button type="success" @click="handleClose" v-if="currentStep === 2">
        完成
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ImportStatementDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      currentStep: 0,
      loading: false,
      uploadUrl: '/api/upload',
      fileList: [],
      form: {
        statementType: '',
        reportPeriod: ''
      },
      previewData: [],
      importResult: {}
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
        return this.form.statementType && this.form.reportPeriod && this.fileList.length > 0
      }
      if (this.currentStep === 1) {
        return this.previewData.length > 0
      }
      return false
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initDialog()
      }
    }
  },
  methods: {
    initDialog() {
      this.currentStep = 0
      this.fileList = []
      this.form = {
        statementType: '',
        reportPeriod: ''
      }
      this.previewData = []
      this.importResult = {}
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
        this.$message.error('文件大小不能超过10MB!')
        return false
      }
      return true
    },
    handleUploadSuccess(response, file, fileList) {
      this.fileList = fileList
      this.$message.success('文件上传成功')
    },
    handleUploadError(err, file, fileList) {
      this.$message.error('文件上传失败')
    },
    nextStep() {
      if (this.currentStep === 0) {
        // 解析文件数据
        this.loading = true
        setTimeout(() => {
          this.previewData = [
            { itemCode: '1001', itemName: '货币资金', currentAmount: 1000000, previousAmount: 900000, status: '正常' },
            { itemCode: '1002', itemName: '应收账款', currentAmount: 500000, previousAmount: 450000, status: '正常' },
            { itemCode: '2001', itemName: '应付账款', currentAmount: 300000, previousAmount: 280000, status: '正常' }
          ]
          this.loading = false
          this.currentStep++
        }, 1000)
      } else if (this.currentStep === 1) {
        // 执行导入
        this.loading = true
        setTimeout(() => {
          this.importResult = {
            success: true,
            message: '财务报表数据导入成功',
            total: this.previewData.length,
            success_count: this.previewData.length,
            error_count: 0,
            errors: []
          }
          this.loading = false
          this.currentStep++
        }, 2000)
      }
    },
    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万元'
    },
    getStatusType(status) {
      const statusMap = {
        '正常': 'success',
        '异常': 'danger',
        '警告': 'warning'
      }
      return statusMap[status] || 'info'
    },
    handleClose() {
      this.dialogVisible = false
      if (this.currentStep === 2 && this.importResult.success) {
        this.$emit('refresh')
      }
    }
  }
}
</script>

<style scoped>
.upload-demo {
  width: 100%;
}
</style>
