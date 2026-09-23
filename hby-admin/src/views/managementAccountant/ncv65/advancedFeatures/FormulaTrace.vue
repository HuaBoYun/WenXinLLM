<template>
  <div class="formula-trace">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>公式追踪管理</h2>
      <p>预算公式依赖关系追踪和计算路径分析，支持公式调试和影响分析</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateTrace">创建追踪</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-video-play" @click="handleExecuteTrace">执行追踪</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewDependency">依赖图</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">追踪设置</el-button>
            <el-button icon="el-icon-document" @click="handleReports">追踪报告</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 公式追踪统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ traceStats.totalFormulas }}</div>
            <div class="stat-label">公式总数</div>
            <div class="stat-description">系统中的公式数量</div>
            <div class="stat-trend">
              <i class="el-icon-share"></i>
              <span>全覆盖</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-share"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ traceStats.activeTasks }}</div>
            <div class="stat-label">追踪任务</div>
            <div class="stat-description">正在执行的追踪</div>
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
        <el-card class="stat-card dependency-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ traceStats.dependencies }}</div>
            <div class="stat-label">依赖关系</div>
            <div class="stat-description">公式依赖关系数</div>
            <div class="stat-trend">
              <i class="el-icon-connection"></i>
              <span>复杂网络</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-connection"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card accuracy-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ traceStats.accuracy }}%</div>
            <div class="stat-label">追踪准确率</div>
            <div class="stat-description">追踪结果准确度</div>
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

    <!-- 追踪类型选择 -->
    <el-card class="trace-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>追踪类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshTraceTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="traceType in traceTypes" :key="traceType.id">
          <el-card 
            class="trace-type-item" 
            shadow="hover" 
            @click.native="handleSelectTraceType(traceType)"
            :class="{ 'selected': selectedTraceType === traceType.id }"
          >
            <div class="trace-type-icon">
              <i :class="traceType.icon"></i>
            </div>
            <div class="trace-type-title">{{ traceType.name }}</div>
            <div class="trace-type-description">{{ traceType.description }}</div>
            <div class="trace-type-stats">
              <span class="task-count">{{ traceType.taskCount }} 个任务</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 公式追踪列表 -->
    <el-card class="trace-tasks-card" shadow="never">
      <div slot="header" class="card-header">
        <span>追踪任务</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索任务"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getTraceTaskList"
            clearable
            @clear="getTraceTaskList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getTraceTaskList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="traceTaskList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="taskName" label="任务名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.taskName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="traceType" label="追踪类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTraceTypeColor(scope.row.traceType)" size="mini">
              {{ getTraceTypeText(scope.row.traceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="formulaIds" label="公式数量" width="100" align="center">
          <template slot-scope="scope">
            <span class="formula-count">{{ getFormulaCount(scope.row.formulaIds) }} 个</span>
          </template>
        </el-table-column>
        <el-table-column prop="traceDepth" label="依赖关系" width="100" align="center">
          <template slot-scope="scope">
            <span class="dependency-count">{{ scope.row.traceDepth || 0 }} 层</span>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="执行进度" width="150" align="center">
          <template slot-scope="scope">
            <el-progress 
              :percentage="scope.row.progress" 
              :color="getProgressColor(scope.row.progress)"
              :stroke-width="6"
              :show-text="false"
            />
            <span class="progress-text">{{ scope.row.progress }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="创建人" width="100" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-video-play"
              @click="handleExecute(scope.row)"
              :disabled="scope.row.status === 'RUNNING'"
            >执行</el-button>
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
                <el-dropdown-item command="stop">停止</el-dropdown-item>
                <el-dropdown-item command="debug">调试</el-dropdown-item>
                <el-dropdown-item command="analysis">分析</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 追踪任务详情抽屉 -->
    <el-drawer
      title="追踪任务详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentTask">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="任务基本信息" :column="2" border>
              <el-descriptions-item label="任务名称">{{ currentTask.taskName }}</el-descriptions-item>
              <el-descriptions-item label="追踪类型">{{ getTraceTypeText(currentTask.traceType) }}</el-descriptions-item>
              <el-descriptions-item label="公式数量">{{ getFormulaCount(currentTask.formulaIds) }} 个</el-descriptions-item>
              <el-descriptions-item label="依赖关系">{{ currentTask.traceDepth || 0 }} 层</el-descriptions-item>
              <el-descriptions-item label="执行进度">{{ currentTask.progress }}%</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentTask.status)" size="mini">
                  {{ getStatusText(currentTask.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentTask.creator }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentTask.createTime }}</el-descriptions-item>
              <el-descriptions-item label="任务描述" :span="2">{{ currentTask.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="公式列表" name="formulas">
            <el-table :data="formulaList" border size="mini">
              <el-table-column prop="formulaName" label="公式名称" width="200" />
              <el-table-column prop="formulaExpression" label="公式表达式" show-overflow-tooltip />
              <el-table-column prop="dependencyLevel" label="依赖层级" width="100" align="center" />
              <el-table-column prop="calculationOrder" label="计算顺序" width="100" align="center" />
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getFormulaStatusColor(scope.row.status)" size="mini">
                    {{ getFormulaStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="依赖关系" name="dependencies">
            <div id="dependencyGraph" style="height: 500px;"></div>
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
              <el-table-column prop="formulaName" label="相关公式" width="150" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑追踪任务对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="taskForm"
        :model="taskForm"
        :rules="taskRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="任务名称" prop="taskName">
              <el-input v-model="taskForm.taskName" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="追踪类型" prop="traceType">
              <el-select v-model="taskForm.traceType" placeholder="请选择追踪类型" style="width: 100%">
                <el-option value="DEPENDENCY" label="依赖关系追踪" />
                <el-option value="CALCULATION" label="计算路径追踪" />
                <el-option value="IMPACT" label="影响分析追踪" />
                <el-option value="DEBUG" label="公式调试追踪" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="追踪范围" prop="traceScope">
              <el-select v-model="taskForm.traceScope" placeholder="请选择追踪范围" style="width: 100%">
                <el-option value="ALL" label="全部公式" />
                <el-option value="SELECTED" label="指定公式" />
                <el-option value="MODULE" label="指定模块" />
                <el-option value="BUDGET" label="指定预算" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="追踪深度" prop="traceDepth">
              <el-input-number v-model="taskForm.traceDepth" :min="1" :max="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="目标公式" prop="targetFormulas" v-if="taskForm.traceScope === 'SELECTED'">
          <el-select
            v-model="taskForm.targetFormulas"
            multiple
            placeholder="请选择目标公式"
            style="width: 100%"
          >
            <el-option
              v-for="formula in availableFormulas"
              :key="formula.id"
              :label="formula.name"
              :value="formula.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="追踪配置" prop="traceConfig">
          <el-input
            v-model="taskForm.traceConfig"
            type="textarea"
            :rows="3"
            placeholder="请输入追踪配置（JSON格式）"
          />
        </el-form-item>
        <el-form-item label="任务描述" prop="description">
          <el-input
            v-model="taskForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入任务描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 执行追踪弹窗 -->
    <el-dialog
      title="执行追踪"
      :visible.sync="executeDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <div>
        <el-alert type="info" :closable="false" style="margin-bottom:16px">
          <span>将执行列表中第一个【待启动】状态的追踪任务。如需指定任务，请在列表行中点击「执行」按钮。</span>
        </el-alert>
        <el-descriptions :column="1" border size="mini">
          <el-descriptions-item label="可执行任务数">
            {{ traceTaskList.filter(t => t.status === 'PENDING' || t.status === 'CREATED').length }} 个
          </el-descriptions-item>
          <el-descriptions-item label="运行中任务数">
            {{ traceTaskList.filter(t => t.status === 'RUNNING').length }} 个
          </el-descriptions-item>
          <el-descriptions-item label="已完成任务数">
            {{ traceTaskList.filter(t => t.status === 'COMPLETED').length }} 个
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="executeDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="executeConfirmLoading" @click="confirmExecuteTrace">确认执行</el-button>
      </div>
    </el-dialog>

    <!-- 依赖图弹窗 -->
    <el-dialog
      title="公式依赖关系图"
      :visible.sync="dependencyDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div>
        <el-descriptions title="依赖概览" :column="2" border size="mini">
          <el-descriptions-item label="追踪任务总数">{{ traceTaskList.length }} 个</el-descriptions-item>
          <el-descriptions-item label="依赖关系追踪">{{ traceTaskList.filter(t => t.traceType === 'DEPENDENCY').length }} 个</el-descriptions-item>
          <el-descriptions-item label="计算路径追踪">{{ traceTaskList.filter(t => t.traceType === 'CALCULATION').length }} 个</el-descriptions-item>
          <el-descriptions-item label="影响分析追踪">{{ traceTaskList.filter(t => t.traceType === 'IMPACT').length }} 个</el-descriptions-item>
        </el-descriptions>
        <el-table :data="traceTaskList.slice(0, 8)" border size="mini" style="margin-top:16px">
          <el-table-column prop="taskName" label="任务名称" show-overflow-tooltip />
          <el-table-column prop="traceType" label="追踪类型" width="120" align="center">
            <template slot-scope="scope">
              <el-tag :type="getTraceTypeColor(scope.row.traceType)" size="mini">{{ getTraceTypeText(scope.row.traceType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="dependencyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 调试弹窗 -->
    <el-dialog
      :title="debugTask ? '公式调试 - ' + debugTask.taskName : '公式调试'"
      :visible.sync="debugDialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <div v-loading="debugLoading">
        <template v-if="debugTask">
          <el-descriptions :column="2" border size="mini" style="margin-bottom:16px">
            <el-descriptions-item label="任务名称">{{ debugTask.taskName }}</el-descriptions-item>
            <el-descriptions-item label="追踪类型">{{ getTraceTypeText(debugTask.traceType) }}</el-descriptions-item>
            <el-descriptions-item label="追踪深度">{{ debugTask.traceDepth }} 层</el-descriptions-item>
            <el-descriptions-item label="当前状态">
              <el-tag :type="getStatusColor(debugTask.status)" size="mini">{{ getStatusText(debugTask.status) }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </template>
        <template v-if="debugResult">
          <div style="margin-bottom:8px;font-weight:600">依赖关系（共 {{ (debugResult.dependencies || []).length }} 条）</div>
          <el-table :data="debugResult.dependencies || []" border size="mini" max-height="300">
            <el-table-column prop="taskId" label="任务ID" show-overflow-tooltip />
            <el-table-column prop="taskName" label="任务名称" show-overflow-tooltip />
            <el-table-column prop="traceType" label="追踪类型" width="120" align="center">
              <template slot-scope="scope">
                <el-tag size="mini">{{ getTraceTypeText(scope.row.traceType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="!(debugResult.dependencies || []).length" description="暂无依赖数据" :image-size="60" />
        </template>
      </div>
      <div slot="footer">
        <el-button @click="debugDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 分析弹窗 -->
    <el-dialog
      :title="analysisTask ? '影响分析 - ' + analysisTask.taskName : '影响分析'"
      :visible.sync="analysisDialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <div v-loading="analysisLoading">
        <template v-if="analysisTask">
          <el-descriptions :column="2" border size="mini" style="margin-bottom:16px">
            <el-descriptions-item label="任务名称">{{ analysisTask.taskName }}</el-descriptions-item>
            <el-descriptions-item label="追踪类型">{{ getTraceTypeText(analysisTask.traceType) }}</el-descriptions-item>
            <el-descriptions-item label="当前状态">
              <el-tag :type="getStatusColor(analysisTask.status)" size="mini">{{ getStatusText(analysisTask.status) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="影响项总数">{{ analysisResult ? analysisResult.totalImpact || 0 : 0 }} 项</el-descriptions-item>
          </el-descriptions>
        </template>
        <template v-if="analysisResult">
          <div style="margin-bottom:8px;font-weight:600">受影响任务列表</div>
          <el-table :data="analysisResult.impactedItems || []" border size="mini" max-height="300">
            <el-table-column prop="taskId" label="任务ID" show-overflow-tooltip />
            <el-table-column prop="taskName" label="任务名称" show-overflow-tooltip />
            <el-table-column prop="traceType" label="追踪类型" width="120" align="center">
              <template slot-scope="scope">
                <el-tag size="mini">{{ getTraceTypeText(scope.row.traceType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="!(analysisResult.impactedItems || []).length" description="暂无影响数据" :image-size="60" />
        </template>
      </div>
      <div slot="footer">
        <el-button @click="analysisDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'
import * as echarts from 'echarts'

export default {
  name: 'FormulaTrace',
  data() {
    return {
      // 统计数据
      traceStats: {
        totalFormulas: 0,
        activeTasks: 0,
        dependencies: 0,
        accuracy: 0
      },

      // 追踪类型
      traceTypes: [
        { id: 'DEPENDENCY', name: '依赖关系追踪', description: '分析公式间的依赖关系', icon: 'el-icon-connection', taskCount: 0 },
        { id: 'CALCULATION', name: '计算路径追踪', description: '追踪公式计算路径', icon: 'el-icon-share', taskCount: 0 },
        { id: 'IMPACT', name: '影响分析追踪', description: '分析公式变更影响', icon: 'el-icon-data-analysis', taskCount: 0 },
        { id: 'DEBUG', name: '公式调试追踪', description: '公式调试和错误定位', icon: 'el-icon-cpu', taskCount: 0 }
      ],
      selectedTraceType: null,
      
      // 追踪任务列表
      traceTaskList: [],
      loading: false,
      searchKeyword: '',
      
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentTask: null,
      formulaList: [],
      executionLogs: [],
      
      // 执行追踪弹窗
      executeDialogVisible: false,
      executeConfirmLoading: false,
      executeTargetTask: null,

      // 依赖图弹窗
      dependencyDialogVisible: false,

      // 调试弹窗
      debugDialogVisible: false,
      debugLoading: false,
      debugTask: null,
      debugResult: null,

      // 分析弹窗
      analysisDialogVisible: false,
      analysisLoading: false,
      analysisTask: null,
      analysisResult: null,

      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      
      // 表单数据
      taskForm: {
        taskName: '',
        traceType: '',
        traceScope: '',
        traceDepth: 5,
        targetFormulas: [],
        traceConfig: '',
        description: ''
      },
      
      // 可用公式列表
      availableFormulas: [],
      
      // 表单验证规则
      taskRules: {
        taskName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        traceType: [
          { required: true, message: '请选择追踪类型', trigger: 'change' }
        ],
        traceScope: [
          { required: true, message: '请选择追踪范围', trigger: 'change' }
        ]
      }
    }
  },
  
  created() {
    this.getTraceTaskList()
    this.getTraceStats()
    this.getAvailableFormulas()
  },
  
  methods: {
    // 获取追踪任务列表
    async getTraceTaskList() {
      this.loading = true
      try {
        const params = { pageNum: 1, pageSize: 100 }
        if (this.searchKeyword) params.keyword = this.searchKeyword
        if (this.selectedTraceType) params.traceType = this.selectedTraceType
        const response = await advancedFeaturesApi.getFormulaTraceTaskList(params)
        if (response && response.code === 1) {
          const list = (response.data && response.data.list) || (response.data && response.data.tlist) || []
          this.traceTaskList = list
          this.updateTraceTypeCounts(list)
        }
      } catch (error) {
        this.$message.error('获取追踪任务列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 动态统计各追踪类型任务数，同时更新顶部4个统计卡片
    updateTraceTypeCounts(list) {
      const typeMap = { DEPENDENCY: 0, CALCULATION: 0, IMPACT: 0, DEBUG: 0 }
      list.forEach(item => {
        const t = item.traceType
        if (typeMap[t] !== undefined) typeMap[t]++
        else typeMap['DEBUG']++
      })
      this.traceTypes = [
        { id: 'DEPENDENCY', name: '依赖关系追踪', description: '分析公式间的依赖关系', icon: 'el-icon-connection', taskCount: typeMap.DEPENDENCY },
        { id: 'CALCULATION', name: '计算路径追踪', description: '追踪公式计算路径', icon: 'el-icon-share', taskCount: typeMap.CALCULATION },
        { id: 'IMPACT', name: '影响分析追踪', description: '分析公式变更影响', icon: 'el-icon-data-analysis', taskCount: typeMap.IMPACT },
        { id: 'DEBUG', name: '公式调试追踪', description: '公式调试和错误定位', icon: 'el-icon-cpu', taskCount: typeMap.DEBUG }
      ]
      const activeTasks = list.filter(t => t.status === 'executing' || t.status === 'PENDING').length
      const completedTasks = list.filter(t => t.status === 'completed').length
      const accuracy = list.length > 0 ? Math.round(completedTasks * 100 / list.length * 10) / 10 : 0
      this.traceStats = {
        totalFormulas: list.length,
        activeTasks: activeTasks,
        dependencies: completedTasks,
        accuracy: accuracy
      }
    },

    // 获取统计数据（兜底，列表加载后已自动更新统计）
    async getTraceStats() {
      try {
        const response = await advancedFeaturesApi.getFormulaTraceStats({})
        if (response && response.code === 1 && response.data) {
          const d = response.data
          if (d.totalFormulas || d.activeTasks || d.dependencies || d.accuracy) {
            this.traceStats = d
          }
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    // 获取可用公式列表
    async getAvailableFormulas() {
      try {
        const response = await advancedFeaturesApi.getAvailableFormulas({})
        if (response && response.code === 1 && response.data) {
          this.availableFormulas = response.data.formulas || response.data || []
        }
      } catch (error) {
        console.error('获取可用公式失败：', error)
      }
    },
    
    // 创建追踪任务
    handleCreateTrace() {
      this.dialogTitle = '创建追踪任务'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑任务
    handleEdit(row) {
      this.dialogTitle = '编辑追踪任务'
      this.dialogVisible = true
      this.taskForm = { ...row }
    },
    
    // 查看详情
    async handleView(row) {
      this.currentTask = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      const id = row.taskId || row.traceId || row.id
      await this.getFormulaList(id)
      await this.getExecutionLogs(id)
    },
    
    // 获取公式列表
    async getFormulaList(taskId) {
      try {
        const response = await advancedFeaturesApi.getTraceTaskFormulas(taskId)
        this.formulaList = response.data
      } catch (error) {
        console.error('获取公式列表失败：', error)
      }
    },
    
    // 获取执行日志
    async getExecutionLogs(taskId) {
      try {
        const response = await advancedFeaturesApi.getTraceTaskLogs(taskId)
        this.executionLogs = response.data
      } catch (error) {
        console.error('获取执行日志失败：', error)
      }
    },
    
    // 执行追踪
    async handleExecute(row) {
      this.$confirm('确定执行该追踪任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.executeFormulaTrace(row.taskId || row.traceId || row.id)
          this.$message.success('追踪任务已启动')
          this.getTraceTaskList()
        } catch (error) {
          this.$message.error('执行失败：' + error.message)
        }
      })
    },
    
    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'stop':
          this.handleStopTask(row)
          break
        case 'debug':
          this.handleDebugTask(row)
          break
        case 'analysis':
          this.handleAnalysisTask(row)
          break
        case 'copy':
          this.handleCopyTask(row)
          break
        case 'delete':
          this.handleDeleteTask(row)
          break
      }
    },
    
    // 停止任务
    async handleStopTask(row) {
      try {
        await advancedFeaturesApi.stopFormulaTrace(row.taskId || row.traceId || row.id)
        this.$message.success('任务已停止')
        this.getTraceTaskList()
      } catch (error) {
        this.$message.error('停止失败：' + error.message)
      }
    },

    // 调试任务（改为弹窗）
    async handleDebugTask(row) {
      this.debugTask = row
      this.debugResult = null
      this.debugDialogVisible = true
      this.debugLoading = true
      try {
        const id = row.taskId || row.traceId || row.id
        const response = await advancedFeaturesApi.getFormulaDependencies(id)
        if (response && response.code === 1) {
          this.debugResult = response.data
        } else {
          this.debugResult = { dependencies: [], totalCount: 0 }
        }
      } catch (error) {
        this.$message.error('获取调试数据失败：' + error.message)
        this.debugResult = { dependencies: [], totalCount: 0 }
      } finally {
        this.debugLoading = false
      }
    },

    // 分析任务（改为弹窗）
    async handleAnalysisTask(row) {
      this.analysisTask = row
      this.analysisResult = null
      this.analysisDialogVisible = true
      this.analysisLoading = true
      try {
        const id = row.taskId || row.traceId || row.id
        const response = await advancedFeaturesApi.getFormulaImpactAnalysis(id)
        if (response && response.code === 1) {
          this.analysisResult = response.data
        } else {
          this.analysisResult = { impactedItems: [], totalImpact: 0 }
        }
      } catch (error) {
        this.$message.error('获取分析数据失败：' + error.message)
        this.analysisResult = { impactedItems: [], totalImpact: 0 }
      } finally {
        this.analysisLoading = false
      }
    },

    // 复制任务
    async handleCopyTask(row) {
      try {
        await advancedFeaturesApi.copyFormulaTrace(row.taskId || row.traceId || row.id)
        this.$message.success('复制成功')
        this.getTraceTaskList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 删除任务
    handleDeleteTask(row) {
      this.$confirm('确定删除该追踪任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteFormulaTrace(row.taskId || row.traceId || row.id)
          this.$message.success('删除成功')
          this.getTraceTaskList()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },
    
    // 提交表单
    async handleSubmitForm() {
      this.$refs.taskForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            if (this.taskForm.id) {
              await advancedFeaturesApi.updateFormulaTrace(this.taskForm)
              this.$message.success('更新成功')
            } else {
              await advancedFeaturesApi.createFormulaTrace(this.taskForm)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getTraceTaskList()
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
      this.taskForm = {
        taskName: '',
        traceType: '',
        traceScope: '',
        traceDepth: 5,
        targetFormulas: [],
        traceConfig: '',
        description: ''
      }
      this.$nextTick(() => {
        this.$refs.taskForm && this.$refs.taskForm.clearValidate()
      })
    },
    
    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },
    
    // 刷新
    handleRefresh() {
      this.getTraceTaskList()
      this.getTraceStats()
    },
    
    // 执行追踪（改为弹窗）
    handleExecuteTrace() {
      this.executeDialogVisible = true
      this.executeTargetTask = null
    },

    // 确认执行追踪
    async confirmExecuteTrace() {
      const activeTask = this.traceTaskList.find(t => t.status === 'PENDING' || t.status === 'CREATED')
      if (!activeTask) {
        this.$message.warning('当前没有可执行的追踪任务（待启动状态）')
        return
      }
      this.executeConfirmLoading = true
      try {
        await advancedFeaturesApi.executeFormulaTrace(activeTask.traceId || activeTask.taskId || activeTask.id)
        this.$message.success(`任务【${activeTask.taskName}】已启动`)
        this.executeDialogVisible = false
        this.getTraceTaskList()
      } catch (error) {
        this.$message.error('执行失败：' + error.message)
      } finally {
        this.executeConfirmLoading = false
      }
    },

    // 查看依赖图（改为弹窗）
    handleViewDependency() {
      this.dependencyDialogVisible = true
    },
    
    // 追踪设置
    handleSettings() {
      this.$router.push('/managementAccountant/ncv65/advancedFeatures/traceSettings')
    },
    
    // 追踪报告
    handleReports() {
      this.$router.push('/managementAccountant/ncv65/advancedFeatures/traceReports')
    },
    
    // 帮助
    handleHelp() {
      this.$router.push('/managementAccountant/ncv65/advancedFeatures/traceHelp')
    },
    
    // 刷新追踪类型
    refreshTraceTypes() {
      this.getTraceTaskList()
      this.getTraceStats()
      this.$message.success('已刷新')
    },
    
    // 选择追踪类型
    handleSelectTraceType(traceType) {
      this.selectedTraceType = traceType.id
      this.getTraceTaskList()
    },
    

    
    // 获取追踪类型颜色
    getTraceTypeColor(type) {
      const colorMap = {
        'DEPENDENCY': 'primary',
        'CALCULATION': 'success',
        'IMPACT': 'warning',
        'DEBUG': 'danger'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取追踪类型文本
    // 从 formulaIds 字符串计算公式数量
    getFormulaCount(formulaIds) {
      if (!formulaIds) return 0
      return formulaIds.split(',').filter(s => s.trim()).length
    },

    getTraceTypeText(type) {
      const textMap = {
        'DEPENDENCY': '依赖关系',
        'CALCULATION': '计算路径',
        'IMPACT': '影响分析',
        'DEBUG': '公式调试'
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
        'RUNNING': 'primary',   'running': 'primary',   'executing': 'primary',  'EXECUTING': 'primary',
        'COMPLETED': 'success', 'completed': 'success',
        'FAILED': 'danger',     'failed': 'danger',
        'STOPPED': 'warning',   'stopped': 'warning',
        'PENDING': 'info',      'pending': 'info',
        'CREATED': 'info',      'created': 'info',
        'PAUSED': 'warning',    'paused': 'warning'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'RUNNING': '执行中',   'running': '执行中',   'executing': '执行中',  'EXECUTING': '执行中',
        'COMPLETED': '已完成', 'completed': '已完成',
        'FAILED': '失败',      'failed': '失败',
        'STOPPED': '已停止',   'stopped': '已停止',
        'PENDING': '待执行',   'pending': '待执行',
        'CREATED': '已创建',   'created': '已创建',
        'PAUSED': '已暂停',    'paused': '已暂停'
      }
      return textMap[status] || status
    },
    
    // 获取公式状态颜色
    getFormulaStatusColor(status) {
      const colorMap = {
        'TRACED': 'success',
        'TRACING': 'primary',
        'ERROR': 'danger',
        'PENDING': 'info'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取公式状态文本
    getFormulaStatusText(status) {
      const textMap = {
        'TRACED': '已追踪',
        'TRACING': '追踪中',
        'ERROR': '错误',
        'PENDING': '待追踪'
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
.formula-trace {
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

      &.active-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.dependency-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.accuracy-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
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

  .trace-types-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .trace-type-item {
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

      .trace-type-icon {
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

      .trace-type-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .trace-type-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
      }

      .trace-type-stats {
        .task-count {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .trace-tasks-card {
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

    .formula-count {
      color: #409EFF;
      font-weight: 500;
    }

    .dependency-count {
      color: #E6A23C;
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
