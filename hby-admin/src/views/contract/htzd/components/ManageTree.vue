<template>
  <div>
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
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  import { findOrganizationByTreeAllss } from '@/api/audit/project'

  export default {
    name: 'ProjectDataTree',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
          isLeaf: 'isLeaf',
        },
        data: [
          {
            id: 1,
            label: '长江集团有限公司',
            children: [
              {
                id: 2,
                label: '风险管理部',
              },
              {
                label: '人力资源部',
              },
            ],
          },
        ],
      }
    },
    created() {
      // this.getDataprojectLeftFun()
      this.fetchData()
    },
    methods: {
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
            name: i.name,
            isLeaf: !i.isParent,
            children: this.formatTree(i.children || []),
          }
        })
        return list
      },
      //回调
      handleNodeClick: function (data) {
        this.$emit('getChildParam', data.id)
        this.$emit('changeNode', data)
      },
      //回调
      async getDataprojectLeftFun() {
        let res = await findOrganizationByTreeAllss()
        this.data = res
        console.dir(this.data)
      },
    },
  }
</script>
