<!--
 * @Date: 2022-03-28 14:54:04
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-27 12:24:16
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/options/unit.vue
-->
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
  import { getUnitOptions } from '@/api/contract/manage'
  import { findOrganization } from '@/api/setting/org'

  export default {
    name: 'UnitOptions',
    data() {
      return {
        title: '',
        dialogTreeVisible: false,
        defaultProps: {
          children: 'children',
          label: 'label',
          value: 'id',
          isLeaf: 'isLeaf',
        },
        data: [],
        expandedKeys: [],
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
    created() {},
    methods: {
      show() {
        this.dialogTreeVisible = true
        // this.fetchTree()
        this.fetchData()
      },
      async fetchTree() {
        const res = await getUnitOptions()
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
        findOrganization({ fatherorgid: !node ? '' : node.data.id })
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
              this.expandedKeys.push(this.currentOrg.id)

              this.data = tree
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
            children: this.formatTree(i.children || []),
          }
        })
        return list
      },
      handleNodeClick(data) {},
      handleCheckChange(data) {},
      close() {
        this.dialogTreeVisible = false
      },
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
