<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogTreeVisible"
    :append-to-body="true"
    width="500px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm()">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-tree
      ref="tree"
      v-loading="loading"
      :data="data"
      :default-expanded-keys="expandedKeys"
      :expand-on-click-node="false"
      :highlight-current="true"
      lazy
      :load="fetchData"
      node-key="id"
      :props="defaultProps"
      show-checkbox
      @check-change="handleCheckChange"
    />
    <!-- 123
    <vab-tree></vab-tree> -->
  </el-dialog>
</template>

<script>
  import { getOrgTreeByDepartment } from '@/api/common'
  import { findOrganizationData } from '@/api/setting/org'
  export default {
    name: 'DepartmentOptions',
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
        nodeid: '',
        title: '机构',
        keys: [],
        dialogTreeVisible: false,
        checkbox: true,
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
          } catch (e) {
            console.log(e)
          }
        }
        return {
          id: 1,
          label: '长江投资（中国）有限公司',
        }
      },
    },
    created() {},
    methods: {
      show(bol, keys) {
        this.checkbox = bol
        if (this.checkbox) {
          console.log('keys', keys)
          this.keys = keys
        }
        this.dialogTreeVisible = true
        // this.fetchTree()
        this.fetchData()
      },
      async fetchTree() {
        const info = {
          nodeId: '',
        }
        const res = await findOrganizationData(info)
        this.data = res
      },
      async fetchTreeList() {
        // const info = {
        //   nodeId: '',
        // }
        // const res = await findOrganizationData(info)
        // this.data = res
      },
      handleNodeClick(data) {
        // console.log(data)
      },
      handleCheckChange(data) {
        // console.log('data', data)
      },
      close() {
        this.keys = []
        this.data = []
        this.dialogTreeVisible = false
      },
      confirm() {
        if (!this.checkbox) {
          const checked = this.$refs['tree'].getCurrentNode()
          this.$emit('selected', checked)
        } else {
          const checked = this.$refs['tree'].getCheckedNodes()
          this.$emit('selected', checked)
        }
        this.keys = []
        this.data = []
        this.dialogTreeVisible = false
      },
      handleNodeClick(data) {
        console.log(data)
        this.nodeid = data.id
        this.$emit('select', data)
      },

      fetchData(node, resolve, orgId) {
        console.log('node', node, orgId)
        if (node && node.level === 0) {
          return
        }
        if (node && node.level === 1) {
          resolve(this.data[0].children)
          return
        }
        this.loading = true
        // {nodeId: !node ? this.currentOrg.id : node.data.id,}
        findOrganizationData({ nodeId: !node ? '' : node.data.id })
          .then((res) => {
            const tree = this.formatTree(res)
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
              console.log(this.expandedKeys, 'this.expandedKeys')
              console.log('company tree:', tree)
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
