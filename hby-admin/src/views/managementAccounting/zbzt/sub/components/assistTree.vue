<template>
  <div>
    <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick" />
  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  import { getAccountAssistTreeData } from '@/api/workbench/accountData/accountData'
  export default {
    name: 'AssistTree',
    // props: {
    //   alwaysRoot: {
    //     type: Boolean,
    //     default: false,
    //   },
    // },
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
      }
    },
    computed: {},
    created() {
      this.fetchData()
    },
    methods: {
      handleNodeClick(data) {
        //
        // this.nodeid = data.id
        this.$emit('select', data)
      },

      fetchData(node, resolve, orgId) {
        //
        // if (node && node.level === 0) {
        //   return
        // }
        // if (node && node.level === 1) {
        //   resolve(this.data[0].children)
        //   return
        // }
        // this.loading = true
        // // {nodeId: !node ? this.currentOrg.id : node.data.id,}
        // findOrganization({ nodeId: !node ? '' : node.data.id })
        //   .then((res) => {
        //     const tree = this.formatTree(res)
        //     if (node && node.level > 0) {
        //       resolve(tree[0].children)
        //       return
        //     }
        //     if (!node) {
        //       this.$emit('select', {
        //         id: tree[0].id,
        //         label: tree[0].name,
        //       })
        //       this.expandedKeys.push(this.currentOrg.id)
        //
        //       this.data = tree
        //     }
        //   })
        //   .finally(() => {
        //     this.loading = false
        //   })
        getAccountAssistTreeData().then((res) => {
          if (res.data) {
            const info = res.data.map((i) => {
              return {
                label: i,
                children: [
                  { label: '辅助信息表', value: i },
                  {
                    label: '辅助余额表',
                    value: i,
                  },
                  {
                    label: '辅助总表',
                    value: i,
                  },
                ],
              }
            })
            this.data = info
          }
        })
      },
    },
  }
</script>
