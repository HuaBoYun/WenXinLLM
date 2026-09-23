<template>
  <div class="erp-integration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>ERP系统集成</h2>
      <p>SAP、Oracle、用友等ERP系统深度集成，支持预算数据双向同步和业务流程对接</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateConnection">创建连接</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-connection" @click="handleTestConnection">连接测试</el-button>
            <el-button type="info" icon="el-icon-refresh" @click="handleSyncData">数据同步</el-button>
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

    <!-- ERP集成统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ erpStats.totalConnections }}</div>
            <div class="stat-label">ERP连接</div>
            <div class="stat-description">已配置ERP连接数</div>
            <div class="stat-trend">
              <i class="el-icon-s-cooperation"></i>
              <span>系统连接</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-cooperation"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ erpStats.activeConnections }}</div>
            <div class="stat-label">活跃连接</div>
            <div class="stat-description">正在运行的连接</div>
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
        <el-card class="stat-card sync-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ erpStats.todaySync }}</div>
            <div class="stat-label">今日同步</div>
            <div class="stat-description">今日数据同步次数</div>
            <div class="stat-trend">
              <i class="el-icon-refresh"></i>
              <span>数据同步</span>
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
            <div class="stat-number">{{ erpStats.successRate }}%</div>
            <div class="stat-label">成功率</div>
            <div class="stat-description">数据同步成功率</div>
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

    <!-- ERP系统类型选择 -->
    <el-card class="erp-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>ERP系统类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshErpTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="erpType in erpTypes" :key="erpType.id">
          <el-card 
            class="erp-type-item" 
            shadow="hover" 
            @click.native="handleSelectErpType(erpType)"
            :class="{ 'selected': selectedErpType === erpType.id }"
          >
            <div class="erp-type-icon">
              <i :class="erpType.icon"></i>
            </div>
            <div class="erp-type-title">{{ erpType.name }}</div>
            <div class="erp-type-description">{{ erpType.description }}</div>
            <div class="erp-type-stats">
              <span class="connection-count">{{ erpType.connectionCount }} 个连接</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- ERP连接列表 -->
    <el-card class="erp-connections-card" shadow="never">
      <div slot="header" class="card-header">
        <span>ERP连接管理</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索连接"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getErpConnectionList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="filteredErpConnectionList"
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
        <el-table-column prop="erpType" label="ERP类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getErpTypeColor(scope.row.erpType)" size="mini">
              {{ getErpTypeText(scope.row.erpType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="serverAddress" label="服务器地址" width="200" show-overflow-tooltip />
        <el-table-column prop="database" label="数据库" width="120" align="center" />
        <el-table-column prop="lastSync" label="最后同步" width="150" align="center">
          <template slot-scope="scope">
            <span class="last-sync">{{ scope.row.lastSync }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="syncStatus" label="同步状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSyncStatusColor(scope.row.syncStatus)" size="mini">
              {{ getSyncStatusText(scope.row.syncStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="连接状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
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
                <el-dropdown-item command="config">配置</el-dropdown-item>
                <el-dropdown-item command="logs">同步日志</el-dropdown-item>
                <el-dropdown-item command="mapping">字段映射</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 连接详情抽屉 -->
    <el-drawer
      title="ERP连接详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentConnection">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="连接基本信息" :column="2" border>
              <el-descriptions-item label="连接名称">{{ currentConnection.connectionName }}</el-descriptions-item>
              <el-descriptions-item label="ERP类型">{{ getErpTypeText(currentConnection.erpType) }}</el-descriptions-item>
              <el-descriptions-item label="服务器地址">{{ currentConnection.serverAddress }}</el-descriptions-item>
              <el-descriptions-item label="端口">{{ currentConnection.port }}</el-descriptions-item>
              <el-descriptions-item label="数据库">{{ currentConnection.database }}</el-descriptions-item>
              <el-descriptions-item label="用户名">{{ currentConnection.username }}</el-descriptions-item>
              <el-descriptions-item label="最后同步">{{ currentConnection.lastSync }}</el-descriptions-item>
              <el-descriptions-item label="同步状态">
                <el-tag :type="getSyncStatusColor(currentConnection.syncStatus)" size="mini">
                  {{ getSyncStatusText(currentConnection.syncStatus) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="连接状态">
                <el-tag :type="getStatusColor(currentConnection.status)" size="mini">
                  {{ getStatusText(currentConnection.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentConnection.createTime }}</el-descriptions-item>
              <el-descriptions-item label="连接描述" :span="2">{{ currentConnection.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="同步配置" name="config">
            <el-form label-width="120px" size="small">
              <el-form-item label="同步频率">
                <el-input :value="currentConnection.syncFrequency" readonly />
              </el-form-item>
              <el-form-item label="同步方向">
                <el-input :value="currentConnection.syncDirection" readonly />
              </el-form-item>
              <el-form-item label="数据范围">
                <el-input :value="currentConnection.dataScope" readonly type="textarea" :rows="2" />
              </el-form-item>
              <el-form-item label="过滤条件">
                <el-input :value="currentConnection.filterConditions" readonly type="textarea" :rows="3" />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="字段映射" name="mapping">
            <el-table :data="fieldMappings" border size="mini">
              <el-table-column prop="sourceField" label="源字段" width="150" />
              <el-table-column prop="sourceType" label="源类型" width="100" />
              <el-table-column prop="targetField" label="目标字段" width="150" />
              <el-table-column prop="targetType" label="目标类型" width="100" />
              <el-table-column prop="transformation" label="转换规则" />
              <el-table-column prop="required" label="必填" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.required ? 'danger' : 'info'" size="mini">
                    {{ scope.row.required ? '是' : '否' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="同步日志" name="logs">
            <el-table :data="syncLogs" border size="mini">
              <el-table-column prop="syncTime" label="同步时间" width="150" />
              <el-table-column prop="syncType" label="同步类型" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getSyncTypeColor(scope.row.syncType)" size="mini">
                    {{ getSyncTypeText(scope.row.syncType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="recordCount" label="记录数" width="100" align="center" />
              <el-table-column prop="duration" label="耗时" width="100" align="center" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getSyncLogStatusColor(scope.row.status)" size="mini">
                    {{ getSyncLogStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="message" label="消息" />
            </el-table>
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
            <el-form-item label="ERP类型" prop="erpType">
              <el-select v-model="connectionForm.erpType" placeholder="请选择ERP类型" style="width: 100%">
                <el-option value="SAP" label="SAP ERP" />
                <el-option value="ORACLE" label="Oracle ERP" />
                <el-option value="YONYOU" label="用友ERP" />
                <el-option value="KINGDEE" label="金蝶ERP" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="连接方式" prop="connectionType">
              <el-select v-model="connectionForm.connectionType" placeholder="请选择连接方式" style="width: 100%">
                <el-option value="DATABASE" label="数据库直连" />
                <el-option value="API" label="API接口" />
                <el-option value="WEBSERVICE" label="Web Service" />
                <el-option value="FILE" label="文件导入" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-form-item label="服务器地址" prop="serverAddress">
              <el-input v-model="connectionForm.serverAddress" placeholder="请输入服务器地址" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="端口" prop="port">
              <el-input v-model="connectionForm.port" placeholder="端口号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="数据库" prop="database">
              <el-input v-model="connectionForm.database" placeholder="数据库名" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="connectionForm.username" placeholder="用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="密码" prop="password">
              <el-input v-model="connectionForm.password" type="password" placeholder="密码" show-password />
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

    <!-- 集成设置对话框 -->
    <el-dialog title="集成设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="settingsForm" label-width="140px" size="small">
        <el-form-item label="默认同步频率">
          <el-select v-model="settingsForm.defaultSyncFrequency" style="width: 100%">
            <el-option value="REALTIME" label="实时" />
            <el-option value="HOURLY" label="每小时" />
            <el-option value="DAILY" label="每天" />
            <el-option value="WEEKLY" label="每周" />
            <el-option value="MONTHLY" label="每月" />
            <el-option value="MANUAL" label="手动" />
          </el-select>
        </el-form-item>
        <el-form-item label="默认同步方向">
          <el-select v-model="settingsForm.defaultSyncDirection" style="width: 100%">
            <el-option value="IMPORT" label="导入（ERP→本系统）" />
            <el-option value="EXPORT" label="导出（本系统→ERP）" />
            <el-option value="BIDIRECTIONAL" label="双向同步" />
          </el-select>
        </el-form-item>
        <el-form-item label="失败重试次数">
          <el-input-number v-model="settingsForm.retryCount" :min="0" :max="10" />
        </el-form-item>
        <el-form-item label="重试间隔(秒)">
          <el-input-number v-model="settingsForm.retryInterval" :min="10" :max="3600" :step="10" />
        </el-form-item>
        <el-form-item label="启用通知">
          <el-switch v-model="settingsForm.enableNotification" />
        </el-form-item>
        <el-form-item label="通知邮箱" v-if="settingsForm.enableNotification">
          <el-input v-model="settingsForm.notificationEmail" placeholder="请输入通知邮箱" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSettings">保存</el-button>
      </div>
    </el-dialog>

    <!-- 同步日志对话框 -->
    <el-dialog title="同步日志" :visible.sync="logsDialogVisible" width="900px">
      <el-table :data="logsData" border size="mini" v-loading="logsLoading" max-height="500">
        <el-table-column prop="connectionName" label="连接名称" width="150" show-overflow-tooltip />
        <el-table-column prop="syncTime" label="同步时间" width="160" align="center" />
        <el-table-column prop="syncStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSyncLogStatusColor(scope.row.syncStatus)" size="mini">
              {{ getSyncLogStatusText(scope.row.syncStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recordCount" label="记录数" width="100" align="center" />
        <el-table-column prop="duration" label="耗时(ms)" width="100" align="center" />
        <el-table-column prop="message" label="消息" show-overflow-tooltip />
      </el-table>
      <div slot="footer">
        <el-button @click="logsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 数据同步对话框 -->
    <el-dialog title="数据同步" :visible.sync="syncDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form :model="syncForm" label-width="100px" size="small">
        <el-form-item label="同步类型">
          <el-radio-group v-model="syncForm.syncType">
            <el-radio label="FULL">全量同步</el-radio>
            <el-radio label="INCREMENTAL">增量同步</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="同步方向">
          <el-select v-model="syncForm.syncDirection" style="width: 100%">
            <el-option value="IMPORT" label="导入（ERP→本系统）" />
            <el-option value="EXPORT" label="导出（本系统→ERP）" />
            <el-option value="BIDIRECTIONAL" label="双向同步" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据范围">
          <el-input v-model="syncForm.dataScope" type="textarea" :rows="2" placeholder="留空表示全部数据" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="syncDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleExecuteSync" :loading="syncExecuting">开始同步</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="ERP集成帮助" :visible.sync="helpDialogVisible" width="600px">
      <div style="line-height: 2;">
        <h4>功能说明</h4>
        <p>ERP集成管理支持SAP、Oracle、用友、金蝶等ERP系统的连接管理、数据同步和字段映射配置。</p>
        <h4>操作指南</h4>
        <ul>
          <li><b>创建连接</b>：点击"创建连接"按钮，填写ERP系统连接信息</li>
          <li><b>连接测试</b>：选择连接后点击"测试"验证连接是否正常</li>
          <li><b>数据同步</b>：支持全量同步和增量同步，可配置同步方向</li>
          <li><b>字段映射</b>：配置ERP系统字段与本系统字段的对应关系</li>
          <li><b>同步日志</b>：查看历史同步记录和执行状态</li>
        </ul>
        <h4>ERP类型说明</h4>
        <ul>
          <li><b>SAP ERP</b>：支持SAP R/3、SAP S/4HANA等版本</li>
          <li><b>Oracle ERP</b>：支持Oracle EBS、Oracle Cloud等版本</li>
          <li><b>用友ERP</b>：支持用友U8、NC等版本</li>
          <li><b>金蝶ERP</b>：支持金蝶K3、KIS等版本</li>
        </ul>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="helpDialogVisible = false">知道了</el-button>
      </div>
    </el-dialog>

    <!-- 同步配置对话框 -->
    <el-dialog title="同步配置" :visible.sync="syncConfigDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="syncConfigForm" label-width="120px" size="small">
        <el-form-item label="同步频率">
          <el-select v-model="syncConfigForm.syncFrequency" style="width: 100%">
            <el-option value="REALTIME" label="实时" />
            <el-option value="HOURLY" label="每小时" />
            <el-option value="DAILY" label="每天" />
            <el-option value="WEEKLY" label="每周" />
            <el-option value="MONTHLY" label="每月" />
            <el-option value="MANUAL" label="手动" />
          </el-select>
        </el-form-item>
        <el-form-item label="同步方向">
          <el-select v-model="syncConfigForm.syncDirection" style="width: 100%">
            <el-option value="IMPORT" label="导入（ERP→本系统）" />
            <el-option value="EXPORT" label="导出（本系统→ERP）" />
            <el-option value="BIDIRECTIONAL" label="双向同步" />
          </el-select>
        </el-form-item>
        <el-form-item label="CRON表达式">
          <el-input v-model="syncConfigForm.syncSchedule" placeholder="如: 0 0 2 * * ? (每天凌晨2点)" />
        </el-form-item>
        <el-form-item label="同步范围">
          <el-input v-model="syncConfigForm.syncScope" type="textarea" :rows="3" placeholder="请输入同步范围配置（JSON格式）" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="syncConfigDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSyncConfig" :loading="syncConfigLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 字段映射对话框 -->
    <el-dialog title="字段映射配置" :visible.sync="fieldMappingDialogVisible" width="900px" :close-on-click-modal="false">
      <div style="margin-bottom: 10px;">
        <el-button type="primary" size="mini" icon="el-icon-plus" @click="handleAddFieldMapping">添加映射</el-button>
      </div>
      <el-table :data="fieldMappingData" border size="mini" v-loading="fieldMappingLoading" max-height="400">
        <el-table-column label="源字段" width="150">
          <template slot-scope="scope">
            <el-input v-model="scope.row.sourceField" size="mini" placeholder="源字段名" />
          </template>
        </el-table-column>
        <el-table-column label="源类型" width="120">
          <template slot-scope="scope">
            <el-select v-model="scope.row.sourceType" size="mini">
              <el-option value="VARCHAR" label="VARCHAR" />
              <el-option value="NUMBER" label="NUMBER" />
              <el-option value="DATE" label="DATE" />
              <el-option value="DECIMAL" label="DECIMAL" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="目标字段" width="150">
          <template slot-scope="scope">
            <el-input v-model="scope.row.targetField" size="mini" placeholder="目标字段名" />
          </template>
        </el-table-column>
        <el-table-column label="目标类型" width="120">
          <template slot-scope="scope">
            <el-select v-model="scope.row.targetType" size="mini">
              <el-option value="VARCHAR" label="VARCHAR" />
              <el-option value="NUMBER" label="NUMBER" />
              <el-option value="DATE" label="DATE" />
              <el-option value="DECIMAL" label="DECIMAL" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="转换规则">
          <template slot-scope="scope">
            <el-input v-model="scope.row.transformation" size="mini" placeholder="转换规则" />
          </template>
        </el-table-column>
        <el-table-column label="必填" width="80" align="center">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.required" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-delete" @click="handleDeleteFieldMapping(scope.$index)" style="color: #F56C6C;">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="fieldMappingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveFieldMapping" :loading="fieldMappingLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 测试连接结果对话框 -->
    <el-dialog title="连接测试结果" :visible.sync="testResultDialogVisible" width="500px">
      <div v-loading="testLoading" style="min-height: 100px;">
        <div v-if="testResultData" style="text-align: center; padding: 20px;">
          <i :class="testResultData.success ? 'el-icon-success' : 'el-icon-error'"
             :style="{ fontSize: '48px', color: testResultData.success ? '#67C23A' : '#F56C6C' }"></i>
          <p style="margin-top: 16px; font-size: 16px;">{{ testResultData.message }}</p>
          <div v-if="testResultData.responseTime" style="color: #909399; margin-top: 8px;">
            响应时间: {{ testResultData.responseTime }}ms
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="testResultDialogVisible = false">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { systemIntegrationApi } from '@/api/managementAccountant/ncv65/systemIntegration'

export default {
  name: 'ErpIntegration',
  data() {
    return {
      // 统计数据
      erpStats: {
        totalConnections: 0,
        activeConnections: 0,
        todaySync: 0,
        successRate: 0
      },

      // ERP类型
      erpTypes: [],
      selectedErpType: null,

      // 连接列表
      erpConnectionList: [],
      loading: false,
      searchKeyword: '',

      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentConnection: null,
      fieldMappings: [],
      syncLogs: [],

      // 创建/编辑对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,

      // 集成设置对话框
      settingsDialogVisible: false,
      settingsForm: {
        defaultSyncFrequency: 'DAILY',
        defaultSyncDirection: 'IMPORT',
        retryCount: 3,
        retryInterval: 60,
        enableNotification: true,
        notificationEmail: ''
      },

      // 同步日志对话框
      logsDialogVisible: false,
      logsData: [],
      logsLoading: false,

      // 数据同步对话框
      syncDialogVisible: false,
      syncForm: {
        erpId: '',
        syncType: 'FULL',
        syncDirection: 'IMPORT',
        dataScope: ''
      },
      syncExecuting: false,

      // 帮助对话框
      helpDialogVisible: false,

      // 同步配置对话框
      syncConfigDialogVisible: false,
      syncConfigForm: {
        erpId: '',
        syncFrequency: 'DAILY',
        syncDirection: 'IMPORT',
        syncSchedule: '',
        syncScope: ''
      },
      syncConfigLoading: false,

      // 字段映射对话框
      fieldMappingDialogVisible: false,
      fieldMappingData: [],
      fieldMappingErpId: '',
      fieldMappingLoading: false,

      // 测试连接结果对话框
      testResultDialogVisible: false,
      testResultData: null,
      testLoading: false,

      // 表单数据
      connectionForm: {
        connectionName: '',
        erpType: '',
        connectionType: 'DATABASE',
        serverAddress: '',
        port: '',
        database: '',
        username: '',
        password: '',
        description: ''
      },

      // 表单验证规则
      connectionRules: {
        connectionName: [
          { required: true, message: '请输入连接名称', trigger: 'blur' }
        ],
        erpType: [
          { required: true, message: '请选择ERP类型', trigger: 'change' }
        ],
        connectionType: [
          { required: true, message: '请选择连接方式', trigger: 'change' }
        ],
        serverAddress: [
          { required: true, message: '请输入服务器地址', trigger: 'blur' }
        ],
        port: [
          { required: true, message: '请输入端口号', trigger: 'blur' }
        ],
        database: [
          { required: true, message: '请输入数据库名', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  
  computed: {
    filteredErpConnectionList() {
      if (!this.searchKeyword) return this.erpConnectionList
      const kw = this.searchKeyword.toLowerCase()
      return this.erpConnectionList.filter(item =>
        (item.connectionName && item.connectionName.toLowerCase().includes(kw)) ||
        (item.erpType && item.erpType.toLowerCase().includes(kw)) ||
        (item.serverAddress && item.serverAddress.toLowerCase().includes(kw)) ||
        (item.status && item.status.toLowerCase().includes(kw))
      )
    }
  },

  created() {
    this.getErpConnectionList()
    this.getErpStats()
    this.getErpTypes()
  },
  
  methods: {
    // ===== 数据转换辅助方法 =====

    // 将后端实体转换为前端显示格式
    transformEntityToDisplay(entity) {
      let connConfig = {}
      try {
        if (entity.connectionConfig) {
          connConfig = JSON.parse(entity.connectionConfig)
        }
      } catch (e) {
        connConfig = {}
      }
      return {
        ...entity,
        id: entity.erpId,
        connectionName: entity.erpName || '',
        serverAddress: connConfig.serverAddress || '',
        port: connConfig.port || '',
        database: connConfig.database || '',
        username: connConfig.username || '',
        lastSync: entity.lastSyncTime || '',
        syncStatus: this.deriveSyncStatus(entity),
        status: this.deriveConnectionStatus(entity),
        description: entity.remark || '',
        syncFrequency: entity.syncFrequency || '',
        syncDirection: entity.syncDirection || '',
        dataScope: entity.syncScope || '',
        filterConditions: ''
      }
    },

    // 推导同步状态
    deriveSyncStatus(entity) {
      if (!entity.lastSyncTime) return 'PENDING'
      if (entity.integrationStatus === 'ERROR') return 'FAILED'
      if (entity.integrationStatus === 'ACTIVE') return 'SUCCESS'
      return 'PENDING'
    },

    // 推导连接状态
    deriveConnectionStatus(entity) {
      if (entity.integrationStatus === 'ACTIVE' && entity.isEnabled) return 'CONNECTED'
      if (entity.integrationStatus === 'ERROR') return 'DISCONNECTED'
      if (entity.integrationStatus === 'TESTING') return 'PENDING'
      if (entity.isEnabled) return 'CONNECTED'
      return 'DISCONNECTED'
    },

    // 将前端表单转换为后端实体格式
    transformFormToEntity(form) {
      const entity = {
        erpName: form.connectionName,
        erpType: form.erpType,
        connectionType: form.connectionType || 'DATABASE',
        connectionConfig: JSON.stringify({
          serverAddress: form.serverAddress,
          port: form.port,
          database: form.database,
          username: form.username,
          password: form.password
        }),
        remark: form.description
      }
      if (form.erpId) {
        entity.erpId = form.erpId
      }
      return entity
    },

    // 获取连接列表
    async getErpConnectionList() {
      this.loading = true
      try {
        const response = await systemIntegrationApi.getErpConnectionList(this.selectedErpType)
        if (response.code === 1 && response.data) {
          this.erpConnectionList = response.data.map(item => this.transformEntityToDisplay(item))
        }
      } catch (error) {
        this.$message.error('获取连接列表失败：' + (error.message || '未知错误'))
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getErpStats() {
      try {
        const response = await systemIntegrationApi.getErpStats()
        if (response.code === 1 && response.data) {
          this.erpStats = response.data
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    // 获取ERP类型列表（从连接列表中聚合）
    async getErpTypes() {
      try {
        const response = await systemIntegrationApi.getErpConnectionList()
        if (response.code === 1 && response.data) {
          const typeMap = {}
          response.data.forEach(item => {
            const t = item.erpType || 'OTHER'
            if (!typeMap[t]) {
              typeMap[t] = { id: t, name: this.getErpTypeText(t), description: t + '企业资源规划系统', icon: 'el-icon-s-cooperation', connectionCount: 0 }
            }
            typeMap[t].connectionCount++
          })
          this.erpTypes = Object.values(typeMap)
        }
      } catch (error) {
        console.error('获取ERP类型失败：', error)
      }
    },

    // 刷新ERP类型
    refreshErpTypes() {
      this.getErpTypes()
      this.$message.success('ERP类型已刷新')
    },

    // 选择ERP类型筛选
    handleSelectErpType(erpType) {
      if (this.selectedErpType === erpType.id) {
        this.selectedErpType = null
      } else {
        this.selectedErpType = erpType.id
      }
      this.getErpConnectionList()
    },

    // 创建连接
    handleCreateConnection() {
      this.dialogTitle = '创建ERP连接'
      this.connectionForm = {
        connectionName: '',
        erpType: '',
        connectionType: 'DATABASE',
        serverAddress: '',
        port: '',
        database: '',
        username: '',
        password: '',
        description: ''
      }
      this.dialogVisible = true
    },

    // 刷新
    handleRefresh() {
      this.getErpConnectionList()
      this.getErpStats()
      this.getErpTypes()
      this.$message.success('数据已刷新')
    },

    // 工具栏-连接测试（无参数时提示选择）
    handleTestConnection(row) {
      if (row && row.id) {
        this.testLoading = true
        this.testResultData = null
        this.testResultDialogVisible = true
        systemIntegrationApi.erp.test(row.id).then(response => {
          this.testLoading = false
          if (response.code === 1) {
            this.testResultData = response.data || { success: true, message: '连接测试成功' }
            this.getErpConnectionList()
          } else {
            this.testResultData = { success: false, message: response.msg || '连接测试失败' }
          }
        }).catch(error => {
          this.testLoading = false
          this.testResultData = { success: false, message: '连接测试失败：' + (error.message || '未知错误') }
        })
      } else {
        this.$message.info('请先在列表中选择一个连接进行测试')
      }
    },

    // 工具栏-数据同步
    handleSyncData() {
      if (this.currentConnection && this.currentConnection.id) {
        this.syncForm.erpId = this.currentConnection.id
        this.syncDialogVisible = true
      } else {
        this.$message.info('请先在列表中选择一个连接进行同步')
      }
    },

    // 执行数据同步
    async handleExecuteSync() {
      if (!this.syncForm.erpId) {
        this.$message.warning('请选择要同步的连接')
        return
      }
      this.syncExecuting = true
      try {
        const response = await systemIntegrationApi.erp.sync(this.syncForm.erpId, {
          syncType: this.syncForm.syncType,
          syncDirection: this.syncForm.syncDirection,
          dataScope: this.syncForm.dataScope
        })
        if (response.code === 1) {
          this.$message.success('同步任务已启动')
          this.syncDialogVisible = false
          this.getErpConnectionList()
          this.getErpStats()
        } else {
          this.$message.error(response.msg || '同步失败')
        }
      } catch (error) {
        this.$message.error('同步失败：' + (error.message || '未知错误'))
      } finally {
        this.syncExecuting = false
      }
    },

    // 同步单条数据（列表操作按钮）
    handleSync(row) {
      this.syncForm.erpId = row.id
      this.syncDialogVisible = true
    },

    // 集成设置
    handleSettings() {
      this.settingsDialogVisible = true
    },

    // 保存集成设置
    handleSaveSettings() {
      this.$message.success('集成设置已保存')
      this.settingsDialogVisible = false
    },

    // 同步日志
    handleLogs() {
      this.logsDialogVisible = true
      this.loadAllSyncLogs()
    },

    // 加载所有同步日志
    async loadAllSyncLogs() {
      this.logsLoading = true
      try {
        const allLogs = []
        for (const conn of this.erpConnectionList) {
          if (conn.id) {
            try {
              const response = await systemIntegrationApi.erp.getSyncLog(conn.id)
              if (response.code === 1 && response.data) {
                response.data.forEach(log => {
                  allLogs.push({
                    ...log,
                    connectionName: conn.connectionName,
                    erpType: conn.erpType
                  })
                })
              }
            } catch (e) {
              // 单个连接日志获取失败不影响整体
            }
          }
        }
        this.logsData = allLogs.sort((a, b) => {
          return new Date(b.syncTime || 0) - new Date(a.syncTime || 0)
        })
      } catch (error) {
        this.$message.error('获取同步日志失败')
      } finally {
        this.logsLoading = false
      }
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 查看详情
    handleView(row) {
      this.currentConnection = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
    },

    // 行点击
    handleRowClick(row) {
      this.currentConnection = row
    },

    // 更多操作
    handleMoreAction(command, row) {
      const actions = {
        'edit': () => this.handleEdit(row),
        'config': () => this.handleSyncConfig(row),
        'logs': () => this.handleConnectionLogs(row),
        'mapping': () => this.handleFieldMapping(row),
        'delete': () => this.handleDelete(row)
      }
      if (actions[command]) {
        actions[command]()
      }
    },

    // 编辑连接
    handleEdit(row) {
      this.dialogTitle = '编辑ERP连接'
      let connConfig = {}
      try {
        if (row.connectionConfig) {
          connConfig = JSON.parse(row.connectionConfig)
        }
      } catch (e) {
        connConfig = {}
      }
      this.connectionForm = {
        erpId: row.erpId || row.id,
        connectionName: row.connectionName || row.erpName || '',
        erpType: row.erpType || '',
        connectionType: row.connectionType || 'DATABASE',
        serverAddress: connConfig.serverAddress || row.serverAddress || '',
        port: connConfig.port || row.port || '',
        database: connConfig.database || row.database || '',
        username: connConfig.username || row.username || '',
        password: '',
        description: row.remark || row.description || ''
      }
      this.dialogVisible = true
    },

    // 同步配置
    handleSyncConfig(row) {
      this.syncConfigForm = {
        erpId: row.erpId || row.id,
        syncFrequency: row.syncFrequency || 'DAILY',
        syncDirection: row.syncDirection || 'IMPORT',
        syncSchedule: row.syncSchedule || '',
        syncScope: row.syncScope || ''
      }
      this.syncConfigDialogVisible = true
    },

    // 保存同步配置
    async handleSaveSyncConfig() {
      this.syncConfigLoading = true
      try {
        const entity = {
          erpId: this.syncConfigForm.erpId,
          syncFrequency: this.syncConfigForm.syncFrequency,
          syncDirection: this.syncConfigForm.syncDirection,
          syncSchedule: this.syncConfigForm.syncSchedule,
          syncScope: this.syncConfigForm.syncScope
        }
        const response = await systemIntegrationApi.erp.update(entity.erpId, entity)
        if (response.code === 1) {
          this.$message.success('同步配置保存成功')
          this.syncConfigDialogVisible = false
          this.getErpConnectionList()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + (error.message || '未知错误'))
      } finally {
        this.syncConfigLoading = false
      }
    },

    // 查看单个连接的同步日志
    async handleConnectionLogs(row) {
      this.logsDialogVisible = true
      this.logsLoading = true
      try {
        const response = await systemIntegrationApi.erp.getSyncLog(row.erpId || row.id)
        if (response.code === 1 && response.data) {
          this.logsData = response.data.map(log => ({
            ...log,
            connectionName: row.connectionName || row.erpName,
            erpType: row.erpType
          }))
        } else {
          this.logsData = []
        }
      } catch (error) {
        this.logsData = []
        this.$message.error('获取同步日志失败')
      } finally {
        this.logsLoading = false
      }
    },

    // 字段映射
    async handleFieldMapping(row) {
      this.fieldMappingErpId = row.erpId || row.id
      this.fieldMappingDialogVisible = true
      this.fieldMappingLoading = true
      try {
        const response = await systemIntegrationApi.getFieldMappings(this.fieldMappingErpId)
        if (response.code === 1 && response.data) {
          try {
            this.fieldMappingData = JSON.parse(response.data)
          } catch (e) {
            this.fieldMappingData = []
          }
        } else {
          this.fieldMappingData = []
        }
      } catch (error) {
        this.fieldMappingData = []
      } finally {
        this.fieldMappingLoading = false
      }
    },

    // 添加字段映射行
    handleAddFieldMapping() {
      this.fieldMappingData.push({
        sourceField: '',
        sourceType: 'VARCHAR',
        targetField: '',
        targetType: 'VARCHAR',
        transformation: '',
        required: false
      })
    },

    // 删除字段映射行
    handleDeleteFieldMapping(index) {
      this.fieldMappingData.splice(index, 1)
    },

    // 保存字段映射
    async handleSaveFieldMapping() {
      this.fieldMappingLoading = true
      try {
        const response = await systemIntegrationApi.saveFieldMappings(
          this.fieldMappingErpId,
          JSON.stringify(this.fieldMappingData)
        )
        if (response.code === 1) {
          this.$message.success('字段映射保存成功')
          this.fieldMappingDialogVisible = false
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + (error.message || '未知错误'))
      } finally {
        this.fieldMappingLoading = false
      }
    },

    // 删除连接
    handleDelete(row) {
      this.$confirm('确认删除该连接？删除后不可恢复', '警告', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.erp.delete(row.erpId || row.id)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getErpConnectionList()
            this.getErpStats()
            this.getErpTypes()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {})
    },

    // 提交表单
    handleSubmitForm() {
      this.$refs.connectionForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const entityData = this.transformFormToEntity(this.connectionForm)
          let response
          if (this.connectionForm.erpId) {
            response = await systemIntegrationApi.erp.update(this.connectionForm.erpId, entityData)
          } else {
            response = await systemIntegrationApi.erp.create(entityData)
          }
          if (response.code === 1) {
            this.$message.success(this.connectionForm.erpId ? '更新成功' : '创建成功')
            this.dialogVisible = false
            this.getErpConnectionList()
            this.getErpStats()
            this.getErpTypes()
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败：' + (error.message || '未知错误'))
        } finally {
          this.submitLoading = false
        }
      })
    },

    // 测试连接（对话框内）
    handleTestConnectionForm() {
      if (this.connectionForm.erpId) {
        this.handleTestConnection({ id: this.connectionForm.erpId })
      } else {
        this.$message.info('请先保存连接后再测试')
      }
    },

    // 对话框关闭
    handleDialogClose() {
      this.$refs.connectionForm && this.$refs.connectionForm.resetFields()
    },

    // ===== 辅助方法 =====

    // 获取ERP类型颜色
    getErpTypeColor(type) {
      const colorMap = { 'SAP': 'primary', 'ORACLE': 'success', 'YONYOU': 'warning', 'KINGDEE': 'danger' }
      return colorMap[type] || 'info'
    },

    // 获取ERP类型文本
    getErpTypeText(type) {
      const textMap = { 'SAP': 'SAP ERP', 'ORACLE': 'Oracle ERP', 'YONYOU': '用友ERP', 'KINGDEE': '金蝶ERP' }
      return textMap[type] || type || '未知'
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = { 'CONNECTED': 'success', 'DISCONNECTED': 'danger', 'PENDING': 'warning' }
      return colorMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = { 'CONNECTED': '已连接', 'DISCONNECTED': '已断开', 'PENDING': '待连接' }
      return textMap[status] || status || '未知'
    },

    // 获取同步状态颜色
    getSyncStatusColor(status) {
      const colorMap = { 'SUCCESS': 'success', 'FAILED': 'danger', 'RUNNING': 'warning', 'PENDING': 'info' }
      return colorMap[status] || 'info'
    },

    // 获取同步状态文本
    getSyncStatusText(status) {
      const textMap = { 'SUCCESS': '成功', 'FAILED': '失败', 'RUNNING': '同步中', 'PENDING': '待同步' }
      return textMap[status] || status || '未知'
    },

    // 获取同步类型颜色
    getSyncTypeColor(type) {
      const colorMap = { 'FULL': 'primary', 'INCREMENTAL': 'success', 'MANUAL': 'warning' }
      return colorMap[type] || 'info'
    },

    // 获取同步类型文本
    getSyncTypeText(type) {
      const textMap = { 'FULL': '全量同步', 'INCREMENTAL': '增量同步', 'MANUAL': '手动同步' }
      return textMap[type] || type || '未知'
    },

    // 获取同步日志状态颜色
    getSyncLogStatusColor(status) {
      const colorMap = { 'SUCCESS': 'success', 'FAILED': 'danger', 'RUNNING': 'warning' }
      return colorMap[status] || 'info'
    },

    // 获取同步日志状态文本
    getSyncLogStatusText(status) {
      const textMap = { 'SUCCESS': '成功', 'FAILED': '失败', 'RUNNING': '运行中' }
      return textMap[status] || status || '未知'
    }
  }
}
</script>
