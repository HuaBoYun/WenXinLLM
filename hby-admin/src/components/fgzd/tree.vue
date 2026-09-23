<template>
  <div class="content">
    <el-tree
      ref="tree"
      v-loading="loading"
      :data="data"
      :default-expanded-keys="expandedKeys"
      :default-expand-all="false"
      :expand-on-click-node="false"
      :highlight-current="true"
      node-key="id"
      :props="defaultProps"
      :load="fetchData"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  import { fgzdTree } from '@/api/setting/sjzy'
  import info from './tree'

  export default {
    name: 'DepTree',
    props: {
      loadedSelect: {
        type: Boolean,
        default: true,
      },
    },
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
          isLeaf: 'isLeaf',
        },
        list: [],
        data: [],
        loading: false,
        expandedKeys: [1],
        orgid: '',
        nodeid: '',
        orgName: '',
        orgid: '',
        treeData: [],
      }
    },

    computed: {},
    created() {
      // this.orgid = JSON.parse(localStorage.getItem('userInfo')).currentOrg.orgid
      // this.fetchData()
      // this.data = this.formatTree(info.data)
      this.fetchData()
    },
    methods: {
      handleNodeClick(data) {
        this.$emit('select', data)
      },
      async fetchData() {
        fgzdTree().then((res) => {
          this.data = this.formatTree(res.data)
        })
      },
      formatTree(tree) {
        const list = tree.map((i) => {
          return {
            id: i.id,
            label: i.name,
            children: this.formatTree(i.child),
          }
        })
        return list
      },
    },
  }
</script>
<style lang="scss" scoped>
  .content {
    width: 350px;
  }
  ::v-deep {
    .el-tree {
      width: 100%;
      overflow: scroll;
      height: calc(100vh - 260px);
      overflow-x: auto;
    }
    .el-tree > .el-tree-node {
      display: inline-block;
      min-width: 100%;
    }
  }
</style>
