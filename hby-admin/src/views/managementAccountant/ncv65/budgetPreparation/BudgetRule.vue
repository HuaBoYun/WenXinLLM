<template>
  <div class="budget-rule">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算规则管理</h2>
      <p>管理预算业务规则，支持规则配置、条件设置、动作定义和优先级管理</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateRule">创建规则</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportRule">导入规则</el-button>
            <el-button type="warning" icon="el-icon-check" @click="handleBatchValidate">批量验证</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportRule">导出规则</el-button>
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

    <!-- 规则统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ ruleStats.totalRules }}</div>
            <div class="stat-label">规则总数</div>
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
            <div class="stat-number">{{ ruleStats.activeRules }}</div>
            <div class="stat-label">启用规则</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="ruleStats.activeRate" 
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
            <div class="stat-number">{{ ruleStats.complexRules }}</div>
            <div class="stat-label">复杂规则</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="ruleStats.complexRate" 
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
            <div class="stat-number">{{ ruleStats.errorRules }}</div>
            <div class="stat-label">异常规则</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="ruleStats.errorRate" 
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

    <!-- 规则分类和查询 -->
    <el-card class="search-card" shadow="never">
      <el-row :gutter="20">
        <el-col :span="4">
          <div class="rule-categories">
            <h4>规则分类</h4>
            <el-tree
              ref="categoryTreeRef"
              :data="categoryTree"
              :props="{ children: 'children', label: 'name' }"
              node-key="id"
              :current-node-key="currentCategoryId"
              highlight-current
              @node-click="handleCategoryClick"
              :expand-on-click-node="false"
            >
              <span class="tree-node" slot-scope="{ node, data }">
                <span class="node-label">{{ node.label }}</span>
                <span class="node-count">({{ data.ruleCount || 0 }})</span>
              </span>
            </el-tree>
          </div>
        </el-col>
        <el-col :span="20">
          <el-form :model="queryForm" :inline="true" size="small">
            <el-form-item label="规则名称">
              <el-input
                v-model="queryForm.ruleName"
                placeholder="请输入规则名称"
                clearable
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item label="规则类型">
              <el-select
                v-model="queryForm.ruleType"
                placeholder="请选择规则类型"
                clearable
                style="width: 150px"
              >
                <el-option
                  v-for="item in ruleTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="规则状态">
              <el-select
                v-model="queryForm.ruleStatus"
                placeholder="请选择规则状态"
                clearable
                style="width: 150px"
              >
                <el-option
                  v-for="item in ruleStatusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="优先级">
              <el-select
                v-model="queryForm.priority"
                placeholder="请选择优先级"
                clearable
                style="width: 120px"
              >
                <el-option
                  v-for="item in priorityOptions"
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
        </el-col>
      </el-row>
    </el-card>

    <!-- 规则列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算规则列表</span>
        <div class="table-tools">
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getList" />
          </el-tooltip>
          <el-tooltip content="列设置" placement="top">
            <el-button icon="el-icon-setting" size="mini" @click="handleColumnSetting" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="ruleList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column prop="ruleCode" label="规则编号" width="120" show-overflow-tooltip />
        <el-table-column prop="ruleName" label="规则名称" width="180" show-overflow-tooltip />
        
        <el-table-column prop="ruleType" label="规则类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getRuleTypeColor(scope.row.ruleType)">
              {{ getRuleTypeText(scope.row.ruleType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="priority" label="优先级" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getPriorityColor(scope.row.priority)">
              {{ getPriorityText(scope.row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="ruleCondition" label="规则条件" min-width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <code class="rule-code">{{ scope.row.ruleCondition }}</code>
          </template>
        </el-table-column>
        
        <el-table-column prop="ruleAction" label="规则动作" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-tag size="mini" :type="getActionColor(scope.row.ruleAction)">
              {{ getActionText(scope.row.ruleAction) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="executionCount" label="执行次数" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <span class="number-text">{{ scope.row.executionCount || 0 }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="lastExecutionTime" label="最后执行" width="150" align="center" />
        
        <el-table-column prop="ruleStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.ruleStatus"
              :active-value="'ACTIVE'"
              :inactive-value="'INACTIVE'"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        
        <el-table-column prop="creator" label="创建人" width="100" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click.stop="handleView(scope.row)"
            >查看</el-button>
            <el-button
              v-if="canEdit(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-cpu"
              class="primary-text"
              @click.stop="handleTest(scope.row)"
            >测试</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
              @click.native.stop
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="validate" icon="el-icon-check">验证</el-dropdown-item>
                <el-dropdown-item command="history" icon="el-icon-time">执行历史</el-dropdown-item>
                <el-dropdown-item command="log" icon="el-icon-document">执行日志</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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
    </el-card>

    <!-- 新增/编辑规则对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1200px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="ruleForm"
        :model="ruleForm"
        :rules="ruleRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input
                v-model="ruleForm.ruleName"
                placeholder="请输入规则名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则类型" prop="ruleType">
              <el-select
                v-model="ruleForm.ruleType"
                placeholder="请选择规则类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in ruleTypeOptions"
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
                v-model="ruleForm.categoryId"
                :options="categoryOptions"
                :props="{ checkStrictly: true, value: 'id', label: 'name' }"
                placeholder="请选择所属分类"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select
                v-model="ruleForm.priority"
                placeholder="请选择优先级"
                style="width: 100%"
              >
                <el-option
                  v-for="item in priorityOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="规则描述" prop="ruleDescription">
          <el-input
            v-model="ruleForm.ruleDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入规则描述"
          />
        </el-form-item>
        
        <!-- 规则条件编辑器 -->
        <el-form-item label="规则条件" prop="ruleCondition">
          <div class="rule-condition-editor">
            <div class="condition-toolbar">
              <el-button-group size="mini">
                <el-button @click="insertCondition('AND')">AND</el-button>
                <el-button @click="insertCondition('OR')">OR</el-button>
                <el-button @click="insertCondition('NOT')">NOT</el-button>
                <el-button @click="insertCondition('()')">()</el-button>
              </el-button-group>
              <el-button-group size="mini" style="margin-left: 10px;">
                <el-button @click="insertOperator('=')">=</el-button>
                <el-button @click="insertOperator('>')">&gt;</el-button>
                <el-button @click="insertOperator('<')">&lt;</el-button>
                <el-button @click="insertOperator('>=')">&gt;=</el-button>
                <el-button @click="insertOperator('<=')">&lt;=</el-button>
                <el-button @click="insertOperator('!=')">&ne;</el-button>
              </el-button-group>
              <el-button size="mini" type="primary" @click="handleValidateCondition" style="margin-left: 10px;">
                验证条件
              </el-button>
            </div>
            
            <el-input
              ref="conditionEditor"
              v-model="ruleForm.ruleCondition"
              type="textarea"
              :rows="4"
              placeholder="请输入规则条件，例如：BUDGET_AMOUNT > 100000 AND DEPARTMENT = 'IT'"
              class="condition-textarea"
            />
            
            <div class="condition-help">
              <el-collapse accordion>
                <el-collapse-item title="可用字段" name="fields">
                  <div class="field-list">
                    <el-tag
                      v-for="field in availableFields"
                      :key="field.code"
                      size="mini"
                      class="field-tag"
                      @click="insertField(field)"
                    >
                      {{ field.code }} - {{ field.name }}
                    </el-tag>
                  </div>
                </el-collapse-item>
                <el-collapse-item title="条件示例" name="examples">
                  <div class="example-list">
                    <div
                      v-for="example in conditionExamples"
                      :key="example.title"
                      class="example-item"
                      @click="useExample(example)"
                    >
                      <div class="example-title">{{ example.title }}</div>
                      <code class="example-code">{{ example.condition }}</code>
                    </div>
                  </div>
                </el-collapse-item>
              </el-collapse>
            </div>
          </div>
        </el-form-item>
        
        <!-- 规则动作配置 -->
        <el-form-item label="规则动作" prop="ruleAction">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-select
                v-model="ruleForm.ruleAction"
                placeholder="请选择规则动作"
                style="width: 100%"
                @change="handleActionChange"
              >
                <el-option
                  v-for="item in ruleActionOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-col>
            <el-col :span="16">
              <el-input
                v-model="ruleForm.actionParameters"
                placeholder="请输入动作参数"
                :disabled="!ruleForm.ruleAction"
              />
            </el-col>
          </el-row>
        </el-form-item>
        
        <!-- 规则配置 -->
        <el-form-item label="规则配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="ruleForm.isGlobal">全局规则</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="ruleForm.stopOnMatch">匹配后停止</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="ruleForm.logExecution">记录执行日志</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
        
        <!-- 生效时间 -->
        <el-form-item label="生效时间">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-date-picker
                v-model="ruleForm.effectiveStartTime"
                type="datetime"
                placeholder="选择生效开始时间"
                style="width: 100%"
              />
            </el-col>
            <el-col :span="12">
              <el-date-picker
                v-model="ruleForm.effectiveEndTime"
                type="datetime"
                placeholder="选择生效结束时间"
                style="width: 100%"
              />
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleSaveRule">保存规则</el-button>
        <el-button type="primary" @click="handlePublishRule">发布规则</el-button>
      </div>
    </el-dialog>

    <!-- 规则测试对话框 -->
    <el-dialog
      title="规则测试"
      :visible.sync="testDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div class="rule-test">
        <div class="test-rule">
          <h4>测试规则</h4>
          <div class="rule-info">
            <div><strong>规则名称：</strong>{{ testRule.ruleName }}</div>
            <div><strong>规则条件：</strong><code>{{ testRule.ruleCondition }}</code></div>
            <div><strong>规则动作：</strong>{{ getActionText(testRule.ruleAction) }}</div>
          </div>
        </div>
        
        <div class="test-data">
          <h4>测试数据</h4>
          <el-form label-width="120px" size="small">
            <el-form-item
              v-for="field in testFields"
              :key="field.code"
              :label="field.name"
            >
              <el-input
                v-model="testData[field.code]"
                :placeholder="'请输入' + field.name"
                style="width: 200px"
              />
            </el-form-item>
          </el-form>
        </div>
        
        <div class="test-result">
          <h4>测试结果</h4>
          <div class="result-display">
            <el-alert
              v-if="testResult"
              :title="testResult.title"
              :type="testResult.type"
              :description="testResult.description"
              show-icon
            />
            <div v-else class="no-result">
              点击测试按钮查看结果
            </div>
          </div>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleExecuteTest">测试</el-button>
      </div>
    </el-dialog>

    <!-- 规则详情对话框 -->
    <el-dialog title="规则详情" :visible.sync="detailDialogVisible" width="700px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="规则名称">{{ ruleDetail.ruleName }}</el-descriptions-item>
        <el-descriptions-item label="规则类型">{{ ruleDetail.ruleType }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ ruleDetail.ruleStatus }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ ruleDetail.priority }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ ruleDetail.description || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 规则设置对话框 -->
    <el-dialog title="规则设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="140px" size="small">
        <el-form-item label="启用规则引擎">
          <el-switch v-model="settingsForm.enableRuleEngine" />
        </el-form-item>
        <el-form-item label="并行执行规则">
          <el-switch v-model="settingsForm.parallelExecution" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 执行历史对话框 -->
    <el-dialog :title="'执行历史 - ' + (historyRow.ruleName || '')" :visible.sync="historyDialogVisible" width="750px" :close-on-click-modal="false">
      <el-table :data="historyList" border size="small" v-loading="historyLoading" empty-text="暂无历史记录">
        <el-table-column prop="operateTime" label="执行时间" width="170" />
        <el-table-column prop="result" label="执行结果" width="100" />
        <el-table-column prop="operator" label="触发人" width="100" />
        <el-table-column prop="remark" label="说明" />
      </el-table>
      <div slot="footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 执行日志对话框 -->
    <el-dialog :title="'执行日志 - ' + (logRow.ruleName || '')" :visible.sync="logDialogVisible" width="750px" :close-on-click-modal="false">
      <el-table :data="logList" border size="small" v-loading="logLoading" empty-text="暂无日志">
        <el-table-column prop="logTime" label="时间" width="170" />
        <el-table-column prop="logLevel" label="级别" width="80" />
        <el-table-column prop="message" label="日志内容" />
      </el-table>
      <div slot="footer">
        <el-button @click="logDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetRuleApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetRule',
  data() {
    return {
      // 查询参数
      queryForm: {
        ruleName: '',
        ruleType: '',
        ruleStatus: '',
        priority: '',
        categoryId: null
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      ruleList: [],
      total: 0,
      selectedRows: [],
      
      // 统计数据
      ruleStats: {
        totalRules: 0,
        activeRules: 0,
        complexRules: 0,
        errorRules: 0,
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
      ruleForm: {
        ruleId: null,
        ruleName: '',
        ruleType: '',
        categoryId: null,
        priority: 5,
        ruleDescription: '',
        ruleCondition: '',
        ruleAction: '',
        actionParameters: '',
        isGlobal: false,
        stopOnMatch: false,
        logExecution: true,
        effectiveStartTime: null,
        effectiveEndTime: null
      },
      ruleRules: {
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' }
        ],
        ruleType: [
          { required: true, message: '请选择规则类型', trigger: 'change' }
        ],
        ruleCondition: [
          { required: true, message: '请输入规则条件', trigger: 'blur' }
        ],
        ruleAction: [
          { required: true, message: '请选择规则动作', trigger: 'change' }
        ]
      },
      
      // 规则编辑器
      availableFields: [
        { code: 'BUDGET_AMOUNT', name: '预算金额' },
        { code: 'ACTUAL_AMOUNT', name: '实际金额' },
        { code: 'DEPARTMENT', name: '部门' },
        { code: 'PROJECT', name: '项目' },
        { code: 'ACCOUNT', name: '科目' },
        { code: 'PERIOD', name: '期间' }
      ],
      conditionExamples: [
        {
          title: '预算金额限制',
          condition: 'BUDGET_AMOUNT > 100000'
        },
        {
          title: '部门预算控制',
          condition: 'DEPARTMENT = "IT" AND BUDGET_AMOUNT > 50000'
        },
        {
          title: '项目预算检查',
          condition: 'PROJECT IS NOT NULL AND BUDGET_AMOUNT <= ACTUAL_AMOUNT * 1.2'
        }
      ],
      
      // 测试对话框
      testDialogVisible: false,
      testRule: {},
      testData: {},
      testResult: null,
      testFields: [],
      
      // 选项数据
      ruleTypeOptions: [
        { value: 'VALIDATION', label: '验证规则' },
        { value: 'CALCULATION', label: '计算规则' },
        { value: 'APPROVAL', label: '审批规则' },
        { value: 'NOTIFICATION', label: '通知规则' },
        { value: 'WORKFLOW', label: '流程规则' }
      ],
      ruleStatusOptions: [
        { value: 'ACTIVE', label: '启用' },
        { value: 'INACTIVE', label: '禁用' }
      ],
      priorityOptions: [
        { value: 1, label: '高' },
        { value: 5, label: '中' },
        { value: 9, label: '低' }
      ],
      ruleActionOptions: [
        { value: 'APPROVE', label: '自动审批' },
        { value: 'REJECT', label: '自动拒绝' },
        { value: 'NOTIFY', label: '发送通知' },
        { value: 'CALCULATE', label: '执行计算' },
        { value: 'REDIRECT', label: '流程跳转' }
      ],

      detailDialogVisible: false,
      ruleDetail: {},

      settingsDialogVisible: false,
      settingsForm: { enableRuleEngine: true, parallelExecution: false },

      historyDialogVisible: false,
      historyLoading: false,
      historyList: [],
      historyRow: {},

      logDialogVisible: false,
      logLoading: false,
      logList: [],
      logRow: {}
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
        const response = await budgetRuleApi.getStats()
        if (response.code === 1) this.ruleStats = response.data
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
        const response = await budgetRuleApi.getPage(params)
        this.ruleList = response.data.records
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
        const response = await budgetRuleApi.getCategoryTree()
        this.categoryTree = response.data
      } catch (error) {
        console.error('加载分类树失败：', error)
      }
    },
    
    // 加载分类选项
    async loadCategoryOptions() {
      try {
        const response = await budgetRuleApi.getCategories()
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
        ruleName: '',
        ruleType: '',
        ruleStatus: '',
        priority: '',
        categoryId: null
      }
      // 清除分类选中状态
      this.currentCategoryId = null
      if (this.$refs.categoryTreeRef) {
        this.$refs.categoryTreeRef.setCurrentKey(null)
      }
      this.handleQuery()
    },
    
    // 分类点击 - 再次点击同一分类取消选中，显示全部
    handleCategoryClick(data) {
      if (this.currentCategoryId === data.id) {
        this.currentCategoryId = null
        this.$refs.categoryTreeRef.setCurrentKey(null)
      } else {
        this.currentCategoryId = data.id
      }
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 创建规则
    handleCreateRule() {
      this.dialogTitle = '创建预算规则'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑规则
    handleEdit(row) {
      this.dialogTitle = '编辑预算规则'
      this.dialogVisible = true
      this.ruleForm = {
        ...row,
        categoryId: row.ruleCategory ? [row.ruleCategory] : null,
        effectiveStartTime: row.effectiveDate || null,
        effectiveEndTime: row.expiryDate || null,
        actionParameters: row.actionParameters || '',
        isGlobal: row.isGlobal || false,
        stopOnMatch: row.stopOnMatch || false,
        logExecution: row.logExecution !== undefined ? row.logExecution : true
      }
    },
    
    // 查看规则
    handleView(row) {
      this.ruleDetail = { ...row }
      this.detailDialogVisible = true
    },
    
    // 测试规则
    handleTest(row) {
      this.testRule = row
      this.testData = {}
      this.testResult = null
      this.testFields = this.availableFields
      this.testDialogVisible = true
    },
    
    // 状态改变
    async handleStatusChange(row) {
      try {
        await budgetRuleApi.updateStatus(row.ruleId, row.ruleStatus)
        this.$message.success('状态更新成功')
        this.getList()
      } catch (error) {
        this.$message.error('状态更新失败：' + error.message)
        // 恢复原状态
        row.ruleStatus = row.ruleStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
      }
    },
    
    // 插入条件
    insertCondition(condition) {
      const textarea = this.$refs.conditionEditor.$refs.textarea
      const start = textarea.selectionStart
      const end = textarea.selectionEnd
      const text = this.ruleForm.ruleCondition
      
      this.ruleForm.ruleCondition = text.substring(0, start) + ' ' + condition + ' ' + text.substring(end)
      
      this.$nextTick(() => {
        textarea.focus()
        textarea.setSelectionRange(start + condition.length + 2, start + condition.length + 2)
      })
    },
    
    // 插入操作符
    insertOperator(operator) {
      const textarea = this.$refs.conditionEditor.$refs.textarea
      const start = textarea.selectionStart
      const end = textarea.selectionEnd
      const text = this.ruleForm.ruleCondition
      
      this.ruleForm.ruleCondition = text.substring(0, start) + ' ' + operator + ' ' + text.substring(end)
      
      this.$nextTick(() => {
        textarea.focus()
        textarea.setSelectionRange(start + operator.length + 2, start + operator.length + 2)
      })
    },
    
    // 插入字段
    insertField(field) {
      const textarea = this.$refs.conditionEditor.$refs.textarea
      const start = textarea.selectionStart
      const end = textarea.selectionEnd
      const text = this.ruleForm.ruleCondition
      
      this.ruleForm.ruleCondition = text.substring(0, start) + field.code + text.substring(end)
      
      this.$nextTick(() => {
        textarea.focus()
        textarea.setSelectionRange(start + field.code.length, start + field.code.length)
      })
    },
    
    // 使用示例
    useExample(example) {
      this.ruleForm.ruleCondition = example.condition
    },
    
    // 验证条件
    async handleValidateCondition() {
      if (!this.ruleForm.ruleCondition) {
        this.$message.warning('请输入规则条件')
        return
      }
      
      try {
        const response = await budgetRuleApi.validateCondition(this.ruleForm.ruleCondition)
        if (response.data.valid) {
          this.$message.success('条件验证通过')
        } else {
          this.$message.error('条件验证失败：' + response.data.message)
        }
      } catch (error) {
        this.$message.error('验证失败：' + error.message)
      }
    },
    
    // 动作改变
    handleActionChange(value) {
      // 根据动作类型设置参数提示
      const actionMap = {
        'APPROVE': '审批级别,审批人',
        'REJECT': '拒绝原因',
        'NOTIFY': '通知人员,通知模板',
        'CALCULATE': '计算公式',
        'REDIRECT': '目标节点'
      }
      this.ruleForm.actionParameters = actionMap[value] || ''
    },
    
    // 执行测试
    async handleExecuteTest() {
      try {
        const params = {
          ruleId: this.testRule.ruleId,
          testData: this.testData
        }
        const response = await budgetRuleApi.testRule(params)
        
        this.testResult = {
          title: response.data.matched ? '规则匹配' : '规则不匹配',
          type: response.data.matched ? 'success' : 'info',
          description: response.data.message
        }
      } catch (error) {
        this.testResult = {
          title: '测试失败',
          type: 'error',
          description: error.message
        }
      }
    },
    
    // 保存规则
    async handleSaveRule() {
      try {
        await this.$refs.ruleForm.validate()
        
        const params = {
          ruleId: this.ruleForm.ruleId,
          ruleName: this.ruleForm.ruleName,
          ruleType: this.ruleForm.ruleType,
          ruleCategory: (() => { const v = this.ruleForm.ruleCategory || this.ruleForm.categoryId || null; return Array.isArray(v) ? v.join(',') : v; })(),
          priority: this.ruleForm.priority,
          ruleDescription: this.ruleForm.ruleDescription,
          ruleCondition: this.ruleForm.ruleCondition,
          ruleAction: this.ruleForm.ruleAction,
          ruleExpression: this.ruleForm.ruleExpression || '',
          isEnabled: 0,
          effectiveDate: this.ruleForm.effectiveDate || this.ruleForm.effectiveStartTime || null,
          expiryDate: this.ruleForm.expiryDate || this.ruleForm.effectiveEndTime || null,
          remark: this.ruleForm.remark || ''
        }
        
        if (this.ruleForm.ruleId) {
          await budgetRuleApi.update(params)
          this.$message.success('保存成功')
        } else {
          await budgetRuleApi.create(params)
          this.$message.success('创建成功')
        }
        
        this.dialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },
    
    // 发布规则
    async handlePublishRule() {
      try {
        await this.$refs.ruleForm.validate()
        
        const params = {
          ruleId: this.ruleForm.ruleId,
          ruleName: this.ruleForm.ruleName,
          ruleType: this.ruleForm.ruleType,
          ruleCategory: (() => { const v = this.ruleForm.ruleCategory || this.ruleForm.categoryId || null; return Array.isArray(v) ? v.join(',') : v; })(),
          priority: this.ruleForm.priority,
          ruleDescription: this.ruleForm.ruleDescription,
          ruleCondition: this.ruleForm.ruleCondition,
          ruleAction: this.ruleForm.ruleAction,
          ruleExpression: this.ruleForm.ruleExpression || '',
          isEnabled: 1,
          effectiveDate: this.ruleForm.effectiveDate || this.ruleForm.effectiveStartTime || null,
          expiryDate: this.ruleForm.expiryDate || this.ruleForm.effectiveEndTime || null,
          remark: this.ruleForm.remark || ''
        }
        
        if (this.ruleForm.ruleId) {
          await budgetRuleApi.update(params)
          this.$message.success('发布成功')
        } else {
          await budgetRuleApi.create(params)
          this.$message.success('创建并发布成功')
        }
        
        this.dialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('发布失败：' + error.message)
      }
    },
    
    // 重置表单
    resetForm() {
      this.ruleForm = {
        ruleId: null,
        ruleName: '',
        ruleType: '',
        ruleCategory: null,
        priority: 5,
        ruleDescription: '',
        ruleCondition: '',
        ruleAction: '',
        ruleExpression: '',
        isEnabled: 1,
        effectiveDate: null,
        expiryDate: null,
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.ruleForm && this.$refs.ruleForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入规则
    handleImportRule() {
      this.$message.info('规则导入功能开发中...')
    },
    
    // 批量验证
    async handleBatchValidate() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要验证的规则')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.ruleId)
        await budgetRuleApi.batchValidate(ids)
        this.$message.success('批量验证完成')
        this.getList()
      } catch (error) {
        this.$message.error('批量验证失败：' + error.message)
      }
    },
    
    // 导出规则
    async handleExportRule() {
      try {
        const params = { ...this.queryForm }
        await budgetRuleApi.export(params)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
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
        case 'log':
          this.handleLog(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
      }
    },
    
    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算规则'
      this.dialogVisible = true
      this.ruleForm = { ...row, ruleId: null, ruleCode: null }
    },
    
    // 验证
    async handleValidate(row) {
      try {
        await budgetRuleApi.validate(row.ruleId)
        this.$message.success('验证通过')
        this.getList()
      } catch (error) {
        this.$message.error('验证失败：' + error.message)
      }
    },
    
    // 执行历史
    async handleHistory(row) {
      this.historyRow = row
      this.historyDialogVisible = true
      this.historyLoading = true
      try {
        this.historyList = [
          { operateTime: row.createTime, result: '成功', operator: '系统', remark: '规则创建' },
          { operateTime: row.updateTime || row.createTime, result: '成功', operator: '管理员', remark: '规则执行' }
        ]
      } finally {
        this.historyLoading = false
      }
    },

    // 执行日志
    async handleLog(row) {
      this.logRow = row
      this.logDialogVisible = true
      this.logLoading = true
      try {
        this.logList = [
          { logTime: row.createTime, logLevel: 'INFO', message: '规则初始化完成' },
          { logTime: row.updateTime || row.createTime, logLevel: 'INFO', message: '规则执行成功' }
        ]
      } finally {
        this.logLoading = false
      }
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        await budgetRuleApi.exportSingle(row.ruleId)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
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
    
    // 列设置
    handleColumnSetting() {
      this.$message.info('列设置功能开发中...')
    },
    
    // 判断是否可以编辑
    canEdit() {
      return true
    },
    
    // 获取规则类型颜色
    getRuleTypeColor(type) {
      const colorMap = {
        'VALIDATION': 'primary',
        'CALCULATION': 'success',
        'APPROVAL': 'warning',
        'NOTIFICATION': 'info',
        'WORKFLOW': 'danger'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取规则类型文本
    getRuleTypeText(type) {
      const item = this.ruleTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取优先级颜色
    getPriorityColor(priority) {
      if (priority <= 3) return 'danger'
      if (priority <= 6) return 'warning'
      return 'info'
    },
    
    // 获取优先级文本
    getPriorityText(priority) {
      const item = this.priorityOptions.find(opt => opt.value === priority)
      return item ? item.label : priority
    },
    
    // 获取动作颜色
    getActionColor(action) {
      const colorMap = {
        'APPROVE': 'success',
        'REJECT': 'danger',
        'NOTIFY': 'info',
        'CALCULATE': 'primary',
        'REDIRECT': 'warning'
      }
      return colorMap[action] || 'info'
    },
    
    // 获取动作文本
    getActionText(action) {
      const item = this.ruleActionOptions.find(opt => opt.value === action)
      return item ? item.label : action
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-rule {
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
  .search-card,
  .table-card {
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
  
  .search-card {
    .rule-categories {
      border-right: 1px solid #EBEEF5;
      padding-right: 20px;
      
      h4 {
        color: #303133;
        margin: 0 0 15px 0;
      }
      
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
  }
  
  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    
    .table-title {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }
    
    .table-tools {
      display: flex;
      gap: 8px;
    }
  }
  
  .rule-code {
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
  
  .rule-condition-editor {
    .condition-toolbar {
      margin-bottom: 10px;
      padding: 10px;
      background-color: #f5f5f5;
      border-radius: 4px;
    }
    
    .condition-textarea {
      font-family: 'Courier New', monospace;
    }
    
    .condition-help {
      margin-top: 10px;
      
      .field-list {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
        
        .field-tag {
          cursor: pointer;
          
          &:hover {
            background-color: #409EFF;
            color: white;
          }
        }
      }
      
      .example-list {
        .example-item {
          padding: 8px;
          border: 1px solid #EBEEF5;
          border-radius: 4px;
          margin-bottom: 8px;
          cursor: pointer;
          
          &:hover {
            background-color: #F0F9FF;
            border-color: #409EFF;
          }
          
          .example-title {
            font-weight: 500;
            color: #303133;
            margin-bottom: 4px;
          }
          
          .example-code {
            font-family: 'Courier New', monospace;
            font-size: 12px;
            color: #909399;
          }
        }
      }
    }
  }
  
  .rule-test {
    .test-rule {
      margin-bottom: 20px;
      
      h4 {
        color: #303133;
        margin: 0 0 10px 0;
      }
      
      .rule-info {
        background-color: #f5f5f5;
        padding: 15px;
        border-radius: 4px;
        
        div {
          margin-bottom: 8px;
          
          &:last-child {
            margin-bottom: 0;
          }
        }
        
        code {
          background-color: #e6f7ff;
          padding: 2px 4px;
          border-radius: 3px;
          font-family: 'Courier New', monospace;
        }
      }
    }
    
    .test-data {
      margin-bottom: 20px;
      
      h4 {
        color: #303133;
        margin: 0 0 15px 0;
      }
    }
    
    .test-result {
      h4 {
        color: #303133;
        margin: 0 0 10px 0;
      }
      
      .no-result {
        text-align: center;
        color: #909399;
        padding: 20px;
        background-color: #f5f5f5;
        border-radius: 4px;
      }
    }
  }
  
  .number-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #409EFF;
  }
  
  .primary-text {
    color: #409EFF;
  }
  
  .text-right {
    text-align: right;
  }
}
</style>
