<template>
  <div class="bill-endorsement-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-s-promotion"></i>
            票据背书管理
          </h2>
          <p class="page-description">管理票据背书转让的申请、审批和执行流程</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreateEndorsement">
            新增背书
          </el-button>
          <el-button type="success" icon="el-icon-check" @click="handleBatchApprove">
            批量审批
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 背书概览卡片 -->
    <div class="endorsement-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总背书数</div>
                <div class="card-value">{{ totalEndorsements }}</div>
                <div class="card-change">笔背书</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon pending-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">待审批</div>
                <div class="card-value">{{ pendingEndorsements }}</div>
                <div class="card-change warning">需处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon approved-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已完成</div>
                <div class="card-value">{{ completedEndorsements }}</div>
                <div class="card-change positive">本月</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">背书金额</div>
                <div class="card-value">{{ totalAmount }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="背书编号">
            <el-input
              v-model="listQuery.endorsementNumber"
              placeholder="请输入背书编号"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="票据号码">
            <el-input
              v-model="listQuery.billNumber"
              placeholder="请输入票据号码"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="背书状态">
            <el-select
              v-model="listQuery.endorsementStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待审批" value="PENDING" />
              <el-option label="已审批" value="APPROVED" />
              <el-option label="已执行" value="EXECUTED" />
              <el-option label="已拒绝" value="REJECTED" />
              <el-option label="已撤销" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item label="背书类型">
            <el-select
              v-model="listQuery.endorsementType"
              placeholder="请选择类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="转让背书" value="TRANSFER" />
              <el-option label="质押背书" value="PLEDGE" />
              <el-option label="委托收款" value="COLLECTION" />
            </el-select>
          </el-form-item>
          <el-form-item label="申请日期">
            <el-date-picker
              v-model="listQuery.applicationDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 背书表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="endorsementList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="背书ID" prop="endorsementId" width="80" align="center" />
        <el-table-column label="背书编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.endorsementNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票据号码" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.billNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="背书类型" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getEndorsementTypeTagType(row.endorsementType)" size="mini">
              {{ getEndorsementTypeText(row.endorsementType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="票据金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="bill-amount">{{ formatCurrency(row.billAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="背书人" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.endorserName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="被背书人" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.endorseeName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="申请日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.applicationDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="背书状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getStatusTagType(row.endorsementStatus)" size="mini">
              {{ getStatusText(row.endorsementStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="申请人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.applicantName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.endorsementStatus === 'PENDING'" type="success" size="mini" @click="handleApprove(row)">
              审批
            </el-button>
            <el-button type="primary" size="mini" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-if="row.endorsementStatus === 'PENDING'" :command="{action: 'reject', row: row}">拒绝</el-dropdown-item>
                <el-dropdown-item v-if="row.endorsementStatus === 'APPROVED'" :command="{action: 'execute', row: row}">执行</el-dropdown-item>
                <el-dropdown-item v-if="row.endorsementStatus === 'PENDING'" :command="{action: 'cancel', row: row}">撤销</el-dropdown-item>
                <el-dropdown-item :command="{action: 'history', row: row}">历史记录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑背书对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-tabs v-model="activeFormTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="背书编号" prop="endorsementNumber">
                  <el-input v-model="temp.endorsementNumber" placeholder="系统自动生成" :disabled="dialogStatus === 'update'" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="背书类型" prop="endorsementType">
                  <el-select v-model="temp.endorsementType" placeholder="请选择背书类型" style="width: 100%;">
                    <el-option label="转让背书" value="TRANSFER" />
                    <el-option label="质押背书" value="PLEDGE" />
                    <el-option label="委托收款" value="COLLECTION" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票据选择" prop="billNumber">
                  <el-select v-model="temp.billNumber" placeholder="请选择票据" style="width: 100%;" @change="handleBillChange">
                    <el-option
                      v-for="bill in availableBills"
                      :key="bill.billNumber"
                      :label="`${bill.billNumber} - ${formatCurrency(bill.billAmount)}`"
                      :value="bill.billNumber"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="票据金额">
                  <el-input v-model="temp.billAmount" :disabled="true" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="申请日期" prop="applicationDate">
                  <el-date-picker
                    v-model="temp.applicationDate"
                    type="date"
                    placeholder="选择申请日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="预计执行日期">
                  <el-date-picker
                    v-model="temp.expectedExecutionDate"
                    type="date"
                    placeholder="选择预计执行日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="背书信息" name="endorsement">
            <el-form-item label="背书人" prop="endorserName">
              <el-input v-model="temp.endorserName" placeholder="请输入背书人名称" />
            </el-form-item>
            <el-form-item label="背书人账号">
              <el-input v-model="temp.endorserAccount" placeholder="请输入背书人账号" />
            </el-form-item>
            <el-form-item label="被背书人" prop="endorseeName">
              <el-input v-model="temp.endorseeName" placeholder="请输入被背书人名称" />
            </el-form-item>
            <el-form-item label="被背书人账号">
              <el-input v-model="temp.endorseeAccount" placeholder="请输入被背书人账号" />
            </el-form-item>
            <el-form-item label="被背书人开户行">
              <el-input v-model="temp.endorseeBank" placeholder="请输入被背书人开户行" />
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="temp.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="其他信息" name="others">
            <el-form-item label="背书用途">
              <el-select v-model="temp.endorsementPurpose" placeholder="请选择背书用途" style="width: 100%;">
                <el-option label="货款支付" value="PAYMENT" />
                <el-option label="债务清偿" value="DEBT_SETTLEMENT" />
                <el-option label="投资款项" value="INVESTMENT" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
            <el-form-item label="是否连续背书">
              <el-radio-group v-model="temp.isContinuous">
                <el-radio :label="true">是</el-radio>
                <el-radio :label="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="风险等级">
              <el-select v-model="temp.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
                <el-option label="低风险" value="LOW" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="高风险" value="HIGH" />
              </el-select>
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="temp.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 背书详情对话框 -->
    <el-dialog title="背书详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentEndorsement" class="endorsement-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="背书编号">{{ currentEndorsement.endorsementNumber }}</el-descriptions-item>
          <el-descriptions-item label="背书类型">{{ getEndorsementTypeText(currentEndorsement.endorsementType) }}</el-descriptions-item>
          <el-descriptions-item label="票据号码">{{ currentEndorsement.billNumber }}</el-descriptions-item>
          <el-descriptions-item label="票据金额">{{ formatCurrency(currentEndorsement.billAmount) }}</el-descriptions-item>
          <el-descriptions-item label="背书人">{{ currentEndorsement.endorserName }}</el-descriptions-item>
          <el-descriptions-item label="被背书人">{{ currentEndorsement.endorseeName }}</el-descriptions-item>
          <el-descriptions-item label="申请日期">{{ currentEndorsement.applicationDate }}</el-descriptions-item>
          <el-descriptions-item label="背书状态">
            <el-tag :type="getStatusTagType(currentEndorsement.endorsementStatus)">
              {{ getStatusText(currentEndorsement.endorsementStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="申请人">{{ currentEndorsement.applicantName }}</el-descriptions-item>
          <el-descriptions-item label="审批人">{{ currentEndorsement.approverName || '待审批' }}</el-descriptions-item>
        </el-descriptions>
        
        <!-- 审批流程 -->
        <div class="approval-process">
          <h4>审批流程</h4>
          <el-steps :active="getApprovalStep(currentEndorsement.endorsementStatus)" finish-status="success">
            <el-step title="申请提交" :description="currentEndorsement.applicationDate"></el-step>
            <el-step title="风险评估" :description="currentEndorsement.riskAssessmentDate || '待评估'"></el-step>
            <el-step title="审批决定" :description="currentEndorsement.approvalDate || '待审批'"></el-step>
            <el-step title="执行完成" :description="currentEndorsement.executionDate || '待执行'"></el-step>
          </el-steps>
        </div>
        
        <div style="margin-top: 20px;">
          <h4>备注信息</h4>
          <p>{{ currentEndorsement.remark || '无' }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentEndorsement && currentEndorsement.endorsementStatus === 'PENDING'" type="success" @click="handleApprove(currentEndorsement)">
          审批
        </el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog title="背书审批" :visible.sync="dialogApprovalVisible" width="600px">
      <el-form ref="approvalForm" :model="approvalForm" label-width="100px">
        <el-form-item label="审批结果" prop="approvalResult">
          <el-radio-group v-model="approvalForm.approvalResult">
            <el-radio label="APPROVED">通过</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见" prop="approvalComment">
          <el-input v-model="approvalForm.approvalComment" type="textarea" :rows="4" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogApprovalVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApproval">确认审批</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBillEndorsementPage, createBillEndorsement, updateBillEndorsement } from '@/api/globalTreasurer/pzgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'BillEndorsementManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        endorsementNumber: undefined,
        billNumber: undefined,
        endorsementStatus: undefined,
        endorsementType: undefined,
        applicationDateRange: undefined
      },
      totalEndorsements: 89,
      pendingEndorsements: 12,
      completedEndorsements: 65,
      totalAmount: 18560.8,
      endorsementList: [],
      multipleSelection: [],
      currentEndorsement: null,
      availableBills: [
        { billNumber: 'BA20240925001', billAmount: 1000000.00 },
        { billNumber: 'CA20240920002', billAmount: 500000.00 }
      ],
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogApprovalVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      activeFormTab: 'basic',
      temp: {
        endorsementId: undefined,
        endorsementNumber: '',
        endorsementType: '',
        billNumber: '',
        billAmount: '',
        endorserName: '',
        endorserAccount: '',
        endorseeName: '',
        endorseeAccount: '',
        endorseeBank: '',
        contactPhone: '',
        applicationDate: null,
        expectedExecutionDate: null,
        endorsementPurpose: '',
        isContinuous: true,
        riskLevel: 'LOW',
        remark: ''
      },
      approvalForm: {
        approvalResult: '',
        approvalComment: ''
      },
      rules: {
        endorsementNumber: [{ required: true, message: '背书编号不能为空', trigger: 'blur' }],
        endorsementType: [{ required: true, message: '请选择背书类型', trigger: 'change' }],
        billNumber: [{ required: true, message: '请选择票据', trigger: 'change' }],
        endorserName: [{ required: true, message: '背书人不能为空', trigger: 'blur' }],
        endorseeName: [{ required: true, message: '被背书人不能为空', trigger: 'blur' }],
        applicationDate: [{ required: true, message: '请选择申请日期', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.endorsementList = [
          {
            endorsementId: 1,
            endorsementNumber: 'EN20240925001',
            endorsementType: 'TRANSFER',
            billNumber: 'BA20240925001',
            billAmount: 1000000.00,
            endorserName: '示例云科技有限公司',
            endorserAccount: '1234567890',
            endorseeName: '供应商A',
            endorseeAccount: '0987654321',
            endorseeBank: '中国建设银行',
            contactPhone: '13800138000',
            applicationDate: '2024-09-25',
            expectedExecutionDate: '2024-09-26',
            endorsementStatus: 'PENDING',
            applicantName: '张三',
            approverName: '',
            endorsementPurpose: 'PAYMENT',
            isContinuous: true,
            riskLevel: 'LOW',
            remark: '货款支付背书'
          },
          {
            endorsementId: 2,
            endorsementNumber: 'EN20240920002',
            endorsementType: 'PLEDGE',
            billNumber: 'CA20240920002',
            billAmount: 500000.00,
            endorserName: '示例云科技有限公司',
            endorserAccount: '1234567890',
            endorseeName: '银行B',
            endorseeAccount: '1111222233',
            endorseeBank: '中国工商银行',
            contactPhone: '13900139000',
            applicationDate: '2024-09-20',
            expectedExecutionDate: '2024-09-21',
            endorsementStatus: 'APPROVED',
            applicantName: '李四',
            approverName: '王五',
            endorsementPurpose: 'DEBT_SETTLEMENT',
            isContinuous: false,
            riskLevel: 'MEDIUM',
            remark: '质押担保背书'
          }
        ]
        this.total = this.endorsementList.length
        this.listLoading = false
      }, 1000)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        endorsementNumber: undefined,
        billNumber: undefined,
        endorsementStatus: undefined,
        endorsementType: undefined,
        applicationDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreateEndorsement() {
      this.resetTemp()
      this.temp.endorsementNumber = 'EN' + new Date().toISOString().slice(0, 10).replace(/-/g, '') + Math.floor(Math.random() * 1000).toString().padStart(3, '0')
      this.dialogStatus = 'create'
      this.dialogTitle = '新增背书'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑背书'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentEndorsement = row
      this.dialogDetailVisible = true
    },
    handleApprove(row) {
      this.currentEndorsement = row
      this.approvalForm = {
        approvalResult: '',
        approvalComment: ''
      }
      this.dialogApprovalVisible = true
    },
    handleBatchApprove() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要审批的背书记录'
        })
        return
      }
      this.$confirm('确认批量审批选中的背书记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.multipleSelection.forEach(item => {
          if (item.endorsementStatus === 'PENDING') {
            item.endorsementStatus = 'APPROVED'
            item.approverName = '当前用户'
            item.approvalDate = new Date().toISOString().slice(0, 10)
          }
        })
        this.$message({
          type: 'success',
          message: '批量审批成功!'
        })
      })
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'reject':
          this.handleReject(row)
          break
        case 'execute':
          this.handleExecute(row)
          break
        case 'cancel':
          this.handleCancel(row)
          break
        case 'history':
          this.handleViewHistory(row)
          break
      }
    },
    handleReject(row) {
      this.$confirm('确认拒绝该背书申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.endorsementStatus = 'REJECTED'
        row.approverName = '当前用户'
        this.$message({
          type: 'success',
          message: '背书申请已拒绝!'
        })
      })
    },
    handleExecute(row) {
      this.$confirm('确认执行该背书?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.endorsementStatus = 'EXECUTED'
        row.executionDate = new Date().toISOString().slice(0, 10)
        this.$message({
          type: 'success',
          message: '背书执行成功!'
        })
      })
    },
    handleCancel(row) {
      this.$confirm('确认撤销该背书申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.endorsementStatus = 'CANCELLED'
        this.$message({
          type: 'success',
          message: '背书申请已撤销!'
        })
      })
    },
    handleViewHistory(row) {
      this.$message({
        type: 'info',
        message: '查看背书历史记录功能'
      })
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '背书数据导出成功'
      })
    },
    handleBillChange(billNumber) {
      const selectedBill = this.availableBills.find(bill => bill.billNumber === billNumber)
      if (selectedBill) {
        this.temp.billAmount = this.formatCurrency(selectedBill.billAmount)
      }
    },
    submitApproval() {
      if (!this.approvalForm.approvalResult) {
        this.$message({
          type: 'warning',
          message: '请选择审批结果'
        })
        return
      }
      
      this.currentEndorsement.endorsementStatus = this.approvalForm.approvalResult
      this.currentEndorsement.approverName = '当前用户'
      this.currentEndorsement.approvalDate = new Date().toISOString().slice(0, 10)
      this.currentEndorsement.approvalComment = this.approvalForm.approvalComment
      
      this.dialogApprovalVisible = false
      this.dialogDetailVisible = false
      
      this.$message({
        type: 'success',
        message: '审批完成!'
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.endorsementId = parseInt(Math.random() * 100) + 1024
          this.temp.endorsementStatus = 'PENDING'
          this.temp.applicantName = '当前用户'
          if (this.temp.applicationDate) {
            this.temp.applicationDate = this.temp.applicationDate.toISOString().slice(0, 10)
          }
          if (this.temp.expectedExecutionDate) {
            this.temp.expectedExecutionDate = this.temp.expectedExecutionDate.toISOString().slice(0, 10)
          }
          this.endorsementList.unshift(this.temp)
          this.total = this.endorsementList.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '背书申请创建成功'
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.temp.applicationDate && typeof this.temp.applicationDate === 'object') {
            this.temp.applicationDate = this.temp.applicationDate.toISOString().slice(0, 10)
          }
          if (this.temp.expectedExecutionDate && typeof this.temp.expectedExecutionDate === 'object') {
            this.temp.expectedExecutionDate = this.temp.expectedExecutionDate.toISOString().slice(0, 10)
          }
          const index = this.endorsementList.findIndex(v => v.endorsementId === this.temp.endorsementId)
          this.endorsementList.splice(index, 1, this.temp)
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '背书信息更新成功'
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        endorsementId: undefined,
        endorsementNumber: '',
        endorsementType: '',
        billNumber: '',
        billAmount: '',
        endorserName: '',
        endorserAccount: '',
        endorseeName: '',
        endorseeAccount: '',
        endorseeBank: '',
        contactPhone: '',
        applicationDate: null,
        expectedExecutionDate: null,
        endorsementPurpose: '',
        isContinuous: true,
        riskLevel: 'LOW',
        remark: ''
      }
    },
    getEndorsementTypeTagType(type) {
      const typeMap = {
        'TRANSFER': 'primary',
        'PLEDGE': 'warning',
        'COLLECTION': 'success'
      }
      return typeMap[type] || 'info'
    },
    getEndorsementTypeText(type) {
      const textMap = {
        'TRANSFER': '转让背书',
        'PLEDGE': '质押背书',
        'COLLECTION': '委托收款'
      }
      return textMap[type] || type
    },
    getStatusTagType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'EXECUTED': 'primary',
        'REJECTED': 'danger',
        'CANCELLED': 'info'
      }
      return typeMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'PENDING': '待审批',
        'APPROVED': '已审批',
        'EXECUTED': '已执行',
        'REJECTED': '已拒绝',
        'CANCELLED': '已撤销'
      }
      return textMap[status] || status
    },
    getApprovalStep(status) {
      const stepMap = {
        'PENDING': 1,
        'APPROVED': 3,
        'EXECUTED': 4,
        'REJECTED': 2,
        'CANCELLED': 0
      }
      return stepMap[status] || 0
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.bill-endorsement-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .endorsement-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.pending-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.approved-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.amount-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
        }
        .card-info {
          flex: 1;
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
          .card-change {
            font-size: 12px;
            color: #909399;
            &.positive {
              color: #67C23A;
            }
            &.warning {
              color: #E6A23C;
            }
          }
        }
      }
    }
  }

  .search-card, .table-card {
    margin-bottom: 20px;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .bill-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .endorsement-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }
    
    .approval-process {
      margin: 20px 0;
      
      h4 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 14px;
        font-weight: 600;
      }
    }
    
    h4 {
      margin: 16px 0 8px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }
    
    p {
      margin: 0;
      color: #606266;
      line-height: 1.5;
    }
  }
}
</style>
