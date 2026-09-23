<template>
  <div>
    <el-tree
      v-loading="loading"
      :data="data"
      :default-expanded-keys="expandedKeys"
      :expand-on-click-node="false"
      lazy
      :load="fetchData"
      show-checkbox
      node-key="id"
      :props="defaultProps"
      @check-change="handleNodeClick"
      :check-strictly="true"
      :default-checked-keys="defaultList"
    />
  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  import { findOrganization } from '@/api/setting/org'

  export default {
    name: '',
    props: {
      alwaysRoot: {
        type: Boolean,
        default: false,
      },
      notDefaultSelect: {
        type: Boolean,
        default: false,
      },
      defaultTreeData: {
        type: Array,
        default: [],
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
        expandedKeys: [],
        nodeid: '',
        arr: [],
        defaultList: [],
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

      this.$nextTick(() => {
        this.defaultList = this.defaultTreeData.map((res) => res.authOrgId)
        this.arr = this.defaultTreeData.map((res) => res.authOrgId)
      })
    },
    watch: {
      data(val) {
        const ids = this.defaultTreeData.map((res) => res.authOrgId)
        this.defaultList = ids
        this.arr = ids
      },
    },
    methods: {
      handleNodeClick(data) {
        if (this.arr.indexOf(data.id) > -1) {
          this.arr.splice(this.arr.indexOf(data.id), 1)
        } else {
          this.arr.push(data.id)
        }
        this.$emit('select', this.arr)
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
              if (!this.notDefaultSelect) {
                // 不需要第一次进来就默认选择第一个
                this.$emit('select', {
                  id: tree[0].id,
                  label: tree[0].name,
                })
              }
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
    },
  }
</script>
