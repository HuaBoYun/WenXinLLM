<template>
  <div class="advanced-reports">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>高级报表管理</h2>
      <p>智能报表生成和分析，支持自定义报表设计、数据可视化和自动分发</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateReport">创建报表</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-document" @click="handleGenerateReport">生成报表</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleReportCenter">报表中心</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">报表设置</el-button>
            <el-button icon="el-icon-magic-stick" @click="handleTemplates">报表模板</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 报表统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ reportStats.totalReports }}</div>
            <div class="stat-label">报表总数</div>
            <div class="stat-description">已创建报表数量</div>
            <div class="stat-trend">
              <i class="el-icon-document"></i>
              <span>智能报表</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-document"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card generated-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ reportStats.generatedToday }}</div>
            <div class="stat-label">今日生成</div>
            <div class="stat-description">今日生成报表数量</div>
            <div class="stat-trend">
              <i class="el-icon-printer"></i>
              <span>自动生成</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-printer"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card templates-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ reportStats.totalTemplates }}</div>
            <div class="stat-label">报表模板</div>
            <div class="stat-description">可用模板数量</div>
            <div class="stat-trend">
              <i class="el-icon-magic-stick"></i>
              <span>模板库</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-magic-stick"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card subscribers-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ reportStats.totalSubscribers }}</div>
            <div class="stat-label">订阅用户</div>
            <div class="stat-description">报表订阅用户数</div>
            <div class="stat-trend">
              <i class="el-icon-user"></i>
              <span>自动分发</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-user"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 报表类型选择 -->
    <el-card class="report-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>报表类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshReportTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="reportType in reportTypes" :key="reportType.id">
          <el-card
            class="report-type-item"
            shadow="hover"
            @click.native="handleSelectReportType(reportType)"
            :class="{ 'selected': selectedReportType === reportType.typeKey }"
          >
            <div class="report-type-icon">
              <i :class="reportType.icon"></i>
            </div>
            <div class="report-type-title">{{ reportType.name }}</div>
            <div class="report-type-description">{{ reportType.description }}</div>
            <div class="report-type-stats">
              <span class="report-count">{{ reportType.reportCount }} 个报表</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 高级报表列表 -->
    <el-card class="advanced-reports-card" shadow="never">
      <div slot="header" class="card-header">
        <span>高级报表</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索报表"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getAdvancedReportList"
            clearable
            @clear="getAdvancedReportList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getAdvancedReportList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="advancedReportList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="reportName" label="报表名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.reportName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="reportType" label="报表类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getReportTypeColor(scope.row.reportType)" size="mini">
              {{ getReportTypeText(scope.row.reportType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataSource" label="数据源" width="120" align="center">
          <template slot-scope="scope">
            <el-tag type="info" size="mini">{{ getDataSourceText(scope.row.dataSource) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="generationFrequency" label="生成频率" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getFrequencyColor(scope.row.generationFrequency)" size="mini">
              {{ getFrequencyText(scope.row.generationFrequency) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastGeneratedTime" label="最后生成" width="150" align="center">
          <template slot-scope="scope">
            <span class="last-generated">{{ scope.row.lastGeneratedTime || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="subscriberCount" label="订阅数" width="100" align="center">
          <template slot-scope="scope">
            <span class="subscriber-count">{{ scope.row.subscriberCount || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="reportStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.reportStatus)" size="mini">
              {{ getStatusText(scope.row.reportStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建人" width="100" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-document"
              @click="handleGenerate(scope.row)"
            >生成</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="preview">预览</el-dropdown-item>
                <el-dropdown-item command="schedule">定时设置</el-dropdown-item>
                <el-dropdown-item command="subscribe">订阅管理</el-dropdown-item>
                <el-dropdown-item command="export">导出配置</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 报表详情抽屉 -->
    <el-drawer
      title="高级报表详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentReport">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="报表基本信息" :column="2" border>
              <el-descriptions-item label="报表名称">{{ currentReport.reportName }}</el-descriptions-item>
              <el-descriptions-item label="报表类型">{{ getReportTypeText(currentReport.reportType) }}</el-descriptions-item>
              <el-descriptions-item label="数据源">{{ getDataSourceText(currentReport.dataSource) }}</el-descriptions-item>
              <el-descriptions-item label="生成频率">{{ getFrequencyText(currentReport.generationFrequency) }}</el-descriptions-item>
              <el-descriptions-item label="最后生成">{{ currentReport.lastGeneratedTime || '-' }}</el-descriptions-item>
              <el-descriptions-item label="订阅数">{{ currentReport.subscriberCount || 0 }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentReport.reportStatus)" size="mini">
                  {{ getStatusText(currentReport.reportStatus) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentReport.createBy }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentReport.createTime }}</el-descriptions-item>
              <el-descriptions-item label="报表描述" :span="2">{{ currentReport.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="报表配置" name="config">
            <div v-if="reportConfig">
              <el-form label-width="120px" size="small">
                <el-form-item label="查询条件">
                  <el-input :value="reportConfig.queryConditions" readonly type="textarea" :rows="3" />
                </el-form-item>
                <el-form-item label="字段配置">
                  <el-input :value="reportConfig.fieldConfiguration" readonly type="textarea" :rows="3" />
                </el-form-item>
                <el-form-item label="排序规则">
                  <el-input :value="reportConfig.sortRules" readonly />
                </el-form-item>
                <el-form-item label="分组设置">
                  <el-input :value="reportConfig.groupSettings" readonly />
                </el-form-item>
                <el-form-item label="格式设置">
                  <el-input :value="reportConfig.formatSettings" readonly type="textarea" :rows="2" />
                </el-form-item>
              </el-form>
            </div>
            <el-empty v-else description="暂无配置信息" />
          </el-tab-pane>
          <el-tab-pane label="订阅用户" name="subscribers">
            <el-table :data="reportSubscribers" border size="mini">
              <el-table-column prop="userName" label="用户名" width="120" />
              <el-table-column prop="department" label="部门" width="150" />
              <el-table-column prop="email" label="邮箱" width="200" />
              <el-table-column prop="subscribeTime" label="订阅时间" width="150" />
              <el-table-column prop="deliveryMethod" label="推送方式" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getDeliveryMethodColor(scope.row.deliveryMethod)" size="mini">
                    {{ getDeliveryMethodText(scope.row.deliveryMethod) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getSubscriberStatusColor(scope.row.status)" size="mini">
                    {{ getSubscriberStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="生成历史" name="history">
            <el-table :data="generationHistory" border size="mini">
              <el-table-column prop="generateTime" label="生成时间" width="150" />
              <el-table-column prop="generateType" label="生成类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getGenerateTypeColor(scope.row.generateType)" size="mini">
                    {{ getGenerateTypeText(scope.row.generateType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="dataRows" label="数据行数" width="100" align="center" />
              <el-table-column prop="fileSize" label="文件大小" width="100" align="center" />
              <el-table-column prop="generateDuration" label="生成耗时" width="100" align="center" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getGenerateStatusColor(scope.row.status)" size="mini">
                    {{ getGenerateStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120" align="center">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" @click="handleDownloadReport(scope.row)">
                    下载
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑报表对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="reportForm"
        :model="reportForm"
        :rules="reportRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报表名称" prop="reportName">
              <el-input v-model="reportForm.reportName" placeholder="请输入报表名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报表类型" prop="reportType">
              <el-select v-model="reportForm.reportType" placeholder="请选择报表类型" style="width: 100%">
                <el-option value="FINANCIAL_REPORT" label="财务报表" />
                <el-option value="BUDGET_ANALYSIS" label="预算分析报表" />
                <el-option value="PERFORMANCE_REPORT" label="绩效报表" />
                <el-option value="CUSTOM_REPORT" label="自定义报表" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据源" prop="dataSource">
              <el-select v-model="reportForm.dataSource" placeholder="请选择数据源" style="width: 100%">
                <el-option value="BUDGET_DATABASE" label="预算数据库" />
                <el-option value="FINANCIAL_DATABASE" label="财务数据库" />
                <el-option value="EXTERNAL_API" label="外部API" />
                <el-option value="FILE_IMPORT" label="文件导入" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生成频率" prop="generationFrequency">
              <el-select v-model="reportForm.generationFrequency" placeholder="请选择生成频率" style="width: 100%">
                <el-option value="MANUAL" label="手动生成" />
                <el-option value="DAILY" label="每日" />
                <el-option value="WEEKLY" label="每周" />
                <el-option value="MONTHLY" label="每月" />
                <el-option value="QUARTERLY" label="每季度" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="报表描述" prop="description">
          <el-input
            v-model="reportForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入报表描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 生成报表弹窗 -->
    <el-dialog title="生成报表" :visible.sync="generateDialogVisible" width="500px">
      <p>请选择需要生成的报表，系统将根据配置自动生成最新数据报表。</p>
      <el-select v-model="generateReportId" placeholder="请选择报表" style="width: 100%" filterable>
        <el-option v-for="item in advancedReportList" :key="item.reportId" :label="item.reportName" :value="item.reportId" />
      </el-select>
      <div slot="footer">
        <el-button @click="generateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmGenerate">确定生成</el-button>
      </div>
    </el-dialog>

    <!-- 报表中心弹窗 -->
    <el-dialog title="报表中心" :visible.sync="reportCenterDialogVisible" width="600px">
      <el-table :data="advancedReportList" border size="mini" max-height="400">
        <el-table-column prop="reportName" label="报表名称" />
        <el-table-column prop="reportType" label="类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getReportTypeColor(scope.row.reportType)" size="mini">{{ getReportTypeText(scope.row.reportType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reportStatus" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.reportStatus)" size="mini">{{ getStatusText(scope.row.reportStatus) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="reportCenterDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 报表设置弹窗 -->
    <el-dialog title="报表设置" :visible.sync="settingsDialogVisible" width="500px">
      <el-form label-width="100px" size="small">
        <el-form-item label="默认频率">
          <el-select v-model="settingsForm.defaultFrequency" style="width: 100%">
            <el-option value="MANUAL" label="手动生成" />
            <el-option value="DAILY" label="每日" />
            <el-option value="WEEKLY" label="每周" />
            <el-option value="MONTHLY" label="每月" />
          </el-select>
        </el-form-item>
        <el-form-item label="默认格式">
          <el-select v-model="settingsForm.defaultFormat" style="width: 100%">
            <el-option value="PDF" label="PDF" />
            <el-option value="EXCEL" label="Excel" />
            <el-option value="CSV" label="CSV" />
          </el-select>
        </el-form-item>
        <el-form-item label="自动分发">
          <el-switch v-model="settingsForm.autoDistribute" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存</el-button>
      </div>
    </el-dialog>

    <!-- 报表模板弹窗 -->
    <el-dialog title="报表模板" :visible.sync="templatesDialogVisible" width="600px">
      <el-row :gutter="16">
        <el-col :span="8" v-for="tpl in templateList" :key="tpl.name">
          <el-card shadow="hover" style="margin-bottom: 16px; text-align: center;">
            <i :class="tpl.icon" style="font-size: 32px; color: #409EFF;"></i>
            <p style="margin: 8px 0 4px;">{{ tpl.name }}</p>
            <p style="font-size: 12px; color: #999;">{{ tpl.desc }}</p>
          </el-card>
        </el-col>
      </el-row>
      <div slot="footer">
        <el-button @click="templatesDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="帮助" :visible.sync="helpDialogVisible" width="500px">
      <div style="line-height: 2;">
        <h4>高级报表使用指南</h4>
        <p>1. 点击「创建报表」按钮新建报表，填写基本信息后保存。</p>
        <p>2. 在报表列表中点击「生成」按钮可手动生成报表数据。</p>
        <p>3. 通过「报表类型」卡片可快速筛选不同类型的报表。</p>
        <p>4. 点击报表名称可查看详情，包括配置、订阅用户和生成历史。</p>
        <p>5. 使用「更多」菜单可进行预览、定时设置、订阅管理等操作。</p>
      </div>
      <div slot="footer">
        <el-button @click="helpDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 预览报表弹窗 -->
    <el-dialog title="报表预览" :visible.sync="previewDialogVisible" width="700px">
      <div v-if="previewRow">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="报表名称">{{ previewRow.reportName }}</el-descriptions-item>
          <el-descriptions-item label="报表类型">{{ getReportTypeText(previewRow.reportType) }}</el-descriptions-item>
          <el-descriptions-item label="数据源">{{ getDataSourceText(previewRow.dataSource) }}</el-descriptions-item>
          <el-descriptions-item label="生成频率">{{ getFrequencyText(previewRow.generationFrequency) }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusColor(previewRow.reportStatus)" size="mini">{{ getStatusText(previewRow.reportStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建人">{{ previewRow.createBy }}</el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">{{ previewRow.description || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 定时设置弹窗 -->
    <el-dialog title="定时设置" :visible.sync="scheduleDialogVisible" width="500px">
      <div v-if="scheduleRow">
        <p style="margin-bottom: 16px; color: #606266;">报表：{{ scheduleRow.reportName }}</p>
        <el-form label-width="100px" size="small">
          <el-form-item label="启用定时">
            <el-switch v-model="scheduleForm.enabled" />
          </el-form-item>
          <el-form-item label="生成频率">
            <el-select v-model="scheduleForm.frequency" style="width: 100%">
              <el-option value="MANUAL" label="手动生成" />
              <el-option value="DAILY" label="每日" />
              <el-option value="WEEKLY" label="每周" />
              <el-option value="MONTHLY" label="每月" />
              <el-option value="QUARTERLY" label="每季度" />
            </el-select>
          </el-form-item>
          <el-form-item label="执行时间">
            <el-time-select v-model="scheduleForm.time" :picker-options="{ start: '00:00', step: '00:30', end: '23:30' }" style="width: 100%" />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer">
        <el-button @click="scheduleDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="scheduleLoading" @click="handleSaveSchedule">保存</el-button>
      </div>
    </el-dialog>

    <!-- 订阅管理弹窗 -->
    <el-dialog title="订阅管理" :visible.sync="subscribeDialogVisible" width="750px">
      <div v-if="subscribeRow">
        <div style="margin-bottom: 16px; display: flex; justify-content: space-between; align-items: center;">
          <span style="color: #606266;">报表：{{ subscribeRow.reportName }}</span>
          <el-button type="primary" size="mini" icon="el-icon-plus" @click="showAddSubscriberForm = !showAddSubscriberForm">添加订阅</el-button>
        </div>
        <!-- 添加订阅表单 -->
        <el-form v-if="showAddSubscriberForm" :model="subscriberForm" inline size="mini" style="margin-bottom: 12px; padding: 12px; background: #f5f7fa; border-radius: 4px;">
          <el-form-item label="用户名">
            <el-input v-model="subscriberForm.userName" placeholder="请输入" style="width: 100px;" />
          </el-form-item>
          <el-form-item label="部门">
            <el-input v-model="subscriberForm.department" placeholder="请输入" style="width: 100px;" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="subscriberForm.email" placeholder="请输入" style="width: 140px;" />
          </el-form-item>
          <el-form-item label="推送方式">
            <el-select v-model="subscriberForm.deliveryMethod" style="width: 90px;">
              <el-option value="EMAIL" label="邮件" />
              <el-option value="SYSTEM" label="系统通知" />
              <el-option value="SMS" label="短信" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="success" size="mini" :loading="addSubscriberLoading" @click="handleAddSubscriber">确认添加</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="reportSubscribers" border size="mini" max-height="350">
          <el-table-column prop="userName" label="用户名" width="100" />
          <el-table-column prop="department" label="部门" width="120" />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column prop="deliveryMethod" label="推送方式" width="100">
            <template slot-scope="scope">
              <el-tag :type="getDeliveryMethodColor(scope.row.deliveryMethod)" size="mini">{{ getDeliveryMethodText(scope.row.deliveryMethod) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="80">
            <template slot-scope="scope">
              <el-tag :type="getSubscriberStatusColor(scope.row.status)" size="mini">{{ getSubscriberStatusText(scope.row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="mini" style="color: #F56C6C;" @click="handleRemoveSubscriber(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="subscribeDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'

export default {
  name: 'AdvancedReports',
  data() {
    return {
      // 统计数据
      reportStats: {
        totalReports: 0,
        generatedToday: 0,
        totalTemplates: 0,
        totalSubscribers: 0
      },

      // 报表类型
      reportTypes: [
        { id: 1, name: '财务报表', typeKey: 'FINANCIAL_REPORT', description: '财务数据分析报表', icon: 'el-icon-money', reportCount: 0 },
        { id: 2, name: '预算分析报表', typeKey: 'BUDGET_ANALYSIS', description: '预算执行分析报表', icon: 'el-icon-pie-chart', reportCount: 0 },
        { id: 3, name: '绩效报表', typeKey: 'PERFORMANCE_REPORT', description: '绩效考核分析报表', icon: 'el-icon-trophy', reportCount: 0 },
        { id: 4, name: '自定义报表', typeKey: 'CUSTOM_REPORT', description: '用户自定义报表', icon: 'el-icon-magic-stick', reportCount: 0 }
      ],
      selectedReportType: null,

      // 报表列表
      advancedReportList: [],
      loading: false,
      searchKeyword: '',

      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentReport: null,
      reportSubscribers: [],
      generationHistory: [],
      reportConfig: null,

      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,

      // 工具栏弹窗
      generateDialogVisible: false,
      generateReportId: '',
      reportCenterDialogVisible: false,
      settingsDialogVisible: false,
      settingsForm: {
        defaultFrequency: 'MANUAL',
        defaultFormat: 'PDF',
        autoDistribute: false
      },
      templatesDialogVisible: false,
      templateList: [
        { name: '财务月报', icon: 'el-icon-money', desc: '标准财务月度报表' },
        { name: '预算对比', icon: 'el-icon-pie-chart', desc: '预算执行对比分析' },
        { name: '绩效汇总', icon: 'el-icon-trophy', desc: '绩效考核汇总报表' },
        { name: '自定义模板', icon: 'el-icon-magic-stick', desc: '空白自定义模板' },
        { name: '资产负债表', icon: 'el-icon-document', desc: '标准资产负债表' },
        { name: '利润表', icon: 'el-icon-data-line', desc: '标准利润表模板' }
      ],
      helpDialogVisible: false,

      // 预览弹窗
      previewDialogVisible: false,
      previewRow: null,

      // 定时设置弹窗
      scheduleDialogVisible: false,
      scheduleRow: null,
      scheduleForm: {
        frequency: 'MANUAL',
        time: '08:00',
        enabled: false
      },

      // 订阅管理弹窗
      subscribeDialogVisible: false,
      subscribeRow: null,
      showAddSubscriberForm: false,
      addSubscriberLoading: false,
      subscriberForm: {
        userName: '',
        department: '',
        email: '',
        deliveryMethod: 'EMAIL'
      },
      scheduleLoading: false,

      // 表单数据
      reportForm: {
        reportName: '',
        reportType: '',
        dataSource: '',
        generationFrequency: '',
        description: ''
      },

      // 表单验证规则
      reportRules: {
        reportName: [
          { required: true, message: '请输入报表名称', trigger: 'blur' }
        ],
        reportType: [
          { required: true, message: '请选择报表类型', trigger: 'change' }
        ],
        dataSource: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ],
        generationFrequency: [
          { required: true, message: '请选择生成频率', trigger: 'change' }
        ]
      }
    }
  },

  created() {
    this.getAdvancedReportList()
    this.getReportStats()
  },

  methods: {
    // 获取报表列表
    async getAdvancedReportList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword }
        if (this.selectedReportType) {
          params.reportType = this.selectedReportType
        }
        const response = await advancedFeaturesApi.getAdvancedReportsList(params)
        if (response && response.code === 1) {
          const d = response.data || {}
          this.advancedReportList = d.list || d || []
          this.total = d.totalCount || d.total || 0
        }
      } catch (error) {
        this.$message.error('获取报表列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getReportStats() {
      try {
        const response = await advancedFeaturesApi.getAdvancedReportsStats()
        if (response && response.code === 1 && response.data) {
          this.reportStats = {
            totalReports: response.data.totalReports || 0,
            generatedToday: response.data.generatedToday || 0,
            totalTemplates: response.data.totalTemplates || 0,
            totalSubscribers: response.data.totalSubscribers || 0
          }
          // 更新报表类型计数
          if (response.data.typeStats) {
            const ts = response.data.typeStats
            this.reportTypes[0].reportCount = ts.FINANCIAL_REPORT || 0
            this.reportTypes[1].reportCount = ts.BUDGET_ANALYSIS || 0
            this.reportTypes[2].reportCount = ts.PERFORMANCE_REPORT || 0
            this.reportTypes[3].reportCount = ts.CUSTOM_REPORT || 0
          }
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    // 创建报表
    handleCreateReport() {
      this.dialogTitle = '创建高级报表'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑报表
    handleEdit(row) {
      this.dialogTitle = '编辑高级报表'
      this.dialogVisible = true
      this.reportForm = { ...row }
    },

    // 查看详情
    async handleView(row) {
      this.currentReport = row
      this.reportConfig = null
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await Promise.all([
        this.getReportSubscribers(row.reportId),
        this.getGenerationHistory(row.reportId),
        this.getReportConfigData(row.reportId)
      ])
    },

    // 获取报表订阅者
    async getReportSubscribers(reportId) {
      try {
        const response = await advancedFeaturesApi.getReportSubscribers(reportId)
        if (response && response.code === 1) {
          const d = response.data || {}
          this.reportSubscribers = d.subscribers || d || []
        }
      } catch (error) {
        console.error('获取报表订阅者失败：', error)
      }
    },

    // 获取生成历史
    async getGenerationHistory(reportId) {
      try {
        const response = await advancedFeaturesApi.getReportGenerationHistory(reportId)
        if (response && response.code === 1) {
          const d = response.data || {}
          this.generationHistory = d.history || d || []
        }
      } catch (error) {
        console.error('获取生成历史失败：', error)
      }
    },

    // 获取报表配置
    async getReportConfigData(reportId) {
      try {
        const response = await advancedFeaturesApi.getReportConfig(reportId)
        if (response && response.code === 1) {
          this.reportConfig = response.data || null
        }
      } catch (error) {
        console.error('获取报表配置失败：', error)
      }
    },

    // 生成报表
    async handleGenerate(row) {
      this.$confirm('确定生成该报表吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.generateAdvancedReport(row.reportId)
          this.$message.success('报表生成已启动')
          this.getAdvancedReportList()
        } catch (error) {
          this.$message.error('生成失败：' + error.message)
        }
      })
    },

    // 下载报表
    async handleDownloadReport(row) {
      try {
        const response = await advancedFeaturesApi.downloadGeneratedReport(row.reportId)
        if (response && response.data) {
          const jsonStr = JSON.stringify(response.data, null, 2)
          const blob = new Blob([jsonStr], { type: 'application/json' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = (response.data.reportName || 'report_data') + '.json'
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('下载成功')
        } else {
          this.$message.warning('暂无可下载的数据')
        }
      } catch (error) {
        this.$message.error('下载失败：' + error.message)
      }
    },

    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'preview':
          this.handlePreviewReport(row)
          break
        case 'schedule':
          this.handleScheduleReport(row)
          break
        case 'subscribe':
          this.handleSubscribeManagement(row)
          break
        case 'export':
          this.handleExportConfig(row)
          break
        case 'copy':
          this.handleCopyReport(row)
          break
        case 'delete':
          this.handleDeleteReport(row)
          break
      }
    },

    // 预览报表
    handlePreviewReport(row) {
      this.previewRow = row
      this.previewDialogVisible = true
    },

    // 定时设置
    handleScheduleReport(row) {
      this.scheduleRow = row
      this.scheduleForm = {
        frequency: row.generationFrequency || 'MANUAL',
        time: '08:00',
        enabled: row.generationFrequency !== 'MANUAL'
      }
      this.scheduleDialogVisible = true
    },

    // 订阅管理
    handleSubscribeManagement(row) {
      this.subscribeRow = row
      this.subscribeDialogVisible = true
      this.getReportSubscribers(row.reportId)
    },

    // 导出配置
    async handleExportConfig(row) {
      try {
        const response = await advancedFeaturesApi.exportReportConfig(row.reportId)
        if (response && response.data) {
          const jsonStr = JSON.stringify(response.data, null, 2)
          const blob = new Blob([jsonStr], { type: 'application/json' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = (row.reportName || 'report_config') + '.json'
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.warning('暂无可导出的配置')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 保存定时设置
    async handleSaveSchedule() {
      if (!this.scheduleRow) return
      this.scheduleLoading = true
      try {
        const frequency = this.scheduleForm.enabled ? this.scheduleForm.frequency : 'MANUAL'
        await advancedFeaturesApi.updateReportSchedule(this.scheduleRow.reportId, { frequency })
        this.$message.success('定时设置保存成功')
        this.scheduleDialogVisible = false
        this.getAdvancedReportList()
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.scheduleLoading = false
      }
    },

    // 添加订阅者
    async handleAddSubscriber() {
      if (!this.subscriberForm.userName) {
        this.$message.warning('请输入用户名')
        return
      }
      if (!this.subscriberForm.email) {
        this.$message.warning('请输入邮箱')
        return
      }
      this.addSubscriberLoading = true
      try {
        await advancedFeaturesApi.addReportSubscriber(this.subscribeRow.reportId, this.subscriberForm)
        this.$message.success('添加成功')
        this.subscriberForm = { userName: '', department: '', email: '', deliveryMethod: 'EMAIL' }
        this.showAddSubscriberForm = false
        this.getReportSubscribers(this.subscribeRow.reportId)
      } catch (error) {
        this.$message.error('添加失败：' + error.message)
      } finally {
        this.addSubscriberLoading = false
      }
    },

    // 删除订阅者
    async handleRemoveSubscriber(row) {
      try {
        await this.$confirm('确定删除该订阅者？', '提示', { type: 'warning' })
        await advancedFeaturesApi.removeReportSubscriber(row.subscriberId)
        this.$message.success('删除成功')
        this.getReportSubscribers(this.subscribeRow.reportId)
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 复制报表
    async handleCopyReport(row) {
      try {
        await advancedFeaturesApi.copyAdvancedReport(row.reportId)
        this.$message.success('复制成功')
        this.getAdvancedReportList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 删除报表
    handleDeleteReport(row) {
      this.$confirm('确定删除该高级报表吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteAdvancedReport(row.reportId)
          this.$message.success('删除成功')
          this.getAdvancedReportList()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 提交表单
    async handleSubmitForm() {
      this.$refs.reportForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            if (this.reportForm.reportId) {
              await advancedFeaturesApi.updateAdvancedReport(this.reportForm)
              this.$message.success('更新成功')
            } else {
              await advancedFeaturesApi.createAdvancedReport(this.reportForm)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getAdvancedReportList()
          } catch (error) {
            this.$message.error('操作失败：' + error.message)
          } finally {
            this.submitLoading = false
          }
        }
      })
    },

    // 重置表单
    resetForm() {
      this.reportForm = {
        reportName: '',
        reportType: '',
        dataSource: '',
        generationFrequency: '',
        description: ''
      }
      this.$nextTick(() => {
        this.$refs.reportForm && this.$refs.reportForm.clearValidate()
      })
    },

    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },

    // 刷新
    handleRefresh() {
      this.getAdvancedReportList()
      this.getReportStats()
    },

    // 生成报表
    handleGenerateReport() {
      this.generateReportId = ''
      this.generateDialogVisible = true
    },

    // 确认生成报表
    async handleConfirmGenerate() {
      if (!this.generateReportId) {
        this.$message.warning('请选择要生成的报表')
        return
      }
      try {
        await advancedFeaturesApi.generateAdvancedReport(this.generateReportId)
        this.$message.success('报表生成已启动')
        this.generateDialogVisible = false
        this.getAdvancedReportList()
        this.getReportStats()
      } catch (error) {
        this.$message.error('生成失败：' + error.message)
      }
    },

    // 报表中心
    handleReportCenter() {
      this.reportCenterDialogVisible = true
    },

    // 报表设置
    handleSettings() {
      this.settingsDialogVisible = true
    },

    // 报表模板
    handleTemplates() {
      this.templatesDialogVisible = true
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 刷新报表类型
    refreshReportTypes() {
      this.getAdvancedReportList()
      this.getReportStats()
      this.$message.success('已刷新')
    },

    // 选择报表类型（点击切换筛选）
    handleSelectReportType(reportType) {
      if (this.selectedReportType === reportType.typeKey) {
        this.selectedReportType = null
      } else {
        this.selectedReportType = reportType.typeKey
      }
      this.getAdvancedReportList()
    },

    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },

    // 获取报表类型颜色
    getReportTypeColor(type) {
      const colorMap = {
        'FINANCIAL_REPORT': 'primary',
        'BUDGET_ANALYSIS': 'success',
        'PERFORMANCE_REPORT': 'warning',
        'CUSTOM_REPORT': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 获取数据源文本
    getDataSourceText(source) {
      const textMap = {
        'BUDGET_DATABASE': '预算数据库',
        'FINANCIAL_DATABASE': '财务数据库',
        'EXTERNAL_API': '外部API',
        'FILE_IMPORT': '文件导入'
      }
      return textMap[source] || source
    },

    // 获取报表类型文本
    getReportTypeText(type) {
      const textMap = {
        'FINANCIAL_REPORT': '财务报表',
        'BUDGET_ANALYSIS': '预算分析报表',
        'PERFORMANCE_REPORT': '绩效报表',
        'CUSTOM_REPORT': '自定义报表'
      }
      return textMap[type] || type
    },

    // 获取频率颜色
    getFrequencyColor(frequency) {
      const colorMap = {
        'MANUAL': 'info',
        'DAILY': 'success',
        'WEEKLY': 'primary',
        'MONTHLY': 'warning',
        'QUARTERLY': 'danger'
      }
      return colorMap[frequency] || 'info'
    },

    // 获取频率文本
    getFrequencyText(frequency) {
      const textMap = {
        'MANUAL': '手动',
        'DAILY': '每日',
        'WEEKLY': '每周',
        'MONTHLY': '每月',
        'QUARTERLY': '每季度'
      }
      return textMap[frequency] || frequency
    },

    // 获取推送方式颜色
    getDeliveryMethodColor(method) {
      const colorMap = {
        'EMAIL': 'primary',
        'SMS': 'success',
        'SYSTEM_NOTIFICATION': 'warning',
        'FILE_DOWNLOAD': 'info'
      }
      return colorMap[method] || 'info'
    },

    // 获取推送方式文本
    getDeliveryMethodText(method) {
      const textMap = {
        'EMAIL': '邮件',
        'SMS': '短信',
        'SYSTEM_NOTIFICATION': '系统通知',
        'FILE_DOWNLOAD': '文件下载'
      }
      return textMap[method] || method
    },

    // 获取订阅者状态颜色
    getSubscriberStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'SUSPENDED': 'danger'
      }
      return colorMap[status] || 'info'
    },

    // 获取订阅者状态文本
    getSubscriberStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'INACTIVE': '非活跃',
        'SUSPENDED': '暂停'
      }
      return textMap[status] || status
    },

    // 获取生成类型颜色
    getGenerateTypeColor(type) {
      const colorMap = {
        'MANUAL': 'primary',
        'SCHEDULED': 'success',
        'API_TRIGGER': 'warning'
      }
      return colorMap[type] || 'info'
    },

    // 获取生成类型文本
    getGenerateTypeText(type) {
      const textMap = {
        'MANUAL': '手动生成',
        'SCHEDULED': '定时生成',
        'API_TRIGGER': 'API触发'
      }
      return textMap[type] || type
    },

    // 获取生成状态颜色
    getGenerateStatusColor(status) {
      const colorMap = {
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'PROCESSING': 'warning'
      }
      return colorMap[status] || 'info'
    },

    // 获取生成状态文本
    getGenerateStatusText(status) {
      const textMap = {
        'SUCCESS': '成功',
        'FAILED': '失败',
        'PROCESSING': '处理中'
      }
      return textMap[status] || status
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'DRAFT': 'info',
        'ERROR': 'danger'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'INACTIVE': '非活跃',
        'DRAFT': '草稿',
        'ERROR': '错误'
      }
      return textMap[status] || status
    }
  }
}
</script>