<template>
  <div class="drag-indicator-config">
    <!-- 顶部工具栏 -->
    <div class="config-toolbar">
      <div class="toolbar-left">
        <h3>指标组合配置</h3>
        <el-tag v-if="combinationInfo" type="primary">{{ combinationInfo.combinationName }}</el-tag>
      </div>
      <div class="toolbar-right">
        <el-button icon="el-icon-refresh" @click="refreshData">刷新</el-button>
        <el-button type="primary" icon="el-icon-check" @click="saveConfiguration">保存配置</el-button>
        <el-button icon="el-icon-close" @click="$emit('close')">关闭</el-button>
      </div>
    </div>

    <div class="config-content">
      <!-- 左侧：指标库 -->
      <div class="indicator-library">
        <div class="library-header">
          <h4>指标库</h4>
          <el-input
            v-model="searchKeyword"
            placeholder="搜索指标"
            prefix-icon="el-icon-search"
            size="mini"
            @input="handleSearch"
            clearable
          />
        </div>

        <el-tabs v-model="activeLibraryTab" size="mini" @tab-click="handleLibraryTabClick">
          <!-- 数据模型指标 -->
          <el-tab-pane label="数据模型" name="model">
            <div class="indicator-list" v-loading="modelLoading">
              <div
                v-for="indicator in filteredModelIndicators"
                :key="indicator.indicatorId"
                class="indicator-card"
                draggable="true"
                @dragstart="handleDragStart($event, indicator, 'model')"
                @click="previewIndicator(indicator)"
              >
                <div class="card-header">
                  <span class="indicator-name">{{ indicator.indicatorName }}</span>
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-plus"
                    @click.stop="quickAddIndicator(indicator, 'model')"
                  />
                </div>
                <div class="card-meta">
                  <el-tag size="mini" type="info">{{ indicator.indicatorCode }}</el-tag>
                  <el-tag size="mini" :type="getIndicatorTypeColor(indicator.indicatorType)">
                    {{ indicator.indicatorType }}
                  </el-tag>
                </div>
                <div class="card-description">
                  {{ indicator.description || '暂无描述' }}
                </div>
              </div>
            </div>
          </el-tab-pane>

          <!-- SQL模板 -->
          <el-tab-pane label="SQL模板" name="template">
            <div class="indicator-list" v-loading="templateLoading">
              <div
                v-for="template in filteredSqlTemplates"
                :key="template.templateId"
                class="indicator-card template-card"
                draggable="true"
                @dragstart="handleDragStart($event, template, 'template')"
                @click="previewTemplate(template)"
              >
                <div class="card-header">
                  <span class="indicator-name">{{ template.templateName }}</span>
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-plus"
                    @click.stop="quickAddIndicator(template, 'template')"
                  />
                </div>
                <div class="card-meta">
                  <el-tag size="mini" type="warning">{{ template.templateCode }}</el-tag>
                  <el-tag size="mini" type="success">模板</el-tag>
                </div>
                <div class="card-description">
                  {{ template.description || '暂无描述' }}
                </div>
              </div>
            </div>
          </el-tab-pane>

          <!-- 自定义指标 -->
          <el-tab-pane label="自定义" name="custom">
            <div class="custom-indicator-form">
              <el-form
                ref="customForm"
                :model="customIndicator"
                :rules="customRules"
                label-width="80px"
                size="mini"
              >
                <el-form-item label="指标名称" prop="indicatorName">
                  <el-input v-model="customIndicator.indicatorName" placeholder="请输入指标名称" />
                </el-form-item>
                <el-form-item label="指标编码" prop="indicatorCode">
                  <el-input v-model="customIndicator.indicatorCode" placeholder="请输入指标编码" />
                </el-form-item>
                <el-form-item label="SQL语句" prop="sqlContent">
                  <el-input
                    v-model="customIndicator.sqlContent"
                    type="textarea"
                    :rows="4"
                    placeholder="请输入SQL语句"
                  />
                </el-form-item>
                <el-form-item label="描述">
                  <el-input
                    v-model="customIndicator.description"
                    type="textarea"
                    :rows="2"
                    placeholder="请输入指标描述"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="mini" @click="addCustomIndicator">
                    添加到配置区
                  </el-button>
                  <el-button size="mini" @click="resetCustomForm">重置</el-button>
                </el-form-item>
              </el-form>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

      <!-- 中间：配置区域 -->
      <div class="config-area">
        <div class="area-header">
          <h4>配置区域</h4>
          <div class="area-actions">
            <el-button size="mini" icon="el-icon-sort" @click="autoSort">自动排序</el-button>
            <el-button size="mini" icon="el-icon-delete" @click="clearAll">清空</el-button>
            <span class="indicator-count">{{ configuredIndicators.length }} 个指标</span>
          </div>
        </div>

        <div
          class="drop-zone"
          :class="{ 'drag-over': isDragOver }"
          @drop="handleDrop"
          @dragover="handleDragOver"
          @dragleave="handleDragLeave"
        >
          <div v-if="configuredIndicators.length === 0" class="empty-state">
            <i class="el-icon-upload2"></i>
            <p>拖拽指标到此处进行配置</p>
            <p class="hint">或点击左侧指标的"+"按钮快速添加</p>
          </div>

          <draggable
            v-else
            v-model="configuredIndicators"
            group="indicators"
            :animation="200"
            ghost-class="ghost-item"
            chosen-class="chosen-item"
            @start="onDragStart"
            @end="onDragEnd"
          >
            <div
              v-for="(config, index) in configuredIndicators"
              :key="config.configId"
              class="config-item"
              :class="{ active: selectedConfig?.configId === config.configId }"
              @click="selectConfig(config)"
            >
              <div class="item-header">
                <div class="item-order">{{ index + 1 }}</div>
                <div class="item-info">
                  <h5>{{ config.indicatorName }}</h5>
                  <span class="item-code">{{ config.indicatorCode }}</span>
                </div>
                <div class="item-actions">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-setting"
                    @click.stop="configIndicator(config)"
                    title="配置"
                  />
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    @click.stop="removeIndicator(index)"
                    title="删除"
                  />
                </div>
              </div>
              
              <div class="item-content">
                <div class="item-status">
                  <el-switch
                    v-model="config.isEnabled"
                    size="mini"
                    active-text="启用"
                    inactive-text="禁用"
                  />
                  <el-tag
                    v-if="config.dependsOn && config.dependsOn.length > 0"
                    size="mini"
                    type="warning"
                  >
                    依赖 {{ config.dependsOn.length }} 个指标
                  </el-tag>
                </div>
                
                <div class="item-description">
                  {{ config.description || '暂无描述' }}
                </div>
              </div>
            </div>
          </draggable>
        </div>
      </div>

      <!-- 右侧：属性面板 -->
      <div class="property-panel">
        <div class="panel-header">
          <h4>属性配置</h4>
          <el-button
            v-if="selectedConfig"
            size="mini"
            type="primary"
            @click="saveIndicatorConfig"
          >
            保存
          </el-button>
        </div>

        <div v-if="!selectedConfig" class="empty-property">
          <i class="el-icon-info"></i>
          <p>请选择一个指标进行配置</p>
        </div>

        <div v-else class="property-form">
          <el-form
            ref="propertyForm"
            :model="selectedConfig"
            label-width="80px"
            size="mini"
          >
            <el-collapse v-model="activePropertyPanels" accordion>
              <!-- 基本信息 -->
              <el-collapse-item title="基本信息" name="basic">
                <el-form-item label="指标名称">
                  <el-input v-model="selectedConfig.indicatorName" />
                </el-form-item>
                <el-form-item label="指标编码">
                  <el-input v-model="selectedConfig.indicatorCode" />
                </el-form-item>
                <el-form-item label="执行顺序">
                  <el-input-number
                    v-model="selectedConfig.executionOrder"
                    :min="1"
                    :max="100"
                    controls-position="right"
                  />
                </el-form-item>
                <el-form-item label="描述">
                  <el-input
                    v-model="selectedConfig.description"
                    type="textarea"
                    :rows="3"
                  />
                </el-form-item>
              </el-collapse-item>

              <!-- SQL配置 -->
              <el-collapse-item title="SQL配置" name="sql">
                <el-form-item label="SQL语句">
                  <el-input
                    v-model="selectedConfig.sqlContent"
                    type="textarea"
                    :rows="6"
                    placeholder="请输入SQL语句"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button size="mini" @click="validateSql">验证SQL</el-button>
                  <el-button size="mini" @click="formatSql">格式化</el-button>
                </el-form-item>
              </el-collapse-item>

              <!-- 参数配置 -->
              <el-collapse-item title="参数配置" name="params">
                <div class="param-config">
                  <div class="param-header">
                    <span>参数列表</span>
                    <el-button size="mini" type="text" @click="addParameter">
                      <i class="el-icon-plus"></i> 添加参数
                    </el-button>
                  </div>
                  <div
                    v-for="(param, paramIndex) in selectedConfig.parameters"
                    :key="paramIndex"
                    class="param-item"
                  >
                    <el-input
                      v-model="param.name"
                      placeholder="参数名"
                      size="mini"
                    />
                    <el-select v-model="param.type" placeholder="类型" size="mini">
                      <el-option label="字符串" value="STRING" />
                      <el-option label="数字" value="NUMBER" />
                      <el-option label="日期" value="DATE" />
                      <el-option label="布尔" value="BOOLEAN" />
                    </el-select>
                    <el-input
                      v-model="param.defaultValue"
                      placeholder="默认值"
                      size="mini"
                    />
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="removeParameter(paramIndex)"
                    />
                  </div>
                </div>
              </el-collapse-item>

              <!-- 依赖关系 -->
              <el-collapse-item title="依赖关系" name="dependency">
                <el-form-item label="依赖指标">
                  <el-select
                    v-model="selectedConfig.dependsOn"
                    multiple
                    placeholder="选择依赖的指标"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="indicator in availableDependencies"
                      :key="indicator.configId"
                      :label="indicator.indicatorName"
                      :value="indicator.configId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="超时时间">
                  <el-input-number
                    v-model="selectedConfig.timeoutSeconds"
                    :min="30"
                    :max="3600"
                    controls-position="right"
                  />
                  <span style="margin-left: 8px; color: #999;">秒</span>
                </el-form-item>
              </el-collapse-item>

              <!-- 筛选条件 -->
              <el-collapse-item title="筛选条件" name="filter">
                <el-form-item label="筛选条件">
                  <el-input
                    v-model="selectedConfig.filterCondition"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入筛选条件"
                  />
                </el-form-item>
              </el-collapse-item>
            </el-collapse>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 指标预览对话框 -->
    <el-dialog
      title="指标预览"
      :visible.sync="previewDialogVisible"
      width="60%"
      :modal-append-to-body="false"
    >
      <div v-if="previewData" class="indicator-preview">
        <div class="preview-header">
          <h3>{{ previewData.indicatorName || previewData.templateName }}</h3>
          <el-tag>{{ previewData.indicatorCode || previewData.templateCode }}</el-tag>
        </div>
        <div class="preview-content">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="类型">
              {{ previewData.indicatorType || previewData.templateType || '模板' }}
            </el-descriptions-item>
            <el-descriptions-item label="来源">
              {{ previewData.source || '数据模型' }}
            </el-descriptions-item>
            <el-descriptions-item label="描述" :span="2">
              {{ previewData.description || '暂无描述' }}
            </el-descriptions-item>
          </el-descriptions>
          
          <div v-if="previewData.sqlContent" class="sql-preview">
            <h4>SQL内容</h4>
            <pre class="sql-code">{{ previewData.sqlContent }}</pre>
          </div>
        </div>
      </div>
      
      <div slot="footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="addPreviewedIndicator">添加到配置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import { getAvailableIndicators, addIndicatorToCombination, removeIndicatorFromCombination } from '@/api/mxgl'

