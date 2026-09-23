<template>
  <div class="api-integration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>第三方API集成</h2>
      <p>REST、SOAP、GraphQL等API接口集成，支持外部系统数据交换和业务协同</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateApi">创建API</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-connection" @click="handleTestApi">API测试</el-button>
            <el-button type="info" icon="el-icon-document" @click="handleApiDocs">API文档</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">API设置</el-button>
            <el-button icon="el-icon-monitor" @click="handleMonitor">监控中心</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- API集成统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ apiStats.totalApis }}</div>
            <div class="stat-label">API总数</div>
            <div class="stat-description">已配置API接口数</div>
            <div class="stat-trend">
              <i class="el-icon-link"></i>
              <span>API接口</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-link"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ apiStats.activeApis }}</div>
            <div class="stat-label">活跃API</div>
            <div class="stat-description">正在使用的API</div>
            <div class="stat-trend">
              <i class="el-icon-loading"></i>
              <span>运行中</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-loading"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card calls-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ apiStats.todayCalls }}</div>
            <div class="stat-label">今日调用</div>
            <div class="stat-description">今日API调用次数</div>
            <div class="stat-trend">
              <i class="el-icon-refresh"></i>
              <span>API调用</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-refresh"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card success-rate-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ apiStats.successRate }}%</div>
            <div class="stat-label">成功率</div>
            <div class="stat-description">API调用成功率</div>
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

    <!-- API类型选择 -->
    <el-card class="api-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>API类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshApiTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="apiType in apiTypes" :key="apiType.id">
          <el-card 
            class="api-type-item" 
            shadow="hover" 
            @click.native="handleSelectApiType(apiType)"
            :class="{ 'selected': selectedApiType === apiType.id }"
          >
            <div class="api-type-icon">
              <i :class="apiType.icon"></i>
            </div>
            <div class="api-type-title">{{ apiType.name }}</div>
            <div class="api-type-description">{{ apiType.description }}</div>
            <div class="api-type-stats">
              <span class="api-count">{{ apiType.apiCount }} 个API</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- API列表 -->
    <el-card class="api-list-card" shadow="never">
      <div slot="header" class="card-header">
        <span>API接口管理</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索API"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getApiList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="filteredApiList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="apiName" label="API名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.apiName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="apiType" label="API类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getApiTypeColor(scope.row.apiType)" size="mini">
              {{ getApiTypeText(scope.row.apiType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="requestMethod" label="请求方法" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getMethodColor(scope.row.requestMethod)" size="mini">
              {{ scope.row.requestMethod || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="endpoint" label="接口地址" width="300" show-overflow-tooltip />
        <el-table-column prop="callCount" label="调用次数" width="100" align="center">
          <template slot-scope="scope">
            <span class="call-count">{{ scope.row.callCount || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="avgResponseTime" label="平均响应时间" width="120" align="center">
          <template slot-scope="scope">
            <span class="response-time">{{ scope.row.avgResponseTime || 0 }}ms</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastCallTime" label="最后调用" width="150" align="center">
          <template slot-scope="scope">
            <span class="last-call">{{ formatTime(scope.row.lastCallTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-connection"
              @click="handleTestApi(scope.row)"
            >测试</el-button>
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
                <el-dropdown-item command="logs">调用日志</el-dropdown-item>
                <el-dropdown-item command="monitor">监控</el-dropdown-item>
                <el-dropdown-item command="docs">文档</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- API详情抽屉 -->
    <el-drawer
      title="API接口详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentApi">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="API基本信息" :column="2" border>
              <el-descriptions-item label="API名称">{{ currentApi.apiName }}</el-descriptions-item>
              <el-descriptions-item label="API类型">{{ getApiTypeText(currentApi.apiType) }}</el-descriptions-item>
              <el-descriptions-item label="请求方法">{{ currentApi.requestMethod || '-' }}</el-descriptions-item>
              <el-descriptions-item label="接口地址">{{ currentApi.endpoint }}</el-descriptions-item>
              <el-descriptions-item label="调用次数">{{ currentApi.callCount || 0 }}</el-descriptions-item>
              <el-descriptions-item label="平均响应时间">{{ currentApi.avgResponseTime || 0 }}ms</el-descriptions-item>
              <el-descriptions-item label="最后调用">{{ formatTime(currentApi.lastCallTime) }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentApi.status)" size="mini">
                  {{ getStatusText(currentApi.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ formatTime(currentApi.createTime) }}</el-descriptions-item>
              <el-descriptions-item label="API描述" :span="2">{{ currentApi.description || '-' }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="请求配置" name="config">
            <el-form label-width="120px" size="small">
              <el-form-item label="请求头">
                <el-input :value="currentApi.headers" readonly type="textarea" :rows="3" />
              </el-form-item>
              <el-form-item label="请求参数">
                <el-input :value="currentApi.parameters" readonly type="textarea" :rows="3" />
              </el-form-item>
              <el-form-item label="请求体">
                <el-input :value="currentApi.requestBody" readonly type="textarea" :rows="4" />
              </el-form-item>
              <el-form-item label="认证方式">
                <el-input :value="currentApi.authType" readonly />
              </el-form-item>
              <el-form-item label="超时设置">
                <el-input :value="currentApi.timeout + 'ms'" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="调用日志" name="logs">
            <el-table :data="apiLogs" border size="mini">
              <el-table-column prop="callTime" label="调用时间" width="150">
                <template slot-scope="scope">{{ formatTime(scope.row.callTime) }}</template>
              </el-table-column>
              <el-table-column prop="requestId" label="请求ID" width="200" show-overflow-tooltip />
              <el-table-column prop="responseTime" label="响应时间" width="100" align="center">
                <template slot-scope="scope">
                  <span :class="getResponseTimeClass(scope.row.responseTime)">
                    {{ scope.row.responseTime }}ms
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="responseStatus" label="状态码" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusCodeColor(scope.row.responseStatus)" size="mini">
                    {{ scope.row.responseStatus }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="dataSize" label="数据大小" width="100" align="center">
                <template slot-scope="scope">{{ formatDataSize(scope.row.dataSize) }}</template>
              </el-table-column>
              <el-table-column prop="errorMessage" label="错误信息" show-overflow-tooltip />
              <el-table-column label="操作" width="100" align="center">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" @click="handleViewLogDetail(scope.row)">
                    详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="监控统计" name="monitor">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card class="monitor-card">
                  <div slot="header">调用趋势</div>
                  <div class="chart-container">
                    <!-- 这里可以集成ECharts图表 -->
                    <div class="chart-placeholder">调用趋势图表</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card class="monitor-card">
                  <div slot="header">响应时间分布</div>
                  <div class="chart-container">
                    <!-- 这里可以集成ECharts图表 -->
                    <div class="chart-placeholder">响应时间分布图表</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="12">
                <el-card class="monitor-card">
                  <div slot="header">错误率统计</div>
                  <div class="chart-container">
                    <!-- 这里可以集成ECharts图表 -->
                    <div class="chart-placeholder">错误率统计图表</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card class="monitor-card">
                  <div slot="header">状态码分布</div>
                  <div class="chart-container">
                    <!-- 这里可以集成ECharts图表 -->
                    <div class="chart-placeholder">状态码分布图表</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑API对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="apiForm"
        :model="apiForm"
        :rules="apiRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="API名称" prop="apiName">
              <el-input v-model="apiForm.apiName" placeholder="请输入API名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="API类型" prop="apiType">
              <el-select v-model="apiForm.apiType" placeholder="请选择API类型" style="width: 100%">
                <el-option value="REST" label="REST API" />
                <el-option value="SOAP" label="SOAP API" />
                <el-option value="GRAPHQL" label="GraphQL API" />
                <el-option value="WEBHOOK" label="Webhook" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="请求方法" prop="requestMethod">
              <el-select v-model="apiForm.requestMethod" placeholder="请求方法" style="width: 100%">
                <el-option value="GET" label="GET" />
                <el-option value="POST" label="POST" />
                <el-option value="PUT" label="PUT" />
                <el-option value="DELETE" label="DELETE" />
                <el-option value="PATCH" label="PATCH" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="接口地址" prop="endpoint">
              <el-input v-model="apiForm.endpoint" placeholder="请输入接口地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="认证方式">
              <el-select v-model="apiForm.authType" placeholder="请选择认证方式" style="width: 100%">
                <el-option value="NONE" label="无认证" />
                <el-option value="BASIC" label="Basic认证" />
                <el-option value="BEARER" label="Bearer Token" />
                <el-option value="OAUTH2" label="OAuth2.0" />
                <el-option value="API_KEY" label="API Key" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="超时时间(秒)">
              <el-input-number v-model="apiForm.timeoutSeconds" :min="1" :max="300" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="API描述" prop="description">
          <el-input
            v-model="apiForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入API描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button @click="handleTestApiForm">测试API</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- API测试弹窗 -->
    <el-dialog title="API测试" :visible.sync="testDialogVisible" width="700px" :close-on-click-modal="false">
      <el-form label-width="100px" size="small">
        <el-form-item label="选择API">
          <el-select v-model="testForm.apiId" placeholder="请选择要测试的API" style="width: 100%" @change="onTestApiChange">
            <el-option v-for="item in apiList" :key="item.id" :label="item.apiName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="API地址" v-if="testForm.endpoint">
          <el-input :value="testForm.endpoint" readonly />
        </el-form-item>
        <el-form-item label="请求方法" v-if="testForm.requestMethod">
          <el-tag :type="getMethodColor(testForm.requestMethod)">{{ testForm.requestMethod }}</el-tag>
        </el-form-item>
      </el-form>
      <div v-if="testResult" class="test-result-box">
        <el-divider content-position="left">测试结果</el-divider>
        <el-descriptions :column="2" border size="mini">
          <el-descriptions-item label="状态">
            <el-tag :type="testResult.success ? 'success' : 'danger'" size="mini">{{ testResult.success ? '成功' : '失败' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态码">{{ testResult.statusCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="响应时间">{{ testResult.responseTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="消息">{{ testResult.message || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" :loading="testLoading" @click="executeTest" :disabled="!testForm.apiId">开始测试</el-button>
      </div>
    </el-dialog>

    <!-- API文档弹窗 -->
    <el-dialog title="API文档" :visible.sync="docDialogVisible" width="800px">
      <div v-if="docData">
        <el-descriptions title="接口基本信息" :column="2" border>
          <el-descriptions-item label="API名称">{{ docData.apiName }}</el-descriptions-item>
          <el-descriptions-item label="API类型">{{ getApiTypeText(docData.apiType) }}</el-descriptions-item>
          <el-descriptions-item label="请求方法">
            <el-tag :type="getMethodColor(docData.requestMethod)" size="mini">{{ docData.requestMethod }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="接口地址">{{ docData.apiUrl }}</el-descriptions-item>
          <el-descriptions-item label="认证方式">{{ getAuthTypeText(docData.authType) }}</el-descriptions-item>
          <el-descriptions-item label="超时时间">{{ docData.timeoutSeconds || 30 }}秒</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusColor(docData.apiStatus)" size="mini">{{ getStatusText(docData.apiStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="重试次数">{{ docData.retryCount || 0 }}次</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="left">请求头</el-divider>
        <el-input type="textarea" :rows="3" :value="docData.requestHeaders || '无'" readonly />
        <el-divider content-position="left">请求参数</el-divider>
        <el-input type="textarea" :rows="3" :value="docData.requestParams || '无'" readonly />
        <el-divider content-position="left">请求体模板</el-divider>
        <el-input type="textarea" :rows="4" :value="docData.requestBodyTemplate || '无'" readonly />
        <el-divider content-position="left">响应映射</el-divider>
        <el-input type="textarea" :rows="3" :value="docData.responseMapping || '无'" readonly />
      </div>
      <div v-else class="empty-tip">
        <el-empty description="暂无文档数据" />
      </div>
      <div slot="footer">
        <el-button @click="docDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- API设置弹窗 -->
    <el-dialog title="API全局设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <div v-if="settingsData">
        <el-form label-width="140px" size="small">
          <el-form-item label="默认超时时间(秒)">
            <el-input-number v-model="settingsData.defaultTimeout" :min="1" :max="300" />
          </el-form-item>
          <el-form-item label="默认重试次数">
            <el-input-number v-model="settingsData.defaultRetryCount" :min="0" :max="10" />
          </el-form-item>
          <el-form-item label="全局频率限制(次/分)">
            <el-input-number v-model="settingsData.globalRateLimit" :min="1" :max="10000" />
          </el-form-item>
          <el-form-item label="日志保留天数">
            <el-input-number v-model="settingsData.logRetentionDays" :min="1" :max="365" />
          </el-form-item>
        </el-form>
        <el-divider content-position="left">统计概览</el-divider>
        <el-descriptions :column="2" border size="mini">
          <el-descriptions-item label="API总数">{{ settingsData.totalApis || 0 }}</el-descriptions-item>
          <el-descriptions-item label="活跃API">{{ settingsData.activeApis || 0 }}</el-descriptions-item>
          <el-descriptions-item label="总调用次数">{{ settingsData.totalCalls || 0 }}</el-descriptions-item>
          <el-descriptions-item label="总成功率">{{ settingsData.successRate || 0 }}%</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="settingsSaving" @click="saveSettings">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 监控中心弹窗 -->
    <el-dialog title="API监控中心" :visible.sync="monitorDialogVisible" width="850px">
      <div v-if="monitorData">
        <el-row :gutter="16" style="margin-bottom: 16px;">
          <el-col :span="6">
            <el-statistic title="总调用次数" :value="monitorData.totalCalls || 0" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="成功次数" :value="monitorData.successCount || 0" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="失败次数" :value="monitorData.failureCount || 0" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="平均响应(ms)" :value="monitorData.avgResponseTime || 0" />
          </el-col>
        </el-row>
        <el-divider content-position="left">API状态分布</el-divider>
        <el-table :data="monitorData.apiStatusList || []" border size="mini" style="margin-bottom: 16px;">
          <el-table-column prop="apiName" label="API名称" />
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="callCount" label="调用次数" width="100" align="center" />
          <el-table-column prop="avgResponseTime" label="平均响应(ms)" width="120" align="center" />
          <el-table-column prop="lastCallTime" label="最后调用" width="160" align="center">
            <template slot-scope="scope">{{ formatTime(scope.row.lastCallTime) }}</template>
          </el-table-column>
        </el-table>
      </div>
      <div v-else>
        <el-empty description="加载中..." />
      </div>
      <div slot="footer">
        <el-button @click="monitorDialogVisible = false">关闭</el-button>
        <el-button type="primary" icon="el-icon-refresh" @click="loadMonitorCenter">刷新</el-button>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="API集成帮助" :visible.sync="helpDialogVisible" width="650px">
      <el-collapse v-model="helpActiveNames">
        <el-collapse-item title="什么是API集成？" name="1">
          <p>API集成是指通过REST、SOAP、GraphQL、Webhook等标准协议，将外部系统的接口接入到本系统中，实现数据交换和业务协同。</p>
        </el-collapse-item>
        <el-collapse-item title="如何创建API？" name="2">
          <p>1. 点击"创建API"按钮</p>
          <p>2. 填写API名称、类型、请求方法、接口地址等信息</p>
          <p>3. 选择认证方式并配置认证参数</p>
          <p>4. 点击"测试API"验证连通性</p>
          <p>5. 确认无误后点击"确定"保存</p>
        </el-collapse-item>
        <el-collapse-item title="支持哪些API类型？" name="3">
          <p><b>REST API</b> - 最常用的API类型，基于HTTP协议</p>
          <p><b>SOAP API</b> - 基于XML的Web服务协议</p>
          <p><b>GraphQL API</b> - Facebook开发的查询语言</p>
          <p><b>Webhook</b> - 事件驱动的回调机制</p>
          <p><b>gRPC</b> - Google开发的高性能RPC框架</p>
        </el-collapse-item>
        <el-collapse-item title="如何查看调用日志？" name="4">
          <p>在API列表中，点击操作列的"更多"按钮，选择"调用日志"即可查看该API的历史调用记录。</p>
        </el-collapse-item>
        <el-collapse-item title="如何监控API状态？" name="5">
          <p>点击工具栏的"监控中心"按钮，可以查看所有API的运行状态、调用统计和性能指标。</p>
        </el-collapse-item>
      </el-collapse>
      <div slot="footer">
        <el-button type="primary" @click="helpDialogVisible = false">知道了</el-button>
      </div>
    </el-dialog>

    <!-- 调用日志弹窗 -->
    <el-dialog title="API调用日志" :visible.sync="logDialogVisible" width="900px">
      <div v-if="logDialogApi" style="margin-bottom: 12px;">
        <el-tag type="primary" size="small">{{ logDialogApi.apiName }}</el-tag>
        <el-tag :type="getMethodColor(logDialogApi.requestMethod)" size="small" style="margin-left: 8px;">{{ logDialogApi.requestMethod }}</el-tag>
        <span style="margin-left: 8px; color: #909399; font-size: 13px;">{{ logDialogApi.endpoint }}</span>
      </div>
      <el-table :data="logDialogData" border size="mini" v-loading="logDialogLoading" max-height="400">
        <el-table-column prop="callTime" label="调用时间" width="160">
          <template slot-scope="scope">{{ formatTime(scope.row.callTime) }}</template>
        </el-table-column>
        <el-table-column prop="requestId" label="请求ID" width="180" show-overflow-tooltip />
        <el-table-column prop="responseTime" label="响应时间" width="100" align="center">
          <template slot-scope="scope">
            <span :class="getResponseTimeClass(scope.row.responseTime)">{{ scope.row.responseTime }}ms</span>
          </template>
        </el-table-column>
        <el-table-column prop="responseStatus" label="状态码" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusCodeColor(scope.row.responseStatus)" size="mini">{{ scope.row.responseStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="callStatus" label="调用状态" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.callStatus === 'SUCCESS' ? 'success' : 'danger'" size="mini">
              {{ scope.row.callStatus === 'SUCCESS' ? '成功' : scope.row.callStatus === 'TIMEOUT' ? '超时' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataSize" label="数据大小" width="90" align="center">
          <template slot-scope="scope">{{ formatDataSize(scope.row.dataSize) }}</template>
        </el-table-column>
        <el-table-column prop="errorMessage" label="错误信息" show-overflow-tooltip />
        <el-table-column label="操作" width="70" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="handleViewLogDetail(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="logDialogVisible = false">关闭</el-button>
        <el-button type="primary" icon="el-icon-refresh" @click="loadApiLogs(logDialogApi)">刷新</el-button>
      </div>
    </el-dialog>

    <!-- 单条日志详情弹窗 -->
    <el-dialog title="日志详情" :visible.sync="logDetailVisible" width="700px" append-to-body>
      <div v-if="logDetailData">
        <el-descriptions :column="2" border size="mini">
          <el-descriptions-item label="请求ID">{{ logDetailData.requestId }}</el-descriptions-item>
          <el-descriptions-item label="调用时间">{{ formatTime(logDetailData.callTime) }}</el-descriptions-item>
          <el-descriptions-item label="请求方法">{{ logDetailData.requestMethod }}</el-descriptions-item>
          <el-descriptions-item label="状态码">{{ logDetailData.responseStatus }}</el-descriptions-item>
          <el-descriptions-item label="响应时间">{{ logDetailData.responseTime }}ms</el-descriptions-item>
          <el-descriptions-item label="调用状态">{{ logDetailData.callStatus }}</el-descriptions-item>
          <el-descriptions-item label="数据大小">{{ formatDataSize(logDetailData.dataSize) }}</el-descriptions-item>
          <el-descriptions-item label="调用者IP">{{ logDetailData.callerIp || '-' }}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="left">请求URL</el-divider>
        <el-input :value="logDetailData.requestUrl || '-'" readonly size="small" />
        <el-divider content-position="left">请求头</el-divider>
        <el-input type="textarea" :rows="3" :value="logDetailData.requestHeaders || '-'" readonly />
        <el-divider content-position="left">请求体</el-divider>
        <el-input type="textarea" :rows="3" :value="logDetailData.requestBody || '-'" readonly />
        <el-divider content-position="left">响应体</el-divider>
        <el-input type="textarea" :rows="4" :value="logDetailData.responseBody || '-'" readonly />
        <div v-if="logDetailData.errorMessage">
          <el-divider content-position="left">错误信息</el-divider>
          <el-alert :title="logDetailData.errorMessage" type="error" :closable="false" />
        </div>
      </div>
      <div slot="footer">
        <el-button @click="logDetailVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 单个API监控弹窗 -->
    <el-dialog title="API监控详情" :visible.sync="apiMonitorDialogVisible" width="700px">
      <div v-if="apiMonitorData">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="API名称">{{ apiMonitorData.apiName }}</el-descriptions-item>
          <el-descriptions-item label="API状态">
            <el-tag :type="getStatusColor(apiMonitorData.apiStatus)" size="mini">{{ getStatusText(apiMonitorData.apiStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="总调用次数">{{ apiMonitorData.totalCalls || 0 }}</el-descriptions-item>
          <el-descriptions-item label="成功次数">{{ apiMonitorData.successCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="失败次数">{{ apiMonitorData.failureCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="成功率">{{ apiMonitorData.successRate || 0 }}%</el-descriptions-item>
          <el-descriptions-item label="平均响应时间">{{ apiMonitorData.avgResponseTime || 0 }}ms</el-descriptions-item>
          <el-descriptions-item label="最后调用时间">{{ formatTime(apiMonitorData.lastCallTime) }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div v-else>
        <el-empty description="加载中..." />
      </div>
      <div slot="footer">
        <el-button @click="apiMonitorDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { systemIntegrationApi } from '@/api/managementAccountant/ncv65/systemIntegration'

export default {
  name: 'ApiIntegration',
  data() {
    return {
      // 统计数据
      apiStats: {
        totalApis: 0,
        activeApis: 0,
        todayCalls: 0,
        successRate: 0
      },

      // API类型
      apiTypes: [],
      selectedApiType: null,

      // API列表
      apiList: [],
      loading: false,
      searchKeyword: '',

      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentApi: null,
      apiLogs: [],

      // 创建/编辑对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,

      // 表单数据
      apiForm: {
        apiName: '',
        apiType: '',
        requestMethod: '',
        endpoint: '',
        description: '',
        authType: 'NONE',
        timeoutSeconds: 30
      },

      // 表单验证规则
      apiRules: {
        apiName: [{ required: true, message: '请输入API名称', trigger: 'blur' }],
        apiType: [{ required: true, message: '请选择API类型', trigger: 'change' }],
        requestMethod: [{ required: true, message: '请选择请求方法', trigger: 'change' }],
        endpoint: [{ required: true, message: '请输入接口地址', trigger: 'blur' }]
      },

      // API测试弹窗
      testDialogVisible: false,
      testLoading: false,
      testForm: { apiId: '', endpoint: '', requestMethod: '' },
      testResult: null,

      // API文档弹窗
      docDialogVisible: false,
      docData: null,

      // API设置弹窗
      settingsDialogVisible: false,
      settingsData: null,
      settingsSaving: false,

      // 监控中心弹窗
      monitorDialogVisible: false,
      monitorData: null,

      // 帮助弹窗
      helpDialogVisible: false,
      helpActiveNames: ['1'],

      // 调用日志弹窗
      logDialogVisible: false,
      logDialogLoading: false,
      logDialogApi: null,
      logDialogData: [],

      // 日志详情弹窗
      logDetailVisible: false,
      logDetailData: null,

      // 单个API监控弹窗
      apiMonitorDialogVisible: false,
      apiMonitorData: null
    }
  },

  computed: {
    filteredApiList() {
      let list = this.apiList
      // 按API类型筛选
      if (this.selectedApiType) {
        list = list.filter(item => item.apiType === this.selectedApiType)
      }
      // 按关键词搜索
      if (this.searchKeyword) {
        const kw = this.searchKeyword.toLowerCase()
        list = list.filter(item =>
          (item.apiName && item.apiName.toLowerCase().includes(kw)) ||
          (item.apiType && item.apiType.toLowerCase().includes(kw)) ||
          (item.endpoint && item.endpoint.toLowerCase().includes(kw)) ||
          (item.status && item.status.toLowerCase().includes(kw))
        )
      }
      return list
    }
  },

  created() {
    this.getApiList()
    this.getApiStats()
    this.getApiTypes()
  },

  methods: {
    // 获取API列表
    async getApiList() {
      this.loading = true
      try {
        const response = await systemIntegrationApi.getApiList()
        if (response.code === 1 && response.data) { this.apiList = response.data }
      } catch (error) {
        this.$message.error('获取API列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getApiStats() {
      try {
        const response = await systemIntegrationApi.getApiStats()
        if (response.code === 1 && response.data) { this.apiStats = response.data }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    // 获取API类型列表
    async getApiTypes() {
      try {
        const response = await systemIntegrationApi.getApiList()
        if (response.code === 1 && response.data) {
          const typeMap = {}
          const iconMap = { 'REST': 'el-icon-connection', 'SOAP': 'el-icon-document', 'GRAPHQL': 'el-icon-data-analysis', 'WEBHOOK': 'el-icon-message', 'GRPC': 'el-icon-cpu' }
          response.data.forEach(item => {
            const t = item.apiType || 'OTHER'
            if (!typeMap[t]) { typeMap[t] = { id: t, name: this.getApiTypeText(t), description: this.getApiTypeDesc(t), icon: iconMap[t] || 'el-icon-link', apiCount: 0 } }
            typeMap[t].apiCount++
          })
          this.apiTypes = Object.values(typeMap)
        }
      } catch (error) { console.error('获取API类型失败：', error) }
    },

    refreshApiTypes() { this.getApiTypes(); this.$message.success('API类型已刷新') },
    handleSelectApiType(apiType) {
      this.selectedApiType = this.selectedApiType === apiType.id ? null : apiType.id
    },
    handleCreateApi() {
      this.dialogTitle = '创建API接口'
      this.apiForm = { apiName: '', apiType: '', requestMethod: '', endpoint: '', description: '', authType: 'NONE', timeoutSeconds: 30 }
      this.dialogVisible = true
    },
    handleRefresh() { this.getApiList(); this.getApiStats(); this.getApiTypes(); this.$message.success('数据已刷新') },

    // ===== 工具栏按钮 =====
    handleTestApi(row) {
      if (row && row.id) {
        this.$confirm('确认测试该API？', '提示', { type: 'warning' }).then(async () => {
          try {
            const response = await systemIntegrationApi.api.test(row.id)
            if (response.code === 1) { this.$message.success('API测试成功') } else { this.$message.error(response.msg || 'API测试失败') }
          } catch (error) { this.$message.error('API测试失败：' + error.message) }
        }).catch(() => {})
      } else {
        // 工具栏点击，打开测试弹窗
        this.testDialogVisible = true
        this.testResult = null
        this.testForm = { apiId: '', endpoint: '', requestMethod: '' }
      }
    },
    onTestApiChange(apiId) {
      const api = this.apiList.find(item => item.id === apiId)
      if (api) {
        this.testForm.endpoint = api.endpoint
        this.testForm.requestMethod = api.requestMethod
      }
    },
    async executeTest() {
      if (!this.testForm.apiId) return
      this.testLoading = true
      this.testResult = null
      try {
        const response = await systemIntegrationApi.api.test(this.testForm.apiId)
        if (response.code === 1) {
          this.testResult = response.data
          this.$message.success('测试完成')
        } else {
          this.testResult = { success: false, message: response.msg }
          this.$message.error(response.msg || '测试失败')
        }
      } catch (error) {
        this.testResult = { success: false, message: error.message }
        this.$message.error('测试失败：' + error.message)
      } finally { this.testLoading = false }
    },

    // API文档
    async handleApiDocs() {
      if (this.currentApi && this.currentApi.id) {
        await this.loadApiDoc(this.currentApi.id)
      } else {
        this.$message.info('请先在列表中选择一个API查看文档')
      }
    },
    async loadApiDoc(apiId) {
      try {
        const response = await systemIntegrationApi.api.getDoc(apiId)
        if (response.code === 1 && response.data) {
          this.docData = response.data
          this.docDialogVisible = true
        } else { this.$message.error(response.msg || '获取文档失败') }
      } catch (error) { this.$message.error('获取文档失败：' + error.message) }
    },

    // API设置
    async handleSettings() {
      try {
        const response = await systemIntegrationApi.api.getSettings()
        if (response.code === 1 && response.data) {
          this.settingsData = response.data
        } else {
          this.settingsData = { defaultTimeout: 30, defaultRetryCount: 3, globalRateLimit: 100, logRetentionDays: 30, totalApis: 0, activeApis: 0, totalCalls: 0, successRate: 0 }
        }
        this.settingsDialogVisible = true
      } catch (error) {
        this.settingsData = { defaultTimeout: 30, defaultRetryCount: 3, globalRateLimit: 100, logRetentionDays: 30 }
        this.settingsDialogVisible = true
      }
    },
    async saveSettings() {
      this.settingsSaving = true
      try {
        const response = await systemIntegrationApi.api.updateSettings(this.settingsData)
        if (response.code === 1) {
          this.$message.success('设置保存成功')
          this.settingsDialogVisible = false
        } else { this.$message.error(response.msg || '保存失败') }
      } catch (error) { this.$message.error('保存失败：' + error.message) }
      finally { this.settingsSaving = false }
    },

    // 监控中心
    async handleMonitor() {
      this.monitorDialogVisible = true
      this.monitorData = null
      await this.loadMonitorCenter()
    },
    async loadMonitorCenter() {
      try {
        // 汇总所有API的监控数据
        const list = this.apiList
        let totalCalls = 0, successCount = 0, failureCount = 0, totalResponseTime = 0, count = 0
        const apiStatusList = []
        list.forEach(item => {
          const sc = item.successCount || 0
          const fc = item.failureCount || 0
          totalCalls += sc + fc
          successCount += sc
          failureCount += fc
          if (item.avgResponseTime) { totalResponseTime += item.avgResponseTime; count++ }
          apiStatusList.push({
            apiName: item.apiName,
            status: item.status,
            callCount: sc + fc,
            avgResponseTime: item.avgResponseTime || 0,
            lastCallTime: item.lastCallTime
          })
        })
        this.monitorData = {
          totalCalls,
          successCount,
          failureCount,
          avgResponseTime: count > 0 ? Math.round(totalResponseTime / count) : 0,
          apiStatusList
        }
      } catch (error) { this.$message.error('加载监控数据失败') }
    },

    // 帮助
    handleHelp() { this.helpDialogVisible = true },

    // ===== 列表操作按钮 =====
    handleView(row) { this.currentApi = row; this.detailDrawerVisible = true; this.detailActiveTab = 'basic'; this.loadDetailLogs(row) },
    handleRowClick(row) { this.currentApi = row },

    handleMoreAction(command, row) {
      const actions = {
        'edit': () => this.handleEdit(row),
        'logs': () => this.openLogDialog(row),
        'monitor': () => this.openApiMonitor(row),
        'docs': () => this.loadApiDoc(row.id),
        'copy': () => this.handleCopy(row),
        'delete': () => this.handleDelete(row)
      }
      if (actions[command]) actions[command]()
    },

    handleEdit(row) {
      this.dialogTitle = '编辑API接口'
      this.apiForm = { ...row, description: row.description || row.remark || '' }
      this.dialogVisible = true
    },

    handleDelete(row) {
      this.$confirm('确认删除该API？此操作不可恢复。', '警告', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.api.delete(row.id)
          if (response.code === 1) { this.$message.success('删除成功'); this.getApiList(); this.getApiStats(); this.getApiTypes() } else { this.$message.error(response.msg || '删除失败') }
        } catch (error) { this.$message.error('删除失败：' + error.message) }
      }).catch(() => {})
    },

    // 复制API
    async handleCopy(row) {
      this.$confirm(`确认复制API "${row.apiName}"？`, '复制确认', { type: 'info' }).then(async () => {
        try {
          const response = await systemIntegrationApi.api.copy(row.id)
          if (response.code === 1) {
            this.$message.success('复制成功')
            this.getApiList()
            this.getApiStats()
            this.getApiTypes()
          } else { this.$message.error(response.msg || '复制失败') }
        } catch (error) { this.$message.error('复制失败：' + error.message) }
      }).catch(() => {})
    },

    // 调用日志弹窗
    async openLogDialog(row) {
      this.logDialogApi = row
      this.logDialogVisible = true
      this.logDialogData = []
      await this.loadApiLogs(row)
    },
    async loadApiLogs(row) {
      if (!row || !row.id) return
      this.logDialogLoading = true
      try {
        const response = await systemIntegrationApi.api.getLogs(row.id)
        if (response.code === 1 && response.data) {
          this.logDialogData = response.data
        } else { this.logDialogData = [] }
      } catch (error) {
        this.logDialogData = []
        console.error('获取调用日志失败：', error)
      } finally { this.logDialogLoading = false }
    },
    handleViewLogDetail(row) {
      this.logDetailData = row
      this.logDetailVisible = true
    },

    // 详情抽屉中的调用日志
    async loadDetailLogs(row) {
      if (!row || !row.id) return
      try {
        const response = await systemIntegrationApi.api.getLogs(row.id)
        if (response.code === 1 && response.data) {
          this.apiLogs = response.data
        } else { this.apiLogs = [] }
      } catch (error) {
        this.apiLogs = []
        console.error('获取详情日志失败：', error)
      }
    },

    // 单个API监控
    async openApiMonitor(row) {
      this.apiMonitorDialogVisible = true
      this.apiMonitorData = null
      try {
        const response = await systemIntegrationApi.api.getMonitor(row.id)
        if (response.code === 1 && response.data) {
          this.apiMonitorData = response.data
        } else {
          // 从列表数据构建
          const sc = row.successCount || 0
          const fc = row.failureCount || 0
          const total = sc + fc
          this.apiMonitorData = {
            apiName: row.apiName,
            apiStatus: row.status,
            totalCalls: total,
            successCount: sc,
            failureCount: fc,
            successRate: total > 0 ? Math.round(sc / total * 100) : 0,
            avgResponseTime: row.avgResponseTime || 0,
            lastCallTime: row.lastCallTime
          }
        }
      } catch (error) {
        this.$message.error('获取监控数据失败')
      }
    },

    // 表单提交（创建/更新）
    handleSubmitForm() {
      this.$refs.apiForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          let response
          const formData = { ...this.apiForm }
          if (formData.id) {
            response = await systemIntegrationApi.api.update(formData.id, formData)
          } else {
            response = await systemIntegrationApi.api.create(formData)
          }
          if (response.code === 1) {
            this.$message.success(formData.id ? '更新成功' : '创建成功')
            this.dialogVisible = false
            this.getApiList()
            this.getApiStats()
            this.getApiTypes()
          } else { this.$message.error(response.msg || '操作失败') }
        } catch (error) { this.$message.error('操作失败：' + error.message) }
        finally { this.submitLoading = false }
      })
    },

    // 表单中测试API
    handleTestApiForm() {
      if (this.apiForm.id) {
        this.handleTestApi(this.apiForm)
      } else {
        this.$message.info('请先保存API后再测试')
      }
    },

    // 对话框关闭重置表单
    handleDialogClose() {
      this.$refs.apiForm && this.$refs.apiForm.resetFields()
    },

    // ===== 工具方法 =====
    formatTime(time) {
      if (!time) return '-'
      if (typeof time === 'string' && time.length >= 16) return time.substring(0, 16).replace('T', ' ')
      try {
        const d = new Date(time)
        if (isNaN(d.getTime())) return time
        const pad = n => String(n).padStart(2, '0')
        return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
      } catch (e) { return time }
    },
    formatDataSize(size) {
      if (!size && size !== 0) return '-'
      if (size < 1024) return size + 'B'
      if (size < 1024 * 1024) return (size / 1024).toFixed(1) + 'KB'
      return (size / (1024 * 1024)).toFixed(1) + 'MB'
    },
    getResponseTimeClass(time) {
      if (!time) return ''
      if (time < 200) return 'response-fast'
      if (time < 1000) return 'response-normal'
      return 'response-slow'
    },
    getStatusCodeColor(code) {
      if (!code) return 'info'
      if (code >= 200 && code < 300) return 'success'
      if (code >= 300 && code < 400) return 'warning'
      return 'danger'
    },
    getApiTypeColor(type) {
      const m = { 'REST': 'primary', 'SOAP': 'success', 'GRAPHQL': 'warning', 'WEBHOOK': 'danger', 'GRPC': '' }
      return m[type] !== undefined ? m[type] : 'info'
    },
    getApiTypeText(type) {
      const m = { 'REST': 'REST API', 'SOAP': 'SOAP API', 'GRAPHQL': 'GraphQL', 'WEBHOOK': 'Webhook', 'GRPC': 'gRPC' }
      return m[type] || type || '未知'
    },
    getApiTypeDesc(type) {
      const m = { 'REST': '基于HTTP的RESTful接口', 'SOAP': '基于XML的Web服务', 'GRAPHQL': 'Facebook查询语言', 'WEBHOOK': '事件驱动回调', 'GRPC': '高性能RPC框架' }
      return m[type] || 'API接口'
    },
    getAuthTypeText(type) {
      const m = { 'NONE': '无认证', 'BASIC': 'Basic认证', 'BEARER': 'Bearer Token', 'OAUTH2': 'OAuth2.0', 'API_KEY': 'API Key' }
      return m[type] || type || '未知'
    },
    getStatusColor(status) {
      const m = { 'ACTIVE': 'success', 'INACTIVE': 'danger', 'PENDING': 'warning' }
      return m[status] || 'info'
    },
    getStatusText(status) {
      const m = { 'ACTIVE': '启用', 'INACTIVE': '停用', 'PENDING': '待启用' }
      return m[status] || status || '未知'
    },
    getMethodColor(method) {
      const m = { 'GET': 'success', 'POST': 'primary', 'PUT': 'warning', 'DELETE': 'danger', 'PATCH': '' }
      return m[method] !== undefined ? m[method] : 'info'
    }
  }
}
</script>

<style scoped>
.response-fast { color: #67C23A; font-weight: bold; }
.response-normal { color: #E6A23C; }
.response-slow { color: #F56C6C; font-weight: bold; }
.test-result-box { margin-top: 12px; }
.empty-tip { text-align: center; padding: 40px 0; }
</style>
