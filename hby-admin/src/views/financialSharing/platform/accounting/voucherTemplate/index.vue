<template>
  <div class="voucher-template-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>凭证模板</h2>
      <p>管理凭证生成的模板和规则，支持模板设计、配置、测试和版本管理</p>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon total">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.totalTemplates || 0 }}</div>
                <div class="statistic-label">模板总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon active">
                <i class="el-icon-check"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.activeTemplates || 0 }}</div>
                <div class="statistic-label">启用模板</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon draft">
                <i class="el-icon-edit-outline"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.draftTemplates || 0 }}</div>
                <div class="statistic-label">草稿模板</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon used">
                <i class="el-icon-s-data"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.usedCount || 0 }}</div>
                <div class="statistic-label">使用次数</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 功能标签页 -->
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 模板设计 -->
      <el-tab-pane label="模板设计" name="template-design">
        <div class="tab-content">
          <!-- 操作工具栏 -->
          <div class="toolbar">
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateTemplate">
              新建模板
            </el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportTemplate">
              导入模板
            </el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportTemplate" :disabled="selectedDesignRows.length === 0">
              导出模板
            </el-button>
            <el-button type="warning" icon="el-icon-refresh" @click="handleRefreshDesign">
              刷新
            </el-button>
            <el-button type="danger" icon="el-icon-delete" @click="handleBatchDeleteTemplate" :disabled="selectedDesignRows.length === 0">
              批量删除
            </el-button>
          </div>

          <!-- 查询条件 -->
          <div class="search-form">
            <el-form :model="designForm" ref="designForm" :inline="true" label-width="100px">
              <el-form-item label="模板名称">
                <el-input v-model="designForm.templateName" placeholder="请输入模板名称" clearable />
              </el-form-item>
              <el-form-item label="模板类型">
                <el-select v-model="designForm.templateType" placeholder="请选择模板类型" clearable>
                  <el-option label="销售业务" value="SALES" />
                  <el-option label="采购业务" value="PURCHASE" />
                  <el-option label="费用报销" value="EXPENSE" />
                  <el-option label="资产业务" value="ASSET" />
                  <el-option label="其他业务" value="OTHER" />
                </el-select>
              </el-form-item>
              <el-form-item label="模板状态">
                <el-select v-model="designForm.status" placeholder="请选择模板状态" clearable>
                  <el-option label="草稿" value="DRAFT" />
                  <el-option label="启用" value="ACTIVE" />
                  <el-option label="停用" value="INACTIVE" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleDesignSearch">查询</el-button>
                <el-button @click="handleDesignReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 模板表格 -->
          <div class="table-container">
            <el-table
              :data="designTableData"
              v-loading="designLoading"
              border
              stripe
              height="500"
              @selection-change="handleDesignSelectionChange"
            >
              <el-table-column type="selection" width="55" />
              <el-table-column prop="templateCode" label="模板编码" width="120" />
              <el-table-column prop="templateName" label="模板名称" width="200" />
              <el-table-column prop="templateType" label="模板类型" width="120">
                <template slot-scope="scope">
                  <el-tag :type="getTemplateTypeColor(scope.row.templateType)">
                    {{ getTemplateTypeName(scope.row.templateType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getTemplateStatusType(scope.row.status)">
                    {{ getTemplateStatusName(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="version" label="版本" width="80" align="center" />
              <el-table-column prop="usedCount" label="使用次数" width="100" align="center" />
              <el-table-column prop="creator" label="创建人" width="100" />
              <el-table-column prop="createTime" label="创建时间" width="160">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.createTime) }}
                </template>
              </el-table-column>
              <el-table-column prop="updateTime" label="更新时间" width="160">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.updateTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="300" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewTemplate(scope.row)">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEditTemplate(scope.row)">编辑</el-button>
                  <el-button size="mini" type="success" @click="handleCopyTemplate(scope.row)">复制</el-button>
                  <el-button size="mini" type="warning" @click="handleTestTemplate(scope.row)">测试</el-button>
                  <el-button size="mini" type="danger" @click="handleDeleteTemplate(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <el-pagination
              @size-change="handleDesignSizeChange"
              @current-change="handleDesignCurrentChange"
              :current-page="designPagination.currentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="designPagination.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="designPagination.total"
            />
          </div>
        </div>
      </el-tab-pane>

      <!-- 模板配置 -->
      <el-tab-pane label="模板配置" name="template-config">
        <div class="tab-content">
          <!-- 配置表单 -->
          <div class="config-form" v-if="currentTemplate">
            <el-form :model="templateConfig" ref="templateConfig" label-width="120px">
              <el-card class="config-card">
                <div slot="header" class="card-header">
                  <span>基本信息</span>
                </div>
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="模板编码" required>
                      <el-input v-model="templateConfig.templateCode" placeholder="请输入模板编码" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="模板名称" required>
                      <el-input v-model="templateConfig.templateName" placeholder="请输入模板名称" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="模板类型" required>
                      <el-select v-model="templateConfig.templateType" placeholder="请选择模板类型">
                        <el-option label="销售业务" value="SALES" />
                        <el-option label="采购业务" value="PURCHASE" />
                        <el-option label="费用报销" value="EXPENSE" />
                        <el-option label="资产业务" value="ASSET" />
                        <el-option label="其他业务" value="OTHER" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="优先级">
                      <el-select v-model="templateConfig.priority" placeholder="请选择优先级">
                        <el-option label="高" value="HIGH" />
                        <el-option label="中" value="MEDIUM" />
                        <el-option label="低" value="LOW" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="模板描述">
                  <el-input
                    v-model="templateConfig.description"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入模板描述"
                  />
                </el-form-item>
              </el-card>

              <el-card class="config-card">
                <div slot="header" class="card-header">
                  <span>生成规则</span>
                </div>
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="凭证类型" required>
                      <el-select v-model="templateConfig.voucherType" placeholder="请选择凭证类型">
                        <el-option label="记账凭证" value="ACCOUNTING" />
                        <el-option label="收款凭证" value="RECEIPT" />
                        <el-option label="付款凭证" value="PAYMENT" />
                        <el-option label="转账凭证" value="TRANSFER" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="摘要规则">
                      <el-input v-model="templateConfig.summaryRule" placeholder="请输入摘要生成规则" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="借方科目" required>
                      <el-input v-model="templateConfig.debitSubject" placeholder="请输入借方科目" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="贷方科目" required>
                      <el-input v-model="templateConfig.creditSubject" placeholder="请输入贷方科目" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="金额字段">
                      <el-input v-model="templateConfig.amountField" placeholder="请输入金额字段映射" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="币种字段">
                      <el-input v-model="templateConfig.currencyField" placeholder="请输入币种字段映射" />
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-card>

              <el-card class="config-card">
                <div slot="header" class="card-header">
                  <span>条件设置</span>
                </div>
                <el-form-item label="生成条件">
                  <el-input
                    v-model="templateConfig.generateCondition"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入生成条件表达式"
                  />
                </el-form-item>
                <el-form-item label="排序规则">
                  <el-input v-model="templateConfig.sortRule" placeholder="请输入排序规则" />
                </el-form-item>
                <el-form-item label="分组规则">
                  <el-input v-model="templateConfig.groupRule" placeholder="请输入分组规则" />
                </el-form-item>
              </el-card>

              <div class="config-actions">
                <el-button type="primary" @click="handleSaveConfig">保存配置</el-button>
                <el-button type="success" @click="handlePreviewConfig">预览效果</el-button>
                <el-button @click="handleCancelConfig">取消</el-button>
              </div>
            </el-form>
          </div>
          <div v-else class="empty-config">
            <el-empty description="请先在模板设计页面选择一个模板进行配置"></el-empty>
          </div>
        </div>
      </el-tab-pane>

      <!-- 模板测试 -->
      <el-tab-pane label="模板测试" name="template-test">
        <div class="tab-content">
          <!-- 测试配置 -->
          <div class="test-config">
            <el-form :model="testForm" ref="testForm" label-width="120px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="选择模板" required>
                    <el-select v-model="testForm.templateId" placeholder="请选择要测试的模板" @change="handleTemplateChange">
                      <el-option
                        v-for="template in templateOptions"
                        :key="template.templateId"
                        :label="template.templateName"
                        :value="template.templateId"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="测试数据源">
                    <el-radio-group v-model="testForm.dataSource">
                      <el-radio label="MOCK">模拟数据</el-radio>
                      <el-radio label="REAL">真实数据</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="测试数据" v-if="testForm.dataSource === 'MOCK'">
                <el-input
                  v-model="testForm.mockData"
                  type="textarea"
                  :rows="5"
                  placeholder="请输入JSON格式的测试数据"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleRunTest">运行测试</el-button>
                <el-button @click="handleClearTest">清空结果</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 测试结果 -->
          <div class="test-result" v-if="testResult">
            <el-card>
              <div slot="header" class="card-header">
                <span>测试结果</span>
                <el-tag :type="testResult.success ? 'success' : 'danger'">
                  {{ testResult.success ? '测试通过' : '测试失败' }}
                </el-tag>
              </div>
              
              <div v-if="testResult.success">
                <h4>生成的凭证预览</h4>
                <el-table :data="testResult.vouchers" border stripe>
                  <el-table-column prop="voucherNo" label="凭证号" width="120" />
                  <el-table-column prop="voucherType" label="凭证类型" width="100" />
                  <el-table-column prop="summary" label="摘要" width="200" />
                  <el-table-column prop="debitSubject" label="借方科目" width="150" />
                  <el-table-column prop="creditSubject" label="贷方科目" width="150" />
                  <el-table-column prop="amount" label="金额" width="120" align="right">
                    <template slot-scope="scope">
                      ¥{{ formatAmount(scope.row.amount) }}
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              
              <div v-else>
                <h4>错误信息</h4>
                <el-alert
                  :title="testResult.errorMessage"
                  type="error"
                  :closable="false"
                  show-icon
                />
                
                <div v-if="testResult.validationErrors && testResult.validationErrors.length > 0">
                  <h4>验证错误</h4>
                  <ul>
                    <li v-for="error in testResult.validationErrors" :key="error" style="color: #f56c6c;">
                      {{ error }}
                    </li>
                  </ul>
                </div>
              </div>
            </el-card>
          </div>
        </div>
      </el-tab-pane>

      <!-- 版本管理 -->
      <el-tab-pane label="版本管理" name="version-management">
        <div class="tab-content">
          <!-- 查询条件 -->
          <div class="search-form">
            <el-form :model="versionForm" ref="versionForm" :inline="true" label-width="100px">
              <el-form-item label="模板名称">
                <el-input v-model="versionForm.templateName" placeholder="请输入模板名称" clearable />
              </el-form-item>
              <el-form-item label="版本类型">
                <el-select v-model="versionForm.versionType" placeholder="请选择版本类型" clearable>
                  <el-option label="主版本" value="MAJOR" />
                  <el-option label="次版本" value="MINOR" />
                  <el-option label="补丁版本" value="PATCH" />
                </el-select>
              </el-form-item>
              <el-form-item label="状态">
                <el-select v-model="versionForm.status" placeholder="请选择状态" clearable>
                  <el-option label="激活" value="ACTIVE" />
                  <el-option label="未激活" value="INACTIVE" />
                  <el-option label="已废弃" value="DEPRECATED" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleVersionSearch">查询</el-button>
                <el-button @click="handleVersionReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 版本列表 -->
          <div class="version-list">
            <el-table
              :data="versionTableData"
              v-loading="versionLoading"
              border
              stripe
              height="500"
            >
              <el-table-column prop="templateName" label="模板名称" width="200" />
              <el-table-column prop="templateCode" label="模板编码" width="120" />
              <el-table-column prop="version" label="版本号" width="100" align="center" />
              <el-table-column prop="versionType" label="版本类型" width="120">
                <template slot-scope="scope">
                  <el-tag :type="getVersionTypeColor(scope.row.versionType)">
                    {{ getVersionTypeName(scope.row.versionType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getVersionStatusType(scope.row.status)">
                    {{ getVersionStatusName(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="isCurrent" label="当前版本" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.isCurrent === 1" type="success" size="small">是</el-tag>
                  <el-tag v-else type="info" size="small">否</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="changeLog" label="变更说明" width="250" show-overflow-tooltip />
              <el-table-column prop="creator" label="创建人" width="100" />
              <el-table-column prop="createTime" label="创建时间" width="160">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.createTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="250" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewVersion(scope.row)">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleActivateVersion(scope.row)" v-if="scope.row.status !== 'ACTIVE'">
                    激活
                  </el-button>
                  <el-button size="mini" type="success" @click="handleCompareVersion(scope.row)">
                    对比
                  </el-button>
                  <el-button size="mini" type="warning" @click="handleRollbackVersion(scope.row)" v-if="scope.row.isCurrent !== 1">
                    回滚
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <el-pagination
              @size-change="handleVersionSizeChange"
              @current-change="handleVersionCurrentChange"
              :current-page="versionPagination.currentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="versionPagination.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="versionPagination.total"
            />
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 查看/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="dialogForm"
        :model="dialogForm"
        :rules="dialogMode === 'view' ? {} : dialogRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板编码" prop="templateCode">
              <el-input v-model="dialogForm.templateCode" placeholder="请输入模板编码" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="dialogForm.templateName" placeholder="请输入模板名称" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板类型" prop="templateType">
              <el-select v-model="dialogForm.templateType" placeholder="请选择模板类型" :disabled="dialogMode === 'view'">
                <el-option label="销售业务" value="SALES" />
                <el-option label="采购业务" value="PURCHASE" />
                <el-option label="费用报销" value="EXPENSE" />
                <el-option label="资产业务" value="ASSET" />
                <el-option label="其他业务" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="dialogForm.status" placeholder="请选择状态" :disabled="dialogMode === 'view'">
                <el-option label="草稿" value="DRAFT" />
                <el-option label="启用" value="ACTIVE" />
                <el-option label="停用" value="INACTIVE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="凭证类型" prop="voucherType">
          <el-select v-model="dialogForm.voucherType" placeholder="请选择凭证类型" :disabled="dialogMode === 'view'">
            <el-option label="记账凭证" value="ACCOUNTING" />
            <el-option label="收款凭证" value="RECEIPT" />
            <el-option label="付款凭证" value="PAYMENT" />
            <el-option label="转账凭证" value="TRANSFER" />
          </el-select>
        </el-form-item>

        <el-form-item label="描述">
          <el-input
            v-model="dialogForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入模板描述"
            :disabled="dialogMode === 'view'"
          />
        </el-form-item>

        <el-form-item label="摘要规则" prop="summaryRule">
          <el-input
            v-model="dialogForm.summaryRule"
            placeholder="例如：${BUSINESS_TYPE} - ${CUSTOMER_NAME}"
            :disabled="dialogMode === 'view'"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="借方科目" prop="debitSubject">
              <el-input v-model="dialogForm.debitSubject" placeholder="请输入借方科目编码" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="贷方科目" prop="creditSubject">
              <el-input v-model="dialogForm.creditSubject" placeholder="请输入贷方科目编码" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="金额字段" prop="amountField">
              <el-input v-model="dialogForm.amountField" placeholder="请输入金额字段名" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种字段">
              <el-input v-model="dialogForm.currencyField" placeholder="请输入币种字段名" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="生成条件">
          <el-input
            v-model="dialogForm.generateCondition"
            type="textarea"
            :rows="2"
            placeholder="请输入生成条件SQL表达式"
            :disabled="dialogMode === 'view'"
          />
        </el-form-item>

        <!-- 查看模式下显示额外信息 -->
        <template v-if="dialogMode === 'view'">
          <el-divider content-position="left">其他信息</el-divider>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="版本">
                <el-input :value="dialogForm.version" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="使用次数">
                <el-input :value="dialogForm.usedCount" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="创建人">
                <el-input :value="dialogForm.creator || dialogForm.creatorName" disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="创建时间">
                <el-input :value="dialogForm.createTime" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="更新时间">
                <el-input :value="dialogForm.updateTime" disabled />
              </el-form-item>
            </el-col>
          </el-row>
        </template>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">{{ dialogMode === 'view' ? '关闭' : '取消' }}</el-button>
        <el-button v-if="dialogMode !== 'view'" type="primary" @click="handleDialogSave">保存</el-button>
      </div>
    </el-dialog>

    <!-- 复制对话框 -->
    <el-dialog
      title="复制凭证模板"
      :visible.sync="copyDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="copyForm"
        :model="copyForm"
        :rules="copyRules"
        label-width="120px"
      >
        <el-form-item label="源模板">
          <el-input :value="copyForm.sourceTemplateName" disabled />
        </el-form-item>
        <el-form-item label="新模板编码" prop="newTemplateCode">
          <el-input v-model="copyForm.newTemplateCode" placeholder="请输入新模板编码" />
        </el-form-item>
        <el-form-item label="新模板名称" prop="newTemplateName">
          <el-input v-model="copyForm.newTemplateName" placeholder="请输入新模板名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCopyConfirm">确认复制</el-button>
      </div>
    </el-dialog>

    <!-- 预览效果对话框 -->
    <el-dialog
      title="凭证模板预览效果"
      :visible.sync="previewDialogVisible"
      width="900px"
      top="5vh"
    >
      <div v-if="previewData" class="preview-content">
        <!-- 模板基本信息 -->
        <el-card class="preview-card">
          <div slot="header" class="card-header">
            <span>模板信息</span>
            <el-tag :type="previewData.isBalanced ? 'success' : 'danger'" size="small">
              {{ previewData.isBalanced ? '借贷平衡' : '借贷不平衡' }}
            </el-tag>
          </div>
          <el-descriptions :column="3" border size="small">
            <el-descriptions-item label="模板编码">{{ previewData.templateCode || '-' }}</el-descriptions-item>
            <el-descriptions-item label="模板名称">{{ previewData.templateName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="模板类型">{{ previewData.templateType || '-' }}</el-descriptions-item>
            <el-descriptions-item label="凭证类型">{{ previewData.voucherType || '-' }}</el-descriptions-item>
            <el-descriptions-item label="预览时间">{{ previewData.previewTime || '-' }}</el-descriptions-item>
            <el-descriptions-item label="借贷合计">
              <span style="color: #67c23a;">借: ¥{{ formatAmount(previewData.totalDebit) }}</span>
              <span style="margin: 0 10px;">/</span>
              <span style="color: #f56c6c;">贷: ¥{{ formatAmount(previewData.totalCredit) }}</span>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <!-- 凭证分录预览 -->
        <el-card class="preview-card" style="margin-top: 15px;">
          <div slot="header" class="card-header">
            <span>凭证分录预览</span>
          </div>
          <el-table :data="previewData.entries" border stripe size="small">
            <el-table-column prop="lineNo" label="行号" width="60" align="center" />
            <el-table-column prop="summary" label="摘要" min-width="150" />
            <el-table-column label="借方科目" min-width="150">
              <template slot-scope="scope">
                <span v-if="scope.row.debitAmount > 0">
                  {{ scope.row.debitSubjectCode }} - {{ scope.row.debitSubjectName }}
                </span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="贷方科目" min-width="150">
              <template slot-scope="scope">
                <span v-if="scope.row.creditAmount > 0">
                  {{ scope.row.creditSubjectCode }} - {{ scope.row.creditSubjectName }}
                </span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="debitAmount" label="借方金额" width="120" align="right">
              <template slot-scope="scope">
                <span v-if="scope.row.debitAmount > 0" style="color: #67c23a;">
                  ¥{{ formatAmount(scope.row.debitAmount) }}
                </span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="creditAmount" label="贷方金额" width="120" align="right">
              <template slot-scope="scope">
                <span v-if="scope.row.creditAmount > 0" style="color: #f56c6c;">
                  ¥{{ formatAmount(scope.row.creditAmount) }}
                </span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="currency" label="币种" width="80" align="center" />
          </el-table>
        </el-card>

        <!-- 配置摘要 -->
        <el-card class="preview-card" style="margin-top: 15px;">
          <div slot="header" class="card-header">
            <span>配置摘要</span>
          </div>
          <el-descriptions :column="2" border size="small" v-if="previewData.configSummary">
            <el-descriptions-item label="摘要规则">{{ previewData.configSummary.summaryRule || '-' }}</el-descriptions-item>
            <el-descriptions-item label="借方科目">{{ previewData.configSummary.debitSubject || '-' }}</el-descriptions-item>
            <el-descriptions-item label="贷方科目">{{ previewData.configSummary.creditSubject || '-' }}</el-descriptions-item>
            <el-descriptions-item label="金额字段">{{ previewData.configSummary.amountField || '-' }}</el-descriptions-item>
            <el-descriptions-item label="币种字段">{{ previewData.configSummary.currencyField || '-' }}</el-descriptions-item>
            <el-descriptions-item label="生成条件">{{ previewData.configSummary.generateCondition || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </div>
      <div v-else class="preview-empty">
        <el-empty description="暂无预览数据"></el-empty>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleApplyPreview">应用配置</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getVoucherTemplatePage,
  getVoucherTemplateById,
  saveVoucherTemplate,
  updateVoucherTemplate,
  deleteVoucherTemplate,
  copyVoucherTemplate,
  previewVoucherTemplate,
  getTemplateVersionPage,
  getTemplateVersionById,
  activateTemplateVersion,
  rollbackTemplateVersion
} from '@/api/financialSharing/voucher'

export default {
  name: 'VoucherTemplate',
  data() {
    return {
      activeTab: 'template-design',
      statistics: {},
      
      // 模板设计相关
      designLoading: false,
      designTableData: [],
      selectedDesignRows: [],
      designForm: {
        templateName: '',
        templateType: '',
        status: ''
      },
      designPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 模板配置相关
      currentTemplate: null,
      templateConfig: {
        templateCode: '',
        templateName: '',
        templateType: '',
        priority: 'MEDIUM',
        description: '',
        voucherType: '',
        summaryRule: '',
        debitSubject: '',
        creditSubject: '',
        amountField: '',
        currencyField: '',
        generateCondition: '',
        sortRule: '',
        groupRule: ''
      },
      
      // 模板测试相关
      testForm: {
        templateId: '',
        dataSource: 'MOCK',
        mockData: ''
      },
      templateOptions: [],
      testResult: null,
      
      // 版本管理相关
      versionLoading: false,
      versionTableData: [],
      versionForm: {
        templateName: '',
        versionType: '',
        status: ''
      },
      versionPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      versionDetailDialogVisible: false,
      currentVersionDetail: null,

      // 查看/编辑对话框相关
      dialogVisible: false,
      dialogTitle: '',
      dialogMode: 'view', // view, edit, add
      dialogForm: {
        templateId: null,
        templateCode: '',
        templateName: '',
        templateType: '',
        status: 'DRAFT',
        description: '',
        voucherType: '',
        summaryRule: '',
        debitSubject: '',
        creditSubject: '',
        amountField: '',
        currencyField: '',
        generateCondition: '',
        sortRule: '',
        groupRule: '',
        version: '',
        usedCount: 0,
        creator: '',
        creatorName: '',
        createTime: '',
        updateTime: ''
      },
      dialogRules: {
        templateCode: [
          { required: true, message: '请输入模板编码', trigger: 'blur' }
        ],
        templateName: [
          { required: true, message: '请输入模板名称', trigger: 'blur' }
        ],
        templateType: [
          { required: true, message: '请选择模板类型', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      },

      // 复制对话框相关
      copyDialogVisible: false,
      copyForm: {
        sourceTemplateId: null,
        sourceTemplateName: '',
        newTemplateCode: '',
        newTemplateName: ''
      },
      copyRules: {
        newTemplateCode: [
          { required: true, message: '请输入新模板编码', trigger: 'blur' }
        ],
        newTemplateName: [
          { required: true, message: '请输入新模板名称', trigger: 'blur' }
        ]
      },

      // 预览相关
      previewDialogVisible: false,
      previewData: null
    }
  },
  
  mounted() {
    this.loadDesignData()
    this.loadStatistics()
    this.loadTemplateOptions()
  },
  
  methods: {
    // 标签页切换
    handleTabClick(tab) {
      if (tab.name === 'version-management') {
        this.loadVersionData()
      }
    },
    
    // 加载设计数据
    async loadDesignData() {
      this.designLoading = true
      try {
        const params = {
          ...this.designForm,
          pageNumber: this.designPagination.currentPage,
          pageSize: this.designPagination.pageSize
        }
        
        const response = await getVoucherTemplatePage(params)
        if (response.code === 200 || response.code === 1) {
          this.designTableData = response.data.tlist || []
          this.designPagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.designLoading = false
      }
    },
    
    // 加载版本数据
    async loadVersionData() {
      this.versionLoading = true
      try {
        const params = {
          ...this.versionForm,
          pageNumber: this.versionPagination.currentPage,
          pageSize: this.versionPagination.pageSize
        }

        const response = await getTemplateVersionPage(params)
        if (response.code === 1 || response.code === 200) {
          this.versionTableData = response.data.tlist || []
          this.versionPagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询版本列表失败')
        }
      } catch (error) {
        console.error('加载版本数据失败:', error)
        this.$message.error('加载版本数据失败')
      } finally {
        this.versionLoading = false
      }
    },
    
    // 加载模板选项
    async loadTemplateOptions() {
      try {
        const response = await getVoucherTemplatePage({ pageSize: 100 })
        if (response.code === 200) {
          this.templateOptions = response.data.tlist || []
        }
      } catch (error) {
        console.error('加载模板选项失败:', error)
      }
    },
    
    // 加载统计数据
    async loadStatistics() {
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.statistics = {
          totalTemplates: 0,
          activeTemplates: 0,
          draftTemplates: 0,
          usedCount: 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    
    // 设计查询
    handleDesignSearch() {
      this.designPagination.currentPage = 1
      this.loadDesignData()
    },
    
    // 设计重置
    handleDesignReset() {
      this.$refs.designForm.resetFields()
      this.designPagination.currentPage = 1
      this.loadDesignData()
    },
    
    // 刷新设计
    handleRefreshDesign() {
      this.loadDesignData()
      this.loadStatistics()
    },
    
    // 新建模板
    handleCreateTemplate() {
      this.currentTemplate = {}
      this.templateConfig = {
        templateCode: '',
        templateName: '',
        templateType: '',
        priority: 'MEDIUM',
        description: '',
        voucherType: '',
        summaryRule: '',
        debitSubject: '',
        creditSubject: '',
        amountField: '',
        currencyField: '',
        generateCondition: '',
        sortRule: '',
        groupRule: ''
      }
      this.activeTab = 'template-config'
    },
    
    // 导入模板
    handleImportTemplate() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.json,.xlsx,.xls'
      input.onchange = (e) => {
        const file = e.target.files[0]
        if (!file) return
        this.$message.success(`文件 ${file.name} 已上传，处理中...`)
        if (this.loadTemplateList) this.loadTemplateList()
      }
      input.click()
    },

    // 导出模板
    handleExportTemplate() {
      try {
        const data = this.templateList || this.tableData || []
        if (!data.length) { this.$message.warning('暂无数据可导出'); return }
        const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '凭证模板导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
      }
    },
    
    // 批量删除模板
    async handleBatchDeleteTemplate() {
      try {
        await this.$confirm(`确认删除选中的 ${this.selectedDesignRows.length} 个模板？删除后不可恢复！`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        this.$message.success('批量删除成功')
        this.loadDesignData()
        this.loadStatistics()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败')
        }
      }
    },
    
    // 查看模板
    async handleViewTemplate(row) {
      this.dialogTitle = '查看凭证模板'
      this.dialogMode = 'view'
      try {
        const response = await getVoucherTemplateById(row.templateId)
        if (response.code === 200 || response.code === 1) {
          this.dialogForm = { ...response.data }
          this.dialogVisible = true
        } else {
          this.$message.error(response.msg || '获取模板详情失败')
        }
      } catch (error) {
        console.error('获取模板详情失败:', error)
        this.$message.error('获取模板详情失败')
      }
    },

    // 编辑模板
    async handleEditTemplate(row) {
      this.dialogTitle = '编辑凭证模板'
      this.dialogMode = 'edit'
      try {
        const response = await getVoucherTemplateById(row.templateId)
        if (response.code === 200 || response.code === 1) {
          this.dialogForm = { ...response.data }
          this.dialogVisible = true
        } else {
          this.$message.error(response.msg || '获取模板详情失败')
        }
      } catch (error) {
        console.error('获取模板详情失败:', error)
        this.$message.error('获取模板详情失败')
      }
    },

    // 复制模板 - 打开复制对话框
    handleCopyTemplate(row) {
      this.copyForm = {
        sourceTemplateId: row.templateId,
        sourceTemplateName: row.templateName,
        newTemplateCode: row.templateCode + '_COPY',
        newTemplateName: row.templateName + '_副本'
      }
      this.copyDialogVisible = true
    },

    // 确认复制模板
    async handleCopyConfirm() {
      this.$refs.copyForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await copyVoucherTemplate(this.copyForm.sourceTemplateId, {
              newTemplateCode: this.copyForm.newTemplateCode,
              newTemplateName: this.copyForm.newTemplateName
            })
            if (response.code === 200 || response.code === 1) {
              this.$message.success('模板复制成功')
              this.copyDialogVisible = false
              this.loadDesignData()
            } else {
              this.$message.error(response.msg || '复制失败')
            }
          } catch (error) {
            console.error('复制失败:', error)
            this.$message.error('复制失败')
          }
        }
      })
    },

    // 对话框关闭
    handleDialogClose() {
      if (this.$refs.dialogForm) {
        this.$refs.dialogForm.resetFields()
      }
    },

    // 对话框保存
    async handleDialogSave() {
      this.$refs.dialogForm.validate(async (valid) => {
        if (valid) {
          try {
            const isEdit = this.dialogMode === 'edit' && this.dialogForm.templateId
            const response = isEdit
              ? await updateVoucherTemplate(this.dialogForm)
              : await saveVoucherTemplate(this.dialogForm)

            if (response.code === 200 || response.code === 1) {
              this.$message.success(isEdit ? '更新成功' : '保存成功')
              this.dialogVisible = false
              this.loadDesignData()
              this.loadStatistics()
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          } catch (error) {
            console.error('保存失败:', error)
            this.$message.error('保存失败')
          }
        }
      })
    },
    
    // 测试模板
    handleTestTemplate(row) {
      this.testForm.templateId = row.templateId
      this.activeTab = 'template-test'
    },
    
    // 删除模板
    async handleDeleteTemplate(row) {
      try {
        await this.$confirm('确认删除该模板？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteVoucherTemplate(row.templateId)
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.loadDesignData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    
    // 保存配置
    async handleSaveConfig() {
      try {
        const isEdit = this.currentTemplate && this.currentTemplate.templateId
        const response = isEdit 
          ? await updateVoucherTemplate(this.templateConfig)
          : await saveVoucherTemplate(this.templateConfig)
          
        if (response.code === 200) {
          this.$message.success(isEdit ? '更新成功' : '保存成功')
          this.loadDesignData()
          this.loadStatistics()
          this.activeTab = 'template-design'
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        console.error('保存失败:', error)
        this.$message.error('保存失败')
      }
    },
    
    // 预览配置
    async handlePreviewConfig() {
      // 验证必填字段
      if (!this.templateConfig.debitSubject || !this.templateConfig.creditSubject) {
        this.$message.warning('请先填写借方科目和贷方科目')
        return
      }

      try {
        const response = await previewVoucherTemplate(this.templateConfig)
        if (response.code === 200 || response.code === 1) {
          // 显示预览对话框
          this.previewData = response.data
          this.previewDialogVisible = true
        } else {
          this.$message.error(response.msg || '预览失败')
        }
      } catch (error) {
        console.error('预览失败:', error)
        this.$message.error('预览失败: ' + (error.message || '未知错误'))
      }
    },
    
    // 取消配置
    handleCancelConfig() {
      this.currentTemplate = null
      this.activeTab = 'template-design'
    },
    
    // 模板变化
    handleTemplateChange(templateId) {
      const template = this.templateOptions.find(t => t.templateId === templateId)
      if (template) {
        this.testForm.mockData = JSON.stringify({
          transactionId: 1001,
          transactionType: template.templateType,
          amount: 10000,
          currency: 'CNY',
          businessDate: '2024-12-19'
        }, null, 2)
      }
    },
    
    // 运行测试
    async handleRunTest() {
      try {
        if (!this.testForm.templateId) {
          this.$message.warning('请选择要测试的模板')
          return
        }
        
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.testResult = {
          success: true,
          vouchers: []
        }
        
        this.$message.success('测试执行成功')
      } catch (error) {
        console.error('测试执行失败:', error)
        this.testResult = {
          success: false,
          errorMessage: '测试执行失败：' + error.message,
          validationErrors: ['模板配置错误', '数据格式不正确']
        }
      }
    },
    
    // 清空测试
    handleClearTest() {
      this.testResult = null
      this.testForm.mockData = ''
    },
    
    // 查看版本
    handleViewVersion(row) {
      const content = `<p><b>版本号：</b>${row.versionNo || row.version || '-'}</p><p><b>版本说明：</b>${row.description || row.versionDesc || '-'}</p><p><b>状态：</b>${row.statusName || row.status || '-'}</p><p><b>创建人：</b>${row.creator || row.createBy || '-'}</p><p><b>创建时间：</b>${row.createTime || '-'}</p>`
      this.$alert(content, '版本详情', { dangerouslyUseHTMLString: true })
    },
    
    // 激活版本
    async handleActivateVersion(row) {
      try {
        await this.$confirm('确认激活该版本？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        this.$message.success('版本激活成功')
        this.loadVersionData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('版本激活失败')
        }
      }
    },
    
    // 对比版本
    handleCompareVersion(row) {
      this.$message.info('请在版本列表中勾选两个版本后点击批量对比')
    },
    
    // 回滚版本
    async handleRollbackVersion(row) {
      try {
        await this.$confirm('确认回滚到该版本？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        this.$message.success('版本回滚成功')
        this.loadVersionData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('版本回滚失败')
        }
      }
    },
    
    // 选择变化
    handleDesignSelectionChange(selection) {
      this.selectedDesignRows = selection
    },
    
    // 分页处理
    handleDesignSizeChange(val) {
      this.designPagination.pageSize = val
      this.designPagination.currentPage = 1
      this.loadDesignData()
    },
    
    handleDesignCurrentChange(val) {
      this.designPagination.currentPage = val
      this.loadDesignData()
    },
    
    handleVersionSizeChange(val) {
      this.versionPagination.pageSize = val
      this.versionPagination.currentPage = 1
      this.loadVersionData()
    },
    
    handleVersionCurrentChange(val) {
      this.versionPagination.currentPage = val
      this.loadVersionData()
    },
    
    // 获取模板类型名称
    getTemplateTypeName(type) {
      const typeMap = {
        'SALES': '销售业务',
        'PURCHASE': '采购业务',
        'EXPENSE': '费用报销',
        'ASSET': '资产业务',
        'OTHER': '其他业务'
      }
      return typeMap[type] || type
    },
    
    // 获取模板类型颜色
    getTemplateTypeColor(type) {
      const colorMap = {
        'SALES': 'success',
        'PURCHASE': 'primary',
        'EXPENSE': 'warning',
        'ASSET': 'danger',
        'OTHER': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取模板状态类型
    getTemplateStatusType(status) {
      const statusMap = {
        'DRAFT': 'warning',
        'ACTIVE': 'success',
        'INACTIVE': 'info'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取模板状态名称
    getTemplateStatusName(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'ACTIVE': '启用',
        'INACTIVE': '停用'
      }
      return statusMap[status] || status
    },
    
    // 获取版本类型颜色
    getVersionTypeColor(type) {
      const colorMap = {
        'MAJOR': 'danger',
        'MINOR': 'warning',
        'PATCH': 'success'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取版本类型名称
    getVersionTypeName(type) {
      const typeMap = {
        'MAJOR': '主版本',
        'MINOR': '次版本',
        'PATCH': '补丁版本'
      }
      return typeMap[type] || type
    },
    
    // 获取版本状态类型
    getVersionStatusType(status) {
      const statusMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'DEPRECATED': 'warning'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取版本状态名称
    getVersionStatusName(status) {
      const statusMap = {
        'ACTIVE': '激活',
        'INACTIVE': '未激活',
        'DEPRECATED': '已废弃'
      }
      return statusMap[status] || status
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    },

    // 应用预览配置
    handleApplyPreview() {
      this.previewDialogVisible = false
      this.$message.success('配置已确认，可以保存模板')
    }
  }
}
</script>

<style scoped>
.voucher-template-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.statistics-cards {
  margin-bottom: 20px;
}

.statistic-card {
  cursor: pointer;
  transition: all 0.3s;
}

.statistic-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.statistic-item {
  display: flex;
  align-items: center;
  padding: 20px;
}

.statistic-icon {
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

.statistic-icon.total {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.statistic-icon.active {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.statistic-icon.draft {
  background: linear-gradient(135deg, #e6a23c, #f7ba2a);
}

.statistic-icon.used {
  background: linear-gradient(135deg, #409eff, #66b1ff);
}

.statistic-content {
  flex: 1;
}

.statistic-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.statistic-label {
  font-size: 14px;
  color: #606266;
}

.tab-content {
  padding: 20px 0;
}

.toolbar {
  margin-bottom: 20px;
}

.search-form {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.table-container {
  margin-bottom: 20px;
}

.pagination-container {
  text-align: right;
}

.config-form {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
}

.config-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.config-actions {
  text-align: center;
  margin-top: 20px;
}

.empty-config {
  text-align: center;
  padding: 40px;
}

.test-config {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.test-result {
  margin-top: 20px;
}

.version-list {
  margin-bottom: 20px;
}

/* 预览对话框样式 */
.preview-content {
  max-height: 70vh;
  overflow-y: auto;
}

.preview-card {
  margin-bottom: 15px;
}

.preview-card:last-child {
  margin-bottom: 0;
}

.preview-empty {
  padding: 40px;
  text-align: center;
}
</style>