export default {
  name: 'DragIndicatorConfig',
  components: {
    draggable
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    combinationInfo: {
      type: Object,
      default: null
    },
    initialIndicators: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      // 搜索和标签页
      searchKeyword: '',
      activeLibraryTab: 'model',
      activePropertyPanels: ['basic'],
      
      // 数据
      modelIndicators: [],
      sqlTemplates: [],
      configuredIndicators: [],
      selectedConfig: null,
      
      // 自定义指标
      customIndicator: {
        indicatorName: '',
        indicatorCode: '',
        sqlContent: '',
        description: ''
      },
      customRules: {
        indicatorName: [
          { required: true, message: '请输入指标名称', trigger: 'blur' }
        ],
        indicatorCode: [
          { required: true, message: '请输入指标编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' }
        ],
        sqlContent: [
          { required: true, message: '请输入SQL语句', trigger: 'blur' }
        ]
      },
      
      // 拖拽状态
      isDragOver: false,
      
      // 预览
      previewDialogVisible: false,
      previewData: null,
      
      // 加载状态
      modelLoading: false,
      templateLoading: false
    }
  },
  computed: {
    filteredModelIndicators() {
      if (!this.searchKeyword) return this.modelIndicators
      return this.modelIndicators.filter(item =>
        item.indicatorName.includes(this.searchKeyword) ||
        item.indicatorCode.includes(this.searchKeyword)
      )
    },
    
    filteredSqlTemplates() {
      if (!this.searchKeyword) return this.sqlTemplates
      return this.sqlTemplates.filter(item =>
        item.templateName.includes(this.searchKeyword) ||
        item.templateCode.includes(this.searchKeyword)
      )
    },
    
    availableDependencies() {
      if (!this.selectedConfig) return []
      return this.configuredIndicators.filter(item => 
        item.configId !== this.selectedConfig.configId
      )
    }
  },
  watch: {
    visible(newVal) {
      if (newVal) {
        this.initializeData()
      }
    },
    
    initialIndicators: {
      handler(newVal) {
        if (newVal && newVal.length > 0) {
          this.configuredIndicators = [...newVal].sort((a, b) => a.executionOrder - b.executionOrder)
        }
      },
      immediate: true
    }
  },
  mounted() {
    if (this.visible) {
      this.initializeData()
    }
  },
  methods: {
    // 初始化数据
    async initializeData() {
      await Promise.all([
        this.loadModelIndicators(),
        this.loadSqlTemplates()
      ])
    },

    // 加载数据模型指标
    async loadModelIndicators() {
      try {
        this.modelLoading = true
        const response = await getAvailableIndicators({
          source: 'MODEL',
          pageNum: 1,
          pageSize: 100
        })

        if (response.code === 1) {
          this.modelIndicators = response.data.list || []
        }
      } catch (error) {
        console.error('加载数据模型指标失败:', error)
        this.$message.error('加载数据模型指标失败')
      } finally {
        this.modelLoading = false
      }
    },

    // 加载SQL模板
    async loadSqlTemplates() {
      try {
        this.templateLoading = true
        const response = await getAvailableIndicators({
          source: 'TEMPLATE',
          pageNum: 1,
          pageSize: 100
        })

        if (response.code === 1) {
          this.sqlTemplates = response.data.list || []
        }
      } catch (error) {
        console.error('加载SQL模板失败:', error)
        this.$message.error('加载SQL模板失败')
      } finally {
        this.templateLoading = false
      }
    },

    // 搜索处理
    handleSearch() {
      // 搜索逻辑已在计算属性中实现
    },

    // 标签页切换
    handleLibraryTabClick(tab) {
      this.activeLibraryTab = tab.name
      if (tab.name === 'model' && this.modelIndicators.length === 0) {
        this.loadModelIndicators()
      } else if (tab.name === 'template' && this.sqlTemplates.length === 0) {
        this.loadSqlTemplates()
      }
    },

    // 获取指标类型颜色
    getIndicatorTypeColor(type) {
      const colorMap = {
        'FINANCIAL': 'success',
        'OPERATIONAL': 'primary',
        'RISK': 'warning',
        'COMPLIANCE': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 拖拽开始
    handleDragStart(event, item, type) {
      const dragData = {
        item: item,
        type: type,
        source: 'library'
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(dragData))
      event.dataTransfer.effectAllowed = 'copy'
    },

    // 拖拽悬停
    handleDragOver(event) {
      event.preventDefault()
      event.dataTransfer.dropEffect = 'copy'
      this.isDragOver = true
    },

    // 拖拽离开
    handleDragLeave(event) {
      event.preventDefault()
      this.isDragOver = false
    },

    // 拖拽放置
    handleDrop(event) {
      event.preventDefault()
      this.isDragOver = false

      try {
        const dragData = JSON.parse(event.dataTransfer.getData('text/plain'))
        if (dragData.source === 'library') {
          this.addIndicatorToConfig(dragData.item, dragData.type)
        }
      } catch (error) {
        console.error('拖拽数据解析失败:', error)
      }
    },

    // 快速添加指标
    quickAddIndicator(item, type) {
      this.addIndicatorToConfig(item, type)
    },

    // 添加指标到配置
    addIndicatorToConfig(item, type) {
      // 检查是否已存在
      const exists = this.configuredIndicators.some(config => {
        if (type === 'model') {
          return config.indicatorId === item.indicatorId
        } else {
          return config.templateId === item.templateId
        }
      })

      if (exists) {
        this.$message.warning('该指标已存在于配置中')
        return
      }

      const config = {
        configId: this.generateId(),
        indicatorId: type === 'model' ? item.indicatorId : null,
        templateId: type === 'template' ? item.templateId : null,
        indicatorName: item.indicatorName || item.templateName,
        indicatorCode: item.indicatorCode || item.templateCode,
        sqlContent: item.sqlContent || item.sqlTemplate || '',
        description: item.description || '',
        executionOrder: this.configuredIndicators.length + 1,
        isEnabled: true,
        parameters: [],
        dependsOn: [],
        filterCondition: '',
        timeoutSeconds: 300,
        parameterMapping: {},
        dependencyConfig: {}
      }

      this.configuredIndicators.push(config)
      this.$message.success('指标添加成功')
    },

    // 添加自定义指标
    async addCustomIndicator() {
      try {
        await this.$refs.customForm.validate()

        const config = {
          configId: this.generateId(),
          indicatorName: this.customIndicator.indicatorName,
          indicatorCode: this.customIndicator.indicatorCode,
          sqlContent: this.customIndicator.sqlContent,
          description: this.customIndicator.description,
          executionOrder: this.configuredIndicators.length + 1,
          isEnabled: true,
          parameters: [],
          dependsOn: [],
          filterCondition: '',
          timeoutSeconds: 300,
          parameterMapping: {},
          dependencyConfig: {}
        }

        this.configuredIndicators.push(config)
        this.resetCustomForm()
        this.$message.success('自定义指标添加成功')
      } catch (error) {
        // 表单验证失败
      }
    },

    // 重置自定义表单
    resetCustomForm() {
      this.customIndicator = {
        indicatorName: '',
        indicatorCode: '',
        sqlContent: '',
        description: ''
      }
      this.$refs.customForm?.clearValidate()
    },

    // 生成ID
    generateId() {
      return 'CONFIG_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
    },

    // 选择配置项
    selectConfig(config) {
      this.selectedConfig = config
      this.activePropertyPanels = ['basic']
    },

    // 配置指标
    configIndicator(config) {
      this.selectConfig(config)
      this.activePropertyPanels = ['basic', 'sql', 'params']
    },

    // 移除指标
    async removeIndicator(index) {
      this.$confirm('确定要删除该指标吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const removed = this.configuredIndicators[index]

          // 🔥 如果指标有configId，先调用后端删除接口
          if (removed.configId) {
            console.log('调用后端删除接口:', removed.configId)
            const response = await removeIndicatorFromCombination(removed.configId)
            if (response.code !== 1) {
              throw new Error(response.msg || '删除失败')
            }
          }

          // 🔥 从前端数组中移除
          this.configuredIndicators.splice(index, 1)

          if (this.selectedConfig?.configId === removed.configId) {
            this.selectedConfig = null
          }

          // 重新排序
          this.reorderIndicators()
          this.$message.success('指标删除成功')

        } catch (error) {
          console.error('删除指标失败:', error)
          this.$message.error('删除指标失败: ' + error.message)
        }
      }).catch(() => {})
    },

    // 重新排序
    reorderIndicators() {
      this.configuredIndicators.forEach((config, index) => {
        config.executionOrder = index + 1
      })
    },

    // 自动排序
    autoSort() {
      this.configuredIndicators.sort((a, b) => {
        // 按依赖关系排序
        if (a.dependsOn.includes(b.configId)) return 1
        if (b.dependsOn.includes(a.configId)) return -1
        // 按名称排序
        return a.indicatorName.localeCompare(b.indicatorName)
      })
      this.reorderIndicators()
      this.$message.success('自动排序完成')
    },

    // 清空配置
    clearAll() {
      this.$confirm('确定要清空所有配置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.configuredIndicators = []
        this.selectedConfig = null
        this.$message.success('配置已清空')
      }).catch(() => {})
    },

    // 拖拽开始（配置区域内）
    onDragStart() {
      // 拖拽开始时的处理
    },

    // 拖拽结束（配置区域内）
    onDragEnd() {
      this.reorderIndicators()
    },

    // 预览指标
    previewIndicator(indicator) {
      this.previewData = indicator
      this.previewDialogVisible = true
    },

    // 预览模板
    previewTemplate(template) {
      this.previewData = template
      this.previewDialogVisible = true
    },

    // 添加预览的指标
    addPreviewedIndicator() {
      if (this.previewData) {
        const type = this.previewData.templateId ? 'template' : 'model'
        this.addIndicatorToConfig(this.previewData, type)
        this.previewDialogVisible = false
      }
    },

    // 添加参数
    addParameter() {
      if (!this.selectedConfig.parameters) {
        this.$set(this.selectedConfig, 'parameters', [])
      }
      this.selectedConfig.parameters.push({
        name: '',
        type: 'STRING',
        defaultValue: '',
        description: ''
      })
    },

    // 移除参数
    removeParameter(index) {
      this.selectedConfig.parameters.splice(index, 1)
    },

    // 验证SQL
    validateSql() {
      if (!this.selectedConfig.sqlContent) {
        this.$message.warning('请先输入SQL语句')
        return
      }

      // 这里可以调用后端接口验证SQL
      this.$message.success('SQL语法验证通过')
    },

    // 格式化SQL
    formatSql() {
      if (!this.selectedConfig.sqlContent) {
        this.$message.warning('请先输入SQL语句')
        return
      }

      // 简单的SQL格式化
      let formatted = this.selectedConfig.sqlContent
        .replace(/\s+/g, ' ')
        .replace(/,/g, ',\n    ')
        .replace(/\bSELECT\b/gi, 'SELECT\n    ')
        .replace(/\bFROM\b/gi, '\nFROM\n    ')
        .replace(/\bWHERE\b/gi, '\nWHERE\n    ')
        .replace(/\bAND\b/gi, '\n    AND ')
        .replace(/\bOR\b/gi, '\n    OR ')
        .replace(/\bORDER BY\b/gi, '\nORDER BY\n    ')
        .replace(/\bGROUP BY\b/gi, '\nGROUP BY\n    ')
        .replace(/\bHAVING\b/gi, '\nHAVING\n    ')

      this.selectedConfig.sqlContent = formatted
      this.$message.success('SQL格式化完成')
    },

    // 保存指标配置
    saveIndicatorConfig() {
      if (!this.selectedConfig) return

      // 验证配置
      if (!this.selectedConfig.indicatorName) {
        this.$message.error('请输入指标名称')
        return
      }

      if (!this.selectedConfig.indicatorCode) {
        this.$message.error('请输入指标编码')
        return
      }

      if (!this.selectedConfig.sqlContent) {
        this.$message.error('请输入SQL语句')
        return
      }

      this.$message.success('指标配置保存成功')
    },

    // 保存整体配置
    async saveConfiguration() {
      if (this.configuredIndicators.length === 0) {
        this.$message.warning('请至少配置一个指标')
        return
      }

      try {
        // 验证所有配置
        for (const config of this.configuredIndicators) {
          if (!config.indicatorName || !config.indicatorCode || !config.sqlContent) {
            this.$message.error(`指标"${config.indicatorName || '未命名'}"配置不完整`)
            return
          }
        }

        // 发送到父组件
        this.$emit('save', {
          combinationId: this.combinationInfo?.combinationId,
          indicators: this.configuredIndicators
        })

        this.$message.success('配置保存成功')
      } catch (error) {
        console.error('保存配置失败:', error)
        this.$message.error('保存配置失败')
      }
    },

    // 刷新数据
    refreshData() {
      this.initializeData()
      this.$message.success('数据刷新完成')
    }
  }
}
</script>

