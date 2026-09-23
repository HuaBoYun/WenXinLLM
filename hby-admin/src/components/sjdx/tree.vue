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
  import { sjdxTree } from '@/api/setting/sjzy'

  export default {
    name: 'DepTree',
    props: {
      loadedSelect: {
        type: Boolean,
        default: true,
      },
      companyName: {
        type: String,
        default: '',
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
        treeData: [
          {
            createTime: 1701793404000,
            name: this.companyName,
            sort: 1,
            id: 1,
            parentId: 191,
            status: 0,
            child: [
              {
                createTime: 1701793404000,
                name: '企业基本情况',
                sort: 1,
                id: 192,
                parentId: 191,
                status: 0,
                child: [],
              },
              {
                createTime: 1701793421000,
                name: '重要管理信息',
                sort: 2,
                id: 193,
                parentId: 191,
                status: 0,
                child: [],
              },
              {
                createTime: 1701793442000,
                name: '企业绩效',
                sort: 3,
                id: 194,
                parentId: 191,
                status: 0,
                child: [],
              },
              {
                createTime: 1701793466000,
                name: '历年发现问题',
                sort: 4,
                id: 195,
                parentId: 191,
                status: 0,
                child: [],
              },
              {
                createTime: 1701793489000,
                name: '重要业务领域及风险评估',
                sort: 5,
                id: 196,
                parentId: 191,
                status: 0,
                child: [],
              },
              {
                createTime: 1701793524000,
                name: '总体认识',
                sort: 6,
                id: 197,
                parentId: 191,
                status: 0,
                child: [],
              },
              {
                createTime: 1701793560000,
                name: '审计经验',
                sort: 7,
                id: 198,
                parentId: 191,
                status: 0,
                child: [],
              },
            ],
          },
        ],
      }
    },

    computed: {},
    created() {
      // this.orgid = JSON.parse(localStorage.getItem('userInfo')).currentOrg.orgid
      // this.fetchData()
      // this.data = this.formatTree(this.treeData)
      this.fetchData()
    },
    methods: {
      change() {
        this.$forceUpdate()
      },
      handleNodeClick(data) {
        if (data.id == 1) {
          return
        }
        this.$emit('select', data)
      },
      async fetchData() {
        sjdxTree({ companyName: this.companyName }).then((res) => {
          const data = [
            {
              createTime: 1701793404000,
              name: this.companyName,
              sort: 1,
              id: 1,
              parentId: 191,
              status: 0,
              child: [],
            },
          ]
          data[0].child = res.data
          if (data[0].child) {
            this.data = this.formatTree(data)
          }
        })
      },
      formatTree(tree) {
        const list = tree.map((i) => {
          return {
            id: i.id,
            label: i.name,
            children: this.formatTree(i.child || []),
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
