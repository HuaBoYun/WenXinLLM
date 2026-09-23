<template>
  <div>
    <el-dialog
      :title="title"
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
            style="margin-bottom: 10px; width: 50%; margin-right: 10px"
          />
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button type="primary" @click="resetSearch">重置</el-button>
        </vab-query-form-left-panel>
      </vab-query-form>

      <el-tree
        ref="tree"
        v-loading="loading"
        :data="data"
        :default-expanded-keys="expandedKeys"
        :default-checked-keys="checkedKeys"
        :default-expand-all="false"
        :expand-on-click-node="false"
        :show-checkbox="true"
        :check-strictly="true"
        node-key="id"
        :props="defaultProps"
        :load="fetchData"
        lazy
        @check="handleCheck"
      />
    </el-dialog>
  </div>
</template>

<script>
  import { getAllOrgInfoTree } from '@/api/setting/org'

  export default {
    name: 'MultiDepartmentSelect',
    props: {
      title: {
        type: String,
        default: '选择单位',
      },
      defaultChecked: {
        type: Array,
        default: () => [],
      },
    },
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
          isLeaf: 'isLeaf',
        },
        orgid: '',
        data: [],
        loading: false,
        expandedKeys: [],
        checkedKeys: [],
        dialogVisible: false,
        selectedNodes: [],
        orgName: '',
      }
    },
    created() {
      this.orgid = JSON.parse(localStorage.getItem('userInfo')).currentOrg.orgid
    },
    methods: {
      show(checkedKeys = []) {
        this.dialogVisible = true
        this.checkedKeys = checkedKeys
        this.fetchData()
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

      handleCheck(data, { checkedNodes }) {
        this.selectedNodes = checkedNodes
      },

      async fetchData(node, resolve) {
        if (node && node.level === 0) return
        if (node && node.level === 1) {
          resolve(this.data[0].children)
          return
        }

        this.loading = true
        try {
          const res = await getAllOrgInfoTree({
            fatherorgid: !node ? this.orgid : node.data?.id,
            orgname: this.orgName,
          })

          const tree = this.formatTree(res.data)

          if (node && node.level > 0) {
            resolve(tree[0].children)
            return
          }

          if (!node) {
            this.data = tree
            this.expandedKeys = [tree[0].id]
          }

          if (this.orgName) {
            this.data = tree
            setTimeout(() => this.setAllExpand(), 1000)
          }
        } finally {
          this.loading = false
        }
      },

      setAllExpand() {
        this.$refs.tree.store._getAllNodes().forEach((node) => {
          node.expanded = true
        })
      },

      save() {
        if (!this.selectedNodes.length) {
          this.$baseMessage(
            '请至少选择一个单位！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('submit', this.selectedNodes)
        this.close()
      },

      close() {
        this.dialogVisible = false
        this.selectedNodes = []
        this.checkedKeys = []
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
