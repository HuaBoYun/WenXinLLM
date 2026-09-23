<template>
  <div class="budget-optimization">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算优化管理</h2>
      <p>智能预算优化和资源配置，基于AI算法提供最优预算分配方案</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateOptimization">创建优化</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-cpu" @click="handleRunOptimization">执行优化</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewResults">优化结果</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">优化设置</el-button>
            <el-button icon="el-icon-document" @click="handleReports">优化报告</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 优化统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ optimizationStats.totalOptimizations }}</div>
            <div class="stat-label">优化任务</div>
            <div class="stat-description">总优化任务数量</div>
            <div class="stat-trend">
              <i class="el-icon-magic-stick"></i>
              <span>智能优化</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-magic-stick"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card improvement-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ optimizationStats.avgImprovement }}%</div>
            <div class="stat-label">平均改善</div>
            <div class="stat-description">优化效果改善幅度</div>
            <div class="stat-trend">
              <i class="el-icon-top"></i>
              <span>显著提升</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-top"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card efficiency-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ optimizationStats.efficiency }}%</div>
            <div class="stat-label">优化效率</div>
            <div class="stat-description">算法优化效率</div>
            <div class="stat-trend">
              <i class="el-icon-cpu"></i>
              <span>高效率</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-cpu"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card savings-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ optimizationStats.totalSavings }}</div>
            <div class="stat-label">节约金额</div>
            <div class="stat-description">累计节约资金</div>
            <div class="stat-trend">
              <i class="el-icon-money"></i>
              <span>成本节约</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-money"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 优化类型选择 -->
    <el-card class="optimization-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>优化类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshOptimizationTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="optimizationType in optimizationTypes" :key="optimizationType.id">
          <el-card
            class="optimization-type-item"
            shadow="hover"
            @click.native="handleSelectOptimizationType(optimizationType)"
            :class="{ 'selected': selectedOptimizationType === optimizationType.id }"
          >
            <div class="optimization-type-icon">
              <i :class="optimizationType.icon"></i>
            </div>
            <div class="optimization-type-title">{{ optimizationType.name }}</div>
            <div class="optimization-type-description">{{ optimizationType.description }}</div>
            <div class="optimization-type-stats">
              <span class="task-count">{{ optimizationType.taskCount }} 个任务</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 优化任务列表 -->
    <el-card class="optimization-tasks-card" shadow="never">
      <div slot="header" class="card-header">
        <span>优化任务</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索任务"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getOptimizationTaskList"
            clearable
            @clear="getOptimizationTaskList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getOptimizationTaskList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="optimizationTaskList"
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
        <el-table-column prop="optimizationType" label="优化类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getOptimizationTypeColor(scope.row.optimizationType)" size="mini">
              {{ getOptimizationTypeText(scope.row.optimizationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="algorithm" label="算法" width="130" align="center">
          <template slot-scope="scope">
            <el-tag type="info" size="mini">{{ getAlgorithmText(scope.row.algorithm) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="improvement" label="改善幅度" width="120" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.improvement != null" class="improvement-text">+{{ scope.row.improvement }}%</span>
            <span v-else style="color:#C0C4CC">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="执行进度" width="150" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.progress || 0"
              :color="getProgressColor(scope.row.progress || 0)"
              :stroke-width="6"
              :show-text="false"
            />
            <span class="progress-text">{{ scope.row.progress || 0 }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="optimizationStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.optimizationStatus)" size="mini">
              {{ getStatusText(scope.row.optimizationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-video-play"
              @click.stop="handleRun(scope.row)"
              :disabled="scope.row.optimizationStatus === 'RUNNING'"
            >执行</el-button>
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
                <el-dropdown-item command="apply">应用</el-dropdown-item>
                <el-dropdown-item command="export">导出</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 任务详情抽屉 -->
    <el-drawer
      title="优化任务详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentTask">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="任务基本信息" :column="2" border>
              <el-descriptions-item label="任务名称">{{ currentTask.taskName }}</el-descriptions-item>
              <el-descriptions-item label="优化类型">{{ getOptimizationTypeText(currentTask.optimizationType) }}</el-descriptions-item>
              <el-descriptions-item label="算法">{{ getAlgorithmText(currentTask.algorithm) }}</el-descriptions-item>
              <el-descriptions-item label="改善幅度">{{ currentTask.improvement != null ? '+' + currentTask.improvement + '%' : '-' }}</el-descriptions-item>
              <el-descriptions-item label="执行进度">{{ currentTask.progress || 0 }}%</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentTask.optimizationStatus)" size="mini">
                  {{ getStatusText(currentTask.optimizationStatus) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentTask.createTime }}</el-descriptions-item>
              <el-descriptions-item label="任务描述" :span="2">{{ currentTask.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="优化配置" name="config">
            <el-form label-width="120px" size="small">
              <el-form-item label="目标函数">
                <el-input :value="currentTask.objectiveFunction" readonly />
              </el-form-item>
              <el-form-item label="约束条件">
                <el-input :value="currentTask.constraints" readonly type="textarea" :rows="3" />
              </el-form-item>
              <el-form-item label="算法参数">
                <el-input :value="currentTask.algorithmParameters" readonly type="textarea" :rows="3" />
              </el-form-item>
              <el-form-item label="优化范围">
                <el-input :value="currentTask.optimizationScope" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="优化结果" name="results">
            <el-table :data="optimizationResults" border size="mini" max-height="400">
              <el-table-column prop="variableName" label="变量名称" width="150" />
              <el-table-column prop="originalValue" label="原始值" width="120" align="center">
                <template slot-scope="scope">
                  <span>{{ formatNumber(scope.row.originalValue) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="optimizedValue" label="优化值" width="120" align="center">
                <template slot-scope="scope">
                  <span>{{ formatNumber(scope.row.optimizedValue) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="improvement" label="改善幅度" width="120" align="center">
                <template slot-scope="scope">
                  <span class="improvement-text">{{ scope.row.improvement }}%</span>
                </template>
              </el-table-column>
              <el-table-column prop="impact" label="影响程度" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getImpactColor(scope.row.impact)" size="mini">
                    {{ getImpactText(scope.row.impact) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="recommendation" label="建议" />
            </el-table>
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
              <el-table-column prop="iteration" label="迭代次数" width="100" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 执行优化弹窗 -->
    <el-dialog title="执行优化" :visible.sync="runOptimizationDialogVisible" width="600px" :close-on-click-modal="false">
      <div v-loading="runOptimizationLoading">
        <el-form label-width="100px" size="small">
          <el-form-item label="选择任务">
            <el-select v-model="runOptimizationTaskId" placeholder="请选择要执行的优化任务" style="width:100%">
              <el-option
                v-for="item in optimizationTaskList"
                :key="item.optimizationId"
                :label="item.taskName"
                :value="item.optimizationId"
                :disabled="item.optimizationStatus === 'RUNNING'"
              />
            </el-select>
          </el-form-item>
        </el-form>
        <div v-if="runOptimizationResult" class="run-result">
          <el-alert :title="runOptimizationResult" type="success" show-icon :closable="false" />
        </div>
      </div>
      <div slot="footer">
        <el-button @click="runOptimizationDialogVisible = false">关闭</el-button>
        <el-button type="primary" :loading="runOptimizationLoading" @click="doRunOptimization">执行</el-button>
      </div>
    </el-dialog>

    <!-- 优化结果弹窗 -->
    <el-dialog title="优化结果" :visible.sync="resultsDialogVisible" width="900px" :close-on-click-modal="false">
      <div v-loading="resultsLoading">
        <el-form inline size="small" style="margin-bottom:10px">
          <el-form-item label="选择任务">
            <el-select v-model="resultsTaskId" placeholder="请选择任务" style="width:220px" @change="loadResultsByTaskId">
              <el-option
                v-for="item in optimizationTaskList"
                :key="item.optimizationId"
                :label="item.taskName"
                :value="item.optimizationId"
              />
            </el-select>
          </el-form-item>
        </el-form>
        <el-table :data="dialogOptimizationResults" border size="mini" max-height="400">
          <el-table-column prop="variableName" label="变量名称" width="150" />
          <el-table-column prop="originalValue" label="原始值" width="120" align="center">
            <template slot-scope="scope"><span>{{ formatNumber(scope.row.originalValue) }}</span></template>
          </el-table-column>
          <el-table-column prop="optimizedValue" label="优化值" width="120" align="center">
            <template slot-scope="scope"><span>{{ formatNumber(scope.row.optimizedValue) }}</span></template>
          </el-table-column>
          <el-table-column prop="improvement" label="改善幅度" width="120" align="center">
            <template slot-scope="scope"><span class="improvement-text">{{ scope.row.improvement }}%</span></template>
          </el-table-column>
          <el-table-column prop="impact" label="影响程度" width="120" align="center">
            <template slot-scope="scope">
              <el-tag :type="getImpactColor(scope.row.impact)" size="mini">{{ getImpactText(scope.row.impact) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="recommendation" label="建议" />
        </el-table>
        <div v-if="!resultsLoading && dialogOptimizationResults.length === 0" style="text-align:center;color:#909399;padding:20px">暂无优化结果数据</div>
      </div>
      <div slot="footer"><el-button @click="resultsDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 优化设置弹窗 -->
    <el-dialog title="优化设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="130px" size="small">
        <el-form-item label="默认算法">
          <el-select v-model="settingsForm.defaultAlgorithm" style="width:100%">
            <el-option value="GENETIC_ALGORITHM" label="遗传算法" />
            <el-option value="PARTICLE_SWARM" label="粒子群算法" />
            <el-option value="SIMULATED_ANNEALING" label="模拟退火" />
            <el-option value="LINEAR_PROGRAMMING" label="线性规划" />
            <el-option value="GRADIENT_DESCENT" label="梯度下降" />
          </el-select>
        </el-form-item>
        <el-form-item label="最大迭代次数">
          <el-input-number v-model="settingsForm.maxIterations" :min="10" :max="10000" style="width:100%" />
        </el-form-item>
        <el-form-item label="收敛精度">
          <el-input v-model="settingsForm.convergenceTolerance" placeholder="如 0.0001" />
        </el-form-item>
        <el-form-item label="自动执行">
          <el-switch v-model="settingsForm.autoRun" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSettings">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 优化报告弹窗 -->
    <el-dialog title="优化报告" :visible.sync="reportsDialogVisible" width="800px" :close-on-click-modal="false">
      <div v-loading="reportsLoading">
        <el-descriptions title="优化汇总报告" :column="2" border>
          <el-descriptions-item label="总优化任务数">{{ optimizationStats.totalOptimizations }}</el-descriptions-item>
          <el-descriptions-item label="已完成任务数">{{ optimizationStats.completedOptimizations || 0 }}</el-descriptions-item>
          <el-descriptions-item label="平均改善幅度">{{ optimizationStats.avgImprovement }}%</el-descriptions-item>
          <el-descriptions-item label="优化效率">{{ optimizationStats.efficiency }}%</el-descriptions-item>
          <el-descriptions-item label="累计节约金额" :span="2">{{ optimizationStats.totalSavings }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top:20px">
          <el-table :data="optimizationTaskList" border size="mini" max-height="300">
            <el-table-column prop="taskName" label="任务名称" show-overflow-tooltip />
            <el-table-column prop="optimizationType" label="优化类型" width="140" align="center">
              <template slot-scope="scope">
                <el-tag :type="getOptimizationTypeColor(scope.row.optimizationType)" size="mini">
                  {{ getOptimizationTypeText(scope.row.optimizationType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="optimizationStatus" label="状态" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getStatusColor(scope.row.optimizationStatus)" size="mini">
                  {{ getStatusText(scope.row.optimizationStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
          </el-table>
        </div>
      </div>
      <div slot="footer"><el-button @click="reportsDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="帮助文档" :visible.sync="helpDialogVisible" width="700px" :close-on-click-modal="false">
      <div class="help-content">
        <h4>预算优化管理使用说明</h4>
        <el-collapse>
          <el-collapse-item title="1. 创建优化任务" name="1">
            <p>点击「创建优化」按钮，填写任务名称、优化类型、算法、优化目标等信息，提交后任务进入待执行状态。</p>
          </el-collapse-item>
          <el-collapse-item title="2. 执行优化任务" name="2">
            <p>在列表中点击「执行」按钮，或点击工具栏「执行优化」按钮选择任务执行。任务执行后状态变为「运行中」。</p>
          </el-collapse-item>
          <el-collapse-item title="3. 查看优化结果" name="3">
            <p>点击工具栏「优化结果」按钮，选择已完成的任务查看详细优化结果，包括变量改善幅度和建议。</p>
          </el-collapse-item>
          <el-collapse-item title="4. 优化类型说明" name="4">
            <ul>
              <li><b>资源分配优化</b>：优化各部门资源配置和分配方案</li>
              <li><b>成本优化</b>：通过算法降低运营成本</li>
              <li><b>效率优化</b>：提升预算执行效率</li>
              <li><b>组合优化</b>：投资组合的最优配置</li>
            </ul>
          </el-collapse-item>
          <el-collapse-item title="5. 应用优化结果" name="5">
            <p>在列表「更多」操作中选择「应用」，将优化结果应用到实际预算中。</p>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div slot="footer"><el-button @click="helpDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 新增/编辑任务对话框 -->
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
            <el-form-item label="优化类型" prop="optimizationType">
              <el-select v-model="taskForm.optimizationType" placeholder="请选择优化类型" style="width: 100%">
                <el-option value="RESOURCE_ALLOCATION" label="资源分配优化" />
                <el-option value="COST_OPTIMIZATION" label="成本优化" />
                <el-option value="EFFICIENCY_OPTIMIZATION" label="效率优化" />
                <el-option value="PORTFOLIO_OPTIMIZATION" label="组合优化" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="算法" prop="algorithm">
              <el-select v-model="taskForm.algorithm" placeholder="请选择算法" style="width: 100%">
                <el-option value="GENETIC_ALGORITHM" label="遗传算法" />
                <el-option value="PARTICLE_SWARM" label="粒子群算法" />
                <el-option value="SIMULATED_ANNEALING" label="模拟退火" />
                <el-option value="LINEAR_PROGRAMMING" label="线性规划" />
                <el-option value="GRADIENT_DESCENT" label="梯度下降" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优化目标" prop="optimizationGoal">
              <el-select v-model="taskForm.optimizationGoal" placeholder="请选择优化目标" style="width: 100%">
                <el-option value="MINIMIZE_COST" label="最小化成本" />
                <el-option value="MAXIMIZE_EFFICIENCY" label="最大化效率" />
                <el-option value="MAXIMIZE_ROI" label="最大化投资回报" />
                <el-option value="MINIMIZE_RISK" label="最小化风险" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="目标函数" prop="objectiveFunction">
          <el-input
            v-model="taskForm.objectiveFunction"
            type="textarea"
            :rows="2"
            placeholder="请输入目标函数"
          />
        </el-form-item>
        <el-form-item label="约束条件" prop="constraints">
          <el-input
            v-model="taskForm.constraints"
            type="textarea"
            :rows="3"
            placeholder="请输入约束条件"
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
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'

export default {
  name: 'BudgetOptimization',
  data() {
    return {
      // 统计数据
      optimizationStats: {
        totalOptimizations: 0,
        avgImprovement: 0,
        efficiency: 0,
        totalSavings: '0元',
        completedOptimizations: 0
      },

      // 优化类型（id 与后端 OPTIMIZATION_TYPE 字段值一致）
      optimizationTypes: [
        { id: 'RESOURCE_ALLOCATION', name: '资源分配优化', description: '优化资源配置和分配', icon: 'el-icon-s-grid', taskCount: 0 },
        { id: 'COST_OPTIMIZATION', name: '成本优化', description: '降低成本提高效益', icon: 'el-icon-money', taskCount: 0 },
        { id: 'EFFICIENCY_OPTIMIZATION', name: '效率优化', description: '提升运营效率', icon: 'el-icon-cpu', taskCount: 0 },
        { id: 'PORTFOLIO_OPTIMIZATION', name: '组合优化', description: '投资组合优化', icon: 'el-icon-pie-chart', taskCount: 0 }
      ],
      selectedOptimizationType: null,

      // 任务列表
      optimizationTaskList: [],
      loading: false,
      searchKeyword: '',

      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentTask: null,
      optimizationResults: [],
      executionLogs: [],

      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,

      // 表单数据
      taskForm: {
        taskName: '',
        optimizationType: '',
        algorithm: '',
        optimizationGoal: '',
        objectiveFunction: '',
        constraints: '',
        description: ''
      },

      // 表单验证规则
      taskRules: {
        taskName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        optimizationType: [
          { required: true, message: '请选择优化类型', trigger: 'change' }
        ],
        algorithm: [
          { required: true, message: '请选择算法', trigger: 'change' }
        ],
        optimizationGoal: [
          { required: true, message: '请选择优化目标', trigger: 'change' }
        ]
      },

      // 执行优化弹窗
      runOptimizationDialogVisible: false,
      runOptimizationLoading: false,
      runOptimizationTaskId: '',
      runOptimizationResult: '',

      // 优化结果弹窗
      resultsDialogVisible: false,
      resultsLoading: false,
      resultsTaskId: '',
      dialogOptimizationResults: [],

      // 优化设置弹窗
      settingsDialogVisible: false,
      settingsForm: {
        defaultAlgorithm: 'GENETIC_ALGORITHM',
        maxIterations: 1000,
        convergenceTolerance: '0.0001',
        autoRun: false
      },

      // 优化报告弹窗
      reportsDialogVisible: false,
      reportsLoading: false,

      // 帮助弹窗
      helpDialogVisible: false
    }
  },

  created() {
    this.getOptimizationTaskList()
    this.getOptimizationStats()
  },

  methods: {
    // 获取任务列表
    async getOptimizationTaskList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword }
        if (this.selectedOptimizationType) {
          params.optimizationType = this.selectedOptimizationType
        }
        const response = await advancedFeaturesApi.getBudgetOptimizationList(params)
        if (response && response.code === 1) {
          // 后端返回 data.list（分页结构）
          const data = response.data || {}
          this.optimizationTaskList = data.list || data || []
        }
      } catch (error) {
        this.$message.error('获取任务列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getOptimizationStats() {
      try {
        const response = await advancedFeaturesApi.getBudgetOptimizationStats()
        if (response && response.code === 1 && response.data) {
          this.optimizationStats = response.data
          // 同步更新优化类型卡片的 taskCount
          const typeCountMap = response.data.typeCountMap || {}
          this.optimizationTypes.forEach(t => {
            t.taskCount = typeCountMap[t.id] || 0
          })
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    // 创建优化
    handleCreateOptimization() {
      this.dialogTitle = '创建预算优化任务'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑任务
    handleEdit(row) {
      this.dialogTitle = '编辑预算优化任务'
      this.dialogVisible = true
      this.taskForm = { ...row }
    },

    // 查看详情
    async handleView(row) {
      this.currentTask = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await this.getOptimizationResults(row.optimizationId)
      await this.getExecutionLogs(row.optimizationId)
    },

    // 获取优化结果
    async getOptimizationResults(taskId) {
      try {
        const response = await advancedFeaturesApi.getBudgetOptimizationResults(taskId)
        this.optimizationResults = response.data
      } catch (error) {
        console.error('获取优化结果失败：', error)
      }
    },

    // 获取执行日志
    async getExecutionLogs(taskId) {
      try {
        const response = await advancedFeaturesApi.getBudgetOptimizationLogs(taskId)
        this.executionLogs = response.data
      } catch (error) {
        console.error('获取执行日志失败：', error)
      }
    },

    // 执行优化
    async handleRun(row) {
      this.$confirm('确定执行该预算优化任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.runBudgetOptimization(row.optimizationId)
          this.$message.success('优化任务已启动')
          this.getOptimizationTaskList()
        } catch (error) {
          this.$message.error('执行失败：' + error.message)
        }
      })
    },

    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'stop':
          this.handleStopOptimization(row)
          break
        case 'apply':
          this.handleApplyOptimization(row)
          break
        case 'export':
          this.handleExportOptimization(row)
          break
        case 'copy':
          this.handleCopyOptimization(row)
          break
        case 'delete':
          this.handleDeleteOptimization(row)
          break
      }
    },

    // 停止优化
    async handleStopOptimization(row) {
      try {
        await advancedFeaturesApi.stopBudgetOptimization(row.optimizationId)
        this.$message.success('优化任务已停止')
        this.getOptimizationTaskList()
      } catch (error) {
        this.$message.error('停止失败：' + error.message)
      }
    },

    // 应用优化
    async handleApplyOptimization(row) {
      this.$confirm('确定应用该优化结果吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.applyBudgetOptimization(row.optimizationId)
          this.$message.success('优化结果已应用')
          this.getOptimizationTaskList()
        } catch (error) {
          this.$message.error('应用失败：' + error.message)
        }
      })
    },

    // 导出优化（直接触发文件下载）
    handleExportOptimization(row) {
      const token = this.$store ? this.$store.getters['user/token'] : ''
      const baseUrl = process.env.VUE_APP_BASE_API || '/vab-mock-server'
      const url = `${baseUrl}/glkj/accountant/advanced/optimization/${row.optimizationId}/export`
      // 用 a 标签触发下载，携带 token
      const link = document.createElement('a')
      link.href = token ? `${url}?token=${token}` : url
      link.download = `预算优化_${row.taskName || row.optimizationId}.xlsx`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      this.$message.success('正在导出，请稍候...')
    },

    // 复制优化
    async handleCopyOptimization(row) {
      try {
        await advancedFeaturesApi.copyBudgetOptimization(row.optimizationId)
        this.$message.success('复制成功')
        this.getOptimizationTaskList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 删除优化
    handleDeleteOptimization(row) {
      this.$confirm('确定删除该预算优化任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteBudgetOptimization(row.optimizationId)
          this.$message.success('删除成功')
          this.getOptimizationTaskList()
          this.getOptimizationStats()
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
            if (this.taskForm.optimizationId) {
              await advancedFeaturesApi.updateBudgetOptimization(this.taskForm)
              this.$message.success('更新成功')
            } else {
              await advancedFeaturesApi.createBudgetOptimization(this.taskForm)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getOptimizationTaskList()
            this.getOptimizationStats()
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
        optimizationType: '',
        algorithm: '',
        optimizationGoal: '',
        objectiveFunction: '',
        constraints: '',
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
      this.getOptimizationTaskList()
      this.getOptimizationStats()
    },

    // 执行优化 - 改为弹窗
    handleRunOptimization() {
      this.runOptimizationTaskId = ''
      this.runOptimizationResult = ''
      this.runOptimizationDialogVisible = true
    },

    // 实际执行优化
    async doRunOptimization() {
      if (!this.runOptimizationTaskId) {
        this.$message.warning('请选择要执行的优化任务')
        return
      }
      this.runOptimizationLoading = true
      try {
        await advancedFeaturesApi.runBudgetOptimization(this.runOptimizationTaskId)
        this.runOptimizationResult = '优化任务已成功启动，请稍后刷新查看执行状态'
        this.getOptimizationTaskList()
        this.getOptimizationStats()
      } catch (error) {
        this.$message.error('执行失败：' + error.message)
      } finally {
        this.runOptimizationLoading = false
      }
    },

    // 查看结果 - 改为弹窗
    handleViewResults() {
      this.resultsTaskId = ''
      this.dialogOptimizationResults = []
      this.resultsDialogVisible = true
    },

    // 根据任务ID加载结果
    async loadResultsByTaskId(taskId) {
      if (!taskId) return
      this.resultsLoading = true
      try {
        const response = await advancedFeaturesApi.getBudgetOptimizationResults(taskId)
        if (response && response.code === 1 && response.data) {
          this.dialogOptimizationResults = response.data.results || []
        }
      } catch (error) {
        console.error('获取优化结果失败：', error)
      } finally {
        this.resultsLoading = false
      }
    },

    // 优化设置 - 改为弹窗
    handleSettings() {
      this.settingsDialogVisible = true
    },

    // 保存设置
    saveSettings() {
      this.$message.success('设置已保存')
      this.settingsDialogVisible = false
    },

    // 优化报告 - 改为弹窗
    handleReports() {
      this.reportsDialogVisible = true
    },

    // 帮助 - 改为弹窗
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 刷新优化类型
    refreshOptimizationTypes() {
      this.getOptimizationTaskList()
      this.getOptimizationStats()
      this.$message.success('已刷新')
    },

    // 选择优化类型
    handleSelectOptimizationType(optimizationType) {
      this.selectedOptimizationType = optimizationType.id
      this.getOptimizationTaskList()
    },

    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },

    // 格式化数字
    formatNumber(num) {
      return new Intl.NumberFormat('zh-CN').format(num)
    },

    // 获取优化类型颜色
    getOptimizationTypeColor(type) {
      const colorMap = {
        'RESOURCE_ALLOCATION': 'primary',
        'COST_OPTIMIZATION': 'success',
        'EFFICIENCY_OPTIMIZATION': 'warning',
        'PORTFOLIO_OPTIMIZATION': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 获取算法中文名称
    getAlgorithmText(algorithm) {
      const textMap = {
        'GENETIC_ALGORITHM': '遗传算法',
        'PARTICLE_SWARM': '粒子群算法',
        'SIMULATED_ANNEALING': '模拟退火',
        'LINEAR_PROGRAMMING': '线性规划',
        'GRADIENT_DESCENT': '梯度下降'
      }
      return textMap[algorithm] || algorithm || '-'
    },

    // 获取优化类型文本
    getOptimizationTypeText(type) {
      const textMap = {
        'RESOURCE_ALLOCATION': '资源分配优化',
        'COST_OPTIMIZATION': '成本优化',
        'EFFICIENCY_OPTIMIZATION': '效率优化',
        'PORTFOLIO_OPTIMIZATION': '组合优化'
      }
      return textMap[type] || (type ? type : '-')
    },

    // 获取影响程度颜色
    getImpactColor(impact) {
      const colorMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return colorMap[impact] || 'info'
    },

    // 获取影响程度文本
    getImpactText(impact) {
      const textMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return textMap[impact] || impact
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
        'RUNNING': 'primary',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'STOPPED': 'warning',
        'PENDING': 'info',
        'APPLIED': 'success'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'RUNNING': '运行中',
        'COMPLETED': '已完成',
        'FAILED': '失败',
        'STOPPED': '已停止',
        'PENDING': '待执行',
        'APPLIED': '已应用'
      }
      return textMap[status] || (status ? status : '-')
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
.budget-optimization {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 20px;

  h2 {
    color: #303133;
    margin: 0 0 8px 0;
    font-size: 24px;
    font-weight: 600;
  }

  p {
    color: #606266;
    margin: 0;
    font-size: 14px;
  }
}

.toolbar-card {
  margin-bottom: 20px;
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  position: relative;
  overflow: hidden;
  border: none;
  border-radius: 8px;
  transition: all 0.3s ease;
  cursor: pointer;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  &.total-card {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
  }

  &.improvement-card {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    color: white;
  }

  &.efficiency-card {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    color: white;
  }

  &.savings-card {
    background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    color: white;
  }

  .stat-content {
    position: relative;
    z-index: 2;

    .stat-number {
      font-size: 28px;
      font-weight: bold;
      margin-bottom: 5px;
    }

    .stat-label {
      font-size: 14px;
      margin-bottom: 5px;
      opacity: 0.9;
    }

    .stat-description {
      font-size: 12px;
      opacity: 0.8;
      margin-bottom: 10px;
    }

    .stat-trend {
      display: flex;
      align-items: center;
      font-size: 12px;
      opacity: 0.9;

      i {
        margin-right: 4px;
      }
    }
  }

  .stat-icon {
    position: absolute;
    top: 20px;
    right: 20px;
    font-size: 40px;
    opacity: 0.3;
    z-index: 1;
  }
}

.optimization-types-card {
  margin-bottom: 20px;
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.optimization-type-item {
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
    box-shadow: 0 0 10px rgba(64, 158, 255, 0.3);
  }

  .optimization-type-icon {
    font-size: 32px;
    color: #409EFF;
    margin-bottom: 10px;
  }

  .optimization-type-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 8px;
  }

  .optimization-type-description {
    font-size: 12px;
    color: #909399;
    margin-bottom: 10px;
  }

  .optimization-type-stats {
    .task-count {
      font-size: 12px;
      color: #67C23A;
      font-weight: 500;
    }
  }
}

.optimization-tasks-card {
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .header-tools {
    display: flex;
    align-items: center;
  }
}

.improvement-text {
  color: #67C23A;
  font-weight: 600;
}

.progress-text {
  margin-left: 8px;
  font-size: 12px;
  color: #909399;
}

.detail-content {
  padding: 20px;
}

.dialog-footer {
  text-align: right;
}

.text-right {
  text-align: right;
}

.run-result {
  margin-top: 16px;
}

.help-content {
  h4 {
    margin: 0 0 16px 0;
    color: #303133;
    font-size: 16px;
  }
  p, li {
    color: #606266;
    font-size: 14px;
    line-height: 1.8;
  }
  ul {
    padding-left: 20px;
  }
}
</style>