<template>
  <el-dialog
    title="指标配置"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :lock-scroll="true"
    custom-class="indicator-config-dialog risk-mxgl-sjmxgl-page"
    @close="handleClose"
  >
    <div class="config-container risk-mxgl-sjmxgl-page">
      <!-- 左侧：可用指标 -->
      <div class="available-indicators">
        <div class="panel-header">
          <h3>可用指标</h3>
          <div class="search-box">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索指标"
              prefix-icon="el-icon-search"
              size="mini"
              @input="handleSearch"
              clearable
            />
          </div>
        </div>
        
        <div class="indicator-tabs">
          <el-tabs v-model="activeTab" @tab-click="handleTabClick">
            <el-tab-pane label="数据模型" name="model">
              <div class="indicator-list">
                <div v-if="modelIndicators.length === 0" style="padding: 20px; text-align: center; color: #999;">
                  <p>暂无数据模型</p>
                </div>

                <div
                  v-for="indicator in modelIndicators"
                  :key="indicator.indicatorId || indicator.id"
                  class="indicator-item"
                  draggable="true"
                  @dragstart="handleDragStart($event, indicator)"
                >
                  <div class="indicator-header">
                    <span class="indicator-name">{{ indicator.indicatorName || indicator.name || indicator.modelName || '未知指标' }}</span>
                    <el-button
                      type="text"
                      size="mini"
                      @click="addIndicator(indicator)"
                    >
                      添加
                    </el-button>
                  </div>
                  <div class="indicator-meta">
                    <span>编码: {{ indicator.indicatorCode || indicator.code || indicator.modelCode || '-' }}</span>
                    <span>类型: {{ indicator.indicatorType || indicator.type || indicator.modelType || '-' }}</span>
                  </div>
                  <div class="indicator-description">
                    {{ indicator.description || indicator.desc || '暂无描述' }}
                  </div>
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="SQL模板" name="template">
              <div class="indicator-list">
                <div v-if="sqlTemplates.length === 0" style="padding: 20px; text-align: center; color: #999;">
                  <p>暂无SQL模板</p>
                </div>

                <div
                  v-for="template in sqlTemplates"
                  :key="template.templateId || template.id"
                  class="indicator-item"
                  draggable="true"
                  @dragstart="handleDragStart($event, template, 'template')"
                >
                  <div class="indicator-header">
                    <span class="indicator-name">{{ template.templateName || template.name || '未知模板' }}</span>
                    <el-button
                      type="text"
                      size="mini"
                      @click="addTemplate(template)"
                    >
                      添加
                    </el-button>
                  </div>
                  <div class="indicator-meta">
                    <span>编码: {{ template.templateCode || template.code || '-' }}</span>
                    <span>类型: {{ template.templateType || template.type || '-' }}</span>
                  </div>
                  <div class="indicator-description">
                    {{ template.description || template.desc || '暂无描述' }}
                  </div>
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="新建指标" name="new">
              <div class="new-indicator-form">
                <!-- 创建方式选择 -->
                <div class="creation-mode-selector">
                  <h4>选择创建方式</h4>
                  <div class="mode-options">
                    <div class="mode-option" @click="openFormMode">
                      <div class="mode-icon">
                        <i class="el-icon-edit"></i>
                      </div>
                      <div class="mode-content">
                        <h5>表单模式</h5>
                        <p>传统的表单填写方式，适合熟悉SQL的用户</p>
                      </div>
                      <div class="mode-action">
                        <el-button size="small" type="primary">使用表单</el-button>
                      </div>
                    </div>

                    <div class="mode-option" @click="openDragMode">
                      <div class="mode-icon">
                        <i class="el-icon-s-grid"></i>
                      </div>
                      <div class="mode-content">
                        <h5>拖拽模式</h5>
                        <p>可视化的拖拽操作，从字段库构建SQL语句</p>
                      </div>
                      <div class="mode-action">
                        <el-button size="small" type="success">使用拖拽</el-button>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 表单模式 -->
                <div v-if="showFormMode" class="form-mode-content">
                  <el-divider content-position="left">表单填写</el-divider>
                  <el-form
                    ref="newIndicatorForm"
                    :model="newIndicatorData"
                    :rules="newIndicatorRules"
                    label-width="100px"
                    size="mini"
                  >
                    <el-form-item label="指标名称" prop="indicatorName">
                      <el-input v-model="newIndicatorData.indicatorName" placeholder="请输入指标名称" />
                    </el-form-item>
                    <el-form-item label="指标编码" prop="indicatorCode">
                      <el-input v-model="newIndicatorData.indicatorCode" placeholder="请输入指标编码" />
                    </el-form-item>
                    <el-form-item label="数据源" prop="dataSourceId">
                      <el-select
                        v-model="newIndicatorData.dataSourceId"
                        placeholder="请选择数据源"
                        style="width: 100%"
                        @change="handleDataSourceChange"
                      >
                        <el-option
                          v-for="item in dataSourceList"
                          :key="item.sourceId"
                          :label="item.sourceName"
                          :value="item.sourceId"
                        />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="SQL语句" prop="sqlContent">
                      <el-input
                        v-model="newIndicatorData.sqlContent"
                        type="textarea"
                        :rows="6"
                        placeholder="请输入SQL语句"
                      />
                    </el-form-item>
                    <el-form-item label="描述">
                      <el-input
                        v-model="newIndicatorData.description"
                        type="textarea"
                        :rows="2"
                        placeholder="请输入指标描述"
                      />
                    </el-form-item>
                    <el-form-item>
                      <el-button type="primary" @click="addNewIndicator">添加到组合</el-button>
                      <el-button @click="resetNewIndicator">重置</el-button>
                      <el-button @click="switchToDragMode">切换到拖拽模式</el-button>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>

      <!-- 右侧：已配置指标 -->
      <div class="configured-indicators">
        <div class="panel-header">
          <h3>已配置指标</h3>
          <div class="header-actions">
            <el-button type="primary" size="mini" @click="handleSaveAll">
              保存配置
            </el-button>
          </div>
        </div>
        
        <div
          class="drop-zone"
          @drop="handleDrop"
          @dragover.prevent
          @dragenter.prevent
        >
          <div v-if="configuredIndicators.length === 0" class="empty-state">
            <i class="el-icon-plus"></i>
            <p>拖拽指标到此处或点击"添加"按钮</p>
          </div>
          
          <div v-else class="configured-list">
            <div
              v-for="(config, index) in configuredIndicators"
              :key="config.configId || index"
              class="configured-item"
            >
              <div class="item-header">
                <div class="order-control">
                  <span class="order-number">{{ config.executionOrder }}</span>
                  <div class="order-buttons">
                    <el-button
                      type="text"
                      size="mini"
                      icon="el-icon-arrow-up"
                      :disabled="index === 0"
                      @click="moveUp(index)"
                    />
                    <el-button
                      type="text"
                      size="mini"
                      icon="el-icon-arrow-down"
                      :disabled="index === configuredIndicators.length - 1"
                      @click="moveDown(index)"
                    />
                  </div>
                </div>
                
                <div class="item-info">
                  <h4>{{ config.indicatorName }}</h4>
                  <span class="indicator-code">{{ config.indicatorCode }}</span>
                </div>
                
                <div class="item-actions">
                  <el-switch
                    v-model="config.isEnabled"
                    active-text="启用"
                    inactive-text="禁用"
                    size="mini"
                  />
                  <el-button
                    type="text"
                    size="mini"
                    @click="editIndicatorSQL(config, index)"
                  >
                    编辑SQL
                  </el-button>
                  <el-button
                    type="text"
                    size="mini"
                    @click="editIndicatorConfig(config, index)"
                  >
                    配置参数
                  </el-button>
                  <el-button
                    type="text"
                    size="mini"
                    @click="removeIndicator(index)"
                  >
                    移除
                  </el-button>
                </div>
              </div>
              
              <div class="item-content">
                <div class="config-summary">
                  <span v-if="config.dependencyConfig && Object.keys(config.dependencyConfig).length > 0">
                    <i class="el-icon-link"></i> 有依赖关系
                  </span>
                  <span v-if="config.parameterMapping && Object.keys(config.parameterMapping).length > 0">
                    <i class="el-icon-setting"></i> 有参数映射
                  </span>
                  <span v-if="config.filterCondition">
                    <i class="el-icon-filter"></i> 有筛选条件
                  </span>
                </div>
                
                <div class="sql-preview">
                  <el-collapse>
                    <el-collapse-item title="查看SQL" :name="index">
                      <pre class="sql-content">{{ config.sqlContent || '暂无SQL内容' }}</pre>
                    </el-collapse-item>
                  </el-collapse>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 指标详细配置对话框 -->
    <el-dialog
      title="指标详细配置"
      :visible.sync="detailConfigVisible"
      width="800px"
      :close-on-click-modal="false"
      append-to-body
    >
      <el-form
        v-if="editingConfig"
        ref="detailConfigForm"
        :model="editingConfig"
        label-width="120px"
        size="mini"
      >
        <el-form-item label="指标名称">
          <el-input v-model="editingConfig.indicatorName" />
        </el-form-item>
        
        <el-form-item label="执行顺序">
          <el-input-number
            v-model="editingConfig.executionOrder"
            :min="1"
            :max="configuredIndicators.length"
            style="width: 200px;"
          />
        </el-form-item>
        
        <el-form-item label="参数映射">
          <div class="parameter-mapping">
            <div
              v-for="(value, key) in editingConfig.parameterMapping"
              :key="key"
              class="mapping-item"
            >
              <el-input v-model="key" placeholder="参数名" style="width: 200px;" />
              <span style="margin: 0 8px;">=</span>
              <el-input v-model="editingConfig.parameterMapping[key]" placeholder="映射值" style="width: 200px;" />
              <el-button type="text" icon="el-icon-delete" @click="removeParameterMapping(key)" />
            </div>
            <el-button type="text" @click="addParameterMapping">
              <i class="el-icon-plus"></i> 添加参数映射
            </el-button>
          </div>
        </el-form-item>
        
        <el-form-item label="依赖配置">
          <el-select
            v-model="editingConfig.dependsOn"
            multiple
            placeholder="选择依赖的指标"
            style="width: 100%;"
          >
            <el-option
              v-for="indicator in configuredIndicators"
              :key="indicator.configId || indicator.indicatorCode"
              :label="indicator.indicatorName"
              :value="indicator.indicatorCode"
              :disabled="indicator.indicatorCode === editingConfig.indicatorCode"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="筛选条件">
          <el-input
            v-model="editingConfig.filterCondition"
            type="textarea"
            :rows="3"
            placeholder="请输入筛选条件，如：amount > 1000"
          />
        </el-form-item>
        
        <el-form-item label="超时设置">
          <el-input-number
            v-model="editingConfig.timeoutSeconds"
            :min="1"
            :max="3600"
            style="width: 200px;"
          />
          <span style="margin-left: 8px;">秒</span>
        </el-form-item>
        
        <el-form-item label="是否启用">
          <el-switch v-model="editingConfig.isEnabled" />
        </el-form-item>
      </el-form>
      
      <div slot="footer">
        <el-button @click="detailConfigVisible = false">取消</el-button>
        <el-button type="primary" @click="saveDetailConfig">保存</el-button>
      </div>
    </el-dialog>

    <!-- SQL可视化编辑器对话框 -->
    <el-dialog
      :title="isEditingExistingIndicator ? `编辑指标SQL - ${sqlIndicatorData.indicatorName}` : 'SQL可视化编辑器 - 新建指标'"
      :visible.sync="sqlEditorVisible"
      width="90%"
      :close-on-click-modal="false"
      append-to-body
      custom-class="sql-editor-dialog"
    >
      <div class="sql-editor-main-container">
        <!-- 指标基本信息 -->
        <div class="indicator-info-panel">
          <el-form :model="sqlIndicatorData" label-width="100px" size="small">
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="指标名称" required>
                  <el-input v-model="sqlIndicatorData.indicatorName" placeholder="请输入指标名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="指标编码" required>
                  <el-input v-model="sqlIndicatorData.indicatorCode" placeholder="请输入指标编码" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="指标分类">
                  <el-select v-model="sqlIndicatorData.category" placeholder="请选择分类" style="width: 100%">
                    <el-option label="财务指标" value="financial" />
                    <el-option label="风险指标" value="risk" />
                    <el-option label="运营指标" value="operation" />
                    <el-option label="合规指标" value="compliance" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="数据源">
                  <el-select
                    v-model="sqlIndicatorData.dataSourceId"
                    placeholder="请选择数据源"
                    style="width: 100%"
                    @change="handleSqlDataSourceChange"
                  >
                    <el-option
                      v-for="ds in dataSourceList"
                      :key="ds.sourceId"
                      :label="ds.sourceName"
                      :value="ds.sourceId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="指标描述">
              <el-input
                v-model="sqlIndicatorData.description"
                type="textarea"
                :rows="2"
                placeholder="请输入指标描述"
              />
            </el-form-item>

            <!-- 参数映射配置 -->
            <el-form-item label="参数映射">
              <div class="parameter-mapping">
                <div
                  v-for="(value, key) in sqlIndicatorData.parameterMapping"
                  :key="key"
                  class="mapping-item"
                >
                  <el-input v-model="key" placeholder="参数名" style="width: 150px;" />
                  <span style="margin: 0 8px;">=</span>
                  <el-input v-model="sqlIndicatorData.parameterMapping[key]" placeholder="映射值" style="width: 150px;" />
                  <el-button type="text" icon="el-icon-delete" @click="removeSqlParameterMapping(key)" />
                </div>
                <el-button type="text" @click="addSqlParameterMapping">
                  <i class="el-icon-plus"></i> 添加参数映射
                </el-button>
                <el-button type="text" @click="autoDetectSqlParameters" style="margin-left: 8px;">
                  <i class="el-icon-magic-stick"></i> 自动检测参数
                </el-button>
              </div>
            </el-form-item>
          </el-form>
        </div>

        <!-- SQL可视化编辑器 -->
        <div class="sql-editor-panel">
          <div class="sql-editor-container">
            <!-- 工具栏 -->
            <div class="editor-toolbar">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-button-group>
                    <el-button type="primary" @click="handleNewSql" icon="el-icon-document">新建</el-button>
                    <el-button @click="handleOpenTemplate" icon="el-icon-folder-opened">模板</el-button>
                    <el-button @click="handleSave" icon="el-icon-check">保存</el-button>
                    <el-button @click="handleExecute" icon="el-icon-video-play">执行</el-button>
                  </el-button-group>
                </el-col>
                <el-col :span="12" style="text-align: right;">
                  <el-button @click="handleFormat" icon="el-icon-magic-stick" title="格式化SQL">格式化</el-button>
                  <el-button @click="handleValidate" icon="el-icon-circle-check" title="验证SQL语法">验证</el-button>
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
                      <!-- 数据表选择 -->
                      <div class="table-selector">
                        <h5>数据表</h5>

                        <!-- 表名搜索框 -->
                        <div v-if="sqlIndicatorData.dataSourceId && !tableLoading" class="table-search">
                          <el-input
                            v-model="tableSearchKeyword"
                            placeholder="搜索表名..."
                            prefix-icon="el-icon-search"
                            size="small"
                            clearable
                            @input="handleTableSearch"
                            @clear="handleTableSearchClear"
                          />
                        </div>

                        <div v-if="tableLoading" class="table-loading" v-loading="tableLoading">
                          <i class="el-icon-loading"></i>
                          <span>正在加载数据表...</span>
                        </div>
                        <div v-else-if="!sqlIndicatorData.dataSourceId" class="table-empty">
                          <i class="el-icon-info"></i>
                          <span>请先选择数据源</span>
                        </div>
                        <div v-else-if="tableTreeData.length === 0" class="table-empty">
                          <i class="el-icon-warning"></i>
                          <span v-if="tableSearchKeyword">未找到匹配的数据表</span>
                          <span v-else>该数据源下暂无数据表</span>
                        </div>
                        <el-tree
                          v-else
                          ref="tableTree"
                          :key="`tree-${sqlIndicatorData.dataSourceId}-${tableTreeData.length}-${treeRefreshKey}`"
                          :data="tableTreeData"
                          :props="treeProps"
                          node-key="id"
                          :default-expand-all="false"
                          @node-click="handleTableNodeClick"
                          @node-expand="handleTableNodeExpand"
                        >
                          <span class="custom-tree-node" slot-scope="{ node, data }">
                            <span
                              class="node-label"
                              :class="{
                                'table-node': data.type === 'table',
                                'column-node': data.type === 'column',
                                'loading-node': data.type === 'loading'
                              }"
                              :draggable="data.type === 'table' || data.type === 'column'"
                              @dragstart="handleDragStart($event, data)"
                              @click="handleTableNameClick(data, $event)"
                            >
                              <i :class="getTreeNodeIcon(data.type)"></i>
                              {{ node.label }}
                            </span>
                            <span v-if="data.type === 'column'">
                              <el-tag size="mini" type="info">{{ data.dataType }}</el-tag>
                            </span>
                          </span>
                        </el-tree>

                        <!-- 分页组件 -->
                        <div v-if="tableTreeData.length > 0 && tablePagination.total > tablePagination.pageSize" class="table-pagination">
                          <el-pagination
                            @current-change="handleTablePageChange"
                            :current-page="tablePagination.currentPage"
                            :page-size="tablePagination.pageSize"
                            :total="tablePagination.total"
                            layout="prev, pager, next, total"
                            small
                          />
                        </div>
                      </div>

                      <!-- SQL构建器 -->
                      <div class="sql-builder">
                        <h5>SQL构建器</h5>
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

                        <!-- SQL组件 -->
                        <div class="sql-components">
                          <h6>SQL组件</h6>
                          <div class="component-list">
                            <div
                              v-for="component in sqlComponents"
                              :key="component.type"
                              class="component-item"
                              draggable="true"
                              @dragstart="handleComponentDragStart($event, component)"
                              @click="insertSqlComponent(component)"
                            >
                              <i :class="component.icon"></i>
                              <span>{{ component.label }}</span>
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
                              draggable="true"
                              @dragstart="handleFunctionDragStart($event, func)"
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
                      @dragover.prevent="handleDragOver"
                      @dragenter.prevent="handleDragEnter"
                      @dragleave="handleDragLeave"
                      @drop="handleDrop"
                      :class="{ 'drag-over': isDragOver }"
                    >
                      <textarea
                        v-model="sqlIndicatorData.sqlContent"
                        class="sql-textarea"
                        placeholder="请输入SQL语句或从左侧拖拽表名、字段、SQL组件..."
                        @input="handleSqlChange"
                        ref="sqlTextarea"
                      ></textarea>

                      <!-- 拖拽提示层 -->
                      <div v-if="isDragOver" class="drag-overlay">
                        <div class="drag-hint">
                          <i class="el-icon-upload2"></i>
                          <p>释放以插入到光标位置</p>
                        </div>
                      </div>
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
                          <div v-if="executionLoading" class="loading-container" v-loading="true" element-loading-text="正在执行SQL...">
                            <div style="height: 200px;"></div>
                          </div>
                          <div v-else-if="executionResult && executionResult.records" class="result-table">
                            <el-table
                              :data="executionResult.records.slice(0, 10)"
                              size="mini"
                              max-height="300"
                              border
                            >
                              <el-table-column
                                v-for="column in resultColumns"
                                :key="column.prop"
                                :prop="column.prop"
                                :label="column.label"
                                :width="column.width"
                                show-overflow-tooltip
                              />
                            </el-table>
                            <div class="result-summary">
                              共 {{ executionResult.total || executionResult.records.length }} 条记录
                            </div>
                          </div>
                          <div v-else class="empty-result">
                            <i class="el-icon-data-line"></i>
                            <p>暂无执行结果</p>
                          </div>
                        </div>
                      </el-tab-pane>
                      <el-tab-pane label="执行日志" name="log">
                        <div class="log-content">
                          <div v-for="(log, index) in executionLogs" :key="index" class="log-item">
                            <span class="log-time">{{ log.time }}</span>
                            <span class="log-message" :class="log.type">{{ log.message }}</span>
                          </div>
                        </div>
                      </el-tab-pane>
                    </el-tabs>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCancelEdit">取消</el-button>
        <el-button @click="resetSQLIndicator">重置</el-button>
        <el-button type="primary" @click="saveSQLIndicator">
          {{ isEditingExistingIndicator ? '更新指标' : '保存指标' }}
        </el-button>
      </div>
    </el-dialog>

    <!-- SQL模板选择对话框 -->
    <SqlTemplateSelectDialog
      :visible.sync="templateDialogVisible"
      @select="handleTemplateSelect"
    />


  </el-dialog>
