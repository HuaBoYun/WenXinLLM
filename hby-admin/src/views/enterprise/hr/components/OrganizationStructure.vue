<template>
  <div class="organization-structure">
    <div class="action-section">
      <el-button type="primary" icon="el-icon-plus" @click="handleAddDepartment">新增部门</el-button>
      <el-button type="success" icon="el-icon-folder-opened" @click="handleExpandAll">展开全部</el-button>
      <el-button type="warning" icon="el-icon-folder" @click="handleCollapseAll">收起全部</el-button>
      <el-button type="info" icon="el-icon-download" @click="handleExport">导出架构图</el-button>
    </div>

    <div class="tree-section">
      <el-tree :data="organizationData" :props="treeProps" :default-expand-all="isExpandAll" :expand-on-click-node="false" node-key="id" ref="orgTree" class="organization-tree">
        <template #default="{ node, data }">
          <div class="tree-node">
            <div class="node-content">
              <div class="node-info">
                <div class="node-title">
                  <i :class="getNodeIcon(data.type)"></i>
                  <span class="node-name">{{ data.name }}</span>
                  <el-tag :type="getNodeTagType(data.type)" size="mini">{{ getNodeTypeText(data.type) }}</el-tag>
                </div>
                <div class="node-details">
                  <span class="detail-item"><i class="el-icon-user"></i>{{ data.employeeCount || 0 }}人</span>
                  <span class="detail-item" v-if="data.manager"><i class="el-icon-user-solid"></i>{{ data.manager }}</span>
                  <span class="detail-item" v-if="data.deptCode"><i class="el-icon-collection-tag"></i>{{ data.deptCode }}</span>
                </div>
              </div>
              <div class="node-actions">
                <el-button type="text" size="mini" @click.stop="handleViewDepartment(data)">查看</el-button>
                <el-button type="text" size="mini" @click.stop="handleEditDepartment(data)">编辑</el-button>
                <el-button type="text" size="mini" @click.stop="handleAddSubDepartment(data)">添加子部门</el-button>
                <el-button type="text" size="mini" style="color: #f56c6c" @click.stop="handleDeleteDepartment(data)" v-if="data.id !== 'dept001'">删除</el-button>
              </div>
            </div>
          </div>
        </template>
      </el-tree>
      <el-empty v-if="organizationData.length === 0 && !loading" description="暂无组织架构数据" />
    </div>

    <!-- 查看/编辑/新增部门对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="formData" ref="formRef" :rules="formRules" label-width="100px" :disabled="dialogMode === 'view'">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="部门名称" prop="deptName"><el-input v-model="formData.deptName" placeholder="请输入部门名称" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="部门编码" prop="deptCode"><el-input v-model="formData.deptCode" placeholder="请输入部门编码" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="上级部门">
              <el-select v-model="formData.parentId" placeholder="请选择上级部门" style="width:100%" clearable @change="handleParentChange" :disabled="dialogMode === 'view'">
                <el-option label="无(顶级部门)" value="0" />
                <el-option v-for="dept in deptFlatList" :key="dept.id" :label="dept.deptName" :value="dept.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="部门层级" prop="deptLevel"><el-select v-model="formData.deptLevel" placeholder="请选择" style="width:100%"><el-option label="集团" :value="1" /><el-option label="部门" :value="2" /><el-option label="小组" :value="3" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="负责人" prop="managerName"><el-input v-model="formData.managerName" placeholder="请输入负责人姓名" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="部门人数" prop="employeeCount"><el-input-number v-model="formData.employeeCount" :min="0" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="状态" prop="status"><el-select v-model="formData.status" placeholder="请选择" style="width:100%"><el-option label="正常" value="正常" /><el-option label="停用" value="停用" /></el-select></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-if="dialogMode !== 'view'">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { organizationApi } from '@/api/enterprise/hr'

