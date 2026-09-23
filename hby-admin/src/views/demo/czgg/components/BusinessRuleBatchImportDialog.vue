<template>
  <el-dialog
    title="批量导入业务规则"
    :visible.sync="visible"
    width="600px"
    @close="handleClose"
  >
    <div class="import-container">
      <el-upload
        class="upload-demo"
        drag
        action=""
        :before-upload="beforeUpload"
        :on-success="handleSuccess"
        :on-error="handleError"
        accept=".xlsx,.xls"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
      </el-upload>
      
      <div class="template-download">
        <el-button type="text" @click="downloadTemplate">
          <i class="el-icon-download"></i>
          下载导入模板
        </el-button>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleConfirm">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BusinessRuleBatchImportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      uploadData: []
    }
  },
  methods: {
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || 
                     file.type === 'application/vnd.ms-excel'
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isExcel) {
        this.$message.error('只能上传Excel文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      return true
    },
    handleSuccess(response, file) {
      this.$message.success('文件上传成功')
      this.uploadData = response.data || []
    },
    handleError(error) {
      this.$message.error('文件上传失败')
    },
    downloadTemplate() {
      // 下载模板逻辑
      this.$message.info('模板下载功能待实现')
    },
    handleClose() {
      this.$emit('update:visible', false)
      this.uploadData = []
    },
    handleConfirm() {
      if (this.uploadData.length === 0) {
        this.$message.warning('请先上传文件')
        return
      }
      this.$emit('confirm', this.uploadData)
      this.handleClose()
    }
  }
}
</script>

<style scoped>
.import-container {
  padding: 20px 0;
}

.template-download {
  margin-top: 20px;
  text-align: center;
}

.upload-demo {
  margin-bottom: 20px;
}
</style>
