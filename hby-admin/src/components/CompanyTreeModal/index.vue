<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-07-20 23:13:27
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-21 22:34:06
 * @FilePath: \hb-admin\src\views\contract\contractManage\components\options\sealDepartment.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
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
      ref="companyTree"
      v-loading="loading"
      :data="data"
      :default-expanded-keys="expandedKeys"
      :expand-on-click-node="false"
      :highlight-current="true"
      lazy
      :load="fetchData"
      node-key="id"
      :props="defaultProps"
      :show-checkbox="checkbox"
      :check-strictly="checkStrictly"
      @node-click="handleNodeClick"
    />
  </el-dialog>
</template>

<script>
  import CompanyTree from '@/components/CompanyTree.vue'
  import { findOrganization } from '@/api/setting/org'

  export default {
    name: 'CompanyTreeModal',
    props: ['alwaysRoot', 'checkStrictly'],
    components: { CompanyTree },
    data() {
      return {
        loading: true,
        title: '公司',
        keys: [],
        dialogTreeVisible: false,
        checkbox: false,

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
      }
    },
    created() {},
    methods: {
      show(bol, keys, title) {
        this.checkbox = !!bol
        this.title = title

        if (this.checkbox) {
          this.keys = keys
        }
        this.fetchData()
        this.dialogTreeVisible = true
      },
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
      handleNodeClick(data) {
        data.name = data.label
        this.$emit('select', data)
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
          cs.fatherorgid = node.data.id
        }
        this.loading = true
        findOrganization(cs)
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
      handleCheckChange(data) {},
      close() {
        this.keys = []
        this.dialogTreeVisible = false
        this.orgUrl = undefined
        this.$refs['companyTree'].setCheckedKeys([])
      },
      confirm() {
        if (!this.checkbox) {
          const checked = this.$refs['companyTree'].getCurrentNode()
          this.$emit('selected', checked)
        } else {
          const checked = this.$refs['companyTree'].getCheckedNodes()
          this.$emit('selected', checked)
        }
        this.keys = []
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
