<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogTreeVisible"
    width="500px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-tree
      ref="tree"
      :check-strictly="true"
      :data="data"
      :expand-on-click-node="false"
      :default-expanded-keys="expandedKeys"
      highlight-current
      node-key="id"
      :props="defaultProps"
      :load="fetchData"
      lazy
      @check-change="handleCheckChange"
      @node-click="handleNodeClick"
    />
  </el-dialog>
</template>

<script>
  // import { getExecutorOptionsTree } from '@/api/contract/manage'
  import { getOrgTreeDataByCompany } from '@/api/common'
  import { findOrganizationByTreeAllss } from '@/api/audit/project'
  export default {
    name: 'DepartmentOptions',
    data() {
      return {
        title: '部门',
        dialogTreeVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
          isLeaf: 'isLeaf',
        },
        data: [],
      }
    },
    created() {},
    methods: {
      show() {
        this.dialogTreeVisible = true
        // this.fetchTree()
        this.fetchData()
      },
      async fetchTree() {
        const res = await getOrgTreeDataByCompany()
        this.data = res
      },
      fetchData(node, resolve, orgId) {
        if (node && node.level === 0) {
          return
        }
        if (node && node.level === 1) {
          resolve(this.data[0].children)
          return
        }
        this.loading = true
        // {nodeId: !node ? this.currentOrg.id : node.data.id,}
        findOrganizationByTreeAllss({ fatherorgid: !node ? '' : node.data.id })
          .then((res) => {
            const tree = this.formatTree(res.data)
            if (node && node.level > 0) {
              resolve(tree[0].children)
              return
            }
            if (!node) {
              this.$emit('select', {
                id: tree[0].id,
                label: tree[0].name,
              })
              // this.expandedKeys.push(this.currentOrg.id)

              this.data = tree
            }
          })
          .finally(() => {
            this.loading = false
          })
      },
      handleNodeClick(data) {},
      handleCheckChange(data) {},
      close() {
        this.dialogTreeVisible = false
      },
      formatTree(tree) {
        const list = tree.map((i) => {
          return {
            id: i.id,
            name: i.name,
            isLeaf: !i.isParent,
            children: this.formatTree(i.children || []),
          }
        })
        return list
      },
      //回调
      confirm() {
        const checked = this.$refs['tree'].getCurrentNode()
        this.$emit('selected', checked)
        this.dialogTreeVisible = false
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
