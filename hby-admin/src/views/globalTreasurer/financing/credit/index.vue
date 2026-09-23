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
                <el-option
                  v-for="dict in creditType"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="申请状态" prop="applicationStatus">
              <el-select v-model="applicationQuery.applicationStatus" placeholder="请选择申请状态" clearable>
                <el-option
                  v-for="dict in applicationStatus"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
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
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getApplicationList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="applicationLoading" :data="applicationList" @selection-change="handleApplicationSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="申请编号" align="center" prop="applicationNo" />
            <el-table-column label="申请机构" align="center" prop="orgName" />
            <el-table-column label="授信类型" align="center" prop="creditType">
              <template slot-scope="scope">
                <dict-tag :options="creditType" :value="scope.row.creditType"/>
              </template>
            </el-table-column>
            <el-table-column label="申请金额" align="center" prop="applicationAmount">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.applicationAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="申请状态" align="center" prop="applicationStatus">
              <template slot-scope="scope">
                <dict-tag :options="applicationStatus" :value="scope.row.applicationStatus"/>
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
          <h3>授信合同管理</h3>
          <p>这是授信合同管理页面的内容</p>
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
                v-hasPermi="['financing:creditContract:add']"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="contractSingle"
                @click="handleContractUpdate"
                v-hasPermi="['financing:creditContract:edit']"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="contractMultiple"
                @click="handleContractDelete"
                v-hasPermi="['financing:creditContract:remove']"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleContractExport"
                v-hasPermi="['financing:creditContract:export']"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getContractList"></right-toolbar>
          </el-row>

          <!-- 合同列表 -->
          <el-table v-loading="contractLoading" :data="contractList" @selection-change="handleContractSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="合同编号" align="center" prop="contractNo" />
            <el-table-column label="公司名称" align="center" prop="companyName" />
            <el-table-column label="融资机构" align="center" prop="financingInstitutionName" />
            <el-table-column label="授信类型" align="center" prop="creditType">
              <template slot-scope="scope">
                <dict-tag :options="creditType" :value="scope.row.creditType"/>
              </template>
            </el-table-column>
            <el-table-column label="授信金额" align="center" prop="creditAmount">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.creditAmount, scope.row.currencyCode) }}
              </template>
            </el-table-column>
            <el-table-column label="合同期限" align="center" prop="creditTerm">
              <template slot-scope="scope">
                {{ scope.row.creditTerm }}个月
              </template>
            </el-table-column>
            <el-table-column label="合同状态" align="center" prop="contractStatus">
              <template slot-scope="scope">
                <dict-tag :options="contractStatus" :value="scope.row.contractStatus"/>
              </template>
            </el-table-column>
            <el-table-column label="剩余天数" align="center" prop="remainingDays">
              <template slot-scope="scope">
                <span v-if="scope.row.remainingDays > 0" :class="{'text-danger': scope.row.remainingDays <= 30}">
                  {{ scope.row.remainingDays }}天
                </span>
                <span v-else class="text-danger">已到期</span>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleContractView(scope.row)"
                  v-hasPermi="['financing:creditContract:query']"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleContractUpdate(scope.row)"
                  v-hasPermi="['financing:creditContract:edit']"
                >修改</el-button>
                <el-button
                  v-if="scope.row.contractStatus === 'EFFECTIVE'"
                  size="mini"
                  type="text"
                  icon="el-icon-check"
                  @click="handleContractSign(scope.row)"
                  v-hasPermi="['financing:creditContract:sign']"
                >签署</el-button>
                <el-button
                  v-if="scope.row.contractStatus === 'EFFECTIVE'"
                  size="mini"
                  type="text"
                  icon="el-icon-close"
                  @click="handleContractTerminate(scope.row)"
                  v-hasPermi="['financing:creditContract:terminate']"
                >终止</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleContractDelete(scope.row)"
                  v-hasPermi="['financing:creditContract:remove']"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="contractTotal>0"
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
            <el-form-item label="公司名称" prop="companyId">
              <el-select v-model="limitQuery.companyId" placeholder="请选择公司" clearable>
                <el-option label="示例云科技" value="1" />
                <el-option label="分公司A" value="2" />
                <el-option label="分公司B" value="3" />
              </el-select>
            </el-form-item>
            <el-form-item label="授信类型" prop="creditType">
              <el-select v-model="limitQuery.creditType" placeholder="请选择授信类型" clearable>
                <el-option
                  v-for="dict in creditType"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="额度状态" prop="limitStatus">
              <el-select v-model="limitQuery.limitStatus" placeholder="请选择额度状态" clearable>
                <el-option
                  v-for="dict in limitStatus"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="融资机构" prop="financingInstitutionId">
              <el-select v-model="limitQuery.financingInstitutionId" placeholder="请选择融资机构" clearable>
                <el-option label="中国银行" value="1" />
                <el-option label="工商银行" value="2" />
                <el-option label="建设银行" value="3" />
              </el-select>
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
                v-hasPermi="['financing:creditLimit:add']"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="limitSingle"
                @click="handleLimitUpdate"
                v-hasPermi="['financing:creditLimit:edit']"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="limitMultiple"
                @click="handleLimitDelete"
                v-hasPermi="['financing:creditLimit:remove']"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleLimitExport"
                v-hasPermi="['financing:creditLimit:export']"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getLimitList"></right-toolbar>
          </el-row>

          <!-- 额度列表 -->
          <el-table v-loading="limitLoading" :data="limitList" @selection-change="handleLimitSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="额度代码" align="center" prop="limitCode" />
            <el-table-column label="公司名称" align="center" prop="companyId">
              <template slot-scope="scope">
                <span v-if="scope.row.companyId === 1">示例云科技</span>
                <span v-else-if="scope.row.companyId === 2">分公司A</span>
                <span v-else-if="scope.row.companyId === 3">分公司B</span>
                <span v-else>{{ scope.row.companyId }}</span>
              </template>
            </el-table-column>
            <el-table-column label="授信类型" align="center" prop="creditType">
              <template slot-scope="scope">
                <dict-tag :options="creditType" :value="scope.row.creditType"/>
              </template>
            </el-table-column>
            <el-table-column label="总额度" align="center" prop="totalLimit">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.totalLimit) }}
              </template>
            </el-table-column>
            <el-table-column label="已用额度" align="center" prop="usedLimit">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.usedLimit) }}
              </template>
            </el-table-column>
            <el-table-column label="可用额度" align="center" prop="availableLimit">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.availableLimit) }}
              </template>
            </el-table-column>
            <el-table-column label="使用率" align="center">
              <template slot-scope="scope">
                <el-progress
                  :percentage="getUsageRatio(scope.row)"
                  :color="getUsageColor(scope.row)"
                  :stroke-width="8"
                />
              </template>
            </el-table-column>
            <el-table-column label="额度状态" align="center" prop="limitStatus">
              <template slot-scope="scope">
                <dict-tag :options="limitStatus" :value="scope.row.limitStatus"/>
              </template>
            </el-table-column>
            <el-table-column label="生效日期" align="center" prop="effectiveDate" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.effectiveDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="到期日期" align="center" prop="expiryDate" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.expiryDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleLimitView(scope.row)"
                  v-hasPermi="['financing:creditLimit:query']"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleLimitUpdate(scope.row)"
                  v-hasPermi="['financing:creditLimit:edit']"
                >修改</el-button>
                <el-dropdown size="mini" @command="(command) => handleLimitCommand(command, scope.row)" v-hasPermi="['financing:creditLimit:use', 'financing:creditLimit:freeze', 'financing:creditLimit:suspend']">
                  <el-button size="mini" type="text">
                    更多<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="use" icon="el-icon-money">使用额度</el-dropdown-item>
                    <el-dropdown-item command="repay" icon="el-icon-refresh">归还额度</el-dropdown-item>
                    <el-dropdown-item command="freeze" icon="el-icon-lock">冻结额度</el-dropdown-item>
                    <el-dropdown-item command="unfreeze" icon="el-icon-unlock">解冻额度</el-dropdown-item>
                    <el-dropdown-item command="suspend" icon="el-icon-warning">暂停额度</el-dropdown-item>
                    <el-dropdown-item command="activate" icon="el-icon-check">激活额度</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleLimitDelete(scope.row)"
                  v-hasPermi="['financing:creditLimit:remove']"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="limitTotal>0"
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
          <p>授信评估功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 授信监控 -->
      <el-tab-pane label="授信监控" name="monitoring">
        <div class="monitoring-container">
          <p>授信监控功能开发中...</p>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改授信申请对话框 -->
    <el-dialog :title="applicationTitle" :visible.sync="applicationOpen" width="800px" append-to-body>
      <el-form ref="applicationForm" :model="applicationForm" :rules="applicationRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请机构" prop="orgId">
              <el-select v-model="applicationForm.orgId" placeholder="请选择申请机构">
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
            <el-form-item label="申请金额" prop="applicationAmount">
              <el-input v-model="applicationForm.applicationAmount" placeholder="请输入申请金额">
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="授信期限" prop="creditTerm">
              <el-input v-model="applicationForm.creditTerm" placeholder="请输入授信期限">
                <template slot="append">月</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="申请用途" prop="applicationPurpose">
          <el-input v-model="applicationForm.applicationPurpose" type="textarea" placeholder="请输入申请用途" />
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
    <el-dialog :title="contractTitle" :visible.sync="contractOpen" width="800px" append-to-body>
      <el-form ref="contractForm" :model="contractForm" :rules="contractRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input v-model="contractForm.contractNo" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="授信类型" prop="creditType">
              <el-select v-model="contractForm.creditType" placeholder="请选择授信类型">
                <el-option
                  v-for="option in creditTypeOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="授信金额" prop="creditAmount">
              <el-input v-model="contractForm.creditAmount" placeholder="请输入授信金额" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="contractForm.currencyCode" placeholder="请选择币种">
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
            <el-form-item label="授信期限" prop="creditTerm">
              <el-input v-model="contractForm.creditTerm" placeholder="请输入授信期限（月）" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="担保方式" prop="guaranteeMethod">
              <el-select v-model="contractForm.guaranteeMethod" placeholder="请选择担保方式">
                <el-option
                  v-for="option in guaranteeMethodOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="合同开始日期" prop="contractStartDate">
              <el-date-picker
                v-model="contractForm.contractStartDate"
                type="date"
                placeholder="选择合同开始日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同结束日期" prop="contractEndDate">
              <el-date-picker
                v-model="contractForm.contractEndDate"
                type="date"
                placeholder="选择合同结束日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="利率类型" prop="interestRateType">
              <el-select v-model="contractForm.interestRateType" placeholder="请选择利率类型">
                <el-option
                  v-for="option in interestRateTypeOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="基准利率" prop="baseRate">
              <el-input v-model="contractForm.baseRate" placeholder="请输入基准利率" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="利差" prop="rateSpread">
              <el-input v-model="contractForm.rateSpread" placeholder="请输入利差" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="承诺费率" prop="commitmentFeeRate">
              <el-input v-model="contractForm.commitmentFeeRate" placeholder="请输入承诺费率" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="管理费率" prop="managementFeeRate">
              <el-input v-model="contractForm.managementFeeRate" placeholder="请输入管理费率" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="担保详情" prop="guaranteeDetails">
              <el-input v-model="contractForm.guaranteeDetails" type="textarea" placeholder="请输入担保详情" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="特殊条款" prop="specialClauses">
              <el-input v-model="contractForm.specialClauses" type="textarea" placeholder="请输入特殊条款" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="违约条款" prop="defaultClauses">
              <el-input v-model="contractForm.defaultClauses" type="textarea" placeholder="请输入违约条款" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="contractForm.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitContractForm">确 定</el-button>
        <el-button @click="cancelContract">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改授信额度对话框 -->
    <el-dialog :title="limitTitle" :visible.sync="limitOpen" width="800px" append-to-body>
      <el-form ref="limitForm" :model="limitForm" :rules="limitRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="额度代码" prop="limitCode">
              <el-input v-model="limitForm.limitCode" placeholder="请输入额度代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联合同" prop="contractId">
              <el-select v-model="limitForm.contractId" placeholder="请选择关联合同" clearable>
                <el-option label="CREDIT20251010001" value="1" />
                <el-option label="CREDIT20251010002" value="2" />
                <el-option label="CREDIT20251010003" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyId">
              <el-select v-model="limitForm.companyId" placeholder="请选择公司">
                <el-option label="示例云科技" value="1" />
                <el-option label="分公司A" value="2" />
                <el-option label="分公司B" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="融资机构" prop="financingInstitutionId">
              <el-select v-model="limitForm.financingInstitutionId" placeholder="请选择融资机构">
                <el-option label="中国银行" value="1" />
                <el-option label="工商银行" value="2" />
                <el-option label="建设银行" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="授信类型" prop="creditType">
              <el-select v-model="limitForm.creditType" placeholder="请选择授信类型">
                <el-option
                  v-for="dict in creditType"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="limitForm.currencyCode" placeholder="请选择币种">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="总额度" prop="totalLimit">
              <el-input v-model="limitForm.totalLimit" placeholder="请输入总额度">
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="已用额度" prop="usedLimit">
              <el-input v-model="limitForm.usedLimit" placeholder="请输入已用额度">
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="冻结额度" prop="frozenLimit">
              <el-input v-model="limitForm.frozenLimit" placeholder="请输入冻结额度">
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="额度状态" prop="limitStatus">
              <el-select v-model="limitForm.limitStatus" placeholder="请选择额度状态">
                <el-option
                  v-for="dict in limitStatus"
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
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="limitForm.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="expiryDate">
              <el-date-picker
                v-model="limitForm.expiryDate"
                type="date"
                placeholder="选择到期日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="利率类型" prop="interestRateType">
              <el-select v-model="limitForm.interestRateType" placeholder="请选择利率类型">
                <el-option
                  v-for="dict in interestRateType"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="基准利率" prop="baseRate">
              <el-input v-model="limitForm.baseRate" placeholder="请输入基准利率" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="利率浮动" prop="rateSpread">
              <el-input v-model="limitForm.rateSpread" placeholder="请输入利率浮动">
                <template slot="append">%</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="承诺费率" prop="commitmentFeeRate">
              <el-input v-model="limitForm.commitmentFeeRate" placeholder="请输入承诺费率">
                <template slot="append">%</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitLimitForm">确 定</el-button>
        <el-button @click="cancelLimit">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 额度操作对话框 -->
    <el-dialog :title="limitActionTitle" :visible.sync="limitActionOpen" width="500px" append-to-body>
      <el-form ref="limitActionForm" :model="limitActionForm" :rules="limitActionRules" label-width="100px">
        <el-form-item label="操作金额" prop="amount">
          <el-input v-model="limitActionForm.amount" placeholder="请输入操作金额">
            <template slot="append">万元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="操作原因" prop="reason">
          <el-input
            v-model="limitActionForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入操作原因"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitLimitAction">确 定</el-button>
        <el-button @click="cancelLimitAction">取 消</el-button>
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
  getCreditContractPage,
  getCreditContract,
  createCreditContract,
  updateCreditContract,
  deleteCreditContract,
  signCreditContract,
  terminateCreditContract,
  exportCreditContracts,
  getCreditLimitPage,
  getCreditLimit,
  createCreditLimit,
  updateCreditLimit,
  deleteCreditLimit,
  useCreditLimit,
  repayCreditLimit,
  freezeCreditLimit,
  unfreezeCreditLimit,
  suspendCreditLimit,
  activateCreditLimit,
  cancelCreditLimit,
  exportCreditLimits
} from "@/api/globalTreasurer/rzgl";
import { parseTime } from '@/utils'

