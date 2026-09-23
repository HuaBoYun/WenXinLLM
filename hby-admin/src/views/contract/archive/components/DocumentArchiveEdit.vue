<template>
  <div>
    <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属项目" prop="projectId">
            <el-input v-model="selectedProjectName" placeholder="请选择项目" readonly>
              <el-button slot="append" @click="showProjectDialog">选择项目</el-button>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文档编号" prop="documentNo">
            <el-input v-model="form.documentNo" placeholder="请输入文档编号" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="文档名称" prop="documentName">
            <el-input v-model="form.documentName" placeholder="请输入文档名称" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="文档类型" prop="documentType">
            <el-select v-model="form.documentType" placeholder="请选择文档类型" style="width: 100%">
              <el-option label="合同文件" :value="1" />
              <el-option label="技术文件" :value="2" />
              <el-option label="管理文件" :value="3" />
              <el-option label="财务文件" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文档分类" prop="documentCategory">
            <el-input v-model="form.documentCategory" placeholder="请输入文档分类" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="版本号" prop="versionNo">
            <el-input v-model="form.versionNo" placeholder="请输入版本号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="归档日期" prop="archiveDate">
            <el-date-picker
              v-model="form.archiveDate"
              type="date"
              placeholder="请选择归档日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="存储位置" prop="storageLocation">
            <el-input v-model="form.storageLocation" placeholder="请输入存储位置" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保存期限" prop="retentionPeriod">
            <el-input-number
              v-model="form.retentionPeriod"
              :min="1"
              :max="100"
              placeholder="请输入保存期限(年)"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="访问级别" prop="accessLevel">
            <el-select v-model="form.accessLevel" placeholder="请选择访问级别" style="width: 100%">
              <el-option label="公开" :value="1" />
              <el-option label="内部" :value="2" />
              <el-option label="机密" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文档状态" prop="documentStatus">
            <el-select v-model="form.documentStatus" placeholder="请选择文档状态" style="width: 100%">
              <el-option label="有效" :value="1" />
              <el-option label="作废" :value="2" />
              <el-option label="归档" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="关键词" prop="keywords">
        <el-input v-model="form.keywords" placeholder="请输入关键词，多个关键词用逗号分隔" />
      </el-form-item>

      <el-form-item label="文档摘要" prop="documentSummary">
        <el-input
          v-model="form.documentSummary"
          type="textarea"
          :rows="3"
          placeholder="请输入文档摘要"
        />
      </el-form-item>

      <el-form-item label="相关文档" prop="relatedDocuments">
        <el-input
          v-model="form.relatedDocuments"
          type="textarea"
          :rows="2"
          placeholder="请输入相关文档信息"
        />
      </el-form-item>

      <el-form-item label="文件上传">
        <el-upload
          ref="upload"
          :action="uploadUrl"
          :headers="uploadHeaders"
          :file-list="fileList"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :before-upload="beforeUpload"
          :on-remove="handleRemove"
          multiple
        >
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png/pdf/doc/docx文件，且不超过10MB</div>
        </el-upload>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSave">确定</el-button>
    </div>
    </el-dialog>

    <!-- 项目选择对话框 -->
    <el-dialog
      title="选择项目"
      :visible.sync="projectDialogVisible"
      width="800px"
      :before-close="closeProjectDialog"
    >
      <el-table
        :data="projectList"
        @selection-change="handleProjectSelectionChange"
        style="width: 100%"
        max-height="400"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="projectNo" label="项目编号" width="150" />
        <el-table-column prop="projectName" label="项目名称" />
        <el-table-column prop="projectType" label="项目类型" width="120" />
        <el-table-column prop="projectStatus" label="项目状态" width="100" />
      </el-table>

      <el-pagination
        background
        :current-page="projectQueryForm.pageNumber"
        :page-size="projectQueryForm.pageSize"
        :layout="'total, sizes, prev, pager, next, jumper'"
        :total="projectTotal"
        @size-change="handleProjectSizeChange"
        @current-change="handleProjectCurrentChange"
        style="margin-top: 20px; text-align: right;"
      />

      <div slot="footer" class="dialog-footer">
        <el-button @click="closeProjectDialog">取消</el-button>
        <el-button type="primary" @click="confirmProjectSelection">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    saveDocumentArchive,
    updateDocumentArchive,
    getDocumentArchiveById,
    getProjectList
  } from '@/api/contract/archive'

  export default {
    name: 'DocumentArchiveEdit',
    data() {
      return {
        dialogVisible: false,
        loading: false,
        title: '新增文档',
        isEdit: false,
        uploadUrl: process.env.VUE_APP_BASE_API + '/file/upload',
        uploadHeaders: {
          Authorization: 'Bearer ' + this.$store.getters.token
        },
        fileList: [],
        // 项目选择相关
        projectDialogVisible: false,
        projectList: [],
        projectTotal: 0,
        selectedProjects: [],
        selectedProjectName: '',
        projectQueryForm: {
          pageNumber: 1,
          pageSize: 10
        },
        form: {
          id: null,
          projectId: null,
          documentNo: '',
          documentName: '',
          documentType: 1,
          documentCategory: '',
          filePath: '',
          fileSize: null,
          fileFormat: '',
          versionNo: '',
          archiveDate: '',
          archiverId: null,
          storageLocation: '',
          retentionPeriod: null,
          accessLevel: 1,
          documentStatus: 1,
          keywords: '',
          documentSummary: '',
          relatedDocuments: ''
        },
        rules: {
          documentNo: [
            { required: true, message: '请输入文档编号', trigger: 'blur' }
          ],
          documentName: [
            { required: true, message: '请输入文档名称', trigger: 'blur' }
          ],
          documentType: [
            { required: true, message: '请选择文档类型', trigger: 'change' }
          ],
          archiveDate: [
            { required: true, message: '请选择归档日期', trigger: 'change' }
          ],
          storageLocation: [
            { required: true, message: '请输入存储位置', trigger: 'blur' }
          ],
          accessLevel: [
            { required: true, message: '请选择访问级别', trigger: 'change' }
          ],
          documentStatus: [
            { required: true, message: '请选择文档状态', trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      // 显示编辑对话框
      showEdit(row = null) {
        this.dialogVisible = true
        this.isEdit = !!row
        this.title = this.isEdit ? '编辑文档' : '新增文档'
        
        if (this.isEdit) {
          this.loadData(row.id)
        } else {
          this.resetForm()
        }
      },
      // 加载数据
      async loadData(id) {
        try {
          const response = await getDocumentArchiveById(id)
          if (response.code === 1) {
            this.form = { ...this.form, ...response.data }

            // 如果有项目ID，查询项目名称
            if (this.form.projectId) {
              await this.loadProjectName(this.form.projectId)
            }
          } else {
            this.$message.error(response.msg || '获取数据失败')
          }
        } catch (error) {
          this.$message.error('获取数据失败')
        }
      },

      // 根据项目ID加载项目名称
      async loadProjectName(projectId) {
        try {
          const response = await getProjectList({ pageNumber: 1, pageSize: 1000 })
          if (response.code === 1) {
            const project = response.data?.list?.find(p => p.projectId === projectId)
            if (project) {
              this.selectedProjectName = project.projectName
              this.selectedProject = project
            }
          }
        } catch (error) {
          console.error('加载项目名称失败:', error)
        }
      },
      // 重置表单
      resetForm() {
        this.selectedProjects = []
        this.selectedProjectName = ''
        this.form = {
          id: null,
          projectId: null,
          documentNo: '',
          documentName: '',
          documentType: 1,
          documentCategory: '',
          filePath: '',
          fileSize: null,
          fileFormat: '',
          versionNo: '',
          archiveDate: '',
          archiverId: null,
          storageLocation: '',
          retentionPeriod: null,
          accessLevel: 1,
          documentStatus: 1,
          keywords: '',
          documentSummary: '',
          relatedDocuments: ''
        }
        this.fileList = []
        this.$nextTick(() => {
          this.$refs.form?.clearValidate()
        })
      },
      // 保存
      async handleSave() {
        // 保存前检查项目是否已选择
        if (!this.form.projectId) {
          this.$message.error('请选择项目')
          return
        }

        try {
          await this.$refs.form.validate()

          // 设置归档人ID为当前登录用户ID
          if (!this.form.archiverId) {
            const userId = this.$store.getters.userId || this.$store.getters.userInfo?.userId || '1'
            this.form.archiverId = parseInt(userId) || 1
          }

          this.loading = true
          let response
          if (this.isEdit) {
            response = await updateDocumentArchive(this.form.id, this.form)
          } else {
            response = await saveDocumentArchive(this.form)
          }

          if (response.code === 1) {
            this.$message.success(this.isEdit ? '更新成功' : '新增成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          if (error.message) {
            this.$message.error('操作失败：' + error.message)
          }
        } finally {
          this.loading = false
        }
      },
      // 关闭对话框
      handleClose() {
        this.dialogVisible = false
        this.resetForm()
      },
      // 文件上传成功
      handleUploadSuccess(response, file, fileList) {
        if (response.code === 1) {
          this.form.filePath = response.data.filePath
          this.form.fileSize = file.size
          this.form.fileFormat = file.name.split('.').pop()
          this.$message.success('文件上传成功')
        } else {
          this.$message.error(response.msg || '文件上传失败')
        }
      },
      // 文件上传失败
      handleUploadError() {
        this.$message.error('文件上传失败')
      },
      // 上传前检查
      beforeUpload(file) {
        const isValidType = ['jpg', 'jpeg', 'png', 'pdf', 'doc', 'docx'].includes(
          file.name.split('.').pop().toLowerCase()
        )
        const isLt10M = file.size / 1024 / 1024 < 10

        if (!isValidType) {
          this.$message.error('只能上传jpg/png/pdf/doc/docx格式的文件!')
          return false
        }
        if (!isLt10M) {
          this.$message.error('上传文件大小不能超过10MB!')
          return false
        }
        return true
      },
      // 移除文件
      handleRemove(file, fileList) {
        this.form.filePath = ''
        this.form.fileSize = null
        this.form.fileFormat = ''
      },

      // 项目选择相关方法
      showProjectDialog() {
        this.projectDialogVisible = true
        this.fetchProjectList()
      },

      closeProjectDialog() {
        this.projectDialogVisible = false
        // 不清除 selectedProject，保持已选择的项目状态
      },

      async fetchProjectList() {
        try {
          console.log('开始获取项目列表...')
          const response = await getProjectList(this.projectQueryForm)
          console.log('项目列表API响应:', response)

          if (response.code === 1) {
            this.projectList = response.data?.list || []
            this.projectTotal = response.data?.total || 0
            console.log('项目列表数据:', this.projectList)
            console.log('项目总数:', this.projectTotal)
          } else {
            this.$message.error(response.msg || '获取项目列表失败')
          }
        } catch (error) {
          console.error('获取项目列表失败:', error)
          this.$message.error('获取项目列表失败: ' + error.message)
        }
      },

      handleProjectSelectionChange(selection) {
        this.selectedProjects = selection
      },

      confirmProjectSelection() {
        if (this.selectedProjects.length === 0) {
          this.$message.warning('请选择一个项目')
          return
        }
        if (this.selectedProjects.length > 1) {
          this.$message.warning('只能选择一个项目')
          return
        }

        const selectedProject = this.selectedProjects[0]
        this.form.projectId = selectedProject.projectId
        this.selectedProjectName = selectedProject.projectName
        this.projectDialogVisible = false
        this.selectedProjects = []
      },

      handleProjectSizeChange(val) {
        this.projectQueryForm.pageSize = val
        this.fetchProjectList()
      },

      handleProjectCurrentChange(val) {
        this.projectQueryForm.pageNumber = val
        this.fetchProjectList()
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