</template>

<script>
import {
  getAvailableIndicators,
  getDataModelList,
  getSqlTemplateList,
  addIndicatorToCombination,
  removeIndicatorFromCombination,
  updateIndicatorConfig,
  getDataSourceList,
  getTableList,
  getTableColumns,
  getSqlTemplateDetail,
  useSqlTemplate,
  executeSQL
} from '@/api/mxgl'
import SqlVisualEditor from './SqlVisualEditor.vue'
import SqlTemplateSelectDialog from './SqlTemplateSelectDialog.vue'

export default {
  name: 'IndicatorConfigDialog',
  components: {
    SqlVisualEditor,
    SqlTemplateSelectDialog
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    combination: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      
      // 搜索和标签页
      searchKeyword: '',
      activeTab: 'model',
      
      // 数据
      modelIndicators: [],
      sqlTemplates: [],
      configuredIndicators: [],
      dataSourceList: [],
      
      // 新建指标
      newIndicatorData: {
        indicatorName: '',
        indicatorCode: '',
        dataSourceId: '',
        sqlContent: '',
        description: ''
      },
      newIndicatorRules: {
        indicatorName: [
          { required: true, message: '请输入指标名称', trigger: 'blur' }
        ],
        indicatorCode: [
          { required: true, message: '请输入指标编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' }
        ],
        dataSourceId: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ],
        sqlContent: [
          { required: true, message: '请输入SQL语句', trigger: 'blur' }
        ]
      },

      // 创建模式
      showFormMode: false,

      // SQL可视化编辑器
      sqlEditorVisible: false,
      sqlIndicatorData: {
        indicatorName: '',
        indicatorCode: '',
        description: '',
        category: '',
        dataSourceId: '',
        sqlContent: '',
        parameterMapping: {}
      },

      // 模板相关
      templateDialogVisible: false,

      // SQL编辑器相关数据
      visualMode: true,
      tableTreeData: [],
      tableLoading: false,
      treeProps: {
        children: 'children',
        label: 'label',
        isLeaf: 'leaf'  // 添加isLeaf属性映射，让树组件根据leaf属性判断是否显示展开图标
      },

      // 分页相关数据
      tablePagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0,
        pages: 0
      },

      // 树组件刷新key
      treeRefreshKey: 0,

      // 表名搜索相关数据
      tableSearchKeyword: '',  // 搜索关键词
      searchTimer: null,       // 搜索防抖定时器
      activeFunctionGroups: ['string', 'date'],
      assistantTab: 'syntax',
      resultTab: 'result',
      executionLoading: false,
      executionResult: null,
      executionLogs: [],

      // 拖拽状态
      isDragOver: false,
      dragData: null,

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

      // 函数库
      functionGroups: [
        {
          name: 'string',
          label: '字符串函数',
          functions: [
            { name: 'UPPER()', description: '转换为大写' },
            { name: 'LOWER()', description: '转换为小写' },
            { name: 'LENGTH()', description: '获取字符串长度' },
            { name: 'SUBSTRING()', description: '截取子字符串' },
            { name: 'CONCAT()', description: '连接字符串' },
            { name: 'TRIM()', description: '去除空格' }
          ]
        },
        {
          name: 'date',
          label: '日期函数',
          functions: [
            { name: 'NOW()', description: '当前日期时间' },
            { name: 'DATE_FORMAT()', description: '格式化日期' },
            { name: 'YEAR()', description: '获取年份' },
            { name: 'MONTH()', description: '获取月份' },
            { name: 'DAY()', description: '获取日期' },
            { name: 'DATEDIFF()', description: '计算日期差' }
          ]
        },
        {
          name: 'aggregate',
          label: '聚合函数',
          functions: [
            { name: 'COUNT()', description: '计数' },
            { name: 'SUM()', description: '求和' },
            { name: 'AVG()', description: '平均值' },
            { name: 'MAX()', description: '最大值' },
            { name: 'MIN()', description: '最小值' }
          ]
        }
      ],

      // 语法提示
      syntaxHints: [
        {
          keyword: 'SELECT',
          description: '用于从数据库中选择数据',
          example: 'SELECT column1, column2 FROM table_name;'
        },
        {
          keyword: 'WHERE',
          description: '用于过滤记录',
          example: 'SELECT * FROM table_name WHERE condition;'
        },
        {
          keyword: 'JOIN',
          description: '用于连接两个或多个表',
          example: 'SELECT * FROM table1 JOIN table2 ON table1.id = table2.id;'
        }
      ],



      // 详细配置
      detailConfigVisible: false,
      editingConfig: null,
      editingIndex: -1,
      isEditingExistingIndicator: false, // 标记是否在编辑已存在的指标

      // 加载状态
      loading: false
    }
  },
  computed: {
    // SQL行数
    sqlLineCount() {
      return this.sqlIndicatorData.sqlContent ? this.sqlIndicatorData.sqlContent.split('\n').length : 0
    },

    // SQL字符数
    sqlCharCount() {
      return this.sqlIndicatorData.sqlContent ? this.sqlIndicatorData.sqlContent.length : 0
    },

    // SQL状态
    sqlStatus() {
      if (!this.sqlIndicatorData.sqlContent.trim()) {
        return '空'
      }
      return '已编辑'
    },

    // SQL状态样式
    sqlStatusClass() {
      if (!this.sqlIndicatorData.sqlContent.trim()) {
        return 'status-empty'
      }
      return 'status-edited'
    },

    // 结果表格列
    resultColumns() {
      if (!this.executionResult || !this.executionResult.records || this.executionResult.records.length === 0) {
        return []
      }

      const firstRow = this.executionResult.records[0]
      return Object.keys(firstRow).map(key => ({
        prop: key,
        label: key.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase()),
        width: Math.max(120, key.length * 10 + 40)
      }))
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val) {
          this.loadData()
        }
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  mounted() {
    // 初始化SQL编辑器数据
    this.loadTableData()
  },
  methods: {
    // 加载数据
    async loadData() {
      await Promise.all([
        this.loadAvailableIndicators(),
        this.loadSqlTemplates(),
        this.loadConfiguredIndicators(),
        this.loadDataSources()
      ])
    },

    // 加载可用指标（数据模型列表）
    async loadAvailableIndicators() {
      try {
        console.log('📊 指标配置对话框 - 加载可用指标')
        const queryParams = {
          pageNum: 1,
          pageSize: 100,
          modelName: this.searchKeyword || '',
          status: '',
          modelType: ''
        }
        console.log('📊 指标配置对话框 - 查询参数:', queryParams)

        const response = await getDataModelList(queryParams)
        console.log('📊 指标配置对话框 - 接口响应:', response)

        // 兼容两种响应格式
        if (response && response.code === 1) {
          let modelData = []
          if (response.data) {
            modelData = response.data.records || response.data.list || response.data || []
          }
          this.modelIndicators = modelData
          console.log('✅ 可用指标加载成功(标准格式):', this.modelIndicators.length, '条数据')
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.modelIndicators = response.records || []
          console.log('✅ 可用指标加载成功(直接格式):', this.modelIndicators.length, '条数据')
        } else if (response && Array.isArray(response)) {
          // 数组格式
          this.modelIndicators = response || []
          console.log('✅ 可用指标加载成功(数组格式):', this.modelIndicators.length, '条数据')
        } else {
          this.$message.error(response.msg || '加载数据模型列表失败')
          console.error('❌ 可用指标加载失败:', response)
        }
      } catch (error) {
        console.error('❌ 加载数据模型列表失败:', error)
        this.$message.error('加载数据模型列表失败')
      }
    },

    // 加载SQL模板
    async loadSqlTemplates() {
      try {
        console.log('📊 指标配置对话框 - 加载SQL模板')
        const queryParams = {
          pageNum: 1,
          pageSize: 100,
          templateName: this.searchKeyword || '',
          status: '',
          templateType: ''
        }
        console.log('📊 指标配置对话框 - 查询参数:', queryParams)

        const response = await getSqlTemplateList(queryParams)
        console.log('📊 指标配置对话框 - 接口响应:', response)

        // 兼容两种响应格式
        if (response && response.code === 1) {
          let templateData = []
          if (response.data) {
            templateData = response.data.records || response.data.list || response.data || []
          }
          this.sqlTemplates = templateData
          console.log('✅ SQL模板加载成功(标准格式):', this.sqlTemplates.length, '条数据')
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.sqlTemplates = response.records || []
          console.log('✅ SQL模板加载成功(直接格式):', this.sqlTemplates.length, '条数据')
        } else if (response && Array.isArray(response)) {
          // 数组格式
          this.sqlTemplates = response || []
          console.log('✅ SQL模板加载成功(数组格式):', this.sqlTemplates.length, '条数据')
        } else {
          this.$message.error(response.msg || '加载SQL模板失败')
          console.error('❌ SQL模板加载失败:', response)
        }
      } catch (error) {
        console.error('❌ 加载SQL模板失败:', error)
        this.$message.error('加载SQL模板失败')
      }
    },

    // 加载已配置指标
    loadConfiguredIndicators() {
      if (this.combination && this.combination.indicators) {
        this.configuredIndicators = [...this.combination.indicators].sort((a, b) => a.executionOrder - b.executionOrder)
      } else {
        this.configuredIndicators = []
      }
    },

    // 加载数据源列表
    async loadDataSources() {
      try {
        console.log('📊 指标配置对话框 - 加载数据源列表')
        const response = await getDataSourceList({
          pageNum: 1,
          pageSize: 1000,
          status: 'ACTIVE'
        })
        console.log('📊 指标配置对话框 - 接口响应:', response)

        // 兼容两种响应格式
        if (response && response.code === 1) {
          this.dataSourceList = response.data?.records || []
          console.log('✅ 数据源列表加载成功(标准格式):', this.dataSourceList.length, '个数据源')
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.dataSourceList = response.records || []
          console.log('✅ 数据源列表加载成功(直接格式):', this.dataSourceList.length, '个数据源')
        } else if (response && Array.isArray(response)) {
          // 数组格式
          this.dataSourceList = response || []
          console.log('✅ 数据源列表加载成功(数组格式):', this.dataSourceList.length, '个数据源')
        } else {
          this.$message.error(response.msg || '加载数据源失败')
          console.error('❌ 数据源列表加载失败:', response)
        }
      } catch (error) {
        console.error('❌ 加载数据源失败:', error)
        this.$message.error('加载数据源失败')
      }
    },

    // 搜索
    handleSearch() {
      if (this.activeTab === 'model') {
        this.loadAvailableIndicators()
      } else if (this.activeTab === 'template') {
        this.loadSqlTemplates()
      }
    },

    // 标签页切换
    handleTabClick(tab) {
      this.activeTab = tab.name
    },

    // 拖拽开始
    handleDragStart(event, item, type = 'indicator') {
      event.dataTransfer.setData('text/plain', JSON.stringify({ item, type }))
    },

    // 拖拽放置
    handleDrop(event) {
      event.preventDefault()
      const data = JSON.parse(event.dataTransfer.getData('text/plain'))
      
      if (data.type === 'template') {
        this.addTemplate(data.item)
      } else {
        this.addIndicator(data.item)
      }
    },

    // 添加指标（数据模型）
    addIndicator(model) {
      const config = {
        configId: this.generateId(),
        // 数据模型字段映射
        indicatorId: model.modelId || model.id,
        indicatorName: model.modelName || model.name || model.indicatorName || '未知模型',
        indicatorCode: model.modelCode || model.code || model.indicatorCode || '',
        sqlContent: model.sqlContent || model.sql || '',
        description: model.description || model.desc || '',
        // 配置信息
        executionOrder: this.configuredIndicators.length + 1,
        isEnabled: true,
        parameterMapping: {},
        dependencyConfig: {},
        dependsOn: [],
        filterCondition: '',
        timeoutSeconds: 300,
        // 数据模型特有字段
        modelType: model.modelType || model.type || '',
        dataSourceId: model.dataSourceId || ''
      }

      this.configuredIndicators.push(config)
      this.$message.success(`已添加数据模型: ${config.indicatorName}`)
    },

    // 添加模板
    addTemplate(template) {
      const config = {
        configId: this.generateId(),
        // SQL模板字段映射
        templateId: template.templateId || template.id,
        indicatorName: template.templateName || template.name || '未知模板',
        indicatorCode: template.templateCode || template.code || '',
        sqlContent: template.sqlTemplate || template.sql || template.content || '',
        description: template.description || template.desc || '',
        // 配置信息
        executionOrder: this.configuredIndicators.length + 1,
        isEnabled: true,
        parameterMapping: {},
        dependencyConfig: {},
        dependsOn: [],
        filterCondition: '',
        timeoutSeconds: 300,
        // 模板特有字段
        templateType: template.templateType || template.type || ''
      }

      this.configuredIndicators.push(config)
      this.$message.success(`已添加SQL模板: ${config.indicatorName}`)
    },

    // 添加新建指标
    async addNewIndicator() {
      try {
        await this.$refs.newIndicatorForm.validate()
        
        const config = {
          configId: this.generateId(),
          indicatorName: this.newIndicatorData.indicatorName,
          indicatorCode: this.newIndicatorData.indicatorCode,
          dataSourceId: this.newIndicatorData.dataSourceId,
          sqlContent: this.newIndicatorData.sqlContent,
          description: this.newIndicatorData.description,
          executionOrder: this.configuredIndicators.length + 1,
          isEnabled: true,
          parameterMapping: {},
          dependencyConfig: {},
          dependsOn: [],
          filterCondition: '',
          timeoutSeconds: 300
        }
        
        this.configuredIndicators.push(config)
        this.resetNewIndicator()
        this.$message.success('指标添加成功')
      } catch (error) {
        // 表单验证失败
      }
    },

    // 处理拖拽式指标构建器保存
    handleNewIndicatorSave(indicatorData) {
      try {
        const config = {
          configId: this.generateId(),
          indicatorName: indicatorData.indicatorName,
          indicatorCode: indicatorData.indicatorCode,
          sqlContent: indicatorData.sqlContent,
          description: indicatorData.description,
          executionOrder: this.configuredIndicators.length + 1,
          isEnabled: true,
          parameterMapping: {},
          dependencyConfig: {},
          dependsOn: [],
          filterCondition: '',
          timeoutSeconds: 300,
          parameters: indicatorData.parameters || []
        }

        this.configuredIndicators.push(config)
        this.$message.success('指标添加成功')
      } catch (error) {
        console.error('添加指标失败:', error)
        this.$message.error('添加指标失败')
      }
    },

    // 重置新建指标表单
    resetNewIndicator() {
      this.newIndicatorData = {
        indicatorName: '',
        indicatorCode: '',
        dataSourceId: '',
        sqlContent: '',
        description: ''
      }
      if (this.$refs.newIndicatorForm) {
        this.$refs.newIndicatorForm.clearValidate()
      }
      this.showFormMode = false
    },

    // 数据源变化处理
    handleDataSourceChange(dataSourceId) {
      console.log('选择的数据源:', dataSourceId)
      // 可以在这里加载对应数据源的表结构等信息
    },

    // SQL模式数据源变化处理
    handleSqlDataSourceChange(dataSourceId) {
      console.log('SQL模式选择的数据源:', dataSourceId)
      this.sqlIndicatorData.dataSourceId = dataSourceId
      // 重置分页
      this.tablePagination.currentPage = 1
      // 加载对应数据源的表结构
      this.loadTableData()
    },

    // 分页处理
    handleTablePageChange(page) {
      console.log('切换到第', page, '页')
      this.tablePagination.currentPage = page
      this.loadTableData()
    },

    // 表名搜索处理（带防抖）
    handleTableSearch(keyword) {
      console.log('搜索表名:', keyword)

      // 清除之前的定时器
      if (this.searchTimer) {
        clearTimeout(this.searchTimer)
      }

      // 设置新的定时器，500ms后执行搜索
      this.searchTimer = setTimeout(() => {
        // 重置分页到第一页
        this.tablePagination.currentPage = 1
        // 重新加载数据（会自动带上搜索关键词）
        this.loadTableData()
      }, 500)
    },

    // 清空搜索
    handleTableSearchClear() {
      console.log('清空搜索')

      // 清除搜索定时器
      if (this.searchTimer) {
        clearTimeout(this.searchTimer)
        this.searchTimer = null
      }

      this.tableSearchKeyword = ''
      // 重置分页到第一页
      this.tablePagination.currentPage = 1
      // 重新加载数据
      this.loadTableData()
    },

    // 树节点展开处理（懒加载字段信息）
    async handleTableNodeExpand(data, node, instance) {
      console.log('节点展开:', data, node)

      // 只处理表节点的展开，且未加载过字段的情况
      if (data.type === 'table' && !data.isLazyLoaded) {
        console.log(`懒加载表 ${data.name} 的字段信息...`)

        try {
          // 显示加载状态
          const loadingNode = {
            id: `loading_${data.name}`,
            label: '正在加载字段...',
            type: 'loading',
            name: 'LOADING',
            leaf: true  // 加载节点也是叶子节点
          }
          this.$set(data, 'children', [loadingNode])

          // 调用字段接口获取真实字段数据
          const fieldsResponse = await getTableColumns({
            dataSourceId: data.dataSourceId,
            tableName: data.name
          })

          console.log(`表 ${data.name} 字段响应:`, fieldsResponse)

          // 兼容多种字段响应结构
          let fields = []
          if (fieldsResponse.code === 1 || fieldsResponse.code === '1') {
            if (fieldsResponse.data) {
              if (Array.isArray(fieldsResponse.data)) {
                fields = fieldsResponse.data
              } else if (fieldsResponse.data.records && Array.isArray(fieldsResponse.data.records)) {
                fields = fieldsResponse.data.records
              } else if (fieldsResponse.data.list && Array.isArray(fieldsResponse.data.list)) {
                fields = fieldsResponse.data.list
              }
            }
          }

          console.log(`表 ${data.name} 解析出的字段:`, fields)

          if (fields && fields.length > 0) {
            const fieldNodes = fields.map((field, index) => {
              // 兼容多种字段名格式 - 优先使用大写字段名（后端返回格式）
              const fieldName = field.COLUMN_NAME || field.columnName || field.name || field.column_name || field.fieldName
              const dataType = field.DATA_TYPE || field.dataType || field.type || field.data_type || field.columnType || field.COLUMN_TYPE || 'VARCHAR'

              return {
                id: `col_${data.name}_${fieldName}_${index}`,
                label: fieldName,
                type: 'column',
                name: fieldName,
                dataType: dataType,
                leaf: true  // 字段节点是叶子节点，不可展开
              }
            })
            this.$set(data, 'children', fieldNodes)
            console.log(`表 ${data.name} 成功懒加载 ${fieldNodes.length} 个字段`)
          } else {
            console.warn(`表 ${data.name} 没有字段数据`)
            this.$set(data, 'children', [{
              id: `col_${data.name}_no_fields`,
              label: '暂无字段信息',
              type: 'column',
              name: 'NO_FIELDS',
              dataType: 'INFO',
              leaf: true  // 无字段节点也是叶子节点
            }])
          }

          // 标记已加载
          this.$set(data, 'isLazyLoaded', true)

          // 更新标签，移除字段数量显示
          this.$set(data, 'label', data.name)

          // 强制刷新树组件
          this.treeRefreshKey++
          this.$nextTick(() => {
            this.$forceUpdate()
            // 如果是手动调用的展开，需要手动展开节点
            if (instance === null) {
              const treeRef = this.$refs.tableTree
              if (treeRef && node) {
                // 确保节点展开状态
                const nodeInstance = treeRef.store.nodesMap[node.key]
                if (nodeInstance) {
                  nodeInstance.expanded = true
                  nodeInstance.loaded = true
                }
              }
            }
          })

        } catch (error) {
          console.error(`懒加载表 ${data.name} 字段失败:`, error)
          this.$set(data, 'children', [{
            id: `col_${data.name}_error`,
            label: '字段加载失败',
            type: 'column',
            name: 'ERROR',
            dataType: 'ERROR',
            leaf: true  // 错误节点也是叶子节点
          }])
        }
      }
    },

    // 打开表单模式
    openFormMode() {
      this.showFormMode = true
    },

    // 打开SQL可视化编辑器
    openDragMode() {
      this.sqlIndicatorData = {
        indicatorName: '',
        indicatorCode: '',
        description: '',
        category: '',
        dataSourceId: '',
        sqlContent: '',
        parameterMapping: {}
      }
      this.sqlEditorVisible = true
    },

    // 切换到SQL可视化编辑器
    switchToDragMode() {
      this.sqlIndicatorData = {
        indicatorName: this.newIndicatorData.indicatorName,
        indicatorCode: this.newIndicatorData.indicatorCode,
        description: this.newIndicatorData.description,
        category: this.newIndicatorData.category || '',
        dataSourceId: this.newIndicatorData.dataSourceId || '',
        sqlContent: '',
        parameterMapping: {}
      }
      this.sqlEditorVisible = true
    },

    // 保存SQL可视化编辑器指标
    saveSQLIndicator() {
      // 验证必填字段
      if (!this.sqlIndicatorData.indicatorName.trim()) {
        this.$message.warning('请输入指标名称')
        return
      }
      if (!this.sqlIndicatorData.indicatorCode.trim()) {
        this.$message.warning('请输入指标编码')
        return
      }
      if (!this.sqlIndicatorData.dataSourceId) {
        this.$message.warning('请选择数据源')
        return
      }
      if (!this.sqlIndicatorData.sqlContent.trim()) {
        this.$message.warning('请输入SQL语句')
        return
      }

      try {
        if (this.isEditingExistingIndicator && this.editingIndex >= 0) {
          // 编辑模式：更新现有指标
          const updatedConfig = {
            ...this.editingConfig, // 保留原有的configId等信息
            indicatorName: this.sqlIndicatorData.indicatorName,
            indicatorCode: this.sqlIndicatorData.indicatorCode,
            sqlContent: this.sqlIndicatorData.sqlContent,
            description: this.sqlIndicatorData.description,
            category: this.sqlIndicatorData.category,
            dataSourceId: this.sqlIndicatorData.dataSourceId,
            parameterMapping: this.sqlIndicatorData.parameterMapping || {},
            updateTime: new Date().toLocaleString()
          }

          this.$set(this.configuredIndicators, this.editingIndex, updatedConfig)
          this.$message.success(`指标"${updatedConfig.indicatorName}"更新成功`)
        } else {
          // 新建模式：添加新指标
          const config = {
            configId: this.generateId(),
            indicatorName: this.sqlIndicatorData.indicatorName,
            indicatorCode: this.sqlIndicatorData.indicatorCode,
            sqlContent: this.sqlIndicatorData.sqlContent,
            description: this.sqlIndicatorData.description,
            category: this.sqlIndicatorData.category,
            dataSourceId: this.sqlIndicatorData.dataSourceId,
            executionOrder: this.configuredIndicators.length + 1,
            isEnabled: true,
            parameterMapping: this.sqlIndicatorData.parameterMapping || {},
            dependencyConfig: {},
            dependsOn: [],
            filterCondition: '',
            timeoutSeconds: 300,
            parameters: []
          }

          this.configuredIndicators.push(config)
          this.$message.success('指标创建成功')
        }

        // 重置状态
        this.sqlEditorVisible = false
        this.resetSQLIndicator()
        this.resetEditingState()
      } catch (error) {
        console.error('保存指标失败:', error)
        this.$message.error('保存指标失败')
      }
    },

    // 重置SQL指标数据
    resetSQLIndicator() {
      this.sqlIndicatorData = {
        indicatorName: '',
        indicatorCode: '',
        description: '',
        category: '',
        dataSourceId: '',
        sqlContent: '',
        parameterMapping: {}
      }
    },

    // 重置编辑状态
    resetEditingState() {
      this.isEditingExistingIndicator = false
      this.editingConfig = null
      this.editingIndex = -1
    },

    // 取消编辑
    handleCancelEdit() {
      if (this.isEditingExistingIndicator) {
        this.$confirm('确定要取消编辑吗？未保存的更改将丢失。', '取消编辑', {
          confirmButtonText: '确定',
          cancelButtonText: '继续编辑',
          type: 'warning'
        }).then(() => {
          this.sqlEditorVisible = false
          this.resetSQLIndicator()
          this.resetEditingState()
        }).catch(() => {
          // 用户选择继续编辑，不做任何操作
        })
      } else {
        this.sqlEditorVisible = false
        this.resetSQLIndicator()
        this.resetEditingState()
      }
    },

    // 处理SQL执行成功
    handleSQLExecuteSuccess(result) {
      console.log('SQL执行成功:', result)
      this.$message.success('SQL执行成功')
    },

    // 处理SQL执行错误
    handleSQLExecuteError(error) {
      console.error('SQL执行失败:', error)
      this.$message.error('SQL执行失败: ' + error)
    },

    // 处理SQL导出
    handleSQLExport(exportData) {
      console.log('导出数据:', exportData)
      this.$message.success('导出功能开发中...')
    },

    // SQL编辑器相关方法
    handleNewSql() {
      this.$confirm('确定要清空当前SQL内容吗？', '新建SQL', {
        type: 'warning'
      }).then(() => {
        this.sqlIndicatorData.sqlContent = ''
        this.executionResult = null
        this.executionLogs = []
        this.$message.success('已清空')
      }).catch(() => {})
    },

    handleOpenTemplate() {
      // 打开模板选择对话框
      this.templateDialogVisible = true
    },

    // 处理模板选择
    async handleTemplateSelect(template) {
      try {
        console.log('📊 指标配置 - 选择模板:', template)
        this.loading = true

        // 获取模板详情
        const response = await getSqlTemplateDetail(template.templateId)
        console.log('📊 指标配置 - 模板详情响应:', response)

        // 兼容两种响应格式
        let templateDetail = null
        if (response && response.code === 1) {
          templateDetail = response.data
          console.log('✅ 模板详情加载成功(标准格式)')
        } else if (response && response.sqlContent) {
          // 直接格式（mock数据）
          templateDetail = response
          console.log('✅ 模板详情加载成功(直接格式)')
        } else {
          this.$message.error(response.msg || '获取模板详情失败')
          console.error('❌ 模板详情加载失败:', response)
          return
        }

        // 应用模板内容到SQL编辑器
        this.sqlIndicatorData.sqlContent = templateDetail.sqlContent

        // 自动检测模板中的参数
        this.$nextTick(() => {
          this.autoDetectSqlParameters()
        })

        // 记录模板使用
        await useSqlTemplate(template.templateId)

        this.$message.success('模板应用成功')
        this.templateDialogVisible = false
      } catch (error) {
        console.error('❌ 应用模板失败:', error)
        this.$message.error('应用模板失败')
      } finally {
        this.loading = false
      }
    },

    handleSave() {
      this.saveSQLIndicator()
    },

    async handleExecute() {
      if (!this.sqlIndicatorData.sqlContent.trim()) {
        this.$message.warning('请输入SQL语句')
        return
      }

      if (!this.sqlIndicatorData.dataSourceId) {
        this.$message.warning('请选择数据源')
        return
      }

      try {
        this.executionLoading = true
        this.addExecutionLog('开始执行SQL...', 'info')

        const startTime = Date.now()

        // 处理参数替换
        const parameterMapping = this.sqlIndicatorData.parameterMapping || {}
        let processedSqlContent

        try {
          // 先处理指标引用（模拟）
          processedSqlContent = this.processIndicatorReferencesForTest(this.sqlIndicatorData.sqlContent)
          // 再处理普通参数替换
          processedSqlContent = this.processSqlParameters(processedSqlContent, parameterMapping)
          this.addExecutionLog(`参数替换成功，共替换 ${Object.keys(parameterMapping).length} 个参数`, 'info')
          this.addExecutionLog(`执行SQL: ${processedSqlContent}`, 'info')
        } catch (error) {
          this.addExecutionLog(`参数替换失败: ${error.message}`, 'error')
          this.$message.error(`参数替换失败: ${error.message}`)
          return
        }

        // 调用真正的后端API
        // 🔥 修复：添加 indicatorCode 参数，用于步骤引用时从历史记录中获取前几步的SQL
        // 🔥 使用 combination prop，并处理可能为 null 的情况
        const indicatorCode = this.combination
          ? (this.combination.combinationCode || this.combination.combinationId || 'DEFAULT_INDICATOR')
          : 'DEFAULT_INDICATOR'

        const response = await executeSQL({
          dataSourceId: this.sqlIndicatorData.dataSourceId,
          sqlContent: processedSqlContent,
          parameters: JSON.stringify(parameterMapping),
          indicatorCode: indicatorCode  // 🔥 新增
        })

        console.log('📊 指标配置 - SQL执行响应:', response)
        const executionTime = Date.now() - startTime

        // 兼容两种响应格式
        if (response && response.code === 1) {
          // 处理成功响应
          this.executionResult = {
            records: response.data.records || response.data.data || [],
            total: response.data.total || (response.data.records ? response.data.records.length : 0),
            columns: response.data.columns || []
          }

          this.addExecutionLog(`SQL执行成功，耗时 ${executionTime}ms，返回 ${this.executionResult.total} 条记录`, 'success')
          this.$message.success(`SQL执行成功，耗时 ${executionTime}ms`)
          console.log('✅ SQL执行成功')
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.executionResult = {
            records: response.records || response.data || [],
            total: response.total || (response.records ? response.records.length : 0),
            columns: response.columns || []
          }

          this.addExecutionLog(`SQL执行成功，耗时 ${executionTime}ms，返回 ${this.executionResult.total} 条记录`, 'success')
          this.$message.success(`SQL执行成功，耗时 ${executionTime}ms`)
          console.log('✅ SQL执行成功(直接格式)')
        } else {
          // 处理业务错误
          this.addExecutionLog(`SQL执行失败: ${response.msg}`, 'error')
          this.$message.error(response.msg || 'SQL执行失败')
          console.error('❌ SQL执行失败:', response)
        }

      } catch (error) {
        console.error('SQL执行失败:', error)
        this.addExecutionLog(`SQL执行失败: ${error.message}`, 'error')
        this.$message.error('SQL执行失败: ' + error.message)
      } finally {
        this.executionLoading = false
      }
    },

    handleFormat() {
      if (!this.sqlIndicatorData.sqlContent.trim()) return

      // 简单的SQL格式化
      let formatted = this.sqlIndicatorData.sqlContent
        .replace(/\s+/g, ' ')
        .replace(/\s*,\s*/g, ',\n  ')
        .replace(/\s*(SELECT|FROM|WHERE|GROUP BY|ORDER BY|HAVING|JOIN|LEFT JOIN|RIGHT JOIN|INNER JOIN)\s+/gi, '\n$1 ')
        .replace(/\s*AND\s+/gi, '\n  AND ')
        .replace(/\s*OR\s+/gi, '\n  OR ')
        .trim()

      this.sqlIndicatorData.sqlContent = formatted
      this.$message.success('SQL格式化完成')
    },

    // 🔥 处理指标引用（直接发送给后端处理，不生成模拟数据）
    processIndicatorReferencesForTest(sqlContent) {
      // 检查是否包含引用语法
      const referencePatterns = [
        /\$\{PREV_RESULT\}/g,
        /\$\{STEP_(\d+)_RESULT\}/g,
        /\$\{[A-Z0-9_]+_RESULT\}/g
      ]

      let hasReferences = false
      let referencedSteps = new Set()

      // 检查引用并收集步骤号
      const stepPattern = /\$\{STEP_(\d+)_RESULT\}/g
      let stepMatch
      while ((stepMatch = stepPattern.exec(sqlContent)) !== null) {
        hasReferences = true
        referencedSteps.add(parseInt(stepMatch[1]))
      }

      // 检查其他引用类型
      if (/\$\{PREV_RESULT\}/g.test(sqlContent) || /\$\{[A-Z0-9_]+_RESULT\}/g.test(sqlContent)) {
        hasReferences = true
      }

      if (hasReferences) {
        // 🔥 关键修改：检测到步骤引用时，直接返回原始SQL，让后端处理
        this.addExecutionLog('检测到指标引用语法，将由后端使用真实数据处理', 'info')
        this.addExecutionLog(`发现 ${referencedSteps.size} 个步骤引用，后端将自动构建真实数据查询`, 'info')

        // 直接返回原始SQL，不做任何模拟数据处理
        return sqlContent
      }

      // 没有引用，直接返回
      return sqlContent
    },

    // 为不同步骤生成模拟数据
    generateMockDataForStep(stepNum) {
      switch (stepNum) {
        case 1:
          // 第一步：基础合同数据
          return `SELECT
            'SUPPLIER001' AS SUPPLIER_ID,
            '示例云科技有限公司' AS SUPPLIER_NAME,
            'SERVICE' AS CONTRACT_TYPE,
            1000000 AS CONTRACT_AMOUNT,
            DATE '2024-01-15' AS CONTRACT_SIGN_DATE
          FROM DUAL
          UNION ALL
          SELECT
            'SUPPLIER002' AS SUPPLIER_ID,
            '测试供应商A' AS SUPPLIER_NAME,
            'GOODS' AS CONTRACT_TYPE,
            500000 AS CONTRACT_AMOUNT,
            DATE '2024-02-20' AS CONTRACT_SIGN_DATE
          FROM DUAL
          UNION ALL
          SELECT
            'SUPPLIER003' AS SUPPLIER_ID,
            '示例供应商B' AS SUPPLIER_NAME,
            'SERVICE' AS CONTRACT_TYPE,
            750000 AS CONTRACT_AMOUNT,
            DATE '2024-03-10' AS CONTRACT_SIGN_DATE
          FROM DUAL`

        case 2:
          // 第二步：聚合后的数据
          return `SELECT
            'SUPPLIER001' AS SUPPLIER_ID,
            '示例云科技有限公司' AS SUPPLIER_NAME,
            'SERVICE' AS CONTRACT_TYPE,
            3000000 AS TOTAL_AMOUNT,
            3 AS CONTRACT_COUNT,
            DATE '2024-01-15' AS FIRST_CONTRACT_DATE,
            DATE '2024-03-15' AS LAST_CONTRACT_DATE
          FROM DUAL
          UNION ALL
          SELECT
            'SUPPLIER002' AS SUPPLIER_ID,
            '测试供应商A' AS SUPPLIER_NAME,
            'GOODS' AS CONTRACT_TYPE,
            1500000 AS TOTAL_AMOUNT,
            2 AS CONTRACT_COUNT,
            DATE '2024-02-20' AS FIRST_CONTRACT_DATE,
            DATE '2024-04-20' AS LAST_CONTRACT_DATE
          FROM DUAL
          UNION ALL
          SELECT
            'SUPPLIER003' AS SUPPLIER_ID,
            '示例供应商B' AS SUPPLIER_NAME,
            'SERVICE' AS CONTRACT_TYPE,
            2250000 AS TOTAL_AMOUNT,
            1 AS CONTRACT_COUNT,
            DATE '2024-03-10' AS FIRST_CONTRACT_DATE,
            DATE '2024-05-10' AS LAST_CONTRACT_DATE
          FROM DUAL`

        case 3:
          // 第三步：添加预警字段
          return `SELECT
            'SUPPLIER001' AS SUPPLIER_ID,
            '示例云科技有限公司' AS SUPPLIER_NAME,
            'SERVICE' AS CONTRACT_TYPE,
            3000000 AS TOTAL_AMOUNT,
            3 AS CONTRACT_COUNT,
            DATE '2024-01-15' AS FIRST_CONTRACT_DATE,
            DATE '2024-03-15' AS LAST_CONTRACT_DATE,
            2500000 AS THRESHOLD_VALUE,
            '1' AS IS_WARNING,
            'HIGH' AS WARNING_LEVEL,
            '同一供应商订单总额超标预警' AS WARNING_TYPE
          FROM DUAL
          UNION ALL
          SELECT
            'SUPPLIER002' AS SUPPLIER_ID,
            '测试供应商A' AS SUPPLIER_NAME,
            'GOODS' AS CONTRACT_TYPE,
            1500000 AS TOTAL_AMOUNT,
            2 AS CONTRACT_COUNT,
            DATE '2024-02-20' AS FIRST_CONTRACT_DATE,
            DATE '2024-04-20' AS LAST_CONTRACT_DATE,
            1800000 AS THRESHOLD_VALUE,
            '0' AS IS_WARNING,
            'LOW' AS WARNING_LEVEL,
            '同一供应商订单总额超标预警' AS WARNING_TYPE
          FROM DUAL
          UNION ALL
          SELECT
            'SUPPLIER003' AS SUPPLIER_ID,
            '示例供应商B' AS SUPPLIER_NAME,
            'SERVICE' AS CONTRACT_TYPE,
            2250000 AS TOTAL_AMOUNT,
            1 AS CONTRACT_COUNT,
            DATE '2024-03-10' AS FIRST_CONTRACT_DATE,
            DATE '2024-05-10' AS LAST_CONTRACT_DATE,
            2000000 AS THRESHOLD_VALUE,
            '1' AS IS_WARNING,
            'MEDIUM' AS WARNING_LEVEL,
            '同一供应商订单总额超标预警' AS WARNING_TYPE
          FROM DUAL`

        case 4:
        default:
          // 第四步及以后：包含所有字段
          return `SELECT
            'SUPPLIER001' AS SUPPLIER_ID,
            '示例云科技有限公司' AS SUPPLIER_NAME,
            'SERVICE' AS CONTRACT_TYPE,
            3000000 AS TOTAL_AMOUNT,
            3 AS CONTRACT_COUNT,
            DATE '2024-01-15' AS FIRST_CONTRACT_DATE,
            DATE '2024-03-15' AS LAST_CONTRACT_DATE,
            2500000 AS THRESHOLD_VALUE,
            '1' AS IS_WARNING,
            'HIGH' AS WARNING_LEVEL,
            '同一供应商订单总额超标预警' AS WARNING_TYPE
          FROM DUAL
          UNION ALL
          SELECT
            'SUPPLIER002' AS SUPPLIER_ID,
            '测试供应商A' AS SUPPLIER_NAME,
            'GOODS' AS CONTRACT_TYPE,
            1500000 AS TOTAL_AMOUNT,
            2 AS CONTRACT_COUNT,
            DATE '2024-02-20' AS FIRST_CONTRACT_DATE,
            DATE '2024-04-20' AS LAST_CONTRACT_DATE,
            1800000 AS THRESHOLD_VALUE,
            '0' AS IS_WARNING,
            'LOW' AS WARNING_LEVEL,
            '同一供应商订单总额超标预警' AS WARNING_TYPE
          FROM DUAL
          UNION ALL
          SELECT
            'SUPPLIER003' AS SUPPLIER_ID,
            '示例供应商B' AS SUPPLIER_NAME,
            'SERVICE' AS CONTRACT_TYPE,
            2250000 AS TOTAL_AMOUNT,
            1 AS CONTRACT_COUNT,
            DATE '2024-03-10' AS FIRST_CONTRACT_DATE,
            DATE '2024-05-10' AS LAST_CONTRACT_DATE,
            2000000 AS THRESHOLD_VALUE,
            '1' AS IS_WARNING,
            'MEDIUM' AS WARNING_LEVEL,
            '同一供应商订单总额超标预警' AS WARNING_TYPE
          FROM DUAL`
      }
    },

    handleValidate() {
      if (!this.sqlIndicatorData.sqlContent.trim()) {
        this.$message.warning('请输入SQL语句')
        return
      }

      // 简单的SQL语法验证
      const sql = this.sqlIndicatorData.sqlContent.toUpperCase()
      if (sql.includes('SELECT') || sql.includes('INSERT') || sql.includes('UPDATE') || sql.includes('DELETE')) {
        this.$message.success('SQL语法验证通过')
      } else {
        this.$message.error('SQL语法可能有误')
      }
    },

    handleModeChange(mode) {
      this.visualMode = mode
      if (mode) {
        this.loadTableData()
      }
    },

    handleTableNodeClick(data, node) {
      console.log('树节点点击:', data, node)

      if (data.type === 'table' && !data.isLazyLoaded) {
        // 表节点未展开时，点击直接展开
        this.handleTableNodeExpand(data, node, null)
      } else if (data.type === 'column') {
        // 字段节点点击插入到SQL
        this.insertTextAtCursor(data.name)
      }
    },

    handleTableNameClick(data, event) {
      // 阻止事件冒泡，避免与树组件的点击事件冲突
      if (event) {
        event.stopPropagation()
      }

      // 只处理字段的点击插入，表节点的展开由树组件的node-click处理
      if (data.type === 'column') {
        this.insertTextAtCursor(data.name)
      }
      // 表节点和加载节点不在这里处理
    },

    // 拖拽开始 - 表和字段
    handleDragStart(event, data) {
      console.log('开始拖拽:', data)

      // 加载节点不允许拖拽
      if (data.type === 'loading') {
        event.preventDefault()
        return false
      }

      this.dragData = data

      let dragText = ''
      if (data.type === 'table') {
        dragText = data.name
      } else if (data.type === 'column') {
        dragText = data.name
      }

      event.dataTransfer.setData('text/plain', dragText)
      event.dataTransfer.setData('application/json', JSON.stringify(data))
      event.dataTransfer.effectAllowed = 'copy'

      // 设置拖拽图标
      const dragImage = document.createElement('div')
      dragImage.innerHTML = `<span style="background: #409eff; color: white; padding: 4px 8px; border-radius: 4px; font-size: 12px;">${dragText}</span>`
      dragImage.style.position = 'absolute'
      dragImage.style.top = '-1000px'
      document.body.appendChild(dragImage)
      event.dataTransfer.setDragImage(dragImage, 0, 0)

      setTimeout(() => {
        document.body.removeChild(dragImage)
      }, 0)
    },

    // 拖拽开始 - SQL组件
    handleComponentDragStart(event, component) {
      console.log('开始拖拽SQL组件:', component)
      this.dragData = component

      const templates = {
        'select': 'SELECT ',
        'from': 'FROM ',
        'where': 'WHERE ',
        'join': 'JOIN table_name ON condition',
        'group': 'GROUP BY ',
        'order': 'ORDER BY ',
        'having': 'HAVING ',
        'union': 'UNION '
      }

      const dragText = templates[component.type] || component.label
      event.dataTransfer.setData('text/plain', dragText)
      event.dataTransfer.setData('application/json', JSON.stringify({...component, dragText}))
      event.dataTransfer.effectAllowed = 'copy'
    },

    // 拖拽开始 - 函数
    handleFunctionDragStart(event, func) {
      console.log('开始拖拽函数:', func)
      this.dragData = func

      event.dataTransfer.setData('text/plain', func.name)
      event.dataTransfer.setData('application/json', JSON.stringify(func))
      event.dataTransfer.effectAllowed = 'copy'
    },

    // 拖拽进入
    handleDragEnter(event) {
      event.preventDefault()
      this.isDragOver = true
    },

    // 拖拽悬停
    handleDragOver(event) {
      event.preventDefault()
      event.dataTransfer.dropEffect = 'copy'
    },

    // 拖拽离开
    handleDragLeave(event) {
      // 只有当真正离开编辑器区域时才取消高亮
      const rect = event.currentTarget.getBoundingClientRect()
      const x = event.clientX
      const y = event.clientY

      if (x < rect.left || x > rect.right || y < rect.top || y > rect.bottom) {
        this.isDragOver = false
      }
    },

    // 拖拽释放
    handleDrop(event) {
      event.preventDefault()
      this.isDragOver = false

      try {
        // 获取拖拽的文本
        const dragText = event.dataTransfer.getData('text/plain')
        const dragDataJson = event.dataTransfer.getData('application/json')

        console.log('拖拽释放:', { dragText, dragDataJson })

        if (dragText) {
          // 获取textarea的引用
          const textarea = this.$refs.sqlTextarea
          if (textarea) {
            // 获取当前光标位置
            const cursorPos = textarea.selectionStart
            const textBefore = this.sqlIndicatorData.sqlContent.substring(0, cursorPos)
            const textAfter = this.sqlIndicatorData.sqlContent.substring(cursorPos)

            // 插入拖拽的文本
            this.sqlIndicatorData.sqlContent = textBefore + dragText + textAfter

            // 设置新的光标位置
            this.$nextTick(() => {
              textarea.focus()
              textarea.setSelectionRange(cursorPos + dragText.length, cursorPos + dragText.length)
            })

            this.$message.success(`已插入: ${dragText}`)
          }
        }
      } catch (error) {
        console.error('拖拽处理失败:', error)
        this.$message.error('拖拽插入失败')
      }

      this.dragData = null
    },

    getTreeNodeIcon(type) {
      const iconMap = {
        'table': 'el-icon-s-grid',
        'column': 'el-icon-menu',
        'loading': 'el-icon-loading'
      }
      return iconMap[type] || 'el-icon-document'
    },

    startSelectBuilder() {
      this.insertTextAtCursor('SELECT * FROM ')
    },

    startInsertBuilder() {
      this.insertTextAtCursor('INSERT INTO table_name (column1, column2) VALUES (value1, value2)')
    },

    startUpdateBuilder() {
      this.insertTextAtCursor('UPDATE table_name SET column1 = value1 WHERE condition')
    },

    startDeleteBuilder() {
      this.insertTextAtCursor('DELETE FROM table_name WHERE condition')
    },

    insertSqlComponent(component) {
      const templates = {
        'select': 'SELECT ',
        'from': 'FROM ',
        'where': 'WHERE ',
        'join': 'JOIN table_name ON condition',
        'group': 'GROUP BY ',
        'order': 'ORDER BY ',
        'having': 'HAVING ',
        'union': 'UNION '
      }

      const template = templates[component.type] || component.label
      this.insertTextAtCursor(template)
    },

    handleFunctionClick(func) {
      this.insertTextAtCursor(func.name)
    },

    handleTemplateClick(template) {
      this.sqlIndicatorData.sqlContent = template.sql
      this.$message.success('模板已插入')
    },

    handleUndo() {
      // 撤销功能
      this.$message.info('撤销功能开发中...')
    },

    handleRedo() {
      // 重做功能
      this.$message.info('重做功能开发中...')
    },

    handleClear() {
      this.$confirm('确定要清空SQL内容吗？', '清空确认', {
        type: 'warning'
      }).then(() => {
        this.sqlIndicatorData.sqlContent = ''
        this.$message.success('已清空')
      }).catch(() => {})
    },

    handleSqlChange() {
      // SQL内容变化处理
    },

    insertTextAtCursor(text) {
      const textarea = this.$refs.sqlTextarea
      if (textarea) {
        const start = textarea.selectionStart
        const end = textarea.selectionEnd
        const value = this.sqlIndicatorData.sqlContent

        this.sqlIndicatorData.sqlContent = value.substring(0, start) + text + value.substring(end)

        this.$nextTick(() => {
          textarea.focus()
          textarea.setSelectionRange(start + text.length, start + text.length)
        })
      } else {
        this.sqlIndicatorData.sqlContent += text
      }
    },

    addExecutionLog(message, type = 'info') {
      this.executionLogs.push({
        time: new Date().toLocaleTimeString(),
        message,
        type
      })
    },

    // 清理组合的执行结果数据
    async clearCombinationExecutionResults(combinationId) {
      try {
        // 调用后端API清理执行结果
        const response = await this.$http.post('/api/model/combination/execution-results/clear', {
          combinationId: combinationId
        })

        if (response.data.code === 1) {
          return response.data
        } else {
          throw new Error(response.data.msg || '清理执行结果失败')
        }
      } catch (error) {
        console.error('清理执行结果失败:', error)
        throw error
      }
    },

    // 智能删除指标（先清理执行结果再删除）
    async smartDeleteIndicator(configId) {
      try {
        // 先尝试直接删除
        await removeIndicatorFromCombination(configId)
        return { success: true, message: '指标删除成功' }
      } catch (error) {
        // 如果是外键约束错误，先清理执行结果再删除
        if (error.message && error.message.includes('FK_RESULT_CONFIG')) {
          try {
            // 清理该指标的执行结果
            await this.$http.post('/api/model/combination/execution-results/clear-by-config', {
              configId: configId
            })

            // 再次尝试删除指标
            await removeIndicatorFromCombination(configId)
            return { success: true, message: '清理执行结果后删除成功' }
          } catch (retryError) {
            return { success: false, message: `清理后仍删除失败: ${retryError.message}` }
          }
        } else {
          return { success: false, message: error.message }
        }
      }
    },

    async loadTableData() {
      if (!this.sqlIndicatorData.dataSourceId) {
        this.tableTreeData = []
        return
      }

      try {
        console.log('📊 指标配置 - 加载数据源表结构:', this.sqlIndicatorData.dataSourceId)

        // 显示加载状态
        this.tableLoading = true

        // 根据数据源ID获取对应的表结构数据
        const selectedDataSource = this.dataSourceList.find(ds => ds.sourceId === this.sqlIndicatorData.dataSourceId)
        console.log('📊 指标配置 - 选中的数据源:', selectedDataSource)

        // 调用后端接口获取表列表
        const tableListResponse = await getTableList({
          dataSourceId: this.sqlIndicatorData.dataSourceId,
          pageNum: this.tablePagination.currentPage,  // 修正参数名：pageNum而不是pageNumber
          pageSize: this.tablePagination.pageSize,
          tableName: this.tableSearchKeyword || null  // 添加表名搜索参数
        })

        console.log('📊 指标配置 - 表列表响应:', tableListResponse)
        console.log('📊 指标配置 - 响应数据结构:', JSON.stringify(tableListResponse, null, 2))

        // 兼容多种响应数据结构
        let tables = []
        let total = 0
        let pages = 0

        // 标准格式: {code: 1, data: {records: [...], total: ..., pages: ...}}
        if (tableListResponse && tableListResponse.code === 1) {
          if (tableListResponse.data) {
            if (Array.isArray(tableListResponse.data)) {
              tables = tableListResponse.data
            } else if (tableListResponse.data.records && Array.isArray(tableListResponse.data.records)) {
              tables = tableListResponse.data.records
            } else if (tableListResponse.data.list && Array.isArray(tableListResponse.data.list)) {
              tables = tableListResponse.data.list
            }
            total = tableListResponse.data.total || 0
            pages = tableListResponse.data.pages || 0
          }
          console.log('✅ 表列表加载成功(标准格式):', tables.length, '个表')
        } else if (tableListResponse && tableListResponse.records) {
          // 直接格式（mock数据）: {records: [...], total: ..., pages: ...}
          tables = tableListResponse.records || []
          total = tableListResponse.total || 0
          pages = tableListResponse.pages || 0
          console.log('✅ 表列表加载成功(直接格式):', tables.length, '个表')
        } else if (tableListResponse && Array.isArray(tableListResponse)) {
          // 纯数组格式: [...]
          tables = tableListResponse
          console.log('✅ 表列表加载成功(数组格式):', tables.length, '个表')
        } else {
          console.error('❌ 表列表加载失败，响应格式不匹配:', tableListResponse)
          this.$message.error('加载数据表失败: 响应格式错误')
          this.tableTreeData = []
          return
        }

        console.log('📊 解析出的表列表:', tables)

        if (tables && tables.length > 0) {
          // 更新分页信息
          this.tablePagination.total = total
          this.tablePagination.pages = pages
          console.log(`📊 分页信息: 当前第${this.tablePagination.currentPage}页，共${pages}页，总计${total}条记录`)

          this.tableTreeData = []

          console.log(`开始处理 ${tables.length} 个表`)

          // 为每个表获取字段信息
          for (const table of tables) {
            try {
              console.log('处理表:', table)

              // 兼容多种表名字段 - 优先使用大写字段名（后端返回格式）
              const tableName = table.TABLE_NAME || table.tableName || table.name || table.table_name
              const tableId = table.TABLE_ID || table.tableId || table.id || table.table_id || tableName
              const dataSourceId = table.DATA_SOURCE_ID || table.dataSourceId || table.data_source_id

              if (!tableName) {
                console.warn('表名为空，跳过:', table)
                continue
              }

              console.log(`快速添加表: ${tableName}, ID: ${tableId}, 字段数: ${table.COLUMN_COUNT || table.columnCount || 0}`)

              // 懒加载模式：只创建表节点，字段信息在展开时加载
              const columnCount = table.COLUMN_COUNT || table.columnCount || 0
              const tableNode = {
                id: `table_${tableId}`,
                label: tableName,
                type: 'table',
                name: tableName,
                children: [], // 空数组，但设置leaf为false让树组件显示展开图标
                leaf: false,   // 明确告诉树组件这不是叶子节点，可以展开
                // 懒加载相关属性
                isLazyLoaded: false, // 标记是否已加载字段
                columnCount: columnCount,
                dataSourceId: this.sqlIndicatorData.dataSourceId,
                originalData: table // 保存原始数据，用于后续字段加载
              }

              this.tableTreeData.push(tableNode)
              console.log(`成功添加表: ${tableName}，字段数: ${tableNode.children.length}`)
            } catch (error) {
              console.warn(`处理表 ${tableName} 失败:`, error)
              // 即使处理失败，也添加表节点
              const fallbackTableNode = {
                id: `table_${tableId}`,
                label: tableName,
                type: 'table',
                name: tableName,
                children: [],
                columnCount: table.COLUMN_COUNT || table.columnCount || 0,
                dataSourceId: dataSourceId,
                error: true  // 标记为错误状态
              }
              this.tableTreeData.push(fallbackTableNode)
              console.log(`添加失败表节点: ${tableName}，列数: ${fallbackTableNode.columnCount}`)
            }
          }

          console.log('最终表数据:', this.tableTreeData)
          console.log('表数据加载完成，共加载表数量:', this.tableTreeData.length)

          if (this.tableTreeData.length > 0) {
            const pageInfo = `第${this.tablePagination.currentPage}页，共${this.tablePagination.pages}页`
            this.$message.success(`已加载 ${this.tableTreeData.length} 个数据表 (${pageInfo})`)
            // 强制刷新树组件
            this.$nextTick(() => {
              console.log('强制刷新树组件')
              this.$forceUpdate()
            })
          } else {
            this.$message.warning('该数据源下暂无可用数据表')
          }
        } else {
          console.warn('获取表列表失败或无数据:', tableListResponse)
          console.log('解析出的表数量:', tables.length)
          this.$message.warning('该数据源下暂无数据表')
          this.tableTreeData = []
        }

      } catch (error) {
        console.error('加载表数据失败:', error)
        this.$message.error('加载数据表失败: ' + (error.message || '网络错误'))
        this.tableTreeData = []
      } finally {
        this.tableLoading = false
      }
    },

    // 移除指标
    async removeIndicator(index) {
      const indicator = this.configuredIndicators[index]

      this.$confirm('确定要删除该指标吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          // 🔥 如果指标有configId，先调用后端删除接口
          if (indicator.configId) {
            console.log('调用后端删除接口:', indicator.configId)
            const response = await removeIndicatorFromCombination(indicator.configId)
            if (response.code !== 1) {
              throw new Error(response.msg || '删除失败')
            }
          }

          // 🔥 从前端数组中移除
          this.configuredIndicators.splice(index, 1)
          this.reorderIndicators()
          this.$message.success('指标删除成功')

        } catch (error) {
          console.error('删除指标失败:', error)
          this.$message.error('删除指标失败: ' + error.message)
        }
      }).catch(() => {
        // 用户取消删除
      })
    },

    // 上移
    moveUp(index) {
      if (index > 0) {
        const temp = this.configuredIndicators[index]
        this.$set(this.configuredIndicators, index, this.configuredIndicators[index - 1])
        this.$set(this.configuredIndicators, index - 1, temp)
        this.reorderIndicators()
      }
    },

    // 下移
    moveDown(index) {
      if (index < this.configuredIndicators.length - 1) {
        const temp = this.configuredIndicators[index]
        this.$set(this.configuredIndicators, index, this.configuredIndicators[index + 1])
        this.$set(this.configuredIndicators, index + 1, temp)
        this.reorderIndicators()
      }
    },

    // 重新排序
    reorderIndicators() {
      this.configuredIndicators.forEach((config, index) => {
        config.executionOrder = index + 1
      })
    },

    // 编辑指标SQL
    editIndicatorSQL(config, index) {
      console.log('编辑指标SQL:', config)

      // 🔥 修复：使用 $set 确保响应式更新，并正确处理 parameterMapping
      // 将指标数据加载到SQL编辑器
      this.sqlIndicatorData = {
        indicatorName: config.indicatorName,
        indicatorCode: config.indicatorCode,
        description: config.description || '',
        category: config.category || '',
        dataSourceId: config.dataSourceId || '',
        sqlContent: config.sqlContent || '',
        parameterMapping: config.parameterMapping ? { ...config.parameterMapping } : {}
      }

      console.log('加载的指标数据:', {
        category: this.sqlIndicatorData.category,
        dataSourceId: this.sqlIndicatorData.dataSourceId,
        parameterMapping: this.sqlIndicatorData.parameterMapping
      })

      // 记录编辑状态
      this.editingConfig = { ...config }
      this.editingIndex = index
      this.isEditingExistingIndicator = true

      // 打开SQL编辑器
      this.sqlEditorVisible = true

      // 🔥 修复：如果有数据源ID，加载对应的表结构数据
      this.$nextTick(() => {
        if (this.sqlIndicatorData.dataSourceId) {
          console.log('加载数据源表结构:', this.sqlIndicatorData.dataSourceId)
          this.loadTableData()
        }
      })

      this.$message.info(`正在编辑指标"${config.indicatorName}"的SQL`)
    },

    // 编辑指标配置
    editIndicatorConfig(config, index) {
      this.editingConfig = { ...config }
      this.editingIndex = index

      // 自动识别SQL中的参数
      this.autoDetectConfigParameters()

      this.detailConfigVisible = true
    },

    // 保存详细配置
    saveDetailConfig() {
      if (this.editingIndex >= 0) {
        this.$set(this.configuredIndicators, this.editingIndex, { ...this.editingConfig })
        this.detailConfigVisible = false
        this.editingConfig = null
        this.editingIndex = -1
      }
    },

    // 添加参数映射
    addParameterMapping() {
      if (!this.editingConfig.parameterMapping) {
        this.$set(this.editingConfig, 'parameterMapping', {})
      }
      this.$set(this.editingConfig.parameterMapping, '', '')
    },

    // 移除参数映射
    removeParameterMapping(key) {
      this.$delete(this.editingConfig.parameterMapping, key)
    },

    // SQL编辑器中的参数映射方法
    addSqlParameterMapping() {
      if (!this.sqlIndicatorData.parameterMapping) {
        this.$set(this.sqlIndicatorData, 'parameterMapping', {})
      }
      this.$set(this.sqlIndicatorData.parameterMapping, '', '')
    },

    // 移除SQL编辑器中的参数映射
    removeSqlParameterMapping(key) {
      this.$delete(this.sqlIndicatorData.parameterMapping, key)
    },

    // 自动检测SQL中的参数
    autoDetectSqlParameters() {
      if (!this.sqlIndicatorData.sqlContent) {
        this.$message.warning('请先输入SQL语句')
        return
      }

      // 使用正则表达式提取 ${参数名} 格式的参数
      const paramRegex = /\$\{([^}]+)\}/g
      const foundParams = new Set()
      let match

      while ((match = paramRegex.exec(this.sqlIndicatorData.sqlContent)) !== null) {
        foundParams.add(match[1])
      }

      if (foundParams.size === 0) {
        this.$message.info('SQL中没有找到参数占位符（${参数名}格式）')
        return
      }

      // 初始化参数映射对象
      if (!this.sqlIndicatorData.parameterMapping) {
        this.$set(this.sqlIndicatorData, 'parameterMapping', {})
      }

      // 检查已存在的参数
      const existingParams = new Set(Object.keys(this.sqlIndicatorData.parameterMapping))
      let addedCount = 0

      foundParams.forEach(paramName => {
        if (!existingParams.has(paramName)) {
          this.$set(this.sqlIndicatorData.parameterMapping, paramName, '')
          addedCount++
        }
      })

      if (addedCount > 0) {
        this.$message.success(`自动检测到 ${addedCount} 个新参数`)
      } else {
        this.$message.info('所有参数都已存在')
      }
    },

    // 自动检测配置中SQL的参数（用于配置对话框）
    autoDetectConfigParameters() {
      if (!this.editingConfig || !this.editingConfig.sqlContent) {
        return
      }

      // 使用正则表达式提取 ${参数名} 格式的参数
      const paramRegex = /\$\{([^}]+)\}/g
      const foundParams = new Set()
      let match

      while ((match = paramRegex.exec(this.editingConfig.sqlContent)) !== null) {
        foundParams.add(match[1])
      }

      if (foundParams.size === 0) {
        return
      }

      // 初始化参数映射对象
      if (!this.editingConfig.parameterMapping) {
        this.$set(this.editingConfig, 'parameterMapping', {})
      }

      // 检查已存在的参数
      const existingParams = new Set(Object.keys(this.editingConfig.parameterMapping))
      let addedCount = 0

      foundParams.forEach(paramName => {
        if (!existingParams.has(paramName)) {
          this.$set(this.editingConfig.parameterMapping, paramName, '')
          addedCount++
        }
      })

      // 静默添加，不显示消息提示（因为是自动触发）
      console.log(`自动检测到 ${foundParams.size} 个参数，新增 ${addedCount} 个参数`)
    },

    // 处理SQL参数替换
    processSqlParameters(sqlContent, parameterMapping) {
      if (!sqlContent || !parameterMapping) {
        return sqlContent
      }

      let processedSql = sqlContent
      const missingParams = []

      // 替换SQL中的参数占位符
      Object.keys(parameterMapping).forEach(paramName => {
        const paramValue = parameterMapping[paramName]
        if (paramValue !== null && paramValue !== undefined && paramValue !== '') {
          // 使用正则表达式替换 ${参数名} 格式的占位符
          const regex = new RegExp(`\\$\\{${paramName}\\}`, 'g')
          processedSql = processedSql.replace(regex, paramValue)
        } else {
          // 检查SQL中是否包含这个参数
          const regex = new RegExp(`\\$\\{${paramName}\\}`, 'g')
          if (regex.test(sqlContent)) {
            missingParams.push(paramName)
          }
        }
      })

      // 检查是否还有未替换的参数（跳过步骤引用参数）
      const remainingParams = []
      const paramRegex = /\$\{([^}]+)\}/g
      let match
      while ((match = paramRegex.exec(processedSql)) !== null) {
        const paramName = match[1]
        // 🔥 跳过步骤引用参数，这些参数由后端处理
        if (!paramName.startsWith('STEP_') &&
            !paramName.endsWith('_RESULT') &&
            paramName !== 'PREV_RESULT') {
          remainingParams.push(paramName)
        }
      }

      if (remainingParams.length > 0) {
        throw new Error(`SQL中包含未配置的参数: ${remainingParams.join(', ')}`)
      }

      if (missingParams.length > 0) {
        throw new Error(`以下参数值为空: ${missingParams.join(', ')}`)
      }

      return processedSql
    },



    // 保存所有配置（增强版本，支持智能重试和错误恢复）
    async handleSaveAll() {
      try {
        if (!this.combination || !this.combination.combinationId) {
          this.$message.error('请先保存组合基本信息')
          return
        }

        this.loading = true
        this.addExecutionLog('=== 开始保存指标配置 ===', 'info')

        // 构建保存数据
        const saveData = {
          combinationId: this.combination.combinationId,
          indicators: this.configuredIndicators.map(config => ({
            ...config,
            // 字段名映射修正
            indicatorId: config.indicatorId || null, // 确保有indicatorId字段
            parameterConfig: JSON.stringify(config.parameterMapping || {}), // parameterMapping -> parameterConfig
            filterConditions: config.filterCondition || '', // filterCondition -> filterConditions
            dependencyConfig: JSON.stringify(config.dependencyConfig || {}),
            // 字段长度限制（确保不超出数据库字段限制）
            description: (config.description || '').substring(0, 450), // 限制描述长度
            indicatorName: (config.indicatorName || '').substring(0, 150), // 限制指标名称长度（为执行结果表预留空间）
            indicatorCode: (config.indicatorCode || '').substring(0, 45) // 限制指标编码长度
          }))
        }

        this.addExecutionLog(`准备保存 ${saveData.indicators.length} 个指标`, 'info')

        // 执行智能保存策略
        await this.executeSmartSaveStrategy(saveData)

        this.addExecutionLog('=== 配置保存成功 ===', 'success')
        this.$message.success('配置保存成功')
        this.$emit('saved')
        this.dialogVisible = false
      } catch (error) {
        console.error('保存配置失败:', error)

        // 特殊处理外键约束错误
        if (error.message && error.message.includes('FK_RESULT_CONFIG')) {
          this.addExecutionLog('保存失败: 检测到外键约束问题，建议先清理执行结果数据', 'error')
          this.$message.error('保存失败: 存在执行结果数据引用，请先清理相关数据后重试')

          // 提供解决方案提示
          this.$confirm('检测到外键约束问题，是否尝试自动清理执行结果数据？', '外键约束错误', {
            confirmButtonText: '自动清理',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(async () => {
            try {
              await this.clearCombinationExecutionResults(this.combination.combinationId)
              this.$message.success('执行结果数据清理成功，请重新保存配置')
            } catch (clearError) {
              this.$message.error('清理失败: ' + clearError.message)
            }
          }).catch(() => {
            this.$message.info('已取消清理操作')
          })
        } else if (error.message && error.message.includes('UK_COMBINATION_INDICATOR')) {
          this.addExecutionLog('保存失败: 检测到唯一性约束冲突，指标可能已存在', 'error')
          this.$message.error('保存失败: 指标已存在，请检查指标编码是否重复')
        } else {
          this.addExecutionLog(`保存失败: ${error.message}`, 'error')
          this.$message.error('保存配置失败: ' + (error.message || '未知错误'))
        }
      } finally {
        this.loading = false
      }
    },

    // 智能保存策略（支持多种错误恢复机制）
    async executeSmartSaveStrategy(saveData, retryCount = 0) {
      const maxRetries = 3

      try {
        // 策略1：标准保存流程
        await this.standardSaveProcess(saveData)

      } catch (error) {
        this.addExecutionLog(`保存失败 (尝试 ${retryCount + 1}/${maxRetries}): ${error.message}`, 'error')

        if (retryCount < maxRetries) {
          // 根据错误类型选择恢复策略
          if (error.message && error.message.includes('FK_RESULT_CONFIG')) {
            this.addExecutionLog('检测到外键约束错误，执行清理策略...', 'warning')
            await this.executeCleanupStrategy(saveData)

          } else if (error.message && error.message.includes('UK_COMBINATION_INDICATOR')) {
            this.addExecutionLog('检测到唯一性约束错误，执行重置策略...', 'warning')
            await this.executeResetStrategy(saveData)

          } else {
            this.addExecutionLog('执行通用重试策略...', 'warning')
            await this.executeGeneralRetryStrategy(saveData)
          }

          // 递归重试
          await this.executeSmartSaveStrategy(saveData, retryCount + 1)
        } else {
          throw new Error(`保存失败，已重试 ${maxRetries} 次: ${error.message}`)
        }
      }
    },

    // 标准保存流程
    async standardSaveProcess(saveData) {
      // 第一步：清理执行结果数据
      try {
        this.addExecutionLog('正在清理组合的执行结果数据...', 'info')
        const clearResult = await this.clearCombinationExecutionResults(this.combination.combinationId)
        this.addExecutionLog(`执行结果数据清理成功: ${clearResult.msg}`, 'success')
      } catch (error) {
        console.warn('清理执行结果失败:', error)
        this.addExecutionLog(`清理执行结果失败: ${error.message}，继续尝试删除指标`, 'warning')
      }

      // 第二步：智能删除可能存在的同名指标
      await this.smartCleanupExistingIndicators(saveData.indicators)

      // 第三步：添加所有新指标
      await this.addAllIndicators(saveData)
    },

    // 智能清理现有指标
    async smartCleanupExistingIndicators(indicators) {
      this.addExecutionLog('正在清理可能存在的同名指标...', 'info')

      for (const indicator of indicators) {
        let indicatorDeleted = false // 标记当前指标是否已删除

        try {
          const possibleConfigIds = [
            indicator.configId, // 当前的configId
            `${this.combination.combinationId}_${indicator.indicatorCode}`, // 基于编码的ID
            `config_${indicator.indicatorCode}`, // 简单格式的ID
          ]

          for (const configId of possibleConfigIds) {
            if (indicatorDeleted) break // 如果已删除，跳出内层循环

            try {
              const deleteResult = await this.smartDeleteIndicator(configId)
              if (deleteResult.success) {
                this.addExecutionLog(`删除指标成功: ${configId}`, 'success')
                indicatorDeleted = true // 标记为已删除
              }
            } catch (deleteError) {
              // 删除失败是正常的，可能指标不存在
              console.debug(`尝试删除指标失败: ${configId}`, deleteError)
            }
          }
        } catch (error) {
          // 删除失败不阻止后续操作
          console.debug(`清理指标失败: ${indicator.indicatorName}`, error)
        }
      }
    },

    // 添加所有指标
    async addAllIndicators(saveData) {
      this.addExecutionLog('开始添加新指标...', 'info')

      for (let i = 0; i < saveData.indicators.length; i++) {
        const indicator = saveData.indicators[i]

        // 重新生成configId，确保唯一性
        indicator.configId = this.generateUniqueConfigId(this.combination.combinationId, i)

        try {
          await addIndicatorToCombination({
            combinationId: this.combination.combinationId,
            ...indicator
          })
          this.addExecutionLog(`指标 ${i + 1}/${saveData.indicators.length} 添加成功: ${indicator.indicatorName}`, 'success')
        } catch (error) {
          console.error(`添加指标失败: ${indicator.indicatorName}`, error)
          this.addExecutionLog(`指标添加失败: ${indicator.indicatorName} - ${error.message}`, 'error')
          throw error // 添加失败时停止后续操作
        }
      }
    },

    // 清理策略（针对外键约束错误）
    async executeCleanupStrategy(saveData) {
      this.addExecutionLog('执行深度清理策略...', 'info')

      try {
        // 强制清理所有执行结果
        await this.$http.post('/api/model/combination/execution-results/clear-all')
        this.addExecutionLog('所有执行结果数据已清理', 'success')

        // 等待一秒让数据库操作完成
        await new Promise(resolve => setTimeout(resolve, 1000))

      } catch (error) {
        this.addExecutionLog(`深度清理失败: ${error.message}`, 'error')
        throw error
      }
    },

    // 重置策略（针对唯一性约束错误）
    async executeResetStrategy(saveData) {
      this.addExecutionLog('执行重置策略...', 'info')

      try {
        // 删除当前组合的所有指标
        for (const indicator of saveData.indicators) {
          try {
            await removeIndicatorFromCombination(indicator.configId)
          } catch (error) {
            // 删除失败忽略
          }
        }

        // 重新生成所有configId
        saveData.indicators.forEach((indicator, index) => {
          indicator.configId = this.generateUniqueConfigId(this.combination.combinationId, index, true)
        })

        this.addExecutionLog('重置策略执行完成', 'success')

      } catch (error) {
        this.addExecutionLog(`重置策略失败: ${error.message}`, 'error')
        throw error
      }
    },

    // 通用重试策略
    async executeGeneralRetryStrategy(saveData) {
      this.addExecutionLog('执行通用重试策略...', 'info')

      // 等待2秒后重试
      await new Promise(resolve => setTimeout(resolve, 2000))

      // 重新生成所有ID
      saveData.indicators.forEach((indicator, index) => {
        indicator.configId = this.generateUniqueConfigId(this.combination.combinationId, index, true)
      })

      this.addExecutionLog('通用重试策略准备完成', 'success')
    },

    // 生成ID
    generateId() {
      return 'config_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
    },

    // 生成唯一的ConfigId（用于保存到数据库）
    generateUniqueConfigId(combinationId, index, isRetry = false) {
      const timestamp = Date.now()
      const random = Math.random().toString(36).substr(2, 6) // 缩短随机数
      const retryFlag = isRetry ? 'R' : '' // 缩短重试标记

      // 优化ID格式，确保唯一性但更短
      // 格式: COMB前缀_索引_时间戳后8位_随机数_重试标记
      const shortTimestamp = timestamp.toString().slice(-8) // 只取时间戳后8位
      const shortCombId = combinationId.replace('COMB', 'C') // 缩短组合ID前缀

      const configId = `${shortCombId}_${index}_${shortTimestamp}_${random}${retryFlag}`

      // 确保长度不超过50字符（为数据库字段预留空间）
      return configId.substring(0, 50)
    },



    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
/* 全局样式，确保指标配置对话框在正确层级 */
/* 🔒 移除全局::v-deep样式，改为依赖dialog-z-index.css的作用域限制 */
/* 这些样式现在由dialog-z-index.css中的.risk-mxgl-sjmxgl-page作用域管理 */

/* 局部样式，直接应用到组件内容 */
.config-container {
  display: flex;
  height: 75vh;
  gap: 20px;
}

.available-indicators,
.configured-indicators {
  flex: 1;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
}

.panel-header {
  padding: 16px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  align-items: center;
  justify-content: space-between;

  h3 {
    margin: 0;
    font-size: 16px;
    font-weight: 600;
  }

  .search-box {
    width: 200px;
  }
}

.indicator-tabs {
  flex: 1;
  overflow: hidden;

  .el-tabs {
    height: 100%;
    display: flex;
    flex-direction: column;

    .el-tab-content {
      flex: 1;
      overflow: hidden; /* 改为hidden，让内部的indicator-list处理滚动 */
      min-height: 0; /* 确保flex子元素可以收缩 */
    }

    /* 确保标签页面板有正确的高度 */
    .el-tab-pane {
      height: 100%;
      overflow: hidden;
    }
  }
}

.indicator-list {
  padding: 16px;
  height: calc(100% - 32px); /* 减去padding */
  overflow-y: auto;
  max-height: calc(75vh - 180px); /* 减去对话框头部、面板头部、标签栏等高度 */

  /* 美化滚动条样式 */
  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
  }

  &::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;

    &:hover {
      background: #a8a8a8;
    }
  }

  .indicator-item {
    padding: 12px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    margin-bottom: 8px;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      border-color: #409eff;
      box-shadow: 0 2px 4px rgba(64, 158, 255, 0.1);
    }

    .indicator-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 8px;

      .indicator-name {
        font-weight: 600;
        color: #303133;
      }
    }

    .indicator-meta {
      display: flex;
      gap: 12px;
      font-size: 12px;
      color: #909399;
      margin-bottom: 8px;
    }

    .indicator-description {
      font-size: 12px;
      color: #606266;
      line-height: 1.4;
    }
  }
}

