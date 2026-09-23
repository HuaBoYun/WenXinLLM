<template>
  <div class="drill-through-query">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>穿透查询管理</h2>
      <p>多维度数据穿透查询和钻取分析，支持从汇总数据钻取到明细数据的全链路查询</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateQuery">创建查询</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-search" @click="handleExecuteQuery">执行查询</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewResults">查询结果</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">查询设置</el-button>
            <el-button icon="el-icon-document" @click="handleReports">查询报告</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 穿透查询统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ queryStats.totalQueries }}</div>
            <div class="stat-label">查询总数</div>
            <div class="stat-description">系统中的查询数量</div>
            <div class="stat-trend">
              <i class="el-icon-search"></i>
              <span>全覆盖</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-search"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ queryStats.activeQueries }}</div>
            <div class="stat-label">活跃查询</div>
            <div class="stat-description">正在执行的查询</div>
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
        <el-card class="stat-card dimension-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ queryStats.dimensions }}</div>
            <div class="stat-label">查询维度</div>
            <div class="stat-description">支持的查询维度</div>
            <div class="stat-trend">
              <i class="el-icon-menu"></i>
              <span>多维度</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-menu"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card performance-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ queryStats.avgResponseTime }}ms</div>
            <div class="stat-label">平均响应</div>
            <div class="stat-description">查询平均响应时间</div>
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

    <!-- 查询类型选择 -->
    <el-card class="query-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>查询类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshQueryTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="queryType in queryTypes" :key="queryType.id">
          <el-card 
            class="query-type-item" 
            shadow="hover" 
            @click.native="handleSelectQueryType(queryType)"
            :class="{ 'selected': selectedQueryType === queryType.id }"
          >
            <div class="query-type-icon">
              <i :class="queryType.icon"></i>
            </div>
            <div class="query-type-title">{{ queryType.name }}</div>
            <div class="query-type-description">{{ queryType.description }}</div>
            <div class="query-type-stats">
              <span class="query-count">{{ queryType.queryCount }} 个查询</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 穿透查询列表 -->
    <el-card class="drill-queries-card" shadow="never">
      <div slot="header" class="card-header">
        <span>穿透查询</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索查询"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getDrillQueryList"
            clearable
            @clear="getDrillQueryList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getDrillQueryList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="drillQueryList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="queryName" label="查询名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.queryName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="queryType" label="查询类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getQueryTypeColor(scope.row.queryType)" size="mini">
              {{ getQueryTypeText(scope.row.queryType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dimensionCount" label="维度数量" width="100" align="center">
          <template slot-scope="scope">
            <span class="dimension-count">{{ scope.row.dimensionCount }} 个</span>
          </template>
        </el-table-column>
        <el-table-column prop="drillLevels" label="钻取层级" width="100" align="center">
          <template slot-scope="scope">
            <span class="drill-levels">{{ scope.row.drillLevels }} 级</span>
          </template>
        </el-table-column>
        <el-table-column prop="responseTime" label="响应时间" width="120" align="center">
          <template slot-scope="scope">
            <span class="response-time">{{ scope.row.responseTime }}ms</span>
          </template>
        </el-table-column>
        <el-table-column prop="resultCount" label="结果数量" width="100" align="center">
          <template slot-scope="scope">
            <span class="result-count">{{ scope.row.resultCount }} 条</span>
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
              icon="el-icon-search"
              @click.stop="handleExecute(scope.row)"
              :disabled="scope.row.status === 'RUNNING'"
            >执行</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)" @click.native.stop>
              <el-button type="text" size="mini" @click.stop>
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="stop">停止</el-dropdown-item>
                <el-dropdown-item command="export">导出</el-dropdown-item>
                <el-dropdown-item command="optimize">优化</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 查询详情抽屉 -->
    <el-drawer
      title="穿透查询详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="70%"
    >
      <div class="detail-content" v-if="currentQuery">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="查询基本信息" :column="2" border>
              <el-descriptions-item label="查询名称">{{ currentQuery.queryName }}</el-descriptions-item>
              <el-descriptions-item label="查询类型">{{ getQueryTypeText(currentQuery.queryType) }}</el-descriptions-item>
              <el-descriptions-item label="维度数量">{{ currentQuery.dimensionCount }} 个</el-descriptions-item>
              <el-descriptions-item label="钻取层级">{{ currentQuery.drillLevels }} 级</el-descriptions-item>
              <el-descriptions-item label="响应时间">{{ currentQuery.responseTime }}ms</el-descriptions-item>
              <el-descriptions-item label="结果数量">{{ currentQuery.resultCount }} 条</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentQuery.status)" size="mini">
                  {{ getStatusText(currentQuery.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentQuery.creator }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentQuery.createTime }}</el-descriptions-item>
              <el-descriptions-item label="查询描述" :span="2">{{ currentQuery.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="查询配置" name="config">
            <el-form label-width="120px" size="small">
              <el-form-item label="数据源">
                <el-input :value="currentQuery.dataSource" readonly />
              </el-form-item>
              <el-form-item label="查询条件">
                <el-input :value="currentQuery.queryCondition" readonly type="textarea" :rows="3" />
              </el-form-item>
              <el-form-item label="钻取路径">
                <el-input :value="currentQuery.drillPath" readonly />
              </el-form-item>
              <el-form-item label="排序规则">
                <el-input :value="currentQuery.sortRule" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="查询结果" name="results">
            <el-table :data="queryResults" border size="mini" max-height="400">
              <el-table-column
                v-for="column in resultColumns"
                :key="column.prop"
                :prop="column.prop"
                :label="column.label"
                :width="column.width"
                :align="column.align || 'left'"
              />
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
              <el-table-column prop="executionTime" label="执行时间" width="120" align="center" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑查询对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="queryForm"
        :model="queryForm"
        :rules="queryRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="查询名称" prop="queryName">
              <el-input v-model="queryForm.queryName" placeholder="请输入查询名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="查询类型" prop="queryType">
              <el-select v-model="queryForm.queryType" placeholder="请选择查询类型" style="width: 100%">
                <el-option value="DIMENSION" label="维度钻取" />
                <el-option value="TIME" label="时间钻取" />
                <el-option value="HIERARCHY" label="层级钻取" />
                <el-option value="CUSTOM" label="自定义钻取" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据源" prop="dataSource">
              <el-select v-model="queryForm.dataSource" placeholder="请选择数据源" style="width: 100%">
                <el-option value="BUDGET_DATA" label="预算数据" />
                <el-option value="ACTUAL_DATA" label="实际数据" />
                <el-option value="FORECAST_DATA" label="预测数据" />
                <el-option value="ANALYSIS_DATA" label="分析数据" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="钻取层级" prop="drillLevels">
              <el-input-number v-model="queryForm.drillLevels" :min="1" :max="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="查询维度" prop="queryDimensions">
          <el-select
            v-model="queryForm.queryDimensions"
            multiple
            placeholder="请选择查询维度"
            style="width: 100%"
          >
            <el-option
              v-for="dimension in availableDimensions"
              :key="dimension.id"
              :label="dimension.name"
              :value="dimension.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="查询条件" prop="queryCondition">
          <el-input
            v-model="queryForm.queryCondition"
            type="textarea"
            :rows="3"
            placeholder="请输入查询条件"
          />
        </el-form-item>
        <el-form-item label="钻取路径" prop="drillPath">
          <el-input v-model="queryForm.drillPath" placeholder="请输入钻取路径" />
        </el-form-item>
        <el-form-item label="查询描述" prop="description">
          <el-input
            v-model="queryForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入查询描述"
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
  name: 'DrillThroughQuery',
  data() {
    return {
      // 统计数据
      queryStats: {
        totalQueries: 0,
        activeQueries: 0,
        dimensions: 0,
        avgResponseTime: 0
      },

      // 查询类型
      queryTypes: [
        { id: 1, name: '维度钻取', description: '按维度进行数据钻取', icon: 'el-icon-menu', queryCount: 0 },
        { id: 2, name: '时间钻取', description: '按时间维度钻取', icon: 'el-icon-time', queryCount: 0 },
        { id: 3, name: '层级钻取', description: '按层级结构钻取', icon: 'el-icon-s-grid', queryCount: 0 },
        { id: 4, name: '自定义钻取', description: '自定义钻取规则', icon: 'el-icon-setting', queryCount: 0 }
      ],
      selectedQueryType: null,
      
      // 查询列表
      drillQueryList: [],
      loading: false,
      searchKeyword: '',
      
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentQuery: null,
      queryResults: [],
      resultColumns: [],
      executionLogs: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      
      // 表单数据
      queryForm: {
        queryName: '',
        queryType: '',
        dataSource: '',
        drillLevels: 3,
        queryDimensions: [],
        queryCondition: '',
        drillPath: '',
        description: ''
      },
      
      // 可用维度列表
      availableDimensions: [],
      
      // 表单验证规则
      queryRules: {
        queryName: [
          { required: true, message: '请输入查询名称', trigger: 'blur' }
        ],
        queryType: [
          { required: true, message: '请选择查询类型', trigger: 'change' }
        ],
        dataSource: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ]
      }
    }
  },
  
  created() {
    this.getDrillQueryList()
    this.getQueryStats()
    this.getAvailableDimensions()
  },
  
  methods: {
    // 获取查询列表
    async getDrillQueryList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword }
        if (this.selectedQueryType) {
          params.queryType = this.selectedQueryType
        }
        const response = await advancedFeaturesApi.getDrillThroughQueryList(params)
        if (response && response.code === 1) {
          const data = response.data || {}
          this.drillQueryList = data.list || data || []
          // 同步更新查询类型卡片计数
          this.updateQueryTypeCounts(this.drillQueryList)
        }
      } catch (error) {
        this.$message.error('获取查询列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 更新查询类型卡片计数
    updateQueryTypeCounts(list) {
      const typeMap = { DIMENSION: 0, TIME: 0, HIERARCHY: 0, CUSTOM: 0 }
      list.forEach(item => {
        if (item.queryType && typeMap[item.queryType] !== undefined) {
          typeMap[item.queryType]++
        }
      })
      this.queryTypes = [
        { id: 'DIMENSION', name: '维度钻取', description: '按维度进行数据钻取', icon: 'el-icon-menu', queryCount: typeMap.DIMENSION },
        { id: 'TIME', name: '时间钻取', description: '按时间维度钻取', icon: 'el-icon-time', queryCount: typeMap.TIME },
        { id: 'HIERARCHY', name: '层级钻取', description: '按层级结构钻取', icon: 'el-icon-s-grid', queryCount: typeMap.HIERARCHY },
        { id: 'CUSTOM', name: '自定义钻取', description: '自定义钻取规则', icon: 'el-icon-setting', queryCount: typeMap.CUSTOM }
      ]
    },

    // 获取统计数据
    async getQueryStats() {
      try {
        const response = await advancedFeaturesApi.getDrillThroughQueryStats()
        if (response && response.code === 1 && response.data) {
          this.queryStats = response.data
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    // 获取可用维度列表
    async getAvailableDimensions() {
      try {
        const response = await advancedFeaturesApi.getAvailableDimensions({})
        if (response && response.code === 1) {
          const data = response.data || {}
          this.availableDimensions = data.list || data.dimensions || []
        }
      } catch (error) {
        console.error('获取可用维度失败：', error)
      }
    },
    
    // 创建查询
    handleCreateQuery() {
      this.dialogTitle = '创建穿透查询'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑查询
    handleEdit(row) {
      this.dialogTitle = '编辑穿透查询'
      this.dialogVisible = true
      this.queryForm = { ...row }
    },
    
    // 查看详情
    async handleView(row) {
      this.currentQuery = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await this.getQueryResults(row.queryId)
      await this.getExecutionLogs(row.queryId)
    },
    
    // 获取查询结果
    async getQueryResults(queryId) {
      try {
        const response = await advancedFeaturesApi.getDrillThroughQueryResult(queryId)
        this.queryResults = response.data.results
        this.resultColumns = response.data.columns
      } catch (error) {
        console.error('获取查询结果失败：', error)
      }
    },
    
    // 获取执行日志
    async getExecutionLogs(queryId) {
      try {
        const response = await advancedFeaturesApi.getDrillThroughQueryLogs(queryId)
        this.executionLogs = response.data
      } catch (error) {
        console.error('获取执行日志失败：', error)
      }
    },
    
    // 执行查询
    async handleExecute(row) {
      this.$confirm('确定执行该穿透查询吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.executeDrillThroughQuery(row.queryId)
          this.$message.success('查询已启动')
          this.getDrillQueryList()
        } catch (error) {
          this.$message.error('执行失败：' + error.message)
        }
      })
    },
    
    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'stop':
          this.handleStopQuery(row)
          break
        case 'export':
          this.handleExportQuery(row)
          break
        case 'optimize':
          this.handleOptimizeQuery(row)
          break
        case 'copy':
          this.handleCopyQuery(row)
          break
        case 'delete':
          this.handleDeleteQuery(row)
          break
      }
    },
    
    // 停止查询
    async handleStopQuery(row) {
      try {
        await advancedFeaturesApi.stopDrillThroughQuery(row.queryId)
        this.$message.success('查询已停止')
        this.getDrillQueryList()
      } catch (error) {
        this.$message.error('停止失败：' + error.message)
      }
    },
    
    // 导出查询
    async handleExportQuery(row) {
      try {
        await advancedFeaturesApi.exportDrillThroughQuery(row.queryId)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 优化查询
    async handleOptimizeQuery(row) {
      try {
        await advancedFeaturesApi.optimizeDrillThroughQuery(row.queryId)
        this.$message.success('优化成功')
        this.getDrillQueryList()
      } catch (error) {
        this.$message.error('优化失败：' + error.message)
      }
    },
    
    // 复制查询
    async handleCopyQuery(row) {
      try {
        await advancedFeaturesApi.copyDrillThroughQuery(row.queryId)
        this.$message.success('复制成功')
        this.getDrillQueryList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },
    
    // 删除查询
    handleDeleteQuery(row) {
      this.$confirm('确定删除该穿透查询吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteDrillThroughQuery(row.queryId)
          this.$message.success('删除成功')
          this.getDrillQueryList()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },
    
    // 提交表单
    async handleSubmitForm() {
      this.$refs.queryForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            if (this.queryForm.queryId) {
              await advancedFeaturesApi.updateDrillThroughQuery(this.queryForm)
              this.$message.success('更新成功')
            } else {
              await advancedFeaturesApi.createDrillThroughQuery(this.queryForm)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getDrillQueryList()
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
      this.queryForm = {
        queryName: '',
        queryType: '',
        dataSource: '',
        drillLevels: 3,
        queryDimensions: [],
        queryCondition: '',
        drillPath: '',
        description: ''
      }
      this.$nextTick(() => {
        this.$refs.queryForm && this.$refs.queryForm.clearValidate()
      })
    },
    
    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },
    
    // 刷新
    handleRefresh() {
      this.getDrillQueryList()
      this.getQueryStats()
    },
    
    // 执行查询（弹窗选择）
    handleExecuteQuery() {
      if (!this.drillQueryList.length) {
        this.$message.warning('暂无可执行的查询，请先创建查询')
        return
      }
      this.$alert('请在列表中选择具体查询后点击"执行"按钮', '执行查询', { confirmButtonText: '知道了' })
    },

    // 查看结果（弹窗提示）
    handleViewResults() {
      if (!this.drillQueryList.length) {
        this.$message.warning('暂无查询结果，请先执行查询')
        return
      }
      this.$alert('请在列表中点击查询名称查看详细结果', '查询结果', { confirmButtonText: '知道了' })
    },

    // 查询设置（弹窗）
    handleSettings() {
      this.$alert('查询设置：支持维度钻取、时间钻取、层级钻取、自定义钻取四种模式，可在创建查询时配置', '查询设置说明', { confirmButtonText: '知道了' })
    },

    // 查询报告（弹窗）
    handleReports() {
      const total = this.queryStats.totalQueries || 0
      const active = this.queryStats.activeQueries || 0
      const avg = this.queryStats.avgResponseTime || 0
      this.$alert(
        `查询总数：${total} 个\n活跃查询：${active} 个\n平均响应：${avg}ms`,
        '查询报告',
        { confirmButtonText: '关闭' }
      )
    },
    
    // 帮助
    handleHelp() {
      this.$alert('穿透查询支持从汇总数据逐层钻取到明细数据，支持维度、时间、层级、自定义四种钻取模式。', '使用帮助', { confirmButtonText: '知道了' })
    },
    
    // 刷新查询类型
    refreshQueryTypes() {
      this.getDrillQueryList()
      this.getQueryStats()
      this.$message.success('已刷新')
    },

    // 选择查询类型
    handleSelectQueryType(queryType) {
      this.selectedQueryType = queryType.id
      this.getDrillQueryList()
    },
    
    // 获取查询类型颜色
    getQueryTypeColor(type) {
      const colorMap = {
        'DIMENSION': 'primary',
        'TIME': 'success',
        'HIERARCHY': 'warning',
        'CUSTOM': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取查询类型文本
    getQueryTypeText(type) {
      const textMap = {
        'DIMENSION': '维度钻取',
        'TIME': '时间钻取',
        'HIERARCHY': '层级钻取',
        'CUSTOM': '自定义钻取'
      }
      return textMap[type] || type
    },
    
    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'RUNNING': 'primary',   'running': 'primary',
        'COMPLETED': 'success', 'completed': 'success',
        'FAILED': 'danger',     'failed': 'danger',
        'STOPPED': 'warning',   'stopped': 'warning',
        'PENDING': 'info',      'pending': 'info',
        'IDLE': 'info',         'idle': 'info'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'RUNNING': '执行中',   'running': '执行中',
        'COMPLETED': '已完成', 'completed': '已完成',
        'FAILED': '失败',      'failed': '失败',
        'STOPPED': '已停止',   'stopped': '已停止',
        'PENDING': '待执行',   'pending': '待执行',
        'IDLE': '空闲',        'idle': '空闲'
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
.drill-through-query {
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

      &.dimension-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.performance-card {
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

  .query-types-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .query-type-item {
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

      .query-type-icon {
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

      .query-type-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .query-type-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
      }

      .query-type-stats {
        .query-count {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .drill-queries-card {
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

    .dimension-count {
      color: #409EFF;
      font-weight: 500;
    }

    .drill-levels {
      color: #E6A23C;
      font-weight: 500;
    }

    .response-time {
      color: #67C23A;
      font-weight: 500;
    }

    .result-count {
      color: #F56C6C;
      font-weight: 500;
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
