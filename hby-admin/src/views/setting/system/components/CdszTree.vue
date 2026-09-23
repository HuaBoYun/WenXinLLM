<template>
  <div>
    <el-tree
      :data="data"
      :default-checked-keys="[0]"
      :default-expanded-keys="[1, 2]"
      :expand-on-click-node="false"
      highlight-current
      lazy
      :load="loadNode"
      node-key="id"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  import { getMenuSettingTree } from '@/api/setting/system'
  export default {
    name: 'FlowCategoryList',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        data: [],
      }
    },
    created() {},
    methods: {
      formatArr(source) {
        let arr = []
        source.forEach((item) => {
          let obj = {
            value: item.id,
            label: item.name,
          }
          arr.push(obj)
        })
        return arr
      },
      async loadNode(node, resolve) {
        if (node.level === 0) {
          const res = await getMenuSettingTree({ id: -1 })
          return resolve(this.formatArr(res))
        }
        if (node.level >= 1) {
          const res = await getMenuSettingTree({
            id: -1,
            nodeId: node.data.value,
          })
          return resolve(this.formatArr(res))
        }
      },
      handleNodeClick(data) {
        this.$emit('node-click', data)
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
