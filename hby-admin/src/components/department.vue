<template>
  <div>
    <el-dialog
      title="单位"
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
      </vab-query-form>
      <el-tree
        v-loading="loading"
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
    </el-dialog>
  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  import { findOrganization } from '@/api/setting/org'

  export default {
    name: 'CompanyTree',
    props: {
      alwaysRoot: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      return {
        defaultProps: {
          children: 'children',
          label: 'name',
          isLeaf: (data) => !data.isParent,
        },
        list: [],
        orgid: '',
        data: [],
        loading: false,
        expandedKeys: [userInfo.currentOrg.orgid],
        dialogVisible: false,
        multipleSelection: {},
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleNodeClick(data) {
        this.multipleSelection = data
      },
      fetchData(node, resolve) {
        if (node && node.level === 0) {
          return
        }
        if (node && node.level === 1) {
          resolve(this.data[0].children)
          return
        }

        let params = {}
        if (node && node.data) {
          params.fatherorgid = node.data.id
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const curOrgId = userInfo.currentOrg.orgid
          params.fatherorgid = curOrgId
        }
        this.loading = true
        findOrganization(params)
          .then(({ data }) => {
            if (!node) return (this.data = data)
            if (node && node.level > 0) {
              resolve(data[0].children)
            }
          })
          .finally(() => {
            this.loading = false
          })
      },
      showEdit() {
        this.dialogVisible = true
        this.fetchData()
        // this.getExecutorTree()
      },
      save() {
        if (!this.multipleSelection.name) {
          this.$baseMessage('请选择单位！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('submit', this.multipleSelection)
        this.close()
      },
      close() {
        this.dialogVisible = false
        this.multipleSelection = {}
      },
    },
  }
</script>
