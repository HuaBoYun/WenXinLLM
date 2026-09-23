<template>
  <div class="forecast-analysis">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算预测分析</h2>
      <p>基于历史数据和趋势模型，提供精准的预算预测分析和未来趋势预判</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-data-line" @click="handleCreateForecast">创建预测</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefreshForecast">刷新预测</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExportForecast">导出预测</el-button>
            <el-button type="info" icon="el-icon-s-data" @click="handleForecastModel">预测模型</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleForecastSettings">预测设置</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 预测分析概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card accuracy-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ forecastStats.accuracy }}%</div>
            <div class="stat-label">预测准确率</div>
            <div class="stat-description">历史预测准确率</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>较上期提升2.3%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card confidence-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ forecastStats.confidence }}%</div>
            <div class="stat-label">置信度</div>
            <div class="stat-description">预测结果置信度</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>置信度稳定</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-medal"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card deviation-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ forecastStats.deviation }}%</div>
            <div class="stat-label">平均偏差</div>
            <div class="stat-description">预测值与实际值偏差</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-down trend-down"></i>
              <span>偏差减少1.8%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card models-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ forecastStats.activeModels }}</div>
            <div class="stat-label">活跃模型</div>
            <div class="stat-description">当前使用的预测模型</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>模型优化中</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-cpu"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 预测条件设置 -->
    <el-card class="search-card" shadow="never">
      <div class="search-header">
        <span class="search-title">预测条件设置</span>
        <el-button type="text" @click="handleResetConditions">重置条件</el-button>
      </div>
      <el-form :model="forecastForm" :inline="true" size="small">
        <el-form-item label="预测模型">
          <el-select
            v-model="forecastForm.forecastModel"
            placeholder="全部模型"
            clearable
            style="width: 180px"
          >
            <el-option value="LINEAR_REGRESSION" label="线性回归" />
            <el-option value="ARIMA" label="ARIMA模型" />
            <el-option value="EXPONENTIAL_SMOOTHING" label="指数平滑" />
            <el-option value="NEURAL_NETWORK" label="神经网络" />
            <el-option value="ENSEMBLE" label="集成模型" />
            <el-option value="CUSTOM" label="自定义模型" />
          </el-select>
        </el-form-item>
        <el-form-item label="历史数据期间">
          <el-date-picker
            v-model="forecastForm.historicalPeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="历史开始日期"
            end-placeholder="历史结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="预测期间">
          <el-date-picker
            v-model="forecastForm.forecastPeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="预测开始日期"
            end-placeholder="预测结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="组织单元">
          <el-cascader
            v-model="forecastForm.organizationPath"
            :options="organizationOptions"
            :props="{ checkStrictly: true, emitPath: false }"
            placeholder="请选择组织单元"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="预测粒度">
          <el-select
            v-model="forecastForm.forecastGranularity"
            placeholder="全部粒度"
            clearable
            style="width: 150px"
          >
            <el-option value="DAILY" label="日度" />
            <el-option value="WEEKLY" label="周度" />
            <el-option value="MONTHLY" label="月度" />
            <el-option value="QUARTERLY" label="季度" />
            <el-option value="YEARLY" label="年度" />
          </el-select>
        </el-form-item>
        <el-form-item label="置信区间">
          <el-select
            v-model="forecastForm.confidenceInterval"
            placeholder="全部区间"
            clearable
            style="width: 120px"
          >
            <el-option value="90" label="90%" />
            <el-option value="95" label="95%" />
            <el-option value="99" label="99%" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleStartForecast">开始预测</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 预测图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="16">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>预测趋势图</span>
            <div class="header-tools">
              <el-radio-group v-model="chartViewType" size="mini">
                <el-radio-button label="line">线性图</el-radio-button>
                <el-radio-button label="area">面积图</el-radio-button>
                <el-radio-button label="confidence">置信区间</el-radio-button>
              </el-radio-group>
              <el-button icon="el-icon-full-screen" size="mini" @click="handleFullScreen" />
            </div>
          </div>
          <div id="forecastTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>模型性能</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshModelPerformance" />
          </div>
          <div id="modelPerformanceChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 预测精度分析 -->
    <el-row :gutter="20" class="accuracy-row">
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>预测误差分析</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshErrorAnalysis" />
          </div>
          <div id="errorAnalysisChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>残差分析</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshResidualAnalysis" />
          </div>
          <div id="residualAnalysisChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="model-info-card" shadow="never">
          <div slot="header" class="card-header">
            <span>模型信息</span>
            <el-button icon="el-icon-edit" size="mini" @click="handleEditModel" />
          </div>
          <div class="model-info-content">
            <div class="model-item">
              <div class="model-label">当前模型</div>
              <div class="model-value">{{ currentModel.name }}</div>
            </div>
            <div class="model-item">
              <div class="model-label">模型类型</div>
              <div class="model-value">{{ currentModel.type }}</div>
            </div>
            <div class="model-item">
              <div class="model-label">训练数据</div>
              <div class="model-value">{{ currentModel.trainingData }}</div>
            </div>
            <div class="model-item">
              <div class="model-label">准确率</div>
              <div class="model-value">{{ currentModel.accuracy }}%</div>
            </div>
            <div class="model-item">
              <div class="model-label">最后更新</div>
              <div class="model-value">{{ currentModel.lastUpdate }}</div>
            </div>
            <div class="model-actions">
              <el-button type="primary" size="mini" @click="handleTrainModel">重新训练</el-button>
              <el-button type="success" size="mini" @click="handleValidateModel">验证模型</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 预测结果表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预测结果</span>
        <div class="table-tools">
          <el-tooltip content="显示置信区间" placement="top">
            <el-switch
              v-model="showConfidenceInterval"
              active-text="置信区间"
              @change="handleShowConfidenceIntervalChange"
            />
          </el-tooltip>
          <el-tooltip content="显示预测精度" placement="top">
            <el-switch
              v-model="showAccuracy"
              active-text="预测精度"
              @change="handleShowAccuracyChange"
            />
          </el-tooltip>
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getForecastResults" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="forecastResults"
        border
        stripe
        highlight-current-row
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column prop="forecastDate" label="预测日期" width="120" align="center" />
        <el-table-column prop="organizationName" label="组织单元" width="150" show-overflow-tooltip />
        <el-table-column prop="accountName" label="预算科目" width="120" show-overflow-tooltip />

        <el-table-column prop="historicalValue" label="历史值" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.historicalValue) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="forecastAmount" label="预测值" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="forecast-value">{{ formatAmount(scope.row.forecastAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="lowerBound" label="下限" width="100" align="right" v-if="showConfidenceInterval">
          <template slot-scope="scope">
            <span class="bound-text lower-bound">{{ formatAmount(scope.row.lowerBound) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="upperBound" label="上限" width="100" align="right" v-if="showConfidenceInterval">
          <template slot-scope="scope">
            <span class="bound-text upper-bound">{{ formatAmount(scope.row.upperBound) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="confidenceLevel" label="置信度" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <el-progress
              :percentage="Number(scope.row.confidenceLevel) || 0"
              :stroke-width="6"
              :color="getConfidenceColor(scope.row.confidenceLevel)"
            />
          </template>
        </el-table-column>

        <el-table-column prop="accuracyRate" label="预测精度" width="100" align="center" sortable="custom" v-if="showAccuracy">
          <template slot-scope="scope">
            <span :class="getAccuracyClass(scope.row.accuracyRate)">{{ scope.row.accuracyRate }}%</span>
          </template>
        </el-table-column>

        <el-table-column prop="trend" label="趋势" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTrendColor(scope.row.trend)" size="mini">
              <i :class="getTrendIcon(scope.row.trend)"></i>
              {{ getTrendText(scope.row.trend) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="forecastModelName" label="预测模型" width="120" show-overflow-tooltip />
        
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleViewDetail(scope.row)"
            >详情</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="validate" icon="el-icon-check">验证</el-dropdown-item>
                <el-dropdown-item command="compare" icon="el-icon-data-analysis">对比</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
                <el-dropdown-item command="delete" icon="el-icon-delete" divided style="color:#F56C6C">删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          :total="total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 预测详情抽屉 -->

    <!-- 模型训练对话框 -->
    <el-dialog
      title="模型训练"
      :visible.sync="trainModelDialogVisible"
      width="600px"
      @close="handleCloseTrainDialog"
    >
      <el-form :model="trainModelForm" :rules="trainModelRules" ref="trainModelForm" label-width="120px">
        <el-form-item label="训练数据期间" prop="trainingPeriod">
          <el-date-picker
            v-model="trainModelForm.trainingPeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="验证比例" prop="validationRatio">
          <el-slider
            v-model="trainModelForm.validationRatio"
            :min="10"
            :max="50"
            :step="5"
            show-stops
            show-input
          />
          <span class="form-help">验证数据占总数据的百分比</span>
        </el-form-item>
        <el-form-item label="交叉验证折数" prop="crossValidationFolds">
          <el-input-number
            v-model="trainModelForm.crossValidationFolds"
            :min="3"
            :max="10"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="超参数优化" prop="hyperparameterOptimization">
          <el-switch
            v-model="trainModelForm.hyperparameterOptimization"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="trainModelDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmTrainModel" :loading="trainingModel">
          {{ trainingModel ? '训练中...' : '开始训练' }}
        </el-button>
      </div>
    </el-dialog>

    <!-- 创建预测对话框 -->
    <el-dialog
      title="新建预测分析"
      :visible.sync="createDialogVisible"
      width="560px"
      @close="handleCloseCreateDialog"
    >
      <el-form :model="createForm" :rules="createFormRules" ref="createForm" label-width="110px">
        <el-form-item label="分析名称" prop="analysisName">
          <el-input v-model="createForm.analysisName" placeholder="请输入分析名称" />
        </el-form-item>
        <el-form-item label="预算年度" prop="budgetYear">
          <el-date-picker
            v-model="createForm.budgetYear"
            type="year"
            value-format="yyyy"
            placeholder="请选择年度"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="预测方法" prop="forecastMethod">
          <el-select v-model="createForm.forecastMethod" placeholder="请选择预测方法" style="width: 100%">
            <el-option value="LINEAR_REGRESSION" label="线性回归" />
            <el-option value="ARIMA" label="ARIMA模型" />
            <el-option value="EXPONENTIAL_SMOOTHING" label="指数平滑" />
            <el-option value="NEURAL_NETWORK" label="神经网络" />
            <el-option value="ENSEMBLE" label="集成模型" />
            <el-option value="TREND_ANALYSIS" label="趋势分析" />
            <el-option value="MOVING_AVERAGE" label="移动平均" />
          </el-select>
        </el-form-item>
        <el-form-item label="预测粒度" prop="forecastGranularity">
          <el-select v-model="createForm.forecastGranularity" placeholder="请选择预测粒度" style="width: 100%">
            <el-option value="MONTHLY" label="月度" />
            <el-option value="QUARTERLY" label="季度" />
            <el-option value="YEARLY" label="年度" />
          </el-select>
        </el-form-item>
        <el-form-item label="置信区间" prop="confidenceInterval">
          <el-select v-model="createForm.confidenceInterval" placeholder="请选择置信区间" style="width: 100%">
            <el-option value="90" label="90%" />
            <el-option value="95" label="95%" />
            <el-option value="99" label="99%" />
          </el-select>
        </el-form-item>
        <el-form-item label="组织单元" prop="organizationId">
          <el-select v-model="createForm.organizationId" placeholder="请选择组织单元" style="width: 100%" clearable filterable @change="handleCreateOrgChange">
            <el-option v-for="item in orgFlatOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算科目" prop="accountId">
          <el-select v-model="createForm.accountId" placeholder="请选择预算科目" style="width: 100%" clearable filterable @change="handleCreateAccountChange">
            <el-option v-for="item in accountOptions" :key="item.value" :label="item.label" :value="item.value">
              <span>{{ item.label }}</span>
              <span style="float: right; color: #999; font-size: 12px">{{ item.code }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预测日期" prop="forecastDate">
          <el-date-picker
            v-model="createForm.forecastDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择预测日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="历史值">
          <el-input-number v-model="createForm.historicalValue" :precision="2" :controls="false" placeholder="请输入历史值（可选）" style="width: 100%" />
        </el-form-item>
        <el-form-item label="预测金额" prop="forecastAmount">
          <el-input-number v-model="createForm.forecastAmount" :precision="2" :controls="false" placeholder="请输入预测金额" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="createForm.remark" type="textarea" :rows="2" placeholder="请输入备注（可选）" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmCreate" :loading="creating">确定创建</el-button>
      </div>
    </el-dialog>

    <!-- 编辑预测对话框 -->
    <el-dialog
      title="编辑预测分析"
      :visible.sync="editDialogVisible"
      width="560px"
      @close="handleCloseEditDialog"
    >
      <el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="110px">
        <el-form-item label="分析名称" prop="analysisName">
          <el-input v-model="editForm.analysisName" placeholder="请输入分析名称" />
        </el-form-item>
        <el-form-item label="预算年度" prop="budgetYear">
          <el-date-picker
            v-model="editForm.budgetYear"
            type="year"
            value-format="yyyy"
            placeholder="请选择年度"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="预测方法" prop="forecastMethod">
          <el-select v-model="editForm.forecastMethod" placeholder="请选择预测方法" style="width: 100%">
            <el-option value="LINEAR_REGRESSION" label="线性回归" />
            <el-option value="ARIMA" label="ARIMA模型" />
            <el-option value="EXPONENTIAL_SMOOTHING" label="指数平滑" />
            <el-option value="NEURAL_NETWORK" label="神经网络" />
            <el-option value="ENSEMBLE" label="集成模型" />
            <el-option value="TREND_ANALYSIS" label="趋势分析" />
            <el-option value="MOVING_AVERAGE" label="移动平均" />
          </el-select>
        </el-form-item>
        <el-form-item label="预测粒度" prop="forecastGranularity">
          <el-select v-model="editForm.forecastGranularity" placeholder="请选择预测粒度" style="width: 100%">
            <el-option value="MONTHLY" label="月度" />
            <el-option value="QUARTERLY" label="季度" />
            <el-option value="YEARLY" label="年度" />
          </el-select>
        </el-form-item>
        <el-form-item label="置信区间" prop="confidenceInterval">
          <el-select v-model="editForm.confidenceInterval" placeholder="请选择置信区间" style="width: 100%">
            <el-option value="90" label="90%" />
            <el-option value="95" label="95%" />
            <el-option value="99" label="99%" />
          </el-select>
        </el-form-item>
        <el-form-item label="组织单元">
          <el-select v-model="editForm.organizationId" placeholder="请选择组织单元（可选）" style="width: 100%" clearable filterable @change="handleEditOrgChange">
            <el-option v-for="item in orgFlatOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="editForm.remark" type="textarea" :rows="2" placeholder="请输入备注（可选）" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmEdit" :loading="editing">保存修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetAnalysisApi } from '@/api/managementAccountant/ncv65/budgetAnalysis'
import * as echarts from 'echarts'

export default {
  name: 'ForecastAnalysis',
  data() {
    return {
      // 预测条件
      forecastForm: {
        forecastModel: '',
        historicalPeriod: [],
        forecastPeriod: [],
        organizationPath: '',
        forecastGranularity: '',
        confidenceInterval: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      forecastResults: [],
      total: 0,
      
      // 图表类型
      chartViewType: 'line',
      // 缓存后端返回的图表数据
      cachedChartData: null,
      
      // 控制开关
      showConfidenceInterval: true,
      showAccuracy: false,
      
      // 统计数据
      forecastStats: {
        accuracy: 0,
        confidence: 0,
        deviation: 0,
        activeModels: 0
      },

      // 当前模型信息
      currentModel: {
        name: '',
        type: '',
        trainingData: '',
        accuracy: '',
        lastUpdate: ''
      },
      
      // 抽屉（保留引用，不再使用）
      detailDrawerVisible: false,
      currentForecastDetail: null,
      activeTab: 'basic',

      // 创建预测对话框
      createDialogVisible: false,
      creating: false,
      createForm: {
        analysisName: '',
        budgetYear: String(new Date().getFullYear()),
        forecastMethod: '',
        forecastGranularity: '',
        confidenceInterval: '',
        organizationId: '',
        organizationName: '',
        accountId: '',
        accountName: '',
        forecastDate: '',
        historicalValue: null,
        forecastAmount: null,
        remark: ''
      },
      createFormRules: {
        analysisName: [{ required: true, message: '请输入分析名称', trigger: 'blur' }],
        budgetYear: [{ required: true, message: '请选择预算年度', trigger: 'change' }],
        forecastMethod: [{ required: true, message: '请选择预测方法', trigger: 'change' }],
        forecastGranularity: [{ required: true, message: '请选择预测粒度', trigger: 'change' }],
        confidenceInterval: [{ required: true, message: '请选择置信区间', trigger: 'change' }],
        organizationId: [{ required: true, message: '请选择组织单元', trigger: 'change' }],
        accountId: [{ required: true, message: '请选择预算科目', trigger: 'change' }],
        forecastDate: [{ required: true, message: '请选择预测日期', trigger: 'change' }],
        forecastAmount: [{ required: true, message: '请输入预测金额', trigger: 'blur' }]
      },

      // 编辑预测对话框
      editDialogVisible: false,
      editing: false,
      editForm: {
        id: '',
        analysisName: '',
        budgetYear: '',
        forecastMethod: '',
        forecastGranularity: '',
        confidenceInterval: '',
        organizationId: '',
        organizationName: '',
        remark: ''
      },
      editFormRules: {
        analysisName: [{ required: true, message: '请输入分析名称', trigger: 'blur' }],
        budgetYear: [{ required: true, message: '请选择预算年度', trigger: 'change' }],
        forecastMethod: [{ required: true, message: '请选择预测方法', trigger: 'change' }],
        forecastGranularity: [{ required: true, message: '请选择预测粒度', trigger: 'change' }],
        confidenceInterval: [{ required: true, message: '请选择置信区间', trigger: 'change' }]
      },
      
      // 模型训练对话框
      trainModelDialogVisible: false,
      trainingModel: false,
      trainModelForm: {
        trainingPeriod: [],
        validationRatio: 20,
        crossValidationFolds: 5,
        hyperparameterOptimization: true
      },
      trainModelRules: {
        trainingPeriod: [
          { required: true, message: '请选择训练数据期间', trigger: 'change' }
        ]
      },
      
      // 选项数据
      organizationOptions: [],
      accountOptions: []
    }
  },

  created() {
    this.getForecastResults()
    this.loadOrganizationOptions()
    this.loadAccountOptions()
    this.loadForecastStats()
    this.loadForecastModelInfo()
    this.initCharts()
  },

  watch: {
    chartViewType() {
      if (this.cachedChartData && this.cachedChartData.trendChart) {
        this.renderForecastTrendChart(this.cachedChartData.trendChart)
      }
    }
  },

  beforeDestroy() {
    const ids = ['forecastTrendChart', 'modelPerformanceChart', 'errorAnalysisChart', 'residualAnalysisChart']
    ids.forEach(id => {
      const dom = document.getElementById(id)
      if (dom) {
        const inst = echarts.getInstanceByDom(dom)
        if (inst) inst.dispose()
      }
    })
  },

  computed: {
    // 将树形组织选项拍平为下拉列表
    orgFlatOptions() {
      const result = []
      const flatten = (nodes) => {
        if (!nodes) return
        nodes.forEach(node => {
          result.push({ value: node.value, label: node.label })
          if (node.children && node.children.length) flatten(node.children)
        })
      }
      flatten(this.organizationOptions)
      return result
    }
  },

  methods: {
    // 获取预测结果
    async getForecastResults() {
      this.loading = true
      try {
        const params = {
          ...this.forecastForm,
          ...this.queryParams
        }
        const response = await budgetAnalysisApi.getForecastAnalysis(params)
        this.forecastResults = response.data.tlist || response.data.list || []
        this.total = response.data.totalRecord || response.data.total || 0
      } catch (error) {
        this.$message.error('获取预测分析结果失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载组织选项
    async loadOrganizationOptions() {
      try {
        const response = await budgetAnalysisApi.getOrganizations()
        this.organizationOptions = response.data
      } catch (error) {
        console.error('加载组织选项失败：', error)
      }
    },

    // 加载预算科目选项
    async loadAccountOptions() {
      try {
        const response = await budgetAnalysisApi.getForecastBudgetAccounts()
        if (response.code === 1 && response.data) {
          this.accountOptions = response.data
        }
      } catch (error) {
        console.error('加载预算科目选项失败：', error)
      }
    },
    
    // 初始化图表 —— 直接加载数据并渲染
    initCharts() {
      this.$nextTick(() => {
        this.loadForecastChartData()
      })
    },

    // 加载统计数据
    async loadForecastStats() {
      try {
        const response = await budgetAnalysisApi.getForecastStats()
        if (response.code === 1 && response.data) {
          const d = response.data
          this.forecastStats = {
            accuracy: d.averageAccuracy || 0,
            confidence: 95,
            deviation: d.avgForecastAmount ? (100 - (d.averageAccuracy || 0)).toFixed(1) : 0,
            activeModels: d.activeModels || 0
          }
        }
      } catch (error) {
        console.error('加载预测统计数据失败：', error)
      }
    },

    // 加载模型信息
    async loadForecastModelInfo() {
      try {
        const response = await budgetAnalysisApi.getForecastModelInfo()
        if (response.code === 1 && response.data) {
          const active = response.data.activeModel
          if (active) {
            this.currentModel = {
              id: active.id || '',
              name: active.modelName || '暂无模型',
              type: active.modelType || '-',
              trainingData: active.trainingDataDesc || '-',
              accuracy: active.accuracy || 0,
              lastUpdate: active.lastTrainTime || '-'
            }
          }
        }
      } catch (error) {
        console.error('加载模型信息失败：', error)
      }
    },

    // 加载图表数据（缓存 + 渲染）
    async loadForecastChartData() {
      try {
        const response = await budgetAnalysisApi.getForecastChartData(this.forecastForm)
        if (response.code === 1 && response.data) {
          this.cachedChartData = response.data
          const d = response.data
          if (d.trendChart) this.renderForecastTrendChart(d.trendChart)
          if (d.performanceChart) this.renderModelPerformanceChart(d.performanceChart)
          if (d.errorChart) this.renderErrorAnalysisChart(d.errorChart)
          if (d.residualChart) this.renderResidualAnalysisChart(d.residualChart)
        }
      } catch (error) {
        console.error('加载预测图表数据失败：', error)
      }
    },

    /**
     * 渲染预测趋势图 —— 根据 chartViewType 决定 line / area / confidence
     */
    renderForecastTrendChart(td) {
      const dom = document.getElementById('forecastTrendChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const xData = td.xAxis || []
      const seriesRaw = td.series || []
      const legendData = [...(td.legend || seriesRaw.map(s => s.name))]
      const isArea = this.chartViewType === 'area'
      const isConfidence = this.chartViewType === 'confidence'

      const series = seriesRaw.map((s, i) => ({
        name: s.name,
        type: 'line',
        data: s.data || [],
        smooth: true,
        lineStyle: i === 1 ? { type: 'dashed' } : {},
        itemStyle: { color: i === 0 ? '#409EFF' : '#67C23A' },
        areaStyle: isArea ? { opacity: 0.3 } : undefined
      }))

      if (isConfidence) {
        const forecastSeries = seriesRaw[1] || seriesRaw[0] || {}
        const forecastData = forecastSeries.data || []
        const upperData = forecastData.map(v => v != null ? Math.round(Number(v) * 1.1) : null)
        const lowerData = forecastData.map(v => v != null ? Math.round(Number(v) * 0.9) : null)
        series.push({
          name: '置信上限',
          type: 'line',
          data: upperData,
          lineStyle: { type: 'dotted', color: '#E6A23C' },
          itemStyle: { color: '#E6A23C' },
          areaStyle: { opacity: 0.15, color: '#E6A23C' },
          symbol: 'none'
        })
        series.push({
          name: '置信下限',
          type: 'line',
          data: lowerData,
          lineStyle: { type: 'dotted', color: '#E6A23C' },
          itemStyle: { color: '#E6A23C' },
          symbol: 'none'
        })
        legendData.push('置信上限', '置信下限')
      }

      const option = {
        title: { text: '预算预测趋势', left: 'center' },
        tooltip: { trigger: 'axis', axisPointer: { type: 'cross' } },
        legend: { data: legendData, top: 30 },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', boundaryGap: false, data: xData },
        yAxis: { type: 'value', name: '金额(万元)' },
        series: series
      }
      chart.setOption(option, true)
      chart.resize()
    },

    /**
     * 渲染模型性能图
     */
    renderModelPerformanceChart(pd) {
      const dom = document.getElementById('modelPerformanceChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const option = {
        title: { text: '模型性能对比', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: pd.legend || [] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: pd.xAxis || [] },
        yAxis: { type: 'value' },
        series: (pd.series || []).map(s => ({
          name: s.name,
          type: 'bar',
          data: s.data || []
        }))
      }
      chart.setOption(option, true)
      chart.resize()
    },

    /**
     * 渲染误差分析图
     */
    renderErrorAnalysisChart(ed) {
      const dom = document.getElementById('errorAnalysisChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const option = {
        title: { text: '预测误差分布', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: ed.legend || [] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: ed.xAxis || [], axisLabel: { rotate: 30, interval: 0 } },
        yAxis: { type: 'value' },
        series: (ed.series || []).map(s => ({
          name: s.name,
          type: 'bar',
          data: s.data || []
        }))
      }
      chart.setOption(option, true)
      chart.resize()
    },

    /**
     * 渲染残差分析图
     */
    renderResidualAnalysisChart(rd) {
      const dom = document.getElementById('residualAnalysisChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const option = {
        title: { text: '残差分析', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: rd.legend || [] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: rd.xAxis || [] },
        yAxis: { type: 'value', name: '残差' },
        series: (rd.series || []).map(s => ({
          name: s.name,
          type: 'line',
          data: s.data || [],
          markLine: { data: [{ type: 'average', name: '均值' }] }
        }))
      }
      chart.setOption(option, true)
      chart.resize()
    },

    // 开始预测
    handleStartForecast() {
      this.queryParams.pageNum = 1
      this.getForecastResults()
      this.initCharts()
    },
    
    // 重置
    handleReset() {
      this.forecastForm = {
        forecastModel: '',
        historicalPeriod: [],
        forecastPeriod: [],
        organizationPath: '',
        forecastGranularity: '',
        confidenceInterval: ''
      }
      this.handleStartForecast()
    },
    
    // 重置条件
    handleResetConditions() {
      this.handleReset()
    },
    
    // 显示置信区间切换
    handleShowConfidenceIntervalChange(value) {
      this.$message.info(value ? '已显示置信区间' : '已隐藏置信区间')
    },
    
    // 显示预测精度切换
    handleShowAccuracyChange(value) {
      this.$message.info(value ? '已显示预测精度' : '已隐藏预测精度')
    },
    
    // 创建预测 - 打开新增对话框
    handleCreateForecast() {
      this.createForm = {
        analysisName: '',
        budgetYear: String(new Date().getFullYear()),
        forecastMethod: '',
        forecastGranularity: '',
        confidenceInterval: '',
        organizationId: '',
        organizationName: '',
        accountId: '',
        accountName: '',
        forecastDate: '',
        historicalValue: null,
        forecastAmount: null,
        remark: ''
      }
      this.createDialogVisible = true
    },

    // 组织单元选择变化时同步 organizationName
    handleCreateOrgChange(val) {
      const found = this.orgFlatOptions.find(o => o.value === val)
      this.createForm.organizationName = found ? found.label : ''
    },

    // 预算科目选择变化时同步 accountName
    handleCreateAccountChange(val) {
      const found = this.accountOptions.find(a => a.value === val)
      this.createForm.accountName = found ? found.label : ''
    },

    // 确认创建
    async handleConfirmCreate() {
      this.$refs.createForm.validate(async (valid) => {
        if (!valid) return
        this.creating = true
        try {
          const response = await budgetAnalysisApi.createForecast(this.createForm)
          if (response.code === 1) {
            this.$message.success('创建预测成功')
            this.createDialogVisible = false
            this.getForecastResults()
            this.loadForecastStats()
          } else {
            this.$message.error(response.msg || '创建失败')
          }
        } catch (error) {
          this.$message.error('创建预测失败：' + error.message)
        } finally {
          this.creating = false
        }
      })
    },

    // 关闭创建对话框
    handleCloseCreateDialog() {
      this.$refs.createForm && this.$refs.createForm.resetFields()
    },
    
    // 刷新预测
    handleRefreshForecast() {
      this.getForecastResults()
      this.initCharts()
    },
    
    // 导出预测
    async handleExportForecast() {
      try {
        const params = { ...this.forecastForm }
        await budgetAnalysisApi.exportForecastAnalysis(params)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 预测模型
    handleForecastModel() {
      this.trainModelDialogVisible = true
    },
    
    // 预测设置
    handleForecastSettings() {
      this.settingsDialogVisible = true
    },
    
    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },
    
    // 全屏
    handleFullScreen() {
      // 实现图表全屏功能
      this.$message.info('图表全屏功能')
    },
    
    // 刷新模型性能
    refreshModelPerformance() {
      this.loadForecastChartData()
    },

    // 刷新误差分析
    refreshErrorAnalysis() {
      this.loadForecastChartData()
    },

    // 刷新残差分析
    refreshResidualAnalysis() {
      this.loadForecastChartData()
    },
    
    // 编辑模型
    handleEditModel() {
      this.trainModelDialogVisible = true
    },
    
    // 训练模型
    handleTrainModel() {
      this.trainModelDialogVisible = true
    },
    
    // 验证模型
    async handleValidateModel() {
      if (!this.currentModel.id) {
        this.$message.warning('暂无可验证的模型')
        return
      }
      try {
        const response = await budgetAnalysisApi.validateModel(this.currentModel.id)
        if (response.code === 1 && response.data) {
          const d = response.data
          if (d.isValid) {
            this.$message.success(`模型验证通过，准确率: ${d.accuracy}%`)
          } else {
            this.$message.warning(d.message || '模型验证未通过')
          }
        }
      } catch (error) {
        this.$message.error('模型验证失败：' + error.message)
      }
    },
    
    // 确认训练模型
    async handleConfirmTrainModel() {
      this.$refs.trainModelForm.validate(async (valid) => {
        if (valid) {
          this.trainingModel = true
          try {
            const response = await budgetAnalysisApi.trainModel(this.trainModelForm)
            if (response.code === 1) {
              this.$message.success('模型训练完成')
              this.trainModelDialogVisible = false
              this.loadForecastModelInfo()
              this.loadForecastChartData()
            } else {
              this.$message.error(response.msg || '训练失败')
            }
          } catch (error) {
            this.$message.error('模型训练失败：' + error.message)
          } finally {
            this.trainingModel = false
          }
        }
      })
    },
    
    // 关闭训练对话框
    handleCloseTrainDialog() {
      this.$refs.trainModelForm.resetFields()
    },
    
    // 查看详情 - 用 MessageBox 展示基本信息
    handleViewDetail(row) {
      this.$alert(
        `<div style="line-height:2">
          <p><b>分析名称：</b>${row.analysisName || '-'}</p>
          <p><b>预测日期：</b>${row.forecastDate || '-'}</p>
          <p><b>组织单元：</b>${row.organizationName || '-'}</p>
          <p><b>预算科目：</b>${row.accountName || '-'}</p>
          <p><b>预测模型：</b>${row.forecastModelName || '-'}</p>
          <p><b>预测金额：</b>${this.formatAmount(row.forecastAmount)}</p>
          <p><b>历史值：</b>${this.formatAmount(row.historicalValue)}</p>
          <p><b>置信度：</b>${row.confidenceLevel || '-'}%</p>
          <p><b>准确率：</b>${row.accuracyRate != null ? row.accuracyRate + '%' : '-'}</p>
          <p><b>分析状态：</b>${row.analysisStatus || '-'}</p>
        </div>`,
        '预测详情',
        { dangerouslyUseHTMLString: true, confirmButtonText: '关闭' }
      )
    },

    // 编辑预测
    handleEdit(row) {
      this.editForm = {
        id: row.id,
        analysisName: row.analysisName || '',
        budgetYear: row.budgetYear ? String(row.budgetYear) : '',
        forecastMethod: row.forecastMethod || '',
        forecastGranularity: row.forecastGranularity || '',
        confidenceInterval: row.confidenceInterval || '',
        organizationId: row.organizationId || '',
        organizationName: row.organizationName || '',
        remark: row.remark || ''
      }
      this.editDialogVisible = true
    },

    // 编辑弹窗组织单元变化时同步 organizationName
    handleEditOrgChange(val) {
      const found = this.orgFlatOptions.find(o => o.value === val)
      this.editForm.organizationName = found ? found.label : ''
    },

    // 确认编辑
    async handleConfirmEdit() {
      this.$refs.editForm.validate(async (valid) => {
        if (!valid) return
        this.editing = true
        try {
          const response = await budgetAnalysisApi.updateForecast(this.editForm)
          if (response.code === 1) {
            this.$message.success('修改成功')
            this.editDialogVisible = false
            this.getForecastResults()
          } else {
            this.$message.error(response.msg || '修改失败')
          }
        } catch (error) {
          this.$message.error('修改失败：' + error.message)
        } finally {
          this.editing = false
        }
      })
    },

    // 关闭编辑对话框
    handleCloseEditDialog() {
      this.$refs.editForm && this.$refs.editForm.resetFields()
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'validate':
          this.handleValidateForecast(row)
          break
        case 'compare':
          this.handleCompareForecast(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 删除预测
    handleDelete(row) {
      this.$confirm(`确定删除"${row.analysisName || row.id}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await budgetAnalysisApi.deleteForecast(row.id)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getForecastResults()
            this.loadForecastStats()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      }).catch(() => {})
    },
    
    // 验证预测
    async handleValidateForecast(row) {
      try {
        await budgetAnalysisApi.validateForecast(row.id)
        this.$message.success('预测验证完成')
      } catch (error) {
        this.$message.error('预测验证失败：' + error.message)
      }
    },
    
    // 对比预测
    handleCompareForecast(row) {
      this.$message.info('详情查看功能开发中')
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        await budgetAnalysisApi.exportSingleForecast(row.id)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 行点击 - 不做任何操作
    handleRowClick(row) {},
    
    // 排序改变
    handleSortChange({ column, prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getForecastResults()
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getForecastResults()
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getForecastResults()
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取置信度颜色
    getConfidenceColor(confidence) {
      if (confidence >= 90) return '#67C23A'
      if (confidence >= 80) return '#409EFF'
      if (confidence >= 70) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取精度样式类
    getAccuracyClass(accuracy) {
      if (accuracy >= 90) return 'excellent-accuracy'
      if (accuracy >= 80) return 'good-accuracy'
      if (accuracy >= 70) return 'average-accuracy'
      return 'poor-accuracy'
    },
    
    // 获取趋势颜色
    getTrendColor(trend) {
      const colorMap = {
        'UP': 'success',
        'DOWN': 'danger',
        'STABLE': 'info'
      }
      return colorMap[trend] || 'info'
    },
    
    // 获取趋势图标
    getTrendIcon(trend) {
      const iconMap = {
        'UP': 'el-icon-top',
        'DOWN': 'el-icon-bottom',
        'STABLE': 'el-icon-minus'
      }
      return iconMap[trend] || 'el-icon-minus'
    },
    
    // 获取趋势文本
    getTrendText(trend) {
      const textMap = {
        'UP': '上升',
        'DOWN': '下降',
        'STABLE': '稳定'
      }
      return textMap[trend] || trend
    }
  }
}
</script>

<style lang="scss" scoped>
.forecast-analysis {
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
  
  .toolbar-card,
  .search-card,
  .table-card {
    margin-bottom: 20px;
  }
  
  .stats-row {
    margin-bottom: 20px;
    
    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;
      
      &.accuracy-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.confidence-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.deviation-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.models-card {
        background: linear-gradient(135deg, #909399, #B3B6BC);
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
          
          .trend-up {
            color: #F56C6C;
          }
          
          .trend-down {
            color: #67C23A;
          }
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
  
  .search-card {
    .search-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      
      .search-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
      }
    }
  }
  
  .chart-row,
  .accuracy-row {
    margin-bottom: 20px;
    
    .chart-card,
    .model-info-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .header-tools {
          display: flex;
          align-items: center;
          gap: 10px;
        }
      }
      
      .chart-container {
        height: 300px;
      }
    }
    
    .model-info-card {
      .model-info-content {
        .model-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 8px 0;
          border-bottom: 1px solid #EBEEF5;
          
          &:last-child {
            border-bottom: none;
          }
          
          .model-label {
            font-size: 14px;
            color: #606266;
          }
          
          .model-value {
            font-size: 14px;
            color: #303133;
            font-weight: 500;
          }
        }
        
        .model-actions {
          margin-top: 16px;
          text-align: center;
          
          .el-button {
            margin: 0 4px;
          }
        }
      }
    }
  }
  
  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    
    .table-title {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }
    
    .table-tools {
      display: flex;
      align-items: center;
      gap: 12px;
    }
  }
  
  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #409EFF;
  }
  
  .forecast-value {
    font-family: 'Courier New', monospace;
    font-weight: 600;
    color: #67C23A;
  }
  
  .bound-text {
    font-family: 'Courier New', monospace;
    font-size: 12px;
    
    &.lower-bound {
      color: #F56C6C;
    }
    
    &.upper-bound {
      color: #67C23A;
    }
  }
  
  .excellent-accuracy { color: #67C23A; font-weight: 500; }
  .good-accuracy { color: #409EFF; font-weight: 500; }
  .average-accuracy { color: #E6A23C; font-weight: 500; }
  .poor-accuracy { color: #F56C6C; font-weight: 500; }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .text-right {
    text-align: right;
  }
  
  .detail-content {
    padding: 20px;
    
    .parameters-content,
    .validation-content {
      margin-top: 20px;
    }
    
    .validation-metrics {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 16px;
      
      .metric-item {
        padding: 16px;
        border: 1px solid #EBEEF5;
        border-radius: 6px;
        text-align: center;
        
        .metric-label {
          font-size: 14px;
          color: #606266;
          margin-bottom: 8px;
        }
        
        .metric-value {
          font-size: 18px;
          font-weight: 600;
          color: #303133;
        }
      }
    }
  }
  
  .form-help {
    font-size: 12px;
    color: #909399;
    margin-left: 8px;
  }
  
  .dialog-footer {
    text-align: right;
  }
}
</style>
