<template>
  <el-dialog
    title="表达式可视化编辑器"
    :visible.sync="dialogVisible"
    width="1800px"
    :close-on-click-modal="false"
    @close="handleClose"
    class="expression-editor-dialog"
  >
    <div class="editor-container">
      <!-- 工具栏 -->
      <div class="editor-toolbar">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-button-group>
              <el-button type="primary" @click="handleNew" icon="el-icon-document">新建</el-button>
              <el-button @click="handleOpenTemplate" icon="el-icon-folder-opened">模板</el-button>
              <el-button @click="handleSave" icon="el-icon-check">保存</el-button>
              <el-button @click="handleValidate" icon="el-icon-circle-check">验证</el-button>
              <el-button @click="handleTest" icon="el-icon-video-play">测试</el-button>
            </el-button-group>
          </el-col>
          <el-col :span="12" style="text-align: right;">
            <el-select v-model="currentTemplate" placeholder="选择模板" style="width: 200px; margin-right: 10px;">
              <el-option
                v-for="template in templateList"
                :key="template.expressionId"
                :label="template.expressionName"
                :value="template.expressionId"
              />
            </el-select>
            <el-button @click="handleFormat" icon="el-icon-magic-stick">格式化</el-button>
            <el-button @click="handleClear" icon="el-icon-delete">清空</el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 主编辑区域 -->
      <div class="editor-main">
        <el-row :gutter="20" style="height: 100%;">
          <!-- 左侧：组件面板 -->
          <el-col :span="6" style="height: 100%;">
            <div class="component-panel">
              <div class="panel-header">
                <h4>表达式组件</h4>
                <el-switch
                  v-model="visualMode"
                  active-text="可视化"
                  inactive-text="代码"
                  @change="handleModeChange"
                />
              </div>
              
              <div class="panel-content" v-if="visualMode">
                <!-- 中文公式模板 -->
                <div class="component-group" v-if="chineseFormulaMode">
                  <h5>中文公式模板</h5>
                  <div class="template-list">
                    <div
                      v-for="template in chineseTemplates"
                      :key="template.templateId"
                      class="template-item"
                      @click="applyChineseTemplate(template)"
                    >
                      <div class="template-header">
                        <span class="template-name">{{ template.templateName }}</span>
                        <el-tag size="mini" type="info">{{ template.templateCategory }}</el-tag>
                      </div>
                      <div class="template-formula">{{ template.chineseFormula }}</div>
                      <div class="template-description">{{ template.description }}</div>
                    </div>
                  </div>
                </div>

                <!-- 数据源字段 -->
                <div class="component-group">
                  <h5>数据源字段</h5>
                  <div class="datasource-selector">
                    <el-select
                      v-model="selectedDataSource"
                      placeholder="选择数据源"
                      size="small"
                      style="width: 100%; margin-bottom: 10px;"
                      @change="handleDataSourceChange"
                    >
                      <el-option
                        v-for="source in dataSourceList"
                        :key="source.sourceId"
                        :label="source.sourceName"
                        :value="source.sourceId"
                      />
                    </el-select>
                    <el-select
                      v-model="selectedTable"
                      placeholder="选择数据表"
                      size="small"
                      style="width: 100%; margin-bottom: 10px;"
                      @change="handleTableChange"
                      :disabled="!selectedDataSource"
                    >
                      <el-option
                        v-for="table in tableList"
                        :key="table.tableName"
                        :label="table.tableName"
                        :value="table.tableName"
                      />
                    </el-select>
                  </div>
                  <div class="field-list" v-if="fieldList.length > 0">
                    <div
                      v-for="field in fieldList"
                      :key="field.columnName"
                      class="field-item"
                      draggable="true"
                      @dragstart="handleFieldDragStart($event, field)"
                      @click="handleFieldClick(field)"
                    >
                      <i class="el-icon-document"></i>
                      <span>{{ field.columnName }}</span>
                      <el-tag size="mini" :type="getFieldTypeTag(field.dataType)">
                        {{ field.dataType }}
                      </el-tag>
                    </div>
                  </div>
                </div>

                <!-- 逻辑组件 -->
                <div class="component-group">
                  <h5>逻辑组件</h5>
                  <div class="component-list">
                    <div
                      v-for="component in logicalComponents"
                      :key="component.type"
                      class="component-item"
                      draggable="true"
                      @dragstart="handleDragStart($event, component)"
                      @click="handleComponentClick(component)"
                    >
                      <i :class="component.icon"></i>
                      <span>{{ component.label }}</span>
                    </div>
                  </div>
                </div>

                <!-- 算术组件 -->
                <div class="component-group">
                  <h5>算术组件</h5>
                  <div class="component-list">
                    <div
                      v-for="component in arithmeticComponents"
                      :key="component.type"
                      class="component-item"
                      draggable="true"
                      @dragstart="handleDragStart($event, component)"
                      @click="handleComponentClick(component)"
                    >
                      <i :class="component.icon"></i>
                      <span>{{ component.label }}</span>
                    </div>
                  </div>
                </div>

                <!-- 比较组件 -->
                <div class="component-group">
                  <h5>比较组件</h5>
                  <div class="component-list">
                    <div
                      v-for="component in comparisonComponents"
                      :key="component.type"
                      class="component-item"
                      draggable="true"
                      @dragstart="handleDragStart($event, component)"
                      @click="handleComponentClick(component)"
                    >
                      <i :class="component.icon"></i>
                      <span>{{ component.label }}</span>
                    </div>
                  </div>
                </div>

                <!-- 函数组件 -->
                <div class="component-group">
                  <h5>函数组件</h5>
                  <div class="component-list">
                    <div
                      v-for="component in functionComponents"
                      :key="component.type"
                      class="component-item"
                      draggable="true"
                      @dragstart="handleDragStart($event, component)"
                      @click="handleComponentClick(component)"
                    >
                      <i :class="component.icon"></i>
                      <span>{{ component.label }}</span>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 代码模式下的辅助面板 -->
              <div class="code-assistant" v-else>
                <h5>代码助手</h5>
                <el-tabs v-model="assistantTab">
                  <el-tab-pane label="语法提示" name="syntax">
                    <div class="syntax-hints">
                      <div v-for="hint in syntaxHints" :key="hint.keyword" class="hint-item">
                        <strong>{{ hint.keyword }}</strong>
                        <p>{{ hint.description }}</p>
                        <code @click="insertText(hint.example)">{{ hint.example }}</code>
                      </div>
                    </div>
                  </el-tab-pane>
                  <el-tab-pane label="常用函数" name="functions">
                    <div class="function-list">
                      <div
                        v-for="func in commonFunctions"
                        :key="func.name"
                        class="function-item"
                        @click="insertText(func.syntax)"
                      >
                        <h6>{{ func.name }}</h6>
                        <p>{{ func.description }}</p>
                        <code>{{ func.syntax }}</code>
                      </div>
                    </div>
                  </el-tab-pane>
                </el-tabs>
              </div>
            </div>
          </el-col>

          <!-- 中间：表达式编辑器 -->
          <el-col :span="12" style="height: 100%;">
            <div class="expression-editor">
              <div class="editor-header">
                <h4>表达式编辑器</h4>
                <div class="editor-actions">
                  <el-button
                    size="mini"
                    :type="chineseFormulaMode ? 'primary' : ''"
                    @click="toggleChineseFormulaMode"
                    icon="el-icon-chat-line-square"
                  >
                    {{ chineseFormulaMode ? '中文模式' : '切换中文' }}
                  </el-button>
                  <el-button size="mini" @click="handleUndo" icon="el-icon-refresh-left">撤销</el-button>
                  <el-button size="mini" @click="handleRedo" icon="el-icon-refresh-right">重做</el-button>
                </div>
              </div>
              
              <!-- 可视化编辑区域 -->
              <div class="visual-editor" v-if="visualMode">
                <div
                  class="drop-zone"
                  @drop="handleDrop"
                  @dragover="handleDragOver"
                  @dragenter="handleDragEnter"
                >
                  <div v-if="visualComponents.length === 0" class="empty-hint">
                    <i class="el-icon-plus"></i>
                    <p>拖拽左侧组件到此处构建表达式</p>
                  </div>
                  <div v-else class="visual-components">
                    <div
                      v-for="(component, index) in visualComponents"
                      :key="index"
                      class="visual-component"
                      :class="{ active: selectedComponentIndex === index }"
                      @click="selectComponent(index)"
                    >
                      <div class="component-content">
                        <i :class="component.icon"></i>
                        <span>{{ component.label }}</span>
                        <el-button
                          size="mini"
                          type="danger"
                          icon="el-icon-close"
                          circle
                          @click.stop="removeComponent(index)"
                        />
                      </div>
                      <div v-if="component.hasParams" class="component-params">
                        <el-input
                          v-for="(param, paramIndex) in component.params"
                          :key="paramIndex"
                          v-model="param.value"
                          :placeholder="param.placeholder"
                          size="mini"
                          style="margin: 2px;"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 代码编辑区域 -->
              <div class="code-editor" v-else>
                <!-- 中文公式编辑器 -->
                <div v-if="chineseFormulaMode" class="chinese-formula-editor">
                  <div class="chinese-editor-toolbar">
                    <el-button-group size="mini">
                      <el-button @click="validateChineseFormula" icon="el-icon-circle-check" :loading="chineseValidationResult.loading">验证</el-button>
                      <el-button @click="parseChineseToSQL" icon="el-icon-refresh" :loading="chineseParseResult.loading">转换SQL</el-button>
                      <el-button @click="formatChineseFormula" icon="el-icon-magic-stick">格式化</el-button>
                    </el-button-group>

                    <!-- 中文运算符快捷按钮 -->
                    <div class="chinese-operators" style="margin-top: 10px;">
                      <el-button
                        v-for="operator in supportedOperators.slice(0, 8)"
                        :key="operator.chinese"
                        size="mini"
                        @click="insertChineseOperator(operator)"
                        style="margin: 2px;"
                      >
                        {{ operator.chinese }}
                      </el-button>
                    </div>
                  </div>

                  <textarea
                    v-model="chineseFormula"
                    class="chinese-formula-textarea"
                    placeholder="请输入中文公式，如：金额 大于 10000 并且 状态 等于 有效"
                    @input="handleChineseFormulaInput"
                  ></textarea>

                  <!-- 中文公式验证结果 -->
                  <div v-if="chineseValidationResult.valid !== null" class="validation-result">
                    <el-alert
                      :type="chineseValidationResult.valid ? 'success' : 'error'"
                      :title="chineseValidationResult.message"
                      :closable="false"
                      show-icon
                    >
                      <div v-if="chineseValidationResult.errors.length > 0">
                        <p><strong>错误：</strong></p>
                        <ul>
                          <li v-for="error in chineseValidationResult.errors" :key="error">{{ error }}</li>
                        </ul>
                      </div>
                      <div v-if="chineseValidationResult.warnings.length > 0">
                        <p><strong>警告：</strong></p>
                        <ul>
                          <li v-for="warning in chineseValidationResult.warnings" :key="warning">{{ warning }}</li>
                        </ul>
                      </div>
                    </el-alert>
                  </div>

                  <!-- 中文公式解析结果 -->
                  <div v-if="chineseParseResult.success !== null" class="parse-result">
                    <el-alert
                      :type="chineseParseResult.success ? 'success' : 'error'"
                      :title="chineseParseResult.message"
                      :closable="false"
                      show-icon
                    >
                      <div v-if="chineseParseResult.success && chineseParseResult.sqlExpression">
                        <p><strong>生成的SQL表达式：</strong></p>
                        <code>{{ chineseParseResult.sqlExpression }}</code>
                      </div>
                    </el-alert>
                  </div>
                </div>

                <!-- 标准SQL编辑器 -->
                <textarea
                  v-else
                  v-model="expressionContent"
                  class="expression-textarea"
                  placeholder="请输入表达式内容或使用左侧可视化构建器..."
                  @input="handleContentChange"
                ></textarea>
              </div>

              <!-- 表达式信息面板 -->
              <div class="expression-info">
                <el-row :gutter="10">
                  <el-col :span="6">
                    <div class="info-item">
                      <span class="info-label">复杂度:</span>
                      <span class="info-value">{{ complexityLevel }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="info-item">
                      <span class="info-label">字符数:</span>
                      <span class="info-value">{{ expressionContent.length }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="info-item">
                      <span class="info-label">状态:</span>
                      <span class="info-value" :class="validationStatusClass">{{ validationStatus }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="info-item">
                      <span class="info-label">输出类型:</span>
                      <span class="info-value">{{ outputType }}</span>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-col>

          <!-- 右侧：属性面板 -->
          <el-col :span="6" style="height: 100%;">
            <div class="property-panel">
              <el-tabs v-model="propertyTab" type="border-card">
                <el-tab-pane label="基本属性" name="basic">
                  <div class="property-content">
                    <el-form :model="expressionForm" label-width="80px" size="small">
                      <el-form-item label="表达式名称">
                        <el-input v-model="expressionForm.expressionName" placeholder="请输入表达式名称" />
                      </el-form-item>
                      <el-form-item label="表达式编码">
                        <el-input v-model="expressionForm.expressionCode" placeholder="请输入表达式编码" />
                      </el-form-item>
                      <el-form-item label="表达式类型">
                        <el-select v-model="expressionForm.expressionType" placeholder="请选择类型">
                          <el-option label="逻辑表达式" value="LOGICAL" />
                          <el-option label="算术表达式" value="ARITHMETIC" />
                          <el-option label="比较表达式" value="COMPARISON" />
                          <el-option label="函数表达式" value="FUNCTION" />
                        </el-select>
                      </el-form-item>
                      <el-form-item label="表达式分类">
                        <el-select v-model="expressionForm.expressionCategory" placeholder="请选择分类">
                          <el-option label="财务审计" value="FINANCIAL_AUDIT" />
                          <el-option label="风险控制" value="RISK_CONTROL" />
                          <el-option label="合规检查" value="COMPLIANCE_CHECK" />
                          <el-option label="数据验证" value="DATA_VALIDATION" />
                        </el-select>
                      </el-form-item>
                      <el-form-item label="输出类型">
                        <el-select v-model="expressionForm.outputType" placeholder="请选择输出类型">
                          <el-option label="布尔值" value="BOOLEAN" />
                          <el-option label="数值" value="NUMBER" />
                          <el-option label="字符串" value="STRING" />
                          <el-option label="日期" value="DATE" />
                        </el-select>
                      </el-form-item>
                      <el-form-item label="描述">
                        <el-input
                          v-model="expressionForm.expressionDescription"
                          type="textarea"
                          :rows="3"
                          placeholder="请输入表达式描述"
                        />
                      </el-form-item>
                    </el-form>
                  </div>
                </el-tab-pane>
                
                <el-tab-pane label="验证结果" name="validation">
                  <div class="validation-content">
                    <div v-if="validationResult.loading" class="loading-container">
                      <el-loading text="正在验证..."></el-loading>
                    </div>
                    <div v-else-if="validationResult.valid" class="success-container">
                      <el-alert
                        title="验证通过"
                        type="success"
                        description="表达式语法正确"
                        show-icon
                        :closable="false"
                      />
                      <div v-if="validationResult.warnings && validationResult.warnings.length > 0">
                        <h6>警告信息:</h6>
                        <ul>
                          <li v-for="warning in validationResult.warnings" :key="warning">
                            {{ warning }}
                          </li>
                        </ul>
                      </div>
                    </div>
                    <div v-else-if="validationResult.errors" class="error-container">
                      <el-alert
                        title="验证失败"
                        type="error"
                        :description="validationResult.message"
                        show-icon
                        :closable="false"
                      />
                      <div v-if="validationResult.errors.length > 0">
                        <h6>错误详情:</h6>
                        <ul>
                          <li v-for="error in validationResult.errors" :key="error">
                            {{ error }}
                          </li>
                        </ul>
                      </div>
                    </div>
                    <div v-else class="empty-container">
                      <el-empty description="请点击验证按钮检查表达式语法" />
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="测试结果" name="test">
                  <div class="test-content">
                    <div v-if="testResult.loading" class="loading-container">
                      <el-loading text="正在测试..."></el-loading>
                    </div>
                    <div v-else-if="testResult.success" class="success-container">
                      <el-alert
                        title="测试成功"
                        type="success"
                        :description="`执行时间: ${testResult.executionTime}ms`"
                        show-icon
                        :closable="false"
                      />
                      <div class="test-result-data">
                        <h6>执行结果:</h6>
                        <pre>{{ JSON.stringify(testResult.result, null, 2) }}</pre>
                      </div>
                    </div>
                    <div v-else-if="testResult.error" class="error-container">
                      <el-alert
                        title="测试失败"
                        type="error"
                        :description="testResult.message"
                        show-icon
                        :closable="false"
                      />
                    </div>
                    <div v-else class="empty-container">
                      <el-empty description="请点击测试按钮执行表达式" />
                    </div>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 模板选择对话框 -->
    <ExpressionTemplateSelectDialog
      :visible.sync="templateSelectVisible"
      @select="handleTemplateSelect"
    />

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleSaveAndClose">保存并关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getTemplateExpressions,
  validateExpressionSyntax,
  testExpressionExecution,
  saveExpression,
  analyzeExpressionComplexity,
  getDataSourcesForExpression,
  getTablesForExpression,
  getTableFieldsForExpression,
  parseChineseFormula,
  validateChineseFormula,
  getSupportedOperators,
  getChineseFormulaTemplates,
  getFormulaSuggestions,
  formatChineseFormula,
  useTemplate,
  getTemplateCategories
} from '@/api/mxgl'
import ExpressionTemplateSelectDialog from './ExpressionTemplateSelectDialog'

export default {
  name: 'ExpressionEditorDialog',
  components: {
    ExpressionTemplateSelectDialog
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      visualMode: true,
      currentTemplate: '',
      templateList: [],
      expressionContent: '',
      complexityLevel: 1,
      outputType: 'BOOLEAN',
      validationStatus: '未验证',
      validationStatusClass: 'status-normal',
      selectedComponentIndex: -1,
      visualComponents: [],

      // 数据源相关
      selectedDataSource: '',
      selectedTable: '',
      dataSourceList: [],
      tableList: [],
      fieldList: [],
      // 表达式表单
      expressionForm: {
        expressionName: '',
        expressionCode: '',
        expressionType: 'LOGICAL',
        expressionCategory: 'FINANCIAL_AUDIT',
        outputType: 'BOOLEAN',
        expressionDescription: ''
      },
      // 组件定义
      logicalComponents: [
        { type: 'and', label: 'AND', icon: 'el-icon-connection', syntax: ' && ', hasParams: false },
        { type: 'or', label: 'OR', icon: 'el-icon-plus', syntax: ' || ', hasParams: false },
        { type: 'not', label: 'NOT', icon: 'el-icon-close', syntax: '!', hasParams: false },
        { type: 'if', label: 'IF', icon: 'el-icon-question', syntax: 'if({condition}) {result}', hasParams: true, params: [
          { name: 'condition', placeholder: '条件', value: '' },
          { name: 'result', placeholder: '结果', value: '' }
        ]}
      ],
      arithmeticComponents: [
        { type: 'add', label: '+', icon: 'el-icon-plus', syntax: ' + ', hasParams: false },
        { type: 'subtract', label: '-', icon: 'el-icon-minus', syntax: ' - ', hasParams: false },
        { type: 'multiply', label: '×', icon: 'el-icon-close', syntax: ' * ', hasParams: false },
        { type: 'divide', label: '÷', icon: 'el-icon-remove', syntax: ' / ', hasParams: false },
        { type: 'mod', label: '%', icon: 'el-icon-refresh', syntax: ' % ', hasParams: false }
      ],
      comparisonComponents: [
        { type: 'eq', label: '=', icon: 'el-icon-check', syntax: ' == ', hasParams: false },
        { type: 'ne', label: '≠', icon: 'el-icon-close', syntax: ' != ', hasParams: false },
        { type: 'gt', label: '>', icon: 'el-icon-caret-right', syntax: ' > ', hasParams: false },
        { type: 'lt', label: '<', icon: 'el-icon-caret-left', syntax: ' < ', hasParams: false },
        { type: 'ge', label: '≥', icon: 'el-icon-d-caret', syntax: ' >= ', hasParams: false },
        { type: 'le', label: '≤', icon: 'el-icon-d-caret', syntax: ' <= ', hasParams: false }
      ],
      functionComponents: [
        { type: 'sum', label: 'SUM', icon: 'el-icon-plus', syntax: 'SUM({values})', hasParams: true, params: [
          { name: 'values', placeholder: '数值列表', value: '' }
        ]},
        { type: 'avg', label: 'AVG', icon: 'el-icon-pie-chart', syntax: 'AVG({values})', hasParams: true, params: [
          { name: 'values', placeholder: '数值列表', value: '' }
        ]},
        { type: 'max', label: 'MAX', icon: 'el-icon-top', syntax: 'MAX({values})', hasParams: true, params: [
          { name: 'values', placeholder: '数值列表', value: '' }
        ]},
        { type: 'min', label: 'MIN', icon: 'el-icon-bottom', syntax: 'MIN({values})', hasParams: true, params: [
          { name: 'values', placeholder: '数值列表', value: '' }
        ]}
      ],
      // 代码助手
      assistantTab: 'syntax',
      syntaxHints: [
        {
          keyword: 'IF',
          description: '条件判断',
          example: 'IF(amount > 1000, "高额", "正常")'
        },
        {
          keyword: 'AND',
          description: '逻辑与',
          example: 'status == "active" && amount > 0'
        },
        {
          keyword: 'OR',
          description: '逻辑或',
          example: 'type == "A" || type == "B"'
        }
      ],
      commonFunctions: [
        {
          name: 'SUM',
          description: '求和函数',
          syntax: 'SUM(field1, field2, ...)'
        },
        {
          name: 'AVG',
          description: '平均值函数',
          syntax: 'AVG(field1, field2, ...)'
        },
        {
          name: 'COUNT',
          description: '计数函数',
          syntax: 'COUNT(field)'
        }
      ],
      // 面板状态
      propertyTab: 'basic',
      validationResult: {
        loading: false,
        valid: null,
        message: '',
        warnings: [],
        errors: []
      },
      testResult: {
        loading: false,
        success: null,
        message: '',
        result: null,
        executionTime: 0
      },
      // 对话框状态
      templateSelectVisible: false,

      // 中文公式相关
      chineseFormulaMode: false,
      chineseFormula: '',
      supportedOperators: [],
      chineseTemplates: [],
      formulaSuggestions: [],
      showSuggestions: false,
      suggestionKeyword: '',
      chineseValidationResult: {
        loading: false,
        valid: null,
        message: '',
        warnings: [],
        errors: []
      },
      chineseParseResult: {
        loading: false,
        success: null,
        sqlExpression: '',
        fieldReferences: [],
        message: ''
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadTemplateList()
      }
    },
    expressionContent() {
      this.analyzeComplexity()
    }
  },
  mounted() {
    this.loadDataSources()
    this.loadSupportedOperators()
    this.loadChineseTemplates()
  },
  methods: {
    // 加载数据源列表
    async loadDataSources() {
      try {
        const response = await getDataSourcesForExpression()
        if (response.code === 1) {
          this.dataSourceList = response.data || []
        }
      } catch (error) {
        console.error('获取数据源列表失败:', error)
      }
    },

    // 数据源变化处理
    async handleDataSourceChange(dataSourceId) {
      this.selectedDataSource = dataSourceId
      this.selectedTable = ''
      this.tableList = []
      this.fieldList = []

      if (dataSourceId) {
        try {
          const response = await getTablesForExpression(dataSourceId)
          if (response.code === 1) {
            this.tableList = response.data || []
          }
        } catch (error) {
          console.error('获取表列表失败:', error)
        }
      }
    },

    // 表变化处理
    async handleTableChange(tableName) {
      this.selectedTable = tableName
      this.fieldList = []

      if (tableName && this.selectedDataSource) {
        try {
          const response = await getTableFieldsForExpression({
            dataSourceId: this.selectedDataSource,
            tableName: tableName
          })
          if (response.code === 1) {
            this.fieldList = response.data || []
          }
        } catch (error) {
          console.error('获取字段列表失败:', error)
        }
      }
    },

    // 字段拖拽开始
    handleFieldDragStart(event, field) {
      event.dataTransfer.setData('application/json', JSON.stringify({
        type: 'FIELD',
        field: field
      }))
    },

    // 字段点击
    handleFieldClick(field) {
      // 在光标位置插入字段
      const fieldText = field.columnName
      this.insertTextAtCursor(fieldText)
    },

    // 获取字段类型标签样式
    getFieldTypeTag(dataType) {
      const type = dataType.toLowerCase()
      if (type.includes('varchar') || type.includes('char') || type.includes('text')) {
        return 'success'
      } else if (type.includes('int') || type.includes('number') || type.includes('decimal')) {
        return 'warning'
      } else if (type.includes('date') || type.includes('time')) {
        return 'info'
      } else {
        return ''
      }
    },

    // 在光标位置插入文本
    insertTextAtCursor(text) {
      const textarea = this.$el.querySelector('.expression-textarea')
      if (textarea) {
        const start = textarea.selectionStart
        const end = textarea.selectionEnd
        const value = textarea.value
        const newValue = value.substring(0, start) + text + value.substring(end)
        this.expressionContent = newValue

        // 设置光标位置
        this.$nextTick(() => {
          textarea.focus()
          textarea.setSelectionRange(start + text.length, start + text.length)
        })
      }
    },

    // 加载模板列表
    async loadTemplateList() {
      try {
        const response = await getTemplateExpressions({
          isEnabled: 'Y'
        })
        if (response.code === 1) {
          this.templateList = response.data || []
        }
      } catch (error) {
        console.error('获取模板列表失败:', error)
      }
    },

    // 新建表达式
    handleNew() {
      this.expressionContent = ''
      this.visualComponents = []
      this.expressionForm = {
        expressionName: '',
        expressionCode: '',
        expressionType: 'LOGICAL',
        expressionCategory: 'FINANCIAL_AUDIT',
        outputType: 'BOOLEAN',
        expressionDescription: ''
      }
      this.validationResult = { loading: false, valid: null, message: '', warnings: [], errors: [] }
      this.testResult = { loading: false, success: null, message: '', result: null, executionTime: 0 }
    },

    // 打开模板
    handleOpenTemplate() {
      this.templateSelectVisible = true
    },

    // 模板选择
    handleTemplateSelect(template) {
      this.expressionContent = template.expressionContent
      this.expressionForm.expressionType = template.expressionType
      this.expressionForm.expressionCategory = template.expressionCategory
      this.expressionForm.outputType = template.outputType
    },

    // 保存表达式
    async handleSave() {
      if (!this.expressionForm.expressionName || !this.expressionForm.expressionCode) {
        this.$message.warning('请填写表达式名称和编码')
        return
      }

      if (!this.expressionContent) {
        this.$message.warning('请输入表达式内容')
        return
      }

      try {
        const saveData = {
          ...this.expressionForm,
          expressionContent: this.expressionContent,
          visualConfig: JSON.stringify(this.visualComponents),
          complexityLevel: this.complexityLevel
        }

        const response = await saveExpression(saveData)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.$emit('refresh')
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败')
        console.error('保存表达式失败:', error)
      }
    },

    // 验证表达式
    async handleValidate() {
      if (!this.expressionContent) {
        this.$message.warning('请输入表达式内容')
        return
      }

      this.validationResult.loading = true
      this.propertyTab = 'validation'

      try {
        const response = await validateExpressionSyntax({
          expressionContent: this.expressionContent,
          expressionType: this.expressionForm.expressionType
        })

        if (response.code === 1) {
          this.validationResult = {
            loading: false,
            valid: response.data.valid,
            message: response.data.message,
            warnings: response.data.warnings || [],
            errors: response.data.errors || []
          }
          
          this.validationStatus = response.data.valid ? '验证通过' : '验证失败'
          this.validationStatusClass = response.data.valid ? 'status-success' : 'status-error'
        } else {
          this.validationResult = {
            loading: false,
            valid: false,
            message: response.msg || '验证失败',
            warnings: [],
            errors: []
          }
          this.validationStatus = '验证失败'
          this.validationStatusClass = 'status-error'
        }
      } catch (error) {
        this.validationResult = {
          loading: false,
          valid: false,
          message: '验证失败: ' + error.message,
          warnings: [],
          errors: []
        }
        this.validationStatus = '验证失败'
        this.validationStatusClass = 'status-error'
      }
    },

    // 测试表达式
    async handleTest() {
      if (!this.expressionContent) {
        this.$message.warning('请输入表达式内容')
        return
      }

      this.testResult.loading = true
      this.propertyTab = 'test'

      try {
        // 这里需要先保存表达式才能测试，或者使用临时测试接口
        const response = await testExpressionExecution({
          expressionContent: this.expressionContent,
          expressionType: this.expressionForm.expressionType,
          testData: {}
        })

        if (response.code === 1) {
          this.testResult = {
            loading: false,
            success: response.data.success,
            message: response.data.message,
            result: response.data.result,
            executionTime: response.data.executionTime
          }
        } else {
          this.testResult = {
            loading: false,
            success: false,
            message: response.msg || '测试失败',
            result: null,
            executionTime: 0
          }
        }
      } catch (error) {
        this.testResult = {
          loading: false,
          success: false,
          message: '测试失败: ' + error.message,
          result: null,
          executionTime: 0
        }
      }
    },

    // 格式化表达式
    handleFormat() {
      // TODO: 实现表达式格式化
      this.$message.info('格式化功能待实现')
    },

    // 清空表达式
    handleClear() {
      this.expressionContent = ''
      this.visualComponents = []
    },

    // 模式切换
    handleModeChange(visual) {
      this.visualMode = visual
      if (visual) {
        // 切换到可视化模式时，解析代码生成可视化组件
        this.parseCodeToVisual()
      } else {
        // 切换到代码模式时，生成代码
        this.generateCodeFromVisual()
      }
    },

    // 组件拖拽开始
    handleDragStart(event, component) {
      event.dataTransfer.setData('application/json', JSON.stringify({
        type: 'COMPONENT',
        component: component
      }))
    },

    // 组件点击
    handleComponentClick(component) {
      this.insertText(component.syntax)
    },

    // 拖拽放置
    handleDrop(event) {
      event.preventDefault()
      try {
        const dragData = JSON.parse(event.dataTransfer.getData('application/json'))

        if (dragData.type === 'FIELD') {
          // 处理字段拖拽
          const field = dragData.field
          if (this.visualMode) {
            // 在可视化模式下添加字段组件
            this.visualComponents.push({
              type: 'FIELD',
              label: field.columnName,
              icon: 'el-icon-document',
              syntax: field.columnName,
              fieldInfo: field
            })
            this.generateCodeFromVisual()
          } else {
            // 在代码模式下直接插入字段名
            this.insertTextAtCursor(field.columnName)
          }
        } else if (dragData.type === 'COMPONENT') {
          // 处理组件拖拽
          const component = dragData.component
          this.visualComponents.push({ ...component })
          this.generateCodeFromVisual()
        }
      } catch (error) {
        console.error('拖拽数据解析失败:', error)
      }
    },

    // 拖拽悬停
    handleDragOver(event) {
      event.preventDefault()
    },

    // 拖拽进入
    handleDragEnter(event) {
      event.preventDefault()
    },

    // 选择组件
    selectComponent(index) {
      this.selectedComponentIndex = index
    },

    // 移除组件
    removeComponent(index) {
      this.visualComponents.splice(index, 1)
      this.generateCodeFromVisual()
    },

    // 插入文本
    insertText(text) {
      const textarea = this.$el.querySelector('.expression-textarea')
      if (textarea) {
        const start = textarea.selectionStart
        const end = textarea.selectionEnd
        const before = this.expressionContent.substring(0, start)
        const after = this.expressionContent.substring(end)
        this.expressionContent = before + text + after
        
        this.$nextTick(() => {
          textarea.focus()
          textarea.setSelectionRange(start + text.length, start + text.length)
        })
      }
    },

    // 内容变化
    handleContentChange() {
      this.validationResult.valid = null
      this.validationStatus = '未验证'
      this.validationStatusClass = 'status-normal'
    },

    // 解析代码生成可视化组件
    parseCodeToVisual() {
      // TODO: 实现代码解析
    },

    // 从可视化组件生成代码
    generateCodeFromVisual() {
      if (this.visualComponents.length === 0) {
        this.expressionContent = ''
        return
      }

      let code = ''
      this.visualComponents.forEach((component, index) => {
        if (component.hasParams) {
          let syntax = component.syntax
          component.params.forEach(param => {
            syntax = syntax.replace(`{${param.name}}`, param.value || param.placeholder)
          })
          code += syntax
        } else {
          code += component.syntax
        }
        
        if (index < this.visualComponents.length - 1) {
          code += ' '
        }
      })
      
      this.expressionContent = code
    },

    // 分析复杂度
    async analyzeComplexity() {
      if (!this.expressionContent) {
        this.complexityLevel = 1
        return
      }

      try {
        const response = await analyzeExpressionComplexity({
          expressionContent: this.expressionContent
        })
        if (response.code === 1) {
          this.complexityLevel = response.data.complexityLevel || 1
        }
      } catch (error) {
        console.error('分析复杂度失败:', error)
      }
    },

    // 撤销
    handleUndo() {
      // TODO: 实现撤销功能
      this.$message.info('撤销功能待实现')
    },

    // 重做
    handleRedo() {
      // TODO: 实现重做功能
      this.$message.info('重做功能待实现')
    },

    // 保存并关闭
    async handleSaveAndClose() {
      await this.handleSave()
      this.handleClose()
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.handleNew()
      this.$emit('refresh')
    },

    // =====================================================
    // 中文公式相关方法
    // =====================================================

    // 加载支持的运算符
    async loadSupportedOperators() {
      try {
        const response = await getSupportedOperators()
        if (response.code === 1) {
          this.supportedOperators = response.data || []
        }
      } catch (error) {
        console.error('获取运算符列表失败:', error)
      }
    },

    // 加载中文模板
    async loadChineseTemplates() {
      try {
        const response = await getChineseFormulaTemplates()
        if (response.code === 1) {
          this.chineseTemplates = response.data || []
        }
      } catch (error) {
        console.error('获取中文模板失败:', error)
      }
    },

    // 切换中文公式模式
    toggleChineseFormulaMode() {
      this.chineseFormulaMode = !this.chineseFormulaMode
      if (this.chineseFormulaMode) {
        // 切换到中文模式时，尝试解析现有表达式
        if (this.expressionContent) {
          this.chineseFormula = this.expressionContent
        }
      } else {
        // 切换到SQL模式时，解析中文公式
        if (this.chineseFormula) {
          this.parseChineseToSQL()
        }
      }
    },

    // 解析中文公式为SQL
    async parseChineseToSQL() {
      if (!this.chineseFormula) {
        return
      }

      this.chineseParseResult.loading = true

      try {
        const response = await parseChineseFormula({
          chineseFormula: this.chineseFormula,
          sourceDataConfig: JSON.stringify({
            dataSourceId: this.selectedDataSource,
            tableName: this.selectedTable
          }),
          targetDataConfig: ''
        })

        if (response.code === 1) {
          this.chineseParseResult = {
            loading: false,
            success: response.data.success,
            sqlExpression: response.data.sqlExpression,
            fieldReferences: response.data.fieldReferences || [],
            message: response.data.message
          }

          if (response.data.success) {
            this.expressionContent = response.data.sqlExpression
            this.$message.success('中文公式解析成功')
          } else {
            this.$message.error(response.data.message)
          }
        } else {
          this.chineseParseResult = {
            loading: false,
            success: false,
            sqlExpression: '',
            fieldReferences: [],
            message: response.msg || '解析失败'
          }
          this.$message.error(response.msg || '解析失败')
        }
      } catch (error) {
        this.chineseParseResult = {
          loading: false,
          success: false,
          sqlExpression: '',
          fieldReferences: [],
          message: '解析失败: ' + error.message
        }
        this.$message.error('解析失败')
        console.error('解析中文公式失败:', error)
      }
    },

    // 验证中文公式
    async validateChineseFormula() {
      if (!this.chineseFormula) {
        this.$message.warning('请输入中文公式')
        return
      }

      this.chineseValidationResult.loading = true

      try {
        const response = await validateChineseFormula({
          chineseFormula: this.chineseFormula
        })

        if (response.code === 1) {
          this.chineseValidationResult = {
            loading: false,
            valid: response.data.valid,
            message: response.data.message,
            warnings: response.data.warnings || [],
            errors: response.data.errors || []
          }

          if (response.data.valid) {
            this.$message.success('中文公式语法正确')
          } else {
            this.$message.error('中文公式语法错误')
          }
        } else {
          this.chineseValidationResult = {
            loading: false,
            valid: false,
            message: response.msg || '验证失败',
            warnings: [],
            errors: []
          }
          this.$message.error(response.msg || '验证失败')
        }
      } catch (error) {
        this.chineseValidationResult = {
          loading: false,
          valid: false,
          message: '验证失败: ' + error.message,
          warnings: [],
          errors: []
        }
        this.$message.error('验证失败')
        console.error('验证中文公式失败:', error)
      }
    },

    // 格式化中文公式
    async formatChineseFormula() {
      if (!this.chineseFormula) {
        this.$message.warning('请输入中文公式')
        return
      }

      try {
        const response = await formatChineseFormula({
          chineseFormula: this.chineseFormula
        })

        if (response.code === 1) {
          this.chineseFormula = response.data.formattedFormula
          this.$message.success('格式化成功')
        } else {
          this.$message.error(response.msg || '格式化失败')
        }
      } catch (error) {
        this.$message.error('格式化失败')
        console.error('格式化中文公式失败:', error)
      }
    },

    // 获取智能建议
    async getFormulaSuggestions(partialFormula) {
      try {
        const availableFields = this.fieldList.map(field => field.columnName)
        const response = await getFormulaSuggestions({
          partialFormula: partialFormula,
          availableFields: availableFields
        })

        if (response.code === 1) {
          this.formulaSuggestions = response.data || []
          this.showSuggestions = this.formulaSuggestions.length > 0
        }
      } catch (error) {
        console.error('获取智能建议失败:', error)
      }
    },

    // 插入中文运算符
    insertChineseOperator(operator) {
      const operatorText = ` ${operator.chinese} `
      this.insertChineseText(operatorText)
    },

    // 插入中文字段
    insertChineseField(field) {
      const fieldText = `【${field.columnName}】`
      this.insertChineseText(fieldText)
    },

    // 在中文公式中插入文本
    insertChineseText(text) {
      const textarea = this.$el.querySelector('.chinese-formula-textarea')
      if (textarea) {
        const start = textarea.selectionStart
        const end = textarea.selectionEnd
        const before = this.chineseFormula.substring(0, start)
        const after = this.chineseFormula.substring(end)
        this.chineseFormula = before + text + after

        this.$nextTick(() => {
          textarea.focus()
          textarea.setSelectionRange(start + text.length, start + text.length)
        })
      }
    },

    // 应用中文模板
    async applyChineseTemplate(template) {
      try {
        this.chineseFormula = template.chineseFormula

        // 调用API增加使用次数
        await useTemplate(template.templateId)

        this.$message.success(`已应用模板: ${template.templateName}`)
      } catch (error) {
        console.error('应用模板失败:', error)
        this.chineseFormula = template.chineseFormula
        this.$message.success(`已应用模板: ${template.templateName}`)
      }
    },

    // 处理中文公式输入
    handleChineseFormulaInput() {
      // 清除之前的验证结果
      this.chineseValidationResult.valid = null
      this.chineseParseResult.success = null

      // 实时获取智能建议
      if (this.chineseFormula.length > 2) {
        this.getFormulaSuggestions(this.chineseFormula)
      }
    }
  }
}
</script>

<style scoped>
.expression-editor-dialog {
  height: 90vh;
}

.editor-container {
  height: 80vh;
  display: flex;
  flex-direction: column;
}

.editor-toolbar {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.editor-main {
  flex: 1;
  height: calc(100% - 80px);
}

.component-panel,
.expression-editor,
.property-panel {
  height: 100%;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background: #fff;
}

.panel-header,
.editor-header {
  padding: 15px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-header h4,
.editor-header h4 {
  margin: 0;
}

.panel-content {
  padding: 15px;
  height: calc(100% - 60px);
  overflow-y: auto;
}

.component-group {
  margin-bottom: 20px;
}

.component-group h5 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 14px;
}

.component-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
}

.component-item {
  padding: 8px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 12px;
}

.component-item:hover {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.component-item i {
  display: block;
  margin-bottom: 4px;
  font-size: 16px;
  color: #409eff;
}

.visual-editor {
  height: calc(100% - 120px);
  padding: 15px;
}

.drop-zone {
  width: 100%;
  height: 100%;
  border: 2px dashed #e4e7ed;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 200px;
}

.empty-hint {
  text-align: center;
  color: #909399;
}

.empty-hint i {
  font-size: 48px;
  margin-bottom: 10px;
}

.visual-components {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding: 20px;
}

.visual-component {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 10px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
}

.visual-component:hover,
.visual-component.active {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.component-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.component-content i {
  color: #409eff;
}

.component-params {
  margin-top: 8px;
}

.code-editor {
  height: calc(100% - 120px);
  padding: 15px;
}

.expression-textarea {
  width: 100%;
  height: 100%;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 10px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
  resize: none;
  outline: none;
}

.expression-info {
  padding: 10px 15px;
  border-top: 1px solid #e4e7ed;
  background-color: #f9f9f9;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-label {
  font-size: 12px;
  color: #909399;
  margin-right: 5px;
}

.info-value {
  font-size: 12px;
  font-weight: bold;
}

.status-success {
  color: #67c23a;
}

.status-error {
  color: #f56c6c;
}

.status-normal {
  color: #909399;
}

.property-panel {
  height: 100%;
}

.property-content,
.validation-content,
.test-content {
  padding: 15px;
  height: calc(100% - 40px);
  overflow-y: auto;
}

.syntax-hints .hint-item {
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.syntax-hints code {
  display: block;
  margin-top: 5px;
  padding: 5px;
  background-color: #f5f7fa;
  border-radius: 2px;
  font-size: 12px;
  cursor: pointer;
}

.function-list .function-item {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  cursor: pointer;
}

.function-item:hover {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.function-item h6 {
  margin: 0 0 5px 0;
  color: #303133;
}

.function-item p {
  margin: 0 0 5px 0;
  font-size: 12px;
  color: #909399;
}

.function-item code {
  font-size: 12px;
  color: #409eff;
}

.test-result-data pre {
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  font-size: 12px;
  max-height: 200px;
  overflow-y: auto;
}

.dialog-footer {
  text-align: right;
}

/* 数据源字段样式 */
.datasource-selector {
  margin-bottom: 15px;
}

.field-list {
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 5px;
}

.field-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 8px;
  margin-bottom: 3px;
  border: 1px solid #f0f0f0;
  border-radius: 3px;
  cursor: pointer;
  transition: all 0.3s;
  background-color: #fafafa;
}

.field-item:hover {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.field-item i {
  margin-right: 6px;
  color: #909399;
}

.field-item span {
  flex: 1;
  font-size: 12px;
  color: #303133;
}

.component-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  margin-bottom: 5px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  background-color: #fff;
}

.component-item:hover {
  border-color: #409eff;
  background-color: #ecf5ff;
}

/* 中文公式编辑器样式 */
.chinese-formula-editor {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.chinese-editor-toolbar {
  padding: 10px;
  border-bottom: 1px solid #e4e7ed;
  background-color: #f5f7fa;
}

.chinese-operators {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.chinese-formula-textarea {
  flex: 1;
  width: 100%;
  border: none;
  border-bottom: 1px solid #e4e7ed;
  padding: 15px;
  font-family: 'Microsoft YaHei', sans-serif;
  font-size: 14px;
  line-height: 1.6;
  resize: none;
  outline: none;
  background-color: #fff;
}

.chinese-formula-textarea:focus {
  border-bottom-color: #409eff;
}

.validation-result,
.parse-result {
  padding: 10px;
  border-top: 1px solid #e4e7ed;
}

.validation-result code,
.parse-result code {
  display: block;
  margin-top: 8px;
  padding: 8px;
  background-color: #f5f7fa;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  word-break: break-all;
}

/* 中文模板样式 */
.template-list {
  max-height: 300px;
  overflow-y: auto;
}

.template-item {
  padding: 12px;
  margin-bottom: 8px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
  background-color: #fff;
}

.template-item:hover {
  border-color: #409eff;
  background-color: #ecf5ff;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.template-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.template-name {
  font-weight: bold;
  color: #303133;
  font-size: 13px;
}

.template-formula {
  font-size: 12px;
  color: #409eff;
  background-color: #f0f9ff;
  padding: 6px 8px;
  border-radius: 4px;
  margin-bottom: 6px;
  font-family: 'Microsoft YaHei', sans-serif;
  line-height: 1.4;
}

.template-description {
  font-size: 11px;
  color: #909399;
  line-height: 1.3;
}

/* 中文运算符按钮样式 */
.chinese-operators .el-button--mini {
  padding: 4px 8px;
  font-size: 11px;
  border-radius: 12px;
}

.chinese-operators .el-button--mini:hover {
  background-color: #409eff;
  color: #fff;
  border-color: #409eff;
}
</style>
