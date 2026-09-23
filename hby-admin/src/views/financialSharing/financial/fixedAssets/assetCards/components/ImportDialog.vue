<template>
  <el-dialog
    title="批量导入资产"
    :visible.sync="visible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="import-container">
      <el-alert
        title="导入说明"
        type="info"
        :closable="false"
        show-icon
        style="margin-bottom: 20px"
      >
        <div slot="default">
          <p>1. 请先下载导入模板，按照模板格式填写数据</p>
          <p>2. 支持的文件格式：.xlsx, .xls</p>
          <p>3. 单次最多导入1000条数据</p>
          <p>4. 必填字段：资产编码、资产名称、资产类别、资产原值、购置日期</p>
        </div>
      </el-alert>

      <div class="template-download">
        <el-button type="primary" size="small" @click="handleDownloadTemplate">
          <i class="el-icon-download"></i> 下载导入模板
        </el-button>
      </div>

      <el-upload
        ref="upload"
        class="upload-demo"
        drag
        action="#"
        :auto-upload="false"
        :on-change="handleFileChange"
        :on-remove="handleFileRemove"
        :file-list="fileList"
        :limit="1"
        accept=".xlsx,.xls"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
      </el-upload>

      <div v-if="importResult" class="import-result">
        <el-alert
          :title="importResult.title"
          :type="importResult.type"
          :closable="false"
          show-icon
        >
          <div slot="default">
            <p>成功导入：{{ importResult.successCount }} 条</p>
            <p v-if="importResult.failCount > 0">失败：{{ importResult.failCount }} 条</p>
            <div v-if="importResult.errors && importResult.errors.length > 0">
              <p style="margin-top: 10px; font-weight: bold">错误详情：</p>
              <ul style="margin: 5px 0; padding-left: 20px">
                <li v-for="(error, index) in importResult.errors" :key="index">
                  {{ error }}
                </li>
              </ul>
            </div>
          </div>
        </el-alert>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleImport" :loading="importing" :disabled="!fileList.length">
        开始导入
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { importAssetCards } from '@/api/financialSharing/fixedAssets'

export default {
  name: 'ImportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      importing: false,
      fileList: [],
      importResult: null
    }
  },
  methods: {
    handleDownloadTemplate() {
      // 创建模板数据
      const templateData = [
        ['资产编码*', '资产名称*', '资产类别*', '资产原值*', '购置日期*', '使用部门', '责任人', '存放地点', '规格型号', '折旧方法', '使用年限', '预计净残值'],
        ['ZC001', '办公电脑', '电子设备', '5000', '2024-01-01', '财务部', '张三', '办公室101', 'Dell OptiPlex', '平均年限法', '5', '500'],
        ['ZC002', '打印机', '办公设备', '3000', '2024-01-15', '行政部', '李四', '办公室102', 'HP LaserJet', '平均年限法', '3', '300']
      ]
      
      // 生成CSV模板并下载
      const headers = templateData[0]
      const rows = templateData.slice(1)
      const csvContent = [headers.join(','), ...rows.map(r => r.join(','))].join('\n')
      const blob = new Blob(['\ufeff' + csvContent], { type: 'text/csv;charset=utf-8;' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = '资产导入模板.csv'
      link.click()
      window.URL.revokeObjectURL(url)
      this.$message.success('模板下载成功')
    },
    handleFileChange(file, fileList) {
      this.fileList = fileList
      this.importResult = null
    },
    handleFileRemove() {
      this.fileList = []
      this.importResult = null
    },
    async handleImport() {
      if (!this.fileList.length) {
        this.$message.warning('请先选择要导入的文件')
        return
      }

      this.importing = true
      this.importResult = null

      try {
        const formData = new FormData()
        formData.append('file', this.fileList[0].raw)

        const response = await importAssetCards(formData)
        
        if (response.code === 1) {
          this.importResult = {
            title: '导入成功',
            type: 'success',
            successCount: response.data.successCount || 0,
            failCount: response.data.failCount || 0,
            errors: response.data.errors || []
          }
          
          if (response.data.failCount === 0) {
            this.$message.success('导入成功')
            setTimeout(() => {
              this.$emit('success')
              this.handleClose()
            }, 2000)
          }
        } else {
          this.importResult = {
            title: '导入失败',
            type: 'error',
            successCount: 0,
            failCount: this.fileList.length,
            errors: [response.msg || '导入失败']
          }
        }
      } catch (error) {
        console.error('导入失败：', error)
        this.importResult = {
          title: '导入失败',
          type: 'error',
          successCount: 0,
          failCount: this.fileList.length,
          errors: [error.message || '导入失败']
        }
      } finally {
        this.importing = false
      }
    },
    handleClose() {
      this.fileList = []
      this.importResult = null
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style lang="scss" scoped>
.import-container {
  padding: 10px 0;
}

.template-download {
  margin-bottom: 20px;
  text-align: center;
}

.import-result {
  margin-top: 20px;
}
</style>

