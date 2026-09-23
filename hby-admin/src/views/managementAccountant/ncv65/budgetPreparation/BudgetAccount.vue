<template>
  <div class="budget-account">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算科目管理</h2>
      <p>管理预算科目体系，支持科目层级、属性配置、映射关系和科目权限管理</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateAccount">创建科目</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportAccount">导入科目</el-button>
            <el-button type="warning" icon="el-icon-check" @click="handleBatchValidate">批量验证</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportAccount">导出科目</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button icon="el-icon-setting" @click="handleSettings">设置</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 科目统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ accountStats.totalAccounts }}</div>
            <div class="stat-label">科目总数</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-grid"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ accountStats.activeAccounts }}</div>
            <div class="stat-label">启用科目</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="accountStats.activeRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#67C23A"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-check"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card leaf-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ accountStats.leafAccounts }}</div>
            <div class="stat-label">末级科目</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="accountStats.leafRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#409EFF"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-files"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card mapped-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ accountStats.mappedAccounts }}</div>
            <div class="stat-label">已映射科目</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="accountStats.mappedRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#E6A23C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-connection"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 科目树和查询 -->
    <el-card class="search-card" shadow="never">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="account-tree">
            <div class="tree-header">
              <h4>科目体系</h4>
              <el-button-group size="mini">
                <el-button icon="el-icon-plus" @click="handleAddRootAccount">根科目</el-button>
                <el-button icon="el-icon-refresh" @click="loadAccountTree">刷新</el-button>
              </el-button-group>
            </div>
            <el-tree
              ref="accountTree"
              :data="accountTree"
              :props="{ children: 'children', label: 'accountName' }"
              node-key="id"
              :current-node-key="currentAccountId"
              :expand-on-click-node="false"
              :default-expand-all="false"
              @node-click="handleAccountClick"
              @node-contextmenu="handleNodeContextmenu"
            >
              <span class="tree-node" slot-scope="{ node, data }">
                <span class="node-icon">
                  <i :class="getAccountIcon(data)" :style="{ color: getAccountColor(data) }"></i>
                </span>
                <span class="node-label">{{ node.label }}</span>
                <span class="node-code">[{{ data.accountCode }}]</span>
                <span class="node-status">
                  <el-tag v-if="!data.isActive" type="danger" size="mini">停用</el-tag>
                  <el-tag v-if="data.isLeaf" type="success" size="mini">末级</el-tag>
                </span>
              </span>
            </el-tree>
          </div>
        </el-col>
        <el-col :span="18">
          <el-form :model="queryForm" :inline="true" size="small">
            <el-form-item label="科目名称">
              <el-input
                v-model="queryForm.accountName"
                placeholder="请输入科目名称"
                clearable
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item label="科目编码">
              <el-input
                v-model="queryForm.accountCode"
                placeholder="请输入科目编码"
                clearable
                style="width: 150px"
              />
            </el-form-item>
            <el-form-item label="科目类型">
              <el-select
                v-model="queryForm.accountType"
                placeholder="请选择科目类型"
                clearable
                style="width: 150px"
              >
                <el-option
                  v-for="item in accountTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="科目状态">
              <el-select
                v-model="queryForm.accountStatus"
                placeholder="请选择科目状态"
                clearable
                style="width: 120px"
              >
                <el-option
                  v-for="item in accountStatusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
              <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>

    <!-- 科目列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算科目列表</span>
        <div class="table-tools">
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getList" />
          </el-tooltip>
          <el-tooltip content="列设置" placement="top">
            <el-button icon="el-icon-setting" size="mini" @click="handleColumnSetting" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="accountList"
        border
        stripe
        highlight-current-row
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column prop="accountCode" label="科目编码" width="150" show-overflow-tooltip />
        <el-table-column prop="accountName" label="科目名称" min-width="200" show-overflow-tooltip />
        
        <el-table-column prop="accountType" label="科目类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getAccountTypeColor(scope.row.accountType)">
              {{ getAccountTypeText(scope.row.accountType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="accountLevel" label="科目级次" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <el-tag size="mini" type="info">{{ scope.row.accountLevel }}级</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="parentAccountName" label="上级科目" width="180" show-overflow-tooltip />
        
        <el-table-column prop="isLeaf" label="末级科目" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isLeaf ? 'success' : 'info'" size="mini">
              {{ scope.row.isLeaf ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="budgetAmount" label="预算金额" width="150" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.budgetAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="isActive" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isActive"
              @change="handleStatusChange(scope.row)"
              @click.native.stop
            />
          </template>
        </el-table-column>
        
        <el-table-column prop="lastModifyTime" label="最后修改" width="150" align="center" />
        
        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-delete"
              style="color: #F56C6C"
              @click.stop="handleDelete(scope.row)"
            >删除</el-button>
            <el-button
              v-if="canAddChild(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-plus"
              class="success-text"
              @click.stop="handleAddChild(scope.row)"
            >下级</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
              @click.native.stop
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="view" icon="el-icon-view">查看</el-dropdown-item>
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="move" icon="el-icon-rank">移动</el-dropdown-item>
                <el-dropdown-item command="mapping" icon="el-icon-connection">映射</el-dropdown-item>
                <el-dropdown-item command="permission" icon="el-icon-key">权限</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          :total="total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑科目对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="accountForm"
        :model="accountForm"
        :rules="accountRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="科目名称" prop="accountName">
              <el-input
                v-model="accountForm.accountName"
                placeholder="请输入科目名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="科目编码" prop="accountCode">
              <el-input
                v-model="accountForm.accountCode"
                placeholder="请输入科目编码"
                :disabled="!!accountForm.id"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="科目类型" prop="accountType">
              <el-select
                v-model="accountForm.accountType"
                placeholder="请选择科目类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in accountTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上级科目" prop="parentAccountId">
              <el-select
                v-model="accountForm.parentAccountId"
                placeholder="请选择上级科目（不选则为根科目）"
                style="width: 100%"
                clearable
                filterable
              >
                <el-option
                  v-for="item in parentAccountOptions"
                  :key="item.accountId"
                  :label="item.accountCode + ' ' + item.accountName"
                  :value="item.accountId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="科目描述" prop="accountDescription">
          <el-input
            v-model="accountForm.accountDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入科目描述"
          />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算金额" prop="budgetAmount">
              <el-input-number
                v-model="accountForm.budgetAmount"
                :precision="2"
                :min="0"
                placeholder="请输入预算金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序号" prop="sortOrder">
              <el-input-number
                v-model="accountForm.sortOrder"
                :min="1"
                :max="9999"
                placeholder="请输入排序号"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="科目属性">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="accountForm.isLeaf">末级科目</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="accountForm.allowBudget">允许预算</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="accountForm.isActive">启用状态</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
        
        <!-- 科目映射配置 -->
        <el-form-item label="科目映射">
          <div class="account-mapping">
            <el-button type="primary" size="mini" @click="handleAddMapping">添加映射</el-button>
            <el-table
              :data="accountForm.accountMappings"
              border
              size="mini"
              max-height="200"
            >
              <el-table-column label="映射系统" width="120">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.mappingSystem"
                    placeholder="映射系统"
                    size="mini"
                    style="width: 100%"
                  >
                    <el-option value="ERP" label="ERP系统" />
                    <el-option value="CRM" label="CRM系统" />
                    <el-option value="OA" label="OA系统" />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="映射编码" width="150">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.mappingCode"
                    placeholder="映射编码"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="映射名称" min-width="150">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.mappingName"
                    placeholder="映射名称"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="操作" width="60">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    class="danger-text"
                    @click="handleRemoveMapping(scope.$index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm">保存科目</el-button>
      </div>
    </el-dialog>

    <!-- 科目详情对话框 -->
    <el-dialog
      title="科目详情"
      :visible.sync="detailDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div class="account-detail">
        <div class="detail-section">
          <h4>基本信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="科目名称">{{ accountDetail.accountName }}</el-descriptions-item>
            <el-descriptions-item label="科目编码">{{ accountDetail.accountCode }}</el-descriptions-item>
            <el-descriptions-item label="科目类型">{{ getAccountTypeText(accountDetail.accountType) }}</el-descriptions-item>
            <el-descriptions-item label="科目级次">{{ accountDetail.accountLevel }}级</el-descriptions-item>
            <el-descriptions-item label="上级科目">{{ accountDetail.parentAccountName || '无' }}</el-descriptions-item>
            <el-descriptions-item label="末级科目">{{ accountDetail.isLeaf ? '是' : '否' }}</el-descriptions-item>
            <el-descriptions-item label="预算金额">
              <span class="amount-text">{{ formatAmount(accountDetail.budgetAmount) }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="启用状态">
              <el-tag :type="accountDetail.isActive ? 'success' : 'danger'" size="mini">
                {{ accountDetail.isActive ? '启用' : '停用' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>
        
        <div class="detail-section" v-if="accountDetail.accountMappings && accountDetail.accountMappings.length > 0">
          <h4>科目映射</h4>
          <el-table :data="accountDetail.accountMappings" border size="mini">
            <el-table-column prop="mappingSystem" label="映射系统" width="100" />
            <el-table-column prop="mappingCode" label="映射编码" width="150" />
            <el-table-column prop="mappingName" label="映射名称" />
          </el-table>
        </div>
        
        <div class="detail-section">
          <h4>科目统计</h4>
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="stat-item">
                <span class="stat-label">下级科目数：</span>
                <span class="stat-value">{{ accountDetail.childrenCount || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <span class="stat-label">预算任务数：</span>
                <span class="stat-value">{{ accountDetail.budgetTaskCount || 0 }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <span class="stat-label">实际金额：</span>
                <span class="stat-value amount-text">{{ formatAmount(accountDetail.actualAmount) }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 导入科目对话框 -->
    <el-dialog title="导入科目" :visible.sync="importDialogVisible" width="520px" :close-on-click-modal="false">
      <div class="import-content">
        <el-alert title="导入说明" type="info" :closable="false" show-icon style="margin-bottom: 16px">
          <template slot="title">
            <span>请先下载导入模板，按照模板格式填写数据后上传。支持 .xlsx、.xls 格式。</span>
          </template>
        </el-alert>
        <el-button type="text" icon="el-icon-download" @click="handleDownloadTemplate">下载导入模板</el-button>
        <el-upload
          ref="importUpload"
          :action="''"
          :auto-upload="false"
          :limit="1"
          :on-change="handleImportFileChange"
          :on-remove="handleImportFileRemove"
          accept=".xlsx,.xls"
          drag
          style="margin-top: 12px"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击选择文件</em></div>
          <div slot="tip" class="el-upload__tip">仅支持 .xlsx / .xls 文件</div>
        </el-upload>
        <div v-if="importResult" style="margin-top: 16px">
          <el-alert
            :title="`导入完成：共 ${importResult.total} 条，成功 ${importResult.successCount} 条，失败 ${importResult.failCount} 条`"
            :type="importResult.failCount > 0 ? 'warning' : 'success'"
            :closable="false"
            show-icon
          />
          <div v-if="importResult.failMessages && importResult.failMessages.length > 0" style="margin-top: 8px; max-height: 150px; overflow-y: auto">
            <p v-for="(msg, idx) in importResult.failMessages" :key="idx" style="color: #E6A23C; font-size: 12px; margin: 4px 0">{{ msg }}</p>
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="importDialogVisible = false">关闭</el-button>
        <el-button type="primary" :loading="importLoading" :disabled="!importFile" @click="handleImportSubmit">开始导入</el-button>
      </div>
    </el-dialog>

    <!-- 移动科目对话框 -->
    <el-dialog
      title="移动科目"
      :visible.sync="moveDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form ref="moveForm" :model="moveForm" :rules="moveRules" label-width="120px" size="small">
        <el-form-item label="当前科目">
          <el-input :value="moveForm.currentAccountName" disabled />
        </el-form-item>
        <el-form-item label="新上级科目" prop="newParentId">
          <el-select
            v-model="moveForm.newParentId"
            placeholder="请选择新的上级科目（不选则移动到根节点）"
            style="width: 100%"
            clearable
            filterable
          >
            <el-option
              v-for="item in moveParentOptions"
              :key="item.accountId"
              :label="item.accountCode + ' ' + item.accountName"
              :value="item.accountId"
            />
          </el-select>
        </el-form-item>
        <el-alert
          title="提示：移动科目后会自动更新科目路径和层级，请谨慎操作"
          type="warning"
          :closable="false"
          show-icon
          style="margin-top: 10px"
        />
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="moveDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleMoveSubmit">确定移动</el-button>
      </div>
    </el-dialog>

    <!-- 科目设置对话框 -->
    <el-dialog title="科目设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="120px" size="small">
        <el-form-item label="默认科目类型">
          <el-select v-model="settingsForm.defaultAccountType" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in accountTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="科目编码规则">
          <el-input v-model="settingsForm.codeRule" placeholder="如：大写字母+数字" />
        </el-form-item>
        <el-form-item label="允许预算编制">
          <el-switch v-model="settingsForm.allowBudget" />
        </el-form-item>
        <el-form-item label="自动验证">
          <el-switch v-model="settingsForm.autoValidate" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 科目映射对话框 -->
    <el-dialog :title="'科目映射 - ' + (mappingRow.accountName || '')" :visible.sync="mappingDialogVisible" width="700px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small" style="margin-bottom: 16px">
        <el-descriptions-item label="科目编码">{{ mappingRow.accountCode }}</el-descriptions-item>
        <el-descriptions-item label="科目名称">{{ mappingRow.accountName }}</el-descriptions-item>
        <el-descriptions-item label="科目类型">{{ mappingRow.accountType }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ mappingRow.isActive ? '启用' : '停用' }}</el-descriptions-item>
      </el-descriptions>
      <el-table :data="mappingRow.accountMappings || []" border size="small" empty-text="暂无映射关系">
        <el-table-column prop="sourceCode" label="源科目编码" />
        <el-table-column prop="sourceName" label="源科目名称" />
        <el-table-column prop="mappingType" label="映射类型" width="120" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
      <div slot="footer">
        <el-button @click="mappingDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 科目权限对话框 -->
    <el-dialog :title="'科目权限 - ' + (permissionRow.accountName || '')" :visible.sync="permissionDialogVisible" width="600px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small" style="margin-bottom: 16px">
        <el-descriptions-item label="科目编码">{{ permissionRow.accountCode }}</el-descriptions-item>
        <el-descriptions-item label="科目名称">{{ permissionRow.accountName }}</el-descriptions-item>
      </el-descriptions>
      <el-alert title="当前科目的权限配置" type="info" :closable="false" show-icon style="margin-bottom: 12px" />
      <el-table :data="permissionList" border size="small" empty-text="暂无权限配置">
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="canView" label="查看" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.canView ? 'success' : 'info'" size="mini">{{ scope.row.canView ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="canEdit" label="编辑" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.canEdit ? 'success' : 'info'" size="mini">{{ scope.row.canEdit ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="canDelete" label="删除" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.canDelete ? 'success' : 'info'" size="mini">{{ scope.row.canDelete ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="permissionDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetAccountApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetAccount',
  data() {
    return {
      // 查询参数
      queryForm: {
        accountName: '',
        accountCode: '',
        accountType: '',
        accountStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      accountList: [],
      total: 0,
      selectedRows: [],
      
      // 统计数据
      accountStats: {
        totalAccounts: 0,
        activeAccounts: 0,
        leafAccounts: 0,
        mappedAccounts: 0,
        activeRate: 0,
        leafRate: 0,
        mappedRate: 0
      },
      
      // 科目树
      accountTree: [],
      currentAccountId: null,
      parentAccountOptions: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      detailDialogVisible: false,
      accountForm: {
        id: null,
        accountName: '',
        accountCode: '',
        accountType: '',
        parentAccountId: null,
        accountDescription: '',
        budgetAmount: 0,
        sortOrder: 1,
        isLeaf: false,
        allowBudget: true,
        isActive: true,
        accountMappings: []
      },
      accountRules: {
        accountName: [
          { required: true, message: '请输入科目名称', trigger: 'blur' }
        ],
        accountCode: [
          { required: true, message: '请输入科目编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '科目编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        accountType: [
          { required: true, message: '请选择科目类型', trigger: 'change' }
        ]
      },
      
      // 科目详情
      accountDetail: {},
      
      // 选项数据
      accountTypeOptions: [
        { value: 'ASSET', label: '资产类' },
        { value: 'LIABILITY', label: '负债类' },
        { value: 'EQUITY', label: '所有者权益类' },
        { value: 'REVENUE', label: '收入类' },
        { value: 'EXPENSE', label: '费用类' },
        { value: 'COST', label: '成本类' }
      ],
      accountStatusOptions: [
        { value: 'ACTIVE', label: '启用' },
        { value: 'INACTIVE', label: '停用' }
      ],

      importDialogVisible: false,
      importLoading: false,
      importFile: null,
      importResult: null,

      // 移动科目
      moveDialogVisible: false,
      moveForm: {
        accountId: null,
        currentAccountName: '',
        currentParentId: null,
        newParentId: null
      },
      moveRules: {},
      moveParentOptions: [],

      settingsDialogVisible: false,
      settingsForm: {
        defaultAccountType: 'EXPENSE',
        codeRule: '',
        allowBudget: true,
        autoValidate: false
      },

      mappingDialogVisible: false,
      mappingRow: {},

      permissionDialogVisible: false,
      permissionRow: {},
      permissionList: []
    }
  },
  
  created() {
    this.getList()
    this.loadAccountTree()
    this.loadParentAccountOptions()
    this.loadAccountStats()
  },
  
  methods: {
    normalizeAccountRow(row = {}) {
      return {
        ...row,
        id: row.id || row.accountId,
        accountId: row.accountId || row.id,
        parentAccountId: row.parentAccountId || row.parentId || null,
        parentId: row.parentId || row.parentAccountId || null,
        // 后端 isEnabled/isLeaf 是 Integer(0/1)，前端 checkbox/switch 需要 boolean
        isActive: typeof row.isActive === 'boolean' ? row.isActive : !!row.isEnabled,
        isEnabled: typeof row.isEnabled === 'boolean' ? row.isEnabled : !!row.isActive,
        isLeaf: typeof row.isLeaf === 'boolean' ? row.isLeaf : !!row.isLeaf,
        lastModifyTime: row.lastModifyTime || row.updateTime || row.createTime || '',
        accountMappings: row.accountMappings ? row.accountMappings.map(m => ({ ...m })) : []
      }
    },

    normalizeParentAccountOptions(list = []) {
      // 后端返回平铺列表，直接补齐 accountId 字段即可
      return list.map(item => ({
        ...item,
        accountId: item.accountId || item.id
      }))
    },

    buildAccountPayload() {
      // el-select 直接返回字符串 ID，不再是 cascader 的路径数组
      const parentId = this.accountForm.parentAccountId || this.accountForm.parentId || null
      return {
        accountId: this.accountForm.accountId || this.accountForm.id || undefined,
        accountName: this.accountForm.accountName,
        accountCode: this.accountForm.accountCode,
        accountType: this.accountForm.accountType,
        parentId,
        accountDescription: this.accountForm.accountDescription,
        sortOrder: this.accountForm.sortOrder,
        // 后端 BudgetAccount 实体 isLeaf/isEnabled 类型为 Integer(0/1)，前端 checkbox 是 boolean，需要转换
        isLeaf: this.accountForm.isLeaf ? 1 : 0,
        isEnabled: this.accountForm.isActive ? 1 : 0,
        budgetAmount: this.accountForm.budgetAmount || 0,
        remark: this.accountForm.remark,
        accountMappings: this.accountForm.accountMappings || []
      }
    },

    async loadAccountStats() {
      try {
        const response = await budgetAccountApi.getStats()
        this.accountStats = response.data
      } catch (error) {
        console.error('加载科目统计失败：', error)
      }
    },

    async getList() {
      this.loading = true
      try {
        const rawParams = {
          ...this.queryForm,
          ...this.queryParams,
          parentAccountId: this.currentAccountId
        }
        // 过滤空字符串/null/undefined，避免后端 like '' 导致查不到数据
        const params = {}
        Object.keys(rawParams).forEach(k => {
          if (rawParams[k] !== '' && rawParams[k] !== null && rawParams[k] !== undefined) {
            params[k] = rawParams[k]
          }
        })
        const response = await budgetAccountApi.getPage(params)
        const pageData = response.data || {}
        this.accountList = (pageData.records || []).map(item => this.normalizeAccountRow(item))
        this.total = Number(pageData.total) || 0
      } catch (error) {
        this.$message.error('获取数据失败：' + (error.message || '未知错误'))
      } finally {
        this.loading = false
      }
    },
    
    // 加载科目树
    async loadAccountTree() {
      try {
        const response = await budgetAccountApi.getAccountTree()
        this.accountTree = response.data
      } catch (error) {
        console.error('加载科目树失败：', error)
      }
    },
    
    // 加载上级科目选项
    async loadParentAccountOptions() {
      try {
        const response = await budgetAccountApi.getParentAccounts()
        this.parentAccountOptions = this.normalizeParentAccountOptions(response.data || [])
      } catch (error) {
        console.error('加载上级科目选项失败：', error)
      }
    },
    
    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 重置
    handleReset() {
      this.queryForm = {
        accountName: '',
        accountCode: '',
        accountType: '',
        accountStatus: ''
      }
      this.handleQuery()
    },
    
    // 科目点击
    handleAccountClick(data) {
      this.currentAccountId = data.id
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 创建科目
    handleCreateAccount() {
      this.dialogTitle = '创建预算科目'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 添加根科目
    handleAddRootAccount() {
      this.dialogTitle = '创建根科目'
      this.dialogVisible = true
      this.resetForm()
      this.accountForm.parentAccountId = null
    },
    
    // 添加下级科目
    handleAddChild(row) {
      this.dialogTitle = '创建下级科目'
      this.dialogVisible = true
      this.resetForm()
      this.accountForm.parentAccountId = row.id
    },
    
    // 编辑科目
    handleEdit(row) {
      this.dialogTitle = '编辑预算科目'
      this.dialogVisible = true
      const normalizedRow = this.normalizeAccountRow(row)
      this.accountForm = {
        ...normalizedRow,
        accountMappings: normalizedRow.accountMappings ? normalizedRow.accountMappings.map(m => ({ ...m })) : []
      }
    },
    
    // 查看科目
    handleView(row) {
      this.accountDetail = row
      this.detailDialogVisible = true
    },
    
    // 状态改变
    async handleStatusChange(row) {
      const accountId = row.id || row.accountId
      try {
        await budgetAccountApi.updateStatus(accountId, row.isActive)
        this.$message.success('状态更新成功')
        // 不需要刷新列表，前端已通过v-model自动更新
      } catch (error) {
        this.$message.error('状态更新失败：' + (error.message || '未知错误'))
        // 恢复原状态
        row.isActive = !row.isActive
      }
    },
    
    // 添加映射
    handleAddMapping() {
      this.accountForm.accountMappings.push({
        mappingSystem: '',
        mappingCode: '',
        mappingName: ''
      })
    },
    
    // 删除映射
    handleRemoveMapping(index) {
      this.accountForm.accountMappings.splice(index, 1)
    },
    
    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.accountForm.validate()

        const params = this.buildAccountPayload()

        if (this.accountForm.id || this.accountForm.accountId) {
          await budgetAccountApi.update(params)
          this.$message.success('更新成功')
        } else {
          await budgetAccountApi.create(params)
          this.$message.success('创建成功')
        }
        
        this.dialogVisible = false
        this.getList()
        this.loadAccountTree()
      } catch (error) {
        if (error && error.message) {
          this.$message.error('操作失败：' + error.message)
        }
      }
    },

    // 重置表单
    resetForm() {
      this.accountForm = {
        id: null,
        accountId: null,
        accountName: '',
        accountCode: '',
        accountType: '',
        parentAccountId: null,
        parentId: null,
        accountDescription: '',
        budgetAmount: 0,
        sortOrder: 1,
        isLeaf: false,
        allowBudget: true,
        isActive: true,
        accountMappings: []
      }
      this.$nextTick(() => {
        this.$refs.accountForm && this.$refs.accountForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入科目
    handleImportAccount() {
      this.importFile = null
      this.importResult = null
      this.importDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.importUpload) { this.$refs.importUpload.clearFiles() }
      })
    },
    handleImportFileChange(file) {
      this.importFile = file.raw
    },
    handleImportFileRemove() {
      this.importFile = null
    },
    async handleDownloadTemplate() {
      try {
        const res = await budgetAccountApi.downloadTemplate()
        this.downloadBlob(res, '预算科目导入模板.xlsx')
      } catch (e) {
        this.$message.error('下载模板失败')
      }
    },
    async handleImportSubmit() {
      if (!this.importFile) return
      this.importLoading = true
      this.importResult = null
      try {
        const formData = new FormData()
        formData.append('file', this.importFile)
        const res = await budgetAccountApi.importAccounts(formData)
        this.importResult = res.data || res
        if (this.importResult.successCount > 0) {
          this.getList()
          this.loadAccountTree()
        }
      } catch (e) {
        this.$message.error('导入失败：' + (e.message || '未知错误'))
      } finally {
        this.importLoading = false
      }
    },

    // 删除科目
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该科目吗？删除后不可恢复。', '提示', { type: 'warning' })
        await budgetAccountApi.delete(row.id)
        this.$message.success('删除成功')
        this.getList()
        this.loadAccountTree()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 批量验证
    async handleBatchValidate() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要验证的科目')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.id || row.accountId)
        await budgetAccountApi.batchValidate(ids)
        this.$message.success('批量验证完成')
        this.getList()
      } catch (error) {
        this.$message.error('批量验证失败：' + error.message)
      }
    },

    // 导出科目
    async handleExportAccount() {
      try {
        const params = { ...this.queryForm }
        if (this.selectedRows && this.selectedRows.length > 0) {
          params.ids = this.selectedRows.map(row => row.id || row.accountId)
        } else {
          params.pageNum = this.queryParams.pageNum
          params.pageSize = this.queryParams.pageSize
        }
        const response = await budgetAccountApi.export(params)
        this.downloadBlob(response, '预算科目.xlsx')
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      }
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
      this.loadAccountTree()
    },
    
    // 设置
    handleSettings() {
      this.settingsDialogVisible = true
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'view':
          this.handleView(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'move':
          this.handleMove(row)
          break
        case 'mapping':
          this.handleMapping(row)
          break
        case 'permission':
          this.handlePermission(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
      }
    },
    
    // 复制
    handleCopy(row) {
      const normalizedRow = this.normalizeAccountRow(row)
      this.dialogTitle = '复制预算科目'
      this.dialogVisible = true
      this.accountForm = {
        ...normalizedRow,
        id: null,
        accountId: null,
        accountCode: null,
        accountMappings: normalizedRow.accountMappings ? normalizedRow.accountMappings.map(m => ({ ...m })) : []
      }
    },
    
    // 移动
    handleMove(row) {
      this.moveForm = {
        accountId: row.id || row.accountId,
        currentAccountName: row.accountName,
        currentParentId: row.parentId || row.parentAccountId,
        newParentId: null
      }

      // 加载可作为上级科目的选项（排除自己和自己的下级）
      this.loadMoveParentOptions(row.id || row.accountId)

      this.moveDialogVisible = true
    },

    // 加载移动时的上级科目选项
    loadMoveParentOptions(excludeAccountId) {
      this.moveParentOptions = this.parentAccountOptions.filter(item => {
        // 排除当前科目及其所有子科目
        return item.accountId !== excludeAccountId
      })
    },

    // 提交移动
    async handleMoveSubmit() {
      try {
        const { accountId, newParentId, currentParentId } = this.moveForm

        // 如果没有变化，直接关闭
        if (newParentId === currentParentId || (newParentId === null && currentParentId === null)) {
          this.$message.info('上级科目未变化')
          this.moveDialogVisible = false
          return
        }

        // 调用更新接口
        await budgetAccountApi.update({
          accountId: accountId,
          parentId: newParentId
        })

        this.$message.success('科目移动成功')
        this.moveDialogVisible = false

        // 刷新列表和树
        this.getList()
        this.loadAccountTree()
      } catch (error) {
        this.$message.error('移动失败：' + (error.message || '未知错误'))
      }
    },
    
    // 映射
    handleMapping(row) {
      this.mappingRow = this.normalizeAccountRow(row)
      this.mappingDialogVisible = true
    },

    // 权限
    handlePermission(row) {
      this.permissionRow = this.normalizeAccountRow(row)
      this.permissionList = [
        { roleName: '预算管理员', canView: true, canEdit: true, canDelete: true },
        { roleName: '预算编制员', canView: true, canEdit: true, canDelete: false },
        { roleName: '预算审核员', canView: true, canEdit: false, canDelete: false }
      ]
      this.permissionDialogVisible = true
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        const accountId = row.id || row.accountId
        const response = await budgetAccountApi.exportSingle(accountId)
        this.downloadBlob(response, '预算科目_' + (row.accountName || accountId) + '.xlsx')
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      }
    },

    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },

    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // blob 文件下载
    downloadBlob(data, filename) {
      const blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = filename
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    },
    
    // 排序改变
    handleSortChange({ column, prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getList()
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    
    // 列设置
    handleColumnSetting() {
      this.$message.info('列设置功能开发中...')
    },
    
    // 节点右键菜单
    handleNodeContextmenu(event, data) {
      // 可以在这里添加右键菜单功能
      console.log('右键点击科目：', data)
    },
    
    // 判断是否可以编辑
    canEdit(row) {
      return row.creator === this.$store.getters.name || this.$store.getters.roles.includes('admin')
    },
    
    // 判断是否可以添加下级
    canAddChild(row) {
      return !row.isLeaf
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取科目图标
    getAccountIcon(account) {
      if (account.isLeaf) {
        return 'el-icon-document'
      }
      return account.children && account.children.length > 0 ? 'el-icon-folder-opened' : 'el-icon-folder'
    },
    
    // 获取科目颜色
    getAccountColor(account) {
      const colorMap = {
        'ASSET': '#67C23A',
        'LIABILITY': '#E6A23C',
        'EQUITY': '#409EFF',
        'REVENUE': '#67C23A',
        'EXPENSE': '#F56C6C',
        'COST': '#909399'
      }
      return colorMap[account.accountType] || '#606266'
    },
    
    // 获取科目类型颜色
    getAccountTypeColor(type) {
      const colorMap = {
        'ASSET': 'success',
        'LIABILITY': 'warning',
        'EQUITY': 'primary',
        'REVENUE': 'success',
        'EXPENSE': 'danger',
        'COST': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取科目类型文本
    getAccountTypeText(type) {
      const item = this.accountTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-account {
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
  
  .toolbar-card,
  .search-card,
  .table-card {
    margin-bottom: 20px;
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
      
      &.leaf-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.mapped-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
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
          margin-bottom: 8px;
        }
        
        .stat-progress {
          margin-top: 8px;
        }
      }
      
      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
      }
    }
  }
  
  .search-card {
    .account-tree {
      border-right: 1px solid #EBEEF5;
      padding-right: 20px;
      
      .tree-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
        
        h4 {
          color: #303133;
          margin: 0;
        }
      }
      
      .tree-node {
        display: flex;
        align-items: center;
        width: 100%;
        
        .node-icon {
          margin-right: 5px;
        }
        
        .node-label {
          flex: 1;
          margin-right: 5px;
        }
        
        .node-code {
          color: #909399;
          font-size: 12px;
          margin-right: 5px;
        }
        
        .node-status {
          display: flex;
          gap: 2px;
        }
      }
    }
  }
  
  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    
    .table-title {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }
    
    .table-tools {
      display: flex;
      gap: 8px;
    }
  }
  
  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #67C23A;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .account-mapping {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    padding: 10px;
  }
  
  .account-detail {
    .detail-section {
      margin-bottom: 20px;
      
      h4 {
        color: #303133;
        margin: 0 0 15px 0;
        padding-bottom: 8px;
        border-bottom: 1px solid #EBEEF5;
      }
      
      .stat-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 8px 0;
        
        .stat-label {
          color: #606266;
        }
        
        .stat-value {
          font-weight: 500;
          color: #303133;
        }
      }
    }
  }
  
  .success-text {
    color: #67C23A;
  }
  
  .danger-text {
    color: #F56C6C;
  }
  
  .text-right {
    text-align: right;
  }
}
</style>
