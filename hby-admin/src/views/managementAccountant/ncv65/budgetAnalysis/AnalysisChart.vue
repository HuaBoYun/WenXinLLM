<template>
  <div class="analysis-chart">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>分析图表</h2>
      <p>提供丰富的图表类型和自定义配置，支持多维度数据可视化分析</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateChart">创建图表</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefreshCharts">刷新图表</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleBatchExport">批量导出</el-button>
            <el-button type="info" icon="el-icon-document" @click="handleChartTemplates">图表模板</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleChartSettings">图表设置</el-button>
            <el-button icon="el-icon-share" @click="handleShareCharts">分享</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 图表分析概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card charts-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ chartStats.totalCharts }}</div>
            <div class="stat-label">图表总数</div>
            <div class="stat-description">已创建的分析图表</div>
            <div class="stat-trend">
              <i class="el-icon-pie-chart"></i>
              <span>多样化展示</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-pie-chart"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card types-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ chartStats.chartTypes }}</div>
            <div class="stat-label">图表类型</div>
            <div class="stat-description">支持的图表类型数</div>
            <div class="stat-trend">
              <i class="el-icon-s-data"></i>
              <span>类型丰富</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-data"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card interactive-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ chartStats.interactiveCharts }}</div>
            <div class="stat-label">交互图表</div>
            <div class="stat-description">支持交互的图表数</div>
            <div class="stat-trend">
              <i class="el-icon-mouse"></i>
              <span>交互丰富</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-mouse"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card exports-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ chartStats.totalExports }}</div>
            <div class="stat-label">导出次数</div>
            <div class="stat-description">图表导出总次数</div>
            <div class="stat-trend">
              <i class="el-icon-download"></i>
              <span>使用频繁</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-download"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表筛选条件 -->
    <el-card class="filter-card" shadow="never">
      <div class="filter-header">
        <span class="filter-title">图表筛选</span>
        <el-button type="text" @click="handleResetFilter">重置筛选</el-button>
      </div>
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="图表类型">
          <el-select
            v-model="queryForm.chartType"
            placeholder="请选择图表类型"
            clearable
            style="width: 150px"
          >
            <el-option value="LINE" label="折线图" />
            <el-option value="BAR" label="柱状图" />
            <el-option value="PIE" label="饼图" />
            <el-option value="SCATTER" label="散点图" />
            <el-option value="RADAR" label="雷达图" />
            <el-option value="HEATMAP" label="热力图" />
            <el-option value="TREEMAP" label="树图" />
            <el-option value="SANKEY" label="桑基图" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据源">
          <el-select
            v-model="queryForm.dataSource"
            placeholder="请选择数据源"
            clearable
            style="width: 150px"
          >
            <el-option value="BUDGET_DATA" label="预算数据" />
            <el-option value="EXECUTION_DATA" label="执行数据" />
            <el-option value="ANALYSIS_DATA" label="分析数据" />
            <el-option value="FORECAST_DATA" label="预测数据" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="queryForm.createTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="创建人">
          <el-input
            v-model="queryForm.creator"
            placeholder="请输入创建人"
            clearable
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item label="图表名称">
          <el-input
            v-model="queryForm.chartName"
            placeholder="请输入图表名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleResetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 图表类型选择 -->
    <el-card class="chart-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>图表类型</span>
        <div class="header-tools">
          <el-button icon="el-icon-refresh" size="mini" @click="refreshChartTypes">刷新</el-button>
        </div>
      </div>
      <el-row :gutter="16">
        <el-col :span="3" v-for="chartType in chartTypes" :key="chartType.id">
          <el-card 
            class="chart-type-item" 
            shadow="hover" 
            @click.native="handleSelectChartType(chartType)"
            :class="{ 'selected': selectedChartType === chartType.id }"
          >
            <div class="chart-type-icon">
              <i :class="chartType.icon"></i>
            </div>
            <div class="chart-type-title">{{ chartType.name }}</div>
            <div class="chart-type-description">{{ chartType.description }}</div>
            <div class="chart-type-stats">
              <span class="usage-count">{{ chartType.usageCount }} 次使用</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 图表展示区域 -->
    <el-row :gutter="20" class="chart-display-row">
      <el-col :span="16">
        <el-card class="main-chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>{{ currentChart.title || '图表预览' }}</span>
            <div class="header-tools">
              <el-button-group size="mini">
                <el-button icon="el-icon-zoom-in" @click="handleZoomIn">放大</el-button>
                <el-button icon="el-icon-zoom-out" @click="handleZoomOut">缩小</el-button>
                <el-button icon="el-icon-refresh" @click="handleRefreshChart">刷新</el-button>
                <el-button icon="el-icon-download" @click="handleExportChart">导出</el-button>
              </el-button-group>
            </div>
          </div>
          <div id="mainChart" class="main-chart-container" :style="{ transform: `scale(${chartScale})` }"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-config-card" shadow="never">
          <div slot="header" class="card-header">
            <span>图表配置</span>
            <el-button icon="el-icon-setting" size="mini" @click="handleAdvancedConfig">高级配置</el-button>
          </div>
          <el-form :model="chartConfig" label-width="80px" size="small">
            <el-form-item label="图表标题">
              <el-input v-model="chartConfig.title" placeholder="请输入图表标题" @input="handleConfigChange" />
            </el-form-item>
            <el-form-item label="图表类型">
              <el-select v-model="chartConfig.type" placeholder="请选择图表类型" style="width: 100%" @change="handleConfigChange">
                <el-option value="line" label="折线图" />
                <el-option value="bar" label="柱状图" />
                <el-option value="pie" label="饼图" />
                <el-option value="scatter" label="散点图" />
                <el-option value="radar" label="雷达图" />
                <el-option value="heatmap" label="热力图" />
              </el-select>
            </el-form-item>
            <el-form-item label="主题色彩">
              <el-select v-model="chartConfig.theme" placeholder="请选择主题" style="width: 100%" @change="handleConfigChange">
                <el-option value="default" label="默认主题" />
                <el-option value="dark" label="深色主题" />
                <el-option value="vintage" label="复古主题" />
                <el-option value="westeros" label="权游主题" />
                <el-option value="essos" label="奴隶湾主题" />
              </el-select>
            </el-form-item>
            <el-form-item label="动画效果">
              <el-switch v-model="chartConfig.animation" @change="handleConfigChange" />
            </el-form-item>
            <el-form-item label="显示图例">
              <el-switch v-model="chartConfig.showLegend" @change="handleConfigChange" />
            </el-form-item>
            <el-form-item label="显示工具栏">
              <el-switch v-model="chartConfig.showToolbox" @change="handleConfigChange" />
            </el-form-item>
            <el-form-item label="数据标签">
              <el-switch v-model="chartConfig.showDataLabels" @change="handleConfigChange" />
            </el-form-item>
            <el-form-item label="网格线">
              <el-switch v-model="chartConfig.showGrid" @change="handleConfigChange" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleApplyConfig">应用配置</el-button>
              <el-button @click="handleResetConfig">重置配置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表数据配置 -->
    <el-card class="data-config-card" shadow="never">
      <div slot="header" class="card-header">
        <span>数据配置</span>
        <div class="header-tools">
          <el-button icon="el-icon-plus" size="mini" @click="handleAddDataSeries">添加数据系列</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="handleRefreshData">刷新数据</el-button>
        </div>
      </div>
      <el-table
        :data="chartDataSeries"
        border
        size="mini"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="seriesName" label="系列名称" width="150">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.seriesName"
              size="mini"
              @input="handleDataSeriesChange"
            />
          </template>
        </el-table-column>
        <el-table-column prop="dataSource" label="数据源" width="150">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.dataSource"
              size="mini"
              style="width: 100%"
              @change="handleDataSeriesChange"
            >
              <el-option value="BUDGET_DATA" label="预算数据" />
              <el-option value="EXECUTION_DATA" label="执行数据" />
              <el-option value="ANALYSIS_DATA" label="分析数据" />
              <el-option value="FORECAST_DATA" label="预测数据" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="xField" label="X轴字段" width="120">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.xField"
              size="mini"
              style="width: 100%"
              @change="handleDataSeriesChange"
            >
              <el-option value="month" label="月份" />
              <el-option value="quarter" label="季度" />
              <el-option value="year" label="年份" />
              <el-option value="department" label="部门" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="yField" label="Y轴字段" width="120">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.yField"
              size="mini"
              style="width: 100%"
              @change="handleDataSeriesChange"
            >
              <el-option value="amount" label="金额" />
              <el-option value="rate" label="比率" />
              <el-option value="count" label="数量" />
              <el-option value="percentage" label="百分比" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="color" label="颜色" width="100" align="center">
          <template slot-scope="scope">
            <el-color-picker
              v-model="scope.row.color"
              size="mini"
              @change="handleDataSeriesChange"
            />
          </template>
        </el-table-column>
        <el-table-column prop="visible" label="显示" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.visible"
              size="mini"
              @change="handleDataSeriesChange"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEditDataSeries(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-delete"
              @click="handleDeleteDataSeries(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 图表库 -->
    <el-card class="chart-library-card" shadow="never">
      <div slot="header" class="card-header">
        <span>图表库</span>
        <div class="header-tools">
          <el-tooltip content="网格视图" placement="top">
            <el-button 
              icon="el-icon-s-grid" 
              size="mini" 
              :type="libraryViewMode === 'grid' ? 'primary' : ''"
              @click="libraryViewMode = 'grid'"
            />
          </el-tooltip>
          <el-tooltip content="列表视图" placement="top">
            <el-button 
              icon="el-icon-menu" 
              size="mini" 
              :type="libraryViewMode === 'list' ? 'primary' : ''"
              @click="libraryViewMode = 'list'"
            />
          </el-tooltip>
          <el-button icon="el-icon-refresh" size="mini" @click="refreshChartLibrary">刷新</el-button>
        </div>
      </div>
      
      <!-- 网格视图 -->
      <el-row :gutter="16" v-if="libraryViewMode === 'grid'">
        <el-col :span="6" v-for="chart in chartLibrary" :key="chart.id">
          <el-card class="chart-library-item" shadow="hover" @click.native="handleSelectChart(chart)">
            <div class="chart-thumbnail">
              <img :src="chart.thumbnail" :alt="chart.name" />
            </div>
            <div class="chart-info">
              <div class="chart-name">{{ chart.name }}</div>
              <div class="chart-type">{{ getChartTypeText(chart.type) }}</div>
              <div class="chart-stats">
                <span class="create-time">{{ chart.createTime }}</span>
                <span class="usage-count">{{ chart.usageCount }} 次使用</span>
              </div>
            </div>
            <div class="chart-actions">
              <el-button type="text" size="mini" @click.stop="handlePreviewChart(chart)">预览</el-button>
              <el-button type="text" size="mini" @click.stop="handleEditChart(chart)">编辑</el-button>
              <el-button type="text" size="mini" @click.stop="handleDeleteChart(chart)">删除</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 列表视图 -->
      <el-table
        v-if="libraryViewMode === 'list'"
        :data="chartLibrary"
        border
        stripe
        highlight-current-row
        @row-click="handleSelectChart"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="name" label="图表名称" width="200" show-overflow-tooltip />
        <el-table-column prop="type" label="图表类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getChartTypeColor(scope.row.type)" size="mini">
              {{ getChartTypeText(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataSource" label="数据源" width="120" align="center" />
        <el-table-column prop="creator" label="创建人" width="100" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column prop="usageCount" label="使用次数" width="100" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-view" @click="handlePreviewChart(scope.row)">预览</el-button>
            <el-button type="text" size="mini" icon="el-icon-edit" @click="handleEditChart(scope.row)">编辑</el-button>
            <el-button type="text" size="mini" icon="el-icon-copy-document" @click="handleCopyChart(scope.row)">复制</el-button>
            <el-button type="text" size="mini" icon="el-icon-delete" @click="handleDeleteChart(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 创建/编辑图表对话框 -->
    <el-dialog :title="editMode ? '编辑图表' : '创建图表'" :visible.sync="createDialogVisible" width="600px" @close="resetChartForm">
      <el-form :model="chartForm" :rules="chartFormRules" ref="chartFormRef" label-width="100px" size="small">
        <el-form-item label="图表名称" prop="chartName">
          <el-input v-model="chartForm.chartName" placeholder="请输入图表名称" />
        </el-form-item>
        <el-form-item label="图表类型" prop="chartType">
          <el-select v-model="chartForm.chartType" placeholder="请选择图表类型" style="width: 100%">
            <el-option value="LINE" label="折线图" />
            <el-option value="BAR" label="柱状图" />
            <el-option value="PIE" label="饼图" />
            <el-option value="SCATTER" label="散点图" />
            <el-option value="RADAR" label="雷达图" />
            <el-option value="HEATMAP" label="热力图" />
            <el-option value="TREEMAP" label="树图" />
            <el-option value="SANKEY" label="桑基图" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据源" prop="dataSource">
          <el-select v-model="chartForm.dataSource" placeholder="请选择数据源" style="width: 100%">
            <el-option value="BUDGET_DATA" label="预算数据" />
            <el-option value="EXECUTION_DATA" label="执行数据" />
            <el-option value="ANALYSIS_DATA" label="分析数据" />
            <el-option value="FORECAST_DATA" label="预测数据" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="chartForm.description" type="textarea" :rows="3" placeholder="请输入图表描述" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmitChart">{{ editMode ? '保存' : '创建' }}</el-button>
      </div>
    </el-dialog>

    <!-- 图表预览对话框 -->
    <el-dialog title="图表预览" :visible.sync="previewDialogVisible" width="800px">
      <div class="preview-info" v-if="previewChart">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="图表名称">{{ previewChart.chartName || previewChart.name }}</el-descriptions-item>
          <el-descriptions-item label="图表类型">{{ getChartTypeText(previewChart.chartType || previewChart.type) }}</el-descriptions-item>
          <el-descriptions-item label="数据源">{{ previewChart.dataSource }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ previewChart.status }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ previewChart.creator }}</el-descriptions-item>
          <el-descriptions-item label="使用次数">{{ previewChart.usageCount }}</el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">{{ previewChart.description || '暂无描述' }}</el-descriptions-item>
        </el-descriptions>
        <div id="previewChartContainer" style="height: 350px; margin-top: 16px;"></div>
      </div>
      <div slot="footer"><el-button type="primary" @click="previewDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 设置对话框 -->
    <el-dialog title="分析图表设置" :visible.sync="settingsDialogVisible" width="500px">
      <el-form label-width="120px" size="small">
        <el-form-item label="每页显示条数">
          <el-select v-model="queryParams.pageSize" style="width: 100%">
            <el-option label="10条" :value="10" />
            <el-option label="20条" :value="20" />
            <el-option label="50条" :value="50" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false; $message.success('设置已保存')">保存</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="分析图表帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>分析图表用于以可视化方式展示预算数据和分析结果。</p>
        <h4>操作指南</h4>
        <p>1. 使用顶部工具栏的按钮进行创建、刷新和导出操作。</p>
        <p>2. 使用筛选条件缩小分析范围。</p>
        <p>3. 点击表格行查看详细信息。</p>
      </div>
      <div slot="footer"><el-button type="primary" @click="helpDialogVisible = false">我知道了</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { budgetAnalysisApi } from '@/api/managementAccountant/ncv65/budgetAnalysis'

export default {
  name: 'AnalysisChart',
  data() {
    return {
      createDialogVisible: false,
      settingsDialogVisible: false,
      helpDialogVisible: false,
      previewDialogVisible: false,
      editMode: false,
      submitLoading: false,
      queryParams: { pageNum: 1, pageSize: 10 },
      queryForm: { chartType: '', dataSource: '', createTime: [], creator: '', chartName: '' },
      chartStats: { totalCharts: 0, chartTypes: 0, interactiveCharts: 0, totalExports: 0 },
      chartTypes: [],
      selectedChartType: null,
      currentChart: { title: '预算执行趋势图', type: 'line' },
      chartScale: 1,
      chartConfig: { title: '预算执行趋势图', type: 'line', theme: 'default', animation: true, showLegend: true, showToolbox: true, showDataLabels: false, showGrid: true },
      chartDataSeries: [
        { id: 1, seriesName: '预算金额', dataSource: 'BUDGET_DATA', xField: 'month', yField: 'amount', color: '#409EFF', visible: true },
        { id: 2, seriesName: '实际执行', dataSource: 'EXECUTION_DATA', xField: 'month', yField: 'amount', color: '#67C23A', visible: true }
      ],
      libraryViewMode: 'grid',
      chartLibrary: [],
      previewChart: null,
      chartForm: { chartName: '', chartType: 'LINE', dataSource: 'BUDGET_DATA', description: '' },
      chartFormRules: {
        chartName: [{ required: true, message: '请输入图表名称', trigger: 'blur' }],
        chartType: [{ required: true, message: '请选择图表类型', trigger: 'change' }],
        dataSource: [{ required: true, message: '请选择数据源', trigger: 'change' }]
      },
      editChartId: null,
      mainChartInstance: null,
      chartRenderData: { xAxis: [], budgetData: [], executionData: [] }
    }
  },

  created() {
    this.loadChartStats()
    this.loadChartTypes()
    this.loadChartLibrary()
  },

  mounted() {
    this.loadChartData()
  },

  methods: {
    async loadChartStats() {
      try {
        const res = await budgetAnalysisApi.getChartStats()
        if (res.code === 1 && res.data) this.chartStats = { ...this.chartStats, ...res.data }
      } catch (e) { console.error('加载统计失败', e) }
    },
    async loadChartTypes() {
      try {
        const res = await budgetAnalysisApi.getChartTypes()
        if (res.code === 1 && res.data) this.chartTypes = res.data.types || res.data || []
      } catch (e) { console.error('加载类型失败', e) }
    },
    async loadChartLibrary() {
      try {
        const res = await budgetAnalysisApi.getChartLibrary()
        if (res.code === 1 && res.data) {
          const charts = res.data.charts || res.data || []
          this.chartLibrary = charts.map(c => ({
            ...c,
            createTime: c.createTime ? new Date(c.createTime).toLocaleDateString() : ''
          }))
        }
      } catch (e) { console.error('加载图表库失败', e) }
    },
    async loadChartData() {
      try {
        const res = await budgetAnalysisApi.getChartData({})
        if (res.code === 1 && res.data) {
          this.chartRenderData = res.data
          this.initMainChart()
        }
      } catch (e) {
        console.error('加载图表数据失败', e)
        this.initMainChart()
      }
    },
    initMainChart() {
      this.$nextTick(() => {
        const dom = document.getElementById('mainChart')
        if (!dom) return
        if (this.mainChartInstance) this.mainChartInstance.dispose()
        this.mainChartInstance = echarts.init(dom)
        const d = this.chartRenderData
        const option = {
          title: { text: this.chartConfig.title, left: 'center' },
          tooltip: { trigger: 'axis' },
          legend: { data: ['预算金额', '实际执行'], show: this.chartConfig.showLegend, top: 30 },
          grid: { show: this.chartConfig.showGrid, left: '3%', right: '4%', bottom: '3%', containLabel: true },
          toolbox: { show: this.chartConfig.showToolbox, feature: { saveAsImage: {}, dataView: {}, magicType: { type: ['line', 'bar'] }, restore: {} } },
          xAxis: { type: 'category', data: d.xAxis || [] },
          yAxis: { type: 'value', name: '金额(万元)' },
          series: [
            { name: '预算金额', type: this.chartConfig.type, data: d.budgetData || [], itemStyle: { color: '#409EFF' }, label: { show: this.chartConfig.showDataLabels }, smooth: true },
            { name: '实际执行', type: this.chartConfig.type, data: d.executionData || [], itemStyle: { color: '#67C23A' }, label: { show: this.chartConfig.showDataLabels }, smooth: true }
          ],
          animation: this.chartConfig.animation
        }
        this.mainChartInstance.setOption(option)
      })
    },
    // 查询
    handleQuery() {
      const params = { ...this.queryParams }
      if (this.queryForm.chartType) params.chartType = this.queryForm.chartType
      if (this.queryForm.dataSource) params.dataSource = this.queryForm.dataSource
      if (this.queryForm.creator) params.creator = this.queryForm.creator
      if (this.queryForm.chartName) params.chartName = this.queryForm.chartName
      if (this.queryForm.createTime && this.queryForm.createTime.length === 2) {
        params.startTime = this.queryForm.createTime[0] ? new Date(this.queryForm.createTime[0]).toISOString().slice(0, 10) : ''
        params.endTime = this.queryForm.createTime[1] ? new Date(this.queryForm.createTime[1]).toISOString().slice(0, 10) : ''
      }
      budgetAnalysisApi.getChartPage(params).then(res => {
        if (res.code === 1 && res.data) {
          this.chartLibrary = (res.data.list || []).map(c => ({
            ...c,
            createTime: c.createTime ? new Date(c.createTime).toLocaleDateString() : ''
          }))
          this.$message.success('查询完成，共 ' + (res.data.total || 0) + ' 条')
        }
      }).catch(() => this.$message.error('查询失败'))
    },
    handleResetQuery() {
      this.queryForm = { chartType: '', dataSource: '', createTime: [], creator: '', chartName: '' }
      this.loadChartLibrary()
    },
    handleResetFilter() { this.handleResetQuery() },
    // 创建图表
    handleCreateChart() {
      this.editMode = false
      this.editChartId = null
      this.chartForm = { chartName: '', chartType: 'LINE', dataSource: 'BUDGET_DATA', description: '' }
      this.createDialogVisible = true
    },
    // 编辑图表
    handleEditChart(chart) {
      this.editMode = true
      this.editChartId = chart.chartId || chart.id
      this.chartForm = {
        chartName: chart.chartName || chart.name,
        chartType: chart.chartType || chart.type,
        dataSource: chart.dataSource || 'BUDGET_DATA',
        description: chart.description || ''
      }
      this.createDialogVisible = true
    },
    resetChartForm() {
      this.chartForm = { chartName: '', chartType: 'LINE', dataSource: 'BUDGET_DATA', description: '' }
      this.editMode = false
      this.editChartId = null
    },
    async handleSubmitChart() {
      this.$refs.chartFormRef.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          if (this.editMode) {
            const res = await budgetAnalysisApi.updateChart({ chartId: this.editChartId, ...this.chartForm })
            if (res.code === 1) { this.$message.success('更新成功'); this.createDialogVisible = false; this.loadChartLibrary(); this.loadChartStats() }
            else this.$message.error(res.msg || '更新失败')
          } else {
            const res = await budgetAnalysisApi.createChart(this.chartForm)
            if (res.code === 1) { this.$message.success('创建成功'); this.createDialogVisible = false; this.loadChartLibrary(); this.loadChartStats() }
            else this.$message.error(res.msg || '创建失败')
          }
        } catch (e) { this.$message.error('操作失败：' + e.message) }
        finally { this.submitLoading = false }
      })
    },
    // 刷新
    handleRefreshCharts() {
      this.loadChartStats()
      this.loadChartTypes()
      this.loadChartLibrary()
      this.loadChartData()
      this.$message.success('图表已刷新')
    },
    handleBatchExport() {
      budgetAnalysisApi.exportChart({}).then(() => this.$message.success('导出成功')).catch(() => this.$message.error('导出失败'))
    },
    handleChartTemplates() { this.$message.info('模板管理功能开发中') },
    handleChartSettings() { this.settingsDialogVisible = true },
    handleShareCharts() { this.$message.info('分享功能开发中') },
    handleHelp() { this.helpDialogVisible = true },
    refreshChartTypes() { this.loadChartTypes(); this.$message.success('图表类型已刷新') },
    handleSelectChartType(chartType) {
      this.selectedChartType = chartType.id
      const typeMap = { '折线图': 'line', '柱状图': 'bar', '饼图': 'pie', '散点图': 'scatter', '雷达图': 'radar', '热力图': 'heatmap' }
      this.chartConfig.type = typeMap[chartType.name] || chartType.code.toLowerCase()
      this.initMainChart()
    },
    handleZoomIn() { this.chartScale = Math.min(this.chartScale + 0.1, 2) },
    handleZoomOut() { this.chartScale = Math.max(this.chartScale - 0.1, 0.5) },
    handleRefreshChart() { this.loadChartData() },
    handleExportChart() {
      if (this.mainChartInstance) {
        const url = this.mainChartInstance.getDataURL({ type: 'png', pixelRatio: 2 })
        const a = document.createElement('a')
        a.href = url; a.download = 'chart.png'; a.click()
        this.$message.success('图表导出成功')
      }
    },
    handleAdvancedConfig() { this.$message.info('高级配置功能开发中') },
    handleConfigChange() { this.initMainChart() },
    handleApplyConfig() { this.initMainChart(); this.$message.success('配置已应用') },
    handleResetConfig() {
      this.chartConfig = { title: '预算执行趋势图', type: 'line', theme: 'default', animation: true, showLegend: true, showToolbox: true, showDataLabels: false, showGrid: true }
      this.handleApplyConfig()
    },
    handleAddDataSeries() {
      this.chartDataSeries.push({ id: Date.now(), seriesName: '新数据系列', dataSource: 'BUDGET_DATA', xField: 'month', yField: 'amount', color: '#409EFF', visible: true })
    },
    handleRefreshData() { this.loadChartData(); this.$message.success('数据已刷新') },
    handleDataSeriesChange() { this.initMainChart() },
    handleEditDataSeries(series) { this.$message.info('编辑数据系列: ' + series.seriesName) },
    handleDeleteDataSeries(series) {
      this.$confirm('确定删除该数据系列吗？', '提示', { type: 'warning' }).then(() => {
        const idx = this.chartDataSeries.findIndex(s => s.id === series.id)
        if (idx > -1) { this.chartDataSeries.splice(idx, 1); this.$message.success('删除成功'); this.initMainChart() }
      })
    },
    refreshChartLibrary() { this.loadChartLibrary(); this.$message.success('图表库已刷新') },
    handleSelectChart(chart) {
      this.currentChart = chart
      this.chartConfig.title = chart.chartName || chart.name
      const t = (chart.chartType || chart.type || 'line').toLowerCase()
      this.chartConfig.type = t
      this.initMainChart()
    },
    handlePreviewChart(chart) {
      this.previewChart = chart
      this.previewDialogVisible = true
      this.$nextTick(() => {
        const dom = document.getElementById('previewChartContainer')
        if (dom) {
          const c = echarts.init(dom)
          const d = this.chartRenderData
          c.setOption({
            title: { text: chart.chartName || chart.name, left: 'center' },
            tooltip: { trigger: 'axis' },
            legend: { data: ['预算金额', '实际执行'], top: 30 },
            xAxis: { type: 'category', data: d.xAxis || [] },
            yAxis: { type: 'value' },
            series: [
              { name: '预算金额', type: (chart.chartType || chart.type || 'line').toLowerCase(), data: d.budgetData || [], smooth: true },
              { name: '实际执行', type: (chart.chartType || chart.type || 'line').toLowerCase(), data: d.executionData || [], smooth: true }
            ]
          })
        }
      })
    },
    async handleCopyChart(chart) {
      try {
        const res = await budgetAnalysisApi.createChart({
          chartName: (chart.chartName || chart.name) + ' (副本)',
          chartType: chart.chartType || chart.type,
          dataSource: chart.dataSource || 'BUDGET_DATA',
          description: chart.description || ''
        })
        if (res.code === 1) { this.$message.success('复制成功'); this.loadChartLibrary(); this.loadChartStats() }
        else this.$message.error(res.msg || '复制失败')
      } catch (e) { this.$message.error('复制失败') }
    },
    handleDeleteChart(chart) {
      this.$confirm('确定删除该图表吗？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await budgetAnalysisApi.deleteChart(chart.chartId || chart.id)
          if (res.code === 1) { this.$message.success('删除成功'); this.loadChartLibrary(); this.loadChartStats() }
          else this.$message.error(res.msg || '删除失败')
        } catch (e) { this.$message.error('删除失败') }
      })
    },
    getChartTypeColor(type) {
      const colorMap = {
        'LINE': 'primary',
        'BAR': 'success',
        'PIE': 'warning',
        'SCATTER': 'info',
        'RADAR': 'danger',
        'HEATMAP': 'primary'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取图表类型文本
    getChartTypeText(type) {
      const textMap = {
        'LINE': '折线图',
        'BAR': '柱状图',
        'PIE': '饼图',
        'SCATTER': '散点图',
        'RADAR': '雷达图',
        'HEATMAP': '热力图',
        'TREEMAP': '树图',
        'SANKEY': '桑基图'
      }
      return textMap[type] || type
    }
  }
}
</script>

