<template>
  <div class="cloud-integration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>云平台集成</h2>
      <p>AWS、Azure、阿里云等云平台服务集成，支持云端数据处理和存储</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateConnection">创建连接</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-connection" @click="handleTestConnection">连接测试</el-button>
            <el-button type="info" icon="el-icon-cloudy" @click="handleCloudServices">云服务</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">云设置</el-button>
            <el-button icon="el-icon-monitor" @click="handleMonitor">监控中心</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 云平台统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ cloudStats.totalConnections }}</div>
            <div class="stat-label">云连接数</div>
            <div class="stat-description">已配置云平台连接</div>
            <div class="stat-trend">
              <i class="el-icon-cloudy"></i>
              <span>云平台</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-cloudy"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card services-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ cloudStats.activeServices }}</div>
            <div class="stat-label">活跃服务</div>
            <div class="stat-description">正在使用的云服务</div>
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
        <el-card class="stat-card data-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ cloudStats.dataTransferred }}GB</div>
            <div class="stat-label">数据传输</div>
            <div class="stat-description">本月数据传输量</div>
            <div class="stat-trend">
              <i class="el-icon-upload"></i>
              <span>数据传输</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-upload"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card cost-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">¥{{ cloudStats.monthlyCost }}</div>
            <div class="stat-label">月度费用</div>
            <div class="stat-description">本月云服务费用</div>
            <div class="stat-trend">
              <i class="el-icon-money"></i>
              <span>成本控制</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-money"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 云平台类型选择 -->
    <el-card class="cloud-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>云平台类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshCloudTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="cloudType in cloudTypes" :key="cloudType.id">
          <el-card 
            class="cloud-type-item" 
            shadow="hover" 
            @click.native="handleSelectCloudType(cloudType)"
            :class="{ 'selected': selectedCloudType === cloudType.id }"
          >
            <div class="cloud-type-icon">
              <i :class="cloudType.icon"></i>
            </div>
            <div class="cloud-type-title">{{ cloudType.name }}</div>
            <div class="cloud-type-description">{{ cloudType.description }}</div>
            <div class="cloud-type-stats">
              <span class="connection-count">{{ cloudType.connectionCount }} 个连接</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 云平台连接列表 -->
    <el-card class="cloud-connections-card" shadow="never">
      <div slot="header" class="card-header">
        <span>云平台连接管理</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索连接"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getCloudConnectionList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="filteredCloudConnectionList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="connectionName" label="连接名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.connectionName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="cloudProvider" label="云平台" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getCloudProviderColor(scope.row.cloudProvider)" size="mini">
              {{ getCloudProviderText(scope.row.cloudProvider) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="region" label="区域" width="120" align="center" />
        <el-table-column prop="serviceType" label="服务类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getServiceTypeColor(scope.row.serviceType)" size="mini">
              {{ getServiceTypeText(scope.row.serviceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataTransferred" label="数据传输" width="100" align="center">
          <template slot-scope="scope">
            <span class="data-transferred">{{ scope.row.dataTransferred }}GB</span>
          </template>
        </el-table-column>
        <el-table-column prop="monthlyCost" label="月度费用" width="100" align="center">
          <template slot-scope="scope">
            <span class="monthly-cost">¥{{ scope.row.monthlyCost }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastSync" label="最后同步" width="150" align="center">
          <template slot-scope="scope">
            <span class="last-sync">{{ scope.row.lastSync }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="连接状态" width="100" align="center">
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
              @click="handleTestConnection(scope.row)"
            >测试</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-refresh"
              @click="handleSync(scope.row)"
              :disabled="scope.row.status !== 'CONNECTED'"
            >同步</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="edit">编辑</el-dropdown-item>
                <el-dropdown-item command="services">云服务</el-dropdown-item>
                <el-dropdown-item command="billing">费用详情</el-dropdown-item>
                <el-dropdown-item command="monitor">监控</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 连接详情抽屉 -->
    <el-drawer
      title="云平台连接详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentConnection">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="连接基本信息" :column="2" border>
              <el-descriptions-item label="连接名称">{{ currentConnection.connectionName }}</el-descriptions-item>
              <el-descriptions-item label="云平台">{{ getCloudProviderText(currentConnection.cloudProvider) }}</el-descriptions-item>
              <el-descriptions-item label="区域">{{ currentConnection.region }}</el-descriptions-item>
              <el-descriptions-item label="服务类型">{{ getServiceTypeText(currentConnection.serviceType) }}</el-descriptions-item>
              <el-descriptions-item label="访问密钥ID">{{ currentConnection.accessKeyId }}</el-descriptions-item>
              <el-descriptions-item label="数据传输">{{ currentConnection.dataTransferred }}GB</el-descriptions-item>
              <el-descriptions-item label="月度费用">¥{{ currentConnection.monthlyCost }}</el-descriptions-item>
              <el-descriptions-item label="连接状态">
                <el-tag :type="getStatusColor(currentConnection.status)" size="mini">
                  {{ getStatusText(currentConnection.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="最后同步">{{ currentConnection.lastSync }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentConnection.createTime }}</el-descriptions-item>
              <el-descriptions-item label="连接描述" :span="2">{{ currentConnection.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="云服务" name="services">
            <el-table :data="cloudServices" border size="mini">
              <el-table-column prop="serviceName" label="服务名称" width="150" />
              <el-table-column prop="serviceType" label="服务类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getCloudServiceTypeColor(scope.row.serviceType)" size="mini">
                    {{ getCloudServiceTypeText(scope.row.serviceType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="resourceCount" label="资源数量" width="100" align="center" />
              <el-table-column prop="usage" label="使用量" width="120" align="center" />
              <el-table-column prop="cost" label="费用" width="100" align="center" />
              <el-table-column prop="lastUpdate" label="最后更新" width="150" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getCloudServiceStatusColor(scope.row.status)" size="mini">
                    {{ getCloudServiceStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="费用详情" name="billing">
            <el-table :data="billingDetails" border size="mini">
              <el-table-column prop="billingDate" label="计费日期" width="120" />
              <el-table-column prop="serviceType" label="服务类型" width="120" />
              <el-table-column prop="usage" label="使用量" width="120" align="center" />
              <el-table-column prop="unitPrice" label="单价" width="100" align="center" />
              <el-table-column prop="amount" label="金额" width="100" align="center" />
              <el-table-column prop="currency" label="币种" width="80" align="center" />
              <el-table-column prop="description" label="描述" />
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="监控数据" name="monitor">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card class="monitor-card">
                  <div slot="header">资源使用趋势</div>
                  <div class="chart-container">
                    <!-- 这里可以集成ECharts图表 -->
                    <div class="chart-placeholder">资源使用趋势图表</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card class="monitor-card">
                  <div slot="header">费用趋势</div>
                  <div class="chart-container">
                    <!-- 这里可以集成ECharts图表 -->
                    <div class="chart-placeholder">费用趋势图表</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑连接对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="connectionForm"
        :model="connectionForm"
        :rules="connectionRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="连接名称" prop="connectionName">
              <el-input v-model="connectionForm.connectionName" placeholder="请输入连接名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="云平台" prop="cloudProvider">
              <el-select v-model="connectionForm.cloudProvider" placeholder="请选择云平台" style="width: 100%">
                <el-option value="AWS" label="Amazon AWS" />
                <el-option value="AZURE" label="Microsoft Azure" />
                <el-option value="ALIYUN" label="阿里云" />
                <el-option value="TENCENT" label="腾讯云" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="区域" prop="region">
              <el-input v-model="connectionForm.region" placeholder="请输入区域" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务类型" prop="serviceType">
              <el-select v-model="connectionForm.serviceType" placeholder="请选择服务类型" style="width: 100%">
                <el-option value="STORAGE" label="存储服务" />
                <el-option value="COMPUTE" label="计算服务" />
                <el-option value="DATABASE" label="数据库服务" />
                <el-option value="ANALYTICS" label="分析服务" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="访问密钥ID" prop="accessKeyId">
              <el-input v-model="connectionForm.accessKeyId" placeholder="请输入访问密钥ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="访问密钥" prop="secretAccessKey">
              <el-input v-model="connectionForm.secretAccessKey" type="password" placeholder="请输入访问密钥" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="连接描述" prop="description">
          <el-input
            v-model="connectionForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入连接描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button @click="handleTestConnectionForm">测试连接</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 云服务弹窗 -->
    <el-dialog title="云服务管理" :visible.sync="cloudServicesDialogVisible" width="900px">
      <el-alert title="当前云平台下的服务概览，按服务类型分类展示连接信息" type="info" :closable="false" style="margin-bottom: 16px" />
      <el-table :data="cloudConnectionList" border size="mini" stripe>
        <el-table-column prop="connectionName" label="连接名称" width="160" show-overflow-tooltip />
        <el-table-column prop="cloudProvider" label="云平台" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getCloudProviderColor(scope.row.cloudProvider)" size="mini">{{ getCloudProviderText(scope.row.cloudProvider) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="serviceType" label="服务类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getServiceTypeColor(scope.row.serviceType)" size="mini">{{ getServiceTypeText(scope.row.serviceType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="region" label="区域" width="120" align="center" />
        <el-table-column prop="endpoint" label="端点地址" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer"><el-button @click="cloudServicesDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 云设置弹窗 -->
    <el-dialog title="云平台设置" :visible.sync="settingsDialogVisible" width="800px">
      <el-alert title="云平台连接的全局配置信息" type="info" :closable="false" style="margin-bottom: 16px" />
      <el-table :data="cloudConnectionList" border size="mini" stripe>
        <el-table-column prop="connectionName" label="连接名称" width="150" show-overflow-tooltip />
        <el-table-column prop="cloudProvider" label="云平台" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getCloudProviderColor(scope.row.cloudProvider)" size="mini">{{ getCloudProviderText(scope.row.cloudProvider) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="accessKeyId" label="访问密钥ID" width="180" show-overflow-tooltip />
        <el-table-column prop="syncMode" label="同步模式" width="100" align="center" />
        <el-table-column prop="isEnabled" label="启用状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'info'" size="mini">{{ scope.row.isEnabled ? '已启用' : '未启用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
      </el-table>
      <div slot="footer"><el-button @click="settingsDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 监控中心弹窗 -->
    <el-dialog title="监控中心" :visible.sync="monitorDialogVisible" width="900px">
      <el-alert title="云平台连接的运行状态和同步监控数据" type="info" :closable="false" style="margin-bottom: 16px" />
      <el-row :gutter="16" style="margin-bottom: 16px">
        <el-col :span="6">
          <el-card shadow="never"><div style="text-align:center"><div style="font-size:24px;font-weight:bold;color:#409EFF">{{ cloudStats.totalConnections }}</div><div style="color:#999;font-size:12px">总连接数</div></div></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="never"><div style="text-align:center"><div style="font-size:24px;font-weight:bold;color:#67C23A">{{ cloudStats.activeServices }}</div><div style="color:#999;font-size:12px">活跃服务</div></div></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="never"><div style="text-align:center"><div style="font-size:24px;font-weight:bold;color:#E6A23C">{{ cloudStats.dataTransferred }}MB</div><div style="color:#999;font-size:12px">数据传输</div></div></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="never"><div style="text-align:center"><div style="font-size:24px;font-weight:bold;color:#F56C6C">¥{{ cloudStats.monthlyCost }}</div><div style="color:#999;font-size:12px">月度费用</div></div></el-card>
        </el-col>
      </el-row>
      <el-table :data="cloudConnectionList" border size="mini" stripe>
        <el-table-column prop="connectionName" label="连接名称" width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="连接状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastSync" label="最后同步" width="160" align="center" />
        <el-table-column prop="dataTransferred" label="数据传输(MB)" width="120" align="center" />
        <el-table-column prop="region" label="区域" width="120" align="center" />
        <el-table-column prop="serviceType" label="服务类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getServiceTypeColor(scope.row.serviceType)" size="mini">{{ getServiceTypeText(scope.row.serviceType) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer"><el-button @click="monitorDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="云平台集成帮助" :visible.sync="helpDialogVisible" width="700px">
      <div style="line-height: 2">
        <h4>功能说明</h4>
        <p>云平台集成管理模块支持对接主流云平台（AWS、Azure、阿里云、腾讯云），实现云端数据的统一管理。</p>
        <h4>主要功能</h4>
        <ul>
          <li><b>创建连接</b>：配置云平台访问凭证，建立与云平台的连接</li>
          <li><b>连接测试</b>：验证云平台连接的可用性和延迟</li>
          <li><b>数据同步</b>：支持手动或自动同步云端数据</li>
          <li><b>云服务</b>：查看和管理云平台下的各类服务</li>
          <li><b>监控中心</b>：实时监控连接状态和数据传输情况</li>
          <li><b>云设置</b>：管理云平台连接的全局配置</li>
        </ul>
        <h4>操作流程</h4>
        <p>1. 点击"创建连接"添加云平台配置 → 2. 测试连接可用性 → 3. 启用数据同步 → 4. 通过监控中心查看运行状态</p>
      </div>
      <div slot="footer"><el-button type="primary" @click="helpDialogVisible = false">知道了</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { systemIntegrationApi } from '@/api/managementAccountant/ncv65/systemIntegration'

export default {
  name: 'CloudIntegration',
  data() {
    return {
      // 统计数据
      cloudStats: {
        totalConnections: 0,
        activeServices: 0,
        dataTransferred: 0,
        monthlyCost: 0
      },

      // 云平台类型
      cloudTypes: [],
      selectedCloudType: null,
      
      // 连接列表
      cloudConnectionList: [],
      loading: false,
      searchKeyword: '',
      
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentConnection: null,
      cloudServices: [],
      billingDetails: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,

      // 功能弹窗
      cloudServicesDialogVisible: false,
      settingsDialogVisible: false,
      monitorDialogVisible: false,
      helpDialogVisible: false,
      
      // 表单数据
      connectionForm: {
        connectionName: '',
        cloudProvider: '',
        region: '',
        serviceType: '',
        accessKeyId: '',
        secretAccessKey: '',
        description: ''
      },
      
      // 表单验证规则
      connectionRules: {
        connectionName: [
          { required: true, message: '请输入连接名称', trigger: 'blur' }
        ],
        cloudProvider: [
          { required: true, message: '请选择云平台', trigger: 'change' }
        ],
        region: [
          { required: true, message: '请输入区域', trigger: 'blur' }
        ],
        serviceType: [
          { required: true, message: '请选择服务类型', trigger: 'change' }
        ],
        accessKeyId: [
          { required: true, message: '请输入访问密钥ID', trigger: 'blur' }
        ],
        secretAccessKey: [
          { required: true, message: '请输入访问密钥', trigger: 'blur' }
        ]
      }
    }
  },
  
  computed: {
    filteredCloudConnectionList() {
      let list = this.cloudConnectionList
      if (this.selectedCloudType) {
        list = list.filter(item => item.cloudType === this.selectedCloudType || item.cloudProvider === this.selectedCloudType)
      }
      if (this.searchKeyword) {
        const kw = this.searchKeyword.toLowerCase()
        list = list.filter(item =>
          (item.connectionName && item.connectionName.toLowerCase().includes(kw)) ||
          (item.cloudType && item.cloudType.toLowerCase().includes(kw)) ||
          (item.region && item.region.toLowerCase().includes(kw)) ||
          (item.status && item.status.toLowerCase().includes(kw))
        )
      }
      return list
    }
  },

  created() {
    this.getCloudConnectionList()
    this.getCloudStats()
    this.getCloudTypes()
  },

  methods: {
    async getCloudConnectionList() {
      this.loading = true
      try {
        const response = await systemIntegrationApi.getCloudConnectionList()
        if (response.code === 1 && response.data) { this.cloudConnectionList = response.data }
      } catch (error) { this.$message.error('获取连接列表失败：' + error.message) } finally { this.loading = false }
    },
    async getCloudStats() {
      try {
        const response = await systemIntegrationApi.getCloudStats()
        if (response.code === 1 && response.data) { this.cloudStats = response.data }
      } catch (error) { console.error('获取统计数据失败：', error) }
    },
    async getCloudTypes() {
      try {
        const response = await systemIntegrationApi.getCloudConnectionList()
        if (response.code === 1 && response.data) {
          const typeMap = {}
          response.data.forEach(item => {
            const t = item.cloudType || item.cloudProvider || 'OTHER'
            if (!typeMap[t]) { typeMap[t] = { id: t, name: this.getCloudTypeText(t), description: t + '云平台', icon: 'el-icon-cloudy', connectionCount: 0 } }
            typeMap[t].connectionCount++
          })
          this.cloudTypes = Object.values(typeMap)
        }
      } catch (error) { console.error('获取云平台类型失败：', error) }
    },
    refreshCloudTypes() { this.getCloudTypes(); this.$message.success('云平台类型已刷新') },
    handleSelectCloudType(cloudType) { this.selectedCloudType = this.selectedCloudType === cloudType.id ? null : cloudType.id; this.getCloudConnectionList() },
    handleCreateConnection() {
      this.dialogTitle = '创建云平台连接'
      this.connectionForm = { connectionName: '', cloudProvider: '', region: '', serviceType: '', accessKeyId: '', secretAccessKey: '', description: '' }
      this.dialogVisible = true
    },
    handleRefresh() { this.getCloudConnectionList(); this.getCloudStats(); this.getCloudTypes(); this.$message.success('数据已刷新') },
    handleTestConnection(row) {
      const target = (row && row.id) ? row : this.currentConnection
      if (target && target.id) {
        this.$confirm('确认测试该连接？', '提示', { type: 'warning' }).then(async () => {
          try {
            const response = await systemIntegrationApi.cloud.test(target.id)
            if (response.code === 1) { this.$message.success('连接测试成功') } else { this.$message.error(response.msg || '连接测试失败') }
          } catch (error) { this.$message.error('连接测试失败：' + error.message) }
        }).catch(() => {})
      } else { this.$message.info('请先在列表中点击选择一个连接后再测试') }
    },
    handleSettings() { this.settingsDialogVisible = true },
    handleHelp() { this.helpDialogVisible = true },
    handleView(row) { this.currentConnection = row; this.detailDrawerVisible = true; this.detailActiveTab = 'basic' },
    handleRowClick(row) { this.currentConnection = row },
    handleMoreAction(command, row) {
      const actions = {
        'edit': () => this.handleEdit(row),
        'services': () => { this.currentConnection = row; this.cloudServicesDialogVisible = true },
        'billing': () => { this.currentConnection = row; this.detailDrawerVisible = true; this.detailActiveTab = 'billing' },
        'monitor': () => { this.currentConnection = row; this.monitorDialogVisible = true },
        'delete': () => this.handleDelete(row)
      }
      if (actions[command]) actions[command]()
    },
    handleEdit(row) { this.dialogTitle = '编辑云平台连接'; this.connectionForm = { ...row }; this.dialogVisible = true },
    handleSync(row) {
      if (row && row.id) {
        this.$confirm('确认同步该连接的数据？', '提示', { type: 'warning' }).then(async () => {
          try {
            const response = await systemIntegrationApi.cloud.sync(row.id)
            if (response.code === 1) { this.$message.success('数据同步成功'); this.getCloudConnectionList(); this.getCloudStats() } else { this.$message.error(response.msg || '同步失败') }
          } catch (error) { this.$message.error('同步失败：' + error.message) }
        }).catch(() => {})
      }
    },
    handleDelete(row) {
      this.$confirm('确认删除该连接？', '警告', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.cloud.delete(row.id)
          if (response.code === 1) { this.$message.success('删除成功'); this.getCloudConnectionList(); this.getCloudStats() } else { this.$message.error(response.msg || '删除失败') }
        } catch (error) { this.$message.error('删除失败：' + error.message) }
      }).catch(() => {})
    },
    handleSubmitForm() {
      this.$refs.connectionForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          let response
          if (this.connectionForm.id) { response = await systemIntegrationApi.cloud.update(this.connectionForm.id, this.connectionForm) } else { response = await systemIntegrationApi.cloud.create(this.connectionForm) }
          if (response.code === 1) { this.$message.success(this.connectionForm.id ? '更新成功' : '创建成功'); this.dialogVisible = false; this.getCloudConnectionList(); this.getCloudStats() } else { this.$message.error(response.msg || '操作失败') }
        } catch (error) { this.$message.error('操作失败：' + error.message) } finally { this.submitLoading = false }
      })
    },
    handleTestConnectionForm() {
      if (this.connectionForm.id) { this.handleTestConnection(this.connectionForm) } else { this.$message.info('请先保存连接后再测试') }
    },
    handleDialogClose() { this.$refs.connectionForm && this.$refs.connectionForm.resetFields() },
    handleCloudServices() { this.cloudServicesDialogVisible = true },
    handleMonitor() { this.monitorDialogVisible = true },
    // 辅助方法
    getCloudTypeColor(type) { const m = { 'AWS': 'primary', 'AZURE': 'success', 'ALIYUN': 'warning', 'TENCENT': 'danger' }; return m[type] || 'info' },
    getCloudTypeText(type) { const m = { 'AWS': 'Amazon AWS', 'AZURE': 'Microsoft Azure', 'ALIYUN': '阿里云', 'TENCENT': '腾讯云' }; return m[type] || type || '未知' },
    getStatusColor(status) { const m = { 'CONNECTED': 'success', 'ACTIVE': 'success', 'DISCONNECTED': 'danger', 'ERROR': 'danger', 'INACTIVE': 'info', 'PENDING': 'warning', 'TESTING': 'warning' }; return m[status] || 'info' },
    getStatusText(status) { const m = { 'CONNECTED': '已连接', 'ACTIVE': '活跃', 'DISCONNECTED': '已断开', 'ERROR': '错误', 'INACTIVE': '未激活', 'PENDING': '待连接', 'TESTING': '测试中' }; return m[status] || status || '未知' },
    getCloudProviderColor(type) { return this.getCloudTypeColor(type) },
    getCloudProviderText(type) { return this.getCloudTypeText(type) },
    getServiceTypeColor(type) { const m = { 'OSS': 'primary', 'STORAGE': 'primary', 'RDS': 'success', 'DATABASE': 'success', 'COMPUTE': 'warning', 'MQ': 'danger', 'FUNCTION': 'info', 'API_GATEWAY': '', 'ANALYTICS': '' }; return m[type] || 'info' },
    getServiceTypeText(type) { const m = { 'OSS': '对象存储', 'STORAGE': '存储服务', 'RDS': '数据库', 'DATABASE': '数据库服务', 'COMPUTE': '计算服务', 'MQ': '消息队列', 'FUNCTION': '函数计算', 'API_GATEWAY': 'API网关', 'ANALYTICS': '分析服务' }; return m[type] || type || '未知' },
    getCloudServiceTypeColor(type) { return this.getServiceTypeColor(type) },
    getCloudServiceTypeText(type) { return this.getServiceTypeText(type) },
    getCloudServiceStatusColor(status) { return this.getStatusColor(status) },
    getCloudServiceStatusText(status) { return this.getStatusText(status) }
  }
}
</script>
