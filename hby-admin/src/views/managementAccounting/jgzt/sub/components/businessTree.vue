<template>
  <div>
    <el-tree
      :data="data"
      :expand-on-click-node="false"
      node-key="id"
      :props="defaultProps"
      :load="loadNode"
      lazy
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  import { getBusinessData, getTableList } from '@/api/workbench/businessData'

  export default {
    name: 'ProjectDataTree',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
          value: 'id',
          isLeaf: 'leaf',
        },
        data: [],
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async loadNode(node, resolve) {
        if (node.level <= 0) {
          return false
        }

        const params = JSON.parse(JSON.stringify({ batchId: node.data.id }))
        const { data } = await getTableList(params)
        let newData = data.map((item) => {
          return {
            ...item,
            label: item.customTable,
            leaf: true,
          }
        })
        resolve(newData || [])
      },
      async fetchData(node, resolve, orgId) {
        const {
          data: { records },
        } = await getBusinessData({ pageNo: 1, pageSize: 99999 })
        this.data = records.map((item) => {
          return {
            ...item,
            value: item.id,
            label: item.attname,
            // leaf: true,
          }
        })
      },
      handleNodeClick: function (data, node) {
        if (node.level == 2) {
          this.$emit('changeNode', data)
        }
      },
    },
  }
</script>
