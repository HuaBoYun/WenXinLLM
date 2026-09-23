<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="70%"
    @close="close"
    :close-on-click-modal="false"
  >
    <div v-html="content"></div>
  </el-dialog>
</template>

<script>
  export default {
    name: 'LawPreview',
    data() {
      return {
        dialogFormVisible: false,
        title: '',
        content: '',
      }
    },
    methods: {
      close() {
        this.dialogFormVisible = false
      },
      showEdit(content) {
        this.content = content
        this.dialogFormVisible = true
      },
    },
  }
</script>
