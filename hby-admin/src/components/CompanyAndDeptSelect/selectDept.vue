<!-- 选择公司或部门 -->
<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :modal="false"
    width="600px"
    :close-on-click-modal="false"
  >
    <div class="system-log-container" style="width: 100%">
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-tree
        ref="myTree"
        v-loading="loading"
        :data="data"
        :default-expanded-keys="expandedKeys"
        :show-checkbox="multiple"
        :expand-on-click-node="false"
        :highlight-current="true"
        lazy
        :load="fetchData"
        node-key="id"
        :props="defaultProps"
        @node-click="handleNodeClick"
        style="width: 300px"
        :check-strictly="true"
      />
    </div>
  </el-dialog>
</template>

<script>
  import { findOrganizationByTreeAllss } from '@/api/audit/project'
  import { findOrganizationData } from '@/api/setting/org'

  export default {
    props: {
      multiple: {
        type: Boolean,
        default: true,
      },
    },
    components: {},
    data() {
      return {
        dialogVisible: false,
        defaultProps: {
          children: 'children',
          label: 'label',
          isLeaf: 'isLeaf',
        },
        current: undefined,
        reviewType: '',
        expandedKeys: [],
        data: [],
        loading: false,
      }
    },
    methods: {
      showEdit(e) {
        if (e) {
          this.reviewType = e
        }
        this.dialogVisible = true
        this.current = undefined
        this.fetchData()
      },
      handleNodeClick(val) {
        console.log('nodeClick', val)
        // this.queryForm.orgid = val.id
      },
      confirm() {
        const selected = this.$refs.myTree.getCheckedNodes()
        if (!selected || !selected.length) {
          this.$baseMessage('请选择！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('projectManages', selected)
        this.close()
      },
      async fetchData(node, resolve, orgId) {
        if (node && node.level === 0) {
          return
        }
        const res1 = await findOrganizationByTreeAllss({
          nodeId: !node ? '' : node.data.id,
        })
        if (node && node.level === 1) {
          resolve(
            this.formatTree(res1)[0].children.concat(...this.data[0].children)
          )
          return
        }
        this.loading = true
        findOrganizationData({ nodeId: !node ? '' : node.data.id })
          .then((res) => {
            const tree = this.formatTree(res)
            if (node && node.level > 0) {
              resolve(
                this.formatTree(res1)[0].children.concat(...tree[0].children)
              )
              return
            }
            if (!node) {
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
            isLeaf: !i.isParent && !(i.name.indexOf('有限公司') > -1),
            parentId: i.pId || '',
            children: this.formatTree(i.children || []),
          }
        })
        return list
      },
      close() {
        this.dialogVisible = false
      },
    },
  }
</script>
