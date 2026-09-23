<template>
  <el-dialog
    :modal-append-to-body="false"
    :modal="false"
    :visible.sync="dialogVisible"
    width="30%"
    :close-on-click-modal="false"
  >
    <ManagerTree
      @changeNode="changeNode"
      :defaultExpandedH="defaultExpandedH"
    ></ManagerTree>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </span>
  </el-dialog>
</template>
<script>
  import ManagerTree from './ManageTree.vue'
  export default {
    name: 'ManageTreeDialog',
    props: {
      defaultExpandedH: {
        type: Number,
        default: 3,
      },
    },
    components: { ManagerTree },
    data() {
      return {
        dialogVisible: false,
        currentNode: {},
      }
    },
    methods: {
      confirm() {
        if (this.currentNode.id === undefined) {
          this.$message.warning('请选择')
          return
        }
        this.$emit('selectNode', this.currentNode)
        this.dialogVisible = false
      },
      changeNode(node) {
        this.currentNode = node
      },
      showDialog() {
        this.dialogVisible = true
      },
    },
  }
</script>
