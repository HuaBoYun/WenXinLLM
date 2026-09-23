<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>报表数据导入导出</span>
      </div>

      <!-- 导入区域 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="hover">
            <div slot="header">
              <i class="el-icon-upload" />
              <span> 数据导入</span>
            </div>
            <div class="import-section">
              <el-steps :active="importStep" finish-status="success" align-center>
                <el-step title="下载模板" />
                <el-step title="填写数据" />
                <el-step title="上传文件" />
                <el-step title="导入完成" />
              </el-steps>

              <div class="step-content">
                <!-- 步骤1: 下载模板 -->
                <div v-if="importStep === 0" class="step-item">
                  <p class="step-desc">请先下载Excel导入模板，按照模板格式填写数据</p>
                  <el-button type="primary" icon="el-icon-download" @click="handleDownloadTemplate">
                    下载导入模板
                  </el-button>
                  <el-button type="text" @click="importStep = 1">跳过，直接上传</el-button>
                </div>

                <!-- 步骤2: 填写数据 -->
                <div v-if="importStep === 1" class="step-item">
                  <p class="step-desc">请在下载的模板中填写数据，注意以下事项：</p>
                  <ul class="tips-list">
                    <li>带*号的列为必填项</li>
                    <li>任务ID、模板ID、指标ID、组织ID、期间为必填</li>
                    <li>数据来源可选值：MANUAL(手工)、FETCH(取数)、CALCULATION(计算)</li>
                    <li>是否可编辑可选值：Y(是)、N(否)</li>
                    <li>维度值需要符合JSON格式，如：{"dim1":"value1"}</li>
                  </ul>
                  <el-button type="primary" @click="importStep = 2">下一步</el-button>
                  <el-button @click="importStep = 0">上一步</el-button>
                </div>

                <!-- 步骤3: 上传文件 -->
                <div v-if="importStep === 2" class="step-item">
                  <p class="step-desc">请选择填写好的Excel文件进行上传</p>
                  <el-upload
                    ref="upload"
                    class="upload-demo"
                    drag
                    action="#"
                    :auto-upload="false"
                    :on-change="handleFileChange"
                    :file-list="fileList"
                    :limit="1"
                    accept=".xlsx"
                  >
                    <i class="el-icon-upload" />
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    <div slot="tip" class="el-upload__tip">只能上传.xlsx文件</div>
                  </el-upload>
                  <div class="button-group">
                    <el-button type="primary" :loading="uploading" @click="handleUpload">
                      开始导入
                    </el-button>
                    <el-button @click="importStep = 1">上一步</el-button>
                  </div>
                </div>

                <!-- 步骤4: 导入完成 -->
                <div v-if="importStep === 3" class="step-item">
                  <el-result
                    :icon="importResult.hasError ? 'warning' : 'success'"
                    :title="importResult.hasError ? '导入完成，但存在错误' : '导入成功'"
                  >
                    <template slot="subTitle">
                      <p>总行数: {{ importResult.totalRows }}</p>
                      <p>成功: <span class="text-success">{{ importResult.successRows }}</span> 行</p>
                      <p v-if="importResult.failRows > 0">
                        失败: <span class="text-danger">{{ importResult.failRows }}</span> 行
                      </p>
                    </template>
                    <template slot="extra">
                      <el-button type="primary" @click="resetImport">重新导入</el-button>
                      <el-button @click="$emit('refresh')">刷新列表</el-button>
                    </template>
                  </el-result>

                  <!-- 错误信息 -->
                  <div v-if="importResult.errorMessages && importResult.errorMessages.length > 0" class="error-section">
                    <el-divider content-position="left">错误详情</el-divider>
                    <el-alert
                      v-for="(error, index) in importResult.errorMessages"
                      :key="index"
                      :title="error"
                      type="error"
                      :closable="false"
                      style="margin-bottom: 10px;"
                    />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 导出区域 -->
        <el-col :span="12">
          <el-card shadow="hover">
            <div slot="header">
              <i class="el-icon-download" />
              <span> 数据导出</span>
            </div>
            <div class="export-section">
              <p class="section-desc">根据查询条件导出报表数据为Excel文件</p>

              <el-form :model="exportForm" label-width="100px">
                <el-form-item label="报表任务">
                  <el-select v-model="exportForm.taskId" placeholder="请选择报表任务" clearable>
                    <el-option
                      v-for="item in taskOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="指标">
                  <el-select v-model="exportForm.indicatorId" placeholder="请选择指标" clearable>
                    <el-option
                      v-for="item in indicatorOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="组织ID">
                  <el-input v-model="exportForm.orgId" placeholder="请输入组织ID" clearable />
                </el-form-item>
                <el-form-item label="期间">
                  <el-input v-model="exportForm.period" placeholder="如:202401" clearable />
                </el-form-item>
                <el-form-item label="数据来源">
                  <el-select v-model="exportForm.dataSource" placeholder="请选择数据来源" clearable>
                    <el-option label="手工" value="MANUAL" />
                    <el-option label="取数" value="FETCH" />
                    <el-option label="计算" value="CALCULATION" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="success" icon="el-icon-download" :loading="exporting" @click="handleExport">
                    导出数据
                  </el-button>
                  <el-button @click="resetExportForm">重置</el-button>
                </el-form-item>
              </el-form>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { downloadTemplate, importExcel, exportExcel } from '@/api/financialSharing/enterpriseReport/reportDataImport'
