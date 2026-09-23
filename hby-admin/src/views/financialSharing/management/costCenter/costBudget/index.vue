<template>
  <div class="cost-budget-container">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="预算年度" prop="budgetYear">
          <el-date-picker
            v-model="searchForm.budgetYear"
            type="year"
            placeholder="请选择预算年度"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="成本中心" prop="centerId">
          <el-select
            v-model="searchForm.centerId"
            placeholder="请选择成本中心"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="item in costCenterOptions"
              :key="item.VALUE"
              :label="item.LABEL"
              :value="item.VALUE"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预算类型" prop="budgetType">
          <el-select
            v-model="searchForm.budgetType"
            placeholder="请选择预算类型"
            clearable
            style="width: 150px"
          >
            <el-option label="人力成本" value="HR" />
            <el-option label="办公费用" value="OFFICE" />
            <el-option label="研发投入" value="RD" />
            <el-option label="销售费用" value="SALES" />
            <el-option label="生产成本" value="PRODUCTION" />
            <el-option label="采购支出" value="PROCUREMENT" />
            <el-option label="分公司预算" value="BRANCH" />
            <el-option label="投资项目" value="INVEST" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算状态" prop="budgetStatus">
          <el-select
            v-model="searchForm.budgetStatus"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="草稿" :value="1" />
            <el-option label="待审批" :value="2" />
            <el-option label="已审批" :value="3" />
            <el-option label="执行中" :value="4" />
            <el-option label="已完成" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增预算</el-button>
      <el-button type="success" @click="handleBatchApprove" :disabled="!multipleSelection.length">
        批量审批
      </el-button>
      <el-button type="warning" @click="handleBudgetMonitor">预算监控</el-button>
      <el-button type="info" @click="handleBudgetAnalysis">预算分析</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
    </div>

    <!-- 预算执行概览 -->
    <div class="budget-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="overview-card budget-total">
            <div class="card-icon">
              <i class="el-icon-money"></i>
            </div>
            <div class="card-content">
              <div class="card-title">预算总额</div>
              <div class="card-value">{{ formatAmount(overview.budgetTotal) }}</div>
              <div class="card-desc">本年度预算</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card actual-total">
            <div class="card-icon">
              <i class="el-icon-coin"></i>
            </div>
            <div class="card-content">
              <div class="card-title">实际支出</div>
              <div class="card-value">{{ formatAmount(overview.actualTotal) }}</div>
              <div class="card-desc">累计支出</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card remaining-budget">
            <div class="card-icon">
              <i class="el-icon-wallet"></i>
            </div>
            <div class="card-content">
              <div class="card-title">剩余预算</div>
              <div class="card-value">{{ formatAmount(overview.remainingBudget) }}</div>
              <div class="card-desc">可用预算</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card execution-rate">
            <div class="card-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="card-content">
              <div class="card-title">执行率</div>
              <div class="card-value">{{ overview.executionRate }}%</div>
              <div class="card-desc">预算执行率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="budgetNo" label="预算编号" width="150" />
        <el-table-column prop="budgetYear" label="预算年度" width="100" />
        <el-table-column prop="costCenterName" label="成本中心" min-width="180" show-overflow-tooltip />
        <el-table-column prop="budgetTypeName" label="预算类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getBudgetTypeTag(scope.row.budgetType)">
              {{ scope.row.budgetTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.budgetAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="actualAmount" label="实际金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.actualAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="executionRate" label="执行率" width="100" align="center">
          <template slot-scope="scope">
            <el-progress 
              :percentage="scope.row.executionRate" 
              :color="getProgressColor(scope.row.executionRate)"
              :stroke-width="8"
            />
          </template>
        </el-table-column>
        <el-table-column prop="budgetStatusName" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.budgetStatus)">
              {{ scope.row.budgetStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button 
              size="mini" 
              type="success" 
              @click="handleApprove(scope.row)"
              v-if="scope.row.budgetStatus === 2"
            >
              审批
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </div>

    <!-- 新增/编辑预算对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算编号" prop="budgetNo">
              <el-input v-model="formData.budgetNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算年度" prop="budgetYear">
              <el-date-picker
                v-model="formData.budgetYear"
                type="year"
                placeholder="请选择预算年度"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="成本中心" prop="centerId">
              <el-select v-model="formData.centerId" placeholder="请选择成本中心" style="width: 100%">
                <el-option
                  v-for="item in costCenterOptions"
                  :key="item.VALUE"
                  :label="item.LABEL"
                  :value="item.VALUE"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算类型" prop="budgetType">
              <el-select v-model="formData.budgetType" placeholder="请选择预算类型" style="width: 100%">
                <el-option label="人力成本" value="HR" />
                <el-option label="办公费用" value="OFFICE" />
                <el-option label="研发投入" value="RD" />
                <el-option label="销售费用" value="SALES" />
                <el-option label="生产成本" value="PRODUCTION" />
                <el-option label="采购支出" value="PROCUREMENT" />
                <el-option label="分公司预算" value="BRANCH" />
                <el-option label="投资项目" value="INVEST" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算金额" prop="budgetAmount">
              <el-input-number
                v-model="formData.budgetAmount"
                placeholder="请输入预算金额"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算负责人" prop="budgetManagerName">
              <el-input
                v-model="formData.budgetManagerName"
                placeholder="请输入预算负责人姓名"
                clearable
                maxlength="50"
                show-word-limit
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="预算说明" prop="budgetDesc">
          <el-input
            v-model="formData.budgetDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入预算说明"
          />
        </el-form-item>
        <el-form-item label="控制策略" prop="controlStrategy">
          <el-radio-group v-model="formData.controlStrategy">
            <el-radio :label="1">严格控制</el-radio>
            <el-radio :label="2">预警控制</el-radio>
            <el-radio :label="3">无控制</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCostBudgetPage,
  saveOrUpdateCostBudget,
  deleteCostBudget,
  getCostBudgetStats,
  getCostCenterOptions,
  getCostBudgetDetail,
  batchApproveCostBudget,
  getCostBudgetMonitorData,
  getCostBudgetAnalysisData
} from '@/api/financialSharing/costCenter'
import { BigIntegerUtil } from '@/utils/bigInteger'

export default {
  name: 'CostBudget',
  data() {
    return {
      loading: false,
      searchForm: {
        budgetYear: null,
        centerId: null,
        budgetType: null,
        budgetStatus: null
      },
      tableData: [],
      multipleSelection: [],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      overview: {
        budgetTotal: 0,
        actualTotal: 0,
        remainingBudget: 0,
        executionRate: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      formData: {
        budgetId: '', // 使用字符串类型避免Long精度丢失
        budgetNo: '',
        budgetYear: '',
        centerId: '', // 使用字符串类型避免Long精度丢失
        budgetType: '',
        budgetAmount: 0,
        budgetManagerName: '', // 预算负责人姓名（自由输入）
        budgetDesc: '',
        controlStrategy: 1
      },
      costCenterOptions: [], // 成本中心选项
      formRules: {
        budgetYear: [
          { required: true, message: '请选择预算年度', trigger: 'change' }
        ],
        centerId: [
          { required: true, message: '请选择成本中心', trigger: 'change' }
        ],
        budgetType: [
          { required: true, message: '请选择预算类型', trigger: 'change' }
        ],
        budgetAmount: [
          { required: true, message: '请输入预算金额', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadData()
    this.loadOverview()
    this.loadCostCenterOptions()
  },
  methods: {
    // 辅助方法：确保ID为字符串类型
    ensureStringId(id) {
      if (id === null || id === undefined) return ''
      return String(id).trim()
    },

    // 辅助方法：验证并转换ID为字符串
    validateAndConvertId(id, fieldName) {
      const strId = this.ensureStringId(id)
      if (strId && strId !== 'null' && strId !== 'undefined') {
        // 检查是否为大整数，记录警告
        BigIntegerUtil.checkAndLogWarning(strId, fieldName)
        return strId
      }
      return ''
    },

    async loadData() {
      this.loading = true
      try {
        // 获取用户上下文信息，确保请求参数格式正确
        const { bookId, tenantId } = this.getUserContext()

        // 确保成本中心ID使用字符串类型传递，避免JavaScript大整数精度丢失
        let centerId = this.searchForm.centerId
        if (centerId && Array.isArray(centerId)) {
          centerId = centerId.map(id => String(id))
        } else if (centerId && centerId !== 'null') {
          centerId = String(centerId)
        } else {
          centerId = undefined // 不传递 undefined 或 null 值
        }

        // 处理预算年度，避免传递 null 值
        let budgetYear = ''
        if (this.searchForm.budgetYear && this.searchForm.budgetYear !== 'null') {
          budgetYear = new Date(this.searchForm.budgetYear).getFullYear().toString()
        }

        // 处理预算类型，避免传递 null 值
        const budgetType = this.searchForm.budgetType && this.searchForm.budgetType !== 'null' ?
                          this.searchForm.budgetType : undefined

        // 处理预算状态，避免传递 null 值
        const budgetStatus = this.searchForm.budgetStatus && this.searchForm.budgetStatus !== 'null' ?
                           this.searchForm.budgetStatus : undefined

        const params = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          bookId: Number(bookId) || 1,
          tenantId: Number(tenantId) || 1000
        }

        // 只添加有效的非空参数，并确保类型正确
        if (budgetYear && budgetYear !== 'null' && budgetYear !== '') {
          params.budgetYear = budgetYear
        }
        if (centerId && centerId !== 'null' && centerId !== '') {
          params.centerId = String(centerId) // 确保为字符串类型
        }
        if (budgetType !== undefined && budgetType !== null && budgetType !== 'null') {
          params.budgetType = String(budgetType) // 成本分类是字符串业务码（HR/OFFICE/RD/...）
        }
        if (budgetStatus !== undefined && budgetStatus !== null && budgetStatus !== 'null') {
          params.budgetStatus = Number(budgetStatus) // 状态是数字
        }

        // 添加请求前日志验证
        console.log('=== 成本预算查询调试 ===')
        console.log('1. 发送查询参数:', params)
        console.log('2. 用户上下文 bookId:', bookId, 'tenantId:', tenantId)
        console.log('3. 搜索表单:', this.searchForm)
        console.log('4. 成本中心ID类型:', typeof centerId, '值:', centerId)
        console.log('5. 最终参数类型检查:', {
          budgetYear: typeof params.budgetYear,
          centerId: typeof params.centerId,
          budgetType: typeof params.budgetType,
          budgetStatus: typeof params.budgetStatus
        })

        const response = await getCostBudgetPage(params)
        console.log('6. 接收API响应:', response)

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          console.log('7. 响应码正确，处理数据')
          console.log('8. 原始tlist数据:', response.data.tlist)
          console.log('9. 原始totalRecord:', response.data.totalRecord)

          const pageResult = response.data
          // 数据映射：后端字段 -> 前端字段（兼容大小写字段名）
          this.tableData = (pageResult.tlist || []).map(item => {
            // 兼容小驼峰和大写字段名
            const getFieldValue = (camelCase, upperCase) => {
              return item[camelCase] !== undefined ? item[camelCase] :
                     item[upperCase] !== undefined ? item[upperCase] : 0
            }

            // 使用工具类安全处理ID字段，并添加字符串版本用于API调用
            const budgetId = BigIntegerUtil.safeGetId(item, ['budgetId', 'BUDGET_ID', 'BUDGETID'])
            const centerId = BigIntegerUtil.safeGetId(item, ['centerId', 'CENTER_ID', 'CENTERID'])

            // 检查并记录大整数警告
            BigIntegerUtil.checkAndLogWarning(budgetId, `预算记录-${budgetId}`)
            BigIntegerUtil.checkAndLogWarning(centerId, `成本中心ID-${centerId}`)

            console.log('10. 处理预算项 - 原始budgetId:', item.budgetId || item.BUDGET_ID, '处理后:', budgetId)
            console.log('11. 处理预算项 - 原始centerId:', item.centerId || item.CENTER_ID, '处理后:', centerId)

            return {
              // 使用字符串类型处理ID，避免精度丢失
              budgetId: budgetId,
              budgetIdStr: budgetId, // 字符串版本，用于API调用
              budgetNo: 'BUDGET' + (item.budgetYear || item.BUDGETYEAR || '') +
                        String(centerId).padStart(3, '0'), // 生成预算编号
              budgetYear: item.budgetYear || item.BUDGETYEAR,
              costCenterName: item.centerName || item.CENTERNAME,
              budgetTypeName: this.getBudgetTypeName(item.costCategory || item.COSTCATEGORY),
              budgetAmount: getFieldValue('totalBudget', 'TOTALBUDGET'),
              actualAmount: getFieldValue('actualCost', 'ACTUALCOST'),
              executionRate: this.calculateExecutionRate(
                getFieldValue('actualCost', 'ACTUALCOST'),
                getFieldValue('totalBudget', 'TOTALBUDGET')
              ),
              budgetStatusName: this.getBudgetStatusName(item.status || item.STATUS),
              budgetStatus: item.status || item.STATUS,
              // 使用字符串类型处理ID，避免精度丢失
              centerId: centerId,
              centerIdStr: centerId, // 字符串版本，用于API调用
              centerCode: item.centerCode || item.CENTERCODE,
              totalBudget: getFieldValue('totalBudget', 'TOTALBUDGET'),
              actualCost: getFieldValue('actualCost', 'ACTUALCOST'),
              remainingBudget: getFieldValue('remainingBudget', 'REMAININGBUDGET'),
              costCategory: item.costCategory || item.COSTCATEGORY,
              remark: item.remark || item.REMARK,
              budgetManager: item.budgetManager || item.BUDGETMANAGER || '',
              controlStrategy: item.controlStrategy || item.CONTROLSTRATEGY || 1,
              createTime: item.createTime || item.CREATETIME,
              updateTime: item.updateTime || item.UPDATETIME
            }
          })
          this.pagination.total = pageResult.totalRecord || 0

          console.log('12. 赋值后 tableData 长度:', this.tableData.length)
          console.log('13. 赋值后 pagination.total:', this.pagination.total)
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('查询成本预算列表失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadOverview() {
      try {
        const params = {
          period: new Date().getFullYear().toString()
        }
        const response = await getCostBudgetStats(params)
        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          const stats = response.data
          // 兼容大小写字段名
          this.overview = {
            budgetTotal: stats.totalBudget || stats.TOTALBUDGET || 0,
            actualTotal: stats.actualCost || stats.ACTUALCOST || 0,
            remainingBudget: stats.remainingBudget || stats.REMAININGBUDGET || 0,
            executionRate: this.calculateExecutionRate(
              stats.actualCost || stats.ACTUALCOST || 0,
              stats.totalBudget || stats.TOTALBUDGET || 1
            )
          }
        } else {
          console.error('获取预算统计数据失败:', response.msg)
          // 设置默认值防止页面报错
          this.overview = {
            budgetTotal: 0,
            actualTotal: 0,
            remainingBudget: 0,
            executionRate: 0
          }
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
        // 设置默认值防止页面报错
        this.overview = {
          budgetTotal: 0,
          actualTotal: 0,
          remainingBudget: 0,
          executionRate: 0
        }
      }
    },
    // 获取用户信息中的bookId和tenantId
    getUserContext() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')

        let bookId = userInfo.currentBook?.bookId ||
                     userInfo.bookId ||
                     userInfo.bookInfo?.bookId ||
                     1

        let tenantId = userInfo.currentOrg?.orgid ||
                       userInfo.tenantId ||
                       userInfo.orgId ||
                       1000

        return { bookId, tenantId }
      } catch (error) {
        console.error('获取用户上下文失败:', error)
        return { bookId: 1, tenantId: 1000 }
      }
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    handleReset() {
      // 手动重置表单字段，确保正确处理 null 值
      this.searchForm = {
        budgetYear: null,
        centerId: null,
        budgetType: null,
        budgetStatus: null
      }
      // 重置后立即搜索
      this.handleSearch()
    },
    handleAdd() {
      this.dialogTitle = '新增预算'
      // 初始化表单数据，确保所有ID字段为字符串类型
      this.formData = {
        budgetId: '', // 明确使用空字符串，避免Long精度丢失
        budgetNo: '',
        budgetYear: '',
        centerId: '', // 明确使用空字符串，避免Long精度丢失
        budgetType: '',
        budgetAmount: 0,
        budgetManagerName: '',
        budgetDesc: '',
        controlStrategy: 1
      }
      this.dialogVisible = true

      // 添加调试日志
      console.log('新增预算 - 初始化表单数据:', this.formData)
      console.log('budgetId类型:', typeof this.formData.budgetId)
      console.log('centerId类型:', typeof this.formData.centerId)
    },
    handleEdit(row) {
      this.dialogTitle = '编辑预算'

      // 使用辅助方法确保ID为字符串类型
      const budgetId = this.validateAndConvertId(row.budgetId || row.budgetIdStr, '编辑预算-budgetId')
      const centerId = this.validateAndConvertId(row.centerId || row.centerIdStr, '编辑预算-centerId')

      // 数据映射：前端字段 -> 后端字段，确保ID为字符串类型
      // 编辑时优先回填 DB 实际的 COST_CATEGORY 值（字符串业务码 HR/OFFICE/RD/...）
      this.formData = {
        budgetId: budgetId, // 明确确保为字符串类型
        budgetNo: row.budgetNo || '',
        budgetYear: row.budgetYear || '',
        centerId: centerId, // 明确确保为字符串类型
        budgetType: row.costCategory || row.COSTCATEGORY || row.budgetType || '',
        budgetAmount: row.totalBudget || row.budgetAmount || 0, // 兼容不同字段名
        budgetManagerName: row.budgetManager || row.BUDGETMANAGER || row.budgetManagerName || '',
        budgetDesc: row.remark || row.budgetDesc || '',
        controlStrategy: row.controlStrategy || row.CONTROLSTRATEGY || 1
      }

      // 添加调试日志
      console.log('编辑预算 - 原始数据:', row)
      console.log('编辑预算 - 表单数据:', this.formData)
      console.log('budgetId类型:', typeof this.formData.budgetId, '值:', this.formData.budgetId)
      console.log('centerId类型:', typeof this.formData.centerId, '值:', this.formData.centerId)

      this.dialogVisible = true
    },
    async handleView(row) {
      try {
        const budgetId = this.validateAndConvertId(row.budgetId || row.budgetIdStr, '查看预算-budgetId')
        if (!budgetId) {
          this.$message.error('无效的预算ID')
          return
        }

        console.log('=== 查看预算详情调试 ===')
        console.log('预算ID:', budgetId)
        console.log('原始行数据:', row)

        // 获取预算详情数据
        const response = await getCostBudgetDetail(budgetId)
        console.log('API响应:', response)
        console.log('响应数据详情:', response.data)

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          const budgetDetail = response.data
          console.log('预算详情数据:', budgetDetail)

          // 生成预算编号
          const budgetNo = 'BUDGET' + (budgetDetail.BUDGETYEAR || '') + (budgetDetail.CENTERID || '').slice(-3)

          // 显示查看详情对话框，使用数据库返回的大写字段名
          // 兼容大写连写（达梦默认 alias 行为）和小驼峰（带双引号 alias）
          const pick = (lower, upper) => budgetDetail[lower] !== undefined && budgetDetail[lower] !== null
            ? budgetDetail[lower]
            : budgetDetail[upper]
          const controlStrategyMap = { 1: '严格控制', 2: '预警控制', 3: '无控制' }
          const strategyVal = pick('controlStrategy', 'CONTROLSTRATEGY')
          this.$alert(`
            <div style="text-align: left;">
              <p><strong>预算编号：</strong>${budgetNo}</p>
              <p><strong>预算ID：</strong>${pick('budgetId', 'BUDGETID') || '无'}</p>
              <p><strong>成本中心ID：</strong>${pick('centerId', 'CENTERID') || '无'}</p>
              <p><strong>成本中心编码：</strong>${pick('centerCode', 'CENTERCODE') || '无'}</p>
              <p><strong>成本中心名称：</strong>${pick('centerName', 'CENTERNAME') || '未关联'}</p>
              <p><strong>预算年度：</strong>${pick('budgetYear', 'BUDGETYEAR') || '无'}</p>
              <p><strong>预算季度：</strong>${pick('budgetQuarter', 'BUDGETQUARTER') || '无'}</p>
              <p><strong>预算类型：</strong>${this.getBudgetTypeName(pick('costCategory', 'COSTCATEGORY'))}</p>
              <p><strong>预算金额：</strong>${this.formatAmount(pick('totalBudget', 'TOTALBUDGET') || 0)}</p>
              <p><strong>实际支出：</strong>${this.formatAmount(pick('actualCost', 'ACTUALCOST') || 0)}</p>
              <p><strong>剩余预算：</strong>${this.formatAmount(pick('remainingBudget', 'REMAININGBUDGET') || 0)}</p>
              <p><strong>执行率：</strong>${this.calculateExecutionRate(
                parseFloat(pick('actualCost', 'ACTUALCOST') || 0),
                parseFloat(pick('totalBudget', 'TOTALBUDGET') || 1)
              )}%</p>
              <p><strong>预算状态：</strong>${this.getBudgetStatusName(pick('status', 'STATUS'))}</p>
              <p><strong>预算负责人：</strong>${pick('budgetManager', 'BUDGETMANAGER') || '未指定'}</p>
              <p><strong>控制策略：</strong>${controlStrategyMap[strategyVal] || '未设置'}</p>
              <p><strong>预算说明：</strong>${pick('remark', 'REMARK') || '无'}</p>
              <p><strong>创建时间：</strong>${pick('createTime', 'CREATETIME') || '无'}</p>
              <p><strong>更新时间：</strong>${pick('updateTime', 'UPDATETIME') || '无'}</p>
            </div>
          `, '预算详情', {
            dangerouslyUseHTMLString: true,
            customClass: 'budget-detail-dialog'
          })
        } else {
          console.error('API返回错误:', response)
          this.$message.error(response.msg || '获取预算详情失败')
        }
      } catch (error) {
        console.error('查看预算详情失败:', error)
        this.$message.error('查看详情失败: ' + error.message)
      }
    },
    handleApprove(row) {
      this.$confirm('确认审批该预算吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('审批成功')
        this.loadData()
      })
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该预算吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 使用辅助方法确保ID为字符串类型
        const budgetId = this.validateAndConvertId(row.budgetId || row.budgetIdStr, '删除预算-budgetId')

        if (!budgetId) {
          this.$message.error('无效的预算ID')
          return
        }

        console.log('删除预算，传递的ID:', budgetId, '(类型:', typeof budgetId, ')')
        const response = await deleteCostBudget(budgetId)
        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success('删除成功')
          this.loadData()
          this.loadOverview()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除成本预算失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    async handleBatchApprove() {
      try {
        if (this.multipleSelection.length === 0) {
          this.$message.warning('请选择要审批的记录')
          return
        }

        await this.$confirm(`确认审批选中的 ${this.multipleSelection.length} 条预算记录吗？`, '批量审批', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 使用辅助方法确保ID为字符串类型，避免JavaScript大整数精度丢失
        const budgetIds = this.multipleSelection.map(item => {
          return this.validateAndConvertId(item.budgetId || item.budgetIdStr, `批量审批-budgetId`)
        }).filter(id => id) // 过滤掉空的ID

        const auditData = {
          auditResult: 'approved',
          auditRemark: '批量审批通过'
        }

        console.log('=== 批量预算审批调试信息 ===')
        console.log('选中的记录数量:', this.multipleSelection.length)
        console.log('预算IDs:', budgetIds)
        console.log('审批数据:', auditData)

        this.loading = true
        const response = await batchApproveCostBudget(budgetIds, auditData)

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success(`批量审批成功：共审批通过 ${budgetIds.length} 条记录`)
          this.loadData()
          this.loadOverview()
        } else {
          this.$message.error(response.msg || '批量审批失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量审批失败:', error)
          this.$message.error('批量审批失败')
        }
      } finally {
        this.loading = false
      }
    },
    async handleBatchDelete() {
      try {
        if (this.multipleSelection.length === 0) {
          this.$message.warning('请选择要删除的记录')
          return
        }

        await this.$confirm(`确认删除选中的 ${this.multipleSelection.length} 条预算记录吗？此操作不可恢复！`, '批量删除', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        })

        // 使用辅助方法确保ID为字符串类型，避免JavaScript大整数精度丢失
        const budgetIds = this.multipleSelection.map(item => {
          return this.validateAndConvertId(item.budgetId || item.budgetIdStr, `批量删除-budgetId`)
        }).filter(id => id) // 过滤掉空的ID

        // 暂未对接批量删除 API，逐个调用单个删除接口
        const deletePromises = budgetIds.map(id => deleteCostBudget(id))

        this.loading = true
        await Promise.all(deletePromises)

        this.$message.success(`批量删除成功，共删除 ${budgetIds.length} 条记录`)
        this.loadData()
        this.loadOverview()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      } finally {
        this.loading = false
      }
    },
    /**
     * 构建 monitor / analysis 请求参数：
     *   1) 优先传选中行 budgetIds（如果用户勾选）
     *   2) 否则传当前筛选条件（budgetYear / centerId / budgetType / budgetStatus）
     *   3) 都没有 = 全公司全年度统计
     */
    buildStatsRequestParams() {
      const ctx = this.getUserContext()
      const params = {
        bookId: ctx.bookId,
        tenantId: ctx.tenantId
      }

      // 选中行优先（局部统计）
      if (this.multipleSelection && this.multipleSelection.length > 0) {
        params.budgetIds = this.multipleSelection.map(r => String(r.budgetId || r.budgetIdStr || ''))
          .filter(id => id && id !== 'null')
      }

      // 当前筛选条件（无勾选时按筛选范围统计）
      if (this.searchForm.budgetYear) {
        // budgetYear 可能是日期对象（el-date-picker）或字符串
        const year = typeof this.searchForm.budgetYear === 'string'
          ? this.searchForm.budgetYear
          : new Date(this.searchForm.budgetYear).getFullYear().toString()
        if (year && year !== 'null') params.budgetYear = year
      }
      if (this.searchForm.centerId) {
        params.centerId = String(this.searchForm.centerId)
      }
      if (this.searchForm.budgetType) {
        params.budgetType = String(this.searchForm.budgetType)
      }
      if (this.searchForm.budgetStatus !== null && this.searchForm.budgetStatus !== undefined && this.searchForm.budgetStatus !== '') {
        params.budgetStatus = Number(this.searchForm.budgetStatus)
      }
      return params
    },
    async handleBudgetMonitor() {
      try {
        const params = this.buildStatsRequestParams()
        console.log('[预算监控] 请求参数:', params)

        this.loading = true
        const response = await getCostBudgetMonitorData(params)

        if (response.code === 1) {
          const monitorData = response.data
          // 标题描述：选中 / 筛选 / 全部
          let scopeDesc = '全公司全年度'
          if (params.budgetIds && params.budgetIds.length > 0) {
            scopeDesc = `已选 ${params.budgetIds.length} 个预算`
          } else if (params.budgetYear || params.centerId || params.budgetType || params.budgetStatus !== undefined) {
            scopeDesc = '当前筛选条件'
          }

          this.$alert(`
            <div style="text-align: left;">
              <h3>预算执行监控</h3>
              <p><strong>统计范围：</strong>${scopeDesc}</p>
              <p><strong>预算总额：</strong>${this.formatAmount(monitorData.totalBudget || 0)}</p>
              <p><strong>实际支出：</strong>${this.formatAmount(monitorData.actualCost || 0)}</p>
              <p><strong>剩余预算：</strong>${this.formatAmount(monitorData.remainingBudget || 0)}</p>
              <p><strong>平均执行率：</strong>${monitorData.avgExecutionRate || 0}%</p>
              <p><strong>预算总数：</strong>${monitorData.budgetCount || 0}</p>
              <p><strong>预警中心数量：</strong>${monitorData.warningCount || 0}</p>
              <p><strong>超支中心数量：</strong>${monitorData.overBudgetCount || 0}</p>
            </div>
          `, '预算监控', {
            dangerouslyUseHTMLString: true,
            customClass: 'budget-monitor-dialog'
          })
        } else {
          this.$message.error(response.msg || '获取监控数据失败')
        }
      } catch (error) {
        console.error('预算监控失败:', error)
        this.$message.error('预算监控功能暂时不可用')
      } finally {
        this.loading = false
      }
    },
    async handleBudgetAnalysis() {
      try {
        const params = this.buildStatsRequestParams()
        console.log('[预算分析] 请求参数:', params)

        this.loading = true
        const response = await getCostBudgetAnalysisData(params)

        if (response.code === 1) {
          const analysisData = response.data
          let scopeDesc = '全公司全年度'
          if (params.budgetIds && params.budgetIds.length > 0) {
            scopeDesc = `已选 ${params.budgetIds.length} 个预算`
          } else if (params.budgetYear || params.centerId || params.budgetType || params.budgetStatus !== undefined) {
            scopeDesc = '当前筛选条件'
          }

          this.$alert(`
            <div style="text-align: left;">
              <h3>预算分析报告</h3>
              <p><strong>分析范围：</strong>${scopeDesc}</p>
              <p><strong>预算完成率：</strong>${analysisData.budgetCompletionRate || 0}%</p>
              <p><strong>成本控制效果：</strong>${analysisData.costControlEffect || '良好'}</p>
              <p><strong>预算偏差率：</strong>${analysisData.budgetVarianceRate || 0}%</p>
              <p><strong>预算优化建议：</strong>${analysisData.optimizationSuggestions || '建议加强预算执行监控'}</p>
              <p><strong>下期预算建议：</strong>${analysisData.nextPeriodAdvice || '建议基于本期实际情况调整'}</p>
            </div>
          `, '预算分析', {
            dangerouslyUseHTMLString: true,
            customClass: 'budget-analysis-dialog'
          })
        } else {
          this.$message.error(response.msg || '获取分析数据失败')
        }
      } catch (error) {
        console.error('预算分析失败:', error)
        this.$message.error('预算分析功能暂时不可用')
      } finally {
        this.loading = false
      }
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },
    async handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          try {
            // 创建提交数据副本，确保ID字段为字符串类型
            const formData = {
              ...this.formData,
              // 使用辅助方法确保ID为字符串类型
              budgetId: this.ensureStringId(this.formData.budgetId),
              centerId: this.ensureStringId(this.formData.centerId)
            }

            // 处理预算年份
            if (formData.budgetYear && typeof formData.budgetYear !== 'string') {
              formData.budgetYear = new Date(formData.budgetYear).getFullYear().toString()
            }

            // 处理预算金额，确保为数值类型
            if (formData.budgetAmount && typeof formData.budgetAmount === 'string') {
              formData.budgetAmount = parseFloat(formData.budgetAmount) || 0
            }

            // 使用辅助方法验证并记录ID
            if (formData.budgetId) {
              this.validateAndConvertId(formData.budgetId, '提交预算-budgetId')
            }
            if (formData.centerId) {
              this.validateAndConvertId(formData.centerId, '提交预算-centerId')
            }

            // 添加调试日志
            console.log('=== 提交预算表单数据 ===')
            console.log('原始表单数据:', this.formData)
            console.log('处理后的提交数据:', formData)
            console.log('budgetId类型:', typeof formData.budgetId, '值:', formData.budgetId)
            console.log('centerId类型:', typeof formData.centerId, '值:', formData.centerId)
            console.log('budgetAmount类型:', typeof formData.budgetAmount, '值:', formData.budgetAmount)

            const response = await saveOrUpdateCostBudget(formData)
            console.log('后端响应:', response)
            if (response.code === 1) {
              // axios拦截器会将后端的 code=1 转换为 code=200
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.loadData()
              this.loadOverview()
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          } catch (error) {
            console.error('保存成本预算失败:', error)
            this.$message.error('保存失败')
          }
        }
      })
    },
    handleDialogClose() {
      this.$refs.formRef.resetFields()
    },
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    getBudgetTypeTag(type) {
      const tagMap = {
        // 数字预算周期类型（历史兼容）
        1: 'primary',
        2: 'success',
        3: 'warning',
        // 字符串成本分类（DB COST_CATEGORY 实际值）
        HR: 'primary',
        OFFICE: 'success',
        RD: 'warning',
        SALES: 'danger',
        PRODUCTION: 'info',
        PROCUREMENT: 'primary',
        BRANCH: 'success',
        INVEST: 'warning'
      }
      return tagMap[type] || 'info'
    },
    getStatusTag(status) {
      const tagMap = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'primary',
        5: 'success'
      }
      return tagMap[status] || 'info'
    },
    async loadCostCenterOptions() {
      try {
        const { bookId, tenantId } = this.getUserContext()
        const response = await getCostCenterOptions({ bookId, tenantId })
        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.costCenterOptions = response.data || []
        } else {
          console.error('获取成本中心选项失败:', response.msg)
        }
      } catch (error) {
        console.error('加载成本中心选项失败:', error)
      }
    },
    getProgressColor(percentage) {
      if (percentage < 50) return '#67C23A'
      if (percentage < 80) return '#E6A23C'
      return '#F56C6C'
    },
    // 获取预算类型名称（兼容数字预算类型 + 字符串成本分类）
    getBudgetTypeName(type) {
      const typeMap = {
        // 数字类型（前端 dropdown）
        1: '年度预算',
        2: '季度预算',
        3: '月度预算',
        // 字符串成本分类（后端 COST_CATEGORY 实际数据）
        HR: '人力成本',
        OFFICE: '办公费用',
        RD: '研发投入',
        SALES: '销售费用',
        PRODUCTION: '生产成本',
        PROCUREMENT: '采购支出',
        BRANCH: '分公司预算',
        INVEST: '投资项目'
      }
      return typeMap[type] || (type ? String(type) : '未知类型')
    },
    // 获取预算状态名称
    getBudgetStatusName(status) {
      const statusMap = {
        1: '草稿',
        2: '待审批',
        3: '已审批',
        4: '执行中',
        5: '已完成'
      }
      return statusMap[status] || '未知状态'
    },
    // 计算执行率
    calculateExecutionRate(actualCost, totalBudget) {
      if (!totalBudget || totalBudget === 0) return 0
      const rate = (actualCost / totalBudget) * 100
      return Math.round(Math.min(rate, 100)) // 限制最大100%
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-budget-container {
  padding: 20px;
}

.search-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
  
  .el-button {
    margin-right: 10px;
  }
}

.budget-overview {
  margin-bottom: 20px;
  
  .overview-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    
    .card-icon {
      width: 50px;
      height: 50px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      
      i {
        font-size: 24px;
        color: white;
      }
    }
    
    .card-content {
      .card-title {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }
      
      .card-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }
      
      .card-desc {
        font-size: 12px;
        color: #C0C4CC;
      }
    }
    
    &.budget-total .card-icon {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    
    &.actual-total .card-icon {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }
    
    &.remaining-budget .card-icon {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }
    
    &.execution-rate .card-icon {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }
  }
}

.table-container {
  background: white;
  border-radius: 4px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.amount-text {
  font-weight: 600;
  color: #E6A23C;
}

.dialog-footer {
  text-align: right;
}
</style>
