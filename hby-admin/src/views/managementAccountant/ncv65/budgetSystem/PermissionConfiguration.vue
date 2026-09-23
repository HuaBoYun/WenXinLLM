<template>
  <div class="permission-configuration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>权限配置</h2>
      <p>管理预算系统的用户权限、角色权限、数据权限和功能权限，确保系统安全性和数据保密性</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleAddRole">新建角色</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExport">导出权限</el-button>
            <el-button type="info" icon="el-icon-upload2" @click="handleImport">导入权限</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-user" @click="handleUserPermission">用户权限</el-button>
            <el-button icon="el-icon-s-custom" @click="handleDataPermission">数据权限</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 权限统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card stat-roles" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ permissionStats.totalRoles }}</div>
            <div class="stat-label">角色总数</div>
            <div class="stat-description">系统中的角色数量</div>
            <div class="stat-trend">
              <i class="el-icon-s-custom"></i>
              <span>角色管理</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-custom"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card stat-users" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ permissionStats.totalUsers }}</div>
            <div class="stat-label">用户总数</div>
            <div class="stat-description">已分配权限的用户</div>
            <div class="stat-trend">
              <i class="el-icon-user"></i>
              <span>用户管理</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-user"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card stat-permissions" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ permissionStats.totalPermissions }}</div>
            <div class="stat-label">权限总数</div>
            <div class="stat-description">系统功能权限数</div>
            <div class="stat-trend">
              <i class="el-icon-key"></i>
              <span>权限丰富</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-key"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card stat-data-permissions" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ permissionStats.dataPermissions }}</div>
            <div class="stat-label">数据权限</div>
            <div class="stat-description">数据访问权限数</div>
            <div class="stat-trend">
              <i class="el-icon-files"></i>
              <span>数据安全</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-files"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 权限配置主要内容 -->
    <el-row :gutter="20">
      <!-- 左侧角色列表 -->
      <el-col :span="8">
        <el-card class="roles-card" shadow="never">
          <div slot="header" class="card-header">
            <span>角色列表</span>
            <div class="header-tools">
              <el-button icon="el-icon-plus" size="mini" @click="handleAddRole">新建</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="refreshRoles">刷新</el-button>
            </div>
          </div>
          <div class="roles-list">
            <div
              v-for="role in rolesList"
              :key="role.permissionId"
              class="role-item"
              :class="{ 'active': selectedRole && selectedRole.permissionId === role.permissionId }"
              @click="handleSelectRole(role)"
            >
              <div class="role-info">
                <div class="role-name">{{ role.roleName }}</div>
                <div class="role-description">{{ role.roleDescription }}</div>
                <div class="role-stats">
                  <span class="user-count">{{ role.userCount }} 用户</span>
                  <span class="permission-count">{{ role.permissionCount }} 权限</span>
                </div>
              </div>
              <div class="role-status">
                <el-tag :type="getRoleStatusColor(role.status)" size="mini">
                  {{ getRoleStatusText(role.status) }}
                </el-tag>
              </div>
              <div class="role-actions">
                <el-dropdown @command="(command) => handleRoleAction(command, role)">
                  <el-button type="text" size="mini">
                    <i class="el-icon-more"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="edit">编辑</el-dropdown-item>
                    <el-dropdown-item command="copy">复制</el-dropdown-item>
                    <el-dropdown-item command="users">用户管理</el-dropdown-item>
                    <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 右侧权限配置 -->
      <el-col :span="16">
        <el-card class="permissions-card" shadow="never">
          <div slot="header" class="card-header">
            <span>{{ selectedRole ? selectedRole.roleName + ' - 权限配置' : '权限配置' }}</span>
            <div class="header-tools" v-if="selectedRole">
              <el-button icon="el-icon-check" size="mini" @click="handleSavePermissions">保存权限</el-button>
              <el-button icon="el-icon-refresh-left" size="mini" @click="handleResetPermissions">重置</el-button>
            </div>
          </div>
          
          <div v-if="selectedRole" class="permission-content">
            <el-tabs v-model="permissionActiveTab" type="card">
              <el-tab-pane label="功能权限" name="function">
                <div class="permission-tree-container">
                  <div class="tree-toolbar">
                    <el-button size="mini" @click="expandAllPermissions">展开全部</el-button>
                    <el-button size="mini" @click="collapseAllPermissions">收起全部</el-button>
                    <el-button size="mini" @click="checkAllPermissions">全选</el-button>
                    <el-button size="mini" @click="uncheckAllPermissions">取消全选</el-button>
                  </div>
                  <el-tree
                    ref="permissionTree"
                    :data="functionPermissions"
                    :props="permissionTreeProps"
                    node-key="id"
                    show-checkbox
                    :check-strictly="false"
                    :default-checked-keys="checkedPermissions"
                    @check="handlePermissionCheck"
                  >
                    <span class="permission-node" slot-scope="{ node, data }">
                      <span class="node-icon">
                        <i :class="getPermissionIcon(data)"></i>
                      </span>
                      <span class="node-label">{{ node.label }}</span>
                      <span class="node-description">{{ data.description }}</span>
                    </span>
                  </el-tree>
                </div>
              </el-tab-pane>
              
              <el-tab-pane label="数据权限" name="data">
                <div class="data-permission-container">
                  <el-form :model="dataPermissionForm" label-width="120px" size="small">
                    <el-form-item label="数据范围">
                      <el-radio-group v-model="dataPermissionForm.dataScope">
                        <el-radio label="ALL">全部数据</el-radio>
                        <el-radio label="DEPT">本部门数据</el-radio>
                        <el-radio label="DEPT_AND_CHILD">本部门及子部门数据</el-radio>
                        <el-radio label="SELF">仅本人数据</el-radio>
                        <el-radio label="CUSTOM">自定义数据</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    
                    <el-form-item label="组织权限" v-if="dataPermissionForm.dataScope === 'CUSTOM'">
                      <el-tree
                        ref="orgPermissionTree"
                        :data="organizationTree"
                        :props="orgTreeProps"
                        node-key="id"
                        show-checkbox
                        :check-strictly="true"
                        :default-checked-keys="checkedOrganizations"
                        @check="handleOrgPermissionCheck"
                      />
                    </el-form-item>
                    
                    <el-form-item label="预算期间">
                      <el-checkbox-group v-model="dataPermissionForm.budgetPeriods">
                        <el-checkbox label="2024">2024年</el-checkbox>
                        <el-checkbox label="2025">2025年</el-checkbox>
                        <el-checkbox label="2026">2026年</el-checkbox>
                      </el-checkbox-group>
                    </el-form-item>
                    
                    <el-form-item label="预算类型">
                      <el-checkbox-group v-model="dataPermissionForm.budgetTypes">
                        <el-checkbox label="OPERATING">经营预算</el-checkbox>
                        <el-checkbox label="CAPITAL">资本预算</el-checkbox>
                        <el-checkbox label="CASH_FLOW">现金流预算</el-checkbox>
                        <el-checkbox label="FINANCIAL">财务预算</el-checkbox>
                      </el-checkbox-group>
                    </el-form-item>
                    
                    <el-form-item label="操作权限">
                      <el-checkbox-group v-model="dataPermissionForm.operations">
                        <el-checkbox label="VIEW">查看</el-checkbox>
                        <el-checkbox label="CREATE">新增</el-checkbox>
                        <el-checkbox label="EDIT">编辑</el-checkbox>
                        <el-checkbox label="DELETE">删除</el-checkbox>
                        <el-checkbox label="APPROVE">审批</el-checkbox>
                        <el-checkbox label="EXPORT">导出</el-checkbox>
                      </el-checkbox-group>
                    </el-form-item>
                  </el-form>
                </div>
              </el-tab-pane>
              
              <el-tab-pane label="字段权限" name="field">
                <div class="field-permission-container">
                  <el-table :data="fieldPermissions" border size="mini">
                    <el-table-column prop="fieldName" label="字段名称" width="150" />
                    <el-table-column prop="fieldLabel" label="字段标签" width="150" />
                    <el-table-column prop="fieldType" label="字段类型" width="100">
                      <template slot-scope="scope">
                        <el-tag :type="getFieldTypeColor(scope.row.fieldType)" size="mini">
                          {{ getFieldTypeText(scope.row.fieldType) }}
                        </el-tag>
                      </template>
                    </el-table-column>
                    <el-table-column prop="canView" label="可查看" width="80" align="center">
                      <template slot-scope="scope">
                        <el-switch
                          v-model="scope.row.canView"
                          @change="handleFieldPermissionChange(scope.row)"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column prop="canEdit" label="可编辑" width="80" align="center">
                      <template slot-scope="scope">
                        <el-switch
                          v-model="scope.row.canEdit"
                          :disabled="!scope.row.canView"
                          @change="handleFieldPermissionChange(scope.row)"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column prop="isSensitive" label="敏感字段" width="80" align="center">
                      <template slot-scope="scope">
                        <el-tag :type="scope.row.isSensitive ? 'danger' : 'info'" size="mini">
                          {{ scope.row.isSensitive ? '是' : '否' }}
                        </el-tag>
                      </template>
                    </el-table-column>
                    <el-table-column prop="maskRule" label="脱敏规则" />
                  </el-table>
                </div>
              </el-tab-pane>
              
              <el-tab-pane label="用户分配" name="users">
                <div class="user-assignment-container">
                  <div class="assignment-toolbar">
                    <el-button type="primary" size="mini" @click="handleAssignUsers">分配用户</el-button>
                    <el-button type="warning" size="mini" @click="handleBatchRemoveUsers">批量移除</el-button>
                    <el-input
                      v-model="userSearchKeyword"
                      placeholder="搜索用户"
                      size="mini"
                      style="width: 200px; margin-left: 10px;"
                      @input="handleUserSearch"
                    >
                      <i slot="prefix" class="el-input__icon el-icon-search"></i>
                    </el-input>
                  </div>
                  <el-table
                    :data="assignedUsers"
                    border
                    size="mini"
                    @selection-change="handleUserSelectionChange"
                  >
                    <el-table-column type="selection" width="55" />
                    <el-table-column prop="userName" label="用户名" width="120" />
                    <el-table-column prop="realName" label="真实姓名" width="120" />
                    <el-table-column prop="department" label="部门" width="150" />
                    <el-table-column prop="position" label="职位" width="120" />
                    <el-table-column prop="assignTime" label="分配时间" width="150" />
                    <el-table-column prop="status" label="状态" width="80" align="center">
                      <template slot-scope="scope">
                        <el-tag :type="getUserStatusColor(scope.row.status)" size="mini">
                          {{ getUserStatusText(scope.row.status) }}
                        </el-tag>
                      </template>
                    </el-table-column>
                    <el-table-column label="操作" width="100">
                      <template slot-scope="scope">
                        <el-button
                          type="text"
                          size="mini"
                          @click="handleRemoveUser(scope.row)"
                        >移除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
          
          <div v-else class="empty-state">
            <el-empty description="请选择左侧角色查看权限配置" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 新增/编辑角色对话框 -->
    <el-dialog
      :title="roleDialogTitle"
      :visible.sync="roleDialogVisible"
      width="500px"
      :close-on-click-modal="false"
      @close="handleRoleDialogClose"
    >
      <el-form
        ref="roleForm"
        :model="roleForm"
        :rules="roleRules"
        label-width="100px"
        size="small"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="角色状态" prop="status">
          <el-radio-group v-model="roleForm.status">
            <el-radio label="ACTIVE">启用</el-radio>
            <el-radio label="INACTIVE">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色描述" prop="roleDescription">
          <el-input
            v-model="roleForm.roleDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入角色描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitRole" :loading="roleSubmitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 用户分配对话框 -->
    <el-dialog
      title="分配用户"
      :visible.sync="userAssignDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div class="user-assign-content">
        <div class="search-bar">
          <el-input
            v-model="userAssignSearchKeyword"
            placeholder="搜索用户"
            size="small"
            style="width: 300px;"
            @input="handleUserAssignSearch"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
        </div>
        <el-table
          :data="availableUsers"
          border
          size="mini"
          max-height="400"
          @selection-change="handleAvailableUserSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="userName" label="用户名" width="120" />
          <el-table-column prop="realName" label="真实姓名" width="120" />
          <el-table-column prop="department" label="部门" width="150" />
          <el-table-column prop="position" label="职位" width="120" />
          <el-table-column prop="email" label="邮箱" />
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="userAssignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmAssignUsers" :loading="assignLoading">确定分配</el-button>
      </div>
    </el-dialog>

    <!-- 导入权限对话框 -->
    <el-dialog
      title="导入权限配置"
      :visible.sync="importDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-upload
        ref="importUpload"
        class="upload-area"
        drag
        action=""
        :auto-upload="false"
        :limit="1"
        accept=".xlsx,.xls,.csv"
        :on-change="handleImportFileChange"
        :on-exceed="() => $message.warning('只能上传一个文件')"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">支持 .xlsx、.xls、.csv 格式文件</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmImport" :loading="importLoading">确认导入</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog
      title="权限配置帮助"
      :visible.sync="helpDialogVisible"
      width="600px"
    >
      <div class="help-content">
        <h4>功能说明</h4>
        <p>权限配置模块用于管理预算系统的用户权限、角色权限、数据权限和功能权限。</p>
        <h4>操作指南</h4>
        <el-collapse>
          <el-collapse-item title="1. 角色管理" name="1">
            <p>点击「新建角色」创建角色，在左侧角色列表中选择角色后，可在右侧配置该角色的功能权限、数据权限、字段权限和用户分配。</p>
          </el-collapse-item>
          <el-collapse-item title="2. 功能权限" name="2">
            <p>通过勾选权限树中的节点来分配功能权限。支持展开全部、收起全部、全选和取消全选操作。</p>
          </el-collapse-item>
          <el-collapse-item title="3. 数据权限" name="3">
            <p>设置角色的数据访问范围，包括数据范围（全部/本部门/自定义）、预算期间、预算类型和操作权限。</p>
          </el-collapse-item>
          <el-collapse-item title="4. 字段权限" name="4">
            <p>控制角色对各字段的查看和编辑权限，支持敏感字段标记和脱敏规则配置。</p>
          </el-collapse-item>
          <el-collapse-item title="5. 用户分配" name="5">
            <p>将用户分配到角色中，支持搜索、批量分配和批量移除操作。</p>
          </el-collapse-item>
          <el-collapse-item title="6. 导入/导出" name="6">
            <p>支持将权限配置导出为 Excel 文件，也可通过导入功能批量配置权限。</p>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="helpDialogVisible = false">我知道了</el-button>
      </div>
    </el-dialog>

    <!-- 用户权限查看对话框 -->
    <el-dialog
      title="用户权限查看"
      :visible.sync="userPermissionDialogVisible"
      width="800px"
    >
      <el-alert title="请先在左侧选择一个角色，然后切换到「用户分配」标签页查看和管理该角色下的用户。" type="info" :closable="false" show-icon v-if="!selectedRole" />
      <div v-else>
        <p>当前角色：<el-tag>{{ selectedRole.roleName }}</el-tag></p>
        <el-table :data="assignedUsers" border size="mini" max-height="400">
          <el-table-column prop="userName" label="用户名" width="120" />
          <el-table-column prop="realName" label="真实姓名" width="120" />
          <el-table-column prop="department" label="部门" width="150" />
          <el-table-column prop="position" label="职位" width="120" />
          <el-table-column prop="status" label="状态" width="80" align="center">
            <template slot-scope="scope">
              <el-tag :type="getUserStatusColor(scope.row.status)" size="mini">
                {{ getUserStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="userPermissionDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 数据权限查看对话框 -->
    <el-dialog
      title="数据权限配置"
      :visible.sync="dataPermissionDialogVisible"
      width="700px"
    >
      <el-alert title="请先在左侧选择一个角色，然后切换到「数据权限」标签页进行配置。" type="info" :closable="false" show-icon v-if="!selectedRole" />
      <div v-else>
        <p>当前角色：<el-tag>{{ selectedRole.roleName }}</el-tag></p>
        <el-form :model="dataPermissionForm" label-width="120px" size="small">
          <el-form-item label="数据范围">
            <el-radio-group v-model="dataPermissionForm.dataScope">
              <el-radio label="ALL">全部数据</el-radio>
              <el-radio label="DEPT">本部门数据</el-radio>
              <el-radio label="DEPT_AND_CHILD">本部门及子部门</el-radio>
              <el-radio label="SELF">仅本人数据</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="预算期间">
            <span>{{ (dataPermissionForm.budgetPeriods || []).join('、') || '未配置' }}</span>
          </el-form-item>
          <el-form-item label="预算类型">
            <span>{{ (dataPermissionForm.budgetTypes || []).join('、') || '未配置' }}</span>
          </el-form-item>
          <el-form-item label="操作权限">
            <span>{{ (dataPermissionForm.operations || []).join('、') || '未配置' }}</span>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dataPermissionDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="dataPermissionDialogVisible = false; permissionActiveTab = 'data'" v-if="selectedRole">去配置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetSystemApi } from '@/api/managementAccountant/ncv65/budgetSystem'

export default {
  name: 'PermissionConfiguration',
  data() {
    return {
      // 统计数据
      permissionStats: {
        totalRoles: 0,
        totalUsers: 0,
        totalPermissions: 0,
        dataPermissions: 0
      },
      
      // 角色列表
      rolesList: [],
      selectedRole: null,
      
      // 权限配置
      permissionActiveTab: 'function',
      functionPermissions: [],
      permissionTreeProps: {
        children: 'children',
        label: 'name'
      },
      checkedPermissions: [],
      
      // 数据权限
      dataPermissionForm: {
        dataScope: 'DEPT',
        budgetPeriods: [],
        budgetTypes: [],
        operations: []
      },
      organizationTree: [],
      orgTreeProps: {
        children: 'children',
        label: 'name'
      },
      checkedOrganizations: [],
      
      // 字段权限
      fieldPermissions: [],
      
      // 用户分配
      assignedUsers: [],
      selectedUsers: [],
      userSearchKeyword: '',
      
      // 角色对话框
      roleDialogVisible: false,
      roleDialogTitle: '',
      roleSubmitLoading: false,
      roleForm: {
        roleName: '',
        roleCode: '',
        status: 'ACTIVE',
        roleDescription: ''
      },
      roleRules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        roleCode: [
          { required: true, message: '请输入角色编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' }
        ]
      },
      
      // 用户分配对话框
      userAssignDialogVisible: false,
      availableUsers: [],
      selectedAvailableUsers: [],
      userAssignSearchKeyword: '',
      assignLoading: false,

      // 导入权限对话框
      importDialogVisible: false,
      importLoading: false,
      importFile: null,

      // 帮助对话框
      helpDialogVisible: false,

      // 用户权限查看对话框
      userPermissionDialogVisible: false,

      // 数据权限查看对话框
      dataPermissionDialogVisible: false
    }
  },
  
  created() {
    this.getRolesList()
    this.getPermissionStats()
    this.getFunctionPermissions()
    this.getOrganizationTree()
  },
  
  methods: {
    // 获取角色列表
    async getRolesList() {
      try {
        const response = await budgetSystemApi.getRolesList()
        if (response.code === 1 && response.data) {
          this.rolesList = response.data.tlist || response.data || []
        }
      } catch (error) {
        this.$message.error('获取角色列表失败：' + error.message)
      }
    },

    // 获取统计数据
    async getPermissionStats() {
      try {
        const response = await budgetSystemApi.getPermissionStats()
        if (response.code === 1 && response.data) {
          this.permissionStats = response.data
        }
      } catch (error) {
        this.$message.error('获取统计数据失败')
      }
    },

    // 获取功能权限树
    async getFunctionPermissions() {
      try {
        const response = await budgetSystemApi.getFunctionPermissions()
        if (response.code === 1) {
          this.functionPermissions = response.data || []
        }
      } catch (error) {
        this.$message.error('获取功能权限失败')
      }
    },

    // 获取组织树
    async getOrganizationTree() {
      try {
        const response = await budgetSystemApi.getOrganizationTree()
        if (response.code === 1) {
          this.organizationTree = response.data || []
        }
      } catch (error) {
        this.$message.error('获取组织树失败')
      }
    },

    // 选择角色
    async handleSelectRole(role) {
      this.selectedRole = role
      this.permissionActiveTab = 'function'
      await this.getRolePermissions(role.permissionId)
      await this.getRoleDataPermissions(role.permissionId)
      await this.getRoleFieldPermissions(role.permissionId)
      await this.getRoleUsers(role.permissionId)
    },

    // 获取角色权限
    async getRolePermissions(roleId) {
      try {
        const response = await budgetSystemApi.getRolePermissions(roleId)
        if (response.code === 1) {
          this.checkedPermissions = response.data || []
        }
      } catch (error) {
        this.$message.error('获取角色权限失败')
      }
    },

    // 获取角色数据权限
    async getRoleDataPermissions(roleId) {
      try {
        const response = await budgetSystemApi.getRoleDataPermissions(roleId)
        if (response.code === 1 && response.data) {
          const data = response.data
          this.dataPermissionForm = {
            dataScope: data.dataScope || 'DEPT',
            budgetPeriods: Array.isArray(data.budgetPeriods) ? data.budgetPeriods : [],
            budgetTypes: Array.isArray(data.budgetTypes) ? data.budgetTypes : [],
            operations: Array.isArray(data.operations) ? data.operations : []
          }
          this.checkedOrganizations = Array.isArray(data.organizations) ? data.organizations : []
        }
      } catch (error) {
        this.$message.error('获取角色数据权限失败')
      }
    },

    // 获取角色字段权限
    async getRoleFieldPermissions(roleId) {
      try {
        const response = await budgetSystemApi.getRoleFieldPermissions(roleId)
        if (response.code === 1) {
          this.fieldPermissions = response.data || []
        }
      } catch (error) {
        this.$message.error('获取角色字段权限失败')
      }
    },

    // 获取角色用户
    async getRoleUsers(roleId) {
      try {
        const response = await budgetSystemApi.getRoleUsers(roleId)
        if (response.code === 1) {
          this.assignedUsers = response.data || []
        }
      } catch (error) {
        this.$message.error('获取角色用户失败')
      }
    },
    
    // 新增角色
    handleAddRole() {
      this.roleDialogTitle = '新建角色'
      this.roleDialogVisible = true
      this.resetRoleForm()
    },
    
    // 角色操作
    handleRoleAction(command, role) {
      switch (command) {
        case 'edit':
          this.handleEditRole(role)
          break
        case 'copy':
          this.handleCopyRole(role)
          break
        case 'users':
          this.handleRoleUsers(role)
          break
        case 'delete':
          this.handleDeleteRole(role)
          break
      }
    },
    
    // 编辑角色
    handleEditRole(role) {
      this.roleDialogTitle = '编辑角色'
      this.roleDialogVisible = true
      this.roleForm = { ...role }
    },
    
    // 复制角色
    async handleCopyRole(role) {
      try {
        await budgetSystemApi.copyRole(role.permissionId)
        this.$message.success('复制成功')
        this.getRolesList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 角色用户管理
    handleRoleUsers(role) {
      this.selectedRole = role
      this.permissionActiveTab = 'users'
      this.getRoleUsers(role.permissionId)
    },

    // 删除角色
    handleDeleteRole(role) {
      this.$confirm('确定删除该角色吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await budgetSystemApi.deleteRole(role.permissionId)
          this.$message.success('删除成功')
          this.getRolesList()
          if (this.selectedRole && this.selectedRole.permissionId === role.permissionId) {
            this.selectedRole = null
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },
    
    // 提交角色
    async handleSubmitRole() {
      this.$refs.roleForm.validate(async (valid) => {
        if (valid) {
          this.roleSubmitLoading = true
          try {
            // 映射前端字段到后端 BudgetPermission 实体字段
            const payload = {
              permissionName: this.roleForm.roleName,
              permissionCode: this.roleForm.roleCode,
              isEnabled: this.roleForm.status === 'ACTIVE' ? 1 : 0,
              description: this.roleForm.roleDescription,
              permissionType: 'ROLE'
            }
            if (this.roleForm.permissionId) {
              payload.permissionId = this.roleForm.permissionId
              await budgetSystemApi.updateRole(payload)
              this.$message.success('更新成功')
            } else {
              await budgetSystemApi.createRole(payload)
              this.$message.success('创建成功')
            }
            this.roleDialogVisible = false
            this.getRolesList()
            this.getPermissionStats()
          } catch (error) {
            this.$message.error('操作失败：' + error.message)
          } finally {
            this.roleSubmitLoading = false
          }
        }
      })
    },
    
    // 重置角色表单
    resetRoleForm() {
      this.roleForm = {
        roleName: '',
        roleCode: '',
        status: 'ACTIVE',
        roleDescription: ''
      }
      this.$nextTick(() => {
        this.$refs.roleForm && this.$refs.roleForm.clearValidate()
      })
    },
    
    // 角色对话框关闭
    handleRoleDialogClose() {
      this.resetRoleForm()
    },
    
    // 权限树操作
    expandAllPermissions() {
      const tree = this.$refs.permissionTree
      if (!tree) return
      const nodesMap = tree.store.nodesMap
      Object.keys(nodesMap).forEach(key => {
        nodesMap[key].expanded = true
      })
    },

    collapseAllPermissions() {
      const tree = this.$refs.permissionTree
      if (!tree) return
      const nodesMap = tree.store.nodesMap
      Object.keys(nodesMap).forEach(key => {
        nodesMap[key].expanded = false
      })
    },
    
    checkAllPermissions() {
      this.$refs.permissionTree.setCheckedNodes(this.getAllPermissionNodes())
    },
    
    uncheckAllPermissions() {
      this.$refs.permissionTree.setCheckedKeys([])
    },
    
    // 获取所有权限节点
    getAllPermissionNodes() {
      const nodes = []
      const traverse = (data) => {
        data.forEach(item => {
          nodes.push(item)
          if (item.children) {
            traverse(item.children)
          }
        })
      }
      traverse(this.functionPermissions)
      return nodes
    },
    
    // 权限选择变化
    handlePermissionCheck(data, checked) {
      this.checkedPermissions = this.$refs.permissionTree.getCheckedKeys()
    },
    
    // 组织权限选择变化
    handleOrgPermissionCheck(data, checked) {
      this.checkedOrganizations = this.$refs.orgPermissionTree.getCheckedKeys()
    },
    
    // 字段权限变化
    handleFieldPermissionChange(field) {
      // 如果不能查看，则不能编辑
      if (!field.canView) {
        field.canEdit = false
      }
    },
    
    // 保存权限
    async handleSavePermissions() {
      try {
        const permissionData = {
          roleId: this.selectedRole.permissionId,
          functionPermissions: this.checkedPermissions,
          dataPermissions: {
            ...this.dataPermissionForm,
            organizations: this.checkedOrganizations
          },
          fieldPermissions: this.fieldPermissions
        }
        await budgetSystemApi.saveRolePermissions(permissionData)
        this.$message.success('保存成功')
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },
    
    // 重置权限
    handleResetPermissions() {
      this.getRolePermissions(this.selectedRole.permissionId)
      this.getRoleDataPermissions(this.selectedRole.permissionId)
      this.getRoleFieldPermissions(this.selectedRole.permissionId)
    },
    
    // 分配用户
    async handleAssignUsers() {
      this.userAssignDialogVisible = true
      await this.getAvailableUsers()
    },
    
    // 获取可分配用户
    async getAvailableUsers() {
      try {
        const response = await budgetSystemApi.getAvailableUsers(this.selectedRole.permissionId)
        if (response.code === 1) {
          this.availableUsers = response.data || []
        }
      } catch (error) {
        this.$message.error('获取可分配用户失败')
      }
    },
    
    // 确认分配用户
    async handleConfirmAssignUsers() {
      if (this.selectedAvailableUsers.length === 0) {
        this.$message.warning('请选择要分配的用户')
        return
      }
      
      this.assignLoading = true
      try {
        const userIds = this.selectedAvailableUsers.map(user => user.permissionId || user.id)
        await budgetSystemApi.assignUsersToRole(this.selectedRole.permissionId, userIds)
        this.$message.success('分配成功')
        this.userAssignDialogVisible = false
        this.getRoleUsers(this.selectedRole.permissionId)
      } catch (error) {
        this.$message.error('分配失败：' + error.message)
      } finally {
        this.assignLoading = false
      }
    },
    
    // 移除用户
    async handleRemoveUser(user) {
      this.$confirm('确定移除该用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await budgetSystemApi.removeUserFromRole(this.selectedRole.permissionId, user.permissionId || user.id)
          this.$message.success('移除成功')
          this.getRoleUsers(this.selectedRole.permissionId)
        } catch (error) {
          this.$message.error('移除失败：' + error.message)
        }
      })
    },
    
    // 批量移除用户
    async handleBatchRemoveUsers() {
      if (this.selectedUsers.length === 0) {
        this.$message.warning('请选择要移除的用户')
        return
      }
      
      this.$confirm('确定移除选中的用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const userIds = this.selectedUsers.map(user => user.permissionId || user.id)
          await budgetSystemApi.batchRemoveUsersFromRole(this.selectedRole.permissionId, userIds)
          this.$message.success('移除成功')
          this.getRoleUsers(this.selectedRole.permissionId)
        } catch (error) {
          this.$message.error('移除失败：' + error.message)
        }
      })
    },
    
    // 用户选择变化
    handleUserSelectionChange(selection) {
      this.selectedUsers = selection
    },
    
    // 可分配用户选择变化
    handleAvailableUserSelectionChange(selection) {
      this.selectedAvailableUsers = selection
    },
    
    // 用户搜索
    handleUserSearch() {
      if (this.selectedRole) {
        this.getRoleUsers(this.selectedRole.permissionId)
      }
    },

    // 用户分配搜索
    handleUserAssignSearch() {
      if (this.selectedRole) {
        this.getAvailableUsers()
      }
    },

    // 刷新
    handleRefresh() {
      this.getRolesList()
      this.getPermissionStats()
      this.getFunctionPermissions()
      if (this.selectedRole) {
        this.handleSelectRole(this.selectedRole)
      }
      this.$message.success('刷新成功')
    },

    // 导出权限
    async handleExport() {
      try {
        const response = await budgetSystemApi.exportPermissions()
        if (response) {
          const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = '权限配置.xlsx'
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 导入权限
    handleImport() {
      this.importDialogVisible = true
      this.importFile = null
      this.$nextTick(() => {
        this.$refs.importUpload && this.$refs.importUpload.clearFiles()
      })
    },

    // 导入文件变化
    handleImportFileChange(file) {
      this.importFile = file.raw
    },

    // 确认导入
    async handleConfirmImport() {
      if (!this.importFile) {
        this.$message.warning('请先选择要导入的文件')
        return
      }
      this.importLoading = true
      try {
        const formData = new FormData()
        formData.append('file', this.importFile)
        await budgetSystemApi.importPermissions(formData)
        this.$message.success('导入成功')
        this.importDialogVisible = false
        this.getRolesList()
        this.getPermissionStats()
      } catch (error) {
        this.$message.error('导入失败：' + error.message)
      } finally {
        this.importLoading = false
      }
    },

    // 用户权限
    handleUserPermission() {
      if (this.selectedRole) {
        this.permissionActiveTab = 'users'
      } else {
        this.userPermissionDialogVisible = true
      }
    },

    // 数据权限
    handleDataPermission() {
      if (this.selectedRole) {
        this.permissionActiveTab = 'data'
      } else {
        this.dataPermissionDialogVisible = true
      }
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },
    
    // 刷新角色
    refreshRoles() {
      this.getRolesList()
    },
    
    // 获取角色状态颜色
    getRoleStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'warning'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取角色状态文本
    getRoleStatusText(status) {
      const textMap = {
        'ACTIVE': '启用',
        'INACTIVE': '停用'
      }
      return textMap[status] || status
    },
    
    // 获取权限图标
    getPermissionIcon(permission) {
      const iconMap = {
        'MENU': 'el-icon-menu',
        'BUTTON': 'el-icon-circle-check',
        'API': 'el-icon-link'
      }
      return iconMap[permission.type] || 'el-icon-folder'
    },
    
    // 获取字段类型颜色
    getFieldTypeColor(type) {
      const colorMap = {
        'STRING': 'primary',
        'NUMBER': 'success',
        'DATE': 'warning',
        'BOOLEAN': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取字段类型文本
    getFieldTypeText(type) {
      const textMap = {
        'STRING': '字符串',
        'NUMBER': '数字',
        'DATE': '日期',
        'BOOLEAN': '布尔值'
      }
      return textMap[type] || type
    },
    
    // 获取用户状态颜色
    getUserStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'LOCKED': 'danger'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取用户状态文本
    getUserStatusText(status) {
      const textMap = {
        'ACTIVE': '正常',
        'INACTIVE': '停用',
        'LOCKED': '锁定'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.permission-configuration {
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
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;

      &.stat-roles {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }

      &.stat-users {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.stat-permissions {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.stat-data-permissions {
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
    }
  }

  .roles-card,
  .permissions-card {
    height: 600px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        display: flex;
        align-items: center;
        gap: 8px;
      }
    }
  }

  .roles-card {
    .roles-list {
      height: calc(100% - 60px);
      overflow-y: auto;

      .role-item {
        display: flex;
        align-items: center;
        padding: 16px;
        margin-bottom: 12px;
        border: 1px solid #EBEEF5;
        border-radius: 6px;
        cursor: pointer;
        transition: all 0.3s ease;

        &:hover {
          border-color: #409EFF;
          box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
        }

        &.active {
          border-color: #409EFF;
          background: #F0F8FF;
          box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
        }

        .role-info {
          flex: 1;

          .role-name {
            font-size: 16px;
            font-weight: 500;
            color: #303133;
            margin-bottom: 4px;
          }

          .role-description {
            font-size: 12px;
            color: #606266;
            margin-bottom: 8px;
            line-height: 1.4;
          }

          .role-stats {
            display: flex;
            gap: 16px;
            font-size: 11px;
            color: #909399;

            .user-count,
            .permission-count {
              background: #F5F7FA;
              padding: 2px 8px;
              border-radius: 10px;
            }
          }
        }

        .role-status {
          margin: 0 12px;
        }

        .role-actions {
          margin-left: 8px;
        }
      }
    }
  }

  .permissions-card {
    .permission-content {
      height: calc(100% - 60px);
      overflow-y: auto;

      .permission-tree-container {
        .tree-toolbar {
          margin-bottom: 16px;
          padding: 12px;
          background: #F5F7FA;
          border-radius: 4px;

          .el-button {
            margin-right: 8px;
          }
        }

        .permission-node {
          display: flex;
          align-items: center;
          flex: 1;
          font-size: 14px;

          .node-icon {
            margin-right: 8px;
            color: #409EFF;
          }

          .node-label {
            margin-right: 12px;
            font-weight: 500;
          }

          .node-description {
            font-size: 12px;
            color: #909399;
          }
        }
      }

      .data-permission-container,
      .field-permission-container,
      .user-assignment-container {
        padding: 16px;
      }

      .assignment-toolbar {
        margin-bottom: 16px;
        display: flex;
        align-items: center;

        .el-button {
          margin-right: 8px;
        }
      }
    }

    .empty-state {
      height: calc(100% - 60px);
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .user-assign-content {
    .search-bar {
      margin-bottom: 16px;
    }
  }

  .text-right {
    text-align: right;
  }

  .help-content {
    h4 {
      color: #303133;
      font-size: 16px;
      margin: 16px 0 8px 0;

      &:first-child {
        margin-top: 0;
      }
    }

    p {
      color: #606266;
      font-size: 14px;
      line-height: 1.6;
      margin: 0 0 12px 0;
    }
  }

  .upload-area {
    text-align: center;
  }
}
</style>
