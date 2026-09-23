<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="reportForm"
      :model="reportForm"
      :rules="reportRules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报告名称" prop="reportName">
            <el-input
              v-model="reportForm.reportName"
              placeholder="请输入报告名称"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告类型" prop="reportType">
            <el-select
              v-model="reportForm.reportType"
              placeholder="请选择报告类型"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="监管报告" value="SUPERVISION"></el-option>
              <el-option label="风险报告" value="RISK"></el-option>
              <el-option label="财务报告" value="FINANCIAL"></el-option>
              <el-option label="合规报告" value="COMPLIANCE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报告周期" prop="reportPeriod">
            <el-select
              v-model="reportForm.reportPeriod"
              placeholder="请选择报告周期"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="日报" value="DAILY"></el-option>
              <el-option label="周报" value="WEEKLY"></el-option>
              <el-option label="月报" value="MONTHLY"></el-option>
              <el-option label="季报" value="QUARTERLY"></el-option>
              <el-option label="年报" value="YEARLY"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告状态" prop="status">
            <el-select
              v-model="reportForm.status"
              placeholder="请选择状态"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="草稿" value="DRAFT"></el-option>
              <el-option label="待审核" value="PENDING"></el-option>
              <el-option label="已发布" value="PUBLISHED"></el-option>
              <el-option label="已归档" value="ARCHIVED"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报告日期" prop="reportDate">
            <el-date-picker
              v-model="reportForm.reportDate"
              type="date"
              placeholder="选择报告日期"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生成方式" prop="generateType">
            <el-select
              v-model="reportForm.generateType"
              placeholder="请选择生成方式"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="自动生成" value="AUTO"></el-option>
              <el-option label="手动生成" value="MANUAL"></el-option>
              <el-option label="模板生成" value="TEMPLATE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="报告摘要" prop="summary">
        <el-input
          v-model="reportForm.summary"
          type="textarea"
          :rows="3"
          placeholder="请输入报告摘要"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="报告内容" prop="content">
        <el-input
          v-model="reportForm.content"
          type="textarea"
          :rows="8"
          placeholder="请输入报告内容"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="创建人" prop="creator">
            <el-input
              v-model="reportForm.creator"
              placeholder="请输入创建人"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审核人" prop="reviewer">
            <el-input
              v-model="reportForm.reviewer"
              placeholder="请输入审核人"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="关键词" prop="keywords">
        <el-input
          v-model="reportForm.keywords"
          placeholder="请输入关键词，多个关键词用逗号分隔"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="附件上传" v-if="dialogType !== 'view'">
        <el-upload
          class="upload-demo"
          action="#"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :before-remove="beforeRemove"
          multiple
          :limit="5"
          :on-exceed="handleExceed"
          :file-list="fileList"
        >
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png/pdf文件，且不超过10MB</div>
        </el-upload>
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="reportForm.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="dialogType !== 'view'"
        @click="handleSaveDraft"
        :loading="loading"
      >
        保存草稿
      </el-button>
      <el-button
        v-if="dialogType !== 'view'"
        type="primary"
        @click="handleSubmit"
        :loading="loading"
      >
        {{ dialogType === 'add' ? '创建报告' : '更新报告' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveSupervisionReport } from '@/api/stateAssets/supervisionReport'

export default {
  name: 'SupervisionReportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    reportData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit, view
    }
  },
  data() {
    return {
      loading: false,
      fileList: [],
      reportForm: {
        id: '',
        reportName: '',
        reportType: '',
        reportPeriod: '',
        status: 'DRAFT',
        reportDate: '',
        generateType: '',
        summary: '',
        content: '',
        creator: '',
        reviewer: '',
        keywords: '',
        remarks: ''
      },
      reportRules: {
        reportName: [
          { required: true, message: '请输入报告名称', trigger: 'blur' }
        ],
        reportType: [
          { required: true, message: '请选择报告类型', trigger: 'change' }
        ],
        reportPeriod: [
          { required: true, message: '请选择报告周期', trigger: 'change' }
        ],
        reportDate: [
          { required: true, message: '请选择报告日期', trigger: 'change' }
        ],
        generateType: [
          { required: true, message: '请选择生成方式', trigger: 'change' }
        ],
        summary: [
          { required: true, message: '请输入报告摘要', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入报告内容', trigger: 'blur' }
        ]
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
    dialogTitle() {
      const titleMap = {
        add: '新建监管报告',
        edit: '编辑监管报告',
        view: '查看监管报告'
      }
      return titleMap[this.dialogType] || '新建监管报告'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      if (this.dialogType === 'add') {
        this.reportForm = {
          id: '',
          reportName: '',
          reportType: '',
          reportPeriod: '',
          status: 'DRAFT',
          reportDate: '',
          generateType: '',
          summary: '',
          content: '',
          creator: '',
          reviewer: '',
          keywords: '',
          remarks: ''
        }
        this.fileList = []
      } else {
        this.reportForm = { ...this.reportData }
        this.fileList = this.reportData.attachments || []
      }
      
      this.$nextTick(() => {
        if (this.$refs.reportForm) {
          this.$refs.reportForm.clearValidate()
        }
      })
    },

    handleSubmit() {
      this.$refs.reportForm.validate((valid) => {
        if (valid) {
          this.loading = true
          const formData = { ...this.reportForm }
          formData.status = 'PENDING' // 提交时设置为待审核状态
          
          saveSupervisionReport(formData).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '操作成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
            this.loading = false
          }).catch(error => {
            console.error('保存监管报告失败:', error)
            this.$message.error('操作失败')
            this.loading = false
          })
        }
      })
    },

    handleSaveDraft() {
      this.loading = true
      const formData = { ...this.reportForm }
      formData.status = 'DRAFT' // 保存草稿状态
      
      saveSupervisionReport(formData).then(response => {
        if (response.code === 1) {
          this.$message.success('草稿保存成功')
          this.handleClose()
          this.$emit('refresh')
        } else {
          this.$message.error(response.msg || '保存失败')
        }
        this.loading = false
      }).catch(error => {
        console.error('保存草稿失败:', error)
        this.$message.error('保存失败')
        this.loading = false
      })
    },

    handleClose() {
      this.dialogVisible = false
      this.loading = false
    },

    // 文件上传相关方法
    handleRemove(file, fileList) {
      this.fileList = fileList
    },

    handlePreview(file) {
      console.log('预览文件:', file)
    },

    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 5 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },

    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
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
