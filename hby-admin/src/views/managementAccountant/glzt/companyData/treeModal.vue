<template>
  <el-dialog
    :visible.sync="visible"
    title="财务组织"
    width="500px"
    :close-on-click-modal="false"
    :append-to-body="true"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button type="primary" @click="close">取消</el-button>
        <el-button type="primary" @click="confirm">确定</el-button>
      </vab-query-form-right-panel>
      <vab-query-form-left-panel :span="24">
        <div style="display: flex; align-items: center">
          <el-input
            v-model="queryForm.shortname"
            style="width: 200px; margin-right: 10px"
            placeholder="请输入财务组织名称"
            clearable
          />

          <el-button type="primary" @click="getTreeList">查询</el-button>
        </div>
      </vab-query-form-left-panel>
    </vab-query-form>
    <el-tree
      :data="treeList"
      :props="{
        label: 'name',
        children: 'childrenList',
      }"
      node-key="pkOrg"
      highlight-current
      :expand-on-click-node="false"
      @node-click="handleNodeClick"
    ></el-tree>
  </el-dialog>
</template>
<script>
  import { getCwzzTreeList } from '@/api/cwsc'
  export default {
    name: 'TreeModal',
    data() {
      return {
        visible: false,
        treeList: [],
        queryForm: {
          shortname: '',
        },
      }
    },
    methods: {
      showEdit() {
        this.visible = true
        this.getTreeList()
      },
      getTreeList() {
        getCwzzTreeList(this.queryForm).then((res) => {
          this.treeList = res.data
        })
      },
      close() {
        this.visible = false
      },
      confirm() {
        if (this.selectedNode) {
          // 将选中的节点数据传递给父组件
          this.$emit('select', this.selectedNode)
          this.visible = false
        } else {
          this.$message.warning('请选择财务组织')
        }
      },
      handleNodeClick(node) {
        this.selectedNode = node
      },
    },
  }
</script>
