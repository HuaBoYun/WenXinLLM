<template>
  <el-dialog
    title="拖拽式SQL指标构建器"
    :visible.sync="dialogVisible"
    width="95%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :modal="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :lock-scroll="true"
    custom-class="drag-sql-builder-dialog risk-mxgl-sjmxgl-page"
    @close="handleClose"
  >
    <div class="builder-container risk-mxgl-sjmxgl-page">
      <!-- 顶部工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <span class="title">构建SQL指标</span>
          <el-divider direction="vertical" />
          <el-button size="mini" @click="previewSQL" :disabled="!canPreview">
            <i class="el-icon-view"></i> 预览SQL
          </el-button>
          <el-button size="mini" @click="formatSQL">
            <i class="el-icon-magic-stick"></i> 格式化
          </el-button>
          <el-button size="mini" @click="validateSQL">
            <i class="el-icon-circle-check"></i> 验证
          </el-button>
          <el-button size="mini" @click="clearAll">
            <i class="el-icon-delete"></i> 清空
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-button size="mini" @click="showHelp">
            <i class="el-icon-question"></i> 帮助
          </el-button>
        </div>
      </div>

      <!-- 主要内容区域 -->
      <div class="main-content">
        <!-- 左侧：字段库 -->
        <div class="field-library">
          <div class="library-header">
            <h4><i class="el-icon-folder-opened"></i> 字段库</h4>
            <el-input
              v-model="fieldSearchKeyword"
              placeholder="搜索字段"
              prefix-icon="el-icon-search"
              size="mini"
              @input="handleFieldSearch"
              clearable
            />
          </div>

          <el-tabs v-model="activeFieldTab" size="mini" class="field-tabs">
            <!-- 常用字段 -->
            <el-tab-pane label="常用字段" name="common">
              <div class="field-list">
                <div
                  v-for="field in filteredCommonFields"
                  :key="field.fieldCode"
                  class="field-item"
                  draggable="true"
                  @dragstart="handleFieldDragStart($event, field)"
                  @click="addFieldToBuilder(field)"
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
              <div class="data-source-selector">
                <el-select
                  v-model="selectedDataSource"
                  placeholder="选择数据源"
                  size="mini"
                  @change="loadDataSourceTables"
                  filterable
                  style="width: 100%; margin-bottom: 8px;"
                >
                  <el-option
                    v-for="ds in dataSourceList"
                    :key="ds.sourceId"
                    :label="ds.sourceName"
                    :value="ds.sourceId"
                  />
                </el-select>
              </div>
              <div class="table-selector">
                <el-select
                  v-model="selectedTable"
                  placeholder="选择数据表"
                  size="mini"
                  @change="loadTableFields"
                  filterable
                  style="width: 100%"
                  :disabled="!selectedDataSource"
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
                  @click="addFieldToBuilder(field, 'table')"
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
                        @click="addFunctionToBuilder(func)"
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

            <!-- SQL模板 -->
            <el-tab-pane label="SQL模板" name="template">
              <div class="template-list">
                <div
                  v-for="template in sqlTemplates"
                  :key="template.templateId"
                  class="template-item"
                  @click="applyTemplate(template)"
                >
                  <div class="template-name">{{ template.templateName }}</div>
                  <div class="template-desc">{{ template.description }}</div>
                  <div class="template-preview">{{ template.sqlContent.substring(0, 100) }}...</div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 中间：SQL构建器 -->
        <div class="sql-builder">
          <div class="builder-header">
            <h4><i class="el-icon-s-grid"></i> SQL构建器</h4>
            <div class="builder-stats">
              <span>字段: {{ sqlBuilder.selectFields.length }}</span>
              <span>条件: {{ sqlBuilder.whereConditions.length }}</span>
            </div>
          </div>

          <div class="sql-sections">
            <!-- SELECT 子句 -->
            <div class="sql-section">
              <div class="section-header">
                <span class="section-title">
                  <i class="el-icon-s-data"></i> SELECT
                </span>
                <div class="section-actions">
                  <el-button size="mini" type="text" @click="addSelectField">
                    <i class="el-icon-plus"></i>
                  </el-button>
                  <el-button size="mini" type="text" @click="clearSelectFields">
                    <i class="el-icon-delete"></i>
                  </el-button>
                </div>
              </div>
              <div
                class="drop-zone select-zone"
                :class="{ 'drag-over': dragOverZone === 'select' }"
                @drop="handleDrop($event, 'select')"
                @dragover="handleDragOver($event, 'select')"
                @dragleave="handleDragLeave"
              >
                <div v-if="sqlBuilder.selectFields.length === 0" class="empty-hint">
                  <i class="el-icon-upload2"></i>
                  <p>拖拽字段到此处或点击字段添加</p>
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
                      <span class="as-label">AS</span>
                      <el-input
                        v-model="field.alias"
                        size="mini"
                        placeholder="别名"
                        style="width: 120px;"
                      />
                    </div>
                    <div class="field-actions">
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-delete"
                        @click="removeSelectField(index)"
                      />
                    </div>
                  </div>
                </draggable>
              </div>
            </div>

            <!-- FROM 子句 -->
            <div class="sql-section">
              <div class="section-header">
                <span class="section-title">
                  <i class="el-icon-s-platform"></i> FROM
                </span>
                <div class="section-actions">
                  <el-button size="mini" type="text" @click="clearFromClause">
                    <i class="el-icon-delete"></i>
                  </el-button>
                </div>
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
                  placeholder="拖拽表名到此处或手动输入表名"
                  prefix-icon="el-icon-s-platform"
                />
              </div>
            </div>

            <!-- WHERE 子句 -->
            <div class="sql-section">
              <div class="section-header">
                <span class="section-title">
                  <i class="el-icon-s-operation"></i> WHERE
                </span>
                <div class="section-actions">
                  <el-button size="mini" type="text" @click="addWhereCondition">
                    <i class="el-icon-plus"></i>
                  </el-button>
                  <el-button size="mini" type="text" @click="clearWhereConditions">
                    <i class="el-icon-delete"></i>
                  </el-button>
                </div>
              </div>
              <div
                class="drop-zone where-zone"
                :class="{ 'drag-over': dragOverZone === 'where' }"
                @drop="handleDrop($event, 'where')"
                @dragover="handleDragOver($event, 'where')"
                @dragleave="handleDragLeave"
              >
                <div v-if="sqlBuilder.whereConditions.length === 0" class="empty-hint">
                  <i class="el-icon-upload2"></i>
                  <p>拖拽字段到此处添加条件</p>
                </div>
                <div v-else class="condition-list">
                  <div
                    v-for="(condition, index) in sqlBuilder.whereConditions"
                    :key="index"
                    class="condition-item"
                  >
                    <div class="condition-content">
                      <el-input
                        v-model="condition.expression"
                        size="mini"
                        placeholder="条件表达式，如: AMOUNT > 1000"
                        style="flex: 1;"
                      />
                      <el-select
                        v-if="index < sqlBuilder.whereConditions.length - 1"
                        v-model="condition.operator"
                        size="mini"
                        style="width: 80px; margin-left: 8px;"
                      >
                        <el-option label="AND" value="AND" />
                        <el-option label="OR" value="OR" />
                      </el-select>
                    </div>
                    <div class="condition-actions">
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
            </div>

            <!-- GROUP BY 子句 -->
            <div class="sql-section">
              <div class="section-header">
                <span class="section-title">
                  <i class="el-icon-s-unfold"></i> GROUP BY
                </span>
                <div class="section-actions">
                  <el-button size="mini" type="text" @click="clearGroupByClause">
                    <i class="el-icon-delete"></i>
                  </el-button>
                </div>
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
                  placeholder="拖拽字段到此处或手动输入分组字段"
                  prefix-icon="el-icon-s-unfold"
                />
              </div>
            </div>

            <!-- ORDER BY 子句 -->
            <div class="sql-section">
              <div class="section-header">
                <span class="section-title">
                  <i class="el-icon-sort"></i> ORDER BY
                </span>
                <div class="section-actions">
                  <el-button size="mini" type="text" @click="clearOrderByClause">
                    <i class="el-icon-delete"></i>
                  </el-button>
                </div>
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
                  placeholder="拖拽字段到此处或手动输入排序字段"
                  prefix-icon="el-icon-sort"
                />
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧：预览和配置 -->
        <div class="preview-panel">
          <div class="panel-header">
            <h4><i class="el-icon-view"></i> 预览配置</h4>
          </div>

          <el-tabs v-model="activePreviewTab" size="mini" class="preview-tabs">
            <!-- SQL预览 -->
            <el-tab-pane label="SQL预览" name="sql">
              <div class="sql-preview">
                <div class="preview-header">
                  <span>生成的SQL</span>
                  <div class="preview-actions">
                    <el-button size="mini" @click="copySQL">复制</el-button>
                    <el-button size="mini" @click="executeSQL" :disabled="!generatedSQL">测试</el-button>
                  </div>
                </div>
                <pre class="sql-code">{{ generatedSQL || '// 请在左侧构建SQL语句' }}</pre>
              </div>
            </el-tab-pane>

            <!-- 基本信息 -->
            <el-tab-pane label="基本信息" name="info">
              <el-form label-width="80px" size="mini" class="info-form">
                <el-form-item label="指标名称" required>
                  <el-input v-model="indicatorData.indicatorName" placeholder="请输入指标名称" />
                </el-form-item>
                <el-form-item label="指标编码" required>
                  <el-input v-model="indicatorData.indicatorCode" placeholder="请输入指标编码" />
                </el-form-item>
                <el-form-item label="描述">
                  <el-input
                    v-model="indicatorData.description"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入指标描述"
                  />
                </el-form-item>
                <el-form-item label="分类">
                  <el-select v-model="indicatorData.category" placeholder="选择指标分类">
                    <el-option label="财务指标" value="FINANCIAL" />
                    <el-option label="风险指标" value="RISK" />
                    <el-option label="审计指标" value="AUDIT" />
                    <el-option label="其他" value="OTHER" />
                  </el-select>
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
                <div class="param-list">
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
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>

    <!-- 底部操作栏 -->
    <div slot="footer" class="dialog-footer">
      <div class="footer-left">
        <el-button size="small" @click="resetAll">
          <i class="el-icon-refresh"></i> 重置
        </el-button>
        <el-button size="small" @click="saveAsDraft">
          <i class="el-icon-document"></i> 保存草稿
        </el-button>
      </div>
      <div class="footer-right">
        <el-button size="small" @click="handleClose">取消</el-button>
        <el-button type="primary" size="small" @click="confirmSave" :loading="saving">
          <i class="el-icon-check"></i> 确定创建
        </el-button>
      </div>
    </div>

    <!-- SQL预览对话框 -->
    <el-dialog
      title="SQL预览"
      :visible.sync="sqlPreviewVisible"
      width="60%"
      :append-to-body="true"
      :z-index="3100"
    >
      <div class="sql-preview-dialog">
        <pre class="sql-code">{{ generatedSQL }}</pre>
      </div>
      <div slot="footer">
        <el-button @click="sqlPreviewVisible = false">关闭</el-button>
        <el-button type="primary" @click="copySQL">复制</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog
      title="使用帮助"
      :visible.sync="helpVisible"
      width="50%"
      :append-to-body="true"
      :z-index="3100"
    >
      <div class="help-content">
        <h4>拖拽操作</h4>
        <ul>
          <li>从左侧字段库拖拽字段到中间的SQL构建器</li>
          <li>也可以直接点击字段添加到SELECT子句</li>
          <li>支持拖拽排序和删除操作</li>
        </ul>
        
        <h4>SQL构建</h4>
        <ul>
          <li>SELECT: 选择要查询的字段</li>
          <li>FROM: 指定数据来源表</li>
          <li>WHERE: 添加过滤条件</li>
          <li>GROUP BY: 设置分组字段</li>
          <li>ORDER BY: 设置排序字段</li>
        </ul>
        
        <h4>快捷操作</h4>
        <ul>
          <li>Ctrl+S: 保存指标</li>
          <li>Ctrl+R: 重置所有内容</li>
          <li>F5: 预览SQL</li>
        </ul>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="helpVisible = false">知道了</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import draggable from 'vuedraggable'
