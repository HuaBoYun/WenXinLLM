<template>
  <div class="web-service-integration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>Web服务集成</h2>
      <p>SOAP、REST Web服务集成，支持企业级服务调用和数据交换</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateService">创建服务</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-connection" @click="handleTestService">服务测试</el-button>
            <el-button type="info" icon="el-icon-document" @click="handleWsdl">WSDL管理</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">服务设置</el-button>
            <el-button icon="el-icon-monitor" @click="handleMonitor">监控中心</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- Web服务统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ wsStats.totalServices }}</div>
            <div class="stat-label">服务总数</div>
            <div class="stat-description">已配置Web服务数</div>
            <div class="stat-trend">
              <i class="el-icon-service"></i>
              <span>Web服务</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-service"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ wsStats.activeServices }}</div>
            <div class="stat-label">活跃服务</div>
            <div class="stat-description">正在运行的服务</div>
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
            <div class="stat-number">{{ wsStats.todayCalls }}</div>
            <div class="stat-label">今日调用</div>
            <div class="stat-description">今日服务调用次数</div>
            <div class="stat-trend">
              <i class="el-icon-refresh"></i>
              <span>服务调用</span>
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
            <div class="stat-number">{{ wsStats.successRate }}%</div>
            <div class="stat-label">成功率</div>
            <div class="stat-description">服务调用成功率</div>
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

    <!-- Web服务类型选择 -->
    <el-card class="ws-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>Web服务类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshWsTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="8" v-for="wsType in wsTypes" :key="wsType.id">
          <el-card 
            class="ws-type-item" 
            shadow="hover" 
            @click.native="handleSelectWsType(wsType)"
            :class="{ 'selected': selectedWsType === wsType.id }"
          >
            <div class="ws-type-icon">
              <i :class="wsType.icon"></i>
            </div>
            <div class="ws-type-title">{{ wsType.name }}</div>
            <div class="ws-type-description">{{ wsType.description }}</div>
            <div class="ws-type-stats">
              <span class="service-count">{{ wsType.serviceCount }} 个服务</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- Web服务列表 -->
    <el-card class="ws-list-card" shadow="never">
      <div slot="header" class="card-header">
        <span>Web服务管理</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索服务"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getWsList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="filteredWsList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="serviceName" label="服务名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.serviceName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="serviceType" label="服务类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getServiceTypeColor(scope.row.serviceType)" size="mini">
              {{ scope.row.serviceType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="endpoint" label="服务端点" width="300" show-overflow-tooltip />
        <el-table-column prop="version" label="版本" width="80" align="center" />
        <el-table-column prop="callCount" label="调用次数" width="100" align="center">
          <template slot-scope="scope">
            <span class="call-count">{{ scope.row.callCount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="avgResponseTime" label="平均响应时间" width="120" align="center">
          <template slot-scope="scope">
            <span class="response-time">{{ scope.row.avgResponseTime }}ms</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastCall" label="最后调用" width="150" align="center">
          <template slot-scope="scope">
            <span class="last-call">{{ scope.row.lastCall }}</span>
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
              @click="handleTestService(scope.row)"
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
                <el-dropdown-item command="wsdl">WSDL</el-dropdown-item>
                <el-dropdown-item command="logs">调用日志</el-dropdown-item>
                <el-dropdown-item command="monitor">监控</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 服务详情抽屉 -->
    <el-drawer
      title="Web服务详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentService">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="服务基本信息" :column="2" border>
              <el-descriptions-item label="服务名称">{{ currentService.serviceName }}</el-descriptions-item>
              <el-descriptions-item label="服务类型">{{ currentService.serviceType }}</el-descriptions-item>
              <el-descriptions-item label="服务端点">{{ currentService.endpoint }}</el-descriptions-item>
              <el-descriptions-item label="版本">{{ currentService.version }}</el-descriptions-item>
              <el-descriptions-item label="调用次数">{{ currentService.callCount }}</el-descriptions-item>
              <el-descriptions-item label="平均响应时间">{{ currentService.avgResponseTime }}ms</el-descriptions-item>
              <el-descriptions-item label="最后调用">{{ currentService.lastCall }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentService.status)" size="mini">
                  {{ getStatusText(currentService.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentService.createTime }}</el-descriptions-item>
              <el-descriptions-item label="服务描述" :span="2">{{ currentService.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="服务配置" name="config">
            <el-form label-width="120px" size="small">
              <el-form-item label="WSDL地址">
                <el-input :value="currentService.wsdlUrl" readonly />
              </el-form-item>
              <el-form-item label="命名空间">
                <el-input :value="currentService.namespace" readonly />
              </el-form-item>
              <el-form-item label="认证方式">
                <el-input :value="currentService.authType" readonly />
              </el-form-item>
              <el-form-item label="超时设置">
                <el-input :value="currentService.timeout + 'ms'" readonly />
              </el-form-item>
              <el-form-item label="重试次数">
                <el-input :value="currentService.retryCount" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="服务方法" name="methods">
            <el-table :data="serviceMethods" border size="mini">
              <el-table-column prop="methodName" label="方法名称" width="200" />
              <el-table-column prop="operation" label="操作" width="150" />
              <el-table-column prop="inputMessage" label="输入消息" />
              <el-table-column prop="outputMessage" label="输出消息" />
              <el-table-column prop="callCount" label="调用次数" width="100" align="center" />
              <el-table-column label="操作" width="120" align="center">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" @click="handleTestMethod(scope.row)">
                    测试
                  </el-button>
                  <el-button type="text" size="mini" @click="handleViewMethod(scope.row)">
                    详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="调用日志" name="logs">
            <el-table :data="serviceLogs" border size="mini">
              <el-table-column prop="callTime" label="调用时间" width="150" />
              <el-table-column prop="methodName" label="方法名称" width="150" />
              <el-table-column prop="requestId" label="请求ID" width="200" />
              <el-table-column prop="responseTime" label="响应时间" width="100" align="center">
                <template slot-scope="scope">
                  <span :class="getResponseTimeClass(scope.row.responseTime)">
                    {{ scope.row.responseTime }}ms
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="statusCode" label="状态码" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusCodeColor(scope.row.statusCode)" size="mini">
                    {{ scope.row.statusCode }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="errorMessage" label="错误信息" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑服务对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="serviceForm"
        :model="serviceForm"
        :rules="serviceRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="服务名称" prop="serviceName">
              <el-input v-model="serviceForm.serviceName" placeholder="请输入服务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务类型" prop="serviceType">
              <el-select v-model="serviceForm.serviceType" placeholder="请选择服务类型" style="width: 100%">
                <el-option value="SOAP" label="SOAP服务" />
                <el-option value="REST" label="REST服务" />
                <el-option value="XML_RPC" label="XML-RPC" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="服务端点" prop="endpoint">
          <el-input v-model="serviceForm.endpoint" placeholder="请输入服务端点URL" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="WSDL地址" prop="wsdlUrl">
              <el-input v-model="serviceForm.wsdlUrl" placeholder="WSDL地址(SOAP服务)" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="版本" prop="version">
              <el-input v-model="serviceForm.version" placeholder="服务版本" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="服务描述" prop="description">
          <el-input
            v-model="serviceForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入服务描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button @click="handleTestServiceForm">测试服务</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 服务测试弹窗 -->
    <el-dialog title="服务测试" :visible.sync="testDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="100px" size="small">
        <el-form-item label="选择服务">
          <el-select v-model="testServiceId" placeholder="请选择要测试的服务" style="width: 100%">
            <el-option v-for="item in wsList" :key="item.id" :label="item.serviceName" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div v-if="testResult" style="margin-top: 16px;">
        <el-alert :title="testResult.msg" :type="testResult.success ? 'success' : 'error'" show-icon :closable="false" />
        <div v-if="testResult.success && testResult.data" style="margin-top: 12px;">
          <el-descriptions :column="2" border size="mini">
            <el-descriptions-item label="响应时间">{{ testResult.data.responseTime || 0 }}ms</el-descriptions-item>
            <el-descriptions-item label="状态码">{{ testResult.data.statusCode || '-' }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" :loading="testLoading" @click="doTestService(testServiceId)">开始测试</el-button>
      </div>
    </el-dialog>

    <!-- WSDL管理弹窗 -->
    <el-dialog title="WSDL管理" :visible.sync="wsdlDialogVisible" width="800px">
      <el-table :data="wsdlList" border size="mini" empty-text="暂无SOAP服务或WSDL地址">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="serviceName" label="服务名称" width="180" show-overflow-tooltip />
        <el-table-column prop="wsdlUrl" label="WSDL地址" show-overflow-tooltip />
        <el-table-column prop="namespace" label="命名空间" width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="wsdlDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 服务设置弹窗 -->
    <el-dialog title="服务设置" :visible.sync="settingsDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form :model="settingsForm" label-width="120px" size="small">
        <el-form-item label="默认超时时间">
          <el-input-number v-model="settingsForm.defaultTimeout" :min="1" :max="300" /> <span style="margin-left: 8px;">秒</span>
        </el-form-item>
        <el-form-item label="默认重试次数">
          <el-input-number v-model="settingsForm.defaultRetryCount" :min="0" :max="10" />
        </el-form-item>
        <el-form-item label="默认认证方式">
          <el-select v-model="settingsForm.defaultAuthType" style="width: 100%">
            <el-option value="NONE" label="无认证" />
            <el-option value="BASIC" label="Basic认证" />
            <el-option value="WSSE" label="WS-Security" />
            <el-option value="OAUTH2" label="OAuth2" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSettings">保存</el-button>
      </div>
    </el-dialog>

    <!-- 监控中心弹窗 -->
    <el-dialog title="监控中心" :visible.sync="monitorDialogVisible" width="900px">
      <el-row :gutter="16" style="margin-bottom: 16px;">
        <el-col :span="8">
          <el-card shadow="never"><div style="text-align: center;"><div style="font-size: 24px; font-weight: bold; color: #409EFF;">{{ wsStats.totalServices }}</div><div style="color: #909399; margin-top: 4px;">服务总数</div></div></el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never"><div style="text-align: center;"><div style="font-size: 24px; font-weight: bold; color: #67C23A;">{{ wsStats.activeServices }}</div><div style="color: #909399; margin-top: 4px;">活跃服务</div></div></el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never"><div style="text-align: center;"><div style="font-size: 24px; font-weight: bold; color: #E6A23C;">{{ wsStats.successRate }}%</div><div style="color: #909399; margin-top: 4px;">成功率</div></div></el-card>
        </el-col>
      </el-row>
      <el-table :data="monitorData" border size="mini" empty-text="暂无监控数据">
        <el-table-column prop="serviceName" label="服务名称" width="180" show-overflow-tooltip />
        <el-table-column prop="serviceType" label="类型" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getServiceTypeColor(scope.row.serviceType)" size="mini">{{ scope.row.serviceType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="callCount" label="调用次数" width="100" align="center" />
        <el-table-column prop="avgResponseTime" label="平均响应(ms)" width="120" align="center" />
        <el-table-column prop="lastCall" label="最后调用时间" show-overflow-tooltip />
      </el-table>
      <div slot="footer">
        <el-button @click="monitorDialogVisible = false">关闭</el-button>
        <el-button type="primary" icon="el-icon-refresh" @click="handleMonitor">刷新</el-button>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="帮助 - Web服务集成" :visible.sync="helpDialogVisible" width="650px">
      <div style="line-height: 2;">
        <h4 style="margin: 0 0 8px;">功能说明</h4>
        <p>Web服务集成管理模块支持 SOAP、REST、XML-RPC 等Web服务的连接管理、方法调用和WSDL解析。</p>
        <h4 style="margin: 16px 0 8px;">操作指南</h4>
        <ul style="padding-left: 20px;">
          <li><b>创建服务</b>：点击"创建服务"按钮，填写服务名称、类型、端点等信息</li>
          <li><b>服务测试</b>：选择服务后点击"服务测试"，验证服务连通性</li>
          <li><b>WSDL管理</b>：查看和管理SOAP服务的WSDL描述文件</li>
          <li><b>服务设置</b>：配置默认超时时间、重试次数、认证方式等</li>
          <li><b>监控中心</b>：查看所有服务的运行状态、调用统计和响应时间</li>
        </ul>
        <h4 style="margin: 16px 0 8px;">服务类型</h4>
        <ul style="padding-left: 20px;">
          <li><b>SOAP</b>：基于XML的Web服务协议，支持WSDL描述</li>
          <li><b>REST</b>：基于HTTP的轻量级Web服务</li>
          <li><b>XML-RPC</b>：基于XML的远程过程调用协议</li>
        </ul>
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
  name: 'WebServiceIntegration',
  data() {
    return {
      // 统计数据
      wsStats: {
        totalServices: 0,
        activeServices: 0,
        todayCalls: 0,
        successRate: 0
      },

      // Web服务类型
      wsTypes: [],
      selectedWsType: null,
      
      // 服务列表
      wsList: [],
      loading: false,
      searchKeyword: '',
      
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentService: null,
      serviceMethods: [],
      serviceLogs: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      
      // 表单数据
      serviceForm: {
        serviceName: '',
        serviceType: '',
        endpoint: '',
        wsdlUrl: '',
        version: '',
        description: ''
      },
      
      // 表单验证规则
      serviceRules: {
        serviceName: [
          { required: true, message: '请输入服务名称', trigger: 'blur' }
        ],
        serviceType: [
          { required: true, message: '请选择服务类型', trigger: 'change' }
        ],
        endpoint: [
          { required: true, message: '请输入服务端点', trigger: 'blur' }
        ]
      },

      // 服务测试弹窗
      testDialogVisible: false,
      testServiceId: '',
      testLoading: false,
      testResult: null,

      // WSDL管理弹窗
      wsdlDialogVisible: false,
      wsdlList: [],

      // 服务设置弹窗
      settingsDialogVisible: false,
      settingsForm: {
        defaultTimeout: 30,
        defaultRetryCount: 3,
        defaultAuthType: 'NONE'
      },

      // 监控中心弹窗
      monitorDialogVisible: false,
      monitorData: [],

      // 帮助弹窗
      helpDialogVisible: false
    }
  },
  
  computed: {
    filteredWsList() {
      let list = this.wsList
      // 按服务类型筛选
      if (this.selectedWsType) {
        list = list.filter(item => item.serviceType === this.selectedWsType)
      }
      // 按关键词搜索
      if (this.searchKeyword) {
        const kw = this.searchKeyword.toLowerCase()
        list = list.filter(item =>
          (item.serviceName && item.serviceName.toLowerCase().includes(kw)) ||
          (item.serviceType && item.serviceType.toLowerCase().includes(kw)) ||
          (item.endpoint && item.endpoint.toLowerCase().includes(kw)) ||
          (item.wsdlUrl && item.wsdlUrl.toLowerCase().includes(kw))
        )
      }
      return list
    }
  },

  created() {
    this.getWsList()
    this.getWsStats()
    this.getWsTypes()
  },

  methods: {
    async getWsList() {
      this.loading = true
      try {
        const response = await systemIntegrationApi.getWsList()
        if (response.code === 1 && response.data) { this.wsList = response.data }
      } catch (error) { this.$message.error('获取服务列表失败：' + error.message) } finally { this.loading = false }
    },
    async getWsStats() {
      try {
        const response = await systemIntegrationApi.getWsStats()
        if (response.code === 1 && response.data) { this.wsStats = response.data }
      } catch (error) { console.error('获取统计数据失败：', error) }
    },
    async getWsTypes() {
      try {
        const response = await systemIntegrationApi.getWsList()
        if (response.code === 1 && response.data) {
          const typeMap = {}
          response.data.forEach(item => {
            const t = item.serviceType || 'OTHER'
            if (!typeMap[t]) { typeMap[t] = { id: t, name: this.getWsTypeText(t), description: t + 'Web服务', icon: 'el-icon-service', serviceCount: 0 } }
            typeMap[t].serviceCount++
          })
          this.wsTypes = Object.values(typeMap)
        }
      } catch (error) { console.error('获取WS类型失败：', error) }
    },
    refreshWsTypes() { this.getWsTypes(); this.$message.success('服务类型已刷新') },
    handleSelectWsType(wsType) {
      this.selectedWsType = this.selectedWsType === wsType.id ? null : wsType.id
    },
    handleCreateService() {
      this.dialogTitle = '创建Web服务'
      this.serviceForm = { serviceName: '', serviceType: '', endpoint: '', wsdlUrl: '', version: '', description: '' }
      this.dialogVisible = true
    },
    handleRefresh() { this.getWsList(); this.getWsStats(); this.getWsTypes(); this.$message.success('数据已刷新') },
    handleTestService(row) {
      if (row && row.id) {
        // 从列表行操作直接测试
        this.testServiceId = row.id
        this.testResult = null
        this.testDialogVisible = true
        this.doTestService(row.id)
      } else {
        // 从工具栏按钮打开测试弹窗
        this.testServiceId = ''
        this.testResult = null
        this.testDialogVisible = true
      }
    },
    async doTestService(serviceId) {
      if (!serviceId) {
        this.$message.warning('请选择要测试的服务')
        return
      }
      this.testLoading = true
      this.testResult = null
      try {
        const response = await systemIntegrationApi.webService.test(serviceId)
        if (response.code === 1) {
          this.testResult = { success: true, data: response.data, msg: '服务连接测试成功' }
        } else {
          this.testResult = { success: false, msg: response.msg || '服务测试失败' }
        }
      } catch (error) {
        this.testResult = { success: false, msg: '服务测试异常：' + error.message }
      } finally {
        this.testLoading = false
      }
    },
    handleSettings() {
      this.settingsDialogVisible = true
    },
    async handleSaveSettings() {
      this.$message.success('设置已保存')
      this.settingsDialogVisible = false
    },
    handleHelp() {
      this.helpDialogVisible = true
    },
    handleView(row) { this.currentService = row; this.detailDrawerVisible = true; this.detailActiveTab = 'basic' },
    handleRowClick(row) { this.currentService = row },
    handleMoreAction(command, row) {
      const actions = {
        'edit': () => this.handleEdit(row),
        'config': () => this.handleView(row),
        'wsdl': () => { this.currentService = row; this.handleView(row); this.detailActiveTab = 'config' },
        'logs': () => { this.currentService = row; this.handleView(row); this.detailActiveTab = 'logs' },
        'monitor': () => this.handleMonitor(),
        'copy': () => this.handleCopyService(row),
        'delete': () => this.handleDelete(row)
      }
      if (actions[command]) actions[command]()
    },
    async handleCopyService(row) {
      if (!row || !row.id) return
      try {
        const copyData = { ...row, serviceName: row.serviceName + ' (副本)', id: undefined }
        const response = await systemIntegrationApi.webService.create(copyData)
        if (response.code === 1) {
          this.$message.success('复制成功')
          this.getWsList()
          this.getWsStats()
        } else {
          this.$message.error(response.msg || '复制失败')
        }
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },
    handleEdit(row) { this.dialogTitle = '编辑Web服务'; this.serviceForm = { ...row }; this.dialogVisible = true },
    handleDelete(row) {
      this.$confirm('确认删除该服务？', '警告', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.webService.delete(row.id)
          if (response.code === 1) { this.$message.success('删除成功'); this.getWsList(); this.getWsStats() } else { this.$message.error(response.msg || '删除失败') }
        } catch (error) { this.$message.error('删除失败：' + error.message) }
      }).catch(() => {})
    },
    handleSubmitForm() {
      this.$refs.serviceForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          let response
          if (this.serviceForm.id) { response = await systemIntegrationApi.webService.update(this.serviceForm.id, this.serviceForm) } else { response = await systemIntegrationApi.webService.create(this.serviceForm) }
          if (response.code === 1) { this.$message.success(this.serviceForm.id ? '更新成功' : '创建成功'); this.dialogVisible = false; this.getWsList(); this.getWsStats() } else { this.$message.error(response.msg || '操作失败') }
        } catch (error) { this.$message.error('操作失败：' + error.message) } finally { this.submitLoading = false }
      })
    },
    handleDialogClose() { this.$refs.serviceForm && this.$refs.serviceForm.resetFields() },
    handleTestServiceForm() {
      if (this.serviceForm.id) { this.handleTestService(this.serviceForm) } else { this.$message.info('请先保存服务后再测试') }
    },
    handleTestMethod(row) {
      if (row && row.methodName && this.currentService) {
        this.$confirm('确认测试方法：' + row.methodName + '？', '测试方法', { type: 'info' }).then(async () => {
          try {
            const response = await systemIntegrationApi.webService.invoke(this.currentService.id, { methodName: row.methodName })
            if (response.code === 1) { this.$message.success('方法测试成功') } else { this.$message.error(response.msg || '测试失败') }
          } catch (error) { this.$message.error('测试失败：' + error.message) }
        }).catch(() => {})
      } else { this.$message.info('请先选择一个服务和方法') }
    },
    handleViewMethod(row) {
      if (row) { this.$alert('方法名称：' + (row.methodName || '') + '\n操作：' + (row.operation || '') + '\n输入消息：' + (row.inputMessage || '') + '\n输出消息：' + (row.outputMessage || ''), '方法详情', { confirmButtonText: '关闭' }) }
    },
    handleWsdl() {
      this.wsdlList = this.wsList.filter(item => item.serviceType === 'SOAP' && item.wsdlUrl)
      this.wsdlDialogVisible = true
    },
    handleMonitor() {
      this.monitorData = this.wsList.map(item => ({
        serviceName: item.serviceName,
        serviceType: item.serviceType,
        status: item.status,
        callCount: item.callCount || 0,
        avgResponseTime: item.avgResponseTime || 0,
        lastCall: item.lastCall
      }))
      this.monitorDialogVisible = true
    },
    // 辅助方法
    getWsTypeColor(type) { const m = { 'SOAP': 'primary', 'REST': 'success', 'XMLRPC': 'warning' }; return m[type] || 'info' },
    getWsTypeText(type) { const m = { 'SOAP': 'SOAP服务', 'REST': 'REST服务', 'XMLRPC': 'XML-RPC' }; return m[type] || type || '未知' },
    getServiceTypeColor(type) { const m = { 'SOAP': 'primary', 'REST': 'success', 'XML_RPC': 'warning', 'XMLRPC': 'warning' }; return m[type] || 'info' },
    getStatusColor(status) { const m = { 'ACTIVE': 'success', 'INACTIVE': 'danger', 'PENDING': 'warning', 'ERROR': 'danger', 'TESTING': 'warning' }; return m[status] || 'info' },
    getStatusText(status) { const m = { 'ACTIVE': '运行中', 'INACTIVE': '已停止', 'PENDING': '待启动', 'ERROR': '异常', 'TESTING': '测试中' }; return m[status] || status || '未知' },
    getResponseTimeClass(time) {
      if (time < 200) return 'response-fast'
      if (time < 1000) return 'response-normal'
      return 'response-slow'
    },
    getStatusCodeColor(code) {
      if (code >= 200 && code < 300) return 'success'
      if (code >= 400 && code < 500) return 'warning'
      if (code >= 500) return 'danger'
      return 'info'
    }
  }
}
</script>
