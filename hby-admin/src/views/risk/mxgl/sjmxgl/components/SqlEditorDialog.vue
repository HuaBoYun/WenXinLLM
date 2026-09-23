<template>
  <div>
    <el-dialog
      title="SQL可视化编辑器"
      :visible.sync="dialogVisible"
      width="1800px"
      :close-on-click-modal="false"
      :modal="true"
      :modal-append-to-body="true"
      :append-to-body="true"
      :lock-scroll="true"
      custom-class="sql-editor-dialog"
      @close="handleClose"
    >
    <div class="sql-editor-container">
      <!-- 工具栏 -->
      <div class="editor-toolbar">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-button-group>
              <el-button type="primary" @click="handleNewSql" icon="el-icon-document">新建</el-button>
              <el-button @click="handleOpenTemplate" icon="el-icon-folder-opened">模板</el-button>
              <el-button @click="handleImportModelSql" icon="el-icon-download">导入SQL</el-button>
              <el-button @click="handleSave" icon="el-icon-check">保存</el-button>
              <el-button @click="handleExecute" icon="el-icon-video-play">执行</el-button>
            </el-button-group>
          </el-col>
          <el-col :span="12" style="text-align: right;">
            <el-select v-model="currentDataSource" placeholder="选择数据源" style="width: 200px; margin-right: 10px;">
              <el-option
                v-for="ds in dataSourceList"
                :key="ds.sourceId"
                :label="ds.sourceName"
                :value="ds.sourceId"
              />
            </el-select>
            <el-button @click="handleFormat" icon="el-icon-magic-stick" title="格式化SQL (Ctrl+Shift+F)">格式化</el-button>
            <el-button @click="handleValidate" icon="el-icon-circle-check" title="验证SQL语法 (Ctrl+Shift+V)">验证</el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 主编辑区域 -->
      <div class="editor-main">
        <el-row :gutter="20" style="height: 100%;">
          <!-- 左侧：可视化构建器 -->
          <el-col :span="8" style="height: 100%;">
            <div class="visual-builder">
              <div class="builder-header">
                <h4>可视化构建器</h4>
                <el-switch
                  v-model="visualMode"
                  active-text="可视化"
                  inactive-text="代码"
                  @change="handleModeChange"
                />
              </div>
              
              <div class="builder-content" v-if="visualMode">
                <!-- 表选择区域 -->
                <div class="table-selector">
                  <h5>数据表</h5>
                  <el-tree
                    :data="tableTreeData"
                    :props="treeProps"
                    node-key="id"
                    :default-expand-all="false"
                    @node-click="handleTableNodeClick"
                    @node-contextmenu="handleTableNodeRightClick"
                  >
                    <span class="custom-tree-node" slot-scope="{ node, data }">
                      <span @click="handleTableNameClick(data)" class="node-label">
                        <i :class="getTreeNodeIcon(data.type)"></i>
                        {{ node.label }}
                      </span>
                      <span v-if="data.type === 'column'">
                        <el-tag size="mini" type="info">{{ data.dataType }}</el-tag>
                      </span>
                      <span v-if="data.type === 'table'" class="table-actions" @click.stop>
                        <el-dropdown @command="(command) => handleTableAction(command, data)" trigger="click" size="mini">
                          <el-button type="text" size="mini" icon="el-icon-more" @click.stop></el-button>
                          <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="select">
                              <i class="el-icon-search"></i> SELECT查询
                            </el-dropdown-item>
                            <el-dropdown-item command="insert">
                              <i class="el-icon-plus"></i> INSERT插入
                            </el-dropdown-item>
                            <el-dropdown-item command="update">
                              <i class="el-icon-edit"></i> UPDATE更新
                            </el-dropdown-item>
                            <el-dropdown-item command="delete">
                              <i class="el-icon-delete"></i> DELETE删除
                            </el-dropdown-item>
                            <el-dropdown-item divided command="tableName">
                              <i class="el-icon-document"></i> 仅插入表名
                            </el-dropdown-item>
                          </el-dropdown-menu>
                        </el-dropdown>
                      </span>
                    </span>
                  </el-tree>

                  <!-- 分页控件 -->
                  <div class="table-pagination" v-if="tablePagination.total > 0">
                    <el-pagination
                      @current-change="handleTablePageChange"
                      :current-page="tablePagination.pageNum"
                      :page-size="tablePagination.pageSize"
                      layout="prev, pager, next"
                      :total="tablePagination.total"
                      small
                    />
                  </div>
                </div>

                <!-- 智能SQL构建器 -->
                <div class="sql-builder">
                  <h5>SQL构建器</h5>

                  <!-- 快速操作按钮 -->
                  <div class="quick-actions">
                    <el-button size="mini" @click="startSelectBuilder" icon="el-icon-search">
                      SELECT查询
                    </el-button>
                    <el-button size="mini" @click="startInsertBuilder" icon="el-icon-plus">
                      INSERT插入
                    </el-button>
                    <el-button size="mini" @click="startUpdateBuilder" icon="el-icon-edit">
                      UPDATE更新
                    </el-button>
                    <el-button size="mini" @click="startDeleteBuilder" icon="el-icon-delete">
                      DELETE删除
                    </el-button>
                  </div>

                  <!-- SQL构建组件 -->
                  <div class="sql-components">
                    <h6>SQL组件</h6>
                    <div class="component-list">
                      <div
                        v-for="component in sqlComponents"
                        :key="component.type"
                        class="component-item"
                        draggable="true"
                        @dragstart="handleDragStart($event, component)"
                        @click="insertSqlComponent(component)"
                      >
                        <i :class="component.icon"></i>
                        <span>{{ component.label }}</span>
                      </div>
                    </div>
                  </div>

                  <!-- 查询条件构建器 -->
                  <div class="condition-builder">
                    <h6>查询条件</h6>

                    <!-- 智能条件构建 -->
                    <div class="smart-condition-builder">
                      <el-button size="mini" @click="openSmartConditionBuilder" icon="el-icon-magic-stick">
                        智能构建
                      </el-button>
                    </div>

                    <div class="condition-list">
                      <div
                        v-for="condition in conditionComponents"
                        :key="condition.type"
                        class="condition-item"
                        draggable="true"
                        @dragstart="handleDragStart($event, condition)"
                        @click="insertCondition(condition)"
                        :title="condition.template"
                      >
                        <i :class="condition.icon"></i>
                        <span>{{ condition.label }}</span>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 函数库 -->
                <div class="function-library">
                  <h5>函数库</h5>
                  <el-collapse v-model="activeFunctionGroups">
                    <el-collapse-item
                      v-for="group in functionGroups"
                      :key="group.name"
                      :title="group.label"
                      :name="group.name"
                    >
                      <div
                        v-for="func in group.functions"
                        :key="func.name"
                        class="function-item"
                        @click="handleFunctionClick(func)"
                      >
                        <span class="function-name">{{ func.name }}</span>
                        <span class="function-desc">{{ func.description }}</span>
                      </div>
                    </el-collapse-item>
                  </el-collapse>
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
                        <code>{{ hint.example }}</code>
                      </div>
                    </div>
                  </el-tab-pane>
                  <el-tab-pane label="常用模板" name="templates">
                    <div class="template-list">
                      <div
                        v-for="template in commonTemplates"
                        :key="template.name"
                        class="template-item"
                        @click="handleTemplateClick(template)"
                      >
                        <h6>{{ template.name }}</h6>
                        <p>{{ template.description }}</p>
                      </div>
                    </div>
                  </el-tab-pane>
                </el-tabs>
              </div>
            </div>
          </el-col>

          <!-- 中间：SQL编辑器 -->
          <el-col :span="10" style="height: 100%;">
            <div class="sql-editor">
              <div class="editor-header">
                <h4>SQL编辑器</h4>
                <div class="editor-actions">
                  <el-button size="mini" @click="handleUndo" icon="el-icon-refresh-left">撤销</el-button>
                  <el-button size="mini" @click="handleRedo" icon="el-icon-refresh-right">重做</el-button>
                  <el-button size="mini" @click="handleClear" icon="el-icon-delete">清空</el-button>
                </div>
              </div>
              
              <!-- 代码编辑器 -->
              <div
                class="code-editor"
                ref="codeEditor"
                @dragover.prevent="handleDragOver"
                @dragenter.prevent="handleDragEnter"
                @dragleave="handleDragLeave"
                @drop="handleDrop"
              >
                <textarea
                  v-model="sqlContent"
                  class="sql-textarea"
                  placeholder="请输入SQL语句或使用左侧可视化构建器..."
                  @input="handleSqlChange"
                  ref="sqlTextarea"
                ></textarea>
              </div>

              <!-- SQL信息面板 -->
              <div class="sql-info">
                <el-row :gutter="10">
                  <el-col :span="8">
                    <div class="info-item">
                      <span class="info-label">行数:</span>
                      <span class="info-value">{{ sqlLineCount }}</span>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="info-item">
                      <span class="info-label">字符:</span>
                      <span class="info-value">{{ sqlCharCount }}</span>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="info-item">
                      <span class="info-label">状态:</span>
                      <span class="info-value" :class="sqlStatusClass">{{ sqlStatus }}</span>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-col>

          <!-- 右侧：结果面板 -->
          <el-col :span="6" style="height: 100%;">
            <div class="result-panel">
              <el-tabs v-model="resultTab" type="border-card">
                <el-tab-pane label="执行结果" name="result">
                  <div class="result-content">
                    <div v-if="executionResult.loading" class="loading-container" v-loading="true" element-loading-text="正在执行SQL...">
                      <div style="height: 200px;"></div>
                    </div>
                    <div v-else-if="executionResult.error" class="error-container">
                      <el-alert
                        title="执行错误"
                        type="error"
                        :description="executionResult.error"
                        show-icon
                        :closable="false"
                      />
                    </div>
                    <div v-else-if="!executionResult.loading && !executionResult.error && executionResult.data" class="success-container">
                      <div class="result-stats">
                        <p>执行时间: {{ executionResult.executionTime }}ms</p>
                        <p>返回行数: {{ executionResult.rowCount }}</p>
                      </div>

                      <!-- 调试信息 -->
                      <div v-if="true" style="background: #f0f0f0; padding: 10px; margin: 10px 0; font-size: 12px;">
                        <p>调试信息:</p>
                        <p>data长度: {{ executionResult.data ? executionResult.data.length : 'null' }}</p>
                        <p>columns长度: {{ executionResult.columns ? executionResult.columns.length : 'null' }}</p>
                        <p>第一行数据: {{ executionResult.data && executionResult.data[0] ? JSON.stringify(executionResult.data[0]) : 'null' }}</p>
                      </div>

                      <el-table
                        v-if="executionResult.data && executionResult.data.length > 0"
                        :data="executionResult.data.slice(0, 100)"
                        size="mini"
                        border
                        stripe
                        max-height="300"
                      >
                        <el-table-column
                          v-for="column in executionResult.columns"
                          :key="column.prop"
                          :prop="column.prop"
                          :label="column.label"
                          show-overflow-tooltip
                        />
                      </el-table>

                      <div v-else class="no-data-message">
                        <el-alert
                          title="查询成功"
                          type="success"
                          description="SQL执行成功，但没有返回数据"
                          show-icon
                          :closable="false"
                        />
                      </div>

                      <p v-if="executionResult.data && executionResult.data.length > 100" class="result-note">
                        注: 仅显示前100行数据
                      </p>
                    </div>
                    <div v-else class="empty-container">
                      <el-empty description="暂无执行结果" />
                    </div>
                  </div>
                </el-tab-pane>
                
                <el-tab-pane label="语法检查" name="validation">
                  <div class="validation-content">
                    <div v-if="validationResult.loading" class="loading-container" v-loading="true" element-loading-text="正在验证语法...">
                      <div style="height: 200px;"></div>
                    </div>
                    <div v-else-if="validationResult.valid" class="success-container">
                      <el-alert
                        title="语法正确"
                        type="success"
                        description="SQL语法验证通过"
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
                        title="语法错误"
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
                      <el-empty description="请点击验证按钮检查SQL语法" />
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="执行计划" name="plan">
                  <div class="plan-content">
                    <el-empty description="执行计划功能待实现" />
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- SQL模板选择对话框 -->
    <SqlTemplateSelectDialog
      :visible.sync="templateSelectVisible"
      @select="handleTemplateSelect"
    />

    <!-- 智能条件构建对话框 -->
    <el-dialog
      title="智能查询条件构建器"
      :visible.sync="showConditionDialog"
      width="600px"
      :close-on-click-modal="false"
      :modal="true"
      :modal-append-to-body="true"
      :append-to-body="true"
      :lock-scroll="true"
      custom-class="smart-condition-dialog sql-editor-dialog"
      :z-index="3000"
    >
      <div class="smart-condition-form">
        <el-form :model="conditionForm" label-width="80px">
          <el-form-item label="字段名">
            <el-input v-model="conditionForm.column" placeholder="请输入字段名，如：user_name"></el-input>
          </el-form-item>
          <el-form-item label="操作符">
            <el-select v-model="conditionForm.operator" placeholder="请选择操作符" style="width: 100%">
              <el-option
                v-for="op in operatorOptions"
                :key="op.value"
                :label="op.label"
                :value="op.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="值" v-if="needValue">
            <el-input v-model="conditionForm.value" :placeholder="valuePlaceholder"></el-input>
          </el-form-item>
          <el-form-item label="第二个值" v-if="conditionForm.operator === 'BETWEEN'">
            <el-input v-model="conditionForm.value2" placeholder="请输入第二个值"></el-input>
          </el-form-item>
        </el-form>

        <div class="condition-preview">
          <h4>预览:</h4>
          <div class="preview-code">{{ generatedCondition }}</div>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="closeSmartConditionBuilder">取消</el-button>
        <el-button type="primary" @click="insertGeneratedCondition">插入条件</el-button>
      </div>
    </el-dialog>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleSaveAndClose">保存并关闭</el-button>
    </div>
  </el-dialog>

  <!-- 保存数据模型对话框 -->
  <el-dialog
    title="保存数据模型"
    :visible.sync="saveDialogVisible"
    width="600px"
    :close-on-click-modal="false"
    :modal-append-to-body="true"
    :append-to-body="true"
  >
    <el-form :model="saveForm" :rules="saveRules" ref="saveForm" label-width="120px">
      <el-form-item label="模型编码" prop="modelCode">
        <el-input v-model="saveForm.modelCode" placeholder="请输入模型编码，如：RISK_001"></el-input>
      </el-form-item>
      <el-form-item label="模型名称" prop="modelName">
        <el-input v-model="saveForm.modelName" placeholder="请输入模型名称"></el-input>
      </el-form-item>
      <el-form-item label="模型类型" prop="modelType">
        <el-select v-model="saveForm.modelType" placeholder="请选择模型类型" style="width: 100%;">
          <el-option label="财务模型" value="FINANCIAL"></el-option>
          <el-option label="风险模型" value="RISK"></el-option>
          <el-option label="审计模型" value="AUDIT"></el-option>
          <el-option label="业务模型" value="BUSINESS"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="业务含义" prop="businessMeaning">
        <el-input type="textarea" v-model="saveForm.businessMeaning" :rows="3" placeholder="请描述该模型的业务含义和用途"></el-input>
      </el-form-item>
      <el-form-item label="计算逻辑" prop="calculationLogic">
        <el-input type="textarea" v-model="saveForm.calculationLogic" :rows="2" placeholder="请简要描述计算逻辑"></el-input>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="saveDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleConfirmSave" :loading="saveLoading">确定保存</el-button>
    </div>
  </el-dialog>

  <!-- 导入模型SQL对话框 -->
  <el-dialog
    title="选择数据模型"
    :visible.sync="importSqlDialogVisible"
    width="1000px"
    :close-on-click-modal="false"
    :modal-append-to-body="true"
    :append-to-body="true"
  >
    <div class="import-sql-container">
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :model="importSearchForm" inline>
          <el-form-item label="模型名称">
            <el-input v-model="importSearchForm.modelName" placeholder="请输入模型名称" clearable style="width: 200px;"></el-input>
          </el-form-item>
          <el-form-item label="模型类型">
            <el-select v-model="importSearchForm.modelType" placeholder="请选择模型类型" clearable style="width: 150px;">
              <el-option label="全部" value=""></el-option>
              <el-option label="财务模型" value="FINANCIAL"></el-option>
              <el-option label="风险模型" value="RISK"></el-option>
              <el-option label="审计模型" value="AUDIT"></el-option>
              <el-option label="业务模型" value="BUSINESS"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchImportModels" icon="el-icon-search">搜索</el-button>
            <el-button @click="resetImportSearch" icon="el-icon-refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 模型列表 -->
      <el-table
        :data="importModelList"
        v-loading="importLoading"
        height="400"
        @row-click="handleImportRowClick"
        highlight-current-row
      >
        <el-table-column prop="modelCode" label="模型编码" width="150"></el-table-column>
        <el-table-column prop="modelName" label="模型名称" width="200"></el-table-column>
        <el-table-column prop="modelType" label="模型类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getModelTypeTag(scope.row.modelType)">{{ getModelTypeLabel(scope.row.modelType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="businessMeaning" label="业务含义" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleImportSql(scope.row)">导入</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @current-change="handleImportPageChange"
          :current-page="importPagination.pageNum"
          :page-size="importPagination.pageSize"
          :total="importPagination.total"
          layout="total, prev, pager, next"
        />
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="importSqlDialogVisible = false">取消</el-button>
    </div>
  </el-dialog>
  </div>
</template>

<script>
import {
  getDataSourceList,
  getTableStructureList,
  validateSqlSyntax,
  testDataModelSql,
  saveDataModel,
  getDataModelList,
  getSqlTemplateDetail
} from '@/api/mxgl'
import SqlTemplateSelectDialog from './SqlTemplateSelectDialog'
// 引入z-index层级管理样式
import './dialog-z-index.css'

export default {
  name: 'SqlEditorDialog',
  components: {
    SqlTemplateSelectDialog
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    modelData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      visualMode: true,
      currentDataSource: '',
      sqlContent: '',
      dataSourceList: [],
      tableTreeData: [],
      treeProps: {
        children: 'children',
        label: 'label'
      },
      // 分页信息
      tablePagination: {
        pageNum: 1,
        pageSize: 15,
        total: 0
      },
      // SQL组件
      sqlComponents: [
        { type: 'select', label: 'SELECT', icon: 'el-icon-search' },
        { type: 'from', label: 'FROM', icon: 'el-icon-files' },
        { type: 'where', label: 'WHERE', icon: 'el-icon-filter' },
        { type: 'join', label: 'JOIN', icon: 'el-icon-connection' },
        { type: 'group', label: 'GROUP BY', icon: 'el-icon-pie-chart' },
        { type: 'order', label: 'ORDER BY', icon: 'el-icon-sort' },
        { type: 'having', label: 'HAVING', icon: 'el-icon-zoom-in' },
        { type: 'union', label: 'UNION', icon: 'el-icon-plus' }
      ],
      // 查询条件组件
      conditionComponents: [
        { type: 'equal', label: '等于 =', icon: 'el-icon-check', template: 'column = \'value\'' },
        { type: 'not_equal', label: '不等于 !=', icon: 'el-icon-close', template: 'column != \'value\'' },
        { type: 'like', label: '模糊查询 LIKE', icon: 'el-icon-search', template: 'column LIKE \'%value%\'' },
        { type: 'not_like', label: '不包含 NOT LIKE', icon: 'el-icon-remove-outline', template: 'column NOT LIKE \'%value%\'' },
        { type: 'start_with', label: '开头匹配', icon: 'el-icon-right', template: 'column LIKE \'value%\'' },
        { type: 'end_with', label: '结尾匹配', icon: 'el-icon-left', template: 'column LIKE \'%value\'' },
        { type: 'greater', label: '大于 >', icon: 'el-icon-top', template: 'column > value' },
        { type: 'greater_equal', label: '大于等于 >=', icon: 'el-icon-top-right', template: 'column >= value' },
        { type: 'less', label: '小于 <', icon: 'el-icon-bottom', template: 'column < value' },
        { type: 'less_equal', label: '小于等于 <=', icon: 'el-icon-bottom-left', template: 'column <= value' },
        { type: 'between', label: '范围查询 BETWEEN', icon: 'el-icon-sort', template: 'column BETWEEN value1 AND value2' },
        { type: 'in', label: '包含 IN', icon: 'el-icon-menu', template: 'column IN (value1, value2, value3)' },
        { type: 'not_in', label: '不包含 NOT IN', icon: 'el-icon-remove', template: 'column NOT IN (value1, value2, value3)' },
        { type: 'is_null', label: '为空 IS NULL', icon: 'el-icon-circle-close', template: 'column IS NULL' },
        { type: 'is_not_null', label: '不为空 IS NOT NULL', icon: 'el-icon-circle-check', template: 'column IS NOT NULL' },
        { type: 'exists', label: '存在 EXISTS', icon: 'el-icon-success', template: 'EXISTS (SELECT 1 FROM table WHERE condition)' },
        { type: 'not_exists', label: '不存在 NOT EXISTS', icon: 'el-icon-error', template: 'NOT EXISTS (SELECT 1 FROM table WHERE condition)' }
      ],
      // 函数库
      activeFunctionGroups: ['aggregate'],
      functionGroups: [
        {
          name: 'aggregate',
          label: '聚合函数',
          functions: [
            { name: 'COUNT', description: '计数', syntax: 'COUNT(column)' },
            { name: 'SUM', description: '求和', syntax: 'SUM(column)' },
            { name: 'AVG', description: '平均值', syntax: 'AVG(column)' },
            { name: 'MAX', description: '最大值', syntax: 'MAX(column)' },
            { name: 'MIN', description: '最小值', syntax: 'MIN(column)' }
          ]
        },
        {
          name: 'string',
          label: '字符串函数',
          functions: [
            { name: 'CONCAT', description: '连接字符串', syntax: 'CONCAT(str1, str2)' },
            { name: 'SUBSTRING', description: '截取字符串', syntax: 'SUBSTRING(str, start, length)' },
            { name: 'UPPER', description: '转大写', syntax: 'UPPER(str)' },
            { name: 'LOWER', description: '转小写', syntax: 'LOWER(str)' },
            { name: 'TRIM', description: '去除空格', syntax: 'TRIM(str)' },
            { name: 'LENGTH', description: '字符串长度', syntax: 'LENGTH(str)' },
            { name: 'REPLACE', description: '替换字符串', syntax: 'REPLACE(str, old_str, new_str)' },
            { name: 'LEFT', description: '左截取', syntax: 'LEFT(str, length)' },
            { name: 'RIGHT', description: '右截取', syntax: 'RIGHT(str, length)' },
            { name: 'INSTR', description: '查找位置', syntax: 'INSTR(str, substr)' }
          ]
        },
        {
          name: 'date',
          label: '日期函数',
          functions: [
            { name: 'NOW', description: '当前时间', syntax: 'NOW()' },
            { name: 'CURDATE', description: '当前日期', syntax: 'CURDATE()' },
            { name: 'DATE_FORMAT', description: '格式化日期', syntax: 'DATE_FORMAT(date, \'%Y-%m-%d\')' },
            { name: 'DATEDIFF', description: '日期差', syntax: 'DATEDIFF(date1, date2)' },
            { name: 'DATE_ADD', description: '日期加法', syntax: 'DATE_ADD(date, INTERVAL 1 DAY)' },
            { name: 'DATE_SUB', description: '日期减法', syntax: 'DATE_SUB(date, INTERVAL 1 DAY)' },
            { name: 'YEAR', description: '提取年份', syntax: 'YEAR(date)' },
            { name: 'MONTH', description: '提取月份', syntax: 'MONTH(date)' },
            { name: 'DAY', description: '提取日期', syntax: 'DAY(date)' },
            { name: 'WEEKDAY', description: '星期几', syntax: 'WEEKDAY(date)' },
            { name: 'LAST_DAY', description: '月末日期', syntax: 'LAST_DAY(date)' }
          ]
        },
        {
          name: 'condition',
          label: '条件函数',
          functions: [
            { name: 'CASE WHEN', description: '条件判断', syntax: 'CASE WHEN condition THEN value1 ELSE value2 END' },
            { name: 'IF', description: '简单条件', syntax: 'IF(condition, value1, value2)' },
            { name: 'IFNULL', description: '空值处理', syntax: 'IFNULL(expr, default_value)' },
            { name: 'COALESCE', description: '返回第一个非空值', syntax: 'COALESCE(expr1, expr2, expr3)' },
            { name: 'NULLIF', description: '相等返回NULL', syntax: 'NULLIF(expr1, expr2)' }
          ]
        }
      ],
      // 代码助手
      assistantTab: 'syntax',
      syntaxHints: [
        {
          keyword: 'WITH',
          description: '公用表表达式(CTE)',
          example: 'WITH cte AS (SELECT ...) SELECT * FROM cte'
        },
        {
          keyword: 'CASE WHEN',
          description: '条件表达式',
          example: 'CASE WHEN condition THEN value1 ELSE value2 END'
        },
        {
          keyword: 'WINDOW',
          description: '窗口函数',
          example: 'ROW_NUMBER() OVER (PARTITION BY col ORDER BY col)'
        }
      ],
      commonTemplates: [
        {
          name: '基础查询',
          description: '简单的SELECT查询模板',
          sql: 'SELECT * FROM table_name WHERE condition'
        },
        {
          name: '关联查询',
          description: '多表JOIN查询模板',
          sql: 'SELECT a.*, b.* FROM table_a a LEFT JOIN table_b b ON a.id = b.a_id'
        },
        {
          name: '聚合查询',
          description: '分组聚合查询模板',
          sql: 'SELECT column, COUNT(*) FROM table_name GROUP BY column HAVING COUNT(*) > 1'
        }
      ],
      // 结果面板
      resultTab: 'result',
      executionResult: {
        loading: false,
        data: null,
        columns: [],
        rowCount: 0,
        executionTime: 0,
        error: null
      },
      validationResult: {
        loading: false,
        valid: null,
        message: '',
        warnings: [],
        errors: []
      },
      // 对话框
      templateSelectVisible: false,
      // 智能条件构建器
      showConditionDialog: false,
      conditionForm: {
        column: '',
        operator: '',
        value: '',
        value2: ''
      },
      operatorOptions: [
        { value: '=', label: '等于 (=)' },
        { value: '!=', label: '不等于 (!=)' },
        { value: '>', label: '大于 (>)' },
        { value: '>=', label: '大于等于 (>=)' },
        { value: '<', label: '小于 (<)' },
        { value: '<=', label: '小于等于 (<=)' },
        { value: 'LIKE', label: '模糊查询 (LIKE)' },
        { value: 'NOT LIKE', label: '不包含 (NOT LIKE)' },
        { value: 'IN', label: '包含 (IN)' },
        { value: 'NOT IN', label: '不包含 (NOT IN)' },
        { value: 'BETWEEN', label: '范围查询 (BETWEEN)' },
        { value: 'IS NULL', label: '为空 (IS NULL)' },
        { value: 'IS NOT NULL', label: '不为空 (IS NOT NULL)' }
      ],
      // SQL历史记录
      sqlHistory: [''],
      sqlHistoryIndex: 0,

      // 保存对话框
      saveDialogVisible: false,
      saveForm: {
        modelCode: '',
        modelName: '',
        modelType: 'RISK',
        businessMeaning: '',
        calculationLogic: '',
        templateType: 'CUSTOM'
      },
      saveLoading: false,
      autoCloseAfterSave: false,

      // 导入SQL对话框
      importSqlDialogVisible: false,
      importLoading: false,
      importModelList: [],
      importSearchForm: {
        modelName: '',
        modelType: ''
      },
      importPagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },

      // 保存表单验证规则
      saveRules: {
        modelCode: [
          { required: true, message: '请输入模型编码', trigger: 'blur' },
          { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' },
          { pattern: /^[A-Z0-9_]+$/, message: '只能包含大写字母、数字和下划线', trigger: 'blur' }
        ],
        modelName: [
          { required: true, message: '请输入模型名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        modelType: [
          { required: true, message: '请选择模型类型', trigger: 'change' }
        ],
        businessMeaning: [
          { required: true, message: '请输入业务含义', trigger: 'blur' },
          { min: 10, max: 500, message: '长度在 10 到 500 个字符', trigger: 'blur' }
        ]
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
    },
    sqlLineCount() {
      return this.sqlContent.split('\n').length
    },
    sqlCharCount() {
      return this.sqlContent.length
    },
    sqlStatus() {
      if (!this.sqlContent.trim()) return '空'
      if (this.validationResult.valid === true) return '正确'
      if (this.validationResult.valid === false) return '错误'
      return '未验证'
    },
    sqlStatusClass() {
      if (this.validationResult.valid === true) return 'status-success'
      if (this.validationResult.valid === false) return 'status-error'
      return 'status-normal'
    },
    // 智能条件构建器相关计算属性
    needValue() {
      const noValueOperators = ['IS NULL', 'IS NOT NULL']
      return !noValueOperators.includes(this.conditionForm.operator)
    },
    valuePlaceholder() {
      const { operator } = this.conditionForm
      if (operator === 'LIKE' || operator === 'NOT LIKE') {
        return '请输入查询值，如：张三 (自动添加%通配符)'
      } else if (operator === 'IN' || operator === 'NOT IN') {
        return '请输入多个值，用逗号分隔，如：1,2,3'
      } else if (operator === 'BETWEEN') {
        return '请输入第一个值'
      } else {
        return '请输入查询值'
      }
    },
    generatedCondition() {
      const { column, operator, value, value2 } = this.conditionForm

      if (!column || !operator) {
        return '请填写字段名和操作符'
      }

      let condition = column

      switch (operator) {
        case 'IS NULL':
        case 'IS NOT NULL':
          condition += ` ${operator}`
          break
        case 'LIKE':
          condition += ` LIKE '%${value || 'value'}%'`
          break
        case 'NOT LIKE':
          condition += ` NOT LIKE '%${value || 'value'}%'`
          break
        case 'IN':
        case 'NOT IN':
          const inValues = value ? value.split(',').map(v => `'${v.trim()}'`).join(', ') : "'value1', 'value2'"
          condition += ` ${operator} (${inValues})`
          break
        case 'BETWEEN':
          condition += ` BETWEEN '${value || 'value1'}' AND '${value2 || 'value2'}'`
          break
        default:
          // 数字类型判断
          const isNumber = /^[0-9.]+$/.test(value)
          const quotedValue = isNumber ? (value || 'value') : `'${value || 'value'}'`
          condition += ` ${operator} ${quotedValue}`
      }

      return condition
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadDataSources()
        // 初始化历史记录
        if (this.sqlHistory.length === 1 && this.sqlHistory[0] === '') {
          this.sqlHistory = [this.sqlContent || '']
          this.sqlHistoryIndex = 0
        }
        // 如果有模型数据，加载到编辑器
        this.loadModelData()
      }
    },
    modelData: {
      handler(newVal) {
        if (newVal && this.visible) {
          this.loadModelData()
        }
      },
      immediate: true
    },
    currentDataSource(val) {
      if (val) {
        this.loadTableStructure(val)
      }
    },
    sqlContent: {
      handler(newVal, oldVal) {
        // 当SQL内容发生变化时，重置验证结果
        if (newVal !== oldVal && this.validationResult.valid !== null) {
          this.validationResult.valid = null
          this.validationResult.message = ''
          this.validationResult.warnings = []
          this.validationResult.errors = []
        }
      },
      immediate: false
    }
  },
  mounted() {
    // 添加键盘快捷键监听
    document.addEventListener('keydown', this.handleKeydown)
  },
  beforeDestroy() {
    // 移除键盘事件监听
    document.removeEventListener('keydown', this.handleKeydown)
  },
  methods: {
    // 处理键盘快捷键
    handleKeydown(event) {
      // 只在对话框可见时处理快捷键
      if (!this.visible) return

      // Ctrl+Shift+F: 格式化
      if (event.ctrlKey && event.shiftKey && event.key === 'F') {
        event.preventDefault()
        this.handleFormat()
      }
      // Ctrl+Shift+V: 验证
      else if (event.ctrlKey && event.shiftKey && event.key === 'V') {
        event.preventDefault()
        this.handleValidate()
      }
      // Ctrl+Z: 撤销
      else if (event.ctrlKey && !event.shiftKey && event.key === 'z') {
        event.preventDefault()
        this.handleUndo()
      }
      // Ctrl+Y: 重做
      else if (event.ctrlKey && !event.shiftKey && event.key === 'y') {
        event.preventDefault()
        this.handleRedo()
      }
    },
    // 加载数据源列表
    async loadDataSources() {
      try {
        console.log('📊 SQL编辑器 - 加载数据源列表')
        const response = await getDataSourceList({
          pageNum: 1,
          pageSize: 1000,
          isEnabled: 'Y'
        })
        console.log('📊 SQL编辑器 - 数据源列表响应:', response)

        // 兼容两种响应格式
        if (response && response.code === 1) {
          this.dataSourceList = response.data.records || []
          if (this.dataSourceList.length > 0 && !this.currentDataSource) {
            this.currentDataSource = this.dataSourceList[0].sourceId
          }
          console.log('✅ 数据源列表加载成功(标准格式):', this.dataSourceList.length, '个数据源')
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.dataSourceList = response.records || []
          if (this.dataSourceList.length > 0 && !this.currentDataSource) {
            this.currentDataSource = this.dataSourceList[0].sourceId
          }
          console.log('✅ 数据源列表加载成功(直接格式):', this.dataSourceList.length, '个数据源')
        } else {
          console.error('❌ 数据源列表加载失败:', response)
        }
      } catch (error) {
        console.error('❌ 获取数据源列表失败:', error)
      }
    },

    // 加载模型数据到编辑器
    loadModelData() {
      if (this.modelData) {
        console.log('加载模型数据到SQL编辑器:', this.modelData)

        // 加载SQL内容
        this.sqlContent = this.modelData.sqlStatement || ''

        // 设置数据源
        if (this.modelData.dataSourceId) {
          this.currentDataSource = this.modelData.dataSourceId
        }

        // 更新保存表单（编辑模式）
        this.saveForm = {
          modelCode: this.modelData.modelCode || '',
          modelName: this.modelData.modelName || '',
          modelType: this.modelData.modelType || 'RISK',
          businessMeaning: this.modelData.businessMeaning || '',
          calculationLogic: this.modelData.calculationLogic || '',
          templateType: this.modelData.templateType || 'CUSTOM'
        }

        // 添加到历史记录
        this.addToHistory(this.sqlContent)

        this.$message.success(`已加载模型: ${this.modelData.modelName}`)
      } else {
        // 新建模式，清空内容
        this.sqlContent = ''
        this.currentDataSource = this.dataSourceList.length > 0 ? this.dataSourceList[0].sourceId : ''
        this.saveForm = {
          modelCode: '',
          modelName: '',
          modelType: 'RISK',
          businessMeaning: '',
          calculationLogic: '',
          templateType: 'CUSTOM'
        }
      }
    },

    // 加载表结构
    async loadTableStructure(dataSourceId, pageNum = 1) {
      try {
        console.log('📊 SQL编辑器 - 加载表结构:', { dataSourceId, pageNum })
        const response = await getTableStructureList({
          dataSourceId,
          pageNum: pageNum,
          pageSize: this.tablePagination.pageSize
        })
        console.log('📊 SQL编辑器 - 表结构响应:', response)

        // 兼容两种响应格式
        if (response && response.code === 1) {
          const data = response.data || {}
          this.buildTableTree(data.records || [])
          // 更新分页信息
          this.tablePagination = {
            pageNum: data.pageNum || 1,
            pageSize: data.pageSize || 15,
            total: data.total || 0
          }
          console.log('✅ 表结构加载成功(标准格式):', data.records?.length || 0, '个表')
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.buildTableTree(response.records || [])
          // 更新分页信息
          this.tablePagination = {
            pageNum: response.pageNum || 1,
            pageSize: response.pageSize || 15,
            total: response.total || 0
          }
          console.log('✅ 表结构加载成功(直接格式):', response.records?.length || 0, '个表')
        } else {
          console.error('❌ 表结构加载失败:', response)
        }
      } catch (error) {
        console.error('❌ 获取表结构失败:', error)
      }
    },

    // 构建表结构树
    buildTableTree(tables) {
      this.tableTreeData = tables.map(table => ({
        id: table.tableName,
        label: table.tableName,
        type: 'table',
        tableName: table.tableName,  // 添加tableName属性
        tableComment: table.tableComment,  // 添加表注释
        children: table.columns ? table.columns.map(column => ({
          id: `${table.tableName}.${column.columnName}`,
          label: column.columnName,
          type: 'column',
          dataType: column.dataType,
          tableName: table.tableName,
          columnName: column.columnName,
          columnComment: column.columnComment,
          isPrimaryKey: column.isPrimaryKey,
          isNullable: column.isNullable
        })) : []
      }))
    },

    // 获取树节点图标
    getTreeNodeIcon(type) {
      return type === 'table' ? 'el-icon-files' : 'el-icon-document'
    },

    // 表节点点击（树节点展开/折叠）
    handleTableNodeClick(data) {
      // 这个方法现在只用于树节点的展开/折叠
      // 具体的插入操作通过其他方法处理
    },

    // 表名/列名点击（插入操作）
    handleTableNameClick(data) {
      if (data.type === 'column') {
        this.insertText(`${data.tableName}.${data.columnName}`)
      }
      // 表名不直接插入，通过下拉菜单操作
    },

    // 拖拽开始
    handleDragStart(event, component) {
      event.dataTransfer.setData('text/plain', component.type)
      event.dataTransfer.setData('application/json', JSON.stringify(component))
    },

    // 拖拽进入
    handleDragEnter(event) {
      event.preventDefault()
      this.$refs.codeEditor.classList.add('drag-over')
    },

    // 拖拽悬停
    handleDragOver(event) {
      event.preventDefault()
    },

    // 拖拽离开
    handleDragLeave(event) {
      // 检查是否真的离开了编辑器区域
      if (!this.$refs.codeEditor.contains(event.relatedTarget)) {
        this.$refs.codeEditor.classList.remove('drag-over')
      }
    },

    // 拖拽放置
    handleDrop(event) {
      event.preventDefault()
      this.$refs.codeEditor.classList.remove('drag-over')

      try {
        // 尝试获取组件数据
        const componentData = event.dataTransfer.getData('application/json')
        if (componentData) {
          const component = JSON.parse(componentData)
          this.insertSqlComponent(component)
          this.$message.success(`已插入 ${component.label} 语句`)
          return
        }

        // 兼容处理：如果没有JSON数据，使用text数据
        const componentType = event.dataTransfer.getData('text/plain')
        if (componentType) {
          const component = this.sqlComponents.find(c => c.type === componentType)
          if (component) {
            this.insertSqlComponent(component)
            this.$message.success(`已插入 ${component.label} 语句`)
          }
        }
      } catch (error) {
        console.error('拖拽处理失败:', error)
        this.$message.error('拖拽插入失败')
      }
    },

    // 插入SQL组件
    insertSqlComponent(component) {
      const sqlTemplates = {
        'select': 'SELECT column1, column2 FROM table_name',
        'from': 'FROM table_name',
        'where': 'WHERE condition = value',
        'join': 'JOIN table_name ON condition',
        'group': 'GROUP BY column_name',
        'order': 'ORDER BY column_name ASC',
        'having': 'HAVING condition',
        'union': 'UNION SELECT column1, column2 FROM table_name'
      }

      const template = sqlTemplates[component.type] || component.label
      this.insertText(template)
    },

    // 插入查询条件
    insertCondition(condition) {
      // 如果有模板，使用模板；否则使用标签
      const template = condition.template || condition.label
      this.insertText(template)

      // 显示提示信息
      this.$message.success(`已插入 ${condition.label} 条件`)
    },

    // 插入智能生成的条件
    insertGeneratedCondition() {
      if (!this.conditionForm.column || !this.conditionForm.operator) {
        this.$message.warning('请填写字段名和操作符')
        return
      }

      // 插入生成的条件
      this.insertText(this.generatedCondition)

      // 关闭对话框并重置表单
      this.closeSmartConditionBuilder()

      this.$message.success('智能条件已插入')
    },

    // 重置条件构建表单
    resetConditionForm() {
      this.conditionForm = {
        column: '',
        operator: '',
        value: '',
        value2: ''
      }
    },

    // 打开智能条件构建器
    openSmartConditionBuilder() {
      try {
        // 清理可能存在的遮罩层问题
        this.clearModalMask()

        // 打开对话框
        this.showConditionDialog = true

        // 确保对话框正确显示
        this.$nextTick(() => {
          this.ensureDialogInteractable()
        })
      } catch (error) {
        console.error('打开智能条件构建器失败:', error)
        this.$message.error('打开智能构建器失败')
      }
    },

    // 清理遮罩层
    clearModalMask() {
      try {
        // 移除可能残留的遮罩层
        const modalElements = document.querySelectorAll('.v-modal')
        modalElements.forEach(modal => {
          if (modal && modal.style.zIndex && parseInt(modal.style.zIndex) < 3000) {
            modal.remove()
          }
        })
      } catch (error) {
        console.error('清理遮罩层失败:', error)
      }
    },

    // 确保对话框可交互
    ensureDialogInteractable() {
      try {
        // 确保页面可交互
        document.body.style.pointerEvents = ''
        document.documentElement.style.pointerEvents = ''

        // 检查智能条件构建对话框的层级
        const conditionDialog = document.querySelector('.smart-condition-dialog')
        if (conditionDialog) {
          conditionDialog.style.zIndex = '3000'
        }
      } catch (error) {
        console.error('确保对话框可交互失败:', error)
      }
    },

    // 关闭智能条件构建器
    closeSmartConditionBuilder() {
      try {
        // 关闭对话框
        this.showConditionDialog = false

        // 重置表单
        this.resetConditionForm()

        // 清理遮罩层
        this.$nextTick(() => {
          this.clearModalMask()
          this.ensurePageInteractable()
        })
      } catch (error) {
        console.error('关闭智能条件构建器失败:', error)
      }
    },

    // 确保页面可交互
    ensurePageInteractable() {
      try {
        // 移除可能阻止交互的样式
        document.body.style.pointerEvents = ''
        document.documentElement.style.pointerEvents = ''

        // 移除遮罩层类
        document.body.classList.remove('el-popup-parent--hidden')
        document.documentElement.classList.remove('el-popup-parent--hidden')

        // 重置滚动
        document.body.style.overflow = ''
        document.body.style.paddingRight = ''

        console.log('✅ 页面交互性恢复完成')
      } catch (error) {
        console.error('❌ 恢复页面交互性失败:', error)
      }
    },

    // 函数点击
    handleFunctionClick(func) {
      this.insertText(func.syntax)
    },

    // 模板点击
    handleTemplateClick(template) {
      this.sqlContent = template.sql
    },

    // 插入文本
    insertText(text) {
      const textarea = this.$refs.sqlTextarea
      if (textarea) {
        const start = textarea.selectionStart || 0
        const end = textarea.selectionEnd || 0
        const before = this.sqlContent.substring(0, start)
        const after = this.sqlContent.substring(end)

        // 智能添加空格和换行
        let insertText = text
        if (before && !before.endsWith(' ') && !before.endsWith('\n')) {
          insertText = ' ' + insertText
        }
        if (after && !after.startsWith(' ') && !after.startsWith('\n')) {
          insertText = insertText + ' '
        }

        this.sqlContent = before + insertText + after

        this.$nextTick(() => {
          textarea.focus()
          const newPosition = start + insertText.length
          textarea.setSelectionRange(newPosition, newPosition)
        })
      } else {
        // 如果没有光标位置，直接追加到末尾
        if (this.sqlContent && !this.sqlContent.endsWith(' ') && !this.sqlContent.endsWith('\n')) {
          this.sqlContent += ' ' + text
        } else {
          this.sqlContent += text
        }
      }
    },

    // 模式切换
    handleModeChange(visual) {
      this.visualMode = visual
    },

    // SQL内容变化
    handleSqlChange() {
      // 重置验证结果
      this.validationResult.valid = null
    },

    // 新建SQL
    handleNewSql() {
      this.sqlContent = ''
      this.executionResult = { loading: false, data: null, columns: [], rowCount: 0, executionTime: 0, error: null }
      this.validationResult = { loading: false, valid: null, message: '', warnings: [], errors: [] }
    },

    // 打开模板
    handleOpenTemplate() {
      this.templateSelectVisible = true
    },

    // 模板选择
    async handleTemplateSelect(template) {
      try {
        console.log('📊 SQL编辑器 - 选择模板:', template)

        // 🔥 获取模板详情（如果需要完整内容）
        if (template.templateId) {
          // 如果有templateId，获取完整模板内容
          const response = await getSqlTemplateDetail(template.templateId)
          console.log('📊 SQL编辑器 - 模板详情响应:', response)

          if (response && response.code === 1) {
            this.sqlContent = response.data.sqlContent || template.sqlContent
            console.log('✅ 模板内容加载成功(标准格式)')
          } else if (response && response.sqlContent) {
            // 直接格式
            this.sqlContent = response.sqlContent
            console.log('✅ 模板内容加载成功(直接格式)')
          } else {
            this.sqlContent = template.sqlContent
            console.log('⚠️ 使用模板默认内容')
          }
        } else {
          this.sqlContent = template.sqlContent
        }

        // 🔥 关闭模板选择对话框
        this.templateSelectVisible = false
        this.$message.success('模板应用成功')

      } catch (error) {
        console.error('❌ 应用模板失败:', error)
        this.sqlContent = template.sqlContent
        this.templateSelectVisible = false
        this.$message.success('模板应用成功')
      }
    },

    // 导入模型SQL
    handleImportModelSql() {
      this.importSqlDialogVisible = true
      this.loadImportModels()
    },

    // 加载导入模型列表
    async loadImportModels() {
      try {
        this.importLoading = true

        // 构建查询参数，参考数据模型管理的格式
        const queryParams = {
          pageNum: this.importPagination.pageNum,
          pageSize: this.importPagination.pageSize,
          modelName: this.importSearchForm.modelName || '',
          modelType: this.importSearchForm.modelType || '',
          status: '', // 不限制状态，显示所有状态的模型
          dataSourceId: ''
        }

        console.log('📊 SQL编辑器 - 导入模型查询参数:', queryParams)
        const response = await getDataModelList(queryParams)
        console.log('📊 SQL编辑器 - 导入模型接口响应:', response)

        // 兼容两种响应格式
        if (response && response.code === 1) {
          this.importModelList = response.data.records || []
          this.importPagination.total = response.data.total || 0
          console.log('✅ 导入模型列表加载成功(标准格式):', this.importModelList.length, '条数据')
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.importModelList = response.records || []
          this.importPagination.total = response.total || 0
          console.log('✅ 导入模型列表加载成功(直接格式):', this.importModelList.length, '条数据')
        } else {
          console.error('❌ 导入模型列表加载失败:', response)
          this.$message.error(response.msg || '加载模型列表失败')
        }
      } catch (error) {
        console.error('❌ 加载模型列表失败:', error)
        this.$message.error('加载模型列表失败')
      } finally {
        this.importLoading = false
      }
    },

    // 搜索导入模型
    searchImportModels() {
      this.importPagination.pageNum = 1
      this.loadImportModels()
    },

    // 重置搜索
    resetImportSearch() {
      this.importSearchForm = {
        modelName: '',
        modelType: ''
      }
      this.importPagination.pageNum = 1
      this.loadImportModels()
    },

    // 导入分页切换
    handleImportPageChange(pageNum) {
      this.importPagination.pageNum = pageNum
      this.loadImportModels()
    },

    // 行点击
    handleImportRowClick(row) {
      // 可以在这里添加行选中效果
    },

    // 导入SQL
    async handleImportSql(model) {
      try {
        // 确认导入
        const confirmResult = await this.$confirm(
          `确定要导入模型"${model.modelName}"的SQL语句吗？当前编辑器内容将被替换。`,
          '确认导入',
          {
            confirmButtonText: '确定导入',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )

        if (confirmResult) {
          // 导入SQL内容
          this.sqlContent = model.sqlStatement || ''

          // 可选：同时设置数据源
          if (model.dataSourceId) {
            this.currentDataSource = model.dataSourceId
          }

          // 添加到历史记录
          this.addToHistory(this.sqlContent)

          // 关闭导入对话框
          this.importSqlDialogVisible = false

          this.$message.success(`已导入模型"${model.modelName}"的SQL语句`)
        }
      } catch (error) {
        // 用户取消导入
        if (error === 'cancel') {
          return
        }
        console.error('导入SQL失败:', error)
        this.$message.error('导入SQL失败')
      }
    },

    // 获取模型类型标签样式
    getModelTypeTag(type) {
      const tagMap = {
        'FINANCIAL': 'primary',
        'RISK': 'warning',
        'AUDIT': 'success',
        'BUSINESS': 'info'
      }
      return tagMap[type] || 'primary'
    },

    // 获取模型类型标签文本
    getModelTypeLabel(type) {
      const labelMap = {
        'FINANCIAL': '财务模型',
        'RISK': '风险模型',
        'AUDIT': '审计模型',
        'BUSINESS': '业务模型'
      }
      return labelMap[type] || type
    },

    // 保存
    handleSave() {
      if (!this.sqlContent.trim()) {
        this.$message.warning('请输入SQL语句')
        return
      }

      if (!this.currentDataSource) {
        this.$message.warning('请选择数据源')
        return
      }

      // 重置保存表单
      this.saveForm = {
        modelCode: '',
        modelName: '',
        modelType: 'RISK_MONITOR',
        businessMeaning: '',
        calculationLogic: '',
        templateType: 'CUSTOM'
      }

      // 显示保存对话框
      this.saveDialogVisible = true
    },

    // 确认保存
    async handleConfirmSave() {
      try {
        // 表单验证
        await this.$refs.saveForm.validate()

        this.saveLoading = true

        // 构建保存数据
        const saveData = {
          modelCode: this.saveForm.modelCode,
          modelName: this.saveForm.modelName,
          modelType: this.saveForm.modelType,
          businessMeaning: this.saveForm.businessMeaning,
          calculationLogic: this.saveForm.calculationLogic,
          dataSourceId: this.currentDataSource,
          sqlStatement: this.sqlContent,
          templateType: this.saveForm.templateType,
          status: 'DRAFT' // 草稿状态
        }

        // 如果是编辑模式，添加模型ID
        if (this.modelData && this.modelData.modelId) {
          saveData.modelId = this.modelData.modelId
        }

        console.log('保存数据模型:', saveData)

        // 调用保存接口
        const response = await saveDataModel(saveData)

        console.log('保存响应:', response)

        if (response.code === 1) {
          const isEdit = this.modelData && this.modelData.modelId
          this.$message.success(isEdit ? '数据模型更新成功' : '数据模型保存成功')
          this.saveDialogVisible = false
          this.$emit('refresh') // 通知父组件刷新列表

          // 如果是保存并关闭，则关闭主对话框
          if (this.autoCloseAfterSave) {
            this.autoCloseAfterSave = false
            this.handleClose()
          }
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        console.error('保存数据模型失败:', error)
        if (error.message) {
          this.$message.error('保存失败：' + error.message)
        } else {
          this.$message.error('保存失败，请稍后重试')
        }
      } finally {
        this.saveLoading = false
      }
    },

    // 执行SQL
    async handleExecute() {
      if (!this.sqlContent.trim()) {
        this.$message.warning('请输入SQL语句')
        return
      }

      if (!this.currentDataSource) {
        this.$message.warning('请选择数据源')
        return
      }

      this.executionResult.loading = true
      this.executionResult.error = null
      this.resultTab = 'result'

      try {
        const response = await testDataModelSql({
          sqlContent: this.sqlContent,
          dataSourceId: this.currentDataSource,
          parameters: {}
        })

        if (response.code === 1) {
          const result = response.data
          console.log('SQL执行成功，返回数据:', result) // 调试日志
          console.log('数据行数:', result.data ? result.data.length : 0) // 调试日志
          console.log('列信息:', result.columns) // 调试日志

          this.executionResult = {
            loading: false,
            data: result.data || [],
            columns: result.columns || [],
            rowCount: result.rowCount || 0,
            executionTime: result.executionTime || 0,
            error: null
          }

          console.log('设置后的executionResult:', this.executionResult) // 调试日志

          // 显示成功消息
          this.$message.success(`SQL执行成功，返回 ${result.rowCount || 0} 条记录`)
        } else {
          this.executionResult = {
            loading: false,
            data: null,
            columns: [],
            rowCount: 0,
            executionTime: 0,
            error: response.msg || '执行失败'
          }
        }
      } catch (error) {
        this.executionResult = {
          loading: false,
          data: null,
          columns: [],
          rowCount: 0,
          executionTime: 0,
          error: '执行失败: ' + error.message
        }
      }
    },

    // 格式化SQL
    handleFormat() {
      if (!this.sqlContent.trim()) {
        this.$message.warning('请输入SQL语句')
        return
      }

      try {
        // 保存当前状态到历史记录
        this.addToHistory(this.sqlContent)

        // 基础SQL格式化逻辑
        let formattedSql = this.formatSqlContent(this.sqlContent)
        this.sqlContent = formattedSql

        // 保存格式化后的状态到历史记录
        this.addToHistory(formattedSql)

        this.$message.success('SQL格式化完成')
      } catch (error) {
        console.error('SQL格式化失败:', error)
        this.$message.error('SQL格式化失败，请检查语法')
      }
    },

    // SQL格式化核心逻辑
    formatSqlContent(sql) {
      if (!sql || typeof sql !== 'string') {
        return sql
      }

      try {
        // 移除多余的空白字符
        let formatted = sql.replace(/\s+/g, ' ').trim()

        // SQL关键字列表
        const keywords = [
          'SELECT', 'FROM', 'WHERE', 'JOIN', 'INNER JOIN', 'LEFT JOIN', 'RIGHT JOIN', 'FULL JOIN',
          'ON', 'AND', 'OR', 'ORDER BY', 'GROUP BY', 'HAVING', 'UNION', 'UNION ALL',
          'INSERT', 'INTO', 'VALUES', 'UPDATE', 'SET', 'DELETE', 'CREATE', 'TABLE',
          'ALTER', 'DROP', 'INDEX', 'PRIMARY KEY', 'FOREIGN KEY', 'CONSTRAINT',
          'AS', 'DISTINCT', 'COUNT', 'SUM', 'AVG', 'MAX', 'MIN', 'CASE', 'WHEN', 'THEN', 'ELSE', 'END',
          'LIMIT', 'OFFSET', 'FETCH', 'FIRST', 'ROWS', 'ONLY', 'WITH', 'RECURSIVE'
        ]

        // 格式化关键字 - 转换为大写
        keywords.forEach(keyword => {
          const regex = new RegExp(`\\b${keyword}\\b`, 'gi')
          formatted = formatted.replace(regex, keyword.toUpperCase())
        })

        // 处理字符串和注释，避免格式化其中的内容
        const stringPattern = /'[^']*'/g
        const commentPattern = /--[^\n]*/g
        const strings = []
        const comments = []

        // 保存字符串
        formatted = formatted.replace(stringPattern, (match) => {
          strings.push(match)
          return `__STRING_${strings.length - 1}__`
        })

        // 保存注释
        formatted = formatted.replace(commentPattern, (match) => {
          comments.push(match)
          return `__COMMENT_${comments.length - 1}__`
        })

        // 添加换行和缩进
        formatted = formatted
          // SELECT 子句
          .replace(/\bSELECT\b/gi, '\nSELECT')
          .replace(/\bFROM\b/gi, '\nFROM')
          .replace(/\bWHERE\b/gi, '\nWHERE')
          .replace(/\bAND\b/gi, '\n  AND')
          .replace(/\bOR\b/gi, '\n  OR')
          // JOIN 子句
          .replace(/\b(INNER\s+JOIN|LEFT\s+JOIN|RIGHT\s+JOIN|FULL\s+JOIN|JOIN)\b/gi, '\n$1')
          .replace(/\bON\b/gi, '\n  ON')
          // 分组和排序
          .replace(/\bGROUP\s+BY\b/gi, '\nGROUP BY')
          .replace(/\bHAVING\b/gi, '\nHAVING')
          .replace(/\bORDER\s+BY\b/gi, '\nORDER BY')
          // UNION
          .replace(/\bUNION(\s+ALL)?\b/gi, '\nUNION$1')
          // INSERT, UPDATE, DELETE
          .replace(/\bINSERT\s+INTO\b/gi, '\nINSERT INTO')
          .replace(/\bVALUES\b/gi, '\nVALUES')
          .replace(/\bUPDATE\b/gi, '\nUPDATE')
          .replace(/\bSET\b/gi, '\nSET')
          .replace(/\bDELETE\s+FROM\b/gi, '\nDELETE FROM')
          // 子查询处理
          .replace(/\(\s*SELECT/gi, '(\n  SELECT')
          .replace(/\)\s*([,\s])/gi, '\n)$1')

        // 处理逗号后的换行（但不影响函数参数）
        formatted = formatted.replace(/,(?![^()]*\))/g, ',\n  ')

        // 恢复字符串
        strings.forEach((str, index) => {
          formatted = formatted.replace(`__STRING_${index}__`, str)
        })

        // 恢复注释
        comments.forEach((comment, index) => {
          formatted = formatted.replace(`__COMMENT_${index}__`, comment)
        })

        // 清理多余的空行和空格
        formatted = formatted
          .replace(/\n\s*\n/g, '\n')
          .replace(/^\s+/gm, (match) => {
            // 保持缩进结构，每级缩进2个空格
            const depth = Math.floor(match.length / 2)
            return '  '.repeat(depth)
          })
          .trim()

        return formatted
      } catch (error) {
        console.error('格式化过程中出错:', error)
        return sql // 出错时返回原始SQL
      }
    },

    // 验证SQL
    async handleValidate() {
      if (!this.sqlContent.trim()) {
        this.$message.warning('请输入SQL语句')
        return
      }

      this.validationResult.loading = true
      this.resultTab = 'validation'

      try {
        const startTime = Date.now()
        const response = await validateSqlSyntax({
          sqlContent: this.sqlContent,
          databaseType: 'DM' // 默认达梦数据库
        })
        const endTime = Date.now()
        const validationTime = endTime - startTime

        if (response.code === 1) {
          this.validationResult = {
            loading: false,
            valid: response.data.valid,
            message: response.data.message,
            warnings: response.data.warnings || [],
            errors: response.data.errors || [],
            validationTime: validationTime
          }

          // 显示验证结果消息
          if (response.data.valid) {
            this.$message.success(`SQL验证通过 (${validationTime}ms)`)
          } else {
            this.$message.error('SQL验证失败，请查看详细信息')
          }
        } else {
          this.validationResult = {
            loading: false,
            valid: false,
            message: response.msg || '验证失败',
            warnings: [],
            errors: [response.msg || '验证失败'],
            validationTime: validationTime
          }
          this.$message.error('SQL验证失败: ' + (response.msg || '未知错误'))
        }
      } catch (error) {
        console.error('SQL验证失败:', error)
        this.validationResult = {
          loading: false,
          valid: false,
          message: '验证失败: ' + error.message,
          warnings: [],
          errors: ['网络错误: ' + error.message],
          validationTime: 0
        }
        this.$message.error('验证失败: ' + error.message)
      }
    },

    // 撤销
    handleUndo() {
      if (this.sqlHistory.length > 1 && this.sqlHistoryIndex > 0) {
        this.sqlHistoryIndex--
        this.sqlContent = this.sqlHistory[this.sqlHistoryIndex]
        this.$message.success('撤销成功')
      } else {
        this.$message.info('没有可撤销的操作')
      }
    },

    // 重做
    handleRedo() {
      if (this.sqlHistoryIndex < this.sqlHistory.length - 1) {
        this.sqlHistoryIndex++
        this.sqlContent = this.sqlHistory[this.sqlHistoryIndex]
        this.$message.success('重做成功')
      } else {
        this.$message.info('没有可重做的操作')
      }
    },

    // 添加到历史记录
    addToHistory(sql) {
      // 如果当前不是最新状态，删除后面的历史
      if (this.sqlHistoryIndex < this.sqlHistory.length - 1) {
        this.sqlHistory = this.sqlHistory.slice(0, this.sqlHistoryIndex + 1)
      }

      // 添加新的历史记录
      this.sqlHistory.push(sql)
      this.sqlHistoryIndex = this.sqlHistory.length - 1

      // 限制历史记录数量
      if (this.sqlHistory.length > 50) {
        this.sqlHistory.shift()
        this.sqlHistoryIndex--
      }
    },

    // 清空
    handleClear() {
      this.sqlContent = ''
    },

    // 保存并关闭
    async handleSaveAndClose() {
      if (!this.sqlContent.trim()) {
        this.$message.warning('请输入SQL语句')
        return
      }

      if (!this.currentDataSource) {
        this.$message.warning('请选择数据源')
        return
      }

      // 重置保存表单
      this.saveForm = {
        modelCode: '',
        modelName: '',
        modelType: 'RISK_MONITOR',
        businessMeaning: '',
        calculationLogic: '',
        templateType: 'CUSTOM'
      }

      // 显示保存对话框，保存成功后自动关闭
      this.saveDialogVisible = true
      this.autoCloseAfterSave = true
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.sqlContent = ''
      this.currentDataSource = ''
      this.executionResult = { loading: false, data: null, columns: [], rowCount: 0, executionTime: 0, error: null }
      this.validationResult = { loading: false, valid: null, message: '', warnings: [], errors: [] }
      // 重置分页
      this.tablePagination = {
        pageNum: 1,
        pageSize: 15,
        total: 0
      }

      // 清理保存表单
      this.saveForm = {
        modelCode: '',
        modelName: '',
        modelType: 'RISK',
        businessMeaning: '',
        calculationLogic: '',
        templateType: 'CUSTOM'
      }
      this.saveDialogVisible = false
      this.autoCloseAfterSave = false

      // 通知父组件清理模型数据
      this.$emit('update:model-data', null)
    },

    // 处理表分页切换
    handleTablePageChange(pageNum) {
      this.tablePagination.pageNum = pageNum
      this.loadTableStructure(this.currentDataSource, pageNum)
    },

    // 表节点右键菜单
    handleTableNodeRightClick(event, data) {
      event.preventDefault()
      // 可以在这里添加右键菜单逻辑
    },

    // 表操作处理
    handleTableAction(action, table) {
      console.log('表操作:', action, '表数据:', table) // 调试日志

      switch (action) {
        case 'select':
          this.generateSelectSql(table)
          break
        case 'insert':
          this.generateInsertSql(table)
          break
        case 'update':
          this.generateUpdateSql(table)
          break
        case 'delete':
          this.generateDeleteSql(table)
          break
        case 'tableName':
          this.insertTableName(table)
          break
        default:
          console.warn('未知的表操作:', action)
      }
    },

    // 仅插入表名
    insertTableName(table) {
      if (!table || !table.tableName) {
        this.$message.error('表数据无效')
        return
      }
      this.insertText(table.tableName)
      this.$message.success(`已插入表名: ${table.tableName}`)
    },

    // 生成SELECT语句
    generateSelectSql(table) {
      console.log('生成SELECT语句，表数据:', table) // 调试日志

      if (!table || !table.tableName) {
        console.error('表数据无效:', table)
        this.$message.error('表数据无效，无法生成SQL语句')
        return
      }

      const tableName = table.tableName
      const columns = table.children && table.children.length > 0
        ? table.children.map(col => col.columnName).join(', ')
        : '*'

      const sql = `SELECT ${columns}\nFROM ${tableName}`
      this.insertText(sql)
      this.$message.success(`已生成 ${tableName} 的SELECT语句`)
    },

    // 生成INSERT语句
    generateInsertSql(table) {
      if (!table || !table.tableName) {
        this.$message.error('表数据无效，无法生成SQL语句')
        return
      }

      const tableName = table.tableName

      if (!table.children || table.children.length === 0) {
        this.insertText(`INSERT INTO ${tableName} VALUES ()`)
        this.$message.success(`已生成 ${tableName} 的INSERT语句`)
        return
      }

      const columns = table.children.map(col => col.columnName).join(', ')
      const values = table.children.map(() => '?').join(', ')
      const sql = `INSERT INTO ${tableName} (${columns})\nVALUES (${values})`
      this.insertText(sql)
      this.$message.success(`已生成 ${tableName} 的INSERT语句`)
    },

    // 生成UPDATE语句
    generateUpdateSql(table) {
      if (!table || !table.tableName) {
        this.$message.error('表数据无效，无法生成SQL语句')
        return
      }

      const tableName = table.tableName

      if (!table.children || table.children.length === 0) {
        this.insertText(`UPDATE ${tableName} SET column = value WHERE condition`)
        this.$message.success(`已生成 ${tableName} 的UPDATE语句`)
        return
      }

      const setClause = table.children.slice(0, 3).map(col => `${col.columnName} = ?`).join(',\n       ')
      const sql = `UPDATE ${tableName}\nSET ${setClause}\nWHERE condition = ?`
      this.insertText(sql)
      this.$message.success(`已生成 ${tableName} 的UPDATE语句`)
    },

    // 生成DELETE语句
    generateDeleteSql(table) {
      if (!table || !table.tableName) {
        this.$message.error('表数据无效，无法生成SQL语句')
        return
      }

      const tableName = table.tableName
      const sql = `DELETE FROM ${tableName}\nWHERE condition = ?`
      this.insertText(sql)
      this.$message.success(`已生成 ${tableName} 的DELETE语句`)
    },

    // 快速构建器方法
    startSelectBuilder() {
      this.insertText('SELECT column1, column2\nFROM table_name\nWHERE condition = value')
      this.$message.info('已插入SELECT模板，请修改表名和列名')
    },

    startInsertBuilder() {
      this.insertText('INSERT INTO table_name (column1, column2)\nVALUES (value1, value2)')
      this.$message.info('已插入INSERT模板，请修改表名和字段')
    },

    startUpdateBuilder() {
      this.insertText('UPDATE table_name\nSET column1 = value1,\n    column2 = value2\nWHERE condition = value')
      this.$message.info('已插入UPDATE模板，请修改表名和字段')
    },

    startDeleteBuilder() {
      this.insertText('DELETE FROM table_name\nWHERE condition = value')
      this.$message.info('已插入DELETE模板，请修改表名和条件')
    }
  }
}
</script>

<style scoped>
.sql-editor-dialog {
  height: 90vh;
}

.sql-editor-container {
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

.visual-builder,
.sql-editor,
.result-panel {
  height: 100%;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background: #fff;
}

.builder-header,
.editor-header {
  padding: 15px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.builder-header h4,
.editor-header h4 {
  margin: 0;
}

.builder-content {
  padding: 15px;
  height: calc(100% - 60px);
  overflow-y: auto;
}

.table-selector h5,
.sql-components h5,
.function-library h5 {
  margin: 0 0 10px 0;
  color: #303133;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.node-label {
  flex: 1;
  cursor: pointer;
  padding: 2px 4px;
  border-radius: 2px;
  transition: background-color 0.2s;
}

.node-label:hover {
  background-color: #f5f7fa;
}

.table-actions {
  opacity: 0;
  transition: opacity 0.3s;
}

.custom-tree-node:hover .table-actions {
  opacity: 1;
}

.quick-actions {
  margin-bottom: 15px;
}

.quick-actions .el-button {
  width: 100%;
  margin-bottom: 5px;
  text-align: left;
}

.sql-components h6 {
  margin: 15px 0 10px 0;
  color: #606266;
  font-size: 12px;
  font-weight: normal;
}

.table-pagination {
  margin-top: 10px;
  text-align: center;
}

.table-pagination .el-pagination {
  padding: 0;
}

.component-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.component-item {
  padding: 8px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  text-align: center;
  cursor: grab;
  transition: all 0.3s;
  user-select: none;
}

.component-item:hover {
  border-color: #409eff;
  background-color: #ecf5ff;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(64, 158, 255, 0.2);
}

.component-item:active {
  cursor: grabbing;
  transform: translateY(0);
}

/* 查询条件构建器样式 */
.condition-builder {
  margin-top: 20px;
}

.condition-builder h6 {
  margin: 0 0 10px 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.condition-list {
  display: grid;
  grid-template-columns: 1fr;
  gap: 6px;
  max-height: 300px;
  overflow-y: auto;
}

.condition-item {
  padding: 6px 8px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  user-select: none;
  display: flex;
  align-items: center;
  font-size: 12px;
}

.condition-item:hover {
  border-color: #67c23a;
  background-color: #f0f9ff;
  transform: translateX(2px);
  box-shadow: 0 2px 4px rgba(103, 194, 58, 0.2);
}

.condition-item:active {
  transform: translateX(0);
}

.condition-item i {
  margin-right: 6px;
  color: #67c23a;
}

.condition-item span {
  flex: 1;
  text-align: left;
}

/* 智能条件构建器样式 */
.smart-condition-builder {
  margin-bottom: 15px;
}

.smart-condition-form {
  padding: 10px 0;
}

.condition-preview {
  margin-top: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e9ecef;
}

.condition-preview h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #303133;
}

.preview-code {
  background-color: #2d3748;
  color: #e2e8f0;
  padding: 12px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.4;
  min-height: 20px;
  word-break: break-all;
}

.code-editor {
  position: relative;
}

.code-editor.drag-over {
  border: 2px dashed #409eff;
  background-color: rgba(64, 158, 255, 0.05);
}

.function-item {
  padding: 8px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
}

.function-item:hover {
  background-color: #f5f7fa;
}

.function-name {
  font-weight: bold;
  color: #409eff;
}

.function-desc {
  font-size: 12px;
  color: #909399;
  margin-left: 10px;
}

.code-editor {
  height: calc(100% - 120px);
  padding: 15px;
}

.sql-textarea {
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

.sql-info {
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

.result-panel {
  height: 100%;
}

.result-content,
.validation-content,
.plan-content {
  padding: 15px;
  height: calc(100% - 40px);
  overflow-y: auto;
}

.result-stats {
  margin-bottom: 15px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.result-stats p {
  margin: 0;
  font-size: 12px;
  color: #606266;
}

.result-note {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
  text-align: center;
}

.loading-container,
.error-container,
.success-container,
.empty-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
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
}

.template-list .template-item {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  cursor: pointer;
}

.template-item:hover {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.template-item h6 {
  margin: 0 0 5px 0;
  color: #303133;
}

.template-item p {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

.dialog-footer {
  text-align: right;
}

/* 导入SQL对话框样式 */
.import-sql-container {
  padding: 0;
}

.search-area {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 4px;
}

.pagination-container {
  margin-top: 20px;
  text-align: center;
}

.import-sql-container .el-table {
  border: 1px solid #ebeef5;
}

.import-sql-container .el-table th {
  background: #fafafa;
}

.import-sql-container .el-table .current-row {
  background: #ecf5ff;
}

.import-sql-container .el-table .current-row:hover {
  background: #d9ecff;
}

/* 智能条件构建对话框层级控制 */
::v-deep .smart-condition-dialog {
  z-index: 3000 !important;
}

::v-deep .smart-condition-dialog .el-dialog {
  z-index: 3000 !important;
}

::v-deep .smart-condition-dialog + .v-modal {
  z-index: 2999 !important;
}

/* 确保智能条件构建对话框的遮罩层不会阻挡交互 */
::v-deep .smart-condition-dialog .el-overlay {
  z-index: 2999 !important;
}

/* 修复可能的遮罩层问题 */
::v-deep .el-dialog__wrapper.smart-condition-dialog {
  z-index: 3000 !important;
}

::v-deep .el-dialog__wrapper.smart-condition-dialog .el-dialog {
  z-index: 3000 !important;
}
</style>