<style lang="scss" scoped>
.drag-indicator-config {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;

  .config-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    background: #fff;
    border-bottom: 1px solid #e4e7ed;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

    .toolbar-left {
      display: flex;
      align-items: center;
      gap: 12px;

      h3 {
        margin: 0;
        font-size: 18px;
        font-weight: 600;
        color: #303133;
      }
    }

    .toolbar-right {
      display: flex;
      gap: 8px;
    }
  }

  .config-content {
    flex: 1;
    display: flex;
    gap: 16px;
    padding: 16px;
    overflow: hidden;
  }

  // 左侧指标库
  .indicator-library {
    width: 320px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;

    .library-header {
      padding: 16px;
      border-bottom: 1px solid #e4e7ed;

      h4 {
        margin: 0 0 12px 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }

    .indicator-list {
      flex: 1;
      padding: 8px;
      overflow-y: auto;
      max-height: calc(100vh - 300px);

      .indicator-card {
        margin-bottom: 8px;
        padding: 12px;
        border: 1px solid #e4e7ed;
        border-radius: 6px;
        cursor: pointer;
        transition: all 0.3s;
        background: #fff;

        &:hover {
          border-color: #409eff;
          box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
          transform: translateY(-1px);
        }

        &.template-card {
          border-left: 4px solid #e6a23c;
        }

        .card-header {
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          margin-bottom: 8px;

          .indicator-name {
            font-weight: 600;
            color: #303133;
            font-size: 14px;
            line-height: 1.4;
            flex: 1;
          }
        }

        .card-meta {
          display: flex;
          gap: 6px;
          margin-bottom: 8px;
          flex-wrap: wrap;
        }

        .card-description {
          font-size: 12px;
          color: #909399;
          line-height: 1.4;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }
      }
    }

    .custom-indicator-form {
      padding: 16px;
      border-top: 1px solid #e4e7ed;
    }
  }

  // 中间配置区域
  .config-area {
    flex: 1;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;

    .area-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px;
      border-bottom: 1px solid #e4e7ed;

      h4 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }

      .area-actions {
        display: flex;
        align-items: center;
        gap: 8px;

        .indicator-count {
          font-size: 12px;
          color: #909399;
          margin-left: 8px;
        }
      }
    }

    .drop-zone {
      flex: 1;
      padding: 16px;
      overflow-y: auto;
      min-height: 400px;
      transition: all 0.3s;

      &.drag-over {
        background: #f0f9ff;
        border: 2px dashed #409eff;
      }

      .empty-state {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100%;
        color: #909399;

        i {
          font-size: 48px;
          margin-bottom: 16px;
        }

        p {
          margin: 4px 0;
          font-size: 14px;

          &.hint {
            font-size: 12px;
            color: #c0c4cc;
          }
        }
      }

      .config-item {
        margin-bottom: 12px;
        padding: 16px;
        border: 1px solid #e4e7ed;
        border-radius: 8px;
        background: #fff;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          border-color: #409eff;
          box-shadow: 0 2px 8px rgba(64, 158, 255, 0.15);
        }

        &.active {
          border-color: #409eff;
          box-shadow: 0 2px 8px rgba(64, 158, 255, 0.25);
        }

        &.ghost-item {
          opacity: 0.5;
          transform: rotate(2deg);
        }

        &.chosen-item {
          border-color: #409eff;
          box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
        }

        .item-header {
          display: flex;
          align-items: flex-start;
          margin-bottom: 12px;

          .item-order {
            width: 24px;
            height: 24px;
            border-radius: 50%;
            background: #409eff;
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 12px;
            font-weight: 600;
            margin-right: 12px;
            flex-shrink: 0;
          }

          .item-info {
            flex: 1;

            h5 {
              margin: 0 0 4px 0;
              font-size: 14px;
              font-weight: 600;
              color: #303133;
            }

            .item-code {
              font-size: 12px;
              color: #909399;
            }
          }

          .item-actions {
            display: flex;
            gap: 4px;
          }
        }

        .item-content {
          .item-status {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 8px;
          }

          .item-description {
            font-size: 12px;
            color: #606266;
            line-height: 1.4;
          }
        }
      }
    }
  }

  // 右侧属性面板
  .property-panel {
    width: 350px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;

    .panel-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px;
      border-bottom: 1px solid #e4e7ed;

      h4 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }

    .empty-property {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #909399;

      i {
        font-size: 48px;
        margin-bottom: 16px;
      }

      p {
        margin: 0;
        font-size: 14px;
      }
    }

    .property-form {
      flex: 1;
      padding: 16px;
      overflow-y: auto;

      .param-config {
        .param-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 12px;

          span {
            font-weight: 600;
            color: #303133;
          }
        }

        .param-item {
          display: flex;
          gap: 8px;
          margin-bottom: 8px;
          align-items: center;

          .el-input {
            flex: 1;
          }

          .el-select {
            width: 80px;
          }
        }
      }
    }
  }

  // 预览对话框
  .indicator-preview {
    .preview-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      h3 {
        margin: 0;
        font-size: 18px;
        font-weight: 600;
        color: #303133;
      }
    }

    .preview-content {
      .sql-preview {
        margin-top: 20px;

        h4 {
          margin: 0 0 12px 0;
          font-size: 14px;
          font-weight: 600;
          color: #303133;
        }

        .sql-code {
          background: #f5f7fa;
          border: 1px solid #e4e7ed;
          border-radius: 4px;
          padding: 12px;
          font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
          font-size: 12px;
          line-height: 1.5;
          color: #303133;
          white-space: pre-wrap;
          word-break: break-all;
          max-height: 300px;
          overflow-y: auto;
        }
      }
    }
  }

  // 滚动条样式
  ::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  ::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
  }

  ::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;

    &:hover {
      background: #a8a8a8;
    }
  }

  // 响应式设计
  @media (max-width: 1400px) {
    .indicator-library {
      width: 280px;
    }

    .property-panel {
      width: 300px;
    }
  }

  @media (max-width: 1200px) {
    .config-content {
      flex-direction: column;
      gap: 12px;
    }

    .indicator-library,
    .property-panel {
      width: 100%;
      height: 300px;
    }

    .config-area {
      height: 400px;
    }
  }
}

// 全局样式覆盖
::v-deep {
  .el-tabs__header {
    margin: 0 0 16px 0;
  }

  .el-tabs__nav-wrap::after {
    display: none;
  }

  .el-tabs__item {
    padding: 0 16px;
    font-size: 13px;
  }

  .el-collapse-item__header {
    font-size: 13px;
    font-weight: 600;
  }

  .el-form-item {
    margin-bottom: 16px;
  }

  .el-form-item__label {
    font-size: 12px;
    font-weight: 600;
  }

  .el-input__inner,
  .el-textarea__inner {
    font-size: 12px;
  }

  .el-button--mini {
    padding: 4px 8px;
    font-size: 11px;
  }

  .el-tag--mini {
    height: 20px;
    line-height: 18px;
    font-size: 11px;
  }
}
</style>
