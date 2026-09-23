<template>
  <div class="budget-import-export-container">
    <!-- 头部信息 -->
    <el-card class="header-card">
      <div slot="header" class="header-title">
        <span>预算导入导出</span>
        <el-button-group class="header-actions">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-download"
            @click="handleDownloadTemplate"
          >
            下载模板
          </el-button>
          <el-button
            type="success"
            size="small"
            icon="el-icon-upload2"
            @click="handleBatchExport"
          >
            批量导出
          </el-button>
        </el-button-group>
      </div>
    </el-card>

    <!-- 导入功能区域 -->
    <el-card class="import-card">
      <div slot="header">
        <span>数据导入</span>
        <el-badge :value="importHistory.length" class="history-badge" type="primary">
          <el-button size="mini" icon="el-icon-time" @click="showImportHistory = !showImportHistory">
            {{ showImportHistory ? '隐藏' : '显示' }}历史
          </el-button>
        </el-badge>
      </div>

      <!-- 导入步骤说明 -->
      <el-steps :active="importStep" finish-status="success" class="import-steps">
        <el-step title="下载模板" description="下载标准Excel模板"></el-step>
        <el-step title="填写数据" description="按照模板格式填写数据"></el-step>
        <el-step title="上传文件" description="上传填写好的Excel文件"></el-step>
        <el-step title="数据预览" description="预览导入数据并确认"></el-step>
        <el-step title="导入完成" description="完成数据导入"></el-step>
      </el-steps>

      <!-- 导入操作区域 -->
      <div class="import-content">
        <!-- 步骤1: 下载模板 -->
        <div v-if="importStep === 0" class="step-content">
          <el-alert
            title="请先下载标准模板"
            description="点击下方按钮下载Excel模板，按照模板格式填写预算数据"
            type="info"
            :closable="false"
            show-icon
          />
          <div class="template-download">
            <h4>可选模板类型：</h4>
            <el-radio-group v-model="selectedTemplateType">
              <el-radio label="annual">年度预算模板</el-radio>
              <el-radio label="quarterly">季度预算模板</el-radio>
              <el-radio label="monthly">月度预算模板</el-radio>
              <el-radio label="project">项目预算模板</el-radio>
            </el-radio-group>
            <div class="template-actions">
              <el-button type="primary" icon="el-icon-download" @click="handleDownloadTemplate">
                下载 {{ getTemplateTypeName(selectedTemplateType) }}
              </el-button>
              <el-button type="info" icon="el-icon-view" @click="handlePreviewTemplate">
                预览模板
              </el-button>
            </div>
          </div>
        </div>

        <!-- 步骤2: 数据预览 -->
        <div v-if="importStep === 1" class="step-content">
          <el-alert
            title="填写数据说明"
            description="请按照模板格式填写数据，注意数据格式和必填项要求"
            type="warning"
            :closable="false"
            show-icon
          />
          <div class="data-guide">
            <h4>填写注意事项：</h4>
            <ul>
              <li>预算名称不能为空</li>
              <li>预算金额必须为数字格式</li>
              <li>期间格式：YYYY-MM-DD</li>
              <li>费用类别必须从下拉列表选择</li>
              <li>红色标记为必填项</li>
            </ul>
            <div class="step-actions">
              <el-button type="primary" @click="importStep = 2">
                下一步：上传文件
              </el-button>
            </div>
          </div>
        </div>

        <!-- 步骤3: 上传文件 -->
        <div v-if="importStep === 2" class="step-content">
          <el-upload
            ref="upload"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :before-upload="beforeUpload"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :on-progress="handleUploadProgress"
            :file-list="fileList"
            :auto-upload="false"
            accept=".xlsx,.xls"
            drag
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">
              只能上传xlsx/xls文件，且不超过10MB
            </div>
          </el-upload>

          <div class="upload-actions">
            <el-button @click="importStep = 1">上一步</el-button>
            <el-button type="primary" @click="handleSubmitUpload" :loading="uploading">
              开始上传
            </el-button>
          </div>

          <!-- 上传进度 -->
          <div v-if="uploading" class="upload-progress">
            <el-progress :percentage="uploadPercent" :status="uploadStatus" />
            <p>{{ uploadStatusText }}</p>
          </div>
        </div>

        <!-- 步骤4: 数据预览 -->
        <div v-if="importStep === 3" class="step-content">
          <div v-if="previewData.length > 0">
            <el-alert
              :title="`共解析到 ${previewData.length} 条数据，其中 ${errorCount} 条有错误`"
              :type="errorCount > 0 ? 'warning' : 'success'"
              :closable="false"
              show-icon
            />

            <!-- 数据表格 -->
            <el-table
              :data="previewData"
              border
              max-height="400"
              class="preview-table"
            >
              <el-table-column type="index" width="60" label="序号" />
              <el-table-column prop="budgetName" label="预算名称" min-width="150">
                <template slot-scope="scope">
                  <span :class="{ 'error-cell': scope.row.errors.budgetName }">
                    {{ scope.row.budgetName }}
                  </span>
                  <div v-if="scope.row.errors.budgetName" class="error-text">
                    {{ scope.row.errors.budgetName }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="budgetAmount" label="预算金额" width="120">
                <template slot-scope="scope">
                  <span :class="{ 'error-cell': scope.row.errors.budgetAmount }">
                    {{ scope.row.budgetAmount }}
                  </span>
                  <div v-if="scope.row.errors.budgetAmount" class="error-text">
                    {{ scope.row.errors.budgetAmount }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="period" label="期间" width="120">
                <template slot-scope="scope">
                  <span :class="{ 'error-cell': scope.row.errors.period }">
                    {{ scope.row.period }}
                  </span>
                  <div v-if="scope.row.errors.period" class="error-text">
                    {{ scope.row.errors.period }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="department" label="部门" width="100" />
              <el-table-column prop="category" label="费用类别" width="120" />
              <el-table-column prop="status" label="状态" width="80">
                <template slot-scope="scope">
                  <el-tag
                    :type="scope.row.hasError ? 'danger' : 'success'"
                    size="small"
                  >
                    {{ scope.row.hasError ? '错误' : '正常' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>

            <div class="preview-actions">
              <el-button @click="importStep = 2">重新上传</el-button>
              <el-button
                type="primary"
                @click="handleConfirmImport"
                :disabled="validDataCount === 0"
                :loading="importing"
              >
                确认导入 ({{ validDataCount }}条有效数据)
              </el-button>
            </div>
          </div>

          <div v-else class="no-data">
            <el-empty description="没有解析到数据" />
          </div>
        </div>

        <!-- 步骤5: 导入完成 -->
        <div v-if="importStep === 4" class="step-content">
          <el-result
            :icon="importResult.success ? 'success' : 'error'"
            :title="importResult.title"
            :sub-title="importResult.message"
          >
            <template slot="extra">
              <div class="import-stats">
                <p>成功导入：{{ importResult.successCount }} 条</p>
                <p>失败导入：{{ importResult.failCount }} 条</p>
                <p>总计处理：{{ importResult.totalCount }} 条</p>
              </div>
              <div class="result-actions">
                <el-button type="primary" @click="handleNewImport">继续导入</el-button>
                <el-button @click="handleViewImportedData">查看导入数据</el-button>
                <el-button type="info" @click="handleExportErrorLog" v-if="importResult.failCount > 0">
                  导出错误日志
                </el-button>
              </div>
            </template>
          </el-result>
        </div>
      </div>

      <!-- 导入历史 -->
      <el-collapse v-if="showImportHistory" class="import-history">
        <el-collapse-item title="导入历史记录" name="history">
          <el-table :data="importHistory" border size="small">
            <el-table-column type="index" width="60" label="序号" />
            <el-table-column prop="fileName" label="文件名" min-width="150" />
            <el-table-column prop="totalCount" label="总条数" width="80" align="center" />
            <el-table-column prop="successCount" label="成功条数" width="80" align="center" />
            <el-table-column prop="failCount" label="失败条数" width="80" align="center" />
            <el-table-column prop="importTime" label="导入时间" width="150" align="center">
              <template slot-scope="scope">
                {{ scope.row.importTime | formatDate }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === 'success' ? 'success' : 'danger'" size="small">
                  {{ scope.row.status === 'success' ? '成功' : '失败' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" align="center">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  size="small"
                  icon="el-icon-view"
                  @click="handleViewHistoryDetail(scope.row)"
                >
                  详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-collapse-item>
      </el-collapse>
    </el-card>

    <!-- 导出功能区域 -->
    <el-card class="export-card">
      <div slot="header">
        <span>数据导出</span>
      </div>

      <el-form :model="exportForm" :rules="exportRules" ref="exportForm" label-width="120px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="导出类型" prop="exportType">
              <el-select v-model="exportForm.exportType" placeholder="请选择导出类型" style="width: 100%">
                <el-option label="全部预算数据" value="all" />
                <el-option label="按期间导出" value="period" />
                <el-option label="按部门导出" value="department" />
                <el-option label="按状态导出" value="status" />
                <el-option label="自定义筛选" value="custom" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="导出格式" prop="exportFormat">
              <el-select v-model="exportForm.exportFormat" placeholder="请选择导出格式" style="width: 100%">
                <el-option label="Excel格式 (.xlsx)" value="xlsx" />
                <el-option label="CSV格式 (.csv)" value="csv" />
                <el-option label="PDF格式 (.pdf)" value="pdf" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 条件筛选区域 -->
        <div v-if="exportForm.exportType !== 'all'" class="export-filters">
          <el-row :gutter="20">
            <el-col :span="8" v-if="exportForm.exportType === 'period'">
              <el-form-item label="预算期间">
                <el-date-picker
                  v-model="exportForm.periodRange"
                  type="monthrange"
                  range-separator="至"
                  start-placeholder="开始月份"
                  end-placeholder="结束月份"
                  format="yyyy-MM"
                  value-format="yyyy-MM"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8" v-if="exportForm.exportType === 'department'">
              <el-form-item label="选择部门">
                <el-select v-model="exportForm.departments" multiple placeholder="请选择部门" style="width: 100%">
                  <el-option
                    v-for="dept in departmentList"
                    :key="dept.deptId"
                    :label="dept.deptName"
                    :value="dept.deptId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8" v-if="exportForm.exportType === 'status'">
              <el-form-item label="预算状态">
                <el-select v-model="exportForm.statuses" multiple placeholder="请选择状态" style="width: 100%">
                  <el-option label="草稿" value="draft" />
                  <el-option label="审批中" value="pending" />
                  <el-option label="已生效" value="active" />
                  <el-option label="已归档" value="archived" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <el-form-item>
          <el-button type="primary" @click="handleExport" :loading="exporting">
            <i class="el-icon-download"></i> 开始导出
          </el-button>
          <el-button @click="handleResetExport">重置条件</el-button>
        </el-form-item>
      </el-form>

      <!-- 导出历史 -->
      <div class="export-history">
        <h4>最近导出记录</h4>
        <el-table :data="exportHistory" border size="small">
          <el-table-column type="index" width="60" label="序号" />
          <el-table-column prop="exportType" label="导出类型" width="120" />
          <el-table-column prop="fileName" label="文件名" min-width="200" />
          <el-table-column prop="recordCount" label="记录数" width="80" align="center" />
          <el-table-column prop="fileSize" label="文件大小" width="100" align="center" />
          <el-table-column prop="exportTime" label="导出时间" width="150" align="center">
            <template slot-scope="scope">
              {{ scope.row.exportTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                icon="el-icon-download"
                @click="handleDownloadExport(scope.row)"
              >
                下载
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
import { budgetApi } from '@/api/financialSharing/budget'
import { formatDate } from '@/utils/index'

export default {
  name: 'BudgetImportExport',
  data() {
    return {
      // 导入相关
      importStep: 0,
      selectedTemplateType: 'annual',
      fileList: [],
      uploading: false,
      uploadPercent: 0,
      uploadStatus: '',
      uploadStatusText: '',
      previewData: [],
      importing: false,
      importResult: {
        success: false,
        title: '',
        message: '',
        successCount: 0,
        failCount: 0,
        totalCount: 0
      },

      // 导入历史
      importHistory: [],
      showImportHistory: false,

      // 导出相关
      exportForm: {
        exportType: 'all',
        exportFormat: 'xlsx',
        periodRange: [],
        departments: [],
        statuses: []
      },
      exportRules: {
        exportType: [
          { required: true, message: '请选择导出类型', trigger: 'change' }
        ],
        exportFormat: [
          { required: true, message: '请选择导出格式', trigger: 'change' }
        ]
      },
      exporting: false,

      // 导出历史
      exportHistory: [],

      // 部门列表
      departmentList: [],

      // 上传配置
      uploadUrl: process.env.VUE_APP_BASE_API + '/financial/budget/import/upload',
      uploadHeaders: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      }
    }
  },

  created() {
    this.fetchDepartmentList()
    this.fetchImportHistory()
    this.fetchExportHistory()
  },

  filters: {
    formatDate(time) {
      return formatDate(time, 'yyyy-MM-dd HH:mm')
    }
  },

  computed: {
    // 错误数量
    errorCount() {
      return this.previewData.filter(item => item.hasError).length
    },

    // 有效数据数量
    validDataCount() {
      return this.previewData.filter(item => !item.hasError).length
    }
  },

  methods: {
    // 获取部门列表
    async fetchDepartmentList() {
      try {
        const response = await budgetApi.getDepartmentList()
        if (response.code === 1) {
          this.departmentList = response.data || []
        }
      } catch (error) {
        console.error('获取部门列表异常:', error)
      }
    },

    // 获取导入历史
    async fetchImportHistory() {
      try {
        const response = await budgetApi.getImportHistory()
        if (response.code === 1) {
          this.importHistory = response.data || []
        }
      } catch (error) {
        console.error('获取导入历史异常:', error)
      }
    },

    // 获取导出历史
    async fetchExportHistory() {
      try {
        const response = await budgetApi.getExportHistory()
        if (response.code === 1) {
          this.exportHistory = response.data || []
        }
      } catch (error) {
        console.error('获取导出历史异常:', error)
      }
    },

    // 获取模板类型名称
    getTemplateTypeName(type) {
      const typeMap = {
        'annual': '年度预算模板',
        'quarterly': '季度预算模板',
        'monthly': '月度预算模板',
        'project': '项目预算模板'
      }
      return typeMap[type] || '预算模板'
    },

    // 下载模板
    async handleDownloadTemplate() {
      try {
        const response = await budgetApi.downloadTemplate(this.selectedTemplateType)

        // 处理文件下载
        const blob = new Blob([response], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `${this.getTemplateTypeName(this.selectedTemplateType)}_${new Date().getTime()}.xlsx`
        link.click()
        window.URL.revokeObjectURL(url)

        this.$message.success('模板下载成功')
        this.importStep = 1
      } catch (error) {
        console.error('下载模板异常:', error)
        this.$message.error('下载模板失败，请稍后重试')
      }
    },

    // 预览模板
    handlePreviewTemplate() {
      const content = `
        <p><b>模板说明：</b></p>
        <p>1. 第一行为表头，请勿修改</p>
        <p>2. 必填字段：预算项目、预算金额、所属部门、年度</p>
        <p>3. 金额请使用纯数字格式，不要包含逗号或货币符号</p>
        <p>4. 日期请使用 YYYY-MM-DD 格式</p>
        <p>请下载模板后查看完整结构。</p>
      `
      this.$alert(content, '模板预览', { dangerouslyUseHTMLString: true })
    },

    // 上传前校验
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

    // 提交上传
    handleSubmitUpload() {
      if (this.fileList.length === 0) {
        this.$message.warning('请选择要上传的文件')
        return
      }

      this.uploading = true
      this.uploadStatus = 'active'
      this.uploadStatusText = '正在上传文件...'
      this.uploadPercent = 0

      this.$refs.upload.submit()
    },

    // 上传进度
    handleUploadProgress(event) {
      this.uploadPercent = Math.round(event.percent)
      this.uploadStatusText = `正在上传... ${this.uploadPercent}%`
    },

    // 上传成功
    handleUploadSuccess(response) {
      this.uploading = false
      this.uploadStatus = response.code === 1 ? 'success' : 'exception'

      if (response.code === 1) {
        this.uploadStatusText = '文件上传成功，正在解析数据...'
        this.previewData = response.data || []
        this.importStep = 3
        this.$message.success('文件上传成功')
      } else {
        this.uploadStatusText = response.message || '上传失败'
        this.$message.error(response.message || '文件上传失败')
      }
    },

    // 上传失败
    handleUploadError(error) {
      this.uploading = false
      this.uploadStatus = 'exception'
      this.uploadStatusText = '上传失败'
      this.$message.error('文件上传失败，请稍后重试')
      console.error('上传失败:', error)
    },

    // 确认导入
    async handleConfirmImport() {
      if (this.validDataCount === 0) {
        this.$message.warning('没有有效数据可导入')
        return
      }

      this.importing = true
      try {
        const validData = this.previewData.filter(item => !item.hasError)
        const response = await budgetApi.confirmImport(validData)

        if (response.code === 1) {
          this.importResult = {
            success: true,
            title: '导入成功',
            message: `成功导入 ${response.data.successCount} 条数据`,
            successCount: response.data.successCount,
            failCount: response.data.failCount,
            totalCount: response.data.totalCount
          }
          this.importStep = 4
          this.fetchImportHistory()
        } else {
          this.importResult = {
            success: false,
            title: '导入失败',
            message: response.message || '数据导入失败',
            successCount: 0,
            failCount: this.validDataCount,
            totalCount: this.validDataCount
          }
          this.importStep = 4
        }
      } catch (error) {
        console.error('确认导入异常:', error)
        this.$message.error('数据导入失败，请稍后重试')
      } finally {
        this.importing = false
      }
    },

    // 继续导入
    handleNewImport() {
      this.importStep = 0
      this.fileList = []
      this.previewData = []
      this.importResult = {
        success: false,
        title: '',
        message: '',
        successCount: 0,
        failCount: 0,
        totalCount: 0
      }
    },

    // 查看导入数据
    handleViewImportedData() {
      this.$router.push('/financialSharing/budget')
    },

    // 导出错误日志
    handleExportErrorLog() {
      try {
        const data = this.errorLogs || this.importHistory || []
        if (!data.length) { this.$message.warning('暂无错误日志可导出'); return }
        const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `错误日志_${new Date().getTime()}.json`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
      }
    },

    // 查看历史详情
    handleViewHistoryDetail(row) {
      const content = `<p><b>文件名：</b>${row.fileName || '-'}</p><p><b>导入时间：</b>${row.createTime || row.importTime || '-'}</p><p><b>状态：</b>${row.statusName || row.status || '-'}</p><p><b>成功记录：</b>${row.successCount || 0}</p><p><b>失败记录：</b>${row.failCount || 0}</p><p><b>操作人：</b>${row.operator || '-'}</p>`
      this.$alert(content, '历史详情', { dangerouslyUseHTMLString: true })
    },

    // 导出数据
    async handleExport() {
      try {
        await this.$refs.exportForm.validate()

        this.exporting = true
        const response = await budgetApi.exportData(this.exportForm)

        // 处理文件下载
        const blob = new Blob([response], {
          type: this.getContentType(this.exportForm.exportFormat)
        })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `预算数据_${new Date().getTime()}.${this.exportForm.exportFormat}`
        link.click()
        window.URL.revokeObjectURL(url)

        this.$message.success('数据导出成功')
        this.fetchExportHistory()
      } catch (error) {
        console.error('导出数据异常:', error)
        this.$message.error('导出数据失败，请稍后重试')
      } finally {
        this.exporting = false
      }
    },

    // 批量导出
    handleBatchExport() {
      if (!this.multipleSelection || !this.multipleSelection.length) {
        this.$message.warning('请先选择要导出的数据')
        return
      }
      try {
        const blob = new Blob([JSON.stringify(this.multipleSelection, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `批量导出_${new Date().getTime()}.json`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success(`已导出 ${this.multipleSelection.length} 条数据`)
      } catch (e) {
        this.$message.error('批量导出失败')
      }
    },

    // 重置导出条件
    handleResetExport() {
      this.$refs.exportForm.resetFields()
    },

    // 下载导出文件
    handleDownloadExport(row) {
      try {
        const fileName = row.fileName || `历史文件_${row.id || new Date().getTime()}.json`
        const blob = new Blob([JSON.stringify(row, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = fileName
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('下载成功')
      } catch (e) {
        this.$message.error('下载失败')
      }
    },

    // 获取内容类型
    getContentType(format) {
      const typeMap = {
        'xlsx': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        'csv': 'text/csv',
        'pdf': 'application/pdf'
      }
      return typeMap[format] || 'application/octet-stream'
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-import-export-container {
  padding: 20px;

  .header-card {
    margin-bottom: 16px;

    .header-title {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-actions {
        display: flex;
        gap: 8px;
      }
    }
  }

  .import-card {
    margin-bottom: 16px;

    .history-badge {
      margin-left: 8px;
    }

    .import-steps {
      margin: 20px 0;
    }

    .import-content {
      margin-top: 20px;
    }

    .step-content {
      min-height: 300px;

      .template-download {
        margin-top: 20px;

        h4 {
          margin-bottom: 10px;
        }

        .template-actions {
          margin-top: 15px;
          display: flex;
          gap: 10px;
        }
      }

      .data-guide {
        margin-top: 20px;

        h4 {
          margin-bottom: 10px;
        }

        ul {
          margin: 10px 0;
          padding-left: 20px;

          li {
            margin: 5px 0;
            color: #606266;
          }
        }

        .step-actions {
          margin-top: 15px;
        }
      }

      .upload-actions {
        margin-top: 20px;
        display: flex;
        gap: 10px;
      }

      .upload-progress {
        margin-top: 20px;
        text-align: center;

        p {
          margin-top: 10px;
          color: #606266;
        }
      }

      .preview-table {
        margin: 20px 0;

        .error-cell {
          color: #F56C6C;
          background-color: #FEF0F0;
        }

        .error-text {
          color: #F56C6C;
          font-size: 12px;
        }
      }

      .preview-actions {
        margin-top: 20px;
        display: flex;
        gap: 10px;
      }

      .no-data {
        text-align: center;
        padding: 40px 0;
      }

      .import-stats {
        p {
          margin: 8px 0;
          font-size: 16px;
        }
      }

      .result-actions {
        margin-top: 20px;
        display: flex;
        gap: 10px;
        justify-content: center;
      }
    }

    .import-history {
      margin-top: 20px;
    }
  }

  .export-card {
    .export-filters {
      margin: 20px 0;
      padding: 15px;
      background: #F5F7FA;
      border-radius: 4px;
    }

    .export-history {
      margin-top: 30px;

      h4 {
        margin-bottom: 15px;
      }
    }
  }
}
</style>