<template>
  <div v-loading="loading">
    <el-tree
      :data="data"
      :expand-on-click-node="false"
      :default-expand-all="true"
      node-key="id"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  import { getPreijectTreeData } from '@/api/audit/projectData'
  export default {
    name: 'ProjectSituationList',
    props: {
      projectId: {
        type: Number,
        default: '',
      },
    },
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'name',
          // isLeaf: (data, node) => {
          //   if (node === 0) {
          //     return false
          //   } else {
          //     return !data.isParent
          //   }
          // },
        },
        queryForm: {
          type: 'my',
          nodeId: undefined,
        },
        loading: false,
        data: [],
        //最外层id
        fristId: 0,
      }
    },
    created() {
      // this.load()
      this.getLeftMueData()
    },
    methods: {
      // async load(node, resove) {
      //   if (node.level === 0) {
      //     const res = await getPreijectTreeData({
      //       type: 'all',
      //       projectId: this.projectId,
      //     })

      //     resove([res.data.tree])
      //   }
      //   if (node.level === 1) {
      //     resove(node.data.children)
      //     // this.$emit('getChildParam', undefined)
      //   }
      //   if (node.level === 2) {
      //     if (!node.isLeaf) {
      //       resove(node.data.children)
      //     }
      //   }

      //   if (node.level > 2) {

      //     if (node.data.topName === '审计指引') {
      //       const res = await getLeftTreeZy({
      //         ...this.queryForm,
      //         nodeId: node.data.id,
      //       })
      //       resove(res.data.tree && res.data.tree.length ? res.data.tree : [])
      //     } else if (node.data.topName === '任务查看') {
      //       const res = await getTree({
      //         ...this.queryForm,
      //         nodeId: node.data.id,
      //       })
      //       resove(res.data.tree && res.data.tree.length ? res.data.tree : [])
      //     }
      //   }
      // },
      //获取左侧菜单栏
      getLeftMueData() {
        this.loading = true
        getPreijectTreeData({ type: 'all', projectId: this.projectId }).then(
          (res) => {
            if (res.data.tree) {
              this.data.push(res.data.tree)
              this.loading = false
            }
          }
        )
      },
      handleNodeClick: function (data) {
        this.$emit('getChildParam', data)
        // this.$emit('isFrist', isFrist)
      },
      // handleNodeClick: function (data, node) {
      //   let id = node.level === 1 ? undefined : data.id
      //   this.$emit('getChildParam', id)
      // },
    },
  }
</script>
