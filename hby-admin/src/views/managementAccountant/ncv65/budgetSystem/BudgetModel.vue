<template>
  <div class="budget-model">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算模型管理</h2>
      <p>管理预算模型的创建、配置、版本控制和应用，支持多种预算模型类型和自定义配置</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建模型</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExport">导出模型</el-button>
            <el-button type="info" icon="el-icon-upload2" @click="handleImport">导入模型</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-copy-document" @click="handleBatchCopy">批量复制</el-button>
            <el-button icon="el-icon-delete" @click="handleBatchDelete">批量删除</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 模型统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ modelStats.totalModels }}</div>
            <div class="stat-label">模型总数</div>
            <div class="stat-description">已创建的预算模型</div>
            <div class="stat-trend">
              <i class="el-icon-s-data"></i>
              <span>多样化模型</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-data"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ modelStats.activeModels }}</div>
            <div class="stat-label">活跃模型</div>
            <div class="stat-description">正在使用的模型</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>运行良好</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card types-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ modelStats.modelTypes }}</div>
            <div class="stat-label">模型类型</div>
            <div class="stat-description">支持的模型类型数</div>
            <div class="stat-trend">
              <i class="el-icon-menu"></i>
              <span>类型丰富</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-menu"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card versions-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ modelStats.totalVersions }}</div>
            <div class="stat-label">版本总数</div>
            <div class="stat-description">所有模型版本数</div>
            <div class="stat-trend">
              <i class="el-icon-collection"></i>
              <span>版本管理</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-collection"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="filter-card" shadow="never">
      <div class="filter-header">
        <span class="filter-title">查询条件</span>
        <el-button type="text" @click="handleResetQuery">重置条件</el-button>
      </div>
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="模型名称">
          <el-input
            v-model="queryForm.modelName"
            placeholder="请输入模型名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="模型类型">
          <el-select
            v-model="queryForm.modelType"
            placeholder="请选择模型类型"
            clearable
            style="width: 150px"
          >
            <el-option value="INCREMENTAL" label="增量预算" />
            <el-option value="ZERO_BASED" label="零基预算" />
            <el-option value="ROLLING" label="滚动预算" />
            <el-option value="FLEXIBLE" label="弹性预算" />
            <el-option value="ACTIVITY_BASED" label="作业预算" />
            <el-option value="CAPITAL" label="资本预算" />
            <el-option value="CASH_FLOW" label="现金流预算" />
            <el-option value="CUSTOM" label="自定义模型" />
          </el-select>
        </el-form-item>
        <el-form-item label="模型状态">
          <el-select
            v-model="queryForm.modelStatus"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option value="DRAFT" label="草稿" />
            <el-option value="ACTIVE" label="启用" />
            <el-option value="INACTIVE" label="停用" />
            <el-option value="ARCHIVED" label="归档" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="queryForm.createTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="创建人">
          <el-input
            v-model="queryForm.creator"
            placeholder="请输入创建人"
            clearable
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleResetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 模型类型快速选择 -->
    <el-card class="model-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>模型类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshModelTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="3" v-for="modelType in modelTypes" :key="modelType.id">
          <el-card 
            class="model-type-item" 
            shadow="hover" 
            @click.native="handleSelectModelType(modelType)"
            :class="{ 'selected': selectedModelType === modelType.id }"
          >
            <div class="model-type-icon">
              <i :class="modelType.icon"></i>
            </div>
            <div class="model-type-title">{{ modelType.name }}</div>
            <div class="model-type-description">{{ modelType.description }}</div>
            <div class="model-type-stats">
              <span class="usage-count">{{ modelType.usageCount }} 个模型</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span>模型列表</span>
        <div class="header-tools">
          <el-tooltip content="刷新数据" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getList" />
          </el-tooltip>
          <el-tooltip content="密度" placement="top">
            <el-dropdown @command="handleSizeChange">
              <el-button size="mini">
                <i class="el-icon-operation"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="large">宽松</el-dropdown-item>
                <el-dropdown-item command="default">默认</el-dropdown-item>
                <el-dropdown-item command="small">紧凑</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-tooltip>
        </div>
      </div>
      <el-table
        :data="modelList"
        border
        stripe
        highlight-current-row
        :size="tableSize"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="modelName" label="模型名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.modelName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="modelType" label="模型类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getModelTypeColor(scope.row.modelType)" size="mini">
              {{ getModelTypeText(scope.row.modelType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="version" label="版本" width="80" align="center">
          <template slot-scope="scope">
            <el-tag type="info" size="mini">v{{ scope.row.version || '1.0' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="modelStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.modelStatus)" size="mini">
              {{ getStatusText(scope.row.modelStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applicableScope" label="适用范围" width="150" show-overflow-tooltip />
        <el-table-column prop="creatorName" label="创建人" width="100" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
        <el-table-column prop="updateTime" label="最后修改" width="160" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >查看</el-button>
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
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="version">版本管理</el-dropdown-item>
                <el-dropdown-item command="apply">应用模型</el-dropdown-item>
                <el-dropdown-item command="export">导出</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="modelForm"
        :model="modelForm"
        :rules="modelRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模型名称" prop="modelName">
              <el-input v-model="modelForm.modelName" placeholder="请输入模型名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型编码" prop="modelCode">
              <el-input v-model="modelForm.modelCode" placeholder="请输入模型编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模型类型" prop="modelType">
              <el-select v-model="modelForm.modelType" placeholder="请选择模型类型" style="width: 100%">
                <el-option value="INCREMENTAL" label="增量预算" />
                <el-option value="ZERO_BASED" label="零基预算" />
                <el-option value="ROLLING" label="滚动预算" />
                <el-option value="FLEXIBLE" label="弹性预算" />
                <el-option value="ACTIVITY_BASED" label="作业预算" />
                <el-option value="CAPITAL" label="资本预算" />
                <el-option value="CASH_FLOW" label="现金流预算" />
                <el-option value="CUSTOM" label="自定义模型" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型状态" prop="modelStatus">
              <el-select v-model="modelForm.modelStatus" placeholder="请选择状态" style="width: 100%">
                <el-option value="DRAFT" label="草稿" />
                <el-option value="ACTIVE" label="启用" />
                <el-option value="INACTIVE" label="停用" />
                <el-option value="ARCHIVED" label="归档" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="适用范围" prop="applicableScope">
              <el-input v-model="modelForm.applicableScope" placeholder="请输入适用范围" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算周期" prop="budgetCycle">
              <el-select v-model="modelForm.budgetCycle" placeholder="请选择预算周期" style="width: 100%">
                <el-option value="MONTHLY" label="月度" />
                <el-option value="QUARTERLY" label="季度" />
                <el-option value="YEARLY" label="年度" />
                <el-option value="CUSTOM" label="自定义" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="模型描述" prop="modelDescription">
          <el-input
            v-model="modelForm.modelDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入模型描述"
          />
        </el-form-item>
        <el-form-item label="配置参数" prop="configParameters">
          <el-input
            v-model="modelForm.configParameters"
            type="textarea"
            :rows="4"
            placeholder="请输入JSON格式的配置参数"
          />
        </el-form-item>
        <el-form-item label="计算规则" prop="calculationRules">
          <el-input
            v-model="modelForm.calculationRules"
            type="textarea"
            :rows="4"
            placeholder="请输入计算规则"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 模型详情抽屉 -->
    <el-drawer
      title="模型详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="60%"
    >
      <div class="detail-content" v-if="currentModel">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="模型基本信息" :column="2" border>
              <el-descriptions-item label="模型名称">{{ currentModel.modelName }}</el-descriptions-item>
              <el-descriptions-item label="模型编码">{{ currentModel.modelCode }}</el-descriptions-item>
              <el-descriptions-item label="模型类型">{{ getModelTypeText(currentModel.modelType) }}</el-descriptions-item>
              <el-descriptions-item label="模型状态">
                <el-tag :type="getStatusColor(currentModel.modelStatus)" size="mini">
                  {{ getStatusText(currentModel.modelStatus) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="适用范围">{{ currentModel.applicableScope }}</el-descriptions-item>
              <el-descriptions-item label="预算周期">{{ currentModel.budgetCycle }}</el-descriptions-item>
              <el-descriptions-item label="版本">{{ currentModel.version }}</el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentModel.creatorName }}</el-descriptions-item>
              <el-descriptions-item label="创建时间" :span="2">{{ currentModel.createTime }}</el-descriptions-item>
              <el-descriptions-item label="模型描述" :span="2">{{ currentModel.modelDescription }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="配置参数" name="config">
            <el-card shadow="never">
              <div slot="header">配置参数</div>
              <pre class="config-content">{{ formatJSON(currentModel.configParameters) }}</pre>
            </el-card>
          </el-tab-pane>
          <el-tab-pane label="计算规则" name="rules">
            <el-card shadow="never">
              <div slot="header">计算规则</div>
              <div class="rules-content">{{ currentModel.calculationRules }}</div>
            </el-card>
          </el-tab-pane>
          <el-tab-pane label="版本历史" name="versions">
            <el-table :data="modelVersions" border size="mini">
              <el-table-column prop="versionNo" label="版本号" width="80" />
              <el-table-column prop="versionName" label="版本名称" width="150" />
              <el-table-column prop="changeDescription" label="版本说明" show-overflow-tooltip />
              <el-table-column prop="versionStatus" label="状态" width="80">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.versionStatus === 'PUBLISHED' ? 'success' : scope.row.versionStatus === 'ARCHIVED' ? 'info' : 'warning'" size="mini">
                    {{ scope.row.versionStatus === 'PUBLISHED' ? '已发布' : scope.row.versionStatus === 'ARCHIVED' ? '已归档' : '草稿' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="isCurrent" label="当前版本" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.isCurrent === 1" type="success" size="mini">是</el-tag>
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column prop="creatorName" label="创建人" width="100" />
              <el-table-column prop="createTime" label="创建时间" width="150" />
              <el-table-column label="操作" width="120">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" @click="handleViewVersion(scope.row)">查看</el-button>
                  <el-button type="text" size="mini" @click="handleRestoreVersion(scope.row)">恢复</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 帮助弹窗 -->
    <el-dialog title="预算模型帮助" :visible.sync="helpDialogVisible" width="700px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>预算模型管理模块用于创建、配置和管理各类预算模型，支持以下功能：</p>
        <el-collapse>
          <el-collapse-item title="1. 新建模型" name="1">
            <p>点击"新建模型"按钮，填写模型名称、编码、类型等信息后提交创建。模型编码支持大写字母、数字和下划线。</p>
          </el-collapse-item>
          <el-collapse-item title="2. 模型类型" name="2">
            <p>系统支持8种预算模型类型：增量预算、零基预算、滚动预算、弹性预算、作业预算、资本预算、现金流预算、自定义模型。点击类型卡片可快速筛选对应类型的模型。</p>
          </el-collapse-item>
          <el-collapse-item title="3. 批量操作" name="3">
            <p>在表格中勾选多个模型后，可进行批量复制或批量删除操作。批量复制会为每个模型创建一个副本。</p>
          </el-collapse-item>
          <el-collapse-item title="4. 导入导出" name="4">
            <p>导出：将当前列表数据导出为Excel文件。导入：选择符合模板格式的Excel文件，系统将自动解析并创建模型。</p>
          </el-collapse-item>
          <el-collapse-item title="5. 查询筛选" name="5">
            <p>支持按模型名称、类型、状态、创建时间范围、创建人等条件进行组合查询。</p>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="helpDialogVisible = false">知道了</el-button>
      </div>
    </el-dialog>

    <!-- 版本管理弹窗 -->
    <el-dialog
      :title="'版本管理 - ' + versionDialogModelName"
      :visible.sync="versionDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <el-table :data="versionList" border size="small" v-loading="versionLoading">
        <el-table-column prop="versionNo" label="版本号" width="80" align="center" />
        <el-table-column prop="versionName" label="版本名称" width="160" show-overflow-tooltip />
        <el-table-column prop="versionStatus" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.versionStatus === 'PUBLISHED' ? 'success' : scope.row.versionStatus === 'ARCHIVED' ? 'info' : 'warning'" size="mini">
              {{ scope.row.versionStatus === 'PUBLISHED' ? '已发布' : scope.row.versionStatus === 'ARCHIVED' ? '已归档' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="changeType" label="变更类型" width="80" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.changeType === 'CREATE' ? '创建' : scope.row.changeType === 'UPDATE' ? '更新' : '复制' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="changeDescription" label="变更说明" show-overflow-tooltip />
        <el-table-column prop="isCurrent" label="当前版本" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isCurrent === 1" type="success" size="mini">当前</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="creatorName" label="创建人" width="80" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="handleViewVersion(scope.row)">查看</el-button>
            <el-button type="text" size="mini" @click="handleRestoreVersion(scope.row)" :disabled="scope.row.isCurrent === 1">恢复</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="versionDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 版本详情弹窗 -->
    <el-dialog
      title="版本详情"
      :visible.sync="versionDetailVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <div v-if="currentVersion" v-loading="versionDetailLoading">
        <el-descriptions title="版本基本信息" :column="2" border size="small">
          <el-descriptions-item label="版本号">{{ currentVersion.versionNo }}</el-descriptions-item>
          <el-descriptions-item label="版本名称">{{ currentVersion.versionName }}</el-descriptions-item>
          <el-descriptions-item label="版本状态">
            <el-tag :type="currentVersion.versionStatus === 'PUBLISHED' ? 'success' : currentVersion.versionStatus === 'ARCHIVED' ? 'info' : 'warning'" size="mini">
              {{ currentVersion.versionStatus === 'PUBLISHED' ? '已发布' : currentVersion.versionStatus === 'ARCHIVED' ? '已归档' : '草稿' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="是否当前版本">
            <el-tag v-if="currentVersion.isCurrent === 1" type="success" size="mini">是</el-tag>
            <span v-else>否</span>
          </el-descriptions-item>
          <el-descriptions-item label="版本类型">{{ currentVersion.versionType === 'MANUAL' ? '手动创建' : '自动创建' }}</el-descriptions-item>
          <el-descriptions-item label="变更类型">{{ currentVersion.changeType === 'CREATE' ? '创建' : currentVersion.changeType === 'UPDATE' ? '更新' : '复制' }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ currentVersion.creatorName }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentVersion.createTime }}</el-descriptions-item>
          <el-descriptions-item label="变更说明" :span="2">{{ currentVersion.changeDescription || '-' }}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="left">模型配置快照</el-divider>
        <pre class="config-content">{{ formatJSON(currentVersion.modelConfig) }}</pre>
      </div>
      <div slot="footer">
        <el-button @click="versionDetailVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleRestoreVersion(currentVersion)" :disabled="!currentVersion || currentVersion.isCurrent === 1">恢复此版本</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  budgetSystemApi,
  createBudgetModel,
  updateBudgetModel,
  deleteBudgetModel,
  getBudgetModel,
  getBudgetModelPage,
  copyBudgetModel,
  applyBudgetModel,
  batchDeleteBudgetModels,
  batchCopyBudgetModels,
  getBudgetModelStats,
  getBudgetModelTypeStats,
  getBudgetModelVersions,
  getBudgetModelVersionDetail,
  restoreBudgetModelVersion,
  exportBudgetModel,
  batchExportBudgetModels,
  importBudgetModel
} from '@/api/managementAccountant/ncv65/budgetSystem'

export default {
  name: 'BudgetModel',
  data() {
    return {
      // 查询参数
      queryForm: {
        modelName: '',
        modelType: '',
        modelStatus: '',
        createTime: [],
        creator: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },

      // 表格数据
      modelList: [],
      total: 0,
      loading: false,
      tableSize: 'default',
      selectedRows: [],

      // 统计数据
      modelStats: {
        totalModels: 0,
        activeModels: 0,
        modelTypes: 0,
        totalVersions: 0
      },
      
      // 模型类型
      modelTypes: [
        { id: 1, name: '增量预算', value: 'INCREMENTAL', description: '基于历史数据增量调整', icon: 'el-icon-trend-charts', usageCount: 0 },
        { id: 2, name: '零基预算', value: 'ZERO_BASED', description: '从零开始编制预算', icon: 'el-icon-refresh', usageCount: 0 },
        { id: 3, name: '滚动预算', value: 'ROLLING', description: '动态滚动调整预算', icon: 'el-icon-refresh-right', usageCount: 0 },
        { id: 4, name: '弹性预算', value: 'FLEXIBLE', description: '根据业务量弹性调整', icon: 'el-icon-s-operation', usageCount: 0 },
        { id: 5, name: '作业预算', value: 'ACTIVITY_BASED', description: '基于作业成本编制', icon: 'el-icon-s-cooperation', usageCount: 0 },
        { id: 6, name: '资本预算', value: 'CAPITAL', description: '长期投资预算管理', icon: 'el-icon-money', usageCount: 0 },
        { id: 7, name: '现金流预算', value: 'CASH_FLOW', description: '现金流量预算管理', icon: 'el-icon-coin', usageCount: 0 },
        { id: 8, name: '自定义模型', value: 'CUSTOM', description: '用户自定义预算模型', icon: 'el-icon-setting', usageCount: 0 }
      ],
      selectedModelType: null,
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      
      // 表单数据
      modelForm: {
        modelName: '',
        modelCode: '',
        modelType: '',
        modelStatus: 'DRAFT',
        applicableScope: '',
        budgetCycle: '',
        modelDescription: '',
        configParameters: '',
        calculationRules: ''
      },
      
      // 表单验证规则
      modelRules: {
        modelName: [
          { required: true, message: '请输入模型名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        modelCode: [
          { required: true, message: '请输入模型编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' }
        ],
        modelType: [
          { required: true, message: '请选择模型类型', trigger: 'change' }
        ],
        modelStatus: [
          { required: true, message: '请选择模型状态', trigger: 'change' }
        ]
      },
      
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentModel: null,
      modelVersions: [],

      // 帮助弹窗
      helpDialogVisible: false,

      // 版本管理弹窗
      versionDialogVisible: false,
      versionDialogModelId: '',
      versionDialogModelName: '',
      versionList: [],
      versionLoading: false,

      // 版本详情弹窗
      versionDetailVisible: false,
      currentVersion: null,
      versionDetailLoading: false
    }
  },

  async created() {
    await Promise.all([
      this.getList(),
      this.getModelStats(),
      this.getModelTypeStats()
    ])
  },

  mounted() {
    // 页面挂载完成
  },

  methods: {
    // 构建查询参数（处理日期格式化和空值过滤）
    buildQueryParams() {
      const params = {}
      if (this.queryForm.modelName) params.modelName = this.queryForm.modelName
      if (this.queryForm.modelType) params.modelType = this.queryForm.modelType
      if (this.queryForm.modelStatus) params.modelStatus = this.queryForm.modelStatus
      if (this.queryForm.creator) params.creator = this.queryForm.creator
      // 处理日期范围
      if (this.queryForm.createTime && this.queryForm.createTime.length === 2) {
        const startDate = this.queryForm.createTime[0]
        const endDate = this.queryForm.createTime[1]
        if (startDate) params.createTimeStart = this.formatDate(startDate) + ' 00:00:00'
        if (endDate) params.createTimeEnd = this.formatDate(endDate) + ' 23:59:59'
      }
      return params
    },

    // 日期格式化
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },

    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = this.buildQueryParams()
        const response = await getBudgetModelPage(
          this.queryParams.pageNum,
          this.queryParams.pageSize,
          params
        )
        if (response && response.code === 1 && response.data) {
          this.modelList = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        } else {
          this.$message.warning(response?.msg || '获取数据失败')
        }
      } catch (error) {
        this.$message.error('获取模型列表失败')
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getModelStats() {
      try {
        const response = await getBudgetModelStats()
        if (response && response.code === 1 && response.data) {
          this.modelStats = {
            totalModels: response.data.totalCount || 0,
            activeModels: response.data.activeCount || 0,
            modelTypes: 8,
            totalVersions: response.data.totalCount || 0
          }
        }
      } catch (error) {
        // 统计数据加载失败不影响主流程
      }
    },

    // 获取模型类型统计（各类型数量）
    async getModelTypeStats() {
      try {
        const response = await getBudgetModelTypeStats()
        if (response && response.code === 1 && response.data) {
          const typeData = response.data
          // 更新模型类型卡片的数量
          this.modelTypes.forEach(item => {
            if (typeData[item.value] !== undefined) {
              item.usageCount = typeData[item.value]
            }
          })
          // 同时更新统计卡片
          if (typeData.totalModels !== undefined) this.modelStats.totalModels = typeData.totalModels
          if (typeData.activeModels !== undefined) this.modelStats.activeModels = typeData.activeModels
          if (typeData.totalVersions !== undefined) this.modelStats.totalVersions = typeData.totalVersions
        }
      } catch (error) {
        // 类型统计加载失败不影响主流程
      }
    },

    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    // 重置查询
    handleResetQuery() {
      this.queryForm = {
        modelName: '',
        modelType: '',
        modelStatus: '',
        createTime: [],
        creator: ''
      }
      this.selectedModelType = null
      this.handleQuery()
    },
    
    // 新增
    handleAdd() {
      this.dialogTitle = '新建模型'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑模型'
      this.dialogVisible = true
      this.modelForm = { ...row }
    },
    
    // 查看详情
    async handleView(row) {
      this.currentModel = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await this.getModelVersions(row.modelId)
    },

    // 获取模型版本
    async getModelVersions(modelId) {
      try {
        const response = await getBudgetModelVersions(modelId)
        if (response && response.code === 1 && response.data) {
          this.modelVersions = response.data
        } else {
          this.modelVersions = []
        }
      } catch (error) {
        this.modelVersions = []
      }
    },

    // 提交表单
    async handleSubmitForm() {
      this.$refs.modelForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            if (this.modelForm.modelId) {
              const response = await updateBudgetModel(this.modelForm.modelId, this.modelForm)
              if (response && response.code === 1) {
                this.$message.success('更新成功')
                this.dialogVisible = false
                this.getList()
              } else {
                this.$message.error(response?.msg || '更新失败')
              }
            } else {
              const response = await createBudgetModel(this.modelForm)
              if (response && response.code === 1) {
                this.$message.success('创建成功')
                this.dialogVisible = false
                this.getList()
              } else {
                this.$message.error(response?.msg || '创建失败')
              }
            }
          } catch (error) {
            this.$message.error('操作失败')
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    
    // 重置表单
    resetForm() {
      this.modelForm = {
        modelName: '',
        modelCode: '',
        modelType: '',
        modelStatus: 'DRAFT',
        applicableScope: '',
        budgetCycle: '',
        modelDescription: '',
        configParameters: '',
        calculationRules: ''
      }
      this.$nextTick(() => {
        this.$refs.modelForm && this.$refs.modelForm.clearValidate()
      })
    },
    
    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
      this.getModelStats()
      this.getModelTypeStats()
      this.$message.success('数据已刷新')
    },

    // 导出
    handleExport() {
      const exportData = this.modelList
      if (!exportData || exportData.length === 0) {
        this.$message.warning('暂无数据可导出')
        return
      }
      this.loading = true
      import('@/utils/excel').then((excel) => {
        const tHeader = ['模型名称', '模型编码', '模型类型', '版本', '状态', '适用范围', '创建人', '创建时间', '模型描述']
        const filterVal = ['modelName', 'modelCode', 'modelType', 'version', 'modelStatus', 'applicableScope', 'creatorName', 'createTime', 'modelDescription']
        const data = exportData.map((row) =>
          filterVal.map((key) => {
            if (key === 'modelType') return this.getModelTypeText(row[key])
            if (key === 'modelStatus') return this.getStatusText(row[key])
            return row[key] || ''
          })
        )
        excel.export_json_to_excel({ header: tHeader, data, filename: '预算模型数据', autoWidth: true, bookType: 'xlsx' })
        this.loading = false
        this.$message.success('导出成功')
      }).catch(() => {
        this.loading = false
        this.$message.error('导出失败')
      })
    },

    // 导入
    handleImport() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls'
      input.onchange = async (e) => {
        const file = e.target.files[0]
        if (!file) return
        const formData = new FormData()
        formData.append('file', file)
        this.loading = true
        try {
          const response = await importBudgetModel(formData)
          this.loading = false
          if (response && response.code === 1) {
            this.$message.success(response.msg || '导入成功')
            this.getList()
            this.getModelStats()
            this.getModelTypeStats()
          } else {
            this.$message.error(response?.msg || '导入失败')
          }
        } catch (error) {
          this.loading = false
          this.$message.error('导入失败')
        }
      }
      input.click()
    },

    // 批量复制
    handleBatchCopy() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要复制的模型')
        return
      }
      this.$confirm(`确定复制选中的 ${this.selectedRows.length} 个模型吗？`, '批量复制确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.selectedRows.map(row => row.modelId)
          const response = await batchCopyBudgetModels(ids)
          if (response && response.code === 1) {
            this.$message.success(response.msg || '复制成功')
          } else {
            this.$message.success('复制成功')
          }
          this.getList()
          this.getModelTypeStats()
        } catch (error) {
          this.$message.error('复制失败')
        }
      })
    },

    // 批量删除
    handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的模型')
        return
      }
      this.$confirm(`确定删除选中的 ${this.selectedRows.length} 个模型吗？删除后不可恢复。`, '批量删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.selectedRows.map(row => row.modelId)
          const response = await batchDeleteBudgetModels(ids)
          if (response && response.code === 1) {
            this.$message.success(response.msg || '删除成功')
          } else {
            this.$message.success('删除成功')
          }
          this.getList()
          this.getModelStats()
          this.getModelTypeStats()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 刷新模型类型
    refreshModelTypes() {
      this.getModelTypeStats()
      this.$message.success('模型类型已刷新')
    },

    // 选择模型类型
    handleSelectModelType(modelType) {
      this.selectedModelType = modelType.id
      this.queryForm.modelType = modelType.value
      this.handleQuery()
    },
    
    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 行点击（不再弹出详情抽屉）
    handleRowClick(row) {
      // 不做任何操作，避免误触弹出详情
    },
    
    // 分页大小变化
    handleSizeChange(val) {
      if (typeof val === 'string') {
        this.tableSize = val
      } else {
        this.queryParams.pageSize = val
        this.getList()
      }
    },
    
    // 当前页变化
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    
    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopyModel(row)
          break
        case 'version':
          this.handleVersionManagement(row)
          break
        case 'apply':
          this.handleApplyModel(row)
          break
        case 'export':
          this.handleExportModel(row)
          break
        case 'delete':
          this.handleDeleteModel(row)
          break
      }
    },

    // 复制模型
    async handleCopyModel(row) {
      try {
        const response = await copyBudgetModel(row.modelId)
        if (response && response.code === 1) {
          this.$message.success('复制成功')
        } else {
          this.$message.success('复制成功')
        }
        this.getList()
        this.getModelTypeStats()
      } catch (error) {
        this.$message.error('复制失败')
      }
    },

    // 版本管理
    async handleVersionManagement(row) {
      this.versionDialogModelId = row.modelId
      this.versionDialogModelName = row.modelName
      this.versionDialogVisible = true
      this.versionLoading = true
      try {
        const response = await getBudgetModelVersions(row.modelId)
        if (response && response.code === 1 && response.data) {
          this.versionList = response.data
        } else {
          this.versionList = []
        }
      } catch (error) {
        this.versionList = []
        this.$message.error('获取版本列表失败')
      } finally {
        this.versionLoading = false
      }
    },

    // 应用模型
    async handleApplyModel(row) {
      this.$confirm('确定应用该模型吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await applyBudgetModel(row.modelId)
          this.$message.success('应用成功')
          this.getList()
        } catch (error) {
          this.$message.error('应用失败：' + error.message)
        }
      })
    },

    // 导出单个模型
    handleExportModel(row) {
      this.loading = true
      import('@/utils/excel').then((excel) => {
        const tHeader = ['模型名称', '模型编码', '模型类型', '版本', '状态', '适用范围', '创建人', '创建时间', '模型描述']
        const filterVal = ['modelName', 'modelCode', 'modelType', 'version', 'modelStatus', 'applicableScope', 'creatorName', 'createTime', 'modelDescription']
        const data = [filterVal.map((key) => {
          if (key === 'modelType') return this.getModelTypeText(row[key])
          if (key === 'modelStatus') return this.getStatusText(row[key])
          return row[key] || ''
        })]
        excel.export_json_to_excel({ header: tHeader, data, filename: row.modelName || '预算模型', autoWidth: true, bookType: 'xlsx' })
        this.loading = false
        this.$message.success('导出成功')
      }).catch(() => {
        this.loading = false
        this.$message.error('导出失败')
      })
    },

    // 删除模型
    handleDeleteModel(row) {
      this.$confirm(`确定删除模型「${row.modelName}」吗？删除后不可恢复。`, '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteBudgetModel(row.modelId)
          this.$message.success('删除成功')
          this.getList()
          this.getModelStats()
          this.getModelTypeStats()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },
    
    // 查看版本详情
    async handleViewVersion(version) {
      this.versionDetailVisible = true
      this.versionDetailLoading = true
      try {
        const response = await getBudgetModelVersionDetail(version.versionId)
        if (response && response.code === 1 && response.data) {
          this.currentVersion = response.data
        } else {
          this.currentVersion = version
        }
      } catch (error) {
        this.currentVersion = version
      } finally {
        this.versionDetailLoading = false
      }
    },

    // 恢复版本
    async handleRestoreVersion(version) {
      if (!version || !version.versionId) return
      this.$confirm(`确定恢复到版本「${version.versionNo}」吗？当前模型配置将被覆盖。`, '版本恢复确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await restoreBudgetModelVersion(version.versionId)
          if (response && response.code === 1) {
            this.$message.success('版本恢复成功')
            // 刷新版本列表
            if (this.versionDialogVisible && this.versionDialogModelId) {
              const vResponse = await getBudgetModelVersions(this.versionDialogModelId)
              if (vResponse && vResponse.code === 1) this.versionList = vResponse.data || []
            }
            // 刷新详情抽屉中的版本历史
            if (this.currentModel && this.currentModel.modelId) {
              await this.getModelVersions(this.currentModel.modelId)
            }
            // 刷新主列表
            this.getList()
          } else {
            this.$message.error(response?.msg || '恢复失败')
          }
        } catch (error) {
          this.$message.error('恢复失败')
        }
      })
    },
    
    // 获取模型类型颜色
    getModelTypeColor(type) {
      const colorMap = {
        'INCREMENTAL': 'primary',
        'ZERO_BASED': 'success',
        'ROLLING': 'warning',
        'FLEXIBLE': 'info',
        'ACTIVITY_BASED': 'danger',
        'CAPITAL': 'primary',
        'CASH_FLOW': 'success',
        'CUSTOM': 'warning'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取模型类型文本
    getModelTypeText(type) {
      const textMap = {
        'INCREMENTAL': '增量预算',
        'ZERO_BASED': '零基预算',
        'ROLLING': '滚动预算',
        'FLEXIBLE': '弹性预算',
        'ACTIVITY_BASED': '作业预算',
        'CAPITAL': '资本预算',
        'CASH_FLOW': '现金流预算',
        'CUSTOM': '自定义模型'
      }
      return textMap[type] || type
    },
    
    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'DRAFT': 'info',
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'ARCHIVED': 'danger'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'DRAFT': '草稿',
        'ACTIVE': '启用',
        'INACTIVE': '停用',
        'ARCHIVED': '归档'
      }
      return textMap[status] || status
    },
    
    // 格式化JSON
    formatJSON(jsonStr) {
      try {
        return JSON.stringify(JSON.parse(jsonStr), null, 2)
      } catch (e) {
        return jsonStr
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-model {
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
  .filter-card,
  .model-types-card,
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

      &.types-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.versions-card {
        background: linear-gradient(135deg, #909399, #B3B6BC);
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
    }
  }

  .filter-card {
    .filter-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .filter-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
      }
    }
  }

  .model-types-card {
    .model-type-item {
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
      }

      &.selected {
        border-color: #409EFF;
        box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
      }

      .model-type-icon {
        font-size: 32px;
        color: #409EFF;
        margin-bottom: 12px;
      }

      .model-type-title {
        font-size: 14px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .model-type-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
        line-height: 1.4;
      }

      .model-type-stats {
        .usage-count {
          font-size: 11px;
          color: #909399;
          background: #F5F7FA;
          padding: 2px 8px;
          border-radius: 10px;
        }
      }
    }
  }

  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        display: flex;
        align-items: center;
        gap: 10px;
      }
    }

    .usage-count {
      font-weight: 600;
      color: #409EFF;
    }

    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }
  }

  .detail-content {
    padding: 20px;

    .config-content {
      background: #F5F7FA;
      padding: 16px;
      border-radius: 4px;
      font-family: 'Courier New', monospace;
      font-size: 12px;
      line-height: 1.6;
      white-space: pre-wrap;
      word-break: break-all;
    }

    .rules-content {
      background: #F5F7FA;
      padding: 16px;
      border-radius: 4px;
      line-height: 1.6;
      white-space: pre-wrap;
    }
  }

  .text-right {
    text-align: right;
  }

  .help-content {
    h4 {
      margin-bottom: 12px;
      color: #303133;
    }
    p {
      color: #606266;
      line-height: 1.8;
      margin-bottom: 10px;
    }
  }
}
</style>
