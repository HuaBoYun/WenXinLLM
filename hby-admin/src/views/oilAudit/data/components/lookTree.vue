<template>
  <div v-loading="loading">
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
  import { getLiftMenu, currSsProject } from '@/oapi/audit/projectData'
  export default {
    name: 'ProjectSituationList',
    //档案传入
    props: {
      projectid: {
        type: [Number, String],
        default: undefined,
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
        projectId: 0,
      }
    },
    created() {
      this.projectId = this.projectid
      if (this.$route.path == '/implement/look') {
        this.currSsProject()
      } else {
        this.getLeftMueData(this.projectId)
      }
    },
    methods: {
      async currSsProject() {
        const { data } = await currSsProject()
        this.projectId = data.pj.id
        this.getLeftMueData(data.pj.id)
      },
      //获取左侧菜单栏
      async getLeftMueData(id) {
        this.loading = true
        const data = await getLiftMenu({ projectid: id })
        this.loading = false
        this.data.push(data.data.tree)
      },
      handleNodeClick: function (data) {
        this.$emit('getChildParam', { data, projectId: this.projectId })
      },
    },
  }
</script>
