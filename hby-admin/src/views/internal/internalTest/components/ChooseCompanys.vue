<template>
  <div>
    <el-dialog
      title="公司"
      :visible.sync="dialogVisible"
      width="500px"
      :append-to-body="true"
      :close-on-click-modal="false"
      v-if="dialogVisible"
    >
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </vab-query-form-right-panel>
        <vab-query-form-left-panel :span="24" style="display: flex">
          <el-input
            v-model="orgName"
            clearable
            placeholder="公司名称"
            @change="handleSearch"
            style="margin-bottom: 10px; width: 50%; margin-right: 10px"
          />
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button
            type="primary"
            @click="resetSearch"
            style="margin-bottom: 10px"
          >
            重置
          </el-button>
        </vab-query-form-left-panel>
      </vab-query-form>
      <el-tree
        ref="tree"
        v-loading="loading"
        :data="data"
        :default-expanded-keys="expandedKeys"
        :default-expand-all="false"
        :expand-on-click-node="false"
        node-key="id"
        :props="defaultProps"
        :load="fetchData"
        lazy
        @node-click="handleNodeClick"
        :show-checkbox="multiple"
        :check-strictly="true"
        @check="handleCheck"
      />
    </el-dialog>
  </div>
</template>

<script>
  import { findOrganization } from '@/api/setting/org'

  export default {
    name: 'CompanyTree',
    props: {
      multiple: {
        type: Boolean,
        default: false,
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
        orgid: '',
        data: [],
        loading: false,
        expandedKeys: [],
        dialogVisible: false,
        multipleSelection: {},
        selectedNodes: [], // 存储多选时选中的节点
        orgName: '',
        lastClickTime: 0,
        clickTimeout: 500,
      }
    },
    created() {
      this.orgid = JSON.parse(localStorage.getItem('userInfo')).currentOrg.orgid
      this.fetchData()
    },
    methods: {
      handleSearch() {
        this.queryData()
      },
      queryData() {
        if (this.orgName === '') {
          this.$refs.tree.$data.store.lazy = true
          this.fetchData()
        } else {
          this.data = []
          this.$refs.tree.$data.store.lazy = false
          this.fetchData()
        }
        this.$forceUpdate()
      },
      resetSearch() {
        this.orgName = ''
        this.$refs.tree.$data.store.lazy = true
        this.fetchData()
        this.$forceUpdate()
      },
      handleNodeClick(data) {
        if (!this.multiple) {
          const currentTime = new Date().getTime()
          const timeDiff = currentTime - this.lastClickTime
          if (timeDiff < this.clickTimeout) {
            this.$emit('submit', data)
            this.close()
          } else {
            this.multipleSelection = data
          }
          this.lastClickTime = currentTime
        }
      },
      handleCheck(data, { checkedNodes }) {
        this.selectedNodes = checkedNodes
        this.multipleSelection = {
          nodes: checkedNodes,
          current: data,
        }
      },
      async fetchData(node, resolve) {
        if (node && node.level === 0) {
          return
        }
        if (node && node.level === 1) {
          resolve(this.data[0].children)
          return
        }

        this.loading = true

        try {
          const res = await findOrganization({
            fatherorgid: !node ? this.orgid : node.data && node.data.id,
            orgname: this.orgName,
          })

          const tree = this.formatTree(res.data)

          if (node && node.level > 0) {
            resolve(tree[0].children)
            return
          }

          if (!node) {
            this.$emit('select', {
              id: tree[0].id,
              label: tree[0].name,
            })
            this.expandedKeys.push(tree[0].id)
            this.data = tree
          }

          if (this.orgName) {
            this.data = tree
            setTimeout(() => {
              this.setAllExpand()
            }, 1000)
          }
        } finally {
          this.loading = false
        }
      },
      setAllExpand() {
        const nodes = this.$refs.tree.store._getAllNodes()
        nodes.forEach((node) => {
          node.expanded = true
        })
      },
      showEdit() {
        this.orgName = ''
        this.dialogVisible = true
        this.fetchData()
      },
      save() {
        if (this.multiple) {
          if (!this.selectedNodes.length) {
            this.$baseMessage(
              '请选择至少一个单位！',
              'error',
              'vab-hey-message-error'
            )
            return
          }
          this.$emit('handleChooseCompany', this.selectedNodes)
        } else {
          if (!this.multipleSelection.label) {
            this.$baseMessage('请选择单位！', 'error', 'vab-hey-message-error')
            return
          }
          this.$emit('handleChooseCompany', this.multipleSelection)
        }
        this.close()
      },
      close() {
        this.dialogVisible = false
        this.multipleSelection = {}
        this.selectedNodes = []
      },
      formatTree(tree) {
        return tree.map((i) => ({
          id: i.id,
          label: i.name,
          isLeaf: !i.isParent,
          children: this.formatTree(i.children || []),
        }))
      },
    },
  }
</script>
