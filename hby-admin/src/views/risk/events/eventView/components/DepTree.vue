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
      lazy
      @node-click="handleNodeClick"
    >
      <span slot-scope="{ node, data }" class="custom-tree-node">
        <el-tooltip
          :content="node.label"
          placement="right"
          :disabled="node.label.length < 12"
        >
          <span>{{ node.label }}</span>
        </el-tooltip>
      </span>
    </el-tree>
  </div>
</template>

<style lang="scss" scoped>
  .content {
    width: 300px;
  }
  ::v-deep {
    .el-tree {
      width: 100%;
      overflow: scroll;
      height: calc(100vh - 260px);
      overflow-x: hidden;
    }
    .el-tree > .el-tree-node {
      display: inline-block;
      min-width: 100%;
    }
    .custom-tree-node {
      width: 100%;
      span {
        display: inline-block;
        max-width: 220px; // 考虑到树节点的缩进和图标空间
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
    // 处理树节点文本溢出
    .el-tree-node__label {
      display: inline-block;
      max-width: 220px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
</style>

<script>
  import { findOrganization } from '@/api/setting/org'

  export default {
    name: 'DepTree',
    props: {
      loadedSelect: {
        type: Boolean,
        default: true,
      },
      isSelectNode: {
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
        expandedKeys: [],
        orgid: '',
        nodeid: '',
        orgName: '',
        orgid: '',
      }
    },

    computed: {
      currentOrg() {
        const orgStr = window.sessionStorage.getItem('current-org')
        if (orgStr && !this.alwaysRoot) {
          try {
            const org = JSON.parse(orgStr)
            if (org.id && org.label) {
              return org
            }
          } catch (e) {
            console.log(e)
          }
        }
        return {
          id: 1,
          label: '长江投资（中国）有限公司',
        }
      },
    },
    created() {
      this.orgid = JSON.parse(localStorage.getItem('userInfo')).currentOrg.orgid
      this.fetchData()
    },
    methods: {
      change() {
        this.$forceUpdate()
      },
      resetSearch() {
        this.orgName = ''
        this.$refs.tree.$data.store.lazy = true // 开启懒加载
        this.fetchData()
        this.$forceUpdate()
      },
      handleNodeClick(data) {
        console.log(data)
        this.nodeid = data.id
        this.$emit('select', data)
      },
      queryData() {
        if (this.orgName == '') {
          this.$refs.tree.$data.store.lazy = true // 开启懒加载
          this.fetchData()
        } else {
          this.data = []
          this.$refs.tree.$data.store.lazy = false
          this.fetchData() //加载数据
        }
        this.$forceUpdate()
      },
      async fetchData(node, resolve) {
        console.log('node', node)
        if (node && node.level === 0) {
          return
        }
        if (node && node.level === 1) {
          resolve(this.data[0].children)
          return
        }

        this.loading = true

        findOrganization({
          fatherorgid: !node ? this.orgid : node.data && node.data.id,
          orgname: this.orgName,
        })
          .then((res) => {
            console.log('res', res)
            const tree = this.formatTree(res.data)
            console.log('tree:data', res.data)

            if (node && node.level > 0) {
              console.log('tree:', tree)
              resolve(tree[0].children)
              return
            }
            console.log('node', !node)
            if (!node) {
              // 无论isSelectNode是什么值，都默认选中第一条数据
              if (tree && tree.length > 0) {
                this.$emit('select', {
                  id: tree[0].id,
                  label: tree[0].name,
                })
                this.expandedKeys.push(tree[0].id)
                console.log('tree:lll', tree)
              }
              this.data = tree
            }

            if (this.orgName) {
              let that = this
              this.data = tree
              var Date2 = window.setTimeout(function () {
                console.log('Date2', Date2)
                that.setAllExpand()
              }, 1000)
            }
          })
          .finally(() => {
            this.loading = false
          })
        this.loading = false
      },

      setAllExpand() {
        for (var i = 0; i < this.$refs.tree.store._getAllNodes().length; i++) {
          this.$refs.tree.store._getAllNodes()[i].expanded = true
        }
      },
      formatTree(tree) {
        const list =
          tree &&
          tree.map((i) => {
            return {
              id: i.id,
              label: i.name,
              isLeaf: !i.isParent,
              children: this.formatTree(i.children || []),
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
    .custom-tree-node {
      span {
        display: inline-block;
        max-width: 200px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
</style>
