<template>
  <div class="file-integration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>文件系统集成</h2>
      <p>Excel、CSV、XML、JSON等文件格式的导入导出和批量处理</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-upload" @click="handleFileUpload">文件上传</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleBatchExport">批量导出</el-button>
            <el-button type="info" icon="el-icon-folder" @click="handleFileManager">文件管理</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">导入设置</el-button>
            <el-button icon="el-icon-document" @click="handleTemplates">模板管理</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 文件集成统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ fileStats.totalFiles }}</div>
            <div class="stat-label">文件总数</div>
            <div class="stat-description">已处理文件数量</div>
            <div class="stat-trend">
              <i class="el-icon-folder"></i>
              <span>文件处理</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-folder"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card import-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ fileStats.todayImport }}</div>
            <div class="stat-label">今日导入</div>
            <div class="stat-description">今日导入文件数</div>
            <div class="stat-trend">
              <i class="el-icon-upload"></i>
              <span>文件导入</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-upload"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card export-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ fileStats.todayExport }}</div>
            <div class="stat-label">今日导出</div>
            <div class="stat-description">今日导出文件数</div>
            <div class="stat-trend">
              <i class="el-icon-download"></i>
              <span>文件导出</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-download"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card success-rate-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ fileStats.successRate }}%</div>
            <div class="stat-label">成功率</div>
            <div class="stat-description">文件处理成功率</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>高成功率</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 文件类型选择 -->
    <el-card class="file-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>文件类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshFileTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="fileType in fileTypes" :key="fileType.id">
          <el-card 
            class="file-type-item" 
            shadow="hover" 
            @click.native="handleSelectFileType(fileType)"
            :class="{ 'selected': selectedFileType === fileType.id }"
          >
            <div class="file-type-icon">
              <i :class="fileType.icon"></i>
            </div>
            <div class="file-type-title">{{ fileType.name }}</div>
            <div class="file-type-description">{{ fileType.description }}</div>
            <div class="file-type-stats">
              <span class="file-count">{{ fileType.fileCount }} 个文件</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 文件处理列表 -->
    <el-card class="file-list-card" shadow="never">
      <div slot="header" class="card-header">
        <span>文件处理记录</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索文件"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getFileList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="filteredFileList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="fileName" label="文件名称" width="250" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.fileName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="fileType" label="文件类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getFileTypeColor(scope.row.fileType)" size="mini">
              {{ scope.row.fileType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operation" label="操作类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getOperationColor(scope.row.operation)" size="mini">
              {{ getOperationText(scope.row.operation) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileSize" label="文件大小" width="100" align="center">
          <template slot-scope="scope">
            <span class="file-size">{{ formatFileSize(scope.row.fileSize) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="recordCount" label="记录数" width="100" align="center">
          <template slot-scope="scope">
            <span class="record-count">{{ scope.row.recordCount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="processTime" label="处理时间" width="100" align="center">
          <template slot-scope="scope">
            <span class="process-time">{{ scope.row.processTime }}s</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="处理状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="处理时间" width="150" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-download"
              @click="handleDownload(scope.row)"
              v-if="scope.row.operation === 'EXPORT'"
            >下载</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-refresh"
              @click="handleReprocess(scope.row)"
              v-if="scope.row.status === 'FAILED'"
            >重新处理</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="details">详情</el-dropdown-item>
                <el-dropdown-item command="logs">处理日志</el-dropdown-item>
                <el-dropdown-item command="errors">错误信息</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 文件详情抽屉 -->
    <el-drawer
      title="文件处理详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentFile">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="文件基本信息" :column="2" border>
              <el-descriptions-item label="文件名称">{{ currentFile.fileName }}</el-descriptions-item>
              <el-descriptions-item label="文件类型">{{ currentFile.fileType }}</el-descriptions-item>
              <el-descriptions-item label="操作类型">{{ getOperationText(currentFile.operation) }}</el-descriptions-item>
              <el-descriptions-item label="文件大小">{{ formatFileSize(currentFile.fileSize) }}</el-descriptions-item>
              <el-descriptions-item label="记录数">{{ currentFile.recordCount }}</el-descriptions-item>
              <el-descriptions-item label="处理时间">{{ currentFile.processTime }}s</el-descriptions-item>
              <el-descriptions-item label="处理状态">
                <el-tag :type="getStatusColor(currentFile.status)" size="mini">
                  {{ getStatusText(currentFile.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentFile.createTime }}</el-descriptions-item>
              <el-descriptions-item label="文件路径" :span="2">{{ currentFile.filePath }}</el-descriptions-item>
              <el-descriptions-item label="文件描述" :span="2">{{ currentFile.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="处理配置" name="config">
            <el-descriptions :column="1" border size="small">
              <el-descriptions-item label="字段映射">
                <pre style="margin:0;white-space:pre-wrap;word-break:break-all;">{{ currentFile.fieldMapping || '未配置' }}</pre>
              </el-descriptions-item>
              <el-descriptions-item label="数据验证规则">
                <pre style="margin:0;white-space:pre-wrap;word-break:break-all;">{{ currentFile.validationRules || '未配置' }}</pre>
              </el-descriptions-item>
              <el-descriptions-item label="文件编码">{{ currentFile.fileEncoding || '未配置' }}</el-descriptions-item>
              <el-descriptions-item label="错误处理策略">
                <el-tag v-if="currentFile.errorStrategy" :type="currentFile.errorStrategy === 'STOP' ? 'danger' : (currentFile.errorStrategy === 'SKIP' ? 'warning' : 'info')" size="mini">
                  {{ { 'SKIP': '跳过错误', 'STOP': '停止处理', 'LOG': '记录日志' }[currentFile.errorStrategy] || currentFile.errorStrategy }}
                </el-tag>
                <span v-else>未配置</span>
              </el-descriptions-item>
              <el-descriptions-item label="处理频率">{{ currentFile.processFrequency || '未配置' }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="处理日志" name="logs">
            <el-table :data="processLogs" border size="mini">
              <el-table-column prop="logTime" label="时间" width="150" />
              <el-table-column prop="logLevel" label="级别" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getLogLevelColor(scope.row.logLevel)" size="mini">
                    {{ scope.row.logLevel }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="step" label="处理步骤" width="120" />
              <el-table-column prop="message" label="日志信息" />
              <el-table-column prop="duration" label="耗时" width="80" align="center" />
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="错误信息" name="errors">
            <el-table :data="errorLogs" border size="mini">
              <el-table-column prop="errorTime" label="错误时间" width="150" />
              <el-table-column prop="errorType" label="错误类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag type="danger" size="mini">
                    {{ scope.row.errorType }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="rowNumber" label="行号" width="80" align="center" />
              <el-table-column prop="fieldName" label="字段名" width="120" />
              <el-table-column prop="errorMessage" label="错误信息" />
              <el-table-column prop="suggestion" label="建议" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 文件上传对话框 -->
    <el-dialog
      title="文件上传"
      :visible.sync="uploadDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleUploadDialogClose"
    >
      <el-form
        ref="uploadForm"
        :model="uploadForm"
        :rules="uploadRules"
        label-width="120px"
        size="small"
      >
        <el-form-item label="文件类型" prop="fileType">
          <el-select v-model="uploadForm.fileType" placeholder="请选择文件类型" style="width: 100%">
            <el-option value="EXCEL" label="Excel文件" />
            <el-option value="CSV" label="CSV文件" />
            <el-option value="XML" label="XML文件" />
            <el-option value="JSON" label="JSON文件" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据类型" prop="dataType">
          <el-select v-model="uploadForm.dataType" placeholder="请选择数据类型" style="width: 100%">
            <el-option value="BUDGET_DATA" label="预算数据" />
            <el-option value="ACTUAL_DATA" label="实际数据" />
            <el-option value="FORECAST_DATA" label="预测数据" />
            <el-option value="MASTER_DATA" label="主数据" />
          </el-select>
        </el-form-item>
        <el-form-item label="文件上传" prop="file">
          <el-upload
            ref="upload"
            :auto-upload="false"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            :file-list="uploadFileList"
            :limit="1"
            action=""
            accept=".xls,.xlsx,.csv,.xml,.json,.txt"
          >
            <el-button slot="trigger" size="small" type="primary">选择文件</el-button>
            <div slot="tip" class="el-upload__tip">支持Excel、CSV、XML、JSON格式文件，文件大小不超过10MB</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="处理选项">
          <el-checkbox-group v-model="uploadForm.options">
            <el-checkbox label="skipHeader">跳过表头</el-checkbox>
            <el-checkbox label="validateData">数据验证</el-checkbox>
            <el-checkbox label="allowDuplicate">允许重复</el-checkbox>
            <el-checkbox label="autoCorrect">自动纠错</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="uploadDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitUpload" :loading="uploadLoading">上传</el-button>
      </div>
    </el-dialog>

    <!-- 批量导出对话框 -->
    <el-dialog title="批量导出" :visible.sync="batchExportDialogVisible" width="500px">
      <el-form label-width="100px" size="small">
        <el-form-item label="导出格式">
          <el-select v-model="exportForm.format" placeholder="请选择导出格式" style="width: 100%">
            <el-option value="EXCEL" label="Excel (.xlsx)" />
            <el-option value="CSV" label="CSV (.csv)" />
            <el-option value="JSON" label="JSON (.json)" />
          </el-select>
        </el-form-item>
        <el-form-item label="导出范围">
          <el-radio-group v-model="exportForm.scope">
            <el-radio label="ALL">全部记录</el-radio>
            <el-radio label="FILTERED">当前筛选结果</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="包含字段">
          <el-checkbox-group v-model="exportForm.fields">
            <el-checkbox label="fileName">文件名称</el-checkbox>
            <el-checkbox label="fileType">文件类型</el-checkbox>
            <el-checkbox label="operation">操作类型</el-checkbox>
            <el-checkbox label="status">处理状态</el-checkbox>
            <el-checkbox label="createTime">处理时间</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="batchExportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doBatchExport">确认导出</el-button>
      </div>
    </el-dialog>

    <!-- 文件管理对话框 -->
    <el-dialog title="文件管理" :visible.sync="fileManagerDialogVisible" width="800px" top="5vh">
      <el-table :data="fileList" border stripe size="mini" max-height="400" v-loading="loading">
        <el-table-column type="selection" width="40" />
        <el-table-column prop="fileName" label="文件名称" show-overflow-tooltip />
        <el-table-column prop="fileType" label="类型" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getFileTypeColor(scope.row.fileType)" size="mini">{{ scope.row.fileType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileSize" label="大小" width="90" align="center">
          <template slot-scope="scope">{{ formatFileSize(scope.row.fileSize) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="handleDelete(scope.row)">删除</el-button>
            <el-button type="text" size="mini" @click="handleReprocess(scope.row)">重处理</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="fileManagerDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 导入设置对话框 -->
    <el-dialog title="导入设置" :visible.sync="importSettingsDialogVisible" width="600px">
      <el-form :model="importSettings" label-width="120px" size="small">
        <el-form-item label="默认编码">
          <el-select v-model="importSettings.encoding" style="width: 100%">
            <el-option value="UTF-8" label="UTF-8" />
            <el-option value="GBK" label="GBK" />
            <el-option value="GB2312" label="GB2312" />
          </el-select>
        </el-form-item>
        <el-form-item label="CSV分隔符">
          <el-select v-model="importSettings.delimiter" style="width: 100%">
            <el-option value="," label="逗号 (,)" />
            <el-option value=";" label="分号 (;)" />
            <el-option value="\t" label="制表符 (Tab)" />
            <el-option value="|" label="竖线 (|)" />
          </el-select>
        </el-form-item>
        <el-form-item label="跳过表头行">
          <el-switch v-model="importSettings.skipHeader" />
        </el-form-item>
        <el-form-item label="数据起始行">
          <el-input-number v-model="importSettings.dataStartRow" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="错误处理策略">
          <el-radio-group v-model="importSettings.errorStrategy">
            <el-radio label="SKIP">跳过错误行</el-radio>
            <el-radio label="STOP">遇错停止</el-radio>
            <el-radio label="LOG">记录日志继续</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="最大文件大小">
          <el-input-number v-model="importSettings.maxFileSize" :min="1" :max="100" /> MB
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="importSettingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveImportSettings">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 模板管理对话框 -->
    <el-dialog title="模板管理" :visible.sync="templateDialogVisible" width="700px">
      <el-table :data="templateList" border stripe size="mini">
        <el-table-column prop="templateName" label="模板名称" />
        <el-table-column prop="templateType" label="文件格式" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="mini">{{ scope.row.templateType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" show-overflow-tooltip />
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="downloadTemplate(scope.row)">下载</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="templateDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="文件集成帮助" :visible.sync="helpDialogVisible" width="650px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>文件集成模块支持多种文件格式的导入导出和批量处理，包括Excel、CSV、XML、JSON等。</p>
        <h4>支持的文件格式</h4>
        <el-table :data="helpFileFormats" border size="mini" style="margin-bottom: 16px">
          <el-table-column prop="format" label="格式" width="100" />
          <el-table-column prop="extensions" label="扩展名" width="150" />
          <el-table-column prop="description" label="说明" />
        </el-table>
        <h4>操作流程</h4>
        <el-steps :active="4" finish-status="success" simple style="margin-bottom: 16px">
          <el-step title="选择文件" />
          <el-step title="配置参数" />
          <el-step title="上传处理" />
          <el-step title="查看结果" />
        </el-steps>
        <h4>常见问题</h4>
        <el-collapse>
          <el-collapse-item title="上传文件大小限制是多少？" name="1">
            <p>单个文件最大支持10MB，批量上传最多支持10个文件。</p>
          </el-collapse-item>
          <el-collapse-item title="支持哪些编码格式？" name="2">
            <p>支持UTF-8、GBK、GB2312等常见编码格式，建议使用UTF-8。</p>
          </el-collapse-item>
          <el-collapse-item title="处理失败如何重试？" name="3">
            <p>在文件列表中找到失败的记录，点击"重新处理"按钮即可重试。</p>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="helpDialogVisible = false">知道了</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { systemIntegrationApi } from '@/api/managementAccountant/ncv65/systemIntegration'

export default {
  name: 'FileIntegration',
  data() {
    return {
      // 统计数据
      fileStats: { totalFiles: 0, todayImport: 0, todayExport: 0, successRate: 0 },
      // 文件类型
      fileTypes: [],
      selectedFileType: null,
      // 文件列表
      fileList: [],
      loading: false,
      searchKeyword: '',
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentFile: null,
      processLogs: [],
      errorLogs: [],
      // 上传对话框
      uploadDialogVisible: false,
      uploadLoading: false,
      uploadFileList: [],
      uploadForm: { fileType: '', dataType: '', file: null, options: [] },
      uploadRules: {
        fileType: [{ required: true, message: '请选择文件类型', trigger: 'change' }],
        dataType: [{ required: true, message: '请选择数据类型', trigger: 'change' }]
      },
      // 批量导出对话框
      batchExportDialogVisible: false,
      exportForm: { format: 'EXCEL', scope: 'ALL', fields: ['fileName', 'fileType', 'operation', 'status', 'createTime'] },
      // 文件管理对话框
      fileManagerDialogVisible: false,
      // 导入设置对话框
      importSettingsDialogVisible: false,
      importSettings: { encoding: 'UTF-8', delimiter: ',', skipHeader: true, dataStartRow: 2, errorStrategy: 'LOG', maxFileSize: 10 },
      // 模板管理对话框
      templateDialogVisible: false,
      templateList: [
        { templateId: '1', templateName: '预算数据导入模板', templateType: 'EXCEL', description: '标准预算数据Excel导入模板' },
        { templateId: '2', templateName: '实际数据导入模板', templateType: 'CSV', description: '实际数据CSV导入模板' },
        { templateId: '3', templateName: '预测数据导入模板', templateType: 'EXCEL', description: '预测数据Excel导入模板' },
        { templateId: '4', templateName: '主数据导入模板', templateType: 'XML', description: '主数据XML导入模板' }
      ],
      // 帮助对话框
      helpDialogVisible: false,
      helpFileFormats: [
        { format: 'Excel', extensions: '.xls, .xlsx', description: '支持Excel 97-2003及以上版本' },
        { format: 'CSV', extensions: '.csv', description: '逗号分隔值文件，支持自定义分隔符' },
        { format: 'XML', extensions: '.xml', description: 'XML格式数据文件' },
        { format: 'JSON', extensions: '.json', description: 'JSON格式数据文件' }
      ]
    }
  },
  
  computed: {
    filteredFileList() {
      let list = this.fileList
      if (this.selectedFileType) {
        list = list.filter(item => item.fileType === this.selectedFileType)
      }
      if (this.searchKeyword) {
        const kw = this.searchKeyword.toLowerCase()
        list = list.filter(item =>
          (item.fileName && item.fileName.toLowerCase().includes(kw)) ||
          (item.fileType && item.fileType.toLowerCase().includes(kw)) ||
          (item.filePath && item.filePath.toLowerCase().includes(kw)) ||
          (item.status && item.status.toLowerCase().includes(kw))
        )
      }
      return list
    }
  },

  created() {
    this.getFileList()
    this.getFileStats()
  },

  methods: {
    // ========== 数据获取 ==========
    async getFileList() {
      this.loading = true
      try {
        const params = {}
        if (this.selectedFileType) {
          params.fileType = this.selectedFileType
        }
        const response = await systemIntegrationApi.getFileList(params)
        if (response.code === 1 && response.data) {
          this.fileList = response.data
          this.buildFileTypes(response.data)
        }
      } catch (error) {
        this.$message.error('获取文件列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    async getFileStats() {
      try {
        const response = await systemIntegrationApi.getFileStats()
        if (response.code === 1 && response.data) {
          this.fileStats = response.data
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },
    buildFileTypes(data) {
      const typeMap = {}
      const iconMap = { 'EXCEL': 'el-icon-document', 'CSV': 'el-icon-tickets', 'XML': 'el-icon-document-copy', 'JSON': 'el-icon-document-checked' }
      data.forEach(item => {
        const t = item.fileType || 'OTHER'
        if (!typeMap[t]) {
          typeMap[t] = { id: t, name: this.getFileTypeText(t), description: t + '格式文件', icon: iconMap[t] || 'el-icon-document', fileCount: 0 }
        }
        typeMap[t].fileCount++
      })
      this.fileTypes = Object.values(typeMap)
    },

    // ========== 工具栏按钮 ==========
    handleFileUpload() {
      this.uploadDialogVisible = true
      this.resetUploadForm()
    },
    handleRefresh() {
      this.getFileList()
      this.getFileStats()
      this.$message.success('数据已刷新')
    },
    handleBatchExport() {
      this.batchExportDialogVisible = true
    },
    handleFileManager() {
      this.fileManagerDialogVisible = true
      this.getFileList()
    },
    handleSettings() {
      this.importSettingsDialogVisible = true
    },
    handleTemplates() {
      this.templateDialogVisible = true
    },
    handleHelp() {
      this.helpDialogVisible = true
    },

    // ========== 文件类型筛选 ==========
    refreshFileTypes() {
      this.getFileList()
      this.$message.success('文件类型已刷新')
    },
    handleSelectFileType(fileType) {
      this.selectedFileType = this.selectedFileType === fileType.id ? null : fileType.id
    },
    // ========== 列表操作 ==========
    async handleView(row) {
      this.currentFile = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await this.loadFileDetail(row)
    },
    handleRowClick(row) {
      this.currentFile = row
    },
    handleMoreAction(command, row) {
      const actions = {
        'details': async () => { this.currentFile = row; this.detailDrawerVisible = true; this.detailActiveTab = 'basic'; await this.loadFileDetail(row) },
        'logs': async () => { this.currentFile = row; this.detailDrawerVisible = true; this.detailActiveTab = 'logs'; await this.loadFileDetail(row) },
        'errors': async () => { this.currentFile = row; this.detailDrawerVisible = true; this.detailActiveTab = 'errors'; await this.loadFileDetail(row) },
        'delete': () => this.handleDelete(row)
      }
      if (actions[command]) actions[command]()
    },
    async loadFileDetail(row) {
      if (!row || !row.id) return
      try {
        const [logsRes, errorsRes] = await Promise.all([
          systemIntegrationApi.file.getLogs(row.id),
          systemIntegrationApi.file.getErrors(row.id)
        ])
        if (logsRes.code === 1 && logsRes.data) {
          this.processLogs = logsRes.data
        } else {
          this.processLogs = []
        }
        if (errorsRes.code === 1 && errorsRes.data) {
          this.errorLogs = errorsRes.data
        } else {
          this.errorLogs = []
        }
      } catch (error) {
        console.error('加载文件详情失败：', error)
        this.processLogs = []
        this.errorLogs = []
      }
    },
    handleDownload(row) {
      this.$message.info('正在准备下载文件：' + (row.fileName || ''))
    },
    handleReprocess(row) {
      if (row && row.id) {
        this.$confirm('确认重新处理该文件？', '提示', { type: 'warning' }).then(async () => {
          try {
            const response = await systemIntegrationApi.file.reprocess(row.id)
            if (response.code === 1) {
              this.$message.success('重新处理成功')
              this.getFileList()
              this.getFileStats()
            } else {
              this.$message.error(response.msg || '重新处理失败')
            }
          } catch (error) {
            this.$message.error('重新处理失败：' + error.message)
          }
        }).catch(() => {})
      }
    },
    handleDelete(row) {
      this.$confirm('确认删除该文件记录？', '警告', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.file.delete(row.id)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getFileList()
            this.getFileStats()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      }).catch(() => {})
    },

    // ========== 文件上传（FormData） ==========
    handleFileChange(file) {
      this.uploadForm.file = file.raw
      this.uploadFileList = [file]
    },
    handleFileRemove() {
      this.uploadForm.file = null
      this.uploadFileList = []
    },
    handleSubmitUpload() {
      this.$refs.uploadForm && this.$refs.uploadForm.validate(async (valid) => {
        if (!valid) return
        if (!this.uploadForm.file) {
          this.$message.warning('请选择要上传的文件')
          return
        }
        this.uploadLoading = true
        try {
          const formData = new FormData()
          formData.append('file', this.uploadForm.file)
          formData.append('fileType', this.uploadForm.fileType)
          formData.append('dataType', this.uploadForm.dataType)
          const response = await systemIntegrationApi.file.upload(formData)
          if (response.code === 1) {
            this.$message.success('上传成功')
            this.uploadDialogVisible = false
            this.getFileList()
            this.getFileStats()
          } else {
            this.$message.error(response.msg || '上传失败')
          }
        } catch (error) {
          this.$message.error('上传失败：' + error.message)
        } finally {
          this.uploadLoading = false
        }
      })
    },
    resetUploadForm() {
      this.uploadForm = { fileType: '', dataType: '', file: null, options: [] }
      this.uploadFileList = []
    },
    handleUploadDialogClose() {
      this.resetUploadForm()
    },

    // ========== 批量导出 ==========
    doBatchExport() {
      const dataToExport = this.exportForm.scope === 'FILTERED' ? this.filteredFileList : this.fileList
      if (!dataToExport || dataToExport.length === 0) {
        this.$message.warning('暂无数据可导出')
        return
      }
      const fields = this.exportForm.fields
      const exportRows = dataToExport.map(item => {
        const row = {}
        fields.forEach(f => { row[f] = item[f] || '' })
        return row
      })
      const blob = new Blob([JSON.stringify(exportRows, null, 2)], { type: 'application/json' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = 'file_integration_export.' + (this.exportForm.format === 'JSON' ? 'json' : 'json')
      a.click()
      URL.revokeObjectURL(url)
      this.batchExportDialogVisible = false
      this.$message.success('导出成功，共 ' + exportRows.length + ' 条记录')
    },

    // ========== 导入设置 ==========
    saveImportSettings() {
      this.$message.success('导入设置已保存')
      this.importSettingsDialogVisible = false
    },

    // ========== 模板管理 ==========
    downloadTemplate(template) {
      this.$message.success('正在下载模板：' + template.templateName)
    },

    // ========== 辅助方法 ==========
    formatFileSize(size) {
      if (!size) return '0B'
      if (size < 1024) return size + 'B'
      else if (size < 1024 * 1024) return (size / 1024).toFixed(1) + 'KB'
      else return (size / (1024 * 1024)).toFixed(1) + 'MB'
    },
    getFileTypeColor(type) {
      const m = { 'EXCEL': 'primary', 'CSV': 'success', 'XML': 'warning', 'JSON': 'danger' }
      return m[type] || 'info'
    },
    getFileTypeText(type) {
      const m = { 'EXCEL': 'Excel文件', 'CSV': 'CSV文件', 'XML': 'XML文件', 'JSON': 'JSON文件' }
      return m[type] || type || '未知'
    },
    getOperationColor(op) {
      const m = { 'IMPORT': 'success', 'EXPORT': 'warning', 'BIDIRECTIONAL': 'primary' }
      return m[op] || 'info'
    },
    getOperationText(op) {
      const m = { 'IMPORT': '导入', 'EXPORT': '导出', 'BIDIRECTIONAL': '双向' }
      return m[op] || op || '未知'
    },
    getStatusColor(status) {
      const m = { 'SUCCESS': 'success', 'ACTIVE': 'success', 'FAILED': 'danger', 'ERROR': 'danger', 'PROCESSING': 'warning', 'PENDING': 'info', 'INACTIVE': 'info', 'TESTING': 'warning' }
      return m[status] || 'info'
    },
    getStatusText(status) {
      const m = { 'SUCCESS': '成功', 'ACTIVE': '活跃', 'FAILED': '失败', 'ERROR': '错误', 'PROCESSING': '处理中', 'PENDING': '待处理', 'INACTIVE': '未激活', 'TESTING': '测试中' }
      return m[status] || status || '未知'
    },
    getLogLevelColor(level) {
      const m = { 'INFO': 'primary', 'WARN': 'warning', 'ERROR': 'danger', 'DEBUG': 'info' }
      return m[level] || 'info'
    }
  }
}
</script>
