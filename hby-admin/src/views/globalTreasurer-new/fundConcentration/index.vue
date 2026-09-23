<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 资金池管理 -->
      <el-tab-pane label="资金池管理" name="fundPool">
        <div class="fund-pool-container">
          <!-- 查询条件 -->
          <el-form :model="fundPoolQuery" ref="fundPoolQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="资金池名称" prop="poolName">
              <el-input
                v-model="fundPoolQuery.poolName"
                placeholder="请输入资金池名称"
                clearable
                @keyup.enter.native="handleFundPoolQuery"
              />
            </el-form-item>
            <el-form-item label="池状态" prop="status">
              <el-select v-model="fundPoolQuery.status" placeholder="请选择池状态" clearable>
                <el-option label="启用" value="ACTIVE" />
                <el-option label="停用" value="INACTIVE" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleFundPoolQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetFundPoolQuery">重置</el-button>
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
                @click="handleFundPoolAdd"
              >新增资金池</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="fundPoolSingle"
                @click="handleFundPoolUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="fundPoolMultiple"
                @click="handleFundPoolDelete"
              >删除</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="fundPoolLoading" :data="fundPoolList" @selection-change="handleFundPoolSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="资金池编号" align="center" prop="poolId" />
            <el-table-column label="资金池名称" align="center" prop="poolName" />
            <el-table-column label="所属公司" align="center" prop="companyName">
              <template slot-scope="scope">
                <span>{{ scope.row.companyName || '-' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="池类型" align="center" prop="poolType">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.poolType === 'GENERAL'" type="primary">综合资金池</el-tag>
                <el-tag v-else-if="scope.row.poolType === 'OVERSEAS'" type="warning">境外资金池</el-tag>
                <el-tag v-else-if="scope.row.poolType === 'INVESTMENT'" type="success">投资资金池</el-tag>
                <el-tag v-else-if="scope.row.poolType === 'BACKUP'" type="info">备用资金池</el-tag>
                <el-tag v-else-if="scope.row.poolType === 'VIRTUAL'" type="">虚拟资金池</el-tag>
                <span v-else>{{ scope.row.poolType || '-' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="总额度" align="center" prop="totalLimit">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.totalLimit) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="可用金额" align="center" prop="availableAmount">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.availableAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="池状态" align="center" prop="status">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.status === 'ACTIVE'" type="success">启用</el-tag>
                <el-tag v-else-if="scope.row.status === 'INACTIVE'" type="danger">停用</el-tag>
                <span v-else>{{ scope.row.status || '-' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleFundPoolView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleFundPoolUpdate(scope.row)"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleFundPoolDelete(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            background
            class="pagination"
            :current-page="fundPoolQuery.pageNum"
            :layout="layout"
            :page-size="fundPoolQuery.pageSize"
            :total="fundPoolTotal"
            @current-change="handleFundPoolCurrentChange"
            @size-change="handleFundPoolSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- 资金归集 -->
      <el-tab-pane label="资金归集" name="fundConcentration">
        <div class="fund-concentration-container">
          <!-- 统计卡片 -->
          <div class="statistics-cards">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-card class="statistics-card">
                  <div class="card-content">
                    <div class="card-icon total">
                      <i class="el-icon-s-data"></i>
                    </div>
                    <div class="card-info">
                      <div class="card-title">总规则数</div>
                      <div class="card-value">{{ concentrationStatistics.totalRules || 0 }}</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="statistics-card">
                  <div class="card-content">
                    <div class="card-icon active">
                      <i class="el-icon-check"></i>
                    </div>
                    <div class="card-info">
                      <div class="card-title">启用规则</div>
                      <div class="card-value">{{ concentrationStatistics.activeRules || 0 }}</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="statistics-card">
                  <div class="card-content">
                    <div class="card-icon today">
                      <i class="el-icon-time"></i>
                    </div>
                    <div class="card-info">
                      <div class="card-title">今日执行</div>
                      <div class="card-value">{{ concentrationStatistics.todayExecutions || 0 }}</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="statistics-card">
                  <div class="card-content">
                    <div class="card-icon success">
                      <i class="el-icon-success"></i>
                    </div>
                    <div class="card-info">
                      <div class="card-title">成功率</div>
                      <div class="card-value">{{ concentrationStatistics.successRate || 0 }}%</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <!-- 查询表单 -->
          <div class="query-form">
            <el-form :model="concentrationQuery" :inline="true" size="small">
              <el-form-item label="策略名称">
                <el-input
                  v-model="concentrationQuery.strategyName"
                  placeholder="请输入策略名称"
                  clearable
                  style="width: 200px"
                />
              </el-form-item>
              <el-form-item label="策略类型">
                <el-select
                  v-model="concentrationQuery.strategyType"
                  placeholder="请选择策略类型"
                  clearable
                  style="width: 150px"
                >
                  <el-option label="上划归集" value="SWEEP_UP" />
                  <el-option label="下拨归集" value="SWEEP_DOWN" />
                  <el-option label="双向归集" value="BIDIRECTIONAL" />
                </el-select>
              </el-form-item>
              <el-form-item label="状态">
                <el-select
                  v-model="concentrationQuery.status"
                  placeholder="请选择状态"
                  clearable
                  style="width: 120px"
                >
                  <el-option label="启用" value="ACTIVE" />
                  <el-option label="停用" value="INACTIVE" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getConcentrationRulesList">查询</el-button>
                <el-button @click="resetConcentrationQuery">重置</el-button>
                <el-button type="success" @click="showCreateConcentrationRuleDialog">新增规则</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 数据表格 -->
          <div class="table-container">
            <el-table
              v-loading="concentrationLoading"
              :data="concentrationRulesList"
              border
              stripe
              style="width: 100%"
            >
              <el-table-column prop="strategyId" label="策略ID" width="100" />
              <el-table-column prop="strategyCode" label="策略编号" width="180" />
              <el-table-column prop="strategyName" label="策略名称" min-width="150" />
              <el-table-column prop="strategyType" label="策略类型" width="120">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.strategyType === 'DAILY'" type="primary">日归集</el-tag>
                  <el-tag v-else-if="scope.row.strategyType === 'WEEKLY'" type="success">周归集</el-tag>
                  <el-tag v-else-if="scope.row.strategyType === 'MONTHLY'" type="warning">月归集</el-tag>
                  <el-tag v-else-if="scope.row.strategyType === 'REALTIME'" type="danger">实时归集</el-tag>
                  <el-tag v-else-if="scope.row.strategyType === 'CONDITION'" type="info">条件归集</el-tag>
                  <span v-else>{{ scope.row.strategyType || '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="80">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.status === 'ACTIVE'" type="success">启用</el-tag>
                  <el-tag v-else type="danger">停用</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="160">
                <template slot-scope="scope">
                  {{ formatDateTime(scope.row.createTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="280" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="viewConcentrationRule(scope.row)">查看</el-button>
                  <el-button size="mini" type="primary" @click="editConcentrationRule(scope.row)">编辑</el-button>
                  <el-button
                    v-if="scope.row.status === 'ACTIVE'"
                    size="mini"
                    type="warning"
                    @click="disableConcentrationRule(scope.row)"
                  >
                    禁用
                  </el-button>
                  <el-button
                    v-else
                    size="mini"
                    type="success"
                    @click="enableConcentrationRule(scope.row)"
                  >
                    启用
                  </el-button>
                  <el-button size="mini" type="info" @click="executeConcentrationRule(scope.row)">执行</el-button>
                  <el-button size="mini" type="danger" @click="deleteConcentrationRule(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页 -->
          <div class="pagination-container">
            <el-pagination
              :current-page="concentrationQuery.pageNo"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="concentrationQuery.pageSize"
              :total="concentrationTotal"
              layout="total, sizes, prev, pager, next, jumper"
              @current-change="handleConcentrationCurrentChange"
              @size-change="handleConcentrationSizeChange"
            />
          </div>
        </div>
      </el-tab-pane>

      <!-- 资金下拨 -->
      <el-tab-pane label="资金下拨" name="fundAllocation">
        <div class="fund-allocation-container">
          <!-- 统计卡片 -->
          <div class="statistics-section">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-card class="statistics-card">
                  <div class="card-content">
                    <div class="card-icon primary">
                      <i class="el-icon-s-data"></i>
                    </div>
                    <div class="card-info">
                      <div class="card-title">总申请数</div>
                      <div class="card-value">{{ allocationStatistics.totalApplications || 0 }}</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="statistics-card">
                  <div class="card-content">
                    <div class="card-icon warning">
                      <i class="el-icon-time"></i>
                    </div>
                    <div class="card-info">
                      <div class="card-title">待审批</div>
                      <div class="card-value">{{ allocationStatistics.pendingApproval || 0 }}</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="statistics-card">
                  <div class="card-content">
                    <div class="card-icon info">
                      <i class="el-icon-finished"></i>
                    </div>
                    <div class="card-info">
                      <div class="card-title">今日执行</div>
                      <div class="card-value">{{ allocationStatistics.todayExecutions || 0 }}</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="statistics-card">
                  <div class="card-content">
                    <div class="card-icon success">
                      <i class="el-icon-success"></i>
                    </div>
                    <div class="card-info">
                      <div class="card-title">成功率</div>
                      <div class="card-value">{{ allocationStatistics.successRate || 0 }}%</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <!-- 查询表单 -->
          <div class="query-form">
            <el-form :model="allocationQuery" :inline="true" size="small">
              <el-form-item label="执行编号">
                <el-input
                  v-model="allocationQuery.executionNo"
                  placeholder="请输入执行编号"
                  clearable
                  style="width: 200px"
                />
              </el-form-item>
              <el-form-item label="规则名称">
                <el-input
                  v-model="allocationQuery.ruleName"
                  placeholder="请输入规则名称"
                  clearable
                  style="width: 200px"
                />
              </el-form-item>
              <el-form-item label="执行类型">
                <el-select
                  v-model="allocationQuery.executionType"
                  placeholder="请选择执行类型"
                  clearable
                  style="width: 150px"
                >
                  <el-option label="定时执行" value="SCHEDULED" />
                  <el-option label="手动执行" value="MANUAL" />
                  <el-option label="触发执行" value="TRIGGERED" />
                  <el-option label="申请执行" value="REQUEST" />
                </el-select>
              </el-form-item>
              <el-form-item label="执行状态">
                <el-select
                  v-model="allocationQuery.executionStatus"
                  placeholder="请选择执行状态"
                  clearable
                  style="width: 150px"
                >
                  <el-option label="处理中" value="PROCESSING" />
                  <el-option label="已完成" value="COMPLETED" />
                  <el-option label="部分成功" value="PARTIAL_SUCCESS" />
                  <el-option label="失败" value="FAILED" />
                  <el-option label="已取消" value="CANCELLED" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getAllocationsList">查询</el-button>
                <el-button @click="resetAllocationQuery">重置</el-button>
                <el-button type="success" @click="showCreateAllocationDialog">新增申请</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 数据表格 -->
          <div class="table-container">
            <el-table
              v-loading="allocationLoading"
              :data="allocationList"
              border
              stripe
              style="width: 100%"
            >
              <el-table-column prop="allocationCode" label="下拨编号" width="200" />
              <el-table-column prop="allocationName" label="下拨名称" min-width="150" show-overflow-tooltip />
              <el-table-column prop="allocationAmount" label="下拨金额(元)" width="140" align="right">
                <template slot-scope="scope">
                  {{ formatAmount(scope.row.allocationAmount) }}
                </template>
              </el-table-column>
              <el-table-column prop="allocationStatus" label="下拨状态" width="120">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.allocationStatus === 'COMPLETED'" type="success">已完成</el-tag>
                  <el-tag v-else-if="scope.row.allocationStatus === 'PENDING'" type="warning">待执行</el-tag>
                  <el-tag v-else-if="scope.row.allocationStatus === 'PROCESSING'" type="primary">执行中</el-tag>
                  <el-tag v-else-if="scope.row.allocationStatus === 'FAILED'" type="danger">失败</el-tag>
                  <el-tag v-else-if="scope.row.allocationStatus === 'CANCELLED'" type="info">已取消</el-tag>
                  <span v-else>{{ scope.row.allocationStatus || '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="approvalStatus" label="审批状态" width="120">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.approvalStatus === 'APPROVED'" type="success">已批准</el-tag>
                  <el-tag v-else-if="scope.row.approvalStatus === 'PENDING'" type="warning">待审批</el-tag>
                  <el-tag v-else-if="scope.row.approvalStatus === 'REJECTED'" type="danger">已拒绝</el-tag>
                  <span v-else>{{ scope.row.approvalStatus || '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="approverName" label="审批人" width="100" />
              <el-table-column prop="sourceAccountName" label="来源账户" width="160" show-overflow-tooltip />
              <el-table-column prop="targetAccountName" label="目标账户" width="160" show-overflow-tooltip />
              <el-table-column prop="executionTime" label="执行时间" width="160">
                <template slot-scope="scope">
                  {{ formatDateTime(scope.row.executionTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="280" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="viewAllocationDetail(scope.row)">查看</el-button>
                  <el-button
                    v-if="scope.row.allocationStatus === 'PENDING'"
                    size="mini"
                    type="primary"
                    @click="editAllocation(scope.row)"
                  >修改</el-button>
                  <el-button
                    v-if="scope.row.allocationStatus === 'PENDING'"
                    size="mini"
                    type="danger"
                    @click="deleteAllocation(scope.row)"
                  >删除</el-button>
                  <el-button
                    v-if="scope.row.allocationStatus === 'PROCESSING'"
                    size="mini"
                    type="warning"
                    @click="cancelAllocation(scope.row)"
                  >取消</el-button>
                  <el-button
                    v-if="canRetryAllocation(scope.row)"
                    size="mini"
                    type="success"
                    @click="retryAllocation(scope.row)"
                  >重试</el-button>
                  <el-button
                    v-if="canExecuteAllocation(scope.row)"
                    size="mini"
                    type="primary"
                    @click="executeAllocation(scope.row)"
                  >执行</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页组件 -->
            <div class="pagination-container">
              <el-pagination
                @size-change="handleAllocationSizeChange"
                @current-change="handleAllocationCurrentChange"
                :current-page="allocationQuery.current"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="allocationQuery.size"
                layout="total, sizes, prev, pager, next, jumper"
                :total="allocationTotal"
              />
            </div>
          </div>
        </div>
      </el-tab-pane>

      <!-- 内部借贷 -->
      <el-tab-pane label="内部借贷" name="internalLending">
        <div class="internal-lending-container">
          <!-- 统计卡片 -->
          <div class="statistics-cards">
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="stat-card total-applications">
                  <div class="stat-icon">
                    <i class="el-icon-document"></i>
                  </div>
                  <div class="stat-content">
                    <div class="stat-number">{{ lendingStatistics.totalApplications || 0 }}</div>
                    <div class="stat-label">总申请数</div>
                  </div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="stat-card pending-approval">
                  <div class="stat-icon">
                    <i class="el-icon-time"></i>
                  </div>
                  <div class="stat-content">
                    <div class="stat-number">{{ lendingStatistics.pendingApplications || 0 }}</div>
                    <div class="stat-label">待审批</div>
                  </div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="stat-card today-applications">
                  <div class="stat-icon">
                    <i class="el-icon-date"></i>
                  </div>
                  <div class="stat-content">
                    <div class="stat-number">{{ lendingStatistics.todayApplications || 0 }}</div>
                    <div class="stat-label">今日申请</div>
                  </div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="stat-card success-rate">
                  <div class="stat-icon">
                    <i class="el-icon-success"></i>
                  </div>
                  <div class="stat-content">
                    <div class="stat-number">{{ lendingStatistics.successRate || 0 }}%</div>
                    <div class="stat-label">成功率</div>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>

          <!-- 查询表单 -->
          <div class="query-form">
            <el-form :model="lendingQuery" ref="lendingQueryForm" :inline="true" size="small">
              <el-form-item label="借款编号" prop="loanNo">
                <el-input v-model="lendingQuery.loanNo" placeholder="请输入借款编号" clearable style="width: 200px;"></el-input>
              </el-form-item>
              <el-form-item label="借贷状态" prop="loanStatus">
                <el-select v-model="lendingQuery.loanStatus" placeholder="请选择借贷状态" clearable style="width: 150px;">
                  <el-option label="全部" value=""></el-option>
                  <el-option label="草稿" value="DRAFT"></el-option>
                  <el-option label="待审批" value="PENDING_APPROVAL"></el-option>
                  <el-option label="已批准" value="APPROVED"></el-option>
                  <el-option label="已拒绝" value="REJECTED"></el-option>
                  <el-option label="已放款" value="DISBURSED"></el-option>
                  <el-option label="已取消" value="CANCELLED"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getLendingList" icon="el-icon-search">查询</el-button>
                <el-button @click="resetLendingQuery" icon="el-icon-refresh">重置</el-button>
                <el-button type="success" @click="showCreateLendingDialog" icon="el-icon-plus">新增申请</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 数据表格 -->
          <div class="data-table">
            <el-table
              :data="lendingList"
              v-loading="lendingLoading"
              border
              stripe
              style="width: 100%"
              :header-cell-style="{ background: '#f5f7fa', color: '#606266' }">
              <el-table-column prop="loanCode" label="借款编号" width="180" show-overflow-tooltip></el-table-column>
              <el-table-column prop="loanName" label="借款名称" min-width="140" show-overflow-tooltip></el-table-column>
              <el-table-column prop="borrowAccountName" label="借款方" width="150" show-overflow-tooltip></el-table-column>
              <el-table-column prop="lendAccountName" label="出借方" width="150" show-overflow-tooltip></el-table-column>
              <el-table-column prop="loanAmount" label="借款金额(元)" width="130" align="right">
                <template slot-scope="scope">
                  <span>{{ formatAmount(scope.row.loanAmount) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="loanTerm" label="期限(天)" width="80" align="center"></el-table-column>
              <el-table-column prop="interestRate" label="年利率(%)" width="100" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.row.interestRate }}%</span>
                </template>
              </el-table-column>
              <el-table-column prop="loanStatus" label="借款状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.loanStatus === 'ACTIVE'" type="success" size="small">生效中</el-tag>
                  <el-tag v-else-if="scope.row.loanStatus === 'PENDING'" type="warning" size="small">待处理</el-tag>
                  <el-tag v-else-if="scope.row.loanStatus === 'COMPLETED'" type="info" size="small">已完成</el-tag>
                  <span v-else>{{ scope.row.loanStatus || '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="approvalStatus" label="审批状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.approvalStatus === 'APPROVED'" type="success" size="small">已批准</el-tag>
                  <el-tag v-else-if="scope.row.approvalStatus === 'PENDING'" type="warning" size="small">待审批</el-tag>
                  <el-tag v-else-if="scope.row.approvalStatus === 'REJECTED'" type="danger" size="small">已拒绝</el-tag>
                  <span v-else>{{ scope.row.approvalStatus || '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="申请日期" width="160" align="center">
                <template slot-scope="scope">
                  <span>{{ formatDateTime(scope.row.createTime) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="280" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="viewLendingDetail(scope.row)" icon="el-icon-view">查看</el-button>
                  <el-button
                    v-if="canEditLending(scope.row)"
                    size="mini"
                    type="primary"
                    @click="editLendingApplication(scope.row)"
                    icon="el-icon-edit">
                    修改
                  </el-button>
                  <el-button
                    v-if="canSubmitApplication(scope.row)"
                    size="mini"
                    type="primary"
                    @click="submitApplication(scope.row)"
                    icon="el-icon-upload2">
                    提交
                  </el-button>
                  <el-button
                    v-if="canApproveApplication(scope.row)"
                    size="mini"
                    type="success"
                    @click="approveApplication(scope.row)"
                    icon="el-icon-check">
                    审批
                  </el-button>
                  <el-button
                    v-if="canDisburseApplication(scope.row)"
                    size="mini"
                    type="warning"
                    @click="disburseApplication(scope.row)"
                    icon="el-icon-money">
                    放款
                  </el-button>
                  <el-button
                    v-if="canCancelApplication(scope.row)"
                    size="mini"
                    type="danger"
                    @click="cancelApplication(scope.row)"
                    icon="el-icon-close">
                    取消
                  </el-button>
                  <el-button
                    v-if="canDeleteLending(scope.row)"
                    size="mini"
                    type="danger"
                    @click="deleteLendingApplication(scope.row)"
                    icon="el-icon-delete">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <el-pagination
              @size-change="handleLendingSizeChange"
              @current-change="handleLendingCurrentChange"
              :current-page="lendingQuery.current"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="lendingQuery.size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="lendingTotal">
            </el-pagination>
          </div>
        </div>
      </el-tab-pane>

      <!-- 资金监控 -->
      <el-tab-pane label="资金监控" name="fundMonitoring">
        <div class="fund-monitoring-container">
          <!-- 监控仪表盘 -->
          <div class="monitoring-dashboard">
            <el-row :gutter="20" class="dashboard-cards">
              <el-col :span="6">
                <div class="dashboard-card balance-card">
                  <div class="card-header">
                    <i class="el-icon-wallet"></i>
                    <span>总资金余额</span>
                  </div>
                  <div class="card-value">{{ formatAmount(monitoringDashboard.totalBalance) }}</div>
                  <div class="card-change positive">
                    <i class="el-icon-arrow-up"></i>
                    <span>+{{ formatAmount(monitoringDashboard.todayNetflow) }}</span>
                  </div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="dashboard-card available-card">
                  <div class="card-header">
                    <i class="el-icon-money"></i>
                    <span>可用余额</span>
                  </div>
                  <div class="card-value">{{ formatAmount(monitoringDashboard.availableBalance) }}</div>
                  <div class="card-ratio">
                    <span>利用率: {{ monitoringDashboard.fundUtilizationRate }}%</span>
                  </div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="dashboard-card pool-card">
                  <div class="card-header">
                    <i class="el-icon-data-analysis"></i>
                    <span>活跃资金池</span>
                  </div>
                  <div class="card-value">{{ monitoringDashboard.activePools }}/{{ monitoringDashboard.totalPools }}</div>
                  <div class="card-ratio">
                    <span>健康度: {{ getPoolHealthDisplay() }}</span>
                  </div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="dashboard-card alert-card">
                  <div class="card-header">
                    <i class="el-icon-warning"></i>
                    <span>待处理预警</span>
                  </div>
                  <div class="card-value">{{ monitoringDashboard.pendingAlerts }}</div>
                  <div class="card-ratio">
                    <span>高风险: {{ monitoringDashboard.highRiskAlerts }}</span>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>

          <!-- 监控内容区域 -->
          <div class="monitoring-content">
            <el-tabs v-model="monitoringActiveTab" @tab-click="handleMonitoringTabClick">
              <!-- 实时监控 -->
              <el-tab-pane label="实时监控" name="realtime">
                <div class="realtime-monitoring">
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-card title="资金流动趋势" class="trend-card">
                        <div class="trend-chart">
                          <el-row :gutter="12" class="trend-stats">
                            <el-col :span="12">
                              <div class="trend-stat-item">
                                <div class="trend-stat-label">今日净流入</div>
                                <div class="trend-stat-value" :class="monitoringDashboard.todayNetflow >= 0 ? 'positive' : 'negative'">
                                  {{ formatAmount(monitoringDashboard.todayNetflow) }}
                                </div>
                              </div>
                            </el-col>
                            <el-col :span="12">
                              <div class="trend-stat-item">
                                <div class="trend-stat-label">本月累计流入</div>
                                <div class="trend-stat-value positive">
                                  {{ formatAmount(monitoringDashboard.monthAmount) }}
                                </div>
                              </div>
                            </el-col>
                            <el-col :span="12">
                              <div class="trend-stat-item">
                                <div class="trend-stat-label">执行任务总额</div>
                                <div class="trend-stat-value">
                                  {{ formatAmount(monitoringDashboard.totalExecAmount) }}
                                </div>
                              </div>
                            </el-col>
                            <el-col :span="12">
                              <div class="trend-stat-item">
                                <div class="trend-stat-label">任务成功/总数</div>
                                <div class="trend-stat-value">
                                  {{ monitoringDashboard.successTasks }} / {{ monitoringDashboard.totalTasks }}
                                </div>
                              </div>
                            </el-col>
                            <el-col :span="12">
                              <div class="trend-stat-item">
                                <div class="trend-stat-label">运行中任务</div>
                                <div class="trend-stat-value">
                                  {{ monitoringDashboard.runningConcentrationTasks }}
                                </div>
                              </div>
                            </el-col>
                            <el-col :span="12">
                              <div class="trend-stat-item">
                                <div class="trend-stat-label">失败任务</div>
                                <div class="trend-stat-value" :class="monitoringDashboard.failedTasks > 0 ? 'negative' : ''">
                                  {{ monitoringDashboard.failedTasks }}
                                </div>
                              </div>
                            </el-col>
                          </el-row>
                        </div>
                      </el-card>
                    </el-col>
                    <el-col :span="12">
                      <el-card title="余额分布" class="balance-card">
                        <div class="balance-distribution">
                          <div v-for="bank in bankBalances" :key="bank.bankName" class="balance-item">
                            <div class="bank-info">
                              <span class="bank-name">{{ bank.bankName }}</span>
                              <el-tag :type="getBankStatusType(bank.status)" size="mini">{{ getBankStatusDisplay(bank.status) }}</el-tag>
                            </div>
                            <div class="bank-balance">{{ formatAmount(bank.balance) }}</div>
                          </div>
                        </div>
                      </el-card>
                    </el-col>
                  </el-row>
                </div>
              </el-tab-pane>

              <!-- 预警管理 -->
              <el-tab-pane label="预警管理" name="alerts">
                <div class="alerts-management">
                  <!-- 查询表单 -->
                  <div class="query-form">
                    <el-form :model="alertQuery" inline>
                      <el-form-item label="预警类型">
                        <el-select v-model="alertQuery.alertType" placeholder="请选择预警类型" clearable>
                          <el-option label="余额预警" value="BALANCE"></el-option>
                          <el-option label="流动性预警" value="LIQUIDITY"></el-option>
                          <el-option label="风险预警" value="RISK"></el-option>
                          <el-option label="合规预警" value="COMPLIANCE"></el-option>
                        </el-select>
                      </el-form-item>
                      <el-form-item label="预警级别">
                        <el-select v-model="alertQuery.alertLevel" placeholder="请选择预警级别" clearable>
                          <el-option label="低级" value="LOW"></el-option>
                          <el-option label="中级" value="MEDIUM"></el-option>
                          <el-option label="高级" value="HIGH"></el-option>
                          <el-option label="紧急" value="CRITICAL"></el-option>
                        </el-select>
                      </el-form-item>
                      <el-form-item label="预警状态">
                        <el-select v-model="alertQuery.alertStatus" placeholder="请选择预警状态" clearable>
                          <el-option label="待处理" value="PENDING"></el-option>
                          <el-option label="处理中" value="PROCESSING"></el-option>
                          <el-option label="已解决" value="RESOLVED"></el-option>
                          <el-option label="已忽略" value="IGNORED"></el-option>
                        </el-select>
                      </el-form-item>
                      <el-form-item>
                        <el-button type="primary" @click="getAlertsList">查询</el-button>
                        <el-button @click="resetAlertQuery">重置</el-button>
                      </el-form-item>
                    </el-form>
                  </div>

                  <!-- 预警列表 -->
                  <div class="alerts-table">
                    <el-table :data="alertsList" v-loading="alertsLoading" border>
                      <el-table-column prop="alertId" label="预警编号" width="150"></el-table-column>
                      <el-table-column prop="alertTitle" label="预警标题" min-width="200" show-overflow-tooltip></el-table-column>
                      <el-table-column prop="alertType" label="预警类型" width="120">
                        <template slot-scope="scope">
                          <el-tag :type="getAlertTypeTagType(scope.row.alertType)" size="mini">
                            {{ getAlertTypeDisplay(scope.row.alertType) }}
                          </el-tag>
                        </template>
                      </el-table-column>
                      <el-table-column prop="alertLevel" label="预警级别" width="120">
                        <template slot-scope="scope">
                          <el-tag :type="getAlertLevelTagType(scope.row.alertLevel)" size="mini">
                            {{ getAlertLevelDisplay(scope.row.alertLevel) }}
                          </el-tag>
                        </template>
                      </el-table-column>
                      <el-table-column prop="alertStatus" label="预警状态" width="120">
                        <template slot-scope="scope">
                          <el-tag :type="getAlertStatusTagType(scope.row.alertStatus)" size="mini">
                            {{ getAlertStatusDisplay(scope.row.alertStatus) }}
                          </el-tag>
                        </template>
                      </el-table-column>
                      <el-table-column prop="alertTime" label="触发时间" width="180">
                        <template slot-scope="scope">
                          {{ formatDateTime(scope.row.alertTime) }}
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" width="200">
                        <template slot-scope="scope">
                          <el-button size="mini" @click="viewAlertDetail(scope.row)">查看</el-button>
                          <el-button v-if="canHandleAlert(scope.row)" size="mini" type="primary" @click="handleAlert(scope.row)">处理</el-button>
                          <el-button v-if="canResolveAlert(scope.row)" size="mini" type="success" @click="resolveAlert(scope.row)">解决</el-button>
                        </template>
                      </el-table-column>
                    </el-table>

                    <!-- 分页 -->
                    <div class="pagination-container">
                      <el-pagination
                        @size-change="handleAlertsSizeChange"
                        @current-change="handleAlertsCurrentChange"
                        :current-page="alertQuery.current"
                        :page-sizes="[10, 20, 50, 100]"
                        :page-size="alertQuery.size"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="alertsTotal">
                      </el-pagination>
                    </div>
                  </div>
                </div>
              </el-tab-pane>

              <!-- 统计报表 -->
              <el-tab-pane label="统计报表" name="reports">
                <div class="reports-section">
                  <el-row :gutter="20">
                    <el-col :span="8">
                      <el-card title="预警统计">
                        <div class="stats-item">
                          <span class="stats-label">总预警数:</span>
                          <span class="stats-value">{{ alertStatistics.totalAlerts }}</span>
                        </div>
                        <div class="stats-item">
                          <span class="stats-label">待处理:</span>
                          <span class="stats-value">{{ alertStatistics.pendingAlerts }}</span>
                        </div>
                        <div class="stats-item">
                          <span class="stats-label">已解决:</span>
                          <span class="stats-value">{{ alertStatistics.resolvedAlerts }}</span>
                        </div>
                        <div class="stats-item">
                          <span class="stats-label">解决率:</span>
                          <span class="stats-value">{{ alertStatistics.resolutionRate }}%</span>
                        </div>
                      </el-card>
                    </el-col>
                    <el-col :span="8">
                      <el-card title="系统状态">
                        <div class="stats-item">
                          <span class="stats-label">系统状态:</span>
                          <el-tag :type="getSystemStatusType(monitoringDashboard.systemStatus)" size="mini">
                            {{ getSystemStatusDisplay(monitoringDashboard.systemStatus) }}
                          </el-tag>
                        </div>
                        <div class="stats-item">
                          <span class="stats-label">风险评级:</span>
                          <el-tag :type="getRiskRatingType(monitoringDashboard.riskRating)" size="mini">
                            {{ getRiskRatingDisplay(monitoringDashboard.riskRating) }}
                          </el-tag>
                        </div>
                        <div class="stats-item">
                          <span class="stats-label">合规状态:</span>
                          <el-tag :type="getComplianceStatusType(monitoringDashboard.complianceStatus)" size="mini">
                            {{ getComplianceStatusDisplay(monitoringDashboard.complianceStatus) }}
                          </el-tag>
                        </div>
                      </el-card>
                    </el-col>
                    <el-col :span="8">
                      <el-card title="资金效率">
                        <div class="stats-item">
                          <span class="stats-label">资金利用率:</span>
                          <span class="stats-value">{{ monitoringDashboard.fundUtilizationRate }}%</span>
                        </div>
                        <div class="stats-item">
                          <span class="stats-label">流动性比率:</span>
                          <span class="stats-value">{{ monitoringDashboard.liquidityRatio }}%</span>
                        </div>
                        <div class="stats-item">
                          <span class="stats-label">今日净流入:</span>
                          <span class="stats-value">{{ formatAmount(monitoringDashboard.todayNetflow) }}</span>
                        </div>
                      </el-card>
                    </el-col>
                  </el-row>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改资金池对话框 -->
    <el-dialog :title="fundPoolTitle" :visible.sync="fundPoolOpen" width="800px" append-to-body>
      <el-form ref="fundPoolForm" :model="fundPoolForm" :rules="fundPoolViewOnly ? {} : fundPoolRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="资金池名称" prop="poolName">
              <el-input v-model="fundPoolForm.poolName" placeholder="请输入资金池名称" :disabled="fundPoolViewOnly" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="池类型" prop="poolType">
              <el-select v-model="fundPoolForm.poolType" placeholder="请选择池类型" :disabled="fundPoolViewOnly">
                <el-option label="实体资金池" value="PHYSICAL" />
                <el-option label="虚拟资金池" value="VIRTUAL" />
                <el-option label="名义资金池" value="NOTIONAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属公司" prop="companyName">
              <el-input v-model="fundPoolForm.companyName" placeholder="请输入所属公司名称" :disabled="fundPoolViewOnly" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="池状态" prop="status">
              <el-select v-model="fundPoolForm.status" placeholder="请选择池状态" :disabled="fundPoolViewOnly">
                <el-option label="启用" value="ACTIVE" />
                <el-option label="停用" value="INACTIVE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="池描述" prop="description">
          <el-input v-model="fundPoolForm.description" type="textarea" placeholder="请输入池描述" :disabled="fundPoolViewOnly" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <template v-if="fundPoolViewOnly">
          <el-button @click="cancelFundPool">关 闭</el-button>
        </template>
        <template v-else>
          <el-button type="primary" @click="submitFundPoolForm">确 定</el-button>
          <el-button @click="cancelFundPool">取 消</el-button>
        </template>
      </div>
    </el-dialog>

    <!-- 新增/编辑/查看归集规则对话框 -->
    <el-dialog :title="concentrationRuleDialogTitle" :visible.sync="concentrationRuleDialogVisible" width="600px" append-to-body @close="concentrationRuleViewOnly = false">
      <el-form ref="concentrationRuleForm" :model="concentrationRuleForm" :rules="concentrationRuleViewOnly ? {} : concentrationRuleRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="策略编号" prop="strategyCode">
              <el-input v-model="concentrationRuleForm.strategyCode" placeholder="请输入策略编号" :disabled="concentrationRuleViewOnly" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="策略名称" prop="strategyName">
              <el-input v-model="concentrationRuleForm.strategyName" placeholder="请输入策略名称" :disabled="concentrationRuleViewOnly" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="策略类型" prop="strategyType">
              <el-select v-model="concentrationRuleForm.strategyType" placeholder="请选择策略类型" style="width:100%" :disabled="concentrationRuleViewOnly">
                <el-option label="上划归集" value="SWEEP_UP" />
                <el-option label="下拨归集" value="SWEEP_DOWN" />
                <el-option label="双向归集" value="BIDIRECTIONAL" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行频率" prop="frequency">
              <el-select v-model="concentrationRuleForm.frequency" placeholder="请选择执行频率" style="width:100%" :disabled="concentrationRuleViewOnly">
                <el-option label="每日" value="DAILY" />
                <el-option label="每周" value="WEEKLY" />
                <el-option label="每月" value="MONTHLY" />
                <el-option label="手动" value="MANUAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="目标账户ID" prop="targetAccountId">
              <el-input v-model.number="concentrationRuleForm.targetAccountId" placeholder="请输入目标账户ID" :disabled="concentrationRuleViewOnly" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标账户名称" prop="targetAccountName">
              <el-input v-model="concentrationRuleForm.targetAccountName" placeholder="请输入目标账户名称" :disabled="concentrationRuleViewOnly" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="目标账户号" prop="targetAccountNumber">
              <el-input v-model="concentrationRuleForm.targetAccountNumber" placeholder="请输入目标账户号" :disabled="concentrationRuleViewOnly" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="执行时间">
              <el-input v-model="concentrationRuleForm.executionTime" placeholder="如：09:00" :disabled="concentrationRuleViewOnly" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级">
              <el-input-number v-model="concentrationRuleForm.priority" :min="1" :max="10" style="width:100%" :disabled="concentrationRuleViewOnly" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="归集条件">
          <el-input v-model="concentrationRuleForm.concentrationCondition" placeholder="请输入归集条件" :disabled="concentrationRuleViewOnly" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="concentrationRuleForm.description" type="textarea" :rows="2" placeholder="请输入描述" :disabled="concentrationRuleViewOnly" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="!concentrationRuleViewOnly" type="primary" @click="submitConcentrationRuleForm">确 定</el-button>
        <el-button @click="concentrationRuleDialogVisible = false">{{ concentrationRuleViewOnly ? '关 闭' : '取 消' }}</el-button>
      </div>
    </el-dialog>

    <!-- 新增/编辑资金下拨对话框 -->
    <el-dialog :title="allocationDialogTitle" :visible.sync="allocationDialogVisible" width="600px" append-to-body @close="allocationForm = {}">
      <el-form ref="allocationForm" :model="allocationForm" :rules="allocationFormRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="下拨名称" prop="allocationName">
              <el-input v-model="allocationForm.allocationName" placeholder="请输入下拨名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下拨金额" prop="allocationAmount">
              <el-input-number v-model="allocationForm.allocationAmount" :min="0" :precision="2" style="width:100%" placeholder="请输入金额" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="来源账户名称" prop="sourceAccountName">
              <el-input v-model="allocationForm.sourceAccountName" placeholder="请输入来源账户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标账户名称" prop="targetAccountName">
              <el-input v-model="allocationForm.targetAccountName" placeholder="请输入目标账户名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="来源账户ID">
              <el-input v-model="allocationForm.sourceAccountId" placeholder="请输入来源账户ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标账户ID">
              <el-input v-model="allocationForm.targetAccountId" placeholder="请输入目标账户ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述">
          <el-input v-model="allocationForm.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="allocationForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAllocationForm">确 定</el-button>
        <el-button @click="allocationDialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看资金下拨详情对话框 -->
    <el-dialog title="资金下拨详情" :visible.sync="allocationDetailDialogVisible" width="600px" append-to-body>
      <el-form :model="allocationDetailData" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="下拨编号"><span>{{ allocationDetailData.allocationCode || '-' }}</span></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下拨名称"><span>{{ allocationDetailData.allocationName || '-' }}</span></el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="下拨金额"><span>{{ formatAmount(allocationDetailData.allocationAmount) }}</span></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下拨状态">
              <el-tag v-if="allocationDetailData.allocationStatus === 'COMPLETED'" type="success">已完成</el-tag>
              <el-tag v-else-if="allocationDetailData.allocationStatus === 'PENDING'" type="warning">待执行</el-tag>
              <el-tag v-else-if="allocationDetailData.allocationStatus === 'PROCESSING'" type="primary">执行中</el-tag>
              <el-tag v-else-if="allocationDetailData.allocationStatus === 'FAILED'" type="danger">失败</el-tag>
              <el-tag v-else-if="allocationDetailData.allocationStatus === 'CANCELLED'" type="info">已取消</el-tag>
              <span v-else>{{ allocationDetailData.allocationStatus || '-' }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="来源账户"><span>{{ allocationDetailData.sourceAccountName || '-' }}</span></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标账户"><span>{{ allocationDetailData.targetAccountName || '-' }}</span></el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="审批状态">
              <el-tag v-if="allocationDetailData.approvalStatus === 'APPROVED'" type="success">已批准</el-tag>
              <el-tag v-else-if="allocationDetailData.approvalStatus === 'PENDING'" type="warning">待审批</el-tag>
              <el-tag v-else-if="allocationDetailData.approvalStatus === 'REJECTED'" type="danger">已拒绝</el-tag>
              <span v-else>{{ allocationDetailData.approvalStatus || '-' }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审批人"><span>{{ allocationDetailData.approverName || '-' }}</span></el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="审批意见"><span>{{ allocationDetailData.approvalOpinion || '-' }}</span></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行时间"><span>{{ formatDateTime(allocationDetailData.executionTime) }}</span></el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述"><span>{{ allocationDetailData.description || '-' }}</span></el-form-item>
        <el-form-item label="备注"><span>{{ allocationDetailData.remark || '-' }}</span></el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="创建时间"><span>{{ formatDateTime(allocationDetailData.createTime) }}</span></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="更新时间"><span>{{ formatDateTime(allocationDetailData.updateTime) }}</span></el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="allocationDetailDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 新增/编辑内部借贷对话框 -->
    <el-dialog :title="lendingDialogTitle" :visible.sync="lendingDialogVisible" width="640px" append-to-body @close="resetLendingForm">
      <el-form ref="lendingForm" :model="lendingForm" :rules="lendingRules" label-width="110px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="借款账户名称" prop="borrowAccountName">
              <el-input v-model="lendingForm.borrowAccountName" placeholder="请输入借款账户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出借账户名称" prop="lendAccountName">
              <el-input v-model="lendingForm.lendAccountName" placeholder="请输入出借账户名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="借贷金额" prop="loanAmount">
              <el-input-number v-model="lendingForm.loanAmount" :min="0" :precision="2" style="width:100%" placeholder="请输入金额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种">
              <el-select v-model="lendingForm.currency" placeholder="请选择币种" style="width:100%">
                <el-option label="人民币(CNY)" value="CNY" />
                <el-option label="美元(USD)" value="USD" />
                <el-option label="欧元(EUR)" value="EUR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="借贷期限(天)" prop="loanTerm">
              <el-input-number v-model="lendingForm.loanTerm" :min="1" style="width:100%" placeholder="请输入天数" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年利率(%)" prop="interestRate">
              <el-input-number v-model="lendingForm.interestRate" :min="0" :precision="4" style="width:100%" placeholder="请输入利率" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="还款方式" prop="repaymentMethod">
              <el-select v-model="lendingForm.repaymentMethod" placeholder="请选择还款方式" style="width:100%">
                <el-option label="等额还款" value="EQUAL_INSTALLMENT" />
                <el-option label="到期一次还清" value="BULLET" />
                <el-option label="等额本金" value="EQUAL_PRINCIPAL" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="借贷名称">
              <el-input v-model="lendingForm.loanName" placeholder="请输入借贷名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="借贷用途">
          <el-input v-model="lendingForm.loanPurpose" type="textarea" :rows="2" placeholder="请输入借贷用途" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="lendingForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitLendingForm">确 定</el-button>
        <el-button @click="lendingDialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getFundPoolPage,
  getFundPool,
  createFundPool,
  updateFundPool,
  deleteFundPool,
  batchDeleteFundPool,
  // 资金归集相关API
  getFundConcentrationStrategyPage,
  createFundConcentrationStrategy,
  updateFundConcentrationStrategy,
  enableConcentrationStrategy,
  disableConcentrationStrategy,
  deleteConcentrationStrategy,
  testConcentrationStrategy,
  getExecutionMonitorPage,
  // 资金下拨相关API
  getFundAllocationPage,
  getFundAllocation,
  createFundAllocation,
  updateFundAllocation,
  deleteFundAllocation,
  submitFundAllocation,
  approveFundAllocation,
  rejectFundAllocation,
  executeFundAllocation,
  cancelFundAllocation,
  retryFundAllocation,
  // 内部借贷相关API
  getInternalLoanPage,
  getInternalLoan,
  createInternalLoan,
  updateInternalLoan,
  deleteInternalLoan,
  cancelInternalLoan,
  submitInternalLoan,
  approveInternalLoan,
  disburseLoan,
  repayLoan,
  calculateInterest,
  // 资金监控相关API
  getFundMonitoringDashboard,
  getFundFlowAnalysis,
  getBalanceMonitoring,
  getAlertList,
  handleAlert,
  setMonitoringRule,
  getMonitoringRules,
  // 统计数据API
  getConcentrationStatistics,
  getAllocationStatistics,
  getLendingStatistics,
  getAlertStatistics
} from "@/api/globalTreasurer/zjjz";
import { parseTime } from '@/utils'

export default {
  name: "FundConcentration",
  data() {
    return {
      // 当前激活的标签页
      activeTab: "fundPool",
      // 显示搜索条件
      showSearch: true,
      // 分页布局
      layout: "total, sizes, prev, pager, next, jumper",
      
      // 资金池相关数据
      fundPoolLoading: true,
      fundPoolIds: [],
      fundPoolSingle: true,
      fundPoolMultiple: true,
      fundPoolTotal: 0,
      fundPoolList: [],
      fundPoolTitle: "",
      fundPoolOpen: false,
      fundPoolViewOnly: false,
      fundPoolQuery: {
        pageNum: 1,
        pageSize: 10,
        poolName: null,
        status: null
      },
      fundPoolForm: {
        poolId: null,
        poolName: null,
        poolCode: null,
        poolType: null,
        currency: "CNY",
        companyName: null,
        status: "ACTIVE",
        isEnabled: 1,
        managerId: null,
        description: null,
        remark: null
      },
      fundPoolRules: {
        poolName: [
          { required: true, message: "资金池名称不能为空", trigger: "blur" }
        ],
        poolType: [
          { required: true, message: "池类型不能为空", trigger: "change" }
        ],
        companyName: [
          { required: true, message: "所属公司不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "池状态不能为空", trigger: "change" }
        ]
      },
      
      // 字典选项
      poolTypeOptions: [
        { label: "实体资金池", value: "PHYSICAL" },
        { label: "虚拟资金池", value: "VIRTUAL" },
        { label: "名义资金池", value: "NOTIONAL" }
      ],

      // 资金归集相关数据
      concentrationLoading: false,
      concentrationRulesList: [],
      concentrationTotal: 0,
      concentrationQuery: {
        pageNo: 1,
        pageSize: 10,
        strategyName: null,
        strategyType: null,
        status: null
      },
      concentrationStatistics: {
        totalRules: 0,
        activeRules: 0,
        todayExecutions: 0,
        successRate: 0
      },
      concentrationRuleForm: {},
      concentrationRuleDialogVisible: false,
      concentrationRuleViewOnly: false,
      concentrationRuleDialogTitle: '',
      concentrationRuleRules: {
        strategyCode: [{ required: true, message: "策略编号不能为空", trigger: "blur" }],
        strategyName: [{ required: true, message: "策略名称不能为空", trigger: "blur" }],
        strategyType: [{ required: true, message: "策略类型不能为空", trigger: "change" }],
        targetAccountId: [{ required: true, message: "目标账户ID不能为空", trigger: "blur" }],
        targetAccountName: [{ required: true, message: "目标账户名称不能为空", trigger: "blur" }],
        targetAccountNumber: [{ required: true, message: "目标账户号不能为空", trigger: "blur" }]
      },
      poolStatusOptions: [
        { label: "启用", value: "ACTIVE" },
        { label: "停用", value: "INACTIVE" }
      ],

      // 资金下拨相关数据
      allocationLoading: false,
      allocationList: [],
      allocationTotal: 0,
      allocationQuery: {
        current: 1,
        size: 10,
        executionNo: null,
        ruleName: null,
        executionType: null,
        executionStatus: null,
        startDate: null,
        endDate: null
      },
      allocationStatistics: {
        totalApplications: 0,
        pendingApproval: 0,
        todayExecutions: 0,
        successRate: 0
      },
      allocationForm: {},
      allocationDialogVisible: false,
      allocationDialogTitle: '',
      allocationDetailDialogVisible: false,
      allocationDetailData: {},
      allocationFormRules: {
        allocationName: [{ required: true, message: '下拨名称不能为空', trigger: 'blur' }],
        allocationAmount: [{ required: true, message: '下拨金额不能为空', trigger: 'blur' }]
      },
      allocationRules: {
        ruleId: [
          { required: true, message: "规则不能为空", trigger: "change" }
        ],
        executionType: [
          { required: true, message: "执行类型不能为空", trigger: "change" }
        ],
        totalAmount: [
          { required: true, message: "总金额不能为空", trigger: "blur" }
        ]
      },

      // 内部借贷相关数据
      lendingLoading: false,
      lendingList: [],
      lendingTotal: 0,
      lendingQuery: {
        current: 1,
        size: 10,
        loanNo: null,
        loanStatus: null,
        startDate: null,
        endDate: null
      },
      lendingStatistics: {
        totalApplications: 0,
        pendingApplications: 0,
        todayApplications: 0,
        successRate: 0
      },
      lendingForm: {},
      lendingDialogVisible: false,
      lendingDialogTitle: '',
      lendingDetailDialogVisible: false,
      lendingDetailData: {},
      lendingRules: {
        borrowAccountName: [
          { required: true, message: "借款账户名称不能为空", trigger: "blur" }
        ],
        lendAccountName: [
          { required: true, message: "出借账户名称不能为空", trigger: "blur" }
        ],
        loanAmount: [
          { required: true, message: "借款金额不能为空", trigger: "blur" }
        ],
        loanTerm: [
          { required: true, message: "借款期限不能为空", trigger: "blur" }
        ],
        interestRate: [
          { required: true, message: "年利率不能为空", trigger: "blur" }
        ],
        repaymentMethod: [
          { required: true, message: "还款方式不能为空", trigger: "change" }
        ]
      },

      // 资金监控相关数据
      monitoringActiveTab: 'realtime',
      monitoringDashboard: {
        totalBalance: 0,
        availableBalance: 0,
        frozenBalance: 0,
        todayInflow: 0,
        todayOutflow: 0,
        todayNetflow: 0,
        activePools: 0,
        totalPools: 0,
        runningConcentrationTasks: 0,
        runningAllocationTasks: 0,
        pendingAlerts: 0,
        highRiskAlerts: 0,
        fundUtilizationRate: 0,
        liquidityRatio: 0,
        riskRating: 'LOW',
        complianceStatus: 'COMPLIANT',
        systemStatus: 'NORMAL',
        lastUpdateTime: ''
      },
      bankBalances: [],
      alertQuery: {
        current: 1,
        size: 10,
        alertType: '',
        alertLevel: '',
        alertStatus: ''
      },
      alertsList: [],
      alertsTotal: 0,
      alertsLoading: false,
      alertStatistics: {
        totalAlerts: 0,
        pendingAlerts: 0,
        processingAlerts: 0,
        resolvedAlerts: 0,
        ignoredAlerts: 0,
        resolutionRate: 0
      },
      alertDetailDialogVisible: false,
      alertHandleDialogVisible: false,
      currentAlert: {},
      handleForm: {
        action: '',
        comment: ''
      }
    };
  },
  created() {
    this.getFundPoolList();
    this.getAllocationStatisticsData();
    this.getLendingStatisticsData();
    this.getMonitoringDashboard();
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

    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'fundPool') {
        this.getFundPoolList();
      } else if (tab.name === 'fundConcentration') {
        this.getConcentrationRulesList();
        this.getConcentrationStatisticsData();
      } else if (tab.name === 'fundAllocation') {
        this.getAllocationsList();
        this.getAllocationStatisticsData();
      } else if (tab.name === 'internalLending') {
        this.getLendingList();
        this.getLendingStatisticsData();
      } else if (tab.name === 'fundMonitoring') {
        this.getMonitoringDashboard();
        this.getAlertsList();
      }
    },

    /** 查询资金池列表 */
    async getFundPoolList() {
      this.fundPoolLoading = true;
      try {
        const response = await getFundPoolPage(this.fundPoolQuery);
        
        // 使用与配置文件一致的成功状态码
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && response.data.tlist !== undefined) {
            // PageInfo格式：{ code: 200, data: { tlist: [], totalRecord: 0 } }
            this.fundPoolList = response.data.tlist || [];
            this.fundPoolTotal = parseInt(response.data.totalRecord) || 0;
          } else if (response.data && response.data.list !== undefined) {
            // 标准格式：{ code: 200, data: { list: [], total: 0 } }
            this.fundPoolList = response.data.list || [];
            this.fundPoolTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            // 数组格式：{ code: 200, data: [] }
            this.fundPoolList = response.data || [];
            this.fundPoolTotal = response.data.length || 0;
          } else if (response.tlist !== undefined) {
            // 直接PageInfo格式：{ code: 200, tlist: [], totalRecord: 0 }
            this.fundPoolList = response.tlist || [];
            this.fundPoolTotal = response.totalRecord || 0;
          } else if (response.list !== undefined) {
            // 直接格式：{ code: 200, list: [], total: 0 }
            this.fundPoolList = response.list || [];
            this.fundPoolTotal = response.total || 0;
          } else {
            // 兜底处理
            this.fundPoolList = [];
            this.fundPoolTotal = 0;
          }
        } else {
          this.$message.error(response.message || response.msg || '查询失败');
          this.fundPoolList = [];
          this.fundPoolTotal = 0;
        }
      } catch (error) {
        console.error('获取资金池列表失败:', error);
        this.$message.error('获取数据失败：' + error.message);
        this.fundPoolList = [];
        this.fundPoolTotal = 0;
      }
      this.fundPoolLoading = false;
    },

    /** 搜索按钮操作 */
    handleFundPoolQuery() {
      this.fundPoolQuery.pageNum = 1;
      this.getFundPoolList();
    },

    /** 重置按钮操作 */
    resetFundPoolQuery() {
      this.$refs["fundPoolQueryForm"] && this.$refs["fundPoolQueryForm"].resetFields();
      this.handleFundPoolQuery();
    },

    /** 多选框选中数据 */
    handleFundPoolSelectionChange(selection) {
      this.fundPoolIds = selection.map(item => item.poolId);
      this.fundPoolSingle = selection.length !== 1;
      this.fundPoolMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleFundPoolAdd() {
      this.resetFundPoolForm();
      this.fundPoolOpen = true;
      this.fundPoolTitle = "添加资金池";
    },

    /** 修改按钮操作 */
    handleFundPoolUpdate(row) {
      this.resetFundPoolForm();
      const poolId = row.poolId || this.fundPoolIds;
      getFundPool(poolId).then(response => {
        Object.assign(this.fundPoolForm, response.data);
        this.fundPoolOpen = true;
        this.fundPoolTitle = "修改资金池";
      });
    },

    /** 查看按钮操作 */
    handleFundPoolView(row) {
      this.resetFundPoolForm();
      const poolId = row.poolId;
      getFundPool(poolId).then(response => {
        Object.assign(this.fundPoolForm, response.data);
        this.fundPoolViewOnly = true;
        this.fundPoolOpen = true;
        this.fundPoolTitle = "查看资金池";
      });
    },

    /** 提交按钮 */
    submitFundPoolForm() {
      this.$refs["fundPoolForm"].validate(valid => {
        if (valid) {
          if (this.fundPoolForm.poolId != null) {
            updateFundPool(this.fundPoolForm).then(response => {
              this.$message.success("修改成功");
              this.fundPoolOpen = false;
              this.getFundPoolList();
            });
          } else {
            createFundPool(this.fundPoolForm).then(response => {
              this.$message.success("新增成功");
              this.fundPoolOpen = false;
              this.getFundPoolList();
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleFundPoolDelete(row) {
      const isBatch = !row || !row.poolId;
      const poolIds = isBatch ? this.fundPoolIds : [row.poolId];
      const label = poolIds.join(',');
      this.$confirm('是否确认删除资金池编号为"' + label + '"的数据项？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        if (isBatch) {
          return batchDeleteFundPool(poolIds);
        } else {
          return deleteFundPool(poolIds[0]);
        }
      }).then(() => {
        this.getFundPoolList();
        this.$message.success("删除成功");
      }).catch(() => {});
    },

    /** 取消按钮 */
    cancelFundPool() {
      this.fundPoolOpen = false;
      this.fundPoolViewOnly = false;
      this.resetFundPoolForm();
    },

    /** 表单重置 */
    resetFundPoolForm() {
      Object.assign(this.fundPoolForm, {
        poolId: null,
        poolName: null,
        poolCode: null,
        poolType: null,
        currency: "CNY",
        companyName: null,
        status: "ACTIVE",
        isEnabled: 1,
        managerId: null,
        description: null,
        remark: null
      });
      this.$nextTick(() => {
        this.$refs["fundPoolForm"] && this.$refs["fundPoolForm"].resetFields();
      });
    },

    /** 分页相关方法 */
    handleFundPoolCurrentChange(val) {
      this.fundPoolQuery.pageNum = val;
      this.getFundPoolList();
    },

    handleFundPoolSizeChange(val) {
      this.fundPoolQuery.pageSize = val;
      this.getFundPoolList();
    },

    // ==================== 资金归集相关方法 ====================

    /** 获取资金归集规则列表 */
    async getConcentrationRulesList() {
      this.concentrationLoading = true;
      try {
        const response = await getFundConcentrationStrategyPage(this.concentrationQuery);

        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && response.data.tlist !== undefined) {
            this.concentrationRulesList = response.data.tlist || [];
            this.concentrationTotal = parseInt(response.data.totalRecord) || 0;
          } else if (response.data && response.data.list !== undefined) {
            this.concentrationRulesList = response.data.list || [];
            this.concentrationTotal = response.data.total || 0;
          } else if (response.data && response.data.records !== undefined) {
            this.concentrationRulesList = response.data.records || [];
            this.concentrationTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            this.concentrationRulesList = response.data || [];
            this.concentrationTotal = response.data.length || 0;
          } else {
            this.concentrationRulesList = [];
            this.concentrationTotal = 0;
          }
        } else {
          this.$message.error(response.message || response.msg || '查询失败');
          this.concentrationRulesList = [];
          this.concentrationTotal = 0;
        }
      } catch (error) {
        console.error('获取资金归集规则列表失败:', error);
        this.$message.error('获取数据失败，请检查网络连接或稍后重试');
        this.concentrationRulesList = [];
        this.concentrationTotal = 0;
      } finally {
        this.concentrationLoading = false;
      }
    },

    /** 获取资金归集统计数据 */
    async getConcentrationStatisticsData() {
      try {
        const response = await getConcentrationStatistics();
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code) && response.data) {
          this.concentrationStatistics = {
            totalRules: response.data.totalRules || this.concentrationRulesList.length,
            activeRules: response.data.activeRules || this.concentrationRulesList.filter(rule => rule.isEnabled).length,
            todayExecutions: response.data.todayExecutions || 0,
            successRate: response.data.successRate || 0
          };
        } else {
          // 如果API调用失败，使用本地数据计算
          this.concentrationStatistics = {
            totalRules: this.concentrationRulesList.length,
            activeRules: this.concentrationRulesList.filter(rule => rule.isEnabled).length,
            todayExecutions: 0,
            successRate: 0
          };
        }
      } catch (error) {
        console.error('获取资金归集统计数据失败:', error);
        this.concentrationStatistics = {
          totalRules: this.concentrationRulesList.length,
          activeRules: this.concentrationRulesList.filter(rule => rule.isEnabled).length,
          todayExecutions: 0,
          successRate: 0
        };
      }
    },

    /** 重置查询条件 */
    resetConcentrationQuery() {
      this.concentrationQuery = {
        pageNo: 1,
        pageSize: 10,
        strategyName: null,
        strategyType: null,
        status: null
      };
      this.getConcentrationRulesList();
    },

    /** 显示新增规则对话框 */
    showCreateConcentrationRuleDialog() {
      this.concentrationRuleForm = {
        strategyCode: '',
        strategyName: '',
        strategyType: '',
        targetAccountId: null,
        targetAccountName: '',
        targetAccountNumber: '',
        concentrationCondition: '',
        frequency: '',
        executionTime: '',
        priority: 5,
        status: 'ACTIVE',
        description: '',
        remark: ''
      };
      this.concentrationRuleDialogTitle = '新增归集规则';
      this.concentrationRuleDialogVisible = true;
    },

    /** 提交归集规则表单 */
    submitConcentrationRuleForm() {
      this.$refs['concentrationRuleForm'].validate(valid => {
        if (!valid) return;
        const isEdit = !!this.concentrationRuleForm.strategyId;
        const api = isEdit ? updateFundConcentrationStrategy : createFundConcentrationStrategy;
        api(this.concentrationRuleForm).then(response => {
          this.$message.success(isEdit ? '修改成功' : '新增成功');
          this.concentrationRuleDialogVisible = false;
          this.getConcentrationRulesList();
        }).catch(err => {
          this.$message.error('操作失败：' + (err.message || err));
        });
      });
    },

    /** 查看规则详情 */
    viewConcentrationRule(row) {
      Object.assign(this.concentrationRuleForm, row);
      this.concentrationRuleDialogTitle = '查看归集规则';
      this.concentrationRuleViewOnly = true;
      this.concentrationRuleDialogVisible = true;
    },

    /** 编辑规则 */
    editConcentrationRule(row) {
      Object.assign(this.concentrationRuleForm, row);
      this.concentrationRuleDialogTitle = '编辑归集规则';
      this.concentrationRuleViewOnly = false;
      this.concentrationRuleDialogVisible = true;
    },

    /** 启用规则 */
    async enableConcentrationRule(row) {
      try {
        const response = await enableConcentrationStrategy(row.strategyId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$message.success('规则启用成功');
          this.getConcentrationRulesList();
        } else {
          this.$message.error(response.message || response.msg || '启用失败');
        }
      } catch (error) {
        console.error('启用规则失败:', error);
        this.$message.error('启用失败，请稍后重试');
      }
    },

    /** 禁用规则 */
    async disableConcentrationRule(row) {
      try {
        const response = await disableConcentrationStrategy(row.strategyId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$message.success('规则禁用成功');
          this.getConcentrationRulesList();
        } else {
          this.$message.error(response.message || response.msg || '禁用失败');
        }
      } catch (error) {
        console.error('禁用规则失败:', error);
        this.$message.error('禁用失败，请稍后重试');
      }
    },

    /** 执行归集规则 */
    async executeConcentrationRule(row) {
      try {
        await this.$confirm('确认立即执行该归集规则？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        const response = await testConcentrationStrategy(row.strategyId);
        if (response.code === 1) {
          this.$message.success('归集规则执行成功：' + (response.data || ''));
          this.getConcentrationRulesList();
        } else {
          this.$message.error(response.msg || response.message || '执行失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('执行归集规则失败:', error);
          this.$message.error('执行失败，请稍后重试');
        }
      }
    },

    /** 删除规则 */
    async deleteConcentrationRule(row) {
      this.$confirm('确认删除该归集规则？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteConcentrationStrategy(row.strategyId);
          const successCodes = [200, 0, '200', '0', '1', 1, 2];
          if (successCodes.includes(response.code)) {
            this.$message.success('删除成功');
            this.getConcentrationRulesList();
          } else {
            this.$message.error(response.message || response.msg || '删除失败');
          }
        } catch (error) {
          console.error('删除规则失败:', error);
          this.$message.error('删除失败，请稍后重试');
        }
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },

    /** 格式化日期时间 */
    formatDateTime(dateTime) {
      if (!dateTime) return '';
      return this.parseTime(dateTime, '{y}-{m}-{d} {h}:{i}:{s}');
    },

    /** 分页相关方法 */
    handleConcentrationCurrentChange(val) {
      this.concentrationQuery.pageNo = val;
      this.getConcentrationRulesList();
    },

    handleConcentrationSizeChange(val) {
      this.concentrationQuery.pageSize = val;
      this.getConcentrationRulesList();
    },

    // ==================== 资金下拨相关方法 ====================

    /** 获取资金下拨列表 */
    async getAllocationsList() {
      this.allocationLoading = true;
      try {
        const response = await getFundAllocationPage(this.allocationQuery);
        // 使用与配置文件一致的成功状态码
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && response.data.tlist !== undefined) {
            // PageInfo格式：{ code: 200, data: { tlist: [], totalRecord: 0 } }
            this.allocationList = response.data.tlist || [];
            this.allocationTotal = parseInt(response.data.totalRecord) || 0;
          } else if (response.data && response.data.records !== undefined) {
            // PageResult格式：{ code: 200, data: { records: [], total: 0 } }
            this.allocationList = response.data.records || [];
            this.allocationTotal = response.data.total || 0;
          } else if (response.data && response.data.list !== undefined) {
            // 标准格式：{ code: 200, data: { list: [], total: 0 } }
            this.allocationList = response.data.list || [];
            this.allocationTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            // 数组格式：{ code: 200, data: [] }
            this.allocationList = response.data || [];
            this.allocationTotal = this.allocationList.length;
          } else {
            // 其他格式，尝试直接使用data
            this.allocationList = response.data || [];
            this.allocationTotal = 0;
          }
        } else {
          this.$message.error(response.message || '查询失败');
          this.allocationList = [];
          this.allocationTotal = 0;
        }
      } catch (error) {
        console.error('获取资金下拨列表失败:', error);
        this.$message.error('获取数据失败，请检查网络连接或稍后重试');
        this.allocationList = [];
        this.allocationTotal = 0;
      } finally {
        this.allocationLoading = false;
      }
    },

    /** 获取资金下拨统计数据 */
    async getAllocationStatisticsData() {
      try {
        const response = await getAllocationStatistics();
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code) && response.data) {
          this.allocationStatistics = {
            totalApplications: response.data.totalApplications || 0,
            pendingApproval: response.data.pendingApproval || 0,
            todayExecutions: response.data.todayExecutions || 0,
            successRate: response.data.successRate || 0
          };
        } else {
          this.allocationStatistics = {
            totalApplications: 0,
            pendingApproval: 0,
            todayExecutions: 0,
            successRate: 0
          };
        }
      } catch (error) {
        console.error('获取资金下拨统计数据失败:', error);
        this.allocationStatistics = {
          totalApplications: 0,
          pendingApproval: 0,
          todayExecutions: 0,
          successRate: 0
        };
      }
    },

    /** 重置查询条件 */
    resetAllocationQuery() {
      this.allocationQuery = {
        current: 1,
        size: 10,
        executionNo: null,
        ruleName: null,
        executionType: null,
        executionStatus: null,
        startDate: null,
        endDate: null
      };
      this.getAllocationsList();
    },

    /** 显示新增申请对话框 */
    showCreateAllocationDialog() {
      this.allocationForm = {};
      this.allocationDialogTitle = '新增资金下拨申请';
      this.allocationDialogVisible = true;
    },

    /** 编辑申请 */
    editAllocation(row) {
      this.allocationForm = { ...row };
      this.allocationDialogTitle = '编辑资金下拨申请';
      this.allocationDialogVisible = true;
    },

    /** 删除申请 */
    async deleteAllocation(row) {
      try {
        await this.$confirm('确认删除该资金下拨申请吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        const response = await deleteFundAllocation(row.allocationId);
        if (response.code === 1) {
          this.$message.success('删除成功');
          this.getAllocationsList();
        } else {
          this.$message.error(response.msg || '删除失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败，请稍后重试');
        }
      }
    },

    /** 提交新增/编辑表单 */
    submitAllocationForm() {
      this.$refs['allocationForm'].validate(async valid => {
        if (!valid) return;
        try {
          const isEdit = !!this.allocationForm.allocationId;
          const api = isEdit ? updateFundAllocation : createFundAllocation;
          const response = await api(this.allocationForm);
          if (response.code === 1) {
            this.$message.success(isEdit ? '修改成功' : '新增成功');
            this.allocationDialogVisible = false;
            this.getAllocationsList();
          } else {
            this.$message.error(response.msg || '操作失败');
          }
        } catch (error) {
          this.$message.error('操作失败，请稍后重试');
        }
      });
    },

    /** 查看申请详情 */
    viewAllocationDetail(row) {
      this.allocationDetailData = { ...row };
      this.allocationDetailDialogVisible = true;
    },

    /** 取消申请 */
    async cancelAllocation(row) {
      try {
        await this.$confirm('确认取消该资金下拨申请吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const response = await cancelFundAllocation(row.allocationId, '用户取消');
        if (response.code === 200 || response.code === 1) {
          this.$message.success('取消成功');
          this.getAllocationsList();
        } else {
          this.$message.error(response.message || '取消失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('取消资金下拨申请失败:', error);
          this.$message.error('取消失败，请稍后重试');
        }
      }
    },

    /** 重试申请 */
    async retryAllocation(row) {
      try {
        await this.$confirm('确认重试该资金下拨申请吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        });

        const response = await retryFundAllocation(row.allocationId);
        if (response.code === 200 || response.code === 1) {
          this.$message.success('重试成功');
          this.getAllocationsList();
        } else {
          this.$message.error(response.message || '重试失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('重试资金下拨申请失败:', error);
          this.$message.error('重试失败，请稍后重试');
        }
      }
    },

    /** 执行申请 */
    async executeAllocation(row) {
      try {
        await this.$confirm('确认执行该资金下拨申请吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const response = await executeFundAllocation(row.allocationId);
        if (response.code === 200 || response.code === 1) {
          this.$message.success('执行成功');
          this.getAllocationsList();
        } else {
          this.$message.error(response.message || '执行失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('执行资金下拨申请失败:', error);
          this.$message.error('执行失败，请稍后重试');
        }
      }
    },

    /** 获取执行类型显示名称 */
    getExecutionTypeDisplay(executionType) {
      const typeMap = {
        'SCHEDULED': '定时执行',
        'MANUAL': '手动执行',
        'TRIGGERED': '触发执行',
        'REQUEST': '申请执行'
      };
      return typeMap[executionType] || executionType;
    },

    /** 获取执行类型标签类型 */
    getExecutionTypeTagType(executionType) {
      const typeMap = {
        'SCHEDULED': 'info',
        'MANUAL': 'primary',
        'TRIGGERED': 'warning',
        'REQUEST': 'success'
      };
      return typeMap[executionType] || '';
    },

    /** 获取执行状态显示名称 */
    getExecutionStatusDisplay(executionStatus) {
      const statusMap = {
        'PROCESSING': '处理中',
        'COMPLETED': '已完成',
        'PARTIAL_SUCCESS': '部分成功',
        'FAILED': '失败',
        'CANCELLED': '已取消'
      };
      return statusMap[executionStatus] || executionStatus;
    },

    /** 获取执行状态标签类型 */
    getExecutionStatusTagType(executionStatus) {
      const statusMap = {
        'PROCESSING': 'warning',
        'COMPLETED': 'success',
        'PARTIAL_SUCCESS': 'info',
        'FAILED': 'danger',
        'CANCELLED': 'info'
      };
      return statusMap[executionStatus] || '';
    },

    /** 检查是否可以重试 */
    canRetryAllocation(row) {
      return row.allocationStatus === 'FAILED';
    },

    /** 检查是否可以执行 */
    canExecuteAllocation(row) {
      return row.allocationStatus === 'PENDING' && row.approvalStatus === 'APPROVED';
    },

    /** 分页大小改变 */
    handleAllocationSizeChange(val) {
      this.allocationQuery.size = val;
      this.getAllocationsList();
    },

    /** 当前页改变 */
    handleAllocationCurrentChange(val) {
      this.allocationQuery.current = val;
      this.getAllocationsList();
    },

    // ==================== 内部借贷相关方法 ====================

    /** 获取内部借贷申请列表 */
    async getLendingList() {
      this.lendingLoading = true;
      try {
        const response = await getInternalLoanPage(this.lendingQuery);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];

        if (successCodes.includes(response.code)) {
          // 支持多种数据格式
          if (response.data && response.data.tlist !== undefined) {
            // PageInfo格式
            this.lendingList = response.data.tlist || [];
            this.lendingTotal = parseInt(response.data.totalRecord) || 0;
          } else if (response.data && response.data.records !== undefined) {
            // PageResult格式
            this.lendingList = response.data.records || [];
            this.lendingTotal = response.data.total || 0;
          } else if (response.data && response.data.list !== undefined) {
            // 标准格式
            this.lendingList = response.data.list || [];
            this.lendingTotal = response.data.total || 0;
          } else if (Array.isArray(response.data)) {
            // 数组格式
            this.lendingList = response.data;
            this.lendingTotal = this.lendingList.length;
          } else {
            // 其他格式，尝试直接使用data
            this.lendingList = response.data || [];
            this.lendingTotal = 0;
          }
        } else {
          this.$message.error(response.message || '查询失败');
          this.lendingList = [];
          this.lendingTotal = 0;
        }
      } catch (error) {
        console.error('获取内部借贷申请列表失败:', error);
        this.$message.error('获取数据失败，请检查网络连接或稍后重试');
        this.lendingList = [];
        this.lendingTotal = 0;
      } finally {
        this.lendingLoading = false;
      }
    },

    /** 获取内部借贷统计数据 */
    async getLendingStatisticsData() {
      try {
        const response = await getLendingStatistics();
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code) && response.data) {
          this.lendingStatistics = {
            totalApplications: response.data.totalApplications || 0,
            pendingApplications: response.data.pendingApplications || 0,
            todayApplications: response.data.todayApplications || 0,
            successRate: response.data.successRate || 0
          };
        } else {
          this.lendingStatistics = {
            totalApplications: 0,
            pendingApplications: 0,
            todayApplications: 0,
            successRate: 0
          };
        }
      } catch (error) {
        console.error('获取内部借贷统计数据失败:', error);
        this.lendingStatistics = {
          totalApplications: 0,
          pendingApplications: 0,
          todayApplications: 0,
          successRate: 0
        };
      }
    },

    /** 重置查询条件 */
    resetLendingQuery() {
      this.lendingQuery = {
        current: 1,
        size: 10,
        loanNo: null,
        loanStatus: null,
        startDate: null,
        endDate: null
      };
      this.getLendingList();
    },

    /** 显示新增申请对话框 */
    showCreateLendingDialog() {
      this.lendingDialogTitle = '新增内部借贷申请';
      this.lendingForm = {
        loanId: null,
        loanName: '',
        borrowAccountName: '',
        lendAccountName: '',
        loanAmount: null,
        currency: 'CNY',
        loanTerm: null,
        interestRate: null,
        repaymentMethod: 'EQUAL_INSTALLMENT',
        loanPurpose: '',
        remark: ''
      };
      this.$nextTick(() => {
        this.$refs.lendingForm && this.$refs.lendingForm.clearValidate();
      });
      this.lendingDialogVisible = true;
    },

    /** 编辑申请 */
    editLendingApplication(row) {
      this.lendingDialogTitle = '修改内部借贷申请';
      this.lendingForm = {
        loanId: row.loanId,
        loanName: row.loanName || '',
        borrowAccountName: row.borrowAccountName || '',
        lendAccountName: row.lendAccountName || '',
        loanAmount: row.loanAmount,
        currency: row.currency || 'CNY',
        loanTerm: row.loanTerm,
        interestRate: row.interestRate,
        repaymentMethod: row.repaymentMethod || 'EQUAL_INSTALLMENT',
        loanPurpose: row.loanPurpose || '',
        remark: row.remark || ''
      };
      this.$nextTick(() => {
        this.$refs.lendingForm && this.$refs.lendingForm.clearValidate();
      });
      this.lendingDialogVisible = true;
    },

    /** 重置表单 */
    resetLendingForm() {
      this.$refs.lendingForm && this.$refs.lendingForm.resetFields();
    },

    /** 提交新增/编辑表单 */
    async submitLendingForm() {
      try {
        await this.$refs.lendingForm.validate();
      } catch (e) {
        return;
      }
      try {
        let response;
        if (this.lendingForm.loanId) {
          response = await updateInternalLoan(this.lendingForm);
        } else {
          response = await createInternalLoan(this.lendingForm);
        }
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$message.success(this.lendingForm.loanId ? '修改成功' : '新增成功');
          this.lendingDialogVisible = false;
          this.getLendingList();
        } else {
          this.$message.error(response.message || response.msg || '操作失败');
        }
      } catch (error) {
        console.error('保存内部借贷申请失败:', error);
        this.$message.error('操作失败，请稍后重试');
      }
    },

    /** 删除申请 */
    async deleteLendingApplication(row) {
      try {
        await this.$confirm('确认删除该借贷申请？删除后不可恢复。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        const response = await deleteInternalLoan(row.loanId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$message.success('删除成功');
          this.getLendingList();
        } else {
          this.$message.error(response.message || response.msg || '删除失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除内部借贷申请失败:', error);
          this.$message.error('删除失败，请稍后重试');
        }
      }
    },

    /** 查看申请详情 */
    viewLendingDetail(row) {
      this.lendingDetailData = { ...row };
      this.lendingDetailDialogVisible = true;
    },

    /** 提交申请 */
    async submitApplication(row) {
      try {
        await this.$confirm('确认提交该借贷申请？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const response = await submitInternalLoan(row.loanId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$message.success('提交成功');
          this.getLendingList();
        } else {
          this.$message.error(response.message || response.msg || '提交失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('提交内部借贷申请失败:', error);
          this.$message.error('提交失败，请稍后重试');
        }
      }
    },

    /** 审批申请 */
    async approveApplication(row) {
      try {
        const { value: comment } = await this.$prompt('请输入审批意见', '审批申请', {
          confirmButtonText: '通过',
          cancelButtonText: '取消',
          inputPlaceholder: '请输入审批意见'
        });

        const response = await approveInternalLoan(row.loanId, { approveResult: 'APPROVED', approveRemark: comment });
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$message.success('审批成功');
          this.getLendingList();
        } else {
          this.$message.error(response.message || response.msg || '审批失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('审批内部借贷申请失败:', error);
          this.$message.error('审批失败，请稍后重试');
        }
      }
    },

    /** 放款 */
    async disburseApplication(row) {
      try {
        await this.$confirm('确认对该申请进行放款？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const response = await disburseLoan(row.loanId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$message.success('放款成功');
          this.getLendingList();
        } else {
          this.$message.error(response.message || response.msg || '放款失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('放款失败:', error);
          this.$message.error('放款失败，请稍后重试');
        }
      }
    },

    /** 取消申请 */
    async cancelApplication(row) {
      try {
        await this.$confirm('确认取消该借贷申请？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const response = await cancelInternalLoan(row.loanId);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          this.$message.success('取消成功');
          this.getLendingList();
        } else {
          this.$message.error(response.message || response.msg || '取消失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('取消内部借贷申请失败:', error);
          this.$message.error('取消失败，请稍后重试');
        }
      }
    },

    // ==================== 内部借贷辅助方法 ====================

    /** 获取申请状态显示名称 */
    getApplicationStatusDisplay(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'SUBMITTED': '已提交',
        'APPROVED': '已批准',
        'REJECTED': '已拒绝',
        'DISBURSED': '已放款',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      };
      return statusMap[status] || status;
    },

    /** 获取申请状态标签类型 */
    getApplicationStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'SUBMITTED': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'DISBURSED': 'primary',
        'COMPLETED': 'success',
        'CANCELLED': 'info'
      };
      return typeMap[status] || '';
    },

    /** 获取风险等级显示名称 */
    getRiskLevelDisplay(level) {
      const levelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      };
      return levelMap[level] || level;
    },

    /** 获取风险等级标签类型 */
    getRiskLevelTagType(level) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      };
      return typeMap[level] || '';
    },

    /** 检查是否可以修改申请（仅草稿状态） */
    canEditLending(row) {
      return row.loanStatus === 'DRAFT';
    },

    /** 检查是否可以删除申请（仅草稿状态） */
    canDeleteLending(row) {
      return row.loanStatus === 'DRAFT';
    },

    /** 检查是否可以提交申请 */
    canSubmitApplication(row) {
      return row.loanStatus === 'DRAFT';
    },

    /** 检查是否可以审批申请 */
    canApproveApplication(row) {
      return row.loanStatus === 'PENDING_APPROVAL';
    },

    /** 检查是否可以放款 */
    canDisburseApplication(row) {
      return row.loanStatus === 'APPROVED';
    },

    /** 检查是否可以取消申请 */
    canCancelApplication(row) {
      return row.loanStatus === 'DRAFT' || row.loanStatus === 'PENDING_APPROVAL';
    },

    /** 格式化日期 */
    formatDate(date) {
      if (!date) return '';
      return new Date(date).toLocaleDateString('zh-CN');
    },

    /** 页大小改变 */
    handleLendingSizeChange(val) {
      this.lendingQuery.size = val;
      this.getLendingList();
    },

    /** 当前页改变 */
    handleLendingCurrentChange(val) {
      this.lendingQuery.current = val;
      this.getLendingList();
    },

    // ==================== 资金监控相关方法 ====================

    /**
     * 获取监控仪表盘数据
     */
    async getMonitoringDashboard() {
      try {
        const response = await getFundMonitoringDashboard();
        const successCodes = [200, 0, '200', '0', '1', 1, 2];

        if (successCodes.includes(response.code) && response.data) {
          const d = response.data;
          const poolStats = d.fundPoolStats || {};
          const execStats = d.executionStats || {};
          const totalBalance = Number(poolStats.totalBalance) || 0;
          const availableBalance = Number(poolStats.availableBalance) || 0;
          const utilizationRate = totalBalance > 0
            ? parseFloat(((totalBalance - availableBalance) / totalBalance * 100).toFixed(2))
            : 0;
          this.monitoringDashboard = {
            totalBalance,
            availableBalance,
            todayNetflow: Number(d.todayAmount) || 0,
            monthAmount: Number(d.monthAmount) || 0,
            activePools: Number(poolStats.activeCount) || 0,
            totalPools: Number(poolStats.totalCount) || 0,
            pendingAlerts: Number(d.alertCount) || 0,
            highRiskAlerts: 0,
            fundUtilizationRate: utilizationRate,
            liquidityRatio: totalBalance > 0
              ? parseFloat((availableBalance / totalBalance * 100).toFixed(2))
              : 0,
            runningConcentrationTasks: Number(execStats.runningTasks) || 0,
            totalTasks: Number(execStats.totalTasks) || 0,
            successTasks: Number(execStats.successTasks) || 0,
            failedTasks: Number(execStats.failedTasks) || 0,
            totalExecAmount: Number(execStats.totalAmount) || 0,
            systemStatus: 'NORMAL',
            riskRating: 'LOW',
            complianceStatus: 'COMPLIANT'
          };
          this.getBalanceMonitoring();
        } else {
          this.$message.error(response.msg || response.message || '获取监控数据失败');
        }
      } catch (error) {
        console.error('获取监控仪表盘数据失败:', error);
        this.$message.error('获取监控数据失败，请检查网络连接或稍后重试');
      }
    },

    /**
     * 获取余额监控数据
     */
    async getBalanceMonitoring() {
      try {
        const response = await getBalanceMonitoring();
        const successCodes = [200, 0, '200', '0', '1', 1, 2];

        if (successCodes.includes(response.code) && response.data) {
          const d = response.data;
          if (Array.isArray(d.bankBalances) && d.bankBalances.length > 0) {
            this.bankBalances = d.bankBalances;
          } else {
            this.bankBalances = [
              { bankName: '资金池总余额', balance: Number(d.totalBalance) || 0, status: 'NORMAL' },
              { bankName: '可用余额', balance: Number(d.availableBalance) || 0, status: 'NORMAL' }
            ];
          }
        } else {
          this.bankBalances = [];
        }
      } catch (error) {
        console.error('获取余额监控数据失败:', error);
        this.bankBalances = [];
      }
    },

    /**
     * 获取预警列表
     */
    async getAlertsList() {
      this.alertsLoading = true;
      try {
        const params = { pageNo: this.alertQuery.current, pageSize: this.alertQuery.size };
        if (this.alertQuery.alertType) params.alertType = this.alertQuery.alertType;
        if (this.alertQuery.alertLevel) params.alertLevel = this.alertQuery.alertLevel;
        if (this.alertQuery.alertStatus) params.alertStatus = this.alertQuery.alertStatus;
        const response = await getAlertList(params);
        const successCodes = [200, 0, '200', '0', '1', 1, 2];

        if (successCodes.includes(response.code)) {
          if (response.data && response.data.tlist !== undefined) {
            this.alertsList = response.data.tlist || [];
            this.alertsTotal = parseInt(response.data.totalRecord) || 0;
          } else if (response.data && response.data.records !== undefined) {
            this.alertsList = response.data.records || [];
            this.alertsTotal = response.data.total || 0;
          } else if (Array.isArray(response.data)) {
            this.alertsList = response.data;
            this.alertsTotal = response.data.length;
          } else {
            this.alertsList = [];
            this.alertsTotal = 0;
          }

          const list = this.alertsList;
          const resolved = list.filter(a => a.alertStatus === 'RESOLVED').length;
          const pending = list.filter(a => a.alertStatus === 'PENDING').length;
          this.alertStatistics = {
            totalAlerts: this.alertsTotal || list.length,
            pendingAlerts: pending,
            processingAlerts: list.filter(a => a.alertStatus === 'PROCESSING').length,
            resolvedAlerts: resolved,
            ignoredAlerts: list.filter(a => a.alertStatus === 'IGNORED').length,
            resolutionRate: list.length > 0 ? parseFloat((resolved / list.length * 100).toFixed(1)) : 0
          };
        } else {
          this.$message.error(response.message || '查询预警失败');
          this.alertsList = [];
          this.alertsTotal = 0;
        }
      } catch (error) {
        console.error('获取预警列表失败:', error);
        this.$message.error('获取预警数据失败，请检查网络连接或稍后重试');
        this.alertsList = [];
        this.alertsTotal = 0;
      } finally {
        this.alertsLoading = false;
      }
    },

    /**
     * 获取预警统计数据
     */
    async getAlertStatisticsData() {
      try {
        const response = await getAlertStatistics();
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code) && response.data) {
          this.alertStatistics = {
            totalAlerts: response.data.totalAlerts || 0,
            pendingAlerts: response.data.pendingAlerts || 0,
            processingAlerts: response.data.processingAlerts || 0,
            resolvedAlerts: response.data.resolvedAlerts || 0,
            ignoredAlerts: response.data.ignoredAlerts || 0,
            resolutionRate: response.data.resolutionRate || 0
          };
        } else {
          this.alertStatistics = {
            totalAlerts: 0,
            pendingAlerts: 0,
            processingAlerts: 0,
            resolvedAlerts: 0,
            ignoredAlerts: 0,
            resolutionRate: 0
          };
        }
      } catch (error) {
        console.error('获取预警统计数据失败:', error);
        this.alertStatistics = {
          totalAlerts: 0,
          pendingAlerts: 0,
          processingAlerts: 0,
          resolvedAlerts: 0,
          ignoredAlerts: 0,
          resolutionRate: 0
        };
      }
    },

    /**
     * 重置预警查询条件
     */
    resetAlertQuery() {
      this.alertQuery = {
        current: 1,
        size: 10,
        alertType: '',
        alertLevel: '',
        alertStatus: ''
      };
      this.getAlertsList();
    },

    /**
     * 监控标签页切换
     */
    handleMonitoringTabClick(tab) {
      if (tab.name === 'realtime') {
        this.getMonitoringDashboard();
      } else if (tab.name === 'alerts') {
        this.getAlertsList();
      } else if (tab.name === 'reports') {
        this.getAlertStatistics();
      }
    },

    /**
     * 查看预警详情
     */
    viewAlertDetail(alert) {
      this.currentAlert = alert;
      this.alertDetailDialogVisible = true;
    },

    /**
     * 处理预警
     */
    handleAlert(alert) {
      this.currentAlert = alert;
      this.handleForm = {
        action: '',
        comment: ''
      };
      this.alertHandleDialogVisible = true;
    },

    /**
     * 解决预警
     */
    async resolveAlert(alert) {
      try {
        await handleAlert(alert.alertId, {
          action: 'resolve',
          comment: '预警已解决'
        });
        this.$message.success('预警解决成功');
        this.getAlertsList();
      } catch (error) {
        console.error('解决预警失败:', error);
        this.$message.error('解决预警失败');
      }
    },

    /**
     * 检查是否可以处理预警
     */
    canHandleAlert(alert) {
      return alert.alertStatus === 'PENDING';
    },

    /**
     * 检查是否可以解决预警
     */
    canResolveAlert(alert) {
      return alert.alertStatus === 'PROCESSING';
    },

    /**
     * 获取资金池健康度显示
     */
    getPoolHealthDisplay() {
      const rate = this.monitoringDashboard.activePools / this.monitoringDashboard.totalPools;
      if (rate >= 0.9) return '优秀';
      if (rate >= 0.7) return '良好';
      if (rate >= 0.5) return '一般';
      return '较差';
    },

    /**
     * 获取银行状态显示
     */
    getBankStatusDisplay(status) {
      const statusMap = {
        'NORMAL': '正常',
        'WARNING': '警告',
        'LOW': '偏低',
        'HIGH': '偏高'
      };
      return statusMap[status] || status;
    },

    /**
     * 获取银行状态标签类型
     */
    getBankStatusType(status) {
      const typeMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'LOW': 'danger',
        'HIGH': 'info'
      };
      return typeMap[status] || '';
    },

    /**
     * 获取预警类型显示
     */
    getAlertTypeDisplay(type) {
      const typeMap = {
        'BALANCE': '余额预警',
        'LIQUIDITY': '流动性预警',
        'RISK': '风险预警',
        'COMPLIANCE': '合规预警'
      };
      return typeMap[type] || type;
    },

    /**
     * 获取预警类型标签类型
     */
    getAlertTypeTagType(type) {
      const typeMap = {
        'BALANCE': 'primary',
        'LIQUIDITY': 'success',
        'RISK': 'danger',
        'COMPLIANCE': 'warning'
      };
      return typeMap[type] || '';
    },

    /**
     * 获取预警级别显示
     */
    getAlertLevelDisplay(level) {
      const levelMap = {
        'LOW': '低级',
        'MEDIUM': '中级',
        'HIGH': '高级',
        'CRITICAL': '紧急'
      };
      return levelMap[level] || level;
    },

    /**
     * 获取预警级别标签类型
     */
    getAlertLevelTagType(level) {
      const typeMap = {
        'LOW': 'info',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      };
      return typeMap[level] || '';
    },

    /**
     * 获取预警状态显示
     */
    getAlertStatusDisplay(status) {
      const statusMap = {
        'PENDING': '待处理',
        'PROCESSING': '处理中',
        'RESOLVED': '已解决',
        'IGNORED': '已忽略'
      };
      return statusMap[status] || status;
    },

    /**
     * 获取预警状态标签类型
     */
    getAlertStatusTagType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'PROCESSING': 'primary',
        'RESOLVED': 'success',
        'IGNORED': 'info'
      };
      return typeMap[status] || '';
    },

    /**
     * 获取系统状态显示
     */
    getSystemStatusDisplay(status) {
      const statusMap = {
        'NORMAL': '正常',
        'WARNING': '警告',
        'ERROR': '错误'
      };
      return statusMap[status] || status;
    },

    /**
     * 获取系统状态标签类型
     */
    getSystemStatusType(status) {
      const typeMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'ERROR': 'danger'
      };
      return typeMap[status] || '';
    },

    /**
     * 获取风险评级显示
     */
    getRiskRatingDisplay(rating) {
      const ratingMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      };
      return ratingMap[rating] || rating;
    },

    /**
     * 获取风险评级标签类型
     */
    getRiskRatingType(rating) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      };
      return typeMap[rating] || '';
    },

    /**
     * 获取合规状态显示
     */
    getComplianceStatusDisplay(status) {
      const statusMap = {
        'COMPLIANT': '合规',
        'WARNING': '警告',
        'VIOLATION': '违规'
      };
      return statusMap[status] || status;
    },

    /**
     * 获取合规状态标签类型
     */
    getComplianceStatusType(status) {
      const typeMap = {
        'COMPLIANT': 'success',
        'WARNING': 'warning',
        'VIOLATION': 'danger'
      };
      return typeMap[status] || '';
    },

    /**
     * 预警分页大小改变
     */
    handleAlertsSizeChange(val) {
      this.alertQuery.size = val;
      this.getAlertsList();
    },

    /**
     * 预警当前页改变
     */
    handleAlertsCurrentChange(val) {
      this.alertQuery.current = val;
      this.getAlertsList();
    }
  }
}

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

/* 资金归集页面样式 */
.fund-concentration-container {
  padding: 20px;
}

.statistics-cards {
  margin-bottom: 20px;
}

.statistics-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-content {
  display: flex;
  align-items: center;
  padding: 10px;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.card-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-icon.active {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.card-icon.today {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.card-icon.success {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.card-info {
  flex: 1;
}

.card-title {
  font-size: 14px;
  color: #999;
  margin-bottom: 5px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.query-form {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.table-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

/* 查询表单保持原样 */
.query-form {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

/* 表格容器宽度与查询表单内容宽度一致 */
.table-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

/* 让表格宽度与查询表单的实际内容宽度一致 */
.fund-concentration-container .el-table {
  width: fit-content;
  max-width: 100%;
}

/* 如果需要固定宽度，可以设置具体数值 */
.fund-concentration-container .table-container .el-table {
  width: 100%;
}

.pagination-container {
  text-align: center;
  padding: 20px;
}

/* 内部借贷页面样式 */
.internal-lending-container {
  padding: 20px;
}

.internal-lending-container .statistics-cards {
  margin-bottom: 20px;
}

.internal-lending-container .stat-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
}

.internal-lending-container .stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
}

.internal-lending-container .stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
}

.internal-lending-container .stat-icon i {
  font-size: 28px;
}

.internal-lending-container .total-applications .stat-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.internal-lending-container .pending-approval .stat-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.internal-lending-container .today-applications .stat-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.internal-lending-container .success-rate .stat-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.internal-lending-container .stat-content {
  flex: 1;
}

.internal-lending-container .stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.internal-lending-container .stat-label {
  font-size: 14px;
  color: #666;
}

.internal-lending-container .query-form {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.internal-lending-container .data-table {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.internal-lending-container .pagination-container {
  text-align: center;
  padding: 20px;
}

/* 资金监控样式 */
.fund-monitoring-container {
  .monitoring-dashboard {
    margin-bottom: 20px;

    .dashboard-cards {
      .dashboard-card {
        background: white;
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
        }

        .card-header {
          display: flex;
          align-items: center;
          margin-bottom: 15px;

          i {
            font-size: 20px;
            margin-right: 8px;
            color: #409eff;
          }

          span {
            font-size: 14px;
            color: #666;
            font-weight: 500;
          }
        }

        .card-value {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          margin-bottom: 8px;
        }

        .card-change {
          font-size: 12px;

          &.positive {
            color: #67c23a;
          }

          &.negative {
            color: #f56c6c;
          }

          i {
            margin-right: 4px;
          }
        }

        .card-ratio {
          font-size: 12px;
          color: #909399;
        }
      }

      .balance-card .card-header i {
        color: #67c23a;
      }

      .available-card .card-header i {
        color: #409eff;
      }

      .pool-card .card-header i {
        color: #e6a23c;
      }

      .alert-card .card-header i {
        color: #f56c6c;
      }
    }
  }

  .monitoring-content {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .realtime-monitoring {
      .trend-card,
      .balance-card {
        .el-card__header {
          background: #f8f9fa;
          border-bottom: 1px solid #ebeef5;
          font-weight: 500;
        }

        .trend-chart,
        .balance-distribution {
          min-height: 300px;

          .trend-stats {
            padding: 10px 0;

            .trend-stat-item {
              padding: 16px 12px;
              margin-bottom: 12px;
              background: #f8f9fa;
              border-radius: 6px;
              text-align: center;

              .trend-stat-label {
                font-size: 12px;
                color: #909399;
                margin-bottom: 8px;
              }

              .trend-stat-value {
                font-size: 18px;
                font-weight: 600;
                color: #303133;

                &.positive { color: #67c23a; }
                &.negative { color: #f56c6c; }
              }
            }
          }

          .chart-placeholder {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 300px;
            color: #909399;

            i {
              font-size: 48px;
              margin-bottom: 10px;
            }

            p {
              font-size: 14px;
              margin: 0;
            }
          }

          .balance-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 0;
            border-bottom: 1px solid #f0f0f0;

            &:last-child {
              border-bottom: none;
            }

            .bank-info {
              display: flex;
              align-items: center;

              .bank-name {
                margin-right: 10px;
                font-weight: 500;
              }
            }

            .bank-balance {
              font-weight: bold;
              color: #303133;
            }
          }
        }
      }
    }

    .alerts-management {
      .query-form {
        background: #f8f9fa;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 20px;

        .el-form-item {
          margin-bottom: 0;
        }
      }

      .alerts-table {
        .el-table {
          border-radius: 8px;
          overflow: hidden;
        }
      }

      .pagination-container {
        margin-top: 20px;
        text-align: right;
      }
    }

    .reports-section {
      .el-card {
        .el-card__header {
          background: #f8f9fa;
          border-bottom: 1px solid #ebeef5;
          font-weight: 500;
        }

        .stats-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 8px 0;
          border-bottom: 1px solid #f0f0f0;

          &:last-child {
            border-bottom: none;
          }

          .stats-label {
            color: #666;
            font-size: 14px;
          }

          .stats-value {
            font-weight: bold;
            color: #303133;
          }
        }
      }
    }
  }
}
</style>
