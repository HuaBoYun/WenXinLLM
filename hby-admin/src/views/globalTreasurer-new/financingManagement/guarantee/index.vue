<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 担保申请管理 -->
      <el-tab-pane label="担保申请管理" name="application">
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
            <el-form-item label="担保类型" prop="guaranteeType">
              <el-select v-model="applicationQuery.guaranteeType" placeholder="请选择担保类型" clearable>
                <el-option label="保证担保" value="GUARANTEE" />
                <el-option label="抵押担保" value="MORTGAGE" />
                <el-option label="质押担保" value="PLEDGE" />
                <el-option label="信用担保" value="CREDIT" />
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
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="applicationLoading" :data="applicationList" @selection-change="handleApplicationSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="申请编号" align="center" prop="applicationNo" />
            <el-table-column label="担保类型" align="center" prop="guaranteeType">
              <template slot-scope="scope">
                <el-tag :type="getAppGuaranteeTypeTagType(scope.row.guaranteeType)">
                  {{ getAppGuaranteeTypeLabel(scope.row.guaranteeType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="担保金额" align="center" prop="guaranteeAmount">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.guaranteeAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="被担保方" align="center" prop="guaranteedParty" />
            <el-table-column label="申请状态" align="center" prop="applicationStatus">
              <template slot-scope="scope">
                <el-tag :type="getAppStatusTagType(scope.row.applicationStatus)">
                  {{ getAppStatusLabel(scope.row.applicationStatus) }}
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

      <!-- 担保合同管理 -->
      <el-tab-pane label="担保合同管理" name="contract">
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
            <el-form-item label="合同类型" prop="contractType">
              <el-select v-model="contractQuery.contractType" placeholder="请选择合同类型" clearable>
                <el-option label="保证合同" value="GUARANTEE" />
                <el-option label="抵押合同" value="MORTGAGE" />
                <el-option label="质押合同" value="PLEDGE" />
                <el-option label="留置合同" value="LIEN" />
              </el-select>
            </el-form-item>
            <el-form-item label="担保类型" prop="guaranteeType">
              <el-select v-model="contractQuery.guaranteeType" placeholder="请选择担保类型" clearable>
                <el-option label="一般担保" value="GENERAL" />
                <el-option label="连带担保" value="JOINT" />
                <el-option label="最高额担保" value="MAXIMUM" />
              </el-select>
            </el-form-item>
            <el-form-item label="合同状态" prop="contractStatus">
              <el-select v-model="contractQuery.contractStatus" placeholder="请选择合同状态" clearable>
                <el-option label="草稿" value="DRAFT" />
                <el-option label="待审批" value="PENDING" />
                <el-option label="已审批" value="APPROVED" />
                <el-option label="已签署" value="SIGNED" />
                <el-option label="已生效" value="EFFECTIVE" />
                <el-option label="已终止" value="TERMINATED" />
                <el-option label="已取消" value="CANCELLED" />
              </el-select>
            </el-form-item>
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="contractQuery.riskLevel" placeholder="请选择风险等级" clearable>
                <el-option label="低风险" value="LOW" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="高风险" value="HIGH" />
                <el-option label="严重风险" value="CRITICAL" />
              </el-select>
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
                @click="handleContractUpdate"
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
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="contractLoading" :data="contractList" @selection-change="handleContractSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="合同编号" align="center" prop="contractNo" />
            <el-table-column label="合同类型" align="center" prop="contractType">
              <template slot-scope="scope">
                <el-tag :type="getContractTypeTagType(scope.row.contractType)">
                  {{ getContractTypeLabel(scope.row.contractType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="担保类型" align="center" prop="guaranteeType">
              <template slot-scope="scope">
                <el-tag :type="getGuaranteeTypeTagType(scope.row.guaranteeType)">
                  {{ getGuaranteeTypeLabel(scope.row.guaranteeType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="担保金额" align="center" prop="guaranteeAmount">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.guaranteeAmount, scope.row.currencyCode) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="公司名称" align="center" prop="companyName" />
            <el-table-column label="担保人" align="center" prop="guarantorCompany" />
            <el-table-column label="被担保人" align="center" prop="beneficiaryCompany" />
            <el-table-column label="合同状态" align="center" prop="contractStatus">
              <template slot-scope="scope">
                <el-tag :type="getContractStatusTagType(scope.row.contractStatus)">
                  {{ getContractStatusLabel(scope.row.contractStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="合同期限" align="center" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }} 至 {{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-dropdown size="mini" split-button type="text" @click="handleContractDetail(scope.row)">
                  查看
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item
                      v-if="scope.row.contractStatus === 'DRAFT'"
                      @click.native.stop="handleContractUpdate(scope.row)"
                    >修改</el-dropdown-item>
                    <el-dropdown-item
                      v-if="scope.row.contractStatus === 'DRAFT'"
                      @click.native.stop="handleContractSubmit(scope.row)"
                    >提交审批</el-dropdown-item>
                    <el-dropdown-item
                      v-if="scope.row.contractStatus === 'PENDING'"
                      @click.native.stop="handleContractApprove(scope.row)"
                    >审批</el-dropdown-item>
                    <el-dropdown-item
                      v-if="scope.row.contractStatus === 'APPROVED'"
                      @click.native.stop="handleContractSign(scope.row)"
                    >签署</el-dropdown-item>
                    <el-dropdown-item
                      v-if="scope.row.contractStatus === 'SIGNED'"
                      @click.native.stop="handleContractActivate(scope.row)"
                    >生效</el-dropdown-item>
                    <el-dropdown-item
                      v-if="scope.row.contractStatus === 'EFFECTIVE'"
                      @click.native.stop="handleContractTerminate(scope.row)"
                    >终止</el-dropdown-item>
                    <el-dropdown-item
                      v-if="['DRAFT', 'PENDING'].includes(scope.row.contractStatus)"
                      @click.native.stop="handleContractCancel(scope.row)"
                    >取消</el-dropdown-item>
                    <el-dropdown-item
                      v-if="scope.row.contractStatus === 'DRAFT'"
                      @click.native.stop="handleContractDelete(scope.row)"
                      divided
                    >删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <pagination
            v-show="contractTotal>0"
            :total="contractTotal"
            :page.sync="contractQuery.pageNum"
            :limit.sync="contractQuery.pageSize"
            @pagination="getContractList"
          />
        </div>
      </el-tab-pane>

      <!-- 担保物管理 -->
      <el-tab-pane label="担保物管理" name="collateral">
        <div class="collateral-container">
          <!-- 担保物概览 -->
          <div class="overview-cards" style="margin-bottom: 20px;">
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="overview-card">
                  <div class="card-header">
                    <span>担保物总数</span>
                    <i class="el-icon-files" style="color: #409EFF;"></i>
                  </div>
                  <div class="card-value">{{ collateralOverview.totalCount || 0 }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="overview-card">
                  <div class="card-header">
                    <span>总价值</span>
                    <i class="el-icon-money" style="color: #67C23A;"></i>
                  </div>
                  <div class="card-value">{{ formatAmount(collateralOverview.totalValue) }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="overview-card">
                  <div class="card-header">
                    <span>可用担保物</span>
                    <i class="el-icon-check" style="color: #67C23A;"></i>
                  </div>
                  <div class="card-value">{{ collateralOverview.availableCount || 0 }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="overview-card">
                  <div class="card-header">
                    <span>高风险担保物</span>
                    <i class="el-icon-warning" style="color: #F56C6C;"></i>
                  </div>
                  <div class="card-value">{{ collateralOverview.highRiskCount || 0 }}</div>
                </div>
              </el-col>
            </el-row>
          </div>

          <!-- 查询表单 -->
          <el-form :model="collateralQuery" ref="collateralQueryForm" size="small" :inline="true" v-show="collateralShowSearch" label-width="68px">
            <el-form-item label="担保物编号" prop="collateralNo">
              <el-input
                v-model="collateralQuery.collateralNo"
                placeholder="请输入担保物编号"
                clearable
                @keyup.enter.native="handleCollateralQuery"
              />
            </el-form-item>
            <el-form-item label="担保物名称" prop="collateralName">
              <el-input
                v-model="collateralQuery.collateralName"
                placeholder="请输入担保物名称"
                clearable
                @keyup.enter.native="handleCollateralQuery"
              />
            </el-form-item>
            <el-form-item label="担保物类型" prop="collateralType">
              <el-select v-model="collateralQuery.collateralType" placeholder="请选择担保物类型" clearable>
                <el-option
                  v-for="dict in collateralTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="担保物状态" prop="collateralStatus">
              <el-select v-model="collateralQuery.collateralStatus" placeholder="请选择担保物状态" clearable>
                <el-option
                  v-for="dict in collateralStatusOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="collateralQuery.riskLevel" placeholder="请选择风险等级" clearable>
                <el-option
                  v-for="dict in riskLevelOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleCollateralQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetCollateralQuery">重置</el-button>
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
                @click="handleCollateralAdd"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="collateralSingle"
                @click="handleCollateralUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="collateralMultiple"
                @click="handleCollateralDelete"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleCollateralExport"
              >导出</el-button>
            </el-col>
          </el-row>

          <!-- 担保物列表 -->
          <el-table v-loading="collateralLoading" :data="collateralList" @selection-change="handleCollateralSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="担保物编号" align="center" prop="collateralNo" />
            <el-table-column label="担保物名称" align="center" prop="collateralName" show-overflow-tooltip />
            <el-table-column label="担保物类型" align="center" prop="collateralType">
              <template slot-scope="scope">
                <el-tag :type="getCollateralTypeTagType(scope.row.collateralType)">
                  {{ getCollateralTypeLabel(scope.row.collateralType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="所有者" align="center" prop="ownerName" show-overflow-tooltip />
            <el-table-column label="当前价值" align="center" prop="collateralValue">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.collateralValue || scope.row.evaluationValue, scope.row.currencyCode) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="评估价值" align="center" prop="evaluationValue">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.evaluationValue, scope.row.currencyCode) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="担保物状态" align="center" prop="collateralStatus">
              <template slot-scope="scope">
                <el-tag :type="getCollateralStatusTagType(scope.row.collateralStatus)">
                  {{ getCollateralStatusLabel(scope.row.collateralStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="风险等级" align="center" prop="collateralStatus">
              <template slot-scope="scope">
                <el-tag :type="getCollateralRiskTagType(scope.row.collateralStatus)">
                  {{ getCollateralRiskLabel(scope.row.collateralStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-dropdown size="mini" @command="(command) => handleCollateralCommand(command, scope.row)" v-hasPermi="['financing:guarantee:collateral:edit']">
                  <el-button size="mini" type="primary">
                    操作<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="detail" icon="el-icon-view">查看详情</el-dropdown-item>
                    <el-dropdown-item command="edit" icon="el-icon-edit" v-if="scope.row.collateralStatus === 'AVAILABLE'">编辑</el-dropdown-item>
                    <el-dropdown-item command="freeze" icon="el-icon-lock" v-if="scope.row.collateralStatus === 'AVAILABLE'">冻结</el-dropdown-item>
                    <el-dropdown-item command="unfreeze" icon="el-icon-unlock" v-if="scope.row.collateralStatus === 'FROZEN'">解冻</el-dropdown-item>
                    <el-dropdown-item command="mortgage" icon="el-icon-house" v-if="scope.row.collateralStatus === 'AVAILABLE'">抵押</el-dropdown-item>
                    <el-dropdown-item command="pledge" icon="el-icon-goods" v-if="scope.row.collateralStatus === 'AVAILABLE'">质押</el-dropdown-item>
                    <el-dropdown-item command="release" icon="el-icon-refresh-left" v-if="['MORTGAGED', 'PLEDGED'].includes(scope.row.collateralStatus)">释放</el-dropdown-item>
                    <el-dropdown-item command="dispose" icon="el-icon-delete" v-if="scope.row.collateralStatus !== 'DISPOSED'">处置</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="collateralTotal > 0"
            :total="collateralTotal"
            :page.sync="collateralQuery.pageNum"
            :limit.sync="collateralQuery.pageSize"
            @pagination="getCollateralList"
          />
        </div>
      </el-tab-pane>

      <!-- 担保监控 -->
      <el-tab-pane label="担保监控" name="monitoring">
        <div class="monitoring-container">
          <!-- 监控概览 -->
          <div class="overview-cards" style="margin-bottom: 20px;">
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="overview-card">
                  <div class="card-header">
                    <span>预警总数</span>
                    <i class="el-icon-warning" style="color: #E6A23C;"></i>
                  </div>
                  <div class="card-value">{{ monitoringOverview.total_count || 0 }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="overview-card">
                  <div class="card-header">
                    <span>活跃预警</span>
                    <i class="el-icon-bell" style="color: #F56C6C;"></i>
                  </div>
                  <div class="card-value">{{ monitoringOverview.active_count || 0 }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="overview-card">
                  <div class="card-header">
                    <span>高风险预警</span>
                    <i class="el-icon-warning-outline" style="color: #F56C6C;"></i>
                  </div>
                  <div class="card-value">{{ monitoringOverview.high_risk_count || 0 }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="overview-card">
                  <div class="card-header">
                    <span>需要跟进</span>
                    <i class="el-icon-time" style="color: #909399;"></i>
                  </div>
                  <div class="card-value">{{ monitoringOverview.follow_up_count || 0 }}</div>
                </div>
              </el-col>
            </el-row>
          </div>

          <!-- 查询条件 -->
          <el-form :model="monitoringQuery" ref="monitoringQueryForm" size="small" :inline="true" v-show="monitoringShowSearch" label-width="68px">
            <el-form-item label="预警编号" prop="alertNo">
              <el-input
                v-model="monitoringQuery.alertNo"
                placeholder="请输入预警编号"
                clearable
                @keyup.enter.native="handleMonitoringQuery"
              />
            </el-form-item>
            <el-form-item label="预警类型" prop="alertType">
              <el-select v-model="monitoringQuery.alertType" placeholder="请选择预警类型" clearable>
                <el-option
                  v-for="option in monitoringTypeOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="预警级别" prop="alertLevel">
              <el-select v-model="monitoringQuery.alertLevel" placeholder="请选择预警级别" clearable>
                <el-option
                  v-for="option in monitoringLevelOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="预警状态" prop="alertStatus">
              <el-select v-model="monitoringQuery.alertStatus" placeholder="请选择预警状态" clearable>
                <el-option
                  v-for="option in monitoringStatusOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="公司名称" prop="companyName">
              <el-input
                v-model="monitoringQuery.companyName"
                placeholder="请输入公司名称"
                clearable
                @keyup.enter.native="handleMonitoringQuery"
              />
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
              >新增预警</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="monitoringSingle"
                @click="handleMonitoringUpdate"
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
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleMonitoringExport"
              >导出</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="monitoringLoading" :data="monitoringList" @selection-change="handleMonitoringSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="预警编号" align="center" prop="alertNo" />
            <el-table-column label="预警类型" align="center" prop="alertType">
              <template slot-scope="scope">
                <el-tag :type="getMonitoringTypeTagType(scope.row.alertType)">
                  {{ getMonitoringTypeLabel(scope.row.alertType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="预警级别" align="center" prop="alertLevel">
              <template slot-scope="scope">
                <el-tag :type="getMonitoringLevelTagType(scope.row.alertLevel)">
                  {{ getMonitoringLevelLabel(scope.row.alertLevel) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="预警内容" align="center" prop="alertMessage" :show-overflow-tooltip="true" />
            <el-table-column label="公司名称" align="center" prop="companyName" />
            <el-table-column label="预警状态" align="center" prop="alertStatus">
              <template slot-scope="scope">
                <el-tag :type="getMonitoringStatusTagType(scope.row.alertStatus)">
                  {{ getMonitoringStatusLabel(scope.row.alertStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="预警日期" align="center" prop="alertDate" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.alertDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-dropdown size="mini" @command="(command) => handleMonitoringCommand(command, scope.row)" v-hasPermi="['financing:guarantee:monitoring:edit']">
                  <el-button size="mini" type="primary">
                    操作<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="detail" icon="el-icon-view">查看详情</el-dropdown-item>
                    <el-dropdown-item command="edit" icon="el-icon-edit">修改</el-dropdown-item>
                    <el-dropdown-item command="handle" icon="el-icon-check" v-if="scope.row.alertStatus === 'ACTIVE'">处理</el-dropdown-item>
                    <el-dropdown-item command="close" icon="el-icon-close" v-if="scope.row.alertStatus === 'ACTIVE' || scope.row.alertStatus === 'HANDLED'">关闭</el-dropdown-item>
                    <el-dropdown-item command="reactivate" icon="el-icon-refresh" v-if="scope.row.alertStatus === 'CLOSED'">重新激活</el-dropdown-item>
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

    <!-- 添加或修改担保申请对话框 -->
    <el-dialog :title="applicationTitle" :visible.sync="applicationOpen" width="800px" append-to-body>
      <el-form ref="applicationForm" :model="applicationForm" :rules="applicationRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="担保类型" prop="guaranteeType">
              <el-select v-model="applicationForm.guaranteeType" placeholder="请选择担保类型">
                <el-option label="保证担保" value="GUARANTEE" />
                <el-option label="抵押担保" value="MORTGAGE" />
                <el-option label="质押担保" value="PLEDGE" />
                <el-option label="信用担保" value="CREDIT" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="担保金额" prop="guaranteeAmount">
              <el-input v-model="applicationForm.guaranteeAmount" placeholder="请输入担保金额">
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="被担保方" prop="guaranteedParty">
              <el-input v-model="applicationForm.guaranteedParty" placeholder="请输入被担保方" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="担保期限" prop="guaranteeTerm">
              <el-input v-model="applicationForm.guaranteeTerm" placeholder="请输入担保期限">
                <template slot="append">月</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="担保事由" prop="guaranteeReason">
          <el-input v-model="applicationForm.guaranteeReason" type="textarea" placeholder="请输入担保事由" />
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

    <!-- 添加或修改担保合同对话框 -->
    <el-dialog :title="contractTitle" :visible.sync="contractOpen" width="900px" append-to-body>
      <el-form ref="contractForm" :model="contractForm" :rules="contractRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyName">
              <el-input v-model="contractForm.companyName" placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="担保人" prop="guarantorCompany">
              <el-input v-model="contractForm.guarantorCompany" placeholder="请输入担保人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="被担保人" prop="beneficiaryCompany">
              <el-input v-model="contractForm.beneficiaryCompany" placeholder="请输入被担保人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同类型" prop="contractType">
              <el-select v-model="contractForm.contractType" placeholder="请选择合同类型">
                <el-option label="保证合同" value="GUARANTEE" />
                <el-option label="抵押合同" value="MORTGAGE" />
                <el-option label="质押合同" value="PLEDGE" />
                <el-option label="留置合同" value="LIEN" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="担保类型" prop="guaranteeType">
              <el-select v-model="contractForm.guaranteeType" placeholder="请选择担保类型">
                <el-option label="一般担保" value="GENERAL" />
                <el-option label="连带担保" value="JOINT" />
                <el-option label="最高额担保" value="MAXIMUM" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="担保方式" prop="guaranteeMethod">
              <el-select v-model="contractForm.guaranteeMethod" placeholder="请选择担保方式">
                <el-option label="保证" value="GUARANTEE" />
                <el-option label="抵押" value="MORTGAGE" />
                <el-option label="质押" value="PLEDGE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="担保金额" prop="guaranteeAmount">
              <el-input v-model="contractForm.guaranteeAmount" placeholder="请输入担保金额">
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始日期" prop="contractStartDate">
              <el-date-picker v-model="contractForm.contractStartDate" type="date" placeholder="选择开始日期" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="contractEndDate">
              <el-date-picker v-model="contractForm.contractEndDate" type="date" placeholder="选择结束日期" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="contractForm.riskLevel" placeholder="请选择风险等级">
                <el-option label="低风险" value="LOW" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="高风险" value="HIGH" />
                <el-option label="严重风险" value="CRITICAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="担保比例" prop="guaranteeRatio">
              <el-input v-model="contractForm.guaranteeRatio" placeholder="请输入担保比例">
                <template slot="append">%</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="担保范围" prop="guaranteeScope">
          <el-input v-model="contractForm.guaranteeScope" type="textarea" placeholder="请输入担保范围" />
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

    <!-- 添加或修改担保物对话框 -->
    <el-dialog :title="collateralTitle" :visible.sync="collateralOpen" width="900px" append-to-body>
      <el-form ref="collateralForm" :model="collateralForm" :rules="collateralRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="担保物编号" prop="collateralNo">
              <el-input v-model="collateralForm.collateralNo" placeholder="请输入担保物编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="担保物名称" prop="collateralName">
              <el-input v-model="collateralForm.collateralName" placeholder="请输入担保物名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="所有者" prop="ownerName">
              <el-input v-model="collateralForm.ownerName" placeholder="请输入所有者名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属公司" prop="companyName">
              <el-input v-model="collateralForm.companyName" placeholder="请输入所属公司名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="担保物类型" prop="collateralType">
              <el-select v-model="collateralForm.collateralType" placeholder="请选择担保物类型">
                <el-option v-for="item in collateralTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="担保物分类" prop="collateralCategory">
              <el-select v-model="collateralForm.collateralCategory" placeholder="请选择担保物分类">
                <el-option v-for="item in collateralCategoryOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="原始价值" prop="originalValue">
              <el-input v-model="collateralForm.originalValue" placeholder="请输入原始价值">
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前价值" prop="currentValue">
              <el-input v-model="collateralForm.currentValue" placeholder="请输入当前价值">
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="评估日期" prop="valuationDate">
              <el-date-picker v-model="collateralForm.valuationDate" type="date" placeholder="选择评估日期" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估机构" prop="valuationAgency">
              <el-input v-model="collateralForm.valuationAgency" placeholder="请输入评估机构" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="币种" prop="currency">
              <el-select v-model="collateralForm.currency" placeholder="请选择币种">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="担保物状态" prop="collateralStatus">
              <el-select v-model="collateralForm.collateralStatus" placeholder="请选择担保物状态">
                <el-option label="正常" value="ACTIVE" />
                <el-option label="可用" value="AVAILABLE" />
                <el-option label="已抵押" value="MORTGAGED" />
                <el-option label="已质押" value="PLEDGED" />
                <el-option label="已冻结" value="FROZEN" />
                <el-option label="已处置" value="DISPOSED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="存放位置" prop="location">
          <el-input v-model="collateralForm.location" placeholder="请输入存放位置" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="collateralForm.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCollateralForm">确 定</el-button>
        <el-button @click="cancelCollateral">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改担保监控预警对话框 -->
    <el-dialog :title="monitoringTitle" :visible.sync="monitoringOpen" width="800px" append-to-body>
      <el-form ref="monitoringForm" :model="monitoringForm" :rules="monitoringRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="预警类型" prop="alertType">
              <el-select v-model="monitoringForm.alertType" placeholder="请选择预警类型">
                <el-option v-for="item in monitoringTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警级别" prop="alertLevel">
              <el-select v-model="monitoringForm.alertLevel" placeholder="请选择预警级别">
                <el-option v-for="item in monitoringLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预警日期" prop="alertDate">
              <el-date-picker v-model="monitoringForm.alertDate" type="date" placeholder="选择预警日期" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警状态" prop="alertStatus">
              <el-select v-model="monitoringForm.alertStatus" placeholder="请选择预警状态">
                <el-option v-for="item in monitoringStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
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
            <el-form-item label="关联担保ID" prop="relatedGuaranteeId">
              <el-input v-model="monitoringForm.relatedGuaranteeId" placeholder="请输入关联担保ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="预警标题" prop="alertTitle">
          <el-input v-model="monitoringForm.alertTitle" placeholder="请输入预警标题" />
        </el-form-item>
        <el-form-item label="预警内容" prop="alertMessage">
          <el-input v-model="monitoringForm.alertMessage" type="textarea" :rows="3" placeholder="请输入预警内容" />
        </el-form-item>
        <el-form-item label="建议措施" prop="suggestedAction">
          <el-input v-model="monitoringForm.suggestedAction" type="textarea" :rows="2" placeholder="请输入建议措施" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMonitoringForm">确 定</el-button>
        <el-button @click="cancelMonitoring">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getGuaranteeApplicationPage,
  getGuaranteeApplication,
  createGuaranteeApplication,
  updateGuaranteeApplication,
  deleteGuaranteeApplication,
  getGuaranteeContractPage,
  getGuaranteeContract,
  createGuaranteeContract,
  updateGuaranteeContract,
  deleteGuaranteeContract,
  submitGuaranteeContract,
  approveGuaranteeContract,
  signGuaranteeContract,
  activateGuaranteeContract,
  terminateGuaranteeContract,
  cancelGuaranteeContract,
  exportGuaranteeContracts,
  // 担保物管理API
  getCollateralPage,
  getCollateral,
  createCollateral,
  updateCollateral,
  deleteCollateral,
  batchDeleteCollaterals,
  freezeCollateral,
  unfreezeCollateral,
  mortgageCollateral,
  pledgeCollateral,
  releaseCollateral,
  disposeCollateral,
  getCollateralOverview as fetchCollateralOverview,
  checkCollateralNoExists,
  generateCollateralNo,
  getGuaranteeMonitoringPage,
  getGuaranteeMonitoring,
  createGuaranteeMonitoring,
  updateGuaranteeMonitoring,
  deleteGuaranteeMonitoring,
  batchDeleteGuaranteeMonitorings,
  handleGuaranteeMonitoring,
  closeGuaranteeMonitoring,
  reactivateGuaranteeMonitoring,
  batchHandleGuaranteeMonitorings,
  batchCloseGuaranteeMonitorings,
  getGuaranteeMonitoringStatistics,
  getGuaranteeMonitoringTrend,
  getGuaranteeMonitoringDistribution,
  checkGuaranteeMonitoringNoExists,
  generateGuaranteeMonitoringNo
} from "@/api/globalTreasurer/rzgl";
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'

export default {
  name: "GuaranteeManagement",
  components: {
    Pagination
  },
  data() {
    return {
      // 当前激活的标签页
      activeTab: "application",
      // 显示搜索条件
      showSearch: true,
      
      // 担保申请相关数据
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
        guaranteeType: null,
        applicationStatus: null
      },
      applicationForm: {},
      applicationRules: {
        guaranteeType: [
          { required: true, message: "担保类型不能为空", trigger: "change" }
        ],
        guaranteeAmount: [
          { required: true, message: "担保金额不能为空", trigger: "blur" }
        ],
        guaranteedParty: [
          { required: true, message: "被担保方不能为空", trigger: "blur" }
        ],
        guaranteeTerm: [
          { required: true, message: "担保期限不能为空", trigger: "blur" }
        ],
        guaranteeReason: [
          { required: true, message: "担保事由不能为空", trigger: "blur" }
        ]
      },

      // 担保合同相关数据
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
        contractType: null,
        guaranteeType: null,
        contractStatus: null,
        riskLevel: null,
        startDate: null,
        endDate: null,
        orgId: 1
      },
      contractForm: {},
      contractRules: {
        contractType: [
          { required: true, message: "合同类型不能为空", trigger: "change" }
        ],
        guaranteeType: [
          { required: true, message: "担保类型不能为空", trigger: "change" }
        ],
        guaranteeMethod: [
          { required: true, message: "担保方式不能为空", trigger: "change" }
        ],
        guaranteeAmount: [
          { required: true, message: "担保金额不能为空", trigger: "blur" }
        ],
        contractStartDate: [
          { required: true, message: "合同开始日期不能为空", trigger: "change" }
        ],
        contractEndDate: [
          { required: true, message: "合同结束日期不能为空", trigger: "change" }
        ],
        companyId: [
          { required: true, message: "公司不能为空", trigger: "change" }
        ],
        guarantorId: [
          { required: true, message: "担保人不能为空", trigger: "change" }
        ],
        guaranteedPartyId: [
          { required: true, message: "被担保人不能为空", trigger: "change" }
        ]
      },

      // 字典选项
      guaranteeTypeOptions: [
        { label: "保证担保", value: "GUARANTEE" },
        { label: "抵押担保", value: "MORTGAGE" },
        { label: "质押担保", value: "PLEDGE" },
        { label: "信用担保", value: "CREDIT" }
      ],
      applicationStatusOptions: [
        { label: "草稿", value: "DRAFT" },
        { label: "待审批", value: "PENDING_APPROVAL" },
        { label: "已审批", value: "APPROVED" },
        { label: "已拒绝", value: "REJECTED" }
      ],
      contractTypeOptions: [
        { label: "保证合同", value: "GUARANTEE" },
        { label: "抵押合同", value: "MORTGAGE" },
        { label: "质押合同", value: "PLEDGE" },
        { label: "留置合同", value: "LIEN" }
      ],
      contractStatusOptions: [
        { label: "草稿", value: "DRAFT" },
        { label: "待审批", value: "PENDING" },
        { label: "已审批", value: "APPROVED" },
        { label: "已签署", value: "SIGNED" },
        { label: "已生效", value: "EFFECTIVE" },
        { label: "已终止", value: "TERMINATED" },
        { label: "已取消", value: "CANCELLED" }
      ],
      riskLevelOptions: [
        { label: "低风险", value: "LOW" },
        { label: "中风险", value: "MEDIUM" },
        { label: "高风险", value: "HIGH" },
        { label: "严重风险", value: "CRITICAL" }
      ],

      // ==================== 担保物管理相关数据 ====================
      // 担保物概览数据
      collateralOverview: {},
      // 担保物列表相关
      collateralLoading: false,
      collateralIds: [],
      collateralSingle: true,
      collateralMultiple: true,
      collateralShowSearch: true,
      collateralTotal: 0,
      collateralList: [],
      collateralTitle: "",
      collateralOpen: false,
      // 担保物查询参数
      collateralQuery: {
        pageNum: 1,
        pageSize: 10,
        collateralNo: null,
        collateralName: null,
        collateralType: null,
        collateralCategory: null,
        ownerId: null,
        ownerType: null,
        collateralStatus: null,
        riskLevel: null,
        insuranceStatus: null,
        registrationStatus: null,
        minValue: null,
        maxValue: null,
        startDate: null,
        endDate: null,
        orgid: 1
      },
      // 担保物表单数据
      collateralForm: {},
      // 担保物表单验证规则
      collateralRules: {
        collateralNo: [
          { required: true, message: "担保物编号不能为空", trigger: "blur" }
        ],
        collateralName: [
          { required: true, message: "担保物名称不能为空", trigger: "blur" }
        ],
        collateralType: [
          { required: true, message: "担保物类型不能为空", trigger: "change" }
        ],
        ownerId: [
          { required: true, message: "所有者不能为空", trigger: "change" }
        ],
        originalValue: [
          { required: true, message: "原始价值不能为空", trigger: "blur" }
        ]
      },
      // 担保物类型选项
      collateralTypeOptions: [
        { label: "不动产", value: "REAL_ESTATE" },
        { label: "动产", value: "MOVABLE" },
        { label: "权利", value: "RIGHTS" },
        { label: "金融资产", value: "FINANCIAL" }
      ],
      // 担保物分类选项
      collateralCategoryOptions: [
        { label: "土地", value: "LAND" },
        { label: "建筑物", value: "BUILDING" },
        { label: "设备", value: "EQUIPMENT" },
        { label: "车辆", value: "VEHICLE" },
        { label: "存货", value: "INVENTORY" },
        { label: "应收账款", value: "RECEIVABLE" },
        { label: "股权", value: "STOCK" },
        { label: "存款", value: "DEPOSIT" },
        { label: "债券", value: "BOND" }
      ],
      // 担保物状态选项
      collateralStatusOptions: [
        { label: "可用", value: "AVAILABLE" },
        { label: "已抵押", value: "MORTGAGED" },
        { label: "已质押", value: "PLEDGED" },
        { label: "冻结", value: "FROZEN" },
        { label: "已处置", value: "DISPOSED" }
      ],

      // ==================== 担保监控相关数据 ====================
      // 监控概览数据
      monitoringOverview: {},
      // 监控加载状态
      monitoringLoading: false,
      // 监控选中ID数组
      monitoringIds: [],
      // 监控非单个禁用
      monitoringSingle: true,
      // 监控非多个禁用
      monitoringMultiple: true,
      // 监控显示搜索条件
      monitoringShowSearch: true,
      // 监控总条数
      monitoringTotal: 0,
      // 监控表格数据
      monitoringList: [],
      // 监控弹出层标题
      monitoringTitle: "",
      // 监控是否显示弹出层
      monitoringOpen: false,
      // 监控查询参数
      monitoringQuery: {
        pageNum: 1,
        pageSize: 10,
        alertNo: null,
        alertType: null,
        alertLevel: null,
        alertStatus: null,
        companyName: null,
        orgid: 1
      },
      // 监控表单参数
      monitoringForm: {},
      // 监控表单校验
      monitoringRules: {},
      // 监控预警类型选项
      monitoringTypeOptions: [
        { label: "到期预警", value: "EXPIRY_WARNING" },
        { label: "使用率预警", value: "USAGE_RATE_WARNING" },
        { label: "价值变动预警", value: "VALUE_CHANGE_WARNING" },
        { label: "风险预警", value: "RISK_WARNING" },
        { label: "合规预警", value: "COMPLIANCE_WARNING" },
        { label: "逾期预警", value: "OVERDUE_WARNING" },
        { label: "担保物预警", value: "COLLATERAL_WARNING" },
        { label: "履约预警", value: "PERFORMANCE_WARNING" }
      ],
      // 监控预警级别选项
      monitoringLevelOptions: [
        { label: "低级", value: "LOW" },
        { label: "中级", value: "MEDIUM" },
        { label: "高级", value: "HIGH" },
        { label: "严重", value: "CRITICAL" }
      ],
      // 监控预警状态选项
      monitoringStatusOptions: [
        { label: "活跃", value: "ACTIVE" },
        { label: "已处理", value: "HANDLED" },
        { label: "已关闭", value: "CLOSED" },
        { label: "重新激活", value: "REACTIVATED" }
      ]
    };
  },
  created() {
    console.log('=== 担保管理组件 created ===');
    this.getApplicationList();
    this.getCollateralOverview();
    this.getMonitoringOverview();
  },
  methods: {
    parseTime,

    /** 格式化金额 */
    formatAmount(amount, currency = 'CNY') {
      if (!amount) return '0.00';
      const currencySymbol = currency === 'USD' ? '$' : currency === 'EUR' ? '€' : '¥';
      return currencySymbol + parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      });
    },

    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'application') {
        this.getApplicationList();
      } else if (tab.name === 'contract') {
        this.getContractList();
      } else if (tab.name === 'collateral') {
        this.getCollateralList();
      } else if (tab.name === 'monitoring') {
        this.getMonitoringList();
      }
    },

    /** 查询担保申请列表 */
    getApplicationList() {
      this.applicationLoading = true;
      getGuaranteeApplicationPage(this.applicationQuery).then(response => {
        try {
          let list = [];
          // 支持多种响应格式
          if (response && response.data && response.data.tlist) {
            // tlist格式（示例云标准格式）
            list = response.data.tlist;
            this.applicationTotal = response.data.totalRecord || 0;
          } else if (response && response.data && response.data.records) {
            // PageResult格式
            list = response.data.records;
            this.applicationTotal = response.data.total || 0;
          } else if (response && response.data && Array.isArray(response.data)) {
            // 数组格式
            list = response.data;
            this.applicationTotal = response.data.length;
          } else if (response && response.rows) {
            // rows格式
            list = response.rows;
            this.applicationTotal = response.total || 0;
          } else if (Array.isArray(response)) {
            // 直接数组格式
            list = response;
            this.applicationTotal = response.length;
          } else {
            // 默认空数据
            list = [];
            this.applicationTotal = 0;
          }
          // 确保 applicationId 作为字符串处理，避免 JavaScript 大数精度丢失
          this.applicationList = list.map(item => ({
            ...item,
            applicationId: item.applicationId ? String(item.applicationId) : item.applicationId
          }));
        } catch (error) {
          console.error('解析担保申请列表数据失败', error);
          this.applicationList = [];
          this.applicationTotal = 0;
          this.$message.error('数据格式错误');
        } finally {
          this.applicationLoading = false;
        }
      }).catch(error => {
        console.error('查询担保申请列表失败', error);
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
      this.applicationTitle = "添加担保申请";
    },

    /** 修改按钮操作 */
    handleApplicationUpdate(row) {
      this.resetApplicationForm();
      // 判断是从行内按钮还是工具栏按钮触发
      // row 可能是行数据对象，也可能是事件对象或 undefined
      let applicationId;
      let rowData = null;

      if (row && row.applicationId) {
        // 从行内"修改"按钮触发，row 是行数据
        applicationId = String(row.applicationId);
        rowData = row;
      } else if (this.applicationIds && this.applicationIds.length > 0) {
        // 从工具栏"修改"按钮触发，使用选中的第一条数据
        applicationId = String(this.applicationIds[0]);
        // 从 applicationList 中找到对应的行数据
        rowData = this.applicationList.find(item => String(item.applicationId) === applicationId);
      } else {
        this.$modal.msgWarning("请先选择要修改的数据");
        return;
      }

      console.log('修改担保申请, applicationId:', applicationId, 'rowData:', rowData);

      getGuaranteeApplication(applicationId).then(response => {
        if (response.code === 1 && response.data) {
          this.applicationForm = { ...response.data };
          // 确保 applicationId 是字符串
          if (this.applicationForm.applicationId) {
            this.applicationForm.applicationId = String(this.applicationForm.applicationId);
          }
          // 字段映射：后端 guarantorName -> 前端 guaranteedParty
          if (response.data.guarantorName && !response.data.guaranteedParty) {
            this.applicationForm.guaranteedParty = response.data.guarantorName;
          }
        } else {
          // 如果查询失败，使用行数据填充表单
          if (rowData) {
            this.applicationForm = { ...rowData };
            // 确保 applicationId 是字符串
            if (this.applicationForm.applicationId) {
              this.applicationForm.applicationId = String(this.applicationForm.applicationId);
            }
          }
          this.$modal.msgWarning(response.msg || "查询详情失败，使用列表数据");
        }
        this.applicationOpen = true;
        this.applicationTitle = "修改担保申请";
      }).catch(error => {
        // 请求失败时使用行数据
        if (rowData) {
          this.applicationForm = { ...rowData };
          // 确保 applicationId 是字符串
          if (this.applicationForm.applicationId) {
            this.applicationForm.applicationId = String(this.applicationForm.applicationId);
          }
        }
        this.applicationOpen = true;
        this.applicationTitle = "修改担保申请";
        console.error("查询担保申请详情失败:", error);
      });
    },

    /** 查看按钮操作 */
    handleApplicationView(row) {
      // 跳转到详情页面
      this.$router.push(`/globalTreasurer/financing/guarantee/detail/${row.applicationId}`);
    },

    /** 提交按钮 */
    submitApplicationForm() {
      this.$refs["applicationForm"].validate(valid => {
        if (valid) {
          if (this.applicationForm.applicationId != null) {
            updateGuaranteeApplication(this.applicationForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.applicationOpen = false;
              this.getApplicationList();
            });
          } else {
            createGuaranteeApplication(this.applicationForm).then(response => {
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
      this.$modal.confirm('是否确认删除担保申请编号为"' + applicationIds + '"的数据项？').then(function() {
        return deleteGuaranteeApplication(applicationIds);
      }).then(() => {
        this.getApplicationList();
        this.$modal.msgSuccess("删除成功");
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
        guaranteeType: null,
        guaranteeAmount: null,
        guaranteedParty: null,
        guaranteeTerm: null,
        guaranteeReason: null,
        remark: null
      };
      this.resetForm("applicationForm");
    },

    // ==================== 担保合同管理方法 ====================

    /** 查询担保合同列表 */
    getContractList() {
      this.contractLoading = true;
      try {
        getGuaranteeContractPage(this.contractQuery).then(response => {
          console.log('担保合同响应数据:', response);
          // API已经处理好格式，直接使用 rows 和 total
          if (response && Array.isArray(response.rows)) {
            this.contractList = response.rows;
            this.contractTotal = response.total || 0;
          } else {
            console.warn('担保合同数据格式异常:', response);
            this.contractList = [];
            this.contractTotal = 0;
          }
          this.contractLoading = false;
        }).catch(error => {
          console.error('查询担保合同失败:', error);
          this.contractList = [];
          this.contractTotal = 0;
          this.contractLoading = false;
          this.$message.error('查询担保合同失败');
        });
      } catch (error) {
        console.error('查询担保合同异常:', error);
        this.contractList = [];
        this.contractTotal = 0;
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
      this.contractIds = selection.map(item => String(item.contractId));
      this.contractSingle = selection.length !== 1;
      this.contractMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleContractAdd() {
      this.resetContractForm();
      this.contractOpen = true;
      this.contractTitle = "添加担保合同";
    },

    /** 修改按钮操作 */
    handleContractUpdate(row) {
      this.resetContractForm();
      // 判断是从行内按钮还是工具栏按钮触发
      let contractId;
      let rowData = null;

      if (row && row.contractId) {
        // 从行内"修改"按钮触发，row 是行数据
        contractId = String(row.contractId);
        rowData = row;
      } else if (this.contractIds && this.contractIds.length > 0) {
        // 从工具栏"修改"按钮触发，使用选中的第一条数据
        contractId = String(this.contractIds[0]);
        // 从 contractList 中找到对应的行数据
        rowData = this.contractList.find(item => String(item.contractId) === contractId);
      } else {
        this.$modal.msgWarning("请先选择要修改的数据");
        return;
      }

      console.log('修改担保合同, contractId:', contractId, 'rowData:', rowData);

      // 字段映射函数：后端字段 -> 前端表单字段
      const mapContractData = (data) => {
        return {
          ...data,
          contractId: data.contractId ? String(data.contractId) : null,
          // 日期字段映射：后端 startDate/endDate -> 前端 contractStartDate/contractEndDate
          contractStartDate: data.startDate || data.contractStartDate,
          contractEndDate: data.endDate || data.contractEndDate,
          // 担保方式映射：后端 guaranteeMode -> 前端 guaranteeMethod
          guaranteeMethod: data.guaranteeMode || data.guaranteeMethod,
          // 担保比例映射
          guaranteeRatio: data.guaranteeRatio,
          // 风险等级映射
          riskLevel: data.riskLevel,
          // 担保范围映射
          guaranteeScope: data.guaranteeScope,
          // 保留原始字段以便提交时使用
          startDate: data.startDate,
          endDate: data.endDate,
          guaranteeMode: data.guaranteeMode
        };
      };

      getGuaranteeContract(contractId).then(response => {
        console.log('获取担保合同详情响应:', response);
        if (response.code === 1 && response.data) {
          this.contractForm = mapContractData(response.data);
        } else {
          // 如果查询失败，使用行数据填充表单
          if (rowData) {
            this.contractForm = mapContractData(rowData);
          }
          this.$modal.msgWarning(response.msg || "查询详情失败，使用列表数据");
        }
        console.log('表单数据:', this.contractForm);
        this.contractOpen = true;
        this.contractTitle = "修改担保合同";
      }).catch(error => {
        // 请求失败时使用行数据
        if (rowData) {
          this.contractForm = mapContractData(rowData);
        }
        this.contractOpen = true;
        this.contractTitle = "修改担保合同";
        console.error("查询担保合同详情失败:", error);
      });
    },

    /** 查看详情 */
    handleContractDetail(row) {
      const contractId = row.contractId ? String(row.contractId) : null;
      if (!contractId) {
        this.$message.error('合同ID不存在');
        return;
      }
      getGuaranteeContract(contractId).then(response => {
        if (response.code === 1 && response.data) {
          this.contractForm = { ...response.data };
        } else {
          this.contractForm = { ...row };
        }
        this.contractOpen = true;
        this.contractTitle = "担保合同详情";
      }).catch(error => {
        this.$message.error('获取担保合同详情失败');
      });
    },

    /** 提交审批 */
    handleContractSubmit(row) {
      const contractId = row.contractId ? String(row.contractId) : null;
      if (!contractId) {
        this.$message.error('合同ID不存在');
        return;
      }
      this.$confirm('确认提交该担保合同进行审批吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        submitGuaranteeContract(contractId, this.$store.state.user.userId).then(() => {
          this.$message.success("提交成功");
          this.getContractList();
        }).catch(error => {
          this.$message.error('提交失败');
        });
      });
    },

    /** 审批操作 */
    handleContractApprove(row) {
      const contractId = row.contractId ? String(row.contractId) : null;
      if (!contractId) {
        this.$message.error('合同ID不存在');
        return;
      }
      this.$prompt('请输入审批意见', '担保合同审批', {
        confirmButtonText: '通过',
        cancelButtonText: '拒绝',
        distinguishCancelAndClose: true,
        inputPlaceholder: '请输入审批意见'
      }).then(({ value }) => {
        approveGuaranteeContract(contractId, 'APPROVED', this.$store.state.user.userId, value).then(() => {
          this.$message.success("审批成功");
          this.getContractList();
        }).catch(error => {
          this.$message.error('审批失败');
        });
      }).catch(action => {
        if (action === 'cancel') {
          this.$prompt('请输入拒绝原因', '担保合同审批', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputPlaceholder: '请输入拒绝原因'
          }).then(({ value }) => {
            approveGuaranteeContract(contractId, 'REJECTED', this.$store.state.user.userId, value).then(() => {
              this.$message.success("审批完成");
              this.getContractList();
            }).catch(error => {
              this.$message.error('审批失败');
            });
          });
        }
      });
    },

    /** 签署合同 */
    handleContractSign(row) {
      const contractId = row.contractId ? String(row.contractId) : null;
      if (!contractId) {
        this.$message.error('合同ID不存在');
        return;
      }
      this.$confirm('确认签署该担保合同吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const today = new Date().toISOString().split('T')[0];
        signGuaranteeContract(contractId, today, this.$store.state.user.userId).then(() => {
          this.$message.success("签署成功");
          this.getContractList();
        }).catch(error => {
          this.$message.error('签署失败');
        });
      });
    },

    /** 生效合同 */
    handleContractActivate(row) {
      const contractId = row.contractId ? String(row.contractId) : null;
      if (!contractId) {
        this.$message.error('合同ID不存在');
        return;
      }
      this.$confirm('确认生效该担保合同吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const today = new Date().toISOString().split('T')[0];
        activateGuaranteeContract(contractId, today, this.$store.state.user.userId).then(() => {
          this.$message.success("生效成功");
          this.getContractList();
        }).catch(error => {
          this.$message.error('生效失败');
        });
      });
    },

    /** 终止合同 */
    handleContractTerminate(row) {
      const contractId = row.contractId ? String(row.contractId) : null;
      if (!contractId) {
        this.$message.error('合同ID不存在');
        return;
      }
      this.$prompt('请输入终止原因', '终止担保合同', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '请输入终止原因'
      }).then(({ value }) => {
        const today = new Date().toISOString().split('T')[0];
        terminateGuaranteeContract(contractId, today, value, this.$store.state.user.userId).then(() => {
          this.$message.success("终止成功");
          this.getContractList();
        }).catch(error => {
          this.$message.error('终止失败');
        });
      });
    },

    /** 取消合同 */
    handleContractCancel(row) {
      const contractId = row.contractId ? String(row.contractId) : null;
      if (!contractId) {
        this.$message.error('合同ID不存在');
        return;
      }
      this.$confirm('确认取消该担保合同吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        cancelGuaranteeContract(contractId, this.$store.state.user.userId).then(() => {
          this.$message.success("取消成功");
          this.getContractList();
        }).catch(error => {
          this.$message.error('取消失败');
        });
      });
    },

    /** 删除按钮操作 */
    handleContractDelete(row) {
      let contractIds;
      if (row && row.contractId) {
        contractIds = String(row.contractId);
      } else if (this.contractIds && this.contractIds.length > 0) {
        contractIds = this.contractIds.join(',');
      } else {
        this.$modal.msgWarning("请先选择要删除的数据");
        return;
      }
      this.$confirm('是否确认删除选中的担保合同数据项？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteGuaranteeContract(contractIds).then(() => {
          this.getContractList();
          this.$message.success("删除成功");
        }).catch(error => {
          this.$message.error('删除失败');
        });
      });
    },

    /** 导出按钮操作 */
    async handleContractExport() {
      try {
        this.$message.info("正在导出数据...");
        const response = await exportGuaranteeContracts(this.contractQuery);

        // 创建下载链接
        const blob = new Blob([response], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = '担保合同列表.xlsx';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);

        this.$message.success("导出成功");
      } catch (error) {
        console.error('导出失败:', error);
        this.$message.error("导出失败，请稍后重试");
      }
    },

    // ==================== 标签类型和标签方法 ====================

    /** 获取合同类型标签类型 */
    getContractTypeTagType(contractType) {
      const typeMap = {
        'GUARANTEE': 'primary',
        'MORTGAGE': 'success',
        'PLEDGE': 'warning',
        'LIEN': 'info'
      };
      return typeMap[contractType] || '';
    },

    /** 获取合同类型标签 */
    getContractTypeLabel(contractType) {
      const typeMap = {
        'GUARANTEE': '保证合同',
        'MORTGAGE': '抵押合同',
        'PLEDGE': '质押合同',
        'LIEN': '留置合同'
      };
      return typeMap[contractType] || contractType;
    },

    /** 获取担保类型标签类型 */
    getGuaranteeTypeTagType(guaranteeType) {
      const typeMap = {
        'GENERAL': 'primary',
        'JOINT': 'warning',
        'MAXIMUM': 'danger'
      };
      return typeMap[guaranteeType] || '';
    },

    /** 获取担保类型标签 */
    getGuaranteeTypeLabel(guaranteeType) {
      const typeMap = {
        'GENERAL': '一般担保',
        'JOINT': '连带担保',
        'MAXIMUM': '最高额担保'
      };
      return typeMap[guaranteeType] || guaranteeType;
    },

    /** 获取担保申请-担保类型标签类型 */
    getAppGuaranteeTypeTagType(guaranteeType) {
      const typeMap = {
        'GUARANTEE': 'primary',
        'MORTGAGE': 'success',
        'PLEDGE': 'warning',
        'CREDIT': 'info'
      };
      return typeMap[guaranteeType] || '';
    },

    /** 获取担保申请-担保类型标签 */
    getAppGuaranteeTypeLabel(guaranteeType) {
      const typeMap = {
        'GUARANTEE': '保证担保',
        'MORTGAGE': '抵押担保',
        'PLEDGE': '质押担保',
        'CREDIT': '信用担保'
      };
      return typeMap[guaranteeType] || guaranteeType;
    },

    /** 获取担保申请-申请状态标签类型 */
    getAppStatusTagType(applicationStatus) {
      const statusMap = {
        'DRAFT': 'info',
        'PENDING_APPROVAL': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      };
      return statusMap[applicationStatus] || '';
    },

    /** 获取担保申请-申请状态标签 */
    getAppStatusLabel(applicationStatus) {
      const statusMap = {
        'DRAFT': '草稿',
        'PENDING_APPROVAL': '待审批',
        'APPROVED': '已审批',
        'REJECTED': '已拒绝'
      };
      return statusMap[applicationStatus] || applicationStatus;
    },

    /** 获取合同状态标签类型 */
    getContractStatusTagType(contractStatus) {
      const statusMap = {
        'DRAFT': 'info',
        'PENDING': 'warning',
        'APPROVED': 'primary',
        'SIGNED': 'success',
        'EFFECTIVE': 'success',
        'TERMINATED': 'danger',
        'CANCELLED': 'danger'
      };
      return statusMap[contractStatus] || '';
    },

    /** 获取合同状态标签 */
    getContractStatusLabel(contractStatus) {
      const statusMap = {
        'DRAFT': '草稿',
        'PENDING': '待审批',
        'APPROVED': '已审批',
        'SIGNED': '已签署',
        'EFFECTIVE': '已生效',
        'TERMINATED': '已终止',
        'CANCELLED': '已取消'
      };
      return statusMap[contractStatus] || contractStatus;
    },

    /** 获取风险等级标签类型 */
    getRiskLevelTagType(riskLevel) {
      const levelMap = {
        'LOW': 'success',
        'MEDIUM': 'primary',
        'HIGH': 'warning',
        'CRITICAL': 'danger'
      };
      return levelMap[riskLevel] || '';
    },

    /** 获取风险等级标签 */
    getRiskLevelLabel(riskLevel) {
      const levelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '严重风险'
      };
      return levelMap[riskLevel] || riskLevel;
    },

    /** 表单重置 */
    resetContractForm() {
      this.contractForm = {
        id: null,
        contractId: null,
        contractNo: null,
        contractType: null,
        guaranteeType: null,
        guaranteeMethod: null,
        guaranteeAmount: null,
        currency: 'CNY',
        guaranteeRatio: null,
        contractStartDate: null,
        contractEndDate: null,
        companyId: null,
        companyName: null,
        guarantorCompany: null,
        beneficiaryCompany: null,
        guarantorId: null,
        guaranteedPartyId: null,
        creditorId: null,
        mainContractNo: null,
        mainContractAmount: null,
        guaranteeScope: null,
        guaranteeConditions: null,
        performanceConditions: null,
        breachLiability: null,
        disputeResolution: null,
        counterGuaranteeRequired: '0',
        counterGuaranteeType: null,
        counterGuaranteeAmount: null,
        marginRequired: '0',
        marginRatio: null,
        marginAmount: null,
        marginAccount: null,
        riskLevel: null,
        riskAssessment: null,
        orgid: 1
      };
      this.resetForm("contractForm");
    },

    /** 提交担保合同表单 */
    submitContractForm() {
      this.$refs["contractForm"].validate(valid => {
        if (valid) {
          // 准备提交数据，进行字段映射：前端表单字段 -> 后端字段
          const submitData = {
            ...this.contractForm,
            // 日期字段映射：前端 contractStartDate/contractEndDate -> 后端 startDate/endDate
            startDate: this.contractForm.contractStartDate || this.contractForm.startDate,
            endDate: this.contractForm.contractEndDate || this.contractForm.endDate,
            // 担保方式映射：前端 guaranteeMethod -> 后端 guaranteeMode
            guaranteeMode: this.contractForm.guaranteeMethod || this.contractForm.guaranteeMode,
            // 担保比例、风险等级、担保范围直接使用
            guaranteeRatio: this.contractForm.guaranteeRatio,
            riskLevel: this.contractForm.riskLevel,
            guaranteeScope: this.contractForm.guaranteeScope
          };

          console.log('提交担保合同数据:', submitData);

          // 判断是新增还是修改：使用 contractId 而不是 id
          if (this.contractForm.contractId != null) {
            updateGuaranteeContract(submitData).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.contractOpen = false;
              this.getContractList();
            }).catch(error => {
              this.$modal.msgError("修改失败");
            });
          } else {
            createGuaranteeContract(submitData).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.contractOpen = false;
              this.getContractList();
            }).catch(error => {
              this.$modal.msgError("新增失败");
            });
          }
        }
      });
    },

    /** 取消担保合同按钮 */
    cancelContract() {
      this.contractOpen = false;
      this.resetContractForm();
    },

    // ==================== 担保物管理相关方法 ====================

    /** 获取担保物概览数据 */
    async getCollateralOverview() {
      try {
        const response = await fetchCollateralOverview();
        console.log('担保物概览数据原始响应:', response);
        this.collateralOverview = response || {};
        console.log('担保物概览数据:', this.collateralOverview);
      } catch (error) {
        console.error('获取担保物概览数据失败:', error);
        this.collateralOverview = {};
      }
    },

    /** 查询担保物列表 */
    async getCollateralList() {
      this.collateralLoading = true;
      try {
        const response = await getCollateralPage(this.collateralQuery);
        this.collateralList = response.records || [];
        this.collateralTotal = response.total || 0;
      } catch (error) {
        console.error('查询担保物列表失败:', error);
        this.collateralList = [];
        this.collateralTotal = 0;
        this.$modal.msgError("查询担保物列表失败");
      } finally {
        this.collateralLoading = false;
      }
    },

    /** 担保物搜索按钮操作 */
    handleCollateralQuery() {
      this.collateralQuery.pageNum = 1;
      this.getCollateralList();
    },

    /** 重置担保物查询 */
    resetCollateralQuery() {
      this.resetForm("collateralQueryForm");
      this.handleCollateralQuery();
    },

    /** 多选框选中数据 */
    handleCollateralSelectionChange(selection) {
      this.collateralIds = selection.map(item => item.collateralId);
      this.collateralSingle = selection.length !== 1;
      this.collateralMultiple = !selection.length;
    },

    /** 新增担保物按钮操作 */
    handleCollateralAdd() {
      this.resetCollateralForm();
      this.collateralOpen = true;
      this.collateralTitle = "添加担保物";
    },

    /** 修改担保物按钮操作 */
    async handleCollateralUpdate(row) {
      this.resetCollateralForm();
      const collateralId = row.collateralId || this.collateralIds[0];
      try {
        const response = await getCollateral(collateralId);
        // 字段映射：后端字段 -> 前端表单字段
        this.collateralForm = {
          ...this.collateralForm,
          // ID 映射
          id: response.collateralId,
          collateralId: response.collateralId,
          // 基本信息
          collateralNo: response.collateralNo,
          collateralName: response.collateralName,
          collateralType: response.collateralType,
          collateralCategory: response.collateralCategory,
          // 价值信息映射
          originalValue: response.collateralValue || response.originalValue,
          currentValue: response.collateralValue || response.evaluationValue || response.currentValue,
          // 币种映射
          currency: response.currencyCode || response.currency || 'CNY',
          // 评估信息映射
          valuationDate: response.evaluationDate || response.valuationDate,
          valuationAgency: response.evaluationAgency || response.valuationAgency,
          valuationMethod: response.valuationMethod,
          valuationReportNo: response.valuationReportNo,
          // 所有者信息
          ownerId: response.ownerId,
          ownerName: response.ownerName,
          ownerType: response.ownerType || 'COMPANY',
          // 位置和描述
          location: response.location,
          description: response.remark || response.description,
          // 状态信息
          collateralStatus: response.collateralStatus || 'ACTIVE',
          // 公司信息
          companyId: response.companyId,
          companyName: response.companyName,
          // 合同关联
          contractId: response.contractId,
          // 其他字段
          deleteFlag: response.deleteFlag,
          createdBy: response.createdBy,
          createdTime: response.createdTime,
          updatedBy: response.updatedBy,
          updatedTime: response.updatedTime,
          remark: response.remark
        };
        console.log('担保物修改数据映射:', this.collateralForm);
        this.collateralOpen = true;
        this.collateralTitle = "修改担保物";
      } catch (error) {
        console.error('获取担保物详情失败:', error);
        this.$modal.msgError("获取担保物详情失败");
      }
    },

    /** 查看担保物详情 */
    async handleCollateralDetail(row) {
      try {
        const response = await getCollateral(row.collateralId);
        this.$modal.msgSuccess("查看担保物详情功能待完善");
      } catch (error) {
        console.error('获取担保物详情失败:', error);
        this.$modal.msgError("获取担保物详情失败");
      }
    },

    /** 删除担保物按钮操作 */
    async handleCollateralDelete(row) {
      const collateralIds = row.collateralId ? [row.collateralId] : this.collateralIds;
      this.$modal.confirm('是否确认删除担保物编号为"' + (row.collateralNo || collateralIds) + '"的数据项？').then(async () => {
        try {
          if (collateralIds.length === 1) {
            await deleteCollateral(collateralIds[0]);
          } else {
            await batchDeleteCollaterals(collateralIds);
          }
          this.getCollateralList();
          this.$modal.msgSuccess("删除成功");
        } catch (error) {
          console.error('删除担保物失败:', error);
          this.$modal.msgError("删除失败");
        }
      });
    },

    /** 导出担保物按钮操作 */
    handleCollateralExport() {
      this.$modal.msgSuccess("导出功能待完善");
    },

    /** 担保物操作命令处理 */
    handleCollateralCommand(command, row) {
      switch (command) {
        case 'detail':
          this.handleCollateralDetail(row);
          break;
        case 'edit':
          this.handleCollateralUpdate(row);
          break;
        case 'freeze':
          this.handleCollateralFreeze(row);
          break;
        case 'unfreeze':
          this.handleCollateralUnfreeze(row);
          break;
        case 'mortgage':
          this.handleCollateralMortgage(row);
          break;
        case 'pledge':
          this.handleCollateralPledge(row);
          break;
        case 'release':
          this.handleCollateralRelease(row);
          break;
        case 'dispose':
          this.handleCollateralDispose(row);
          break;
      }
    },

    /** 冻结担保物 */
    async handleCollateralFreeze(row) {
      this.$modal.confirm('是否确认冻结担保物"' + row.collateralName + '"？').then(async () => {
        try {
          await freezeCollateral(row.collateralId, '手动冻结', 1);
          this.getCollateralList();
          this.$modal.msgSuccess("冻结成功");
        } catch (error) {
          console.error('冻结担保物失败:', error);
          this.$modal.msgError("冻结失败");
        }
      });
    },

    /** 解冻担保物 */
    async handleCollateralUnfreeze(row) {
      this.$modal.confirm('是否确认解冻担保物"' + row.collateralName + '"？').then(async () => {
        try {
          await unfreezeCollateral(row.collateralId, '手动解冻', 1);
          this.getCollateralList();
          this.$modal.msgSuccess("解冻成功");
        } catch (error) {
          console.error('解冻担保物失败:', error);
          this.$modal.msgError("解冻失败");
        }
      });
    },

    /** 抵押担保物 */
    async handleCollateralMortgage(row) {
      this.$modal.msgSuccess("抵押功能待完善");
    },

    /** 质押担保物 */
    async handleCollateralPledge(row) {
      this.$modal.msgSuccess("质押功能待完善");
    },

    /** 释放担保物 */
    async handleCollateralRelease(row) {
      this.$modal.confirm('是否确认释放担保物"' + row.collateralName + '"？').then(async () => {
        try {
          await releaseCollateral(row.collateralId, '手动释放', 1);
          this.getCollateralList();
          this.$modal.msgSuccess("释放成功");
        } catch (error) {
          console.error('释放担保物失败:', error);
          this.$modal.msgError("释放失败");
        }
      });
    },

    /** 处置担保物 */
    async handleCollateralDispose(row) {
      this.$modal.msgSuccess("处置功能待完善");
    },

    /** 获取担保物类型标签类型 */
    getCollateralTypeTagType(collateralType) {
      const typeMap = {
        'REAL_ESTATE': 'success',
        'MOVABLE': 'info',
        'RIGHTS': 'warning',
        'FINANCIAL': 'danger'
      };
      return typeMap[collateralType] || '';
    },

    /** 获取担保物类型标签 */
    getCollateralTypeLabel(collateralType) {
      const typeMap = {
        'REAL_ESTATE': '不动产',
        'MOVABLE': '动产',
        'RIGHTS': '权利',
        'FINANCIAL': '金融资产'
      };
      return typeMap[collateralType] || collateralType;
    },

    /** 获取担保物状态标签类型 */
    getCollateralStatusTagType(collateralStatus) {
      const statusMap = {
        'AVAILABLE': 'success',
        'ACTIVE': 'success',
        'MORTGAGED': 'warning',
        'PLEDGED': 'warning',
        'FROZEN': 'danger',
        'DISPOSED': 'info'
      };
      return statusMap[collateralStatus] || '';
    },

    /** 获取担保物状态标签 */
    getCollateralStatusLabel(collateralStatus) {
      const statusMap = {
        'AVAILABLE': '可用',
        'ACTIVE': '正常',
        'MORTGAGED': '已抵押',
        'PLEDGED': '已质押',
        'FROZEN': '冻结',
        'DISPOSED': '已处置'
      };
      return statusMap[collateralStatus] || collateralStatus;
    },

    /** 根据担保物状态获取风险等级标签类型 */
    getCollateralRiskTagType(collateralStatus) {
      const riskMap = {
        'AVAILABLE': 'success',
        'ACTIVE': 'success',
        'MORTGAGED': 'warning',
        'PLEDGED': 'warning',
        'FROZEN': 'danger',
        'DISPOSED': 'danger'
      };
      return riskMap[collateralStatus] || 'info';
    },

    /** 根据担保物状态获取风险等级标签 */
    getCollateralRiskLabel(collateralStatus) {
      const riskMap = {
        'AVAILABLE': '低风险',
        'ACTIVE': '低风险',
        'MORTGAGED': '中风险',
        'PLEDGED': '中风险',
        'FROZEN': '高风险',
        'DISPOSED': '高风险'
      };
      return riskMap[collateralStatus] || '未知';
    },

    /** 获取使用率颜色 */
    getUsageRatioColor(ratio) {
      if (ratio >= 90) return '#F56C6C';
      if (ratio >= 70) return '#E6A23C';
      if (ratio >= 50) return '#409EFF';
      return '#67C23A';
    },

    /** 重置担保物表单 */
    resetCollateralForm() {
      this.collateralForm = {
        id: null,
        collateralNo: null,
        collateralName: null,
        collateralType: null,
        collateralCategory: null,
        ownerId: null,
        ownerType: 'COMPANY',
        location: null,
        description: null,
        originalValue: null,
        currentValue: null,
        currency: 'CNY',
        valuationDate: null,
        valuationMethod: null,
        valuationAgency: null,
        valuationReportNo: null,
        purchaseDate: null,
        purchasePrice: null,
        depreciationMethod: null,
        depreciationRate: null,
        residualValue: null,
        usefulLife: null,
        collateralStatus: 'ACTIVE',
        mortgageStatus: 'UNMORTGAGED',
        pledgeStatus: 'UNPLEDGED',
        insuranceStatus: 'UNINSURED',
        insuranceCompany: null,
        insurancePolicyNo: null,
        insuranceAmount: null,
        insuranceStartDate: null,
        insuranceEndDate: null,
        certificateType: null,
        certificateNo: null,
        certificateIssueDate: null,
        certificateExpireDate: null,
        certificateAgency: null,
        registrationStatus: 'UNREGISTERED',
        registrationNo: null,
        registrationDate: null,
        registrationAgency: null,
        riskLevel: 'LOW',
        riskAssessment: null,
        maintenanceCost: null,
        storageCost: null,
        managementCost: null,
        remarks: null,
        orgid: 1
      };
      this.resetForm("collateralForm");
    },

    /** 提交担保物表单 */
    submitCollateralForm() {
      this.$refs["collateralForm"].validate(valid => {
        if (valid) {
          // 字段映射：前端表单字段 -> 后端字段
          const submitData = {
            ...this.collateralForm,
            // ID 映射
            collateralId: this.collateralForm.collateralId || this.collateralForm.id,
            // 价值映射
            collateralValue: this.collateralForm.currentValue || this.collateralForm.originalValue,
            // 币种映射
            currencyCode: this.collateralForm.currency || 'CNY',
            // 评估信息映射
            evaluationDate: this.collateralForm.valuationDate,
            evaluationAgency: this.collateralForm.valuationAgency,
            evaluationValue: this.collateralForm.currentValue,
            // 备注映射
            remark: this.collateralForm.description || this.collateralForm.remark
          };

          console.log('提交担保物数据:', submitData);

          // 判断是修改还是新增
          if (this.collateralForm.collateralId != null || this.collateralForm.id != null) {
            updateCollateral(submitData).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.collateralOpen = false;
              this.getCollateralList();
            }).catch(error => {
              console.error('修改担保物失败:', error);
              this.$modal.msgError("修改失败");
            });
          } else {
            createCollateral(submitData).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.collateralOpen = false;
              this.getCollateralList();
            }).catch(error => {
              console.error('新增担保物失败:', error);
              this.$modal.msgError("新增失败");
            });
          }
        }
      });
    },

    /** 取消担保物按钮 */
    cancelCollateral() {
      this.collateralOpen = false;
      this.resetCollateralForm();
    },

    // ==================== 担保监控相关方法 ====================

    /** 获取担保监控概览数据 */
    async getMonitoringOverview() {
      try {
        const response = await getGuaranteeMonitoringStatistics();
        this.monitoringOverview = response || {};
      } catch (error) {
        console.error('获取担保监控概览数据失败:', error);
        this.monitoringOverview = {};
      }
    },

    /** 查询担保监控预警列表 */
    async getMonitoringList() {
      this.monitoringLoading = true;
      try {
        const response = await getGuaranteeMonitoringPage(this.monitoringQuery);
        this.monitoringList = response.records || [];
        this.monitoringTotal = response.total || 0;
      } catch (error) {
        console.error('查询担保监控预警列表失败:', error);
        this.monitoringList = [];
        this.monitoringTotal = 0;
        this.$modal.msgError("查询担保监控预警列表失败");
      } finally {
        this.monitoringLoading = false;
      }
    },

    /** 担保监控搜索按钮操作 */
    handleMonitoringQuery() {
      this.monitoringQuery.pageNum = 1;
      this.getMonitoringList();
    },

    /** 重置担保监控查询 */
    resetMonitoringQuery() {
      this.resetForm("monitoringQueryForm");
      this.handleMonitoringQuery();
    },

    /** 多选框选中数据 */
    handleMonitoringSelectionChange(selection) {
      this.monitoringIds = selection.map(item => item.alertId);
      this.monitoringSingle = selection.length !== 1;
      this.monitoringMultiple = !selection.length;
    },

    /** 新增担保监控预警按钮操作 */
    handleMonitoringAdd() {
      this.resetMonitoringForm();
      this.monitoringOpen = true;
      this.monitoringTitle = "添加担保监控预警";
    },

    /** 修改担保监控预警按钮操作 */
    async handleMonitoringUpdate(row) {
      this.resetMonitoringForm();
      const monitoringId = row.alertId || this.monitoringIds[0];
      try {
        const response = await getGuaranteeMonitoring(monitoringId);
        this.monitoringForm = {
          ...response,
          id: response.alertId,
          alertId: response.alertId
        };
        this.monitoringOpen = true;
        this.monitoringTitle = "修改担保监控预警";
      } catch (error) {
        console.error('获取担保监控预警详情失败:', error);
        this.$modal.msgError("获取担保监控预警详情失败");
      }
    },

    /** 查看担保监控预警详情 */
    async handleMonitoringDetail(row) {
      try {
        const response = await getGuaranteeMonitoring(row.alertId);
        this.$modal.msgSuccess("查看担保监控预警详情功能待完善");
      } catch (error) {
        console.error('获取担保监控预警详情失败:', error);
        this.$modal.msgError("获取担保监控预警详情失败");
      }
    },

    /** 删除担保监控预警按钮操作 */
    async handleMonitoringDelete(row) {
      const monitoringIds = row.alertId ? [row.alertId] : this.monitoringIds;
      this.$modal.confirm('是否确认删除担保监控预警编号为"' + (row.alertNo || monitoringIds) + '"的数据项？').then(async () => {
        try {
          if (monitoringIds.length === 1) {
            await deleteGuaranteeMonitoring(monitoringIds[0]);
          } else {
            await batchDeleteGuaranteeMonitorings(monitoringIds);
          }
          this.getMonitoringList();
          this.$modal.msgSuccess("删除成功");
        } catch (error) {
          console.error('删除担保监控预警失败:', error);
          this.$modal.msgError("删除失败");
        }
      });
    },

    /** 导出担保监控预警按钮操作 */
    handleMonitoringExport() {
      this.$modal.msgSuccess("导出功能待完善");
    },

    /** 担保监控操作命令处理 */
    handleMonitoringCommand(command, row) {
      switch (command) {
        case 'detail':
          this.handleMonitoringDetail(row);
          break;
        case 'edit':
          this.handleMonitoringUpdate(row);
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
      }
    },

    /** 处理担保监控预警 */
    async handleMonitoringHandle(row) {
      this.$modal.confirm('是否确认处理担保监控预警"' + row.alertTitle + '"？').then(async () => {
        try {
          await handleGuaranteeMonitoring(row.alertId, 1, '手动处理');
          this.getMonitoringList();
          this.$modal.msgSuccess("处理成功");
        } catch (error) {
          console.error('处理担保监控预警失败:', error);
          this.$modal.msgError("处理失败");
        }
      });
    },

    /** 关闭担保监控预警 */
    async handleMonitoringClose(row) {
      this.$modal.confirm('是否确认关闭担保监控预警"' + row.alertTitle + '"？').then(async () => {
        try {
          await closeGuaranteeMonitoring(row.alertId, '手动关闭', 1);
          this.getMonitoringList();
          this.$modal.msgSuccess("关闭成功");
        } catch (error) {
          console.error('关闭担保监控预警失败:', error);
          this.$modal.msgError("关闭失败");
        }
      });
    },

    /** 重新激活担保监控预警 */
    async handleMonitoringReactivate(row) {
      this.$modal.confirm('是否确认重新激活担保监控预警"' + row.alertTitle + '"？').then(async () => {
        try {
          await reactivateGuaranteeMonitoring(row.alertId, '手动重新激活', 1);
          this.getMonitoringList();
          this.$modal.msgSuccess("重新激活成功");
        } catch (error) {
          console.error('重新激活担保监控预警失败:', error);
          this.$modal.msgError("重新激活失败");
        }
      });
    },

    /** 获取担保监控预警类型标签类型 */
    getMonitoringTypeTagType(alertType) {
      const typeMap = {
        'EXPIRY_WARNING': 'warning',
        'USAGE_RATE_WARNING': 'danger',
        'VALUE_CHANGE_WARNING': 'info',
        'RISK_WARNING': 'danger',
        'COMPLIANCE_WARNING': 'warning',
        'OVERDUE_WARNING': 'danger',
        'COLLATERAL_WARNING': 'warning',
        'PERFORMANCE_WARNING': 'info'
      };
      return typeMap[alertType] || '';
    },

    /** 获取担保监控预警类型标签 */
    getMonitoringTypeLabel(alertType) {
      const typeMap = {
        'EXPIRY_WARNING': '到期预警',
        'USAGE_RATE_WARNING': '使用率预警',
        'VALUE_CHANGE_WARNING': '价值变动预警',
        'RISK_WARNING': '风险预警',
        'COMPLIANCE_WARNING': '合规预警',
        'OVERDUE_WARNING': '逾期预警',
        'COLLATERAL_WARNING': '担保物预警',
        'PERFORMANCE_WARNING': '履约预警'
      };
      return typeMap[alertType] || alertType;
    },

    /** 获取担保监控预警级别标签类型 */
    getMonitoringLevelTagType(alertLevel) {
      const levelMap = {
        'LOW': 'info',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      };
      return levelMap[alertLevel] || '';
    },

    /** 获取担保监控预警级别标签 */
    getMonitoringLevelLabel(alertLevel) {
      const levelMap = {
        'LOW': '低级',
        'MEDIUM': '中级',
        'HIGH': '高级',
        'CRITICAL': '严重'
      };
      return levelMap[alertLevel] || alertLevel;
    },

    /** 获取担保监控预警状态标签类型 */
    getMonitoringStatusTagType(alertStatus) {
      const statusMap = {
        'ACTIVE': 'danger',
        'HANDLED': 'warning',
        'CLOSED': 'info',
        'REACTIVATED': 'success'
      };
      return statusMap[alertStatus] || '';
    },

    /** 获取担保监控预警状态标签 */
    getMonitoringStatusLabel(alertStatus) {
      const statusMap = {
        'ACTIVE': '活跃',
        'HANDLED': '已处理',
        'CLOSED': '已关闭',
        'REACTIVATED': '重新激活'
      };
      return statusMap[alertStatus] || alertStatus;
    },

    /** 重置担保监控表单 */
    resetMonitoringForm() {
      this.monitoringForm = {
        id: null,
        alertId: null,
        alertNo: null,
        contractId: null,
        collateralId: null,
        companyId: null,
        companyName: null,
        relatedGuaranteeId: null,
        alertType: null,
        alertLevel: null,
        alertTitle: null,
        alertMessage: null,
        alertDate: null,
        triggerCondition: null,
        suggestedAction: null,
        alertStatus: 'PENDING',
        priorityLevel: 3,
        expiryDate: null,
        autoCloseFlag: '0',
        escalationLevel: 1,
        riskScore: null,
        impactAssessment: null,
        mitigationPlan: null,
        followUpDate: null,
        orgid: 1
      };
      this.resetForm("monitoringForm");
    },

    /** 提交担保监控表单 */
    submitMonitoringForm() {
      this.$refs["monitoringForm"].validate(valid => {
        if (valid) {
          if (this.monitoringForm.id != null) {
            updateGuaranteeMonitoring(this.monitoringForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.monitoringOpen = false;
              this.getMonitoringList();
            }).catch(error => {
              this.$modal.msgError("修改失败");
            });
          } else {
            createGuaranteeMonitoring(this.monitoringForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.monitoringOpen = false;
              this.getMonitoringList();
            }).catch(error => {
              this.$modal.msgError("新增失败");
            });
          }
        }
      });
    },

    /** 取消担保监控按钮 */
    cancelMonitoring() {
      this.monitoringOpen = false;
      this.resetMonitoringForm();
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

/* 担保物概览卡片样式 */
.overview-cards {
  margin-bottom: 20px;
}

.overview-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border: 1px solid #ebeef5;
  transition: all 0.3s;
}

.overview-card:hover {
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  font-size: 14px;
  color: #606266;
}

.card-header i {
  font-size: 24px;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

/* 担保物容器样式 */
.collateral-container {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .overview-card {
    margin-bottom: 15px;
  }

  .card-value {
    font-size: 24px;
  }
}
</style>
