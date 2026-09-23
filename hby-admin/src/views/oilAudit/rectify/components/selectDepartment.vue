<template>
  <div>
    <el-dialog
      title="被审计单位"
      :visible.sync="dialogVisible"
      width="500px"
      :append-to-body="true"
      :close-on-click-modal="false"
      v-if="dialogVisible"
    >
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
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
    </el-dialog>
  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  import { findOrganization } from '@/oapi/setting/org'

  export default {
    name: 'CompanyTree',
    props: {
      alwaysRoot: {
        type: Boolean,
        default: false,
      },
    },
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
        expandedKeys: [116821],
        dialogVisible: false,
        multipleSelection: {},
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
        this.multipleSelection = data
        data.name = data.label
      },
      fetchData(node, resolve) {
        if (node && node.level === 0) {
          return
        }
        if (node && node.level === 1) {
          resolve(this.data[0].children)
          return
        }

        let cs = {}
        if (node && node.data) {
          cs.nodeId = node.data.id
        }
        this.loading = true
        // {nodeId: !node ? this.currentOrg.id : node.data.id,}
        findOrganization(cs)
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
      showEdit() {
        this.dialogVisible = true
        this.fetchData()
        // this.getExecutorTree()
      },
      save() {
        if (!this.multipleSelection.name) {
          this.$baseMessage('请选择部门！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('submit', this.multipleSelection)
        this.dialogVisible = false
        this.multipleSelection = {}
      },
      close() {
        this.dialogVisible = false
        this.multipleSelection = {}
      },
    },
  }
</script>
