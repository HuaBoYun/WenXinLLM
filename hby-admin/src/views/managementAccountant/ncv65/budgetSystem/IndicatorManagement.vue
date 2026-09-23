<template>
  <div class="indicator-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算指标管理</h2>
      <p>管理预算指标，支持8种指标类型和树形结构</p>
    </div>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="指标名称">
          <el-input
            v-model="queryForm.indicatorName"
            placeholder="请输入指标名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="指标编码">
          <el-input
            v-model="queryForm.indicatorCode"
            placeholder="请输入指标编码"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="指标类型">
          <el-select
            v-model="queryForm.indicatorType"
            placeholder="请选择指标类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in indicatorTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数据类型">
          <el-select
            v-model="queryForm.dataType"
            placeholder="请选择数据类型"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in dataTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="queryForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="激活" value="active" />
            <el-option label="停用" value="inactive" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="toolbar-card" shadow="never">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
            新增指标
          </el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            :disabled="!multipleSelection.length"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
          <el-button
            type="success"
            icon="el-icon-check"
            :disabled="!multipleSelection.length"
            @click="handleBatchEnable"
          >
            批量启用
          </el-button>
          <el-button
            type="warning"
            icon="el-icon-close"
            :disabled="!multipleSelection.length"
            @click="handleBatchDisable"
          >
            批量禁用
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-button-group>
            <el-button
              :type="viewMode === 'table' ? 'primary' : 'default'"
              icon="el-icon-s-grid"
              @click="viewMode = 'table'"
            >
              表格视图
            </el-button>
            <el-button
              :type="viewMode === 'tree' ? 'primary' : 'default'"
              icon="el-icon-share"
              @click="viewMode = 'tree'"
            >
              树形视图
            </el-button>
          </el-button-group>
          <el-button icon="el-icon-download" @click="handleExport">
            导出
          </el-button>
          <el-button icon="el-icon-upload2" @click="handleImport">
            导入
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 表格视图 -->
    <el-card v-if="viewMode === 'table'" class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="indicatorCode" label="指标编码" width="120" />
        <el-table-column prop="indicatorName" label="指标名称" min-width="150" />
        <el-table-column prop="indicatorType" label="指标类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getIndicatorTypeTagType(scope.row.indicatorType)">
              {{ formatIndicatorType(scope.row.indicatorType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataType" label="数据类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getDataTypeTagType(scope.row.dataType)">
              {{ formatDataType(scope.row.dataType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="isRequired" label="必填" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isRequired ? 'danger' : 'info'" size="mini">
              {{ scope.row.isRequired ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isCalculated" label="计算" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isCalculated ? 'warning' : 'info'" size="mini">
              {{ scope.row.isCalculated ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="启用状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'active' ? 'success' : 'danger'">
              {{ scope.row.status === 'active' ? '激活' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="text" size="small" @click="handleCopy(scope.row)">
              复制
            </el-button>
            <el-button type="text" size="small" @click="handleCalculate(scope.row)">
              计算
            </el-button>
            <el-button
              type="text"
              size="small"
              style="color: #f56c6c"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 树形视图 -->
    <el-card v-if="viewMode === 'tree'" class="tree-card" shadow="never">
      <el-tree
        ref="indicatorTree"
        :data="treeData"
        :props="treeProps"
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
        show-checkbox
        @check-change="handleTreeCheckChange"
      >
        <span slot-scope="{ node, data }" class="custom-tree-node">
          <span class="tree-node-label">
            <i :class="getTreeNodeIcon(data.type)" />
            {{ node.label }}
            <el-tag v-if="data.type" :type="getIndicatorTypeTagType(data.type)" size="mini">
              {{ formatIndicatorType(data.type) }}
            </el-tag>
          </span>
          <span class="tree-node-actions">
            <el-button type="text" size="mini" @click="handleTreeEdit(data)">
              编辑
            </el-button>
            <el-button type="text" size="mini" @click="handleTreeAdd(data)">
              新增子项
            </el-button>
            <el-button
              type="text"
              size="mini"
              style="color: #f56c6c"
              @click="handleTreeDelete(data)"
            >
              删除
            </el-button>
          </span>
        </span>
      </el-tree>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        size="small"
      >
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="指标编码" prop="indicatorCode">
                  <el-input v-model="form.indicatorCode" placeholder="请输入指标编码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="指标名称" prop="indicatorName">
                  <el-input v-model="form.indicatorName" placeholder="请输入指标名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="指标类型" prop="indicatorType">
                  <el-select v-model="form.indicatorType" placeholder="请选择指标类型" style="width: 100%">
                    <el-option
                      v-for="item in indicatorTypeOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="数据类型" prop="dataType">
                  <el-select v-model="form.dataType" placeholder="请选择数据类型" style="width: 100%">
                    <el-option
                      v-for="item in dataTypeOptions"
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
                <el-form-item label="单位">
                  <el-input v-model="form.unit" placeholder="请输入单位" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="小数位数">
                  <el-input-number
                    v-model="form.decimalPlaces"
                    :min="0"
                    :max="6"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="是否必填">
                  <el-switch v-model="form.isRequired" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="是否计算">
                  <el-switch v-model="form.isCalculated" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="是否启用">
                  <el-switch v-model="form.isEnabled" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="计算公式" name="formula">
            <el-form-item label="计算公式">
              <el-input
                v-model="form.formula"
                type="textarea"
                :rows="4"
                placeholder="请输入计算公式，如：[指标1] + [指标2] * 0.1"
              />
            </el-form-item>
            <el-form-item label="汇总方式">
              <el-select v-model="form.summaryMethod" placeholder="请选择汇总方式" style="width: 100%">
                <el-option label="求和" value="sum" />
                <el-option label="平均值" value="avg" />
                <el-option label="最大值" value="max" />
                <el-option label="最小值" value="min" />
                <el-option label="计数" value="count" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleValidateFormula">
                验证公式
              </el-button>
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="其他设置" name="other">
            <el-form-item label="默认值">
              <el-input-number v-model="form.defaultValue" style="width: 100%" />
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="最小值">
                  <el-input-number v-model="form.minValue" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="最大值">
                  <el-input-number v-model="form.maxValue" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="指标描述">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="3"
                placeholder="请输入指标描述"
              />
            </el-form-item>
            <el-form-item label="备注">
              <el-input
                v-model="form.remark"
                type="textarea"
                :rows="2"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog title="指标详情" :visible.sync="viewDialogVisible" width="700px">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="指标编码">{{ viewData.indicatorCode }}</el-descriptions-item>
        <el-descriptions-item label="指标名称">{{ viewData.indicatorName }}</el-descriptions-item>
        <el-descriptions-item label="指标类型">{{ formatIndicatorType(viewData.indicatorType) }}</el-descriptions-item>
        <el-descriptions-item label="数据类型">{{ formatDataType(viewData.dataType) }}</el-descriptions-item>
        <el-descriptions-item label="单位">{{ viewData.unit || '-' }}</el-descriptions-item>
        <el-descriptions-item label="小数位数">{{ viewData.decimalPlaces }}</el-descriptions-item>
        <el-descriptions-item label="是否必填">
          <el-tag :type="viewData.isRequired ? 'danger' : 'info'" size="mini">{{ viewData.isRequired ? '是' : '否' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否计算">
          <el-tag :type="viewData.isCalculated ? 'warning' : 'info'" size="mini">{{ viewData.isCalculated ? '是' : '否' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否启用">
          <el-tag :type="viewData.isEnabled ? 'success' : 'info'" size="mini">{{ viewData.isEnabled ? '启用' : '禁用' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="汇总方式">{{ formatSummaryMethod(viewData.summaryMethod) }}</el-descriptions-item>
        <el-descriptions-item label="默认值">{{ viewData.defaultValue != null ? viewData.defaultValue : '-' }}</el-descriptions-item>
        <el-descriptions-item label="最小值">{{ viewData.minValue != null ? viewData.minValue : '-' }}</el-descriptions-item>
        <el-descriptions-item label="最大值">{{ viewData.maxValue != null ? viewData.maxValue : '-' }}</el-descriptions-item>
        <el-descriptions-item label="计算公式" :span="2">{{ viewData.formula || '-' }}</el-descriptions-item>
        <el-descriptions-item label="指标描述" :span="2">{{ viewData.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

// ==================== 本地API调用方法 ====================
// 由于公共API文件路径不一致,在组件内部直接定义正确的API调用

/**
 * 创建指标
 */
function createIndicatorLocal(data) {
  return request({
    url: '/glkj/accountant/budget/indicator/create',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

/**
 * 查询指标详情
 */
function getIndicatorLocal(indicatorId) {
  return request({
    url: `/glkj/accountant/budget/indicator/detail/${indicatorId}`,
    method: 'get'
  })
}

/**
 * 更新指标
 */
function updateIndicatorLocal(indicatorId, data) {
  return request({
    url: `/glkj/accountant/budget/indicator/update/${indicatorId}`,
    method: 'put',
    headers: { 'Content-Type': 'application/json' },
    data
  })
}

/**
 * 删除指标
 */
function deleteIndicatorLocal(indicatorId) {
  return request({
    url: `/glkj/accountant/budget/indicator/delete/${indicatorId}`,
    method: 'delete'
  })
}

/**
 * 分页查询指标列表
 */
function getIndicatorPageLocal(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/indicator/page',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: { pageNum: current, pageSize: size, ...params }
  })
}

/**
 * 获取指标树
 */
function getIndicatorTreeLocal() {
  return request({
    url: '/glkj/accountant/budget/indicator/tree',
    method: 'get'
  })
}

/**
 * 批量删除指标
 */
function batchDeleteIndicatorsLocal(ids) {
  return request({
    url: '/glkj/accountant/budget/indicator/batch-delete',
    method: 'delete',
    headers: { 'Content-Type': 'application/json' },
    data: { ids }
  })
}

/**
 * 批量启用指标
 */
function batchEnableIndicatorsLocal(ids) {
  return request({
    url: '/glkj/accountant/budget/indicator/batch-enable',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: { ids }
  })
}

/**
 * 批量禁用指标
 */
function batchDisableIndicatorsLocal(ids) {
  return request({
    url: '/glkj/accountant/budget/indicator/batch-disable',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: { ids }
  })
}

/**
 * 启用指标
 */
function enableIndicatorLocal(indicatorId) {
  return request({
    url: `/glkj/accountant/budget/indicator/enable/${indicatorId}`,
    method: 'put'
  })
}

/**
 * 禁用指标
 */
function disableIndicatorLocal(indicatorId) {
  return request({
    url: `/glkj/accountant/budget/indicator/disable/${indicatorId}`,
    method: 'put'
  })
}

/**
 * 验证公式
 */
function validateFormulaLocal(formula) {
  return request({
    url: '/glkj/accountant/budget/indicator/validate-formula',
    method: 'post',
    headers: { 'Content-Type': 'application/json' },
    data: { formula }
  })
}

export default {
  name: 'IndicatorManagement',
  data() {
    return {
      loading: false,
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      viewMode: 'table', // table | tree
      activeTab: 'basic',
      viewDialogVisible: false,
      viewData: {},
      tableData: [],
      treeData: [],
      multipleSelection: [],
      queryForm: {
        indicatorName: '',
        indicatorCode: '',
        indicatorType: '',
        dataType: '',
        status: ''
      },
      form: {
        indicatorCode: '',
        indicatorName: '',
        indicatorType: '',
        dataType: '',
        unit: '',
        decimalPlaces: 2,
        formula: '',
        summaryMethod: '',
        defaultValue: null,
        minValue: null,
        maxValue: null,
        isRequired: false,
        isCalculated: false,
        isEnabled: true,
        description: '',
        remark: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      treeProps: {
        children: 'children',
        label: 'label'
      },
      indicatorTypeOptions: [
        { label: '收入指标', value: 'revenue' },
        { label: '成本指标', value: 'cost' },
        { label: '费用指标', value: 'expense' },
        { label: '利润指标', value: 'profit' },
        { label: '资产指标', value: 'asset' },
        { label: '负债指标', value: 'liability' },
        { label: '现金流指标', value: 'cashflow' },
        { label: '自定义指标', value: 'custom' }
      ],
      dataTypeOptions: [
        { label: '数值', value: 'number' },
        { label: '百分比', value: 'percentage' },
        { label: '金额', value: 'amount' },
        { label: '文本', value: 'text' }
      ],
      rules: {
        indicatorCode: [
          { required: true, message: '请输入指标编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        indicatorName: [
          { required: true, message: '请输入指标名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        indicatorType: [
          { required: true, message: '请选择指标类型', trigger: 'change' }
        ],
        dataType: [
          { required: true, message: '请选择数据类型', trigger: 'change' }
        ]
      }
    }
  },
  mounted() {
    this.loadData()
    this.loadTreeData()
  },
  methods: {
    // 加载表格数据
    async loadData() {
      this.loading = true
      try {
        const params = {}
        // 处理查询条件
        if (this.queryForm.indicatorName) {
          params.indicatorName = this.queryForm.indicatorName
        }
        if (this.queryForm.indicatorCode) {
          params.indicatorCode = this.queryForm.indicatorCode
        }
        if (this.queryForm.indicatorType) {
          params.indicatorType = this.queryForm.indicatorType
        }
        if (this.queryForm.dataType) {
          params.dataType = this.queryForm.dataType
        }
        if (this.queryForm.status) {
          params.isEnabled = this.queryForm.status === 'active' ? 1 : 0
        }

        const response = await getIndicatorPageLocal(
          this.pagination.current,
          this.pagination.size,
          params
        )
        if (response.code === 1) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 加载树形数据
    async loadTreeData() {
      try {
        const response = await getIndicatorTreeLocal()
        if (response.code === 1) {
          this.treeData = response.data || []
        }
      } catch (error) {
        this.$message.error('加载树形数据失败')
      }
    },

    // 查询
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.queryForm = {
        indicatorName: '',
        indicatorCode: '',
        indicatorType: '',
        dataType: '',
        status: ''
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增指标'
      this.isEdit = false
      this.activeTab = 'basic'
      this.resetForm()
      this.dialogVisible = true
    },

    // 编辑
    async handleEdit(row) {
      this.dialogTitle = '编辑指标'
      this.isEdit = true
      this.activeTab = 'basic'
      try {
        const indicatorId = row.indicatorId || row.id
        const response = await getIndicatorLocal(indicatorId)
        if (response.code === 1) {
          const data = { ...response.data }
          // 后端返回 0/1，el-switch 需要 boolean
          data.isRequired = !!data.isRequired
          data.isCalculated = !!data.isCalculated
          data.isEnabled = !!data.isEnabled
          this.form = data
          this.dialogVisible = true
        }
      } catch (error) {
        this.$message.error('获取数据失败')
      }
    },

    // 提交
    async handleSubmit() {
      try {
        await this.$refs.form.validate()

        // 后端实体 isRequired/isCalculated/isEnabled 是 Integer(0/1)，前端用 boolean，提交前转换
        const submitData = { ...this.form }
        submitData.isRequired = submitData.isRequired ? 1 : 0
        submitData.isCalculated = submitData.isCalculated ? 1 : 0
        submitData.isEnabled = submitData.isEnabled ? 1 : 0

        if (this.isEdit) {
          const response = await updateIndicatorLocal(submitData.indicatorId, submitData)
          if (response.code === 1) {
            this.$message.success('更新成功')
            this.dialogVisible = false
            this.loadData()
            this.loadTreeData()
          }
        } else {
          // 新增时剔除id/indicatorId，由后端自动生成
          delete submitData.id
          delete submitData.indicatorId
          const response = await createIndicatorLocal(submitData)
          if (response.code === 1) {
            this.$message.success('创建成功')
            this.dialogVisible = false
            this.loadData()
            this.loadTreeData()
          }
        }
      } catch (error) {
        // 表单验证失败或接口调用失败
      }
    },

    // 验证公式
    async handleValidateFormula() {
      if (!this.form.formula) {
        this.$message.warning('请先输入公式')
        return
      }
      
      try {
        const response = await validateFormulaLocal(this.form.formula)
        if (response.code === 1) {
          if (response.data && response.data.valid) {
            this.$message.success(response.data.message || '公式验证通过')
          } else {
            this.$message.error(response.data.message || '公式格式错误')
          }
        }
      } catch (error) {
        this.$message.error('验证公式失败')
      }
    },

    // 重置表单
    resetForm() {
      this.form = {
        indicatorCode: '',
        indicatorName: '',
        indicatorType: '',
        dataType: '',
        unit: '',
        decimalPlaces: 2,
        formula: '',
        summaryMethod: '',
        defaultValue: null,
        minValue: null,
        maxValue: null,
        isRequired: false,
        isCalculated: false,
        isEnabled: true,
        description: '',
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },

    // 格式化指标类型
    formatIndicatorType(type) {
      const option = this.indicatorTypeOptions.find(item => item.value === type)
      return option ? option.label : type
    },

    // 格式化数据类型
    formatDataType(type) {
      const option = this.dataTypeOptions.find(item => item.value === type)
      return option ? option.label : type
    },

    // 格式化汇总方式
    formatSummaryMethod(method) {
      const map = { sum: '求和', avg: '平均值', max: '最大值', min: '最小值', count: '计数' }
      return map[method] || method || '-'
    },

    // 获取指标类型标签类型
    getIndicatorTypeTagType(type) {
      const typeMap = {
        revenue: 'success',
        cost: 'warning',
        expense: 'danger',
        profit: 'primary',
        asset: 'info',
        liability: 'warning',
        cashflow: 'success',
        custom: 'default'
      }
      return typeMap[type] || 'default'
    },

    // 获取数据类型标签类型
    getDataTypeTagType(type) {
      const typeMap = {
        number: 'primary',
        percentage: 'success',
        amount: 'warning',
        text: 'info'
      }
      return typeMap[type] || 'default'
    },

    // 获取树节点图标
    getTreeNodeIcon(type) {
      const iconMap = {
        revenue: 'el-icon-money',
        cost: 'el-icon-shopping-cart-2',
        expense: 'el-icon-wallet',
        profit: 'el-icon-trophy',
        asset: 'el-icon-office-building',
        liability: 'el-icon-document',
        cashflow: 'el-icon-coin',
        custom: 'el-icon-setting'
      }
      return iconMap[type] || 'el-icon-document'
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadData()
    },

    // 当前页改变
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    // 选择改变
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 状态改变
    async handleStatusChange(row) {
      try {
        const indicatorId = row.indicatorId || row.id
        if (row.isEnabled) {
          await enableIndicatorLocal(indicatorId)
          this.$message.success('启用成功')
        } else {
          await disableIndicatorLocal(indicatorId)
          this.$message.success('禁用成功')
        }
      } catch (error) {
        // 恢复原状态
        row.isEnabled = !row.isEnabled
        this.$message.error('操作失败')
      }
    },

    // 查看详情
    async handleView(row) {
      try {
        const indicatorId = row.indicatorId || row.id
        const response = await getIndicatorLocal(indicatorId)
        if (response.code === 1) {
          this.viewData = response.data
          this.viewDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        this.$message.error('获取详情失败')
      }
    },

    // 复制
    async handleCopy(row) {
      try {
        await this.$confirm('确定要复制该指标吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        })
        const copyData = { ...row }
        delete copyData.indicatorId
        delete copyData.id
        copyData.indicatorCode = row.indicatorCode + '_copy'
        copyData.indicatorName = row.indicatorName + '(副本)'
        const response = await createIndicatorLocal(copyData)
        if (response.code === 1) {
          this.$message.success('复制成功')
          this.loadData()
          this.loadTreeData()
        } else {
          this.$message.error(response.msg || '复制失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('复制失败')
        }
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该指标吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const indicatorId = row.indicatorId || row.id
        const response = await deleteIndicatorLocal(indicatorId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
          this.loadTreeData()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },

    // 批量删除
    async handleBatchDelete() {
      try {
        await this.$confirm(`确定要删除选中的 ${this.multipleSelection.length} 条数据吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const ids = this.multipleSelection.map(item => item.indicatorId || item.id)
        const response = await batchDeleteIndicatorsLocal(ids)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
          this.loadTreeData()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败')
        }
      }
    },

    // 批量启用
    async handleBatchEnable() {
      try {
        const ids = this.multipleSelection.map(item => item.indicatorId || item.id)
        const response = await batchEnableIndicatorsLocal(ids)
        if (response.code === 1) {
          this.$message.success('批量启用成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量启用失败')
        }
      } catch (error) {
        this.$message.error('批量启用失败')
      }
    },

    // 批量禁用
    async handleBatchDisable() {
      try {
        const ids = this.multipleSelection.map(item => item.indicatorId || item.id)
        const response = await batchDisableIndicatorsLocal(ids)
        if (response.code === 1) {
          this.$message.success('批量禁用成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量禁用失败')
        }
      } catch (error) {
        this.$message.error('批量禁用失败')
      }
    },

    // 导出
    handleExport() {
      this.loading = true
      request({
        url: '/glkj/accountant/budget/indicator/export',
        method: 'get',
        responseType: 'blob'
      }).then((response) => {
        this.loading = false
        const blob = new Blob([response])
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = '预算指标数据.xlsx'
        link.click()
        window.URL.revokeObjectURL(link.href)
        this.$message.success('导出成功')
      }).catch(() => {
        this.loading = false
        this.$message.error('导出失败')
      })
    },

    // 导入
    handleImport() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls'
      input.onchange = (e) => {
        const file = e.target.files[0]
        if (!file) return
        const formData = new FormData()
        formData.append('file', file)
        this.loading = true
        request({
          url: '/glkj/accountant/budget/indicator/import',
          method: 'post',
          data: formData,
          headers: { 'Content-Type': 'multipart/form-data' }
        }).then((response) => {
          this.loading = false
          if (response.code === 1) {
            this.$message.success(response.msg || '导入成功')
            this.loadData()
            this.loadTreeData()
          } else {
            this.$message.error(response.msg || '导入失败')
          }
        }).catch(() => {
          this.loading = false
          this.$message.error('导入失败')
        })
      }
      input.click()
    },

    // 计算指标
    async handleCalculate(row) {
      if (!row.formula) {
        this.$message.warning('该指标未设置计算公式')
        return
      }
      try {
        const response = await validateFormulaLocal(row.formula)
        if (response.code === 1 && response.data && response.data.valid) {
          this.$message.success('公式计算验证通过')
        } else {
          this.$message.warning(response.data?.message || '公式验证未通过')
        }
      } catch (error) {
        this.$message.error('计算失败')
      }
    },

    // 切换视图模式
    handleViewModeChange(mode) {
      this.viewMode = mode
    },

    // 树节点点击
    handleTreeNodeClick(data) {
      // 点击树节点时可以加载详情
    },

    // 树节点选中变化
    handleTreeCheckChange() {
      // 树节点选中状态变化
    },

    // 树形视图 - 编辑
    handleTreeEdit(data) {
      this.handleEdit(data)
    },

    // 树形视图 - 新增子项
    handleTreeAdd(data) {
      this.dialogTitle = '新增子指标'
      this.isEdit = false
      this.resetForm()
      this.form.parentId = data.id
      this.dialogVisible = true
    },

    // 树形视图 - 删除
    async handleTreeDelete(data) {
      await this.handleDelete(data)
    },

    // 添加子节点（兼容旧方法名）
    handleAddChild(node, data) {
      this.handleTreeAdd(data)
    },

    // 编辑树节点（兼容旧方法名）
    handleEditNode(node, data) {
      this.handleEdit(data)
    },

    // 删除树节点（兼容旧方法名）
    async handleDeleteNode(node, data) {
      await this.handleDelete(data)
    }
  }
}
</script>

<style scoped>
.indicator-management {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-card,
.toolbar-card,
.table-card,
.tree-card {
  margin-bottom: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.tree-node-label {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tree-node-actions {
  display: none;
}

.custom-tree-node:hover .tree-node-actions {
  display: block;
}
</style>
