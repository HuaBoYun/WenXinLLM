<template>
  <div>
    <el-tree
      :data="data"
      :default-checked-keys="[0]"
      default-expand-all
      :expand-on-click-node="false"
      highlight-current
      node-key="id"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  import { getContractTypeTree } from '@/api/contract/manage'
  export default {
    name: 'CategoryTree',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        data: [],
      }
    },
    created() {
      this.fetchTree()
    },
    methods: {
      //数据结构处理
      formatFr(fr) {
        let arr = []
        let obj = {}
        fr.forEach((item) => {
          let tmp = { ...item }
          if (tmp.children) {
            tmp.children = this.formatFr(tmp.children)
            obj = {
              value: tmp.id,
              label: tmp.name,
              children: tmp.children,
            }
          } else {
            obj = {
              value: tmp.id,
              label: tmp.name,
            }
          }
          arr.push(obj)
        })
        return arr
      },
      //请求树
      async fetchTree() {
        const res = await getContractTypeTree()
        this.data = this.formatFr(res)
      },
      handleNodeClick(data) {
        this.$emit('node-change', data)
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
