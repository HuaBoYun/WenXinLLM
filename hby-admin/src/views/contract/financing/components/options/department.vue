<!--
 * @Date: 2022-03-28 15:04:10
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-03-29 09:56:44
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/options/department.vue
-->
<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogTreeVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-tree
      ref="tree"
      :check-strictly="true"
      :data="data"
      default-expand-all
      :expand-on-click-node="false"
      highlight-current
      node-key="id"
      :props="defaultProps"
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
  import { getDepartmentOptions } from '@/api/contract/manage'
  export default {
    name: 'DepartmentOptions',
    data() {
      return {
        title: '执行部门',
        dialogTreeVisible: false,
        defaultProps: {
          children: 'children',
          label: 'text',
          value: 'id',
        },
        data: [],
      }
    },
    created() {},
    methods: {
      show() {
        this.dialogTreeVisible = true
        this.fetchTree()
      },
      async fetchTree() {
        const res = await getDepartmentOptions()
        this.data = res
      },
      handleNodeClick(data) {
        this.$emit('selectedde', data)
        this.close()
      },
      handleCheckChange(data) {},
      close() {
        this.dialogTreeVisible = false
      },
      confirm() {
        const checked = this.$refs['tree'].getCurrentNode()
        this.$emit('selected', checked)
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
