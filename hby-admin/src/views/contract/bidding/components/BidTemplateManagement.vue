<template>
  <div class="bid-template-management">
    <!-- 查询表单 -->
    <el-card shadow="never" class="search-card">
      <el-form
        ref="searchForm"
        :model="searchForm"
        :inline="true"
        label-width="80px"
        size="small"
      >
        <el-form-item label="模板名称">
          <el-input
            v-model="searchForm.templateName"
            placeholder="请输入模板名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="模板类型">
          <el-select
            v-model="searchForm.templateType"
            placeholder="请选择模板类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in templateTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="模板状态">
          <el-select
            v-model="searchForm.templateStatus"
            placeholder="请选择模板状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in templateStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card shadow="never" class="operation-card">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
        新建模板
      </el-button>
      <el-button
        type="success"
        icon="el-icon-upload2"
        @click="handleImport"
        :disabled="selectedRows.length === 0"
      >
        批量导入
      </el-button>
      <el-button
        type="warning"
        icon="el-icon-download"
        @click="handleExport"
        :disabled="selectedRows.length === 0"
      >
        批量导出
      </el-button>
      <el-button
        type="danger"
        icon="el-icon-delete"
        @click="handleBatchDelete"
        :disabled="selectedRows.length === 0"
      >
        批量删除
      </el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="tableLoading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          prop="templateNo"
          label="模板编号"
          width="120"
          align="center"
        />
        <el-table-column
          prop="templateName"
          label="模板名称"
          min-width="200"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <el-button type="text" @click="handlePreview(row)">
              {{ row.templateName }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="templateType"
          label="模板类型"
          width="120"
          align="center"
        >
          <template #default="{ row }">
            <el-tag :type="getTemplateTypeTagType(row.templateType)">
              {{ getTemplateTypeName(row.templateType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="templateCategory"
          label="模板分类"
          width="120"
          align="center"
        />
        <el-table-column
          prop="templateVersion"
          label="版本"
          width="80"
          align="center"
        />
        <el-table-column
          prop="usageCount"
          label="使用次数"
          width="100"
          align="center"
        />
        <el-table-column
          prop="ratingScore"
          label="评分"
          width="80"
          align="center"
        >
          <template #default="{ row }">
            <el-rate
              v-model="row.ratingScore"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="templateStatus"
          label="状态"
          width="100"
          align="center"
        >
          <template #default="{ row }">
            <el-switch
              v-model="row.isEnabled"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="160"
          align="center"
        >
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <el-button
              type="text"
              size="small"
              icon="el-icon-view"
              @click="handlePreview(row)"
            >
              预览
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-edit"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-document-copy"
              @click="handleCopy(row)"
            >
              复制
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-download"
              @click="handleDownload(row)"
            >
              下载
            </el-button>
            <el-popconfirm
              title="确定删除这个模板吗？"
              @confirm="handleDelete(row)"
            >
              <el-button
                slot="reference"
                type="text"
                size="small"
                icon="el-icon-delete"
                style="color: #f56c6c"
              >
                删除
              </el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </el-card>

    <!-- 模板表单对话框 -->
    <bid-template-form
      ref="templateForm"
      @fetch-data="fetchData"
    />

    <!-- 模板预览对话框 -->
    <bid-template-preview
      ref="templatePreview"
    />

    <!-- 文件上传对话框 -->
    <el-dialog
      title="批量导入模板"
      :visible.sync="importDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-upload
        ref="upload"
        :action="uploadUrl"
        :headers="uploadHeaders"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :before-upload="beforeUpload"
        :file-list="fileList"
        multiple
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">
          只能上传 doc/docx/pdf 文件，且不超过 10MB
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitUpload">确定上传</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBidTemplateList,
  deleteBidTemplate,
  updateBidTemplateStatus,
  copyBidTemplate,
  downloadBidTemplate,
  batchDeleteBidTemplates,
  exportBidTemplates
} from '@/api/contract/bidding'
import BidTemplateForm from './BidTemplateForm.vue'
import BidTemplatePreview from './BidTemplatePreview.vue'
import { getToken } from '@/utils/token'

export default {
  name: 'BidTemplateManagement',
  components: {
    BidTemplateForm,
    BidTemplatePreview
  },
  data() {
    return {
      tableLoading: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        templateName: '',
        templateType: null,
        templateStatus: null
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      templateTypeOptions: [
        { label: '技术标模板', value: 1 },
        { label: '商务标模板', value: 2 },
        { label: '综合标模板', value: 3 },
        { label: '资格预审模板', value: 4 }
      ],
      templateStatusOptions: [
        { label: '启用', value: 1 },
        { label: '禁用', value: 0 }
      ],
      importDialogVisible: false,
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/contract/bidding/template/import',
      uploadHeaders: {
        Authorization: 'Bearer ' + getToken()
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.tableLoading = true
      try {
        const params = {
          ...this.searchForm,
          current: this.pagination.current,
          size: this.pagination.size
        }
        const response = await getBidTemplateList(params)
        if (response.code === 200) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
        } else {
          this.$message.error(response.message || '获取数据失败')
        }
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.tableLoading = false
      }
    },

    handleSearch() {
      this.pagination.current = 1
      this.fetchData()
    },

    handleReset() {
      this.$refs.searchForm.resetFields()
      this.searchForm = {
        templateName: '',
        templateType: null,
        templateStatus: null
      }
      this.pagination.current = 1
      this.fetchData()
    },

    handleAdd() {
      this.$refs.templateForm.showEdit()
    },

    handleEdit(row) {
      this.$refs.templateForm.showEdit(row)
    },

    handlePreview(row) {
      this.$refs.templatePreview.showPreview(row)
    },

    async handleCopy(row) {
      try {
        const response = await copyBidTemplate(row.id)
        if (response.code === 200) {
          this.$message.success('复制成功')
          this.fetchData()
        } else {
          this.$message.error(response.message || '复制失败')
        }
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    async handleDownload(row) {
      try {
        const response = await downloadBidTemplate(row.id)
        // 处理文件下载
        const blob = new Blob([response.data])
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = row.templateName + '.docx'
        link.click()
        window.URL.revokeObjectURL(url)
      } catch (error) {
        this.$message.error('下载失败：' + error.message)
      }
    },

    async handleDelete(row) {
      try {
        const response = await deleteBidTemplate(row.id)
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.fetchData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        this.$message.error('删除失败：' + error.message)
      }
    },

    async handleStatusChange(row) {
      try {
        const response = await updateBidTemplateStatus(row.id, row.isEnabled)
        if (response.code === 200) {
          this.$message.success('状态更新成功')
        } else {
          this.$message.error(response.message || '状态更新失败')
          // 恢复原状态
          row.isEnabled = row.isEnabled === 1 ? 0 : 1
        }
      } catch (error) {
        this.$message.error('状态更新失败：' + error.message)
        // 恢复原状态
        row.isEnabled = row.isEnabled === 1 ? 0 : 1
      }
    },

    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    async handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }

      try {
        await this.$confirm('确定删除选中的模板吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const ids = this.selectedRows.map(row => row.id)
        const response = await batchDeleteBidTemplates(ids)
        if (response.code === 200) {
          this.$message.success('批量删除成功')
          this.fetchData()
        } else {
          this.$message.error(response.message || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败：' + error.message)
        }
      }
    },

    handleImport() {
      this.importDialogVisible = true
      this.fileList = []
    },

    async handleExport() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要导出的数据')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.id)
        const response = await exportBidTemplates(ids)
        // 处理文件下载
        const blob = new Blob([response.data])
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '标书模板_' + new Date().getTime() + '.zip'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    beforeUpload(file) {
      const isValidType = ['application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'application/pdf'].includes(file.type)
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isValidType) {
        this.$message.error('只能上传 DOC/DOCX/PDF 格式的文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      return true
    },

    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.$message.success(`${file.name} 上传成功`)
      } else {
        this.$message.error(`${file.name} 上传失败：${response.message}`)
      }
    },

    handleUploadError(err, file, fileList) {
      this.$message.error(`${file.name} 上传失败`)
    },

    submitUpload() {
      this.$refs.upload.submit()
      this.importDialogVisible = false
      this.fetchData()
    },

    handleSizeChange(val) {
      this.pagination.size = val
      this.pagination.current = 1
      this.fetchData()
    },

    handleCurrentChange(val) {
      this.pagination.current = val
      this.fetchData()
    },

    getTemplateTypeName(type) {
      const option = this.templateTypeOptions.find(item => item.value === type)
      return option ? option.label : '未知'
    },

    getTemplateTypeTagType(type) {
      const typeMap = {
        1: 'primary',
        2: 'success',
        3: 'warning',
        4: 'info'
      }
      return typeMap[type] || 'info'
    },

    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.bid-template-management {
  padding: 20px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
