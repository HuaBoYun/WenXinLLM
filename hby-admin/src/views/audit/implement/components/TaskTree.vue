<template>
  <div>
    <el-tree
      :data="data"
      :expand-on-click-node="false"
      :default-expand-all="true"
      node-key="name"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  import { myWorkGetTree, myWorkGetTreeOld } from '@/oapi/audit/project.js'
  export default {
    props: {
      //项目查看传入{projectId}
      projectId: {
        type: Number,
        default: null,
      },
    },
    name: 'ProjectSituationList',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'name',
        },
        data: [],
      }
    },
    created() {
      let model = localStorage.getItem('model')
      if(model == 'znsj') {
        this.getLeftMueDataOld()
      } else {
        this.getLeftMueData()
      }
    },
    methods: {
      async getLeftMueData() {
        this.loading = true
        const { data, code } = await myWorkGetTree({ projectId: this.projectId })
        if (code != 1) return
        this.loading = false
        this.data = data.tree
        this.$emit('getChildParam', undefined)
      },
      async getLeftMueDataOld() {
        this.loading = true
        const { data, code } = await myWorkGetTreeOld({ projectId: this.projectId })
        if (code != 1) return
        this.loading = false
        this.data = data.tree
        this.$emit('getChildParam', undefined)
      },
      handleNodeClick: function (data, node) {
        let id = node.level === 1 ? undefined : data.id
        this.$emit('getChildParam', id)
      },
    },
  }
</script>
