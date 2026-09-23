<!--
 * @Date: 2022-03-28 15:04:10
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 17:05:56
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/options/department.vue
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
      :show-checkbox="multiSelect"
      :expand-on-click-node="false"
      highlight-current
      node-key="id"
      :load="fetchData"
      lazy
      :props="defaultProps"
      @check-change="handleCheckChange"
      @node-click="handleNodeClick"
    />
  </el-dialog>
</template>

<script>
  import { findOrganizationByTreeAllss } from '@/api/audit/project'
  export default {
    name: 'DepartmentOptions',
    props: {
      multiSelect: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        title: '部门',
        dialogTreeVisible: false,
        defaultProps: {
          children: 'children',
          label: 'label',
          value: 'id',
          isLeaf: 'isLeaf',
        },
        data: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        indexNumber: -1,
      }
    },
    created() {},
    methods: {
      show(e) {
        if (e !== undefined) {
          this.indexNumber = e
        }
        this.dialogTreeVisible = true
        this.fetchData()
        // this.fetchTree()
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
      async fetchTree() {
        const res = await findOrganizationByTreeAllss(this.queryForm)
        this.data = res
      },
      handleNodeClick(data) {},
      handleCheckChange(data) {},
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogTreeVisible = false
      },
      confirm() {
        const checked = this.$refs['tree'].getCurrentNode()
        this.$emit('selected', checked)
        if (this.multiSelect) {
          const checkeds = this.$refs['tree'].getCheckedNodes()
          this.$emit('selectByTable', {
            index: this.indexNumber,
            row: checkeds,
          })
        }
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
