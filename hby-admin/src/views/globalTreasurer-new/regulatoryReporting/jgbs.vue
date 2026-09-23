<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 监管机构管理 -->
      <el-tab-pane label="监管机构管理" name="authority">
        <div class="authority-container">
          <!-- 查询条件 -->
          <el-form :model="authorityQueryParams" ref="authorityQueryForm" size="small" :inline="true" v-show="authorityShowSearch" label-width="68px">
            <el-form-item label="机构编码" prop="authorityCode">
              <el-input
                v-model="authorityQueryParams.authorityCode"
                placeholder="请输入机构编码"
                clearable
                @keyup.enter.native="handleAuthorityQuery"
              />
            </el-form-item>
            <el-form-item label="机构名称" prop="authorityName">
              <el-input
                v-model="authorityQueryParams.authorityName"
                placeholder="请输入机构名称"
                clearable
                @keyup.enter.native="handleAuthorityQuery"
              />
            </el-form-item>
            <el-form-item label="机构类型" prop="authorityType">
              <el-select v-model="authorityQueryParams.authorityType" placeholder="请选择机构类型" clearable>
                <el-option label="央行" value="CENTRAL_BANK" />
                <el-option label="证监会" value="SECURITIES_COMMISSION" />
                <el-option label="银保监会" value="BANKING_REGULATOR" />
                <el-option label="外汇局" value="FOREX_REGULATOR" />
                <el-option label="税务局" value="TAX_AUTHORITY" />
              </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="isActive">
              <el-select v-model="authorityQueryParams.isActive" placeholder="请选择状态" clearable>
                <el-option label="启用" value="1" />
                <el-option label="停用" value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleAuthorityQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetAuthorityQuery">重置</el-button>
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
                @click="handleAuthorityAdd"
                v-hasPermi="['globalTreasurer:regulatory:authority:add']"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="authoritySingle"
                @click="handleAuthorityUpdate"
                v-hasPermi="['globalTreasurer:regulatory:authority:edit']"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="authorityMultiple"
                @click="handleAuthorityDelete"
                v-hasPermi="['globalTreasurer:regulatory:authority:remove']"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleAuthorityExport"
                v-hasPermi="['globalTreasurer:regulatory:authority:export']"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="authorityShowSearch" @queryTable="getAuthorityList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="authorityLoading" :data="authorityList" @selection-change="handleAuthoritySelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="机构编码" align="center" prop="authorityCode" />
            <el-table-column label="机构名称" align="center" prop="authorityName" />
            <el-table-column label="英文名称" align="center" prop="authorityNameEng" />
            <el-table-column label="机构类型" align="center" prop="authorityType">
              <template slot-scope="scope">
                <span>{{ getAuthorityTypeLabel(scope.row.authorityType) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="国家代码" align="center" prop="countryCode" />
            <el-table-column label="联系人" align="center" prop="contactPerson" />
            <el-table-column label="联系电话" align="center" prop="contactPhone" />
            <el-table-column label="状态" align="center" prop="isActive">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.isActive"
                  :active-value="1"
                  :inactive-value="0"
                  @change="handleAuthorityStatusChange(scope.row)"
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleAuthorityUpdate(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:authority:edit']"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleAuthorityDelete(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:authority:remove']"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="authorityTotal>0"
            :total="authorityTotal"
            :page.sync="authorityQueryParams.pageNum"
            :limit.sync="authorityQueryParams.pageSize"
            @pagination="getAuthorityList"
          />
        </div>
      </el-tab-pane>

      <!-- 报告模板管理 -->
      <el-tab-pane label="报告模板管理" name="template">
        <div class="template-container">
          <!-- 查询条件 -->
          <el-form :model="templateQueryParams" ref="templateQueryForm" size="small" :inline="true" v-show="templateShowSearch" label-width="68px">
            <el-form-item label="模板编码" prop="templateCode">
              <el-input
                v-model="templateQueryParams.templateCode"
                placeholder="请输入模板编码"
                clearable
                @keyup.enter.native="handleTemplateQuery"
              />
            </el-form-item>
            <el-form-item label="模板名称" prop="templateName">
              <el-input
                v-model="templateQueryParams.templateName"
                placeholder="请输入模板名称"
                clearable
                @keyup.enter.native="handleTemplateQuery"
              />
            </el-form-item>
            <el-form-item label="监管机构" prop="authorityId">
              <el-select v-model="templateQueryParams.authorityId" placeholder="请选择监管机构" clearable>
                <el-option
                  v-for="authority in authorityOptions"
                  :key="authority.authorityId"
                  :label="authority.authorityName"
                  :value="authority.authorityId"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="isEnabled">
              <el-select v-model="templateQueryParams.isEnabled" placeholder="请选择状态" clearable>
                <el-option label="启用" value="1" />
                <el-option label="停用" value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleTemplateQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetTemplateQuery">重置</el-button>
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
                @click="handleTemplateAdd"
                v-hasPermi="['globalTreasurer:regulatory:template:add']"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="templateSingle"
                @click="handleTemplateUpdate"
                v-hasPermi="['globalTreasurer:regulatory:template:edit']"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="templateMultiple"
                @click="handleTemplateDelete"
                v-hasPermi="['globalTreasurer:regulatory:template:remove']"
              >删除</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="templateShowSearch" @queryTable="getTemplateList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="templateLoading" :data="templateList" @selection-change="handleTemplateSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="模板编码" align="center" prop="templateCode" />
            <el-table-column label="模板名称" align="center" prop="templateName" />
            <el-table-column label="监管机构" align="center" prop="authorityName" />
            <el-table-column label="报告频率" align="center" prop="reportFrequency">
              <template slot-scope="scope">
                <span>{{ getReportFrequencyLabel(scope.row.reportFrequency) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="版本" align="center" prop="templateVersion" />
            <el-table-column label="状态" align="center" prop="isEnabled">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.isEnabled"
                  :active-value="1"
                  :inactive-value="0"
                  @change="handleTemplateStatusChange(scope.row)"
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleTemplateUpdate(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:template:edit']"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-copy-document"
                  @click="handleTemplateCopy(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:template:add']"
                >复制</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleTemplateDelete(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:template:remove']"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="templateTotal>0"
            :total="templateTotal"
            :page.sync="templateQueryParams.pageNum"
            :limit.sync="templateQueryParams.pageSize"
            @pagination="getTemplateList"
          />
        </div>
      </el-tab-pane>

      <!-- 监管报告管理 -->
      <el-tab-pane label="监管报告管理" name="report">
        <div class="report-container">
          <!-- 统计卡片 -->
          <el-row :gutter="20" class="mb20">
            <el-col :span="6">
              <div class="stat-card">
                <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                  <i class="el-icon-document"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-number">{{ reportStatistics.totalReports || 0 }}</div>
                  <div class="stat-label">总报告数</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card">
                <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                  <i class="el-icon-warning"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-number">{{ reportStatistics.overdueReports || 0 }}</div>
                  <div class="stat-label">逾期报告</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card">
                <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                  <i class="el-icon-clock"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-number">{{ reportStatistics.dueSoonReports || 0 }}</div>
                  <div class="stat-label">即将到期</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card">
                <div class="stat-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                  <i class="el-icon-check"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-number">{{ reportStatistics.acceptedReports || 0 }}</div>
                  <div class="stat-label">已完成</div>
                </div>
              </div>
            </el-col>
          </el-row>

          <!-- 查询条件 -->
          <el-form :model="reportQueryParams" ref="reportQueryForm" size="small" :inline="true" v-show="reportShowSearch" label-width="68px">
            <el-form-item label="报告编号" prop="reportNo">
              <el-input
                v-model="reportQueryParams.reportNo"
                placeholder="请输入报告编号"
                clearable
                @keyup.enter.native="handleReportQuery"
              />
            </el-form-item>
            <el-form-item label="报告名称" prop="reportName">
              <el-input
                v-model="reportQueryParams.reportName"
                placeholder="请输入报告名称"
                clearable
                @keyup.enter.native="handleReportQuery"
              />
            </el-form-item>
            <el-form-item label="监管机构" prop="authorityId">
              <el-select v-model="reportQueryParams.authorityId" placeholder="请选择监管机构" clearable>
                <el-option
                  v-for="authority in authorityOptions"
                  :key="authority.authorityId"
                  :label="authority.authorityName"
                  :value="authority.authorityId"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="报告状态" prop="reportStatus">
              <el-select v-model="reportQueryParams.reportStatus" placeholder="请选择报告状态" clearable>
                <el-option label="草稿" value="DRAFT" />
                <el-option label="已生成" value="GENERATED" />
                <el-option label="已验证" value="VALIDATED" />
                <el-option label="已提交" value="SUBMITTED" />
                <el-option label="已接受" value="ACCEPTED" />
                <el-option label="已拒绝" value="REJECTED" />
              </el-select>
            </el-form-item>
            <el-form-item label="报告期间" prop="reportPeriod">
              <el-input
                v-model="reportQueryParams.reportPeriod"
                placeholder="请输入报告期间"
                clearable
                @keyup.enter.native="handleReportQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleReportQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetReportQuery">重置</el-button>
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
                @click="handleReportAdd"
                v-hasPermi="['globalTreasurer:regulatory:report:add']"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="reportSingle"
                @click="handleReportUpdate"
                v-hasPermi="['globalTreasurer:regulatory:report:edit']"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="reportMultiple"
                @click="handleReportDelete"
                v-hasPermi="['globalTreasurer:regulatory:report:remove']"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleReportExport"
                v-hasPermi="['globalTreasurer:regulatory:report:export']"
              >导出</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-s-operation"
                size="mini"
                :disabled="reportMultiple"
                @click="handleBatchGenerate"
                v-hasPermi="['globalTreasurer:regulatory:report:generate']"
              >批量生成</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="reportShowSearch" @queryTable="getReportList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="reportLoading" :data="reportList" @selection-change="handleReportSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="报告编号" align="center" prop="reportNo" width="140" />
            <el-table-column label="报告名称" align="center" prop="reportName" :show-overflow-tooltip="true" />
            <el-table-column label="监管机构" align="center" prop="authorityName" width="120" />
            <el-table-column label="报告期间" align="center" prop="reportPeriod" width="100" />
            <el-table-column label="截止日期" align="center" prop="dueDate" width="100">
              <template slot-scope="scope">
                <span :class="{'text-danger': scope.row.isOverdue}">
                  {{ parseTime(scope.row.dueDate, '{y}-{m}-{d}') }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="报告状态" align="center" prop="reportStatus" width="100">
              <template slot-scope="scope">
                <el-tag :type="getReportStatusTagType(scope.row.reportStatus)">
                  {{ getReportStatusDisplay(scope.row.reportStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="验证状态" align="center" prop="reportStatus" width="100">
              <template slot-scope="scope">
                <el-tag :type="getValidationStatusTagType(scope.row.reportStatus)">
                  {{ getValidationStatusDisplay(scope.row.reportStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="进度" align="center" prop="reportStatus" width="120">
              <template slot-scope="scope">
                <el-progress :percentage="getProgressByStatus(scope.row.reportStatus)" :stroke-width="6" :show-text="false"></el-progress>
                <span class="progress-text">{{ getProgressByStatus(scope.row.reportStatus) }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createdTime" width="160">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleReportView(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:report:query']"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-s-operation"
                  @click="handleReportGenerate(scope.row)"
                  v-if="canGenerate(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:report:generate']"
                >生成</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-check"
                  @click="handleReportValidate(scope.row)"
                  v-if="canValidate(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:report:validate']"
                >验证</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-upload"
                  @click="handleReportSubmit(scope.row)"
                  v-if="canSubmit(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:report:submit']"
                >提交</el-button>
                <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['globalTreasurer:regulatory:report:edit']">
                  <span class="el-dropdown-link">
                    更多<i class="el-icon-arrow-down el-icon--right"></i>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="edit" icon="el-icon-edit">修改</el-dropdown-item>
                    <el-dropdown-item command="copy" icon="el-icon-copy-document">复制</el-dropdown-item>
                    <el-dropdown-item command="download" icon="el-icon-download">下载</el-dropdown-item>
                    <el-dropdown-item command="delete" icon="el-icon-delete">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="reportTotal>0"
            :total="reportTotal"
            :page.sync="reportQueryParams.pageNum"
            :limit.sync="reportQueryParams.pageSize"
            @pagination="getReportList"
          />
        </div>
      </el-tab-pane>

      <!-- 合规检查管理 -->
      <el-tab-pane label="合规检查管理" name="compliance">
        <div class="compliance-container">
          <!-- 统计卡片 -->
          <el-row :gutter="20" class="mb20">
            <el-col :span="6">
              <div class="stat-card" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                <div class="stat-icon" style="background: rgba(255,255,255,0.2);">
                  <i class="el-icon-document-checked" style="color: #fff;"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-number" style="color: #fff;">{{ complianceStatistics.totalRules || 45 }}</div>
                  <div class="stat-label" style="color: rgba(255,255,255,0.8);">总规则数</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                <div class="stat-icon" style="background: rgba(255,255,255,0.2);">
                  <i class="el-icon-warning" style="color: #fff;"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-number" style="color: #fff;">{{ complianceStatistics.failedResults || 15 }}</div>
                  <div class="stat-label" style="color: rgba(255,255,255,0.8);">违规检查</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                <div class="stat-icon" style="background: rgba(255,255,255,0.2);">
                  <i class="el-icon-circle-check" style="color: #fff;"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-number" style="color: #fff;">{{ complianceStatistics.passedResults || 189 }}</div>
                  <div class="stat-label" style="color: rgba(255,255,255,0.8);">通过检查</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                <div class="stat-icon" style="background: rgba(255,255,255,0.2);">
                  <i class="el-icon-data-line" style="color: #fff;"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-number" style="color: #fff;">{{ complianceStatistics.complianceScore || 92.5 }}%</div>
                  <div class="stat-label" style="color: rgba(255,255,255,0.8);">合规评分</div>
                </div>
              </div>
            </el-col>
          </el-row>

          <!-- 合规检查规则管理 -->
          <el-card class="mb20">
            <div slot="header" class="clearfix">
              <span style="font-weight: bold; color: #303133;">合规检查规则</span>
            </div>

            <!-- 查询表单 -->
            <el-form :model="ruleQueryParams" ref="ruleQueryForm" size="small" :inline="true" v-show="ruleShowSearch" label-width="68px">
              <el-form-item label="规则编码" prop="ruleCode">
                <el-input
                  v-model="ruleQueryParams.ruleCode"
                  placeholder="请输入规则编码"
                  clearable
                  @keyup.enter.native="handleRuleQuery"
                />
              </el-form-item>
              <el-form-item label="规则名称" prop="ruleName">
                <el-input
                  v-model="ruleQueryParams.ruleName"
                  placeholder="请输入规则名称"
                  clearable
                  @keyup.enter.native="handleRuleQuery"
                />
              </el-form-item>
              <el-form-item label="规则类型" prop="ruleType">
                <el-select v-model="ruleQueryParams.ruleType" placeholder="请选择规则类型" clearable>
                  <el-option label="限额检查" value="LIMIT_CHECK" />
                  <el-option label="比率检查" value="RATIO_CHECK" />
                  <el-option label="阈值检查" value="THRESHOLD_CHECK" />
                  <el-option label="业务规则" value="BUSINESS_RULE" />
                  <el-option label="数据质量" value="DATA_QUALITY" />
                </el-select>
              </el-form-item>
              <el-form-item label="严重程度" prop="severityLevel">
                <el-select v-model="ruleQueryParams.severityLevel" placeholder="请选择严重程度" clearable>
                  <el-option label="低" value="LOW" />
                  <el-option label="中" value="MEDIUM" />
                  <el-option label="高" value="HIGH" />
                  <el-option label="严重" value="CRITICAL" />
                </el-select>
              </el-form-item>
              <el-form-item label="状态" prop="isEnabled">
                <el-select v-model="ruleQueryParams.isEnabled" placeholder="请选择状态" clearable>
                  <el-option label="启用" value="1" />
                  <el-option label="停用" value="0" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleRuleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetRuleQuery">重置</el-button>
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
                  @click="handleRuleAdd"
                  v-hasPermi="['globalTreasurer:regulatory:rule:add']"
                >新增</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="success"
                  plain
                  icon="el-icon-edit"
                  size="mini"
                  :disabled="ruleSingle"
                  @click="handleRuleUpdate"
                  v-hasPermi="['globalTreasurer:regulatory:rule:edit']"
                >修改</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="danger"
                  plain
                  icon="el-icon-delete"
                  size="mini"
                  :disabled="ruleMultiple"
                  @click="handleRuleDelete"
                  v-hasPermi="['globalTreasurer:regulatory:rule:remove']"
                >删除</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="warning"
                  plain
                  icon="el-icon-download"
                  size="mini"
                  @click="handleRuleExport"
                  v-hasPermi="['globalTreasurer:regulatory:rule:export']"
                >导出</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="info"
                  plain
                  icon="el-icon-video-play"
                  size="mini"
                  :disabled="ruleMultiple"
                  @click="handleBatchExecuteRules"
                  v-hasPermi="['globalTreasurer:regulatory:rule:execute']"
                >批量执行</el-button>
              </el-col>
              <right-toolbar :showSearch.sync="ruleShowSearch" @queryTable="getRuleList"></right-toolbar>
            </el-row>

            <!-- 规则列表 -->
            <el-table v-loading="ruleLoading" :data="ruleList" @selection-change="handleRuleSelectionChange">
              <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="规则编码" align="center" prop="ruleCode" />
              <el-table-column label="规则名称" align="center" prop="ruleName" show-overflow-tooltip />
              <el-table-column label="规则类型" align="center" prop="ruleType">
                <template slot-scope="scope">
                  <el-tag :type="getRuleTypeTagType(scope.row.ruleType)">{{ getRuleTypeDisplay(scope.row.ruleType) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="严重程度" align="center" prop="severityLevel">
                <template slot-scope="scope">
                  <el-tag :type="getSeverityTagType(scope.row.severityLevel)">{{ getSeverityDisplay(scope.row.severityLevel) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="检查频率" align="center" prop="checkFrequency">
                <template slot-scope="scope">
                  {{ getFrequencyDisplay(scope.row.checkFrequency) }}
                </template>
              </el-table-column>
              <el-table-column label="阈值" align="center" prop="thresholdValue" />
              <el-table-column label="状态" align="center" prop="isEnabled">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.isEnabled"
                    :active-value="1"
                    :inactive-value="0"
                    @change="handleRuleStatusChange(scope.row)"
                  ></el-switch>
                </template>
              </el-table-column>
              <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-view"
                    @click="handleRuleView(scope.row)"
                    v-hasPermi="['globalTreasurer:regulatory:rule:query']"
                  >查看</el-button>
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handleRuleUpdate(scope.row)"
                    v-hasPermi="['globalTreasurer:regulatory:rule:edit']"
                  >修改</el-button>
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-video-play"
                    @click="handleExecuteRule(scope.row)"
                    v-hasPermi="['globalTreasurer:regulatory:rule:execute']"
                  >执行</el-button>
                  <el-dropdown size="mini" @command="(command) => handleRuleCommand(command, scope.row)" style="margin-left: 10px">
                    <span class="el-dropdown-link">
                      更多<i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                      <el-dropdown-item command="test" icon="el-icon-cpu">测试</el-dropdown-item>
                      <el-dropdown-item command="history" icon="el-icon-time">历史</el-dropdown-item>
                      <el-dropdown-item command="delete" icon="el-icon-delete">删除</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <pagination
              v-show="ruleTotal>0"
              :total="ruleTotal"
              :page.sync="ruleQueryParams.pageNum"
              :limit.sync="ruleQueryParams.pageSize"
              @pagination="getRuleList"
            />
          </el-card>

          <!-- 合规检查结果管理 -->
          <el-card>
            <div slot="header" class="clearfix">
              <span style="font-weight: bold; color: #303133;">合规检查结果</span>
            </div>

            <!-- 查询表单 -->
            <el-form :model="resultQueryParams" ref="resultQueryForm" size="small" :inline="true" v-show="resultShowSearch" label-width="68px">
              <el-form-item label="规则ID" prop="ruleId">
                <el-input
                  v-model="resultQueryParams.ruleId"
                  placeholder="请输入规则ID"
                  clearable
                  @keyup.enter.native="handleResultQuery"
                />
              </el-form-item>
              <el-form-item label="报告ID" prop="reportId">
                <el-input
                  v-model="resultQueryParams.reportId"
                  placeholder="请输入报告ID"
                  clearable
                  @keyup.enter.native="handleResultQuery"
                />
              </el-form-item>
              <el-form-item label="检查结果" prop="isPassed">
                <el-select v-model="resultQueryParams.isPassed" placeholder="请选择检查结果" clearable>
                  <el-option label="通过" :value="1" />
                  <el-option label="未通过" :value="0" />
                </el-select>
              </el-form-item>
              <el-form-item label="检查状态" prop="checkStatus">
                <el-select v-model="resultQueryParams.checkStatus" placeholder="请选择检查状态" clearable>
                  <el-option label="待检查" value="PENDING" />
                  <el-option label="检查中" value="CHECKING" />
                  <el-option label="已完成" value="COMPLETED" />
                  <el-option label="检查失败" value="FAILED" />
                  <el-option label="已取消" value="CANCELLED" />
                </el-select>
              </el-form-item>
              <el-form-item label="严重级别" prop="severityLevel">
                <el-select v-model="resultQueryParams.severityLevel" placeholder="请选择严重级别" clearable>
                  <el-option label="低" value="LOW" />
                  <el-option label="中" value="MEDIUM" />
                  <el-option label="高" value="HIGH" />
                  <el-option label="严重" value="CRITICAL" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleResultQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetResultQuery">重置</el-button>
              </el-form-item>
            </el-form>

            <!-- 操作按钮 -->
            <el-row :gutter="10" class="mb8">
              <el-col :span="1.5">
                <el-button
                  type="success"
                  plain
                  icon="el-icon-check"
                  size="mini"
                  :disabled="resultMultiple"
                  @click="handleBatchProcessResults"
                  v-hasPermi="['globalTreasurer:regulatory:result:process']"
                >批量处理</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  icon="el-icon-circle-check"
                  size="mini"
                  :disabled="resultMultiple"
                  @click="handleBatchResolveResults"
                  v-hasPermi="['globalTreasurer:regulatory:result:resolve']"
                >批量解决</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="warning"
                  plain
                  icon="el-icon-top"
                  size="mini"
                  :disabled="resultMultiple"
                  @click="handleBatchEscalateResults"
                  v-hasPermi="['globalTreasurer:regulatory:result:escalate']"
                >批量升级</el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="info"
                  plain
                  icon="el-icon-download"
                  size="mini"
                  @click="handleResultExport"
                  v-hasPermi="['globalTreasurer:regulatory:result:export']"
                >导出</el-button>
              </el-col>
              <right-toolbar :showSearch.sync="resultShowSearch" @queryTable="getResultList"></right-toolbar>
            </el-row>

            <!-- 结果列表 -->
            <el-table v-loading="resultLoading" :data="resultList" @selection-change="handleResultSelectionChange">
              <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="结果ID" align="center" prop="resultId" width="120" show-overflow-tooltip />
              <el-table-column label="规则ID" align="center" prop="ruleId" width="120" show-overflow-tooltip />
              <el-table-column label="报告ID" align="center" prop="reportId" width="120" show-overflow-tooltip />
              <el-table-column label="检查结果" align="center" prop="isPassed">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.isPassed === 1 ? 'success' : 'danger'">{{ scope.row.isPassed === 1 ? '通过' : '未通过' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="严重级别" align="center" prop="severityLevel">
                <template slot-scope="scope">
                  <el-tag :type="getSeverityTagType(scope.row.severityLevel)">{{ getSeverityDisplay(scope.row.severityLevel) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="检查状态" align="center" prop="checkStatus">
                <template slot-scope="scope">
                  <el-tag :type="getCheckStatusTagType(scope.row.checkStatus)">{{ getCheckStatusDisplay(scope.row.checkStatus) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="违规详情" align="center" prop="violationDetails" show-overflow-tooltip />
              <el-table-column label="采取措施" align="center" prop="actionTaken" show-overflow-tooltip />
              <el-table-column label="是否升级" align="center" prop="isEscalated">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.isEscalated === 1 ? 'warning' : 'info'">{{ scope.row.isEscalated === 1 ? '已升级' : '未升级' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="检查时间" align="center" prop="checkTime" width="180">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.checkTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-view"
                    @click="handleResultView(scope.row)"
                    v-hasPermi="['globalTreasurer:regulatory:result:query']"
                  >查看</el-button>
                  <el-button
                    v-if="scope.row.resultStatus === 'UNPROCESSED'"
                    size="mini"
                    type="text"
                    icon="el-icon-check"
                    @click="handleProcessResult(scope.row)"
                    v-hasPermi="['globalTreasurer:regulatory:result:process']"
                  >处理</el-button>
                  <el-button
                    v-if="scope.row.resultStatus === 'PROCESSED'"
                    size="mini"
                    type="text"
                    icon="el-icon-circle-check"
                    @click="handleResolveResult(scope.row)"
                    v-hasPermi="['globalTreasurer:regulatory:result:resolve']"
                  >解决</el-button>
                  <el-dropdown size="mini" @command="(command) => handleResultCommand(command, scope.row)" style="margin-left: 10px">
                    <span class="el-dropdown-link">
                      更多<i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item command="escalate" icon="el-icon-top">升级</el-dropdown-item>
                      <el-dropdown-item command="track" icon="el-icon-view">跟踪</el-dropdown-item>
                      <el-dropdown-item command="analysis" icon="el-icon-data-analysis">分析</el-dropdown-item>
                      <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <pagination
              v-show="resultTotal>0"
              :total="resultTotal"
              :page.sync="resultQueryParams.pageNum"
              :limit.sync="resultQueryParams.pageSize"
              @pagination="getResultList"
            />
          </el-card>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 报告模板添加或修改对话框 -->
    <el-dialog :title="templateTitle" :visible.sync="templateOpen" width="700px" append-to-body>
      <el-form ref="templateForm" :model="templateForm" :rules="templateRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="模板编码" prop="templateCode">
              <el-input v-model="templateForm.templateCode" placeholder="请输入模板编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="templateForm.templateName" placeholder="请输入模板名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="监管机构" prop="authorityId">
              <el-select v-model="templateForm.authorityId" placeholder="请选择监管机构" style="width:100%">
                <el-option
                  v-for="authority in authorityOptions"
                  :key="authority.authorityId"
                  :label="authority.authorityName"
                  :value="authority.authorityId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告频率" prop="reportFrequency">
              <el-select v-model="templateForm.reportFrequency" placeholder="请选择报告频率" style="width:100%">
                <el-option label="每日" value="DAILY" />
                <el-option label="每周" value="WEEKLY" />
                <el-option label="每月" value="MONTHLY" />
                <el-option label="每季度" value="QUARTERLY" />
                <el-option label="半年" value="SEMI_ANNUAL" />
                <el-option label="每年" value="ANNUAL" />
                <el-option label="不定期" value="AD_HOC" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="模板版本" prop="templateVersion">
              <el-input v-model="templateForm.templateVersion" placeholder="请输入版本号，如 1.0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板类型" prop="templateType">
              <el-input v-model="templateForm.templateType" placeholder="请输入模板类型" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker v-model="templateForm.effectiveDate" type="date" placeholder="选择生效日期" style="width:100%" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expiryDate">
              <el-date-picker v-model="templateForm.expiryDate" type="date" placeholder="选择失效日期" style="width:100%" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态" prop="isEnabled">
              <el-select v-model="templateForm.isEnabled" placeholder="请选择状态" style="width:100%">
                <el-option label="启用" :value="1" />
                <el-option label="停用" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="templateForm.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitTemplateForm">确 定</el-button>
        <el-button @click="cancelTemplate">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 监管机构添加或修改对话框 -->
    <el-dialog :title="authorityTitle" :visible.sync="authorityOpen" width="600px" append-to-body>
      <el-form ref="authorityForm" :model="authorityForm" :rules="authorityRules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="机构编码" prop="authorityCode">
              <el-input v-model="authorityForm.authorityCode" placeholder="请输入机构编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构名称" prop="authorityName">
              <el-input v-model="authorityForm.authorityName" placeholder="请输入机构名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="英文名称" prop="authorityNameEng">
              <el-input v-model="authorityForm.authorityNameEng" placeholder="请输入英文名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构类型" prop="authorityType">
              <el-select v-model="authorityForm.authorityType" placeholder="请选择机构类型">
                <el-option label="央行" value="CENTRAL_BANK" />
                <el-option label="证监会" value="SECURITIES_COMMISSION" />
                <el-option label="银保监会" value="BANKING_REGULATOR" />
                <el-option label="外汇局" value="FOREX_REGULATOR" />
                <el-option label="税务局" value="TAX_AUTHORITY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="国家代码" prop="countryCode">
              <el-input v-model="authorityForm.countryCode" placeholder="请输入国家代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管辖范围" prop="jurisdiction">
              <el-input v-model="authorityForm.jurisdiction" placeholder="请输入管辖范围" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="authorityForm.contactPerson" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="authorityForm.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="authorityForm.contactEmail" placeholder="请输入联系邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="网站" prop="website">
              <el-input v-model="authorityForm.website" placeholder="请输入网站" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址" prop="address">
          <el-input v-model="authorityForm.address" type="textarea" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="authorityForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAuthorityForm">确 定</el-button>
        <el-button @click="cancelAuthority">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 监管报告添加或修改对话框 -->
    <el-dialog :title="reportTitle" :visible.sync="reportOpen" width="800px" append-to-body>
      <el-form ref="reportForm" :model="reportForm" :rules="reportRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="报告编号" prop="reportNo">
              <el-input v-model="reportForm.reportNo" placeholder="请输入报告编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告名称" prop="reportName">
              <el-input v-model="reportForm.reportName" placeholder="请输入报告名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="监管机构" prop="authorityId">
              <el-select v-model="reportForm.authorityId" placeholder="请选择监管机构" style="width: 100%">
                <el-option
                  v-for="authority in authorityOptions"
                  :key="authority.authorityId"
                  :label="authority.authorityName"
                  :value="authority.authorityId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告模板" prop="templateId">
              <el-select v-model="reportForm.templateId" placeholder="请选择报告模板" style="width: 100%">
                <el-option
                  v-for="template in templateOptions"
                  :key="template.templateId"
                  :label="template.templateName"
                  :value="template.templateId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="报告期间" prop="reportPeriod">
              <el-input v-model="reportForm.reportPeriod" placeholder="请输入报告期间，如：2024Q3" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="截止日期" prop="dueDate">
              <el-date-picker
                v-model="reportForm.dueDate"
                type="date"
                placeholder="选择截止日期"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="报告日期" prop="reportDate">
              <el-date-picker
                v-model="reportForm.reportDate"
                type="date"
                placeholder="选择报告日期"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提交方式" prop="submissionMethod">
              <el-select v-model="reportForm.submissionMethod" placeholder="请选择提交方式" style="width: 100%">
                <el-option label="在线提交" value="ONLINE" />
                <el-option label="邮件提交" value="EMAIL" />
                <el-option label="文件上传" value="UPLOAD" />
                <el-option label="手工提交" value="MANUAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="报告内容" prop="reportContent">
          <el-input v-model="reportForm.reportContent" type="textarea" :rows="4" placeholder="请输入报告内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="reportForm.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitReportForm">确 定</el-button>
        <el-button @click="cancelReport">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 报告查看对话框 -->
    <el-dialog title="报告详情" :visible.sync="reportViewOpen" width="900px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="报告编号">{{ reportViewData.reportNo }}</el-descriptions-item>
        <el-descriptions-item label="报告名称">{{ reportViewData.reportName }}</el-descriptions-item>
        <el-descriptions-item label="监管机构">{{ reportViewData.authorityName }}</el-descriptions-item>
        <el-descriptions-item label="报告模板">{{ reportViewData.templateName }}</el-descriptions-item>
        <el-descriptions-item label="报告期间">{{ reportViewData.reportPeriod }}</el-descriptions-item>
        <el-descriptions-item label="截止日期">{{ parseTime(reportViewData.dueDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="报告状态">
          <el-tag :type="getReportStatusTagType(reportViewData.reportStatus)">
            {{ getReportStatusDisplay(reportViewData.reportStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="提交方式">{{ getSubmissionMethodDisplay(reportViewData.submissionMethod) }}</el-descriptions-item>
        <el-descriptions-item label="提交日期">{{ parseTime(reportViewData.submitDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="回执编号">{{ reportViewData.acknowledgmentNo }}</el-descriptions-item>
        <el-descriptions-item label="拒绝原因" v-if="reportViewData.rejectReason">{{ reportViewData.rejectReason }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(reportViewData.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">报告内容</el-divider>
      <div class="report-content">
        {{ reportViewData.reportContent || '暂无内容' }}
      </div>

      <el-divider content-position="left">备注</el-divider>
      <div class="validation-result">
        {{ reportViewData.remark || '暂无备注' }}
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="reportViewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 合规检查规则添加或修改对话框 -->
    <el-dialog :title="ruleTitle" :visible.sync="ruleOpen" width="800px" append-to-body>
      <el-form ref="ruleForm" :model="ruleForm" :rules="ruleRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="规则编码" prop="ruleCode">
              <el-input v-model="ruleForm.ruleCode" placeholder="请输入规则编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="ruleForm.ruleName" placeholder="请输入规则名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="规则类型" prop="ruleType">
              <el-select v-model="ruleForm.ruleType" placeholder="请选择规则类型">
                <el-option label="限额检查" value="LIMIT_CHECK" />
                <el-option label="比率检查" value="RATIO_CHECK" />
                <el-option label="阈值检查" value="THRESHOLD_CHECK" />
                <el-option label="业务规则" value="BUSINESS_RULE" />
                <el-option label="数据质量" value="DATA_QUALITY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="严重程度" prop="severityLevel">
              <el-select v-model="ruleForm.severityLevel" placeholder="请选择严重程度">
                <el-option label="低" value="LOW" />
                <el-option label="中" value="MEDIUM" />
                <el-option label="高" value="HIGH" />
                <el-option label="严重" value="CRITICAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="检查频率" prop="checkFrequency">
              <el-select v-model="ruleForm.checkFrequency" placeholder="请选择检查频率">
                <el-option label="实时" value="REAL_TIME" />
                <el-option label="每小时" value="HOURLY" />
                <el-option label="每日" value="DAILY" />
                <el-option label="每周" value="WEEKLY" />
                <el-option label="每月" value="MONTHLY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查范围" prop="checkScope">
              <el-input v-model="ruleForm.checkScope" placeholder="请输入检查范围" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="阈值" prop="thresholdValue">
              <el-input-number v-model="ruleForm.thresholdValue" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="警告阈值" prop="warningThreshold">
              <el-input-number v-model="ruleForm.warningThreshold" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="规则条件" prop="ruleCondition">
          <el-input v-model="ruleForm.ruleCondition" type="textarea" :rows="3" placeholder="请输入规则条件" />
        </el-form-item>
        <el-form-item label="规则公式" prop="ruleFormula">
          <el-input v-model="ruleForm.ruleFormula" type="textarea" :rows="2" placeholder="请输入规则公式（可选）" />
        </el-form-item>
        <el-form-item label="法规依据" prop="regulationReference">
          <el-input v-model="ruleForm.regulationReference" placeholder="请输入法规依据" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="ruleForm.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expiryDate">
              <el-date-picker
                v-model="ruleForm.expiryDate"
                type="date"
                placeholder="选择失效日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-radio-group v-model="ruleForm.isEnabled">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRuleForm">确 定</el-button>
        <el-button @click="cancelRule">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 合规检查规则查看对话框 -->
    <el-dialog :title="ruleViewTitle" :visible.sync="ruleViewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="规则编码">{{ ruleViewForm.ruleCode }}</el-descriptions-item>
        <el-descriptions-item label="规则名称">{{ ruleViewForm.ruleName }}</el-descriptions-item>
        <el-descriptions-item label="规则类型">{{ getRuleTypeDisplay(ruleViewForm.ruleType) }}</el-descriptions-item>
        <el-descriptions-item label="严重程度">{{ getSeverityDisplay(ruleViewForm.severityLevel) }}</el-descriptions-item>
        <el-descriptions-item label="检查频率">{{ getFrequencyDisplay(ruleViewForm.checkFrequency) }}</el-descriptions-item>
        <el-descriptions-item label="检查范围">{{ ruleViewForm.checkScope }}</el-descriptions-item>
        <el-descriptions-item label="阈值">{{ ruleViewForm.thresholdValue }}</el-descriptions-item>
        <el-descriptions-item label="警告阈值">{{ ruleViewForm.warningThreshold }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="ruleViewForm.isEnabled === 1 ? 'success' : 'danger'">{{ ruleViewForm.isEnabled === 1 ? '启用' : '停用' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(ruleViewForm.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">规则条件</el-divider>
      <div class="report-content">{{ ruleViewForm.ruleCondition || '暂无条件' }}</div>

      <el-divider content-position="left" v-if="ruleViewForm.ruleFormula">规则公式</el-divider>
      <div class="validation-result" v-if="ruleViewForm.ruleFormula">{{ ruleViewForm.ruleFormula }}</div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="ruleViewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 合规检查结果查看对话框 -->
    <el-dialog :title="resultViewTitle" :visible.sync="resultViewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="结果ID">{{ resultViewForm.resultId }}</el-descriptions-item>
        <el-descriptions-item label="规则ID">{{ resultViewForm.ruleId }}</el-descriptions-item>
        <el-descriptions-item label="报告ID">{{ resultViewForm.reportId }}</el-descriptions-item>
        <el-descriptions-item label="检查结果">
          <el-tag :type="resultViewForm.isPassed === 1 ? 'success' : 'danger'">{{ resultViewForm.isPassed === 1 ? '通过' : '未通过' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="检查状态">
          <el-tag :type="getCheckStatusTagType(resultViewForm.checkStatus)">{{ getCheckStatusDisplay(resultViewForm.checkStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="严重级别">
          <el-tag :type="getSeverityTagType(resultViewForm.severityLevel)">{{ getSeverityDisplay(resultViewForm.severityLevel) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否升级">
          <el-tag :type="resultViewForm.isEscalated === 1 ? 'warning' : 'info'">{{ resultViewForm.isEscalated === 1 ? '已升级' : '未升级' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="检查时间">{{ parseTime(resultViewForm.checkTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</el-descriptions-item>
        <el-descriptions-item label="解决时间">{{ resultViewForm.resolvedTime ? parseTime(resultViewForm.resolvedTime, '{y}-{m}-{d} {h}:{i}:{s}') : '未解决' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(resultViewForm.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left" v-if="resultViewForm.violationDetails">违规详情</el-divider>
      <div class="report-content" v-if="resultViewForm.violationDetails">{{ resultViewForm.violationDetails }}</div>

      <el-divider content-position="left" v-if="resultViewForm.actionTaken">采取措施</el-divider>
      <div class="validation-result" v-if="resultViewForm.actionTaken">{{ resultViewForm.actionTaken }}</div>

      <el-divider content-position="left" v-if="resultViewForm.remark">备注</el-divider>
      <div class="report-content" v-if="resultViewForm.remark">{{ resultViewForm.remark }}</div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="resultViewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 规则执行历史对话框 -->
    <el-dialog :title="ruleHistoryTitle" :visible.sync="ruleHistoryOpen" width="900px" append-to-body>
      <el-table v-loading="ruleHistoryLoading" :data="ruleHistoryList" border>
        <el-table-column label="检查时间" align="center" prop="checkTime" width="160">
          <template slot-scope="scope">{{ parseTime(scope.row.checkTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</template>
        </el-table-column>
        <el-table-column label="检查结果" align="center" prop="isPassed" width="90">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isPassed === 1 ? 'success' : 'danger'">{{ scope.row.isPassed === 1 ? '通过' : '未通过' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="检查状态" align="center" prop="checkStatus" width="100">
          <template slot-scope="scope">
            <el-tag :type="getCheckStatusTagType(scope.row.checkStatus)">{{ getCheckStatusDisplay(scope.row.checkStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="严重级别" align="center" prop="severityLevel" width="90">
          <template slot-scope="scope">
            <el-tag :type="getSeverityTagType(scope.row.severityLevel)">{{ getSeverityDisplay(scope.row.severityLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="是否升级" align="center" prop="isEscalated" width="90">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEscalated === 1 ? 'warning' : 'info'">{{ scope.row.isEscalated === 1 ? '已升级' : '未升级' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="违规详情" align="center" prop="violationDetails" show-overflow-tooltip />
        <el-table-column label="采取措施" align="center" prop="actionTaken" show-overflow-tooltip />
        <el-table-column label="解决时间" align="center" prop="resolvedTime" width="160">
          <template slot-scope="scope">{{ scope.row.resolvedTime ? parseTime(scope.row.resolvedTime, '{y}-{m}-{d} {h}:{i}:{s}') : '-' }}</template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="ruleHistoryTotal > 0"
        :total="ruleHistoryTotal"
        :page.sync="ruleHistoryQuery.pageNum"
        :limit.sync="ruleHistoryQuery.pageSize"
        @pagination="loadRuleHistory"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="ruleHistoryOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getAuthorityList,
  getAuthorityById,
  addAuthority,
  updateAuthority,
  delAuthority,
  toggleAuthorityStatus,
  exportAuthorityData,
  getActiveAuthorities,
  getTemplateList,
  getTemplateById,
  addTemplate,
  updateTemplate,
  delTemplate,
  toggleTemplateStatus,
  copyTemplate,
  // 监管报告管理相关API
  getReportList,
  getReportById,
  addReport,
  updateReport,
  delReport,
  generateReport,
  validateReport,
  submitReport,
  copyReport,
  getReportStatistics,
  getUsableTemplates,
  exportReportData,
  recallReport,
  getReportHistory,
  batchGenerateReports,
  batchSubmitReports,
  // 合规检查规则相关API
  getComplianceRuleList,
  getComplianceRuleById,
  addComplianceRule,
  updateComplianceRule,
  delComplianceRule,
  toggleComplianceRuleStatus,
  executeComplianceRule,
  batchExecuteComplianceRules,
  copyComplianceRule,
  exportComplianceRuleData,
  // 合规检查结果相关API
  getComplianceResultList,
  getComplianceResultById,
  processComplianceResult,
  resolveComplianceResult,
  escalateComplianceResult,
  batchProcessComplianceResults,
  batchResolveComplianceResults,
  batchEscalateComplianceResults,
  exportComplianceResultData,
  getComplianceStatistics,
  getComplianceTrends
} from "@/api/globalTreasurer/jgbs";
import Pagination from '@/components/Pagination'

export default {
  name: "RegulatoryReporting",
  components: { Pagination },
  directives: {
    hasPermi: {
      inserted(el, binding) {
        const { value } = binding;
        const permStr = sessionStorage.getItem('permissions');
        // 如果没有设置权限列表，默认放行所有按钮（开发环境友好）
        if (!permStr) return;
        const permissions = JSON.parse(permStr || '[]');
        if (value && value instanceof Array && value.length > 0) {
          const hasPermission = permissions.some(permission => {
            return value.includes(permission);
          });
          if (!hasPermission) {
            el.style.display = 'none';
          }
        }
      }
    }
  },
  data() {
    return {
      // 当前激活的标签页
      activeTab: "authority",

      // 监管机构管理相关数据
      authorityLoading: true,
      authorityIds: [],
      authoritySingle: true,
      authorityMultiple: true,
      authorityShowSearch: true,
      authorityTotal: 0,
      authorityList: [],
      authorityTitle: "",
      authorityOpen: false,
      authorityQueryParams: {
        pageNum: 1,
        pageSize: 10,
        authorityCode: null,
        authorityName: null,
        authorityType: null,
        isActive: null
      },
      authorityForm: {},
      authorityRules: {
        authorityCode: [
          { required: true, message: "机构编码不能为空", trigger: "blur" }
        ],
        authorityName: [
          { required: true, message: "机构名称不能为空", trigger: "blur" }
        ],
        authorityType: [
          { required: true, message: "机构类型不能为空", trigger: "change" }
        ]
      },

      // 报告模板管理相关数据
      templateLoading: true,
      templateIds: [],
      templateSingle: true,
      templateMultiple: true,
      templateShowSearch: true,
      templateTotal: 0,
      templateList: [],
      templateTitle: "",
      templateOpen: false,
      templateQueryParams: {
        pageNum: 1,
        pageSize: 10,
        templateCode: null,
        templateName: null,
        authorityId: null,
        isEnabled: null
      },
      templateForm: {},
      templateRules: {
        templateCode: [
          { required: true, message: "模板编码不能为空", trigger: "blur" }
        ],
        templateName: [
          { required: true, message: "模板名称不能为空", trigger: "blur" }
        ],
        authorityId: [
          { required: true, message: "监管机构不能为空", trigger: "change" }
        ],
        reportFrequency: [
          { required: true, message: "报告频率不能为空", trigger: "change" }
        ]
      },

      // 监管报告管理相关数据
      reportLoading: true,
      reportIds: [],
      reportSingle: true,
      reportMultiple: true,
      reportShowSearch: true,
      reportTotal: 0,
      reportList: [],
      reportTitle: "",
      reportOpen: false,
      reportViewOpen: false,
      reportViewData: {},
      reportQueryParams: {
        pageNum: 1,
        pageSize: 10,
        reportNo: null,
        reportName: null,
        authorityId: null,
        reportStatus: null,
        reportPeriod: null
      },
      reportForm: {},
      reportRules: {
        reportNo: [
          { required: true, message: "报告编号不能为空", trigger: "blur" }
        ],
        reportName: [
          { required: true, message: "报告名称不能为空", trigger: "blur" }
        ],
        authorityId: [
          { required: true, message: "监管机构不能为空", trigger: "change" }
        ],
        templateId: [
          { required: true, message: "报告模板不能为空", trigger: "change" }
        ],
        reportPeriod: [
          { required: true, message: "报告期间不能为空", trigger: "blur" }
        ],
        dueDate: [
          { required: true, message: "截止日期不能为空", trigger: "change" }
        ]
      },
      reportStatistics: {},

      // 选项数据
      authorityOptions: [],
      templateOptions: [],
      authorityTypeOptions: [
        { label: "央行", value: "CENTRAL_BANK" },
        { label: "证监会", value: "SECURITIES_COMMISSION" },
        { label: "银保监会", value: "BANKING_REGULATOR" },
        { label: "外汇局", value: "FOREX_REGULATOR" },
        { label: "税务局", value: "TAX_AUTHORITY" }
      ],
      reportFrequencyOptions: [
        { label: "日报", value: "DAILY" },
        { label: "周报", value: "WEEKLY" },
        { label: "月报", value: "MONTHLY" },
        { label: "季报", value: "QUARTERLY" },
        { label: "半年报", value: "SEMI_ANNUAL" },
        { label: "年报", value: "ANNUAL" },
        { label: "临时报告", value: "AD_HOC" }
      ],

      // 合规检查管理相关数据
      complianceStatistics: {
        totalRules: 45,
        failedResults: 15,
        passedResults: 189,
        complianceScore: 92.5
      },

      // 合规检查规则管理相关数据
      ruleLoading: true,
      ruleIds: [],
      ruleSingle: true,
      ruleMultiple: true,
      ruleShowSearch: true,
      ruleTotal: 0,
      ruleList: [],
      ruleTitle: "",
      ruleOpen: false,
      ruleViewOpen: false,
      ruleViewTitle: "规则详情",
      ruleQueryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleCode: null,
        ruleName: null,
        ruleType: null,
        severityLevel: null,
        isEnabled: null
      },
      ruleForm: {
        ruleId: null,
        ruleCode: null,
        ruleName: null,
        ruleGroup: null,
        authorityId: null,
        regulationReference: null,
        ruleType: null,
        checkScope: null,
        ruleCondition: null,
        ruleFormula: null,
        thresholdValue: null,
        warningThreshold: null,
        checkFrequency: "DAILY",
        severityLevel: "MEDIUM",
        isEnabled: 1,
        effectiveDate: null,
        expiryDate: null
      },
      ruleViewForm: {},
      // 规则执行历史
      ruleHistoryOpen: false,
      ruleHistoryTitle: "执行历史",
      ruleHistoryLoading: false,
      ruleHistoryList: [],
      ruleHistoryTotal: 0,
      ruleHistoryQuery: { pageNum: 1, pageSize: 10, ruleId: null },
      ruleRules: {
        ruleCode: [
          { required: true, message: "规则编码不能为空", trigger: "blur" }
        ],
        ruleName: [
          { required: true, message: "规则名称不能为空", trigger: "blur" }
        ],
        ruleType: [
          { required: true, message: "规则类型不能为空", trigger: "change" }
        ],
        checkScope: [
          { required: true, message: "检查范围不能为空", trigger: "blur" }
        ],
        ruleCondition: [
          { required: true, message: "规则条件不能为空", trigger: "blur" }
        ]
      },

      // 合规检查结果管理相关数据
      resultLoading: true,
      resultIds: [],
      resultSingle: true,
      resultMultiple: true,
      resultShowSearch: true,
      resultTotal: 0,
      resultList: [],
      resultViewOpen: false,
      resultViewTitle: "结果详情",
      resultQueryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleId: null,
        reportId: null,
        isPassed: null,
        checkStatus: null,
        severityLevel: null
      },
      resultViewForm: {}
    };
  },
  created() {
    this.getAuthorityList();
    this.getAuthorityOptions();
    this.getTemplateOptions();
    this.getReportList();
    this.getReportStatistics();
    this.getComplianceStatistics();
  },
  methods: {
    // ==================== 工具方法 ====================
    /** 获取机构类型显示文字 */
    getAuthorityTypeLabel(type) {
      const map = {
        CENTRAL_BANK: '央行',
        SECURITIES_COMMISSION: '证监会',
        BANKING_REGULATOR: '银保监会',
        FOREX_REGULATOR: '外汇局',
        TAX_AUTHORITY: '税务局'
      };
      return map[type] || type || '-';
    },

    /** 获取报告频率显示文字 */
    getReportFrequencyLabel(freq) {
      const map = {
        DAILY: '每日',
        WEEKLY: '每周',
        MONTHLY: '每月',
        QUARTERLY: '每季度',
        SEMI_ANNUAL: '半年',
        ANNUAL: '每年',
        AD_HOC: '不定期'
      };
      return map[freq] || freq || '-';
    },

    // ==================== 标签页切换 ====================
    handleTabClick(tab) {
      if (tab.name === "authority") {
        this.getAuthorityList();
      } else if (tab.name === "template") {
        this.getTemplateList();
      } else if (tab.name === "report") {
        this.getReportList();
        this.getReportStatistics();
      } else if (tab.name === "compliance") {
        this.getRuleList();
        this.getResultList();
        this.getComplianceStatistics();
      }
    },

    // ==================== 监管机构管理方法 ====================
    /** 查询监管机构列表 */
    getAuthorityList() {
      this.authorityLoading = true;
      getAuthorityList(this.authorityQueryParams).then(response => {
        // 支持多种响应格式: {code: 1, data: {rows: [...], total: N}} 或 {rows: [...], total: N}
        if (response.code === 1 && response.data) {
          this.authorityList = response.data.rows || response.data || [];
          this.authorityTotal = response.data.total || 0;
        } else {
          this.authorityList = response.rows || response.data || response.records || [];
          this.authorityTotal = response.total || response.count || 0;
        }
        this.authorityLoading = false;
      }).catch(error => {
        console.error('获取监管机构列表失败:', error);
        this.$message.error('获取监管机构列表失败，请检查网络连接或稍后重试');
        this.authorityList = [];
        this.authorityTotal = 0;
        this.authorityLoading = false;
      });
    },

    /** 获取监管机构选项 */
    getAuthorityOptions() {
      getActiveAuthorities().then(response => {
        if (response.code === 1 && response.data) {
          this.authorityOptions = response.data.rows || response.data || [];
        } else {
          this.authorityOptions = response.data || response.rows || [];
        }
      }).catch(error => {
        console.error('获取监管机构选项失败:', error);
        this.authorityOptions = [];
      });
    },

    /** 取消按钮 */
    cancelAuthority() {
      this.authorityOpen = false;
      this.resetAuthorityForm();
    },

    /** 表单重置 */
    resetAuthorityForm() {
      this.authorityForm = {
        authorityId: null,
        authorityCode: null,
        authorityName: null,
        authorityNameEng: null,
        authorityType: null,
        countryCode: null,
        jurisdiction: null,
        contactPerson: null,
        contactPhone: null,
        contactEmail: null,
        address: null,
        website: null,
        isActive: 1,
        remark: null
      };
      this.$nextTick(() => {
        if (this.$refs["authorityForm"]) {
          this.$refs["authorityForm"].clearValidate();
        }
      });
    },

    /** 搜索按钮操作 */
    handleAuthorityQuery() {
      this.authorityQueryParams.pageNum = 1;
      this.getAuthorityList();
    },

    /** 重置按钮操作 */
    resetAuthorityQuery() {
      this.authorityQueryParams = {
        pageNum: 1,
        pageSize: 10,
        authorityCode: null,
        authorityName: null,
        authorityType: null,
        isActive: null
      };
      this.$nextTick(() => {
        if (this.$refs["authorityQueryForm"]) {
          this.$refs["authorityQueryForm"].clearValidate();
        }
      });
      this.handleAuthorityQuery();
    },

    /** 多选框选中数据 */
    handleAuthoritySelectionChange(selection) {
      this.authorityIds = selection.map(item => item.authorityId);
      this.authoritySingle = selection.length !== 1;
      this.authorityMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAuthorityAdd() {
      this.resetAuthorityForm();
      this.authorityOpen = true;
      this.authorityTitle = "添加监管机构";
    },

    /** 修改按钮操作 */
    handleAuthorityUpdate(row) {
      this.resetAuthorityForm();
      const authorityId = (row && row.authorityId) ? row.authorityId : (this.authorityIds && this.authorityIds[0]);
      if (!authorityId) {
        this.$message.warning('请先选择要修改的记录');
        return;
      }
      getAuthorityById(authorityId).then(response => {
        const data = response.data || response;
        this.authorityForm = { ...data };
        this.authorityOpen = true;
        this.authorityTitle = "修改监管机构";
      }).catch(error => {
        console.error('获取监管机构详情失败:', error);
        this.$message.error('获取数据失败，请稍后重试');
      });
    },

    /** 提交按钮 */
    submitAuthorityForm() {
      this.$refs["authorityForm"].validate(valid => {
        if (valid) {
          if (this.authorityForm.authorityId != null) {
            updateAuthority(this.authorityForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.authorityOpen = false;
              this.getAuthorityList();
            }).catch(error => {
              console.error('修改监管机构失败:', error);
              this.$message.error('修改监管机构失败，请稍后重试');
            });
          } else {
            addAuthority(this.authorityForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.authorityOpen = false;
              this.getAuthorityList();
            }).catch(error => {
              console.error('新增监管机构失败:', error);
              this.$message.error('新增监管机构失败，请稍后重试');
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleAuthorityDelete(row) {
      const authorityIds = row.authorityId || this.authorityIds;
      this.$modal.confirm('是否确认删除监管机构编号为"' + authorityIds + '"的数据项？').then(function() {
        return delAuthority(authorityIds);
      }).then(() => {
        this.getAuthorityList();
        this.$modal.msgSuccess("删除成功");
      }).catch((error) => {
        if (error !== 'cancel') {
          console.error('删除监管机构失败:', error);
          this.$message.error('删除监管机构失败，请稍后重试');
        }
      });
    },

    /** 状态修改 */
    handleAuthorityStatusChange(row) {
      let text = row.isActive === 1 ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.authorityName + '"监管机构吗？').then(function() {
        return toggleAuthorityStatus(row.authorityId, row.isActive);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch((error) => {
        row.isActive = row.isActive === 0 ? 1 : 0;
        if (error !== 'cancel') {
          console.error('切换监管机构状态失败:', error);
          this.$message.error('切换监管机构状态失败，请稍后重试');
        }
      });
    },

    /** 导出按钮操作 */
    handleAuthorityExport() {
      exportAuthorityData(this.authorityQueryParams).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = `authority_${new Date().getTime()}.xlsx`
        link.click()
        URL.revokeObjectURL(link.href)
        this.$modal.msgSuccess('导出成功')
      }).catch(error => {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      })
    },

    // ==================== 报告模板管理方法 ====================
    /** 查询报告模板列表 */
    getTemplateList() {
      this.templateLoading = true;
      getTemplateList(this.templateQueryParams).then(response => {
        // 支持多种响应格式: {code: 1, data: {rows: [...], total: N}} 或 {rows: [...], total: N}
        if (response.code === 1 && response.data) {
          this.templateList = response.data.rows || response.data || [];
          this.templateTotal = response.data.total || 0;
        } else {
          this.templateList = response.rows || response.data || response.records || [];
          this.templateTotal = response.total || response.count || 0;
        }
        this.templateLoading = false;
      }).catch(error => {
        console.error('获取报告模板列表失败:', error);
        this.$message.error('获取报告模板列表失败，请检查网络连接或稍后重试');
        this.templateList = [];
        this.templateTotal = 0;
        this.templateLoading = false;
      });
    },

    /** 取消按钮 */
    cancelTemplate() {
      this.templateOpen = false;
      this.resetTemplateForm();
    },

    /** 表单重置 */
    resetTemplateForm() {
      this.templateForm = {
        templateId: null,
        templateCode: null,
        templateName: null,
        templateNameEng: null,
        authorityId: null,
        reportFrequency: null,
        reportFormat: null,
        templateStructure: null,
        dataSourceConfig: null,
        validationRules: null,
        version: null,
        effectiveDate: null,
        expiryDate: null,
        isEnabled: 1,
        remark: null
      };
      this.$nextTick(() => {
        if (this.$refs["templateForm"]) {
          this.$refs["templateForm"].clearValidate();
        }
      });
    },

    /** 搜索按钮操作 */
    handleTemplateQuery() {
      this.templateQueryParams.pageNum = 1;
      this.getTemplateList();
    },

    /** 重置按钮操作 */
    resetTemplateQuery() {
      this.templateQueryParams = {
        pageNum: 1,
        pageSize: 10,
        templateCode: null,
        templateName: null,
        authorityId: null,
        isEnabled: null
      };
      this.$nextTick(() => {
        if (this.$refs["templateQueryForm"]) {
          this.$refs["templateQueryForm"].clearValidate();
        }
      });
      this.handleTemplateQuery();
    },

    /** 多选框选中数据 */
    handleTemplateSelectionChange(selection) {
      this.templateIds = selection.map(item => item.templateId);
      this.templateSingle = selection.length !== 1;
      this.templateMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleTemplateAdd() {
      this.resetTemplateForm();
      this.templateOpen = true;
      this.templateTitle = "添加报告模板";
    },

    /** 修改按钮操作 */
    handleTemplateUpdate(row) {
      this.resetTemplateForm();
      const templateId = (row && row.templateId) ? row.templateId : (this.templateIds && this.templateIds[0]);
      if (!templateId) {
        this.$message.warning('请先选择一条记录');
        return;
      }
      getTemplateById(templateId).then(response => {
        const data = (response.code === 1 && response.data) ? response.data : response;
        // 日期字段格式化：确保 el-date-picker 能正确回显
        if (data.effectiveDate && typeof data.effectiveDate !== 'string') {
          data.effectiveDate = new Date(data.effectiveDate).toISOString().slice(0, 10);
        }
        if (data.expiryDate && typeof data.expiryDate !== 'string') {
          data.expiryDate = new Date(data.expiryDate).toISOString().slice(0, 10);
        }
        this.templateForm = data;
        this.templateOpen = true;
        this.templateTitle = "修改报告模板";
      }).catch(error => {
        console.error('获取报告模板详情失败:', error);
        this.$message.error('获取模板详情失败，请稍后重试');
      });
    },

    /** 提交按钮 */
    submitTemplateForm() {
      this.$refs["templateForm"].validate(valid => {
        if (valid) {
          if (this.templateForm.templateId != null) {
            updateTemplate(this.templateForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.templateOpen = false;
              this.getTemplateList();
            }).catch(error => {
              console.error('修改报告模板失败:', error);
              this.$message.error('修改报告模板失败，请稍后重试');
            });
          } else {
            addTemplate(this.templateForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.templateOpen = false;
              this.getTemplateList();
            }).catch(error => {
              console.error('新增报告模板失败:', error);
              this.$message.error('新增报告模板失败，请稍后重试');
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleTemplateDelete(row) {
      const templateIds = row.templateId || this.templateIds;
      this.$modal.confirm('是否确认删除报告模板编号为"' + templateIds + '"的数据项？').then(function() {
        return delTemplate(templateIds);
      }).then(() => {
        this.getTemplateList();
        this.$modal.msgSuccess("删除成功");
      }).catch((error) => {
        if (error !== 'cancel') {
          console.error('删除报告模板失败:', error);
          this.$message.error('删除报告模板失败，请稍后重试');
        }
      });
    },

    /** 状态修改 */
    handleTemplateStatusChange(row) {
      let text = row.isEnabled === 1 ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.templateName + '"报告模板吗？').then(function() {
        return toggleTemplateStatus(row.templateId, row.isEnabled);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch((error) => {
        row.isEnabled = row.isEnabled === 0 ? 1 : 0;
        if (error !== 'cancel') {
          console.error('切换报告模板状态失败:', error);
          this.$message.error('切换报告模板状态失败，请稍后重试');
        }
      });
    },

    /** 复制模板 */
    handleTemplateCopy(row) {
      this.$prompt('请输入新模板编码', '复制模板', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[a-zA-Z0-9_-]+$/,
        inputErrorMessage: '模板编码格式不正确'
      }).then(({ value }) => {
        const newTemplateName = row.templateName + '_副本';
        copyTemplate(row.templateId, value, newTemplateName).then(response => {
          this.$modal.msgSuccess("复制成功");
          this.getTemplateList();
        });
      }).catch(() => {});
    },

    // ==================== 监管报告管理方法 ====================
    /** 查询监管报告列表 */
    getReportList() {
      this.reportLoading = true;
      getReportList(this.reportQueryParams).then(response => {
        // 支持多种响应格式: {code: 1, data: {rows: [...], total: N}} 或 {rows: [...], total: N}
        if (response.code === 1 && response.data) {
          this.reportList = response.data.rows || response.data || [];
          this.reportTotal = response.data.total || 0;
        } else {
          this.reportList = response.rows || response.data || [];
          this.reportTotal = response.total || (response.result ? response.result.total : 0);
        }
        this.reportLoading = false;
      }).catch(error => {
        console.error('获取监管报告列表失败:', error);
        this.$message.error('获取报告列表失败，请检查网络连接或稍后重试');
        this.reportList = [];
        this.reportTotal = 0;
        this.reportLoading = false;
      });
    },

    /** 获取报告统计信息 */
    getReportStatistics() {
      getReportStatistics().then(response => {
        const stats = response.data || {};
        this.reportStatistics = {
          totalReports: Number(stats.totalReports || stats.TOTALCOUNT || 0),
          overdueReports: Number(stats.overdueReports || stats.OVERDUECOUNT || 0),
          dueSoonReports: Number(stats.dueSoonReports || stats.DUESOONCOUNT || 0),
          acceptedReports: Number(stats.acceptedReports || stats.ACCEPTEDCOUNT || 0),
          draftCount: Number(stats.draftCount || stats.DRAFTCOUNT || 0),
          generatedCount: Number(stats.generatedCount || stats.GENERATEDCOUNT || 0),
          validatedCount: Number(stats.validatedCount || stats.VALIDATEDCOUNT || 0),
          submittedCount: Number(stats.submittedCount || stats.SUBMITTEDCOUNT || 0),
          rejectedCount: Number(stats.rejectedCount || stats.REJECTEDCOUNT || 0)
        };
      }).catch(error => {
        console.error('获取报告统计信息失败:', error);
        this.reportStatistics = {
          totalReports: 0,
          overdueReports: 0,
          dueSoonReports: 0,
          acceptedReports: 0
        };
      });
    },

    /** 获取模板选项 */
    getTemplateOptions() {
      getUsableTemplates().then(response => {
        this.templateOptions = response.data || [];
      }).catch(error => {
        console.error('获取模板选项失败:', error);
        this.templateOptions = [];
      });
    },

    /** 取消按钮 */
    cancelReport() {
      this.reportOpen = false;
      this.resetReportForm();
    },

    /** 表单重置 */
    resetReportForm() {
      this.reportForm = {
        reportId: null,
        reportNo: null,
        reportName: null,
        templateId: null,
        authorityId: null,
        reportPeriod: null,
        reportDate: null,
        dueDate: null,
        reportStatus: 'DRAFT',
        reportContent: null,
        submissionMethod: 'ONLINE',
        remark: null
      };
      this.$nextTick(() => {
        if (this.$refs["reportForm"]) {
          this.$refs["reportForm"].clearValidate();
        }
      });
    },

    /** 搜索按钮操作 */
    handleReportQuery() {
      this.reportQueryParams.pageNum = 1;
      this.getReportList();
    },

    /** 重置按钮操作 */
    resetReportQuery() {
      this.reportQueryParams = {
        pageNum: 1,
        pageSize: 10,
        reportNo: null,
        reportName: null,
        authorityId: null,
        reportStatus: null,
        reportPeriod: null
      };
      this.$nextTick(() => {
        if (this.$refs["reportQueryForm"]) {
          this.$refs["reportQueryForm"].clearValidate();
        }
      });
      this.handleReportQuery();
    },

    /** 多选框选中数据 */
    handleReportSelectionChange(selection) {
      this.reportIds = selection.map(item => item.reportId);
      this.reportSingle = selection.length !== 1;
      this.reportMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleReportAdd() {
      this.resetReportForm();
      this.reportOpen = true;
      this.reportTitle = "添加监管报告";
    },

    /** 修改按钮操作 */
    handleReportUpdate(row) {
      this.resetReportForm();
      const reportId = row.reportId || this.reportIds;
      getReportById(reportId).then(response => {
        this.reportForm = response.data;
        this.reportOpen = true;
        this.reportTitle = "修改监管报告";
      });
    },

    /** 提交按钮 */
    submitReportForm() {
      this.$refs["reportForm"].validate(valid => {
        if (valid) {
          if (this.reportForm.reportId != null) {
            updateReport(this.reportForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.reportOpen = false;
              this.getReportList();
            }).catch(error => {
              console.error('修改监管报告失败:', error);
              this.$message.error('修改监管报告失败，请稍后重试');
            });
          } else {
            addReport(this.reportForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.reportOpen = false;
              this.getReportList();
            }).catch(error => {
              console.error('新增监管报告失败:', error);
              this.$message.error('新增监管报告失败，请稍后重试');
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleReportDelete(row) {
      const reportIds = row.reportId || this.reportIds;
      this.$modal.confirm('是否确认删除监管报告编号为"' + reportIds + '"的数据项？').then(function() {
        return delReport(reportIds);
      }).then(() => {
        this.getReportList();
        this.$modal.msgSuccess("删除成功");
      }).catch((error) => {
        if (error !== 'cancel') {
          console.error('删除监管报告失败:', error);
          this.$message.error('删除监管报告失败，请稍后重试');
        }
      });
    },

    /** 导出按钮操作 */
    handleReportExport() {
      exportReportData(this.reportQueryParams).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = `report_${new Date().getTime()}.xlsx`
        link.click()
        URL.revokeObjectURL(link.href)
        this.$modal.msgSuccess('导出成功')
      }).catch(error => {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      })
    },

    /** 查看报告详情 */
    handleReportView(row) {
      this.reportViewData = row;
      this.reportViewOpen = true;
    },

    /** 生成报告 */
    handleReportGenerate(row) {
      this.$modal.confirm('确认要生成报告"' + row.reportName + '"吗？').then(function() {
        return generateReport(row.reportId);
      }).then(() => {
        this.$modal.msgSuccess("生成成功");
        this.getReportList();
      }).catch((error) => {
        if (error !== 'cancel') {
          console.error('生成报告失败:', error);
          this.$message.error('生成报告失败，请稍后重试');
        }
      });
    },

    /** 验证报告 */
    handleReportValidate(row) {
      this.$modal.confirm('确认要验证报告"' + row.reportName + '"吗？').then(function() {
        return validateReport(row.reportId);
      }).then(() => {
        this.$modal.msgSuccess("验证成功");
        this.getReportList();
      }).catch((error) => {
        if (error !== 'cancel') {
          console.error('验证报告失败:', error);
          this.$message.error('验证报告失败，请稍后重试');
        }
      });
    },

    /** 提交报告 */
    handleReportSubmit(row) {
      this.$modal.confirm('确认要提交报告"' + row.reportName + '"吗？').then(function() {
        return submitReport(row.reportId);
      }).then(() => {
        this.$modal.msgSuccess("提交成功");
        this.getReportList();
      }).catch((error) => {
        if (error !== 'cancel') {
          console.error('提交报告失败:', error);
          this.$message.error('提交报告失败，请稍后重试');
        }
      });
    },

    /** 批量生成报告 */
    handleBatchGenerate() {
      if (this.reportIds.length === 0) {
        this.$modal.msgWarning("请选择要生成的报告");
        return;
      }
      this.$modal.confirm('确认要批量生成选中的报告吗？').then(() => {
        return batchGenerateReports(this.reportIds);
      }).then(() => {
        this.$modal.msgSuccess("批量生成成功");
        this.getReportList();
      }).catch((error) => {
        if (error !== 'cancel') {
          console.error('批量生成报告失败:', error);
          this.$message.error('批量生成报告失败，请稍后重试');
        }
      });
    },

    /** 下拉菜单命令处理 */
    handleCommand(command, row) {
      switch (command) {
        case 'edit':
          this.handleReportUpdate(row);
          break;
        case 'copy':
          this.handleReportCopy(row);
          break;
        case 'download':
          this.handleReportDownload(row);
          break;
        case 'delete':
          this.handleReportDelete(row);
          break;
      }
    },

    /** 复制报告 */
    handleReportCopy(row) {
      this.$prompt('请输入新报告编号', '复制报告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[a-zA-Z0-9_-]+$/,
        inputErrorMessage: '报告编号格式不正确'
      }).then(({ value }) => {
        const newReportName = row.reportName + '_副本';
        copyReport(row.reportId, value, newReportName).then(response => {
          this.$modal.msgSuccess("复制成功");
          this.getReportList();
        }).catch(error => {
          console.error('复制报告失败:', error);
          this.$message.error('复制报告失败，请稍后重试');
        });
      }).catch(() => {});
    },

    /** 下载报告 */
    handleReportDownload(row) {
      exportReportData({
        reportName: row.reportName,
        reportStatus: row.reportStatus
      }).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = `report_${row.reportNo || new Date().getTime()}.xlsx`
        link.click()
        URL.revokeObjectURL(link.href)
        this.$modal.msgSuccess('下载成功')
      }).catch(error => {
        console.error('下载失败:', error)
        this.$message.error('下载失败，请稍后重试')
      })
    },

    // ==================== 辅助方法 ====================
    /** 获取报告状态显示名称 */
    getReportStatusDisplay(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'GENERATED': '已生成',
        'VALIDATED': '已验证',
        'SUBMITTED': '已提交',
        'ACCEPTED': '已接受',
        'REJECTED': '已拒绝'
      };
      return statusMap[status] || status;
    },

    /** 获取报告状态标签类型 */
    getReportStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'GENERATED': 'warning',
        'VALIDATED': 'primary',
        'SUBMITTED': 'success',
        'ACCEPTED': 'success',
        'REJECTED': 'danger'
      };
      return typeMap[status] || '';
    },

    /** 获取验证状态显示名称（从reportStatus推导） */
    getValidationStatusDisplay(status) {
      const statusMap = {
        'DRAFT': '未验证',
        'GENERATED': '待验证',
        'VALIDATED': '已验证',
        'SUBMITTED': '已验证',
        'ACCEPTED': '已验证',
        'REJECTED': '验证失败'
      };
      return statusMap[status] || '未验证';
    },

    /** 获取验证状态标签类型（从reportStatus推导） */
    getValidationStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'GENERATED': 'warning',
        'VALIDATED': 'success',
        'SUBMITTED': 'success',
        'ACCEPTED': 'success',
        'REJECTED': 'danger'
      };
      return typeMap[status] || 'info';
    },

    /** 根据报告状态计算进度百分比 */
    getProgressByStatus(status) {
      const progressMap = {
        'DRAFT': 10,
        'GENERATED': 30,
        'VALIDATED': 60,
        'SUBMITTED': 80,
        'ACCEPTED': 100,
        'REJECTED': 50
      };
      return progressMap[status] || 0;
    },

    /** 获取提交方式显示名称 */
    getSubmissionMethodDisplay(method) {
      const methodMap = {
        'ONLINE': '在线提交',
        'EMAIL': '邮件提交',
        'UPLOAD': '文件上传',
        'MANUAL': '手工提交'
      };
      return methodMap[method] || method;
    },

    /** 检查是否可以生成 */
    canGenerate(row) {
      return row.reportStatus === 'DRAFT';
    },

    /** 检查是否可以验证 */
    canValidate(row) {
      return row.reportStatus === 'GENERATED';
    },

    /** 检查是否可以提交 */
    canSubmit(row) {
      return row.reportStatus === 'VALIDATED';
    },

    // ==================== 合规检查管理方法 ====================

    /** 获取合规检查统计信息 */
    getComplianceStatistics() {
      getComplianceStatistics().then(response => {
        if (response.code === 1 && response.data) {
          const data = response.data;
          this.complianceStatistics = {
            totalRules: data.total || 0,
            failedResults: data.failed || 0,
            passedResults: data.passed || 0,
            complianceScore: data.passRate || 0
          };
        }
      }).catch(() => {
        this.complianceStatistics = {
          totalRules: 0,
          failedResults: 0,
          passedResults: 0,
          complianceScore: 0
        };
      });
    },

    /** 查询合规检查规则列表 */
    getRuleList() {
      this.ruleLoading = true;
      getComplianceRuleList(this.ruleQueryParams).then(response => {
        // 支持多种响应格式: {code: 1, data: {rows: [...], total: N}} 或 {rows: [...], total: N}
        if (response.code === 1 && response.data) {
          this.ruleList = response.data.rows || response.data || [];
          this.ruleTotal = response.data.total || 0;
        } else {
          this.ruleList = response.rows || response.data || response.records || [];
          this.ruleTotal = response.total || response.count || 0;
        }
        this.ruleLoading = false;
      }).catch(error => {
        console.error('获取合规检查规则列表失败:', error);
        this.$message.error('获取规则列表失败，请检查网络连接或稍后重试');
        this.ruleList = [];
        this.ruleTotal = 0;
        this.ruleLoading = false;
      });
    },

    /** 查询合规检查结果列表 */
    getResultList() {
      this.resultLoading = true;
      getComplianceResultList(this.resultQueryParams).then(response => {
        // 支持多种响应格式: {code: 1, data: {rows: [...], total: N}} 或 {rows: [...], total: N}
        if (response.code === 1 && response.data) {
          this.resultList = response.data.rows || response.data || [];
          this.resultTotal = response.data.total || 0;
        } else {
          this.resultList = response.rows || response.data || response.records || [];
          this.resultTotal = response.total || response.count || 0;
        }
        this.resultLoading = false;
      }).catch(error => {
        console.error('获取合规检查结果列表失败:', error);
        this.$message.error('获取结果列表失败，请检查网络连接或稍后重试');
        this.resultList = [];
        this.resultTotal = 0;
        this.resultLoading = false;
      });
    },

    /** 规则查询 */
    handleRuleQuery() {
      this.ruleQueryParams.pageNum = 1;
      this.getRuleList();
    },

    /** 重置规则查询 */
    resetRuleQuery() {
      this.ruleQueryParams = {
        pageNum: 1,
        pageSize: 10,
        ruleCode: null,
        ruleName: null,
        ruleType: null,
        severityLevel: null,
        isEnabled: null
      };
      this.$nextTick(() => {
        if (this.$refs["ruleQueryForm"]) {
          this.$refs["ruleQueryForm"].clearValidate();
        }
      });
      this.handleRuleQuery();
    },

    /** 规则选择变化 */
    handleRuleSelectionChange(selection) {
      this.ruleIds = selection.map(item => item.ruleId);
      this.ruleSingle = selection.length !== 1;
      this.ruleMultiple = !selection.length;
    },

    /** 新增规则 */
    handleRuleAdd() {
      this.resetRuleForm();
      this.ruleOpen = true;
      this.ruleTitle = "添加合规检查规则";
    },

    /** 修改规则 */
    handleRuleUpdate(row) {
      this.resetRuleForm();
      const ruleId = row.ruleId || this.ruleIds[0];
      getComplianceRuleById(ruleId).then(response => {
        if (response.code === 1 && response.data) {
          this.ruleForm = response.data;
        } else {
          this.ruleForm = { ...row };
        }
        this.ruleOpen = true;
        this.ruleTitle = "修改合规检查规则";
      });
    },

    /** 查看规则 */
    handleRuleView(row) {
      this.ruleViewForm = { ...row };
      this.ruleViewOpen = true;
      this.ruleViewTitle = "规则详情";
    },

    /** 删除规则 */
    handleRuleDelete(row) {
      const ruleIds = row.ruleId ? [row.ruleId] : this.ruleIds;
      this.$modal.confirm('是否确认删除选中的合规检查规则？').then(() => {
        return delComplianceRule(ruleIds.join(','));
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
        this.getRuleList();
      }).catch(() => {});
    },

    /** 导出规则 */
    handleRuleExport() {
      exportComplianceRuleData(this.ruleQueryParams).then(response => {
        const blob = new Blob([response], { type: 'application/octet-stream' });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = '合规检查规则_' + new Date().getTime() + '.xlsx';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
      });
    },

    /** 执行规则 */
    handleExecuteRule(row) {
      this.$modal.confirm(`是否确认执行规则"${row.ruleName}"？`).then(() => {
        return executeComplianceRule(row.ruleId);
      }).then(() => {
        this.$modal.msgSuccess("执行成功");
        this.getResultList();
      }).catch(() => {});
    },

    /** 批量执行规则 */
    handleBatchExecuteRules() {
      if (this.ruleIds.length === 0) {
        this.$message.warning('请选择要执行的规则');
        return;
      }
      this.$modal.confirm('是否确认批量执行选中的规则？').then(() => {
        return batchExecuteComplianceRules(this.ruleIds);
      }).then(() => {
        this.$modal.msgSuccess("批量执行成功");
        this.getResultList();
      }).catch(() => {});
    },

    /** 规则状态切换 */
    handleRuleStatusChange(row) {
      const text = row.isEnabled === 1 ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.ruleName + '"规则吗？').then(() => {
        return toggleComplianceRuleStatus(row.ruleId, row.isEnabled);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(() => {
        row.isEnabled = row.isEnabled === 0 ? 1 : 0;
      });
    },

    /** 规则命令处理 */
    handleRuleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleRuleCopy(row);
          break;
        case 'test':
          this.handleRuleTest(row);
          break;
        case 'history':
          this.handleRuleHistory(row);
          break;
        case 'delete':
          this.handleRuleDelete(row);
          break;
      }
    },

    /** 复制规则 */
    handleRuleCopy(row) {
      this.$modal.confirm(`是否确认复制规则"${row.ruleName}"？`).then(() => {
        return copyComplianceRule(row.ruleId);
      }).then(() => {
        this.$modal.msgSuccess("复制成功");
        this.getRuleList();
      }).catch(() => {});
    },

    /** 测试规则 */
    handleRuleTest(row) {
      this.$modal.confirm(`是否确认测试规则"${row.ruleName}"？`).then(() => {
        return executeComplianceRule(row.ruleId);
      }).then(response => {
        this.$modal.msgSuccess("测试执行完成");
        this.getResultList();
      }).catch(() => {});
    },

    /** 规则历史 */
    handleRuleHistory(row) {
      this.ruleHistoryQuery = { pageNum: 1, pageSize: 10, ruleId: row.ruleId };
      this.ruleHistoryTitle = `执行历史 - ${row.ruleName || row.ruleId}`;
      this.ruleHistoryOpen = true;
      this.loadRuleHistory();
    },

    /** 加载规则执行历史 */
    loadRuleHistory() {
      this.ruleHistoryLoading = true;
      getComplianceResultList(this.ruleHistoryQuery).then(response => {
        if (response.code === 1 && response.data) {
          this.ruleHistoryList = response.data.rows || [];
          this.ruleHistoryTotal = response.data.total || 0;
        } else {
          this.ruleHistoryList = [];
          this.ruleHistoryTotal = 0;
        }
      }).catch(() => {
        this.ruleHistoryList = [];
        this.ruleHistoryTotal = 0;
      }).finally(() => {
        this.ruleHistoryLoading = false;
      });
    },

    /** 提交规则表单 */
    submitRuleForm() {
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          if (this.ruleForm.ruleId != null) {
            updateComplianceRule(this.ruleForm).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.ruleOpen = false;
              this.getRuleList();
            });
          } else {
            addComplianceRule(this.ruleForm).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.ruleOpen = false;
              this.getRuleList();
            });
          }
        }
      });
    },

    /** 取消规则 */
    cancelRule() {
      this.ruleOpen = false;
      this.resetRuleForm();
    },

    /** 重置规则表单 */
    resetRuleForm() {
      this.ruleForm = {
        ruleId: null,
        ruleCode: null,
        ruleName: null,
        ruleGroup: null,
        authorityId: null,
        regulationReference: null,
        ruleType: null,
        checkScope: null,
        ruleCondition: null,
        ruleFormula: null,
        thresholdValue: null,
        warningThreshold: null,
        checkFrequency: "DAILY",
        severityLevel: "MEDIUM",
        isEnabled: 1,
        effectiveDate: null,
        expiryDate: null
      };
      this.$nextTick(() => {
        if (this.$refs["ruleForm"]) {
          this.$refs["ruleForm"].clearValidate();
        }
      });
    },

    // ==================== 合规检查结果管理方法 ====================

    /** 结果查询 */
    handleResultQuery() {
      this.resultQueryParams.pageNum = 1;
      this.getResultList();
    },

    /** 重置结果查询 */
    resetResultQuery() {
      this.resultQueryParams = {
        pageNum: 1,
        pageSize: 10,
        ruleId: null,
        reportId: null,
        isPassed: null,
        checkStatus: null,
        severityLevel: null
      };
      this.$nextTick(() => {
        if (this.$refs["resultQueryForm"]) {
          this.$refs["resultQueryForm"].clearValidate();
        }
      });
      this.handleResultQuery();
    },

    /** 结果选择变化 */
    handleResultSelectionChange(selection) {
      this.resultIds = selection.map(item => item.resultId);
      this.resultSingle = selection.length !== 1;
      this.resultMultiple = !selection.length;
    },

    /** 查看结果 */
    handleResultView(row) {
      this.resultViewForm = { ...row };
      this.resultViewOpen = true;
      this.resultViewTitle = "检查结果详情";
    },

    /** 处理结果 */
    handleProcessResult(row) {
      this.$prompt('请输入处理措施', '处理合规检查结果', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputPlaceholder: '请描述具体的处理措施...'
      }).then(({ value }) => {
        return processComplianceResult(row.resultId, value);
      }).then(() => {
        this.$modal.msgSuccess("处理成功");
        this.getResultList();
      }).catch(() => {});
    },

    /** 解决结果 */
    handleResolveResult(row) {
      this.$modal.confirm(`是否确认解决检查结果"${row.resultId}"？`).then(() => {
        return resolveComplianceResult(row.resultId);
      }).then(() => {
        this.$modal.msgSuccess("解决成功");
        this.getResultList();
      }).catch(() => {});
    },

    /** 批量处理结果 */
    handleBatchProcessResults() {
      if (this.resultIds.length === 0) {
        this.$message.warning('请选择要处理的结果');
        return;
      }
      this.$prompt('请输入处理措施', '批量处理合规检查结果', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputPlaceholder: '请描述具体的处理措施...'
      }).then(({ value }) => {
        return batchProcessComplianceResults(this.resultIds);
      }).then(() => {
        this.$modal.msgSuccess("批量处理成功");
        this.getResultList();
      }).catch(() => {});
    },

    /** 批量解决结果 */
    handleBatchResolveResults() {
      if (this.resultIds.length === 0) {
        this.$message.warning('请选择要解决的结果');
        return;
      }
      this.$modal.confirm('是否确认批量解决选中的检查结果？').then(() => {
        return batchResolveComplianceResults(this.resultIds);
      }).then(() => {
        this.$modal.msgSuccess("批量解决成功");
        this.getResultList();
      }).catch(() => {});
    },

    /** 批量升级结果 */
    handleBatchEscalateResults() {
      if (this.resultIds.length === 0) {
        this.$message.warning('请选择要升级的结果');
        return;
      }
      this.$modal.confirm('是否确认批量升级选中的检查结果？').then(() => {
        return batchEscalateComplianceResults(this.resultIds);
      }).then(() => {
        this.$modal.msgSuccess("批量升级成功");
        this.getResultList();
      }).catch(() => {});
    },

    /** 导出结果 */
    handleResultExport() {
      exportComplianceResultData(this.resultQueryParams).then(response => {
        const blob = new Blob([response], { type: 'application/octet-stream' });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = '合规检查结果_' + new Date().getTime() + '.xlsx';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
      });
    },

    /** 结果命令处理 */
    handleResultCommand(command, row) {
      switch (command) {
        case 'escalate':
          this.handleEscalateResult(row);
          break;
        case 'track':
          this.handleTrackResult(row);
          break;
        case 'analysis':
          this.handleAnalysisResult(row);
          break;
        case 'copy':
          this.handleCopyResult(row);
          break;
      }
    },

    /** 升级结果 */
    handleEscalateResult(row) {
      this.$modal.confirm(`是否确认升级检查结果"${row.resultId}"？`).then(() => {
        return escalateComplianceResult(row.resultId);
      }).then(() => {
        this.$modal.msgSuccess("升级成功");
        this.getResultList();
      }).catch(() => {});
    },

    /** 跟踪结果 */
    handleTrackResult(row) {
      getComplianceResultById(row.resultId).then(response => {
        if (response.code === 1 && response.data) {
          this.$alert(`
            <p><b>结果ID:</b> ${response.data.resultId}</p>
            <p><b>检查状态:</b> ${response.data.checkStatus}</p>
            <p><b>是否通过:</b> ${response.data.isPassed === 1 ? '通过' : '未通过'}</p>
            <p><b>处理措施:</b> ${response.data.actionTaken || '暂无'}</p>
            <p><b>是否升级:</b> ${response.data.isEscalated === 1 ? '是' : '否'}</p>
          `, '结果跟踪详情', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '关闭'
          });
        }
      });
    },

    /** 分析结果 */
    handleAnalysisResult(row) {
      getComplianceResultById(row.resultId).then(response => {
        if (response.code === 1 && response.data) {
          const data = response.data;
          this.$alert(`
            <p><b>违规详情:</b> ${data.violationDetails || '暂无'}</p>
            <p><b>严重程度:</b> ${data.severityLevel || '暂无'}</p>
            <p><b>检查时间:</b> ${data.checkTime || '暂无'}</p>
            <p><b>解决时间:</b> ${data.resolvedTime || '暂未解决'}</p>
          `, '结果分析', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '关闭'
          });
        }
      });
    },

    /** 复制结果 */
    handleCopyResult(row) {
      this.$message.info("合规检查结果不支持复制操作");
    },

    // ==================== 合规检查辅助方法 ====================

    /** 获取规则类型显示名称 */
    getRuleTypeDisplay(type) {
      const typeMap = {
        'LIMIT_CHECK': '限额检查',
        'RATIO_CHECK': '比率检查',
        'THRESHOLD_CHECK': '阈值检查',
        'BUSINESS_RULE': '业务规则',
        'DATA_QUALITY': '数据质量'
      };
      return typeMap[type] || type;
    },

    /** 获取规则类型标签类型 */
    getRuleTypeTagType(type) {
      const typeMap = {
        'LIMIT_CHECK': 'primary',
        'RATIO_CHECK': 'success',
        'THRESHOLD_CHECK': 'warning',
        'BUSINESS_RULE': 'info',
        'DATA_QUALITY': 'danger'
      };
      return typeMap[type] || '';
    },

    /** 获取严重程度显示名称 */
    getSeverityDisplay(level) {
      const levelMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高',
        'CRITICAL': '严重'
      };
      return levelMap[level] || level;
    },

    /** 获取严重程度标签类型 */
    getSeverityTagType(level) {
      const typeMap = {
        'LOW': 'info',
        'MEDIUM': 'primary',
        'HIGH': 'warning',
        'CRITICAL': 'danger'
      };
      return typeMap[level] || '';
    },

    /** 获取检查频率显示名称 */
    getFrequencyDisplay(frequency) {
      const frequencyMap = {
        'REAL_TIME': '实时',
        'HOURLY': '每小时',
        'DAILY': '每日',
        'WEEKLY': '每周',
        'MONTHLY': '每月'
      };
      return frequencyMap[frequency] || frequency;
    },

    /** 获取检查结果显示名称 */
    getCheckResultDisplay(result) {
      const resultMap = {
        'PASS': '通过',
        'WARNING': '警告',
        'FAIL': '失败',
        'ERROR': '错误'
      };
      return resultMap[result] || result;
    },

    /** 获取检查结果标签类型 */
    getCheckResultTagType(result) {
      const typeMap = {
        'PASS': 'success',
        'WARNING': 'warning',
        'FAIL': 'danger',
        'ERROR': 'info'
      };
      return typeMap[result] || '';
    },

    /** 获取结果状态显示名称 */
    getResultStatusDisplay(status) {
      const statusMap = {
        'UNPROCESSED': '未处理',
        'PROCESSING': '处理中',
        'PROCESSED': '已处理',
        'RESOLVED': '已解决',
        'ESCALATED': '已升级'
      };
      return statusMap[status] || status;
    },

    /** 获取结果状态标签类型 */
    getResultStatusTagType(status) {
      const typeMap = {
        'UNPROCESSED': 'danger',
        'PROCESSING': 'warning',
        'PROCESSED': 'primary',
        'RESOLVED': 'success',
        'ESCALATED': 'info'
      };
      return typeMap[status] || '';
    },

    /** 获取检查状态显示名称 */
    getCheckStatusDisplay(status) {
      const statusMap = {
        'PENDING': '待检查',
        'CHECKING': '检查中',
        'COMPLETED': '已完成',
        'FAILED': '检查失败',
        'CANCELLED': '已取消',
        'PROCESSED': '已处理',
        'RESOLVED': '已解决',
        'ESCALATED': '已升级'
      };
      return statusMap[status] || status;
    },

    /** 获取检查状态标签类型 */
    getCheckStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info',
        'CHECKING': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'CANCELLED': '',
        'PROCESSED': 'primary',
        'RESOLVED': 'success',
        'ESCALATED': 'warning'
      };
      return typeMap[status] || '';
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.authority-container,
.template-container,
.report-container,
.compliance-container {
  margin-top: 20px;
}

.mb8 {
  margin-bottom: 8px;
}

.mb20 {
  margin-bottom: 20px;
}

.el-table {
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}

.el-alert {
  margin: 20px 0;
}

/* 统计卡片样式 */
.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.stat-icon i {
  font-size: 24px;
  color: #fff;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  line-height: 1;
}

/* 进度条样式 */
.progress-text {
  font-size: 12px;
  color: #606266;
  margin-left: 8px;
}

/* 报告内容样式 */
.report-content {
  background: #f5f7fa;
  padding: 16px;
  border-radius: 4px;
  border-left: 4px solid #409eff;
  margin: 16px 0;
  white-space: pre-wrap;
  word-break: break-word;
  max-height: 200px;
  overflow-y: auto;
}

.validation-result {
  background: #f0f9ff;
  padding: 16px;
  border-radius: 4px;
  border-left: 4px solid #67c23a;
  margin: 16px 0;
  white-space: pre-wrap;
  word-break: break-word;
}

/* 逾期文本样式 */
.text-danger {
  color: #f56c6c;
  font-weight: bold;
}

/* 下拉菜单样式 */
.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
  font-size: 12px;
}

.el-dropdown-link:hover {
  color: #66b1ff;
}

/* 表格操作按钮样式 */
.el-table .el-button--mini {
  padding: 5px 8px;
  font-size: 12px;
}

/* 状态标签样式 */
.el-tag {
  font-size: 12px;
}

/* 描述列表样式 */
.el-descriptions {
  margin: 16px 0;
}

.el-descriptions /deep/ .el-descriptions__label {
  font-weight: bold;
  color: #606266;
}

.el-descriptions /deep/ .el-descriptions__content {
  color: #303133;
}

/* 分割线样式 */
.el-divider {
  margin: 24px 0 16px 0;
}

.el-divider /deep/ .el-divider__text {
  font-weight: bold;
  color: #303133;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .stat-card {
    margin-bottom: 16px;
  }

  .stat-icon {
    width: 50px;
    height: 50px;
    margin-right: 12px;
  }

  .stat-icon i {
    font-size: 20px;
  }

  .stat-number {
    font-size: 24px;
  }

  .stat-label {
    font-size: 12px;
  }
}
</style>
