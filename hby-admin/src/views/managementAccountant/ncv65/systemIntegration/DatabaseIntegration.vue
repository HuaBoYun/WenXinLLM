<template>
  <div class="database-integration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>数据库集成</h2>
      <p>MySQL、Oracle、SQL Server、PostgreSQL等异构数据库系统集成和数据同步</p>
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
            <el-button icon="el-icon-setting" @click="handleSettings">连接设置</el-button>
            <el-button icon="el-icon-document" @click="handleLogs">同步日志</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据库集成统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dbStats.totalConnections }}</div>
            <div class="stat-label">数据库连接</div>
            <div class="stat-description">已配置数据库连接</div>
            <div class="stat-trend">
              <i class="el-icon-coin"></i>
              <span>数据库</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-coin"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dbStats.activeConnections }}</div>
            <div class="stat-label">活跃连接</div>
            <div class="stat-description">正在使用的连接</div>
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
            <div class="stat-number">{{ dbStats.todaySync }}</div>
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
        <el-card class="stat-card performance-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dbStats.avgResponseTime }}ms</div>
            <div class="stat-label">平均响应</div>
            <div class="stat-description">平均查询响应时间</div>
            <div class="stat-trend">
              <i class="el-icon-timer"></i>
              <span>高性能</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-timer"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据库类型选择 -->
    <el-card class="db-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>数据库类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshDbTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="dbType in dbTypes" :key="dbType.id">
          <el-card 
            class="db-type-item" 
            shadow="hover" 
            @click.native="handleSelectDbType(dbType)"
            :class="{ 'selected': selectedDbType === dbType.id }"
          >
            <div class="db-type-icon">
              <i :class="dbType.icon"></i>
            </div>
            <div class="db-type-title">{{ dbType.name }}</div>
            <div class="db-type-description">{{ dbType.description }}</div>
            <div class="db-type-stats">
              <span class="connection-count">{{ dbType.connectionCount }} 个连接</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据库连接列表 -->
    <el-card class="db-connections-card" shadow="never">
      <div slot="header" class="card-header">
        <span>数据库连接管理</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索连接"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getDbConnectionList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="filteredDbConnectionList"
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
        <el-table-column prop="dbType" label="数据库类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getDbTypeColor(scope.row.dbType)" size="mini">
              {{ getDbTypeText(scope.row.dbType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="host" label="主机地址" width="150" show-overflow-tooltip />
        <el-table-column prop="port" label="端口" width="80" align="center" />
        <el-table-column prop="database" label="数据库" width="120" align="center" />
        <el-table-column prop="connectionPool" label="连接池" width="100" align="center">
          <template slot-scope="scope">
            <span class="connection-pool">{{ scope.row.activeConnections }}/{{ scope.row.maxConnections }}</span>
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
                <el-dropdown-item command="query">查询工具</el-dropdown-item>
                <el-dropdown-item command="schema">表结构</el-dropdown-item>
                <el-dropdown-item command="monitor">性能监控</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 连接详情抽屉 -->
    <el-drawer
      title="数据库连接详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentConnection">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="连接基本信息" :column="2" border>
              <el-descriptions-item label="连接名称">{{ currentConnection.connectionName }}</el-descriptions-item>
              <el-descriptions-item label="数据库类型">{{ getDbTypeText(currentConnection.dbType) }}</el-descriptions-item>
              <el-descriptions-item label="主机地址">{{ currentConnection.host }}</el-descriptions-item>
              <el-descriptions-item label="端口">{{ currentConnection.port }}</el-descriptions-item>
              <el-descriptions-item label="数据库">{{ currentConnection.database }}</el-descriptions-item>
              <el-descriptions-item label="用户名">{{ currentConnection.username }}</el-descriptions-item>
              <el-descriptions-item label="连接池">{{ currentConnection.activeConnections }}/{{ currentConnection.maxConnections }}</el-descriptions-item>
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
          <el-tab-pane label="表结构" name="schema">
            <el-table :data="dbTables" border size="mini">
              <el-table-column prop="tableName" label="表名" width="200" />
              <el-table-column prop="tableType" label="表类型" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getTableTypeColor(scope.row.tableType)" size="mini">
                    {{ getTableTypeText(scope.row.tableType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="rowCount" label="行数" width="100" align="center" />
              <el-table-column prop="dataSize" label="数据大小" width="120" align="center" />
              <el-table-column prop="lastUpdate" label="最后更新" width="150" />
              <el-table-column prop="comment" label="备注" />
              <el-table-column label="操作" width="120" align="center">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" @click="$alert(`表名: ${scope.row.tableName}\n行数: ${scope.row.rowCount || '-'}\n大小: ${scope.row.dataSize || '-'}\n备注: ${scope.row.comment || '无'}`, '表详情', { confirmButtonText: '关闭' })">
                    查看
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="同步配置" name="config">
            <el-form label-width="120px" size="small">
              <el-form-item label="同步频率">
                <el-input :value="currentConnection.syncFrequency" readonly />
              </el-form-item>
              <el-form-item label="同步方向">
                <el-input :value="currentConnection.syncDirection" readonly />
              </el-form-item>
              <el-form-item label="同步表">
                <el-input :value="currentConnection.syncTables" readonly type="textarea" :rows="3" />
              </el-form-item>
              <el-form-item label="过滤条件">
                <el-input :value="currentConnection.filterConditions" readonly type="textarea" :rows="3" />
              </el-form-item>
              <el-form-item label="连接池配置">
                <el-input :value="currentConnection.poolConfig" readonly type="textarea" :rows="2" />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="性能监控" name="monitor">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card class="monitor-card">
                  <div slot="header">连接数趋势</div>
                  <div class="chart-container">
                    <!-- 这里可以集成ECharts图表 -->
                    <div class="chart-placeholder">连接数趋势图表</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card class="monitor-card">
                  <div slot="header">查询响应时间</div>
                  <div class="chart-container">
                    <!-- 这里可以集成ECharts图表 -->
                    <div class="chart-placeholder">响应时间图表</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="24">
                <el-card class="monitor-card">
                  <div slot="header">慢查询日志</div>
                  <el-table :data="slowQueries" border size="mini">
                    <el-table-column prop="queryTime" label="查询时间" width="150" />
                    <el-table-column prop="duration" label="执行时间" width="100" align="center">
                      <template slot-scope="scope">
                        <span :class="getDurationClass(scope.row.duration)">
                          {{ scope.row.duration }}ms
                        </span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="queryType" label="查询类型" width="100" align="center" />
                    <el-table-column prop="affectedRows" label="影响行数" width="100" align="center" />
                    <el-table-column prop="sqlStatement" label="SQL语句" />
                  </el-table>
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
            <el-form-item label="数据库类型" prop="dbType">
              <el-select v-model="connectionForm.dbType" placeholder="请选择数据库类型" style="width: 100%">
                <el-option value="MYSQL" label="MySQL" />
                <el-option value="ORACLE" label="Oracle" />
                <el-option value="SQLSERVER" label="SQL Server" />
                <el-option value="POSTGRESQL" label="PostgreSQL" />
                <el-option value="DAMENG" label="达梦数据库" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="主机地址" prop="host">
              <el-input v-model="connectionForm.host" placeholder="请输入主机地址" />
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

    <!-- 连接测试弹窗 -->
    <el-dialog title="连接测试" :visible.sync="testDialogVisible" width="700px" :close-on-click-modal="false">
      <div style="margin-bottom: 16px;">
        <el-button type="primary" size="small" @click="handleBatchTest" :loading="testLoading">测试所有连接</el-button>
      </div>
      <el-table :data="testResults" border size="mini" v-loading="testLoading">
        <el-table-column prop="connectionName" label="连接名称" width="180" />
        <el-table-column prop="dbType" label="数据库类型" width="120" align="center">
          <template slot-scope="scope"><el-tag size="mini" :type="getDbTypeColor(scope.row.dbType)">{{ getDbTypeText(scope.row.dbType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="success" label="测试结果" width="100" align="center">
          <template slot-scope="scope"><el-tag size="mini" :type="scope.row.success ? 'success' : 'danger'">{{ scope.row.success ? '成功' : '失败' }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="responseTime" label="响应时间" width="100" align="center" />
        <el-table-column prop="message" label="消息" />
      </el-table>
      <div slot="footer"><el-button @click="testDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 数据同步弹窗 -->
    <el-dialog title="数据同步" :visible.sync="syncDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="100px" size="small">
        <el-form-item label="选择连接">
          <el-select v-model="syncSelectedId" placeholder="请选择要同步的连接" style="width: 100%">
            <el-option v-for="conn in dbConnectionList" :key="conn.id" :label="conn.connectionName" :value="conn.id">
              <span>{{ conn.connectionName }}</span>
              <el-tag size="mini" :type="getDbTypeColor(conn.dbType)" style="margin-left: 8px;">{{ getDbTypeText(conn.dbType) }}</el-tag>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div v-if="syncResult" style="margin-top: 16px;">
        <el-alert :title="syncResult.message" :type="syncResult.success ? 'success' : 'error'" show-icon :closable="false" />
        <div v-if="syncResult.success" style="margin-top: 8px; color: #909399; font-size: 13px;">
          <span v-if="syncResult.syncedRecords !== undefined">同步记录数: {{ syncResult.syncedRecords }}</span>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="syncDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleDoSync" :loading="syncLoading">开始同步</el-button>
      </div>
    </el-dialog>

    <!-- 连接设置弹窗 -->
    <el-dialog title="连接设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="settingsForm" label-width="140px" size="small" v-loading="settingsLoading">
        <el-form-item label="默认连接池大小"><el-input-number v-model="settingsForm.defaultPoolSize" :min="1" :max="100" /></el-form-item>
        <el-form-item label="最大连接池大小"><el-input-number v-model="settingsForm.maxPoolSize" :min="1" :max="200" /></el-form-item>
        <el-form-item label="连接超时(ms)"><el-input-number v-model="settingsForm.connectionTimeout" :min="1000" :max="120000" :step="1000" /></el-form-item>
        <el-form-item label="空闲超时(ms)"><el-input-number v-model="settingsForm.idleTimeout" :min="10000" :max="3600000" :step="10000" /></el-form-item>
        <el-form-item label="最大生命周期(ms)"><el-input-number v-model="settingsForm.maxLifetime" :min="60000" :max="7200000" :step="60000" /></el-form-item>
        <el-form-item label="自动重连"><el-switch v-model="settingsForm.autoReconnect" /></el-form-item>
        <el-form-item label="启用SSL"><el-switch v-model="settingsForm.sslEnabled" /></el-form-item>
        <el-form-item label="记录慢查询"><el-switch v-model="settingsForm.logSlowQuery" /></el-form-item>
        <el-form-item label="慢查询阈值(ms)"><el-input-number v-model="settingsForm.slowQueryThreshold" :min="100" :max="30000" :step="100" /></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSettings" :loading="settingsLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 同步日志弹窗 -->
    <el-dialog title="同步日志" :visible.sync="logsDialogVisible" width="900px" :close-on-click-modal="false">
      <el-table :data="syncLogs" border size="mini" v-loading="logsLoading" max-height="400">
        <el-table-column prop="connectionName" label="连接名称" width="160" show-overflow-tooltip />
        <el-table-column prop="dbType" label="数据库类型" width="100" align="center">
          <template slot-scope="scope"><el-tag size="mini" :type="getDbTypeColor(scope.row.dbType)">{{ getDbTypeText(scope.row.dbType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="syncTime" label="同步时间" width="160" align="center" />
        <el-table-column prop="syncMode" label="同步模式" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.syncMode === 'FULL' ? '全量' : '增量' }}</template>
        </el-table-column>
        <el-table-column prop="syncRecords" label="同步记录数" width="100" align="center" />
        <el-table-column prop="duration" label="耗时" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope"><el-tag size="mini" :type="getSyncStatusColor(scope.row.status)">{{ getSyncStatusText(scope.row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="successCount" label="成功次数" width="80" align="center" />
      </el-table>
      <div slot="footer"><el-button @click="logsDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="数据库集成帮助" :visible.sync="helpDialogVisible" width="700px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>数据库集成管理模块支持多种异构数据库系统的连接管理、数据同步和监控。</p>
        <h4>支持的数据库类型</h4>
        <ul><li>MySQL - 开源关系型数据库</li><li>Oracle - 企业级关系型数据库</li><li>SQL Server - 微软关系型数据库</li><li>PostgreSQL - 开源对象关系型数据库</li><li>达梦数据库 - 国产关系型数据库</li></ul>
        <h4>操作指南</h4>
        <ul><li><b>创建连接</b>：点击"创建连接"按钮，填写数据库连接信息</li><li><b>连接测试</b>：验证数据库连接是否正常</li><li><b>数据同步</b>：将源数据库数据同步到目标系统</li><li><b>查询工具</b>：在线执行SQL查询（仅支持SELECT）</li><li><b>表结构</b>：查看数据库中的表结构信息</li><li><b>性能监控</b>：监控数据库连接性能指标</li></ul>
        <h4>注意事项</h4>
        <ul><li>请确保数据库服务器网络可达</li><li>建议定期测试连接状态</li><li>同步操作请在业务低峰期执行</li><li>查询工具仅支持SELECT语句，禁止执行修改操作</li></ul>
      </div>
      <div slot="footer"><el-button type="primary" @click="helpDialogVisible = false">知道了</el-button></div>
    </el-dialog>

    <!-- 查询工具弹窗 -->
    <el-dialog :title="'查询工具 - ' + queryConnectionName" :visible.sync="queryDialogVisible" width="900px" :close-on-click-modal="false">
      <el-form size="small">
        <el-form-item label="SQL语句">
          <el-input v-model="querySql" type="textarea" :rows="4" placeholder="请输入SELECT查询语句" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="handleExecuteQuery" :loading="queryLoading" icon="el-icon-caret-right">执行查询</el-button>
        </el-form-item>
      </el-form>
      <div v-if="queryResult" style="margin-top: 8px;">
        <div style="margin-bottom: 8px; color: #909399; font-size: 13px;">
          共 {{ queryResult.totalRows }} 条记录，耗时 {{ queryResult.executionTime }}
        </div>
        <el-table :data="queryResult.rows" border size="mini" max-height="300">
          <el-table-column v-for="col in (queryResult.columns || [])" :key="col" :prop="col" :label="col" min-width="120" show-overflow-tooltip />
        </el-table>
      </div>
      <div slot="footer"><el-button @click="queryDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 表结构弹窗 -->
    <el-dialog :title="'表结构 - ' + schemaConnectionName" :visible.sync="schemaDialogVisible" width="900px" :close-on-click-modal="false">
      <el-table :data="schemaTables" border size="mini" v-loading="schemaLoading" max-height="400">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="tableName" label="表名" width="220" show-overflow-tooltip />
        <el-table-column prop="tableType" label="类型" width="80" align="center">
          <template slot-scope="scope"><el-tag size="mini" :type="getTableTypeColor(scope.row.tableType)">{{ getTableTypeText(scope.row.tableType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="columnCount" label="字段数" width="80" align="center" />
        <el-table-column prop="rowCount" label="行数" width="100" align="center" />
        <el-table-column prop="dataSize" label="数据大小" width="100" align="center" />
        <el-table-column prop="comment" label="备注" />
      </el-table>
      <div slot="footer"><el-button @click="schemaDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 性能监控弹窗 -->
    <el-dialog :title="'性能监控 - ' + monitorConnectionName" :visible.sync="monitorDialogVisible" width="800px" :close-on-click-modal="false">
      <div v-loading="monitorLoading">
        <div v-if="monitorData">
          <el-row :gutter="16" style="margin-bottom: 16px;">
            <el-col :span="6"><el-card shadow="never"><div class="monitor-stat"><div class="monitor-stat-value">{{ monitorData.activeConnections }}/{{ monitorData.maxConnections }}</div><div class="monitor-stat-label">活跃连接</div></div></el-card></el-col>
            <el-col :span="6"><el-card shadow="never"><div class="monitor-stat"><div class="monitor-stat-value">{{ monitorData.avgResponseTime }}</div><div class="monitor-stat-label">平均响应</div></div></el-card></el-col>
            <el-col :span="6"><el-card shadow="never"><div class="monitor-stat"><div class="monitor-stat-value">{{ monitorData.totalQueries }}</div><div class="monitor-stat-label">总查询数</div></div></el-card></el-col>
            <el-col :span="6"><el-card shadow="never"><div class="monitor-stat"><div class="monitor-stat-value">{{ monitorData.cacheHitRate }}</div><div class="monitor-stat-label">缓存命中率</div></div></el-card></el-col>
          </el-row>
          <el-descriptions title="连接信息" :column="2" border size="mini" style="margin-bottom: 16px;">
            <el-descriptions-item label="数据库类型">{{ getDbTypeText(monitorData.dbType) }}</el-descriptions-item>
            <el-descriptions-item label="运行时间">{{ monitorData.uptime }}</el-descriptions-item>
            <el-descriptions-item label="慢查询数">{{ monitorData.slowQueries }}</el-descriptions-item>
            <el-descriptions-item label="最后检查">{{ monitorData.lastCheck }}</el-descriptions-item>
          </el-descriptions>
          <h4 style="margin: 12px 0 8px;">慢查询日志</h4>
          <el-table :data="monitorData.slowQueryList || []" border size="mini" max-height="200">
            <el-table-column prop="queryTime" label="时间" width="160" />
            <el-table-column prop="duration" label="耗时(ms)" width="100" align="center">
              <template slot-scope="scope"><span :class="getDurationClass(scope.row.duration)">{{ scope.row.duration }}</span></template>
            </el-table-column>
            <el-table-column prop="queryType" label="类型" width="80" align="center" />
            <el-table-column prop="affectedRows" label="影响行数" width="100" align="center" />
            <el-table-column prop="sqlStatement" label="SQL语句" show-overflow-tooltip />
          </el-table>
        </div>
        <el-empty v-else description="暂无性能数据" />
      </div>
      <div slot="footer"><el-button @click="monitorDialogVisible = false">关闭</el-button></div>
    </el-dialog>

  </div>
</template>

<script>
import { systemIntegrationApi } from '@/api/managementAccountant/ncv65/systemIntegration'

export default {
  name: 'DatabaseIntegration',
  data() {
    return {
      // 统计数据
      dbStats: {
        totalConnections: 0,
        activeConnections: 0,
        todaySync: 0,
        avgResponseTime: 0
      },

      // 数据库类型
      dbTypes: [],
      selectedDbType: null,
      
      // 连接列表
      dbConnectionList: [],
      loading: false,
      searchKeyword: '',
      
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentConnection: null,
      dbTables: [],
      slowQueries: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      
      // 表单数据
      connectionForm: {
        connectionName: '',
        dbType: '',
        host: '',
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
        dbType: [
          { required: true, message: '请选择数据库类型', trigger: 'change' }
        ],
        host: [
          { required: true, message: '请输入主机地址', trigger: 'blur' }
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
      },

      // 连接测试弹窗
      testDialogVisible: false,
      testLoading: false,
      testResults: [],

      // 数据同步弹窗
      syncDialogVisible: false,
      syncLoading: false,
      syncSelectedId: '',
      syncResult: null,

      // 连接设置弹窗
      settingsDialogVisible: false,
      settingsLoading: false,
      settingsForm: { defaultPoolSize: 20, maxPoolSize: 50, connectionTimeout: 30000, idleTimeout: 600000, maxLifetime: 1800000, autoReconnect: true, sslEnabled: false, logSlowQuery: true, slowQueryThreshold: 1000 },

      // 同步日志弹窗
      logsDialogVisible: false,
      logsLoading: false,
      syncLogs: [],

      // 帮助弹窗
      helpDialogVisible: false,

      // 查询工具弹窗
      queryDialogVisible: false,
      queryLoading: false,
      queryConnectionId: '',
      queryConnectionName: '',
      querySql: '',
      queryResult: null,

      // 表结构弹窗
      schemaDialogVisible: false,
      schemaLoading: false,
      schemaConnectionName: '',
      schemaTables: [],

      // 性能监控弹窗
      monitorDialogVisible: false,
      monitorLoading: false,
      monitorConnectionName: '',
      monitorData: null
    }
  },
  
  computed: {
    filteredDbConnectionList() {
      let list = this.dbConnectionList
      // 按数据库类型筛选
      if (this.selectedDbType) {
        list = list.filter(item => item.dbType === this.selectedDbType)
      }
      // 按关键字搜索
      if (this.searchKeyword) {
        const kw = this.searchKeyword.toLowerCase()
        list = list.filter(item =>
        (item.connectionName && item.connectionName.toLowerCase().includes(kw)) ||
        (item.dbType && item.dbType.toLowerCase().includes(kw)) ||
        (item.host && item.host.toLowerCase().includes(kw)) ||
        (item.status && item.status.toLowerCase().includes(kw))
        )
      }
      return list
    }
  },

  created() {
    this.getDbConnectionList()
    this.getDbStats()
    this.getDbTypes()
  },

  methods: {
    async getDbConnectionList() {
      this.loading = true
      try {
        const response = await systemIntegrationApi.getDbConnectionList()
        if (response.code === 1 && response.data) { this.dbConnectionList = response.data }
      } catch (error) {
        this.$message.error('获取连接列表失败：' + error.message)
      } finally { this.loading = false }
    },
    async getDbStats() {
      try {
        const response = await systemIntegrationApi.getDbStats()
        if (response.code === 1 && response.data) { this.dbStats = response.data }
      } catch (error) { console.error('获取统计数据失败：', error) }
    },
    async getDbTypes() {
      try {
        const response = await systemIntegrationApi.getDbConnectionList()
        if (response.code === 1 && response.data) {
          const typeMap = {}
          response.data.forEach(item => {
            const t = item.dbType || 'OTHER'
            if (!typeMap[t]) { typeMap[t] = { id: t, name: this.getDbTypeText(t), description: t + '数据库', icon: 'el-icon-coin', connectionCount: 0 } }
            typeMap[t].connectionCount++
          })
          this.dbTypes = Object.values(typeMap)
        }
      } catch (error) { console.error('获取数据库类型失败：', error) }
    },
    refreshDbTypes() { this.getDbTypes(); this.$message.success('数据库类型已刷新') },
    handleSelectDbType(dbType) { this.selectedDbType = this.selectedDbType === dbType.id ? null : dbType.id },
    handleCreateConnection() {
      this.dialogTitle = '创建数据库连接'
      this.connectionForm = { connectionName: '', dbType: '', host: '', port: '', database: '', username: '', password: '', description: '' }
      this.dialogVisible = true
    },
    handleRefresh() { this.getDbConnectionList(); this.getDbStats(); this.getDbTypes(); this.$message.success('数据已刷新') },
    // ===== 连接测试弹窗 =====
    handleTestConnection(row) {
      if (row && row.id) {
        this.doTestConnection(row)
      } else {
        // 工具栏按钮：弹窗选择连接
        if (this.dbConnectionList.length === 0) { this.$message.warning('暂无可测试的连接'); return }
        this.testDialogVisible = true
        this.testResults = []
      }
    },
    async doTestConnection(row) {
      this.testLoading = true
      try {
        const response = await systemIntegrationApi.database.test(row.id)
        const result = { connectionName: row.connectionName, dbType: row.dbType, success: false, message: '', responseTime: '' }
        if (response.code === 1 && response.data) {
          result.success = response.data.success
          result.message = response.data.message || '连接测试成功'
          result.responseTime = response.data.responseTime || '-'
        } else {
          result.message = response.msg || '连接测试失败'
        }
        this.testResults = [result]
        this.testDialogVisible = true
        this.getDbConnectionList()
      } catch (error) {
        this.$message.error('连接测试失败：' + error.message)
      } finally { this.testLoading = false }
    },
    async handleBatchTest() {
      this.testLoading = true
      this.testResults = []
      try {
        for (const conn of this.dbConnectionList) {
          const response = await systemIntegrationApi.database.test(conn.id)
          const result = { connectionName: conn.connectionName, dbType: conn.dbType, success: false, message: '', responseTime: '' }
          if (response.code === 1 && response.data) {
            result.success = response.data.success
            result.message = response.data.message || '连接测试成功'
            result.responseTime = response.data.responseTime || '-'
          } else { result.message = response.msg || '测试失败' }
          this.testResults.push(result)
        }
        this.getDbConnectionList()
      } catch (error) { this.$message.error('批量测试失败：' + error.message) }
      finally { this.testLoading = false }
    },
    // ===== 数据同步弹窗 =====
    handleSyncData() {
      if (this.dbConnectionList.length === 0) { this.$message.warning('暂无可同步的连接'); return }
      this.syncDialogVisible = true
      this.syncSelectedId = ''
      this.syncResult = null
    },
    handleSync(row) {
      if (row && row.id) {
        this.$confirm('确认同步该连接的数据？', '提示', { type: 'warning' }).then(async () => {
          try {
            const response = await systemIntegrationApi.database.sync(row.id)
            if (response.code === 1) { this.$message.success('数据同步成功'); this.getDbConnectionList(); this.getDbStats() } else { this.$message.error(response.msg || '同步失败') }
          } catch (error) { this.$message.error('同步失败：' + error.message) }
        }).catch(() => {})
      }
    },
    async handleDoSync() {
      if (!this.syncSelectedId) { this.$message.warning('请选择要同步的连接'); return }
      this.syncLoading = true
      try {
        const response = await systemIntegrationApi.database.sync(this.syncSelectedId)
        if (response.code === 1) {
          this.syncResult = { success: true, message: '同步成功', ...(response.data || {}) }
          this.getDbConnectionList(); this.getDbStats()
        } else { this.syncResult = { success: false, message: response.msg || '同步失败' } }
      } catch (error) { this.syncResult = { success: false, message: '同步失败：' + error.message } }
      finally { this.syncLoading = false }
    },
    // ===== 连接设置弹窗 =====
    async handleSettings() {
      this.settingsDialogVisible = true
      this.settingsLoading = true
      try {
        const response = await systemIntegrationApi.database.settings()
        if (response.code === 1 && response.data) { this.settingsForm = { ...response.data } }
      } catch (error) { this.$message.error('获取设置失败：' + error.message) }
      finally { this.settingsLoading = false }
    },
    async handleSaveSettings() {
      this.settingsLoading = true
      try {
        const response = await systemIntegrationApi.database.updateSettings(this.settingsForm)
        if (response.code === 1) { this.$message.success('设置保存成功'); this.settingsDialogVisible = false }
        else { this.$message.error(response.msg || '保存失败') }
      } catch (error) { this.$message.error('保存失败：' + error.message) }
      finally { this.settingsLoading = false }
    },
    // ===== 同步日志弹窗 =====
    async handleLogs() {
      this.logsDialogVisible = true
      this.logsLoading = true
      try {
        const response = await systemIntegrationApi.database.syncLogs()
        if (response.code === 1 && response.data) { this.syncLogs = response.data }
      } catch (error) { this.$message.error('获取日志失败：' + error.message) }
      finally { this.logsLoading = false }
    },
    // ===== 帮助弹窗 =====
    handleHelp() { this.helpDialogVisible = true },
    handleView(row) { this.currentConnection = row; this.detailDrawerVisible = true; this.detailActiveTab = 'basic' },
    handleRowClick(row) { this.currentConnection = row },
    handleMoreAction(command, row) {
      const actions = {
        'edit': () => this.handleEdit(row),
        'query': () => this.handleQuery(row),
        'schema': () => this.handleSchema(row),
        'monitor': () => this.handleMonitor(row),
        'delete': () => this.handleDelete(row)
      }
      if (actions[command]) actions[command]()
    },
    handleEdit(row) { this.dialogTitle = '编辑数据库连接'; this.connectionForm = { ...row }; this.dialogVisible = true },
    handleDelete(row) {
      this.$confirm('确认删除该连接？', '警告', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.database.delete(row.id)
          if (response.code === 1) { this.$message.success('删除成功'); this.getDbConnectionList(); this.getDbStats() } else { this.$message.error(response.msg || '删除失败') }
        } catch (error) { this.$message.error('删除失败：' + error.message) }
      }).catch(() => {})
    },
    handleSubmitForm() {
      this.$refs.connectionForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          let response
          if (this.connectionForm.id) { response = await systemIntegrationApi.database.update(this.connectionForm.id, this.connectionForm) } else { response = await systemIntegrationApi.database.create(this.connectionForm) }
          if (response.code === 1) { this.$message.success(this.connectionForm.id ? '更新成功' : '创建成功'); this.dialogVisible = false; this.getDbConnectionList(); this.getDbStats() } else { this.$message.error(response.msg || '操作失败') }
        } catch (error) { this.$message.error('操作失败：' + error.message) } finally { this.submitLoading = false }
      })
    },
    handleTestConnectionForm() {
      if (this.connectionForm.id) { this.handleTestConnection(this.connectionForm) } else { this.$message.info('请先保存连接后再测试') }
    },
    handleDialogClose() { this.$refs.connectionForm && this.$refs.connectionForm.resetFields() },
    // ===== 查询工具弹窗 =====
    handleQuery(row) {
      this.queryDialogVisible = true
      this.queryConnectionId = row.id
      this.queryConnectionName = row.connectionName
      this.querySql = 'SELECT * FROM TBL_BUDGET_MAIN WHERE ROWNUM <= 10'
      this.queryResult = null
    },
    async handleExecuteQuery() {
      if (!this.querySql || !this.querySql.trim()) { this.$message.warning('请输入SQL语句'); return }
      if (!this.querySql.trim().toUpperCase().startsWith('SELECT')) { this.$message.error('仅支持SELECT查询'); return }
      this.queryLoading = true
      try {
        const response = await systemIntegrationApi.database.query(this.queryConnectionId, { sql: this.querySql })
        if (response.code === 1 && response.data) { this.queryResult = response.data }
        else { this.$message.error(response.msg || '查询失败') }
      } catch (error) { this.$message.error('查询失败：' + error.message) }
      finally { this.queryLoading = false }
    },
    // ===== 表结构弹窗 =====
    async handleSchema(row) {
      this.schemaDialogVisible = true
      this.schemaConnectionName = row.connectionName
      this.schemaLoading = true
      this.schemaTables = []
      try {
        const response = await systemIntegrationApi.database.tables(row.id)
        if (response.code === 1 && response.data) { this.schemaTables = response.data }
      } catch (error) { this.$message.error('获取表结构失败：' + error.message) }
      finally { this.schemaLoading = false }
    },
    // ===== 性能监控弹窗 =====
    async handleMonitor(row) {
      this.monitorDialogVisible = true
      this.monitorConnectionName = row.connectionName
      this.monitorLoading = true
      this.monitorData = null
      try {
        const response = await systemIntegrationApi.database.performance(row.id)
        if (response.code === 1 && response.data) { this.monitorData = response.data }
      } catch (error) { this.$message.error('获取性能数据失败：' + error.message) }
      finally { this.monitorLoading = false }
    },
    // 辅助方法
    getDbTypeColor(type) { const m = { 'MYSQL': 'primary', 'ORACLE': 'success', 'SQLSERVER': 'warning', 'POSTGRESQL': 'danger', 'DM': 'info' }; return m[type] || 'info' },
    getDbTypeText(type) { const m = { 'MYSQL': 'MySQL', 'ORACLE': 'Oracle', 'SQLSERVER': 'SQL Server', 'POSTGRESQL': 'PostgreSQL', 'DM': '达梦数据库', 'DAMENG': '达梦数据库' }; return m[type] || type || '未知' },
    getStatusColor(status) { const m = { 'CONNECTED': 'success', 'DISCONNECTED': 'danger', 'PENDING': 'warning' }; return m[status] || 'info' },
    getStatusText(status) { const m = { 'CONNECTED': '已连接', 'DISCONNECTED': '已断开', 'PENDING': '待连接', 'ACTIVE': '已连接', 'INACTIVE': '已断开', 'ERROR': '错误', 'TESTING': '测试中' }; return m[status] || status || '未知' },
    getTableTypeColor(type) { return type === 'VIEW' ? 'warning' : 'primary' },
    getTableTypeText(type) { return type === 'VIEW' ? '视图' : '表' },
    getDurationClass(duration) { return duration > 3000 ? 'text-danger' : (duration > 1000 ? 'text-warning' : 'text-success') },
    getSyncStatusText(status) { return status === 'SUCCESS' ? '成功' : (status === 'FAILURE' ? '失败' : '待处理') },
    getSyncStatusColor(status) { return status === 'SUCCESS' ? 'success' : (status === 'FAILURE' ? 'danger' : 'warning') }
  }
}
</script>

<style scoped>
.monitor-stat { text-align: center; padding: 8px 0; }
.monitor-stat-value { font-size: 24px; font-weight: bold; color: #409EFF; line-height: 1.4; }
.monitor-stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
.help-content h4 { margin: 16px 0 8px; color: #303133; }
.help-content p { color: #606266; line-height: 1.8; margin: 0 0 8px; }
.help-content ul { padding-left: 20px; color: #606266; line-height: 2; }
.text-danger { color: #F56C6C; font-weight: bold; }
.text-warning { color: #E6A23C; }
.text-success { color: #67C23A; }
.text-right { text-align: right; }
.connection-pool { font-family: monospace; }
.last-sync { font-size: 12px; color: #909399; }
.detail-content { padding: 0 20px 20px; }
.monitor-card { margin-bottom: 16px; }
.chart-container { height: 200px; display: flex; align-items: center; justify-content: center; }
.chart-placeholder { color: #C0C4CC; font-size: 14px; }
</style>
