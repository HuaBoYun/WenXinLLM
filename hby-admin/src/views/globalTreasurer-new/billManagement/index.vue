<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 商业汇票管理 -->
      <el-tab-pane label="商业汇票管理" name="commercialBill">
        <div class="commercial-bill-container">
          <!-- 查询条件 -->
          <el-form :model="billQuery" ref="billQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="票据号码" prop="instrumentNumber">
              <el-input
                v-model="billQuery.instrumentNumber"
                placeholder="请输入票据号码"
                clearable
                @keyup.enter.native="handleBillQuery"
              />
            </el-form-item>
            <el-form-item label="票据类型" prop="instrumentType">
              <el-select v-model="billQuery.instrumentType" placeholder="请选择票据类型" clearable>
                <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
                <el-option label="商业承兑汇票" value="COMMERCIAL_ACCEPTANCE" />
                <el-option label="电子汇票" value="ELECTRONIC_BILL" />
              </el-select>
            </el-form-item>
            <el-form-item label="票据状态" prop="instrumentStatus">
              <el-select v-model="billQuery.instrumentStatus" placeholder="请选择票据状态" clearable>
                <el-option label="草稿" value="DRAFT" />
                <el-option label="已开立" value="ISSUED" />
                <el-option label="已承兑" value="ACCEPTED" />
                <el-option label="已背书" value="ENDORSED" />
                <el-option label="已贴现" value="DISCOUNTED" />
                <el-option label="已到期" value="MATURED" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleBillQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetBillQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleBillAdd"
              >开立票据</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="billSingle"
                @click="handleBillUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-document"
                size="mini"
                :disabled="billSingle"
                @click="handleBillEndorse"
              >背书</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-money"
                size="mini"
                :disabled="billSingle"
                @click="handleBillDiscount"
              >贴现</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="billLoading" :data="billList" @selection-change="handleBillSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="票据号码" align="center" prop="instrumentNumber" />
            <el-table-column label="票据类型" align="center" prop="instrumentType">
              <template slot-scope="scope">
                <span>{{ formatBillType(scope.row.instrumentType) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="票面金额" align="center" prop="instrumentAmount">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.instrumentAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="出票人" align="center" prop="drawer" />
            <el-table-column label="收款人" align="center" prop="payee" />
            <el-table-column label="到期日" align="center" prop="maturityDate" width="100">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.maturityDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="票据状态" align="center" prop="instrumentStatus">
              <template slot-scope="scope">
                <el-tag :type="getBillStatusTagType(scope.row.instrumentStatus)">
                  {{ formatBillStatus(scope.row.instrumentStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleBillView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleBillUpdate(scope.row)"
                  v-if="scope.row.instrumentStatus === 'DRAFT'"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-document"
                  @click="handleBillEndorse(scope.row)"
                  v-if="['DRAFT', 'ISSUED', 'ACCEPTED'].includes(scope.row.instrumentStatus)"
                >背书</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            background
            class="pagination"
            :current-page="billQuery.pageNum"
            :layout="layout"
            :page-size="billQuery.pageSize"
            :total="billTotal"
            @current-change="handleBillCurrentChange"
            @size-change="handleBillSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- 银行承兑汇票 -->
      <el-tab-pane label="银行承兑汇票" name="bankAcceptance">
        <div class="bank-acceptance-container">
          <!-- 查询条件 -->
          <el-form :model="acceptanceQuery" ref="acceptanceQueryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
            <el-form-item label="票据编号" prop="acceptanceNumber">
              <el-input
                v-model="acceptanceQuery.acceptanceNumber"
                placeholder="请输入票据编号"
                clearable
                @keyup.enter.native="handleAcceptanceQuery"
              />
            </el-form-item>
            <el-form-item label="承兑银行" prop="acceptingBank">
              <el-input
                v-model="acceptanceQuery.acceptingBank"
                placeholder="请输入承兑银行"
                clearable
                @keyup.enter.native="handleAcceptanceQuery"
              />
            </el-form-item>
            <el-form-item label="状态" prop="acceptanceStatus">
              <el-select v-model="acceptanceQuery.acceptanceStatus" placeholder="请选择状态" clearable>
                <el-option label="草稿" value="DRAFT" />
                <el-option label="正常" value="NORMAL" />
                <el-option label="待审批" value="PENDING" />
                <el-option label="已审批" value="APPROVED" />
                <el-option label="已拒绝" value="REJECTED" />
                <el-option label="已开立" value="ISSUED" />
                <el-option label="已承兑" value="ACCEPTED" />
                <el-option label="已背书" value="ENDORSED" />
                <el-option label="已贴现" value="DISCOUNTED" />
                <el-option label="已到期" value="MATURED" />
                <el-option label="已取消" value="CANCELLED" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleAcceptanceQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetAcceptanceQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleAcceptanceAdd"
              >新增申请</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="acceptanceSingle"
                @click="handleAcceptanceUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-check"
                size="mini"
                :disabled="acceptanceSingle"
                @click="handleAcceptanceSubmit"
              >提交审批</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="acceptanceMultiple"
                @click="handleAcceptanceDelete"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-money"
                size="mini"
                :disabled="acceptanceSingle"
                @click="handleMarginManage"
              >保证金管理</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="acceptanceLoading" :data="acceptanceList" @selection-change="handleAcceptanceSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="申请编号" align="center" prop="acceptanceNumber" width="150" />
            <el-table-column label="票据编号" align="center" prop="acceptanceNumber" width="150" />
            <el-table-column label="承兑银行" align="center" prop="acceptingBank" show-overflow-tooltip />
            <el-table-column label="票面金额" align="center" prop="acceptanceAmount" width="120">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.acceptanceAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="保证金比例" align="center" prop="marginRate" width="100">
              <template slot-scope="scope">
                <span>{{ (scope.row.marginRate * 100).toFixed(2) }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="保证金金额" align="center" prop="marginAmount" width="120">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.marginAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="到期日" align="center" prop="maturityDate" width="100">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.maturityDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" align="center" prop="acceptanceStatus" width="100">
              <template slot-scope="scope">
                <el-tag :type="getAcceptanceStatusTagType(scope.row.acceptanceStatus)">
                  {{ formatAcceptanceStatus(scope.row.acceptanceStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleAcceptanceView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleAcceptanceUpdate(scope.row)"
                  v-if="['DRAFT', 'NORMAL', 'PENDING', '正常'].includes(scope.row.acceptanceStatus)"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-check"
                  @click="handleAcceptanceApprove(scope.row)"
                  v-if="['NORMAL', 'PENDING', '正常'].includes(scope.row.acceptanceStatus)"
                >审批</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleAcceptanceDelete(scope.row)"
                  v-if="['DRAFT'].includes(scope.row.acceptanceStatus)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            background
            class="pagination"
            :current-page="acceptanceQuery.pageNum"
            :layout="layout"
            :page-size="acceptanceQuery.pageSize"
            :total="acceptanceTotal"
            @current-change="handleAcceptanceCurrentChange"
            @size-change="handleAcceptanceSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- 信用证管理 -->
      <el-tab-pane label="信用证管理" name="letterOfCredit">
        <div class="letter-of-credit-container">
          <!-- 查询表单 -->
          <el-form :model="lcQuery" ref="lcQueryForm" :inline="true" v-show="showSearch" label-width="100px" class="mb8">
            <el-form-item label="信用证编号" prop="lcNumber">
              <el-input
                v-model="lcQuery.lcNumber"
                placeholder="请输入信用证编号"
                clearable
                size="small"
                @keyup.enter.native="handleLcQuery"
              />
            </el-form-item>
            <el-form-item label="信用证类型" prop="lcType">
              <el-select v-model="lcQuery.lcType" placeholder="请选择信用证类型" clearable size="small">
                <el-option label="即期信用证" value="SIGHT_LC" />
                <el-option label="远期信用证" value="USANCE_LC" />
                <el-option label="循环信用证" value="REVOLVING_LC" />
                <el-option label="可转让信用证" value="TRANSFERABLE_LC" />
                <el-option label="备用信用证" value="STANDBY_LC" />
              </el-select>
            </el-form-item>
            <el-form-item label="信用证状态" prop="lcStatus">
              <el-select v-model="lcQuery.lcStatus" placeholder="请选择信用证状态" clearable size="small">
                <el-option label="草稿" value="DRAFT" />
                <el-option label="已开立" value="ISSUED" />
                <el-option label="已通知" value="ADVISED" />
                <el-option label="已保兑" value="CONFIRMED" />
                <el-option label="已修改" value="AMENDED" />
                <el-option label="已使用" value="UTILIZED" />
                <el-option label="已到期" value="EXPIRED" />
                <el-option label="已取消" value="CANCELLED" />
              </el-select>
            </el-form-item>
            <el-form-item label="申请人" prop="applicant">
              <el-input
                v-model="lcQuery.applicant"
                placeholder="请输入申请人"
                clearable
                size="small"
                @keyup.enter.native="handleLcQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleLcQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetLcQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleLcAdd"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="lcSingle"
                @click="handleLcUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="lcMultiple"
                @click="handleLcDelete"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-upload"
                size="mini"
                :disabled="lcSingle"
                @click="handleLcIssue"
              >开立</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="lcLoading" :data="lcList" @selection-change="handleLcSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="信用证编号" align="center" prop="lcNumber" width="150" />
            <el-table-column label="信用证类型" align="center" prop="lcType" width="120">
              <template slot-scope="scope">
                <span>{{ formatLcType(scope.row.lcType) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="申请人" align="center" prop="applicant" show-overflow-tooltip />
            <el-table-column label="受益人" align="center" prop="beneficiary" show-overflow-tooltip />
            <el-table-column label="开证行" align="center" prop="issuingBank" show-overflow-tooltip />
            <el-table-column label="信用证金额" align="center" prop="lcAmount" width="120">
              <template slot-scope="scope">
                {{ scope.row.lcAmount ? scope.row.lcAmount.toLocaleString() : '-' }}
              </template>
            </el-table-column>
            <el-table-column label="到期日期" align="center" prop="expiryDate" width="100">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.expiryDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="信用证状态" align="center" prop="lcStatus" width="100">
              <template slot-scope="scope">
                <el-tag :type="getLcStatusTagType(scope.row.lcStatus)">
                  {{ formatLcStatus(scope.row.lcStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleLcView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleLcUpdate(scope.row)"
                  v-if="scope.row.lcStatus === 'DRAFT'"
                >编辑</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleLcDelete(scope.row)"
                  v-if="scope.row.lcStatus === 'DRAFT'"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="lcTotal>0"
            :total="lcTotal"
            :page.sync="lcQuery.pageNum"
            :limit.sync="lcQuery.pageSize"
            @pagination="getLcList"
            @current-change="handleLcCurrentChange"
            @size-change="handleLcSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- 保函管理 -->
      <el-tab-pane label="保函管理" name="guarantee">
        <div class="guarantee-container">
          <!-- 查询表单 -->
          <el-form :model="guaranteeQuery" ref="guaranteeQueryForm" :inline="true" v-show="showSearch" label-width="100px" class="mb8">
            <el-form-item label="保函编号" prop="guaranteeNo">
              <el-input
                v-model="guaranteeQuery.guaranteeNo"
                placeholder="请输入保函编号"
                clearable
                size="small"
                @keyup.enter.native="handleGuaranteeQuery"
              />
            </el-form-item>
            <el-form-item label="保函类型" prop="guaranteeType">
              <el-select v-model="guaranteeQuery.guaranteeType" placeholder="请选择保函类型" clearable size="small">
                <el-option label="投标保函" value="BID_BOND" />
                <el-option label="履约保函" value="PERFORMANCE_BOND" />
                <el-option label="预付款保函" value="ADVANCE_PAYMENT_BOND" />
                <el-option label="质保金保函" value="WARRANTY_BOND" />
                <el-option label="关税保函" value="CUSTOMS_BOND" />
              </el-select>
            </el-form-item>
            <el-form-item label="保函状态" prop="guaranteeStatus">
              <el-select v-model="guaranteeQuery.guaranteeStatus" placeholder="请选择保函状态" clearable size="small">
                <el-option label="草稿" value="DRAFT" />
                <el-option label="已开立" value="ISSUED" />
                <el-option label="已生效" value="EFFECTIVE" />
                <el-option label="已索赔" value="CLAIMED" />
                <el-option label="已解除" value="RELEASED" />
                <el-option label="已到期" value="EXPIRED" />
                <el-option label="已取消" value="CANCELLED" />
              </el-select>
            </el-form-item>
            <el-form-item label="申请人" prop="applicant">
              <el-input
                v-model="guaranteeQuery.applicant"
                placeholder="请输入申请人"
                clearable
                size="small"
                @keyup.enter.native="handleGuaranteeQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleGuaranteeQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetGuaranteeQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleGuaranteeAdd"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="guaranteeSingle"
                @click="handleGuaranteeUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="guaranteeMultiple"
                @click="handleGuaranteeDelete"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-upload"
                size="mini"
                :disabled="guaranteeSingle"
                @click="handleGuaranteeIssue"
              >开立</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="guaranteeLoading" :data="guaranteeList" @selection-change="handleGuaranteeSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="保函编号" align="center" prop="guaranteeNumber" width="150" />
            <el-table-column label="保函类型" align="center" prop="guaranteeType" width="120">
              <template slot-scope="scope">
                <span>{{ formatGuaranteeType(scope.row.guaranteeType) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="申请人" align="center" prop="applicant" show-overflow-tooltip />
            <el-table-column label="受益人" align="center" prop="beneficiary" show-overflow-tooltip />
            <el-table-column label="公司ID" align="center" prop="companyId" show-overflow-tooltip />
            <el-table-column label="保函金额" align="center" prop="guaranteeAmount" width="120">
              <template slot-scope="scope">
                {{ scope.row.guaranteeAmount ? scope.row.guaranteeAmount.toLocaleString() : '-' }}
              </template>
            </el-table-column>
            <el-table-column label="到期日期" align="center" prop="expiryDate" width="100">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.expiryDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="保函状态" align="center" prop="guaranteeStatus" width="100">
              <template slot-scope="scope">
                <el-tag :type="getGuaranteeStatusTagType(scope.row.guaranteeStatus)">
                  {{ formatGuaranteeStatus(scope.row.guaranteeStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleGuaranteeView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleGuaranteeUpdate(scope.row)"
                  v-if="scope.row.guaranteeStatus === 'DRAFT'"
                >编辑</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleGuaranteeDelete(scope.row)"
                  v-if="scope.row.guaranteeStatus === 'DRAFT'"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="guaranteeTotal>0"
            :total="guaranteeTotal"
            :page.sync="guaranteeQuery.pageNum"
            :limit.sync="guaranteeQuery.pageSize"
            @pagination="getGuaranteeList"
            @current-change="handleGuaranteeCurrentChange"
            @size-change="handleGuaranteeSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- 票据查询 -->
      <el-tab-pane label="票据查询" name="billQuery">
        <div class="bill-query-container">
          <!-- 查询表单 -->
          <el-form :model="queryForm" ref="queryForm" :inline="true" v-show="showSearch" label-width="100px" class="mb8">
            <el-form-item label="票据编号" prop="instrumentNo">
              <el-input
                v-model="queryForm.instrumentNo"
                placeholder="请输入票据编号"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="票据类型" prop="instrumentType">
              <el-select v-model="queryForm.instrumentType" placeholder="请选择票据类型" clearable size="small">
                <el-option label="商业承兑汇票" value="COMMERCIAL_BILL" />
                <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
                <el-option label="商业汇票" value="COMMERCIAL_DRAFT" />
                <el-option label="信用证" value="LETTER_OF_CREDIT" />
                <el-option label="保函" value="GUARANTEE" />
                <el-option label="本票" value="PROMISSORY_NOTE" />
              </el-select>
            </el-form-item>
            <el-form-item label="票据状态" prop="instrumentStatus">
              <el-select v-model="queryForm.instrumentStatus" placeholder="请选择票据状态" clearable size="small">
                <el-option label="有效" value="ACTIVE" />
                <el-option label="草稿" value="DRAFT" />
                <el-option label="已开立" value="ISSUED" />
                <el-option label="已承兑" value="ACCEPTED" />
                <el-option label="已背书" value="ENDORSED" />
                <el-option label="已贴现" value="DISCOUNTED" />
                <el-option label="已到期" value="MATURED" />
                <el-option label="已取消" value="CANCELLED" />
              </el-select>
            </el-form-item>
            <el-form-item label="出票人" prop="issuer">
              <el-input
                v-model="queryForm.issuer"
                placeholder="请输入出票人"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="收款人" prop="payee">
              <el-input
                v-model="queryForm.payee"
                placeholder="请输入收款人"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="queryForm.currencyCode" placeholder="请选择币种" clearable size="small">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
                <el-option label="港币" value="HKD" />
              </el-select>
            </el-form-item>
            <el-form-item label="到期日期" prop="maturityDateRange">
              <el-date-picker
                v-model="queryForm.maturityDateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="yyyy-MM-dd"
                size="small"
                style="width: 390px">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleExport"
              >导出</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-pie-chart"
                size="mini"
                @click="handleStatistics"
              >统计分析</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-bell"
                size="mini"
                @click="handleMaturityAlerts"
              >到期提醒</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="queryLoading" :data="queryList" @selection-change="handleQuerySelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="票据编号" align="center" prop="instrumentNo" width="150" show-overflow-tooltip />
            <el-table-column label="票据类型" align="center" prop="instrumentType" width="120">
              <template slot-scope="scope">
                <span>{{ formatInstrumentType(scope.row.instrumentType) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="票据状态" align="center" prop="instrumentStatus" width="100">
              <template slot-scope="scope">
                <el-tag :type="getInstrumentStatusTagType(scope.row.instrumentStatus)">
                  {{ formatInstrumentStatus(scope.row.instrumentStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="出票人" align="center" prop="issuer" show-overflow-tooltip />
            <el-table-column label="收款人" align="center" prop="payee" show-overflow-tooltip />
            <el-table-column label="票面金额" align="center" prop="faceAmount" width="120">
              <template slot-scope="scope">
                {{ scope.row.faceAmount ? scope.row.faceAmount.toLocaleString() : '-' }}
              </template>
            </el-table-column>
            <el-table-column label="币种" align="center" prop="currencyCode" width="80" />
            <el-table-column label="到期日期" align="center" prop="maturityDate" width="100">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.maturityDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleQueryView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-document"
                  @click="handleQueryHistory(scope.row)"
                >历史</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="queryTotal>0"
            :total="queryTotal"
            :page.sync="queryForm.pageNum"
            :limit.sync="queryForm.pageSize"
            @pagination="getQueryList"
            @current-change="handleQueryCurrentChange"
            @size-change="handleQuerySizeChange"
          />
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改票据对话框 -->
    <el-dialog :title="billTitle" :visible.sync="billOpen" width="800px" append-to-body>
      <el-form ref="billForm" :model="billForm" :rules="billRules" label-width="100px" :disabled="billViewMode">
        <el-row>
          <el-col :span="12">
            <el-form-item label="票据类型" prop="instrumentType">
              <el-select v-model="billForm.instrumentType" placeholder="请选择票据类型">
                <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
                <el-option label="商业承兑汇票" value="COMMERCIAL_ACCEPTANCE" />
                <el-option label="电子汇票" value="ELECTRONIC_BILL" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="票面金额" prop="instrumentAmount">
              <el-input v-model="billForm.instrumentAmount" placeholder="请输入票面金额">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="出票人" prop="drawer">
              <el-input v-model="billForm.drawer" placeholder="请输入出票人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款人" prop="payee">
              <el-input v-model="billForm.payee" placeholder="请输入收款人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="出票日期" prop="issueDate">
              <el-date-picker
                v-model="billForm.issueDate"
                type="date"
                placeholder="选择出票日期"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="maturityDate">
              <el-date-picker
                v-model="billForm.maturityDate"
                type="date"
                placeholder="选择到期日期"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="承兑人" prop="acceptor">
          <el-input v-model="billForm.acceptor" placeholder="请输入承兑人/承兑银行" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="billForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBillForm" v-if="!billViewMode">确 定</el-button>
        <el-button @click="cancelBill">{{ billViewMode ? '关 闭' : '取 消' }}</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改银行承兑汇票对话框 -->
    <el-dialog :title="acceptanceTitle" :visible.sync="acceptanceOpen" width="900px" append-to-body>
      <el-form ref="acceptanceForm" :model="acceptanceForm" :rules="acceptanceRules" label-width="120px" :disabled="acceptanceViewMode">
        <el-row>
          <el-col :span="12">
            <el-form-item label="票据编号" prop="acceptanceNumber">
              <el-input v-model="acceptanceForm.acceptanceNumber" placeholder="请输入票据编号，不填则自动生成" :disabled="!!acceptanceForm.acceptanceId" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="承兑银行" prop="acceptingBank">
              <el-input v-model="acceptanceForm.acceptingBank" placeholder="请输入承兑银行" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="票面金额" prop="acceptanceAmount">
              <el-input v-model="acceptanceForm.acceptanceAmount" placeholder="请输入票面金额">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currency">
              <el-select v-model="acceptanceForm.currency" placeholder="请选择币种">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="出票人" prop="drawer">
              <el-input v-model="acceptanceForm.drawer" placeholder="请输入出票人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款人" prop="payee">
              <el-input v-model="acceptanceForm.payee" placeholder="请输入收款人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="出票日期" prop="issueDate">
              <el-date-picker
                v-model="acceptanceForm.issueDate"
                type="date"
                placeholder="选择出票日期"
                value-format="yyyy-MM-dd"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="maturityDate">
              <el-date-picker
                v-model="acceptanceForm.maturityDate"
                type="date"
                placeholder="选择到期日期"
                value-format="yyyy-MM-dd"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="授信额度" prop="creditLine">
              <el-input v-model="acceptanceForm.creditLine" placeholder="请输入授信额度">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保证金比例" prop="marginRate">
              <el-input v-model="acceptanceForm.marginRate" placeholder="请输入保证金比例(如10表示10%)" @input="calculateMarginAmount">
                <template slot="append">%</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="保证金金额" prop="marginAmount">
              <el-input v-model="acceptanceForm.marginAmount" placeholder="自动计算" :disabled="true">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手续费率" prop="commissionRate">
              <el-input v-model="acceptanceForm.commissionRate" placeholder="请输入手续费率">
                <template slot="append">%</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="担保方式" prop="guaranteeType">
              <el-select v-model="acceptanceForm.guaranteeType" placeholder="请选择担保方式">
                <el-option label="现金保证金" value="CASH_MARGIN" />
                <el-option label="信用担保" value="CREDIT_GUARANTEE" />
                <el-option label="资产抵押" value="ASSET_MORTGAGE" />
                <el-option label="第三方担保" value="THIRD_PARTY_GUARANTEE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务用途" prop="businessPurpose">
              <el-input v-model="acceptanceForm.businessPurpose" placeholder="请输入业务用途" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="担保详情" prop="guaranteeDetails">
          <el-input v-model="acceptanceForm.guaranteeDetails" type="textarea" :rows="3" placeholder="请输入担保详情" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="acceptanceForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAcceptanceForm" v-if="!acceptanceViewMode">确 定</el-button>
        <el-button @click="cancelAcceptance">{{ acceptanceViewMode ? '关 闭' : '取 消' }}</el-button>
      </div>
    </el-dialog>

    <!-- 保证金管理对话框 -->
    <el-dialog title="保证金管理" :visible.sync="marginOpen" width="600px" append-to-body>
      <el-form ref="marginForm" :model="marginForm" :rules="marginRules" label-width="120px">
        <el-form-item label="申请编号">
          <el-input v-model="marginForm.applicationNo" :disabled="true" />
        </el-form-item>
        <el-form-item label="票面金额">
          <el-input v-model="marginForm.faceAmount" :disabled="true">
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="保证金比例">
          <el-input v-model="marginForm.marginRatio" :disabled="true">
            <template slot="append">%</template>
          </el-input>
        </el-form-item>
        <el-form-item label="应缴保证金">
          <el-input v-model="marginForm.marginAmount" :disabled="true">
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="保证金账户" prop="marginAccount">
          <el-input v-model="marginForm.marginAccount" placeholder="请输入保证金账户" />
        </el-form-item>
        <el-form-item label="操作类型" prop="operationType">
          <el-radio-group v-model="marginForm.operationType">
            <el-radio label="DEPOSIT">缴存</el-radio>
            <el-radio label="RELEASE">释放</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="marginForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMarginForm">确 定</el-button>
        <el-button @click="cancelMargin">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 票据详情对话框 -->
    <el-dialog title="票据详情" :visible.sync="queryDetailOpen" width="900px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="票据编号">{{ queryDetail.instrumentNo }}</el-descriptions-item>
        <el-descriptions-item label="票据类型">
          <span>{{ formatInstrumentType(queryDetail.instrumentType) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="票据状态">
          <el-tag :type="getInstrumentStatusTagType(queryDetail.instrumentStatus)">
            {{ formatInstrumentStatus(queryDetail.instrumentStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="票面金额">{{ queryDetail.faceAmount }} {{ queryDetail.currencyCode }}</el-descriptions-item>
        <el-descriptions-item label="出票人">{{ queryDetail.issuer }}</el-descriptions-item>
        <el-descriptions-item label="收款人">{{ queryDetail.payee }}</el-descriptions-item>
        <el-descriptions-item label="付款人">{{ queryDetail.drawee }}</el-descriptions-item>
        <el-descriptions-item label="承兑人">{{ queryDetail.acceptor }}</el-descriptions-item>
        <el-descriptions-item label="开立银行">{{ queryDetail.issuingBank }}</el-descriptions-item>
        <el-descriptions-item label="承兑银行">{{ queryDetail.acceptingBank }}</el-descriptions-item>
        <el-descriptions-item label="开立日期">{{ parseTime(queryDetail.issueDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ parseTime(queryDetail.maturityDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="业务用途" :span="2">{{ queryDetail.businessPurpose }}</el-descriptions-item>
        <el-descriptions-item label="票据条款" :span="2">{{ queryDetail.instrumentTerms }}</el-descriptions-item>
        <el-descriptions-item label="贸易背景" :span="2">{{ queryDetail.tradeBackground }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ queryDetail.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="queryDetailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 票据流转历史对话框 -->
    <el-dialog title="票据流转历史" :visible.sync="queryHistoryOpen" width="800px" append-to-body>
      <el-timeline>
        <el-timeline-item
          v-for="(item, index) in queryHistory"
          :key="index"
          :timestamp="item.operationTime"
          placement="top">
          <el-card>
            <h4>{{ item.operationType }}</h4>
            <p>操作人：{{ item.operatorName }}</p>
            <p>操作说明：{{ item.operationDesc }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <div slot="footer" class="dialog-footer">
        <el-button @click="queryHistoryOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 统计分析对话框 -->
    <el-dialog title="票据统计分析" :visible.sync="statisticsOpen" width="1000px" append-to-body>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="hover">
            <div slot="header">按类型统计</div>
            <div v-for="item in statistics.typeStats" :key="item.type" class="stat-item">
              <span>{{ item.typeName }}：</span>
              <span class="stat-value">{{ item.count }} 笔</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover">
            <div slot="header">按状态统计</div>
            <div v-for="item in statistics.statusStats" :key="item.status" class="stat-item">
              <span>{{ item.statusName }}：</span>
              <span class="stat-value">{{ item.count }} 笔</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <el-card shadow="hover">
            <div slot="header">按币种统计</div>
            <div v-for="item in statistics.currencyStats" :key="item.currency" class="stat-item">
              <span>{{ item.currency }}：</span>
              <span class="stat-value">{{ item.totalAmount ? item.totalAmount.toLocaleString() : 0 }}</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover">
            <div slot="header">汇总信息</div>
            <div class="stat-item">
              <span>票据总数：</span>
              <span class="stat-value">{{ statistics.summary.totalCount || 0 }} 笔</span>
            </div>
            <div class="stat-item">
              <span>总金额：</span>
              <span class="stat-value">{{ statistics.summary.totalAmount ? statistics.summary.totalAmount.toLocaleString() : 0 }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="statisticsOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 到期提醒对话框 -->
    <el-dialog title="票据到期提醒" :visible.sync="alertsOpen" width="900px" append-to-body>
      <el-alert
        :title="`未来${alertDays}天内到期的票据共${maturityAlerts.length}笔`"
        type="warning"
        :closable="false"
        style="margin-bottom: 20px;">
      </el-alert>
      <el-table :data="maturityAlerts" style="width: 100%">
        <el-table-column label="票据编号" prop="instrumentNo" width="150" />
        <el-table-column label="票据类型" prop="instrumentType" width="120">
          <template slot-scope="scope">
            <span>{{ formatInstrumentType(scope.row.instrumentType) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票面金额" prop="faceAmount" width="120">
          <template slot-scope="scope">
            {{ scope.row.faceAmount ? scope.row.faceAmount.toLocaleString() : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="到期日期" prop="maturityDate" width="110">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.maturityDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="剩余天数" prop="daysToMaturity" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.daysToMaturity <= 7 ? 'danger' : 'warning'">
              {{ scope.row.daysToMaturity }} 天
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="alertsOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改保函对话框 -->
    <el-dialog :title="guaranteeTitle" :visible.sync="guaranteeOpen" width="1000px" append-to-body>
      <el-form ref="guaranteeForm" :model="guaranteeForm" :rules="guaranteeRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="保函编号" prop="guaranteeNo">
              <el-input v-model="guaranteeForm.guaranteeNo" placeholder="自动生成" :disabled="!!guaranteeForm.guaranteeId" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保函类型" prop="guaranteeType">
              <el-select v-model="guaranteeForm.guaranteeType" placeholder="请选择保函类型">
                <el-option label="投标保函" value="BID_BOND" />
                <el-option label="履约保函" value="PERFORMANCE_BOND" />
                <el-option label="预付款保函" value="ADVANCE_PAYMENT_BOND" />
                <el-option label="质保金保函" value="WARRANTY_BOND" />
                <el-option label="关税保函" value="CUSTOMS_BOND" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请人" prop="applicant">
              <el-input v-model="guaranteeForm.applicant" placeholder="请输入申请人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="受益人" prop="beneficiary">
              <el-input v-model="guaranteeForm.beneficiary" placeholder="请输入受益人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开立银行" prop="issuingBank">
              <el-input v-model="guaranteeForm.issuingBank" placeholder="请输入开立银行" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保函金额" prop="guaranteeAmount">
              <el-input v-model="guaranteeForm.guaranteeAmount" placeholder="请输入保函金额">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="guaranteeForm.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                value-format="yyyy-MM-dd"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="expiryDate">
              <el-date-picker
                v-model="guaranteeForm.expiryDate"
                type="date"
                placeholder="选择到期日期"
                value-format="yyyy-MM-dd"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="索赔期限" prop="claimPeriod">
              <el-input v-model="guaranteeForm.claimPeriod" placeholder="请输入索赔期限">
                <template slot="append">天</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="基础合同" prop="underlyingContract">
              <el-input v-model="guaranteeForm.underlyingContract" placeholder="请输入基础合同" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="合同金额" prop="contractAmount">
              <el-input v-model="guaranteeForm.contractAmount" placeholder="请输入合同金额">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保证金比例" prop="marginRatio">
              <el-input v-model="guaranteeForm.marginRatio" placeholder="请输入保证金比例">
                <template slot="append">%</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="保证金金额" prop="marginAmount">
              <el-input v-model="guaranteeForm.marginAmount" placeholder="自动计算" :disabled="true">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手续费率" prop="commissionRate">
              <el-input v-model="guaranteeForm.commissionRate" placeholder="请输入手续费率">
                <template slot="append">%</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="保函条款" prop="guaranteeTerms">
          <el-input v-model="guaranteeForm.guaranteeTerms" type="textarea" :rows="3" placeholder="请输入保函条款" />
        </el-form-item>
        <el-form-item label="索赔条件" prop="claimConditions">
          <el-input v-model="guaranteeForm.claimConditions" type="textarea" :rows="3" placeholder="请输入索赔条件" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="guaranteeForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitGuaranteeForm">确 定</el-button>
        <el-button @click="cancelGuarantee">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改信用证对话框 -->
    <el-dialog :title="lcTitle" :visible.sync="lcOpen" width="1000px" append-to-body>
      <el-form ref="lcForm" :model="lcForm" :rules="lcRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="信用证编号" prop="lcNumber">
              <el-input v-model="lcForm.lcNumber" placeholder="自动生成" :disabled="!!lcForm.lcId" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="信用证类型" prop="lcType">
              <el-select v-model="lcForm.lcType" placeholder="请选择信用证类型">
                <el-option label="即期信用证" value="SIGHT_LC" />
                <el-option label="远期信用证" value="USANCE_LC" />
                <el-option label="循环信用证" value="REVOLVING_LC" />
                <el-option label="可转让信用证" value="TRANSFERABLE_LC" />
                <el-option label="备用信用证" value="STANDBY_LC" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请人" prop="applicant">
              <el-input v-model="lcForm.applicant" placeholder="请输入申请人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="受益人" prop="beneficiary">
              <el-input v-model="lcForm.beneficiary" placeholder="请输入受益人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开证行" prop="issuingBank">
              <el-input v-model="lcForm.issuingBank" placeholder="请输入开证行" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="通知行" prop="advisingBank">
              <el-input v-model="lcForm.advisingBank" placeholder="请输入通知行" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="信用证金额" prop="lcAmount">
              <el-input v-model="lcForm.lcAmount" placeholder="请输入信用证金额">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="lcForm.currencyCode" placeholder="请选择币种">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="装运日期" prop="shipmentDate">
              <el-date-picker
                v-model="lcForm.shipmentDate"
                type="date"
                placeholder="选择装运日期"
                value-format="yyyy-MM-dd"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="expiryDate">
              <el-date-picker
                v-model="lcForm.expiryDate"
                type="date"
                placeholder="选择到期日期"
                value-format="yyyy-MM-dd"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="到期地点" prop="expiryPlace">
              <el-input v-model="lcForm.expiryPlace" placeholder="请输入到期地点" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="交单期限" prop="presentationPeriod">
              <el-input v-model="lcForm.presentationPeriod" placeholder="请输入交单期限">
                <template slot="append">天</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="溢短装条款" prop="tolerance">
              <el-input v-model="lcForm.tolerance" placeholder="请输入溢短装条款">
                <template slot="append">%</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分批装运" prop="partialShipment">
              <el-radio-group v-model="lcForm.partialShipment">
                <el-radio label="ALLOWED">允许</el-radio>
                <el-radio label="NOT_ALLOWED">不允许</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="转运" prop="transshipment">
              <el-radio-group v-model="lcForm.transshipment">
                <el-radio label="ALLOWED">允许</el-radio>
                <el-radio label="NOT_ALLOWED">不允许</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保证金金额" prop="marginAmount">
              <el-input v-model="lcForm.marginAmount" placeholder="请输入保证金金额">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="单据要求" prop="documentRequirements">
          <el-input v-model="lcForm.documentRequirements" type="textarea" :rows="3" placeholder="请输入单据要求" />
        </el-form-item>
        <el-form-item label="特殊条款" prop="specialConditions">
          <el-input v-model="lcForm.specialConditions" type="textarea" :rows="3" placeholder="请输入特殊条款" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="lcForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitLcForm">确 定</el-button>
        <el-button @click="cancelLc">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 票据背书对话框 -->
    <el-dialog title="票据背书" :visible.sync="endorseOpen" width="700px" append-to-body>
      <el-form ref="endorseForm" :model="endorseForm" :rules="endorseRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="票据号码">
              <el-input v-model="endorseForm.billNo" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="票面金额">
              <el-input v-model="endorseForm.billAmount" disabled>
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="背书人" prop="endorser">
              <el-input v-model="endorseForm.endorser" placeholder="请输入背书人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="被背书人" prop="endorsee">
              <el-input v-model="endorseForm.endorsee" placeholder="请输入被背书人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="背书日期" prop="endorseDate">
              <el-date-picker
                v-model="endorseForm.endorseDate"
                type="date"
                placeholder="选择背书日期"
                value-format="yyyy-MM-dd"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="背书类型" prop="endorseType">
              <el-select v-model="endorseForm.endorseType" placeholder="请选择背书类型" style="width: 100%">
                <el-option label="转让背书" value="TRANSFER" />
                <el-option label="委托收款背书" value="COLLECTION" />
                <el-option label="质押背书" value="PLEDGE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="背书原因" prop="endorseReason">
          <el-input v-model="endorseForm.endorseReason" type="textarea" :rows="3" placeholder="请输入背书原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEndorseForm">确 定</el-button>
        <el-button @click="cancelEndorse">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 票据贴现对话框 -->
    <el-dialog title="票据贴现" :visible.sync="discountOpen" width="700px" append-to-body>
      <el-form ref="discountForm" :model="discountForm" :rules="discountRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="票据号码">
              <el-input v-model="discountForm.billNo" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="票面金额">
              <el-input v-model="discountForm.billAmount" disabled>
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="到期日期">
              <el-input v-model="discountForm.maturityDate" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="贴现银行" prop="discountBank">
              <el-input v-model="discountForm.discountBank" placeholder="请输入贴现银行" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="贴现利率" prop="discountRate">
              <el-input v-model="discountForm.discountRate" placeholder="请输入贴现利率" @input="calculateDiscountAmount">
                <template slot="append">%</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="贴现天数" prop="discountDays">
              <el-input v-model="discountForm.discountDays" placeholder="自动计算" disabled>
                <template slot="append">天</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="贴现利息">
              <el-input v-model="discountForm.discountInterest" disabled>
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实付金额">
              <el-input v-model="discountForm.actualAmount" disabled>
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="贴现日期" prop="discountDate">
              <el-date-picker
                v-model="discountForm.discountDate"
                type="date"
                placeholder="选择贴现日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
                @change="calculateDiscountDays">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否追索" prop="isRecourse">
              <el-radio-group v-model="discountForm.isRecourse">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="贴现用途" prop="discountPurpose">
          <el-input v-model="discountForm.discountPurpose" type="textarea" :rows="2" placeholder="请输入贴现用途" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDiscountForm">确 定</el-button>
        <el-button @click="cancelDiscount">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getInstrumentPage,
  getInstrument,
  createInstrument,
  updateInstrument,
  deleteInstrument,
  getBankAcceptancePage,
  getBankAcceptance,
  createBankAcceptance,
  updateBankAcceptance,
  deleteBankAcceptance,
  submitAcceptanceApplication,
  approveAcceptanceApplication,
  manageMargin,
  getMarginInfo,
  getLetterOfCreditPage,
  getLetterOfCredit,
  createLetterOfCredit,
  updateLetterOfCredit,
  deleteLetterOfCredit,
  issueLetterOfCredit,
  getGuaranteePage,
  getGuarantee,
  createGuarantee,
  updateGuarantee,
  deleteGuarantee,
  issueGuarantee,
  getInstrumentLedger,
  getInstrumentStatistics,
  getMaturityAlerts,
  getInstrumentDetail,
  getInstrumentHistory,
  exportBillLedger,
  endorseBill,
  discountBill
} from "@/api/globalTreasurer/pzgl";
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'

export default {
  name: "InstrumentManagement",
  components: {
    Pagination
  },
  data() {
    return {
      // 当前激活的标签页
      activeTab: "commercialBill",
      // 显示搜索条件
      showSearch: true,
      // 分页布局
      layout: "total, sizes, prev, pager, next, jumper",

      // 票据相关数据
      billLoading: true,
      billIds: [],
      selectedBillRows: [],  // 保存选中的行数据
      billSingle: true,
      billMultiple: true,
      billTotal: 0,
      billList: [],
      billTitle: "",
      billOpen: false,
      billViewMode: false,
      billQuery: {
        pageNum: 1,
        pageSize: 10,
        instrumentNumber: null,
        instrumentType: null,
        instrumentStatus: null
      },
      billForm: {},
      billRules: {
        instrumentType: [
          { required: true, message: "票据类型不能为空", trigger: "change" }
        ],
        instrumentAmount: [
          { required: true, message: "票面金额不能为空", trigger: "blur" }
        ],
        drawer: [
          { required: true, message: "出票人不能为空", trigger: "blur" }
        ],
        payee: [
          { required: true, message: "收款人不能为空", trigger: "blur" }
        ],
        issueDate: [
          { required: true, message: "出票日期不能为空", trigger: "change" }
        ],
        maturityDate: [
          { required: true, message: "到期日期不能为空", trigger: "change" }
        ]
      },

      // 票据背书相关数据
      endorseOpen: false,
      endorseForm: {},
      endorseRules: {
        endorser: [
          { required: true, message: "背书人不能为空", trigger: "blur" }
        ],
        endorsee: [
          { required: true, message: "被背书人不能为空", trigger: "blur" }
        ],
        endorseDate: [
          { required: true, message: "背书日期不能为空", trigger: "change" }
        ],
        endorseType: [
          { required: true, message: "背书类型不能为空", trigger: "change" }
        ],
        endorseReason: [
          { required: true, message: "背书原因不能为空", trigger: "blur" }
        ]
      },

      // 票据贴现相关数据
      discountOpen: false,
      discountForm: {},
      discountRules: {
        discountBank: [
          { required: true, message: "贴现银行不能为空", trigger: "blur" }
        ],
        discountRate: [
          { required: true, message: "贴现利率不能为空", trigger: "blur" }
        ],
        discountDate: [
          { required: true, message: "贴现日期不能为空", trigger: "change" }
        ],
        isRecourse: [
          { required: true, message: "是否追索不能为空", trigger: "change" }
        ],
        discountPurpose: [
          { required: true, message: "贴现用途不能为空", trigger: "blur" }
        ]
      },

      // 银行承兑汇票相关数据
      acceptanceLoading: true,
      acceptanceIds: [],
      acceptanceSingle: true,
      acceptanceMultiple: true,
      acceptanceTotal: 0,
      acceptanceList: [],
      acceptanceTitle: "",
      acceptanceOpen: false,
      acceptanceViewMode: false,
      acceptanceQuery: {
        pageNum: 1,
        pageSize: 10,
        acceptanceNumber: null,
        acceptingBank: null,
        acceptanceStatus: null
      },
      acceptanceForm: {
        acceptanceId: null,
        acceptanceNumber: null,
        acceptingBank: null,
        acceptanceAmount: null,
        currency: 'CNY',
        drawer: null,
        payee: null,
        issueDate: null,
        maturityDate: null,
        creditLine: null,
        marginRate: 10,
        marginAmount: 0,
        commissionRate: null,
        guaranteeType: null,
        guaranteeDetails: null,
        businessPurpose: null,
        remark: null
      },
      acceptanceRules: {
        acceptingBank: [
          { required: true, message: "承兑银行不能为空", trigger: "blur" }
        ],
        acceptanceAmount: [
          { required: true, message: "票面金额不能为空", trigger: "blur" }
        ],
        currency: [
          { required: true, message: "币种不能为空", trigger: "change" }
        ],
        drawer: [
          { required: true, message: "出票人不能为空", trigger: "blur" }
        ],
        payee: [
          { required: true, message: "收款人不能为空", trigger: "blur" }
        ],
        issueDate: [
          { required: true, message: "出票日期不能为空", trigger: "change" }
        ],
        maturityDate: [
          { required: true, message: "到期日期不能为空", trigger: "change" }
        ],
        marginRate: [
          { required: true, message: "保证金比例不能为空", trigger: "blur" }
        ],
        guaranteeType: [
          { required: true, message: "担保方式不能为空", trigger: "change" }
        ]
      },

      // 保证金管理相关数据
      marginOpen: false,
      marginForm: {},
      marginRules: {
        marginAccount: [
          { required: true, message: "保证金账户不能为空", trigger: "blur" }
        ],
        operationType: [
          { required: true, message: "操作类型不能为空", trigger: "change" }
        ]
      },

      // 字典选项
      billTypeOptions: [
        { label: "银行承兑汇票", value: "BANK_ACCEPTANCE" },
        { label: "商业承兑汇票", value: "COMMERCIAL_ACCEPTANCE" },
        { label: "电子汇票", value: "ELECTRONIC_BILL" }
      ],
      billStatusOptions: [
        { label: "已开立", value: "ISSUED" },
        { label: "已承兑", value: "ACCEPTED" },
        { label: "已背书", value: "ENDORSED" },
        { label: "已贴现", value: "DISCOUNTED" },
        { label: "已到期", value: "MATURED" }
      ],
      acceptanceStatusOptions: [
        { label: "待审批", value: "PENDING" },
        { label: "已审批", value: "APPROVED" },
        { label: "已拒绝", value: "REJECTED" },
        { label: "已开立", value: "ISSUED" }
      ],

      // 信用证相关数据
      lcLoading: true,
      lcIds: [],
      lcSingle: true,
      lcMultiple: true,
      lcTotal: 0,
      lcList: [],
      lcTitle: "",
      lcOpen: false,
      lcQuery: {
        pageNum: 1,
        pageSize: 10,
        lcNumber: null,
        lcType: null,
        lcStatus: null,
        applicant: null
      },
      lcForm: {},
      lcRules: {
        lcType: [
          { required: true, message: "信用证类型不能为空", trigger: "change" }
        ],
        applicant: [
          { required: true, message: "申请人不能为空", trigger: "blur" }
        ],
        beneficiary: [
          { required: true, message: "受益人不能为空", trigger: "blur" }
        ],
        issuingBank: [
          { required: true, message: "开证行不能为空", trigger: "blur" }
        ],
        lcAmount: [
          { required: true, message: "信用证金额不能为空", trigger: "blur" }
        ],
        expiryDate: [
          { required: true, message: "到期日期不能为空", trigger: "change" }
        ]
      },
      lcTypeOptions: [
        { label: "即期信用证", value: "SIGHT_LC" },
        { label: "远期信用证", value: "USANCE_LC" },
        { label: "循环信用证", value: "REVOLVING_LC" },
        { label: "可转让信用证", value: "TRANSFERABLE_LC" },
        { label: "备用信用证", value: "STANDBY_LC" }
      ],
      lcStatusOptions: [
        { label: "草稿", value: "DRAFT" },
        { label: "已开立", value: "ISSUED" },
        { label: "已通知", value: "ADVISED" },
        { label: "已保兑", value: "CONFIRMED" },
        { label: "已修改", value: "AMENDED" },
        { label: "已使用", value: "UTILIZED" },
        { label: "已到期", value: "EXPIRED" },
        { label: "已取消", value: "CANCELLED" }
      ],

      // 保函相关数据
      guaranteeLoading: true,
      guaranteeIds: [],
      guaranteeSingle: true,
      guaranteeMultiple: true,
      guaranteeTotal: 0,
      guaranteeList: [],
      guaranteeTitle: "",
      guaranteeOpen: false,
      guaranteeQuery: {
        pageNum: 1,
        pageSize: 10,
        guaranteeNo: null,
        guaranteeType: null,
        guaranteeStatus: null,
        applicant: null
      },
      guaranteeForm: {},
      guaranteeRules: {
        guaranteeType: [
          { required: true, message: "保函类型不能为空", trigger: "change" }
        ],
        applicant: [
          { required: true, message: "申请人不能为空", trigger: "blur" }
        ],
        beneficiary: [
          { required: true, message: "受益人不能为空", trigger: "blur" }
        ],
        issuingBank: [
          { required: true, message: "开立银行不能为空", trigger: "blur" }
        ],
        guaranteeAmount: [
          { required: true, message: "保函金额不能为空", trigger: "blur" }
        ],
        expiryDate: [
          { required: true, message: "到期日期不能为空", trigger: "change" }
        ]
      },
      guaranteeTypeOptions: [
        { label: "投标保函", value: "BID_BOND" },
        { label: "履约保函", value: "PERFORMANCE_BOND" },
        { label: "预付款保函", value: "ADVANCE_PAYMENT_BOND" },
        { label: "质保金保函", value: "WARRANTY_BOND" },
        { label: "关税保函", value: "CUSTOMS_BOND" }
      ],
      guaranteeStatusOptions: [
        { label: "草稿", value: "DRAFT" },
        { label: "已开立", value: "ISSUED" },
        { label: "已生效", value: "EFFECTIVE" },
        { label: "已索赔", value: "CLAIMED" },
        { label: "已解除", value: "RELEASED" },
        { label: "已到期", value: "EXPIRED" },
        { label: "已取消", value: "CANCELLED" }
      ],

      // 票据查询相关数据
      queryLoading: true,
      queryIds: [],
      queryTotal: 0,
      queryList: [],
      queryForm: {
        pageNum: 1,
        pageSize: 10,
        instrumentNo: null,
        instrumentType: null,
        instrumentStatus: null,
        issuer: null,
        payee: null,
        currencyCode: null,
        maturityDateRange: null
      },
      queryDetailOpen: false,
      queryDetail: {},
      queryHistoryOpen: false,
      queryHistory: [],
      statisticsOpen: false,
      statistics: {
        typeStats: [],
        statusStats: [],
        currencyStats: [],
        summary: {}
      },
      alertsOpen: false,
      alertDays: 30,
      maturityAlerts: [],
      instrumentTypeOptions: [
        { label: "商业汇票", value: "COMMERCIAL_DRAFT" },
        { label: "银行承兑汇票", value: "BANK_ACCEPTANCE" },
        { label: "信用证", value: "LETTER_OF_CREDIT" },
        { label: "保函", value: "GUARANTEE" },
        { label: "本票", value: "PROMISSORY_NOTE" }
      ],
      instrumentStatusOptions: [
        { label: "草稿", value: "DRAFT" },
        { label: "已开立", value: "ISSUED" },
        { label: "已承兑", value: "ACCEPTED" },
        { label: "已背书", value: "ENDORSED" },
        { label: "已贴现", value: "DISCOUNTED" },
        { label: "已到期", value: "MATURED" },
        { label: "已取消", value: "CANCELLED" }
      ]
    };
  },
  created() {
    this.getBillList();
  },
  methods: {
    parseTime,
    
    /** 格式化金额 */
    formatAmount(amount) {
      if (!amount) return '0.00';
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }) + ' 元';
    },

    /** 格式化票据类型 */
    formatBillType(type) {
      const typeMap = {
        'BANK_ACCEPTANCE': '银行承兑汇票',
        'COMMERCIAL_ACCEPTANCE': '商业承兑汇票',
        'ELECTRONIC_BILL': '电子汇票'
      };
      return typeMap[type] || type || '-';
    },

    /** 格式化票据状态 */
    formatBillStatus(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'ISSUED': '已开立',
        'ACCEPTED': '已承兑',
        'ENDORSED': '已背书',
        'DISCOUNTED': '已贴现',
        'MATURED': '已到期'
      };
      return statusMap[status] || status || '-';
    },

    /** 获取票据状态标签类型 */
    getBillStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'ISSUED': '',
        'ACCEPTED': 'success',
        'ENDORSED': 'warning',
        'DISCOUNTED': 'info',
        'MATURED': 'danger'
      };
      return typeMap[status] || '';
    },

    /** 格式化银行承兑汇票状态 */
    formatAcceptanceStatus(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'NORMAL': '正常',
        'PENDING': '待审批',
        'APPROVED': '已审批',
        'REJECTED': '已拒绝',
        'ISSUED': '已开立',
        'ACCEPTED': '已承兑',
        'ENDORSED': '已背书',
        'DISCOUNTED': '已贴现',
        'MATURED': '已到期',
        'CANCELLED': '已取消',
        '正常': '正常',
        '已贴现': '已贴现',
        '已到期': '已到期'
      };
      return statusMap[status] || status || '-';
    },

    /** 获取银行承兑汇票状态标签类型 */
    getAcceptanceStatusTagType(status) {
      const typeMap = {
        'DRAFT': '',
        'NORMAL': 'success',
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'ISSUED': 'primary',
        'ACCEPTED': 'success',
        'ENDORSED': 'warning',
        'DISCOUNTED': '',
        'MATURED': 'info',
        'CANCELLED': 'info',
        '正常': 'success',
        '已贴现': '',
        '已到期': 'info'
      };
      return typeMap[status] || 'info';
    },

    /** 格式化信用证类型 */
    formatLcType(type) {
      const typeMap = {
        'SIGHT_LC': '即期信用证',
        'USANCE_LC': '远期信用证',
        'REVOLVING_LC': '循环信用证',
        'TRANSFERABLE_LC': '可转让信用证',
        'STANDBY_LC': '备用信用证'
      };
      return typeMap[type] || type || '-';
    },

    /** 格式化信用证状态 */
    formatLcStatus(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'ISSUED': '已开立',
        'ADVISED': '已通知',
        'CONFIRMED': '已保兑',
        'AMENDED': '已修改',
        'UTILIZED': '已使用',
        'EXPIRED': '已到期',
        'CANCELLED': '已取消'
      };
      return statusMap[status] || status || '-';
    },

    /** 获取信用证状态标签类型 */
    getLcStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'ISSUED': 'success',
        'ADVISED': '',
        'CONFIRMED': 'success',
        'AMENDED': 'warning',
        'UTILIZED': '',
        'EXPIRED': 'danger',
        'CANCELLED': 'danger'
      };
      return typeMap[status] || '';
    },

    /** 格式化保函类型 */
    formatGuaranteeType(type) {
      const typeMap = {
        'BID_GUARANTEE': '投标保函',
        'PERFORMANCE_GUARANTEE': '履约保函',
        'PAYMENT_GUARANTEE': '付款保函',
        'ADVANCE_PAYMENT_GUARANTEE': '预付款保函',
        'MAINTENANCE_GUARANTEE': '质量保函',
        'LOAN_GUARANTEE': '借款保函',
        // 数据库中实际使用的类型值
        'CREDIT': '信用担保',
        'BOND': '债券担保',
        'PROJECT': '项目担保',
        'BID_BOND': '投标保函',
        'PERFORMANCE_BOND': '履约保函',
        'ADVANCE_PAYMENT_BOND': '预付款保函',
        'WARRANTY_BOND': '质保金保函',
        'CUSTOMS_BOND': '关税保函'
      };
      return typeMap[type] || type || '-';
    },

    /** 格式化保函状态 */
    formatGuaranteeStatus(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'ISSUED': '已开立',
        'VALID': '有效',
        'EXPIRED': '已到期',
        'CANCELLED': '已取消',
        'CLAIMED': '已索赔',
        // 数据库中实际使用的状态值
        'ACTIVE': '生效中',
        'EFFECTIVE': '已生效',
        'RELEASED': '已解除'
      };
      return statusMap[status] || status || '-';
    },

    /** 获取保函状态标签类型 */
    getGuaranteeStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'ISSUED': 'success',
        'VALID': 'success',
        'EXPIRED': 'danger',
        'CANCELLED': 'danger',
        'CLAIMED': 'danger',
        // 数据库中实际使用的状态值
        'ACTIVE': 'success',
        'EFFECTIVE': 'success',
        'RELEASED': 'warning'
      };
      return typeMap[status] || '';
    },

    /** 格式化票据台账类型 */
    formatInstrumentType(type) {
      const typeMap = {
        'BANK_ACCEPTANCE': '银行承兑汇票',
        'COMMERCIAL_ACCEPTANCE': '商业承兑汇票',
        'COMMERCIAL_BILL': '商业承兑汇票',
        'COMMERCIAL_DRAFT': '商业汇票',
        'CHECK': '支票',
        'PROMISSORY_NOTE': '本票',
        'LETTER_OF_CREDIT': '信用证',
        'GUARANTEE': '保函'
      };
      return typeMap[type] || type || '-';
    },

    /** 格式化票据台账状态 */
    formatInstrumentStatus(status) {
      const statusMap = {
        'ACTIVE': '有效',
        'ISSUED': '已开立',
        'ACCEPTED': '已承兑',
        'ENDORSED': '已背书',
        'DISCOUNTED': '已贴现',
        'MATURED': '已到期',
        'CANCELLED': '已注销',
        'DRAFT': '草稿'
      };
      return statusMap[status] || status || '-';
    },

    /** 获取票据台账状态标签类型 */
    getInstrumentStatusTagType(status) {
      const typeMap = {
        'ACTIVE': 'success',
        'ISSUED': '',
        'ACCEPTED': 'success',
        'ENDORSED': 'warning',
        'DISCOUNTED': 'info',
        'MATURED': 'danger',
        'CANCELLED': 'danger',
        'DRAFT': 'info'
      };
      return typeMap[status] || '';
    },

    
    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'commercialBill') {
        this.getBillList();
      } else if (tab.name === 'bankAcceptance') {
        this.getAcceptanceList();
      } else if (tab.name === 'letterOfCredit') {
        this.getLcList();
      } else if (tab.name === 'guarantee') {
        this.getGuaranteeList();
      } else if (tab.name === 'billQuery') {
        this.getQueryList();
      }
    },

    /** 查询票据列表 */
    async getBillList() {
      this.billLoading = true;
      try {
        const response = await getInstrumentPage(this.billQuery);

        console.log('票据列表接口返回数据:', response);

        // 使用与配置文件一致的成功状态码
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && Array.isArray(response.data) && response.result) {
            // JsonBean格式：{ code: 1, data: [], result: { total: 10, ... } }
            this.billList = response.data || [];
            this.billTotal = response.result.total || response.data.length || 0;
          } else if (response.data && response.data.tlist !== undefined) {
            // PageInfo格式：{ code: 200, data: { tlist: [], totalRecord: 0 } }
            this.billList = response.data.tlist || [];
            this.billTotal = response.data.totalRecord || 0;
          } else if (response.data && response.data.list !== undefined) {
            // 标准格式：{ code: 200, data: { list: [], total: 0 } }
            this.billList = response.data.list || [];
            this.billTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            // 数组格式：{ code: 200, data: [] }
            this.billList = response.data || [];
            this.billTotal = response.data.length || 0;
          } else if (response.tlist !== undefined) {
            // 直接PageInfo格式：{ code: 200, tlist: [], totalRecord: 0 }
            this.billList = response.tlist || [];
            this.billTotal = response.totalRecord || 0;
          } else if (response.list !== undefined) {
            // 直接格式：{ code: 200, list: [], total: 0 }
            this.billList = response.list || [];
            this.billTotal = response.total || 0;
          } else {
            // 兜底处理
            this.billList = [];
            this.billTotal = 0;
          }

          console.log('解析后的票据列表:', this.billList);
        } else {
          this.$message.error(response.message || response.msg || '查询失败');
          this.billList = [];
          this.billTotal = 0;
        }
      } catch (error) {
        console.error('获取票据列表失败:', error);
        this.$message.error('获取数据失败：' + error.message);
        this.billList = [];
        this.billTotal = 0;
      }
      this.billLoading = false;
    },

    /** 搜索按钮操作 */
    handleBillQuery() {
      this.billQuery.pageNum = 1;
      this.getBillList();
    },

    /** 重置按钮操作 */
    resetBillQuery() {
      this.resetForm("billQueryForm");
      this.handleBillQuery();
    },

    /** 多选框选中数据 */
    handleBillSelectionChange(selection) {
      this.billIds = selection.map(item => item.instrumentId);
      this.billSingle = selection.length !== 1;
      this.billMultiple = !selection.length;
      // 保存选中的行数据，用于背书和贴现操作
      this.selectedBillRows = selection;
      console.log('选中的票据数据:', selection);
    },

    /** 新增按钮操作 */
    handleBillAdd() {
      this.billViewMode = false;
      this.billForm = {
        instrumentId: null,
        instrumentType: null,
        instrumentAmount: null,
        drawer: null,
        payee: null,
        issueDate: null,
        maturityDate: null,
        acceptor: null,
        remark: null
      };
      this.billTitle = "开立票据";
      this.billOpen = true;
    },

    /** 修改按钮操作 */
    handleBillUpdate(row) {
      this.billViewMode = false;
      this.resetBillForm();
      const instrumentId = row.instrumentId || this.billIds;
      getInstrument(instrumentId).then(response => {
        const data = response.data || {};
        this.billForm = {
          ...data,
          issueDate: data.issueDate ? this.parseTime(data.issueDate, '{y}-{m}-{d}') : null,
          maturityDate: data.maturityDate ? this.parseTime(data.maturityDate, '{y}-{m}-{d}') : null
        };
        this.billOpen = true;
        this.billTitle = "修改票据";
      });
    },

    /** 查看按钮操作 */
    handleBillView(row) {
      this.resetBillForm();
      this.billViewMode = true;
      const instrumentId = row.instrumentId || this.billIds;
      getInstrument(instrumentId).then(response => {
        const data = response.data || {};
        this.billForm = {
          ...data,
          issueDate: data.issueDate ? this.parseTime(data.issueDate, '{y}-{m}-{d}') : null,
          maturityDate: data.maturityDate ? this.parseTime(data.maturityDate, '{y}-{m}-{d}') : null
        };
        this.billOpen = true;
        this.billTitle = "查看票据详情";
      }).catch(error => {
        console.error('获取票据详情失败:', error);
        this.$message.error('获取票据详情失败：' + (error.message || '网络错误'));
      });
    },

    /** 背书按钮操作 */
    handleBillEndorse(row) {
      // 判断 row 是否是有效的行数据（不是事件对象）
      const isValidRow = row && row.instrumentId !== undefined && !(row instanceof Event) && !row.isTrusted;
      // 如果是从表格行操作按钮点击，直接使用 row
      // 如果是从工具栏按钮点击，使用保存的选中行数据
      const selectedRow = isValidRow ? row : (this.selectedBillRows.length === 1 ? this.selectedBillRows[0] : null);

      console.log('背书操作 - 选中的行数据:', selectedRow);

      if (!selectedRow) {
        this.$message.warning("请选择一条票据记录");
        return;
      }
      this.resetEndorseForm();
      this.endorseForm = {
        instrumentId: selectedRow.instrumentId,
        billNo: selectedRow.instrumentNumber,
        billAmount: selectedRow.instrumentAmount,
        endorser: '',
        endorsee: '',
        endorseDate: this.formatDate(new Date()),
        endorseType: null,
        endorseReason: ''
      };

      console.log('背书表单数据:', this.endorseForm);

      this.endorseOpen = true;
    },

    /** 贴现按钮操作 */
    handleBillDiscount(row) {
      // 判断 row 是否是有效的行数据（不是事件对象）
      const isValidRow = row && row.instrumentId !== undefined && !(row instanceof Event) && !row.isTrusted;
      // 如果是从表格行操作按钮点击，直接使用 row
      // 如果是从工具栏按钮点击，使用保存的选中行数据
      const selectedRow = isValidRow ? row : (this.selectedBillRows.length === 1 ? this.selectedBillRows[0] : null);

      console.log('贴现操作 - 选中的行数据:', selectedRow);

      if (!selectedRow) {
        this.$message.warning("请选择一条票据记录");
        return;
      }
      this.resetDiscountForm();
      this.discountForm = {
        instrumentId: selectedRow.instrumentId,
        billNo: selectedRow.instrumentNumber,
        billAmount: selectedRow.instrumentAmount,
        maturityDate: selectedRow.maturityDate ? this.parseTime(selectedRow.maturityDate, '{y}-{m}-{d}') : '',
        discountBank: '',
        discountRate: '',
        discountDate: this.formatDate(new Date()),
        discountDays: '',
        discountInterest: '',
        actualAmount: '',
        isRecourse: 1,
        discountPurpose: ''
      };

      console.log('贴现表单数据:', this.discountForm);

      // 计算贴现天数
      this.calculateDiscountDays();
      this.discountOpen = true;
    },

    /** 提交背书表单 */
    submitEndorseForm() {
      this.$refs["endorseForm"].validate(valid => {
        if (valid) {
          const data = {
            endorser: this.endorseForm.endorser,
            endorsee: this.endorseForm.endorsee,
            endorseDate: this.endorseForm.endorseDate,
            endorseType: this.endorseForm.endorseType,
            endorseReason: this.endorseForm.endorseReason
          };
          endorseBill(this.endorseForm.instrumentId, data).then(response => {
            this.$message.success("背书成功");
            this.endorseOpen = false;
            this.getBillList();
          }).catch(error => {
            this.$message.error("背书失败: " + (error.message || '未知错误'));
          });
        }
      });
    },

    /** 取消背书 */
    cancelEndorse() {
      this.endorseOpen = false;
      this.resetEndorseForm();
    },

    /** 重置背书表单 */
    resetEndorseForm() {
      this.endorseForm = {
        instrumentId: null,
        billNo: '',
        billAmount: '',
        endorser: '',
        endorsee: '',
        endorseDate: '',
        endorseType: null,
        endorseReason: ''
      };
      if (this.$refs["endorseForm"]) {
        this.$refs["endorseForm"].resetFields();
      }
    },

    /** 提交贴现表单 */
    submitDiscountForm() {
      this.$refs["discountForm"].validate(valid => {
        if (valid) {
          const data = {
            discountBank: this.discountForm.discountBank,
            discountRate: this.discountForm.discountRate,
            discountDate: this.discountForm.discountDate,
            discountDays: this.discountForm.discountDays,
            discountInterest: this.discountForm.discountInterest,
            actualAmount: this.discountForm.actualAmount,
            isRecourse: this.discountForm.isRecourse,
            discountPurpose: this.discountForm.discountPurpose
          };
          discountBill(this.discountForm.instrumentId, data).then(response => {
            this.$message.success("贴现成功");
            this.discountOpen = false;
            this.getBillList();
          }).catch(error => {
            this.$message.error("贴现失败: " + (error.message || '未知错误'));
          });
        }
      });
    },

    /** 取消贴现 */
    cancelDiscount() {
      this.discountOpen = false;
      this.resetDiscountForm();
    },

    /** 重置贴现表单 */
    resetDiscountForm() {
      this.discountForm = {
        instrumentId: null,
        billNo: '',
        billAmount: '',
        maturityDate: '',
        discountBank: '',
        discountRate: '',
        discountDate: '',
        discountDays: '',
        discountInterest: '',
        actualAmount: '',
        isRecourse: 1,
        discountPurpose: ''
      };
      if (this.$refs["discountForm"]) {
        this.$refs["discountForm"].resetFields();
      }
    },

    /** 计算贴现天数 */
    calculateDiscountDays() {
      if (this.discountForm.discountDate && this.discountForm.maturityDate) {
        const discountDate = new Date(this.discountForm.discountDate);
        const maturityDate = new Date(this.discountForm.maturityDate);
        const diffTime = maturityDate.getTime() - discountDate.getTime();
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        this.discountForm.discountDays = diffDays > 0 ? diffDays : 0;
        this.calculateDiscountAmount();
      }
    },

    /** 计算贴现利息和实付金额 */
    calculateDiscountAmount() {
      const billAmount = parseFloat(this.discountForm.billAmount) || 0;
      const discountRate = parseFloat(this.discountForm.discountRate) || 0;
      const discountDays = parseInt(this.discountForm.discountDays) || 0;

      if (billAmount > 0 && discountRate > 0 && discountDays > 0) {
        // 贴现利息 = 票面金额 × 贴现利率 × 贴现天数 / 360
        const interest = (billAmount * discountRate * discountDays) / (100 * 360);
        this.discountForm.discountInterest = interest.toFixed(2);
        // 实付金额 = 票面金额 - 贴现利息
        this.discountForm.actualAmount = (billAmount - interest).toFixed(2);
      }
    },

    /** 格式化日期 */
    formatDate(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },

    /** 提交按钮 */
    submitBillForm() {
      this.$refs["billForm"].validate(valid => {
        if (valid) {
          if (this.billForm.instrumentId != null) {
            updateInstrument(this.billForm).then(response => {
              this.$message.success("修改成功");
              this.billOpen = false;
              this.getBillList();
            });
          } else {
            createInstrument(this.billForm).then(response => {
              this.$message.success("新增成功");
              this.billOpen = false;
              this.getBillList();
            });
          }
        }
      });
    },

    /** 取消按钮 */
    cancelBill() {
      this.billOpen = false;
      this.resetBillForm();
    },

    /** 表单重置 */
    resetBillForm() {
      this.billForm = {
        instrumentId: null,
        instrumentType: null,
        instrumentAmount: null,
        drawer: null,
        payee: null,
        issueDate: null,
        maturityDate: null,
        acceptor: null,
        remark: null
      };
      // 重置表单验证
      if (this.$refs["billForm"]) {
        this.$refs["billForm"].resetFields();
      }
    },

    /** 分页相关方法 */
    handleBillCurrentChange(val) {
      this.billQuery.pageNum = val;
      this.getBillList();
    },

    handleBillSizeChange(val) {
      this.billQuery.pageSize = val;
      this.getBillList();
    },

    // ==================== 银行承兑汇票相关方法 ====================

    /** 查询银行承兑汇票列表 */
    async getAcceptanceList() {
      this.acceptanceLoading = true;
      try {
        const response = await getBankAcceptancePage(this.acceptanceQuery);

        console.log('银行承兑汇票接口返回数据:', response);

        // 使用与配置文件一致的成功状态码
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && response.data.rows !== undefined) {
            // 后端返回 {code: 1, data: {total: 5, rows: [...]}}
            this.acceptanceList = response.data.rows || [];
            this.acceptanceTotal = response.data.total || 0;
          } else if (response.data && response.data.tlist !== undefined) {
            this.acceptanceList = response.data.tlist || [];
            this.acceptanceTotal = response.data.totalRecord || 0;
          } else if (response.data && response.data.list !== undefined) {
            this.acceptanceList = response.data.list || [];
            this.acceptanceTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            this.acceptanceList = response.data || [];
            this.acceptanceTotal = response.data.length || 0;
          } else if (response.tlist !== undefined) {
            this.acceptanceList = response.tlist || [];
            this.acceptanceTotal = response.totalRecord || 0;
          } else if (response.list !== undefined) {
            this.acceptanceList = response.list || [];
            this.acceptanceTotal = response.total || 0;
          } else {
            this.acceptanceList = [];
            this.acceptanceTotal = 0;
          }

          console.log('处理后的列表数据:', this.acceptanceList);
          console.log('处理后的总数:', this.acceptanceTotal);
        } else {
          this.$message.error(response.message || response.msg || '查询失败');
          this.acceptanceList = [];
          this.acceptanceTotal = 0;
        }
      } catch (error) {
        console.error('获取银行承兑汇票列表失败:', error);
        this.$message.error('获取数据失败：' + (error.message || '网络错误'));
        this.acceptanceList = [];
        this.acceptanceTotal = 0;
      }
      this.acceptanceLoading = false;
    },

    /** 搜索按钮操作 */
    handleAcceptanceQuery() {
      this.acceptanceQuery.pageNum = 1;
      this.getAcceptanceList();
    },

    /** 重置按钮操作 */
    resetAcceptanceQuery() {
      this.resetForm("acceptanceQueryForm");
      this.handleAcceptanceQuery();
    },

    /** 多选框选中数据 */
    handleAcceptanceSelectionChange(selection) {
      this.acceptanceIds = selection.map(item => item.acceptanceId);
      this.acceptanceSingle = selection.length !== 1;
      this.acceptanceMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAcceptanceAdd() {
      this.resetAcceptanceForm();
      this.acceptanceViewMode = false;
      this.acceptanceOpen = true;
      this.acceptanceTitle = "新增银行承兑汇票申请";
    },

    /** 修改按钮操作 */
    async handleAcceptanceUpdate(row) {
      this.resetAcceptanceForm();
      this.acceptanceViewMode = false;
      const acceptanceId = row.acceptanceId || this.acceptanceIds[0];
      try {
        const response = await getBankAcceptance(acceptanceId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          const data = response.data || response;
          this.acceptanceForm = {
            ...data,
            issueDate: data.issueDate ? this.parseTime(data.issueDate, '{y}-{m}-{d}') : null,
            maturityDate: data.maturityDate ? this.parseTime(data.maturityDate, '{y}-{m}-{d}') : null
          };
          this.acceptanceOpen = true;
          this.acceptanceTitle = "修改银行承兑汇票申请";
        } else {
          this.$message.error(response.message || response.msg || '获取详情失败');
        }
      } catch (error) {
        console.error('获取银行承兑汇票详情失败:', error);
        this.$message.error('获取详情失败：' + (error.message || '网络错误'));
      }
    },

    /** 查看按钮操作 */
    async handleAcceptanceView(row) {
      this.resetAcceptanceForm();
      this.acceptanceViewMode = true;
      const acceptanceId = row.acceptanceId;
      try {
        const response = await getBankAcceptance(acceptanceId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          const data = response.data || response;
          this.acceptanceForm = {
            ...data,
            issueDate: data.issueDate ? this.parseTime(data.issueDate, '{y}-{m}-{d}') : null,
            maturityDate: data.maturityDate ? this.parseTime(data.maturityDate, '{y}-{m}-{d}') : null
          };
          this.acceptanceOpen = true;
          this.acceptanceTitle = "查看银行承兑汇票详情";
        } else {
          this.$message.error(response.message || response.msg || '获取详情失败');
        }
      } catch (error) {
        console.error('获取银行承兑汇票详情失败:', error);
        this.$message.error('获取详情失败：' + (error.message || '网络错误'));
      }
    },

    /** 提交审批按钮操作 */
    async handleAcceptanceSubmit(row) {
      const acceptanceId = row.acceptanceId || this.acceptanceIds[0];
      this.$confirm('是否确认提交该银行承兑汇票申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await submitAcceptanceApplication(acceptanceId);
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$message.success("提交成功");
            this.getAcceptanceList();
          } else {
            this.$message.error(response.message || response.msg || '提交失败');
          }
        } catch (error) {
          console.error('提交申请失败:', error);
          this.$message.error('提交失败：' + (error.message || '网络错误'));
        }
      }).catch(() => {});
    },

    /** 审批按钮操作 */
    handleAcceptanceApprove(row) {
      this.$prompt('请输入审批意见', '审批', {
        confirmButtonText: '通过',
        cancelButtonText: '拒绝',
        distinguishCancelAndClose: true,
        inputPlaceholder: '请输入审批意见'
      }).then(async ({ value }) => {
        try {
          const response = await approveAcceptanceApplication(row.acceptanceId, {
            approved: true,
            approvalComment: value
          });
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$message.success("审批通过");
            this.getAcceptanceList();
          } else {
            this.$message.error(response.message || response.msg || '审批失败');
          }
        } catch (error) {
          console.error('审批失败:', error);
          this.$message.error('审批失败：' + (error.message || '网络错误'));
        }
      }).catch(action => {
        if (action === 'cancel') {
          this.$prompt('请输入拒绝原因', '拒绝', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputPlaceholder: '请输入拒绝原因'
          }).then(async ({ value }) => {
            try {
              const response = await approveAcceptanceApplication(row.acceptanceId, {
                approved: false,
                approvalComment: value
              });
              const successCodes = [200, 0, '200', '0', '1', 1, 2];
              if (successCodes.includes(response.code)) {
                this.$message.success("已拒绝");
                this.getAcceptanceList();
              } else {
                this.$message.error(response.message || response.msg || '操作失败');
              }
            } catch (error) {
              console.error('拒绝失败:', error);
              this.$message.error('操作失败：' + (error.message || '网络错误'));
            }
          }).catch(() => {});
        }
      });
    },

    /** 保证金管理按钮操作 */
    async handleMarginManage(row) {
      // 判断 row 是否是有效的行数据（不是事件对象）
      const isValidRow = row && row.acceptanceId !== undefined && !(row instanceof Event) && !row.isTrusted;
      const acceptanceId = isValidRow ? row.acceptanceId : this.acceptanceIds[0];

      if (!acceptanceId) {
        this.$message.warning("请选择一条银行承兑汇票记录");
        return;
      }

      try {
        const response = await getMarginInfo(acceptanceId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          const data = response.data || response;
          this.marginForm = {
            acceptanceId: acceptanceId,
            applicationNo: data.applicationNo,
            faceAmount: data.faceAmount,
            marginRatio: data.marginRatio,
            marginAmount: data.marginAmount,
            marginAccount: data.marginAccount || '',
            operationType: 'DEPOSIT',
            remark: ''
          };
          this.marginOpen = true;
        } else {
          this.$message.error(response.message || response.msg || '获取保证金信息失败');
        }
      } catch (error) {
        console.error('获取保证金信息失败:', error);
        this.$message.error('获取保证金信息失败：' + (error.message || '网络错误'));
      }
    },

    /** 提交银行承兑汇票表单 */
    submitAcceptanceForm() {
      this.$refs["acceptanceForm"].validate(async valid => {
        if (valid) {
          try {
            let response;
            if (this.acceptanceForm.acceptanceId != null) {
              response = await updateBankAcceptance(this.acceptanceForm);
            } else {
              response = await createBankAcceptance(this.acceptanceForm);
            }
            const successCodes = [200, 0, '200', '0', '1', 1, 2];
            if (successCodes.includes(response.code)) {
              this.$message.success(this.acceptanceForm.acceptanceId ? "修改成功" : "新增成功");
              this.acceptanceOpen = false;
              this.getAcceptanceList();
            } else {
              this.$message.error(response.message || response.msg || '操作失败');
            }
          } catch (error) {
            console.error('提交失败:', error);
            this.$message.error('操作失败：' + (error.message || '网络错误'));
          }
        }
      });
    },

    /** 删除银行承兑汇票 */
    handleAcceptanceDelete(row) {
      const acceptanceIds = row && row.acceptanceId ? [row.acceptanceId] : this.acceptanceIds;
      if (!acceptanceIds || acceptanceIds.length === 0) {
        this.$message.warning("请选择要删除的银行承兑汇票");
        return;
      }
      this.$confirm('是否确认删除选中的银行承兑汇票?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return deleteBankAcceptance(acceptanceIds);
      }).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.getAcceptanceList();
          this.$message.success("删除成功");
        } else {
          this.$message.error(response.message || response.msg || '删除失败');
        }
      }).catch(error => {
        if (error !== 'cancel') {
          console.error('删除银行承兑汇票失败:', error);
          this.$message.error('删除失败：' + (error.message || '网络错误'));
        }
      });
    },

    /** 取消银行承兑汇票按钮 */
    cancelAcceptance() {
      this.acceptanceOpen = false;
      this.resetAcceptanceForm();
    },

    /** 银行承兑汇票表单重置 */
    resetAcceptanceForm() {
      this.acceptanceForm = {
        acceptanceId: null,
        acceptanceNumber: null,
        acceptingBank: null,
        acceptanceAmount: null,
        currency: 'CNY',
        drawer: null,
        payee: null,
        issueDate: null,
        maturityDate: null,
        creditLine: null,
        marginRate: 10,
        marginAmount: 0,
        commissionRate: null,
        guaranteeType: null,
        guaranteeDetails: null,
        businessPurpose: null,
        remark: null
      };
      this.$nextTick(() => {
        if (this.$refs["acceptanceForm"]) {
          this.$refs["acceptanceForm"].clearValidate();
        }
      });
    },

    /** 计算保证金金额 */
    calculateMarginAmount() {
      const acceptanceAmount = parseFloat(this.acceptanceForm.acceptanceAmount) || 0;
      const marginRate = parseFloat(this.acceptanceForm.marginRate) || 0;
      this.acceptanceForm.marginAmount = (acceptanceAmount * marginRate / 100).toFixed(2);
    },

    /** 提交保证金管理表单 */
    submitMarginForm() {
      this.$refs["marginForm"].validate(async valid => {
        if (valid) {
          try {
            const response = await manageMargin(this.marginForm.acceptanceId, this.marginForm);
            const successCodes = [200, 0, '200', '0', '1', 1, 2];
            if (successCodes.includes(response.code)) {
              this.$message.success("操作成功");
              this.marginOpen = false;
              this.getAcceptanceList();
            } else {
              this.$message.error(response.message || response.msg || '操作失败');
            }
          } catch (error) {
            console.error('保证金管理失败:', error);
            this.$message.error('操作失败：' + (error.message || '网络错误'));
          }
        }
      });
    },

    /** 取消保证金管理按钮 */
    cancelMargin() {
      this.marginOpen = false;
      this.marginForm = {};
    },

    /** 分页相关方法 */
    handleAcceptanceCurrentChange(val) {
      this.acceptanceQuery.pageNum = val;
      this.getAcceptanceList();
    },

    handleAcceptanceSizeChange(val) {
      this.acceptanceQuery.pageSize = val;
      this.getAcceptanceList();
    },

    // ==================== 信用证管理方法 ====================

    /** 查询信用证列表 */
    async getLcList() {
      this.lcLoading = true;
      try {
        const response = await getLetterOfCreditPage(this.lcQuery);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];

        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && response.data.rows !== undefined) {
            // rows格式：{ code: 1, data: { rows: [], total: 7 } }
            this.lcList = response.data.rows || [];
            this.lcTotal = response.data.total || 0;
          } else if (response.data && response.data.tlist !== undefined) {
            this.lcList = response.data.tlist || [];
            this.lcTotal = response.data.totalRecord || 0;
          } else if (response.data && response.data.list !== undefined) {
            this.lcList = response.data.list || [];
            this.lcTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            this.lcList = response.data;
            this.lcTotal = response.data.length;
          } else if (response.data) {
            this.lcList = [response.data];
            this.lcTotal = 1;
          } else {
            this.lcList = [];
            this.lcTotal = 0;
          }
        } else {
          this.$message.error(response.message || response.msg || '查询失败');
          this.lcList = [];
          this.lcTotal = 0;
        }
      } catch (error) {
        console.error('获取信用证列表失败:', error);
        this.$message.error('获取数据失败：' + (error.message || '网络错误'));
        this.lcList = [];
        this.lcTotal = 0;
      }
      this.lcLoading = false;
    },

    /** 搜索按钮操作 */
    handleLcQuery() {
      this.lcQuery.pageNum = 1;
      this.getLcList();
    },

    /** 重置按钮操作 */
    resetLcQuery() {
      this.lcQuery = {
        pageNum: 1,
        pageSize: 10,
        lcNumber: null,
        lcType: null,
        lcStatus: null,
        applicant: null
      };
      this.handleLcQuery();
    },

    /** 新增按钮操作 */
    handleLcAdd() {
      this.resetLcForm();
      this.lcOpen = true;
      this.lcTitle = "添加信用证";
    },

    /** 修改按钮操作 */
    handleLcUpdate(row) {
      this.resetLcForm();
      const lcId = row.lcId || this.lcIds[0];
      getLetterOfCredit(lcId).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.lcForm = response.data || {};
          this.lcOpen = true;
          this.lcTitle = "修改信用证";
        } else {
          this.$message.error(response.message || response.msg || '获取信用证详情失败');
        }
      }).catch(error => {
        console.error('获取信用证详情失败:', error);
        this.$message.error('获取信用证详情失败：' + (error.message || '网络错误'));
      });
    },

    /** 查看按钮操作 */
    handleLcView(row) {
      this.resetLcForm();
      const lcId = row.lcId;
      getLetterOfCredit(lcId).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.lcForm = response.data || {};
          this.lcOpen = true;
          this.lcTitle = "查看信用证";
          // 禁用所有表单项
          this.$nextTick(() => {
            if (this.$refs.lcForm) {
              this.$refs.lcForm.$el.querySelectorAll('input, textarea, select').forEach(el => {
                el.disabled = true;
              });
            }
          });
        } else {
          this.$message.error(response.message || response.msg || '获取信用证详情失败');
        }
      }).catch(error => {
        console.error('获取信用证详情失败:', error);
        this.$message.error('获取信用证详情失败：' + (error.message || '网络错误'));
      });
    },

    /** 删除按钮操作 */
    handleLcDelete(row) {
      const lcIds = row.lcId ? [row.lcId] : this.lcIds;
      this.$confirm('是否确认删除选中的信用证?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        // 一次性发送所有ID数组，而不是逐个发送
        return deleteLetterOfCredit(lcIds);
      }).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.getLcList();
          this.$message.success("删除成功");
        } else {
          this.$message.error(response.message || response.msg || '删除失败');
        }
      }).catch(error => {
        if (error !== 'cancel') {
          console.error('删除信用证失败:', error);
          this.$message.error('删除失败：' + (error.message || '网络错误'));
        }
      });
    },

    /** 开立按钮操作 */
    handleLcIssue() {
      const lcId = this.lcIds[0];
      this.$confirm('是否确认开立该信用证?', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return issueLetterOfCredit(lcId);
      }).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.getLcList();
          this.$message.success("开立成功");
        } else {
          this.$message.error(response.message || response.msg || '开立失败');
        }
      }).catch(error => {
        if (error !== 'cancel') {
          console.error('开立信用证失败:', error);
          this.$message.error('开立失败：' + (error.message || '网络错误'));
        }
      });
    },

    /** 提交信用证表单 */
    submitLcForm() {
      this.$refs["lcForm"].validate(async valid => {
        if (valid) {
          try {
            let response;
            if (this.lcForm.lcId != null) {
              response = await updateLetterOfCredit(this.lcForm);
            } else {
              response = await createLetterOfCredit(this.lcForm);
            }

            const successCodes = [200, 0, '200', '0', '1', 1, 2];
            if (successCodes.includes(response.code)) {
              this.$message.success(this.lcForm.lcId ? "修改成功" : "新增成功");
              this.lcOpen = false;
              this.getLcList();
            } else {
              this.$message.error(response.message || response.msg || '操作失败');
            }
          } catch (error) {
            console.error('提交信用证表单失败:', error);
            this.$message.error('操作失败：' + (error.message || '网络错误'));
          }
        }
      });
    },

    /** 取消信用证表单 */
    cancelLc() {
      this.lcOpen = false;
      this.resetLcForm();
    },

    /** 重置信用证表单 */
    resetLcForm() {
      this.lcForm = {
        lcId: null,
        lcNumber: null,
        lcType: null,
        applicant: null,
        beneficiary: null,
        issuingBank: null,
        advisingBank: null,
        confirmingBank: null,
        negotiatingBank: null,
        lcAmount: null,
        availableAmount: null,
        tolerance: 0,
        partialShipment: 'ALLOWED',
        transshipment: 'ALLOWED',
        shipmentDate: null,
        expiryDate: null,
        expiryPlace: null,
        presentationPeriod: 21,
        documentRequirements: null,
        specialConditions: null,
        lcStatus: 'DRAFT',
        marginRequired: 1,
        marginAmount: null,
        commissionAmount: null,
        remark: null,
        currencyCode: 'CNY'
      };
      if (this.$refs.lcForm) {
        this.$nextTick(() => {
          this.$refs.lcForm.resetFields();
        });
      }
    },

    /** 多选框选中数据 */
    handleLcSelectionChange(selection) {
      this.lcIds = selection.map(item => item.lcId);
      this.lcSingle = selection.length !== 1;
      this.lcMultiple = !selection.length;
    },

    /** 处理信用证分页页码变化 */
    handleLcCurrentChange(val) {
      this.lcQuery.pageNum = val;
      this.getLcList();
    },

    /** 处理信用证分页大小变化 */
    handleLcSizeChange(val) {
      this.lcQuery.pageSize = val;
      this.getLcList();
    },

    // ==================== 保函管理方法 ====================

    /** 查询保函列表 */
    async getGuaranteeList() {
      this.guaranteeLoading = true;
      try {
        const response = await getGuaranteePage(this.guaranteeQuery);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];

        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && response.data.rows !== undefined) {
            // rows格式：{ code: 1, data: { rows: [], total: 5 } }
            this.guaranteeList = response.data.rows || [];
            this.guaranteeTotal = response.data.total || 0;
          } else if (response.data && response.data.tlist !== undefined) {
            this.guaranteeList = response.data.tlist || [];
            this.guaranteeTotal = response.data.totalRecord || 0;
          } else if (response.data && response.data.list !== undefined) {
            this.guaranteeList = response.data.list || [];
            this.guaranteeTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            this.guaranteeList = response.data;
            this.guaranteeTotal = response.data.length;
          } else if (response.data) {
            this.guaranteeList = [response.data];
            this.guaranteeTotal = 1;
          } else {
            this.guaranteeList = [];
            this.guaranteeTotal = 0;
          }
        } else {
          this.$message.error(response.message || response.msg || '查询失败');
          this.guaranteeList = [];
          this.guaranteeTotal = 0;
        }
      } catch (error) {
        console.error('获取保函列表失败:', error);
        this.$message.error('获取数据失败：' + (error.message || '网络错误'));
        this.guaranteeList = [];
        this.guaranteeTotal = 0;
      }
      this.guaranteeLoading = false;
    },

    /** 搜索按钮操作 */
    handleGuaranteeQuery() {
      this.guaranteeQuery.pageNum = 1;
      this.getGuaranteeList();
    },

    /** 重置按钮操作 */
    resetGuaranteeQuery() {
      this.guaranteeQuery = {
        pageNum: 1,
        pageSize: 10,
        guaranteeNo: null,
        guaranteeType: null,
        guaranteeStatus: null,
        applicant: null
      };
      this.handleGuaranteeQuery();
    },

    /** 新增按钮操作 */
    handleGuaranteeAdd() {
      // 先重置表单数据
      this.guaranteeForm = {
        guaranteeId: null,
        guaranteeNo: null,
        guaranteeType: null,
        applicant: null,
        beneficiary: null,
        issuingBank: null,
        guaranteeAmount: null,
        effectiveDate: null,
        expiryDate: null,
        claimPeriod: 30,
        underlyingContract: null,
        contractAmount: null,
        guaranteeTerms: null,
        claimConditions: null,
        guaranteeStatus: 'DRAFT',
        marginRequired: 1,
        marginRatio: 100,
        marginAmount: null,
        commissionRate: null,
        commissionAmount: null,
        claimStatus: 'NO_CLAIM',
        remark: null
      };
      this.guaranteeTitle = "添加保函";
      this.guaranteeOpen = true;
      // 打开对话框后清除表单验证状态
      this.$nextTick(() => {
        if (this.$refs.guaranteeForm) {
          this.$refs.guaranteeForm.clearValidate();
        }
      });
    },

    /** 修改按钮操作 */
    handleGuaranteeUpdate(row) {
      this.resetGuaranteeForm();
      const guaranteeId = row.guaranteeId || this.guaranteeIds[0];
      getGuarantee(guaranteeId).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          const data = response.data || {};
          // 映射后端字段到前端表单字段，并转换日期格式
          this.guaranteeForm = {
            guaranteeId: data.guaranteeId,
            guaranteeNo: data.guaranteeNumber || data.guaranteeId, // 保函编号
            guaranteeType: data.guaranteeType,
            applicant: data.applicant,
            beneficiary: data.beneficiary,
            issuingBank: data.issuingBank || data.guaranteeBank, // 开立银行
            guaranteeAmount: data.guaranteeAmount,
            // 将时间戳转换为 yyyy-MM-dd 格式字符串
            effectiveDate: data.issueDate ? this.parseTime(data.issueDate, '{y}-{m}-{d}') : null,
            expiryDate: data.expiryDate ? this.parseTime(data.expiryDate, '{y}-{m}-{d}') : null,
            guaranteeStatus: data.guaranteeStatus,
            companyId: data.companyId,
            remark: data.remark,
            // 其他前端表单字段保持默认值
            claimPeriod: data.claimPeriod || 30,
            underlyingContract: data.underlyingContract,
            contractAmount: data.contractAmount,
            marginRatio: data.marginRatio || 100,
            marginAmount: data.marginAmount,
            commissionRate: data.commissionRate,
            guaranteeTerms: data.guaranteeTerms,
            claimConditions: data.claimConditions
          };
          this.guaranteeOpen = true;
          this.guaranteeTitle = "修改保函";
        } else {
          this.$message.error(response.message || response.msg || '获取保函详情失败');
        }
      }).catch(error => {
        console.error('获取保函详情失败:', error);
        this.$message.error('获取保函详情失败：' + (error.message || '网络错误'));
      });
    },

    /** 查看按钮操作 */
    handleGuaranteeView(row) {
      this.resetGuaranteeForm();
      const guaranteeId = row.guaranteeId;
      getGuarantee(guaranteeId).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          const data = response.data || {};
          // 映射后端字段到前端表单字段，并转换日期格式
          this.guaranteeForm = {
            guaranteeId: data.guaranteeId,
            guaranteeNo: data.guaranteeNumber || data.guaranteeId,
            guaranteeType: data.guaranteeType,
            applicant: data.applicant,
            beneficiary: data.beneficiary,
            issuingBank: data.issuingBank || data.guaranteeBank,
            guaranteeAmount: data.guaranteeAmount,
            // 将时间戳转换为 yyyy-MM-dd 格式字符串
            effectiveDate: data.issueDate ? this.parseTime(data.issueDate, '{y}-{m}-{d}') : null,
            expiryDate: data.expiryDate ? this.parseTime(data.expiryDate, '{y}-{m}-{d}') : null,
            guaranteeStatus: data.guaranteeStatus,
            companyId: data.companyId,
            remark: data.remark,
            claimPeriod: data.claimPeriod || 30,
            underlyingContract: data.underlyingContract,
            contractAmount: data.contractAmount,
            marginRatio: data.marginRatio || 100,
            marginAmount: data.marginAmount,
            commissionRate: data.commissionRate,
            guaranteeTerms: data.guaranteeTerms,
            claimConditions: data.claimConditions
          };
          this.guaranteeOpen = true;
          this.guaranteeTitle = "查看保函";
          // 禁用所有表单项
          this.$nextTick(() => {
            if (this.$refs.guaranteeForm) {
              this.$refs.guaranteeForm.$el.querySelectorAll('input, textarea, select').forEach(el => {
                el.disabled = true;
              });
            }
          });
        } else {
          this.$message.error(response.message || response.msg || '获取保函详情失败');
        }
      }).catch(error => {
        console.error('获取保函详情失败:', error);
        this.$message.error('获取保函详情失败：' + (error.message || '网络错误'));
      });
    },

    /** 删除按钮操作 */
    handleGuaranteeDelete(row) {
      const guaranteeIds = row.guaranteeId ? [row.guaranteeId] : this.guaranteeIds;
      if (!guaranteeIds || guaranteeIds.length === 0) {
        this.$message.warning("请选择要删除的保函");
        return;
      }
      this.$confirm('是否确认删除选中的保函?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        // 一次性发送所有要删除的ID
        return deleteGuarantee(guaranteeIds);
      }).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.getGuaranteeList();
          this.$message.success("删除成功");
        } else {
          this.$message.error(response.message || response.msg || '删除失败');
        }
      }).catch(error => {
        if (error !== 'cancel') {
          console.error('删除保函失败:', error);
          this.$message.error('删除失败：' + (error.message || '网络错误'));
        }
      });
    },

    /** 开立按钮操作 */
    handleGuaranteeIssue() {
      const guaranteeId = this.guaranteeIds[0];
      this.$confirm('是否确认开立该保函?', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return issueGuarantee(guaranteeId);
      }).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.getGuaranteeList();
          this.$message.success("开立成功");
        } else {
          this.$message.error(response.message || response.msg || '开立失败');
        }
      }).catch(error => {
        if (error !== 'cancel') {
          console.error('开立保函失败:', error);
          this.$message.error('开立失败：' + (error.message || '网络错误'));
        }
      });
    },

    /** 提交保函表单 */
    submitGuaranteeForm() {
      this.$refs["guaranteeForm"].validate(async valid => {
        if (valid) {
          try {
            let response;
            if (this.guaranteeForm.guaranteeId != null) {
              response = await updateGuarantee(this.guaranteeForm);
            } else {
              response = await createGuarantee(this.guaranteeForm);
            }

            const successCodes = [200, 0, '200', '0', '1', 1, 2];
            if (successCodes.includes(response.code)) {
              this.$message.success(this.guaranteeForm.guaranteeId ? "修改成功" : "新增成功");
              this.guaranteeOpen = false;
              this.getGuaranteeList();
            } else {
              this.$message.error(response.message || response.msg || '操作失败');
            }
          } catch (error) {
            console.error('提交保函表单失败:', error);
            this.$message.error('操作失败：' + (error.message || '网络错误'));
          }
        }
      });
    },

    /** 取消保函表单 */
    cancelGuarantee() {
      this.guaranteeOpen = false;
      this.resetGuaranteeForm();
    },

    /** 重置保函表单 */
    resetGuaranteeForm() {
      this.guaranteeForm = {
        guaranteeId: null,
        guaranteeNo: null,
        guaranteeType: null,
        applicant: null,
        beneficiary: null,
        issuingBank: null,
        guaranteeAmount: null,
        effectiveDate: null,
        expiryDate: null,
        claimPeriod: 30,
        underlyingContract: null,
        contractAmount: null,
        guaranteeTerms: null,
        claimConditions: null,
        guaranteeStatus: 'DRAFT',
        marginRequired: 1,
        marginRatio: 100,
        marginAmount: null,
        commissionRate: null,
        commissionAmount: null,
        claimStatus: 'NO_CLAIM',
        remark: null
      };
      if (this.$refs.guaranteeForm) {
        this.$nextTick(() => {
          this.$refs.guaranteeForm.resetFields();
        });
      }
    },

    /** 多选框选中数据 */
    handleGuaranteeSelectionChange(selection) {
      this.guaranteeIds = selection.map(item => item.guaranteeId);
      this.guaranteeSingle = selection.length !== 1;
      this.guaranteeMultiple = !selection.length;
    },

    /** 处理保函分页页码变化 */
    handleGuaranteeCurrentChange(val) {
      this.guaranteeQuery.pageNum = val;
      this.getGuaranteeList();
    },

    /** 处理保函分页大小变化 */
    handleGuaranteeSizeChange(val) {
      this.guaranteeQuery.pageSize = val;
      this.getGuaranteeList();
    },

    // ==================== 票据查询方法 ====================

    /** 查询票据台账列表 */
    async getQueryList() {
      this.queryLoading = true;
      try {
        // 处理日期范围
        const params = { ...this.queryForm };
        if (params.maturityDateRange && params.maturityDateRange.length === 2) {
          params.maturityDateStart = params.maturityDateRange[0];
          params.maturityDateEnd = params.maturityDateRange[1];
        }
        // 始终删除前端专用的日期范围字段，后端不识别该字段
        delete params.maturityDateRange;
        // 清理空值参数，避免后端将 null 当作有效筛选条件
        Object.keys(params).forEach(key => {
          if (params[key] === null || params[key] === undefined || params[key] === '') {
            delete params[key];
          }
        });

        const response = await getInstrumentLedger(params);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];

        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && response.data.tlist !== undefined) {
            this.queryList = response.data.tlist || [];
            this.queryTotal = response.data.totalRecord || 0;
          } else if (response.data && response.data.list !== undefined) {
            this.queryList = response.data.list || [];
            this.queryTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            this.queryList = response.data;
            this.queryTotal = response.data.length;
          } else if (response.data) {
            this.queryList = [response.data];
            this.queryTotal = 1;
          } else {
            this.queryList = [];
            this.queryTotal = 0;
          }
        } else {
          this.$message.error(response.message || response.msg || '查询失败');
          this.queryList = [];
          this.queryTotal = 0;
        }
      } catch (error) {
        console.error('获取票据台账列表失败:', error);
        this.$message.error('获取数据失败：' + (error.message || '网络错误'));
        this.queryList = [];
        this.queryTotal = 0;
      }
      this.queryLoading = false;
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryForm.pageNum = 1;
      this.getQueryList();
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.queryForm = {
        pageNum: 1,
        pageSize: 10,
        instrumentNo: null,
        instrumentType: null,
        instrumentStatus: null,
        issuer: null,
        payee: null,
        currencyCode: null,
        maturityDateRange: null
      };
      this.handleQuery();
    },

    /** 查看票据详情 */
    async handleQueryView(row) {
      try {
        const response = await getInstrumentDetail(row.instrumentId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.queryDetail = response.data || {};
          this.queryDetailOpen = true;
        } else {
          this.$message.error(response.message || response.msg || '获取票据详情失败');
        }
      } catch (error) {
        console.error('获取票据详情失败:', error);
        this.$message.error('获取票据详情失败：' + (error.message || '网络错误'));
      }
    },

    /** 查看票据流转历史 */
    async handleQueryHistory(row) {
      try {
        const response = await getInstrumentHistory(row.instrumentId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.queryHistory = response.data || [];
          this.queryHistoryOpen = true;
        } else {
          this.$message.error(response.message || response.msg || '获取流转历史失败');
        }
      } catch (error) {
        console.error('获取流转历史失败:', error);
        this.$message.error('获取流转历史失败：' + (error.message || '网络错误'));
      }
    },

    /** 统计分析 */
    async handleStatistics() {
      try {
        const response = await getInstrumentStatistics({ orgId: this.queryForm.orgId });
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.statistics = response.data || {
            typeStats: [],
            statusStats: [],
            currencyStats: [],
            summary: {}
          };
          this.statisticsOpen = true;
        } else {
          this.$message.error(response.message || response.msg || '获取统计数据失败');
        }
      } catch (error) {
        console.error('获取统计数据失败:', error);
        this.$message.error('获取统计数据失败：' + (error.message || '网络错误'));
      }
    },

    /** 到期提醒 */
    async handleMaturityAlerts() {
      try {
        const response = await getMaturityAlerts({
          days: this.alertDays,
          orgId: this.queryForm.orgId
        });
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          if (response.data && response.data.list) {
            this.maturityAlerts = response.data.list || [];
          } else if (response.data && Array.isArray(response.data)) {
            this.maturityAlerts = response.data;
          } else {
            this.maturityAlerts = [];
          }
          this.alertsOpen = true;
        } else {
          this.$message.error(response.message || response.msg || '获取到期提醒失败');
        }
      } catch (error) {
        console.error('获取到期提醒失败:', error);
        this.$message.error('获取到期提醒失败：' + (error.message || '网络错误'));
      }
    },

    /** 导出 */
    handleExport() {
      // 调用后端API导出数据
      const params = { ...this.queryForm }
      exportBillLedger(params).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '票据台账数据_' + new Date().getTime() + '.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
        this.$message({ type: 'success', message: '数据导出成功' })
      }).catch(error => {
        console.error('导出失败:', error)
        this.$message({ type: 'error', message: '导出失败，请稍后重试' })
      })
    },

    /** 多选框选中数据 */
    handleQuerySelectionChange(selection) {
      this.queryIds = selection.map(item => item.instrumentId);
    },

    /** 处理票据查询分页页码变化 */
    handleQueryCurrentChange(val) {
      this.queryForm.pageNum = val;
      this.getQueryList();
    },

    /** 处理票据查询分页大小变化 */
    handleQuerySizeChange(val) {
      this.queryForm.pageSize = val;
      this.getQueryList();
    },

    /** 重置表单 */
    resetForm(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields();
      }
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.mb8 {
  margin-bottom: 8px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.stat-item:last-child {
  border-bottom: none;
}

.stat-value {
  font-weight: bold;
  color: #409EFF;
}
</style>
