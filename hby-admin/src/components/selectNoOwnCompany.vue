<template>
  <el-dialog
    append-to-body
    v-loading="loading"
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogTreeVisible"
    width="600px"
    @close="close"
  >
    <div class="content">
      <!-- 将按钮移到头部 -->
      <div class="action-area" style="margin-bottom: 15px">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </div>

      <el-input
        v-model="orgName"
        clearable
        placeholder="公司名称"
        @change="change()"
        style="margin-bottom: 10px; width: 60%; margin-right: 10px"
      />
      <el-button icon="el-icon-search" type="primary" @click="queryData">
        查询
      </el-button>
      <el-button
        type="primary"
        @click="resetSearch"
        style="margin-bottom: 10px"
      >
        重置
      </el-button>
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
        :show-checkbox="multiple"
        :check-strictly="true"
      />
    </div>
  </el-dialog>
</template>

<script>
  import { getAllOrgInfoTree } from '@/api/setting/org'

  export default {
    name: 'DepTree',
    props: {
      isAll: {
        type: Boolean,
        default: false,
      },
      multiple: {
        type: Boolean,
        default: false,
      },
      title: {
        type: String,
        default: '部门',
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
        dialogTreeVisible: false,
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
      show(keys = []) {
        if (this.multiple && keys) {
          this.keys = keys
        }
        this.dialogTreeVisible = true
        this.fetchData()
      },
      resetSearch() {
        this.orgName = ''
        this.$refs.tree.$data.store.lazy = true // 开启懒加载
        this.fetchData()
        this.$forceUpdate()
      },
      // handleNodeClick(data) {
      //   console.log(data)
      //   this.nodeid = data.id
      //   this.$emit('select', data)
      // },
      handleNodeClick(data) {
        if (!this.multiple) {
          this.$refs.tree.setCurrentKey(data.id)
        }
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

        getAllOrgInfoTree({
          fatherorgid: !node ? '' : node.data && node.data.id,
          orgname: this.orgName,
          status: this.isAll ? null : 0,
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
              this.$emit('select', {
                id: tree[0].id,
                label: tree[0].name,
              })
              this.expandedKeys.push(tree[0].id)
              console.log('tree:lll', tree)
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
      confirm() {
        let selected
        if (!this.multiple) {
          // 单选模式 - 获取当前选中的节点
          selected = this.$refs.tree.getCurrentNode()
          console.log('单选模式选中的节点:', selected) // 添加调试日志

          // 如果没有选中节点，提示用户
          if (!selected) {
            this.$message.warning('请先选择一个部门')
            return
          }
        } else {
          // 多选模式 - 获取所有选中的节点
          selected = this.$refs.tree.getCheckedNodes()
          console.log('多选模式选中的节点:', selected) // 添加调试日志

          // 如果没有选中节点，提示用户
          if (!selected || selected.length === 0) {
            this.$message.warning('请至少选择一个部门')
            return
          }
        }

        this.$emit('selected', selected)
        this.dialogTreeVisible = false
        this.searchText = ''
      },
      close() {
        this.dialogTreeVisible = false
        this.searchText = ''
      },
    },
  }
</script>
<style lang="scss" scoped>
  .content {
    width: 500px;
  }
  ::v-deep {
    .el-tree {
      width: 100%;
      overflow: scroll;
      height: calc(100vh - 320px); /* 调整高度，为头部按钮留出空间 */
      overflow-x: auto;
    }
    .el-tree > .el-tree-node {
      display: inline-block;
      min-width: 100%;
    }
  }
  .action-area {
    display: flex;
    justify-content: flex-end;
  }
</style>
