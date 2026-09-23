<template>
  <el-dialog
    title="批量导入租赁申请"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="import-content">
      <el-upload
        ref="upload"
        class="upload-area"
        drag
        action="#"
        :auto-upload="false"
        :limit="1"
        :on-change="handleFileChange"
        :on-exceed="handleExceed"
        :file-list="fileList"
        accept=".xlsx,.xls"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">
          只能上传 xlsx/xls 文件，且不超过10MB
        </div>
      </el-upload>
      <div class="template-download">
        <el-button type="text" icon="el-icon-download" @click="downloadTemplate">下载导入模板</el-button>
      </div>
      <div v-if="importResult.show" class="import-result">
        <el-alert
          :title="importResult.success ? '导入成功' : '导入失败'"
          :type="importResult.success ? 'success' : 'error'"
          :description="importResult.message"
          show-icon
          :closable="false"
        />
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="uploading" :disabled="!fileList.length" @click="handleImport">开始导入</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'LeaseImportDialog',
  props: {
    visible: { type: Boolean, default: false }
  },
  data() {
    return {
      dialogVisible: false,
      uploading: false,
      fileList: [],
      importResult: { show: false, success: false, message: '' }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) this.resetState()
    }
  },
  methods: {
    resetState() {
      this.fileList = []
      this.importResult = { show: false, success: false, message: '' }
      this.$refs.upload && this.$refs.upload.clearFiles()
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
      this.resetState()
    },
    handleFileChange(file, fileList) {
      this.fileList = fileList
      this.importResult.show = false
    },
    handleExceed() {
      this.$message.warning('只能上传一个文件，请先删除已选文件')
    },
    downloadTemplate() {
      // TODO: 实现模板下载
      this.$message.info('模板下载功能开发中')
    },
    async handleImport() {
      if (!this.fileList.length) {
        this.$message.warning('请先选择要导入的文件')
        return
      }
      this.uploading = true
      try {
        // TODO: 调用后端导入接口
        // const formData = new FormData()
        // formData.append('file', this.fileList[0].raw)
        // const res = await importFinancialLease(formData)
        this.importResult = { show: true, success: true, message: '导入功能开发中，请稍后再试' }
        // this.$emit('success')
      } catch (e) {
        this.importResult = { show: true, success: false, message: e.message || '导入失败' }
      } finally {
        this.uploading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.import-content {
  .upload-area { width: 100%; }
  .template-download { margin-top: 10px; text-align: center; }
  .import-result { margin-top: 20px; }
}
.dialog-footer { text-align: right; }
</style>

