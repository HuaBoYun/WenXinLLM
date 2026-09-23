<!--
 * @Date: 2022-03-28 15:04:10
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 17:05:56
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/options/department.vue
-->
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
      default-expand-all
      :show-checkbox="checkbox"
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
  import { findOrganizationByTreeAllss } from '@/oapi/audit/implement'
  export default {
    name: 'DepartmentOptions',
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
          pageNumber: 1,
          pageSize: 20,
        },
        checkbox: false,
      }
    },
    created() {},
    methods: {
      show(checkbox) {
        this.checkbox = checkbox
        this.dialogTreeVisible = true
        this.fetchTree()
      },
      async fetchTree() {
        const res = await findOrganizationByTreeAllss(this.queryForm)
        this.data = res
      },
      handleNodeClick(data) {
        //
      },
      handleCheckChange(data) {
        //
      },
      handleSelected(val) {},
      close() {
        this.dialogTreeVisible = false
      },
      confirm() {
        //
        if (this.checkbox) {
          const checkList = this.$refs['tree'].getCheckedNodes()
          this.$emit('selected', checkList, true)
        } else {
          const checked = this.$refs['tree'].getCurrentNode()

          this.$emit('selected', checked)
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
