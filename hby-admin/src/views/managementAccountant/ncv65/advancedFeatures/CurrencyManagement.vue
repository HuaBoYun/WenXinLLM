<template>
  <div class="currency-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>多币种管理</h2>
      <p>多币种预算管理和汇率转换，支持多种货币的预算编制、执行和分析</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleAddCurrency">添加币种</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-refresh-right" @click="handleUpdateRates">更新汇率</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewRateHistory">汇率历史</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">币种设置</el-button>
            <el-button icon="el-icon-document" @click="handleReports">汇率报告</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 币种统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover" @click.native="handleStatCardClick('total')" :class="{ 'stat-active': activeStatFilter === 'total' }">
          <div class="stat-content">
            <div class="stat-number">{{ currencyStats.totalCurrencies }}</div>
            <div class="stat-label">支持币种</div>
            <div class="stat-description">系统支持的币种数</div>
            <div class="stat-trend">
              <i class="el-icon-money"></i>
              <span>全球化</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-money"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover" @click.native="handleStatCardClick('active')" :class="{ 'stat-active': activeStatFilter === 'active' }">
          <div class="stat-content">
            <div class="stat-number">{{ currencyStats.activeCurrencies }}</div>
            <div class="stat-label">活跃币种</div>
            <div class="stat-description">正在使用的币种</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>使用中</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card rate-card" shadow="hover" @click.native="handleStatCardClick('rateUpdates')" :class="{ 'stat-active': activeStatFilter === 'rateUpdates' }">
          <div class="stat-content">
            <div class="stat-number">{{ currencyStats.rateUpdates }}</div>
            <div class="stat-label">汇率更新</div>
            <div class="stat-description">今日汇率更新次数</div>
            <div class="stat-trend">
              <i class="el-icon-refresh"></i>
              <span>实时更新</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-refresh"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card conversion-card" shadow="hover" @click.native="handleStatCardClick('conversions')" :class="{ 'stat-active': activeStatFilter === 'conversions' }">
          <div class="stat-content">
            <div class="stat-number">{{ currencyStats.conversions }}</div>
            <div class="stat-label">转换次数</div>
            <div class="stat-description">今日币种转换次数</div>
            <div class="stat-trend">
              <i class="el-icon-sort"></i>
              <span>高频转换</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-sort"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 币种类型选择 -->
    <el-card class="currency-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>币种类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshCurrencyTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="currencyType in currencyTypes" :key="currencyType.id">
          <el-card 
            class="currency-type-item" 
            shadow="hover" 
            @click.native="handleSelectCurrencyType(currencyType)"
            :class="{ 'selected': selectedCurrencyType === currencyType.id }"
          >
            <div class="currency-type-icon">
              <i :class="currencyType.icon"></i>
            </div>
            <div class="currency-type-title">{{ currencyType.name }}</div>
            <div class="currency-type-description">{{ currencyType.description }}</div>
            <div class="currency-type-stats">
              <span class="currency-count">{{ currencyType.currencyCount }} 种币种</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 币种列表 -->
    <el-card class="currencies-card" shadow="never">
      <div slot="header" class="card-header">
        <span>币种管理</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索币种"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getCurrencyList"
            clearable
            @clear="getCurrencyList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getCurrencyList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="currencyList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="currencyCode" label="币种代码" width="120" align="center">
          <template slot-scope="scope">
            <el-tag type="primary" size="mini">{{ scope.row.currencyCode }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="currencyName" label="币种名称" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.currencyName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="currencySymbol" label="币种符号" width="100" align="center">
          <template slot-scope="scope">
            <span class="currency-symbol">{{ scope.row.currencySymbol }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="exchangeRate" label="汇率" width="120" align="right">
          <template slot-scope="scope">
            <span class="exchange-rate">{{ formatRate(scope.row.exchangeRate) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="rateChange" label="汇率变动" width="120" align="center">
          <template slot-scope="scope">
            <span :class="getRateChangeClass(scope.row.rateChange)">
              <i :class="getRateChangeIcon(scope.row.rateChange)"></i>
              {{ Math.abs(scope.row.rateChange) }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdateTime" label="更新时间" width="150" align="center" />
        <el-table-column prop="isBaseCurrency" label="基准币种" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isBaseCurrency ? 'success' : 'info'" size="mini">
              {{ scope.row.isBaseCurrency ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-refresh"
              @click="handleUpdateRate(scope.row)"
              :disabled="scope.row.isBaseCurrency"
            >更新汇率</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="setBase" :disabled="scope.row.isBaseCurrency">设为基准</el-dropdown-item>
                <el-dropdown-item command="enable">启用</el-dropdown-item>
                <el-dropdown-item command="disable">禁用</el-dropdown-item>
                <el-dropdown-item command="history">汇率历史</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 币种详情抽屉 -->
    <el-drawer
      title="币种详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="60%"
    >
      <div class="detail-content" v-if="currentCurrency">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="币种基本信息" :column="2" border>
              <el-descriptions-item label="币种代码">{{ currentCurrency.currencyCode }}</el-descriptions-item>
              <el-descriptions-item label="币种名称">{{ currentCurrency.currencyName }}</el-descriptions-item>
              <el-descriptions-item label="币种符号">{{ currentCurrency.currencySymbol }}</el-descriptions-item>
              <el-descriptions-item label="当前汇率">{{ formatRate(currentCurrency.exchangeRate) }}</el-descriptions-item>
              <el-descriptions-item label="汇率变动">
                <span :class="getRateChangeClass(currentCurrency.rateChange)">
                  <i :class="getRateChangeIcon(currentCurrency.rateChange)"></i>
                  {{ Math.abs(currentCurrency.rateChange) }}%
                </span>
              </el-descriptions-item>
              <el-descriptions-item label="基准币种">
                <el-tag :type="currentCurrency.isBaseCurrency ? 'success' : 'info'" size="mini">
                  {{ currentCurrency.isBaseCurrency ? '是' : '否' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentCurrency.status)" size="mini">
                  {{ getStatusText(currentCurrency.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="更新时间">{{ currentCurrency.lastUpdateTime }}</el-descriptions-item>
              <el-descriptions-item label="币种描述" :span="2">{{ currentCurrency.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="汇率配置" name="config">
            <el-form label-width="120px" size="small">
              <el-form-item label="汇率来源">
                <el-input :value="currentCurrency.rateSource" readonly />
              </el-form-item>
              <el-form-item label="更新频率">
                <el-input :value="currentCurrency.updateFrequency" readonly />
              </el-form-item>
              <el-form-item label="精度设置">
                <el-input :value="currentCurrency.precisionVal" readonly />
              </el-form-item>
              <el-form-item label="舍入规则">
                <el-input :value="currentCurrency.roundingRule" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="汇率历史" name="history">
            <el-table :data="rateHistory" border size="mini">
              <el-table-column prop="date" label="日期" width="120" />
              <el-table-column prop="exchangeRate" label="汇率" width="120" align="right">
                <template slot-scope="scope">
                  <span class="exchange-rate">{{ formatRate(scope.row.exchangeRate) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="change" label="变动" width="100" align="center">
                <template slot-scope="scope">
                  <span :class="getRateChangeClass(scope.row.change)">
                    <i :class="getRateChangeIcon(scope.row.change)"></i>
                    {{ Math.abs(scope.row.change) }}%
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="source" label="来源" width="120" />
              <el-table-column prop="updateTime" label="更新时间" />
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="使用统计" name="statistics">
            <div id="currencyChart" style="height: 400px;"></div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑币种对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="currencyForm"
        :model="currencyForm"
        :rules="currencyRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="币种代码" prop="currencyCode">
              <el-input v-model="currencyForm.currencyCode" placeholder="如：USD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种名称" prop="currencyName">
              <el-input v-model="currencyForm.currencyName" placeholder="如：美元" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="币种符号" prop="currencySymbol">
              <el-input v-model="currencyForm.currencySymbol" placeholder="如：$" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="初始汇率" prop="exchangeRate">
              <el-input-number v-model="currencyForm.exchangeRate" :precision="4" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="币种类型" prop="currencyType">
              <el-select v-model="currencyForm.currencyType" placeholder="请选择币种类型" style="width: 100%">
                <el-option value="MAJOR" label="主要货币" />
                <el-option value="ASIA" label="亚洲货币" />
                <el-option value="EUROPE" label="欧洲货币" />
                <el-option value="OTHER" label="其他货币" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种状态" prop="currencyStatus">
              <el-select v-model="currencyForm.currencyStatus" placeholder="请选择状态" style="width: 100%">
                <el-option value="ACTIVE" label="活跃" />
                <el-option value="INACTIVE" label="非活跃" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="汇率来源" prop="rateSource">
              <el-select v-model="currencyForm.rateSource" placeholder="请选择汇率来源" style="width: 100%">
                <el-option value="MANUAL" label="手动输入" />
                <el-option value="API" label="API接口" />
                <el-option value="BANK" label="银行汇率" />
                <el-option value="MARKET" label="市场汇率" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="更新频率" prop="updateFrequency">
              <el-select v-model="currencyForm.updateFrequency" placeholder="请选择更新频率" style="width: 100%">
                <el-option value="REAL_TIME" label="实时" />
                <el-option value="HOURLY" label="每小时" />
                <el-option value="DAILY" label="每日" />
                <el-option value="WEEKLY" label="每周" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="精度设置" prop="precisionVal">
              <el-input-number v-model="currencyForm.precisionVal" :min="2" :max="8" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="舍入规则" prop="roundingRule">
              <el-select v-model="currencyForm.roundingRule" placeholder="请选择舍入规则" style="width: 100%">
                <el-option value="ROUND_HALF_UP" label="四舍五入" />
                <el-option value="ROUND_DOWN" label="向下舍入" />
                <el-option value="ROUND_UP" label="向上舍入" />
                <el-option value="ROUND_HALF_EVEN" label="银行家舍入" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="币种描述" prop="description">
          <el-input
            v-model="currencyForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入币种描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 更新汇率弹窗 -->
    <el-dialog title="批量更新汇率" :visible.sync="updateRatesDialogVisible" width="700px" :close-on-click-modal="false">
      <el-table :data="currencyList" border size="mini" max-height="400">
        <el-table-column prop="currencyCode" label="币种代码" width="100" align="center">
          <template slot-scope="scope">
            <el-tag type="primary" size="mini">{{ scope.row.currencyCode }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="currencyName" label="币种名称" width="120" />
        <el-table-column prop="exchangeRate" label="当前汇率" width="120" align="right">
          <template slot-scope="scope">
            {{ formatRate(scope.row.exchangeRate) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-refresh" @click="handleUpdateRate(scope.row)" :disabled="scope.row.isBaseCurrency">更新</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateRatesDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 汇率历史弹窗 -->
    <el-dialog title="汇率历史记录" :visible.sync="rateHistoryDialogVisible" width="800px" :close-on-click-modal="false">
      <el-form :inline="true" size="mini" style="margin-bottom: 10px;">
        <el-form-item label="币种">
          <el-select v-model="rateHistoryQuery.currencyCode" placeholder="全部" clearable style="width: 150px;" @change="loadRateHistoryPage">
            <el-option v-for="c in currencyList" :key="c.currencyCode" :label="c.currencyName + '(' + c.currencyCode + ')'" :value="c.currencyCode" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadRateHistoryPage">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="rateHistoryList" border size="mini" v-loading="rateHistoryLoading" max-height="400">
        <el-table-column prop="currencyCode" label="币种代码" width="100" align="center" />
        <el-table-column prop="currencyName" label="币种名称" width="120" />
        <el-table-column prop="exchangeRate" label="汇率" width="120" align="right">
          <template slot-scope="scope">
            {{ formatRate(scope.row.exchangeRate) }}
          </template>
        </el-table-column>
        <el-table-column prop="rateChange" label="变动" width="100" align="center" />
        <el-table-column prop="rateSource" label="来源" width="100" />
        <el-table-column prop="effectiveDate" label="生效日期" width="160" />
        <el-table-column prop="updateTime" label="记录时间" />
      </el-table>
      <el-pagination
        v-if="rateHistoryTotal > 0"
        style="margin-top: 10px; text-align: right;"
        :current-page.sync="rateHistoryQuery.pageNum"
        :page-size.sync="rateHistoryQuery.pageSize"
        :total="rateHistoryTotal"
        layout="total, prev, pager, next"
        @current-change="loadRateHistoryPage"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="rateHistoryDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 币种设置弹窗 -->
    <el-dialog title="币种设置" :visible.sync="settingsDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form label-width="120px" size="small">
        <el-form-item label="基准币种">
          <el-select v-model="baseCurrencyCode" placeholder="请选择基准币种" style="width: 100%;">
            <el-option v-for="c in currencyList" :key="c.currencyCode" :label="c.currencyName + '(' + c.currencyCode + ')'" :value="c.currencyCode" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveBaseCurrency">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 汇率报告弹窗 -->
    <el-dialog title="汇率报告" :visible.sync="reportDialogVisible" width="700px" :close-on-click-modal="false">
      <el-descriptions title="汇率概览" :column="2" border size="small">
        <el-descriptions-item label="支持币种数">{{ currencyStats.totalCurrencies }}</el-descriptions-item>
        <el-descriptions-item label="活跃币种数">{{ currencyStats.activeCurrencies }}</el-descriptions-item>
        <el-descriptions-item label="今日汇率更新">{{ currencyStats.rateUpdates }} 次</el-descriptions-item>
        <el-descriptions-item label="转换次数">{{ currencyStats.conversions }} 次</el-descriptions-item>
      </el-descriptions>
      <h4 style="margin: 16px 0 8px;">币种汇率一览</h4>
      <el-table :data="currencyList" border size="mini" max-height="300">
        <el-table-column prop="currencyCode" label="代码" width="80" align="center" />
        <el-table-column prop="currencyName" label="名称" width="100" />
        <el-table-column prop="exchangeRate" label="汇率" width="100" align="right">
          <template slot-scope="scope">{{ formatRate(scope.row.exchangeRate) }}</template>
        </el-table-column>
        <el-table-column prop="rateChange" label="变动(%)" width="100" align="center">
          <template slot-scope="scope">
            <span :class="getRateChangeClass(scope.row.rateChange)">{{ scope.row.rateChange }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdateTime" label="更新时间" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="reportDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="多币种管理帮助" :visible.sync="helpDialogVisible" width="600px" :close-on-click-modal="false">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>多币种管理模块支持企业在全球化业务中管理多种货币的预算编制、汇率维护和币种转换。</p>
        <h4>操作指南</h4>
        <ul>
          <li><b>添加币种</b>：点击"添加币种"按钮，填写币种代码（3位大写字母）、名称、符号等信息。</li>
          <li><b>更新汇率</b>：点击"更新汇率"按钮或列表中的更新按钮，可手动更新币种汇率。</li>
          <li><b>设为基准</b>：在列表操作中选择"设为基准"，将该币种设为基准货币。</li>
          <li><b>启用/禁用</b>：控制币种的使用状态。</li>
          <li><b>汇率历史</b>：查看币种的历史汇率变动记录。</li>
          <li><b>币种类型筛选</b>：点击币种类型卡片可按类型筛选列表。</li>
          <li><b>统计卡片筛选</b>：点击顶部统计卡片可按状态筛选列表。</li>
        </ul>
        <h4>币种类型说明</h4>
        <ul>
          <li><b>主要货币</b>：USD、EUR、GBP、JPY等全球主要流通货币。</li>
          <li><b>亚洲货币</b>：CNY、JPY、KRW、SGD等亚洲地区货币。</li>
          <li><b>欧洲货币</b>：EUR、GBP、CHF等欧洲地区货币。</li>
          <li><b>其他货币</b>：其他地区或特殊用途货币。</li>
        </ul>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="helpDialogVisible = false">知道了</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'
import * as echarts from 'echarts'

export default {
  name: 'CurrencyManagement',
  data() {
    return {
      // 统计数据
      currencyStats: {
        totalCurrencies: 0,
        activeCurrencies: 0,
        rateUpdates: 0,
        conversions: 0
      },

      // 币种类型
      currencyTypes: [
        { id: 1, name: '主要货币', description: '全球主要流通货币', icon: 'el-icon-star-on', currencyCount: 0 },
        { id: 2, name: '亚洲货币', description: '亚洲地区货币', icon: 'el-icon-location', currencyCount: 0 },
        { id: 3, name: '欧洲货币', description: '欧洲地区货币', icon: 'el-icon-location-outline', currencyCount: 0 },
        { id: 4, name: '其他货币', description: '其他地区货币', icon: 'el-icon-more', currencyCount: 0 }
      ],
      selectedCurrencyType: null,
      
      // 币种列表
      currencyList: [],
      loading: false,
      searchKeyword: '',
      
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentCurrency: null,
      rateHistory: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      
      // 表单数据
      currencyForm: {
        currencyId: '',
        currencyCode: '',
        currencyName: '',
        currencySymbol: '',
        exchangeRate: 1.0000,
        currencyType: '',
        currencyStatus: 'ACTIVE',
        rateSource: '',
        updateFrequency: '',
        precisionVal: 4,
        roundingRule: '',
        description: ''
      },

      // 币种类型映射
      currencyTypeMap: {
        1: 'MAJOR',
        2: 'ASIA',
        3: 'EUROPE',
        4: 'OTHER'
      },

      // 更新汇率弹窗
      updateRatesDialogVisible: false,
      updateRatesLoading: false,
      updateRatesList: [],

      // 汇率历史弹窗
      rateHistoryDialogVisible: false,
      rateHistoryLoading: false,
      rateHistoryList: [],
      rateHistoryTotal: 0,
      rateHistoryQuery: { pageNum: 1, pageSize: 10, currencyCode: '' },

      // 币种设置弹窗
      settingsDialogVisible: false,
      baseCurrencyCode: '',
      allCurrenciesForSettings: [],

      // 汇率报告弹窗
      reportDialogVisible: false,
      reportData: {},

      // 帮助弹窗
      helpDialogVisible: false,

      // 统计筛选状态
      activeStatFilter: '',

      // 表单验证规则
      currencyRules: {
        currencyCode: [
          { required: true, message: '请输入币种代码', trigger: 'blur' },
          { pattern: /^[A-Z]{3}$/, message: '币种代码必须为3位大写字母', trigger: 'blur' }
        ],
        currencyName: [
          { required: true, message: '请输入币种名称', trigger: 'blur' }
        ],
        currencySymbol: [
          { required: true, message: '请输入币种符号', trigger: 'blur' }
        ],
        rateSource: [
          { required: true, message: '请选择汇率来源', trigger: 'change' }
        ]
      }
    }
  },
  
  created() {
    this.getCurrencyList()
    this.getCurrencyStats()
  },
  
  methods: {
    // 获取币种列表
    async getCurrencyList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword }
        if (this.selectedCurrencyType) {
          const typeStr = this.currencyTypeMap[this.selectedCurrencyType]
          if (typeStr) {
            params.currencyType = typeStr
          }
        }
        if (this.activeStatFilter) {
          // 映射统计卡片筛选到后端参数
          if (this.activeStatFilter === 'active') {
            params.currencyStatus = 'ACTIVE'
          }
          // total/rateUpdates/conversions 不做状态筛选，只是视觉高亮
        }
        const response = await advancedFeaturesApi.getCurrencyList(params)
        if (response && response.code === 1 && response.data) {
          this.currencyList = response.data.list || response.data || []
        }
      } catch (error) {
        this.$message.error('获取币种列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getCurrencyStats() {
      try {
        const response = await advancedFeaturesApi.getCurrencyStats()
        if (response && response.code === 1 && response.data) {
          this.currencyStats = response.data
          // 更新币种类型计数
          this.currencyTypes[0].currencyCount = response.data.majorCount || 0
          this.currencyTypes[1].currencyCount = response.data.asiaCount || 0
          this.currencyTypes[2].currencyCount = response.data.europeCount || 0
          this.currencyTypes[3].currencyCount = response.data.otherCount || 0
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },
    
    // 添加币种
    handleAddCurrency() {
      this.dialogTitle = '添加币种'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑币种
    handleEdit(row) {
      this.dialogTitle = '编辑币种'
      this.dialogVisible = true
      this.currencyForm = {
        currencyId: row.currencyId || row.id,
        currencyCode: row.currencyCode,
        currencyName: row.currencyName,
        currencySymbol: row.currencySymbol,
        exchangeRate: row.exchangeRate,
        rateSource: row.rateSource,
        updateFrequency: row.updateFrequency,
        precisionVal: row.precisionVal,
        roundingRule: row.roundingRule,
        description: row.description,
        currencyType: row.currencyType,
        currencyStatus: row.status || row.currencyStatus || 'ACTIVE'
      }
    },
    
    // 查看详情
    async handleView(row) {
      this.currentCurrency = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await this.getRateHistory(row.currencyCode)
    },
    
    // 获取汇率历史
    async getRateHistory(currencyCode) {
      try {
        const response = await advancedFeaturesApi.getCurrencyRateHistory({ currencyCode })
        if (response && response.code === 1 && response.data) {
          this.rateHistory = response.data.tlist || response.data.list || response.data || []
        }
      } catch (error) {
        console.error('获取汇率历史失败：', error)
      }
    },
    
    // 更新汇率
    async handleUpdateRate(row) {
      this.$confirm('确定更新该币种汇率吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.updateCurrencyRate(row.currencyCode)
          this.$message.success('汇率更新成功')
          this.getCurrencyList()
        } catch (error) {
          this.$message.error('更新失败：' + error.message)
        }
      })
    },
    
    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'setBase':
          this.handleSetBaseCurrency(row)
          break
        case 'enable':
          this.handleEnableCurrency(row)
          break
        case 'disable':
          this.handleDisableCurrency(row)
          break
        case 'history':
          this.handleViewHistory(row)
          break
        case 'delete':
          this.handleDeleteCurrency(row)
          break
      }
    },
    
    // 设为基准币种
    async handleSetBaseCurrency(row) {
      this.$confirm('确定将该币种设为基准币种吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.setBaseCurrency(row.currencyCode)
          this.$message.success('设置成功')
          this.getCurrencyList()
          this.getCurrencyStats()
        } catch (error) {
          this.$message.error('设置失败：' + error.message)
        }
      })
    },
    
    // 启用币种
    async handleEnableCurrency(row) {
      try {
        await advancedFeaturesApi.enableCurrency(row.currencyId || row.id)
        this.$message.success('启用成功')
        this.getCurrencyList()
        this.getCurrencyStats()
      } catch (error) {
        this.$message.error('启用失败：' + error.message)
      }
    },
    
    // 禁用币种
    async handleDisableCurrency(row) {
      try {
        await advancedFeaturesApi.disableCurrency(row.currencyId || row.id)
        this.$message.success('禁用成功')
        this.getCurrencyList()
        this.getCurrencyStats()
      } catch (error) {
        this.$message.error('禁用失败：' + error.message)
      }
    },
    
    // 查看历史
    handleViewHistory(row) {
      this.handleView(row)
      this.detailActiveTab = 'history'
    },
    
    // 删除币种
    handleDeleteCurrency(row) {
      this.$confirm('确定删除该币种吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteCurrency(row.currencyId || row.id)
          this.$message.success('删除成功')
          this.getCurrencyList()
          this.getCurrencyStats()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },
    
    // 提交表单
    async handleSubmitForm() {
      this.$refs.currencyForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            if (this.currencyForm.currencyId) {
              await advancedFeaturesApi.updateCurrency(this.currencyForm)
              this.$message.success('更新成功')
            } else {
              await advancedFeaturesApi.createCurrency(this.currencyForm)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getCurrencyList()
            this.getCurrencyStats()
          } catch (error) {
            this.$message.error('操作失败：' + error.message)
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    
    // 重置表单
    resetForm() {
      this.currencyForm = {
        currencyId: '',
        currencyCode: '',
        currencyName: '',
        currencySymbol: '',
        exchangeRate: 1.0000,
        rateSource: '',
        updateFrequency: '',
        precisionVal: 4,
        roundingRule: '',
        description: '',
        currencyType: '',
        currencyStatus: 'ACTIVE'
      }
      this.$nextTick(() => {
        this.$refs.currencyForm && this.$refs.currencyForm.clearValidate()
      })
    },
    
    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },
    
    // 刷新
    handleRefresh() {
      this.getCurrencyList()
      this.getCurrencyStats()
    },
    
    // 更新汇率
    async handleUpdateRates() {
      this.updateRatesDialogVisible = true
      this.updateRatesLoading = true
      try {
        const response = await advancedFeaturesApi.getCurrencyList({})
        if (response && response.code === 1 && response.data) {
          this.updateRatesList = (response.data.list || response.data || []).filter(c => !c.isBaseCurrency)
        }
      } catch (error) {
        this.$message.error('获取币种列表失败')
      } finally {
        this.updateRatesLoading = false
      }
    },
    
    // 查看汇率历史
    async handleViewRateHistory() {
      this.rateHistoryDialogVisible = true
      this.rateHistoryQuery = { pageNum: 1, pageSize: 10, currencyCode: '' }
      await this.loadRateHistoryPage()
    },

    // 加载汇率历史分页
    async loadRateHistoryPage() {
      this.rateHistoryLoading = true
      try {
        const response = await advancedFeaturesApi.getExchangeRateHistoryPage(this.rateHistoryQuery)
        if (response && response.code === 1 && response.data) {
          this.rateHistoryList = response.data.tlist || response.data.list || []
          this.rateHistoryTotal = response.data.totalRecord || response.data.total || 0
        }
      } catch (error) {
        this.$message.error('获取汇率历史失败')
      } finally {
        this.rateHistoryLoading = false
      }
    },

    // 汇率历史分页变化
    handleRateHistoryPageChange(page) {
      this.rateHistoryQuery.pageNum = page
      this.loadRateHistoryPage()
    },
    
    // 币种设置
    async handleSettings() {
      this.settingsDialogVisible = true
      try {
        const response = await advancedFeaturesApi.getCurrencyList({})
        if (response && response.code === 1 && response.data) {
          this.allCurrenciesForSettings = response.data.list || response.data || []
          const baseCurrency = this.allCurrenciesForSettings.find(c => c.isBaseCurrency)
          this.baseCurrencyCode = baseCurrency ? baseCurrency.currencyCode : ''
        }
      } catch (error) {
        this.$message.error('获取币种列表失败')
      }
    },

    // 保存基准币种设置
    async handleSaveBaseCurrency() {
      if (!this.baseCurrencyCode) {
        this.$message.warning('请选择基准币种')
        return
      }
      try {
        await advancedFeaturesApi.setBaseCurrency(this.baseCurrencyCode)
        this.$message.success('基准币种设置成功')
        this.settingsDialogVisible = false
        this.getCurrencyList()
        this.getCurrencyStats()
      } catch (error) {
        this.$message.error('设置失败：' + error.message)
      }
    },
    
    // 汇率报告
    async handleReports() {
      this.reportDialogVisible = true
      try {
        const statsRes = await advancedFeaturesApi.getCurrencyStats()
        const listRes = await advancedFeaturesApi.getCurrencyList({})
        this.reportData = {
          stats: statsRes && statsRes.code === 1 ? statsRes.data : {},
          list: listRes && listRes.code === 1 ? (listRes.data.list || listRes.data || []) : []
        }
      } catch (error) {
        this.$message.error('获取报告数据失败')
      }
    },
    
    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },
    
    // 刷新币种类型
    refreshCurrencyTypes() {
      this.getCurrencyList()
      this.getCurrencyStats()
      this.$message.success('已刷新')
    },

    // 选择币种类型
    handleSelectCurrencyType(currencyType) {
      if (this.selectedCurrencyType === currencyType.id) {
        this.selectedCurrencyType = null
      } else {
        this.selectedCurrencyType = currencyType.id
      }
      this.getCurrencyList()
    },

    // 统计卡片点击筛选
    handleStatCardClick(filterType) {
      if (this.activeStatFilter === filterType) {
        this.activeStatFilter = ''
      } else {
        this.activeStatFilter = filterType
      }
      this.selectedCurrencyType = null
      this.getCurrencyList()
    },

    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },
    
    // 格式化汇率
    formatRate(rate) {
      return parseFloat(rate).toFixed(4)
    },
    
    // 获取汇率变动样式类
    getRateChangeClass(change) {
      if (change > 0) return 'rate-up'
      if (change < 0) return 'rate-down'
      return 'rate-stable'
    },
    
    // 获取汇率变动图标
    getRateChangeIcon(change) {
      if (change > 0) return 'el-icon-top'
      if (change < 0) return 'el-icon-bottom'
      return 'el-icon-minus'
    },
    
    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'DISABLED': 'danger'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'INACTIVE': '非活跃',
        'DISABLED': '禁用'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.currency-management {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;

    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .toolbar-card {
    margin-bottom: 20px;

    .text-right {
      text-align: right;
    }
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;

      &.total-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }

      &.active-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.rate-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.conversion-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
        color: white;
      }

      .stat-content {
        position: relative;
        z-index: 2;

        .stat-number {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 2px;
        }

        .stat-description {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }

        .stat-trend {
          font-size: 12px;
          opacity: 0.9;
        }
      }

      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
      }

      &.stat-active {
        transform: translateY(-3px);
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.25);
        cursor: pointer;
      }

      cursor: pointer;
    }
  }

  .currency-types-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .currency-type-item {
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }

      &.selected {
        border-color: #409EFF;
        background: #F0F8FF;
      }

      .currency-type-icon {
        width: 48px;
        height: 48px;
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 12px;

        i {
          font-size: 24px;
          color: white;
        }
      }

      .currency-type-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .currency-type-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
      }

      .currency-type-stats {
        .currency-count {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .currencies-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        display: flex;
        align-items: center;
      }
    }

    .currency-symbol {
      font-size: 16px;
      font-weight: 600;
      color: #409EFF;
    }

    .exchange-rate {
      font-family: 'Courier New', monospace;
      font-weight: 500;
      color: #303133;
    }

    .rate-up {
      color: #67C23A;
      font-weight: 500;
    }

    .rate-down {
      color: #F56C6C;
      font-weight: 500;
    }

    .rate-stable {
      color: #909399;
      font-weight: 500;
    }
  }

  .detail-content {
    padding: 20px;

    .exchange-rate {
      font-family: 'Courier New', monospace;
      font-weight: 500;
    }

    .rate-up {
      color: #67C23A;
      font-weight: 500;
    }

    .rate-down {
      color: #F56C6C;
      font-weight: 500;
    }

    .rate-stable {
      color: #909399;
      font-weight: 500;
    }
  }

  .dialog-footer {
    text-align: right;
  }

  .help-content {
    h4 {
      color: #303133;
      margin: 16px 0 8px;
      &:first-child { margin-top: 0; }
    }
    p { color: #606266; line-height: 1.8; }
    ul {
      padding-left: 20px;
      li {
        color: #606266;
        line-height: 2;
      }
    }
  }
}
</style>
