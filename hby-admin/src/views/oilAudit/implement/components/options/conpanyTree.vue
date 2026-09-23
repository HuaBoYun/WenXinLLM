<template>
  <div>
    <el-tree
      v-loading="loading"
      :data="data"
      :default-expanded-keys="expandedKeys"
      :expand-on-click-node="false"
      lazy
      :load="fetchData"
      node-key="id"
      :props="defaultProps"
      :show-checkbox="checkbox"
      :check-strictly="checkStrictly"
      @check-change="handleNodeClick"
      @node-click="handleSelect"
    />
  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  import { qxsdLeftorg, findOrganization } from '@/api/setting/org'

  export default {
    name: 'CompanyTree',
    props: {
      alwaysRoot: {
        type: Boolean,
        default: false,
      },
      checkbox: {
        type: Boolean,
        default: true,
      },
      checkStrictly: {
        type: Boolean,
        default: true,
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
        arr: [],
        arrData: [],
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
        if (this.arr.indexOf(data.id) > -1) {
          this.arr.splice(this.arr.indexOf(data.id), 1)
          this.arrData.splice(this.arr.indexOf(data.id), 1)
        } else {
          this.arr.push(data.id)
          this.arrData.push(data)
        }
        data.name = data.label
        this.$emit('select', data, this.arrData)
      },
      handleSelect(data) {
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
        // {nodeId: !node ? this.currentOrg.id : node.data.id,}
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
    },
  }
</script>
