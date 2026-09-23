<template>
  <el-dialog
    :default-expanded-keys="defaultExpandKeys"
    :title="title"
    :visible.sync="dialogTreeVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-tree
      ref="tree"
      v-loading="loading"
      :check-strictly="true"
      :data="data"
      :default-checked-keys="[0]"
      :default-expanded-keys="[1, 2]"
      :expand-on-click-node="false"
      highlight-current
      node-key="id"
      :props="defaultProps"
      show-checkbox
      @check-change="handleCheckChange"
      @node-click="handleNodeClick"
    />
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { getIssueModuleTree } from '@/api/setting/system'
  export default {
    name: 'FlowCategoryList',
    data() {
      return {
        loading: false,
        title: '下发',
        dialogTreeVisible: false,
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        data: [],
        moduleId: undefined,
        defaultExpandKeys: [],
      }
    },
    created() {
      // this.fetchTree()
    },
    methods: {
      formatFr(fr) {
        let arr = []
        let obj = {}
        fr.forEach((item) => {
          let tmp = { ...item }
          if (tmp.children) {
            tmp.children = this.formatFr(tmp.children)
            obj = {
              value: tmp.id,
              label: tmp.name,
              children: tmp.children,
            }
          } else {
            obj = {
              value: tmp.id,
              label: tmp.name,
            }
          }
          arr.push(obj)
        })
        return arr
      },
      showTree(row) {
        this.moduleId = row.modelId
        this.fetchTree()
        this.dialogTreeVisible = true
      },
      async fetchTree() {
        this.loading = true
        const res = await getIssueModuleTree()
        if (res) {
          this.data = this.formatFr([res])
          this.defaultExpandKeys = res.children.map((item) => item.id)
        }
        this.loading = false
      },
      handleNodeClick(data) {},
      handleCheckChange(data) {},
      close() {
        this.dialogTreeVisible = false
      },
      confirm() {
        const checked = this.$refs['tree'].getCheckedNodes()
        this.$emit('checked', checked, this.moduleId)
        this.dialogTreeVisible = false
      },
    },
  }
</script>
<style scoped>
  .top-action {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
</style>
