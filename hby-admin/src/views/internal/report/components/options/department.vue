<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogTreeVisible"
    width="500px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-tree
      ref="tree"
      :check-strictly="true"
      :data="data"
      :show-checkbox="multiSelect"
      default-expand-all
      :expand-on-click-node="false"
      highlight-current
      node-key="id"
      :props="defaultProps"
      @check-change="handleCheckChange"
      @node-click="handleNodeClick"
    />
  </el-dialog>
</template>

<script>
  import { getExecutorOptionsTree } from '@/api/contract/manage'
  export default {
    name: 'DepartmentOptions',
    props: {
      multiSelect: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        title: '部门',
        dialogTreeVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        data: [],
        queryForm: {
          pid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        indexNumber: -1,
      }
    },
    created() {},
    methods: {
      show(e) {
        if (e !== undefined) {
          this.indexNumber = e
        }
        this.dialogTreeVisible = true
        this.fetchTree()
      },
      async fetchTree() {
        const res = await getExecutorOptionsTree(this.queryForm)
        this.data = res
      },
      handleNodeClick(data) {
        this.queryForm.pageNumber = 1
        this.queryForm.pid = val.id
        console.log(data)
      },
      handleCheckChange(data) {
        console.log(data)
      },
      close() {
        this.dialogTreeVisible = false
      },
      confirm() {
        const checked = this.$refs['tree'].getCurrentNode()
        console.log(checked)
        this.$emit('selected', checked)
        if (this.multiSelect) {
          const checkeds = this.$refs['tree'].getCheckedNodes()
          this.$emit('selectByTable', {
            index: this.indexNumber,
            row: checkeds,
          })
        }
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
