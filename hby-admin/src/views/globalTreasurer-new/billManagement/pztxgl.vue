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
            <span>{{ formatDateParam(row.applicationDate) || '' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDateParam(row.maturityDate) || '' }}</span>
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

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />
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
                <el-form-item label="票据金额" prop="billAmount">
                  <el-input v-model="temp.billAmount" placeholder="请输入票据金额" @change="calculateDiscountAmount" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期日期" prop="maturityDate">
                  <el-date-picker
                    v-model="temp.maturityDate"
                    type="date"
                    placeholder="选择到期日期"
                    value-format="yyyy-MM-dd"
                    style="width: 100%;"
                    :append-to-body="true"
                    @change="calculateDiscountDays"
                  />
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

    <!-- 批量审批对话框 -->
    <el-dialog title="批量审批贴现" :visible.sync="dialogBatchApprovalVisible" width="600px">
      <div class="batch-approval-info">
        <el-alert
          :title="`已选择 ${multipleSelection.filter(item => item.discountStatus === 'PENDING').length} 条待审批记录`"
          type="info"
          :closable="false"
          show-icon
          style="margin-bottom: 20px;"
        />
      </div>
      <el-form ref="batchApprovalForm" :model="batchApprovalForm" label-width="100px">
        <el-form-item label="审批结果" prop="approvalResult">
          <el-radio-group v-model="batchApprovalForm.approvalResult">
            <el-radio label="APPROVED">全部通过</el-radio>
            <el-radio label="REJECTED">全部拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见" prop="approvalComment">
          <el-input v-model="batchApprovalForm.approvalComment" type="textarea" :rows="4" placeholder="请输入审批意见（可选）" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogBatchApprovalVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBatchApproval">确认批量审批</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBillDiscountPage,
  getBillDiscountDetail,
  createBillDiscount,
  updateBillDiscount,
  deleteBillDiscount,
  approveBillDiscount,
  batchApproveBillDiscount,
  executeBillDiscount,
  cancelBillDiscount,
  calculateDiscountInterest,
  exportBillDiscount,
  getBillDiscountStatistics,
  getAvailableBillsForDiscount
} from '@/api/globalTreasurer-new/billManagement/billDiscount'
import Pagination from '@/components/Pagination'

