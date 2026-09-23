<!--
 * @Date: 2022-04-27 10:22:15
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-05 16:32:24
 * @FilePath: /hb-admin/src/views/setting/system/components/Department.vue
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
      :expand-on-click-node="false"
      highlight-current
      node-key="id"
      :props="defaultProps"
      :show-checkbox="multiple"
      @check-change="handleCheckChange"
      @node-click="handleNodeClick"
    />
  </el-dialog>
</template>

<script>
  import { getTreeLevel } from '@/api/setting/system'

  export default {
    name: 'DepartmentOptions',
    data() {
      return {
        title: '部门',
        dialogTreeVisible: false,
        defaultProps: {
          children: 'children',
          label: 'text',
          value: 'id',
        },
        data: [],
        field: undefined,
        multiple: false,
      }
    },
    created() {},
    methods: {
      show(options) {
        if (options) {
          const { field, multiple } = options
          this.field = field
          this.multiple = multiple
        }
        this.dialogTreeVisible = true
        this.fetchTree(1)
      },
      async fetchTree(level) {
        const res = await getTreeLevel({ str: 1 })
        this.data = [res]
        // this.options = [
        //   {
        //     value: res.id,
        //     label: res.text,
        //     children: [],
        //   },
        // ]
        //
      },
      handleNodeClick(data) {},
      handleCheckChange(data, checked, indeterminate) {},
      close() {
        this.dialogTreeVisible = false
      },
      confirm() {
        const checked = this.multiple
          ? this.$refs['tree'].getCheckedNodes()
          : this.$refs['tree'].getCurrentNode()
        this.$emit('selected', {
          checked,
          field: this.field,
          multiple: this.multiple,
        })
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
