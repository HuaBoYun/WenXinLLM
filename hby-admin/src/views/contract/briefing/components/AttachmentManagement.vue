<template>
  <el-dialog
    title="附件管理"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <div style="margin-bottom: 20px;">
      <el-upload
        class="upload-demo"
        :action="uploadUrl"
        :headers="uploadHeaders"
        :data="uploadData"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :before-upload="beforeUpload"
        multiple
        :show-file-list="false"
      >
        <el-button type="primary" size="small" icon="el-icon-upload">上传附件</el-button>
      </el-upload>
    </div>

    <el-table :data="attachmentList" border style="width: 100%">
      <el-table-column label="文件名" prop="fileName" min-width="200" />
      <el-table-column label="文件类型" prop="fileType" width="100">
        <template #default="{ row }">
          <el-tag :type="getFileTypeColor(row.fileType)">
            {{ row.fileType }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="文件大小" prop="fileSize" width="100">
        <template #default="{ row }">
          {{ formatFileSize(row.fileSize) }}
        </template>
      </el-table-column>
      <el-table-column label="上传人" prop="uploaderName" width="120" />
      <el-table-column label="上传时间" prop="uploadTime" width="150" :formatter="formatDate" />
      <el-table-column label="文件描述" prop="fileDescription" min-width="150" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row, $index }">
          <el-button type="text" size="small" @click="handlePreview(row)">预览</el-button>
          <el-button type="text" size="small" @click="handleDownload(row)">下载</el-button>
          <el-button type="text" size="small" @click="handleEdit(row, $index)">编辑</el-button>
          <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDelete($index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 文件编辑弹窗 -->
    <el-dialog
      title="编辑附件信息"
      :visible.sync="editDialogVisible"
      width="50%"
      append-to-body
    >
      <el-form
        ref="editForm"
        :model="editForm"
        :rules="editRules"
        label-width="120px"
      >
        <el-form-item label="文件名" prop="fileName">
          <el-input v-model="editForm.fileName" placeholder="请输入文件名" />
        </el-form-item>

        <el-form-item label="文件描述" prop="fileDescription">
          <el-input
            v-model="editForm.fileDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入文件描述"
          />
        </el-form-item>

        <el-form-item label="文件标签" prop="fileTags">
          <el-input v-model="editForm.fileTags" placeholder="请输入文件标签，多个标签用逗号分隔" />
        </el-form-item>

        <el-form-item label="访问权限" prop="accessPermission">
          <el-select v-model="editForm.accessPermission" placeholder="请选择访问权限" style="width: 100%">
            <el-option label="公开" :value="1" />
            <el-option label="项目组内" :value="2" />
            <el-option label="仅管理员" :value="3" />
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveEdit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 文件预览弹窗 -->
    <el-dialog
      title="文件预览"
      :visible.sync="previewDialogVisible"
      width="80%"
      append-to-body
    >
      <div v-if="previewFile.fileType === 'pdf'" style="height: 600px;">
        <iframe :src="previewFile.fileUrl" width="100%" height="100%" frameborder="0"></iframe>
      </div>
      <div v-else-if="isImageFile(previewFile.fileType)" style="text-align: center;">
        <img :src="previewFile.fileUrl" style="max-width: 100%; max-height: 600px;" />
      </div>
      <div v-else style="text-align: center; padding: 50px;">
        <i class="el-icon-document" style="font-size: 64px; color: #ccc;"></i>
        <p>该文件类型不支持预览，请下载后查看</p>
      </div>
    </el-dialog>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    getBriefingAttachmentList,
    uploadBriefingAttachment,
    updateBriefingAttachment,
    deleteBriefingAttachment,
    downloadBriefingAttachment
  } from '@/api/contract/briefing'
  import { formatDate } from '@/utils/index'

  export default {
    name: 'AttachmentManagement',
    data() {
      return {
        dialogVisible: false,
        editDialogVisible: false,
        previewDialogVisible: false,
        briefingInfo: {},
        attachmentList: [],
        uploadUrl: process.env.VUE_APP_BASE_API + '/briefing/attachment/upload',
        uploadHeaders: {
          'Authorization': 'Bearer ' + this.$store.getters.token
        },
        uploadData: {},
        editForm: {
          id: null,
          fileName: '',
          fileDescription: '',
          fileTags: '',
          accessPermission: 1
        },
        editIndex: -1,
        previewFile: {},
        editRules: {
          fileName: [
            { required: true, message: '请输入文件名', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      async showEdit(data) {
        this.dialogVisible = true
        this.briefingInfo = { ...data }
        this.uploadData = { briefingId: data.id }
        await this.loadAttachmentList()
      },

      async loadAttachmentList() {
        try {
          const response = await getBriefingAttachmentList({
            briefingId: this.briefingInfo.id
          })
          if (response.code === 200) {
            this.attachmentList = response.data || []
          }
        } catch (error) {
          console.error('加载附件列表失败：', error)
        }
      },
      
      handleClose() {
        this.dialogVisible = false
        this.attachmentList = []
        this.briefingInfo = {}
      },

      beforeUpload(file) {
        const isLt10M = file.size / 1024 / 1024 < 10
        if (!isLt10M) {
          this.$message.error('上传文件大小不能超过 10MB!')
        }
        return isLt10M
      },

      handleUploadSuccess(response, file) {
        if (response.code === 200) {
          this.$message.success('上传成功')
          this.loadAttachmentList()
        } else {
          this.$message.error(response.message || '上传失败')
        }
      },

      handleUploadError(error) {
        this.$message.error('上传失败：' + error.message)
      },

      handlePreview(row) {
        this.previewFile = { ...row }
        this.previewDialogVisible = true
      },

      async handleDownload(row) {
        try {
          const response = await downloadBriefingAttachment(row.filePath)
          // 创建下载链接
          const url = window.URL.createObjectURL(new Blob([response]))
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', row.fileName)
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
        } catch (error) {
          this.$message.error('下载失败：' + error.message)
        }
      },

      handleEdit(row, index) {
        this.editIndex = index
        this.editForm = {
          id: row.id,
          fileName: row.fileName,
          fileDescription: row.fileDescription || '',
          fileTags: row.fileTags || '',
          accessPermission: row.accessPermission || 1
        }
        this.editDialogVisible = true
      },

      async handleSaveEdit() {
        try {
          await this.$refs.editForm.validate()
          
          const response = await updateBriefingAttachment(this.editForm.id, this.editForm)
          if (response.code === 200) {
            this.$message.success('更新成功')
            this.editDialogVisible = false
            this.loadAttachmentList()
          } else {
            this.$message.error(response.message || '更新失败')
          }
        } catch (error) {
          this.$message.error('更新失败：' + error.message)
        }
      },

      handleDelete(index) {
        this.$confirm('确定要删除这个附件吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          try {
            const attachment = this.attachmentList[index]
            const response = await deleteBriefingAttachment(attachment.id)
            if (response.code === 200) {
              this.attachmentList.splice(index, 1)
              this.$message.success('删除成功')
            } else {
              this.$message.error(response.message || '删除失败')
            }
          } catch (error) {
            this.$message.error('删除失败：' + error.message)
          }
        })
      },

      formatDate(row, column) {
        let data = row[column.property]
        return formatDate(data)
      },

      formatFileSize(size) {
        if (!size) return '0 B'
        const units = ['B', 'KB', 'MB', 'GB']
        let index = 0
        while (size >= 1024 && index < units.length - 1) {
          size /= 1024
          index++
        }
        return size.toFixed(2) + ' ' + units[index]
      },

      getFileTypeColor(fileType) {
        const colorMap = {
          'pdf': 'danger',
          'doc': 'primary',
          'docx': 'primary',
          'xls': 'success',
          'xlsx': 'success',
          'jpg': 'warning',
          'jpeg': 'warning',
          'png': 'warning',
          'gif': 'warning'
        }
        return colorMap[fileType.toLowerCase()] || 'info'
      },

      isImageFile(fileType) {
        const imageTypes = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp']
        return imageTypes.includes(fileType.toLowerCase())
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
  .upload-demo {
    display: inline-block;
  }
</style>
