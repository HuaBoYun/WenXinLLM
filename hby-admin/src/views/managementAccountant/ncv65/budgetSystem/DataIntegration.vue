<template>
  <div class="data-integration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>数据集成</h2>
      <p>管理预算系统与外部系统的数据集成，包括数据源配置、同步规则、映射关系和集成监控</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleAddIntegration">新建集成</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-video-play" @click="handleSyncAll">同步所有</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleMonitor">集成监控</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">集成设置</el-button>
            <el-button icon="el-icon-document" @click="handleLogs">同步日志</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 集成统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ integrationStats.totalIntegrations }}</div>
            <div class="stat-label">集成总数</div>
            <div class="stat-description">已配置的数据集成</div>
            <div class="stat-trend">
              <i class="el-icon-connection"></i>
              <span>多源集成</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-connection"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ integrationStats.activeIntegrations }}</div>
            <div class="stat-label">活跃集成</div>
            <div class="stat-description">正在运行的集成</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>运行正常</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card sync-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ integrationStats.todaySyncs }}</div>
            <div class="stat-label">今日同步</div>
            <div class="stat-description">今日同步次数</div>
            <div class="stat-trend">
              <i class="el-icon-refresh"></i>
              <span>实时同步</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-refresh"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card error-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ integrationStats.errorCount }}</div>
            <div class="stat-label">同步错误</div>
            <div class="stat-description">需要处理的错误</div>
            <div class="stat-trend">
              <i class="el-icon-warning"></i>
              <span>需要关注</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据源类型 -->
    <el-card class="datasource-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>数据源类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshDataSourceTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="4" v-for="sourceType in dataSourceTypes" :key="sourceType.id">
          <el-card
            class="datasource-type-item"
            shadow="hover"
            @click.native="handleSelectSourceType(sourceType)"
            :class="{ 'selected': selectedSourceType === sourceType.id }"
          >
            <div class="datasource-type-icon">
              <i :class="sourceType.icon"></i>
            </div>
            <div class="datasource-type-title">{{ sourceType.name }}</div>
            <div class="datasource-type-description">{{ sourceType.description }}</div>
            <div class="datasource-type-stats">
              <span class="integration-count">{{ sourceType.integrationCount }} 个集成</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 集成列表 -->
    <el-card class="integration-list-card" shadow="never">
      <div slot="header" class="card-header">
        <span>集成列表</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索集成名称"
            size="mini"
            clearable
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getIntegrationList"
            @clear="getIntegrationList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getIntegrationList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="integrationList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="integrationName" label="集成名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.integrationName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="integrationType" label="数据源类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSourceTypeColor(scope.row.integrationType)" size="mini">
              {{ getSourceTypeText(scope.row.integrationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="syncFrequency" label="同步模式" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSyncModeColor(scope.row.syncFrequency)" size="mini">
              {{ getSyncModeText(scope.row.syncFrequency) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="syncStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.syncStatus)" size="mini">
              {{ getStatusText(scope.row.syncStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastSyncTime" label="最后同步" width="150" align="center" />
        <el-table-column prop="totalRecords" label="总记录数" width="100" align="center" />
        <el-table-column prop="failureRecords" label="失败记录" width="100" align="center">
          <template slot-scope="scope">
            <span :class="scope.row.failureRecords > 0 ? 'error-count' : 'normal-count'">
              {{ scope.row.failureRecords || 0 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="creatorName" label="创建人" width="100" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-video-play"
              @click.stop="handleSync(scope.row)"
              :disabled="scope.row.syncStatus === 'SYNCING'"
            >同步</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="text" size="mini" @click.stop>
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="test">测试连接</el-dropdown-item>
                <el-dropdown-item command="mapping">字段映射</el-dropdown-item>
                <el-dropdown-item command="logs">同步日志</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 集成详情抽屉 -->
    <el-drawer
      title="集成详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="60%"
    >
      <div class="detail-content" v-if="currentIntegration">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="集成基本信息" :column="2" border>
              <el-descriptions-item label="集成名称">{{ currentIntegration.integrationName }}</el-descriptions-item>
              <el-descriptions-item label="数据源类型">{{ getSourceTypeText(currentIntegration.integrationType) }}</el-descriptions-item>
              <el-descriptions-item label="同步模式">{{ getSyncModeText(currentIntegration.syncFrequency) }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentIntegration.syncStatus)" size="mini">
                  {{ getStatusText(currentIntegration.syncStatus) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="来源系统">{{ currentIntegration.sourceSystem }}</el-descriptions-item>
              <el-descriptions-item label="目标系统">{{ currentIntegration.targetSystem }}</el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentIntegration.creatorName }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentIntegration.createTime }}</el-descriptions-item>
              <el-descriptions-item label="总记录数">{{ currentIntegration.totalRecords || 0 }}</el-descriptions-item>
              <el-descriptions-item label="成功/失败">{{ currentIntegration.successRecords || 0 }} / {{ currentIntegration.failureRecords || 0 }}</el-descriptions-item>
              <el-descriptions-item label="备注" :span="2">{{ currentIntegration.remark }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="连接配置" name="connection">
            <el-form label-width="120px" size="small">
              <el-form-item label="来源系统">
                <el-input :value="currentIntegration.sourceSystem" readonly />
              </el-form-item>
              <el-form-item label="目标系统">
                <el-input :value="currentIntegration.targetSystem" readonly />
              </el-form-item>
              <el-form-item label="同步频率">
                <el-input :value="currentIntegration.syncFrequency" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="字段映射" name="mapping">
            <el-table :data="fieldMappings" border size="mini" empty-text="暂无字段映射数据">
              <el-table-column prop="sourceField" label="来源字段" width="120" />
              <el-table-column prop="sourceFieldName" label="来源字段名称" width="120" />
              <el-table-column prop="targetField" label="目标字段" width="120" />
              <el-table-column prop="targetFieldName" label="目标字段名称" width="120" />
              <el-table-column prop="dataType" label="数据类型" width="90" />
              <el-table-column prop="required" label="必填" width="60" align="center">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.required ? 'danger' : 'info'" size="mini">
                    {{ scope.row.required ? '是' : '否' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="defaultValue" label="默认值" width="100" />
              <el-table-column prop="convertRule" label="转换规则" show-overflow-tooltip />
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="同步日志" name="logs">
            <el-table :data="syncLogs" border size="mini">
              <el-table-column prop="syncTime" label="同步时间" width="150" />
              <el-table-column prop="syncResult" label="同步结果" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.syncResult === 'SUCCESS' ? 'success' : 'danger'" size="mini">
                    {{ scope.row.syncResult === 'SUCCESS' ? '成功' : '失败' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="recordCount" label="记录数" width="100" align="center" />
              <el-table-column prop="duration" label="耗时" width="100" align="center" />
              <el-table-column prop="errorMessage" label="错误信息" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑集成对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="integrationForm"
        :model="integrationForm"
        :rules="integrationRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="集成名称" prop="integrationName">
              <el-input v-model="integrationForm.integrationName" placeholder="请输入集成名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据源类型" prop="integrationType">
              <el-select v-model="integrationForm.integrationType" placeholder="请选择数据源类型" style="width: 100%">
                <el-option value="DATABASE" label="数据库" />
                <el-option value="API" label="API接口" />
                <el-option value="FILE" label="文件" />
                <el-option value="ERP" label="ERP系统" />
                <el-option value="OA" label="OA系统" />
                <el-option value="BI" label="BI系统" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="同步频率" prop="syncFrequency">
              <el-select v-model="integrationForm.syncFrequency" placeholder="请选择同步模式" style="width: 100%">
                <el-option value="MANUAL" label="手动同步" />
                <el-option value="SCHEDULED" label="定时同步" />
                <el-option value="REALTIME" label="实时同步" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标系统" prop="targetSystem">
              <el-input v-model="integrationForm.targetSystem" placeholder="请输入目标系统" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="来源系统" prop="sourceSystem">
          <el-input v-model="integrationForm.sourceSystem" placeholder="请输入来源系统/连接地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="integrationForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTestConnection" :loading="testLoading">测试连接</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 集成监控弹窗 -->
    <el-dialog title="集成监控" :visible.sync="monitorDialogVisible" width="800px">
      <div v-loading="monitorLoading">
        <el-row :gutter="16" style="margin-bottom: 20px;">
          <el-col :span="6">
            <div class="monitor-stat-item">
              <div class="monitor-stat-value">{{ monitorData.totalIntegrations || 0 }}</div>
              <div class="monitor-stat-label">集成总数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="monitor-stat-item">
              <div class="monitor-stat-value" style="color: #67C23A;">{{ monitorData.activeCount || 0 }}</div>
              <div class="monitor-stat-label">活跃</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="monitor-stat-item">
              <div class="monitor-stat-value" style="color: #F56C6C;">{{ monitorData.errorCount || 0 }}</div>
              <div class="monitor-stat-label">错误</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="monitor-stat-item">
              <div class="monitor-stat-value" style="color: #409EFF;">{{ monitorData.successRate || '0' }}%</div>
              <div class="monitor-stat-label">成功率</div>
            </div>
          </el-col>
        </el-row>
        <el-descriptions title="数据统计" :column="3" border size="small">
          <el-descriptions-item label="总记录数">{{ monitorData.totalRecords || 0 }}</el-descriptions-item>
          <el-descriptions-item label="成功记录">{{ monitorData.successRecords || 0 }}</el-descriptions-item>
          <el-descriptions-item label="失败记录">{{ monitorData.failureRecords || 0 }}</el-descriptions-item>
          <el-descriptions-item label="同步中">{{ monitorData.syncingCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="已停用">{{ monitorData.inactiveCount || 0 }}</el-descriptions-item>
        </el-descriptions>
        <el-table :data="monitorData.integrations || []" border size="mini" style="margin-top: 16px;" max-height="300">
          <el-table-column prop="integrationName" label="集成名称" width="180" />
          <el-table-column prop="integrationType" label="类型" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getSourceTypeColor(scope.row.integrationType)" size="mini">{{ getSourceTypeText(scope.row.integrationType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="syncStatus" label="状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getStatusColor(scope.row.syncStatus)" size="mini">{{ getStatusText(scope.row.syncStatus) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="totalRecords" label="总记录" width="80" align="center" />
          <el-table-column prop="lastSyncTime" label="最后同步" width="150" align="center" />
        </el-table>
      </div>
    </el-dialog>

    <!-- 集成设置弹窗 -->
    <el-dialog title="集成设置" :visible.sync="settingsDialogVisible" width="600px">
      <div v-loading="settingsLoading">
        <el-form :model="settingsForm" label-width="140px" size="small">
          <el-form-item label="默认同步模式">
            <el-select v-model="settingsForm.defaultSyncMode" style="width: 100%">
              <el-option value="MANUAL" label="手动同步" />
              <el-option value="SCHEDULED" label="定时同步" />
              <el-option value="REALTIME" label="实时同步" />
            </el-select>
          </el-form-item>
          <el-form-item label="每页显示条数">
            <el-input-number v-model="settingsForm.defaultPageSize" :min="10" :max="100" />
          </el-form-item>
          <el-form-item label="最大重试次数">
            <el-input-number v-model="settingsForm.maxRetryCount" :min="0" :max="10" />
          </el-form-item>
          <el-form-item label="连接超时(秒)">
            <el-input-number v-model="settingsForm.connectionTimeout" :min="5" :max="300" />
          </el-form-item>
          <el-form-item label="读取超时(秒)">
            <el-input-number v-model="settingsForm.readTimeout" :min="5" :max="600" />
          </el-form-item>
          <el-form-item label="同步线程池大小">
            <el-input-number v-model="settingsForm.syncThreadPoolSize" :min="1" :max="20" />
          </el-form-item>
          <el-form-item label="自动重试">
            <el-switch v-model="settingsForm.enableAutoRetry" />
          </el-form-item>
          <el-form-item label="启用通知">
            <el-switch v-model="settingsForm.enableNotification" />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSettings" :loading="settingsSaving">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 同步日志弹窗 -->
    <el-dialog title="同步日志" :visible.sync="syncLogDialogVisible" width="1000px">
      <el-table :data="allSyncLogs" border size="mini" v-loading="syncLogLoading" max-height="400">
        <el-table-column prop="integrationName" label="集成名称" width="150" show-overflow-tooltip />
        <el-table-column prop="integrationType" label="集成类型" width="90" align="center">
          <template slot-scope="scope">
            {{ getSourceTypeText(scope.row.integrationType) }}
          </template>
        </el-table-column>
        <el-table-column prop="sourceSystem" label="来源系统" width="120" show-overflow-tooltip />
        <el-table-column prop="syncFrequency" label="同步频率" width="80" align="center">
          <template slot-scope="scope">
            {{ getSyncModeText(scope.row.syncFrequency) }}
          </template>
        </el-table-column>
        <el-table-column prop="syncTime" label="同步时间" width="150" align="center" />
        <el-table-column prop="syncStatus" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.syncStatus)" size="mini">{{ getStatusText(scope.row.syncStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalRecords" label="总记录" width="70" align="center" />
        <el-table-column prop="successRecords" label="成功" width="60" align="center" />
        <el-table-column prop="failureRecords" label="失败" width="60" align="center" />
        <el-table-column prop="duration" label="耗时" width="60" align="center" />
        <el-table-column prop="message" label="消息" show-overflow-tooltip />
      </el-table>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="数据集成帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>数据集成模块用于管理预算系统与外部系统的数据同步，支持多种数据源类型。</p>
        <h4>数据源类型</h4>
        <ul>
          <li><strong>数据库</strong>：支持达梦、Oracle、MySQL等关系型数据库的数据同步</li>
          <li><strong>API接口</strong>：支持REST、SOAP等接口的数据集成</li>
          <li><strong>文件</strong>：支持Excel、CSV等文件格式的数据导入</li>
          <li><strong>ERP系统</strong>：支持SAP、Oracle ERP等企业资源计划系统的数据对接</li>
          <li><strong>OA系统</strong>：支持钉钉、企业微信等办公自动化系统的数据同步</li>
          <li><strong>BI系统</strong>：支持Tableau、Power BI等商业智能系统的数据集成</li>
        </ul>
        <h4>操作指南</h4>
        <ol>
          <li>点击"新建集成"创建新的数据集成配置</li>
          <li>填写集成名称、数据源类型、来源系统等信息</li>
          <li>点击"测试连接"验证连接是否正常</li>
          <li>保存后可在列表中管理集成，执行同步操作</li>
          <li>通过"集成监控"查看所有集成的运行状态</li>
        </ol>
      </div>
    </el-dialog>

    <!-- 测试连接结果弹窗 -->
    <el-dialog title="连接测试结果" :visible.sync="testResultDialogVisible" width="550px">
      <div v-loading="testResultLoading">
        <div v-if="!testResultLoading && testResultData" style="padding: 0 10px;">
          <el-result
            :icon="testResultData.success ? 'success' : 'error'"
            :title="testResultData.success ? '连接成功' : '连接失败'"
            :sub-title="testResultData.message"
          >
          </el-result>
          <el-divider content-position="left">基本信息</el-divider>
          <el-row :gutter="10" style="margin-bottom: 8px;">
            <el-col :span="8"><span style="color:#909399;">集成名称：</span></el-col>
            <el-col :span="16">{{ testResultData.integrationName || '-' }}</el-col>
          </el-row>
          <el-row :gutter="10" style="margin-bottom: 8px;">
            <el-col :span="8"><span style="color:#909399;">集成类型：</span></el-col>
            <el-col :span="16">{{ getSourceTypeText(testResultData.integrationType) }}</el-col>
          </el-row>
          <el-row :gutter="10" style="margin-bottom: 8px;">
            <el-col :span="8"><span style="color:#909399;">来源系统：</span></el-col>
            <el-col :span="16">{{ testResultData.sourceSystem || '-' }}</el-col>
          </el-row>
          <el-row :gutter="10" style="margin-bottom: 8px;">
            <el-col :span="8"><span style="color:#909399;">响应时间：</span></el-col>
            <el-col :span="16">{{ testResultData.responseTime || '-' }}</el-col>
          </el-row>
          <el-row :gutter="10" style="margin-bottom: 8px;">
            <el-col :span="8"><span style="color:#909399;">测试时间：</span></el-col>
            <el-col :span="16">{{ testResultData.testTime || '-' }}</el-col>
          </el-row>
          <template v-if="testResultData.detail">
            <el-divider content-position="left">详细信息</el-divider>
            <el-row v-for="(val, key) in testResultData.detail" :key="key" :gutter="10" style="margin-bottom: 6px;">
              <el-col :span="8"><span style="color:#909399;">{{ key }}：</span></el-col>
              <el-col :span="16">{{ val }}</el-col>
            </el-row>
          </template>
        </div>
      </div>
    </el-dialog>

    <!-- 字段映射弹窗 -->
    <el-dialog
      :title="'字段映射 - ' + (currentMappingIntegration ? currentMappingIntegration.integrationName : '')"
      :visible.sync="fieldMappingDialogVisible"
      width="1050px"
      :close-on-click-modal="false"
    >
      <div v-loading="fieldMappingLoading">
        <div style="margin-bottom: 10px; text-align: right;">
          <el-button size="mini" type="primary" icon="el-icon-plus" @click="handleAddMapping">新增映射</el-button>
        </div>
        <el-table :data="fieldMappingList" border size="mini" max-height="400">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="来源字段" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sourceField" size="mini" placeholder="字段名" />
            </template>
          </el-table-column>
          <el-table-column label="来源字段名称" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sourceFieldName" size="mini" placeholder="显示名" />
            </template>
          </el-table-column>
          <el-table-column label="目标字段" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.targetField" size="mini" placeholder="字段名" />
            </template>
          </el-table-column>
          <el-table-column label="目标字段名称" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.targetFieldName" size="mini" placeholder="显示名" />
            </template>
          </el-table-column>
          <el-table-column label="数据类型" width="110">
            <template slot-scope="scope">
              <el-select v-model="scope.row.dataType" size="mini" placeholder="类型">
                <el-option label="VARCHAR" value="VARCHAR" />
                <el-option label="DECIMAL" value="DECIMAL" />
                <el-option label="INTEGER" value="INTEGER" />
                <el-option label="DATE" value="DATE" />
                <el-option label="TIMESTAMP" value="TIMESTAMP" />
                <el-option label="CLOB" value="CLOB" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="必填" width="60" align="center">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.required" />
            </template>
          </el-table-column>
          <el-table-column label="默认值" width="100">
            <template slot-scope="scope">
              <el-input v-model="scope.row.defaultValue" size="mini" placeholder="默认值" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="60" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="mini" style="color:#F56C6C;" @click="handleDeleteMapping(scope.$index)">
                <i class="el-icon-delete"></i>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="fieldMappingDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="fieldMappingSaving" @click="handleSaveFieldMappings">保 存</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { budgetSystemApi } from '@/api/managementAccountant/ncv65/budgetSystem'

export default {
  name: 'DataIntegration',
  data() {
    return {
      // 统计数据
      integrationStats: {
        totalIntegrations: 0,
        activeIntegrations: 0,
        todaySyncs: 0,
        errorCount: 0
      },

      // 数据源类型
      dataSourceTypes: [
        { id: 'DATABASE', name: '数据库', description: 'MySQL、Oracle等', icon: 'el-icon-coin', integrationCount: 0 },
        { id: 'API', name: 'API接口', description: 'REST、SOAP接口', icon: 'el-icon-link', integrationCount: 0 },
        { id: 'FILE', name: '文件', description: 'Excel、CSV文件', icon: 'el-icon-document', integrationCount: 0 },
        { id: 'ERP', name: 'ERP系统', description: 'SAP、Oracle ERP', icon: 'el-icon-office-building', integrationCount: 0 },
        { id: 'OA', name: 'OA系统', description: '钉钉、企业微信', icon: 'el-icon-chat-dot-round', integrationCount: 0 },
        { id: 'BI', name: 'BI系统', description: 'Tableau、Power BI', icon: 'el-icon-data-analysis', integrationCount: 0 }
      ],
      selectedSourceType: null,

      // 集成列表
      integrationList: [],
      allIntegrationList: [],
      loading: false,
      searchKeyword: '',

      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentIntegration: null,
      fieldMappings: [],
      syncLogs: [],

      // 新建/编辑对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      testLoading: false,

      // 弹窗控制
      monitorDialogVisible: false,
      monitorData: {},
      monitorLoading: false,
      settingsDialogVisible: false,
      settingsForm: {},
      settingsLoading: false,
      settingsSaving: false,
      syncLogDialogVisible: false,
      allSyncLogs: [],
      syncLogLoading: false,
      helpDialogVisible: false,

      // 测试连接结果弹窗
      testResultDialogVisible: false,
      testResultData: {},
      testResultLoading: false,

      // 字段映射弹窗
      fieldMappingDialogVisible: false,
      fieldMappingList: [],
      fieldMappingLoading: false,
      fieldMappingSaving: false,
      currentMappingIntegration: null,

      // 表单数据 - 字段与后端Entity对齐
      integrationForm: {
        integrationName: '',
        integrationType: '',
        syncFrequency: '',
        sourceSystem: '',
        targetSystem: '',
        remark: ''
      },

      // 表单验证规则
      integrationRules: {
        integrationName: [
          { required: true, message: '请输入集成名称', trigger: 'blur' }
        ],
        integrationType: [
          { required: true, message: '请选择数据源类型', trigger: 'change' }
        ],
        sourceSystem: [
          { required: true, message: '请输入来源系统/连接地址', trigger: 'blur' }
        ]
      }
    }
  },

  created() {
    this.getIntegrationList()
    this.getIntegrationStats()
    this.getSourceTypeStats()
  },

  methods: {
    // 获取集成列表
    async getIntegrationList() {
      this.loading = true
      try {
        const params = {}
        if (this.selectedSourceType) {
          params.integrationType = this.selectedSourceType
        }
        if (this.searchKeyword) {
          params.integrationName = this.searchKeyword
        }
        const response = await budgetSystemApi.getDataIntegrationList(params)
        if (response.code === 1 && response.data) {
          const list = response.data.tlist || response.data.list || response.data || []
          this.allIntegrationList = list
          this.integrationList = list
        }
      } catch (error) {
        this.$message.error('获取集成列表失败：' + (error.message || '未知错误'))
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getIntegrationStats() {
      try {
        const response = await budgetSystemApi.getIntegrationStats()
        if (response.code === 1 && response.data) {
          this.integrationStats = response.data
        }
      } catch (error) {
        console.error('获取统计数据失败', error)
      }
    },

    // 获取数据源类型统计
    async getSourceTypeStats() {
      try {
        const response = await budgetSystemApi.getSourceTypeStats()
        if (response.code === 1 && response.data) {
          const statsMap = {}
          response.data.forEach(item => {
            statsMap[item.sourceType] = item.integrationCount
          })
          this.dataSourceTypes.forEach(type => {
            if (statsMap[type.id] !== undefined) {
              type.integrationCount = statsMap[type.id]
            }
          })
        }
      } catch (error) {
        console.error('获取数据源类型统计失败', error)
      }
    },

    // 新建集成
    handleAddIntegration() {
      this.dialogTitle = '新建集成'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑集成
    handleEdit(row) {
      this.dialogTitle = '编辑集成'
      this.dialogVisible = true
      this.integrationForm = {
        integrationId: row.integrationId,
        integrationName: row.integrationName,
        integrationType: row.integrationType,
        syncFrequency: row.syncFrequency,
        sourceSystem: row.sourceSystem,
        targetSystem: row.targetSystem,
        remark: row.remark
      }
    },

    // 查看详情
    async handleView(row) {
      this.currentIntegration = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await this.getFieldMappings(row.integrationId)
      await this.getSyncLogs(row.integrationId)
    },

    // 获取字段映射
    async getFieldMappings(integrationId) {
      try {
        const response = await budgetSystemApi.getFieldMappings(integrationId)
        if (response.code === 1) {
          this.fieldMappings = response.data || []
        }
      } catch (error) {
        this.$message.error('获取字段映射失败')
      }
    },

    // 获取同步日志
    async getSyncLogs(integrationId) {
      try {
        const response = await budgetSystemApi.getSyncLogs(integrationId)
        if (response.code === 1) {
          this.syncLogs = response.data || []
        }
      } catch (error) {
        this.$message.error('获取同步日志失败')
      }
    },

    // 同步数据
    async handleSync(row) {
      try {
        await budgetSystemApi.syncDataIntegration(row.integrationId)
        this.$message.success('同步成功')
        this.getIntegrationList()
      } catch (error) {
        this.$message.error('同步失败：' + error.message)
      }
    },

    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'test':
          this.handleTestIntegration(row)
          break
        case 'mapping':
          this.handleFieldMapping(row)
          break
        case 'logs':
          this.handleViewLogs(row)
          break
        case 'copy':
          this.handleCopyIntegration(row)
          break
        case 'delete':
          this.handleDeleteIntegration(row)
          break
      }
    },

    // 测试集成 - 弹窗展示详细结果
    async handleTestIntegration(row) {
      this.testResultDialogVisible = true
      this.testResultLoading = true
      this.testResultData = {}
      try {
        const response = await budgetSystemApi.testDataIntegration(row.integrationId)
        if (response.code === 1 && response.data) {
          this.testResultData = response.data
        } else {
          this.testResultData = { success: false, message: response.msg || '测试失败' }
        }
      } catch (error) {
        this.testResultData = { success: false, message: '连接测试请求失败：' + (error.message || '未知错误') }
      } finally {
        this.testResultLoading = false
      }
    },

    // 字段映射 - 弹窗管理
    async handleFieldMapping(row) {
      this.currentMappingIntegration = row
      this.fieldMappingDialogVisible = true
      this.fieldMappingLoading = true
      this.fieldMappingList = []
      try {
        const response = await budgetSystemApi.getFieldMappings(row.integrationId)
        if (response.code === 1 && response.data) {
          this.fieldMappingList = response.data
        }
      } catch (error) {
        this.$message.error('获取字段映射失败')
      } finally {
        this.fieldMappingLoading = false
      }
    },

    // 保存字段映射
    async handleSaveFieldMappings() {
      if (!this.currentMappingIntegration) return
      this.fieldMappingSaving = true
      try {
        const response = await budgetSystemApi.saveFieldMappings(
          this.currentMappingIntegration.integrationId,
          this.fieldMappingList
        )
        if (response.code === 1) {
          this.$message.success('字段映射保存成功')
          this.fieldMappingDialogVisible = false
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存字段映射失败')
      } finally {
        this.fieldMappingSaving = false
      }
    },

    // 新增映射行
    handleAddMapping() {
      var idx = this.fieldMappingList.length + 1
      this.fieldMappingList.push({
        mappingId: 'MAP_NEW_' + idx,
        sourceField: '',
        sourceFieldName: '',
        targetField: '',
        targetFieldName: '',
        dataType: 'VARCHAR',
        required: false,
        defaultValue: '',
        convertRule: ''
      })
    },

    // 删除映射行
    handleDeleteMapping(index) {
      this.fieldMappingList.splice(index, 1)
    },

    // 查看日志
    handleViewLogs(row) {
      this.handleView(row)
      this.detailActiveTab = 'logs'
    },

    // 复制集成
    async handleCopyIntegration(row) {
      try {
        await budgetSystemApi.copyDataIntegration(row.integrationId)
        this.$message.success('复制成功')
        this.getIntegrationList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 删除集成
    handleDeleteIntegration(row) {
      this.$confirm('确定删除该集成吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await budgetSystemApi.deleteDataIntegration(row.integrationId)
          this.$message.success('删除成功')
          this.getIntegrationList()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 测试连接
    async handleTestConnection() {
      this.testLoading = true
      try {
        await budgetSystemApi.testConnection(this.integrationForm)
        this.$message.success('连接测试成功')
      } catch (error) {
        this.$message.error('连接测试失败：' + error.message)
      } finally {
        this.testLoading = false
      }
    },

    // 提交表单
    async handleSubmitForm() {
      this.$refs.integrationForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            if (this.integrationForm.integrationId) {
              await budgetSystemApi.updateDataIntegration(this.integrationForm)
              this.$message.success('更新成功')
            } else {
              await budgetSystemApi.createDataIntegration(this.integrationForm)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getIntegrationList()
            this.getIntegrationStats()
            this.getSourceTypeStats()
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
      this.integrationForm = {
        integrationName: '',
        integrationType: '',
        syncFrequency: '',
        sourceSystem: '',
        targetSystem: '',
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.integrationForm && this.$refs.integrationForm.clearValidate()
      })
    },

    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },

    // 刷新
    handleRefresh() {
      this.selectedSourceType = null
      this.searchKeyword = ''
      this.getIntegrationList()
      this.getIntegrationStats()
      this.getSourceTypeStats()
      this.$message.success('数据已刷新')
    },

    // 同步所有
    async handleSyncAll() {
      this.$confirm('确定同步所有活跃的集成吗？此操作将启动所有集成的同步任务。', '同步确认', {
        confirmButtonText: '确定同步',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await budgetSystemApi.syncAllIntegrations()
          if (response.code === 1) {
            this.$message.success(response.msg || '同步任务已启动')
          } else {
            this.$message.error(response.msg || '同步失败')
          }
          this.getIntegrationList()
          this.getIntegrationStats()
        } catch (error) {
          this.$message.error('同步失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {})
    },

    // 集成监控 - 弹窗
    async handleMonitor() {
      this.monitorDialogVisible = true
      this.monitorLoading = true
      try {
        const response = await budgetSystemApi.getMonitorData()
        if (response.code === 1 && response.data) {
          this.monitorData = response.data
        }
      } catch (error) {
        this.$message.error('获取监控数据失败')
      } finally {
        this.monitorLoading = false
      }
    },

    // 集成设置 - 弹窗
    async handleSettings() {
      this.settingsDialogVisible = true
      this.settingsLoading = true
      try {
        const response = await budgetSystemApi.getIntegrationSettings()
        if (response.code === 1 && response.data) {
          this.settingsForm = { ...response.data }
        }
      } catch (error) {
        this.$message.error('获取设置失败')
      } finally {
        this.settingsLoading = false
      }
    },

    // 保存集成设置
    async handleSaveSettings() {
      this.settingsSaving = true
      try {
        const response = await budgetSystemApi.saveIntegrationSettings(this.settingsForm)
        if (response.code === 1) {
          this.$message.success('设置保存成功')
          this.settingsDialogVisible = false
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存设置失败')
      } finally {
        this.settingsSaving = false
      }
    },

    // 同步日志 - 弹窗（汇总所有集成的日志）
    async handleLogs() {
      this.syncLogDialogVisible = true
      this.syncLogLoading = true
      this.allSyncLogs = []
      try {
        // 遍历所有集成获取日志
        const list = this.allIntegrationList || this.integrationList
        for (const item of list) {
          if (item.integrationId) {
            try {
              const response = await budgetSystemApi.getSyncLogs(item.integrationId)
              if (response.code === 1 && response.data) {
                this.allSyncLogs = this.allSyncLogs.concat(response.data)
              }
            } catch (e) {
              // 单个失败不影响整体
            }
          }
        }
      } catch (error) {
        this.$message.error('获取同步日志失败')
      } finally {
        this.syncLogLoading = false
      }
    },

    // 帮助 - 弹窗
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 刷新数据源类型
    refreshDataSourceTypes() {
      this.getSourceTypeStats()
      this.$message.success('数据源类型已刷新')
    },

    // 选择数据源类型 - 筛选列表
    handleSelectSourceType(sourceType) {
      if (this.selectedSourceType === sourceType.id) {
        // 再次点击取消筛选
        this.selectedSourceType = null
        this.integrationList = this.allIntegrationList
      } else {
        this.selectedSourceType = sourceType.id
        // 从后端重新查询带筛选条件的列表
        this.getIntegrationList()
      }
    },

    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },

    // 获取数据源类型颜色
    getSourceTypeColor(type) {
      const colorMap = {
        'DATABASE': 'primary',
        'API': 'success',
        'FILE': 'warning',
        'ERP': 'info',
        'OA': 'danger',
        'BI': 'primary'
      }
      return colorMap[type] || 'info'
    },

    // 获取数据源类型文本
    getSourceTypeText(type) {
      const textMap = {
        'DATABASE': '数据库',
        'API': 'API接口',
        'FILE': '文件',
        'ERP': 'ERP系统',
        'OA': 'OA系统',
        'BI': 'BI系统'
      }
      return textMap[type] || type
    },

    // 获取同步模式颜色
    getSyncModeColor(mode) {
      const colorMap = {
        'MANUAL': 'info',
        'SCHEDULED': 'warning',
        'REALTIME': 'success'
      }
      return colorMap[mode] || 'info'
    },

    // 获取同步模式文本
    getSyncModeText(mode) {
      const textMap = {
        'MANUAL': '手动',
        'SCHEDULED': '定时',
        'REALTIME': '实时'
      }
      return textMap[mode] || mode
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'SUCCESS': 'success',
        'INACTIVE': 'warning',
        'ERROR': 'danger',
        'SYNCING': 'primary'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'SUCCESS': '成功',
        'INACTIVE': '停用',
        'ERROR': '错误',
        'SYNCING': '同步中'
      }
      return textMap[status] || status || '未知'
    }
  }
}
</script>


<style scoped>
.data-integration {
  padding: 20px;
}
.page-header {
  margin-bottom: 20px;
}
.page-header h2 {
  margin: 0 0 8px 0;
}
.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}
.toolbar-card {
  margin-bottom: 20px;
}
.text-right {
  text-align: right;
}
.stats-row {
  margin-bottom: 20px;
}
.stat-card {
  position: relative;
  overflow: hidden;
  cursor: pointer;
}
.stat-content {
  position: relative;
  z-index: 1;
}
.stat-number {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 4px;
}
.stat-label {
  font-size: 14px;
  color: #606266;
}
.stat-description {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
.stat-trend {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
}
.stat-icon {
  position: absolute;
  right: 20px;
  top: 20px;
  font-size: 48px;
  opacity: 0.15;
}
.datasource-types-card {
  margin-bottom: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-tools {
  display: flex;
  align-items: center;
}
.datasource-type-item {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}
.datasource-type-item:hover {
  transform: translateY(-2px);
}
.datasource-type-item.selected {
  border-color: #409EFF;
  box-shadow: 0 0 8px rgba(64, 158, 255, 0.3);
}
.datasource-type-icon {
  font-size: 32px;
  color: #409EFF;
  margin-bottom: 8px;
}
.datasource-type-title {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 4px;
}
.datasource-type-description {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}
.datasource-type-stats {
  font-size: 12px;
  color: #409EFF;
}
.integration-list-card {
  margin-bottom: 20px;
}
.error-count {
  color: #F56C6C;
  font-weight: bold;
}
.normal-count {
  color: #67C23A;
}
.detail-content {
  padding: 0 20px;
}
.help-content h4 {
  margin: 16px 0 8px 0;
  color: #303133;
}
.help-content p,
.help-content li {
  color: #606266;
  line-height: 1.8;
}
.monitor-stat-item {
  text-align: center;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
}
.monitor-stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}
.monitor-stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}
</style>