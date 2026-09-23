<template>
  <div class="budget-subject-container">
    <!-- 头部操作区 -->
    <div class="header-section">
      <el-card shadow="never">
        <div class="header-content">
          <div class="title-section">
            <h2>预算科目管理</h2>
            <p>管理预算体系中的科目结构和层级关系</p>
          </div>
          <div class="action-section">
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
              新增科目
            </el-button>
            <el-button icon="el-icon-upload2" @click="handleImport">
              导入科目
            </el-button>
            <el-button icon="el-icon-download" @click="handleExport">
              导出科目
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 查询条件 -->
    <div class="search-section">
      <el-card shadow="never">
        <el-form :model="queryParams" ref="queryForm" inline>
          <el-form-item label="科目名称" prop="subjectName">
            <el-input
              v-model="queryParams.subjectName"
              placeholder="请输入科目名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="科目编码" prop="subjectCode">
            <el-input
              v-model="queryParams.subjectCode"
              placeholder="请输入科目编码"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="科目类型" prop="subjectType">
            <el-select
              v-model="queryParams.subjectType"
              placeholder="请选择科目类型"
              clearable
              style="width: 200px"
            >
              <el-option label="收入类" value="INCOME" />
              <el-option label="支出类" value="EXPENSE" />
              <el-option label="资产类" value="ASSET" />
              <el-option label="负债类" value="LIABILITY" />
            </el-select>
          </el-form-item>
          <el-form-item label="预算体系" prop="systemId">
            <el-select
              v-model="queryParams.systemId"
              placeholder="请选择预算体系"
              clearable
              style="width: 200px"
            >
              <el-option
                v-for="system in budgetSystems"
                :key="system.systemId"
                :label="system.systemName"
                :value="system.systemId"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">
              查询
            </el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 主要内容区 -->
    <div class="main-section">
      <el-card shadow="never">
        <div class="content-tabs">
          <el-tabs v-model="activeTab" @tab-click="handleTabClick">
            <el-tab-pane label="树形结构" name="tree">
              <div class="tree-section">
                <div class="tree-toolbar">
                  <el-button size="small" @click="expandAll">展开全部</el-button>
                  <el-button size="small" @click="collapseAll">收起全部</el-button>
                  <el-button size="small" @click="refreshTree">刷新</el-button>
                </div>
                <el-tree
                  ref="subjectTree"
                  :data="treeData"
                  :props="treeProps"
                  :expand-on-click-node="false"
                  :default-expand-all="false"
                  node-key="id"
                  draggable
                  @node-drop="handleNodeDrop"
                  @node-click="handleNodeClick"
                >
                  <span class="custom-tree-node" slot-scope="{ node, data }">
                    <span class="node-label">
                      <i :class="getNodeIcon(data)" style="margin-right: 5px;"></i>
                      <span>{{ node.label }}</span>
                      <el-tag v-if="data.type" size="mini" style="margin-left: 8px;">
                        {{ getTypeLabel(data.type) }}
                      </el-tag>
                      <el-tag v-if="!data.isEnabled" type="danger" size="mini" style="margin-left: 5px;">
                        已禁用
                      </el-tag>
                    </span>
                    <span class="node-actions">
                      <el-button
                        type="text"
                        size="mini"
                        @click.stop="handleAdd(data)"
                      >
                        添加
                      </el-button>
                      <el-button
                        type="text"
                        size="mini"
                        @click.stop="handleEdit(data)"
                      >
                        编辑
                      </el-button>
                      <el-button
                        type="text"
                        size="mini"
                        @click.stop="handleCopy(data)"
                      >
                        复制
                      </el-button>
                      <el-dropdown @command="handleMoreAction" trigger="click">
                        <el-button type="text" size="mini" @click.stop>
                          更多<i class="el-icon-arrow-down el-icon--right"></i>
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                          <el-dropdown-item :command="{action: 'enable', data: data}" v-if="!data.isEnabled">
                            启用
                          </el-dropdown-item>
                          <el-dropdown-item :command="{action: 'disable', data: data}" v-if="data.isEnabled">
                            禁用
                          </el-dropdown-item>
                          <el-dropdown-item :command="{action: 'move', data: data}">
                            移动
                          </el-dropdown-item>
                          <el-dropdown-item :command="{action: 'delete', data: data}" divided>
                            删除
                          </el-dropdown-item>
                        </el-dropdown-menu>
                      </el-dropdown>
                    </span>
                  </span>
                </el-tree>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="列表视图" name="list">
              <div class="list-section">
                <div class="list-toolbar">
                  <el-button
                    type="primary"
                    size="small"
                    icon="el-icon-plus"
                    @click="handleAdd"
                  >
                    新增
                  </el-button>
                  <el-button
                    type="danger"
                    size="small"
                    icon="el-icon-delete"
                    :disabled="!multipleSelection.length"
                    @click="handleBatchDelete"
                  >
                    批量删除
                  </el-button>
                  <el-button
                    size="small"
                    icon="el-icon-setting"
                    @click="handleBatchOperation"
                  >
                    批量操作
                  </el-button>
                </div>
                
                <el-table
                  ref="subjectTable"
                  :data="tableData"
                  v-loading="loading"
                  @selection-change="handleSelectionChange"
                  row-key="subjectId"
                  default-expand-all
                  :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
                >
                  <el-table-column type="selection" width="55" />
                  <el-table-column prop="subjectCode" label="科目编码" width="150" />
                  <el-table-column prop="subjectName" label="科目名称" min-width="200" />
                  <el-table-column prop="subjectType" label="科目类型" width="100">
                    <template slot-scope="scope">
                      <el-tag size="small">{{ getTypeLabel(scope.row.subjectType) }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="subjectLevel" label="层级" width="80" />
                  <el-table-column prop="isEnabled" label="状态" width="80">
                    <template slot-scope="scope">
                      <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'" size="small">
                        {{ scope.row.isEnabled ? '启用' : '禁用' }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="isLeaf" label="叶子节点" width="100">
                    <template slot-scope="scope">
                      <el-tag :type="scope.row.isLeaf ? 'success' : 'info'" size="small">
                        {{ scope.row.isLeaf ? '是' : '否' }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="createdTime" label="创建时间" width="160" />
                  <el-table-column label="操作" width="200" fixed="right">
                    <template slot-scope="scope">
                      <el-button type="text" size="small" @click="handleEdit(scope.row)">
                        编辑
                      </el-button>
                      <el-button type="text" size="small" @click="handleCopy(scope.row)">
                        复制
                      </el-button>
                      <el-button
                        type="text"
                        size="small"
                        @click="scope.row.isEnabled ? handleDisable(scope.row) : handleEnable(scope.row)"
                      >
                        {{ scope.row.isEnabled ? '禁用' : '启用' }}
                      </el-button>
                      <el-button
                        type="text"
                        size="small"
                        style="color: #f56c6c"
                        @click="handleDelete(scope.row)"
                      >
                        删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
                
                <pagination
                  v-show="total > 0"
                  :total="total"
                  :page.sync="queryParams.current"
                  :limit.sync="queryParams.size"
                  @pagination="getList"
                />
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-card>
    </div>

    <!-- 科目表单对话框 -->
    <BudgetSubjectForm
      ref="subjectForm"
      @success="handleFormSuccess"
    />

    <!-- 科目移动对话框 -->
    <BudgetSubjectMove
      ref="subjectMove"
      @success="handleMoveSuccess"
    />

    <!-- 批量操作对话框 -->
    <BudgetSubjectBatch
      ref="subjectBatch"
      @success="handleBatchSuccess"
    />
  </div>
</template>

<script>
import {
  queryBudgetSubjectPage,
  getBudgetSubjectTree,
  deleteBudgetSubject,
  batchDeleteBudgetSubjects,
  enableBudgetSubject,
  disableBudgetSubject,
  moveBudgetSubject,
  getBudgetSubjectStatistics
} from '@/api/managementAccountant/eps/budgetSubject'
import { getBudgetSystemPage } from '@/api/managementAccountant/eps/budgetSystem'
import BudgetSubjectForm from './components/BudgetSubjectForm'
import BudgetSubjectMove from './components/BudgetSubjectMove'
import BudgetSubjectBatch from './components/BudgetSubjectBatch'
import Pagination from '@/components/Pagination'

export default {
  name: 'BudgetSubject',
  components: {
    BudgetSubjectForm,
    BudgetSubjectMove,
    BudgetSubjectBatch,
    Pagination
  },
  data() {
    return {
      loading: false,
      activeTab: 'tree',
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        subjectName: '',
        subjectCode: '',
        subjectType: '',
        systemId: null,
        parentSubjectId: null
      },
      // 表格数据
      tableData: [],
      total: 0,
      multipleSelection: [],
      // 树形数据
      treeData: [],
      treeProps: {
        children: 'children',
        label: 'label'
      },
      // 预算体系列表
      budgetSystems: [],
      // 统计数据
      statistics: {}
    }
  },
  created() {
    this.getBudgetSystems()
    this.getTreeData()
    this.getList()
    this.getStatistics()
  },
  methods: {
    // 获取预算体系列表
    async getBudgetSystems() {
      try {
        const response = await getBudgetSystemPage({ current: 1, size: 100 })
        if (response.code === 200) {
          this.budgetSystems = response.data.records || []
        }
      } catch (error) {
        console.error('获取预算体系失败:', error)
      }
    },

    // 获取树形数据
    async getTreeData() {
      try {
        this.loading = true
        const response = await getBudgetSubjectTree({
          systemId: this.queryParams.systemId,
          subjectType: this.queryParams.subjectType,
          includeDisabled: false
        })
        if (response.code === 200) {
          this.treeData = response.data || []
        }
      } catch (error) {
        this.$message.error('获取科目树失败')
        console.error(error)
      } finally {
        this.loading = false
      }
    },

    // 获取列表数据
    async getList() {
      try {
        this.loading = true
        const response = await queryBudgetSubjectPage(this.queryParams)
        if (response.code === 200) {
          this.tableData = response.data.records || []
          this.total = response.data.total || 0
        }
      } catch (error) {
        this.$message.error('获取科目列表失败')
        console.error(error)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getStatistics() {
      try {
        const response = await getBudgetSubjectStatistics({
          systemId: this.queryParams.systemId,
          subjectType: this.queryParams.subjectType
        })
        if (response.code === 200) {
          this.statistics = response.data || {}
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },

    // 查询
    handleQuery() {
      this.queryParams.current = 1
      if (this.activeTab === 'tree') {
        this.getTreeData()
      } else {
        this.getList()
      }
      this.getStatistics()
    },

    // 重置查询
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.handleQuery()
    },

    // 切换标签页
    handleTabClick(tab) {
      if (tab.name === 'tree') {
        this.getTreeData()
      } else {
        this.getList()
      }
    },

    // 展开全部
    expandAll() {
      this.$refs.subjectTree.filter()
      this.$nextTick(() => {
        const nodes = this.$refs.subjectTree.store._getAllNodes()
        nodes.forEach(node => {
          node.expanded = true
        })
      })
    },

    // 收起全部
    collapseAll() {
      const nodes = this.$refs.subjectTree.store._getAllNodes()
      nodes.forEach(node => {
        node.expanded = false
      })
    },

    // 刷新树
    refreshTree() {
      this.getTreeData()
    },

    // 节点拖拽
    async handleNodeDrop(draggingNode, dropNode, dropType) {
      try {
        const targetParentId = dropType === 'inner' ? dropNode.data.id : dropNode.parent.data.id
        await moveBudgetSubject(draggingNode.data.id, targetParentId)
        this.$message.success('移动成功')
        this.getTreeData()
      } catch (error) {
        this.$message.error('移动失败')
        console.error(error)
      }
    },

    // 节点点击
    handleNodeClick(data) {
      // 可以在这里处理节点点击事件
      console.log('节点点击:', data)
    },

    // 多选变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 新增
    handleAdd(parentData = null) {
      this.$refs.subjectForm.open('add', null, parentData)
    },

    // 编辑
    handleEdit(data) {
      this.$refs.subjectForm.open('edit', data)
    },

    // 复制
    handleCopy(data) {
      this.$refs.subjectForm.open('copy', data)
    },

    // 删除
    async handleDelete(data) {
      try {
        await this.$confirm('确定要删除该科目吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await deleteBudgetSubject(data.subjectId || data.id)
        this.$message.success('删除成功')
        this.refreshData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
          console.error(error)
        }
      }
    },

    // 批量删除
    async handleBatchDelete() {
      try {
        await this.$confirm(`确定要删除选中的 ${this.multipleSelection.length} 个科目吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const subjectIds = this.multipleSelection.map(item => item.subjectId)
        await batchDeleteBudgetSubjects(subjectIds)
        this.$message.success('批量删除成功')
        this.refreshData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败')
          console.error(error)
        }
      }
    },

    // 启用
    async handleEnable(data) {
      try {
        await enableBudgetSubject(data.subjectId || data.id)
        this.$message.success('启用成功')
        this.refreshData()
      } catch (error) {
        this.$message.error('启用失败')
        console.error(error)
      }
    },

    // 禁用
    async handleDisable(data) {
      try {
        await disableBudgetSubject(data.subjectId || data.id)
        this.$message.success('禁用成功')
        this.refreshData()
      } catch (error) {
        this.$message.error('禁用失败')
        console.error(error)
      }
    },

    // 更多操作
    handleMoreAction(command) {
      const { action, data } = command
      switch (action) {
        case 'enable':
          this.handleEnable(data)
          break
        case 'disable':
          this.handleDisable(data)
          break
        case 'move':
          this.$refs.subjectMove.open(data)
          break
        case 'delete':
          this.handleDelete(data)
          break
      }
    },

    // 导入
    handleImport() {
      // TODO: 实现导入功能
      this.$message.info('导入功能开发中...')
    },

    // 导出
    handleExport() {
      // TODO: 实现导出功能
      this.$message.info('导出功能开发中...')
    },

    // 批量操作
    handleBatchOperation() {
      this.$refs.subjectBatch.open(this.multipleSelection)
    },

    // 表单成功回调
    handleFormSuccess() {
      this.refreshData()
    },

    // 移动成功回调
    handleMoveSuccess() {
      this.refreshData()
    },

    // 批量操作成功回调
    handleBatchSuccess() {
      this.refreshData()
    },

    // 刷新数据
    refreshData() {
      if (this.activeTab === 'tree') {
        this.getTreeData()
      } else {
        this.getList()
      }
      this.getStatistics()
    },

    // 获取节点图标
    getNodeIcon(data) {
      if (data.isLeaf) {
        return 'el-icon-document'
      } else {
        return 'el-icon-folder'
      }
    },

    // 获取类型标签
    getTypeLabel(type) {
      const typeMap = {
        'INCOME': '收入类',
        'EXPENSE': '支出类',
        'ASSET': '资产类',
        'LIABILITY': '负债类'
      }
      return typeMap[type] || type
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-subject-container {
  padding: 20px;
  
  .header-section {
    margin-bottom: 20px;
    
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .title-section {
        h2 {
          margin: 0 0 8px 0;
          color: #303133;
          font-size: 20px;
          font-weight: 600;
        }
        
        p {
          margin: 0;
          color: #909399;
          font-size: 14px;
        }
      }
    }
  }
  
  .search-section {
    margin-bottom: 20px;
  }
  
  .main-section {
    .tree-section {
      .tree-toolbar {
        margin-bottom: 15px;
        padding-bottom: 15px;
        border-bottom: 1px solid #ebeef5;
      }
      
      .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding-right: 8px;
        
        .node-actions {
          .el-button--text {
            padding: 0;
            margin-left: 8px;
          }
        }
      }
    }
    
    .list-section {
      .list-toolbar {
        margin-bottom: 15px;
        padding-bottom: 15px;
        border-bottom: 1px solid #ebeef5;
      }
    }
  }
}
</style>
