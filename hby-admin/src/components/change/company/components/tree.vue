<template>
  <div>
    <el-tree
      ref="companyTree"
      v-loading="loading"
      :current-node-key="activeNodeId"
      :data="data"
      :default-expanded-keys="expandedKeys"
      :expand-on-click-node="false"
      :highlight-current="true"
      lazy
      :load="fetchData"
      node-key="id"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  import { findOrganizationDataA } from '@/api/setting/org'

  export default {
    name: 'CompanyTree',
    props: {
      alwaysRoot: {
        type: Boolean,
        default: false,
      },
      notDefaultSelect: {
        type: Boolean,
        default: false,
      },
      currentNodeId: {
        type: [String, Number],
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
        orgid: '',
        data: [],
        loading: false,
        expandedKeys: [],
        nodeid: '',
        activeNodeId: '',
      }
    },
    computed: {
      defaultNodeId() {
        if (this.currentNodeId !== '' && this.currentNodeId !== null) {
          return this.currentNodeId
        }
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        return (userInfo.currentOrg && userInfo.currentOrg.orgid) || ''
      },
    },
    created() {
      this.initTree()
    },
    methods: {
      async initTree() {
        await this.fetchData()
      },
      handleNodeClick(data) {
        this.nodeid = data.id
        this.activeNodeId = data.id
        this.$emit('select', data)
      },
      setActiveNodeByTree(tree = []) {
        const matchedNode = this.findNodeById(tree, this.defaultNodeId)
        const nextNodeId = matchedNode
          ? String(matchedNode.id)
          : this.findFirstNodeId(tree)

        this.activeNodeId = nextNodeId || ''
        this.expandedKeys = this.activeNodeId ? [this.activeNodeId] : []

        this.$nextTick(() => {
          if (this.$refs.companyTree && this.activeNodeId !== '') {
            this.$refs.companyTree.setCurrentKey(this.activeNodeId)
          }
        })
      },
      findNodeById(tree = [], targetId) {
        if (targetId === '' || targetId === null || targetId === undefined) {
          return null
        }
        for (const item of tree) {
          if (String(item.id) === String(targetId)) {
            return item
          }
          const child = this.findNodeById(item.children || [], targetId)
          if (child) {
            return child
          }
        }
        return null
      },
      findFirstNodeId(tree = []) {
        for (const item of tree) {
          if (
            item &&
            item.id !== '' &&
            item.id !== null &&
            item.id !== undefined
          ) {
            return String(item.id)
          }
          const childId = this.findFirstNodeId(item.children || [])
          if (childId) {
            return childId
          }
        }
        return ''
      },
      async fetchData(node, resolve) {
        if (node && node.level === 0) {
          return
        }
        if (node && node.level === 1) {
          resolve(this.data[0] ? this.data[0].children : [])
          this.setActiveNodeByTree(this.data)
          return
        }
        this.loading = true
        try {
          const res = await findOrganizationDataA({
            nodeId: !node ? '' : node.data.id,
          })
          const tree = this.formatTree(res || [])
          if (node && node.level > 0) {
            resolve(tree[0] ? tree[0].children : [])
            return
          }
          if (!node) {
            this.data = tree
            this.setActiveNodeByTree(tree)
            if (!this.notDefaultSelect) {
              const selectedNode =
                this.findNodeById(tree, this.activeNodeId) || tree[0]
              if (selectedNode) {
                this.$emit('select', {
                  id: selectedNode.id,
                  label: selectedNode.label,
                })
              }
            }
          }
        } finally {
          this.loading = false
        }
      },
      formatTree(tree = []) {
        return tree.map((item) => ({
          id: item.id,
          label: item.name,
          isLeaf: !item.isParent,
          children: this.formatTree(item.children || []),
        }))
      },
    },
  }
</script>
