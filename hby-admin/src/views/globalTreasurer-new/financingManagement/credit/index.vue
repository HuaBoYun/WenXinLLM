<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 授信申请管理 -->
      <el-tab-pane label="授信申请管理" name="application">
        <div class="application-container">
          <!-- 查询条件 -->
          <el-form :model="applicationQuery" ref="applicationQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="申请编号" prop="applicationNo">
              <el-input
                v-model="applicationQuery.applicationNo"
                placeholder="请输入申请编号"
                clearable
                @keyup.enter.native="handleApplicationQuery"
              />
            </el-form-item>
            <el-form-item label="申请机构" prop="orgName">
              <el-input
                v-model="applicationQuery.orgName"
                placeholder="请输入申请机构"
                clearable
                @keyup.enter.native="handleApplicationQuery"
              />
            </el-form-item>
            <el-form-item label="授信类型" prop="creditType">
              <el-select v-model="applicationQuery.creditType" placeholder="请选择授信类型" clearable>
                <el-option label="流动资金贷款" value="WORKING_CAPITAL_LOAN" />
                <el-option label="固定资产贷款" value="FIXED_ASSET_LOAN" />
                <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
                <el-option label="信用证" value="LETTER_OF_CREDIT" />
              </el-select>
            </el-form-item>
            <el-form-item label="申请状态" prop="applicationStatus">
              <el-select v-model="applicationQuery.applicationStatus" placeholder="请选择申请状态" clearable>
                <el-option label="草稿" value="DRAFT" />
                <el-option label="待审批" value="PENDING_APPROVAL" />
                <el-option label="已审批" value="APPROVED" />
                <el-option label="已拒绝" value="REJECTED" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleApplicationQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetApplicationQuery">重置</el-button>
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
                @click="handleApplicationAdd"
              >新增申请</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="applicationSingle"
                @click="handleApplicationUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="applicationMultiple"
                @click="handleApplicationDelete"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-upload2"
                size="mini"
                :disabled="applicationSingle"
                @click="handleApplicationSubmit"
              >提交审批</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="applicationLoading" :data="applicationList" @selection-change="handleApplicationSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="申请编号" align="center" prop="applicationNo" />
            <el-table-column label="申请机构" align="center" prop="companyName" />
            <el-table-column label="授信类型" align="center" prop="creditType">
              <template slot-scope="scope">
                <el-tag :type="getApplicationCreditTypeTagType(scope.row.creditType)">
                  {{ getApplicationCreditTypeLabel(scope.row.creditType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="申请金额" align="center" prop="applyAmount">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.applyAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="申请状态" align="center" prop="applicationStatus">
              <template slot-scope="scope">
                <el-tag :type="getApplicationStatusTagType(scope.row.applicationStatus)">
                  {{ getApplicationStatusLabel(scope.row.applicationStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="申请时间" align="center" prop="applicationTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.applicationTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleApplicationView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleApplicationUpdate(scope.row)"
                  v-if="scope.row.applicationStatus === 'DRAFT'"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-upload2"
                  @click="handleApplicationSubmit(scope.row)"
                  v-if="scope.row.applicationStatus === 'DRAFT'"
                >提交</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleApplicationDelete(scope.row)"
                  v-if="scope.row.applicationStatus === 'DRAFT'"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination
            v-show="applicationTotal>0"
            :total="applicationTotal"
            :page.sync="applicationQuery.pageNum"
            :limit.sync="applicationQuery.pageSize"
            @pagination="getApplicationList"
          />
        </div>
      </el-tab-pane>

      <!-- 授信合同管理 -->
      <el-tab-pane label="授信合同管理" name="contract">
        <div class="contract-container">
          <!-- 查询条件 -->
          <el-form :model="contractQuery" ref="contractQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input
                v-model="contractQuery.contractNo"
                placeholder="请输入合同编号"
                clearable
                @keyup.enter.native="handleContractQuery"
              />
            </el-form-item>
            <el-form-item label="公司名称" prop="companyName">
              <el-input
                v-model="contractQuery.companyName"
                placeholder="请输入公司名称"
                clearable
                @keyup.enter.native="handleContractQuery"
              />
            </el-form-item>
            <el-form-item label="授信类型" prop="creditType">
              <el-select v-model="contractQuery.creditType" placeholder="请选择授信类型" clearable>
                <el-option label="综合授信" value="COMPREHENSIVE" />
                <el-option label="专项授信" value="SPECIAL" />
                <el-option label="临时授信" value="TEMPORARY" />
              </el-select>
            </el-form-item>
            <el-form-item label="合同状态" prop="contractStatus">
              <el-select v-model="contractQuery.contractStatus" placeholder="请选择合同状态" clearable>
                <el-option label="有效" value="EFFECTIVE" />
                <el-option label="已终止" value="TERMINATED" />
                <el-option label="已到期" value="EXPIRED" />
              </el-select>
            </el-form-item>
            <el-form-item label="融资机构" prop="financingInstitution">
              <el-input
                v-model="contractQuery.financingInstitution"
                placeholder="请输入融资机构"
                clearable
                @keyup.enter.native="handleContractQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleContractQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetContractQuery">重置</el-button>
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
                @click="handleContractAdd"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="contractSingle"
                @click="handleContractUpdate(null)"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="contractMultiple"
                @click="handleContractDelete(null)"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleContractExport"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getContractList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table
            v-loading="contractLoading"
            :data="contractList"
            row-key="contractId"
            @selection-change="handleContractSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="合同编号" align="center" prop="contractNo" width="150" />
            <el-table-column label="公司名称" align="center" prop="companyName" width="150" />
            <el-table-column label="银行名称" align="center" prop="bankName" width="180" />
            <el-table-column label="授信额度" align="center" prop="creditLimit" width="140">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.creditLimit, scope.row.currencyCode) }}
              </template>
            </el-table-column>
            <el-table-column label="授信期限" align="center" prop="creditPeriod" width="100">
              <template slot-scope="scope">
                <span>{{ scope.row.creditPeriod }}个月</span>
              </template>
            </el-table-column>
            <el-table-column label="合同期限" align="center" width="220">
              <template slot-scope="scope">
                {{ formatDateValue(scope.row.startDate) }} 至 {{ formatDateValue(scope.row.endDate) }}
              </template>
            </el-table-column>
            <el-table-column label="利率" align="center" prop="interestRate" width="100">
              <template slot-scope="scope">
                <span>{{ scope.row.interestRate }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="担保方式" align="center" prop="guaranteeMethod" width="120">
              <template slot-scope="scope">
                {{ getGuaranteeMethodLabel(scope.row.guaranteeMethod) }}
              </template>
            </el-table-column>
            <el-table-column label="合同状态" align="center" prop="contractStatus" width="100">
              <template slot-scope="scope">
                <el-tag :type="getContractStatusTagType(scope.row.contractStatus)">
                  {{ getContractStatusLabel(scope.row.contractStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createdTime" width="160">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
              <template slot-scope="scope">
                <el-dropdown size="mini" split-button type="primary" @click="handleContractView(scope.row)">
                  查看
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="handleContractUpdate(scope.row)">
                      <i class="el-icon-edit"></i> 修改
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="handleContractSign(scope.row)" v-if="scope.row.contractStatus === 'EFFECTIVE'">
                      <i class="el-icon-check"></i> 签署
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="handleContractTerminate(scope.row)" v-if="scope.row.contractStatus === 'EFFECTIVE'">
                      <i class="el-icon-close"></i> 终止
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="handleContractDelete(scope.row)">
                      <i class="el-icon-delete"></i> 删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页组件 -->
          <pagination
            v-show="contractTotal > 0"
            :total="contractTotal"
            :page.sync="contractQuery.pageNum"
            :limit.sync="contractQuery.pageSize"
            @pagination="getContractList"
          />
        </div>
      </el-tab-pane>

      <!-- 授信额度管理 -->
      <el-tab-pane label="授信额度管理" name="limit">
        <div class="limit-container">
          <!-- 查询条件 -->
          <el-form :model="limitQuery" ref="limitQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="额度代码" prop="limitCode">
              <el-input
                v-model="limitQuery.limitCode"
                placeholder="请输入额度代码"
                clearable
                @keyup.enter.native="handleLimitQuery"
              />
            </el-form-item>
            <el-form-item label="公司名称" prop="companyName">
              <el-input
                v-model="limitQuery.companyName"
                placeholder="请输入公司名称"
                clearable
                @keyup.enter.native="handleLimitQuery"
              />
            </el-form-item>
            <el-form-item label="授信类型" prop="creditType">
              <el-select v-model="limitQuery.creditType" placeholder="请选择授信类型" clearable>
                <el-option
                  v-for="item in limitCreditTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="额度状态" prop="limitStatus">
              <el-select v-model="limitQuery.limitStatus" placeholder="请选择额度状态" clearable>
                <el-option
                  v-for="item in limitStatusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="融资机构" prop="financingInstitution">
              <el-input
                v-model="limitQuery.financingInstitution"
                placeholder="请输入融资机构"
                clearable
                @keyup.enter.native="handleLimitQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleLimitQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetLimitQuery">重置</el-button>
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
                @click="handleLimitAdd"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="limitSingle"
                @click="handleLimitUpdate(null)"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="limitMultiple"
                @click="handleLimitDelete(null)"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleLimitExport"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getLimitList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="limitLoading" :data="limitList" row-key="limitId" @selection-change="handleLimitSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="额度编号" align="center" prop="limitNo" width="150" />
            <el-table-column label="公司名称" align="center" prop="companyName" width="180" />
            <el-table-column label="额度类型" align="center" prop="limitType" width="120">
              <template slot-scope="scope">
                <el-tag :type="getLimitTypeTagType(scope.row.limitType)">
                  {{ getLimitTypeLabel(scope.row.limitType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="总额度" align="center" prop="totalLimit" width="140">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.totalLimit, scope.row.currencyCode) }}
              </template>
            </el-table-column>
            <el-table-column label="已用额度" align="center" prop="usedLimit" width="140">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.usedLimit, scope.row.currencyCode) }}
              </template>
            </el-table-column>
            <el-table-column label="可用额度" align="center" prop="availableLimit" width="140">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.availableLimit, scope.row.currencyCode) }}
              </template>
            </el-table-column>
            <el-table-column label="使用率" align="center" width="100">
              <template slot-scope="scope">
                <el-progress
                  :percentage="getLimitUsagePercentage(scope.row)"
                  :color="getLimitUsageColor(scope.row)"
                  :stroke-width="8"
                />
              </template>
            </el-table-column>
            <el-table-column label="额度状态" align="center" prop="limitStatus" width="100">
              <template slot-scope="scope">
                <el-tag :type="getLimitStatusTagType(scope.row.limitStatus)">
                  {{ getLimitStatusLabel(scope.row.limitStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="有效期" align="center" width="220">
              <template slot-scope="scope">
                {{ formatDateValue(scope.row.startDate) }} 至 {{ formatDateValue(scope.row.endDate) }}
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createdTime" width="160">
              <template slot-scope="scope">
                {{ parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
              <template slot-scope="scope">
                <el-dropdown size="mini" @command="(command) => handleLimitCommand(command, scope.row)">
                  <el-button type="primary" size="mini">
                    操作<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="edit" icon="el-icon-edit">修改</el-dropdown-item>
                    <el-dropdown-item command="view" icon="el-icon-view">查看</el-dropdown-item>
                    <el-dropdown-item command="use" icon="el-icon-money">使用额度</el-dropdown-item>
                    <el-dropdown-item command="repay" icon="el-icon-refresh">归还额度</el-dropdown-item>
                    <el-dropdown-item command="freeze" icon="el-icon-lock">冻结额度</el-dropdown-item>
                    <el-dropdown-item command="unfreeze" icon="el-icon-unlock">解冻额度</el-dropdown-item>
                    <el-dropdown-item command="suspend" icon="el-icon-warning">暂停额度</el-dropdown-item>
                    <el-dropdown-item command="activate" icon="el-icon-check">激活额度</el-dropdown-item>
                    <el-dropdown-item command="cancel" icon="el-icon-close">取消额度</el-dropdown-item>
                    <el-dropdown-item command="delete" icon="el-icon-delete">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页组件 -->
          <pagination
            v-show="limitTotal > 0"
            :total="limitTotal"
            :page.sync="limitQuery.pageNum"
            :limit.sync="limitQuery.pageSize"
            @pagination="getLimitList"
          />
        </div>
      </el-tab-pane>

      <!-- 授信评估 -->
      <el-tab-pane label="授信评估" name="assessment">
        <div class="assessment-container">
          <!-- 查询表单 -->
          <el-form :model="assessmentQuery" ref="assessmentQueryForm" size="small" :inline="true" v-show="assessmentShowSearch" label-width="68px">
            <el-form-item label="评估编号" prop="assessmentNo">
              <el-input
                v-model="assessmentQuery.assessmentNo"
                placeholder="请输入评估编号"
                clearable
                @keyup.enter.native="handleAssessmentQuery"
              />
            </el-form-item>
            <el-form-item label="公司名称" prop="companyName">
              <el-input
                v-model="assessmentQuery.companyName"
                placeholder="请输入公司名称"
                clearable
                @keyup.enter.native="handleAssessmentQuery"
              />
            </el-form-item>
            <el-form-item label="评估类型" prop="assessmentType">
              <el-select v-model="assessmentQuery.assessmentType" placeholder="请选择评估类型" clearable>
                <el-option
                  v-for="dict in assessmentTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="评估状态" prop="assessmentStatus">
              <el-select v-model="assessmentQuery.assessmentStatus" placeholder="请选择评估状态" clearable>
                <el-option
                  v-for="dict in assessmentStatusOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="信用评级" prop="creditRating">
              <el-select v-model="assessmentQuery.creditRating" placeholder="请选择信用评级" clearable>
                <el-option
                  v-for="dict in creditRatingOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="assessmentQuery.riskLevel" placeholder="请选择风险等级" clearable>
                <el-option
                  v-for="dict in riskLevelOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleAssessmentQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetAssessmentQuery">重置</el-button>
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
                @click="handleAssessmentAdd"
                v-hasPermi="['financing:creditAssessment:add']"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="assessmentSingle"
                @click="handleAssessmentUpdate"
                v-hasPermi="['financing:creditAssessment:edit']"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="assessmentMultiple"
                @click="handleAssessmentDelete"
                v-hasPermi="['financing:creditAssessment:remove']"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleAssessmentExport"
                v-hasPermi="['financing:creditAssessment:export']"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="assessmentShowSearch" @queryTable="getAssessmentList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="assessmentLoading" :data="assessmentList" @selection-change="handleAssessmentSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="评估编号" align="center" prop="assessmentNo" />
            <el-table-column label="公司名称" align="center" prop="companyName" />
            <el-table-column label="评估类型" align="center" prop="assessmentType">
              <template slot-scope="scope">
                <el-tag :type="getAssessmentTypeTagType(scope.row.assessmentType)">
                  {{ getAssessmentTypeLabel(scope.row.assessmentType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="评估日期" align="center" prop="assessmentDate" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.assessmentDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="信用评级" align="center" prop="creditRating">
              <template slot-scope="scope">
                <el-tag :type="getCreditRatingTagType(scope.row.creditRating)">
                  {{ getCreditRatingLabel(scope.row.creditRating) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="风险等级" align="center" prop="riskLevel">
              <template slot-scope="scope">
                <el-tag :type="getRiskLevelTagType(scope.row.riskLevel)">
                  {{ getRiskLevelLabel(scope.row.riskLevel) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="建议额度" align="center" prop="recommendedLimit">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.recommendedLimit) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="评估状态" align="center" prop="assessmentStatus">
              <template slot-scope="scope">
                <el-tag :type="getAssessmentStatusTagType(scope.row.assessmentStatus)">
                  {{ getAssessmentStatusLabel(scope.row.assessmentStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="评估人" align="center" prop="assessorName" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-dropdown @command="(command) => handleAssessmentCommand(command, scope.row)" size="mini">
                  <el-button type="primary" size="mini">
                    操作<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="edit">修改</el-dropdown-item>
                    <el-dropdown-item command="view">查看</el-dropdown-item>
                    <el-dropdown-item command="submit" v-if="scope.row.assessmentStatus === 'DRAFT' || scope.row.assessmentStatus === 'PENDING'">提交</el-dropdown-item>
                    <el-dropdown-item command="approve" v-if="scope.row.assessmentStatus === 'COMPLETED' || scope.row.assessmentStatus === 'PENDING'">批准</el-dropdown-item>
                    <el-dropdown-item command="reject" v-if="scope.row.assessmentStatus === 'COMPLETED' || scope.row.assessmentStatus === 'PENDING'">拒绝</el-dropdown-item>
                    <el-dropdown-item command="withdraw" v-if="scope.row.assessmentStatus === 'COMPLETED' || scope.row.assessmentStatus === 'APPROVED'">撤回</el-dropdown-item>
                    <el-dropdown-item command="copy">复制</el-dropdown-item>
                    <el-dropdown-item command="delete">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="assessmentTotal > 0"
            :total="assessmentTotal"
            :page.sync="assessmentQuery.pageNum"
            :limit.sync="assessmentQuery.pageSize"
            @pagination="getAssessmentList"
          />
        </div>
      </el-tab-pane>

      <!-- 授信监控 -->
      <el-tab-pane label="授信监控" name="monitoring">
        <div class="monitoring-container">
          <!-- 监控概览卡片 -->
          <el-row :gutter="20" class="mb8">
            <el-col :span="6">
              <el-card class="overview-card critical">
                <div class="card-content">
                  <div class="card-icon">
                    <i class="el-icon-warning"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">严重预警</div>
                    <div class="card-value">{{ monitoringOverview.criticalCount || 0 }}</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="overview-card high">
                <div class="card-content">
                  <div class="card-icon">
                    <i class="el-icon-warning-outline"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">高级预警</div>
                    <div class="card-value">{{ monitoringOverview.highCount || 0 }}</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="overview-card active">
                <div class="card-content">
                  <div class="card-icon">
                    <i class="el-icon-bell"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">活跃预警</div>
                    <div class="card-value">{{ monitoringOverview.activeCount || 0 }}</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="overview-card total">
                <div class="card-content">
                  <div class="card-icon">
                    <i class="el-icon-data-analysis"></i>
                  </div>
                  <div class="card-info">
                    <div class="card-title">总预警数</div>
                    <div class="card-value">{{ monitoringOverview.totalCount || 0 }}</div>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <!-- 查询表单 -->
          <el-form :model="monitoringQuery" ref="monitoringQueryForm" size="small" :inline="true" v-show="monitoringShowSearch" label-width="68px">
            <el-form-item label="预警编号" prop="alertNo">
              <el-input
                v-model="monitoringQuery.alertNo"
                placeholder="请输入预警编号"
                clearable
                @keyup.enter.native="handleMonitoringQuery"
              />
            </el-form-item>
            <el-form-item label="公司名称" prop="companyName">
              <el-input
                v-model="monitoringQuery.companyName"
                placeholder="请输入公司名称"
                clearable
                @keyup.enter.native="handleMonitoringQuery"
              />
            </el-form-item>
            <el-form-item label="额度代码" prop="limitCode">
              <el-input
                v-model="monitoringQuery.limitCode"
                placeholder="请输入额度代码"
                clearable
                @keyup.enter.native="handleMonitoringQuery"
              />
            </el-form-item>
            <el-form-item label="预警类型" prop="alertType">
              <el-select v-model="monitoringQuery.alertType" placeholder="请选择预警类型" clearable>
                <el-option
                  v-for="dict in alertTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="预警级别" prop="alertLevel">
              <el-select v-model="monitoringQuery.alertLevel" placeholder="请选择预警级别" clearable>
                <el-option
                  v-for="dict in alertLevelOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="预警状态" prop="alertStatus">
              <el-select v-model="monitoringQuery.alertStatus" placeholder="请选择预警状态" clearable>
                <el-option
                  v-for="dict in alertStatusOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleMonitoringQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetMonitoringQuery">重置</el-button>
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
                @click="handleMonitoringAdd"
                v-hasPermi="['financing:creditMonitoring:add']"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="monitoringSingle"
                @click="handleMonitoringUpdate"
                v-hasPermi="['financing:creditMonitoring:edit']"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="monitoringMultiple"
                @click="handleMonitoringDelete"
                v-hasPermi="['financing:creditMonitoring:remove']"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-check"
                size="mini"
                :disabled="monitoringMultiple"
                @click="handleMonitoringBatchHandle"
                v-hasPermi="['financing:creditMonitoring:batchHandle']"
              >批量处理</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleMonitoringExport"
                v-hasPermi="['financing:creditMonitoring:export']"
              >导出</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-refresh"
                size="mini"
                @click="handleMonitoringGenerate"
                v-hasPermi="['financing:creditMonitoring:generate']"
              >生成预警</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="monitoringShowSearch" @queryTable="getMonitoringList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="monitoringLoading" :data="monitoringList" @selection-change="handleMonitoringSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="预警编号" align="center" prop="alertNo" />
            <el-table-column label="公司名称" align="center" prop="companyName" />
            <el-table-column label="额度代码" align="center" prop="limitCode">
              <template slot-scope="scope">
                <span>{{ scope.row.limitCode || scope.row.creditLimitId || '-' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="预警类型" align="center" prop="alertType">
              <template slot-scope="scope">
                <el-tag :type="getAlertTypeTagType(scope.row.alertType)">
                  {{ getAlertTypeLabel(scope.row.alertType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="预警级别" align="center" prop="alertLevel">
              <template slot-scope="scope">
                <el-tag :type="getAlertLevelTagType(scope.row.alertLevel)">
                  {{ getAlertLevelLabel(scope.row.alertLevel) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="预警内容" align="center" prop="alertContent" :show-overflow-tooltip="true" />
            <el-table-column label="预警日期" align="center" prop="alertDate" width="180">
              <template slot-scope="scope">
                <span>{{ formatAlertDate(scope.row) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="预警状态" align="center" prop="alertStatus">
              <template slot-scope="scope">
                <el-tag :type="getAlertStatusTagType(scope.row.alertStatus)">
                  {{ getAlertStatusLabel(scope.row.alertStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="处理人" align="center" prop="handlerName" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-dropdown @command="(command) => handleMonitoringCommand(command, scope.row)" size="mini">
                  <el-button type="primary" size="mini">
                    操作<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="edit">修改</el-dropdown-item>
                    <el-dropdown-item command="view">查看</el-dropdown-item>
                    <el-dropdown-item command="handle" v-if="scope.row.alertStatus === 'ACTIVE' || scope.row.alertStatus === 'PENDING'">处理</el-dropdown-item>
                    <el-dropdown-item command="close" v-if="scope.row.alertStatus === 'ACTIVE' || scope.row.alertStatus === 'HANDLED' || scope.row.alertStatus === 'PENDING'">关闭</el-dropdown-item>
                    <el-dropdown-item command="reactivate" v-if="scope.row.alertStatus === 'HANDLED' || scope.row.alertStatus === 'CLOSED'">重新激活</el-dropdown-item>
                    <el-dropdown-item command="delete">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="monitoringTotal > 0"
            :total="monitoringTotal"
            :page.sync="monitoringQuery.pageNum"
            :limit.sync="monitoringQuery.pageSize"
            @pagination="getMonitoringList"
          />
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改授信申请对话框 -->
    <el-dialog :title="applicationTitle" :visible.sync="applicationOpen" width="800px" append-to-body>
      <el-form ref="applicationForm" :model="applicationForm" :rules="applicationRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请机构" prop="companyId">
              <el-select v-model="applicationForm.companyId" placeholder="请选择申请机构">
                <el-option
                  v-for="org in orgOptions"
                  :key="org.orgId"
                  :label="org.orgName"
                  :value="org.orgId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="授信类型" prop="creditType">
              <el-select v-model="applicationForm.creditType" placeholder="请选择授信类型">
                <el-option label="流动资金贷款" value="WORKING_CAPITAL_LOAN" />
                <el-option label="固定资产贷款" value="FIXED_ASSET_LOAN" />
                <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
                <el-option label="信用证" value="LETTER_OF_CREDIT" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请金额" prop="creditAmount">
              <el-input v-model="applicationForm.creditAmount" placeholder="请输入申请金额">
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="授信期限" prop="creditPeriod">
              <el-input v-model="applicationForm.creditPeriod" placeholder="请输入授信期限">
                <template slot="append">月</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="期限单位" prop="periodUnit">
              <el-select v-model="applicationForm.periodUnit" placeholder="请选择期限单位">
                <el-option label="月" value="MONTH" />
                <el-option label="年" value="YEAR" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="applicationForm.currencyCode" placeholder="请选择币种">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="申请用途" prop="purpose">
          <el-input v-model="applicationForm.purpose" type="textarea" placeholder="请输入申请用途" />
        </el-form-item>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="applicationForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitApplicationForm">确 定</el-button>
        <el-button @click="cancelApplication">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改授信合同对话框 -->
    <el-dialog :title="contractTitle" :visible.sync="contractOpen" width="1000px" append-to-body>
      <el-form ref="contractForm" :model="contractForm" :rules="contractRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input v-model="contractForm.contractNo" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="授信申请ID" prop="creditApplicationId">
              <el-input v-model="contractForm.creditApplicationId" placeholder="请输入授信申请ID" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="银行代码" prop="bankCode">
              <el-input v-model="contractForm.bankCode" placeholder="请输入银行代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行名称" prop="bankName">
              <el-input v-model="contractForm.bankName" placeholder="请输入银行名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="授信额度" prop="creditLimit">
              <el-input v-model="contractForm.creditLimit" placeholder="请输入授信额度" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="contractForm.currencyCode" placeholder="请选择币种" clearable style="width: 100%">
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
            <el-form-item label="授信期限(月)" prop="creditPeriod">
              <el-input v-model="contractForm.creditPeriod" placeholder="请输入授信期限" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="利率(%)" prop="interestRate">
              <el-input v-model="contractForm.interestRate" placeholder="请输入利率" type="number" step="0.01" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="contractForm.startDate"
                type="date"
                placeholder="选择开始日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="contractForm.endDate"
                type="date"
                placeholder="选择结束日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="担保方式" prop="guaranteeMethod">
              <el-input v-model="contractForm.guaranteeMethod" placeholder="请输入担保方式" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同状态" prop="contractStatus">
              <el-select v-model="contractForm.contractStatus" placeholder="请选择合同状态" clearable style="width: 100%">
                <el-option label="草稿" value="DRAFT" />
                <el-option label="已签署" value="SIGNED" />
                <el-option label="生效中" value="EFFECTIVE" />
                <el-option label="已终止" value="TERMINATED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="公司ID" prop="companyId">
              <el-input v-model="contractForm.companyId" placeholder="请输入公司ID" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyName">
              <el-input v-model="contractForm.companyName" placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="合同文件路径" prop="contractFile">
          <el-input v-model="contractForm.contractFile" placeholder="请输入合同文件路径" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="contractForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitContractForm">确 定</el-button>
        <el-button @click="cancelContract">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 授信合同详情对话框 -->
    <el-dialog title="授信合同详情" :visible.sync="contractDetailOpen" width="1000px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="合同编号">{{ contractForm.contractNo }}</el-descriptions-item>
        <el-descriptions-item label="申请ID">{{ contractForm.applicationId }}</el-descriptions-item>
        <el-descriptions-item label="公司ID">{{ contractForm.companyId }}</el-descriptions-item>
        <el-descriptions-item label="融资机构ID">{{ contractForm.financingInstitutionId }}</el-descriptions-item>
        <el-descriptions-item label="授信类型">
          <el-tag :type="getCreditTypeTagType(contractForm.creditType)">
            {{ getCreditTypeLabel(contractForm.creditType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="授信金额">
          {{ formatAmount(contractForm.creditAmount, contractForm.currencyCode) }}
        </el-descriptions-item>
        <el-descriptions-item label="授信期限">{{ contractForm.creditTerm }} 个月</el-descriptions-item>
        <el-descriptions-item label="合同期限">
          {{ parseTime(contractForm.contractStartDate, '{y}-{m}-{d}') }} 至 {{ parseTime(contractForm.contractEndDate, '{y}-{m}-{d}') }}
        </el-descriptions-item>
        <el-descriptions-item label="担保方式">{{ getGuaranteeMethodLabel(contractForm.guaranteeMethod) }}</el-descriptions-item>
        <el-descriptions-item label="利率类型">{{ contractForm.interestRateType === 'FIXED' ? '固定利率' : '浮动利率' }}</el-descriptions-item>
        <el-descriptions-item label="基准利率">{{ contractForm.baseRate }}</el-descriptions-item>
        <el-descriptions-item label="利差">{{ contractForm.rateSpread }}%</el-descriptions-item>
        <el-descriptions-item label="承诺费率">{{ contractForm.commitmentFeeRate }}%</el-descriptions-item>
        <el-descriptions-item label="管理费率">{{ contractForm.managementFeeRate }}%</el-descriptions-item>
        <el-descriptions-item label="合同状态">
          <el-tag :type="getContractStatusTagType(contractForm.contractStatus)">
            {{ getContractStatusLabel(contractForm.contractStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ parseTime(contractForm.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
        </el-descriptions-item>
        <el-descriptions-item label="担保详情" :span="2">{{ contractForm.guaranteeDetails }}</el-descriptions-item>
        <el-descriptions-item label="特殊条款" :span="2">{{ contractForm.specialClauses }}</el-descriptions-item>
        <el-descriptions-item label="违约条款" :span="2">{{ contractForm.defaultClauses }}</el-descriptions-item>
        <el-descriptions-item label="合同文件路径" :span="2">{{ contractForm.contractFilePath }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ contractForm.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="contractDetailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改授信额度对话框 -->
    <el-dialog :title="limitTitle" :visible.sync="limitOpen" width="900px" append-to-body>
      <el-form ref="limitForm" :model="limitForm" :rules="limitRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="合同ID" prop="contractId">
              <el-input v-model="limitForm.contractId" placeholder="请输入合同ID" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司" prop="companyId">
              <el-select v-model="limitForm.companyId" placeholder="请选择公司" clearable @change="handleCompanyChange">
                <el-option
                  v-for="item in orgOptions"
                  :key="item.orgId"
                  :label="item.orgName"
                  :value="item.orgId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="额度类型" prop="limitType">
              <el-select v-model="limitForm.limitType" placeholder="请选择额度类型" clearable>
                <el-option
                  v-for="item in limitCreditTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="limitForm.currencyCode" placeholder="请选择币种" clearable>
                <el-option
                  v-for="item in currencyOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="总额度" prop="totalLimit">
              <el-input v-model="limitForm.totalLimit" placeholder="请输入总额度" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="利率(%)" prop="interestRate">
              <el-input v-model="limitForm.interestRate" placeholder="请输入利率" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="起始日期" prop="startDate">
              <el-date-picker
                v-model="limitForm.startDate"
                type="date"
                placeholder="选择起始日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="endDate">
              <el-date-picker
                v-model="limitForm.endDate"
                type="date"
                placeholder="选择到期日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="担保方式" prop="guaranteeMethod">
              <el-select v-model="limitForm.guaranteeMethod" placeholder="请选择担保方式" clearable>
                <el-option label="信用" value="CREDIT" />
                <el-option label="抵押" value="MORTGAGE" />
                <el-option label="质押" value="PLEDGE" />
                <el-option label="保证" value="GUARANTEE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="额度用途" prop="purpose">
              <el-input v-model="limitForm.purpose" placeholder="请输入额度用途" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="limitForm.remark" type="textarea" placeholder="请输入备注" :rows="3" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitLimitForm">确 定</el-button>
        <el-button @click="cancelLimit">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 授信额度详情对话框 -->
    <el-dialog title="授信额度详情" :visible.sync="limitDetailOpen" width="1000px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="额度代码">{{ limitForm.limitCode }}</el-descriptions-item>
        <el-descriptions-item label="合同ID">{{ limitForm.contractId }}</el-descriptions-item>
        <el-descriptions-item label="公司ID">{{ limitForm.companyId }}</el-descriptions-item>
        <el-descriptions-item label="融资机构ID">{{ limitForm.financingInstitutionId }}</el-descriptions-item>
        <el-descriptions-item label="授信类型">{{ getCreditTypeLabel(limitForm.creditType) }}</el-descriptions-item>
        <el-descriptions-item label="币种">{{ limitForm.currencyCode }}</el-descriptions-item>
        <el-descriptions-item label="总额度">{{ formatAmount(limitForm.totalLimit, limitForm.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="已用额度">{{ formatAmount(limitForm.usedLimit, limitForm.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="可用额度">{{ formatAmount(limitForm.availableLimit, limitForm.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="冻结额度">{{ formatAmount(limitForm.frozenLimit, limitForm.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="生效日期">{{ limitForm.effectiveDate }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ limitForm.expiryDate }}</el-descriptions-item>
        <el-descriptions-item label="利率类型">{{ limitForm.interestRateType === 'FIXED' ? '固定利率' : '浮动利率' }}</el-descriptions-item>
        <el-descriptions-item label="基准利率">{{ limitForm.baseRate }}</el-descriptions-item>
        <el-descriptions-item label="利率浮动">{{ limitForm.rateSpread }}%</el-descriptions-item>
        <el-descriptions-item label="承诺费率">{{ limitForm.commitmentFeeRate }}%</el-descriptions-item>
        <el-descriptions-item label="额度状态">{{ getLimitStatusLabel(limitForm.limitStatus) }}</el-descriptions-item>
        <el-descriptions-item label="最后使用日期">{{ limitForm.lastUsageDate }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(limitForm.createtime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(limitForm.updatetime) }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="limitDetailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 额度操作对话框 -->
    <el-dialog :title="limitOperationTitle" :visible.sync="limitOperationOpen" width="500px" append-to-body>
      <el-form ref="limitOperationForm" :model="limitOperationForm" :rules="limitOperationRules" label-width="100px">
        <el-form-item v-if="limitOperationType === 'use' || limitOperationType === 'repay' || limitOperationType === 'freeze' || limitOperationType === 'unfreeze'" label="金额" prop="amount">
          <el-input v-model="limitOperationForm.amount" placeholder="请输入金额" type="number" />
        </el-form-item>
        <el-form-item v-if="limitOperationType === 'use'" label="用途" prop="purpose">
          <el-input v-model="limitOperationForm.purpose" placeholder="请输入用途" type="textarea" />
        </el-form-item>
        <el-form-item v-if="limitOperationType === 'freeze' || limitOperationType === 'unfreeze' || limitOperationType === 'suspend' || limitOperationType === 'activate' || limitOperationType === 'cancel'" label="原因" prop="reason">
          <el-input v-model="limitOperationForm.reason" placeholder="请输入原因" type="textarea" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitLimitOperation">确 定</el-button>
        <el-button @click="cancelLimitOperation">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 授信评估新增/修改对话框 -->
    <el-dialog :title="assessmentTitle" :visible.sync="assessmentOpen" width="800px" append-to-body>
      <el-form ref="assessmentForm" :model="assessmentForm" :rules="assessmentRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="评估编号" prop="assessmentNo">
              <el-input v-model="assessmentForm.assessmentNo" placeholder="请输入评估编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司ID" prop="companyId">
              <el-input v-model="assessmentForm.companyId" placeholder="请输入公司ID" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="评估类型" prop="assessmentType">
              <el-select v-model="assessmentForm.assessmentType" placeholder="请选择评估类型">
                <el-option
                  v-for="dict in assessmentTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估日期" prop="assessmentDate">
              <el-date-picker
                v-model="assessmentForm.assessmentDate"
                type="date"
                placeholder="选择评估日期"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="信用评级" prop="creditRating">
              <el-select v-model="assessmentForm.creditRating" placeholder="请选择信用评级">
                <el-option
                  v-for="dict in creditRatingOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="assessmentForm.riskLevel" placeholder="请选择风险等级">
                <el-option
                  v-for="dict in riskLevelOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="建议额度" prop="recommendedLimit">
              <el-input v-model="assessmentForm.recommendedLimit" placeholder="请输入建议额度" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估人ID" prop="assessorId">
              <el-input v-model="assessmentForm.assessorId" placeholder="请输入评估人ID" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="财务指标" prop="financialIndicators">
          <el-input v-model="assessmentForm.financialIndicators" type="textarea" placeholder="请输入财务指标(JSON格式)" />
        </el-form-item>
        <el-form-item label="经营指标" prop="businessIndicators">
          <el-input v-model="assessmentForm.businessIndicators" type="textarea" placeholder="请输入经营指标(JSON格式)" />
        </el-form-item>
        <el-form-item label="风险指标" prop="riskIndicators">
          <el-input v-model="assessmentForm.riskIndicators" type="textarea" placeholder="请输入风险指标(JSON格式)" />
        </el-form-item>
        <el-form-item label="评估结论" prop="assessmentConclusion">
          <el-input v-model="assessmentForm.assessmentConclusion" type="textarea" placeholder="请输入评估结论" />
        </el-form-item>
        <el-form-item label="评估建议" prop="assessmentRecommendations">
          <el-input v-model="assessmentForm.assessmentRecommendations" type="textarea" placeholder="请输入评估建议" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAssessmentForm">确 定</el-button>
        <el-button @click="cancelAssessment">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 授信评估详情对话框 -->
    <el-dialog :title="assessmentTitle" :visible.sync="assessmentDetailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="评估编号">{{ assessmentForm.assessmentNo }}</el-descriptions-item>
        <el-descriptions-item label="公司ID">{{ assessmentForm.companyId }}</el-descriptions-item>
        <el-descriptions-item label="评估类型">{{ getAssessmentTypeLabel(assessmentForm.assessmentType) }}</el-descriptions-item>
        <el-descriptions-item label="评估日期">{{ assessmentForm.assessmentDate }}</el-descriptions-item>
        <el-descriptions-item label="信用评级">{{ getCreditRatingLabel(assessmentForm.creditRating) }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ getRiskLevelLabel(assessmentForm.riskLevel) }}</el-descriptions-item>
        <el-descriptions-item label="建议额度">{{ formatAmount(assessmentForm.recommendedLimit) }}</el-descriptions-item>
        <el-descriptions-item label="评估状态">{{ getAssessmentStatusLabel(assessmentForm.assessmentStatus) }}</el-descriptions-item>
        <el-descriptions-item label="评估人">{{ assessmentForm.assessorName }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(assessmentForm.createtime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(assessmentForm.updatetime) }}</el-descriptions-item>
        <el-descriptions-item label="财务指标" :span="2">{{ assessmentForm.financialIndicators }}</el-descriptions-item>
        <el-descriptions-item label="经营指标" :span="2">{{ assessmentForm.businessIndicators }}</el-descriptions-item>
        <el-descriptions-item label="风险指标" :span="2">{{ assessmentForm.riskIndicators }}</el-descriptions-item>
        <el-descriptions-item label="评估结论" :span="2">{{ assessmentForm.assessmentConclusion }}</el-descriptions-item>
        <el-descriptions-item label="评估建议" :span="2">{{ assessmentForm.assessmentRecommendations }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="assessmentDetailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 授信监控新增/修改对话框 -->
    <el-dialog :title="monitoringTitle" :visible.sync="monitoringOpen" width="800px" append-to-body>
      <el-form ref="monitoringForm" :model="monitoringForm" :rules="monitoringRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="预警编号" prop="alertNo">
              <el-input v-model="monitoringForm.alertNo" placeholder="请输入预警编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="额度代码" prop="limitCode">
              <el-input v-model="monitoringForm.limitCode" placeholder="请输入额度代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyName">
              <el-input v-model="monitoringForm.companyName" placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警标题" prop="alertTitle">
              <el-input v-model="monitoringForm.alertTitle" placeholder="请输入预警标题" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预警类型" prop="alertType">
              <el-select v-model="monitoringForm.alertType" placeholder="请选择预警类型">
                <el-option
                  v-for="dict in alertTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警级别" prop="alertLevel">
              <el-select v-model="monitoringForm.alertLevel" placeholder="请选择预警级别">
                <el-option
                  v-for="dict in alertLevelOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预警日期" prop="alertDate">
              <el-date-picker
                v-model="monitoringForm.alertDate"
                type="date"
                placeholder="选择预警日期"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警状态" prop="alertStatus">
              <el-select v-model="monitoringForm.alertStatus" placeholder="请选择预警状态">
                <el-option
                  v-for="dict in alertStatusOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="预警内容" prop="alertContent">
              <el-input v-model="monitoringForm.alertContent" type="textarea" :rows="3" placeholder="请输入预警内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="触发条件" prop="triggerConditions">
              <el-input v-model="monitoringForm.triggerConditions" type="textarea" :rows="2" placeholder="请输入触发条件" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="建议措施" prop="suggestedActions">
              <el-input v-model="monitoringForm.suggestedActions" type="textarea" :rows="2" placeholder="请输入建议措施" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMonitoringForm">确 定</el-button>
        <el-button @click="cancelMonitoring">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 授信监控详情对话框 -->
    <el-dialog title="预警详情" :visible.sync="monitoringDetailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="预警编号">{{ monitoringForm.alertNo }}</el-descriptions-item>
        <el-descriptions-item label="公司名称">{{ monitoringForm.companyName }}</el-descriptions-item>
        <el-descriptions-item label="额度代码">{{ monitoringForm.limitCode }}</el-descriptions-item>
        <el-descriptions-item label="预警类型">{{ getAlertTypeLabel(monitoringForm.alertType) }}</el-descriptions-item>
        <el-descriptions-item label="预警级别">{{ getAlertLevelLabel(monitoringForm.alertLevel) }}</el-descriptions-item>
        <el-descriptions-item label="预警日期">{{ monitoringForm.alertDate }}</el-descriptions-item>
        <el-descriptions-item label="预警状态">{{ getAlertStatusLabel(monitoringForm.alertStatus) }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ monitoringForm.handlerName }}</el-descriptions-item>
        <el-descriptions-item label="处理时间">{{ parseTime(monitoringForm.handleTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(monitoringForm.createtime) }}</el-descriptions-item>
        <el-descriptions-item label="预警内容" :span="2">{{ monitoringForm.alertContent }}</el-descriptions-item>
        <el-descriptions-item label="触发条件" :span="2">{{ monitoringForm.triggerConditions }}</el-descriptions-item>
        <el-descriptions-item label="建议措施" :span="2">{{ monitoringForm.suggestedActions }}</el-descriptions-item>
        <el-descriptions-item label="处理意见" :span="2">{{ monitoringForm.handleComments }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="monitoringDetailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 授信监控处理对话框 -->
    <el-dialog title="处理预警" :visible.sync="monitoringHandleOpen" width="600px" append-to-body>
      <el-form ref="monitoringHandleForm" :model="monitoringHandleForm" label-width="100px">
        <el-form-item label="处理人" prop="handlerId">
          <el-input v-model="monitoringHandleForm.handlerId" placeholder="请输入处理人ID" type="number" />
        </el-form-item>
        <el-form-item label="处理意见" prop="handleComments">
          <el-input v-model="monitoringHandleForm.handleComments" type="textarea" placeholder="请输入处理意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMonitoringHandleForm">确 定</el-button>
        <el-button @click="cancelMonitoringHandle">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCreditApplicationPage,
  getCreditApplication,
  createCreditApplication,
  updateCreditApplication,
  deleteCreditApplication,
  submitCreditApplication,
  // 授信合同管理API
  getCreditContractPage,
  getCreditContract,
  createCreditContract,
  updateCreditContract,
  deleteCreditContract,
  signCreditContract,
  terminateCreditContract,
  exportCreditContracts,
  // 授信额度管理API
  getCreditLimitPage,
  getCreditLimit,
  createCreditLimit,
  updateCreditLimit,
  deleteCreditLimit,
  batchDeleteCreditLimit,
  getCreditLimitByContract,
  getCreditLimitByCompany,
  useCreditLimit,
  repayCreditLimit,
  freezeCreditLimit,
  unfreezeCreditLimit,
  suspendCreditLimit,
  activateCreditLimit,
  cancelCreditLimit,
  getExpiringCreditLimits,
  getCreditLimitStatistics,
  exportCreditLimits,
  // 授信评估管理API
  getCreditAssessmentPage,
  getCreditAssessment,
  createCreditAssessment,
  updateCreditAssessment,
  deleteCreditAssessment,
  submitCreditAssessment,
  approveCreditAssessment,
  rejectCreditAssessment,
  withdrawCreditAssessment,
  copyCreditAssessment,
  getCreditAssessmentHistory,
  getCreditAssessmentStatistics,
  getHighRiskCreditAssessments,
  getExpiringCreditAssessments,
  exportCreditAssessments,
  performAutoCreditAssessment,
  // 授信监控管理API
  getCreditMonitoringPage,
  getCreditMonitoring,
  createCreditMonitoring,
  updateCreditMonitoring,
  deleteCreditMonitoring,
  handleCreditMonitoring,
  closeCreditMonitoring,
  reactivateCreditMonitoring,
  batchHandleCreditMonitoring,
  batchCloseCreditMonitoring,
  getActiveCreditMonitoring,
  getHighLevelCreditMonitoring,
  getCriticalCreditMonitoring,
  getUnhandledCreditMonitoring,
  getCreditMonitoringStatistics,
  getCreditMonitoringTypeStatistics,
  getCreditMonitoringLevelStatistics,
  getCreditMonitoringStatusStatistics,
  getCreditMonitoringTrend,
  getCreditMonitoringEfficiency,
  getCreditMonitoringQuality,
  exportCreditMonitoring,
  generateCreditMonitoringReport,
  batchGenerateCreditMonitoring,
  autoCloseCreditMonitoring
} from "@/api/globalTreasurer/rzgl";
import { parseTime } from '@/utils'
import { getOrgList } from '@/api/globalTreasurer/treasuryCommon'

export default {
  name: "CreditManagement",
  directives: {
    hasPermi: {
      inserted(el, binding) {
        const { value } = binding;
        const permissions = JSON.parse(sessionStorage.getItem('permissions') || '[]');
        if (value && value instanceof Array && value.length > 0) {
          const hasPermission = permissions.some(permission => {
            return value.includes(permission);
          });
          if (!hasPermission) {
            el.parentNode && el.parentNode.removeChild(el);
          }
        }
      }
    }
  },
  data() {
    return {
      // 当前激活的标签页
      activeTab: "application",
      // 显示搜索条件
      showSearch: true,
      
      // 授信申请相关数据
      applicationLoading: true,
      applicationIds: [],
      applicationSingle: true,
      applicationMultiple: true,
      applicationTotal: 0,
      applicationList: [],
      applicationTitle: "",
      applicationOpen: false,
      applicationQuery: {
        pageNum: 1,
        pageSize: 10,
        applicationNo: null,
        orgName: null,
        creditType: null,
        applicationStatus: null
      },
      applicationForm: {},
      applicationRules: {
        companyId: [
          { required: true, message: "申请机构不能为空", trigger: "change" }
        ],
        creditType: [
          { required: true, message: "授信类型不能为空", trigger: "change" }
        ],
        creditAmount: [
          { required: true, message: "申请金额不能为空", trigger: "blur" }
        ],
        creditPeriod: [
          { required: true, message: "授信期限不能为空", trigger: "blur" }
        ],
        periodUnit: [
          { required: true, message: "期限单位不能为空", trigger: "change" }
        ],
        currencyCode: [
          { required: true, message: "币种不能为空", trigger: "change" }
        ],
        purpose: [
          { required: true, message: "申请用途不能为空", trigger: "blur" }
        ]
      },
      
      // 字典选项
      creditTypeOptions: [
        { label: "流动资金贷款", value: "WORKING_CAPITAL_LOAN" },
        { label: "固定资产贷款", value: "FIXED_ASSET_LOAN" },
        { label: "银行承兑汇票", value: "BANK_ACCEPTANCE" },
        { label: "信用证", value: "LETTER_OF_CREDIT" }
      ],
      applicationStatusOptions: [
        { label: "草稿", value: "DRAFT" },
        { label: "待审批", value: "PENDING" },
        { label: "已审批", value: "APPROVED" },
        { label: "已拒绝", value: "REJECTED" }
      ],
      orgOptions: [],

      // 授信合同相关数据
      contractLoading: false,
      contractIds: [],
      contractSingle: true,
      contractMultiple: true,
      contractTotal: 0,
      contractList: [],
      contractTitle: "",
      contractOpen: false,
      contractDetailOpen: false,
      contractQuery: {
        pageNum: 1,
        pageSize: 10,
        contractNo: null,
        companyName: null,
        creditType: null,
        contractStatus: null,
        financingInstitution: null
      },
      contractForm: {},
      contractRules: {
        contractNo: [
          { required: true, message: "合同编号不能为空", trigger: "blur" }
        ],
        bankCode: [
          { required: true, message: "银行代码不能为空", trigger: "blur" }
        ],
        bankName: [
          { required: true, message: "银行名称不能为空", trigger: "blur" }
        ],
        creditLimit: [
          { required: true, message: "授信额度不能为空", trigger: "blur" }
        ],
        creditPeriod: [
          { required: true, message: "授信期限不能为空", trigger: "blur" }
        ],
        startDate: [
          { required: true, message: "开始日期不能为空", trigger: "change" }
        ],
        endDate: [
          { required: true, message: "结束日期不能为空", trigger: "change" }
        ],
        companyId: [
          { required: true, message: "公司ID不能为空", trigger: "blur" }
        ],
        companyName: [
          { required: true, message: "公司名称不能为空", trigger: "blur" }
        ]
      },

      // 授信合同字典选项
      contractCreditTypeOptions: [
        { label: "综合授信", value: "COMPREHENSIVE" },
        { label: "专项授信", value: "SPECIAL" },
        { label: "临时授信", value: "TEMPORARY" }
      ],
      contractStatusOptions: [
        { label: "有效", value: "EFFECTIVE" },
        { label: "已终止", value: "TERMINATED" },
        { label: "已到期", value: "EXPIRED" }
      ],
      guaranteeMethodOptions: [
        { label: "信用", value: "CREDIT" },
        { label: "保证", value: "GUARANTEE" },
        { label: "抵押", value: "MORTGAGE" },
        { label: "质押", value: "PLEDGE" }
      ],
      interestRateTypeOptions: [
        { label: "固定利率", value: "FIXED" },
        { label: "浮动利率", value: "FLOATING" }
      ],
      currencyOptions: [
        { label: "人民币", value: "CNY" },
        { label: "美元", value: "USD" },
        { label: "欧元", value: "EUR" },
        { label: "日元", value: "JPY" }
      ],
      companyOptions: [],
      financingInstitutionOptions: [],

      // 授信额度相关数据
      limitLoading: false,
      limitIds: [],
      limitSingle: true,
      limitMultiple: true,
      limitTotal: 0,
      limitList: [],
      limitTitle: "",
      limitOpen: false,
      limitDetailOpen: false,
      limitQuery: {
        pageNum: 1,
        pageSize: 10,
        limitNo: null,
        companyName: null,
        limitType: null,
        limitStatus: null
      },
      limitForm: {},
      limitRules: {
        contractId: [
          { required: true, message: "合同ID不能为空", trigger: "blur" }
        ],
        companyId: [
          { required: true, message: "公司不能为空", trigger: "change" }
        ],
        limitType: [
          { required: true, message: "额度类型不能为空", trigger: "change" }
        ],
        totalLimit: [
          { required: true, message: "总额度不能为空", trigger: "blur" }
        ],
        currencyCode: [
          { required: true, message: "币种不能为空", trigger: "change" }
        ],
        startDate: [
          { required: true, message: "起始日期不能为空", trigger: "change" }
        ],
        endDate: [
          { required: true, message: "到期日期不能为空", trigger: "change" }
        ]
      },

      // 额度操作相关数据
      limitOperationOpen: false,
      limitOperationTitle: "",
      limitOperationType: "",
      limitOperationForm: {},
      limitOperationRules: {
        amount: [
          { required: true, message: "金额不能为空", trigger: "blur" }
        ],
        purpose: [
          { required: true, message: "用途不能为空", trigger: "blur" }
        ],
        reason: [
          { required: true, message: "原因不能为空", trigger: "blur" }
        ]
      },

      // 授信额度字典选项
      limitCreditTypeOptions: [
        { label: "流动资金贷款", value: "WORKING_CAPITAL" },
        { label: "短期贷款", value: "SHORT_TERM" },
        { label: "贸易融资", value: "TRADE_FINANCE" },
        { label: "项目贷款", value: "PROJECT_LOAN" },
        { label: "信用证", value: "LETTER_OF_CREDIT" },
        { label: "票据贴现", value: "BILL_DISCOUNT" },
        { label: "保函", value: "GUARANTEE" }
      ],
      limitStatusOptions: [
        { label: "正常", value: "NORMAL" },
        { label: "冻结", value: "FROZEN" },
        { label: "暂停", value: "SUSPENDED" },
        { label: "已取消", value: "CANCELLED" }
      ],

      // ==================== 授信评估相关数据 ====================
      // 授信评估加载状态
      assessmentLoading: false,
      // 授信评估选中数组
      assessmentIds: [],
      // 非单个禁用
      assessmentSingle: true,
      // 非多个禁用
      assessmentMultiple: true,
      // 显示搜索条件
      assessmentShowSearch: true,
      // 总条数
      assessmentTotal: 0,
      // 授信评估表格数据
      assessmentList: [],
      // 弹出层标题
      assessmentTitle: "",
      // 是否显示弹出层
      assessmentOpen: false,
      // 是否显示详情弹出层
      assessmentDetailOpen: false,
      // 查询参数
      assessmentQuery: {
        pageNum: 1,
        pageSize: 10,
        assessmentNo: null,
        companyName: null,
        assessmentType: null,
        assessmentStatus: null,
        creditRating: null,
        riskLevel: null,
        assessorId: null,
        assessmentDateStart: null,
        assessmentDateEnd: null
      },
      // 表单参数
      assessmentForm: {},
      // 表单校验
      assessmentRules: {
        assessmentNo: [
          { required: true, message: "评估编号不能为空", trigger: "blur" }
        ],
        companyId: [
          { required: true, message: "公司不能为空", trigger: "change" }
        ],
        assessmentType: [
          { required: true, message: "评估类型不能为空", trigger: "change" }
        ],
        assessmentDate: [
          { required: true, message: "评估日期不能为空", trigger: "blur" }
        ],
        creditRating: [
          { required: true, message: "信用评级不能为空", trigger: "change" }
        ],
        riskLevel: [
          { required: true, message: "风险等级不能为空", trigger: "change" }
        ],
        recommendedLimit: [
          { required: true, message: "建议额度不能为空", trigger: "blur" },
          { type: 'number', message: '建议额度必须为数字值', trigger: 'blur' }
        ],
        assessmentConclusion: [
          { required: true, message: "评估结论不能为空", trigger: "blur" }
        ]
      },

      // 授信评估字典选项
      assessmentTypeOptions: [
        { label: "初始评估", value: "INITIAL" },
        { label: "定期评估", value: "PERIODIC" },
        { label: "专项评估", value: "SPECIAL" }
      ],
      assessmentStatusOptions: [
        { label: "草稿", value: "DRAFT" },
        { label: "已完成", value: "COMPLETED" },
        { label: "已批准", value: "APPROVED" }
      ],
      creditRatingOptions: [
        { label: "AAA级", value: "AAA" },
        { label: "AA级", value: "AA" },
        { label: "A级", value: "A" },
        { label: "BBB级", value: "BBB" },
        { label: "BB级", value: "BB" },
        { label: "B级", value: "B" },
        { label: "CCC级", value: "CCC" },
        { label: "CC级", value: "CC" },
        { label: "C级", value: "C" },
        { label: "D级", value: "D" }
      ],
      riskLevelOptions: [
        { label: "低风险", value: "LOW" },
        { label: "中风险", value: "MEDIUM" },
        { label: "高风险", value: "HIGH" },
        { label: "极高风险", value: "CRITICAL" }
      ],

      // ==================== 授信监控相关数据 ====================
      // 授信监控加载状态
      monitoringLoading: false,
      // 授信监控选中数组
      monitoringIds: [],
      // 非单个禁用
      monitoringSingle: true,
      // 非多个禁用
      monitoringMultiple: true,
      // 显示搜索条件
      monitoringShowSearch: true,
      // 总条数
      monitoringTotal: 0,
      // 授信监控表格数据
      monitoringList: [],
      // 弹出层标题
      monitoringTitle: "",
      // 是否显示弹出层
      monitoringOpen: false,
      // 是否显示详情弹出层
      monitoringDetailOpen: false,
      // 是否显示处理弹出层
      monitoringHandleOpen: false,
      // 监控概览数据
      monitoringOverview: {
        criticalCount: 0,
        highCount: 0,
        activeCount: 0,
        totalCount: 0
      },
      // 查询参数
      monitoringQuery: {
        pageNum: 1,
        pageSize: 10,
        alertNo: null,
        companyName: null,
        limitCode: null,
        alertType: null,
        alertLevel: null,
        alertStatus: null,
        handlerId: null,
        alertDateStart: null,
        alertDateEnd: null
      },
      // 表单参数
      monitoringForm: {},
      // 处理表单参数
      monitoringHandleForm: {
        handlerId: null,
        handleComments: null,
        action: 'handle' // handle, close, reactivate
      },
      // 表单校验
      monitoringRules: {
        alertNo: [
          { required: true, message: "预警编号不能为空", trigger: "blur" }
        ],
        limitId: [
          { required: true, message: "授信额度不能为空", trigger: "change" }
        ],
        alertType: [
          { required: true, message: "预警类型不能为空", trigger: "change" }
        ],
        alertLevel: [
          { required: true, message: "预警级别不能为空", trigger: "change" }
        ],
        alertContent: [
          { required: true, message: "预警内容不能为空", trigger: "blur" }
        ]
      },

      // 授信监控字典选项
      alertTypeOptions: [
        { label: "额度超限", value: "LIMIT_EXCEED" },
        { label: "使用率预警", value: "USAGE_RATIO" },
        { label: "到期预警", value: "EXPIRY_WARNING" },
        { label: "逾期预警", value: "OVERDUE" },
        { label: "风险预警", value: "RISK_ALERT" }
      ],
      alertLevelOptions: [
        { label: "低级", value: "LOW" },
        { label: "中级", value: "MEDIUM" },
        { label: "高级", value: "HIGH" },
        { label: "严重", value: "CRITICAL" }
      ],
      alertStatusOptions: [
        { label: "活跃", value: "ACTIVE" },
        { label: "待处理", value: "PENDING" },
        { label: "已处理", value: "HANDLED" },
        { label: "已关闭", value: "CLOSED" }
      ]
    };
  },
  created() {
    // 加载组织机构列表
    this.loadOrgOptions();

    // 根据当前激活的标签页初始化数据
    if (this.activeTab === 'application') {
      this.getApplicationList();
    } else if (this.activeTab === 'contract') {
      this.getContractList();
    } else if (this.activeTab === 'limit') {
      this.getLimitList();
    } else if (this.activeTab === 'assessment') {
      this.getAssessmentList();
    } else if (this.activeTab === 'monitoring') {
      this.getMonitoringList();
      this.getMonitoringOverview();
    } else {
      // 默认加载授信申请
      this.getApplicationList();
    }
  },
  methods: {
    parseTime,

    /** 加载组织机构选项 */
    async loadOrgOptions() {
      try {
        const response = await getOrgList();
        if (response && response.code === 1 && response.data) {
          // 根据实际返回的数据结构调整
          if (Array.isArray(response.data)) {
            this.orgOptions = response.data.map(org => ({
              orgId: org.id || org.orgId || org.companyId,
              orgName: org.name || org.orgName || org.companyName
            }));
          } else if (response.data.list && Array.isArray(response.data.list)) {
            this.orgOptions = response.data.list.map(org => ({
              orgId: org.id || org.orgId || org.companyId,
              orgName: org.name || org.orgName || org.companyName
            }));
          } else if (response.data.rows && Array.isArray(response.data.rows)) {
            this.orgOptions = response.data.rows.map(org => ({
              orgId: org.id || org.orgId || org.companyId,
              orgName: org.name || org.orgName || org.companyName
            }));
          }
        }
      } catch (error) {
        console.error('加载组织机构列表失败:', error);
        // 如果加载失败，使用模拟数据
        this.orgOptions = [
          { orgId: 1, orgName: '示例云科技有限公司' },
          { orgId: 2, orgName: '华东分公司' },
          { orgId: 3, orgName: '华南分公司' }
        ];
      }
    },

    /** 格式化金额 */
    formatAmount(amount, currency = 'CNY') {
      if (!amount) return '0.00';
      const currencyMap = {
        'CNY': '万元',
        'USD': '万美元',
        'EUR': '万欧元',
        'JPY': '万日元'
      };
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }) + ' ' + (currencyMap[currency] || '万元');
    },

    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'application') {
        this.getApplicationList();
      } else if (tab.name === 'contract') {
        this.getContractList();
      } else if (tab.name === 'limit') {
        this.getLimitList();
      } else if (tab.name === 'assessment') {
        this.getAssessmentList();
      } else if (tab.name === 'monitoring') {
        this.getMonitoringList();
        this.getMonitoringOverview();
      }
    },

    /** 查询授信申请列表 */
    getApplicationList() {
      this.applicationLoading = true;
      getCreditApplicationPage(this.applicationQuery).then(response => {
        try {
          // 支持多种响应格式
          if (response && response.data && response.data.rows) {
            // data.rows格式（当前后端返回格式）
            this.applicationList = response.data.rows;
            this.applicationTotal = response.data.total || 0;
          } else if (response && response.data && response.data.records) {
            // PageResult格式
            this.applicationList = response.data.records;
            this.applicationTotal = response.data.total || 0;
          } else if (response && response.data && Array.isArray(response.data)) {
            // 数组格式
            this.applicationList = response.data;
            this.applicationTotal = response.data.length;
          } else if (response && response.rows) {
            // rows格式
            this.applicationList = response.rows;
            this.applicationTotal = response.total || 0;
          } else if (Array.isArray(response)) {
            // 直接数组格式
            this.applicationList = response;
            this.applicationTotal = response.length;
          } else {
            // 默认空数据
            this.applicationList = [];
            this.applicationTotal = 0;
          }
        } catch (error) {
          console.error('解析授信申请列表数据失败', error);
          this.applicationList = [];
          this.applicationTotal = 0;
          this.$message.error('数据格式错误');
        } finally {
          this.applicationLoading = false;
        }
      }).catch(error => {
        console.error('查询授信申请列表失败', error);
        this.applicationList = [];
        this.applicationTotal = 0;
        this.applicationLoading = false;
        this.$message.error('查询失败，请稍后重试');
      });
    },

    /** 搜索按钮操作 */
    handleApplicationQuery() {
      this.applicationQuery.pageNum = 1;
      this.getApplicationList();
    },

    /** 重置按钮操作 */
    resetApplicationQuery() {
      this.resetForm("applicationQueryForm");
      this.handleApplicationQuery();
    },

    /** 多选框选中数据 */
    handleApplicationSelectionChange(selection) {
      this.applicationIds = selection.map(item => item.applicationId);
      this.applicationSingle = selection.length !== 1;
      this.applicationMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleApplicationAdd() {
      this.resetApplicationForm();
      this.applicationOpen = true;
      this.applicationTitle = "添加授信申请";
    },

    /** 修改按钮操作 */
    handleApplicationUpdate(row) {
      this.resetApplicationForm();
      const applicationId = row.applicationId || this.applicationIds;
      getCreditApplication(applicationId).then(response => {
        const data = response.data;
        // 字段映射：后端实体字段 -> 前端表单字段
        // 注意：后端实体类中没有 creditPeriod, periodUnit, purpose, guaranteeMethod, remark 字段
        this.applicationForm = {
          applicationId: data.applicationId,
          companyId: data.companyId,
          companyName: data.companyName,
          creditType: data.creditType,
          creditAmount: data.applyAmount,  // 后端实体字段：applyAmount
          currencyCode: data.currencyCode || 'CNY',
          creditPeriod: null,  // 后端实体类中没有此字段
          periodUnit: 'MONTH',  // 默认值
          purpose: '',  // 后端实体类中没有此字段
          guaranteeMethod: '',  // 后端实体类中没有此字段
          remark: ''  // 后端实体类中没有此字段
        };
        this.applicationOpen = true;
        this.applicationTitle = "修改授信申请";
      });
    },

    /** 查看按钮操作 */
    handleApplicationView(row) {
      // 跳转到详情页面
      this.$router.push(`/globalTreasurer/financing/credit/detail/${row.applicationId}`);
    },

    /** 提交按钮 */
    submitApplicationForm() {
      this.$refs["applicationForm"].validate(valid => {
        if (valid) {
          // 根据companyId查找companyName
          const selectedOrg = this.orgOptions.find(org => org.orgId === this.applicationForm.companyId);
          if (selectedOrg) {
            this.applicationForm.companyName = selectedOrg.orgName;
          }

          if (this.applicationForm.applicationId != null) {
            updateCreditApplication(this.applicationForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.applicationOpen = false;
              this.getApplicationList();
            });
          } else {
            createCreditApplication(this.applicationForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.applicationOpen = false;
              this.getApplicationList();
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleApplicationDelete(row) {
      const applicationIds = row.applicationId || this.applicationIds;
      this.$modal.confirm('是否确认删除授信申请编号为"' + applicationIds + '"的数据项？').then(function() {
        return deleteCreditApplication(applicationIds);
      }).then(() => {
        this.getApplicationList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 提交审批操作 */
    handleApplicationSubmit(row) {
      const applicationIds = row.applicationId || this.applicationIds;
      this.$modal.confirm('是否确认提交审批？').then(function() {
        return submitCreditApplication(applicationIds);
      }).then(() => {
        this.getApplicationList();
        this.$modal.msgSuccess("提交成功");
      }).catch(() => {});
    },

    /** 取消按钮 */
    cancelApplication() {
      this.applicationOpen = false;
      this.resetApplicationForm();
    },

    /** 表单重置 */
    resetApplicationForm() {
      this.applicationForm = {
        applicationId: null,
        companyId: null,
        creditType: null,
        creditAmount: null,
        creditPeriod: null,
        periodUnit: 'MONTH',
        currencyCode: 'CNY',
        purpose: null,
        remark: null
      };
      this.resetForm("applicationForm");
    },

    // ==================== 授信合同管理方法 ====================

    /** 查询授信合同列表 */
    async getContractList() {
      this.contractLoading = true;
      try {
        const response = await getCreditContractPage(this.contractQuery);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          // 支持多种响应格式
          if (response.data && response.data.tlist) {
            // 示例云标准格式：{ code: 1, data: { tlist: [], totalRecord: 0 } }
            this.contractList = response.data.tlist || [];
            this.contractTotal = response.data.totalRecord || 0;
          } else if (response.data && response.data.records) {
            // PageInfo格式
            this.contractList = response.data.records || [];
            this.contractTotal = response.data.total || 0;
          } else if (response.rows) {
            // 标准格式
            this.contractList = response.rows || [];
            this.contractTotal = response.total || 0;
          } else if (Array.isArray(response.data)) {
            // 数组格式
            this.contractList = response.data || [];
            this.contractTotal = response.data.length || 0;
          } else {
            this.contractList = [];
            this.contractTotal = 0;
          }
        } else {
          this.contractList = [];
          this.contractTotal = 0;
          console.warn('获取授信合同列表失败:', response.message);
        }
      } catch (error) {
        console.error('获取授信合同列表失败:', error);
        this.contractList = [];
        this.contractTotal = 0;
        // 不显示错误提示，保证前端正常展示
      } finally {
        this.contractLoading = false;
      }
    },

    /** 搜索按钮操作 */
    handleContractQuery() {
      this.contractQuery.pageNum = 1;
      this.getContractList();
    },

    /** 重置按钮操作 */
    resetContractQuery() {
      this.resetForm("contractQueryForm");
      this.handleContractQuery();
    },

    /** 多选框选中数据 */
    handleContractSelectionChange(selection) {
      this.contractIds = selection.map(item => item.contractId);
      this.contractSingle = selection.length !== 1;
      this.contractMultiple = !selection.length;
    },

    /** 日期格式化工具方法 */
    formatDateValue(dateValue) {
      if (!dateValue) return null;
      // 如果是时间戳
      if (typeof dateValue === 'number') {
        const date = new Date(dateValue);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
      }
      // 如果已经是字符串格式
      if (typeof dateValue === 'string') {
        return dateValue.split(' ')[0]; // 取日期部分
      }
      return null;
    },

    /** 日期格式化方法 - 用于日期选择器 */
    formatDateForPicker(dateValue) {
      if (!dateValue) return null;
      // 处理数字时间戳（毫秒）
      if (typeof dateValue === 'number') {
        const date = new Date(dateValue);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
      }
      // 处理 Date 对象
      if (dateValue instanceof Date) {
        const year = dateValue.getFullYear();
        const month = String(dateValue.getMonth() + 1).padStart(2, '0');
        const day = String(dateValue.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
      }
      // 处理字符串日期
      if (typeof dateValue === 'string') {
        // 如果包含时间部分，只取日期部分
        if (dateValue.includes(' ')) {
          return dateValue.split(' ')[0];
        }
        // 如果包含 T（ISO格式），只取日期部分
        if (dateValue.includes('T')) {
          return dateValue.split('T')[0];
        }
        // 已经是 yyyy-MM-dd 格式，直接返回
        return dateValue;
      }
      return null;
    },

    /** 新增按钮操作 */
    handleContractAdd() {
      this.resetContractForm();
      // 自动生成合同编号
      this.contractForm.contractNo = 'CREDIT' + Date.now();
      this.contractForm.orgId = 1; // 设置默认组织ID
      this.contractOpen = true;
      this.contractTitle = "添加授信合同";
    },

    /** 修改按钮操作 */
    async handleContractUpdate(row) {
      this.resetContractForm();

      // 如果没有传递row参数（工具栏按钮），则从选中的行中获取
      if (!row) {
        if (this.contractIds.length === 0) {
          this.$modal.msgWarning('请选择要修改的合同');
          return;
        }

        console.log('选中的合同IDs:', this.contractIds);
        console.log('合同列表:', this.contractList);

        // 从列表中找到选中的第一条记录
        row = this.contractList.find(item => item.contractId === this.contractIds[0]);

        console.log('找到的记录:', row);

        if (!row) {
          this.$modal.msgError('未找到选中的合同记录，请刷新页面后重试');
          return;
        }
      }

      const contractId = row ? row.contractId : null;
      if (!contractId) {
        this.$modal.msgError('合同ID不能为空');
        console.error('row对象:', row);
        return;
      }

      try {
        const response = await getCreditContract(contractId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          const data = response.data || {};

          // 字段映射：后端实体字段 -> 前端表单字段
          this.contractForm = {
            contractId: data.contractId,
            contractNo: data.contractNo,
            creditApplicationId: data.creditApplicationId,
            bankCode: data.bankCode,
            bankName: data.bankName,
            creditLimit: data.creditLimit,
            currencyCode: data.currencyCode || 'CNY',
            creditPeriod: data.creditPeriod,
            startDate: this.formatDateValue(data.startDate),
            endDate: this.formatDateValue(data.endDate),
            interestRate: data.interestRate,
            guaranteeMethod: data.guaranteeMethod,
            contractStatus: data.contractStatus || 'DRAFT',
            signingDate: this.formatDateValue(data.signingDate),
            effectiveDate: this.formatDateValue(data.effectiveDate),
            terminationDate: this.formatDateValue(data.terminationDate),
            usedAmount: data.usedAmount,
            availableAmount: data.availableAmount,
            contractFile: data.contractFile,
            signedBy: data.signedBy,
            signedByName: data.signedByName,
            signedAt: this.formatDateValue(data.signedAt),
            companyId: data.companyId,
            companyName: data.companyName,
            remark: data.remark
          };
          this.contractOpen = true;
          this.contractTitle = "修改授信合同";
        } else {
          this.$modal.msgError(response.message || '获取合同信息失败');
        }
      } catch (error) {
        console.error('获取授信合同失败:', error);
        this.$modal.msgError('获取合同信息失败，请稍后重试');
      }
    },

    /** 查看按钮操作 */
    async handleContractView(row) {
      try {
        const response = await getCreditContract(row.contractId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.contractForm = response.data || {};
          this.contractDetailOpen = true;
        } else {
          this.$modal.msgError(response.message || '获取合同详情失败');
        }
      } catch (error) {
        console.error('获取授信合同详情失败:', error);
        this.$modal.msgError('获取合同详情失败，请稍后重试');
      }
    },

    /** 删除按钮操作 */
    handleContractDelete(row) {
      // 如果没有传递row参数（工具栏按钮），则使用选中的记录
      const contractIds = (row && row.contractId) ? [row.contractId] : this.contractIds;

      if (contractIds.length === 0) {
        this.$modal.msgWarning('请选择要删除的合同');
        return;
      }

      const contractNos = (row && row.contractNo) ? row.contractNo : contractIds.join(',');
      this.$modal.confirm('是否确认删除授信合同编号为"' + contractNos + '"的数据项？').then(() => {
        return this.deleteContractData(contractIds);
      }).then(() => {
        this.getContractList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
        console.log('用户取消删除操作');
      });
    },

    /** 签署合同操作 */
    handleContractSign(row) {
      this.$modal.confirm('是否确认签署合同编号为"' + row.contractNo + '"的授信合同？').then(async () => {
        try {
          const response = await signCreditContract(row.contractId);
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$modal.msgSuccess("签署成功");
            this.getContractList();
          } else {
            this.$modal.msgError(response.message || '签署失败');
          }
        } catch (error) {
          console.error('签署授信合同失败:', error);
          this.$modal.msgError('签署失败，请稍后重试');
        }
      }).catch(() => {
        console.log('用户取消签署操作');
      });
    },

    /** 终止合同操作 */
    handleContractTerminate(row) {
      this.$prompt('请输入终止原因', '终止合同', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '终止原因不能为空'
      }).then(async ({ value }) => {
        try {
          const response = await terminateCreditContract(row.contractId, value);
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$modal.msgSuccess("终止成功");
            this.getContractList();
          } else {
            this.$modal.msgError(response.message || '终止失败');
          }
        } catch (error) {
          console.error('终止授信合同失败:', error);
          this.$modal.msgError('终止失败，请稍后重试');
        }
      }).catch(() => {
        console.log('用户取消终止操作');
      });
    },

    /** 导出按钮操作 */
    async handleContractExport() {
      try {
        this.$modal.loading("正在导出数据...");
        const response = await exportCreditContracts(this.contractQuery);

        // 创建下载链接
        const blob = new Blob([response], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = '授信合同列表.xlsx';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);

        this.$modal.closeLoading();
        this.$modal.msgSuccess("导出成功");
      } catch (error) {
        this.$modal.closeLoading();
        console.error('导出失败:', error);
        this.$modal.msgError("导出失败，请稍后重试");
      }
    },

    /** 提交按钮 */
    submitContractForm() {
      this.$refs["contractForm"].validate(valid => {
        if (valid) {
          if (this.contractForm.contractId != null) {
            this.updateContractData();
          } else {
            this.addContractData();
          }
        }
      });
    },

    /** 新增授信合同 */
    async addContractData() {
      try {
        const response = await createCreditContract(this.contractForm);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("新增成功");
          this.contractOpen = false;
          this.getContractList();
        } else {
          this.$modal.msgError(response.message || '新增失败');
        }
      } catch (error) {
        console.error('新增授信合同失败:', error);
        this.$modal.msgError('新增失败，请稍后重试');
      }
    },

    /** 修改授信合同 */
    async updateContractData() {
      try {
        const response = await updateCreditContract(this.contractForm.contractId, this.contractForm);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("修改成功");
          this.contractOpen = false;
          this.getContractList();
        } else {
          this.$modal.msgError(response.message || '修改失败');
        }
      } catch (error) {
        console.error('修改授信合同失败:', error);
        this.$modal.msgError('修改失败，请稍后重试');
      }
    },

    /** 删除授信合同 */
    async deleteContractData(contractIds) {
      try {
        // 如果是数组，循环删除每个合同
        if (Array.isArray(contractIds)) {
          for (const contractId of contractIds) {
            await deleteCreditContract(contractId);
          }
          return Promise.resolve();
        } else {
          // 单个删除
          const response = await deleteCreditContract(contractIds);
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            return Promise.resolve();
          } else {
            return Promise.reject(new Error(response.message || '删除失败'));
          }
        }
      } catch (error) {
        console.error('删除授信合同失败:', error);
        return Promise.reject(error);
      }
    },

    /** 取消按钮 */
    cancelContract() {
      this.contractOpen = false;
      this.resetContractForm();
    },

    /** 表单重置 */
    resetContractForm() {
      this.contractForm = {
        contractId: null,
        contractNo: null,
        creditApplicationId: null,
        bankCode: null,
        bankName: null,
        creditLimit: null,
        currencyCode: 'CNY',
        creditPeriod: null,
        startDate: null,
        endDate: null,
        interestRate: null,
        guaranteeMethod: null,
        contractStatus: 'DRAFT',
        signingDate: null,
        effectiveDate: null,
        terminationDate: null,
        usedAmount: 0,
        availableAmount: 0,
        contractFile: null,
        signedBy: null,
        signedByName: null,
        signedAt: null,
        companyId: null,
        companyName: null,
        remark: null
      };
      this.resetForm("contractForm");
    },

    // ==================== 授信额度管理方法 ====================

    /** 查询授信额度列表 */
    async getLimitList() {
      this.limitLoading = true;
      try {
        const response = await getCreditLimitPage(this.limitQuery);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          // 支持多种响应格式
          if (response.data && response.data.tlist) {
            // 示例云标准格式：{ code: 1, data: { tlist: [], totalRecord: 0 } }
            this.limitList = response.data.tlist || [];
            this.limitTotal = response.data.totalRecord || 0;
          } else if (response.data && response.data.records) {
            // MyBatis-Plus分页格式
            this.limitList = response.data.records || [];
            this.limitTotal = response.data.total || 0;
          } else if (response.data && response.data.list) {
            // PageInfo格式
            this.limitList = response.data.list || [];
            this.limitTotal = response.data.total || 0;
          } else if (response.rows) {
            // 简单格式
            this.limitList = response.rows || [];
            this.limitTotal = response.total || 0;
          } else if (Array.isArray(response.data)) {
            // 数组格式
            this.limitList = response.data || [];
            this.limitTotal = response.data.length || 0;
          } else {
            this.limitList = [];
            this.limitTotal = 0;
          }
          console.log('授信额度列表加载成功, 共' + this.limitTotal + '条记录');
          console.log('列表数据示例:', this.limitList[0]); // 打印第一条数据查看字段名
        } else {
          this.limitList = [];
          this.limitTotal = 0;
          console.warn('获取授信额度列表失败:', response.message);
        }
      } catch (error) {
        console.error('获取授信额度列表失败:', error);
        this.limitList = [];
        this.limitTotal = 0;
        // 不显示错误提示，保证前端正常展示
      } finally {
        this.limitLoading = false;
      }
    },

    /** 搜索按钮操作 */
    handleLimitQuery() {
      this.limitQuery.pageNum = 1;
      this.getLimitList();
    },

    /** 重置按钮操作 */
    resetLimitQuery() {
      this.resetForm("limitQueryForm");
      this.handleLimitQuery();
    },

    /** 多选框选中数据 */
    handleLimitSelectionChange(selection) {
      console.log('选中的数据:', selection);
      // 兼容多种字段名格式
      this.limitIds = selection.map(item => item.limitId || item.limit_id || item.LIMIT_ID || item.id);
      console.log('提取的limitIds:', this.limitIds);
      this.limitSingle = selection.length !== 1;
      this.limitMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleLimitAdd() {
      this.resetLimitForm();
      this.limitOpen = true;
      this.limitTitle = "添加授信额度";
    },

    /** 修改按钮操作 */
    async handleLimitUpdate(row) {
      this.resetLimitForm();
      // 兼容多种字段名格式
      let limitId;
      if (row) {
        limitId = row.limitId || row.limit_id || row.LIMIT_ID || row.id;
        console.log('从行数据获取limitId:', limitId, '原始row:', row);
      } else {
        limitId = this.limitIds[0];
        console.log('从选中列表获取limitId:', limitId, '完整列表:', this.limitIds);
      }
      if (!limitId) {
        this.$modal.msgWarning('请先选择要修改的数据');
        return;
      }
      try {
        const response = await getCreditLimit(limitId);
        console.log('获取授信额度详情响应:', response);
        console.log('响应类型:', typeof response);
        console.log('response.data:', response.data);
        console.log('response.data类型:', typeof response.data);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          // 处理可能的嵌套数据结构
          let formData = response.data;
          // 如果data是字符串，尝试解析
          if (typeof formData === 'string') {
            try {
              formData = JSON.parse(formData);
            } catch (e) {
              console.warn('data字段解析失败:', e);
            }
          }
          // 如果data中还有data字段（嵌套结构）
          if (formData && formData.data) {
            formData = formData.data;
          }
          console.log('最终表单数据:', formData);
          console.log('startDate原始值:', formData.startDate, '类型:', typeof formData.startDate);
          console.log('endDate原始值:', formData.endDate, '类型:', typeof formData.endDate);

          // 处理日期格式转换（后端可能返回时间戳或Date对象）
          if (formData.startDate) {
            formData.startDate = this.formatDateForPicker(formData.startDate);
          }
          if (formData.endDate) {
            formData.endDate = this.formatDateForPicker(formData.endDate);
          }
          console.log('转换后 startDate:', formData.startDate);
          console.log('转换后 endDate:', formData.endDate);

          this.limitForm = formData || {};
          this.limitOpen = true;
          this.limitTitle = "修改授信额度";
        } else {
          this.$modal.msgError(response.message || response.msg || '获取额度信息失败');
        }
      } catch (error) {
        console.error('获取授信额度失败:', error);
        this.$modal.msgError('获取额度信息失败，请稍后重试');
      }
    },

    /** 查看按钮操作 */
    async handleLimitView(row) {
      // 兼容多种字段名格式
      const limitId = row ? (row.limitId || row.limit_id || row.LIMIT_ID || row.id) : null;
      console.log('查看操作 - limitId:', limitId, '原始row:', row);
      if (!limitId) {
        this.$modal.msgWarning('请先选择要查看的数据');
        return;
      }
      try {
        const response = await getCreditLimit(limitId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.limitForm = response.data || {};
          this.limitDetailOpen = true;
        } else {
          this.$modal.msgError(response.message || response.msg || '获取额度详情失败');
        }
      } catch (error) {
        console.error('获取授信额度详情失败:', error);
        this.$modal.msgError('获取额度详情失败，请稍后重试');
      }
    },

    /** 删除按钮操作 */
    handleLimitDelete(row) {
      // 兼容多种字段名格式
      let limitIds;
      let limitNos;
      if (row) {
        const id = row.limitId || row.limit_id || row.LIMIT_ID || row.id;
        limitIds = [id];
        limitNos = row.limitNo || row.limit_no || row.LIMIT_NO || id;
      } else {
        limitIds = this.limitIds;
        limitNos = limitIds.join(',');
      }
      console.log('删除操作 - limitIds:', limitIds);
      if (!limitIds || limitIds.length === 0 || !limitIds[0]) {
        this.$modal.msgWarning('请先选择要删除的数据');
        return;
      }
      this.$modal.confirm('是否确认删除授信额度代码为"' + limitNos + '"的数据项？').then(() => {
        return this.deleteLimitData(limitIds);
      }).then(() => {
        this.getLimitList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
        console.log('用户取消删除操作');
      });
    },

    /** 导出按钮操作 */
    handleLimitExport() {
      this.$modal.confirm('是否确认导出所有授信额度数据项？').then(() => {
        this.limitLoading = true;
        return exportCreditLimits(this.limitQuery);
      }).then(response => {
        this.$download.excel(response, '授信额度数据.xlsx');
        this.limitLoading = false;
      }).catch(() => {
        this.limitLoading = false;
      });
    },

    /** 额度操作命令处理 */
    handleLimitCommand(command, row) {
      switch (command) {
        case 'edit':
          this.handleLimitUpdate(row);
          break;
        case 'view':
          this.handleLimitView(row);
          break;
        case 'use':
          this.handleLimitUse(row);
          break;
        case 'repay':
          this.handleLimitRepay(row);
          break;
        case 'freeze':
          this.handleLimitFreeze(row);
          break;
        case 'unfreeze':
          this.handleLimitUnfreeze(row);
          break;
        case 'suspend':
          this.handleLimitSuspend(row);
          break;
        case 'activate':
          this.handleLimitActivate(row);
          break;
        case 'cancel':
          this.handleLimitCancel(row);
          break;
        case 'delete':
          this.handleLimitDelete(row);
          break;
      }
    },

    /** 使用额度操作 */
    handleLimitUse(row) {
      const limitId = row ? (row.limitId || row.limit_id || row.LIMIT_ID || row.id) : null;
      if (!limitId) {
        this.$modal.msgWarning('请先选择要操作的数据');
        return;
      }
      this.limitOperationType = 'use';
      this.limitOperationTitle = '使用额度';
      this.limitOperationForm = { limitId: limitId, amount: null, purpose: null };
      this.limitOperationOpen = true;
    },

    /** 归还额度操作 */
    handleLimitRepay(row) {
      const limitId = row ? (row.limitId || row.limit_id || row.LIMIT_ID || row.id) : null;
      if (!limitId) {
        this.$modal.msgWarning('请先选择要操作的数据');
        return;
      }
      this.limitOperationType = 'repay';
      this.limitOperationTitle = '归还额度';
      this.limitOperationForm = { limitId: limitId, amount: null };
      this.limitOperationOpen = true;
    },

    /** 冻结额度操作 */
    handleLimitFreeze(row) {
      const limitId = row ? (row.limitId || row.limit_id || row.LIMIT_ID || row.id) : null;
      if (!limitId) {
        this.$modal.msgWarning('请先选择要操作的数据');
        return;
      }
      this.limitOperationType = 'freeze';
      this.limitOperationTitle = '冻结额度';
      this.limitOperationForm = { limitId: limitId, amount: null, reason: null };
      this.limitOperationOpen = true;
    },

    /** 解冻额度操作 */
    handleLimitUnfreeze(row) {
      const limitId = row ? (row.limitId || row.limit_id || row.LIMIT_ID || row.id) : null;
      if (!limitId) {
        this.$modal.msgWarning('请先选择要操作的数据');
        return;
      }
      this.limitOperationType = 'unfreeze';
      this.limitOperationTitle = '解冻额度';
      this.limitOperationForm = { limitId: limitId, amount: null, reason: null };
      this.limitOperationOpen = true;
    },

    /** 暂停额度操作 */
    handleLimitSuspend(row) {
      const limitId = row ? (row.limitId || row.limit_id || row.LIMIT_ID || row.id) : null;
      if (!limitId) {
        this.$modal.msgWarning('请先选择要操作的数据');
        return;
      }
      this.limitOperationType = 'suspend';
      this.limitOperationTitle = '暂停额度';
      this.limitOperationForm = { limitId: limitId, reason: null };
      this.limitOperationOpen = true;
    },

    /** 激活额度操作 */
    handleLimitActivate(row) {
      const limitId = row ? (row.limitId || row.limit_id || row.LIMIT_ID || row.id) : null;
      if (!limitId) {
        this.$modal.msgWarning('请先选择要操作的数据');
        return;
      }
      this.limitOperationType = 'activate';
      this.limitOperationTitle = '激活额度';
      this.limitOperationForm = { limitId: limitId, reason: null };
      this.limitOperationOpen = true;
    },

    /** 取消额度操作 */
    handleLimitCancel(row) {
      const limitId = row ? (row.limitId || row.limit_id || row.LIMIT_ID || row.id) : null;
      if (!limitId) {
        this.$modal.msgWarning('请先选择要操作的数据');
        return;
      }
      this.limitOperationType = 'cancel';
      this.limitOperationTitle = '取消额度';
      this.limitOperationForm = { limitId: limitId, reason: null };
      this.limitOperationOpen = true;
    },

    /** 提交额度操作 */
    async submitLimitOperation() {
      this.$refs["limitOperationForm"].validate(async (valid) => {
        if (valid) {
          try {
            let response;
            const { limitId, amount, purpose, reason } = this.limitOperationForm;

            switch (this.limitOperationType) {
              case 'use':
                response = await useCreditLimit(limitId, amount, purpose);
                break;
              case 'repay':
                response = await repayCreditLimit(limitId, amount);
                break;
              case 'freeze':
                response = await freezeCreditLimit(limitId, amount, reason);
                break;
              case 'unfreeze':
                response = await unfreezeCreditLimit(limitId, amount, reason);
                break;
              case 'suspend':
                response = await suspendCreditLimit(limitId, reason);
                break;
              case 'activate':
                response = await activateCreditLimit(limitId, reason);
                break;
              case 'cancel':
                response = await cancelCreditLimit(limitId, reason);
                break;
            }

            const successCodes = [200, 0, '200', '0', '1', 1, 2];
            if (successCodes.includes(response.code)) {
              this.$modal.msgSuccess("操作成功");
              this.limitOperationOpen = false;
              this.getLimitList();
            } else {
              this.$modal.msgError(response.message || '操作失败');
            }
          } catch (error) {
            console.error('额度操作失败:', error);
            this.$modal.msgError('操作失败，请稍后重试');
          }
        }
      });
    },

    /** 取消额度操作 */
    cancelLimitOperation() {
      this.limitOperationOpen = false;
      this.limitOperationForm = {};
    },

    /** 提交按钮 */
    submitLimitForm() {
      this.$refs["limitForm"].validate(valid => {
        if (valid) {
          if (this.limitForm.limitId != null) {
            this.updateLimitData();
          } else {
            this.addLimitData();
          }
        }
      });
    },

    /** 新增授信额度 */
    async addLimitData() {
      try {
        const response = await createCreditLimit(this.limitForm);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("新增成功");
          this.limitOpen = false;
          this.getLimitList();
        } else {
          this.$modal.msgError(response.message || response.msg || '新增失败');
        }
      } catch (error) {
        console.error('新增授信额度失败:', error);
        this.$modal.msgError('新增失败，请稍后重试');
      }
    },

    /** 修改授信额度 */
    async updateLimitData() {
      try {
        const response = await updateCreditLimit(this.limitForm.limitId, this.limitForm);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("修改成功");
          this.limitOpen = false;
          this.getLimitList();
        } else {
          this.$modal.msgError(response.message || '修改失败');
        }
      } catch (error) {
        console.error('修改授信额度失败:', error);
        this.$modal.msgError('修改失败，请稍后重试');
      }
    },

    /** 删除授信额度 */
    async deleteLimitData(limitIds) {
      try {
        let response;
        // 如果是数组且长度大于1，使用批量删除接口
        if (Array.isArray(limitIds) && limitIds.length > 1) {
          response = await batchDeleteCreditLimit(limitIds);
        } else {
          // 单个删除
          const id = Array.isArray(limitIds) ? limitIds[0] : limitIds;
          response = await deleteCreditLimit(id);
        }
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          return Promise.resolve();
        } else {
          return Promise.reject(new Error(response.message || response.msg || '删除失败'));
        }
      } catch (error) {
        console.error('删除授信额度失败:', error);
        return Promise.reject(error);
      }
    },

    /** 取消按钮 */
    cancelLimit() {
      this.limitOpen = false;
      this.resetLimitForm();
    },

    /** 表单重置 */
    resetLimitForm() {
      this.limitForm = {
        limitId: null,
        contractId: null,
        companyId: null,
        companyName: null,
        limitType: null,
        totalLimit: null,
        currencyCode: 'CNY',
        startDate: null,
        endDate: null,
        interestRate: null,
        guaranteeMethod: null,
        purpose: null,
        remark: null
      };
      this.resetForm("limitForm");
    },

    /** 公司选择变化处理 */
    handleCompanyChange(companyId) {
      const selectedOrg = this.orgOptions.find(org => org.orgId === companyId);
      if (selectedOrg) {
        this.limitForm.companyName = selectedOrg.orgName;
      }
    },

    // ==================== 辅助方法 ====================

    /** 获取授信申请类型标签类型 */
    getApplicationCreditTypeTagType(creditType) {
      const typeMap = {
        'WORKING_CAPITAL_LOAN': 'primary',
        'FIXED_ASSET_LOAN': 'success',
        'BANK_ACCEPTANCE': 'warning',
        'LETTER_OF_CREDIT': 'info'
      };
      return typeMap[creditType] || 'info';
    },

    /** 获取授信申请类型标签文本 */
    getApplicationCreditTypeLabel(creditType) {
      const typeMap = {
        'WORKING_CAPITAL_LOAN': '流动资金贷款',
        'FIXED_ASSET_LOAN': '固定资产贷款',
        'BANK_ACCEPTANCE': '银行承兑汇票',
        'LETTER_OF_CREDIT': '信用证'
      };
      return typeMap[creditType] || creditType;
    },

    /** 获取申请状态标签类型 */
    getApplicationStatusTagType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      };
      return statusMap[status] || 'info';
    },

    /** 获取申请状态标签文本 */
    getApplicationStatusLabel(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'PENDING': '待审批',
        'APPROVED': '已审批',
        'REJECTED': '已拒绝'
      };
      return statusMap[status] || status;
    },

    /** 获取授信类型标签类型 */
    getCreditTypeTagType(creditType) {
      const typeMap = {
        'COMPREHENSIVE': 'primary',
        'SPECIAL': 'success',
        'TEMPORARY': 'warning'
      };
      return typeMap[creditType] || 'info';
    },

    /** 获取授信类型标签文本 */
    getCreditTypeLabel(creditType) {
      const typeMap = {
        'COMPREHENSIVE': '综合授信',
        'SPECIAL': '专项授信',
        'TEMPORARY': '临时授信'
      };
      return typeMap[creditType] || creditType;
    },

    /** 获取合同状态标签类型 */
    getContractStatusTagType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'SIGNED': 'warning',
        'EFFECTIVE': 'success',
        'TERMINATED': 'danger',
        'EXPIRED': 'warning'
      };
      return statusMap[status] || 'info';
    },

    /** 获取合同状态标签文本 */
    getContractStatusLabel(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'SIGNED': '已签署',
        'EFFECTIVE': '生效中',
        'TERMINATED': '已终止',
        'EXPIRED': '已到期'
      };
      return statusMap[status] || status;
    },

    /** 获取担保方式标签文本 */
    getGuaranteeMethodLabel(method) {
      const methodMap = {
        'CREDIT': '信用',
        'GUARANTEE': '保证',
        'MORTGAGE': '抵押',
        'PLEDGE': '质押'
      };
      return methodMap[method] || method;
    },

    // ==================== 授信额度辅助方法 ====================

    /** 获取额度状态标签类型 */
    getLimitStatusTagType(status) {
      const typeMap = {
        'ACTIVE': 'success',
        'SUSPENDED': 'warning',
        'EXPIRED': 'info',
        'CANCELLED': 'danger'
      };
      return typeMap[status] || 'info';
    },

    /** 获取额度类型标签文本 */
    getLimitTypeLabel(type) {
      const typeMap = {
        'WORKING_CAPITAL': '流动资金贷款',
        'SHORT_TERM': '短期贷款',
        'TRADE_FINANCE': '贸易融资',
        'PROJECT_LOAN': '项目贷款',
        'LETTER_OF_CREDIT': '信用证',
        'BILL_DISCOUNT': '票据贴现',
        'GUARANTEE': '保函'
      };
      return typeMap[type] || type;
    },

    /** 获取额度类型标签类型 */
    getLimitTypeTagType(type) {
      const typeMap = {
        'WORKING_CAPITAL': 'primary',
        'SHORT_TERM': 'success',
        'TRADE_FINANCE': 'warning',
        'PROJECT_LOAN': 'danger',
        'LETTER_OF_CREDIT': 'info',
        'BILL_DISCOUNT': '',
        'GUARANTEE': 'warning'
      };
      return typeMap[type] || '';
    },

    /** 获取额度状态标签文本 */
    getLimitStatusLabel(status) {
      const statusMap = {
        'NORMAL': '正常',
        'FROZEN': '冻结',
        'SUSPENDED': '暂停',
        'CANCELLED': '已取消',
        'ACTIVE': '有效',
        'EXPIRED': '已到期'
      };
      return statusMap[status] || status;
    },

    /** 获取额度状态标签类型 */
    getLimitStatusTagType(status) {
      const statusMap = {
        'NORMAL': 'success',
        'FROZEN': 'danger',
        'SUSPENDED': 'warning',
        'CANCELLED': 'info',
        'ACTIVE': 'success',
        'EXPIRED': 'info'
      };
      return statusMap[status] || '';
    },

    /** 计算额度使用率 */
    getLimitUsagePercentage(row) {
      if (!row.totalLimit || row.totalLimit === 0) return 0;
      const percentage = (row.usedLimit / row.totalLimit) * 100;
      return Math.round(percentage);
    },

    /** 获取额度使用率颜色 */
    getLimitUsageColor(row) {
      const percentage = this.getLimitUsagePercentage(row);
      if (percentage >= 90) return '#F56C6C'; // 红色 - 高风险
      if (percentage >= 70) return '#E6A23C'; // 橙色 - 中风险
      if (percentage >= 50) return '#409EFF'; // 蓝色 - 正常
      return '#67C23A'; // 绿色 - 低使用率
    },

    // ==================== 授信评估管理方法 ====================

    /** 查询授信评估列表 */
    getAssessmentList() {
      this.assessmentLoading = true;
      getCreditAssessmentPage(this.assessmentQuery).then(response => {
        try {
          console.log('授信评估响应:', response);
          // 支持多种响应格式
          let data = response;
          if (response.data) {
            data = response.data;
          }
          console.log('解析后的data:', data);

          // 处理tlist格式（示例云标准格式）
          if (data.tlist) {
            this.assessmentList = data.tlist || [];
            this.assessmentTotal = data.totalRecord || data.total || 0;
            console.log('使用tlist格式, 数据条数:', this.assessmentList.length);
          }
          // 处理PageInfo格式
          else if (data.records) {
            this.assessmentList = data.records || [];
            this.assessmentTotal = data.total || 0;
          }
          // 处理标准格式
          else if (data.rows) {
            this.assessmentList = data.rows || [];
            this.assessmentTotal = data.total || 0;
          }
          // 处理数组格式
          else if (Array.isArray(data)) {
            this.assessmentList = data;
            this.assessmentTotal = data.length;
          }
          // 处理其他格式
          else {
            this.assessmentList = [];
            this.assessmentTotal = 0;
          }
        } catch (error) {
          console.error('解析授信评估数据失败:', error);
          this.assessmentList = [];
          this.assessmentTotal = 0;
        }
        this.assessmentLoading = false;
      }).catch(error => {
        console.error('查询授信评估列表失败:', error);
        this.assessmentList = [];
        this.assessmentTotal = 0;
        this.assessmentLoading = false;
      });
    },

    /** 搜索按钮操作 */
    handleAssessmentQuery() {
      console.log('搜索按钮被点击, 查询参数:', this.assessmentQuery);
      this.assessmentQuery.pageNum = 1;
      this.getAssessmentList();
    },

    /** 重置按钮操作 */
    resetAssessmentQuery() {
      console.log('重置按钮被点击');
      this.resetForm("assessmentQueryForm");
      this.handleAssessmentQuery();
    },

    /** 多选框选中数据 */
    handleAssessmentSelectionChange(selection) {
      console.log('选中的评估数据:', selection);
      // 兼容多种字段名格式
      this.assessmentIds = selection.map(item => item.assessmentId || item.id);
      console.log('提取的assessmentIds:', this.assessmentIds);
      this.assessmentSingle = selection.length !== 1;
      this.assessmentMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAssessmentAdd() {
      this.resetAssessmentForm();
      this.assessmentOpen = true;
      this.assessmentTitle = "添加授信评估记录";
    },

    /** 修改按钮操作 */
    handleAssessmentUpdate(row) {
      this.resetAssessmentForm();
      const assessmentId = row ? (row.assessmentId || row.id) : this.assessmentIds[0];
      if (!assessmentId) {
        this.$modal.msgWarning('请先选择要修改的数据');
        return;
      }
      console.log('修改评估, assessmentId:', assessmentId);
      getCreditAssessment(assessmentId).then(response => {
        try {
          let data = response;
          if (response.data) {
            data = response.data;
          }

          this.assessmentForm = data;
          this.assessmentOpen = true;
          this.assessmentTitle = "修改授信评估记录";
        } catch (error) {
          console.error('获取授信评估详情失败:', error);
          this.$modal.msgError("获取评估详情失败");
        }
      }).catch(error => {
        console.error('获取授信评估详情失败:', error);
        this.$modal.msgError("获取评估详情失败");
      });
    },

    /** 查看按钮操作 */
    handleAssessmentView(row) {
      this.resetAssessmentForm();
      const assessmentId = row ? (row.assessmentId || row.id) : null;
      if (!assessmentId) {
        this.$modal.msgWarning('请先选择要查看的数据');
        return;
      }
      console.log('查看评估, assessmentId:', assessmentId);
      getCreditAssessment(assessmentId).then(response => {
        try {
          let data = response;
          if (response.data) {
            data = response.data;
          }

          this.assessmentForm = data;
          this.assessmentDetailOpen = true;
          this.assessmentTitle = "授信评估记录详情";
        } catch (error) {
          console.error('获取授信评估详情失败:', error);
          this.$modal.msgError("获取评估详情失败");
        }
      }).catch(error => {
        console.error('获取授信评估详情失败:', error);
        this.$modal.msgError("获取评估详情失败");
      });
    },

    /** 删除按钮操作 */
    handleAssessmentDelete(row) {
      const assessmentIds = row ? [row.assessmentId || row.id] : this.assessmentIds;
      if (!assessmentIds || assessmentIds.length === 0 || !assessmentIds[0]) {
        this.$modal.msgWarning('请先选择要删除的数据');
        return;
      }
      console.log('删除评估, assessmentIds:', assessmentIds);
      this.$modal.confirm('是否确认删除授信评估记录编号为"' + assessmentIds + '"的数据项？').then(() => {
        return deleteCreditAssessment(assessmentIds.join(","));
      }).then(() => {
        this.getAssessmentList();
        this.$modal.msgSuccess("删除成功");
      }).catch(error => {
        console.error('删除授信评估记录失败:', error);
      });
    },

    /** 导出按钮操作 */
    handleAssessmentExport() {
      this.$modal.confirm('是否确认导出所有授信评估记录数据项？').then(() => {
        this.assessmentLoading = true;
        return exportCreditAssessments(this.assessmentQuery);
      }).then(response => {
        this.$download.excel(response, '授信评估记录.xlsx');
        this.assessmentLoading = false;
      }).catch(error => {
        console.error('导出授信评估记录失败:', error);
        this.assessmentLoading = false;
      });
    },

    /** 操作下拉菜单命令分发 */
    handleAssessmentCommand(command, row) {
      console.log('操作命令:', command, '行数据:', row);
      switch (command) {
        case 'edit':
          this.handleAssessmentUpdate(row);
          break;
        case 'view':
          this.handleAssessmentView(row);
          break;
        case 'submit':
          this.handleAssessmentSubmit(row);
          break;
        case 'approve':
          this.handleAssessmentApprove(row);
          break;
        case 'reject':
          this.handleAssessmentReject(row);
          break;
        case 'withdraw':
          this.handleAssessmentWithdraw(row);
          break;
        case 'copy':
          this.handleAssessmentCopy(row);
          break;
        case 'delete':
          this.handleAssessmentDelete(row);
          break;
      }
    },

    /** 提交评估记录 */
    handleAssessmentSubmit(row) {
      const assessmentId = row.assessmentId || row.id;
      this.$modal.confirm('是否确认提交该评估记录？').then(() => {
        return submitCreditAssessment(assessmentId, '当前用户');
      }).then(() => {
        this.getAssessmentList();
        this.$modal.msgSuccess("提交成功");
      }).catch(error => {
        console.error('提交评估记录失败:', error);
      });
    },

    /** 批准评估记录 */
    handleAssessmentApprove(row) {
      const assessmentId = row.assessmentId || row.id;
      this.$modal.confirm('是否确认批准该评估记录？').then(() => {
        return approveCreditAssessment(assessmentId, '当前用户');
      }).then(() => {
        this.getAssessmentList();
        this.$modal.msgSuccess("批准成功");
      }).catch(error => {
        console.error('批准评估记录失败:', error);
      });
    },

    /** 拒绝评估记录 */
    handleAssessmentReject(row) {
      const assessmentId = row.assessmentId || row.id;
      this.$prompt('请输入拒绝原因', '拒绝评估', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '拒绝原因不能为空'
      }).then(({ value }) => {
        return rejectCreditAssessment(assessmentId, value, '当前用户');
      }).then(() => {
        this.getAssessmentList();
        this.$modal.msgSuccess("拒绝成功");
      }).catch(error => {
        console.error('拒绝评估记录失败:', error);
      });
    },

    /** 撤回评估记录 */
    handleAssessmentWithdraw(row) {
      const assessmentId = row.assessmentId || row.id;
      this.$prompt('请输入撤回原因', '撤回评估', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '撤回原因不能为空'
      }).then(({ value }) => {
        return withdrawCreditAssessment(assessmentId, value);
      }).then(() => {
        this.getAssessmentList();
        this.$modal.msgSuccess("撤回成功");
      }).catch(error => {
        console.error('撤回评估记录失败:', error);
      });
    },

    /** 复制评估记录 */
    handleAssessmentCopy(row) {
      const assessmentId = row.assessmentId || row.id;
      this.$prompt('请输入目标公司ID', '复制评估', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^\d+$/,
        inputErrorMessage: '请输入有效的公司ID'
      }).then(({ value }) => {
        return copyCreditAssessment(assessmentId, parseInt(value));
      }).then(() => {
        this.getAssessmentList();
        this.$modal.msgSuccess("复制成功");
      }).catch(error => {
        console.error('复制评估记录失败:', error);
      });
    },

    /** 提交表单 */
    submitAssessmentForm() {
      this.$refs["assessmentForm"].validate(valid => {
        if (valid) {
          // 兼容 assessmentId 和 id 两种字段名
          const assessmentId = this.assessmentForm.assessmentId || this.assessmentForm.id;
          console.log('提交表单, assessmentId:', assessmentId, 'form:', this.assessmentForm);
          if (assessmentId != null) {
            this.updateAssessmentData();
          } else {
            this.addAssessmentData();
          }
        }
      });
    },

    /** 新增数据 */
    addAssessmentData() {
      // 设置默认值
      if (!this.assessmentForm.assessmentNo) {
        this.assessmentForm.assessmentNo = 'ASSESS' + Date.now();
      }
      if (!this.assessmentForm.assessmentDate) {
        this.assessmentForm.assessmentDate = new Date().toISOString().split('T')[0];
      }
      if (!this.assessmentForm.assessmentStatus) {
        this.assessmentForm.assessmentStatus = 'DRAFT';
      }
      if (!this.assessmentForm.orgid) {
        this.assessmentForm.orgid = 1;
      }

      createCreditAssessment(this.assessmentForm).then(response => {
        this.$modal.msgSuccess("新增成功");
        this.assessmentOpen = false;
        this.getAssessmentList();
      }).catch(error => {
        console.error('新增授信评估记录失败:', error);
        this.$modal.msgError("新增失败");
      });
    },

    /** 修改数据 */
    updateAssessmentData() {
      // 兼容 assessmentId 和 id 两种字段名
      const assessmentId = this.assessmentForm.assessmentId || this.assessmentForm.id;
      console.log('修改数据, assessmentId:', assessmentId);
      updateCreditAssessment(assessmentId, this.assessmentForm).then(response => {
        this.$modal.msgSuccess("修改成功");
        this.assessmentOpen = false;
        this.getAssessmentList();
      }).catch(error => {
        console.error('修改授信评估记录失败:', error);
        this.$modal.msgError("修改失败");
      });
    },

    /** 取消按钮 */
    cancelAssessment() {
      this.assessmentOpen = false;
      this.resetAssessmentForm();
    },

    /** 表单重置 */
    resetAssessmentForm() {
      this.assessmentForm = {
        id: null,
        assessmentNo: null,
        companyId: null,
        assessmentType: null,
        assessmentDate: null,
        financialIndicators: null,
        businessIndicators: null,
        riskIndicators: null,
        creditRating: null,
        riskLevel: null,
        recommendedLimit: null,
        assessmentConclusion: null,
        assessmentRecommendations: null,
        assessorId: null,
        assessmentStatus: 'DRAFT',
        orgid: 1
      };
      this.resetForm("assessmentForm");
    },

    // ==================== 授信评估辅助方法 ====================

    /** 获取评估类型标签类型 */
    getAssessmentTypeTagType(assessmentType) {
      const typeMap = {
        'INITIAL': 'primary',
        'PERIODIC': 'success',
        'SPECIAL': 'warning'
      };
      return typeMap[assessmentType] || 'info';
    },

    /** 获取评估类型标签文本 */
    getAssessmentTypeLabel(assessmentType) {
      const labelMap = {
        'INITIAL': '初始评估',
        'PERIODIC': '定期评估',
        'SPECIAL': '专项评估'
      };
      return labelMap[assessmentType] || assessmentType;
    },

    /** 获取评估状态标签类型 */
    getAssessmentStatusTagType(assessmentStatus) {
      const typeMap = {
        'DRAFT': 'info',
        'COMPLETED': 'warning',
        'APPROVED': 'success'
      };
      return typeMap[assessmentStatus] || 'info';
    },

    /** 获取评估状态标签文本 */
    getAssessmentStatusLabel(assessmentStatus) {
      const labelMap = {
        'DRAFT': '草稿',
        'COMPLETED': '已完成',
        'APPROVED': '已批准'
      };
      return labelMap[assessmentStatus] || assessmentStatus;
    },

    /** 获取信用评级标签类型 */
    getCreditRatingTagType(creditRating) {
      if (['AAA', 'AA', 'A'].includes(creditRating)) return 'success';
      if (['BBB', 'BB', 'B'].includes(creditRating)) return 'warning';
      if (['CCC', 'CC', 'C', 'D'].includes(creditRating)) return 'danger';
      return 'info';
    },

    /** 获取信用评级标签文本 */
    getCreditRatingLabel(creditRating) {
      const labelMap = {
        'AAA': 'AAA级',
        'AA': 'AA级',
        'A': 'A级',
        'BBB': 'BBB级',
        'BB': 'BB级',
        'B': 'B级',
        'CCC': 'CCC级',
        'CC': 'CC级',
        'C': 'C级',
        'D': 'D级'
      };
      return labelMap[creditRating] || creditRating;
    },

    /** 获取风险等级标签类型 */
    getRiskLevelTagType(riskLevel) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      };
      return typeMap[riskLevel] || 'info';
    },

    /** 获取风险等级标签文本 */
    getRiskLevelLabel(riskLevel) {
      const labelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '极高风险'
      };
      return labelMap[riskLevel] || riskLevel;
    },

    // ==================== 授信监控管理方法 ====================

    /** 查询授信监控预警列表 */
    async getMonitoringList() {
      this.monitoringLoading = true;
      try {
        const response = await getCreditMonitoringPage(this.monitoringQuery);
        console.log('授信监控响应:', response);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          const data = response.data;
          console.log('解析后的data:', data);

          // 支持多种响应格式
          if (data && data.tlist) {
            // 示例云标准格式 tlist
            this.monitoringList = data.tlist || [];
            this.monitoringTotal = data.totalRecord || data.total || 0;
            console.log('使用tlist格式, 数据条数:', this.monitoringList.length);
          } else if (data && data.records) {
            // PageInfo格式
            this.monitoringList = data.records || [];
            this.monitoringTotal = data.total || 0;
            console.log('使用records格式, 数据条数:', this.monitoringList.length);
          } else if (data && data.rows) {
            // rows格式
            this.monitoringList = data.rows || [];
            this.monitoringTotal = data.total || 0;
            console.log('使用rows格式, 数据条数:', this.monitoringList.length);
          } else if (data && Array.isArray(data)) {
            // 数组格式
            this.monitoringList = data;
            this.monitoringTotal = data.length;
            console.log('使用数组格式, 数据条数:', this.monitoringList.length);
          } else if (response.rows) {
            // 标准格式
            this.monitoringList = response.rows || [];
            this.monitoringTotal = response.total || 0;
            console.log('使用response.rows格式, 数据条数:', this.monitoringList.length);
          } else {
            this.monitoringList = [];
            this.monitoringTotal = 0;
            console.log('未匹配到任何格式, 数据为空');
          }
        } else {
          this.monitoringList = [];
          this.monitoringTotal = 0;
        }
      } catch (error) {
        console.error('查询授信监控预警列表失败:', error);
        this.monitoringList = [];
        this.monitoringTotal = 0;
      } finally {
        this.monitoringLoading = false;
      }
    },

    /** 获取监控概览数据 */
    async getMonitoringOverview() {
      try {
        // 获取严重预警数量
        const criticalResponse = await getCriticalCreditMonitoring();
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(criticalResponse.code)) {
          this.monitoringOverview.criticalCount = Array.isArray(criticalResponse.data) ? criticalResponse.data.length : 0;
        }

        // 获取高级别预警数量
        const highResponse = await getHighLevelCreditMonitoring();
        if (successCodes.includes(highResponse.code)) {
          this.monitoringOverview.highCount = Array.isArray(highResponse.data) ? highResponse.data.length : 0;
        }

        // 获取活跃预警数量
        const activeResponse = await getActiveCreditMonitoring();
        if (successCodes.includes(activeResponse.code)) {
          this.monitoringOverview.activeCount = Array.isArray(activeResponse.data) ? activeResponse.data.length : 0;
        }

        // 计算总数
        this.monitoringOverview.totalCount = this.monitoringTotal;
      } catch (error) {
        console.error('获取监控概览数据失败:', error);
        this.monitoringOverview = {
          criticalCount: 0,
          highCount: 0,
          activeCount: 0,
          totalCount: 0
        };
      }
    },

    /** 授信监控搜索按钮操作 */
    handleMonitoringQuery() {
      this.monitoringQuery.pageNum = 1;
      this.getMonitoringList();
    },

    /** 授信监控重置按钮操作 */
    resetMonitoringQuery() {
      this.resetForm("monitoringQueryForm");
      this.handleMonitoringQuery();
    },

    /** 多选框选中数据 */
    handleMonitoringSelectionChange(selection) {
      // 兼容 alertId 和 id 两种字段名
      this.monitoringIds = selection.map(item => item.alertId || item.id);
      this.monitoringSingle = selection.length !== 1;
      this.monitoringMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleMonitoringAdd() {
      this.resetMonitoringForm();
      this.monitoringOpen = true;
      this.monitoringTitle = "添加授信监控预警";
    },

    /** 修改按钮操作 */
    async handleMonitoringUpdate(row) {
      console.log('修改预警, row:', row);
      this.resetMonitoringForm();
      // 兼容 alertId 和 id 两种字段名
      const alertId = row.alertId || row.id || this.monitoringIds[0];
      console.log('使用的alertId:', alertId);
      try {
        const response = await getCreditMonitoring(alertId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.monitoringForm = response.data || {};
          this.monitoringOpen = true;
          this.monitoringTitle = "修改授信监控预警";
        } else {
          this.$modal.msgError(response.message || '获取预警信息失败');
        }
      } catch (error) {
        console.error('获取授信监控预警详情失败:', error);
        this.$modal.msgError('获取预警信息失败，请稍后重试');
      }
    },

    /** 查看详情 */
    async handleMonitoringView(row) {
      console.log('查看预警详情, row:', row);
      // 兼容 alertId 和 id 两种字段名
      const alertId = row.alertId || row.id;
      console.log('使用的alertId:', alertId);
      try {
        const response = await getCreditMonitoring(alertId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.monitoringForm = response.data || {};
          this.monitoringDetailOpen = true;
        } else {
          this.$modal.msgError(response.message || '获取预警详情失败');
        }
      } catch (error) {
        console.error('获取授信监控预警详情失败:', error);
        this.$modal.msgError('获取预警详情失败，请稍后重试');
      }
    },

    /** 删除按钮操作 */
    async handleMonitoringDelete(row) {
      console.log('删除预警, row:', row);
      // 兼容 alertId 和 id 两种字段名
      const rowId = row.alertId || row.id;
      const alertIds = rowId ? [rowId] : this.monitoringIds;
      console.log('使用的alertIds:', alertIds);
      this.$modal.confirm('是否确认删除授信监控预警编号为"' + alertIds + '"的数据项？').then(() => {
        return this.deleteMonitoringData(alertIds.join(","));
      }).then(() => {
        this.getMonitoringList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 处理预警 */
    handleMonitoringHandle(row) {
      console.log('处理预警, row:', row);
      // 兼容 alertId 和 id 两种字段名
      const alertId = row.alertId || row.id;
      console.log('使用的alertId:', alertId);
      this.monitoringHandleForm = {
        alertId: alertId,
        handlerId: null,
        handleComments: null,
        action: 'handle'
      };
      this.monitoringHandleOpen = true;
    },

    /** 关闭预警 */
    handleMonitoringClose(row) {
      console.log('关闭预警, row:', row);
      // 兼容 alertId 和 id 两种字段名
      const alertId = row.alertId || row.id;
      console.log('使用的alertId:', alertId);
      this.monitoringHandleForm = {
        alertId: alertId,
        handlerId: null,
        handleComments: null,
        action: 'close'
      };
      this.monitoringHandleOpen = true;
    },

    /** 重新激活预警 */
    async handleMonitoringReactivate(row) {
      console.log('重新激活预警, row:', row);
      // 兼容 alertId 和 id 两种字段名
      const alertId = row.alertId || row.id;
      console.log('使用的alertId:', alertId);
      this.$prompt('请输入重新激活的原因', '重新激活预警', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '原因不能为空'
      }).then(async ({ value }) => {
        try {
          const response = await reactivateCreditMonitoring(alertId, value);
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$modal.msgSuccess("重新激活成功");
            this.getMonitoringList();
          } else {
            this.$modal.msgError(response.message || '重新激活失败');
          }
        } catch (error) {
          console.error('重新激活预警失败:', error);
          this.$modal.msgError('重新激活失败，请稍后重试');
        }
      }).catch(() => {});
    },

    /** 批量处理预警 */
    handleMonitoringBatchHandle() {
      if (this.monitoringIds.length === 0) {
        this.$modal.msgWarning('请选择要处理的预警记录');
        return;
      }
      this.monitoringHandleForm = {
        alertIds: this.monitoringIds,
        handlerId: null,
        handleComments: null,
        action: 'batchHandle'
      };
      this.monitoringHandleOpen = true;
    },

    /** 导出按钮操作 */
    handleMonitoringExport() {
      this.download('/qqsk/financing/credit/monitoring/export', {
        ...this.monitoringQuery
      }, `credit_monitoring_${new Date().getTime()}.xlsx`);
    },

    /** 生成预警按钮操作 */
    async handleMonitoringGenerate() {
      this.$modal.confirm('是否确认生成预警？此操作将扫描所有授信额度并生成相应预警。').then(async () => {
        try {
          const response = await batchGenerateCreditMonitoring();
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$modal.msgSuccess("生成预警成功");
            this.getMonitoringList();
            this.getMonitoringOverview();
          } else {
            this.$modal.msgError(response.message || '生成预警失败');
          }
        } catch (error) {
          console.error('生成预警失败:', error);
          this.$modal.msgError('生成预警失败，请稍后重试');
        }
      }).catch(() => {});
    },

    /** 操作下拉菜单命令处理 */
    handleMonitoringCommand(command, row) {
      switch (command) {
        case 'edit':
          this.handleMonitoringUpdate(row);
          break;
        case 'view':
          this.handleMonitoringView(row);
          break;
        case 'handle':
          this.handleMonitoringHandle(row);
          break;
        case 'close':
          this.handleMonitoringClose(row);
          break;
        case 'reactivate':
          this.handleMonitoringReactivate(row);
          break;
        case 'delete':
          this.handleMonitoringDelete(row);
          break;
        default:
          break;
      }
    },

    /** 提交按钮 */
    submitMonitoringForm() {
      this.$refs["monitoringForm"].validate(valid => {
        if (valid) {
          // 兼容 alertId 和 id 两种字段名
          const hasId = this.monitoringForm.alertId != null || this.monitoringForm.id != null;
          if (hasId) {
            this.updateMonitoringData();
          } else {
            this.addMonitoringData();
          }
        }
      });
    },

    /** 提交处理表单 */
    async submitMonitoringHandleForm() {
      if (!this.monitoringHandleForm.handlerId) {
        this.$modal.msgError('请选择处理人');
        return;
      }
      if (!this.monitoringHandleForm.handleComments) {
        this.$modal.msgError('请输入处理意见');
        return;
      }

      try {
        let response;
        const { action, alertId, alertIds, handlerId, handleComments } = this.monitoringHandleForm;

        if (action === 'handle') {
          response = await handleCreditMonitoring(alertId, handlerId, handleComments);
        } else if (action === 'close') {
          response = await closeCreditMonitoring(alertId, handlerId, handleComments);
        } else if (action === 'batchHandle') {
          response = await batchHandleCreditMonitoring(alertIds.join(','), handlerId, handleComments);
        }

        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("操作成功");
          this.monitoringHandleOpen = false;
          this.getMonitoringList();
          this.getMonitoringOverview();
        } else {
          this.$modal.msgError(response.message || '操作失败');
        }
      } catch (error) {
        console.error('处理预警失败:', error);
        this.$modal.msgError('操作失败，请稍后重试');
      }
    },

    /** 新增数据 */
    async addMonitoringData() {
      try {
        // 设置默认值
        if (!this.monitoringForm.alertNo) {
          this.monitoringForm.alertNo = 'ALERT' + Date.now();
        }
        if (!this.monitoringForm.alertDate) {
          this.monitoringForm.alertDate = new Date().toISOString().split('T')[0];
        }
        if (!this.monitoringForm.alertStatus) {
          this.monitoringForm.alertStatus = 'ACTIVE';
        }
        if (!this.monitoringForm.orgid) {
          this.monitoringForm.orgid = 1;
        }

        const response = await createCreditMonitoring(this.monitoringForm);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("新增成功");
          this.monitoringOpen = false;
          this.getMonitoringList();
          this.getMonitoringOverview();
        } else {
          this.$modal.msgError(response.message || '新增失败');
        }
      } catch (error) {
        console.error('新增授信监控预警失败:', error);
        this.$modal.msgError('新增失败，请稍后重试');
      }
    },

    /** 修改数据 */
    async updateMonitoringData() {
      try {
        // 兼容 alertId 和 id 两种字段名
        const alertId = this.monitoringForm.alertId || this.monitoringForm.id;
        console.log('更新预警数据, alertId:', alertId);
        const response = await updateCreditMonitoring(alertId, this.monitoringForm);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$modal.msgSuccess("修改成功");
          this.monitoringOpen = false;
          this.getMonitoringList();
        } else {
          this.$modal.msgError(response.message || '修改失败');
        }
      } catch (error) {
        console.error('修改授信监控预警失败:', error);
        this.$modal.msgError('修改失败，请稍后重试');
      }
    },

    /** 删除数据 */
    async deleteMonitoringData(alertIds) {
      try {
        const response = await deleteCreditMonitoring(alertIds);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          return Promise.resolve();
        } else {
          return Promise.reject(new Error(response.message || '删除失败'));
        }
      } catch (error) {
        console.error('删除授信监控预警失败:', error);
        return Promise.reject(error);
      }
    },

    /** 取消按钮 */
    cancelMonitoring() {
      this.monitoringOpen = false;
      this.resetMonitoringForm();
    },

    /** 取消处理按钮 */
    cancelMonitoringHandle() {
      this.monitoringHandleOpen = false;
      this.monitoringHandleForm = {
        handlerId: null,
        handleComments: null,
        action: 'handle'
      };
    },

    /** 表单重置 */
    resetMonitoringForm() {
      this.monitoringForm = {
        alertId: null,
        id: null,
        alertNo: null,
        limitId: null,
        limitCode: null,
        companyId: null,
        companyName: null,
        alertTitle: null,
        alertType: null,
        alertLevel: null,
        alertContent: null,
        alertDate: null,
        triggerConditions: null,
        suggestedActions: null,
        alertStatus: 'ACTIVE',
        handlerId: null,
        handleTime: null,
        handleComments: null,
        orgid: 1
      };
      this.resetForm("monitoringForm");
    },

    // ==================== 授信监控辅助方法 ====================

    /** 获取预警类型标签类型 */
    getAlertTypeTagType(alertType) {
      const typeMap = {
        'USAGE_RATIO': 'warning',
        'EXPIRY': 'primary',
        'EXPIRY_WARNING': 'primary',
        'OVERDUE': 'danger',
        'RISK': 'danger',
        'RISK_ALERT': 'danger',
        'LIMIT_EXCEED': 'warning'
      };
      return typeMap[alertType] || 'info';
    },

    /** 获取预警类型标签文本 */
    getAlertTypeLabel(alertType) {
      const labelMap = {
        'USAGE_RATIO': '使用率预警',
        'EXPIRY': '到期预警',
        'EXPIRY_WARNING': '到期预警',
        'OVERDUE': '逾期预警',
        'RISK': '风险预警',
        'RISK_ALERT': '风险预警',
        'LIMIT_EXCEED': '额度超限'
      };
      return labelMap[alertType] || alertType;
    },

    /** 格式化预警日期 */
    formatAlertDate(row) {
      // 优先使用 alertDate，其次使用 alertTime
      const dateStr = row.alertDate || row.alertTime;
      if (!dateStr) return '-';
      // 如果是完整的日期时间格式，只取日期部分
      if (dateStr.includes(' ')) {
        return dateStr.split(' ')[0];
      }
      return dateStr;
    },

    /** 获取预警级别标签类型 */
    getAlertLevelTagType(alertLevel) {
      const typeMap = {
        'LOW': 'info',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      };
      return typeMap[alertLevel] || 'info';
    },

    /** 获取预警级别标签文本 */
    getAlertLevelLabel(alertLevel) {
      const labelMap = {
        'LOW': '低级',
        'MEDIUM': '中级',
        'HIGH': '高级',
        'CRITICAL': '严重'
      };
      return labelMap[alertLevel] || alertLevel;
    },

    /** 获取预警状态标签类型 */
    getAlertStatusTagType(alertStatus) {
      const typeMap = {
        'ACTIVE': 'danger',
        'PENDING': 'warning',
        'HANDLED': 'success',
        'CLOSED': 'info'
      };
      return typeMap[alertStatus] || 'info';
    },

    /** 获取预警状态标签文本 */
    getAlertStatusLabel(alertStatus) {
      const labelMap = {
        'ACTIVE': '活跃',
        'PENDING': '待处理',
        'HANDLED': '已处理',
        'CLOSED': '已关闭'
      };
      return labelMap[alertStatus] || alertStatus;
    },

    /** 通用表单重置方法 */
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

/* 监控概览卡片样式 */
.overview-card {
  cursor: pointer;
  transition: all 0.3s;
}

.overview-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.card-content {
  display: flex;
  align-items: center;
  padding: 10px;
}

.card-icon {
  font-size: 32px;
  margin-right: 15px;
  width: 50px;
  text-align: center;
}

.card-info {
  flex: 1;
}

.card-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

/* 不同类型卡片的颜色 */
.overview-card.critical .card-icon {
  color: #f56c6c;
}

.overview-card.critical .card-value {
  color: #f56c6c;
}

.overview-card.high .card-icon {
  color: #e6a23c;
}

.overview-card.high .card-value {
  color: #e6a23c;
}

.overview-card.active .card-icon {
  color: #409eff;
}

.overview-card.active .card-value {
  color: #409eff;
}

.overview-card.total .card-icon {
  color: #67c23a;
}

.overview-card.total .card-value {
  color: #67c23a;
}

/* 监控容器样式 */
.monitoring-container {
  padding: 20px;
}
</style>
