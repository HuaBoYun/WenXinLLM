<template>
  <div class="budget-workflow">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算工作流管理</h2>
      <p>管理预算工作流程，支持流程设计、节点配置、路由规则和流程监控</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateWorkflow">创建流程</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportWorkflow">导入流程</el-button>
            <el-button type="warning" icon="el-icon-check" @click="handleBatchDeploy">批量部署</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportWorkflow">导出流程</el-button>
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

    <!-- 工作流统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ workflowStats.totalWorkflows }}</div>
            <div class="stat-label">工作流总数</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-operation"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ workflowStats.activeWorkflows }}</div>
            <div class="stat-label">运行中</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="workflowStats.activeRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#67C23A"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-video-play"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card draft-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ workflowStats.draftWorkflows }}</div>
            <div class="stat-label">草稿状态</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="workflowStats.draftRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#E6A23C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-edit-outline"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card instance-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ workflowStats.runningInstances }}</div>
            <div class="stat-label">运行实例</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="workflowStats.instanceRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#409EFF"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-cpu"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 工作流分类和查询 -->
    <el-card class="search-card" shadow="never">
      <el-row :gutter="20">
        <el-col :span="4">
          <div class="workflow-categories">
            <h4>流程分类</h4>
            <el-tree
              :data="categoryTree"
              :props="{ children: 'children', label: 'name' }"
              node-key="id"
              :current-node-key="currentCategoryId"
              @node-click="handleCategoryClick"
              :expand-on-click-node="false"
            >
              <span class="tree-node" slot-scope="{ node, data }">
                <span class="node-label">{{ node.label }}</span>
                <span class="node-count">({{ data.workflowCount || 0 }})</span>
              </span>
            </el-tree>
          </div>
        </el-col>
        <el-col :span="20">
          <el-form :model="queryForm" :inline="true" size="small">
            <el-form-item label="流程名称">
              <el-input
                v-model="queryForm.workflowName"
                placeholder="请输入流程名称"
                clearable
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item label="流程类型">
              <el-select
                v-model="queryForm.workflowType"
                placeholder="请选择流程类型"
                clearable
                style="width: 150px"
              >
                <el-option
                  v-for="item in workflowTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="流程状态">
              <el-select
                v-model="queryForm.workflowStatus"
                placeholder="请选择流程状态"
                clearable
                style="width: 150px"
              >
                <el-option
                  v-for="item in workflowStatusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
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
              <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>

    <!-- 工作流列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算工作流列表</span>
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
        :data="workflowList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column prop="workflowCode" label="流程编码" width="150" show-overflow-tooltip />
        <el-table-column prop="workflowName" label="流程名称" width="200" show-overflow-tooltip />
        
        <el-table-column prop="workflowType" label="流程类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getWorkflowTypeColor(scope.row.workflowType)">
              {{ getWorkflowTypeText(scope.row.workflowType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="version" label="版本" width="80" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" type="info">v{{ scope.row.version }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="nodeCount" label="节点数" width="80" align="center" sortable="custom">
          <template slot-scope="scope">
            <span class="number-text">{{ scope.row.nodeCount || 0 }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="instanceCount" label="实例数" width="80" align="center" sortable="custom">
          <template slot-scope="scope">
            <span class="number-text">{{ scope.row.instanceCount || 0 }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="avgDuration" label="平均耗时" width="100" align="center">
          <template slot-scope="scope">
            <span class="duration-text">{{ formatDuration(scope.row.avgDuration) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="successRate" label="成功率" width="100" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.successRate || 0"
              :stroke-width="6"
              :text-inside="true"
              :color="getSuccessRateColor(scope.row.successRate)"
            />
          </template>
        </el-table-column>
        
        <el-table-column prop="workflowStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getWorkflowStatusType(scope.row.workflowStatus)" size="mini">
              {{ getWorkflowStatusText(scope.row.workflowStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="creator" label="创建人" width="100" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        
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
              class="danger-text"
              @click.stop="handleDelete(scope.row)"
            >删除</el-button>
            <el-dropdown trigger="click" @command="(cmd) => handleCommand(cmd, scope.row)" @click.native.stop>
              <el-button type="text" size="mini" icon="el-icon-more">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="detail" icon="el-icon-view">查看详情</el-dropdown-item>
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制流程</el-dropdown-item>
                <el-dropdown-item command="deploy" icon="el-icon-upload">部署流程</el-dropdown-item>
                <el-dropdown-item command="version" icon="el-icon-time">版本管理</el-dropdown-item>
                <el-dropdown-item command="instance" icon="el-icon-cpu">流程实例</el-dropdown-item>
                <el-dropdown-item command="monitor" icon="el-icon-data-analysis">流程监控</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出流程</el-dropdown-item>
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

    <!-- 新增/编辑工作流对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1000px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="workflowForm"
        :model="workflowForm"
        :rules="workflowRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="流程名称" prop="workflowName">
              <el-input
                v-model="workflowForm.workflowName"
                placeholder="请输入流程名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="流程编码" prop="workflowCode">
              <el-input
                v-model="workflowForm.workflowCode"
                placeholder="请输入流程编码"
                :disabled="!!workflowForm.workflowId"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="流程类型" prop="workflowType">
              <el-select
                v-model="workflowForm.workflowType"
                placeholder="请选择流程类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in workflowTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属分类" prop="categoryId">
              <el-cascader
                v-model="workflowForm.categoryId"
                :options="categoryOptions"
                :props="{ checkStrictly: true, value: 'id', label: 'name' }"
                placeholder="请选择所属分类"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="流程描述" prop="workflowDescription">
          <el-input
            v-model="workflowForm.workflowDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入流程描述"
          />
        </el-form-item>
        
        <!-- 流程节点配置 -->
        <el-form-item label="流程节点" prop="workflowNodes">
          <div class="workflow-nodes">
            <div class="nodes-header">
              <el-button type="primary" size="mini" @click="handleAddNode">添加节点</el-button>
              <el-button type="success" size="mini" @click="handleImportNodes">导入节点</el-button>
              <el-button type="warning" size="mini" @click="handleValidateNodes">验证节点</el-button>
            </div>
            
            <el-table
              :data="workflowForm.workflowNodes"
              border
              size="mini"
              max-height="300"
            >
              <el-table-column type="index" label="序号" width="60" align="center" />
              
              <el-table-column label="节点名称" width="150">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.nodeName"
                    placeholder="节点名称"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="节点类型" width="120">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.nodeType"
                    placeholder="节点类型"
                    size="mini"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="type in nodeTypeOptions"
                      :key="type.value"
                      :label="type.label"
                      :value="type.value"
                    />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="处理人" width="150">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.assignee"
                    placeholder="处理人"
                    size="mini"
                    style="width: 100%"
                    filterable
                  >
                    <el-option
                      v-for="user in userOptions"
                      :key="user.value"
                      :label="user.label"
                      :value="user.value"
                    />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="超时时间" width="100">
                <template slot-scope="scope">
                  <el-input-number
                    v-model="scope.row.timeoutHours"
                    :min="1"
                    :max="720"
                    size="mini"
                    controls-position="right"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="必填" width="60" align="center">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.required"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    class="danger-text"
                    @click="handleRemoveNode(scope.$index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form-item>
        
        <!-- 流程配置 -->
        <el-form-item label="流程配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="workflowForm.allowParallel">允许并行</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="workflowForm.allowSkip">允许跳过</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="workflowForm.autoStart">自动启动</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
        
        <!-- 通知配置 -->
        <el-form-item label="通知配置">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-checkbox v-model="workflowForm.notifyOnStart">启动时通知</el-checkbox>
            </el-col>
            <el-col :span="12">
              <el-checkbox v-model="workflowForm.notifyOnComplete">完成时通知</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleSaveWorkflow">保存草稿</el-button>
        <el-button type="primary" @click="handlePublishWorkflow">发布流程</el-button>
      </div>
    </el-dialog>

    <!-- 流程设计器对话框 -->
    <el-dialog
      title="流程设计器"
      :visible.sync="designerDialogVisible"
      width="90%"
      :close-on-click-modal="false"
      fullscreen
    >
      <div class="workflow-designer">
        <div class="designer-toolbar">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus">添加节点</el-button>
            <el-button type="success" icon="el-icon-connection">连接线</el-button>
            <el-button type="warning" icon="el-icon-check">验证</el-button>
            <el-button type="info" icon="el-icon-view">预览</el-button>
          </el-button-group>
        </div>
        
        <div class="designer-canvas">
          <div class="canvas-placeholder">
            <i class="el-icon-s-operation" style="font-size: 64px; color: #ddd;"></i>
            <p>流程设计器开发中...</p>
            <p>将支持拖拽式流程设计、节点配置、连接线绘制等功能</p>
          </div>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="designerDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleSaveDesign">保存设计</el-button>
      </div>
    </el-dialog>

    <!-- 工作流详情对话框（更多） -->
    <el-dialog :title="'工作流详情 - ' + (workflowDetail.workflowName || '')" :visible.sync="detailDialogVisible" width="850px" :close-on-click-modal="false" top="5vh">
      <!-- 基本信息 -->
      <h4 style="margin: 0 0 12px 0; color: #303133;"><i class="el-icon-info" /> 基本信息</h4>
      <el-descriptions :column="3" border size="small">
        <el-descriptions-item label="流程名称">{{ workflowDetail.workflowName }}</el-descriptions-item>
        <el-descriptions-item label="流程编码">{{ workflowDetail.workflowCode }}</el-descriptions-item>
        <el-descriptions-item label="流程类型">
          <el-tag size="mini" :type="getWorkflowTypeColor(workflowDetail.workflowType)">
            {{ getWorkflowTypeText(workflowDetail.workflowType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag size="mini" :type="getWorkflowStatusType(workflowDetail.workflowStatus)">
            {{ getWorkflowStatusText(workflowDetail.workflowStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="版本">v{{ workflowDetail.version || 1 }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ workflowDetail.categoryId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ workflowDetail.creator || workflowDetail.createBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ workflowDetail.createTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ workflowDetail.updateTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="3">{{ workflowDetail.workflowDescription || '-' }}</el-descriptions-item>
      </el-descriptions>

      <!-- 运行统计 -->
      <h4 style="margin: 20px 0 12px 0; color: #303133;"><i class="el-icon-data-analysis" /> 运行统计</h4>
      <el-row :gutter="16">
        <el-col :span="6">
          <div class="detail-stat-item">
            <div class="detail-stat-value">{{ workflowDetail.nodeCount || 0 }}</div>
            <div class="detail-stat-label">节点数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="detail-stat-item">
            <div class="detail-stat-value">{{ workflowDetail.instanceCount || 0 }}</div>
            <div class="detail-stat-label">实例数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="detail-stat-item">
            <div class="detail-stat-value">{{ workflowDetail.avgDuration || 0 }}</div>
            <div class="detail-stat-label">平均耗时(分)</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="detail-stat-item">
            <div class="detail-stat-value">{{ workflowDetail.successRate || 0 }}%</div>
            <div class="detail-stat-label">成功率</div>
          </div>
        </el-col>
      </el-row>

      <!-- 配置参数 -->
      <h4 style="margin: 20px 0 12px 0; color: #303133;"><i class="el-icon-setting" /> 配置参数</h4>
      <el-descriptions :column="4" border size="small">
        <el-descriptions-item label="允许并行">
          <el-tag size="mini" :type="workflowDetail.allowParallel ? 'success' : 'info'">{{ workflowDetail.allowParallel ? '是' : '否' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="允许跳过">
          <el-tag size="mini" :type="workflowDetail.allowSkip ? 'success' : 'info'">{{ workflowDetail.allowSkip ? '是' : '否' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="自动启动">
          <el-tag size="mini" :type="workflowDetail.autoStart ? 'success' : 'info'">{{ workflowDetail.autoStart ? '是' : '否' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="完成通知">
          <el-tag size="mini" :type="workflowDetail.notifyOnComplete ? 'success' : 'info'">{{ workflowDetail.notifyOnComplete ? '是' : '否' }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>

      <!-- 流程节点 -->
      <h4 style="margin: 20px 0 12px 0; color: #303133;"><i class="el-icon-s-operation" /> 流程节点</h4>
      <el-table :data="workflowDetail.workflowNodes || []" border size="mini" style="width: 100%" empty-text="暂无节点配置">
        <el-table-column type="index" label="#" width="50" align="center" />
        <el-table-column prop="nodeName" label="节点名称" />
        <el-table-column prop="nodeType" label="节点类型" width="120">
          <template slot-scope="scope">
            <el-tag size="mini">{{ getNodeTypeText(scope.row.nodeType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assignee" label="处理人" width="120" />
        <el-table-column prop="timeoutHours" label="超时(小时)" width="100" align="center" />
        <el-table-column prop="required" label="必填" width="80" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="scope.row.required ? 'danger' : 'info'">{{ scope.row.required ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer">
        <el-button icon="el-icon-document-copy" @click="handleDetailCopy">复制</el-button>
        <el-button icon="el-icon-download" @click="handleDetailExport">导出</el-button>
        <el-button icon="el-icon-upload" type="success" @click="handleDetailDeploy">部署</el-button>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 工作流设置对话框 -->
    <el-dialog title="工作流设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="140px" size="small">
        <el-form-item label="启用工作流引擎">
          <el-switch v-model="settingsForm.enableEngine" />
        </el-form-item>
        <el-form-item label="允许并行审批">
          <el-switch v-model="settingsForm.allowParallel" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 版本管理对话框 -->
    <el-dialog :title="'版本管理 - ' + (versionRow.workflowName || '')" :visible.sync="versionDialogVisible" width="700px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="流程名称">{{ versionRow.workflowName }}</el-descriptions-item>
        <el-descriptions-item label="当前版本">{{ versionRow.version }}</el-descriptions-item>
      </el-descriptions>
      <el-alert title="版本管理功能用于管理工作流的历史版本" type="info" :closable="false" show-icon style="margin-top: 16px" />
      <div slot="footer">
        <el-button @click="versionDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 流程实例对话框 -->
    <el-dialog :title="'流程实例 - ' + (instanceRow.workflowName || '')" :visible.sync="instanceDialogVisible" width="700px" :close-on-click-modal="false">
      <el-alert title="流程实例列表展示该工作流的所有运行实例" type="info" :closable="false" show-icon style="margin-bottom: 16px" />
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="流程名称">{{ instanceRow.workflowName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag size="mini" :type="getWorkflowStatusType(instanceRow.workflowStatus)">{{ getWorkflowStatusText(instanceRow.workflowStatus) }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="instanceDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 流程监控对话框 -->
    <el-dialog :title="'流程监控 - ' + (monitorRow.workflowName || '')" :visible.sync="monitorDialogVisible" width="700px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="流程名称">{{ monitorRow.workflowName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag size="mini" :type="getWorkflowStatusType(monitorRow.workflowStatus)">{{ getWorkflowStatusText(monitorRow.workflowStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="流程类型">
          <el-tag size="mini" :type="getWorkflowTypeColor(monitorRow.workflowType)">{{ getWorkflowTypeText(monitorRow.workflowType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="版本">{{ monitorRow.version }}</el-descriptions-item>
      </el-descriptions>
      <el-alert title="流程监控功能用于实时监控工作流运行状态" type="info" :closable="false" show-icon style="margin-top: 16px" />
      <div slot="footer">
        <el-button @click="monitorDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetWorkflowApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetWorkflow',
  data() {
    return {
      // 查询参数
      queryForm: {
        workflowName: '',
        workflowType: '',
        workflowStatus: '',
        creator: '',
        categoryId: null
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      workflowList: [],
      total: 0,
      selectedRows: [],
      
      // 统计数据
      workflowStats: {
        totalWorkflows: 0,
        activeWorkflows: 0,
        draftWorkflows: 0,
        runningInstances: 0,
        activeRate: 0,
        draftRate: 0,
        instanceRate: 0
      },
      
      // 分类相关
      categoryTree: [],
      currentCategoryId: null,
      categoryOptions: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      designerDialogVisible: false,
      workflowForm: {
        workflowId: null,
        workflowName: '',
        workflowCode: '',
        workflowType: '',
        categoryId: null,
        workflowDescription: '',
        workflowNodes: [],
        allowParallel: false,
        allowSkip: false,
        autoStart: false,
        notifyOnStart: false,
        notifyOnComplete: true
      },
      workflowRules: {
        workflowName: [
          { required: true, message: '请输入流程名称', trigger: 'blur' }
        ],
        workflowCode: [
          { required: true, message: '请输入流程编码', trigger: 'blur' },
          { pattern: /^[A-Z_][A-Z0-9_]*$/, message: '流程编码只能包含大写字母、数字和下划线，且以字母或下划线开头', trigger: 'blur' }
        ],
        workflowType: [
          { required: true, message: '请选择流程类型', trigger: 'change' }
        ]
      },
      
      // 选项数据
      workflowTypeOptions: [
        { value: 'APPROVAL', label: '审批流程' },
        { value: 'REVIEW', label: '评审流程' },
        { value: 'NOTIFICATION', label: '通知流程' },
        { value: 'CALCULATION', label: '计算流程' },
        { value: 'INTEGRATION', label: '集成流程' }
      ],
      workflowStatusOptions: [
        { value: 'DRAFT', label: '草稿' },
        { value: 'PUBLISHED', label: '已发布' },
        { value: 'ACTIVE', label: '运行中' },
        { value: 'SUSPENDED', label: '已暂停' },
        { value: 'ARCHIVED', label: '已归档' }
      ],
      nodeTypeOptions: [
        { value: 'START', label: '开始节点' },
        { value: 'USER_TASK', label: '用户任务' },
        { value: 'SERVICE_TASK', label: '服务任务' },
        { value: 'GATEWAY', label: '网关节点' },
        { value: 'END', label: '结束节点' }
      ],
      userOptions: [
        { value: 'admin', label: '管理员' },
        { value: 'manager', label: '经理' },
        { value: 'finance', label: '财务' }
      ],

      detailDialogVisible: false,
      workflowDetail: {},

      settingsDialogVisible: false,
      settingsForm: { enableEngine: true, allowParallel: false },

      versionDialogVisible: false,
      versionRow: {},

      instanceDialogVisible: false,
      instanceRow: {},

      monitorDialogVisible: false,
      monitorRow: {}
    }
  },
  
  created() {
    this.getList()
    this.loadCategoryTree()
    this.loadCategoryOptions()
    this.loadStats()
  },
  
  methods: {
    // 加载统计数据
    async loadStats() {
      try {
        const response = await budgetWorkflowApi.getStats()
        if (response.code === 1) this.workflowStats = response.data
      } catch (error) {
        console.error('加载统计数据失败：', error)
      }
    },
    // 将后端实体数据转换为前端表格展示字段
    mapWorkflowRecord(record) {
      let nodes = []
      try {
        if (record.nodeConfig) nodes = JSON.parse(record.nodeConfig)
      } catch (e) { /* ignore */ }
      let definition = {}
      try {
        if (record.workflowDefinition) definition = JSON.parse(record.workflowDefinition)
      } catch (e) { /* ignore */ }
      return {
        ...record,
        // 表格展示字段
        version: record.version || 1,
        nodeCount: Array.isArray(nodes) ? nodes.length : 0,
        instanceCount: record.instanceCount || 0,
        avgDuration: record.avgDuration || 0,
        successRate: record.successRate || 0,
        workflowStatus: record.isEnabled === 1 ? 'ACTIVE' : 'DRAFT',
        creator: record.createBy || '',
        // 编辑回显用
        workflowNodes: Array.isArray(nodes) ? nodes : [],
        categoryId: definition.categoryId || null,
        allowParallel: !!definition.allowParallel,
        allowSkip: !!definition.allowSkip,
        autoStart: !!definition.autoStart,
        notifyOnStart: !!definition.notifyOnStart,
        notifyOnComplete: definition.notifyOnComplete !== false
      }
    },

    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.queryParams,
          categoryId: this.currentCategoryId
        }
        const response = await budgetWorkflowApi.getPage(params)
        const records = response.data.records || []
        this.workflowList = records.map(r => this.mapWorkflowRecord(r))
        this.total = response.data.total
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载分类树
    async loadCategoryTree() {
      try {
        const response = await budgetWorkflowApi.getCategoryTree()
        const rawTree = response.data || []
        // 后端已返回 workflowCount，直接使用；name 从英文枚举转中文
        const typeNodes = rawTree.map(node => ({
          ...node,
          name: this.getWorkflowTypeText(node.name) || node.name,
          workflowCount: node.workflowCount || 0,
          children: (node.children || []).map(child => ({
            ...child,
            name: this.getWorkflowTypeText(child.name) || child.name,
            workflowCount: child.workflowCount || 0
          }))
        }))
        // 计算全部数量
        const totalCount = typeNodes.reduce((sum, n) => sum + (n.workflowCount || 0), 0)
        // 在顶部插入"全部"节点
        this.categoryTree = [
          { id: null, name: '全部', workflowCount: totalCount, children: [] },
          ...typeNodes
        ]
      } catch (error) {
        console.error('加载分类树失败：', error)
      }
    },

    // 加载分类选项
    async loadCategoryOptions() {
      try {
        const response = await budgetWorkflowApi.getCategories()
        this.categoryOptions = response.data
      } catch (error) {
        console.error('加载分类选项失败：', error)
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
        workflowName: '',
        workflowType: '',
        workflowStatus: '',
        creator: '',
        categoryId: null
      }
      this.currentCategoryId = null
      this.handleQuery()
    },
    
    // 分类点击（id 为 null 表示"全部"）
    handleCategoryClick(data) {
      this.currentCategoryId = data.id || null
      this.queryParams.pageNum = 1
      this.getList()
      // 切换分类后刷新分类树计数
      this.loadCategoryTree()
    },
    
    // 创建工作流
    handleCreateWorkflow() {
      this.dialogTitle = '创建预算工作流'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑工作流
    handleEdit(row) {
      this.dialogTitle = '编辑预算工作流'
      this.dialogVisible = true
      this.workflowForm = { ...row }
    },
    
    // 查看工作流（详情弹窗）
    handleView(row) {
      this.workflowDetail = { ...row }
      this.detailDialogVisible = true
    },

    // 更多操作已通过 handleCommand 分发

    // 删除工作流
    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除工作流「${row.workflowName}」吗？删除后不可恢复。`, '删除确认', {
          type: 'warning',
          confirmButtonText: '确认删除',
          cancelButtonText: '取消'
        })
        await budgetWorkflowApi.delete(row.workflowId)
        this.$message.success('删除成功')
        this.getList()
        this.loadStats()
        this.loadCategoryTree()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 详情页 - 复制
    handleDetailCopy() {
      this.detailDialogVisible = false
      this.handleCopy(this.workflowDetail)
    },

    // 详情页 - 导出
    handleDetailExport() {
      this.handleExportSingle(this.workflowDetail)
    },

    // 详情页 - 部署
    handleDetailDeploy() {
      this.detailDialogVisible = false
      this.handleDeploy(this.workflowDetail)
    },

    // 部署工作流
    async handleDeploy(row) {
      try {
        await this.$confirm('确认部署该工作流吗？', '提示', {
          type: 'warning'
        })
        await budgetWorkflowApi.deploy(row.workflowId)
        this.$message.success('部署成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('部署失败：' + error.message)
        }
      }
    },
    
    // 添加节点
    handleAddNode() {
      this.workflowForm.workflowNodes.push({
        nodeName: '',
        nodeType: 'USER_TASK',
        assignee: '',
        timeoutHours: 24,
        required: true
      })
    },
    
    // 删除节点
    handleRemoveNode(index) {
      this.workflowForm.workflowNodes.splice(index, 1)
    },
    
    // 导入节点
    handleImportNodes() {
      this.$message.info('节点导入功能开发中...')
    },
    
    // 验证节点
    handleValidateNodes() {
      if (this.workflowForm.workflowNodes.length === 0) {
        this.$message.warning('请先添加流程节点')
        return
      }
      
      // 简单验证逻辑
      const invalidNodes = this.workflowForm.workflowNodes.filter(node => 
        !node.nodeName || !node.nodeType
      )
      
      if (invalidNodes.length > 0) {
        this.$message.error('存在未完成配置的节点')
        return
      }
      
      this.$message.success('节点验证通过')
    },
    
    // 构建后端请求数据（将前端表单字段映射到后端实体字段）
    buildRequestData(status) {
      const form = this.workflowForm
      const data = {
        workflowCode: form.workflowCode,
        workflowName: form.workflowName,
        workflowType: form.workflowType,
        workflowDescription: form.workflowDescription,
        // workflowNodes 数组 → nodeConfig JSON字符串
        nodeConfig: form.workflowNodes && form.workflowNodes.length > 0
          ? JSON.stringify(form.workflowNodes)
          : null,
        // categoryId、allowParallel 等配置 → workflowDefinition JSON字符串
        workflowDefinition: JSON.stringify({
          categoryId: form.categoryId,
          allowParallel: form.allowParallel,
          allowSkip: form.allowSkip,
          autoStart: form.autoStart,
          notifyOnStart: form.notifyOnStart,
          notifyOnComplete: form.notifyOnComplete
        }),
        // status → isEnabled（PUBLISHED=1, DRAFT=0）
        isEnabled: status === 'PUBLISHED' ? 1 : 0
      }
      // 编辑时带上 workflowId
      if (form.workflowId) {
        data.workflowId = form.workflowId
      }
      return data
    },

    // 保存工作流
    async handleSaveWorkflow() {
      try {
        await this.$refs.workflowForm.validate()

        const params = this.buildRequestData('DRAFT')

        if (this.workflowForm.workflowId) {
          await budgetWorkflowApi.update(params)
          this.$message.success('保存成功')
        } else {
          await budgetWorkflowApi.create(params)
          this.$message.success('创建成功')
        }

        this.dialogVisible = false
        this.getList()
        this.loadCategoryTree()
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    // 发布工作流
    async handlePublishWorkflow() {
      try {
        await this.$refs.workflowForm.validate()

        if (this.workflowForm.workflowNodes.length === 0) {
          this.$message.warning('请添加至少一个流程节点')
          return
        }

        const params = this.buildRequestData('PUBLISHED')

        if (this.workflowForm.workflowId) {
          await budgetWorkflowApi.update(params)
          this.$message.success('发布成功')
        } else {
          await budgetWorkflowApi.create(params)
          this.$message.success('创建并发布成功')
        }

        this.dialogVisible = false
        this.getList()
        this.loadCategoryTree()
      } catch (error) {
        this.$message.error('发布失败：' + error.message)
      }
    },
    
    // 重置表单
    resetForm() {
      this.workflowForm = {
        workflowId: null,
        workflowName: '',
        workflowCode: '',
        workflowType: '',
        categoryId: null,
        workflowDescription: '',
        workflowNodes: [],
        allowParallel: false,
        allowSkip: false,
        autoStart: false,
        notifyOnStart: false,
        notifyOnComplete: true
      }
      this.$nextTick(() => {
        this.$refs.workflowForm && this.$refs.workflowForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入工作流
    handleImportWorkflow() {
      this.$message.info('工作流导入功能开发中...')
    },
    
    // 批量部署
    async handleBatchDeploy() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要部署的工作流')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.workflowId)
        await budgetWorkflowApi.batchDeploy(ids)
        this.$message.success('批量部署完成')
        this.getList()
      } catch (error) {
        this.$message.error('批量部署失败：' + error.message)
      }
    },
    
    // 导出工作流
    async handleExportWorkflow() {
      try {
        const params = { ...this.queryForm }
        await budgetWorkflowApi.export(params)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
    },
    
    // 设置
    handleSettings() {
      this.settingsDialogVisible = true
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'detail':
          this.handleView(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'deploy':
          this.handleDeploy(row)
          break
        case 'version':
          this.handleVersion(row)
          break
        case 'instance':
          this.handleInstance(row)
          break
        case 'monitor':
          this.handleMonitor(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
      }
    },
    
    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算工作流'
      this.dialogVisible = true
      this.workflowForm = { ...row, workflowId: null, workflowCode: '' }
    },
    
    // 版本管理
    handleVersion(row) {
      this.versionRow = { ...row }
      this.versionDialogVisible = true
    },

    // 流程实例
    handleInstance(row) {
      this.instanceRow = { ...row }
      this.instanceDialogVisible = true
    },

    // 流程监控
    handleMonitor(row) {
      this.monitorRow = { ...row }
      this.monitorDialogVisible = true
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        await budgetWorkflowApi.exportSingle(row.workflowId)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 保存设计
    handleSaveDesign() {
      this.$message.success('设计保存成功')
      this.designerDialogVisible = false
    },
    
    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },
    
    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
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
    
    // 判断是否可以编辑
    canEdit(row) {
      return row.workflowStatus !== 'ARCHIVED'
    },

    // 判断是否可以部署
    canDeploy(row) {
      return ['DRAFT', 'PUBLISHED'].includes(row.workflowStatus)
    },
    
    // 格式化持续时间
    formatDuration(duration) {
      if (!duration) return '-'
      const hours = Math.floor(duration / 3600)
      const minutes = Math.floor((duration % 3600) / 60)
      return `${hours}h${minutes}m`
    },
    
    // 获取工作流类型颜色
    getWorkflowTypeColor(type) {
      const colorMap = {
        'APPROVAL': 'primary',
        'REVIEW': 'success',
        'NOTIFICATION': 'info',
        'CALCULATION': 'warning',
        'INTEGRATION': 'danger'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取工作流类型文本
    getWorkflowTypeText(type) {
      const item = this.workflowTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取节点类型文本
    getNodeTypeText(type) {
      const item = this.nodeTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },

    // 获取成功率颜色
    getSuccessRateColor(rate) {
      if (rate >= 90) return '#67C23A'
      if (rate >= 70) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取工作流状态类型
    getWorkflowStatusType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'PUBLISHED': 'primary',
        'ACTIVE': 'success',
        'SUSPENDED': 'warning',
        'ARCHIVED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取工作流状态文本
    getWorkflowStatusText(status) {
      const item = this.workflowStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-workflow {
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
      
      &.draft-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.instance-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
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
    .workflow-categories {
      border-right: 1px solid #EBEEF5;
      padding-right: 20px;
      
      h4 {
        color: #303133;
        margin: 0 0 15px 0;
      }
      
      .tree-node {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        
        .node-count {
          color: #909399;
          font-size: 12px;
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
  
  .number-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #409EFF;
  }
  
  .duration-text {
    font-family: 'Courier New', monospace;
    color: #606266;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .workflow-nodes {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    
    .nodes-header {
      padding: 12px;
      background-color: #F5F7FA;
      border-bottom: 1px solid #EBEEF5;
      display: flex;
      gap: 8px;
    }
  }
  
  .workflow-designer {
    height: 600px;
    display: flex;
    flex-direction: column;
    
    .designer-toolbar {
      padding: 10px;
      border-bottom: 1px solid #EBEEF5;
      background-color: #F5F7FA;
    }
    
    .designer-canvas {
      flex: 1;
      position: relative;
      background-color: #FAFAFA;
      
      .canvas-placeholder {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        text-align: center;
        color: #909399;
        
        p {
          margin: 10px 0;
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

  .detail-stat-item {
    text-align: center;
    padding: 12px 0;
    background: #f5f7fa;
    border-radius: 4px;
    .detail-stat-value {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      line-height: 1.4;
    }
    .detail-stat-label {
      font-size: 12px;
      color: #909399;
      margin-top: 4px;
    }
  }

  .text-right {
    text-align: right;
  }
}
</style>