export default {
  name: 'BillDiscountManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        pageNum: 1,
        pageSize: 20,
        discountNumber: undefined,
        billNumber: undefined,
        discountStatus: undefined,
        discountBank: undefined,
        applicationDateRange: [],
        applicationDateStart: undefined,
        applicationDateEnd: undefined
      },
      totalDiscounts: 0,
      pendingDiscounts: 0,
      completedDiscounts: 0,
      totalAmount: 0,
      discountList: [],
      multipleSelection: [],
      currentDiscount: null,
      calculateResult: null,
      availableBills: [],
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogApprovalVisible: false,
      dialogCalculateVisible: false,
      dialogBatchApprovalVisible: false,
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
      batchApprovalForm: {
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
    this.loadStatistics()
    this.loadAvailableBills()
  },
  methods: {
    // 格式化日期参数
    formatDateParam(date) {
      if (!date) return undefined
      if (typeof date === 'string' && isNaN(date)) return date
      const d = new Date(typeof date === 'string' ? Number(date) : date)
      if (isNaN(d.getTime())) return date
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    // 加载统计数据
    loadStatistics() {
      getBillDiscountStatistics({}).then(response => {
        if (response.code === 1 && response.data) {
          this.totalDiscounts = response.data.totalDiscounts || 0
          this.pendingDiscounts = response.data.pendingDiscounts || 0
          this.completedDiscounts = response.data.completedDiscounts || 0
          this.totalAmount = response.data.totalAmount || 0
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
      })
    },
    // 加载可贴现票据列表
    loadAvailableBills() {
      getAvailableBillsForDiscount({}).then(response => {
        if (response.code === 1) {
          const rawData = response.data || []
          // 打印原始数据的 key，用于调试
          if (rawData.length > 0) {
            console.log('原始数据第一条:', rawData[0])
            console.log('原始数据的所有 key:', Object.keys(rawData[0]))
          }
          // 兼容处理：将后端返回的数据统一转换为前端期望的格式
          this.availableBills = rawData.map(bill => this.normalizeBillData(bill))
          console.log('转换后的票据列表:', this.availableBills)
        }
      }).catch(error => {
        console.error('获取可贴现票据失败:', error)
      })
    },
    // 标准化票据数据格式，兼容多种命名格式
    normalizeBillData(bill) {
      return {
        // 兼容各种可能的 key 格式：全小写、驼峰、大写下划线
        billId: bill.billid || bill.billId || bill.BILL_ID || bill.instrumentid || bill.instrumentId || bill.INSTRUMENT_ID,
        billNumber: bill.billnumber || bill.billNumber || bill.BILL_NUMBER || bill.instrumentno || bill.instrumentNo || bill.INSTRUMENT_NO,
        billType: bill.billtype || bill.billType || bill.BILL_TYPE,
        billAmount: bill.billamount || bill.billAmount || bill.BILL_AMOUNT || 0,
        currency: bill.currency || bill.CURRENCY || bill.currencycode || bill.currencyCode || bill.CURRENCY_CODE || 'CNY',
        drawer: bill.drawer || bill.DRAWER,
        payee: bill.payee || bill.PAYEE,
        acceptor: bill.acceptor || bill.ACCEPTOR,
        issueDate: bill.issuedate || bill.issueDate || bill.ISSUE_DATE,
        maturityDate: bill.maturitydate || bill.maturityDate || bill.MATURITY_DATE,
        billStatus: bill.billstatus || bill.billStatus || bill.BILL_STATUS,
        remark: bill.remark || bill.REMARK
      }
    },
    getList() {
      this.listLoading = true
      // 处理日期范围参数
      const params = { ...this.listQuery }
      if (this.listQuery.applicationDateRange && this.listQuery.applicationDateRange.length === 2) {
        params.applicationDateStart = this.formatDateParam(this.listQuery.applicationDateRange[0])
        params.applicationDateEnd = this.formatDateParam(this.listQuery.applicationDateRange[1])
      }
      delete params.applicationDateRange

      // 调用后端API获取真实数据
      getBillDiscountPage(params).then(response => {
        if (response.code === 1) {
          // 兼容两种返回格式
          if (response.data && Array.isArray(response.data)) {
            this.discountList = response.data
            this.total = response.result ? response.result.total : response.data.length
          } else if (response.data && response.data.tlist) {
            this.discountList = response.data.tlist
            this.total = response.data.totalRecord || 0
          } else {
            this.discountList = response.result ? response.result.list || [] : []
            this.total = response.result ? response.result.total || 0 : 0
          }
        } else {
          this.$message({ type: 'error', message: response.msg || '获取数据失败' })
          this.discountList = []
          this.total = 0
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取票据贴现列表失败:', error)
        this.discountList = []
        this.total = 0
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        pageNum: 1,
        pageSize: 20,
        discountNumber: undefined,
        billNumber: undefined,
        discountStatus: undefined,
        discountBank: undefined,
        applicationDateRange: [],
        applicationDateStart: undefined,
        applicationDateEnd: undefined
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
      // 调用后端API获取详情
      getBillDiscountDetail(row.discountId).then(response => {
        if (response.code === 1) {
          this.currentDiscount = response.data || row
        } else {
          this.currentDiscount = row
        }
        this.dialogDetailVisible = true
      }).catch(error => {
        console.error('获取详情失败:', error)
        this.currentDiscount = row
        this.dialogDetailVisible = true
      })
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
      // 过滤出待审批的记录
      const pendingItems = this.multipleSelection.filter(item => item.discountStatus === 'PENDING')
      if (pendingItems.length === 0) {
        this.$message({
          type: 'warning',
          message: '选中的记录中没有待审批的贴现'
        })
        return
      }
      this.batchApprovalForm = {
        approvalResult: '',
        approvalComment: ''
      }
      this.dialogBatchApprovalVisible = true
    },
    submitBatchApproval() {
      if (!this.batchApprovalForm.approvalResult) {
        this.$message({
          type: 'warning',
          message: '请选择审批结果'
        })
        return
      }
      const pendingItems = this.multipleSelection.filter(item => item.discountStatus === 'PENDING')
      const discountIds = pendingItems.map(item => item.discountId)

      batchApproveBillDiscount({
        discountIds,
        approvalResult: this.batchApprovalForm.approvalResult,
        approvalComment: this.batchApprovalForm.approvalComment
      }).then(response => {
        if (response.code === 1) {
          this.dialogBatchApprovalVisible = false
          this.$message({
            type: 'success',
            message: '批量审批成功!'
          })
          this.getList()
          this.loadStatistics()
        } else {
          this.$message({
            type: 'error',
            message: response.msg || '批量审批失败'
          })
        }
      }).catch(error => {
        console.error('批量审批失败:', error)
        this.$message({
          type: 'error',
          message: '批量审批失败，请稍后重试'
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
        approveBillDiscount(row.discountId, {
          approvalResult: 'REJECTED',
          approvalComment: '拒绝贴现申请'
        }).then(response => {
          if (response.code === 1) {
            this.$message({
              type: 'success',
              message: '贴现申请已拒绝!'
            })
            this.getList()
            this.loadStatistics()
          } else {
            this.$message({
              type: 'error',
              message: response.msg || '操作失败'
            })
          }
        }).catch(error => {
          console.error('拒绝失败:', error)
          this.$message({
            type: 'error',
            message: '操作失败，请稍后重试'
          })
        })
      })
    },
    handleExecute(row) {
      this.$confirm('确认执行该贴现?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        executeBillDiscount(row.discountId).then(response => {
          if (response.code === 1) {
            this.$message({
              type: 'success',
              message: '贴现执行成功!'
            })
            this.getList()
            this.loadStatistics()
          } else {
            this.$message({
              type: 'error',
              message: response.msg || '执行失败'
            })
          }
        }).catch(error => {
          console.error('执行失败:', error)
          this.$message({
            type: 'error',
            message: '执行失败，请稍后重试'
          })
        })
      })
    },
    handleCancel(row) {
      this.$confirm('确认撤销该贴现申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        cancelBillDiscount(row.discountId).then(response => {
          if (response.code === 1) {
            this.$message({
              type: 'success',
              message: '贴现申请已撤销!'
            })
            this.getList()
            this.loadStatistics()
          } else {
            this.$message({
              type: 'error',
              message: response.msg || '撤销失败'
            })
          }
        }).catch(error => {
          console.error('撤销失败:', error)
          this.$message({
            type: 'error',
            message: '撤销失败，请稍后重试'
          })
        })
      })
    },
    handleCalculateInterest(row) {
      // 调用后端API计算利息
      calculateDiscountInterest({
        billAmount: row.billAmount,
        discountRate: row.discountRate,
        discountDays: row.discountDays,
        feeRate: row.feeRate || 0
      }).then(response => {
        if (response.code === 1 && response.data) {
          this.calculateResult = response.data
        } else {
          // 使用本地计算
          this.calculateResult = {
            billAmount: row.billAmount,
            discountRate: row.discountRate,
            discountDays: row.discountDays,
            discountInterest: row.discountInterest,
            handlingFee: row.handlingFee || 0,
            actualAmount: row.discountAmount - (row.handlingFee || 0)
          }
        }
        this.dialogCalculateVisible = true
      }).catch(error => {
        console.error('计算利息失败:', error)
        // 使用本地计算
        this.calculateResult = {
          billAmount: row.billAmount,
          discountRate: row.discountRate,
          discountDays: row.discountDays,
          discountInterest: row.discountInterest,
          handlingFee: row.handlingFee || 0,
          actualAmount: row.discountAmount - (row.handlingFee || 0)
        }
        this.dialogCalculateVisible = true
      })
    },
    handleExport() {
      // 调用后端API导出数据
      const params = { ...this.listQuery }
      if (this.listQuery.applicationDateRange && this.listQuery.applicationDateRange.length === 2) {
        params.applicationDateStart = this.formatDateParam(this.listQuery.applicationDateRange[0])
        params.applicationDateEnd = this.formatDateParam(this.listQuery.applicationDateRange[1])
      }
      delete params.applicationDateRange

      exportBillDiscount(params).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '票据贴现数据_' + new Date().getTime() + '.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
        this.$message({ type: 'success', message: '贴现数据导出成功' })
      }).catch(error => {
        console.error('导出失败:', error)
        this.$message({ type: 'error', message: '导出失败，请稍后重试' })
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

      // 调用后端API审批
      approveBillDiscount(this.currentDiscount.discountId, {
        approvalResult: this.approvalForm.approvalResult,
        approvalComment: this.approvalForm.approvalComment
      }).then(response => {
        if (response.code === 1) {
          this.dialogApprovalVisible = false
          this.dialogDetailVisible = false
          this.$message({
            type: 'success',
            message: '审批完成!'
          })
          this.getList()
          this.loadStatistics()
        } else {
          this.$message({
            type: 'error',
            message: response.msg || '审批失败'
          })
        }
      }).catch(error => {
        console.error('审批失败:', error)
        this.$message({
          type: 'error',
          message: '审批失败，请稍后重试'
        })
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const data = { ...this.temp }
          if (data.applicationDate && typeof data.applicationDate === 'object') {
            data.applicationDate = this.formatDateParam(data.applicationDate)
          }
          // 处理票据金额
          if (typeof data.billAmount === 'string') {
            data.billAmount = parseFloat(data.billAmount.replace(/[¥,]/g, ''))
          }
          // 将前端字段名映射为后端期望的字段名
          data.discountPeriod = data.discountDays || 0
          delete data.discountDays
          delete data.feeRate
          delete data.handlingFee
          delete data.maturityDate
          delete data.discountPurpose
          delete data.riskLevel
          delete data.isRecourse

          createBillDiscount(data).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$message({
                type: 'success',
                message: '贴现申请创建成功'
              })
              this.getList()
              this.loadStatistics()
            } else {
              this.$message({
                type: 'error',
                message: response.msg || '创建失败'
              })
            }
          }).catch(error => {
            console.error('创建失败:', error)
            this.$message({
              type: 'error',
              message: '创建失败，请稍后重试'
            })
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const data = { ...this.temp }
          if (data.applicationDate && typeof data.applicationDate === 'object') {
            data.applicationDate = this.formatDateParam(data.applicationDate)
          }
          // 处理票据金额
          if (typeof data.billAmount === 'string') {
            data.billAmount = parseFloat(data.billAmount.replace(/[¥,]/g, ''))
          }
          // 将前端字段名映射为后端期望的字段名
          data.discountPeriod = data.discountDays || 0
          delete data.discountDays
          delete data.feeRate
          delete data.handlingFee
          delete data.maturityDate
          delete data.discountPurpose
          delete data.riskLevel
          delete data.isRecourse

          updateBillDiscount(data).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$message({
                type: 'success',
                message: '贴现信息更新成功'
              })
              this.getList()
              this.loadStatistics()
            } else {
              this.$message({
                type: 'error',
                message: response.msg || '更新失败'
              })
            }
          }).catch(error => {
            console.error('更新失败:', error)
            this.$message({
              type: 'error',
              message: '更新失败，请稍后重试'
            })
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
