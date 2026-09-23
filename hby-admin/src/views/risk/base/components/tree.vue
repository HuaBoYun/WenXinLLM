<template>
  <div>
    <div class="top-action">
      <el-button type="success" @click="handleCreate" size="mini">
        新建
      </el-button>
      <el-button type="primary" @click="handleEdit" size="mini">修改</el-button>
      <el-button type="primary" @click="handleDelete" size="mini">
        删除
      </el-button>
    </div>
    <el-tree
      v-loading="loading"
      :data="data"
      :default-expanded-keys="expandedKeys"
      :expand-on-click-node="false"
      :highlight-current="true"
      lazy
      :load="fetchData"
      node-key="id"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
    <TreeEdit ref="edit" @FetchData="fetchData"></TreeEdit>
  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  import { getTreeData, deleteTree } from '@/api/setting/org'
  import TreeEdit from './editTree.vue'

  export default {
    name: 'CompanyTree',
    components: { TreeEdit },
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
          isLeaf: 'isLeaf',
        },
        list: [],
        orgid: '',
        data: [],
        loading: false,
        expandedKeys: [],
        nodeId: '',
        nodeInfo: {},
      }
    },
    computed: {
      currentOrg() {
        const orgStr = window.sessionStorage.getItem('current-org')
        if (orgStr && !this.alwaysRoot) {
          try {
            const org = JSON.parse(orgStr)
            if (org.id && org.label) {
              return org
            }
          } catch (e) {}
        }
        return {
          id: 1,
          label: '长江投资（中国）有限公司',
        }
      },
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleNodeClick(data) {
        this.nodeId = data.id
        this.nodeInfo = data
        this.$emit('changeNode', data)
      },

      fetchData(node, resolve, orgId) {
        if (node && node.level === 0) {
          return
        }
        this.loading = true
        // {nodeId: !node ? this.currentOrg.id : node.data.id,}
        getTreeData({ nodeId: !node ? '' : node.data.id, mpdeltype: 'FXCT' })
          .then((res) => {
            const tree = this.formatTree(res.data.data)
            if (node && node.level > 0) {
              resolve(tree)
              return
            }
            if (!node) {
              this.data = tree
              this.$nextTick(() => {
                this.expandedKeys = (tree || []).map((item) => item.id)
              })
            }
          })
          .finally(() => {
            this.loading = false
          })
      },
      formatTree(tree) {
        const list = tree.map((i) => {
          return {
            id: i.id,
            label: i.name,
            isLeaf: !i.isParent,
            parentId: i.pId || '',
            children: this.formatTree(i.children || []),
          }
        })
        return list
      },
      handleCreate() {
        if (!this.nodeId) {
          this.$message({
            type: 'error',
            message: '请先选择节点',
          })
          return
        }
        const info = {
          nodeId: this.nodeId,
        }
        this.$refs['edit'].show(info, '新增')
      },
      handleEdit() {
        if (!this.nodeId) {
          this.$message({
            type: 'error',
            message: '请先选择节点',
          })
          return
        }
        const info = {
          nodeId: this.nodeId,
        }
        this.$refs['edit'].show(info, '编辑')
      },
      handleDelete() {
        if (!this.nodeInfo.isLeaf) {
          this.$message({
            type: 'error',
            message: '该节点下有子节点，无法删除',
          })
          return
        }
        deleteTree({
          nodeId: this.nodeId,
        }).then((res) => {
          if (res.code == 1) {
            this.fetchData()
          }
        })
      },
    },
  }
</script>
<style scoped>
  .top-action {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
</style>
