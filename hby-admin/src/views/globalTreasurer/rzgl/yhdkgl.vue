<template>
  <div class="bank-loan-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-bank-card"></i>
            银行贷款管理
          </h2>
          <p class="page-description">银行贷款申请、审批、放款和还款全流程管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增贷款申请
          </el-button>
          <el-button type="success" icon="el-icon-upload" @click="handleImport">
            批量导入
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 贷款概览卡片 -->
    <div class="loan-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">贷款申请总数</div>
                <div class="card-value">{{ totalLoans }}</div>
                <div class="card-change">笔</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-loading"></i>
              </div>
              <div class="card-info">
                <div class="card-title">在贷余额</div>
                <div class="card-value">{{ outstandingAmount }}</div>
                <div class="card-change positive">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon rate-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">平均利率</div>
                <div class="card-value">{{ averageRate }}</div>
                <div class="card-change">%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon approval-icon">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="card-info">
                <div class="card-title">审批通过率</div>
                <div class="card-value">{{ approvalRate }}</div>
                <div class="card-change positive">%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 贷款分析图表 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>贷款类型分布</h3>
            <div class="chart-controls">
              <el-radio-group v-model="loanTypeChartType" size="small" @change="handleLoanTypeChartChange">
                <el-radio-button label="pie">饼图</el-radio-button>
                <el-radio-button label="bar">柱状图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="loanTypeChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>贷款申请趋势</h3>
            <div class="chart-controls">
              <el-radio-group v-model="trendPeriod" size="small" @change="handleTrendPeriodChange">
                <el-radio-button label="6M">6个月</el-radio-button>
                <el-radio-button label="1Y">1年</el-radio-button>
                <el-radio-button label="2Y">2年</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="loanTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="申请编号">
            <el-input
              v-model="listQuery.applicationNo"
              placeholder="请输入申请编号"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="贷款类型">
            <el-select
              v-model="listQuery.loanType"
              placeholder="请选择贷款类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="信用额度" value="CREDIT_LINE" />
              <el-option label="定期贷款" value="TERM_LOAN" />
              <el-option label="循环贷款" value="REVOLVING_LOAN" />
              <el-option label="抵押贷款" value="MORTGAGE" />
              <el-option label="担保贷款" value="GUARANTEE" />
            </el-select>
          </el-form-item>
          <el-form-item label="银行名称">
            <el-select
              v-model="listQuery.bankCode"
              placeholder="请选择银行"
              clearable
              style="width: 150px;"
            >
              <el-option label="中国工商银行" value="ICBC" />
              <el-option label="中国建设银行" value="CCB" />
              <el-option label="中国农业银行" value="ABC" />
              <el-option label="中国银行" value="BOC" />
              <el-option label="招商银行" value="CMB" />
            </el-select>
          </el-form-item>
          <el-form-item label="申请状态">
            <el-select
              v-model="listQuery.applicationStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待提交" value="PENDING" />
              <el-option label="已提交" value="SUBMITTED" />
              <el-option label="已审批" value="APPROVED" />
              <el-option label="已拒绝" value="REJECTED" />
              <el-option label="已取消" value="CANCELLED" />
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

    <!-- 贷款申请表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="loanList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="申请ID" prop="applicationId" width="80" align="center" />
        <el-table-column label="申请编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.applicationNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="贷款类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getLoanTypeTagType(row.loanType)" size="mini">
              {{ getLoanTypeText(row.loanType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="银行名称" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.bankName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="贷款金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="loan-amount">{{ formatCurrency(row.loanAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="贷款期限" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.loanTerm }}{{ getTermUnitText(row.termUnit) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="利率" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.interestRate ? row.interestRate + '%' : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="利率类型" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRateTypeTagType(row.rateType)" size="mini">
              {{ getRateTypeText(row.rateType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="还款方式" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ getRepaymentMethodText(row.repaymentMethod) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="担保方式" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getGuaranteeTypeTagType(row.guaranteeType)" size="mini">
              {{ getGuaranteeTypeText(row.guaranteeType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="申请状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getApplicationStatusTagType(row.applicationStatus)" size="mini">
              {{ getApplicationStatusText(row.applicationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="申请日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.applicationDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.applicationStatus === 'PENDING'" type="primary" size="mini" @click="handleSubmit(row)">
              提交
            </el-button>
            <el-button v-if="row.applicationStatus === 'SUBMITTED'" type="success" size="mini" @click="handleApprove(row)">
              审批
            </el-button>
            <el-button v-if="row.applicationStatus === 'APPROVED'" type="warning" size="mini" @click="handleDrawdown(row)">
              放款
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'edit', row: row}">编辑</el-dropdown-item>
                <el-dropdown-item :command="{action: 'copy', row: row}">复制</el-dropdown-item>
                <el-dropdown-item :command="{action: 'contract', row: row}">合同管理</el-dropdown-item>
                <el-dropdown-item :command="{action: 'repayment', row: row}">还款计划</el-dropdown-item>
                <el-dropdown-item :command="{action: 'monitor', row: row}">贷款监控</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 贷款申请创建/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="900px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-tabs v-model="activeFormTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="申请编号" prop="applicationNo">
                  <el-input v-model="temp.applicationNo" placeholder="系统自动生成" :disabled="dialogStatus==='update'" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="关联计划">
                  <el-select v-model="temp.planId" placeholder="请选择融资计划" style="width: 100%;">
                    <el-option label="流动资金贷款计划" value="1" />
                    <el-option label="设备采购融资计划" value="2" />
                    <el-option label="项目建设融资计划" value="3" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="贷款类型" prop="loanType">
                  <el-select v-model="temp.loanType" placeholder="请选择贷款类型" style="width: 100%;">
                    <el-option label="信用额度" value="CREDIT_LINE" />
                    <el-option label="定期贷款" value="TERM_LOAN" />
                    <el-option label="循环贷款" value="REVOLVING_LOAN" />
                    <el-option label="抵押贷款" value="MORTGAGE" />
                    <el-option label="担保贷款" value="GUARANTEE" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="银行选择" prop="bankCode">
                  <el-select v-model="temp.bankCode" placeholder="请选择银行" style="width: 100%;" @change="handleBankChange">
                    <el-option label="中国工商银行" value="ICBC" />
                    <el-option label="中国建设银行" value="CCB" />
                    <el-option label="中国农业银行" value="ABC" />
                    <el-option label="中国银行" value="BOC" />
                    <el-option label="招商银行" value="CMB" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="贷款金额" prop="loanAmount">
                  <el-input-number
                    v-model="temp.loanAmount"
                    :precision="2"
                    :step="1000000"
                    :min="0"
                    :max="10000000000"
                    style="width: 100%;"
                    placeholder="请输入贷款金额"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="币种">
                  <el-select v-model="temp.currencyCode" placeholder="请选择币种" style="width: 100%;">
                    <el-option label="人民币" value="CNY" />
                    <el-option label="美元" value="USD" />
                    <el-option label="欧元" value="EUR" />
                    <el-option label="日元" value="JPY" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="贷款期限" prop="loanTerm">
                  <el-input-number
                    v-model="temp.loanTerm"
                    :min="1"
                    :max="360"
                    style="width: 70%;"
                    placeholder="请输入期限"
                  />
                  <el-select v-model="temp.termUnit" style="width: 28%; margin-left: 2%;">
                    <el-option label="天" value="DAY" />
                    <el-option label="月" value="MONTH" />
                    <el-option label="年" value="YEAR" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="预期利率">
                  <el-input-number
                    v-model="temp.interestRate"
                    :precision="4"
                    :step="0.1"
                    :min="0"
                    :max="20"
                    style="width: 100%;"
                    placeholder="请输入预期利率"
                  />
                  <span style="margin-left: 8px;">%</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="贷款条件" name="conditions">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="利率类型">
                  <el-select v-model="temp.rateType" placeholder="请选择利率类型" style="width: 100%;">
                    <el-option label="固定利率" value="FIXED" />
                    <el-option label="浮动利率" value="FLOATING" />
                    <el-option label="混合利率" value="MIXED" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="还款方式">
                  <el-select v-model="temp.repaymentMethod" placeholder="请选择还款方式" style="width: 100%;">
                    <el-option label="等额本息" value="EQUAL_INSTALLMENT" />
                    <el-option label="等额本金" value="EQUAL_PRINCIPAL" />
                    <el-option label="先息后本" value="INTEREST_ONLY" />
                    <el-option label="到期还本" value="BALLOON" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="担保方式">
                  <el-select v-model="temp.guaranteeType" placeholder="请选择担保方式" style="width: 100%;">
                    <el-option label="信用贷款" value="CREDIT" />
                    <el-option label="抵押贷款" value="MORTGAGE" />
                    <el-option label="质押贷款" value="PLEDGE" />
                    <el-option label="保证贷款" value="GUARANTEE" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="担保价值">
                  <el-input-number
                    v-model="temp.guaranteeValue"
                    :precision="2"
                    :step="100000"
                    :min="0"
                    style="width: 100%;"
                    placeholder="请输入担保价值"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="贷款用途" prop="loanPurpose">
              <el-input v-model="temp.loanPurpose" type="textarea" :rows="4" placeholder="请详细描述贷款用途" />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="其他信息" name="other">
            <el-form-item label="备注信息">
              <el-input v-model="temp.remark" type="textarea" :rows="4" placeholder="请输入备注信息" />
            </el-form-item>
            <el-form-item label="附件上传">
              <el-upload
                class="upload-demo"
                action="#"
                :auto-upload="false"
                :on-change="handleAttachmentChange"
                multiple
              >
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">支持上传多个文件，如申请材料、担保文件等</div>
              </el-upload>
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

    <!-- 贷款详情对话框 -->
    <el-dialog title="贷款申请详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentLoan" class="loan-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="申请编号">{{ currentLoan.applicationNo }}</el-descriptions-item>
          <el-descriptions-item label="贷款类型">{{ getLoanTypeText(currentLoan.loanType) }}</el-descriptions-item>
          <el-descriptions-item label="银行名称">{{ currentLoan.bankName }}</el-descriptions-item>
          <el-descriptions-item label="贷款金额">{{ formatCurrency(currentLoan.loanAmount) }}</el-descriptions-item>
          <el-descriptions-item label="贷款期限">{{ currentLoan.loanTerm }}{{ getTermUnitText(currentLoan.termUnit) }}</el-descriptions-item>
          <el-descriptions-item label="利率">{{ currentLoan.interestRate ? currentLoan.interestRate + '%' : '-' }}</el-descriptions-item>
          <el-descriptions-item label="利率类型">{{ getRateTypeText(currentLoan.rateType) }}</el-descriptions-item>
          <el-descriptions-item label="还款方式">{{ getRepaymentMethodText(currentLoan.repaymentMethod) }}</el-descriptions-item>
          <el-descriptions-item label="担保方式">{{ getGuaranteeTypeText(currentLoan.guaranteeType) }}</el-descriptions-item>
          <el-descriptions-item label="申请状态">
            <el-tag :type="getApplicationStatusTagType(currentLoan.applicationStatus)">
              {{ getApplicationStatusText(currentLoan.applicationStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="申请日期">{{ currentLoan.applicationDate }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentLoan.createTime }}</el-descriptions-item>
        </el-descriptions>
        
        <div class="loan-purpose">
          <h4>贷款用途</h4>
          <p>{{ currentLoan.loanPurpose || '无' }}</p>
        </div>
        
        <div class="loan-progress">
          <h4>申请进度</h4>
          <el-steps :active="getApplicationStep(currentLoan)" finish-status="success">
            <el-step title="申请创建" description="创建贷款申请"></el-step>
            <el-step title="申请提交" description="提交银行审批"></el-step>
            <el-step title="银行审批" description="银行审批通过"></el-step>
            <el-step title="合同签署" description="签署贷款合同"></el-step>
            <el-step title="贷款放款" description="银行放款到账"></el-step>
          </el-steps>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentLoan && currentLoan.applicationStatus === 'PENDING'" type="primary" @click="handleSubmit(currentLoan)">
          提交申请
        </el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog title="贷款申请审批" :visible.sync="dialogApprovalVisible" width="600px">
      <el-form ref="approvalForm" :model="approvalForm" label-width="100px">
        <el-form-item label="审批结果" prop="approvalResult">
          <el-radio-group v-model="approvalForm.approvalResult">
            <el-radio label="APPROVED">通过</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见" prop="approvalOpinion">
          <el-input v-model="approvalForm.approvalOpinion" type="textarea" :rows="4" placeholder="请输入审批意见" />
        </el-form-item>
        <el-form-item v-if="approvalForm.approvalResult === 'APPROVED'" label="批准金额">
          <el-input-number
            v-model="approvalForm.approvedAmount"
            :precision="2"
            :step="100000"
            :min="0"
            style="width: 100%;"
            placeholder="请输入批准金额"
          />
        </el-form-item>
        <el-form-item v-if="approvalForm.approvalResult === 'APPROVED'" label="批准利率">
          <el-input-number
            v-model="approvalForm.approvedRate"
            :precision="4"
            :step="0.1"
            :min="0"
            :max="20"
            style="width: 100%;"
            placeholder="请输入批准利率"
          />
          <span style="margin-left: 8px;">%</span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogApprovalVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApproval">提交审批</el-button>
      </div>
    </el-dialog>

    <!-- 放款对话框 -->
    <el-dialog title="贷款放款" :visible.sync="dialogDrawdownVisible" width="600px">
      <el-form ref="drawdownForm" :model="drawdownForm" label-width="100px">
        <el-form-item label="放款金额" prop="drawdownAmount">
          <el-input-number
            v-model="drawdownForm.drawdownAmount"
            :precision="2"
            :step="100000"
            :min="0"
            style="width: 100%;"
            placeholder="请输入放款金额"
          />
        </el-form-item>
        <el-form-item label="放款日期" prop="drawdownDate">
          <el-date-picker
            v-model="drawdownForm.drawdownDate"
            type="date"
            placeholder="选择放款日期"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="收款账户" prop="receivingAccount">
          <el-input v-model="drawdownForm.receivingAccount" placeholder="请输入收款账户" />
        </el-form-item>
        <el-form-item label="放款说明">
          <el-input v-model="drawdownForm.drawdownNotes" type="textarea" :rows="3" placeholder="请输入放款说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDrawdownVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDrawdown">确认放款</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBankLoanPage, createBankLoan, updateBankLoan, submitLoanApplication, drawdownLoan } from '@/api/globalTreasurer/rzgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'BankLoanManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        applicationNo: undefined,
        loanType: undefined,
        bankCode: undefined,
        applicationStatus: undefined,
        applicationDateRange: undefined
      },
      totalLoans: 45,
      outstandingAmount: 125600.8,
      averageRate: 4.65,
      approvalRate: 87.2,
      loanTypeChartType: 'pie',
      trendPeriod: '1Y',
      loanList: [],
      multipleSelection: [],
      currentLoan: null,
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogApprovalVisible: false,
      dialogDrawdownVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      activeFormTab: 'basic',
      temp: {
        applicationId: undefined,
        applicationNo: '',
        planId: undefined,
        loanType: '',
        bankCode: '',
        bankName: '',
        loanAmount: null,
        currencyCode: 'CNY',
        loanTerm: null,
        termUnit: 'MONTH',
        interestRate: null,
        rateType: 'FIXED',
        repaymentMethod: 'EQUAL_INSTALLMENT',
        guaranteeType: 'CREDIT',
        guaranteeValue: null,
        loanPurpose: '',
        remark: ''
      },
      approvalForm: {
        approvalResult: '',
        approvalOpinion: '',
        approvedAmount: null,
        approvedRate: null
      },
      drawdownForm: {
        drawdownAmount: null,
        drawdownDate: null,
        receivingAccount: '',
        drawdownNotes: ''
      },
      rules: {
        applicationNo: [{ required: true, message: '申请编号不能为空', trigger: 'blur' }],
        loanType: [{ required: true, message: '请选择贷款类型', trigger: 'change' }],
        bankCode: [{ required: true, message: '请选择银行', trigger: 'change' }],
        loanAmount: [{ required: true, message: '请输入贷款金额', trigger: 'blur' }],
        loanTerm: [{ required: true, message: '请输入贷款期限', trigger: 'blur' }],
        loanPurpose: [{ required: true, message: '请输入贷款用途', trigger: 'blur' }]
      },
      loanTypeChart: null,
      trendChart: null
    }
  },
  mounted() {
    this.getList()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.loanTypeChart) {
      this.loanTypeChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.loanList = [
          {
            applicationId: 1,
            applicationNo: 'BL20240925001',
            planId: 1,
            loanType: 'CREDIT_LINE',
            bankCode: 'ICBC',
            bankName: '中国工商银行',
            loanAmount: 50000000.00,
            currencyCode: 'CNY',
            loanTerm: 12,
            termUnit: 'MONTH',
            interestRate: 4.35,
            rateType: 'FIXED',
            repaymentMethod: 'EQUAL_INSTALLMENT',
            guaranteeType: 'CREDIT',
            guaranteeValue: null,
            applicationStatus: 'APPROVED',
            applicationDate: '2024-09-25',
            createTime: '2024-09-25 10:00:00',
            loanPurpose: '用于补充企业流动资金，支持日常经营活动'
          },
          {
            applicationId: 2,
            applicationNo: 'BL20240920002',
            planId: 2,
            loanType: 'TERM_LOAN',
            bankCode: 'CCB',
            bankName: '中国建设银行',
            loanAmount: 30000000.00,
            currencyCode: 'CNY',
            loanTerm: 24,
            termUnit: 'MONTH',
            interestRate: 4.8,
            rateType: 'FLOATING',
            repaymentMethod: 'EQUAL_PRINCIPAL',
            guaranteeType: 'MORTGAGE',
            guaranteeValue: 35000000.00,
            applicationStatus: 'SUBMITTED',
            applicationDate: '2024-09-20',
            createTime: '2024-09-20 14:30:00',
            loanPurpose: '用于设备采购和技术升级'
          },
          {
            applicationId: 3,
            applicationNo: 'BL20240915003',
            planId: 3,
            loanType: 'MORTGAGE',
            bankCode: 'ABC',
            bankName: '中国农业银行',
            loanAmount: 80000000.00,
            currencyCode: 'CNY',
            loanTerm: 36,
            termUnit: 'MONTH',
            interestRate: 5.2,
            rateType: 'FIXED',
            repaymentMethod: 'INTEREST_ONLY',
            guaranteeType: 'MORTGAGE',
            guaranteeValue: 100000000.00,
            applicationStatus: 'PENDING',
            applicationDate: '2024-09-15',
            createTime: '2024-09-15 16:45:00',
            loanPurpose: '用于项目建设和基础设施投资'
          }
        ]
        this.total = this.loanList.length
        this.listLoading = false
      }, 1000)
    },
    initCharts() {
      const echarts = require('echarts')
      
      // 初始化贷款类型图表
      this.loanTypeChart = echarts.init(document.getElementById('loanTypeChart'))
      this.updateLoanTypeChart()
      
      // 初始化趋势图表
      this.trendChart = echarts.init(document.getElementById('loanTrendChart'))
      this.updateTrendChart()
    },
    updateLoanTypeChart() {
      const data = [
        { name: '信用额度', value: 18, itemStyle: { color: '#409EFF' } },
        { name: '定期贷款', value: 12, itemStyle: { color: '#67C23A' } },
        { name: '循环贷款', value: 8, itemStyle: { color: '#E6A23C' } },
        { name: '抵押贷款', value: 5, itemStyle: { color: '#F56C6C' } },
        { name: '担保贷款', value: 2, itemStyle: { color: '#909399' } }
      ]
      
      let option = {}
      
      if (this.loanTypeChartType === 'pie') {
        option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c}笔 ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 10,
            data: data.map(item => item.name)
          },
          series: [
            {
              name: '贷款类型',
              type: 'pie',
              radius: ['50%', '70%'],
              center: ['60%', '50%'],
              data: data
            }
          ]
        }
      } else {
        option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: data.map(item => item.name),
            axisLabel: {
              rotate: 45
            }
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '申请数量',
              type: 'bar',
              data: data.map(item => ({
                value: item.value,
                itemStyle: item.itemStyle
              }))
            }
          ]
        }
      }
      
      this.loanTypeChart.setOption(option)
    },
    updateTrendChart() {
      const months = this.generateMonthLabels(12)
      const applicationData = this.generateMockData(12, 3, 12)
      const amountData = this.generateMockData(12, 8000, 25000)
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['申请数量', '申请金额(万元)']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: months
        },
        yAxis: [
          {
            type: 'value',
            name: '数量(笔)',
            position: 'left'
          },
          {
            type: 'value',
            name: '金额(万元)',
            position: 'right'
          }
        ],
        series: [
          {
            name: '申请数量',
            type: 'line',
            data: applicationData,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '申请金额(万元)',
            type: 'line',
            yAxisIndex: 1,
            data: amountData,
            itemStyle: { color: '#E6A23C' }
          }
        ]
      }
      
      this.trendChart.setOption(option)
    },
    generateMonthLabels(count) {
      const labels = []
      for (let i = count - 1; i >= 0; i--) {
        const date = new Date()
        date.setMonth(date.getMonth() - i)
        labels.push((date.getMonth() + 1) + '月')
      }
      return labels
    },
    generateMockData(count, min, max) {
      const data = []
      for (let i = 0; i < count; i++) {
        data.push(Math.floor(Math.random() * (max - min) + min))
      }
      return data
    },
    handleLoanTypeChartChange() {
      this.updateLoanTypeChart()
    },
    handleTrendPeriodChange() {
      this.updateTrendChart()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        applicationNo: undefined,
        loanType: undefined,
        bankCode: undefined,
        applicationStatus: undefined,
        applicationDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '新增贷款申请'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentLoan = row
      this.dialogDetailVisible = true
    },
    handleSubmit(row) {
      this.$confirm('确认提交该贷款申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.applicationStatus = 'SUBMITTED'
        this.$message({
          type: 'success',
          message: '贷款申请提交成功!'
        })
      })
    },
    handleApprove(row) {
      this.currentLoan = row
      this.approvalForm = {
        approvalResult: '',
        approvalOpinion: '',
        approvedAmount: row.loanAmount,
        approvedRate: row.interestRate
      }
      this.dialogApprovalVisible = true
    },
    handleDrawdown(row) {
      this.currentLoan = row
      this.drawdownForm = {
        drawdownAmount: row.loanAmount,
        drawdownDate: new Date(),
        receivingAccount: '',
        drawdownNotes: ''
      }
      this.dialogDrawdownVisible = true
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'edit':
          this.handleEdit(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'contract':
          this.handleContract(row)
          break
        case 'repayment':
          this.handleRepayment(row)
          break
        case 'monitor':
          this.handleMonitor(row)
          break
      }
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑贷款申请'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleCopy(row) {
      this.temp = Object.assign({}, row)
      this.temp.applicationId = undefined
      this.temp.applicationNo = ''
      this.temp.applicationStatus = 'PENDING'
      this.dialogStatus = 'create'
      this.dialogTitle = '复制贷款申请'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleContract(row) {
      this.$message({
        type: 'info',
        message: '合同管理功能'
      })
    },
    handleRepayment(row) {
      this.$message({
        type: 'info',
        message: '还款计划功能'
      })
    },
    handleMonitor(row) {
      this.$message({
        type: 'info',
        message: '贷款监控功能'
      })
    },
    handleImport() {
      this.$message({
        type: 'info',
        message: '批量导入功能'
      })
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '数据导出成功'
      })
    },
    handleBankChange(bankCode) {
      const bankMap = {
        'ICBC': '中国工商银行',
        'CCB': '中国建设银行',
        'ABC': '中国农业银行',
        'BOC': '中国银行',
        'CMB': '招商银行'
      }
      this.temp.bankName = bankMap[bankCode] || ''
    },
    handleAttachmentChange(file) {
      this.$message({
        type: 'info',
        message: `已选择附件: ${file.name}`
      })
    },
    submitApproval() {
      if (!this.approvalForm.approvalResult) {
        this.$message({
          type: 'warning',
          message: '请选择审批结果'
        })
        return
      }
      
      if (this.approvalForm.approvalResult === 'APPROVED') {
        this.currentLoan.applicationStatus = 'APPROVED'
        this.currentLoan.loanAmount = this.approvalForm.approvedAmount
        this.currentLoan.interestRate = this.approvalForm.approvedRate
      } else {
        this.currentLoan.applicationStatus = 'REJECTED'
      }
      
      this.dialogApprovalVisible = false
      this.$message({
        type: 'success',
        message: '审批完成!'
      })
    },
    submitDrawdown() {
      if (!this.drawdownForm.drawdownAmount || !this.drawdownForm.drawdownDate) {
        this.$message({
          type: 'warning',
          message: '请填写完整的放款信息'
        })
        return
      }
      
      this.currentLoan.applicationStatus = 'DISBURSED'
      this.dialogDrawdownVisible = false
      this.$message({
        type: 'success',
        message: '放款成功!'
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.applicationId = parseInt(Math.random() * 100) + 1024
          this.temp.applicationNo = 'BL' + new Date().getTime()
          this.temp.applicationStatus = 'PENDING'
          this.temp.applicationDate = new Date().toISOString().slice(0, 10)
          this.temp.createTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
          this.loanList.unshift(this.temp)
          this.total = this.loanList.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '贷款申请创建成功'
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const index = this.loanList.findIndex(v => v.applicationId === this.temp.applicationId)
          this.loanList.splice(index, 1, this.temp)
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '贷款申请更新成功'
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        applicationId: undefined,
        applicationNo: '',
        planId: undefined,
        loanType: '',
        bankCode: '',
        bankName: '',
        loanAmount: null,
        currencyCode: 'CNY',
        loanTerm: null,
        termUnit: 'MONTH',
        interestRate: null,
        rateType: 'FIXED',
        repaymentMethod: 'EQUAL_INSTALLMENT',
        guaranteeType: 'CREDIT',
        guaranteeValue: null,
        loanPurpose: '',
        remark: ''
      }
    },
    getLoanTypeTagType(type) {
      const typeMap = {
        'CREDIT_LINE': 'primary',
        'TERM_LOAN': 'success',
        'REVOLVING_LOAN': 'warning',
        'MORTGAGE': 'info',
        'GUARANTEE': 'default'
      }
      return typeMap[type] || 'default'
    },
    getLoanTypeText(type) {
      const textMap = {
        'CREDIT_LINE': '信用额度',
        'TERM_LOAN': '定期贷款',
        'REVOLVING_LOAN': '循环贷款',
        'MORTGAGE': '抵押贷款',
        'GUARANTEE': '担保贷款'
      }
      return textMap[type] || type
    },
    getTermUnitText(unit) {
      const textMap = {
        'DAY': '天',
        'MONTH': '月',
        'YEAR': '年'
      }
      return textMap[unit] || unit
    },
    getRateTypeTagType(type) {
      const typeMap = {
        'FIXED': 'success',
        'FLOATING': 'warning',
        'MIXED': 'info'
      }
      return typeMap[type] || 'default'
    },
    getRateTypeText(type) {
      const textMap = {
        'FIXED': '固定利率',
        'FLOATING': '浮动利率',
        'MIXED': '混合利率'
      }
      return textMap[type] || type
    },
    getRepaymentMethodText(method) {
      const textMap = {
        'EQUAL_INSTALLMENT': '等额本息',
        'EQUAL_PRINCIPAL': '等额本金',
        'INTEREST_ONLY': '先息后本',
        'BALLOON': '到期还本'
      }
      return textMap[method] || method
    },
    getGuaranteeTypeTagType(type) {
      const typeMap = {
        'CREDIT': 'primary',
        'MORTGAGE': 'success',
        'PLEDGE': 'warning',
        'GUARANTEE': 'info'
      }
      return typeMap[type] || 'default'
    },
    getGuaranteeTypeText(type) {
      const textMap = {
        'CREDIT': '信用贷款',
        'MORTGAGE': '抵押贷款',
        'PLEDGE': '质押贷款',
        'GUARANTEE': '保证贷款'
      }
      return textMap[type] || type
    },
    getApplicationStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info',
        'SUBMITTED': 'warning',
        'APPROVED': 'primary',
        'REJECTED': 'danger',
        'CANCELLED': 'default',
        'DISBURSED': 'success'
      }
      return typeMap[status] || 'info'
    },
    getApplicationStatusText(status) {
      const textMap = {
        'PENDING': '待提交',
        'SUBMITTED': '已提交',
        'APPROVED': '已审批',
        'REJECTED': '已拒绝',
        'CANCELLED': '已取消',
        'DISBURSED': '已放款'
      }
      return textMap[status] || status
    },
    getApplicationStep(loan) {
      const stepMap = {
        'PENDING': 0,
        'SUBMITTED': 1,
        'APPROVED': 2,
        'CONTRACTED': 3,
        'DISBURSED': 4
      }
      return stepMap[loan.applicationStatus] || 0
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
.bank-loan-manage {
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

  .loan-overview {
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
          &.active-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.rate-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.approval-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
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
          }
        }
      }
    }
  }

  .chart-card, .search-card, .table-card {
    margin-bottom: 20px;
  }

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h3 {
      margin: 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .chart-container {
    height: 300px;
    width: 100%;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .loan-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .loan-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }
    
    .loan-purpose, .loan-progress {
      margin-top: 20px;
      
      h4 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 14px;
        font-weight: 600;
        border-left: 3px solid #409EFF;
        padding-left: 8px;
      }
      
      p {
        margin: 0;
        color: #606266;
        line-height: 1.6;
        padding: 8px 12px;
        background: #f8f9fa;
        border-radius: 4px;
      }
    }
  }

  .upload-demo {
    width: 100%;
  }
}
</style>