.new-indicator-form {
  padding: 20px;
  max-height: calc(75vh - 120px);
  overflow-y: auto;

  .creation-mode-selector {
    margin-bottom: 30px;

    h4 {
      margin: 0 0 16px 0;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }

    .mode-options {
      display: flex;
      gap: 20px;

      .mode-option {
        flex: 1;
        padding: 20px;
        border: 2px solid #e4e7ed;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s;
        background: #fff;

        &:hover {
          border-color: #409eff;
          box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
          transform: translateY(-2px);
        }

        .mode-icon {
          text-align: center;
          margin-bottom: 12px;

          i {
            font-size: 32px;
            color: #409eff;
          }
        }

        .mode-content {
          text-align: center;
          margin-bottom: 16px;

          h5 {
            margin: 0 0 8px 0;
            font-size: 16px;
            font-weight: 600;
            color: #303133;
          }

          p {
            margin: 0;
            font-size: 13px;
            color: #606266;
            line-height: 1.5;
          }
        }

        .mode-action {
          text-align: center;
        }
      }
    }
  }

  .form-mode-content {
    max-height: calc(75vh - 200px);
    overflow-y: auto;
    padding: 16px;

    .el-form {
      max-width: 600px;
      margin: 0 auto;
    }
  }
}

.drop-zone {
  flex: 1;
  overflow-y: auto;

  .empty-state {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: #909399;

    i {
      font-size: 48px;
      margin-bottom: 16px;
    }
  }
}

