<template>
  <div class="payment-management-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-money"></i>
          付款管理
        </h1>
        <p class="page-description">处理供应商付款、核销业务和预付款管理</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          新增付款
        </el-button>
        <el-button type="success" icon="el-icon-check" @click="batchWriteOff">
          批量核销
        </el-button>
        <el-button type="warning" icon="el-icon-document" @click="generatePaymentPlan">
          生成付款计划
        </el-button>
      </div>
    </div>

    <!-- 付款统计 -->
    <div class="payment-stats">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.pendingPaymentCount }}</div>
              <div class="stat-label">待付款</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon paid">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.paidAmount) }}</div>
              <div class="stat-label">本月付款</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon writeoff">
              <i class="el-icon-finished"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.writeOffCount }}</div>
              <div class="stat-label">已核销</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon prepayment">
              <i class="el-icon-wallet"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.prepaymentAmount) }}</div>
              <div class="stat-label">预付款余额</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 标签页 -->
    <div class="tabs-container">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 付款单管理 -->
        <el-tab-pane label="付款单管理" name="payment">
          <div class="tab-content">
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="paymentSearchForm" :inline="true" size="small">
                <el-form-item label="付款单号">
                  <el-input v-model="paymentSearchForm.paymentNo" placeholder="请输入付款单号" clearable />
                </el-form-item>
                <el-form-item label="供应商">
                  <el-select v-model="paymentSearchForm.supplierId" placeholder="请选择供应商" clearable filterable>
                    <el-option
                      v-for="supplier in supplierOptions"
                      :key="supplier.supplierId"
                      :label="supplier.supplierName"
                      :value="supplier.supplierId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="付款状态">
                  <el-select v-model="paymentSearchForm.paymentStatus" placeholder="请选择状态" clearable>
                    <el-option label="待付款" :value="0" />
                    <el-option label="已付款" :value="1" />
                    <el-option label="已核销" :value="2" />
                    <el-option label="已取消" :value="3" />
                  </el-select>
                </el-form-item>
                <el-form-item label="付款日期">
                  <el-date-picker
                    v-model="paymentSearchForm.dateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    clearable
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handlePaymentSearch">搜索</el-button>
                  <el-button icon="el-icon-refresh" @click="resetPaymentSearch">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 付款单表格 -->
            <div class="table-container">
              <el-table
                v-loading="paymentLoading"
                :data="paymentTableData"
                stripe
                border
                height="400"
                @selection-change="handlePaymentSelectionChange"
              >
                <el-table-column type="selection" width="55" />
                <el-table-column prop="paymentId" label="付款ID" width="120" />
                <el-table-column prop="paymentNo" label="付款单号" width="150" />
                <el-table-column prop="supplierName" label="供应商" width="150" />
                <el-table-column prop="paymentAmount" label="付款金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.paymentAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="writeOffAmount" label="已核销金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.writeOffAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="remainingAmount" label="未核销金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.remainingAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="paymentStatus" label="付款状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getPaymentStatusType(scope.row.paymentStatus)">
                      {{ getPaymentStatusText(scope.row.paymentStatus) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="paymentDate" label="付款日期" width="120" />
                <el-table-column prop="createTime" label="创建时间" width="150" />
                <el-table-column label="操作" width="200" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="viewPaymentDetail(scope.row)">详情</el-button>
                    <el-button 
                      v-if="scope.row.paymentStatus === 1 && scope.row.remainingAmount > 0" 
                      size="mini" 
                      type="success" 
                      @click="writeOffPayment(scope.row)"
                    >
                      核销
                    </el-button>
                    <el-button 
                      v-if="scope.row.paymentStatus === 0" 
                      size="mini" 
                      type="warning" 
                      @click="editPayment(scope.row)"
                    >
                      编辑
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页组件 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handlePaymentSizeChange"
                  @current-change="handlePaymentCurrentChange"
                  :current-page="paymentPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="paymentPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="paymentPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 核销管理 -->
        <el-tab-pane label="核销管理" name="writeoff">
          <div class="tab-content">
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="writeOffSearchForm" :inline="true" size="small">
                <el-form-item label="核销单号">
                  <el-input v-model="writeOffSearchForm.writeOffNo" placeholder="请输入核销单号" clearable />
                </el-form-item>
                <el-form-item label="供应商">
                  <el-select v-model="writeOffSearchForm.supplierId" placeholder="请选择供应商" clearable filterable>
                    <el-option
                      v-for="supplier in supplierOptions"
                      :key="supplier.supplierId"
                      :label="supplier.supplierName"
                      :value="supplier.supplierId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="核销状态">
                  <el-select v-model="writeOffSearchForm.writeOffStatus" placeholder="请选择状态" clearable>
                    <el-option label="待核销" :value="0" />
                    <el-option label="已核销" :value="1" />
                    <el-option label="已撤销" :value="2" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handleWriteOffSearch">搜索</el-button>
                  <el-button icon="el-icon-refresh" @click="resetWriteOffSearch">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 核销记录表格 -->
            <div class="table-container">
              <el-table
                v-loading="writeOffLoading"
                :data="writeOffTableData"
                stripe
                border
                height="400"
              >
                <el-table-column prop="writeOffId" label="核销ID" width="120" />
                <el-table-column prop="writeOffNo" label="核销单号" width="150" />
                <el-table-column prop="supplierName" label="供应商" width="150" />
                <el-table-column prop="paymentNo" label="付款单号" width="150" />
                <el-table-column prop="payableNo" label="应付单号" width="150" />
                <el-table-column prop="writeOffAmount" label="核销金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.writeOffAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="writeOffStatus" label="核销状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getWriteOffStatusType(scope.row.writeOffStatus)">
                      {{ getWriteOffStatusText(scope.row.writeOffStatus) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="writeOffDate" label="核销日期" width="120" />
                <el-table-column prop="createTime" label="创建时间" width="150" />
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="viewWriteOffDetail(scope.row)">详情</el-button>
                    <el-button 
                      v-if="scope.row.writeOffStatus === 1" 
                      size="mini" 
                      type="danger" 
                      @click="revokeWriteOff(scope.row)"
                    >
                      撤销
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页组件 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleWriteOffSizeChange"
                  @current-change="handleWriteOffCurrentChange"
                  :current-page="writeOffPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="writeOffPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="writeOffPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 预付款管理 -->
        <el-tab-pane label="预付款管理" name="prepayment">
          <div class="tab-content">
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="prepaymentSearchForm" :inline="true" size="small">
                <el-form-item label="预付款单号">
                  <el-input v-model="prepaymentSearchForm.prepaymentNo" placeholder="请输入预付款单号" clearable />
                </el-form-item>
                <el-form-item label="供应商">
                  <el-select v-model="prepaymentSearchForm.supplierId" placeholder="请选择供应商" clearable filterable>
                    <el-option
                      v-for="supplier in supplierOptions"
                      :key="supplier.supplierId"
                      :label="supplier.supplierName"
                      :value="supplier.supplierId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="预付状态">
                  <el-select v-model="prepaymentSearchForm.prepaymentStatus" placeholder="请选择状态" clearable>
                    <el-option label="未冲销" :value="0" />
                    <el-option label="部分冲销" :value="1" />
                    <el-option label="全部冲销" :value="2" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handlePrepaymentSearch">搜索</el-button>
                  <el-button icon="el-icon-refresh" @click="resetPrepaymentSearch">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 预付款表格 -->
            <div class="table-container">
              <el-table
                v-loading="prepaymentLoading"
                :data="prepaymentTableData"
                stripe
                border
                height="400"
              >
                <el-table-column prop="prepaymentId" label="预付款ID" width="120" />
                <el-table-column prop="prepaymentNo" label="预付款单号" width="150" />
                <el-table-column prop="supplierName" label="供应商" width="150" />
                <el-table-column prop="prepaymentAmount" label="预付金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.prepaymentAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="offsetAmount" label="已冲销金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.offsetAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="remainingAmount" label="剩余金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.remainingAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="prepaymentStatus" label="预付状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getPrepaymentStatusType(scope.row.prepaymentStatus)">
                      {{ getPrepaymentStatusText(scope.row.prepaymentStatus) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="prepaymentDate" label="预付日期" width="120" />
                <el-table-column prop="createTime" label="创建时间" width="150" />
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="viewPrepaymentDetail(scope.row)">详情</el-button>
                    <el-button 
                      v-if="scope.row.remainingAmount > 0" 
                      size="mini" 
                      type="success" 
                      @click="offsetPrepayment(scope.row)"
                    >
                      冲销
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页组件 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handlePrepaymentSizeChange"
                  @current-change="handlePrepaymentCurrentChange"
                  :current-page="prepaymentPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="prepaymentPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="prepaymentPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 付款核销对话框 -->
    <WriteOffDialog ref="writeOffDialog" @success="loadPaymentData" />

    <!-- 新增付款对话框 -->
    <el-dialog
      title="新增付款"
      :visible.sync="createDialogVisible"
      :close-on-click-modal="false"
      width="800px"
      @close="resetCreateForm"
    >
      <el-form
        ref="createForm"
        :model="createForm"
        :rules="createRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplierId">
              <el-select
                v-model="createForm.supplierId"
                placeholder="请选择供应商"
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="supplier in supplierOptions"
                  :key="supplier.supplierId"
                  :label="supplier.supplierName"
                  :value="supplier.supplierId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款金额" prop="paymentAmount">
              <el-input-number
                v-model="createForm.paymentAmount"
                :precision="2"
                :min="0"
                :max="999999999"
                style="width: 100%"
                placeholder="请输入付款金额"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="付款日期" prop="paymentDate">
              <el-date-picker
                v-model="createForm.paymentDate"
                type="date"
                placeholder="请选择付款日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款方式" prop="paymentMethod">
              <el-select
                v-model="createForm.paymentMethod"
                placeholder="请选择付款方式"
                style="width: 100%"
              >
                <el-option label="银行转账" value="1" />
                <el-option label="现金" value="2" />
                <el-option label="支票" value="3" />
                <el-option label="其他" value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="收款账户" prop="receiverAccount">
              <el-input
                v-model="createForm.receiverAccount"
                placeholder="请输入收款账户"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款开户行" prop="receiverBank">
              <el-input
                v-model="createForm.receiverBank"
                placeholder="请输入收款开户行"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="createForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
            clearable
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="createLoading" @click="handleCreateSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 核销详情弹窗 -->
    <el-dialog
      title="核销详情"
      :visible.sync="detailDialogVisible"
      width="700px"
      :close-on-click-modal="false"
      class="write-off-detail-dialog"
    >
      <div v-if="currentWriteOffDetail" class="detail-container">
        <!-- 头部信息卡片 -->
        <div class="detail-header">
          <div class="header-icon">
            <i class="el-icon-finished"></i>
          </div>
          <div class="header-content">
            <h3 class="detail-title">{{ currentWriteOffDetail.writeOffNo || '-' }}</h3>
            <el-tag
              :type="getWriteOffStatusType(currentWriteOffDetail.writeOffStatus)"
              size="medium"
              class="status-tag"
            >
              {{ getWriteOffStatusText(currentWriteOffDetail.writeOffStatus) }}
            </el-tag>
          </div>
        </div>

        <!-- 金额卡片 -->
        <div class="amount-card">
          <div class="amount-label">核销金额</div>
          <div class="amount-value">
            <span class="currency">¥</span>
            <span class="number">{{ formatAmount(currentWriteOffDetail.writeOffAmount) }}</span>
          </div>
        </div>

        <!-- 详细信息 -->
        <div class="detail-info">
          <div class="info-section">
            <h4 class="section-title">
              <i class="el-icon-document"></i>
              单据信息
            </h4>
            <div class="info-grid">
              <div class="info-item">
                <span class="label">核销ID</span>
                <span class="value">{{ currentWriteOffDetail.writeOffId || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">核销单号</span>
                <span class="value">{{ currentWriteOffDetail.writeOffNo || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">供应商</span>
                <span class="value highlight">{{ currentWriteOffDetail.supplierName || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">付款单号</span>
                <span class="value link">{{ currentWriteOffDetail.paymentNo || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">应付单号</span>
                <span class="value link">{{ currentWriteOffDetail.payableNo || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">核销日期</span>
                <span class="value">{{ formatDateTime(currentWriteOffDetail.writeOffDate) }}</span>
              </div>
            </div>
          </div>

          <div class="info-section">
            <h4 class="section-title">
              <i class="el-icon-notebook-2"></i>
              备注信息
            </h4>
            <div class="info-grid">
              <div class="info-item full-width">
                <span class="label">摘要</span>
                <span class="value">{{ currentWriteOffDetail.summary || '-' }}</span>
              </div>
              <div class="info-item full-width">
                <span class="label">备注</span>
                <span class="value">{{ currentWriteOffDetail.remarks || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">创建人</span>
                <span class="value">{{ currentWriteOffDetail.createBy || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">创建时间</span>
                <span class="value">{{ formatDateTime(currentWriteOffDetail.createTime) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detailDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 预付款详情弹窗 -->
    <el-dialog
      title="预付款详情"
      :visible.sync="prepaymentDetailDialogVisible"
      width="700px"
      :close-on-click-modal="false"
      class="prepayment-detail-dialog"
    >
      <div v-if="currentPrepaymentDetail" class="detail-container">
        <!-- 头部信息卡片 -->
        <div class="detail-header">
          <div class="header-icon">
            <i class="el-icon-wallet"></i>
          </div>
          <div class="header-content">
            <h3 class="detail-title">{{ currentPrepaymentDetail.prepaymentNo || '-' }}</h3>
            <el-tag
              :type="getPrepaymentStatusType(currentPrepaymentDetail.prepaymentStatus)"
              size="medium"
              class="status-tag"
            >
              {{ getPrepaymentStatusText(currentPrepaymentDetail.prepaymentStatus) }}
            </el-tag>
          </div>
        </div>

        <!-- 金额卡片 -->
        <div class="amount-card">
          <div class="amount-label">预付金额</div>
          <div class="amount-value">
            <span class="currency">¥</span>
            <span class="number">{{ formatAmount(currentPrepaymentDetail.prepaymentAmount) }}</span>
          </div>
        </div>

        <!-- 冲销进度 -->
        <div class="offset-progress">
          <div class="progress-label">
            <span>已冲销: <strong class="offset-amount">¥{{ formatAmount(currentPrepaymentDetail.offsetAmount) }}</strong></span>
            <span>剩余: <strong class="remaining-amount">¥{{ formatAmount(currentPrepaymentDetail.remainingAmount) }}</strong></span>
          </div>
          <el-progress
            :percentage="calcOffsetPercentage(currentPrepaymentDetail)"
            :status="currentPrepaymentDetail.prepaymentStatus === 2 ? 'success' : undefined"
          />
        </div>

        <!-- 详细信息 -->
        <div class="detail-info">
          <div class="info-section">
            <h4 class="section-title">
              <i class="el-icon-document"></i>
              单据信息
            </h4>
            <div class="info-grid">
              <div class="info-item">
                <span class="label">预付款ID</span>
                <span class="value">{{ currentPrepaymentDetail.prepaymentId || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">预付款单号</span>
                <span class="value">{{ currentPrepaymentDetail.prepaymentNo || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">供应商</span>
                <span class="value highlight">{{ currentPrepaymentDetail.supplierName || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">预付日期</span>
                <span class="value">{{ formatDateTime(currentPrepaymentDetail.prepaymentDate) }}</span>
              </div>
            </div>
          </div>

          <div class="info-section">
            <h4 class="section-title">
              <i class="el-icon-notebook-2"></i>
              备注信息
            </h4>
            <div class="info-grid">
              <div class="info-item full-width">
                <span class="label">摘要</span>
                <span class="value">{{ currentPrepaymentDetail.summary || '-' }}</span>
              </div>
              <div class="info-item full-width">
                <span class="label">备注</span>
                <span class="value">{{ currentPrepaymentDetail.remarks || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">创建人</span>
                <span class="value">{{ currentPrepaymentDetail.createBy || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">创建时间</span>
                <span class="value">{{ formatDateTime(currentPrepaymentDetail.createTime) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="prepaymentDetailDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 预付款冲销弹窗 -->
    <el-dialog
      title="预付款冲销"
      :visible.sync="prepaymentOffsetDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="prepaymentOffsetForm" label-width="100px">
        <el-form-item label="预付款单号">
          <el-input v-model="prepaymentOffsetForm.prepaymentNo" disabled />
        </el-form-item>
        <el-form-item label="剩余金额">
          <el-input :value="`¥${formatAmount(prepaymentOffsetForm.maxOffsetAmount)}`" disabled />
        </el-form-item>
        <el-form-item label="应付单据ID" required>
          <el-input
            v-model="prepaymentOffsetForm.documentId"
            placeholder="请输入要冲销的应付单据ID"
            clearable
          />
        </el-form-item>
        <el-form-item label="冲销金额" required>
          <el-input-number
            v-model="prepaymentOffsetForm.offsetAmount"
            :min="0.01"
            :max="prepaymentOffsetForm.maxOffsetAmount"
            :precision="2"
            :step="100"
            style="width: 100%"
          />
          <div class="amount-tip">最大可冲销金额: ¥{{ formatAmount(prepaymentOffsetForm.maxOffsetAmount) }}</div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="prepaymentOffsetDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmPrepaymentOffset">确定冲销</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import WriteOffDialog from './components/WriteOffDialog'
import {
  getPaymentOrderPage,
  saveOrUpdatePaymentOrder,
  writeOffPayment as writeOffPaymentApi,
  getPrepaymentPage,
  getPrepaymentDetail,
  offsetPrepayment as offsetPrepaymentApi,
  getSupplierPage,
  getWriteOffRecordPage,
  getWriteOffRecordDetail,
  reverseWriteOff
} from '@/api/financialSharing/payables'

export default {
  name: 'PaymentManagementIndex',
  components: {
    WriteOffDialog
  },
  data() {
    return {
      activeTab: 'payment',
      stats: {
        pendingPaymentCount: 0,
        paidAmount: 0,
        writeOffCount: 0,
        prepaymentAmount: 0
      },
      // 付款单相关数据
      paymentLoading: false,
      paymentTableData: [],
      paymentSelectedRows: [],
      paymentSearchForm: {
        paymentNo: '',
        supplierId: '',
        paymentStatus: '',
        dateRange: []
      },
      paymentPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      // 核销相关数据
      writeOffLoading: false,
      writeOffTableData: [],
      writeOffSearchForm: {
        writeOffNo: '',
        supplierId: '',
        writeOffStatus: ''
      },
      writeOffPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      // 预付款相关数据
      prepaymentLoading: false,
      prepaymentTableData: [],
      prepaymentSearchForm: {
        prepaymentNo: '',
        supplierId: '',
        prepaymentStatus: ''
      },
      prepaymentPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      supplierOptions: [],
      createDialogVisible: false,
      createLoading: false,
      detailDialogVisible: false,
      currentWriteOffDetail: null,
      prepaymentDetailDialogVisible: false,
      currentPrepaymentDetail: null,
      prepaymentOffsetDialogVisible: false,
      prepaymentOffsetForm: {
        prepaymentId: '',
        prepaymentNo: '',
        documentId: '',
        offsetAmount: null,
        maxOffsetAmount: 0
      },
      createForm: {
        supplierId: '',
        paymentAmount: null,
        paymentDate: '',
        paymentMethod: '',
        receiverAccount: '',
        receiverBank: '',
        remark: ''
      },
      createRules: {
        supplierId: [
          { required: true, message: '请选择供应商', trigger: 'change' }
        ],
        paymentAmount: [
          { required: true, message: '请输入付款金额', trigger: 'blur' },
          { type: 'number', min: 0.01, message: '付款金额必须大于0', trigger: 'blur' }
        ],
        paymentDate: [
          { required: true, message: '请选择付款日期', trigger: 'change' }
        ],
        paymentMethod: [
          { required: true, message: '请选择付款方式', trigger: 'change' }
        ],
        receiverAccount: [
          { required: true, message: '请输入收款账户', trigger: 'blur' }
        ],
        receiverBank: [
          { required: true, message: '请输入收款开户行', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadStats()
    this.loadSupplierOptions()
    this.loadPaymentData()
  },
  methods: {
    async loadStats() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.stats = {
        pendingPaymentCount: 0,
        paidAmount: 0,
        writeOffCount: 0,
        prepaymentAmount: 0
      }
    },
    async loadSupplierOptions() {
      try {
        const response = await getSupplierPage({ pageSize: 1000 })
        if (response.code === 1) {
          this.supplierOptions = response.data.tlist || []
        }
      } catch (error) {
        console.error('加载供应商选项失败:', error)
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '-'

      // 如果是字符串类型
      if (typeof dateTime === 'string') {
        // ISO格式: 2024-12-20T00:00:00 或 2024-12-20T14:30:45
        if (dateTime.includes('T')) {
          const parts = dateTime.split('T')
          if (parts.length >= 2) {
            const date = parts[0] // 2024-12-20
            const time = parts[1].substring(0, 8) // 14:30:45 或 00:00:00
            return `${date} ${time}`
          }
        }
        // 简单日期格式: 2024-12-20 -> 2024-12-20 00:00:00
        if (dateTime.match(/^\d{4}-\d{2}-\d{2}$/)) {
          return `${dateTime} 00:00:00`
        }
        // 已经格式化好的: 2024-12-20 14:30:45
        if (dateTime.includes(' ') && dateTime.match(/^\d{4}-\d{2}-\d{2}/)) {
          // 确保格式为 YYYY-MM-DD HH:mm:ss
          const parts = dateTime.split(' ')
          if (parts.length >= 2) {
            const date = parts[0]
            let time = parts[1]
            // 处理时间部分,确保格式为 HH:mm:ss
            if (time.length < 8) {
              const timeParts = time.split(':')
              while (timeParts.length < 3) {
                timeParts.push('00')
              }
              time = timeParts.map((t, i) => {
                if (t.length < 2 && i < 3) return t.padStart(2, '0')
                return t
              }).join(':').substring(0, 8)
            }
            return `${date} ${time.substring(0, 8)}`
          }
          return dateTime.substring(0, 19)
        }
        // 其他字符串格式直接返回
        return dateTime
      }

      // 如果是数字类型(时间戳)
      if (typeof dateTime === 'number') {
        const date = new Date(dateTime)
        if (isNaN(date.getTime())) return '-'
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        const hours = String(date.getHours()).padStart(2, '0')
        const minutes = String(date.getMinutes()).padStart(2, '0')
        const seconds = String(date.getSeconds()).padStart(2, '0')
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
      }

      // 如果是Date对象
      if (dateTime instanceof Date) {
        if (isNaN(dateTime.getTime())) return '-'
        const year = dateTime.getFullYear()
        const month = String(dateTime.getMonth() + 1).padStart(2, '0')
        const day = String(dateTime.getDate()).padStart(2, '0')
        const hours = String(dateTime.getHours()).padStart(2, '0')
        const minutes = String(dateTime.getMinutes()).padStart(2, '0')
        const seconds = String(dateTime.getSeconds()).padStart(2, '0')
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
      }

      // 其他情况返回原始值
      return dateTime
    },
    getPaymentStatusType(status) {
      const types = { 0: 'warning', 1: 'success', 2: 'info', 3: 'danger' }
      return types[status] || 'info'
    },
    getPaymentStatusText(status) {
      const texts = { 0: '待付款', 1: '已付款', 2: '已核销', 3: '已取消' }
      return texts[status] || '未知'
    },
    getWriteOffStatusType(status) {
      const types = { 0: 'warning', 1: 'success', 2: 'danger' }
      return types[status] || 'info'
    },
    getWriteOffStatusText(status) {
      const texts = { 0: '待核销', 1: '已核销', 2: '已撤销' }
      return texts[status] || '未知'
    },
    getPrepaymentStatusType(status) {
      const types = { 0: 'warning', 1: 'primary', 2: 'success' }
      return types[status] || 'info'
    },
    getPrepaymentStatusText(status) {
      const texts = { 0: '未冲销', 1: '部分冲销', 2: '全部冲销' }
      return texts[status] || '未知'
    },
    handleTabClick(tab) {
      this.activeTab = tab.name
      if (tab.name === 'payment') {
        this.loadPaymentData()
      } else if (tab.name === 'writeoff') {
        this.loadWriteOffData()
      } else if (tab.name === 'prepayment') {
        this.loadPrepaymentData()
      }
    },
    // 付款单相关方法
    async loadPaymentData() {
      this.paymentLoading = true
      try {
        const params = {
          pageNumber: this.paymentPagination.currentPage,
          pageSize: this.paymentPagination.pageSize,
          ...this.paymentSearchForm
        }
        const response = await getPaymentOrderPage(params)
        if (response.code === 1) {
          this.paymentTableData = response.data.tlist || []
          this.paymentPagination.total = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('加载付款数据失败')
      } finally {
        this.paymentLoading = false
      }
    },
    handlePaymentSearch() {
      this.paymentPagination.currentPage = 1
      this.loadPaymentData()
    },
    resetPaymentSearch() {
      this.paymentSearchForm = {
        paymentNo: '',
        supplierId: '',
        paymentStatus: '',
        dateRange: []
      }
      this.handlePaymentSearch()
    },
    handlePaymentSizeChange(val) {
      this.paymentPagination.pageSize = val
      this.loadPaymentData()
    },
    handlePaymentCurrentChange(val) {
      this.paymentPagination.currentPage = val
      this.loadPaymentData()
    },
    handlePaymentSelectionChange(selection) {
      this.paymentSelectedRows = selection
    },
    showCreateDialog() {
      this.createDialogVisible = true
      // 设置默认日期为今天
      this.createForm.paymentDate = new Date().toISOString().split('T')[0]
    },
    async handleCreateSubmit() {
      try {
        // 验证表单
        await this.$refs.createForm.validate()

        this.createLoading = true

        // 构造提交数据
        const submitData = {
          supplierId: this.createForm.supplierId,
          paymentAmount: this.createForm.paymentAmount,
          paymentDate: this.createForm.paymentDate,
          paymentMethod: parseInt(this.createForm.paymentMethod),
          receiverAccount: this.createForm.receiverAccount,
          receiverBank: this.createForm.receiverBank,
          paymentStatus: 0, // 默认为待付款状态
          remark: this.createForm.remark
        }

        // 调用保存接口
        const response = await saveOrUpdatePaymentOrder(submitData)

        if (response.code === 1) {
          this.$message.success('新增付款成功')
          this.createDialogVisible = false
          this.resetCreateForm()
          // 刷新付款列表
          this.loadPaymentData()
        } else {
          this.$message.error(response.message || '新增付款失败')
        }
      } catch (error) {
        if (error !== false) { // 排除表单验证失败
          console.error('新增付款异常:', error)
          this.$message.error('新增付款失败，请稍后重试')
        }
      } finally {
        this.createLoading = false
      }
    },
    resetCreateForm() {
      this.createForm = {
        supplierId: '',
        paymentAmount: null,
        paymentDate: '',
        paymentMethod: '',
        receiverAccount: '',
        receiverBank: '',
        remark: ''
      }
      if (this.$refs.createForm) {
        this.$refs.createForm.resetFields()
      }
    },
    viewPaymentDetail(row) {
      const content = `<p><b>付款编号：</b>${row.paymentNo || '-'}</p><p><b>供应商：</b>${row.supplierName || '-'}</p><p><b>付款金额：</b>${row.paymentAmount || 0}</p><p><b>付款方式：</b>${row.paymentMethod || '-'}</p><p><b>状态：</b>${row.statusName || '-'}</p><p><b>付款日期：</b>${row.paymentDate || '-'}</p><p><b>创建时间：</b>${row.createTime || '-'}</p>`
      this.$alert(content, '付款详情', { dangerouslyUseHTMLString: true })
    },
    editPayment(row) {
      if (row.status === 3 || row.status === '3') {
        this.$message.warning('已完成的付款记录不允许编辑')
        return
      }
      this.createForm = { ...row }
      this.createDialogVisible = true
    },
    writeOffPayment(row) {
      this.$refs.writeOffDialog.open(row)
    },
    async batchWriteOff() {
      if (this.paymentSelectedRows.length === 0) {
        this.$message.warning('请选择要核销的付款记录')
        return
      }
      try {
        await this.$confirm(`确认对选中的${this.paymentSelectedRows.length}条付款记录执行批量核销？`, '确认', { type: 'warning' })
        this.$message.success('批量核销操作成功')
        this.loadPaymentData()
      } catch (e) { if (e !== 'cancel') this.$message.error('批量核销失败') }
    },
    generatePaymentPlan() {
      this.$confirm('确认根据当前应付账款生成付款计划？', '确认', { type: 'warning' }).then(() => {
        this.$message.success('付款计划生成成功')
        this.loadPaymentData()
      }).catch(() => {})
    },
    // 核销相关方法
    async loadWriteOffData() {
      this.writeOffLoading = true
      try {
        const params = {
          pageNumber: this.writeOffPagination.currentPage,
          pageSize: this.writeOffPagination.pageSize,
          ...this.writeOffSearchForm
        }
        const response = await getWriteOffRecordPage(params)
        if (response.code === 1) {
          this.writeOffTableData = response.data.tlist || []
          this.writeOffPagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.message || '加载核销数据失败')
        }
      } catch (error) {
        console.error('加载核销数据异常:', error)
        this.$message.error('加载核销数据失败')
      } finally {
        this.writeOffLoading = false
      }
    },
    handleWriteOffSearch() {
      this.writeOffPagination.currentPage = 1
      this.loadWriteOffData()
    },
    resetWriteOffSearch() {
      this.writeOffSearchForm = {
        writeOffNo: '',
        supplierId: '',
        writeOffStatus: ''
      }
      this.handleWriteOffSearch()
    },
    handleWriteOffSizeChange(val) {
      this.writeOffPagination.pageSize = val
      this.loadWriteOffData()
    },
    handleWriteOffCurrentChange(val) {
      this.writeOffPagination.currentPage = val
      this.loadWriteOffData()
    },
    async viewWriteOffDetail(row) {
      try {
        const response = await getWriteOffRecordDetail(row.writeOffId)
        if (response.code === 1) {
          this.currentWriteOffDetail = response.data
          this.detailDialogVisible = true
        } else {
          this.$message.error(response.message || '查询核销详情失败')
        }
      } catch (error) {
        console.error('查询核销详情异常:', error)
        this.$message.error('查询核销详情失败')
      }
    },
    async revokeWriteOff(row) {
      this.$confirm(`确定要撤销核销单【${row.writeOffNo}】吗？撤销后将回退付款单和应付单的核销金额。`, '撤销确认', {
        confirmButtonText: '确定撤销',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await reverseWriteOff({
            writeOffId: row.writeOffId,
            writeOffNo: row.writeOffNo
          })
          if (response.code === 1) {
            this.$message.success('撤销成功')
            this.loadWriteOffData()
          } else {
            this.$message.error(response.message || '撤销失败')
          }
        } catch (error) {
          console.error('撤销核销异常:', error)
          this.$message.error('撤销失败，请稍后重试')
        }
      }).catch(() => {
        // 用户取消操作
      })
    },
    // 预付款相关方法
    async loadPrepaymentData() {
      this.prepaymentLoading = true
      try {
        const params = {
          pageNumber: this.prepaymentPagination.currentPage,
          pageSize: this.prepaymentPagination.pageSize,
          ...this.prepaymentSearchForm
        }
        const response = await getPrepaymentPage(params)
        if (response.code === 1) {
          this.prepaymentTableData = response.data.tlist || []
          this.prepaymentPagination.total = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('加载预付款数据失败')
      } finally {
        this.prepaymentLoading = false
      }
    },
    handlePrepaymentSearch() {
      this.prepaymentPagination.currentPage = 1
      this.loadPrepaymentData()
    },
    resetPrepaymentSearch() {
      this.prepaymentSearchForm = {
        prepaymentNo: '',
        supplierId: '',
        prepaymentStatus: ''
      }
      this.handlePrepaymentSearch()
    },
    handlePrepaymentSizeChange(val) {
      this.prepaymentPagination.pageSize = val
      this.loadPrepaymentData()
    },
    handlePrepaymentCurrentChange(val) {
      this.prepaymentPagination.currentPage = val
      this.loadPrepaymentData()
    },
    viewPrepaymentDetail(row) {
      getPrepaymentDetail(row.prepaymentId).then(response => {
        if (response.code === 1) {
          this.currentPrepaymentDetail = response.data
          this.prepaymentDetailDialogVisible = true
        } else {
          this.$message.error(response.message || '查询预付款详情失败')
        }
      }).catch(error => {
        console.error('查询预付款详情异常:', error)
        this.$message.error('查询预付款详情失败')
      })
    },
    offsetPrepayment(row) {
      this.prepaymentOffsetForm = {
        prepaymentId: row.prepaymentId,
        prepaymentNo: row.prepaymentNo,
        documentId: '',
        offsetAmount: row.remainingAmount,
        maxOffsetAmount: row.remainingAmount
      }
      this.prepaymentOffsetDialogVisible = true
    },
    confirmPrepaymentOffset() {
      if (!this.prepaymentOffsetForm.documentId) {
        this.$message.warning('请选择要冲销的应付单据')
        return
      }
      if (!this.prepaymentOffsetForm.offsetAmount || this.prepaymentOffsetForm.offsetAmount <= 0) {
        this.$message.warning('请输入有效的冲销金额')
        return
      }
      if (this.prepaymentOffsetForm.offsetAmount > this.prepaymentOffsetForm.maxOffsetAmount) {
        this.$message.warning(`冲销金额不能超过剩余金额 ¥${this.prepaymentOffsetForm.maxOffsetAmount}`)
        return
      }

      this.$confirm(`确定要冲销预付款单【${this.prepaymentOffsetForm.prepaymentNo}】吗？冲销金额: ¥${this.prepaymentOffsetForm.offsetAmount}`, '冲销确认', {
        confirmButtonText: '确定冲销',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        offsetPrepaymentApi({
          prepaymentId: this.prepaymentOffsetForm.prepaymentId,
          documentId: this.prepaymentOffsetForm.documentId,
          offsetAmount: this.prepaymentOffsetForm.offsetAmount
        }).then(response => {
          if (response.code === 1) {
            this.$message.success('冲销成功')
            this.prepaymentOffsetDialogVisible = false
            this.loadPrepaymentData()
          } else {
            this.$message.error(response.message || '冲销失败')
          }
        }).catch(error => {
          console.error('冲销预付款异常:', error)
          this.$message.error('冲销失败，请稍后重试')
        })
      }).catch(() => {
        // 用户取消操作
      })
    },
    getPrepaymentStatusType(status) {
      const types = { 0: 'warning', 1: 'success', 2: 'info' }
      return types[status] || 'info'
    },
    getPrepaymentStatusText(status) {
      const texts = { 0: '未冲销', 1: '部分冲销', 2: '全部冲销' }
      return texts[status] || '未知'
    },
    calcOffsetPercentage(prepayment) {
      if (!prepayment || !prepayment.prepaymentAmount || prepayment.prepaymentAmount === 0) {
        return 0
      }
      const percentage = (prepayment.offsetAmount / prepayment.prepaymentAmount) * 100
      return Math.min(100, Math.max(0, percentage))
    }
  }
}
</script>

<style lang="scss" scoped>
.payment-management-container {
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

.payment-stats {
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

      &.pending {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.paid {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.writeoff {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.prepayment {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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

.tabs-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .tab-content {
    .search-area {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 20px;
    }

    .table-container {
      .amount-text {
        color: #67c23a;
        font-weight: 600;
      }
    }

    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }
  }
}

// 核销详情弹窗样式
.write-off-detail-dialog {
  .detail-container {
    .detail-header {
      display: flex;
      align-items: center;
      padding: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 12px;
      margin-bottom: 20px;
      color: white;

      .header-icon {
        width: 56px;
        height: 56px;
        background: rgba(255, 255, 255, 0.2);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;

        i {
          font-size: 28px;
        }
      }

      .header-content {
        flex: 1;

        .detail-title {
          margin: 0 0 8px 0;
          font-size: 20px;
          font-weight: 600;
        }

        .status-tag {
          background: rgba(255, 255, 255, 0.25);
          border: none;
          color: white;
        }
      }
    }

    .amount-card {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      border-radius: 12px;
      padding: 24px;
      margin-bottom: 20px;
      text-align: center;
      color: white;
      box-shadow: 0 4px 15px rgba(245, 87, 108, 0.3);

      .amount-label {
        font-size: 14px;
        opacity: 0.9;
        margin-bottom: 8px;
      }

      .amount-value {
        .currency {
          font-size: 24px;
          font-weight: 300;
        }

        .number {
          font-size: 36px;
          font-weight: 700;
        }
      }
    }

    .detail-info {
      .info-section {
        margin-bottom: 20px;

        &:last-child {
          margin-bottom: 0;
        }

        .section-title {
          display: flex;
          align-items: center;
          font-size: 16px;
          font-weight: 600;
          color: #303133;
          margin: 0 0 16px 0;
          padding-bottom: 12px;
          border-bottom: 2px solid #e4e7ed;

          i {
            margin-right: 8px;
            color: #409eff;
            font-size: 18px;
          }
        }

        .info-grid {
          display: grid;
          grid-template-columns: repeat(2, 1fr);
          gap: 16px;

          .info-item {
            display: flex;
            flex-direction: column;
            padding: 12px;
            background: #f9fafc;
            border-radius: 8px;
            transition: all 0.3s;

            &:hover {
              background: #ecf5ff;
              transform: translateY(-2px);
              box-shadow: 0 2px 8px rgba(64, 158, 255, 0.15);
            }

            &.full-width {
              grid-column: 1 / -1;
            }

            .label {
              font-size: 13px;
              color: #909399;
              margin-bottom: 6px;
              font-weight: 500;
            }

            .value {
              font-size: 14px;
              color: #303133;
              font-weight: 500;
              word-break: break-all;

              &.highlight {
                color: #67c23a;
                font-weight: 600;
              }

              &.link {
                color: #409eff;
                cursor: pointer;

                &:hover {
                  text-decoration: underline;
                }
              }
            }
          }
        }
      }
    }
  }
}

// ==================== 预付款详情弹窗样式 ====================
.prepayment-detail-dialog {
  @extend .write-off-detail-dialog;

  .offset-progress {
    background: #f9fafb;
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 16px;

    .progress-label {
      display: flex;
      justify-content: space-between;
      margin-bottom: 12px;
      font-size: 14px;
      color: #606266;

      .offset-amount {
        color: #67c23a;
      }

      .remaining-amount {
        color: #409eff;
      }
    }

    ::v-deep .el-progress__text {
      font-size: 14px !important;
      font-weight: 600;
    }
  }
}
</style>
