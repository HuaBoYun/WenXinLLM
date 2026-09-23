<template>
  <el-dialog
    title="文档详情"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <el-descriptions :column="2" border>
      <el-descriptions-item label="文档编号">
        {{ form.documentNo }}
      </el-descriptions-item>
      <el-descriptions-item label="文档名称">
        {{ form.documentName }}
      </el-descriptions-item>
      <el-descriptions-item label="文档类型">
        <el-tag :type="getDocumentTypeStyle(form.documentType)">
          {{ getDocumentTypeName(form.documentType) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="文档分类">
        {{ form.documentCategory }}
      </el-descriptions-item>
      <el-descriptions-item label="版本号">
        {{ form.versionNo }}
      </el-descriptions-item>
      <el-descriptions-item label="归档日期">
        {{ form.archiveDate }}
      </el-descriptions-item>
      <el-descriptions-item label="存储位置">
        {{ form.storageLocation }}
      </el-descriptions-item>
      <el-descriptions-item label="保存期限">
        {{ form.retentionPeriod }}年
      </el-descriptions-item>
      <el-descriptions-item label="访问级别">
        <el-tag :type="getAccessLevelStyle(form.accessLevel)">
          {{ getAccessLevelName(form.accessLevel) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="文档状态">
        <el-tag :type="getDocumentStatusStyle(form.documentStatus)">
          {{ getDocumentStatusName(form.documentStatus) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="文件大小" v-if="form.fileSize">
        {{ formatFileSize(form.fileSize) }}
      </el-descriptions-item>
      <el-descriptions-item label="文件格式" v-if="form.fileFormat">
        {{ form.fileFormat }}
      </el-descriptions-item>
      <el-descriptions-item label="关键词" :span="2">
        {{ form.keywords }}
      </el-descriptions-item>
      <el-descriptions-item label="文档摘要" :span="2">
        {{ form.documentSummary }}
      </el-descriptions-item>
      <el-descriptions-item label="相关文档" :span="2">
        {{ form.relatedDocuments }}
      </el-descriptions-item>
      <el-descriptions-item label="创建时间" :span="2">
        {{ form.createTime }}
      </el-descriptions-item>
    </el-descriptions>

    <div v-if="form.filePath" style="margin-top: 20px;">
      <h4>附件文件</h4>
      <el-button
        type="primary"
        size="small"
        icon="el-icon-download"
        @click="handleDownload"
      >
        下载文件
      </el-button>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleEdit">编辑</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { getDocumentArchiveById } from '@/api/contract/archive'

  export default {
    name: 'DocumentArchiveView',
    data() {
      return {
        dialogVisible: false,
        form: {
          id: null,
          documentNo: '',
          documentName: '',
          documentType: 1,
          documentCategory: '',
          filePath: '',
          fileSize: null,
          fileFormat: '',
          versionNo: '',
          archiveDate: '',
          storageLocation: '',
          retentionPeriod: null,
          accessLevel: 1,
          documentStatus: 1,
          keywords: '',
          documentSummary: '',
          relatedDocuments: '',
          createTime: ''
        }
      }
    },
    methods: {
      // 显示查看对话框
      async showView(row) {
        this.dialogVisible = true
        try {
          const response = await getDocumentArchiveById(row.id)
          if (response.code === 1) {
            this.form = { ...this.form, ...response.data }
          } else {
            this.$message.error(response.msg || '获取数据失败')
          }
        } catch (error) {
          this.$message.error('获取数据失败')
        }
      },
      // 关闭对话框
      handleClose() {
        this.dialogVisible = false
      },
      // 编辑
      handleEdit() {
        this.$parent.$refs.edit.showEdit(this.form)
        this.handleClose()
      },
      // 下载文件
      handleDownload() {
        if (this.form.filePath) {
          window.open(this.form.filePath)
        } else {
          this.$message.warning('暂无可下载的文件')
        }
      },
      // 格式化文件大小
      formatFileSize(size) {
        if (!size) return '0 B'
        const units = ['B', 'KB', 'MB', 'GB']
        let index = 0
        while (size >= 1024 && index < units.length - 1) {
          size /= 1024
          index++
        }
        return `${size.toFixed(2)} ${units[index]}`
      },
      // 获取文档类型名称
      getDocumentTypeName(type) {
        const typeMap = {
          1: '合同文件',
          2: '技术文件',
          3: '管理文件',
          4: '财务文件'
        }
        return typeMap[type] || '未知'
      },
      // 获取文档类型样式
      getDocumentTypeStyle(type) {
        const styleMap = {
          1: 'primary',
          2: 'success',
          3: 'warning',
          4: 'info'
        }
        return styleMap[type] || 'info'
      },
      // 获取访问级别名称
      getAccessLevelName(level) {
        const levelMap = {
          1: '公开',
          2: '内部',
          3: '机密'
        }
        return levelMap[level] || '未知'
      },
      // 获取访问级别样式
      getAccessLevelStyle(level) {
        const styleMap = {
          1: 'success',
          2: 'warning',
          3: 'danger'
        }
        return styleMap[level] || 'info'
      },
      // 获取文档状态名称
      getDocumentStatusName(status) {
        const statusMap = {
          1: '有效',
          2: '作废',
          3: '归档'
        }
        return statusMap[status] || '未知'
      },
      // 获取文档状态样式
      getDocumentStatusStyle(status) {
        const styleMap = {
          1: 'success',
          2: 'danger',
          3: 'info'
        }
        return styleMap[status] || 'info'
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