.configured-list {
  padding: 16px;

  .configured-item {
    border: 1px solid #ebeef5;
    border-radius: 4px;
    margin-bottom: 12px;
    overflow: hidden;

    .item-header {
      padding: 12px;
      background-color: #f5f7fa;
      display: flex;
      align-items: center;
      gap: 12px;

      .order-control {
        display: flex;
        align-items: center;
        gap: 8px;

        .order-number {
          width: 24px;
          height: 24px;
          border-radius: 50%;
          background-color: #409eff;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 12px;
        font-weight: 600;
      }

      .order-buttons {
        display: flex;
        flex-direction: column;
      }
    }

    .item-info {
      flex: 1;

      h4 {
        margin: 0 0 4px 0;
        font-size: 14px;
        font-weight: 600;
        color: #303133;
      }

      .indicator-code {
        font-size: 12px;
        color: #909399;
      }
    }

    .item-actions {
      display: flex;
      align-items: center;
      gap: 8px;
    }
  }

  .item-content {
    padding: 12px;

    .config-summary {
      display: flex;
      gap: 12px;
      margin-bottom: 8px;
      font-size: 12px;
      color: #606266;

      span {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }

    .sql-preview {
      .sql-content {
        background-color: #f5f7fa;
        padding: 8px;
        border-radius: 4px;
        font-size: 12px;
        line-height: 1.4;
        max-height: 200px;
        overflow-y: auto;
      }
    }
  }
}

.parameter-mapping {
  .mapping-item {
    display: flex;
    align-items: center;
    margin-bottom: 8px;
  }
}
}

/* 额外的全局样式确保对话框显示正确 */
::v-deep .el-dialog__wrapper.indicator-config-dialog {
  z-index: 2500 !important;
}

::v-deep .el-overlay {
  z-index: 2499 !important;
}

/* 确保Element UI的遮罩层不会覆盖对话框 */
::v-deep .v-modal {
  z-index: 2499 !important;
}

/* SQL编辑器对话框样式 */
::v-deep .sql-editor-dialog {
  margin-top: 5vh !important;
}

.sql-editor-main-container {
  display: flex;
  flex-direction: column;
  height: 75vh;
  overflow: hidden;
}

.indicator-info-panel {
  padding: 16px;
  background: #f8f9fa;
  border-radius: 4px;
  margin-bottom: 16px;
  flex-shrink: 0;
  max-height: 180px;
  overflow-y: auto;
}

.sql-editor-panel {
  flex: 1;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  overflow: hidden;
  min-height: 0;
}

/* SQL编辑器样式 */
.sql-editor-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.editor-toolbar {
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.editor-main {
  flex: 1;
  overflow: hidden;
  min-height: 0;
}

.visual-builder,
.sql-editor,
.result-panel {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #e4e7ed;
}

.result-panel {
  border-right: none;
}

.builder-header,
.editor-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.builder-header h4,
.editor-header h4 {
  margin: 0;
  font-size: 14px;
  color: #333;
}

.builder-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  max-height: calc(100vh - 300px);
}

.table-selector,
.sql-builder,
.function-library {
  margin-bottom: 24px;
}

.table-selector h5,
.sql-builder h5,
.function-library h5 {
  margin: 0 0 12px 0;
  font-size: 13px;
  color: #666;
  font-weight: 500;
}

/* 表名搜索框样式 */
.table-search {
  margin-bottom: 12px;
}

.table-search .el-input {
  width: 100%;
}

.table-search .el-input__inner {
  border-radius: 4px;
  font-size: 12px;
  height: 32px;
  line-height: 32px;
}

.table-loading,
.table-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  padding: 40px 20px;
  color: #909399;
  font-size: 14px;
  text-align: center;

  i {
    font-size: 24px;
    margin-bottom: 8px;
  }

  .el-icon-loading {
    font-size: 24px;
    margin-bottom: 8px;
    animation: rotating 2s linear infinite;
  }
}

