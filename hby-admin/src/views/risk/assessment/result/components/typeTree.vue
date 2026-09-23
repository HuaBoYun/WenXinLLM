<template>
  <div>
    <el-tree :data="data" :default-expand-all="true" :expand-on-click-node="false" node-key="riskcatid"
      :props="defaultProps" @node-click="handleNodeClick" />
  </div>
</template>

<script>
import { riskTreeLeft } from '@/api/risk/riskEvents'

export default {
  name: 'TypeTree',
  props: {
    editable: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'riskcatname',
      },
      data: [],
    }
  },
  created() {
    this.riskTreeLeftData()
  },
  methods: {
    // 获取树状结构
    async riskTreeLeftData() {
      const res = await riskTreeLeft({ orgid: 2 })
      if (res.code === 1) {
        this.data = res.data.tree
        this.$emit('select', res.data.tree[0])
      }
    },
    handleNodeClick(data) {
      this.$emit('select', data)
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
