<template>
  <div class="provision-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="预提单号" prop="provisionNumber">
          <el-input
            v-model="searchForm.provisionNumber"
            placeholder="请输入预提单号"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="申请人" prop="applicant">
          <el-input
            v-model="searchForm.applicant"
            placeholder="请输入申请人"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="预提类型" prop="provisionType">
          <el-select
            v-model="searchForm.provisionType"
            placeholder="请选择预提类型"
            clearable
            style="width: 150px"
          >
            <el-option label="工资预提" value="SALARY" />
            <el-option label="奖金预提" value="BONUS" />
            <el-option label="租金预提" value="RENT" />
            <el-option label="利息预提" value="INTEREST" />
            <el-option label="折旧预提" value="DEPRECIATION" />
            <el-option label="其他预提" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="草稿" value="DRAFT" />
            <el-option label="待审批" value="PENDING" />
            <el-option label="已审批" value="APPROVED" />
            <el-option label="已生效" value="EFFECTIVE" />
            <el-option label="部分冲销" value="PARTIAL_REVERSED" />
            <el-option label="已冲销" value="REVERSED" />
            <el-option label="已拒绝" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="预提期间" prop="dateRange">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增费用预提</el-button>
      <el-button type="success" @click="handleBatchApprove" :disabled="!multipleSelection.length">
        批量审批
      </el-button>
      <el-button type="warning" @click="handleExport">导出</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
    </div>

    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="provisionNumber" label="预提单号" width="180" />
        <el-table-column prop="provisionTitle" label="预提标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="applicant" label="申请人" width="100" />
        <el-table-column prop="departmentName" label="部门" width="120" />
        <el-table-column prop="provisionTypeName" label="预提类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getProvisionTypeColor(scope.row.provisionType)" size="small">
              {{ scope.row.provisionTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="provisionPeriod" label="预提期间" width="120" />
        <el-table-column prop="provisionAmount" label="预提金额" width="120">
          <template slot-scope="scope">
            <span class="amount">¥{{ formatAmount(scope.row.provisionAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="reversedAmount" label="已冲销金额" width="120">
          <template slot-scope="scope">
            <span class="amount reversed">¥{{ formatAmount(scope.row.reversedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="待冲销金额" width="120">
          <template slot-scope="scope">
            <span class="amount remaining">¥{{ formatAmount(scope.row.remainingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="small">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="submitTime" label="提交时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.submitTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="effectiveTime" label="生效时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.effectiveTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="400" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button 
              size="mini" 
              type="primary" 
              @click="handleEdit(scope.row)"
              v-if="scope.row.status === 'DRAFT'"
            >
              编辑
            </el-button>
            <el-button 
              size="mini" 
              type="success" 
              @click="handleApprove(scope.row)"
              v-if="scope.row.status === 'PENDING'"
            >
              审批
            </el-button>
            <el-button
              size="mini"
              type="warning"
              @click="handleReverse(scope.row)"
              v-if="scope.row.status === 'EFFECTIVE' && scope.row.remainingAmount > 0"
            >
              冲销
            </el-button>
            <el-button
              size="mini"
              type="info"
              @click="handleViewVoucher(scope.row)"
              v-if="scope.row.status === 'EFFECTIVE' || scope.row.status === 'PARTIAL_REVERSED' || scope.row.status === 'REVERSED'"
            >
              查看凭证
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)"
              v-if="scope.row.status === 'DRAFT'"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

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

    <!-- 费用预提详情对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预提单号" prop="provisionNumber">
              <el-input v-model="formData.provisionNumber" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预提标题" prop="provisionTitle">
              <el-input v-model="formData.provisionTitle" :disabled="isViewMode" placeholder="请输入预提标题" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预提类型" prop="provisionType">
              <el-select v-model="formData.provisionType" :disabled="isViewMode" style="width: 100%" placeholder="请选择预提类型">
                <el-option label="工资预提" value="SALARY" />
                <el-option label="奖金预提" value="BONUS" />
                <el-option label="租金预提" value="RENT" />
                <el-option label="利息预提" value="INTEREST" />
                <el-option label="折旧预提" value="DEPRECIATION" />
                <el-option label="其他预提" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预提期间" prop="provisionPeriod">
              <el-date-picker
                v-model="formData.provisionPeriod"
                :disabled="isViewMode"
                type="month"
                placeholder="请选择预提期间"
                style="width: 100%"
                value-format="yyyy-MM"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预提金额" prop="provisionAmount">
              <el-input-number v-model="formData.provisionAmount" :disabled="isViewMode" :min="0" :precision="2" style="width: 100%" placeholder="请输入预提金额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会计科目" prop="accountSubject">
              <el-select v-model="formData.accountSubject" :disabled="isViewMode" filterable placeholder="请选择会计科目" style="width: 100%">
                <el-option
                  v-for="item in accountSubjectList"
                  :key="item.subjectCode"
                  :label="item.subjectCode + '-' + item.subjectName"
                  :value="item.subjectCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="对应科目" prop="correspondingSubject">
              <el-select v-model="formData.correspondingSubject" :disabled="isViewMode" filterable placeholder="请选择对应科目" style="width: 100%">
                <el-option
                  v-for="item in accountSubjectList"
                  :key="item.subjectCode"
                  :label="item.subjectCode + '-' + item.subjectName"
                  :value="item.subjectCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计冲销日期" prop="expectedReverseDate">
              <el-date-picker
                v-model="formData.expectedReverseDate"
                :disabled="isViewMode"
                type="date"
                placeholder="请选择预计冲销日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属部门" prop="departmentId">
              <el-select v-model="formData.departmentId" :disabled="isViewMode" filterable placeholder="请选择部门" style="width: 100%">
                <el-option
                  v-for="item in departmentList"
                  :key="item.deptId"
                  :label="item.deptName"
                  :value="item.deptId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联项目" prop="projectId">
              <el-select v-model="formData.projectId" :disabled="isViewMode" filterable clearable placeholder="请选择项目" style="width: 100%">
                <el-option
                  v-for="item in projectList"
                  :key="item.projectId"
                  :label="item.projectName"
                  :value="item.projectId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="预提事由" prop="provisionReason">
              <el-input
                v-model="formData.provisionReason"
                :disabled="isViewMode"
                type="textarea"
                :rows="3"
                placeholder="请输入预提事由"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="formData.remark"
                :disabled="isViewMode"
                type="textarea"
                :rows="2"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-alert
          title="预提提示"
          type="info"
          :closable="false"
          show-icon
          v-if="!isViewMode"
          style="margin-top: 10px"
        >
          <div>1. 费用预提将自动生成会计凭证</div>
          <div>2. 预提金额将计入当期费用</div>
          <div>3. 实际发生时需要进行冲销处理</div>
        </el-alert>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" v-if="!isViewMode">保存</el-button>
        <el-button type="success" @click="handleSubmit" v-if="!isViewMode && formData.provisionId">提交审批</el-button>
      </div>
    </el-dialog>

    <!-- 冲销对话框 -->
    <ReverseDialog
      :visible.sync="reverseDialogVisible"
      :provision-data="currentRow"
      @success="handleReverseSuccess"
    />

    <!-- 会计凭证查看对话框 -->
    <VoucherDialog
      :visible.sync="voucherDialogVisible"
      :provision-data="currentRow"
    />
  </div>
</template>

<script>
import { provisionApi } from '@/api/financialSharing/coreBusiness'
import { getAccountSubjectDropdownList, getDepartmentDropdownList, getProjectDropdownList } from '@/api/financialSharing/common'
import ReverseDialog from './components/ReverseDialog.vue'
import VoucherDialog from './components/VoucherDialog.vue'

export default {
  name: 'ProvisionManagement',
  components: {
    ReverseDialog,
    VoucherDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        provisionNumber: '',
        applicant: '',
        provisionType: '',
        status: '',
        dateRange: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      isViewMode: false,
      formData: {
        provisionId: '',
        provisionNumber: '',
        provisionTitle: '',
        provisionType: '',
        provisionPeriod: '',
        provisionAmount: 0,
        accountSubject: '',
        correspondingSubject: '',
        expectedReverseDate: '',
        departmentId: '',
        projectId: '',
        provisionReason: '',
        remark: ''
      },
      formRules: {
        provisionTitle: [{ required: true, message: '请输入预提标题', trigger: 'blur' }],
        provisionType: [{ required: true, message: '请选择预提类型', trigger: 'change' }],
        provisionPeriod: [{ required: true, message: '请选择预提期间', trigger: 'change' }],
        provisionAmount: [{ required: true, message: '请输入预提金额', trigger: 'blur' }],
        accountSubject: [{ required: true, message: '请选择会计科目', trigger: 'change' }],
        correspondingSubject: [{ required: true, message: '请选择对应科目', trigger: 'change' }],
        departmentId: [{ required: true, message: '请选择部门', trigger: 'change' }],
        provisionReason: [{ required: true, message: '请输入预提事由', trigger: 'blur' }]
      },
      accountSubjectList: [],
      departmentList: [],
      projectList: [],
      reverseDialogVisible: false,
      voucherDialogVisible: false,
      currentRow: null
    }
  },
  created() {
    this.loadData()
    this.loadAccountSubjects()
    this.loadDepartments()
    this.loadProjects()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }

        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startPeriod = this.formatMonth(this.searchForm.dateRange[0])
          params.endPeriod = this.formatMonth(this.searchForm.dateRange[1])
        }

        const res = await provisionApi.getList(params)
        if (res.code === 1) {
          this.tableData = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        } else {
          this.$message.error(res.message || '查询失败')
        }
      } catch (error) {
        console.error('查询失败:', error)
        this.$message.error('查询失败')
      } finally {
        this.loading = false
      }
    },

    // 加载会计科目列表
    async loadAccountSubjects() {
      try {
        const res = await getAccountSubjectDropdownList()
        if (res.code === 1 && res.data) {
          this.accountSubjectList = res.data || []
        }
      } catch (error) {
        console.error('加载会计科目失败:', error)
      }
    },

    // 加载部门列表
    async loadDepartments() {
      try {
        const res = await getDepartmentDropdownList()
        if (res.code === 1 && res.data) {
          this.departmentList = res.data || []
        }
      } catch (error) {
        console.error('加载部门失败:', error)
      }
    },

    // 加载项目列表
    async loadProjects() {
      try {
        const res = await getProjectDropdownList()
        if (res.code === 1 && res.data) {
          this.projectList = res.data || []
        }
      } catch (error) {
        console.error('加载项目失败:', error)
      }
    },

    // 查询
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.searchForm.dateRange = null
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增费用预提'
      this.isViewMode = false
      this.resetForm()
      this.formData.provisionNumber = 'PROV' + Date.now()
      this.formData.provisionPeriod = this.getCurrentMonth()
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑费用预提'
      this.isViewMode = false
      this.formData = { ...row }
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.dialogTitle = '费用预提详情'
      this.isViewMode = true
      this.formData = { ...row }
      this.dialogVisible = true
    },

    // 保存
    async handleSave() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          try {
            const res = await provisionApi.save(this.formData)
            if (res.code === 1) {
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.loadData()
            } else {
              this.$message.error(res.message || '保存失败')
            }
          } catch (error) {
            console.error('保存失败:', error)
            this.$message.error('保存失败')
          }
        }
      })
    },

    // 提交审批
    async handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          try {
            const res = await provisionApi.submit(this.formData.provisionId, {})
            if (res.code === 1) {
              this.$message.success('提交成功,将自动生成预提凭证')
              this.dialogVisible = false
              this.loadData()
            } else {
              this.$message.error(res.message || '提交失败')
            }
          } catch (error) {
            console.error('提交失败:', error)
            this.$message.error('提交失败')
          }
        }
      })
    },

    // 审批
    handleApprove(row) {
      this.$confirm('确认审批通过该费用预提申请?', '提示', {
        confirmButtonText: '通过',
        cancelButtonText: '驳回',
        distinguishCancelAndClose: true,
        type: 'warning'
      }).then(async () => {
        try {
          const res = await provisionApi.approve(row.provisionId, { approved: true })
          if (res.code === 1) {
            this.$message.success('审批成功,费用预提已生效')
            this.loadData()
          } else {
            this.$message.error(res.message || '审批失败')
          }
        } catch (error) {
          console.error('审批失败:', error)
          this.$message.error('审批失败')
        }
      }).catch((action) => {
        if (action === 'cancel') {
          // 驳回操作
          this.$prompt('请输入驳回原因', '驳回', {
            confirmButtonText: '确定',
            cancelButtonText: '取消'
          }).then(async ({ value }) => {
            try {
              const res = await provisionApi.approve(row.provisionId, {
                approved: false,
                rejectReason: value
              })
              if (res.code === 1) {
                this.$message.success('已驳回')
                this.loadData()
              }
            } catch (error) {
              this.$message.error('驳回失败')
            }
          })
        }
      })
    },

    // 冲销
    handleReverse(row) {
      this.currentRow = row
      this.reverseDialogVisible = true
    },

    // 冲销成功回调
    handleReverseSuccess() {
      this.loadData()
    },

    // 查看凭证
    handleViewVoucher(row) {
      this.currentRow = row
      this.voucherDialogVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该费用预提?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await provisionApi.delete(row.provisionId)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      })
    },

    // 批量审批
    handleBatchApprove() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要审批的费用预提')
        return
      }
      this.$confirm(`确认批量审批${this.multipleSelection.length}条费用预提?`, '提示', {
        confirmButtonText: '通过',
        cancelButtonText: '驳回',
        distinguishCancelAndClose: true,
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.multipleSelection.map(item => item.provisionId)
          const results = await Promise.allSettled(
            ids.map(id => provisionApi.approve(id, { approved: true }))
          )
          const succeeded = results.filter(r => r.status === 'fulfilled' && r.value && r.value.code === 1).length
          const failed = results.length - succeeded
          if (failed === 0) {
            this.$message.success(`批量审批成功，共${succeeded}条`)
          } else {
            this.$message.warning(`审批完成：成功${succeeded}条，失败${failed}条`)
          }
          this.loadData()
        } catch (error) {
          console.error('批量审批失败:', error)
          this.$message.error('批量审批失败')
        }
      }).catch((action) => {
        if (action === 'cancel') {
          this.$prompt('请输入批量驳回原因', '批量驳回', {
            confirmButtonText: '确定',
            cancelButtonText: '取消'
          }).then(async ({ value }) => {
            try {
              const ids = this.multipleSelection.map(item => item.provisionId)
              const results = await Promise.allSettled(
                ids.map(id => provisionApi.approve(id, { approved: false, rejectReason: value }))
              )
              const succeeded = results.filter(r => r.status === 'fulfilled' && r.value && r.value.code === 1).length
              this.$message.success(`批量驳回完成：成功${succeeded}条`)
              this.loadData()
            } catch (error) {
              this.$message.error('批量驳回失败')
            }
          })
        }
      })
    },

    // 批量删除
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的费用预提')
        return
      }
      this.$confirm(`确认删除${this.multipleSelection.length}条费用预提?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.multipleSelection.map(item => item.provisionId)
          const res = await provisionApi.batchDelete(ids)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      })
    },

    // 导出
    handleExport() {
      const params = {
        ...this.searchForm,
        startPeriod: this.searchForm.dateRange && this.searchForm.dateRange.length === 2
          ? this.formatMonth(this.searchForm.dateRange[0]) : '',
        endPeriod: this.searchForm.dateRange && this.searchForm.dateRange.length === 2
          ? this.formatMonth(this.searchForm.dateRange[1]) : ''
      }
      delete params.dateRange
      provisionApi.export(params).then(res => {
        if (res.code === 1) {
          this.$message.success('导出成功')
          if (res.data && res.data.url) {
            window.open(res.data.url)
          }
        } else {
          this.$message.error(res.msg || '导出失败')
        }
      }).catch(error => {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      })
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },

    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },

    // 重置表单
    resetForm() {
      this.formData = {
        provisionId: '',
        provisionNumber: '',
        provisionTitle: '',
        provisionType: '',
        provisionPeriod: '',
        provisionAmount: 0,
        accountSubject: '',
        correspondingSubject: '',
        expectedReverseDate: '',
        departmentId: '',
        projectId: '',
        provisionReason: '',
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.formRef && this.$refs.formRef.clearValidate()
      })
    },

    // 获取当前月份
    getCurrentMonth() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      return `${year}-${month}`
    },

    // 获取预提类型颜色
    getProvisionTypeColor(type) {
      const colorMap = {
        'SALARY': 'primary',
        'BONUS': 'success',
        'RENT': 'warning',
        'INTEREST': 'danger',
        'DEPRECIATION': 'info',
        'OTHER': 'info'
      }
      return colorMap[type] || 'info'
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'DRAFT': 'info',
        'PENDING': 'warning',
        'APPROVED': 'success',
        'EFFECTIVE': 'primary',
        'PARTIAL_REVERSED': '',
        'REVERSED': 'success',
        'REJECTED': 'danger'
      }
      return colorMap[status] || 'info'
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const hour = String(d.getHours()).padStart(2, '0')
      const minute = String(d.getMinutes()).padStart(2, '0')
      const second = String(d.getSeconds()).padStart(2, '0')

      // 如果只有日期部分,返回日期
      if (hour === '00' && minute === '00' && second === '00') {
        return `${year}-${month}-${day}`
      }

      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    },

    // 格式化月份
    formatMonth(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      return `${year}-${month}`
    }
  }
}
</script>

<style scoped lang="scss">
.provision-container {
  padding: 20px;
  background: #fff;

  .search-container {
    margin-bottom: 20px;
    padding: 20px;
    background: #f5f7fa;
    border-radius: 4px;

    .search-form {
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }

  .toolbar {
    margin-bottom: 20px;
  }

  .table-container {
    .amount {
      font-weight: 600;
      color: #303133;

      &.reversed {
        color: #67c23a;
      }

      &.remaining {
        color: #e6a23c;
      }
    }

    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>