import { getReportTaskList } from '@/api/financialSharing/enterpriseReport/reportTask'
import { getIndicatorInfoList } from '@/api/financialSharing/enterpriseReport/indicatorInfo'

export default {
  name: 'ReportDataImportExport',
  data() {
    return {
      // 导入相关
      importStep: 0,
      fileList: [],
      uploading: false,
      importResult: {
        totalRows: 0,
        successRows: 0,
        failRows: 0,
        errorMessages: [],
        hasError: false
      },
      // 导出相关
      exporting: false,
      exportForm: {
        taskId: '',
        indicatorId: '',
        orgId: '',
        period: '',
        dataSource: ''
      },
      // 选项数据
      taskOptions: [],
      indicatorOptions: []
    }
  },
  created() {
    this.loadTaskOptions()
    this.loadIndicatorOptions()
  },
  methods: {
    /** 加载任务选项 */
    loadTaskOptions() {
      getReportTaskList({ status: 'PUBLISHED' }).then(res => {
        if (res.code === 200 && res.data) {
          this.taskOptions = res.data.map(item => ({
            label: item.taskName,
            value: item.taskId
          }))
        }
      })
    },
    /** 加载指标选项 */
    loadIndicatorOptions() {
      getIndicatorInfoList({ status: 'ENABLED' }).then(res => {
        if (res.code === 200 && res.data) {
          this.indicatorOptions = res.data.map(item => ({
            label: item.indicatorName,
            value: item.indicatorId
          }))
        }
      })
    },
    /** 下载导入模板 */
    handleDownloadTemplate() {
      downloadTemplate().then(res => {
        const blob = new Blob([res], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '报表数据导入模板.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('模板下载成功')
        this.importStep = 1
      }).catch(() => {
        this.$message.error('模板下载失败')
      })
    },
    /** 文件选择变化 */
    handleFileChange(file, fileList) {
      this.fileList = fileList
    },
    /** 上传并导入 */
    handleUpload() {
      if (this.fileList.length === 0) {
        this.$message.warning('请先选择要导入的文件')
        return
      }

      const formData = new FormData()
      formData.append('file', this.fileList[0].raw)

      this.uploading = true
      importExcel(formData).then(res => {
        this.uploading = false
        if (res.code === 200) {
          this.importResult = res.data || {}
          this.importResult.hasError = this.importResult.failRows > 0
          this.importStep = 3
          if (this.importResult.hasError) {
            this.$message.warning('导入完成，但存在错误，请查看错误详情')
          } else {
            this.$message.success('导入成功')
          }
        } else {
          this.$message.error(res.msg || '导入失败')
        }
      }).catch(() => {
        this.uploading = false
        this.$message.error('导入失败')
      })
    },
    /** 导出数据 */
    handleExport() {
      this.exporting = true
      exportExcel(this.exportForm).then(res => {
        this.exporting = false
        const blob = new Blob([res], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '报表数据_' + new Date().getTime() + '.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      }).catch(() => {
        this.exporting = false
        this.$message.error('导出失败')
      })
    },
    /** 重置导入 */
    resetImport() {
      this.importStep = 0
      this.fileList = []
      this.importResult = {
        totalRows: 0,
        successRows: 0,
        failRows: 0,
        errorMessages: [],
        hasError: false
      }
    },
    /** 重置导出表单 */
    resetExportForm() {
      this.exportForm = {
        taskId: '',
        indicatorId: '',
        orgId: '',
        period: '',
        dataSource: ''
      }
    }
  }
}
</script>

<style scoped>
.import-section,
.export-section {
  padding: 20px;
}

.step-content {
  margin-top: 30px;
}

.step-item {
  text-align: center;
  padding: 20px;
}

.step-desc {
  font-size: 14px;
  color: #606266;
  margin-bottom: 20px;
}

.tips-list {
  text-align: left;
  max-width: 500px;
  margin: 0 auto 20px;
  padding-left: 20px;
}

.tips-list li {
  margin-bottom: 8px;
  color: #909399;
}

.button-group {
  margin-top: 20px;
}

.upload-demo {
  margin-bottom: 20px;
}

.error-section {
  margin-top: 20px;
  text-align: left;
  max-height: 300px;
  overflow-y: auto;
}

.section-desc {
  font-size: 14px;
  color: #909399;
  margin-bottom: 20px;
}

.text-success {
  color: #67c23a;
  font-weight: bold;
}

.text-danger {
  color: #f56c6c;
  font-weight: bold;
}
</style>


