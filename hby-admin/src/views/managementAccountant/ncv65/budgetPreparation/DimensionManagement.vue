<template>
  <div class="dimension-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>维度管理</h2>
      <p>管理预算维度体系，支持维度配置、成员管理、层级关系和维度权限</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateDimension">创建维度</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportDimension">导入维度</el-button>
            <el-button type="warning" icon="el-icon-check" @click="handleBatchValidate">批量验证</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportDimension">导出维度</el-button>
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

    <!-- 维度统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dimensionStats.totalDimensions }}</div>
            <div class="stat-label">维度总数</div>
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
            <div class="stat-number">{{ dimensionStats.activeDimensions }}</div>
            <div class="stat-label">启用维度</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="dimensionStats.activeRate" 
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
        <el-card class="stat-card members-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dimensionStats.totalMembers }}</div>
            <div class="stat-label">维度成员</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="dimensionStats.memberRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#409EFF"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-user"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card hierarchy-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ dimensionStats.hierarchyDimensions }}</div>
            <div class="stat-label">层级维度</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="dimensionStats.hierarchyRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#E6A23C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-operation"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 维度树和查询 -->
    <el-card class="search-card" shadow="never">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="dimension-tree">
            <div class="tree-header">
              <h4>维度体系</h4>
              <el-button-group size="mini">
                <el-button icon="el-icon-plus" @click="handleAddRootDimension">根维度</el-button>
                <el-button icon="el-icon-refresh" @click="loadDimensionTree">刷新</el-button>
              </el-button-group>
            </div>
            <!-- 全部维度入口 -->
            <div
              class="tree-all-entry"
              :class="{ 'is-active': !currentDimensionId }"
              @click="handleShowAllDimensions"
            >
              <i class="el-icon-s-grid"></i>
              <span>全部维度</span>
              <span class="entry-count">{{ dimensionStats.totalDimensions }}</span>
            </div>
            <el-tree
              ref="dimensionTree"
              :data="dimensionTree"
              :props="{ children: 'children', label: 'dimensionName' }"
              node-key="dimensionId"
              :current-node-key="currentDimensionId"
              :expand-on-click-node="false"
              :default-expand-all="true"
              highlight-current
              @node-click="handleDimensionClick"
            >
              <span class="tree-node" slot-scope="{ node, data }">
                <span class="node-icon">
                  <i :class="getDimensionIcon(data)" :style="{ color: getDimensionColor(data) }"></i>
                </span>
                <span class="node-label">{{ node.label }}</span>
                <span v-if="data.childCount > 0" class="node-count">{{ data.childCount }}</span>
                <span class="node-code">[{{ data.dimensionCode }}]</span>
                <span class="node-status">
                  <el-tag v-if="data.isActive === false || data.isActive === 0" type="danger" size="mini">停用</el-tag>
                </span>
              </span>
            </el-tree>
          </div>
        </el-col>
        <el-col :span="18">
          <el-form :model="queryForm" :inline="true" size="small">
            <el-form-item label="维度名称">
              <el-input
                v-model="queryForm.dimensionName"
                placeholder="请输入维度名称"
                clearable
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item label="维度编码">
              <el-input
                v-model="queryForm.dimensionCode"
                placeholder="请输入维度编码"
                clearable
                style="width: 150px"
              />
            </el-form-item>
            <el-form-item label="维度类型">
              <el-select
                v-model="queryForm.dimensionType"
                placeholder="请选择维度类型"
                clearable
                style="width: 150px"
              >
                <el-option
                  v-for="item in dimensionTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="维度状态">
              <el-select
                v-model="queryForm.dimensionStatus"
                placeholder="请选择维度状态"
                clearable
                style="width: 120px"
              >
                <el-option
                  v-for="item in dimensionStatusOptions"
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

    <!-- 维度列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算维度列表</span>
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
        :data="dimensionList"
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
        
        <el-table-column prop="dimensionCode" label="维度编码" width="150" show-overflow-tooltip />
        <el-table-column prop="dimensionName" label="维度名称" min-width="200" show-overflow-tooltip />
        
        <el-table-column prop="dimensionType" label="维度类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getDimensionTypeColor(scope.row.dimensionType)">
              {{ getDimensionTypeText(scope.row.dimensionType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="dimensionLevel" label="维度级次" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <el-tag size="mini" type="info">第{{ scope.row.dimensionLevel || 1 }}级</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="isHierarchy" label="层级维度" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isHierarchy ? 'success' : 'info'" size="mini">
              {{ scope.row.isHierarchy ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="memberCount" label="成员数量" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <span class="number-text">{{ scope.row.memberCount || 0 }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="isRequired" label="必填维度" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isRequired ? 'danger' : 'info'" size="mini">
              {{ scope.row.isRequired ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="isActive" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isActive"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        
        <el-table-column prop="lastModifyTime" label="最后修改" width="150" align="center" />
        
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >查看</el-button>
            <el-button
              v-if="canEdit(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-user"
              class="primary-text"
              @click="handleMembers(scope.row)"
            >成员</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
                <el-dropdown-item command="delete" icon="el-icon-delete" style="color:#F56C6C">删除</el-dropdown-item>
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

    <!-- 新增/编辑维度对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="dimensionForm"
        :model="dimensionForm"
        :rules="dimensionRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="维度名称" prop="dimensionName">
              <el-input
                v-model="dimensionForm.dimensionName"
                placeholder="请输入维度名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维度编码" prop="dimensionCode">
              <el-input
                v-model="dimensionForm.dimensionCode"
                placeholder="请输入维度编码"
                :disabled="!!dimensionForm.id"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="维度类型" prop="dimensionType">
              <el-select
                v-model="dimensionForm.dimensionType"
                placeholder="请选择维度类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in dimensionTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上级维度" prop="parentId">
              <el-select
                v-model="dimensionForm.parentId"
                placeholder="请选择上级维度(可选)"
                style="width: 100%"
                clearable
                filterable
              >
                <el-option
                  v-for="item in parentDimensionOptions"
                  :key="item.dimensionId"
                  :label="item.dimensionName"
                  :value="item.dimensionId"
                >
                  <span style="float: left">{{ item.dimensionName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.dimensionCode }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="维度描述" prop="dimensionDescription">
          <el-input
            v-model="dimensionForm.dimensionDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入维度描述"
          />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="默认成员" prop="defaultMember">
              <el-input
                v-model="dimensionForm.defaultMember"
                placeholder="请输入默认成员"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序号" prop="sortOrder">
              <el-input-number
                v-model="dimensionForm.sortOrder"
                :min="1"
                :max="9999"
                placeholder="请输入排序号"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="维度属性">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="dimensionForm.isHierarchy">层级维度</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="dimensionForm.isRequired">必填维度</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="dimensionForm.isActive">启用状态</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
        
        <el-form-item label="维度权限">
          <el-checkbox-group v-model="dimensionForm.permissions">
            <el-checkbox label="VIEW">查看权限</el-checkbox>
            <el-checkbox label="EDIT">编辑权限</el-checkbox>
            <el-checkbox label="DELETE">删除权限</el-checkbox>
            <el-checkbox label="MANAGE">管理权限</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        
        <!-- 维度成员预览 -->
        <el-form-item label="维度成员" v-if="dimensionForm.id">
          <div class="dimension-members">
            <div class="members-header">
              <span>成员数量：{{ dimensionForm.memberCount || 0 }}</span>
              <el-button type="text" @click="handleManageMembers">管理成员</el-button>
            </div>
            <div class="members-preview">
              <el-tag
                v-for="member in dimensionForm.memberPreview"
                :key="member.id"
                size="mini"
                style="margin: 2px"
              >
                {{ member.memberName }}
              </el-tag>
              <span v-if="dimensionForm.memberCount > 10" class="more-members">
                ...等{{ dimensionForm.memberCount - 10 }}个成员
              </span>
            </div>
          </div>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm">保存维度</el-button>
      </div>
    </el-dialog>

    <!-- 查看维度详情对话框 -->
    <el-dialog
      title="维度详情"
      :visible.sync="viewDialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="2" border size="small" v-if="viewDetail">
        <el-descriptions-item label="维度名称">{{ viewDetail.dimensionName }}</el-descriptions-item>
        <el-descriptions-item label="维度编码">{{ viewDetail.dimensionCode }}</el-descriptions-item>
        <el-descriptions-item label="维度类型">{{ getDimensionTypeText(viewDetail.dimensionType) }}</el-descriptions-item>
        <el-descriptions-item label="排序号">{{ viewDetail.sortOrder }}</el-descriptions-item>
        <el-descriptions-item label="是否必填">{{ viewDetail.isRequired ? '是' : '否' }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ viewDetail.isActive ? '启用' : '停用' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ viewDetail.creatorName }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewDetail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新人">{{ viewDetail.updaterName }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ viewDetail.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="维度描述" :span="2">{{ viewDetail.dimensionDescription || '无' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleEditFromView">编辑</el-button>
      </div>
    </el-dialog>

    <!-- 维度成员管理对话框 -->
    <el-dialog
      :title="'成员管理 - ' + currentMemberDimensionName"
      :visible.sync="memberDialogVisible"
      width="1000px"
      :close-on-click-modal="false"
      @close="handleMemberDialogClose"
    >
      <div class="dimension-member-management">
        <div class="member-toolbar">
          <el-button type="primary" size="small" @click="handleAddMember">添加成员</el-button>
          <el-button type="success" size="small" @click="handleImportMembers">导入成员</el-button>
          <el-button type="warning" size="small" @click="handleBatchDelete">批量删除</el-button>
        </div>
        
        <el-table
          :data="dimensionMembers"
          border
          size="small"
          max-height="400"
          @selection-change="handleMemberSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column prop="memberCode" label="成员编码" width="120" />
          <el-table-column prop="memberName" label="成员名称" width="150" />
          <el-table-column prop="parentMemberName" label="上级成员" width="150">
            <template slot-scope="scope">
              {{ scope.row.parentMemberName || currentMemberDimensionName || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="memberLevel" label="成员级次" width="80" align="center">
            <template slot-scope="scope">
              {{ scope.row.memberLevel || 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="isActive" label="状态" width="80" align="center">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.isActive" size="mini" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="handleEditMember(scope.row)">编辑</el-button>
              <el-button type="text" size="mini" class="danger-text" @click="handleDeleteMember(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="memberDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleSaveMembers">保存成员</el-button>
      </div>
    </el-dialog>

    <!-- 添加/编辑成员子弹窗 -->
    <el-dialog
      :title="memberEditTitle"
      :visible.sync="memberEditDialogVisible"
      width="500px"
      :close-on-click-modal="false"
      append-to-body
    >
      <el-form
        ref="memberForm"
        :model="memberForm"
        :rules="memberRules"
        label-width="100px"
        size="small"
      >
        <el-form-item label="成员编码" prop="memberCode">
          <el-input
            v-model="memberForm.memberCode"
            placeholder="请输入成员编码（大写字母、数字、下划线）"
            :disabled="!!memberForm.memberId"
          />
        </el-form-item>
        <el-form-item label="成员名称" prop="memberName">
          <el-input v-model="memberForm.memberName" placeholder="请输入成员名称" />
        </el-form-item>
        <el-form-item label="排序号">
          <el-input-number v-model="memberForm.sortOrder" :min="1" :max="9999" style="width:100%" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="memberForm.dimensionDescription" type="textarea" :rows="2" placeholder="请输入描述（可选）" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="memberForm.isActive" active-text="启用" inactive-text="停用" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="memberEditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitMember">确定</el-button>
      </div>
    </el-dialog>

    <!-- 导入成员对话框 -->
    <el-dialog
      title="导入成员"
      :visible.sync="importDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleImportDialogClose"
    >
      <div class="import-dialog-content">
        <!-- 导入说明 -->
        <el-alert
          title="导入说明"
          type="info"
          :closable="false"
          show-icon
          style="margin-bottom: 20px"
        >
          <div>1. 请先下载导入模板，按照模板格式填写数据</div>
          <div>2. 成员编码必须唯一，重复编码将更新已有数据</div>
          <div>3. 成员编码只能包含大写字母、数字、下划线和横线</div>
          <div>4. 支持的文件格式：.xlsx、.xls，文件大小不超过10MB</div>
        </el-alert>

        <!-- 下载模板按钮 -->
        <div style="margin-bottom: 20px">
          <el-button
            type="primary"
            icon="el-icon-download"
            size="small"
            @click="handleDownloadTemplate"
          >
            下载导入模板
          </el-button>
        </div>

        <!-- 文件上传区域 -->
        <el-upload
          ref="upload"
          :file-list="importFileList"
          :auto-upload="false"
          :before-upload="beforeUpload"
          :on-change="handleFileChange"
          :on-remove="handleRemove"
          :limit="1"
          accept=".xlsx,.xls"
          drag
          action="#"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <div class="el-upload__tip" slot="tip">
            只能上传 xlsx/xls 文件，且不超过 10MB
          </div>
        </el-upload>

        <!-- 导入结果 -->
        <el-collapse v-if="importResultVisible && importResult" style="margin-top: 20px">
          <el-collapse-item title="导入结果" name="result">
            <el-result
              :icon="importResult.fail > 0 ? 'warning' : 'success'"
              :title="'导入完成：成功 ' + importResult.success + ' 条，失败 ' + importResult.fail + ' 条'"
              :sub-title="'总计 ' + importResult.total + ' 条'"
            >
              <template v-if="importResult.errors && importResult.errors.length > 0" slot="extra">
                <el-alert
                  title="错误详情"
                  type="error"
                  :closable="false"
                >
                  <div v-for="(error, index) in importResult.errors" :key="index" style="margin-bottom: 5px">
                    第{{ error.row }}行：{{ error.message }}
                  </div>
                </el-alert>
              </template>
            </el-result>
          </el-collapse-item>
        </el-collapse>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleImportDialogClose">取消</el-button>
        <el-button
          type="primary"
          :loading="importing"
          @click="handleDoImport"
          :disabled="importFileList.length === 0"
        >
          开始导入
        </el-button>
      </div>
    </el-dialog>

    <!-- 导入维度对话框 -->
    <el-dialog
      title="导入维度"
      :visible.sync="importDimensionDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleImportDimensionDialogClose"
    >
      <div class="import-dialog-content">
        <!-- 导入说明 -->
        <el-alert
          title="导入说明"
          type="info"
          :closable="false"
          show-icon
          style="margin-bottom: 20px"
        >
          <div>1. 请先下载导入模板，按照模板格式填写数据</div>
          <div>2. 维度编码必须唯一，重复编码将更新已有数据</div>
          <div>3. 维度编码只能包含大写字母、数字、下划线和横线</div>
          <div>4. 支持的文件格式：.xlsx、.xls，文件大小不超过10MB</div>
        </el-alert>

        <!-- 下载模板按钮 -->
        <div style="margin-bottom: 20px">
          <el-button
            type="primary"
            icon="el-icon-download"
            size="small"
            @click="handleDownloadDimensionTemplate"
          >
            下载导入模板
          </el-button>
        </div>

        <!-- 文件上传区域 -->
        <el-upload
          ref="dimensionUpload"
          :file-list="importDimensionFileList"
          :auto-upload="false"
          :before-upload="beforeUpload"
          :on-change="handleDimensionFileChange"
          :on-remove="handleDimensionFileRemove"
          :limit="1"
          accept=".xlsx,.xls"
          drag
          action="#"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <div class="el-upload__tip" slot="tip">
            只能上传 xlsx/xls 文件，且不超过 10MB
          </div>
        </el-upload>

        <!-- 导入结果 -->
        <el-collapse v-if="importDimensionResultVisible && importDimensionResult" style="margin-top: 20px">
          <el-collapse-item title="导入结果" name="result">
            <el-result
              :icon="importDimensionResult.failCount > 0 ? 'warning' : 'success'"
              :title="'导入完成：成功 ' + importDimensionResult.successCount + ' 条，失败 ' + importDimensionResult.failCount + ' 条'"
              :sub-title="'总计 ' + importDimensionResult.totalCount + ' 条'"
            >
              <template v-if="importDimensionResult.errors && importDimensionResult.errors.length > 0" slot="extra">
                <el-alert
                  title="错误详情"
                  type="error"
                  :closable="false"
                >
                  <div v-for="(error, index) in importDimensionResult.errors" :key="index" style="margin-bottom: 5px">
                    {{ error }}
                  </div>
                </el-alert>
              </template>
            </el-result>
          </el-collapse-item>
        </el-collapse>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleImportDimensionDialogClose">取消</el-button>
        <el-button
          type="primary"
          :loading="importingDimension"
          @click="handleDoImportDimension"
          :disabled="importDimensionFileList.length === 0"
        >
          开始导入
        </el-button>
      </div>
    </el-dialog>

    <!-- 维度设置对话框 -->
    <el-dialog title="维度设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="140px" size="small">
        <el-form-item label="启用维度校验">
          <el-switch v-model="settingsForm.enableValidation" />
        </el-form-item>
        <el-form-item label="允许多级维度">
          <el-switch v-model="settingsForm.allowMultiLevel" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { dimensionApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'DimensionManagement',
  data() {
    return {
      // 查询参数
      queryForm: {
        dimensionName: '',
        dimensionCode: '',
        dimensionType: '',
        dimensionStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      dimensionList: [],
      total: 0,
      selectedRows: [],
      
      // 统计数据
      dimensionStats: {
        totalDimensions: 0,
        activeDimensions: 0,
        totalMembers: 0,
        hierarchyDimensions: 0,
        activeRate: 0,
        memberRate: 0,
        hierarchyRate: 0
      },
      
      // 维度树
      dimensionTree: [],
      currentDimensionId: null,
      parentDimensionOptions: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      viewDialogVisible: false,
      viewDetail: null,
      memberDialogVisible: false,
      currentMemberDimensionId: null,
      currentMemberDimensionName: '',
      dimensionForm: {
        id: null,
        dimensionName: '',
        dimensionCode: '',
        dimensionType: '',
        parentDimensionId: null,
        dimensionDescription: '',
        defaultMember: '',
        sortOrder: 1,
        isHierarchy: false,
        isRequired: false,
        isActive: true,
        permissions: [],
        memberCount: 0,
        memberPreview: []
      },
      dimensionRules: {
        dimensionName: [
          { required: true, message: '请输入维度名称', trigger: 'blur' }
        ],
        dimensionCode: [
          { required: true, message: '请输入维度编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '维度编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        dimensionType: [
          { required: true, message: '请选择维度类型', trigger: 'change' }
        ]
      },
      
      // 维度成员
      dimensionMembers: [],
      selectedMembers: [],
      memberImportLoading: false,
      memberImportResult: null,
      memberEditDialogVisible: false,
      memberEditTitle: '',
      memberEditingIndex: -1,
      memberForm: {
        memberId: null,
        memberCode: '',
        memberName: '',
        sortOrder: 1,
        isActive: true,
        dimensionDescription: ''
      },
      memberRules: {
        memberCode: [
          { required: true, message: '请输入成员编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '成员编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        memberName: [
          { required: true, message: '请输入成员名称', trigger: 'blur' }
        ]
      },

      // 导入成员相关
      importDialogVisible: false,
      importFileList: [],
      importing: false,
      importResult: null,
      importResultVisible: false,

      // 导入维度相关
      importDimensionDialogVisible: false,
      importDimensionFileList: [],
      importingDimension: false,
      importDimensionResult: null,
      importDimensionResultVisible: false,

      // 选项数据
      dimensionTypeOptions: [
        { value: 'ORGANIZATION', label: '组织维度' },
        { value: 'ACCOUNT', label: '科目维度' },
        { value: 'PROJECT', label: '项目维度' },
        { value: 'PRODUCT', label: '产品维度' },
        { value: 'CUSTOMER', label: '客户维度' },
        { value: 'REGION', label: '地区维度' },
        { value: 'TIME', label: '时间维度' },
        { value: 'CUSTOM', label: '自定义维度' }
      ],
      dimensionStatusOptions: [
        { value: 'ACTIVE', label: '启用' },
        { value: 'INACTIVE', label: '停用' }
      ],

      settingsDialogVisible: false,
      settingsForm: { enableValidation: true, allowMultiLevel: true }
    }
  },
  
  created() {
    this.getList()
    this.loadDimensionTree()
    this.loadParentDimensionOptions()
    this.loadStats()
  },
  
  methods: {
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        // 构建查询参数，过滤掉空字符串
        const params = {
          ...this.queryParams
        }

        // 只添加非空的查询条件
        if (this.queryForm.dimensionName) {
          params.dimensionName = this.queryForm.dimensionName
        }
        if (this.queryForm.dimensionCode) {
          params.dimensionCode = this.queryForm.dimensionCode
        }
        if (this.queryForm.dimensionType) {
          params.dimensionType = this.queryForm.dimensionType
        }
        if (this.queryForm.dimensionStatus) {
          params.dimensionStatus = this.queryForm.dimensionStatus
        }

        // 只有当选择了维度树节点时才添加 parentDimensionId 参数
        if (this.currentDimensionId) {
          params.parentDimensionId = this.currentDimensionId
          params.includeChildren = true
        }

        const response = await dimensionApi.getPage(params)
        this.dimensionList = response.data.records
        this.total = response.data.total
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载维度树
    async loadDimensionTree() {
      try {
        const response = await dimensionApi.getDimensionTree()
        // 递归计算每个节点的后代总数
        const enrichTree = (nodes) => {
          if (!nodes) return []
          return nodes.map(node => {
            const children = enrichTree(node.children || [])
            const childCount = children.reduce((sum, c) => sum + 1 + (c.childCount || 0), 0)
            return { ...node, children, childCount }
          })
        }
        this.dimensionTree = enrichTree(response.data)
      } catch (error) {
        console.error('加载维度树失败：', error)
      }
    },
    
    // 加载上级维度选项
    async loadParentDimensionOptions() {
      try {
        const response = await dimensionApi.getParentDimensions()
        this.parentDimensionOptions = response.data
      } catch (error) {
        console.error('加载上级维度选项失败：', error)
      }
    },

    // 加载统计数据
    async loadStats() {
      try {
        const response = await dimensionApi.getStats()
        if (response.data) {
          this.dimensionStats = response.data
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
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
        dimensionName: '',
        dimensionCode: '',
        dimensionType: '',
        dimensionStatus: ''
      }
      // 同时清除树筛选
      this.currentDimensionId = null
      if (this.$refs.dimensionTree) {
        this.$refs.dimensionTree.setCurrentKey(null)
      }
      this.handleQuery()
    },
    
    // 维度点击
    handleDimensionClick(data) {
      this.currentDimensionId = data.dimensionId
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 显示全部维度（清除树筛选）
    handleShowAllDimensions() {
      this.currentDimensionId = null
      // 清除树的选中状态
      if (this.$refs.dimensionTree) {
        this.$refs.dimensionTree.setCurrentKey(null)
      }
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 创建维度
    handleCreateDimension() {
      this.dialogTitle = '创建预算维度'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 添加根维度
    handleAddRootDimension() {
      this.dialogTitle = '创建根维度'
      this.dialogVisible = true
      this.resetForm()
      this.dimensionForm.parentDimensionId = null
    },
    
    // 编辑维度
    async handleEdit(row) {
      this.dialogTitle = '编辑预算维度'
      try {
        const response = await dimensionApi.getDetail(row.dimensionId)
        if (response.code === 1 && response.data) {
          const d = response.data
          this.dimensionForm = {
            id: d.dimensionId,
            dimensionName: d.dimensionName,
            dimensionCode: d.dimensionCode,
            dimensionType: d.dimensionType,
            parentId: d.parentId || null,
            dimensionDescription: d.dimensionDescription || '',
            defaultMember: '',
            sortOrder: d.sortOrder || 1,
            isHierarchy: false,
            isRequired: d.isRequired === 1 || d.isRequired === true,
            isActive: d.isActive !== false && d.isActive !== 0,
            permissions: [],
            memberCount: 0,
            memberPreview: []
          }
          this.dialogVisible = true
        } else {
          this.$message.error('获取维度详情失败')
        }
      } catch (error) {
        this.$message.error('获取维度详情失败：' + error.message)
      }
    },

    // 查看维度
    async handleView(row) {
      try {
        const response = await dimensionApi.getDetail(row.dimensionId)
        if (response.code === 1 && response.data) {
          this.viewDetail = response.data
          this.viewDialogVisible = true
        } else {
          this.$message.error('获取维度详情失败')
        }
      } catch (error) {
        this.$message.error('获取维度详情失败：' + error.message)
      }
    },

    // 从查看弹窗跳转到编辑
    handleEditFromView() {
      this.viewDialogVisible = false
      if (this.viewDetail) {
        this.handleEdit({ dimensionId: this.viewDetail.dimensionId })
      }
    },

    // 维度成员管理
    async handleMembers(row) {
      this.currentMemberDimensionId = row.dimensionId
      this.currentMemberDimensionName = row.dimensionName || ''
      this.memberDialogVisible = true
      await this.loadMembers()
    },

    // 状态改变
    async handleStatusChange(row) {
      try {
        await dimensionApi.updateStatus(row.dimensionId, row.isActive)
        this.$message.success('状态更新成功')
      } catch (error) {
        this.$message.error('状态更新失败：' + error.message)
        row.isActive = !row.isActive
      }
    },
    
    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.dimensionForm.validate()

        // 只提交后端实体类认识的字段，并做类型转换
        const params = {
          dimensionId: this.dimensionForm.id || undefined,
          dimensionName: this.dimensionForm.dimensionName,
          dimensionCode: this.dimensionForm.dimensionCode,
          dimensionType: this.dimensionForm.dimensionType,
          parentId: this.dimensionForm.parentId || null,
          sortOrder: this.dimensionForm.sortOrder,
          isRequired: this.dimensionForm.isRequired ? 1 : 0,
          isActive: this.dimensionForm.isActive !== false,
          dimensionDescription: this.dimensionForm.dimensionDescription || null
        }

        if (this.dimensionForm.id) {
          await dimensionApi.update(params)
          this.$message.success('更新成功')
        } else {
          await dimensionApi.create(params)
          this.$message.success('创建成功')
        }

        this.dialogVisible = false
        this.getList()
        this.loadDimensionTree()
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },
    
    // 重置表单
    resetForm() {
      this.dimensionForm = {
        id: null,
        dimensionName: '',
        dimensionCode: '',
        dimensionType: '',
        parentDimensionId: null,
        dimensionDescription: '',
        defaultMember: '',
        sortOrder: 1,
        isHierarchy: false,
        isRequired: false,
        isActive: true,
        permissions: [],
        memberCount: 0,
        memberPreview: []
      }
      this.$nextTick(() => {
        this.$refs.dimensionForm && this.$refs.dimensionForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 管理成员
    handleManageMembers() {
      this.memberDialogVisible = true
    },
    
    // 添加成员
    handleAddMember() {
      this.memberEditTitle = '添加成员'
      this.memberEditingIndex = -1
      this.memberForm = {
        memberId: null,
        memberCode: '',
        memberName: '',
        sortOrder: this.dimensionMembers.length + 1,
        isActive: true,
        dimensionDescription: ''
      }
      this.$nextTick(() => {
        this.$refs.memberForm && this.$refs.memberForm.clearValidate()
      })
      this.memberEditDialogVisible = true
    },

    // 编辑成员
    handleEditMember(row) {
      this.memberEditTitle = '编辑成员'
      this.memberEditingIndex = this.dimensionMembers.indexOf(row)
      this.memberForm = {
        memberId: row.memberId || null,
        memberCode: row.memberCode || '',
        memberName: row.memberName || '',
        sortOrder: row.sortOrder || 1,
        isActive: row.isActive !== false,
        dimensionDescription: row.dimensionDescription || ''
      }
      this.$nextTick(() => {
        this.$refs.memberForm && this.$refs.memberForm.clearValidate()
      })
      this.memberEditDialogVisible = true
    },

    // 提交成员表单（添加/编辑）
    async handleSubmitMember() {
      try {
        await this.$refs.memberForm.validate()
      } catch {
        return
      }
      const item = { ...this.memberForm }
      if (this.memberEditingIndex >= 0) {
        this.$set(this.dimensionMembers, this.memberEditingIndex, item)
      } else {
        this.dimensionMembers.push(item)
      }
      this.memberEditDialogVisible = false
    },
    
    // 删除成员
    async handleDeleteMember(row) {
      try {
        await this.$confirm('确定要删除该成员吗？', '提示', { type: 'warning' })
        if (row.memberId) {
          await dimensionApi.deleteMember(row.memberId)
          this.$message.success('删除成功')
        }
        const index = this.dimensionMembers.findIndex(item => item.memberId === row.memberId && item.memberCode === row.memberCode)
        if (index > -1) {
          this.dimensionMembers.splice(index, 1)
        }
      } catch (e) {
        if (e !== 'cancel') {
          this.$message.error('删除失败：' + e.message)
        }
      }
    },
    
    // 导入成员
    handleImportMembers() {
      if (!this.currentMemberDimensionId) {
        this.$message.warning('请先打开一个维度的成员管理')
        return
      }
      this.importDialogVisible = true
      this.importFileList = []
      this.importResult = null
      this.importResultVisible = false
    },

    // 下载导入模板
    async handleDownloadTemplate() {
      try {
        const response = await dimensionApi.downloadTemplate()
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '维度成员导入模板.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('模板下载成功')
      } catch (error) {
        this.$message.error('模板下载失败：' + error.message)
      }
    },

    // 文件上传前校验
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                      file.type === 'application/vnd.ms-excel'
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isExcel) {
        this.$message.error('上传文件只能是 Excel 格式!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      return true
    },

    // 文件状态改变
    handleFileChange(_file, fileList) {
      this.importFileList = fileList.slice(-1)
      this.importResult = null
      this.importResultVisible = false
    },

    // 移除文件
    handleRemove() {
      this.importFileList = []
      this.importResult = null
      this.importResultVisible = false
    },

    // 执行导入
    async handleDoImport() {
      if (!this.currentMemberDimensionId) {
        this.$message.error('未找到维度ID，请重新打开成员管理')
        return
      }
      if (this.importFileList.length === 0) {
        this.$message.warning('请选择要导入的文件')
        return
      }

      const formData = new FormData()
      formData.append('file', this.importFileList[0].raw)

      this.importing = true
      try {
        const response = await dimensionApi.importMembers(this.currentMemberDimensionId, formData)
        if (response.code === 1) {
          this.importResult = response.data
          this.importResultVisible = true
          this.$message.success('导入完成，成功 ' + response.data.success + ' 条，失败 ' + response.data.fail + ' 条')
          // 刷新成员列表
          await this.loadMembers()
        } else {
          this.$message.error('导入失败：' + response.msg)
        }
      } catch (error) {
        this.$message.error('导入失败：' + error.message)
      } finally {
        this.importing = false
      }
    },

    // 刷新成员列表
    async loadMembers() {
      if (!this.currentMemberDimensionId) {
        return
      }
      try {
        const response = await dimensionApi.getMembers(this.currentMemberDimensionId)
        if (response.code === 1) {
          this.dimensionMembers = response.data || []
        } else {
          this.$message.error('获取维度成员失败：' + response.msg)
        }
      } catch (error) {
        this.$message.error('获取维度成员失败：' + error.message)
      }
    },

    // 关闭导入对话框
    handleImportDialogClose() {
      this.importFileList = []
      this.importResult = null
      this.importResultVisible = false
      this.importDialogVisible = false
    },
    
    // 批量删除成员
    async handleBatchDelete() {
      if (this.selectedMembers.length === 0) {
        this.$message.warning('请选择要删除的成员')
        return
      }
      try {
        await this.$confirm(`确定要删除选中的 ${this.selectedMembers.length} 个成员吗？`, '提示', { type: 'warning' })
        for (const member of this.selectedMembers) {
          if (member.memberId) {
            await dimensionApi.deleteMember(member.memberId)
          }
          const index = this.dimensionMembers.findIndex(item => item === member)
          if (index > -1) {
            this.dimensionMembers.splice(index, 1)
          }
        }
        this.selectedMembers = []
        this.$message.success('批量删除成功')
      } catch (e) {
        if (e !== 'cancel') {
          this.$message.error('删除失败：' + e.message)
        }
      }
    },

    // 成员弹窗关闭时重新从后端加载（丢弃未保存的修改）
    async handleMemberDialogClose() {
      this.dimensionMembers = []
      this.selectedMembers = []
      this.memberEditDialogVisible = false
    },
    
    // 保存成员
    async handleSaveMembers() {
      if (!this.currentMemberDimensionId) {
        this.$message.error('未找到维度ID，请重新打开成员管理')
        return
      }
      try {
        await dimensionApi.saveMembers(this.currentMemberDimensionId, this.dimensionMembers)
        this.$message.success('成员保存成功')
        this.memberDialogVisible = false
        this.getList()
        this.loadStats()
      } catch (error) {
        this.$message.error('成员保存失败：' + error.message)
      }
    },
    
    // 成员选择改变
    handleMemberSelectionChange(selection) {
      this.selectedMembers = selection
    },
    
    // 导入维度
    handleImportDimension() {
      this.importDimensionDialogVisible = true
      this.importDimensionFileList = []
      this.importDimensionResult = null
      this.importDimensionResultVisible = false
    },

    // 下载维度导入模板
    async handleDownloadDimensionTemplate() {
      try {
        const response = await dimensionApi.downloadDimensionTemplate()
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算维度导入模板.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('模板下载成功')
      } catch (error) {
        this.$message.error('模板下载失败：' + error.message)
      }
    },

    // 维度文件状态改变
    handleDimensionFileChange(_file, fileList) {
      this.importDimensionFileList = fileList.slice(-1)
    },

    // 移除维度文件
    handleDimensionFileRemove() {
      this.importDimensionFileList = []
    },

    // 执行维度导入
    async handleDoImportDimension() {
      if (this.importDimensionFileList.length === 0) {
        this.$message.warning('请选择要导入的文件')
        return
      }

      const formData = new FormData()
      formData.append('file', this.importDimensionFileList[0].raw)

      this.importingDimension = true
      try {
        const response = await dimensionApi.importDimensions(formData)
        if (response.code === 1) {
          this.importDimensionResult = response.data
          this.importDimensionResultVisible = true
          this.$message.success('导入完成，成功 ' + response.data.successCount + ' 条，失败 ' + response.data.failCount + ' 条')
          // 刷新列表和统计
          this.getList()
          this.loadDimensionTree()
          this.loadStats()
        } else {
          this.$message.error('导入失败：' + response.msg)
        }
      } catch (error) {
        this.$message.error('导入失败：' + error.message)
      } finally {
        this.importingDimension = false
      }
    },

    // 关闭维度导入对话框
    handleImportDimensionDialogClose() {
      this.importDimensionFileList = []
      this.importDimensionResult = null
      this.importDimensionResultVisible = false
      this.importDimensionDialogVisible = false
    },
    
    // 批量验证
    async handleBatchValidate() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要验证的维度')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.id)
        await dimensionApi.batchValidate(ids)
        this.$message.success('批量验证完成')
        this.getList()
      } catch (error) {
        this.$message.error('批量验证失败：' + error.message)
      }
    },
    
    // 导出维度
    async handleExportDimension() {
      try {
        const params = { ...this.queryForm }

        // 如果有选中的行，只导出选中的维度
        if (this.selectedRows && this.selectedRows.length > 0) {
          params.selectedIds = this.selectedRows.map(row => row.id || row.dimensionId)
          this.$message.info(`正在导出 ${this.selectedRows.length} 条维度数据...`)
        } else {
          this.$message.info('正在导出当前页面所有维度数据...')
        }

        const response = await dimensionApi.export(params)

        // 处理 blob 响应，触发文件下载
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        const fileName = params.selectedIds
          ? `维度选中数据_${new Date().getTime()}.xlsx`
          : `预算维度数据_${new Date().getTime()}.xlsx`
        link.download = fileName
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
      this.loadDimensionTree()
      this.loadStats()
    },
    
    // 设置
    handleSettings() {
      this.settingsDialogVisible = true
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopy(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算维度'
      this.resetForm()
      this.dimensionForm.dimensionName = row.dimensionName
      this.dimensionForm.dimensionType = row.dimensionType
      this.dimensionForm.parentId = row.parentId || null
      this.dimensionForm.dimensionDescription = row.dimensionDescription || ''
      this.dimensionForm.sortOrder = row.sortOrder || 1
      this.dimensionForm.isRequired = row.isRequired === 1 || row.isRequired === true
      this.dimensionForm.isActive = row.isActive !== false && row.isActive !== 0
      this.dialogVisible = true
    },

    // 删除维度
    async handleDelete(row) {
      try {
        await this.$confirm(`确定要删除维度"${row.dimensionName}"吗？此操作不可恢复！`, '警告', { type: 'warning', confirmButtonText: '确定删除', confirmButtonClass: 'el-button--danger' })
        await dimensionApi.delete(row.dimensionId)
        this.$message.success('删除成功')
        this.getList()
        this.loadDimensionTree()
        this.loadStats()
      } catch (e) {
        if (e !== 'cancel') {
          this.$message.error('删除失败：' + e.message)
        }
      }
    },

    // 导出单个
    async handleExportSingle(row) {
      try {
        const response = await dimensionApi.exportSingle(row.dimensionId)
        const blob = new Blob([response], { type: 'application/octet-stream' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `维度_${row.dimensionName}_${row.dimensionCode}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 行点击（不自动触发查看，避免误操作）
    handleRowClick() {
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
    canEdit() {
      return true
    },
    
    // 获取维度图标
    getDimensionIcon(dimension) {
      const iconMap = {
        'ORGANIZATION': 'el-icon-office-building',
        'ACCOUNT': 'el-icon-s-finance',
        'PROJECT': 'el-icon-s-management',
        'PRODUCT': 'el-icon-goods',
        'CUSTOMER': 'el-icon-user',
        'REGION': 'el-icon-location',
        'TIME': 'el-icon-time',
        'CUSTOM': 'el-icon-setting'
      }
      return iconMap[dimension.dimensionType] || 'el-icon-s-grid'
    },
    
    // 获取维度颜色
    getDimensionColor(dimension) {
      const colorMap = {
        'ORGANIZATION': '#67C23A',
        'ACCOUNT': '#E6A23C',
        'PROJECT': '#409EFF',
        'PRODUCT': '#F56C6C',
        'CUSTOMER': '#909399',
        'REGION': '#67C23A',
        'TIME': '#E6A23C',
        'CUSTOM': '#409EFF'
      }
      return colorMap[dimension.dimensionType] || '#606266'
    },
    
    // 获取维度类型颜色
    getDimensionTypeColor(type) {
      const colorMap = {
        'ORGANIZATION': 'success',
        'ACCOUNT': 'warning',
        'PROJECT': 'primary',
        'PRODUCT': 'danger',
        'CUSTOMER': 'info',
        'REGION': 'success',
        'TIME': 'warning',
        'CUSTOM': 'primary'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取维度类型文本
    getDimensionTypeText(type) {
      const item = this.dimensionTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    }
  }
}
</script>

<style lang="scss" scoped>
.dimension-management {
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
      
      &.members-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.hierarchy-card {
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
    .dimension-tree {
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
      
      .tree-all-entry {
        display: flex;
        align-items: center;
        padding: 8px 12px;
        margin-bottom: 8px;
        border-radius: 4px;
        cursor: pointer;
        color: #606266;
        font-size: 14px;
        transition: all 0.2s;
        
        i {
          margin-right: 8px;
          font-size: 16px;
        }
        
        .entry-count {
          margin-left: auto;
          background: #F0F2F5;
          color: #909399;
          padding: 0 8px;
          border-radius: 10px;
          font-size: 12px;
          line-height: 20px;
        }
        
        &:hover {
          background: #F5F7FA;
          color: #409EFF;
        }
        
        &.is-active {
          background: #ECF5FF;
          color: #409EFF;
          font-weight: 500;
          
          .entry-count {
            background: #409EFF;
            color: #fff;
          }
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
        
        .node-count {
          background: #F0F2F5;
          color: #909399;
          padding: 0 6px;
          border-radius: 10px;
          font-size: 11px;
          line-height: 18px;
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
  
  .number-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #409EFF;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .dimension-members {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    padding: 10px;
    
    .members-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
    }
    
    .members-preview {
      .more-members {
        color: #909399;
        font-size: 12px;
      }
    }
  }
  
  .dimension-member-management {
    .member-toolbar {
      margin-bottom: 15px;
      display: flex;
      gap: 8px;
    }
  }
  
  .primary-text {
    color: #409EFF;
  }
  
  .danger-text {
    color: #F56C6C;
  }
  
  .text-right {
    text-align: right;
  }

  // 导入对话框样式
  .import-dialog-content {
    .el-upload {
      width: 100%;
    }
    .el-upload-dragger {
      width: 100%;
    }
    .el-upload__tip {
      margin-top: 10px;
      font-size: 12px;
      color: #909399;
    }
    .el-collapse {
      margin-top: 20px;
    }
  }
}
</style>
