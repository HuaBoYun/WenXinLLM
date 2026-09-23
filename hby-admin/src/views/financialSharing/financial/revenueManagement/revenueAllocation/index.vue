<template>
  <div class="revenue-allocation-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-s-operation"></i>
          收入分配管理
        </h1>
        <p class="page-description">管理收入在不同维度的分配，包括部门、产品、项目等分配规则</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          新增分配
        </el-button>
        <el-button type="success" icon="el-icon-setting" @click="manageRules">
          分配规则
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 分配统计 -->
    <div class="allocation-stats">
      <el-row :gutter="24">
        <el-col :span="8">
          <div class="stat-card">
            <div class="stat-icon department">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.departmentAllocations }}</div>
              <div class="stat-label">部门分配</div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card">
            <div class="stat-icon product">
              <i class="el-icon-goods"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.productAllocations }}</div>
              <div class="stat-label">产品分配</div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card">
            <div class="stat-icon project">
              <i class="el-icon-folder"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.projectAllocations }}</div>
              <div class="stat-label">项目分配</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="分配编号">
          <el-input v-model="searchForm.allocationNo" placeholder="请输入分配编号" clearable />
        </el-form-item>
        <el-form-item label="分配类型">
          <el-select v-model="searchForm.allocationType" placeholder="请选择分配类型" clearable>
            <el-option label="部门分配" :value="1" />
            <el-option label="产品分配" :value="2" />
            <el-option label="项目分配" :value="3" />
            <el-option label="客户分配" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="分配状态">
          <el-select v-model="searchForm.allocationStatus" placeholder="请选择分配状态" clearable>
            <el-option label="待分配" :value="0" />
            <el-option label="已分配" :value="1" />
            <el-option label="已撤销" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="分配期间">
          <el-date-picker
            v-model="searchForm.allocationPeriod"
            type="month"
            placeholder="选择分配期间"
            format="yyyy-MM"
            value-format="yyyy-MM"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        border
        height="500"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="allocationId" label="分配ID" width="120" />
        <el-table-column prop="allocationNo" label="分配编号" width="150" />
        <el-table-column prop="allocationTypeName" label="分配类型" width="120" />
        <el-table-column prop="allocationPeriod" label="分配期间" width="120" />
        <el-table-column prop="totalAmount" label="总金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="allocatedAmount" label="已分配金额" width="130" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.allocatedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="剩余金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.remainingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="allocationStatus" label="分配状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.allocationStatus)">
              {{ getStatusText(scope.row.allocationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="allocationDate" label="分配日期" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="viewDetail(scope.row)">详情</el-button>
            <el-button 
              v-if="scope.row.allocationStatus === 0" 
              size="mini" 
              type="success" 
              @click="executeAllocation(scope.row)"
            >
              执行
            </el-button>
            <el-button 
              v-if="scope.row.allocationStatus === 0" 
              size="mini" 
              type="warning" 
              @click="editAllocation(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-if="scope.row.allocationStatus === 1" 
              size="mini" 
              type="danger" 
              @click="revokeAllocation(scope.row)"
            >
              撤销
            </el-button>
          </template>
        </el-table-column>
      </el-table>

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
    </div>

    <!-- 创建分配对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="createDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <el-form :model="createForm" :rules="createRules" ref="createForm" label-width="120px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="分配编号" prop="allocationNo">
              <el-input v-model="createForm.allocationNo" placeholder="请输入分配编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分配类型" prop="allocationType">
              <el-select v-model="createForm.allocationType" placeholder="请选择分配类型" @change="onAllocationTypeChange">
                <el-option label="部门分配" :value="1" />
                <el-option label="产品分配" :value="2" />
                <el-option label="项目分配" :value="3" />
                <el-option label="客户分配" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="分配期间" prop="allocationPeriod">
              <el-date-picker
                v-model="createForm.allocationPeriod"
                type="month"
                placeholder="选择分配期间"
                format="yyyy-MM"
                value-format="yyyy-MM"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总金额" prop="totalAmount">
              <el-input-number
                v-model="createForm.totalAmount"
                :precision="2"
                :min="0"
                placeholder="请输入总金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="分配规则" prop="allocationRuleId">
          <el-select v-model="createForm.allocationRuleId" placeholder="请选择分配规则" style="width: 100%">
            <el-option
              v-for="rule in allocationRules"
              :key="rule.ruleId"
              :label="rule.ruleName"
              :value="rule.ruleId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分配说明" prop="allocationDesc">
          <el-input
            v-model="createForm.allocationDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入分配说明"
          />
        </el-form-item>

        <!-- 分配明细 -->
        <el-form-item label="分配明细">
          <el-table :data="createForm.allocationDetails" border style="width: 100%">
            <el-table-column prop="targetName" :label="getTargetLabel()" width="200" />
            <el-table-column prop="allocationRatio" label="分配比例(%)" width="120">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.allocationRatio"
                  :precision="2"
                  :min="0"
                  :max="100"
                  size="mini"
                  @change="calculateAmount(scope.row)"
                />
              </template>
            </el-table-column>
            <el-table-column prop="allocationAmount" label="分配金额" width="150">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.allocationAmount"
                  :precision="2"
                  :min="0"
                  size="mini"
                />
              </template>
            </el-table-column>
            <el-table-column prop="remarks" label="备注">
              <template slot-scope="scope">
                <el-input v-model="scope.row.remarks" size="mini" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">
                <el-button size="mini" type="danger" @click="removeDetail(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin-top: 10px;">
            <el-button size="small" type="primary" @click="addDetail">添加明细</el-button>
            <span style="margin-left: 20px;">
              总比例: {{ totalRatio }}% | 总金额: {{ formatAmount(totalDetailAmount) }}
            </span>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>

    <!-- 分配规则管理对话框 -->
    <el-dialog
      title="分配规则管理"
      :visible.sync="rulesDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div class="rules-header">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="showAddRuleDialog">
          新增规则
        </el-button>
      </div>

      <el-table :data="allocationRules" border style="width: 100%; margin-top: 15px;">
        <el-table-column prop="ruleId" label="规则ID" width="100" />
        <el-table-column prop="ruleName" label="规则名称" width="200" />
        <el-table-column prop="ruleType" label="规则类型" width="150">
          <template slot-scope="scope">
            <el-tag :type="getRuleTypeTag(scope.row.ruleType)">
              {{ getRuleTypeName(scope.row.ruleType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="状态" width="100">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              @change="toggleRuleStatus(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="editRule(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="deleteRule(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="rulesDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 新增/编辑规则对话框 -->
    <el-dialog
      :title="ruleFormMode === 'add' ? '新增规则' : '编辑规则'"
      :visible.sync="ruleFormDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form ref="ruleForm" :model="ruleForm" :rules="ruleFormRules" label-width="120px">
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="ruleForm.ruleName" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="规则类型" prop="ruleType">
          <el-select v-model="ruleForm.ruleType" placeholder="请选择规则类型" style="width: 100%">
            <el-option label="按比例分配" :value="1" />
            <el-option label="按金额分配" :value="2" />
            <el-option label="按权重分配" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="ruleForm.isEnabled" />
        </el-form-item>
        <el-form-item label="规则描述" prop="ruleDesc">
          <el-input
            v-model="ruleForm.ruleDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入规则描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="ruleFormDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRule">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getRevenueAllocationList,
  createRevenueAllocation,
  updateRevenueAllocation,
  executeRevenueAllocation,
  deleteRevenueAllocation,
  getAllocationRuleList,
  getRevenueAllocationStats
} from '@/api/financialSharing/revenueManagement'

export default {
  name: 'RevenueAllocationIndex',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      stats: {
        departmentAllocations: 0,
        productAllocations: 0,
        projectAllocations: 0
      },
      searchForm: {
        allocationNo: '',
        allocationType: '',
        allocationStatus: '',
        allocationPeriod: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      createDialogVisible: false,
      dialogTitle: '新增收入分配',
      isEdit: false,
      editId: null,
      createForm: {
        allocationNo: '',
        allocationType: '',
        allocationPeriod: '',
        totalAmount: 0,
        allocationRuleId: '',
        allocationDesc: '',
        allocationDetails: []
      },
      createRules: {
        allocationNo: [{ required: true, message: '请输入分配编号', trigger: 'blur' }],
        allocationType: [{ required: true, message: '请选择分配类型', trigger: 'change' }],
        allocationPeriod: [{ required: true, message: '请选择分配期间', trigger: 'change' }],
        totalAmount: [{ required: true, message: '请输入总金额', trigger: 'blur' }]
      },
      allocationRules: [],
      rulesDialogVisible: false,
      ruleFormDialogVisible: false,
      ruleFormMode: 'add',
      ruleForm: {
        ruleId: '',
        ruleName: '',
        ruleType: 1,
        isEnabled: true,
        ruleDesc: ''
      },
      ruleFormRules: {
        ruleName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
        ruleType: [{ required: true, message: '请选择规则类型', trigger: 'change' }]
      }
    }
  },
  computed: {
    totalRatio() {
      return this.createForm.allocationDetails.reduce((sum, item) => sum + (item.allocationRatio || 0), 0)
    },
    totalDetailAmount() {
      return this.createForm.allocationDetails.reduce((sum, item) => sum + (item.allocationAmount || 0), 0)
    }
  },
  mounted() {
    this.loadData()
    this.loadStats()
    this.loadAllocationRules()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await getRevenueAllocationList(params)
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
    async loadStats() {
      try {
        const response = await getRevenueAllocationStats()
        if (response.code === 1) {
          this.stats = response.data || {
            departmentAllocations: 0,
            productAllocations: 0,
            projectAllocations: 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
        this.stats = {
          departmentAllocations: 0,
          productAllocations: 0,
          projectAllocations: 0
        }
      }
    },
    async loadAllocationRules() {
      try {
        const response = await getAllocationRuleList({})
        if (response.code === 1) {
          this.allocationRules = response.data || []
        }
      } catch (error) {
        console.error('加载分配规则失败:', error)
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getStatusType(status) {
      const types = { 0: 'warning', 1: 'success', 2: 'info' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '待分配', 1: '已分配', 2: '已撤销' }
      return texts[status] || '未知'
    },
    getTargetLabel() {
      const labels = { 1: '部门', 2: '产品', 3: '项目', 4: '客户' }
      return labels[this.createForm.allocationType] || '目标'
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    resetSearch() {
      this.searchForm = {
        allocationNo: '',
        allocationType: '',
        allocationStatus: '',
        allocationPeriod: ''
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    showCreateDialog() {
      this.dialogTitle = '新增收入分配'
      this.isEdit = false
      this.editId = null
      this.createDialogVisible = true
      this.resetCreateForm()
    },
    resetCreateForm() {
      this.createForm = {
        allocationNo: '',
        allocationType: '',
        allocationPeriod: '',
        totalAmount: 0,
        allocationRuleId: '',
        allocationDesc: '',
        allocationDetails: []
      }
    },
    onAllocationTypeChange() {
      this.createForm.allocationDetails = []
    },
    addDetail() {
      this.createForm.allocationDetails.push({
        targetName: '',
        allocationRatio: 0,
        allocationAmount: 0,
        remarks: ''
      })
    },
    removeDetail(index) {
      this.createForm.allocationDetails.splice(index, 1)
    },
    calculateAmount(row) {
      if (this.createForm.totalAmount && row.allocationRatio) {
        row.allocationAmount = (this.createForm.totalAmount * row.allocationRatio / 100).toFixed(2)
      }
    },
    async handleSave() {
      this.$refs.createForm.validate(async (valid) => {
        if (valid) {
          if (this.totalRatio !== 100) {
            this.$message.warning('分配比例总和必须等于100%')
            return
          }
          
          try {
            let response
            if (this.isEdit) {
              response = await updateRevenueAllocation(this.editId, this.createForm)
            } else {
              response = await createRevenueAllocation(this.createForm)
            }
            
            if (response.code === 1) {
              this.$message.success(this.isEdit ? '更新成功' : '创建成功')
              this.createDialogVisible = false
              this.loadData()
              this.loadStats()
            }
          } catch (error) {
            this.$message.error(this.isEdit ? '更新失败' : '创建失败')
          }
        }
      })
    },
    viewDetail(row) {
      const content = `<p><b>编号：</b>${row.id || '-'}</p><p><b>名称：</b>${row.name || '-'}</p><p><b>金额：</b>${row.amount || 0}</p><p><b>状态：</b>${row.statusName || row.status || '-'}</p><p><b>时间：</b>${row.createTime || '-'}</p>`
      this.$alert(content, '详情', { dangerouslyUseHTMLString: true })
    },
    async executeAllocation(row) {
      this.$confirm('确定要执行此收入分配吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await executeRevenueAllocation(row.allocationId)
          if (response.code === 1) {
            this.$message.success('执行成功')
            this.loadData()
            this.loadStats()
          }
        } catch (error) {
          this.$message.error('执行失败')
        }
      })
    },
    editAllocation(row) {
      this.dialogTitle = '编辑收入分配'
      this.isEdit = true
      this.editId = row.allocationId
      this.createDialogVisible = true
      
      // 填充表单数据
      this.createForm = {
        allocationNo: row.allocationNo,
        allocationType: row.allocationType,
        allocationPeriod: row.allocationPeriod,
        totalAmount: row.totalAmount,
        allocationRuleId: row.allocationRuleId,
        allocationDesc: row.allocationDesc,
        allocationDetails: row.allocationDetails || []
      }
    },
    async revokeAllocation(row) {
      this.$confirm('确定要撤销此收入分配吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteRevenueAllocation(row.allocationId)
          if (response.code === 1) {
            this.$message.success('撤销成功')
            this.loadData()
            this.loadStats()
          }
        } catch (error) {
          this.$message.error('撤销失败')
        }
      })
    },
    manageRules() {
      this.rulesDialogVisible = true
      this.loadAllocationRules()
    },
    getRuleTypeName(type) {
      const names = { 1: '按比例分配', 2: '按金额分配', 3: '按权重分配' }
      return names[type] || '未知'
    },
    getRuleTypeTag(type) {
      const tags = { 1: 'success', 2: 'primary', 3: 'warning' }
      return tags[type] || 'info'
    },
    showAddRuleDialog() {
      this.ruleFormMode = 'add'
      this.ruleForm = {
        ruleId: '',
        ruleName: '',
        ruleType: 1,
        isEnabled: true,
        ruleDesc: ''
      }
      this.ruleFormDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.ruleForm) {
          this.$refs.ruleForm.clearValidate()
        }
      })
    },
    editRule(row) {
      this.ruleFormMode = 'edit'
      this.ruleForm = { ...row }
      this.ruleFormDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.ruleForm) {
          this.$refs.ruleForm.clearValidate()
        }
      })
    },
    async saveRule() {
      try {
        await this.$refs.ruleForm.validate()

        if (this.ruleFormMode === 'add') {
          // 新增规则 - 这里可以调用后端API
          const newRule = {
            ...this.ruleForm,
            ruleId: 9000 + this.allocationRules.length + 1
          }
          this.allocationRules.push(newRule)
          this.$message.success('新增规则成功')
        } else {
          // 编辑规则
          const index = this.allocationRules.findIndex(r => r.ruleId === this.ruleForm.ruleId)
          if (index !== -1) {
            this.$set(this.allocationRules, index, { ...this.ruleForm })
          }
          this.$message.success('编辑规则成功')
        }

        this.ruleFormDialogVisible = false
      } catch (error) {
        if (error !== false) {
          console.error('保存规则失败:', error)
        }
      }
    },
    deleteRule(row) {
      this.$confirm('确定要删除该规则吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const index = this.allocationRules.findIndex(r => r.ruleId === row.ruleId)
        if (index !== -1) {
          this.allocationRules.splice(index, 1)
          this.$message.success('删除规则成功')
        }
      }).catch(() => {})
    },
    toggleRuleStatus(row) {
      this.$message.success(`规则已${row.isEnabled ? '启用' : '禁用'}`)
    },
    refreshData() {
      this.loadData()
      this.loadStats()
    }
  }
}
</script>

<style lang="scss" scoped>
.revenue-allocation-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #67c23a;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.allocation-stats {
  margin-bottom: 24px;

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.department {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.product {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.project {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.search-area {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.table-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .amount-text {
    color: #67c23a;
    font-weight: 600;
  }
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
