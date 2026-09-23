<template>
  <div class="bill-discount-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-money"></i>
            票据贴现管理
          </h2>
          <p class="page-description">管理票据贴现申请、审批和执行流程</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreateDiscount">
            新增贴现
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

    <!-- 贴现概览卡片 -->
    <div class="discount-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总贴现数</div>
                <div class="card-value">{{ totalDiscounts }}</div>
                <div class="card-change">笔贴现</div>
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
                <div class="card-value">{{ pendingDiscounts }}</div>
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
                <div class="card-value">{{ completedDiscounts }}</div>
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
                <div class="card-title">贴现金额</div>
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
          <el-form-item label="贴现编号">
            <el-input
              v-model="listQuery.discountNumber"
              placeholder="请输入贴现编号"
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
          <el-form-item label="贴现状态">
            <el-select
              v-model="listQuery.discountStatus"
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
          <el-form-item label="贴现银行">
            <el-select
              v-model="listQuery.discountBank"
              placeholder="请选择银行"
              clearable
              style="width: 150px;"
            >
              <el-option label="中国工商银行" value="ICBC" />
              <el-option label="中国建设银行" value="CCB" />
              <el-option label="中国银行" value="BOC" />
              <el-option label="中国农业银行" value="ABC" />
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

    <!-- 贴现表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="discountList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="贴现ID" prop="discountId" width="80" align="center" />
        <el-table-column label="贴现编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.discountNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票据号码" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.billNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票据金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="bill-amount">{{ formatCurrency(row.billAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="贴现金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="discount-amount">{{ formatCurrency(row.discountAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="贴现利率" width="100px" align="center">
          <template slot-scope="{row}">
            <span class="discount-rate">{{ row.discountRate }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="贴现银行" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ getBankName(row.discountBank) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="申请日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.applicationDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.maturityDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="贴现状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getStatusTagType(row.discountStatus)" size="mini">
              {{ getStatusText(row.discountStatus) }}
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
            <el-button v-if="row.discountStatus === 'PENDING'" type="success" size="mini" @click="handleApprove(row)">
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
                <el-dropdown-item v-if="row.discountStatus === 'PENDING'" :command="{action: 'reject', row: row}">拒绝</el-dropdown-item>
                <el-dropdown-item v-if="row.discountStatus === 'APPROVED'" :command="{action: 'execute', row: row}">执行</el-dropdown-item>
                <el-dropdown-item v-if="row.discountStatus === 'PENDING'" :command="{action: 'cancel', row: row}">撤销</el-dropdown-item>
                <el-dropdown-item :command="{action: 'calculate', row: row}">计算利息</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑贴现对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-tabs v-model="activeFormTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="贴现编号" prop="discountNumber">
                  <el-input v-model="temp.discountNumber" placeholder="系统自动生成" :disabled="dialogStatus === 'update'" />
                </el-form-item>
              </el-col>
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
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票据金额">
                  <el-input v-model="temp.billAmount" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期日期">
                  <el-input v-model="temp.maturityDate" :disabled="true" />
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
                <el-form-item label="贴现银行" prop="discountBank">
                  <el-select v-model="temp.discountBank" placeholder="请选择贴现银行" style="width: 100%;">
                    <el-option label="中国工商银行" value="ICBC" />
                    <el-option label="中国建设银行" value="CCB" />
                    <el-option label="中国银行" value="BOC" />
                    <el-option label="中国农业银行" value="ABC" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="贴现条件" name="conditions">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="贴现利率" prop="discountRate">
                  <el-input-number
                    v-model="temp.discountRate"
                    :precision="4"
                    :min="0"
                    :max="100"
                    style="width: 100%;"
                    placeholder="请输入贴现利率"
                    @change="calculateDiscountAmount"
                  />
                  <span style="margin-left: 8px;">%</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="贴现天数">
                  <el-input-number
                    v-model="temp.discountDays"
                    :min="1"
                    style="width: 100%;"
                    placeholder="自动计算"
                    :disabled="true"
                  />
                  <span style="margin-left: 8px;">天</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="贴现利息">
                  <el-input-number
                    v-model="temp.discountInterest"
                    :precision="2"
                    style="width: 100%;"
                    placeholder="自动计算"
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="贴现金额">
                  <el-input-number
                    v-model="temp.discountAmount"
                    :precision="2"
                    style="width: 100%;"
                    placeholder="自动计算"
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="手续费率">
                  <el-input-number
                    v-model="temp.feeRate"
                    :precision="4"
                    :min="0"
                    :max="100"
                    style="width: 100%;"
                    placeholder="请输入手续费率"
                    @change="calculateDiscountAmount"
                  />
                  <span style="margin-left: 8px;">%</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手续费">
                  <el-input-number
                    v-model="temp.handlingFee"
                    :precision="2"
                    style="width: 100%;"
                    placeholder="自动计算"
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="其他信息" name="others">
            <el-form-item label="贴现用途">
              <el-select v-model="temp.discountPurpose" placeholder="请选择贴现用途" style="width: 100%;">
                <el-option label="流动资金" value="WORKING_CAPITAL" />
                <el-option label="投资项目" value="INVESTMENT" />
                <el-option label="债务偿还" value="DEBT_REPAYMENT" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
            <el-form-item label="风险等级">
              <el-select v-model="temp.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
                <el-option label="低风险" value="LOW" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="高风险" value="HIGH" />
              </el-select>
            </el-form-item>
            <el-form-item label="是否追索">
              <el-radio-group v-model="temp.isRecourse">
                <el-radio :label="true">有追索权</el-radio>
                <el-radio :label="false">无追索权</el-radio>
              </el-radio-group>
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

    <!-- 贴现详情对话框 -->
    <el-dialog title="贴现详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentDiscount" class="discount-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="贴现编号">{{ currentDiscount.discountNumber }}</el-descriptions-item>
          <el-descriptions-item label="票据号码">{{ currentDiscount.billNumber }}</el-descriptions-item>
          <el-descriptions-item label="票据金额">{{ formatCurrency(currentDiscount.billAmount) }}</el-descriptions-item>
          <el-descriptions-item label="贴现金额">{{ formatCurrency(currentDiscount.discountAmount) }}</el-descriptions-item>
          <el-descriptions-item label="贴现利率">{{ currentDiscount.discountRate }}%</el-descriptions-item>
          <el-descriptions-item label="贴现利息">{{ formatCurrency(currentDiscount.discountInterest) }}</el-descriptions-item>
          <el-descriptions-item label="贴现银行">{{ getBankName(currentDiscount.discountBank) }}</el-descriptions-item>
          <el-descriptions-item label="申请日期">{{ currentDiscount.applicationDate }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ currentDiscount.maturityDate }}</el-descriptions-item>
          <el-descriptions-item label="贴现状态">
            <el-tag :type="getStatusTagType(currentDiscount.discountStatus)">
              {{ getStatusText(currentDiscount.discountStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="申请人">{{ currentDiscount.applicantName }}</el-descriptions-item>
          <el-descriptions-item label="审批人">{{ currentDiscount.approverName || '待审批' }}</el-descriptions-item>
        </el-descriptions>
        
        <!-- 审批流程 -->
        <div class="approval-process">
          <h4>审批流程</h4>
          <el-steps :active="getApprovalStep(currentDiscount.discountStatus)" finish-status="success">
            <el-step title="申请提交" :description="currentDiscount.applicationDate"></el-step>
            <el-step title="风险评估" :description="currentDiscount.riskAssessmentDate || '待评估'"></el-step>
            <el-step title="审批决定" :description="currentDiscount.approvalDate || '待审批'"></el-step>
            <el-step title="执行完成" :description="currentDiscount.executionDate || '待执行'"></el-step>
          </el-steps>
        </div>
        
        <div style="margin-top: 20px;">
          <h4>备注信息</h4>
          <p>{{ currentDiscount.remark || '无' }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentDiscount && currentDiscount.discountStatus === 'PENDING'" type="success" @click="handleApprove(currentDiscount)">
          审批
        </el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog title="贴现审批" :visible.sync="dialogApprovalVisible" width="600px">
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

    <!-- 利息计算对话框 -->
    <el-dialog title="贴现利息计算" :visible.sync="dialogCalculateVisible" width="600px">
      <div v-if="calculateResult" class="calculate-result">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="票据金额">{{ formatCurrency(calculateResult.billAmount) }}</el-descriptions-item>
          <el-descriptions-item label="贴现利率">{{ calculateResult.discountRate }}%</el-descriptions-item>
          <el-descriptions-item label="贴现天数">{{ calculateResult.discountDays }}天</el-descriptions-item>
          <el-descriptions-item label="贴现利息">{{ formatCurrency(calculateResult.discountInterest) }}</el-descriptions-item>
          <el-descriptions-item label="手续费">{{ formatCurrency(calculateResult.handlingFee) }}</el-descriptions-item>
          <el-descriptions-item label="实际到账金额">
            <span class="highlight-amount">{{ formatCurrency(calculateResult.actualAmount) }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCalculateVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBillDiscountPage, createBillDiscount, updateBillDiscount, calculateDiscountInterest } from '@/api/globalTreasurer/pzgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'BillDiscountManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        discountNumber: undefined,
        billNumber: undefined,
        discountStatus: undefined,
        discountBank: undefined,
        applicationDateRange: undefined
      },
      totalDiscounts: 67,
      pendingDiscounts: 8,
      completedDiscounts: 52,
      totalAmount: 12680.5,
      discountList: [],
      multipleSelection: [],
      currentDiscount: null,
      calculateResult: null,
      availableBills: [
        { billNumber: 'BA20240925001', billAmount: 1000000.00, maturityDate: '2024-12-25' },
        { billNumber: 'CA20240920002', billAmount: 500000.00, maturityDate: '2024-10-20' }
      ],
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogApprovalVisible: false,
      dialogCalculateVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      activeFormTab: 'basic',
      temp: {
        discountId: undefined,
        discountNumber: '',
        billNumber: '',
        billAmount: '',
        maturityDate: '',
        discountBank: '',
        discountRate: 0,
        discountDays: 0,
        discountInterest: 0,
        discountAmount: 0,
        feeRate: 0,
        handlingFee: 0,
        applicationDate: null,
        discountPurpose: '',
        riskLevel: 'LOW',
        isRecourse: true,
        remark: ''
      },
      approvalForm: {
        approvalResult: '',
        approvalComment: ''
      },
      rules: {
        discountNumber: [{ required: true, message: '贴现编号不能为空', trigger: 'blur' }],
        billNumber: [{ required: true, message: '请选择票据', trigger: 'change' }],
        discountBank: [{ required: true, message: '请选择贴现银行', trigger: 'change' }],
        discountRate: [{ required: true, message: '请输入贴现利率', trigger: 'blur' }],
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
        this.discountList = [
          {
            discountId: 1,
            discountNumber: 'DC20240925001',
            billNumber: 'BA20240925001',
            billAmount: 1000000.00,
            discountAmount: 985000.00,
            discountRate: 4.5000,
            discountInterest: 15000.00,
            discountDays: 90,
            discountBank: 'ICBC',
            applicationDate: '2024-09-25',
            maturityDate: '2024-12-25',
            discountStatus: 'PENDING',
            applicantName: '张三',
            approverName: '',
            discountPurpose: 'WORKING_CAPITAL',
            riskLevel: 'LOW',
            isRecourse: true,
            feeRate: 0.1,
            handlingFee: 1000.00,
            remark: '流动资金贴现'
          },
          {
            discountId: 2,
            discountNumber: 'DC20240920002',
            billNumber: 'CA20240920002',
            billAmount: 500000.00,
            discountAmount: 495000.00,
            discountRate: 5.0000,
            discountInterest: 5000.00,
            discountDays: 30,
            discountBank: 'CCB',
            applicationDate: '2024-09-20',
            maturityDate: '2024-10-20',
            discountStatus: 'APPROVED',
            applicantName: '李四',
            approverName: '王五',
            discountPurpose: 'INVESTMENT',
            riskLevel: 'MEDIUM',
            isRecourse: false,
            feeRate: 0.05,
            handlingFee: 250.00,
            remark: '投资项目贴现'
          }
        ]
        this.total = this.discountList.length
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
        discountNumber: undefined,
        billNumber: undefined,
        discountStatus: undefined,
        discountBank: undefined,
        applicationDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreateDiscount() {
      this.resetTemp()
      this.temp.discountNumber = 'DC' + new Date().toISOString().slice(0, 10).replace(/-/g, '') + Math.floor(Math.random() * 1000).toString().padStart(3, '0')
      this.dialogStatus = 'create'
      this.dialogTitle = '新增贴现'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑贴现'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentDiscount = row
      this.dialogDetailVisible = true
    },
    handleApprove(row) {
      this.currentDiscount = row
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
          message: '请选择要审批的贴现记录'
        })
        return
      }
      this.$confirm('确认批量审批选中的贴现记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.multipleSelection.forEach(item => {
          if (item.discountStatus === 'PENDING') {
            item.discountStatus = 'APPROVED'
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
        case 'calculate':
          this.handleCalculateInterest(row)
          break
      }
    },
    handleReject(row) {
      this.$confirm('确认拒绝该贴现申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.discountStatus = 'REJECTED'
        row.approverName = '当前用户'
        this.$message({
          type: 'success',
          message: '贴现申请已拒绝!'
        })
      })
    },
    handleExecute(row) {
      this.$confirm('确认执行该贴现?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.discountStatus = 'EXECUTED'
        row.executionDate = new Date().toISOString().slice(0, 10)
        this.$message({
          type: 'success',
          message: '贴现执行成功!'
        })
      })
    },
    handleCancel(row) {
      this.$confirm('确认撤销该贴现申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.discountStatus = 'CANCELLED'
        this.$message({
          type: 'success',
          message: '贴现申请已撤销!'
        })
      })
    },
    handleCalculateInterest(row) {
      this.calculateResult = {
        billAmount: row.billAmount,
        discountRate: row.discountRate,
        discountDays: row.discountDays,
        discountInterest: row.discountInterest,
        handlingFee: row.handlingFee,
        actualAmount: row.discountAmount - row.handlingFee
      }
      this.dialogCalculateVisible = true
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '贴现数据导出成功'
      })
    },
    handleBillChange(billNumber) {
      const selectedBill = this.availableBills.find(bill => bill.billNumber === billNumber)
      if (selectedBill) {
        this.temp.billAmount = this.formatCurrency(selectedBill.billAmount)
        this.temp.maturityDate = selectedBill.maturityDate
        this.calculateDiscountDays()
      }
    },
    calculateDiscountDays() {
      if (this.temp.applicationDate && this.temp.maturityDate) {
        const appDate = new Date(this.temp.applicationDate)
        const matDate = new Date(this.temp.maturityDate)
        const diffTime = Math.abs(matDate - appDate)
        this.temp.discountDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
        this.calculateDiscountAmount()
      }
    },
    calculateDiscountAmount() {
      if (this.temp.billAmount && this.temp.discountRate && this.temp.discountDays) {
        const billAmount = parseFloat(this.temp.billAmount.toString().replace(/[¥,]/g, ''))
        // 计算贴现利息：票面金额 × 贴现利率 × 贴现天数 / 360
        this.temp.discountInterest = (billAmount * this.temp.discountRate / 100 * this.temp.discountDays / 360).toFixed(2)
        // 计算手续费
        this.temp.handlingFee = (billAmount * (this.temp.feeRate || 0) / 100).toFixed(2)
        // 计算贴现金额：票面金额 - 贴现利息
        this.temp.discountAmount = (billAmount - parseFloat(this.temp.discountInterest)).toFixed(2)
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
      
      this.currentDiscount.discountStatus = this.approvalForm.approvalResult
      this.currentDiscount.approverName = '当前用户'
      this.currentDiscount.approvalDate = new Date().toISOString().slice(0, 10)
      this.currentDiscount.approvalComment = this.approvalForm.approvalComment
      
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
          this.temp.discountId = parseInt(Math.random() * 100) + 1024
          this.temp.discountStatus = 'PENDING'
          this.temp.applicantName = '当前用户'
          if (this.temp.applicationDate) {
            this.temp.applicationDate = this.temp.applicationDate.toISOString().slice(0, 10)
          }
          this.discountList.unshift(this.temp)
          this.total = this.discountList.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '贴现申请创建成功'
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
          const index = this.discountList.findIndex(v => v.discountId === this.temp.discountId)
          this.discountList.splice(index, 1, this.temp)
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '贴现信息更新成功'
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        discountId: undefined,
        discountNumber: '',
        billNumber: '',
        billAmount: '',
        maturityDate: '',
        discountBank: '',
        discountRate: 0,
        discountDays: 0,
        discountInterest: 0,
        discountAmount: 0,
        feeRate: 0,
        handlingFee: 0,
        applicationDate: null,
        discountPurpose: '',
        riskLevel: 'LOW',
        isRecourse: true,
        remark: ''
      }
    },
    getBankName(bankCode) {
      const bankMap = {
        'ICBC': '中国工商银行',
        'CCB': '中国建设银行',
        'BOC': '中国银行',
        'ABC': '中国农业银行'
      }
      return bankMap[bankCode] || bankCode
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
  },
  watch: {
    'temp.applicationDate'() {
      this.calculateDiscountDays()
    }
  }
}
</script>

<style lang="scss" scoped>
.bill-discount-manage {
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

  .discount-overview {
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

  .bill-amount, .discount-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .discount-rate {
    font-weight: 600;
    color: #E6A23C;
  }

  .discount-detail {
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

  .calculate-result {
    .highlight-amount {
      font-size: 18px;
      font-weight: 600;
      color: #E6A23C;
    }
  }
}
</style>
