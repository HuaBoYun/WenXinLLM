<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      @close="close"
      fullscreen
    >
      <iframe
        src="http://192.0.2.19:8081/"
        style="width: 100%; height: 670px"
      ></iframe>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        dialogFormVisible: false,
        title: '拖拽分析',
      }
    },
    methods: {
      showEdit() {
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
