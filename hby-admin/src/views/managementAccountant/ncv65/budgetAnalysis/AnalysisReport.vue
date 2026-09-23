<template>
  <div class="analysis-report">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>分析报告</h2>
      <p>生成和管理各类预算分析报告，支持自定义报告模板和自动化报告生成</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateReport">创建报告</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefreshReports">刷新报告</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleBatchExport">批量导出</el-button>
            <el-button type="info" icon="el-icon-document" @click="handleTemplateManagement">模板管理</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-timer" @click="handleScheduleManagement">定时任务</el-button>
            <el-button icon="el-icon-setting" @click="handleReportSettings">报告设置</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 报告分析概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card reports-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ reportStats.totalReports }}</div>
            <div class="stat-label">总报告数</div>
            <div class="stat-description">已生成的分析报告</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>较上期增长15%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-document"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card scheduled-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ reportStats.scheduledReports }}</div>
            <div class="stat-label">定时报告</div>
            <div class="stat-description">自动生成的报告数</div>
            <div class="stat-trend">
              <i class="el-icon-timer"></i>
              <span>自动化生成</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-timer"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card templates-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ reportStats.totalTemplates }}</div>
            <div class="stat-label">报告模板</div>
            <div class="stat-description">可用的报告模板</div>
            <div class="stat-trend">
              <i class="el-icon-document-copy"></i>
              <span>模板丰富</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-document-copy"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card downloads-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ reportStats.totalDownloads }}</div>
            <div class="stat-label">下载次数</div>
            <div class="stat-description">报告下载总次数</div>
            <div class="stat-trend">
              <i class="el-icon-download"></i>
              <span>使用频繁</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-download"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 报告筛选条件 -->
    <el-card class="filter-card" shadow="never">
      <div class="filter-header">
        <span class="filter-title">报告筛选</span>
        <el-button type="text" @click="handleResetFilter">重置筛选</el-button>
      </div>
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="报告类型">
          <el-select
            v-model="queryForm.reportType"
            placeholder="请选择报告类型"
            clearable
            style="width: 150px"
          >
            <el-option value="VARIANCE" label="差异分析报告" />
            <el-option value="TREND" label="趋势分析报告" />
            <el-option value="COMPARISON" label="对比分析报告" />
            <el-option value="PERFORMANCE" label="绩效分析报告" />
            <el-option value="FORECAST" label="预测分析报告" />
            <el-option value="SCENARIO" label="场景分析报告" />
            <el-option value="SENSITIVITY" label="敏感性分析报告" />
            <el-option value="COMPREHENSIVE" label="综合分析报告" />
          </el-select>
        </el-form-item>
        <el-form-item label="报告状态">
          <el-select
            v-model="queryForm.reportStatus"
            placeholder="请选择报告状态"
            clearable
            style="width: 120px"
          >
            <el-option value="GENERATING" label="生成中" />
            <el-option value="COMPLETED" label="已完成" />
            <el-option value="FAILED" label="生成失败" />
            <el-option value="SCHEDULED" label="已调度" />
          </el-select>
        </el-form-item>
        <el-form-item label="生成时间">
          <el-date-picker
            v-model="queryForm.generateTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="创建人">
          <el-input
            v-model="queryForm.creator"
            placeholder="请输入创建人"
            clearable
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item label="报告名称">
          <el-input
            v-model="queryForm.reportName"
            placeholder="请输入报告名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleResetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 快速报告生成 -->
    <el-card class="quick-report-card" shadow="never">
      <div slot="header" class="card-header">
        <span>快速报告生成</span>
        <div class="header-tools">
          <el-button icon="el-icon-refresh" size="mini" @click="refreshQuickReports">刷新</el-button>
        </div>
      </div>
      <el-row :gutter="16">
        <el-col :span="4" v-for="template in quickReportTemplates" :key="template.id">
          <el-card class="quick-report-item" shadow="hover" @click.native="handleQuickGenerate(template)">
            <div class="quick-report-icon">
              <i :class="template.icon"></i>
            </div>
            <div class="quick-report-title">{{ template.name }}</div>
            <div class="quick-report-description">{{ template.description }}</div>
            <div class="quick-report-stats">
              <span class="usage-count">使用 {{ template.usageCount }} 次</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 报告列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">分析报告列表</span>
        <div class="table-tools">
          <el-tooltip content="自动刷新" placement="top">
            <el-switch
              v-model="autoRefresh"
              active-text="自动刷新"
              @change="handleAutoRefreshChange"
            />
          </el-tooltip>
          <el-tooltip content="显示预览" placement="top">
            <el-switch
              v-model="showPreview"
              active-text="显示预览"
              @change="handleShowPreviewChange"
            />
          </el-tooltip>
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getReportList" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="reportList"
        border
        stripe
        highlight-current-row
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column prop="reportName" label="报告名称" width="200" show-overflow-tooltip />
        <el-table-column prop="reportType" label="报告类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getReportTypeColor(scope.row.reportType)" size="mini">
              {{ getReportTypeText(scope.row.reportType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="templateName" label="使用模板" width="150" show-overflow-tooltip />
        
        <el-table-column prop="reportStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.reportStatus)" size="mini">
              <i :class="getStatusIcon(scope.row.reportStatus)"></i>
              {{ getStatusText(scope.row.reportStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="generateProgress" label="生成进度" width="120" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.generateProgress"
              :status="getProgressStatus(scope.row.reportStatus)"
              :stroke-width="6"
            />
          </template>
        </el-table-column>
        
        <el-table-column prop="fileSize" label="文件大小" width="100" align="right">
          <template slot-scope="scope">
            <span class="file-size">{{ formatFileSize(scope.row.fileSize) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="downloadCount" label="下载次数" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <span class="download-count">{{ scope.row.downloadCount }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="creator" label="创建人" width="100" align="center" />
        <el-table-column prop="generateTime" label="生成时间" width="150" align="center" sortable="custom" />
        
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click.stop="handlePreviewReport(scope.row)"
              :disabled="scope.row.reportStatus !== 'COMPLETED'"
            >预览</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEditReport(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-download"
              @click.stop="handleDownloadReport(scope.row)"
              :disabled="scope.row.reportStatus !== 'COMPLETED'"
            >下载</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini" @click.stop>
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="regenerate" icon="el-icon-refresh">重新生成</el-dropdown-item>
                <el-dropdown-item command="share" icon="el-icon-share">分享</el-dropdown-item>
                <el-dropdown-item command="schedule" icon="el-icon-timer">定时生成</el-dropdown-item>
                <el-dropdown-item command="copy" icon="el-icon-copy-document">复制</el-dropdown-item>
                <el-dropdown-item command="delete" icon="el-icon-delete">删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          :total="total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 报告预览抽屉 -->
    <el-drawer
      title="报告预览"
      :visible.sync="previewDrawerVisible"
      direction="rtl"
      size="80%"
    >
      <div class="preview-content" v-if="currentReportPreview">
        <el-tabs v-model="previewActiveTab" type="card">
          <el-tab-pane label="报告内容" name="content">
            <div class="report-content">
              <iframe
                v-if="currentReportPreview.previewUrl"
                :src="currentReportPreview.previewUrl"
                style="width: 100%; height: 600px; border: none;"
              ></iframe>
              <div v-else class="no-preview">
                <i class="el-icon-document"></i>
                <p>暂无预览内容</p>
              </div>
            </div>
          </el-tab-pane>
          
          <el-tab-pane label="报告信息" name="info">
            <el-descriptions title="报告基本信息" :column="2" border>
              <el-descriptions-item label="报告名称">{{ currentReportPreview.reportName }}</el-descriptions-item>
              <el-descriptions-item label="报告类型">{{ getReportTypeText(currentReportPreview.reportType) }}</el-descriptions-item>
              <el-descriptions-item label="使用模板">{{ currentReportPreview.templateName }}</el-descriptions-item>
              <el-descriptions-item label="文件大小">{{ formatFileSize(currentReportPreview.fileSize) }}</el-descriptions-item>
              <el-descriptions-item label="生成时间">{{ currentReportPreview.generateTime }}</el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentReportPreview.creator }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          
          <el-tab-pane label="生成日志" name="log">
            <div class="generation-log">
              <el-timeline>
                <el-timeline-item
                  v-for="log in currentReportPreview.generationLogs || []"
                  :key="log.id"
                  :timestamp="log.timestamp"
                  :type="getLogType(log.level)"
                >
                  {{ log.message }}
                </el-timeline-item>
              </el-timeline>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 创建报告对话框 -->
    <el-dialog
      title="创建分析报告"
      :visible.sync="createReportDialogVisible"
      width="800px"
      @close="handleCloseCreateDialog"
    >
      <el-form :model="createReportForm" :rules="createReportRules" ref="createReportForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报告名称" prop="reportName">
              <el-input v-model="createReportForm.reportName" placeholder="请输入报告名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告类型" prop="reportType">
              <el-select v-model="createReportForm.reportType" placeholder="请选择报告类型" style="width: 100%">
                <el-option value="VARIANCE" label="差异分析报告" />
                <el-option value="TREND" label="趋势分析报告" />
                <el-option value="COMPARISON" label="对比分析报告" />
                <el-option value="PERFORMANCE" label="绩效分析报告" />
                <el-option value="FORECAST" label="预测分析报告" />
                <el-option value="SCENARIO" label="场景分析报告" />
                <el-option value="SENSITIVITY" label="敏感性分析报告" />
                <el-option value="COMPREHENSIVE" label="综合分析报告" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报告模板" prop="templateId">
              <el-select v-model="createReportForm.templateId" placeholder="请选择报告模板" style="width: 100%">
                <el-option
                  v-for="template in reportTemplates"
                  :key="template.id"
                  :value="template.id"
                  :label="template.name"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分析期间" prop="analysisPeriod">
              <el-date-picker
                v-model="createReportForm.analysisPeriod"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="报告描述" prop="description">
          <el-input
            v-model="createReportForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入报告描述"
          />
        </el-form-item>
        <el-form-item label="输出格式" prop="outputFormats">
          <el-checkbox-group v-model="createReportForm.outputFormats">
            <el-checkbox label="PDF">PDF</el-checkbox>
            <el-checkbox label="EXCEL">Excel</el-checkbox>
            <el-checkbox label="WORD">Word</el-checkbox>
            <el-checkbox label="HTML">HTML</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createReportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmCreateReport" :loading="creatingReport">
          {{ creatingReport ? '生成中...' : '确定生成' }}
        </el-button>
      </div>
    </el-dialog>

    <!-- 编辑报告对话框 -->
    <el-dialog title="编辑分析报告" :visible.sync="editReportDialogVisible" width="800px" @close="handleCloseEditDialog">
      <el-form :model="editReportForm" :rules="editReportRules" ref="editReportForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报告名称" prop="reportName">
              <el-input v-model="editReportForm.reportName" placeholder="请输入报告名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告类型" prop="reportType">
              <el-select v-model="editReportForm.reportType" placeholder="请选择报告类型" style="width: 100%">
                <el-option value="VARIANCE" label="差异分析报告" />
                <el-option value="TREND" label="趋势分析报告" />
                <el-option value="COMPARISON" label="对比分析报告" />
                <el-option value="PERFORMANCE" label="绩效分析报告" />
                <el-option value="FORECAST" label="预测分析报告" />
                <el-option value="SCENARIO" label="场景分析报告" />
                <el-option value="SENSITIVITY" label="敏感性分析报告" />
                <el-option value="COMPREHENSIVE" label="综合分析报告" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报告模板">
              <el-select v-model="editReportForm.templateId" placeholder="请选择报告模板" style="width: 100%">
                <el-option v-for="template in reportTemplates" :key="template.id" :value="template.id" :label="template.name" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告状态">
              <el-select v-model="editReportForm.reportStatus" style="width: 100%">
                <el-option value="GENERATING" label="生成中" />
                <el-option value="COMPLETED" label="已完成" />
                <el-option value="FAILED" label="生成失败" />
                <el-option value="SCHEDULED" label="已调度" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="报告描述">
          <el-input v-model="editReportForm.description" type="textarea" :rows="3" placeholder="请输入报告描述" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="editReportForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editReportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmEditReport" :loading="editingReport">保存</el-button>
      </div>
    </el-dialog>

    <!-- 模板管理对话框 -->
    <el-dialog title="模板管理" :visible.sync="templateDialogVisible" width="900px">
      <div style="margin-bottom: 16px;">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleCreateTemplate">新建模板</el-button>
        <el-input v-model="templateSearchName" placeholder="搜索模板名称" size="small" style="width: 200px; margin-left: 10px;" clearable @clear="loadTemplateList" @keyup.enter.native="loadTemplateList" />
        <el-button size="small" icon="el-icon-search" @click="loadTemplateList" style="margin-left: 5px;">搜索</el-button>
      </div>
      <el-table :data="templateList" border stripe v-loading="templateLoading" max-height="400">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="name" label="模板名称" width="180" show-overflow-tooltip />
        <el-table-column prop="reportType" label="报告类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getReportTypeColor(scope.row.reportType)" size="mini">{{ getReportTypeText(scope.row.reportType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="usageCount" label="使用次数" width="90" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="mini">{{ scope.row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-edit" @click="handleEditTemplate(scope.row)">编辑</el-button>
            <el-button type="text" size="mini" icon="el-icon-delete" style="color: #F56C6C;" @click="handleDeleteTemplate(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="templatePageNum" :page-size="templatePageSize" :total="templateTotal" layout="total, prev, pager, next" @current-change="handleTemplatePageChange" style="margin-top: 16px; text-align: right;" />
    </el-dialog>

    <!-- 新建/编辑模板对话框 -->
    <el-dialog :title="templateFormMode === 'create' ? '新建模板' : '编辑模板'" :visible.sync="templateFormDialogVisible" width="600px" append-to-body>
      <el-form :model="templateForm" :rules="templateFormRules" ref="templateForm" label-width="100px">
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="templateForm.name" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="报告类型" prop="reportType">
          <el-select v-model="templateForm.reportType" placeholder="请选择" style="width: 100%">
            <el-option value="VARIANCE" label="差异分析" />
            <el-option value="TREND" label="趋势分析" />
            <el-option value="COMPARISON" label="对比分析" />
            <el-option value="PERFORMANCE" label="绩效分析" />
            <el-option value="FORECAST" label="预测分析" />
            <el-option value="SCENARIO" label="场景分析" />
            <el-option value="SENSITIVITY" label="敏感性分析" />
            <el-option value="COMPREHENSIVE" label="综合分析" />
          </el-select>
        </el-form-item>
        <el-form-item label="模板描述">
          <el-input v-model="templateForm.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="快速模板">
              <el-switch v-model="templateForm.isQuick" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序号">
              <el-input-number v-model="templateForm.sortOrder" :min="0" :max="999" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="templateFormDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmTemplate" :loading="templateSaving">确定</el-button>
      </div>
    </el-dialog>

    <!-- 分享对话框 -->
    <el-dialog title="分享报告" :visible.sync="shareDialogVisible" width="600px">
      <el-form :model="shareForm" :rules="shareFormRules" ref="shareForm" label-width="100px">
        <el-form-item label="报告名称">
          <el-input :value="shareForm.reportName" disabled />
        </el-form-item>
        <el-form-item label="分享方式" prop="shareType">
          <el-radio-group v-model="shareForm.shareType">
            <el-radio label="LINK">链接分享</el-radio>
            <el-radio label="EMAIL">邮件分享</el-radio>
            <el-radio label="INTERNAL">内部分享</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分享对象" prop="shareTarget">
          <el-input v-model="shareForm.shareTarget" :placeholder="shareForm.shareType === 'EMAIL' ? '请输入邮箱地址' : '请输入用户名或部门'" />
        </el-form-item>
        <el-form-item label="权限" prop="sharePermission">
          <el-select v-model="shareForm.sharePermission" style="width: 100%">
            <el-option value="VIEW" label="仅查看" />
            <el-option value="DOWNLOAD" label="可下载" />
            <el-option value="EDIT" label="可编辑" />
          </el-select>
        </el-form-item>
        <el-form-item label="有效期">
          <el-select v-model="shareForm.expireDays" style="width: 100%">
            <el-option :value="7" label="7天" />
            <el-option :value="30" label="30天" />
            <el-option :value="90" label="90天" />
            <el-option :value="365" label="1年" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="shareForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <!-- 分享记录 -->
      <div v-if="shareRecords.length > 0" style="margin-top: 10px;">
        <el-divider content-position="left">历史分享记录</el-divider>
        <el-table :data="shareRecords" border size="mini" max-height="200">
          <el-table-column prop="shareType" label="方式" width="80" align="center">
            <template slot-scope="scope">{{ { LINK: '链接', EMAIL: '邮件', INTERNAL: '内部' }[scope.row.shareType] || scope.row.shareType }}</template>
          </el-table-column>
          <el-table-column prop="shareTarget" label="对象" show-overflow-tooltip />
          <el-table-column prop="sharePermission" label="权限" width="80" align="center">
            <template slot-scope="scope">{{ { VIEW: '查看', DOWNLOAD: '下载', EDIT: '编辑' }[scope.row.sharePermission] || scope.row.sharePermission }}</template>
          </el-table-column>
          <el-table-column prop="createTime" label="分享时间" width="150" />
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="shareDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmShare" :loading="sharing">确认分享</el-button>
      </div>
    </el-dialog>

    <!-- 定时生成对话框 -->
    <el-dialog title="定时生成" :visible.sync="scheduleDialogVisible" width="600px">
      <el-form :model="scheduleForm" :rules="scheduleFormRules" ref="scheduleForm" label-width="100px">
        <el-form-item label="报告名称">
          <el-input :value="scheduleForm.reportName" disabled />
        </el-form-item>
        <el-form-item label="任务名称" prop="scheduleName">
          <el-input v-model="scheduleForm.scheduleName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="执行频率" prop="frequency">
          <el-select v-model="scheduleForm.frequency" style="width: 100%">
            <el-option value="ONCE" label="单次执行" />
            <el-option value="DAILY" label="每天" />
            <el-option value="WEEKLY" label="每周" />
            <el-option value="MONTHLY" label="每月" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行时间" v-if="scheduleForm.frequency === 'ONCE'">
          <el-date-picker v-model="scheduleForm.nextRunTime" type="datetime" placeholder="选择执行时间" style="width: 100%" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="scheduleForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <!-- 已有定时任务 -->
      <div v-if="scheduleRecords.length > 0" style="margin-top: 10px;">
        <el-divider content-position="left">已有定时任务</el-divider>
        <el-table :data="scheduleRecords" border size="mini" max-height="200">
          <el-table-column prop="scheduleName" label="任务名称" show-overflow-tooltip />
          <el-table-column prop="frequency" label="频率" width="80" align="center">
            <template slot-scope="scope">{{ { ONCE: '单次', DAILY: '每天', WEEKLY: '每周', MONTHLY: '每月' }[scope.row.frequency] || scope.row.frequency }}</template>
          </el-table-column>
          <el-table-column prop="nextRunTime" label="下次执行" width="150" />
          <el-table-column prop="status" label="状态" width="80" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="mini">{{ scope.row.status === 1 ? '运行中' : scope.row.status === 2 ? '已暂停' : '已停止' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="handleToggleSchedule(scope.row)">{{ scope.row.status === 1 ? '暂停' : '启动' }}</el-button>
              <el-button type="text" size="mini" style="color: #F56C6C;" @click="handleDeleteSchedule(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="scheduleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmSchedule" :loading="scheduling">创建任务</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetAnalysisApi } from '@/api/managementAccountant/ncv65/budgetAnalysis'

export default {
  name: 'AnalysisReport',
  data() {
    return {
      createDialogVisible: false,
      settingsDialogVisible: false,
      helpDialogVisible: false,
      // 查询表单
      queryForm: {
        reportType: '',
        reportStatus: '',
        generateTime: [],
        creator: '',
        reportName: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      reportList: [],
      total: 0,
      selectedReports: [],
      
      // 控制开关
      autoRefresh: false,
      showPreview: true,
      refreshTimer: null,
      
      // 统计数据
      reportStats: {
        totalReports: 0,
        scheduledReports: 0,
        totalTemplates: 0,
        totalDownloads: 0
      },

      // 快速报告模板
      quickReportTemplates: [],

      // 报告模板
      reportTemplates: [],
      
      // 抽屉
      previewDrawerVisible: false,
      currentReportPreview: null,
      previewActiveTab: 'content',
      
      // 创建报告对话框
      createReportDialogVisible: false,
      creatingReport: false,
      createReportForm: {
        reportName: '',
        reportType: '',
        templateId: '',
        analysisPeriod: [],
        description: '',
        outputFormats: ['PDF']
      },
      createReportRules: {
        reportName: [
          { required: true, message: '请输入报告名称', trigger: 'blur' }
        ],
        reportType: [
          { required: true, message: '请选择报告类型', trigger: 'change' }
        ],
        templateId: [
          { required: true, message: '请选择报告模板', trigger: 'change' }
        ],
        analysisPeriod: [
          { required: true, message: '请选择分析期间', trigger: 'change' }
        ]
      },

      // 编辑报告
      editReportDialogVisible: false,
      editingReport: false,
      editReportForm: {
        id: '',
        reportName: '',
        reportType: '',
        templateId: '',
        reportStatus: '',
        description: '',
        remark: ''
      },
      editReportRules: {
        reportName: [{ required: true, message: '请输入报告名称', trigger: 'blur' }]
      },

      // 模板管理
      templateDialogVisible: false,
      templateList: [],
      templateLoading: false,
      templateSearchName: '',
      templatePageNum: 1,
      templatePageSize: 10,
      templateTotal: 0,
      templateFormDialogVisible: false,
      templateFormMode: 'create',
      templateForm: { name: '', reportType: '', description: '', isQuick: 0, sortOrder: 0 },
      templateFormRules: {
        name: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
        reportType: [{ required: true, message: '请选择报告类型', trigger: 'change' }]
      },
      templateSaving: false,

      // 分享
      shareDialogVisible: false,
      sharing: false,
      shareRecords: [],
      shareForm: {
        reportId: '',
        reportName: '',
        shareType: 'LINK',
        shareTarget: '',
        sharePermission: 'VIEW',
        expireDays: 7,
        remark: ''
      },
      shareFormRules: {
        shareType: [{ required: true, message: '请选择分享方式', trigger: 'change' }],
        shareTarget: [{ required: true, message: '请输入分享对象', trigger: 'blur' }],
        sharePermission: [{ required: true, message: '请选择权限', trigger: 'change' }]
      },

      // 定时生成
      scheduleDialogVisible: false,
      scheduling: false,
      scheduleRecords: [],
      scheduleForm: {
        reportId: '',
        reportName: '',
        reportType: '',
        templateId: '',
        templateName: '',
        scheduleName: '',
        frequency: 'ONCE',
        nextRunTime: '',
        remark: ''
      },
      scheduleFormRules: {
        scheduleName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
        frequency: [{ required: true, message: '请选择执行频率', trigger: 'change' }]
      }
    }
  },
  
  created() {
    this.getReportList()
    this.loadReportStats()
    this.loadReportTemplates()
  },
  
  beforeDestroy() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
  },
  
  methods: {
    // 加载统计数据
    async loadReportStats() {
      try {
        const response = await budgetAnalysisApi.getReportStats()
        if (response.code === 1 && response.data) {
          this.reportStats = { ...this.reportStats, ...response.data }
        }
      } catch (error) {
        console.error('加载报告统计数据失败：', error)
      }
    },

    // 加载报告模板
    async loadReportTemplates() {
      try {
        const response = await budgetAnalysisApi.getReportTemplates()
        if (response.code === 1 && response.data) {
          this.reportTemplates = response.data.templates || []
          this.quickReportTemplates = response.data.quickTemplates || []
        }
      } catch (error) {
        console.error('加载报告模板失败：', error)
      }
    },

    // 获取报告列表
    async getReportList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.queryParams
        }
        const response = await budgetAnalysisApi.getAnalysisReports(params)
        this.reportList = response.data.tlist || response.data.list || []
        this.total = response.data.totalRecord || response.data.total || 0
      } catch (error) {
        this.$message.error('获取报告列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getReportList()
    },
    
    // 重置查询
    handleResetQuery() {
      this.queryForm = {
        reportType: '',
        reportStatus: '',
        generateTime: [],
        creator: '',
        reportName: ''
      }
      this.handleQuery()
    },
    
    // 重置筛选
    handleResetFilter() {
      this.handleResetQuery()
    },
    
    // 创建报告
    handleCreateReport() {
      this.createReportDialogVisible = true
    },
    
    // 确认创建报告
    async handleConfirmCreateReport() {
      this.$refs.createReportForm.validate(async (valid) => {
        if (valid) {
          this.creatingReport = true
          try {
            await budgetAnalysisApi.createAnalysisReport(this.createReportForm)
            this.$message.success('报告创建成功，正在生成中...')
            this.createReportDialogVisible = false
            this.getReportList()
          } catch (error) {
            this.$message.error('报告创建失败：' + error.message)
          } finally {
            this.creatingReport = false
          }
        }
      })
    },
    
    // 关闭创建对话框
    handleCloseCreateDialog() {
      this.$refs.createReportForm.resetFields()
    },
    
    // 刷新报告
    handleRefreshReports() {
      this.getReportList()
    },
    
    // 批量导出
    async handleBatchExport() {
      if (this.selectedReports.length === 0) {
        this.$message.warning('请选择要导出的报告')
        return
      }
      const loading = this.$loading({ lock: true, text: '正在导出...', background: 'rgba(0,0,0,0.7)' })
      try {
        const reportIds = this.selectedReports.map(report => report.id)
        const response = await budgetAnalysisApi.batchExportReports(reportIds)
        if (response.code === 1) {
          this.$message.success(`批量导出成功，共导出 ${reportIds.length} 份报告`)
        } else {
          this.$message.error(response.msg || '批量导出失败')
        }
      } catch (error) {
        this.$message.error('批量导出失败：' + error.message)
      } finally {
        loading.close()
      }
    },

    // 模板管理
    handleTemplateManagement() {
      this.templateDialogVisible = true
      this.loadTemplateList()
    },

    // 定时任务管理
    handleScheduleManagement() {
      this.$message.info('报告功能开发中')
    },
    
    // 报告设置
    handleReportSettings() {
      this.settingsDialogVisible = true
    },
    
    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },
    
    // 刷新快速报告
    refreshQuickReports() {
      this.$message.success('快速报告模板已刷新')
    },
    
    // 快速生成
    async handleQuickGenerate(template) {
      try {
        await budgetAnalysisApi.quickGenerateReport(template.id)
        this.$message.success(`正在生成${template.name}，请稍候...`)
        this.getReportList()
      } catch (error) {
        this.$message.error('快速生成失败：' + error.message)
      }
    },
    
    // 预览报告
    handlePreviewReport(row) {
      this.currentReportPreview = row
      this.previewDrawerVisible = true
    },
    
    // 下载报告
    async handleDownloadReport(row) {
      try {
        await budgetAnalysisApi.downloadReport(row.id)
        this.$message.success('下载成功')
      } catch (error) {
        this.$message.error('下载失败：' + error.message)
      }
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'regenerate':
          this.handleRegenerateReport(row)
          break
        case 'share':
          this.handleShareReport(row)
          break
        case 'schedule':
          this.handleScheduleReport(row)
          break
        case 'copy':
          this.handleCopyReport(row)
          break
        case 'delete':
          this.handleDeleteReport(row)
          break
      }
    },
    
    // 重新生成
    async handleRegenerateReport(row) {
      try {
        await budgetAnalysisApi.regenerateReport(row.id)
        this.$message.success('重新生成成功')
        this.getReportList()
      } catch (error) {
        this.$message.error('重新生成失败：' + error.message)
      }
    },
    
    // 分享报告
    handleShareReport(row) {
      this.shareForm = {
        reportId: row.id,
        reportName: row.reportName,
        shareType: 'LINK',
        shareTarget: '',
        sharePermission: 'VIEW',
        expireDays: 7,
        remark: ''
      }
      this.shareRecords = []
      this.shareDialogVisible = true
      this.loadShareRecords(row.id)
    },

    // 定时生成
    handleScheduleReport(row) {
      this.scheduleForm = {
        reportId: row.id,
        reportName: row.reportName,
        reportType: row.reportType,
        templateId: row.templateId || '',
        templateName: row.templateName || '',
        scheduleName: '',
        frequency: 'ONCE',
        nextRunTime: '',
        remark: ''
      }
      this.scheduleRecords = []
      this.scheduleDialogVisible = true
      this.loadScheduleRecords(row.id)
    },
    
    // 复制报告
    async handleCopyReport(row) {
      try {
        await budgetAnalysisApi.copyReport(row.id)
        this.$message.success('报告复制成功')
        this.getReportList()
      } catch (error) {
        this.$message.error('报告复制失败：' + error.message)
      }
    },
    
    // 删除报告
    handleDeleteReport(row) {
      this.$confirm('确定删除该报告吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await budgetAnalysisApi.deleteReport(row.id)
          this.$message.success('删除成功')
          this.getReportList()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },
    
    // 自动刷新切换
    handleAutoRefreshChange(value) {
      if (value) {
        this.refreshTimer = setInterval(() => {
          this.getReportList()
        }, 30000) // 30秒刷新一次
        this.$message.info('已开启自动刷新')
      } else {
        if (this.refreshTimer) {
          clearInterval(this.refreshTimer)
          this.refreshTimer = null
        }
        this.$message.info('已关闭自动刷新')
      }
    },
    
    // 显示预览切换
    handleShowPreviewChange(value) {
      this.$message.info(value ? '已显示预览' : '已隐藏预览')
    },
    
    // 行点击
    handleRowClick(row) {
      if (row.reportStatus === 'COMPLETED') {
        this.handlePreviewReport(row)
      }
    },
    
    // 选择改变
    handleSelectionChange(selection) {
      this.selectedReports = selection
    },
    
    // 排序改变
    handleSortChange({ column, prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getReportList()
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getReportList()
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getReportList()
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
      return `${size.toFixed(1)} ${units[index]}`
    },
    
    // 获取报告类型颜色
    getReportTypeColor(type) {
      const colorMap = {
        'VARIANCE': 'primary',
        'TREND': 'success',
        'COMPARISON': 'warning',
        'PERFORMANCE': 'danger',
        'FORECAST': 'info',
        'SCENARIO': 'primary',
        'SENSITIVITY': 'warning',
        'COMPREHENSIVE': 'success'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取报告类型文本
    getReportTypeText(type) {
      const textMap = {
        'VARIANCE': '差异分析',
        'TREND': '趋势分析',
        'COMPARISON': '对比分析',
        'PERFORMANCE': '绩效分析',
        'FORECAST': '预测分析',
        'SCENARIO': '场景分析',
        'SENSITIVITY': '敏感性分析',
        'COMPREHENSIVE': '综合分析'
      }
      return textMap[type] || type
    },
    
    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'GENERATING': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'SCHEDULED': 'info'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取状态图标
    getStatusIcon(status) {
      const iconMap = {
        'GENERATING': 'el-icon-loading',
        'COMPLETED': 'el-icon-success',
        'FAILED': 'el-icon-error',
        'SCHEDULED': 'el-icon-timer'
      }
      return iconMap[status] || 'el-icon-minus'
    },
    
    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'GENERATING': '生成中',
        'COMPLETED': '已完成',
        'FAILED': '生成失败',
        'SCHEDULED': '已调度'
      }
      return textMap[status] || status
    },
    
    // 获取进度状态
    getProgressStatus(status) {
      if (status === 'COMPLETED') return 'success'
      if (status === 'FAILED') return 'exception'
      return null
    },
    
    // 获取日志类型
    getLogType(level) {
      const typeMap = {
        'INFO': 'primary',
        'WARN': 'warning',
        'ERROR': 'danger',
        'SUCCESS': 'success'
      }
      return typeMap[level] || 'primary'
    },

    // ==================== 编辑报告 ====================
    handleEditReport(row) {
      this.editReportForm = {
        id: row.id,
        reportName: row.reportName,
        reportType: row.reportType,
        templateId: row.templateId || '',
        reportStatus: row.reportStatus,
        description: row.description || '',
        remark: row.remark || ''
      }
      this.editReportDialogVisible = true
    },
    async handleConfirmEditReport() {
      this.$refs.editReportForm.validate(async (valid) => {
        if (!valid) return
        this.editingReport = true
        try {
          const { id, ...data } = this.editReportForm
          const response = await budgetAnalysisApi.updateReport(id, data)
          if (response.code === 1) {
            this.$message.success('报告更新成功')
            this.editReportDialogVisible = false
            this.getReportList()
          } else {
            this.$message.error(response.msg || '更新失败')
          }
        } catch (error) {
          this.$message.error('更新失败：' + error.message)
        } finally {
          this.editingReport = false
        }
      })
    },
    handleCloseEditDialog() {
      this.$refs.editReportForm && this.$refs.editReportForm.resetFields()
    },

    // ==================== 模板管理 ====================
    async loadTemplateList() {
      this.templateLoading = true
      try {
        const response = await budgetAnalysisApi.getTemplatePage({
          pageNum: this.templatePageNum,
          pageSize: this.templatePageSize,
          name: this.templateSearchName
        })
        if (response.code === 1 && response.data) {
          this.templateList = response.data.tlist || []
          this.templateTotal = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('加载模板列表失败：' + error.message)
      } finally {
        this.templateLoading = false
      }
    },
    handleTemplatePageChange(page) {
      this.templatePageNum = page
      this.loadTemplateList()
    },
    handleCreateTemplate() {
      this.templateFormMode = 'create'
      this.templateForm = { name: '', reportType: '', description: '', isQuick: 0, sortOrder: 0 }
      this.templateFormDialogVisible = true
    },
    handleEditTemplate(row) {
      this.templateFormMode = 'edit'
      this.templateForm = { ...row }
      this.templateFormDialogVisible = true
    },
    async handleDeleteTemplate(row) {
      try {
        await this.$confirm('确定删除该模板吗？', '提示', { type: 'warning' })
        const response = await budgetAnalysisApi.deleteTemplate(row.id)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadTemplateList()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('删除失败：' + e.message)
      }
    },
    async handleConfirmTemplate() {
      this.$refs.templateForm.validate(async (valid) => {
        if (!valid) return
        this.templateSaving = true
        try {
          let response
          if (this.templateFormMode === 'create') {
            response = await budgetAnalysisApi.createTemplate(this.templateForm)
          } else {
            response = await budgetAnalysisApi.updateTemplate(this.templateForm.id, this.templateForm)
          }
          if (response.code === 1) {
            this.$message.success(this.templateFormMode === 'create' ? '创建成功' : '更新成功')
            this.templateFormDialogVisible = false
            this.loadTemplateList()
            this.loadReportTemplates()
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败：' + error.message)
        } finally {
          this.templateSaving = false
        }
      })
    },

    // ==================== 分享 ====================
    async loadShareRecords(reportId) {
      try {
        const response = await budgetAnalysisApi.getReportShares(reportId)
        if (response.code === 1) {
          this.shareRecords = response.data || []
        }
      } catch (error) {
        console.error('加载分享记录失败：', error)
      }
    },
    async handleConfirmShare() {
      this.$refs.shareForm.validate(async (valid) => {
        if (!valid) return
        this.sharing = true
        try {
          const response = await budgetAnalysisApi.shareReport(this.shareForm.reportId, {
            shareType: this.shareForm.shareType,
            shareTarget: this.shareForm.shareTarget,
            sharePermission: this.shareForm.sharePermission,
            expireDays: this.shareForm.expireDays,
            remark: this.shareForm.remark
          })
          if (response.code === 1) {
            this.$message.success('分享成功')
            this.loadShareRecords(this.shareForm.reportId)
            if (response.data && response.data.shareUrl) {
              this.$alert('分享链接：' + response.data.shareUrl, '分享成功', { confirmButtonText: '复制链接' })
            }
          } else {
            this.$message.error(response.msg || '分享失败')
          }
        } catch (error) {
          this.$message.error('分享失败：' + error.message)
        } finally {
          this.sharing = false
        }
      })
    },

    // ==================== 定时生成 ====================
    async loadScheduleRecords(reportId) {
      try {
        const response = await budgetAnalysisApi.getReportSchedules(reportId)
        if (response.code === 1) {
          this.scheduleRecords = response.data || []
        }
      } catch (error) {
        console.error('加载定时任务失败：', error)
      }
    },
    async handleConfirmSchedule() {
      this.$refs.scheduleForm.validate(async (valid) => {
        if (!valid) return
        this.scheduling = true
        try {
          const response = await budgetAnalysisApi.createSchedule({
            reportId: this.scheduleForm.reportId,
            reportName: this.scheduleForm.reportName,
            reportType: this.scheduleForm.reportType,
            templateId: this.scheduleForm.templateId,
            templateName: this.scheduleForm.templateName,
            scheduleName: this.scheduleForm.scheduleName,
            frequency: this.scheduleForm.frequency,
            nextRunTime: this.scheduleForm.nextRunTime,
            remark: this.scheduleForm.remark
          })
          if (response.code === 1) {
            this.$message.success('定时任务创建成功')
            this.loadScheduleRecords(this.scheduleForm.reportId)
            this.scheduleForm.scheduleName = ''
            this.scheduleForm.remark = ''
          } else {
            this.$message.error(response.msg || '创建失败')
          }
        } catch (error) {
          this.$message.error('创建失败：' + error.message)
        } finally {
          this.scheduling = false
        }
      })
    },
    async handleToggleSchedule(row) {
      const newStatus = row.status === 1 ? 2 : 1
      try {
        const response = await budgetAnalysisApi.updateScheduleStatus(row.id, newStatus)
        if (response.code === 1) {
          this.$message.success(newStatus === 1 ? '已启动' : '已暂停')
          this.loadScheduleRecords(this.scheduleForm.reportId)
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },
    async handleDeleteSchedule(row) {
      try {
        await this.$confirm('确定删除该定时任务吗？', '提示', { type: 'warning' })
        const response = await budgetAnalysisApi.deleteSchedule(row.id)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadScheduleRecords(this.scheduleForm.reportId)
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('删除失败：' + e.message)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.analysis-report {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;

    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .toolbar-card,
  .filter-card,
  .quick-report-card,
  .table-card {
    margin-bottom: 20px;
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;

      &.reports-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }

      &.scheduled-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.templates-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.downloads-card {
        background: linear-gradient(135deg, #909399, #B3B6BC);
        color: white;
      }

      .stat-content {
        position: relative;
        z-index: 2;

        .stat-number {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 2px;
        }

        .stat-description {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }

        .stat-trend {
          font-size: 12px;
          opacity: 0.9;

          .trend-up {
            color: #F56C6C;
          }
        }
      }

      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
      }
    }
  }

  .filter-card {
    .filter-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .filter-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
      }
    }
  }

  .quick-report-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        display: flex;
        align-items: center;
        gap: 10px;
      }
    }

    .quick-report-item {
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
      }

      .quick-report-icon {
        font-size: 32px;
        color: #409EFF;
        margin-bottom: 12px;
      }

      .quick-report-title {
        font-size: 14px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .quick-report-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
        line-height: 1.4;
      }

      .quick-report-stats {
        .usage-count {
          font-size: 11px;
          color: #909399;
          background: #F5F7FA;
          padding: 2px 8px;
          border-radius: 10px;
        }
      }
    }
  }

  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    .table-title {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }

    .table-tools {
      display: flex;
      align-items: center;
      gap: 12px;
    }
  }

  .file-size {
    font-family: 'Courier New', monospace;
    color: #606266;
  }

  .download-count {
    color: #409EFF;
    font-weight: 500;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }

  .text-right {
    text-align: right;
  }

  .preview-content {
    padding: 20px;

    .report-content {
      .no-preview {
        text-align: center;
        padding: 60px 0;
        color: #909399;

        i {
          font-size: 48px;
          margin-bottom: 16px;
        }

        p {
          font-size: 14px;
          margin: 0;
        }
      }
    }

    .generation-log {
      max-height: 400px;
      overflow-y: auto;
    }
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>