import { getDataSourceList } from '@/api/mxgl'
// 引入z-index层级管理样式
import './dialog-z-index.css'

export default {
  name: 'DragSQLBuilderDialog',
  components: {
    draggable
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    initialData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: false,
      
      // 指标数据
      indicatorData: {
        indicatorName: '',
        indicatorCode: '',
        description: '',
        category: '',
        parameters: []
      },
      
      // 字段库相关
      fieldSearchKeyword: '',
      activeFieldTab: 'common',
      selectedDataSource: '',
      selectedTable: '',
      dataSourceList: [],
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
      
      // SQL模板
      sqlTemplates: [
        {
          templateId: 'T001',
          templateName: '基础查询模板',
          description: '简单的SELECT查询模板',
          sqlContent: 'SELECT COLUMN1, COLUMN2 FROM TABLE_NAME WHERE CONDITION'
        },
        {
          templateId: 'T002',
          templateName: '聚合查询模板',
          description: '包含GROUP BY的聚合查询',
          sqlContent: 'SELECT COLUMN1, SUM(COLUMN2) FROM TABLE_NAME GROUP BY COLUMN1'
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
      helpVisible: false,
      
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
      return this.sqlBuilder.selectFields.length > 0 || this.sqlBuilder.fromClause
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val) {
          this.initializeData()
        }
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  mounted() {
    this.loadDataSources()
    this.loadAvailableTables()
    this.bindKeyboardShortcuts()
  },
  beforeDestroy() {
    this.unbindKeyboardShortcuts()
  },
  methods: {
    // 初始化数据
    initializeData() {
      if (this.initialData) {
        this.indicatorData = { ...this.indicatorData, ...this.initialData }
      }
    },

    // 加载数据源列表
    async loadDataSources() {
      try {
        const response = await getDataSourceList({
          pageNum: 1,
          pageSize: 1000,
          status: 'ACTIVE'
        })
        if (response.code === 1) {
          this.dataSourceList = response.data?.records || []
        } else {
          this.$message.error(response.msg || '加载数据源失败')
        }
      } catch (error) {
        console.error('加载数据源失败:', error)
        this.$message.error('加载数据源失败')
      }
    },

    // 数据源变化时加载对应的表
    async loadDataSourceTables() {
      if (!this.selectedDataSource) {
        this.availableTables = []
        this.selectedTable = ''
        this.tableFields = []
        return
      }

      // 这里应该调用API获取指定数据源的表列表
      // 暂时使用模拟数据
      this.availableTables = [
        { tableName: 'TBL_ENTERPRISE_INFO', tableComment: '企业基本信息表' },
        { tableName: 'TBL_FINANCIAL_DATA', tableComment: '财务数据表' },
        { tableName: 'TBL_RISK_ASSESSMENT', tableComment: '风险评估表' },
        { tableName: 'TBL_AUDIT_PROJECT', tableComment: '审计项目表' }
      ]
      this.selectedTable = ''
      this.tableFields = []
    },

    // 加载可用表
    async loadAvailableTables() {
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

    // 点击添加字段到构建器
    addFieldToBuilder(field, source = 'common') {
      let fieldExpression = ''

      if (source === 'table') {
        fieldExpression = `${this.selectedTable}.${field.columnName}`
      } else {
        fieldExpression = field.fieldCode
      }

      this.sqlBuilder.selectFields.push({
        expression: fieldExpression,
        alias: field.fieldName || field.columnComment || ''
      })

      this.$message.success(`已添加字段: ${field.fieldName || field.columnComment}`)
    },

    // 点击添加函数到构建器
    addFunctionToBuilder(func) {
      this.sqlBuilder.selectFields.push({
        expression: func.syntax,
        alias: func.name.toLowerCase() + '_result'
      })

      this.$message.success(`已添加函数: ${func.name}`)
    },

    // 应用SQL模板
    applyTemplate(template) {
      this.$confirm(`确定要应用模板"${template.templateName}"吗？这将清空当前的构建内容。`, '确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.resetSQLBuilder()
        // 简单解析模板SQL（实际项目中可能需要更复杂的解析）
        this.parseTemplateSQL(template.sqlContent)
        this.$message.success('模板应用成功')
      }).catch(() => {})
    },

    // 解析模板SQL
    parseTemplateSQL(sql) {
      // 简单的SQL解析，实际项目中可能需要更复杂的解析逻辑
      const upperSQL = sql.toUpperCase()

      // 解析FROM子句
      const fromMatch = sql.match(/FROM\s+(\w+)/i)
      if (fromMatch) {
        this.sqlBuilder.fromClause = fromMatch[1]
      }

      // 解析SELECT字段（简单处理）
      const selectMatch = sql.match(/SELECT\s+(.+?)\s+FROM/i)
      if (selectMatch) {
        const fields = selectMatch[1].split(',').map(field => field.trim())
        this.sqlBuilder.selectFields = fields.map(field => ({
          expression: field,
          alias: ''
        }))
      }
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

    // SQL构建器操作方法
    addSelectField() {
      this.sqlBuilder.selectFields.push({
        expression: '',
        alias: ''
      })
    },

    removeSelectField(index) {
      this.sqlBuilder.selectFields.splice(index, 1)
    },

    clearSelectFields() {
      this.sqlBuilder.selectFields = []
    },

    addWhereCondition() {
      this.sqlBuilder.whereConditions.push({
        expression: '',
        operator: 'AND'
      })
    },

    removeWhereCondition(index) {
      this.sqlBuilder.whereConditions.splice(index, 1)
    },

    clearWhereConditions() {
      this.sqlBuilder.whereConditions = []
    },

    clearFromClause() {
      this.sqlBuilder.fromClause = ''
    },

    clearGroupByClause() {
      this.sqlBuilder.groupByClause = ''
    },

    clearOrderByClause() {
      this.sqlBuilder.orderByClause = ''
    },

    // 构建SQL
    buildSQL() {
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
        this.$message.success('SQL格式化完成')
      } else {
        this.$message.warning('请先构建SQL语句')
      }
    },

    // 验证SQL
    validateSQL() {
      const sql = this.buildSQL()
      if (!sql) {
        this.$message.warning('请先构建SQL语句')
        return
      }

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

    // 测试SQL
    executeSQL() {
      this.$message.info('SQL测试功能开发中...')
    },

    // 参数管理
    addParameter() {
      this.indicatorData.parameters.push({
        name: '',
        type: 'STRING',
        defaultValue: '',
        description: ''
      })
    },

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
        this.resetAll()
        this.$message.success('已清空所有内容')
      }).catch(() => {})
    },

    // 重置所有
    resetAll() {
      this.resetIndicatorData()
      this.resetSQLBuilder()
    },

    // 重置指标数据
    resetIndicatorData() {
      this.indicatorData = {
        indicatorName: '',
        indicatorCode: '',
        description: '',
        category: '',
        parameters: []
      }
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

    // 保存草稿
    saveAsDraft() {
      this.$message.info('保存草稿功能开发中...')
    },

    // 显示帮助
    showHelp() {
      this.helpVisible = true
    },

    // 确认保存
    async confirmSave() {
      try {
        // 验证基本信息
        if (!this.indicatorData.indicatorName) {
          this.$message.error('请输入指标名称')
          this.activePreviewTab = 'info'
          return
        }

        if (!this.indicatorData.indicatorCode) {
          this.$message.error('请输入指标编码')
          this.activePreviewTab = 'info'
          return
        }

        // 获取SQL内容
        const sqlContent = this.buildSQL()
        if (!sqlContent) {
          this.$message.error('请构建SQL语句')
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

        // 关闭对话框
        this.handleClose()

        this.$message.success('指标创建成功')
      } catch (error) {
        console.error('保存指标失败:', error)
        this.$message.error('保存指标失败')
      } finally {
        this.saving = false
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.$emit('close')
    },

    // 绑定键盘快捷键
    bindKeyboardShortcuts() {
      document.addEventListener('keydown', this.handleKeydown)
    },

    // 解绑键盘快捷键
    unbindKeyboardShortcuts() {
      document.removeEventListener('keydown', this.handleKeydown)
    },

    // 处理键盘事件
    handleKeydown(event) {
      if (!this.dialogVisible) return

      // Ctrl+S: 保存
      if (event.ctrlKey && event.key === 's') {
        event.preventDefault()
        this.confirmSave()
      }

      // Ctrl+R: 重置
      if (event.ctrlKey && event.key === 'r') {
        event.preventDefault()
        this.clearAll()
      }

      // F5: 预览
      if (event.key === 'F5') {
        event.preventDefault()
        this.previewSQL()
      }

      // Esc: 关闭
      if (event.key === 'Escape') {
        this.handleClose()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.drag-sql-builder-dialog {
  // 提高对话框的z-index层级
  ::v-deep .el-dialog__wrapper {
    z-index: 3000 !important;
  }

  ::v-deep .el-dialog {
    margin-top: 2vh !important;
    margin-bottom: 2vh !important;
    height: 96vh;
    display: flex;
    flex-direction: column;
    z-index: 3001 !important;
  }

  ::v-deep .el-dialog__body {
    flex: 1;
    padding: 0;
    overflow: hidden;
  }

  .builder-container {
    height: 100%;
    display: flex;
    flex-direction: column;
    background: #f5f7fa;
  }

  // 顶部工具栏
  .toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 20px;
    background: #fff;
    border-bottom: 1px solid #e4e7ed;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

    .toolbar-left {
      display: flex;
      align-items: center;
      gap: 12px;

      .title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }

    .toolbar-right {
      display: flex;
      gap: 8px;
    }
  }

  // 主要内容区域
  .main-content {
    flex: 1;
    display: flex;
    gap: 12px;
    padding: 12px;
    overflow: hidden;
  }

  // 字段库
  .field-library {
    width: 300px;
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
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        display: flex;
        align-items: center;
        gap: 8px;

        i {
          color: #409eff;
        }
      }
    }

    .field-tabs {
      flex: 1;
      display: flex;
      flex-direction: column;

      ::v-deep .el-tabs__content {
        flex: 1;
        overflow: hidden;
      }

      ::v-deep .el-tab-pane {
        height: 100%;
        display: flex;
        flex-direction: column;
      }
    }

    .table-selector {
      padding: 12px;
      border-bottom: 1px solid #e4e7ed;
    }

    .field-list {
      flex: 1;
      padding: 8px 12px;
      overflow-y: auto;
      max-height: 400px;

      .field-item {
        display: flex;
        align-items: center;
        padding: 10px;
        margin-bottom: 6px;
        border: 1px solid #e4e7ed;
        border-radius: 6px;
        cursor: pointer;
        transition: all 0.3s;
        background: #fff;

        &:hover {
          border-color: #409eff;
          background: #f0f9ff;
          transform: translateY(-1px);
          box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
        }

        &:active {
          transform: translateY(0);
        }

        .field-icon {
          margin-right: 10px;
          color: #909399;
          font-size: 16px;
        }

        .field-info {
          flex: 1;
          min-width: 0;

          .field-name {
            font-size: 13px;
            font-weight: 600;
            color: #303133;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            margin-bottom: 2px;
          }

          .field-code {
            font-size: 11px;
            color: #909399;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            font-family: monospace;
          }
        }

        .field-type {
          margin-left: 8px;
        }
      }
    }

    .function-categories {
      padding: 8px 12px;
      flex: 1;
      overflow-y: auto;

      .function-list {
        .function-item {
          padding: 10px;
          margin-bottom: 6px;
          border: 1px solid #e4e7ed;
          border-radius: 6px;
          cursor: pointer;
          transition: all 0.3s;
          background: #fff;

          &:hover {
            border-color: #409eff;
            background: #f0f9ff;
          }

          .function-name {
            font-size: 13px;
            font-weight: 600;
            color: #409eff;
            margin-bottom: 4px;
          }

          .function-desc {
            font-size: 11px;
            color: #606266;
            margin-bottom: 4px;
          }

          .function-syntax {
            font-size: 10px;
            color: #909399;
            font-family: monospace;
            background: #f5f7fa;
            padding: 2px 6px;
            border-radius: 3px;
          }
        }
      }
    }

    .template-list {
      padding: 8px 12px;
      flex: 1;
      overflow-y: auto;

      .template-item {
        padding: 12px;
        margin-bottom: 8px;
        border: 1px solid #e4e7ed;
        border-radius: 6px;
        cursor: pointer;
        transition: all 0.3s;
        background: #fff;

        &:hover {
          border-color: #409eff;
          background: #f0f9ff;
        }

        .template-name {
          font-size: 13px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 4px;
        }

        .template-desc {
          font-size: 11px;
          color: #606266;
          margin-bottom: 6px;
        }

        .template-preview {
          font-size: 10px;
          color: #909399;
          font-family: monospace;
          background: #f5f7fa;
          padding: 4px 6px;
          border-radius: 3px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
    }
  }

  // SQL构建器
  .sql-builder {
    flex: 1;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
    margin: 0 6px;

    .builder-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px 20px;
      border-bottom: 1px solid #e4e7ed;

      h4 {
        margin: 0;
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        display: flex;
        align-items: center;
        gap: 8px;

        i {
          color: #409eff;
        }
      }

      .builder-stats {
        display: flex;
        gap: 16px;
        font-size: 12px;
        color: #909399;

        span {
          padding: 2px 8px;
          background: #f5f7fa;
          border-radius: 12px;
        }
      }
    }

    .sql-sections {
      flex: 1;
      padding: 16px 20px;
      overflow-y: auto;

      .sql-section {
        margin-bottom: 20px;

        .section-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 12px;

          .section-title {
            font-size: 13px;
            font-weight: 600;
            color: #409eff;
            display: flex;
            align-items: center;
            gap: 6px;
          }

          .section-actions {
            display: flex;
            gap: 4px;
          }
        }

        .drop-zone {
          min-height: 80px;
          border: 2px dashed #e4e7ed;
          border-radius: 6px;
          padding: 12px;
          transition: all 0.3s;
          background: #fafbfc;

          &.drag-over {
            border-color: #409eff;
            background: #f0f9ff;
            box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
          }

          .empty-hint {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 56px;
            color: #909399;
            font-size: 12px;

            i {
              font-size: 20px;
              margin-bottom: 4px;
              opacity: 0.6;
            }

            p {
              margin: 0;
            }
          }

          .field-list {
            .sql-field-item {
              display: flex;
              align-items: center;
              margin-bottom: 10px;
              padding: 8px;
              background: #fff;
              border: 1px solid #e4e7ed;
              border-radius: 4px;

              .field-content {
                flex: 1;
                display: flex;
                align-items: center;
                gap: 8px;

                .as-label {
                  font-size: 12px;
                  color: #909399;
                  font-weight: 600;
                }
              }

              .field-actions {
                margin-left: 8px;
              }
            }
          }

          .condition-list {
            .condition-item {
              display: flex;
              align-items: center;
              margin-bottom: 10px;
              padding: 8px;
              background: #fff;
              border: 1px solid #e4e7ed;
              border-radius: 4px;

              .condition-content {
                flex: 1;
                display: flex;
                align-items: center;
              }

              .condition-actions {
                margin-left: 8px;
              }
            }
          }
        }
      }
    }
  }

  // 预览面板
  .preview-panel {
    width: 350px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;

    .panel-header {
      padding: 16px 20px;
      border-bottom: 1px solid #e4e7ed;

      h4 {
        margin: 0;
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        display: flex;
        align-items: center;
        gap: 8px;

        i {
          color: #409eff;
        }
      }
    }

    .preview-tabs {
      flex: 1;
      display: flex;
      flex-direction: column;

      ::v-deep .el-tabs__content {
        flex: 1;
        overflow: hidden;
        padding: 16px 20px;
      }

      ::v-deep .el-tab-pane {
        height: 100%;
        overflow-y: auto;
      }
    }

    .sql-preview {
      .preview-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;

        span {
          font-size: 12px;
          font-weight: 600;
          color: #303133;
        }

        .preview-actions {
          display: flex;
          gap: 8px;
        }
      }

      .sql-code {
        background: #f5f7fa;
        border: 1px solid #e4e7ed;
        border-radius: 6px;
        padding: 16px;
        font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
        font-size: 11px;
        line-height: 1.6;
        color: #303133;
        white-space: pre-wrap;
        word-break: break-all;
        max-height: 300px;
        overflow-y: auto;
        margin: 0;
      }
    }

    .info-form {
      .el-form-item {
        margin-bottom: 16px;
      }
    }

    .param-config {
      .param-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16px;

        span {
          font-size: 12px;
          font-weight: 600;
          color: #303133;
        }
      }

      .param-list {
        .param-item {
          display: flex;
          gap: 8px;
          margin-bottom: 10px;
          align-items: center;

          .el-input {
            flex: 1;
          }

          .el-select {
            width: 90px;
          }
        }
      }
    }
  }

  // 底部操作栏
  .dialog-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    background: #fff;
    border-top: 1px solid #e4e7ed;

    .footer-left,
    .footer-right {
      display: flex;
      gap: 8px;
    }
  }

  // SQL预览对话框
  .sql-preview-dialog {
    .sql-code {
      background: #f5f7fa;
      border: 1px solid #e4e7ed;
      border-radius: 6px;
      padding: 20px;
      font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
      font-size: 12px;
      line-height: 1.6;
      color: #303133;
      white-space: pre-wrap;
      max-height: 500px;
      overflow-y: auto;
      margin: 0;
    }
  }

  // 帮助内容
  .help-content {
    h4 {
      color: #303133;
      margin: 16px 0 8px 0;
      font-size: 14px;
    }

    ul {
      margin: 0 0 16px 0;
      padding-left: 20px;

      li {
        margin-bottom: 6px;
        line-height: 1.5;
        color: #606266;
        font-size: 13px;
      }
    }
  }
}

// 全局样式，确保对话框在最顶层
::v-deep .el-dialog__wrapper.drag-sql-builder-dialog {
  z-index: 3000 !important;
}

::v-deep .drag-sql-builder-dialog .el-dialog {
  z-index: 3001 !important;
}

// 确保子对话框也在正确的层级
::v-deep .el-dialog__wrapper {
  &[aria-label="SQL预览"],
  &[aria-label="使用帮助"] {
    z-index: 3100 !important;
  }
}</style>
