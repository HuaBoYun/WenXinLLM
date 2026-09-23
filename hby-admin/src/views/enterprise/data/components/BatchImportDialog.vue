<template>
  <el-dialog
    title="批量导入数据"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
  >
    <el-steps :active="currentStep" finish-status="success">
      <el-step title="选择文件" description="上传Excel文件"></el-step>
      <el-step title="数据预览" description="预览导入数据"></el-step>
      <el-step title="导入结果" description="查看导入结果"></el-step>
    </el-steps>
    
    <!-- 步骤1：文件上传 -->
    <div v-if="currentStep === 0" style="margin-top: 30px;">
      <el-alert
        title="导入说明"
        type="info"
        :closable="false"
        style="margin-bottom: 20px;"
      >
        <template slot="description">
          <p>1. 请使用标准的Excel模板文件进行数据导入</p>
          <p>2. 支持的文件格式：.xlsx, .xls</p>
          <p>3. 单次最多导入1000条数据</p>
        </template>
      </el-alert>
      
      <el-upload
        ref="upload"
        class="upload-demo"
        drag
        :action="uploadUrl"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :file-list="fileList"
        :auto-upload="false"
        accept=".xlsx,.xls"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
      </el-upload>
      
      <div style="margin-top: 20px;">
        <el-button @click="downloadTemplate">下载模板</el-button>
        <el-button type="primary" @click="handleUpload" :disabled="fileList.length === 0">
          开始解析
        </el-button>
      </div>
    </div>
    
    <!-- 步骤2：数据预览 -->
    <div v-if="currentStep === 1" style="margin-top: 30px;">
      <el-alert
        :title="`共解析到 ${previewData.length} 条数据`"
        type="success"
        :closable="false"
        style="margin-bottom: 20px;"
      ></el-alert>
      
      <el-table :data="previewData" border max-height="400">
        <el-table-column label="序号" width="60">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="enterpriseName" label="企业名称" width="200"></el-table-column>
        <el-table-column prop="dataType" label="数据类型" width="120"></el-table-column>
        <el-table-column prop="dataValue" label="数据值"></el-table-column>
        <el-table-column prop="reportPeriod" label="报告期" width="120"></el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-tag size="small" :type="scope.row.valid ? 'success' : 'danger'">
              {{ scope.row.valid ? '有效' : '无效' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      
      <div style="margin-top: 20px;">
        <el-button @click="currentStep = 0">上一步</el-button>
        <el-button type="primary" @click="handleImport" :loading="importing">
          确认导入
        </el-button>
      </div>
    </div>
    
    <!-- 步骤3：导入结果 -->
    <div v-if="currentStep === 2" style="margin-top: 30px;">
      <el-result
        :icon="importResult.success ? 'success' : 'warning'"
        :title="importResult.title"
        :sub-title="importResult.subtitle"
      >
        <template slot="extra">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="总数据量">
              {{ importResult.total }}
            </el-descriptions-item>
            <el-descriptions-item label="成功导入">
              <span style="color: #67C23A;">{{ importResult.success }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="导入失败">
              <span style="color: #F56C6C;">{{ importResult.failed }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="重复数据">
              <span style="color: #E6A23C;">{{ importResult.duplicate }}</span>
            </el-descriptions-item>
          </el-descriptions>
          
          <div style="margin-top: 20px;">
            <el-button @click="handleClose">关闭</el-button>
            <el-button type="primary" @click="downloadErrorReport" v-if="importResult.failed > 0">
              下载错误报告
            </el-button>
          </div>
        </template>
      </el-result>
    </div>
    
    <div slot="footer" class="dialog-footer" v-if="currentStep < 2">
      <el-button @click="handleClose">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BatchImportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      currentStep: 0,
      importing: false,
      uploadUrl: '/api/upload',
      fileList: [],
      previewData: [],
      importResult: {
        success: true,
        title: '',
        subtitle: '',
        total: 0,
        success: 0,
        failed: 0,
        duplicate: 0
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
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.resetDialog()
      }
    }
  },
  methods: {
    resetDialog() {
      this.currentStep = 0
      this.fileList = []
      this.previewData = []
      this.importing = false
    },
    handlePreview(file) {
      console.log(file)
    },
    handleRemove(file, fileList) {
      this.fileList = fileList
    },
    handleUploadSuccess(response, file, fileList) {
      this.previewData = response.data || []
      this.currentStep = 1
    },
    handleUploadError(err, file, fileList) {
      this.$message.error('文件上传失败：' + err.message)
    },
    handleUpload() {
      this.$refs.upload.submit()
    },
    downloadTemplate() {
      // 模拟下载模板
      this.$message.success('模板下载中...')
    },
    handleImport() {
      this.importing = true
      // 模拟导入过程
      setTimeout(() => {
        this.importing = false
        this.importResult = {
          success: true,
          title: '数据导入完成',
          subtitle: '批量导入操作已完成，请查看详细结果',
          total: this.previewData.length,
          success: Math.floor(this.previewData.length * 0.9),
          failed: Math.floor(this.previewData.length * 0.05),
          duplicate: Math.floor(this.previewData.length * 0.05)
        }
        this.currentStep = 2
        this.$emit('refresh')
      }, 2000)
    },
    downloadErrorReport() {
      this.$message.success('错误报告下载中...')
    },
    handleClose() {
      this.dialogVisible = false
      this.resetDialog()
    }
  }
}
</script>

<style scoped>
.upload-demo {
  margin: 20px 0;
}
</style>
