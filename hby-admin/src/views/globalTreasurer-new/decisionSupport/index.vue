<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 决策模型管理 -->
      <el-tab-pane label="决策模型管理" name="decisionModel">
        <div class="decision-model-container">
          <!-- 查询条件 -->
          <el-form :model="modelQuery" ref="modelQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="模型名称" prop="modelName">
              <el-input
                v-model="modelQuery.modelName"
                placeholder="请输入模型名称"
                clearable
                @keyup.enter.native="handleModelQuery"
              />
            </el-form-item>
            <el-form-item label="模型类型" prop="modelType">
              <el-select v-model="modelQuery.modelType" placeholder="请选择模型类型" clearable>
                <el-option label="风险评估模型" value="RISK_ASSESSMENT" />
                <el-option label="投资决策模型" value="INVESTMENT_DECISION" />
                <el-option label="融资决策模型" value="FINANCING_DECISION" />
                <el-option label="现金流预测模型" value="CASHFLOW_FORECAST" />
              </el-select>
            </el-form-item>
            <el-form-item label="模型状态" prop="modelStatus">
              <el-select v-model="modelQuery.modelStatus" placeholder="请选择模型状态" clearable>
                <el-option label="启用" value="ACTIVE" />
                <el-option label="停用" value="INACTIVE" />
                <el-option label="测试中" value="TESTING" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleModelQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetModelQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click.native="handleModelAdd"
              >新增模型</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="modelSingle"
                @click="handleModelUpdateBatch"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-setting"
                size="mini"
                :disabled="modelSingle"
                @click="handleModelConfigBatch"
              >配置</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-data-analysis"
                size="mini"
                :disabled="modelSingle"
                @click="handleModelTestBatch"
              >测试</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="modelMultiple"
                @click="handleModelDelete"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleModelExport"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getModelList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="modelLoading" :data="modelList" row-key="modelId" @selection-change="handleModelSelectionChange" border stripe>
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="模型编号" align="center" prop="modelCode" width="120" />
            <el-table-column label="模型名称" align="center" prop="modelName" min-width="180" show-overflow-tooltip />
            <el-table-column label="模型类型" align="center" prop="modelType" width="150">
              <template slot-scope="scope">
                <el-tag :type="getModelTypeTagType(scope.row.modelType)" size="small">
                  {{ getModelTypeLabel(scope.row.modelType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="准确率" align="center" prop="accuracy" width="100">
              <template slot-scope="scope">
                <span>{{ scope.row.accuracy ? (scope.row.accuracy * 100).toFixed(2) + '%' : '-' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="模型状态" align="center" prop="modelStatus" width="100">
              <template slot-scope="scope">
                <el-tag :type="getModelStatusType(scope.row.modelStatus)" size="small">
                  {{ getModelStatusLabel(scope.row.modelStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="300" fixed="right">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleModelView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleModelUpdate(scope.row)"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-setting"
                  @click="handleModelConfig(scope.row)"
                >配置</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-data-analysis"
                  @click="handleModelTest(scope.row)"
                >测试</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleModelDelete(scope.row)"
                  style="color: #F56C6C;"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            background
            class="pagination"
            :current-page="modelQuery.pageNum"
            :layout="layout"
            :page-size="modelQuery.pageSize"
            :total="modelTotal"
            @current-change="handleModelCurrentChange"
            @size-change="handleModelSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- 数据分析任务 -->
      <el-tab-pane label="数据分析任务" name="dataAnalysis">
        <div class="data-analysis-container">
          <!-- 查询条件 -->
          <el-form :model="taskQuery" ref="taskQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="任务编号" prop="taskNo">
              <el-input
                v-model="taskQuery.taskNo"
                placeholder="请输入任务编号"
                clearable
                @keyup.enter.native="handleTaskQuery"
              />
            </el-form-item>
            <el-form-item label="任务名称" prop="taskName">
              <el-input
                v-model="taskQuery.taskName"
                placeholder="请输入任务名称"
                clearable
                @keyup.enter.native="handleTaskQuery"
              />
            </el-form-item>
            <el-form-item label="任务类型" prop="taskType">
              <el-select v-model="taskQuery.taskType" placeholder="请选择任务类型" clearable>
                <el-option
                  v-for="dict in taskType"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="任务状态" prop="taskStatus">
              <el-select v-model="taskQuery.taskStatus" placeholder="请选择任务状态" clearable>
                <el-option
                  v-for="dict in taskStatus"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleTaskQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetTaskQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleTaskAdd"
              >新增任务</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="taskSingle"
                @click="handleTaskUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="taskMultiple"
                @click="handleTaskDelete"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-video-play"
                size="mini"
                :disabled="taskSingle"
                @click="handleTaskExecute"
              >执行任务</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleTaskExport"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getTaskList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="taskLoading" :data="taskList" row-key="taskId" @selection-change="handleTaskSelectionChange" border stripe>
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="任务编号" align="center" prop="taskNo" />
            <el-table-column label="任务名称" align="center" prop="taskName" />
            <el-table-column label="任务类型" align="center" prop="taskType">
              <template slot-scope="scope">
                <dict-tag :options="taskType" :value="scope.row.taskType"/>
              </template>
            </el-table-column>
            <el-table-column label="分析类型" align="center" prop="analysisType" />
            <el-table-column label="数据源" align="center" prop="dataSource" />
            <el-table-column label="任务状态" align="center" prop="taskStatus">
              <template slot-scope="scope">
                <dict-tag :options="taskStatus" :value="scope.row.taskStatus"/>
              </template>
            </el-table-column>
            <el-table-column label="执行时间" align="center" prop="executionTime">
              <template slot-scope="scope">
                <span v-if="scope.row.executionTime">{{ scope.row.executionTime }}秒</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleTaskView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleTaskUpdate(scope.row)"
                  v-if="scope.row.taskStatus !== 'RUNNING'"
                >修改</el-button>
                <el-dropdown @command="(command) => handleTaskCommand(command, scope.row)" style="margin-left: 5px;">
                  <el-button size="mini" type="text">
                    更多<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="execute" v-if="scope.row.taskStatus === 'PENDING' || scope.row.taskStatus === 'FAILED'">
                      <i class="el-icon-video-play"></i>执行任务
                    </el-dropdown-item>
                    <el-dropdown-item command="cancel" v-if="scope.row.taskStatus === 'RUNNING' || scope.row.taskStatus === 'PENDING'">
                      <i class="el-icon-video-pause"></i>取消任务
                    </el-dropdown-item>
                    <el-dropdown-item command="retry" v-if="scope.row.taskStatus === 'FAILED'">
                      <i class="el-icon-refresh"></i>重试任务
                    </el-dropdown-item>
                    <el-dropdown-item command="delete" v-if="scope.row.taskStatus !== 'RUNNING'">
                      <i class="el-icon-delete"></i>删除任务
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            background
            class="pagination"
            :current-page="taskQuery.pageNum"
            :layout="layout"
            :page-size="taskQuery.pageSize"
            :total="taskTotal"
            @current-change="handleTaskCurrentChange"
            @size-change="handleTaskSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- KPI指标管理 -->
      <el-tab-pane label="KPI指标管理" name="kpiIndicator">
        <div class="kpi-indicator-container">
          <!-- 查询条件 -->
          <el-form :model="kpiQuery" ref="kpiQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="KPI编码" prop="kpiCode">
              <el-input
                v-model="kpiQuery.kpiCode"
                placeholder="请输入KPI编码"
                clearable
                @keyup.enter.native="handleKpiQuery"
              />
            </el-form-item>
            <el-form-item label="KPI名称" prop="kpiName">
              <el-input
                v-model="kpiQuery.kpiName"
                placeholder="请输入KPI名称"
                clearable
                @keyup.enter.native="handleKpiQuery"
              />
            </el-form-item>
            <el-form-item label="KPI分类" prop="kpiCategory">
              <el-select v-model="kpiQuery.kpiCategory" placeholder="请选择KPI分类" clearable>
                <el-option
                  v-for="dict in kpiCategoryOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="KPI类型" prop="kpiType">
              <el-select v-model="kpiQuery.kpiType" placeholder="请选择KPI类型" clearable>
                <el-option
                  v-for="dict in kpiTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="KPI状态" prop="kpiStatus">
              <el-select v-model="kpiQuery.kpiStatus" placeholder="请选择KPI状态" clearable>
                <el-option
                  v-for="dict in kpiStatusOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="计算频率" prop="calculationFrequency">
              <el-select v-model="kpiQuery.calculationFrequency" placeholder="请选择计算频率" clearable>
                <el-option
                  v-for="dict in calculationFrequencyOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleKpiQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetKpiQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleKpiAdd"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="kpiSingle"
                @click="handleKpiUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="kpiMultiple"
                @click="handleKpiDelete"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleKpiExport"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getKpiList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="kpiLoading" :data="kpiList" row-key="indicatorId" @selection-change="handleKpiSelectionChange" style="width: 100%" border stripe>
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="KPI编码" align="center" prop="kpiCode" width="120" />
            <el-table-column label="KPI名称" align="center" prop="kpiName" width="150" />
            <el-table-column label="KPI分类" align="center" prop="kpiCategory" width="120">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.kpiCategory === 'FINANCIAL'" type="primary" size="small">财务指标</el-tag>
                <el-tag v-else-if="scope.row.kpiCategory === 'OPERATIONAL'" type="success" size="small">运营指标</el-tag>
                <el-tag v-else-if="scope.row.kpiCategory === 'RISK'" type="warning" size="small">风险指标</el-tag>
                <el-tag v-else type="info" size="small">{{ scope.row.kpiCategory }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="KPI类型" align="center" prop="kpiType" width="120">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.kpiType === 'FINANCIAL'" type="primary" size="small">财务类</el-tag>
                <el-tag v-else-if="scope.row.kpiType === 'OPERATIONAL'" type="success" size="small">运营类</el-tag>
                <el-tag v-else-if="scope.row.kpiType === 'RISK'" type="warning" size="small">风险类</el-tag>
                <el-tag v-else type="info" size="small">{{ scope.row.kpiType }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="当前值" align="center" prop="currentValue" width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.currentValue !== null && scope.row.currentValue !== undefined">
                  {{ scope.row.currentValue }}{{ scope.row.unit }}
                </span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="目标值" align="center" prop="targetValue" width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.targetValue !== null && scope.row.targetValue !== undefined">
                  {{ scope.row.targetValue }}{{ scope.row.unit }}
                </span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="完成率" align="center" width="120">
              <template slot-scope="scope">
                <el-progress
                  v-if="scope.row.currentValue && scope.row.targetValue"
                  :percentage="Math.min(100, Math.round((scope.row.currentValue / scope.row.targetValue) * 100))"
                  :color="getProgressColor(scope.row.currentValue, scope.row.targetValue)"
                  :stroke-width="8"
                />
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="预警阈值" align="center" prop="warningThreshold" width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.warningThreshold">{{ scope.row.warningThreshold }}</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="临界阈值" align="center" prop="criticalThreshold" width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.criticalThreshold">{{ scope.row.criticalThreshold }}</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="KPI状态" align="center" prop="kpiStatus" width="100">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.kpiStatus === 'NORMAL'" type="success" size="small">正常</el-tag>
                <el-tag v-else-if="scope.row.kpiStatus === 'WARNING'" type="warning" size="small">预警</el-tag>
                <el-tag v-else-if="scope.row.kpiStatus === 'CRITICAL'" type="danger" size="small">临界</el-tag>
                <el-tag v-else type="info" size="small">{{ scope.row.kpiStatus }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="计算频率" align="center" prop="calculationFrequency" width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.calculationFrequency === 'DAILY'">每日</span>
                <span v-else-if="scope.row.calculationFrequency === 'WEEKLY'">每周</span>
                <span v-else-if="scope.row.calculationFrequency === 'MONTHLY'">每月</span>
                <span v-else-if="scope.row.calculationFrequency === 'QUARTERLY'">每季度</span>
                <span v-else-if="scope.row.calculationFrequency === 'YEARLY'">每年</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="计算日期" align="center" prop="calculationDate" width="120">
              <template slot-scope="scope">
                <span v-if="scope.row.calculationDate">{{ parseTime(scope.row.calculationDate, '{y}-{m}-{d}') }}</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="120" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-edit" @click="handleKpiUpdate(scope.row)">编辑</el-button>
                <el-button size="mini" type="text" icon="el-icon-delete" @click="handleKpiDelete(scope.row)" style="color: #F56C6C;">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            background
            class="pagination"
            :current-page="kpiQuery.pageNum"
            :layout="layout"
            :page-size="kpiQuery.pageSize"
            :total="kpiTotal"
            @current-change="handleKpiCurrentChange"
            @size-change="handleKpiSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- 预测分析 -->
      <el-tab-pane label="预测分析" name="predictiveAnalysis">
        <div class="predictive-analysis-container">
          <!-- 查询条件 -->
          <el-form :model="predictiveQuery" ref="predictiveQueryRef" size="small" :inline="true" v-show="predictiveShowSearch" label-width="68px">
            <el-form-item label="分析编号" prop="analysisNo">
              <el-input
                v-model="predictiveQuery.analysisNo"
                placeholder="请输入分析编号"
                clearable
                @keyup.enter.native="handlePredictiveQuery"
              />
            </el-form-item>
            <el-form-item label="分析名称" prop="analysisName">
              <el-input
                v-model="predictiveQuery.analysisName"
                placeholder="请输入分析名称"
                clearable
                @keyup.enter.native="handlePredictiveQuery"
              />
            </el-form-item>
            <el-form-item label="预测类型" prop="predictionType">
              <el-select v-model="predictiveQuery.predictionType" placeholder="请选择预测类型" clearable>
                <el-option
                  v-for="dict in predictionTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="分析状态" prop="analysisStatus">
              <el-select v-model="predictiveQuery.analysisStatus" placeholder="请选择分析状态" clearable>
                <el-option
                  v-for="dict in analysisStatusOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="预测周期" prop="predictionPeriod">
              <el-select v-model="predictiveQuery.predictionPeriod" placeholder="请选择预测周期" clearable>
                <el-option
                  v-for="dict in predictionPeriodOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="分析日期">
              <el-date-picker
                v-model="predictiveDateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              ></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handlePredictiveQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetPredictiveQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handlePredictiveAdd"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="predictiveSingle"
                @click="handlePredictiveUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="predictiveMultiple"
                @click="handlePredictiveDelete"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handlePredictiveExport"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="predictiveShowSearch" @queryTable="getPredictiveList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table
            ref="predictiveTable"
            v-loading="predictiveLoading"
            :data="predictiveList"
            row-key="analysisId"
            @selection-change="handlePredictiveSelectionChange"
            border
            stripe
          >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="分析编号" align="center" prop="analysisNo" width="120" />
            <el-table-column label="分析名称" align="center" prop="analysisName" show-overflow-tooltip />
            <el-table-column label="预测类型" align="center" prop="predictionType">
              <template slot-scope="scope">
                <dict-tag :options="predictionTypeOptions" :value="scope.row.predictionType"/>
              </template>
            </el-table-column>
            <el-table-column label="预测周期" align="center" prop="predictionPeriod">
              <template slot-scope="scope">
                <dict-tag :options="predictionPeriodOptions" :value="scope.row.predictionPeriod"/>
              </template>
            </el-table-column>
            <el-table-column label="预测期限" align="center" prop="predictionHorizon">
              <template slot-scope="scope">
                {{ scope.row.predictionHorizon }}天
              </template>
            </el-table-column>
            <el-table-column label="准确率" align="center" prop="accuracy">
              <template slot-scope="scope">
                <el-progress
                  v-if="scope.row.accuracy"
                  :percentage="parseFloat(scope.row.accuracy)"
                  :color="getPredictiveProgressColor(scope.row.accuracy)"
                  :stroke-width="8"
                  :show-text="true"
                  :format="() => scope.row.accuracy + '%'"
                />
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="置信水平" align="center" prop="confidenceLevel">
              <template slot-scope="scope">
                {{ scope.row.confidenceLevel }}%
              </template>
            </el-table-column>
            <el-table-column label="分析状态" align="center" prop="analysisStatus">
              <template slot-scope="scope">
                <dict-tag :options="analysisStatusOptions" :value="scope.row.analysisStatus"/>
              </template>
            </el-table-column>
            <el-table-column label="分析日期" align="center" prop="analysisDate" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.analysisDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-dropdown size="mini" @command="(command) => handlePredictiveCommand(command, scope.row)">
                  <el-button size="mini" type="primary" split-button @click="handlePredictiveUpdate(scope.row)">
                    修改<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="execute" icon="el-icon-video-play" v-if="scope.row.analysisStatus === 'PENDING'">执行分析</el-dropdown-item>
                    <el-dropdown-item command="cancel" icon="el-icon-video-pause" v-if="scope.row.analysisStatus === 'RUNNING'">取消分析</el-dropdown-item>
                    <el-dropdown-item command="retry" icon="el-icon-refresh" v-if="scope.row.analysisStatus === 'FAILED'">重试分析</el-dropdown-item>
                    <el-dropdown-item command="updateResult" icon="el-icon-edit-outline" v-if="scope.row.analysisStatus === 'COMPLETED'">更新结果</el-dropdown-item>
                    <el-dropdown-item command="calculateAccuracy" icon="el-icon-s-data" v-if="scope.row.analysisStatus === 'COMPLETED' && scope.row.actualResult">计算准确率</el-dropdown-item>
                    <el-dropdown-item command="delete" icon="el-icon-delete">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <el-pagination
            background
            class="pagination"
            :current-page="predictiveQuery.pageNum"
            :layout="layout"
            :page-size="predictiveQuery.pageSize"
            :total="predictiveTotal"
            @current-change="handlePredictiveCurrentChange"
            @size-change="handlePredictiveSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- 决策建议 -->
      <el-tab-pane label="决策建议" name="decisionRecommendation">
        <div class="decision-recommendation-container">
          <!-- 查询条件 -->
          <el-form :model="recommendationQuery" ref="recommendationQueryForm" size="small" :inline="true" v-show="recommendationShowSearch" label-width="68px">
            <el-form-item label="建议编号" prop="recommendationNo">
              <el-input
                v-model="recommendationQuery.recommendationNo"
                placeholder="请输入建议编号"
                clearable
                @keyup.enter.native="handleRecommendationQuery"
              />
            </el-form-item>
            <el-form-item label="建议名称" prop="recommendationName">
              <el-input
                v-model="recommendationQuery.recommendationName"
                placeholder="请输入建议名称"
                clearable
                @keyup.enter.native="handleRecommendationQuery"
              />
            </el-form-item>
            <el-form-item label="建议类型" prop="recommendationType">
              <el-select v-model="recommendationQuery.recommendationType" placeholder="请选择建议类型" clearable>
                <el-option
                  v-for="dict in recommendationTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="建议状态" prop="recommendationStatus">
              <el-select v-model="recommendationQuery.recommendationStatus" placeholder="请选择建议状态" clearable>
                <el-option
                  v-for="dict in recommendationStatusOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="recommendationQuery.priority" placeholder="请选择优先级" clearable>
                <el-option
                  v-for="dict in priorityOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="创建时间">
              <el-date-picker
                v-model="recommendationDateRange"
                style="width: 240px"
                value-format="yyyy-MM-dd"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleRecommendationQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetRecommendationQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleRecommendationAdd"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="recommendationSingle"
                @click="handleRecommendationUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="recommendationMultiple"
                @click="handleRecommendationDelete"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleRecommendationExport"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="recommendationShowSearch" @queryTable="getRecommendationList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="recommendationLoading" :data="recommendationList" row-key="recommendationId" border fit @selection-change="handleRecommendationSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="建议编号" align="center" prop="recommendationNo" />
            <el-table-column label="建议名称" align="center" prop="recommendationName" show-overflow-tooltip />
            <el-table-column label="建议类型" align="center" prop="recommendationType">
              <template slot-scope="scope">
                <el-tag :type="getRecommendationTypeTagType(scope.row.recommendationType)">
                  {{ getRecommendationTypeLabel(scope.row.recommendationType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="优先级" align="center" prop="priority">
              <template slot-scope="scope">
                <el-tag :type="getPriorityTagType(scope.row.priority)">
                  {{ getPriorityLabel(scope.row.priority) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="置信水平" align="center" prop="confidenceLevel" width="120">
              <template slot-scope="scope">
                <el-progress
                  :percentage="parseFloat(scope.row.confidenceLevel) || 0"
                  :color="getRecommendationProgressColor(scope.row.confidenceLevel)"
                  :stroke-width="8"
                  :show-text="true"
                  :format="() => `${parseFloat(scope.row.confidenceLevel) || 0}%`"
                />
              </template>
            </el-table-column>
            <el-table-column label="建议状态" align="center" prop="recommendationStatus">
              <template slot-scope="scope">
                <el-tag :type="getRecommendationStatusTagType(scope.row.recommendationStatus)">
                  {{ getRecommendationStatusLabel(scope.row.recommendationStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
              <template slot-scope="scope">
                <el-dropdown size="mini" @command="(command) => handleRecommendationCommand(command, scope.row)">
                  <el-button size="mini" type="primary">
                    操作<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="edit" icon="el-icon-edit">修改</el-dropdown-item>
                    <el-dropdown-item command="review" icon="el-icon-check" v-if="scope.row.recommendationStatus === 'PENDING'">审核</el-dropdown-item>
                    <el-dropdown-item command="approve" icon="el-icon-circle-check" v-if="scope.row.recommendationStatus === 'REVIEWED'">批准</el-dropdown-item>
                    <el-dropdown-item command="reject" icon="el-icon-circle-close" v-if="scope.row.recommendationStatus === 'REVIEWED'">拒绝</el-dropdown-item>
                    <el-dropdown-item command="implement" icon="el-icon-success" v-if="scope.row.recommendationStatus === 'APPROVED'">实施</el-dropdown-item>
                    <el-dropdown-item command="delete" icon="el-icon-delete">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <el-pagination
            background
            class="pagination"
            :current-page="recommendationQuery.pageNum"
            :layout="layout"
            :page-size="recommendationQuery.pageSize"
            :total="recommendationTotal"
            @current-change="handleRecommendationCurrentChange"
            @size-change="handleRecommendationSizeChange"
          />
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改决策模型对话框 -->
    <el-dialog :title="modelTitle" :visible.sync="modelOpen" width="800px" append-to-body>
      <el-form ref="modelForm" :model="modelForm" :rules="modelRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="模型名称" prop="modelName">
              <el-input v-model="modelForm.modelName" placeholder="请输入模型名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型类型" prop="modelType">
              <el-select v-model="modelForm.modelType" placeholder="请选择模型类型">
                <el-option label="风险评估模型" value="RISK_ASSESSMENT" />
                <el-option label="投资决策模型" value="INVESTMENT_DECISION" />
                <el-option label="融资决策模型" value="FINANCING_DECISION" />
                <el-option label="现金流预测模型" value="CASHFLOW_FORECAST" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="算法类型" prop="algorithmType">
              <el-select v-model="modelForm.algorithmType" placeholder="请选择算法类型">
                <el-option label="线性回归" value="LINEAR_REGRESSION" />
                <el-option label="逻辑回归" value="LOGISTIC_REGRESSION" />
                <el-option label="决策树" value="DECISION_TREE" />
                <el-option label="随机森林" value="RANDOM_FOREST" />
                <el-option label="神经网络" value="NEURAL_NETWORK" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型状态" prop="modelStatus">
              <el-select v-model="modelForm.modelStatus" placeholder="请选择模型状态">
                <el-option label="启用" value="ACTIVE" />
                <el-option label="停用" value="INACTIVE" />
                <el-option label="测试中" value="TESTING" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="模型描述" prop="modelDescription">
          <el-input v-model="modelForm.modelDescription" type="textarea" placeholder="请输入模型描述" />
        </el-form-item>
        <el-form-item label="输入参数" prop="inputParameters">
          <el-input v-model="modelForm.inputParameters" type="textarea" placeholder="请输入输入参数（JSON格式）" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="modelForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitModelForm">确 定</el-button>
        <el-button @click="cancelModel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 模型配置对话框 -->
    <el-dialog title="模型配置" :visible.sync="modelConfigOpen" width="900px" append-to-body>
      <el-form ref="modelConfigForm" :model="modelConfigForm" label-width="120px">
        <el-form-item label="模型名称">
          <el-input v-model="modelConfigForm.modelName" disabled />
        </el-form-item>
        <el-form-item label="输入参数">
          <el-input
            v-model="modelConfigForm.inputParameters"
            type="textarea"
            :rows="4"
            placeholder="请输入输入参数配置（JSON格式）&#10;例如：{&quot;param1&quot;: &quot;value1&quot;, &quot;param2&quot;: &quot;value2&quot;}"
          />
        </el-form-item>
        <el-form-item label="输出参数">
          <el-input
            v-model="modelConfigForm.outputParameters"
            type="textarea"
            :rows="4"
            placeholder="请输入输出参数配置（JSON格式）"
          />
        </el-form-item>
        <el-form-item label="模型公式">
          <el-input
            v-model="modelConfigForm.modelFormula"
            type="textarea"
            :rows="3"
            placeholder="请输入模型计算公式"
          />
        </el-form-item>
        <el-form-item label="训练数据">
          <el-input
            v-model="modelConfigForm.trainingData"
            type="textarea"
            :rows="5"
            placeholder="请输入训练数据（JSON格式）"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitModelConfig">保 存</el-button>
        <el-button @click="modelConfigOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 模型测试对话框 -->
    <el-dialog title="模型测试" :visible.sync="modelTestOpen" width="900px" append-to-body>
      <el-form ref="modelTestForm" :model="modelTestForm" label-width="120px">
        <el-form-item label="模型名称">
          <el-input v-model="modelTestForm.modelName" disabled />
        </el-form-item>
        <el-form-item label="测试数据">
          <el-input
            v-model="modelTestForm.testData"
            type="textarea"
            :rows="6"
            placeholder="请输入测试数据（JSON格式）&#10;例如：{&quot;input1&quot;: 100, &quot;input2&quot;: 200}"
          />
        </el-form-item>
        <el-form-item label="测试结果">
          <el-input
            v-model="modelTestForm.testResult"
            type="textarea"
            :rows="8"
            placeholder="点击"执行测试"按钮后显示测试结果"
            disabled
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="executeModelTest" :loading="modelTestLoading">执行测试</el-button>
        <el-button @click="modelTestOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改数据分析任务对话框 -->
    <el-dialog :title="taskTitle" :visible.sync="taskOpen" width="800px" append-to-body>
      <el-form ref="taskForm" :model="taskForm" :rules="taskRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务编号" prop="taskNo">
              <el-input v-model="taskForm.taskNo" placeholder="请输入任务编号" :disabled="taskForm.taskId != null" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务名称" prop="taskName">
              <el-input v-model="taskForm.taskName" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务类型" prop="taskType">
              <el-select v-model="taskForm.taskType" placeholder="请选择任务类型">
                <el-option
                  v-for="dict in taskType"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分析类型" prop="analysisType">
              <el-input v-model="taskForm.analysisType" placeholder="请输入分析类型" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据源" prop="dataSource">
              <el-input v-model="taskForm.dataSource" placeholder="请输入数据源" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据范围" prop="dataRange">
              <el-input v-model="taskForm.dataRange" placeholder="请输入数据范围" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="调度类型" prop="scheduleType">
              <el-select v-model="taskForm.scheduleType" placeholder="请选择调度类型">
                <el-option
                  v-for="dict in scheduleType"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调度表达式" prop="scheduleCron" v-if="taskForm.scheduleType === 'SCHEDULED'">
              <el-input v-model="taskForm.scheduleCron" placeholder="请输入调度表达式" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="结果格式" prop="resultFormat">
              <el-select v-model="taskForm.resultFormat" placeholder="请选择结果格式">
                <el-option
                  v-for="dict in resultFormat"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型ID" prop="modelId">
              <el-input v-model="taskForm.modelId" placeholder="请输入模型ID" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="分析参数" prop="analysisParameters">
          <el-input v-model="taskForm.analysisParameters" type="textarea" :rows="3" placeholder="请输入分析参数（JSON格式）" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="taskForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitTaskForm">确 定</el-button>
        <el-button @click="cancelTask">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改KPI指标对话框 -->
    <el-dialog :title="kpiTitle" :visible.sync="kpiOpen" width="900px" append-to-body>
      <el-form ref="kpiForm" :model="kpiForm" :rules="kpiRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="KPI编码" prop="kpiCode">
              <el-input v-model="kpiForm.kpiCode" placeholder="请输入KPI编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="KPI名称" prop="kpiName">
              <el-input v-model="kpiForm.kpiName" placeholder="请输入KPI名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="KPI分类" prop="kpiCategory">
              <el-select v-model="kpiForm.kpiCategory" placeholder="请选择KPI分类">
                <el-option
                  v-for="dict in kpiCategoryOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="KPI类型" prop="kpiType">
              <el-select v-model="kpiForm.kpiType" placeholder="请选择KPI类型">
                <el-option
                  v-for="dict in kpiTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="计算公式" prop="calculationFormula">
              <el-input v-model="kpiForm.calculationFormula" type="textarea" :rows="2" placeholder="请输入计算公式" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据源" prop="dataSource">
              <el-input v-model="kpiForm.dataSource" placeholder="请输入数据源" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="kpiForm.unit" placeholder="请输入单位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="目标值" prop="targetValue">
              <el-input-number v-model="kpiForm.targetValue" :precision="2" :step="0.01" placeholder="目标值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预警阈值" prop="warningThreshold">
              <el-input-number v-model="kpiForm.warningThreshold" :precision="2" :step="0.01" placeholder="预警阈值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="临界阈值" prop="criticalThreshold">
              <el-input-number v-model="kpiForm.criticalThreshold" :precision="2" :step="0.01" placeholder="临界阈值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="当前值" prop="currentValue">
              <el-input-number v-model="kpiForm.currentValue" :precision="2" :step="0.01" placeholder="当前值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="计算频率" prop="calculationFrequency">
              <el-select v-model="kpiForm.calculationFrequency" placeholder="请选择计算频率">
                <el-option
                  v-for="dict in calculationFrequencyOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="计算日期" prop="calculationDate">
              <el-date-picker
                v-model="kpiForm.calculationDate"
                type="date"
                placeholder="选择计算日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="KPI状态" prop="kpiStatus">
              <el-select v-model="kpiForm.kpiStatus" placeholder="请选择KPI状态">
                <el-option
                  v-for="dict in kpiStatusOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="kpiForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitKpiForm">确 定</el-button>
        <el-button @click="cancelKpi">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改预测分析对话框 -->
    <el-dialog :title="predictiveTitle" :visible.sync="predictiveOpen" width="900px" append-to-body>
      <el-form ref="predictiveForm" :model="predictiveForm" :rules="predictiveRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="分析编号" prop="analysisNo">
              <el-input v-model="predictiveForm.analysisNo" placeholder="请输入分析编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分析名称" prop="analysisName">
              <el-input v-model="predictiveForm.analysisName" placeholder="请输入分析名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预测类型" prop="predictionType">
              <el-select v-model="predictiveForm.predictionType" placeholder="请选择预测类型">
                <el-option
                  v-for="dict in predictionTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型ID" prop="modelId">
              <el-input-number v-model="predictiveForm.modelId" placeholder="请输入模型ID" :min="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预测周期" prop="predictionPeriod">
              <el-select v-model="predictiveForm.predictionPeriod" placeholder="请选择预测周期">
                <el-option
                  v-for="dict in predictionPeriodOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预测期限" prop="predictionHorizon">
              <el-input-number v-model="predictiveForm.predictionHorizon" placeholder="请输入预测期限(天)" :min="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="置信水平" prop="confidenceLevel">
              <el-input-number v-model="predictiveForm.confidenceLevel" placeholder="请输入置信水平" :min="0" :max="100" :precision="2" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分析状态" prop="analysisStatus">
              <el-select v-model="predictiveForm.analysisStatus" placeholder="请选择分析状态">
                <el-option
                  v-for="dict in analysisStatusOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="输入数据" prop="inputData">
              <el-input v-model="predictiveForm.inputData" type="textarea" :rows="4" placeholder="请输入预测分析的输入数据(JSON格式)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="预测结果" prop="predictionResult">
              <el-input v-model="predictiveForm.predictionResult" type="textarea" :rows="3" placeholder="预测结果(JSON格式)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="实际结果" prop="actualResult">
              <el-input v-model="predictiveForm.actualResult" type="textarea" :rows="3" placeholder="实际结果(JSON格式)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="置信区间" prop="confidenceInterval">
              <el-input v-model="predictiveForm.confidenceInterval" type="textarea" :rows="2" placeholder="置信区间(JSON格式)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="偏差分析" prop="deviationAnalysis">
              <el-input v-model="predictiveForm.deviationAnalysis" type="textarea" :rows="3" placeholder="偏差分析结果" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="predictiveForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitPredictiveForm">确 定</el-button>
        <el-button @click="cancelPredictive">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改决策建议对话框 -->
    <el-dialog :title="recommendationTitle" :visible.sync="recommendationOpen" width="800px" append-to-body>
      <el-form ref="recommendationForm" :model="recommendationForm" :rules="recommendationRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="建议编号" prop="recommendationNo">
              <el-input v-model="recommendationForm.recommendationNo" placeholder="请输入建议编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="建议名称" prop="recommendationName">
              <el-input v-model="recommendationForm.recommendationName" placeholder="请输入建议名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="建议类型" prop="recommendationType">
              <el-select v-model="recommendationForm.recommendationType" placeholder="请选择建议类型">
                <el-option
                  v-for="dict in recommendationTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="recommendationForm.priority" placeholder="请选择优先级">
                <el-option
                  v-for="dict in priorityOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="模型ID" prop="modelId">
              <el-input-number v-model="recommendationForm.modelId" placeholder="模型ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分析ID" prop="analysisId">
              <el-input-number v-model="recommendationForm.analysisId" placeholder="分析ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="置信水平" prop="confidenceLevel">
              <el-input-number v-model="recommendationForm.confidenceLevel" :min="0" :max="100" :precision="2" placeholder="置信水平" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="建议状态" prop="recommendationStatus">
              <el-select v-model="recommendationForm.recommendationStatus" placeholder="请选择建议状态">
                <el-option
                  v-for="dict in recommendationStatusOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="建议内容" prop="recommendationContent">
              <el-input v-model="recommendationForm.recommendationContent" type="textarea" :rows="4" placeholder="请输入建议内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="预期收益" prop="expectedBenefit">
              <el-input v-model="recommendationForm.expectedBenefit" type="textarea" :rows="3" placeholder="请输入预期收益" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="风险评估" prop="riskAssessment">
              <el-input v-model="recommendationForm.riskAssessment" type="textarea" :rows="3" placeholder="请输入风险评估" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="实施计划" prop="implementationPlan">
              <el-input v-model="recommendationForm.implementationPlan" type="textarea" :rows="3" placeholder="请输入实施计划" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="recommendationForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRecommendationForm">确 定</el-button>
        <el-button @click="cancelRecommendation">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  getDecisionModelPage,
  getDecisionModel,
  createDecisionModel,
  updateDecisionModel,
  deleteDecisionModel,
  batchDeleteDecisionModels,
  exportDecisionModelData,
  trainDecisionModel,
  getDataAnalysisTaskPage,
  getDataAnalysisTask,
  createDataAnalysisTask,
  updateDataAnalysisTask,
  deleteDataAnalysisTask,
  batchDeleteDataAnalysisTasks,
  executeDataAnalysisTask,
  cancelDataAnalysisTask,
  retryFailedTask,
  getPendingTasks,
  getRunningTasks,
  getFailedTasks,
  getTaskStatistics,
  getKpiIndicatorPage,
  getKpiIndicator,
  createKpiIndicator,
  updateKpiIndicator,
  deleteKpiIndicator,
  batchDeleteKpiIndicators,
  calculateKpiIndicator,
  batchCalculateKpiIndicators,
  updateKpiCurrentValue,
  updateKpiStatus,
  getKpisByCategory,
  getKpisByType,
  getWarningKpis,
  getCriticalKpis,
  getKpiStatistics,
  getKpiTypeDistribution,
  getKpiStatusDistribution,
  getKpiTargetCompletion,
  getKpiHistory,
  getKpiRanking,
  getKpiWarningStatistics,
  exportKpiData,
  performKpiHealthCheck,
  // 预测分析API
  getPredictiveAnalysisPage,
  getPredictiveAnalysisById,
  createPredictiveAnalysis,
  updatePredictiveAnalysis,
  deletePredictiveAnalysis,
  deletePredictiveAnalyses,
  executePredictiveAnalysis,
  batchExecutePredictiveAnalysis,
  cancelPredictiveAnalysis,
  retryFailedPredictiveAnalysis,
  updateActualResult,
  calculatePredictionAccuracy,
  getPredictiveAnalysisStatistics,
  getPredictionTypeDistribution,
  getAnalysisStatusDistribution,
  getPredictionTrend,
  getPredictionPerformance,
  getAccuracyRanking,
  getAbnormalPredictions,
  performPredictiveAnalysisHealthCheck,
  exportPredictiveAnalysisData,
  // 决策建议相关API
  getDecisionRecommendationPage,
  getDecisionRecommendationById,
  createDecisionRecommendation,
  updateDecisionRecommendation,
  deleteDecisionRecommendation,
  deleteDecisionRecommendations,
  reviewDecisionRecommendation,
  approveDecisionRecommendation,
  rejectDecisionRecommendation,
  implementDecisionRecommendation,
  batchReviewDecisionRecommendations,
  batchImplementDecisionRecommendations,
  updateImplementationResult,
  getDecisionRecommendationStatistics,
  getRecommendationTypeDistribution,
  getRecommendationStatusDistribution,
  getPriorityDistribution,
  getConfidenceStatistics,
  getRecommendationTrend,
  getRecommendationEffectiveness,
  getConfidenceRanking,
  getImplementationRateStatistics,
  getRecommendationQualityAssessment,
  getAbnormalRecommendations,
  performDecisionRecommendationHealthCheck,
  exportDecisionRecommendationData
} from "@/api/globalTreasurer/decisionSupport";
import { parseTime } from '@/utils'
import DictTag from '@/components/DictTag'
import Pagination from '@/components/Pagination'
import RightToolbar from '@/components/RightToolbar'

export default {
  name: "DecisionSupport",
  components: {
    DictTag,
    Pagination,
    RightToolbar
  },
  directives: {
    hasPermi: {
      inserted(el, binding) {
        const { value } = binding;
        const permissions = JSON.parse(sessionStorage.getItem('permissions') || '[]');
        if (value && value instanceof Array && value.length > 0) {
          const hasPermission = permissions.some(permission => {
            return value.includes(permission);
          });
          if (!hasPermission) {
            el.parentNode && el.parentNode.removeChild(el);
          }
        }
      }
    }
  },
  data() {
    return {
      // 当前激活的标签页
      activeTab: "kpiIndicator",
      // 显示搜索条件
      showSearch: true,
      // 分页布局
      layout: "total, sizes, prev, pager, next, jumper",
      
      // 决策模型相关数据
      modelLoading: true,
      modelIds: [],
      modelSingle: true,
      modelMultiple: true,
      modelTotal: 0,
      modelList: [],
      modelTitle: "",
      modelOpen: false,
      modelQuery: {
        pageNum: 1,
        pageSize: 10,
        modelName: null,
        modelType: null,
        modelStatus: null
      },
      modelForm: {},
      modelRules: {
        modelName: [
          { required: true, message: "模型名称不能为空", trigger: "blur" }
        ],
        modelType: [
          { required: true, message: "模型类型不能为空", trigger: "change" }
        ],
        algorithmType: [
          { required: true, message: "算法类型不能为空", trigger: "change" }
        ],
        modelStatus: [
          { required: true, message: "模型状态不能为空", trigger: "change" }
        ]
      },

      // 模型配置相关数据
      modelConfigOpen: false,
      modelConfigForm: {
        modelId: null,
        modelName: null,
        inputParameters: null,
        outputParameters: null,
        modelFormula: null,
        trainingData: null
      },

      // 模型测试相关数据
      modelTestOpen: false,
      modelTestForm: {
        modelId: null,
        modelName: null,
        testData: null,
        testResult: null
      },
      modelTestLoading: false,
      
      // 数据分析任务相关数据
      taskLoading: false,
      taskIds: [],
      taskSingle: true,
      taskMultiple: true,
      taskTotal: 0,
      taskList: [],
      taskTitle: "",
      taskOpen: false,
      taskQuery: {
        pageNum: 1,
        pageSize: 10,
        taskNo: null,
        taskName: null,
        taskType: null,
        analysisType: null,
        taskStatus: null,
        scheduleType: null,
        dataSource: null,
        modelId: null,
        executeUser: null,
        startDate: null,
        endDate: null,
        orgId: null
      },
      taskForm: {
        taskId: null,
        taskNo: null,
        taskName: null,
        taskType: null,
        analysisType: null,
        dataSource: null,
        dataRange: null,
        analysisParameters: null,
        modelId: null,
        scheduleType: 'MANUAL',
        scheduleCron: null,
        resultFormat: 'JSON',
        remark: null,
        orgId: null
      },
      taskRules: {
        taskNo: [
          { required: true, message: "任务编号不能为空", trigger: "blur" }
        ],
        taskName: [
          { required: true, message: "任务名称不能为空", trigger: "blur" }
        ],
        taskType: [
          { required: true, message: "任务类型不能为空", trigger: "change" }
        ],
        analysisType: [
          { required: true, message: "分析类型不能为空", trigger: "blur" }
        ]
      },

      // 字典选项
      modelTypeOptions: [
        { label: "风险评估模型", value: "RISK_ASSESSMENT" },
        { label: "投资决策模型", value: "INVESTMENT_DECISION" },
        { label: "融资决策模型", value: "FINANCING_DECISION" },
        { label: "现金流预测模型", value: "CASHFLOW_FORECAST" }
      ],
      modelStatusOptions: [
        { label: "启用", value: "ACTIVE" },
        { label: "停用", value: "INACTIVE" },
        { label: "测试中", value: "TESTING" }
      ],
      taskType: [
        { label: "描述性分析", value: "DESCRIPTIVE" },
        { label: "诊断性分析", value: "DIAGNOSTIC" },
        { label: "预测性分析", value: "PREDICTIVE" },
        { label: "规范性分析", value: "PRESCRIPTIVE" }
      ],
      taskStatus: [
        { label: "待执行", value: "PENDING" },
        { label: "执行中", value: "RUNNING" },
        { label: "已完成", value: "COMPLETED" },
        { label: "执行失败", value: "FAILED" },
        { label: "已取消", value: "CANCELLED" }
      ],
      scheduleType: [
        { label: "手动执行", value: "MANUAL" },
        { label: "定时调度", value: "SCHEDULED" },
        { label: "触发执行", value: "TRIGGERED" }
      ],
      resultFormat: [
        { label: "JSON格式", value: "JSON" },
        { label: "XML格式", value: "XML" },
        { label: "CSV格式", value: "CSV" },
        { label: "Excel格式", value: "EXCEL" }
      ],

      // KPI指标相关数据
      kpiLoading: false,
      kpiIds: [],
      kpiSingle: true,
      kpiMultiple: true,
      kpiTotal: 0,
      kpiList: [],
      kpiTitle: "",
      kpiOpen: false,
      kpiQuery: {
        pageNum: 1,
        pageSize: 10,
        kpiCode: null,
        kpiName: null,
        kpiCategory: null,
        kpiType: null,
        kpiStatus: null,
        calculationFrequency: null,
        trend: null,
        dataSource: null,
        startDate: null,
        endDate: null,
        orgId: 1
      },
      kpiForm: {},
      kpiRules: {
        kpiCode: [
          { required: true, message: "KPI编码不能为空", trigger: "blur" }
        ],
        kpiName: [
          { required: true, message: "KPI名称不能为空", trigger: "blur" }
        ],
        kpiCategory: [
          { required: true, message: "KPI分类不能为空", trigger: "change" }
        ],
        kpiType: [
          { required: true, message: "KPI类型不能为空", trigger: "change" }
        ],
        calculationFormula: [
          { required: true, message: "计算公式不能为空", trigger: "blur" }
        ]
      },

      // KPI字典选项
      kpiCategoryOptions: [
        { label: "财务指标", value: "FINANCIAL" },
        { label: "运营指标", value: "OPERATIONAL" },
        { label: "风险指标", value: "RISK" },
        { label: "绩效指标", value: "PERFORMANCE" },
        { label: "客户指标", value: "CUSTOMER" },
        { label: "流程指标", value: "PROCESS" }
      ],
      kpiTypeOptions: [
        { label: "财务类", value: "FINANCIAL" },
        { label: "运营类", value: "OPERATIONAL" },
        { label: "风险类", value: "RISK" },
        { label: "绩效类", value: "PERFORMANCE" }
      ],
      kpiStatusOptions: [
        { label: "正常", value: "NORMAL" },
        { label: "预警", value: "WARNING" },
        { label: "临界", value: "CRITICAL" },
        { label: "停用", value: "INACTIVE" }
      ],
      calculationFrequencyOptions: [
        { label: "实时", value: "REAL_TIME" },
        { label: "小时", value: "HOURLY" },
        { label: "日", value: "DAILY" },
        { label: "周", value: "WEEKLY" },
        { label: "月", value: "MONTHLY" }
      ],

      // 预测分析相关数据
      predictiveLoading: true,
      predictiveIds: [],
      predictiveSingle: true,
      predictiveMultiple: true,
      predictiveTotal: 0,
      predictiveList: [],
      tableHeight: null,
      predictiveTitle: "",
      predictiveOpen: false,
      predictiveShowSearch: true,
      predictiveDateRange: [],
      predictiveQuery: {
        pageNum: 1,
        pageSize: 10,
        analysisNo: null,
        analysisName: null,
        predictionType: null,
        analysisStatus: null,
        predictionPeriod: null,
        startDate: null,
        endDate: null,
        orgId: 1
      },
      predictiveForm: {},
      predictiveRules: {
        analysisNo: [
          { required: true, message: "分析编号不能为空", trigger: "blur" }
        ],
        analysisName: [
          { required: true, message: "分析名称不能为空", trigger: "blur" }
        ],
        predictionType: [
          { required: true, message: "预测类型不能为空", trigger: "change" }
        ],
        modelId: [
          { required: true, message: "模型ID不能为空", trigger: "blur" }
        ],
        inputData: [
          { required: true, message: "输入数据不能为空", trigger: "blur" }
        ],
        predictionPeriod: [
          { required: true, message: "预测周期不能为空", trigger: "change" }
        ],
        predictionHorizon: [
          { required: true, message: "预测期限不能为空", trigger: "blur" }
        ]
      },
      predictionTypeOptions: [
        { label: "现金流预测", value: "CASH_FLOW" },
        { label: "汇率预测", value: "EXCHANGE_RATE" },
        { label: "利率预测", value: "INTEREST_RATE" },
        { label: "风险指标预测", value: "RISK_METRIC" },
        { label: "市场趋势预测", value: "MARKET_TREND" }
      ],
      analysisStatusOptions: [
        { label: "待执行", value: "PENDING" },
        { label: "执行中", value: "RUNNING" },
        { label: "已完成", value: "COMPLETED" },
        { label: "执行失败", value: "FAILED" }
      ],
      predictionPeriodOptions: [
        { label: "日", value: "DAILY" },
        { label: "周", value: "WEEKLY" },
        { label: "月", value: "MONTHLY" },
        { label: "季", value: "QUARTERLY" },
        { label: "年", value: "YEARLY" }
      ],

      // 决策建议数据
      recommendationLoading: true,
      recommendationIds: [],
      recommendationSingle: true,
      recommendationMultiple: true,
      recommendationTotal: 0,
      recommendationList: [],
      recommendationTitle: "",
      recommendationOpen: false,
      recommendationShowSearch: true,
      recommendationDateRange: [],
      recommendationQuery: {
        pageNum: 1,
        pageSize: 10,
        recommendationNo: null,
        recommendationName: null,
        recommendationType: null,
        recommendationStatus: null,
        priority: null,
        modelId: null,
        analysisId: null,
        startDate: null,
        endDate: null,
        minConfidence: null,
        maxConfidence: null,
        orgId: 1
      },
      recommendationForm: {},
      recommendationRules: {
        recommendationNo: [
          { required: true, message: "建议编号不能为空", trigger: "blur" },
          { max: 50, message: "建议编号长度不能超过50个字符", trigger: "blur" }
        ],
        recommendationName: [
          { required: true, message: "建议名称不能为空", trigger: "blur" },
          { max: 200, message: "建议名称长度不能超过200个字符", trigger: "blur" }
        ],
        recommendationType: [
          { required: true, message: "建议类型不能为空", trigger: "change" }
        ],
        recommendationContent: [
          { required: true, message: "建议内容不能为空", trigger: "blur" }
        ],
        priority: [
          { required: true, message: "优先级不能为空", trigger: "change" }
        ],
        confidenceLevel: [
          { type: 'number', min: 0, max: 100, message: "置信水平必须在0-100之间", trigger: "blur" }
        ]
      },
      recommendationTypeOptions: [
        { label: "投资建议", value: "INVESTMENT" },
        { label: "融资建议", value: "FINANCING" },
        { label: "风险控制", value: "RISK_CONTROL" },
        { label: "资金管理", value: "CASH_MANAGEMENT" },
        { label: "优化建议", value: "OPTIMIZATION" }
      ],
      recommendationStatusOptions: [
        { label: "待审核", value: "PENDING" },
        { label: "已审核", value: "REVIEWED" },
        { label: "已批准", value: "APPROVED" },
        { label: "已拒绝", value: "REJECTED" },
        { label: "已实施", value: "IMPLEMENTED" }
      ],
      priorityOptions: [
        { label: "高", value: "HIGH" },
        { label: "中", value: "MEDIUM" },
        { label: "低", value: "LOW" }
      ]
    };
  },
  created() {
    // 初始化表格高度
    const baseHeight = 50; // 表头高度
    const rowHeight = 48;  // 每行高度
    const padding = 20;    // 额外空间
    this.tableHeight = baseHeight + (10 * rowHeight) + padding; // 默认10条/页

    // 根据当前激活的标签页初始化数据
    if (this.activeTab === 'decisionModel') {
      this.getModelList();
    } else if (this.activeTab === 'dataAnalysis') {
      this.getTaskList();
    } else if (this.activeTab === 'kpiIndicator') {
      this.getKpiList();
    } else if (this.activeTab === 'predictiveAnalysis') {
      this.getPredictiveList();
    } else if (this.activeTab === 'decisionRecommendation') {
      this.getRecommendationList();
    } else {
      // 默认加载决策模型
      this.getModelList();
    }
  },
  methods: {
    parseTime,

    // ==================== 通用方法 ====================

    /**
     * 统一处理分页响应数据
     * @param {Object} response - API响应对象
     * @returns {Object} - {list: [], total: 0, success: boolean, message: string}
     */
    parsePageResponse(response) {
      const successCodes = [200, 0, '200', '0', '1', 1, 2];

      if (!successCodes.includes(response.code)) {
        return {
          list: [],
          total: 0,
          success: false,
          message: response.message || response.msg || '查询失败'
        };
      }

      let list = [];
      let total = 0;

      if (response.data) {
        // 优先处理 MyBatis-Plus IPage 标准格式
        if (response.data.records !== undefined) {
          // IPage格式: {code: 1, data: {records: [], total: 100, current: 1, size: 10}}
          list = response.data.records || [];
          total = parseInt(response.data.total) || 0;
          console.log('[parsePageResponse] 使用MyBatis-Plus IPage格式 - total:', total, 'records:', list.length);
        } else if (response.data.tlist !== undefined) {
          // JsonBean格式: {code: 1, data: {tlist: [], totalRecord: 100}}
          list = response.data.tlist || [];
          total = parseInt(response.data.totalRecord) || 0;
          console.log('[parsePageResponse] 使用JsonBean格式 - total:', total, 'tlist:', list.length);
        } else if (response.data.list !== undefined) {
          // 标准格式: {code: 200, data: {list: [], total: 100}}
          list = response.data.list || [];
          total = parseInt(response.data.total) || 0;
          console.log('[parsePageResponse] 使用标准格式 - total:', total, 'list:', list.length);
        } else if (Array.isArray(response.data)) {
          // 数组格式: {code: 200, data: []}
          list = response.data || [];
          total = response.data.length || 0;
          console.log('[parsePageResponse] 使用数组格式 - total:', total);
        }

        // 兜底逻辑：如果total为0但有size字段，使用size作为total
        if ((total === 0 || !total) && response.data.size && response.data.size > 0) {
          console.log('[parsePageResponse] total为0，使用size作为total:', response.data.size);
          total = parseInt(response.data.size) || 0;
        }
      } else if (response.rows !== undefined) {
        // TableDataInfo格式: {code: 200, rows: [], total: 100}
        list = response.rows || [];
        total = parseInt(response.total) || 0;
        console.log('[parsePageResponse] 使用TableDataInfo格式 - total:', total);
      } else if (response.tlist !== undefined) {
        // 直接JsonBean格式: {code: 1, tlist: [], totalRecord: 100}
        list = response.tlist || [];
        total = parseInt(response.totalRecord) || 0;
        console.log('[parsePageResponse] 使用直接JsonBean格式 - total:', total);
      }

      // 兜底逻辑：如果total仍然为0但list有数据，使用list.length作为total
      if ((total === 0 || !total) && list && list.length > 0) {
        console.log('[parsePageResponse] total仍为0，使用list.length作为total:', list.length);
        total = list.length;
      }

      return { list, total, success: true };
    },

    /** 获取模型类型标签 */
    getModelTypeLabel(value) {
      const option = this.modelTypeOptions.find(item => item.value === value);
      return option ? option.label : value;
    },

    /** 获取模型状态标签 */
    getModelStatusLabel(value) {
      const option = this.modelStatusOptions.find(item => item.value === value);
      return option ? option.label : value;
    },

    /** 获取模型状态标签类型 */
    getModelStatusType(value) {
      const typeMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'TESTING': 'warning'
      };
      return typeMap[value] || 'info';
    },

    /** 获取模型类型标签类型 */
    getModelTypeTagType(value) {
      const typeMap = {
        'RISK_ASSESSMENT': 'warning',
        'INVESTMENT_DECISION': 'success',
        'FINANCING_DECISION': 'primary',
        'CASHFLOW_FORECAST': 'info'
      };
      return typeMap[value] || '';
    },

    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'decisionModel') {
        this.getModelList();
      } else if (tab.name === 'dataAnalysis') {
        this.getTaskList();
      } else if (tab.name === 'kpiIndicator') {
        this.getKpiList();
      } else if (tab.name === 'predictiveAnalysis') {
        this.getPredictiveList();
      } else if (tab.name === 'decisionRecommendation') {
        this.getRecommendationList();
      }
    },

    /** 查询决策模型列表 */
    async getModelList() {
      this.modelLoading = true;
      try {
        // 构建查询参数对象
        const params = {
          pageNum: this.modelQuery.pageNum,
          pageSize: this.modelQuery.pageSize
        };

        // 只添加有值的查询条件（排除空字符串、null、undefined）
        if (this.modelQuery.modelName) {
          params.modelName = this.modelQuery.modelName;
        }
        if (this.modelQuery.modelType) {
          params.modelType = this.modelQuery.modelType;
        }
        if (this.modelQuery.modelStatus) {
          params.modelStatus = this.modelQuery.modelStatus;
        }

        console.log('[决策模型] 查询参数:', params);
        console.log('[决策模型] 当前页码:', this.modelQuery.pageNum, '每页条数:', this.modelQuery.pageSize);
        const response = await getDecisionModelPage(params);

        // 详细打印响应数据结构
        console.log('[决策模型] 原始response:', response);
        console.log('[决策模型] response.code:', response.code);
        console.log('[决策模型] response.data:', response.data);
        if (response.data) {
          console.log('[决策模型] response.data类型:', typeof response.data);
          console.log('[决策模型] response.data.records:', response.data?.records);
          console.log('[决策模型] response.data.total:', response.data?.total);
          console.log('[决策模型] response.data.current:', response.data?.current);
          console.log('[决策模型] response.data.size:', response.data?.size);
          console.log('[决策模型] response.data.records长度:', response.data?.records?.length);
        }

        // 使用统一方法解析数据
        const { list, total, success, message } = this.parsePageResponse(response);
        console.log('[决策模型] 解析后list长度:', list.length, 'total:', total);
        this.modelList = list;
        this.modelTotal = total;

        if (success) {
          console.log('[决策模型] 查询成功 - 共', total, '条数据，当前页显示', list.length, '条');
        } else {
          this.$message.error(message);
        }
      } catch (error) {
        console.error('[决策模型] 查询失败:', error);
        this.$message.error('获取数据失败：' + error.message);
        this.modelList = [];
        this.modelTotal = 0;
      } finally {
        this.modelLoading = false;
      }
    },

    /** 搜索按钮操作 */
    handleModelQuery() {
      this.modelQuery.pageNum = 1;
      this.getModelList();
    },

    /** 重置按钮操作 */
    resetModelQuery() {
      this.resetForm("modelQueryForm");
      this.handleModelQuery();
    },

    /** 多选框选中数据 */
    handleModelSelectionChange(selection) {
      this.modelIds = selection.map(item => item.modelId);
      this.modelSingle = selection.length !== 1;
      this.modelMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleModelAdd() {
      console.log('[DEBUG-新增模型] 点击新增按钮');
      this.resetModelForm();
      this.modelOpen = true;
      this.modelTitle = "添加决策模型";
      console.log('[DEBUG-新增模型] modelOpen:', this.modelOpen);
      console.log('[DEBUG-新增模型] modelTitle:', this.modelTitle);
    },

    /** 修改按钮操作（批量） */
    handleModelUpdateBatch() {
      if (this.modelIds.length === 0) {
        this.$message.warning("请选择要修改的模型");
        return;
      }
      const modelId = this.modelIds[0];
      this.handleModelUpdate({ modelId });
    },

    /** 修改按钮操作 */
    handleModelUpdate(row) {
      this.resetModelForm();
      const modelId = row.modelId || this.modelIds[0];
      getDecisionModel(modelId).then(response => {
        console.log('[DEBUG-修改模型] API响应:', response);
        if (response.code === 1) {
          this.modelForm = response.data;
          this.modelOpen = true;
          this.modelTitle = "修改决策模型";
        } else {
          this.$message.error(response.msg || "获取模型信息失败");
        }
      }).catch(error => {
        console.error('[ERROR-修改模型] 请求失败:', error);
        this.$message.error("获取模型信息失败");
      });
    },

    /** 查看按钮操作 */
    handleModelView(row) {
      this.$router.push(`/globalTreasurer/jczc/model/detail/${row.modelId}`);
    },

    /** 配置按钮操作（批量） */
    handleModelConfigBatch() {
      if (this.modelIds.length === 0) {
        this.$message.warning("请选择要配置的模型");
        return;
      }
      const modelId = this.modelIds[0];
      // 从列表中找到对应的模型
      const model = this.modelList.find(m => m.modelId === modelId);
      if (model) {
        this.handleModelConfig(model);
      }
    },

    /** 配置按钮操作 */
    handleModelConfig(row) {
      console.log('[DEBUG-配置模型] 选中的模型:', row);
      this.modelConfigForm = {
        modelId: row.modelId,
        modelName: row.modelName,
        inputParameters: row.inputParameters || '',
        outputParameters: row.outputParameters || '',
        modelFormula: row.modelFormula || '',
        trainingData: row.trainingData || ''
      };
      this.modelConfigOpen = true;
    },

    /** 提交模型配置 */
    submitModelConfig() {
      const configData = {
        modelId: this.modelConfigForm.modelId,
        inputParameters: this.modelConfigForm.inputParameters,
        outputParameters: this.modelConfigForm.outputParameters,
        modelFormula: this.modelConfigForm.modelFormula,
        trainingData: this.modelConfigForm.trainingData
      };

      console.log('[DEBUG-保存配置] 配置数据:', configData);

      updateDecisionModel(configData).then(response => {
        console.log('[DEBUG-保存配置] API响应:', response);
        if (response.code === 1) {
          this.$message.success("配置保存成功");
          this.modelConfigOpen = false;
          this.getModelList();
        } else {
          this.$message.error(response.msg || "配置保存失败");
        }
      }).catch(error => {
        console.error('[ERROR-保存配置] 请求失败:', error);
        this.$message.error("配置保存失败");
      });
    },

    /** 测试按钮操作（批量） */
    handleModelTestBatch() {
      if (this.modelIds.length === 0) {
        this.$message.warning("请选择要测试的模型");
        return;
      }
      const modelId = this.modelIds[0];
      // 从列表中找到对应的模型
      const model = this.modelList.find(m => m.modelId === modelId);
      if (model) {
        this.handleModelTest(model);
      }
    },

    /** 测试按钮操作 */
    handleModelTest(row) {
      console.log('[DEBUG-测试模型] 选中的模型:', row);
      this.modelTestForm = {
        modelId: row.modelId,
        modelName: row.modelName,
        testData: '',
        testResult: ''
      };
      this.modelTestOpen = true;
    },

    /** 执行模型测试 */
    executeModelTest() {
      if (!this.modelTestForm.testData) {
        this.$message.warning("请输入测试数据");
        return;
      }

      this.modelTestLoading = true;
      console.log('[DEBUG-执行测试] 测试数据:', this.modelTestForm.testData);

      // 调用后端测试接口
      trainDecisionModel(this.modelTestForm.modelId, this.modelTestForm.testData).then(response => {
        console.log('[DEBUG-执行测试] API响应:', response);
        this.modelTestLoading = false;

        if (response.code === 1) {
          // 模拟测试结果
          const testResult = {
            status: "success",
            accuracy: (Math.random() * 0.2 + 0.8).toFixed(4),
            executionTime: (Math.random() * 1000 + 500).toFixed(2) + "ms",
            result: response.data || "测试执行成功",
            timestamp: new Date().toLocaleString()
          };
          this.modelTestForm.testResult = JSON.stringify(testResult, null, 2);
          this.$message.success("测试执行成功");
        } else {
          this.modelTestForm.testResult = JSON.stringify({
            status: "error",
            message: response.msg || "测试执行失败"
          }, null, 2);
          this.$message.error(response.msg || "测试执行失败");
        }
      }).catch(error => {
        console.error('[ERROR-执行测试] 请求失败:', error);
        this.modelTestLoading = false;
        this.modelTestForm.testResult = JSON.stringify({
          status: "error",
          message: "测试执行失败: " + (error.message || "网络错误")
        }, null, 2);
        this.$message.error("测试执行失败");
      });
    },

    /** 删除按钮操作 */
    handleModelDelete(row) {
      const modelIds = row && row.modelId ? [row.modelId] : this.modelIds;
      const modelNames = row && row.modelName ? [row.modelName] : this.modelList.filter(item => modelIds.includes(item.modelId)).map(item => item.modelName);

      if (modelIds.length === 0) {
        this.$modal.msgWarning("请选择要删除的模型");
        return;
      }

      this.$modal.confirm('是否确认删除决策模型"' + modelNames.join('", "') + '"？').then(async () => {
        try {
          let response;
          if (modelIds.length === 1) {
            response = await deleteDecisionModel(modelIds[0]);
          } else {
            response = await batchDeleteDecisionModels(modelIds);
          }

          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$modal.msgSuccess("删除成功");
            this.getModelList();
          } else {
            this.$modal.msgError(response.message || response.msg || "删除失败");
          }
        } catch (error) {
          console.error('[ERROR-删除模型] 请求失败:', error);
          this.$modal.msgError("删除失败,请稍后重试");
        }
      }).catch(() => {
        console.log('用户取消删除操作');
      });
    },

    /** 提交按钮 */
    submitModelForm() {
      this.$refs["modelForm"].validate(valid => {
        if (valid) {
          if (this.modelForm.modelId != null) {
            updateDecisionModel(this.modelForm).then(response => {
              this.$message.success("修改成功");
              this.modelOpen = false;
              this.getModelList();
            });
          } else {
            createDecisionModel(this.modelForm).then(response => {
              this.$message.success("新增成功");
              this.modelOpen = false;
              this.getModelList();
            });
          }
        }
      });
    },

    /** 取消按钮 */
    cancelModel() {
      this.modelOpen = false;
      this.resetModelForm();
    },

    /** 表单重置 */
    resetModelForm() {
      this.modelForm = {
        modelId: null,
        modelName: null,
        modelType: null,
        algorithmType: null,
        modelStatus: "TESTING",
        modelDescription: null,
        inputParameters: null,
        remark: null
      };
      this.resetForm("modelForm");
    },

    /** 导出按钮操作 */
    async handleModelExport() {
      try {
        this.$modal.loading('正在导出数据,请稍后...');
        const response = await exportDecisionModelData(this.modelQuery);
        this.$modal.closeLoading();

        // 处理blob响应
        if (response instanceof Blob) {
          const url = window.URL.createObjectURL(response);
          const link = document.createElement('a');
          link.href = url;
          link.download = `决策模型_${new Date().getTime()}.xlsx`;
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          window.URL.revokeObjectURL(url);
          this.$modal.msgSuccess('导出成功');
        } else {
          this.$modal.msgError('导出失败：响应格式错误');
        }
      } catch (error) {
        this.$modal.closeLoading();
        console.error('导出决策模型数据失败:', error);
        this.$modal.msgError('导出失败,请稍后重试');
      }
    },

    /** 分页相关方法 */
    handleModelCurrentChange(val) {
      this.modelQuery.pageNum = val;
      this.getModelList();
    },

    handleModelSizeChange(val) {
      this.modelQuery.pageSize = val;
      this.getModelList();
    },

    // ==================== 数据分析任务管理方法 ====================

    /** 查询数据分析任务列表 */
    async getTaskList() {
      this.taskLoading = true;
      try {
        // 只传必要的分页参数 - 后端使用pageNo参数
        const params = {
          pageNo: this.taskQuery.pageNum,
          pageSize: this.taskQuery.pageSize
        };

        // 只添加有值的查询条件
        if (this.taskQuery.taskNo) params.taskNo = this.taskQuery.taskNo;
        if (this.taskQuery.taskName) params.taskName = this.taskQuery.taskName;
        if (this.taskQuery.taskType) params.taskType = this.taskQuery.taskType;
        if (this.taskQuery.taskStatus) params.taskStatus = this.taskQuery.taskStatus;

        console.log('[数据分析任务] 查询参数:', params);
        const response = await getDataAnalysisTaskPage(params);

        // 详细打印响应数据结构
        console.log('[数据分析任务] 原始response:', response);
        console.log('[数据分析任务] response.code:', response.code);
        console.log('[数据分析任务] response.data:', response.data);
        if (response.data) {
          console.log('[数据分析任务] response.data类型:', typeof response.data);
          console.log('[数据分析任务] response.data.records:', response.data?.records);
          console.log('[数据分析任务] response.data.tlist:', response.data?.tlist);
          console.log('[数据分析任务] response.data.total:', response.data?.total);
          console.log('[数据分析任务] response.data.totalRecord:', response.data?.totalRecord);
          console.log('[数据分析任务] response.data.current:', response.data?.current);
          console.log('[数据分析任务] response.data.pageNo:', response.data?.pageNo);
          console.log('[数据分析任务] response.data.size:', response.data?.size);
          console.log('[数据分析任务] response.data.records长度:', response.data?.records?.length);
          console.log('[数据分析任务] response.data.tlist长度:', response.data?.tlist?.length);
        }

        // 使用统一方法解析数据
        const { list, total, success, message } = this.parsePageResponse(response);
        console.log('[数据分析任务] 解析后list长度:', list.length, 'total:', total);
        this.taskList = list;
        this.taskTotal = total;

        if (success) {
          console.log('[数据分析任务] 查询成功 - 共', total, '条数据');
        } else {
          this.$message.error(message);
        }
      } catch (error) {
        console.error('[数据分析任务] 查询失败:', error);
        this.$message.error('获取数据失败：' + error.message);
        this.taskList = [];
        this.taskTotal = 0;
      } finally {
        this.taskLoading = false;
      }
    },

    /** 数据分析任务搜索按钮操作 */
    handleTaskQuery() {
      this.taskQuery.pageNum = 1;
      this.getTaskList();
    },

    /** 重置按钮操作 */
    resetTaskQuery() {
      this.resetForm("taskQueryForm");
      this.handleTaskQuery();
    },

    /** 多选框选中数据 */
    handleTaskSelectionChange(selection) {
      this.taskIds = selection.map(item => item.taskId);
      this.taskSingle = selection.length !== 1;
      this.taskMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleTaskAdd() {
      this.resetTaskForm();
      this.taskOpen = true;
      this.taskTitle = "添加数据分析任务";
    },

    /** 修改按钮操作 */
    handleTaskUpdate(row) {
      this.resetTaskForm();
      const taskId = row.taskId || this.taskIds[0];
      getDataAnalysisTask(taskId).then(response => {
        if (response.code === 1) {
          this.taskForm = response.data;
          this.taskOpen = true;
          this.taskTitle = "修改数据分析任务";
        } else {
          this.$modal.msgError(response.message || '获取任务信息失败');
        }
      }).catch(error => {
        console.error('获取任务信息失败:', error);
        this.$modal.msgError('获取任务信息失败，请稍后重试');
      });
    },

    /** 查看按钮操作 */
    handleTaskView(row) {
      this.resetTaskForm();
      const taskId = row.taskId;
      getDataAnalysisTask(taskId).then(response => {
        if (response.code === 1) {
          this.taskForm = response.data;
          this.taskOpen = true;
          this.taskTitle = "查看数据分析任务";
        } else {
          this.$modal.msgError(response.message || '获取任务信息失败');
        }
      }).catch(error => {
        console.error('获取任务信息失败:', error);
        this.$modal.msgError('获取任务信息失败，请稍后重试');
      });
    },

    /** 删除按钮操作 */
    handleTaskDelete(row) {
      const taskIds = row.taskId ? [row.taskId] : this.taskIds;
      this.$modal.confirm('是否确认删除任务编号为"' + taskIds + '"的数据项？').then(function() {
        return batchDeleteDataAnalysisTasks(taskIds);
      }).then((response) => {
        if (response.code === 1) {
          this.getTaskList();
          this.$modal.msgSuccess("删除成功");
        } else {
          this.$modal.msgError(response.message || '删除失败');
        }
      }).catch(error => {
        console.error('删除数据分析任务失败:', error);
        this.$modal.msgError('删除失败，请稍后重试');
      });
    },

    /** 执行任务按钮操作 */
    handleTaskExecute(row) {
      const taskId = row ? row.taskId : this.taskIds[0];
      this.$modal.confirm('是否确认执行该数据分析任务？').then(() => {
        return executeDataAnalysisTask(taskId, 1); // 假设当前用户ID为1
      }).then((response) => {
        if (response.code === 1) {
          this.getTaskList();
          this.$modal.msgSuccess("任务执行成功");
        } else {
          this.$modal.msgError(response.message || '任务执行失败');
        }
      }).catch(error => {
        console.error('执行数据分析任务失败:', error);
        this.$modal.msgError('任务执行失败，请稍后重试');
      });
    },

    /** 任务操作命令处理 */
    handleTaskCommand(command, row) {
      switch (command) {
        case 'execute':
          this.handleTaskExecute(row);
          break;
        case 'cancel':
          this.handleTaskCancel(row);
          break;
        case 'retry':
          this.handleTaskRetry(row);
          break;
        case 'delete':
          this.handleTaskDelete(row);
          break;
      }
    },

    /** 取消任务 */
    handleTaskCancel(row) {
      this.$modal.confirm('是否确认取消该数据分析任务？').then(() => {
        return cancelDataAnalysisTask(row.taskId, 1); // 假设当前用户ID为1
      }).then((response) => {
        if (response.code === 1) {
          this.getTaskList();
          this.$modal.msgSuccess("任务取消成功");
        } else {
          this.$modal.msgError(response.message || '任务取消失败');
        }
      }).catch(error => {
        console.error('取消数据分析任务失败:', error);
        this.$modal.msgError('任务取消失败，请稍后重试');
      });
    },

    /** 重试任务 */
    handleTaskRetry(row) {
      this.$modal.confirm('是否确认重试该数据分析任务？').then(() => {
        return retryFailedTask(row.taskId, 1); // 假设当前用户ID为1
      }).then((response) => {
        if (response.code === 1) {
          this.getTaskList();
          this.$modal.msgSuccess("任务重试成功");
        } else {
          this.$modal.msgError(response.message || '任务重试失败');
        }
      }).catch(error => {
        console.error('重试数据分析任务失败:', error);
        this.$modal.msgError('任务重试失败，请稍后重试');
      });
    },

    /** 导出按钮操作 */
    async handleTaskExport() {
      try {
        this.$modal.loading('正在导出数据,请稍后...');
        const response = await exportDataAnalysisTasks(this.taskQuery);
        this.$modal.closeLoading();

        // 处理blob响应
        if (response instanceof Blob) {
          const url = window.URL.createObjectURL(response);
          const link = document.createElement('a');
          link.href = url;
          link.download = `数据分析任务_${new Date().getTime()}.xlsx`;
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          window.URL.revokeObjectURL(url);
          this.$modal.msgSuccess('导出成功');
        } else {
          this.$modal.msgError('导出失败：响应格式错误');
        }
      } catch (error) {
        this.$modal.closeLoading();
        console.error('导出数据分析任务失败:', error);
        this.$modal.msgError('导出失败,请稍后重试');
      }
    },

    /** 提交按钮 */
    submitTaskForm() {
      this.$refs["taskForm"].validate(valid => {
        if (valid) {
          if (this.taskForm.taskId != null) {
            this.updateTask();
          } else {
            this.addTask();
          }
        }
      });
    },

    /** 新增数据分析任务 */
    addTask() {
      // 生成任务编号
      if (!this.taskForm.taskNo) {
        this.taskForm.taskNo = 'TASK' + Date.now();
      }
      // 设置组织ID（这里假设为1，实际应该从用户信息中获取）
      this.taskForm.orgId = 1;

      createDataAnalysisTask(this.taskForm).then(response => {
        if (response.code === 1) {
          this.$modal.msgSuccess("新增成功");
          this.taskOpen = false;
          this.getTaskList();
        } else {
          this.$modal.msgError(response.message || '新增失败');
        }
      }).catch(error => {
        console.error('新增数据分析任务失败:', error);
        this.$modal.msgError('新增失败，请稍后重试');
      });
    },

    /** 修改数据分析任务 */
    updateTask() {
      updateDataAnalysisTask(this.taskForm).then(response => {
        if (response.code === 1) {
          this.$modal.msgSuccess("修改成功");
          this.taskOpen = false;
          this.getTaskList();
        } else {
          this.$modal.msgError(response.message || '修改失败');
        }
      }).catch(error => {
        console.error('修改数据分析任务失败:', error);
        this.$modal.msgError('修改失败，请稍后重试');
      });
    },

    /** 取消按钮 */
    cancelTask() {
      this.taskOpen = false;
      this.resetTaskForm();
    },

    /** 表单重置 */
    resetTaskForm() {
      this.taskForm = {
        taskId: null,
        taskNo: null,
        taskName: null,
        taskType: null,
        analysisType: null,
        dataSource: null,
        dataRange: null,
        analysisParameters: null,
        modelId: null,
        scheduleType: 'MANUAL',
        scheduleCron: null,
        resultFormat: 'JSON',
        remark: null,
        orgId: null
      };
      this.resetForm("taskForm");
    },

    /** 数据分析任务分页方法 */
    handleTaskCurrentChange(val) {
      this.taskQuery.pageNum = val;
      this.getTaskList();
    },

    handleTaskSizeChange(val) {
      this.taskQuery.pageSize = val;
      this.getTaskList();
    },

    // ==================== KPI指标管理方法 ====================

    /** 查询KPI指标列表 */
    async getKpiList() {
      this.kpiLoading = true;
      try {
        // 只传必要的分页参数 - 后端使用pageNo参数
        const params = {
          pageNo: this.kpiQuery.pageNum,
          pageSize: this.kpiQuery.pageSize,
          orgId: this.kpiQuery.orgId || 1
        };

        // 只添加有值的查询条件
        if (this.kpiQuery.kpiCode) params.kpiCode = this.kpiQuery.kpiCode;
        if (this.kpiQuery.kpiName) params.kpiName = this.kpiQuery.kpiName;
        if (this.kpiQuery.kpiCategory) params.kpiCategory = this.kpiQuery.kpiCategory;
        if (this.kpiQuery.kpiType) params.kpiType = this.kpiQuery.kpiType;
        if (this.kpiQuery.kpiStatus) params.kpiStatus = this.kpiQuery.kpiStatus;
        if (this.kpiQuery.calculationFrequency) params.calculationFrequency = this.kpiQuery.calculationFrequency;

        console.log('[KPI指标] 查询参数:', params);
        const response = await getKpiIndicatorPage(params);

        // 使用统一方法解析数据
        const { list, total, success, message } = this.parsePageResponse(response);
        this.kpiList = list;
        this.kpiTotal = total;

        if (success) {
          console.log('[KPI指标] 查询成功 - 共', total, '条数据');
        } else {
          this.$message.error(message);
        }
      } catch (error) {
        console.error('[KPI指标] 查询失败:', error);
        this.$message.error('获取数据失败：' + error.message);
        this.kpiList = [];
        this.kpiTotal = 0;
      } finally {
        this.kpiLoading = false;
      }
    },

    /** 搜索按钮操作 */
    handleKpiQuery() {
      this.kpiQuery.pageNum = 1;
      this.getKpiList();
    },

    /** 重置按钮操作 */
    resetKpiQuery() {
      this.resetForm("kpiQueryForm");
      this.handleKpiQuery();
    },

    /** 多选框选中数据 */
    handleKpiSelectionChange(selection) {
      this.kpiIds = selection.map(item => item.kpiId);
      this.kpiSingle = selection.length !== 1;
      this.kpiMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleKpiAdd() {
      this.resetKpiForm();
      this.kpiOpen = true;
      this.kpiTitle = "添加KPI指标";
    },

    /** 修改按钮操作 */
    async handleKpiUpdate(row) {
      this.resetKpiForm();
      const kpiId = row.kpiId || this.kpiIds[0];
      try {
        const response = await getKpiIndicator(kpiId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.kpiForm = response.data || {};
          this.kpiOpen = true;
          this.kpiTitle = "修改KPI指标";
        } else {
          this.$modal.msgError('获取KPI指标信息失败');
        }
      } catch (error) {
        console.error('获取KPI指标信息失败:', error);
        this.$modal.msgError('获取KPI指标信息失败，请稍后重试');
      }
    },

    /** 提交按钮 */
    submitKpiForm() {
      this.$refs["kpiForm"].validate(valid => {
        if (valid) {
          if (this.kpiForm.kpiId != null) {
            this.updateKpiData();
          } else {
            this.addKpiData();
          }
        }
      });
    },

    /** 新增KPI指标 */
    async addKpiData() {
      try {
        // 设置组织ID
        this.kpiForm.orgId = this.kpiQuery.orgId || 1;

        const response = await createKpiIndicator(this.kpiForm);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("新增成功");
          this.kpiOpen = false;
          this.getKpiList();
        } else {
          this.$modal.msgError(response.message || '新增失败');
        }
      } catch (error) {
        console.error('新增KPI指标失败:', error);
        this.$modal.msgError('新增失败，请稍后重试');
      }
    },

    /** 修改KPI指标 */
    async updateKpiData() {
      try {
        const response = await updateKpiIndicator(this.kpiForm);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("修改成功");
          this.kpiOpen = false;
          this.getKpiList();
        } else {
          this.$modal.msgError(response.message || '修改失败');
        }
      } catch (error) {
        console.error('修改KPI指标失败:', error);
        this.$modal.msgError('修改失败，请稍后重试');
      }
    },

    /** 删除按钮操作 */
    async handleKpiDelete(row) {
      const kpiIds = row.kpiId ? [row.kpiId] : this.kpiIds;
      const kpiNames = row.kpiName ? [row.kpiName] : this.kpiList.filter(item => kpiIds.includes(item.kpiId)).map(item => item.kpiName);

      this.$modal.confirm('是否确认删除KPI指标"' + kpiNames.join('", "') + '"？').then(async () => {
        try {
          let response;
          if (kpiIds.length === 1) {
            response = await deleteKpiIndicator(kpiIds[0]);
          } else {
            response = await batchDeleteKpiIndicators(kpiIds);
          }

          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.getKpiList();
            this.$modal.msgSuccess("删除成功");
          } else {
            this.$modal.msgError(response.message || '删除失败');
          }
        } catch (error) {
          console.error('删除KPI指标失败:', error);
          this.$modal.msgError('删除失败，请稍后重试');
        }
      }).catch(() => {});
    },

    /** 导出按钮操作 */
    async handleKpiExport() {
      try {
        const response = await exportKpiData(this.kpiQuery);

        // 处理blob响应
        if (response instanceof Blob) {
          const url = window.URL.createObjectURL(response);
          const link = document.createElement('a');
          link.href = url;
          link.download = `KPI指标_${new Date().getTime()}.xlsx`;
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          window.URL.revokeObjectURL(url);
          this.$modal.msgSuccess("导出成功");
        } else {
          this.$modal.msgError('导出失败：响应格式错误');
        }
      } catch (error) {
        console.error('导出KPI指标失败:', error);
        this.$modal.msgError('导出失败，请稍后重试');
      }
    },

    /** 计算KPI指标 */
    async handleKpiCalculate(row) {
      try {
        const response = await calculateKpiIndicator(row.kpiId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("计算成功");
          this.getKpiList();
        } else {
          this.$modal.msgError(response.message || '计算失败');
        }
      } catch (error) {
        console.error('计算KPI指标失败:', error);
        this.$modal.msgError('计算失败，请稍后重试');
      }
    },

    /** 更新KPI状态 */
    async handleKpiStatusUpdate(row, status) {
      try {
        const response = await updateKpiStatus(row.kpiId, status);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("状态更新成功");
          this.getKpiList();
        } else {
          this.$modal.msgError(response.message || '状态更新失败');
        }
      } catch (error) {
        console.error('更新KPI状态失败:', error);
        this.$modal.msgError('状态更新失败，请稍后重试');
      }
    },

    /** 取消按钮 */
    cancelKpi() {
      this.kpiOpen = false;
      this.resetKpiForm();
    },

    /** 表单重置 */
    resetKpiForm() {
      this.kpiForm = {
        kpiId: null,
        kpiCode: null,
        kpiName: null,
        kpiCategory: null,
        kpiType: null,
        calculationFormula: null,
        dataSource: null,
        unit: null,
        targetValue: null,
        warningThreshold: null,
        criticalThreshold: null,
        currentValue: null,
        calculationFrequency: 'DAILY',
        kpiStatus: 'NORMAL',
        remark: null,
        orgId: 1
      };
      this.resetForm("kpiForm");
    },

    /** 获取进度条颜色 */
    getProgressColor(currentValue, targetValue) {
      if (!currentValue || !targetValue) return '#909399';

      const percentage = (currentValue / targetValue) * 100;
      if (percentage >= 100) return '#67c23a';
      if (percentage >= 80) return '#e6a23c';
      if (percentage >= 60) return '#f56c6c';
      return '#909399';
    },

    /** KPI指标分页方法 */
    handleKpiCurrentChange(val) {
      this.kpiQuery.pageNum = val;
      this.getKpiList();
    },

    handleKpiSizeChange(val) {
      this.kpiQuery.pageSize = val;
      this.getKpiList();
    },

    // ==================== 预测分析方法 ====================

    /** 查询预测分析列表 */
    async getPredictiveList() {
      this.predictiveLoading = true;
      try {
        const params = {
          pageNo: this.predictiveQuery.pageNum,
          pageSize: this.predictiveQuery.pageSize,
          orgId: this.predictiveQuery.orgId || 1
        };

        if (this.predictiveDateRange && this.predictiveDateRange.length === 2) {
          params.startDate = this.predictiveDateRange[0];
          params.endDate = this.predictiveDateRange[1];
        }

        if (this.predictiveQuery.analysisNo) params.analysisNo = this.predictiveQuery.analysisNo;
        if (this.predictiveQuery.analysisName) params.analysisName = this.predictiveQuery.analysisName;
        if (this.predictiveQuery.predictionType) params.predictionType = this.predictiveQuery.predictionType;
        if (this.predictiveQuery.analysisStatus) params.analysisStatus = this.predictiveQuery.analysisStatus;
        if (this.predictiveQuery.predictionPeriod) params.predictionPeriod = this.predictiveQuery.predictionPeriod;

        const response = await getPredictiveAnalysisPage(params);
        const { list, total, success, message } = this.parsePageResponse(response);

        if (success) {
          this.predictiveList = list;
          this.predictiveTotal = total;
        } else {
          this.$message.error(message || '获取数据失败');
        }
      } catch (error) {
        console.error('获取预测分析列表失败:', error);
        this.$message.error('获取数据失败：' + error.message);
        this.predictiveList = [];
        this.predictiveTotal = 0;
      } finally {
        this.predictiveLoading = false;
      }
    },

    /** 搜索按钮操作 */
    handlePredictiveQuery() {
      this.predictiveQuery.pageNum = 1;
      this.getPredictiveList();
    },

    /** 重置按钮操作 */
    resetPredictiveQuery() {
      this.predictiveDateRange = [];
      this.resetForm("predictiveQueryRef");
      this.handlePredictiveQuery();
    },

    /** 多选框选中数据 */
    handlePredictiveSelectionChange(selection) {
      this.predictiveIds = selection.map(item => item.analysisId);
      this.predictiveSingle = selection.length !== 1;
      this.predictiveMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handlePredictiveAdd() {
      this.resetPredictiveForm();
      this.predictiveOpen = true;
      this.predictiveTitle = "添加预测分析";
    },

    /** 修改按钮操作 */
    handlePredictiveUpdate(row) {
      this.resetPredictiveForm();
      const analysisId = row.analysisId || this.predictiveIds[0];
      getPredictiveAnalysisById(analysisId).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.predictiveForm = response.data || {};
          this.predictiveOpen = true;
          this.predictiveTitle = "修改预测分析";
        } else {
          this.$modal.msgError(response.message || '获取预测分析信息失败');
        }
      }).catch(error => {
        console.error('获取预测分析信息失败:', error);
        this.$modal.msgError('获取预测分析信息失败，请稍后重试');
      });
    },

    /** 提交按钮 */
    submitPredictiveForm() {
      this.$refs["predictiveForm"].validate(valid => {
        if (valid) {
          if (this.predictiveForm.analysisId != null) {
            this.updatePredictiveData();
          } else {
            this.addPredictiveData();
          }
        }
      });
    },

    /** 新增预测分析 */
    async addPredictiveData() {
      try {
        const response = await createPredictiveAnalysis(this.predictiveForm);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("新增成功");
          this.predictiveOpen = false;
          this.getPredictiveList();
        } else {
          this.$modal.msgError(response.message || '新增失败');
        }
      } catch (error) {
        console.error('新增预测分析失败:', error);
        this.$modal.msgError('新增失败，请稍后重试');
      }
    },

    /** 修改预测分析 */
    async updatePredictiveData() {
      try {
        const response = await updatePredictiveAnalysis(this.predictiveForm.analysisId, this.predictiveForm);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("修改成功");
          this.predictiveOpen = false;
          this.getPredictiveList();
        } else {
          this.$modal.msgError(response.message || '修改失败');
        }
      } catch (error) {
        console.error('修改预测分析失败:', error);
        this.$modal.msgError('修改失败，请稍后重试');
      }
    },

    /** 删除按钮操作 */
    handlePredictiveDelete(row) {
      const analysisIds = row.analysisId ? [row.analysisId] : this.predictiveIds;
      this.$modal.confirm('是否确认删除预测分析编号为"' + analysisIds + '"的数据项？').then(() => {
        return deletePredictiveAnalyses(analysisIds);
      }).then((response) => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.getPredictiveList();
          this.$modal.msgSuccess("删除成功");
        } else {
          this.$modal.msgError(response.message || '删除失败');
        }
      }).catch(error => {
        console.error('删除预测分析失败:', error);
        this.$modal.msgError('删除失败，请稍后重试');
      });
    },

    /** 预测分析操作命令处理 */
    async handlePredictiveCommand(command, row) {
      switch (command) {
        case 'execute':
          await this.handlePredictiveExecute(row);
          break;
        case 'cancel':
          await this.handlePredictiveCancel(row);
          break;
        case 'retry':
          await this.handlePredictiveRetry(row);
          break;
        case 'updateResult':
          await this.handlePredictiveUpdateResult(row);
          break;
        case 'calculateAccuracy':
          await this.handlePredictiveCalculateAccuracy(row);
          break;
        case 'delete':
          await this.handlePredictiveDelete(row);
          break;
      }
    },

    /** 执行预测分析 */
    async handlePredictiveExecute(row) {
      try {
        const response = await executePredictiveAnalysis(row.analysisId, 1);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("执行成功");
          this.getPredictiveList();
        } else {
          this.$modal.msgError(response.message || '执行失败');
        }
      } catch (error) {
        console.error('执行预测分析失败:', error);
        this.$modal.msgError('执行失败，请稍后重试');
      }
    },

    /** 取消预测分析 */
    async handlePredictiveCancel(row) {
      try {
        const response = await cancelPredictiveAnalysis(row.analysisId, 1);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("取消成功");
          this.getPredictiveList();
        } else {
          this.$modal.msgError(response.message || '取消失败');
        }
      } catch (error) {
        console.error('取消预测分析失败:', error);
        this.$modal.msgError('取消失败，请稍后重试');
      }
    },

    /** 重试预测分析 */
    async handlePredictiveRetry(row) {
      try {
        const response = await retryFailedPredictiveAnalysis(row.analysisId, 1);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("重试成功");
          this.getPredictiveList();
        } else {
          this.$modal.msgError(response.message || '重试失败');
        }
      } catch (error) {
        console.error('重试预测分析失败:', error);
        this.$modal.msgError('重试失败，请稍后重试');
      }
    },

    /** 更新实际结果 */
    async handlePredictiveUpdateResult(row) {
      this.$prompt('请输入实际结果', '更新实际结果', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea'
      }).then(async ({ value }) => {
        try {
          const response = await updateActualResult(row.analysisId, value, 1);
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$modal.msgSuccess("更新成功");
            this.getPredictiveList();
          } else {
            this.$modal.msgError(response.message || '更新失败');
          }
        } catch (error) {
          console.error('更新实际结果失败:', error);
          this.$modal.msgError('更新失败，请稍后重试');
        }
      }).catch(() => {});
    },

    /** 计算预测准确率 */
    async handlePredictiveCalculateAccuracy(row) {
      try {
        const response = await calculatePredictionAccuracy(row.analysisId, 1);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("计算成功");
          this.getPredictiveList();
        } else {
          this.$modal.msgError(response.message || '计算失败');
        }
      } catch (error) {
        console.error('计算预测准确率失败:', error);
        this.$modal.msgError('计算失败，请稍后重试');
      }
    },

    /** 导出按钮操作 */
    async handlePredictiveExport() {
      try {
        const response = await exportPredictiveAnalysisData(this.predictiveQuery);

        // 处理blob响应
        if (response instanceof Blob) {
          const url = window.URL.createObjectURL(response);
          const link = document.createElement('a');
          link.href = url;
          link.download = `预测分析_${new Date().getTime()}.xlsx`;
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          window.URL.revokeObjectURL(url);
          this.$modal.msgSuccess("导出成功");
        } else {
          this.$modal.msgError('导出失败：响应格式错误');
        }
      } catch (error) {
        console.error('导出预测分析失败:', error);
        this.$modal.msgError('导出失败，请稍后重试');
      }
    },

    /** 取消按钮 */
    cancelPredictive() {
      this.predictiveOpen = false;
      this.resetPredictiveForm();
    },

    /** 表单重置 */
    resetPredictiveForm() {
      this.predictiveForm = {
        analysisId: null,
        analysisNo: null,
        analysisName: null,
        predictionType: null,
        modelId: null,
        inputData: null,
        predictionPeriod: null,
        predictionHorizon: null,
        predictionResult: null,
        confidenceLevel: 95,
        confidenceInterval: null,
        accuracy: null,
        actualResult: null,
        deviationAnalysis: null,
        analysisStatus: 'PENDING',
        remark: null,
        orgId: 1
      };
      this.resetForm("predictiveForm");
    },

    /** 获取预测分析进度条颜色 */
    getPredictiveProgressColor(accuracy) {
      if (!accuracy) return '#909399';

      const acc = parseFloat(accuracy);
      if (acc >= 90) return '#67c23a';
      if (acc >= 80) return '#e6a23c';
      if (acc >= 70) return '#f56c6c';
      return '#909399';
    },

    /** 预测分析分页方法 */
    handlePredictiveCurrentChange(val) {
      this.predictiveQuery.pageNum = val;
      this.getPredictiveList();
    },

    handlePredictiveSizeChange(val) {
      this.predictiveQuery.pageSize = val;
      this.getPredictiveList();
    },

    // ==================== 决策建议管理方法 ====================

    /** 查询决策建议列表 */
    async getRecommendationList() {
      this.recommendationLoading = true;
      try {
        // 只传必要的分页参数 - 后端使用pageNo参数
        const params = {
          pageNo: this.recommendationQuery.pageNum,
          pageSize: this.recommendationQuery.pageSize,
          orgId: this.recommendationQuery.orgId || 1
        };

        // 处理日期范围
        if (this.recommendationDateRange && this.recommendationDateRange.length === 2) {
          params.startDate = this.recommendationDateRange[0];
          params.endDate = this.recommendationDateRange[1];
        }

        // 只添加有值的查询条件
        if (this.recommendationQuery.recommendationNo) params.recommendationNo = this.recommendationQuery.recommendationNo;
        if (this.recommendationQuery.recommendationName) params.recommendationName = this.recommendationQuery.recommendationName;
        if (this.recommendationQuery.recommendationType) params.recommendationType = this.recommendationQuery.recommendationType;
        if (this.recommendationQuery.recommendationStatus) params.recommendationStatus = this.recommendationQuery.recommendationStatus;
        if (this.recommendationQuery.priority) params.priority = this.recommendationQuery.priority;

        console.log('[决策建议] 查询参数:', params);
        const response = await getDecisionRecommendationPage(params);

        // 使用统一方法解析数据
        const { list, total, success, message } = this.parsePageResponse(response);
        this.recommendationList = list;
        this.recommendationTotal = total;

        if (success) {
          console.log('[决策建议] 查询成功 - 共', total, '条数据');
        } else {
          this.$message.error(message);
        }
      } catch (error) {
        console.error('[决策建议] 查询失败:', error);
        this.$message.error('获取数据失败：' + error.message);
        this.recommendationList = [];
        this.recommendationTotal = 0;
      } finally {
        this.recommendationLoading = false;
      }
    },

    /** 搜索按钮操作 */
    handleRecommendationQuery() {
      this.recommendationQuery.pageNum = 1;
      this.getRecommendationList();
    },

    /** 重置按钮操作 */
    resetRecommendationQuery() {
      this.recommendationDateRange = [];
      this.resetForm("recommendationQueryForm");
      this.handleRecommendationQuery();
    },

    /** 多选框选中数据 */
    handleRecommendationSelectionChange(selection) {
      console.log('[DEBUG-选中数据] selection:', selection);
      console.log('[DEBUG-选中数据] 第一条数据:', selection[0]);
      console.log('[DEBUG-选中数据] 第一条数据的所有字段:', Object.keys(selection[0] || {}));
      // 使用统一的ID获取方法
      this.recommendationIds = selection.map(item => this.getRecommendationId(item));
      this.recommendationSingle = selection.length !== 1;
      this.recommendationMultiple = !selection.length;
      console.log('[DEBUG-选中数据] 提取的recommendationIds:', this.recommendationIds);
    },

    /** 获取决策建议的ID（兼容多种字段名）*/
    getRecommendationId(row) {
      return row ? (row.recommendationId || row.id || row.recommendationNo) : null;
    },

    /** 新增按钮操作 */
    handleRecommendationAdd() {
      this.resetRecommendationForm();
      // 自动生成建议编号
      this.recommendationForm.recommendationNo = 'DR' + Date.now();
      this.recommendationOpen = true;
      this.recommendationTitle = "添加决策建议";
    },

    /** 修改按钮操作 */
    async handleRecommendationUpdate(row) {
      this.resetRecommendationForm();

      // 使用统一的ID获取方法
      const recommendationId = this.getRecommendationId(row) || this.recommendationIds[0];

      console.log('[DEBUG-修改] row:', row);
      console.log('[DEBUG-修改] row的所有字段:', row ? Object.keys(row) : 'row is null');
      console.log('[DEBUG-修改] 最终使用的recommendationId:', recommendationId);

      if (!recommendationId) {
        this.$modal.msgError('无法获取建议ID，请重试');
        return;
      }

      try {
        const response = await getDecisionRecommendationById(recommendationId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          // 合并数据，确保必填字段有默认值
          this.recommendationForm = {
            ...this.recommendationForm,
            ...response.data,
            // 确保必填字段不为空
            recommendationType: response.data?.recommendationType || 'INVESTMENT',
            priority: response.data?.priority || 'MEDIUM',
            recommendationContent: response.data?.recommendationContent || '',
            recommendationStatus: response.data?.recommendationStatus || 'PENDING',
            confidenceLevel: response.data?.confidenceLevel || 80.00
          };
          this.recommendationOpen = true;
          this.recommendationTitle = "修改决策建议";
        } else {
          this.$modal.msgError(response.message || '获取决策建议信息失败');
        }
      } catch (error) {
        console.error('获取决策建议信息失败:', error);
        this.$modal.msgError('获取决策建议信息失败，请稍后重试');
      }
    },

    /** 提交按钮 */
    submitRecommendationForm() {
      this.$refs["recommendationForm"].validate(valid => {
        if (valid) {
          if (this.recommendationForm.recommendationId != null) {
            this.updateRecommendationData();
          } else {
            this.addRecommendationData();
          }
        }
      });
    },

    /** 新增决策建议 */
    async addRecommendationData() {
      try {
        // 确保设置组织ID
        this.recommendationForm.orgId = 1;

        const response = await createDecisionRecommendation(this.recommendationForm);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("新增成功");
          this.recommendationOpen = false;
          this.getRecommendationList();
        } else {
          this.$modal.msgError(response.message || '新增失败');
        }
      } catch (error) {
        console.error('新增决策建议失败:', error);
        this.$modal.msgError('新增失败，请稍后重试');
      }
    },

    /** 修改决策建议 */
    async updateRecommendationData() {
      try {
        const response = await updateDecisionRecommendation(this.recommendationForm.recommendationId, this.recommendationForm);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("修改成功");
          this.recommendationOpen = false;
          this.getRecommendationList();
        } else {
          this.$modal.msgError(response.message || '修改失败');
        }
      } catch (error) {
        console.error('修改决策建议失败:', error);
        this.$modal.msgError('修改失败，请稍后重试');
      }
    },

    /** 删除按钮操作 */
    async handleRecommendationDelete(row) {
      const recommendationIds = row ? [this.getRecommendationId(row)] : this.recommendationIds;
      this.$modal.confirm('是否确认删除决策建议编号为"' + (row ? row.recommendationNo : recommendationIds.join(',')) + '"的数据项？').then(async () => {
        try {
          let response;
          if (recommendationIds.length === 1) {
            response = await deleteDecisionRecommendation(recommendationIds[0]);
          } else {
            response = await deleteDecisionRecommendations(recommendationIds);
          }

          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.getRecommendationList();
            this.$modal.msgSuccess("删除成功");
          } else {
            this.$modal.msgError(response.message || '删除失败');
          }
        } catch (error) {
          console.error('删除决策建议失败:', error);
          this.$modal.msgError('删除失败，请稍后重试');
        }
      }).catch(() => {
        console.log('用户取消删除操作');
      });
    },

    /** 决策建议操作命令处理 */
    async handleRecommendationCommand(command, row) {
      switch (command) {
        case 'edit':
          await this.handleRecommendationUpdate(row);
          break;
        case 'review':
          await this.handleRecommendationReview(row);
          break;
        case 'approve':
          await this.handleRecommendationApprove(row);
          break;
        case 'reject':
          await this.handleRecommendationReject(row);
          break;
        case 'implement':
          await this.handleRecommendationImplement(row);
          break;
        case 'delete':
          await this.handleRecommendationDelete(row);
          break;
      }
    },

    /** 审核决策建议 */
    async handleRecommendationReview(row) {
      this.$prompt('请输入审核意见', '审核决策建议', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea'
      }).then(async ({ value }) => {
        try {
          const recommendationId = this.getRecommendationId(row);
          const response = await reviewDecisionRecommendation(recommendationId, value, 1);
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$modal.msgSuccess("审核成功");
            this.getRecommendationList();
          } else {
            this.$modal.msgError(response.message || '审核失败');
          }
        } catch (error) {
          console.error('审核决策建议失败:', error);
          this.$modal.msgError('审核失败，请稍后重试');
        }
      }).catch(() => {});
    },

    /** 批准决策建议 */
    async handleRecommendationApprove(row) {
      this.$prompt('请输入批准意见', '批准决策建议', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea'
      }).then(async ({ value }) => {
        try {
          const recommendationId = this.getRecommendationId(row);
          const response = await approveDecisionRecommendation(recommendationId, value, 1);
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$modal.msgSuccess("批准成功");
            this.getRecommendationList();
          } else {
            this.$modal.msgError(response.message || '批准失败');
          }
        } catch (error) {
          console.error('批准决策建议失败:', error);
          this.$modal.msgError('批准失败，请稍后重试');
        }
      }).catch(() => {});
    },

    /** 拒绝决策建议 */
    async handleRecommendationReject(row) {
      this.$prompt('请输入拒绝理由', '拒绝决策建议', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea'
      }).then(async ({ value }) => {
        try {
          const recommendationId = this.getRecommendationId(row);
          const response = await rejectDecisionRecommendation(recommendationId, value, 1);
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$modal.msgSuccess("拒绝成功");
            this.getRecommendationList();
          } else {
            this.$modal.msgError(response.message || '拒绝失败');
          }
        } catch (error) {
          console.error('拒绝决策建议失败:', error);
          this.$modal.msgError('拒绝失败，请稍后重试');
        }
      }).catch(() => {});
    },

    /** 实施决策建议 */
    async handleRecommendationImplement(row) {
      this.$prompt('请输入实施结果', '实施决策建议', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea'
      }).then(async ({ value }) => {
        try {
          const recommendationId = this.getRecommendationId(row);
          const response = await implementDecisionRecommendation(recommendationId, value, 1);
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$modal.msgSuccess("实施成功");
            this.getRecommendationList();
          } else {
            this.$modal.msgError(response.message || '实施失败');
          }
        } catch (error) {
          console.error('实施决策建议失败:', error);
          this.$modal.msgError('实施失败，请稍后重试');
        }
      }).catch(() => {});
    },

    /** 导出按钮操作 */
    async handleRecommendationExport() {
      try {
        const response = await exportDecisionRecommendationData(this.recommendationQuery);

        // 处理blob响应
        if (response instanceof Blob) {
          // 创建blob URL
          const url = window.URL.createObjectURL(response);
          const link = document.createElement('a');
          link.href = url;
          link.download = `决策建议_${new Date().getTime()}.xlsx`;
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          window.URL.revokeObjectURL(url);
          this.$modal.msgSuccess("导出成功");
        } else {
          this.$modal.msgError('导出失败：响应格式错误');
        }
      } catch (error) {
        console.error('导出决策建议失败:', error);
        this.$modal.msgError('导出失败，请稍后重试');
      }
    },

    /** 取消按钮 */
    cancelRecommendation() {
      this.recommendationOpen = false;
      this.resetRecommendationForm();
    },

    /** 表单重置 */
    resetRecommendationForm() {
      this.recommendationForm = {
        recommendationId: null,
        recommendationNo: null,
        recommendationName: null,
        recommendationType: null,
        modelId: null,
        analysisId: null,
        recommendationContent: null,
        expectedBenefit: null,
        riskAssessment: null,
        implementationPlan: null,
        priority: 'MEDIUM',
        confidenceLevel: 80.00,
        recommendationStatus: 'PENDING',
        reviewUser: null,
        reviewDate: null,
        reviewOpinion: null,
        implementationDate: null,
        implementationResult: null,
        remark: null,
        orgId: 1
      };
      this.resetForm("recommendationForm");
    },

    /** 获取建议类型标签类型 */
    getRecommendationTypeTagType(type) {
      const typeMap = {
        'INVESTMENT': 'success',
        'FINANCING': 'primary',
        'RISK_CONTROL': 'danger',
        'CASH_MANAGEMENT': 'warning',
        'OPTIMIZATION': 'info'
      };
      return typeMap[type] || 'info';
    },

    /** 获取建议类型标签文本 */
    getRecommendationTypeLabel(type) {
      const option = this.recommendationTypeOptions.find(item => item.value === type);
      return option ? option.label : type;
    },

    /** 获取建议状态标签类型 */
    getRecommendationStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'REVIEWED': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'IMPLEMENTED': 'primary'
      };
      return statusMap[status] || 'info';
    },

    /** 获取建议状态标签文本 */
    getRecommendationStatusLabel(status) {
      const option = this.recommendationStatusOptions.find(item => item.value === status);
      return option ? option.label : status;
    },

    /** 获取优先级标签类型 */
    getPriorityTagType(priority) {
      const priorityMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      };
      return priorityMap[priority] || 'info';
    },

    /** 获取优先级标签文本 */
    getPriorityLabel(priority) {
      const option = this.priorityOptions.find(item => item.value === priority);
      return option ? option.label : priority;
    },

    /** 获取决策建议进度条颜色 */
    getRecommendationProgressColor(confidence) {
      if (!confidence) return '#909399';

      const conf = parseFloat(confidence);
      if (conf >= 90) return '#67c23a';  // 绿色 - 很高
      if (conf >= 80) return '#e6a23c';  // 橙色 - 高
      if (conf >= 70) return '#f56c6c';  // 红色 - 中等
      return '#909399';                  // 灰色 - 低
    },

    /** 决策建议分页方法 */
    handleRecommendationCurrentChange(val) {
      this.recommendationQuery.pageNum = val;
      this.getRecommendationList();
    },

    handleRecommendationSizeChange(val) {
      this.recommendationQuery.pageSize = val;
      this.getRecommendationList();
    },

    // ==================== 通用方法 ====================

    /** 获取表格行的唯一key */
    getRowKey(row) {
      return row.analysisId || row.analysisNo || Math.random().toString(36).substr(2, 9);
    },

    /** 计算表格高度 */
    calculateTableHeight() {
      // 根据每页显示的数据量动态计算表格高度
      // 每行约48px，表头约50px，留一些余量
      const baseHeight = 50; // 表头高度
      const rowHeight = 48;  // 每行高度
      const padding = 20;    // 额外空间
      const calculatedHeight = baseHeight + (this.predictiveQuery.pageSize * rowHeight) + padding;
      this.tableHeight = calculatedHeight;
      console.log('[预测分析] 计算表格高度:', calculatedHeight, 'px');
    },

    /** 通用表单重置方法 */
    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields();
      }
    }
  }
};
</script>

<style scoped>
/* 只保留最基本的样式 */
.app-container {
  padding: 20px;
}

.mb8 {
  margin-bottom: 8px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>

<!-- 添加非 scoped 样式来覆盖可能的全局样式 -->
<style>
/* 强制覆盖可能影响预测分析表格的全局样式 */
.predictive-analysis-container .el-table__body-wrapper {
  min-height: auto !important;
  max-height: none !important;
  height: auto !important;
  overflow-y: visible !important;
  overflow: visible !important;
}

.predictive-analysis-container .el-table {
  height: auto !important;
  max-height: none !important;
  overflow: visible !important;
}

.predictive-analysis-container .el-table__body {
  width: 100% !important;
}

.predictive-analysis-container .el-table__body tr {
  display: table-row !important;
  visibility: visible !important;
  height: auto !important;
}

.predictive-analysis-container .el-table__body tr td {
  display: table-cell !important;
  visibility: visible !important;
}

/* 确保表格容器不限制高度 */
.predictive-analysis-container {
  height: auto !important;
  min-height: auto !important;
  max-height: none !important;
  overflow: visible !important;
}
</style>
