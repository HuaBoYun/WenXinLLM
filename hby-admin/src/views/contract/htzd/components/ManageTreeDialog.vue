<template>
  <el-dialog
    :modal-append-to-body="false"
    :modal="false"
    :visible.sync="dialogVisible"
    width="30%"
    :close-on-click-modal="false"
  >
    <ManagerTree @changeNode="changeNode"></ManagerTree>
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
    components: { ManagerTree },
    data() {
      return {
        dialogVisible: false,
        currentNode: {},
      }
    },
    methods: {
      //前置校验
      confirm() {
        if (this.currentNode.id === undefined) {
          this.$message.warning('请选择')
          return
        }
        this.$emit('selectNode', this.currentNode)
        this.dialogVisible = false
      },
      //回调
      changeNode(node) {
        this.currentNode = node
      },
      showDialog() {
        this.dialogVisible = true
      },
    },
  }
</script>
