<template>
  <div class="data-model-container risk-mxgl-sjmxgl-page sjmxgl-dialog-scope">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>数据模型管理</h2>
      <p>管理数据模型的创建、编辑、版本控制和发布</p>
    </div>

    <!-- 功能导航卡片 -->
    <div class="function-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card
            class="function-card"
            @click.native="handleSqlTemplateManagement"
          >
            <div class="card-content">
              <i class="el-icon-document-copy card-icon"></i>
              <h3>SQL模板管理</h3>
              <p>管理SQL模板，包含13个预置采购审计模板</p>
              <div class="card-stats">
                <span>模板数量: {{ templateStats.totalCount || 0 }}</span>
                <span>系统预置: {{ templateStats.systemCount || 0 }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card
            class="function-card"
            @click.native="handleDataModelManagement"
          >
            <div class="card-content">
              <i class="el-icon-s-data card-icon"></i>
              <h3>数据模型管理</h3>
              <p>创建和管理复杂的数据模型，支持版本控制</p>
              <div class="card-stats">
                <span>模型数量: {{ modelStats.totalCount || 0 }}</span>
                <span>已发布: {{ modelStats.publishedCount || 0 }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="function-card" @click.native="handleSqlEditor">
            <div class="card-content">
              <i class="el-icon-edit card-icon"></i>
              <h3>SQL可视化编辑器</h3>
              <p>拖拽式SQL构建器，支持复杂查询语句</p>
              <div class="card-stats">
                <span>支持WITH子句</span>
                <span>支持CASE WHEN</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card
            class="function-card"
            @click.native="handleIndicatorCombination"
          >
            <div class="card-content">
              <i class="el-icon-connection card-icon"></i>
              <h3>指标组合分析</h3>
              <p>多指标组合查询，支持顺序/并行执行和交集分析</p>
              <div class="card-stats">
                <span>
                  组合数量: {{ combinationStats.totalCombinations || 0 }}
                </span>
                <span>
                  执行次数: {{ combinationStats.totalExecutions || 0 }}
                </span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 快速统计 -->
    <div class="quick-stats">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">{{ overallStats.totalModels || 0 }}</div>
            <div class="stat-label">总模型数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">
              {{ overallStats.totalTemplates || 0 }}
            </div>
            <div class="stat-label">总模板数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">
              {{ overallStats.totalExecutions || 0 }}
            </div>
            <div class="stat-label">总执行次数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">
              {{ overallStats.activeDataSources || 0 }}
            </div>
            <div class="stat-label">活跃数据源</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 数据模型管理列表 -->
    <div class="data-model-list">
      <el-card>
        <div slot="header" class="card-header">
          <span>📊 数据管理</span>
          <div class="header-actions">
            <el-button
              size="mini"
              icon="el-icon-refresh"
              @click="refreshCurrentList"
            >
              刷新
            </el-button>
          </div>
        </div>

        <!-- Tab切换 -->
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <!-- 指标组合分析Tab -->
          <el-tab-pane label="指标组合分析" name="combination">
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="combinationQueryForm" inline size="small">
                <el-form-item label="组合名称">
                  <el-input
                    v-model="combinationQueryForm.combinationName"
                    placeholder="请输入组合名称"
                    clearable
                    style="width: 200px"
                  />
                </el-form-item>
                <el-form-item label="业务分类">
                  <el-select
                    v-model="combinationQueryForm.category"
                    placeholder="请选择业务分类"
                    clearable
                    style="width: 150px"
                  >
                    <el-option label="采购管理" value="PROCUREMENT" />
                    <el-option label="财务管理" value="FINANCE" />
                    <el-option label="合规管理" value="COMPLIANCE" />
                    <el-option label="风险管理" value="RISK" />
                  </el-select>
                </el-form-item>
                <el-form-item label="状态">
                  <el-select
                    v-model="combinationQueryForm.status"
                    placeholder="请选择状态"
                    clearable
                    style="width: 120px"
                  >
                    <el-option label="草稿" value="DRAFT" />
                    <el-option label="启用" value="ACTIVE" />
                    <el-option label="禁用" value="INACTIVE" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button
                    type="primary"
                    icon="el-icon-search"
                    @click="searchCombinationList"
                  >
                    搜索
                  </el-button>
                  <el-button
                    icon="el-icon-refresh"
                    @click="resetCombinationSearch"
                  >
                    重置
                  </el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 指标组合表格 -->
            <el-table
              :data="combinationList"
              v-loading="combinationListLoading"
              border
              stripe
              height="400"
              @selection-change="handleCombinationSelectionChange"
            >
              <el-table-column type="selection" width="55" />
              <el-table-column
                prop="combinationCode"
                label="组合编码"
                width="120"
              />
              <el-table-column
                prop="combinationName"
                label="组合名称"
                min-width="150"
                show-overflow-tooltip
              />
              <el-table-column prop="category" label="业务分类" width="120">
                <template slot-scope="scope">
                  <el-tag
                    :type="getCombinationCategoryTagType(scope.row.category)"
                    size="mini"
                  >
                    {{ getCombinationCategoryText(scope.row.category) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column
                prop="executionMode"
                label="执行模式"
                width="100"
              >
                <template slot-scope="scope">
                  <el-tag
                    :type="
                      scope.row.executionMode === 'SEQUENCE'
                        ? 'primary'
                        : 'success'
                    "
                    size="mini"
                  >
                    {{
                      scope.row.executionMode === 'SEQUENCE' ? '顺序' : '并行'
                    }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column
                prop="indicatorCount"
                label="指标数量"
                width="90"
              />
              <el-table-column prop="status" label="状态" width="80">
                <template slot-scope="scope">
                  <el-tag
                    :type="getStatusTagType(scope.row.status)"
                    size="mini"
                  >
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="150" />
            </el-table>

            <!-- 分页组件 -->
            <div class="pagination-container">
              <el-pagination
                @size-change="handleCombinationPageSizeChange"
                @current-change="handleCombinationPageChange"
                :current-page="combinationQueryForm.pageNum"
                :page-sizes="[5, 10, 20, 50]"
                :page-size="combinationQueryForm.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="combinationTotal"
              />
            </div>
          </el-tab-pane>

          <!-- 数据模型管理Tab -->
          <el-tab-pane label="数据模型管理" name="dataModel">
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="modelQueryForm" inline size="small">
                <el-form-item label="模型名称">
                  <el-input
                    v-model="modelQueryForm.modelName"
                    placeholder="请输入模型名称"
                    clearable
                    style="width: 200px"
                  />
                </el-form-item>
                <el-form-item label="模型类型">
                  <el-select
                    v-model="modelQueryForm.modelType"
                    placeholder="请选择模型类型"
                    clearable
                    style="width: 150px"
                  >
                    <el-option label="财务模型" value="FINANCIAL" />
                    <el-option label="风险模型" value="RISK" />
                    <el-option label="审计模型" value="AUDIT" />
                  </el-select>
                </el-form-item>
                <el-form-item label="状态">
                  <el-select
                    v-model="modelQueryForm.status"
                    placeholder="请选择状态"
                    clearable
                    style="width: 120px"
                  >
                    <el-option label="草稿" value="DRAFT" />
                    <el-option label="已发布" value="PUBLISHED" />
                    <el-option label="已归档" value="ARCHIVED" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button
                    type="primary"
                    icon="el-icon-search"
                    @click="searchModelList"
                  >
                    搜索
                  </el-button>
                  <el-button icon="el-icon-refresh" @click="resetModelSearch">
                    重置
                  </el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 数据模型表格 -->
            <el-table
              :data="modelList"
              v-loading="modelListLoading"
              border
              stripe
              height="400"
              @selection-change="handleModelSelectionChange"
            >
              <el-table-column type="selection" width="55" />
              <el-table-column prop="modelCode" label="模型编码" width="120" />
              <el-table-column
                prop="modelName"
                label="模型名称"
                min-width="150"
                show-overflow-tooltip
              />
              <el-table-column prop="modelType" label="模型类型" width="100">
                <template slot-scope="scope">
                  <el-tag
                    :type="getModelTypeTagType(scope.row.modelType)"
                    size="mini"
                  >
                    {{ getModelTypeText(scope.row.modelType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="80">
                <template slot-scope="scope">
                  <el-tag
                    :type="getStatusTagType(scope.row.status)"
                    size="mini"
                  >
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="isEnabled" label="启用状态" width="80">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.isEnabled"
                    active-value="Y"
                    inactive-value="N"
                    @change="handleModelStatusChange(scope.row)"
                  />
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="150" />
            </el-table>

            <!-- 分页组件 -->
            <div class="pagination-container">
              <el-pagination
                @size-change="handleModelPageSizeChange"
                @current-change="handleModelPageChange"
                :current-page="modelQueryForm.pageNum"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="modelQueryForm.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="modelTotal"
              />
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>

    <!-- SQL模板管理对话框 -->
    <SqlTemplateDialog
      ref="sqlTemplateDialog"
      :visible.sync="sqlTemplateDialogVisible"
    />

    <!-- 数据模型管理对话框 -->
    <DataModelDialog
      :visible.sync="dataModelDialogVisible"
      @open-sql-template-management="handleOpenSqlTemplateManagement"
      @open-sql-editor="handleOpenSqlEditorWithModel"
    />

    <!-- SQL编辑器对话框 -->
    <SqlEditorDialog
      :visible.sync="sqlEditorDialogVisible"
      :model-data="currentEditModel"
      @refresh="handleRefreshData"
    />

    <!-- 指标组合分析对话框 -->
    <IndicatorCombinationDialog
      ref="indicatorCombinationDialog"
      :visible.sync="indicatorCombinationDialogVisible"
      @update:visible="handleIndicatorCombinationVisibleChange"
    />
  </div>
</template>

<script>
  import {
    getSqlTemplateStatistics,
    getDataSourceStatistics,
    getCombinationStatistics,
    getDataModelStatistics,
    getDataModelList,
    saveDataModel,
    getCombinationList,
  } from '@/api/mxgl'
  import SqlTemplateDialog from './components/SqlTemplateDialog'
  import DataModelDialog from './components/DataModelDialog'
  import SqlEditorDialog from './components/SqlEditorDialog'
  import IndicatorCombinationDialog from './components/IndicatorCombinationDialog'
  // 引入z-index层级管理样式
  import './components/dialog-z-index.css'

  export default {
    name: 'DataModelManagement',
    components: {
      SqlTemplateDialog,
      DataModelDialog,
      SqlEditorDialog,
      IndicatorCombinationDialog,
    },
    data() {
      return {
        // 统计数据
        templateStats: {},
        modelStats: {},
        combinationStats: {},
        overallStats: {},
        // Tab切换
        activeTab: 'combination',
        // 数据模型列表相关
        modelList: [],
        modelListLoading: false,
        modelTotal: 0,
        modelQueryForm: {
          pageNum: 1,
          pageSize: 20,
          modelName: '',
          modelType: '',
          status: '',
          isEnabled: '',
        },
        selectedModels: [],
        // 指标组合列表相关
        combinationList: [],
        combinationListLoading: false,
        combinationTotal: 0,
        combinationQueryForm: {
          pageNum: 1,
          pageSize: 5,
          combinationName: '',
          category: '',
          status: '',
          createUser: '',
        },
        selectedCombinations: [],
        // 对话框状态
        sqlTemplateDialogVisible: false,
        dataModelDialogVisible: false,
        sqlEditorDialogVisible: false,
        indicatorCombinationDialogVisible: false,

        // 🔥 新增：用户主动关闭标志
        userClosedDialog: false,

        // 当前编辑的模型数据
        currentEditModel: null,
      }
    },
    created() {
      this.loadStatistics()
      this.loadModelList()
      this.loadCombinationList()
      // 🆕 新增：检查路由参数，处理来自风险预警页面的跳转
      this.handleRouteParams()
    },
    methods: {
      // 加载统计数据
      async loadStatistics() {
        try {
          // 并行调用4个统计接口，避免串行等待
          const [templateResponse, modelResponse, dataSourceResponse, combinationStatsResponse] = await Promise.all([
            getSqlTemplateStatistics(),
            getDataModelStatistics(),
            getDataSourceStatistics(),
            getCombinationStatistics()
          ])

          // 处理模板统计
          if (templateResponse && templateResponse.code === 1) {
            this.templateStats = templateResponse.data || {}
          }

          // 处理数据模型统计
          if (modelResponse && modelResponse.code === 1) {
            this.modelStats = modelResponse.data || {}
          }

          // 处理数据源统计
          if (dataSourceResponse && dataSourceResponse.code === 1) {
            const dsStats = dataSourceResponse.data || {}
            this.overallStats.activeDataSources = dsStats.activeCount || 0
          }

          // 处理指标组合统计
          if (combinationStatsResponse && combinationStatsResponse.code === 1) {
            this.combinationStats = combinationStatsResponse.data
          }

          // 计算总体统计
          this.calculateOverallStats()
        } catch (error) {
          console.error('加载统计数据失败:', error)
        }
      },

      // 计算总体统计
      calculateOverallStats() {
        // 总模型数 = 数据模型数量 + 指标组合数量
        const totalModels =
          (this.modelStats.totalCount || 0) +
          (this.combinationStats.totalCombinations || 0)

        // 总执行次数 = 数据模型执行次数 + 指标组合执行次数
        const totalExecutions =
          (this.modelStats.totalExecutions || 0) +
          (this.combinationStats.totalExecutions || 0)

        this.overallStats = {
          ...this.overallStats,
          totalModels: totalModels,
          totalTemplates: this.templateStats.totalCount || 0,
          totalExecutions: totalExecutions,
        }

        console.log('📊 总体统计计算完成:', {
          数据模型数量: this.modelStats.totalCount || 0,
          指标组合数量: this.combinationStats.totalCombinations || 0,
          总模型数: totalModels,
          数据模型执行次数: this.modelStats.totalExecutions || 0,
          指标组合执行次数: this.combinationStats.totalExecutions || 0,
          总执行次数: totalExecutions,
        })
      },

      // SQL模板管理
      handleSqlTemplateManagement() {
        this.sqlTemplateDialogVisible = true
      },

      // 数据模型管理
      handleDataModelManagement() {
        this.dataModelDialogVisible = true
      },

      // SQL编辑器
      handleSqlEditor() {
        this.currentEditModel = null // 新建模式
        this.sqlEditorDialogVisible = true
      },

      // 打开SQL编辑器并加载模型数据
      handleOpenSqlEditorWithModel(modelData) {
        this.currentEditModel = modelData // 编辑模式
        this.sqlEditorDialogVisible = true
      },

      // 指标组合分析
      handleIndicatorCombination() {
        console.log('🎯 打开指标组合分析对话框')
        // 🔥 重置用户关闭标志
        this.userClosedDialog = false
        this.indicatorCombinationDialogVisible = true
      },

      // 🔥 修复：处理指标组合分析对话框可见性变化
      handleIndicatorCombinationVisibleChange(visible) {
        console.log('🔍 指标组合分析对话框可见性变化:', visible)

        // 🔥 修复：只有在用户主动关闭时才更新状态
        if (!visible) {
          // 检查是否是用户主动关闭
          if (this.userClosedDialog) {
            console.log('🔒 用户主动关闭指标组合分析对话框')
            this.indicatorCombinationDialogVisible = false
            this.userClosedDialog = false
          } else {
            console.log('⚠️ 检测到非用户主动关闭，可能是子对话框影响')
            // 延迟检查，如果确实需要关闭才关闭
            setTimeout(() => {
              if (!this.indicatorCombinationDialogVisible) {
                console.log('🔄 确认关闭指标组合分析对话框')
              }
            }, 100)
          }
        } else {
          // 对话框打开
          this.indicatorCombinationDialogVisible = visible
        }
      },

      // 🆕 新增：处理路由参数
      handleRouteParams() {
        const {
          action,
          modelId,
          modelName,
          modelType,
          dataModelId,
          combinationId,
        } = this.$route.query

        console.log('🔍 数据模型管理页面接收到路由参数:', {
          action,
          modelId,
          modelName,
          modelType,
          dataModelId,
          combinationId,
        })

        if (action && modelId) {
          // 显示来源信息
          this.$message.success(`来自评估模型"${modelName || modelId}"的跳转`)

          if (action === 'viewFlowChart' && combinationId) {
            // 查看流程图：打开指标组合分析并直接显示流程图
            console.log('🎯 准备打开指标组合分析流程图')

            // 延迟执行，确保页面完全加载
            this.$nextTick(() => {
              setTimeout(() => {
                this.openIndicatorCombinationWithFlowChart(combinationId, {
                  modelId,
                  modelName,
                  modelType,
                  dataModelId,
                })
              }, 500)
            })
          } else if (action === 'viewDataModel' && dataModelId) {
            // 查看数据模型：显示数据模型详情
            console.log('🎯 准备显示数据模型详情')
            this.$message.info('数据模型详情功能待实现')
          }
        }
      },

      // 🆕 新增：打开指标组合分析并显示流程图
      async openIndicatorCombinationWithFlowChart(combinationId, modelInfo) {
        try {
          console.log('🚀 开始打开指标组合分析流程图:', {
            combinationId,
            modelInfo,
          })

          // 1. 先打开指标组合分析对话框
          this.indicatorCombinationDialogVisible = true

          // 2. 等待对话框完全打开
          await this.$nextTick()

          // 3. 延迟一段时间确保组件完全初始化
          setTimeout(() => {
            // 4. 通过事件总线或直接调用子组件方法来显示特定组合的流程图
            this.triggerFlowChartDisplay(combinationId, modelInfo)
          }, 1000)
        } catch (error) {
          console.error('❌ 打开指标组合分析流程图失败:', error)
          this.$message.error('打开流程图失败: ' + error.message)
        }
      },

      // 🆕 新增：触发流程图显示
      triggerFlowChartDisplay(combinationId, modelInfo) {
        console.log('🎯 触发流程图显示:', { combinationId, modelInfo })

        // 直接调用子组件的方法显示特定组合的流程图
        if (this.$refs.indicatorCombinationDialog) {
          console.log('📞 调用子组件方法显示流程图')
          this.$refs.indicatorCombinationDialog.showSpecificCombinationFlowChart(
            combinationId,
            modelInfo
          )
        } else {
          console.warn('⚠️ 指标组合分析对话框组件未找到')

          // 备用方案：通过事件总线通知
          this.$eventBus &&
            this.$eventBus.$emit('showCombinationFlowChart', {
              combinationId,
              modelInfo,
            })
        }
      },

      // 刷新数据
      handleRefreshData() {
        this.loadStatistics()
      },

      // 🔥 新增：加载数据模型列表
      async loadModelList() {
        try {
          this.modelListLoading = true
          console.log('📊 开始加载数据模型列表...')
          console.log('📊 请求参数:', this.modelQueryForm)

          const response = await getDataModelList(this.modelQueryForm)
          console.log('📊 数据模型API完整响应:', response)
          console.log('📊 response.code:', response.code)
          console.log('📊 response.data:', response.data)

          // ✅ 修复：从 response.data 中获取分页数据
          if (response.code === 1 && response.data) {
            this.modelList = response.data.records || []
            this.modelTotal = response.data.total || 0
            console.log('✅ 数据模型列表加载成功:', {
              列表长度: this.modelList.length,
              总记录数: this.modelTotal,
              第一条数据: this.modelList[0]
            })
          } else {
            console.warn('⚠️ 数据模型响应格式异常:', response)
            this.modelList = []
            this.modelTotal = 0
          }
        } catch (error) {
          console.error('❌ 加载数据模型列表异常:', error)
          this.modelList = []
          this.modelTotal = 0
          this.$message.error('加载数据模型列表失败')
        } finally {
          this.modelListLoading = false
        }
      },

      // 🔥 新增：刷新当前列表
      refreshCurrentList() {
        if (this.activeTab === 'dataModel') {
          this.refreshModelList()
        } else if (this.activeTab === 'combination') {
          this.refreshCombinationList()
        }
      },

      // 🔥 新增：Tab切换处理
      handleTabClick(tab) {
        console.log('🔄 切换Tab:', tab.name)
        // Tab切换时可以做一些额外的处理
      },

      // 🔥 新增：刷新数据模型列表
      refreshModelList() {
        console.log('🔄 刷新数据模型列表...')
        this.loadModelList()
      },

      // 🔥 新增：搜索数据模型
      searchModelList() {
        console.log('🔍 搜索数据模型...', this.modelQueryForm)
        this.modelQueryForm.pageNum = 1
        this.loadModelList()
      },

      // 🔥 新增：重置搜索条件
      resetModelSearch() {
        console.log('🔄 重置搜索条件...')
        this.modelQueryForm = {
          pageNum: 1,
          pageSize: 20,
          modelName: '',
          modelType: '',
          status: '',
          isEnabled: '',
        }
        this.loadModelList()
      },

      // 🔥 新增：处理分页大小变化
      handleModelPageSizeChange(pageSize) {
        console.log('📄 数据模型分页大小变化:', pageSize)
        this.modelQueryForm.pageSize = pageSize
        this.modelQueryForm.pageNum = 1
        this.loadModelList()
      },

      // 🔥 新增：处理页码变化
      handleModelPageChange(pageNum) {
        console.log('📄 数据模型页码变化:', pageNum)
        this.modelQueryForm.pageNum = pageNum
        this.loadModelList()
      },

      // 🔥 新增：处理模型选择变化
      handleModelSelectionChange(selection) {
        this.selectedModels = selection
        console.log('✅ 选中的模型:', this.selectedModels.length)
      },

      // 🔥 新增：处理模型状态变化
      async handleModelStatusChange(model) {
        try {
          console.log('🔄 切换模型状态:', model.modelName, model.isEnabled)

          const response = await saveDataModel({
            ...model,
            isEnabled: model.isEnabled,
          })

          if (response && response.code === 1) {
            this.$message.success(
              `模型${model.isEnabled === 'Y' ? '启用' : '禁用'}成功`
            )
          } else {
            // 如果失败，恢复原状态
            model.isEnabled = model.isEnabled === 'Y' ? 'N' : 'Y'
            this.$message.error(`状态切换失败: ${response?.msg || '未知错误'}`)
          }
        } catch (error) {
          console.error('❌ 切换模型状态失败:', error)
          // 如果失败，恢复原状态
          model.isEnabled = model.isEnabled === 'Y' ? 'N' : 'Y'
          this.$message.error('状态切换失败')
        }
      },

      // 🔥 新增：获取模型类型标签类型
      getModelTypeTagType(modelType) {
        const typeMap = {
          FINANCIAL: 'success',
          RISK: 'warning',
          AUDIT: 'info',
        }
        return typeMap[modelType] || 'info'
      },

      // 🔥 新增：获取模型类型文本
      getModelTypeText(modelType) {
        const typeMap = {
          FINANCIAL: '财务模型',
          RISK: '风险模型',
          AUDIT: '审计模型',
        }
        return typeMap[modelType] || modelType
      },

      // 🔥 新增：获取状态标签类型
      getStatusTagType(status) {
        const statusMap = {
          DRAFT: 'info',
          PUBLISHED: 'success',
          ARCHIVED: 'warning',
        }
        return statusMap[status] || 'info'
      },

      // 🔥 新增：获取状态文本
      getStatusText(status) {
        const statusMap = {
          DRAFT: '草稿',
          PUBLISHED: '已发布',
          ARCHIVED: '已归档',
        }
        return statusMap[status] || status
      },

      // ==================== 指标组合列表相关方法 ====================

      // 🔥 新增：加载指标组合列表
      async loadCombinationList() {
        try {
          this.combinationListLoading = true
          console.log('📊 开始加载指标组合列表...')

          const response = await getCombinationList(this.combinationQueryForm)
          console.log('📊 API响应数据:', response)

          // ✅ 修复：从 response.data 中获取分页数据
          if (response.code === 1 && response.data) {
            this.combinationList = response.data.records || []
            this.combinationTotal = response.data.total || 0
          } else {
            this.combinationList = []
            this.combinationTotal = 0
          }
        } catch (error) {
          console.error('❌ 加载异常:', error)
          this.combinationList = []
          this.combinationTotal = 0
          this.$message.error('加载指标组合列表失败')
        } finally {
          this.combinationListLoading = false
        }
      },

      // 🔥 新增：刷新指标组合列表
      refreshCombinationList() {
        console.log('🔄 刷新指标组合列表...')
        this.loadCombinationList()
      },

      // 🔥 新增：搜索指标组合
      searchCombinationList() {
        console.log('🔍 搜索指标组合...', this.combinationQueryForm)
        this.combinationQueryForm.pageNum = 1
        this.loadCombinationList()
      },

      // 🔥 新增：重置指标组合搜索条件
      resetCombinationSearch() {
        console.log('🔄 重置指标组合搜索条件...')
        this.combinationQueryForm = {
          pageNum: 1,
          pageSize: 20,
          combinationName: '',
          category: '',
          status: '',
          createUser: '',
        }
        this.loadCombinationList()
      },

      // 🔥 新增：处理指标组合分页大小变化
      handleCombinationPageSizeChange(pageSize) {
        console.log('📄 指标组合分页大小变化:', pageSize)
        this.combinationQueryForm.pageSize = pageSize
        this.combinationQueryForm.pageNum = 1
        this.loadCombinationList()
      },

      // 🔥 新增：处理指标组合页码变化
      handleCombinationPageChange(pageNum) {
        console.log('📄 指标组合页码变化:', pageNum)
        this.combinationQueryForm.pageNum = pageNum
        this.loadCombinationList()
      },

      // 🔥 新增：处理指标组合选择变化
      handleCombinationSelectionChange(selection) {
        this.selectedCombinations = selection
        console.log('✅ 选中的指标组合:', this.selectedCombinations.length)
      },

      // 🔥 新增：获取业务分类标签类型
      getCombinationCategoryTagType(category) {
        const typeMap = {
          PROCUREMENT: 'primary',
          FINANCE: 'success',
          COMPLIANCE: 'warning',
          RISK: 'danger',
        }
        return typeMap[category] || 'info'
      },

      // 🔥 新增：获取业务分类文本
      getCombinationCategoryText(category) {
        const typeMap = {
          PROCUREMENT: '采购管理',
          FINANCE: '财务管理',
          COMPLIANCE: '合规管理',
          RISK: '风险管理',
        }
        return typeMap[category] || category
      },

      // 处理打开SQL模板管理
      handleOpenSqlTemplateManagement(templateId) {
        // 关闭数据模型管理对话框
        this.dataModelDialogVisible = false

        // 打开SQL模板管理对话框
        this.sqlTemplateDialogVisible = true

        // 可以在这里添加定位到特定模板的逻辑
        if (templateId) {
          this.$nextTick(() => {
            // 通知SQL模板对话框定位到指定模板
            this.$refs.sqlTemplateDialog &&
              this.$refs.sqlTemplateDialog.locateTemplate(templateId)
          })
        }
      },
    },
  }
</script>

<style scoped>
  .data-model-container {
    padding: 20px;
  }

  .page-header {
    margin-bottom: 30px;
  }

  .page-header h2 {
    margin: 0 0 10px 0;
    color: #303133;
    font-weight: 600;
  }

  .page-header p {
    margin: 0;
    color: #909399;
    font-size: 14px;
  }

  .function-cards {
    margin-bottom: 30px;
  }

  .function-card {
    cursor: pointer;
    transition: all 0.3s ease;
    height: 240px; /* 增加高度从180px到240px */
  }

  .function-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  }

  .card-content {
    text-align: center;
    padding: 25px 15px; /* 增加内边距 */
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between; /* 内容均匀分布 */
  }

  .card-icon {
    font-size: 52px; /* 稍微增大图标 */
    color: #409eff;
    margin-bottom: 18px;
    flex-shrink: 0; /* 防止图标被压缩 */
  }

  .card-content h3 {
    margin: 0 0 12px 0;
    color: #303133;
    font-size: 19px; /* 稍微增大标题字体 */
    font-weight: 600;
    flex-shrink: 0; /* 防止标题被压缩 */
  }

  .card-content p {
    margin: 0 0 18px 0;
    color: #606266;
    font-size: 14px;
    line-height: 1.6; /* 增加行高提高可读性 */
    flex-grow: 1; /* 描述文字占据剩余空间 */
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .card-stats {
    display: flex;
    justify-content: space-around;
    font-size: 13px; /* 稍微增大统计字体 */
    color: #909399;
    flex-shrink: 0; /* 防止统计信息被压缩 */
    padding-top: 8px;
    border-top: 1px solid #f0f0f0; /* 添加分隔线 */
  }

  .quick-stats {
    margin-bottom: 30px;
  }

  .stat-item {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    text-align: center;
    border: 1px solid #e4e7ed;
    transition: all 0.3s ease;
  }

  .stat-item:hover {
    border-color: #409eff;
    box-shadow: 0 2px 12px rgba(64, 158, 255, 0.1);
  }

  .stat-number {
    font-size: 32px;
    font-weight: bold;
    color: #409eff;
    margin-bottom: 8px;
  }

  .stat-label {
    font-size: 14px;
    color: #909399;
  }

  /* 数据模型列表样式 */
  .data-model-list {
    margin-bottom: 30px;
  }

  .data-model-list .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: 600;
    color: #303133;
  }

  .data-model-list .header-actions {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .data-model-list .search-area {
    margin-bottom: 16px;
    padding: 16px;
    background-color: #f8f9fa;
    border-radius: 4px;
  }

  .data-model-list .search-area .el-form-item {
    margin-bottom: 0;
    margin-right: 16px;
  }

  .data-model-list .pagination-container {
    display: flex;
    justify-content: center;
    padding: 20px 0;
    border-top: 1px solid #ebeef5;
    margin-top: 16px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  /* 响应式设计 */
  @media (max-width: 768px) {
    .function-cards .el-col {
      margin-bottom: 20px;
    }

    .function-card {
      height: 220px; /* 移动端稍微减小高度 */
    }

    .card-content {
      padding: 20px 12px; /* 移动端减小内边距 */
    }

    .card-icon {
      font-size: 44px; /* 移动端减小图标 */
      margin-bottom: 15px;
    }

    .card-content h3 {
      font-size: 17px; /* 移动端减小标题字体 */
    }

    .card-content p {
      font-size: 13px; /* 移动端减小描述字体 */
      line-height: 1.5;
    }

    .card-stats {
      font-size: 12px; /* 移动端减小统计字体 */
    }

    .quick-stats .el-col {
      margin-bottom: 15px;
    }

    .stat-number {
      font-size: 24px;
    }
  }
</style>
