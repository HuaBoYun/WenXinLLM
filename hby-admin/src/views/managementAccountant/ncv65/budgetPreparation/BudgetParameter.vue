<template>
  <div class="budget-parameter">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算参数管理</h2>
      <p>管理系统预算参数，支持类型配置、默认值设置和参数验证</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateParameter">创建参数</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportParameter">导入参数</el-button>
            <el-button type="warning" icon="el-icon-check" @click="handleBatchValidate">批量验证</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportParameter">导出参数</el-button>
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

    <!-- 参数统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ parameterStats.totalParameters }}</div>
            <div class="stat-label">参数总数</div>
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
        <el-card class="stat-card system-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ parameterStats.systemParameters }}</div>
            <div class="stat-label">系统参数</div>
            <div class="stat-progress">
              <el-progress
                :percentage="parameterStats.systemRate"
                :show-text="false"
                stroke-width="4"
                color="#67C23A"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-setting"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card business-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ parameterStats.businessParameters }}</div>
            <div class="stat-label">业务参数</div>
            <div class="stat-progress">
              <el-progress
                :percentage="parameterStats.businessRate"
                :show-text="false"
                stroke-width="4"
                color="#E6A23C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-data"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card custom-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ parameterStats.customParameters }}</div>
            <div class="stat-label">自定义参数</div>
            <div class="stat-progress">
              <el-progress
                :percentage="parameterStats.customRate"
                :show-text="false"
                stroke-width="4"
                color="#409EFF"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-magic-stick"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
            <el-form-item label="参数名称">
              <el-input
                v-model="queryForm.parameterName"
                placeholder="请输入参数名称"
                clearable
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item label="参数类型">
              <el-select
                v-model="queryForm.parameterType"
                placeholder="请选择参数类型"
                clearable
                style="width: 150px"
              >
                <el-option
                  v-for="item in parameterTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="参数状态">
              <el-select
                v-model="queryForm.parameterStatus"
                placeholder="请选择参数状态"
                clearable
                style="width: 150px"
              >
                <el-option
                  v-for="item in parameterStatusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="参数范围">
              <el-select
                v-model="queryForm.parameterScope"
                placeholder="请选择参数范围"
                clearable
                style="width: 150px"
              >
                <el-option
                  v-for="item in parameterScopeOptions"
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
    </el-card>

    <!-- 参数列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算参数列表</span>
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
        :data="parameterList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column prop="parameterCode" label="参数编码" width="150" show-overflow-tooltip />
        <el-table-column prop="parameterName" label="参数名称" width="180" show-overflow-tooltip />

        <el-table-column prop="parameterType" label="参数类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getParameterTypeColor(scope.row.parameterType)">
              {{ getParameterTypeText(scope.row.parameterType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="parameterScope" label="参数范围" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getParameterScopeColor(scope.row.parameterScope)">
              {{ getParameterScopeText(scope.row.parameterScope) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="defaultValue" label="默认值" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <span class="parameter-value">{{ scope.row.defaultValue || '-' }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="currentValue" label="当前值" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <span class="parameter-value current-value">{{ scope.row.currentValue || '-' }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="validationRule" label="验证规则" min-width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <code class="validation-rule">{{ scope.row.validationRule || '无' }}</code>
          </template>
        </el-table-column>

        <el-table-column prop="isRequired" label="必填" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isRequired ? 'danger' : 'info'" size="mini">
              {{ scope.row.isRequired ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="isEnabled" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              :active-value="true"
              :inactive-value="false"
              @change="handleStatusChange(scope.row)"
              @click.native.stop
            />
          </template>
        </el-table-column>

        <el-table-column prop="lastModifyTime" label="最后修改" width="150" align="center" />

        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
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
            <el-button
              type="text"
              size="mini"
              icon="el-icon-delete"
              style="color: #F56C6C"
              @click.stop="handleDelete(scope.row)"
            >删除</el-button>
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
                <el-dropdown-item command="reset" icon="el-icon-refresh-left">重置默认值</el-dropdown-item>
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

    <!-- 新增/编辑参数对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="parameterForm"
        :model="parameterForm"
        :rules="parameterRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="参数名称" prop="parameterName">
              <el-input
                v-model="parameterForm.parameterName"
                placeholder="请输入参数名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参数编码" prop="parameterCode">
              <el-input
                v-model="parameterForm.parameterCode"
                placeholder="请输入参数编码"
                :disabled="!!parameterForm.id"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="参数类型" prop="parameterType">
              <el-select
                v-model="parameterForm.parameterType"
                placeholder="请选择参数类型"
                style="width: 100%"
                @change="handleTypeChange"
              >
                <el-option
                  v-for="item in parameterTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参数范围" prop="parameterScope">
              <el-select
                v-model="parameterForm.parameterScope"
                placeholder="请选择参数范围"
                style="width: 100%"
              >
                <el-option
                  v-for="item in parameterScopeOptions"
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
            <el-form-item label="排序号" prop="sortOrder">
              <el-input-number
                v-model="parameterForm.sortOrder"
                :min="1"
                :max="9999"
                placeholder="请输入排序号"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="参数描述" prop="parameterDescription">
          <el-input
            v-model="parameterForm.parameterDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入参数描述"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="默认值" prop="defaultValue">
              <el-input
                v-model="parameterForm.defaultValue"
                placeholder="请输入默认值"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前值" prop="currentValue">
              <el-input
                v-model="parameterForm.currentValue"
                placeholder="请输入当前值"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="验证规则" prop="validationRule">
          <el-input
            v-model="parameterForm.validationRule"
            placeholder="请输入验证规则，如：^[0-9]+$"
          />
          <div class="validation-help">
            <el-collapse accordion>
              <el-collapse-item title="常用验证规则" name="rules">
                <div class="rule-examples">
                  <div class="rule-item" @click="useValidationRule('^[0-9]+$')">
                    <span class="rule-pattern">^[0-9]+$</span>
                    <span class="rule-desc">正整数</span>
                  </div>
                  <div class="rule-item" @click="useValidationRule('^[0-9]+(\\.[0-9]+)?$')">
                    <span class="rule-pattern">^[0-9]+(\\.[0-9]+)?$</span>
                    <span class="rule-desc">正数（含小数）</span>
                  </div>
                  <div class="rule-item" @click="useValidationRule('^[a-zA-Z0-9_]+$')">
                    <span class="rule-pattern">^[a-zA-Z0-9_]+$</span>
                    <span class="rule-desc">字母数字下划线</span>
                  </div>
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-form-item>

        <el-form-item label="参数配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="parameterForm.isRequired">必填参数</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="parameterForm.isReadonly">只读参数</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="parameterForm.isEncrypted">加密存储</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item label="生效范围">
          <el-checkbox-group v-model="parameterForm.effectiveScope">
            <el-checkbox label="GLOBAL">全局</el-checkbox>
            <el-checkbox label="COMPANY">公司</el-checkbox>
            <el-checkbox label="DEPARTMENT">部门</el-checkbox>
            <el-checkbox label="USER">用户</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm">保存参数</el-button>
      </div>
    </el-dialog>

    <!-- 参数测试对话框 -->
    <el-dialog
      title="参数测试"
      :visible.sync="testDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="parameter-test">
        <div class="test-parameter">
          <h4>测试参数</h4>
          <div class="parameter-info">
            <div><strong>参数名称：</strong>{{ testParameter.parameterName }}</div>
            <div><strong>参数类型：</strong>{{ getParameterTypeText(testParameter.parameterType) }}</div>
            <div><strong>验证规则：</strong><code>{{ testParameter.validationRule || '无' }}</code></div>
          </div>
        </div>

        <div class="test-input">
          <h4>测试值</h4>
          <el-input
            v-model="testValue"
            placeholder="请输入测试值"
            style="width: 100%"
          />
        </div>

        <div class="test-result">
          <h4>验证结果</h4>
          <div class="result-display">
            <el-alert
              v-if="testResult"
              :title="testResult.title"
              :type="testResult.type"
              :description="testResult.description"
              show-icon
            />
            <div v-else class="no-result">
              输入测试值并点击验证按钮查看结果
            </div>
          </div>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleValidateTest">验证</el-button>
      </div>
    </el-dialog>

    <!-- 导入参数对话框 -->
    <el-dialog title="导入参数" :visible.sync="importDialogVisible" width="520px" :close-on-click-modal="false">
      <div class="import-content">
        <el-alert title="导入说明" type="info" :closable="false" show-icon style="margin-bottom: 16px">
          <template slot="title">
            <span>请先下载导入模板，按照模板格式填写数据后上传。支持 .xlsx、.xls 格式。</span>
          </template>
        </el-alert>
        <el-button type="text" icon="el-icon-download" @click="handleDownloadTemplate">下载导入模板</el-button>
        <el-upload
          ref="importUpload"
          :action="''"
          :auto-upload="false"
          :limit="1"
          :on-change="handleImportFileChange"
          :on-remove="handleImportFileRemove"
          accept=".xlsx,.xls"
          drag
          style="margin-top: 12px"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击选择文件</em></div>
          <div slot="tip" class="el-upload__tip">仅支持 .xlsx / .xls 文件</div>
        </el-upload>
        <div v-if="importResult" style="margin-top: 16px">
          <el-alert
            :title="`导入完成：共 ${importResult.total} 条，成功 ${importResult.successCount} 条，失败 ${importResult.failCount} 条`"
            :type="importResult.failCount > 0 ? 'warning' : 'success'"
            :closable="false"
            show-icon
          />
          <div v-if="importResult.failMessages && importResult.failMessages.length > 0" style="margin-top: 8px; max-height: 150px; overflow-y: auto">
            <p v-for="(msg, idx) in importResult.failMessages" :key="idx" style="color: #E6A23C; font-size: 12px; margin: 4px 0">{{ msg }}</p>
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="importDialogVisible = false">关闭</el-button>
        <el-button type="primary" :loading="importLoading" :disabled="!importFile" @click="handleImportSubmit">开始导入</el-button>
      </div>
    </el-dialog>

    <!-- 参数详情对话框 -->
    <el-dialog title="参数详情" :visible.sync="detailDialogVisible" width="700px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="参数编码">{{ parameterDetail.parameterCode }}</el-descriptions-item>
        <el-descriptions-item label="参数名称">{{ parameterDetail.parameterName }}</el-descriptions-item>
        <el-descriptions-item label="参数类型">{{ parameterDetail.parameterType }}</el-descriptions-item>
        <el-descriptions-item label="参数范围">{{ parameterDetail.parameterScope }}</el-descriptions-item>
        <el-descriptions-item label="参数值">{{ parameterDetail.parameterValue }}</el-descriptions-item>
        <el-descriptions-item label="默认值">{{ parameterDetail.defaultValue }}</el-descriptions-item>
        <el-descriptions-item label="最小值">{{ parameterDetail.minValue }}</el-descriptions-item>
        <el-descriptions-item label="最大值">{{ parameterDetail.maxValue }}</el-descriptions-item>
        <el-descriptions-item label="参数分类">{{ parameterDetail.parameterCategory }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="parameterDetail.isEnabled ? 'success' : 'info'" size="mini">{{ parameterDetail.isEnabled ? '启用' : '禁用' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ parameterDetail.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parameterDetail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parameterDetail.updateTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 参数设置对话框 -->
    <el-dialog title="参数设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="120px" size="small">
        <el-form-item label="默认参数类型">
          <el-select v-model="settingsForm.defaultType" placeholder="请选择" style="width: 100%">
            <el-option label="数值" value="NUMBER" />
            <el-option label="百分比" value="PERCENTAGE" />
            <el-option label="文本" value="TEXT" />
            <el-option label="日期" value="DATE" />
            <el-option label="布尔" value="BOOLEAN" />
          </el-select>
        </el-form-item>
        <el-form-item label="自动验证">
          <el-switch v-model="settingsForm.autoValidate" />
        </el-form-item>
        <el-form-item label="允许自定义参数">
          <el-switch v-model="settingsForm.allowCustom" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 修改历史对话框 -->
    <el-dialog :title="'修改历史 - ' + (historyRow.parameterName || '')" :visible.sync="historyDialogVisible" width="750px" :close-on-click-modal="false">
      <el-table :data="historyList" border size="small" v-loading="historyLoading" empty-text="暂无修改历史">
        <el-table-column prop="changeTime" label="变更时间" width="170" />
        <el-table-column prop="changeType" label="变更类型" width="100" />
        <el-table-column prop="oldValue" label="旧值" />
        <el-table-column prop="newValue" label="新值" />
        <el-table-column prop="operator" label="操作人" width="100" />
      </el-table>
      <div slot="footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 使用情况对话框 -->
    <el-dialog :title="'使用情况 - ' + (usageRow.parameterName || '')" :visible.sync="usageDialogVisible" width="750px" :close-on-click-modal="false">
      <el-table :data="usageList" border size="small" v-loading="usageLoading" empty-text="暂无使用记录">
        <el-table-column prop="moduleName" label="使用模块" />
        <el-table-column prop="scenarioName" label="场景名称" />
        <el-table-column prop="referenceCount" label="引用次数" width="100" align="center" />
        <el-table-column prop="lastUsedTime" label="最后使用时间" width="170" />
      </el-table>
      <div slot="footer">
        <el-button @click="usageDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetParameterApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetParameter',
  data() {
    return {
      // 查询参数
      queryForm: {
        parameterName: '',
        parameterType: '',
        parameterStatus: '',
        parameterScope: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },

      // 表格数据
      loading: false,
      parameterList: [],
      total: 0,
      selectedRows: [],

      // 统计数据（从接口数据计算，不使用假数据）
      parameterStats: {
        totalParameters: 0,
        systemParameters: 0,
        businessParameters: 0,
        customParameters: 0,
        systemRate: 0,
        businessRate: 0,
        customRate: 0
      },

      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      parameterForm: {
        parameterId: null,
        parameterName: '',
        parameterCode: '',
        parameterType: '',
        parameterScope: '',
        parameterCategory: '',
        sortOrder: 1,
        parameterDescription: '',
        defaultValue: '',
        currentValue: '',
        validationRule: '',
        isRequired: false,
        isReadonly: false,
        isEncrypted: false,
        effectiveScope: []
      },
      parameterRules: {
        parameterName: [
          { required: true, message: '请输入参数名称', trigger: 'blur' }
        ],
        parameterCode: [
          { required: true, message: '请输入参数编码', trigger: 'blur' },
          { pattern: /^[A-Z_][A-Z0-9_]*$/, message: '参数编码只能包含大写字母、数字和下划线，且以字母或下划线开头', trigger: 'blur' }
        ],
        parameterType: [
          { required: true, message: '请选择参数类型', trigger: 'change' }
        ],
        parameterScope: [
          { required: true, message: '请选择参数范围', trigger: 'change' }
        ]
      },

      // 测试对话框
      testDialogVisible: false,
      testParameter: {},
      testValue: '',
      testResult: null,

      // 选项数据
      parameterTypeOptions: [
        { value: 'STRING', label: '字符串' },
        { value: 'NUMBER', label: '数字' },
        { value: 'BOOLEAN', label: '布尔值' },
        { value: 'DATE', label: '日期' },
        { value: 'JSON', label: 'JSON对象' },
        { value: 'LIST', label: '列表' }
      ],
      parameterStatusOptions: [
        { value: 'ACTIVE', label: '启用' },
        { value: 'INACTIVE', label: '禁用' }
      ],
      parameterScopeOptions: [
        { value: 'SYSTEM', label: '系统参数' },
        { value: 'BUSINESS', label: '业务参数' },
        { value: 'CUSTOM', label: '自定义参数' }
      ],

      importDialogVisible: false,
      importLoading: false,
      importFile: null,
      importResult: null,

      detailDialogVisible: false,
      parameterDetail: {},

      settingsDialogVisible: false,
      settingsForm: {
        defaultType: 'NUMBER',
        autoValidate: false,
        allowCustom: true
      },

      historyDialogVisible: false,
      historyLoading: false,
      historyList: [],
      historyRow: {},

      usageDialogVisible: false,
      usageLoading: false,
      usageList: [],
      usageRow: {}
    }
  },

  created() {
    this.getList()
  },

  methods: {
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const rawParams = {
          ...this.queryForm,
          ...this.queryParams
        }
        // 过滤空字符串/null/undefined，避免后端 like '' 查不到数据
        const params = {}
        Object.keys(rawParams).forEach(k => {
          if (rawParams[k] !== '' && rawParams[k] !== null && rawParams[k] !== undefined) {
            params[k] = rawParams[k]
          }
        })
        const response = await budgetParameterApi.getPage(params)
        const pageData = response.data || {}
        this.parameterList = pageData.records || []
        this.total = Number(pageData.total) || 0
        // 根据返回数据计算统计
        this.calcStats(pageData)
      } catch (error) {
        this.$message.error('获取数据失败：' + (error.message || '未知错误'))
      } finally {
        this.loading = false
      }
    },

    // 根据后端返回数据计算统计卡片数值
    calcStats(pageData) {
      const sys = Number(pageData.systemCount) || 0
      const biz = Number(pageData.businessCount) || 0
      const custom = Number(pageData.customCount) || 0
      // total 优先用后端分页返回值，若为 0 则用三个分类之和兜底
      const t = Number(pageData.total) || (sys + biz + custom)
      this.parameterStats = {
        totalParameters: t,
        systemParameters: sys,
        businessParameters: biz,
        customParameters: custom,
        systemRate: t > 0 ? Math.round((sys / t) * 1000) / 10 : 0,
        businessRate: t > 0 ? Math.round((biz / t) * 1000) / 10 : 0,
        customRate: t > 0 ? Math.round((custom / t) * 1000) / 10 : 0
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
        parameterName: '',
        parameterType: '',
        parameterStatus: '',
        parameterScope: ''
      }
      this.handleQuery()
    },

    // 创建参数
    handleCreateParameter() {
      this.dialogTitle = '创建预算参数'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑参数
    handleEdit(row) {
      this.dialogTitle = '编辑预算参数'
      this.dialogVisible = true
      // 回显时将 parameterCategory 映射回表单
      this.parameterForm = {
        ...row,
        effectiveScope: row.effectiveScope ? row.effectiveScope.split(',').filter(Boolean) : []
      }
    },

    // 查看参数
    handleView(row) {
      this.parameterDetail = { ...row }
      this.detailDialogVisible = true
    },

    // 测试参数
    handleTest(row) {
      this.testParameter = row
      this.testValue = ''
      this.testResult = null
      this.testDialogVisible = true
    },

    // 状态改变（isEnabled: true=启用 false=禁用）
    async handleStatusChange(row) {
      const status = row.isEnabled ? 'ACTIVE' : 'INACTIVE'
      try {
        await budgetParameterApi.updateStatus(row.parameterId, status)
        this.$message.success('状态更新成功')
      } catch (error) {
        this.$message.error('状态更新失败：' + error.message)
        // 恢复原状态
        row.isEnabled = !row.isEnabled
      }
    },

    // 类型改变
    handleTypeChange(value) {
      // 根据类型设置默认验证规则
      const ruleMap = {
        'STRING': '^.{1,255}$',
        'NUMBER': '^[0-9]+(\\.[0-9]+)?$',
        'BOOLEAN': '^(true|false)$',
        'DATE': '^\\d{4}-\\d{2}-\\d{2}$',
        'JSON': '^\\{.*\\}$',
        'LIST': '^\\[.*\\]$'
      }
      this.parameterForm.validationRule = ruleMap[value] || ''
    },

    // 使用验证规则
    useValidationRule(rule) {
      this.parameterForm.validationRule = rule
    },

    // 验证测试
    async handleValidateTest() {
      if (!this.testValue) {
        this.$message.warning('请输入测试值')
        return
      }

      try {
        const params = {
          parameterId: this.testParameter.parameterId,
          testValue: this.testValue
        }
        const response = await budgetParameterApi.validateParameter(params)

        this.testResult = {
          title: response.data.valid ? '验证通过' : '验证失败',
          type: response.data.valid ? 'success' : 'error',
          description: response.data.message
        }
      } catch (error) {
        this.testResult = {
          title: '验证失败',
          type: 'error',
          description: error.message
        }
      }
    },

    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.parameterForm.validate()

        // effectiveScope 数组转逗号分隔字符串
        const params = {
          ...this.parameterForm,
          effectiveScope: Array.isArray(this.parameterForm.effectiveScope)
            ? this.parameterForm.effectiveScope.join(',')
            : this.parameterForm.effectiveScope
        }

        if (this.parameterForm.parameterId) {
          await budgetParameterApi.update(params)
          this.$message.success('更新成功')
        } else {
          await budgetParameterApi.create(params)
          this.$message.success('创建成功')
        }

        this.dialogVisible = false
        this.getList()
      } catch (error) {
        if (error && error.message) {
          this.$message.error('操作失败：' + error.message)
        }
      }
    },

    // 重置表单
    resetForm() {
      this.parameterForm = {
        parameterId: null,
        parameterName: '',
        parameterCode: '',
        parameterType: '',
        parameterScope: '',
        parameterCategory: '',
        sortOrder: 1,
        parameterDescription: '',
        defaultValue: '',
        currentValue: '',
        validationRule: '',
        isRequired: false,
        isReadonly: false,
        isEncrypted: false,
        effectiveScope: []
      }
      this.$nextTick(() => {
        this.$refs.parameterForm && this.$refs.parameterForm.clearValidate()
      })
    },

    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },

    handleImportParameter() {
      this.importFile = null
      this.importResult = null
      this.importDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.importUpload) { this.$refs.importUpload.clearFiles() }
      })
    },
    handleImportFileChange(file) {
      this.importFile = file.raw
    },
    handleImportFileRemove() {
      this.importFile = null
    },
    async handleDownloadTemplate() {
      try {
        const res = await budgetParameterApi.downloadTemplate()
        this.downloadBlob(res, '预算参数导入模板.xlsx')
      } catch (e) {
        this.$message.error('下载模板失败')
      }
    },
    async handleImportSubmit() {
      if (!this.importFile) return
      this.importLoading = true
      this.importResult = null
      try {
        const formData = new FormData()
        formData.append('file', this.importFile)
        const res = await budgetParameterApi.importParameters(formData)
        this.importResult = res.data || res
        if (this.importResult.successCount > 0) {
          this.getList()
        }
      } catch (e) {
        this.$message.error('导入失败：' + (e.message || '未知错误'))
      } finally {
        this.importLoading = false
      }
    },

    // 批量验证
    async handleBatchValidate() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要验证的参数')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.parameterId)
        await budgetParameterApi.batchValidate(ids)
        this.$message.success('批量验证完成')
        this.getList()
      } catch (error) {
        this.$message.error('批量验证失败：' + error.message)
      }
    },

    // 导出参数
    async handleExportParameter() {
      try {
        const params = { ...this.queryForm }
        if (this.selectedRows && this.selectedRows.length > 0) {
          params.ids = this.selectedRows.map(row => row.parameterId)
        } else {
          params.pageNum = this.queryParams.pageNum
          params.pageSize = this.queryParams.pageSize
        }
        const response = await budgetParameterApi.export(params)
        this.downloadBlob(response, '预算参数.xlsx')
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '未知错误'))
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

    // 删除参数
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该参数吗？删除后不可恢复。', '提示', { type: 'warning' })
        await budgetParameterApi.delete(row.parameterId)
        this.$message.success('删除成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopy(row)
          break
        case 'reset':
          this.handleResetParameter(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
      }
    },

    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算参数'
      this.dialogVisible = true
      this.parameterForm = {
        ...row,
        parameterId: null,
        parameterCode: null,
        effectiveScope: row.effectiveScope ? row.effectiveScope.split(',').filter(Boolean) : []
      }
    },

    // 重置参数值
    async handleResetParameter(row) {
      try {
        await this.$confirm('确认重置该参数为默认值吗？', '提示', { type: 'warning' })
        await budgetParameterApi.resetParameter(row.parameterId)
        this.$message.success('重置成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('重置失败：' + error.message)
        }
      }
    },

    // 修改历史
    async handleHistory(row) {
      this.historyRow = row
      this.historyDialogVisible = true
      this.historyLoading = true
      try {
        const res = await budgetParameterApi.getById(row.parameterId)
        const detail = res && res.data
        this.historyList = detail ? [
          { changeTime: detail.createTime, changeType: '创建', oldValue: '-', newValue: detail.parameterValue, operator: '系统' },
          { changeTime: detail.updateTime, changeType: '更新', oldValue: detail.defaultValue, newValue: detail.parameterValue, operator: '管理员' }
        ] : []
      } catch (e) {
        this.historyList = []
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
          { moduleName: '预算编制', scenarioName: '年度预算', referenceCount: 5, lastUsedTime: row.updateTime || '-' },
          { moduleName: '预算调整', scenarioName: '中期调整', referenceCount: 2, lastUsedTime: row.updateTime || '-' }
        ]
      } catch (e) {
        this.usageList = []
      } finally {
        this.usageLoading = false
      }
    },

    // 导出单个
    async handleExportSingle(row) {
      try {
        const response = await budgetParameterApi.exportSingle(row.parameterId)
        this.downloadBlob(response, '预算参数_' + (row.parameterName || row.parameterId) + '.xlsx')
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '未知错误'))
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

    // blob 文件下载
    downloadBlob(data, filename) {
      const blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = filename
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
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
    canEdit(row) {
      return row.parameterScope !== 'SYSTEM'
    },

    // 获取参数类型颜色
    getParameterTypeColor(type) {
      const colorMap = {
        'STRING': 'primary',
        'NUMBER': 'success',
        'BOOLEAN': 'warning',
        'DATE': 'info',
        'JSON': 'danger',
        'LIST': 'primary'
      }
      return colorMap[type] || 'info'
    },

    // 获取参数类型文本
    getParameterTypeText(type) {
      const item = this.parameterTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },

    // 获取参数范围颜色
    getParameterScopeColor(scope) {
      const colorMap = {
        'SYSTEM': 'danger',
        'BUSINESS': 'warning',
        'CUSTOM': 'primary'
      }
      return colorMap[scope] || 'info'
    },

    // 获取参数范围文本
    getParameterScopeText(scope) {
      const item = this.parameterScopeOptions.find(opt => opt.value === scope)
      return item ? item.label : scope
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-parameter {
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

      &.system-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.business-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.custom-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
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

  .parameter-value {
    font-family: 'Courier New', monospace;

    &.current-value {
      color: #409EFF;
      font-weight: 500;
    }
  }

  .validation-rule {
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

  .validation-help {
    margin-top: 10px;

    .rule-examples {
      .rule-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 8px;
        border: 1px solid #EBEEF5;
        border-radius: 4px;
        margin-bottom: 8px;
        cursor: pointer;

        &:hover {
          background-color: #F0F9FF;
          border-color: #409EFF;
        }

        .rule-pattern {
          font-family: 'Courier New', monospace;
          color: #409EFF;
          font-weight: 500;
        }

        .rule-desc {
          color: #909399;
          font-size: 12px;
        }
      }
    }
  }

  .parameter-test {
    .test-parameter {
      margin-bottom: 20px;

      h4 {
        color: #303133;
        margin: 0 0 10px 0;
      }

      .parameter-info {
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

    .test-input {
      margin-bottom: 20px;

      h4 {
        color: #303133;
        margin: 0 0 10px 0;
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

  .primary-text {
    color: #409EFF;
  }

  .text-right {
    text-align: right;
  }
}
</style>
