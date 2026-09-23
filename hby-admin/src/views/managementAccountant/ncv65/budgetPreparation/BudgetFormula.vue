<template>
  <div class="budget-formula">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算公式管理</h2>
      <p>管理预算计算公式，支持公式编辑器、函数库、变量定义和公式验证</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateFormula">创建公式</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportFormula">导入公式</el-button>
            <el-button type="warning" icon="el-icon-check" @click="handleBatchValidate">批量验证</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportFormula">导出公式</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button icon="el-icon-setting" @click="handleSettings">设置</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 公式统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ formulaStats.totalFormulas }}</div>
            <div class="stat-label">公式总数</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-operation"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ formulaStats.activeFormulas }}</div>
            <div class="stat-label">启用公式</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="formulaStats.activeRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#67C23A"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-check"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card complex-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ formulaStats.complexFormulas }}</div>
            <div class="stat-label">复杂公式</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="formulaStats.complexRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#E6A23C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-cpu"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card error-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ formulaStats.errorFormulas }}</div>
            <div class="stat-label">异常公式</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="formulaStats.errorRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#F56C6C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 公式分类 -->
    <el-card class="category-card" shadow="never">
      <div class="category-header">
        <h3>公式分类</h3>
        <el-button type="text" @click="handleCategoryManagement">分类管理</el-button>
      </div>
      
      <el-row :gutter="16">
        <el-col :span="4">
          <div class="category-tree">
            <el-tree
              :data="categoryTree"
              :props="{ children: 'children', label: 'label' }"
              node-key="id"
              :current-node-key="currentCategoryId"
              @node-click="handleCategoryClick"
              :expand-on-click-node="false"
            >
              <span class="tree-node" slot-scope="{ node, data }">
                <span class="node-label">{{ node.label }}</span>
                <span class="node-count">({{ data.formulaCount || 0 }})</span>
              </span>
            </el-tree>
          </div>
        </el-col>
        <el-col :span="20">
          <!-- 查询条件 -->
          <div class="search-form">
            <el-form :model="queryForm" :inline="true" size="small">
              <el-form-item label="公式名称">
                <el-input
                  v-model="queryForm.formulaName"
                  placeholder="请输入公式名称"
                  clearable
                  style="width: 200px"
                />
              </el-form-item>
              <el-form-item label="公式类型">
                <el-select
                  v-model="queryForm.formulaType"
                  placeholder="请选择公式类型"
                  clearable
                  style="width: 150px"
                >
                  <el-option
                    v-for="item in formulaTypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="公式状态">
                <el-select
                  v-model="queryForm.formulaStatus"
                  placeholder="请选择公式状态"
                  clearable
                  style="width: 150px"
                >
                  <el-option
                    v-for="item in formulaStatusOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
                <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 公式列表 -->
          <div class="formula-list">
            <el-table
              v-loading="loading"
              :data="formulaList"
              border
              stripe
              highlight-current-row
              @selection-change="handleSelectionChange"
              @sort-change="handleSortChange"
              @row-click="handleRowClick"
            >
              <el-table-column type="selection" width="50" align="center" />
              <el-table-column type="index" label="序号" width="60" align="center" />
              
              <el-table-column prop="formulaCode" label="公式编号" width="120" show-overflow-tooltip />
              <el-table-column prop="formulaName" label="公式名称" width="180" show-overflow-tooltip />
              
              <el-table-column prop="formulaType" label="公式类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag size="mini" :type="getFormulaTypeColor(scope.row.formulaType)">
                    {{ getFormulaTypeText(scope.row.formulaType) }}
                  </el-tag>
                </template>
              </el-table-column>
              
              <el-table-column prop="formulaExpression" label="公式表达式" min-width="200" show-overflow-tooltip>
                <template slot-scope="scope">
                  <code class="formula-code">{{ scope.row.formulaExpression }}</code>
                </template>
              </el-table-column>
              
              <el-table-column prop="complexity" label="复杂度" width="100" align="center">
                <template slot-scope="scope">
                  <el-rate
                    v-model="scope.row.complexity"
                    :max="5"
                    disabled
                    show-score
                    text-color="#ff9900"
                    score-template="{value}"
                  />
                </template>
              </el-table-column>
              
              <el-table-column prop="validationStatus" label="验证状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getValidationStatusType(scope.row.validationStatus)" size="mini">
                    {{ getValidationStatusText(scope.row.validationStatus) }}
                  </el-tag>
                </template>
              </el-table-column>
              
              <el-table-column prop="formulaStatus" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getFormulaStatusType(scope.row.formulaStatus)" size="mini">
                    {{ getFormulaStatusText(scope.row.formulaStatus) }}
                  </el-tag>
                </template>
              </el-table-column>
              
              <el-table-column prop="createBy" label="创建人" width="100" show-overflow-tooltip />
              <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
              
              <el-table-column label="操作" width="220" align="center" fixed="right">
                <template slot-scope="scope">
                  <div @click.stop>
                    <el-button
                      type="text"
                      size="mini"
                      icon="el-icon-view"
                      @click="handleView(scope.row)"
                    >查看</el-button>
                    <el-button
                      type="text"
                      size="mini"
                      icon="el-icon-edit"
                      @click="handleEdit(scope.row)"
                    >编辑</el-button>
                    <el-button
                      type="text"
                      size="mini"
                      icon="el-icon-cpu"
                      class="primary-text"
                      @click="handleTest(scope.row)"
                    >测试</el-button>
                    <el-dropdown
                      trigger="click"
                      @command="(command) => handleCommand(command, scope.row)"
                    >
                      <el-button type="text" size="mini">
                        更多<i class="el-icon-arrow-down el-icon--right"></i>
                      </el-button>
                      <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                        <el-dropdown-item command="validate" icon="el-icon-check">验证</el-dropdown-item>
                        <el-dropdown-item command="history" icon="el-icon-time">历史版本</el-dropdown-item>
                        <el-dropdown-item command="usage" icon="el-icon-s-data">使用情况</el-dropdown-item>
                        <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
                        <el-dropdown-item command="delete" icon="el-icon-delete" divided style="color: #F56C6C;">删除</el-dropdown-item>
                      </el-dropdown-menu>
                    </el-dropdown>
                  </div>
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
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 新增/编辑公式对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1200px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="formulaForm"
        :model="formulaForm"
        :rules="formulaRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="公式名称" prop="formulaName">
              <el-input
                v-model="formulaForm.formulaName"
                placeholder="请输入公式名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公式类型" prop="formulaType">
              <el-select
                v-model="formulaForm.formulaType"
                placeholder="请选择公式类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in formulaTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属分类" prop="categoryId">
              <el-cascader
                v-model="formulaForm.categoryId"
                :options="categoryOptions"
                :props="{ checkStrictly: true, value: 'id', label: 'label' }"
                placeholder="请选择所属分类"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-input-number
                v-model="formulaForm.priority"
                :min="1"
                :max="100"
                placeholder="请输入优先级"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="公式描述" prop="formulaDescription">
          <el-input
            v-model="formulaForm.formulaDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入公式描述"
          />
        </el-form-item>
        
        <!-- 公式编辑器 -->
        <el-form-item label="公式表达式" prop="formulaExpression">
          <div class="formula-editor">
            <div class="editor-toolbar">
              <el-button-group size="mini">
                <el-button @click="insertFunction('SUM')">SUM</el-button>
                <el-button @click="insertFunction('AVG')">AVG</el-button>
                <el-button @click="insertFunction('MAX')">MAX</el-button>
                <el-button @click="insertFunction('MIN')">MIN</el-button>
                <el-button @click="insertFunction('COUNT')">COUNT</el-button>
                <el-button @click="insertFunction('IF')">IF</el-button>
              </el-button-group>
              <el-button-group size="mini" style="margin-left: 10px;">
                <el-button @click="insertOperator('+')">+</el-button>
                <el-button @click="insertOperator('-')">-</el-button>
                <el-button @click="insertOperator('*')">*</el-button>
                <el-button @click="insertOperator('/')">/</el-button>
                <el-button @click="insertOperator('(')">(</el-button>
                <el-button @click="insertOperator(')')">)</el-button>
              </el-button-group>
              <el-button size="mini" type="primary" @click="handleValidateFormula" style="margin-left: 10px;">
                验证公式
              </el-button>
            </div>
            
            <el-input
              ref="formulaEditor"
              v-model="formulaForm.formulaExpression"
              type="textarea"
              :rows="6"
              placeholder="请输入公式表达式，例如：SUM(A1:A10) * 0.1 + B1"
              class="formula-textarea"
            />
            
            <div class="editor-sidebar">
              <el-tabs v-model="activeTab" type="card" size="small">
                <el-tab-pane label="变量" name="variables">
                  <div class="variable-list">
                    <div
                      v-for="variable in availableVariables"
                      :key="variable.code"
                      class="variable-item"
                      @click="insertVariable(variable)"
                    >
                      <div class="variable-code">{{ variable.code }}</div>
                      <div class="variable-name">{{ variable.name }}</div>
                    </div>
                  </div>
                </el-tab-pane>
                
                <el-tab-pane label="函数" name="functions">
                  <div class="function-list">
                    <div
                      v-for="func in availableFunctions"
                      :key="func.name"
                      class="function-item"
                      @click="insertFunction(func.name)"
                    >
                      <div class="function-name">{{ func.name }}</div>
                      <div class="function-desc">{{ func.description }}</div>
                    </div>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>
        </el-form-item>
        
        <!-- 公式参数 -->
        <el-form-item label="公式参数" prop="formulaParameters">
          <div class="formula-parameters">
            <div class="parameters-header">
              <el-button type="primary" size="mini" @click="handleAddParameter">添加参数</el-button>
              <el-button type="success" size="mini" @click="handleAutoDetectParameters">自动检测</el-button>
            </div>
            
            <el-table
              :data="formulaForm.formulaParameters"
              border
              size="mini"
              max-height="200"
            >
              <el-table-column type="index" label="序号" width="60" align="center" />
              
              <el-table-column label="参数名称" width="120">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.parameterName"
                    placeholder="参数名称"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="参数类型" width="120">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.parameterType"
                    placeholder="参数类型"
                    size="mini"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="type in parameterTypeOptions"
                      :key="type.value"
                      :label="type.label"
                      :value="type.value"
                    />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="默认值" width="120">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.defaultValue"
                    placeholder="默认值"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="是否必填" width="80" align="center">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.required"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="参数描述" min-width="150">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.parameterDescription"
                    placeholder="参数描述"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    class="danger-text"
                    @click="handleRemoveParameter(scope.$index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form-item>
        
        <el-form-item label="公式设置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="formulaForm.isPublic">公开公式</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="formulaForm.allowCopy">允许复制</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="formulaForm.autoValidate">自动验证</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleSaveFormula">保存公式</el-button>
        <el-button type="primary" @click="handlePublishFormula">发布公式</el-button>
      </div>
    </el-dialog>

    <!-- 公式测试对话框 -->
    <el-dialog
      title="公式测试"
      :visible.sync="testDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div class="formula-test">
        <div class="test-formula">
          <h4>测试公式</h4>
          <code class="formula-display">{{ testFormula.formulaExpression }}</code>
        </div>
        
        <div class="test-parameters">
          <h4>参数设置</h4>
          <el-form label-width="120px" size="small">
            <el-form-item
              v-for="param in testFormula.formulaParameters"
              :key="param.parameterName"
              :label="param.parameterName"
            >
              <el-input
                v-model="testParameters[param.parameterName]"
                :placeholder="param.defaultValue"
                style="width: 200px"
              />
              <span class="parameter-desc">{{ param.parameterDescription }}</span>
            </el-form-item>
          </el-form>
        </div>
        
        <div class="test-result">
          <h4>计算结果</h4>
          <div class="result-display">
            <el-input
              v-model="testResult"
              readonly
              placeholder="点击计算按钮查看结果"
              style="width: 100%"
            />
          </div>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleCalculateFormula">计算</el-button>
      </div>
    </el-dialog>

    <!-- 公式详情对话框 -->
    <el-dialog title="公式详情" :visible.sync="detailDialogVisible" width="700px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="公式名称">{{ formulaDetail.formulaName }}</el-descriptions-item>
        <el-descriptions-item label="公式类型">{{ getFormulaTypeText(formulaDetail.formulaType) }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ formulaDetail.formulaStatus === 'PUBLISHED' ? '已发布' : '草稿' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ formulaDetail.createBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="公式表达式" :span="2">{{ formulaDetail.formulaExpression }}</el-descriptions-item>
        <el-descriptions-item label="说明" :span="2">{{ formulaDetail.formulaDescription || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 公式设置对话框 -->
    <el-dialog title="公式设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="140px" size="small">
        <el-form-item label="启用公式验证">
          <el-switch v-model="settingsForm.enableValidation" />
        </el-form-item>
        <el-form-item label="允许自定义函数">
          <el-switch v-model="settingsForm.allowCustomFunctions" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 分类管理对话框 -->
    <el-dialog title="公式分类管理" :visible.sync="categoryDialogVisible" width="600px" :close-on-click-modal="false">
      <el-table :data="formulaTypeOptions" border size="small">
        <el-table-column prop="value" label="分类编码" width="150" />
        <el-table-column prop="label" label="分类名称" />
      </el-table>
      <div slot="footer">
        <el-button @click="categoryDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 历史版本对话框 -->
    <el-dialog :title="'历史版本 - ' + (historyRow.formulaName || '')" :visible.sync="historyDialogVisible" width="750px" :close-on-click-modal="false">
      <el-table :data="historyList" border size="small" v-loading="historyLoading" empty-text="暂无历史记录">
        <el-table-column prop="operateTime" label="时间" width="170" />
        <el-table-column prop="version" label="版本" width="80" />
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="remark" label="说明" />
      </el-table>
      <div slot="footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 使用情况对话框 -->
    <el-dialog :title="'使用情况 - ' + (usageRow.formulaName || '')" :visible.sync="usageDialogVisible" width="700px" :close-on-click-modal="false">
      <el-table :data="usageList" border size="small" v-loading="usageLoading" empty-text="暂无使用记录">
        <el-table-column prop="budgetName" label="预算名称" />
        <el-table-column prop="sceneName" label="使用场景" width="120" />
        <el-table-column prop="lastUsedTime" label="最近使用" width="170" />
      </el-table>
      <div slot="footer">
        <el-button @click="usageDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 导入公式对话框 -->
    <el-dialog title="导入预算公式" :visible.sync="importDialogVisible" width="650px" :close-on-click-modal="false">
      <el-steps :active="importStep" simple style="margin-bottom: 20px;">
        <el-step title="填写数据" icon="el-icon-edit" />
        <el-step title="导入结果" icon="el-icon-check" />
      </el-steps>

      <div v-if="importStep === 0">
        <el-alert title="请按行填写公式信息，每行一条。格式：公式名称 | 公式类型 | 公式表达式 | 说明（可选）" type="info" :closable="false" style="margin-bottom: 15px;" />
        <el-alert title="公式类型可选值：CALCULATION(计算)、VALIDATION(验证)、ALLOCATION(分配)、FORECAST(预测)" type="warning" :closable="false" style="margin-bottom: 15px;" />
        <el-input
          v-model="importText"
          type="textarea"
          :rows="10"
          placeholder="示例：&#10;年度预算汇总 | CALCULATION | SUM(A1:A12) | 汇总12个月预算&#10;增长率校验 | VALIDATION | (B1-A1)/A1*100 | 同比增长率"
        />
      </div>

      <div v-if="importStep === 1">
        <el-result
          :icon="importResult.failCount === 0 ? 'success' : 'warning'"
          :title="'导入完成：成功 ' + importResult.successCount + ' 条，失败 ' + importResult.failCount + ' 条'"
        >
          <template slot="extra">
            <div v-if="importResult.errors && importResult.errors.length > 0" style="text-align: left; max-height: 200px; overflow-y: auto;">
              <el-alert v-for="(err, idx) in importResult.errors" :key="idx" :title="err" type="error" :closable="false" style="margin-bottom: 5px;" />
            </div>
          </template>
        </el-result>
      </div>

      <div slot="footer">
        <el-button @click="importDialogVisible = false">{{ importStep === 1 ? '关闭' : '取消' }}</el-button>
        <el-button v-if="importStep === 0" type="primary" :loading="importLoading" @click="submitImport">开始导入</el-button>
        <el-button v-if="importStep === 1 && importResult.successCount > 0" type="primary" @click="importDialogVisible = false; getList()">完成</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetFormulaApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetFormula',
  data() {
    return {
      // 查询参数
      queryForm: {
        formulaName: '',
        formulaType: '',
        formulaStatus: '',
        categoryId: null
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      formulaList: [],
      total: 0,
      selectedRows: [],
      
      // 统计数据
      formulaStats: {
        totalFormulas: 0,
        activeFormulas: 0,
        complexFormulas: 0,
        errorFormulas: 0,
        activeRate: 0,
        complexRate: 0,
        errorRate: 0
      },
      
      // 分类相关
      categoryTree: [],
      currentCategoryId: null,
      categoryOptions: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      formulaForm: {
        formulaId: null,
        formulaName: '',
        formulaType: '',
        categoryId: null,
        priority: 1,
        formulaDescription: '',
        formulaExpression: '',
        formulaParameters: [],
        isPublic: false,
        allowCopy: true,
        autoValidate: true
      },
      formulaRules: {
        formulaName: [
          { required: true, message: '请输入公式名称', trigger: 'blur' }
        ],
        formulaType: [
          { required: true, message: '请选择公式类型', trigger: 'change' }
        ],
        formulaExpression: [
          { required: true, message: '请输入公式表达式', trigger: 'blur' }
        ]
      },
      
      // 公式编辑器
      activeTab: 'variables',
      availableVariables: [
        { code: 'BUDGET_AMOUNT', name: '预算金额' },
        { code: 'ACTUAL_AMOUNT', name: '实际金额' },
        { code: 'LAST_YEAR_AMOUNT', name: '上年金额' },
        { code: 'GROWTH_RATE', name: '增长率' }
      ],
      availableFunctions: [
        { name: 'SUM', description: '求和函数' },
        { name: 'AVG', description: '平均值函数' },
        { name: 'MAX', description: '最大值函数' },
        { name: 'MIN', description: '最小值函数' },
        { name: 'COUNT', description: '计数函数' },
        { name: 'IF', description: '条件函数' }
      ],
      
      // 测试对话框
      testDialogVisible: false,
      testFormula: {},
      testParameters: {},
      testResult: '',
      
      // 选项数据
      formulaTypeOptions: [
        { value: 'CALCULATION', label: '计算公式' },
        { value: 'VALIDATION', label: '验证公式' },
        { value: 'ALLOCATION', label: '分配公式' },
        { value: 'FORECAST', label: '预测公式' }
      ],
      formulaStatusOptions: [
        { value: 'DRAFT', label: '草稿' },
        { value: 'PUBLISHED', label: '已发布' },
        { value: 'ARCHIVED', label: '已归档' }
      ],
      parameterTypeOptions: [
        { value: 'NUMBER', label: '数字' },
        { value: 'TEXT', label: '文本' },
        { value: 'DATE', label: '日期' },
        { value: 'BOOLEAN', label: '布尔值' }
      ],

      detailDialogVisible: false,
      formulaDetail: {},

      settingsDialogVisible: false,
      settingsForm: { enableValidation: true, allowCustomFunctions: false },

      categoryDialogVisible: false,

      historyDialogVisible: false,
      historyLoading: false,
      historyList: [],
      historyRow: {},

      usageDialogVisible: false,
      usageLoading: false,
      usageList: [],
      usageRow: {},

      // 导入相关
      importDialogVisible: false,
      importStep: 0,
      importText: '',
      importLoading: false,
      importResult: { successCount: 0, failCount: 0, errors: [] }
    }
  },
  
  created() {
    this.getList()
    this.loadCategoryTree()
    this.loadCategoryOptions()
    this.loadStats()
  },
  
  methods: {
    // 加载统计数据
    async loadStats() {
      try {
        const response = await budgetFormulaApi.getStats()
        if (response.code === 1) this.formulaStats = response.data
      } catch (error) {
        console.error('加载统计数据失败：', error)
      }
    },
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.queryParams,
          categoryId: this.currentCategoryId
        }
        const response = await budgetFormulaApi.getPage(params)
        this.formulaList = response.data.records
        this.total = response.data.total
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载分类树
    async loadCategoryTree() {
      try {
        const response = await budgetFormulaApi.getCategoryTree()
        this.categoryTree = response.data
      } catch (error) {
        console.error('加载分类树失败：', error)
      }
    },
    
    // 加载分类选项
    async loadCategoryOptions() {
      try {
        const response = await budgetFormulaApi.getCategories()
        this.categoryOptions = response.data
      } catch (error) {
        console.error('加载分类选项失败：', error)
      }
    },
    
    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 重置
    handleReset() {
      this.queryForm = {
        formulaName: '',
        formulaType: '',
        formulaStatus: '',
        categoryId: null
      }
      this.handleQuery()
    },
    
    // 分类点击
    handleCategoryClick(data) {
      this.currentCategoryId = data.id
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 创建公式
    handleCreateFormula() {
      this.dialogTitle = '创建预算公式'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑公式
    handleEdit(row) {
      this.dialogTitle = '编辑预算公式'
      this.dialogVisible = true
      this.formulaForm = { ...row, formulaParameters: [] }
      // 将后端 inputParameters JSON字符串解析为前端 formulaParameters 数组
      this.parseInputParameters(row)
    },
    
    // 查看公式
    handleView(row) {
      this.formulaDetail = { ...row }
      this.detailDialogVisible = true
    },
    
    // 测试公式
    handleTest(row) {
      this.testFormula = row
      this.testParameters = {}
      this.testResult = ''
      this.testDialogVisible = true
    },
    
    // 插入函数
    insertFunction(funcName) {
      const textarea = this.$refs.formulaEditor.$refs.textarea
      const start = textarea.selectionStart
      const end = textarea.selectionEnd
      const text = this.formulaForm.formulaExpression
      
      this.formulaForm.formulaExpression = text.substring(0, start) + funcName + '()' + text.substring(end)
      
      this.$nextTick(() => {
        textarea.focus()
        textarea.setSelectionRange(start + funcName.length + 1, start + funcName.length + 1)
      })
    },
    
    // 插入操作符
    insertOperator(operator) {
      const textarea = this.$refs.formulaEditor.$refs.textarea
      const start = textarea.selectionStart
      const end = textarea.selectionEnd
      const text = this.formulaForm.formulaExpression
      
      this.formulaForm.formulaExpression = text.substring(0, start) + operator + text.substring(end)
      
      this.$nextTick(() => {
        textarea.focus()
        textarea.setSelectionRange(start + operator.length, start + operator.length)
      })
    },
    
    // 插入变量
    insertVariable(variable) {
      const textarea = this.$refs.formulaEditor.$refs.textarea
      const start = textarea.selectionStart
      const end = textarea.selectionEnd
      const text = this.formulaForm.formulaExpression
      
      this.formulaForm.formulaExpression = text.substring(0, start) + variable.code + text.substring(end)
      
      this.$nextTick(() => {
        textarea.focus()
        textarea.setSelectionRange(start + variable.code.length, start + variable.code.length)
      })
    },
    
    // 验证公式
    async handleValidateFormula() {
      if (!this.formulaForm.formulaExpression) {
        this.$message.warning('请输入公式表达式')
        return
      }
      
      try {
        const response = await budgetFormulaApi.validateExpression(this.formulaForm.formulaExpression)
        if (response.data.valid) {
          this.$message.success('公式验证通过')
        } else {
          this.$message.error('公式验证失败：' + response.data.message)
        }
      } catch (error) {
        this.$message.error('验证失败：' + error.message)
      }
    },
    
    // 添加参数
    handleAddParameter() {
      this.formulaForm.formulaParameters.push({
        parameterName: '',
        parameterType: 'NUMBER',
        defaultValue: '',
        required: false,
        parameterDescription: ''
      })
    },
    
    // 删除参数
    handleRemoveParameter(index) {
      this.formulaForm.formulaParameters.splice(index, 1)
    },
    
    // 自动检测参数
    handleAutoDetectParameters() {
      if (!this.formulaForm.formulaExpression) {
        this.$message.warning('请先输入公式表达式')
        return
      }
      
      // 简单的参数检测逻辑（实际应该调用后端API）
      const regex = /\$\{(\w+)\}/g
      const matches = []
      let match
      
      while ((match = regex.exec(this.formulaForm.formulaExpression)) !== null) {
        if (!matches.includes(match[1])) {
          matches.push(match[1])
        }
      }
      
      matches.forEach(paramName => {
        const exists = this.formulaForm.formulaParameters.some(p => p.parameterName === paramName)
        if (!exists) {
          this.formulaForm.formulaParameters.push({
            parameterName: paramName,
            parameterType: 'NUMBER',
            defaultValue: '',
            required: true,
            parameterDescription: ''
          })
        }
      })
      
      this.$message.success(`检测到 ${matches.length} 个参数`)
    },
    
    // 计算公式
    async handleCalculateFormula() {
      try {
        const params = {
          formulaExpression: this.testFormula.formulaExpression,
          parameters: this.testParameters
        }
        const response = await budgetFormulaApi.calculateFormula(params)
        this.testResult = response.data.result
      } catch (error) {
        this.$message.error('计算失败：' + error.message)
        this.testResult = 'ERROR: ' + error.message
      }
    },
    
    // 保存公式
    async handleSaveFormula() {
      try {
        await this.$refs.formulaForm.validate()

        const params = {
          ...this.formulaForm,
          isEnabled: 0,
          formulaType: Array.isArray(this.formulaForm.categoryId) ? this.formulaForm.categoryId[this.formulaForm.categoryId.length - 1] : (this.formulaForm.formulaType || this.formulaForm.categoryId)
        }
        // cascader 返回数组，取最后一个值
        if (Array.isArray(params.categoryId)) {
          params.categoryId = params.categoryId[params.categoryId.length - 1]
        }

        // 将前端 formulaParameters 数组转为后端 inputParameters JSON字符串
        if (params.formulaParameters && params.formulaParameters.length > 0) {
          params.inputParameters = JSON.stringify(params.formulaParameters)
        } else {
          params.inputParameters = null
        }
        delete params.formulaParameters

        if (this.formulaForm.formulaId) {
          await budgetFormulaApi.update(params)
          this.$message.success('保存成功')
        } else {
          await budgetFormulaApi.create(params)
          this.$message.success('创建成功')
        }

        this.dialogVisible = false
        this.getList()
      } catch (error) {
        if (error !== false) {
          this.$message.error('保存失败：' + (error.message || ''))
        }
      }
    },

    // 发布公式
    async handlePublishFormula() {
      try {
        await this.$refs.formulaForm.validate()

        const params = {
          ...this.formulaForm,
          isEnabled: 1,
          formulaType: Array.isArray(this.formulaForm.categoryId) ? this.formulaForm.categoryId[this.formulaForm.categoryId.length - 1] : (this.formulaForm.formulaType || this.formulaForm.categoryId)
        }
        // cascader 返回数组，取最后一个值
        if (Array.isArray(params.categoryId)) {
          params.categoryId = params.categoryId[params.categoryId.length - 1]
        }

        // 将前端 formulaParameters 数组转为后端 inputParameters JSON字符串
        if (params.formulaParameters && params.formulaParameters.length > 0) {
          params.inputParameters = JSON.stringify(params.formulaParameters)
        } else {
          params.inputParameters = null
        }
        delete params.formulaParameters

        if (this.formulaForm.formulaId) {
          await budgetFormulaApi.update(params)
          this.$message.success('发布成功')
        } else {
          await budgetFormulaApi.create(params)
          this.$message.success('创建并发布成功')
        }

        this.dialogVisible = false
        this.getList()
      } catch (error) {
        if (error !== false) {
          this.$message.error('发布失败：' + (error.message || ''))
        }
      }
    },
    
    // 重置表单
    resetForm() {
      this.formulaForm = {
        id: null,
        formulaName: '',
        formulaType: '',
        categoryId: null,
        priority: 1,
        formulaDescription: '',
        formulaExpression: '',
        formulaParameters: [],
        isPublic: false,
        allowCopy: true,
        autoValidate: true
      }
      this.$nextTick(() => {
        this.$refs.formulaForm && this.$refs.formulaForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入公式
    handleImportFormula() {
      this.importStep = 0
      this.importText = ''
      this.importResult = { successCount: 0, failCount: 0, errors: [] }
      this.importDialogVisible = true
    },

    // 提交导入
    async submitImport() {
      if (!this.importText.trim()) {
        this.$message.warning('请输入要导入的公式数据')
        return
      }
      const lines = this.importText.trim().split('\n').filter(l => l.trim())
      if (lines.length === 0) {
        this.$message.warning('没有有效的数据行')
        return
      }
      const formulas = lines.map(line => {
        const parts = line.split('|').map(s => s.trim())
        return {
          formulaName: parts[0] || '',
          formulaType: parts[1] || 'CALCULATION',
          formulaExpression: parts[2] || '',
          formulaDescription: parts[3] || ''
        }
      })
      this.importLoading = true
      try {
        const response = await budgetFormulaApi.importFormulas(formulas)
        this.importResult = response.data || response
        this.importStep = 1
        if (this.importResult.successCount > 0) {
          this.getList()
          this.loadStats()
        }
      } catch (error) {
        this.$message.error('导入失败：' + (error.message || ''))
      } finally {
        this.importLoading = false
      }
    },
    
    // 批量验证
    async handleBatchValidate() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要验证的公式')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.formulaId)
        const response = await budgetFormulaApi.batchValidate(ids)
        const data = response.data || response
        const msg = '验证完成：共 ' + data.total + ' 条，通过 ' + data.valid + ' 条，失败 ' + data.invalid + ' 条'
        if (data.invalid > 0) {
          this.$message.warning(msg)
        } else {
          this.$message.success(msg)
        }
        this.getList()
      } catch (error) {
        this.$message.error('批量验证失败：' + (error.message || ''))
      }
    },
    
    // 导出公式
    async handleExportFormula() {
      try {
        const params = { ...this.queryForm }
        const response = await budgetFormulaApi.export(params)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算公式_' + new Date().getTime() + '.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || ''))
      }
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
    },
    
    // 设置
    handleSettings() {
      this.settingsDialogVisible = true
    },

    // 分类管理
    handleCategoryManagement() {
      this.categoryDialogVisible = true
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopy(row)
          break
        case 'validate':
          this.handleValidate(row)
          break
        case 'history':
          this.handleHistory(row)
          break
        case 'usage':
          this.handleUsage(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
        case 'delete':
          this.handleDeleteFormula(row)
          break
      }
    },

    // 删除公式
    async handleDeleteFormula(row) {
      try {
        await this.$confirm('确定要删除公式「' + row.formulaName + '」吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await budgetFormulaApi.delete(row.formulaId)
        this.$message.success('删除成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + (error.message || ''))
        }
      }
    },
    
    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算公式'
      this.dialogVisible = true
      this.formulaForm = { ...row, formulaId: null, formulaCode: null, formulaParameters: [] }
      // 将后端 inputParameters JSON字符串解析为前端 formulaParameters 数组
      this.parseInputParameters(row)
    },
    
    // 验证
    async handleValidate(row) {
      try {
        const response = await budgetFormulaApi.validate(row.formulaId)
        const data = response.data || response
        if (data.valid) {
          this.$message.success('「' + row.formulaName + '」验证通过')
        } else {
          this.$message.warning('「' + row.formulaName + '」验证失败：' + (data.message || ''))
        }
        this.getList()
      } catch (error) {
        this.$message.error('验证失败：' + (error.message || ''))
      }
    },
    
    // 历史版本
    async handleHistory(row) {
      this.historyRow = row
      this.historyDialogVisible = true
      this.historyLoading = true
      try {
        this.historyList = [
          { operateTime: row.createTime, version: 'v1.0', operator: '系统', remark: '初始版本' },
          { operateTime: row.updateTime || row.createTime, version: row.version || 'v1.1', operator: '管理员', remark: '更新公式' }
        ]
      } finally {
        this.historyLoading = false
      }
    },

    // 使用情况
    async handleUsage(row) {
      this.usageRow = row
      this.usageDialogVisible = true
      this.usageLoading = true
      try {
        this.usageList = [
          { budgetName: '年度预算', sceneName: '计算', lastUsedTime: row.updateTime || row.createTime }
        ]
      } finally {
        this.usageLoading = false
      }
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        const response = await budgetFormulaApi.exportSingle(row.formulaId)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = (row.formulaName || '公式') + '.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || ''))
      }
    },
    
    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },
    
    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 排序改变
    handleSortChange({ column, prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getList()
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    
    // 判断是否可以编辑
    canEdit(row) {
      return row.formulaStatus === 'DRAFT'
    },
    
    // 获取公式类型颜色
    getFormulaTypeColor(type) {
      const colorMap = {
        'CALCULATION': 'primary',
        'VALIDATION': 'success',
        'ALLOCATION': 'warning',
        'FORECAST': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取公式类型文本
    getFormulaTypeText(type) {
      const item = this.formulaTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取验证状态类型
    getValidationStatusType(status) {
      const statusMap = {
        'VALID': 'success',
        'INVALID': 'danger',
        'PENDING': 'warning'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取验证状态文本
    getValidationStatusText(status) {
      const textMap = {
        'VALID': '有效',
        'INVALID': '无效',
        'PENDING': '待验证'
      }
      return textMap[status] || status
    },
    
    // 获取公式状态类型
    getFormulaStatusType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'PUBLISHED': 'success',
        'ARCHIVED': 'warning'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取公式状态文本
    getFormulaStatusText(status) {
      const item = this.formulaStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    },

    // 解析后端 inputParameters JSON字符串为前端 formulaParameters 数组
    parseInputParameters(row) {
      if (row.inputParameters) {
        try {
          const parsed = typeof row.inputParameters === 'string' ? JSON.parse(row.inputParameters) : row.inputParameters
          this.formulaForm.formulaParameters = Array.isArray(parsed) ? parsed : []
        } catch (e) {
          console.error('解析公式参数失败：', e)
          this.formulaForm.formulaParameters = []
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-formula {
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
  .category-card {
    margin-bottom: 20px;
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
      
      &.complex-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.error-card {
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
          margin-bottom: 8px;
        }
        
        .stat-progress {
          margin-top: 8px;
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
  
  .category-card {
    .category-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h3 {
        color: #303133;
        margin: 0;
      }
    }
    
    .category-tree {
      border-right: 1px solid #EBEEF5;
      padding-right: 20px;
      
      .tree-node {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        
        .node-count {
          color: #909399;
          font-size: 12px;
        }
      }
    }
    
    .search-form {
      margin-bottom: 20px;
      padding-bottom: 20px;
      border-bottom: 1px solid #EBEEF5;
    }
  }
  
  .formula-list {
    .formula-code {
      background-color: #f5f5f5;
      padding: 2px 4px;
      border-radius: 3px;
      font-family: 'Courier New', monospace;
      font-size: 12px;
    }
    
    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }
  }
  
  .formula-editor {
    display: flex;
    gap: 20px;
    
    .editor-main {
      flex: 1;
      
      .editor-toolbar {
        margin-bottom: 10px;
        padding: 10px;
        background-color: #f5f5f5;
        border-radius: 4px;
      }
      
      .formula-textarea {
        font-family: 'Courier New', monospace;
      }
    }
    
    .editor-sidebar {
      width: 300px;
      
      .variable-list,
      .function-list {
        max-height: 200px;
        overflow-y: auto;
        
        .variable-item,
        .function-item {
          padding: 8px;
          border: 1px solid #EBEEF5;
          border-radius: 4px;
          margin-bottom: 8px;
          cursor: pointer;
          
          &:hover {
            background-color: #F0F9FF;
            border-color: #409EFF;
          }
          
          .variable-code,
          .function-name {
            font-weight: 500;
            color: #303133;
          }
          
          .variable-name,
          .function-desc {
            font-size: 12px;
            color: #909399;
            margin-top: 4px;
          }
        }
      }
    }
  }
  
  .formula-parameters {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    
    .parameters-header {
      padding: 12px;
      background-color: #F5F7FA;
      border-bottom: 1px solid #EBEEF5;
      display: flex;
      gap: 8px;
    }
  }
  
  .formula-test {
    .test-formula {
      margin-bottom: 20px;
      
      h4 {
        color: #303133;
        margin: 0 0 10px 0;
      }
      
      .formula-display {
        display: block;
        background-color: #f5f5f5;
        padding: 10px;
        border-radius: 4px;
        font-family: 'Courier New', monospace;
      }
    }
    
    .test-parameters {
      margin-bottom: 20px;
      
      h4 {
        color: #303133;
        margin: 0 0 15px 0;
      }
      
      .parameter-desc {
        margin-left: 10px;
        color: #909399;
        font-size: 12px;
      }
    }
    
    .test-result {
      h4 {
        color: #303133;
        margin: 0 0 10px 0;
      }
      
      .result-display {
        font-family: 'Courier New', monospace;
      }
    }
  }
  
  .primary-text {
    color: #409EFF;
  }
  
  .danger-text {
    color: #F56C6C;
  }
  
  .text-right {
    text-align: right;
  }
}
</style>
