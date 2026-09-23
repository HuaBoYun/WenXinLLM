<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="100%"
    @close="close"
    :close-on-press-escape="false"
    :close-on-click-modal="false"
    :fullscreen="true"
  >
    <fc-designer ref="designer"/>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="close">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  export default {
    name: 'CustomFormEdit',
    data() {
      return {
        title: '自定义表单',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      show(){
        this.dialogFormVisible = true
      },
      close(){
        this.dialogFormVisible = false
      }
    },
  }
</script>