.table-loading {
  color: #409eff;
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.custom-tree-node {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.node-label {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  padding: 2px 4px;
  border-radius: 3px;
  transition: all 0.2s;
}

.node-label[draggable="true"]:hover {
  background: #f0f9ff;
  border: 1px dashed #409eff;
}

.node-label[draggable="true"]:active {
  background: #e6f7ff;
  transform: scale(0.95);
}

.quick-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
  margin-bottom: 16px;
}

.sql-components h6 {
  margin: 0 0 8px 0;
  font-size: 12px;
  color: #999;
}

.component-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 6px;
}

.component-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 8px;
  border: 1px solid #e4e7ed;
  border-radius: 3px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.2s;
}

.component-item:hover {
  border-color: #409eff;
  background: #f0f9ff;
}

.component-item[draggable="true"]:hover {
  cursor: grab;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.component-item[draggable="true"]:active {
  cursor: grabbing;
  transform: scale(0.95);
}

.function-item {
  padding: 8px 12px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.function-item:hover {
  background: #f0f9ff;
}

.function-item[draggable="true"]:hover {
  cursor: grab;
  background: #f0f9ff;
  border-left: 3px solid #409eff;
  padding-left: 9px;
}

.function-item[draggable="true"]:active {
  cursor: grabbing;
  background: #e6f7ff;
}

.function-name {
  font-weight: 500;
  color: #409eff;
  margin-right: 8px;
}

.function-desc {
  font-size: 12px;
  color: #666;
}

.code-assistant {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  max-height: calc(100vh - 300px);
}

.hint-item {
  margin-bottom: 16px;
  padding: 12px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.hint-item strong {
  color: #409eff;
}

.hint-item p {
  margin: 8px 0;
  font-size: 12px;
  color: #666;
}

.hint-item code {
  display: block;
  padding: 8px;
  background: #f8f9fa;
  border-radius: 3px;
  font-size: 11px;
  color: #333;
}

.template-item {
  padding: 12px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.template-item:hover {
  border-color: #409eff;
  background: #f0f9ff;
}

.template-item h6 {
  margin: 0 0 4px 0;
  color: #333;
}

.template-item p {
  margin: 0;
  font-size: 12px;
  color: #666;
}

.code-editor {
  flex: 1;
  position: relative;
  transition: all 0.3s ease;
}

.code-editor.drag-over {
  border: 2px dashed #409eff;
  background: rgba(64, 158, 255, 0.05);
}

.drag-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(64, 158, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  pointer-events: none;
  z-index: 10;
}

.drag-hint {
  background: rgba(64, 158, 255, 0.9);
  color: white;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.drag-hint i {
  font-size: 32px;
  margin-bottom: 8px;
  display: block;
}

.drag-hint p {
  margin: 0;
  font-size: 14px;
  font-weight: 500;
}

.sql-textarea {
  width: 100%;
  height: calc(100% - 60px);
  border: none;
  outline: none;
  padding: 16px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
  resize: none;
  background: #fafafa;
}

.sql-info {
  padding: 8px 16px;
  border-top: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.info-item {
  display: flex;
  align-items: center;
  font-size: 12px;
}

.info-label {
  color: #666;
  margin-right: 4px;
}

.info-value {
  color: #333;
  font-weight: 500;
}

.status-empty {
  color: #999;
}

.status-edited {
  color: #67c23a;
}

.result-content {
  flex: 1;
  overflow-y: auto;
  max-height: calc(100vh - 350px);
}

.loading-container {
  height: 100%;
}

.result-table {
  height: 100%;
  overflow: auto;
}

.result-summary {
  padding: 8px 16px;
  border-top: 1px solid #e4e7ed;
  background: #f8f9fa;
  font-size: 12px;
  color: #666;
  text-align: center;
}

.empty-result {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #999;
}

.empty-result i {
  font-size: 48px;
  margin-bottom: 12px;
  opacity: 0.5;
}

.log-content {
  height: 250px;
  overflow-y: auto;
  padding: 8px;
  background: #fafafa;
  border-radius: 4px;
}

.log-item {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin-bottom: 8px;
  font-size: 12px;
}

.log-time {
  color: #999;
}

/* 分页组件样式 */
.table-pagination {
  padding: 10px 0;
  text-align: center;
  border-top: 1px solid #e4e7ed;
  background: #fafafa;
}

.table-pagination .el-pagination {
  text-align: center;
}

.table-pagination .el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: #409eff;
  color: #fff;
}

.log-message {
  flex: 1;
}

.log-message.info {
  color: #409eff;
}

.log-message.success {
  color: #67c23a;
}

/* 树节点样式 */
.custom-tree-node {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.node-label {
  display: flex;
  align-items: center;
  cursor: pointer;
  flex: 1;
}

.node-label.table-node {
  font-weight: 500;
  color: #303133;
}

.node-label.column-node {
  color: #606266;
}

.node-label.loading-node {
  color: #909399;
  font-style: italic;
  cursor: default;
}

.node-label.loading-node:hover {
  color: #409eff;
}

.node-label i {
  margin-right: 5px;
}

.log-message.error {
  color: #f56c6c;
}

/* 🎯 局部z-index管理 - 只影响当前组件 */
.indicator-config-dialog {
  /* 确保指标配置对话框在合适的层级 */
  z-index: 2600 !important;
}

.indicator-config-dialog .el-dialog {
  z-index: 2601 !important;
}

/* SQL编辑器对话框 - 在指标配置对话框之上 */
.indicator-config-dialog .sql-editor-dialog {
  z-index: 2700 !important;
}

.indicator-config-dialog .sql-editor-dialog .el-dialog {
  z-index: 2701 !important;
}

/* 详细配置对话框 - 在指标配置对话框之上 */
.indicator-config-dialog .el-dialog[aria-label*="指标详细配置"] {
  z-index: 2650 !important;
}

/* 确保下拉菜单等组件在对话框之上 */
.indicator-config-dialog .el-select-dropdown,
.indicator-config-dialog .el-picker-panel,
.indicator-config-dialog .el-cascader-menus {
  z-index: 2800 !important;
}

/* 工具提示 */
.indicator-config-dialog .el-tooltip__popper {
  z-index: 2850 !important;
}

/* 消息提示 */
.indicator-config-dialog .el-message {
  z-index: 2900 !important;
}

.indicator-config-dialog .el-message-box__wrapper {
  z-index: 2900 !important;
}

</style>
