<template>
  <div class="org-tree-panel dashboard-card">
    <div class="card-header">
      <span class="card-title"><i class="el-icon-share"></i> 权限组织架构</span>
      <span class="org-count">共 {{ totalOrg }} 个组织</span>
    </div>
    <div class="card-body">
      <div v-if="loading" class="loading-wrap">
        <i class="el-icon-loading"></i> 加载中...
      </div>
      <div v-else-if="orgData && orgData.length > 0">
        <el-tree
          :data="orgData"
          :props="treeProps"
          default-expand-all
          highlight-current
          :expand-on-click-node="false"
          class="org-tree"
        >
          <template slot-scope="{ node, data }">
            <span class="tree-node">
              <i
                class="el-icon-office-building node-icon"
                :class="{ 'node-icon--root': !node.parent || !node.parent.data }"
              ></i>
              <span class="node-label" :title="node.label">{{ node.label }}</span>
              <span v-if="data.children && data.children.length" class="child-count">
                {{ data.children.length }}
              </span>
            </span>
          </template>
        </el-tree>
      </div>
      <el-empty v-else description="暂无组织数据" :image-size="60" />
    </div>
    <div class="card-footer">
      <i class="el-icon-info footer-icon"></i>
      <span>仅显示当前用户权限范围内的组织</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OrgTreePanel',
  props: {
    orgData: {
      type: Array,
      default: () => [],
    },
    loading: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      treeProps: {
        label: 'name',
        children: 'children',
      },
    }
  },
  computed: {
    totalOrg() {
      let count = 0
      const countNodes = (nodes) => {
        if (!nodes) return
        nodes.forEach((n) => {
          count++
          if (n.children) countNodes(n.children)
        })
      }
      countNodes(this.orgData)
      return count
    },
  },
}
</script>

<style lang="scss" scoped>
.org-tree-panel {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 16px;
    background: linear-gradient(90deg, #f5f7fa 0%, #ffffff 100%);
    border-bottom: 2px solid #e3f2fd;

    .card-title {
      font-size: 14px;
      font-weight: 600;
      color: #1565c0;
      display: flex;
      align-items: center;
      gap: 6px;
    }

    .org-count {
      font-size: 12px;
      color: #78909c;
      padding: 2px 8px;
      background: #e3f2fd;
      border-radius: 10px;
    }
  }

  .card-body {
    flex: 1;
    padding: 8px;
    overflow: auto;

    .loading-wrap {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 100px;
      color: #78909c;
      gap: 6px;
    }

    .org-tree {
      background: transparent;

      :deep(.el-tree-node__content) {
        height: 32px;
        border-radius: 4px;
        &:hover { background: #e3f2fd; }
      }

      :deep(.el-tree-node.is-current > .el-tree-node__content) {
        background: #bbdefb;
      }
    }

    .tree-node {
      display: flex;
      align-items: center;
      gap: 4px;
      width: 100%;

      .node-icon {
        color: #90a4ae;
        font-size: 14px;
        &--root { color: #1976d2; }
      }

      .node-label {
        flex: 1;
        font-size: 13px;
        color: #37474f;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .child-count {
        font-size: 11px;
        background: #e3f2fd;
        color: #1565c0;
        padding: 0 5px;
        border-radius: 8px;
        flex-shrink: 0;
      }
    }
  }

  .card-footer {
    padding: 8px 14px;
    border-top: 1px solid #f0f4f8;
    font-size: 11px;
    color: #b0bec5;
    display: flex;
    align-items: center;
    gap: 4px;
  }
}
</style>