<style lang="scss" scoped>
.analysis-chart {
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
  .filter-card,
  .chart-types-card,
  .data-config-card,
  .chart-library-card {
    margin-bottom: 20px;
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;

      &.charts-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }

      &.types-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.interactive-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.exports-card {
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

  .filter-card {
    .filter-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .filter-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
      }
    }
  }

  .chart-types-card {
    .chart-type-item {
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
      }

      &.selected {
        border-color: #409EFF;
        box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
      }

      .chart-type-icon {
        font-size: 32px;
        color: #409EFF;
        margin-bottom: 12px;
      }

      .chart-type-title {
        font-size: 14px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .chart-type-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
        line-height: 1.4;
      }

      .chart-type-stats {
        .usage-count {
          font-size: 11px;
          color: #909399;
          background: #F5F7FA;
          padding: 2px 8px;
          border-radius: 10px;
        }
      }
    }
  }

  .chart-display-row {
    margin-bottom: 20px;

    .main-chart-card,
    .chart-config-card {
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

      .main-chart-container {
        height: 400px;
        transform-origin: center;
        transition: transform 0.3s ease;
      }
    }
  }

  .chart-library-card {
    .chart-library-item {
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
      }

      .chart-thumbnail {
        height: 120px;
        background: #F5F7FA;
        border-radius: 4px;
        margin-bottom: 12px;
        display: flex;
        align-items: center;
        justify-content: center;

        img {
          max-width: 100%;
          max-height: 100%;
          object-fit: cover;
        }
      }

      .chart-info {
        margin-bottom: 12px;

        .chart-name {
          font-size: 14px;
          font-weight: 500;
          color: #303133;
          margin-bottom: 4px;
        }

        .chart-type {
          font-size: 12px;
          color: #606266;
          margin-bottom: 8px;
        }

        .chart-stats {
          display: flex;
          justify-content: space-between;
          font-size: 11px;
          color: #909399;
        }
      }

      .chart-actions {
        display: flex;
        justify-content: space-between;
        border-top: 1px solid #EBEEF5;
        padding-top: 8px;
      }
    }
  }

  .text-right {
    text-align: right;
  }
}
</style>
