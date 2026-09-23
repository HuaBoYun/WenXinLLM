<template>
  <div class="financial-product-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-goods"></i>
            金融产品管理
          </h2>
          <p class="page-description">管理各类金融产品信息，包括产品定义、风险等级、收益配置和生命周期管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增产品
          </el-button>
          <el-button type="success" icon="el-icon-upload2" @click="handleImport">
            批量导入
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 产品概览卡片 -->
    <div class="product-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总产品数</div>
                <div class="card-value">{{ totalProducts }}</div>
                <div class="card-change">已上架产品</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">在售产品</div>
                <div class="card-value">{{ activeProducts }}</div>
                <div class="card-change positive">正常销售</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon risk-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">高风险产品</div>
                <div class="card-value">{{ highRiskProducts }}</div>
                <div class="card-change negative">需要关注</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon revenue-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">平均收益率</div>
                <div class="card-value">{{ avgYield }}%</div>
                <div class="card-change">年化收益</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="产品代码">
            <el-input
              v-model="listQuery.productCode"
              placeholder="请输入产品代码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="产品名称">
            <el-input
              v-model="listQuery.productName"
              placeholder="请输入产品名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="产品类型">
            <el-select
              v-model="listQuery.productType"
              placeholder="请选择产品类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="银行理财" value="BANK_WEALTH" />
              <el-option label="债券" value="BOND" />
              <el-option label="股票" value="EQUITY" />
              <el-option label="基金" value="FUND" />
              <el-option label="衍生品" value="DERIVATIVE" />
            </el-select>
          </el-form-item>
          <el-form-item label="发行机构">
            <el-input
              v-model="listQuery.issuer"
              placeholder="请输入发行机构"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="币种">
            <el-select
              v-model="listQuery.currencyCode"
              placeholder="请选择币种"
              clearable
              style="width: 100px;"
            >
              <el-option label="人民币" value="CNY" />
              <el-option label="美元" value="USD" />
              <el-option label="欧元" value="EUR" />
              <el-option label="日元" value="JPY" />
            </el-select>
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
            </el-select>
          </el-form-item>
          <el-form-item label="产品状态">
            <el-select
              v-model="listQuery.productStatus"
              placeholder="请选择产品状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="活跃" value="ACTIVE" />
              <el-option label="非活跃" value="INACTIVE" />
              <el-option label="暂停" value="SUSPENDED" />
              <el-option label="到期" value="MATURED" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <div class="table-title">
          <span class="title-text">金融产品列表</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="getList">刷新</el-button>
            <el-button size="small" icon="el-icon-setting" @click="handleTableSetting">设置</el-button>
          </el-button-group>
        </div>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        :row-class-name="tableRowClassName"
        @selection-change="handleSelectionChange"
      >
        <!-- 调试信息 -->
        <div v-if="list.length === 0" slot="empty">
          <div style="padding: 20px; color: #999;">
            <i class="el-icon-warning"></i>
            <span>暂无数据 (list.length: {{ list.length }})</span>
          </div>
        </div>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />

        <!-- 动态列 -->
        <el-table-column
          v-for="column in getVisibleColumns()"
          :key="column.key"
          :prop="column.key"
          :label="column.label"
          :width="column.width"
          align="center"
          show-overflow-tooltip
        >
          <template slot-scope="{row}">
            <!-- 产品类型：使用tag标签 -->
            <el-tag v-if="column.key === 'productType'" :type="getProductTypeColor(row.productType)" size="small">
              {{ getProductTypeText(row.productType) }}
            </el-tag>

            <!-- 风险等级：使用tag标签 -->
            <el-tag v-else-if="column.key === 'riskLevel'" :type="getRiskLevelColor(row.riskLevel)" size="small">
              {{ getRiskLevelText(row.riskLevel) }}
            </el-tag>

            <!-- 产品状态：使用tag标签 -->
            <el-tag v-else-if="column.key === 'productStatus'" :type="getProductStatusColor(row.productStatus)" size="small">
              {{ getProductStatusText(row.productStatus) }}
            </el-tag>

            <!-- 预期收益率：带百分号 -->
            <span v-else-if="column.key === 'expectedReturnRate'" class="rate-value">
              {{ row.expectedReturnRate }}%
            </span>

            <!-- 投资期限：带单位 -->
            <span v-else-if="column.key === 'investmentTerm'">
              {{ row.investmentTerm ? row.investmentTerm + getTermUnitText(row.termUnit) : '灵活' }}
            </span>

            <!-- 日期字段：格式化显示 -->
            <span v-else-if="column.key === 'subscriptionStartDate' || column.key === 'subscriptionEndDate'">
              {{ row[column.key] | parseTime('{y}-{m}-{d}') }}
            </span>

            <!-- 币种：显示中文 -->
            <span v-else-if="column.key === 'currencyCode'">
              {{ getCurrencyText(row.currencyCode) }}
            </span>

            <!-- 创建时间：带图标 -->
            <span v-else-if="column.key === 'createTime'" class="create-time">
              <i class="el-icon-time"></i>
              {{ formatTime(row.createTime) }}
            </span>

            <!-- 金额字段：格式化显示 -->
            <span v-else-if="column.key === 'minInvestmentAmount' || column.key === 'maxInvestmentAmount'">
              {{ formatMoney(row[column.key]) }}
            </span>

            <!-- 普通文本字段 -->
            <span v-else>{{ row[column.key] }}</span>
          </template>
        </el-table-column>

        <!-- 操作列（固定） -->
        <el-table-column label="操作" align="center" width="240" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">
                编辑
              </el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(row)">
                详情
              </el-button>
              <el-button type="success" size="mini" icon="el-icon-copy-document" @click="handleCopy(row)">
                复制
              </el-button>
              <el-button
                v-if="row.status !== 'deleted'"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                @click="handleDelete(row,$index)"
              >
                删除
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
      </div>
    </el-card>

    <!-- 创建/编辑金融产品对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新增金融产品' : '编辑金融产品'" :visible.sync="dialogFormVisible" width="1200px" :close-on-click-modal="false">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="产品代码" prop="productCode">
                  <el-input v-model="temp.productCode" placeholder="请输入产品代码" :disabled="dialogStatus === 'update'" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="产品名称" prop="productName">
                  <el-input v-model="temp.productName" placeholder="请输入产品名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="产品类型" prop="productType">
                  <el-select v-model="temp.productType" placeholder="请选择产品类型" style="width: 100%;">
                    <el-option label="银行理财" value="BANK_WEALTH" />
                    <el-option label="债券" value="BOND" />
                    <el-option label="股票" value="EQUITY" />
                    <el-option label="基金" value="FUND" />
                    <el-option label="衍生品" value="DERIVATIVE" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发行机构" prop="issuer">
                  <el-input v-model="temp.issuer" placeholder="请输入发行机构" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="币种" prop="currencyCode">
                  <el-select v-model="temp.currencyCode" placeholder="请选择币种" style="width: 100%;">
                    <el-option label="人民币" value="CNY" />
                    <el-option label="美元" value="USD" />
                    <el-option label="欧元" value="EUR" />
                    <el-option label="日元" value="JPY" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="风险等级" prop="riskLevel">
                  <el-select v-model="temp.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
                    <el-option label="低风险" value="LOW" />
                    <el-option label="中风险" value="MEDIUM" />
                    <el-option label="高风险" value="HIGH" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <el-tab-pane label="投资信息" name="investment">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="最小投资金额" prop="minInvestmentAmount">
                  <el-input-number v-model="temp.minInvestmentAmount" :min="0" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="最大投资金额" prop="maxInvestmentAmount">
                  <el-input-number v-model="temp.maxInvestmentAmount" :min="0" style="width: 100%;" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="预期收益率(%)" prop="expectedReturnRate">
                  <el-input-number v-model="temp.expectedReturnRate" :min="0" :max="100" :precision="2" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="投资期限" prop="investmentTerm">
                  <el-input-number v-model="temp.investmentTerm" :min="0" style="width: 200px;" />
                  <el-select v-model="temp.termUnit" placeholder="期限单位" style="width: 100px; margin-left: 10px;">
                    <el-option label="天" value="DAY" />
                    <el-option label="月" value="MONTH" />
                    <el-option label="年" value="YEAR" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="认购开始日期" prop="subscriptionStartDate">
                  <el-date-picker v-model="temp.subscriptionStartDate" type="date" placeholder="选择日期" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="认购结束日期" prop="subscriptionEndDate">
                  <el-date-picker v-model="temp.subscriptionEndDate" type="date" placeholder="选择日期" style="width: 100%;" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <el-tab-pane label="状态设置" name="status">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="产品状态" prop="productStatus">
                  <el-select v-model="temp.productStatus" placeholder="请选择产品状态" style="width: 100%;">
                    <el-option label="活跃" value="ACTIVE" />
                    <el-option label="非活跃" value="INACTIVE" />
                    <el-option label="暂停" value="SUSPENDED" />
                    <el-option label="到期" value="MATURED" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否允许提前赎回" prop="earlyRedemption">
                  <el-switch v-model="temp.earlyRedemption" :active-value="1" :inactive-value="0" active-text="是" inactive-text="否" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="产品描述">
              <el-input v-model="temp.description" type="textarea" :rows="4" placeholder="请输入产品描述" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 产品详情对话框 -->
    <el-dialog title="产品详情" :visible.sync="viewDialogVisible" width="1000px" :close-on-click-modal="false">
      <div v-if="selectedProduct" class="product-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="产品代码">{{ selectedProduct.productCode }}</el-descriptions-item>
          <el-descriptions-item label="产品名称">{{ selectedProduct.productName }}</el-descriptions-item>
          <el-descriptions-item label="产品类型">
            <el-tag :type="getProductTypeColor(selectedProduct.productType)" size="small">
              {{ getProductTypeText(selectedProduct.productType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="发行机构">{{ selectedProduct.issuer }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ getCurrencyText(selectedProduct.currencyCode) }}</el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="getRiskLevelColor(selectedProduct.riskLevel)" size="small">
              {{ getRiskLevelText(selectedProduct.riskLevel) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="最小投资金额">{{ formatMoney(selectedProduct.minInvestmentAmount) }}</el-descriptions-item>
          <el-descriptions-item label="最大投资金额">{{ formatMoney(selectedProduct.maxInvestmentAmount) }}</el-descriptions-item>
          <el-descriptions-item label="预期收益率">{{ selectedProduct.expectedReturnRate }}%</el-descriptions-item>
          <el-descriptions-item label="投资期限">{{ selectedProduct.investmentTerm }}{{ getTermUnitText(selectedProduct.termUnit) }}</el-descriptions-item>
          <el-descriptions-item label="认购开始日期">{{ selectedProduct.subscriptionStartDate | parseTime('{y}-{m}-{d}') }}</el-descriptions-item>
          <el-descriptions-item label="认购结束日期">{{ selectedProduct.subscriptionEndDate | parseTime('{y}-{m}-{d}') }}</el-descriptions-item>
          <el-descriptions-item label="产品状态">
            <el-tag :type="getProductStatusColor(selectedProduct.productStatus)" size="small">
              {{ getProductStatusText(selectedProduct.productStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="允许提前赎回">{{ selectedProduct.earlyRedemption === 1 ? '是' : '否' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间" span="2">{{ selectedProduct.createTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</el-descriptions-item>
          <el-descriptions-item label="产品描述" span="2">{{ selectedProduct.description }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 复制产品对话框 -->
    <el-dialog title="复制产品" :visible.sync="copyDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form ref="copyForm" :rules="copyRules" :model="copyForm" label-position="right" label-width="100px">
        <el-form-item label="源产品代码">
          <el-input v-model="copyForm.sourceProductCode" disabled />
        </el-form-item>
        <el-form-item label="源产品名称">
          <el-input v-model="copyForm.sourceProductName" disabled />
        </el-form-item>
        <el-form-item label="新产品代码" prop="newProductCode">
          <el-input v-model="copyForm.newProductCode" placeholder="请输入新产品代码" />
        </el-form-item>
        <el-form-item label="新产品名称" prop="newProductName">
          <el-input v-model="copyForm.newProductName" placeholder="请输入新产品名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmCopy" :loading="copyLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 导入文件对话框 -->
    <el-dialog title="批量导入产品" :visible.sync="importDialogVisible" width="500px" :close-on-click-modal="false">
      <el-upload
        class="upload-demo"
        drag
        action=""
        :auto-upload="false"
        :on-change="handleFileChange"
        :file-list="fileList"
        accept=".xlsx,.xls,.csv"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls/csv文件，且不超过10MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmImport" :loading="importLoading">导入</el-button>
      </div>
    </el-dialog>

    <!-- 导出配置对话框 -->
    <el-dialog title="导出产品配置" :visible.sync="exportDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form ref="exportForm" :model="exportForm" label-position="right" label-width="100px">
        <el-form-item label="导出范围" prop="exportRange">
          <el-radio-group v-model="exportForm.exportRange">
            <el-radio label="all">全部数据</el-radio>
            <el-radio label="filtered">当前筛选结果</el-radio>
            <el-radio label="selected">选中数据</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="文件格式" prop="fileFormat">
          <el-radio-group v-model="exportForm.fileFormat">
            <el-radio label="xlsx">Excel格式(.xlsx)</el-radio>
            <el-radio label="csv">CSV格式(.csv)</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="exportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmExport" :loading="exportLoading">导出</el-button>
      </div>
    </el-dialog>

    <!-- 表格设置对话框 -->
    <el-dialog title="表格设置" :visible.sync="tableSettingDialogVisible" width="700px" :close-on-click-modal="false">
      <div class="table-setting-content">
        <!-- 每页显示行数 -->
        <div class="setting-section">
          <div class="section-title">
            <i class="el-icon-s-grid"></i>
            <span>每页显示行数</span>
          </div>
          <el-select v-model="listQuery.limit" placeholder="请选择" style="width: 200px;">
            <el-option
              v-for="item in pageSizeOptions"
              :key="item"
              :label="item + ' 条/页'"
              :value="item">
            </el-option>
          </el-select>
        </div>

        <el-divider></el-divider>

        <!-- 列显示设置 -->
        <div class="setting-section">
          <div class="section-title">
            <i class="el-icon-menu"></i>
            <span>列显示设置</span>
            <el-button type="text" size="small" @click="toggleAllColumns(true)" style="margin-left: 20px;">全选</el-button>
            <el-button type="text" size="small" @click="toggleAllColumns(false)">取消全选</el-button>
          </div>
          <div class="column-list">
            <div
              v-for="(column, index) in allColumns"
              :key="column.key"
              class="column-item"
              draggable="true"
              @dragstart="handleDragStart($event, column)"
              @dragend="handleDragEnd"
              @dragover="handleDragOver($event)"
              @drop="handleDrop($event, column)"
              :class="{ 'dragging': currentDragColumn && currentDragColumn.key === column.key }"
            >
              <i class="el-icon-rank drag-handle"></i>
              <el-checkbox v-model="column.visible" @change="toggleColumnVisibility(column)">
                {{ column.label }}
              </el-checkbox>
              <el-tag size="mini" type="info" style="margin-left: 10px;">{{ column.width }}px</el-tag>
            </div>
          </div>
          <div class="section-tips">
            <i class="el-icon-info"></i>
            <span>提示：拖拽列名可调整显示顺序，取消勾选可隐藏该列</span>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetTableSettings" style="float: left;">重置默认</el-button>
        <el-button @click="tableSettingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveTableSettings">确定保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils' // 导入 parseTime 工具函数
import {
  getFinancialProductList,
  createFinancialProduct,
  updateFinancialProduct,
  deleteFinancialProduct,
  checkFinancialProductCodeUnique,
  copyFinancialProduct,
  exportFinancialProducts,
  importFinancialProducts,
  getFinancialProductStatistics,
  updateFinancialProductStatus,
  getFinancialProductDetail
} from '@/api/globalTreasurer/financialProductDefinition/financialProductManage'

export default {
  name: 'FinancialProductManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    },
    // 添加日期格式化过滤器
    parseTime(time, pattern) {
      if (!time) return ''
      return parseTime(time, pattern)
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        productCode: undefined,
        productName: undefined,
        productType: undefined,
        issuer: undefined,
        currencyCode: undefined,
        riskLevel: undefined,
        productStatus: undefined
      },
      totalProducts: 0,
      activeProducts: 0,
      highRiskProducts: 0,
      avgYield: 0,

      // 对话框相关
      dialogFormVisible: false,
      dialogStatus: '',
      viewDialogVisible: false,
      copyDialogVisible: false,
      importDialogVisible: false,
      exportDialogVisible: false,
      tableSettingDialogVisible: false,
      activeTab: 'basic',

      // 表格设置相关
      allColumns: [
        { key: 'productCode', label: '产品代码', visible: true, fixed: false, width: 150 },
        { key: 'productName', label: '产品名称', visible: true, fixed: false, width: 200 },
        { key: 'productType', label: '产品类型', visible: true, fixed: false, width: 120 },
        { key: 'issuer', label: '发行机构', visible: true, fixed: false, width: 180 },
        { key: 'currencyCode', label: '币种', visible: true, fixed: false, width: 80 },
        { key: 'minInvestmentAmount', label: '最小投资金额', visible: true, fixed: false, width: 120 },
        { key: 'maxInvestmentAmount', label: '最大投资金额', visible: true, fixed: false, width: 120 },
        { key: 'expectedReturnRate', label: '预期收益率', visible: true, fixed: false, width: 120 },
        { key: 'riskLevel', label: '风险等级', visible: true, fixed: false, width: 100 },
        { key: 'investmentTerm', label: '投资期限', visible: true, fixed: false, width: 120 },
        { key: 'subscriptionStartDate', label: '认购开始日期', visible: false, fixed: false, width: 120 },
        { key: 'subscriptionEndDate', label: '认购结束日期', visible: false, fixed: false, width: 120 },
        { key: 'productStatus', label: '产品状态', visible: true, fixed: false, width: 100 },
        { key: 'createTime', label: '创建时间', visible: true, fixed: false, width: 160 }
      ],
      pageSizeOptions: [10, 20, 50, 100],
      currentDragColumn: null,

      // 加载状态
      copyLoading: false,
      importLoading: false,
      exportLoading: false,

      // 选中的数据
      selectedProduct: null,
      selectedRows: [],

      // 临时数据
      temp: {
        productId: undefined,
        productCode: '',
        productName: '',
        productType: '',
        issuer: '',
        currencyCode: 'CNY',
        minInvestmentAmount: 0,
        maxInvestmentAmount: 0,
        expectedReturnRate: 0,
        riskLevel: '',
        investmentTerm: 0,
        termUnit: 'DAY',
        subscriptionStartDate: '',
        subscriptionEndDate: '',
        productStatus: 'ACTIVE',
        earlyRedemption: 1,
        description: '',
        remark: ''
      },

      // 复制表单
      copyForm: {
        sourceProductCode: '',
        sourceProductName: '',
        newProductCode: '',
        newProductName: ''
      },

      // 导出表单
      exportForm: {
        exportRange: 'all',
        fileFormat: 'xlsx'
      },

      // 文件列表
      fileList: [],

      // 表单验证规则
      rules: {
        productCode: [
          { required: true, message: '请输入产品代码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' },
          { validator: this.validateCode, trigger: 'blur' }
        ],
        productName: [
          { required: true, message: '请输入产品名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        productType: [
          { required: true, message: '请选择产品类型', trigger: 'change' }
        ],
        issuer: [
          { required: true, message: '请输入发行机构', trigger: 'blur' }
        ],
        currencyCode: [
          { required: true, message: '请选择币种', trigger: 'change' }
        ],
        riskLevel: [
          { required: true, message: '请选择风险等级', trigger: 'change' }
        ],
        minInvestmentAmount: [
          { required: true, message: '请输入最小投资金额', trigger: 'blur' },
          { type: 'number', min: 0, message: '最小投资金额不能小于0', trigger: 'blur' }
        ],
        maxInvestmentAmount: [
          { required: true, message: '请输入最大投资金额', trigger: 'blur' },
          { type: 'number', min: 0, message: '最大投资金额不能小于0', trigger: 'blur' }
        ],
        expectedReturnRate: [
          { required: true, message: '请输入预期收益率', trigger: 'blur' },
          { type: 'number', min: 0, max: 100, message: '预期收益率必须在0-100之间', trigger: 'blur' }
        ],
        investmentTerm: [
          { required: true, message: '请输入投资期限', trigger: 'blur' },
          { type: 'number', min: 0, message: '投资期限不能小于0', trigger: 'blur' }
        ],
        termUnit: [
          { required: true, message: '请选择期限单位', trigger: 'change' }
        ],
        subscriptionStartDate: [
          { required: true, message: '请选择认购开始日期', trigger: 'change' }
        ],
        subscriptionEndDate: [
          { required: true, message: '请选择认购结束日期', trigger: 'change' }
        ],
        productStatus: [
          { required: true, message: '请选择产品状态', trigger: 'change' }
        ]
      },

      // 复制表单验证规则
      copyRules: {
        newProductCode: [
          { required: true, message: '请输入新产品代码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        newProductName: [
          { required: true, message: '请输入新产品名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadTableSettings()
    this.getList()
  },
  methods: {
    // 编码唯一性验证
    validateCode(rule, value, callback) {
      if (!value) {
        return callback(new Error('请输入产品代码'))
      }

      checkFinancialProductCodeUnique(value, this.temp.productId).then(response => {
        console.log('=== 编码验证响应 ===', response)
        // 修复：判断 response.code 而不是 response.data.code
        // code === 1 表示编码可用（唯一），code === 0 表示编码已存在
        if (response.code === 0) {
          callback(new Error(response.msg || '编码已存在，请重新输入'))
        } else {
          callback()
        }
      }).catch(() => {
        callback()
      })
    },

    // 获取列表数据
    getList() {
      this.listLoading = true
      const params = {
        pageNo: this.listQuery.page,
        pageSize: this.listQuery.limit,
        productCode: this.listQuery.productCode,
        productName: this.listQuery.productName,
        productType: this.listQuery.productType,
        issuer: this.listQuery.issuer,
        currencyCode: this.listQuery.currencyCode,
        riskLevel: this.listQuery.riskLevel,
        productStatus: this.listQuery.productStatus
      }

      getFinancialProductList(params).then(response => {
        console.log('=== 完整响应 ===', response)
        console.log('=== response.data ===', response.data)

        // 根据实际响应结构处理数据
        let listData = []
        let totalRecord = 0

        // 响应格式: response.data = {pageNo: 1, tlist: [...], totalRecord: 0}
        if (response.data && response.data.tlist) {
          console.log('=== 检测到格式: response.data直接包含tlist ===')
          listData = response.data.tlist || []
          totalRecord = response.data.totalRecord || listData.length
          console.log('=== 提取数据 === listData.length:', listData.length, ', totalRecord:', totalRecord)
        }
        // 兼容其他格式
        else if (response.data && response.data.data && response.data.data.tlist) {
          console.log('=== 检测到格式: response.data.data.tlist ===')
          listData = response.data.data.tlist || []
          totalRecord = response.data.data.totalRecord || listData.length
        }

        // 赋值给组件
        this.list = listData
        this.total = totalRecord
        this.calculateStatistics()

        console.log('=== 最终结果 === list.length:', this.list.length, ', total:', this.total)
        console.log('=== list数据 ===', this.list)
        console.log('=== Vue实例list ===', this.$data.list)

        // 强制更新视图
        this.$nextTick(() => {
          console.log('=== nextTick后 list.length ===', this.list.length)
        })

        this.listLoading = false
      }).catch(error => {
        console.error('获取金融产品列表失败:', error)
        this.$message.error('获取数据失败')
        this.listLoading = false
      })
    },

    // 计算统计数据
    calculateStatistics() {
      this.totalProducts = this.list.length
      this.activeProducts = this.list.filter(item => item.productStatus === 'ACTIVE').length
      this.highRiskProducts = this.list.filter(item => item.riskLevel === 'HIGH').length

      if (this.list.length > 0) {
        const totalYield = this.list.reduce((sum, item) => sum + (item.expectedReturnRate || 0), 0)
        this.avgYield = (totalYield / this.list.length).toFixed(2)
      } else {
        this.avgYield = 0
      }
    },

    // 搜索
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },

    // 重置搜索
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        productCode: undefined,
        productName: undefined,
        productType: undefined,
        issuer: undefined,
        currencyCode: undefined,
        riskLevel: undefined,
        productStatus: undefined
      }
      this.getList()
    },

    // 打开创建对话框
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.activeTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 重置临时数据
    resetTemp() {
      this.temp = {
        productId: undefined,
        productCode: '',
        productName: '',
        productType: '',
        issuer: '',
        currencyCode: 'CNY',
        minInvestmentAmount: 0,
        maxInvestmentAmount: 0,
        expectedReturnRate: 0,
        riskLevel: '',
        investmentTerm: 0,
        termUnit: 'DAY',
        subscriptionStartDate: '',
        subscriptionEndDate: '',
        productStatus: 'ACTIVE',
        earlyRedemption: 1,
        description: '',
        remark: ''
      }
    },

    // 创建数据
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createFinancialProduct(this.temp).then(response => {
            console.log('=== 创建响应 ===', response)
            // 修复：判断 response.code 而不是 response.data.code
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: response.msg || '创建成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            } else {
              this.$message.error(response.msg || '创建失败')
            }
          }).catch(error => {
            console.error('创建金融产品失败:', error)
            this.$message.error('创建失败')
          })
        }
      })
    },

    // 打开编辑对话框
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.activeTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 更新数据
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateFinancialProduct(tempData).then(response => {
            console.log('=== 更新响应 ===', response)
            // 修复：判断 response.code 而不是 response.data.code
            if (response.code === 1) {
              const index = this.list.findIndex(v => v.productId === this.temp.productId)
              this.list.splice(index, 1, this.temp)
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: response.msg || '更新成功',
                type: 'success',
                duration: 2000
              })
              this.calculateStatistics()
            } else {
              this.$message.error(response.msg || '更新失败')
            }
          }).catch(error => {
            console.error('更新金融产品失败:', error)
            this.$message.error('更新失败')
          })
        }
      })
    },

    // 删除数据
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFinancialProduct(row.productId).then(response => {
          console.log('=== 删除响应 ===', response)
          // 修复：判断 response.code 而不是 response.data.code
          if (response.code === 1) {
            this.$notify({
              title: '成功',
              message: response.msg || '删除成功',
              type: 'success',
              duration: 2000
            })
            this.list.splice(index, 1)
            this.total--
            this.calculateStatistics()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        }).catch(error => {
          console.error('删除金融产品失败:', error)
          this.$message.error('删除失败')
        })
      })
    },

    // 查看详情
    handleView(row) {
      this.selectedProduct = row
      this.viewDialogVisible = true
    },

    // 复制产品
    handleCopy(row) {
      this.copyForm = {
        sourceProductCode: row.productCode,
        sourceProductName: row.productName,
        newProductCode: '',
        newProductName: ''
      }
      this.copyDialogVisible = true
      this.$nextTick(() => {
        this.$refs['copyForm'].clearValidate()
      })
    },

    // 确认复制
    confirmCopy() {
      this.$refs['copyForm'].validate((valid) => {
        if (valid) {
          this.copyLoading = true
          copyFinancialProduct(
            this.selectedProduct.productId,
            this.copyForm.newProductCode,
            this.copyForm.newProductName
          ).then(response => {
            console.log('=== 复制响应 ===', response)
            this.copyLoading = false
            // 修复：判断 response.code 而不是 response.data.code
            if (response.code === 1) {
              this.copyDialogVisible = false
              this.$notify({
                title: '成功',
                message: response.msg || '复制成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            } else {
              this.$message.error(response.msg || '复制失败')
            }
          }).catch(error => {
            console.error('复制金融产品失败:', error)
            this.copyLoading = false
            this.$message.error('复制失败')
          })
        }
      })
    },

    // 导入功能
    handleImport() {
      this.importDialogVisible = true
      this.fileList = []
    },

    // 文件选择处理
    handleFileChange(file, fileList) {
      this.fileList = fileList.slice(-1) // 只保留最后一个文件
    },

    // 确认导入
    confirmImport() {
      if (this.fileList.length === 0) {
        this.$message.warning('请选择要导入的文件')
        return
      }

      this.importLoading = true
      const formData = new FormData()
      formData.append('file', this.fileList[0].raw)

      importFinancialProducts(formData).then(response => {
        console.log('=== 导入响应 ===', response)
        this.importLoading = false
        this.importDialogVisible = false
        // 修复：判断 response.code 而不是 response.data.code
        if (response.code === 1) {
          this.$notify({
            title: '成功',
            message: response.msg || `导入成功，共导入${response.data ? response.data.successCount : 0}条记录`,
            type: 'success',
            duration: 2000
          })
          this.getList()
        } else {
          this.$message.error(response.msg || '导入失败')
        }
      }).catch(error => {
        console.error('导入失败:', error)
        this.importLoading = false
        this.$message.error('导入失败')
      })
    },

    // 导出功能
    handleExport() {
      this.exportDialogVisible = true
    },

    // 确认导出
    confirmExport() {
      this.exportLoading = true

      const params = {
        exportRange: this.exportForm.exportRange,
        fileFormat: this.exportForm.fileFormat,
        ...this.listQuery
      }

      // 如果是导出筛选结果，移除分页参数
      if (this.exportForm.exportRange === 'filtered') {
        delete params.pageNo
        delete params.pageSize
      }

      exportFinancialProducts(params).then(response => {
        this.exportLoading = false
        this.exportDialogVisible = false

        // 处理文件下载
        const blob = new Blob([response.data])
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `金融产品配置_${new Date().toLocaleDateString()}.${this.exportForm.fileFormat}`
        link.click()

        this.$notify({
          title: '成功',
          message: '导出成功',
          type: 'success',
          duration: 2000
        })
      }).catch(error => {
        console.error('导出失败:', error)
        this.exportLoading = false
        this.$message.error('导出失败')
      })
    },

    // 状态变更
    handleStatusChange(row) {
      updateFinancialProductStatus(row.productId, row.status ? 1 : 0).then(response => {
        console.log('=== 状态更新响应 ===', response)
        // 修复：判断 response.code 而不是 response.data.code
        if (response.code === 1) {
          this.$notify({
            title: '成功',
            message: response.msg || '状态更新成功',
            type: 'success',
            duration: 2000
          })
        } else {
          // 如果更新失败，恢复状态
          row.status = row.status === 1 ? 0 : 1
          this.$message.error(response.msg || '状态更新失败')
        }
      }).catch(error => {
        console.error('状态更新失败:', error)
        // 恢复状态
        row.status = row.status === 1 ? 0 : 1
        this.$message.error('状态更新失败')
      })
    },

    // 选择变更
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 表格设置
    handleTableSetting() {
      this.tableSettingDialogVisible = true
    },

    // 加载表格设置
    loadTableSettings() {
      const savedSettings = localStorage.getItem('financialProduct_tableSettings')
      if (savedSettings) {
        try {
          const settings = JSON.parse(savedSettings)
          this.allColumns = settings.columns || this.allColumns
          if (settings.pageSize) {
            this.listQuery.limit = settings.pageSize
          }
        } catch (e) {
          console.error('加载表格设置失败:', e)
        }
      }
    },

    // 保存表格设置
    saveTableSettings() {
      const settings = {
        columns: this.allColumns,
        pageSize: this.listQuery.limit
      }
      localStorage.setItem('financialProduct_tableSettings', JSON.stringify(settings))
      this.tableKey++ // 强制刷新表格
      this.$message.success('表格设置已保存')
    },

    // 重置表格设置
    resetTableSettings() {
      this.$confirm('确定要重置表格设置为默认值吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('financialProduct_tableSettings')
        location.reload() // 重新加载页面
      })
    },

    // 切换列显示/隐藏
    toggleColumnVisibility(column) {
      column.visible = !column.visible
    },

    // 全选/取消全选列
    toggleAllColumns(visible) {
      this.allColumns.forEach(col => {
        col.visible = visible
      })
    },

    // 拖拽开始
    handleDragStart(event, column) {
      this.currentDragColumn = column
      event.dataTransfer.effectAllowed = 'move'
    },

    // 拖拽结束
    handleDragEnd() {
      this.currentDragColumn = null
    },

    // 拖拽经过
    handleDragOver(event) {
      event.preventDefault()
      event.dataTransfer.dropEffect = 'move'
    },

    // 放置
    handleDrop(event, targetColumn) {
      event.preventDefault()
      if (this.currentDragColumn && this.currentDragColumn !== targetColumn) {
        const fromIndex = this.allColumns.findIndex(col => col.key === this.currentDragColumn.key)
        const toIndex = this.allColumns.findIndex(col => col.key === targetColumn.key)
        const [movedColumn] = this.allColumns.splice(fromIndex, 1)
        this.allColumns.splice(toIndex, 0, movedColumn)
      }
    },

    // 获取可见列
    getVisibleColumns() {
      return this.allColumns.filter(col => col.visible)
    },

    // 获取列的显示内容
    getColumnContent(row, column) {
      const value = row[column.key]
      switch (column.key) {
        case 'productCode':
          return value
        case 'productName':
          return value
        case 'productType':
          return this.getProductTypeText(value)
        case 'issuer':
          return value
        case 'currencyCode':
          return this.getCurrencyText(value)
        case 'minInvestmentAmount':
          return this.formatMoney(value)
        case 'maxInvestmentAmount':
          return this.formatMoney(value)
        case 'expectedReturnRate':
          return value + '%'
        case 'riskLevel':
          return this.getRiskLevelText(value)
        case 'investmentTerm':
          return value ? value + this.getTermUnitText(row.termUnit) : '灵活'
        case 'subscriptionStartDate':
        case 'subscriptionEndDate':
          return this.parseTime(value, '{y}-{m}-{d}')
        case 'productStatus':
          return this.getProductStatusText(value)
        case 'createTime':
          return this.formatTime(value)
        default:
          return value
      }
    },

    // 获取列的标签类型（用于某些需要特殊样式的列）
    getColumnType(column) {
      if (column.key === 'productType') return 'product-type'
      if (column.key === 'riskLevel') return 'risk-level'
      if (column.key === 'productStatus') return 'product-status'
      return 'normal'
    },

    // 表格行类名
    tableRowClassName({ row }) {
      if (row.productStatus === 'MATURED') {
        return 'warning-row'
      }
      if (row.productStatus === 'ACTIVE') {
        return 'success-row'
      }
      return ''
    },
      // 工具函数
    formatMoney(amount) {
      if (!amount) return '0'
      return new Intl.NumberFormat('zh-CN').format(amount)
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 获取产品类型颜色
    getProductTypeColor(type) {
      const colorMap = {
        DEPOSIT: 'success',
        WEALTH: 'warning',
        LOAN: 'danger',
        FUND: 'primary',
        INSURANCE: 'info',
        // 新增：金融产品类型颜色映射
        BANK_WEALTH: 'primary',
        BOND: 'success',
        EQUITY: 'danger',
        FUND: 'warning',
        DERIVATIVE: 'info'
      }
      return colorMap[type] || 'info'
    },

    // 获取币种中文名称
    getCurrencyText(code) {
      const currencyMap = {
        CNY: '人民币',
        USD: '美元',
        EUR: '欧元',
        JPY: '日元',
        GBP: '英镑',
        HKD: '港币'
      }
      return currencyMap[code] || code
    },

    // 获取产品类型文本
    getProductTypeText(type) {
      const typeMap = {
        DEPOSIT: '存款类',
        WEALTH: '理财类',
        LOAN: '贷款类',
        FUND: '基金类',
        INSURANCE: '保险类',
        // 新增：金融产品类型映射
        BANK_WEALTH: '银行理财',
        BOND: '债券',
        EQUITY: '股票',
        FUND: '基金',
        DERIVATIVE: '衍生品'
      }
      return typeMap[type] || type
    },

    // 获取产品状态颜色
    getProductStatusColor(status) {
      const colorMap = {
        ACTIVE: 'success',
        DRAFT: 'info',
        EXPIRED: 'danger',
        SUSPENDED: 'warning'
      }
      return colorMap[status] || 'info'
    },

    // 获取风险等级颜色
    getRiskLevelColor(level) {
      const colorMap = {
        LOW: 'success',
        MEDIUM: 'warning',
        HIGH: 'danger'
      }
      return colorMap[level] || 'info'
    },

    // 获取产品状态文本
    getProductStatusText(status) {
      const statusMap = {
        ACTIVE: '生效中',
        DRAFT: '草稿',
        EXPIRED: '已过期',
        SUSPENDED: '已暂停'
      }
      return statusMap[status] || status
    },

    // 获取风险等级文本
    getRiskLevelText(level) {
      const levelMap = {
        LOW: '低风险',
        MEDIUM: '中风险',
        HIGH: '高风险'
      }
      return levelMap[level] || level
    },

    // 获取期限单位文本
    getTermUnitText(unit) {
      const unitMap = {
        DAY: '天',
        MONTH: '个月',
        YEAR: '年'
      }
      return unitMap[unit] || unit
    },

    formatTime(time) {
      if (!time) return ''
      const date = new Date(typeof time === 'string' ? time : Number(time))
      if (isNaN(date.getTime())) return time
      const y = date.getFullYear()
      const m = String(date.getMonth() + 1).padStart(2, '0')
      const d = String(date.getDate()).padStart(2, '0')
      return `${y}-${m}-${d}`
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-product-manage {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;

  .page-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 12px;
    padding: 24px;
    margin-bottom: 20px;
    color: white;
    box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        .page-title {
          font-size: 24px;
          font-weight: 600;
          margin: 0 0 8px 0;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            font-size: 28px;
          }
        }

        .page-description {
          font-size: 14px;
          opacity: 0.9;
          margin: 0;
        }
      }

      .header-right {
        display: flex;
        gap: 12px;

        .el-button {
          border: 1px solid rgba(255, 255, 255, 0.3);
          background: rgba(255, 255, 255, 0.1);
          color: white;

          &:hover {
            background: rgba(255, 255, 255, 0.2);
            border-color: rgba(255, 255, 255, 0.5);
          }
        }
      }
    }
  }

  .product-overview {
    margin-bottom: 20px;

    .overview-card {
      border: none;
      border-radius: 12px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
      }

      .card-content {
        display: flex;
        align-items: center;
        padding: 20px;

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 12px;
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
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &.high-risk-icon {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
          }

          &.revenue-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 14px;
            color: #666;
            margin-bottom: 8px;
          }

          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #333;
            margin-bottom: 4px;
          }

          .card-change {
            font-size: 12px;
            color: #999;

            &.positive {
              color: #67c23a;
            }

            &.negative {
              color: #f56c6c;
            }
          }
        }
      }
    }
  }

  .search-card, .table-card {
    border: none;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;

    .search-form {
      padding: 20px;

      .demo-form-inline {
        .el-form-item {
          margin-bottom: 0;
        }
      }
    }

    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px 20px 0;

      .table-title {
        .title-text {
          font-size: 16px;
          font-weight: 600;
          color: #333;
        }

        .title-count {
          font-size: 14px;
          color: #666;
          margin-left: 8px;
        }
      }
    }

    .el-table {
      margin: 20px;
      width: calc(100% - 40px);

      .product-info {
        display: flex;
        align-items: center;

        .product-avatar {
          width: 40px;
          height: 40px;
          border-radius: 8px;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;

          i {
            font-size: 18px;
            color: white;
          }
        }

        .product-details {
          .product-name {
            font-weight: 600;
            color: #333;
            margin-bottom: 4px;
          }

          .product-code {
            font-size: 12px;
            color: #999;
          }
        }
      }

      .create-time {
        display: flex;
        align-items: center;
        color: #666;

        i {
          margin-right: 4px;
        }
      }
    }

    .pagination-wrapper {
      padding: 20px;
      text-align: right;
    }
  }
}

