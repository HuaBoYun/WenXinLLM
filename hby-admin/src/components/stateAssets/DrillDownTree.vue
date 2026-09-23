<template>
  <div class="drill-down-tree">
    <div class="tree-panel">
      <div class="tree-header">
        <span class="tree-title">{{ title || '穿透下钻' }}</span>
        <el-button size="mini" icon="el-icon-refresh" circle @click="refreshTree" />
      </div>
      <el-input v-model="filterText" placeholder="搜索企业名称" size="small" prefix-icon="el-icon-search" clearable style="margin-bottom: 8px" />
      <el-tree
        ref="tree"
        :data="treeData"
        :props="treeProps"
        :load="loadNode"
        lazy
        node-key="id"
        highlight-current
        :filter-node-method="filterNode"
        @node-click="handleNodeClick"
      >
        <span slot-scope="{ node, data }" class="tree-node">
          <span class="node-label">{{ data.companyName || data.label }}</span>
          <el-tag v-if="data.warningCount > 0" type="danger" size="mini" class="warning-tag">
            {{ data.warningCount }}
          </el-tag>
        </span>
      </el-tree>
    </div>
    <div class="detail-panel" v-if="selectedNode">
      <slot name="detail" :node="selectedNode"></slot>
    </div>
  </div>
</template>
<script>
export default {
  name: 'DrillDownTree',
  props: {
    title: { type: String, default: '穿透下钻' },
    domainType: { type: String, required: true },
    companyId: { type: String, default: '' },
    maxLevel: { type: Number, default: 5 },
    loadChildren: { type: Function, default: null },
  },
  data() {
    return {
      filterText: '',
      treeData: [],
      selectedNode: null,
      treeProps: { children: 'children', label: 'companyName', isLeaf: 'isLeaf' },
    }
  },
  watch: {
    filterText(val) { this.$refs.tree && this.$refs.tree.filter(val) },
  },
  methods: {
    filterNode(value, data) {
      if (!value) return true
      return (data.companyName || '').indexOf(value) !== -1
    },
    async loadNode(node, resolve) {
      if (this.loadChildren) {
        try {
          const children = await this.loadChildren(node.level === 0 ? this.companyId : node.data.id, node.level, this.domainType)
          resolve(children || [])
        } catch (e) { resolve([]) }
      } else {
        resolve([])
      }
    },
    handleNodeClick(data) {
      this.selectedNode = data
      this.$emit('node-click', data)
    },
    refreshTree() {
      this.treeData = []
      this.selectedNode = null
      this.$nextTick(() => { this.$refs.tree && this.$refs.tree.store.root.loadData() })
    },
  },
}
</script>
<style scoped>
.drill-down-tree { display: flex; height: 100%; }
.tree-panel { width: 300px; border-right: 1px solid #e8e8e8; padding: 12px; overflow-y: auto; }
.tree-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.tree-title { font-size: 14px; font-weight: 600; color: #303133; }
.detail-panel { flex: 1; padding: 12px; overflow-y: auto; }
.tree-node { display: flex; align-items: center; font-size: 13px; }
.node-label { margin-right: 6px; }
.warning-tag { margin-left: 4px; }
</style>

