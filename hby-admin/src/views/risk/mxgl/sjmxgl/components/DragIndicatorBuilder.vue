<template>
  <div class="drag-indicator-builder">
    <!-- 顶部切换按钮 -->
    <div class="builder-header">
      <div class="mode-toggle">
        <el-radio-group v-model="buildMode" size="mini">
          <el-radio-button label="form">表单模式</el-radio-button>
          <el-radio-button label="drag">拖拽模式</el-radio-button>
        </el-radio-group>
      </div>
      <div class="header-actions">
        <el-button size="mini" @click="previewSQL" :disabled="!canPreview">预览SQL</el-button>
        <el-button size="mini" @click="insertTemplate" v-if="buildMode === 'form'">
          <i class="el-icon-document-add"></i> 模板
        </el-button>
        <el-button size="mini" @click="clearAll">清空</el-button>
      </div>
    </div>

    <!-- 表单模式 -->
    <div v-if="buildMode === 'form'" class="form-mode">
      <el-form
        ref="indicatorForm"
        :model="indicatorData"
        :rules="formRules"
        label-width="100px"
        size="mini"
      >
        <el-form-item label="指标名称" prop="indicatorName">
          <el-input v-model="indicatorData.indicatorName" placeholder="请输入指标名称" />
        </el-form-item>
        <el-form-item label="指标编码" prop="indicatorCode">
          <el-input v-model="indicatorData.indicatorCode" placeholder="请输入指标编码" />
        </el-form-item>
        <el-form-item label="SQL语句" prop="sqlContent">
          <el-input
            v-model="indicatorData.sqlContent"
            type="textarea"
            :rows="6"
            placeholder="请输入SQL语句"
          />
          <div class="sql-help" style="margin-top: 8px;">
            <el-button type="text" size="mini" @click="insertTemplate">
              <i class="el-icon-document-add"></i> 插入模板
            </el-button>
            <el-button type="text" size="mini" @click="formatSQL">
              <i class="el-icon-magic-stick"></i> 格式化
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="indicatorData.description"
            type="textarea"
            :rows="2"
            placeholder="请输入指标描述"
          />
        </el-form-item>
      </el-form>
    </div>

    <!-- 拖拽模式 -->
    <div v-else class="drag-mode">
      <div class="drag-container">
        <!-- 左侧：字段库 -->
        <div class="field-library">
          <div class="library-header">
            <h4>字段库</h4>
            <el-input
              v-model="fieldSearchKeyword"
              placeholder="搜索字段"
              prefix-icon="el-icon-search"
              size="mini"
              @input="handleFieldSearch"
              clearable
            />
          </div>

          <el-tabs v-model="activeFieldTab" size="mini">
            <!-- 常用字段 -->
            <el-tab-pane label="常用字段" name="common">
              <div class="field-list">
                <div
                  v-for="field in filteredCommonFields"
                  :key="field.fieldCode"
                  class="field-item"
                  draggable="true"
                  @dragstart="handleFieldDragStart($event, field)"
                >
                  <div class="field-icon">
                    <i :class="getFieldIcon(field.fieldType)"></i>
                  </div>
                  <div class="field-info">
                    <div class="field-name">{{ field.fieldName }}</div>
                    <div class="field-code">{{ field.fieldCode }}</div>
                  </div>
                  <div class="field-type">
                    <el-tag size="mini" :type="getFieldTypeColor(field.fieldType)">
                      {{ field.fieldType }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 表字段 -->
            <el-tab-pane label="表字段" name="table">
              <div class="table-selector">
                <el-select
                  v-model="selectedTable"
                  placeholder="选择数据表"
                  size="mini"
                  @change="loadTableFields"
                  filterable
                >
                  <el-option
                    v-for="table in availableTables"
                    :key="table.tableName"
                    :label="table.tableComment || table.tableName"
                    :value="table.tableName"
                  />
                </el-select>
              </div>
              
              <div class="field-list" v-loading="tableFieldsLoading">
                <div
                  v-for="field in filteredTableFields"
                  :key="field.columnName"
                  class="field-item"
                  draggable="true"
                  @dragstart="handleFieldDragStart($event, field, 'table')"
                >
                  <div class="field-icon">
                    <i :class="getFieldIcon(field.columnType)"></i>
                  </div>
                  <div class="field-info">
                    <div class="field-name">{{ field.columnComment || field.columnName }}</div>
                    <div class="field-code">{{ selectedTable }}.{{ field.columnName }}</div>
                  </div>
                  <div class="field-type">
                    <el-tag size="mini" :type="getFieldTypeColor(field.columnType)">
                      {{ field.columnType }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 函数库 -->
            <el-tab-pane label="函数库" name="function">
              <div class="function-categories">
                <el-collapse v-model="activeFunctionCategories" accordion>
                  <el-collapse-item
                    v-for="category in functionCategories"
                    :key="category.name"
                    :title="category.title"
                    :name="category.name"
                  >
                    <div class="function-list">
                      <div
                        v-for="func in category.functions"
                        :key="func.name"
                        class="function-item"
                        draggable="true"
                        @dragstart="handleFunctionDragStart($event, func)"
                      >
                        <div class="function-name">{{ func.name }}</div>
                        <div class="function-desc">{{ func.description }}</div>
                        <div class="function-syntax">{{ func.syntax }}</div>
                      </div>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 中间：SQL构建器 -->
        <div class="sql-builder">
          <div class="builder-header">
            <h4>SQL构建器</h4>
            <div class="builder-actions">
              <el-button size="mini" @click="formatSQL">格式化</el-button>
              <el-button size="mini" @click="validateSQL">验证</el-button>
            </div>
          </div>

          <div class="sql-sections">
            <!-- SELECT 子句 -->
            <div class="sql-section">
              <div class="section-header">
                <span class="section-title">SELECT</span>
                <el-button size="mini" type="text" @click="addSelectField">
                  <i class="el-icon-plus"></i>
                </el-button>
              </div>
              <div
                class="drop-zone select-zone"
                :class="{ 'drag-over': dragOverZone === 'select' }"
                @drop="handleDrop($event, 'select')"
                @dragover="handleDragOver($event, 'select')"
                @dragleave="handleDragLeave"
              >
                <div v-if="sqlBuilder.selectFields.length === 0" class="empty-hint">
                  拖拽字段到此处或点击"+"添加
                </div>
                <draggable
                  v-else
                  v-model="sqlBuilder.selectFields"
                  group="sql-fields"
                  :animation="200"
                  class="field-list"
                >
                  <div
                    v-for="(field, index) in sqlBuilder.selectFields"
                    :key="index"
                    class="sql-field-item"
                  >
                    <div class="field-content">
                      <el-input
                        v-model="field.expression"
                        size="mini"
                        placeholder="字段表达式"
                      />
                      <el-input
                        v-model="field.alias"
                        size="mini"
                        placeholder="别名"
                        style="margin-left: 8px; width: 100px;"
                      />
                    </div>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="removeSelectField(index)"
                    />
                  </div>
                </draggable>
              </div>
            </div>

            <!-- FROM 子句 -->
            <div class="sql-section">
              <div class="section-header">
                <span class="section-title">FROM</span>
              </div>
              <div
                class="drop-zone from-zone"
                :class="{ 'drag-over': dragOverZone === 'from' }"
                @drop="handleDrop($event, 'from')"
                @dragover="handleDragOver($event, 'from')"
                @dragleave="handleDragLeave"
              >
                <el-input
                  v-model="sqlBuilder.fromClause"
                  size="mini"
                  placeholder="拖拽表名到此处或手动输入"
                />
              </div>
            </div>

            <!-- WHERE 子句 -->
            <div class="sql-section">
              <div class="section-header">
                <span class="section-title">WHERE</span>
                <el-button size="mini" type="text" @click="addWhereCondition">
                  <i class="el-icon-plus"></i>
                </el-button>
              </div>
              <div
                class="drop-zone where-zone"
                :class="{ 'drag-over': dragOverZone === 'where' }"
                @drop="handleDrop($event, 'where')"
                @dragover="handleDragOver($event, 'where')"
                @dragleave="handleDragLeave"
              >
                <div v-if="sqlBuilder.whereConditions.length === 0" class="empty-hint">
                  拖拽字段到此处添加条件
                </div>
                <div v-else class="condition-list">
                  <div
                    v-for="(condition, index) in sqlBuilder.whereConditions"
                    :key="index"
                    class="condition-item"
                  >
                    <el-input
                      v-model="condition.expression"
                      size="mini"
                      placeholder="条件表达式"
                    />
                    <el-select
                      v-model="condition.operator"
                      size="mini"
                      style="width: 80px; margin: 0 8px;"
                    >
                      <el-option label="AND" value="AND" />
                      <el-option label="OR" value="OR" />
                    </el-select>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="removeWhereCondition(index)"
                    />
                  </div>
                </div>
              </div>
            </div>

            <!-- GROUP BY 子句 -->
            <div class="sql-section">
              <div class="section-header">
                <span class="section-title">GROUP BY</span>
              </div>
              <div
                class="drop-zone groupby-zone"
                :class="{ 'drag-over': dragOverZone === 'groupby' }"
                @drop="handleDrop($event, 'groupby')"
                @dragover="handleDragOver($event, 'groupby')"
                @dragleave="handleDragLeave"
              >
                <el-input
                  v-model="sqlBuilder.groupByClause"
                  size="mini"
                  placeholder="拖拽字段到此处或手动输入"
                />
              </div>
            </div>

            <!-- ORDER BY 子句 -->
            <div class="sql-section">
              <div class="section-header">
                <span class="section-title">ORDER BY</span>
              </div>
              <div
                class="drop-zone orderby-zone"
                :class="{ 'drag-over': dragOverZone === 'orderby' }"
                @drop="handleDrop($event, 'orderby')"
                @dragover="handleDragOver($event, 'orderby')"
                @dragleave="handleDragLeave"
              >
                <el-input
                  v-model="sqlBuilder.orderByClause"
                  size="mini"
                  placeholder="拖拽字段到此处或手动输入"
                />
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧：预览和配置 -->
        <div class="preview-panel">
          <div class="panel-header">
            <h4>预览和配置</h4>
          </div>

          <el-tabs v-model="activePreviewTab" size="mini">
            <!-- SQL预览 -->
            <el-tab-pane label="SQL预览" name="sql">
              <div class="sql-preview">
                <pre class="sql-code">{{ generatedSQL }}</pre>
                <div class="preview-actions">
                  <el-button size="mini" @click="copySQL">复制SQL</el-button>
                  <el-button size="mini" @click="applySQL">应用到表单</el-button>
                </div>
              </div>
            </el-tab-pane>

            <!-- 基本信息 -->
            <el-tab-pane label="基本信息" name="info">
              <el-form label-width="80px" size="mini">
                <el-form-item label="指标名称">
                  <el-input v-model="indicatorData.indicatorName" />
                </el-form-item>
                <el-form-item label="指标编码">
                  <el-input v-model="indicatorData.indicatorCode" />
                </el-form-item>
                <el-form-item label="描述">
                  <el-input
                    v-model="indicatorData.description"
                    type="textarea"
                    :rows="3"
                  />
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 参数配置 -->
            <el-tab-pane label="参数配置" name="params">
              <div class="param-config">
                <div class="param-header">
                  <span>参数列表</span>
                  <el-button size="mini" type="text" @click="addParameter">
                    <i class="el-icon-plus"></i>
                  </el-button>
                </div>
                <div
                  v-for="(param, index) in indicatorData.parameters"
                  :key="index"
                  class="param-item"
                >
                  <el-input
                    v-model="param.name"
                    placeholder="参数名"
                    size="mini"
                  />
                  <el-select v-model="param.type" size="mini">
                    <el-option label="字符串" value="STRING" />
                    <el-option label="数字" value="NUMBER" />
                    <el-option label="日期" value="DATE" />
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
                    @click="removeParameter(index)"
                  />
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>

    <!-- 底部操作按钮 -->
    <div class="builder-footer">
      <el-button @click="resetForm">重置</el-button>
      <el-button type="primary" @click="saveIndicator" :loading="saving">
        添加到组合
      </el-button>
    </div>

    <!-- SQL预览对话框 -->
    <el-dialog
      title="SQL预览"
      :visible.sync="sqlPreviewVisible"
      width="60%"
    >
      <div class="sql-preview-dialog">
        <pre class="sql-code">{{ generatedSQL }}</pre>
      </div>
      <div slot="footer">
        <el-button @click="sqlPreviewVisible = false">关闭</el-button>
        <el-button type="primary" @click="copySQL">复制</el-button>
      </div>
    </el-dialog>

    <!-- SQL模板选择对话框 -->
    <el-dialog
      title="SQL模板"
      :visible.sync="templateDialogVisible"
      width="600px"
      append-to-body
    >
      <div class="template-list">
        <div
          v-for="template in sqlTemplates"
          :key="template.id"
          class="template-item"
          @click="selectTemplate(template)"
        >
          <div class="template-header">
            <h4>{{ template.name }}</h4>
            <el-tag size="mini">{{ template.category }}</el-tag>
          </div>
          <div class="template-description">
            {{ template.description }}
          </div>
          <div class="template-preview">
            <pre>{{ template.sql }}</pre>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import draggable from 'vuedraggable'

export default {
  name: 'DragIndicatorBuilder',
  components: {
    draggable
  },
  data() {
    return {
      buildMode: 'form', // 'form' | 'drag'
      
      // 指标数据
      indicatorData: {
        indicatorName: '',
        indicatorCode: '',
        sqlContent: '',
        description: '',
        parameters: []
      },
      
      // 表单验证规则
      formRules: {
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
      
      // 字段库相关
      fieldSearchKeyword: '',
      activeFieldTab: 'common',
      selectedTable: '',
      availableTables: [],
      tableFields: [],
      tableFieldsLoading: false,
      
      // 常用字段
      commonFields: [
        { fieldCode: 'ENTERPRISE_ID', fieldName: '企业ID', fieldType: 'VARCHAR' },
        { fieldCode: 'ENTERPRISE_NAME', fieldName: '企业名称', fieldType: 'VARCHAR' },
        { fieldCode: 'AMOUNT', fieldName: '金额', fieldType: 'DECIMAL' },
        { fieldCode: 'CREATE_TIME', fieldName: '创建时间', fieldType: 'TIMESTAMP' },
        { fieldCode: 'UPDATE_TIME', fieldName: '更新时间', fieldType: 'TIMESTAMP' },
        { fieldCode: 'STATUS', fieldName: '状态', fieldType: 'VARCHAR' },
        { fieldCode: 'REMARK', fieldName: '备注', fieldType: 'VARCHAR' }
      ],
      
      // 函数库
      activeFunctionCategories: ['aggregate'],
      functionCategories: [
        {
          name: 'aggregate',
          title: '聚合函数',
          functions: [
            { name: 'SUM', description: '求和', syntax: 'SUM(column)' },
            { name: 'COUNT', description: '计数', syntax: 'COUNT(column)' },
            { name: 'AVG', description: '平均值', syntax: 'AVG(column)' },
            { name: 'MAX', description: '最大值', syntax: 'MAX(column)' },
            { name: 'MIN', description: '最小值', syntax: 'MIN(column)' }
          ]
        },
        {
          name: 'string',
          title: '字符串函数',
          functions: [
            { name: 'CONCAT', description: '连接字符串', syntax: 'CONCAT(str1, str2)' },
            { name: 'SUBSTRING', description: '截取字符串', syntax: 'SUBSTRING(str, start, length)' },
            { name: 'UPPER', description: '转大写', syntax: 'UPPER(str)' },
            { name: 'LOWER', description: '转小写', syntax: 'LOWER(str)' }
          ]
        },
        {
          name: 'date',
          title: '日期函数',
          functions: [
            { name: 'SYSDATE', description: '当前日期', syntax: 'SYSDATE' },
            { name: 'TO_DATE', description: '转换日期', syntax: 'TO_DATE(str, format)' },
            { name: 'TRUNC', description: '截取日期', syntax: 'TRUNC(date, format)' }
          ]
        }
      ],
      
      // SQL构建器
      sqlBuilder: {
        selectFields: [],
        fromClause: '',
        whereConditions: [],
        groupByClause: '',
        orderByClause: ''
      },
      
      // 拖拽状态
      dragOverZone: null,
      
      // 预览相关
      activePreviewTab: 'sql',
      sqlPreviewVisible: false,

      // 模板相关
      templateDialogVisible: false,
      sqlTemplates: [
        {
          id: 1,
          name: '基础查询',
          category: '查询',
          description: '基本的SELECT查询模板',
          sql: 'SELECT column1, column2\nFROM table_name\nWHERE condition;'
        },
        {
          id: 2,
          name: '聚合查询',
          category: '查询',
          description: '带聚合函数的查询模板',
          sql: 'SELECT column1, COUNT(*) as count\nFROM table_name\nGROUP BY column1\nHAVING COUNT(*) > 1;'
        },
        {
          id: 3,
          name: '连接查询',
          category: '查询',
          description: '多表连接查询模板',
          sql: 'SELECT t1.column1, t2.column2\nFROM table1 t1\nLEFT JOIN table2 t2 ON t1.id = t2.table1_id\nWHERE t1.status = \'active\';'
        },
        {
          id: 4,
          name: '风险指标查询',
          category: '风险',
          description: '风险评估指标查询模板',
          sql: 'SELECT \n    ENTERPRISE_NAME,\n    SUM(RISK_AMOUNT) as total_risk,\n    COUNT(*) as risk_count\nFROM TBL_RISK_ASSESSMENT\nWHERE RISK_LEVEL >= \'HIGH\'\nGROUP BY ENTERPRISE_NAME\nORDER BY total_risk DESC;'
        },
        {
          id: 5,
          name: '财务指标查询',
          category: '财务',
          description: '财务数据分析模板',
          sql: 'SELECT \n    ENTERPRISE_NAME,\n    REVENUE,\n    PROFIT,\n    (PROFIT / REVENUE) * 100 as profit_margin\nFROM TBL_FINANCIAL_DATA\nWHERE YEAR = ${year}\nORDER BY profit_margin DESC;'
        }
      ],

      // 状态
      saving: false
    }
  },
  computed: {
    // 过滤后的常用字段
    filteredCommonFields() {
      if (!this.fieldSearchKeyword) return this.commonFields
      return this.commonFields.filter(field =>
        field.fieldName.includes(this.fieldSearchKeyword) ||
        field.fieldCode.includes(this.fieldSearchKeyword)
      )
    },
    
    // 过滤后的表字段
    filteredTableFields() {
      if (!this.fieldSearchKeyword) return this.tableFields
      return this.tableFields.filter(field =>
        (field.columnComment && field.columnComment.includes(this.fieldSearchKeyword)) ||
        field.columnName.includes(this.fieldSearchKeyword)
      )
    },
    
    // 生成的SQL
    generatedSQL() {
      return this.buildSQL()
    },
    
    // 是否可以预览
    canPreview() {
      return this.buildMode === 'drag' && (
        this.sqlBuilder.selectFields.length > 0 || 
        this.sqlBuilder.fromClause
      )
    }
  },
  mounted() {
    this.loadAvailableTables()
  },
  methods: {
    // 加载可用表
    async loadAvailableTables() {
      // 模拟数据，实际应该调用API
      this.availableTables = [
        { tableName: 'TBL_ENTERPRISE_INFO', tableComment: '企业基本信息表' },
        { tableName: 'TBL_FINANCIAL_DATA', tableComment: '财务数据表' },
        { tableName: 'TBL_RISK_ASSESSMENT', tableComment: '风险评估表' },
        { tableName: 'TBL_AUDIT_PROJECT', tableComment: '审计项目表' }
      ]
    },

    // 加载表字段
    async loadTableFields() {
      if (!this.selectedTable) return

      this.tableFieldsLoading = true
      try {
        // 模拟数据，实际应该调用API获取表结构
        const mockFields = {
          'TBL_ENTERPRISE_INFO': [
            { columnName: 'ENTERPRISE_ID', columnComment: '企业ID', columnType: 'VARCHAR' },
            { columnName: 'ENTERPRISE_NAME', columnComment: '企业名称', columnType: 'VARCHAR' },
            { columnName: 'REGISTERED_CAPITAL', columnComment: '注册资本', columnType: 'DECIMAL' },
            { columnName: 'ESTABLISHMENT_DATE', columnComment: '成立日期', columnType: 'DATE' },
            { columnName: 'INDUSTRY_TYPE', columnComment: '行业类型', columnType: 'VARCHAR' }
          ],
          'TBL_FINANCIAL_DATA': [
            { columnName: 'DATA_ID', columnComment: '数据ID', columnType: 'VARCHAR' },
            { columnName: 'ENTERPRISE_ID', columnComment: '企业ID', columnType: 'VARCHAR' },
            { columnName: 'REVENUE', columnComment: '营业收入', columnType: 'DECIMAL' },
            { columnName: 'PROFIT', columnComment: '净利润', columnType: 'DECIMAL' },
            { columnName: 'ASSETS', columnComment: '总资产', columnType: 'DECIMAL' }
          ]
        }

        this.tableFields = mockFields[this.selectedTable] || []
      } catch (error) {
        console.error('加载表字段失败:', error)
        this.$message.error('加载表字段失败')
      } finally {
        this.tableFieldsLoading = false
      }
    },

    // 字段搜索
    handleFieldSearch() {
      // 搜索逻辑已在计算属性中实现
    },

    // 获取字段图标
    getFieldIcon(fieldType) {
      const iconMap = {
        'VARCHAR': 'el-icon-document',
        'DECIMAL': 'el-icon-coin',
        'NUMBER': 'el-icon-coin',
        'DATE': 'el-icon-date',
        'TIMESTAMP': 'el-icon-time',
        'CLOB': 'el-icon-document-copy'
      }
      return iconMap[fieldType] || 'el-icon-document'
    },

    // 获取字段类型颜色
    getFieldTypeColor(fieldType) {
      const colorMap = {
        'VARCHAR': 'primary',
        'DECIMAL': 'success',
        'NUMBER': 'success',
        'DATE': 'warning',
        'TIMESTAMP': 'warning',
        'CLOB': 'info'
      }
      return colorMap[fieldType] || 'info'
    },

    // 字段拖拽开始
    handleFieldDragStart(event, field, source = 'common') {
      const dragData = {
        type: 'field',
        source: source,
        field: field
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(dragData))
      event.dataTransfer.effectAllowed = 'copy'
    },

    // 函数拖拽开始
    handleFunctionDragStart(event, func) {
      const dragData = {
        type: 'function',
        function: func
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(dragData))
      event.dataTransfer.effectAllowed = 'copy'
    },

    // 拖拽悬停
    handleDragOver(event, zone) {
      event.preventDefault()
      event.dataTransfer.dropEffect = 'copy'
      this.dragOverZone = zone
    },

    // 拖拽离开
    handleDragLeave() {
      this.dragOverZone = null
    },

    // 拖拽放置
    handleDrop(event, zone) {
      event.preventDefault()
      this.dragOverZone = null

      try {
        const dragData = JSON.parse(event.dataTransfer.getData('text/plain'))

        if (dragData.type === 'field') {
          this.handleFieldDrop(dragData, zone)
        } else if (dragData.type === 'function') {
          this.handleFunctionDrop(dragData, zone)
        }
      } catch (error) {
        console.error('拖拽数据解析失败:', error)
      }
    },

    // 处理字段放置
    handleFieldDrop(dragData, zone) {
      const field = dragData.field
      let fieldExpression = ''

      if (dragData.source === 'table') {
        fieldExpression = `${this.selectedTable}.${field.columnName}`
      } else {
        fieldExpression = field.fieldCode
      }

      switch (zone) {
        case 'select':
          this.sqlBuilder.selectFields.push({
            expression: fieldExpression,
            alias: field.fieldName || field.columnComment || ''
          })
          break
        case 'from':
          if (dragData.source === 'table') {
            this.sqlBuilder.fromClause = this.selectedTable
          }
          break
        case 'where':
          this.sqlBuilder.whereConditions.push({
            expression: `${fieldExpression} = ?`,
            operator: 'AND'
          })
          break
        case 'groupby':
          if (this.sqlBuilder.groupByClause) {
            this.sqlBuilder.groupByClause += `, ${fieldExpression}`
          } else {
            this.sqlBuilder.groupByClause = fieldExpression
          }
          break
        case 'orderby':
          if (this.sqlBuilder.orderByClause) {
            this.sqlBuilder.orderByClause += `, ${fieldExpression}`
          } else {
            this.sqlBuilder.orderByClause = fieldExpression
          }
          break
      }
    },

    // 处理函数放置
    handleFunctionDrop(dragData, zone) {
      const func = dragData.function

      switch (zone) {
        case 'select':
          this.sqlBuilder.selectFields.push({
            expression: func.syntax,
            alias: func.name.toLowerCase() + '_result'
          })
          break
        case 'where':
          this.sqlBuilder.whereConditions.push({
            expression: func.syntax + ' > 0',
            operator: 'AND'
          })
          break
      }
    },

    // 添加SELECT字段
    addSelectField() {
      this.sqlBuilder.selectFields.push({
        expression: '',
        alias: ''
      })
    },

    // 移除SELECT字段
    removeSelectField(index) {
      this.sqlBuilder.selectFields.splice(index, 1)
    },

    // 添加WHERE条件
    addWhereCondition() {
      this.sqlBuilder.whereConditions.push({
        expression: '',
        operator: 'AND'
      })
    },

    // 移除WHERE条件
    removeWhereCondition(index) {
      this.sqlBuilder.whereConditions.splice(index, 1)
    },

    // 构建SQL
    buildSQL() {
      if (this.buildMode === 'form') {
        return this.indicatorData.sqlContent
      }

      let sql = ''

      // SELECT子句
      if (this.sqlBuilder.selectFields.length > 0) {
        const selectParts = this.sqlBuilder.selectFields
          .filter(field => field.expression)
          .map(field => {
            if (field.alias) {
              return `${field.expression} AS ${field.alias}`
            }
            return field.expression
          })

        if (selectParts.length > 0) {
          sql += 'SELECT\n    ' + selectParts.join(',\n    ')
        }
      }

      // FROM子句
      if (this.sqlBuilder.fromClause) {
        sql += '\nFROM\n    ' + this.sqlBuilder.fromClause
      }

      // WHERE子句
      if (this.sqlBuilder.whereConditions.length > 0) {
        const whereParts = this.sqlBuilder.whereConditions
          .filter(condition => condition.expression)
          .map((condition, index) => {
            if (index === 0) {
              return condition.expression
            }
            return `${condition.operator} ${condition.expression}`
          })

        if (whereParts.length > 0) {
          sql += '\nWHERE\n    ' + whereParts.join('\n    ')
        }
      }

      // GROUP BY子句
      if (this.sqlBuilder.groupByClause) {
        sql += '\nGROUP BY\n    ' + this.sqlBuilder.groupByClause
      }

      // ORDER BY子句
      if (this.sqlBuilder.orderByClause) {
        sql += '\nORDER BY\n    ' + this.sqlBuilder.orderByClause
      }

      return sql
    },

    // 格式化SQL
    formatSQL() {
      const sql = this.buildSQL()
      if (sql) {
        // 简单的SQL格式化
        const formatted = sql
          .replace(/\s+/g, ' ')
          .replace(/,/g, ',\n    ')
          .replace(/\bSELECT\b/gi, 'SELECT\n    ')
          .replace(/\bFROM\b/gi, '\nFROM\n    ')
          .replace(/\bWHERE\b/gi, '\nWHERE\n    ')
          .replace(/\bAND\b/gi, '\n    AND ')
          .replace(/\bOR\b/gi, '\n    OR ')
          .replace(/\bGROUP BY\b/gi, '\nGROUP BY\n    ')
          .replace(/\bORDER BY\b/gi, '\nORDER BY\n    ')

        if (this.buildMode === 'form') {
          this.indicatorData.sqlContent = formatted
        }

        this.$message.success('SQL格式化完成')
      }
    },

    // 验证SQL
    validateSQL() {
      const sql = this.buildSQL()
      if (!sql) {
        this.$message.warning('请先构建SQL语句')
        return
      }

      // 简单的SQL验证
      const requiredKeywords = ['SELECT', 'FROM']
      const hasRequired = requiredKeywords.every(keyword =>
        sql.toUpperCase().includes(keyword)
      )

      if (hasRequired) {
        this.$message.success('SQL语法验证通过')
      } else {
        this.$message.error('SQL语法不完整，缺少必要的关键字')
      }
    },

    // 预览SQL
    previewSQL() {
      this.sqlPreviewVisible = true
    },

    // 复制SQL
    copySQL() {
      const sql = this.generatedSQL
      if (sql) {
        navigator.clipboard.writeText(sql).then(() => {
          this.$message.success('SQL已复制到剪贴板')
        }).catch(() => {
          this.$message.error('复制失败')
        })
      }
    },

    // 应用SQL到表单
    applySQL() {
      const sql = this.buildSQL()
      if (sql) {
        this.indicatorData.sqlContent = sql
        this.buildMode = 'form'
        this.$message.success('SQL已应用到表单模式')
      }
    },

    // 添加参数
    addParameter() {
      this.indicatorData.parameters.push({
        name: '',
        type: 'STRING',
        defaultValue: '',
        description: ''
      })
    },

    // 移除参数
    removeParameter(index) {
      this.indicatorData.parameters.splice(index, 1)
    },

    // 清空所有
    clearAll() {
      this.$confirm('确定要清空所有内容吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.resetForm()
        this.resetSQLBuilder()
        this.$message.success('已清空所有内容')
      }).catch(() => {})
    },

    // 重置表单
    resetForm() {
      this.indicatorData = {
        indicatorName: '',
        indicatorCode: '',
        sqlContent: '',
        description: '',
        parameters: []
      }
      this.$refs.indicatorForm?.clearValidate()
    },

    // 重置SQL构建器
    resetSQLBuilder() {
      this.sqlBuilder = {
        selectFields: [],
        fromClause: '',
        whereConditions: [],
        groupByClause: '',
        orderByClause: ''
      }
    },

    // 保存指标
    async saveIndicator() {
      try {
        // 验证基本信息
        if (!this.indicatorData.indicatorName) {
          this.$message.error('请输入指标名称')
          return
        }

        if (!this.indicatorData.indicatorCode) {
          this.$message.error('请输入指标编码')
          return
        }

        // 获取SQL内容
        let sqlContent = ''
        if (this.buildMode === 'form') {
          sqlContent = this.indicatorData.sqlContent
        } else {
          sqlContent = this.buildSQL()
        }

        if (!sqlContent) {
          this.$message.error('请构建或输入SQL语句')
          return
        }

        this.saving = true

        // 构建指标数据
        const indicatorData = {
          ...this.indicatorData,
          sqlContent: sqlContent
        }

        // 发送到父组件
        this.$emit('save', indicatorData)

        // 重置表单
        this.resetForm()
        this.resetSQLBuilder()

        this.$message.success('指标创建成功')
      } catch (error) {
        console.error('保存指标失败:', error)
        this.$message.error('保存指标失败')
      } finally {
        this.saving = false
      }
    },

    // 插入模板
    insertTemplate() {
      this.templateDialogVisible = true
    },

    // 选择模板
    selectTemplate(template) {
      this.indicatorData.sqlContent = template.sql
      this.templateDialogVisible = false
      this.$message.success('模板已插入')
    },

    // 格式化SQL
    formatSQL() {
      if (!this.indicatorData.sqlContent.trim()) {
        this.$message.warning('请先输入SQL内容')
        return
      }

      // 简单的SQL格式化
      let formatted = this.indicatorData.sqlContent
        .replace(/\s+/g, ' ')
        .replace(/\s*,\s*/g, ',\n  ')
        .replace(/\s*(SELECT|FROM|WHERE|GROUP BY|ORDER BY|HAVING|JOIN|LEFT JOIN|RIGHT JOIN|INNER JOIN)\s+/gi, '\n$1 ')
        .replace(/\s*AND\s+/gi, '\n  AND ')
        .replace(/\s*OR\s+/gi, '\n  OR ')
        .trim()

      this.indicatorData.sqlContent = formatted
      this.$message.success('SQL格式化完成')
    }
  }
}
</script>

<style lang="scss" scoped>
.drag-indicator-builder {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;

  .builder-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    background: #fff;
    border-bottom: 1px solid #e4e7ed;

    .mode-toggle {
      .el-radio-group {
        .el-radio-button__inner {
          padding: 8px 16px;
          font-size: 12px;
        }
      }
    }

    .header-actions {
      display: flex;
      gap: 8px;
    }
  }

  .form-mode {
    flex: 1;
    padding: 20px;
    background: #fff;
    overflow-y: auto;

    .el-form {
      max-width: 600px;
      margin: 0 auto;
    }
  }

  .drag-mode {
    flex: 1;
    overflow: hidden;

    .drag-container {
      height: 100%;
      display: flex;
      gap: 12px;
      padding: 12px;
    }
  }

  // 字段库
  .field-library {
    width: 280px;
    background: #fff;
    border-radius: 6px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;

    .library-header {
      padding: 12px;
      border-bottom: 1px solid #e4e7ed;

      h4 {
        margin: 0 0 8px 0;
        font-size: 14px;
        font-weight: 600;
        color: #303133;
      }
    }

    .table-selector {
      padding: 8px 12px;
      border-bottom: 1px solid #e4e7ed;
    }

    .field-list {
      flex: 1;
      padding: 8px;
      overflow-y: auto;
      max-height: 400px;

      .field-item {
        display: flex;
        align-items: center;
        padding: 8px;
        margin-bottom: 4px;
        border: 1px solid #e4e7ed;
        border-radius: 4px;
        cursor: grab;
        transition: all 0.3s;

        &:hover {
          border-color: #409eff;
          background: #f0f9ff;
        }

        &:active {
          cursor: grabbing;
        }

        .field-icon {
          margin-right: 8px;
          color: #909399;
        }

        .field-info {
          flex: 1;
          min-width: 0;

          .field-name {
            font-size: 12px;
            font-weight: 600;
            color: #303133;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }

          .field-code {
            font-size: 11px;
            color: #909399;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
        }

        .field-type {
          margin-left: 4px;
        }
      }
    }

    .function-categories {
      padding: 8px;

      .function-list {
        .function-item {
          padding: 8px;
          margin-bottom: 4px;
          border: 1px solid #e4e7ed;
          border-radius: 4px;
          cursor: grab;
          transition: all 0.3s;

          &:hover {
            border-color: #409eff;
            background: #f0f9ff;
          }

          .function-name {
            font-size: 12px;
            font-weight: 600;
            color: #409eff;
          }

          .function-desc {
            font-size: 11px;
            color: #606266;
            margin: 2px 0;
          }

          .function-syntax {
            font-size: 10px;
            color: #909399;
            font-family: monospace;
            background: #f5f7fa;
            padding: 2px 4px;
            border-radius: 2px;
          }
        }
      }
    }
  }

  // SQL构建器
  .sql-builder {
    flex: 1;
    background: #fff;
    border-radius: 6px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;

    .builder-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 16px;
      border-bottom: 1px solid #e4e7ed;

      h4 {
        margin: 0;
        font-size: 14px;
        font-weight: 600;
        color: #303133;
      }

      .builder-actions {
        display: flex;
        gap: 8px;
      }
    }

    .sql-sections {
      flex: 1;
      padding: 12px;
      overflow-y: auto;

      .sql-section {
        margin-bottom: 16px;

        .section-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 8px;

          .section-title {
            font-size: 13px;
            font-weight: 600;
            color: #409eff;
          }
        }

        .drop-zone {
          min-height: 60px;
          border: 2px dashed #e4e7ed;
          border-radius: 4px;
          padding: 8px;
          transition: all 0.3s;

          &.drag-over {
            border-color: #409eff;
            background: #f0f9ff;
          }

          .empty-hint {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 44px;
            color: #909399;
            font-size: 12px;
          }

          .field-list {
            .sql-field-item {
              display: flex;
              align-items: center;
              margin-bottom: 8px;

              .field-content {
                flex: 1;
                display: flex;
                align-items: center;
              }
            }
          }

          .condition-list {
            .condition-item {
              display: flex;
              align-items: center;
              margin-bottom: 8px;
            }
          }
        }
      }
    }
  }

  // 预览面板
  .preview-panel {
    width: 320px;
    background: #fff;
    border-radius: 6px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;

    .panel-header {
      padding: 12px 16px;
      border-bottom: 1px solid #e4e7ed;

      h4 {
        margin: 0;
        font-size: 14px;
        font-weight: 600;
        color: #303133;
      }
    }

    .sql-preview {
      .sql-code {
        background: #f5f7fa;
        border: 1px solid #e4e7ed;
        border-radius: 4px;
        padding: 12px;
        font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
        font-size: 11px;
        line-height: 1.5;
        color: #303133;
        white-space: pre-wrap;
        word-break: break-all;
        max-height: 200px;
        overflow-y: auto;
        margin-bottom: 12px;
      }

      .preview-actions {
        display: flex;
        gap: 8px;
      }
    }

    .param-config {
      .param-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;

        span {
          font-size: 12px;
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

  .builder-footer {
    padding: 12px 16px;
    background: #fff;
    border-top: 1px solid #e4e7ed;
    display: flex;
    justify-content: flex-end;
    gap: 8px;
  }
}

// 模板对话框样式
.template-list {
  max-height: 400px;
  overflow-y: auto;
}

.template-item {
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    border-color: #409eff;
    box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
  }
}

.template-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;

  h4 {
    margin: 0;
    font-size: 16px;
  }
}

.template-description {
  color: #666;
  margin-bottom: 12px;
}

.template-preview {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 12px;

  pre {
    margin: 0;
    white-space: pre-wrap;
  }
}

.sql-help {
  .el-button--text {
    padding: 0;
    margin-right: 12px;
    font-size: 12px;
  }
}</style>
