<template>
  <div class="dimension-member-container">
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <el-select v-model="selectedDimensionId" placeholder="请选择维度" style="width: 200px" @change="handleDimensionChange">
        <el-option
          v-for="dimension in dimensionList"
          :key="dimension.dimensionId"
          :label="dimension.dimensionName"
          :value="dimension.dimensionId"
        />
      </el-select>
      <el-button type="primary" @click="handleAdd" :disabled="!selectedDimensionId">新增根节点</el-button>
      <el-button type="success" @click="handleAddChild" :disabled="!currentNode">新增子节点</el-button>
      <el-button type="danger" @click="handleDelete" :disabled="!currentNode">删除节点</el-button>
    </div>

    <!-- 树形结构 -->
    <div class="tree-container">
      <el-tree
        ref="tree"
        :data="treeData"
        :props="treeProps"
        node-key="memberId"
        :default-expand-all="false"
        :expand-on-click-node="false"
        :highlight-current="true"
        draggable
        :allow-drop="allowDrop"
        :allow-drag="allowDrag"
        @node-click="handleNodeClick"
        @node-drop="handleNodeDrop"
      >
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span class="node-label">
            <i :class="getNodeIcon(data)" style="margin-right: 5px"></i>
            <span>{{ data.memberName }}</span>
            <el-tag v-if="data.memberType === 'SHARED'" size="mini" type="warning" style="margin-left: 5px">共享</el-tag>
            <el-tag v-if="data.status === 'INACTIVE'" size="mini" type="danger" style="margin-left: 5px">停用</el-tag>
          </span>
          <span class="node-actions">
            <el-button type="text" size="mini" @click.stop="handleView(data)">查看</el-button>
            <el-button type="text" size="mini" @click.stop="handleEdit(data)">编辑</el-button>
            <el-button 
              type="text" 
              size="mini" 
              @click.stop="handleToggleStatus(data)"
            >
              {{ data.status === 'ACTIVE' ? '停用' : '启用' }}
            </el-button>
          </span>
        </span>
      </el-tree>
    </div>

    <!-- 成员表单对话框 -->
    <dimension-member-form
      v-if="formVisible"
      :visible.sync="formVisible"
      :form-type="formType"
      :form-data="formData"
      :dimension-id="selectedDimensionId"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script>
import { getDimensionList } from '@/api/financialSharing/groupControl/dimension'
import { 
  getDimensionMemberTree, 
  deleteDimensionMember, 
  moveDimensionMember,
  updateDimensionMemberStatus 
} from '@/api/financialSharing/groupControl/dimensionMember'
import DimensionMemberForm from './components/DimensionMemberForm.vue'

export default {
  name: 'DimensionMember',
  components: {
    DimensionMemberForm
  },
  data() {
    return {
      loading: false,
      dimensionList: [],
      selectedDimensionId: '',
      treeData: [],
      treeProps: {
        children: 'children',
        label: 'memberName'
      },
      currentNode: null,
      formVisible: false,
      formType: 'add',
      formData: null
    }
  },
  created() {
    this.fetchDimensionList()
  },
  methods: {
    // 查询维度列表
    async fetchDimensionList() {
      try {
        const res = await getDimensionList({ pageNumber: 1, pageSize: 100 })
        if (res.code === 1) {
          this.dimensionList = res.data.records || []
          if (this.dimensionList.length > 0) {
            this.selectedDimensionId = this.dimensionList[0].dimensionId
            this.fetchTreeData()
          }
        }
      } catch (error) {
        console.error('查询维度列表失败:', error)
      }
    },

    // 查询树形数据
    async fetchTreeData() {
      if (!this.selectedDimensionId) {
        return
      }
      this.loading = true
      try {
        const res = await getDimensionMemberTree({ dimensionId: this.selectedDimensionId })
        if (res.code === 1) {
          this.treeData = res.data || []
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        console.error('查询成员树失败:', error)
        this.$message.error('查询失败')
      } finally {
        this.loading = false
      }
    },

    // 维度改变
    handleDimensionChange() {
      this.currentNode = null
      this.fetchTreeData()
    },

    // 节点点击
    handleNodeClick(data, node) {
      this.currentNode = data
    },

    // 新增根节点
    handleAdd() {
      this.formType = 'add'
      this.formData = { parentMemberId: null }
      this.formVisible = true
    },

    // 新增子节点
    handleAddChild() {
      if (!this.currentNode) {
        this.$message.warning('请先选择父节点')
        return
      }
      this.formType = 'add'
      this.formData = { parentMemberId: this.currentNode.memberId }
      this.formVisible = true
    },

    // 查看
    handleView(data) {
      this.formType = 'view'
      this.formData = { ...data }
      this.formVisible = true
    },

    // 编辑
    handleEdit(data) {
      this.formType = 'edit'
      this.formData = { ...data }
      this.formVisible = true
    },

    // 删除
    handleDelete() {
      if (!this.currentNode) {
        this.$message.warning('请先选择要删除的节点')
        return
      }
      this.$confirm('确定要删除该节点及其所有子节点吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteDimensionMember(this.currentNode.memberId)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.currentNode = null
            this.fetchTreeData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除成员失败:', error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },

    // 切换状态
    handleToggleStatus(data) {
      const newStatus = data.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
      const statusText = newStatus === 'ACTIVE' ? '启用' : '停用'
      this.$confirm(`确定要${statusText}该成员吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await updateDimensionMemberStatus(data.memberId, newStatus)
          if (res.code === 1) {
            this.$message.success(`${statusText}成功`)
            this.fetchTreeData()
          } else {
            this.$message.error(res.msg || `${statusText}失败`)
          }
        } catch (error) {
          console.error('更新成员状态失败:', error)
          this.$message.error(`${statusText}失败`)
        }
      }).catch(() => {})
    },

    // 节点拖拽
    async handleNodeDrop(draggingNode, dropNode, dropType) {
      try {
        const targetParentId = dropType === 'inner' ? dropNode.data.memberId : dropNode.data.parentMemberId
        const res = await moveDimensionMember(draggingNode.data.memberId, targetParentId, 0)
        if (res.code === 1) {
          this.$message.success('移动成功')
          this.fetchTreeData()
        } else {
          this.$message.error(res.msg || '移动失败')
          this.fetchTreeData()
        }
      } catch (error) {
        console.error('移动成员失败:', error)
        this.$message.error('移动失败')
        this.fetchTreeData()
      }
    },

    // 允许拖拽
    allowDrag(draggingNode) {
      return draggingNode.data.status === 'ACTIVE'
    },

    // 允许放置
    allowDrop(draggingNode, dropNode, type) {
      return dropNode.data.status === 'ACTIVE'
    },

    // 获取节点图标
    getNodeIcon(data) {
      if (data.isLeaf === 'Y') {
        return 'el-icon-document'
      } else {
        return 'el-icon-folder-opened'
      }
    },

    // 表单提交成功
    handleFormSuccess() {
      this.formVisible = false
      this.fetchTreeData()
    }
  }
}
</script>

<style scoped lang="scss">
.dimension-member-container {
  padding: 20px;

  .toolbar {
    background: #fff;
    padding: 15px 20px;
    margin-bottom: 20px;
    border-radius: 4px;

    .el-button {
      margin-left: 10px;
    }
  }

  .tree-container {
    background: #fff;
    padding: 20px;
    border-radius: 4px;
    min-height: 600px;

    .custom-tree-node {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 14px;
      padding-right: 8px;

      .node-label {
        display: flex;
        align-items: center;
      }

      .node-actions {
        display: none;
      }

      &:hover .node-actions {
        display: inline-block;
      }
    }
  }
}
</style>

