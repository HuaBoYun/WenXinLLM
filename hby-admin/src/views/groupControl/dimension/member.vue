<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <div class="query-form">
      <el-form :model="queryForm" :inline="true" label-width="100px">
        <el-form-item label="所属维度">
          <el-select v-model="queryForm.dimensionId" placeholder="请选择维度" clearable style="width: 250px" @change="handleDimensionChange">
            <el-option
              v-for="item in dimensionList"
              :key="item.dimensionId"
              :label="item.dimensionName"
              :value="item.dimensionId"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <div style="margin-bottom: 10px">
      <el-button type="primary" icon="el-icon-plus" :disabled="!queryForm.dimensionId" @click="handleAdd">新增</el-button>
      <el-button type="danger" icon="el-icon-delete" :disabled="selectedIds.length === 0" @click="handleBatchDelete">批量删除</el-button>
      <el-button type="warning" icon="el-icon-refresh" :disabled="!queryForm.dimensionId" @click="handleRefresh">刷新</el-button>
    </div>

    <!-- 树形结构 -->
    <el-tree
      ref="tree"
      v-loading="loading"
      :data="treeData"
      :props="treeProps"
      node-key="memberId"
      :default-expand-all="false"
      :expand-on-click-node="false"
      :highlight-current="true"
      draggable
      :allow-drop="allowDrop"
      :allow-drag="allowDrag"
      @node-drop="handleDrop"
      @node-click="handleNodeClick"
    >
      <span slot-scope="{ node, data }" class="custom-tree-node">
        <span>
          <i :class="node.isLeaf ? 'el-icon-document' : 'el-icon-folder'" />
          <span style="margin-left: 5px">{{ data.memberName }}</span>
          <el-tag v-if="data.status === 'ACTIVE'" type="success" size="mini" style="margin-left: 10px">启用</el-tag>
          <el-tag v-else type="info" size="mini" style="margin-left: 10px">停用</el-tag>
        </span>
        <span>
          <el-button type="text" size="mini" @click.stop="handleEdit(data)">编辑</el-button>
          <el-button type="text" size="mini" @click.stop="handleAddChild(data)">新增子节点</el-button>
          <el-button type="text" size="mini" style="color: #f56c6c" @click.stop="handleDelete(data)">删除</el-button>
        </span>
      </span>
    </el-tree>

    <!-- 新增/编辑对话框 -->
    <dimension-member-form
      v-if="dialogVisible"
      :visible.sync="dialogVisible"
      :member-id="currentMemberId"
      :dimension-id="queryForm.dimensionId"
      :parent-id="currentParentId"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getDimensionList } from '@/api/groupControl/dimension'
import {
  getDimensionMemberTree,
  deleteDimensionMember,
  batchDeleteDimensionMember,
  moveDimensionMember
} from '@/api/groupControl/dimensionMember'
import DimensionMemberForm from './components/DimensionMemberForm'

