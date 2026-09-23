<template>
  <div>
    <el-tree
      :expand-on-click-node="false"
      :default-expand-all="true"
      node-key="id"
      :props="defaultProps"
      @node-click="handleNodeClick"
      :load="load"
      :lazy="true"
    />
  </div>
</template>

<script>
  import { getTree } from '@/api/audit/implement'
  import { getTreeTask } from '@/oapi/audit/project.js'

  export default {
    name: 'ProjectSituationList',
    props: {
      //项目查看传入{isShow,projectId}
      projectId: {
        type: Number,
        default: null,
      },
    },
    data() {
      return {
        queryForm: {
          type: 'my',
          nodeId: undefined,
        },
        defaultProps: {
          children: 'children',
          label: 'name',
          isLeaf: (data, node) => {
            if (node === 0) {
              return false
            } else {
              return !data.isParent
            }
          },
        },
        data: [],
      }
    },
    methods: {
      async load(node, resove) {
        if (node.level === 0) {
          const res = await getTreeTask({
            ...this.queryForm,
            projectId: this.projectId,
          })
          resove(res.data.tree)
        }
        if (node.level === 1) {
          this.$emit('getChildParam', undefined)
        }
        if (node.level > 0) {
          const res = await getTreeTask({
            ...this.queryForm,
            nodeId: node.data.id,
            projectId: this.projectId,
          })
          resove(res.data.tree && res.data.tree.length ? res.data.tree : [])
        }
      },
      handleNodeClick: function (data, node) {
        let id = node.level === 1 ? undefined : data.id
        this.$emit('getChildParam', id)
      },
    },
  }
</script>
