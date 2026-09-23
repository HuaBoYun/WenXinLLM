<template>
  <div class="cost-model-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-cpu"></i>
          成本模型管理
        </h1>
        <p class="page-description">管理成本计算模型、算法配置和参数设置</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreateModel">
          新建模型
        </el-button>
        <el-button type="success" icon="el-icon-view" @click="handleModelLibrary">
          模型库
        </el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleExportModel">
          导出模型
        </el-button>
      </div>
    </div>

    <!-- 搜索条件 -->
    <div class="search-container">
      <el-form :model="queryForm" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="模型名称" prop="modelName">
          <el-input
            v-model="queryForm.modelName"
            placeholder="请输入模型名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="模型类型" prop="modelType">
          <el-select
            v-model="queryForm.modelType"
            placeholder="请选择模型类型"
            clearable
            style="width: 180px"
          >
            <el-option label="线性模型" value="LINEAR" />
            <el-option label="非线性模型" value="NONLINEAR" />
            <el-option label="混合模型" value="MIXED" />
            <el-option label="全成本模型" value="ALL_COST" />
          </el-select>
        </el-form-item>
        <el-form-item label="适用行业" prop="industry">
          <el-select
            v-model="queryForm.industry"
            placeholder="请选择适用行业"
            clearable
            style="width: 150px"
          >
            <el-option label="制造业" value="manufacturing" />
            <el-option label="服务业" value="service" />
            <el-option label="建筑业" value="construction" />
            <el-option label="零售业" value="retail" />
          </el-select>
        </el-form-item>
        <el-form-item label="模型状态" prop="status">
          <el-select
            v-model="queryForm.status"
            placeholder="请选择模型状态"
            clearable
            style="width: 120px"
          >
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
            <el-option label="测试中" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="modelCode" label="模型编码" width="150" />
        <el-table-column prop="modelName" label="模型名称" min-width="200" />
        <el-table-column prop="modelTypeName" label="模型类型" width="120" />
        <el-table-column prop="industryName" label="适用行业" width="100" />
        <el-table-column prop="algorithmName" label="算法" width="120" />
        <el-table-column prop="accuracy" label="准确率" width="100" align="right">
          <template slot-scope="scope">
            <span :class="getAccuracyClass(scope.row.accuracy)">
              {{ scope.row.accuracy }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="usageCount" label="使用次数" width="100" align="right" />
        <el-table-column prop="statusName" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button size="mini" type="text" @click="handleTest(scope.row)">
              测试
            </el-button>
            <el-button size="mini" type="text" @click="handleCopy(scope.row)">
              复制
            </el-button>
            <el-button
              size="mini"
              type="text"
              :class="scope.row.isEnabled === 1 ? 'danger-text' : 'success-text'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.isEnabled === 1 ? '停用' : '启用' }}
            </el-button>
            <el-button size="mini" type="text" class="danger-text" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryForm.pageNumber"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryForm.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1000px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="formRef"
        label-width="120px"
        class="dialog-form"
      >
        <el-tabs v-model="activeTab" type="border-card">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="模型编码" prop="modelCode">
                  <el-input
                    v-model="formData.modelCode"
                    placeholder="请输入模型编码"
                    :disabled="isView"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="模型名称" prop="modelName">
                  <el-input
                    v-model="formData.modelName"
                    placeholder="请输入模型名称"
                    :disabled="isView"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="模型类型" prop="modelType">
                  <el-select
                    v-model="formData.modelType"
                    placeholder="请选择模型类型"
                    style="width: 100%"
                    :disabled="isView"
                  >
                    <el-option label="线性模型" value="LINEAR" />
                    <el-option label="非线性模型" value="NONLINEAR" />
                    <el-option label="混合模型" value="MIXED" />
                    <el-option label="全成本模型" value="ALL_COST" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="适用行业" prop="industry">
                  <el-select
                    v-model="formData.industry"
                    placeholder="请选择适用行业"
                    style="width: 100%"
                    :disabled="isView"
                  >
                    <el-option label="制造业" value="manufacturing" />
                    <el-option label="服务业" value="service" />
                    <el-option label="建筑业" value="construction" />
                    <el-option label="零售业" value="retail" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="算法" prop="algorithm">
                  <el-select
                    v-model="formData.algorithm"
                    placeholder="请选择算法"
                    style="width: 100%"
                    :disabled="isView"
                  >
                    <el-option label="线性回归" value="linear_regression" />
                    <el-option label="决策树" value="decision_tree" />
                    <el-option label="随机森林" value="random_forest" />
                    <el-option label="神经网络" value="neural_network" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="版本号" prop="version">
                  <el-input
                    v-model="formData.version"
                    placeholder="请输入版本号"
                    :disabled="isView"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="模型描述" prop="description">
              <el-input
                v-model="formData.description"
                type="textarea"
                :rows="4"
                placeholder="请输入模型描述"
                :disabled="isView"
              />
            </el-form-item>
          </el-tab-pane>

          <!-- 参数配置 -->
          <el-tab-pane label="参数配置" name="parameters">
            <div class="parameters-section">
              <div class="section-header">
                <h4>成本要素权重</h4>
                <el-button v-if="!isView" size="small" type="primary" @click="handleAddParameter">
                  添加参数
                </el-button>
              </div>
              <el-table :data="formData.parameters" border stripe>
                <el-table-column prop="parameterName" label="参数名称" width="200">
                  <template slot-scope="scope">
                    <el-input
                      v-model="scope.row.parameterName"
                      placeholder="请输入参数名称"
                      :disabled="isView"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="parameterValue" label="参数值" width="150">
                  <template slot-scope="scope">
                    <el-input-number
                      v-model="scope.row.parameterValue"
                      :min="0"
                      :max="100"
                      :precision="2"
                      style="width: 100%"
                      :disabled="isView"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="parameterUnit" label="单位" width="100">
                  <template slot-scope="scope">
                    <el-input
                      v-model="scope.row.parameterUnit"
                      placeholder="单位"
                      :disabled="isView"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="description" label="说明" min-width="200">
                  <template slot-scope="scope">
                    <el-input
                      v-model="scope.row.description"
                      placeholder="请输入说明"
                      :disabled="isView"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100" align="center" v-if="!isView">
                  <template slot-scope="scope">
                    <el-button size="mini" type="text" class="danger-text" @click="handleDeleteParameter(scope.$index)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>

          <!-- 计算规则 -->
          <el-tab-pane label="计算规则" name="rules">
            <div class="rules-section">
              <el-form-item label="计算公式" prop="formula">
                <el-input
                  v-model="formData.formula"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入计算公式，如：总成本 = 材料成本 * 材料权重 + 人工成本 * 人工权重 + 制造费用 * 制造费用权重"
                  :disabled="isView"
                />
              </el-form-item>
              <el-form-item label="分摊规则" prop="allocationRule">
                <el-input
                  v-model="formData.allocationRule"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入成本分摊规则"
                  :disabled="isView"
                />
              </el-form-item>
              <el-form-item label="调整规则" prop="adjustmentRule">
                <el-input
                  v-model="formData.adjustmentRule"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入成本调整规则"
                  :disabled="isView"
                />
              </el-form-item>
            </div>
          </el-tab-pane>

          <!-- 验证设置 -->
          <el-tab-pane label="验证设置" name="validation">
            <div class="validation-section">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="准确率阈值" prop="accuracyThreshold">
                    <el-input-number
                      v-model="formData.accuracyThreshold"
                      :min="0"
                      :max="100"
                      :precision="2"
                      placeholder="准确率阈值"
                      style="width: 100%"
                      :disabled="isView"
                    />
                    <span style="margin-left: 8px; color: #909399;">%</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="误差范围" prop="errorRange">
                    <el-input-number
                      v-model="formData.errorRange"
                      :min="0"
                      :max="50"
                      :precision="2"
                      placeholder="允许误差范围"
                      style="width: 100%"
                      :disabled="isView"
                    />
                    <span style="margin-left: 8px; color: #909399;">%</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="验证数据集" prop="validationDataset">
                <el-input
                  v-model="formData.validationDataset"
                  placeholder="请输入验证数据集路径或描述"
                  :disabled="isView"
                />
              </el-form-item>
              <el-form-item label="测试用例" prop="testCases">
                <el-input
                  v-model="formData.testCases"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入测试用例描述"
                  :disabled="isView"
                />
              </el-form-item>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button v-if="!isView" type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!-- 模型测试对话框 -->
    <el-dialog
      title="模型测试"
      :visible.sync="testDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div class="test-container">
        <el-form :model="testForm" label-width="120px">
          <el-form-item label="测试数据">
            <el-input
              v-model="testForm.testData"
              type="textarea"
              :rows="6"
              placeholder="请输入测试数据（JSON格式）"
            />
          </el-form-item>
        </el-form>
        
        <div class="test-actions">
          <el-button type="primary" @click="handleRunTest" :loading="testLoading">
            运行测试
          </el-button>
          <el-button @click="handleClearTest">
            清空结果
          </el-button>
        </div>

        <div v-if="testResult" class="test-result">
          <h4>测试结果</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="计算结果">{{ testResult.calculatedCost }}</el-descriptions-item>
            <el-descriptions-item label="执行时间">{{ testResult.executionTime }}ms</el-descriptions-item>
            <el-descriptions-item label="准确率">{{ testResult.accuracy }}%</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="testResult.success ? 'success' : 'danger'">
                {{ testResult.success ? '成功' : '失败' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
          
          <div v-if="testResult.details" style="margin-top: 15px;">
            <h5>详细信息</h5>
            <pre>{{ testResult.details }}</pre>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 模型库对话框：浏览已有模型 + 一键复制为新模型 -->
    <el-dialog
      title="模型库"
      :visible.sync="libraryDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div class="library-tip">
        从已有模型中挑一个作为模板，点"复制为新模型"会自动填入新建表单，方便快速复制配置后做局部调整。
      </div>
      <el-table
        v-loading="libraryLoading"
        :data="libraryData"
        border
        stripe
        height="420"
        style="width: 100%"
      >
        <el-table-column prop="modelCode" label="模型编码" width="160" />
        <el-table-column prop="modelName" label="模型名称" min-width="180" />
        <el-table-column prop="modelTypeName" label="类型" width="110" />
        <el-table-column prop="industryName" label="适用行业" width="100" />
        <el-table-column prop="formula" label="计算公式" min-width="200" show-overflow-tooltip />
        <el-table-column label="准确率" width="90" align="right">
          <template slot-scope="scope">
            <span :class="getAccuracyClass(scope.row.accuracy)">{{ scope.row.accuracy }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" plain @click="handleApplyTemplate(scope.row)">
              复制为新模型
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="libraryDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCostModelPage,
  saveOrUpdateCostModel,
  deleteCostModel,
  updateCostModelStatus,
  validateCostModel,
  exportCostEstimateDataBlob
} from '@/api/financialSharing/costEstimation'
import { normalizeKeysArray } from '@/utils/keyNormalize'

export default {
  name: 'CostModel',
  data() {
    return {
      loading: false,
      submitLoading: false,
      testLoading: false,
      tableData: [],
      total: 0,
      selectedRows: [],
      activeTab: 'basic',
      queryForm: {
        pageNumber: 1,
        pageSize: 20,
        modelName: '',
        modelType: null,
        industry: '',
        status: null
      },
      dialogVisible: false,
      testDialogVisible: false,
      libraryDialogVisible: false,
      libraryLoading: false,
      libraryData: [],
      dialogTitle: '',
      isView: false,
      formData: {
        modelId: null,
        modelCode: '',
        modelName: '',
        modelType: null,
        industry: '',
        algorithm: '',
        version: '1.0',
        description: '',
        formula: '',
        allocationRule: '',
        adjustmentRule: '',
        accuracyThreshold: 95.0,
        errorRange: 5.0,
        validationDataset: '',
        testCases: '',
        parameters: []
      },
      testForm: {
        testData: ''
      },
      testResult: null,
      formRules: {
        modelCode: [
          { required: true, message: '请输入模型编码', trigger: 'blur' }
        ],
        modelName: [
          { required: true, message: '请输入模型名称', trigger: 'blur' }
        ],
        modelType: [
          { required: true, message: '请选择模型类型', trigger: 'change' }
        ],
        algorithm: [
          { required: true, message: '请选择算法', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取数据
    async fetchData() {
      this.loading = true
      try {
        const response = await getCostModelPage(this.queryForm)

        if (response && response.code === 1) {
          // 后端 PageResult: tlist + totalRecord（兼容 records/total）
          // 达梦 ALLCAPS key（MODELNAME / MODELCODE）需归一化为 camelCase
          const dataObj = response.data || {}
          const rawList = dataObj.tlist || dataObj.records || []
          this.tableData = normalizeKeysArray(rawList)
          this.total = dataObj.totalRecord || dataObj.total || 0

          // 处理数据格式
          this.tableData.forEach(item => {
            // 模型类型名称映射（兼容字符串枚举 LINEAR/NONLINEAR/MIXED/ALL_COST 与历史数字编码）
            const typeMap = {
              LINEAR: '线性模型',
              NONLINEAR: '非线性模型',
              MIXED: '混合模型',
              ALL_COST: '全成本模型',
              1: '线性模型',
              2: '非线性模型',
              3: '混合模型',
              4: '全成本模型'
            }
            item.modelTypeName = typeMap[item.modelType] || (item.modelType || '未知')

            // 状态名称映射
            const statusMap = { 0: '停用', 1: '启用' }
            item.statusName = statusMap[item.isEnabled] || '未知'

            // 派生字段：accuracy 兜底 accuracyScore；formula 兜底 modelFormula；description 兜底 modelDescription
            if (item.accuracy == null) item.accuracy = item.accuracyScore != null ? item.accuracyScore : ''
            if (!item.formula) item.formula = item.modelFormula || ''
            if (!item.description) item.description = item.modelDescription || ''
            // industry / algorithm 派生中文名（前后端枚举码 → 显示名）
            const industryMap = {
              manufacturing: '制造业', service: '服务业',
              construction: '建筑业',  retail: '零售业'
            }
            item.industryName = industryMap[item.industry] || (item.industry || '')

            const algorithmMap = {
              linear_regression: '线性回归', activity_based: '作业成本',
              variable_costing: '变动成本',  neural_network: '神经网络'
            }
            item.algorithmName = algorithmMap[item.algorithm] || (item.algorithm || '')

            if (item.usageCount == null) item.usageCount = 0
          })
        } else if (response && response.msg) {
          this.$message.error(response.msg)
        }

        // 历史保留代码（已停用，不再使用模拟数据）
        /*
        this.tableData = [
          {
            modelId: 1,
            modelCode: 'MODEL001',
            modelName: '标准制造成本模型',
            modelType: 1,
            modelTypeName: '标准成本模型',
            industry: 'manufacturing',
            industryName: '制造业',
            algorithm: 'linear_regression',
            accuracy: 96.5,
            usageCount: 128,
            status: 1,
            statusName: '启用',
            createTime: '2024-01-15 10:30:00'
          },
          {
            modelId: 2,
            modelCode: 'MODEL002',
            modelName: '作业成本分析模型',
            modelType: 2,
            modelTypeName: '作业成本模型',
            industry: 'service',
            industryName: '服务业',
            algorithm: 'decision_tree',
            accuracy: 89.2,
            usageCount: 56,
            status: 2,
            statusName: '测试中',
            createTime: '2024-01-10 14:20:00'
          }
        ]
        this.total = 2
        */
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      // 复位分页避免在小页码上停留导致空数据
      this.queryForm.pageNumber = 1
      this.fetchData()
    },

    // 新建模型
    handleCreateModel() {
      this.dialogTitle = '新建成本模型'
      this.isView = false
      this.activeTab = 'basic'
      this.resetFormData()
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.dialogTitle = '查看成本模型'
      this.isView = true
      this.activeTab = 'basic'
      this.formData = { ...row, parameters: row.parameters || [] }
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑成本模型'
      this.isView = false
      this.activeTab = 'basic'
      this.formData = { ...row, parameters: row.parameters || [] }
      this.dialogVisible = true
    },

    // 测试模型
    handleTest(row) {
      this.testForm.testData = ''
      this.testResult = null
      this.testDialogVisible = true
    },

    // 运行测试
    async handleRunTest() {
      if (!this.testForm.testData.trim()) {
        this.$message.warning('请输入测试数据')
        return
      }

      this.testLoading = true
      try {
        const response = await validateCostModel(this.testForm.modelId, this.testForm.testData)

        if (response && response.code === 1) {
          this.testResult = response.data || {}
          this.$message.success('测试完成')
        } else {
          this.$message.error(response && response.msg ? response.msg : '测试失败')
        }
      } catch (error) {
        this.$message.error('测试失败：' + (error && error.message ? error.message : ''))
      } finally {
        this.testLoading = false
      }
    },

    // 清空测试结果
    handleClearTest() {
      this.testForm.testData = ''
      this.testResult = null
    },

    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制成本模型'
      this.isView = false
      this.activeTab = 'basic'
      this.formData = { 
        ...row, 
        modelId: null,
        modelCode: row.modelCode + '_COPY',
        modelName: row.modelName + '_副本',
        parameters: row.parameters || []
      }
      this.dialogVisible = true
    },

    // 切换状态
    async handleToggleStatus(row) {
      try {
        const action = row.status === 1 ? '停用' : '启用'
        await this.$confirm(`确认${action}该成本模型吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const isEnabled = row.isEnabled === 1 ? 0 : 1
        await updateCostModelStatus(row.modelId, isEnabled)

        this.$message.success(`${action}成功`)
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败：' + error.message)
        }
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该成本模型吗？删除后不可恢复！', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 走真删除接口（mapper.deleteById），不要再用 saveOrUpdate 假装软删
        const resp = await deleteCostModel(row.modelId)
        if (resp && resp.code === 1) {
          this.$message.success('删除成功')
          this.fetchData()
        } else {
          this.$message.error(resp && resp.msg ? resp.msg : '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + (error && error.message ? error.message : ''))
        }
      }
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.formRef.validate()
        this.submitLoading = true

        await saveOrUpdateCostModel(this.formData)

        this.$message.success('保存成功')
        this.dialogVisible = false
        this.fetchData()
      } catch (error) {
        if (error !== false) {
          this.$message.error('保存失败：' + error.message)
        }
      } finally {
        this.submitLoading = false
      }
    },

    // 添加参数
    handleAddParameter() {
      this.formData.parameters.push({
        parameterName: '',
        parameterValue: 0,
        parameterUnit: '%',
        description: ''
      })
    },

    // 删除参数
    handleDeleteParameter(index) {
      this.formData.parameters.splice(index, 1)
    },

    // 模型库：弹窗展示已有模型，可一键复制为新建模板
    async handleModelLibrary() {
      this.libraryDialogVisible = true
      this.libraryLoading = true
      try {
        // 复用列表接口，固定取前 50 条；条件不带过滤，让用户尽量多看
        const response = await getCostModelPage({ pageNumber: 1, pageSize: 50 })
        if (response && response.code === 1) {
          const dataObj = response.data || {}
          const rawList = dataObj.tlist || dataObj.records || []
          const list = normalizeKeysArray(rawList)
          // 派生展示字段（与 fetchData 同步）
          const typeMap = {
            LINEAR: '线性模型', NONLINEAR: '非线性模型', MIXED: '混合模型', ALL_COST: '全成本模型',
            1: '线性模型', 2: '非线性模型', 3: '混合模型', 4: '全成本模型'
          }
          const industryMap = {
            manufacturing: '制造业', service: '服务业', construction: '建筑业', retail: '零售业'
          }
          list.forEach(item => {
            item.modelTypeName = typeMap[item.modelType] || (item.modelType || '未知')
            item.industryName  = industryMap[item.industry] || (item.industry || '')
            if (!item.formula) item.formula = item.modelFormula || ''
            if (item.accuracy == null) item.accuracy = item.accuracyScore != null ? item.accuracyScore : ''
          })
          this.libraryData = list
        } else if (response && response.msg) {
          this.$message.error(response.msg)
        }
      } catch (e) {
        this.$message.error('加载模型库失败：' + (e && e.message ? e.message : '未知错误'))
      } finally {
        this.libraryLoading = false
      }
    },

    // 模型库：选中某条，关库 + 打开新建对话框 + 预填模板字段
    handleApplyTemplate(row) {
      // 关掉模型库
      this.libraryDialogVisible = false
      // 设为新建模式（不带 modelId，保存时走 insert 分支）
      this.dialogTitle = '新建成本模型（来自模板）'
      this.isView = false
      this.activeTab = 'basic'
      this.formData = {
        modelId: null,
        modelCode: '',  // 编码必须用户重新填，不能直接复制（会触发"编码已存在"）
        modelName: row.modelName ? row.modelName + '（副本）' : '',
        modelType: row.modelType || null,
        industry: row.industry || '',
        algorithm: row.algorithm || '',
        version: row.version || '1.0.0',
        description: row.description || row.modelDescription || '',
        formula: row.formula || row.modelFormula || '',
        allocationRule: row.allocationRule || '',
        adjustmentRule: row.adjustmentRule || '',
        accuracyThreshold: row.accuracyThreshold || row.accuracyScore || 90,
        errorRange: row.errorRange || 5,
        validationDataset: row.validationDataset || '',
        testCases: row.testCases || '',
        applicableScenario: row.applicableScenario || '',
        parameters: Array.isArray(row.parameters) ? JSON.parse(JSON.stringify(row.parameters)) : []
      }
      this.dialogVisible = true
      this.$message.info('已带入模板内容，请修改"模型编码"后保存')
    },

    // 导出模型
    async handleExportModel() {
      try {
        // 后端 blob 模式返回 { data, headers, status } 包装；老代码直接 Blob 整个响应会拿到 [object Object]
        const resp = await exportCostEstimateDataBlob(this.queryForm)
        const raw = (resp && resp.data) ? resp.data : resp
        const blob = raw instanceof Blob ? raw : new Blob([raw], { type: 'text/csv;charset=utf-8' })

        // 优先读 Content-Disposition 给的文件名，没有就回退到时间戳
        let filename = `成本模型_${new Date().toISOString().slice(0, 10)}.csv`
        const cd = resp && resp.headers && (resp.headers['content-disposition'] || resp.headers['Content-Disposition'])
        if (cd) {
          const m = /filename\*?=(?:UTF-8'')?"?([^";]+)"?/i.exec(cd)
          if (m && m[1]) {
            try { filename = decodeURIComponent(m[1]) } catch (e) { filename = m[1] }
          }
        }

        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = filename
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error && error.message ? error.message : ''))
      }
    },

    // 分页相关
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },

    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 对话框关闭
    handleDialogClose() {
      this.$refs.formRef?.resetFields()
      this.resetFormData()
    },

    // 重置表单数据
    resetFormData() {
      this.formData = {
        modelId: null,
        modelCode: '',
        modelName: '',
        modelType: null,
        industry: '',
        algorithm: '',
        version: '1.0',
        description: '',
        formula: '',
        allocationRule: '',
        adjustmentRule: '',
        accuracyThreshold: 95.0,
        errorRange: 5.0,
        validationDataset: '',
        testCases: '',
        parameters: []
      }
    },

    // 获取准确率样式类
    getAccuracyClass(accuracy) {
      if (accuracy >= 95) return 'high-accuracy'
      if (accuracy >= 85) return 'medium-accuracy'
      return 'low-accuracy'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        0: 'danger',  // 停用
        1: 'success', // 启用
        2: 'warning'  // 测试中
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-model-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      margin: 0 0 8px 0;
      font-size: 24px;
      font-weight: 600;
      color: #303133;

      i {
        margin-right: 8px;
        color: #909399;
      }
    }

    .page-description {
      margin: 0;
      color: #909399;
      font-size: 14px;
    }
  }

  .header-right {
    .el-button {
      margin-left: 10px;
    }
  }
}

.search-container {
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
  }
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}

.dialog-form {
  .parameters-section,
  .rules-section,
  .validation-section {
    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;

      h4 {
        margin: 0;
        color: #303133;
      }
    }
  }
}

.test-container {
  .test-actions {
    margin: 20px 0;
    text-align: center;
  }

  .test-result {
    margin-top: 20px;
    padding: 15px;
    background: #f8f9fa;
    border-radius: 8px;

    h4, h5 {
      margin: 0 0 10px 0;
      color: #303133;
    }

    pre {
      background: #fff;
      padding: 10px;
      border-radius: 4px;
      border: 1px solid #e4e7ed;
      font-size: 12px;
      color: #606266;
    }
  }
}

.danger-text {
  color: #f56c6c !important;
}

.library-tip {
  margin-bottom: 12px;
  padding: 10px 14px;
  background-color: #ecf5ff;
  border-left: 3px solid #409eff;
  color: #606266;
  font-size: 13px;
  line-height: 1.6;
  border-radius: 2px;
}

.success-text {
  color: #67c23a !important;
}

.high-accuracy {
  color: #67c23a;
  font-weight: 600;
}

.medium-accuracy {
  color: #e6a23c;
  font-weight: 600;
}

.low-accuracy {
  color: #f56c6c;
  font-weight: 600;
}
</style>
