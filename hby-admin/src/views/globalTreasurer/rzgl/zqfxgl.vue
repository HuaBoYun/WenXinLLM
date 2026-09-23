<template>
  <div class="bond-issuance-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-tickets"></i>
            债券发行管理
          </h2>
          <p class="page-description">企业债券发行计划、审批、发行和兑付全流程管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增债券发行
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

    <!-- 债券概览卡片 -->
    <div class="bond-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">债券发行总数</div>
                <div class="card-value">{{ totalBonds }}</div>
                <div class="card-change">只</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon outstanding-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">存续债券余额</div>
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
                <div class="card-title">平均票面利率</div>
                <div class="card-value">{{ averageCouponRate }}</div>
                <div class="card-change">%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon rating-icon">
                <i class="el-icon-star-on"></i>
              </div>
              <div class="card-info">
                <div class="card-title">平均信用评级</div>
                <div class="card-value">{{ averageRating }}</div>
                <div class="card-change positive">级</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 债券分析图表 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>债券类型分布</h3>
            <div class="chart-controls">
              <el-radio-group v-model="bondTypeChartType" size="small" @change="handleBondTypeChartChange">
                <el-radio-button label="pie">饼图</el-radio-button>
                <el-radio-button label="bar">柱状图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="bondTypeChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>债券发行趋势</h3>
            <div class="chart-controls">
              <el-radio-group v-model="trendPeriod" size="small" @change="handleTrendPeriodChange">
                <el-radio-button label="6M">6个月</el-radio-button>
                <el-radio-button label="1Y">1年</el-radio-button>
                <el-radio-button label="2Y">2年</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="bondTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="发行编号">
            <el-input
              v-model="listQuery.issuanceNo"
              placeholder="请输入发行编号"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="债券名称">
            <el-input
              v-model="listQuery.bondName"
              placeholder="请输入债券名称"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="债券类型">
            <el-select
              v-model="listQuery.bondType"
              placeholder="请选择债券类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="企业债券" value="CORPORATE" />
              <el-option label="政府债券" value="GOVERNMENT" />
              <el-option label="可转换债券" value="CONVERTIBLE" />
              <el-option label="永续债券" value="PERPETUAL" />
            </el-select>
          </el-form-item>
          <el-form-item label="发行状态">
            <el-select
              v-model="listQuery.issuanceStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="计划中" value="PLANNING" />
              <el-option label="已审批" value="APPROVED" />
              <el-option label="已发行" value="ISSUED" />
              <el-option label="存续中" value="OUTSTANDING" />
              <el-option label="已到期" value="MATURED" />
              <el-option label="已取消" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item label="发行日期">
            <el-date-picker
              v-model="listQuery.issuanceDateRange"
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

    <!-- 债券发行表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="bondList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="发行ID" prop="issuanceId" width="80" align="center" />
        <el-table-column label="发行编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.issuanceNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="债券名称" width="200px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.bondName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="债券代码" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="bond-code">{{ row.bondCode || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="债券类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getBondTypeTagType(row.bondType)" size="mini">
              {{ getBondTypeText(row.bondType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发行金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="issuance-amount">{{ formatCurrency(row.issuanceAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="面值" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.faceValue }}元</span>
          </template>
        </el-table-column>
        <el-table-column label="票面利率" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.couponRate }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="债券期限" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.bondTerm }}{{ getTermUnitText(row.termUnit) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="付息频率" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ getPaymentFrequencyText(row.paymentFrequency) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="信用评级" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRatingTagType(row.creditRating)" size="mini">
              {{ row.creditRating || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发行状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getIssuanceStatusTagType(row.issuanceStatus)" size="mini">
              {{ getIssuanceStatusText(row.issuanceStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发行日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.issuanceDate || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.maturityDate || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.issuanceStatus === 'PLANNING'" type="primary" size="mini" @click="handleApprove(row)">
              审批
            </el-button>
            <el-button v-if="row.issuanceStatus === 'APPROVED'" type="success" size="mini" @click="handleIssue(row)">
              发行
            </el-button>
            <el-button v-if="row.issuanceStatus === 'OUTSTANDING'" type="warning" size="mini" @click="handlePayInterest(row)">
              付息
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
                <el-dropdown-item :command="{action: 'rating', row: row}">评级管理</el-dropdown-item>
                <el-dropdown-item :command="{action: 'listing', row: row}">上市管理</el-dropdown-item>
                <el-dropdown-item :command="{action: 'redeem', row: row}">兑付管理</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 债券发行创建/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="900px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-tabs v-model="activeFormTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="发行编号" prop="issuanceNo">
                  <el-input v-model="temp.issuanceNo" placeholder="系统自动生成" :disabled="dialogStatus==='update'" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="关联计划">
                  <el-select v-model="temp.planId" placeholder="请选择融资计划" style="width: 100%;">
                    <el-option label="企业债券发行计划" value="3" />
                    <el-option label="项目建设融资计划" value="4" />
                    <el-option label="流动资金补充计划" value="5" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="债券名称" prop="bondName">
                  <el-input v-model="temp.bondName" placeholder="请输入债券名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="债券代码">
                  <el-input v-model="temp.bondCode" placeholder="请输入债券代码" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="债券类型" prop="bondType">
                  <el-select v-model="temp.bondType" placeholder="请选择债券类型" style="width: 100%;">
                    <el-option label="企业债券" value="CORPORATE" />
                    <el-option label="政府债券" value="GOVERNMENT" />
                    <el-option label="可转换债券" value="CONVERTIBLE" />
                    <el-option label="永续债券" value="PERPETUAL" />
                  </el-select>
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
                <el-form-item label="发行金额" prop="issuanceAmount">
                  <el-input-number
                    v-model="temp.issuanceAmount"
                    :precision="2"
                    :step="10000000"
                    :min="0"
                    :max="50000000000"
                    style="width: 100%;"
                    placeholder="请输入发行金额"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="面值" prop="faceValue">
                  <el-input-number
                    v-model="temp.faceValue"
                    :precision="2"
                    :step="10"
                    :min="1"
                    :max="10000"
                    style="width: 100%;"
                    placeholder="请输入面值"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票面利率" prop="couponRate">
                  <el-input-number
                    v-model="temp.couponRate"
                    :precision="4"
                    :step="0.1"
                    :min="0"
                    :max="20"
                    style="width: 100%;"
                    placeholder="请输入票面利率"
                  />
                  <span style="margin-left: 8px;">%</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="债券期限" prop="bondTerm">
                  <el-input-number
                    v-model="temp.bondTerm"
                    :min="1"
                    :max="50"
                    style="width: 70%;"
                    placeholder="请输入期限"
                  />
                  <el-select v-model="temp.termUnit" style="width: 28%; margin-left: 2%;">
                    <el-option label="月" value="MONTH" />
                    <el-option label="年" value="YEAR" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <el-tab-pane label="发行条件" name="conditions">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="付息频率">
                  <el-select v-model="temp.paymentFrequency" placeholder="请选择付息频率" style="width: 100%;">
                    <el-option label="月付" value="MONTHLY" />
                    <el-option label="季付" value="QUARTERLY" />
                    <el-option label="半年付" value="SEMI_ANNUAL" />
                    <el-option label="年付" value="ANNUAL" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="信用评级">
                  <el-select v-model="temp.creditRating" placeholder="请选择信用评级" style="width: 100%;">
                    <el-option label="AAA" value="AAA" />
                    <el-option label="AA+" value="AA+" />
                    <el-option label="AA" value="AA" />
                    <el-option label="AA-" value="AA-" />
                    <el-option label="A+" value="A+" />
                    <el-option label="A" value="A" />
                    <el-option label="A-" value="A-" />
                    <el-option label="BBB+" value="BBB+" />
                    <el-option label="BBB" value="BBB" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="承销商">
                  <el-input v-model="temp.underwriter" placeholder="请输入承销商名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="受托管理人">
                  <el-input v-model="temp.trustee" placeholder="请输入受托管理人名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="评级机构">
                  <el-input v-model="temp.ratingAgency" placeholder="请输入评级机构名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="上市交易所">
                  <el-input v-model="temp.listingExchange" placeholder="请输入上市交易所" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="发行日期">
                  <el-date-picker
                    v-model="temp.issuanceDate"
                    type="date"
                    placeholder="选择发行日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期日期">
                  <el-date-picker
                    v-model="temp.maturityDate"
                    type="date"
                    placeholder="选择到期日期"
                    style="width: 100%;"
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
                <div slot="tip" class="el-upload__tip">支持上传多个文件，如发行文件、评级报告等</div>
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

    <!-- 债券详情对话框 -->
    <el-dialog title="债券发行详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentBond" class="bond-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="发行编号">{{ currentBond.issuanceNo }}</el-descriptions-item>
          <el-descriptions-item label="债券名称">{{ currentBond.bondName }}</el-descriptions-item>
          <el-descriptions-item label="债券代码">{{ currentBond.bondCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="债券类型">{{ getBondTypeText(currentBond.bondType) }}</el-descriptions-item>
          <el-descriptions-item label="发行金额">{{ formatCurrency(currentBond.issuanceAmount) }}</el-descriptions-item>
          <el-descriptions-item label="面值">{{ currentBond.faceValue }}元</el-descriptions-item>
          <el-descriptions-item label="票面利率">{{ currentBond.couponRate }}%</el-descriptions-item>
          <el-descriptions-item label="债券期限">{{ currentBond.bondTerm }}{{ getTermUnitText(currentBond.termUnit) }}</el-descriptions-item>
          <el-descriptions-item label="付息频率">{{ getPaymentFrequencyText(currentBond.paymentFrequency) }}</el-descriptions-item>
          <el-descriptions-item label="信用评级">{{ currentBond.creditRating || '-' }}</el-descriptions-item>
          <el-descriptions-item label="承销商">{{ currentBond.underwriter || '-' }}</el-descriptions-item>
          <el-descriptions-item label="受托管理人">{{ currentBond.trustee || '-' }}</el-descriptions-item>
          <el-descriptions-item label="发行状态">
            <el-tag :type="getIssuanceStatusTagType(currentBond.issuanceStatus)">
              {{ getIssuanceStatusText(currentBond.issuanceStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="发行日期">{{ currentBond.issuanceDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ currentBond.maturityDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentBond.createTime }}</el-descriptions-item>
        </el-descriptions>

        <div class="bond-progress">
          <h4>发行进度</h4>
          <el-steps :active="getIssuanceStep(currentBond)" finish-status="success">
            <el-step title="计划制定" description="制定债券发行计划"></el-step>
            <el-step title="审批通过" description="监管机构审批"></el-step>
            <el-step title="债券发行" description="公开发行债券"></el-step>
            <el-step title="存续管理" description="债券存续期管理"></el-step>
            <el-step title="到期兑付" description="债券到期兑付"></el-step>
          </el-steps>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentBond && currentBond.issuanceStatus === 'PLANNING'" type="primary" @click="handleApprove(currentBond)">
          提交审批
        </el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog title="债券发行审批" :visible.sync="dialogApprovalVisible" width="600px">
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
            :step="1000000"
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

    <!-- 发行对话框 -->
    <el-dialog title="债券发行" :visible.sync="dialogIssuanceVisible" width="600px">
      <el-form ref="issuanceForm" :model="issuanceForm" label-width="100px">
        <el-form-item label="实际发行金额" prop="actualAmount">
          <el-input-number
            v-model="issuanceForm.actualAmount"
            :precision="2"
            :step="1000000"
            :min="0"
            style="width: 100%;"
            placeholder="请输入实际发行金额"
          />
        </el-form-item>
        <el-form-item label="发行日期" prop="issuanceDate">
          <el-date-picker
            v-model="issuanceForm.issuanceDate"
            type="date"
            placeholder="选择发行日期"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="上市日期">
          <el-date-picker
            v-model="issuanceForm.listingDate"
            type="date"
            placeholder="选择上市日期"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="发行说明">
          <el-input v-model="issuanceForm.issuanceNotes" type="textarea" :rows="3" placeholder="请输入发行说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogIssuanceVisible = false">取消</el-button>
        <el-button type="primary" @click="submitIssuance">确认发行</el-button>
      </div>
    </el-dialog>

    <!-- 付息对话框 -->
    <el-dialog title="债券付息" :visible.sync="dialogPayInterestVisible" width="600px">
      <el-form ref="payInterestForm" :model="payInterestForm" label-width="100px">
        <el-form-item label="付息金额" prop="interestAmount">
          <el-input-number
            v-model="payInterestForm.interestAmount"
            :precision="2"
            :step="100000"
            :min="0"
            style="width: 100%;"
            placeholder="请输入付息金额"
          />
        </el-form-item>
        <el-form-item label="付息日期" prop="paymentDate">
          <el-date-picker
            v-model="payInterestForm.paymentDate"
            type="date"
            placeholder="选择付息日期"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="付息说明">
          <el-input v-model="payInterestForm.paymentNotes" type="textarea" :rows="3" placeholder="请输入付息说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogPayInterestVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPayInterest">确认付息</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBondFinancingPage, createBondFinancing, updateBondFinancing, issueBond, payBondInterest } from '@/api/globalTreasurer/rzgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'BondIssuanceManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        issuanceNo: undefined,
        bondName: undefined,
        bondType: undefined,
        issuanceStatus: undefined,
        issuanceDateRange: undefined
      },
      totalBonds: 18,
      outstandingAmount: 285600.5,
      averageCouponRate: 4.85,
      averageRating: 'AA',
      bondTypeChartType: 'pie',
      trendPeriod: '1Y',
      bondList: [],
      multipleSelection: [],
      currentBond: null,
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogApprovalVisible: false,
      dialogIssuanceVisible: false,
      dialogPayInterestVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      activeFormTab: 'basic',
      temp: {
        issuanceId: undefined,
        issuanceNo: '',
        planId: undefined,
        bondName: '',
        bondCode: '',
        bondType: '',
        issuanceAmount: null,
        currencyCode: 'CNY',
        faceValue: 100,
        couponRate: null,
        bondTerm: null,
        termUnit: 'YEAR',
        paymentFrequency: 'ANNUAL',
        underwriter: '',
        trustee: '',
        ratingAgency: '',
        creditRating: '',
        listingExchange: '',
        issuanceDate: null,
        maturityDate: null,
        remark: ''
      },
      approvalForm: {
        approvalResult: '',
        approvalOpinion: '',
        approvedAmount: null,
        approvedRate: null
      },
      issuanceForm: {
        actualAmount: null,
        issuanceDate: null,
        listingDate: null,
        issuanceNotes: ''
      },
      payInterestForm: {
        interestAmount: null,
        paymentDate: null,
        paymentNotes: ''
      },
      rules: {
        issuanceNo: [{ required: true, message: '发行编号不能为空', trigger: 'blur' }],
        bondName: [{ required: true, message: '债券名称不能为空', trigger: 'blur' }],
        bondType: [{ required: true, message: '请选择债券类型', trigger: 'change' }],
        issuanceAmount: [{ required: true, message: '请输入发行金额', trigger: 'blur' }],
        faceValue: [{ required: true, message: '请输入面值', trigger: 'blur' }],
        couponRate: [{ required: true, message: '请输入票面利率', trigger: 'blur' }],
        bondTerm: [{ required: true, message: '请输入债券期限', trigger: 'blur' }]
      },
      bondTypeChart: null,
      trendChart: null
    }
  },
  mounted() {
    this.getList()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.bondTypeChart) {
      this.bondTypeChart.dispose()
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
        this.bondList = [
          {
            issuanceId: 1,
            issuanceNo: 'BD20240925001',
            planId: 3,
            bondName: '示例云科技企业债券',
            bondCode: 'HBY001',
            bondType: 'CORPORATE',
            issuanceAmount: 100000000.00,
            currencyCode: 'CNY',
            faceValue: 100.00,
            couponRate: 4.8,
            bondTerm: 3,
            termUnit: 'YEAR',
            paymentFrequency: 'ANNUAL',
            underwriter: '中信证券股份有限公司',
            trustee: '中信信托有限责任公司',
            ratingAgency: '中诚信国际信用评级有限公司',
            creditRating: 'AA',
            listingExchange: '上海证券交易所',
            issuanceStatus: 'OUTSTANDING',
            issuanceDate: '2024-01-15',
            maturityDate: '2027-01-15',
            createTime: '2024-09-25 10:00:00'
          },
          {
            issuanceId: 2,
            issuanceNo: 'BD20240920002',
            planId: 4,
            bondName: '示例云绿色发展债券',
            bondCode: 'HBY002',
            bondType: 'CORPORATE',
            issuanceAmount: 200000000.00,
            currencyCode: 'CNY',
            faceValue: 100.00,
            couponRate: 5.2,
            bondTerm: 5,
            termUnit: 'YEAR',
            paymentFrequency: 'SEMI_ANNUAL',
            underwriter: '华泰联合证券有限责任公司',
            trustee: '华润深国投信托有限公司',
            ratingAgency: '联合资信评估股份有限公司',
            creditRating: 'AA+',
            listingExchange: '深圳证券交易所',
            issuanceStatus: 'APPROVED',
            issuanceDate: null,
            maturityDate: null,
            createTime: '2024-09-20 14:30:00'
          },
          {
            issuanceId: 3,
            issuanceNo: 'BD20240915003',
            planId: 5,
            bondName: '示例云可转换公司债券',
            bondCode: 'HBY003',
            bondType: 'CONVERTIBLE',
            issuanceAmount: 150000000.00,
            currencyCode: 'CNY',
            faceValue: 100.00,
            couponRate: 3.5,
            bondTerm: 6,
            termUnit: 'YEAR',
            paymentFrequency: 'ANNUAL',
            underwriter: '国泰君安证券股份有限公司',
            trustee: '平安信托有限责任公司',
            ratingAgency: '大公国际资信评估有限公司',
            creditRating: 'AA-',
            listingExchange: '上海证券交易所',
            issuanceStatus: 'PLANNING',
            issuanceDate: null,
            maturityDate: null,
            createTime: '2024-09-15 16:45:00'
          }
        ]
        this.total = this.bondList.length
        this.listLoading = false
      }, 1000)
    },
    initCharts() {
      const echarts = require('echarts')

      // 初始化债券类型图表
      this.bondTypeChart = echarts.init(document.getElementById('bondTypeChart'))
      this.updateBondTypeChart()

      // 初始化趋势图表
      this.trendChart = echarts.init(document.getElementById('bondTrendChart'))
      this.updateTrendChart()
    },
    updateBondTypeChart() {
      const data = [
        { name: '企业债券', value: 12, itemStyle: { color: '#409EFF' } },
        { name: '可转换债券', value: 3, itemStyle: { color: '#67C23A' } },
        { name: '政府债券', value: 2, itemStyle: { color: '#E6A23C' } },
        { name: '永续债券', value: 1, itemStyle: { color: '#F56C6C' } }
      ]

      let option = {}

      if (this.bondTypeChartType === 'pie') {
        option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c}只 ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 10,
            data: data.map(item => item.name)
          },
          series: [
            {
              name: '债券类型',
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
              name: '发行数量',
              type: 'bar',
              data: data.map(item => ({
                value: item.value,
                itemStyle: item.itemStyle
              }))
            }
          ]
        }
      }

      this.bondTypeChart.setOption(option)
    },
    updateTrendChart() {
      const months = this.generateMonthLabels(12)
      const issuanceData = this.generateMockData(12, 1, 4)
      const amountData = this.generateMockData(12, 15000, 50000)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['发行数量', '发行金额(万元)']
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
            name: '数量(只)',
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
            name: '发行数量',
            type: 'line',
            data: issuanceData,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '发行金额(万元)',
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
    handleBondTypeChartChange() {
      this.updateBondTypeChart()
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
        issuanceNo: undefined,
        bondName: undefined,
        bondType: undefined,
        issuanceStatus: undefined,
        issuanceDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '新增债券发行'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentBond = row
      this.dialogDetailVisible = true
    },
    handleApprove(row) {
      this.currentBond = row
      this.approvalForm = {
        approvalResult: '',
        approvalOpinion: '',
        approvedAmount: row.issuanceAmount,
        approvedRate: row.couponRate
      }
      this.dialogApprovalVisible = true
    },
    handleIssue(row) {
      this.currentBond = row
      this.issuanceForm = {
        actualAmount: row.issuanceAmount,
        issuanceDate: new Date(),
        listingDate: null,
        issuanceNotes: ''
      }
      this.dialogIssuanceVisible = true
    },
    handlePayInterest(row) {
      this.currentBond = row
      // 计算付息金额
      const interestAmount = (row.issuanceAmount * row.couponRate / 100)
      if (row.paymentFrequency === 'SEMI_ANNUAL') {
        this.payInterestForm.interestAmount = interestAmount / 2
      } else if (row.paymentFrequency === 'QUARTERLY') {
        this.payInterestForm.interestAmount = interestAmount / 4
      } else if (row.paymentFrequency === 'MONTHLY') {
        this.payInterestForm.interestAmount = interestAmount / 12
      } else {
        this.payInterestForm.interestAmount = interestAmount
      }

      this.payInterestForm.paymentDate = new Date()
      this.payInterestForm.paymentNotes = ''
      this.dialogPayInterestVisible = true
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
        case 'rating':
          this.handleRating(row)
          break
        case 'listing':
          this.handleListing(row)
          break
        case 'redeem':
          this.handleRedeem(row)
          break
      }
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      if (this.temp.issuanceDate) {
        this.temp.issuanceDate = new Date(this.temp.issuanceDate)
      }
      if (this.temp.maturityDate) {
        this.temp.maturityDate = new Date(this.temp.maturityDate)
      }
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑债券发行'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleCopy(row) {
      this.temp = Object.assign({}, row)
      this.temp.issuanceId = undefined
      this.temp.issuanceNo = ''
      this.temp.bondName = row.bondName + ' (副本)'
      this.temp.bondCode = ''
      this.temp.issuanceStatus = 'PLANNING'
      this.dialogStatus = 'create'
      this.dialogTitle = '复制债券发行'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleRating(row) {
      this.$message({
        type: 'info',
        message: '评级管理功能'
      })
    },
    handleListing(row) {
      this.$message({
        type: 'info',
        message: '上市管理功能'
      })
    },
    handleRedeem(row) {
      this.$message({
        type: 'info',
        message: '兑付管理功能'
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
        this.currentBond.issuanceStatus = 'APPROVED'
        this.currentBond.issuanceAmount = this.approvalForm.approvedAmount
        this.currentBond.couponRate = this.approvalForm.approvedRate
      } else {
        this.currentBond.issuanceStatus = 'PLANNING'
      }

      this.dialogApprovalVisible = false
      this.$message({
        type: 'success',
        message: '审批完成!'
      })
    },
    submitIssuance() {
      if (!this.issuanceForm.actualAmount || !this.issuanceForm.issuanceDate) {
        this.$message({
          type: 'warning',
          message: '请填写完整的发行信息'
        })
        return
      }

      this.currentBond.issuanceStatus = 'ISSUED'
      this.currentBond.issuanceAmount = this.issuanceForm.actualAmount
      this.currentBond.issuanceDate = this.issuanceForm.issuanceDate.toISOString().slice(0, 10)
      if (this.issuanceForm.listingDate) {
        this.currentBond.listingDate = this.issuanceForm.listingDate.toISOString().slice(0, 10)
      }

      // 计算到期日期
      const maturityDate = new Date(this.issuanceForm.issuanceDate)
      if (this.currentBond.termUnit === 'YEAR') {
        maturityDate.setFullYear(maturityDate.getFullYear() + this.currentBond.bondTerm)
      } else {
        maturityDate.setMonth(maturityDate.getMonth() + this.currentBond.bondTerm)
      }
      this.currentBond.maturityDate = maturityDate.toISOString().slice(0, 10)

      // 更新状态为存续中
      setTimeout(() => {
        this.currentBond.issuanceStatus = 'OUTSTANDING'
      }, 1000)

      this.dialogIssuanceVisible = false
      this.$message({
        type: 'success',
        message: '债券发行成功!'
      })
    },
    submitPayInterest() {
      if (!this.payInterestForm.interestAmount || !this.payInterestForm.paymentDate) {
        this.$message({
          type: 'warning',
          message: '请填写完整的付息信息'
        })
        return
      }

      this.dialogPayInterestVisible = false
      this.$message({
        type: 'success',
        message: '付息成功!'
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.issuanceId = parseInt(Math.random() * 100) + 1024
          this.temp.issuanceNo = 'BD' + new Date().getTime()
          this.temp.issuanceStatus = 'PLANNING'
          this.temp.createTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
          if (this.temp.issuanceDate) {
            this.temp.issuanceDate = this.temp.issuanceDate.toISOString().slice(0, 10)
          }
          if (this.temp.maturityDate) {
            this.temp.maturityDate = this.temp.maturityDate.toISOString().slice(0, 10)
          }
          this.bondList.unshift(this.temp)
          this.total = this.bondList.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '债券发行创建成功'
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.temp.issuanceDate && typeof this.temp.issuanceDate === 'object') {
            this.temp.issuanceDate = this.temp.issuanceDate.toISOString().slice(0, 10)
          }
          if (this.temp.maturityDate && typeof this.temp.maturityDate === 'object') {
            this.temp.maturityDate = this.temp.maturityDate.toISOString().slice(0, 10)
          }
          const index = this.bondList.findIndex(v => v.issuanceId === this.temp.issuanceId)
          this.bondList.splice(index, 1, this.temp)
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '债券发行更新成功'
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        issuanceId: undefined,
        issuanceNo: '',
        planId: undefined,
        bondName: '',
        bondCode: '',
        bondType: '',
        issuanceAmount: null,
        currencyCode: 'CNY',
        faceValue: 100,
        couponRate: null,
        bondTerm: null,
        termUnit: 'YEAR',
        paymentFrequency: 'ANNUAL',
        underwriter: '',
        trustee: '',
        ratingAgency: '',
        creditRating: '',
        listingExchange: '',
        issuanceDate: null,
        maturityDate: null,
        remark: ''
      }
    },
    getBondTypeTagType(type) {
      const typeMap = {
        'CORPORATE': 'primary',
        'GOVERNMENT': 'success',
        'CONVERTIBLE': 'warning',
        'PERPETUAL': 'info'
      }
      return typeMap[type] || 'default'
    },
    getBondTypeText(type) {
      const textMap = {
        'CORPORATE': '企业债券',
        'GOVERNMENT': '政府债券',
        'CONVERTIBLE': '可转换债券',
        'PERPETUAL': '永续债券'
      }
      return textMap[type] || type
    },
    getTermUnitText(unit) {
      const textMap = {
        'MONTH': '月',
        'YEAR': '年'
      }
      return textMap[unit] || unit
    },
    getPaymentFrequencyText(frequency) {
      const textMap = {
        'MONTHLY': '月付',
        'QUARTERLY': '季付',
        'SEMI_ANNUAL': '半年付',
        'ANNUAL': '年付'
      }
      return textMap[frequency] || frequency
    },
    getRatingTagType(rating) {
      if (!rating) return 'default'
      if (rating.startsWith('AAA')) return 'success'
      if (rating.startsWith('AA')) return 'primary'
      if (rating.startsWith('A')) return 'warning'
      return 'info'
    },
    getIssuanceStatusTagType(status) {
      const typeMap = {
        'PLANNING': 'info',
        'APPROVED': 'primary',
        'ISSUED': 'warning',
        'OUTSTANDING': 'success',
        'MATURED': 'default',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getIssuanceStatusText(status) {
      const textMap = {
        'PLANNING': '计划中',
        'APPROVED': '已审批',
        'ISSUED': '已发行',
        'OUTSTANDING': '存续中',
        'MATURED': '已到期',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },
    getIssuanceStep(bond) {
      const stepMap = {
        'PLANNING': 0,
        'APPROVED': 1,
        'ISSUED': 2,
        'OUTSTANDING': 3,
        'MATURED': 4
      }
      return stepMap[bond.issuanceStatus] || 0
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
.bond-issuance-manage {
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

  .bond-overview {
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
          &.outstanding-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.rate-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.rating-icon {
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

  .bond-code {
    font-weight: 600;
    color: #E6A23C;
  }

  .issuance-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .bond-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }

    .bond-progress {
      margin-top: 20px;

      h4 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 14px;
        font-weight: 600;
        border-left: 3px solid #409EFF;
        padding-left: 8px;
      }
    }
  }

  .upload-demo {
    width: 100%;
  }
}
</style>