::v-deep .el-table .warning-row {
  background: #fdf6ec;
}

::v-deep .el-table .success-row {
  background: #f0f9ff;
}

// 表格设置对话框样式
.table-setting-content {
  .setting-section {
    margin-bottom: 20px;

    .section-title {
      font-size: 16px;
      font-weight: 600;
      color: #333;
      margin-bottom: 15px;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        font-size: 18px;
        color: #409eff;
      }
    }

    .column-list {
      max-height: 400px;
      overflow-y: auto;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      padding: 10px;

      .column-item {
        padding: 10px 15px;
        margin-bottom: 8px;
        background: #f5f7fa;
        border-radius: 4px;
        cursor: move;
        transition: all 0.3s;
        display: flex;
        align-items: center;

        &:hover {
          background: #e6f7ff;
        }

        &.dragging {
          opacity: 0.5;
          background: #bae7ff;
        }

        .drag-handle {
          margin-right: 10px;
          color: #909399;
          cursor: move;
        }

        .el-checkbox {
          flex: 1;
        }
      }
    }

    .section-tips {
      margin-top: 10px;
      padding: 10px;
      background: #f0f9ff;
      border-left: 3px solid #409eff;
      border-radius: 4px;
      color: #666;
      font-size: 13px;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }
  }

  .el-divider {
    margin: 20px 0;
  }
}
</style>