export default {
  name: 'OrganizationStructure',
  data() {
    return {
      loading: false,
      submitLoading: false,
      isExpandAll: false,
      organizationData: [],
      treeProps: { children: 'children', label: 'name' },
      rawDeptList: [],
      deptFlatList: [],
      // 对话框
      dialogVisible: false,
      dialogMode: 'add',
      formData: {},
      formRules: { deptName: [{ required: true, message: '请输入部门名称', trigger: 'blur' }], deptCode: [{ required: true, message: '请输入部门编码', trigger: 'blur' }] },
      parentDeptName: '',
      parentDeptId: '0'
    }
  },
  computed: {
    dialogTitle() { return { add: '新增部门', edit: '编辑部门', view: '部门详情' }[this.dialogMode] || '' }
  },
  mounted() { this.loadOrganizationData() },
  methods: {
    async loadOrganizationData() {
      this.loading = true
      try {
        const res = await organizationApi.getDepartmentList({})
        if (res && res.data) {
          this.organizationData = this.convertTreeData(res.data || [])
          // 提取扁平部门列表供下拉选择
          this.deptFlatList = []
          this.flattenDeptTree(res.data || [])
        } else { this.organizationData = []; this.deptFlatList = [] }
      } catch (e) { console.error('加载组织架构数据失败:', e); this.$message.error('加载组织架构数据失败') }
      finally { this.loading = false }
    },
    flattenDeptTree(list) {
      list.forEach(item => {
        this.deptFlatList.push({ id: item.id, deptName: item.deptName || item.name, deptLevel: item.deptLevel || 1 })
        if (item.children && item.children.length > 0) this.flattenDeptTree(item.children)
      })
    },
    convertTreeData(data) {
      return data.map(item => {
        const node = { id: item.id, name: item.deptName || item.name, type: item.deptLevel === 1 ? 'company' : (item.children && item.children.length > 0 ? 'department' : 'team'), employeeCount: item.employeeCount || 0, manager: item.managerName || item.manager || '', deptCode: item.deptCode || '', deptLevel: item.deptLevel || 1, status: item.status || '', parentId: item.parentId || '0' }
        if (item.children && item.children.length > 0) node.children = this.convertTreeData(item.children)
        return node
      })
    },
    handleAddDepartment() { this.dialogMode = 'add'; this.formData = { enterpriseId: 'ent001', enterpriseName: '贵州国资投资集团', parentId: '0', deptLevel: 2, employeeCount: 0, status: '正常' }; this.parentDeptName = '无(顶级部门)'; this.parentDeptId = '0'; this.dialogVisible = true },
    handleAddSubDepartment(data) { this.dialogMode = 'add'; this.formData = { enterpriseId: 'ent001', enterpriseName: '贵州国资投资集团', parentId: data.id, deptLevel: (data.deptLevel || 1) + 1, employeeCount: 0, status: '正常' }; this.parentDeptName = data.name; this.parentDeptId = data.id; this.dialogVisible = true },
    handleParentChange(val) {
      if (!val || val === '0') {
        this.formData.parentId = '0'
        this.formData.deptLevel = 1
      } else {
        const parent = this.deptFlatList.find(d => d.id === val)
        if (parent) { this.formData.deptLevel = (parent.deptLevel || 1) + 1 }
      }
    },
    handleViewDepartment(data) { this.dialogMode = 'view'; this.formData = { ...data }; this.parentDeptName = ''; this.dialogVisible = true },
    handleEditDepartment(data) { this.dialogMode = 'edit'; this.formData = { ...data }; this.parentDeptName = ''; this.dialogVisible = true },
    handleDeleteDepartment(data) {
      this.$confirm('确认删除该部门吗？删除后该部门下的所有子部门也将被删除。', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        try { await organizationApi.deleteDepartment(data.id); this.$message.success('删除成功'); this.loadOrganizationData() } catch (e) { this.$message.error('删除失败') }
      }).catch(() => {})
    },
    handleExpandAll() { this.isExpandAll = true; this.$nextTick(() => { const tree = this.$refs.orgTree; if (tree) { tree.store.setData(this.organizationData) } }) },
    handleCollapseAll() { this.isExpandAll = false; this.$nextTick(() => { const tree = this.$refs.orgTree; if (tree) { const nodes = tree.store._getAllNodes(); nodes.forEach(n => { n.expanded = false }) } }) },
    handleExport() {
      // 导出架构图为文本文件
      let text = '组织架构图\n' + '='.repeat(40) + '\n'
      const printNode = (nodes, indent = '') => { nodes.forEach(n => { text += `${indent}${n.name} (${n.employeeCount || 0}人${n.manager ? '，负责人:' + n.manager : ''})\n`; if (n.children) printNode(n.children, indent + '  ') }) }
      printNode(this.organizationData)
      const blob = new Blob([text], { type: 'text/plain;charset=utf-8' })
      const link = document.createElement('a'); link.href = URL.createObjectURL(blob); link.download = '组织架构图.txt'; link.click(); URL.revokeObjectURL(link.href)
      this.$message.success('导出成功')
    },
    async handleSubmit() {
      try { await this.$refs.formRef.validate() } catch (e) { return }
      this.submitLoading = true
      try {
        const data = { ...this.formData }
        if (this.dialogMode === 'add') { if (!data.parentId) data.parentId = '0'; await organizationApi.createDepartment(data); this.$message.success('新增成功') }
        else { await organizationApi.updateDepartment(data.id, data); this.$message.success('编辑成功') }
        this.dialogVisible = false; this.loadOrganizationData()
      } catch (e) { this.$message.error(this.dialogMode === 'add' ? '新增失败' : '编辑失败') }
      finally { this.submitLoading = false }
    },
    getNodeIcon(type) { return { company: 'el-icon-office-building', department: 'el-icon-folder', team: 'el-icon-user' }[type] || 'el-icon-folder' },
    getNodeTagType(type) { return { company: 'danger', department: 'primary', team: 'success' }[type] || 'info' },
    getNodeTypeText(type) { return { company: '公司', department: '部门', team: '团队' }[type] || '未知' }
  }
}
</script>

<style lang="scss" scoped>
.organization-structure {
  .action-section { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); }
  .tree-section {
    background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    .organization-tree {
      :deep(.el-tree-node__content) { height: auto; padding: 8px 0; }
      .tree-node { width: 100%;
        .node-content { display: flex; justify-content: space-between; align-items: center; width: 100%; padding: 12px; border: 1px solid #e4e7ed; border-radius: 8px; background: #fafafa; margin: 4px 0;
          &:hover { background: #f0f9ff; border-color: #409eff; }
          .node-info { flex: 1;
            .node-title { display: flex; align-items: center; margin-bottom: 8px; i { margin-right: 8px; font-size: 16px; color: #409eff; } .node-name { font-weight: 600; font-size: 16px; margin-right: 12px; } }
            .node-details { display: flex; gap: 16px; .detail-item { display: flex; align-items: center; font-size: 14px; color: #666; i { margin-right: 4px; } } }
          }
          .node-actions { display: flex; gap: 8px; }
        }
      }
    }
  }
}
</style>
