<template>
  <div class="budget-control">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算控制管理</h2>
      <p>配置和管理预算控制规则，实现预算执行的有效控制</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateRule">创建规则</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportRules">导入规则</el-button>
            <el-button type="warning" icon="el-icon-check" @click="handleBatchEnable">批量启用</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportRules">导出规则</el-button>
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

    <!-- 控制规则统计 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ controlStats.totalRules }}</div>
            <div class="stat-label">控制规则</div>
            <div class="stat-description">已配置的控制规则总数</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-check"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ controlStats.activeRules }}</div>
            <div class="stat-label">生效规则</div>
            <div class="stat-description">当前生效的控制规则</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="controlStats.activeRate" 
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
        <el-card class="stat-card triggered-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ controlStats.triggeredCount }}</div>
            <div class="stat-label">触发次数</div>
            <div class="stat-description">本月规则触发总次数</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="controlStats.triggerRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#E6A23C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card blocked-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ controlStats.blockedCount }}</div>
            <div class="stat-label">阻止次数</div>
            <div class="stat-description">成功阻止的违规操作</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="controlStats.blockRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#F56C6C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-circle-close"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 统计图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card" shadow="hover">
          <div slot="header" class="chart-header">
            <span>规则类型分布</span>
          </div>
          <div ref="typeChart" style="width: 100%; min-width: 200px; height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="hover">
          <div slot="header" class="chart-header">
            <span>控制概览统计</span>
          </div>
          <div ref="overviewChart" style="width: 100%; min-width: 200px; height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 控制健康度评估 -->
    <el-card class="health-card" shadow="hover">
      <div slot="header" class="health-header">
        <span>控制健康度评估</span>
        <el-button type="text" icon="el-icon-refresh" @click="loadHealthData">刷新</el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="health-score">
            <div class="health-score-wrap">
              <el-progress type="dashboard" :percentage="healthData.overallScore" :width="120" :color="getHealthColor(healthData.overallScore)" />
              <div class="health-score-overlay">
                <span class="health-score-text">{{ healthData.overallScore }}</span>
                <span class="health-score-label">总评分</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="18">
          <el-row :gutter="16">
            <el-col :span="6" v-for="metric in healthData.metrics" :key="metric.key">
              <div class="health-metric">
                <div class="metric-label">{{ metric.label }}</div>
                <div class="metric-value">
                  <span class="metric-number">{{ metric.value }}</span>
                  <span class="metric-unit">{{ metric.unit }}</span>
                </div>
                <el-progress :percentage="metric.value" :stroke-width="6" :show-text="false" :color="getMetricColor(metric.status)" />
                <div class="metric-status">
                  <el-tag :type="getMetricTagType(metric.status)" size="mini">{{ getMetricStatusText(metric.status) }}</el-tag>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-card>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
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
        <el-form-item label="控制级别">
          <el-select
            v-model="queryForm.controlLevel"
            placeholder="请选择控制级别"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in controlLevelOptions"
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
            style="width: 120px"
          >
            <el-option
              v-for="item in ruleStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建人">
          <el-input
            v-model="queryForm.creator"
            placeholder="请输入创建人"
            clearable
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 控制规则列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算控制规则列表</span>
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
        :data="controlRuleList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column v-if="columnVisible.ruleCode" prop="ruleCode" label="规则编码" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.ruleName" prop="ruleName" label="规则名称" min-width="200" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.ruleType" prop="ruleType" label="规则类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getRuleTypeColor(scope.row.ruleType)">
              {{ getRuleTypeText(scope.row.ruleType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.controlLevel" prop="controlLevel" label="控制级别" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getControlLevelColor(scope.row.controlLevel)">
              {{ getControlLevelText(scope.row.controlLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.controlAction" prop="controlAction" label="控制动作" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getControlActionColor(scope.row.controlAction)">
              {{ getControlActionText(scope.row.controlAction) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.priority" prop="priority" label="优先级" width="80" align="center" sortable="custom">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getPriorityColor(scope.row.priority)">
              {{ scope.row.priority }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.triggerCount" prop="triggerCount" label="触发次数" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <span class="number-text">{{ scope.row.triggerCount || 0 }}</span>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.successRate" prop="successRate" label="成功率" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.successRate || 0"
              :stroke-width="6"
              :text-inside="true"
              :color="getSuccessRateColor(scope.row.successRate)"
            />
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.isEnabled" prop="isEnabled" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.lastTriggerTime" prop="lastTriggerTime" label="最后触发" width="150" align="center" />
        
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >查看</el-button>
            <el-button
              v-if="canEdit(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-s-data"
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
                <el-dropdown-item command="history" icon="el-icon-time">触发历史</el-dropdown-item>
                <el-dropdown-item command="log" icon="el-icon-document">执行日志</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
                <el-dropdown-item command="delete" icon="el-icon-delete" divided>删除</el-dropdown-item>
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

    <!-- 新增/编辑控制规则对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="controlRuleForm"
        :model="controlRuleForm"
        :rules="controlRuleRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input
                v-model="controlRuleForm.ruleName"
                placeholder="请输入规则名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则编码" prop="ruleCode">
              <el-input
                v-model="controlRuleForm.ruleCode"
                placeholder="请输入规则编码"
                :disabled="!!controlRuleForm.id"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则类型" prop="ruleType">
              <el-select
                v-model="controlRuleForm.ruleType"
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
          <el-col :span="12">
            <el-form-item label="控制级别" prop="controlLevel">
              <el-select
                v-model="controlRuleForm.controlLevel"
                placeholder="请选择控制级别"
                style="width: 100%"
              >
                <el-option
                  v-for="item in controlLevelOptions"
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
            <el-form-item label="控制动作" prop="controlAction">
              <el-select
                v-model="controlRuleForm.controlAction"
                placeholder="请选择控制动作"
                style="width: 100%"
              >
                <el-option
                  v-for="item in controlActionOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-input-number
                v-model="controlRuleForm.priority"
                :min="1"
                :max="100"
                placeholder="请输入优先级"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="规则描述" prop="ruleDescription">
          <el-input
            v-model="controlRuleForm.ruleDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入规则描述"
          />
        </el-form-item>
        
        <!-- 控制条件配置 -->
        <el-form-item label="控制条件" prop="controlConditions">
          <div class="control-conditions">
            <div class="conditions-header">
              <el-button type="primary" size="mini" @click="handleAddCondition">添加条件</el-button>
              <el-button type="success" size="mini" @click="handleImportConditions">导入条件</el-button>
            </div>
            
            <el-table
              :data="controlRuleForm.controlConditions"
              border
              size="mini"
              max-height="300"
            >
              <el-table-column type="index" label="序号" width="60" align="center" />
              
              <el-table-column label="条件字段" width="150">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.conditionField"
                    placeholder="条件字段"
                    size="mini"
                    style="width: 100%"
                  >
                    <el-option value="BUDGET_AMOUNT" label="预算金额" />
                    <el-option value="ACTUAL_AMOUNT" label="实际金额" />
                    <el-option value="EXECUTION_RATE" label="执行率" />
                    <el-option value="VARIANCE_RATE" label="差异率" />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="比较操作符" width="120">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.operator"
                    placeholder="操作符"
                    size="mini"
                    style="width: 100%"
                  >
                    <el-option value="GT" label="大于" />
                    <el-option value="GTE" label="大于等于" />
                    <el-option value="LT" label="小于" />
                    <el-option value="LTE" label="小于等于" />
                    <el-option value="EQ" label="等于" />
                    <el-option value="NEQ" label="不等于" />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="条件值" width="120">
                <template slot-scope="scope">
                  <el-input-number
                    v-model="scope.row.conditionValue"
                    :precision="2"
                    size="mini"
                    style="width: 100%"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="逻辑关系" width="100">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.logicOperator"
                    placeholder="逻辑关系"
                    size="mini"
                    style="width: 100%"
                    :disabled="scope.$index === controlRuleForm.controlConditions.length - 1"
                  >
                    <el-option value="AND" label="并且" />
                    <el-option value="OR" label="或者" />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    class="danger-text"
                    @click="handleRemoveCondition(scope.$index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form-item>
        
        <!-- 控制动作配置 -->
        <el-form-item label="动作配置">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="通知方式" prop="notificationMethod">
                <el-checkbox-group v-model="controlRuleForm.notificationMethods">
                  <el-checkbox label="EMAIL">邮件</el-checkbox>
                  <el-checkbox label="SMS">短信</el-checkbox>
                  <el-checkbox label="SYSTEM">系统通知</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="通知人员" prop="notificationUsers">
                <el-select
                  v-model="controlRuleForm.notificationUsers"
                  placeholder="请选择通知人员"
                  multiple
                  style="width: 100%"
                >
                  <el-option
                    v-for="user in userOptions"
                    :key="user.id"
                    :label="user.name"
                    :value="user.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>
        
        <el-form-item label="规则配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="controlRuleForm.isEnabled">启用规则</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="controlRuleForm.allowOverride">允许覆盖</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="controlRuleForm.logExecution">记录执行日志</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleTestRule">测试规则</el-button>
        <el-button type="primary" @click="handleSubmitForm">保存规则</el-button>
      </div>
    </el-dialog>

    <!-- 规则测试对话框 -->
    <el-dialog
      title="规则测试"
      :visible.sync="testDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="rule-test">
        <el-form :model="testForm" label-width="120px" size="small">
          <el-form-item label="测试数据">
            <el-input
              v-model="testForm.testData"
              type="textarea"
              :rows="4"
              placeholder="请输入测试数据（JSON格式）"
            />
          </el-form-item>
        </el-form>
        
        <div class="test-result" v-if="testResult">
          <h4>测试结果</h4>
          <el-alert
            :title="testResult.title"
            :type="testResult.type"
            :description="testResult.description"
            show-icon
          />
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleExecuteTest">执行测试</el-button>
      </div>
    </el-dialog>

    <!-- 列设置对话框 -->
    <el-dialog title="列设置" :visible.sync="columnSettingVisible" width="400px">
      <el-checkbox-group v-model="columnChecked">
        <el-row :gutter="10">
          <el-col v-for="col in columnOptions" :key="col.key" :span="12">
            <el-checkbox :label="col.key" style="margin-bottom: 8px;">{{ col.label }}</el-checkbox>
          </el-col>
        </el-row>
      </el-checkbox-group>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleResetColumns">重置</el-button>
        <el-button @click="columnSettingVisible = false">取消</el-button>
        <el-button type="primary" @click="handleApplyColumns">确定</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog title="导入控制规则" :visible.sync="importDialogVisible" width="500px">
      <el-upload
        drag
        action="#"
        :auto-upload="false"
        :on-change="handleImportFileChange"
        :limit="1"
        accept=".xlsx,.xls"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">仅支持 .xlsx / .xls 格式文件</div>
      </el-upload>
      <div slot="footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImportConfirm">确认导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetControlApi } from '@/api/managementAccountant/ncv65/budgetControl'
import * as echarts from 'echarts'

export default {
  name: 'BudgetControl',
  data() {
    return {
      // 查询参数
      queryForm: {
        ruleName: '',
        ruleType: '',
        controlLevel: '',
        ruleStatus: '',
        creator: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      controlRuleList: [],
      total: 0,
      selectedRows: [],
      
      // 统计数据
      controlStats: {
        totalRules: 0,
        activeRules: 0,
        triggeredCount: 0,
        blockedCount: 0,
        activeRate: 0,
        triggerRate: 0,
        blockRate: 0
      },
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      testDialogVisible: false,
      importDialogVisible: false,
      importFile: null,
      controlRuleForm: {
        id: null,
        ruleName: '',
        ruleCode: '',
        ruleType: '',
        controlLevel: '',
        controlAction: '',
        priority: 50,
        ruleDescription: '',
        controlConditions: [],
        notificationMethods: [],
        notificationUsers: [],
        isEnabled: true,
        allowOverride: false,
        logExecution: true
      },
      controlRuleRules: {
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' }
        ],
        ruleCode: [
          { required: true, message: '请输入规则编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '规则编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        ruleType: [
          { required: true, message: '请选择规则类型', trigger: 'change' }
        ],
        controlLevel: [
          { required: true, message: '请选择控制级别', trigger: 'change' }
        ],
        controlAction: [
          { required: true, message: '请选择控制动作', trigger: 'change' }
        ]
      },
      
      // 规则测试
      testForm: {
        testData: ''
      },
      testResult: null,
      
      // 选项数据
      ruleTypeOptions: [
        { value: 'BUDGET_LIMIT', label: '预算限额' },
        { value: 'EXECUTION_RATE', label: '执行率控制' },
        { value: 'VARIANCE_CONTROL', label: '差异控制' },
        { value: 'APPROVAL_CONTROL', label: '审批控制' },
        { value: 'TIME_CONTROL', label: '时间控制' },
        { value: 'CUSTOM', label: '自定义规则' }
      ],
      controlLevelOptions: [
        { value: 'STRICT', label: '严格控制' },
        { value: 'WARNING', label: '预警提示' },
        { value: 'SOFT', label: '软控制' },
        { value: 'MONITOR', label: '仅监控' }
      ],
      controlActionOptions: [
        { value: 'BLOCK', label: '阻止执行' },
        { value: 'WARNING', label: '警告提示' },
        { value: 'APPROVAL', label: '需要审批' },
        { value: 'NOTIFICATION', label: '发送通知' },
        { value: 'LOG', label: '记录日志' }
      ],
      ruleStatusOptions: [
        { value: 'ACTIVE', label: '启用' },
        { value: 'INACTIVE', label: '停用' }
      ],
      userOptions: [],

      // 列设置
      columnSettingVisible: false,
      columnOptions: [
        { key: 'ruleCode', label: '规则编码' },
        { key: 'ruleName', label: '规则名称' },
        { key: 'ruleType', label: '规则类型' },
        { key: 'controlLevel', label: '控制级别' },
        { key: 'controlAction', label: '控制动作' },
        { key: 'priority', label: '优先级' },
        { key: 'triggerCount', label: '触发次数' },
        { key: 'successRate', label: '成功率' },
        { key: 'isEnabled', label: '状态' },
        { key: 'lastTriggerTime', label: '最后触发' }
      ],
      columnChecked: ['ruleCode', 'ruleName', 'ruleType', 'controlLevel', 'controlAction', 'priority', 'triggerCount', 'successRate', 'isEnabled', 'lastTriggerTime'],
      columnVisible: {
        ruleCode: true, ruleName: true, ruleType: true,
        controlLevel: true, controlAction: true, priority: true,
        triggerCount: true, successRate: true, isEnabled: true,
        lastTriggerTime: true
      },

      // 健康度数据
      healthData: {
        overallScore: 0,
        metrics: []
      },

      // 图表实例
      typeChartInstance: null,
      overviewChartInstance: null
    }
  },

  created() {
    this.getList()
    this.loadStats()
    this.loadUserOptions()
    this.loadHealthData()
  },

  mounted() {
    this.$nextTick(() => {
      setTimeout(() => {
        this.initCharts()
      }, 300)
    })
    this._resizeHandler = () => {
      this.typeChartInstance && this.typeChartInstance.resize()
      this.overviewChartInstance && this.overviewChartInstance.resize()
    }
    window.addEventListener('resize', this._resizeHandler)
  },

  beforeDestroy() {
    if (this._resizeHandler) {
      window.removeEventListener('resize', this._resizeHandler)
    }
    if (this.typeChartInstance) {
      this.typeChartInstance.dispose()
      this.typeChartInstance = null
    }
    if (this.overviewChartInstance) {
      this.overviewChartInstance.dispose()
      this.overviewChartInstance = null
    }
  },

  methods: {
    // 加载统计数据
    async loadStats() {
      try {
        const response = await budgetControlApi.getStatistics()
        if (response.code === 1 && response.data) {
          const d = response.data
          const total = d.totalCount || 0
          const enabled = d.enabledCount || 0
          this.controlStats = {
            totalRules: total,
            activeRules: enabled,
            triggeredCount: d.triggeredCount || 0,
            blockedCount: d.blockedCount || 0,
            activeRate: total > 0 ? Math.min(100, Math.round((enabled / total) * 100)) : 0,
            triggerRate: Math.min(100, Math.round(parseFloat(d.triggerRate) || 0)),
            blockRate: Math.min(100, Math.round(parseFloat(d.blockRate) || 0))
          }
        }
      } catch (error) {
        console.error('加载统计数据失败', error)
      }
    },

    // 初始化图表（分开 try/catch，一个失败不影响另一个）
    async initCharts() {
      // 饼图：规则类型分布
      try {
        const response = await budgetControlApi.getStatistics()
        const typeData = (response && response.data && response.data.typeStatistics) ? response.data.typeStatistics : []
        this.renderTypeChart(typeData)
      } catch (error) {
        console.error('加载规则类型分布图失败', error)
        this.renderTypeChart([])
      }

      // 柱状图：控制概览统计
      try {
        const statsResponse = await budgetControlApi.getControlStats()
        const statsData = (statsResponse && statsResponse.data) ? statsResponse.data : {}
        this.renderOverviewChart(statsData)
      } catch (error) {
        console.error('加载控制概览统计图失败', error)
        this.renderOverviewChart({})
      }

      // 初始化完成后触发一次 resize，确保图表尺寸正确
      this.$nextTick(() => {
        this.typeChartInstance && this.typeChartInstance.resize()
        this.overviewChartInstance && this.overviewChartInstance.resize()
      })
    },

    // 渲染规则类型分布饼图
    renderTypeChart(typeStatistics) {
      const chartDom = this.$refs.typeChart
      if (!chartDom) return
      // 如果已有实例先销毁，防止重复初始化报错
      const existing = echarts.getInstanceByDom(chartDom)
      if (existing) existing.dispose()
      this.typeChartInstance = echarts.init(chartDom)

      const typeNameMap = {
        'BUDGET_LIMIT': '预算限额',
        'EXECUTION_RATE': '执行率控制',
        'VARIANCE_CONTROL': '差异控制',
        'APPROVAL_CONTROL': '审批控制',
        'TIME_CONTROL': '时间控制',
        'CUSTOM': '自定义规则'
      }
      const data = typeStatistics.map(item => ({
        name: typeNameMap[item.CONTROL_TYPE] || item.CONTROL_TYPE || '未知',
        value: item.count || item.COUNT || 0
      }))

      const option = {
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { orient: 'vertical', right: 10, top: 'center' },
        color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#00BFFF'],
        series: [{
          name: '规则类型',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
          label: { show: false },
          emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
          data: data.length > 0 ? data : [{ name: '暂无数据', value: 0 }]
        }]
      }
      this.typeChartInstance.setOption(option)
    },

    // 渲染控制概览柱状图
    renderOverviewChart(stats) {
      const chartDom = this.$refs.overviewChart
      if (!chartDom) return
      // 如果已有实例先销毁，防止重复初始化报错
      const existing = echarts.getInstanceByDom(chartDom)
      if (existing) existing.dispose()
      this.overviewChartInstance = echarts.init(chartDom)

      const option = {
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          data: ['控制规则', '启用规则', '预警记录', '待处理预警', '执行总数', '冻结预算', '预算限额'],
          axisLabel: { rotate: 15, fontSize: 11 }
        },
        yAxis: { type: 'value' },
        series: [{
          name: '数量',
          type: 'bar',
          barWidth: '50%',
          itemStyle: {
            borderRadius: [4, 4, 0, 0],
            color: function(params) {
              const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#00BFFF', '#FF6B81']
              return colors[params.dataIndex % colors.length]
            }
          },
          data: [
            stats.totalRules || 0,
            stats.activeRules || 0,
            stats.totalWarnings || 0,
            stats.pendingWarnings || 0,
            stats.totalExecutions || 0,
            stats.frozenBudgets || 0,
            stats.budgetLimits || 0
          ]
        }]
      }
      this.overviewChartInstance.setOption(option)
    },

    // 加载健康度数据
    async loadHealthData() {
      try {
        const response = await budgetControlApi.getControlHealth()
        if (response && response.data) {
          const d = response.data
          // el-progress percentage 必须是 0-100 的整数
          const score = Math.round(parseFloat(d.overallScore) || 0)
          const metrics = (d.metrics || []).map(m => ({
            key: m.key,
            label: m.label,
            value: Math.min(100, Math.max(0, Math.round(parseFloat(m.value) || 0))),
            unit: m.unit || '%',
            status: m.status || 'good'
          }))
          this.healthData = { overallScore: score, metrics }
        }
      } catch (error) {
        console.error('加载健康度数据失败', error)
      }
    },

    // 健康度颜色
    getHealthColor(score) {
      if (score >= 80) return '#67C23A'
      if (score >= 60) return '#E6A23C'
      return '#F56C6C'
    },

    // 指标颜色
    getMetricColor(status) {
      const map = { good: '#67C23A', warning: '#E6A23C', danger: '#F56C6C' }
      return map[status] || '#409EFF'
    },

    // 指标标签类型
    getMetricTagType(status) {
      const map = { good: 'success', warning: 'warning', danger: 'danger' }
      return map[status] || 'info'
    },

    // 指标状态文本
    getMetricStatusText(status) {
      const map = { good: '良好', warning: '警告', danger: '异常' }
      return map[status] || '未知'
    },

    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.queryParams
        }
        const response = await budgetControlApi.getPage(params)
        if (response.code === 1) {
          this.controlRuleList = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载用户选项
    async loadUserOptions() {
      try {
        const response = await budgetControlApi.getUsers()
        if (response.code === 1) {
          this.userOptions = response.data
        }
      } catch (error) {
        console.error('加载用户选项失败：', error)
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
        controlLevel: '',
        ruleStatus: '',
        creator: ''
      }
      this.handleQuery()
    },
    
    // 创建规则
    handleCreateRule() {
      this.dialogTitle = '创建控制规则'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑规则
    handleEdit(row) {
      this.dialogTitle = '编辑控制规则'
      this.dialogVisible = true
      this.controlRuleForm = { ...row }
    },

    // 查看规则
    handleView(row) {
      this.$router.push({
        path: '/qmys/BudgetControl',
        query: { id: row.ruleId }
      })
    },
    
    // 测试规则
    handleTest(row) {
      this.testDialogVisible = true
      this.testForm.testData = JSON.stringify({
        budgetAmount: 10000,
        actualAmount: 8500,
        executionRate: 85,
        varianceRate: -15
      }, null, 2)
      this.testResult = null
    },
    
    // 状态改变
    async handleStatusChange(row) {
      try {
        let response
        if (row.isEnabled) {
          response = await budgetControlApi.enable(row.ruleId)
        } else {
          response = await budgetControlApi.disable(row.ruleId)
        }
        if (response.code === 1) {
          this.$message.success('状态更新成功')
          this.getList()
        }
      } catch (error) {
        this.$message.error('状态更新失败：' + error.message)
        // 恢复原状态
        row.isEnabled = !row.isEnabled
      }
    },
    
    // 添加条件
    handleAddCondition() {
      this.controlRuleForm.controlConditions.push({
        conditionField: '',
        operator: 'GT',
        conditionValue: 0,
        logicOperator: 'AND'
      })
    },
    
    // 删除条件
    handleRemoveCondition(index) {
      this.controlRuleForm.controlConditions.splice(index, 1)
    },
    
    // 导入条件
    handleImportConditions() {
      this.$message.info('条件导入功能开发中...')
    },
    
    // 测试规则
    handleTestRule() {
      this.testDialogVisible = true
      this.testForm.testData = JSON.stringify({
        budgetAmount: 10000,
        actualAmount: 8500,
        executionRate: 85,
        varianceRate: -15
      }, null, 2)
      this.testResult = null
    },
    
    // 执行测试
    async handleExecuteTest() {
      try {
        const testData = JSON.parse(this.testForm.testData)
        const params = {
          ruleId: this.controlRuleForm.ruleId,
          testData: testData
        }
        const response = await budgetControlApi.validate(params)
        if (response.code === 1) {
          this.testResult = {
            title: response.data.passed ? '规则测试通过' : '规则测试失败',
            type: response.data.passed ? 'success' : 'error',
            description: response.data.message || JSON.stringify(response.data)
          }
        }
      } catch (error) {
        this.testResult = {
          title: '测试执行失败',
          type: 'error',
          description: error.message
        }
      }
    },
    
    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.controlRuleForm.validate()

        const params = { ...this.controlRuleForm }
        let response

        if (this.controlRuleForm.ruleId) {
          response = await budgetControlApi.update(this.controlRuleForm.ruleId, params)
        } else {
          response = await budgetControlApi.create(params)
        }

        if (response.code === 1) {
          this.$message.success(this.controlRuleForm.ruleId ? '更新成功' : '创建成功')
          this.dialogVisible = false
          this.getList()
        }
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },
    
    // 重置表单
    resetForm() {
      this.controlRuleForm = {
        ruleId: null,
        ruleName: '',
        ruleCode: '',
        controlType: '',
        controlObject: '',
        controlLevel: 5,
        actionType: '',
        thresholdValue: 0,
        thresholdType: 'PERCENTAGE',
        ruleDescription: '',
        controlConditions: [],
        notificationMethods: [],
        notificationUsers: [],
        isEnabled: true,
        allowOverride: false,
        logExecution: true
      }
      this.$nextTick(() => {
        this.$refs.controlRuleForm && this.$refs.controlRuleForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入规则
    handleImportRules() {
      this.importDialogVisible = true
    },

    // 上传文件改变
    handleImportFileChange(file) {
      this.importFile = file.raw
    },

    // 确认导入
    async handleImportConfirm() {
      if (!this.importFile) {
        this.$message.warning('请选择要导入的文件')
        return
      }
      try {
        const formData = new FormData()
        formData.append('file', this.importFile)
        await budgetControlApi.importRules ? budgetControlApi.importRules(formData) : this.$message.info('后端导入接口开发中')
        this.$message.success('导入成功')
        this.importDialogVisible = false
        this.importFile = null
        this.getList()
        this.loadStats()
        this.initCharts()
      } catch (error) {
        this.$message.error('导入失败：' + error.message)
      }
    },
    
    // 批量启用
    async handleBatchEnable() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要启用的规则')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.ruleId)
        for (const id of ids) {
          await budgetControlApi.enable(id)
        }
        this.$message.success('批量启用成功')
        this.getList()
      } catch (error) {
        this.$message.error('批量启用失败：' + error.message)
      }
    },
    
    // 导出规则
    async handleExportRules() {
      try {
        const params = { ...this.queryForm }
        const response = await budgetControlApi.export(params)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算控制规则_' + new Date().getTime() + '.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        URL.revokeObjectURL(url)
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
      this.$router.push('/qmys/BudgetControl')
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopy(row)
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
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    handleCopy(row) {
      this.dialogTitle = '复制控制规则'
      this.dialogVisible = true
      this.controlRuleForm = { ...row, id: null, ruleCode: null }
    },
    
    // 触发历史
    handleHistory(row) {
      this.$router.push({
        path: '/qmys/BudgetAlert',
        query: { ruleId: row.ruleId }
      })
    },

    // 执行日志
    handleLog(row) {
      this.$router.push({
        path: '/qmys/BudgetExecution',
        query: { ruleId: row.ruleId }
      })
    },

    // 导出单个
    async handleExportSingle(row) {
      try {
        const response = await budgetControlApi.exportSingle(row.ruleId)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '控制规则_' + (row.ruleName || row.ruleId) + '.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该控制规则？删除后不可恢复', '提示', {
          type: 'warning'
        })
        await budgetControlApi.delete(row.ruleId)
        this.$message.success('删除成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
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
      this.columnChecked = Object.keys(this.columnVisible).filter(k => this.columnVisible[k])
      this.columnSettingVisible = true
    },
    handleApplyColumns() {
      this.columnOptions.forEach(col => {
        this.$set(this.columnVisible, col.key, this.columnChecked.includes(col.key))
      })
      this.columnSettingVisible = false
    },
    handleResetColumns() {
      this.columnChecked = this.columnOptions.map(col => col.key)
    },
    
    // 判断是否可以编辑
    canEdit(row) {
      return row.creator === this.$store.getters.name || this.$store.getters.roles.includes('admin')
    },
    
    // 获取规则类型颜色
    getRuleTypeColor(type) {
      const colorMap = {
        'BUDGET_LIMIT': 'danger',
        'EXECUTION_RATE': 'warning',
        'VARIANCE_CONTROL': 'primary',
        'APPROVAL_CONTROL': 'success',
        'TIME_CONTROL': 'info',
        'CUSTOM': 'primary'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取规则类型文本
    getRuleTypeText(type) {
      const item = this.ruleTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取控制级别颜色
    getControlLevelColor(level) {
      const colorMap = {
        'STRICT': 'danger',
        'WARNING': 'warning',
        'SOFT': 'primary',
        'MONITOR': 'info'
      }
      return colorMap[level] || 'info'
    },
    
    // 获取控制级别文本
    getControlLevelText(level) {
      const item = this.controlLevelOptions.find(opt => opt.value === level)
      return item ? item.label : level
    },
    
    // 获取控制动作颜色
    getControlActionColor(action) {
      const colorMap = {
        'BLOCK': 'danger',
        'WARNING': 'warning',
        'APPROVAL': 'primary',
        'NOTIFICATION': 'success',
        'LOG': 'info'
      }
      return colorMap[action] || 'info'
    },
    
    // 获取控制动作文本
    getControlActionText(action) {
      const item = this.controlActionOptions.find(opt => opt.value === action)
      return item ? item.label : action
    },
    
    // 获取优先级颜色
    getPriorityColor(priority) {
      if (priority >= 80) return 'danger'
      if (priority >= 60) return 'warning'
      if (priority >= 40) return 'primary'
      return 'info'
    },
    
    // 获取成功率颜色
    getSuccessRateColor(rate) {
      if (rate >= 90) return '#67C23A'
      if (rate >= 70) return '#E6A23C'
      return '#F56C6C'
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-control {
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
  .table-card,
  .chart-card,
  .health-card {
    margin-bottom: 20px;
  }

  .chart-row {
    margin-bottom: 20px;

    .chart-header {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }
  }

  .health-card {
    .health-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }

    .health-score {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100%;

      .health-score-wrap {
        position: relative;
        display: inline-block;

        .health-score-overlay {
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          text-align: center;
          pointer-events: none;

          .health-score-text {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            display: block;
            line-height: 1.2;
          }

          .health-score-label {
            font-size: 11px;
            color: #909399;
            display: block;
          }
        }
      }
    }

    .health-metric {
      text-align: center;
      padding: 12px 0;

      .metric-label {
        font-size: 13px;
        color: #606266;
        margin-bottom: 8px;
      }

      .metric-value {
        margin-bottom: 8px;

        .metric-number {
          font-size: 24px;
          font-weight: 600;
          color: #303133;
        }

        .metric-unit {
          font-size: 12px;
          color: #909399;
          margin-left: 2px;
        }
      }

      .metric-status {
        margin-top: 8px;
      }
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
      
      &.triggered-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.blocked-card {
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
  
  .number-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #409EFF;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .control-conditions {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    
    .conditions-header {
      padding: 12px;
      background-color: #F5F7FA;
      border-bottom: 1px solid #EBEEF5;
      display: flex;
      gap: 8px;
    }
  }
  
  .rule-test {
    .test-result {
      margin-top: 20px;
      
      h4 {
        color: #303133;
        font-size: 14px;
        margin: 0 0 10px 0;
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
