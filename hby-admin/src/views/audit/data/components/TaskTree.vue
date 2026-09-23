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
  import { getTreeDataById } from '@/api/audit/projectData'
  export default {
    name: 'ProjectSituationList',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'name',
        },
        data: [],
        loading: false,
        //最外层id
        fristId: 0,
      }
    },
    created() {
      this.getLeftMueData()
    },
    methods: {
      //获取左侧菜单栏
      getLeftMueData() {
        this.loading = true
        getTreeDataById({ type: 'all' }).then((res) => {
          if (res.data.tree) {
            const nodeId = res.data.tree[0].id
            const rootN = res.data.tree[0]

            getTreeDataById({ nodeId: nodeId, type: 'all' }).then((e) => {
              e.data.tree.forEach((item) => {
                rootN.children.push(item)
              })
              // obj.children = e.data.tree
              // this.data = e.data.tree
              this.data = [rootN]
              this.fristId = rootN.id
              this.loading = false

              if (nodeId == this.fristId) return
              this.$emit('getChildParam', e.data.tree[0].id)
            })
          }
        })
      },
      handleNodeClick(data) {
        // let isFrist = data.id === this.fristId

        if (data.id === this.fristId) {
          this.$emit('getChildParam', '')
        } else {
          this.$emit('getChildParam', data.id)
        }
        // this.$emit('isFrist', isFrist)
      },
    },
  }
</script>
