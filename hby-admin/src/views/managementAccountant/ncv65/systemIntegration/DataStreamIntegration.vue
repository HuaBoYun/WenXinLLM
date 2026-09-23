<template>
  <div class="data-stream-integration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>实时数据流集成</h2>
      <p>Apache Kafka、Apache Storm、Apache Flink等实时数据流处理平台集成</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateStream">创建数据流</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-video-play" @click="handleStartStream">启动流</el-button>
            <el-button type="info" icon="el-icon-monitor" @click="handleMonitor">实时监控</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">流设置</el-button>
            <el-button icon="el-icon-data-line" @click="handleTopology">拓扑图</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据流统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ streamStats.totalStreams }}</div>
            <div class="stat-label">数据流总数</div>
            <div class="stat-description">已配置数据流数量</div>
            <div class="stat-trend">
              <i class="el-icon-connection"></i>
              <span>数据流</span>
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
            <div class="stat-number">{{ streamStats.activeStreams }}</div>
            <div class="stat-label">活跃数据流</div>
            <div class="stat-description">正在运行的数据流</div>
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
        <el-card class="stat-card throughput-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ streamStats.avgThroughput }}</div>
            <div class="stat-label">平均吞吐量</div>
            <div class="stat-description">记录/秒</div>
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
      <el-col :span="6">
        <el-card class="stat-card latency-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ streamStats.avgLatency }}ms</div>
            <div class="stat-label">平均延迟</div>
            <div class="stat-description">数据处理延迟</div>
            <div class="stat-trend">
              <i class="el-icon-timer"></i>
              <span>低延迟</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-timer"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据流平台类型选择 -->
    <el-card class="stream-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>数据流平台</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshStreamTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="8" v-for="streamType in streamTypes" :key="streamType.id">
          <el-card
            class="stream-type-item"
            shadow="hover"
            @click.native="handleSelectStreamType(streamType)"
            :class="{ 'selected': selectedStreamType === streamType.id }"
          >
            <div class="stream-type-icon">
              <i :class="streamType.icon"></i>
            </div>
            <div class="stream-type-title">{{ streamType.name }}</div>
            <div class="stream-type-description">{{ streamType.description }}</div>
            <div class="stream-type-stats">
              <span class="stream-count">{{ streamType.streamCount }} 个数据流</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据流列表 -->
    <el-card class="stream-list-card" shadow="never">
      <div slot="header" class="card-header">
        <span>数据流管理</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索数据流"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getStreamList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="filteredStreamList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="streamName" label="数据流名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.streamName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="platform" label="处理平台" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getPlatformColor(scope.row.platform)" size="mini">
              {{ getPlatformText(scope.row.platform) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sourceType" label="数据源" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSourceTypeColor(scope.row.sourceType)" size="mini">
              {{ getSourceTypeText(scope.row.sourceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="throughput" label="吞吐量" width="100" align="center">
          <template slot-scope="scope">
            <span class="throughput">{{ scope.row.throughput }}/s</span>
          </template>
        </el-table-column>
        <el-table-column prop="latency" label="延迟" width="80" align="center">
          <template slot-scope="scope">
            <span class="latency">{{ scope.row.latency }}ms</span>
          </template>
        </el-table-column>
        <el-table-column prop="processedRecords" label="已处理记录" width="120" align="center">
          <template slot-scope="scope">
            <span class="processed-records">{{ scope.row.processedRecords }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdate" label="最后更新" width="150" align="center">
          <template slot-scope="scope">
            <span class="last-update">{{ scope.row.lastUpdate }}</span>
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
              icon="el-icon-video-play"
              @click="handleStartStream(scope.row)"
              v-if="scope.row.status === 'STOPPED'"
            >启动</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-video-pause"
              @click="handleStopStream(scope.row)"
              v-if="scope.row.status === 'RUNNING'"
            >停止</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="edit">编辑</el-dropdown-item>
                <el-dropdown-item command="topology">拓扑图</el-dropdown-item>
                <el-dropdown-item command="monitor">监控</el-dropdown-item>
                <el-dropdown-item command="logs">日志</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 数据流详情抽屉 -->
    <el-drawer
      title="数据流详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentStream">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="数据流基本信息" :column="2" border>
              <el-descriptions-item label="数据流名称">{{ currentStream.streamName }}</el-descriptions-item>
              <el-descriptions-item label="处理平台">{{ getPlatformText(currentStream.platform) }}</el-descriptions-item>
              <el-descriptions-item label="数据源">{{ getSourceTypeText(currentStream.sourceType) }}</el-descriptions-item>
              <el-descriptions-item label="目标">{{ getTargetTypeText(currentStream.targetType) }}</el-descriptions-item>
              <el-descriptions-item label="吞吐量">{{ currentStream.throughput }}/s</el-descriptions-item>
              <el-descriptions-item label="延迟">{{ currentStream.latency }}ms</el-descriptions-item>
              <el-descriptions-item label="已处理记录">{{ currentStream.processedRecords }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentStream.status)" size="mini">
                  {{ getStatusText(currentStream.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="最后更新">{{ currentStream.lastUpdate }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentStream.createTime }}</el-descriptions-item>
              <el-descriptions-item label="数据流描述" :span="2">{{ currentStream.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="拓扑配置" name="topology">
            <el-form label-width="120px" size="small">
              <el-form-item label="数据源配置">
                <el-input :value="currentStream.sourceConfig" readonly type="textarea" :rows="3" />
              </el-form-item>
              <el-form-item label="处理逻辑">
                <el-input :value="currentStream.processingLogic" readonly type="textarea" :rows="4" />
              </el-form-item>
              <el-form-item label="目标配置">
                <el-input :value="currentStream.targetConfig" readonly type="textarea" :rows="3" />
              </el-form-item>
              <el-form-item label="并行度">
                <el-input :value="currentStream.parallelism" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="实时监控" name="monitor">
            <el-descriptions title="实时监控数据" :column="2" border style="margin-bottom: 20px;">
              <el-descriptions-item label="当前吞吐量">{{ monitorData.throughput || 0 }} 条/秒</el-descriptions-item>
              <el-descriptions-item label="平均延迟">{{ monitorData.avgLatency || 0 }} ms</el-descriptions-item>
              <el-descriptions-item label="总处理记录">{{ monitorData.totalRecords || 0 }}</el-descriptions-item>
              <el-descriptions-item label="成功记录">{{ monitorData.successRecords || 0 }}</el-descriptions-item>
              <el-descriptions-item label="失败记录">{{ monitorData.failureRecords || 0 }}</el-descriptions-item>
              <el-descriptions-item label="成功率">{{ monitorData.successRate || 0 }}%</el-descriptions-item>
              <el-descriptions-item label="运行状态">
                <el-tag :type="getStatusColor(monitorData.status)" size="mini">{{ getStatusText(monitorData.status) }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="并行度">{{ monitorData.parallelism || '-' }}</el-descriptions-item>
              <el-descriptions-item label="检查点间隔">{{ monitorData.checkpointInterval || '-' }} 秒</el-descriptions-item>
              <el-descriptions-item label="启动时间">{{ monitorData.startTime || '-' }}</el-descriptions-item>
              <el-descriptions-item label="最后处理时间">{{ monitorData.lastProcessTime || '-' }}</el-descriptions-item>
              <el-descriptions-item label="错误信息" :span="2">
                <span style="color: #F56C6C;">{{ monitorData.errorMessage || '无' }}</span>
              </el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="处理日志" name="logs">
            <el-table :data="streamLogs" border size="mini">
              <el-table-column prop="logTime" label="时间" width="150" />
              <el-table-column prop="logLevel" label="级别" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getLogLevelColor(scope.row.logLevel)" size="mini">
                    {{ scope.row.logLevel }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="component" label="组件" width="120" />
              <el-table-column prop="message" label="日志信息" />
              <el-table-column prop="recordCount" label="记录数" width="100" align="center" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑数据流对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="streamForm"
        :model="streamForm"
        :rules="streamRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据流名称" prop="streamName">
              <el-input v-model="streamForm.streamName" placeholder="请输入数据流名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处理平台" prop="platform">
              <el-select v-model="streamForm.platform" placeholder="请选择处理平台" style="width: 100%">
                <el-option value="KAFKA" label="Apache Kafka" />
                <el-option value="STORM" label="Apache Storm" />
                <el-option value="FLINK" label="Apache Flink" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据源类型" prop="sourceType">
              <el-select v-model="streamForm.sourceType" placeholder="请选择数据源类型" style="width: 100%">
                <el-option value="KAFKA_TOPIC" label="Kafka主题" />
                <el-option value="DATABASE" label="数据库" />
                <el-option value="FILE" label="文件" />
                <el-option value="API" label="API接口" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标类型" prop="targetType">
              <el-select v-model="streamForm.targetType" placeholder="请选择目标类型" style="width: 100%">
                <el-option value="DATABASE" label="数据库" />
                <el-option value="FILE" label="文件" />
                <el-option value="KAFKA_TOPIC" label="Kafka主题" />
                <el-option value="ELASTICSEARCH" label="Elasticsearch" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="数据流描述" prop="description">
          <el-input
            v-model="streamForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入数据流描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button @click="handleValidateStream">验证配置</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 实时监控弹窗 -->
    <el-dialog title="实时监控" :visible.sync="monitorDialogVisible" width="700px" v-loading="monitorLoading">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="当前吞吐量">{{ monitorData.throughput || 0 }} 条/秒</el-descriptions-item>
        <el-descriptions-item label="平均延迟">{{ monitorData.avgLatency || 0 }} ms</el-descriptions-item>
        <el-descriptions-item label="总处理记录">{{ monitorData.totalRecords || 0 }}</el-descriptions-item>
        <el-descriptions-item label="成功记录">{{ monitorData.successRecords || 0 }}</el-descriptions-item>
        <el-descriptions-item label="失败记录">{{ monitorData.failureRecords || 0 }}</el-descriptions-item>
        <el-descriptions-item label="成功率">{{ monitorData.successRate || 0 }}%</el-descriptions-item>
        <el-descriptions-item label="运行状态">
          <el-tag :type="getStatusColor(monitorData.status)" size="mini">{{ getStatusText(monitorData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="并行度">{{ monitorData.parallelism || '-' }}</el-descriptions-item>
        <el-descriptions-item label="检查点间隔">{{ monitorData.checkpointInterval || '-' }} 秒</el-descriptions-item>
        <el-descriptions-item label="启动时间">{{ monitorData.startTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="最后处理时间">{{ monitorData.lastProcessTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="错误信息" :span="2">
          <span style="color: #F56C6C;">{{ monitorData.errorMessage || '无' }}</span>
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="monitorDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 拓扑图弹窗 -->
    <el-dialog title="数据流拓扑" :visible.sync="topologyDialogVisible" width="700px">
      <div v-if="currentStream" class="topology-container">
        <el-steps :active="3" align-center finish-status="success" style="margin-bottom: 30px;">
          <el-step :title="getSourceTypeText(currentStream.sourceType)" icon="el-icon-upload2" :description="'数据源: ' + getSourceTypeText(currentStream.sourceType)"></el-step>
          <el-step :title="getPlatformText(currentStream.platform)" icon="el-icon-cpu" :description="'处理平台: ' + getPlatformText(currentStream.platform)"></el-step>
          <el-step :title="getTargetTypeText(currentStream.targetType)" icon="el-icon-download" :description="'目标: ' + getTargetTypeText(currentStream.targetType)"></el-step>
        </el-steps>
        <el-descriptions title="拓扑详情" :column="1" border>
          <el-descriptions-item label="数据流名称">{{ currentStream.streamName }}</el-descriptions-item>
          <el-descriptions-item label="数据源配置">{{ currentStream.sourceConfig || '默认配置' }}</el-descriptions-item>
          <el-descriptions-item label="处理逻辑">{{ currentStream.processingLogic || '默认处理逻辑' }}</el-descriptions-item>
          <el-descriptions-item label="目标配置">{{ currentStream.targetConfig || '默认配置' }}</el-descriptions-item>
          <el-descriptions-item label="并行度">{{ currentStream.parallelism || '-' }}</el-descriptions-item>
          <el-descriptions-item label="窗口大小">{{ currentStream.windowSize || '-' }} 秒</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="topologyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 流设置弹窗 -->
    <el-dialog title="流设置" :visible.sync="settingsDialogVisible" width="500px">
      <el-form :model="settingsForm" label-width="140px">
        <el-form-item label="默认并行度">
          <el-input-number v-model="settingsForm.defaultParallelism" :min="1" :max="64" />
        </el-form-item>
        <el-form-item label="检查点间隔(秒)">
          <el-input-number v-model="settingsForm.defaultCheckpointInterval" :min="10" :max="600" :step="10" />
        </el-form-item>
        <el-form-item label="默认窗口大小(秒)">
          <el-input-number v-model="settingsForm.defaultWindowSize" :min="5" :max="300" :step="5" />
        </el-form-item>
        <el-form-item label="自动重启">
          <el-switch v-model="settingsForm.autoRestart" active-text="开启" inactive-text="关闭" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="数据流集成帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>功能概述</h4>
        <p>数据流集成模块支持 Apache Kafka、Apache Storm、Apache Flink 等实时数据流处理平台的连接管理、流启停控制和实时监控。</p>
        <el-divider />
        <h4>操作说明</h4>
        <ul>
          <li><strong>创建数据流</strong>：点击"创建数据流"按钮，填写数据流名称、处理平台、数据源类型和目标类型。</li>
          <li><strong>启动/停止</strong>：在列表中选择数据流，点击"启动"或"停止"按钮控制数据流运行状态。</li>
          <li><strong>实时监控</strong>：查看数据流的吞吐量、延迟、处理记录等实时指标。</li>
          <li><strong>拓扑图</strong>：查看数据流从数据源到处理平台再到目标的完整拓扑结构。</li>
          <li><strong>日志查看</strong>：在"更多"菜单中选择"日志"查看数据流的处理日志。</li>
        </ul>
        <el-divider />
        <h4>平台类型</h4>
        <ul>
          <li><strong>Apache Kafka</strong>：分布式流处理平台，适用于高吞吐低延迟场景。</li>
          <li><strong>Apache Flink</strong>：有状态流处理引擎，支持精确一次语义。</li>
          <li><strong>Apache Storm</strong>：实时计算系统，适用于毫秒级延迟需求。</li>
          <li><strong>Spark Streaming</strong>：微批处理引擎，适用于高吞吐量场景。</li>
        </ul>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="helpDialogVisible = false">知道了</el-button>
      </div>
    </el-dialog>

    <!-- 日志弹窗 -->
    <el-dialog :title="'数据流日志 - ' + logsStreamName" :visible.sync="logsDialogVisible" width="900px" v-loading="logsLoading">
      <el-table :data="streamLogs" border size="mini" max-height="400">
        <el-table-column prop="logTime" label="时间" width="160" />
        <el-table-column prop="logLevel" label="级别" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getLogLevelColor(scope.row.logLevel)" size="mini">{{ scope.row.logLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="component" label="组件" width="130" />
        <el-table-column prop="message" label="日志信息" show-overflow-tooltip />
        <el-table-column prop="recordCount" label="记录数" width="100" align="center" />
      </el-table>
      <div slot="footer">
        <el-button @click="logsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { systemIntegrationApi, dataStreamIntegrationApi } from '@/api/managementAccountant/ncv65/systemIntegration'

export default {
  name: 'DataStreamIntegration',
  data() {
    return {
      // 统计数据
      streamStats: {
        totalStreams: 0,
        activeStreams: 0,
        avgThroughput: 0,
        avgLatency: 0
      },

      // 数据流平台类型
      streamTypes: [],
      selectedStreamType: null,

      // 数据流列表
      streamList: [],
      loading: false,
      searchKeyword: '',

      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentStream: null,
      streamLogs: [],

      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,

      // 监控弹窗
      monitorDialogVisible: false,
      monitorData: {},
      monitorLoading: false,

      // 拓扑图弹窗
      topologyDialogVisible: false,

      // 流设置弹窗
      settingsDialogVisible: false,
      settingsForm: {
        defaultParallelism: 4,
        defaultCheckpointInterval: 60,
        defaultWindowSize: 30,
        autoRestart: true
      },

      // 帮助弹窗
      helpDialogVisible: false,

      // 日志弹窗
      logsDialogVisible: false,
      logsLoading: false,
      logsStreamName: '',

      // 表单数据
      streamForm: {
        streamName: '',
        platform: '',
        sourceType: '',
        targetType: '',
        description: ''
      },

      // 表单验证规则
      streamRules: {
        streamName: [
          { required: true, message: '请输入数据流名称', trigger: 'blur' }
        ],
        platform: [
          { required: true, message: '请选择处理平台', trigger: 'change' }
        ],
        sourceType: [
          { required: true, message: '请选择数据源类型', trigger: 'change' }
        ],
        targetType: [
          { required: true, message: '请选择目标类型', trigger: 'change' }
        ]
      }
    }
  },

  computed: {
    filteredStreamList() {
      let list = this.streamList
      if (this.selectedStreamType) {
        list = list.filter(item => item.platform === this.selectedStreamType)
      }
      if (this.searchKeyword) {
        const kw = this.searchKeyword.toLowerCase()
        list = list.filter(item =>
          (item.streamName && item.streamName.toLowerCase().includes(kw)) ||
          (item.platform && item.platform.toLowerCase().includes(kw)) ||
          (item.sourceType && item.sourceType.toLowerCase().includes(kw)) ||
          (item.status && item.status.toLowerCase().includes(kw))
        )
      }
      return list
    }
  },

  created() {
    this.getStreamList()
    this.getStreamStats()
    this.getStreamTypes()
  },

  methods: {
    async getStreamList() {
      this.loading = true
      try {
        const params = {}
        if (this.selectedStreamType) {
          params.streamType = this.selectedStreamType
        }
        const response = await systemIntegrationApi.getStreamList(params)
        if (response.code === 1 && response.data) { this.streamList = response.data }
      } catch (error) { this.$message.error('获取数据流列表失败：' + error.message) } finally { this.loading = false }
    },
    async getStreamStats() {
      try {
        const response = await systemIntegrationApi.getStreamStats()
        if (response.code === 1 && response.data) { this.streamStats = response.data }
      } catch (error) { console.error('获取统计数据失败：', error) }
    },
    async getStreamTypes() {
      try {
        const response = await systemIntegrationApi.getStreamList()
        if (response.code === 1 && response.data) {
          const typeMap = {}
          response.data.forEach(item => {
            const t = item.platform || 'OTHER'
            if (!typeMap[t]) {
              typeMap[t] = {
                id: t,
                name: this.getPlatformText(t),
                description: this.getPlatformDescription(t),
                icon: this.getPlatformIcon(t),
                streamCount: 0
              }
            }
            typeMap[t].streamCount++
          })
          this.streamTypes = Object.values(typeMap)
        }
      } catch (error) { console.error('获取数据流类型失败：', error) }
    },
    refreshStreamTypes() { this.getStreamTypes(); this.$message.success('数据流类型已刷新') },
    handleSelectStreamType(streamType) {
      this.selectedStreamType = this.selectedStreamType === streamType.id ? null : streamType.id
      this.getStreamList()
    },
    handleCreateStream() {
      this.dialogTitle = '创建数据流'
      this.streamForm = { streamName: '', platform: '', sourceType: '', targetType: '', description: '' }
      this.dialogVisible = true
    },
    handleRefresh() {
      this.getStreamList()
      this.getStreamStats()
      this.getStreamTypes()
      this.$message.success('数据已刷新')
    },
    handleSettings() { this.settingsDialogVisible = true },
    handleHelp() { this.helpDialogVisible = true },
    handleView(row) {
      this.currentStream = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      this.loadStreamLogs(row.id)
      this.loadDrawerMonitor(row.id)
    },
    async loadStreamLogs(streamId) {
      try {
        const response = await dataStreamIntegrationApi.getLogs(streamId)
        if (response.code === 1 && response.data) { this.streamLogs = response.data }
      } catch (error) { console.error('获取日志失败：', error) }
    },
    async loadDrawerMonitor(streamId) {
      try {
        const response = await dataStreamIntegrationApi.getMonitor(streamId)
        if (response.code === 1 && response.data) { this.monitorData = response.data }
      } catch (error) { console.error('获取监控数据失败：', error) }
    },
    handleRowClick(row) { this.currentStream = row },
    handleMoreAction(command, row) {
      const actions = {
        'edit': () => this.handleEdit(row),
        'topology': () => this.handleViewTopology(row),
        'monitor': () => this.handleViewMonitor(row),
        'logs': () => this.handleViewLogs(row),
        'delete': () => this.handleDelete(row)
      }
      if (actions[command]) actions[command]()
    },
    handleEdit(row) { this.dialogTitle = '编辑数据流'; this.streamForm = { ...row }; this.dialogVisible = true },
    handleStart(row) {
      if (row && row.id) {
        this.$confirm('确认启动该数据流？', '提示', { type: 'warning' }).then(async () => {
          try {
            const response = await dataStreamIntegrationApi.start(row.id)
            if (response.code === 1) { this.$message.success('数据流已启动'); this.getStreamList(); this.getStreamStats() } else { this.$message.error(response.msg || '启动失败') }
          } catch (error) { this.$message.error('启动失败：' + error.message) }
        }).catch(() => {})
      }
    },
    handleStop(row) {
      if (row && row.id) {
        this.$confirm('确认停止该数据流？', '提示', { type: 'warning' }).then(async () => {
          try {
            const response = await dataStreamIntegrationApi.stop(row.id)
            if (response.code === 1) { this.$message.success('数据流已停止'); this.getStreamList(); this.getStreamStats() } else { this.$message.error(response.msg || '停止失败') }
          } catch (error) { this.$message.error('停止失败：' + error.message) }
        }).catch(() => {})
      }
    },
    handleStartStream(row) {
      if (row && row.id) {
        this.handleStart(row)
      } else if (this.currentStream && this.currentStream.id) {
        this.handleStart(this.currentStream)
      } else {
        this.$message.info('请先在列表中选择一个数据流')
      }
    },
    handleStopStream(row) {
      if (row && row.id) {
        this.handleStop(row)
      } else if (this.currentStream && this.currentStream.id) {
        this.handleStop(this.currentStream)
      } else {
        this.$message.info('请先在列表中选择一个数据流')
      }
    },
    handleMonitor() {
      if (this.currentStream && this.currentStream.id) {
        this.handleViewMonitor(this.currentStream)
      } else {
        this.$message.info('请先在列表中选择一个数据流查看监控')
      }
    },
    handleTopology() {
      if (this.currentStream && this.currentStream.id) {
        this.handleViewTopology(this.currentStream)
      } else {
        this.$message.info('请先在列表中选择一个数据流查看拓扑')
      }
    },
    // 查看监控弹窗
    async handleViewMonitor(row) {
      this.currentStream = row
      this.monitorLoading = true
      this.monitorDialogVisible = true
      try {
        const response = await dataStreamIntegrationApi.getMonitor(row.id)
        if (response.code === 1 && response.data) {
          this.monitorData = response.data
        }
      } catch (error) {
        this.$message.error('获取监控数据失败：' + error.message)
      } finally {
        this.monitorLoading = false
      }
    },
    // 查看拓扑弹窗
    handleViewTopology(row) {
      this.currentStream = row
      this.topologyDialogVisible = true
    },
    // 查看日志弹窗
    async handleViewLogs(row) {
      this.currentStream = row
      this.logsStreamName = row.streamName
      this.logsLoading = true
      this.logsDialogVisible = true
      try {
        const response = await dataStreamIntegrationApi.getLogs(row.id)
        if (response.code === 1 && response.data) {
          this.streamLogs = response.data
        }
      } catch (error) {
        this.$message.error('获取日志失败：' + error.message)
      } finally {
        this.logsLoading = false
      }
    },
    handleDelete(row) {
      this.$confirm('确认删除该数据流？', '警告', { type: 'warning' }).then(async () => {
        try {
          const response = await dataStreamIntegrationApi.delete(row.id)
          if (response.code === 1) { this.$message.success('删除成功'); this.getStreamList(); this.getStreamStats(); this.getStreamTypes() } else { this.$message.error(response.msg || '删除失败') }
        } catch (error) { this.$message.error('删除失败：' + error.message) }
      }).catch(() => {})
    },
    handleSubmitForm() {
      this.$refs.streamForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          let response
          if (this.streamForm.id) {
            response = await dataStreamIntegrationApi.update(this.streamForm.id, this.streamForm)
          } else {
            response = await dataStreamIntegrationApi.create(this.streamForm)
          }
          if (response.code === 1) {
            this.$message.success(this.streamForm.id ? '更新成功' : '创建成功')
            this.dialogVisible = false
            this.getStreamList()
            this.getStreamStats()
            this.getStreamTypes()
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
    handleDialogClose() { this.$refs.streamForm && this.$refs.streamForm.resetFields() },
    async handleValidateStream() {
      if (!this.streamForm.streamName || !this.streamForm.platform) {
        this.$message.warning('请先填写数据流名称和处理平台')
        return
      }
      this.$message.info('正在验证数据流配置...')
      try {
        const response = await dataStreamIntegrationApi.validate(this.streamForm)
        if (response.code === 1 && response.data && response.data.valid) {
          this.$message.success('数据流配置验证通过')
        } else {
          const errors = response.data && response.data.errors ? response.data.errors.join('；') : '验证失败'
          this.$message.error(errors)
        }
      } catch (error) {
        this.$message.error('数据流配置验证失败：' + error.message)
      }
    },
    // 辅助方法
    getPlatformColor(type) { const m = { 'KAFKA_STREAM': 'primary', 'KAFKA': 'primary', 'STORM': 'success', 'FLINK': 'warning', 'SPARK_STREAMING': 'danger' }; return m[type] || 'info' },
    getPlatformText(type) { const m = { 'KAFKA_STREAM': 'Apache Kafka', 'KAFKA': 'Apache Kafka', 'STORM': 'Apache Storm', 'FLINK': 'Apache Flink', 'SPARK_STREAMING': 'Spark Streaming' }; return m[type] || type || '未知' },
    getPlatformDescription(type) { const m = { 'KAFKA_STREAM': '分布式流处理平台，高吞吐低延迟', 'KAFKA': '分布式流处理平台，高吞吐低延迟', 'STORM': '实时计算系统，毫秒级延迟', 'FLINK': '有状态流处理引擎，精确一次语义', 'SPARK_STREAMING': '微批处理引擎，高吞吐量' }; return m[type] || '流处理平台' },
    getPlatformIcon(type) { const m = { 'KAFKA_STREAM': 'el-icon-connection', 'KAFKA': 'el-icon-connection', 'STORM': 'el-icon-lightning', 'FLINK': 'el-icon-cpu', 'SPARK_STREAMING': 'el-icon-data-line' }; return m[type] || 'el-icon-connection' },
    getSourceTypeColor(type) { const m = { 'KAFKA_TOPIC': 'primary', 'KAFKA': 'primary', 'DATABASE': 'success', 'FILE': 'warning', 'API': 'danger', 'RABBITMQ': 'info' }; return m[type] || 'info' },
    getSourceTypeText(type) { const m = { 'KAFKA_TOPIC': 'Kafka主题', 'KAFKA': 'Kafka', 'DATABASE': '数据库', 'FILE': '文件', 'API': 'API接口', 'RABBITMQ': 'RabbitMQ' }; return m[type] || type || '未知' },
    getTargetTypeText(type) { const m = { 'DATABASE': '数据库', 'FILE': '文件', 'KAFKA_TOPIC': 'Kafka主题', 'KAFKA': 'Kafka', 'ELASTICSEARCH': 'Elasticsearch', 'API': 'API接口', 'RABBITMQ': 'RabbitMQ' }; return m[type] || type || '未知' },
    getStatusColor(status) { const m = { 'RUNNING': 'success', 'STOPPED': 'danger', 'PENDING': 'warning', 'ERROR': 'danger', 'TESTING': 'info' }; return m[status] || 'info' },
    getStatusText(status) { const m = { 'RUNNING': '运行中', 'STOPPED': '已停止', 'PENDING': '待启动', 'ERROR': '异常', 'TESTING': '测试中' }; return m[status] || status || '未知' },
    getLogLevelColor(level) { const m = { 'INFO': 'success', 'WARN': 'warning', 'ERROR': 'danger', 'DEBUG': 'info' }; return m[level] || 'info' }
  }
}
</script>


<style scoped>
.data-stream-integration { padding: 20px; }
.page-header { margin-bottom: 20px; }
.page-header h2 { margin: 0 0 8px 0; font-size: 20px; }
.page-header p { margin: 0; color: #909399; font-size: 14px; }
.toolbar-card { margin-bottom: 20px; }
.text-right { text-align: right; }
.stats-row { margin-bottom: 20px; }
.stat-card { position: relative; overflow: hidden; }
.stat-card .stat-content { position: relative; z-index: 1; }
.stat-card .stat-number { font-size: 28px; font-weight: bold; margin-bottom: 4px; }
.stat-card .stat-label { font-size: 14px; color: #606266; margin-bottom: 4px; }
.stat-card .stat-description { font-size: 12px; color: #909399; margin-bottom: 8px; }
.stat-card .stat-trend { font-size: 12px; color: #909399; }
.stat-card .stat-icon { position: absolute; right: 20px; top: 50%; transform: translateY(-50%); font-size: 48px; opacity: 0.15; }
.total-card .stat-number { color: #409EFF; }
.total-card .stat-icon { color: #409EFF; }
.active-card .stat-number { color: #67C23A; }
.active-card .stat-icon { color: #67C23A; }
.throughput-card .stat-number { color: #E6A23C; }
.throughput-card .stat-icon { color: #E6A23C; }
.latency-card .stat-number { color: #F56C6C; }
.latency-card .stat-icon { color: #F56C6C; }
.stream-types-card { margin-bottom: 20px; }
.stream-type-item { cursor: pointer; padding: 12px; border: 2px solid transparent; border-radius: 8px; transition: all 0.3s; text-align: center; }
.stream-type-item:hover { border-color: #409EFF; background: #f0f7ff; }
.stream-type-item.active { border-color: #409EFF; background: #ecf5ff; }
.stream-type-item .type-icon { font-size: 32px; color: #409EFF; margin-bottom: 8px; }
.stream-type-item .type-name { font-size: 14px; font-weight: bold; margin-bottom: 4px; }
.stream-type-item .type-desc { font-size: 12px; color: #909399; margin-bottom: 4px; }
.stream-type-item .type-count { font-size: 12px; color: #409EFF; }
.stream-list-card { margin-bottom: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.detail-content { padding: 20px; }
.topology-container { padding: 10px; }
.help-content h4 { margin: 10px 0 8px; color: #303133; }
.help-content p { color: #606266; line-height: 1.8; margin: 0 0 8px; }
.help-content ul { padding-left: 20px; color: #606266; line-height: 2; }
</style>