export default {
  name: 'DimensionMember',
  components: {
    DimensionMemberForm
  },
  data() {
    return {
      loading: false,
      queryForm: {
        dimensionId: null
      },
      dimensionList: [],
      treeData: [],
      treeProps: {
        children: 'children',
        label: 'memberName'
      },
      selectedIds: [],
      dialogVisible: false,
      currentMemberId: null,
      currentParentId: null
    }
  },
  created() {
    this.loadDimensionList()
  },
  methods: {
    // 加载维度列表
    loadDimensionList() {
      // 强制使用 Mock 数据（后端调试期间）
      console.warn('🔧 开发模式：使用 Mock 数据')
      this.dimensionList = [
        { dimensionId: 1, dimensionName: '公司维度', dimensionCode: 'DIM001' },
        { dimensionId: 2, dimensionName: '部门维度', dimensionCode: 'DIM002' },
        { dimensionId: 3, dimensionName: '产品维度', dimensionCode: 'DIM006' }
      ]

      // 如果需要调用后端接口，取消下面的注释
      /*
      getDimensionList({ pageNumber: 1, pageSize: 1000, status: 'ACTIVE' }).then(response => {
        if (response.code === 1) {
          this.dimensionList = response.data.records || []
        }
      }).catch(() => {
        console.warn('维度列表接口调用失败，使用 Mock 数据')
        this.dimensionList = [
          { dimensionId: 1, dimensionName: '公司维度', dimensionCode: 'DIM001' },
          { dimensionId: 2, dimensionName: '部门维度', dimensionCode: 'DIM002' },
          { dimensionId: 3, dimensionName: '产品维度', dimensionCode: 'DIM006' }
        ]
      })
      */
    },

    // 加载树形数据
    loadTree() {
      if (!this.queryForm.dimensionId) {
        this.$message.warning('请先选择维度')
        return
      }
      this.loading = true

      // 强制使用 Mock 数据（后端调试期间）
      console.warn('🔧 开发模式：使用 Mock 数据')
      this.treeData = [
        {
          memberId: 1,
          memberCode: 'M001',
          memberName: '总公司',
          parentId: null,
          level: 1,
          status: 'ACTIVE',
          children: [
            {
              memberId: 2,
              memberCode: 'M002',
              memberName: '华东分公司',
              parentId: 1,
              level: 2,
              status: 'ACTIVE',
              children: [
                { memberId: 5, memberCode: 'M005', memberName: '上海办事处', parentId: 2, level: 3, status: 'ACTIVE', children: [] },
                { memberId: 6, memberCode: 'M006', memberName: '杭州办事处', parentId: 2, level: 3, status: 'ACTIVE', children: [] }
              ]
            },
            {
              memberId: 3,
              memberCode: 'M003',
              memberName: '华南分公司',
              parentId: 1,
              level: 2,
              status: 'ACTIVE',
              children: [
                { memberId: 7, memberCode: 'M007', memberName: '广州办事处', parentId: 3, level: 3, status: 'ACTIVE', children: [] },
                { memberId: 8, memberCode: 'M008', memberName: '深圳办事处', parentId: 3, level: 3, status: 'ACTIVE', children: [] }
              ]
            },
            {
              memberId: 4,
              memberCode: 'M004',
              memberName: '华北分公司',
              parentId: 1,
              level: 2,
              status: 'ACTIVE',
              children: [
                { memberId: 9, memberCode: 'M009', memberName: '北京办事处', parentId: 4, level: 3, status: 'ACTIVE', children: [] },
                { memberId: 10, memberCode: 'M010', memberName: '天津办事处', parentId: 4, level: 3, status: 'INACTIVE', children: [] }
              ]
            }
          ]
        }
      ]
      this.loading = false

      // 如果需要调用后端接口，取消下面的注释
      /*
      getDimensionMemberTree({ dimensionId: this.queryForm.dimensionId }).then(response => {
        if (response.code === 1) {
          this.treeData = response.data || []
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
      */
    },

    // 维度变化
    handleDimensionChange() {
      this.treeData = []
      if (this.queryForm.dimensionId) {
        this.loadTree()
      }
    },

    // 查询按钮
    handleQuery() {
      this.loadTree()
    },

    // 重置按钮
    handleReset() {
      this.queryForm.dimensionId = null
      this.treeData = []
    },

    // 刷新按钮
    handleRefresh() {
      this.loadTree()
    },

    // 新增按钮
    handleAdd() {
      this.currentMemberId = null
      this.currentParentId = null
      this.dialogVisible = true
    },

    // 新增子节点
    handleAddChild(data) {
      this.currentMemberId = null
      this.currentParentId = data.memberId
      this.dialogVisible = true
    },

    // 编辑按钮
    handleEdit(data) {
      this.currentMemberId = data.memberId
      this.currentParentId = null
      this.dialogVisible = true
    },

    // 删除按钮
    handleDelete(data) {
      this.$confirm('确认删除该成员吗？删除后子节点也将被删除！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDimensionMember({ memberId: data.memberId }).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.loadTree()
          }
        })
      })
    },

    // 批量删除
    handleBatchDelete() {
      if (this.selectedIds.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('确认删除选中的成员吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchDeleteDimensionMember(this.selectedIds).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.loadTree()
          }
        })
      })
    },

    // 节点点击
    handleNodeClick(data) {
      // 可以在这里处理节点点击事件
    },

    // 拖拽判断
    allowDrop(draggingNode, dropNode, type) {
      // 只允许在同一维度内拖拽
      return true
    },

    allowDrag(draggingNode) {
      // 允许拖拽
      return true
    },

    // 拖拽完成
    handleDrop(draggingNode, dropNode, dropType, ev) {
      const params = {
        memberId: draggingNode.data.memberId,
        targetMemberId: dropNode.data.memberId,
        dropType: dropType // 'before', 'after', 'inner'
      }
      moveDimensionMember(params).then(response => {
        if (response.code === 1) {
          this.$message.success('移动成功')
          this.loadTree()
        } else {
          this.$message.error('移动失败')
          this.loadTree() // 刷新恢复原状态
        }
      }).catch(() => {
        this.$message.error('移动失败')
        this.loadTree() // 刷新恢复原状态
      })
    },

    // 操作成功回调
    handleSuccess() {
      this.dialogVisible = false
      this.loadTree()
    }
  }
}
</script>

<style scoped>
.query-form {
  background: #fff;
  padding: 20px;
  margin-bottom: 10px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>

