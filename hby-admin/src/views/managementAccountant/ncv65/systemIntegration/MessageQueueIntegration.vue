<template>
  <div class="message-queue-integration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>消息队列集成</h2>
      <p>RabbitMQ、Apache Kafka、ActiveMQ等消息队列系统集成，支持异步消息处理</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateQueue">创建队列</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-connection" @click="handleTestConnection">连接测试</el-button>
            <el-button type="info" icon="el-icon-message" @click="handleSendMessage">发送消息</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">队列设置</el-button>
            <el-button icon="el-icon-monitor" @click="handleMonitor">监控中心</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 消息队列统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ mqStats.totalQueues }}</div>
            <div class="stat-label">队列总数</div>
            <div class="stat-description">已配置消息队列数</div>
            <div class="stat-trend">
              <i class="el-icon-message"></i>
              <span>消息队列</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-message"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ mqStats.activeQueues }}</div>
            <div class="stat-label">活跃队列</div>
            <div class="stat-description">正在运行的队列</div>
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
        <el-card class="stat-card messages-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ mqStats.todayMessages }}</div>
            <div class="stat-label">今日消息</div>
            <div class="stat-description">今日处理消息数</div>
            <div class="stat-trend">
              <i class="el-icon-chat-dot-round"></i>
              <span>消息处理</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-chat-dot-round"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card throughput-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ mqStats.avgThroughput }}</div>
            <div class="stat-label">平均吞吐量</div>
            <div class="stat-description">消息/秒</div>
            <div class="stat-trend">
              <i class="el-icon-odometer"></i>
              <span>高吞吐</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-odometer"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 消息队列类型选择 -->
    <el-card class="mq-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>消息队列类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshMqTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="mqType in mqTypes" :key="mqType.id">
          <el-card
            class="mq-type-item"
            shadow="hover"
            @click.native="handleSelectMqType(mqType)"
            :class="{ 'selected': selectedMqType === mqType.id }"
          >
            <div class="mq-type-icon">
              <i :class="mqType.icon"></i>
            </div>
            <div class="mq-type-title">{{ mqType.name }}</div>
            <div class="mq-type-description">{{ mqType.description }}</div>
            <div class="mq-type-stats">
              <span class="queue-count">{{ mqType.queueCount }} 个队列</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 消息队列列表 -->
    <el-card class="mq-list-card" shadow="never">
      <div slot="header" class="card-header">
        <span>消息队列管理</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索队列"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getMqList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="filteredMqList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="queueName" label="队列名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.queueName || scope.row.name }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="mqType" label="队列类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getMqTypeColor(scope.row.mqType)" size="mini">
              {{ getMqTypeText(scope.row.mqType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="messageCount" label="消息数量" width="100" align="center">
          <template slot-scope="scope">
            <span class="message-count">{{ scope.row.messageCount != null ? scope.row.messageCount : 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="consumerCount" label="消费者数" width="100" align="center">
          <template slot-scope="scope">
            <span class="consumer-count">{{ scope.row.consumerCount != null ? scope.row.consumerCount : 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="throughput" label="吞吐量" width="100" align="center">
          <template slot-scope="scope">
            <span class="throughput">{{ scope.row.throughput != null ? scope.row.throughput : 0 }}/s</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastMessage" label="最后消息" width="170" align="center">
          <template slot-scope="scope">
            <span class="last-message">{{ scope.row.lastMessage ? formatTime(scope.row.lastMessage) : '暂无' }}</span>
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
              icon="el-icon-message"
              @click="handleSendMessage(scope.row)"
            >发送</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleViewMessages(scope.row)"
            >消息</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="edit">编辑</el-dropdown-item>
                <el-dropdown-item command="start">启动</el-dropdown-item>
                <el-dropdown-item command="stop">停止</el-dropdown-item>
                <el-dropdown-item command="purge">清空队列</el-dropdown-item>
                <el-dropdown-item command="monitor">监控</el-dropdown-item>
                <el-dropdown-item command="consumers">消费者</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 队列详情抽屉 -->
    <el-drawer
      title="消息队列详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentQueue">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="队列基本信息" :column="2" border>
              <el-descriptions-item label="队列名称">{{ currentQueue.queueName }}</el-descriptions-item>
              <el-descriptions-item label="队列类型">{{ getMqTypeText(currentQueue.mqType) }}</el-descriptions-item>
              <el-descriptions-item label="消息数量">{{ currentQueue.messageCount }}</el-descriptions-item>
              <el-descriptions-item label="消费者数">{{ currentQueue.consumerCount }}</el-descriptions-item>
              <el-descriptions-item label="吞吐量">{{ currentQueue.throughput }}/s</el-descriptions-item>
              <el-descriptions-item label="最大长度">{{ currentQueue.maxLength }}</el-descriptions-item>
              <el-descriptions-item label="最后消息">{{ currentQueue.lastMessage }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentQueue.status)" size="mini">
                  {{ getStatusText(currentQueue.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentQueue.createTime }}</el-descriptions-item>
              <el-descriptions-item label="队列描述" :span="2">{{ currentQueue.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="消息列表" name="messages">
            <el-table :data="queueMessages" border size="mini">
              <el-table-column prop="messageId" label="消息ID" width="200" />
              <el-table-column prop="messageType" label="消息类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getMessageTypeColor(scope.row.messageType)" size="mini">
                    {{ getMessageTypeText(scope.row.messageType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="priority" label="优先级" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getPriorityColor(scope.row.priority)" size="mini">
                    {{ scope.row.priority }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="payload" label="消息内容" show-overflow-tooltip />
              <el-table-column prop="createTime" label="创建时间" width="150" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getMessageStatusColor(scope.row.status)" size="mini">
                    {{ getMessageStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120" align="center">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" @click="handleViewMessage(scope.row)">
                    详情
                  </el-button>
                  <el-button type="text" size="mini" @click="handleResendMessage(scope.row)">
                    重发
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="消费者" name="consumers">
            <el-table :data="queueConsumers" border size="mini">
              <el-table-column prop="consumerId" label="消费者ID" width="200" />
              <el-table-column prop="consumerName" label="消费者名称" width="150" />
              <el-table-column prop="consumerType" label="消费者类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getConsumerTypeColor(scope.row.consumerType)" size="mini">
                    {{ getConsumerTypeText(scope.row.consumerType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="processedCount" label="已处理" width="100" align="center" />
              <el-table-column prop="errorCount" label="错误数" width="80" align="center" />
              <el-table-column prop="lastActivity" label="最后活动" width="150" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getConsumerStatusColor(scope.row.status)" size="mini">
                    {{ getConsumerStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="监控统计" name="monitor">
            <el-row :gutter="16" style="margin-bottom: 20px;">
              <el-col :span="6">
                <el-card shadow="hover" class="monitor-stat-card">
                  <div class="stat-value" style="color:#409EFF">{{ monitorStats.totalMessages || 0 }}</div>
                  <div class="stat-label">总消息数</div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card shadow="hover" class="monitor-stat-card">
                  <div class="stat-value" style="color:#67C23A">{{ monitorStats.successRate || 0 }}%</div>
                  <div class="stat-label">成功率</div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card shadow="hover" class="monitor-stat-card">
                  <div class="stat-value" style="color:#E6A23C">{{ monitorStats.pendingMessages || 0 }}</div>
                  <div class="stat-label">待处理消息</div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card shadow="hover" class="monitor-stat-card">
                  <div class="stat-value" style="color:#F56C6C">{{ monitorStats.totalErrors || 0 }}</div>
                  <div class="stat-label">错误总数</div>
                </el-card>
              </el-col>
            </el-row>
            <el-descriptions title="消息收发统计" :column="2" border size="small">
              <el-descriptions-item label="发送成功">{{ monitorStats.sendSuccess || 0 }}</el-descriptions-item>
              <el-descriptions-item label="发送失败">{{ monitorStats.sendFailure || 0 }}</el-descriptions-item>
              <el-descriptions-item label="接收成功">{{ monitorStats.receiveSuccess || 0 }}</el-descriptions-item>
              <el-descriptions-item label="接收失败">{{ monitorStats.receiveFailure || 0 }}</el-descriptions-item>
              <el-descriptions-item label="消息记录数">{{ monitorStats.messageCount || 0 }}</el-descriptions-item>
              <el-descriptions-item label="消费者数">{{ monitorStats.consumerCount || 0 }}</el-descriptions-item>
              <el-descriptions-item label="最后消息时间">{{ formatTime(monitorStats.lastMessageTime) }}</el-descriptions-item>
              <el-descriptions-item label="队列状态">
                <el-tag :type="getStatusColor(monitorStats.status)" size="mini">{{ getStatusText(monitorStats.status) }}</el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑队列对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="queueForm"
        :model="queueForm"
        :rules="queueRules"
        label-width="120px"
        size="small"
      >
        <el-form-item label="队列名称" prop="queueName">
          <el-input v-model="queueForm.queueName" placeholder="请输入队列名称" />
        </el-form-item>
        <el-form-item label="队列类型" prop="mqType">
          <el-select v-model="queueForm.mqType" placeholder="请选择队列类型" style="width: 100%">
            <el-option value="RABBITMQ" label="RabbitMQ" />
            <el-option value="KAFKA" label="Apache Kafka" />
            <el-option value="ACTIVEMQ" label="ActiveMQ" />
            <el-option value="ROCKETMQ" label="RocketMQ" />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最大长度" prop="maxLength">
              <el-input v-model="queueForm.maxLength" placeholder="最大消息数" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="TTL(秒)" prop="ttl">
              <el-input v-model="queueForm.ttl" placeholder="消息存活时间" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="队列描述" prop="description">
          <el-input
            v-model="queueForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入队列描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button @click="handleTestQueue">测试队列</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 连接测试对话框 -->
    <el-dialog title="连接测试" :visible.sync="testDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form label-width="100px" size="small">
        <el-form-item label="选择队列">
          <el-select v-model="testQueueId" placeholder="请选择要测试的队列" style="width: 100%">
            <el-option v-for="item in mqList" :key="item.id" :label="item.queueName || item.name" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div v-if="testResult" style="margin-top: 10px;">
        <el-alert :title="testResult.success ? '连接成功' : '连接失败'" :type="testResult.success ? 'success' : 'error'" :description="testResult.message || ''" show-icon :closable="false" />
        <el-descriptions v-if="testResult.success" :column="2" border size="mini" style="margin-top: 10px;">
          <el-descriptions-item label="队列类型">{{ getMqTypeText(testResult.mqType) }}</el-descriptions-item>
          <el-descriptions-item label="服务器">{{ testResult.serverHost }}:{{ testResult.serverPort }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ getStatusText(testResult.status) }}</el-descriptions-item>
          <el-descriptions-item label="响应时间">{{ testResult.responseTime }}ms</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="doTestConnection" :loading="testLoading">开始测试</el-button>
      </div>
    </el-dialog>

    <!-- 发送消息对话框 -->
    <el-dialog title="发送消息" :visible.sync="sendDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form label-width="100px" size="small">
        <el-form-item label="目标队列">
          <el-select v-model="sendForm.queueId" placeholder="请选择目标队列" style="width: 100%">
            <el-option v-for="item in mqList" :key="item.id" :label="item.queueName || item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="消息内容">
          <el-input v-model="sendForm.content" type="textarea" :rows="5" placeholder="请输入消息内容" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="sendDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doSendMessage" :loading="sendLoading">发送</el-button>
      </div>
    </el-dialog>

    <!-- 队列设置对话框 -->
    <el-dialog title="队列设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="120px" size="small">
        <el-form-item label="默认队列类型">
          <el-select placeholder="请选择默认队列类型" style="width: 100%">
            <el-option value="RABBITMQ" label="RabbitMQ" />
            <el-option value="KAFKA" label="Apache Kafka" />
            <el-option value="ACTIVEMQ" label="ActiveMQ" />
            <el-option value="ROCKETMQ" label="RocketMQ" />
          </el-select>
        </el-form-item>
        <el-form-item label="消息持久化">
          <el-switch active-text="开启" inactive-text="关闭" />
        </el-form-item>
        <el-form-item label="自动确认">
          <el-switch active-text="开启" inactive-text="关闭" />
        </el-form-item>
        <el-form-item label="预取数量">
          <el-input-number :min="1" :max="100" :value="10" />
        </el-form-item>
        <el-form-item label="消息格式">
          <el-radio-group value="JSON">
            <el-radio label="JSON">JSON</el-radio>
            <el-radio label="XML">XML</el-radio>
            <el-radio label="TEXT">TEXT</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 监控中心对话框 -->
    <el-dialog title="监控中心" :visible.sync="monitorDialogVisible" width="700px" :close-on-click-modal="false">
      <el-row :gutter="16">
        <el-col :span="6">
          <el-card shadow="never" class="monitor-stat-card">
            <div style="text-align: center;">
              <div style="font-size: 24px; font-weight: bold; color: #409EFF;">{{ monitorData.totalMessages }}</div>
              <div style="color: #909399; font-size: 12px; margin-top: 5px;">总消息数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="never" class="monitor-stat-card">
            <div style="text-align: center;">
              <div style="font-size: 24px; font-weight: bold; color: #67C23A;">{{ monitorData.successRate }}%</div>
              <div style="color: #909399; font-size: 12px; margin-top: 5px;">成功率</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="never" class="monitor-stat-card">
            <div style="text-align: center;">
              <div style="font-size: 24px; font-weight: bold; color: #E6A23C;">{{ monitorData.avgResponseTime }}</div>
              <div style="color: #909399; font-size: 12px; margin-top: 5px;">平均吞吐量</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="never" class="monitor-stat-card">
            <div style="text-align: center;">
              <div style="font-size: 24px; font-weight: bold; color: #F56C6C;">{{ monitorData.errorCount }}</div>
              <div style="color: #909399; font-size: 12px; margin-top: 5px;">待处理消息</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-divider content-position="left">队列状态概览</el-divider>
      <el-table :data="mqList" border size="mini" max-height="300">
        <el-table-column prop="queueName" label="队列名称" show-overflow-tooltip />
        <el-table-column prop="mqType" label="类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getMqTypeColor(scope.row.mqType)" size="mini">{{ getMqTypeText(scope.row.mqType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="messageCount" label="消息数" width="80" align="center" />
        <el-table-column prop="throughput" label="吞吐量" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="monitorDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="loadMonitorData">刷新数据</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="消息队列集成帮助" :visible.sync="helpDialogVisible" width="600px">
      <div style="line-height: 2;">
        <h4 style="margin: 0 0 10px;">功能说明</h4>
        <p>消息队列集成管理模块支持以下消息队列系统的连接管理、消息监控和消费者管理：</p>
        <ul style="padding-left: 20px;">
          <li><strong>RabbitMQ</strong> - 基于AMQP协议的消息代理</li>
          <li><strong>Apache Kafka</strong> - 分布式流处理平台</li>
          <li><strong>ActiveMQ</strong> - Apache开源消息中间件</li>
          <li><strong>RocketMQ</strong> - 阿里巴巴开源消息中间件</li>
        </ul>
        <h4 style="margin: 15px 0 10px;">操作指南</h4>
        <ul style="padding-left: 20px;">
          <li><strong>创建队列</strong> - 配置新的消息队列连接</li>
          <li><strong>连接测试</strong> - 验证队列连接是否正常</li>
          <li><strong>发送消息</strong> - 向指定队列发送测试消息</li>
          <li><strong>队列设置</strong> - 配置队列全局参数</li>
          <li><strong>监控中心</strong> - 查看队列运行状态和统计数据</li>
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
  name: 'MessageQueueIntegration',
  data() {
    return {
      // 统计数据
      mqStats: {
        totalQueues: 0,
        activeQueues: 0,
        todayMessages: 0,
        avgThroughput: 0
      },

      // 消息队列类型
      mqTypes: [],
      selectedMqType: null,

      // 队列列表
      mqList: [],
      loading: false,
      searchKeyword: '',

      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentQueue: null,
      queueMessages: [],
      queueConsumers: [],
      monitorStats: {},

      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,

      // 表单数据
      queueForm: {
        queueName: '',
        mqType: '',
        maxLength: '',
        ttl: '',
        description: ''
      },

      // 表单验证规则
      queueRules: {
        queueName: [
          { required: true, message: '请输入队列名称', trigger: 'blur' }
        ],
        mqType: [
          { required: true, message: '请选择队列类型', trigger: 'change' }
        ]
      },

      // 连接测试对话框
      testDialogVisible: false,
      testLoading: false,
      testQueueId: '',
      testResult: null,

      // 发送消息对话框
      sendDialogVisible: false,
      sendLoading: false,
      sendForm: { queueId: '', content: '' },

      // 队列设置对话框
      settingsDialogVisible: false,

      // 监控中心对话框
      monitorDialogVisible: false,
      monitorData: { totalMessages: 0, successRate: 0, avgResponseTime: 0, errorCount: 0 },

      // 帮助对话框
      helpDialogVisible: false
    }
  },

  computed: {
    filteredMqList() {
      let list = this.mqList
      // 按消息队列类型筛选
      if (this.selectedMqType) {
        list = list.filter(item => item.mqType === this.selectedMqType)
      }
      if (!this.searchKeyword) return list
      const kw = this.searchKeyword.toLowerCase()
      return list.filter(item =>
        (item.queueName && item.queueName.toLowerCase().includes(kw)) ||
        (item.name && item.name.toLowerCase().includes(kw)) ||
        (item.mqType && item.mqType.toLowerCase().includes(kw)) ||
        (item.status && item.status.toLowerCase().includes(kw))
      )
    }
  },

  watch: {
    detailActiveTab(tab) {
      if (this.currentQueue && this.currentQueue.id) {
        if (tab === 'monitor') { this.loadQueueMonitor(this.currentQueue.id) }
        else if (tab === 'messages') { this.loadQueueMessages(this.currentQueue.id) }
        else if (tab === 'consumers') { this.loadQueueConsumers(this.currentQueue.id) }
      }
    }
  },

  created() {
    this.getMqList()
    this.getMqStats()
    this.getMqTypes()
  },

  methods: {
    async getMqList() {
      this.loading = true
      try {
        const response = await systemIntegrationApi.getMqList()
        if (response.code === 1 && response.data) { this.mqList = response.data }
      } catch (error) { this.$message.error('获取队列列表失败：' + error.message) } finally { this.loading = false }
    },
    async getMqStats() {
      try {
        const response = await systemIntegrationApi.getMqStats()
        if (response.code === 1 && response.data) { this.mqStats = response.data }
      } catch (error) { console.error('获取统计数据失败：', error) }
    },
    async getMqTypes() {
      try {
        const response = await systemIntegrationApi.getMqList()
        if (response.code === 1 && response.data) {
          const typeMap = {}
          response.data.forEach(item => {
            const t = item.mqType || 'OTHER'
            if (!typeMap[t]) { typeMap[t] = { id: t, name: this.getMqTypeText(t), description: t + '消息队列', icon: 'el-icon-message', queueCount: 0 } }
            typeMap[t].queueCount++
          })
          this.mqTypes = Object.values(typeMap)
        }
      } catch (error) { console.error('获取MQ类型失败：', error) }
    },
    refreshMqTypes() { this.getMqTypes(); this.$message.success('MQ类型已刷新') },
    handleSelectMqType(mqType) { this.selectedMqType = this.selectedMqType === mqType.id ? null : mqType.id },
    handleCreateQueue() {
      this.dialogTitle = '创建消息队列'
      this.queueForm = { queueName: '', mqType: '', maxLength: '', ttl: '', description: '' }
      this.dialogVisible = true
    },
    handleRefresh() { this.getMqList(); this.getMqStats(); this.getMqTypes(); this.$message.success('数据已刷新') },
    handleTestConnection() {
      this.testResult = null
      this.testQueueId = ''
      this.testDialogVisible = true
    },
    async doTestConnection() {
      if (!this.testQueueId) { this.$message.warning('请选择要测试的队列'); return }
      this.testLoading = true
      try {
        const response = await systemIntegrationApi.messageQueue.test(this.testQueueId)
        if (response.code === 1) { this.testResult = response.data; this.$message.success('连接测试完成') }
        else { this.testResult = { success: false, message: response.msg || '测试失败' }; this.$message.error(response.msg || '测试失败') }
      } catch (error) { this.testResult = { success: false, message: error.message }; this.$message.error('测试失败：' + error.message) }
      finally { this.testLoading = false }
    },
    handleSettings() { this.settingsDialogVisible = true },
    handleHelp() { this.helpDialogVisible = true },
    handleView(row) {
      this.currentQueue = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      if (row && row.id) {
        this.loadQueueMessages(row.id)
        this.loadQueueConsumers(row.id)
      }
    },
    handleRowClick(row) { this.currentQueue = row },
    handleMoreAction(command, row) {
      const actions = { 'edit': () => this.handleEdit(row), 'config': () => this.handleView(row), 'start': () => this.handleStart(row), 'stop': () => this.handleStop(row), 'purge': () => this.handlePurge(row), 'delete': () => this.handleDelete(row), 'monitor': () => this.handleView(row), 'consumers': () => this.handleView(row) }
      if (actions[command]) actions[command]()
    },
    handleEdit(row) { this.dialogTitle = '编辑消息队列'; this.queueForm = { ...row }; this.dialogVisible = true },
    handleStart(row) {
      this.$confirm('确认启动该队列？', '提示', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.messageQueue.start(row.id)
          if (response.code === 1) { this.$message.success('启动成功'); this.getMqList() } else { this.$message.error(response.msg || '启动失败') }
        } catch (error) { this.$message.error('启动失败：' + error.message) }
      }).catch(() => {})
    },
    handleStop(row) {
      this.$confirm('确认停止该队列？', '提示', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.messageQueue.stop(row.id)
          if (response.code === 1) { this.$message.success('停止成功'); this.getMqList() } else { this.$message.error(response.msg || '停止失败') }
        } catch (error) { this.$message.error('停止失败：' + error.message) }
      }).catch(() => {})
    },
    handlePurge(row) {
      this.$confirm('确认清空该队列消息？', '警告', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.messageQueue.purge(row.id)
          if (response.code === 1) { this.$message.success('队列消息已清空'); this.getMqList() } else { this.$message.error(response.msg || '清空失败') }
        } catch (error) { this.$message.error('清空失败：' + error.message) }
      }).catch(() => {})
    },
    handleDelete(row) {
      this.$confirm('确认删除该队列？', '警告', { type: 'warning' }).then(async () => {
        try {
          const response = await systemIntegrationApi.messageQueue.delete(row.id)
          if (response.code === 1) { this.$message.success('删除成功'); this.getMqList(); this.getMqStats() } else { this.$message.error(response.msg || '删除失败') }
        } catch (error) { this.$message.error('删除失败：' + error.message) }
      }).catch(() => {})
    },
    handleSubmitForm() {
      this.$refs.queueForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          let response
          if (this.queueForm.id) { response = await systemIntegrationApi.messageQueue.update(this.queueForm.id, this.queueForm) } else { response = await systemIntegrationApi.messageQueue.create(this.queueForm) }
          if (response.code === 1) { this.$message.success(this.queueForm.id ? '更新成功' : '创建成功'); this.dialogVisible = false; this.getMqList(); this.getMqStats() } else { this.$message.error(response.msg || '操作失败') }
        } catch (error) { this.$message.error('操作失败：' + error.message) } finally { this.submitLoading = false }
      })
    },
    handleDialogClose() { this.$refs.queueForm && this.$refs.queueForm.resetFields() },
    handleMonitor() {
      this.monitorDialogVisible = true
      this.loadMonitorData()
    },
    async loadMonitorData() {
      try {
        const response = await systemIntegrationApi.getMqStats()
        if (response.code === 1 && response.data) {
          this.monitorData = {
            totalMessages: response.data.todayMessages || 0,
            successRate: response.data.totalQueues > 0 ? Math.round(response.data.activeQueues / response.data.totalQueues * 100) : 0,
            avgResponseTime: response.data.avgThroughput || 0,
            errorCount: response.data.pendingMessages || 0
          }
        }
      } catch (error) { console.error('加载监控数据失败', error) }
    },
    async handleTestQueue() {
      if (!this.queueForm.queueName || !this.queueForm.mqType) { this.$message.warning('请先填写队列名称和类型'); return }
      this.$message.info('正在测试队列连接...')
      try {
        const response = await systemIntegrationApi.messageQueue.test(this.queueForm.id || '')
        if (response.code === 1) { this.$message.success('队列连接测试通过') }
        else { this.$message.error(response.msg || '队列连接测试失败') }
      } catch (error) { this.$message.error('队列连接测试失败：' + error.message) }
    },
    handleSendMessage(row) {
      if (row && row.id) {
        this.sendForm = { queueId: row.id, content: '' }
      } else {
        this.sendForm = { queueId: '', content: '' }
      }
      this.sendDialogVisible = true
    },
    async doSendMessage() {
      if (!this.sendForm.queueId) { this.$message.warning('请选择目标队列'); return }
      if (!this.sendForm.content) { this.$message.warning('请输入消息内容'); return }
      this.sendLoading = true
      try {
        const response = await systemIntegrationApi.messageQueue.sendMessage(this.sendForm.queueId, { content: this.sendForm.content })
        if (response.code === 1) {
          this.$message.success('消息发送成功')
          this.sendDialogVisible = false
          this.getMqList()
          this.getMqStats()
        } else { this.$message.error(response.msg || '发送失败') }
      } catch (error) { this.$message.error('发送失败：' + error.message) }
      finally { this.sendLoading = false }
    },
    handleViewMessages(row) {
      if (row && row.id) {
        this.currentQueue = row
        this.detailDrawerVisible = true
        this.detailActiveTab = 'messages'
        this.loadQueueMessages(row.id)
        this.loadQueueConsumers(row.id)
      }
      else { this.$message.info('请先选择一个队列') }
    },
    handleViewMessage(msg) {
      this.$alert(`<pre style="max-height:300px;overflow:auto">${JSON.stringify(msg, null, 2)}</pre>`, '消息详情', { dangerouslyUseHTMLString: true, confirmButtonText: '关闭' })
    },
    async handleResendMessage(msg) {
      try {
        await this.$confirm('确认重新发送该消息？', '提示', { type: 'warning' })
        const response = await systemIntegrationApi.messageQueue.resendMessage(msg.id)
        if (response.code === 1) { this.$message.success('消息已重新发送'); this.getMqList() }
        else { this.$message.error(response.msg || '消息重发失败') }
      } catch (error) {
        if (error !== 'cancel') { this.$message.error('消息重发失败：' + (error.message || '')) }
      }
    },
    async loadQueueMessages(mqId) {
      try {
        const response = await systemIntegrationApi.messageQueue.getMessages(mqId)
        if (response.code === 1) { this.queueMessages = response.data || [] }
        else { this.queueMessages = [] }
      } catch (error) { console.error('加载消息列表失败', error); this.queueMessages = [] }
    },
    async loadQueueConsumers(mqId) {
      try {
        const response = await systemIntegrationApi.messageQueue.getConsumers(mqId)
        if (response.code === 1) { this.queueConsumers = response.data || [] }
        else { this.queueConsumers = [] }
      } catch (error) { console.error('加载消费者列表失败', error); this.queueConsumers = [] }
    },
    async loadQueueMonitor(mqId) {
      try {
        const response = await systemIntegrationApi.messageQueue.getMonitor(mqId)
        if (response.code === 1) { this.monitorStats = response.data || {} }
        else { this.monitorStats = {} }
      } catch (error) { console.error('加载监控统计失败', error); this.monitorStats = {} }
    },
    // 辅助方法
    getMqTypeColor(type) { const m = { 'RABBITMQ': 'primary', 'KAFKA': 'success', 'ACTIVEMQ': 'warning', 'ROCKETMQ': 'danger' }; return m[type] || 'info' },
    getMqTypeText(type) { const m = { 'RABBITMQ': 'RabbitMQ', 'KAFKA': 'Apache Kafka', 'ACTIVEMQ': 'ActiveMQ', 'ROCKETMQ': 'RocketMQ' }; return m[type] || type || '未知' },
    getStatusColor(status) { const m = { 'ACTIVE': 'success', 'INACTIVE': 'danger', 'STOPPED': 'danger', 'PENDING': 'warning', 'ERROR': 'danger', 'TESTING': 'warning' }; return m[status] || 'info' },
    getStatusText(status) { const m = { 'ACTIVE': '运行中', 'INACTIVE': '已停止', 'STOPPED': '已停止', 'PENDING': '待启动', 'ERROR': '异常', 'TESTING': '测试中' }; return m[status] || status || '未知' },
    getMessageTypeColor(type) { const m = { 'BUDGET_UPDATE': 'primary', 'APPROVAL_NOTIFY': 'success', 'ALERT': 'danger' }; return m[type] || 'info' },
    getMessageTypeText(type) { const m = { 'BUDGET_UPDATE': '预算更新', 'APPROVAL_NOTIFY': '审批通知', 'ALERT': '告警' }; return m[type] || type || '未知' },
    getPriorityColor(priority) { if (priority >= 8) return 'danger'; if (priority >= 5) return 'warning'; return 'success' },
    getMessageStatusColor(status) { const m = { 'SENT': 'success', 'PENDING': 'warning', 'FAILED': 'danger', 'CONSUMED': 'info' }; return m[status] || 'info' },
    getMessageStatusText(status) { const m = { 'SENT': '已发送', 'PENDING': '待处理', 'FAILED': '失败', 'CONSUMED': '已消费' }; return m[status] || status || '未知' },
    getConsumerTypeColor(type) { return type === 'AUTO' ? 'success' : 'primary' },
    getConsumerTypeText(type) { const m = { 'AUTO': '自动', 'MANUAL': '手动' }; return m[type] || type || '未知' },
    getConsumerStatusColor(status) { const m = { 'RUNNING': 'success', 'STOPPED': 'danger', 'IDLE': 'warning' }; return m[status] || 'info' },
    getConsumerStatusText(status) { const m = { 'RUNNING': '运行中', 'STOPPED': '已停止', 'IDLE': '空闲' }; return m[status] || status || '未知' },
    formatTime(time) {
      if (!time) return '暂无'
      const d = new Date(time)
      if (isNaN(d.getTime())) return time
      const pad = (n) => String(n).padStart(2, '0')
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
    }
  }
}
</script>

<style scoped>
.monitor-stat-card .stat-value {
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  padding: 10px 0 5px;
}
.monitor-stat-card .stat-label {
  text-align: center;
  color: #909399;
  font-size: 13px;
  padding-bottom: 10px;
}
</style>