export default {
  name: "CreditManagement",
  components: {

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

      // 授信合同相关数据
      contractLoading: true,
      contractIds: [],
      contractSingle: true,
      contractMultiple: true,
      contractTotal: 0,
      contractList: [],
      contractTitle: "",
      contractOpen: false,
      contractQuery: {
        pageNum: 1,
        pageSize: 10,
        contractNo: null,
        companyName: null,
        creditType: null,
        contractStatus: null,
        financingInstitution: null
      },
      contractForm: {
        id: null,
        contractNo: null,
        applicationId: null,
        companyId: null,
        financingInstitutionId: null,
        creditType: null,
        creditAmount: null,
        currencyCode: 'CNY',
        creditTerm: null,
        contractStartDate: null,
        contractEndDate: null,
        guaranteeMethod: null,
        guaranteeDetails: null,
        interestRateType: null,
        baseRate: null,
        rateSpread: null,
        commitmentFeeRate: null,
        managementFeeRate: null,
        specialClauses: null,
        defaultClauses: null,
        contractStatus: 'EFFECTIVE',
        remark: null
      },
      contractRules: {
        contractNo: [
          { required: true, message: "合同编号不能为空", trigger: "blur" }
        ],
        companyId: [
          { required: true, message: "公司不能为空", trigger: "change" }
        ],
        financingInstitutionId: [
          { required: true, message: "融资机构不能为空", trigger: "change" }
        ],
        creditType: [
          { required: true, message: "授信类型不能为空", trigger: "change" }
        ],
        creditAmount: [
          { required: true, message: "授信金额不能为空", trigger: "blur" }
        ],
        creditTerm: [
          { required: true, message: "授信期限不能为空", trigger: "blur" }
        ],
        contractStartDate: [
          { required: true, message: "合同开始日期不能为空", trigger: "blur" }
        ],
        contractEndDate: [
          { required: true, message: "合同结束日期不能为空", trigger: "blur" }
        ]
      },

      // 字典数据已统一在下方定义
      applicationForm: {},
      applicationRules: {
        orgId: [
          { required: true, message: "申请机构不能为空", trigger: "change" }
        ],
        creditType: [
          { required: true, message: "授信类型不能为空", trigger: "change" }
        ],
        applicationAmount: [
          { required: true, message: "申请金额不能为空", trigger: "blur" }
        ],
        creditTerm: [
          { required: true, message: "授信期限不能为空", trigger: "blur" }
        ],
        applicationPurpose: [
          { required: true, message: "申请用途不能为空", trigger: "blur" }
        ]
      },

      // 组织选项
      orgOptions: [],

      // 授信额度相关数据
      limitLoading: false,
      limitIds: [],
      limitSingle: true,
      limitMultiple: true,
      limitTotal: 0,
      limitList: [],
      limitTitle: "",
      limitOpen: false,
      limitQuery: {
        pageNum: 1,
        pageSize: 10,
        limitCode: null,
        companyId: null,
        creditType: null,
        limitStatus: null,
        financingInstitutionId: null
      },
      limitForm: {
        id: null,
        limitCode: null,
        contractId: null,
        companyId: null,
        financingInstitutionId: null,
        creditType: null,
        totalLimit: null,
        usedLimit: 0,
        frozenLimit: 0,
        currencyCode: 'CNY',
        effectiveDate: null,
        expiryDate: null,
        interestRateType: null,
        baseRate: null,
        rateSpread: null,
        commitmentFeeRate: null,
        limitStatus: 'ACTIVE'
      },
      limitRules: {
        limitCode: [
          { required: true, message: "额度代码不能为空", trigger: "blur" }
        ],
        contractId: [
          { required: true, message: "关联合同不能为空", trigger: "change" }
        ],
        companyId: [
          { required: true, message: "公司不能为空", trigger: "change" }
        ],
        financingInstitutionId: [
          { required: true, message: "融资机构不能为空", trigger: "change" }
        ],
        creditType: [
          { required: true, message: "授信类型不能为空", trigger: "change" }
        ],
        totalLimit: [
          { required: true, message: "总额度不能为空", trigger: "blur" }
        ],
        effectiveDate: [
          { required: true, message: "生效日期不能为空", trigger: "blur" }
        ],
        expiryDate: [
          { required: true, message: "到期日期不能为空", trigger: "blur" }
        ]
      },

      // 额度操作相关数据
      limitActionTitle: "",
      limitActionOpen: false,
      limitActionType: "",
      limitActionForm: {
        amount: null,
        reason: null
      },
      limitActionRules: {
        amount: [
          { required: true, message: "操作金额不能为空", trigger: "blur" }
        ]
      },
      currentLimitId: null,

      // 字典数据
      limitStatus: [
        { label: "有效", value: "ACTIVE" },
        { label: "暂停", value: "SUSPENDED" },
        { label: "已到期", value: "EXPIRED" },
        { label: "已取消", value: "CANCELLED" }
      ],
      creditType: [
        { label: "综合授信", value: "COMPREHENSIVE" },
        { label: "专项授信", value: "SPECIAL" },
        { label: "临时授信", value: "TEMPORARY" }
      ],
      interestRateType: [
        { label: "固定利率", value: "FIXED" },
        { label: "浮动利率", value: "FLOATING" }
      ],
      applicationStatus: [
        { label: "草稿", value: "DRAFT" },
        { label: "已提交", value: "SUBMITTED" },
        { label: "审核中", value: "REVIEWING" },
        { label: "已批准", value: "APPROVED" },
        { label: "已拒绝", value: "REJECTED" }
      ],
      contractStatus: [
        { label: "有效", value: "EFFECTIVE" },
        { label: "已终止", value: "TERMINATED" },
        { label: "已到期", value: "EXPIRED" }
      ],
      guaranteeMethod: [
        { label: "信用", value: "CREDIT" },
        { label: "保证", value: "GUARANTEE" },
        { label: "抵押", value: "MORTGAGE" },
        { label: "质押", value: "PLEDGE" }
      ]
    };
  },
  created() {
    this.getApplicationList();
    this.getContractList();
    this.getLimitList();
  },
  methods: {
    parseTime,
    
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
      }
    },

    /** 查询授信申请列表 */
    getApplicationList() {
      this.applicationLoading = true;
      getCreditApplicationPage(this.applicationQuery).then(response => {
        this.applicationList = response.rows;
        this.applicationTotal = response.total;
        this.applicationLoading = false;
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
        this.applicationForm = response.data;
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
        orgId: null,
        creditType: null,
        applicationAmount: null,
        creditTerm: null,
        applicationPurpose: null,
        remark: null
      };
      this.resetForm("applicationForm");
    },

    // ==================== 授信合同管理方法 ====================

    /** 查询授信合同列表 */
    getContractList() {
      this.contractLoading = true;
      getCreditContractPage(this.contractQuery).then(response => {
        if (response.code === 200) {
          this.contractList = response.data.list || [];
          this.contractTotal = response.data.total || 0;
        } else {
          this.$modal.msgError(response.message || '查询失败');
          this.contractList = [];
          this.contractTotal = 0;
        }
        this.contractLoading = false;
      }).catch(error => {
        console.error('查询授信合同失败:', error);
        this.$modal.msgError('查询失败，请稍后重试');
        this.contractList = [];
        this.contractTotal = 0;
        this.contractLoading = false;
      });
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
      this.contractIds = selection.map(item => item.id);
      this.contractSingle = selection.length !== 1;
      this.contractMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleContractAdd() {
      this.resetContractForm();
      this.contractOpen = true;
      this.contractTitle = "添加授信合同";
    },

    /** 修改按钮操作 */
    handleContractUpdate(row) {
      this.resetContractForm();
      const contractId = row.id || this.contractIds[0];
      getCreditContract(contractId).then(response => {
        if (response.code === 200) {
          this.contractForm = response.data;
          this.contractOpen = true;
          this.contractTitle = "修改授信合同";
        } else {
          this.$modal.msgError(response.message || '获取合同信息失败');
        }
      }).catch(error => {
        console.error('获取合同信息失败:', error);
        this.$modal.msgError('获取合同信息失败，请稍后重试');
      });
    },

    /** 查看按钮操作 */
    handleContractView(row) {
      getCreditContract(row.id).then(response => {
        if (response.code === 200) {
          this.contractForm = response.data;
          this.contractOpen = true;
          this.contractTitle = "查看授信合同";
        } else {
          this.$modal.msgError(response.message || '获取合同信息失败');
        }
      }).catch(error => {
        console.error('获取合同信息失败:', error);
        this.$modal.msgError('获取合同信息失败，请稍后重试');
      });
    },

    /** 删除按钮操作 */
    handleContractDelete(row) {
      const contractIds = row.id ? [row.id] : this.contractIds;
      const contractNos = row.contractNo ? [row.contractNo] : this.contractList.filter(item => contractIds.includes(item.id)).map(item => item.contractNo);

      this.$modal.confirm('是否确认删除授信合同编号为"' + contractNos.join(',') + '"的数据项？').then(() => {
        const deletePromises = contractIds.map(id =>
          deleteCreditContract(id)
        );

        Promise.all(deletePromises).then(responses => {
          const failedCount = responses.filter(response => response.code !== 200).length;
          if (failedCount === 0) {
            this.$modal.msgSuccess("删除成功");
            this.getContractList();
          } else {
            this.$modal.msgError(`删除失败，${failedCount}个合同删除失败`);
          }
        }).catch(error => {
          console.error('删除授信合同失败:', error);
          this.$modal.msgError('删除失败，请稍后重试');
        });
      });
    },

    /** 签署合同 */
    handleContractSign(row) {
      this.$modal.confirm(`是否确认签署合同"${row.contractNo}"？`).then(() => {
        signCreditContract(row.id).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("签署成功");
            this.getContractList();
          } else {
            this.$modal.msgError(response.message || '签署失败');
          }
        }).catch(error => {
          console.error('签署合同失败:', error);
          this.$modal.msgError('签署失败，请稍后重试');
        });
      });
    },

    /** 终止合同 */
    handleContractTerminate(row) {
      this.$prompt('请输入终止原因', '终止合同', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '终止原因不能为空'
      }).then(({ value }) => {
        terminateCreditContract(row.id, value).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("终止成功");
            this.getContractList();
          } else {
            this.$modal.msgError(response.message || '终止失败');
          }
        }).catch(error => {
          console.error('终止合同失败:', error);
          this.$modal.msgError('终止失败，请稍后重试');
        });
      });
    },

    /** 导出按钮操作 */
    handleContractExport() {
      exportCreditContracts(this.contractQuery).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `credit_contracts_${new Date().getTime()}.xlsx`;
        link.click();
        window.URL.revokeObjectURL(url);
      }).catch(error => {
        console.error('导出失败:', error);
        this.$modal.msgError('导出失败，请稍后重试');
      });
    },

    /** 提交按钮 */
    submitContractForm() {
      this.$refs["contractForm"].validate(valid => {
        if (valid) {
          if (this.contractForm.id != null) {
            this.updateContract();
          } else {
            this.addContract();
          }
        }
      });
    },

    /** 新增授信合同 */
    addContract() {
      createCreditContract(this.contractForm).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess("新增成功");
          this.contractOpen = false;
          this.getContractList();
        } else {
          this.$modal.msgError(response.message || '新增失败');
        }
      }).catch(error => {
        console.error('新增授信合同失败:', error);
        this.$modal.msgError('新增失败，请稍后重试');
      });
    },

    /** 修改授信合同 */
    updateContract() {
      updateCreditContract(this.contractForm.id, this.contractForm).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess("修改成功");
          this.contractOpen = false;
          this.getContractList();
        } else {
          this.$modal.msgError(response.message || '修改失败');
        }
      }).catch(error => {
        console.error('修改授信合同失败:', error);
        this.$modal.msgError('修改失败，请稍后重试');
      });
    },

    /** 取消按钮 */
    cancelContract() {
      this.contractOpen = false;
      this.resetContractForm();
    },

    /** 表单重置 */
    resetContractForm() {
      this.contractForm = {
        id: null,
        contractNo: null,
        applicationId: null,
        companyId: null,
        financingInstitutionId: null,
        creditType: null,
        creditAmount: null,
        currencyCode: 'CNY',
        creditTerm: null,
        contractStartDate: null,
        contractEndDate: null,
        guaranteeMethod: null,
        guaranteeDetails: null,
        interestRateType: null,
        baseRate: null,
        rateSpread: null,
        commitmentFeeRate: null,
        managementFeeRate: null,
        specialClauses: null,
        defaultClauses: null,
        contractStatus: 'EFFECTIVE',
        remark: null
      };
      this.resetForm("contractForm");
    },

    // ==================== 授信额度管理方法 ====================

    /** 查询授信额度列表 */
    getLimitList() {
      this.limitLoading = true;
      getCreditLimitPage(this.limitQuery).then(response => {
        if (response.code === 200) {
          this.limitList = response.data || [];
          this.limitTotal = response.total || 0;
        } else {
          this.limitList = [];
          this.limitTotal = 0;
          console.error('获取授信额度列表失败:', response.message);
        }
        this.limitLoading = false;
      }).catch(error => {
        console.error('获取授信额度列表失败:', error);
        this.limitList = [];
        this.limitTotal = 0;
        this.limitLoading = false;
      });
    },

    /** 授信额度搜索按钮操作 */
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
      this.limitIds = selection.map(item => item.id);
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
    handleLimitUpdate(row) {
      this.resetLimitForm();
      const limitId = row.id || this.limitIds[0];
      getCreditLimit(limitId).then(response => {
        if (response.code === 200) {
          this.limitForm = response.data;
          this.limitOpen = true;
          this.limitTitle = "修改授信额度";
        } else {
          this.$modal.msgError(response.message || '获取授信额度信息失败');
        }
      }).catch(error => {
        console.error('获取授信额度信息失败:', error);
        this.$modal.msgError('获取授信额度信息失败，请稍后重试');
      });
    },

    /** 查看按钮操作 */
    handleLimitView(row) {
      this.resetLimitForm();
      const limitId = row.id;
      getCreditLimit(limitId).then(response => {
        if (response.code === 200) {
          this.limitForm = response.data;
          this.limitOpen = true;
          this.limitTitle = "查看授信额度";
        } else {
          this.$modal.msgError(response.message || '获取授信额度信息失败');
        }
      }).catch(error => {
        console.error('获取授信额度信息失败:', error);
        this.$modal.msgError('获取授信额度信息失败，请稍后重试');
      });
    },

    /** 删除按钮操作 */
    handleLimitDelete(row) {
      const limitIds = row.id ? [row.id] : this.limitIds;
      this.$modal.confirm('是否确认删除授信额度编号为"' + limitIds + '"的数据项？').then(function() {
        return deleteCreditLimit(limitIds);
      }).then((response) => {
        if (response.code === 200) {
          this.getLimitList();
          this.$modal.msgSuccess("删除成功");
        } else {
          this.$modal.msgError(response.message || '删除失败');
        }
      }).catch(error => {
        console.error('删除授信额度失败:', error);
        this.$modal.msgError('删除失败，请稍后重试');
      });
    },

    /** 额度操作命令处理 */
    handleLimitCommand(command, row) {
      this.currentLimitId = row.id;
      this.limitActionForm = {
        amount: null,
        reason: null
      };

      switch (command) {
        case 'use':
          this.limitActionTitle = "使用额度";
          this.limitActionType = "use";
          this.limitActionOpen = true;
          break;
        case 'repay':
          this.limitActionTitle = "归还额度";
          this.limitActionType = "repay";
          this.limitActionOpen = true;
          break;
        case 'freeze':
          this.limitActionTitle = "冻结额度";
          this.limitActionType = "freeze";
          this.limitActionOpen = true;
          break;
        case 'unfreeze':
          this.limitActionTitle = "解冻额度";
          this.limitActionType = "unfreeze";
          this.limitActionOpen = true;
          break;
        case 'suspend':
          this.handleLimitSuspend(row.id);
          break;
        case 'activate':
          this.handleLimitActivate(row.id);
          break;
      }
    },

    /** 暂停额度 */
    handleLimitSuspend(limitId) {
      this.$modal.confirm('是否确认暂停该授信额度？').then(() => {
        return suspendCreditLimit(limitId, '手动暂停');
      }).then((response) => {
        if (response.code === 200) {
          this.getLimitList();
          this.$modal.msgSuccess("暂停成功");
        } else {
          this.$modal.msgError(response.message || '暂停失败');
        }
      }).catch(error => {
        console.error('暂停额度失败:', error);
        this.$modal.msgError('暂停失败，请稍后重试');
      });
    },

    /** 激活额度 */
    handleLimitActivate(limitId) {
      this.$modal.confirm('是否确认激活该授信额度？').then(() => {
        return activateCreditLimit(limitId, '手动激活');
      }).then((response) => {
        if (response.code === 200) {
          this.getLimitList();
          this.$modal.msgSuccess("激活成功");
        } else {
          this.$modal.msgError(response.message || '激活失败');
        }
      }).catch(error => {
        console.error('激活额度失败:', error);
        this.$modal.msgError('激活失败，请稍后重试');
      });
    },

    /** 导出按钮操作 */
    handleLimitExport() {
      exportCreditLimits(this.limitQuery).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `credit_limits_${new Date().getTime()}.xlsx`;
        link.click();
        window.URL.revokeObjectURL(url);
      }).catch(error => {
        console.error('导出失败:', error);
        this.$modal.msgError('导出失败，请稍后重试');
      });
    },

    /** 提交按钮 */
    submitLimitForm() {
      this.$refs["limitForm"].validate(valid => {
        if (valid) {
          if (this.limitForm.id != null) {
            this.updateLimit();
          } else {
            this.addLimit();
          }
        }
      });
    },

    /** 新增授信额度 */
    addLimit() {
      createCreditLimit(this.limitForm).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess("新增成功");
          this.limitOpen = false;
          this.getLimitList();
        } else {
          this.$modal.msgError(response.message || '新增失败');
        }
      }).catch(error => {
        console.error('新增授信额度失败:', error);
        this.$modal.msgError('新增失败，请稍后重试');
      });
    },

    /** 修改授信额度 */
    updateLimit() {
      updateCreditLimit(this.limitForm.id, this.limitForm).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess("修改成功");
          this.limitOpen = false;
          this.getLimitList();
        } else {
          this.$modal.msgError(response.message || '修改失败');
        }
      }).catch(error => {
        console.error('修改授信额度失败:', error);
        this.$modal.msgError('修改失败，请稍后重试');
      });
    },

    /** 取消按钮 */
    cancelLimit() {
      this.limitOpen = false;
      this.resetLimitForm();
    },

    /** 表单重置 */
    resetLimitForm() {
      this.limitForm = {
        id: null,
        limitCode: null,
        contractId: null,
        companyId: null,
        financingInstitutionId: null,
        creditType: null,
        totalLimit: null,
        usedLimit: 0,
        frozenLimit: 0,
        currencyCode: 'CNY',
        effectiveDate: null,
        expiryDate: null,
        interestRateType: null,
        baseRate: null,
        rateSpread: null,
        commitmentFeeRate: null,
        limitStatus: 'ACTIVE'
      };
      this.resetForm("limitForm");
    },

    /** 提交额度操作 */
    submitLimitAction() {
      this.$refs["limitActionForm"].validate(valid => {
        if (valid) {
          const { amount, reason } = this.limitActionForm;
          let apiCall;

          switch (this.limitActionType) {
            case 'use':
              apiCall = useCreditLimit(this.currentLimitId, amount, reason);
              break;
            case 'repay':
              apiCall = repayCreditLimit(this.currentLimitId, amount);
              break;
            case 'freeze':
              apiCall = freezeCreditLimit(this.currentLimitId, amount, reason);
              break;
            case 'unfreeze':
              apiCall = unfreezeCreditLimit(this.currentLimitId, amount, reason);
              break;
          }

          if (apiCall) {
            apiCall.then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("操作成功");
                this.limitActionOpen = false;
                this.getLimitList();
              } else {
                this.$modal.msgError(response.message || '操作失败');
              }
            }).catch(error => {
              console.error('额度操作失败:', error);
              this.$modal.msgError('操作失败，请稍后重试');
            });
          }
        }
      });
    },

    /** 取消额度操作 */
    cancelLimitAction() {
      this.limitActionOpen = false;
      this.limitActionForm = {
        amount: null,
        reason: null
      };
    },

    /** 计算使用率 */
    getUsageRatio(row) {
      if (!row.totalLimit || row.totalLimit === 0) return 0;
      return Math.round((row.usedLimit / row.totalLimit) * 100);
    },

    /** 获取使用率颜色 */
    getUsageColor(row) {
      const ratio = this.getUsageRatio(row);
      if (ratio >= 90) return '#F56C6C';
      if (ratio >= 70) return '#E6A23C';
      if (ratio >= 50) return '#409EFF';
      return '#67C23A';
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
</style>
