<template>
  <div class="budget-simulation">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算模拟管理</h2>
      <p>预算场景模拟和假设分析，支持多种预算模型的仿真测试和效果评估</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateSimulation">创建模拟</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-video-play" @click="handleRunSimulation">运行模拟</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewResults">模拟结果</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">模拟设置</el-button>
            <el-button icon="el-icon-document" @click="handleReports">模拟报告</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 模拟统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ simulationStats.totalSimulations }}</div>
            <div class="stat-label">模拟总数</div>
            <div class="stat-description">创建的模拟数量</div>
            <div class="stat-trend">
              <i class="el-icon-cpu"></i>
              <span>仿真测试</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-cpu"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card running-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ simulationStats.runningSimulations }}</div>
            <div class="stat-label">运行中</div>
            <div class="stat-description">正在运行的模拟</div>
            <div class="stat-trend">
              <i class="el-icon-loading"></i>
              <span>执行中</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-loading"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card scenarios-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ simulationStats.totalScenarios }}</div>
            <div class="stat-label">模拟场景</div>
            <div class="stat-description">配置的场景数量</div>
            <div class="stat-trend">
              <i class="el-icon-s-grid"></i>
              <span>多场景</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-grid"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card accuracy-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ simulationStats.accuracy }}%</div>
            <div class="stat-label">模拟准确率</div>
            <div class="stat-description">模拟结果准确度</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>高精度</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 模拟类型选择 -->
    <el-card class="simulation-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>模拟类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshSimulationTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="simulationType in simulationTypes" :key="simulationType.id">
          <el-card
            class="simulation-type-item"
            shadow="hover"
            @click.native="handleSelectSimulationType(simulationType)"
            :class="{ 'selected': selectedSimulationType === simulationType.id }"
          >
            <div class="simulation-type-icon">
              <i :class="simulationType.icon"></i>
            </div>
            <div class="simulation-type-title">{{ simulationType.name }}</div>
            <div class="simulation-type-description">{{ simulationType.description }}</div>
            <div class="simulation-type-stats">
              <span class="simulation-count">{{ simulationType.simulationCount }} 个模拟</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 模拟列表 -->
    <el-card class="simulations-card" shadow="never">
      <div slot="header" class="card-header">
        <span>预算模拟</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索模拟"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getSimulationList"
            clearable
            @clear="getSimulationList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getSimulationList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="simulationList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="simulationName" label="模拟名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.simulationName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="simulationType" label="模拟类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSimulationTypeColor(scope.row.simulationType)" size="mini">
              {{ getSimulationTypeText(scope.row.simulationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="scenarioCount" label="场景数量" width="100" align="center">
          <template slot-scope="scope">
            <span class="scenario-count">{{ scope.row.scenarioCount != null ? scope.row.scenarioCount : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="simulationStatus" label="执行进度" width="150" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.simulationStatus === 'COMPLETED' ? 100 : scope.row.simulationStatus === 'IN_PROGRESS' ? 50 : 0"
              :color="getProgressColor(scope.row.simulationStatus === 'COMPLETED' ? 100 : scope.row.simulationStatus === 'IN_PROGRESS' ? 50 : 0)"
              :stroke-width="6"
              :show-text="false"
            />
            <span class="progress-text">{{ scope.row.simulationStatus === 'COMPLETED' ? '100' : scope.row.simulationStatus === 'IN_PROGRESS' ? '50' : '0' }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="执行时间" width="150" align="center">
          <template slot-scope="scope">
            <span class="execution-time">{{ scope.row.updateTime || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="simulationStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.simulationStatus)" size="mini">
              {{ getStatusText(scope.row.simulationStatus) }}
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
              icon="el-icon-video-play"
              @click.stop="handleRun(scope.row)"
              :disabled="scope.row.simulationStatus === 'IN_PROGRESS'"
            >运行</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)" @click.stop.native>
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="stop">停止</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="export">导出</el-dropdown-item>
                <el-dropdown-item command="compare">对比</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 模拟详情抽屉 -->
    <el-drawer
      title="模拟详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentSimulation">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="模拟基本信息" :column="2" border>
              <el-descriptions-item label="模拟名称">{{ currentSimulation.simulationName }}</el-descriptions-item>
              <el-descriptions-item label="模拟类型">{{ getSimulationTypeText(currentSimulation.simulationType) }}</el-descriptions-item>
              <el-descriptions-item label="场景数量">{{ currentSimulation.scenarioCount }}</el-descriptions-item>
              <el-descriptions-item label="执行进度">{{ currentSimulation.progress }}%</el-descriptions-item>
              <el-descriptions-item label="执行时间">{{ currentSimulation.executionTime }}s</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentSimulation.status)" size="mini">
                  {{ getStatusText(currentSimulation.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentSimulation.creator }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentSimulation.createTime }}</el-descriptions-item>
              <el-descriptions-item label="模拟描述" :span="2">{{ currentSimulation.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="场景配置" name="scenarios">
            <el-table :data="simulationScenarios" border size="mini">
              <el-table-column prop="scenarioName" label="场景名称" width="150" />
              <el-table-column prop="scenarioType" label="场景类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getScenarioTypeColor(scope.row.scenarioType)" size="mini">
                    {{ getScenarioTypeText(scope.row.scenarioType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="probability" label="概率" width="100" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.row.probability }}%</span>
                </template>
              </el-table-column>
              <el-table-column prop="parameters" label="参数配置" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusColor(scope.row.status)" size="mini">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="模拟结果" name="results">
            <div id="simulationChart" style="height: 400px;"></div>
          </el-tab-pane>
          <el-tab-pane label="执行日志" name="logs">
            <el-table :data="executionLogs" border size="mini">
              <el-table-column prop="logTime" label="时间" width="150" />
              <el-table-column prop="logLevel" label="级别" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getLogLevelColor(scope.row.logLevel)" size="mini">
                    {{ scope.row.logLevel }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="logMessage" label="日志信息" />
              <el-table-column prop="scenarioId" label="场景ID" width="100" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑模拟对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="simulationForm"
        :model="simulationForm"
        :rules="simulationRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模拟名称" prop="simulationName">
              <el-input v-model="simulationForm.simulationName" placeholder="请输入模拟名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模拟类型" prop="simulationType">
              <el-select v-model="simulationForm.simulationType" placeholder="请选择模拟类型" style="width: 100%">
                <el-option value="WHAT_IF" label="What-If 分析" />
                <el-option value="SCENARIO" label="场景分析" />
                <el-option value="STRESS_TEST" label="压力测试" />
                <el-option value="OPTIMIZATION" label="优化模拟" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="场景数量" prop="scenarioCount">
              <el-input-number v-model="simulationForm.scenarioCount" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模拟编码" prop="simulationCode">
              <el-input v-model="simulationForm.simulationCode" placeholder="请输入模拟编码（选填）" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="模拟参数" prop="simulationParams">
          <el-input
            v-model="simulationForm.simulationParams"
            type="textarea"
            :rows="4"
            placeholder="请输入模拟参数（JSON格式，选填）"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 运行模拟弹窗 -->
    <el-dialog title="运行模拟" :visible.sync="runSimulationDialogVisible" width="600px" :close-on-click-modal="false">
      <div v-loading="runSimulationLoading">
        <el-alert title="选择要运行的模拟任务" type="info" :closable="false" style="margin-bottom:16px" />
        <el-table :data="simulationList" border size="mini" highlight-current-row @current-change="handleRunSelectRow" style="width:100%">
          <el-table-column type="index" width="50" align="center" />
          <el-table-column prop="simulationName" label="模拟名称" show-overflow-tooltip />
          <el-table-column prop="simulationType" label="类型" width="120" align="center">
            <template slot-scope="scope">
              <el-tag :type="getSimulationTypeColor(scope.row.simulationType)" size="mini">{{ getSimulationTypeText(scope.row.simulationType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="simulationStatus" label="状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getStatusColor(scope.row.simulationStatus)" size="mini">{{ getStatusText(scope.row.simulationStatus) }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="runSimulationDialogVisible = false">取消</el-button>
        <el-button type="primary" :disabled="!selectedRunRow" @click="confirmRunSimulation">确认运行</el-button>
      </div>
    </el-dialog>

    <!-- 模拟结果弹窗 -->
    <el-dialog title="模拟结果" :visible.sync="resultsDialogVisible" width="800px">
      <div v-loading="resultsLoading">
        <el-table :data="simulationList" border size="mini" highlight-current-row @current-change="handleResultSelectRow" style="width:100%;margin-bottom:16px">
          <el-table-column type="index" width="50" align="center" />
          <el-table-column prop="simulationName" label="模拟名称" show-overflow-tooltip />
          <el-table-column prop="simulationStatus" label="状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getStatusColor(scope.row.simulationStatus)" size="mini">{{ getStatusText(scope.row.simulationStatus) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        </el-table>
        <div v-if="selectedResultRow">
          <el-divider>模拟结果详情</el-divider>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="模拟名称">{{ selectedResultRow.simulationName }}</el-descriptions-item>
            <el-descriptions-item label="模拟类型">{{ getSimulationTypeText(selectedResultRow.simulationType) }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusColor(selectedResultRow.simulationStatus)" size="mini">{{ getStatusText(selectedResultRow.simulationStatus) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="创建人">{{ selectedResultRow.createBy }}</el-descriptions-item>
            <el-descriptions-item label="结果数据" :span="2">
              <pre style="white-space:pre-wrap;word-break:break-all;font-size:12px;max-height:200px;overflow:auto">{{ selectedResultRow.simulationResult || '暂无结果数据' }}</pre>
            </el-descriptions-item>
          </el-descriptions>
        </div>
        <el-empty v-else description="请在上方选择一条模拟记录查看结果" />
      </div>
      <div slot="footer">
        <el-button @click="resultsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 模拟设置弹窗 -->
    <el-dialog title="模拟设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="settingsForm" label-width="120px" size="small">
        <el-form-item label="默认模拟次数">
          <el-input-number v-model="settingsForm.defaultCount" :min="100" :max="10000" style="width:100%" />
        </el-form-item>
        <el-form-item label="默认置信水平">
          <el-select v-model="settingsForm.defaultConfidence" style="width:100%">
            <el-option value="0.90" label="90%" />
            <el-option value="0.95" label="95%" />
            <el-option value="0.99" label="99%" />
          </el-select>
        </el-form-item>
        <el-form-item label="并行执行数">
          <el-input-number v-model="settingsForm.parallelism" :min="1" :max="8" style="width:100%" />
        </el-form-item>
        <el-form-item label="自动保存结果">
          <el-switch v-model="settingsForm.autoSave" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 模拟报告弹窗 -->
    <el-dialog title="模拟报告" :visible.sync="reportsDialogVisible" width="800px">
      <div v-loading="reportsLoading">
        <el-table :data="simulationList.filter(s => s.simulationStatus === 'COMPLETED')" border size="mini" style="width:100%">
          <el-table-column type="index" width="50" align="center" />
          <el-table-column prop="simulationName" label="模拟名称" show-overflow-tooltip />
          <el-table-column prop="simulationType" label="类型" width="120" align="center">
            <template slot-scope="scope">
              <el-tag :type="getSimulationTypeColor(scope.row.simulationType)" size="mini">{{ getSimulationTypeText(scope.row.simulationType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createBy" label="创建人" width="100" align="center" />
          <el-table-column prop="updateTime" label="完成时间" width="150" align="center" />
          <el-table-column label="操作" width="80" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="viewReportDetail(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="simulationList.filter(s => s.simulationStatus === 'COMPLETED').length === 0" description="暂无已完成的模拟报告" />
      </div>
      <div slot="footer">
        <el-button @click="reportsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="使用帮助" :visible.sync="helpDialogVisible" width="700px">
      <el-collapse>
        <el-collapse-item title="什么是预算模拟？" name="1">
          <p>预算模拟是通过数学模型对预算方案进行仿真测试，帮助管理者在实际执行前评估不同预算策略的效果和风险。</p>
        </el-collapse-item>
        <el-collapse-item title="模拟类型说明" name="2">
          <ul style="line-height:2">
            <li><strong>What-If 分析</strong>：假设某个条件发生变化时，对预算结果的影响分析</li>
            <li><strong>场景分析</strong>：对多个预设场景（乐观/悲观/基准）进行对比分析</li>
            <li><strong>压力测试</strong>：模拟极端不利条件下预算的承压能力</li>
            <li><strong>优化模拟</strong>：在约束条件下寻找最优预算分配方案</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="如何创建模拟？" name="3">
          <p>点击「创建模拟」按钮，填写模拟名称、选择模拟类型，配置相关参数后提交即可创建。创建后可点击「运行」按钮启动模拟执行。</p>
        </el-collapse-item>
        <el-collapse-item title="如何查看模拟结果？" name="4">
          <p>模拟执行完成后（状态变为「已完成」），可点击顶部「模拟结果」按钮或在「模拟报告」中查看详细结果数据。</p>
        </el-collapse-item>
      </el-collapse>
      <div slot="footer">
        <el-button type="primary" @click="helpDialogVisible = false">我知道了</el-button>
      </div>
    </el-dialog>

    <!-- 对比弹窗 -->
    <el-dialog title="模拟对比" :visible.sync="compareDialogVisible" width="900px" :close-on-click-modal="false">
      <div v-if="compareCurrentRow">
        <div style="margin-bottom: 16px;">
          <strong>当前模拟：</strong>{{ compareCurrentRow.simulationName }}（{{ getStatusText(compareCurrentRow.simulationStatus) }}）
        </div>
        <el-table :data="compareList" v-loading="compareLoading" border size="small" empty-text="暂无已完成的其他模拟可供对比">
          <el-table-column prop="simulationName" label="模拟名称" min-width="140" />
          <el-table-column prop="simulationType" label="类型" width="120">
            <template slot-scope="scope">{{ getSimulationTypeText(scope.row.simulationType) }}</template>
          </el-table-column>
          <el-table-column prop="scenarioCount" label="场景数量" width="90" align="center" />
          <el-table-column prop="simulationStatus" label="状态" width="90" align="center">
            <template slot-scope="scope">
              <el-tag :type="getStatusColor(scope.row.simulationStatus)" size="mini">{{ getStatusText(scope.row.simulationStatus) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createBy" label="创建人" width="100" />
          <el-table-column prop="createTime" label="创建时间" width="160">
            <template slot-scope="scope">{{ scope.row.createTime }}</template>
          </el-table-column>
          <el-table-column label="模拟结果摘要" min-width="160">
            <template slot-scope="scope">
              <span v-if="scope.row.simulationResult" style="color: #606266; font-size: 12px;">{{ scope.row.simulationResult.substring(0, 50) }}...</span>
              <span v-else style="color: #C0C4CC;">暂无结果</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="compareDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'
import * as echarts from 'echarts'

export default {
  name: 'BudgetSimulation',
  data() {
    return {
      // 统计数据
      simulationStats: {
        totalSimulations: 0,
        runningSimulations: 0,
        totalScenarios: 0,
        accuracy: 0
      },

      // 模拟类型
      simulationTypes: [
        { id: 'WHAT_IF', name: 'What-If 分析', description: '假设条件变化影响分析', icon: 'el-icon-data-analysis', simulationCount: 0 },
        { id: 'SCENARIO', name: '场景分析', description: '多场景对比分析', icon: 'el-icon-s-grid', simulationCount: 0 },
        { id: 'STRESS_TEST', name: '压力测试', description: '极端情况压力测试', icon: 'el-icon-warning', simulationCount: 0 },
        { id: 'OPTIMIZATION', name: '优化模拟', description: '最优预算方案模拟', icon: 'el-icon-connection', simulationCount: 0 }
      ],
      selectedSimulationType: null,

      // 模拟列表
      simulationList: [],
      loading: false,
      searchKeyword: '',

      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentSimulation: null,
      simulationScenarios: [],
      executionLogs: [],

      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,

      // 运行模拟弹窗
      runSimulationDialogVisible: false,
      runSimulationLoading: false,
      selectedRunRow: null,

      // 模拟结果弹窗
      resultsDialogVisible: false,
      resultsLoading: false,
      selectedResultRow: null,

      // 模拟设置弹窗
      settingsDialogVisible: false,
      settingsForm: {
        defaultCount: 1000,
        defaultConfidence: '0.95',
        parallelism: 2,
        autoSave: true
      },

      // 模拟报告弹窗
      reportsDialogVisible: false,
      reportsLoading: false,

      // 帮助弹窗
      helpDialogVisible: false,

      // 对比弹窗
      compareDialogVisible: false,
      compareLoading: false,
      compareCurrentRow: null,
      compareList: [],

      // 表单数据
      simulationForm: {
        simulationName: '',
        simulationCode: '',
        simulationType: '',
        scenarioCount: 0,
        simulationParams: ''
      },

      // 表单验证规则
      simulationRules: {
        simulationName: [
          { required: true, message: '请输入模拟名称', trigger: 'blur' }
        ],
        simulationType: [
          { required: true, message: '请选择模拟类型', trigger: 'change' }
        ]
      }
    }
  },

  created() {
    this.getSimulationList()
    this.getSimulationStats()
  },

  methods: {
    // 获取模拟列表
    async getSimulationList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword }
        if (this.selectedSimulationType) {
          params.simulationType = this.selectedSimulationType
        }
        const response = await advancedFeaturesApi.getBudgetSimulationList(params)
        if (response && response.code === 1) {
          this.simulationList = (response.data && response.data.list) || []
        }
      } catch (error) {
        this.$message.error('获取模拟列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getSimulationStats() {
      try {
        const response = await advancedFeaturesApi.getBudgetSimulationStats()
        if (response && response.code === 1 && response.data) {
          this.simulationStats = {
            totalSimulations: response.data.totalSimulations || 0,
            runningSimulations: response.data.runningSimulations || 0,
            totalScenarios: response.data.totalScenarios || 0,
            accuracy: response.data.accuracy || 0
          }
          // 更新各模拟类型的数量
          const typeCountMap = {
            'WHAT_IF': response.data.whatIfCount || 0,
            'SCENARIO': response.data.scenarioCount || 0,
            'STRESS_TEST': response.data.stressTestCount || 0,
            'OPTIMIZATION': response.data.optimizationCount || 0
          }
          this.simulationTypes = this.simulationTypes.map(t => ({
            ...t,
            simulationCount: typeCountMap[t.id] || 0
          }))
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    // 创建模拟
    handleCreateSimulation() {
      this.dialogTitle = '创建预算模拟'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑模拟
    handleEdit(row) {
      this.dialogTitle = '编辑预算模拟'
      this.dialogVisible = true
      this.simulationForm = { ...row }
    },

    // 查看详情
    async handleView(row) {
      this.currentSimulation = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await this.getSimulationScenarios(row.simulationId)
      await this.getExecutionLogs(row.simulationId)
    },

    // 获取模拟场景
    async getSimulationScenarios(simulationId) {
      try {
        const response = await advancedFeaturesApi.getBudgetSimulationScenarios(simulationId)
        this.simulationScenarios = response.data
      } catch (error) {
        console.error('获取模拟场景失败：', error)
      }
    },

    // 获取执行日志
    async getExecutionLogs(simulationId) {
      try {
        const response = await advancedFeaturesApi.getBudgetSimulationLogs(simulationId)
        this.executionLogs = response.data
      } catch (error) {
        console.error('获取执行日志失败：', error)
      }
    },

    // 运行模拟
    async handleRun(row) {
      this.$confirm('确定运行该预算模拟吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.runBudgetSimulation(row.simulationId)
          this.$message.success('模拟已启动')
          this.getSimulationList()
        } catch (error) {
          this.$message.error('运行失败：' + error.message)
        }
      })
    },

    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'stop':
          this.handleStopSimulation(row)
          break
        case 'copy':
          this.handleCopySimulation(row)
          break
        case 'export':
          this.handleExportSimulation(row)
          break
        case 'compare':
          this.handleCompareSimulation(row)
          break
        case 'delete':
          this.handleDeleteSimulation(row)
          break
      }
    },

    // 停止模拟
    async handleStopSimulation(row) {
      try {
        await advancedFeaturesApi.stopBudgetSimulation(row.simulationId)
        this.$message.success('模拟已停止')
        this.getSimulationList()
      } catch (error) {
        this.$message.error('停止失败：' + error.message)
      }
    },

    // 复制模拟
    async handleCopySimulation(row) {
      try {
        await advancedFeaturesApi.copyBudgetSimulation(row.simulationId)
        this.$message.success('复制成功')
        this.getSimulationList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 导出模拟
    async handleExportSimulation(row) {
      try {
        const res = await advancedFeaturesApi.exportBudgetSimulation(row.simulationId)
        if (res && res.code === 1 && res.data) {
          // 将返回数据转为 JSON 文件下载
          const content = JSON.stringify(res.data, null, 2)
          const blob = new Blob([content], { type: 'application/json;charset=utf-8' })
          const url = URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `simulation_${row.simulationName || row.simulationId}.json`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error((res && res.msg) || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 对比模拟（改为弹窗）
    async handleCompareSimulation(row) {
      this.compareCurrentRow = row
      this.compareDialogVisible = true
      this.compareLoading = true
      try {
        // 查询所有已完成的模拟用于对比
        const res = await advancedFeaturesApi.getBudgetSimulationList({ simulationStatus: 'COMPLETED', pageNum: 1, pageSize: 100 })
        if (res && res.code === 1) {
          const list = (res.data && res.data.list) || []
          // 排除当前行自身
          this.compareList = list.filter(item => item.simulationId !== row.simulationId)
        } else {
          this.compareList = []
        }
      } catch (e) {
        this.compareList = []
      } finally {
        this.compareLoading = false
      }
    },

    // 删除模拟
    handleDeleteSimulation(row) {
      this.$confirm('确定删除该预算模拟吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteBudgetSimulation(row.simulationId)
          this.$message.success('删除成功')
          this.getSimulationList()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 提交表单
    async handleSubmitForm() {
      this.$refs.simulationForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            if (this.simulationForm.simulationId) {
              await advancedFeaturesApi.updateBudgetSimulation(this.simulationForm)
              this.$message.success('更新成功')
            } else {
              await advancedFeaturesApi.createBudgetSimulation(this.simulationForm)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getSimulationList()
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
      this.simulationForm = {
        simulationName: '',
        simulationCode: '',
        simulationType: '',
        scenarioCount: 0,
        simulationParams: ''
      }
      this.$nextTick(() => {
        this.$refs.simulationForm && this.$refs.simulationForm.clearValidate()
      })
    },

    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },

    // 刷新
    handleRefresh() {
      this.getSimulationList()
      this.getSimulationStats()
    },

    // 运行模拟（改为弹窗）
    handleRunSimulation() {
      this.selectedRunRow = null
      this.runSimulationDialogVisible = true
    },

    // 选择要运行的行
    handleRunSelectRow(row) {
      this.selectedRunRow = row
    },

    // 确认运行
    async confirmRunSimulation() {
      if (!this.selectedRunRow) return
      await this.handleRun(this.selectedRunRow)
      this.runSimulationDialogVisible = false
    },

    // 查看结果（改为弹窗）
    handleViewResults() {
      this.selectedResultRow = null
      this.resultsDialogVisible = true
    },

    // 选择结果行
    handleResultSelectRow(row) {
      this.selectedResultRow = row
    },

    // 模拟设置（改为弹窗）
    handleSettings() {
      this.settingsDialogVisible = true
    },

    // 模拟报告（改为弹窗）
    handleReports() {
      this.reportsDialogVisible = true
    },

    // 查看报告详情
    viewReportDetail(row) {
      this.selectedResultRow = row
      this.reportsDialogVisible = false
      this.resultsDialogVisible = true
    },

    // 帮助（改为弹窗）
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 刷新模拟类型
    refreshSimulationTypes() {
      this.getSimulationList()
      this.getSimulationStats()
      this.$message.success('已刷新')
    },

    // 选择模拟类型
    handleSelectSimulationType(simulationType) {
      this.selectedSimulationType = simulationType.id
      this.getSimulationList()
    },

    // 获取模拟类型颜色
    getSimulationTypeColor(type) {
      const colorMap = {
        'WHAT_IF': 'primary',
        'SCENARIO': 'success',
        'STRESS_TEST': 'danger',
        'OPTIMIZATION': 'warning',
        // 兼容旧值
        'MONTE_CARLO': 'primary',
        'SCENARIO_ANALYSIS': 'success',
        'SENSITIVITY_ANALYSIS': 'warning'
      }
      return colorMap[type] || 'info'
    },

    // 获取模拟类型文本
    getSimulationTypeText(type) {
      const textMap = {
        'WHAT_IF': 'What-If分析',
        'SCENARIO': '场景分析',
        'STRESS_TEST': '压力测试',
        'OPTIMIZATION': '优化模拟',
        // 兼容旧值
        'MONTE_CARLO': '蒙特卡洛模拟',
        'SCENARIO_ANALYSIS': '场景分析',
        'SENSITIVITY_ANALYSIS': '敏感性分析'
      }
      return textMap[type] || type
    },

    // 获取场景类型颜色
    getScenarioTypeColor(type) {
      const colorMap = {
        'OPTIMISTIC': 'success',
        'PESSIMISTIC': 'danger',
        'REALISTIC': 'primary',
        'STRESS': 'warning'
      }
      return colorMap[type] || 'info'
    },

    // 获取场景类型文本
    getScenarioTypeText(type) {
      const textMap = {
        'OPTIMISTIC': '乐观',
        'PESSIMISTIC': '悲观',
        'REALISTIC': '现实',
        'STRESS': '压力'
      }
      return textMap[type] || type
    },

    // 获取进度颜色
    getProgressColor(progress) {
      if (progress >= 90) return '#67C23A'
      if (progress >= 60) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'DRAFT': 'info',
        'IN_PROGRESS': 'primary',
        'COMPLETED': 'success',
        'FAILED': 'danger'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'DRAFT': '草稿',
        'IN_PROGRESS': '执行中',
        'COMPLETED': '已完成',
        'FAILED': '失败'
      }
      return textMap[status] || status
    },

    // 获取日志级别颜色
    getLogLevelColor(level) {
      const colorMap = {
        'INFO': 'primary',
        'WARN': 'warning',
        'ERROR': 'danger',
        'DEBUG': 'info'
      }
      return colorMap[level] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-simulation {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;

    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .toolbar-card {
    margin-bottom: 20px;

    .text-right {
      text-align: right;
    }
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;

      &.total-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }

      &.running-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.scenarios-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
        color: white;
      }

      &.accuracy-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      .stat-content {
        position: relative;
        z-index: 2;

        .stat-number {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 2px;
        }

        .stat-description {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }

        .stat-trend {
          font-size: 12px;
          opacity: 0.9;
        }
      }

      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
      }
    }
  }

  .simulation-types-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .simulation-type-item {
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }

      &.selected {
        border-color: #409EFF;
        background: #F0F8FF;
      }

      .simulation-type-icon {
        width: 48px;
        height: 48px;
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 12px;

        i {
          font-size: 24px;
          color: white;
        }
      }

      .simulation-type-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .simulation-type-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
      }

      .simulation-type-stats {
        .simulation-count {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .simulations-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        display: flex;
        align-items: center;
      }
    }

    .scenario-count {
      color: #409EFF;
      font-weight: 500;
    }

    .execution-time {
      color: #67C23A;
      font-weight: 500;
    }

    .progress-text {
      margin-left: 8px;
      font-size: 12px;
      color: #606266;
    }
  }

  .detail-content {
    padding: 20px;
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>