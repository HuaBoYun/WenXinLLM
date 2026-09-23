<template>
  <div class="integration-config">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>集成配置管理</h2>
      <p>管理系统集成的参数配置、连接规则和业务逻辑设置</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateConfig">创建配置</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-connection" @click="handleTestConfig">测试配置</el-button>
            <el-button type="info" icon="el-icon-document" @click="handleImportConfig">导入配置</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleGlobalSettings">全局设置</el-button>
            <el-button icon="el-icon-download" @click="handleExport">导出配置</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 配置统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ configStats.totalConfigs }}</div>
            <div class="stat-label">配置总数</div>
            <div class="stat-description">已创建配置数量</div>
            <div class="stat-trend">
              <i class="el-icon-setting"></i>
              <span>集成配置</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-setting"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ configStats.activeConfigs }}</div>
            <div class="stat-label">活跃配置</div>
            <div class="stat-description">正在使用的配置</div>
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
        <el-card class="stat-card rules-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ configStats.totalRules }}</div>
            <div class="stat-label">规则总数</div>
            <div class="stat-description">已配置业务规则</div>
            <div class="stat-trend">
              <i class="el-icon-document"></i>
              <span>业务规则</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-document"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card success-rate-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ configStats.successRate }}%</div>
            <div class="stat-label">配置成功率</div>
            <div class="stat-description">配置应用成功率</div>
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

    <!-- 配置类型选择 -->
    <el-card class="config-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>配置类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshConfigTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="configType in configTypes" :key="configType.id">
          <el-card 
            class="config-type-item" 
            shadow="hover" 
            @click.native="handleSelectConfigType(configType)"
            :class="{ 'selected': selectedConfigType === configType.id }"
          >
            <div class="config-type-icon">
              <i :class="configType.icon"></i>
            </div>
            <div class="config-type-title">{{ configType.name }}</div>
            <div class="config-type-description">{{ configType.description }}</div>
            <div class="config-type-stats">
              <span class="config-count">{{ configType.configCount }} 个配置</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 配置列表 -->
    <el-card class="config-list-card" shadow="never">
      <div slot="header" class="card-header">
        <span>集成配置管理</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索配置"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getConfigList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="filteredConfigList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="configName" label="配置名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.configName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="configType" label="配置类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getConfigTypeColor(scope.row.configType)" size="mini">
              {{ getConfigTypeText(scope.row.configType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="integrationType" label="集成类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getIntegrationTypeColor(scope.row.integrationType)" size="mini">
              {{ getIntegrationTypeText(scope.row.integrationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="environment" label="环境" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getEnvironmentColor(scope.row.environment)" size="mini">
              {{ getEnvironmentText(scope.row.environment) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="version" label="版本" width="80" align="center" />
        <el-table-column prop="ruleCount" label="规则数量" width="100" align="center">
          <template slot-scope="scope">
            <span class="rule-count">{{ scope.row.ruleCount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastModified" label="最后修改" width="150" align="center">
          <template slot-scope="scope">
            <span class="last-modified">{{ scope.row.lastModified }}</span>
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
              @click="handleTestConfig(scope.row)"
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
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="export">导出</el-dropdown-item>
                <el-dropdown-item command="version">版本管理</el-dropdown-item>
                <el-dropdown-item command="deploy">部署</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 配置详情抽屉 -->
    <el-drawer
      title="集成配置详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="80%"
    >
      <div class="detail-content" v-if="currentConfig">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="配置基本信息" :column="2" border>
              <el-descriptions-item label="配置名称">{{ currentConfig.configName }}</el-descriptions-item>
              <el-descriptions-item label="配置类型">{{ getConfigTypeText(currentConfig.configType) }}</el-descriptions-item>
              <el-descriptions-item label="集成类型">{{ getIntegrationTypeText(currentConfig.integrationType) }}</el-descriptions-item>
              <el-descriptions-item label="环境">{{ getEnvironmentText(currentConfig.environment) }}</el-descriptions-item>
              <el-descriptions-item label="版本">{{ currentConfig.version }}</el-descriptions-item>
              <el-descriptions-item label="规则数量">{{ currentConfig.ruleCount }}</el-descriptions-item>
              <el-descriptions-item label="最后修改">{{ currentConfig.lastModified }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentConfig.status)" size="mini">
                  {{ getStatusText(currentConfig.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentConfig.createTime }}</el-descriptions-item>
              <el-descriptions-item label="配置描述" :span="2">{{ currentConfig.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="参数配置" name="parameters">
            <div class="parameter-config-container">
              <el-button type="primary" size="mini" @click="handleAddParameter" style="margin-bottom: 10px;">
                添加参数
              </el-button>
              <el-table :data="configParameters" border size="mini">
                <el-table-column prop="parameterName" label="参数名称" width="200" />
                <el-table-column prop="parameterType" label="参数类型" width="120" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getParameterTypeColor(scope.row.parameterType)" size="mini">
                      {{ scope.row.parameterType }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="parameterValue" label="参数值" width="200" />
                <el-table-column prop="defaultValue" label="默认值" width="150" />
                <el-table-column prop="required" label="必填" width="80" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.required ? 'danger' : 'info'" size="mini">
                      {{ scope.row.required ? '是' : '否' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="description" label="描述" />
                <el-table-column label="操作" width="120" align="center">
                  <template slot-scope="scope">
                    <el-button type="text" size="mini" @click="handleEditParameter(scope.row)">
                      编辑
                    </el-button>
                    <el-button type="text" size="mini" @click="handleDeleteParameter(scope.row)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="业务规则" name="rules">
            <div class="business-rules-container">
              <el-button type="primary" size="mini" @click="handleAddRule" style="margin-bottom: 10px;">
                添加规则
              </el-button>
              <el-table :data="businessRules" border size="mini">
                <el-table-column prop="ruleName" label="规则名称" width="200" />
                <el-table-column prop="ruleType" label="规则类型" width="120" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getRuleTypeColor(scope.row.ruleType)" size="mini">
                      {{ getRuleTypeText(scope.row.ruleType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="condition" label="条件" width="200" />
                <el-table-column prop="action" label="动作" width="200" />
                <el-table-column prop="priority" label="优先级" width="100" align="center" />
                <el-table-column prop="enabled" label="启用" width="80" align="center">
                  <template slot-scope="scope">
                    <el-switch v-model="scope.row.enabled" @change="handleToggleRule(scope.row)" />
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="120" align="center">
                  <template slot-scope="scope">
                    <el-button type="text" size="mini" @click="handleEditRule(scope.row)">
                      编辑
                    </el-button>
                    <el-button type="text" size="mini" @click="handleDeleteRule(scope.row)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="版本历史" name="versions">
            <el-table :data="configVersions" border size="mini">
              <el-table-column prop="version" label="版本号" width="100" />
              <el-table-column prop="versionName" label="版本名称" width="150" />
              <el-table-column prop="changeLog" label="变更日志" />
              <el-table-column prop="createTime" label="创建时间" width="150" />
              <el-table-column prop="createdBy" label="创建人" width="120" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getVersionStatusColor(scope.row.status)" size="mini">
                    {{ getVersionStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150" align="center">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" @click="handleViewVersion(scope.row)">
                    查看
                  </el-button>
                  <el-button type="text" size="mini" @click="handleRollbackVersion(scope.row)">
                    回滚
                  </el-button>
                  <el-button type="text" size="mini" @click="handleCompareVersion(scope.row)">
                    对比
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑配置对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="configForm"
        :model="configForm"
        :rules="configRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="配置名称" prop="configName">
              <el-input v-model="configForm.configName" placeholder="请输入配置名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="配置类型" prop="configType">
              <el-select v-model="configForm.configType" placeholder="请选择配置类型" style="width: 100%">
                <el-option value="CONNECTION" label="连接配置" />
                <el-option value="MAPPING" label="映射配置" />
                <el-option value="TRANSFORM" label="转换配置" />
                <el-option value="BUSINESS" label="业务配置" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="集成类型" prop="integrationType">
              <el-select v-model="configForm.integrationType" placeholder="请选择集成类型" style="width: 100%">
                <el-option value="ERP" label="ERP集成" />
                <el-option value="API" label="API集成" />
                <el-option value="DATABASE" label="数据库集成" />
                <el-option value="FILE" label="文件集成" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="环境" prop="environment">
              <el-select v-model="configForm.environment" placeholder="请选择环境" style="width: 100%">
                <el-option value="DEV" label="开发环境" />
                <el-option value="TEST" label="测试环境" />
                <el-option value="UAT" label="UAT环境" />
                <el-option value="PROD" label="生产环境" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="配置描述" prop="description">
          <el-input
            v-model="configForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入配置描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button @click="handleTestConfigForm">测试配置</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 全局设置对话框 -->
    <el-dialog title="全局设置" :visible.sync="globalSettingsVisible" width="600px" :close-on-click-modal="false">
      <el-form ref="globalSettingsForm" :model="globalSettingsForm" label-width="140px" size="small" v-loading="globalSettingsLoading">
        <el-form-item label="请求超时时间(ms)">
          <el-input-number v-model="globalSettingsForm.timeout" :min="1000" :max="300000" :step="1000" style="width: 100%" />
        </el-form-item>
        <el-form-item label="重试次数">
          <el-input-number v-model="globalSettingsForm.retryCount" :min="0" :max="10" style="width: 100%" />
        </el-form-item>
        <el-form-item label="重试间隔(ms)">
          <el-input-number v-model="globalSettingsForm.retryInterval" :min="1000" :max="60000" :step="1000" style="width: 100%" />
        </el-form-item>
        <el-form-item label="日志级别">
          <el-select v-model="globalSettingsForm.logLevel" style="width: 100%">
            <el-option value="DEBUG" label="DEBUG" />
            <el-option value="INFO" label="INFO" />
            <el-option value="WARN" label="WARN" />
            <el-option value="ERROR" label="ERROR" />
          </el-select>
        </el-form-item>
        <el-form-item label="最大并发数">
          <el-input-number v-model="globalSettingsForm.maxConcurrent" :min="1" :max="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="启用通知">
          <el-switch v-model="globalSettingsForm.enableNotification" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="globalSettingsVisible = false">取消</el-button>
        <el-button type="primary" @click="saveGlobalSettings" :loading="globalSettingsLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 导入配置对话框 -->
    <el-dialog title="导入配置" :visible.sync="importDialogVisible" width="500px" :close-on-click-modal="false">
      <el-upload
        ref="importUpload"
        drag
        action=""
        :auto-upload="false"
        :on-change="handleImportFileChange"
        :limit="1"
        accept=".json"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将JSON文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">仅支持 .json 格式文件</div>
      </el-upload>
      <div v-if="importPreviewData.length > 0" style="margin-top: 15px;">
        <el-alert :title="'预览：共 ' + importPreviewData.length + ' 条配置'" type="info" :closable="false" />
      </div>
      <div slot="footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitImport" :loading="importLoading" :disabled="importPreviewData.length === 0">确认导入</el-button>
      </div>
    </el-dialog>

    <!-- 测试配置对话框 -->
    <el-dialog title="测试配置" :visible.sync="testDialogVisible" width="600px" :close-on-click-modal="false">
      <div v-if="configList.length === 0">
        <el-empty description="暂无配置可测试" />
      </div>
      <div v-else>
        <el-checkbox-group v-model="testSelectedIds" style="margin-bottom: 15px;">
          <el-checkbox v-for="item in configList" :key="item.id" :label="item.id" style="display: block; margin-bottom: 8px;">
            {{ item.configName }} ({{ getConfigTypeText(item.configType) }})
          </el-checkbox>
        </el-checkbox-group>
        <el-divider v-if="testResults.length > 0" />
        <el-table v-if="testResults.length > 0" :data="testResults" border size="mini">
          <el-table-column prop="configName" label="配置名称" />
          <el-table-column prop="success" label="结果" width="80" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.success ? 'success' : 'danger'" size="mini">{{ scope.row.success ? '通过' : '失败' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="message" label="说明" />
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="submitBatchTest" :loading="testLoading" :disabled="testSelectedIds.length === 0">开始测试</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="集成配置管理 - 帮助" :visible.sync="helpDialogVisible" width="650px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>集成配置管理用于统一管理系统集成的参数配置、连接规则和业务逻辑设置。</p>
        <h4>操作指南</h4>
        <el-collapse>
          <el-collapse-item title="创建配置" name="1">
            <p>点击"创建配置"按钮，填写配置名称、配置类型、集成类型、环境和描述信息，点击确定即可创建新的集成配置。</p>
          </el-collapse-item>
          <el-collapse-item title="测试配置" name="2">
            <p>选择需要测试的配置，点击"测试配置"按钮进行连接测试，系统将验证配置的有效性并返回测试结果。</p>
          </el-collapse-item>
          <el-collapse-item title="导入/导出配置" name="3">
            <p>支持JSON格式的配置导入导出。导出时将当前所有配置导出为JSON文件；导入时选择JSON文件即可批量导入配置。</p>
          </el-collapse-item>
          <el-collapse-item title="全局设置" name="4">
            <p>全局设置用于管理所有集成配置的公共参数，包括请求超时时间、重试策略、日志级别、最大并发数等。</p>
          </el-collapse-item>
          <el-collapse-item title="配置类型筛选" name="5">
            <p>点击配置类型卡片可按类型筛选配置列表，再次点击取消筛选。</p>
          </el-collapse-item>
        </el-collapse>
        <h4 style="margin-top: 15px;">配置类型说明</h4>
        <el-table :data="helpConfigTypes" border size="mini" style="margin-top: 10px;">
          <el-table-column prop="type" label="类型" width="120" />
          <el-table-column prop="desc" label="说明" />
        </el-table>
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
  name: 'IntegrationConfig',
  data() {
    return {
      // 统计数据
      configStats: {
        totalConfigs: 0,
        activeConfigs: 0,
        totalRules: 0,
        successRate: 0
      },
      // 配置类型
      configTypes: [],
      selectedConfigType: null,
      // 配置列表
      configList: [],
      loading: false,
      searchKeyword: '',
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentConfig: null,
      configParameters: [],
      businessRules: [],
      configVersions: [],
      // 创建/编辑对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      configForm: {
        configName: '',
        configType: '',
        integrationType: '',
        environment: '',
        description: ''
      },
      configRules: {
        configName: [{ required: true, message: '请输入配置名称', trigger: 'blur' }],
        configType: [{ required: true, message: '请选择配置类型', trigger: 'change' }],
        integrationType: [{ required: true, message: '请选择集成类型', trigger: 'change' }],
        environment: [{ required: true, message: '请选择环境', trigger: 'change' }]
      },
      // 全局设置对话框
      globalSettingsVisible: false,
      globalSettingsLoading: false,
      globalSettingsForm: {
        timeout: 30000,
        retryCount: 3,
        retryInterval: 5000,
        logLevel: 'INFO',
        maxConcurrent: 10,
        enableNotification: true
      },
      // 导入配置对话框
      importDialogVisible: false,
      importLoading: false,
      importPreviewData: [],
      // 测试配置对话框
      testDialogVisible: false,
      testLoading: false,
      testSelectedIds: [],
      testResults: [],
      // 帮助对话框
      helpDialogVisible: false,
      helpConfigTypes: [
        { type: '连接配置', desc: '管理系统间的连接参数，如URL、端口、认证信息等' },
        { type: '映射配置', desc: '管理数据字段映射关系，定义源系统与目标系统的字段对应' },
        { type: '转换配置', desc: '管理数据转换规则，如格式转换、编码转换、单位换算等' },
        { type: '业务配置', desc: '管理业务逻辑规则，如审批流程、数据校验、触发条件等' }
      ]
    }
  },

  computed: {
    filteredConfigList() {
      let list = this.configList
      // 按配置类型筛选
      if (this.selectedConfigType) {
        list = list.filter(item => item.configType === this.selectedConfigType)
      }
      // 按关键字搜索
      if (this.searchKeyword) {
        const kw = this.searchKeyword.toLowerCase()
        list = list.filter(item =>
          (item.configName && item.configName.toLowerCase().includes(kw)) ||
          (item.configType && item.configType.toLowerCase().includes(kw)) ||
          (item.integrationType && item.integrationType.toLowerCase().includes(kw)) ||
          (item.status && item.status.toLowerCase().includes(kw))
        )
      }
      return list
    }
  },

  created() {
    this.getConfigList()
    this.getConfigStats()
    this.getConfigTypes()
  },

  methods: {
    // ==================== 数据获取 ====================
    async getConfigList() {
      this.loading = true
      try {
        const response = await systemIntegrationApi.getConfigList()
        const codeOk = response.code === 1 || String(response.code) === '1'
        if (codeOk && response.data) {
          this.configList = response.data
        }
      } catch (error) {
        console.error('获取配置列表失败：', error)
        this.$message.error('获取配置列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    async getConfigStats() {
      try {
        const response = await systemIntegrationApi.getConfigStats()
        const codeOk = response.code === 1 || String(response.code) === '1'
        if (codeOk && response.data) {
          this.configStats = response.data
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },
    async getConfigTypes() {
      try {
        const response = await systemIntegrationApi.getConfigList()
        const codeOk = response.code === 1 || String(response.code) === '1'
        if (codeOk && response.data) {
          const iconMap = { 'CONNECTION': 'el-icon-connection', 'MAPPING': 'el-icon-sort', 'TRANSFORM': 'el-icon-refresh', 'BUSINESS': 'el-icon-office-building' }
          const typeMap = {}
          response.data.forEach(item => {
            const t = item.configType || 'OTHER'
            if (!typeMap[t]) {
              typeMap[t] = { id: t, name: this.getConfigTypeText(t), description: this.getConfigTypeText(t), icon: iconMap[t] || 'el-icon-setting', configCount: 0 }
            }
            typeMap[t].configCount++
          })
          this.configTypes = Object.values(typeMap)
        }
      } catch (error) {
        console.error('获取配置类型失败：', error)
      }
    },
    refreshConfigTypes() {
      this.getConfigTypes()
      this.$message.success('配置类型已刷新')
    },
    // ==================== 配置类型筛选 ====================
    handleSelectConfigType(configType) {
      this.selectedConfigType = this.selectedConfigType === configType.id ? null : configType.id
    },
    // ==================== 工具栏操作 ====================
    handleCreateConfig() {
      this.dialogTitle = '创建集成配置'
      this.configForm = { configName: '', configType: '', integrationType: '', environment: '', description: '' }
      this.dialogVisible = true
    },
    handleRefresh() {
      this.getConfigList()
      this.getConfigStats()
      this.getConfigTypes()
      this.$message.success('数据已刷新')
    },
    handleTestConfig(row) {
      if (row && row.id) {
        this.$confirm('确认测试该配置？', '提示', { type: 'warning' }).then(async () => {
          try {
            const response = await systemIntegrationApi.config.test(row.id)
            if (response.code === 1 && response.data) {
              const r = response.data
              this.$alert(
                '<p>配置名称：' + (row.configName || '-') + '</p><p>测试结果：' + (r.success ? '<span style="color:#67C23A">通过</span>' : '<span style="color:#F56C6C">失败</span>') + '</p><p>说明：' + (r.message || '-') + '</p>',
                '测试结果', { dangerouslyUseHTMLString: true, confirmButtonText: '关闭' }
              )
            } else {
              this.$message.error(response.msg || '配置测试失败')
            }
          } catch (error) {
            this.$message.error('配置测试失败：' + error.message)
          }
        }).catch(() => {})
      } else {
        // 工具栏按钮点击，打开批量测试弹窗
        this.testSelectedIds = []
        this.testResults = []
        this.testDialogVisible = true
      }
    },
    handleTestConfigForm() {
      if (this.configForm.id) {
        this.handleTestConfig(this.configForm)
      } else {
        this.$message.info('请先保存配置后再测试')
      }
    },
    async submitBatchTest() {
      if (this.testSelectedIds.length === 0) {
        this.$message.warning('请选择要测试的配置')
        return
      }
      this.testLoading = true
      try {
        const response = await systemIntegrationApi.config.batchTest(this.testSelectedIds)
        if (response.code === 1 && response.data) {
          this.testResults = response.data
          this.$message.success('批量测试完成')
        } else {
          this.$message.error(response.msg || '批量测试失败')
        }
      } catch (error) {
        this.$message.error('批量测试失败：' + error.message)
      } finally {
        this.testLoading = false
      }
    },
    handleImportConfig() {
      this.importPreviewData = []
      this.importDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.importUpload) {
          this.$refs.importUpload.clearFiles()
        }
      })
    },
    handleImportFileChange(file) {
      const reader = new FileReader()
      reader.onload = (event) => {
        try {
          const data = JSON.parse(event.target.result)
          this.importPreviewData = Array.isArray(data) ? data : [data]
        } catch (err) {
          this.$message.error('文件解析失败，请确保文件格式正确')
          this.importPreviewData = []
        }
      }
      reader.readAsText(file.raw)
    },
    async submitImport() {
      if (this.importPreviewData.length === 0) return
      this.importLoading = true
      try {
        const response = await systemIntegrationApi.config.importConfig(this.importPreviewData)
        if (response.code === 1) {
          this.$message.success(response.msg || '导入成功')
          this.importDialogVisible = false
          this.getConfigList()
          this.getConfigStats()
          this.getConfigTypes()
        } else {
          this.$message.error(response.msg || '导入失败')
        }
      } catch (error) {
        this.$message.error('导入失败：' + error.message)
      } finally {
        this.importLoading = false
      }
    },
    async handleGlobalSettings() {
      this.globalSettingsVisible = true
      this.globalSettingsLoading = true
      try {
        const response = await systemIntegrationApi.config.getGlobalSettings()
        if (response.code === 1 && response.data) {
          const d = response.data
          this.globalSettingsForm = {
            timeout: Number(d.timeout) || 30000,
            retryCount: Number(d.retryCount) || 3,
            retryInterval: Number(d.retryInterval) || 5000,
            logLevel: d.logLevel || 'INFO',
            maxConcurrent: Number(d.maxConcurrent) || 10,
            enableNotification: d.enableNotification === true || d.enableNotification === 'true'
          }
        }
      } catch (error) {
        console.error('获取全局设置失败：', error)
      } finally {
        this.globalSettingsLoading = false
      }
    },
    async saveGlobalSettings() {
      this.globalSettingsLoading = true
      try {
        const response = await systemIntegrationApi.config.saveGlobalSettings(this.globalSettingsForm)
        if (response.code === 1) {
          this.$message.success('全局设置保存成功')
          this.globalSettingsVisible = false
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.globalSettingsLoading = false
      }
    },
    async handleExport() {
      if (!this.configList || this.configList.length === 0) {
        this.$message.info('暂无数据可导出')
        return
      }
      try {
        const response = await systemIntegrationApi.config.exportConfig()
        if (response.code === 1 && response.data) {
          const dataStr = JSON.stringify(response.data, null, 2)
          const blob = new Blob([dataStr], { type: 'application/json' })
          const url = URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = '集成配置_' + new Date().toISOString().slice(0, 10) + '.json'
          a.click()
          URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    handleHelp() {
      this.helpDialogVisible = true
    },
    // ==================== 列表操作 ====================
    handleView(row) {
      this.currentConfig = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      this.loadConfigDetail(row.id)
    },
    handleRowClick(row) {
      this.currentConfig = row
    },
    handleMoreAction(command, row) {
      const actions = {
        'copy': () => this.handleClone(row),
        'export': () => this.handleExportSingle(row),
        'version': () => this.handleVersionManage(row),
        'deploy': () => this.handleDeploy(row),
        'delete': () => this.handleDelete(row)
      }
      if (actions[command]) actions[command]()
    },
    handleEdit(row) {
      this.dialogTitle = '编辑集成配置'
      this.configForm = { ...row }
      this.dialogVisible = true
    },
    handleClone(row) {
      if (row && row.id) {
        this.$confirm('确认复制该配置？', '提示', { type: 'info' }).then(async () => {
          try {
            const cloneData = { ...row }
            delete cloneData.id
            cloneData.configName = row.configName + '_副本'
            const response = await systemIntegrationApi.config.create(cloneData)
            if (response.code === 1) {
              this.$message.success('复制成功')
              this.getConfigList()
              this.getConfigStats()
              this.getConfigTypes()
            } else {
              this.$message.error(response.msg || '复制失败')
            }
          } catch (error) {
            this.$message.error('复制失败：' + error.message)
          }
        }).catch(() => {})
      }
    },
    handleExportSingle(row) {
      const dataStr = JSON.stringify(row, null, 2)
      const blob = new Blob([dataStr], { type: 'application/json' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = (row.configName || '配置') + '.json'
      a.click()
      URL.revokeObjectURL(url)
      this.$message.success('导出成功')
    },
    handleVersionManage(row) {
      this.currentConfig = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'versions'
      this.loadConfigDetail(row.id)
    },
    async handleDeploy(row) {
      try {
        await this.$confirm('确认部署配置 "' + row.configName + '" ？', '部署确认', { type: 'warning' })
        const response = await systemIntegrationApi.config.apply(row.id)
        if (response.code === 1) {
          this.$message.success('部署成功')
          this.getConfigList()
        } else {
          this.$message.error(response.msg || '部署失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('部署失败：' + (error.message || ''))
        }
      }
    },
    handleDelete(row) {
      this.$confirm('确认删除配置 "' + row.configName + '" ？此操作不可恢复。', '警告', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.config.delete(row.id)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getConfigList()
            this.getConfigStats()
            this.getConfigTypes()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      }).catch(() => {})
    },
    // ==================== 表单提交 ====================
    handleSubmitForm() {
      this.$refs.configForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          let response
          if (this.configForm.id) {
            response = await systemIntegrationApi.config.update(this.configForm.id, this.configForm)
          } else {
            response = await systemIntegrationApi.config.create(this.configForm)
          }
          if (response.code === 1) {
            this.$message.success(this.configForm.id ? '更新成功' : '创建成功')
            this.dialogVisible = false
            this.getConfigList()
            this.getConfigStats()
            this.getConfigTypes()
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败：' + error.message)
        } finally {
          this.submitLoading = false
        }
      })
    },
    handleDialogClose() {
      this.$refs.configForm && this.$refs.configForm.resetFields()
    },
    // ==================== 详情数据加载 ====================
    async loadConfigDetail(configId) {
      this.configParameters = []
      this.businessRules = []
      this.configVersions = []
      try {
        const [paramRes, ruleRes, versionRes] = await Promise.all([
          systemIntegrationApi.config.getParams(configId),
          systemIntegrationApi.config.getRules(configId),
          systemIntegrationApi.config.getVersions(configId)
        ])
        if (paramRes.code === 1 && paramRes.data) {
          this.configParameters = paramRes.data
        }
        if (ruleRes.code === 1 && ruleRes.data) {
          this.businessRules = ruleRes.data
        }
        if (versionRes.code === 1 && versionRes.data) {
          this.configVersions = versionRes.data
        }
      } catch (error) {
        console.error('加载配置详情失败：', error)
      }
    },
    // ==================== 参数配置方法 ====================
    handleAddParameter() {
      this.$prompt('请输入参数名称', '添加参数', { confirmButtonText: '确定', cancelButtonText: '取消' }).then(async ({ value }) => {
        if (value && this.currentConfig) {
          try {
            const response = await systemIntegrationApi.config.addParam(this.currentConfig.id, {
              parameterName: value, parameterType: 'STRING', parameterValue: '', defaultValue: '', required: false, description: ''
            })
            if (response.code === 1) {
              this.$message.success('参数已添加')
              this.loadConfigDetail(this.currentConfig.id)
            } else {
              this.$message.error(response.msg || '添加失败')
            }
          } catch (error) {
            this.$message.error('添加参数失败：' + error.message)
          }
        }
      }).catch(() => {})
    },
    handleEditParameter(row) {
      this.$prompt('编辑参数值', '编辑参数: ' + row.parameterName, { confirmButtonText: '确定', cancelButtonText: '取消', inputValue: row.parameterValue }).then(async ({ value }) => {
        try {
          const response = await systemIntegrationApi.config.updateParam(this.currentConfig.id, row.id, { parameterValue: value })
          if (response.code === 1) {
            this.$message.success('参数已更新')
            this.loadConfigDetail(this.currentConfig.id)
          } else {
            this.$message.error(response.msg || '更新失败')
          }
        } catch (error) {
          this.$message.error('更新参数失败：' + error.message)
        }
      }).catch(() => {})
    },
    handleDeleteParameter(row) {
      this.$confirm('确认删除参数: ' + row.parameterName + '？', '警告', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.config.deleteParam(this.currentConfig.id, row.id)
          if (response.code === 1) {
            this.$message.success('参数已删除')
            this.loadConfigDetail(this.currentConfig.id)
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除参数失败：' + error.message)
        }
      }).catch(() => {})
    },
    // ==================== 业务规则方法 ====================
    handleAddRule() {
      this.$prompt('请输入规则名称', '添加规则', { confirmButtonText: '确定', cancelButtonText: '取消' }).then(async ({ value }) => {
        if (value && this.currentConfig) {
          try {
            const response = await systemIntegrationApi.config.addRule(this.currentConfig.id, {
              ruleName: value, ruleType: 'VALIDATION', condition: '', action: '', priority: 1, enabled: true, description: ''
            })
            if (response.code === 1) {
              this.$message.success('规则已添加')
              this.loadConfigDetail(this.currentConfig.id)
            } else {
              this.$message.error(response.msg || '添加失败')
            }
          } catch (error) {
            this.$message.error('添加规则失败：' + error.message)
          }
        }
      }).catch(() => {})
    },
    handleEditRule(row) {
      this.$prompt('编辑规则条件', '编辑规则: ' + row.ruleName, { confirmButtonText: '确定', cancelButtonText: '取消', inputValue: row.condition }).then(async ({ value }) => {
        try {
          const response = await systemIntegrationApi.config.updateRule(this.currentConfig.id, row.id, { condition: value })
          if (response.code === 1) {
            this.$message.success('规则已更新')
            this.loadConfigDetail(this.currentConfig.id)
          } else {
            this.$message.error(response.msg || '更新失败')
          }
        } catch (error) {
          this.$message.error('更新规则失败：' + error.message)
        }
      }).catch(() => {})
    },
    handleDeleteRule(row) {
      this.$confirm('确认删除规则: ' + row.ruleName + '？', '警告', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.config.deleteRule(this.currentConfig.id, row.id)
          if (response.code === 1) {
            this.$message.success('规则已删除')
            this.loadConfigDetail(this.currentConfig.id)
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除规则失败：' + error.message)
        }
      }).catch(() => {})
    },
    async handleToggleRule(row) {
      try {
        const response = await systemIntegrationApi.config.updateRule(this.currentConfig.id, row.id, { enabled: row.enabled })
        if (response.code === 1) {
          this.$message.success(row.ruleName + (row.enabled ? ' 已启用' : ' 已停用'))
        } else {
          this.$message.error(response.msg || '操作失败')
          row.enabled = !row.enabled
        }
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
        row.enabled = !row.enabled
      }
    },
    // ==================== 版本管理方法 ====================
    handleViewVersion(row) {
      this.$alert(
        '<div><p>版本: ' + row.version + '</p><p>修改人: ' + (row.modifier || '系统') + '</p><p>修改时间: ' + (row.modifyTime || '-') + '</p><p>说明: ' + (row.description || '无') + '</p></div>',
        '版本详情', { dangerouslyUseHTMLString: true, confirmButtonText: '关闭' }
      )
    },
    async handleRollbackVersion(row) {
      try {
        await this.$confirm('确认回滚到版本 ' + row.version + '？此操作不可撤销。', '警告', { type: 'warning' })
        const response = await systemIntegrationApi.config.rollback(this.currentConfig.id, row.version)
        if (response.code === 1) {
          this.$message.success('已回滚到版本: ' + row.version)
          this.getConfigList()
        } else {
          this.$message.error(response.msg || '版本回滚失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('版本回滚失败：' + (error.message || ''))
        }
      }
    },
    async handleCompareVersion(row) {
      try {
        const response = await systemIntegrationApi.config.compare(this.currentConfig.id, row.version)
        if (response.code === 1 && response.data) {
          const diff = response.data
          const html = '<div style="max-height:400px;overflow:auto"><p><b>版本:</b> ' + row.version + ' vs 当前版本</p><pre>' + JSON.stringify(diff, null, 2) + '</pre></div>'
          this.$alert(html, '版本对比', { dangerouslyUseHTMLString: true, confirmButtonText: '关闭' })
        } else {
          this.$message.error(response.msg || '获取版本对比数据失败')
        }
      } catch (error) {
        this.$message.error('版本对比失败：' + error.message)
      }
    },
    // ==================== 辅助方法 ====================
    getConfigTypeColor(type) {
      const m = { 'CONNECTION': 'primary', 'MAPPING': 'success', 'TRANSFORM': 'warning', 'BUSINESS': 'danger' }
      return m[type] || 'info'
    },
    getConfigTypeText(type) {
      const m = { 'CONNECTION': '连接配置', 'MAPPING': '映射配置', 'TRANSFORM': '转换配置', 'BUSINESS': '业务配置' }
      return m[type] || type || '未知'
    },
    getIntegrationTypeColor(type) {
      const m = { 'ERP': 'primary', 'API': 'success', 'DATABASE': 'warning', 'FILE': 'info' }
      return m[type] || 'info'
    },
    getIntegrationTypeText(type) {
      const m = { 'ERP': 'ERP集成', 'API': 'API集成', 'DATABASE': '数据库集成', 'FILE': '文件集成' }
      return m[type] || type || '未知'
    },
    getEnvironmentColor(env) {
      const m = { 'DEV': 'info', 'TEST': 'warning', 'UAT': '', 'PROD': 'danger' }
      return m[env] || 'info'
    },
    getEnvironmentText(env) {
      const m = { 'DEV': '开发', 'TEST': '测试', 'UAT': 'UAT', 'PROD': '生产' }
      return m[env] || env || '未知'
    },
    getStatusColor(status) {
      const m = { 'ACTIVE': 'success', 'INACTIVE': 'danger', 'PENDING': 'warning' }
      return m[status] || 'info'
    },
    getStatusText(status) {
      const m = { 'ACTIVE': '启用', 'INACTIVE': '停用', 'PENDING': '待启用' }
      return m[status] || status || '未知'
    },
    getParameterTypeColor(type) {
      const m = { 'STRING': 'primary', 'NUMBER': 'success', 'BOOLEAN': 'warning', 'JSON': 'danger' }
      return m[type] || 'info'
    },
    getRuleTypeColor(type) {
      const m = { 'VALIDATION': 'primary', 'TRANSFORM': 'success', 'FILTER': 'warning', 'TRIGGER': 'danger' }
      return m[type] || 'info'
    },
    getRuleTypeText(type) {
      const m = { 'VALIDATION': '验证规则', 'TRANSFORM': '转换规则', 'FILTER': '过滤规则', 'TRIGGER': '触发规则' }
      return m[type] || type || '未知'
    },
    getVersionStatusColor(status) {
      const m = { 'CURRENT': 'success', 'ARCHIVED': 'info', 'DEPRECATED': 'danger' }
      return m[status] || 'info'
    },
    getVersionStatusText(status) {
      const m = { 'CURRENT': '当前版本', 'ARCHIVED': '已归档', 'DEPRECATED': '已废弃' }
      return m[status] || status || '未知'
    }
  }
}
</script>

