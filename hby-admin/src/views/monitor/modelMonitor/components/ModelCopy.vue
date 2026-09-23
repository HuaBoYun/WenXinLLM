<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    destroy-on-close
    @close="close"
  >
    <el-tree
      node-key="orgid"
      :data="treeList"
      :props="defaultProps"
      default-expand-all
      highlight-current
      :expand-on-click-node="false"
      @node-click="handleNodeClick"
    />
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary" :loading="loading" @click="save">
        确 定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
  import {
    getModelManageIndustry,
    copyModelManageIndustry,
  } from '@/api/monitor/model'
  import lodash from 'lodash'

  export default {
    name: 'ModelCopy',
    data() {
      return {
        title: '模型管理 - 复制到行业规则库',
        dialogFormVisible: false,
        treeList: [],
        defaultProps: {
          label: 'orgname',
        },
        loading: false,
        orgid: '',
        modelid: '',
      }
    },
    methods: {
      async getList() {
        const { code, msg, data } = await getModelManageIndustry({
          orgid: '',
          type: 1,
        })
        if (code === 200 && msg === 'success') {
          const { orgTree } = data
          this.treeList = this.generateTree(orgTree)
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
      },
      showEdit(modelid) {
        this.dialogFormVisible = true
        this.modelid = modelid
        this.getList()
      },
      close() {
        this.dialogFormVisible = false
      },
      generateTree(list) {
        const group = lodash.groupBy(list, 'fatherorgid')
        const formatTree = (items, pid, key) => {
          const result = []
          if (!items[pid]) {
            return result
          }
          for (let t of items[pid]) {
            const temp = formatTree(items, t[key], key)
            if (temp.length) t.children = temp
            result.push(t)
          }
          return result
        }
        return formatTree(group, '-1', 'orgid')
      },
      handleNodeClick({ orgid }) {
        this.orgid = orgid
      },
      async save() {
        this.loading = true
        const { code, msg } = await copyModelManageIndustry({
          modelid: this.modelid,
          orgid: this.orgid,
        })
        if (code === 200 && msg === '成功') {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.close()
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
        this.loading = false
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
