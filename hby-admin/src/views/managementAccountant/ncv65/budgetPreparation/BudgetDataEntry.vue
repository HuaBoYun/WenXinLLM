<template>
  <div class="budget-data-entry">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算数据录入</h2>
      <p>支持多种预算数据录入方式，包括手工录入、Excel导入、模板填报等</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增数据</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImport">Excel导入</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExport">导出数据</el-button>
            <el-button type="info" icon="el-icon-document-copy" @click="handleTemplate">下载模板</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button type="primary" icon="el-icon-check" @click="handleSubmit">提交审批</el-button>
            <el-button type="success" icon="el-icon-finished" @click="handleSave">保存草稿</el-button>
            <el-button icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="预算年度">
          <el-date-picker
            v-model="queryForm.fiscalYear"
            type="year"
            placeholder="选择预算年度"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="预算期间">
          <el-select
            v-model="queryForm.budgetPeriod"
            placeholder="请选择预算期间"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in budgetPeriodOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="组织机构">
          <el-cascader
            v-model="queryForm.organizationId"
            :options="organizationOptions"
            :props="{ checkStrictly: true, value: 'value', label: 'label' }"
            placeholder="请选择组织机构"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="预算科目">
          <el-cascader
            v-model="queryForm.budgetAccountId"
            :options="budgetAccountOptions"
            :props="{ checkStrictly: true, value: 'value', label: 'label' }"
            placeholder="请选择预算科目"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="数据状态">
          <el-select
            v-model="queryForm.dataStatus"
            placeholder="请选择数据状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in dataStatusOptions"
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

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算数据列表</span>
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
        :data="dataList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column prop="budgetYear" label="预算年度" width="100" align="center" sortable="custom" />
        <el-table-column prop="budgetPeriod" label="预算期间" width="120" align="center" />
        <el-table-column prop="organizationId" label="组织机构" width="150" show-overflow-tooltip>
          <template slot-scope="scope">{{ getOrganizationName(scope.row.organizationId) }}</template>
        </el-table-column>
        <el-table-column prop="indicatorName" label="预算科目" width="150" show-overflow-tooltip />

        <el-table-column prop="budgetValue" label="预算金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.budgetValue) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="actualValue" label="实际金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.actualValue) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="varianceValue" label="差异金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span :class="getVarianceClass(scope.row.varianceValue)">
              {{ formatAmount(scope.row.varianceValue) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column prop="dataStatus" label="数据状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.dataStatus)" size="mini">
              {{ getStatusText(scope.row.dataStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="createBy" label="录入人" width="100" show-overflow-tooltip />
        <el-table-column prop="createTime" label="录入时间" width="150" align="center" />
        
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >查看</el-button>
            <el-button
              v-if="scope.row.dataStatus === 'DRAFT'"
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              v-if="scope.row.dataStatus === 'DRAFT'"
              type="text"
              size="mini"
              icon="el-icon-delete"
              class="danger-text"
              @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="history" icon="el-icon-time">历史记录</el-dropdown-item>
                <el-dropdown-item command="audit" icon="el-icon-view">审计日志</el-dropdown-item>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="dataForm"
        :model="dataForm"
        :rules="dataRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算年度" prop="budgetYear">
              <el-date-picker
                v-model="dataForm.budgetYear"
                type="year"
                placeholder="选择预算年度"
                value-format="yyyy"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算期间" prop="budgetPeriod">
              <el-select
                v-model="dataForm.budgetPeriod"
                placeholder="请选择预算期间"
                style="width: 100%"
              >
                <el-option
                  v-for="item in budgetPeriodOptions"
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
            <el-form-item label="组织机构" prop="organizationId">
              <el-cascader
                v-model="dataForm.organizationId"
                :options="organizationOptions"
                :props="{ checkStrictly: true, value: 'value', label: 'label' }"
                placeholder="请选择组织机构"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算科目" prop="budgetAccountId">
              <el-cascader
                v-model="dataForm.budgetAccountId"
                :options="budgetAccountOptions"
                :props="{ checkStrictly: true, value: 'value', label: 'label' }"
                placeholder="请选择预算科目"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算金额" prop="budgetValue">
              <el-input
                v-model="dataForm.budgetValue"
                placeholder="请输入预算金额"
                type="number"
                step="0.01"
              >
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currency">
              <el-select
                v-model="dataForm.currency"
                placeholder="请选择币种"
                style="width: 100%"
              >
                <el-option
                  v-for="item in currencyOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="dataForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- Excel导入对话框 -->
    <el-dialog
      title="Excel数据导入"
      :visible.sync="importDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="import-content">
        <el-steps :active="importStep" finish-status="success">
          <el-step title="选择文件" />
          <el-step title="数据预览" />
          <el-step title="导入完成" />
        </el-steps>
        
        <div class="import-step-content">
          <!-- 步骤1：选择文件 -->
          <div v-if="importStep === 0" class="step-content">
            <el-upload
              ref="upload"
              :action="uploadUrl"
              :headers="uploadHeaders"
              :on-success="handleUploadSuccess"
              :on-error="handleUploadError"
              :before-upload="beforeUpload"
              :file-list="fileList"
              accept=".xlsx,.xls"
              drag
            >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
            </el-upload>
          </div>
          
          <!-- 步骤2：数据预览 -->
          <div v-if="importStep === 1" class="step-content">
            <div class="preview-info">
              <p>共检测到 <strong>{{ importData.length }}</strong> 条数据，其中：</p>
              <ul>
                <li>有效数据：<span class="success-text">{{ validCount }}</span> 条</li>
                <li>错误数据：<span class="danger-text">{{ errorCount }}</span> 条</li>
              </ul>
            </div>
            
            <el-table
              :data="importData.slice(0, 10)"
              border
              size="mini"
              max-height="300"
            >
              <el-table-column prop="fiscalYear" label="预算年度" width="100" />
              <el-table-column prop="budgetPeriod" label="预算期间" width="120" />
              <el-table-column prop="organizationName" label="组织机构" width="150" />
              <el-table-column prop="budgetAccountName" label="预算科目" width="150" />
              <el-table-column prop="budgetAmount" label="预算金额" width="120" />
              <el-table-column prop="errorMsg" label="错误信息" show-overflow-tooltip>
                <template slot-scope="scope">
                  <span v-if="scope.row.errorMsg" class="danger-text">{{ scope.row.errorMsg }}</span>
                  <span v-else class="success-text">正常</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
          
          <!-- 步骤3：导入完成 -->
          <div v-if="importStep === 2" class="step-content">
            <div class="import-result">
              <i class="el-icon-success" style="color: #67C23A; font-size: 48px;"></i>
              <h3>导入完成</h3>
              <p>成功导入 <strong>{{ importSuccessCount }}</strong> 条数据</p>
              <p v-if="importErrorCount > 0">失败 <strong>{{ importErrorCount }}</strong> 条数据</p>
            </div>
          </div>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button v-if="importStep > 0" @click="importStep--">上一步</el-button>
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button
          v-if="importStep === 1"
          type="primary"
          @click="handleConfirmImport"
        >确认导入</el-button>
        <el-button
          v-if="importStep === 2"
          type="primary"
          @click="handleImportComplete"
        >完成</el-button>
      </div>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog title="预算数据详情" :visible.sync="viewDialogVisible" width="700px">
      <el-descriptions :column="2" border size="small" v-if="viewRow">
        <el-descriptions-item label="预算年度">{{ viewRow.budgetYear }}</el-descriptions-item>
        <el-descriptions-item label="预算期间">{{ viewRow.budgetPeriod }}</el-descriptions-item>
        <el-descriptions-item label="组织机构">{{ getOrganizationName(viewRow.organizationId) }}</el-descriptions-item>
        <el-descriptions-item label="预算科目">{{ viewRow.indicatorName }}</el-descriptions-item>
        <el-descriptions-item label="预算金额">{{ formatAmount(viewRow.budgetValue) }}</el-descriptions-item>
        <el-descriptions-item label="币种">{{ viewRow.currency }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getStatusText(viewRow.dataStatus) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewRow.remark }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewRow.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ viewRow.updateTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer"><el-button @click="viewDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 历史记录弹窗 -->
    <el-dialog title="变更历史" :visible.sync="historyDialogVisible" width="800px">
      <el-table :data="historyList" border size="small" v-loading="historyLoading">
        <el-table-column prop="operationType" label="操作类型" width="100" />
        <el-table-column prop="operatorName" label="操作人" width="100" />
        <el-table-column prop="operateTime" label="操作时间" width="160" />
        <el-table-column prop="fieldName" label="字段" width="120" />
        <el-table-column prop="oldValue" label="变更前" show-overflow-tooltip />
        <el-table-column prop="newValue" label="变更后" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      </el-table>
      <div slot="footer"><el-button @click="historyDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 审计日志弹窗 -->
    <el-dialog title="审计日志" :visible.sync="auditDialogVisible" width="800px">
      <el-table :data="auditList" border size="small" v-loading="auditLoading">
        <el-table-column prop="operationType" label="操作类型" width="100" />
        <el-table-column prop="operatorName" label="操作人" width="100" />
        <el-table-column prop="operateTime" label="操作时间" width="160" />
        <el-table-column prop="ipAddress" label="IP地址" width="130" />
        <el-table-column prop="requestUrl" label="请求路径" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      </el-table>
      <div slot="footer"><el-button @click="auditDialogVisible = false">关闭</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetDataApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetDataEntry',
  data() {
    return {
      // 查询参数
      queryForm: {
        fiscalYear: null,
        budgetPeriod: '',
        organizationId: [],
        budgetAccountId: [],
        dataStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      dataList: [],
      total: 0,
      selectedRows: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      dataForm: {
        dataId: null,
        budgetYear: null,
        budgetPeriod: '',
        organizationId: [],
        budgetAccountId: [],
        budgetValue: null,
        currency: 'CNY',
        remark: ''
      },
      dataRules: {
        budgetYear: [
          { required: true, message: '请选择预算年度', trigger: 'change' }
        ],
        budgetPeriod: [
          { required: true, message: '请选择预算期间', trigger: 'change' }
        ],
        organizationId: [
          { required: true, message: '请选择组织机构', trigger: 'change' }
        ],
        budgetAccountId: [
          { required: true, message: '请选择预算科目', trigger: 'change' }
        ],
        budgetValue: [
          { required: true, message: '请输入预算金额', trigger: 'blur' }
        ]
      },
      
      // 导入相关
      importDialogVisible: false,
      importStep: 0,
      fileList: [],
      importData: [],
      validCount: 0,
      errorCount: 0,
      importSuccessCount: 0,
      importErrorCount: 0,
      uploadUrl: process.env.VUE_APP_BASE_API + '/accountant/ncv65/budget-data/import',
      uploadHeaders: {
        Authorization: 'Bearer ' + this.$store.getters.token
      },
      
      // 选项数据
      budgetPeriodOptions: [
        { value: 'Q1', label: '第一季度' },
        { value: 'Q2', label: '第二季度' },
        { value: 'Q3', label: '第三季度' },
        { value: 'Q4', label: '第四季度' },
        { value: 'M01', label: '1月' },
        { value: 'M02', label: '2月' },
        { value: 'M03', label: '3月' },
        { value: 'M04', label: '4月' },
        { value: 'M05', label: '5月' },
        { value: 'M06', label: '6月' },
        { value: 'M07', label: '7月' },
        { value: 'M08', label: '8月' },
        { value: 'M09', label: '9月' },
        { value: 'M10', label: '10月' },
        { value: 'M11', label: '11月' },
        { value: 'M12', label: '12月' }
      ],
      dataStatusOptions: [
        { value: 'DRAFT', label: '草稿' },
        { value: 'SUBMITTED', label: '已提交' },
        { value: 'APPROVED', label: '已批准' },
        { value: 'REJECTED', label: '已拒绝' }
      ],
      currencyOptions: [
        { value: 'CNY', label: '人民币' },
        { value: 'USD', label: '美元' },
        { value: 'EUR', label: '欧元' },
        { value: 'JPY', label: '日元' }
      ],
      organizationOptions: [],
      budgetAccountOptions: [],
      // 详情弹窗
      viewDialogVisible: false,
      viewRow: null,
      // 历史记录弹窗
      historyDialogVisible: false,
      historyList: [],
      historyLoading: false,
      // 审计日志弹窗
      auditDialogVisible: false,
      auditList: [],
      auditLoading: false
    }
  },
  
  created() {
    this.getList()
    this.loadOrganizations()
    this.loadBudgetAccounts()
  },
  
  methods: {
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        // 处理 fiscalYear：el-date-picker type=year 返回 Date 对象，转为年份数字
        let fiscalYear = this.queryForm.fiscalYear
        if (fiscalYear instanceof Date) {
          fiscalYear = fiscalYear.getFullYear()
        } else if (typeof fiscalYear === 'string' && fiscalYear.length >= 4) {
          fiscalYear = parseInt(fiscalYear.substring(0, 4))
        }
        // 处理 organizationId：cascader 返回数组，取最后一个元素
        let organizationId = this.queryForm.organizationId
        if (Array.isArray(organizationId) && organizationId.length > 0) {
          organizationId = organizationId[organizationId.length - 1]
        } else if (Array.isArray(organizationId) && organizationId.length === 0) {
          organizationId = null
        }
        const params = {
          ...this.queryForm,
          fiscalYear: fiscalYear || null,
          organizationId: organizationId || null,
          ...this.queryParams
        }
        const response = await budgetDataApi.getPage(params)
        if (response.code === 1) {
          this.dataList = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载组织机构选项
    async loadOrganizations() {
      try {
        const response = await budgetDataApi.getOrganizations()
        if (response.code === 1) {
          this.organizationOptions = response.data
        }
      } catch (error) {
        console.error('加载组织机构失败：', error)
      }
    },

    // 加载预算科目选项
    async loadBudgetAccounts() {
      try {
        const response = await budgetDataApi.getBudgetAccounts()
        if (response.code === 1) {
          this.budgetAccountOptions = response.data
        }
      } catch (error) {
        console.error('加载预算科目失败：', error)
      }
    },
    
    // 根据 organizationId 获取组织名称
    getOrganizationName(organizationId) {
      if (!organizationId) return ''
      const org = this.organizationOptions.find(o => o.value === organizationId)
      return org ? org.label : organizationId
    },

    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 重置
    handleReset() {
      this.queryForm = {
        fiscalYear: null,
        budgetPeriod: '',
        organizationId: [],
        budgetAccountId: [],
        dataStatus: ''
      }
      this.handleQuery()
    },
    
    // 新增
    handleAdd() {
      this.dialogTitle = '新增预算数据'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑预算数据'
      this.dialogVisible = true
      this.dataForm = {
        dataId: row.dataId,
        budgetYear: row.budgetYear != null ? String(row.budgetYear) : null,
        budgetPeriod: row.budgetPeriod,
        organizationId: row.organizationId ? [row.organizationId] : [],
        budgetAccountId: row.indicatorId ? [row.indicatorId] : [],
        budgetValue: row.budgetValue,
        currency: row.currency || 'CNY',
        remark: row.remark
      }
    },
    
    // 查看
    handleView(row) {
      this.viewRow = { ...row }
      this.viewDialogVisible = true
    },
    
    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该预算数据吗？', '提示', {
          type: 'warning'
        })
        const response = await budgetDataApi.delete(row.dataId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.getList()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    
    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.dataForm.validate()
        // 构建提交参数，将 cascader 数组取最后一个元素作为实际 ID
        const orgId = Array.isArray(this.dataForm.organizationId)
          ? this.dataForm.organizationId[this.dataForm.organizationId.length - 1]
          : this.dataForm.organizationId
        const accId = Array.isArray(this.dataForm.budgetAccountId)
          ? this.dataForm.budgetAccountId[this.dataForm.budgetAccountId.length - 1]
          : this.dataForm.budgetAccountId
        // budgetYear: value-format="yyyy" 返回字符串，转为数字提交
        let budgetYear = this.dataForm.budgetYear
        if (!budgetYear) {
          this.$message.error('请选择预算年度')
          return
        }
        budgetYear = parseInt(budgetYear)
        if (!orgId) {
          this.$message.error('请选择组织机构')
          return
        }
        if (!accId) {
          this.$message.error('请选择预算科目')
          return
        }
        const submitData = {
          ...this.dataForm,
          budgetYear,
          organizationId: orgId,
          indicatorId: accId,
          budgetAccountId: undefined
        }
        let response
        if (submitData.dataId) {
          response = await budgetDataApi.update(submitData.dataId, submitData)
        } else {
          response = await budgetDataApi.create(submitData)
        }
        if (response.code === 1) {
          this.$message.success(submitData.dataId ? '更新成功' : '创建成功')
          this.dialogVisible = false
          this.getList()
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        if (error && error.message) {
          this.$message.error('操作失败：' + error.message)
        }
      }
    },
    
    // 重置表单
    resetForm() {
      this.dataForm = {
        dataId: null,
        budgetYear: null,
        budgetPeriod: '',
        organizationId: [],
        budgetAccountId: [],
        budgetValue: null,
        currency: 'CNY',
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.dataForm && this.$refs.dataForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入Excel
    handleImport() {
      this.importDialogVisible = true
      this.importStep = 0
      this.fileList = []
      this.importData = []
    },
    
    // 上传成功
    handleUploadSuccess(response) {
      if (response.code === 1) {
        this.importData = response.data
        this.validCount = this.importData.filter(item => !item.errorMsg).length
        this.errorCount = this.importData.filter(item => item.errorMsg).length
        this.importStep = 1
      } else {
        this.$message.error('文件解析失败：' + response.message)
      }
    },
    
    // 上传失败
    handleUploadError() {
      this.$message.error('文件上传失败')
    },
    
    // 上传前检查
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                     file.type === 'application/vnd.ms-excel'
      const isLt10M = file.size / 1024 / 1024 < 10
      
      if (!isExcel) {
        this.$message.error('只能上传Excel文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('文件大小不能超过10MB!')
        return false
      }
      return true
    },
    
    // 确认导入
    async handleConfirmImport() {
      try {
        const validData = this.importData.filter(item => !item.errorMsg)
        const response = await budgetDataApi.batchImport(validData)
        this.importSuccessCount = response.data.successCount
        this.importErrorCount = response.data.errorCount
        this.importStep = 2
      } catch (error) {
        this.$message.error('导入失败：' + error.message)
      }
    },
    
    // 导入完成
    handleImportComplete() {
      this.importDialogVisible = false
      this.getList()
    },
    
    // 导出数据
    async handleExport() {
      try {
        const params = { ...this.queryForm }
        const response = await budgetDataApi.export(params)
        const blob = new Blob([response], { type: 'application/octet-stream' })
        const url = URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算数据.xlsx'
        link.click()
        URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 下载模板
    async handleTemplate() {
      try {
        const response = await budgetDataApi.downloadTemplate()
        const blob = new Blob([response], { type: 'application/octet-stream' })
        const url = URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算数据导入模板.xlsx'
        link.click()
        URL.revokeObjectURL(url)
        this.$message.success('模板下载成功')
      } catch (error) {
        this.$message.error('模板下载失败：' + error.message)
      }
    },
    
    // 提交审批
    async handleSubmit() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要提交的数据')
        return
      }
      
      try {
        await this.$confirm('确认提交选中的数据进行审批吗？', '提示', {
          type: 'warning'
        })
        const ids = this.selectedRows.map(row => row.dataId)
        const response = await budgetDataApi.batchSubmit({ ids })
        if (response.code === 1) {
          this.$message.success('提交成功')
        }
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('提交失败：' + error.message)
        }
      }
    },
    
    // 保存草稿
    async handleSave() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要保存的数据')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.dataId)
        const response = await budgetDataApi.batchSave(this.selectedRows)
        if (response.code === 1) {
          this.$message.success('保存成功')
        }
        this.getList()
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
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
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopy(row)
          break
        case 'history':
          this.handleHistory(row)
          break
        case 'audit':
          this.handleAudit(row)
          break
      }
    },
    
    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算数据'
      this.dialogVisible = true
      this.dataForm = {
        dataId: null,
        budgetYear: row.budgetYear,
        budgetPeriod: row.budgetPeriod,
        organizationId: row.organizationId ? [row.organizationId] : [],
        budgetAccountId: row.indicatorId ? [row.indicatorId] : [],
        budgetValue: row.budgetValue,
        currency: row.currency || 'CNY',
        remark: row.remark
      }
    },
    
    // 历史记录
    async handleHistory(row) {
      this.historyDialogVisible = true
      this.historyLoading = true
      this.historyList = []
      try {
        const response = await budgetDataApi.getHistory(row.dataId)
        if (response.code === 1) {
          this.historyList = response.data || []
        }
      } catch (e) {
        this.$message.error('加载历史记录失败')
      } finally {
        this.historyLoading = false
      }
    },

    // 审计日志
    async handleAudit(row) {
      this.auditDialogVisible = true
      this.auditLoading = true
      this.auditList = []
      try {
        const response = await budgetDataApi.getAuditLog(row.dataId)
        if (response.code === 1) {
          this.auditList = response.data || []
        }
      } catch (e) {
        this.$message.error('加载审计日志失败')
      } finally {
        this.auditLoading = false
      }
    },
    
    // 列设置
    handleColumnSetting() {
      this.$message.info('列设置功能开发中...')
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取差异样式
    getVarianceClass(variance) {
      if (!variance) return ''
      return parseFloat(variance) >= 0 ? 'success-text' : 'danger-text'
    },
    
    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'SUBMITTED': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'SUBMITTED': '已提交',
        'APPROVED': '已批准',
        'REJECTED': '已拒绝'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-data-entry {
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
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
  }
  
  .success-text {
    color: #67C23A;
  }
  
  .danger-text {
    color: #F56C6C;
  }
  
  .text-right {
    text-align: right;
  }
  
  .import-content {
    .import-step-content {
      margin-top: 30px;
      min-height: 300px;
      
      .step-content {
        padding: 20px 0;
      }
      
      .preview-info {
        margin-bottom: 20px;
        padding: 16px;
        background-color: #f5f7fa;
        border-radius: 4px;
        
        p {
          margin: 0 0 10px 0;
        }
        
        ul {
          margin: 0;
          padding-left: 20px;
          
          li {
            margin-bottom: 5px;
          }
        }
      }
      
      .import-result {
        text-align: center;
        padding: 40px 0;
        
        h3 {
          margin: 20px 0 10px 0;
          color: #303133;
        }
        
        p {
          margin: 5px 0;
          color: #606266;
        }
      }
    }
  }
}
</style>
