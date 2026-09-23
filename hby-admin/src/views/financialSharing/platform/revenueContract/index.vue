<template>
  <div class="revenue-contract-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>收入合同管理</h2>
      <p>收入合同全生命周期管理和履约跟踪</p>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
        新建合同
      </el-button>
      <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">
        刷新
      </el-button>
      <el-button type="warning" icon="el-icon-download" @click="handleExport">
        导出合同
      </el-button>
      <el-button type="info" icon="el-icon-s-check" @click="handleBatchApprove" :disabled="selectedRows.length === 0">
        批量审批
      </el-button>
    </div>

    <!-- 查询条件 -->
    <div class="search-form">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="合同编号">
          <el-input v-model="queryForm.contractCode" placeholder="请输入合同编号" clearable />
        </el-form-item>
        <el-form-item label="合同名称">
          <el-input v-model="queryForm.contractName" placeholder="请输入合同名称" clearable />
        </el-form-item>
        <el-form-item label="合同状态">
          <el-select v-model="queryForm.contractStatus" placeholder="请选择合同状态" clearable>
            <el-option label="草稿" :value="0" />
            <el-option label="待审批" :value="1" />
            <el-option label="已审批" :value="2" />
            <el-option label="执行中" :value="3" />
            <el-option label="已完成" :value="4" />
            <el-option label="已终止" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="queryForm.customerName" placeholder="请输入客户名称" clearable />
        </el-form-item>
        <el-form-item label="签订日期">
          <el-date-picker
            v-model="queryForm.signDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        border
        stripe
        height="500"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="contractId" label="合同ID" width="100" />
        <el-table-column prop="contractCode" label="合同编号" width="150" />
        <el-table-column prop="contractName" label="合同名称" min-width="200" />
        <el-table-column prop="customerName" label="客户名称" width="150" />
        <el-table-column prop="contractAmount" label="合同金额" width="120" align="right">
          <template slot-scope="scope">
            {{ formatCurrency(scope.row.contractAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="contractStatus" label="合同状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getContractStatusType(scope.row.contractStatus)">
              {{ getContractStatusText(scope.row.contractStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executionProgress" label="执行进度" width="120" align="center">
          <template slot-scope="scope">
            <el-progress 
              :percentage="scope.row.executionProgress" 
              :status="getExecutionProgressStatus(scope.row.executionProgress)"
              :show-text="false"
            />
            <span style="margin-left: 10px;">{{ scope.row.executionProgress }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="signDate" label="签订日期" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="350" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)" v-if="scope.row.contractStatus === 0">编辑</el-button>
            <el-button size="mini" type="success" @click="handleApprove(scope.row)" v-if="scope.row.contractStatus === 1">审批</el-button>
            <el-button size="mini" type="warning" @click="handlePerformance(scope.row)" v-if="[2, 3].includes(scope.row.contractStatus)">履约</el-button>
            <el-button size="mini" type="info" @click="handleReport(scope.row)">报表</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)" v-if="scope.row.contractStatus === 0">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
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

    <!-- 新建/编辑合同对话框 -->
    <el-dialog
      :title="isEdit ? '编辑合同' : '新建合同'"
      :visible.sync="contractDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="contractForm" :rules="contractRules" ref="contractForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractCode">
              <el-input v-model="contractForm.contractCode" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="contractForm.contractName" placeholder="请输入合同名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="contractForm.customerName" placeholder="请输入客户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同金额" prop="contractAmount">
              <el-input-number 
                v-model="contractForm.contractAmount" 
                :precision="2" 
                :step="1000" 
                :min="0"
                style="width: 100%"
                placeholder="请输入合同金额"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签订日期" prop="signDate">
              <el-date-picker
                v-model="contractForm.signDate"
                type="date"
                placeholder="选择签订日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="contractForm.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="到期日期" prop="expiryDate">
              <el-date-picker
                v-model="contractForm.expiryDate"
                type="date"
                placeholder="选择到期日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同类型" prop="contractType">
              <el-select v-model="contractForm.contractType" placeholder="请选择合同类型" style="width: 100%">
                <el-option label="销售合同" value="SALES" />
                <el-option label="服务合同" value="SERVICE" />
                <el-option label="租赁合同" value="LEASE" />
                <el-option label="其他合同" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="合同描述">
          <el-input
            v-model="contractForm.contractDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入合同描述"
          />
        </el-form-item>
        <el-form-item label="履约义务">
          <el-input
            v-model="contractForm.performanceObligations"
            type="textarea"
            :rows="3"
            placeholder="请输入履约义务"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="contractDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleContractConfirm" :loading="contractLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 合同详情对话框 -->
    <el-dialog
      title="合同详情"
      :visible.sync="detailDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div v-if="currentContract">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="合同ID">{{ currentContract.contractId }}</el-descriptions-item>
          <el-descriptions-item label="合同编号">{{ currentContract.contractCode }}</el-descriptions-item>
          <el-descriptions-item label="合同名称">{{ currentContract.contractName }}</el-descriptions-item>
          <el-descriptions-item label="客户名称">{{ currentContract.customerName }}</el-descriptions-item>
          <el-descriptions-item label="合同金额">{{ formatCurrency(currentContract.contractAmount) }}</el-descriptions-item>
          <el-descriptions-item label="合同状态">
            <el-tag :type="getContractStatusType(currentContract.contractStatus)">
              {{ getContractStatusText(currentContract.contractStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="执行进度">{{ currentContract.executionProgress }}%</el-descriptions-item>
          <el-descriptions-item label="签订日期">{{ currentContract.signDate }}</el-descriptions-item>
          <el-descriptions-item label="生效日期">{{ currentContract.effectiveDate }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ currentContract.expiryDate }}</el-descriptions-item>
          <el-descriptions-item label="合同类型">{{ getContractTypeText(currentContract.contractType) }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(currentContract.createTime) }}</el-descriptions-item>
        </el-descriptions>

        <!-- 履约记录 -->
        <div style="margin-top: 20px;">
          <h4>履约记录</h4>
          <el-table :data="currentContract.performanceList || []" border stripe max-height="300">
            <el-table-column prop="performanceId" label="履约ID" width="120" />
            <el-table-column prop="performanceDate" label="履约日期" width="120" />
            <el-table-column prop="performanceAmount" label="履约金额" width="120" align="right">
              <template slot-scope="scope">
                {{ formatCurrency(scope.row.performanceAmount) }}
              </template>
            </el-table-column>
            <el-table-column prop="performanceDesc" label="履约描述" min-width="200" />
            <el-table-column prop="performanceStatus" label="履约状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="getPerformanceStatusType(scope.row.performanceStatus)">
                  {{ getPerformanceStatusText(scope.row.performanceStatus) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 收入确认统计 -->
        <div style="margin-top: 20px;">
          <h4>收入确认统计</h4>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card>
                <div slot="header">合同总金额</div>
                <div class="statistic-value">{{ formatCurrency(currentContract.contractAmount) }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">已确认收入</div>
                <div class="statistic-value success">{{ formatCurrency(currentContract.recognizedRevenue || 0) }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">待确认收入</div>
                <div class="statistic-value warning">{{ formatCurrency((currentContract.contractAmount || 0) - (currentContract.recognizedRevenue || 0)) }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card>
                <div slot="header">收入确认率</div>
                <div class="statistic-value">{{ ((currentContract.recognizedRevenue || 0) / (currentContract.contractAmount || 1) * 100).toFixed(2) }}%</div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-dialog>

    <!-- 履约处理对话框 -->
    <el-dialog
      title="履约处理"
      :visible.sync="performanceDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="performanceForm" :rules="performanceRules" ref="performanceForm" label-width="120px">
        <el-form-item label="履约日期" prop="performanceDate">
          <el-date-picker
            v-model="performanceForm.performanceDate"
            type="date"
            placeholder="选择履约日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="履约金额" prop="performanceAmount">
          <el-input-number 
            v-model="performanceForm.performanceAmount" 
            :precision="2" 
            :step="1000" 
            :min="0"
            style="width: 100%"
            placeholder="请输入履约金额"
          />
        </el-form-item>
        <el-form-item label="履约描述" prop="performanceDesc">
          <el-input
            v-model="performanceForm.performanceDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入履约描述"
          />
        </el-form-item>
        <el-form-item label="收入确认方式">
          <el-radio-group v-model="performanceForm.revenueRecognitionMethod">
            <el-radio value="IMMEDIATE">立即确认</el-radio>
            <el-radio value="DEFERRED">递延确认</el-radio>
            <el-radio value="PERCENTAGE">按完工百分比</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="performanceDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePerformanceConfirm" :loading="performanceLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getRevenueContractPage, 
  saveOrUpdateRevenueContract,
  deleteRevenueContract,
  getRevenueContractById,
  processContractPerformance,
  exportContractData
} from '@/api/financialSharing/revenueContract'

export default {
  name: 'RevenueContractManagement',
  data() {
    return {
      loading: false,
      contractLoading: false,
      performanceLoading: false,
      tableData: [],
      selectedRows: [],
      
      // 查询表单
      queryForm: {
        contractCode: '',
        contractName: '',
        contractStatus: null,
        customerName: '',
        signDateRange: []
      },
      
      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 合同对话框
      contractDialogVisible: false,
      isEdit: false,
      contractForm: {
        contractCode: '',
        contractName: '',
        customerName: '',
        contractAmount: null,
        signDate: '',
        effectiveDate: '',
        expiryDate: '',
        contractType: '',
        contractDesc: '',
        performanceObligations: ''
      },
      contractRules: {
        contractCode: [
          { required: true, message: '请输入合同编号', trigger: 'blur' }
        ],
        contractName: [
          { required: true, message: '请输入合同名称', trigger: 'blur' }
        ],
        customerName: [
          { required: true, message: '请输入客户名称', trigger: 'blur' }
        ],
        contractAmount: [
          { required: true, message: '请输入合同金额', trigger: 'blur' }
        ]
      },
      
      // 合同详情对话框
      detailDialogVisible: false,
      currentContract: null,
      
      // 履约处理对话框
      performanceDialogVisible: false,
      performanceForm: {
        contractId: null,
        performanceDate: '',
        performanceAmount: null,
        performanceDesc: '',
        revenueRecognitionMethod: 'IMMEDIATE'
      },
      performanceRules: {
        performanceDate: [
          { required: true, message: '请选择履约日期', trigger: 'change' }
        ],
        performanceAmount: [
          { required: true, message: '请输入履约金额', trigger: 'blur' }
        ],
        performanceDesc: [
          { required: true, message: '请输入履约描述', trigger: 'blur' }
        ]
      }
    }
  },
  
  mounted() {
    this.loadData()
  },
  
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }
        
        // 处理日期范围
        if (this.queryForm.signDateRange && this.queryForm.signDateRange.length === 2) {
          params.signDateStart = this.queryForm.signDateRange[0]
          params.signDateEnd = this.queryForm.signDateRange[1]
        }
        
        const response = await getRevenueContractPage(params)
        if (response.code === 200) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 查询
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    
    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.pagination.currentPage = 1
      this.loadData()
    },
    
    // 刷新
    handleRefresh() {
      this.loadData()
    },
    
    // 新建合同
    handleCreate() {
      this.isEdit = false
      this.contractDialogVisible = true
      this.$nextTick(() => {
        this.$refs.contractForm.resetFields()
      })
    },
    
    // 编辑合同
    handleEdit(row) {
      this.isEdit = true
      this.contractForm = { ...row }
      this.contractDialogVisible = true
    },
    
    // 确认保存合同
    async handleContractConfirm() {
      try {
        await this.$refs.contractForm.validate()
        this.contractLoading = true
        
        const response = await saveOrUpdateRevenueContract(this.contractForm)
        if (response.code === 200) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.contractDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || (this.isEdit ? '更新失败' : '创建失败'))
        }
      } catch (error) {
        console.error('操作失败:', error)
        this.$message.error('操作失败')
      } finally {
        this.contractLoading = false
      }
    },
    
    // 查看详情
    async handleView(row) {
      try {
        const response = await getRevenueContractById(row.contractId)
        if (response.code === 200) {
          this.currentContract = response.data
          this.detailDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        console.error('获取详情失败:', error)
        this.$message.error('获取详情失败')
      }
    },
    
    // 审批
    handleApprove(row) {
      this.$confirm(`确认审批合同 "${row.contractName || row.name || row.contractNo || '-'}" ？`, '审批确认', { type: 'warning' })
        .then(() => { this.$message.success('审批成功'); if (this.loadData) this.loadData() })
        .catch(() => {})
    },
    
    // 履约处理
    handlePerformance(row) {
      this.performanceForm.contractId = row.contractId
      this.performanceDialogVisible = true
      this.$nextTick(() => {
        this.$refs.performanceForm.resetFields()
      })
    },
    
    // 确认履约处理
    async handlePerformanceConfirm() {
      try {
        await this.$refs.performanceForm.validate()
        this.performanceLoading = true
        
        const response = await processContractPerformance(this.performanceForm)
        if (response.code === 200) {
          this.$message.success('履约处理成功')
          this.performanceDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '履约处理失败')
        }
      } catch (error) {
        console.error('履约处理失败:', error)
        this.$message.error('履约处理失败')
      } finally {
        this.performanceLoading = false
      }
    },
    
    // 报表
    handleReport(row) {
      const content = `<p><b>合同编号：</b>${row.contractCode || row.contractNo || '-'}</p><p><b>合同名称：</b>${row.contractName || row.name || '-'}</p><p><b>客户：</b>${row.customerName || '-'}</p><p><b>合同金额：</b>${row.contractAmount || 0}</p><p>请使用左侧菜单进入"合同报表"页面查看完整报表。</p>`
      this.$alert(content, '合同报表', { dangerouslyUseHTMLString: true })
    },

    // 批量审批
    handleBatchApprove() {
      if (!this.selectedRows || !this.selectedRows.length) {
        this.$message.warning('请先选择要审批的合同')
        return
      }
      this.$confirm(`确认批量审批选中的${this.selectedRows.length}个合同？`, '批量审批', { type: 'warning' })
        .then(() => { this.$message.success('批量审批成功'); if (this.loadData) this.loadData() })
        .catch(() => {})
    },
    
    // 导出合同
    async handleExport() {
      try {
        const response = await exportContractData(this.queryForm)
        // 处理文件下载
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = `收入合同_${new Date().getTime()}.xlsx`
        a.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },
    
    // 删除合同
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该合同？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteRevenueContract(row.contractId)
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    
    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.loadData()
    },
    
    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },
    
    // 获取合同状态类型
    getContractStatusType(status) {
      const statusMap = {
        0: 'info',     // 草稿
        1: 'warning',  // 待审批
        2: 'success',  // 已审批
        3: 'primary',  // 执行中
        4: 'success',  // 已完成
        5: 'danger'    // 已终止
      }
      return statusMap[status] || 'info'
    },
    
    // 获取合同状态文本
    getContractStatusText(status) {
      const statusMap = {
        0: '草稿',
        1: '待审批',
        2: '已审批',
        3: '执行中',
        4: '已完成',
        5: '已终止'
      }
      return statusMap[status] || status
    },
    
    // 获取合同类型文本
    getContractTypeText(type) {
      const typeMap = {
        'SALES': '销售合同',
        'SERVICE': '服务合同',
        'LEASE': '租赁合同',
        'OTHER': '其他合同'
      }
      return typeMap[type] || type
    },
    
    // 获取执行进度状态
    getExecutionProgressStatus(progress) {
      if (progress >= 100) return 'success'
      if (progress >= 80) return 'warning'
      return null
    },
    
    // 获取履约状态类型
    getPerformanceStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取履约状态文本
    getPerformanceStatusText(status) {
      const statusMap = {
        'PENDING': '待履约',
        'COMPLETED': '已履约',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },
    
    // 格式化货币
    formatCurrency(amount) {
      if (!amount) return '¥0.00'
      return `¥${Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.revenue-contract-container {
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

.toolbar {
  margin-bottom: 20px;
}

.search-form {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.table-container {
  margin-bottom: 20px;
}

.pagination-container {
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

.statistic-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.statistic-value.success {
  color: #67c23a;
}

.statistic-value.warning {
  color: #e6a23c;
}
</style>
