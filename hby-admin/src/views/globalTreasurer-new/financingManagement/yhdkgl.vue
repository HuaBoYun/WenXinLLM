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
              <el-option label="中国工商银行" value="1" />
              <el-option label="中国建设银行" value="2" />
              <el-option label="中国农业银行" value="3" />
              <el-option label="招商银行" value="4" />
              <el-option label="中信银行" value="5" />
              <el-option label="浦发银行" value="6" />
              <el-option label="兴业银行" value="7" />
              <el-option label="汇丰银行" value="8" />
              <el-option label="民生银行" value="9" />
              <el-option label="光大银行" value="10" />
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
              <el-option label="执行中" value="ACTIVE" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已拒绝" value="REJECTED" />
              <el-option label="已取消" value="CANCELLED" />
              <el-option label="已逾期" value="OVERDUE" />
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
        <el-table-column label="贷款ID" prop="loanId" width="100" align="center" />
        <el-table-column label="贷款编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.applicationNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="贷款名称" prop="loanPurpose" width="150px" align="center" show-overflow-tooltip />
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
        <el-table-column label="开始日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.applicationDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.maturityDate) }}</span>
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
                <el-form-item label="申请编号">
                  <el-input v-model="temp.applicationNo" placeholder="系统自动生成" disabled />
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
            <el-form-item label="贷款名称/用途" prop="loanPurpose">
              <el-input v-model="temp.loanPurpose" type="textarea" :rows="3" placeholder="请输入贷款名称或用途描述" />
            </el-form-item>
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

    <!-- ========== 合同管理对话框 ========== -->
    <el-dialog title="合同管理" :visible.sync="dialogContractVisible" width="1000px" top="5vh">
      <el-row :gutter="20">
        <!-- 左侧：合同列表 -->
        <el-col :span="14">
          <div class="contract-list-header">
            <h4>合同列表</h4>
            <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddContract">新增合同</el-button>
          </div>
          <el-table :data="contractList" v-loading="contractLoading" border size="small" max-height="400">
            <el-table-column prop="contractNo" label="合同编号" width="120" />
            <el-table-column prop="contractName" label="合同名称" width="150" show-overflow-tooltip />
            <el-table-column prop="contractAmount" label="合同金额" width="120" align="right">
              <template slot-scope="{row}">{{ formatCurrency(row.contractAmount) }}</template>
            </el-table-column>
            <el-table-column prop="signingDate" label="签署日期" width="100">
              <template slot-scope="{row}">{{ formatDate(row.signingDate) }}</template>
            </el-table-column>
            <el-table-column prop="contractStatus" label="状态" width="80" align="center">
              <template slot-scope="{row}">
                <el-tag :type="getContractStatusType(row.contractStatus)" size="mini">
                  {{ getContractStatusText(row.contractStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" align="center">
              <template slot-scope="{row}">
                <el-button type="text" size="mini" @click="handleEditContract(row)">编辑</el-button>
                <el-button type="text" size="mini" @click="handleUpdateContractStatus(row, 'ACTIVE')" v-if="row.contractStatus === 'DRAFT'">生效</el-button>
                <el-button type="text" size="mini" style="color: #F56C6C;" @click="handleDeleteContract(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <!-- 右侧：合同表单 -->
        <el-col :span="10">
          <div class="contract-form-header">
            <h4>{{ contractForm.contractId ? '编辑合同' : '新增合同' }}</h4>
          </div>
          <el-form ref="contractForm" :model="contractForm" :rules="contractRules" label-width="80px" size="small">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="contractForm.contractName" placeholder="请输入合同名称" />
            </el-form-item>
            <el-form-item label="合同金额" prop="contractAmount">
              <el-input-number v-model="contractForm.contractAmount" :precision="2" :min="0" style="width: 100%;" />
            </el-form-item>
            <el-form-item label="签署日期" prop="signingDate">
              <el-date-picker v-model="contractForm.signingDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;" placeholder="请选择签署日期" />
            </el-form-item>
            <el-form-item label="生效日期">
              <el-date-picker v-model="contractForm.effectiveDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;" placeholder="请选择生效日期" />
            </el-form-item>
            <el-form-item label="到期日期">
              <el-date-picker v-model="contractForm.expiryDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;" placeholder="请选择到期日期" />
            </el-form-item>
            <el-form-item label="合同完整性">
              <el-select v-model="contractForm.contractCompleteness" style="width: 100%;">
                <el-option label="完整" value="COMPLETE" />
                <el-option label="不完整" value="INCOMPLETE" />
              </el-select>
            </el-form-item>
            <el-form-item label="合同文件">
              <el-input v-model="contractForm.contractFilePath" placeholder="请输入合同文件路径" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="contractForm.remark" type="textarea" :rows="2" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveContract">保存</el-button>
              <el-button @click="resetContractForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-dialog>

    <!-- ========== 还款计划对话框 ========== -->
    <el-dialog title="还款计划管理" :visible.sync="dialogRepaymentVisible" width="1100px" top="5vh">
      <!-- 生成还款计划表单 -->
      <el-card v-if="showGenerateForm" class="generate-form-card" shadow="never">
        <div slot="header">
          <span>自动生成还款计划</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="showGenerateForm = false">收起</el-button>
        </div>
        <el-form :model="generateForm" label-width="100px" :inline="true">
          <el-form-item label="还款方式">
            <el-select v-model="generateForm.repaymentMethod" style="width: 150px;">
              <el-option label="等额本息" value="EQUAL_INSTALLMENT" />
              <el-option label="等额本金" value="EQUAL_PRINCIPAL" />
            </el-select>
          </el-form-item>
          <el-form-item label="还款期数">
            <el-input-number v-model="generateForm.periods" :min="1" :max="360" style="width: 120px;" />
          </el-form-item>
          <el-form-item label="贷款金额">
            <el-input-number v-model="generateForm.loanAmount" :precision="2" :min="0" style="width: 150px;" />
          </el-form-item>
          <el-form-item label="年利率(%)">
            <el-input-number v-model="generateForm.interestRate" :precision="4" :min="0" :max="100" style="width: 120px;" />
          </el-form-item>
          <el-form-item label="起始日期">
            <el-date-picker v-model="generateForm.startDate" type="date" value-format="yyyy-MM-dd" style="width: 150px;" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleGenerateRepaymentPlans">生成计划</el-button>
          </el-form-item>
        </el-form>
      </el-card>
      <!-- 操作按钮 -->
      <div class="repayment-toolbar" v-if="!showGenerateForm">
        <el-button type="primary" size="small" icon="el-icon-magic-stick" @click="handleShowGenerateForm">自动生成还款计划</el-button>
      </div>
      <!-- 还款计划列表 -->
      <el-table :data="repaymentList" v-loading="repaymentLoading" border size="small" max-height="450" style="margin-top: 10px;">
        <el-table-column prop="periodNo" label="期数" width="60" align="center" />
        <el-table-column prop="dueDate" label="应还日期" width="100" align="center" />
        <el-table-column prop="principalAmount" label="应还本金" width="110" align="right">
          <template slot-scope="{row}">{{ formatCurrency(row.principalAmount) }}</template>
        </el-table-column>
        <el-table-column prop="interestAmount" label="应还利息" width="100" align="right">
          <template slot-scope="{row}">{{ formatCurrency(row.interestAmount) }}</template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="应还总额" width="110" align="right">
          <template slot-scope="{row}">
            <span style="font-weight: bold; color: #409EFF;">{{ formatCurrency(row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paidAmount" label="已还金额" width="100" align="right">
          <template slot-scope="{row}">{{ formatCurrency(row.paidAmount || 0) }}</template>
        </el-table-column>
        <el-table-column label="剩余应还" width="100" align="right">
          <template slot-scope="{row}">
            <span :style="{color: (row.totalAmount - (row.paidAmount || 0)) > 0 ? '#F56C6C' : '#67C23A'}">
              {{ formatCurrency(row.totalAmount - (row.paidAmount || 0)) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="paymentStatus" label="状态" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getPaymentStatusType(row.paymentStatus)" size="mini">
              {{ getPaymentStatusText(row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" @click="handleExecuteRepayment(row)" v-if="row.paymentStatus !== 'PAID'">还款</el-button>
            <el-button type="text" size="mini" style="color: #F56C6C;" @click="handleDeleteRepaymentPlan(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 执行还款对话框 -->
    <el-dialog title="执行还款" :visible.sync="dialogExecuteRepaymentVisible" width="400px" append-to-body>
      <el-form :model="executeRepaymentForm" label-width="80px">
        <el-form-item label="还款金额">
          <el-input-number v-model="executeRepaymentForm.amount" :precision="2" :min="0" style="width: 100%;" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogExecuteRepaymentVisible = false">取消</el-button>
        <el-button type="primary" @click="submitExecuteRepayment">确认还款</el-button>
      </div>
    </el-dialog>

    <!-- ========== 贷款监控对话框 ========== -->
    <el-dialog title="贷款监控预警" :visible.sync="dialogMonitoringVisible" width="1000px" top="5vh">
      <el-row :gutter="20">
        <!-- 左侧：预警列表 -->
        <el-col :span="14">
          <div class="monitoring-list-header">
            <h4>预警列表</h4>
            <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddMonitoring">新增预警</el-button>
          </div>
          <el-table :data="monitoringList" v-loading="monitoringLoading" border size="small" max-height="400">
            <el-table-column prop="alertTitle" label="预警标题" width="150" show-overflow-tooltip />
            <el-table-column prop="alertType" label="预警类型" width="90" align="center">
              <template slot-scope="{row}">{{ getAlertTypeText(row.alertType) }}</template>
            </el-table-column>
            <el-table-column prop="alertLevel" label="级别" width="60" align="center">
              <template slot-scope="{row}">
                <el-tag :type="getAlertLevelType(row.alertLevel)" size="mini">
                  {{ getAlertLevelText(row.alertLevel) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="alertStatus" label="状态" width="80" align="center">
              <template slot-scope="{row}">
                <el-tag :type="getAlertStatusType(row.alertStatus)" size="mini">
                  {{ getAlertStatusText(row.alertStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="100" />
            <el-table-column label="操作" width="150" align="center">
              <template slot-scope="{row}">
                <el-button type="text" size="mini" @click="handleEditMonitoring(row)">编辑</el-button>
                <el-button type="text" size="mini" @click="handleProcessMonitoring(row)" v-if="row.alertStatus === 'PENDING'">处理</el-button>
                <el-button type="text" size="mini" style="color: #F56C6C;" @click="handleDeleteMonitoring(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <!-- 右侧：预警表单 -->
        <el-col :span="10">
          <div class="monitoring-form-header">
            <h4>{{ monitoringForm.monitoringId ? '编辑预警' : '新增预警' }}</h4>
          </div>
          <el-form ref="monitoringForm" :model="monitoringForm" :rules="monitoringRules" label-width="80px" size="small">
            <el-form-item label="预警标题" prop="alertTitle">
              <el-input v-model="monitoringForm.alertTitle" placeholder="请输入预警标题" />
            </el-form-item>
            <el-form-item label="预警类型" prop="alertType">
              <el-select v-model="monitoringForm.alertType" style="width: 100%;">
                <el-option label="到期预警" value="MATURITY" />
                <el-option label="逾期预警" value="OVERDUE" />
                <el-option label="利率变动" value="RATE_CHANGE" />
                <el-option label="金额变动" value="AMOUNT_CHANGE" />
              </el-select>
            </el-form-item>
            <el-form-item label="预警级别" prop="alertLevel">
              <el-select v-model="monitoringForm.alertLevel" style="width: 100%;">
                <el-option label="低" value="LOW" />
                <el-option label="中" value="MEDIUM" />
                <el-option label="高" value="HIGH" />
                <el-option label="紧急" value="CRITICAL" />
              </el-select>
            </el-form-item>
            <el-form-item label="预警内容">
              <el-input v-model="monitoringForm.alertContent" type="textarea" :rows="3" placeholder="请输入预警内容" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="monitoringForm.remark" type="textarea" :rows="2" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveMonitoring">保存</el-button>
              <el-button @click="resetMonitoringForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-dialog>

    <!-- 处理预警对话框 -->
    <el-dialog title="处理预警" :visible.sync="dialogHandleMonitoringVisible" width="500px" append-to-body>
      <el-form :model="handleMonitoringForm" label-width="80px">
        <el-form-item label="处理状态">
          <el-select v-model="handleMonitoringForm.status" style="width: 100%;">
            <el-option label="已处理" value="HANDLED" />
            <el-option label="已关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理意见">
          <el-input v-model="handleMonitoringForm.handleOpinion" type="textarea" :rows="4" placeholder="请输入处理意见" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogHandleMonitoringVisible = false">取消</el-button>
        <el-button type="primary" @click="submitHandleMonitoring">确认处理</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBankLoanPage, createBankLoan, updateBankLoan, submitLoanApplication, drawdownLoan,
  getBankLoanStatistics, getBankLoanTypeDistribution, getBankLoanTrend,
  // 贷款合同管理
  getLoanContractsByLoanId, saveLoanContract, deleteLoanContract, updateLoanContractStatus,
  // 还款计划管理
  getRepaymentPlansByLoanId, saveRepaymentPlan, deleteRepaymentPlan, generateRepaymentPlans, executeBankLoanRepayment,
  getPendingRepaymentPlans, getOverdueRepaymentPlans,
  // 贷款监控管理
  getLoanMonitoringByLoanId, addLoanMonitoring, updateLoanMonitoring, deleteLoanMonitoring, handleLoanMonitoring,
  getPendingLoanMonitoring, countPendingLoanMonitoring
} from '@/api/globalTreasurer/rzgl'
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
      totalLoans: 0,
      outstandingAmount: 0,
      averageRate: 0,
      approvalRate: 0,
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
        loanType: [{ required: true, message: '请选择贷款类型', trigger: 'change' }],
        bankCode: [{ required: true, message: '请选择银行', trigger: 'change' }],
        loanAmount: [{ required: true, message: '请输入贷款金额', trigger: 'blur' }],
        loanTerm: [{ required: true, message: '请输入贷款期限', trigger: 'blur' }],
        loanPurpose: [{ required: true, message: '请输入贷款用途', trigger: 'blur' }]
      },
      loanTypeChart: null,
      trendChart: null,
      // ========== 合同管理相关 ==========
      dialogContractVisible: false,
      contractList: [],
      contractLoading: false,
      contractForm: {
        contractId: undefined,
        loanId: '',
        contractNo: '',
        contractName: '',
        contractStatus: 'DRAFT',
        contractCompleteness: 'COMPLETE',
        signingDate: '',
        effectiveDate: '',
        expiryDate: '',
        contractAmount: null,
        contractFilePath: '',
        remark: ''
      },
      contractRules: {
        contractName: [{ required: true, message: '请输入合同名称', trigger: 'blur' }],
        contractAmount: [{ required: true, message: '请输入合同金额', trigger: 'blur' }],
        signingDate: [{ required: true, message: '请选择签署日期', trigger: 'change' }]
      },
      // ========== 还款计划相关 ==========
      dialogRepaymentVisible: false,
      repaymentList: [],
      repaymentLoading: false,
      showGenerateForm: false,
      generateForm: {
        loanId: '',
        repaymentMethod: 'EQUAL_INSTALLMENT',
        periods: 12,
        loanAmount: null,
        interestRate: null,
        startDate: ''
      },
      repaymentForm: {
        planId: undefined,
        loanId: '',
        periodNo: null,
        dueDate: '',
        principalAmount: null,
        interestAmount: null,
        totalAmount: null,
        paidPrincipal: 0,
        paidInterest: 0,
        paidAmount: 0,
        paymentStatus: 'PENDING',
        remark: ''
      },
      executeRepaymentForm: {
        planId: '',
        amount: null
      },
      dialogExecuteRepaymentVisible: false,
      // ========== 贷款监控相关 ==========
      dialogMonitoringVisible: false,
      monitoringList: [],
      monitoringLoading: false,
      monitoringForm: {
        monitoringId: undefined,
        loanId: '',
        alertType: 'MATURITY',
        alertLevel: 'MEDIUM',
        alertTitle: '',
        alertContent: '',
        alertStatus: 'PENDING',
        handlerId: '',
        handleOpinion: '',
        remark: ''
      },
      monitoringRules: {
        alertTitle: [{ required: true, message: '请输入预警标题', trigger: 'blur' }],
        alertType: [{ required: true, message: '请选择预警类型', trigger: 'change' }],
        alertLevel: [{ required: true, message: '请选择预警级别', trigger: 'change' }]
      },
      dialogHandleMonitoringVisible: false,
      handleMonitoringForm: {
        monitoringId: '',
        handlerId: '',
        handleOpinion: '',
        status: 'HANDLED'
      }
    }
  },
  mounted() {
    console.log('BankLoanManage 组件已挂载')
    try {
      this.getList()
      this.$nextTick(() => {
        try {
          this.initCharts()
        } catch (error) {
          console.error('图表初始化失败:', error)
        }
      })
    } catch (error) {
      console.error('组件挂载过程出错:', error)
    }
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
    // 响应码校验辅助方法
    isSuccessResponse(response) {
      return response && [1, 200, '1', '200'].includes(response.code)
    },
    getList() {
      this.listLoading = true
      // 调用真实API获取数据
      const params = {
        pageNum: this.listQuery.page,
        pageSize: this.listQuery.limit,
        applicationNo: this.listQuery.applicationNo || undefined,
        bankCode: this.listQuery.bankCode || undefined,
        loanType: this.listQuery.loanType || undefined,
        applicationStatus: this.listQuery.applicationStatus || undefined
      }

      getBankLoanPage(params).then(response => {
        if (this.isSuccessResponse(response)) {
          // 处理分页数据 - 兼容多种返回格式
          this.loanList = response.data.rows || response.data.tlist || response.data.list || response.data.records || []
          this.total = parseInt(response.data.total) || response.data.totalRecord || 0
        } else {
          this.loanList = []
          this.total = 0
          if (response && response.msg) {
            this.$message.error(response.msg)
          }
        }
        this.listLoading = false
        // 加载统计数据
        this.loadStatistics()
      }).catch(error => {
        console.error('查询银行贷款数据异常:', error)
        this.loanList = []
        this.total = 0
        this.listLoading = false
        this.$message.error('查询银行贷款数据失败，请稍后重试')
      })
    },
    loadStatistics() {
      // 加载统计数据
      getBankLoanStatistics({}).then(response => {
        if (this.isSuccessResponse(response) && response.data) {
          this.totalLoans = response.data.totalCount || 0
          // 金额转换为万元
          this.outstandingAmount = Math.round((response.data.totalOutstandingAmount || 0) / 10000 * 100) / 100
          this.averageRate = response.data.averageRate || 0
          this.approvalRate = response.data.approvalRate || 0
        }
      }).catch(error => {
        console.error('加载统计数据失败:', error)
      })
    },
    initCharts() {
      try {
        const echarts = require('echarts')

        // 初始化贷款类型图表
        const loanTypeChartDom = document.getElementById('loanTypeChart')
        if (loanTypeChartDom) {
          this.loanTypeChart = echarts.init(loanTypeChartDom)
          this.updateLoanTypeChart()
        } else {
          console.warn('找不到 loanTypeChart 元素')
        }

        // 初始化趋势图表
        const trendChartDom = document.getElementById('loanTrendChart')
        if (trendChartDom) {
          this.trendChart = echarts.init(trendChartDom)
          this.updateTrendChart()
        } else {
          console.warn('找不到 loanTrendChart 元素')
        }
      } catch (error) {
        console.error('initCharts 执行失败:', error)
        throw error
      }
    },
    updateLoanTypeChart() {
      // 调用后端API获取贷款类型分布数据
      getBankLoanTypeDistribution({}).then(response => {
        let data = []
        if (this.isSuccessResponse(response) && response.data) {
          // 定义颜色映射
          const colorMap = {
            '信用额度': '#409EFF',
            '定期贷款': '#67C23A',
            '循环贷款': '#E6A23C',
            '抵押贷款': '#F56C6C',
            '担保贷款': '#909399',
            'CREDIT_LINE': '#409EFF',
            'TERM_LOAN': '#67C23A',
            'REVOLVING_LOAN': '#E6A23C',
            'MORTGAGE': '#F56C6C',
            'GUARANTEE': '#909399'
          }
          data = response.data.map(item => ({
            name: item.name,
            value: item.value || 0,
            itemStyle: { color: colorMap[item.name] || '#409EFF' }
          }))
        }
        // 如果没有数据，使用默认空数据
        if (data.length === 0) {
          data = [
            { name: '信用额度', value: 0, itemStyle: { color: '#409EFF' } },
            { name: '定期贷款', value: 0, itemStyle: { color: '#67C23A' } },
            { name: '循环贷款', value: 0, itemStyle: { color: '#E6A23C' } },
            { name: '抵押贷款', value: 0, itemStyle: { color: '#F56C6C' } },
            { name: '担保贷款', value: 0, itemStyle: { color: '#909399' } }
          ]
        }
        this.renderLoanTypeChart(data)
      }).catch(error => {
        console.error('获取贷款类型分布数据失败:', error)
        // 使用默认数据
        const data = [
          { name: '信用额度', value: 0, itemStyle: { color: '#409EFF' } },
          { name: '定期贷款', value: 0, itemStyle: { color: '#67C23A' } },
          { name: '循环贷款', value: 0, itemStyle: { color: '#E6A23C' } },
          { name: '抵押贷款', value: 0, itemStyle: { color: '#F56C6C' } },
          { name: '担保贷款', value: 0, itemStyle: { color: '#909399' } }
        ]
        this.renderLoanTypeChart(data)
      })
    },
    renderLoanTypeChart(data) {
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
      // 调用后端API获取贷款申请趋势数据
      getBankLoanTrend({ period: this.trendPeriod }).then(response => {
        let months = []
        let applicationData = []
        let amountData = []

        if (this.isSuccessResponse(response) && response.data && response.data.length > 0) {
          // 处理后端返回的数据
          response.data.forEach(item => {
            months.push(item.month + '月')
            applicationData.push(item.applicationCount || 0)
            // 金额转换为万元
            amountData.push(Math.round((item.applicationAmount || 0) / 10000))
          })
        }

        // 如果没有数据，使用12个月的空数据
        if (months.length === 0) {
          months = this.generateMonthLabels(12)
          applicationData = new Array(12).fill(0)
          amountData = new Array(12).fill(0)
        }

        this.renderTrendChart(months, applicationData, amountData)
      }).catch(error => {
        console.error('获取贷款申请趋势数据失败:', error)
        // 使用默认空数据
        const months = this.generateMonthLabels(12)
        const applicationData = new Array(12).fill(0)
        const amountData = new Array(12).fill(0)
        this.renderTrendChart(months, applicationData, amountData)
      })
    },
    renderTrendChart(months, applicationData, amountData) {
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
      console.log('handleCreate 被调用')
      this.resetTemp()
      // 预先生成申请编号
      this.temp.applicationNo = 'BL' + new Date().getTime()
      this.dialogStatus = 'create'
      this.dialogTitle = '新增贷款申请'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      console.log('dialogFormVisible 设置为:', this.dialogFormVisible)
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      console.log('handleViewDetail 被调用, row:', row)
      this.currentLoan = row
      this.dialogDetailVisible = true
      console.log('dialogDetailVisible 设置为:', this.dialogDetailVisible)
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
      console.log('handleEdit 被调用, row:', row)
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑贷款申请'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      console.log('dialogFormVisible 设置为:', this.dialogFormVisible)
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
    // ========== 合同管理方法 ==========
    handleContract(row) {
      this.currentLoan = row
      this.dialogContractVisible = true
      this.loadContractList(row.loanId)
    },
    loadContractList(loanId) {
      this.contractLoading = true
      getLoanContractsByLoanId(loanId).then(response => {
        if (this.isSuccessResponse(response)) {
          this.contractList = response.data || []
        } else {
          this.contractList = []
        }
        this.contractLoading = false
      }).catch(error => {
        console.error('加载合同列表失败:', error)
        this.contractList = []
        this.contractLoading = false
      })
    },
    resetContractForm() {
      this.contractForm = {
        contractId: undefined,
        loanId: this.currentLoan ? this.currentLoan.loanId : '',
        contractNo: '',
        contractName: '',
        contractStatus: 'DRAFT',
        contractCompleteness: 'COMPLETE',
        signingDate: '',
        effectiveDate: '',
        expiryDate: '',
        contractAmount: this.currentLoan ? this.currentLoan.loanAmount : null,
        contractFilePath: '',
        remark: ''
      }
    },
    handleAddContract() {
      this.resetContractForm()
    },
    handleEditContract(row) {
      this.contractForm = {
        contractId: row.contractId,
        loanId: row.loanId,
        contractNo: row.contractNo,
        contractName: row.contractName,
        contractStatus: row.contractStatus,
        contractCompleteness: row.contractCompleteness,
        signingDate: row.signingDate ? this.formatDate(row.signingDate) : '',
        effectiveDate: row.effectiveDate ? this.formatDate(row.effectiveDate) : '',
        expiryDate: row.expiryDate ? this.formatDate(row.expiryDate) : '',
        contractAmount: row.contractAmount,
        contractFilePath: row.contractFilePath,
        remark: row.remark
      }
    },
    handleSaveContract() {
      this.$refs['contractForm'].validate((valid) => {
        if (valid) {
          this.contractForm.loanId = this.currentLoan.loanId
          saveLoanContract(this.contractForm).then(response => {
            if (this.isSuccessResponse(response)) {
              this.$message.success(this.contractForm.contractId ? '合同更新成功' : '合同创建成功')
              this.loadContractList(this.currentLoan.loanId)
              this.resetContractForm()
            } else {
              this.$message.error(response.msg || '操作失败')
            }
          }).catch(error => {
            console.error('保存合同失败:', error)
            this.$message.error('保存合同失败')
          })
        }
      })
    },
    handleDeleteContract(row) {
      this.$confirm('确认删除该合同吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteLoanContract(row.contractId).then(response => {
          if (this.isSuccessResponse(response)) {
            this.$message.success('删除成功')
            this.loadContractList(this.currentLoan.loanId)
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    handleUpdateContractStatus(row, status) {
      updateLoanContractStatus(row.contractId, status).then(response => {
        if (this.isSuccessResponse(response)) {
          this.$message.success('状态更新成功')
          this.loadContractList(this.currentLoan.loanId)
        } else {
          this.$message.error(response.msg || '状态更新失败')
        }
      })
    },
    getContractStatusText(status) {
      const map = { 'DRAFT': '草稿', 'ACTIVE': '生效中', 'EXPIRED': '已过期', 'TERMINATED': '已终止' }
      return map[status] || status
    },
    getContractStatusType(status) {
      const map = { 'DRAFT': 'info', 'ACTIVE': 'success', 'EXPIRED': 'warning', 'TERMINATED': 'danger' }
      return map[status] || 'info'
    },
    // ========== 还款计划方法 ==========
    handleRepayment(row) {
      this.currentLoan = row
      this.dialogRepaymentVisible = true
      this.showGenerateForm = false
      this.loadRepaymentList(row.loanId)
      // 初始化生成表单
      this.generateForm = {
        loanId: row.loanId,
        repaymentMethod: 'EQUAL_INSTALLMENT',
        periods: row.loanTerm || 12,
        loanAmount: row.loanAmount,
        interestRate: row.interestRate,
        startDate: row.applicationDate || ''
      }
    },
    loadRepaymentList(loanId) {
      this.repaymentLoading = true
      getRepaymentPlansByLoanId(loanId).then(response => {
        if (this.isSuccessResponse(response)) {
          this.repaymentList = response.data || []
        } else {
          this.repaymentList = []
        }
        this.repaymentLoading = false
      }).catch(error => {
        console.error('加载还款计划失败:', error)
        this.repaymentList = []
        this.repaymentLoading = false
      })
    },
    handleShowGenerateForm() {
      this.showGenerateForm = true
    },
    handleGenerateRepaymentPlans() {
      if (!this.generateForm.periods || !this.generateForm.loanAmount || !this.generateForm.interestRate) {
        this.$message.warning('请填写完整的生成参数')
        return
      }
      this.$confirm('生成还款计划将覆盖现有计划，确认继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        generateRepaymentPlans(this.generateForm).then(response => {
          if (this.isSuccessResponse(response)) {
            this.$message.success('还款计划生成成功')
            this.showGenerateForm = false
            this.loadRepaymentList(this.currentLoan.loanId)
          } else {
            this.$message.error(response.msg || '生成失败')
          }
        }).catch(error => {
          console.error('生成还款计划失败:', error)
          this.$message.error('生成还款计划失败')
        })
      }).catch(() => {})
    },
    handleExecuteRepayment(row) {
      this.executeRepaymentForm = {
        planId: row.planId,
        amount: row.totalAmount - row.paidAmount
      }
      this.dialogExecuteRepaymentVisible = true
    },
    submitExecuteRepayment() {
      if (!this.executeRepaymentForm.amount || this.executeRepaymentForm.amount <= 0) {
        this.$message.warning('请输入有效的还款金额')
        return
      }
      executeBankLoanRepayment(this.executeRepaymentForm.planId, this.executeRepaymentForm.amount).then(response => {
        if (this.isSuccessResponse(response)) {
          this.$message.success('还款成功')
          this.dialogExecuteRepaymentVisible = false
          this.loadRepaymentList(this.currentLoan.loanId)
        } else {
          this.$message.error(response.msg || '还款失败')
        }
      }).catch(error => {
        console.error('还款失败:', error)
        this.$message.error('还款失败')
      })
    },
    handleDeleteRepaymentPlan(row) {
      this.$confirm('确认删除该还款计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRepaymentPlan(row.planId).then(response => {
          if (this.isSuccessResponse(response)) {
            this.$message.success('删除成功')
            this.loadRepaymentList(this.currentLoan.loanId)
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    getPaymentStatusText(status) {
      const map = { 'PENDING': '待还款', 'PARTIAL': '部分还款', 'PAID': '已还清', 'OVERDUE': '已逾期' }
      return map[status] || status
    },
    getPaymentStatusType(status) {
      const map = { 'PENDING': 'warning', 'PARTIAL': 'primary', 'PAID': 'success', 'OVERDUE': 'danger' }
      return map[status] || 'info'
    },
    // ========== 贷款监控方法 ==========
    handleMonitor(row) {
      this.currentLoan = row
      this.dialogMonitoringVisible = true
      this.loadMonitoringList(row.loanId)
    },
    loadMonitoringList(loanId) {
      this.monitoringLoading = true
      getLoanMonitoringByLoanId(loanId).then(response => {
        if (this.isSuccessResponse(response)) {
          this.monitoringList = response.data || []
        } else {
          this.monitoringList = []
        }
        this.monitoringLoading = false
      }).catch(error => {
        console.error('加载监控预警失败:', error)
        this.monitoringList = []
        this.monitoringLoading = false
      })
    },
    resetMonitoringForm() {
      this.monitoringForm = {
        monitoringId: undefined,
        loanId: this.currentLoan ? this.currentLoan.loanId : '',
        alertType: 'MATURITY',
        alertLevel: 'MEDIUM',
        alertTitle: '',
        alertContent: '',
        alertStatus: 'PENDING',
        handlerId: '',
        handleOpinion: '',
        remark: ''
      }
    },
    handleAddMonitoring() {
      this.resetMonitoringForm()
    },
    handleEditMonitoring(row) {
      this.monitoringForm = Object.assign({}, row)
    },
    handleSaveMonitoring() {
      this.$refs['monitoringForm'].validate((valid) => {
        if (valid) {
          this.monitoringForm.loanId = this.currentLoan.loanId
          const isUpdate = !!this.monitoringForm.monitoringId
          const apiCall = isUpdate ? updateLoanMonitoring : addLoanMonitoring
          const successMsg = isUpdate ? '预警更新成功' : '预警创建成功'

          apiCall(this.monitoringForm).then(response => {
            if (this.isSuccessResponse(response)) {
              this.$message.success(successMsg)
              this.loadMonitoringList(this.currentLoan.loanId)
              this.resetMonitoringForm()
            } else {
              this.$message.error(response.msg || '操作失败')
            }
          }).catch(error => {
            console.error('保存预警失败:', error)
            this.$message.error('保存预警失败')
          })
        }
      })
    },
    handleDeleteMonitoring(row) {
      this.$confirm('确认删除该预警吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteLoanMonitoring(row.monitoringId).then(response => {
          if (this.isSuccessResponse(response)) {
            this.$message.success('删除成功')
            this.loadMonitoringList(this.currentLoan.loanId)
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    handleProcessMonitoring(row) {
      this.handleMonitoringForm = {
        monitoringId: row.monitoringId,
        handlerId: '',
        handleOpinion: '',
        status: 'HANDLED'
      }
      this.dialogHandleMonitoringVisible = true
    },
    submitHandleMonitoring() {
      if (!this.handleMonitoringForm.handleOpinion) {
        this.$message.warning('请输入处理意见')
        return
      }
      handleLoanMonitoring(
        this.handleMonitoringForm.monitoringId,
        this.handleMonitoringForm.handlerId,
        this.handleMonitoringForm.handleOpinion,
        this.handleMonitoringForm.status
      ).then(response => {
        if (this.isSuccessResponse(response)) {
          this.$message.success('处理成功')
          this.dialogHandleMonitoringVisible = false
          this.loadMonitoringList(this.currentLoan.loanId)
        } else {
          this.$message.error(response.msg || '处理失败')
        }
      }).catch(error => {
        console.error('处理预警失败:', error)
        this.$message.error('处理预警失败')
      })
    },
    getAlertTypeText(type) {
      const map = { 'MATURITY': '到期预警', 'OVERDUE': '逾期预警', 'RATE_CHANGE': '利率变动', 'AMOUNT_CHANGE': '金额变动' }
      return map[type] || type
    },
    getAlertLevelText(level) {
      const map = { 'LOW': '低', 'MEDIUM': '中', 'HIGH': '高', 'CRITICAL': '紧急' }
      return map[level] || level
    },
    getAlertLevelType(level) {
      const map = { 'LOW': 'info', 'MEDIUM': 'warning', 'HIGH': 'danger', 'CRITICAL': 'danger' }
      return map[level] || 'info'
    },
    getAlertStatusText(status) {
      const map = { 'PENDING': '待处理', 'HANDLING': '处理中', 'HANDLED': '已处理', 'CLOSED': '已关闭' }
      return map[status] || status
    },
    getAlertStatusType(status) {
      const map = { 'PENDING': 'warning', 'HANDLING': 'primary', 'HANDLED': 'success', 'CLOSED': 'info' }
      return map[status] || 'info'
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
          // 准备提交数据
          const submitData = {
            ...this.temp,
            applicationStatus: 'PENDING'
          }
          // 调用后端API创建贷款
          createBankLoan(submitData).then(response => {
            if (this.isSuccessResponse(response)) {
              this.dialogFormVisible = false
              this.$message({
                type: 'success',
                message: '贷款申请创建成功'
              })
              // 刷新列表
              this.getList()
            } else {
              this.$message.error(response.msg || '创建失败')
            }
          }).catch(error => {
            console.error('创建贷款失败:', error)
            this.$message.error('创建贷款失败，请稍后重试')
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const submitData = {
            ...this.temp,
            loanId: this.temp.loanId || this.temp.applicationId
          }
          updateBankLoan(submitData).then(response => {
            if (this.isSuccessResponse(response)) {
              this.dialogFormVisible = false
              this.$message({
                type: 'success',
                message: '贷款申请更新成功'
              })
              this.getList()
            } else {
              this.$message.error(response.msg || '更新失败')
            }
          }).catch(error => {
            console.error('更新贷款失败:', error)
            this.$message.error('更新贷款失败，请稍后重试')
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
        'DISBURSED': 'success',
        'ACTIVE': 'success',
        'COMPLETED': 'info',
        'OVERDUE': 'danger'
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
        'DISBURSED': '已放款',
        'ACTIVE': '执行中',
        'COMPLETED': '已完成',
        'OVERDUE': '已逾期'
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
    },
    formatDate(timestamp) {
      if (!timestamp) return '-'
      const date = new Date(timestamp)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
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

  // 合同管理样式
  .contract-list-header,
  .contract-form-header,
  .monitoring-list-header,
  .monitoring-form-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    padding-bottom: 10px;
    border-bottom: 1px solid #EBEEF5;

    h4 {
      margin: 0;
      font-size: 14px;
      font-weight: 600;
      color: #303133;
    }
  }

  // 还款计划样式
  .generate-form-card {
    margin-bottom: 15px;

    .el-card__header {
      padding: 10px 15px;
      background: #f5f7fa;
    }
  }

  .repayment-toolbar {
    margin-bottom: 10px;
  }
}
</style>
